//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
	console.log($("#ini").val());
	
	if(!params || !params.actualizo_datos){
		//var tab = params.tab;
		//FORMULARIO PARA ACTUALIZAR LOS DATOS DEL USUARIO
		_inputs('alu_familiar-frm');
		
		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true,
	        autoUpdateInput: false,
	        locale: { format: 'DD/MM/YYYY'}
	    });
		
		var validator = $("#alu_familiar-frm").validate();
		
		$('#alu_familiar-frm').on('submit',function(event){
			event.preventDefault();
			if ($("#alu_familiar-frm").valid()){

				_post($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize());

				/*
				if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
					_post($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize());
				}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
					
					_post_silent($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize(),
						function(data){
							_send('pages/alumno/alu_familiar_confirmacion.html','Confirmacion','Datos ',{actualizo_datos:1},
									function(data){
										$('#id').val(_usuario.id);
									}
								);
							}
						);
				}
				*/
			}
		});
		//alert();
		
		//$('#li-tabs-' + tab).click();
		
		_get('api/familiar/' + _usuario.id,
				function(data){
					var familiar = data.familiar;
					var ini = familiar.ini;
					var corr_val  = familiar.corr_val;
					//comentado en el 2020
					/*if(ini=='1'){
						swal({
				            html:true,
				            title: "<font color='black'>IMPORTANTE</font>",
				            text: "<font color='black'>Ingrese su email personal, se enviará un CÓDIGO DE VERIFICACIÓN necesario para ACTIVAR su cuenta.</font>",
				            confirmButtonColor: "#66BB6A",
				            type: "success"
				        });
						$('#btn-grabar').html('Confirmar datos para continuar');
					}*/
					
					//alert(corr_val);
					if(corr_val==_CORREO_VALIDADO ){
						//alert(2);
						$("#corr").parent().removeClass('input-group');
						$("#corr").attr("readonly","readonly");
						$("#corr").parent().find('span').css('display','none');
					}else{
						//$("#corr").parent().addClass('input-group');
						//$("#corr").parent().find('span').css('display','inline-block');
					}
 					if(familiar.cel_val =='1'){
						//alert(2);
						$("#cel").val(familiar.cel);
						$("#cel").attr("readonly","readonly");
					}
					
					_fillForm(familiar,$('#alu_familiar-frm'));
					
					$("#foto").attr('src', 'data:image/png;base64,' + familiar.foto);

					
					var fec_nac = familiar.fec_nac;
					if(fec_nac){
						var arrFec_nac = fec_nac.split('-');
						$('#fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
					}
					
					$('#id_dep').on('change',function(){
						_llenarCombo('cat_provincia',$('#id_pro'), familiar.distrito.id_pro, null, function(){
							$('#id_pro').change();
						});	
					});
					$('#id_pro').on('change',function(){
						_llenarCombo('cat_distrito',$('#id_dist'), familiar.id_dist);	
					});

					_llenarCombo('cat_tipo_documento',$('#id_tdc'), familiar.id_tdc);
					_llenarCombo('cat_parentesco',$('#id_par'), familiar.id_par);
					_llenarCombo('cat_est_civil',$('#id_eci'), familiar.id_eci);
					_llenarCombo('cat_grado_instruccion',$('#id_gin'), familiar.id_gin);
					_llenarCombo('cat_religion',$('#id_rel'), familiar.id_reli);
					if(familiar.distrito!=null)
					_llenarCombo('cat_departamento',$('#id_dep'), familiar.distrito.provincia.id_dep,null, function(){
						$('#id_dep').change();
					});
					
					_llenarCombo('cat_ocupacion',$('#id_ocu'), familiar.id_ocu);
					
					var map;
					var LONGITUD = -77.530033333333;
					var LATITUD = -9.5272222222222;
					var DIRECCION_DEPARTAMEN	= null;
					var DIRECCION_PROVINCIA		= null;
					var ZOOM = 16;
					
					//LLENAR FAMILIAS
					_get('api/familiar/familias/' + $('#_id_anio').text() + '/' + _usuario.id, function(data){
						var familias = data;
						
						var lista_familias = $('#familia-row');
						var linea = lista_familias.html();
						for ( var i=0; i<data.length-1;i++){
							 $('#familia-row').append(linea);
						}
						
						//llenar la data
						
					//	console.log(lista_familias);
						
						var i=0;
						$('.familia-div').each(function () {
							var familia = data[i];
							var padre_madre = obtener_padre_madre(familia.gruFamFamiliars);
							
							$(this).find('#btn-agregar').data('id_gpf',familia.gruFamFamiliars[0].id_gpf); 
							$(this).find('#btn-agregar').data('id_fam',familia.gruFamFamiliars[0].id_fam); 
							$(this).find('#btn-agregar').on('click', function(e){
								familiar_editarFull($(this),e);	
							});
							
							$(this).find('#titulo').html('FAMILIA ' + padre_madre);
							$(this).find('#familiar-tabla').dataTable({
								 data : familia.gruFamFamiliars,
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
						        	 	{"title":"Familiar",  "render":function ( data, type, row,meta ) { return row.familiar.ape_pat + ' ' + row.familiar.ape_mat +', ' + row.familiar.nom}},
						        	 	{"title":"Nro Doc", "data": "familiar.nro_doc"},
						        	 	{"title":"Parentesco", "data": "familiar.parentesco.par"},
										{"title":"Acciones", "render": function ( data, type, row ) {
						                   return '<ul class="icons-list">'+
												'<li class="dropdown">'+
											'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
												'<i class="icon-menu9"></i>'+
												'</a>'+
											'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + row.id + '" onclick="familiar_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
												'<li><a href="#" data-id="' + row.id + '" onclick="familiar_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
											'</ul>'+
						                   '</li>'+
						                   '</ul>';}
										}
							    ]
						    });
							
							i++;
						});
						
					});
					
					
				  	map = $("#location").width("100%").height("800px").gmap3({
			    	    map:{
			    	        options:{
			    	        	center:[LATITUD,LONGITUD ],
			    	            zoom: ZOOM,
			    	            mapTypeId: google.maps.MapTypeId.ROADMAP,
			    	            mapTypeControl: true,
			    	            mapTypeControlOptions:  {   style: google.maps.MapTypeControlStyle.DROPDOWN_MENU    },
			    	            navigationControl: true,
			    	            scrollwheel: true,
			    	            streetViewControl: false
			    	        },
			    	        events:{                    
			    	            click: function(map, event) // clicking on map adds a new marker
			    	            {
			    	                var lat = event.latLng.lat(), 
			    	                    lng = event.latLng.lng();
			    	                var zoom1 = map.getZoom();
			    	                
			    	               // $("#direccion-form #longitud").val(lng);
			    	            	//$("#direccion-form #latitud").val(lat);
			    	            	//$("#direccion-form #zoom").val(zoom1);
			    	            	
			    	                console.log(lat);
			    	                // save to form
			    	                $("#map_lat").val(lat);
			    	                $("#map_long").val(lng);
			    	                $("#map_zoom").val( zoom1 );

			    	                $(this).gmap3({
			    	                    clear: { id: ["newTag"] }, // remove old/new tags (based on marker id value)
			    	                    marker: { id: 'newTag', latLng: event.latLng } // add new marker on map
			    	                });
			    	            } // end click
			    	        } // end event    	        
			    	    },
			    	        marker:{
			    	        	values:[
			    	        		{latLng:[LATITUD,LONGITUD ], id:["newTag"]},
			    	        		{options:{icon: "http://maps.google.com/mapfiles/marker_green.png"}}
			    	        		]
			    	        	}
			    	        
			    	});

					
					
				});
		
	}else{
		//FORMULARIO PARA INGRESAR EL CODIGO DE CONFIRMACION
		$('#alu_familiar-frm').on('submit',function(event){
				event.preventDefault();
				_post_silent($('#alu_familiar-frm').attr('action'), $('#alu_familiar-frm').serialize(),function(data){
					console.log(data);
					if(data.msg==null)
						swal(
							{   html: true,
								title : "Exito",
								text : "<FONT COLOR='GREEN'>CUENTA ACTIVADA</FONT><BR><B>GRACIAS!</B>",
								type : "warning",
								showCancelButton :false,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "Aceptar",
								//cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : false
							},
							function(isConfirm) {
								$('body').addClass('login-container');
								_send('change.html','Cambio de clave','',{},
										function(data){
											$('#id').val(_usuario.id);
											$('#id_per').val(_usuario.id_per);
										}
									);
								//document.location.href = 'dashboard.html';
							})
					else
						swal({
				            html:true,
				            title: "<font color='black'>CUENTA NO ACTIVADA</font>",
				            text: "<font color='RED'> COPIE CORRECTAMENTE EL CODIGO DE VERIFACI&Oacute;N ENVIADO A SU EMAIL.</font><br><B>VERIFIQUE QUE SU EMAIL ESTE CORRECTAMENTE ESCRITO!</B>",
				            confirmButtonColor: "#66BB6A",
				            type: "success"
				        });
					
					
					
				});
		});
		
	}
	
	
}
//se ejecuta siempre despues de cargar el html

