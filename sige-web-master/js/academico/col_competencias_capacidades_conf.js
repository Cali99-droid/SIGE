//Se ejecuta cuando la pagina q lo llama le envia parametros
var _padre_madre=null;
var com_seleccionado=null;
var cap_seleccionado=null;
var des_seleccionado=null;
function onloadPage(params){
	_onloadPage();
	//Cargar los combos
	$('#id_grad').on('change',function(event){
		if(com_seleccionado!=null){
			listar_desempenios(com_seleccionado,$('#id_grad').val());
		}			
	});	
	
	$('#id_dcare').on('change',function(event){
		listar_competencias($('#id_dcare').val());
		
	});	
	$('#id_dcniv').on('change',function(event){
		_llenarComboURL('api/disenioCurricular/listarAreasCombo/'+$('#id_dcniv').val(),$('#id_dcare'),null,null,function(){$('#id_dcare').change();});
		var id_niv=$('#id_dcniv option:selected').attr("data-aux1");
		_llenarCombo('cat_grad',$('#id_grad'),null,id_niv, function(){
			$('#id_grad').change();
		});
	});
	$('#id_dcn').on('change',function(event){
		_llenarComboURL('api/disenioCurricular/listarNivelesCombo/'+$('#id_dcn').val(),$('#id_dcniv'),null,null,function(){$('#id_dcniv').change();});
	});
	_llenarComboURL('api/disenioCurricular/listarDisenioCurricular',$('#id_dcn'),null,null,function(){$('#id_dcn').change();});
	
	var id_anio=$("#_id_anio").text();
	//listar_disenio_curricular(id_anio);
	$("#div_competencias").show();
	$("#div_capacidades").hide();
	$("#div_desempenio").hide();
}

function listar_competencias(id_dcare){
	_get('api/competenciaDC/listarCompetencias/'+id_dcare,function(data){
	if(data.length>0){
	var id_com_pri=data[0].id;
	com_seleccionado=id_com_pri;
	} else {
		com_seleccionado=0;
	}	
	$('#tabla_competencias').dataTable({
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
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_capacidades(com_seleccionado);
				listar_desempenios(com_seleccionado,$('#id_grad').val());
				_llenarComboURL('api/disenioCurricular/listarAreasCombo/'+$('#id_dcniv').val(),$('#id_area'),null,null,function(){$('#id_area').change();
				$('#id_area').val($('#id_dcare').val()).trigger('change');
				});
				$('#id_area').prop('disabled',true);
				
				//listar_giro_negocio(id_dc_pri);
				$("#tabla_competencias tbody tr").click(function(){  
					$("#div_competencias").show();
					$("#div_capacidades").hide();
					$("#div_desempenio").hide();
					var id_com=($(this).find('td:nth-child(1)').find('#id').val());
					com_seleccionado=id_com;
					listar_capacidades(com_seleccionado);
					listar_desempenios(com_seleccionado,$('#id_grad').val());
					//listar_giro_negocio(id_emp);
					
					_get('api/competenciaDC/' + id_com,
					function(data){
						$("#div_competencias #nom_competencia").text(data.nom);
						console.log(data);
						_fillForm(data,$('#frm-competencia'));
					});
				});	   
				}
			});
	});
}

function listar_capacidades(id_com){
	_get('api/competenciaDC/listarCapacidades/'+id_com,function(data){
	if(data.length>0)
	cap_seleccionado=data[0].id;
	//listar_areas(niv_seleccionado);
	//var id_gir_pri=$("#tabla_giro_negocio tbody tr").find('tr:nth-child(1)').find('#id').val();
	$('#tabla_capacidades').dataTable({
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
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="local_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="local_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				$('#frm-capacidad #id_com').prop('disabled',true);
				//$('#frm_local #id_ggn').val(id_gir);
				$("#tabla_capacidades tbody tr").click(function(){  
					cap_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					//listar_areas(niv_seleccionado);		 
					//listar_servicios(suc_seleccionado,id_gir_pri);
					$("#div_competencias").hide();
					$("#div_capacidades").show();
					$("#div_desempenio").hide();
					//var id_anio=$("#_id_anio").text();
					_llenarComboURL('api/competenciaDC/listarCompetencias/'+$('#id_dcare').val(),$('#frm-capacidad #id_com'),com_seleccionado,null);
					if(cap_seleccionado!=null){
						_get('api/competenciaDC/obtenerDatosCapacidad/' + cap_seleccionado,
						function(data){
							console.log(data);
							_fillForm(data,$('#frm-capacidad'));
							$("#div_capacidades #nom_capacidad").text("CAPACIDAD "+(data.nom));
							/*_llenarCombo('ges_giro_negocio',$('#id_ggn'),data.id_ggn,null, function(){
								$('#id_ggn').change();
							});*/
							//alert(data.id_niv);
							$('#frm-capacidad #id_dcn').val(data.id_com).change();					
						});
					}	
				});	   
				}
			});
	});
}


