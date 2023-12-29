//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/aca_festivo_modal.html" id="aca_festivo-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Dias Festivos</a></li>');

	$('#aca_festivo-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		festivo_modal($(this));
	});

	//lista tabla
	festivo_listar_tabla();
});

function festivo_eliminar(link){
	_delete('api/festivo/' + $(link).data("id"),
			function(){
					festivo_listar_tabla();
				}
			);
}

function festivo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/aca_festivo_modal.html');
	festivo_modal(link);
	
}

function festivo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('aca_festivo-frm');
		
		$('#aca_festivo-frm #btn-agregar').on('click',function(event){
			$('#aca_festivo-frm #id').val('');
			_post($('#aca_festivo-frm').attr('action') , $('#aca_festivo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/festivo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#aca_festivo-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		festivo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Dias Festivos';
	else
		titulo = 'Nuevo  Dias Festivos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function festivo_listar_tabla(){
	_get('api/festivo/listar/',
			function(data){
			console.log(data);
				$('#aca_festivo-tabla').dataTable({
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
							{"title":"D&iacute;a Festivo", "data" : "dia"}, 
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="festivo_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="festivo_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="festivo_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

