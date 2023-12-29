//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
var _id_gpf=null;
var _ape_pat=null;
var _ape_mat=null;
function onloadPage(params){
	_onloadPage();
	//var tipo=params.tipo;
	var id_usr=_usuario.id;
	llenarTabla(id_usr);
	/*if(_id_gpf!=null){
		$('#frm-alumno_mant #btn_nuevo').click();
	}*/
	$('#div_aula').hide();
	$('#btn-editarMat').hide();
	$("#obs").hide();
	/*$('#frm-alumno_mant #btn_grabar').on('click',function(event){
		_post($('#frm-alumno_mant').attr('action') , $('#frm-alumno_mant').serialize(),function(data) {
			console.log(data);
			$('#frm-alumno_mant #cod').val(data.result.cod);
			if($("[name='tip_busqueda']:checked").length>0){
			valor=$('input[name="tip_busqueda"]:checked').val();
			llenarTabla(valor);
			}
		});
	});	*/
								
	//$("[name='tip_busqueda']").change(function () {
	
	function llenarTabla(id_usr){
				var param={tipBusqueda:'L', id_anio:$('#_id_anio').text(), id_gir:null, id_suc: null, id_niv:null};
				_get('api/alumno/busquedaAlumnos',function(data){
					$('#tabla-alumnos').dataTable({
					 data : data,
					 /*
					 destroy: true,
					 select: true,
					 bFilter: false,
					 */
					 searching: true, 
					 paging: false, 
					 info: false,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [ 
			        	 	//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Codigo", "data": "cod"},
							{"title":"Alumno", "data": "alumno"},
	    	        ],
	    	        "initComplete": function( settings ) {
					_initCompleteDT(settings);
					$("#total").text(data.length);
					$("#tabla-alumnos tbody tr").click(function(){
							form.steps("previous");
							form.steps("previous");
							form.steps("previous");
					$("#id_anio").val($('#_id_anio').text());
					   //$(this).addClass('selected').siblings().removeClass('selected');    
					   var codigo=$(this).find('td:nth-child(1)').html();
					   _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   console.log(data);
						   $("#frm-matricula #id_alu").val(data.id_alu);
						   $("#alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						   $("#btn-grabar_matricula").show();
						    $("#frm-matricula #id_gpf").val(data.id_gpf);
							listarMatriculas(data.id_alu);
							llenarDatos(data.id_gpf, null);
							$("input:checkbox[name='actyc']").on("click", function(e) {
							if($("#frm-matricula input[name='actyc']").is(':checked')){
								$("#frm-matricula #actyc").val(1);
								$("#btn_matricular").removeAttr('disabled');
							} else{
								$("#frm-matricula #actyc").val(0);
								$("#btn_matricular").attr('disabled','disabled');
							}
							});

							//Listar las matriculas del alumno

							
					   });
					}
					);

					$('.ok').on('click', function(e){
						alert($("#table tr.selected td:first").html());
					});
	    			 }
			});
		},param);	
	}

$('#frm-matricula #btn-consultar_vac').on('click',function(event){
	 consultar_vacantes();
});

$('#btn_nuevo').on('click',function(event){
	form.steps("previous");
	form.steps("previous");
	form.steps("previous");
		//$("#frm-matricula [name=actyc]").val('');
		$("#frm-matricula [name=actyc]").attr("checked",false);
		//$("#frm-matricula [name=srvint]").val('');
		$("#frm-matricula [name=srvint]").attr("checked",false);
		//$("#frm-matricula [name=camweb]").val('');
		$("#frm-matricula [name=camweb]").attr("checked",false);
		$('#frm-matricula #id').val('');
		$('#id_familias').attr("disabled", "disabled");
		$("#id_per_res").removeAttr('disabled');
		$("#id_gir").removeAttr('disabled');
		$("#id_per").removeAttr('disabled');
		//$("#id_suc").removeAttr('disabled');
		$('#id_cic').removeAttr('disabled');
		$('#id_tur').removeAttr('disabled');
		$("#id_per_res").val('').trigger('change');
		//$("#id_suc").val('').trigger('change');
		$("#id_gir").val('').trigger('change');
		$('#id_cic').val('').trigger('change');
		$("#id_per").val('').trigger('change');
		$('#id_grad').val('').trigger('change');
		$('#id_au').val('').trigger('change');
		$('#id_tur').val('').trigger('change');
		
		$('#div_aula').hide();
		$('#id_au').attr("disabled", "disabled");
		$('#resumen-tabla').html('');
		$('#total_costo').val('');
		//llenarDatos($('#frm-matricula #id_gpf').val(), null);
		
		//form.steps("previous");
});

}

