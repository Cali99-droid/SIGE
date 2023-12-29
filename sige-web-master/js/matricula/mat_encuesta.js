//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	
}

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	listar_tabla();	
}


//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_conf_encuesta_modal.html" id="mat_conf_mensualidad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Configuraci&oacute;n de encuesta</a></li>');

	$('#mat_conf_mensualidad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		var anio = $("#_ID_ANIO").text();
        $(this).data('anio' ,anio);
		conf_encuesta_modal($(this));
	});

	//lista tabla
	listar_tabla();
});

function conf_encuesta_eliminar(link){
	_delete('api/encuesta/' + $(link).data("id"),
			function(){
					listar_tabla();
				}
			);
}

function conf_encuesta_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_conf_encuesta_modal.html');
	conf_encuesta_modal(link);
	
}

function agregar_preguntas(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/matricula/mat_preguntas_modal.html');
	//link.data('id_com',$('#col_competencia_full-frm #id').val());
	link.data('id_enc',$('#mat_conf_encuesta-frm #id').val());
	console.log($('#mat_conf_encuesta-frm #id').val());
	conf_preguntas_modal(link);
}

function conf_encuesta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_conf_encuesta-frm');
		$('#mat_conf_encuesta-frm #id_anio').val($('#_id_anio').text());
		$('#mat_conf_encuesta-frm #btn-agregar_pre').hide();
		if (link.data('id')){
			_get('api/encuesta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				$('#mat_conf_encuesta-frm #id').val(data.id);
				$('#mat_conf_encuesta-frm #nom').val(data.nom);
				$('#mat_conf_encuesta-frm #fec_ini').val(_parseDate(data.fec_ini));
				$('#mat_conf_encuesta-frm #fec_fin').val(_parseDate(data.fec_fin));
				$('#mat_conf_encuesta-frm #msj_ini').val(data.msj_ini);
				$('#mat_conf_encuesta-frm #msj_fin').val(data.msj_fin);
				_llenarCombo('ges_giro_negocio',$('#id_gir'), data.id_gir);
				$('#mat_conf_encuesta-frm #btn-agregar_pre').show();
			});
		}else{
			_llenarCombo('ges_giro_negocio',$('#id_gir'));
			//$('#mat_conf_mensualidad-frm #btn-grabar').hide();
		}
		
		$('#mat_conf_encuesta-frm #btn-grabar_enc').on('click',function(event){
			_post($('#mat_conf_encuesta-frm').attr('action') , $('#mat_conf_encuesta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					$('#mat_conf_encuesta-frm #id').val(data.result);
				}
			);
		});
		
		/*$('#btn-agregar_pre').on('click', function(event){
			
			//var link = $(row);
			//link.attr('href','pages/matricula/mat_preguntas_modal.html');
			conf_preguntas_modal(link);
			
		});	*/
		
		/*$('#mat_conf_mensualidad-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_mensualidad-frm #id').val('');
			_post($('#mat_conf_mensualidad-frm').attr('action') , $('#mat_conf_mensualidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/confMensualidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('per_periodo',$('#id_per'), data.id_per);
			});
		}else{
			_llenarCombo('per_periodo',$('#id_per'));
			$('#mat_conf_mensualidad-frm #btn-grabar').hide();
		}*/
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		listar_tabla();
		$('#mat_conf_encuesta-frm #btn-agregar_pre').show();
		//conf_mensualidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de Encuesta (A&ntilde;o ' +  link.data('anio')+ ')';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de Encuesta (A&ntilde;o ' +  link.data('anio')+ ')';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function conf_preguntas_modal(link){
	//link.attr('href','pages/matricula/mat_conf_mensualidad_modal.html');
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_conf_mensualidad-frm');
		
		$('#encuesta_pregunta_form #id_enc').val(link.data('id_enc'));
		
		if (link.data('id_enc')){
			alert();
			//Lista de Preguntas
			var param = {id_enc: link.data('id_enc')};
			_get('api/encuesta/listarPreguntasAlt',
			function(data){
				var cant_pre=data.length;
				console.log(cant_pre);
				for(i=0; i<cant_pre; i++){
					
							var last_pregunta=$("#pregunta").last();
							//console.log(last.html());
							divPregunta = last_pregunta.clone();
							$("#divCuestionario").append(divPregunta);
							$( divPregunta ).find("#pre").val(data[i].pre);
						var alternativas=data[i].encuestaAlts;
						if(alternativas){
							var cant_alt=alternativas.length;
							for(i=0; i<cant_alt; i++){
								var last=$("#alternativa").last();
								console.log(last.html());
								divAlternativa = last.clone(false);
								//$("#divCuestionario").append(divPregunta);
								//var divClonado = $('#cuestionarioClonar').clone();
								//var hijo = divPregunta.children().first();
								//var hermano =hijo.next();
								console.log($('#divCuestionario').html());
								var divCopyHijo=$(divAlternativa).insertAfter($('#pregunta').children().last());
								//var divCopyHer= $(hermano).insertAfter($('#divCuestionario').children().last());
							}	
							
						}	
							//Para el acta de compromiso tipo Alumno
							/*var last_est=$(".div_estudiantes").last();
							divEstuadiante= last_est.clone(false);
							$("#estudiantes").append(divEstuadiante);
							
							//Para el acta de compromiso tipo Familiar
							var last_fam=$(".div_familiares").last();
							divFamiliar= last_fam.clone(false);
							$("#familiares").append(divFamiliar);
							var last_res=$(".div_responsable").last();
							divResponsable= last_res.clone(false);
							$("#responsables").append(divResponsable);
							
							//Para la derivacion
							var last_inf=$(".div_estudiantes_inf").last();
							divEstuadianteInf= last_inf.clone(false);
							$("#estudiantes_inf").append(divEstuadianteInf);*/
			} 
			}, param);
		} else {
		}	
		
		/*$('#mat_conf_mensualidad-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_mensualidad-frm #id').val('');
			_post($('#mat_conf_mensualidad-frm').attr('action') , $('#mat_conf_mensualidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/confMensualidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('per_periodo',$('#id_per'), data.id_per);
			});
		}else{
			_llenarCombo('per_periodo',$('#id_per'));
			$('#mat_conf_mensualidad-frm #btn-grabar').hide();
		}*/
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		//conf_mensualidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de Preguntas (A&ntilde;o ' +  link.data('anio')+ ')';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de Preguntas (A&ntilde;o ' +  link.data('anio')+ ')';
	
	_modal(titulo, link.attr('href') ,onShowModal,onSuccessSave);

}

