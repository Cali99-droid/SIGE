//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_ses=null;
function onloadPage(params){
	$('#evaluaciones-frm #id_aula').on('change',function(event){
		var param = {id_niv: $('#id_nivel').val() , id_anio: $('#_id_anio').text() };
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#evaluaciones-frm #id_per'),null,param, function (){$('#evaluaciones-frm #id_per').change()});
	});
	$('#evaluaciones-frm #id_grado').on('change',function(event){
		var param={id_tra:_usuario.id_tra, id_gra: $('#id_grado').val(),id_anio: $('#_id_anio').text(), id_niv: $('#id_nivel').val()};
		_llenarComboURL('api/evaluacion/listarAulasProfesor',$('#evaluaciones-frm #id_aula'),null,param,function(){$('#evaluaciones-frm #id_aula').change()});
	});
	$('#evaluaciones-frm #id_nivel').on('change',function(event){
		//_llenarCombo('cat_grad',$('#evaluaciones-frm #id_gra'),null,$(this).val(),function(){$('#evaluaciones-frm #id_gra').change()});
		var param2={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#evaluaciones-frm #id_nivel").val()};
		_llenarComboURL('api/evaluacion/listarGrados',$('#evaluaciones-frm #id_grado'),null, param2,function(){$('#evaluaciones-frm #id_grado').change()});
	});
	var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};
	_llenarComboURL('api/evaluacion/listarNiveles',$('#evaluaciones-frm #id_nivel'),null,param,function(){$('#evaluaciones-frm #id_nivel').change();});
	
	id_ses=params.id;
	//_llenarCombo('cat_nivel',$('#evaluaciones-frm #id_niv'),null,null, function(){$('#evaluaciones-frm #id_niv').change()});
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_evaluacion_modal.html" id="not_evaluacion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevas Evaluaciones Acad&eacute;micas</a></li>');

	$('#not_evaluacion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		evaluacion_modal($(this));
	});

	//lista tabla
	//alert();
	//evaluacion_listar_tabla();
	
});

$('#evaluaciones-frm #id_per').on('change',function(event){
	evaluacion_listar_tabla();
});

function evaluacion_eliminar(link){
	_delete('api/evaluacion/' + $(link).data("id"),
			function(){
					evaluacion_listar_tabla();
				}
			);
}

function evaluacion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_evaluacion_modal.html');
	evaluacion_modal(link);
	
}