function obtener_padre_madre(gruFamFamiliars){
	
	console.log('gruFamFamiliars',gruFamFamiliars);

	var padre = "";
	var madre = "";
	for ( var i=0; i<gruFamFamiliars.length;i++){
		if (gruFamFamiliars[i].familiar.id_par == _PARENTESCO_PADRE){
			padre = gruFamFamiliars[i].familiar.ape_pat;
		}
		if (gruFamFamiliars[i].familiar.id_par == _PARENTESCO_MADRE){
			 madre = gruFamFamiliars[i].familiar.ape_pat;
		}
	}
	
	if (padre!="" && madre!="")
		return padre + " - " + madre;
	else if (padre!="")
		return padre;
	else
		return madre;
}

// Map settings
function initialize() {
	var map;
	// Optinos
	var mapOptions = {
		zoom: 12,
		center: new google.maps.LatLng(47.496, 19.037)
	};

	// Apply options
	map = new google.maps.Map($('.map-basic')[0], mapOptions);
}

$(function(){


	
	

	
	
});



function validarCorreo(){
	var corr = $('#corr').val();
	if(_validateEmail){
		_POST({url:'api/seguridad/validarCorreo',
			   params:{id:_usuario.id, id_per:_usuario.id_per, corr: corr},
			   silent:true,
			   success:function(data){
				   _alert('Estimad@, para continuar revise su correo electrónico y dele click al link enviado.');
			   }
			  });
	}else{
		_alert_error('Por favor ingrese un correo válido.');
	}
}