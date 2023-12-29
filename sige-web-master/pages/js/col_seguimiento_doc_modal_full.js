//edicion completa de una tabla
function seguimiento_doc_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_seguimiento_doc_full.html');
	seguimiento_doc_full_modal(link);

}


function seguimiento_doc_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre

		_inputs('col_seguimiento_doc_full-frm');
		
		$('#col_seguimiento_doc_full-frm #btn-agregar').on('click',function(event){
			$('#col_seguimiento_doc_full-frm #id').val('');
			_post($('#col_seguimiento_doc_full-frm').attr('action') , $('#col_seguimiento_doc_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/seguimientoDoc/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('alu_familiar',$('#col_seguimiento_doc-frm #id_fam'),data.id_fam);	
						_llenarCombo('col_per_uni',$('#col_seguimiento_doc-frm #id_cpu'),data.id_cpu);	
						}
			);
		}else{
					_llenarCombo('alu_familiar',$('#col_seguimiento_doc-frm #id_fam'));
					_llenarCombo('col_per_uni',$('#col_seguimiento_doc-frm #id_cpu'));
				}
		$('#col_seguimiento_doc_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		seguimiento_doc_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Seguimiento Documento';
	else
		titulo = 'Nuevo  Seguimiento Documento';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

