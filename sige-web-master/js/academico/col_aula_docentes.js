//Se ejecuta cuando la pagina q lo llama le envia parametros
var docentes=null;
var cap_seleccionado=null;
var des_seleccionado=null;
var id_anio=$("#_id_anio").text();
var id_tra=_usuario.id_tra;
function onloadPage(params){
	_onloadPage();
	//Cargar los combos
	$('#id_au').on('change',function(event){
		listar_curso_aula($("#_id_anio").text(),$('#id_niv').val(),$('#id_tpe').val(),$('#id_gir').val(),$('#id_grad').val(),$('#id_au').val(),$('#id_prof').val());
	});	
	
	$('#id_grad').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_tra').on('change',function(event){
		$('#id_au').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_grad').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		_get('api/trabajador/listarTodosTrabajadores',function(data){
			docentes=data;
		});	
	});	
	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_niv').on('change',function(event){
		var id_niv=$('#id_niv').val();
		_llenarCombo('cat_grad_todos',$('#id_grad'),null,id_niv,function(){$('#id_grad').change();});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	/*$('#id_gir').on('change',function(event){
		_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){
			$('#id_niv').change();
		});
	});*/
	$('#id_gir').on('change',function(event){
		/*_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){
			$('#id_niv').change();
		});*/
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){
				$('#id_niv').find('option[value=""]').remove();
				$('#id_niv').change();
				});
		} else {
			_llenarComboURL('api/disenioCurricular/listarNivelesComboxGiro/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){$('#id_niv').change();});
		}	
	});
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarComboURL('api/aula/listarGiroNegocio',$('#id_gir'),null,null,function(){$('#id_gir').change();});
	}
	
	//_llenarCombo('cat_tip_periodo',$('#id_tpe'),null,null,function(){$('#id_tpe').change();});
	//_llenarComboURL('api/aula/listarGiroNegocio',$('#id_gir'),null,null,function(){$('#id_gir').change();});
	_llenarComboURL('api/trabajador/listarTodosTrabajadores',$('#id_prof'),null,function(){
		//docentes=data;
	});
	
}

function listar_curso_aula(id_anio, id_niv, id_tpe, id_gir, id_grad, id_au, id_tra){
	var param={id_anio:id_anio, id_niv:id_niv, id_tpe:id_tpe,id_gir:id_gir,id_grad:id_grad, id_au:id_au, id_tra:id_tra};
	_get('api/cursoAula/listaCursosAulaxNivelAnio',function(data){
	if(data.length>0){
	var id_au_pri=data[0].id;
	au_seleccionado=id_au_pri;
	} else {
		au_seleccionado=0;
	}	
	$('#tabla_curso_aulas').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			{"title":"Aula", "data": "aula"},
			{"title":"Área", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id_caa" name="id_caa" value="'+row.id_caa+'"/>'+row.area 
			}},
			{"title":"Curso", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id_cua" name="id_cua" value="'+row.id_cua+'"/>'+row.curso 
			}},
			{"title": "Profesor", "render":function ( data, type, row,meta ) {
	     	        	  var combo; 
	     	        		   combo = _getComboJSON( docentes,row.id_tra,row.id_caa, row.id_cua, row.id_caul, row.id_au);     	        	   
	     	        	   return combo;
	     	 } }
			
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_matriculados(au_seleccionado);
				 
				}
			});
	}, param);
}

function _getComboJSON( data,id_tra, id_caa, id_cua, id_cual, id_au ){

	
	var s = $('<select/>');
	if (id_tra==null)
		id_tra = '';
	
	$('<option />', {value: '-1', text: 'seleccione'}).appendTo(s);
	 
	 for ( var i in data) {
		 var id = data[i].id;
		 var value = data[i].value;
			
	    //$('<option data-id_secc_sug="' + _id  + '" />', {value: id, text: value}).appendTo(s);
	    //$('<option/>', {value: id, text: value}).appendTo(s);
		 if (id_tra!='' && id==id_tra )
			 s.append('<option selected value="'+ id + '">' + value + '</option>');
		 else
			 s.append('<option value="'+ id + '">' + value + '</option>');
 
	}
	 
	 return '<select class="form-control select-search" data-id_caa="' + id_caa+ '"  data-id_cua="' + id_cua + '" data-id_cual="' + id_cual + '" data-id_au="' + id_au + '" onchange="onchangeCombo(this)">' +  s.html() + '</select>';	  
}