function listarResumenPagos(id_cct, id_alu, id_per){
	var param = {
		id_cct : $('#id_cct').val(),
		nro_cuotas : $('#nro_cuo').val(),
		id_alu : id_alu,
		id_per : id_per
		};
	_get("api/confPagosCiclo/ResumendePagos", function(data) {
		$('#resumen-tabla').dataTable(
								{	data : data.listPagos,
									aaSorting : [],
									destroy : true,
									orderCellsTop : true,
									select : true,
									bLengthChange : false,
									bPaginate : false,
									bFilter : false,
									bInfo : false,
									columns : [
											{"title" : "Nro","render" : function(data,type,row,meta) {return parseInt(meta.row) + 1;}},
											{"title" : "Descripción","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return '<B>'+row.descripcion+'</B>';
												else if (row.tip == 'DESC')
													return '<B>'+row.descripcion+'</B>';
												/*else if (row.tip == 'ING')
													return '<B>CUOTA DE INGRESO</B>';
												else if (row.tip == 'LOC')
													return '<B>DIFERENCIA POR CAMBIO DE LOCAL</B>';
												else if (row.tip == 'DEV_LOC')
													return '<B>DEVOLUCIÓN POR CAMBIO DE LOCAL</B>';*/
												else 
													return null;}},
											{"title" : "Monto","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return 'S/.' + $.number(row.monto,2);
												else if (row.tip == 'DESC')
													return '<font color="red">- S/.' + $.number(row.monto,2)+'</font>';
												else
													return null; }}//,
											/*{"title" : "Nro Recibo","render" : function(data,type,row) {
												if (row.nro_rec==null && _data.matricula.mat_val==0)
													return 'PENDIENTE'; 
												else if (row.nro_rec==null && _data.matricula.mat_val==1)
													return ''; 
												else 
													return row.nro_rec;	
											}},*/
									]
					});
					//alert(data.monto_total);
					$('#total_costo').val(data.monto_total);
		
	},param);	
}	

