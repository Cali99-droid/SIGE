//edicion completa de una tabla
function curso_horario_pad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_pad_full.html');
	curso_horario_pad_full_modal(link);

}


function curso_horario_pad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_curso_horario-tabla').data('id_padre',link.data('id'));

		curso_horario_listar_tabla();
		_inputs('col_curso_horario_pad_full-frm');
		
		$('#col_curso_horario_pad_full-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario_pad_full-frm #id').val('');
			_post($('#col_curso_horario_pad_full-frm').attr('action') , $('#col_curso_horario_pad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoHorarioPad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#col_curso_horario_pad-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_curso_horario_pad-frm #id_au'),data.id_au);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#col_curso_horario_pad-frm #id_anio'));
					_llenarCombo('col_aula',$('#col_curso_horario_pad-frm #id_au'));
				}
		$('#col_curso_horario_pad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_horario_pad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Horario Padre';
	else
		titulo = 'Nuevo  Curso Horario Padre';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_horario_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_modal.html');
	curso_horario_modal(link);
}

function curso_horario_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_modal.html');
	link.data('id_cchp',$('#col_curso_horario_pad_full-frm #id').val());
	curso_horario_modal(link);
}

function curso_horario_eliminar(link){
	_delete('api/cursoHorario/' + $(link).data("id"),
			function(){
				curso_horario_listar_tabla();
				}
			);
}
function curso_horario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_horario-frm');
		
		
		
		$('#col_curso_horario-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario-frm #id').val('');
			_post($('#col_curso_horario-frm').attr('action') , $('#col_curso_horario-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoHorario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_curso_horario_pad-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_curso_aula',$('#col_curso_horario_pad-frm #id_cca'),data.id_cca);	
						_llenarCombo('col_curso_horario_pad',$('#col_curso_horario_pad-frm #id_cchp'),data.id_cchp);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_curso_horario_pad-frm #id_anio'));
			_llenarCombo('col_curso_aula',$('#col_curso_horario_pad-frm #id_cca'));
			_llenarCombo('col_curso_horario_pad',$('#col_curso_horario_pad-frm #id_cchp'),$('#col_curso_horario_pad_full-frm #id').val());
			$('#col_curso_horario-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_horario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Horario';
	else
		titulo = 'Nuevo  Curso Horario';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_horario_listar_tabla(){
	_get('api/cursoHorario/listar/',
			function(data){
			console.log(data);
				$('#col_curso_horario-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Dia", "data" : "dia"}, 
							{"title":"Hora Inicio", "data" : "hora_ini"}, 
							{"title":"Hora Fin", "data" : "hora_fin"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Curso Aula", "data": "cursoAula.nro_ses"}, 
							{"title":"Curso Horario Padre", "data": "cursoHorarioPad.fec_ini_vig"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Horario', 'curso_horario_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cchp":$('#col_curso_horario-tabla').data('id_padre')}
	);

}	
