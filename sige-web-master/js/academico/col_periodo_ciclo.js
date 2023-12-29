//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
var id_per_sel=null;
var id_cic_sel=null;
function onloadPage(params){
	_onloadPage();
	//var id_anio=$('#_id_anio').text();
	$("#div_periodo").show();
	$("#div_ciclo").hide();
	$("#frm-mant_periodo #id_anio").val(id_anio);
	$('#id_gir').on('change',function(event){
		listar_periodos(id_anio,$('#id_gir').val());
	});
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null, function(){
		$('#id_gir').change();		
	});
}

function _onchangeAnio(id_anio, anio){
	listar_periodos(id_anio,$('#id_gir').val());
}

function listar_periodos(id_anio,id_gir){
	var param={id_anio:$('#_id_anio').text(), id_gir:id_gir};
	_get('api/periodo/listar',function(data){
	id_per_sel=data[0].id;
	$('#tabla_periodo').dataTable({
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
			{"title":"Giro de Negocio", "data" : "giroNegocio.nom"}, 
			{"title":"Local", "data" : "servicio.sucursal.nom"}, 
			{"title":"Nivel", "data" : "nivel.nom"}, 
			{"title":"Tipo Periodo", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.tipPeriodo.nom
			}}
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_ciclos(id_per_sel);
				$("#tabla_periodo tbody tr").click(function(){  
					$("#div_periodo").show();
					$("#div_ciclo").hide();
					id_per_sel=($(this).find('td:nth-child(4)').find('#id').val());
					listar_ciclos(id_per_sel);
					_get('api/periodo/'+id_per_sel,
					function(data){
						//$("#div_periodo #nom_periodo").text("PERIODO "+data.nom);
						console.log(data);
						_fillForm(data,$('#frm-mant_periodo'));	
						$('#id_srv').on('change',function(event){
							var id_niv=$('#id_srv option:selected').attr("data-aux2");
							var id_suc=$('#id_srv option:selected').attr("data-aux3");
							$("#frm-mant_periodo #id_niv").val(id_niv);
							$("#frm-mant_periodo #id_suc").val(id_suc);
						});
						_llenarCombo('cat_tip_periodo',$('#frm-mant_periodo #id_tpe'),data.id_tpe,null, function(){
							$('#frm-mant_periodo #id_tpe').change();
						});
						_llenarComboURL('api/servicio/listarServicios',$('#id_srv'), data.id_srv, null, function(){
							$('#frm-mant_periodo #id_srv').change();
						});
						$("#frm-mant_periodo #fec_ini").val(_parseDate(data.fec_ini));
						$("#frm-mant_periodo #fec_fin").val(_parseDate(data.fec_fin));
						$("#frm-mant_periodo #fec_cie_mat").val(_parseDate(data.fec_cie_mat));
						$('.daterange-single').daterangepicker({ 
							singleDatePicker: true,
						 //   autoUpdateInput: false,
							locale: { format: 'DD/MM/YYYY'}
						});
					});
				});	   
				}
			});
	},param);
}

function listar_ciclos(id_per){
	var param={id_per:id_per};
	_get('api/periodo/listarCiclos',function(data){
	if(data.length>0)
		id_cic_sel=data[0].id;
	$('#tabla_ciclo').dataTable({
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
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				//_initCompleteDT(settings);
				$('#frm-mant_ciclo #id_per').val(id_per);
				$("#tabla_ciclo tbody tr").click(function(){  
					id_cic_sel=($(this).find('td:nth-child(1)').find('#id').val());
					$("#div_ciclo").show();
					$("#div_periodo").hide();
					if(id_cic_sel!=null){
						_get('api/periodo/obtenerDatosCiclo/' + id_cic_sel,
						function(data){
							console.log(data.size);
							if(data){
								_fillForm(data,$('#frm-mant_ciclo'));
								$("#frm-mant_ciclo #fec_ini").val(_parseDate(data.fec_ini));
								$("#frm-mant_ciclo #fec_fin").val(_parseDate(data.fec_fin));
								$("#div_ciclo #nom_ciclo").text("CICLO "+data.nom);
								/*_llenarCombo('per_periodo',$('#frm-mant_ciclo #id_per'),data.id_per,null, function(){
									$('#frm-mant_ciclo #id_per').change();
								});*/
								var param={id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									id: data.id_per,
									funcion:function(){
										$('#id_per').change();
									}
								});
								
								/*_llenarComboURL('api/periodo/listar',$('#id_per'), data.id_per, null, function(){
									$('#frm-mant_periodo #id_srv').change();
								});*/
								listarTurnos(id_cic_sel);
							}else{
								$("#div_ciclo #nom_ciclo").text("CICLO NUEVO");
								/*_llenarCombo('per_periodo',$('#frm-mant_ciclo #id_per'),id_per,null, function(){
									$('#frm-mant_ciclo #id_per').change();
								});*/
								var param={id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									id:id_per_sel,
									funcion:function(){
										$('#id_per').change();
									}
								});
								listarTurnos(id_cic_sel);
							}
							
						});
					} else{
						$("#div_ciclo #nom_ciclo").text("CICLO NUEVO");
						/*_llenarCombo('per_periodo',$('#frm-mant_ciclo #id_per'),id_per,null, function(){
							$('#frm-mant_ciclo #id_per').change();
						});*/
						var param={id_anio:id_anio, id_gir:$('#id_gir').val()};
						_llenar_combo({
							url:'api/periodo/listar',
							combo:$('#id_per'),
							params: param,
							context:_URL,
							text:'value',
							id:id_per_sel,
							funcion:function(){
								$('#id_per').change();
							}
						});
						listarTurnos(id_cic_sel);
					}
						
				});	   
				}
			});
	},param);
	listarTurnos(id_cic_sel);
}	


