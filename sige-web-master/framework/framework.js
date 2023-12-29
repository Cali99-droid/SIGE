var _MES = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"];
var _url = null;
var _titulo;
var _subTitulo;
var _callBackfuncion;
var _params;
var _login = null;


var _ERROR_401 ="401";//permiso denegado
var _ERROR_404 ="404";//no existe el servicio
var _ERROR_440 ="440";//sesion expirada

//validaciones de entrada

/** EMTODOS PARA EL CONTENIDO**/
function _response(result){
	for (var key in result) {
		
		for (var key2 in result[key]) {
			var bean = key + "." + key2;
						
			var  elem = document.querySelectorAll('[data-bean="' + bean +'"]');
			if (elem.length==1){
				$('[data-bean="' + bean +'"]').text(result[key][key2]);
			}
			for (var key3 in result[key][key2]) {
				var bean = key + "." + key2 + "." + key3;

				var  elem = document.querySelectorAll('[data-bean="' + bean +'"]');
				if (elem.length==1){
					$('[data-bean="' + bean +'"]').text(result[key][key2][key3]);
				}
				
			}
		}
	}
	
	//
}

/**METODOS DE NAVEGACION **/
function _refresh(){
	if(_url!=null){
		
		_send(_url, _titulo, _subTitulo, _params,_callBackfuncion);
	}
}

function _refreshTags(){

	$('[sige-display]').each(function( index ) {
		var test = $(this).attr("sige-display");
		var condicion = false;
		
		eval("var condicion=(" + test + ")");

		if(condicion)
			$(this).css("display","block");
		else
			$(this).css("display","none");
	});
}

function _cargando(){
	$(".content-body").html('Cargando.....');
	
}
function _send(url, titulo, subTitulo, params, callBackfuncion){
	
	//SI TIENE EL MENU SUPERIOR DE RESOLUCION MOVIL
	if ($('body').hasClass('sidebar-mobile-main'))
		$('.sidebar-mobile-main-toggle').click();//ocultar el menu	

	if (!_CACHE){
		if (url.indexOf("?")==-1)
			url = url + "?t=" + $.now();
		else
			url = url + "&t=" + $.now();
	}
	
	_cargando();
 
	$(".content-body").load(url, params, function(responseText, textStatus, req){
	
		if (textStatus=="error"){
			var status = req.status;
			var text = "Ocurri&oacute; un error al mostrar la pagina";
			if (status=="404")
				text = "<b>Lo sentimos :(</b> <br>La p&aacute;gina se encuentra en construcci&oacute;n.";
			//console.error(responseText);
			swal({
				html: true,
				title : "Error",
				text :text,
				confirmButtonColor : "#2196F3",
				type : "error"
			});
			
		}else{
			
			_url =  url;
			_titulo = titulo;
			_subTitulo = subTitulo;
			_callBackfuncion = callBackfuncion;
			_params = params;

			$(".liMenu").removeClass("active");
			if (typeof titulo !== 'undefined') 
				$("._titulo").html(titulo);
			if (typeof subTitulo !== 'undefined') 
				$("._subTitulo").html(subTitulo);

			
			if (_AMBIENTE == 'P' && $("#usuario\\.login").html()=="" )
				document.location.href = 'index.html';

				
			_refreshTags();
			onloadPage(params);
			if (typeof callBackfuncion != "undefined") 
				callBackfuncion();
			
		}
		

	});
}

/**
 * 
 * @param tabla obligatorio
 * @param combo obligatorioll
 * @param _id opcional
 * @param param opcional
 */
function _llenarCombo(tabla, combo, _id, param, funcion, contexto){
	var url = 'api/combo/' + tabla;
	//Inicio caso personalizado
	var anio=$('#_id_anio').text();
	if(tabla=='per_periodo'){
		url = url + '/'+ anio;
	//Fin caso personalizado
	}else if (typeof param  == 'undefined' || param==null){

		url = url + '/A';
	}else{
		url = url + '/' + param;
	}
	
	//remover todos excepto el q viene por defecto
	
	var valor = combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	
	//PARCHE.. MEJHORARLO EN JDK1.8
		_GET({url:url,
			  context:contexto, 
			success:function (data) {
			//console.log(data);
			  for ( var i in data) {
					var id = data[i].id;
					var value = data[i].value;
					if(data[i].aux1 === null && typeof data[i].aux1 === "object")
						combo.append('<option value="'+ id +'">' +value + '</option>');
					else
						combo.append('<option value="'+ id +'" data-aux1="' + data[i].aux1 +'" data-aux2="' + data[i].aux2 + '" data-aux3="' + data[i].aux3  + '" >' +value + '</option>');
				}

			  if (typeof _id != 'undefined'&& _id!=null ){
				  if(combo.hasClass("select-search")){
					  combo.val(_id);
					  combo.trigger('change.select2');//combo.val(_id).change();
				  }else
						combo.val(_id);
			 }
			  if (typeof funcion != 'undefined'&& funcion!=null ){//esto estaba :(, no se como se borró-
				  funcion();
			  }		  
			  
			}
		});

}

/*function _llenarCombo(tabla, combo, _id, param, funcion, contexto){
	var url = 'api/combo/' + tabla;

	//Inicio caso personalizado
	var anio=$('#_id_anio').text();
	if(tabla=='per_periodo'){
		url = url + '/'+ anio;
	//Fin caso personalizado
	}else if (typeof param  == 'undefined' || param==null){
		//console.log("param");
		url = url + '/A';
	}else{
		url = url + '/' + param;
	}
	
	//remover todos excepto el q viene por defecto
	
	var valor = combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	
	//PARCHE.. MEJHORARLO EN JDK1.8
		_GET({url:url,
			  context:contexto, 
			success:function (data) {
			//console.log(data);
			  for ( var i in data) {
					var id = data[i].id;
					var value = data[i].value;
					if(data[i].aux1 === null && typeof data[i].aux1 === "object")
						combo.append('<option value="'+ id +'">' +value + '</option>');
					else
						combo.append('<option value="'+ id +'" data-aux1="' + data[i].aux1 +'" data-aux2="' + data[i].aux2 + '" data-aux3="' + data[i].aux3  + '" >' +value + '</option>');
				}

			  if (typeof _id != 'undefined'&& _id!=null ){
				  if(combo.hasClass("select-search")){
					  combo.val(_id);
					  combo.trigger('change.select2');//combo.val(_id).change();
				  }else
						combo.val(_id);
			 }
			  if (typeof funcion != 'undefined'&& funcion!=null ){//esto estaba :(, no se como se borró-
				  funcion();
			  }		  
			  
			}
		});

}*/
function _llenarComboJSON(combo, data,_id ,funcion){
	var valor = combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	
	 for ( var i in data) {
			var id = data[i].id;
			var value = data[i].value;
			if(data[i].aux1 === null && typeof data[i].aux1 === "object")
				combo.append('<option value="'+ id +'">' +value + '</option>');
			else
				combo.append('<option value="'+ id +'" data-aux1="' + data[i].aux1 +'" data-aux2="' + data[i].aux2 + '" data-aux3="' + data[i].aux3  + '" >' +value + '</option>');
		}

	  if (typeof _id != 'undefined'&& _id!=null ){
		  if(combo.hasClass("select-search")){
			  combo.val(_id);
			  combo.trigger('change.select2');//combo.val(_id).change();
		  }else
				combo.val(_id);
	 }
	  
}

function _getComboJSON(combo, data,_id){


	var s = $('<select onchange="onchangeCombo(this)"/>');

	$('<option />', {value: '-1', text: 'seleccione'}).appendTo(s);
	 
	 for ( var i in data) {
		 var id = data[i].id;
		var value = data[i].value;
			
	    $('<option />', {value: id, text: value}).appendTo(s);
	}
	
	 return s.html();
	
	  
}

