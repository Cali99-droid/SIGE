//edicion completa de una tabla
function nota_indicador_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_nota_indicador_full.html');
	nota_indicador_full_modal(link);

}


function nota_indicador_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('not_nota_indicador_full-frm');
		
		$('#not_nota_indicador_full-frm #btn-agregar').on('click',function(event){
			$('#not_nota_indicador_full-frm #id').val('');
			_post($('#not_nota_indicador_full-frm').attr('action') , $('#not_nota_indicador_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/notaIndicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('not_nota',$('#not_nota_indicador-frm #id_not'),data.id_not);	
						_llenarCombo('not_ind_eva',$('#not_nota_indicador-frm #id_nie'),data.id_nie);	
						}
			);
		}else{
					_llenarCombo('not_nota',$('#not_nota_indicador-frm #id_not'));
					_llenarCombo('not_ind_eva',$('#not_nota_indicador-frm #id_nie'));
				}
		$('#not_nota_indicador_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		nota_indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Indicador';
	else
		titulo = 'Nuevo  Nota Indicador';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

