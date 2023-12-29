function onloadPage(params){
	var id_usr=_usuario.id;
	_llenarCombo('ges_giro_negocio', $('#id_gir'), null, null, function() {
	});
}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$('#panel-descuentos').css("display","block");
		
		$('#tabla-descuentos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Alumno", "data" : "alumno"}, 
					   {"title": "Nivel", "data" : "nivel"}, 
	    	           {"title": "Grado", "data" : "grado"},
	    	           {"title": "Secc", "data" : "secc"},
					   {"title": "Cuota", "data" : "mens"},
	     	           {"title": "Monto", "data" : "monto"}, 
	     	           {"title": "Descuento", "data" : "descuento"}, 
	     	           {"title": "Monto Total", "data" : "monto_total"}, 
	     	           {"title": "Descripción", "data" : "des"}
	        ],
	        "initComplete": function( settings ) {


			 }

	    });
		
	};
	
	$('#frm-reporte-descuentos').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var id_anio=$('#_id_anio').text();
		return _get('api/pagos/listarDescuentosAlumno/'+id_anio+'/'+$('#id_gir').val(),fncExito, $(this).serialize());
	});
	

	
});

