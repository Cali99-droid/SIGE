//Se ejecuta cuando la pagina q lo llama le envia parametros
var _padre_madre=null;
var suc_seleccionado=null;
var gir_seleccionado=null;
function onloadPage(params){
	_onloadPage();
	listar_empresas();
	$("#div_empresa").show();
	$("#div_giro_negocio").hide();
	$("#div_servicio").hide();
	$("#div_locales").hide();
}

function listar_empresas(){
	_get('api/empresa/listar',function(data){
	var id_emp_pri=data[0].id;
	$('#tabla_empresa').dataTable({
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
			{"title":"Razon Social", "data": "raz_soc"},
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				
				listar_locales(id_emp_pri);
				listar_giro_negocio(id_emp_pri);
				$("#tabla_empresa tbody tr").click(function(){  
					$("#div_empresa").show();
					$("#div_giro_negocio").hide();
					$("#div_servicio").hide();
					$("#div_locales").hide();
					var id_emp=($(this).find('td:nth-child(2)').find('#id').val());
					
					listar_locales(id_emp);
					listar_giro_negocio(id_emp);
					_get('api/empresa/' + id_emp,
					function(data){
						$("#div_empresa #nom_empresa").text("EMPRESA "+data.nom);
						console.log(data);
						var id_pro=null;
						var id_dep=null;
						var id_pais=null;
						if(data.provincia!=null){
							id_pro=data.provincia.id;
						}
						if(data.departamento!=null){
							id_dep=data.departamento.id;
						}	
						if(data.pais!=null){
							id_pais=data.pais.id;
						}	
						_fillForm(data,$('#frm-mant_empresa'));
						$('#frm-mant_empresa #id_pro').on('change',function(event){
							if ($(this).val()!=null && $(this).val()!=''){ 
								_llenar_combo({
									url:'api/comboCache/distritos/' + $(this).val(),
									combo:$('#frm-mant_empresa #id_dist'),
									id: data.id_dist,
									funcion:function(){
										console.log('remove data');
										$('#frm-mant_empresa #id_dep').removeData('id_pro');
										$('#frm-mant_empresa #id_pro').removeData('id_dist');
									}
								});	
							}
						});
						$('#frm-mant_empresa #id_dep').on('change',function(event){
						 if($('#frm-mant_empresa #id_dep').val()!=null && $('#frm-mant_empresa #id_dep').val()!=''){
							 _llenar_combo({
									url:'api/comboCache/provincias/' + $(this).val(),
									combo:$('#frm-mant_empresa #id_pro'),
									id: id_pro,
									funcion:function(){
										$('#frm-mant_empresa #id_pro').change();
									}
								}); 
						 }
						});
						$('#frm-mant_empresa #id_pais').on('change',function(event){
							 if($('#frm-mant_empresa #id_pais').val()=="193"){
								 $('#frm-mant_empresa #id_dep').removeAttr("disabled");
								 $('#frm-mant_empresa #id_pro').removeAttr("disabled");
								 $('#frm-mant_empresa #id_dist').removeAttr("disabled");
								 _llenar_combo({
										url:'api/comboCache/departamentos/' + $(this).val(),
										combo:$('#frm-mant_empresa #id_dep'),
										id: id_dep,
										funcion:function(){
											$('#frm-mant_empresa #id_dep').change();
										}
									});
							 } else {
								 $('#frm-mant_empresa #id_dep').val('');
								 $('#frm-mant_empresa #id_pro').val('');
								 $('#frm-mant_empresa #id_dist').val('');
								// $('#dir').val('');
								// $('#referencia').val('');
								 $('#frm-mant_empresa #id_dep').attr("disabled", "disabled");
								 $('#frm-mant_empresa #id_pro').attr("disabled", "disabled");
								 $('#frm-mant_empresa #id_dist').attr("disabled", "disabled");
								// $('#dir').attr("readonly", "readonly");
								// $('#referencia').attr("readonly", "readonly");
								 $('#frm-mant_empresa #id_dep').change();
							 }
						});
							_llenarCombo('cat_pais',$('#frm-mant_empresa #id_pais'),id_pais,null, function(){
							$('#frm-mant_empresa #id_pais').change();
							});	
						
						_llenarCombo('ges_trabajador',$('#id_rep_leg'),data.id_rep_leg);	
					});
				});	   
				}
			});
	});
}

