function onloadPage(params){
	$('#btn-excel').on('click',function(){
		document.location.href = _URL + 'api/archivo/reporte/xls?fec_ini=' + $('#fec_ini').val() + '&fec_fin=' + $('#fec_fin').val()+'&fec_ini_env=' + $('#fec_ini_env').val() + '&fec_fin_env=' + $('#fec_fin_env').val() +'&tip_com=' + $('#tip_com').val()+'&nro_serie=' + $('#nro_serie').val() + '&enviadoSunat=' + $('#enviadoSunat').val();
	});
	
	$('#fec_ini_env').attr('disabled','disabled');
	$('#fec_fin_env').attr('disabled','disabled');
	$('#fec_ini_env').val('');
	$('#fec_fin_env').val('');
	$('input[name="tip_busqueda"]').change(function() {	
		var valor=$('input[name="tip_busqueda"]:checked').val();
		if(valor=='I'){
			$('#fec_ini').removeAttr('disabled');
			$('#fec_fin').removeAttr('disabled');
			$('#fec_ini_env').attr('disabled','disabled');
			$('#fec_fin_env').attr('disabled','disabled');
			$('#fec_ini_env').val('');
			$('#fec_fin_env').val('');
		} else if(valor=='E'){
			$('#fec_ini_env').removeAttr('disabled');
			$('#fec_fin_env').removeAttr('disabled');
			$('#fec_ini').attr('disabled','disabled');
			$('#fec_fin').attr('disabled','disabled');
			$('#fec_ini').val('');
			$('#fec_fin').val('');
		}	
	});
	
	/*$('#fec_ini_env').on('click', function(event) {
		
	});	
	
	$('#fec_ini').on('click', function(event) {
		alert();
		
	});	*/
	
	$('#tip_com').on('change', function(event) {
		var tip=$('#tip_com').val();
		if(tip=='B'){
			_llenarComboURL('api/pagos/listarSeriesBoletas',$('#nro_serie'), null, null, function() {				
				$('#nro_serie').change();
			});
		} else if(tip=='N'){
			_llenarComboURL('api/pagos/listarSeriesNotasCredito',$('#nro_serie'), null, null, function() {				
				$('#nro_serie').change();
			});
		}		
	});
	
	$('#tip_com').change();
	
	$('#btn-excel').attr('disabled','disabled');
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
	    	           {"title": "Fecha Emisi&oacute;n", "data" : "fec","render":function ( data, type, row,meta ) {
	    	        	   return _parseDate(data);
	    	           }
	    	           	}, 
	    	           {"title": "Fecha Envio SUNAT", "data" : "fec_sunat","render":function ( data, type, row,meta ) {
	    	        	   if (data!=null )
	    	        		   return _parseDate(data);
	    	        	   else 
	    	        		   return "";
	    	           }
	    	           	},
						{"title": "Recibo Afectado", "data" : "monto","render":function ( data, type, row ) {
							if(row.nro_rec_afec!=null)
	    	        		   return row.nro_rec_afec;
						   else
							   return '';
	    	        	   }
					   },
						/*{"title": "Recibo Afectado", "data" : "nro_rec_afec","render":function ( data, type, row,meta ) {
							console.log(data);
	    	        	   if (data!=null )
	    	        		   return row.data;
	    	        	   else 
	    	        		   return "";
	    	           }
	    	           	},*/
						{"title": "Fecha Recibo Afectado", "data" : "fec_rec_env","render":function ( data, type, row,meta ) {
	    	        	   if (data!=null )
	    	        		   return _parseDate(data);
	    	        	   else 
	    	        		   return "";
	    	           }
	    	           	},
	     	           {"title": "Observacion", "data" : "obs"}, 
	     	           /*{"title": "Tipo", "data" : "tipo","render":function ( data, type, row,meta ) {
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
    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
    	        	   		}
					   },					   
					   {"title": "Monto Total", "data" : "monto_total","render":function ( data, type, row ) {
    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
    	        	   		}
					   }, {"title": "Estado SUNAT", "render":function ( data, type, row ) {
						   if (row.cod_res!=null && row.cod_res!=""){
							   return "<font color='blue'>ENVIADO SUNAT</font>";
    	        	   		}else{
    	        	   			return "<font color='red'>PENDIENTE ENVIO</font>";
    	        	   		}
					   }},		
					   {"title": "Correlativo SUNAT", "data" : "cod_res","render":function ( data, type, row ) {
						   if (data ==null || data=="")
							   return "<input type='hidden' class='cod_res_pen'/><font color='red'></font>";
						   else
							   return "<input type='hidden' class='cod_res_proc'/>" + data;
    	        		   ;
	        	   		}}					   
	        ],
	        "initComplete": function( settings ) {
	        	_dataTable_total(settings,'monto_total');
	        	
	        	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

	        	$('#btn-excel').removeAttr('disabled');
			 }

	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/facturaElectronica/consulta',fncExito, $(this).serialize());
	});
	

	
});

function printReporteCaja(event){
	event.preventDefault();
	//document.location.href = _URL + "api/archivo/excel?fec_ini=" + $("#fec_ini").val()+"&fec_fin="+$("#fec_fin").val() + "&usuario=" + $("#usuario\\.login").text() + "&sucursal=" + $("#_sucursal").text() + "&id_suc=" + $("#_id_suc").html();
	document.location.href = _URL + "api/archivo/excel?fec_ini=" + $("#fec_ini").val()+"&fec_fin="+$("#fec_fin").val() + "&usuario=" + $("#usuario\\.login").text() + "&sucursal=" + $("#_sucursal").text() + "&id_suc=" + $("#_id_suc").html()+"&id_usr="+$("#id_usr").val();
}
