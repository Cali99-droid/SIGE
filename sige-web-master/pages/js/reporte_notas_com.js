//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
function onloadPage(params) {
	$('#id_gra').on('change',function(event){
		var id_suc=$("#id_suc").val();
		_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
	});
	$('#id_niv').on('change',function(event){
		var param = {id_niv: $('#id_niv').val() , id_anio: $('#_id_anio').text() };
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'),null,param);
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(), function(){$('#id_gra').change()});
	});
	_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){ $('#id_niv').change()});
	_llenarCombo('ges_sucursal',$('#id_suc'));//lina
	
}

$('#reporte_nota_com-frm').on('submit', function(e) {
	e.preventDefault();
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param= {id_cpu:$('#id_cpu').val(), id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val(), id_suc:  $('#id_suc').val(), id_tra:null};
	}else{
		var param= {id_cpu:$('#id_cpu').val(), id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val(), id_suc:  $('#id_suc').val(), id_tra:_usuario.id_tra};	
	}
	reporte(param);
});


function reporte(param){
	_get('api/comportamiento/reporteComportamiento/',
			function(data){
			console.log(data);
				$('#reporte_nota_com-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 pageLength: 100,
					 select: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tutor", "data" : "tutor"},
							{"title":"Celular", "data" : "cel"},
							{"title":"Nivel", "data" : "nivel"},
							{"title":"Grado", "data" : "grado"},
							{"title":"Sección", "data" : "secc"},
							{"title":"Cantidad de Notas", "data" : "cantidad"}

				    ]
			    });
			}, param
	);
}
	
	
