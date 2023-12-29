//edicion completa de una tabla
function tip_falta_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_tip_falta_full.html');
	tip_falta_full_modal(link);

}


function tip_falta_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#cat_falta-tabla').data('id_padre',link.data('id'));

		falta_listar_tabla();
		_inputs('cat_tip_falta_full-frm');
		
		$('#cat_tip_falta_full-frm #btn-agregar').on('click',function(event){
			$('#cat_tip_falta_full-frm #id').val('');
			_post($('#cat_tip_falta_full-frm').attr('action') , $('#cat_tip_falta_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tipFalta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#cat_tip_falta_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de Falta Conductual';
	else
		titulo = 'Nuevo  Tipo de Falta Conductual';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function falta_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/catalogos/cat_falta_modal.html');
	falta_modal(link);
}

function falta_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_falta_modal.html');
	link.data('id_ctf',$('#cat_tip_falta_full-frm #id').val());
	falta_modal(link);
}

function falta_eliminar(link){
	_delete('api/falta/' + $(link).data("id"),
			function(){
				falta_listar_tabla();
				}
			);
}
function falta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('cat_falta-frm');
		
		
		
		$('#cat_falta-frm #btn-agregar').on('click',function(event){
			$('#cat_falta-frm #id').val('');
			_post($('#cat_falta-frm').attr('action') , $('#cat_falta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/falta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_tip_falta',$('#cat_tip_falta-frm #id_ctf'),data.id_ctf);	
				}
			);
		}else{
			_llenarCombo('cat_tip_falta',$('#cat_tip_falta-frm #id_ctf'),$('#cat_tip_falta_full-frm #id').val());
			$('#cat_falta-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Faltas';
	else
		titulo = 'Nuevo  Faltas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function falta_listar_tabla(){
	_get('api/falta/listar/',
			function(data){
			console.log(data);
				$('#cat_falta-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nombre de la Falta", "data" : "nom"}, 
							{"title":"Falta", "data": "tipFalta.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="falta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="falta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Faltas', 'falta_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ctf":$('#cat_falta-tabla').data('id_padre')}
	);

}	
