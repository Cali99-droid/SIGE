//Se ejecuta cuando la pagina q lo llama le envia parametros
var _padre_madre=null;
var _es_padre_madre=true;
var _PARENTESCO_PAPA = "2";
var _PARENTESCO_MAMA = "1";
function onloadPage(params){
	_onloadPage();
	//Por defecto deshabilitado los tabs de familiares e hijos
	$("#tabs_familia").tabs({
        disabled: [1,2]
    })
	deshabilitar_campos_familia();
	//botones grabar y eliminar desactivados
	$('#frm-familia_mant #btn_grabar').attr('disabled','disabled');
	$('#frm-familia_mant #btn_eliminar').attr('disabled','disabled');
	$('#id_par').on('change',function(event){
		if($('#id_par').val()==_PARENTESCO_PAPA){
			 $("#frm-familiar_mant #id_gen").val(1).change();
		} else if($('#id_par').val()==_PARENTESCO_MAMA){
			$("#frm-familiar_mant #id_gen").val(0).change();
		}
		
	});
	
	$('#id_suc').on('change',function(event){
		if($("[name='tip_busqueda']:checked").length>0){
		valor=$('input[name="tip_busqueda"]:checked').val();
		llenarTabla(valor);
		}
	});
	
	$('#id_niv').on('change',function(event){
		if($("[name='tip_busqueda']:checked").length>0){
		valor=$('input[name="tip_busqueda"]:checked').val();
		llenarTabla(valor);
		}
	});
	_llenarCombo('ges_giro_negocio',$('#id_gir'));
	_llenarCombo('ges_sucursal',$('#id_suc'));
	_llenarCombo('cat_nivel',$('#id_niv'));
	//Buscar los datos del alumno

	if($("[name='tip_busqueda']:checked").length>0){
		var valor=$('input[name="tip_busqueda"]:checked').val();
		//alert(valor);
		llenarTabla(valor);
	}	
	
	$('input[name="tip_busqueda"]').change(function() {	
		var valor=$('input[name="tip_busqueda"]:checked').val();
		//var param={tipBusqueda:valor};
		//alert(valor);
		llenarTabla(valor);
	});
	
	$('#frm-alumno_mant #btn_grabar').on('click',function(event){
		_post($('#frm-alumno_mant').attr('action') , $('#frm-alumno_mant').serialize());
	});	

	$("#frm-familiar_mant #id_tdc").on('change', function(){
		if($(this).val()==1){
			 $("#frm-familiar_mant #nro_doc").attr('maxlength','8');
		}else if($(this).val()==2){
			 $("#frm-familiar_mant #nro_doc").attr('maxlength','11');
		}
	});
	
	$("#frm-familiar_mant #id_anio").val($('#_id_anio').text());
}

	function llenarTabla(valor){
				var param={tipBusqueda:valor, id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_suc: $('#id_suc').val(), id_niv:$('#id_niv').val()};
				_get('api/familiar/busquedaAlumnos1',function(data){
					$('#tabla-familiares').dataTable({
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
							{"title":"Familia", "data": "familia"},
	    	        ],
	    	        "initComplete": function( settings ) {
					_initCompleteDT(settings);
					$("#total").text(data.length);
					$("#tabla-familiares tbody tr").click(function(){
					//Limpiar los campos
					limpiar_campos_familia();
	
					$("#id_anio").val($('#_id_anio').text());
					$(this).addClass('selected').siblings().removeClass('selected');    
					   var codigo=$(this).find('td:nth-child(1)').html();
					   _get('api/familiar/obtenerDatosFamilia/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   habilitar_campos_familia();
						   $("#frm-familia_mant #btn_grabar").removeAttr('disabled');
						   $("#frm-familia_mant #btn_eliminar").removeAttr('disabled');
						   $("#tabs_familia").tabs("enable",1);
						   $("#tabs_familia").tabs("enable",2);
						   $("#familia").text(data.nom);
						   $("#frm-familiares_mant #familia").text(data.nom);
						   $("#frm-familia_mant #id_gpf").val(data.id_gpf);
						   $("#frm-familia_mant #id").val(data.id);
						   $("#frm-familia_mant #id_usr").val(data.id_usr);
						   $("#frm-familia_mant #cod").val(data.cod);
						   $("#frm-familia_mant #nom").val(data.nom);
						   $("#frm-familia_mant #cod_aseg").val(data.cod_aseg);
						   $("#frm-familia_mant #login").val(data.login);
						  // if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
							   $("#frm-familia_mant #password").val(data.pass);
						 //  }	   
						   
						   if(data.id_gen!=null){
								   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
								   $("#frm-alumno_mant #id_gen").change();
						   }
							
							if(data.est=='A'){
								$("#est").val('A');
								$('#est').change();
							} else if(data.est=='I'){
								$("#est").val('I');
								$('#est').change();
							}	
								
							$("#frm-familia_mant #direccion").val(data.direccion);
							$("#frm-familia_mant #referencia").val(data.referencia);
								$('#frm-familia_mant #id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#frm-familia_mant #id_dist'),
										id:data.id_dist,
										funcion:function(){
											console.log('remove data');
											$('#frm-familia_mant #id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#frm-familia_mant #id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#frm-familia_mant #id_dep').on('change',function(event){
								
								 if($('#frm-familia_mant #id_dep').val()!=null && $('#frm-familia_mant #id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
									 _llenar_combo({
											url:'api/comboCache/provincias/' + $(this).val(),
											combo:$('#frm-familia_mant #id_pro'),
											id:data.id_pro,
											funcion:function(){
												$('#frm-familia_mant #id_pro').change();
											}
										}); 
								 }
							});
							_llenarCombo('cat_departamento',$('#frm-familia_mant #id_dep'),data.id_dep,null, function(){
								$('#frm-familia_mant #id_dep').change();
							});	
							
							_llenarCombo('cat_tipo_seguro',$('#frm-familia_mant #id_seg'), data.id_seg);
							_llenarCombo('cat_centro_salud',$('#frm-familia_mant #id_csal'), data.id_csal);
							
							//Listar a los familiares
							var param={id_gpf:data.id};
							_llenarComboURL('api/alumno/listarTodosFamiliares', $('#id_fam_emer'),
								data.id_fam_emer, param, function() {
									 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
									 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
									$('#id_fam_emer').change();
							});
							
									//Listar las matriculas del alumno
							var param={id_gpf:data.id, id_anio:$('#_id_anio').text()};
							 _get('api/alumno/listarAlumnos',function(data){
								 $('#tabla_hijos').dataTable({
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
										{"title":"Código", "data" : "cod"}, 
										{"title":"Apellidos y Nombres", "data" : "alumno"}, 
										{"title":"Nivel", "data" : "nivel"}, 
										{"title":"Grado", "data" : "grado"}, 
										{"title":"Sección", "data" : "aula"}, 
										{"title":"Matricula", "data" : "matricula"}, 
										{"title":"Situacion", "data" : "estado"}
								],
								"initComplete": function( settings ) {
									   _initCompleteDT(settings);
								 }
								});
							 },param);	
							
							_get('api/alumno/listarTodosFamiliares',function(data){
										$('#tabla_familiares').dataTable({
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
												{"title":"N° Doc", "data": "nro_doc"},
												{"title":"Apellidos y Nombres", "data": "aux1"},
												{"title":"Parentesco", "data": "value"},
												{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="familiar_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="familiar_eliminar(' + row.id + ')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
										],
										"initComplete": function( settings ) {
										_initCompleteDT(settings);
										$("#total").text(data.length);
										$("#tabla_familiares tbody tr").click(function(){
										$("#id_anio").val($('#_id_anio').text());
										   $(this).addClass('selected').siblings().removeClass('selected');    
										   var nro=$(this).find('td:nth-child(1)').html();

								}
								);

								 }
							});
							_padre_madre=obtener_padre_madre(data);
		},param);	
		$('#frm-familiar_mant #id_gpf').val(data.id);
					   });
					   $('#frm-familiar_mant #btn_nuevo').click();
					   
					}
					);

	    			 }
			});
		},param);	
	}
	
	
