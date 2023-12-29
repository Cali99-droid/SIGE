//Se ejecuta cuando la pagina q lo llama le envia parametros

var _btnSiguienteHabil = false;
var _data;
var _cant_padres;
var _PARENTESCO_PAPA = "2";
var _PARENTESCO_MAMA = "1";
var _GENERO_FEMENINO = "0";
var _GENERO_MASCULINO = "1";
var _id_gpf;
var _cant_actualizaciones;
var _cant_alu_mat=0;
var _cant_pag=0;
var _cant_mat;
var _num_contrato;
var _id_dep_viv;
var _id_pro_viv;
var _id_dist_viv;
var _dir;
var _ref;
var _id_apod;
var _cs;
var _cl;
var _id_par;
var _primeraCarga = false;//true: cuando se jala los datos desde data
var _id_bco_pag;
//false: cuando el usuario ya jalo los datos y puede cambiar los datos del combo
function onloadPage(params) {
	console.log(params);
	 var id_gpf=params.id_gpf;
	$('#frm-matricula #id_anio').val($('#_id_anio').text());
	$("#div_actualizacion_padres").load("pages/matricula/mat_actualizacion_datos.html", {}, function(responseText, textStatus, req){
		llenarDatosFamilia(id_gpf);
		listarPadres();
		$("#frm_datos_familiar").css('display','none');
		$("#alu_familiar_alumno-frm #btn-grabar").css('display','none');		
		_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + id_gpf + "&id_alu="+params.id_alu,
				function(data){
				$("#fam_alumnos-tab").load("pages/matricula/mat_actualizacion_dato_alu.html", {}, function(responseText, textStatus, req){
					console.log(data);
					mostrarDatosTabAlumno(data);
					$('#alu_alumno-frm #id_gpf').val(id_gpf);
				});
				
		});
		$("#fam_otros_familiares-tab").load("pages/alumno/alu_familiar_otro_fam.html", {}, function(responseText, textStatus, req){
				mostrarDatosTabOtroFam(id_gpf);
		});
	});
	_id_gpf=params.id_gpf;
	
	// console.log(id_gpf);
			
	$("#id_alu").val(params.id_alu);
	$("#label_alumno").data('id', params.id_alu);
	$("#label_alumno").text(params.alumno);
	
	//Cargamnos los datos de matricula
	mostrarDatosMatricula(params.id_alu,params.id_mat, id_gpf);

	

	// pagos
	$('#abono').on('keyup', function(e) {
		var total = parseFloat($('#total').val());
		var abono = parseFloat($(this).val());
		var vuelto = abono - total;
		$('#vuelto').val(vuelto);
	});	
	
	/*_get('api/reglasNegocio/pagoObligatorio',
								function(data){
									console.log(data.val);
								/*if(data.val=="1"){	
									$("#div_pago_matricula").show();
									alert(1);
								}else{
									$("#div_pago_matricula").hide();
									alert(2);
								}*/
		//});
	
}

