//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_cond_matricula_modal.html" id="cat_cond_matricula-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Condicion de matricula</a></li>');

	$('#cat_cond_matricula-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cond_matricula_modal($(this));
	});

	//lista tabla
	cond_matricula_listar_tabla();
});

function cond_matricula_eliminar(link){
	_delete('api/condMatricula/' + $(link).data("id"),
			function(){
					cond_matricula_listar_tabla();
				}
			);
}

function cond_matricula_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_cond_matricula_modal.html');
	cond_matricula_modal(link);
	
}

function cond_matricula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_cond_matricula-frm');
		
		$('#cat_cond_matricula-frm #btn-agregar').on('click',function(event){
			$('#cat_cond_matricula-frm #id').val('');
			_post($('#cat_cond_matricula-frm').attr('action') , $('#cat_cond_matricula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/condMatricula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_cond_matricula-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cond_matricula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Condicion de matricula';
	else
		titulo = 'Nuevo  Condicion de matricula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cond_matricula_listar_tabla(){
	_get('api/condMatricula/listar/',
			function(data){
			console.log(data);
				$('#cat_cond_matricula-tabla').dataTable({
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
							{"title":"Condicion", "data" : "nom"}, 
							{"title":"Descripci&oacute;n", "data" : "des"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cond_matricula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cond_matricula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cond_matricula_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

