//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/evaluacion/eva_instrumento_modal.html" id="eva_instrumento-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Instrumentos</a></li>');

	$('#eva_instrumento-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		instrumento_modal($(this));
	});

	//lista tabla
	instrumento_listar_tabla();
});

function instrumento_eliminar(link){
	_delete('api/instrumento/' + $(link).data("id"),
			function(){
					instrumento_listar_tabla();
				}
			);
}

function instrumento_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_instrumento_modal.html');
	instrumento_modal(link);
	
}

function instrumento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('eva_instrumento-frm');
		
		$('#eva_instrumento-frm #btn-agregar').on('click',function(event){
			$('#eva_instrumento-frm #id').val('');
			_post($('#eva_instrumento-frm').attr('action') , $('#eva_instrumento-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/instrumento/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#eva_instrumento-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		instrumento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Instrumentos';
	else
		titulo = 'Nuevo  Instrumentos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function instrumento_listar_tabla(){
	_get('api/instrumento/listar/',
			function(data){
			console.log(data);
				$('#eva_instrumento-tabla').dataTable({
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
									'<li><a href="#" data-id="' + row.id + '" onclick="instrumento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="instrumento_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="instrumento_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

