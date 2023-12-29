//edicion completa de una tabla
function cap_com_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_cap_com_full.html');
	cap_com_full_modal(link);

}


function cap_com_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('not_cap_com_full-frm');
		
		$('#not_cap_com_full-frm #btn-agregar').on('click',function(event){
			$('#not_cap_com_full-frm #id').val('');
			_post($('#not_cap_com_full-frm').attr('action') , $('#not_cap_com_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/capCom/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('not_comportamiento',$('#not_cap_com-frm #id_nc'),data.id_nc);	
						_llenarCombo('col_capacidad',$('#not_cap_com-frm #id_cap'),data.id_cap);	
						}
			);
		}else{
					_llenarCombo('not_comportamiento',$('#not_cap_com-frm #id_nc'));
					_llenarCombo('col_capacidad',$('#not_cap_com-frm #id_cap'));
				}
		$('#not_cap_com_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cap_com_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Capacidad Comportamiento';
	else
		titulo = 'Nuevo  Nota Capacidad Comportamiento';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

