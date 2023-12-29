//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/configuracion/mod_modulo_modal.html" id="mod_modulo-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Módulo</a></li>');

	$('#mod_modulo-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		modulo_modal($(this));
	});

	//lista tabla
	modulo_listar_tabla();
});

function modulo_eliminar(link){
	_delete('api/modulo/' + $(link).data("id"),
			function(){
					modulo_listar_tabla();
				}
			);
}

function modulo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/configuracion/mod_modulo_modal.html');
	modulo_modal(link);
	
}

function modulo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mod_modulo-frm');
		
		$('#mod_modulo-frm #btn-agregar').on('click',function(event){
			$('#mod_modulo-frm #id').val('');
			_post($('#mod_modulo-frm').attr('action') , $('#mod_modulo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/modulo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#mod_modulo-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		modulo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar modulo';
	else
		titulo = 'Nuevo  modulo';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function modulo_listar_tabla(){
	_get('api/modulo/listarModulos/',
			function(data){
			console.log(data);
				$('#mod_modulo-tabla').dataTable({
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
							{"title":"Modulo", "data" : "nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
							return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="modulo_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
							'<a href="#" data-id="' + row.id + '" onclick="modulo_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'+
							' <a href="#" data-id="' + row.id + '" onclick="_send(\'pages/configuracion/mod_modulo_full.html\',\'Configuracion modulos\',\'Edici&oacute;n avanzada\',{id:' + row.id + ', modulo:\''+row.nom+'\'})" class="list-icons-item" title="Configuraci&oacute;n de Par&aacute;metros"><i class="fa fa-list-alt mr-2 ui-blue ui-size" aria-hidden="true"></i></a></div>';
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

