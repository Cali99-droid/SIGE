//edicion completa de una tabla
function sesion_tema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_tema_full.html');
	sesion_tema_full_modal(link);

}


function sesion_tema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_sesion_tema_full-frm');
		
		_llenarCombo('col_unidad_sesion',$('#col_sesion_tema_full-frm #id_ses'));
		_llenarCombo('col_curso_subtema',$('#col_sesion_tema_full-frm #id_ccs'));
		
		$('#col_sesion_tema_full-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_tema_full-frm #id').val('');
			_post($('#col_sesion_tema_full-frm').attr('action') , $('#col_sesion_tema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/sesionTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_sesion_tema_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Campo Tematico por Sesion';
	else
		titulo = 'Nuevo  Campo Tematico por Sesion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

