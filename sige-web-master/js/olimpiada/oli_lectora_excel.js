//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	_onloadPage();	

	_llenar_combo({
		tabla : 'oli_config',
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
	});

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

	
	$('#frm-reporte').on('submit', function(event){
		event.preventDefault();
		
		var validator = $(this).validate();
		if ($(this).valid()){
			_download(_URL_OLI + 'api/resultados/generarExcelLectora?id_oli=' + $('#id_oli').val()+ "&id_nvl=" + $('#id_niv').val() + "&id_gra=" + $('#id_gra').val()  , "lectora.xls");
		}
		
	});
}
