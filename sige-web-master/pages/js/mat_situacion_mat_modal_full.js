//edicion completa de una tabla
function situacion_mat_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_situacion_mat_full.html');
	situacion_mat_full_modal(link);

}


function situacion_mat_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_situacion_mat_full-frm');
		
		$('#col_situacion_mat_full-frm #btn-agregar').on('click',function(event){
			$('#col_situacion_mat_full-frm #id').val('');
			_post($('#col_situacion_mat_full-frm').attr('action') , $('#col_situacion_mat_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/situacionMat/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('mat_matricula',$('#col_situacion_mat-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_col_situacion',$('#col_situacion_mat-frm #id_cma'),data.id_cma);	
						}
			);
		}else{
					_llenarCombo('mat_matricula',$('#col_situacion_mat-frm #id_mat'));
					_llenarCombo('cat_col_situacion',$('#col_situacion_mat-frm #id_cma'));
				}
		$('#col_situacion_mat_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		situacion_mat_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Situaci&oacute;n Matr&iacute;cula';
	else
		titulo = 'Nuevo  Situaci&oacute;n Matr&iacute;cula';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