function _llenarComboURL(url, combo,_id,param, funcion){
	
	var valor = combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	
		_get(url,function (data) {
			//console.log(data);
			  for ( var i in data) {
					var id = data[i].id;
					var value = data[i].value;
					if(data[i].aux1 === null && typeof data[i].aux1 === "object")
						combo.append('<option value="'+ id +'">' +value + '</option>');
					else
						combo.append('<option value="'+ id +'" data-aux1="' + data[i].aux1 +'" data-aux2="' + data[i].aux2 + '" data-aux3="' + data[i].aux3  + '" data-aux4="' + data[i].aux4  + '"  data-aux5="' + data[i].aux5  + '" data-aux6="' + data[i].aux6  + '" data-aux7="' + data[i].aux7  + '">' +value + '</option>');
				}

			  if (typeof _id != 'undefined'&& _id!=null ){
				  if(combo.hasClass("select-search")){//antes ejecutaba change
					  combo.val(_id);
					  combo.trigger('change.select2');//combo.val(_id).change();
				  }else
						combo.val(_id);
			 }
			  if (typeof funcion != 'undefined'&& funcion!=null ){
				  funcion();
			  }		  
			  
			},param);

}

/*version definitiva*/
function _llenar_combo(params){
	
	var _url = params['url'];
	var _combo = params['combo'];
	var _id = params['id'];
	var _text = 'nom';
	if ('text' in params)
		_text = params['text'];
	var _funcion = params['funcion'];
	var _params = null;
	if("params" in params) {
		_params = params['params'];
	}
	
	//url, combo,_id,param, funcion
	var valor = _combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		_combo.find('option').not(':first').remove();
	}else{
		_combo.empty();
	}
	
	if("tabla" in params) {
		_url = 'api/comboCache/' + params['tabla'];
	}

	_attr = false;
	if("attr" in params) {
		_attr  = params['attr'];
	}
	

	 if (typeof _id != 'undefined'&& _id!=null ){
		  if(_combo.hasClass("select-search")){
			  _combo.val(_id);
			  _combo.trigger('change.select2');//combo.val(_id).change();
		  }else
				_combo.val(_id);
	 }
	
	//console.log(_params);
		_GET({  url:_url,
				context : params['context'],
				params : _params,
				success:function (data) {
					//console.log(data);
					  for ( var i in data) {
							var id = data[i].id;
							var text = data[i][_text];
							var option = $('<option/>');
							option.attr({ 'value': id }).text(text);

							 for(var k in data[i]){
								 if(_attr && k!='value')
									 option.attr(k,data[i][k]);
								 else
									 option.data(k,data[i][k]);
							 }
							 
							 _combo.append(option);
						}

					  if (typeof _id != 'undefined'&& _id!=null ){
						  if(_combo.hasClass("select-search")){//antes ejecutaba change
							  _combo.val(_id);
							  _combo.trigger('change.select2');//combo.val(_id).change();
						  }else
								_combo.val(_id);
					 }
					  if (typeof _funcion != 'undefined'&& _funcion!=null ){//esto estaba :(, no se como se borró-
						  _funcion();
					  }		  
					  
					}
			});

}

function getCookie(cname) {
	  var name = cname + "=";
	  var decodedCookie = decodeURIComponent(document.cookie);
	  var ca = decodedCookie.split(';');
	  for(var i = 0; i <ca.length; i++) {
	    var c = ca[i];
	    while (c.charAt(0) == ' ') {
	      c = c.substring(1);
	    }
	    if (c.indexOf(name) == 0) {
	      return c.substring(name.length, c.length);
	    }
	  }
	  return "";
	}


function _get(url, success, param){
	
	if (!_CACHE){
		if (url.indexOf("?")==-1)
			url = url + "?t=" + $.now();
		else
			url = url + "&t=" + $.now();
	}
	
	if(url.indexOf("http:")==-1)
		url = _URL + url;
	var _token = getCookie("_token");
	
	console.log(url);
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		 headers: {
		        'Authorization':_token
		 },
		url : url,
		data : param,
		dataType : 'json',
		cache: false,
		timeout : 100000,
		success : function(data) {
			console.log(data);
			if(data.code && data.code!='200'){
				swal({
					title : "Error",
					text :data.msg,
					confirmButtonColor : "#2196F3",
					type : "error"
				},
				function(){
					console.error(data);
					if (data.code==_ERROR_401)
						document.location.href = "index.html";
					
					}
				);
				
			}else{
				success(data.result);
			}
		},
		error : function(e) {
			if(e.status==_ERROR_404){
				swal({
					title : "Error",
					text :"El servicio no esta disponible, inténtelo en 5 minutos por favor, si el problema persiste comunicarse con el administrador del sistema",
					confirmButtonColor : "#2196F3",
					type : "error"
				});
			}
			if(e.status=='440'){
				
				if(_usuario==null)
					document.location.href = "index.html";
				else{
					var _onShowModal = function(){
						$('#login-frm #id_per').val(_usuario.id_per);
					};
					var _callBackExito = function(data){
						//console.log("MSG" + data.msg);
						document.cookie = "_token=" + data.token;

						$(".modal .close").click();
						/////_refresh();

					};
					$(".modal  .modal-dialog").addClass('modal-ssm');
					_modal('','login.html',_onShowModal,_callBackExito);
				}

			}
			
			
			console.log("ERROR: ", e);
			
		},
		done : function(e) {
			console.log("DONE");
		}
	});
	
}

function _post_silent(url,form, success,fncError){
	_post(url,form, success, 'silent', fncError);
}

/**
 * Eliminar un registro ( el controller debe tener el metodo DELETE )
 * @param url url/{id}
 * @param success funcion que se ejecuta al finalizar la transaccion correctamente
 */
function _delete(url, success,_textError,_text){
	var text =  "De eliminar el registro seleccionado?";
	if(typeof _text !='undefined')
		text = _text;
	
	
		swal(
				{
					title : "Esta seguro?",
					text :text,
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "rgb(33, 150, 243)",
					cancelButtonColor : "#EF5350",
					confirmButtonText : "Si, Eliminar",
					cancelButtonText : "No, cancela!!!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
						

						 $.blockUI({ 
						        message: '<i class="icon-spinner4 spinner"></i>',
						        //timeout: 2000, //unblock after 2 seconds
						        overlayCSS: {
						            backgroundColor: '#1b2024',
						            opacity: 0.8,
						            zIndex: 1200,
						            cursor: 'wait'
						        },
						        css: {
						            border: 0,
						            color: '#fff',
						            padding: 0,
						            zIndex: 1201,
						            backgroundColor: 'transparent'
						        }
						    });
				

						 	var _token = getCookie("_token");
						 
							$.ajax({
								type : "DELETE",
								url : _URL + url,
								timeout : 100000,
								headers: {
							        'Authorization':_token
								},
								success : function(data) {
									
									if(data.code){
										//EXISTE UN ERROR
										if(typeof _textError !='undefined')
										var	texto_error = _textError;
										else
										var	texto_error=data.msg
										 swal({
												title : "Error",
												text : texto_error,
												confirmButtonColor : "#2196F3",
												type : "error"
											});
									}else{

										//console.log("succes",data);
										if (typeof silent == 'undefined') {
											swal({
									            html:true,
									            title: "<font color='black'>Transaccion existosa!</font>",
									            text: "El registro se elimino exitosamente.",
									            confirmButtonColor: "#66BB6A",
									            type: "success"
									        });

										}
											
										
										success(data);

									}
									
									 $.unblockUI();
									//display(data);
								},
								error : function(e) {

									console.error( e);
									//console.log("caramba");
									 $.unblockUI();
									 
									 swal({
											title : "<font color='black'>Cancelado</font>",
											text : "<font color='black'>" + e.responseText + "</font>",
											confirmButtonColor : "#2196F3",
											type : "error"
										});
									 
								},
								done : function(e) {
									//console.log("DONE");
									//enableSearchButton(true);
									 $.unblockUI();
								}
							});						 
						
					} else {
						swal({
							title : "Cancelado",
							text : "No se ha eliminado ningun registro",
							confirmButtonColor : "#2196F3",
							type : "error"
						});
					}
				});




	    
		

		

}

function _delete_silent(url, success){


	var _token = getCookie("_token");
	 
	$.ajax({
		type : "DELETE",
		url : _URL + url,
		timeout : 100000,
		 headers: {
		        'Authorization':_token
		 },
		success : function(data) {
				success(data);
		},
		error : function(e) {

			console.error( e);
			 $.unblockUI();
			 
			 swal({
					title : "<font color='black'>Cancelado</font>",
					text : "<font color='black'>" + e.responseText + "</font>",
					confirmButtonColor : "#2196F3",
					type : "error"
				});
			 
		},
		done : function(e) {
			//console.log("DONE");
			//enableSearchButton(true);
			 $.unblockUI();
		}
	});						 

}

