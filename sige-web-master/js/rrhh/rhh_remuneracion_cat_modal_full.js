//edicion completa de una tabla
function remuneracion_cat_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/recursos_humanos/rhh_remuneracion_cat_full.html');
	remuneracion_cat_full_modal(link);

}


function remuneracion_cat_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('rhh_remuneracion_cat_full-frm');
		
		$('#rhh_remuneracion_cat_full-frm #btn-agregar').on('click',function(event){
			$('#rhh_remuneracion_cat_full-frm #id').val('');
			_post($('#rhh_remuneracion_cat_full-frm').attr('action') , $('#rhh_remuneracion_cat_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/remuneracionCat/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#rhh_remuneracion_cat-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_linea_carrera',$('#rhh_remuneracion_cat-frm #id_lcarr'),data.id_lcarr);	
						_llenarCombo('cat_denominacion',$('#rhh_remuneracion_cat-frm #id_cden'),data.id_cden);	
						_llenarCombo('cat_categoria_ocupacional',$('#rhh_remuneracion_cat-frm #id_cocu'),data.id_cocu);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#rhh_remuneracion_cat-frm #id_anio'));
					_llenarCombo('cat_linea_carrera',$('#rhh_remuneracion_cat-frm #id_lcarr'));
					_llenarCombo('cat_denominacion',$('#rhh_remuneracion_cat-frm #id_cden'));
					_llenarCombo('cat_categoria_ocupacional',$('#rhh_remuneracion_cat-frm #id_cocu'));
				}
		$('#rhh_remuneracion_cat_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		remuneracion_cat_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Remuneracion Categoria Ocupacional';
	else
		titulo = 'Nuevo  Remuneracion Categoria Ocupacional';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

