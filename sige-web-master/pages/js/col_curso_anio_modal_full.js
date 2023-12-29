//edicion completa de una tabla
function curso_anio_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_anio_full.html');
	curso_anio_full_modal(link);

}


function curso_anio_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_curso_aula-tabla').data('id_padre',link.data('id'));
		$('#col_competencia-tabla').data('id_padre',link.data('id'));
		$('#col_curso_unidad-tabla').data('id_padre',link.data('id'));

		curso_aula_listar_tabla();
		competencia_listar_tabla();
		curso_unidad_listar_tabla();
		_inputs('col_curso_anio_full-frm');
		
		_llenarCombo('per_periodo',$('#col_curso_anio_full-frm #id_per'));
		_llenarCombo('cat_grad',$('#col_curso_anio_full-frm #id_gra'));
		_llenarCombo('col_area_anio',$('#col_curso_anio_full-frm #id_caa'));
		_llenarCombo('cat_curso',$('#col_curso_anio_full-frm #id_cur'));
		
		$('#col_curso_anio_full-frm #btn-agregar').on('click',function(event){
			$('#col_curso_anio_full-frm #id').val('');
			_post($('#col_curso_anio_full-frm').attr('action') , $('#col_curso_anio_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_curso_anio_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso';
	else
		titulo = 'Nuevo  Curso';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_aula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_modal.html');
	curso_aula_modal(link);
}

function curso_aula_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_modal.html');
	link.data('id_cua',$('#col_curso_anio_full-frm #id').val());
	curso_aula_modal(link);
}

function curso_aula_eliminar(link){
	_delete('api/cursoAula/' + $(link).data("id"),
			function(){
				curso_aula_listar_tabla();
				}
			);
}
function curso_aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_aula-frm');
		
		_llenarCombo('col_curso_anio',$('#col_curso_aula-frm #id_cua'),link.data('id_cua'));
		_llenarCombo('col_aula',$('#col_curso_aula-frm #id_aula'));	
		_llenarCombo('ges_trabajador',$('#col_curso_aula-frm #id_tra'));	
		
		$('#col_curso_aula-frm #btn-agregar').on('click',function(event){
			$('#col_curso_aula-frm #id').val('');
			_post($('#col_curso_aula-frm').attr('action') , $('#col_curso_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoAula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_curso_aula-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Aula';
	else
		titulo = 'Nuevo  Curso Aula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_aula_listar_tabla(){
	_get('api/cursoAula/listar/',
			function(data){
			console.log(data);
				$('#col_curso_aula-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Curso Anio", "data": "cursoAnio.peso"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Docente", "data": "trabajador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_aula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_aula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Aula', 'curso_aula_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cua":$('#col_curso_aula-tabla').data('id_padre')}
	);

}	

function competencia_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_competencia_modal.html');
	competencia_modal(link);
}

function competencia_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_competencia_modal.html');
	link.data('id_cua',$('#col_curso_anio_full-frm #id').val());
	competencia_modal(link);
}

function competencia_eliminar(link){
	_delete('api/competencia/' + $(link).data("id"),
			function(){
				competencia_listar_tabla();
				}
			);
}
function competencia_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_competencia-frm');
		
		_llenarCombo('col_curso_anio',$('#col_competencia-frm #id_cua'),link.data('id_cua'));
		
		$('#col_competencia-frm #btn-agregar').on('click',function(event){
			$('#col_competencia-frm #id').val('');
			_post($('#col_competencia-frm').attr('action') , $('#col_competencia-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/competencia/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_competencia-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		competencia_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Competencia';
	else
		titulo = 'Nuevo  Competencia';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function competencia_listar_tabla(){
	_get('api/competencia/listar/',
			function(data){
			console.log(data);
				$('#col_competencia-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Competencia", "data" : "nom"}, 
							{"title":"Peso", "data" : "peso"}, 
							{"title":"Curso", "data": "cursoAnio.peso"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="competencia_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="competencia_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Competencia', 'competencia_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cua":$('#col_competencia-tabla').data('id_padre')}
	);

}	

function curso_unidad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_modal.html');
	curso_unidad_modal(link);
}

function curso_unidad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_modal.html');
	link.data('id_cua',$('#col_curso_anio_full-frm #id').val());
	curso_unidad_modal(link);
}

function curso_unidad_eliminar(link){
	_delete('api/cursoUnidad/' + $(link).data("id"),
			function(){
				curso_unidad_listar_tabla();
				}
			);
}
function curso_unidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_unidad-frm');
		
		_llenarCombo('col_curso_anio',$('#col_curso_unidad-frm #id_cua'),link.data('id_cua'));
		
		$('#col_curso_unidad-frm #btn-agregar').on('click',function(event){
			$('#col_curso_unidad-frm #id').val('');
			_post($('#col_curso_unidad-frm').attr('action') , $('#col_curso_unidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoUnidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_curso_unidad-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_unidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Did&aacute;ctica';
	else
		titulo = 'Nuevo  Unidad Did&aacute;ctica';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_unidad_listar_tabla(){
	_get('api/cursoUnidad/listar/',
			function(data){
			console.log(data);
				$('#col_curso_unidad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"N&uacute;mero de Unidad", "data" : "num"}, 
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Descripcion", "data" : "des"}, 
							{"title":"Sesion", "data" : "nro_ses"}, 
							{"title":"Semanas", "data" : "nro_sem"}, 
							{"title":"Producto", "data" : "producto"}, 
							{"title":"Curso", "data": "cursoAnio.peso"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Unidad', 'curso_unidad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cua":$('#col_curso_unidad-tabla').data('id_padre')}
	);

}	
