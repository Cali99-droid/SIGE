//edicion completa de una tabla
function subtema_capacidad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_capacidad_full.html');
	subtema_capacidad_full_modal(link);

}


function subtema_capacidad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_subtema_capacidad_full-frm');
		
		$('#col_subtema_capacidad_full-frm #btn-agregar').on('click',function(event){
			$('#col_subtema_capacidad_full-frm #id').val('');
			_post($('#col_subtema_capacidad_full-frm').attr('action') , $('#col_subtema_capacidad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/subtemaCapacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_curso_subtema',$('#id_ccs-frm #id_ccs'),data.id_ccs);	
						_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'),data.id_cap);	
						}
			);
		}else{
					_llenarCombo('col_curso_subtema',$('#id_ccs-frm #id_ccs'));
					_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'));
				}
		$('#col_subtema_capacidad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La relacion de tema capacidad nos da la programacion anual';
	else
		titulo = 'Nuevo  La relacion de tema capacidad nos da la programacion anual';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

