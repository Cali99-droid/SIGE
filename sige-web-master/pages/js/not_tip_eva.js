//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_tip_eva_modal.html" id="not_tip_eva-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tipo de Evaluacion</a></li>');

	$('#not_tip_eva-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tip_eva_modal($(this));
	});

	//lista tabla
	tip_eva_listar_tabla();
});

function tip_eva_eliminar(link){
	_delete('api/tipEva/' + $(link).data("id"),
			function(){
					tip_eva_listar_tabla();
				}
			);
}

function tip_eva_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_tip_eva_modal.html');
	tip_eva_modal(link);
	
}

function tip_eva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_tip_eva-frm');
		
		$('#not_tip_eva-frm #btn-agregar').on('click',function(event){
			$('#not_tip_eva-frm #id').val('');
			_post($('#not_tip_eva-frm').attr('action') , $('#not_tip_eva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tipEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#not_tip_eva-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de Evaluacion';
	else
		titulo = 'Nuevo  Tipo de Evaluacion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tip_eva_listar_tabla(){
	_get('api/tipEva/listar/',
			function(data){
			console.log(data);
				$('#not_tip_eva-tabla').dataTable({
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
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="tip_eva_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="tip_eva_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