function listar_locales(id_emp){
	_get('api/empresa/listarLocales/'+id_emp,function(data){
	
	suc_seleccionado=data[0].id;
	//var id_gir_pri=$("#tabla_giro_negocio tbody tr").find('tr:nth-child(1)').find('#id').val();
	$('#tabla_locales').dataTable({
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
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="local_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="local_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				
				//$('#frm_local #id_ggn').val(id_gir);
				$("#tabla_locales tbody tr").click(function(){  
					suc_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					listar_servicios(suc_seleccionado,gir_seleccionado);
					// $(this).toggleClass("selected");
					//$(this).css('background-color','blue');
					/* var selected = $(this).css('background-color','blue');
					$("#data tr").removeClass("selected");
					if(!selected)
					$(this).addClass("selected");*/
					/* $('#data tr').removeClass('highlighted');
       $(this).toggleClass('highlighted');  */
		/*var selected = $(this).hasClass("highlight");
       $("#data tr").removeClass("highlight");
	   console.log(selected);
       if(selected)
               $("#data tr").removeClass("highlight");
	 else 
		 $(this).toggleClass('highlighted');	*/
		 
					//listar_servicios(suc_seleccionado,id_gir_pri);
					$("#div_locales").show();
					$("#div_empresa").hide();
					$("#div_giro_negocio").hide();
					$("#div_servicio").hide();
					_get('api/empresa/obtenerDatosLocal/' + suc_seleccionado,
					function(data){
						console.log(data);
						_fillForm(data,$('#frm_local'));
						$("#div_locales #local_nom").text("LOCAL "+data.nom);
						/*_llenarCombo('ges_giro_negocio',$('#id_ggn'),data.id_ggn,null, function(){
							$('#id_ggn').change();
						});*/
						_llenarCombo('ges_empresa',$('#frm_local #id_emp'),data.id_emp,null, function(){
							$('#frm_local #id_emp').change();
						});
						var id_pro=null;
						var id_dep=null;
						var id_pais=null;
						if(data.provincia!=null){
							id_pro=data.provincia.id;
						}
						if(data.departamento!=null){
							id_dep=data.departamento.id;
						}	
						if(data.pais!=null){
							id_pais=data.pais.id;
						}	
						$('#frm_local #id_pro').on('change',function(event){
							if ($(this).val()!=null && $(this).val()!=''){ 
								_llenar_combo({
									url:'api/comboCache/distritos/' + $(this).val(),
									combo:$('#frm_local #id_dist'),
									id: data.id_dist,
									funcion:function(){
										console.log('remove data');
										$('#frm_local #id_dep').removeData('id_pro');
										$('#frm_local #id_pro').removeData('id_dist');
									}
								});	
							}
						});
						$('#frm_local #id_dep').on('change',function(event){
						 if($('#frm_local #id_dep').val()!=null && $('#frm_local #id_dep').val()!=''){
							 _llenar_combo({
									url:'api/comboCache/provincias/' + $(this).val(),
									combo:$('#frm_local #id_pro'),
									id: id_pro,
									funcion:function(){
										$('#frm_local #id_pro').change();
									}
								}); 
						 }
						});
						$('#frm_local #id_pais').on('change',function(event){
							 if($('#frm_local #id_pais').val()=="193"){
								 $('#frm_local #id_dep').removeAttr("disabled");
								 $('#frm_local #id_pro').removeAttr("disabled");
								 $('#frm_local #id_dist').removeAttr("disabled");
								 _llenar_combo({
										url:'api/comboCache/departamentos/' + $(this).val(),
										combo:$('#frm_local #id_dep'),
										id: id_dep,
										funcion:function(){
											$('#frm_local #id_dep').change();
										}
									});
							 } else {
								 $('#frm_local #id_dep').val('');
								 $('#frm_local #id_pro').val('');
								 $('#frm_local #id_dist').val('');
								// $('#dir').val('');
								// $('#referencia').val('');
								 $('#frm_local #id_dep').attr("disabled", "disabled");
								 $('#frm_local #id_pro').attr("disabled", "disabled");
								 $('#frm_local #id_dist').attr("disabled", "disabled");
								// $('#dir').attr("readonly", "readonly");
								// $('#referencia').attr("readonly", "readonly");
								 $('#frm_local #id_dep').change();
							 }
						});
							_llenarCombo('cat_pais',$('#frm_local #id_pais'),id_pais,null, function(){
							$('#frm_local #id_pais').change();
							});	
					});
				});	   
				}
			});
	});
}


