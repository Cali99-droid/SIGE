//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
var id_cic_tur_sel=null;
function onloadPage(params){
	_onloadPage();
	var id_anio=$('#_id_anio').text();
	$('#id_gir').on('change',function(event){
		listar_ciclos($('#id_gir').val(), id_anio);
	});
	$('#btn_agregar_descuento').hide();
	$('#btn_grabar').hide();
	$('#btn_eliminar').hide();
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null, function(){
		$('#id_gir').change();		
	});
}

function _onchangeAnio(id_anio, anio){
	listar_ciclos($('#id_gir').val(), id_anio);
}

function listar_ciclos(id_gir, id_anio){
	var param={id_gir:id_gir, id_anio:id_anio};
	_get('api/periodo/listarCiclosxGiroNegocio',function(data){
	$('#tabla_ciclo').dataTable({
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
			{"title":"Local", "data" : "sucursal"}, 
			{"title":"Nivel", "data" : "nivel"}, 
			{"title":"Tipo Periodo", "data" : "tipo_periodo"}, 
			{"title":"Turno", "data" : "turno"},
			{"title":"Ciclo", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.ciclo
			}}
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				//_initCompleteDT(settings);
				$("#tabla_ciclo tbody tr").click(function(){ 
					$("#tabla_cuotas").html('');
					 id_cic_tur_sel=($(this).find('td:nth-child(5)').find('#id').val());
					 limpiar_campos();
					 $('#btn_grabar').show();
					 $('#btn_eliminar').show();
					if(id_cic_tur_sel!=null){
						_get('api/confPagosCiclo/obtenerDatosConfPago/' + id_cic_tur_sel,
						function(data){
							if(data){
								//console.log(data.id_cic);
								_fillForm(data,$('#frm-mant_confpagosciclo'));
								var param={id_gir:id_gir, id_anio:id_anio};
								_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cct'), data.id_cct,param,function(){$('#id_cct').change()});
								mostrar_cuotas();
								listar_descuentos(data.id_cct);
								$('#btn_agregar_descuento').show();
								
							}else{
								var param={id_gir:id_gir, id_anio:id_anio};
								_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cct'),id_cic_tur_sel,param,function(){$('#id_cct').change()});
								listar_descuentos(id_cic_tur_sel);
								$('#btn_agregar_descuento').hide();
							}
							
						});
						
					} else{
						var param={id_gir:id_gir, id_anio:id_anio};
						_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cct'), id_cic_tur_sel,param,function(){$('#id_cct').change()});
						listar_descuentos(id_cic_tur_sel);
						$("#num_cuotas").on('blur',function(){
							alert('entrooo');
							
						});
					}
						
				});	   
				}
			});
	},param);
}	

