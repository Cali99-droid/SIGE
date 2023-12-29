//edicion completa de una tabla
function cronograma_libreta_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_cronograma_libreta_full.html');
	cronograma_libreta_full_modal(link);

}


function cronograma_libreta_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('not_cronograma_libreta_full-frm');
		
		$('#not_cronograma_libreta_full-frm #btn-agregar').on('click',function(event){
			$('#not_cronograma_libreta_full-frm #id').val('');
			_post($('#not_cronograma_libreta_full-frm').attr('action') , $('#not_cronograma_libreta_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/cronogramaLibreta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#not_cronograma_libreta-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#not_cronograma_libreta-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_grad',$('#not_cronograma_libreta-frm #id_gra'),data.id_gra);	
						}
			);
		}else{
					_llenarCombo('col_anio',$('#not_cronograma_libreta-frm #id_anio'));
					_llenarCombo('cat_nivel',$('#not_cronograma_libreta-frm #id_niv'));
					_llenarCombo('cat_grad',$('#not_cronograma_libreta-frm #id_gra'));
				}
		$('#not_cronograma_libreta_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_libreta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma para recojer libretas';
	else
		titulo = 'Nuevo  Cronograma para recojer libretas';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

