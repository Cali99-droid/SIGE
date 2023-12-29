//edicion completa de una tabla
function conf_mensualidad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_conf_mensualidad_full.html');
	conf_mensualidad_full_modal(link);

}


function conf_mensualidad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_conf_mensualidad_full-frm');
		
		$('#mat_conf_mensualidad_full-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_mensualidad_full-frm #id').val('');
			_post($('#mat_conf_mensualidad_full-frm').attr('action') , $('#mat_conf_mensualidad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/confMensualidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('per_periodo',$('#mat_conf_mensualidad-frm #id_per'),data.id_per);	
						}
			);
		}else{
					_llenarCombo('per_periodo',$('#mat_conf_mensualidad-frm #id_per'));
				}
		$('#mat_conf_mensualidad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_mensualidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de mensualidad';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de mensualidad';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