$("#num_cuotas").on("keyup", function() {
	//$("#tabla_cuotas").html('');
	mostrar_cuotas();
	//_onloadPage();
	/*var costo_ciclo=$("#costo").val();
	if(costo_ciclo!=''){
			var nro_cuotas=$("#num_cuotas").val();
			var cuota_men=costo_ciclo/nro_cuotas;
	var id_fdes=$("#frm-mant_confpagosciclo #id").val();
								if(nro_cuotas){
									for (var j=1;j<= nro_cuotas; j++) {
										//alert(j);
										//Hallar los datos de la cuota descuento
										var param={id_fdes:id_fdes, nro_cuota:j};
										var div=null;
										var id_cuota=null;
										var costo=cuota_men;
										var fec_venc='19/01/2021';
										_get('api/confPagosCiclo/listarDescuentos',function(data){
										console.log(data);
										
										//if(data.length>0){
											 id_cuota=(data.id!=null) ? data.id: '';
											  costo=(data.costo!=null) ? data.costo: cuota_men;
											  fec_venc=(data.fec_venc!=null) ? _parseDate(data.fec_venc): '19/01/2021'; 
										//}
										
										
										},param);	
										//alert(j);										
										var div="<div class='col-md-4'>"
													+"<div class='form-group'>"
													+"<label>Cuota</label><input type='hidden' name='id_cuo' id='id_cuo'"+j+" value="+id_cuota+"><input type='text' name='nro_cuo' id='nro_cuo'"+j+" value="+j+" class='form-control' readonly='readonly'>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-4'>"
													+"<div class='form-group'>"
													+"<label>Costo</label><input type='text' name='costo_cuo' id='costo_cuo'"+j+" value="+costo+"  class='form-control' >"
													+"</div>"
												+"</div>"
												+"<div class='col-md-4'>"
													+"<div class='form-group'>"
													+"<label>Fecha de Vencimiento</label><input type='text' name='fec_venc' id='fec_venc'"+j+" value="+fec_venc+" class='form-control daterange-single' readonly='readonly'>"
													+"</div>"
												+"</div>";
										//var div="<p><input type='checkbox' id='nro_cuota'"+i+" name='nro_cuota' value="+i+" />"+i+"</p>";
										$("#tabla_cuotas").append(div);
									}
								}
								$('.daterange-single').daterangepicker({ 
									singleDatePicker: true,
									autoUpdateInput: false,
									locale: { format: 'DD/MM/YYYY'}
								});
								
	} else {
		alert('Debe de poner el costo del ciclo');
		$("#costo").focus();
	}	*/	

});	

$("#costo").on("keyup", function() {
	
	mostrar_cuotas();

});	

function mostrar_cuotas(){
	$("#tabla_cuotas").html('');
	var costo_ciclo=$("#costo").val();
	if(costo_ciclo!=''){
			var nro_cuotas=$("#num_cuotas").val();
			if(nro_cuotas!=null){
				var cuota_men=(costo_ciclo/nro_cuotas).toFixed(2);
			var id_fdes=$("#frm-mant_confpagosciclo #id").val();
								if(nro_cuotas){
									for (var j=1;j<= nro_cuotas; j++) {
										//alert(j);
										//Hallar los datos de la cuota descuento
										var param={id_fdes:id_fdes, nro_cuota:j};
										var div=null;
										var id_cuota=null;
										var costo=cuota_men;
										var fec_venc='19/01/2021';
										crearTabla(j,id_cuota,costo, fec_venc);
										_get('api/confPagosCiclo/obtenerDatosCuotas',function(data){
										console.log(data);
										
										if(data){
											id_cuota=(data.id!=null) ? data.id: '';
											//alert(id_cuota);
											costo=(data.costo!=null) ? data.costo: cuota_men;
											fec_venc=(data.fec_venc!=null) ? _parseDate(data.fec_venc): '19/01/2021'; 
											var numero_cuota=data.nro_cuota;
										//}
										
										$("input[name=nro_cuo]").each(function (index) {  
											if($(this).val()==numero_cuota){
												//Si es igual le pongo check
												$(this).val(numero_cuota);
												$("#id_cuo"+numero_cuota).val(id_cuota);
												$("#costo_cuo"+numero_cuota).val(costo);
												$("#fec_venc"+numero_cuota).val(fec_venc);
												$('.daterange-single').daterangepicker({ 
													singleDatePicker: true,
													autoUpdateInput: true,
													locale: { format: 'DD/MM/YYYY'}
												});
											}
										});
										}				
										//crearTabla(j,id_cuota,costo, fec_venc);
										},param);	
										//alert(id_cuota);										
										
									}
								}
			}	
			
			
	} else {
		alert('Debe de poner el costo del ciclo');
		$("#costo").focus();
	}		
}	