function agregarPre(btn){

var divClonado = $('#cuestionarioClonar').clone();
var hijo = divClonado.children().first();
var hermano =hijo.next();
console.log($('#divCuestionario').html());
var divCopyHijo=$(hijo).insertAfter($('#divCuestionario').children().last());
var divCopyHer= $(hermano).insertAfter($('#divCuestionario').children().last());

$( divCopyHijo ).find("#pre").val("");
$( divCopyHijo ).find("#id_pre").val("");
$( divCopyHijo ).find("#ord").val("");
$( divCopyHer ).find("#alt").val("");
$( divCopyHer ).find("#id_alt").val("");
$( divCopyHer ).find("#punt").val("");
}

function agregarAlternativa(btn){
	//obtener div a clonar
	var divSeleccionado = btn.parentNode.parentNode;
	var divNuevo = $(divSeleccionado).clone();
	$( divNuevo ).insertAfter(divSeleccionado);
	$( divNuevo ).find("#alt").val("");
	$( divNuevo ).find("#id_alt").val("");
	$( divNuevo ).find("#punt").val("");
	//$( divNuevo ).find("#btnagregar").remove();
	
	
}

function grabarPre(btn){
	var divSeleccionado = btn.parentNode.parentNode.parentNode;
	var id_enc=$('#encuesta_pregunta_form #id_enc').val();
	var id_pre=$(divSeleccionado).find("#id_pre").val();
	//$(divSeleccionado).find("#id_pre").val(id_pre); 
	var pre=$(divSeleccionado).find("#pre").val();
	var ord=$(divSeleccionado).find("#ord").val();
	var est=$(divSeleccionado).find("#est").val();
	//console.log($(divSeleccionado).html());
	var id_ctp=$(divSeleccionado).find("#id_ctp").val();
	var dep=$(divSeleccionado).find("#dep").val();
	var id_pre_dep=$(divSeleccionado).find("#id_pre_dep").val();
	
	var json_pregunta={};
	json_pregunta.id_enc=id_enc;
	json_pregunta.id_pre=id_pre;
	json_pregunta.pre=pre;
	json_pregunta.ord=ord;
	json_pregunta.est=est;
	json_pregunta.id_ctp=id_ctp;
	json_pregunta.dep=dep;
	json_pregunta.id_pre_dep=id_pre_dep;
	console.log(json_pregunta);
	 //var param = { id_enc: id_enc, id_pre:id_pre, pre:pre, ord:ord, est:est, id_ctp:id_ctp, dep:dep, id_pre_dep:id_pre_dep}
	// 'id_enc':id_enc,'id_pre': id_pre, 'pre':pre}
	 
	 _post($('#encuesta_pregunta_form').attr('action') , json_pregunta,
			function(data){
					//onSuccessSave(data) ;
					//$('#mat_conf_encuesta-frm #id').val(data.result);
				}
			);
			
	/*_POST({
			context:_URL,
			url:'api/encuesta/grabarPreguntas',
			params:json_pregunta,
			contentType:"application/json",
			success: function(data){
				console.log(data);
				alert('Se grabo correctamente');
				//$("#id_ent_res").val(data.result);	
				//$("#div_compromiso_alu #des").val($("#div_entrevista_res #des").val());
				//$("#div_compromiso_alu #fam").val($("#div_entrevista_res #des").val());
				//$("#div_entrevista_res #des").attr('readonly', true);
				//$("#div_entrevista_res #btn_grabar").attr('disabled','disabled');
				
			}
	});	*/
	/*var URL = "CriterioPreguntaGrabar?id_per="+id_per+"&pre="+pre+"&ord="+ord+"&est="+est+"&id_pre="+id_pre;
	$.ajax({
	type: "GET",
	data : $(divSeleccionado).find("#pregunta").serialize(),
	url: URL,
	}).done(function( result ) {
		console.log(result);
		//alert(result.id)
		$(divSeleccionado).find("#id_pre").val(result.id);
		alert('Se grabo correctamente');
	})*/

   
}


function eliminarAlternativa(btn){
	//obtener div a clonar
	
	var divSeleccionado = btn.parentNode.parentNode;
	$( divSeleccionado ).remove();
}

function listar_tabla(){
	var param = {id_anio:$('#_id_anio').text()};
	_get('api/encuesta/listar',
			function(data){
			console.log(data);
				$('#mat_encuesta-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Giro Negocio", "data": "giroNegocio.nom"},
			        	 	{"title":"Nombre", "data": "nom"},
							{"title":"Fecha Inicio", "data" : "monto","render":function( data, type, row,meta){
			        	 		return _parseDate(row.fec_ini);
			        	 	}}, 
			        	 	{"title":"Fecha Final", "data" : "descuento","render":function( data, type, row,meta){
			        	 		return _parseDate(row.fec_ini);
			        	 	}},  
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="conf_encuesta_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="conf_encuesta_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

