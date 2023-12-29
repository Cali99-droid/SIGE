//edicion completa de una tabla
function habilitacion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_habilitacion_full.html');
	habilitacion_full_modal(link);

}


function habilitacion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('mat_habilitacion_full-frm');
		
		$('#mat_habilitacion_full-frm #btn-agregar').on('click',function(event){
			$('#mat_habilitacion_full-frm #id').val('');
			_post($('#mat_habilitacion_full-frm').attr('action') , $('#mat_habilitacion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/habilitacion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('alu_alumno',$('#mat_habilitacion-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_anio',$('#mat_habilitacion-frm #id_anio'),data.id_anio);	
						}
			);
		}else{
					_llenarCombo('alu_alumno',$('#mat_habilitacion-frm #id_alu'));
					_llenarCombo('col_anio',$('#mat_habilitacion-frm #id_anio'));
				}
		$('#mat_habilitacion_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		habilitacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Habilitaci&oacute;n de matr&iacute;cula';
	else
		titulo = 'Nuevo  Habilitaci&oacute;n de matr&iacute;cula';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

