//Se ejecuta cuando la pagina q lo llama le envia parametros
var _PARENTESCO_PAPA = "2";
var _PARENTESCO_MAMA = "1";
var _GENERO_FEMENINO = "0";
var _GENERO_MASCULINO = "1";
var _id_gpf;
var _id_fam;
var _id_alu='';
var _cant_padres;
var _cant_padres;
var _primeraCarga ;//true: cuando se jala los datos desde data
						  //false: cuando el usuario ya jalo los datos y puede cambiar los datos del combo
var _data;
var _familiar_existe;
var _madre_existe;
var _padre_existe;

function onloadPage(params){
	console.log(params);
	_id_gpf = params.id_gpf;
	_id_fam = params.id_fam;
	_dni = params.dni;
	
	$("#alu_familiar-frm #ape_pat").focus();
	$('#id_par').on('change',function(event){
		if($('#id_par').val()==_PARENTESCO_PAPA){
			$('#id_gen1').click();
		} else if($('#id_par').val()==_PARENTESCO_MAMA){
			$('#id_gen2').click();
		}
		
	});
	
	
	/////_onloadPage();

	
	//FORMULARIO PARA ACTUALIZAR LOS DATOS DEL USUARIO
	
		var validator = $("#alu_familiar-frm").validate();
		
		/*$('#alu_familiar-frm').on('submit',function(event){
			event.preventDefault();
			if ($("#alu_familiar-frm").valid()){
				
				if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
					_post($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize());
				}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
					
					//logica michael
					_post_silent($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize(),
						function(data){
						/*
							swal({
					            html:true,
					            title: "<font color='black'>Se actualiz&oacute; sus datos existosamente!</font>",
					            text: "<font color='black'>Se envi&oacute; un codigo de seguridad a su correo, por favor ingresarlo en el sistema para finalizar su registro.<br>Si el correo no es correo por favor actualice su correo</font>",
					            confirmButtonColor: "#66BB6A",
					            type: "success"
					        });
					        */
					/*		_send('pages/alumno/alu_familiar_confirmacion.html','Confirmacion','Datos ',{actualizo_datos:1},
									function(data){
										$('#id').val(_usuario.id);
									}
								);
							}
						);
				}
			}
		});*/
		
		$("#alu_familiar-frm #nro_doc").on('blur',function(){
			if($("#alu_familiar-frm #nro_doc").val()!=null && $("#alu_familiar-frm #nro_doc").val()!=""){
				var param ={id_fam:null, nro_doc:$("#alu_familiar-frm #nro_doc").val()};
				_GET({ url: 'api/familiar/obtenerDatosFamiliar/',
						context: _URL,
					   params:param,
					   success:
						function(data){
						   console.log(data);
						if(data!=null){
							console.log(data);
							_fillForm(data,$('#alu_familiar-frm'));
							
							$("#foto").attr('src', 'data:image/png;base64,' + data.foto);
							
							var fec_nac = data.fec_nac;
							if(fec_nac){
								var arrFec_nac = fec_nac.split('-');
								$('#alu_familiar-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);							
							}
							
							
							

							_inputs('alu_familiar-frm');
							$("#alu_familiar-frm [name=id_tap]").val([data.id_tap]);
							//$("#alu_familiar-frm [name=id_gen]").val([data.id_gen]);


							//SOLO LECTURA SI HAY DATOS CONFIRMADOS
							if (data.ini=="3"){//DATOS CONFIRMADOS
								$("[name=id_tap]").attr('disabled', true);
								$("[name=id_gen]").attr('disabled', true);
								$('#corr').attr('readonly', 'readonly');
								$('#nro_doc').attr('readonly', 'readonly');
							}
							
							$("input[name='id_gen']").val(data.id_gen);
							_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'), data.id_tdc,null,function(){
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
								function(){
									$('#id_par').change();
								}
							});	
							_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'), data.id_eci);
							_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
							_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);

							
							_llenarCombo('cat_pais',$('#id_pais'),data.id_pais,null, function(){
								$('#id_pais').change();
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
							_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
							
							$('#id_dep').on('change',function(event){
								 if($('#id_dep').val()!=null){
									 _llenar_combo({
											url:'api/comboCache/provincias/' + $(this).val(),
											combo:$('#id_pro'),
										   	id:data.id_pro,
										   	funcion:function(){
												$('#id_pro').change();
											}
										}); 
								 }
							});
							

							$('#id_pro').on('change',function(event){
						 
								 	if ($(this).val()==null || $(this).val()==''){ 
								 		$('#id_dist').find('option').not(':first').remove();
								 	}
								 	else{
										_llenar_combo({
											url:'api/comboCache/distritos/' + $(this).val(),
											combo:$('#id_dist'),
										   	id:data.id_dist,
										   	funcion:function(){
										   		console.log('remove data');
										   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
										   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
										   	}
										});		 		
								 	}
								 
							});
							
							if(data.ocu==null){
								$("#id_tra_rem2").click();
							} else{
								$("#id_tra_rem1").click();
							} 
							
							$('#alu_familiar-frm #nro_doc').attr('readonly', 'readonly');
							$('#alu_familiar-frm #ape_pat').attr('readonly', 'readonly');
							$('#alu_familiar-frm #ape_mat').attr('readonly', 'readonly');
							$('#alu_familiar-frm #nom').attr('readonly', 'readonly');
							$('#alu_familiar-frm #id_par').attr('disabled', 'disabled');
							$('#alu_familiar-frm #id_tdc').attr('disabled', 'disabled');
							//$('#alu_familiar-frm #id_tap').attr('disabled', 'disabled');
							//$(".id_tap").prop("disabled", true); 
							$('#alu_familiar-frm input[name=id_tap]').attr("disabled",true);
							$('#alu_familiar-frm #id_gpf').val(_id_gpf);
							_familiar_existe=true;
						} else{
							_familiar_existe=false;
						}	 				
				}});
			}
		});
		
		$("#alu_familiar-frm #id_tdc").on('change', function(){
			if($(this).val()==1){
				 $("#alu_familiar-frm #nro_doc").attr('maxlength','8');
			}else if($(this).val()==2){
				 $("#alu_familiar-frm #nro_doc").attr('maxlength','11');
			}
		});
		
		$('#alu_familiar-frm').on('submit',function(event){
			var validator = $("#alu_familiar-frm").validate();
			  $('#alu_familiar-frm #id_tdc').removeAttr('disabled');
			  $('#alu_familiar-frm #id_par').removeAttr('disabled');
			 
			event.preventDefault();
			if($('#alu_familiar-frm #nro_doc').val().length<8 && $('#alu_familiar-frm #id_tdc').val()==1 ){
				alert('El DNI debe de tener 8 dígitos!!');
				$("#alu_familiar-frm #nro_doc").focus();
			} else if($('#alu_familiar-frm #nro_doc').val().length<11 && $('#alu_familiar-frm #id_tdc').val()==3){
				alert('El carnet de extranjeria debe de tener 11 dígitos!!');
				$("#alu_familiar-frm #nro_doc").focus();
			} else{
				 $('#alu_familiar-frm input[name=id_tap]').attr("disabled",false);
				if ($("#alu_familiar-frm").valid()){
					if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
						_post($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize());
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
						if(!$("#alu_familiar-frm input[name='id_gen']").is(':checked')){
							alert("Seleccione el género, porfavor!!");			
						} else{
							$('#alu_familiar-frm #btn-grabar').attr("disabled", "disabled");
							_POST({form:$('#alu_familiar-frm'),
								  context:_URL,
								  msg_type:'notification',
								  msg_exito : 'SE REGISTRO AL FAMILIAR CORRECTAMENTE.',
								  success:function(data){
									    //DESCARGAR 
									  //id_gpf=2490, id_per=null, id=5010, error=null
									  $("#alu_familiar-frm #id").val(data.id);
									  $("#alu_familiar-frm #id_gpf").val(data.id_gpf);
									  $('#alu_alumno-frm #id_gpf').val(data.id_gpf);
									  console.log(data.id_par);
									  //Por ahora envio correo de ususario
									  //event.preventDefault();
										//var id_gra = $(obj).data('id');	
										//_POST({url:'/api/familiar/enviarMensajexFamiliar/' + data.id+'/'+ data.id_gpf+'/'+$('#_id_anio').text(), context:_URL});
									  var parentesco=data.id_par;
									  if(parentesco=="1"){
										 _madre_existe=true;
										 _padre_existe=false;
									  } else if(parentesco=="2"){
										_padre_existe=true; 
										_madre_existe=false;
									  }
									  $('#alu_familiar-frm #btn-grabar').removeAttr('disabled');
									  listar_padres(data.id_gpf);
									  $('#alu_familiar-frm #id_tdc').attr("disabled", "disabled");
									  $('#alu_familiar-frm #id_par').attr("disabled", "disabled");
									  $('#alu_familiar-frm input[name=id_tap]').attr("disabled",true);
									  $("#frm_datos_familiar").css('display','none');
									  $("#alu_familiar-frm #btn-grabar").css('display','none');
									  _get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + data.id_gpf + "&id_alu=" + _id_alu,
												function(data){
												$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
													mostrarDatosTabAlumno(data);
													$("#div_alumno").css('display','block');
													$('#alu_alumno-frm #id_gpf').val(data.id_gpf);
												});
												
										});
									  console.log(_madre_existe);
									  console.log(_padre_existe);
								 }, 
								 error:function(data){
									 console.log(data);
									 _alert_error(data.msg);
									 $('#alu_familiar-frm #btn-grabar').removeAttr('disabled');
									 $('#nro_dni').focus();
									 if(_familiar_existe){
										 $('#alu_familiar-frm #id_tdc').attr("disabled", "disabled");
										  $('#alu_familiar-frm #id_par').attr("disabled", "disabled");
										  $('#alu_familiar-frm input[name=id_tap]').attr("disabled",true); 
									 }
								}
							}); 
						}	
						//}
						
						
					}
				}
			}
		
			/*
			} else{
				alert('El DNI debe de tener 8 dígitos!!');
				$("#alu_familiar-frm #nro_doc").focus();
			}*/
		});
		
		
		
		if(_id_gpf!=null && _id_fam!=null){
		//alert(1);	
		listar_padres(_id_gpf);

		$("#frm_datos_familiar").css('display','none');
		$("#alu_familiar-frm #btn-grabar").css('display','none');
		$('#alu_familiar-frm #id_gpf').val(_id_gpf);
		/*$('#alu_familiar-frm #nro_doc').attr('readonly', 'readonly');
		$('#alu_familiar-frm #ape_pat').attr('readonly', 'readonly');
		$('#alu_familiar-frm #ape_mat').attr('readonly', 'readonly');
		$('#alu_familiar-frm #nom').attr('readonly', 'readonly');
		$('#alu_familiar-frm #id_gpf').val(_id_gpf);
		$('#alu_familiar-frm #id_par').attr('disabled', 'disabled');
		$('#alu_familiar-frm #id_tdc').attr('disabled', 'disabled');
		$("#frm_datos_familiar").css('display','none');
		$("#alu_familiar-frm #btn-grabar").css('display','none');
		
		/*$('#viv').on('change',function(event){
			 
			if($("#alu_familiar-frm #viv").val()==1){
				$("#alu_familiar-frm #div_defuncion").css('display','none');
			} else if($("#alu_familiar-frm #viv").val()==0){
				$("#alu_familiar-frm #div_defuncion").css('display','block');
			}			 
		});*/
		
					
		_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + _id_gpf + "&id_alu=" + _id_alu,
				function(data){
				
				$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
					mostrarDatosTabAlumno(data);
					$("#div_alumno").css('display','none');
					$('#alu_alumno-frm #id_gpf').val(_id_gpf);
				});
				
		});
	/*	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + _id_gpf + "&id_alu=" + _id_alu,
				function(data){*/
				
		/*		$("#fam_otros_familiares-tab").load("pages/alumno/alu_familiar_otro_fam.html", {}, function(responseText, textStatus, req){
					mostrarDatosTabOtroFam(_id_gpf);
					$("#div_otro_familiar").css('display','none');
					//"OtroFamiliarEditar?id_fam=" + id + "&id_gpf=" + $("#_id_gpf").val(),
					//$('#alu_alumno-frm #id_gpf').val(_id_gpf);
				});
				
		//});
		/*_get('api/familiar/obtenerDatosFamiliar/' + _id_fam+'/'+null,
				function(data){*/	
			/*var param ={id_fam:_id_fam, nro_doc:null};
			_GET({ url: 'api/familiar/obtenerDatosFamiliar/',
				context: _URL,
			   params:param,
			   success:
				function(data){		
					
					console.log(data);
					_fillForm(data,$('#alu_familiar-frm'));
					
					$("#foto").attr('src', 'data:image/png;base64,' + data.foto);

					
					var fec_nac = data.fec_nac;
					if(fec_nac){
						var arrFec_nac = fec_nac.split('-');
						$('#alu_familiar-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
					}
					

					_inputs('alu_familiar-frm');
					$("#alu_familiar-frm [name=id_tap]").val([data.id_tap]);
					$("#alu_familiar-frm [name=id_gen]").val([data.id_gen]);
					
					if(data.viv==1){
						$("#alu_familiar-frm #div_defuncion").css('display','none');
					} else if(data.viv==0){
						$("#alu_familiar-frm #div_defuncion").css('display','block');
					}

					//SOLO LECTURA SI HAY DATOS CONFIRMADOS
					if (data.ini=="3"){//DATOS CONFIRMADOS
						$("[name=id_tap]").attr('disabled', true);
						$("[name=id_gen]").attr('disabled', true);
						$('#corr').attr('readonly', 'readonly');
						$('#nro_doc').attr('readonly', 'readonly');
					}
					
					//$("input[name='id_gen']").val(data.id_gen);
					_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'), data.id_tdc,null,function(){
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
						function(){
							$('#id_par').change();
						}
					});	
					_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'), data.id_eci);
					_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
					_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);

					
					_llenarCombo('cat_pais',$('#id_pais'),data.id_pais,null, function(){
						$('#id_pais').change();
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
					_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
					
					$('#id_dep').on('change',function(event){
						 if($('#id_dep').val()!=null){
							 _llenar_combo({
									url:'api/comboCache/provincias/' + $(this).val(),
									combo:$('#id_pro'),
								   	id:data.id_pro,
								   	funcion:function(){
										$('#id_pro').change();
									}
								}); 
						 }
					});
					

					$('#id_pro').on('change',function(event){
				 
						 	if ($(this).val()==null || $(this).val()==''){ 
						 		$('#id_dist').find('option').not(':first').remove();
						 	}
						 	else{
								_llenar_combo({
									url:'api/comboCache/distritos/' + $(this).val(),
									combo:$('#id_dist'),
								   	id:data.id_dist,
								   	funcion:function(){
								   		console.log('remove data');
								   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
								   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
								   	}
								});		 		
						 	}
						 
					});
					
					if(data.ocu==null){
						$("#id_tra_rem2").click();
					} else{
						$("#id_tra_rem1").click();
					} 
					
				/*	var map;
					var LONGITUD = -77.530033333333;
					var LATITUD = -9.5272222222222;
					var DIRECCION_DEPARTAMEN	= null;
					var DIRECCION_PROVINCIA		= null;
					var ZOOM = 16;
					
					//LLENAR FAMILIAS
					_get('api/familiar/familias/' + $('#_id_anio').text() + '/' + _usuario.id, function(data){
						var familias = data;
						
						var lista_familias = $('#familia-row');
						var linea = lista_familias.html();
						for ( var i=0; i<data.length-1;i++){
							 $('#familia-row').append(linea);
						}
						
						//llenar la data
						
					//	console.log(lista_familias);
						
						var i=0;
						$('.familia-div').each(function () {
							var familia = data[i];
							var padre_madre = obtener_padre_madre(familia.gruFamFamiliars);
							
							$(this).find('#btn-agregar').data('id_gpf',familia.gruFamFamiliars[0].id_gpf); 
							$(this).find('#btn-agregar').data('id_fam',familia.gruFamFamiliars[0].id_fam); 
							$(this).find('#btn-agregar').on('click', function(e){
								familiar_editarFull($(this),e);	
							});
							
							$(this).find('#titulo').html('FAMILIA ' + padre_madre);
							$(this).find('#familiar-tabla').dataTable({
								 data : familia.gruFamFamiliars,
								 aaSorting: [],
								 destroy: true,
								 orderCellsTop:true,
								 select: true,
								 bLengthChange: false,
								 bPaginate: false,
								 bFilter: false,
							     bInfo : false,
						         columns : [ 
						        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						        	 	{"title":"Familiar",  "render":function ( data, type, row,meta ) { return row.familiar.ape_pat + ' ' + row.familiar.ape_mat +', ' + row.familiar.nom}},
						        	 	{"title":"Nro Doc", "data": "familiar.nro_doc"},
						        	 	{"title":"Parentesco", "data": "familiar.parentesco.par"},
										{"title":"Acciones", "render": function ( data, type, row ) {
						                   return '<ul class="icons-list">'+
												'<li class="dropdown">'+
											'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
												'<i class="icon-menu9"></i>'+
												'</a>'+
											'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + row.id + '" onclick="familiar_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
												'<li><a href="#" data-id="' + row.id + '" onclick="familiar_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
											'</ul>'+
						                   '</li>'+
						                   '</ul>';}
										}
							    ]
						    });
							
							i++;
						});
						
					});
					
				  	map = $("#location").width("100%").height("800px").gmap3({
			    	    map:{
			    	        options:{
			    	        	center:[LATITUD,LONGITUD ],
			    	            zoom: ZOOM,
			    	            mapTypeId: google.maps.MapTypeId.ROADMAP,
			    	            mapTypeControl: true,
			    	            mapTypeControlOptions:  {   style: google.maps.MapTypeControlStyle.DROPDOWN_MENU    },
			    	            navigationControl: true,
			    	            scrollwheel: true,
			    	            streetViewControl: false
			    	        },
			    	        events:{                    
			    	            click: function(map, event) // clicking on map adds a new marker
			    	            {
			    	                var lat = event.latLng.lat(), 
			    	                    lng = event.latLng.lng();
			    	                var zoom1 = map.getZoom();
			    	                
			    	               // $("#direccion-form #longitud").val(lng);
			    	            	//$("#direccion-form #latitud").val(lat);
			    	            	//$("#direccion-form #zoom").val(zoom1);
			    	            	
			    	                console.log(lat);
			    	                // save to form
			    	                $("#map_lat").val(lat);
			    	                $("#map_long").val(lng);
			    	                $("#map_zoom").val( zoom1 );

			    	                $(this).gmap3({
			    	                    clear: { id: ["newTag"] }, // remove old/new tags (based on marker id value)
			    	                    marker: { id: 'newTag', latLng: event.latLng } // add new marker on map
			    	                });
			    	            } // end click
			    	        } // end event    	        
			    	    },
			    	        marker:{
			    	        	values:[
			    	        		{latLng:[LATITUD,LONGITUD ], id:["newTag"]},
			    	        		{options:{icon: "http://maps.google.com/mapfiles/marker_green.png"}}
			    	        		]
			    	        	}
			    	        
			    	});*/

					
					
				//}});
		} else{
			//alert(2);
		/*	_get('api/familiar/obtenerDatosFamiliar/' + null + "/" + _dni,
					function(data){*/
			var param ={id_fam:null, nro_doc:_dni};
			$('.daterange-single').daterangepicker({ 
			    singleDatePicker: true,
			    autoUpdateInput: false,
			    locale: { format: 'DD/MM/YYYY'}
			});
			
			
			
			_GET({ url: 'api/familiar/obtenerDatosFamiliar/',
					context: _URL,
				   params:param,
				   success:
					function(data){
					   console.log(data);
					if(data!=null){
						console.log(data);
						_fillForm(data,$('#alu_familiar-frm'));
						
						$("#foto").attr('src', 'data:image/png;base64,' + data.foto);
						
						var fec_nac = data.fec_nac;
						if(fec_nac){
							var arrFec_nac = fec_nac.split('-');
							$('#alu_familiar-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
						}
						

						_inputs('alu_familiar-frm');
						$("#alu_familiar-frm [name=id_tap]").val([data.id_tap]);
						$("#alu_familiar-frm [name=id_gen]").val([data.id_gen]);

						//SOLO LECTURA SI HAY DATOS CONFIRMADOS
						if (data.ini=="3"){//DATOS CONFIRMADOS
							$("[name=id_tap]").attr('disabled', true);
							$("[name=id_gen]").attr('disabled', true);
							$('#corr').attr('readonly', 'readonly');
							$('#nro_doc').attr('readonly', 'readonly');
						}
						
						//$("input[name='id_gen']").val(data.id_gen);
						_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'), data.id_tdc,null,function(){
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
							function(){
								$('#id_par').change();
							}
						});	
						_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'), data.id_eci);
						_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
						_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);

						
						_llenarCombo('cat_pais',$('#id_pais'),data.id_pais,null, function(){
							$('#id_pais').change();
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
						_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
						
						$('#id_dep').on('change',function(event){
							 if($('#id_dep').val()!=null){
								 _llenar_combo({
										url:'api/comboCache/provincias/' + $(this).val(),
										combo:$('#id_pro'),
									   	id:data.id_pro,
									   	funcion:function(){
											$('#id_pro').change();
										}
									}); 
							 }
						});
						

						$('#id_pro').on('change',function(event){
					 
							 	if ($(this).val()==null || $(this).val()==''){ 
							 		$('#id_dist').find('option').not(':first').remove();
							 	}
							 	else{
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist'),
									   	id:data.id_dist,
									   	funcion:function(){
									   		console.log('remove data');
									   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
									   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
									   	}
									});		 		
							 	}
							 
						});
						
						if(data.ocu==null){
							$("#id_tra_rem2").click();
						} else{
							$("#id_tra_rem1").click();
						} 
					} else{

						//"initComplete": function( settings ) {
							//   _initCompleteDT(settings);
								

								//$('yourinput').daterangepicker({ autoUpdateInput: false }, function(chosen_date) { $('yourinput').val(chosen_date.format('YYYY-MM-DD')); });
						// }
						$('#alu_familiar-frm input:radio[name="id_tap"][value="A"]').click();
						_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'),1,null,function(){
							$('#id_tdc').change()});
						$("#alu_familiar-frm #nro_doc").val(_dni);
						$('#viv').change();
						$('#alu_familiar-frm #id_par').on('change',function(event){
							if($('#id_par').val()==_PARENTESCO_PAPA){
								$('#id_gen1').click();
							} else if($('#id_par').val()==_PARENTESCO_MAMA){
								$('#id_gen2').click();
							}
							
						});
						//_llenarCombo('cat_parentesco',$('#id_par'));
						
						_llenar_combo({
							url:'api/familiar/llenarOpcionesMamayPapa',
							combo:$('#id_par'),
							text:'par',
							context: _URL,
							funcion(){
								$('#alu_familiar-frm #id_par').change();
							}
						});	
						
						_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'));
						_llenarCombo('cat_grado_instruccion',$('#id_gin'));
						_llenarCombo('cat_religion',$('#id_rel'));
						_llenarCombo('cat_pais',$('#id_pais'),null,null, function(){
							$('#id_pais').change();
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
									   	id:3,
									   	funcion:function(){
											$('#id_dep').change();
										//	alert();
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
						
						_llenarCombo('cat_ocupacion',$('#id_ocu'));
						
						$('#id_dep').on('change',function(event){
							 if( $(this).val()!=null){
								 _llenar_combo({
									 url:'api/comboCache/provincias/' + $(this).val(),
									 combo:$('#id_pro'),
									 //	id:$(this).data('id_pro'),
									 funcion:function(){
										 $('#id_pro').change();
									 }
								 });
							}

						});
						

						$('#id_pro').on('change',function(event){
					 
							 	if ($(this).val()==null || $(this).val()==''){ 
							 		$('#id_dist').find('option').not(':first').remove();
							 	}
							 	else{
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist'),
									  // 	id:$(this).data('id_dis'),
									   	funcion:function(){
									   		console.log('remove data');
									   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
									   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
									   	}
									});		 		
							 	}
							 
						});
					}
					
			}});
			
		}
		
		$('#viv').on('change',function(event){
			 
			if($("#alu_familiar-frm #viv").val()==1){
				$("#alu_familiar-frm #div_defuncion").css('display','none');
			} else if($("#alu_familiar-frm #viv").val()==0){
				$("#alu_familiar-frm #div_defuncion").css('display','block');
			}			 
		});
		
		$('#id_par').on('change',function(event){
			if($('#id_par').val()==_PARENTESCO_PAPA){
				$('#id_gen1').click();
			} else if($('#id_par').val()==_PARENTESCO_MAMA){
				$('#id_gen2').click();
			}
			
		});
	 
		$("input:radio[name='id_tap']").on("click", function(e) {
			var valor = $(this).val();
			$('#alu_familiar-frm #ape_pat').removeAttr('required');
			$('#alu_familiar-frm #ape_mat').removeAttr('required');

			if (valor == "A") {
				$('#alu_familiar-frm #ape_pat').attr("required", "required");
				$('#alu_familiar-frm #ape_mat').attr("required", "required");
				$('#alu_familiar-frm #id_tap1').attr("checked", "checked");
				$('#alu_familiar-frm #id_tap2').removeAttr("checked");
				$('#alu_familiar-frm #id_tap3').removeAttr("checked");
				$('#alu_familiar-frm #ape_mat').removeAttr("disabled");
				$('#alu_familiar-frm #ape_pat').removeAttr("disabled");
			}
			if (valor == "P") {
				$('#alu_familiar-frm #ape_pat').attr("required", "required");
				$('#alu_familiar-frm #ape_mat').attr("disabled", "disabled");
				$('#alu_familiar-frm #ape_pat').removeAttr("readonly");
				$('#alu_familiar-frm #id_tap2').attr("checked", "checked");
				$('#alu_familiar-frm #id_tap1').removeAttr("checked");
				$('#alu_familiar-frm #id_tap3').removeAttr("checked");
			}
			if (valor == "M") {
				$('#alu_familiar-frm #ape_mat').attr("required", "required");
				$('#alu_familiar-frm #ape_pat').attr("disabled", "disabled");
				$('#alu_familiar-frm #ape_mat').removeAttr("disabled");
				$('#alu_familiar-frm #id_tap3').attr("checked", "checked");
				$('#alu_familiar-frm #id_tap1').removeAttr("checked");
				$('#alu_familiar-frm #id_tap2').removeAttr("checked");
			}
			//$('#familiar-form').parsley();

		});
		
		$("input:radio[name='id_tra_rem']").on("click", function(e) {
			var valor = $(this).val();
			if(valor==0){
				$("#div_trabajo").css('display','none');
			} else 	if(valor==1){
				$("#div_trabajo").css('display','block');
			}
		});
		
		$('#viv').on('change',function(event){
			if($(this).val()==0)
				$('#alu_familiar-frm #fec_def').removeAttr("disabled");
			else
				$('#alu_familiar-frm #fec_def').attr("disabled", "disabled");
		});
		
		
}

