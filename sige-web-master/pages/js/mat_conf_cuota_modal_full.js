//edicion completa de una tabla
function conf_cuota_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_conf_cuota_full.html');
	conf_cuota_full_modal(link);

}


function conf_cuota_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_conf_cuota_full-frm');
		
		$('#mat_conf_cuota_full-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_cuota_full-frm #id').val('');
			_post($('#mat_conf_cuota_full-frm').attr('action') , $('#mat_conf_cuota_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/confCuota/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('per_periodo',$('#mat_conf_cuota-frm #id_per'),data.id_per);	
						}
			);
		}else{
					_llenarCombo('per_periodo',$('#mat_conf_cuota-frm #id_per'));
				}
		$('#mat_conf_cuota_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_cuota_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de ingreso';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de ingreso';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