function llenarDatosFamilia(id_gpf){
					   _get('api/familiar/obtenerDatosFamiliaxGrupoFamiliar/'+id_gpf,function(data){
						   //habilitar_campos_familia();
						   $("#familia_datos-tab #btn_grabar").removeAttr('disabled');
						   //$("#tabs_familia").tabs("enable",1);
						   //$("#tabs_familia").tabs("enable",2);
							console.log(data);
						   $("#familia").text(data.nom);
						   //$("#frm-familiares_mant #familia").text(data.nom);
						   $("#familia_datos-tab #id_gpf").val(data.id_gpf);
						   $("#familia_datos-tab #id").val(data.id);
						   $("#familia_datos-tab #id_usr").val(data.id_usr);
						   $("#familia_datos-tab #cod").val(data.cod);
						   $("#familia_datos-tab #nom").val(data.nom);
						   $("#familia_datos-tab #cod_aseg").val(data.cod_aseg);
						   if(data.id_gen!=null){
								   $("#familia_datos-tab #id_gen").val(data.id_gen); 
								   $("#familia_datos-tab #id_gen").change();
						   }
							
							if(data.est=='A'){
								$("#est").val('A');
								$('#est').change();
							} else if(data.est=='I'){
								$("#est").val('I');
								$('#est').change();
							}	
								
							$("#familia_datos-tab #direccion").val(data.direccion);
							$("#familia_datos-tab #referencia").val(data.referencia);
								$('#familia_datos-tab #id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#familia_datos-tab #id_dist'),
										id:data.id_dist,
										funcion:function(){
											console.log('remove data');
											$('#familia_datos-tab #id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#familia_datos-tab #id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#familia_datos-tab #id_dep').on('change',function(event){
								
								 if($('#familia_datos-tab #id_dep').val()!=null && $('#familia_datos-tab #id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
									 _llenar_combo({
											url:'api/comboCache/provincias/' + $(this).val(),
											combo:$('#familia_datos-tab #id_pro'),
											id:data.id_pro,
											funcion:function(){
												$('#familia_datos-tab #id_pro').change();
											}
										}); 
								 }
							});
							_llenarCombo('cat_departamento',$('#familia_datos-tab #id_dep'),data.id_dep,null, function(){
								$('#familia_datos-tab #id_dep').change();
							});	
							
							_llenarCombo('cat_tipo_seguro',$('#familia_datos-tab #id_seg'), data.id_seg);
							_llenarCombo('cat_centro_salud',$('#familia_datos-tab #id_csal'), data.id_csal);
							
							//Listar a los familiares
							/*var param={id_gpf:data.id};
							_llenarComboURL('api/alumno/listarTodosFamiliares', $('#id_fam_emer'),
								data.id_fam_emer, param, function() {
									 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
									 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
									$('#id_fam_emer').change();
							});		*/		
		});
	}
	
function grabarFamilia(obj){
	if($("#familia_datos-tab #id_dist").val()!=""){
		if($("#familia_datos-tab #direccion").val()!=""){
			if($("#familia_datos-tab #referencia").val()!=""){
				$('#familia_datos-tab #btn_grabar').attr("disabled", "disabled");
					var frm_familia=$(obj).parent().parent().parent();
					//var dni=frm_entrevista.find("#nro_dni").val();
					var json_familia={};
					  json_familia.id=frm_familia.find("#id").val();
					  json_familia.id_dep=frm_familia.find("#id_dep").val();
					  json_familia.id_pro=frm_familia.find("#id_pro").val();
					  json_familia.id_dist=frm_familia.find("#id_dist").val();
					  json_familia.direccion=frm_familia.find("#direccion").val();
					  json_familia.referencia=frm_familia.find("#referencia").val();
					  json_familia.id_seg=frm_familia.find("#id_seg").val();
					  json_familia.cod_aseg=frm_familia.find("#cod_aseg").val();
					  json_familia.id_csal=frm_familia.find("#id_csal").val();
					  json_familia.dir_csalud=frm_familia.find("#dir_csalud").val();
					   _POST({
							context:_URL,
							url:'api/gruFam/FamiliaGrabarReq',
							params:JSON.stringify(json_familia),
							contentType:"application/json",
							msg_type:'notification',
							msg_exito : 'se actualizó correctamente datos de la familia.',
							success: function(data){
								console.log(data);
								//$("#frm_datos_familiar").css('display','none');
								  $("#familia_datos-tab #id").val(data.id_gpf);
								//  $("#frm_datos_familiar #id_gpf").val(data.id_gpf);
								  $('#familia_datos-tab #btn_grabar').removeAttr('disabled');
								  $('#li-tabs-1').click();
								 // listarPadres();
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 $('#familia_datos-tab #btn_grabar').removeAttr('disabled');
								 //$('#alu_familiar_alumno-frm #btn-grabar').removeAttr('disabled');
								// $('#nro_dni').focus();
								//frm_entrevista.find("#id_ent_alu")
						}
					});	
		} else {
			alert('Escriba la referencia donde vive la familia');
			$('#familia_datos-tab #btn_grabar').removeAttr('disabled');
		}		
	} else{
		alert('Escriba la dirección donde vive la familia');
		$('#familia_datos-tab #btn_grabar').removeAttr('disabled');
	}	
	
}	else{
	alert('Seleccione el distrito donde vive la familia');
	$('#familia_datos-tab #btn_grabar').removeAttr('disabled');
}	
	
}	
	
function mostrarDatosMatricula(id_alu,id_mat,id_gpf){
	$("#frm-matricula #id_alu").val(id_alu);
	$('#id_perfil').val(_usuario.id_per);
	var _btnMatriculado = false;

	var _cambio_local = false;
	var _cambio_seccion = false;
	if(id_mat!=null){
		$("#btn-grabar_matricula").attr('disabled', 'disabled');
	}	
	//Por el año 2021 , deshabilitado Nivel y Grado
	$('#id_niv').attr('disabled', 'disabled');
	$('#id_gra').attr('disabled', 'disabled');
	var param_fam={id_anio:$("#_id_anio").text(), id_gpf:id_gpf };
	_get('api/matricula/obtenerFamiliarSugerido', function(data) {
		console.log(data);
		_id_apod=data[0].id_fam;
		_id_bco_pag=data[0].id_bco_pag;
		$('#apoderados-tabla').dataTable({
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
					{"title":"Apoderado", "data" : "familiar"}, 
					{"title":"Contrato", "render": function ( data, type, row ) {
							return '<button type="button" class="btn btn-primary" data-id="' + row.id_per + '" onclick="generarContrato(this, event)">Generar Contrato<i class="icon-arrow-right14 position-right"></i></button>';
					}}
		    ],
		    "initComplete": function( settings ) {
				   _initCompleteDT(settings);
			 }
	    });
		//alert(_id_apod);
		if(_id_apod==null){
			//alert('entro2');
					$('#id_fam_mat').removeAttr('disbled');
					$('#id_bco_pag').removeAttr('disbled');
					} else{
						$('#id_fam_mat').attr('disabled','disabled');
						$('#id_bco_pag').attr('disabled','disabled');
						//alert('deshabilitando');
					}
	}, param_fam);

	var param2 = {
			id_anio : $('#_id_anio').text(),
			id_alu : id_alu,
			id_mat : id_mat,
			id_suc : $("#_id_suc").text()
		}
		_get(
				'api/matricula/MatriculaEditar2',
				function(data) {
					$("#label_alumno").text(data.persona.ape_pat+' '+ data.persona.ape_mat+' '+data.persona.nom);
					console.log(data);
					_data = data;
					var id_au_actual = _data.matricula.id_au;
					var id_au_nueva =  _data.matricula.id_au_asi;

					_btnSiguienteHabil = true;

					var condicionada = data.condicionada;

					if (condicionada != null)
						new PNotify(
								{	title : 'AVISO IMPORTANTE!',
									text : condicionada,
									addclass : 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
									delay : 10500
								});
					console.log(_data.matricula.id);
					if (data.matricula.id!=null){
						//form.steps("next");
						_btnMatriculado = true;
						$("#btn-grabar_matricula").attr('disabled', 'disabled');
					} else{
						$("#btn-grabar_matricula").removeAttr('disabled');
					} 
					
					//}
						

					/*if (_btnMatriculado) {
						$('#id_fam_mat').attr('disabled', 'disabled');
						$('#id_au').attr('disabled', 'disabled');
						$('#id_bco_pag').attr('disabled', 'disabled');
						$("#btn-grabar_matricula").attr('disabled', 'disabled');
						//alert('deshabilitacion 1');
					} /*else{
						$('#id_fam').removeAttr('disabled'); 	
					}*/
					if ($("#frm-matricula").steps("getCurrentIndex") == 1) {
						

					}

					// eventos
					//Comentado para el 2021 q la matricula ya no es x aula
					/*$('#id_au').on('change', function() {
						console.log("capacidad");
						var id_au = $(this).val();
						if (id_au!=null) { //antes id_au != '' 
							// var url = "MatriculaCapacidad2/" + id_au;
							var param = {
								id_au : $('#id_au').val(),
								id_alu : $('#id_alu').val()
							};
							_get("api/matricula/capacidad", function(data) {

								console.log(data);
								if(data!=null){
									var capacidad = data.capacidad;
									var nro_vac = data.nro_vac;
									var id_au_sug = data.id_au_sug;
									var tip_cronograma = "AC";// data.tip_cronograma;

									$('#nro_vacantes').val(nro_vac);
									$('#lbl_nro_vacantes').html("Disponibles");
									$('#lbl_nro_vacantes').addClass("active");
									$('#spanCap').val("de " + capacidad);
																	
									if (true){
										$('#id_au').attr('disabled', 'disabled');
										//alert('deshabilitacion 2');
									}
									//Regla de Negocio para validar el cambio de aula (Habilitado o Deshabilitado)
									/*_get('api/reglasNegocio/validarCambioAula',
										function(data){
										if(data.val=="1"){*/
											//alert(_data.nuevo_sin_cronograma_cs);
							//cometnado 2021 if (_data.antiguo_sin_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs=="1"){ // && _data.matricula.id!=null Si es antiguo sin cronograma y hay cambio de seccion es libre , esto era antes (&& _data.matricula.id!=null)
												//alert(1);
								/*				$('#id_au').removeAttr('disabled'); 	
											}else if (_data.antiguo_con_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
											}else if(_data.nuevo_sin_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
											}else if(_data.nuevo_con_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
											}
											
											if ($('#nro_vacantes').val() == "" || parseFloat($('#nro_vacantes').val()) < 1) {
												_alert_error('No existen vacantes disponibles');
												$("#btn-grabar_matricula").attr('disabled', 'disabled');
												return false;
												
											} else{
												$("#btn-grabar_matricula").removeAttr('disabled');
											}
											
										/*}
											
									});*/
						/* comentado 2021			
								}
								

							}, param);
						} else{
							//Regla de Negocio para validar el cambio de aula (Habilitado o Deshabilitado)
							_get('api/reglasNegocio/validarCambioAula',
										function(data){
										if(data.val=="1"){
											if (_data.antiguo_sin_cronograma_cs == "1" && _data.matricula.id!=null){
												//alert('1');
												$('#id_au').removeAttr('disabled'); 	
											}else if (_data.antiguo_con_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
												//alert('2');
											}else if(_data.nuevo_sin_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
												//alert('3');
											}else if(_data.nuevo_con_cronograma_cs == "1" && _data.matricula.id!=null){
												$('#id_au').removeAttr('disabled');
												//alert('4');
											} 
										}
											
							});
						}
					});*/
					
					//Logica 2021 por grado, no se puede x las secretarias
					/*if (_data.antiguo_sin_cronograma_cs == "1" && _data.matricula.id==null){
						$('#id_suc_mat').removeAttr('disabled'); 	
					}else if (_data.antiguo_con_cronograma_cs == "1" && _data.matricula.id==null){
						$('#id_suc_mat').removeAttr('disabled');
						//alert('2');
					}else if(_data.nuevo_sin_cronograma_cs == "1" && _data.matricula.id==null){
						$('#id_suc_mat').removeAttr('disabled');
						//alert('3');
					}else if(_data.nuevo_con_cronograma_cs == "1" && _data.matricula.id==null){
						$('#id_suc_mat').removeAttr('disabled');
						//alert('4');
					} 
					
					$('#id_suc_mat').on('change',function(event) {
						$('#id_gra').change();
					});*/					
					
					$('#id_gra').on('change',function(event) {
						//comentado 2021
								/*var id_suc = $("#_id_suc").text();
								_llenarCombo('col_aula_local', $('#id_au'),data.matricula.id_au, $("#_id_anio").text() + ',' + $(this).val() + ',' + id_suc, function() {
										if (_data.matricula.id_au_asi !=null)
											$('#id_au').val(_data.matricula.id_au_asi);
										$('#id_au').change();
									});*/
						var param = {
								id_grad : $('#id_gra').val(),
								id_suc : $("#_id_suc").text(),
								id_niv : $('#id_niv').val(),
								id_anio : $('#_id_anio').text(),
								id_alu : $('#id_alu').val()
							};
						_get("api/matricula/capacidadxGrado", function(data) {

								console.log(data);
								if(data!=null){
									var capacidad = data.capacidad;
									var nro_vac = data.nro_vac;
									var id_au_sug = data.id_au_sug;
									var tip_cronograma = "AC";// data.tip_cronograma;

									$('#nro_vacantes').val(nro_vac);
									$('#lbl_nro_vacantes').html("Disponibles");
									$('#lbl_nro_vacantes').addClass("active");
									$('#spanCap').val("de " + capacidad);
																	
									//Regla de Negocio para validar el cambio de aula (Habilitado o Deshabilitado)
									/*_get('api/reglasNegocio/validarCambioAula',
										function(data){
										if(data.val=="1"){*/
											//alert(_data.nuevo_sin_cronograma_cs);
							 if (_data.antiguo_sin_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs=="1"){ // && _data.matricula.id!=null Si es antiguo sin cronograma y hay cambio de seccion es libre , esto era antes (&& _data.matricula.id!=null)
									$('#id_au').removeAttr('disabled'); 									
							}else if (_data.antiguo_con_cronograma_cs == "1" && _data.matricula.id!=null){
									$('#id_au').removeAttr('disabled'); 
							}else if(_data.nuevo_sin_cronograma_cs == "1" && _data.matricula.id!=null){
									$('#id_au').removeAttr('disabled'); 
							}else if(_data.nuevo_con_cronograma_cs == "1" && _data.matricula.id!=null){
									$('#id_au').removeAttr('disabled'); 
							}
							
							if ($('#nro_vacantes').val() == "" || parseFloat($('#nro_vacantes').val()) < 1) {
								_alert_error('No existen vacantes disponibles');
								$("#btn-grabar_matricula").attr('disabled', 'disabled');
								return false;
								
							} else{	
					
								//if(_data.matricula.mat_val!="1" && _data.matricula.id!=null){
								if(_data.pago!="1" && _data.matricula.id!=null){
									$("#btn-grabar_matricula").attr('disabled', 'disabled');
									//_alert_error('El alumno no ha hecho la el pago por derecho de matrícula');
									swal({
									  title: "IMPORTANTE",
									  html:true,
									  type : "warning",
									  text: "El alumno aun no ha hecho el pago por derecho de matrícula.",
									  icon: "info",
									  button: "Cerrar",
									});
								} else {
									$("#btn-grabar_matricula").removeAttr('disabled');
								}	
								
							}
											
									/*	}
											
										});*/
									}
							},param);
							
							var param = {
								id_grad : $('#id_gra').val(),
								id_suc : $("#_id_suc").text(),
								//id_niv : id_niv,
								id_anio : $('#_id_anio').text(),
								//id_alu : id_alu
								};
								
								console.log(param);
							//var param = {id_niv:id_niv, id_gir:id_gir};
							$('#id_cme').on('change',function(event) {
								console.log($('#id_cme option:selected').attr('data-id_cct'));
								$('#id_cct').val($('#id_cme option:selected').attr('data-id_cct'));
								
							});	
							_get('api/aula/listarModalidadesxLocalyGrado',function(data){
								console.log(data.length);
								if(data.length>0){
									$("#id_cme").html('');
									$("#id_cme").show();
									$("#id_cme").append('<option value="">Seleccione</option>');
									for ( var i in data) {
										
										$("#id_cme").append('<option value="'+ data[i].id + '" data-id_cct="'+data[i].aux1+'">' + data[i].value + '</option>');
									}
									if(_data.modalidad!=null){
										$("#id_cme").val(_data.modalidad).change();
									}	
								} else {
									$("#id_cme").html('');
									//alert('no hay');
									$("#id_cme").hide();
										swal({
													  title: "IMPORTANTE",
													  html:false,
													  type : "warning",
													  text: "No existe modalidades configuradas para dicho local, por favor seleccionar otro. Gracias!",
													  icon: "info",
													  button: "Cerrar",
													});
								}

							},param);
					});		

					$('#id_fam_mat').on('change',function(event) {
										var param = {id_fam : $(this).val()};
										
										_llenarComboURL('api/familiar/familiarParentesco', $('#id_fam_par'), null,param,
												function() { var nro_doc = $('#id_fam_par option:selected').attr('data-aux1');$('#id_fam_dni').val(nro_doc);
												});

										_llenarCombo('cat_cond_matricula',$('#id_con'), data.id_con, null);

										_llenarCombo('ges_sucursal', $('#id_suc_mat'),$("#_id_suc").text(), null);

										var param2 = {id_alu : id_alu,id_anio : $('#_id_anio').text() };
										
										_get("api/matricula/situacionAlumno",
												function(data) {
													console.log(data);
													_llenarCombo('cat_cliente',$('#id_cli'), data,null);
												}, param2);

										// numero de contrato correlativo //Ahora el contrato se calcula en caliente
										/*if ($(this).val() == "") {
											$('#num_cont').val("");
										} else {
											var param3 = {
												id_fam : $('#id_fam').val(),
												id_anio : $("#_id_anio").text(),
												id_suc : $("#_id_suc").text()
											};
											_get(
													"api/matricula/MatriculaContratoCorrelativo/",
													function(data) {
														var contrato = data.contrato;
														_num_contrato=contrato;
														$('#num_cont')
																.val(contrato);
														$('#llb_num_cont')
																.addClass('active');
													}, param3);
										}*/

									});

					var paramz = {
						id_fam : $('#id_fam').val()
					};
					_llenarComboURL('api/familiar/familiarParentesco',
							$('#id_enc_par'), null, paramz);

					$('#id_enc').on('change', function(event) {

						var familiar = $('#id_enc').find(":selected");
						// alert(familiar.val());
						var familiar_nro_doc = familiar.data('nro_doc');
						var familiar_id_par = familiar.data('id_par');

						$("#id_enc_par").val(familiar_id_par);
						$("#id_enc_dni").val(familiar_nro_doc);

					});
					_llenarCombo('cat_nivel', $('#id_niv'), data.matricula.id_niv, null);
					_llenarCombo('cat_grad', $('#id_gra'), data.matricula.id_gra,
							null, function() {
								$('#id_gra').change();
							});
					
					//alert(data.matricula.id_fam);
					
					var param = {
							id_alu : id_alu
						};
											
					if(data.matricula.id_fam!=null){
						_llenarComboURL('api/alumno/listarFamiliares', $('#id_fam_mat'),
								data.matricula.id_fam, param, function() {
									$('#id_fam_mat').change();
								});
					}else{
						
						_llenarComboURL('api/alumno/listarFamiliares', $('#id_fam_mat'),
								_id_apod, param, function() {
									$('#id_fam_mat').change();
								});
								
					}
					
					_llenarCombo('fac_banco',$('#id_bco_pag'),_id_bco_pag,null,function(){});					
					

					_get('api/alumno/listarOtrosFamiliares', function(data) {

						console.log(data);
						var combo = $('#id_enc');
						combo.empty();

						for ( var i in data) {
							var id = data[i].id;
							var value = data[i].ape_pat + data[i].ape_mat + ', '
									+ data[i].nom;

							combo.append('<option data-nro_doc="' + data[i].nro_doc
									+ '" data-id_par="' + data[i].id_par
									+ '"  value="' + id + '">' + value
									+ '</option>');

							// $('<option />', {value: data.id, text:
							// data.ape_pat}).appendTo(s);
						}

					}, param);
					/*
					 * _llenarComboURL('api/alumno/listarOtrosFamiliares',$('#id_enc'),data.matricula.id_enc,param,
					 * function(){$('#id_enc').change()} );
					 */
					$('#cronograma').val(data.cronograma);
					$('#aula_sugerida').val(data.aula_sugerida);
					$('#id').val(data.matricula.id);
					$('#id_au_sug').val(data.matricula.id_au);
					$('#id_sit').val(data.id_sit);
					$('#id_per').val(data.matricula.id_per);
					$('#antiguo_sin_cronograma').val(data.antiguo_sin_cronograma);
					$('#antiguo_con_cronograma').val(data.antiguo_con_cronograma);
					$('#llb_num_cont').addClass('active');
					$('#num_cont').val(data.matricula.num_cont);
					_inputs('frm-matricula');
					
					//if ((_data.antiguo_sin_cronograma_cs=="1" || _data.antiguo_con_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs == "1" || _data.nuevo_con_cronograma_cs == "1") && id_au_actual!=null && id_au_actual != id_au_nueva && (_data.matricula!=null && _data.matricula.periodo.id_suc ==$('#_id_suc').text())) {
					if ((_data.antiguo_sin_cronograma_cs=="1" || _data.antiguo_con_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs == "1" || _data.nuevo_con_cronograma_cs == "1") && (_data.matricula.id!=null && _data.matricula.periodo.id_suc ==$('#_id_suc').text())) {
						$("#btn-grabar_matricula").text('Cambiar Sección o Local');
						$("#btn-grabar_matricula").removeAttr('disabled');
						_cambio_seccion=true;	
						//alert(1);
					}
					
					if (_btnMatriculado && !_cambio_seccion) { //if if (_btnMatriculado && !_cambio_seccion) {
						//$('#pagardiv').hide();
						$("#btn-grabar_matricula").attr('disabled', 'disabled');
						//alert(2);
					} else if(_btnMatriculado && _cambio_seccion){
						$("#btn-grabar_matricula").removeAttr('disabled');
						//alert(3);
					} //else
					/*{
						$("#btn-grabar_matricula").removeAttr('disabled');
					}*/
					
					if(id_au_actual!=null && id_au_nueva!=null && id_au_actual!=id_au_nueva){
						_cs=true;
						_btnSiguienteHabil=false;
						//alert(4);
					} 
					/*alert('id_au_actual'+id_au_actual);
					alert('id_au_nueva'+id_au_nueva);*/
					//if(id_au_actual!=null && id_au_nueva!=null && id_au_actual!=id_au_nueva && _data.matricula.mat_val=='0'){
					if(id_au_actual!=null && id_au_nueva!=null && id_au_actual!=id_au_nueva && _data.pago=='0'){
						new PNotify(
								{	title : 'AVISO IMPORTANTE!',
									text : 'Para validar el cambio de local, se debe hacer el Pago Pendiente o el Reembolso.',
									addclass : 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
									delay : 10500
								});
					} 

					if (!_btnSiguienteHabil){
						return false;
					}else{
						return true;
					}
					
					
					/*var id_au_actual =null;
					var id_au_nueva = $('#id_au').val();*/
					
					
				}, param2);
	$('#pagardiv').hide();
	

	listar_hermanos(id_gpf);
	listar_hermanos_pagos(id_gpf);	
}

//editar familiares
//editar datos del familiar
function editarFamiliar(id_gpf, id_fam){
	
	/*$('.daterange-single').daterangepicker({ 
        singleDatePicker: true,
        autoUpdateInput: false,
        locale: { format: 'DD/MM/YYYY'}
    });*/
	
	console.log(id_fam);
	//_inputs('frm_datos_familiar');
	var params ={id_gpf:id_gpf, id_fam:id_fam};
	$("#frm_datos_familiar").css('display','block');
	$("#alu_familiar_alumno-frm #btn-grabar").css('display','block');
	//listar_padres(id_gpf);
	$('#frm_datos_familiar #nro_doc').attr('readonly', 'readonly');
	$('#frm_datos_familiar #ape_pat').attr('readonly', 'readonly');
	$('#frm_datos_familiar #ape_mat').attr('readonly', 'readonly');
	$('#frm_datos_familiar #nom').attr('readonly', 'readonly');
	$('#frm_datos_familiar #id_par').attr('disabled', 'disabled');
	$('#frm_datos_familiar #id_tdc').attr('disabled', 'disabled');
	//$('#alu_familiar-frm #id_tap').attr('disabled', 'disabled');
	//$(".id_tap").prop("disabled", true); 
	
	//$('#frm_datos_familiar input[name=id_tap]').attr("disabled",true);
	$('#frm_datos_familiar #id_gpf').val(id_gpf);
	if(_id_par!=null){
		$('#btn-direccion').css('display','block');
		if(_id_par=="1"){
			  $('#btn-direccion').text('Obtener Datos de Domicilio de la madre');
		  } else if(_id_par=="2"){
			  $('#btn-direccion').text('Obtener Datos de Domicilio del padre');
		  }
	} else{
		$('#btn-direccion').css('display','none');
	}
	  
	
	$('#viv').on('change',function(event){
		if($("#frm_datos_familiar #viv").val()==1){
			$("#frm_datos_familiar #div_defuncion").css('display','none');
			$('#fec_def_fam').val('');
			$('#fec_def_fam').attr("disabled", "disabled");
		} else if($("#frm_datos_familiar #viv").val()==0){
			$("#frm_datos_familiar #div_defuncion").css('display','block');
			$('#fec_def_fam').removeAttr("disabled");
		} else {
			$("#frm_datos_familiar #div_defuncion").css('display','none');
			$('#fec_def_fam').val('');
			$('#fec_def_fam').attr("disabled", "disabled");
		}		
	});
	
	$('#viv').change();
	
	//$("#alu_familiar-frm").css('display','none');
	
	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + id_gpf + "&id_alu=",
			function(data){
			
			$("#fam_alumnos-tab").load("pages/matricula/mat_actualizacion_dato_alu.html", {}, function(responseText, textStatus, req){
				mostrarDatosTabAlumno(data);
				$('#alu_alumno-frm #id_gpf').val(id_gpf);
			});
			
	});
	
	$("input:radio[name='id_tra_rem']").on("click", function(e) {
		var valor = $(this).val();
		if(valor==0){
			$("#div_trabajo").css('display','none');
		} else 	if(valor==1){
			$("#div_trabajo").css('display','block');
		}
	});
			
	//});
	/*_get('api/familiar/obtenerDatosFamiliar/' + _id_fam+'/'+null,
			function(data){*/	
		console.log(id_fam);
		var param ={id_fam:id_fam};
		_GET({ url: 'api/familiar/obtenerDatosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){	
				console.log(data);
				_fillForm(data,$('#frm_datos_familiar'));
				$("#foto").attr('src', 'data:image/png;base64,' + data.foto);
				$("#frm_datos_familiar #id_fam").val(id_fam);
				$("#frm_datos_familiar #viv").val(data.viv);
				var fec_nac = data.fec_nac;
				if(fec_nac){
					var arrFec_nac = fec_nac.split('-');
					$('#frm_datos_familiar #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
				}
				

				_inputs('frm_datos_familiar');
				//$("#frm_datos_familiar [name=id_tap]").val([data.id_tap]);
				$("#frm_datos_familiar [name=id_gen]").val([data.id_gen]);
					
				_llenarCombo('cat_tipo_documento',$('#frm_datos_familiar #id_tdc'), data.id_tdc,null,function(){
					$('#id_tdc').change();
				});
				
				$('#id_par').on('change',function(event){
					if($('#id_par').val()==_PARENTESCO_PAPA){
						$('#id_gen1').click();
					} else if($('#id_par').val()==_PARENTESCO_MAMA){
						$('#id_gen2').click();
					}
				});
				
				_llenar_combo({
					url:'api/familiar/llenarOpcionesMamayPapa',
					combo:$('#id_par'),
					text:'par',
					context: _URL,
					id:data.id_par,
					funcion(){
						$('#id_par').change();
					}
				});	
				_llenarCombo('cat_est_civil',$('#frm_datos_familiar #id_eci'), data.id_eci);
				_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
				_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);

				$('#id_pro').on('change',function(event){ 
					 if($('#id_pro').val()!=null){
							_llenar_combo({
								url:'api/comboCache/distritos/' + $(this).val(),
								combo:$('#id_dist'),
							   	id:data.id_dist,
							   	funcion:function(){
							   		$('#id_dist').change();
							   	}
							});		 
					 }
					 
				});
				
				$('#id_dep').on('change',function(event){
					 if($('#id_dep').val()!=null){
						 _llenar_combo({
								url:'api/comboCache/provincias/' + $('#id_dep').val(),
								combo:$('#id_pro'),
							   	id:data.id_pro,
							   	funcion:function(){
									$('#id_pro').change();
								}
							}); 
					 }
				});
				
				$('#id_pais').on('change',function(event){
					 if($('#id_pais').val()=="193"){
						 $('#id_dep').removeAttr("disabled");
						 $('#id_pro').removeAttr("disabled");
						 $('#id_dist').removeAttr("disabled");
						 $('#dir').removeAttr("readonly");
						 $('#ref').removeAttr("readonly");
						 _llenar_combo({
								url:'api/comboCache/departamentos/' + $(this).val(),
								combo:$('#id_dep'),
							   	id: data.id_dep,
							   	funcion:function(){
									$('#id_dep').change();
								}
							});
					 } else {
						 $('#id_dep').val('');
						 $('#id_pro').val('');
						 $('#id_dist').val('');
						 $('#dir').val('');
						 $('#ref').val('');
						 $('#id_dep').attr("disabled", "disabled");
						 $('#id_pro').attr("disabled", "disabled");
						 $('#id_dist').attr("disabled", "disabled");
						 $('#dir').attr("readonly", "readonly");
						 $('#ref').attr("readonly", "readonly");
						 $('#id_dep').change();
					 }
				});
				
				_llenarCombo('cat_pais',$('#id_pais'),data.id_pais,null, function(){
					$('#id_pais').change();
				});		
				
				_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
				
				
				
				if(data.ocu==null || data.ocu==''){
					$("#id_tra_rem2").click();
				} else{
					$("#id_tra_rem1").click();
				} 
				
			}});
}

