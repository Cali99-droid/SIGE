function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));
	//_llenarCombo('cat_curso',$('#id_cur'));

}
if( _usuario.id_tra==3){
	_llenarCombo('cat_nivel',$('#id_niv'));
	_llenarCombo('cat_curso',$('#id_cur'));
} else{
	_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),null, null, function(){
		$('#id_niv').change();
	});
	$('#id_niv').on('change',function(event){
	var param2 = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val()};
	_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), null,param2,function(){$('#id_cur').change();});
	});
}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$('#col_reporte_capacidades-panel').css("display","block");
		
		$('#col_reporte_capacidades').dataTable({
			 data : data,
			 //aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Competencia", "data" : "competencia"},
	     	           {"title": "Capacidad", "data" : "capacidad"}
	        ],
	        "initComplete": function( settings ) {

	        	$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	$('#frm-reporte-capacidades').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var id_niv=$('#id_niv').val();
		var id_suc=$('#id_suc').val();
		return _get('api/competencia/consulta?id_niv='+id_niv+'&id_suc'+id_suc,fncExito, $(this).serialize());
	});
	

	
});