function listar_giro_negocio(id_emp){
	_get('api/empresa/listarGiroNegocio/'+id_emp,function(data){
	//Primer Giro de Negocio
	gir_seleccionado=data[0].id;
	$('#tabla_giro_negocio').dataTable({
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
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="giro_negocio_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="giro_negocio_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				//_initCompleteDT(settings);
				//listar_locales(id_gir_pri);
				//alert(suc_seleccionado);
				listar_servicios(suc_seleccionado,gir_seleccionado);
				$('#frm-mant_giro_negocio #id_emp').val(id_emp);
				//listar_servicios(id_suc_pri,id_gir_pri);
				$("#tabla_giro_negocio tbody tr").click(function(){  
					gir_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					//listar_locales(id_gir);
					listar_servicios(suc_seleccionado,gir_seleccionado);
					$("#div_giro_negocio").show();
					$("#div_empresa").hide();
					$("#div_servicio").hide();
					$("#div_locales").hide();
					_get('api/empresa/datosGiroNegocio/' + gir_seleccionado,
					function(data){
						console.log(data);
						_fillForm(data,$('#div_giro_negocio'));
						$("#div_giro_negocio #nom_gir_neg").text("GIRO DE NEGOCIO "+data.nom);
						_llenarCombo('ges_empresa',$('#frm-mant_giro_negocio #id_emp'),data.id_emp,null, function(){
							$('#frm-mant_giro_negocio #id_emp').change();
						});
					});	
				});	   
				}
			});
	});
}	


function listar_servicios(id_suc, id_gir){
	_get('api/empresa/listarServicios/'+id_suc+'/'+id_gir,function(data){
	$('#tabla_servicios').dataTable({
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
			{"title":"Nivel", "data": "nom"},
			//{"title":"Local", "data": "sucursal.nom"},
			{"title":"Local", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.sucursal.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="servicio_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="servicio_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				$("#tabla_servicios tbody tr").click(function(){  
					var id_ser=($(this).find('td:nth-child(2)').find('#id').val());
					$("#div_locales").hide();
					$("#div_empresa").hide();
					$("#div_giro_negocio").hide();
					$("#div_servicio").show();
					//Listar Niveles
					/*_get('api/empresa/datosGiroNegocio/' + id_gir,
					function(data){
						console.log(data);
						$('#tab_niveles').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 orderCellsTop:true,
							 select: true,
					         columns : [
					                    
					        	 	//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
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
					});	*/
					_get('api/empresa/obtenerDatosServicio/' + id_ser,
					function(data){
						console.log(data);
						_fillForm(data,$('#frm_servicio'));
						$('#div_servicio #servicio_nom').text("SERVICIO "+data.nom);
						_llenarCombo('ges_giro_negocio',$('#id_gir'),data.id_gir,null, function(){
							$('#id_gir').change();
						});
						_llenarCombo('ges_sucursal',$('#frm_servicio #id_suc'),data.id_suc,null, function(){
							$('#frm_servicio #id_suc').change();
						});
						_llenarCombo('cat_nivel',$('#frm_servicio #id_niv'),data.id_niv,null, function(){
							$('#frm_servicio #id_niv').change();
						});
					});	
				});	   
				}
			});
	});
}

$('#frm-mant_empresa #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_empresa').validate(); 
		if ($('#frm-mant_empresa').valid()){
			_post($('#frm-mant_empresa').attr('action') , $('#frm-mant_empresa').serialize(),function(data) {
				$('#frm-mant_empresa #id').val(data.result);
				listar_empresas();
			}	
			);
		}	
		
});

$('#frm-mant_empresa #btn_nuevo').on('click',function(event){
	$('#frm-mant_empresa #id').val('');
	$('#frm-mant_empresa #nom').val('');
	$('#frm-mant_empresa #raz_soc').val('');
	$('#frm-mant_empresa #ruc').val('');
	$('#frm-mant_empresa #abrv').val('');
	$('#frm-mant_empresa #dir').val('');
	$('#frm-mant_empresa #tel').val('');
	$('#frm-mant_empresa #corr').val('');
	$('#frm-mant_empresa #pag_web').val('');
	$("#frm-mant_empresa #id_pais").val('').trigger('change');
	$("#frm-mant_empresa #id_dep").val('').trigger('change');
	$("#frm-mant_empresa #id_pro").val('').trigger('change');
	$("#frm-mant_empresa #id_dist").val('').trigger('change');
});	

$('#frm-mant_empresa #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/empresa/' + $('#frm-mant_empresa #id').val(),
		context:_URL,
		success:function(){
			listar_empresas();
			$('#frm-mant_empresa #id').val('');
			$('#frm-mant_empresa #nom').val('');
			$('#frm-mant_empresa #raz_soc').val('');
			$('#frm-mant_empresa #ruc').val('');
			$('#frm-mant_empresa #abrv').val('');
			$('#frm-mant_empresa #dir').val('');
			$('#frm-mant_empresa #tel').val('');
			$('#frm-mant_empresa #corr').val('');
			$('#frm-mant_empresa #pag_web').val('');
			$("#frm-mant_empresa #id_pais").val('').trigger('change');
			$("#frm-mant_empresa #id_dep").val('').trigger('change');
			$("#frm-mant_empresa #id_pro").val('').trigger('change');
			$("#frm-mant_empresa #id_dist").val('').trigger('change');
		}
		});
});

