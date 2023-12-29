//edicion completa de una tabla
function reporte_infractor_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_infractor_full.html');
	reporte_infractor_full_modal(link);

}


function reporte_infractor_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_reporte_infractor_full-frm');
		
		$('#col_reporte_infractor_full-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_infractor_full-frm #id').val('');
			_post($('#col_reporte_infractor_full-frm').attr('action') , $('#col_reporte_infractor_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/reporteInfractor/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_reporte_conductual',$('#col_reporte_infractor-frm #id_cp'),data.id_cp);	
						_llenarCombo('mat_matricula',$('#col_reporte_infractor-frm #id_mat'),data.id_mat);	
						}
			);
		}else{
					_llenarCombo('col_reporte_conductual',$('#col_reporte_infractor-frm #id_cp'));
					_llenarCombo('mat_matricula',$('#col_reporte_infractor-frm #id_mat'));
				}
		$('#col_reporte_infractor_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_infractor_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Infractor del Reporte Conductual';
	else
		titulo = 'Nuevo  Infractor del Reporte Conductual';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