function _post(url,form, success, silent, fncError){
	
    $.blockUI({ 
        message: '<i class="icon-spinner4 spinner"></i>',
        //timeout: 2000, //unblock after 2 seconds
        overlayCSS: {
            backgroundColor: '#1b2024',
            opacity: 0.8,
            zIndex: 1200,
            cursor: 'wait'
        },
        css: {
            border: 0,
            color: '#fff',
            padding: 0,
            zIndex: 1201,
            backgroundColor: 'transparent'
        }
    });
    
	
	var _token = getCookie("_token");

	$.ajax({
		type : "POST",
		//contentType : "application/json",
		url : _URL + url,
		data : form,
		 headers: {
		        'Authorization':_token
		 },
		//dataType : 'json',
		timeout : 100000,
		success : function(data) {
			//console.log(data);
			if(data.code && data.code!='200'){
				//EXISTE UN ERROR
				if (typeof fncError != 'undefined'){
					fncError(data);
				}else{
					var mensajeError =data.msg;
					
					
					if(data.code=='399'){
						 swal({
							 	html: true,
						 		title : "<font color='black'>Advertencia</font>",
								text : "<font color='black'>" +  mensajeError + "</font>",
								confirmButtonColor : "#2196F3",
								type : "warning"
							});
					} else{
						if (data.code=='400')
							mensajeError = 'Se ha enviado un dato no válido, por favor verificar';
						 swal({
							 	html: true,
						 		title : "<font color='black'>Error</font>",
								text : "<font color='black'>" +  mensajeError + "</font>",
								confirmButtonColor : "#2196F3",
								type : "error"
							});
					}
					
					
					
				} 
				 
						
			}else{

				//console.log("succes",data);
				if (typeof silent == 'undefined') {
					swal({
			            html:true,
						title: "<font color='black'>Transaccion existosa!</font>",
			            text: "La operacion se efectuo exitosamente.",
			            confirmButtonColor: "#66BB6A",
			            type: "success"
			        });

				}
					
				if (typeof success != 'undefined'){ 
					success(data);
				}

			}
			
			 $.unblockUI();
			//display(data);
		},
		error : function(e) {
			console.error( e);
			 $.unblockUI();
			 
				swal({
					html:true,
				 	title : "<font color='black'>Cancelado</font>",
					text : "<font color='black'>Error desconocido</font>",//e.responseText,
					confirmButtonColor : "#2196F3",
					type : "error"
				});
			
			 
			 
		},
		done : function(e) {
			//console.log("DONE");
			//enableSearchButton(true);
			 $.unblockUI();
		}
	});
	
}


function _post_json(url,form, success){

    $.blockUI({ 
        message: '<i class="icon-spinner4 spinner"></i>',
        //timeout: 2000, //unblock after 2 seconds
        overlayCSS: {
            backgroundColor: '#1b2024',
            opacity: 0.8,
            zIndex: 1200,
            cursor: 'wait'
        },
        css: {
            border: 0,
            color: '#fff',
            padding: 0,
            zIndex: 1201,
            backgroundColor: 'transparent'
        }
    });
    
    var url1;
    if (url.indexOf('http:')==0)
    	url1 = url;
    else
    	url1 =  _URL + url;

    var _token = getCookie("_token");
    
	$.ajax({
		type : "POST",
		url : url1,
		data : JSON.stringify(form),
		async:false,
		contentType: "application/json",
		dataType: 'json',
		headers: {
	        'Authorization':_token
		},    
		timeout : 200000,
		success : function(data) {
			//console.log(data);
			if(data.code && data.code!='200'){
				//EXISTE UN ERROR
				
				var mensajeError =data.msg;
				
				if (data.code=='400')
					mensajeError = 'Se ha enviado un dato no válido, por favor verificar';
				 swal({
						title : "Error",
						text : mensajeError,
						confirmButtonColor : "#2196F3",
						type : "error"
					});
			}else{

				//console.log("succes",data);
				if (typeof silent == 'undefined') {
					swal({
			            title: "Transaccion existosa!",
			            text: "La operacion se efectuo exitosamente.",
			            confirmButtonColor: "#66BB6A",
			            type: "success"
			        });

				}
					
				
				success(data);

			}
			
			 $.unblockUI();
			//display(data);
		},
		error : function(e) {
			console.error( e);
			 $.unblockUI();
			 
			 swal({
					title : "Cancelado",
					text : "Error desconocido",//e.responseText,
					confirmButtonColor : "#2196F3",
					type : "error"
				});
			 
		},
		done : function(e) {
			//console.log("DONE");
			//enableSearchButton(true);
			 $.unblockUI();
		}
	});
	
}

/**
 * Llena la data del formulario  a partir del json
 * @param data
 * @param $form
 */
function _fillForm(data,$form ){
	for (var key in data) {
		if ($form.find('#' + key).hasClass('select-search')){
			//alert(data[key]);
			//$form.find('#' + key).val(data[key]).change();
			$form.find('#' + key).val(data[key]);
			$form.find('#' + key).trigger('change.select2');
		}else
			$form.find('#' + key).val(data[key]);
		
		}
	//console.log(data);
}


function _modal(titulo,url_html,callBackOnShowModal,callBackSave, params){
	
	  try{
		  $modal = $('#modal').modal('show');
		  $modal.draggable({handle: ".modal-header"});
		  $modal.find('.modal-content').resizable({
		      minHeight: 200,
		      minWidth: 300
		    });
		  
		  $modal.on('shown.bs.modal', function() {
			  _focus($(this));
		    });
		    
	    $modal.on('hidden.bs.modal', function () {
	    	$('#modal .modal-body ').html('');
	    	$(".modal .modal-dialog").removeClass('modal-ssm');
	    })

		  $modal.find('.modal-body').load(url_html, function(){
			  $modal.find('.modal-title').html(titulo);
			  $modal.find('form').on('submit',function(event){
		    		event.preventDefault();
					//HABILTAR COMBOS DEL FORMULARIO

		    		$(this).find('select').each(function() {
		    			var select = $(this);
		    			select.attr("disabled",false);//habilita
		    		});
	    			
		    		_post($(this).attr('action') , $(this).serialize(),
		    				function(data){
		    					if(callBackSave!=null && typeof callBackSave!='undefined' )
		    						callBackSave(data) ;
		    					
		    					if( $('#modal_full').length>0 ) 
		    						$("#modal .close").click();
		    					else
		    						$(".modal .close").click();
		    					}
		    				);

				});

			  if(typeof params !='undefined')
				  callBackOnShowModal(params);
			  else
				  callBackOnShowModal();

		  });
		  
		  
	  }catch(e){
		  console.error(e);
	  }
	
}

function _modal_full(titulo,url_html,callBackOnShowModal,callBackSave,ancho){
	
	  try{
		//michael ya me recorde q tratamos de arreglar y no nos salio...estas??
		  var width=600;
		  if(typeof ancho!='undefined')
			  width=ancho;
		  $modal = $('#modal_full').modal('show');
		  $modal.draggable({handle: ".modal-header"});
		  $modal.find('.modal-content').resizable({
		      minHeight: 400,
		      minWidth: width
		    });		  
		  
		  $modal.find('.modal-content.ui-resizable').css('width','800px');
		  $modal.find('.modal-content.ui-resizable').css('heigth','600px');
		  //$modal.find('.modal-content.ui-resizable').css(' margin-right',auto);
		  //$modal.find('.modal-content.ui-resizable').css(' margin-left',auto);
		  
		  $modal.on('shown.bs.modal', function() {
			  _focus($(this));
		    })
		    
		    $modal.on('hidden.bs.modal', function () {
	    	$('#modal_full .modal-body ').html('');
	    })
	    //alert($modal.find('.modal-body').length);
		  $modal.find('.modal-body').load(url_html, function(){
			  $modal.find('.modal-title').html(titulo);
			  $modal.find('form').on('submit',function(event){
		    		event.preventDefault();
					
		    		_post($(this).attr('action') , $(this).serialize(),
		    				function(data){
		    					callBackSave(data) ;
		    					$("#modal_form_vertical2 .close").click()
		    					}
		    				);
				});

			  callBackOnShowModal();
		  });
		  
	  }catch(e){
		  console.error(e);
	  }
	
}

