//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
function onloadPage(params) {
	var id_suc=$("#_id_suc").text();
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		_llenarCombo('ges_sucursal',$('#id_suc'));	
	} else{
		_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc);	
	}
			
}
_onloadPage();	
/*$('#reporte_infocorp-frm').on('submit', function(e) {
	e.preventDefault();
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
		
	var param= {id_anio: $('#_id_anio').text(), id_mes: $('#id_mes').val(), id_suc: id_suc};
	
	reporte(param);
});*/

var fncExito = function(data){
	$('#panel-notaCredito').css("display","block");
	
	$('#tabla-notaCredito').dataTable({
		 data : data,
		 //aaSorting: [],
		 order: [[ 1, "asc" ]],
		 destroy: true,
		 pageLength: 1000,
		 select: true,
         columns : [ 
     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
     	           {"title": "Recibo", "data" : "nro_rec"}, 
     	           {"title": "Recibo Afectado", "data" : "nro_rec_afe"}, 
    	           {"title": "Concepto", "data" : "motivo"},
    	           {"title": "Monto", "data" : "monto"},
    	           {"title": "Fecha Emisi&oacute;n", "data" : "fec_emi"},
     	           {"title": "Alumno", "data" : "alumno"}, 
     	           {"title": "Nivel", "data" : "nivel"}, 
     	           {"title": "Grado", "data" : "grado"}, 
     	           {"title": "Seccion", "data" : "secc"}
    	           /*{"title": "Tipo", "data" : "tipo","render":function ( data, type, row,meta ) {
    	        	   if (data =="I")
    	        		   return "Ingreso"
    	        	   else
    	        	       return "Salida"
    	           		}
				   },*/
    	          /* {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
    	        	   if (row.tipo=="I")
    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
    	        	   else 
    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
    	        	   }
				   }*/
        ],
        "initComplete": function( settings ) {
		 }

    });
	
};

$('#reporte_nota_credito-frm').on('submit',function(event){
	event.preventDefault();
	return _get('api/notaCredito/listarNotasCredito',fncExito, $(this).serialize());
});