$('#frm-mant_periodo #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_periodo').validate(); 
		if ($('#frm-mant_periodo').valid()){
			_post($('#frm-mant_periodo').attr('action') , $('#frm-mant_periodo').serialize(),function(data) {
				$('#frm-mant_periodo #id').val(data.result);
				listar_periodos(id_anio);
			}	
			);
		}	
		
});

$('#frm-mant_periodo #btn_nuevo').on('click',function(event){
	$('#frm-mant_periodo #id').val('');
	$('#frm-mant_periodo #fec_ini').val('');
	$('#frm-mant_periodo #fec_fin').val('');
	$('#frm-mant_periodo #fec_cie_mat').val('');
	$("#frm-mant_periodo #id_tpe").val('').trigger('change');
	$("#frm-mant_periodo #id_srv").val('').trigger('change');
	$('#id_srv').on('change',function(event){
		var id_niv=$('#id_srv option:selected').attr("data-aux2");
		var id_suc=$('#id_srv option:selected').attr("data-aux3");
		$("#frm-mant_periodo #id_niv").val(id_niv);
		$("#frm-mant_periodo #id_suc").val(id_suc);
	});
	_llenarCombo('cat_tip_periodo',$('#frm-mant_periodo #id_tpe'),null,null, function(){
		$('#frm-mant_periodo #id_tpe').change();
	});
	//_llenarComboURL('api/servicio/listarServicios',$('#id_srv'));
	_llenarComboURL('api/servicio/listarServicios',$('#id_srv'),null, null, function(){
		$('#frm-mant_periodo #id_srv').change();
	});
});	

$('#frm-mant_periodo #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/periodo/' + $('#frm-mant_periodo #id').val(),
		context:_URL,
		success:function(){
			listar_periodos(id_anio);
			$('#frm-mant_periodo #id').val('');
			$('#frm-mant_periodo #fec_ini').val('');
			$('#frm-mant_periodo #fec_fin').val('');
			$('#frm-mant_periodo #fec_cie_mat').val('');
			$("#frm-mant_periodo #id_tpe").val('').trigger('change');
			$("#frm-mant_periodo #id_srv").val('').trigger('change');
		}
		});
});

$('#frm-mant_ciclo #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/periodo/eliminarCiclo/' + $('#frm-mant_ciclo #id').val(),
		context:_URL,
		success:function(){
			listar_ciclos($('#frm-mant_ciclo #id_per').val());
		}
		});
});

$('#frm-mant_ciclo #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_ciclo').validate(); 
		if ($('#frm-mant_ciclo').valid()){
			_post($('#frm-mant_ciclo').attr('action') , $('#frm-mant_ciclo').serialize(),function(data) {
				console.log(data);
				$('#frm-mant_ciclo #id').val(data.result);
				listar_ciclos($('#frm-mant_ciclo #id_per').val());
				listarTurnos(data.result);
			}	
			);
		}		
});

