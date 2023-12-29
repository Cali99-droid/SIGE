//Se ejecuta cuando la pagina q lo llama le envia parametros
var _ini = 0;
var _correo_valido= false;

function onloadPage(params) {

	console.log(params);
	_ini = (typeof params == 'undefined') ? 0 : params.ini;
   
	inicializar_wizard();
	
	_get('api/familiar/' + _usuario.id,
			function(data) {
				var familiar = data.familiar;
				var mensajeria = data.mensajeria;
				
				_ini = familiar.ini;
				
				if (_ini ==_ESTADO_ACTUALIZADO || mensajeria!=null) {
					
					$('.steps-validation').steps("next");
					// $('#btn-grabar').html('Confirmar datos para continuar');
					
					if(mensajeria!=null){
						modal_ingrese_clave_celular(mensajeria, familiar.cel);
					}else{
						swal({
							html : true,
							title : "<font color='black'>IMPORTANTE</font>",
							text : "<font color='black'>Validar su celular y/o correo para continuar.</font>",
							confirmButtonColor : "#66BB6A",
							type : "success"
						});
					}
					
				}

				if (_ini == _ESTADO_CELULAR ) {
					$('.steps-validation').steps("next");
					$('.steps-validation').steps("next");
				}

				/**
				 * llenar datos del familiar
				 */
				familiar_llenar_datos(familiar);
				
				familiar_llenar_comunicacion(familiar);

			});

	// var form = $(".steps-validation").show();

	/**
	 * formulario de validacion de celular y correo
	 */

	$('a[href$="next"]').text('Siguiente >>');
	$('a[href$="previous"]').text('<< Anterior ');
	$('a[href$="finish"]').text('Grabar y finalizar ');
	
	
	$('#alu_celular-frm').on('submit',function(event){
	//	alert('x');
	
		event.preventDefault();
		
		var form = $("#alu_celular-frm").show();
		form.validate().settings.ignore = ":disabled,:hidden";
		var isValid = form.valid();
		if (isValid) {
			validar_celular();
		}
	});

	
	$('#alu_correo-frm').on('submit',function(event){
			//alert('x');
		
			event.preventDefault();
			
			var form = $("#alu_correo-frm").show();
			form.validate().settings.ignore = ":disabled,:hidden";
			var isValid = form.valid();
			if (isValid) {
				validarCorreo();
			}
		});
		
	$('#frm-change').on('submit', function(event){
		event.preventDefault();
		$('a[href$="finish"]').click();
	});
}

function inicializar_wizard(){
	
	// Initialize wizard
	$(".steps-validation").steps({
			headerTag : "h6",
			bodyTag : "fieldset",
			transitionEffect : "fade",
			titleTemplate : '<span class="number">#index#</span> #title#',
			autoFocus : true,
			onStepChanging : function(event, currentIndex, newIndex) {
				var form = $("#alu_familiar-frm").show();
			//alert(newIndex + "z" + _ini);
				
				//alert(newIndex);	
				
				if (currentIndex > newIndex) {
					return true;
				}
				
				//alert(newIndex +"," +_ini);

				if (currentIndex < newIndex) {

					if (currentIndex == 0) {
						// alert($("#alu_familiar-frm").data('changed'));
						if (_ini == _ESTADO_ACTUALIZADO ||  (_ini==_ESTADO_CELULAR ) || !$("#alu_familiar-frm").data('changed')) {
							//alert('cambio!!');
							// YA TIENE SUS DATOS VALIDADOS EN BD
							return true;
						}

						form = $("#alu_familiar-frm").show();
						form.validate().settings.ignore = ":disabled,:hidden";
						var isValid = form.valid();

						if (isValid) {
							form.find(".body:eq(" + newIndex+ ") label.error").remove();
							form.find(".body:eq(" + newIndex + ") .error").removeClass("error");

							// actualizar datos
							familiar_actualizar_datos();
						}
						
						return false;
					}

					if (newIndex == 2){
						if ( _ini== _ESTADO_CELULAR || _ini== _ESTADO_CAMBIO_CLAVE )  {
							return true;
						}else{
							_alert_error('Validar el celular es obligatorio.');
							return false;
						}
						
					}else{
						
					}

					
				}
 
				
				// Needed in some cases if the user went back (clean
				// up)
/*							
							if (currentIndex < newIndex) {
								alert('mnarcabndi no eror');
								// To remove error styles
								form.find(".body:eq(" + newIndex+ ") label.error").remove();
								form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
							}
*/
				form.validate().settings.ignore = ":disabled,:hidden";
				return form.valid();
			},

			onStepChanged : function(event, currentIndex, priorIndex) {

				$('#idPasos').html('Paso ' + (currentIndex + 1) + ' de 2');

				// Used to skip the "Warning" step if the user is
				// old enough.
				if (currentIndex === 2
						&& Number($("#age-2").val()) >= 18) {
					form.steps("next");
				}

				// Used to skip the "Warning" step if the user is
				// old enough and wants to the previous step.
				if (currentIndex === 2 && priorIndex === 3) {
					form.steps("previous");
				}
				

			},

			onFinishing : function(event, currentIndex) {
 /*
					$('#frm-change #id').val(_usuario.id);
					$('#frm-change #id_per').val(_usuario.id_per);
					
					var isValid = $('#frm-change').valid();
					if (isValid) {
						_post('api/seguridad/change',$('#frm-change').serialize(), function(data){
							
							document.location.href = 'dashboard.html';
						});
						
					}
	*/
				if( _ini== _ESTADO_CELULAR)
					document.location.href="dashboard.html=t=" + new Date();
				else
					_alert_error('El número celular es obligatorio.');
					return false;
				
			},

			onFinished : function(event, currentIndex) {
				//alert("Submitted!");
			}
		});

	
}



