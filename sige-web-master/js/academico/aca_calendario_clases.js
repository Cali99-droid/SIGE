//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/aca_calendario_clases_modal.html" id="aca_calendario_clases-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Clases sab y/o dom.</a></li>');

	$('#aca_calendario_clases-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		calendario_clases_modal($(this));
	});

	//lista tabla
	calendario_clases_listar_tabla();
});

function calendario_clases_eliminar(link){
	_delete('api/calendarioClases/' + $(link).data("id"),
			function(){
					calendario_clases_listar_tabla();
				}
			);
}

function calendario_clases_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/aca_calendario_clases_modal.html');
	calendario_clases_modal(link);
	
}

function calendario_clases_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('aca_calendario_clases-frm');
		
		$('#aca_calendario_clases-frm #btn-agregar').on('click',function(event){
			$('#aca_calendario_clases-frm #id').val('');
			_post($('#aca_calendario_clases-frm').attr('action') , $('#aca_calendario_clases-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/calendarioClases/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#aca_calendario_clases-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		calendario_clases_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Clases sab y/o dom.';
	else
		titulo = 'Nuevo  Clases sab y/o dom.';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function calendario_clases_listar_tabla(){
	_get('api/calendarioClases/listar/',
			function(data){
			console.log(data);
				$('#aca_calendario_clases-tabla').dataTable({
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
							{"title":"D&iacute;a", "data" : "dia"}, 
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="calendario_clases_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="calendario_clases_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="calendario_clases_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