function consultar_vacantes(){
	
	if($('#id_grad').val()!=''){
						if($('#id_au').val()!='' && $('#id_au').val()!=null){
							var param = {
								id_cic : $('#id_cic').val(),
								id_grad : $('#id_grad').val(),
								id_au : $('#id_au').val()
								};
							_get("api/matricula/capacidadAcadVac", function(data) {

								console.log(data);
								if(data!=null){
									var nro_vac = data;
									//alert('nro_vac'+nro_vac);
									if(nro_vac>0){
										if($('#id_mat').val()!=null){
											
											swal({
											  title: "IMPORTANTE",
											  html:false,
											  type : "success",
											  text: "Existen " + nro_vac+ " vacantes.",
											  icon: "info",
											  button: "Cerrar",
											});
											$("#btn-editarMat").removeAttr('disabled');
										} else{
											swal(
											{
												title : "Desea continuar con el proceso de PRE MATRÍCULA?",
												text :"Existen <font>" + nro_vac+ "</font> vacantes.",
												type: "success",
												showCancelButton : true,
												confirmButtonColor : "rgb(33, 150, 243)",
												cancelButtonColor : "#EF5350",
												confirmButtonText : "Si, Continuar",
												cancelButtonText : "No, Salir!",
												closeOnConfirm : true,
												closeOnCancel : true,
												html:true
											},
											function(isConfirm) {

												if (isConfirm) {
													form.steps("next");

												}else{
														$(".modal .close").click();
													}
												}
											);
										}
										
									} else {
										if($('#id_mat').val()!=null){
											swal({
											  title: "IMPORTANTE",
											  html:false,
											  type : "warning",
											  text: "No existen vacantes disponibles en este aula, por favor seleccionar otro. Gracias!",
											  icon: "info",
											  button: "Cerrar",
											});
											$("#btn-editarMat").attr('disabled', 'disabled');
										} else{
											swal({
											  title: "IMPORTANTE",
											  html:false,
											  type : "warning",
											  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;
										}	
										
									}	
								}
							},param);				
						} else if($('#id_grad').val()!=null && $('#id').val()==''){
							var param = {
								id_cic : $('#id_cic').val(),
								id_grad : $('#id_grad').val()
								//id_au : $('#id_au').val()
								};
							_get("api/matricula/capacidadAcadVac", function(data) {

								console.log(data);
								if(data!=null){
									var nro_vac = data;
									//alert('nro_vac'+nro_vac);
									if(nro_vac>0){
										swal(
										{
											title : "Desea continuar con el proceso de matrícula?",
											text :'Existen <font id="_seconds">' + nro_vac+ '</font> vacantes.',
											type: "success",
											showCancelButton : true,
											confirmButtonColor : "rgb(33, 150, 243)",
											cancelButtonColor : "#EF5350",
											confirmButtonText : "Si, Continuar",
											cancelButtonText : "No, Salir!",
											closeOnConfirm : true,
											closeOnCancel : true,
											html:true
										},
										function(isConfirm) {

											if (isConfirm) {
												form.steps("next");

											}else{
													$(".modal .close").click();
												}
											}
										);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:false,
											  type : "warning",
											  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;
									}									
								}
								

							}, param);
						} else {
							swal({
								  title: "IMPORTANTE",
								  html:false,
								  type : "warning",
								  text: "Seleccione el aula por favor. Gracias!",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
						}

				} else {
					swal({
						  title: "IMPORTANTE",
						  html:true,
						  type : "warning",
						  text: "Por favor seleccionar el grado al que se desea matricular.",
						  icon: "info",
						  button: "Cerrar",
						});
					return false;								
				}
}	

function grabarMatricula(){
	//Debo de poner la resta en los cuadros siguientes
												var monto=0;
												$("input[name=monto]").each(function (index) {  
													var monto_cuota=parseFloat($(this).val());
													monto = monto +monto_cuota;
												});		
console.log(monto);
console.log($("#total_costo").val());	
var nro_cuota=$('#frm-matricula #nro_cuo').val();									
							//Valido si suma el total
							if(nro_cuota!=0){
							if(monto==$("#total_costo").val()){
															if($("[name='actyc']:checked").val()==1){
								if($('#frm-matricula #nro_cuo').val()!=''){
																	swal({
								title : "Esta seguro?",
								text : "Esta Ud. de acuerdo con realizar la pre matrícula del alumno:" + $("#alumno").text(),
								type : "warning",
								showCancelButton : true,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "",
								cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : true
							},
							function(isConfirm) {
								if (isConfirm) {
									//Validar si hay vacantes
									$('#id_gpf').removeAttr('disabled');
									if($('#id_grad').val()!=''){								
										var param = {
										id_cic : $('#id_cic').val(),
										id_grad : $('#id_grad').val()
										};
									_get("api/matricula/capacidadAcadVac", function(data) {

										console.log(data);
										if(data!=null){
											var nro_vac = data;
											//alert('nro_vac'+nro_vac);
											if(nro_vac>0){
												var validation = $('#frm-matricula').validate(); 
												if ($('#frm-matricula').valid()){
														/*_post($('#frm-matricula').attr('action') , $('#frm-matricula').serialize(),
														function(data){
																$('#frm-matricula #id').val(data.result);
																listarMatriculas($('#frm-matricula #id_alu').val());
															}
														);*/
														_POST({form:$('#frm-matricula'),
														  context:_URL,
														  msg_type:'notification',
														  msg_exito : 'Pre Matrícula exitosa.',
														  success:function(data){
																//$('#frm-matricula #id').val(data.result.id_mat);
																form.steps("next");
																swal({
																  title: "IMPORTANTE",
																  html:true,
																  type : "success",
																  text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
																  icon: "info",
																  button: "Cerrar",
																});
																_POST({url:'/api/familiar/enviarMensajexMatricula/' + data.result.id_mat+'/'+data.result.id_gpf+'/'+$('#_id_anio').text(),context:_URL});
																listarMatriculas($('#frm-matricula #id_alu').val());
																$('#id_gpf').attr("disabled", "disabled");
														 }, 
														 error:function(data){
															 _alert_error(data.msg);
															 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
															 $('#nro_dni').focus();*/
														}
													}); 
												}
											} else {
												swal({
													  title: "IMPORTANTE",
													  html:false,
													  type : "warning",
													  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
													  icon: "info",
													  button: "Cerrar",
													});
												return false;
											}									
										}
										

									}, param);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "Por favor seleccionar el grado al que se desea matricular.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;								
									}
								
								} else {
									swal({
										title : "Cancelado",
										text : "No se ha realizado la matricula",
										confirmButtonColor : "#2196F3",
										type : "error"
									});

									return false;
								}
								});
								} else {
									 swal({
									  title: "IMPORTANTE",
									  html:true,
									  text: "Porfavor seleccione el número de cuotas en el que desea pagar.",
									  icon: "info",
									  button: "Cerrar",
									});
									return false;
								}	

							} else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}
							} else {
								
								 swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "La suma de cuotas debe ser igual al monto a Pagar.",
								  icon: "info",
								  button: "Cerrar",
								});
								return false;
								
							}	
							} else {
															//if(monto==$("#total_costo").val()){
															if($("[name='actyc']:checked").val()==1){
							if($('#frm-matricula #nro_cuo').val()!=''){
																	swal({
								title : "Esta seguro?",
								text : "Esta Ud. de acuerdo con realizar la pre matrícula del alumno:" + $("#alumno").text(),
								type : "warning",
								showCancelButton : true,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "",
								cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : true
							},
							function(isConfirm) {
								if (isConfirm) {
									//Validar si hay vacantes
									$('#id_gpf').removeAttr('disabled');
									if($('#id_grad').val()!=''){								
										var param = {
										id_cic : $('#id_cic').val(),
										id_grad : $('#id_grad').val()
										};
									_get("api/matricula/capacidadAcadVac", function(data) {

										console.log(data);
										if(data!=null){
											var nro_vac = data;
											//alert('nro_vac'+nro_vac);
											if(nro_vac>0){
												var validation = $('#frm-matricula').validate(); 
												if ($('#frm-matricula').valid()){
														/*_post($('#frm-matricula').attr('action') , $('#frm-matricula').serialize(),
														function(data){
																$('#frm-matricula #id').val(data.result);
																listarMatriculas($('#frm-matricula #id_alu').val());
															}
														);*/
														_POST({form:$('#frm-matricula'),
														  context:_URL,
														  msg_type:'notification',
														  msg_exito : 'Pre Matrícula exitosa.',
														  success:function(data){
																//$('#frm-matricula #id').val(data.result.id_mat);
																form.steps("next");
																swal({
																  title: "IMPORTANTE",
																  html:true,
																  type : "success",
																  text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
																  icon: "info",
																  button: "Cerrar",
																});
																_POST({url:'/api/familiar/enviarMensajexMatricula/' + data.result.id_mat+'/'+data.result.id_gpf+'/'+$('#_id_anio').text(),context:_URL});
																listarMatriculas($('#frm-matricula #id_alu').val());
																$('#id_gpf').attr("disabled", "disabled");
														 }, 
														 error:function(data){
															 _alert_error(data.msg);
															 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
															 $('#nro_dni').focus();*/
														}
													}); 
												}
											} else {
												swal({
													  title: "IMPORTANTE",
													  html:false,
													  type : "warning",
													  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
													  icon: "info",
													  button: "Cerrar",
													});
												return false;
											}									
										}
										

									}, param);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "Por favor seleccionar el grado al que se desea matricular.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;								
									}
								
								} else {
									swal({
										title : "Cancelado",
										text : "No se ha realizado la matricula",
										confirmButtonColor : "#2196F3",
										type : "error"
									});

									return false;
								}
								});
								} else {
									 swal({
									  title: "IMPORTANTE",
									  html:true,
									  text: "Porfavor seleccione el número de cuotas en el que desea pagar.",
									  icon: "info",
									  button: "Cerrar",
									});
									return false;
								}	

							} else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}
							}
							//}	
	

}