function onchangeCombo( field){
	var combo = $(field);
	var id_tra = combo.val();
	var id_caa = combo.data('id_caa');
	var id_cua = combo.data('id_cua');
	var id_caul = combo.data('id_cual');//de la bd
	var id_au = combo.data('id_au');
	
	if (id_tra=='')
		id_tra = null;
	
	var param ={id_caa:id_caa,id_cua:id_cua,id_au:id_au,id_tra:id_tra,id_caul:id_caul};
	
	swal(
			{
				title : "Esta seguro?",
				text :'Se asignará al trabajador como docente',
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, Asignar",
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					console.log(param);
					_post('api/cursoAula/grabarCursosAula',param, function(data){
						 console.log(data.result);
						 combo.data('id_cual',data.result);
						 $('#id_au').change();
					});
						 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha asignado al docente",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
					
					combo.val(id_caul);//regresa a su estado anterior
				}
			});
	

}

function aula_detalle_modal(id_au){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		//Obtener los datos del aula
		$('#col_aula_detalle-frm #id_au').val(id_au);
		_GET({url:'api/aula/obtenerDatosAula/' +id_au,
		success:	function(data){
		$('#nivel') .val(data.nivel); 
		$('#grado') .val(data.grado);
		$('#secc') .val(data.secc);
		$('#matriculados') .val(data.matriculados);
		$('#trasladados') .val(data.trasladados);
		}	  
		});
		
		
		_llenarComboURL('api/trabajador/listarTodosTrabajadores',$('#id_tut'),null);
		_llenarComboURL('api/trabajador/listarTodosTrabajadores',$('#id_aux'),null);
		_llenarComboURL('api/trabajador/listarTodosTrabajadores',$('#id_cord'),null);
		
		_get('api/aula/obtenerDatosDetalleAula/' + id_au,
			function(data){
				_fillForm(data,$('#modal').find('form') );
				if(data){
					$('#id_tut').val(data.id_tut).change();
					$('#id_aux').val(data.id_aux).change();
					$('#id_cord').val(data.id_cord).change();
				}	
		});
		//_inputs('col_curso_anio_dc-frm');
		
		$('#col_aula_detalle-frm #btn-grabar').on('click',function(event){
			_post($('#col_aula_detalle-frm').attr('action') , $('#col_aula_detalle-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
	}	
	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		//festivo_listar_tabla();
		
	}
	
	//Abrir el modal
	var titulo;
		titulo = 'Autoridades del Aula';
	
	_modal(titulo, 'pages/academico/col_aula_detalle_modal.html',onShowModal,onSuccessSave);

}

function listar_matriculados(id_au){
	var param ={id_anio:$('#_id_anio').text(), id_gra:$('#id_grad').val() , id_au:id_au, id_gir: $('#id_gir').val(), id_niv:$('#id_niv').val() , rep_com:1 };
	_get('api/matricula/reporteMatriculaList',function(data){
	if(data.length>0)
	cap_seleccionado=data[0].id;
	//listar_areas(niv_seleccionado);
	//var id_gir_pri=$("#tabla_giro_negocio tbody tr").find('tr:nth-child(1)').find('#id').val();
	$('#tabla_matriculados').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			{"title":"Alumno", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.Alumno 
			}},
			{"title":"Fec. Mat.", "data": "fecha"},
			{"title":"Sit. Academica", "data": "nom"},
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="local_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="local_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				//$('#frm_local #id_ggn').val(id_gir);	   
				}
			});
	},param);
}


function listar_desempenios(id_com, id_gra){
	_get('api/competenciaDC/listarDesempenios/'+id_com+'/'+id_gra,function(data){
	//Primer Giro de Negocio
	if(data.length>0){
		des_seleccionado=data[0].id;
	}		
	$('#tabla_desempenios').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value; 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="giro_negocio_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="giro_negocio_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				//_initCompleteDT(settings);
				//listar_locales(id_gir_pri);
				//alert(suc_seleccionado);
				//listar_servicios(id_suc_pri,id_gir_pri);
				$("#tabla_desempenios tbody tr").click(function(){  
					des_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					$("#div_competencias").hide();
					$("#div_capacidades").hide();
					$("#div_desempenio").show();
					_llenarComboURL('api/competenciaDC/listarCompetencias/'+$('#id_dcare').val(),$('#frm-desempenio #id_com'),com_seleccionado,null);
					var id_niv=$('#id_dcniv option:selected').attr("data-aux1");
					_llenarCombo('cat_grad',$('#frm-desempenio #id_gra'),$('#id_grad').val(),id_niv, function(){
						
					});
					$('#frm-desempenio #id_com').prop('disabled',true);
					$('#frm-desempenio #id_gra').prop('disabled',true);
					if(des_seleccionado!=null){
						_get('api/competenciaDC/obtenerDatosDesempenio/' + des_seleccionado,
						function(data){
							console.log(data);
							_fillForm(data,$('#div_desempenio'));
							$("#div_desempenio #desempenio_nom").text("DESEMPEÑO "+data.nom);
							$('#frm-desempenio #id_com').val(data.id_com).change();
							$('#frm-desempenio #id_gra').val(data.id_gra).change();
						});	
					}	
				});	   
				}
			});
	});
}	