function editarAlumno(id_gpf, id_alu){
//function editarAlumno(codigo){	
	
	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + id_gpf + "&id_alu=" + id_alu,
	// _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
			function(data){
		$("#div_alumno").css('display','block');
				mostrarDatosTabAlumno(data);			
	});
}

function grabarFamiliar(obj){
	

	//var validator = $("#alu_familiar_alumno-frm").validate();
	  $('#frm_datos_familiar #id_tdc').removeAttr('disabled');
	  $('#frm_datos_familiar #id_par').removeAttr('disabled');
	 // $('#frm_datos_familiar input[name=id_tap]').attr("disabled",false);
	  
	event.preventDefault();
	if($('#frm_datos_familiar #nro_doc').val().length<8 && $('#frm_datos_familiar #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#frm_datos_familiar #nro_doc").focus();
	}  else if($('#frm_datos_familiar #nro_doc').val().length<11 && $('#frm_datos_familiar #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#frm_datos_familiar #nro_doc").focus();
	} /*else if($("#alu_familiar-frm #id_pais").selectedIndex==-1){
		alert("Seleccione el país!!");			
	}*/ else{
		
		//if ($("#alu_familiar_alumno-frm").valid()){
		/* $("#frm_datos_familiar").validate({

			    onfocusout:true,
			    rules:{
			    	id_eci:"required",
			    	id_pais:"required"
			        
			    }
		});
		 if( $("#frm_datos_familiar").validate()){
			 alert(333333333);
		 }*/
			/*if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
				_post($('#alu_familiar_alumno-frm').attr('action') , $('#alu_familiar_alumno-frm').serialize());
			}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
				/*if(!$("#alu_familiar-frm #id_pais").selectedIndex!=-1){
					alert("Seleccione el país!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dep']").is(':checked')){
					alert("Seleccione el departamento!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_pro']").is(':checked')){
					alert("Seleccione la provincia!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dist']").is(':checked')){
					alert("Seleccione el distrito!!");			
				} else{*/
				if(!$("#frm_datos_familiar input[name='id_gen']").is(':checked')){
					alert("Seleccione el género, porfavor!!");			
				} else{
					$('#alu_familiar_alumno-frm #btn-grabar').attr("disabled", "disabled");
					var frm_familiar=$(obj).parent().parent().parent();
					//var dni=frm_entrevista.find("#nro_dni").val();
					var json_familiar={};
					  json_familiar.id_per=frm_familiar.find("#frm_datos_familiar #id").val();
					  json_familiar.id_fam=frm_familiar.find("#frm_datos_familiar #id_fam").val();
					  json_familiar.id=frm_familiar.find("#frm_datos_familiar #id").val();
					  json_familiar.id_gpf=frm_familiar.find("#frm_datos_familiar #id_gpf").val();
					  json_familiar.est=frm_familiar.find("#est").val();
					  json_familiar.id_par=frm_familiar.find("#id_par").val();
					  json_familiar.id_tdc=frm_familiar.find("#id_tdc").val();
					  json_familiar.nro_doc=frm_familiar.find("#nro_doc").val();
					 // json_familiar.id_tap=$("input:radio[name=id_tap]:checked").val();
					  json_familiar.ape_pat=frm_familiar.find("#ape_pat").val();
					  json_familiar.ape_mat=frm_familiar.find("#ape_mat").val();
					  json_familiar.nom=frm_familiar.find("#nom").val();
					  json_familiar.id_gen=$("input:radio[name=id_gen]:checked").val();
					  json_familiar.id_eci=frm_familiar.find("#id_eci").val();
					  json_familiar.id_anio=$('#_id_anio').text();
					  json_familiar.fec_nac=frm_familiar.find("#fec_nac").val();
					  json_familiar.viv=frm_familiar.find("#viv").val();
					  if(frm_familiar.find("#viv").val()!=1)
					  json_familiar.fec_def=frm_familiar.find("#fec_def_fam").val();
					  json_familiar.viv_alu=frm_familiar.find("#viv_alu").val();
					  json_familiar.id_pais=frm_familiar.find("#id_pais").val();
					  json_familiar.id_dist=frm_familiar.find("#id_dist").val();
					  json_familiar.dir=frm_familiar.find("#dir").val();
					  json_familiar.ref=frm_familiar.find("#ref").val();
					  json_familiar.corr=frm_familiar.find("#corr").val();
					  json_familiar.tlf=frm_familiar.find("#tlf").val();
					  json_familiar.cel=frm_familiar.find("#cel").val();
					  json_familiar.id_gin=frm_familiar.find("#id_gin").val();
					  json_familiar.flag_alu_ex=frm_familiar.find("#flag_alu_ex").val();
					  json_familiar.prof=frm_familiar.find("#prof").val();
					  json_familiar.ocu=frm_familiar.find("#ocu").val();
					  json_familiar.dir_tra=frm_familiar.find("#dir_tra").val();
					  json_familiar.tlf_tra=frm_familiar.find("#tlf_tra").val();
					  json_familiar.cel_tra=frm_familiar.find("#cel_tra").val();
					  json_familiar.email_tra=frm_familiar.find("#email_tra").val();
					  console.log(json_familiar);
					  
					  _POST({
							context:_URL,
							url:'api/familiar/FamiliarGrabarReq',
							params:JSON.stringify(json_familiar),
							contentType:"application/json",
							success: function(data){
								console.log(data);
								$("#frm_datos_familiar").css('display','none');
								//  $("#frm_datos_familiar #id").val(data.id);
								//  $("#frm_datos_familiar #id_gpf").val(data.id_gpf);
								  $('#frm_datos_familiar #btn-grabar').removeAttr('disabled');
								  listarPadres();
								 
								  $('#frm_datos_familiar #id_tdc').attr("disabled", "disabled");
								  $('#frm_datos_familiar #id_par').attr("disabled", "disabled");
								  //$('#frm_datos_familiar input[name=id_tap]').attr("disabled",true);
								  _id_par=data.id_par;
								  _get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + data.id_gpf + "&id_alu=",
											function(data){
											$("#fam_alumnos-tab").load("pages/matricula/mat_actualizacion_dato_alu.html", {}, function(responseText, textStatus, req){
												mostrarDatosTabAlumno(data);
												$("#div_alumno").css('display','block');
												$('#alu_alumno-frm #id_gpf').val(data.id_gpf);
											});
											
									});
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 $('#alu_familiar_alumno-frm #btn-grabar').removeAttr('disabled');
								 $('#nro_dni').focus();
								//frm_entrevista.find("#id_ent_alu")
							}
						});	
						
				}	
				//}
				
				
			//}
		//}
	}
} //);

function grabarAlumno(obj){
	var validator = $("#alu_alumno-frm").validate();
	 $('#alu_alumno-frm #btn-grabar').attr("disabled", "disabled");
	event.preventDefault();
	if($('#alu_alumno-frm #nro_doc').val().length<8 && $('#alu_alumno-frm #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
	} else if($('#alu_alumno-frm #nro_doc').val().length<11 && $('#alu_alumno-frm #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
	} else{
		//$('#alu_alumno-frm input[name=id_tap]').attr("disabled",false);
		//  $('#alu_alumno-frm #cod').removeAttr('readonly');
		if ($("#alu_alumno-frm").valid()){
			// $('#btn-grabar').removeAttr('disabled');
			if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
				_post($('#alu_alumno-frm').attr('action') , $('#alu_alumno-frm').serialize());
			}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
				if(!$("#alu_alumno-frm input[name='id_gen']").is(':checked')){
					alert("Seleccione el género, porfavor!!");	
					$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
				} else{
					_POST({form:$('#alu_alumno-frm'),
						  context:_URL,
						  msg_type:'notification',
						  msg_exito : 'SE REGISTRO AL ALUMNO CORRECTAMENTE.',
						  success:function(data){
							    //DESCARGAR 
							  //id_gpf=2490, id_per=null, id=5010, error=null
							console.log(data);
							  $("#alu_alumno-frm #id").val(data.id);
							  $("#alu_alumno-frm #id_gpf").val(data.id_gpf);
							  $('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
							  listar_alumnos(data.id_gpf); 
						 }, 
						 error:function(data){
							 _alert_error(data.msg);
							 $('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
							 $('#nro_dni').focus();
						}
					}); 
				}				
				
			}
		} else{
			$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
		}
	}
	
	/*} else{
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
	}*/
};

function listarPadres(){
	var param={id_gpf:_id_gpf};
	_GET({ url: 'api/familiar/listarPadres/',
		context: _URL,
	   params:param,
	   success:
		function(data){
		 //  _cant_padres=data.length;
		console.log(data);
		var cant_padres=data.length;
		 _cant_pad=data.length;
		if(cant_padres>=2){
			$("#fam_datos-tab #btn-nuevo").css('display','none');
		} else{
			$("#fam_datos-tab #btn-nuevo").css('display','block');
		}
			$('#familiares_table').dataTable({
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
						{"title":"Nro. Documento", "data" : "nro_doc"}, 
						{"title":"Apellidos y Nombres", "data" : "familiar"}, 
						{"title":"Parentesco", "data" : "parentesco"}, 
						{"title":"Acciones", "render": function ( data, type, row ) {
							if(row.id_anio_act==$('#_id_anio').text()){
								if(row.id_par=='1'){
									_id_par=1;
								} else if(row.id_par=='2'){
									_id_par=2;
								}
								return '<button onclick="editarFamiliar(' + row.id_gpf +',' + row.id+')" type="button" tipo="E" class="btnPadre btn btn-success btn-xs"><i class="icon-pencil3"></i> Datos Actualizados</button>';								
							} else{
								return '<button onclick="editarFamiliar(' + row.id_gpf +',' + row.id+')" type="button" tipo="N" class="btnPadre btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Datos</button>';
							}
							
							
						}} 
		    ]
		    });
			
			var cantidadPadres = $('.btnPadre').length;	
			var _cant_pad_act=0;
			$( ".btnPadre" ).each(function( index ) {
				 // alert($( this ).attr('tipo') );
				  if($( this ).attr('tipo')=='E'){
					  _cant_pad_act=_cant_pad_act+1;
				  }
			});
			
			if(cantidadPadres==_cant_pad_act){
				$("#li-tabs-3").click();
			}
			/*alert(cant_padres);
			alert(cant_actua);
			if(cant_padres==cant_actua)
				alert();*/
		}
	});
	
}

/*function listar_otros_familiares(id_gpf){
	var param={id_gpf:id_gpf};
	_GET({ url: 'api/familiar/listarOtrosFamiliares/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			//   _cant_padres=data.length;
			console.log(data);
				$('#otro_familiar-tabla').dataTable({
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
							{"title":"Nro. Documento", "data" : "nro_doc"}, 
							{"title":"Apellidos y Nombres", "data" : "familiar"}, 
							{"title":"Parentesco", "data" : "parentesco"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="editarOtroFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
								+ '<button onclick="eliminarOtroFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-danger btn-xs"><i class="icon-trash-alt position-left"></i> Eliminar</button>';
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});
}*/

function mostrarDatosTabAlumno(data) {
	var alumno = data.alumno; 
	var persona = data.persona; 
	console.log(data);
	
	// llenamos los datos globales
	//var _id_dep=alumno.departamento_nac.id;
	//var _id_pro=alumno.provincia_nac.id;
	//var _id_dist=alumno.id_dist_nac;
	//var _id_pais_nac=alumno.id_pais_nac;
	
	/**
	 * DATOS DEL ALUMNO (FORMULARIO)
	 */
	$("#alu_alumno-frm #ape_pat").attr("readonly", "readonly");
	$("#alu_alumno-frm #ape_mat").attr("readonly", "readonly");
	$("#alu_alumno-frm #id_per").val(persona.id);
	if(alumno.id_anio_act!=0){
		
		$("#alu_alumno-frm #nom").attr("readonly", "readonly");
		$("#alu_alumno-frm #nro_doc").attr("readonly", "readonly");
		//$('#alu_alumno-frm input[name=id_tap]').attr("disabled",true);
	
		$('#id_pro_nac').on('change',function(event){

		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist_nac').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_nac'),
				   	id:persona.id_dist_nac,
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#id_dep_nac').removeData('id_pro_nac');//eliminar campos auxiliares
				   		$('#id_pro_nac').removeData('id_dist_nac');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
		});
		
		$('#id_dep_nac').on('change',function(event){
			
			 if($('#id_dep_nac').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_nac'),
					   	id:persona.provincia_nac.id,
					   	funcion:function(){
							$('#id_pro_nac').change();
						}
					}); 
			 }
		});
		
		$('#id_pais_nac').on('change',function(event){
			 if($('#id_pais_nac').val()=="193"){
				 $('#id_dep_nac').removeAttr("disabled");
				 $('#id_pro_nac').removeAttr("disabled");
				 $('#id_dist_nac').removeAttr("disabled");
				// $('#dir').removeAttr("readonly");
				// $('#referencia').removeAttr("readonly");
				 _llenar_combo({
						url:'api/comboCache/departamentos/' + $(this).val(),
						combo:$('#id_dep_nac'),
					   	id: persona.departamento.id,
					   	funcion:function(){
							$('#id_dep_nac').change();
						}
					});

				_llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'), 69);
			 } else {
				 $('#id_dep_nac').val('');
				 $('#id_pro_nac').val('');
				 $('#id_dist_nac').val('');
				// $('#dir').val('');
				// $('#referencia').val('');
				 $('#id_dep_nac').attr("disabled", "disabled");
				 $('#id_pro_nac').attr("disabled", "disabled");
				 $('#id_dist_nac').attr("disabled", "disabled");
				// $('#dir').attr("readonly", "readonly");
				// $('#referencia').attr("readonly", "readonly");
				 $('#id_dep_nac').change();
				 _llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'));
			 }
		});
		_llenarCombo('cat_pais',$('#id_pais_nac'),persona.id_pais_nac,null, function(){
			$('#id_pais_nac').change();
		});	
		
		$('#id_pro_viv').on('change',function(event){

			 	if ($(this).val()==null || $(this).val()==''){ 
			 		$('#id_dist_viv').find('option').not(':first').remove();
			 	}
			 	else{
					_llenar_combo({
						url:'api/comboCache/distritos/' + $(this).val(),
						combo:$('#id_dist_viv'),
					   	id:persona.id_dist_viv,
					   	funcion:function(){
					   		console.log('remove data');
					   		$('#id_dep_viv').removeData('id_pro_viv');//eliminar campos auxiliares
					   		$('#id_pro_viv').removeData('id_dist_viv');//eliminar campos auxiliares
					   	}
					});		 		
			 	}
			 
		});
		
		$('#id_dep_viv').on('change',function(event){
			 if($('#id_dep_viv').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_viv'),
					   	id:persona.provincia_viv.id,
					   	funcion:function(){
							$('#id_pro_viv').change();
						}
					}); 
			 }
		});
		
		 _llenar_combo({
				url:'api/comboCache/departamentos/' + 193,
				combo:$('#id_dep_viv'),
			   //	id: persona.departamento_viv.id,
			   	funcion:function(){
					$('#id_dep_viv').change();
				}
		});	
		
	} else {
		$('#id_pro_nac').on('change',function(event){

		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist_nac').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_nac'),
				   //	id:data.id_dist,
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#id_dep_nac').removeData('id_pro_nac');//eliminar campos auxiliares
				   		$('#id_pro_nac').removeData('id_dist_nac');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
		});
		
		$('#id_dep_nac').on('change',function(event){
			
			 if($('#id_dep_nac').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_nac'),
					   //	id:alumno.provincia.id,
					   	funcion:function(){
							$('#id_pro_nac').change();
						}
					}); 
			 }
		});
		
		$('#id_pais_nac').on('change',function(event){
			 if($('#id_pais_nac').val()=="193"){
				 $('#id_dep_nac').removeAttr("disabled");
				 $('#id_pro_nac').removeAttr("disabled");
				 $('#id_dist_nac').removeAttr("disabled");
				// $('#dir').removeAttr("readonly");
				// $('#referencia').removeAttr("readonly");
				 _llenar_combo({
						url:'api/comboCache/departamentos/' + $(this).val(),
						combo:$('#id_dep_nac'),
					   	//id: alumno.departamento.id,
					   	funcion:function(){
							$('#id_dep_nac').change();
						}
					});

				_llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'), 69);
			 } else {
				 $('#id_dep_nac').val('');
				 $('#id_pro_nac').val('');
				 $('#id_dist_nac').val('');
				// $('#dir').val('');
				// $('#referencia').val('');
				 $('#id_dep_nac').attr("disabled", "disabled");
				 $('#id_pro_nac').attr("disabled", "disabled");
				 $('#id_dist_nac').attr("disabled", "disabled");
				// $('#dir').attr("readonly", "readonly");
				// $('#referencia').attr("readonly", "readonly");
				 $('#id_dep_nac').change();
				 _llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'));
			 }
		});
			
		_llenarCombo('cat_pais',$('#id_pais_nac'),persona.id_pais_nac,null, function(){
			$('#id_pais_nac').change();
		});	
		
		$('#id_pro_viv').on('change',function(event){

		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist_viv').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_viv'),
				  // 	id:data.id_dist,
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#id_dep_viv').removeData('id_pro_viv');//eliminar campos auxiliares
				   		$('#id_pro_viv').removeData('id_dist_viv');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
	});
	
		$('#id_dep_viv').on('change',function(event){
			
			 if($('#id_dep_viv').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_viv'),
					//   	id:1,
					   	funcion:function(){
							$('#id_pro_viv').change();
						}
					}); 
			 }
		});
		
		 _llenar_combo({
				url:'api/comboCache/departamentos/' + 193,
				combo:$('#id_dep_viv'),
			  // 	id: 3,
			   	funcion:function(){
					$('#id_dep_viv').change();
				}
			});
		
		
	}
	
	$("#alu_alumno-frm #nro_doc").on('blur',function(){
		if(_cant_padres>=1){
			if(data.padre==null && data.madre!=null){
				var text =  "El familiar solo tiene registrado a la mamá, el alumno tendra los apellidos de la madre!!";
				swal(
						{
							title : "Desea Continuar??",
							text :text,
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "No, registrar al padre!!!",
							cancelButtonText : "Si, continuar",
							closeOnConfirm : true,
							closeOnCancel : true
						},
						function(isConfirm) {
							if (isConfirm) {
								var params ={id_gpf:data.id_gpf, id_fam:data.madre.id};
								_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);
							} else{
								$("#alu_alumno-frm #nom").focus();
							}
					});
			} else if(data.madre==null && data.padre!=null){
				var text =  "El familiar solo tiene registrado al papá, el alumno tendra los apellidos del padre!!";
				swal(
						{
							title : "Desea Continuar??",
							text :text,
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "No, registrar a la madre!!!",
							cancelButtonText : "Si, continuar",
							closeOnConfirm : true,
							closeOnCancel : true
						},
						function(isConfirm) {
							if (isConfirm) {
								var params ={id_gpf:data.id_gpf, id_fam:data.madre.id};
								_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);
							} else {
								$("#alu_alumno-frm #nom").focus();
							}
					});
			}
		}
		
	});
	
	$("#alu_alumno-frm #id_tdc").on('change', function(){
		if($(this).val()==1){
			 $("#alu_alumno-frm #nro_doc").attr('maxlength','8');
		}else if($(this).val()==2){
			 $("#alu_alumno-frm #nro_doc").attr('maxlength','11');
		}
	});
	
	
	_fillForm(persona,$('#alu_alumno-frm'));
	$("#alu_alumno-frm #id_anio_act").val($('#_id_anio').text());
	_llenarCombo('cat_tipo_documento',$('#alu_alumno-frm #id_tdc'), persona.id_tdc,null, function(){
		$('#alu_alumno-frm #id_tdc').change();
	});
			
	
	listar_alumnos(data.id_gpf);
	
	//$("#alu_alumno-frm [name=id_tap]").val([alumno.id_tap]);
	//$("#alu_alumno-frm [name=id_gen]").val([alumno.id_gen]);
	_llenarCombo('cat_est_civil',$('#alu_alumno-frm #id_eci'), persona.id_eci);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio1'), persona.id_idio1);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio2'), persona.id_idio2);

	var fec_nac = persona.fec_nac;
	if(fec_nac){
		var arrFec_nac = fec_nac.split('-');
		$('#alu_alumno-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
	}

	_inputs('alu_alumno-frm');
	
	/**
	 * ALUMNOS REGISTRADOS
	 */
 
	console.log(data.alumnoList);
  
}

