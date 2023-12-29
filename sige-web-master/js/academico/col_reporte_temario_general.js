function onloadPage(params){

	_llenarCombo('cat_nivel',$('#id_niv'));
	_llenarCombo('cat_curso',$('#id_cur'));

	$('#id_niv').on('change',function(event){
		var param2 = {id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val()};
		_llenarComboURL('api/curso/listarCursosxNivel',$('#id_cur'), null,param2);
	});
}



function imprimiReporte(e){
	e.preventDefault();
	document.location.href = _URL + "api/tema/excel?id_niv=" + $("#id_niv").val()+"&id_cur="+$("#id_cur").val()+"&id_anio="+$('#_id_anio').text() ;

}

$(function(){
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$('#col_reporte_tema-panel').css("display","block");
		
		$('#col_reporte_tema').dataTable({
			 data : data,
			 //aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	     	           {"title": "Tema", "data" : "tema"},
	     	           {"title": "Subtema", "data" : "subtema"}
	        ],
	        "initComplete": function( settings ) {

	        	$("<span><a href='#' target='_blank' onclick='imprimiReporte(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	$('#frm-reporte-tema').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var id_niv=$('#id_niv').val();
		var id_suc=$('#id_suc').val();
		return _get('api/tema/consulta?id_niv='+id_niv+'&id_suc='+id_suc+'&id_anio='+$('#_id_anio').text(),fncExito, $(this).serialize());
	});
	

	
});
