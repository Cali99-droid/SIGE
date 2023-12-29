//edicion completa de una tabla
function tarifas_emergencia_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_tarifas_emergencia_full.html');
	tarifas_emergencia_full_modal(link);

}


function tarifas_emergencia_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_tarifas_emergencia_full-frm');
		
		$('#mat_tarifas_emergencia_full-frm #btn-agregar').on('click',function(event){
			$('#mat_tarifas_emergencia_full-frm #id').val('');
			_post($('#mat_tarifas_emergencia_full-frm').attr('action') , $('#mat_tarifas_emergencia_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tarifasEmergencia/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('per_periodo',$('#mat_tarifas_emergencia-frm #id_per'),data.id_per);	
						}
			);
		}else{
					_llenarCombo('per_periodo',$('#mat_tarifas_emergencia-frm #id_per'));
				}
		$('#mat_tarifas_emergencia_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tarifas_emergencia_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tarifas de emergencia';
	else
		titulo = 'Nuevo  Tarifas de emergencia';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

