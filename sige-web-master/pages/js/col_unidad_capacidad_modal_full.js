//edicion completa de una tabla
function unidad_capacidad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_capacidad_full.html');
	unidad_capacidad_full_modal(link);

}


function unidad_capacidad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_unidad_capacidad_full-frm');
		
		_llenarCombo('col_curso_unidad',$('#col_unidad_capacidad_full-frm #id_uni'));
		_llenarCombo('col_capacidad',$('#col_unidad_capacidad_full-frm #id_cap'));
		
		$('#col_unidad_capacidad_full-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_capacidad_full-frm #id').val('');
			_post($('#col_unidad_capacidad_full-frm').attr('action') , $('#col_unidad_capacidad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/unidadCapacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_unidad_capacidad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La relacion de unidad capacidad nos da la programacion anual';
	else
		titulo = 'Nuevo  La relacion de unidad capacidad nos da la programacion anual';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

