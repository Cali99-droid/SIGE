//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	var rol = _usuario.roles[0];
	$('#id_gra').on('change',function(event){
		aula_listar_tabla();
	});	
	
	$('#id_ciclo').on('change',function(event){
		$('#id_gra').change();
	});	
	
	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_ciclo'),null,param,function(){$('#id_ciclo').change();});
	});
	$('#id_nvl').on('change',function(event){
		var id_niv=$('#id_nvl').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_ciclo'),null,param,function(){$('#id_ciclo').change()});
	}); 
		
	/*_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});*/

		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});

}

function _onchangeAnio(id_anio, anio){
	aula_listar_tabla();	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_aula_modal.html" id="col_aula-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Aula</a></li>');

	$('#col_aula-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		aula_modal($(this));
	});

	//lista tabla
	//aula_listar_tabla();
});

function aula_eliminar(link){
	_DELETE({url:'api/aula/' + $(link).data("id"),
			success:function(){
					aula_listar_tabla();
				}
			});
}

function aula_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_modal.html');
	aula_modal(link);
	
}

function aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_aula-frm');
		$('#btn_detallar_mod').hide();
		/*$('#col_aula-frm #btn-agregar').on('click',function(event){
			$('#col_aula-frm #id').val('');
			_POST({form:$('#col_aula-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});*/
		$('#col_aula-frm #btn_grabar').on('click',function(event){
		var validation = $('#col_aula-frm').validate(); 
		if ($('#col_aula-frm').valid()){
			var turnos_seleccionadas = $("[name='id_cit']:checked").length;
				if(turnos_seleccionadas==0){//VALIDACION 1
					
					_alert_error("Debe seleccionar por lo menos un turno!!");
				
				} else{
				_post($('#col_aula-frm').attr('action') , $('#col_aula-frm').serialize(),function(data) {
				console.log(data);
				//$('#col_aula-frm #id').val(data.result);
				listarTurnosxAula(data.result,$('#col_aula-frm #id_cic').val());
				listarModalidadesxAula(data.result);
				aula_listar_tabla();
				});
				}
		}		
		});
		
		if (link.data('id')){
			_GET({url:'api/aula/' + link.data('id'),
				  success:	function(data){
					 console.log(data);
				_fillForm(data,$('#modal').find('form') );
				$('#col_aula-frm #id').val(data.id);
				$('#col_aula-frm #id_per').val(data.id_per);
				$('#col_aula-frm #secc').val(data.secc);
				$('#col_aula-frm #cap').val(data.cap);
				$('#col_aula-frm #des_classroom').val(data.des_classroom);
				$('#btn_detallar_mod').show();
				$('#id_grad').on('change',function(event){
					var param={id_anio: $('#_id_anio').text(),id_grad:$('#id_grad').val(),id_suc:$('#id_cic option:selected').attr("data-aux3")};
					_llenarComboURL('api/aula/seccionAnterior',$('#id_secc_ant'),data.id_secc_ant,param);
				});
				/*$('#id_per').on('change',function(event){
					var id_niv=$('#id_per option:selected').attr("data-aux2");|
					_llenarCombo('cat_grad',$('#id_grad'),data.id_grad,id_niv,function(){$('#id_grad').change();});
					var param1={id_per: $('#id_per').val()};
					_llenarComboURL('api/periodo/listarTurnosxPeriodo',$('#id_tur'),data.id_tur,param1);
				});*/
				$('#id_cic').on('change',function(event){
					var id_niv=$('#id_cic option:selected').attr("data-aux2");
					var id_per=$('#id_cic option:selected').attr("data-aux4");
					$('#id_per').val(id_per);
					_llenarCombo('cat_grad_todos',$('#id_grad'),data.id_grad,id_niv,function(){$('#id_grad').change();});
					/*var param1={id_per: $('#id_per').val()};
					_llenarComboURL('api/periodo/listarTurnosxPeriodo',$('#id_tur'),data.id_tur,param1);*/
					listarTurnosxAula(link.data('id'),$('#id_cic').val());
					listarModalidadesxAula(link.data('id'));
				});
				//_llenarCombo('cat_grad',$('#id_grad'), data.id_grad);
				//_llenarCombo('per_periodo',$('#id_per'), data.id_per, null, function(){$('#id_per').change();});
				var param2={id_anio: $('#_id_anio').text()};
				_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),data.id_cic,param2,function(){$('#id_cic').change();});
				
				_llenarCombo('col_turno',$('#id_turno'), data.id_turno);
				_llenarCombo('cat_modalidad_estudio',$('#id_cme'), data.id_cme);
				if(data.des_classroom==null){
					//if(data.id!=null){
							_GET({url:'api/aula/generarDescripcion/' + data.id,
							success:	function(data){
							$('#des_classroom') .val(data.descripcion); 
							}	  
							});
					//}	
				}	
				  }
				}
			);
		}else{
			$('#id_grad').on('change',function(event){
				
			});
			/*$('#id_per').on('change',function(event){
				var id_niv=$('#id_per option:selected').attr("data-aux2");
				_llenarCombo('cat_grad',$('#id_grad'),null,id_niv);
				var param1={id_per: $('#id_per').val()};
				_llenarComboURL('api/periodo/listarTurnosxPeriodo',$('#id_tur'),null, param1);
			});*/
			$('#id_grad').on('change',function(event){
					var param={id_anio: $('#_id_anio').text(),id_grad:$('#id_grad').val(),id_suc:$('#id_cic option:selected').attr("data-aux3")};
					_llenarComboURL('api/aula/seccionAnterior',$('#id_secc_ant'),null,param);
			});
			$('#id_cic').on('change',function(event){
				var id_niv=$('#id_cic option:selected').attr("data-aux2");
				var id_per=$('#id_cic option:selected').attr("data-aux4");
					$('#id_per').val(id_per);
				_llenarCombo('cat_grad_todos',$('#id_grad'),null,id_niv,function(){$('#id_grad').change();});
				//var param1={id_per: $('#id_per').val()};
				//_llenarComboURL('api/periodo/listarTurnosxPeriodo',$('#id_tur'),null,param1);
				listarTurnosxAula(link.data('id'),$('#id_cic').val());
				listarModalidadesxAula(link.data('id'));
			});
			
			/*if(data){
				_GET({url:'api/aula/generarDescripcion/' + data.id,
				  success:	function(data){
					$('#des_classroom') .val(data.descripcion); 
				  }	  
			});
			}	*/
			
			//_llenarCombo('per_periodo',$('#id_per'),null,null,function(){$('#id_per').change();});
			var param2={id_anio: $('#_id_anio').text()};
			_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param2,function(){$('#id_cic').change();});
			_llenarCombo('cat_modalidad_estudio',$('#id_cme'));
			//_llenarCombo('cat_grad',$('#id_grad'));
			//_llenarCombo('col_turno',$('#id_turno'));
			$('#col_aula-frm #btn-grabar').hide();
			//listarTurnosxAula(null,$('#id_cic').val());
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Aula del colegio';
	else
		titulo = 'Nuevo  Aula del colegio';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function aula_listar_tabla(){
	var param={id_anio:$("#_id_anio").text(), id_cic:$('#id_ciclo').val(), id_gra:$('#id_gra').val()};
	_GET({ url: 'api/aula/listar/',
		   params:param,
		   success:
			function(data){
			console.log(data);
				$('#col_aula-tabla').dataTable({
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
							{"title":"Modalidad", "data": "modalidadEstudio.nom"}, 
							{"title":"Local", "data": "periodo.sucursal.nom"},
							{"title":"Periodo", "data": "periodo.tipPeriodo.nom"}, 
							{"title":"Ciclo", "data": "ciclo.nom"}, 
							{"title":"Nivel", "data": "grad.nivel.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Secci&oacute;n", "data" : "secc"}, 
							{"title":"Desc. Classroom", "data" : "des_classroom"}, 
							//{"title":"Secci&oacute;n anterior", "data" : "id_secc_ant"}, 
							{"title":"Capacidad M&aacute;xima", "data" : "cap"}, 
							{"title":"Turno", "data": "turno.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="aula_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="aula_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});

}

function listarTurnosxAula(id_au,id_cic){
	var param = {id_au:id_au, id_cic:id_cic};
	_get('api/turnoAula/listarTurnosxAula',function(data){
	$('#tabla_turnos_aula').dataTable({
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
			{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
				//creamos un hidden x cada checkbox
				var checked = (row.id_atur!=null) ? ' checked ': ' ';
				var disabled = (checked) ? '':'disabled';
				
				return "<input type='hidden' id='id_atur" + row.id +"' name='id_atur' " + disabled + " value='"+row.id_atur+"'/><input type='checkbox' " + checked + "id='id_cit"+row.id+"'  name ='id_cit'  value='" + row.id_cit+ "' />";
			} 
			},
			{"title":"Turno", "data" : "nom"},
			{"title":"Hora Inicio", "render":function ( data, type, row,meta ) { 
				//var hor_ini = (row.hor_ini != null) ? _parseDate(row.hor_ini) :'';
					return  row.hor_ini;
				} 
			},
			{"title":"Hora Fin", "render":function ( data, type, row,meta ) { 
				//var hor_fin = (row.hor_fin != null) ? _parseDate(row.hor_fin) :'';
				return row.hor_fin;
			}} 
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);   
				//$('.datetimerange-single').prop('disabled',true);
				//aca reviso si tiene checked y deshabilito
				/* $("#frm-mant_ciclo input[name='id_tur']:checked").each(function() {
						$('#id_ctu' + $(this).val()).prop('disabled',false);
						$('#id_tur' + $(this).val()).prop('disabled',false);						
						//$('#id_eva' + $(this).val()).val($(this).data('ideva'));
						$('#hor_ini' + $(this).val()).prop('disabled',false);
						$('#hor_fin' + $(this).val()).prop('disabled',false);
				  });	
				 
				 $("#frm-mant_ciclo input[name='id_tur']:unchecked").each(function() {
					 $('#hor_ini' + $(this).val()).val('');
					 $('#hor_fin' + $(this).val()).val('');
				 });*/
				}
			});
	},param);
}

function listarModalidadesxAula(id_au){
	var param = {id_au:id_au};
	_get('api/aula/listarModalidadesxAulaMes',function(data){
	$('#tabla_modalidades_mes').dataTable({
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
			{"title":"Mes", "render":function ( data, type, row,meta ) {
				//creamos un hidden x cada checkbox
				if(row.mes==1){
					return 'Enero';
				} else  if(row.mes==2){
					return 'Febrero';
				} else  if(row.mes==3){
					return 'Marzo';
				}	else  if(row.mes==4){
					return 'Abril';
				}	else  if(row.mes==5){
					return 'Mayo';
				}	else  if(row.mes==6){
					return 'Junio';
				}	else  if(row.mes==7){
					return 'Julio';
				}	else  if(row.mes==8){
					return 'Agosto';
				}	else  if(row.mes==9){
					return 'Setiembre';
				}	else  if(row.mes==10){
					return 'Octubre';
				}	else  if(row.mes==11){
					return 'Noviembre';
				}	else  if(row.mes==12){
					return 'Diciembre';
				}		
			} 
			},
			{"title":"Modalidad", "data" : "modalidad"},
			{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons">'+ 
								'<a href="#" data-id="' + row.id + '" onclick="modalidad_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);   
				//$('.datetimerange-single').prop('disabled',true);
				//aca reviso si tiene checked y deshabilito
				/* $("#frm-mant_ciclo input[name='id_tur']:checked").each(function() {
						$('#id_ctu' + $(this).val()).prop('disabled',false);
						$('#id_tur' + $(this).val()).prop('disabled',false);						
						//$('#id_eva' + $(this).val()).val($(this).data('ideva'));
						$('#hor_ini' + $(this).val()).prop('disabled',false);
						$('#hor_fin' + $(this).val()).prop('disabled',false);
				  });	
				 
				 $("#frm-mant_ciclo input[name='id_tur']:unchecked").each(function() {
					 $('#hor_ini' + $(this).val()).val('');
					 $('#hor_fin' + $(this).val()).val('');
				 });*/
				}
			});
	},param);
}

function agregar_modalidad(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_modalidad.html');
	//link.data('id_com',$('#col_competencia_full-frm #id').val());
	link.data('id_au',$('#col_aula-frm #id').val());
	//console.log($('#mat_conf_encuesta-frm #id').val());
	conf_aula_modalidad_modal(link);
}

function conf_aula_modalidad_modal(link){
	//link.attr('href','pages/matricula/mat_conf_mensualidad_modal.html');
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('frm-aula_modalidad');
		
		$('#frm-aula_modalidad #id_au').val(link.data('id_au'));
		
		if (link.data('id_au')){
			//Lista de Preguntas
			var param = {id_enc: link.data('id_enc')};
			_llenarCombo('cat_modalidad_estudio',$('#id_cme'),null,null, function(){$("#id_cme").change()});
			/*_get('api/encuesta/listarPreguntasAlt',
			function(data){
				var cant_pre=data.length;
				console.log(cant_pre);
				
			}, param);*/
		} else {
		}
		
		$('#btn_grabar_mod').on('click',function(event){
			alert();
			_post($('#frm-aula_modalidad').attr('action') , $('#frm-aula_modalidad').serialize(),
			function(data){
					onSuccessSave(data) ;
					listarModalidadesxAula($('#col_aula-frm #id').val());
				}
			);
		});		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		//conf_mensualidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	//if (link.data('id'))
		titulo = 'Modalidad de Estudio para el aula seleccionado';
	//else
	//	titulo = 'Nuevo  Configuraci&oacute;n de Preguntas (A&ntilde;o ' +  link.data('anio')+ ')';
	
	_modal(titulo, link.attr('href') ,onShowModal,onSuccessSave);

}

