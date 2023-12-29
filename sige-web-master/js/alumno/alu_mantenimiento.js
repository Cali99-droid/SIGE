//Se ejecuta cuando la pagina q lo llama le envia parametros
var _id_gpf=null;
var _ape_pat=null;
var _ape_mat=null;
function onloadPage(params){
	_onloadPage();
	console.log(params);
	if(params!=undefined){
	_id_gpf=params.id_gpf;
	var apellidos = params.padre_madre.split('-');
	_ape_pat=apellidos[0];
	_ape_mat=apellidos[1];
	}
	//$("#li-tabs-1").attr
	$("#alu_familiar-frm #nro_doc").focus();
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
	//$("#tip_busqueda").change();
	//alert($("[name='tip_busqueda']:checked").length);
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
	
	if(_id_gpf!=null){
		$('#frm-alumno_mant #btn_nuevo').click();
	}	
	
	$('#frm-alumno_mant #btn_grabar').on('click',function(event){
		var validation = $('#frm-alumno_mant').validate(); 
		$('#frm-alumno_mant #id_tdc').removeAttr('disabled');
		if ($('#frm-alumno_mant').valid()){
			_post($('#frm-alumno_mant').attr('action') , $('#frm-alumno_mant').serialize(),function(data) {
			console.log(data);
			$('#frm-alumno_mant #id_tdc').attr('disabled', 'disabled');
			$('#frm-alumno_mant #cod').val(data.result.cod);
			$("#frm-alumno_mant #alumno").text(data.result.ape_pat+' '+data.result.ape_mat+' '+data.result.nom);
			if($("[name='tip_busqueda']:checked").length>0){
			valor=$('input[name="tip_busqueda"]:checked').val();
			llenarTabla(valor);
			}
			});
		}	
	});	
								
	//$("[name='tip_busqueda']").change(function () {
	
	function llenarTabla(valor){
				var param={tipBusqueda:valor, id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_suc: $('#id_suc').val(), id_niv:$('#id_niv').val()};
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
					$("#id_anio").val($('#_id_anio').text());
					   $(this).addClass('selected').siblings().removeClass('selected');    
					   var codigo=$(this).find('td:nth-child(1)').html();
					   _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   $("#frm-alumno_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						   $("#frm-alumno_mant #cod").val(data.cod);
						   $("#frm-alumno_mant #ape_pat").val(data.ape_pat);
						   $("#frm-alumno_mant #ape_mat").val(data.ape_mat);
						   $("#frm-alumno_mant #nom").val(data.nom);
						   if(data.id_gen!=null){
								   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
								   $("#frm-alumno_mant #id_gen").change();
						   }
							_llenarCombo('cat_tipo_documento',$('#frm-alumno_mant #id_tdc'), data.id_tdc,null,function(){
								$('#id_tdc').change();
							});	
							$("#frm-alumno_mant #nro_doc").val(data.nro_doc);
							_llenarCombo('cat_est_civil',$('#frm-alumno_mant #id_eci'), data.id_tdc,null,function(){
								$('#id_eci').change();
							});
							_llenarCombo('cat_condicion_per',$('#frm-alumno_mant #id_cond'), data.id_cond,null,function(){
								$('#id_cond').change();
							});
							var fec_nac = data.fec_nac;
							if(fec_nac){
								
								var arrFec_nac = fec_nac.split('-');
								$('#frm-alumno_mant #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
								var fecha_actual = new Date();
								var anio=fecha_actual.getFullYear();
								var edad=anio-arrFec_nac[0];
								$('#edad').text('Edad:'+edad);
							}
		
							
							
							if(data.viv==null){
								$("#id_viv1").click();
								$("#id_viv1").val('1');
								$('#div_defuncion').hide();
								$('#fec_def').attr("disabled", "disabled");
							} else{
								if(data.viv=='0')
								$("#id_viv2").click();
								if(data.viv=='1')
								$("#id_viv1").click();
							} 
							
							$("input:radio[name='viv']").on("click", function(e) {
								if($(this).val()==0){
									$('#div_defuncion').show();
									$('#fec_def').removeAttr("disabled");
								}else {
									$('#div_defuncion').hide();
									$('#fec_def').attr("disabled", "disabled");
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
							
							if(data.id_fam_emer!=null){
								$('#div_familiar_emergencia').show();
							} else {
								$('#div_familiar_emergencia').hide();	
							}
							$('#id_fam_emer').on('change',function(event){
								$('#familiar_emer').val()
							});	
							
							$('#id_fam_emer').on('change',function(event){
								 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
								 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
							});
							
							var param={id_gpf:data.id_gpf};
							_llenarComboURL('api/alumno/listarTodosFamiliares', $('#id_fam_emer'),
								data.id_fam_emer, param, function() {
									 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
									 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
									$('#id_fam_emer').change();
							});
								
							_llenar_combo({
							url:'api/alumno/listarFamilias',
							combo:$('#id_gpf'),
							text:'nom',
							context: _URL,
							id:data.id_gpf,
							function(){
								$('#id_gpf').change();
							}
							});	
							$("#frm-alumno_mant #corr").val(data.corr);
							$("#frm-alumno_mant #email_inst").val(data.usuario);
							$("#frm-alumno_mant #cel").val(data.cel);
							$("#frm-alumno_mant #tlf").val(data.tlf);
							$("#frm-alumno_mant #usuario").val(data.usuario);
							$("#frm-alumno_mant #pass_educando").val(data.pass_educando);
								$('#id_pro_nac').on('change',function(event){

								if ($(this).val()==null || $(this).val()==''){ 
									$('#id_dist_nac').find('option').not(':first').remove();
								}
								else{
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist_nac'),
										id:data.id_dist_nac,
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
											id:data.id_pro_nac,
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
											id: data.id_dep_nac,
											funcion:function(){
												$('#id_dep_nac').change();
											}
										});

									_llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'), data.id_nac);
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
								console.log(data.id_pais_nac);
							_llenarCombo('cat_pais',$('#id_pais_nac'),data.id_pais_nac,null, function(){
								$('#id_pais_nac').change();
							});	
							_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);
							_llenarCombo('cat_idioma',$('#id_idio1'), data.id_idio1);
							_llenarCombo('cat_idioma',$('#id_idio2'), data.id_idio2);
							
							//Listar las matriculas del alumno
							var param={id_alu:data.id_alu};
							 _get('api/alumno/listarMatriculas',function(data){
								 $('#tab_matriculas').dataTable({
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
										{"title":"Año", "data" : "anio"}, 
										{"title":"N", "data" : "niv"}, 
										{"title":"G", "data" : "grado"}, 
										{"title":"S", "data" : "secc"}, 
										{"title":"Salón", "data" : "salon"}, 
										{"title":"Servicio", "data" : "servicio"}, 
										{"title":"Estado", "data" : "estado"}
								],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
							 },param);	 
							
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

}

$('#frm-alumno_mant #btn_buscar').on('click',function(event){
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
								   $("#frm-alumno_mant #id").val(id_per);
								   //$("#frm-alumno_mant #id_fam").val(data.id_fam);
								   
								  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
									_fillForm(data, $('#frm-alumno_mant'));
									//$("#frm-familiar_mant #id_fam").val('');
									
									if(data.id_gen!=null){
											   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
											   $("#frm-alumno_mant #id_gen").change();
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
											$('#frm-alumno_mant #edad').text('Edad:'+edad);
										}
									/*if(data.id_nac==null){
										_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
									} else {
										_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
									}*/
								
								 $("#frm-alumno_mant #nro_doc").val(data.nro_doc);
								 $("#frm-alumno_mant #ubigeo").val(data.ubigeo);
								// $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
							     $("#frm-alumno_mant #ape_pat").val(data.ape_pat);
							     $("#frm-alumno_mant #ape_mat").val(data.ape_mat);
							     $("#frm-alumno_mant #nom").val(data.nom);
								$("#frm-alumno_mant #corr").val(data.corr);
								$("#frm-alumno_mant #email_inst").val(data.email_inst);
								$("#frm-alumno_mant #cel").val(data.cel);
								$("#frm-alumno_mant #tlf").val(data.tlf);
								$('#frm-alumno_mant #nro_doc').attr('readonly', 'readonly');
								$('#frm-alumno_mant #ape_pat').attr('readonly', 'readonly');
								$('#frm-alumno_mant #ape_mat').attr('readonly', 'readonly');
								$('#frm-alumno_mant #nom').attr('readonly', 'readonly');
								//$('#frm-familiar_mant #id_par').attr('disabled', 'disabled');
								$('#frm-alumno_mant #id_tdc').attr('disabled', 'disabled');
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

$("#frm-alumno_mant #nro_doc").on('blur',function(){
			if($("#frm-alumno_mant #nro_doc").val()!=null && $("#frm-alumno_mant #nro_doc").val()!=""){
				var id_fam=0;
				nro_doc=$("#frm-alumno_mant #nro_doc").val();
				_GET({ url: 'api/familiar/obtenerDatosFamiliarPersona/'+id_fam+'/'+nro_doc,
						context: _URL,
					  // params:param,
					   success:
						function(data){
						   console.log(data);
						   if(data!=null){
									console.log(data);
									_fillForm(data, $('#frm-trabajador_mant'));
								   //$("#frm-alumno_mant #id_fam").val(data.id_fam);
								   
								  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
									_fillForm(data, $('#frm-alumno_mant'));
									//$("#frm-familiar_mant #id_fam").val('');
									
									if(data.id_gen!=null){
											   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
											   $("#frm-alumno_mant #id_gen").change();
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
											$('#frm-alumno_mant #edad').text('Edad:'+edad);
										}
									/*if(data.id_nac==null){
										_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
									} else {
										_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
									}*/
								
								 $("#frm-alumno_mant #nro_doc").val(data.nro_doc);
								 $("#frm-alumno_mant #ubigeo").val(data.ubigeo);
								// $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
							     $("#frm-alumno_mant #ape_pat").val(data.ape_pat);
							     $("#frm-alumno_mant #ape_mat").val(data.ape_mat);
							     $("#frm-alumno_mant #nom").val(data.nom);
								$("#frm-alumno_mant #corr").val(data.corr);
								$("#frm-alumno_mant #email_inst").val(data.email_inst);
								$("#frm-alumno_mant #cel").val(data.cel);
								$("#frm-alumno_mant #tlf").val(data.tlf);
								$('#frm-alumno_mant #nro_doc').attr('readonly', 'readonly');
								$('#frm-alumno_mant #ape_pat').attr('readonly', 'readonly');
								$('#frm-alumno_mant #ape_mat').attr('readonly', 'readonly');
								$('#frm-alumno_mant #nom').attr('readonly', 'readonly');
								//$('#frm-familiar_mant #id_par').attr('disabled', 'disabled');
								$('#frm-alumno_mant #id_tdc').attr('disabled', 'disabled');
								//$("#frm-familiar_mant #usuario").val(data.usuario);
								//$("#frm-familiar_mant #pass_educando").val(data.pass_educando); 
									
								} else{
									//_familiar_existe=false;
								}	 				
					}
				});		   
			}
});

$('#frm-alumno_mant #btn_nuevo').on('click',function(event){
	   $("#frm-alumno_mant #alumno").text('NUEVO');
	   $("#frm-alumno_mant #id_gpf").val(_id_gpf);
	   $("#frm-alumno_mant #cod").val('');
	   $("#frm-alumno_mant #ape_pat").val('');
	   $("#frm-alumno_mant #ape_mat").val('');
	   if(_ape_pat!=null)
		   $("#frm-alumno_mant #ape_pat").val(_ape_pat);
	   if(_ape_mat!=null)
		   $("#frm-alumno_mant #ape_mat").val(_ape_mat);
	   $("#frm-alumno_mant #nom").val('');
	   $("#frm-alumno_mant #id_gen").val('').trigger('change'); 
		_llenarCombo('cat_tipo_documento',$('#frm-alumno_mant #id_tdc'), null,null,function(){
			$('#id_tdc').change();
		});	
		$("#frm-alumno_mant #nro_doc").val('');
		_llenarCombo('cat_est_civil',$('#frm-alumno_mant #id_eci'),null,null,function(){
			$('#id_eci').change();
		});
		_llenarCombo('cat_condicion_per',$('#frm-alumno_mant #id_cond'),null,null,function(){
			$('#id_cond').change();
		});
			$('#frm-alumno_mant #fec_nac').val('');	
			$('#edad').text('Edad:');

			$("#id_viv1").click();
			$("#id_viv1").val('1');
			$('#div_defuncion').hide();
			$('#fec_def').attr("disabled", "disabled");
		
		$("input:radio[name='viv']").on("click", function(e) {
			if($(this).val()==0){
				$('#div_defuncion').show();
				$('#fec_def').removeAttr("disabled");
			}else {
				$('#div_defuncion').hide();
				$('#fec_def').attr("disabled", "disabled");
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
		
			$('#div_familiar_emergencia').hide();	
			
		_llenar_combo({
		url:'api/alumno/listarFamilias',
		combo:$('#id_gpf'),
		text:'nom',
		context: _URL,
		id:_id_gpf,
		function(){
			$('#id_gpf').change();
		}
		});	
		$("#frm-alumno_mant #corr").val('');
		$("#frm-alumno_mant #email_inst").val('');
		$("#frm-alumno_mant #cel").val('');
		$("#frm-alumno_mant #tlf").val('');
		$("#frm-alumno_mant #usuario").val('');
		$("#frm-alumno_mant #pass_educando").val('');
			$('#id_pro_nac').on('change',function(event){

			if ($(this).val()==null || $(this).val()==''){ 
				$('#id_dist_nac').find('option').not(':first').remove();
			}
			else{
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_nac'),
					funcion:function(){
						console.log('remove data');
						$('#id_dep_nac').removeData('id_pro_nac');
						$('#id_pro_nac').removeData('id_dist_nac');
					}
				});		 		
			}
		 
		});
		
		$('#id_dep_nac').on('change',function(event){
			
			 if($('#id_dep_nac').val()!=null){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro_nac'),
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
				 _llenar_combo({
						url:'api/comboCache/departamentos/' + $(this).val(),
						combo:$('#id_dep_nac'),
						funcion:function(){
							$('#id_dep_nac').change();
						}
					});

				_llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'));
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
		_llenarCombo('cat_pais',$('#id_pais_nac'),null,null, function(){
			$('#id_pais_nac').change();
		});	
		_llenarCombo('cat_religion',$('#id_rel'));
		_llenarCombo('cat_idioma',$('#id_idio1'));
		_llenarCombo('cat_idioma',$('#id_idio2'));
	});	