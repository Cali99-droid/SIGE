function onloadPage(params){
	
	$('#btn-envio-sunat').on('click',function(event){
		event.preventDefault();

		
		return _post('api/facturaElectronica/envioResumenDiario', $('#frm-pagos-buscar').serialize(),

				function(data){
					var result = data.result;
					console.log(result);
					var id_res = result.res_id;
					var ticket = result.ticket;
					if (result.code!='0'){
						
						var delayInMilliseconds = 1000; //5 second
						_alert('Espere unos segundos, hasta que sunat responda.');

						setTimeout(function() {
								//_blockUI(true);
									
						
								_post('api/facturaElectronica/respuestaTicket', $('#frm-pagos-buscar').serialize() + "&id_res="+ id_res +"&ticket=" + ticket,function(data){
								//_blockUI(false);
								$('#frm-pagos-buscar').submit();
								
							});
							
								},
								delayInMilliseconds);
						
						
						
					}else
						 $('#frm-pagos-buscar').submit();
						 
				}
		);
		 
		 /*
					 var id_res = '344';
					 var ticket = '201903620957192';
					 var id_eiv = 17167;
							 
							return _post('api/facturaElectronica/respuestaTicket', $('#frm-pagos-buscar').serialize() + "&id_res="+ id_res +"&ticket=" + ticket + "&id_eiv=" +id_eiv,function(data){
								$('#frm-pagos-buscar').submit();
							});
						 
						*/
		 
				 
		 
	});
	
}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$('#panel-alumnos').css("display","block");
		
		$('#tabla-alumnos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Recibo", "data" : "nro_rec"}, 
	    	           {"title": "Fecha", "data" : "fecha"},
	     	           {"title": "Alumno/Observacion", "data" : "obs"}, 
	     	          /*{"title": "Nivel", "data" : "nivel"}, 
	     	           {"title": "Grado", "data" : "grado"}, 
	     	           {"title": "Seccion", "data" : "secc"}, 
	    	           {"title": "Tipo", "data" : "tipo","render":function ( data, type, row,meta ) {
	    	        	   if (data =="I")
	    	        		   return "Ingreso"
	    	        	   else
	    	        	       return "Salida"
	    	           		}
					   },*/
	    	           {"title": "Monto", "data" : "monto","render":function ( data, type, row ) {
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   },
					   {"title": "Descuento", "data" : "descuento","render":function ( data, type, row ) {
    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
    	        	   		}
					   },					   
					   {"title": "Monto Total", "data" : "monto_total","render":function ( data, type, row ) {
    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
    	        	   		}
					   },
					   {"title": "Estado SUNAT", "render":function ( data, type, row ) {
						   if (row.cod_res!=null && row.cod_res!=""){
							   return "ENVIADO SUNAT";
    	        	   		}else{
    	        	   			return "PENDIENTE ENVIO";
    	        	   		}
					   }},		
					    {"title": "Estado ENVIO", "render":function ( data, type, row ) {
						   if (row.est_env!=null && row.est_env!=""){
							   return "<font color='green'>ENVIADO</font>";
    	        	   		}else{
    	        	   			return "PENDIENTE ENVIO";
    	        	   		}
					   }},		
					    {"title": "Fecha ENVIO", "render":function ( data, type, row ) {
						   if (row.fecha_envio!=null && row.fecha_envio!=""){
							   return row.fecha_envio;
    	        	   	   }else{
    	        	   			return "";
    	        	   	   }
					   }},		
					   {"title": "Resumen diario SUNAT", "data" : "cod_res","render":function ( data, type, row ) {
						   if (data ==null || data=="")
							   return "<input type='hidden' class='cod_res_pen'/><font color='red'>PENDIENTE ENVIO A SUNAT</font>";
						   else
							   return "<input type='hidden' class='cod_res_proc'/>" + data;
    	        		   ;
	        	   		}}, 
	        ],
	        "initComplete": function( settings ) {
	        	_dataTable_total(settings,'monto_total');
	        	
	        	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
	        	 var recibo_pendientes= $('.cod_res_pen').length;
	        	var recibo_enviados= $(".cod_res_proc").length;
	        	
	        	if(recibo_pendientes>0){
		        	$('#btn-envio-sunat').css('display','inline-block');
		        	$('#divFechaEnvio').css('display','inline-block');
	        	}else{
		        	$('#btn-envio-sunat').css('display','none');
		        	$('#divFechaEnvio').css('display','none');
	        		
	        	}
			 }

	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/facturaElectronica/pendientesResumenDiario',fncExito, $(this).serialize());
	});
	

	
});

function printReporteCaja(event){
	event.preventDefault();
	document.location.href = _URL + "api/reporteCaja/excel?fec_ini=" + $("#fec_ini").val()+"&fec_fin="+$("#fec_fin").val() + "&usuario=" + $("#usuario\\.login").text() + "&sucursal=" + $("#_sucursal").text() + "&id_suc=" + $("#_id_suc").html();
}
