//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_tip_falta_modal.html" id="cat_tip_falta-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tipo de Falta Conductual</a></li>');

	$('#cat_tip_falta-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tip_falta_modal($(this));
	});

	//lista tabla
	tip_falta_listar_tabla();
});

function tip_falta_eliminar(link){
	_delete('api/tipFalta/' + $(link).data("id"),
			function(){
					tip_falta_listar_tabla();
				}
			);
}

function tip_falta_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_tip_falta_modal.html');
	tip_falta_modal(link);
	
}

function tip_falta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_tip_falta-frm');
		
		$('#cat_tip_falta-frm #btn-agregar').on('click',function(event){
			$('#cat_tip_falta-frm #id').val('');
			_post($('#cat_tip_falta-frm').attr('action') , $('#cat_tip_falta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tipFalta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_tip_falta-frm #btn-grabar').hide();
		}
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tip_falta_listar_tabla(){
	_get('api/tipFalta/listar/',
			function(data){
			console.log(data);
				$('#cat_tip_falta-tabla').dataTable({
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
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_falta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_falta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_falta_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

