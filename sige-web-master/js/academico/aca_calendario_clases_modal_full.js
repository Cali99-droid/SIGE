//edicion completa de una tabla
function calendario_clases_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/aca_calendario_clases_full.html');
	calendario_clases_full_modal(link);

}


function calendario_clases_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('aca_calendario_clases_full-frm');
		
		
		$('#aca_calendario_clases_full-frm #btn-agregar').on('click',function(event){
			$('#aca_calendario_clases_full-frm #id').val('');
			_post($('#aca_calendario_clases_full-frm').attr('action') , $('#aca_calendario_clases_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/calendarioClases/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#aca_calendario_clases_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		calendario_clases_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Clases sab y/o dom.';
	else
		titulo = 'Nuevo  Clases sab y/o dom.';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

