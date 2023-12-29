//edicion completa de una tabla
function curso_aula_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_full.html');
	curso_aula_full_modal(link);

}


function curso_aula_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_curso_aula_full-frm');
		
		$('#col_curso_aula_full-frm #btn-agregar').on('click',function(event){
			$('#col_curso_aula_full-frm #id').val('');
			_post($('#col_curso_aula_full-frm').attr('action') , $('#col_curso_aula_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cursoAula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_curso_anio',$('#id_cua-frm #id_cua'),data.id_cua);	
						_llenarCombo('col_aula',$('#id_au-frm #id_au'),data.id_au);	
						_llenarCombo('ges_trabajador',$('#id_tra-frm #id_tra'),data.id_tra);	
						}
			);
		}else{
					_llenarCombo('col_curso_anio',$('#id_cua-frm #id_cua'));
					_llenarCombo('col_aula',$('#id_au-frm #id_au'));
					_llenarCombo('ges_trabajador',$('#id_tra-frm #id_tra'));
				}
		$('#col_curso_aula_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