$('#frm-familia_mant #btn_nuevo').on('click',function(event){
		habilitar_campos_familia();
		limpiar_campos_familia();
		$('#tabla_familiares').html('');
		$('#tabla_hijos').html('');
		$('#frm-familia_mant #id_pro').on('change',function(event){

			if ($(this).val()!=null && $(this).val()!=''){ 
			/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
			}
			else{*/
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#frm-familia_mant #id_dist'),
					funcion:function(){
						console.log('remove data');
						$('#frm-familia_mant #id_dep').removeData('id_pro');//eliminar campos auxiliares
						$('#frm-familia_mant #id_pro').removeData('id_dist');//eliminar campos auxiliares
					}
				});		 		
			}
		 
		});
							
		$('#frm-familia_mant #id_dep').on('change',function(event){
			 if($('#frm-familia_mant #id_dep').val()!=null && $('#frm-familia_mant #id_dep').val()!=''){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#frm-familia_mant #id_pro'),
						funcion:function(){
							$('#frm-familia_mant #id_pro').change();
						}
					}); 
			 }
		});
		_llenarCombo('cat_departamento',$('#frm-familia_mant #id_dep'),null,null, function(){
			$('#frm-familia_mant #id_dep').change();
		});	
		
		_llenarCombo('cat_tipo_seguro',$('#frm-familia_mant #id_seg'));
		_llenarCombo('cat_centro_salud',$('#frm-familia_mant #id_csal'));
		$('#frm-familia_mant #btn_grabar').removeAttr('disabled');
			
});

