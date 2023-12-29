


function matricula_full_modal(id){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_aula_especial-tabla').data('id_padre',id);
		$('#mat_solicitud-tabla').data('id_padre',id);
		$('#fac_academico_pago-tabla').data('id_padre',id);
		$('#fac_movimiento-tabla').data('id_padre',id);
		$('#fac_alumno_descuento-tabla').data('id_padre',id);
		$('#mat_situacion_mat-tabla').data('id_padre',id);
		$('#not_curso_exoneracion-tabla').data('id_padre',id);
		$('#col_seguimiento_doc-tabla').data('id_padre',id);

		aula_especial_listar_tabla();
		solicitud_listar_tabla();
		academico_pago_listar_tabla();
		movimiento_listar_tabla();
		alumno_descuento_listar_tabla();
		situacion_mat_listar_tabla();
		curso_exoneracion_listar_tabla();
		seguimiento_doc_listar_tabla();
		_inputs('mat_matricula_full-frm');
		
		$('#mat_matricula_full-frm #btn-agregar').on('click',function(event){
			$('#mat_matricula_full-frm #id').val('');
			_post($('#mat_matricula_full-frm').attr('action') , $('#mat_matricula_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		 
		_get('api/matricula/' + id,
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('alu_alumno',$('#mat_matricula-frm #id_alu'),data.id_alu);	
						_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('cat_cond_matricula',$('#mat_matricula-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#mat_matricula-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#mat_matricula-frm #id_per'),data.id_per);	
						_llenarCombo('col_aula',$('#mat_matricula-frm #id_au'),data.id_au);	
						_llenarCombo('col_aula',$('#mat_matricula-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#mat_matricula-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#mat_matricula-frm #id_niv'),data.id_niv);	
						}
			);
 
		$('#mat_matricula_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		matricula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
 
		titulo = 'Editar Matricula del alumno';
 
	
	_modal_full(titulo, 'pages/matricula/mat_matricula_full.html',onShowModal,onSuccessSave);

}


function aula_especial_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_aula_especial_modal.html');
	aula_especial_modal(link);
}

function aula_especial_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_especial_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	aula_especial_modal(link);
}

function aula_especial_eliminar(link){
	_delete('api/aulaEspecial/' + $(link).data("id"),
			function(){
				aula_especial_listar_tabla();
				}
			);
}
function aula_especial_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_aula_especial-frm');
		
		
		
		$('#col_aula_especial-frm #btn-agregar').on('click',function(event){
			$('#col_aula_especial-frm #id').val('');
			_post($('#col_aula_especial-frm').attr('action') , $('#col_aula_especial-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/aulaEspecial/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_grad',$('#mat_matricula-frm #id_gra'),data.id_gra);	
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('cat_grad',$('#mat_matricula-frm #id_gra'));
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			$('#col_aula_especial-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		aula_especial_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Alumno por sal&oacute;n especial';
	else
		titulo = 'Nuevo  Alumno por sal&oacute;n especial';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function aula_especial_listar_tabla(){
	_get('api/aulaEspecial/listar/',
			function(data){
			console.log(data);
				$('#col_aula_especial-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="aula_especial_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="aula_especial_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Alumno por sal�n especial', 'aula_especial_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#col_aula_especial-tabla').data('id_padre')}
	);

}	

function solicitud_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_solicitud_modal.html');
	solicitud_modal(link);
}

function solicitud_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_solicitud_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	solicitud_modal(link);
}

function solicitud_eliminar(link){
	_delete('api/solicitud/' + $(link).data("id"),
			function(){
				solicitud_listar_tabla();
				}
			);
}
function solicitud_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_solicitud-frm');
		
		
		
		$('#mat_solicitud-frm #btn-agregar').on('click',function(event){
			$('#mat_solicitud-frm #id').val('');
			_post($('#mat_solicitud-frm').attr('action') , $('#mat_solicitud-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/solicitud/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
						_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'),data.id_suc);	
						_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'),data.id_suc);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'));
			_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'));
			_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'));
			$('#mat_solicitud-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		solicitud_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Matr&iacute;cula Solicitud';
	else
		titulo = 'Nuevo  Matr&iacute;cula Solicitud';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function solicitud_listar_tabla(){
	_get('api/solicitud/listar/',
			function(data){
			console.log(data);
				$('#mat_solicitud-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tipo", "data" : "tipo"}, 
							{"title":"Numero Expediente", "data" : "nro_exp"}, 
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Apoderado", "data": "familiar.nom"}, 
							{"title":"Local Origen", "data": "sucursal.nom"}, 
							{"title":"Local Destino", "data": "sucursal.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="solicitud_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="solicitud_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Matr�cula Solicitud', 'solicitud_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#mat_solicitud-tabla').data('id_padre')}
	);

}	

function academico_pago_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_academico_pago_modal.html');
	academico_pago_modal(link);
}

function academico_pago_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_academico_pago_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	academico_pago_modal(link);
}

function academico_pago_eliminar(link){
	_delete('api/academicoPago/' + $(link).data("id"),
			function(){
				academico_pago_listar_tabla();
				}
			);
}
function academico_pago_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('fac_academico_pago-frm');
		
		
		
		$('#fac_academico_pago-frm #btn-agregar').on('click',function(event){
			$('#fac_academico_pago-frm #id').val('');
			_post($('#fac_academico_pago-frm').attr('action') , $('#fac_academico_pago-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/academicoPago/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			$('#fac_academico_pago-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		academico_pago_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Facturacion Pago';
	else
		titulo = 'Nuevo  Facturacion Pago';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function academico_pago_listar_tabla(){
	_get('api/academicoPago/listar/',
			function(data){
			console.log(data);
				$('#fac_academico_pago-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tipo de Pago", "data" : "tip"}, 
							{"title":"Mes", "data" : "mens"}, 
							{"title":"Monto", "data" : "monto"}, 
							{"title":"Cancelado", "data" : "canc"}, 
							{"title":"N&uacute;mero Recibo", "data" : "nro_rec"}, 
							{"title":"N&uacute;mero de operaci&oacute;n", "data" : "nro_pe"}, 
							{"title":"Banco", "data" : "banco"}, 
							{"title":"Fecha Pago", "data" : "fec_pago"}, 
							{"title":"Descuento por hermano", "data" : "desc_hermano"}, 
							{"title":"Descuento pronto pago", "data" : "desc_pronto_pago"}, 
							{"title":"Descuento pago adelantando", "data" : "desc_pago_adelantado"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="academico_pago_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="academico_pago_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Facturacion Pago', 'academico_pago_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#fac_academico_pago-tabla').data('id_padre')}
	);

}	

function movimiento_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_movimiento_modal.html');
	movimiento_modal(link);
}

function movimiento_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_movimiento_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	movimiento_modal(link);
}

function movimiento_eliminar(link){
	_delete('api/movimiento/' + $(link).data("id"),
			function(){
				movimiento_listar_tabla();
				}
			);
}
function movimiento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('fac_movimiento-frm');
		
		
		
		$('#fac_movimiento-frm #btn-agregar').on('click',function(event){
			$('#fac_movimiento-frm #id').val('');
			_post($('#fac_movimiento-frm').attr('action') , $('#fac_movimiento-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/movimiento/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'),data.id_suc);	
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('ges_sucursal',$('#mat_matricula-frm #id_suc'));
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			$('#fac_movimiento-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		movimiento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Entradas y salidas';
	else
		titulo = 'Nuevo  Entradas y salidas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function movimiento_listar_tabla(){
	_get('api/movimiento/listar/',
			function(data){
			console.log(data);
				$('#fac_movimiento-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tipo", "data" : "tipo"}, 
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Monto", "data" : "monto"}, 
							{"title":"Descuento", "data" : "descuento"}, 
							{"title":"Monto total", "data" : "monto_total"}, 
							{"title":"N&uacute;mero Recibo", "data" : "nro_rec"}, 
							{"title":"Observacion", "data" : "obs"}, 
							{"title":"Local", "data": "sucursal.nom"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="movimiento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="movimiento_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Movimiento de caja', 'movimiento_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#fac_movimiento-tabla').data('id_padre')}
	);

}	

function alumno_descuento_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_alumno_descuento_modal.html');
	alumno_descuento_modal(link);
}

function alumno_descuento_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_alumno_descuento_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	alumno_descuento_modal(link);
}

function alumno_descuento_eliminar(link){
	_delete('api/alumnoDescuento/' + $(link).data("id"),
			function(){
				alumno_descuento_listar_tabla();
				}
			);
}
function alumno_descuento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('fac_alumno_descuento-frm');
		
		
		
		$('#fac_alumno_descuento-frm #btn-agregar').on('click',function(event){
			$('#fac_alumno_descuento-frm #id').val('');
			_post($('#fac_alumno_descuento-frm').attr('action') , $('#fac_alumno_descuento-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/alumnoDescuento/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			$('#fac_alumno_descuento-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_descuento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Descuento por alumno';
	else
		titulo = 'Nuevo  Descuento por alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function alumno_descuento_listar_tabla(){
	_get('api/alumnoDescuento/listar/',
			function(data){
			console.log(data);
				$('#fac_alumno_descuento-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Descuento", "data" : "descuento"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alumno_descuento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alumno_descuento_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Descuento por alumno', 'alumno_descuento_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#fac_alumno_descuento-tabla').data('id_padre')}
	);

}	

function situacion_mat_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_situacion_mat_modal.html');
	situacion_mat_modal(link);
}

function situacion_mat_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_situacion_mat_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	situacion_mat_modal(link);
}

function situacion_mat_eliminar(link){
	_delete('api/situacionMat/' + $(link).data("id"),
			function(){
				situacion_mat_listar_tabla();
				}
			);
}
function situacion_mat_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_situacion_mat-frm');
		
		
		
		$('#mat_situacion_mat-frm #btn-agregar').on('click',function(event){
			$('#mat_situacion_mat-frm #id').val('');
			_post($('#mat_situacion_mat-frm').attr('action') , $('#mat_situacion_mat-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/situacionMat/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_col_situacion',$('#mat_matricula-frm #id_cma'),data.id_cma);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			_llenarCombo('cat_col_situacion',$('#mat_matricula-frm #id_cma'));
			$('#mat_situacion_mat-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		situacion_mat_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Situaci&oacute;n Matr&iacute;cula';
	else
		titulo = 'Nuevo  Situaci&oacute;n Matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function situacion_mat_listar_tabla(){
	_get('api/situacionMat/listar/',
			function(data){
			console.log(data);
				$('#mat_situacion_mat-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Motivo", "data" : "mot"}, 
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Situaci&oacute;n", "data": "colSituacion.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="situacion_mat_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="situacion_mat_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Situadci�n Matr�cula', 'situacion_mat_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#mat_situacion_mat-tabla').data('id_padre')}
	);

}	

function curso_exoneracion_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_curso_exoneracion_modal.html');
	curso_exoneracion_modal(link);
}

function curso_exoneracion_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_curso_exoneracion_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	curso_exoneracion_modal(link);
}

function curso_exoneracion_eliminar(link){
	_delete('api/cursoExoneracion/' + $(link).data("id"),
			function(){
				curso_exoneracion_listar_tabla();
				}
			);
}
function curso_exoneracion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_curso_exoneracion-frm');
		
		
		
		$('#not_curso_exoneracion-frm #btn-agregar').on('click',function(event){
			$('#not_curso_exoneracion-frm #id').val('');
			_post($('#not_curso_exoneracion-frm').attr('action') , $('#not_curso_exoneracion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/cursoExoneracion/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_curso',$('#mat_matricula-frm #id_cur'),data.id_cur);	
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('cat_curso',$('#mat_matricula-frm #id_cur'));
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			$('#not_curso_exoneracion-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_exoneracion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Exoneraci&oacute;n de cursos';
	else
		titulo = 'Nuevo  Exoneraci&oacute;n de cursos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_exoneracion_listar_tabla(){
	_get('api/cursoExoneracion/listar/',
			function(data){
			console.log(data);
				$('#not_curso_exoneracion-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha", "data" : "fecha"}, 
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Resoluci&oacute;n", "data" : "resol"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_exoneracion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_exoneracion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Exoneraci�n de cursos', 'curso_exoneracion_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#not_curso_exoneracion-tabla').data('id_padre')}
	);

}	

function seguimiento_doc_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_seguimiento_doc_modal.html');
	seguimiento_doc_modal(link);
}

function seguimiento_doc_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_seguimiento_doc_modal.html');
	link.data('id_mat',$('#mat_matricula_full-frm #id').val());
	seguimiento_doc_modal(link);
}

function seguimiento_doc_eliminar(link){
	_delete('api/seguimientoDoc/' + $(link).data("id"),
			function(){
				seguimiento_doc_listar_tabla();
				}
			);
}
function seguimiento_doc_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_seguimiento_doc-frm');
		
		
		
		$('#col_seguimiento_doc-frm #btn-agregar').on('click',function(event){
			$('#col_seguimiento_doc-frm #id').val('');
			_post($('#col_seguimiento_doc-frm').attr('action') , $('#col_seguimiento_doc-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (id){
			_get('api/seguimientoDoc/' + id,
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),data.id_mat);	
						_llenarCombo('col_per_uni',$('#mat_matricula-frm #id_cpu'),data.id_cpu);	
				}
			);
		}else{
			_llenarCombo('alu_familiar',$('#mat_matricula-frm #id_fam'));
			_llenarCombo('mat_matricula',$('#mat_matricula-frm #id_mat'),$('#mat_matricula_full-frm #id').val());
			_llenarCombo('col_per_uni',$('#mat_matricula-frm #id_cpu'));
			$('#col_seguimiento_doc-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		seguimiento_doc_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (id)
		titulo = 'Editar Seguimiento Documento';
	else
		titulo = 'Nuevo  Seguimiento Documento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function seguimiento_doc_listar_tabla(){
	_get('api/seguimientoDoc/listar/',
			function(data){
			console.log(data);
				$('#col_seguimiento_doc-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tipo", "data" : "tip"}, 
							{"title":"Familiar", "data": "familiar.nom"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Periodo Academico", "data": "perUni.nump"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="seguimiento_doc_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="seguimiento_doc_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Seguimiento Documento', 'seguimiento_doc_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat":$('#col_seguimiento_doc-tabla').data('id_padre')}
	);

}	
