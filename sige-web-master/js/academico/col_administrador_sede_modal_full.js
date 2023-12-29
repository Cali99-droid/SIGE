//edicion completa de una tabla
function administrador_sede_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_administrador_sede_full.html');
	administrador_sede_full_modal(link);

}


function administrador_sede_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_administrador_sede_full-frm');
		
		$('#col_administrador_sede_full-frm #btn-agregar').on('click',function(event){
			$('#col_administrador_sede_full-frm #id').val('');
			_post($('#col_administrador_sede_full-frm').attr('action') , $('#col_administrador_sede_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/administradorSede/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('ges_sucursal',$('#col_administrador_sede-frm #id_suc'),data.id_suc);	
						_llenarCombo('col_anio',$('#col_administrador_sede-frm #id_anio'),data.id_anio);	
						_llenarCombo('ges_trabajador',$('#col_administrador_sede-frm #id_tra'),data.id_tra);	
						}
			);
		}else{
					_llenarCombo('ges_sucursal',$('#col_administrador_sede-frm #id_suc'));
					_llenarCombo('col_anio',$('#col_administrador_sede-frm #id_anio'));
					_llenarCombo('ges_trabajador',$('#col_administrador_sede-frm #id_tra'));
				}
		$('#col_administrador_sede_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		administrador_sede_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Administrador Sede';
	else
		titulo = 'Nuevo  Administrador Sede';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

