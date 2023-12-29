//edicion completa de una tabla
function res_diario_boleta_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_res_diario_boleta_full.html');
	res_diario_boleta_full_modal(link);

}


function res_diario_boleta_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('fac_res_diario_boleta_full-frm');
		
		$('#fac_res_diario_boleta_full-frm #btn-agregar').on('click',function(event){
			$('#fac_res_diario_boleta_full-frm #id').val('');
			_post($('#fac_res_diario_boleta_full-frm').attr('action') , $('#fac_res_diario_boleta_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/resDiarioBoleta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#fac_res_diario_boleta_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		res_diario_boleta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Resumen Diario de Boletas';
	else
		titulo = 'Nuevo  Resumen Diario de Boletas';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