function _inputs(contenedor){
	var content = " ";
	if (typeof contenedor !== 'undefined') 
		content = "#" + contenedor + " ";
	
	$(content + ".number").keydown(function(event) {
		var key = event.charCode || event.keyCode || 0;

		var valor = (
                key == 8 || 
                key == 9 ||
                key == 46 ||
                key == 32 || //tab
                //(key >= 37 && key <= 57) ||
                (key >= 48 && key <= 57) ||
                //(key >= 65 && key <= 90) ||
                (key >= 96 && key <= 105) ||
                key == 192 || key==0// enie
                );

		if (!valor)
			event.preventDefault();
		
	});
	
	
    // Select with search
	$(content + '.select-search').select2();
	//alert();
	$(content + '.disabled').on('mousedown', function(event){//desabilitando combos
		event.preventDefault();
		$(this).css('cursor','not-allowed');
	});
	$('.daterange-single').daterangepicker({ 
        singleDatePicker: true,
        locale: { format: 'DD/MM/YYYY'}
    	}
	);

}

function _disabled_combo(obj, disabled){
	if (disabled){
		
		obj.on('mousedown', function(event){//desabilitando combos
			event.preventDefault();
			$(this).css('cursor','not-allowed');
		});
	}else{
		obj.removeClass('disabled');
		obj.off();
		$(this).css('cursor','initial');
	}
	
}

function _onloadPage(){
	$('#_botonera').html('');
	$('#_clonar').html('');
	$('#_a_titulo').unbind( 'click' );//limpiar evento del click del link titulo bar
	_inputs();
	//_focus();
}

function _disabled(div){
	$(div).find('input').each(function() {
		$(this).attr("disabled","disabled");
	});
}

/**
 * 
 * @param objContent
 * @returns
 */
function _focus(objContent){
	var focused = false;
	var content = "";

	if (!(objContent instanceof jQuery))
		objContent = $('.content-body');
		
	$('input', objContent).each(function () {
	    if (!focused && $(this).attr("disabled")!='disabled' && $(this).attr("readonly")!='readonly' && ( $(this).prop("type")=='text' ||  $(this).prop("type")=='number' || $(this).prop("type")=='select') && $(this).attr("id")  ){
	    	focused = true;
	    	$(this).focus();
	    }
	});
		
	/*
	if (typeof idContent != 'undefined')
		content = "#" + idContent + " ";
	
	if (objContent instanceof jQuery)
	

*/
}
/*
 * Funcion para cargar el valor dependiente a partir del valor seleccionado de la tabla dependiente
 */
function _attachEvent(id_tabla_independiente, id_tabla_depediente){
	
	//Para el caso de que las areas dependen del nivel(Nivel esta dentro de periodo)
	if(id_tabla_independiente=='id_per' && id_tabla_depediente=='id_caa' ){
		$('#'+id_tabla_independiente).on('change',function(event){
			var niv= $('#'+id_tabla_independiente +' option:selected').data('aux2');
			_llenarCombo('col_area_anio',$('#'+id_tabla_depediente),null,niv);
		});	
	}
	
	//Para el caso de que los grados dependen del nivel(Nivel esta dentro de periodo)
	if(id_tabla_independiente=='id_per' && id_tabla_depediente=='id_gra' ){
		$('#'+id_tabla_independiente).on('change',function(event){
			var niv= $('#'+id_tabla_independiente +' option:selected').data('aux2');
			_llenarCombo('cat_grad',$('#'+id_tabla_depediente),null,niv);
		});	
	}
	
	if(id_tabla_independiente=='id_per' && id_tabla_depediente=='id_gra_todos' ){
		$('#'+id_tabla_independiente).on('change',function(event){
			var niv= $('#'+id_tabla_independiente +' option:selected').data('aux2');
			_llenarCombo('cat_grad_todos',$('#id_gra'),null,niv,function(){$('#id_gra').change()});
		});	
	}
	/*//Para el caso de que los grados dependen de nivel
	if(id_tabla_independiente=='id_niv' && id_tabla_depediente=='id_gra' ){
		$('#'+id_tabla_independiente).on('change',function(event){
			var niv= $('#'+id_tabla_independiente +' option:selected').data('id');
			_llenarCombo('cat_grad',$('#'+id_tabla_depediente),null,niv);
		});	
	}*/
	
	
}

/*
 * 
 
				    "drawCallback": function( settings ) {
	
				        var $tr    = $(this).find('thead').children();
				        var $clone = $tr.clone();
				        $clone.find(':text').val('');
				        $clone.find('th').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
				        $clone.find('th').removeAttr('aria-label');
				        $clone.find('th').removeClass('sorting');
				        $clone.find('th').removeAttr('aria-controls');
				        
				        //alert($clone.html());
				        $tr.after("<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td></tr>");

				        //var that = this;
				        $('input', $clone).each(function () {
				        	$(this).on('keyup change', function () {
				        		//$xxx.search(this.value).draw();
					        });
				        });
				    }
				    
 * ordenar por columnas
 * ORIGINAL
 * table.columns().every(function (index) {
        $('#example thead tr:eq(1) th:eq(' + index + ') input').on('keyup change', function () {
            table.column($(this).parent().index() + ':visible')
                .search(this.value)
                .draw();
        });
    });
    
 * "drawCallback": function( settings ) {

				        var $tr    = $(this).find('thead').children();
				        var $clone = $tr.clone();
				        $clone.find(':text').val('');
				        $clone.find('th').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
				        $clone.find('th').removeAttr('aria-label');
				        $clone.find('th').removeClass('sorting');
				        //var that = this;
				        $('input', $clone).each(function () {
				        	$(this).on('keyup change', function () {
				        		$xxx.search(this.value).draw();
					        });
				        });
				        $tr.after($clone);
				        table.columns().every( function () {
					        var that = this;
					        $('input', this.footer()).on('keyup change', function () {
					            that.search(this.value).draw();
					        });
					    });

				        $('#col_curso_anio-tabla thead td').not(':last-child').each(function () {
					        var title = $('#col_curso_anio-tabla thead th').eq($(this).index()).text();
					        $(this).html('<input type="text" class="form-control input-sm" placeholder="Search '+title+'" />');
					    });
*/
/**
 * Inicializa los DT del SIGE
 * @param settings
 * @param idTabla
 * @returns
 */
