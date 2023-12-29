//edicion completa de una tabla
function aula_especial_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_especial_full.html');
	aula_especial_full_modal(link);

}


function aula_especial_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_aula_especial_full-frm');
		
		$('#col_aula_especial_full-frm #btn-agregar').on('click',function(event){
			$('#col_aula_especial_full-frm #id').val('');
			_post($('#col_aula_especial_full-frm').attr('action') , $('#col_aula_especial_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/aulaEspecial/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_grad',$('#col_aula_especial-frm #id_gra'),data.id_gra);	
						_llenarCombo('mat_matricula',$('#col_aula_especial-frm #id_mat'),data.id_mat);	
						}
			);
		}else{
					_llenarCombo('cat_grad',$('#col_aula_especial-frm #id_gra'));
					_llenarCombo('mat_matricula',$('#col_aula_especial-frm #id_mat'));
				}
		$('#col_aula_especial_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		aula_especial_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno por sal&oacute;n especial';
	else
		titulo = 'Nuevo  Alumno por sal&oacute;n especial';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