/*function obtenerDireccionPadre() {
	var id_gpf=_id_gpf;
	var param={id_gpf:id_gpf, id_par:2};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   $("#alu_alumno-frm #id_dist_viv").val(data.id_dist).change();;
			   $("#alu_alumno-frm #id_pro_viv").val(data.id_pro).change();;
			   $("#alu_alumno-frm #id_dep_viv").val(data.id_dep).change();;
			   $("#alu_alumno-frm #direccion").val(data.dir);
			   $("#alu_alumno-frm #ref").val(data.ref);
			   $("#alu_alumno-frm #telf").val(data.tlf);
			}
		});
}*/
function obtenerDireccionPadre(){
	var id_gpf=_id_gpf;
	_primeraCarga=true;
	var param={id_gpf:id_gpf, id_par:2};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   var id_dep = null;
				if(_primeraCarga)
					id_dep = data.id_dep;//solo en la primera carga se jala desde la data, deacuerdo? si
				_llenar_combo({
							url:'api/comboCache/departamentos/' +193,
							combo:$('#id_dep_viv'),
						   	id: id_dep,
						   	funcion:function(){
								$('#id_dep_viv').change();
							}
				});
				 
				$('#id_dep_viv').on('change',function(event){
					var id_pro = null;
					if(_primeraCarga)
						id_pro = data.id_pro//solo en la primera carga se jala desde la data, deacuerdo? si
					console.log('onchange departamento (id_pro):' + id_pro);
					
					 if($('#id_dep_viv').val()!=null && $('#id_dep_viv').val()!=''){
						// alert('entroe al change');
						 _llenar_combo({
								url:'api/comboCache/provincias/' + $('#id_dep_viv').val(),
								combo:$('#id_pro_viv'),
							   	id:id_pro,//aca es el tema.. la primera vez deberia usar el data.id_pro y cuando el usuario edita.. ya no deberia usar data.id_pro
							   					//cierto? claro, podemos manejar un flag
							   	funcion:function(){
									$('#id_pro_viv').change();
								}
							}); 
					 }
				});
				
				//los combos dep, pro, dis
				$('#id_pro_viv').on('change',function(event){
					 console.log('onchange provincia');
					 var id_dist = null 		 
					 if(_primeraCarga)
						 id_dist = data.id_dist;
					 console.log('onchange provincia:' + id_dist);
					if($('#id_pro_viv').val()!=null && $('#id_pro_viv').val()!=''){
					 _llenar_combo({
							url:'api/comboCache/distritos/' + $(this).val(),
							combo:$('#id_dist_viv'),
						   	id:id_dist,// aca le paso el valor del id para q aparezca selecconado, siempre lo haciamos asi----estas??								   	
						   	funcion:function(){
						   		$('#id_dist_viv').change();
						   		//aca finalizamos la primera carga
						   		_primeraCarga = false;
						   	}
						});	
					}
				});
			   $("#alu_alumno-frm #direccion").val(data.dir);
			   $("#alu_alumno-frm #ref").val(data.ref);
			   $("#alu_alumno-frm #telf").val(data.tlf);
			}
		});
	
	
}


