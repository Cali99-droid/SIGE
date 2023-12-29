//Se ejecuta cuando la pagina q lo llama le envia parametros
var _cant_historial;
function onloadPage(params) {
	_onloadPage();

	$('#id_mat').val(params.id_mat);
	$("#alumno").text(params.alumno);
	$("#nivel").text(params.nivel);
	$("#grado").text(params.grado);
	$("#seccion").text(params.secc);
	$("#apoderado").text(params.apoderado);
	$("#dni").text(params.nro_doc);
	$("#celular").text(params.cel);
	
	
	$('#mat_blo').on('click',function (e){
		if ($(this).is(':checked')){
			$('#obs_blo').removeAttr('disabled');
		}else{
			$('#obs_blo').attr('disabled','disabled');			
		}
	});
	
	$('#id_cond').on('change',function(event){
		if($('#id_cond').val()!=''){
			$('#bloqueo').css('display','none');
		} else{
			$('#bloqueo').css('display','block');
		}
	});
	
	var param ={id_mat:params.id_mat, tip:'E'}
	_get('api/condicion/obtenerBloqueo', function(data) {
		console.log(data);
		if(data!=null){
		$('#mat_blo').val(data.mat_blo);
		$('#obs_blo').val(data.obs_blo);
		if (data.mat_blo == "1") {
			$('#mat_blo').prop("checked", true);
		}
		$('#mat_condicion-frm #id').val(data.id);
		}
		if ($('#mat_blo').is(':checked')){
			$('#obs_blo').removeAttr('disabled');
		}else{
			$('#obs_blo').attr('disabled','disabled');			
		}
		
		
	},param);
	$('#id_ccr_mad').on(
			'change',
			function(event) {
				$ptj_nro_mens = 0;
				if ($("#nro_mens").val() == '0') {
					$ptj_nro_mens = 5;
				} else if ($("#nro_mens").val() == '1'
						|| $("#nro_mens").val() == '2'
						|| $("#nro_mens").val() == '3') {
					$ptj_nro_mens = -1;
				} else if ($("#nro_mens").val() == '4'
						|| $("#nro_mens").val() == '5'
						|| $("#nro_mens").val() == '6'
						|| $("#nro_mens").val() == '7') {
					$ptj_nro_mens = -2.5;
				} else if ($("#nro_mens").val() == '8'
						|| $("#nro_mens").val() == '9'
						|| $("#nro_mens").val() == '10') {
					$ptj_nro_mens = -5;
				}

				if ($("#ing_fam").val() == '1')
					$ptj_ing_fam = 0;
				else if ($("#ing_fam").val() == '2')
					$ptj_ing_fam = 2.5;
				else if ($("#ing_fam").val() == '3')
					$ptj_ing_fam = 5;
				else if ($("#ing_fam").val() == '0')
					$ptj_ing_fam = 0;

				if ($("#id_ccr_pad").val() == '1')
					$ptj_cr_pad = 0;
				else if ($("#id_ccr_pad").val() == '2')
					$ptj_cr_pad = -2.5;
				else if ($("#id_ccr_pad").val() == '3')
					$ptj_cr_pad = 1;
				else if ($("#id_ccr_pad").val() == '4')
					$ptj_cr_pad = 2.5;
				else if ($("#id_ccr_pad").val() == '-1')
					$ptj_cr_pad = 0;

				if ($("#id_ccr_mad").val() == '1')
					$ptj_cr_mad = 0;
				else if ($("#id_ccr_mad").val() == '2')
					$ptj_cr_mad = -2.5;
				else if ($("#id_ccr_mad").val() == '3')
					$ptj_cr_mad = 1;
				else if ($("#id_ccr_mad").val() == '4')
					$ptj_cr_mad = 2.5;
				else if ($("#id_ccr_mad").val() == '-1')
					$ptj_cr_mad = 0;
				$ptje_total = 0;
				console.log($ptj_nro_mens);
				console.log($ptj_ing_fam);
				console.log($ptj_cr_pad);
				console.log($ptj_cr_mad);
				$ptje_total = $ptj_nro_mens + $ptj_ing_fam + $ptj_cr_pad
						+ $ptj_cr_mad;
				$("#puntaje").val($ptje_total);

			});

	function llenarDatosEconomicos() {
		_get('api/pagos/datosAlumnoPago/' + $('#id_mat').val(), function(data) {
			console.log(data);
			marmes = '';
			if (data.ultimo_pago == '3')
				$mes = 'Marzo';
			else if (data.ultimo_pago == '4')
				$mes = 'Abril';
			else if (data.ultimo_pago == '5')
				$mes = 'Mayo';
			else if (data.ultimo_pago == '6')
				$mes = 'Junio';
			else if (data.ultimo_pago == '7')
				$mes = 'Julio';
			else if (data.ultimo_pago == '8')
				$mes = 'Agosto';
			else if (data.ultimo_pago == '9')
				$mes = 'Setiembre';
			else if (data.ultimo_pago == '10')
				$mes = 'Octubre';
			else if (data.ultimo_pago == '11')
				$mes = 'Noviembre';
			else if (data.ultimo_pago == '12')
				$mes = 'Diciembre';
			else
				$mes = 'Ninguno';
			$('#ult_men').val($mes);
			// $('#nro_mens').val(data.meses_deuda);
			$('#dni_padre').val(data.dni_padre);
			$('#dni_madre').val(data.dni_madre);
			$('#fecha').val(_fechaActual());
			$('#ing_fam').change();
		});
		_get('api/pagos/pagosAtrasados/' + $('#id_mat').val(), function(data) {
			console.log(data);
			$('#nro_mens').val(data.length);
		});
	}
	historial_eco_listar_tabla(params.id_mat);

	$('#mat_condicion-frm #btn-agregar').on(
			'click',
			function(event) {
				$('#mat_condicion-frm #id').val('');
				if ($('#mat_blo').is(':checked'))
					$('#mat_blo').val('1');
				else
					$('#mat_blo').val('');
				_post($('#mat_condicion-frm').attr('action'), $(
						'#mat_condicion-frm').serialize(), function(data) {
					// onSuccessSave(data) ;
				});
			});

	$('#mat_condicion-frm #btn-grabar').on(
			'click',
			function(event) {
				if(_cant_historial<=0 && $('#id_cond').val()!=''){
					alert('Primero debe de llenar el historial económico');
					//$('#mat_condicion-frm #btn-grabar').attr('disabled','disabled');
				} else if(_cant_historial<=0 && $('#id_cond').val()==''){
					if($('#id_cond').val()!=''){
						$('#mat_blo').val('');
						$('#obs_blo').val('');
						$('#tip_blo').val('');
					} else{
						if($('#mat_blo').is(':checked'))
							$('#mat_blo').val('1');
						else
							$('#mat_blo').val('');
					}
					_post($('#mat_condicion-frm').attr('action'), $(
							'#mat_condicion-frm').serialize(), function(data) {
						// onSuccessSave(data) ;
					});
				} else if(_cant_historial>0 && $('#id_cond').val()!=''){
				
					//$('#mat_condicion-frm #btn-grabar').removeAttr('disabled');
					if($('#id_cond').val()!=''){
						$('#mat_blo').val('');
						$('#obs_blo').val('');
						$('#tip_blo').val('');
					} else{
						if($('#mat_blo').is(':checked'))
							$('#mat_blo').val('1');
						else
							$('#mat_blo').val('');
					}
					_post($('#mat_condicion-frm').attr('action'), $(
							'#mat_condicion-frm').serialize(), function(data) {
						// onSuccessSave(data) ;
					});
				}
				
			});
	
	$('#mat_condicion-frm #btn-eliminar').on(
			'click',
			function(event) {
				_delete('api/condicion/' + $('#mat_condicion-frm #id').val(),
						function(){
					$('#mat_condicion-frm #id').val('');
					$('#mat_blo').val('');
					$('#obs_blo').val('');
					$('#tip_blo').val('');
					$('#id_cond').val('').change();
					$('#des').val('');
							}
						);
	});

	$('#btn-grabar_hist')
			.on(
					'click',
					function(event) {
						event.preventDefault();
						if ($("#id_ccr_pad").val() == ''
								|| $("#id_ccr_mad").val() == '') {
							_alert_error("Debe seleccionar el tipo de central riesgo!!");
						} else {
							var id_mat = $('#mat_condicion-frm #id_mat').val();
							console.log(id_mat);
							$('#col_historial_eco-frm #id').val('');
							_post('api/historialEco/grabarHistorial/' + id_mat,
									$('#col_historial_eco-frm').serialize(),
									function(data) {
										// onSuccessSave(data) ;
										historial_eco_listar_tabla(id_mat);
									});
						}
					});

	
	var param ={id_mat:params.id_mat}
	_get('api/condMatricula/obtenerDatosCond', function(data) {
		console.log(data);
		if(data!=null){
		$('#mat_condicion-frm #id').val(data.mat_cond);
		}
		if ($('#mat_condicion-frm #id').val()!='') {
			var id_cond=$('#mat_condicion-frm #id').val();
			_get('api/condicion/' +id_cond+'/1', function(data) {
				_fillForm(data, $('#modal').find('form'));
				console.log(data);
				if(data!=null){
					_llenarComboURL('api/condAlumno/listarCondicionTipo', $('#id_cond'), data.id_cond,function(){$('#id_cond').change()});
					$('#bloqueo').css('display','none');
					$('#alumno').val(data.alumno.ape_pat + ' ' + data.alumno.ape_mat + ' '+ data.alumno.nom);
					$('#id_mat').val(params.id_mat);
					$('#id_anio').val(data.id_anio);
					llenarDatosEconomicos();
					$('#mat_condicion-frm #des').val(data.des);
					$('#mat_blo').val(data.mat_blo);
				}
				else
				_llenarComboURL('api/condAlumno/listarCondicionTipo', $('#id_cond'),null,function(){$('#id_cond').change()});
				
				$('#ing_fam').on('change',
						function(event) {
							_llenarCombo('cat_central_riesgo', $('#id_ccr_pad'),
									null, null, function() {
										$('#id_ccr_pad').change()
									});
						});
				$('#id_ccr_pad').on(
						'change',
						function(event) {
							_llenarCombo('cat_central_riesgo', $('#id_ccr_mad'),
									null, null, function() {
										$('#id_ccr_mad').change()
									});
						});
				$('#mat_blo').val();
				
				console.log('aquii' + $('#mat_blo').val());

				/*if ($('#mat_blo').val() != '') {

					$('#mat_blo').prop("checked", true);


				}*/

				if ($('#mat_blo').is(':checked')){
					$('#obs_blo').removeAttr('disabled');
				}else{
					$('#obs_blo').attr('disabled','disabled');			
				}

				
			});
			$('#mat_condicion-frm #btn-agregar').hide();
			/*
			 * $('#id_cond').on('change',function(event){
			 * $('#bloqueo').css('display','none'); });
			 */
		} else {
			llenarDatosEconomicos();
			_llenarComboURL('api/condAlumno/listarCondicionTipo', $('#id_cond'),
					null);
			/*
			 * $('#id_cond').on('change',function(event){ alert();
			 * $('#bloqueo').css('display','none'); });
			 */
			$('#id_ccr_pad').on(
					'change',
					function(event) {
						_llenarCombo('cat_central_riesgo', $('#id_ccr_mad'), null,
								null, function() {
									$('#id_ccr_mad').change()
								});

					});
			$('#ing_fam').on(
					'change',
					function(event) {
						_llenarCombo('cat_central_riesgo', $('#id_ccr_pad'), null,
								null, function() {
									$('#id_ccr_pad').change()
								});
					});
			$('#bloqueo').css('display', 'block');
			$('#mat_condicion-frm #btn-grabar').hide();
			
			if ($('#mat_blo').checked){
				$('#obs_blo').removeAttr('disabled');
			}else{
				$('#obs_blo').attr('disabled','disabled');			
			}

		}
		
	},param);

}

