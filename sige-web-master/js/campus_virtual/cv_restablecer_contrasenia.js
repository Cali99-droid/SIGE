
function onloadPage(params){
	//id_gpf=params.id_gpf;
	console.log(params);
	//$("#usuario").text(params.usuario);
	
}

$(function(){
	_onloadPage();
	

});	
	
function generar_contrasenia(){
	//alert('1-modal');
	var nuevo_pass=$("#nuevo_pass").val();
	var conf_pass=$("#conf_pass").val();
	//Buscamos la contraseña anterior
	var param={usuario:$("#usr").val()};
	_get('api/inscripcionCampus/obtenerContraseniaAnterior', function(data){
		console.log(data);
		var anterior_contrasenia=data.psw;
		console.log('anterior_contrasenia'+anterior_contrasenia);
		console.log('nueva_contrasenia'+nuevo_pass);
		if(nuevo_pass==anterior_contrasenia){
			$("#info").css('display','block');
			$("#info").text('La nueva contraseña debe ser diferente a la anterior');
			$("#info").css({'color':'red'});
		} else if(nuevo_pass!=conf_pass){
			alert('Las contraseñas no coinciden, porfavor verifiquelos!!')
		} else{
			var gen_con=_validar_clave(nuevo_pass);
			if(!gen_con){
				$("#info").css('display','block');
				$("#info").text('La contraseña debe tener al menos 8 caracteres y además contener letras mayúsculas, minúsculas y números.');
				$("#info").css({'color':'red'});
			} else{
				var param={usuario:$("#usr").val(),nuevo_pass:nuevo_pass};
				_POST({
					context:_URL,
					url:'api/inscripcionCampus/cambiarContrasenia',
					params:param,
					//contentType:"application/json",
					success: function(data){
						console.log(data);
						$(".modal .close").click();
						//_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
						_send('pages/matricula/mat_hijos_carnet.html','Mis hijos','Datos principales');
					}
				});	
			}
		}
		
	},param);
	
	/*if(nuevo_pass!=conf_pass){
		alert('Las contraseñas no coinciden, porfavor verifiquelos!!')
	} else {
		var gen_con=_validar_clave(nuevo_pass);
		if(!gen_con){
			$("#info").css('display','block');
			$("#info").text('La contraseña debe ser segura, tener una letra en mayúscula, un caracter especial(#,$), un número');
			$("#info").css({'color':'red'});
		} else{
			var param={usuario:$("#usr").val(),nuevo_pass:nuevo_pass};
			_POST({
				context:_URL,
				url:'api/inscripcionCampus/generarPass',
				params:param,
				//contentType:"application/json",
				success: function(data){
					console.log(data);
					$(".modal .close").click();
					_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
				}
			});	
		}
	}*/
	
}

