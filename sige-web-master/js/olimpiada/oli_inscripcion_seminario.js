//Se ejecuta cuando la pagina q lo llama le envia parametros
var _tip;
var _id_ges_int;
function onloadPage(params){
	
	var id_sem=params.id;
	var tip=params.tip;
	_tip = tip;

	$("#id_sem").val(id_sem);
	$("#nro_dni").focus();
	if(tip=='I'){
		$("#ape_pat").attr("readonly","readonly");
		$("#ape_mat").attr("readonly","readonly");
		//$("#corr").attr("readonly","readonly");
		$("#ape_pat").attr("readonly","readonly");
		$("#col").attr("readonly","readonly");	
		$("#id_ti").val(1);
		var param={id_oli:id_oli};
		_GET({url:'api/public/config/ontenerDatosConcurso',
			params: param,
			context:_URL_OLI,
			success:function(data){
				console.log(data.id_dep);
				if(data!=null){
					$("#nivel").val(data.nivel);
					$("#col_org").val(data.colegio);
					$("#col").val(data.colegio);
					$("#col").attr("readonly","readonly");
					$("#titulo").text('INSCRIPCIÓN AL SEMINARIO');
					$("#descripcion").text('Completar sus datos, por favor. ');
					
					$('#id_dep').attr("disabled","disabled");
					$('#id_dist').attr("disabled","disabled");
					$('#id_pro').attr("disabled","disabled");
					
					seleccionarUbigeo(data.id_dep, data.id_pro, data.id_dis);
					
					var gestion=parseInt(data.estatal);
					var id_ges=null;
					if(gestion==1){
						id_ges=1;
						_id_ges_int=1;
					} else if(gestion==0){
						id_ges=2;
						_id_ges_int=2;
					}
					_llenar_combo({
						tabla:'cat_gestion',
						combo:$('#id_cg'),
						id:id_ges,
						context:_URL_OLI,
						funcion:function(){
							$('#id_cg').change();
							$('#id_cg').attr("disabled","disabled");
						}
					});
					
					$('#id_cg').on('change',function(event){
						var param={id_oli:id_oli,id_ti:$("#id_ti").val(), id_cg:$('#id_cg').val()};
						if($('#id_cg').val()!=null && $('#id_cg').val()!=''){

							console.log(param);
							
							_GET({url:'api/public/costo/obtenerCostoInscripcion',
								params: param,
								context:_URL_OLI,
								success:function(data){
									console.log(data);
									if(data==null){
										_alert_error('No existe un costo configurado para estas fechas, porfavor comunicarse con el colegio organizador!!');
										$('#btn-grabar').attr('disabled','disabled');
									} else{
										$("#oli_inscripcion_ind-frm #id_oc").val(data.id);
										$('#btn-grabar').removeAttr("disabled");
									}
								}

							});	
						}

					});
					
				}
				
			}

		});	
		
		$('#id_niv').on('change',function(event){
			//var arr_param = [{id_dep:$('#id_dep').val()},{est:'A'}];
				//alert($('#id_niv').val());
			if($('#id_niv').val()!=''){
				_llenar_combo({
					url:'api/public/grado/listarGrados/'+id_oli+'/'+$('#id_niv').val(),
					combo:$('#id_og'),
					text:'grado',
					context: _URL_OLI,
					funcion:function(){
						$('#id_og').change();
					}
				});	
			}

		});
		
		_llenar_combo({
		   	url:'api/public/grado/listarNiveles/'+ id_oli,
			combo:$('#id_niv'),
			context:_URL_OLI,
			text:'nivel',
			funcion:function(){
				$('#id_niv').change();
			}
		});
		//Autocompletar datos del alumno
		$('#ape_pat').focus(function() {
			var param={dni:$('#nro_dni').val(),id_anio:6,id_oli:$("#id_oli").val()};
			_GET({url:'api/public/inscripcionInd/obtenerDatosAlumnoxDNI',
				context:_URL_OLI,
				params:param,
				success:function(data){
					console.log(data);
					if(data!=null){
						$("#ape_pat").val(data.ape_pat);
						$("#ape_mat").val(data.ape_mat);
						$("#nom").val(data.nom);
						$("#ape_pat").attr("readonly","readonly");
						$("#ape_mat").attr("readonly","readonly");
						$("#nom").attr("readonly","readonly");

						//$("#id_cg").val("0");//particular
						if(data.estatal=="0")
							$("#id_cg").val("2");//particular
						else if(data.estatal=="1")
							$("#id_cg").val("1");//particular
						$("#id_cg").attr("disabled","disabled");
						
						$('#id_dep').attr("disabled","disabled");
						$('#id_dist').attr("disabled","disabled");
						$('#id_pro').attr("disabled","disabled");
						$('#id_niv').attr("disabled","disabled");
						$('#id_og').attr("disabled","disabled");
						
						$('#id_dep').val(data.id_dep);
						$('#id_pro').val(data.id_pro);
						//alert(data.id_dis);
						$('#id_dist').val(data.id_dis);
						
						$('#col').val(data.col);
						
						$('#id_cg').change();
						
						$('#id_niv').val(data.id_niv);
						$('#id_niv').change();
						//$('#id_niv').
						//$("#id_og").val(data.oli_id_gra);
						$('#id_og').on('change',function(event){
							$("#id_og").val(data.oli_id_gra);
						
						});	
						
						
						
						
					} else{
						$("#ape_pat").removeAttr("readonly");
						$("#ape_mat").removeAttr("readonly");
						$('#id_dist').attr("disabled","disabled");
					} /*else{

						
						$('#id_niv').on('change',function(event){
							//var arr_param = [{id_dep:$('#id_dep').val()},{est:'A'}];
								//alert($('#id_niv').val());
								_llenar_combo({
									url:'api/public/grado/listarGrados/'+id_oli+'/'+$('#id_niv').val(),
									combo:$('#id_og'),
									text:'grado',
									context: _URL_OLI,
									funcion:function(){
										$('#id_og').change();
									}
								});	

						});
						
						_llenar_combo({
						   	url:'api/public/grado/listarNiveles/'+ id_oli,
							combo:$('#id_niv'),
							context:_URL_OLI,
							text:'nivel',
							funcion:function(){
								$('#id_niv').change();
							}
						});
						
						
						$("#ape_pat").removeAttr("readonly");
						$("#ape_mat").removeAttr("readonly");
						$('#id_dist').attr("disabled","disabled");
						
					}*/
				}

				});
		});
		
		
		
	}else if(params.tip=='L'){
		$("#id_ti").val(2);
		$("#titulo").text('INSCRIPCIÓN AL SEMINARIO');
		$("#descripcion").text('Completar sus datos, por favor. ');
		seleccionarUbigeo(null, null, null);
		/*$('#ape_pat').focus(function() {
		var nro_doc=$('#nro_dni').val();
						if(nro_doc!=''){
							_GET({ url: 'api/archivo/obtenerDatosPersonaxNroDoc/'+nro_doc,
								context: _URL,
							  // params:param,
							   success:
								function(data){
								   console.log(data);
								if(data!=null){
									console.log(data);
									_fillForm(data, $('#oli_inscripcion_ind-frm'));
								  $('#id_dep').change();
									
								} else{
									//_familiar_existe=false;
								}	 				
						}});
						}
		});	*/



		
	}
	
	
	$('#btn-grabar').on('click',function(event){
	$("#oli_inscripcion_ind-frm #id").val('');
		var dni=$("#nro_dni").val();
		 var validator = $("#oli_inscripcion_ind-frm").validate();
		 if ($("#oli_inscripcion_ind-frm").valid()){
				if(dni.length<8){
					alert('La cantidad de dígitos para el DNI es de 8');
					$("#nro_dni").focus();
				} else{
					 var param={dni:$('#nro_dni').val(),id_sem:$('#id_sem').val()}
					 $("#id_dist").removeAttr('disabled');
						_GET({url:'api/archivo/existeAlumnoSeminario',
							params: param,
							success:function(data){
								if(data==true){
									_alert_error('El número de DNI ingresado ya se encuentra inscrito en el respectivo seminario, verifíquelo');
									 $('#nro_dni').focus();
								} else{
										 $('#btn-grabar').attr('disabled','disabled');
											
											_POST({form:$('#oli_inscripcion_ind-frm'),
												 // context:_URL_OLI,
												  msg_type:'notification',
												  msg_exito : 'SU INSCRIPCIÓN SE REALIZÓ CON ÉXITO.',
												  success:function(data){
													    //DESCARGAR 
													    _download(_URL+'api/archivo/imprimirCarnetSeminario?id_ins='+data.result,dni + '_seminario.pdf');
													    
													   // $('#oli_inscripcion_ind-frm').trigger('reset');
													    $('#btn-grabar').removeAttr('disabled');
													  //Limpiamos los campos necesarios
													    $('#oli_inscripcion_ind-frm #id').val('');
													    $('#oli_inscripcion_ind-frm #nro_dni').val('');
													    $('#oli_inscripcion_ind-frm #ape_pat').val('');
													    $('#oli_inscripcion_ind-frm #ape_mat').val('');
													    $('#oli_inscripcion_ind-frm #nom').val('');
													    $('#oli_inscripcion_ind-frm #corr').val('');
													    $('#oli_inscripcion_ind-frm #id_niv').val('');
														//$('#oli_inscripcion_ind-frm #id_niv').change();
														$('#oli_inscripcion_ind-frm #id_og').val('');
														$('#oli_inscripcion_ind-frm #id_cg').val('');
														//$('#oli_inscripcion_ind-frm #id_og').change();
													    //$('#oli_inscripcion_ind-frm #id_og').empty();
													   // $('#oli_inscripcion_ind-frm #id_niv').val('');
													  //  $('#oli_inscripcion_ind-frm #id_niv').change();
													    //$('#oli_inscripcion_ind-frm #id_og').val('');
													    //$('#oli_inscripcion_ind-frm #id_og').change();
													 //   $('#oli_inscripcion_ind-frm #id_cg').val('');
													  //$('#oli_inscripcion_ind-frm').trigger('reset');
		
													    
													    
													 
												 }, 
												 error:function(data){
													 _alert_error(data.msg);
													 $('#btn-grabar').removeAttr('disabled');
													 $('#nro_dni').focus();
												}
											}); 
								}
									
							}

						});	
				} 
		 }

	});
	
	
	//EVENTOS
	
	$('#id_dep').on('change',function(event){
		 console.log($(this).data('id_pro'));
		_llenar_combo({
			url:'api/comboCache/provincias/' + $(this).val(),
			combo:$('#id_pro'),
			context: _URL_OLI,
		   	id:$(this).data('id_pro'),
		   	funcion:function(){
				$('#id_pro').change();
			}
		});
 

	});
	

	$('#id_pro').on('change',function(event){
		 	if ($(this).val()==''){ 
		 		$('#id_dist').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist'),
					context: _URL_OLI,
				   	id:$(this).data('id_dis'),
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
				   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
	});

}


function seleccionarUbigeo(id_dep, id_pro, id_dis){
	$('#id_dep').data('id_pro',id_pro);
	$('#id_pro').data('id_dis',id_dis);
	
	if($('#id_dep option').length>1){
		$('#id_dep').val(id_dep);
		$('#id_dep').change();
	}else{
	var id_pais=193;
	id_dep=3;
		_llenar_combo({
			//tabla:'cat_departamento',
			//url:'api/comboCache/departamentos/' + $(this).val(),
			url:'api/comboCache/departamentos/'+id_pais,
			combo:$('#id_dep'),
			context: _URL_OLI,
		   	id:id_dep,
		   	funcion:function(){
				$('#id_dep').change();
			}
		});

	}
		
}



