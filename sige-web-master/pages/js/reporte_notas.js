//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
function onloadPage(params) {
	
	$('#id_niv').on('change',function(event){
		var param = {id_niv: $('#id_niv').val() , id_anio: $('#_id_anio').text(), id_gir:$('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'),null,param);
	});
	//_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){ $('#id_niv').change()});
	$('#id_gir').on('change',function(event){
			_llenarComboURL('api/disenioCurricular/listarNivelesComboxGiro/'+$("#_id_anio").text()+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){$('#id_niv').change();});	
		
	});	
	/*if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		_llenarCombo('cat_nivel',$('#id_niv'),null,null,function(){$('#id_niv').change();});
	}else{
		var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};
		_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),null,param,function(){$('#id_niv').change();});
	}*/
	_llenarCombo('ges_sucursal',$('#id_suc'));
	$('.daterange-single').daterangepicker({ 
	    singleDatePicker: true,
	    locale: { format: 'DD/MM/YYYY'},
	});
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'), null, null,function() {
			$('#id_gir').change();
	});	
}

$('#reporte_nota-frm').on('submit', function(e) {
	e.preventDefault();
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param= {id_cpu:$('#id_cpu').val(), id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val(), id_suc:  $('#id_suc').val(), id_tra:null, est:$('#est').val(), id_gir:$('#id_gir').val()};
	}else{
		var param= {id_cpu:$('#id_cpu').val(), id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val(), id_suc:  $('#id_suc').val(), id_tra:_usuario.id_tra, est:$('#est').val(), id_gir:$('#id_gir').val()};	
	}
	reporte(param);
});


function reporte(param){
	_get('api/nota/reporteNota/',
			function(data){
			console.log(data);
				$('#reporte_nota-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 pageLength: 100,
					 select: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Docente", "data" : "docente"},
							{"title":"Celular", "data" : "cel"},
							{"title":"Nivel", "data" : "nivel"},
							{"title":"Grado", "data" : "grado"},
							{"title":"Sección", "data" : "secc"},
							{"title":"Área", "data" : "curso"},
							{"title":"Desempeño", "data" : "evaluacion"},
							//{"title":"Tipo", "data" : "tipo"},
							{"title":"Cantidad de Notas", "data" : "cantidad"}

				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);
}
	
	