function _initCompleteDTSB(settings){
	
    var table = settings.oInstance.api();
    var idTabla =table.table().node().id;
	var paginator = $('#' + idTabla + '_length').find('#_paginator');
	//alert($('#' + idTabla).find('trSearch').length);

	$('<span><a href="#" title="BUSQUEDA AVANZADA" onclick="_hiddenSearchTr(\'#' + idTabla + '\')"> <i class="fa fa-search"></i></a>&nbsp;</span>').insertBefore(paginator);

	if($('#' + idTabla).find('.trSearch').length== 1 ){
		$('#' + idTabla).find('.trSearch').remove();
	}
	
		//(LUPA) - PARA BUSCAR P0OR COLUMNA

		var $tr    = $('#' + idTabla).find('thead').children();
		var $clone = $tr.clone();
		

		$tr.after("<tr class='trSearch' style='display:none'>" + $clone.html() + "</tr>");	
		//$tr.after("<tr class='trSearch' >" + $clone.html() + "</tr>");	
		var $trSearch =  $('#' + idTabla).find('.trSearch');
		//$trSearch.find(':text').val('');
		//$trSearch.find('th').not(':last-child').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
		$trSearch.find('th').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
		//$trSearch.find(':last-child').html('');
		$trSearch.find('th').removeAttr('aria-label');
		$trSearch.find('th').removeClass('sorting');
		$trSearch.find('th').removeAttr('aria-controls');
		
	    table.columns().every(function (index) {
			
	        $('#' + idTabla + ' thead tr:eq(1) th:eq(' + index + ') input').on('keyup change', function () {

				table.column($(this).parent().index() + ':visible')
	                .search(this.value)
	                .draw();

	        });
	    });
		

}
function _initCompleteDTPrueba(settings){
	
    var table = settings.oInstance.api();
    var idTabla =table.table().node().id;
	var paginator = $('#' + idTabla + '_length').find('#_paginator');
	//alert($('#' + idTabla).find('trSearch').length);

	$('<span><a href="#" title="BUSQUEDA AVANZADA" onclick="_hiddenSearchTr(\'#' + idTabla + '\')"> <i class="fa fa-search"></i></a>&nbsp;</span>').insertBefore(paginator);

	if($('#' + idTabla).find('.trSearch').length== 1 ){
		$('#' + idTabla).find('.trSearch').remove();
	}
	
		//(LUPA) - PARA BUSCAR P0OR COLUMNA
//		alert(idTabla);

		var $tr    = $('#' + idTabla).find('thead').children();
		var $clone = $tr.clone();
		

		//$tr.after("<tr class='trSearch' style='display:none'>" + $clone.html() + "</tr>");	
		$tr.after("<tr class='trSearch' >" + $clone.html() + "</tr>");	
		var $trSearch =  $('#' + idTabla).find('.trSearch');
		
//		alert($trSearch.html());
		$trSearch.find(':text').val('');//no sale la lupita, antes recuerdas q salia eso es con estilo del html pero como no se necesitó se saco
		$trSearch.find('th').not(':last-child').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
		$trSearch.find(':last-child').html('');0
		$trSearch.find('th').removeAttr('aria-label');
		$trSearch.find('th').removeClass('sorting');
		$trSearch.find('th').removeAttr('aria-controls');
		
	    table.columns().every(function (index) {
	        $('#' + idTabla + ' thead tr:eq(1) th:eq(' + index + ') input').on('keyup change', function () {
	        	table.column($(this).parent().index() + ':visible')
	                .search(this.value)
	                .draw();
	        });
	    });
		

}
function _initCompleteDT(settings){
	
    var table = settings.oInstance.api();
    var idTabla =table.table().node().id;
	var paginator = $('#' + idTabla + '_length').find('#_paginator');
	//alert($('#' + idTabla).find('trSearch').length);

	$('<span><a href="#" title="BUSQUEDA AVANZADA" onclick="_hiddenSearchTr(\'#' + idTabla + '\')"> <i class="fa fa-search"></i></a>&nbsp;</span>').insertBefore(paginator);

	if($('#' + idTabla).find('.trSearch').length== 1 ){
		$('#' + idTabla).find('.trSearch').remove();
	}
	
		//(LUPA) - PARA BUSCAR P0OR COLUMNA
		//alert(idTabla);

		var $tr    = $('#' + idTabla).find('thead').children();
		var $clone = $tr.clone();
		

		//$tr.after("<tr class='trSearch' style='display:none'>" + $clone.html() + "</tr>");	
		$tr.after("<tr class='trSearch'>" + $clone.html() + "</tr>");	
		var $trSearch =  $('#' + idTabla).find('.trSearch');
		//$trSearch.find(':text').val('');
		//$trSearch.find('th').not(':last-child').html('<input type="text" class="form-control input-sm" placeholder="Search " />');
		$trSearch.find('th').html('<input size="2" type="text" class="form-control input-sm" placeholder="Buscar " />');
		//$trSearch.find(':last-child').html('');
		$trSearch.find('th').removeAttr('aria-label');
		$trSearch.find('th').removeClass('sorting');
		$trSearch.find('th').removeAttr('aria-controls');
		
	    table.columns().every(function (index) {
			
	        $('#' + idTabla + ' thead tr:eq(1) th:eq(' + index + ') input').on('keyup change', function () {
console.log(index+''+this.value+'- ' +$(this).parent().index());
				table.column($(this).parent().index() )
	                .search(this.value)
	                .draw();

	        });
	    });
		

}

function _hiddenSearchTr(id){
	var tr = $(id).find('.trSearch');
	if (tr.is(":visible"))
		tr.hide();
	else
		tr.show();
}

function _agregarIcono(settings, title, funcion, icon,texto){
    var table = settings.oInstance.api();
    var idTabla =table.table().node().id;
	var paginator = $('#' + idTabla + '_length').find('#_paginator'); 
	  if (typeof texto != 'undefined'&& texto!=null ){
		  $('<span>'+texto+'<a href="#" title="' + title.toUpperCase() + '" onclick="' + funcion + '"> <i class="' + icon + '"></i></a>&nbsp;</span>').insertBefore(paginator);
	  } 
	else{
		$('<span><a href="#" title="' + title.toUpperCase() + '" onclick="' + funcion + '"> <i class="' + icon + '"></i></a>&nbsp;</span>').insertBefore(paginator);
	}
			

}

function _dataTable_total(settings,columna){

	var table = settings.oInstance.api();
	var idTabla =table.table().node().id;
	var columns =  table.columns().header().length;
	var finded = $('#' + idTabla).find('#_th_total');
	if(finded.length==0 ){
		var tr = "<tr><th colspan='" + (columns-1) + "' class='text-right'><b>TOTAL</b></th><th id='_th_total'>s</th></tr>";
		var $thead    = $('#' + idTabla).find('thead tr');
		$(tr).insertBefore($thead);
	}

	var th_total =  $('#' + idTabla).find('#_th_total');

	var total = 0;
	table.rows().every(function (index) {
		 var row = table.row( index );
		 var data = row.data()[columna];
		 total = total + parseFloat(data);
	});
	
	th_total.html( '<b>S/.' +  $.number( total, 2) + '</b>');
}


function _dataTable_total_sin_punto(settings,columna){

	var table = settings.oInstance.api();
	var idTabla =table.table().node().id;
	var columns =  table.columns().header().length;
	var finded = $('#' + idTabla).find('#_th_total');
	if(finded.length==0 ){
		var tr = "<tr><th colspan='" + (columns-1) + "' class='text-right'><b>TOTAL</b></th><th id='_th_total'>s</th></tr>";
		var $thead    = $('#' + idTabla).find('thead tr');
		$(tr).insertBefore($thead);
	}

	var th_total =  $('#' + idTabla).find('#_th_total');

	var total = 0;
	table.rows().every(function (index) {
		 var row = table.row( index );
		 var data = row.data()[columna];
		 total = total + parseFloat(data);
	});
	
	th_total.html( '<b>S/.' +   total.toFixed(2)+ '</b>');
}
 
function _fechaHtml(dia,nom_campo){
	var fecha  = dia.split("-");
	$('#modal').find('#'+nom_campo).val(fecha[2] + "/" + fecha[1] + "/" + fecha[0]);
}

function _llenarComboArreglo(data, combo, _id, funcion){
	//remover todos excepto el q viene por defecto
	
	var valor = combo.find("option:first-child").val();
	
	if (valor==''){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	
	//PARCHE.. MEJHORARLO EN JDK1.8
			  for ( var i in data) {
					var id = data[i].id;
					var value = data[i].value;
						combo.append('<option value="'+ id +'">' +value + '</option>');
				}

			  if (typeof _id != 'undefined'&& _id!=null ){
				  if(combo.hasClass("select-search"))
						combo.val(_id).change();
				  else
						combo.val(_id);
			 }
			  if (typeof funcion != 'undefined'&& funcion!=null ){
				  funcion();
			  }		  
			  

}

function _fechaActual(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd = '0'+dd
	} 
	if(mm<10) {
	    mm = '0'+mm
	} 
	today = dd + '/' + mm + '/' + yyyy;
	return today;

}

function _alert_error(msg, _function){
	swal({
		title : "Error",
		text :msg,
		confirmButtonColor : "#2196F3",
		type : "error"
	},function(){
		if (typeof _function !='undefined'){
				_function();
			}
		}
	);

}

function _alert(titulo,msg){
	swal({
        html:true,
        title: "<font color='black'>" + titulo + "</font>",
        text: msg,
        confirmButtonColor: "#66BB6A",
        type: "success"
    });
}


function _parseDate(fechaSQL){
	var arr= fechaSQL.split('-');
	return arr[2] + "/" + arr[1] + "/" + arr[0]; 
}

function _blockUI(accion){
	if (accion)
	 $.blockUI({ 
	        message: '<i class="icon-spinner4 spinner"></i>',
	        //timeout: 2000, //unblock after 2 seconds
	        overlayCSS: {
	            backgroundColor: '#1b2024',
	            opacity: 0.8,
	            zIndex: 1200,
	            cursor: 'wait'
	        },
	        css: {
	            border: 0,
	            color: '#fff',
	            padding: 0,
	            zIndex: 1201,
	            backgroundColor: 'transparent'
	        }
	    });
	else
		$.unblockUI();
}

