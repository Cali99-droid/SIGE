//edicion completa de una tabla
function historial_eco_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_full.html');
	historial_eco_full_modal(link);

}


function historial_eco_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_historial_eco_full-frm');
		
		$('#col_historial_eco_full-frm #btn-agregar').on('click',function(event){
			$('#col_historial_eco_full-frm #id').val('');
			_post($('#col_historial_eco_full-frm').attr('action') , $('#col_historial_eco_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/historialEco/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('mat_matricula',$('#col_historial_eco-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_central_riesgo',$('#col_historial_eco-frm #id_ccr'),data.id_ccr);	
						_llenarCombo('cat_central_riesgo',$('#col_historial_eco-frm #id_ccr'),data.id_ccr);	
						}
			);
		}else{
					_llenarCombo('mat_matricula',$('#col_historial_eco-frm #id_mat'));
					_llenarCombo('cat_central_riesgo',$('#col_historial_eco-frm #id_ccr'));
					_llenarCombo('cat_central_riesgo',$('#col_historial_eco-frm #id_ccr'));
				}
		$('#col_historial_eco_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		historial_eco_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Historial Economico Alumno';
	else
		titulo = 'Nuevo  Historial Economico Alumno';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

