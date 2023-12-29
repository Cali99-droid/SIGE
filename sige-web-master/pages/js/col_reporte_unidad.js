function onloadPage(params){
	
	_onloadPage();	
	
	$('#frm-reporte-unidad').on('submit', function (event){
		event.preventDefault();
		
		return _get('api/cursoUnidad/consultar',
				function (data){
				console.log(data);
				$('#col_reporte_unidad').dataTable({
					 data : data,
		 			 destroy: true,
					 pageLength: 1000,
					 select: true,
			         columns : [ 
			     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
			     	           //{"title": "Nivel", "data" : "id_niv"},
			     	           //{"title": "Grado", "data" : "id_gra"},
			     	           {"title": "Curso", "data" : "curso"},
			     	           {"title": "Unidad", "data" : "nom"},
			     	           //{"title": "Producto", "data" : "producto"},
			     	           {"title": "Periodo","render":function ( data, type, row,meta ) { return row.per_aca_nom + " " + row.cpu_nump }},
			     	           {"title": "Numero", "data" : "num"},
			     	           {"title": "Generar", "data" : "id","render":function ( data, type, row,meta ) { 
			     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="generar_pdf(this, event)" class="list-icons-item"><i class="icon-file-pdf ui-blue ui-size" aria-hidden="true"></i></a> </div>';
			     	           }}
			        ] 

			    });}
				, $(this).serialize() + "&id_tra=" + _usuario.id_tra + "&id_anio=" + $('#_id_anio').text());

		
	});
	
	
	$('#id_gra').on('change',function(event){
		var param1={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(), id_gra:$("#id_gra").val()};
		if($(this).val()!='')
			_llenarComboURL('api/evaluacion/listarCursos',$('#id_cur'), null, param1,function(){$('#id_cur').change();});
		else{
			$('#id_cur').find('option').not(':first').remove();
		}
	});
	
	$('#id_niv').on('change',function(event){
		var param2={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val()};
		if($(this).val()!='')
			_llenarComboURL('api/evaluacion/listarGrados',$('#id_gra'),null, param2,function(){$('#id_gra').change()});
		else{
			$('#id_gra').find('option').not(':first').remove();
			$('#id_gra').change();
		}
	});
	
	var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};
	_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),null,param,function(){$('#id_niv').change();});
}
 
/* 
 * Genera pdf Unidad de aprendizaje
 * */
function generar_pdf(obj, e){
	e.preventDefault();
	var id = $(obj).data('id');
    
    var param = {id_uni:id};
	_GET({  url:'api/cursoUnidad/validarSesionesCompletasxUnidad',
		    params:param,
			success:function(data){
				var param1={id_uni:id, id_tra:_usuario.id_tra, id_anio: $('#_id_anio').text()};
				_GET({  url:'api/cursoUnidad/validarSesionesVinculadasxUnidad',
				    params:param1,
					success:function(data){
						console.log(data);
						var url = _URL + 'api/cursoUnidad/unidadAprendizajePDF/' + $('#_id_anio').text() + '/' + _usuario.id_tra + '/' + id;
					    window.open( url, '_blank');
						
					},
					error:function(data){
						_alert_error(data.msg);
					}
			     });
			},
			error:function(data){
				_alert_error(data.msg);
			}
	     });
    
    

}