/*function obtenerDireccionMadre() {
	var id_gpf=_id_gpf;
	var param={id_gpf:id_gpf, id_par:1};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   $("#alu_alumno-frm #id_dist_viv").val(data.id_dist).change();;
			   $("#alu_alumno-frm #id_pro_viv").val(data.id_pro).change();;
			   $("#alu_alumno-frm #id_dep_viv").val(data.id_dep).change();;
			   $("#alu_alumno-frm #direccion").val(data.dir);
			   $("#alu_alumno-frm #ref").val(data.ref);
			   $("#alu_alumno-frm #telf").val(data.tlf);
			}
		});
}*/

function obtenerDireccionMadre(){
	var id_gpf=_id_gpf;
	_primeraCarga=true;
	var param={id_gpf:id_gpf, id_par:1};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   var id_dep = null;
				if(_primeraCarga)
					id_dep = data.id_dep;//solo en la primera carga se jala desde la data, deacuerdo? si
				_llenar_combo({
							url:'api/comboCache/departamentos/' +193,
							combo:$('#id_dep_viv'),
						   	id: id_dep,
						   	funcion:function(){
								$('#id_dep_viv').change();
							}
				});
				 
				$('#id_dep_viv').on('change',function(event){
					var id_pro = null;
					if(_primeraCarga)
						id_pro = data.id_pro//solo en la primera carga se jala desde la data, deacuerdo? si
					console.log('onchange departamento (id_pro):' + id_pro);
					
					 if($('#id_dep_viv').val()!=null && $('#id_dep_viv').val()!=''){
						// alert('entroe al change');
						 _llenar_combo({
								url:'api/comboCache/provincias/' + $('#id_dep_viv').val(),
								combo:$('#id_pro_viv'),
							   	id:id_pro,//aca es el tema.. la primera vez deberia usar el data.id_pro y cuando el usuario edita.. ya no deberia usar data.id_pro
							   					//cierto? claro, podemos manejar un flag
							   	funcion:function(){
									$('#id_pro_viv').change();
								}
							}); 
					 }
				});
				
				//los combos dep, pro, dis
				$('#id_pro_viv').on('change',function(event){
					 console.log('onchange provincia');
					 var id_dist = null 		 
					 if(_primeraCarga)
						 id_dist = data.id_dist;
					 console.log('onchange provincia:' + id_dist);
					if($('#id_pro_viv').val()!=null && $('#id_pro_viv').val()!=''){
					 _llenar_combo({
							url:'api/comboCache/distritos/' + $(this).val(),
							combo:$('#id_dist_viv'),
						   	id:id_dist,// aca le paso el valor del id para q aparezca selecconado, siempre lo haciamos asi----estas??								   	
						   	funcion:function(){
						   		$('#id_dist_viv').change();
						   		//aca finalizamos la primera carga
						   		_primeraCarga = false;
						   	}
						});	
					}
				});
			   $("#alu_alumno-frm #direccion").val(data.dir);
			   $("#alu_alumno-frm #ref").val(data.ref);
			   $("#alu_alumno-frm #telf").val(data.tlf);
			}
		});
	
	
}

