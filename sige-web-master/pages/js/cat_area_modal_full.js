//edicion completa de una tabla
function area_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_area_full.html');
	area_full_modal(link);

}


function area_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_area_anio-tabla').data('id_padre',link.data('id'));

		area_anio_listar_tabla();
		_inputs('cat_area_full-frm');
		
		
		$('#cat_area_full-frm #btn-agregar').on('click',function(event){
			$('#cat_area_full-frm #id').val('');
			_post($('#cat_area_full-frm').attr('action') , $('#cat_area_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/area/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#cat_area_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Area';
	else
		titulo = 'Nuevo  Area';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function area_anio_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_modal.html');
	area_anio_modal(link);
}

function area_anio_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_modal.html');
	link.data('id_area',$('#cat_area_full-frm #id').val());
	area_anio_modal(link);
}

function area_anio_eliminar(link){
	_delete('api/areaAnio/' + $(link).data("id"),
			function(){
				area_anio_listar_tabla();
				}
			);
}
function area_anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_area_anio-frm');
		
		_llenarCombo('col_anio',$('#col_area_anio-frm #id_anio'));	
		_llenarCombo('cat_nivel',$('#col_area_anio-frm #id_niv'));	
		_llenarCombo('cat_area',$('#col_area_anio-frm #id_area'),link.data('id_area'));
		
		$('#col_area_anio-frm #btn-agregar').on('click',function(event){
			$('#col_area_anio-frm #id').val('');
			_post($('#col_area_anio-frm').attr('action') , $('#col_area_anio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/areaAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				}
			);
		}else
			$('#col_area_anio-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar &Aacute;reas Educativas';
	else
		titulo = 'Nuevo  &Aacute;reas Educativas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function area_anio_listar_tabla(){
	_get('api/areaAnio/listar/',
			function(data){
			console.log(data);
				$('#col_area_anio-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Orden", "data" : "ord"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Area", "data": "area.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="area_anio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="area_anio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Áreas', 'area_anio_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_area":$('#col_area_anio-tabla').data('id_padre')}
	);

}	