function editarMatricula(){
	var id_mat=$('#frm-matricula #id').val();
							if($("[name='actyc']:checked").val()==1){
								swal({
								title : "Esta seguro?",
								text : "Se procederá a cambiar de aula al alumno:" + $("#alumno").text(),
								type : "warning",
								showCancelButton : true,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "Si, Cambiar",
								cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : true
							},
							function(isConfirm) {
								if (isConfirm) {
									//Validar si hay vacantes
									if($('#id_au').val()!=''){								
										var param = {
										id_cic : $('#id_cic').val(),
										id_grad : $('#id_grad').val(),
										id_au : $('#id_au').val()
										};
									_get("api/matricula/capacidadAcadVac", function(data) {

										console.log(data);
										if(data!=null){
											var nro_vac = data;
											//alert('nro_vac'+nro_vac);
											if(nro_vac>0){
											$('#btn-editarMat').removeAttr('disabled');
												var validation = $('#frm-matricula').validate(); 
												if ($('#frm-matricula').valid()){
														/*_post($('#frm-matricula').attr('action') , $('#frm-matricula').serialize(),
														function(data){
																$('#frm-matricula #id').val(data.result);
																listarMatriculas($('#frm-matricula #id_alu').val());
															}
														);*/
														/*var frm={};
														frm.id = $('#frm-matricula #id').val();
														frm.id_grad = $('#frm-matricula #id_grad').val();
														frm.id_au = $('#frm-matricula #id_au').val();*/
														_POST({
														context:_URL,
														url:'api/matricula/actualizarAulaGrado/'+$('#frm-matricula #id').val()+'/'+$('#frm-matricula #id_grad').val()+'/'+$('#frm-matricula #id_au').val(),
														//params:frm,
														contentType:"application/json",
														success: function(data){
															console.log(data);
															  form.steps("next");
															  form.steps("next");
															  listarMatriculas($('#frm-matricula #id_alu').val());
														 }, 
														 error:function(data){
															 console.log(data);
															 _alert_error(data.msg);
														}
													});	
												}
											} else {
												if($('#id_mat').val()!=null){
													swal({
													  title: "IMPORTANTE",
													  html:false,
													  type : "warning",
													  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
													  icon: "info",
													  button: "Cerrar",
													});
													$("#btn-editarMat").attr('disabled', 'disabled');
												}
											}									
										}
										

									}, param);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "Por favor seleccionar el aula al que se desea cambiar al alumno.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;								
									}
								
								} else {
									swal({
										title : "Cancelado",
										text : "No se ha cambiado de aula",
										confirmButtonColor : "#2196F3",
										type : "error"
									});

									return false;
								}
								});
							} else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}	

}

