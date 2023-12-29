/** Se ejeuta al ser llamado desde otra pagina * */
function onloadPage(params) {

	_onloadPage();
	
	// datos del concurso
	$("#oli\\.nom").html(params.nom);
	$("#oli\\.alumno").val(params.alumno);
	$("#oli\\.nro_dni").val(params.nro_dni);
	$("#oli\\.colegio").val(params.colegio);
	$("#oli\\.nivel").val(params.nivel);
	$("#oli\\.grado").val(params.grado);
	$("#oli\\.monto").val(_formatMonto(params.monto, 2));
	$("#oli\\.modalidad").val(params.modalidad);
	$("#oli\\.distrito").val(params.distrito);
	$("#oli\\.id ").val(params.id);

	$("resumen_total").html(_formatMonto(params.monto, 2));

	if(params.id_pag!=null)
		marcar_pagado();

	//SI ES DELEGACION
	if(params.id_ti=='3'){
		//listar alumnos pendientes de pago
		$('#divPagos').hide();
		$('#divRowdelegacionResumen').show();
		$('#tabla-delegacion').show();
		$("#rowDelegacion").show();

		
		_GET({url:'api/pagos/delegacionPorPagar/' + params.id, 
				context:_URL_OLI,
				success:function(data){

					$("#oli\\.delegado").val(data.delegacion.ape_pat_del  + ' '  + data.delegacion.ape_mat_del + ', ' + data.delegacion.nom_del);
					$("#oli\\.director").val(data.delegacion.ape_pat_dir  + ' '  + data.delegacion.ape_mat_dir + ', ' + data.delegacion.nom_dir);

					
				$('#tabla-delegacion').dataTable({
					 data : data.pagadosDelegacion,
					 aaSorting: [],
					 destroy: true,
					 pageLength: 1000,
					 select: true,
			         columns : [ 
			           {"title": "Nro doc.", "data" : "nro_dni"},
			           {"title":"Alumno", "data" : "alumno"},
			           {"title":"Nivel", "data" : "nivel"}, 
				       {"title":"Grado", "data" : "grado"}, 
				       {"title":"Monto", "data" : "monto",
			        	   "render": function ( data) {
			                   return "S/"  + _formatMonto(data);}
			        	   },
			           {"title":"Pagar","render": function ( data, type, row) {
				                   return "<input class='chkPagar' data-id=" + row.id + " data-monto='" +  row.monto + "' type='checkbox' checked onclick='seleccionarPago()'>";}
				       }
			        ],
			        initComplete: function( settings ) {
			        	console.log(params);
			        	//$(".chkPagar[data-id=" + params.id + "]").prop('checked',true);
			        	//$(".chkPagar").prop('checked',true);
			        	seleccionarPago();
					 }

			    });
				
					
					
				}
			});
	}
	
	_GET({url:'api/pagos/pagados/' + params.id, 
			context:_URL_OLI,
			success:function(data){
			
			$('#tabla-pagos').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		           {"title": "Nro doc.", "data" : "nro_dni"},
		           {"title":"Alumno", "data" : "alumno"},
		           {"title":"Colegio", "data" : "colegio"}, 
			       {"title":"Nivel", "data" : "nivel"}, 
			       {"title":"Grado", "data" : "grado"}, 
			       {"title":"Modalidad", "data" : "modalidad"}, 
			       {"title":"Monto", "data" : "monto",
		        	   "render": function ( data) {
		                   return "S/"  + _formatMonto(data);}
		        	   }, 
		           {"title":"Fecha Pago", "data" : "fecha"}
		        ]
		    });
			
			}
		});
	
	$(".btnPagar").on('click',function() {
		console.log('clcicj');
						swal(
								{
									title : "Esta seguro?",
									text : "El pago afectara a caja, Esta seguro que el cliente va a pagar?",
									type : "warning",
									showCancelButton : true,
									confirmButtonColor : "rgb(33, 150, 243)",
									cancelButtonColor : "#EF5350",
									confirmButtonText : "Si, Pagar",
									cancelButtonText : "No, cancela!",
									closeOnConfirm : true,
									closeOnCancel : true
								},
								function(isConfirm) {
									if (isConfirm) {

										$("#btnPagar").attr("disabled","disabled");
										
										var id;
										
										if(params.id_ti=='3'){//modalidad delegacion
											id = obtenerIdsFromCkeck();
										}else
											id = params.id;

										if(id==''){
											_alert_error('Por favor seleccionar por lo menos un alumno para pagar en caja');
										}else{
											//alert($("#_id_suc").html());
											_POST({url : 'api/pagos/pagar',
												params : {id : id, id_suc:$("#_id_suc").html()},
												context: _URL_OLI,
												success: function(){
													marcar_pagado();
												},error: function(data){
													_alert_error(data.msg);
													$("#btnPagar").removeAttr("disabled");
												}
											  });											
										}

									} else {
										swal({
											title : "Cancelado",
											text : "No se ha realizado ninguna transaccion",
											confirmButtonColor : "#2196F3",
											type : "error"
										});
									}
								});

					});

}

function obtenerIdsFromCkeck(){
	var ids	= '';
	$('#tabla-delegacion').each(function ()
	{
		$('.chkPagar').each(function (index){
		if ($(this).prop("checked"))
			ids = ids + '-' + $(this).data('id');
	});
	});	
	
	
	if(ids!='') ids = ids.substring(1);
	
	return ids;
}

function seleccionarPago(){
	var total	= 0;
		$('#tabla-delegacion').each(function ()
	{
	$('.chkPagar').each(function (index){
		if ($(this).prop("checked")) 
			total = total + parseFloat($(this).data('monto'));
	});
		});	
	$('resumen_total').html(_formatMonto(total));

}

function marcar_pagado() {
	
	//$('#pagos_titulo"').html('CANCELADO');
	$('#div-pagos-btn').html('<label class="control-label col-lg-8 text-white"><b><h4>PAGADO</h4></b></label>');

	$("#btnPagar").attr("disabled","disabled");
	
}
