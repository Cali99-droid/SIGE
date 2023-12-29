//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	/*_llenar_combo({
		tabla : 'oli_config',
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
	});*/
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SOPORTE_OLI)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		url : 'api/config/listarOlimpiadasxAnio',
		params: param,
		//tabla : 'oli_config',
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
		});
	} else{
		var param={id_anio:$("#_id_anio").text(),id_usr:_usuario.id};

		_llenar_combo({
			url : 'api/config/listarOliVigxUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}
/*
	_llenar_combo({
		tabla : 'cat_tipo_inscripcion',
		combo : $('#id_ti'),
		context : _URL_OLI
	});
*/
	_llenar_combo({
		tabla : 'cat_gestion',
		combo : $('#id_cg'),
		context : _URL_OLI
	});

	$('#id_oli').on('change', function() {
		if($(this).val()==''){
			$(this).find('#id_niv').not(':first').remove();
			$(this).find('#id_gra').not(':first').remove();
		}else
			_llenar_combo({
				url : 'api/config/listarNiveles/' + $(this).val(),
				combo : $('#id_niv'),
				context : _URL_OLI,
				funcion : function() {
					$('#id_niv').change();
				}
			});
	});

	$('#id_niv').on('change',function() {
			if($(this).val()=='' ||  $('#id_oli').val()=='')
				$(this).find('#id_gra').not(':first').remove();
			else
				_llenar_combo({
					url : 'api/config/listarGrados/' + $('#id_oli').val()+ '/' + $(this).val(),
					combo : $('#id_gra'),
					context : _URL_OLI
				});
			});


	var validator = $('#frm-reporte').validate();
	
	$('#frm-reporte').on('submit', function(event){
		event.preventDefault();
		//alert();
		if ($(this).valid())
		_GET({url:'api/resultados/resultados',
			  params: $(this).serialize(),
			  context : _URL_OLI,
			  success:function(data){
				  
				  var columns = [ 
					  	{"title": "Nro.", "data" : "puesto"},
						{"title":"Alumno", "data" : "alumno"}, 
						{"title":"Colegio", "data" : "colegio"}, 
						{"title":"Modalidad", "data" : "modalidad"}, 
						{"title":"Gestión", "data" : "gestion"}, 
						{"title":"Nivel", "data" : "nivel"},
						{"title":"Grado", "data" : "grado"},
						{"title":"H. Final", "data" : "hor_sal"}, 
						 {"title":"Puntaje", "data" : "puntaje","render": function ( data, type, row,meta ) {
					    	   if(data!=null)
					    		   return data;
					    	   else
					    		   return '-';
					       }},
					    {"title":"Puesto", "data" : "puesto"}												
			    ];
				   
				  
				  $('#panel-inscritos').show();
				  $('#tabla-reporte').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 scrollX: true,              
				         columns :columns,
					    "initComplete": function( settings ) {
							
				        	$("<span><a href='#' onclick='generarExcel(event)'> <i class='fa fa-file-excel-o'></i>Exportar</a>&nbsp;</span>").insertBefore($("#_paginator"));
				        	 //_dataTable_total(settings,'monto');
						 }
				    });
			  }
			});
	});
}
 
function generarExcel(e){
	_download(_URL_OLI + 'api/resultados/excel?id_oli=' + $('#id_oli').val()  + "&id_ti=" + $('#id_ti').val() + "&id_cg=" + $('#id_cg').val() + "&id_niv=" + $('#id_niv').val() + "&id_gra=" + $('#id_gra').val() + "&primeros=" + $('#primeros').val(), "resultados.xls");	
}
 