$('#frm-familia_mant #btn_grabar').on('click',function(event){
		var validation = $('#frm-familia_mant').validate(); 
		//Verificar si existe la familia
		
							_get("api/gruFam/verficarFamilia/"+$('#frm-familia_mant #nom').val(), function(data) {

								console.log(data);
								if(data!=null){
									var existe = data;
									//alert('nro_vac'+nro_vac);
									if(existe==1){
										swal(
										{
											title : "Desea continuar?",
											text :'Ya existe una familia '+$('#frm-familia_mant #nom').val()+' , realmenete desea crear otro?',
											type: "success",
											showCancelButton : true,
											confirmButtonColor : "rgb(33, 150, 243)",
											cancelButtonColor : "#EF5350",
											confirmButtonText : "Si, Continuar",
											cancelButtonText : "No, Cancelar!",
											closeOnConfirm : true,
											closeOnCancel : true,
											html:true
										},
										function(isConfirm) {

											if (isConfirm) {
											if ($('#frm-familia_mant').valid()){
												_post($('#frm-familia_mant').attr('action') , $('#frm-familia_mant').serialize(),function(data) {
													var valor=$('input[name="tip_busqueda"]:checked').val();
													llenarTabla(valor);
													$('#frm-familia_mant #id').val(data.result.id_gpf);
													$('#frm-familia_mant #login').val(data.result.usuario);
													$('#frm-familia_mant #password').val(data.result.pass);
													$('#frm-familia_mant #cod').val(data.result.cod);
													$("#tabs_familia").tabs("enable",1);
													$("#tabs_familia").tabs("enable",2);
													$('#frm-familiar_mant #btn_nuevo').click();
													$('#frm-familiar_mant #id_gpf').val(data.result.id_gpf);
												}	
												);
											}		

											}else{
													$('#frm-familia_mant #nom').val('');
												}
											}
										);
									} else {
										if ($('#frm-familia_mant').valid()){
												_post($('#frm-familia_mant').attr('action') , $('#frm-familia_mant').serialize(),function(data) {
													var valor=$('input[name="tip_busqueda"]:checked').val();
													llenarTabla(valor);
													$('#frm-familia_mant #id').val(data.result.id_gpf);
													$('#frm-familia_mant #login').val(data.result.usuario);
													$('#frm-familia_mant #password').val(data.result.pass);
													$('#frm-familia_mant #cod').val(data.result.cod);
													$("#tabs_familia").tabs("enable",1);
													$("#tabs_familia").tabs("enable",2);
													$('#frm-familiar_mant #btn_nuevo').click();
													$('#frm-familiar_mant #id_gpf').val(data.result.id_gpf);
												}	
												);
											}	
										
									}									
								}
								

		});
		
		
});

$('#frm-familia_mant #btn_eliminar').on('click',function(event){
		var id=$('#frm-familia_mant #id').val();
		_DELETE({url:'api/gruFam/' + id,
		context:_URL,
		success:function(){
			var valor=$('input[name="tip_busqueda"]:checked').val();
			llenarTabla(valor);

		}
		});	
});	

$('#frm-familiar_mant #btn_grabar').on('click',function(event){
	$('#frm-familiar_mant #id_par').removeAttr('disabled');
	$('#frm-familiar_mant #id_tdc').removeAttr('disabled');
	$('#frm-familiar_mant #es_padre_madre').val(_es_padre_madre);
	var validation = $('#frm-familiar_mant').validate(); 
	if ($('#frm-familiar_mant').valid()){
		_POST({form:$('#frm-familiar_mant'),
						  context:_URL,
						  msg_type:'notification',
						  msg_exito : 'SE REGISTRO AL FAMILIAR CORRECTAMENTE.',
						  success:function(data){
							    //DESCARGAR 
							  //id_gpf=2490, id_per=null, id=5010, error=null
							  limpiar_campos_familiar();
							  //$('#frm-familiar_mant #id').val(data.result.id_per);
			//$('#frm-familiar_mant #id_fam').val(data.result.id_fam);
			var param={id_gpf:data.result.id_gpf};
					_get('api/alumno/listarTodosFamiliares',function(data){
					$('#tabla_familiares').dataTable({
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
							{"title":"N° Doc", "data": "nro_doc"},
							{"title":"Apellidos y Nombres", "data": "aux1"},
							{"title":"Parentesco", "data": "value"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="familiar_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="familiar_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
										],
										"initComplete": function( settings ) {
										_initCompleteDT(settings);
										$("#total").text(data.length);
										$("#tabla_familiares tbody tr").click(function(){
										$("#id_anio").val($('#_id_anio').text());
										   $(this).addClass('selected').siblings().removeClass('selected');    
										   var nro=$(this).find('td:nth-child(1)').html();

								}
								);

								 }
							});
							_padre_madre=obtener_padre_madre(data);
							},param); 
						 }, 
						 error:function(data){
							 _alert_error(data.msg);
							 //$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
							 //$('#nro_dni').focus();
						}
					}); 
		/*_post($('#frm-familiar_mant').attr('action') , $('#frm-familiar_mant').serialize(),function(data) {
			console.log(data);
			$('#frm-familiar_mant #id').val(data.result.id_per);
			$('#frm-familiar_mant #id_fam').val(data.result.id_fam);
			var param={id_gpf:data.result.id_gpf};
					_get('api/alumno/listarTodosFamiliares',function(data){
					$('#tabla_familiares').dataTable({
					 data : data,
					 searching: false, 
					 paging: false, 
					 info: false,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
					 columns : [ 
							{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"N° Doc", "data": "nro_doc"},
							{"title":"Apellidos y Nombres", "data": "aux1"},
							{"title":"Parentesco", "data": "value"},
							{"title":"Acciones", "render": function ( data, type, row ) {
			return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="familiar_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
			'<a href="#" data-id="' + row.id + '" onclick="familiar_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
		}}
					],
					"initComplete": function( settings ) {
					_initCompleteDT(settings);
					$("#total").text(data.length);
					$("#tabla_familiares tbody tr").click(function(){
					$("#id_anio").val($('#_id_anio').text());
					   $(this).addClass('selected').siblings().removeClass('selected');    
					   var nro=$(this).find('td:nth-child(1)').html();

			}
			);

			 }
		});
		_padre_madre=obtener_padre_madre(data);
		},param);
		});*/
		
	}	
});	

