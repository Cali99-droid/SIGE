//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	var id_tra = _usuario.trabajador.id;
	var id_usr = _usuario.id;

	_fillForm(_usuario.trabajador,$('#ges_trabajador-frm') );
	_llenarCombo('cat_tipo_documento',$('#id_tdc'),_usuario.trabajador.id_tdc);
	_llenarCombo('cat_est_civil',$('#id_eci'),_usuario.trabajador.id_eci);	
	_llenarCombo('cat_grado_instruccion',$('#id_gin'),_usuario.trabajador.id_gin);	

	var fec_nac = _usuario.trabajador.fec_nac;
	if(fec_nac){
		var arrFec_nac = fec_nac.split('-');
		$('#fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
	}
	
	$('#login').val(_usuario.login);
	$('#id_usr').val(id_usr);
	
	$('#ges_trabajador-frm').on('submit',function(e){
		e.preventDefault();
		
		//if($('#password').val() == $('#password2').val())
			_post($('#ges_trabajador-frm').attr('action') , $('#ges_trabajador-frm').serialize(),
				function(data){
						console.log(data);
						document.location.href = 'dashboard.html';
					}
				);
		//else
		//	alert('Las claves deben ser iguales');

	});
	
});

