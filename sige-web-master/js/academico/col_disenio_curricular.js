//Se ejecuta cuando la pagina q lo llama le envia parametros
var _padre_madre=null;
var dc_seleccionado=null;
var niv_seleccionado=null;
var area_seleccionado=null;
function onloadPage(params){
	_onloadPage();
	var id_anio=$("#_id_anio").text();
	listar_disenio_curricular(id_anio);
	$("#div_disenio_curricular").show();
	$("#div_nivel").hide();
	$("#div_area").hide();
	$("#div_competencias_trans").hide();
	$('#frm-disenio_curricular #id_anio').val($("#_id_anio").text());
}

function listar_disenio_curricular(id_anio){
	_get('api/disenioCurricular/listar/'+id_anio,function(data){
	var id_dc_pri=data[0].id;
	dc_seleccionado=id_dc_pri;
	$('#tabla_disenio_curricular').dataTable({
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
				
				listar_niveles(id_dc_pri);
				//listar_giro_negocio(id_dc_pri);
				$("#tabla_disenio_curricular tbody tr").click(function(){  
					$("#div_disenio_curricular").show();
					$("#div_nivel").hide();
					$("#div_area").hide();
					$("#div_competencias_trans").hide();
					var id_dc=($(this).find('td:nth-child(1)').find('#id').val());
					dc_seleccionado=id_dc;
					listar_niveles(id_dc);
					//listar_giro_negocio(id_emp);
					_get('api/disenioCurricular/' + id_dc,
					function(data){
						$("#div_disenio_curricular #nom_disenio_curricular ").text(data.nom);
						console.log(data);
						_fillForm(data,$('#frm-disenio_curricular'));
					});
				});	   
				}
			});
	});
}

function listar_niveles(id_dc){
	_get('api/disenioCurricular/listarNiveles/'+id_dc,function(data){
	if(data.length>0)
	niv_seleccionado=data[0].id;
	listar_areas(niv_seleccionado);
	//var id_gir_pri=$("#tabla_giro_negocio tbody tr").find('tr:nth-child(1)').find('#id').val();
	$('#tabla_nivel').dataTable({
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
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nivel.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="local_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="local_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_competencias_transversales(niv_seleccionado);
				//$('#frm_local #id_ggn').val(id_gir);
				$("#tabla_nivel tbody tr").click(function(){  
					niv_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					listar_areas(niv_seleccionado);
					listar_competencias_transversales(niv_seleccionado);
					//listar_servicios(suc_seleccionado,id_gir_pri);
					$("#div_disenio_curricular").hide();
					$("#div_nivel").show();
					$("#div_area").hide();
					$("#div_competencias_trans").hide();
					var id_anio=$("#_id_anio").text();
					_llenar_combo({
								url:'api/disenioCurricular/listar/'+id_anio,
								//params: param,
								id: id_dc,
								combo:$('#frm-nivel #id_dcn'),
								funcion:function(){
									$('#frm-nivel #id_dcn').change();
								}
						}); 
						
					_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){
							$('#id_niv').change();
					})
					if(niv_seleccionado!=null){
						_get('api/disenioCurricular/obtenerDatosNivel/' + niv_seleccionado,
						function(data){
							console.log(data);
							_fillForm(data,$('#frm-nivel'));
							$("#div_nivel #nom_nivel").text("NIVEL "+(data.nivel.nom));
							/*_llenarCombo('ges_giro_negocio',$('#id_ggn'),data.id_ggn,null, function(){
								$('#id_ggn').change();
							});*/
							//alert(data.id_niv);
							$('#frm-nivel #id_dcn').val(data.id_dcn).change();
							$('#frm-nivel #id_niv').val(data.id_niv).change();							
						});
					}	
				});	   
				}
			});
	});
}


function listar_areas(id_dcniv){
	_get('api/disenioCurricular/listarAreas/'+id_dcniv,function(data){
	//Primer Giro de Negocio
	if(data.length>0){
		area_seleccionado=data[0].id;
	}		
	$('#tabla_areas').dataTable({
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
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.area.nom 
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

				$('#frm_area #id_dcniv').val(id_dcniv);
				//listar_servicios(id_suc_pri,id_gir_pri);
				$("#tabla_areas tbody tr").click(function(){  
					area_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					//listar_locales(id_gir);
					//listar_competencias_transversales(niv_seleccionado);
					$("#div_disenio_curricular").hide();
					$("#div_nivel").hide();
					$("#div_area").show();
					$("#div_competencias_trans").hide();
					_llenarComboURL('api/disenioCurricular/listarNivelesCombo/'+dc_seleccionado,$('#id_dcniv'),niv_seleccionado,null);
					_llenarCombo('cat_area',$('#frm_area #id_are'),null,null, function(){
						$('#frm_area #id_are').change();
					});
					if(area_seleccionado!=null){
						_get('api/disenioCurricular/obtenerDatosArea/' + area_seleccionado,
						function(data){
							console.log(data);
							_fillForm(data,$('#div_area'));
							$("#div_area #area_nom").text("AREA "+data.area.nom);
							$('#frm-area #id_dcniv').val(data.id_dcniv).change();
							$('#frm-area #id_are').val(data.id_are).change();
						});	
					}	
				});	   
				}
			});
	});
}	