$('#frm-mant_giro_negocio #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_giro_negocio').validate(); 
		if ($('#frm-mant_giro_negocio').valid()){
			_post($('#frm-mant_giro_negocio').attr('action') , $('#frm-mant_giro_negocio').serialize(),function(data) {
				$('#frm-mant_giro_negocio #id').val(data.result);
				listar_giro_negocio($('#frm-mant_giro_negocio #id_emp').val());
			}	
			);
		}		
});

$('#frm-mant_giro_negocio #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/empresa/eliminarGiroNegocio/' + $('#frm-mant_giro_negocio #id').val(),
		context:_URL,
		success:function(){
			listar_giro_negocio($('#frm-mant_giro_negocio #id_emp').val());
			$('#frm-mant_giro_negocio #id').val('');
			$('#frm-mant_giro_negocio #nom').val('');
			$('#frm-mant_giro_negocio #des').val('');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-mant_giro_negocio #btn_nuevo').on('click',function(event){
	$('#frm-mant_giro_negocio #id').val('');
	$('#frm-mant_giro_negocio #nom').val('');
	$('#frm-mant_giro_negocio #des').val('');
	$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
});

$('#frm_local #btn_grabar').on('click',function(event){
		var validation = $('#frm_local').validate(); 
		if ($('#frm_local').valid()){
			_post($('#frm_local').attr('action') , $('#frm_local').serialize(),function(data) {
				$('#frm_local #id').val(data.result);
				listar_locales($('#frm_local #id_emp').val());
			}	
			);
		}	
		
});

$('#frm_local #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/empresa/eliminarSucursal/' + $('#frm_local #id').val(),
		context:_URL,
		success:function(){
			listar_locales($('#frm_local #id_ggn').val());
			$('#frm_local #id').val('');
			$('#frm_local #nom').val('');
			$('#frm_local #abrv').val('');
			$('#frm_local #cod').val('');
			$('#frm_local #dir').val('');
			$('#frm_local #tel').val('');
			$('#frm_local #correo').val('');
			$('#frm_local #pag_web').val('');
			$('#frm_local #tot_au').val('');
			$('#frm_local #tot_ofi').val('');
			$("#frm_local #id_pais").val('').trigger('change');
			$("#frm_local #id_dep").val('').trigger('change');
			$("#frm_local #id_pro").val('').trigger('change');
			$("#frm_local #id_dist").val('').trigger('change');
			//$("#frm_local #id_ggn").val('').trigger('change');
		}
		});
});

$('#frm_local #btn_nuevo').on('click',function(event){
	$('#frm_local #id').val('');
	$('#frm_local #nom').val('');
	$('#frm_local #abrv').val('');
	$('#frm_local #cod').val('');
	$('#frm_local #dir').val('');
	$('#frm_local #tel').val('');
	$('#frm_local #correo').val('');
	$('#frm_local #pag_web').val('');
	$('#frm_local #tot_au').val('');
	$('#frm_local #tot_ofi').val('');
	$("#frm_local #id_pais").val('').trigger('change');
	$("#frm_local #id_dep").val('').trigger('change');
	$("#frm_local #id_pro").val('').trigger('change');
	$("#frm_local #id_dist").val('').trigger('change');
	$("#frm_local #id_ggn").val('').trigger('change');
});

$('#frm_servicio #btn_grabar').on('click',function(event){
		var validation = $('#frm_servicio').validate(); 
		if ($('#frm_servicio').valid()){
			_post($('#frm_servicio').attr('action') , $('#frm_servicio').serialize(),function(data) {
				listar_servicios(suc_seleccionado,gir_seleccionado);
			}	
			);
		}	
		
});

$('#frm_servicio #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/empresa/eliminarServicio/' + $('#frm_servicio #id').val(),
		context:_URL,
		success:function(){
			listar_servicios($('#frm_servicio #id_suc').val());
			$('#frm_servicio #id').val('');
			$('#frm_servicio #nom').val('');
			$('#frm_servicio #des').val('');
			//$("#frm_servicio #id_suc").val('').trigger('change');
			//$("#frm_servicio #id_niv").val('').trigger('change');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm_servicio #btn_nuevo').on('click',function(event){
	$('#frm_servicio #id').val('');
	$('#frm_servicio #nom').val('');
	$('#frm_servicio #des').val('');
	_llenarCombo('ges_sucursal',$('#frm_servicio #id_suc'),null,null, function(){
		$('#frm_servicio #id_suc').change();
	});
	_llenarCombo('cat_nivel',$('#frm_servicio #id_niv'),null,null, function(){
		$('#frm_servicio #id_niv').change();
	});
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null, function(){
		$('#id_gir').change();
	});
	$("#frm_servicio #id_suc").val('').trigger('change');
	$("#frm_servicio #id_niv").val('').trigger('change');
	$("#frm_servicio #id_gir").val('').trigger('change');
});
