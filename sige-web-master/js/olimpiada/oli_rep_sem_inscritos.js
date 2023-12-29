//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	//if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		//tabla : 'oli_config',
			url : 'api/seminario/listar',
			params: param,
		combo : $('#id_sem'),
		//context : _URL_OLI,
		funcion : function() {
			$('#id_sem').change();
		}
		});
	//} /*else{
	/*	var param={id_anio:$("#_id_anio").text(),id_usr:_usuario.id};

		_llenar_combo({
			url : 'api/config/listarOliUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}*/
	

	/*_llenar_combo({
		tabla : 'cat_tipo_inscripcion',
		combo : $('#id_ti'),
		context : _URL_OLI
	});

	_llenar_combo({
		tabla : 'cat_gestion',
		combo : $('#id_cg'),
		context : _URL_OLI
	});*/

	$('#id_sem').on('change', function() {
		var param={id_sem:$("#id_sem").val()};
		if($(this).val()==''){
			$(this).find('#id_niv').not(':first').remove();
			$(this).find('#id_gra').not(':first').remove();
		}else{
			_llenar_combo({
				url : 'api/seminario/listarGrupos',
				combo : $('#id_gru'),
				params: param,
				//context : _URL_OLI,
				funcion : function() {
					//$('#id_sem').change();
				}
			});
		}	
	});

	/*$('#id_niv').on('change',function() {
			if($(this).val()=='' ||  $('#id_oli').val()=='')
				$(this).find('#id_gra').not(':first').remove();
			else
				_llenar_combo({
					url : 'api/config/listarGrados/' + $('#id_oli').val()+ '/' + $(this).val(),
					combo : $('#id_gra'),
					context : _URL_OLI
				});
			});*/


	var validator = $('#frm-reporte').validate();
	
	$('#frm-reporte').on('submit', function(event){
		event.preventDefault();
		var param={id_sem:$("#id_sem").val(), id_gru:$("#id_gru").val()};
		//alert();
		if ($(this).valid())
		_GET({url:'api/seminario/listarInscritosxSeminario',
			  params: param,
			 // context : _URL_OLI,
			  success:function(data){
				console.log(data);  
				  var columns = [ 
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title":"DNI", "data" : "nro_dni"}, 
						{"title": "Alumno", "render":function ( data, type, row,meta ) {
								return row.ape_pat+" "+row.ape_mat+" "+row.nom;
						}},
						{"title":"Correo", "data" : "corr"}, 
						//{"title":"Alumno", "data": "alumno"}, 
						//{"title":"Nivel", "data": "nivel"},
						//{"title":"Grado", "data": "grado"},
						//{"title":"Colegio", "data": "colegio"},
						//{"title":"Modalidad", "data": "modalidad"},
						//{"title":"Gestion", "data": "gestion"},
						//{"title":"Pago", "data": "pago"},
						//{"title":"Monto", "data": "monto","render":function ( data, type, row,meta ) { return "S/"+_formatMonto(data);} },
						//{"title":"Distrito", "data": "distrito"},
						{"title": "Alumno", "render":function ( data, type, row,meta ) {
								return row.distrito.nom;
						}},
						{"title": "Grupo", "render":function ( data, type, row,meta ) {
								return "Grupo "+row.semGrupo.nro;
						}},
						//{"title":"Correo", "data": "corr"},
						/*{"title": "Editar", "render":function ( data, type, row,meta ) {
							return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="inscripcion_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>';								
						}},*/
						{"title": "Re-enviar", "render":function ( data, type, row,meta ) {
							if(row.corr==null || row.corr=='')
								return "";
							else
								return "<a href=\"javascript:reenviar('" + row.id + "')\"><i class='fa fa-envelope-o'></i></a>" 
						}},
						{"title": "Carnet", "render":function ( data, type, row,meta ) {
							//if(row.corr==null || row.corr=='')
							//	return "";
							//else
								return "<a href=\"javascript:descargarCarnet(" + row.id + ",'" + row.nro_dni + "')\"><i class='icon-file-pdf ui-blue'></i></a>" 
						}}

			    ];
				  
				  if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_ADMIN_OLI)>-1){
					columns.push({"title": "Editar", "render":function ( data, type, row,meta ) {
							return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="inscripcion_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>';
					}}),
					  columns.push({"title": "Eliminar", "render":function ( data, type, row,meta ) {
							if(row.pago=='NO')
								return "<a href=\"javascript:eliminarInscr(" + row.id + ")\"><i class='fa fa-trash-o'></i></a>";
							else
								return "";
						}})						
				  }
					  
				  
				  $('#panel-inscritos').show();
				  $('#tabla-reporte').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 scrollX: true,              
				         columns :columns,
					    "initComplete": function( settings ) {
							
				        	$("<span><a href='#' onclick='generarExcel(event)'> <i class='fa fa-file-excel-o'></i>Generar excel para lectora</a>&nbsp;</span>").insertBefore($("#_paginator"));
				        	 _dataTable_total(settings,'monto');
						 }
				    });
			  }
			});
	});
}

