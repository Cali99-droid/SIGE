//edicion completa de una tabla
function unidad_sesion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_sesion_full.html');
	unidad_sesion_full_modal(link);

}


function unidad_sesion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_sesion_tema-tabla').data('id_padre',link.data('id'));
		$('#col_sesion_indicador-tabla').data('id_padre',link.data('id'));
		$('#col_sesion_actividad-tabla').data('id_padre',link.data('id'));

		sesion_tema_listar_tabla();
		sesion_indicador_listar_tabla();
		sesion_actividad_listar_tabla();
		_inputs('col_unidad_sesion_full-frm');
		
		_llenarCombo('col_curso_unidad',$('#col_unidad_sesion_full-frm #id_uni'));
		
		$('#col_unidad_sesion_full-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_sesion_full-frm #id').val('');
			_post($('#col_unidad_sesion_full-frm').attr('action') , $('#col_unidad_sesion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/unidadSesion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_unidad_sesion_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_sesion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Sesiones por unidad';
	else
		titulo = 'Nuevo  Sesiones por unidad';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_tema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_tema_modal.html');
	sesion_tema_modal(link);
}

function sesion_tema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_tema_modal.html');
	link.data('id_ses',$('#col_unidad_sesion_full-frm #id').val());
	sesion_tema_modal(link);
}

function sesion_tema_eliminar(link){
	_delete('api/sesionTema/' + $(link).data("id"),
			function(){
				sesion_tema_listar_tabla();
				}
			);
}
function sesion_tema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_sesion_tema-frm');
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_tema-frm #id_ses'),link.data('id_ses'));
		_llenarCombo('col_curso_subtema',$('#col_sesion_tema-frm #id_ccs'));	
		
		$('#col_sesion_tema-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_tema-frm #id').val('');
			_post($('#col_sesion_tema-frm').attr('action') , $('#col_sesion_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/sesionTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_sesion_tema-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Campo Tematico por Sesion';
	else
		titulo = 'Nuevo  Campo Tematico por Sesion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_tema_listar_tabla(){
	_get('api/sesionTema/listar/',
			function(data){
			console.log(data);
				$('#col_sesion_tema-tabla').dataTable({
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
							{"title":"Sesion", "data": "unidadSesion.tit"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_tema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_tema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Sesion Campo Temático', 'sesion_tema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ses":$('#col_sesion_tema-tabla').data('id_padre')}
	);

}	

function sesion_indicador_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_indicador_modal.html');
	sesion_indicador_modal(link);
}

function sesion_indicador_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_indicador_modal.html');
	link.data('id_ses',$('#col_unidad_sesion_full-frm #id').val());
	sesion_indicador_modal(link);
}

function sesion_indicador_eliminar(link){
	_delete('api/sesionIndicador/' + $(link).data("id"),
			function(){
				sesion_indicador_listar_tabla();
				}
			);
}
function sesion_indicador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_sesion_indicador-frm');
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_indicador-frm #id_ses'),link.data('id_ses'));
		_llenarCombo('col_indicador',$('#col_sesion_indicador-frm #id_ind'));	
		
		$('#col_sesion_indicador-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_indicador-frm #id').val('');
			_post($('#col_sesion_indicador-frm').attr('action') , $('#col_sesion_indicador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/sesionIndicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_sesion_indicador-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicadores que se trabajara en la sesion';
	else
		titulo = 'Nuevo  Indicadores que se trabajara en la sesion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_indicador_listar_tabla(){
	_get('api/sesionIndicador/listar/',
			function(data){
			console.log(data);
				$('#col_sesion_indicador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Sesion", "data": "unidadSesion.tit"}, 
							{"title":"Indicador", "data": "indicador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_indicador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_indicador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Sesion Indicador', 'sesion_indicador_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ses":$('#col_sesion_indicador-tabla').data('id_padre')}
	);

}	

function sesion_actividad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_actividad_modal.html');
	sesion_actividad_modal(link);
}

function sesion_actividad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_actividad_modal.html');
	link.data('id_ses',$('#col_unidad_sesion_full-frm #id').val());
	sesion_actividad_modal(link);
}

function sesion_actividad_eliminar(link){
	_delete('api/sesionActividad/' + $(link).data("id"),
			function(){
				sesion_actividad_listar_tabla();
				}
			);
}
function sesion_actividad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_sesion_actividad-frm');
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_actividad-frm #id_ses'),link.data('id_ses'));
		
		$('#col_sesion_actividad-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_actividad-frm #id').val('');
			_post($('#col_sesion_actividad-frm').attr('action') , $('#col_sesion_actividad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/sesionActividad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_sesion_actividad-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_actividad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Actividades por Sesion';
	else
		titulo = 'Nuevo  Actividades por Sesion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_actividad_listar_tabla(){
	_get('api/sesionActividad/listar/',
			function(data){
			console.log(data);
				$('#col_sesion_actividad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Actividad", "data" : "nom"}, 
							{"title":"Sesion", "data": "unidadSesion.tit"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_actividad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_actividad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Sesion Actividad', 'sesion_actividad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ses":$('#col_sesion_actividad-tabla').data('id_padre')}
	);

}	
