//edicion completa de una tabla
function log_login_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_log_login_full.html');
	log_login_full_modal(link);

}


function log_login_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('seg_log_login_full-frm');
		
		$('#seg_log_login_full-frm #btn-agregar').on('click',function(event){
			$('#seg_log_login_full-frm #id').val('');
			_post($('#seg_log_login_full-frm').attr('action') , $('#seg_log_login_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/logLogin/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('seg_perfil',$('#seg_log_login-frm #id_per'),data.id_per);	
						}
			);
		}else{
					_llenarCombo('seg_perfil',$('#seg_log_login-frm #id_per'));
				}
		$('#seg_log_login_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		log_login_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Log de login al sistema';
	else
		titulo = 'Nuevo  Log de login al sistema';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

