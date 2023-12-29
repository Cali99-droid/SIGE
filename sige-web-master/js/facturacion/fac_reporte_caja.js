function onloadPage(params){
	var id_usr=_usuario.id;
	var rol = _usuario.roles[0];
	if (rol!='1'){
		$('#frm-pagos-buscar #id_usr').val(id_usr);
	}	
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
	    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
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
	        	
	        	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/reporteCaja/consulta',fncExito, $(this).serialize());
	});
	

	
});

function printReporteCaja(event){
	event.preventDefault();
	document.location.href = _URL + "api/archivo/excel?fec_ini=" + $("#fec_ini").val()+"&fec_fin="+$("#fec_fin").val() + "&usuario=" + $("#usuario\\.login").text() + "&sucursal=" + $("#_sucursal").text() + "&id_suc=" + $("#_id_suc").html()+"&id_usr="+$("#id_usr").val();
}
