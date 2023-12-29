//edicion completa de una tabla
function alumno_grado_ficticio_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_alumno_grado_ficticio_full.html');
	alumno_grado_ficticio_full_modal(link);

}


function alumno_grado_ficticio_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('cat_alumno_grado_ficticio_full-frm');
		
		$('#cat_alumno_grado_ficticio_full-frm #btn-agregar').on('click',function(event){
			$('#cat_alumno_grado_ficticio_full-frm #id').val('');
			_post($('#cat_alumno_grado_ficticio_full-frm').attr('action') , $('#cat_alumno_grado_ficticio_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/alumnoGradoFicticio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_grad',$('#cat_alumno_grado_ficticio-frm #id_gra'),data.id_gra);	
						_llenarCombo('mat_matricula',$('#cat_alumno_grado_ficticio-frm #id_mat'),data.id_mat);	
						}
			);
		}else{
					_llenarCombo('cat_grad',$('#cat_alumno_grado_ficticio-frm #id_gra'));
					_llenarCombo('mat_matricula',$('#cat_alumno_grado_ficticio-frm #id_mat'));
				}
		$('#cat_alumno_grado_ficticio_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_grado_ficticio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno por grado ficticio';
	else
		titulo = 'Nuevo  Alumno por grado ficticio';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

