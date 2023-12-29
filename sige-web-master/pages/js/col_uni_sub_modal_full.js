//edicion completa de una tabla
function uni_sub_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_uni_sub_full.html');
	uni_sub_full_modal(link);

}


function uni_sub_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_uni_sub_full-frm');
		
		$('#col_uni_sub_full-frm #btn-agregar').on('click',function(event){
			$('#col_uni_sub_full-frm #id').val('');
			_post($('#col_uni_sub_full-frm').attr('action') , $('#col_uni_sub_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/uniSub/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_curso_unidad',$('#col_uni_sub-frm #id_uni'),data.id_uni);	
						_llenarCombo('col_curso_subtema',$('#col_uni_sub-frm #id_ccs'),data.id_ccs);	
						}
			);
		}else{
					_llenarCombo('col_curso_unidad',$('#col_uni_sub-frm #id_uni'));
					_llenarCombo('col_curso_subtema',$('#col_uni_sub-frm #id_ccs'));
				}
		$('#col_uni_sub_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		uni_sub_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Subtema';
	else
		titulo = 'Nuevo  Unidad Subtema';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