function listarMatriculas(id_alu){
var param={id_alu:id_alu, id_anio: $('#_id_anio').text()};
	_GET({ url: 'api/matricula/listarMatriculasCicloxAlumno/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			//   _cant_padres=data.length;
			console.log(data);
				$('#matriculas_alumno-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 searching: false, 
					 paging: false, 
					 info: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							//{"title":"Alumno", "data" : "alumno"},
							{"title":"Giro de Negocio", "data" : "giro"},
							{"title":"Ciclo", "data" : "ciclo"}, 
							{"title":"Nivel", "data" : "nivel"}, 
							
							{"title":"Aula", "data" : "grado"}, 
							{"title":"Sección", "render": function ( data, type, row ) {
								if(row.secc==null)
									return '<font color="red">Pre-Matrícula</font>';
								else
									return row.secc;
							}},	
							//{"title":"Sección", "data" : "secc"}, 
							{"title":"Turno", "data" : "turno"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								//return '<button onclick="editarOtroFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
								/*return '<button onclick="eliminarMatricula(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-danger btn-xs"><i class="icon-trash-alt position-left"></i> Eliminar</button>';
							 '<a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this,event)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'*/
							 if(row.id_au_asi!=null){
								  return '<div class="list-icons"><a href="#"  data-id="' + row.id + '" onclick="matricula_editar(this, event)" title="Editar" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a><a href="#"  data-id="' + row.id + '" onclick="inactivar_matricula(this, event)" title="Inactivar" class="list-icons-item"><i class="icon-minus-circle2 ui-blue ui-size" aria-hidden="true"></i></a>' //+ '<a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							 } else {
								 return '<a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this,event)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							 }
							
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
			}
		});
}

function inactivar_matricula(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	_POST({
		context:_URL,
		url:'api/matricula/inactivarMatricula/'+id,
		//params:frm,
		contentType:"application/json",
		success: function(data){
			
		 }, 
		 error:function(data){
			 console.log(data);
			 _alert_error(data.msg);
		}
	});
}

function matricula_eliminar(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	_delete('api/matricula/eliminarMatriculaAcadVac/' + $(link).data("id"),
			function(){
					listarMatriculas($('#frm-matricula #id_alu').val());
				}
			);
	
}

function llenarDatos(id_gpf, id_mat){
	if(id_mat!=null){
		var param={id_mat:id_mat};
	_get("api/matricula/matriculaAcadVacEditar", function(data) {
		_fillForm(data,$('#frm-matricula'));
		$("#frm-matricula [name=actyc]").val([data.actyc]);
		$("#frm-matricula [name=srvint]").val([data.actyc]);
		$("#frm-matricula [name=camweb]").val([data.actyc]);
		$('#id_familias').attr("disabled", "disabled");
		$('#id_per_res').attr("disabled", "disabled");
		$('#id_gir').attr("disabled", "disabled");
		$('#id_gir').attr("disabled", "disabled");
		//$('#id_suc').attr("disabled", "disabled");
		$('#id_per').attr("disabled", "disabled");
		$('#id_cic').attr("disabled", "disabled");
		$('#id_tur').attr("disabled", "disabled");
		//var id_gpf=$('#frm-matricula #id_gpf').val();
							$("input:checkbox[name='actyc']").on("click", function(e) {
							if($("#frm-matricula input[name='actyc']").is(':checked')){
								$("#frm-matricula #actyc").val(1);
								$("#btn_matricular").removeAttr('disabled');
							} else{
								$("#frm-matricula #actyc").val(0);
								$("#btn_matricular").attr('disabled','disabled');
							}
							});
						   //Cargamos los datos
						   $('#id_per_res').on('change',function(event){
								var parentesco = $('#id_per_res option:selected').attr('data-aux1');
								var celular = $('#id_per_res option:selected').attr('data-aux2');
								var tipo_doc = $('#id_per_res option:selected').attr('data-aux4');
								var nro_doc= $('#id_per_res option:selected').attr('data-aux3');
								var correo = $('#id_per_res option:selected').attr('data-aux5');
								$('#id_fam_par').val(parentesco);
								$('#cel').val(celular);
								$('#corr').val(correo);
								$('#id_fam_dni').val(nro_doc);
								_llenarCombo('cat_tipo_documento',$('#id_tdc'), tipo_doc,null,function(){
								$('#id_tdc').change();
							});
							});
							
						   var param={id_gpf:$('#frm-matricula #id_gpf').val(), id_alu:$('#frm-matricula #id_alu').val()};
							_llenarComboURL('api/alumno/listarTodosIntegrantesFamilia', $('#id_per_res'),
								data.id_per_res, param, function() {
									$('#id_per_res').change();
							});
							$('#id_familias').attr("disabled", "disabled");								
							_llenar_combo({
							url:'api/alumno/listarFamilias',
							combo:$('#id_familias'),
							text:'nom',
							context: _URL,
							id:$('#frm-matricula #id_gpf').val(),
							function(){
								$('#id_familias').change();
							}
							});	

							if(data.id_au_asi!=null){
								$('#div_aula').show();
								$('#btn-editarMat').show();
								
							}
							$('#id_grad').on('change',function(event){
									var niv=null;
									var id_gra =  $(this).val();
									if(id_gra==4 || id_gra==1 || id_gra==2 || id_gra==3){
										niv='INICIAL';
									} else if (id_gra==10 || id_gra==5 || id_gra==6 || id_gra==7 || id_gra==8 || id_gra==9){
										niv='PRIMARIA';
									} else if (id_gra==11 || id_gra==12 || id_gra==13 || id_gra==14 || id_gra==15 || id_gra==16 || id_gra==17 || id_gra==18 || id_gra==19 || id_gra==20 || id_gra==21 || id_gra==22){
										niv='SECUNDARIA';
									}
									
									var param2={nivel:niv};
									_llenar_combo({
										url:'api/colegio/listarcolegiosDistrito',
										params: param2,
										combo:$('#id_col'),
										context:_URL,
										text:'value',
										id: data.id_col
										/*funcion:function(){
											$('#id_eva').change();
										}*/
									});
							});	
							
							$("#id_grad").off("change");
							$('#id_grad').on('change',function(event){
								//$('#id_grad').empty();
								var param={id_cic:$('#id_cic').val(),id_grad:$('#id_grad').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),
								data.id_au_asi, param, function() {
									$('#id_au').change();
								});
							});		
							
							$("#id_tur").off("change");
							$('#id_tur').on('change',function(event){
								var param={id_cic:$('#id_cic').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarGradosxTurnoCiclo', $('#id_grad'),
								data.id_gra, param, function() {
									$('#id_grad').change();
								});
								//}
							});	

							$("#id_cic").off("change");
							$('#id_cic').on('change',function(event){
								var param1={id_cic: $('#id_cic').val()};
								_llenarComboURL('api/periodo/listarTurnosExistentesxCiclo',$('#id_tur'),data.id_tur,param1,function(){
									$('#id_tur').change();
								});
								var id_niv=$('#id_cic option:selected').attr("data-aux2");
								$('#id_niv').val(id_niv);
								/*var id_niv=$('#id_cic option:selected').attr("data-aux2");
								_llenarCombo('cat_grad_todos',$('#id_grad'),data.id_gra,id_niv,function(){$('#id_grad').change();});
								$('#id_niv').val(id_niv);*/
							});	
							
							$("#id_per").off("change");
							$('#id_per').on('change',function(event){
								var param={id_per:$('#id_per').val()};
								_llenarComboURL('api/periodo/listarCiclosCombo', $('#id_cic'),
								data.id_cic, param, function() {
									$('#id_cic').change();
								});
							});	
							
							/*$('#id_suc').on('change',function(event){
								
							});
							
							_llenarCombo('ges_sucursal',$('#id_suc'),data.id_suc,null,function(){
								$('#id_suc').change();
							});*/
							$("#id_gir").off("change");
							$('#id_gir').on('change',function(event){
								var id_gir=$('#id_gir').val();
								if(id_gir==3){
									$('#tipo').val('V');
									$('#lbl_grado').text('Grado');
								} else if(id_gir==2){
									$('#tipo').val('A');
									$('#lbl_grado').text('Aula');
								}
								
								var param={id_anio:id_anio, id_gir:$('#id_gir').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									id: data.id_per,
									funcion:function(){
										$('#id_per').change();
									}
								});
								
							});	
							
							//_llenarCombo('ges_giro_negocio',$('#id_gir'),data.id_gir,null,function(){
								$('#id_gir').change();
							//});
	},param);
	} else{
							$("input:checkbox[name='actyc']").on("click", function(e) {
							if($("#frm-matricula input[name='actyc']").is(':checked')){
								$("#frm-matricula #actyc").val(1);
								$("#btn_matricular").removeAttr('disabled');
							} else{
								$("#frm-matricula #actyc").val(0);
								$("#btn_matricular").attr('disabled','disabled');
							}
							});
						   //Cargamos los datos
						   $('#id_per_res').on('change',function(event){
								var parentesco = $('#id_per_res option:selected').attr('data-aux1');
								var celular = $('#id_per_res option:selected').attr('data-aux2');
								var tipo_doc = $('#id_per_res option:selected').attr('data-aux4');
								var nro_doc= $('#id_per_res option:selected').attr('data-aux3');
								var correo = $('#id_per_res option:selected').attr('data-aux5');
								$('#id_fam_par').val(parentesco);
								$('#cel').val(celular);
								$('#corr').val(correo);
								$('#id_fam_dni').val(nro_doc);
								_llenarCombo('cat_tipo_documento',$('#id_tdc'), tipo_doc,null,function(){
								$('#id_tdc').change();
							});
							});
							
						   var param={id_gpf:$('#frm-matricula #id_gpf').val(), id_alu:$('#frm-matricula #id_alu').val()};
							_llenarComboURL('api/alumno/listarTodosIntegrantesFamilia', $('#id_per_res'),
								null, param, function() {
									$('#id_per_res').change();
							});

							$('#id_familias').attr("disabled", "disabled");			
							_llenar_combo({
							url:'api/alumno/listarFamilias',
							combo:$('#id_familias'),
							text:'nom',
							context: _URL,
							id:$('#frm-matricula #id_gpf').val(),
							function(){
								$('#id_familias').change();
							}
							});	
														
							/*$('#id_grad').on('change',function(event){
								var param={id_cic:$('#id_cic').val(),id_grad:$('#id_grad').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),
								data.id_au, param, function() {
									$('#id_au').change();
								});
							});*/
							
							$('#nro_cuo').on('change',function(event){
								$('#cuotas-tabla').html('');
								//$('#cuotas-tabla').find('tbody').html('');
								var nro_cuotas=$('#nro_cuo').val();
								if(nro_cuotas){
									var id_cct=$('#id_tur option:selected').attr("data-aux1");
									if(nro_cuotas==0){
										$("#total_costo").removeAttr('readonly');
										_get("api/confPagosCiclo/obtenerDatosConfPago/"+id_cct, function(data) {
											$("#total_costo").val(data.costo);
											$("#obs").show();
										});
										
									} else {
									// $("#total_costo").removeAttr('readonly');
										for (var i=1;i<= nro_cuotas; i++) {
										var monto=300;
										var fec_venc='19/01/2021';
										var estado='cancelado';
										//var div="<p><input type='checkbox' id='nro_cuota'"+i+" name='nro_cuota' value="+i+" />"+i+"</p>";
										var div="<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Cuota"+i+"</label><input type='hidden' name='nro_cuota' id='nro_cuota"+i+"' value="+i+"><input type='text' name='monto' id='monto"+i+"' value="+monto+" class='form-control' >"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Fecha Límite de Pago</label><input type='text' name='fec_venc' id='fec_venc"+i+"' value="+fec_venc+" class='form-control ' readonly='readonly'>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Banco</label><select name='id_bco_pag' id='id_bco_pag"+i+"' data-placeholder='Seleccione' class='form-control select-search' required><option value=''>Seleccione</option><option value='1'>Banco de Crédito del Perú</option><option value='2'>Banco Continental</option></select>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Estado</label><input type='text' name='estado' id='estado"+i+"' value="+estado+" class='form-control' readonly='readonly'>"
													+"</div>"
												+"</div>"

										$("#cuotas-tabla").append(div);
												var param={id_cct:id_cct, nro_cuo:i,nro_cuo_total:nro_cuotas, id_alu:$('#frm-matricula #id_alu').val(), id_per:$('#frm-matricula #id_per').val()};
												/*var div=null;
												var id_cuota=null;
												var costo=cuota_men;
												var fec_venc='19/01/2021';*/
												//crearTabla(j,id_cuota,costo, fec_venc);
												_get('api/confPagosCiclo/obtenerDatosCuotaxCiclo',function(data){
												console.log(data);
												
												//if(data){
													//alert('entroooo');
													monto=(data.monto_total!=null) ? data.monto_total: '';
													//alert(id_cuota);
													//costo=(data.costo!=null) ? data.costo: cuota_men;
													fec_venc=(data.fec_venc!=null) ? data.fec_venc: '19/01/2021'; 
													estado=(data.estado!=null) ? data.estado: '';
													var numero_cuota=data.nro_cuota;
												//}
												
												$("input[name=nro_cuota]").each(function (index) {  
													if($(this).val()==numero_cuota){
														//Si es igual le pongo check
														$("#monto"+numero_cuota).val(monto);
														$("#fec_venc"+numero_cuota).val(fec_venc);
														$("#estado"+numero_cuota).val(estado);
														//$("#fec_venc"+numero_cuota).val(fec_venc);
														/*$('.daterange-single').daterangepicker({ 
															singleDatePicker: true,
															autoUpdateInput: true,
															locale: { format: 'DD/MM/YYYY'}
														});*/
													}
												});
																
												//crearTabla(j,id_cuota,costo, fec_venc);
												},param);
												
													
									}
									
									listarResumenPagos(id_cct, $('#frm-matricula #id_alu').val(), $('#frm-matricula #id_per').val());
									}
									
									
								}
							});	
							//$("#id_tur").off("change");
							$('#id_grad').on('change',function(event){
									var niv=null;
									var id_gra =  $(this).val();
									if(id_gra==4 || id_gra==1 || id_gra==2 || id_gra==3){
										niv='INICIAL';
									} else if (id_gra==10 || id_gra==5 || id_gra==6 || id_gra==7 || id_gra==8 || id_gra==9){
										niv='PRIMARIA';
									} else if (id_gra==11 || id_gra==12 || id_gra==13 || id_gra==14 || id_gra==15 || id_gra==16 || id_gra==17 || id_gra==18 || id_gra==19 || id_gra==20 || id_gra==21 || id_gra==22){
										niv='SECUNDARIA';
									}
									
									var param2={nivel:niv};
									_llenar_combo({
										url:'api/colegio/listarcolegiosDistrito',
										params: param2,
										combo:$('#id_col'),
										context:_URL,
										text:'value',
										/*funcion:function(){
											$('#id_eva').change();
										}*/
									});
							});	
							$("#id_tur").off("change");
							$('#id_tur').on('change',function(event){
								$('#id_grad').empty();
								var param={id_cic:$('#id_cic').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarGradosxTurnoCiclo', $('#id_grad'),null, param, function() {
									$('#id_grad').change();
								});
								var nro_cuo=$('#id_tur option:selected').attr("data-aux2");
								var id_cct=$('#id_tur option:selected').attr("data-aux1");
								$('#id_cct').val(id_cct);
								if(nro_cuo== 'undefined'){
									swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "No existen configurado costos para este ciclo en este turno, por favor comunicarse con el Administrador.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;
								} else {
										var valor = $('#nro_cuo').find("option:first-child").val();
										var _id=null;
										if (valor==''){
											$('#nro_cuo').find('option').not(':first').remove();
										}else{
											$('#nro_cuo').empty();
										}
											for(var i=1;i<=nro_cuo;i++)
											{
												var id = i;
												var value = i;
												$('#nro_cuo').append('<option value="'+ id +'">' +value + '</option>');
											}
												$('#nro_cuo').append('<option value="0">OTRO</option>');

											  if (typeof _id != 'undefined'&& _id!=null ){
												  if($('#nro_cuo').hasClass("select-search"))
														$('#nro_cuo').val(_id).change();
												  else
														$('#nro_cuo').val(_id);
											 }
											  if (typeof funcion != 'undefined'&& funcion!=null ){
												  funcion();
											  }	
								}
								$('#nro_cuo').change();								
							});	

							
							
							$("#id_cic").off("change");
							$('#id_cic').on('change',function(event){
								var param1={id_cic: $('#id_cic').val()};
								_llenarComboURL('api/periodo/listarTurnosExistentesxCiclo',$('#id_tur'),null,param1,function(){
									$('#id_tur').change();
								});
								var id_niv=$('#id_cic option:selected').attr("data-aux2");
								$('#id_niv').val(id_niv);
								/*var id_niv=$('#id_cic option:selected').attr("data-aux2");
								_llenarCombo('cat_grad_todos',$('#id_grad'),null,id_niv,function(){$('#id_grad').change();});
								$('#id_niv').val(id_niv);*/
								
							});	

							//$("#id_per").off("change");
							$("#id_per").off("change");
							$('#id_per').on('change',function(event){
								var param={id_per:$('#id_per').val()};
								_llenarComboURL('api/periodo/listarCiclosCombo', $('#id_cic'),
								null, param, function() {
									$('#id_cic').change();
								});
							});	
							
							/*$('#id_suc').on('change',function(event){
								
							});	*/
							
							/*_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
								$('#id_suc').change();
							});*/
							$("#id_gir").off("change");
							$('#id_gir').on('change',function(event){
								var id_gir=$('#id_gir').val();
								if(id_gir==3){
									$('#tipo').val('V');
									$('#lbl_grado').text('Grado');
								} else if(id_gir==2){
									$('#tipo').val('A');
									$('#lbl_grado').text('Aula');
								}
								
								var param={id_anio:id_anio, id_gir:$('#id_gir').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									//id: data.id_per,
									funcion:function(){
										$('#id_per').change();
									}
								});
								
							});	
							
						//	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
								$('#id_gir').change();
						//	});
	}		
	
}	

