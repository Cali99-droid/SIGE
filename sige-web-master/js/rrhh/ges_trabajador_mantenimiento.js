//Se ejecuta cuando la pagina q lo llama le envia parametros
var _padre_madre=null;
var _es_padre_madre=true;
var _PARENTESCO_PAPA = "2";
var _PARENTESCO_MAMA = "1";
function onloadPage(params){
	_onloadPage();
	//Por defecto deshabilitado los tabs de familiares e hijos
	$("#tabs_trabajador").tabs({
        disabled: [1]
    })
	deshabilitar_campos_trabajador();
	//botones grabar y eliminar desactivados
	$('#btn_grabar').attr('disabled','disabled');
	$('#btn_eliminar').attr('disabled','disabled');
	$('#id_par').on('change',function(event){
		if($('#id_par').val()==_PARENTESCO_PAPA){
			 $("#frm-familiar_mant #id_gen").val(1).change();
		} else if($('#id_par').val()==_PARENTESCO_MAMA){
		} else if($('#id_par').val()==_PARENTESCO_MAMA){
			$("#frm-familiar_mant #id_gen").val(0).change();
		}
		
	});
	
	$('#con_indf').on( 'click', function() {
    if( $(this).is(':checked') ){
        // Hacer algo si el checkbox ha sido seleccionado
		$('#con_indf').val(1);
		//deshabilito fecha fin
		$('#fec_fin').val('');
		$('#fec_fin').attr("disabled", "disabled");
    } else {
        // Hacer algo si el checkbox ha sido deseleccionado
	   $('#con_indf').val('');
       $('#fec_fin').removeAttr('disabled');
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
	//_llenarCombo('ges_giro_negocio',$('#id_gir'));
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

	$("#id_tdc").on('change', function(){
		if($(this).val()==1){
			 $("#nro_doc").attr('maxlength','8');
		}else if($(this).val()==2){
			 $("#nro_doc").attr('maxlength','11');
		}
	});
	
	//Onchange
	_llenarComboURL('api/trabajador/listarRegimenLaboral', $('#id_reg'),null, null, function() {
	});
							
	_llenarComboURL('api/trabajador/listarModalidadTrabajo', $('#id_mod'),null, null, function() {
	});
	
	_llenarComboURL('api/trabajador/listarCategoriaOcupacional', $('#id_cat'),null, null, function() {
		
	});
	
	_llenarComboURL('api/trabajador/listarPuestoTrabajador', $('#id_pue'),null, null, function() {
	});
	
	_llenarCombo('cat_nivel',$('#id_niv_tra'),null,null, function(){
	});
	
	$('#id_emp').on('change',function(event){
		if($('#id_emp').val()!=null && $('#id_emp').val()!=''){
			_llenarComboURL('api/trabajador/listarGirosNegocio/'+$('#id_emp').val(), $('#id_gir'),null, null, function() {
		});
		}	
		
	});

	_llenarCombo('ges_empresa',$('#id_emp'),null,null, function(){
		$('#id_emp').change();
	});
	
	/*if( $('.con_indf').prop('checked') ) {
    alert('Seleccionado');
}*/

/*if( $('#con_indf').attr('checked') ) {
    alert('Seleccionado');
}*/

	
	
	$('#id_den').on('change',function(event){
		var param={id_lcarr:$('#id_lin_carr').val(), id_cden:$('#id_den').val(), id_cocu:$('#id_cat').val(), id_anio:$('#_id_anio').text()};
		/*_llenarComboURL('api/trabajador/listarRemuneracion', $('#id_gir'),param, null, function() {
		});*/
		
		_llenar_combo({
			url:'api/trabajador/listarRemuneracion',
			combo:$('#id_rem_cat'),
			params: param,
			context:_URL,
			text:'value',
			//id:id_per_sel,
			funcion:function(){
				//$('#id_per').change();
			}
		});
	});
	
	$('#id_cat').on('change',function(event){
		var param={id_lcarr:$('#id_lin_carr').val(), id_cden:$('#id_den').val(), id_cocu:$('#id_cat').val(), id_anio:$('#_id_anio').text()};
		/*_llenarComboURL('api/trabajador/listarRemuneracion', $('#id_gir'),param, null, function() {
		});*/
		
		_llenar_combo({
			url:'api/trabajador/listarRemuneracion',
			combo:$('#id_rem_cat'),
			params: param,
			context:_URL,
			text:'value', 
			//id:id_per_sel,
			funcion:function(){
				//$('#id_per').change();
			}
		});
	});
	
	$('#id_lin_carr').on('change',function(event){
		var param={id_lcarr:$('#id_lin_carr').val(), id_cden:$('#id_den').val(), id_cocu:$('#id_cat').val(), id_anio:$('#_id_anio').text()};
		/*_llenarComboURL('api/trabajador/listarRemuneracion', $('#id_gir'),param, null, function() {
		});*/
		
		_llenar_combo({
			url:'api/trabajador/listarRemuneracion',
			combo:$('#id_rem_cat'),
			params: param,
			context:_URL,
			text:'value',
			//id:id_per_sel,
			funcion:function(){
				//$('#id_per').change();
			}
		});
	});
	
	$('#id_lin_carr').on('change',function(event){
		var param ={id_anio:$('#_id_anio').text(), id_lcarr: $('#id_lin_carr').val()};
		_llenar_combo({
			url:'api/trabajador/listarDenominacionTrabajador',
			combo:$('#id_den'),
			params: param,
			context:_URL,
			text:'value',
			//id:id_per_sel,
			funcion:function(){
				$('#id_den').change();
			}
		});
		//_llenarComboURL('api/trabajador/listarDenominacionTrabajador', $('#id_den'),param, null, function() {
		//});
		
	});	
	
	_llenarComboURL('api/trabajador/listarLineaCarrera', $('#id_lin_carr'),null, null, function() {
		$('#id_lin_carr').change();
	});
	
	
	/*_llenarComboURL('api/trabajador/listarRemuneracion', $('#id_rem'),null, null, function() {
	});*/
	
	_llenarComboURL('api/trabajador/listarFrecuenciaPago', $('#id_frec_pag'),null, null, function() {
		$('#id_frec_pag').change();
	});
	
	$('#id_prue').on('change',function(event){
		//Buscamos la fecha fin
		var fec_ini = $('#fec_ini').val();
		console.log(fec_ini);
		//08/04/2022
		var id_prue=$('#id_prue').val();

		var fecha_split = fec_ini.split('/');

		var nueva_fecha = new Date(fecha_split[2]+'-'+fecha_split[1]+'-'+fecha_split[0]);
		var fechaB = new Date(nueva_fecha.setMonth(nueva_fecha.getMonth() + parseInt(id_prue)));

		console.log(fechaB.toLocaleDateString());
		$('#fec_fin_prue').val(fechaB.toLocaleDateString());
		
	});	
	
	_llenarComboURL('api/trabajador/listarPeriodoPrueba', $('#id_prue'),null, null, function() {
	});
	
	_llenarComboURL('api/trabajador/listarAnio', $('#id_anio_con'),null, null, function() {
	});
	
	_llenarCombo('cat_grado_instruccion',$('#id_gin'));
	//Calcula la fecha fin de prueba
	/*var nro_meses=
		_get('api/trabajador/calcularFechaPrueba/'+,function(data){
			
		});	*/
}


	function llenarTabla(valor){
				var param={tipBusqueda:valor, id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_suc: $('#id_suc').val(), id_niv:$('#id_niv').val()};
				_get('api/trabajador/listarTodosTrabajadores',function(data){
					$('#tabla-trabajadores').dataTable({
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
							{"title":"Trabajador", "data": "label"},
	    	        ],
	    	        "initComplete": function( settings ) {
					_initCompleteDT(settings);
					$("#total").text(data.length);
					$("#tabla-trabajadores tbody tr").click(function(){
					$("#tabs_trabajador").tabs("enable",1);
					$("#tabs_trabajador").tabs("enable",2);
					//Limpiar los campos
					limpiar_campos_trabajador();
	
					$("#id_anio").val($('#_id_anio').text());
					$(this).addClass('selected').siblings().removeClass('selected');    
					   var codigo=$(this).find('td:nth-child(1)').html();
					   _get('api/trabajador/obtenerDatosTrabajador/'+codigo,function(data){
						   habilitar_campos_trabajador();
						   $("#frm-trabajador_mant #btn_grabar").removeAttr('disabled');
						   $("#frm-trabajador_mant #btn_eliminar").removeAttr('disabled');
						   $("#frm-trabajador_mant #trabajador").text(data.trabajador);
						   $("#frm-contrato_mant #trabajador").text(data.trabajador);
						   $("#frm-datos_academicos_mant #trabajador").text(data.trabajador);
						  _fillForm(data, $('#frm-trabajador_mant'));
						   //$("#frm-trabajador_mant #id_per").val(data.id);
						  /* _llenarComboURL('api/trabajador/listarPersonas', $('#id_per'),
							data.id, null, function() {
								$('#id_per').change();
							});*/
							//_llenarCombo('cat_grado_instruccion',$('#id_gin'),data.id_gin);

							if(data.hijos==null){
									$("#id_hijo1").click();
									$("#id_hijo1").attr("checked",true);
									$('#id_hijo1').val(0);
									$('#id_hijo1').change();
									$('#nrohijos').hide();	
							} else{
									if(data.hijos=='1'){
									$("#id_hijo2").click();
									$("#id_hijo2").attr("checked",true);
									$('#id_hijo2').val(1);
									$('#id_hijo2').change();
									$('#nrohijos').show();	
									}
									if(data.viv=='0'){
									$("#id_hijo1").click();
									$("#id_hijo1").attr("checked",true);
									$('#id_hijo1').val(0);
									$('#id_hijo1').change();
									$('#nrohijos').hide();	
									}
							}
						   $("#frm-trabajador_mant #id").val(data.id_tra);
						   $("#frm-contrato_mant #id_tra").val(data.id_tra);
						   $("#frm-datos_academicos_mant #id_tra").val(data.id_tra);
						  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						  /* $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
						   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
						   $("#frm-familiar_mant #nom").val(data.nom);
						   $("#frm-familiar_mant #prof").val(data.prof);
						   $("#frm-familiar_mant #ocu").val(data.ocu);*/
							if(data.id_gen!=null){
									   $("#frm-trabajador_mant #id_gen").val(data.id_gen); 
									   $("#frm-trabajador_mant #id_gen").change();
							}

								_llenarCombo('cat_tipo_documento',$('#id_tdc'), data.id_tdc,null,function(){
									$('#id_tdc').change();
								});	
								_llenarCombo('cat_est_civil',$('#id_eci'), data.id_eci,null,function(){
									$('#id_eci').change();
								});
								var fec_nac = data.fec_nac;
								if(fec_nac){
									
									var arrFec_nac = fec_nac.split('-');
									$('#fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
									var fecha_actual = new Date();
									var anio=fecha_actual.getFullYear();
									var edad=anio-arrFec_nac[0];
									$('#frm-trabajador_mant #edad').text('Edad:'+edad);
								}
							if(data.id_nac==null){
								_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
							} else {
								_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
							}
							
							$('#id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist_viv'),
										id:data.id_dist_viv,
										funcion:function(){
											console.log('remove data');
											$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#id_dep').on('change',function(event){
								
								 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
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
							_llenarCombo('cat_departamento',$('#id_dep'),data.id_dep,null, function(){
								$('#id_dep').change();
							});
							
						   listarContratos(data.id_tra);
						   listarGradosAcademicos(data.id_tra);
						  //borrar de awui para bajo

					   });
					   
					   $('#frm-familiar_mant #btn_nuevo').click();
					   limpiar_campos_contrato();
					   
					}
					);

	    			 }
			});
		},param);	
	}
	
	
$('#frm-trabajador_mant #btn_nuevo').on('click',function(event){
		habilitar_campos_trabajador();
		limpiar_campos_trabajador();
		$("#frm-contrato_mant #trabajador").text('');
		$("#frm-datos_academicos_mant #trabajador").text('');
		$('#tabla_contratos').html('');
		_llenarCombo('cat_tipo_documento',$('#id_tdc'), null,null,function(){
			$('#id_tdc').change();
		});	
		_llenarCombo('cat_est_civil',$('#id_eci'), null,null,function(){
			$('#id_eci').change();
		});
		$('#id_pro').on('change',function(event){

			if ($(this).val()!=null && $(this).val()!=''){ 
			/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
			}
			else{*/
				_llenar_combo({
					url:'api/comboCache/distritos/' + $(this).val(),
					combo:$('#id_dist_viv'),
					funcion:function(){
						console.log('remove data');
						$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
						$('#id_pro').removeData('id_dist');//eliminar campos auxiliares
					}
				});		 		
			}
		 
		});
							
		$('#id_dep').on('change',function(event){
			 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){
				 _llenar_combo({
						url:'api/comboCache/provincias/' + $(this).val(),
						combo:$('#id_pro'),
						funcion:function(){
							$('#id_pro').change();
						}
					}); 
			 }
		});
		_llenarCombo('cat_departamento',$('#id_dep'),null,null, function(){
			$('#id_dep').change();
		});	
		
		_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
		
		
		$('#nrohijos').hide();	
		$("#id_hijo1").click();
		$("#id_hijo1").val('0');						
								
		/*$("input:radio[name='id_tra_rem']").on("click", function(e) {
			var valor = $(this).val();
			if(valor==0){
				$("#div_trabajo").css('display','none');
			} else 	if(valor==1){
				$("#div_trabajo").css('display','block');
			}
		});*/
		
		$("input:radio[name='hijos']").on("click", function(e) {
			if($(this).val()==0){
				$('#nrohijos').hide();	
				//$('#frm-familiar_mant #div_defuncion').show();
				//$('#fec_def').removeAttr("disabled");
			}else {
				$('#nrohijos').show();	
				//$('#frm-familiar_mant #div_defuncion').hide();
				//$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		});
		/*$('#id_per').on('change',function(event){
				var id_per=$("#id_per").val();
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
						   $("#frm-trabajador_mant #id_per").val(data.id);
						   $("#frm-trabajador_mant #id").val('');
						  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						  /* $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
						   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
						   $("#frm-familiar_mant #nom").val(data.nom);
						   $("#frm-familiar_mant #prof").val(data.prof);
						   $("#frm-familiar_mant #ocu").val(data.ocu);*/
		/*					if(data.id_gen!=null){
									   $("#frm-trabajador_mant #id_gen").val(data.id_gen); 
									   $("#frm-trabajador_mant #id_gen").change();
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
									$('#frm-trabajador_mant #edad').text('Edad:'+edad);
								}
							if(data.id_nac==null){
								_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
							} else {
								_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
							}
							
							$('#id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
				/*					_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist_viv'),
										id:data.id_dist_viv,
										funcion:function(){
											console.log('remove data');
											$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#id_dep').on('change',function(event){
								
								 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
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
							_llenarCombo('cat_departamento',$('#id_dep'),data.id_dep,null, function(){
								$('#id_dep').change();
							});
						} else{
							//_familiar_existe=false;
						}	 				
				}});
				}	
		});	*/
		/*_llenarComboURL('api/trabajador/listarPersonas', $('#id_per'),
		null, null, function() {
			$('#id_per').change();
		});*/
		$('#btn_grabar').removeAttr('disabled');
			
});

$('#frm-trabajador_mant #btn_buscar').on('click',function(event){
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
								   $("#frm-trabajador_mant #id_per").val(id_per);
								   
								  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
									_fillForm(data, $('#frm-trabajador_mant'));
									$("#frm-trabajador_mant #id").val('');
									if(data.id_gen!=null){
											   $("#frm-trabajador_mant #id_gen").val(data.id_gen); 
											   $("#frm-trabajador_mant #id_gen").change();
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
											$('#frm-trabajador_mant #edad').text('Edad:'+edad);
										}
									if(data.id_nac==null){
										_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
									} else {
										_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
									}
									
									$('#id_pro').on('change',function(event){

										if ($(this).val()!=null && $(this).val()!=''){ 
										/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
										}
										else{*/
											_llenar_combo({
												url:'api/comboCache/distritos/' + $(this).val(),
												combo:$('#id_dist_viv'),
												id:data.id_dist_viv,
												funcion:function(){
													console.log('remove data');
													$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
													$('#id_pro').removeData('id_dist');//eliminar campos auxiliares
												}
											});		 		
										}
									 
									});
									
									$('#id_dep').on('change',function(event){
										
										 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
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
									_llenarCombo('cat_departamento',$('#id_dep'),data.id_dep,null, function(){
										$('#id_dep').change();
									});
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

$('#frm-trabajador_mant #btn_grabar').on('click',function(event){
		var validation = $('#frm-trabajador_mant').validate(); 
		if ($('#frm-trabajador_mant').valid()){
		_POST({form:$('#frm-trabajador_mant'),
			  context:_URL,
			  msg_type:'notification',
			  msg_exito : 'SE REGISTRO AL TRABAJADOR CORRECTAMENTE.',
			  success:function(data){
				  $('#frm-trabajador_mant #id').val(data.result.id);
				   $('#frm-datos_academicos_mant #id_tra').val(data.result.id);
				  $('#frm-familiar_mant #id_per').val(data.result.id_per).change();
				  $('#frm-trabajador_mant #trabajador').text(data.result.trabajador);
				  $('#frm-contrato_mant #trabajador').text(data.result.trabajador);
				  valor=$('input[name="tip_busqueda"]:checked').val();
				  llenarTabla(valor);
				  $("#tabs_trabajador").tabs("enable",1);
				  $("#tabs_trabajador").tabs("enable",2);
				 }, 
				 error:function(data){
					 _alert_error(data.msg);
					 //$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
					 //$('#nro_dni').focus();
				}
			}); 
		}	
		
});

$('#frm-trabajador_mant #btn_eliminar').on('click',function(event){
	_DELETE({url:'api/trabajador/' + $('#frm-trabajador_mant #id').val(),
			success:function(){
			limpiar_campos_trabajador();
			var valor=$('input[name="tip_busqueda"]:checked').val();
			llenarTabla(valor);
	}
	});
});	

function listarContratos(id_tra){
	var param = {id_tra:id_tra};
	_GET({ url: 'api/contratoTrabajador/listarContratosTrabajador',
		context: _URL,
	    params:param,
	    success:
		function(data){
			$('#tabla_contratos').dataTable({
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
							{"title":"Número Contrato", "data": "num_con"},
							{"title":"Fecha Inicio", "render":function ( data, type, row,meta ) { 
									return  _parseDate(row.fec_ini);
								} 
							},
							{"title":"Fecha Fin", "render":function ( data, type, row,meta ) {
									if(row.fec_fin!=null){
										return  _parseDate(row.fec_fin);
									} else {
										return  '';
									}			
									
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
							}}
						],
						"initComplete": function( settings ) {
						_initCompleteDTSB(settings);
						$("#total").text(data.length);
						$("#tabla_familiares tbody tr").click(function(){
						$("#id_anio").val($('#_id_anio').text());
						   $(this).addClass('selected').siblings().removeClass('selected');    
						   var nro=$(this).find('td:nth-child(1)').html();

				}
				);

				 }
			});
		}
	});
}	

function listarGradosAcademicos(id_tra){
	var param = {id_tra:id_tra};
	_GET({ url: 'api/trabajador/listarGradosAcademicos',
		context: _URL,
	    params:param,
	    success:
		function(data){
			$('#tabla_grados_academicos').dataTable({
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
							{"title":"Grado Instrucción", "data": "gradoInstruccion.nom"},
							{"title":"Profesión", "data": "carrera"},
							/*{"title":"Fecha Egreso", "render":function ( data, type, row,meta ) { 
									return  _parseDate(row.fec_egre);
								} 
							},*/
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> '+ 
								'<a href="#" data-id="' + row.id + '" onclick="grado_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
						],
						"initComplete": function( settings ) {
						_initCompleteDTSB(settings);
						$("#total").text(data.length);

				 }
			});
		}
	});
}	

$('#frm-contrato_mant #btn_grabar').on('click',function(event){
	var validation = $('#frm-contrato_mant').validate(); 
	if ($('#frm-contrato_mant').valid()){
		_POST({form:$('#frm-contrato_mant'),
						  context:_URL,
						  msg_type:'notification',
						  msg_exito : 'SE REGISTRO EL CONTRATO CORRECTAMENTE.',
						  success:function(data){
						  listarContratos($('#frm-contrato_mant #id_tra').val());
						 }, 
						 error:function(data){
							 _alert_error(data.msg);
							 //$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
							 //$('#nro_dni').focus();
						}
					}); 
		
	}	
});

$('#frm-contrato_mant #btn_eliminar').on('click',function(event){
	if($('#frm-contrato_mant #id').val()==null){
		alert('No existe un contrato seleccionado');
	} else {
		contrato_eliminar($('#frm-contrato_mant #id').val());
	}	
});
function contrato_editar(id_con){
	_get('api/contratoTrabajador/' + id_con,
	function(data){
		_fillForm(data, $('#frm-contrato_mant'));
		$("#fec_ini").val(_parseDate(data.fec_ini));
		if(data.fec_fin!=null){
		$("#fec_fin").val(_parseDate(data.fec_fin));
		}
		$("#fec_fin_prue").val(_parseDate(data.fec_fin_prue));
		//$("#id_emp").trigger('change');
		//$("#id_gir").val(data.id_gir).trigger('change');
		//$('#id_lin_carr').on('change',function(event){
			$("#id_rem").val(data.remuneracion.id).trigger('change');
			$("#id_den").val(data.denominacion.id).trigger('change');
		//});
		//});
		//$("#id_gir").val(data.id_gir).trigger('change');
		$("#id_lin_carr").change();
		if(data.con_indf=='1'){
			$('#con_indf').click();
			//$("#con_indf").attr("checked",true);
			//$("#con_indf").val(1);
		} else {
			$("#con_indf").attr("checked",false);
			$("#con_indf").val(1);
		}	
		//alert(data.denominacion.id);
		//$("#id_lin_carr").val(data.denominacion.id).trigger('change');
		
		listarContratos($('#frm-contrato_mant #id_tra').val());
	});	
}	

function contrato_eliminar(id_con){
	_DELETE({url:'api/contratoTrabajador/' + id_con,
			success:function(){
			listarContratos($('#frm-contrato_mant #id_tra').val());
	}
	});
}

$('#frm-contrato_mant #btn_nuevo').on('click',function(event){
	limpiar_campos_contrato();
});	

$('#frm-datos_academicos_mant #btn_nuevo').on('click',function(event){
	limpiar_campos_grado();
});	

$('#frm-datos_academicos_mant #btn_grabar').on('click',function(event){
	var validation = $('#frm-datos_academicos_mant').validate(); 
	if ($('#frm-datos_academicos_mant').valid()){
		_POST({form:$('#frm-datos_academicos_mant'),
						  context:_URL,
						  msg_type:'notification',
						  msg_exito : 'SE REGISTRO EL GRADO ACADÉMICO CORRECTAMENTE.',
						  success:function(data){
						  listarGradosAcademicos($('#frm-datos_academicos_mant #id_tra').val());
						  limpiar_campos_grado();
						 }, 
						 error:function(data){
							 _alert_error(data.msg);
							 //$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
							 //$('#nro_dni').focus();
						}
					}); 
		
	}	
});

function grado_eliminar(id_gra){
	_DELETE({url:'api/trabajador/eliminarGrado/' + id_gra,
			success:function(){
			listarGradosAcademicos($('#frm-datos_academicos_mant #id_tra').val());
	}
	});
}



$("#frm-trabajador_mant #nro_doc").on('blur',function(){
			if($("#frm-trabajador_mant #nro_doc").val()!=null && $("#frm-trabajador_mant #nro_doc").val()!=""){
				var id_fam=0;
				nro_doc=$("#frm-trabajador_mant #nro_doc").val();
			   _get('api/trabajador/obtenerDatosPersonaxDNI/'+nro_doc,function(data){
						  // habilitar_campos_trabajador();
						 //  $("#frm-trabajador_mant #btn_grabar").removeAttr('disabled');
						 //  $("#frm-trabajador_mant #btn_eliminar").removeAttr('disabled');
						   $("#frm-trabajador_mant #trabajador").text(data.trabajador);
						   $("#frm-contrato_mant #trabajador").text(data.trabajador);
						   $("#frm-datos_academicos_mant #trabajador").text(data.trabajador);
						  _fillForm(data, $('#frm-trabajador_mant'));
						   //$("#frm-trabajador_mant #id_per").val(data.id);
						   //Aqui lo nuevo
						   $('#id_per').val(data.id);
						   $("#frm-trabajador_mant #id").val('');
						   /*llenarComboURL('api/trabajador/listarPersonas', $('#id_per'),
							data.id, null, function() {
								$('#id_per').change();
							});
							_llenarCombo('cat_grado_instruccion',$('#id_gin'),data.id_gin);*/

						  // $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						  /* $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
						   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
						   $("#frm-familiar_mant #nom").val(data.nom);
						   $("#frm-familiar_mant #prof").val(data.prof);
						   $("#frm-familiar_mant #ocu").val(data.ocu);*/
							if(data.id_gen!=null){
									   $("#frm-trabajador_mant #id_gen").val(data.id_gen); 
									   $("#frm-trabajador_mant #id_gen").change();
							}

							$('#id_tdc').val(data.id_tdc).trigger('change');
							$('#id_eci').val(data.id_eci).trigger('change');
								/*_llenarCombo('cat_tipo_documento',$('#id_tdc'), data.id_tdc,null,function(){
									$('#id_tdc').change();
								});	
								_llenarCombo('cat_est_civil',$('#id_eci'), data.id_tdc,null,function(){
									$('#id_eci').change();
								});*/
								var fec_nac = data.fec_nac;
								if(fec_nac){
									
									var arrFec_nac = fec_nac.split('-');
									$('#fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
									var fecha_actual = new Date();
									var anio=fecha_actual.getFullYear();
									var edad=anio-arrFec_nac[0];
									$('#frm-trabajador_mant #edad').text('Edad:'+edad);
								}
							if(data.id_nac==null){
								$('#id_eci').val(69).trigger('change');
								//_llenarCombo('cat_nacionalidad',$('#id_nac'), 69);
							} else {
								$('#id_nac').val(data.id_nac).trigger('change');
								//_llenarCombo('cat_nacionalidad',$('#id_nac'), data.id_nac);
							}
							$('#id_dist_viv').val(data.id_dist_viv).trigger('change');
							$('#id_pro').val(data.id_pro).trigger('change');
							$('#id_dep').val(data.id_dep).trigger('change');
							/*$('#id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
							/*		_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist_viv'),
										id:data.id_dist_viv,
										funcion:function(){
											console.log('remove data');
											$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#id_dep').on('change',function(event){
								
								 if($('#id_dep').val()!=null && $('#id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
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
							_llenarCombo('cat_departamento',$('#id_dep'),data.id_dep,null, function(){
								$('#id_dep').change();
							});
							*/
							
						  //borrar de awui para bajo

					   });
			}
		});
	

function deshabilitar_campos_trabajador(){
	$('#frm-trabajador_mant #btn_buscar').attr('disabled', 'disabled');
	$("#nom").attr('readonly','readonly');
	$("#ape_pat").attr('readonly','readonly');
	$("#ape_mat").attr('readonly','readonly');
	$("#dir").attr('readonly','readonly');
	$("#corr").attr('readonly','readonly');
	$("#cel").attr('readonly','readonly');
	$("#nro_doc").attr('readonly','readonly');
	$("#fec_nac").attr('readonly','readonly');
	//$("#carrera").attr('readonly','readonly');
	$("#tlf").attr('readonly','readonly');
	$("#face").attr('readonly','readonly');
	$("#istrg").attr('readonly','readonly');
	$("#twitter").attr('readonly','readonly');
	$("#tik_tok").attr('readonly','readonly');
	$("#email_inst").attr('readonly','readonly');
	$("#id_dep").attr("disabled", "disabled");
	$("#id_pro").attr("disabled", "disabled");
	$("#id_dist_viv").attr("disabled", "disabled");
	$("#id_eci").attr("disabled", "disabled");
	$("#id_tdc").attr("disabled", "disabled");
	$("#id_nac").attr("disabled", "disabled");
	$("#id_gen").attr("disabled", "disabled");
	$("#id_per").attr("disabled", "disabled");
	//$("#id_gin").attr("disabled", "disabled");
}

function habilitar_campos_trabajador(){
	$('#frm-trabajador_mant #btn_buscar').removeAttr('disabled');
	$("#nom").removeAttr('readonly');
	$("#ape_pat").removeAttr('readonly');
	$("#ape_mat").removeAttr('readonly');
	$("#dir").removeAttr('readonly');
	$("#corr").removeAttr('readonly');
	$("#cel").removeAttr('readonly');
	$("#nro_doc").removeAttr('readonly');
	$("#fec_nac").removeAttr('readonly');
	//$("#carrera").removeAttr('readonly');
	$("#tlf").removeAttr('readonly');
	$("#face").removeAttr('readonly');
	$("#istrg").removeAttr('readonly');
	$("#twitter").removeAttr('readonly');
	$("#tik_tok").removeAttr('readonly');
	$("#email_inst").removeAttr('readonly');
	$("#id_dep").removeAttr('disabled');
	$("#id_pro").removeAttr('disabled');
	$("#id_dist_viv").removeAttr('disabled');
	$("#id_eci").removeAttr('disabled');
	$("#id_tdc").removeAttr('disabled');
	$("#id_nac").removeAttr('disabled');
	$("#id_gen").removeAttr('disabled');
	$("#id_per").removeAttr('disabled');
	//$("#id_gin").removeAttr('disabled');
}

function limpiar_campos_trabajador(){
	$("#frm-trabajador_mant #trabajador").text('');
	$("#frm-trabajador_mant #id").val('');
	$("#nom").val('');
	$("#ape_pat").val('');
	$("#ape_mat").val('');
	$("#dir").val('');
	$("#corr").val('');
	$("#cel").val('');
	$("#nro_doc").val('');
	$("#fec_nac").val('');
	//$("#carrera").val('');
	$("#tlf").val('');
	$("#face").val('');
	$("#istrg").val('');
	$("#twitter").val('');
	$("#tik_tok").val('');
	$("#email_inst").val('');
	$("#id_dep").val('').trigger('change');
	$("#id_pro").val('').trigger('change');
	$("#id_dist_viv").val('').trigger('change');
	$("#id_eci").val('').trigger('change');
	$("#id_tdc").val('').trigger('change');
	$("#id_nac").val('').trigger('change');
	$("#id_gen").val('').trigger('change');
	$("#id_per").val('').trigger('change');
	//$("#id_gin").val('').trigger('change');
}

function limpiar_campos_contrato(){
	$("#frm-contrato_mant #id").val('');
	$('#id_anio_con').val('').trigger('change');
	$('#id_emp').val('').trigger('change');
	$('#id_gir').val('').trigger('change');
	$('#num_con').val('');
	$('#id_reg').val('').trigger('change');
	$('#id_mod').val('').trigger('change');
	$('#id_cat').val('').trigger('change');
	$('#id_pue').val('').trigger('change');
	$('#id_niv_tra').val('').trigger('change');
	$('#fec_ini').val('');
	$('#fec_fin').val('');
	$("#con_indf").attr("checked",false);
	$("#con_indf").val('');
	$('#fec_fin_prue').val('');
	$('#id_lin_carr').val('').trigger('change');
	$('#id_den').val('').trigger('change');
	$('#id_rem').val('').trigger('change');
	$('#id_frec_pag').val('').trigger('change');
}	

function limpiar_campos_grado(){
	$("#frm-datos_academicos_mant #id").val('');
	$('#id_gir').val('').trigger('change');
	$('#carrera').val('');
	$('#fec_egre').val('');
}	


