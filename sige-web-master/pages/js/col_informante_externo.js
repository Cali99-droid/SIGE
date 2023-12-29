//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_informante_externo_modal.html" id="col_informante_externo-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Informante Externo</a></li>');

	$('#col_informante_externo-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		informante_externo_modal($(this));
	});

	//lista tabla
	informante_externo_listar_tabla();
});

function informante_externo_eliminar(link){
	_delete('api/informanteExterno/' + $(link).data("id"),
			function(){
					informante_externo_listar_tabla();
				}
			);
}

function informante_externo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_informante_externo_modal.html');
	informante_externo_modal(link);
	
}

function informante_externo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_informante_externo-frm');
		
		$('#col_informante_externo-frm #btn-agregar').on('click',function(event){
			$('#col_informante_externo-frm #id').val('');
			_post($('#col_informante_externo-frm').attr('action') , $('#col_informante_externo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/informanteExterno/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#col_informante_externo-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		informante_externo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Informante Externo';
	else
		titulo = 'Nuevo  Informante Externo';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function informante_externo_listar_tabla(){
	_get('api/informanteExterno/listar/',
			function(data){
			console.log(data);
				$('#col_informante_externo-tabla').dataTable({
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
							{"title":"Apellido Paterno", "data" : "ape_pat"}, 
							{"title":"Apellido Materno", "data" : "ape_mat"}, 
							{"title":"N&uacute;mero de documento", "data" : "nro_doc"}, 
							{"title":"Tipo de documento", "data" : "tip_doc"}, 
							{"title":"Celular", "data" : "cel"}, 
							{"title":"Direcci&oacute;n", "data" : "dir"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="informante_externo_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="informante_externo_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

