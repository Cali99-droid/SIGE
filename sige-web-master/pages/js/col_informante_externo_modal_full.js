//edicion completa de una tabla
function informante_externo_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_informante_externo_full.html');
	informante_externo_full_modal(link);

}


function informante_externo_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_informante_externo_full-frm');
		
		$('#col_informante_externo_full-frm #btn-agregar').on('click',function(event){
			$('#col_informante_externo_full-frm #id').val('');
			_post($('#col_informante_externo_full-frm').attr('action') , $('#col_informante_externo_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/informanteExterno/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#col_informante_externo_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		informante_externo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Informante Externo';
	else
		titulo = 'Nuevo  Informante Externo';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

