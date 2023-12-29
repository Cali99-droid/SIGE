//edicion completa de una tabla
function curso_exoneracion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_curso_exoneracion_full.html');
	curso_exoneracion_full_modal(link);

}


function curso_exoneracion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('not_curso_exoneracion_full-frm');
		
		$('#not_curso_exoneracion_full-frm #btn-agregar').on('click',function(event){
			$('#not_curso_exoneracion_full-frm #id').val('');
			_post($('#not_curso_exoneracion_full-frm').attr('action') , $('#not_curso_exoneracion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoExoneracion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_curso',$('#not_curso_exoneracion-frm #id_cur'),data.id_cur);	
						_llenarCombo('mat_matricula',$('#not_curso_exoneracion-frm #id_mat'),data.id_mat);	
						}
			);
		}else{
					_llenarCombo('cat_curso',$('#not_curso_exoneracion-frm #id_cur'));
					_llenarCombo('mat_matricula',$('#not_curso_exoneracion-frm #id_mat'));
				}
		$('#not_curso_exoneracion_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_exoneracion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Exoneraci&oacute;n de cursos';
	else
		titulo = 'Nuevo  Exoneraci&oacute;n de cursos';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

