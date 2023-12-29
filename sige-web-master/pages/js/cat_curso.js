//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_curso_modal.html" id="cat_curso-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Curso</a></li>');

	$('#cat_curso-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_modal($(this));
	});

	//lista tabla
	curso_listar_tabla();
});

function curso_eliminar(link){
	_delete('api/curso/' + $(link).data("id"),
			function(){
					curso_listar_tabla();
				},'No se puede eliminar el curso porque ya esta siendo usado en los Periodos Academicos!!'
			);
}

function curso_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_curso_modal.html');
	curso_modal(link);
	
}

function curso_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_curso-frm');
		
		$('#cat_curso-frm #btn-agregar').on('click',function(event){
			$('#cat_curso-frm #id').val('');
			_post($('#cat_curso-frm').attr('action') , $('#cat_curso-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/curso/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_curso-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso';
	else
		titulo = 'Nuevo  Curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_listar_tabla(){
	_get('api/curso/listar/',
			function(data){
			console.log(data);
				$('#cat_curso-tabla').dataTable({
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
							{"title":"Curso", "data" : "nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
							return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="curso_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
							'<a href="#" data-id="' + row.id + '" onclick="curso_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'+
							' <a href="#" data-id="' + row.id + '" onclick="_send(\'pages/catalogos/cat_curso_full.html\',\'Configuracion Cursos\',\'Edici&oacute;n avanzada\',{id:' + row.id + ', curso:\''+row.nom+'\'})" class="list-icons-item" title="Configuraci&oacute;n de Temas"><i class="fa fa-list-alt mr-2 ui-blue ui-size" aria-hidden="true"></i></a></div>';
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

