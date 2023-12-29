//edicion completa de una tabla
function ind_eva_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_ind_eva_full.html');
	ind_eva_full_modal(link);

}


function ind_eva_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('not_ind_eva_full-frm');
		
		$('#not_ind_eva_full-frm #btn-agregar').on('click',function(event){
			$('#not_ind_eva_full-frm #id').val('');
			_post($('#not_ind_eva_full-frm').attr('action') , $('#not_ind_eva_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/indEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('not_evaluacion',$('#not_ind_eva-frm #id_ne'),data.id_ne);	
						_llenarCombo('col_ind_sub',$('#not_ind_eva-frm #id_cis'),data.id_cis);	
						}
			);
		}else{
					_llenarCombo('not_evaluacion',$('#not_ind_eva-frm #id_ne'));
					_llenarCombo('col_ind_sub',$('#not_ind_eva-frm #id_cis'));
				}
		$('#not_ind_eva_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ind_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador Evalaucion';
	else
		titulo = 'Nuevo  Indicador Evalaucion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

