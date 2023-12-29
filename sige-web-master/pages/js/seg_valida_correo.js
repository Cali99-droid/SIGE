//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){

	var token =document.location.href.split("=")[1];

	$('#btn-cerrar').on('click',function(){
		window.close();
	});
	
	_POST({	url:'api/seguridad/validarCorreoToken',
			params :{token:token},
			silent: true,
			success: function(data){
				$('#msg_valida_correo').html("FELICITACIONES!! Cuenta de correo {" + data.result + "} validado correctamente.");
			},
			error: function(data){
				$('#msg_valida_correo').html(data.msg);
			}
		  });
	
});