function cargarDatos(){
	var id_pais = null;
	if(_primeraCarga)
		id_pais = _data.id_pais;//solo en la primera carga se jala desde la data, deacuerdo? si
	_llenarCombo('cat_pais',$('#id_pais'),id_pais,null, function(){
		$('#id_pais').change();
	});
	
	$('#id_pais').on('change',function(event){
		 if($('#id_pais').val()=="193"){
				var id_dep = null;
				if(_primeraCarga){
					id_dep = _data.id_dep;//solo en la primera carga se jala desde la data, deacuerdo? si
				}
			 $('#id_dep').removeAttr("disabled");
			 $('#id_pro').removeAttr("disabled");
			 $('#id_dist').removeAttr("disabled");
			 $('#dir').removeAttr("readonly");
			 $('#ref').removeAttr("readonly");
			 _llenar_combo({
					url:'api/comboCache/departamentos/' + $(this).val(),
					combo:$('#id_dep'),
				   	id: id_dep,
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
	
	$('#id_dep').on('change',function(event){
		var id_pro = null;
		if(_primeraCarga);
			id_pro = _data.id_pro;//solo en la primera carga se jala desde la data, deacuerdo? si
		console.log('onchange departamento (id_pro):' + id_pro);
		
		 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){
			 _llenar_combo({
					url:'api/comboCache/provincias/' + $('#id_dep').val(),
					combo:$('#id_pro'),
				   	id:id_pro,//aca es el tema.. la primera vez deberia usar el data.id_pro y cuando el usuario edita.. ya no deberia usar data.id_pro
				   					//cierto? claro, podemos manejar un flag
				   	funcion:function(){
						$('#id_pro').change();
					}
				}); 
		 }
	});
	
	//los combos dep, pro, dis
	$('#id_pro').on('change',function(event){
		 console.log('onchange provincia');
		 var id_dist = null 
		 if(_primeraCarga)
			 id_dist = _data.id_dist;
		 console.log('onchange provincia:' + id_dist);

		 _llenar_combo({
				url:'api/comboCache/distritos/' + $(this).val(),
				combo:$('#id_dist'),
			   	id:id_dist,// aca le paso el valor del id para q aparezca selecconado, siempre lo haciamos asi----estas??								   	
			   	funcion:function(){
			   		$('#id_dist').change();
			   		//aca finalizamos la primera carga
			   		_primeraCarga = false;
			   	//	$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
			   	//	$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
			   	}
			});		 
		 //	}
	
	});
}

$(function(){
	_onloadPage();

});

function editarFamiliar(id_gpf, id_fam){
	var params ={id_gpf:id_gpf, id_fam:id_fam};
	$("#frm_datos_familiar").css('display','block');
	$("#alu_familiar-frm #btn-grabar").css('display','block');
	//_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);
	listar_padres(id_gpf);
	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + id_gpf + "&id_alu=" + _id_alu,
			function(data){
			
			$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
				mostrarDatosTabAlumno(data);
				$('#alu_alumno-frm #id_gpf').val(id_gpf);
			});
			
	});
	$('#alu_familiar-frm #nro_doc').attr('readonly', 'readonly');
	$('#alu_familiar-frm #ape_pat').attr('readonly', 'readonly');
	$('#alu_familiar-frm #ape_mat').attr('readonly', 'readonly');
	$('#alu_familiar-frm #nom').attr('readonly', 'readonly');
	$('#alu_familiar-frm #id_par').attr('disabled', 'disabled');
	$('#alu_familiar-frm #id_tdc').attr('disabled', 'disabled');
	//$('#alu_familiar-frm #id_tap').attr('disabled', 'disabled');
	//$(".id_tap").prop("disabled", true); 
	$('#alu_familiar-frm input[name=id_tap]').attr("disabled",true);
	$('#alu_familiar-frm #id_gpf').val(id_gpf);
	//$("#alu_familiar-frm").css('display','none');
	if($("#alu_familiar-frm #viv").val()==1){
		$("#alu_familiar-frm #div_defuncion").css('display','none');
	} else if($("#alu_familiar-frm #viv").val()==0){
		$("#alu_familiar-frm #div_defuncion").css('display','block');
	}
				
	
/*	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + _id_gpf + "&id_alu=" + _id_alu,
			function(data){*/
			
			$("#fam_otros_familiares-tab").load("pages/alumno/alu_familiar_otro_fam.html", {}, function(responseText, textStatus, req){
				mostrarDatosTabOtroFam(id_gpf);
				//"OtroFamiliarEditar?id_fam=" + id + "&id_gpf=" + $("#_id_gpf").val(),
				//$('#alu_alumno-frm #id_gpf').val(_id_gpf);
			});
			
	//});
	/*_get('api/familiar/obtenerDatosFamiliar/' + _id_fam+'/'+null,
			function(data){*/	
		var param ={id_fam:id_fam, nro_doc:null};
		_GET({ url: 'api/familiar/obtenerDatosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){		
				_data=data;
				console.log(data);
				_fillForm(data,$('#alu_familiar-frm'));
				
				$("#foto").attr('src', 'data:image/png;base64,' + data.foto);

				
				var fec_nac = data.fec_nac;
				if(fec_nac){
					var arrFec_nac = fec_nac.split('-');
					$('#alu_familiar-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
				}
				

				_inputs('alu_familiar-frm');
				$("#alu_familiar-frm [name=id_tap]").val([data.id_tap]);
				$("#alu_familiar-frm [name=id_gen]").val([data.id_gen]);

				//SOLO LECTURA SI HAY DATOS CONFIRMADOS
				if (data.ini=="3"){//DATOS CONFIRMADOS
					$("[name=id_tap]").attr('disabled', true);
					$("[name=id_gen]").attr('disabled', true);
					$('#corr').attr('readonly', 'readonly');
					$('#nro_doc').attr('readonly', 'readonly');
				}
				
				//$("input[name='id_gen']").val(data.id_gen);
				_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'), data.id_tdc,null,function(){
					$('#id_tdc').change();
				});
				
				_llenar_combo({
					url:'api/familiar/llenarOpcionesMamayPapa',
					combo:$('#id_par'),
					text:'par',
					context: _URL,
					id:data.id_par,
					function(){
						$('#id_par').change();
					}
				});	
				_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'), data.id_eci);
				_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
				_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);

				if(id_fam!=null){
					//mira aqui es el editar
					_primeraCarga=true;
					cargarDatos();
					
				} else{
					_primeraCarga=false;
					cargarDatos();
				}
				
				_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
				
				
				
				if(data.ocu==null || data.ocu==''){
					$("#id_tra_rem2").click();
				} else{
					$("#id_tra_rem1").click();
				} 

			}});
}