function crearTabla(j,id_cuota,costo, fec_venc){
	var div="<div class='col-md-4'>"
			+"<div class='form-group'>"
			+"<label>Cuota</label><input type='hidden' name='id_cuo' id='id_cuo"+j+"' value="+id_cuota+"><input type='text' name='nro_cuo' id='nro_cuo"+j+"' value="+j+" class='form-control' readonly='readonly'>"
			+"</div>"
		+"</div>"
		+"<div class='col-md-4'>"
			+"<div class='form-group'>"
			+"<label>Costo</label><input type='text' name='costo_cuo' id='costo_cuo"+j+"' value="+costo+"  class='form-control' >"
			+"</div>"
		+"</div>"
		+"<div class='col-md-4'>"
			+"<div class='form-group'>"
			+"<label>Fecha de Vencimiento</label><input type='text' name='fec_venc' id='fec_venc"+j+"' value="+fec_venc+" class='form-control daterange-single' readonly='readonly'>"
			+"</div>"
		+"</div>";
//var div="<p><input type='checkbox' id='nro_cuota'"+i+" name='nro_cuota' value="+i+" />"+i+"</p>";
$("#tabla_cuotas").append(div);
$('.daterange-single').daterangepicker({ 
				singleDatePicker: true,
				autoUpdateInput: true,
				locale: { format: 'DD/MM/YYYY'}
			});
}	

function listar_descuentos(id_cct){
	var param={id_cct:id_cct};
	_get('api/confPagosCiclo/listarDescuentos',function(data){		
	$('#tabla_descuentos').dataTable({
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
			{"title":"Nombre", "data" : "nom"}, 
			{"title":"Monto", "data" : "monto"}, 
			{"title":"Vencimiento", "data" : "venc"}, 
			{"title":"Fecha de Vencimiento", "data" : "fec_venc"},
			{"title":"Acumulable", "data" : "acu"}, 
			{"title":"Acciones", "render": function ( data, type, row ) {
				return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="descuento_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="descuento_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				
				//_initCompleteDT(settings); 
				}
			});
	},param);
	
		
	
}	

function limpiar_campos(){
	$('#frm-mant_confpagosciclo #id').val('');
	$('#frm-mant_confpagosciclo #costo').val('');
	$('#frm-mant_confpagosciclo #num_cuotas').val('');
}	

$('#frm-mant_confpagosciclo #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_confpagosciclo').validate(); 
		if ($('#frm-mant_confpagosciclo').valid()){
			_post($('#frm-mant_confpagosciclo').attr('action') , $('#frm-mant_confpagosciclo').serialize(),function(data) {
				$('#frm-mant_confpagosciclo #id').val(data.result);
				mostrar_cuotas();
				$('#btn_agregar_descuento').show();
				//listar_periodos(id_anio);
			}	
			);
		}	
		
});

function agregar_descuento(){
	descuentoConfModal(null,id_cic_tur_sel);	
}

function descuento_editar(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	descuentoConfModal(id,id_cic_tur_sel);
	
}	

function descuento_eliminar(link){
	_delete('api/confPagosCiclo/eliminarDescuento/' + $(link).data("id"),
			function(){
					listar_descuentos(id_cic_tur_sel);
				}
			);
}	

