function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));
	$('#id_suc').on('change',function(event){
		//_llenarCombo('ges_servicio',$('#id_nvl'),null,$(this).val(),function(){$("#id_nvl").change()});
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	});
	//_llenarCombo('cat_nivel',$('#id_niv'));
	$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});


	$('#id_gra').on('change',function(event){
		var id_suc=$("#id_suc").val();
		_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
	});
	_llenarCombo('ges_sucursal',$('#id_suc'),null,null, function(){$("#id_suc").change()});
		
	
	
}

/*if( _usuario.id_tra==3){
	_llenarCombo('cat_nivel',$('#id_niv'));
	_llenarCombo('cat_curso',$('#id_cur'));
} else{
	_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'));
	$('#id_niv').on('change',function(event){
	var param2 = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val()};
	_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), null,param2,function(){$('#id_cur').change();});
	});
}*/
$(function(){
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		//$('#col_reporte_reserva-panel').css("display","block");
		
		$('#col_reporte_reserva').dataTable({
			 data : data,
			 //aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	     	           {"title": "Nro. Reserva", "data" : "nro_recibo"},
	     	           {"title": "Alumno", "data" : "alumno"},
	     	           {"title": "Nivel", "data" : "nivel"},
	     	           {"title": "Grado", "data" : "gra"},
	     	           {"title": "Sección", "data" : "secc"},
	     	           {"title": "Fecha Reserva", "data" : "fec"},
	     	           {"title": "Fecha Límite", "data" : "fec_lim"},
	     	           {"title": "Matrícula", "data" : "matricula"}
	        ],
	        "initComplete": function( settings ) {

	        	$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	$('#frm-reporte-reserva').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var param={id_au:$('#id_au').val(), mat:$('#mat').val(), id_niv:$('#id_nvl').val(), id_gra:$('#id_gra').val(), id_suc:$('#id_suc').val(), id_anio:$('#_id_anio').text()};
		return _get('api/reserva/listarReserva',fncExito, param);
	});
	

	
});
