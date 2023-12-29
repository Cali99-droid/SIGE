var _roles;
function onloadPage(params){
	_onloadPage();
			
}

$('#form_asistencia_rm').on('submit',function(event){
	event.preventDefault();
	//alert($("#_id_suc").html());//lina
	var trabajador = _usuario.nombres;
	var id_rol="";
	if (_usuario.roles.indexOf(_ROL_AUXILIAR)>-1)
		id_rol=5;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR_SEDE)>-1)
		id_rol=20;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
		id_rol=1;
var url = _URL + 'api/archivo/registro_asistencia_mensual?id_anio=' + $('#_id_anio').text()+'&id_tra='+_usuario.id_tra+'&usuario='+trabajador+'&id_mes='+$("#id_mes").val()+'&id_rol='+id_rol;
	document.location.href = url;	
	/*var param ={id_niv:$("#id_niv").val(), fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val(), id_suc:$("#id_suc").val(), id_anio:$('#_id_anio').text()};
	
	return _get('api/reporteAsistencia/reporteEstadisticaAsistencia',
	function(data){
		fncExito(data);//se le tiene q pasar el resultado del controller
	},param); //aqui*/
});




