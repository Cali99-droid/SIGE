function onloadPage(params){

}

$(function(){
	_onloadPage();	

	$("#fecha").focus();
	
	
	var fncExito = function(data){
		$('#panel-pagos').css("display","block");
		
		if (_usuario.id_per == 1 ){
			
			$('#tabla-pagos').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
		     	           {"title": "Local", "data" : "local"}, 
		     	           {"title": "Recibo", "data" : "nro_rec"}, 
		     	           {"title": "Mes", "data" : "concepto"}, 
		     	           {"title": "Contrato", "data" : "num_cont"}, 
		     	           {"title": "Alumno", "data" : "alumno"}, 
		     	           {"title": "Nivel", "data" : "nivel"}, 
		     	           {"title": "Grado", "data" : "grado"}, 
		     	           {"title": "Secci&oacute;n", "data" : "secc"}, 
		     	           {"title": "Fec. pago", "data" : "fec_pago"}, 
		    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
		    	        	   if (row.tipo=="I")
		    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
		    	        	   else 
		    	        		   return '<font color="red">S./-' + $.number( data, 2) + '</font>';
		    	        	   }
						   }
		        ],
		        
		    });
			
		}else{
			
			$('#tabla-pagos').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
		     	           {"title": "Recibo", "data" : "nro_rec"}, 
		     	           //{"title": "Contrato", "data" : "num_cont"}, 
		     	           {"title": "Concepto", "data" : "concepto"}, 
		     	           {"title": "Alumno", "data" : "alumno"}, 
		     	           {"title": "Nivel", "data" : "nivel"}, 
		     	           {"title": "Grado", "data" : "grado"}, 
		     	           {"title": "Secci&oacute;n", "data" : "secc"}, 
		     	           {"title": "Fec. pago", "data" : "fec_pago"}, 
		    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
		    	        	   if (row.tipo=="I")
		    	        		   return '<font color="blue">' + $.number( data, 2) + '</font>'; // return '<font color="blue">S./' + $.number( data, 2) + '</font>';
		    	        	   else 
		    	        		   return '<font color="red">-' + $.number( data, 2) + '</font>'; // return '<font color="red">S./-' + $.number( data, 2) + '</font>';
		    	        	   }
						   },
				           {"title":"Boleta", "data" : "nro_rec", 
				        	   "render":function ( data, type, row ) {
				        		   if (row.nro_rec.indexOf("B")=='0' || row.nro_rec.indexOf("F")=='0')
				        			   return '<a href="#" title="RE-IMPRESION" onclick="reimprimir(\'' +data +'\')"><i class="fa fa-print" style="color:blue"></i></a>&nbsp;&nbsp;&nbsp;<a href="#" target="_blank" data-id_fmo="' + row.id_fmo + '" onclick="pdf(event,this)" title="DESCARGA PDF"><i class="fa fa-file-pdf-o" style="color:red"></i></a>'; 
				        		   else
				        			   return '';
				        	   }
				           }
		        ],
		        "initComplete": function( settings ) {
		        	_dataTable_total(settings,'monto_total');
		        	
		        	$("<span><a href='#' target='_blank' onclick='printReporteBanco(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

				 }
		    });
			
		}

		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());

		return _get('api/pagos/banco/' + $("#_id_suc").html(),fncExito, $(this).serialize());
	});
	

	
});


function reimprimir(nro_rec){
	
	_post_silent('api/banco/imprimir/mensualidad/'+ nro_rec,null,
			function(data){
				_post_json( "http://localhost:8082/api/print", data.result, 
					function(data){
						console.log(data);
						//$("#_btnRefresh").click();
					}
				);
		}
	);
	
}

function printReporteBanco(event){
	event.preventDefault();
	document.location.href = _URL + "api/banco/excel/" + $("#_id_suc").html() + "?fec_ini=" + $('#fec_ini').val() + '&usuario=' + $("#usuario\\.login").text() + '&sucursal=' + $("#_sucursal").text() ;
}

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	//btn pdf boleta
	//document.location.href=_URL + "api/movimiento/pdf/boleta/" + id_fmo;
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo;
}