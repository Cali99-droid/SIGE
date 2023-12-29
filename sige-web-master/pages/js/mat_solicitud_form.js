//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
var alumno;
function onloadPage(param) {
	console.log(param);
	_param= param;	
	_param.id_anio = $('#_id_anio').text();
	_llenarComboApi('api/solicitud/otrosLocales?id_suc=' + $('#_id_suc').text(), 'nom', $('#id_suc_ori'), _param.id_suc_ori);

	_get('api/solicitud/datos',function(data){
		
		alumno = data.alumno;
		$('#nom_alu').html(alumno.alumno);
		$('#id_niv').val(alumno.nivel);
		$('#id_gra').val(alumno.grado);
		$('#id_au').val(alumno.seccion);
			
		var param = {
				id_alu : alumno.id_alu
		};
		
		_llenarComboURL('api/alumno/listarFamiliares', $('#id_fam'),
				alumno.id_fam, param, function() {
					if (alumno.id_fam ==null){
						//alert();
						$('#id_fam').removeAttr('disabled');
					}
				});
		
		$('#estado').val(_param.tipo);
		
	}, _param);

	
	$('#frm-solicitud').on('submit', function(event){
		event.preventDefault();
		
		$("#id_anio").val($("#_id_anio").text());
		$("#id_suc_des").val($("#_id_suc").text());
		$("#id_alu").val(alumno.id_alu);
		$("#tipo").val(_param.tipo);
		$("#id_suc_or").val(_param.id_suc_ori);
		$("#id_fam").removeAttr('disabled');

		_post('api/solicitud/grabar', $(this).serialize(), function(data){
			console.log(data);
			_send('pages/matricula/mat_solicitud_buscar.html','Solicitud','Cambio de local');
		});
	});
	
	$('#btn-cancelar').on('click', function(event){
		_send('pages/matricula/mat_solicitud_buscar.html','Solicitud','Cambio de local');
	});
}

 