$('#frm-competencia #btn_grabar').on('click',function(event){
		$('#id_area').prop('disabled',false);
		var validation = $('#frm-competencia').validate(); 
		if ($('#frm-competencia').valid()){
			_post($('#frm-competencia').attr('action') , $('#frm-competencia').serialize(),function(data) {
				$('#frm-competencia #id').val(data.result);
				listar_competencias($('#id_dcare').val());
				$('#id_area').prop('disabled',true);
			}	
			);
		}	
		
});

$('#frm-competencia #btn_nuevo').on('click',function(event){
	$('#frm-competencia #id').val('');
	$('#frm-competencia #nom').val('');
	$('#frm-competencia #peso').val('');
	$('#frm-competencia #orden').val('');
});	

$('#frm-competencia #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/' + $('#frm-competencia #id').val(),
		context:_URL,
		success:function(){
			listar_competencias($('#id_dcare').val());
			$('#frm-competencia #id').val('');
			$('#frm-competencia #nom').val('');
			$('#frm-competencia #peso').val('');
			$('#frm-competencia #orden').val('');
		}
		});
});

$('#frm-capacidad #btn_grabar').on('click',function(event){
		$('#frm-capacidad #id_com').prop('disabled',false);
		var validation = $('#frm-capacidad').validate(); 
		if ($('#frm-capacidad').valid()){
			_post($('#frm-capacidad').attr('action') , $('#frm-capacidad').serialize(),function(data) {
				$('#frm-capacidad #id').val(data.result);
				listar_capacidades(com_seleccionado);
				$('#frm-capacidad #id_com').prop('disabled',true);
			}	
			);
		}		
});

$('#frm-capacidad #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/eliminarCapacidad/' + $('#frm-capacidad #id').val(),
		context:_URL,
		success:function(){
			listar_capacidades(com_seleccionado);
			$('#frm-capacidad #id').val('');
			//$('#frm-capacidad #id_com').val('').trigger('change');
			$('#frm-capacidad #peso').val('');
			$('#frm-capacidad #orden').val('');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-capacidad #btn_nuevo').on('click',function(event){
	$('#frm-capacidad #id').val('');
	//$("#frm-capacidad #id_com").val('').trigger('change');
	$('#frm-capacidad #nom').val('');
	$('#frm-capacidad #peso').val('');
	$('#frm-capacidad #orden').val('');
	$('#div_capacidades #nom_capacidad').text('');
});

$('#frm-desempenio #btn_grabar').on('click',function(event){
		$('#frm-desempenio #id_com').prop('disabled',false);
		$('#frm-desempenio #id_gra').prop('disabled',false);
		var validation = $('#frm_desempenio').validate(); 
		if ($('#frm-desempenio').valid()){
			_post($('#frm-desempenio').attr('action') , $('#frm-desempenio').serialize(),function(data) {
				$('#frm_desempenio #id').val(data.result);
				//listar_competencias_transversales($('#frm_area #id').val());
				listar_desempenios(com_seleccionado,$('#id_grad').val());
				$('#frm-desempenio #id_com').prop('disabled',true);
				$('#frm-desempenio #id_gra').prop('disabled',true);
			}	
			);
		}	
		
});

$('#frm-desempenio #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/eliminarDesempenio/' + $('#frm-desempenio #id').val(),
		context:_URL,
		success:function(){
			listar_desempenios(com_seleccionado,$('#id_grad').val());
			$('#frm-desempenio #id').val('');
			$("#frm-desempenio #nom").val('');
			$("#frm-desempenio #orden").val('');
			$("#frm-desempenio #peso").val('');
		}
		});
});

$('#frm-desempenio #btn_nuevo').on('click',function(event){
	$('#frm-desempenio #id').val('');
			$("#frm-desempenio #nom").val('');
			$("#frm-desempenio #orden").val('');
			$("#frm-desempenio #peso").val('');
});

