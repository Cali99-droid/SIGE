//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		//tabla : 'oli_config',
		url : 'api/config/listarOlimpiadasxAnio',
		params: param,
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
		
}

$("#btn_descargar").on('click', function(event){
	_download(_URL_OLI  + 'api/resultados/formato/'+$('#id_oli').val(),'FORMATO_CARGA_LECTORA.xls');
});

