//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/seguridad/seg_opcion_modal.html" id="seg_opcion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Opci&oacute;n del sistema</a></li>');

	$('#seg_opcion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		opcion_modal($(this));
	});

	//lista tabla
	opcion_listar_tabla();
});

function opcion_eliminar(link){
	_delete('api/opcion/' + $(link).data("id"),
			function(){
					opcion_listar_tabla();
				}
			);
}

function opcion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_opcion_modal.html');
	opcion_modal(link);
	
}

function opcion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('seg_opcion-frm');
		
		$('#seg_opcion-frm #btn-agregar').on('click',function(event){
			$('#seg_opcion-frm #id').val('');
			_post($('#seg_opcion-frm').attr('action') , $('#seg_opcion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/opcion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('seg_opcion',$('#id_opc'), data.id_opc);
			});
		}else{
			_llenarCombo('seg_opcion',$('#id_opc'));
			$('#seg_opcion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		opcion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Opci&oacute;n del sistema';
	else
		titulo = 'Nuevo  Opci&oacute;n del sistema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function opcion_listar_tabla(){
	_get('api/opcion/listar/',
			function(data){
			console.log(data);
				$('#seg_opcion-tabla').dataTable({
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
			        	 	{"title":"Padre", "data": "opcion.nom"}, 
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Nombre", "data" : "titulo"}, 
							{"title":"Icono", "data" : "icon"}, 
							{"title":"URL", "data" : "url"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="opcion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="opcion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="opcion_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