function descuentoConfModal(id,id_cic_tur_sel){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		$('#frm_descuento #id_cct').val(id_cic_tur_sel);
		_inputs('frm_descuento');
		//_llenarComboURL('api/confPagosCiclo/listarDescuentosCatalogo',$('#id_des'),id,null,function(){$('#id_des').change();});
		_llenar_combo({
			url:'api/confPagosCiclo/listarDescuentosCatalogo',
			combo:$('#id_des'),
			text:'nom',
			context: _URL,
			//id:id,
			function(){
				$('#id_des').change();
			}
		});	
		$('#frm_descuento #btn-grabar').on('click',function(event){
			event.preventDefault();
			var validation = $('#frm_descuento').validate(); 
			if ($('#frm_descuento').valid()){
				/*if($("[name='nro_cuota']:checked").length==0){
					swal({
						  title: "IMPORTANTE",
						  html:true,
						  text: "Debe seleccionar la(s) cuota(s) a la cual es aplicable el descuento.",
						  icon: "info",
						  button: "Cerrar",
						});
				} else{*/
					_post($('#frm_descuento').attr('action') , $('#frm_descuento').serialize(),
					function(data){
							onSuccessSave(data) ;
						}
					);
				//}	
			}		
		});
		_get('api/confPagosCiclo/obtenerDatosConfPago/' + id_cic_tur_sel,
			function(data){
			var nro_cuotas=data.num_cuotas;
			if(nro_cuotas){
				/*for (var i=1;i<= nro_cuotas; i++) {
					var div="<p><input type='checkbox' id='nro_cuota'"+i+" name='nro_cuota' value="+i+" />"+i+"</p>";
					$("#tab_cuotas").append(div);
				}*/
				var valor = $('#nro_cuota_max').find("option:first-child").val();
										var _id=null;
										if (valor==''){
											$('#nro_cuota_max').find('option').not(':first').remove();
										}else{
											$('#nro_cuota_max').empty();
										}
											for(var i=1;i<=nro_cuotas;i++)
											{
												var id_val = i;
												var value = i;
												$('#nro_cuota_max').append('<option value="'+ id_val +'">' +value + '</option>');
											}

											  if (typeof _id != 'undefined'&& _id!=null ){
												  if($('#nro_cuota_max').hasClass("select-search"))
														$('#nro_cuota_max').val(_id).change();
												  else
														$('#nro_cuota_max').val(_id);
											 }
											  if (typeof funcion != 'undefined'&& funcion!=null ){
												  funcion();
											  }	
			}
			if (id){
			_get('api/confPagosCiclo/obtenerDatosDescuento/' +id,
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form'));
				_llenar_combo({
					url:'api/confPagosCiclo/listarDescuentosCatalogo',
					combo:$('#id_des'),
					text:'nom',
					context: _URL,
					id:data.id_des,
					function(){
						$('#id_des').change();
					}
				});	
				var fec_venc= _parseDate(data.fec_venc);
				$('#frm_descuento #fec_venc').val(fec_venc);
				var valor = $('#nro_cuota_max').find("option:first-child").val();
										var _id=data.nro_cuota_max;
										if (valor==''){
											$('#nro_cuota_max').find('option').not(':first').remove();
										}else{
											$('#nro_cuota_max').empty();
										}
											for(var i=1;i<=nro_cuotas;i++)
											{
												var id = i;
												var value = i;
												$('#nro_cuota_max').append('<option value="'+ id +'">' +value + '</option>');
											}

											  if (typeof _id != 'undefined'&& _id!=null ){
												  if($('#nro_cuota_max').hasClass("select-search"))
														$('#nro_cuota_max').val(_id).change();
												  else
														$('#nro_cuota_max').val(_id);
											 }
											  if (typeof funcion != 'undefined'&& funcion!=null ){
												  funcion();
											  }	
				//Obtener las cuotas grabadas
				/*var cuotas=data.descuentoCuotas;
				if(cuotas.length>0){
					for (var i=0;i< cuotas.length; i++) {
						var nro_cuota_ex=cuotas[i].nro_cuota;
						//Encontrar el check y seleccionarlo
						$("input[name=nro_cuota]").each(function (index) {  
							if($(this).val()==nro_cuota_ex){
								//Si es igual le pongo check
								$(this).attr("checked", "checked");
							}
						});
					}	
				}	*/
				
			});
			}
		});
		
		
				
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		$('#frm_descuento #id').val(data.result);
		listar_descuentos(id_cic_tur_sel);

	}
	
	//Abrir el modal
	var titulo;
	//if (link.data('id'))
		titulo = 'Configuracion de Descuento';
	/*else
		titulo = 'Nuevo  Area';*/
	
	_modal(titulo, 'pages/tesoreria/fac_descuento_conf_modal.html',onShowModal,onSuccessSave);

}	
