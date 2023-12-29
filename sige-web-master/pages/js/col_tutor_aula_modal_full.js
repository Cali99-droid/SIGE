//edicion completa de una tabla
function tutor_aula_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_tutor_aula_full.html');
	tutor_aula_full_modal(link);

}


function tutor_aula_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_tutor_aula_full-frm');
		
		_llenarCombo('col_aula',$('#col_tutor_aula_full-frm #id_aula'));
		_llenarCombo('ges_trabajador',$('#col_tutor_aula_full-frm #id_tra'));
		
		$('#col_tutor_aula_full-frm #btn-agregar').on('click',function(event){
			$('#col_tutor_aula_full-frm #id').val('');
			_post($('#col_tutor_aula_full-frm').attr('action') , $('#col_tutor_aula_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tutorAula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_tutor_aula_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tutor_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Aula del Tutor';
	else
		titulo = 'Nuevo  Aula del Tutor';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