function listar_desempenios(id_com, id_gra){
	_get('api/competenciaDC/listarDesempenios/'+id_com+'/'+id_gra,function(data){
	//Primer Giro de Negocio
	if(data.length>0){
		des_seleccionado=data[0].id;
	}		
	$('#tabla_desempenios').dataTable({
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
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value; 
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
				//listar_servicios(id_suc_pri,id_gir_pri);
				$("#tabla_desempenios tbody tr").click(function(){  
					des_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					$("#div_competencias").hide();
					$("#div_capacidades").hide();
					$("#div_desempenio").show();
					_llenarComboURL('api/competenciaDC/listarCompetencias/'+$('#id_dcare').val(),$('#frm-desempenio #id_com'),com_seleccionado,null);
					var id_niv=$('#id_dcniv option:selected').attr("data-aux1");
					_llenarCombo('cat_grad',$('#frm-desempenio #id_gra'),$('#id_grad').val(),id_niv, function(){
						
					});
					$('#frm-desempenio #id_com').prop('disabled',true);
					$('#frm-desempenio #id_gra').prop('disabled',true);
					if(des_seleccionado!=null){
						_get('api/competenciaDC/obtenerDatosDesempenio/' + des_seleccionado,
						function(data){
							console.log(data);
							_fillForm(data,$('#div_desempenio'));
							$("#div_desempenio #desempenio_nom").text("DESEMPEÑO "+data.nom);
							$('#frm-desempenio #id_com').val(data.id_com).change();
							$('#frm-desempenio #id_gra').val(data.id_gra).change();
						});	
					}	
				});	   
				}
			});
	});
}	

$('#frm-competencia #btn_grabar').on('click',function(event){
		$('#id_area').prop('disabled',false);
		var validation = $('#frm-competencia').validate(); 
		if ($('#frm-competencia').valid()){
			_post($('#frm-competencia').attr('action') , $('#frm-competencia').serialize(),function(data) {
				$('#frm-competencia #id').val(data.result);
				listar_competencias($('#id_dcare').val());
				$('#id_area').prop('disabled',true);
			}	
			);
		}	
		
});

$('#frm-competencia #btn_nuevo').on('click',function(event){
	$('#frm-competencia #id').val('');
	$('#frm-competencia #nom').val('');
	$('#frm-competencia #peso').val('');
	$('#frm-competencia #orden').val('');
});	

$('#frm-competencia #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/' + $('#frm-competencia #id').val(),
		context:_URL,
		success:function(){
			listar_competencias($('#id_dcare').val());
			$('#frm-competencia #id').val('');
			$('#frm-competencia #nom').val('');
			$('#frm-competencia #peso').val('');
			$('#frm-competencia #orden').val('');
		}
		});
});

$('#frm-capacidad #btn_grabar').on('click',function(event){
		$('#frm-capacidad #id_com').prop('disabled',false);
		var validation = $('#frm-capacidad').validate(); 
		if ($('#frm-capacidad').valid()){
			_post($('#frm-capacidad').attr('action') , $('#frm-capacidad').serialize(),function(data) {
				$('#frm-capacidad #id').val(data.result);
				listar_capacidades(com_seleccionado);
				$('#frm-capacidad #id_com').prop('disabled',true);
			}	
			);
		}		
});

$('#frm-capacidad #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/eliminarCapacidad/' + $('#frm-capacidad #id').val(),
		context:_URL,
		success:function(){
			listar_capacidades(com_seleccionado);
			$('#frm-capacidad #id').val('');
			//$('#frm-capacidad #id_com').val('').trigger('change');
			$('#frm-capacidad #peso').val('');
			$('#frm-capacidad #orden').val('');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-capacidad #btn_nuevo').on('click',function(event){
	$('#frm-capacidad #id').val('');
	//$("#frm-capacidad #id_com").val('').trigger('change');
	$('#frm-capacidad #nom').val('');
	$('#frm-capacidad #peso').val('');
	$('#frm-capacidad #orden').val('');
	$('#div_capacidades #nom_capacidad').text('');
});

$('#frm-desempenio #btn_grabar').on('click',function(event){
		$('#frm-desempenio #id_com').prop('disabled',false);
		$('#frm-desempenio #id_gra').prop('disabled',false);
		var validation = $('#frm_desempenio').validate(); 
		if ($('#frm-desempenio').valid()){
			_post($('#frm-desempenio').attr('action') , $('#frm-desempenio').serialize(),function(data) {
				$('#frm_desempenio #id').val(data.result);
				//listar_competencias_transversales($('#frm_area #id').val());
				listar_desempenios(com_seleccionado,$('#id_grad').val());
				$('#frm-desempenio #id_com').prop('disabled',true);
				$('#frm-desempenio #id_gra').prop('disabled',true);
			}	
			);
		}	
		
});

$('#frm-desempenio #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/competenciaDC/eliminarDesempenio/' + $('#frm-desempenio #id').val(),
		context:_URL,
		success:function(){
			listar_desempenios(com_seleccionado,$('#id_grad').val());
			$('#frm-desempenio #id').val('');
			$("#frm-desempenio #nom").val('');
			$("#frm-desempenio #orden").val('');
			$("#frm-desempenio #peso").val('');
		}
		});
});

$('#frm-desempenio #btn_nuevo').on('click',function(event){
	$('#frm-desempenio #id').val('');
			$("#frm-desempenio #nom").val('');
			$("#frm-desempenio #orden").val('');
			$("#frm-desempenio #peso").val('');
});

