//edicion completa de una tabla
function cond_alumno_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_cond_alumno_full.html');
	cond_alumno_full_modal(link);

}


function cond_alumno_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#mat_condicion-tabla').data('id_padre',link.data('id'));

		condicion_listar_tabla();
		_inputs('cat_cond_alumno_full-frm');
		
		$('#cat_cond_alumno_full-frm #btn-agregar').on('click',function(event){
			$('#cat_cond_alumno_full-frm #id').val('');
			_post($('#cat_cond_alumno_full-frm').attr('action') , $('#cat_cond_alumno_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/condAlumno/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_tip_cond',$('#cat_cond_alumno-frm #id_ctc'),data.id_ctc);	
						}
			);
		}else{
					_llenarCombo('cat_tip_cond',$('#cat_cond_alumno-frm #id_ctc'));
				}
		$('#cat_cond_alumno_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cond_alumno_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Condici&oacute;n del alumno';
	else
		titulo = 'Nuevo  Condici&oacute;n del alumno';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function condicion_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_condicion_modal.html');
	condicion_modal(link);
}

function condicion_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_condicion_modal.html');
	link.data('id_cond',$('#cat_cond_alumno_full-frm #id').val());
	condicion_modal(link);
}

function condicion_eliminar(link){
	_delete('api/condicion/' + $(link).data("id"),
			function(){
				condicion_listar_tabla();
				}
			);
}
function condicion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_condicion-frm');
		
		
		
		$('#mat_condicion-frm #btn-agregar').on('click',function(event){
			$('#mat_condicion-frm #id').val('');
			_post($('#mat_condicion-frm').attr('action') , $('#mat_condicion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/condicion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_cond_alumno',$('#cat_cond_alumno-frm #id_cond'),data.id_cond);	
						_llenarCombo('mat_matricula',$('#cat_cond_alumno-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('cat_cond_alumno',$('#cat_cond_alumno-frm #id_cond'),$('#cat_cond_alumno_full-frm #id').val());
			_llenarCombo('mat_matricula',$('#cat_cond_alumno-frm #id_mat'));
			$('#mat_condicion-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		condicion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Matr&iacute;cula Condici&oacute;n Alumno';
	else
		titulo = 'Nuevo  Matr&iacute;cula Condici&oacute;n Alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function condicion_listar_tabla(){
	_get('api/condicion/listar/',
			function(data){
			console.log(data);
				$('#mat_condicion-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Condici&oacute;n", "data": "condAlumno.nom"}, 
							{"title":"Matr&iacute;cula Alumno", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="condicion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="condicion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Matrícula Condición Alumno', 'condicion_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cond":$('#mat_condicion-tabla').data('id_padre')}
	);

}	
