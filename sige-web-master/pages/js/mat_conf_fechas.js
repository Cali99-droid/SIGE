//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	

}
// chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio) {
	conf_fechas_listar_tabla(id_anio);
}

// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();

	$('#btn-grabar').on('click', function(event) {
		event.preventDefault();
		$('#id_anio').val($('#_id_anio').text());
		var frm = $('#mat_conf_fechas-frm').serialize();

		_post('api/confFechas/grabar', frm, function(data) {
			console.log(data.result);
			pintarForm(data.result);
			
		});
	});

	// lista tabla
	conf_fechas_listar_tabla($('#_id_anio').text());
});

function conf_fechas_listar_tabla(id_anio) {
	_get('api/confFechas/' + id_anio, function(data) {
		console.log(data);

		pintarForm(data);

		

	});

}


function pintarForm(data){
	$('#ac_al').val(data.ac_al);
	$('#ac_del').val(data.ac_del);
	$('#ac_al_cs').val(data.ac_al_cs);
	$('#ac_del_cs').val(data.ac_del_cs);
	$('#ac_id').val(data.ac_id);

	$('#as_al').val(data.as_al);
	$('#as_del').val(data.as_del);
	$('#as_al_cs').val(data.as_al_cs);
	$('#as_del_cs').val(data.as_del_cs);
	$('#as_id').val(data.as_id);

	$('#nc_al').val(data.nc_al);
	$('#nc_del').val(data.nc_del);
	$('#nc_al_cs').val(data.nc_al_cs);
	$('#nc_del_cs').val(data.nc_del_cs);
	$('#nc_id').val(data.nc_id);

	$('#ns_al').val(data.ns_al);
	$('#ns_del').val(data.ns_del);
	$('#ns_al_cs').val(data.ns_al_cs);
	$('#ns_del_cs').val(data.ns_del_cs);
	$('#ns_id').val(data.ns_id);
	
	$('.daterange-time').daterangepicker({
		timePicker : true,
		singleDatePicker : true,
		applyClass : 'bg-slate-600',
		cancelClass : 'btn-light',
		locale : {
			format : 'DD/MM/YYYY h:mm a'
		}
	});
}
