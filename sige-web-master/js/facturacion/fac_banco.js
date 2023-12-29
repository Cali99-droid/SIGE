//Se ejecuta cuando la pagina q lo llama le envia parametross
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	$('#fac_banco-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		banco_modal($(this));
	});
	
	//lista tabla
	banco_listar_tabla();
});

function banco_eliminar(link){
	_delete('api/banco/' + $(link).data("id"),
			function(){
					banco_listar_tabla();
				}
			);
}

function banco_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_banco_modal.html');
	banco_modal(link);
	
}

function banco_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		if (link.data('id')){
			_get('api/banco/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		banco_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Banco';
	else
		titulo = 'Nuevo  Banco';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function banco_listar_tabla(){
	_get('api/banco/listar/',
			function(data){
			console.log(data);
				$('#fac_banco-tabla').dataTable({
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
			               {"title":"C&oacute;digo", "data" : "cod"}, 
			               {"title":"Nro de cuenta", "data" : "nro_cta"}, 
			               {"title":"Moneda", "data" : "moneda"}, 
			               {"title":"Tipo de cuenta", "data" : "tip_cta"}, 
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="banco_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="banco_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}
	);

}

function generarArchivo(e, mes){
		e.preventDefault();
		document.location.href= _URL + 'api/banco/genArchivoPagos/' + $('#_id_anio').text() + '/' + mes;
}