function obtenerDireccionPrimerPadre(){
	var id_gpf=_id_gpf;
	_primeraCarga=true;
	var param={id_gpf:id_gpf, id_par:_id_par};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   var id_dep = null;
				if(_primeraCarga)
					id_dep = data.id_dep;//solo en la primera carga se jala desde la data, deacuerdo? si
				_llenarCombo('cat_pais',$('#id_pais'),data.id_pais,null, function(){
					$('#id_pais').change();
				});	
				
				$('#frm_datos_familiar #id_pais').on('change',function(event){
					/*_llenar_combo({
						url:'api/comboCache/departamentos/' +193,
						combo:$('#frm_datos_familiar #id_dep'),
					   	id: id_dep,
					   	funcion:function(){
							$('#frm_datos_familiar #id_dep').change();
						}
					});*/
					if($('#frm_datos_familiar #id_pais').val()=="193"){
						 $('#frm_datos_familiar #id_dep').removeAttr("disabled");
						 $('#frm_datos_familiar #id_pro').removeAttr("disabled");
						 $('#frm_datos_familiar #id_dist').removeAttr("disabled");
						 $('#frm_datos_familiar #dir').removeAttr("readonly");
						 $('#frm_datos_familiar #ref').removeAttr("readonly");
						 _llenar_combo({
								url:'api/comboCache/departamentos/' + $(this).val(),
								combo:$('#frm_datos_familiar #id_dep'),
							   	id: data.id_dep,
							   	funcion:function(){
									$('#frm_datos_familiar #id_dep').change();
								}
							});
					 } else {
						 $('#frm_datos_familiar #id_dep').val('');
						 $('#frm_datos_familiar #id_pro').val('');
						 $('#frm_datos_familiar #id_dist').val('');
						 $('#frm_datos_familiar #dir').val('');
						 $('#frm_datos_familiar #ref').val('');
						 $('#frm_datos_familiar #id_dep').attr("disabled", "disabled");
						 $('#frm_datos_familiar #id_pro').attr("disabled", "disabled");
						 $('#frm_datos_familiar #id_dist').attr("disabled", "disabled");
						 $('#frm_datos_familiar #dir').attr("readonly", "readonly");
						 $('#frm_datos_familiar #ref').attr("readonly", "readonly");
						 $('#frm_datos_familiar #id_dep').change();
					 }
				});
				 
				$('#frm_datos_familiar #id_dep').on('change',function(event){
					var id_pro = null;
					if(_primeraCarga)
						id_pro = data.id_pro//solo en la primera carga se jala desde la data, deacuerdo? si
					console.log('onchange departamento (id_pro):' + id_pro);
					
					 if($('#frm_datos_familiar #id_dep').val()!=null && $('#frm_datos_familiar #id_dep').val()!=''){
						// alert('entroe al change');
						 _llenar_combo({
								url:'api/comboCache/provincias/' + $('#frm_datos_familiar #id_dep').val(),
								combo:$('#frm_datos_familiar #id_pro'),
							   	id:id_pro,//aca es el tema.. la primera vez deberia usar el data.id_pro y cuando el usuario edita.. ya no deberia usar data.id_pro
							   					//cierto? claro, podemos manejar un flag
							   	funcion:function(){
									$('#frm_datos_familiar #id_pro').change();
								}
							}); 
					 }
				});
				
				//los combos dep, pro, dis
				$('#frm_datos_familiar #id_pro').on('change',function(event){
					 console.log('onchange provincia');
					 var id_dist = null 		 
					 if(_primeraCarga)
						 id_dist = data.id_dist;
					 console.log('onchange provincia:' + id_dist);
					if($('#frm_datos_familiar #id_pro').val()!=null && $('#frm_datos_familiar #id_pro').val()!=''){
					 _llenar_combo({
							url:'api/comboCache/distritos/' + $(this).val(),
							combo:$('#frm_datos_familiar #id_dist'),
						   	id:id_dist,// aca le paso el valor del id para q aparezca selecconado, siempre lo haciamos asi----estas??								   	
						   	funcion:function(){
						   		$('#frm_datos_familiar #id_dist').change();
						   		//aca finalizamos la primera carga
						   		_primeraCarga = false;
						   	}
						});	
					}
				});
			   $("#frm_datos_familiar #dir").val(data.dir);
			   $("#frm_datos_familiar #ref").val(data.ref);
			}
		});
	
	
}

function listar_alumnos(id_gpf){
	
	var param={id_gpf:id_gpf, id_suc:_usuario.id_suc, id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/alumno/listarHermanosxLocal/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			 //  _cant_padres=data.length;
			   _cant_alu=data.length;
			console.log(data);
				$('#alumno-tabla').dataTable({
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
							{"title":"Nro. Documento", "data" : "nro_doc"}, 
							//{"title":"Apellidos y Nombres", "data" : "alumno"}, 
							{"title":"Apellidos y Nombres", "render": function ( data, type, row ) {
				            	   return row.ape_pat+" "+row.ape_mat+" "+row.nom;
				            }} ,
							{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.id_anio_act==$('#_id_anio').text()){
									return '<button onclick="editarAlumno(' + row.id_gpf +',' + row.id_alu+')" type="button" tipo="E" class="btnAlumno btn btn-success btn-xs"><i class="icon-pencil3"></i>Datos Actualizados</button>';
								} else{
									return '<button onclick="editarAlumno(' + row.id_gpf +',' + row.id_alu+')" type="button" tipo="N" class="btnAlumno btn btn-warning btn-xs"><i class="icon-pencil3"></i> Editar</button>';
								}
								
							}} 
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
				
				var cantidadAlumnos = $('.btnAlumno').length;	
				var _cant_alu_act=0;
				$( ".btnAlumno" ).each(function( index ) {
					 // alert($( this ).attr('tipo') );
					  if($( this ).attr('tipo')=='E'){
						  _cant_alu_act=_cant_alu_act+1;
					  }
				});
				if(cantidadAlumnos==_cant_alu_act){
					form.steps("next");
				}
			}
		});
	
}

function listar_hermanos(id_gpf){
	var param={id_gpf:id_gpf, id_suc:_usuario.id_suc, id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/alumno/listarHermanosxLocal/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			 //  _cant_padres=data.length;
			console.log(data);	
			   _cant_mat=data.length;
				$('#hermanos-tabla').dataTable({
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
							{"title":"Nro. Documento", "data" : "nro_doc"}, 
							//{"title":"Apellidos y Nombres", "data" : "alumno"}, 
							{"title":"Apellidos y Nombres", "render": function ( data, type, row ) {
				            	   return row.ape_pat+" "+row.ape_mat+" "+row.nom;
				            }} ,
							{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.id_mat!=null){
									_cant_alu_mat = _cant_alu_mat+1;
									return '<button onclick="mostrarDatosMatricula(' + row.id_alu +',' + row.id_mat+','+row.id_gpf+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i> Editar Matricula</button>';
								}
								else{
									return '<button onclick="mostrarDatosMatricula(' + row.id_alu +',' + row.id_mat+','+row.id_gpf+')" type="button" class="btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Datos Matricula</button>';
								}
								
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
			}
		});
	console.log(_cant_alu_mat);
}

