function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	

	_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
		$('#id_suc').change();
	});
		
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});
	

	/*$('#rep_com').on('click',function(){
	   if(this.checked){
		 $('#rep_com').val(1);
	   } else {
		 $('#rep_com').val(0);
	   }	   
	});*/
	
}



$(function(){
	_onloadPage();	

	
	
});
var fncExito = function(data){
		
		$('#mat_reporte_contratos').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 scrollY:        true,
			 scrollX:        true,
			 scrollCollapse: true,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					    {"title": "Nª Contrato", "data" : "contrato"},
	     	           {"title": "Trabajador", "data" : "trabajador"},
					   {"title": "DNI", "data" : "nro_doc"},
					  
					   {"title": "Fecha Inicio","render":function ( data, type, row ) {
						   if(row.fec_ini!=null){
							     return _parseDate(row.fec_ini); 
						   } else {
							  return ''; 
						   }

					   }
	    	       
					   },
					   {"title": "Fecha Fin","render":function ( data, type, row ) {
						    if(row.fec_fin!=null){
								 return _parseDate(row.fec_fin); 
							} else{
								return '';
							}	
					   }	    	       
					   },
					   {"title": "Giro", "data" : "giro"},
					   {"title": "Puesto", "data" : "puesto"},
					   {"title": "Denominación", "data" : "denominacion"},
					   {"title": "Remuneración", "data" : "rem"}
	     	           
	        ],
	        "initComplete": function( settings ) {

	        	//$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	
$('#frm-reporte-contratos').on('submit',function(event){
		event.preventDefault();
		
	
		var param={ id_anio:$('#_id_anio').text(), tip_con:$('#tip_con').val(), id_gir:$('#id_gir').val()};
		return _get('api/trabajador/listarContratosxAnio',fncExito, param);
	});
	


