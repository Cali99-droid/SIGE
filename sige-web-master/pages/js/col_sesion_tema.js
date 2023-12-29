//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_sesion_tema_modal.html" id="col_sesion_tema-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Sesion Campo Tem&aacute;tico</a></li>');

	$('#col_sesion_tema-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		sesion_tema_modal($(this));
	});

	//lista tabla
	sesion_tema_listar_tabla();
});

function sesion_tema_eliminar(link){
	_delete('api/sesionTema/' + $(link).data("id"),
			function(){
					sesion_tema_listar_tabla();
				}
			);
}

function sesion_tema_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_tema_modal.html');
	sesion_tema_modal(link);
	
}

function sesion_tema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_sesion_tema-frm');
		
		$('#col_sesion_tema-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_tema-frm #id').val('');
			_post($('#col_sesion_tema-frm').attr('action') , $('#col_sesion_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/sesionTema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_unidad_sesion',$('#id_ses'), data.id_ses);
				_llenarCombo('col_curso_subtema',$('#id_ccs'), data.id_ccs);
			});
		}else{
			_llenarCombo('col_unidad_sesion',$('#id_ses'));
			_llenarCombo('col_curso_subtema',$('#id_ccs'));
			$('#col_sesion_tema-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Campo Tematico por Sesion';
	else
		titulo = 'Nuevo  Campo Tematico por Sesion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function sesion_tema_listar_tabla(){
	_get('api/sesionTema/listar/',
			function(data){
			console.log(data);
				$('#col_sesion_tema-tabla').dataTable({
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
							{"title":"Tipo", "data" : "tipo"}, 
							{"title":"Sesion", "data": "unidadSesion.tit"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_tema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_tema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_tema_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