function listar_competencias_transversales(id_dcniv){
	_get('api/disenioCurricular/listarCompetenciasTransversales/'+id_dcniv,function(data){
	$('#tabla_comp_trans').dataTable({
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
			//{"title":"Nivel", "data": "nom"},
			//{"title":"Local", "data": "sucursal.nom"},
			{"title":"Competencia", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.competenciaTrans.nom 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="servicio_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="servicio_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				$("#tabla_comp_trans tbody tr").click(function(){  
					var id_comp=($(this).find('td:nth-child(1)').find('#id').val());
					$("#div_disenio_curricular").hide();
					$("#div_nivel").hide();
					$("#div_area").hide();
					$("#div_competencias_trans").show();
					//_llenarComboURL('api/disenioCurricular/listarAreasCombo/'+niv_seleccionado,$('#id_dcare'),area_seleccionado,null);
					_llenarComboURL('api/disenioCurricular/listarNivelesCombo/'+dc_seleccionado,$('#frm_competencia_trans #id_dcniv'),niv_seleccionado,null);
					_llenarCombo('cat_competencia_trans',$('#id_ctran'),null,null, function(){
							$('#id_ctran').change();
					});
					if(id_comp!=null){
						_get('api/disenioCurricular/obtenerDatosCompetencia/' + id_comp,
						function(data){
							console.log(data);
							_fillForm(data,$('#frm_competencia_trans'));
							$('#div_competencias_trans #competencia_trans').text("COMPETENCIA "+data.competenciaTrans.nom);
							$('#frm_competencia_trans #id_dcniv').val(data.id_dcniv).change();
							$('#frm_competencia_trans #id_ctran').val(data.id_ctran).change();
						});	
					}	
				});	   
				}
			});
	});
}

$('#frm-disenio_curricular #btn_grabar').on('click',function(event){
		var validation = $('#frm-disenio_curricular').validate(); 
		if ($('#frm-disenio_curricular').valid()){
			_post($('#frm-disenio_curricular').attr('action') , $('#frm-disenio_curricular').serialize(),function(data) {
				$('#frm-disenio_curricular #id').val(data.result);
				listar_disenio_curricular(id_anio);
			}	
			);
		}	
		
});

$('#frm-disenio_curricular #btn_nuevo').on('click',function(event){
	$('#frm-disenio_curricular #id').val('');
	$('#frm-disenio_curricular #nom').val('');
});	

$('#frm-disenio_curricular #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/disenioCurricular/' + $('#frm-disenio_curricular #id').val(),
		context:_URL,
		success:function(){
			listar_disenio_curricular(id_anio);
			$('#frm-disenio_curricular #id').val('');
			$('#frm-disenio_curricular #nom').val('');
		}
		});
});

$('#frm-nivel #btn_grabar').on('click',function(event){
		var validation = $('#frm-nivel').validate(); 
		if ($('#frm-nivel').valid()){
			_post($('#frm-nivel').attr('action') , $('#frm-nivel').serialize(),function(data) {
				$('#frm-nivel #id').val(data.result);
				listar_niveles($('#frm-nivel #id_dcn').val());
			}	
			);
		}		
});

$('#frm-nivel #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/disenioCurricular/eliminarNivel/' + $('#frm-nivel #id').val(),
		context:_URL,
		success:function(){
			listar_niveles($('#frm-nivel #id_dcn').val());
			$('#frm-nivel #id').val('');
			$('#frm-nivel #id_niv').val('').trigger('change');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-nivel #btn_nuevo').on('click',function(event){
	$('#frm-nivel #id').val('');
	$("#frm-nivel #id_dcn").val('').trigger('change');
	$("#frm-nivel #id_niv").val('').trigger('change');
});

$('#frm_area #btn_grabar').on('click',function(event){
		var validation = $('#frm_area').validate(); 
		if ($('#frm_area').valid()){
			_post($('#frm_area').attr('action') , $('#frm_area').serialize(),function(data) {
				$('#frm_area #id').val(data.result);
				//listar_competencias_transversales($('#frm_area #id').val());
				listar_areas(niv_seleccionado);
			}	
			);
		}	
		
});

$('#frm_area #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/disenioCurricular/eliminarArea/' + $('#frm_area #id').val(),
		context:_URL,
		success:function(){
			listar_areas(niv_seleccionado);
			$('#frm_area #id').val('');
			$("#frm_area #id_dcniv").val('').trigger('change');
			$("#frm_area #id_are").val('').trigger('change');
		}
		});
});

$('#frm_area #btn_nuevo').on('click',function(event){
	$('#frm_area #id').val('');
	$("#frm_area #id_dcniv").val('').trigger('change');
	$("#frm_area #id_are").val('').trigger('change');
});

$('#frm_competencia_trans #btn_grabar').on('click',function(event){
		var validation = $('#frm_competencia_trans').validate(); 
		if ($('#frm_competencia_trans').valid()){
			_post($('#frm_competencia_trans').attr('action') , $('#frm_competencia_trans').serialize(),function(data) {
				listar_competencias_transversales(niv_seleccionado);
			}	
			);
		}	
		
});

$('#frm_competencia_trans #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/disenioCurricular/eliminarCompetenciaTransversal/' + $('#frm_competencia_trans #id').val(),
		context:_URL,
		success:function(){
			listar_competencias_transversales(niv_seleccionado);
			$('#frm_competencia_trans #id').val('');
			$('#frm_competencia_trans #id_dcare').val('').trigger('change');
			$('#frm_competencia_trans #id_ctran').val('').trigger('change');
		}
		});
});

$('#frm_competencia_trans #btn_nuevo').on('click',function(event){
	$('#frm_competencia_trans #id').val('');
	$("#frm_competencia_trans #id_dcare").val('').trigger('change');
	$("#frm_competencia_trans #id_ctran").val('').trigger('change');
});
