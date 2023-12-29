 function onloadPage(params){

}
 
$(function(){
	
 });
 

function validateEmail(id)
{
  var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
	if(!email_regex.test($("#"+id).val()))
	{
	return false;
	}
	else{
	    return true;
	}
}

function validar_datos(){
	var form = $("#frm-ficha_validacion_datos").show();
	var isValid = form.valid();
	
	if (isValid) {
		if($("#cel").val().length<9){
			alert('El número de celular debe de tener mínimo 9 digitos');
		} else if(!validateEmail("corr")){
			alert('El correo no es válido');
		} else{
			var param={cel: $("#cel").val(), corr:$("#corr").val(), fec_emi_dni: $("#fec_emi_dni").val(), ubigeo:$("#ubigeo").val(), id_fam:_usuario.id}
			/*_POST({
				context:_URL,
				url:'api/familiar/actualizarDatosVerificados',
				params:param,
				//contentType:"application/json",
				success: function(data){
					console.log(data);
					//$(".modal .close").click();
					ficha_modal();
				}
			});	*/
			_post_silent('api/familiar/actualizarDatosVerificados', param,function(data){
				ficha_modal();
			});
		}

	}
	
	 /*
	if($("[name='id_alu']:checked").length==0){
		alert('Sleccione al menos un alumno!!');
	} else if($("[name='tc_acept']:checked").length==0){
		alert('Seleccione su respuesta de los términos y condiciones')
	} else{
		//_post($('#frm-ficha_matricula').attr('action') , $('#frm-ficha_matricula').serialize());
		_POST({
			context:_URL,
			url:'api/inscripcionCampus',
			params:$('#frm-ficha_matricula').serialize(),
			//contentType:"application/json",
			success: function(data){
				console.log(data);
				$(".modal .close").click();
				_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
			}
		});	
	}*/
 }
var onShowModal = function(){
	_onloadPage();
}
//funcion q se ejecuta despues de grabar
var onSuccessSave = function(data){

}
function ficha_validacion_datos(link){
	//alert('1-modal');
	//funcion q se ejecuta al cargar el modal
	

	
	
	//Abrir el modal
	var titulo= 'INSTRUCCIONES PARA EL LLENADO';
	//alert('2-_modal_full');
	_modal_full(titulo,'pages/campus_virtual/cv_ficha_validacion_datos_modal.html',onShowModal,onSuccessSave);
	
	var param = {};
	param.id_fam = _usuario.id;
	//param.id_anio= $('#_id_anio').text();
	
	_get('api/familiar/obtenerDatosFamiliar/',
			function(data){
			console.log(data.nro_doc);
			$("#nro_dni").val(data.nro_doc);
			//$("#nro_dni").css({'color':'black'});
			$("#ape_nom").val(data.ape_pat+' '+data.ape_mat+' '+data.nom);
			$("#cel").val(data.cel);
			$("#corr").val(data.corr);
			if(data.fec_emi_dni!=null)
				$("#fec_emi_dni").val(_parseDate(data.fec_emi_dni));
			$("#nro_ubigeo").val(data.ubigeo);
			//$("#ape_nom").css({'color':'black'});
			
			//$('#frm-ficha_matricula #id_gpf').val(data[0].id_gpf);
			}, param
	);

}
