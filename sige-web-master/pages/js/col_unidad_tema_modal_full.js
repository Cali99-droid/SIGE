//edicion completa de una tabla
function unidad_tema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_tema_full.html');
	unidad_tema_full_modal(link);

}


function unidad_tema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_unidad_tema_full-frm');
		
		_llenarCombo('col_curso_unidad',$('#col_unidad_tema_full-frm #id_uni'));
		_llenarCombo('col_curso_subtema',$('#col_unidad_tema_full-frm #id_ccs'));
		
		$('#col_unidad_tema_full-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_tema_full-frm #id').val('');
			_post($('#col_unidad_tema_full-frm').attr('action') , $('#col_unidad_tema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/unidadTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_unidad_tema_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La unidad tiene campo tem&aacute;tico exclusivos';
	else
		titulo = 'Nuevo  La unidad tiene campo tem&aacute;tico exclusivos';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