function historial_eco_listar_tabla(id_mat) {
	_get(
			'api/historialEco/listar/' + id_mat,
			function(data) {
				console.log(data);
				_cant_historial=data.length;
				$('#col_historial_eco-tabla')
						.dataTable(
								{
									data : data,
									aaSorting : [],
									destroy : true,
									orderCellsTop : true,
									// bLengthChange: false,
									// bPaginate: false,
									// bFilter: false,
									select : true,
									columns : [
											{
												"title" : "Nro",
												"render" : function(data, type,
														row, meta) {
													return parseInt(meta.row) + 1;
												}
											},
											{
												"title" : "Fecha Consulta",
												"render" : function(data, type,
														row, meta) {
													return "<font align='center'>"
															+ _parseDate(row.fec_ins)
															+ "</font>";
												}
											},
											{
												"title" : "Mens. Cancelada",
												"data" : "ult_men"
											},
											{
												"title" : "Nro. Mens. Adeuda ",
												"data" : "nro_mens"
											},
											{
												"title" : "Ingresos Familiares",
												"render" : function(data, type,
														row, meta) {
													if (row.ing_fam == '1') {
														return "<font>S/.(0-1499)</font>";
													} else if (row.ing_fam == '2') {
														return "<font align='center'>S/. (1500 - 2499)</font>";
													} else if (row.ing_fam == '3') {
														return "<font align='center'>S/. (2500 a más)</font>";
													} else if (row.ing_fam == '0') {
														return "<font align='center'>Ninguno</font>";
													}
												}
											},
											{
												"title" : "Score Padre",
												"data" : "centralRiesgo.nom_pad"
											},
											{
												"title" : "Score Madre",
												"data" : "centralRiesgo.nom_mad"
											}, {
												"title" : "Score Familia",
												"data" : "puntaje"
											} ],
									"initComplete" : function(settings) {
										_initCompleteDT(settings);
									}
								});
			});

}
