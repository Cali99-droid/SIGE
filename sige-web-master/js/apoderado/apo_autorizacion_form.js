//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
		_onloadPage();
	
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
				
				if($("#ini").val()=="2"){//SI YA TIENE TODO VALIDADO
					_post($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize());
				}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
					
					_post_silent($('#alu_familiar-frm').attr('action') , $('#alu_familiar-frm').serialize(),
						function(data){
						
							_send('pages/alumno/alu_familiar_confirmacion.html','Confirmacion','Datos del familiar',{actualizo_datos:1},
									function(data){
										$('#id').val(_usuario.id);
									}
								);
							}
						);
				}
			}
		});
		//alert();
		
		_get('api/familiar/' + _usuario.id,
				function(data){				
					
					
					
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
							
							console.log(familia.gruFamFamiliars);
							
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
						                   var html= '<ul class="icons-list">'+
												'<li class="dropdown">'+
											'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
												'<i class="icon-menu9"></i>'+
												'</a>'+
											'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + row.familiar.id + '" data-id_par="' + row.id_par + '" onclick="familiar_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>';
						                   
						                   if (row.id_par!='1' && row.id_par!='2' )
						                	   html= html + '<li><a href="#" data-id="' + row.familiar.id + '" onclick="familiar_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>';
						                   html = html +	
											'</ul>'+
						                   '</li>'+
						                   '</ul>'
						                   
						                   return html;
						                   
										}
										}
							    ]
						    });
							
							$(this).find('#hijos-tabla').dataTable({
								 data : familia.gruFamAlumnos,
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
						        	 	{"title":"Alumno",  "render":function ( data, type, row,meta ) { return row.alumno.ape_pat + ' ' + row.alumno.ape_mat +', ' + row.alumno.nom}},
						        	 	{"title":"Nivel",  "data":"alumno.matricula.grad.nivel.nom"},
						        	 	{"title":"Grado",  "data":"alumno.matricula.grad.nom"},
						        	 	{"title":"Seccion",  "data":"alumno.matricula.aula.secc"}
							    ]
						    });
							
							i++;
						});
						
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
	
	return padre + " - " + madre;
	
	if (padre!="" && madre!="")
		return padre + " - " + madre;
	else if (padre!="")
		return padre;
	else
		return madre;
}


$(function(){


	
	

	
	
});