function listar_hermanos_pagos(id_gpf){
	var param={id_gpf:id_gpf, id_suc:_usuario.id_suc, id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/alumno/listarHermanosxLocal/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			 //  _cant_padres=data.length;
			console.log(data);
				$('#hermanos_pagos-tabla').dataTable({
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
							{"title":"Nro. Documento", "data" : "nro_doc"}, 
							//{"title":"Apellidos y Nombres", "data" : "alumno"}, 
							{"title":"Apellidos y Nombres", "render": function ( data, type, row ) {
				            	   return row.ape_pat+" "+row.ape_mat+" "+row.nom;
				            }} ,
							{"title":"Validar Contrato", "render": function ( data, type, row ) {
									if(row.con_val==null && row.id_mat!=null){
										return "<input type='checkbox' id='id_mat_sel" + row.id_mat+"' name='id_mat_sel' value="+row.id_mat+" onclick='validarContrato(this)'/>"; 
									} else{
										return null;
									}	
				            }} ,
							{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.con_val==1){
									if(row.canc==1){
									_cant_pag=_cant_pag+1;
										return '<button onclick="mostrarDatosPago(' + row.id_mat +',' + row.id_alu+','+row.id_au_asi+','+row.id_fac+','+row.id_gpf+','+row.canc+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i> Ver Pago</button>';
									} else{
										console.log(row.id_mat);
										var disabled = (row.id_mat==null) ? ' disabled ': ' ';

										return '<button onclick="mostrarDatosPago(' + row.id_mat +',' + row.id_alu+','+row.id_au_asi+','+row.id_fac+','+row.id_gpf+','+row.canc+')" type="button"'+disabled+'class="btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Pagos</button>';
									}
								} else{
									return null;
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

function validarContrato(campo1){
	console.log(campo1);
	var id_mat =$(campo1).val();
	swal({
			title : "Esta seguro?",
			text : "Se procedera a validar el contrato del alumno :" + $("#label_alumno").text(),
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "rgb(33, 150, 243)",
			cancelButtonColor : "#EF5350",
			confirmButtonText : "Si, Validar",
			cancelButtonText : "No, cancela!!!",
			closeOnConfirm : true,
			closeOnCancel : false
		},
		function(isConfirm) {
				if (isConfirm) {
					$(".modal .close").click();
					_POST({
							context:_URL,
							url:'api/matricula/validarContrato/'+id_mat,
							//params:id_mat,
							msg_type:'notification',
							msg_exito : 'Se validó el contrato correctamente.',
							success: function(data){
								listar_hermanos_pagos(_id_gpf);
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 
							}
						});	
					

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

}	

function mostrarDatosPago(id_mat,id_alu, id_au, id_fac, id_gpf, canc){
	var param = {
			id_mat : id_mat,
			id_alu : id_alu,
			id_au  : id_au,
			id_anio : $("#_id_anio").text()
	};
	$("#div_pago #id_mat").val(id_mat);
	$("#div_pago #id_pago").val(id_fac);
	$("#div_pago #id_gpf").val(id_gpf);	
	$("#abono").val('');
	$("#vuelto").val('');
	_get("api/matricula/MatriculaPagos2",
			function(data) {
				var id_au_actual =null;
				//var id_au_nueva = $('#id_au').val();
				//alert(id_au_nueva);
				var totalPagar  = data.total;
				$('#total').val(totalPagar);
				$('#abona').val('');
				$('#vuelto').val('');
				var param2 = {
						id_anio : $('#_id_anio').text(),
						id_alu : id_alu,
						id_mat : id_mat,
						id_suc : $("#_id_suc").text()
					}
					_get(
							'api/matricula/MatriculaEditar2',
							function(data) {
								$("#label_alumno").text(data.persona.ape_pat+' '+ data.persona.ape_mat+' '+data.persona.nom);
								var _data=data;
								/*if (_data.matricula!=null && _data.matricula.id_au_asi!=null){
									id_au_actual = _data.matricula.id_au_asi;
								}*/
								
								var id_au_actual = _data.matricula.id_au;
								var id_au_nueva =  _data.matricula.id_au_asi;
								/*alert(_data.sucursal_anterior);
								alert($("#_id_suc").text());
								alert('nueva'+id_au_nueva);
								alert('actual'+id_au_actual);
								alert('mat_val'+_data.matricula.mat_val);*/
								$('#pagardiv').show();
								//if((id_au_nueva == id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text() && (_data.matricula.mat_val==0 || data.matricula.mat_val==null))){//Cuando se va a matricular por primera vez
								if((id_au_nueva == id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text() && (_data.pago==0 || data.pago==null))){//Cuando se va a matricular por primera vez
									//alert('aquiii0');
									$('#lblTotalPagar').html('Total a Pagar');
									$('#divTotalPagar').show();
									$('#pagardiv').show();
									$("#btn-grabar_pago").removeAttr("disabled");
								//} /*else if( (id_au_nueva == id_au_actual && id_fac!=null && canc==1)  /*MISMA SECCION*/
									/*|| (_data.matricula!=null && _data.matricula.periodo.id_suc ==$('#_id_suc').val()) ){/*MISMO LOCAL*/
									//MOSTRAR SOLO LOS PAGOS EFECTUADOS
								/*	$('#divTituloPagos').html('Pagos realizados');
									$('#divTotalPagar').hide();
									$("#btn-grabar_pago").attr('disabled','disabled');*/
								//} else if((id_au_nueva == id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text() && _data.matricula.mat_val==1)){//Cuando ya esta matriculada y validad su matricula
								} else if((id_au_nueva == id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text() && _data.pago==1)){//Cuando ya esta matriculada y validad su matricula
									//alert('aaaa');
									$('#pagardiv').hide();
								//} else if((id_au_nueva != id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text()) && _data.matricula.mat_val==1){//cuando tiene cambio de sección pero el mismo local
								} else if((id_au_nueva != id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text()) && _data.pago==1){//cuando tiene cambio de sección pero el mismo local
									//alert('eeeee');
									//$('#pagardiv').hide();
								//}else if((id_au_nueva != id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text()) && _data.matricula.mat_val==0){//cuando tiene cambio de sección pero el mismo local
								}else if((id_au_nueva != id_au_actual) && (_data.sucursal_anterior ==$("#_id_suc").text()) && _data.pago==0){//cuando tiene cambio de sección pero el mismo local
									//alert('eeeee');
									$('#lblTotalPagar').html('Total a Pagar');
									$('#divTotalPagar').show();
									$('#pagardiv').show();
									$("#btn-grabar_pago").removeAttr("disabled");
								//}else if((id_au_nueva != id_au_actual) && (_data.matricula.mat_val==0 && _data.sucursal_anterior!=$("#_id_suc").text())){ // Se hace el cambio de local y aun no ha pagado o se ha reembolsado
								}else if((id_au_nueva != id_au_actual) && (_data.pago==0 && _data.sucursal_anterior!=$("#_id_suc").text())){ // Se hace el cambio de local y aun no ha pagado o se ha reembolsado
									//alert('debe entrar');
									$("#btn-grabar_pago").removeAttr("disabled");
									if (parseFloat(totalPagar)==0){
										//alert('aquiii');
										$('#divTotalPagar').hide();
										$('#pagardiv').hide();
									}else{
										$('#divTotalPagar').show();
										$('#pagardiv').show();
										if (parseFloat(totalPagar)<0){
											//alert('aquiii2');
											$('#lblTotalPagar').html('Total a Reembolsar al cliente por diferencia del costo de matricula');
											//$('#pagardiv').hide();
											$('#total').val($('#total').val().replace("-",""));
											$('#total').css("color","red");
											$("#abono").attr('readonly','readonly');
											$("#btn-grabar_pago").text('Reembolsar');
											_cl=true;
										}
										else{
											//alert('aquiii3');
											$('#lblTotalPagar').html('Pago Pendiente');
											_cl=true;
										}
											

									}
								} else if((id_au_nueva != id_au_actual) && (_data.pago==1 && _data.sucursal_anterior!=$("#_id_suc").text())){
								//} else if((id_au_nueva != id_au_actual) && (_data.matricula.mat_val==1 && _data.sucursal_anterior!=$("#_id_suc").text())){
									$('#pagardiv').hide();
								}

								
							}, param2);
				$('#alu_pagos-tabla').dataTable(
								{	data : data.pagos,
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
											{"title" : "Concepto","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return '<B>MATRICULA</B>';
												else if (row.tip == 'RES')
													return '<B>RESERVA DE MATRICULA</B>';
												else if (row.tip == 'ING')
													return '<B>CUOTA DE INGRESO</B>';
												else if (row.tip == 'LOC')
													return '<B>DIFERENCIA POR CAMBIO DE LOCAL</B>';
												else if (row.tip == 'DEV_LOC')
													return '<B>DEVOLUCIÓN POR CAMBIO DE LOCAL</B>';
												else 
													return row.tip;}},
											{"title" : "Monto","render" : function(data,type,row) {return 'S/.' + $.number(row.monto,2)}},
											{"title" : "Nro Recibo","render" : function(data,type,row) {
												//if (row.nro_rec==null && _data.matricula.mat_val==0)
												if (row.nro_rec==null && _data.pago==0)
													return 'PENDIENTE'; 
												//else if (row.nro_rec==null && _data.matricula.mat_val==1)
												else if (row.nro_rec==null && _data.pago==1)
													return ''; 
												else 
													return row.nro_rec;	
											}},
									]
					});

		}, param);

}

function onclickCarPod(field) {
	var valor = null;

	if (field instanceof jQuery)
		valor = field.val();
	else
		valor = field.value;

	// console.log("valor", valor);

	if (valor == "1") {// SI
		$("#div_id_enc").css("display", "inline-block");
		$("#div_iid_enc_par").css("display", "inline-block");
		$("#div_enc_dni").css("display", "inline-block");
		// $("#id_enc_par").attr("required","required");
		$("#id_enc").attr("required", "required");
		$("#id_enc_dni").attr("required", "required");
		$("#id_enc_par").removeAttr("disabled");
		$("#id_enc_dni").removeAttr("disabled");

	} else {
		// $("#fieldCartaPoder").css("display","none");
		$("#div_id_enc").css("display", "none");
		$("#div_iid_enc_par").css("display", "none");
		$("#div_enc_dni").css("display", "none");
		$("#id_enc_par").removeAttr("required");
		$("#id_enc").removeAttr("required");
		$("#id_enc_dni").removeAttr("required");

		$("#id_enc_par").attr("disabled", "disabled");
		$("#id_enc_dni").attr("disabled", "disabled");

	}

	$('#id_enc').change();
}


function grabarMatricula(){
	//verificar que este seleccionado un apoderado
	if($("#id_fam").val()==null)
		alert('Seleccione al apoderado del alumno!!');
	else{
		if($("#id_bco_pag").val()==""){
			alert('Seleccione el banco en el que desea pagar!!');
		} else {
			var id_au_actual =null;
		var id_au_nueva = $('#id_au').val();

		if (_data.matricula!=null && _data.matricula.id_au_asi!=null){
			id_au_actual = _data.matricula.id_au_asi;
		}
		
		if(id_au_actual == null){
			//alert ("ventana para matricular");	
			swal({
				title : "Esta seguro?",
				text : "Se procedera a matricular al alumno:" + $("#label_alumno").text(),
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, Matricular",
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					var validation = $('#frm-matricula').validate(); 
					if ($('#frm-matricula').valid()){
					
					$('#frm-matricula #id_suc').val(_usuario.id_suc);
					$('#frm-matricula #id_au').removeAttr('disabled');
					$('#frm-matricula #id_fam_mat').removeAttr('disabled');
					$('#frm-matricula #id_gra').removeAttr('disabled');
					$('#frm-matricula #id_cli').removeAttr('disabled');
					$('#frm-matricula #id_niv').removeAttr('disabled');
					$('#frm-matricula #fecha').removeAttr('disabled');
					$('#frm-matricula #num_cont').removeAttr('disabled');

					_post(
							"api/matricula/matricularPagarV2",$('#frm-matricula').serialize(),
							function(data) {
								console.log(data);
								_btnMatriculado = true;
								//comentado2021id_au_actual = $('#frm-matricula #id_au').val();
								//alert ("despues:" + id_au_actual);
								//_post_json("http://localhost:8081/api/print",data.result,function(data) {}); //Esto ya en el pago
								_id_apod=data.id_apod;
								mostrarDatosMatricula(data.result.id_alu,data.result.id_mat,data.result.id_gpf);
								_cant_alu_mat=_cant_alu_mat+1;
								console.log(_cant_alu_mat);
								$("#btn-grabar_matricula").attr('disabled', 'disabled');
								$('#lblObservaciones').html('LISTA DE ALUMNOS MATRICULADOS (CONTRATO: '+ $('#num_cont').val()+ ' )');

								/*_get("api/matricula/matriculadosxContrato?num_cont="+ $('#num_cont').val(),function(data1) {
									$('#matriculados-tabla').dataTable(
															{	data : data1,
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
																		{"title" : "alumno","data" : "alumno"},
																		{"title" : "nivel","data" : "nivel"},
																		{"title" : "grado","data" : "grado"},
																		{"title" : "seccion","data" : "seccion"}
																]
									});
											console.log('pinta matriculadls');
								 });*/

								console.log('antes de next');
								console.log(form);
								form.steps("next");
								//return true;
								console.log('despues de next');
							});

					var id_au_sug = $('#id_au_sug').val();
					var tip_cronograma = "AC";// data.tip_cronograma;

					if (tip_cronograma == "AC") {
						if (id_au_sug != null) {
							$('#id_au').attr('disabled','disabled');
							//alert('deshabilitacion 3');
						} else
							$('#id_au').removeAttr('disabled');
					}

					$('#frm-matricula #id_gra').attr('disabled', 'disabled');
					$('#frm-matricula #id_cli').attr('disabled', 'disabled');
					$('#frm-matricula #id_niv').attr('disabled', 'disabled');
					$('#frm-matricula #fecha').attr('disabled', 'disabled');
					$('#frm-matricula #num_cont').attr('disabled', 'disabled');
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

		}else{
			//2022 no funciona
			/*if ( (_data.antiguo_sin_cronograma_cs=="1" || _data.antiguo_con_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs == "1" || _data.nuevo_con_cronograma_cs == "1") && id_au_actual!=null && id_au_actual != id_au_nueva && (_data.matricula!=null && _data.sucursal_anterior ==$('#_id_suc').text())) {
				var texto = "Se procedera a cambiar al alumno de sección";
				
				var	texto_confirm = "Si, cambiar de sección";
				

				swal({		title : "Esta seguro?",
							text : texto,
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : texto_confirm,
							cancelButtonText : "No, cancela!!!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
							
								_post("api/matricula/actualizarSeccion",
										$('#frm-matricula').serialize(),
										function(data) {
											//_data.matricula.id_au = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
											_data.matricula.id_au_asi = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
											_cambio_seccion = true;
											_cs = true;
											//if (data.result!=null)
											//	_post_json("http://localhost:8081/api/print",data.result,function(data) {});
											//_cambio_local = true;
											//form.steps("next");
											return false;
										});

							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado el cambio de secciòn",
									confirmButtonColor : "#2196F3",
									type : "error"
								});

								return false;
							}
						});
				
			} else if ( (_data.antiguo_sin_cronograma_cs=="1" || _data.antiguo_con_cronograma_cs == "1" || _data.nuevo_sin_cronograma_cs == "1" || _data.nuevo_con_cronograma_cs == "1") && id_au_actual!=null && id_au_actual != id_au_nueva && (_data.matricula!=null && _data.matricula.sucursal_anterior !=$('#_id_suc').text())) {
			*/	
				var texto = "Se procedera a cambiar al alumno de local";
				
				var	texto_confirm = "Si, cambiar de local";
				

				swal({		title : "Esta seguro?",
							text : texto,
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : texto_confirm,
							cancelButtonText : "No, cancela!!!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
							//Logica para cuando se trabaja con grado
							_post("api/matricula/actualizarGradoxLocal",
								$('#frm-matricula').serialize(),
								function(data) {
									//_data.matricula.id_au = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
									_data.matricula.id_au_asi = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
									_cambio_local = true;
									_cl = true;
									//_cambio_seccion = true;
									if (data.result!=null)
									//	_post_json("http://localhost:8081/api/print",data.result,function(data) {});
									//_cambio_local = true;
									$('#frm-matricula').steps("next");
									new PNotify(
											{	title : 'AVISO IMPORTANTE!',
												text : 'Para validar el cambio de local, se debe hacer el Pago Pendiente o el Reembolso.',
												addclass : 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
												delay : 10500
											});
								});
							//Logica cuando solo se trabajaba con aula
								/*_post("api/matricula/actualizarSeccion",
										$('#frm-matricula').serialize(),
										function(data) {
											//_data.matricula.id_au = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
											_data.matricula.id_au_asi = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
											_cambio_local = true;
											_cl = true;
											//_cambio_seccion = true;
											if (data.result!=null)
											//	_post_json("http://localhost:8081/api/print",data.result,function(data) {});
											//_cambio_local = true;
											$('#frm-matricula').steps("next");
											new PNotify(
													{	title : 'AVISO IMPORTANTE!',
														text : 'Para validar el cambio de local, se debe hacer el Pago Pendiente o el Reembolso.',
														addclass : 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
														delay : 10500
													});
										});*/

							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado el cambio de secciòn",
									confirmButtonColor : "#2196F3",
									type : "error"
								});

								return false;
							}
						});
			//}

			
		}
		}		
		
	}	
	//}
}

function grabarPago(obj){
	$('#btn-grabar_pago').attr("disabled", "disabled");
	var div_pago=$(obj).parent().parent().parent().parent();
	console.log(div_pago.html());
	//var dni=frm_entrevista.find("#nro_dni").val();
	
	if ($('#lblTotalPagar').html().indexOf('Reembolsar')==-1 && $('#lblTotalPagar').html().indexOf('Pendiente')==-1){
		//alert(1);
		
		var vuelto = $('#vuelto').val();

		if ($('#abono').val() == "") {
			_alert_error('Debe ingresar el monto a pagar',
					function() {return false;});
			 $('#btn-grabar_pago').removeAttr('disabled');
			return false;
		}

		if (parseFloat(vuelto) < 0) {
			_alert_error('Monto insuficiente para cubrir la matricula',
					function() {return false;});
			 $('#btn-grabar_pago').removeAttr('disabled');
			return false;
		}
		
		var json_pago={};
		  json_pago.id=div_pago.find("#id_pago").val();
		  json_pago.id_mat=div_pago.find("#id_mat").val();
		  json_pago.monto=div_pago.find("#total").val();
		  json_pago.id_suc=$('#_id_suc').text();
		  _POST({
				context:_URL,
				url:'api/matricula/GrabarPagoReq',
				params:JSON.stringify(json_pago),
				contentType:"application/json",
				success: function(data){
					console.log(data);
					_cant_pag=_cant_pag+1;
					console.log(_cant_pag);
					_post_json("http://localhost:8082/api/print",data.result,function(data) {}); //Esto ya en el pago
					listar_hermanos_pagos(div_pago.find("#id_gpf").val());
				}, 
				 error:function(data){
					 console.log(data);
					 _alert_error(data.msg);
					 $('#btn-grabar_pago').removeAttr('disabled');
				}
			});	
		
	} else if($('#lblTotalPagar').html().indexOf('Reembolsar')>-1 || $('#lblTotalPagar').html().indexOf('Pendiente')>-1){
	/*	_post("api/matricula/actualizarSeccion",
				$('#frm-matricula').serialize(),
				function(data) {
					//_data.matricula.id_au = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
					_data.matricula.id_au_asi = $('#frm-matricula #id_au').val(); //ahora la matricula tiene nueva aula
					_cambio_seccion = true;
					//if (data.result!=null)
					//	_post_json("http://localhost:8081/api/print",data.result,function(data) {});
					//_cambio_local = true;
					//form.steps("next");
					return false;
				});*/
		//alert('_cl'+_cl);
		var json_pago={};
		  json_pago.id_mat=div_pago.find("#id_mat").val();
		  _POST({
				context:_URL,
				url:'api/matricula/pagarCambioLocal',
				params:JSON.stringify(json_pago),
				contentType:"application/json",
				success: function(data){
					console.log(data);
					/*_cant_pag=_cant_pag+1;
					console.log(_cant_pag);
					_post_json("http://localhost:8081/api/print",data.result,function(data) {}); //Esto ya en el pago
					listar_hermanos_pagos(div_pago.find("#id_gpf").val());*/
					if (data.result!=null)
						_post_json("http://localhost:8082/api/print",data.result,function(data) {});
					_cambio_local = true;
				}, 
				 error:function(data){
					 console.log(data);
					 _alert_error(data.msg);
					 $('#btn-grabar_pago').removeAttr('disabled');
				}
			});	
	}
	
	
	
}

function generarContrato(obj, e){
	e.preventDefault();
	//var id_fam = $(obj).data('id');
	var id_per = $(obj).data('id');
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
	//document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_suc="+id_suc+"&id_anio="+$('#_id_anio').text();
	document.location.href = _URL + "api/archivo/generarContratoMatriculaWeb?id_per="+id_per+"&id_anio="+$('#_id_anio').text();	  
}

function generarAdenda(obj, e){
	e.preventDefault();
	var id_fam = $(obj).data('id');
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
	document.location.href = _URL + "api/matricula/generarAdenda?id_fam="+id_fam+"&id_suc="+id_suc+"&id_anio="+$('#_id_anio').text();
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
							var cantidadPadres = $('.btnPadre').length;	
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
							//_get('api/reglasNegocio/validarActualizacionDatos',
							//	function(data){
							//	if(data.val=="1")	
										if(cantidadPadres==_cant_pad_act && cantidadAlumnos==_cant_alu_act){
											return true;
										} else{
											alert('Debe de actualizar todos los datos de los padres y alumnos, para proceder con la matricula!!');
											return false;
										}	
											
								//});
											
						}

						if (currentIndex == 1 && newIndex == 2) {
							console.log(_cant_alu_mat);
							/*alert(id_au_actual);
							alert(id_au_nueva);
							if(id_au_actual!=null && id_au_nueva!=null && id_au_actual!=id_au_nueva){
								alert(1);
								return false;
							} else{
								alert(2);*/
								/*if (!_btnSiguienteHabil){
									return false;
								}else
									return true;*/
							console.log(_cs);
							//if(!_cs){
								if(_cant_alu_mat>=1){
									return true;
								} else{
									alert('No se puede pasar a los pagos sin almenos matricular a un alumno!!')
									return false;
								}	
							/*} else{
								return false;
							}*/
								
						/*	}
							console.log(_btnSiguienteHabil);*/
							
 							
						}

						// Forbid next action on "Warning" step if the user is
						// to young
						if (newIndex === 3 && Number($("#age-2").val()) < 18) {
							console.log('AAA');
							return false;
							
						}
						//Regla de Negocio para verificar si la actualizacion de datos es obligatorio
							//_get('api/reglasNegocio/pagoObligatorio',
							//	function(data){
							//	if(data.val=="1"){
										if (currentIndex == 2 && newIndex == 3) {
									console.log(_cant_pag);
									if(_cant_pag>=1){
										return true;
									} else{
										alert('No se puede pasar a la generación de contrato si no se ha validado la matricula mediante pago!!')
										return false;
									}
									}											
								//}
						
						//});

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

						//TAB DE PAGOS
						if (currentIndex === 1) {
						}

						if (currentIndex === 2 && priorIndex === 3) {
							console.log('DDD');
							form.steps("previous");
						}
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