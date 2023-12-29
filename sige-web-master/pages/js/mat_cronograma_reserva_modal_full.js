//edicion completa de una tabla
function cronograma_reserva_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_cronograma_reserva_full.html');
	cronograma_reserva_full_modal(link);

}


function cronograma_reserva_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_cronograma_reserva_full-frm');
		
		$('#mat_cronograma_reserva_full-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma_reserva_full-frm #id').val('');
			_post($('#mat_cronograma_reserva_full-frm').attr('action') , $('#mat_cronograma_reserva_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cronogramaReserva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#mat_cronograma_reserva-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#mat_cronograma_reserva-frm #id_niv'),data.id_niv);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#mat_cronograma_reserva-frm #id_anio'));
					_llenarCombo('cat_nivel',$('#mat_cronograma_reserva-frm #id_niv'));
				}
		$('#mat_cronograma_reserva_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_reserva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma de Reserva';
	else
		titulo = 'Nuevo  Cronograma de Reserva';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

