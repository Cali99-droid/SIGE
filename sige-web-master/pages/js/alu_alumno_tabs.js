//Se ejecuta cuando la pagina q lo llama le envia parametros
var _id_gpf;
var _id_fam;
var _id_alu='';

function onloadPage(params){
	_id_gpf = params.id_gpf;
	_id_fam = params.id_fam;

	//FORMULARIO PARA ACTUALIZAR LOS DATOS DEL USUARIO
	var validator = $("#alu_familiar-frm").validate();
		
		$('#alu_familiar-frm').on('submit',function(event){
			event.preventDefault();
			if ($("#alu_familiar-frm").valid()){
				
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
			}
		});
		
		_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + _id_gpf + "&id_alu=" + _id_alu,
				function(data){
			
				$("#fam_alumnos-tab").load("pages/alumno/alu_familiar_tabs_alu.html", {}, function(responseText, textStatus, req){
					mostrarDatosTabAlumno(data);
				});
				
		});
		
		_get('api/familiar/' + _id_fam,
				function(data){				
					
					console.log(data);
					_fillForm(data,$('#alu_familiar-frm'));
					
					$("#foto").attr('src', 'data:image/png;base64,' + data.foto);

					
					var fec_nac = data.fec_nac;
					if(fec_nac){
						var arrFec_nac = fec_nac.split('-');
						$('#alu_familiar-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
					}
					
					$('#id_dep').on('change',function(){
						_llenarCombo('cat_provincia',$('#id_pro'), data.distrito.id_pro, null, function(){
							$('#id_pro').change();
						});	
					});
					$('#id_pro').on('change',function(){
						_llenarCombo('cat_distrito',$('#id_dist'), data.id_dist);	
					});

					_inputs('alu_familiar-frm');
					$("#alu_familiar-frm [name=id_tap]").val([data.id_tap]);
					$("#alu_familiar-frm [name=id_gen]").val([data.id_gen]);

					//SOLO LECTURA SI HAY DATOS CONFIRMADOS
					if (data.ini=="3"){//DATOS CONFIRMADOS
						$("[name=id_tap]").attr('disabled', true);
						$("[name=id_gen]").attr('disabled', true);
						$('#corr').attr('readonly', 'readonly');
						$('#nro_doc').attr('readonly', 'readonly');
					}
					
					//$("input[name='id_gen']").val(data.id_gen);
					_llenarCombo('cat_tipo_documento',$('#alu_familiar-frm #id_tdc'), data.id_tdc);
					_llenarCombo('cat_parentesco',$('#id_par'), data.id_par);
					_llenarCombo('cat_est_civil',$('#alu_familiar-frm #id_eci'), data.id_eci);
					_llenarCombo('cat_grado_instruccion',$('#id_gin'), data.id_gin);
					_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);
					_llenarCombo('cat_departamento',$('#id_dep'), data.distrito.provincia.id_dep,null, function(){
						$('#id_dep').change();
					});
					
					_llenarCombo('cat_ocupacion',$('#id_ocu'), data.id_ocu);
					
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



/***
 * Tab nro 3 - Alumnos
 * @param data
 * @returns
 */
function mostrarDatosTabAlumno(data) {
	var alumno = data.alumno; 
	
	/**
	 * DATOS DEL ALUMNO (FORMULARIO)
	 */
	_fillForm(alumno,$('#alu_alumno-frm'));
	_llenarCombo('cat_tipo_documento',$('#alu_alumno-frm #id_tdc'), alumno.id_tdc);
	
	$("#alu_alumno-frm [name=id_tap]").val([alumno.id_tap]);
	$("#alu_alumno-frm [name=id_gen]").val([alumno.id_gen]);
	_llenarCombo('cat_est_civil',$('#alu_alumno-frm #id_eci'), alumno.id_eci);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio1'), alumno.id_idio1);
	_llenarCombo('cat_idioma',$('#alu_alumno-frm #id_idio2'), alumno.id_idio2);

	var fec_nac = alumno.fec_nac;
	if(fec_nac){
		var arrFec_nac = fec_nac.split('-');
		$('#alu_alumno-frm #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
	}

	_inputs('alu_alumno-frm');
	
	/**
	 * ALUMNOS REGISTRADOS
	 */
 
	console.log(data.alumnoList);
 
	$('#alumno-tabla').dataTable({
		 data : data.alumnoList,
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
        	 	{"title":"Codigo", "data": "alumno.cod"},
        	 	{"title":"Alumno", "data": "alumno","render": function ( data, type, row ) {
        	 		 return data.ape_pat + " " + data.ape_mat + ", " + data.nom;
        	 	}},
        	 	{"title":"Tip. Doc.", "data": "alumno.tipoDocumento.nom"},
        	 	{"title":"Nro doc.", "data": "alumno.nro_doc"},
        	 	{"title":"Actualizar datos", "render": function ( data, type, row ) {
					return '<button onclick="editarFamiliar(' + row.id +',' + row.id_fam +')" type="button" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-edit"></i> Editar</button>';}
				}
	    ]
	});
 
	
	
}