$('#frm-mant_grado #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/grad/' + $('#frm-mant_grado #id').val(),
		context:_URL,
		success:function(){
			listar_grados($('#frm-mant_grado #id_nvl').val());
			$('#frm-mant_grado #id').val('');
			$('#frm-mant_grado #nom').val('');
			$('#frm-mant_grado #des').val('');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-mant_ciclo #btn_nuevo').on('click',function(event){
	$('#frm-mant_ciclo #id').val('');
	$('#frm-mant_ciclo #nom').val('');
	$('#frm-mant_ciclo #fec_ini').val('');
	$('#frm-mant_ciclo #fec_fin').val('');
	$("#frm-mant_ciclo #id_per").val('').trigger('change');
	/*_llenarCombo('per_periodo',$('#frm-mant_ciclo #id_per'),null,null, function(){
		$('#frm-mant_ciclo #id_per').change();
	});*/
	var param={id_anio:id_anio, id_gir:$('#id_gir').val()};
						_llenar_combo({
							url:'api/periodo/listar',
							combo:$('#id_per'),
							params: param,
							context:_URL,
							text:'value',
							id:id_per_sel,
							funcion:function(){
								$('#id_per').change();
							}
						});
	listarTurnos(null);
	$("#div_ciclo #nom_ciclo").text("CICLO NUEVO");
});

function listarTurnos (id_cic){
	var param = {id_cic:id_cic};
	_get('api/periodo/listarTurnosxCiclo',function(data){
	$('#tabla_turnos').dataTable({
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
			{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
				//creamos un hidden x cada checkbox
				var checked = (row.id_ctu!=null) ? ' checked ': ' ';
				var disabled = (checked) ? 'disabled':'';
				
				return "<input type='hidden' id='id_ctu" + row.id +"' name='id_ctu' " + disabled + " value='"+row.id_ctu+"'/><input type='checkbox' " + checked + "id='id_tur"+row.id+"'  name ='id_tur'  value='" + row.id + "' onclick='seleccionarTurno(this)'/>";
			} 
			},
			{"title":"Turno", "data" : "nom"},
			{"title":"Hora Inicio", "render":function ( data, type, row,meta ) { 
				//var hor_ini = (row.hor_ini != null) ? _parseDate(row.hor_ini) :'';
					return "<input type='time' id='hor_ini" + row.id + "' name='hor_ini' value='" + row.hor_ini + "'  data-id='" + row.id + "' class='form-control input-sm datetimerange-single' maxlength='10' />";
				} 
			},
			{"title":"Hora Fin", "render":function ( data, type, row,meta ) { 
				//var hor_fin = (row.hor_fin != null) ? _parseDate(row.hor_fin) :'';
				return "<input type='time' id='hor_fin" + row.id + "' name='hor_fin'  value='" + row.hor_fin + "' data-id='" + row.id + "' class='form-control input-sm datetimerange-single' maxlength='10' />";
			}} 
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);   
				$('.datetimerange-single').prop('disabled',true);
				//aca reviso si tiene checked y deshabilito
				 $("#frm-mant_ciclo input[name='id_tur']:checked").each(function() {
						$('#id_ctu' + $(this).val()).prop('disabled',false);
						$('#id_tur' + $(this).val()).prop('disabled',false);						
						//$('#id_eva' + $(this).val()).val($(this).data('ideva'));
						$('#hor_ini' + $(this).val()).prop('disabled',false);
						$('#hor_fin' + $(this).val()).prop('disabled',false);
				  });	
				 
				 $("#frm-mant_ciclo input[name='id_tur']:unchecked").each(function() {
					 $('#hor_ini' + $(this).val()).val('');
					 $('#hor_fin' + $(this).val()).val('');
				 });
				}
			});
	},param);
}

//grilla de aulas/fecha
function seleccionarTurno(campo1){
	var campo =$(campo1);
	console.log(campo);
	var id = campo.val();
	
	//$('#id_eva' + id).val('');
	if(campo.is(':checked')){
		$('#hor_ini' + id ).prop( "disabled", false );
		$('#hor_fin' + id ).prop( "disabled", false );
		$('#id_ctu' + id ).prop( "disabled", false );
		$('#id_tur' + id ).prop( "disabled", false );
	}else{
		//var id_eva = $('#id_eva' + id).val();
		$('#id_ctu' + id).prop('disabled', true);//SE DESHABILITA
		$('#id_tur' + id).prop('disabled', true);
		$('#hor_ini' + id ).prop( "disabled", true );
		$('#hor_ini' + id).val('');
		$('#hor_fin' + id ).prop( "disabled", true );
		$('#hor_fin' + id).val('');
	}
	
	
}

