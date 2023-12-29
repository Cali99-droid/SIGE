//edicion completa de una tabla
function usuario_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_full.html');
	usuario_full_modal(link);

}


function usuario_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#seg_usuario_rol-tabla').data('id_padre',link.data('id'));
		$('#seg_usuario_sucursal-tabla').data('id_padre',link.data('id'));
		$('#ges_trabajador-tabla').data('id_padre',link.data('id'));

		usuario_rol_listar_tabla();
		usuario_sucursal_listar_tabla();
		trabajador_listar_tabla();
		_inputs('seg_usuario_full-frm');
		
		_llenarCombo('seg_perfil',$('#seg_usuario_full-frm #id_per'));
		_llenarCombo('ges_trabajador',$('#seg_usuario_full-frm #id_tra'));
		_llenarCombo('ges_sucursal',$('#seg_usuario_full-frm #id_suc'));
		
		$('#seg_usuario_full-frm #btn-agregar').on('click',function(event){
			$('#seg_usuario_full-frm #id').val('');
			_post($('#seg_usuario_full-frm').attr('action') , $('#seg_usuario_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/usuario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#seg_usuario_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		usuario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Usuario del sistema';
	else
		titulo = 'Nuevo  Usuario del sistema';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function usuario_rol_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_rol_modal.html');
	usuario_rol_modal(link);
}

function usuario_rol_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_rol_modal.html');
	link.data('id_usr',$('#seg_usuario_full-frm #id').val());
	usuario_rol_modal(link);
}

function usuario_rol_eliminar(link){
	_delete('api/usuarioRol/' + $(link).data("id"),
			function(){
				usuario_rol_listar_tabla();
				}
			);
}
function usuario_rol_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('seg_usuario_rol-frm');
		
		_llenarCombo('seg_usuario',$('#seg_usuario_rol-frm #id_usr'),link.data('id_usr'));
		_llenarCombo('seg_rol',$('#seg_usuario_rol-frm #id_rol'));	
		
		$('#seg_usuario_rol-frm #btn-agregar').on('click',function(event){
			$('#seg_usuario_rol-frm #id').val('');
			_post($('#seg_usuario_rol-frm').attr('action') , $('#seg_usuario_rol-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/usuarioRol/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#seg_usuario_rol-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		usuario_rol_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Rol Usuario ';
	else
		titulo = 'Nuevo  Rol Usuario ';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function usuario_rol_listar_tabla(){
	_get('api/usuarioRol/listar/',
			function(data){
			console.log(data);
				$('#seg_usuario_rol-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Usuario", "data": "usuario.login"}, 
							{"title":"Rol", "data": "rol.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_rol_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_rol_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Rol ', 'usuario_rol_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_usr":$('#seg_usuario_rol-tabla').data('id_padre')}
	);

}	

function usuario_sucursal_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_sucursal_modal.html');
	usuario_sucursal_modal(link);
}

function usuario_sucursal_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_sucursal_modal.html');
	link.data('id_usr',$('#seg_usuario_full-frm #id').val());
	usuario_sucursal_modal(link);
}

function usuario_sucursal_eliminar(link){
	_delete('api/usuarioSucursal/' + $(link).data("id"),
			function(){
				usuario_sucursal_listar_tabla();
				}
			);
}
function usuario_sucursal_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('seg_usuario_sucursal-frm');
		
		_llenarCombo('seg_usuario',$('#seg_usuario_sucursal-frm #id_usr'),link.data('id_usr'));
		_llenarCombo('ges_sucursal',$('#seg_usuario_sucursal-frm #id_suc'));	
		
		$('#seg_usuario_sucursal-frm #btn-agregar').on('click',function(event){
			$('#seg_usuario_sucursal-frm #id').val('');
			_post($('#seg_usuario_sucursal-frm').attr('action') , $('#seg_usuario_sucursal-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/usuarioSucursal/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#seg_usuario_sucursal-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		usuario_sucursal_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Usuario Sucursal';
	else
		titulo = 'Nuevo  Usuario Sucursal';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function usuario_sucursal_listar_tabla(){
	_get('api/usuarioSucursal/listar/',
			function(data){
			console.log(data);
				$('#seg_usuario_sucursal-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Usuario", "data": "usuario.login"}, 
							{"title":"Local", "data": "sucursal.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_sucursal_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_sucursal_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Usuario Sucursal', 'usuario_sucursal_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_usr":$('#seg_usuario_sucursal-tabla').data('id_padre')}
	);

}	

function trabajador_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/gestion/ges_trabajador_modal.html');
	trabajador_modal(link);
}

function trabajador_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/gestion/ges_trabajador_modal.html');
	link.data('id_usr',$('#seg_usuario_full-frm #id').val());
	trabajador_modal(link);
}

function trabajador_eliminar(link){
	_delete('api/trabajador/' + $(link).data("id"),
			function(){
				trabajador_listar_tabla();
				}
			);
}
function trabajador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('ges_trabajador-frm');
		
		_llenarCombo('cat_tipo_documento',$('#ges_trabajador-frm #id_tdc'));	
		_llenarCombo('cat_est_civil',$('#ges_trabajador-frm #id_eci'));	
		_llenarCombo('cat_grado_instruccion',$('#ges_trabajador-frm #id_gin'));	
		_llenarCombo('seg_usuario',$('#ges_trabajador-frm #id_usr'),link.data('id_usr'));
		
		$('#ges_trabajador-frm #btn-agregar').on('click',function(event){
			$('#ges_trabajador-frm #id').val('');
			_post($('#ges_trabajador-frm').attr('action') , $('#ges_trabajador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/trabajador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#ges_trabajador-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		trabajador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Trabajador de la empresa';
	else
		titulo = 'Nuevo  Trabajador de la empresa';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function trabajador_listar_tabla(){
	_get('api/trabajador/listar/',
			function(data){
			console.log(data);
				$('#ges_trabajador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nro de documento", "data" : "nro_doc"}, 
							{"title":"Apellido paterno", "data" : "ape_pat"}, 
							{"title":"Apellido materno", "data" : "ape_mat"}, 
							{"title":"Nombres", "data" : "nom"}, 
							{"title":"Fec. Nacimiento", "data" : "fec_nac"}, 
							{"title":"G&eacute;nero", "data" : "genero"}, 
							{"title":"Direcci&oacute;n", "data" : "dir"}, 
							{"title":"Tel&eacute;fono", "data" : "tel"}, 
							{"title":"Celular", "data" : "cel"}, 
							{"title":"Correo", "data" : "corr"}, 
							{"title":"Carrera", "data" : "carrera"}, 
							{"title":"Foto", "data" : "fot"}, 
							{"title":"Cant. hijos", "data" : "num_hij"}, 
							{"title":"Tipo de documento", "data": "tipoDocumento.nom"}, 
							{"title":"Estado civil", "data": "estCivil.nom"}, 
							{"title":"Grado de instrucci&oacute;n", "data": "gradoInstruccion.nom"}, 
							{"title":"Usuario", "data": "usuario.login"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="trabajador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="trabajador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Trabajador', 'trabajador_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_usr":$('#ges_trabajador-tabla').data('id_padre')}
	);

}	