/*
 * primary, success, info, warning or danger*/
function _alertHTML(tipo,texto,reemplazaContenido){
	if (reemplazaContenido){
		$(".content-body").load('alert.html', function(responseText, textStatus, req){
			$(this).find('#_text').html(texto);
		});
	}else{
		//agrega pero sin borrar el contenido
	}
}

function incrementarFecha(days){
    var milisegundos=parseInt(35*24*60*60*1000);
 
    var fecha=new Date();

   var day=fecha.getDate();
    // el mes es devuelto entre 0 y 11
    var month=fecha.getMonth()+1;
   var  year=fecha.getFullYear();
 
    //document.write("Fecha actual: "+day+"/"+month+"/"+year);
 
    //Obtenemos los milisegundos desde media noche del 1/1/1970
   var tiempo=fecha.getTime();
    //Calculamos los milisegundos sobre la fecha que hay que sumar o restar...
   var milisegundos=parseInt(days*24*60*60*1000);
    //Modificamos la fecha actual
  var  total=fecha.setTime(tiempo+milisegundos);
  var  day=fecha.getDate();
  var  month=fecha.getMonth()+1;
  var  year=fecha.getFullYear();
 
    //document.write("Fecha modificada: "+day+"/"+month+"/"+year);
    
  var  fecha_actual_incrementada=day+"/"+month+"/"+year;
    return fecha_actual_incrementada;
}

function _block(){
	
	$.blockUI({ 
        message: '<i class="icon-spinner4 spinner"></i>', 
        overlayCSS: {
            backgroundColor: '#1b2024',
            opacity: 0.8,
            zIndex: 1200,
            cursor: 'wait'
        },
        css: {
            border: 0,
            color: '#fff',
            padding: 0,
            zIndex: 1201,
            backgroundColor: 'transparent'
        }
    });
	
}

function _ver_alumno(label){
	
	var id = $(label).data('id');
	 __ver_alumno (id);
	
}