//se ejecuta siempre despues de cargar el html

function obtener_padre_madre(gruFamFamiliars){
	
	console.log('gruFamFamiliars',gruFamFamiliars);

	var padre = "";
	var madre = "";
	for ( var i=0; i<gruFamFamiliars.length;i++){
		if (gruFamFamiliars[i].familiar.id_par == _PARENTESCO_PADRE){
			padre = gruFamFamiliars[i].familiar.ape_pat;
		}
		if (gruFamFamiliars[i].familiar.id_par == _PARENTESCO_MADRE){
			 madre = gruFamFamiliars[i].familiar.ape_pat;
		}
	}
	
	if (padre!="" && madre!="")
		return padre + " - " + madre;
	else if (padre!="")
		return padre;
	else
		return madre;
}

// Map settings
function initialize() {
	var map;
	// Optinos
	var mapOptions = {
		zoom: 12,
		center: new google.maps.LatLng(47.496, 19.037)
	};

	// Apply options
	map = new google.maps.Map($('.map-basic')[0], mapOptions);
}


/***
 * Tab nro 3 - Alumnos
 * @param data
 * @returns
 */
function mostrarDatosTabAlumno(data) {
	var alumno = data.alumno; 
	console.log(data);
	
	/**
	 * DATOS DEL ALUMNO (FORMULARIO)
	 */
	$("#alu_alumno-frm #ape_pat").attr("readonly", "readonly");
	$("#alu_alumno-frm #ape_mat").attr("readonly", "readonly");
	if(alumno.id!=null){
		$("#alu_alumno-frm #nom").attr("readonly", "readonly");
		$("#alu_alumno-frm #nro_doc").attr("readonly", "readonly");
		$('#alu_alumno-frm input[name=id_tap]').attr("disabled",true);
		$('#id_pro_nac').on('change',function(event){

		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist_nac').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_nac'),
				   	id:alumno.id_dist_nac,
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
					   	id:alumno.provincia_nac.id,
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
					   	id: alumno.departamento_nac.id,
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
			console.log(alumno.id_pais_nac);
		_llenarCombo('cat_pais',$('#id_pais_nac'),alumno.id_pais_nac,null, function(){
			$('#id_pais_nac').change();
		});	
		
		$('#id_pro_viv').on('change',function(event){

			 	/*if ($(this).val()==null || $(this).val()==''){ 
			 		$('#id_dist_viv').find('option').not(':first').remove();
			 	}
			 	else{*/
			 if($('#id_dep_viv').val()!=null){
					_llenar_combo({
						url:'api/comboCache/distritos/' + $(this).val(),
						combo:$('#id_dist_viv'),
					   	id:alumno.id_dist_viv,
					   	funcion:function(){
					   		console.log('remove data');
					   		$('#id_dep_viv').removeData('id_pro_viv');//eliminar campos auxiliares
					   		$('#id_pro_viv').removeData('id_dist_viv');//eliminar campos auxiliares
					   	}
					});		
			 }	
			 	//}
			 
		});
		
		$('#id_dep_viv').on('change',function(event){
			 if($('#id_dep_viv').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_viv'),
					   	id:alumno.provincia_viv.id,
					   	funcion:function(){
							$('#id_pro_viv').change();
						}
					}); 
			 }
		});
		
		 _llenar_combo({
				url:'api/comboCache/departamentos/' + 193,
				combo:$('#id_dep_viv'),
			   	id: alumno.departamento_viv.id,
			   	funcion:function(){
					$('#id_dep_viv').change();
				}
		});	
	} else{
		//alert('nuevoaqui');
		$('#id_pro_nac').on('change',function(event){

		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist_nac').find('option').not(':first').remove();
		 	} else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_nac'),
				   //	id:alumno.id_dist_nac,
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
					   	//id:alumno.provincia_nac.id,
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
					   //	id: alumno.departamento_nac.id,
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
			console.log(alumno.id_pais_nac);
		_llenarCombo('cat_pais',$('#id_pais_nac'),alumno.id_pais_nac,null, function(){
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
					   //	id:alumno.id_dist_viv,
					   	funcion:function(){
					   		//$('#id_dist_viv').change();
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
					   //	id:alumno.provincia_viv.id,
					   	funcion:function(){
							$('#id_pro_viv').change();
						}
					}); 
			 }
		});
		
		 _llenar_combo({
				url:'api/comboCache/departamentos/' + 193,
				combo:$('#id_dep_viv'),
			  // 	id: alumno.departamento_viv.id,
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
	
	
	_fillForm(alumno,$('#alu_alumno-frm'));
	_llenarCombo('cat_tipo_documento',$('#alu_alumno-frm #id_tdc'), alumno.id_tdc,null, function(){
		$('#alu_alumno-frm #id_tdc').change();
	});
	
	/*_llenarCombo('cat_pais',$('#id_pais_nac'),null,null, function(){
		$('#id_pais_nac').change();
	});	*/		
	
	listar_alumnos(data.id_gpf);
	
	
	$("#alu_alumno-frm [name=id_tap]").val([alumno.id_tap]);
	$("#alu_alumno-frm [name=id_gen]").val([alumno.id_gen]);
	_llenarCombo('cat_est_civil',$('#alu_alumno-frm #id_eci'), alumno.id_eci);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio1'), alumno.id_idio1);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio2'), alumno.id_idio2);

	var fec_nac = alumno.fec_nac;
	if(fec_nac){
		var arrFec_nac = fec_nac.split('-');
		$('#alu_alumno-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
	}

	_inputs('alu_alumno-frm');
	
	/**
	 * ALUMNOS REGISTRADOS
	 */
 
	console.log(data.alumnoList);
 
	/*$('#alumno-tabla').dataTable({
		 data : data.alumnoList,
		 aaSorting: [],
		 destroy: true,
		 orderCellsTop:true,
		 select: true,
		 bLengthChange: false,
		 bPaginate: false,
		 bFilter: false,
	     bInfo : false,
         columns : [ 
        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
        	 	{"title":"Codigo", "data": "alumno.cod"},
        	 	{"title":"Alumno", "data": "alumno","render": function ( data, type, row ) {
        	 		 return data.ape_pat + " " + data.ape_mat + ", " + data.nom;
        	 	}},
        	 	{"title":"Tip. Doc.", "data": "alumno.tipoDocumento.nom"},
        	 	{"title":"Nro doc.", "data": "alumno.nro_doc"},
        	 	{"title":"Actualizar datos", "render": function ( data, type, row ) {
					return '<button onclick="editarFamiliar(' + row.id +',' + row.id_fam +')" type="button" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-edit"></i> Editar</button>';}
				}
	    ]
	});*/
 
}

