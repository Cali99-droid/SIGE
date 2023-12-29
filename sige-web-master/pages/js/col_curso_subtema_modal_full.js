//edicion completa de una tabla
function curso_subtema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_full.html');
	curso_subtema_full_modal(link);

}


function curso_subtema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_unidad_tema-tabla').data('id_padre',link.data('id'));
		$('#col_sesion_tema-tabla').data('id_padre',link.data('id'));

		unidad_tema_listar_tabla();
		sesion_tema_listar_tabla();
		_inputs('col_curso_subtema_full-frm');
		
		_llenarCombo('col_anio',$('#col_curso_subtema_full-frm #id_anio'));
		_llenarCombo('cat_nivel',$('#col_curso_subtema_full-frm #id_niv'));
		_llenarCombo('cat_grad',$('#col_curso_subtema_full-frm #id_gra'));
		_llenarCombo('cat_curso',$('#col_curso_subtema_full-frm #id_cur'));
		_llenarCombo('col_subtema',$('#col_curso_subtema_full-frm #id_sub'));
		
		$('#col_curso_subtema_full-frm #btn-agregar').on('click',function(event){
			$('#col_curso_subtema_full-frm #id').val('');
			_post($('#col_curso_subtema_full-frm').attr('action') , $('#col_curso_subtema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoSubtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_curso_subtema_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Subtema';
	else
		titulo = 'Nuevo  Curso Subtema';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function unidad_tema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_tema_modal.html');
	unidad_tema_modal(link);
}

function unidad_tema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_tema_modal.html');
	link.data('id_ccs',$('#col_curso_subtema_full-frm #id').val());
	unidad_tema_modal(link);
}

function unidad_tema_eliminar(link){
	_delete('api/unidadTema/' + $(link).data("id"),
			function(){
				unidad_tema_listar_tabla();
				}
			);
}
function unidad_tema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_unidad_tema-frm');
		
		_llenarCombo('col_curso_unidad',$('#col_unidad_tema-frm #id_uni'));	
		_llenarCombo('col_curso_subtema',$('#col_unidad_tema-frm #id_ccs'),link.data('id_ccs'));
		
		$('#col_unidad_tema-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_tema-frm #id').val('');
			_post($('#col_unidad_tema-frm').attr('action') , $('#col_unidad_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/unidadTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_unidad_tema-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La unidad tiene campo tem&aacute;tico exclusivos';
	else
		titulo = 'Nuevo  La unidad tiene campo tem&aacute;tico exclusivos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function unidad_tema_listar_tabla(){
	_get('api/unidadTema/listar/',
			function(data){
			console.log(data);
				$('#col_unidad_tema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Unidad", "data": "cursoUnidad.nom"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_tema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_tema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Campo Temático', 'unidad_tema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ccs":$('#col_unidad_tema-tabla').data('id_padre')}
	);

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
	link.data('id_ccs',$('#col_curso_subtema_full-frm #id').val());
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
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_tema-frm #id_ses'));	
		_llenarCombo('col_curso_subtema',$('#col_sesion_tema-frm #id_ccs'),link.data('id_ccs'));
		
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
			},{"id_ccs":$('#col_sesion_tema-tabla').data('id_padre')}
	);

}	
