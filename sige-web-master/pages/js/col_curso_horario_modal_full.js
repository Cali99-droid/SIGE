//edicion completa de una tabla
function curso_horario_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_full.html');
	curso_horario_full_modal(link);

}


function curso_horario_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_curso_horario_full-frm');
		
		$('#col_curso_horario_full-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario_full-frm #id').val('');
			_post($('#col_curso_horario_full-frm').attr('action') , $('#col_curso_horario_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoHorario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#col_curso_horario-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_curso_aula',$('#col_curso_horario-frm #id_cca'),data.id_cca);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#col_curso_horario-frm #id_anio'));
					_llenarCombo('col_curso_aula',$('#col_curso_horario-frm #id_cca'));
				}
		$('#col_curso_horario_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

