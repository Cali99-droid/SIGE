//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_anio_modal.html" id="col_anio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Anio</a></li>');

	$('#col_anio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		anio_modal($(this));
	});

	//lista tabla
	anio_listar_tabla();
});

function anio_eliminar(link){
	_delete('api/anio/' + $(link).data("id"),
			function(){
					anio_listar_tabla();
				},'No se puede eliminar este Año Acadñemico, porque tiene registros relacionados!!'
			);
}

function anio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_anio_modal.html');
	anio_modal(link);
	
}

function anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_anio-frm');
		
		$('#col_anio-frm #btn-agregar').on('click',function(event){
			$('#col_anio-frm #id').val('');
			_post($('#col_anio-frm').attr('action') , $('#col_anio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/anio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#col_anio-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar A&ntilde;o Acad&eacute;mico';
	else
		titulo = 'Nuevo  A&ntilde;o Acad&eacute;mico';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function anio_listar_tabla(){
	_get('api/anio/listar/',
			function(data){
			console.log(data);
				$('#col_anio-tabla').dataTable({
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
							{"title":"A&ntilde;o", "data" : "nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="anio_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="anio_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

