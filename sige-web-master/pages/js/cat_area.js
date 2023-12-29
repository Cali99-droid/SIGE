//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_area_modal.html" id="cat_area-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Area</a></li>');

	$('#cat_area-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		area_modal($(this));
	});

	//lista tabla
	area_listar_tabla();
});

function area_eliminar(link){
	_delete('api/area/' + $(link).data("id"),
			function(){
					area_listar_tabla();
				}
			);
}

function area_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_area_modal.html');
	area_modal(link);
	
}

function area_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_area-frm');
		
		$('#cat_area-frm #btn-agregar').on('click',function(event){
			$('#cat_area-frm #id').val('');
			_post($('#cat_area-frm').attr('action') , $('#cat_area-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/area/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_area-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Area';
	else
		titulo = 'Nuevo  Area';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function area_listar_tabla(){
	_get('api/area/listar/',
			function(data){
			console.log(data);
				$('#cat_area-tabla').dataTable({
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
							{"title":"Area", "data" : "nom"}, 
							{"title":"Descripci&oacute;n", "data" : "des"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="area_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="area_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

