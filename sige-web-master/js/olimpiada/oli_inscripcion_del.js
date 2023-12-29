//Se ejecuta cuando la pagina q lo llama le envia parametros
var nro_alu_del=null;
function onloadPage(params){
	$("#cod_mod").focus();
	var id_oli=params.id;
	var tip=params.tip;
	$("#oli_inscripcion_del-frm #id_oli").val(id_oli);
	$("#oli_inscripcion_del-frm #id_ti").val(3);
	$("#frm_registro_participante").hide();
	$("#div_postulantes").hide();
	$("#btn_agregar_participante").hide();
	$("#div_controles_del").hide();
	//$("#inscritos").hide();
	//Exigimos que escriba codigo modular
	$("#oli_inscripcion_del-frm #nom").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #corr").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #tlf").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #ape_pat_dir").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #ape_mat_dir").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #nom_dir").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #ape_pat_del").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #ape_mat_del").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #nom_del").attr("readonly","readonly");
	$("#oli_inscripcion_del-frm #id_cg").attr("disabled","disabled");
	$("#oli_inscripcion_del-frm #id_dep").attr("disabled","disabled");
	$("#oli_inscripcion_del-frm #id_pro").attr("disabled","disabled");
	$("#oli_inscripcion_del-frm #id_dist").attr("disabled","disabled");
	
	var param={id_oli:id_oli};
	_GET({url:'api/public/config/ontenerDatosConcurso',
		params: param,
		context:_URL_OLI,
		success:function(data){
			console.log(data);
			if(data!=null){
				nro_alu_del=data.nro_alu_del;
			} 
		}

	});	
	
	
	$('#oli_inscripcion_del-frm #cod_mod').on('blur',function(){

		var param={cod_mod:$("#cod_mod").val(), id_oli:id_oli};
		if($("#cod_mod").val()!=''){
		_GET({url:'api/public/delegacion/obtenerDatosDelegacion',
			params: param,
			context:_URL_OLI,
			success:function(data){
				console.log(data);
				if(data!=null){
					$("#oli_inscripcion_del-frm #id").val(data.id);
					$("#oli_inscripcion_del-frm #cod_mod").val(data.cod_mod);
					$("#oli_inscripcion_del-frm #nom").val(data.nom);
					$("#oli_inscripcion_del-frm #corr").val(data.corr);
					$("#oli_inscripcion_del-frm #tlf").val(data.tlf);
					$("#oli_inscripcion_del-frm #ape_pat_dir").val(data.ape_pat_dir);
					$("#oli_inscripcion_del-frm #ape_mat_dir").val(data.ape_mat_dir);
					$("#oli_inscripcion_del-frm #nom_dir").val(data.nom_dir);
					$("#oli_inscripcion_del-frm #ape_pat_del").val(data.ape_pat_del);
					$("#oli_inscripcion_del-frm #ape_mat_del").val(data.ape_mat_del);
					$("#oli_inscripcion_del-frm #nom_del").val(data.nom_del);
					$("#oli_inscripcion_del-frm #cod_mod").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #nom").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #corr").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #tlf").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #ape_pat_dir").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #ape_mat_dir").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #nom_dir").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #ape_pat_del").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #ape_mat_del").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #nom_del").attr("readonly","readonly");
					$("#oli_inscripcion_del-frm #id_niv").prop('disabled', 'disabled');
					$("#frm_registro_participante #id_niv").prop('disabled', 'disabled');
					/*$("#oli_inscripcion_del-frm #id_niv").val(data.id_niv); 
					$("#oli_inscripcion_del-frm #id_niv").trigger('change.select2');
					$("#oli_inscripcion_del-frm #id_niv").attr("disabled","disabled");*/
					
					
					var param_ins={id_od:$("#oli_inscripcion_del-frm #id").val()};
					_GET({url:'api/public/delegacion/obtenerCantidadInscritos',
						params: param_ins,
						context:_URL_OLI,
						success:function(data1){
							console.log(data1.cant_ins);
							if(data1!=null){
								//$("#inscritos").show();	
								//$("#cant_ins").val(data1.cant_ins);
								//if(data1.cant_ins<nro_alu_del){
									$("#div_postulantes").show();
									$("#frm_registro_participante").show();
								//}	
							} else{
								$("#frm_registro_participante").hide();	
								$("#div_postulantes").hide();
							}
						}

					});	
					
					_llenar_combo({
						tabla:'cat_gestion',
						combo:$('#id_cg'),
						id:data.id_cg,
						//id:id_cg,
						context: _URL_OLI,
						funcion:function(){
							$("#oli_inscripcion_del-frm #id_cg").prop('disabled', 'disabled');
						}
					});	
					
					_llenar_combo({
					   	url:'api/public/grado/listarNiveles/'+ id_oli,
						combo:$('#oli_inscripcion_del-frm #id_niv'),
						context:_URL_OLI,
						id:data.id_niv,
						text:'nivel',
						funcion:function(){
							//$("#oli_inscripcion_del-frm #id_niv").trigger('change.select2');
							//$("#oli_inscripcion_del-frm #id_niv").attr("disbled","disabled");
						}
					});
					
					$("#id_dep").attr("readonly","readonly");
					$("#id_pro").attr("readonly","readonly");
					$("#id_dist").attr("readonly","readonly");
					
					seleccionarUbigeo(data.id_dep, data.id_pro, data.id_dis);
					  
				 
					
					$('#frm_registro_participante #id_niv').on('change',function(event){
						//var arr_param = [{id_dep:$('#id_dep').val()},{est:'A'}];
						_llenar_combo({
							url:'api/public/grado/listarGrados/'+id_oli+'/'+$('#frm_registro_participante #id_niv').val(),
							combo:$('#id_og'),
							text:'grado',
							context: _URL_OLI
						});	
					});
					
					_llenar_combo({
					   	url:'api/public/grado/listarNiveles/'+ id_oli,
						combo:$('#frm_registro_participante #id_niv'),
						context:_URL_OLI,
						id: data.id_niv,
						text:'nivel',
						funcion:function(){
							$('#frm_registro_participante #id_niv').change();
						}
					});
					
				} else{
					//Habilitamos los input y select
					$("#oli_inscripcion_del-frm #nom").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #corr").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #tlf").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #ape_pat_dir").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #ape_mat_dir").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #nom_dir").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #ape_pat_del").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #ape_mat_del").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #nom_del").removeAttr("readonly");
					$("#oli_inscripcion_del-frm #id_cg").removeAttr("disabled");
					$("#oli_inscripcion_del-frm #id_dep").removeAttr("disabled");
					$("#oli_inscripcion_del-frm #id_pro").removeAttr("disabled");
					$("#oli_inscripcion_del-frm #id_dist").removeAttr("disabled");
					var param={cod_mod:$("#cod_mod").val()};
					_GET({url:'api/public/colegioOrg/obtenerDatosColegio',
						params: param,
						context:_URL_OLI,
						success:function(data){
							if(data!=null){
								//Validar si es del nivel q se desea inscribir
								/*_GET({url:'api/public/grado/listarNiveles/'+ id_oli,
									context:_URL_OLI,
									success:function(data1){
										if(data!=null){
											for(var i=0; i<data1.length; i++){
												if(data.id_niv==)
											}
										}
										}
									});*/
								
								$("#oli_inscripcion_del-frm #nom").val(data.nom);
								$("#oli_inscripcion_del-frm #nom").attr("readonly","readonly");
								//var estado=$("#oli_inscripcion_del-frm #estado").val();
								var estado=data.estatal;
								$("#oli_inscripcion_del-frm #id_niv").prop("disabled","disabled");
								seleccionarUbigeo(data.id_dep, data.id_pro, data.id_dis);
								
								var id_ges="";
								if(estado==1)
									id_ges=1;
								else
									id_ges=2;
								
								$("#id_oli").val(id_oli);
								_llenar_combo({
									tabla:'cat_gestion',
									combo:$('#id_cg'),
									//id:data.estatal,
									id:id_ges,
									context: _URL_OLI,
									funcion:function(){
										if(id_ges!=null)
											$("#oli_inscripcion_del-frm #id_cg").prop('disabled', 'disabled');
									}
								});	
								
								_llenar_combo({
								   	url:'api/public/grado/listarNiveles/'+ id_oli,
									combo:$('#oli_inscripcion_del-frm #id_niv'),
									id: data.id_niv,
									context:_URL_OLI,
									text:'nivel',
									funcion:function(){
										//$('#id_niv').change();
									}
								});
							} else{
								$("#id_oli").val(id_oli);
								_llenar_combo({
									tabla:'cat_gestion',
									combo:$('#id_cg'),
									context: _URL_OLI
								});	
								
								_llenar_combo({
								   	url:'api/public/grado/listarNiveles/'+ id_oli,
									combo:$('#oli_inscripcion_del-frm #id_niv'),
									context:_URL_OLI,
									text:'nivel',
									funcion:function(){
										//$('#id_niv').change();
									}
								});
								
								seleccionarUbigeo(null,null, null);
								
							}
							
						}

					});	
					


				}
				
			}

		});	
	}
	});
	
	var validator = $("#oli_inscripcion_del-frm").validate();
	
	$('#btn-grabar-del').on('click',function(event){
		
		if ($("#oli_inscripcion_del-frm").valid()){

			$('#btn-grabar-del').attr('disabled','disabled');
			$("#oli_inscripcion_del-frm #id_cg").removeAttr('disabled');
			$("#oli_inscripcion_del-frm #id_niv").removeAttr('disabled');
			_POST({form:$('#oli_inscripcion_del-frm'),
				  context:_URL_OLI,
				  msg_type:'notification',
				  success:function(data){
					  $("#oli_inscripcion_del-frm #id_cg").prop('disabled','disabled');
					  $("#oli_inscripcion_del-frm #id_niv").prop('disabled','disabled');
					  $('#oli_inscripcion_del-frm #id').val(data.result);
					  $("#frm_registro_participante #id_od").val(data.result);
					  $("#frm_registro_participante").show();
					  $("#frm_registro_participante #btn-eliminar-part").hide();
					  $("#div_postulantes").show();
						_llenar_combo({
						   	url:'api/public/grado/listarNiveles/'+ id_oli,
							combo:$('#frm_registro_participante #id_niv'),
							context:_URL_OLI,
							text:'nivel',
							id: $('#oli_inscripcion_del-frm #id_niv').val(),
							funcion:function(){
								$('#frm_registro_participante #id_niv').prop("disabled","disabled");
								$('#frm_registro_participante #id_niv').change();
							}
						});

						$('#frm_registro_participante #id_niv').on('change',function(event){
							_llenar_combo({
								url:'api/public/grado/listarGrados/'+id_oli+'/'+$('#frm_registro_participante #id_niv').val(),
								combo:$('#id_og'),
								text:'grado',
								context: _URL_OLI
							});	
						});
				}
			});
			
		}

	});
	
		
	
	$("#btn_agregar_participante").on('click',function(event){
		/*var cant_div=$(".div_participante").length;
		var cant_ins=parseInt($("#cant_ins").val())+cant_div;
		if(cant_ins>nro_alu_del){
			alert('Ya no puede inscribir más postulantes porque ya llegó al máximo de alumnos por delegación!!');
			$("#btn_agregar_participante").attr('disabled','disabled');
		}*/
		var last=$(".div_participante").last();
		console.log(last.html());
		divParticipante = last.clone(false);
		divParticipante.find("#nro_dni").val('');
		divParticipante.find("#ape_pat").val('');
		divParticipante.find("#ape_mat").val('');
		divParticipante.find("#nom").val('');
		divParticipante.find("#id").val('');
		$("#div_registro_participante").append(divParticipante);
		divParticipante.find("#nro_dni").focus();
		$("#btn_agregar_participante").hide();
		//divParticipante.find('#btn-eliminar-part').hide();
		divParticipante.find('#btn-eliminar-part').hide();
		divParticipante.find('#btn-registrar-part').show();

		
		//poner como escritura
		divParticipante.find('#nro_dni').removeAttr('readonly');
		divParticipante.find('#ape_pat').removeAttr('readonly');
		divParticipante.find('#ape_mat').removeAttr('readonly');
		divParticipante.find('#nom').removeAttr('readonly');
		divParticipante.find('#frm_registro_participante #id_niv').val($("#oli_inscripcion_del-frm #id_niv").val());
		//divParticipante.find('#frm_registro_participante #id_niv').removeAttr('disabled');
		divParticipante.find('#id_og').removeAttr('disabled');
		
		//last.find("#btn-registrar-part").show();

	});

//EVENTOS
	
	$('#id_dep').on('change',function(event){
		 console.log($(this).data('id_pro'));
		_llenar_combo({
			url:'api/comboCache/provincias/' + $(this).val(),
			context: _URL_OLI,
			combo:$('#id_pro'),
		   	id:$(this).data('id_pro'),
		   	funcion:function(){
				$('#id_pro').change();
			}
		});
 

	});
	

	$('#id_pro').on('change',function(event){
 
		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					context: _URL_OLI,
					combo:$('#id_dist'),
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



//aqui

function registrarParticipante(obj){
	var frm_participante=$(obj).parent().parent().parent();
	var dni=frm_participante.find("#nro_dni").val();
	
	frm_participante.find("#id_dis").val($('#id_dist').val());
	if(dni.length<8){
		alert('La cantidad de dígitos para el DNI es de 8');
		frm_participante.find($("#nro_dni")).focus();
	} else{
		$('#frm_registro_participante #id_niv').removeAttr('disabled');
		var param="id_oli="+$("#id_oli").val()+"&id_ti="+$("#id_ti").val()+"&id_cg="+$("#id_cg").val()+"&id_od="+$("#oli_inscripcion_del-frm #id").val();
		_POST({form:frm_participante,
			  //url:'api/inscripcionDel',
			  params:param,
			  msg_type:'notification',
			 // msg_exito : 'PARA FINALIZAR LA INSCRIPCION, CANCELAR EN SECRETARIA.',
			  context:_URL_OLI,
			  success:function(data){
				  console.log(data.result);
				  $("#btn_agregar_participante").show();
				  $("#div_controles_del").show();
				  $('#frm_registro_participante #id_niv').prop('disabled','disabled');
				  //frm_participante.find($("#btn-registrar-part")).hide();
				  console.log(frm_participante.find("#id"));
				  (frm_participante.find("#id")).val(data.result);
				  frm_participante.find('#btn-registrar-part').hide();
				  frm_participante.find('#btn-eliminar-part').show();
				  //poner como lectura
				  frm_participante.find('#nro_dni').attr('readonly','readonly');
				  frm_participante.find('#ape_pat').attr('readonly','readonly');
				  frm_participante.find('#ape_mat').attr('readonly','readonly');
				  frm_participante.find('#nom').attr('readonly','readonly');
				  frm_participante.find('#frm_registro_participante #id_niv').attr('disabled','disabled');
				  frm_participante.find('#id_og').attr('disabled','disabled');
			},
			 error:function(data){
				// alert(1);
				 console.log(data);
				 _alert_error(data.msg);
			}
		});
	}	
	
};	

	function eliminarParticipante(obj){
	var frm_participante=$(obj).parent().parent().parent();
	_DELETE({url:'api/public/inscripcionDel/' + frm_participante.find("#id").val(),
		context:_URL,
		success:function(){
				frm_participante.find("#nro_dni").val('');
				frm_participante.find("#ape_pat").val('');
				frm_participante.find("#ape_mat").val('');
				frm_participante.find("#nom").val('');
				frm_participante.find("#id").val('');
				  
				var cant_div=$(".div_participante").length;
				if(cant_div>1){
					frm_participante.remove();
				}				

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

			_llenar_combo({
				//tabla:'cat_departamento',
				url:'api/comboCache/departamentos/193',
				combo:$('#id_dep'),
				//context: _URL,
				context: _URL_OLI,
			   	id:id_dep,
			   	funcion:function(){
					$('#id_dep').change();
				}
			});	

		}
			
	}
	
	function imprimirCarnets(){
		//obtener todos los id de de l delegacion
		var id_oigs='';
		$( ".div_participante" ).each(function( index ) {
			var id = $(this).find('#id').val();
			id_oigs = id_oigs + "-" + id;
			});
		
		
	    _download(_URL_OLI+'api/public/inscripcionInd/imprimirCarnet?correo=1&id_ins='+$("#oli_inscripcion_del-frm #id").val()+'&tipo=D&id_oigs=' + id_oigs,'delegacion_olimpiada.pdf');
	    
		new PNotify({
	         title: 'Grabación existosa!',
	         text: 'PARA FINALIZAR LA INSCRIPCION, DEBERÁ REALIZAR EL PAGO EN SECRETARIA.',
	         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
	         delay:2500
	     });


	}
	
	function imprimirFichaInscripcion(){
		
		var id_oigs='';
		$( ".div_participante" ).each(function( index ) {
			var id = $(this).find('#id').val();
			id_oigs = id_oigs + "-" + id;
			});

		new PNotify({
	         title: 'Grabación existosa!',
	         text: 'PARA FINALIZAR LA INSCRIPCION, DEBERÁ REALIZAR EL PAGO EN SECRETARIA.',
	         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
	         delay:2500
	     });

 		_download(_URL_OLI+'api/public/inscripcionDel/imprimirFichaIns?id_od='+$("#oli_inscripcion_del-frm #id").val()+'&id_oigs=' + id_oigs,'ficha_olimpiada.pdf');
		
		//Enviar Correo a la delegacion
		_POST({url:'/api/public/inscripcionDel/reenviar/' + $("#oli_inscripcion_del-frm #id").val(), context:_URL_OLI});
	}

	