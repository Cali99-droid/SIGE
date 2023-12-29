//edicion completa de una tabla
function competencia_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_competencia_full.html');
	competencia_full_modal(link);

}


function competencia_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_capacidad-tabla').data('id_padre',link.data('id'));

		capacidad_listar_tabla();
		_inputs('col_competencia_full-frm');
		
		$('#col_competencia_full-frm #btn-agregar').on('click',function(event){
			$('#col_competencia_full-frm #id').val('');
			_post($('#col_competencia_full-frm').attr('action') , $('#col_competencia_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/competencia/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_nivel',$('#col_competencia_full-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_curso',$('#col_competencia_full-frm #id_cur'),data.id_cur);	
						}
			);
		}else{
					_llenarCombo('cat_nivel',$('#col_competencia_full-frm #id_niv'));
					_llenarCombo('cat_curso',$('#col_competencia_full-frm #id_cur'));
				}
		$('#col_competencia_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		competencia_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Competencia';
	else
		titulo = 'Nuevo  Competencia';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function capacidad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_modal.html');
	capacidad_modal(link);
}

function capacidad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_modal.html');
	link.data('id_com',$('#col_competencia_full-frm #id').val());
	capacidad_modal(link);
}

function capacidad_eliminar(link){
	_delete('api/capacidad/' + $(link).data("id"),
			function(){
				capacidad_listar_tabla();
				}
			);
}
function capacidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_capacidad-frm');
		
		
		
		$('#col_capacidad-frm #btn-agregar').on('click',function(event){
			$('#col_capacidad-frm #id').val('');
			$('#col_capacidad-frm #id_com').attr('disabled',false); 
			_post($('#col_capacidad-frm').attr('action') , $('#col_capacidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
			$('#col_capacidad-frm #id_com').attr('disabled',true); 
		});
		
		if (link.data('id')){
			_get('api/capacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						$('#col_capacidad-frm #id_com').attr('disabled',true); 
						_llenarCombo('col_competencia',$('#col_capacidad-frm #id_com'),data.id_com);	
				}
			);
		}else{
			_llenarCombo('col_competencia',$('#col_capacidad-frm #id_com'),$('#col_competencia_full-frm #id').val());
			$('#col_capacidad-frm #id_com').attr('disabled',true); 
			$('#col_capacidad-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Capacidad';
	else
		titulo = 'Nuevo  Capacidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function capacidad_listar_tabla(){
	_get('api/capacidad/listar/',
			function(data){
			console.log(data);
				$('#col_capacidad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Capacidad", "data" : "nom"}, 
							{"title":"Competencia", "data": "competencia.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="capacidad_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="capacidad_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}	
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Capacidad', 'capacidad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_com":$('#col_capacidad-tabla').data('id_padre')}
	);

}	
