//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	$('#seg_perfil-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		perfil_modal($(this));
	});

	//lista tabla
	perfil_listar_tabla();
});

function perfil_eliminar(link){
	_delete('api/perfil/' + $(link).data("id"),
			function(){
					perfil_listar_tabla();
				}
			);
}

function perfil_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_perfil_modal.html');
	perfil_modal(link);
	
}

function perfil_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		if (link.data('id')){
			_get('api/perfil/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		perfil_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Perfil de usuario';
	else
		titulo = 'Nuevo  Perfil de usuario';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function perfil_listar_tabla(){
	_get('api/perfil/listar/',
			function(data){
			console.log(data);
				$('#seg_perfil-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			               {"title":"Nombre", "data" : "nom"}, 
			               {"title":"Descripci&oacute;n", "data" : "des"}, 
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="perfil_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="perfil_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}
	);

}