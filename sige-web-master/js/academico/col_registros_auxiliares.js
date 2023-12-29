//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
function onloadPage(params) {
		
	$('#id_gir').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR_SEDE)>-1){
			var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_rol:_usuario.roles[0]};
			_llenarComboURL('api/periodo/listarCiclosxGiroNegocioTrabajador',$('#id_cic'),null,param,function(){$('#id_cic').change();});
		} else {
			var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
		}	
		
	});
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'), null,null,function(){ $('#id_gir').change()});

	$('#buscar-frm').on('submit', function(e) {
		e.preventDefault();
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 || _usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			var url = _URL + 'api/archivo/registro_auxiliares?id_anio=' + $('#_id_anio').text()+'&id_gir='+$("#id_gir").val()+'&id_cit='+$('#id_cic').val();
		} else {
			var url = _URL + 'api/archivo/registro_auxiliares?id_anio=' + $('#_id_anio').text()+'&id_gir='+$("#id_gir").val()+'&id_cit='+$('#id_cic').val();
		}	
	
 	document.location.href = url;	

	});

}


// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();



	$('#not_nota-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		nota_modal($(this));
	});

	// lista tabla
	//nota_listar_tabla();
});

