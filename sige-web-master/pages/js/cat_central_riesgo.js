//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_central_riesgo_modal.html" id="cat_central_riesgo-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Central Riesgo</a></li>');

	$('#cat_central_riesgo-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		central_riesgo_modal($(this));
	});

	//lista tabla
	central_riesgo_listar_tabla();
});

function central_riesgo_eliminar(link){
	_delete('api/centralRiesgo/' + $(link).data("id"),
			function(){
					central_riesgo_listar_tabla();
				}
			);
}

function central_riesgo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_central_riesgo_modal.html');
	central_riesgo_modal(link);
	
}

function central_riesgo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_central_riesgo-frm');
		
		$('#cat_central_riesgo-frm #btn-agregar').on('click',function(event){
			$('#cat_central_riesgo-frm #id').val('');
			_post($('#cat_central_riesgo-frm').attr('action') , $('#cat_central_riesgo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/centralRiesgo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_central_riesgo-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		central_riesgo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Central Riesgo';
	else
		titulo = 'Nuevo  Central Riesgo';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function central_riesgo_listar_tabla(){
	_get('api/centralRiesgo/listar/',
			function(data){
			console.log(data);
				$('#cat_central_riesgo-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="central_riesgo_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="central_riesgo_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="central_riesgo_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

