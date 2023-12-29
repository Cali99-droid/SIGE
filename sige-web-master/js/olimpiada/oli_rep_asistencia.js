//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
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
			url : 'api/config/listarOliUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}

	_llenar_combo({
		tabla : 'cat_tipo_inscripcion',
		combo : $('#id_ti'),
		context : _URL_OLI
	});

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
		_GET({url:'api/reporte/asistencia',
			  params: $(this).serialize(),
			  context : _URL_OLI,
			  success:function(data){
				  
				  var columns = [ 
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title":"DNI", "data" : "nro_dni"}, 
						{"title":"Alumno", "data": "alumno"}, 
						{"title":"Nivel", "data": "nivel"},
						{"title":"Grado", "data": "grado"},
						{"title":"Colegio", "data": "colegio"},
						{"title":"Modalidad", "data": "modalidad"},
						{"title":"Hora Ingreso", "data": "hor_ing"},
						{"title":"Hora Final.", "data": "hor_sal"}
			    ];
				  
				  $('#panel-inscritos').show();
				  $('#tabla-reporte').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 scrollX: true,              
				         columns :columns
				    });
			  }
			});
	});
}
//REENVIAR CORREO
function reenviar(id){
	_POST({url:'/api/public/inscripcionInd/reenviar/' + id, context:_URL_OLI});
}
function generarExcel(e){
	_download(_URL_OLI + 'api/resultados/generarExcelLectora?id_oli=' + $('#id_oli').val()  , "lectora.xls");	
}

function descargarCarnet(id,dni){
	_download(_URL_OLI+'api/public/inscripcionInd/imprimirCarnet?tipo=I&id_ins='+id,dni + '_olimpiada.pdf');	
}

function eliminarInscr(id){
	_DELETE({
				url:'api/reporte/' + id,
				context : _URL_OLI,
				success : function (data){
					$('#frm-reporte').submit();
				}
			});
}