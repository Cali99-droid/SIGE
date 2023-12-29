//edicion completa de una tabla
function subtema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_full.html');
	subtema_full_modal(link);

}


function subtema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_curso_subtema-tabla').data('id_padre',link.data('id'));

		curso_subtema_listar_tabla();
		_inputs('col_subtema_full-frm');
		
		_llenarCombo('col_tema',$('#col_subtema_full-frm #id_tem'));
		
		$('#col_subtema_full-frm #btn-agregar').on('click',function(event){
			$('#col_subtema_full-frm #id').val('');
			_post($('#col_subtema_full-frm').attr('action') , $('#col_subtema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/subtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_subtema_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Subtema por Tema';
	else
		titulo = 'Nuevo  Subtema por Tema';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_subtema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_modal.html');
	curso_subtema_modal(link);
}

function curso_subtema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_modal.html');
	link.data('id_sub',$('#col_subtema_full-frm #id').val());
	curso_subtema_modal(link);
}

function curso_subtema_eliminar(link){
	_delete('api/cursoSubtema/' + $(link).data("id"),
			function(){
				curso_subtema_listar_tabla();
				}
			);
}
function curso_subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_subtema-frm');
		
		_llenarCombo('col_anio',$('#col_curso_subtema-frm #id_anio'));	
		_llenarCombo('cat_curso',$('#col_curso_subtema-frm #id_cur'));	
		_llenarCombo('col_subtema',$('#col_curso_subtema-frm #id_sub'),link.data('id_sub'));
		_llenarCombo('cat_grad',$('#col_curso_subtema-frm #id_gra'));	
		
		$('#col_curso_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_curso_subtema-frm #id').val('');
			_post($('#col_curso_subtema-frm').attr('action') , $('#col_curso_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoSubtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_curso_subtema-frm #btn-grabar').hide();
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_subtema_listar_tabla(){
	_get('api/cursoSubtema/listar/',
			function(data){
			console.log(data);
				$('#col_curso_subtema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Duraci&oacute;n", "data" : "dur"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Subtema", "data": "subtema.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_subtema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_subtema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Subtema', 'curso_subtema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_sub":$('#col_curso_subtema-tabla').data('id_padre')}
	);

}	