$('#frm-familiar_mant #btn_nuevo').on('click',function(event){
	familiar_nuevo();
	_es_padre_madre=true;
});	
	
$('#frm-alumno_mant #btn_nuevo').on('click',function(event){
	var param1 = {};
	param1.id_gpf=$('#frm-familia_mant #id').val();
	param1.padre_madre=_padre_madre;
	_send('pages/alumno/alu_alumno_mantenimiento.html','Mantenimiento de Alumnos','Lista',param1);
})

function familiar_editar(id_fam){
	_es_padre_madre=false;
	var dni=null;
	_get('api/familiar/obtenerDatosFamiliarPersona/'+id_fam+'/'+dni,function(data){
	   $("#frm-familiar_mant #id").val(data.id);
	   $("#frm-familiar_mant #id_fam").val(id_fam);
	   $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
	   $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
	   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
	   $("#frm-familiar_mant #nom").val(data.nom);
	   $("#frm-familiar_mant #prof").val(data.prof);
	   $("#frm-familiar_mant #ocu").val(data.ocu);
	   //$('#frm-familiar_mant #nro_doc').attr("readonly", "readonly");
	  // $('#frm-familiar_mant #id_par').attr("disabled", "disabled");
	   if(data.id_gen!=null){
			   $("#frm-familiar_mant #id_gen").val(data.id_gen); 
			   $("#frm-familiar_mant #id_gen").change();
	   }
	   _llenarCombo('cat_parentesco',$('#frm-familiar_mant #id_par'), data.id_par,null,function(){
			$('#frm-familiar_mant #id_par').change();
		});
		_llenarCombo('cat_tipo_documento',$('#frm-familiar_mant #id_tdc'), data.id_tdc,null,function(){
			$('#frm-familiar_mant id_tdc').change();
		});	
		$("#frm-familiar_mant #nro_doc").val(data.nro_doc);
		$("#frm-familiar_mant #ubigeo").val(data.ubigeo);
		_llenarCombo('cat_est_civil',$('#frm-familiar_mant #id_eci'), data.id_tdc,null,function(){
			$('#frm-familiar_mant #id_eci').change();
		});
		var fec_nac = data.fec_nac;
		if(fec_nac){
			
			var arrFec_nac = fec_nac.split('-');
			$('#frm-familiar_mant #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
			var fecha_actual = new Date();
			var anio=fecha_actual.getFullYear();
			var edad=anio-arrFec_nac[0];
			$('#frm-familiar_mant #edad').text('Edad:'+edad);
		}

		
		if(data.viv==null){
			$("#frm-familiar_mant #id_viv1").click();
			$("#frm-familiar_mant #id_viv1").val('1');
			$('#frm-familiar_mant #div_defuncion').hide();
			$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
		} else{
			if(data.viv=='0'){
			$("#frm-familiar_mant #id_viv2").click();
			$('#frm-familiar_mant #div_defuncion').show();
			$('#frm-familiar_mant #fec_def').removeAttr("disabled");
			}
			if(data.viv=='1'){
			$("#frm-familiar_mant #id_viv1").click();
			$('#frm-familiar_mant #div_defuncion').hide();
			$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		} 
		
		if(data.ocu==null){
				$("#id_tra_rem2").click();
		} else{
				$("#id_tra_rem1").click();
		} 
		
		$("input:radio[name='id_tra_rem']").on("click", function(e) {
			var valor = $(this).val();
			if(valor==0){
				$("#div_trabajo").css('display','none');
			} else 	if(valor==1){
				$("#div_trabajo").css('display','block');
			}
		});
		
		$("input:radio[name='viv']").on("click", function(e) {
			if($(this).val()==0){
				$('#frm-familiar_mant #div_defuncion').show();
				$('#frm-familiar_mant #fec_def').removeAttr("disabled");
			}else {
				$('#frm-familiar_mant #div_defuncion').hide();
				$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		});
		if(data.trab==null){
			$("#trab2").click();
			$("#trab2").attr("checked",true);
			$('#id_cond').val(2);
			$('#id_cond').change();
		} else{
			if(data.trab=='1'){
			$('#trab1').click();
			}
			if(data.viv=='0'){
			$('#div_trabaja').hide();
			$('#trab2').click();
			}
		} 
		
		$("input:radio[name='trab']").on("click", function(e) {
			if($(this).val()==1){
				$("#id_cond").val('1');
				$('#id_cond').change();
			}else {
				$("#id_cond").val('2');
				$('#id_cond').change();
			}
		});
		
		if(data.est=='A'){
			$("#est").val('A');
			$('#est').change();
		} else if(data.est=='I'){
			$("#est").val('I');
			$('#est').change();
		}	
		
		$("#frm-familiar_mant #corr").val(data.corr);
		$("#frm-familiar_mant #email_inst").val(data.email_inst);
		$("#frm-familiar_mant #cel").val(data.cel);
		$("#frm-familiar_mant #tlf").val(data.tlf);
		$("#frm-familiar_mant #usuario").val(data.usuario);
		$("#frm-familiar_mant #pass_educando").val(data.pass_educando); 

});

	if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_ASIST_ADMINISTRATIVO)>-1){
		$("#frm-familiar_mant #cel").removeAttr("disabled");
		$("#frm-familiar_mant #corr").removeAttr("disabled");
	} else{
		_get('api/familiar/validarApoderado/'+id_fam+'/'+$('#_id_anio').text(),function(data){
	
		if(data){
			$("#frm-familiar_mant #cel").attr("disabled", "disabled");
			$("#frm-familiar_mant #corr").attr("disabled", "disabled");
		} else{
			$("#frm-familiar_mant #cel").removeAttr("disabled");
			$("#frm-familiar_mant #corr").removeAttr("disabled");
		}	
	
		});	
	}		
}	

$("#frm-familiar_mant #nro_doc").on('blur',function(){
			if($("#frm-familiar_mant #nro_doc").val()!=null && $("#frm-familiar_mant #nro_doc").val()!=""){
				var id_fam=0;
				nro_doc=$("#frm-familiar_mant #nro_doc").val();
				_GET({ url: 'api/familiar/obtenerDatosFamiliarPersona/'+id_fam+'/'+nro_doc,
						context: _URL,
					  // params:param,
					   success:
						function(data){
						   console.log(data);
						if(data!=null){
							console.log(data);
							//alert(data.id_tdc);
							$("#frm-familiar_mant #id").val(data.id);
						   $("#frm-familiar_mant #id_fam").val(data.id_fam);
						   $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						   $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
						   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
						   $("#frm-familiar_mant #nom").val(data.nom);
						   $("#frm-familiar_mant #prof").val(data.prof);
						   $("#frm-familiar_mant #ocu").val(data.ocu);
							if(data.id_gen!=null){
									   $("#frm-familiar_mant #id_gen").val(data.id_gen); 
									   $("#frm-familiar_mant #id_gen").change();
							}
							   _llenarCombo('cat_parentesco',$('#frm-familiar_mant #id_par'), data.id_par,null,function(){
									$('#frm-familiar_mant #id_par').change();
								});
								_llenarCombo('cat_tipo_documento',$('#frm-familiar_mant #id_tdc'), data.id_tdc,null,function(){
									$('#frm-familiar_mant id_tdc').change();
								});	
								$("#frm-familiar_mant #nro_doc").val(data.nro_doc);
								$("#frm-familiar_mant #ubigeo").val(data.ubigeo);
								_llenarCombo('cat_est_civil',$('#frm-familiar_mant #id_eci'), data.id_tdc,null,function(){
									$('#frm-familiar_mant #id_eci').change();
								});
								var fec_nac = data.fec_nac;
								if(fec_nac){
									
									var arrFec_nac = fec_nac.split('-');
									$('#frm-familiar_mant #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
									var fecha_actual = new Date();
									var anio=fecha_actual.getFullYear();
									var edad=anio-arrFec_nac[0];
									$('#frm-familiar_mant #edad').text('Edad:'+edad);
								}

								
								if(data.viv==null){
									$("#frm-familiar_mant #id_viv1").click();
									$("#frm-familiar_mant #id_viv1").val('1');
									$('#frm-familiar_mant #div_defuncion').hide();
									$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
								} else{
									if(data.viv=='0'){
									$("#frm-familiar_mant #id_viv2").click();
									$('#frm-familiar_mant #div_defuncion').show();
									$('#frm-familiar_mant #fec_def').removeAttr("disabled");
									}
									if(data.viv=='1'){
									$("#frm-familiar_mant #id_viv1").click();
									$('#frm-familiar_mant #div_defuncion').hide();
									$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
									}
								} 
								
								if(data.ocu==null){
										$("#id_tra_rem2").click();
								} else{
										$("#id_tra_rem1").click();
								} 
								
								$("input:radio[name='id_tra_rem']").on("click", function(e) {
									var valor = $(this).val();
									if(valor==0){
										$("#div_trabajo").css('display','none');
									} else 	if(valor==1){
										$("#div_trabajo").css('display','block');
									}
								});
								
								$("input:radio[name='viv']").on("click", function(e) {
									if($(this).val()==0){
										$('#frm-familiar_mant #div_defuncion').show();
										$('#frm-familiar_mant #fec_def').removeAttr("disabled");
									}else {
										$('#frm-familiar_mant #div_defuncion').hide();
										$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
									}
								});
								if(data.trab==null){
									$("#trab2").click();
									$("#trab2").attr("checked",true);
									$('#id_cond').val(2);
									$('#id_cond').change();
								} else{
									if(data.trab=='1'){
									$('#trab1').click();
									}
									if(data.viv=='0'){
									$('#div_trabaja').hide();
									$('#trab2').click();
									}
								} 
								
								$("input:radio[name='trab']").on("click", function(e) {
									if($(this).val()==1){
										$("#id_cond").val('1');
										$('#id_cond').change();
									}else {
										$("#id_cond").val('2');
										$('#id_cond').change();
									}
								});
								
								if(data.est=='A'){
									$("#est").val('A');
									$('#est').change();
								} else if(data.est=='I'){
									$("#est").val('I');
									$('#est').change();
								}	
								
								$("#frm-familiar_mant #corr").val(data.corr);
								$("#frm-familiar_mant #email_inst").val(data.email_inst);
								$("#frm-familiar_mant #cel").val(data.cel);
								$("#frm-familiar_mant #tlf").val(data.tlf);
								$("#frm-familiar_mant #usuario").val(data.usuario);
								$("#frm-familiar_mant #pass_educando").val(data.pass_educando); 
							
							$('#frm-familiar_mant #nro_doc').attr('readonly', 'readonly');
							$('#frm-familiar_mant #ape_pat').attr('readonly', 'readonly');
							$('#frm-familiar_mant #ape_mat').attr('readonly', 'readonly');
							$('#frm-familiar_mant #nom').attr('readonly', 'readonly');
							//$('#frm-familiar_mant #id_par').attr('disabled', 'disabled');
							$('#frm-familiar_mant #id_tdc').attr('disabled', 'disabled');
							//$('#alu_familiar-frm #id_tap').attr('disabled', 'disabled');
							//$(".id_tap").prop("disabled", true); 
							//$('#alu_familiar-frm input[name=id_tap]').attr("disabled",true);
							//$('#alu_familiar-frm #id_gpf').val(_id_gpf);
							//_familiar_existe=true;
						} else{
							//_familiar_existe=false;
						}	 				
				}});
			}
		});
		
$('#frm-familiar_mant #btn_buscar').on('click',function(event){
	var onShowModal = function(){		
	_GET({ url: 'api/trabajador/listarPersonas',
		context: _URL,
	    //params:param,
	    success:
		function(data){
			$('#col_persona-tabla').dataTable({
					 data : data,
					 /*
					 destroy: true,
					 select: true,
					 bFilter: false,
					 */
					 searching: true, 
					 paging: false, 
					 info: true,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
					 columns : [ 
							{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"DNI", "data": "nro_doc"},
							//{"title":"Persona", "data": "value"},
							{"title":"Persona", "render": function ( data, type, row ) {
								return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value 
							}}
							/*{"title":"Fecha Inicio", "render":function ( data, type, row,meta ) { 
									return  _parseDate(row.fec_ini);
								} 
							},*/
							/*{"title":"Fecha Fin", "render":function ( data, type, row,meta ) { 
									return  _parseDate(row.fec_fin);
								} 
							},
							{"title":"Fecha Fin PP", "render":function ( data, type, row,meta ) { 
									return  _parseDate(row.fec_fin_prue);
								} 
							},
							{"title":"Remuneracion", "data": "remuneracion"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="contrato_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="contrato_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}*/
						],
						"initComplete": function( settings ) {
						_initCompleteDTSB(settings);
						$("#total").text(data.length);
						$("#col_persona-tabla tbody tr").click(function(){
						//var id_per=$(this).find('td:nth-child(1)').html();
						var id_per=($(this).find('td:nth-child(3)').find('#id').val());
						if(id_per!=''){
							_GET({ url: 'api/familiar/obtenerDatosPersona/'+id_per,
								context: _URL,
							  // params:param,
							   success:
								function(data){
								   console.log(data);
								if(data!=null){
									console.log(data);
									_fillForm(data, $('#frm-trabajador_mant'));
								   $("#frm-familiar_mant #id").val(id_per);
								   $("#frm-familiar_mant #id_fam").val(data.id_fam);
								   
								  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
									_fillForm(data, $('#frm-trabajador_mant'));
									//$("#frm-familiar_mant #id_fam").val('');
									if(data.id_par!=null){
											   $("#frm-familiar_mant #id_par").val(data.id_par); 
											   $("#frm-familiar_mant #id_par").change();
									}
									if(data.id_gen!=null){
											   $("#frm-familiar_mant #id_gen").val(data.id_gen); 
											   $("#frm-familiar_mant #id_gen").change();
									}

										_llenarCombo('cat_tipo_documento',$('#id_tdc'), data.id_tdc,null,function(){
											$('#id_tdc').change();
										});	
										_llenarCombo('cat_est_civil',$('#id_eci'), data.id_tdc,null,function(){
											$('#id_eci').change();
										});
										var fec_nac = data.fec_nac;
										if(fec_nac){
											
											var arrFec_nac = fec_nac.split('-');
											$('#fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
											var fecha_actual = new Date();
											var anio=fecha_actual.getFullYear();
											var edad=anio-arrFec_nac[0];
											$('#frm-familiar_mant #edad').text('Edad:'+edad);
										}
									/*if(data.id_nac==null){
										_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
									} else {
										_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
									}*/
								
								 $("#frm-familiar_mant #nro_doc").val(data.nro_doc);
								 $("#frm-familiar_mant #ubigeo").val(data.ubigeo);
								// $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
							     $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
							     $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
							     $("#frm-familiar_mant #nom").val(data.nom);
							     $("#frm-familiar_mant #prof").val(data.prof);
							     $("#frm-familiar_mant #ocu").val(data.ocu);
								$("#frm-familiar_mant #corr").val(data.corr);
								$("#frm-familiar_mant #email_inst").val(data.email_inst);
								$("#frm-familiar_mant #cel").val(data.cel);
								$("#frm-familiar_mant #tlf").val(data.tlf);
								$('#frm-familiar_mant #nro_doc').attr('readonly', 'readonly');
								$('#frm-familiar_mant #ape_pat').attr('readonly', 'readonly');
								$('#frm-familiar_mant #ape_mat').attr('readonly', 'readonly');
								$('#frm-familiar_mant #nom').attr('readonly', 'readonly');
								//$('#frm-familiar_mant #id_par').attr('disabled', 'disabled');
								$('#frm-familiar_mant #id_tdc').attr('disabled', 'disabled');
								//$("#frm-familiar_mant #usuario").val(data.usuario);
								//$("#frm-familiar_mant #pass_educando").val(data.pass_educando); 
									
								} else{
									//_familiar_existe=false;
								}	 				
						}});
						}
						$(".modal .close").click();
						   //var nro=$(this).find('td:nth-child(1)').html();

						});
				 }
			});
		}
	});
   }
		//funcion q se ejecuta despues de grabar
		var onSuccessSave = function(data){

		}
		
		//Abrir el modal
		var titulo;

			titulo = 'Búsqueda de Personas';
		
		_modal_full(titulo, "pages/rrhh/listapersonas.html",onShowModal,onSuccessSave);
});

function familiar_eliminar(id_fam){
	var id_gpf=$('#frm-familiar_mant #id_gpf').val();
	_DELETE({url:'api/familiar/eliminarOtroFamiliar/' + id_gpf+'/'+ id_fam,
		context:_URL,
		success:function(){
					var param={id_gpf:id_gpf};
					_get('api/alumno/listarTodosFamiliares',function(data){
					$('#tabla_familiares').dataTable({
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
							{"title":"N° Doc", "data": "nro_doc"},
							{"title":"Apellidos y Nombres", "data": "aux1"},
							{"title":"Parentesco", "data": "value"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="familiar_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="familiar_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
										],
										"initComplete": function( settings ) {
										_initCompleteDT(settings);
										$("#total").text(data.length);
										$("#tabla_familiares tbody tr").click(function(){
										$("#id_anio").val($('#_id_anio').text());
										   $(this).addClass('selected').siblings().removeClass('selected');    
										   var nro=$(this).find('td:nth-child(1)').html();

								}
								);

								 }
							});
							_padre_madre=obtener_padre_madre(data);
							},param);
							_es_padre_madre=true;
			}
		});
}	

function familiar_nuevo(){
	   limpiar_campos_familiar();
	   _llenarCombo('cat_parentesco',$('#frm-familiar_mant #id_par'),null,null,function(){
			$('#frm-familiar_mant #id_par').change();
		});
		_llenarCombo('cat_tipo_documento',$('#frm-familiar_mant #id_tdc'),null,null,function(){
			$('#frm-familiar_mant id_tdc').change();
		});	
		_llenarCombo('cat_est_civil',$('#frm-familiar_mant #id_eci'),null,null,function(){
			$('#frm-familiar_mant #id_eci').change();
		});

			$("#frm-familiar_mant #id_viv1").click();
			$("#frm-familiar_mant #id_viv1").val('1');
			$('#frm-familiar_mant #div_defuncion').hide();
			$('#frm-familiar_mant #fec_def').attr("disabled", "disabled"); 
			$("#id_tra_rem2").click();
		
		$("input:radio[name='id_tra_rem']").on("click", function(e) {
			var valor = $(this).val();
			if(valor==0){
				$("#div_trabajo").css('display','none');
			} else 	if(valor==1){
				$("#div_trabajo").css('display','block');
			}
		});
		
		$("input:radio[name='viv']").on("click", function(e) {
			if($(this).val()==0){
				$('#frm-familiar_mant #div_defuncion').show();
				$('#frm-familiar_mant #fec_def').removeAttr("disabled");
			}else {
				$('#frm-familiar_mant #div_defuncion').hide();
				$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		});
		
		$("#trab2").click();
		$("#trab2").attr("checked",true);
		$('#id_cond').val(2);
		$('#id_cond').change();
		
		$("input:radio[name='trab']").on("click", function(e) {
			if($(this).val()==1){
				$("#id_cond").val('1');
				$('#id_cond').change();
			}else {
				$("#id_cond").val('2');
				$('#id_cond').change();
			}
		});

		$("#est").val('A');
		$('#est').change();
}	

function obtener_padre_madre(gruFamFamiliars){
	
	console.log('gruFamFamiliars',gruFamFamiliars);

	var padre = "";
	var madre = "";
	for ( var i=0; i<gruFamFamiliars.length;i++){
		if (gruFamFamiliars[i].id_par == _PARENTESCO_PADRE){
			padre = gruFamFamiliars[i].ape_pat;
		}
		if (gruFamFamiliars[i].id_par == _PARENTESCO_MADRE){
			 madre = gruFamFamiliars[i].ape_pat;
		}
	}
	
	if (padre!="" && madre!="")
		return padre + " - " + madre;
	else if (padre!="")
		return padre;
	else
		return madre;
}

function deshabilitar_campos_familia(){
	$("#frm-familia_mant #nom").attr('readonly','readonly');
	$("#frm-familia_mant #direccion").attr('readonly','readonly');
	$("#frm-familia_mant #referencia").attr('readonly','readonly');
	$("#frm-familia_mant #cod_aseg").attr('readonly','readonly');
	$("#frm-familia_mant #est").attr("disabled", "disabled");
	$("#frm-familia_mant #id_dep").attr("disabled", "disabled");
	$("#frm-familia_mant #id_pro").attr("disabled", "disabled");
	$("#frm-familia_mant #id_dist").attr("disabled", "disabled");
	$("#frm-familia_mant #id_seg").attr("disabled", "disabled");
	$("#frm-familia_mant #id_csal").attr("disabled", "disabled");
	$("#privilegios_matricula").css('display','none');
}

function habilitar_campos_familia(){
	$("#frm-familia_mant #nom").removeAttr('readonly');
	$("#frm-familia_mant #direccion").removeAttr('readonly');
	$("#frm-familia_mant #referencia").removeAttr('readonly');
	$("#frm-familia_mant #cod_aseg").removeAttr('readonly');
	$("#frm-familia_mant #est").removeAttr('disabled');
	$("#frm-familia_mant #id_dep").removeAttr('disabled');
	$("#frm-familia_mant #id_pro").removeAttr('disabled');
	$("#frm-familia_mant #id_dist").removeAttr('disabled');
	$("#frm-familia_mant #id_seg").removeAttr('disabled');
	$("#frm-familia_mant #id_csal").removeAttr('disabled');
}

function limpiar_campos_familia(){
	$("#frm-familia_mant #familia").text('');
	$("#frm-familia_mant #id").val('');
	$("#frm-familia_mant #id_usr").val('');
	$("#frm-familia_mant #cod").val('');
	$("#frm-familia_mant #nom").val('');
	$("#frm-familia_mant #usuario").val('');
	$("#frm-familia_mant #password").val('');
	$("#frm-familia_mant #id_rel").val('').trigger('change');
	$("#frm-familia_mant #id_dep").val('').trigger('change');
	$("#frm-familia_mant #id_pro").val('').trigger('change');
	$("#frm-familia_mant #id_dist").val('').trigger('change');
	$("#frm-familia_mant #direccion").val('');
	$("#frm-familia_mant #referencia").val('');
	$("#frm-familia_mant #id_seg").val('').trigger('change');
	$("#frm-familia_mant #id_csal").val('').trigger('change');
	$("#frm-familia_mant #cod_aseg").val('');
}

function limpiar_campos_familiar(){
	$("#frm-familiar_mant #id").val('');
	$("#frm-familiar_mant #nro_doc").val('');
	$("#frm-familiar_mant #ubigeo").val('');
	$("#frm-familiar_mant #id_fam").val('');
	$("#frm-familiar_mant #alumno").text('');
	$("#frm-familiar_mant #ape_pat").val('');
	$("#frm-familiar_mant #ape_mat").val('');
	$("#frm-familiar_mant #nom").val('');
	$("#frm-familiar_mant #fec_nac").val('');
	$("#frm-familiar_mant #prof").val('');
	$("#frm-familiar_mant #ocu").val('');
	$('#frm-familiar_mant #id_par').removeAttr('disabled');
	$('#frm-familiar_mant #id_tdc').removeAttr('disabled');
	$('#frm-familiar_mant #nro_doc').removeAttr('readonly');
	$('#frm-familiar_mant #ape_pat').removeAttr('readonly');
	$('#frm-familiar_mant #ape_mat').removeAttr('readonly');
	$('#frm-familiar_mant #nom').removeAttr('readonly');
	$("#frm-familiar_mant #id_par").val('').trigger('change');
	$("#frm-familiar_mant #id_eci").val('').trigger('change');
	$("#frm-familiar_mant #id_tdc").val('').trigger('change');
	$("#frm-familiar_mant #id_gen").val('').trigger('change');
	//$("#frm-familiar_mant #fec_nac").val('02/02/1990');
	$("#frm-familiar_mant #corr").val('');
	$("#frm-familiar_mant #email_inst").val('');
	$("#frm-familiar_mant #cel").val('');
	$("#frm-familiar_mant #tlf").val('');
	$("#frm-familiar_mant #usuario").val('');
	$("#frm-familiar_mant #pass_educando").val(''); 
}	


