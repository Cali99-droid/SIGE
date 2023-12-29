function onloadPage(params){
	
	//_llenarCombo('ges_sucursal',$('#id_suc'),null,null);
	//_llenarCombo('cat_grad',$('#id_gra'),null,null,function(){$('#id_gra').change()});	
	
	
	
	//$("#id_gra").on("change", function() {
	//	$("#frm-mat_consolidado").submit();	
	//});
	
	$('#id_gra').on('change',function(event){
		$('#frm-mat_consolidado').submit();	
	});	
	
	var id_anio=$('#_id_anio').text()
	$('#id_niv').on('change',function(event){
		//if(com_seleccionado!=null){
			if($('#id_niv').val()!=''){
				_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
			}
			$("#id_gra").change();			
		//}			
	});	
	
	$('#id_gir').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
			_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+_usuario.id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){
				$('#id_niv').find('option[value=""]').remove();
				$('#id_niv').change();
				});
		} else {
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		}	
		
	});	
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null);
	} else {
		_llenarComboURL('api/usuario/listarLocales/'+_usuario.id,$('#id_suc'),null,null,function(){$('#id_suc').change();}); //
	}	
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}
	
	/*if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		var param={id_anio:$('#_id_anio').text()};
	} else if(_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		var param={id_anio:$('#_id_anio').text(),id_suc : $("#_id_suc").text(), id_niv: $("#id_niv").val()};
	} else{
		var param={id_anio:$('#_id_anio').text(),id_suc : $("#_id_suc").text()};
	}
		return _get('api/matricula/reporteConsolidadoGrado',fncExito, param);*/
	
	
}

$('#frm-mat_consolidado').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var param={id_anio:$('#_id_anio').text(),id_suc : $("#id_suc").val(), id_niv: $("#id_niv").val(), id_gir:$("#id_gir").val(), id_gra:$("#id_gra").val()};
		return _get('api/matricula/reporteConsolidadoGeneralGrado',fncExito, param);
	});

var fncExito = function(data){
	//$('#col_reporte_reserva-panel').css("display","block");
	console.log(data);
	$('#col_mat_consolidado').dataTable({
		 data : data,
		 //aaSorting: [],
		 destroy: true,
		 pageLength: 1000,
		 bFilter: true,
		 select: true,
         columns : [ 
     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
     	           {"title": "Giro", "data" : "giro"},
				   {"title": "Local", "data" : "sucursal"},
     	           {"title": "Nivel", "data" : "nivel"},
     	           {"title": "Grado", "data" : "nom"},
     	           //{"title": "Sección", "data" : "secc"},
     	           {"title": "Capacidad", "data" : "total_capacidad", "className": "text-center"},
				    {"title": "Inscripciones Vac.", "data" : "matriculas_vacante","className": "text-center"},
     	           {"title": "Matriculados", "data" : "matriculados","className": "text-center"},
				   {"title": "Matr. Anteriores", "data" : "matriculas_ant","className": "text-center"},
     	           {"title": "Sugeridos", "data" : "sugeridos","className": "text-center"},
     	           {"title": "Reservas", "data" : "reservas","className": "text-center"},
				   {"title": "No Ratificaron", "data" : "no_ratificados","className": "text-center"},
     	           {"title": "Trasladados", "data" : "trasladados","className": "text-center"},
     	           {"title": "Retirados", "data" : "retirados","className": "text-center"},
     	           {"title": "Fallecidos", "data" : "fallecidos","className": "text-center"},
     	           {"title": "Vacantes", "data" : "vacantes","className": "text-center"},
        ],
        "initComplete": function( settings ) {
        	 _initCompleteDT(settings);
        	$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

		 },
	"footerCallback": function ( row, data, start, end, display ) {
        var api = this.api(), data;
        // Remove the formatting to get integer data for summation
        var intVal = function ( i ) {
            return typeof i === 'string' ?
                i.replace(/[\$,]/g, '')*1 :
                typeof i === 'number' ?
                    i : 0;
        };
        
        var i;
        for(i=5; i<=15; i++){
        	   // Total over all pages
            total = api
                .column( i)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
            console.log(total);

            // Total over this page
            pageTotal = api
                .column( i, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
            console.log(pageTotal);

            // Update footer
            $( api.column( i ).footer() ).html(       pageTotal       );
        }

     
    }
    });
	
};
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
function _onchangeAnio(id_anio, anio){
	//$("#id_gra").change();	
	var param={id_anio:$('#_id_anio').text()};
	return _get('api/matricula/reporteConsolidadoGeneralGrado',fncExito, param);
}

/*$(function(){
	//alert(1);
	_onloadPage();	

	
	
	/*$('#frm-mat_consolidado').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		var param={id_anio:$('#_id_anio').text(), id_suc:$('#id_suc').val(), id_grad:$('#id_gra').val()};
		return _get('api/matricula/reporteConsolidado',fncExito, param);
	});*/
	

	
//});
