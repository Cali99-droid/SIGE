//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	$('#password').val('');
	$('#password2').val('');
	$('#password').focus();
	var token =document.location.href.split("=")[1];
	
	$('#token').val(token);
	console.log(token);
	
	/*$('#frm-login').on('click',function(event){
		event.preventDefault();
alert();
		return _post_silent('api/seguridad/loginToken', $(this).serialize(),function(data){
				document.location.href = 'dashboard.html';
			
		});
		
	});*/
	$('#btn-aceptar').on('click',function(event){
		event.preventDefault();
		var pas1=$('#frm-login #password').val();
		var pas2=$('#frm-login #password2').val();
		var gen_con=_validar_clave(pas1);
			if(!gen_con){
				$("#info").css('display','block');
				$("#info").text('La contraseña debe tener al menos 8 caracteres y además contener letras mayúsculas, minúsculas y números.');
				$("#info").css({'color':'red'});
			} else{
				if(pas1!=pas2){
					swal({
					  title: "IMPORTANTE",
					  html:true,
					  type : "warning",
					  text: "<font style='color:black ; font-size:14px'>Las contraseñas no coinciden</font>",
					  icon: "info",
					  button: "Cerrar",
					});

				} else {
					console.log(_URL);
					_URL = window.location.origin.replace(':8080','')+ ":8081/sige-mat/";
					_POST({
					context:_URL,
					url:'api/seguridad/loginToken',
					params:$('#frm-login').serialize(),
					//contentType:"application/json",
					success: function(data){
						console.log(data);
						//$(".modal .close").click();
						//_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
						//_send('pages/matricula/mat_hijos_carnet.html','Mis hijos','Datos principales');
						//document.location.href = 'dashboard.html';
						swal(
							{   html: true,
								title : "EXITO",
								text : "<font style='color:black ; font-size:14px'>La nueva contraseña fue actualizada, por favor ingrese con sus nuevas credenciales.</font>",
								type : "success",
								showCancelButton :false,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "Aceptar",
								//cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : false
							},
							function(isConfirm) {
								 window.location = "http://login.ae.edu.pe:8080/sige/"

							})
					}
					});	
				}	

			}
		
		
	//return _post_silent($('#frm-login').attr('action'), $('#frm-login').serialize(),function(data){
		
	//});
		/*return _post_silent('api/seguridad/loginToken', $(this).serialize(),function(data){
				document.location.href = 'dashboard.html';
			
		});*/
		
	});
	
});

$('#btn-aceptar').on('click',function(event){
		event.preventDefault();
	return _post_silent($('#frm-login').attr('action'), $('#frm-login').serialize(),function(data){
	});
		/*return _post_silent('api/seguridad/loginToken', $(this).serialize(),function(data){
				document.location.href = 'dashboard.html';
			
		});*/
		
	});