/** Se ejeuta al ser llamado desde otra pagina **/
function onloadPage(params){

	_onloadPage();
	_GET({
		context:_URL,
		url:'api/pagos/pagoMensualidad',
		success:function(data){
			console.log(data);
				if(data.val==0){
					$("#btnPagar").attr('disabled','disabled');
				} else{
					$("#btnPagar").removeAttr('disabled');
				}
		}
	});
	
	var pagosPendientesCallBack = function(data){
		//Si es del año 2020 deshabilitar
		if($("#_id_anio").text()==4)
			$("btnPagar").attr('disabled','disabled');
		else
			$("#btnPagar").removeAttr('disabled');
		
		// RESPONSE - CREAMOS LOS MESES PARA PAGAR
		var apoderado = data.apoderado; /*DATOS del cliente*/
		var hijos = data.hijos; /* Arreglo de todos los hijos del contrato*/
		
		//RESPONSE - MOSTRAMOS EN PANTALLA LOS DATOS DEL APODERADO
		$("#apoderado\\.nom").val(apoderado.nom);
		$("#apoderado\\.nro_doc").val(apoderado.nro_doc);
		$("#apoderado\\.cel").val(apoderado.cel);
		$("#apoderado\\.direccion").val(apoderado.direccion);
		$("#apoderado\\.num_cont").html(apoderado.num_cont);
		var url_img = _URL + 'Foto/familiar/' + apoderado.id;

		$("#apoderado\\.foto").attr("src", url_img );
		
		$("#nro_rec").html(data.nro_rec);
		
		//RESPONSE - MOSTRAMOS EN PANTALLA LOS DATOS DE LOS HIJOS
		
		for(var k in hijos) {
			var hijo = hijos[k];
			var id_mat = hijo.id_mat;
			var nombres = hijo.nombres;
			var meses_descuento = hijo.meses_descuento;
			var descuento_hermano = hijo.descuento_hermano;
			//var descuento_secretaria = hijo.descuento_secretaria;
			
			var rowAlumno;
			if ( $(".rowAlumno").length== 1 && $(".rowAlumno").data("id_mat")==""){
					rowAlumno = $(".rowAlumno");
			}
			else{
				//alert("d");
				rowAlumno = ($(".rowAlumno").last()).clone();
				//rowAlumno.appendTo( "#divRowAlumno" );
				$( "#divRowAlumno" ).append(rowAlumno);
				
				//limpiar los botones
				rowAlumno.find(".mes").parent().html('<div class="col-xs-5 col-md-3 mt-5 mes"><button type="button" disabled class="btn bg-orange btn-rounded btn-sm btn-sige1"><i class="fa fa-square-o" position-left"></i> <mes></mes></button></div>');
			}
			
			rowAlumno.data("id_mat",id_mat);
			rowAlumno.find("#alumno").html(nombres + " (" + hijo.nivel + "-" + hijo.grado + "-" + hijo.seccion  + ")");
			//alert("nro meses:" + meses_descuento.length);

			if (meses_descuento.length==0){
				//EL ALUNO NO TIENE DEUDA
				//rowAlumno.find(".panel_mes").html('<div class="alert alert-info alert-styled-left">' +
				//						'El alumno tiene todas las mensualidades pagadas'+
				//						'</div>');
				rowAlumno.find(".mes").css('display','none');
				rowAlumno.find(".nodebenada").css('display','block');
			}else{
				rowAlumno.find(".mes").css('display','inline-block');
				rowAlumno.find(".nodebenada").css('display','none');

				//alert("meses_descuento.length" + meses_descuento.length);
				for (var i=1;i<meses_descuento.length;i++){
					//agregar el mes a la sección alumno
					var last_mes= rowAlumno.find(".mes").last(); 
					(last_mes.clone()).appendTo( rowAlumno.find(".panel_mes") );
				}
				
				//console.log(rowAlumno.html());

				//alert("nombres::" + nombres);
				//console.log("meses_descuento",meses_descuento);
				rowAlumno.find( ".mes" ).each(function( index ) {
					
					$(this).find("mes").html(meses_descuento[index].mes);
					$(this).find("button").attr('data-id_mat',id_mat);
					$(this).find("button").attr('data-id',meses_descuento[index].id);
					$(this).find("button").attr('data-mens',meses_descuento[index].mens);
					$(this).find("button").attr('data-descuento_secretaria',meses_descuento[index].desc_pronto_pago);
					$(this).find("button").attr('data-monto',meses_descuento[index].monto);
					$(this).find("button").attr('data-descuento_hermano', meses_descuento[index].desc_hermano);
					$(this).find("button").attr('data-descuento_personalizado', meses_descuento[index].desc_personalizado);
					$(this).find("button").attr('data-descuento_personalizado_id', hijo.descuento_personalizado_id);// id del descuento personzalido
					
					if (meses_descuento[index].mens= "12")//TODO PARAMETRIZAR
						$(this).find("button").attr('data-descuento_12', hijo.descuento_12);// DESCUENTO DEL MES 12
					
					$(this).find("button").attr('data-alumno',nombres);
					$(this).find("button").on('click', function(){
							carritoMesAgregar($(this));
					});
				});
				
				//habilitamos el primer mes de pago
				rowAlumno.find(".mes").first().find("button").removeAttr("disabled");
				
			}

		}
		
	};
		_get('api/pagos/pagosPendientes/' + $("#_id_suc").text() + "/" +params.id_mat,pagosPendientesCallBack);
		
		_get('api/pagos/pagados/' +  params.id_mat,
				function(data){
					
					for (var i in data) {
						console.log(data);
						$("#tab_pagados").append("<h5>" + data[i].alumno + "</h5><table class='table' id='tabla-pagados" + data[i].id_mat  + "'></table>");
					
						$('#tabla-pagados' + data[i].id_mat).dataTable({
							 data : data[i].pagos,
							 aaSorting: [],
							 destroy: true,
							 bLengthChange: false,
							 bPaginate: false,
							 bFilter: false,
							 select: true,
					         columns : [ 
								   {"title":"Recibo", "data" : "nro_rec"},
								   {"title": "Mes", "data" : "mes"}, 
								   {"title": "Fec. pago", "data" : "fec_pago"}, 
						           {"title":"Monto", "data" : "monto","render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}},
						           {"title":"Desc. x hermano", "data" : "desc_hermano","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           {"title":"Desc. pronto pago", "data" : "desc_pronto_pago","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           {"title":"Desc. administrativo", "data" : "desc_personalizado","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           {"title":"Monto total", "data" : "monto_total", 
						        	   "render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}
						           },
						           {"title":"Boleta", "data" : "nro_rec", 
						        	   "render":function ( data, type, row ) {
						        		   if (row.nro_rec.indexOf("B")=='0' || row.nro_rec.indexOf("F")=='0')
						        			   return '<a href="#" title="RE-IMPRESION" onclick="reimprimir(\'' +data +'\',\'' +  row.banco + '\')"><i class="fa fa-print" style="color:blue"></i></a>&nbsp;&nbsp;&nbsp;<a href="#" data-id_fmo="' +  row.id_fmo + '" onclick="pdf(event,this)" title="DESCARGA PDF"><i class="fa fa-file-pdf-o" style="color:red"></i></a>'; 
						        		   else
						        			   return '';
						        	   }
						           }
					        ]
					    });
					}
			
					
				}
		);
		
		
}

function reimprimir(nro_rec, banco){
	var url;
	console.log(">" + banco  + "<-");
	if(banco==null ||banco=="null" || banco=="")
		url = 'api/movimiento/imprimir/mensualidad/'+ nro_rec;
	else
		url = 'api/banco/imprimir/mensualidad/'+ nro_rec;
	
	_post_silent(url ,null,
			function(data){
				_post_json( "http://localhost:8081/api/print", data.result, 
					function(data){
						console.log(data);
						//$("#_btnRefresh").click();
					}
				);
		}
	);
	
}

/** Se ejecuta siempre la primera vez **/
$(function() {
	
	
	 new PNotify({
         title: 'Ayuda',
         text: 'Para seleccionar una mensualidad, seleccione al menos un mes debajo del alumno.',
         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
         delay:1500
     });
	 
	$("#btnPagar").on('click',function() {
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
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
								
								var pago_id = "";
								var pago_monto = "";
								var pago_descuento = "";
								var pago_descuento_secretaria = "";
								var pago_descuento_personalizado = "";
								$(".resumen").each(function(index){
									pago_id = pago_id + "|" + $(this).data("resumen-id");
									pago_monto = pago_monto +"|" + $(this).data("monto");
									pago_descuento = pago_descuento + "|" + $(this).data("descuento_hermano");
									pago_descuento_secretaria = pago_descuento_secretaria + "|" + $(this).data("descuento_secretaria");
									pago_descuento_personalizado = pago_descuento_personalizado + "|" + $(this).data("descuento_personalizado");
								});
								
								$("#frm-pagos-resumen #id").val(pago_id);
								$("#frm-pagos-resumen #id_suc").val($('#_id_suc').text());
								$("#frm-pagos-resumen #monto").val(pago_monto);
								$("#frm-pagos-resumen #descuento").val(pago_descuento);
								$("#frm-pagos-resumen #descuento_secretaria").val(pago_descuento_secretaria);
								$("#frm-pagos-resumen #descuento_personalizado").val(pago_descuento_personalizado);
								$("#btnPagar").attr("disabled", "disabled");
								_post( "api/pagos/pagar", $("#frm-pagos-resumen").serialize(), 
										function(data){
											console.log(data);
											data.result.cabecera.codigoBarras="PENDIENTE";
											_post_json( "http://localhost:8081/api/print", data.result, 
													function(data){
														console.log(data);
														$("#_btnRefresh").click();
														$("#btnPagar").removeAttr("disabled"); 
													}
											);
											
											//$("#_btnRefresh").click();
										}
								);
								
								
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

	
		
	
});

function carritoMesELiminar(id) {
	var mens = $('[data-resumen-id="' + id + '"]').data("mens");
	$('[data-resumen-id="' + id + '"]').remove();

	$('[data-mens="' + (parseInt(mens) - 1) + '"]').removeAttr("disabled");

	//
	$('[data-mens="' + parseInt(mens) + '"]').removeAttr("disabled");
	$('[data-mens="' + (parseInt(mens) + 1) + '"]').attr("disabled", "disabled");

	$('[data-mens="' + parseInt(mens) + '"]').removeClass("bg-success-600");
	$('[data-mens="' + parseInt(mens) + '"]').addClass("bg-orange");

	$('[data-mens="' + parseInt(mens) + '"]').find("i").addClass("fa fa-square-o");
	$('[data-mens="' + parseInt(mens) + '"]').find("i").removeClass("glyphicon glyphicon-ok");
	carritoMesActualizarResumen();
}

var _toPrecision = function( number , precision ){
    var prec = Math.pow( 10 , precision );
    return Math.round( number * prec ) / prec;
}

function carritoMesActualizarResumen() {

	
	// se recalcula el total a pagar
	var total = 0;
	var meses_seleccionados = 0;
	$("detalle_mes").each(function(index) {
		total = total + parseFloat($(this).text());
		meses_seleccionados = meses_seleccionados + 1;
	});
	$("detalle_secre").each(function(index) {
		total = total - parseFloat($(this).text());
	});
	$("detalle_desc").each(function(index) {
		total = total - parseFloat($(this).text());
	});
	$("detalle_personalizado").each(function(index) {
		total = total - parseFloat($(this).text());
	
	});
	$("detalle_12").each(function(index) {
		var texto = $(this).text();
		
		var valor = parseFloat(texto);
		//alert("detalle_12:" +parseFloat(parseFloat($(this).text()).toFixed(1)));
		total = total -valor;
		//alert("total:" + total);

	});

	$("resumen_total").html($.number(total, 2));
	
	//alert(meses_seleccionados);
	
	if(meses_seleccionados==0)
		$("#div-resumen").css("display","none");
	else
		$("#div-resumen").css("display","block");
}
function carritoMesAgregar(field) {
	
	if (field.find(".glyphicon-ok").length>0){
		//ELIMINAR MES SELECCIONADO
		carritoMesELiminar(field.data("id"));
		
	}else{
		//AGREGAR MES SELECCIONADO
		var id_mat = field.data('id_mat');
		var id = field.data('id');
		var mens = field.data('mens');
		var descuento_secretaria = field.data('descuento_secretaria');
		var descuento_hermano = field.data('descuento_hermano');
		var descuento_personalizado = field.data('descuento_personalizado');
		var descuento_personalizado_id = field.data('descuento_personalizado_id');
		var descuento_12 = field.data('descuento_12');

		//alert("descuento_hermano:" + descuento_hermano);
		var monto = field.data('monto');
		var alumno = field.data('alumno');
		
		var detallePago = '<div class="form-group div-detalle-pago"> '
				+ '<label class="control-label col-lg-8">Mensualidad de mensualidad_desc, alumno</label>'
				+ '<div class="col-lg-2">'
				+ '<span class="label label-success position-right">S/.<detalle_mes>mensualidad</detalle_mes></span>'
				+ '</div>'
				+ '<div class="col-lg-2">&nbsp;</div>' + '</div>';

		

		if (parseFloat(descuento_personalizado) != 0) {
			//SI TIENE DESCUENTO PERSONALIZADO NO SE APLICAN OTROS TIPOS DE DESCUENTO
			//alert(descuento_personalizado);
			detallePago = detallePago
					+ '<div class="form-group div-detalle-pago" > '
					+ '<label class="col-lg-8"><a href="#" class="text-danger" onclick="alumno_descuento_modal(' + descuento_personalizado_id + ',true)">Descuento administrativo</a></label>'
					+ '<div class="col-lg-2">'
					+ '<span class="label label-danger position-right">S/.-<detalle_personalizado>' +  $.number(descuento_personalizado, 2) + '</detalle_personalizado></span>'
					+ '</div>' + '<div class="col-lg-2">' + '</div>' + '</div>';
		}else{
			if (parseFloat(descuento_hermano) != 0) {
				
				//alert(descuento_hermano);
				
				detallePago = detallePago
						+ '<div class="form-group div-detalle-pago" > '
						+ '<label class="control-label col-lg-8">Descuento por hermano</label>'
						+ '<div class="col-lg-2">'
						+ '<span class="label label-danger position-right">S/.-<detalle_desc>descHermano</detalle_desc></span>'
						+ '</div>' + '<div class="col-lg-2">' + '</div>' + '</div>';
			}

			if (parseFloat(descuento_secretaria) != 0) {
				detallePago = detallePago
				+ '<div class="form-group div-detalle-pago" > '
				+ '<label class="control-label col-lg-8">Descuento por pronto pago</label>'
				+ '<div class="col-lg-2">'
				+ '<span class="label label-danger position-right">S/.-<detalle_secre>descuento_secretaria</detalle_secre></span>'
				+ '</div>' + '<div class="col-lg-2">' + '</div>' + '</div>';	
			}
			

			//alert( mens + "-" + descuento_12+ "-" + $('.resumen').length  );
			if (mens == 12 && descuento_12!="0" && $('.resumen').length ==9) {
				
				detallePago = detallePago
						+ '<div class="form-group div-detalle-pago" > '
						+ '<label class="control-label col-lg-8">Descuento anual</label>'
						+ '<div class="col-lg-2">'
						+ '<span class="label label-danger position-right">S/.-<detalle_12>descuento_12</detalle_12></span>'
						+ '</div>' + '<div class="col-lg-2">' + '</div>' + '</div>';
			}
			
		}
		

		//console.log($('[data-mens="' + mens + '"]').html());
		
		//field.attr("disabled", "disabled");
		field.removeClass("bg-orange");
		field.addClass("bg-success-600");

		field.find("i").removeClass("fa fa-square-o");
		field.find("i").addClass("glyphicon glyphicon-ok");
		$('[data-id_mat="' + id_mat + '"][data-mens="' + (parseInt(mens) - 1) + '"]').attr("disabled", "disabled");
		
		$('[data-id_mat="' + id_mat + '"][data-mens="' + (parseInt(mens) + 1) + '"]').removeAttr("disabled");
		
		
		detallePago = detallePago.replace("alumno", alumno);
		detallePago = detallePago.replace("mensualidad_desc", _MES[mens - 1]);
		detallePago = detallePago.replace("mensualidad", $.number(monto, 2));
		detallePago = detallePago.replace("descuento_secretaria", $.number(	descuento_secretaria, 2));
		detallePago = detallePago.replace("descHermano", $.number(descuento_hermano, 2));
		
		if (mens == 12 && descuento_12!="0" && $('.resumen').length == 9) {
			//descuento_12 = ( ( parseFloat(monto) +  parseFloat(descuento_hermano) - parseFloat(descuento_secretaria))*parseFloat(descuento_12))/100;
			descuento_12 = ( ( parseFloat(monto) -  parseFloat(descuento_hermano) - parseFloat(descuento_secretaria))*parseFloat(descuento_12))/100;
			//alert(descuento_12);
			//detallePago = detallePago.replace("descuento_12", $.number( descuento_12  , 2));
			detallePago = detallePago.replace("descuento_12", descuento_12  );
			
		}
		detallePago = '<div class="resumen" data-resumen-id="' + id + 
						'" data-mens="' + mens + 
						'" data-monto="' + monto + 
						'" data-descuento_hermano="' +descuento_hermano + 
						'" data-descuento_12="' +descuento_hermano + 
						'" data-descuento_personalizado="' +descuento_personalizado + 
						'" data-descuento_secretaria="'+  descuento_secretaria + '" >'
					+ detallePago + '</div>';

		$("#frm-pagos-resumen-mes").append(detallePago);

		carritoMesActualizarResumen();		
		
	}
	
}

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	document.location.href=_URL + "api/movimiento/pdf/boleta/" + id_fmo;

}
