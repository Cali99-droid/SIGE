//edicion completa de una tabla
function cronograma_ratificacion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_cronograma_ratificacion_full.html');
	cronograma_ratificacion_full_modal(link);

}


function cronograma_ratificacion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_cronograma_ratificacion_full-frm');
		
		$('#mat_cronograma_ratificacion_full-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma_ratificacion_full-frm #id').val('');
			_post($('#mat_cronograma_ratificacion_full-frm').attr('action') , $('#mat_cronograma_ratificacion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cronogramaRatificacion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#mat_cronograma_ratificacion-frm #id_anio'),data.id_anio);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#mat_cronograma_ratificacion-frm #id_anio'));
				}
		$('#mat_cronograma_ratificacion_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_ratificacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma Ratificacion';
	else
		titulo = 'Nuevo  Cronograma Ratificacion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