function evaluacion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_evaluacion-frm');
		
		if (link.data('id'))
			$("#id_nep").val(link.data('id_nep'));
				
		$("#id_ses").val(id_ses);
		
		$('#not_evaluacion-frm #btn-actualizar').on('click',function(event){
			_post('api/evaluacion/actualizarEvaluaciones' , $('#not_evaluacion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					
				}
			);
		});
		
		$('#btn-siguiente').on('click',function(event){
			//no paso por aca. es cache? talvez no
			event.preventDefault();//mata el evento del submit
			var aulas_seleccionadas = $("[name='id_au']:checked").length;//valida q haya un aula seleccionada
			var ins = $("#not_evaluacion-frm #ins").val();
			refescarTabIndicadores();
			if(aulas_seleccionadas==0){//VALIDACION 1
			
				_alert_error("Debe seleccionar por lo menos un aula!!");
			
			}else if($("#not_evaluacion-frm #ins").val()==''){//ACA VALIDA EL OTRO CAMPO
				
				_alert_error("Debe llenar instrumento");
				$('#ins').focus();

			}else{
				var fechasError = false;
				/* $("#not_evaluacion-frm input[name='id_au']:checked").each(function() {//ahh, aca falla seguro por q dejo pasasr creo q esta mal q active x cada
					 //iteracion como hago para q sea al final??, jajaj ya te respondiste 
					    var id_au= $(this).val();
					    if(id_au!=''){
					    	var fec_fin=$("#not_evaluacion-frm #fec_fin"+id_au).val();

					    	if(fec_fin==''){
					    		fechasError = true;
					    		_alert_error("Debe poner valor a la fecha fin");
					    	}

					    }
					    
					});	*/
				 
				 if(!fechasError)
						$('#not_ind_eva').click();	//estda bien? si
			}
		});
	
		//GRABAR FORMULARIOS
		$('#btn-grabar').on('click',function(event){
				event.preventDefault();
				var indicadores_seleccionadas = $("[name='id_ind']:checked").length;
				if(indicadores_seleccionadas==0){//VALIDACION 1
					
					_alert_error("Debe seleccionar por lo menos un idicador!!");
				
				} else{
				$('#id_cpu').attr('disabled', false);
				$('#id_tra').val(_usuario.id_tra);
				$('#id_anio').val($('#_id_anio').text());
				_post('api/evaluacion/grabarEvaluaciones' , $('#not_evaluacion-frm,#not_ind_eva-frm').serialize(),
						function(data){
								onSuccessSave(data) ;
		    					$(".modal .close").click();

							}
						);
				}
		});
		
		
		$('#id_nte').on('change',function(event){
			var id = $(this).val();
			var table = $('#list_aulas-tabla').DataTable();
			
			if(id=="1"){//examen
				table.column( 3 ).visible( false);
				$('#list_aulas-tabla tr:eq(0) th:eq(3)').text("Fec. Evaluación");
			}else{//tarea o trabajo
				table.column( 3 ).visible( true);
				$('#list_aulas-tabla tr:eq(0) th:eq(4)').text("Fec. Fin");
			}
		});
		
		$('#id_cpu').on('change',function(event){
			var nump=$('#id_cpu option:selected').attr("data-aux1");
			var param={id_cur:$("#id_cur").val(),id_anio:$('#_id_anio').text(),num:nump,id_gra:$("#id_gra").val(), id_nep:$("#id_nep").val(), id_cpu:$('#id_cpu').val()};
			_get('api/cursoUnidad/listarDesem/',
					function(data){
					console.log(data);
						$('#list_desemp-tabla').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 orderCellsTop:true,
							 select: true,
					         columns : [
					                    
					        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		var checked = (row.nie_id!=0 && row.est=="A" && row.cpu_id==$('#id_cpu').val()) ? ' checked ': ' ';
					        	 		var disabled = (checked) ? 'disabled':'';
					        	 		var id_ind = (row.nie_id!=null) ? row.nie_id: '';
										return "<input type='checkbox'"+checked+" id='id_cis"+row.id+"' name ='id_ind' value='" + row.id + "' data-ideva=''  />";
					        	 	} 
					        	 	},
					        	 	//{"title":"Tema", "data" : "tema"},
					        	 	//{"title":"Subtema", "data" : "subtema"},
					        	 	{"title":"Indicador", "data" : "indicador"}
						    ],
						    "initComplete": function( settings ) {
								   _initCompleteDT(settings);
									//$('.daterange-single').val('');
									//$('.daterange-single').prop('disabled',true);
							 }
					    });
					}, param
			);
		});

		$('#id_cur').on('change',function(event){
			var param1 = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
			_llenarComboURL('api/cronogramaLibreta/listarPeriodosVigentes',$('#id_cpu'), null,param1,function(){
			var param2={id_tra:_usuario.id_tra, id_cur: $("#id_cur").val(), id_gra: $("#id_gra").val(),id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(), id_nep:$("#id_nep").val()};
			listar_aulas_tabla(param2); 
			$('#id_cpu').change();
			});
		});
		$('#id_niv').on('change',function(event){
			var param={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val()}
			_llenarComboURL('api/evaluacion/listarGrados',$('#id_gra'), null, param, function(){
			var param1={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(), id_gra:$("#id_gra").val()}
			_llenarComboURL('api/evaluacion/listarCursos',$('#id_cur'), null, param1,function(){$('#id_cur').change();});	
			});
			
		});
		
		$('#id_gra').on('change',function(event){
			var param={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(),id_gra:$("#id_gra").val()}
			_llenarComboURL('api/evaluacion/listarCursos',$('#id_cur'), null, param,function(){$('#id_cur').change();});
		});
		
		if (link.data('id')){
			//$("#id_nep").val(link.data('id_nep'));
			_get('api/evaluacion/' + link.data('id'),
				function(data){
					console.log(data);
					_fillForm(data,$('#not_evaluacion-frm') );
					
					
					_llenarCombo('not_tip_eva',$('#id_nte'), data.id_nte);
					var param1 = {id_tra:_usuario.id_tra, id_anio:$('#_id_anio').text()};
					_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),data.cursoAula.cursoAnio.grad.id_nvl,param1,function(){
						var param={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val()};
						_llenarComboURL('api/evaluacion/listarGrados',$('#id_gra'), data.cursoAula.cursoAnio.id_gra, param,function(){
							var param2={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(),id_gra: $("#id_gra").val()};
							_llenarComboURL('api/evaluacion/listarCursos',$('#id_cur'), data.cursoAula.cursoAnio.id_cur, param2, function(){
							//$('#id_cur').change(); 
							var param2={id_tra:_usuario.id_tra, id_cur: $("#id_cur").val(), id_gra: $("#id_gra").val(),id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(), id_nep:$("#id_nep").val()};
							listar_aulas_tabla(param2); 
							var param1 = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
							_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), data.nump,param1,function(){$('#id_cpu').change()});
							});
								
						});
						
					});
				});
			$('#id_cpu').attr('disabled', true);
		}else{
			$('#id_cpu').attr('disabled', false);
			var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};
			_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),null,param,function(){$('#id_niv').change();});
			_llenarCombo('not_tip_eva',$('#id_nte'));
			//$('#not_evaluacion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		console.log(data);
		$.each(data.result, function(i, evaluacion) {
			var id_eva = evaluacion.id;
			var id_cca = evaluacion.id_cca;
		    $('#id_eva' + id_cca).val(id_eva );
		    $('#id_eva' + id_cca).attr('disabled', false);
		    $('#id_nep').val(evaluacion.id_nep);
		    //alert($('#id_nep').val());
		    $.each(evaluacion.indEvas, function(j, indeva) {
		    	var id_ne = id_eva;
		    	var id_cis = indeva.id_cis;
		    	
		    });
		});
		
		evaluacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Evaluaciones Acad&eacute;micas';
	else
		titulo = 'Nuevas Evaluaciones Acad&eacute;micas';
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function evaluacion_listar_tabla(){
	var param = {id_tra: _usuario.id_tra, id_au: $('#evaluaciones-frm #id_aula').val(), id_cpu:$('#evaluaciones-frm #id_per').val()};
	//alert();
	console.log(param);
	_get('api/evaluacion/listar/',
			function(data){
			console.log(data);
				$('#not_evaluacion-tabla').dataTable({
					data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Nivel", "data": "nivel.nom"},
			        	 	{"title":"Sucursal", "data": "sucursal.nom"},
			        	 	{"title":"Grado", "data": "grad.nom"},	
							{"title":"Sección", "data": "aula.secc"},
							{"title":"Curso", "data": "curso.nom"},	
			        	 	{"title":"Tipo Evaluacion", "data": "tipEva.nom"},
			        	 	{"title":"Instrumento", "data" : "ins"}, 
							{"title":"Fecha Fin", "data" : "fec_fin", "render":function(data, type, row){ 
								if(data!=null)
									return _parseDate(data);
								else
									return data;
							}}, 		
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" data-id_nep="' + row.id_nep + '"onclick="evaluacion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="evaluacion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}



//grilla de aulas/fecha
function seleccionarFecha(campo1){
	var campo =$(campo1);
	var id = campo.val();
	
	$('#id_eva' + id).val('');
	if(campo.is(':checked')){
		$('#fec_ini' + id ).prop( "disabled", false );
		$('#fec_fin' + id ).prop( "disabled", false );
		$('#id_eva' + id ).prop( "disabled", false );
	}else{
		var id_eva = $('#id_eva' + id).val();
		$('#id_eva' + id).prop('disabled', true);//SE DESHABILITA
		$('#fec_ini' + id ).prop( "disabled", true );
		$('#fec_ini' + id).val('');
		$('#fec_fin' + id ).prop( "disabled", true );
		$('#fec_fin' + id).val('');
	}
	
	refescarTabIndicadores();
}

//funcion q se ejecuta despues de grabar
var onSuccessSave = function(data){
	evaluacion_listar_tabla();
}


//Activa o desactiva tab indicadres segun la cantidad de checks seleccionados de aulas
function refescarTabIndicadores(){
	var aulas_seleccionadas = $("[name='id_au']:checked").length;
	if(aulas_seleccionadas >0)
		$('#li_indicadores').removeClass('disabled');
	else
		$('#li_indicadores').addClass('disabled');
}

function listar_aulas_tabla(param2){
	_get('api/evaluacion/listarAulas/',
			function(data){
			console.log(data);
				$('#list_aulas-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
			        	 		//creamos un hidden x cada checkbox
			        	 		var checked = (row.eva_id!=null) ? ' checked ': ' ';
			        	 		var disabled = (checked) ? 'disabled':'';
			        	 		var ideva = (row.eva_id!=null) ? row.eva_id: '';
			        	 		
								return "<input type='hidden' id='id_eva" + row.id +"' name='id_eva' " + disabled + "/><input type='hidden' id='id_eva_est" + row.id +"' name='id_eva_est' " + disabled + "/><input type='checkbox' " + checked + "id='id_cca"+row.id+"'  name ='id_au' value='" + row.id + "' data-ideva='" + ideva + "' onclick='seleccionarFecha(this)' />";
			        	 	} 
			        	 	},
							{"title":"Aula", "data" : "secc"},
							{"title":"Fec. Inicio", "render":function ( data, type, row,meta ) { 
								var fec_ini = (row.fec_ini != null) ? _parseDate(row.fec_ini) :'';
									return "<input type='text' id='fec_ini" + row.id + "' name='fec_ini' value='" + fec_ini + "'  data-id='" + row.id + "' class='form-control input-sm daterange-single' maxlength='10' />";
								} 
							},
							{"title":"Fec. evaluacion", "render":function ( data, type, row,meta ) { 
								var fec_fin = (row.fec_fin != null) ? _parseDate(row.fec_fin) :'';
								return "<input type='text' id='fec_fin" + row.id + "' name='fec_fin'  value='" + fec_fin + "' data-id='" + row.id + "' class='form-control input-sm daterange-single' maxlength='10' />";
							} 
						}
				    ],
				    		"initComplete": function( settings ) {
						   _initCompleteDT(settings);
							$('.daterange-single').daterangepicker({ 
						        singleDatePicker: true,
						        autoUpdateInput: false,
						        locale: { format: 'DD/MM/YYYY'}
						    });
							
							
							$('.daterange-single').prop('disabled',true);
							
							//aca reviso si tiene checked y deshabilito
							 $("#not_evaluacion-frm input[name='id_au']:checked").each(function() {
								 	$('#id_eva' + $(this).val()).prop('disabled',false);	
								 	$('#id_eva' + $(this).val()).val($(this).data('ideva'));
								    $('#fec_ini' + $(this).val()).prop('disabled',false);
								 	$('#fec_fin' + $(this).val()).prop('disabled',false);
							  });	
							 
							 $("#not_evaluacion-frm input[name='id_au']:unchecked").each(function() {
								 $('#fec_ini' + $(this).val()).val('');
								 $('#fec_fin' + $(this).val()).val('');
							 });
							 
							$('#id_nte').change();
							refescarTabIndicadores();
					 }
			    });
			}, param2
	);
}
