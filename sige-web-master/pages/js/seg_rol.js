//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
/*//se ejecuta siempre despues de cargar el html
$(function(){
	$('#seg_rol-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		rol_modal($(this));
	});

	//lista tabla
	rol_listar_tabla();
});*/

$(function(){
	_onloadPage();
	$('#_botonera').html('<li><a href="pages/seguridad/seg_rol_modal.html" id="seg_rol-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Rol</a></li>');
	$('#seg_rol-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		rol_modal($(this));
	});

	//lista tabla
	rol_listar_tabla();	
});


function rol_eliminar(link){
	_delete('api/rol/' + $(link).data("id"),
			function(){
					rol_listar_tabla();
				}
			);
}

function rol_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_rol_modal.html');
	rol_modal(link);
	
}

function rol_permisos(row,e){
	e.preventDefault();
	var link = $(row);
	/*
	link.attr('href','pages/seguridad/seg_rol_opcion.html');
	rol_opcion_modal(link);
	*/
	var param = {id:link.data('id'),rol: link.data('nom')};
	_send('pages/seguridad/seg_rol_opcion.html','Permisos del rol' , 'Editar' ,param);
}

function rol_opcion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
	
		$('.tree-checkbox').fancytree({
            checkbox: true,
            selectMode: 3,
            source: {
                url: _URL + 'api/opcion/menuTree/' + link.data("id")
            }
        });
		

      
		if (link.data('id')){
			_get('api/rol/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		rol_listar_tabla();
	}
	
	//Abrir el modal
	var titulo = 'Asignaci&oacute;n de permisos al rol del sistema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}




function rol_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		if (link.data('id')){
			_get('api/rol/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		rol_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Rol del Usuario del sistema';
	else
		titulo = 'Nuevo  Rol del Usuario del sistema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function rol_listar_tabla(){
	_get('api/rol/listar/',
			function(data){
			console.log(data);
				$('#seg_rol-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			               {"title":"Rol", "data" : "nom"}, 
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
								'<li><a href="#" data-id="' + row.id + '" onclick="rol_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
								'<li><a href="#" data-id="' + row.id + '" data-nom="' + row.nom + '" onclick="rol_permisos(this, event)"><i class="fa fa-unlock"></i> Permisos</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="rol_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}
	);

}