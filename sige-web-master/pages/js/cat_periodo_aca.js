//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_periodo_aca_modal.html" id="cat_periodo_aca-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Periodo Acad&eacute;mico</a></li>');

	$('#cat_periodo_aca-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		periodo_aca_modal($(this));
	});

	//lista tabla
	periodo_aca_listar_tabla();
});

function periodo_aca_eliminar(link){
	_delete('api/periodoAca/' + $(link).data("id"),
			function(){
					periodo_aca_listar_tabla();
				},'No se puede eliminar el Tipo de Periodo porque esta siendo usado en los Periodos Académicos!!'
			);
}

function periodo_aca_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_periodo_aca_modal.html');
	periodo_aca_modal(link);
	
}

function periodo_aca_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_periodo_aca-frm');
		
		$('#cat_periodo_aca-frm #btn-agregar').on('click',function(event){
			$('#cat_periodo_aca-frm #id').val('');
			_post($('#cat_periodo_aca-frm').attr('action') , $('#cat_periodo_aca-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/periodoAca/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_periodo_aca-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		periodo_aca_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Acad&eacute;mico Ejm(Bimestre - Trimestres)';
	else
		titulo = 'Nuevo  Periodo Acad&eacute;mico Ejm(Bimestre - Trimestres)';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function periodo_aca_listar_tabla(){
	_get('api/periodoAca/listar/',
			function(data){
			console.log(data);
				$('#cat_periodo_aca-tabla').dataTable({
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
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="periodo_aca_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="periodo_aca_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