function familiar_actualizar_datos() {
	_POST({
		form : $('#alu_familiar-frm'),
		silent:true,
		success : function(data) {
			//if (_ini!=null)
				_ini =_ESTADO_ACTUALIZADO; // YA TIENE SUS DATOS ACTUALIZADOS
			$(".steps-validation").steps("next");
		}
	});

	// _post($('#alu_familiar-frm').attr('action') ,
	// $('#alu_familiar-frm').serialize());

}

function validar_celular() {
	
	_POST({
		form : $('#alu_celular-frm'),
		params : "id=" + _usuario.id,
		silent :true,
		success : function(data) {
			_ini = _ESTADO_CELULAR; // YA TIENE CELULAR VALIDADO
			//$(".steps-validation").steps("next");
			
			modal_ingrese_clave_celular(data.result, $('#cel').val());
		}
	});

}

function solicitar_clave_celular_otra_vez()
{
	$('#alu_celular-frm #cel').val($('#celular').text());
	 validar_celular();
}

	

function familiar_llenar_datos(data) {
	
	_fillForm(data, $('#alu_familiar-frm'));
	

	var fec_nac = data.fec_nac;

	if (fec_nac) {
		var arrFec_nac = fec_nac.split('-');
		$('#fec_nac').val(
				arrFec_nac[2] + '/' + arrFec_nac[1] + '/' + arrFec_nac[0]);
	}
	
	_inputs('alu_familiar-frm');

	$('#id_dep').on(
			'change',
			function() {
				_llenarCombo('cat_provincia', $('#id_pro'),
						data.distrito.id_pro, null, function() {
							$('#id_pro').change();
						});
			});
	$('#id_pro').on('change', function() {
		_llenarCombo('cat_distrito', $('#id_dist'), data.id_dist);
	});

	_llenarCombo('cat_tipo_documento', $('#id_tdc'), data.id_tdc);
	_llenarCombo('cat_parentesco', $('#id_par'), data.id_par);
	_llenarCombo('cat_est_civil', $('#id_eci'), data.id_eci);
	_llenarCombo('cat_grado_instruccion', $('#id_gin'), data.id_gin);
	_llenarCombo('cat_religion', $('#id_rel'), data.id_reli);
	_llenarCombo('cat_departamento', $('#id_dep'),
			data.distrito.provincia.id_dep, null, function() {
				$('#id_dep').change();
			});

	_llenarCombo('cat_ocupacion', $('#id_ocu'), data.id_ocu);
	$("#alu_familiar-frm").data('changed', false);

	$("#alu_familiar-frm :input").change(function() {
		$("#alu_familiar-frm").data('changed', true);
	});
}

function familiar_llenar_comunicacion(data){
	
	var ini = data.ini;
	var corr_val = data.corr_val;
	//alert(corr_val);
	if(ini==_ESTADO_CELULAR){
		//bloqueando celular
		$('#cel').attr('disabled','disabled');
		$('#btn-validar-celular').attr('disabled','disabled');
		$("#cel").val(data.cel);
		$('#btn-validar-celular').html('Celular validado');
	//	$('a[href$="next"]').removeAttr('disabled');
	}

	if(corr_val==_CORREO_VALIDADO ){
		//bloqueando correo
		$('#corr').attr('disabled','disabled');
		$('#btn-validar-correo').attr('disabled','disabled');
		$("#corr").val(data.corr);
		
		$('#btn-validar-correo').html('Correo validado');
	}

}


function validarCorreo(){
	var corr = $('#corr').val();
	if(_validateEmail){
		_POST({url:'api/seguridad/validarCorreo',
			   params:{id:_usuario.id, id_per:_usuario.id_per, corr: corr},
			   silent:true,
			   success:function(data){
				   _alert('Revise su correo, de click en el link enviado para finalizar');
				   _correo_valido= false;	
				   setInterval(validarTokenCorreo, 2000);
			   }
			  });
	}else{
		_alert_error('Por favor ingrese un correo válido.');
	}
}

function validarTokenCorreo(){
	if(!_correo_valido){
		_GET({url:'api/seguridad/esCorreoValidado',
			  params:{id_fam:_usuario.id},
			  silent:true,
			  success:function(data){
				  console.log(data);
				  if(data){
					  _alert('Correo validado exitosamente!');
					  _correo_valido= true;
						$('#corr').attr('disabled','disabled');
						$('#btn-validar-correo').attr('disabled','disabled');
						$('#btn-validar-correo').html('Correo validado!!');
					  
				  }
			  }
		});
		
	}
}

function modal_ingrese_clave_celular(id_msj,cel){
	
	_modal('Validaci&oacute;n del celular en proceso', 'pages/alumno/alu_familiar_clave_modal.html',
			function(){
			//console.log(data);
			//alert($('#cel').val());
			$('#alu_familiar-clave-val-frm #id_msj').val(id_msj);
			$('#alu_familiar-clave-val-frm #celular').html(cel);
			//$('#btn-aceptar-clave').on()}^`
			
		},
		function(data){
			_alert("Su número celular fue validado, gracias.");
			
			document.cookie = "_token=" + data.token;
			document.location.href = 'dashboard.html';
			
			//bloqueando celular
			$('#cel').attr('disabled','disabled');
			$('#btn-validar-celular').attr('disabled','disabled');
			$('#btn-validar-celular').html('Celular validado');
		});
	
}