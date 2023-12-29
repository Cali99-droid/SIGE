//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_competencia_modal.html" id="col_competencia-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Competencia</a></li>');

	$('#col_competencia-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		competencia_modal($(this));
	});

	//lista tabla
	competencia_listar_tabla();
});

function competencia_eliminar(link){
	_delete('api/competencia/' + $(link).data("id"),
			function(){
					competencia_listar_tabla();
				}
			);
}

function competencia_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_competencia_modal.html');
	competencia_modal(link);
	
}

function competencia_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_competencia-frm');
		
		$('#col_competencia-frm #btn-agregar').on('click',function(event){
			$('#col_competencia-frm #id').val('');
			_post($('#col_competencia-frm').attr('action') , $('#col_competencia-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/competencia/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_curso',$('#id_cur'), data.id_cur);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_curso',$('#id_cur'));
			$('#col_competencia-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		competencia_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Competencia';
	else
		titulo = 'Nuevo  Competencia';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function competencia_listar_tabla(){
	_get('api/competencia/listar/',
			function(data){
			console.log(data);
				$('#col_competencia-tabla').dataTable({
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
							{"title":"Competencia", "data" : "nom"}, 
							{"title":"Peso", "data" : "peso"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="competencia_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="competencia_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'+
								'<a href="#"  data-id="' + row.id + '" onclick="competencia_editarFull(this, event)" class="list-icons-item" title="Configuraci&oacute;n de Capacidades"><i class="fa fa-list-alt mr-2 ui-blue ui-size" aria-hidden="true"></i></a></div>';
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

