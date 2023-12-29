//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_grad_modal.html" id="cat_grad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Grado educativo</a></li>');

	$('#cat_grad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		grad_modal($(this));
	});

	//lista tabla
	grad_listar_tabla();
});

function grad_eliminar(link){
	_delete('api/grad/' + $(link).data("id"),
			function(){
					grad_listar_tabla();
				},'No se puede eliminar el grado porque esta siendo usado en las configuraciones académicas!!'
			);
}

function grad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_grad_modal.html');
	grad_modal(link);
	
}

function grad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_grad-frm');
		
		$('#cat_grad-frm #btn-agregar').on('click',function(event){
			$('#cat_grad-frm #id').val('');
			_post($('#cat_grad-frm').attr('action') , $('#cat_grad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/grad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_nvl'), data.id_niv);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_nvl'));
			$('#cat_grad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		grad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Grado Educativo';
	else
		titulo = 'Nuevo  Grado Educativo';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function grad_listar_tabla(){
	_get('api/grad/listar/',
			function(data){
			console.log(data);
				$('#cat_grad-tabla').dataTable({
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
							{"title":"Grado Educativo", "data" : "nom"}, 
							{"title":"nivel", "data": "nivel.nom"}, 
							{"title":"Tipo de grado", "data" : "tipo","render":function(data, type, row,meta){
								if (data=='F')
									return "Ficticio";
								else
									return "Normal";
							}},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="grad_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="grad_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