function __ver_alumno (id){
	
	var callBackModalVerAlumno = function(){
		_get('api/alumno/obtenerDatosAlumnoxId/' + id,
				function(data){
					_fillForm(data,$('#_alumno-div') );
					_llenarCombo('cat_tipo_documento',$('#id_tdc'), data.id_tdc);
				});
		_get('api/matricula',
				function(data){
					if (data==null){
						data = {};
						data.id_suc='';
						data.id_niv='';
						data.id_gra='';
						data.secc='';
						data.sit_nom='';
						data.id_cme='';
					}
					_llenarCombo('col_anio',$('#_alu_situacion-tab #id_anio'), (parseFloat($('#_id_anio').text())));
					_llenarCombo('ges_sucursal',$('#_alu_situacion-tab #id_suc'), data.id_suc);
					_llenarCombo('cat_nivel',$('#_alu_situacion-tab #id_niv'), data.id_niv);
					_llenarCombo('cat_grad',$('#_alu_situacion-tab #id_gra'), data.id_gra);
					//_llenarCombo('id_au',$('#_alu_situacion-tab #id_au'), data.id_niv);
					$('#_alu_situacion-tab #id_au').val(data.secc);
					$('#_alu_situacion-tab #sit_nom').val(data.sit_nom);
					$('#_alu_situacion-tab #id_cme').val(data.modalidad);
					listar_responsables(data.id);
				}
		, {id_alu:id, id_anio:(parseFloat($('#_id_anio').text()))}//año anterior TODO mejorar
		);
		
		

		$('#_alu_situacion-tab #id_anio').on('change', function(e){
			_get('api/matricula',
					function(data){
						if (data!=null){
							$('#_alu_situacion-tab #id_suc').val(data.id_suc);
							$('#_alu_situacion-tab #id_niv').val(data.id_niv);
							$('#_alu_situacion-tab #id_gra').val(data.id_gra); 
							$('#_alu_situacion-tab #id_au').val(data.secc);
							$('#_alu_situacion-tab #sit_nom').val(data.sit_nom);
							$('#_alu_situacion-tab #id_cme').val(data.modalidad);
							listar_responsables(data.id);
						}else{
							$('#_alu_situacion-tab #id_suc').val('');
							$('#_alu_situacion-tab #id_niv').val('');
							$('#_alu_situacion-tab #id_gra').val(''); 
							$('#_alu_situacion-tab #id_au').val('');
							$('#_alu_situacion-tab #sit_nom').val('');
							$('#_alu_situacion-tab #id_cme').val('');
						}
					}
			, {id_alu:id, id_anio:$(this).val()}//año anterior TODO mejorar
			);
		});
		
		//pagos pendientes del alumno
		_get('api/pagos/alumno/pagosPendientes/' + id,
				function(data){

					$('#_alu_mensualidades-tabla').dataTable({
						 data : data,
						 aaSorting: [], 
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 bLengthChange: false,
						 bPaginate: false,
						 bFilter: false,
					     bInfo : false,
				         columns : [ 
					     	   {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
				               {"title":"Mes",  "data" : "mes"},
 				               {"title":"Monto", "render": function ( data, type, row ) { return 'S/.' +  $.number(row.monto,2) }},
				               {"title":"Desc. Adm.", "render": function ( data, type, row ) { return 'S/.' +  $.number(row.desc_pronto_pago,2) }}
					    ]
				    });
				}
		);

		//padres del alumno
		_GET({url:'api/alumno/listarPadres?id_alu=' + id,
			  success: function (data){
				  $('#_padres-tabla').dataTable({
						 data : data,
						 aaSorting: [], 
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 bLengthChange: false,
						 bPaginate: false,
						 bFilter: false,
					     bInfo : false,
					     scrollX: true,
				         columns : [ 
					     	   {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
					     	   {"title":"Padre",  "data" : "padre",
					     		  "render":function ( data, type, row,meta ) { 
					     			  if(row.apoderado=='1')
					     				  return "<font title='APODERADO' color='green'><b>" + data + "</B></font>";
					     			  else
					     				  return data;
					     		  }},
					     	   {"title":"Parentesco",  "data" : "parentesco"},
					     	   {"title":"Nro. doc.",  "data" : "nro_doc"},
					     	   {"title":"Celular", "render": function ( data, type, row ) {
					     		   if(row.cel_val=='1')
					     			   return '<font color="green">' + row.cel + '</font>';
					     		   else
					     			  return row.cel;
					     	   }},
					     	   {"title":"Telf. fijo",  "data" : "tlf"}
					    ]
				    });
			  }
		});
		
		function listar_responsables(id_mat){
			_GET({url:'api/alumno/listarResponsablesMat?id_mat=' + id_mat,
			  success: function (data){
						  $('#_alu_responsables-tabla').dataTable({
								 data : data,
								 aaSorting: [], 
								 destroy: true,
								 orderCellsTop:true,
								 select: true,
								 bLengthChange: false,
								 bPaginate: false,
								 bFilter: false,
								 bInfo : false,
								 scrollX: true,
								 columns : [ 
									   {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
									   {"title":"Responsable",  "data" : "padre",
										  "render":function ( data, type, row,meta ) { 
											  if(row.res=='res_mat')
												   return "<font color='green'><b>RESPONSABLE MATRÍCULA</B></font>";
												  //return "<font title='APODERADO' color='green'><b>" + data + "</B></font>";
											  else if(row.res=='res_eco')
												   return "<font color='green'><b>RESPONSABLE ECONÓMICO</B></font>";
											  else if(row.res=='res_aca')
												   return "<font color='green'><b>RESPONSABLE ACADÉMICO</B></font>";
										  }},
									   {"title":"Apellidos y Nombres",  "data" : "responsable"},
									   {"title":"Nro. doc.",  "data" : "nro_doc"},
									   {"title":"Celular",  "data" : "cel"},
									   {"title":"Correo",  "data" : "corr"}
								]
							});
					  }
				});
		}	
		
	};
		
		
	var callBackSaveAlumno = function(){};
	
	_modal_full('Datos del alumno','pages/alumno/alu_alumno_tabs.html',callBackModalVerAlumno,callBackSaveAlumno);
	
}

function _formatMonto(numero){
	if (numero==null)
		return '0.00';
	var monto=numero.toFixed(2);
	return monto;
}

/**
 * 
 * @param url
 * @param propiedad que se obtendrà el value
 * @param combo
 * @param _id opcional
 * @param funcion opcional
 */
function _llenarComboApi(url, _value, combo, _id, funcion,context){

	
	//remover todos excepto el q viene por defecto
	
	var valor = combo.find("option:first-child").val();
	
	if (valor=='' || valor=='-1'){
		combo.find('option').not(':first').remove();
	}else{
		combo.empty();
	}
	

		_GET(
			{url:url,
			context: context,
			success:function (data) {
			
		  for ( var i in data) {
				var id = data[i].id;
				var value = data[i][_value];
				combo.append('<option value="'+ id +'">' +value + '</option>');
			}
		  		//combo.attr();
		
		  
			  if (typeof _id != 'undefined'&& _id!=null ){
				  if(combo.hasClass("select-search")){
					  combo.val(_id);
					  combo.trigger('change.select2');//combo.val(_id).change();
				  }else
						combo.val(_id);
			 }
			  if (typeof funcion != 'undefined'&& funcion!=null ){//esto estaba :(, no se como se borró-
				  funcion();
			  }		  
			 
			}
		});

}


//strings
function _pad(str,pad) {
	
	 if(typeof str == 'number')
		 str = str.toString();
	
	return pad.substring(0, pad.length - str.length) + str;
}


function _POST(params){
	console.log('3');
	var _confirm = null;
	if("confirm" in params)		_confirm 	= params['confirm'];
	console.log(_confirm);
	if(_confirm!=null){

		console.log('4');
		swal(
				{
					title : "Esta seguro?",
					text :_confirm,
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "rgb(33, 150, 243)",
					cancelButtonColor : "#EF5350",
					confirmButtonText : "Si, Continuar",
					cancelButtonText : "No, cancela!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					console.log('5');
					if (isConfirm) {
 						_POST_(params);
					}else{
 							swal({
								title : "Cancelado",
								text : "No se ha realizado ninguna acción",
								confirmButtonColor : "#2196F3",
								type : "error"
							});
						}
					}
				);
	}else{
		_POST_(params);
	}
	

	
	
}

function _POST_(params) {

	var _type = 'POST';
	var _params = {};
	var _form = {};
	//var _fncError = function(){};
	var _fncSuccess = function(){};
	var _url = '';
	var _msg_type = 'modal';
	var _context = _URL;
	var _silent = false;
	var _confirm = false;
	var _button = null;
	var _contentType='application/x-www-form-urlencoded; charset=UTF-8' ;
	var _msg_exito = 'La operacion se efectuo exitosamente.';
	var _cookie = true;
	
 	if("form" in params) {
		_form = params["form"];
		_params = _form.serialize();
		_url	= _form.attr('action');
		
		if("params" in params) {
			_params = _params + "&" + params["params"];
		}
	}
	if("type" in params) 	_type = params["type"];
	if("params" in params && !("form" in params) ) 	_params = params["params"];
	if("error" in params) 		_fncError	= params["error"];
	if("success" in params) 	_fncSuccess = params["success"];
	if("url" in params) 		_url 		= params["url"];
	if("silent" in params) 		_silent 	= params["silent"];
	if("context" in params) 	_context 	= params["context"];
	if("msg_type" in params)	_msg_type 	= params["msg_type"];
	if("confirm" in params)		_confirm 	= params['confirm'];
	if("button" in params)		_button 	= params['button'];
	if("msg_exito" in params)	_msg_exito 	= params['msg_exito'];
	if("contentType" in params)	_contentType 	= params['contentType'];
	if("cookie" in params)		_cookie 	= params['cookie'];
	
	if(_button!=null)
		_button.attr('disabled','disabled');
	
	$.blockUI({
		message : '<i class="icon-spinner4 spinner"></i>',
		overlayCSS : {
			backgroundColor : '#1b2024',
			opacity : 0.8,
			zIndex : 1200,
			cursor : 'wait'
		},
		css : {
			border : 0,
			color : '#fff',
			padding : 0,
			zIndex : 1201,
			backgroundColor : 'transparent'
		}
	});


	var _token = (_cookie) ? getCookie("_token"): '';
	
	$.ajax({
				type : _type,
				url : _context + _url,
				data : _params,
				//data : JSON.stringify(_params),
				//dataType: 'json',
				//data : JSON.stringify(form),
				contentType: _contentType,
				//dataType: 'json',
				timeout : 100000,
				headers: {
			        'Authorization':_token
				},    
				success : function(data) {
					
					if(_button!=null) _button.removeAttr('disabled');
					
 					if (data.code && data.code != '200') {
 						
						if (typeof _fncError != 'undefined') {
 							_fncError(data);
						} else {
 							var mensajeError = data.msg;

							if (data.code == '400')
								mensajeError = 'Se ha enviado un dato no válido, por favor verificar';
							swal({
								html : true,
								title : "<font color='black'>Error</font>",
								text : "<font color='black'>" + mensajeError
										+ "</font>",
								confirmButtonColor : "#2196F3",
								type : "error"
							});
							
							
						}

					} else {

						if(!_silent){

							if (_msg_type == 'modal') {
								swal({
									html : true,
									title : "<font color='black'>Transaccion existosa!</font>",
									text : _msg_exito,
									confirmButtonColor : "#66BB6A",
									type : "success"
								});

							}else{
								 new PNotify({
							         title: 'Grabación existosa!',
							         text: _msg_exito,
							         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
							         delay:4000
							     });
							}
							
						}

 
							_fncSuccess(data);
						 

					}

					$.unblockUI();

				},
				error : function(e) {
					
					if(_button!=null) _button.removeAttr('disabled');
					
					console.error(e);
					$.unblockUI();

					swal({
						html : true,
						title : "<font color='black'>Cancelado</font>",
						text : "<font color='black'>Error desconocido</font>",// e.responseText,
						confirmButtonColor : "#2196F3",
						type : "error"
					});

				},
				done : function(e) { 
					$.unblockUI();
				}
			});

}


function _GET(params) {

	var _type = 'GET';
	var _params = {};
	var _form = {};
	var _fncError = function(){};
	var _fncSuccess = function(){};
	var _url = '';
	var _silent = false;
	var _async = true;
	var _context = _URL;
	var _block = false;
	
	if("form" in params) {
		_form = params["form"];
		_params = _form.serialize();
		_url	= _form.attr('action');
	}
	if("type" in params) 	_type = params["type"];
	if("params" in params) 	_params = params["params"];
	if("error" in params) _fncError = params["error"];
	if("success" in params) _fncSuccess = params["success"];
	if("url" in params) 	_url = params["url"];
	if("silent" in params) 	_silent = params["silent"];
	if("async" in params) 	_async = params["async"];
	if("context" in params && typeof params["context"] !='undefined') _context = params["context"];
	if("block" in params) 	_block = params["block"];
	
	var _token = getCookie("_token");

	if (_block) _blockUI(true);
	$.ajax({
				type : _type,
				url : _context + _url,
				data : _params,
				timeout : 100000,
				async : _async,
				contentType : "application/json",
				headers: {
			        'Authorization':_token
				},				
				success : function(data) {
					if (_block) _blockUI(false);
					if (data.code && data.code != '200') {
						if (typeof params["error"] != 'undefined') {
							_fncError(data);
						} else {
							var mensajeError = data.msg;
							if (data.code == '400')
								mensajeError = 'Se ha enviado un dato no válido, por favor verificar';
							swal({
								html : true,
								title : "<font color='black'>Error</font>",
								text : "<font color='black'>" + mensajeError
										+ "</font>",
								confirmButtonColor : "#2196F3",
								type : "error"
							});

						}

					} else {
						
						_fncSuccess(data.result);
					}

					//$.unblockUI();

				},
				error : function(e) {
					console.error(e);
					var status = e.status;
					if(status==403||status==401){
						var data = {};
						data.code=status;
						data.msg = 'No tiene acceso';
						_fncError(data);
					}
					if (_block) _blockUI(false);
					if(!_silent){

						if(status=='404'){
							swal({
								title : "Error",
								text :"El servicio no esta disponible, inténtelo en 5 minutos por favor, si el problema persiste comunicarse con el administrador del sistema",
								confirmButtonColor : "#2196F3",
								type : "error"
							});
						}else if (typeof params["error"] != 'undefined'){ 
							_fncError(data);
						}else{
							swal({
								html : true,
								title : "<font color='black'>Cancelado</font>",
								text : "<font color='black'>Error desconocido</font>",// e.responseText,
								confirmButtonColor : "#2196F3",
								type : "error"
							});	
						}
					}

				},
				done : function(e) { 
					//$.unblockUI();
				}
			});

}


function _GET2(params) {

	var _type = 'GET';
	var _params = {};
	var _form = {};
	var _fncError = function(){};
	var _fncSuccess = function(){};
	var _url = '';
	var _silent = false;
	var _async = true;
	var _block = false;
	
	if("form" in params) {
		_form = params["form"];
		_params = _form.serialize();
		_url	= _form.attr('action');
	}
	if("type" in params) 	_type = params["type"];
	if("params" in params) 	_params = params["params"];
	if("error" in params) _fncError = params["error"];
	if("success" in params) _fncSuccess = params["success"];
	if("url" in params) 	_url = params["url"];
	if("silent" in params) 	_silent = params["silent"];
	if("async" in params) 	_async = params["async"];
	if("context" in params && typeof params["context"] !='undefined') _context = params["context"];
	if("block" in params) 	_block = params["block"];
	

	
	$.ajax({
				type : _type,
				url : _context + _url,
				data : _params,
				timeout : 100000,
				//async : _async,
				contentType : "application/json",			
				success : function(data) {
					
						
						_fncSuccess(data.result);
		

					//$.unblockUI();

				},
				error : function(e) {
					console.error(e);
					var status = e.status;
					if(status==403||status==401){
						var data = {};
						data.code=status;
						data.msg = 'No tiene acceso';
						_fncError(data);
					}
					if (_block) _blockUI(false);

				},
				done : function(e) { 
					//$.unblockUI();
				}
			});

}


function _DELETE(params){
	var _text =  "De eliminar el registro seleccionado?";
	var _type = 'DELETE';
	var _params = {};
	var _fncError = function(){};
	var _fncSuccess = function(){};
	var _url = '';
	var _context = _URL;
	
	console.log(params);

	if("params" in params) 	_params = params["params"];
	if("error" in params) _fncError = params["error"];
	if("success" in params) _fncSuccess = params["success"];
	if("url" in params) 	_url = params["url"];
	if("text" in params) 	_text = params["text"];
	if("context" in params && typeof params["context"] !='undefined') _context = params["context"];
	
	swal(
			{
				title : "Esta seguro?",
				text :_text,
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, Eliminar",
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {

					 $.blockUI({ 
					        message: '<i class="icon-spinner4 spinner"></i>',
					        //timeout: 2000, //unblock after 2 seconds
					        overlayCSS: {
					            backgroundColor: '#1b2024',
					            opacity: 0.8,
					            zIndex: 1200,
					            cursor: 'wait'
					        },
					        css: {
					            border: 0,
					            color: '#fff',
					            padding: 0,
					            zIndex: 1201,
					            backgroundColor: 'transparent'
					        }
					    });
					 
					 
					 	var _token = getCookie("_token");
				
						$.ajax({
							type : "DELETE",
							url : _context + _url,
							data : _params,
							timeout : 100000,
							headers: {
						        'Authorization':_token
							},
							success : function(data) {
								
								if(data.code){
									//EXISTE UN ERROR
									 swal({
											title : "Error",
											text : data.msg,
											confirmButtonColor : "#2196F3",
											type : "error"
										});
								}else{

									console.log("succes",data);
									if (typeof silent == 'undefined') {
										swal({
								            html:true,
								            title: "<font color='black'>Transaccion existosa!</font>",
								            text: "El registro se elimino exitosamente.",
								            confirmButtonColor: "#66BB6A",
								            type: "success"
								        });

									}
										
									
									_fncSuccess(data);

								}
								
								 $.unblockUI();
								//display(data);
							},
							error : function(e) {

								console.error( e);
								console.log("caramba");
								 $.unblockUI();
								 
								 swal({
										title : "<font color='black'>Cancelado</font>",
										text : "<font color='black'>" + e.responseText + "</font>",
										confirmButtonColor : "#2196F3",
										type : "error"
									});
								 
							},
							done : function(e) {
								console.log("DONE");
								//enableSearchButton(true);
								 $.unblockUI();
							}
						});						 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha eliminado ningun registro",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
				}
			});

}

function _block_UI(){
	 $.blockUI({ 
	        message: '<i class="icon-spinner4 spinner"></i>',
	        //timeout: 2000, //unblock after 2 seconds
	        overlayCSS: {
	            backgroundColor: '#1b2024',
	            opacity: 0.8,
	            zIndex: 1200,
	            cursor: 'wait'
	        },
	        css: {
	            border: 0,
	            color: '#fff',
	            padding: 0,
	            zIndex: 1201,
	            backgroundColor: 'transparent'
	        }
	    });
}

function _disabled(obj){
	obj.on('mousedown', function(event){//desabilitando combos
		event.preventDefault();
		obj.css('cursor','not-allowed');
		obj.addClass('disabled');
	});
	
}

function _enabled(obj){
	if (obj.hasClass('disabled')){
		obj.unbind('mousedown');
		obj.removeClass('disabled');
		obj.css('cursor','hand');
	}	
}

function _validateEmail($email) {
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  return emailReg.test( $email );
}

function _download(url, archivo){
	var req = new XMLHttpRequest();
    req.open("GET",url, true);
    req.responseType = "blob";
    var _token = getCookie("_token");
    req.setRequestHeader("Authorization", _token);

    	req.onload = function (event) {
      var blob = req.response;
      console.log(blob.size);
      var link=document.createElement('a');
      link.href=window.URL.createObjectURL(blob);
      
      link.download=archivo;
      link.click();
    };

    req.send();
}

function _validar_clave(contrasenna)
{
	if(contrasenna.length >= 8)
	{		
		var mayuscula = false;
		var minuscula = false;
		var numero = false;
		var caracter_raro = false;
		
		for(var i = 0;i<contrasenna.length;i++)
		{
			if(contrasenna.charCodeAt(i) >= 65 && contrasenna.charCodeAt(i) <= 90)
			{
				mayuscula = true;
			}
			else if(contrasenna.charCodeAt(i) >= 97 && contrasenna.charCodeAt(i) <= 122)
			{
				minuscula = true;
			}
			else if(contrasenna.charCodeAt(i) >= 48 && contrasenna.charCodeAt(i) <= 57)
			{
				numero = true;
			}
			else
			{
				caracter_raro = true;
			}
		}
		if(mayuscula == true && minuscula == true  && numero == true) //	if(mayuscula == true && minuscula == true && caracter_raro == true && numero == true)
		{
			return true;
		}
	}
	return false;
}

/**subir archivo
*/
function _set_upload(codigo,contentFile){
	var img = contentFile.find('img');
	var file = contentFile.find('input');
 
	img.attr("src",_URL + 'api/archivo/codigo/' + codigo);

	img.on('click',function(){
		file.click();
	});
	
	file.on('change',function(){
		_upload_img(codigo,img,file);
	});
}

function _upload_img(codigo,img,file ) {
 	var fd = new FormData(); 
    var files = file[0].files[0]; 
    fd.append('file', files); 
				 
    var url = _URL +  'api/archivo/codigo/' + codigo;
    
	 $.blockUI({ 
	        message: '<i class="icon-spinner4 spinner"></i>',
	        overlayCSS: {
	            backgroundColor: '#1b2024',
	            opacity: 0.8,
	            zIndex: 1200,
	            cursor: 'wait'
	        },
	        css: {
	            border: 0,
	            color: '#fff',
	            padding: 0,
	            zIndex: 1201,
	            backgroundColor: 'transparent'
	        }
	    });    

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: url,
        data: fd,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
        	 $.unblockUI();
        	 
        	console.log(data);
        	var error = data.code;
        	
        	if (error==null){

        		//lectora_listar_tabla(data.result);
				img.attr("src",_URL + 'api/archivo/codigo/' + codigo);

        		 	
        	}else{
        		console.log("Error al subir archivo");
        		 
				 swal({
						title : "Error al procesar el archivo",
						text : data.msg,
						confirmButtonColor : "#2196F3",
						type : "error"
					});
        	}
        		
            
 
        },
       
        error: function (e) {
        	 $.unblockUI();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}
