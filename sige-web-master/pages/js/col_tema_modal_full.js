//edicion completa de una tabla
function tema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_tema_full.html');
	tema_full_modal(link);

}


function tema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_subtema-tabla').data('id_padre',link.data('id'));

		subtema_listar_tabla();
		_inputs('col_tema_full-frm');
		
		_llenarCombo('cat_nivel',$('#col_tema_full-frm #id_niv'));
		_llenarCombo('cat_curso',$('#col_tema_full-frm #id_cur'));
		
		$('#col_tema_full-frm #btn-agregar').on('click',function(event){
			$('#col_tema_full-frm #id').val('');
			_post($('#col_tema_full-frm').attr('action') , $('#col_tema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_tema_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tema a tratarse por curso';
	else
		titulo = 'Nuevo  Tema a tratarse por curso';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function subtema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_modal.html');
	subtema_modal(link);
}

function subtema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_modal.html');
	link.data('id_tem',$('#col_tema_full-frm #id').val());
	subtema_modal(link);
}

function subtema_eliminar(link){
	_delete('api/subtema/' + $(link).data("id"),
			function(){
				subtema_listar_tabla();
				}
			);
}
function subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_subtema-frm');
		
				
		_llenarCombo('col_tema',$('#col_subtema-frm #id_tem'),link.data('id_tem'));
		//_llenarComboUrl('api/tema/listar',$('#col_subtema-frm #id_tem'),link.data('id_tem'));
		
		$('#col_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_subtema-frm #id').val('');
			_post($('#col_subtema-frm').attr('action') , $('#col_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/subtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_subtema-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Subtema por Tema';
	else
		titulo = 'Nuevo  Subtema por Tema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function subtema_listar_tabla(){
	_get('api/subtema/listar/',
			function(data){
			console.log(data);
				$('#col_subtema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Observacion", "data" : "obs"}, 
							{"title":"Tema", "data": "tema.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Subtema', 'subtema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_tem":$('#col_subtema-tabla').data('id_padre')}
	);

}	
