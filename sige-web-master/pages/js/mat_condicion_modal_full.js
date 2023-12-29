//edicion completa de una tabla
function condicion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_condicion_full.html');
	condicion_full_modal(link);

}


function condicion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_condicion_full-frm');
		
		$('#mat_condicion_full-frm #btn-agregar').on('click',function(event){
			$('#mat_condicion_full-frm #id').val('');
			_post($('#mat_condicion_full-frm').attr('action') , $('#mat_condicion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/condicion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_cond_alumno',$('#mat_condicion-frm #id_cond'),data.id_cond);	
						_llenarCombo('mat_matricula',$('#mat_condicion-frm #id_mat'),data.id_mat);	
						}
			);
		}else{
					_llenarCombo('cat_cond_alumno',$('#mat_condicion-frm #id_cond'));
					_llenarCombo('mat_matricula',$('#mat_condicion-frm #id_mat'));
				}
		$('#mat_condicion_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