function inscripcion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/olimpiada/oli_inscripcion_modal.html');
	inscripcion_modal(link);
	
}

function inscripcion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_tema-frm');
		
		$('#oli_inscripcion-frm #btn-editar').on('click',function(event){
			/*_post($('#oli_inscripcion-frm').attr('action') , $('#oli_inscripcion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);*/
			
			_POST({form:$('#oli_inscripcion-frm'),
				context:_URL_OLI,
				success: function(data){
					console.log(data);
					$("#btn_buscar").click();
					//$("#id_ent_res").val(data.result);	
				}
			});	
		});
		
		if (link.data('id')){
			_GET({url:'/api/public/inscripcionInd/obtenerDatosImportantes/' + link.data('id'),
				context:_URL_OLI,
				success:function(data){
					console.log(data);
					_fillForm(data,$('#modal').find('form') );
					$('#id_nvl').on('change',function(event){
						//var arr_param = [{id_dep:$('#id_dep').val()},{est:'A'}];
						_llenar_combo({
							url:'api/public/grado/listarGrados/'+data.id_oli+'/'+$('#id_nvl').val(),
							combo:$('#oli_inscripcion-frm #id_og'),
							text:'grado',
							context: _URL_OLI,
							id: data.id_og,
							funcion:function(){
							}
						});	
					});
												
					_llenar_combo({
					   	url:'api/public/grado/listarNiveles/'+ data.id_oli,
						combo:$('#id_nvl'),
						context:_URL_OLI,
						id: data.id_nvl,
						text:'nivel',
						funcion:function(){
							$('#id_nvl').change();
							if(data.id_od!=null){
								$('#id_nvl').attr("disabled","disabled");
								$("#correo").hide();
								$("#colegio").hide();
							} else{
								$('#id_nvl').removeAttr("readonly");
							}
						}
					});
					
					$('#id_dep').on('change',function(event){
						 console.log($(this).data('id_pro'));
						_llenar_combo({
							url:'api/comboCache/provincias/' + $(this).val(),
							combo:$('#id_pro'),
						   	id:$(this).data('id_pro'),
						   	funcion:function(){
								$('#id_pro').change();
							}
						});


					});


					$('#id_pro').on('change',function(event){

						 	if ($(this).val()==''){ 
						 		$('#id_dist').find('option').not(':first').remove();
						 	}
						 	else{
								_llenar_combo({
									url:'api/comboCache/distritos/' + $(this).val(),
									combo:$('#id_dist'),
								   	id:$(this).data('id_dis'),
								   	funcion:function(){
								   		console.log('remove data');
								   		$('#id_dep').removeData('id_pro');//eliminar campos auxiliares
								   		$('#id_pro').removeData('id_dis');//eliminar campos auxiliares
								   	}
								});		 		
						 	}
					});	 
					
					seleccionarUbigeo(data.id_dep, data.id_pro, data.id_dis);
				}

			});	
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Datos de la Inscripción';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}



function seleccionarUbigeo(id_dep, id_pro, id_dis){
	$('#id_dep').data('id_pro',id_pro);
	$('#id_pro').data('id_dis',id_dis);
	
	if($('#id_dep option').length>1){
		$('#id_dep').val(id_dep);
		$('#id_dep').change();
	}else{
		_llenar_combo({
			tabla:'cat_departamento',
			combo:$('#id_dep'),
			context: _URL,
		   	id:id_dep,
		   	funcion:function(){
				$('#id_dep').change();
			}
		});	

	}
		
}

//REENVIAR CORREO
function reenviar(id){
	_POST({url:'/api/public/inscripcionInd/reenviar/' + id, context:_URL_OLI});
}
function generarExcel(e){
	_download(_URL_OLI + 'api/resultados/generarExcelLectora?id_oli=' + $('#id_oli').val()  , "lectora.xls");	
}

function descargarCarnet(id,dni){
	_download(_URL_OLI+'api/public/inscripcionInd/imprimirCarnet?tipo=I&id_ins='+id,dni + '_olimpiada.pdf');	
}

function eliminarInscr(id){
	_DELETE({
				url:'api/reporte/' + id,
				context : _URL_OLI,
				success : function (data){
					$('#frm-reporte').submit();
				}
			});
}