/*function obtenerDireccionPadre() {
	var id_gpf=$("#alu_familiar-frm #id_gpf").val();
	var param={id_gpf:id_gpf, id_par:2};
	_GET({ url: 'api/familiar/obtenerDatosDomiciliariosFamiliar/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   console.log(data);
			   $("#alu_alumno-frm #id_dist_viv").val(data.id_dist).change();
			   $("#alu_alumno-frm #id_pro_viv").val(data.id_pro).change();
			   $("#alu_alumno-frm #id_dep_viv").val(data.id_dep).change();
			   $("#alu_alumno-frm #direccion").val(data.dir);
			   $("#alu_alumno-frm #ref").val(data.ref);
			   $("#alu_alumno-frm #telf").val(data.tlf);
			}
		});
}*/

function obtenerDireccionPadre(){
	var id_gpf=$("#alu_familiar-frm #id_gpf").val();
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

function obtenerDireccionMadre() {
	var id_gpf=$("#alu_familiar-frm #id_gpf").val();
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
				//alert('id_dep'+id_dep);
					 _llenar_combo({
							url:'api/comboCache/departamentos/' + 193,
							combo:$('#id_dep_viv'),
						   	id: id_dep,
						   	funcion:function(){
								$('#id_dep_viv').change();
							}
						});
				 
				$('#id_dep_viv').on('change',function(event){
					var id_pro = null;
					if(_primeraCarga)
						id_pro = data.id_pro;//solo en la primera carga se jala desde la data, deacuerdo? si
					// alert('id_pro'+id_pro);
					console.log('onchange departamento (id_pro):' + id_pro);
					
					 if($('#id_dep_viv').val()!=null && $('#id_dep_viv').val()!=''){
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
					// alert('id_dist'+id_dist);
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
		

function mostrarDatosTabOtroFam(id_gpf){
	 listar_otros_familiares(id_gpf);
	$('#alu_otro_fam-frm input[name=id_tap]').attr("disabled",false);
	$('#alu_otro_fam-frm input:radio[name="id_tap"][value="A"]').click();
	$("#alu_otro_fam-frm #id_gpf").val(id_gpf);
	$("#alu_otro_fam-frm #id_tdc").on('change', function(){
		if($(this).val()==1){
			 $("#alu_otro_fam-frm #nro_doc").attr('maxlength','8');
		}else if($(this).val()==2){
			 $("#alu_otro_fam-frm #nro_doc").attr('maxlength','11');
		}
	});
	
	_llenarCombo('cat_tipo_documento',$('#alu_otro_fam-frm #id_tdc'),1,null, function(){
		$('#alu_otro_fam-frm #id_tdc').change();
	});
	
	_llenar_combo({
		url:'api/familiar/llenarOpcionesOtros',
		combo:$('#alu_otro_fam-frm  #id_par'),
		text:'par',
		context: _URL
	});	
	
	$("input:radio[name='id_tap']").on("click", function(e) {
		var valor = $(this).val();
		$('#alu_otro_fam-frm #ape_pat').removeAttr('required');
		$('#alu_otro_fam-frm #ape_mat').removeAttr('required');

		if (valor == "A") {
			$('#alu_otro_fam-frm #ape_pat').attr("required", "required");
			$('#alu_otro_fam-frm #ape_mat').attr("required", "required");
			$('#alu_otro_fam-frm #id_tap1').attr("checked", "checked");
			$('#alu_otro_fam-frm #id_tap2').removeAttr("checked");
			$('#alu_otro_fam-frm #id_tap3').removeAttr("checked");
			$('#alu_otro_fam-frm #ape_mat').removeAttr("disabled");
			$('#alu_otro_fam-frm #ape_pat').removeAttr("disabled");
		}
		if (valor == "P") {
			$('#alu_otro_fam-frm #ape_pat').attr("required", "required");
			$('#alu_otro_fam-frm #ape_mat').attr("disabled", "disabled");
			$('#alu_otro_fam-frm #ape_pat').removeAttr("readonly");
			$('#alu_otro_fam-frm #id_tap2').attr("checked", "checked");
			$('#alu_otro_fam-frm #id_tap1').removeAttr("checked");
			$('#alu_otro_fam-frm #id_tap3').removeAttr("checked");
		}
		if (valor == "M") {
			$('#alu_otro_fam-frm #ape_mat').attr("required", "required");
			$('#alu_otro_fam-frm #ape_pat').attr("disabled", "disabled");
			$('#alu_otro_fam-frm #ape_mat').removeAttr("disabled");
			$('#alu_otro_fam-frm #id_tap3').attr("checked", "checked");
			$('#alu_otro_fam-frm #id_tap1').removeAttr("checked");
			$('#alu_otro_fam-frm #id_tap2').removeAttr("checked");
		}
		//$('#familiar-form').parsley();

	});
	
	_llenarCombo('cat_pais',$('#alu_otro_fam-frm #id_pais'),null,null, function(){
		$('#alu_otro_fam-frm #id_pais').change();
	});			
	
	$('#alu_otro_fam-frm #id_pais').on('change',function(event){
		
		 if($('#alu_otro_fam-frm #id_pais').val()=="193"){
			 $('#alu_otro_fam-frm #id_dep').removeAttr("disabled");
			 $('#alu_otro_fam-frm #id_pro').removeAttr("disabled");
			 $('#alu_otro_fam-frm #id_dist').removeAttr("disabled");
			 $('#alu_otro_fam-frm #dir').removeAttr("readonly");
			 $('#alu_otro_fam-frm #ref').removeAttr("readonly");
			 _llenar_combo({
					url:'api/comboCache/departamentos/' + $(this).val(),
					combo:$('#alu_otro_fam-frm #id_dep'),
				   //	id:193,
				   	funcion:function(){
						$('#alu_otro_fam-frm #id_dep').change();
					}
				});
		 } else {
			 $('#alu_otro_fam-frm #id_dep').val('');
			 $('#alu_otro_fam-frm #id_pro').val('');
			 $('#alu_otro_fam-frm #id_dist').val('');
			 $('#alu_otro_fam-frm #dir').val('');
			 $('#alu_otro_fam-frm #ref').val('');
			 $('#alu_otro_fam-frm #id_dep').attr("disabled", "disabled");
			 $('#alu_otro_fam-frm #id_pro').attr("disabled", "disabled");
			 $('#alu_otro_fam-frm #id_dist').attr("disabled", "disabled");
			 $('#alu_otro_fam-frm #dir').attr("readonly", "readonly");
			 $('#alu_otro_fam-frm #ref').attr("readonly", "readonly");
			 $('#alu_otro_fam-frm #id_dep').change();
		 }
		
	});
		
	$('#alu_otro_fam-frm #id_dep').on('change',function(event){
		 if( $(this).val()!=null){
			 _llenar_combo({
				 url:'api/comboCache/provincias/' + $(this).val(),
				 combo:$('#alu_otro_fam-frm #id_pro'),
				 //	id:$(this).data('id_pro'),
				 funcion:function(){
					 $('#alu_otro_fam-frm #id_pro').change();
				 }
			 });
		}

	});
	

	$('#alu_otro_fam-frm #id_pro').on('change',function(event){
 
		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#alu_otro_fam-frm #id_dist').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#alu_otro_fam-frm #id_dist'),
				  // 	id:$(this).data('id_dis'),
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#alu_otro_fam-frm #id_dep').removeData('id_pro');//eliminar campos auxiliares
				   		$('#alu_otro_fam-frm #id_pro').removeData('id_dis');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
	});
}

function listar_padres(id_gpf){
	var param={id_gpf:id_gpf};
	_GET({ url: 'api/familiar/listarPadres/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   _cant_padres=data.length;
			console.log(data);
			var cant_padres=data.length;
			if(cant_padres>=2){
				$("#fam_datos-tab #btn-nuevo").css('display','none');
			} else{
				$("#fam_datos-tab #btn-nuevo").css('display','block');
			}
				$('#familiar_table').dataTable({
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
							/*{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="editarFamiliar(' + row.id_gpf +',' + row.id+')" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'
							}}*/
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="editarFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i> Editar</button>';
							}}
							/*{"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="editarFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-edit"></i> Editar</button>';
								'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_det_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
							}}*/
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});
}

$('#alu_otro_fam-frm #btn_grabarOtroFam').on('click',function(event){
	var validator = $("#alu_otro_fam-frm").validate();
	 $('#alu_otro_fam-frm #btn-grabar').attr("disabled", "disabled");
	 $('#alu_familiar-frm #id_par').removeAttr('disabled');
	 $('#alu_familiar-frm #id_tdc').removeAttr('disabled');
	event.preventDefault();
	//if(($('#alu_otro_fam-frm #nro_doc').val().length==8 && $('#alu_otro_fam-frm #id_tdc').val()==1 ) || ($('#alu_otro_fam-frm #nro_doc').val().length==11 && $('#alu_otro_fam-frm #id_tdc').val()==2)){
	event.preventDefault();
	if($('#alu_otro_fam-frm #nro_doc').val().length<8 && $('#alu_otro_fam-frm #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_otro_fam-frm #nro_doc").focus();
	} else if($('#alu_otro_fam-frm #nro_doc').val().length<11 && $('#alu_otro_fam-frm #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#alu_otro_fam-frm #nro_doc").focus();
	} else{
		$('#alu_otro_fam-frm input[name=id_tap]').attr("disabled",false);
		if ($("#alu_otro_fam-frm").valid()){
			if(!$("#alu_otro_fam-frm input[name='id_gen']").is(':checked')){
				alert("Seleccione el género, porfavor!!");			
			} /*else if(!$("#alu_otro_fam-frm input[name='ped_inf']").is(':checked')){
				alert("Seleccione si el familiar pedirá información!!");	
			} */else if(!$("#alu_otro_fam-frm input[name='rec_lib']").is(':checked')){
				alert("Seleccione si el familiar recojerá libretas!!");
			} else { 
					_POST({form:$('#alu_otro_fam-frm'),
						  context:_URL,
						  msg_type:'notification',
						  msg_exito : 'SE REGISTRO AL FAMILIAR CORRECTAMENTE.',
						  success:function(data){
							    //DESCARGAR 
							  //id_gpf=2490, id_per=null, id=5010, error=null
							  $("#alu_otro_fam-frm #id").val(data.id);
							  $("#alu_otro_fam-frm #id_gpf").val(data.id_gpf);
							  $('#alu_otro_fam-frm #id_gpf').val(data.id_gpf);
							  $('#alu_otro_fam-frm #btn-grabar').removeAttr('disabled');
							  listar_otros_familiares(data.id_gpf); 
							  $('#alu_familiar-frm #id_par').attr('disbled', 'disabled');
								$('#alu_familiar-frm #id_tdc').attr('disbled', 'disabled');
						 }, 
						 error:function(data){
							 _alert_error(data.msg);
							 $('#alu_otro_fam-frm #btn-grabar').removeAttr('disabled');
							 $('#nro_dni').focus();
						}
					}); 
					
			}
			
		}
	}
	/*} else{
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_otro_fam-frm #nro_doc").focus();
	}*/
});

$('#alu_alumno-frm').on('submit',function(event){
	var validator = $("#alu_alumno-frm").validate();
	 $('#alu_alumno-frm #btn-grabar').attr("disabled", "disabled");
	event.preventDefault();
	if($('#alu_alumno-frm #nro_doc').val().length<8 && $('#alu_alumno-frm #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
		$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
	} else if($('#alu_alumno-frm #nro_doc').val().length<11 && $('#alu_alumno-frm #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
		$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
	} else{
		$('#alu_alumno-frm input[name=id_tap]').attr("disabled",false);
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
		}
	}
	
	/*} else{
		alert('El DNI debe de tener 8 dígitos!!');
		$("#alu_alumno-frm #nro_doc").focus();
	}*/
});



function listar_alumnos(id_gpf){
	var param={id_gpf:id_gpf};
	_GET({ url: 'api/alumno/listarAlumnos/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			 //  _cant_padres=data.length;
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
							{"title":"Apellidos y Nombres", "data" : "alumno"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="editarAlumno(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i> Editar</button>';
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});
}

function editarAlumno(id_gpf, id_alu){
	/*var params ={id_gpf:id_gpf, id_fam:id_fam};
	
	_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);*/
	
	_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + id_gpf + "&id_alu=" + id_alu,
			function(data){
		$("#div_alumno").css('display','block');
			//$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
				mostrarDatosTabAlumno(data);
			//});
			
	});
}

function listar_otros_familiares(id_gpf){
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
}

function editarOtroFamiliar(id_gpf, id_fam){
	/*var params ={id_gpf:id_gpf, id_fam:id_fam};
	
	_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);*/
	$("#div_otro_familiar").css('display','block');
	_get('api/familiar/OtroFamiliarEditar?id_gpf=' + id_gpf + "&id_fam=" + id_fam,
			function(data){
			console.log(data);
			_fillForm(data.otroFamiliar,$('#alu_otro_fam-frm'));
			//$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
			//mostrarDatosTabOtroFam(data.id_gpf);
			//});
			
	});
}

function eliminarOtroFamiliar(id_gpf, id_fam){
	_DELETE({url:'api/familiar/eliminarOtroFamiliar/' + id_gpf+'/'+ id_fam,
		context:_URL,
		success:function(){
			listar_otros_familiares(id_gpf);
			}
		});
}

function familiarNuevo() {

	$("#frm_datos_familiar").css('display','block');
	$("#alu_familiar-frm #btn-grabar").css('display','block');
	$("#alu_familiar-frm #nro_doc").removeAttr("readonly");
	$('#alu_familiar-frm input[name=id_tap]').attr("disabled",false);
	$("#alu_familiar-frm #nro_doc").focus();
	var _id_par=null;
	if(_madre_existe){
		_id_par=2;
	} else if(_padre_existe){
		_id_par=1;
	}	
	
	_llenar_combo({
		url:'api/familiar/llenarOpcionesMamayPapa',
		combo:$('#id_par'),
		id: _id_par,
		text:'par',
		context: _URL,
		funcion(){
			$('#alu_familiar-frm #id_par').change();
		}
	});	
	
	var id_par = $("#alu_familiar-frm #id_par").val();
	var id_gen = $("#alu_familiar-frm #id_gen").val();
	$('#alu_familiar-frm input:radio[name="id_tap"][value="A"]').click();
	_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'),1,null,function(){
		$('#id_tdc').change()});
	$("#alu_familiar-frm #nro_doc").val(_dni);
	$('#viv').change();
	$('#alu_familiar-frm #id_par').on('change',function(event){
		if($('#id_par').val()==_PARENTESCO_PAPA){
			$('#id_gen1').click();
		} else if($('#id_par').val()==_PARENTESCO_MAMA){
			$('#id_gen2').click();
		}
		
	});
	
	
	
	
	_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'));
	_llenarCombo('cat_grado_instruccion',$('#id_gin'));
	_llenarCombo('cat_religion',$('#id_rel'));
	
	_llenarCombo('cat_pais',$('#id_pais'),null,null, function(){
		$('#id_pais').change();
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
				   	id:3,
				   	funcion:function(){
						$('#id_dep').change();
					//	alert();
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
	
	_llenarCombo('cat_ocupacion',$('#id_ocu'));
	
	$('#id_dep').on('change',function(event){
		 if( $(this).val()!=null){
			 _llenar_combo({
				 url:'api/comboCache/provincias/' + $(this).val(),
				 combo:$('#id_pro'),
				 //	id:$(this).data('id_pro'),
				 funcion:function(){
					 $('#id_pro').change();
				 }
			 });
		}

	});
	

	$('#id_pro').on('change',function(event){
 
		 	if ($(this).val()==null || $(this).val()==''){ 
		 		$('#id_dist').find('option').not(':first').remove();
		 	}
		 	else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist'),
				  // 	id:$(this).data('id_dis'),
				   	funcion:function(){
				   		console.log('remove data');
				   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
				   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
				   	}
				});		 		
		 	}
		 
	});


//	$(':input', '#alu_familiar-frm').not(':button, :radio, :hidden, :select').val('');

	/*if (id_par == _PARENTESCO_PAPA) {
		id_par = _PARENTESCO_MAMA;
		id_gen = _GENERO_FEMENINO
	} else {
		id_par = _PARENTESCO_PAPA;
		id_gen = _GENERO_MASCULINO;
	}*/

	$("#alu_familiar-frm #fam_id").val('');
	$("#alu_familiar-frm #id").val('');
	$("#alu_familiar-frm #nro_doc").val('');
	$("#alu_familiar-frm #ape_pat").val('');
	$("#alu_familiar-frm #ape_mat").val('');
	$("#alu_familiar-frm #nom").val('');
	//$("#alu_familiar-frm #fec_nac").val('');
	$("#alu_familiar-frm #fec_def").val('');
	$("#alu_familiar-frm #dir").val('');
	$("#alu_familiar-frm #ref").val('');
	$("#alu_familiar-frm #corr").val('');
	$("#alu_familiar-frm #tlf").val('');
	$("#alu_familiar-frm #cel").val('');
	$("#alu_familiar-frm #prof").val('');
	$("#alu_familiar-frm #ocu").val('');
	$("#alu_familiar-frm #cto_tra").val('');
	$("#alu_familiar-frm #dir_tra").val('');
	$("#alu_familiar-frm #tlf_tra").val('');
	$("#alu_familiar-frm #cel_tra").val('');
	$("#alu_familiar-frm #email_tra").val('');
	$('#alu_familiar-frm input:radio[name="tap"][value="A"]').click();
	$('#alu_familiar-frm #ape_pat').removeAttr("readonly");
	$('#alu_familiar-frm #ape_mat').removeAttr("readonly");
	$('#alu_familiar-frm #nom').removeAttr("readonly");	

	//$("#alu_familiar-frm #id_par").val(id_par).trigger('change');
	$("#alu_familiar-frm #id_tdc").val('1').trigger('change');
	//$("#alu_familiar-frm #id_eci").selectmenu('refresh', true);
	$("#alu_familiar-frm #id_eci option:first").attr('selected','selected').trigger('change'); 
	$("#alu_familiar-frm #id_pais option:first").attr('selected','selected').trigger('change'); 
	//id_gin
	$("#alu_familiar-frm #id_gin option:first").attr('selected','selected').trigger('change'); 
	//$("#alu_familiar-frm #id_eci option:first").attr('selected','selected').trigger('change'); 
	$("#alu_familiar-frm #flag_alu_ex option:first").attr('selected','selected').trigger('change'); 
	//$('#alu_familiar-frm input:radio[name="viv_alu"][value="1"]').click();
	//$('#alu_familiar-frm input:radio[name="viv"][value="1"]').click();

	/*$("#alu_familiar-frm #id_gin").val('').trigger('change');
	$("#alu_familiar-frm #id_rel").val('').trigger('change');
	$("#alu_familiar-frm #id_eci").val('').trigger('change');
	$("#alu_familiar-frm #id_ocu").val('').trigger('change');
	$("#alu_familiar-frm #viv").val('');
	if ($("#usu").val() == "COORDINACION"){
	$("#alu_familiar-frm #id_eci").val('1').trigger('change');}*/

	$("#alu_familiar-frm #dropzone1").css("background-image", "");
	$("#alu_familiar-frm #dropzone1").css('display', 'none');

	//$("#alu_familiar-frm #fec_nac").val('31/12/2000');
	
	//$('#btnfamiliarNuevo').css('display', 'none');
	$('#btnCapturarFoto').attr("disabled", "disabled");
	$(window).scrollTop(0);
}

function alumnoNuevo() {

	
	/*$("#alu_alumno-frm #nro_doc").removeAttr("readonly");
	$("#alu_alumno-frm #nro_doc").focus();
	var id_par = $("#alu_familiar-frm #id_par").val();
	var id_gen = $("#alu_familiar-frm #id_gen").val();

//	$(':input', '#alu_familiar-frm').not(':button, :radio, :hidden, :select').val('');

	if (id_par == _PARENTESCO_PAPA) {
		id_par = _PARENTESCO_MAMA;
		id_gen = _GENERO_FEMENINO
	} else {
		id_par = _PARENTESCO_PAPA;
		id_gen = _GENERO_MASCULINO;
	}

	$("#alu_familiar-frm #fam_id").val('');
	$("#alu_familiar-frm #id").val('');
	$("#alu_familiar-frm #nro_doc").val('');
	$("#alu_familiar-frm #ape_pat").val('');
	$("#alu_familiar-frm #ape_mat").val('');
	$("#alu_familiar-frm #nom").val('');
	$("#alu_familiar-frm #fec_nac").val('');
	$("#alu_familiar-frm #fec_def").val('');
	$("#alu_familiar-frm #dir").val('');
	$("#alu_familiar-frm #ref").val('');
	$("#alu_familiar-frm #corr").val('');
	$("#alu_familiar-frm #tlf").val('');
	$("#alu_familiar-frm #cel").val('');
	$("#alu_familiar-frm #prof").val('');
	$("#alu_familiar-frm #ocu").val('');
	$("#alu_familiar-frm #cto_tra").val('');
	$("#alu_familiar-frm #dir_tra").val('');
	$("#alu_familiar-frm #tlf_tra").val('');
	$("#alu_familiar-frm #cel_tra").val('');
	$("#alu_familiar-frm #email_tra").val('');
	$('#alu_familiar-frm input:radio[name="tap"][value="A"]').click();
	$('#alu_familiar-frm #ape_pat').removeAttr("readonly");
	$('#alu_familiar-frm #ape_mat').removeAttr("readonly");
	$('#alu_familiar-frm #nom').removeAttr("readonly");	

	$("#alu_familiar-frm #id_par").val(id_par).trigger('change');
	$("#alu_familiar-frm #id_tdc").val('1').trigger('change');
	//$("#alu_familiar-frm #id_eci").selectmenu('refresh', true);
	$("#alu_familiar-frm #id_eci option:first").attr('selected','selected').trigger('change'); 
	$("#alu_familiar-frm #flag_alu_ex option:first").attr('selected','selected').trigger('change'); 
	//$('#alu_familiar-frm input:radio[name="viv_alu"][value="1"]').click();
	//$('#alu_familiar-frm input:radio[name="viv"][value="1"]').click();

	/*$("#alu_familiar-frm #id_gin").val('').trigger('change');
	$("#alu_familiar-frm #id_rel").val('').trigger('change');
	$("#alu_familiar-frm #id_eci").val('').trigger('change');
	$("#alu_familiar-frm #id_ocu").val('').trigger('change');
	$("#alu_familiar-frm #viv").val('');
	if ($("#usu").val() == "COORDINACION"){
	$("#alu_familiar-frm #id_eci").val('1').trigger('change');}*/

	/*$("#alu_familiar-frm #dropzone1").css("background-image", "");
	$("#alu_familiar-frm #dropzone1").css('display', 'none');

	//$("#alu_familiar-frm #fec_nac").val('31/12/2000');
	
	//$('#btnfamiliarNuevo').css('display', 'none');
	$('#btnCapturarFoto').attr("disabled", "disabled");
	$(window).scrollTop(0);*/
	$("#div_alumno").css('display','block');
	var ape_pat = $("#alu_alumno-frm #ape_pat").val(); 
	var ape_mat = $("#alu_alumno-frm #ape_mat").val(); 
	
	console.log(ape_pat);
	
	/*$(':input','#alu_alumno-frm')
	 .not(':button, :radio, :hidden')
	 .val('');*/
		
	$("#alu_alumno-frm #alu_id").val('');	
	$("#alu_alumno-frm #id").val('');	
	$("#alu_alumno-frm #nro_doc").val('');	
	$("#alu_alumno-frm #nro_doc").val('');
	$("#alu_alumno-frm #ape_pat").val('');
	$("#alu_alumno-frm #ape_mat").val('');
	$("#alu_alumno-frm #nom").val('');
	//$("#alu_alumno-frm #fec_nac").val('');
	$("#alu_alumno-frm #direccion").val('');
	$("#alu_alumno-frm #ref").val('');
	$("#alu_alumno-frm #telf").val('');
	$("#alu_alumno-frm #celular").val('');
	$("#alu_alumno-frm #corr").val('');
	//$("#alumno-form #id_tap").val('A').trigger('click');	
	//$("#alumno-form input[name='id_tap']").val('A').click();
	//alert($("#alumno-form input[name='id_tap'][value='A']").html());
    $("#alu_alumno-frm  INPUT[name=id_tap]").val(['A']);


	$("#alu_alumno-frm #id_par").val('').trigger('change');	
	$("#alu_alumno-frm #id_tdc").val('1').trigger('change');	
	//$("#alu_alumno-frm #id_gen").prop('checked',false);
	//('#alu_alumno-frm input[name="id_gen"]').prop('checked', false).change();
	//$("#alu_alumno-frm #id_gen" ).removeAttr('checked');
	$("#alu_alumno-frm input[name='id_gen']").removeAttr('checked');
	$("#alu_alumno-frm #id_idio1").val('2').trigger('change');	
	$("#alu_alumno-frm #id_idio2").val('1').trigger('change');	
	
	$("#alu_alumno-frm #dropzone2").css("background-image", "");	
	
	$("#alu_alumno-frm #ape_pat").val(ape_pat); 
	$("#alu_alumno-frm #ape_mat").val(ape_mat); 

	$('#btnCapturarFotoAlu').attr("disabled","disabled");
	
	$("#alu_alumno-frm #nro_doc").removeAttr("readonly");
	$("#alu_alumno-frm #nom").removeAttr("readonly");
	$("#alu_alumno-frm #nro_doc").focus();
}

function otroFamiliarNuevo(){
	$("#div_otro_familiar").css('display','block');
	$('#otrofamiliar-form input[name=id_tap]').attr("disabled",false);
	$("#otrofamiliar-form #fam_id").val('');	
	$("#otrofamiliar-form #id").val('');
	$("#div_otro_familiar #nro_doc").val('');
	$("#div_otro_familiar #ape_pat").val('');
	$("#div_otro_familiar #ape_mat").val('');
	$("#div_otro_familiar #nom").val('');
	$("#div_otro_familiar #dir").val('');
	$("#div_otro_familiar #ref").val('');
	$("#div_otro_familiar input[name='id_gen']").removeAttr('checked');
	$("#div_otro_familiar input[name='rec_lib']").removeAttr('checked');
	$("#div_otro_familiar #tap").val('A').trigger('click');	
	$("#div_otro_familiar #id_par").val('').trigger('change');	
	$("#div_otro_familiar #id_tdc").val('1').trigger('change');	
	$("#div_otro_familiar #id_gin").val('').trigger('change');	
}



//$('#alu_familiar-frm #fec_nac').daterangepicker({ autoUpdateInput: false }, function(chosen_date) {  });
