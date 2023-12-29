function onloadPage(params){
	var id_usr=_usuario.id;
	var rol = _usuario.roles[0];
	if (rol!='1'){
		$('#frm-pagos-buscar #id_usr').val(id_usr);
	}	
	
	var param = {id_usr:id_usr};
	_llenarComboURL('api/familiar/listarHijosFamilia', $('#id_per'),null, param, function() {
	});	
	
	_llenarComboURL('api/anio/listarAnios', $('#id_anio_des'),null, null, function() {
	});	
}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$("#div_pago").css('display','block');
		var pagos=data;
		$('#panel-alumnos').css("display","block");
		for (var i=0;i<data.length-1;i++){
					//agregar el mes a la sección alumno
			var last_div_pago= ($("#div_pago").last()).clone();
			
			$( "#pagos_resumen" ).append(last_div_pago);
		}
		$(".div_pago").each(function (index, value) {
			console.log($(this).html());		
			$(this).find("#concepto").text(data[index].concepto);
			if(data[index].canc==1){
				$(this).removeClass("bg-danger-300");
				$(this).addClass("bg-green-200");
				$(this).find("#fecha").text('Fecha de Pago: ');
				$(this).find("#lbl_fecha_est").text(_parseDate(data[index].fec_pago));
				$(this).find("#estado").text('CANCELADO: ');
				$(this).find("#lbl_monto").text(data[index].monto_total);
				$(this).find("#div_impresion").css('display','block');
				$(this).find("#btnImprimir").attr("data-id_fmo", data[index].id_fmo);
				$(this).find("#btnImprimir").attr("data-id_alu", data[index].id);
			} else if(data[index].canc==0){
				$(this).removeClass("bg-green-200");
				$(this).addClass("bg-danger-300");
				$(this).find("#fecha").text('Fecha de Vencimiento: ');
				$(this).find("#lbl_fecha_est").text(_parseDate(data[index].fec_venc));
				$(this).find("#estado").text('PENDIENTE: ');
				var monto_final=0;
				var monto=data[index].monto;
				var beca=data[index].desc_beca;
				if(beca!=null)
					monto_final=monto-beca;
				else
					monto_final=monto;
				$(this).find("#lbl_monto").text(monto_final);
				$(this).find("#div_impresion").css('display','none');
			}	
		});	
		/*for(var k in pagos) {
			var detallePago = '<div class="form-group div-detalle-pago"> '
				+ '<label class="control-label col-lg-8">Mensualidad de mensualidad_desc, alumno</label>'
				+ '<div class="col-lg-2">'
				+ '<span class="label label-success position-right">S/.<detalle_mes>mensualidad</detalle_mes></span>'
				+ '</div>'
				+ '<div class="col-lg-2">&nbsp;</div>' + '</div>';
		$("#frm-pagos-resumen-mes").append(detallePago);		
		$('#panel-alumnos').css("display","block");
		}*/	
		
		
		/*$('#tabla-alumno_pagos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           //{"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
					   {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
	    	        	   if (row.tipo=="I")
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   else 
	    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   }
	     	           {"title": "Usuario", "data" : "login"}, 
					   {"title": "Recibo", "data" : "nro_rec"}, 
	    	           {"title": "Concepto", "data" : "concepto"},
	    	           {"title": "Fecha", "data" : "fecha"},
	     	           {"title": "Alumno/Observacion", "data" : "obs"}, 
	     	           {"title": "Nivel", "data" : "nivel"}, 
	     	           {"title": "Grado", "data" : "grado"}, 
	     	           {"title": "Seccion", "data" : "secc"}, 
	    	           /*{"title": "Tipo", "data" : "tipo","render":function ( data, type, row,meta ) {
	    	        	   if (data =="I")
	    	        		   return "Ingreso"
	    	        	   else
	    	        	       return "Salida"
	    	           		}
					   },*/
	    	/*           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
	    	        	   if (row.tipo=="I")
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   else 
	    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   }
	        ],
	        "initComplete": function( settings ) {
	        	_dataTable_total(settings,'monto_total');
				//_initCompleteDT(settings);
				/*var table = $('#tabla-alumnos').DataTable();
				table.on( 'search.dt', function () {
					_dataTable_total(settings,'monto_total');
					//alert();
				//$('#filterInfo').html( 'Currently applied global search: '+table.search() );
				} );*/
	        	
	       /* 	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });*/
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		//$("#div_pago").last().remove();
		$("#div_pago").css('display','none');
		$("#id_suc").val($("#_id_suc").html());
		var param = {id_anio_des: $("#id_anio_des").val(), id_per: $("#id_per").val()}
		//return _get('api/alumno/listarPagosxAlumno',fncExito, $(this).serialize());
		return _get('api/alumno/listarPagosxAlumno',fncExito, param);
	});
	

	
});

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	var id_alu = $(o).data('id_alu');
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo+"/"+id_alu;

}
