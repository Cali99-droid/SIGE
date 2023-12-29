//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	$('#fac_concepto-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		concepto_modal($(this));
	});

	//lista tabla
	concepto_listar_tabla();
});

function concepto_eliminar(link){
	_delete('api/concepto/' + $(link).data("id"),
			function(){
					concepto_listar_tabla();
				}
			);
}

function concepto_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_concepto_modal.html');
	concepto_modal(link);
	
}

function concepto_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		if (link.data('id')){
			_get('api/concepto/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		concepto_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Concepto de pago';
	else
		titulo = 'Nuevo  Concepto de pago';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function concepto_listar_tabla(){
	_get('api/concepto/listar/',
			function(data){
			console.log(data);
				$('#fac_concepto-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
		   	               {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
		   	               {"title":"Tipo", "data" : "tipDes"}, 
		   	               {"title":"Concepto", "data" : "nom"}, 
		   	               {"title":"Descripci&oacute;n", "data" : "des"}, 
		   	               {"title":"Editable?",
		   	            	"render": function ( data, type, row ) {   
		   	            		if (row.flag_edit=="1")
		   	            			return "SI";
		   	            		else
		   	            			return "NO";
		   	            	}
		   	            	}, 
		   	               {"title":"Monto", "data" : "monto"}, 
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="concepto_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="concepto_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}
	);
	

}