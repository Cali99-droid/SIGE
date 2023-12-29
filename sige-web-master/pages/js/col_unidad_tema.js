//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_unidad_tema_modal.html" id="col_unidad_tema-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Campo Tem&aacute;tico</a></li>');

	$('#col_unidad_tema-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		unidad_tema_modal($(this));
	});

	//lista tabla
	unidad_tema_listar_tabla();
});

function unidad_tema_eliminar(link){
	_delete('api/unidadTema/' + $(link).data("id"),
			function(){
					unidad_tema_listar_tabla();
				}
			);
}

function unidad_tema_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_tema_modal.html');
	unidad_tema_modal(link);
	
}

function unidad_tema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_unidad_tema-frm');
		
		$('#col_unidad_tema-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_tema-frm #id').val('');
			_post($('#col_unidad_tema-frm').attr('action') , $('#col_unidad_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/unidadTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_curso_unidad',$('#id_uni'), data.id_uni);
				_llenarCombo('col_curso_subtema',$('#id_ccs'), data.id_ccs);
			});
		}else{
			_llenarCombo('col_curso_unidad',$('#id_uni'));
			_llenarCombo('col_curso_subtema',$('#id_ccs'));
			$('#col_unidad_tema-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La unidad tiene campo tem&aacute;tico exclusivos';
	else
		titulo = 'Nuevo  La unidad tiene campo tem&aacute;tico exclusivos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function unidad_tema_listar_tabla(){
	_get('api/unidadTema/listar/',
			function(data){
			console.log(data);
				$('#col_unidad_tema-tabla').dataTable({
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
							{"title":"Unidad", "data": "cursoUnidad.nom"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_tema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_tema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_tema_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

