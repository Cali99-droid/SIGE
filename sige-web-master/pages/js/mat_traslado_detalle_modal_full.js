//edicion completa de una tabla
function traslado_detalle_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_traslado_detalle_full.html');
	traslado_detalle_full_modal(link);

}


function traslado_detalle_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_traslado_detalle_full-frm');
		
		$('#mat_traslado_detalle_full-frm #btn-agregar').on('click',function(event){
			$('#mat_traslado_detalle_full-frm #id').val('');
			_post($('#mat_traslado_detalle_full-frm').attr('action') , $('#mat_traslado_detalle_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/trasladoDetalle/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('mat_matricula',$('#mat_traslado_detalle-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_col_situacion',$('#mat_traslado_detalle-frm #id_cma'),data.id_cma);	
						_llenarCombo('col_colegio',$('#mat_traslado_detalle-frm #id_col'),data.id_col);	
						}
			);
		}else{
					_llenarCombo('mat_matricula',$('#mat_traslado_detalle-frm #id_mat'));
					_llenarCombo('cat_col_situacion',$('#mat_traslado_detalle-frm #id_cma'));
					_llenarCombo('col_colegio',$('#mat_traslado_detalle-frm #id_col'));
				}
		$('#mat_traslado_detalle_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		traslado_detalle_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Detalle del trsalado';
	else
		titulo = 'Nuevo  Detalle del trsalado';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

