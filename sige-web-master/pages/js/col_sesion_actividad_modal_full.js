//edicion completa de una tabla
function sesion_actividad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_actividad_full.html');
	sesion_actividad_full_modal(link);

}


function sesion_actividad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_sesion_actividad_full-frm');
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_actividad_full-frm #id_ses'));
		
		$('#col_sesion_actividad_full-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_actividad_full-frm #id').val('');
			_post($('#col_sesion_actividad_full-frm').attr('action') , $('#col_sesion_actividad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/sesionActividad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_sesion_actividad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_actividad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Actividades por Sesion';
	else
		titulo = 'Nuevo  Actividades por Sesion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

