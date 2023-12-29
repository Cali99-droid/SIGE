function onloadPage(params){
	_onloadPage();
}

$(function(){
	
	$('#fac_conf_recibo-btn-nuevo').on('click', function(e) {
		
		e.preventDefault();

		conf_recibo_modal($(this));
		
	});

	//lista tabla
	conf_recibo_listar_tabla();
	
});

function conf_recibo_eliminar(link){
	_delete('api/confRecibo/' + $(link).data("id"),
			function(){
				conf_recibo_listar_tabla();
				}
			);
}

function conf_recibo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_conf_recibo_modal.html');
	conf_recibo_modal(link);
	
}

function conf_recibo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_llenarCombo('ges_sucursal',$('#id_suc'));
		
		if (link.data('id')){
			_get('api/confRecibo/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_recibo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar serie de recibo';
	else
		titulo = 'Nueva serie de recibo';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function conf_recibo_listar_tabla(){
	_get('api/confRecibo/listar/',
			function(data){
			console.log(data);
				$('#fac_conf_recibo-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 bLengthChange: false,
					 bPaginate: false,
					 //bFilter: false,//se oculta la caja de filtro
					 select: true,
			         columns : [ 
			               {"title":"Local", "data" : "sucursal.nom"}, 
				           {"title":"Tipo", "data" : "tipoDesc"},
				           {"title":"Serie", "data" : "serie"},
				           {"title":"N&uacute;mero", "data" : "numero"},
				           {"title":"Hasta", "data" : "hasta"},
				           {"title":"Estado", "data" : "estDesc"},
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_recibo_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_recibo_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}
	);

}