function matricula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	form.steps("previous");
	form.steps("previous");
	$("#btn-grabar_matricula").hide();
	llenarDatos($('#frm-matricula #id_gpf').val(), id);

	
}	
	
	var form = $(".steps-validation").show();
	
	$(".steps-validation").steps(
				{	headerTag : "h6",
					bodyTag : "fieldset",
					transitionEffect : "fade",
					titleTemplate : '<span class="number">#index#</span> #title#',
					autoFocus : true,
					onStepChanging : function(event, currentIndex, newIndex) {

						if (currentIndex == 0) {
							//alert();
							//if($("[name='tc_acept1']:checked").length==0){
							if($('#id_alu').val()==""){
								swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Falta seleccionar al alumno(a).",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}	
							if($("[name='actyc']:checked").val()==0){
							swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							} 
							/*var cantidadPadres = $('.btnPadre').length;	
							var _cant_pad_act=0;
							//alert('cantidadPadres'+cantidadPadres);
							$( ".btnPadre" ).each(function( index ) {
								 // alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_pad_act=_cant_pad_act+1;
								  }
							});
							//alert(_cant_pad_act);
							
							var cantidadAlumnos=$('.btnAlumno').length;
							//alert('cantidadAlumnos'+cantidadAlumnos);
							var _cant_alu_act=0;
							$( ".btnAlumno" ).each(function( index ) {
								  //alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_alu_act=_cant_alu_act+1;
								  }
							});
							//alert(_cant_alu_act);
							//Regla de Negocio para verificar si la actualizacion de datos es obligatorio
							_get('api/reglasNegocio/validarActualizacionDatos',
								function(data){
								if(data.val=="1")	
										if(cantidadPadres==_cant_pad_act && cantidadAlumnos==_cant_alu_act){
											return true;
										} else{
											alert('Debe de actualizar todos los datos de los padres y alumnos, para proceder con la matricula!!');
											return false;
										}	
											
								});*/
											
						}

						if (currentIndex == 1 && newIndex == 2) {
							
						}
						if (currentIndex === 2 && newIndex === 3) {
							//console.log('DDD');
							//form.steps("previous");
								//alert ("ventana para matricular");	
								
						}

						// Forbid next action on "Warning" step if the user is
						// to young
						/*if (newIndex === 3 && Number($("#age-2").val()) < 18) {
							console.log('AAA');
							return false;
							
						}*/

						// Needed in some cases if the user went back (clean up)
						if (currentIndex < newIndex) {
							console.log('BBB');
							// To remove error styles
							form.find(".body:eq(" + newIndex + ") label.error")
									.remove();
							form.find(".body:eq(" + newIndex + ") .error")
									.removeClass("error");
						}

						form.validate().settings.ignore = ":disabled,:hidden";
						return form.valid();
					},

					onStepChanged : function(event, currentIndex, priorIndex) {

						/*//TAB DE PAGOS
						if (currentIndex === 1) {
						}

						/*if (currentIndex === 2 && priorIndex === 3) {
							console.log('DDD');
							form.steps("previous");
						}*/
					},

					onFinishing : function(event, currentIndex) {
						console.log('EEE');
						form.validate().settings.ignore = ":disabled";
						return form.valid();
					},

					onFinished : function(event, currentIndex) {
						alert("Submitted!");
					}
				});

$('a[href$="next"]').text('Siguiente >>');
$('a[href$="previous"]').text('<< Anterior ');