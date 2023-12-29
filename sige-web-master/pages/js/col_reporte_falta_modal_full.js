//edicion completa de una tabla
function reporte_falta_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_falta_full.html');
	reporte_falta_full_modal(link);

}


function reporte_falta_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_reporte_falta_full-frm');
		
		$('#col_reporte_falta_full-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_falta_full-frm #id').val('');
			_post($('#col_reporte_falta_full-frm').attr('action') , $('#col_reporte_falta_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/reporteFalta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_reporte_conductual',$('#col_reporte_falta-frm #id_cp'),data.id_cp);	
						_llenarCombo('cat_falta',$('#col_reporte_falta-frm #id_cf'),data.id_cf);	
						}
			);
		}else{
					_llenarCombo('col_reporte_conductual',$('#col_reporte_falta-frm #id_cp'));
					_llenarCombo('cat_falta',$('#col_reporte_falta-frm #id_cf'));
				}
		$('#col_reporte_falta_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reporte Falta';
	else
		titulo = 'Nuevo  Reporte Falta';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

