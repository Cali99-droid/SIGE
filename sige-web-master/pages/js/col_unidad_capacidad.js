//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_unidad_capacidad_modal.html" id="col_unidad_capacidad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Unidad Capacidad</a></li>');

	$('#col_unidad_capacidad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		unidad_capacidad_modal($(this));
	});

	//lista tabla
	unidad_capacidad_listar_tabla();
});

function unidad_capacidad_eliminar(link){
	_delete('api/unidadCapacidad/' + $(link).data("id"),
			function(){
					unidad_capacidad_listar_tabla();
				}
			);
}

function unidad_capacidad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_unidad_capacidad_modal.html');
	unidad_capacidad_modal(link);
	
}

function unidad_capacidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_unidad_capacidad-frm');
		
		$('#col_unidad_capacidad-frm #btn-agregar').on('click',function(event){
			$('#col_unidad_capacidad-frm #id').val('');
			_post($('#col_unidad_capacidad-frm').attr('action') , $('#col_unidad_capacidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/unidadCapacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_curso_unidad',$('#id_uni'), data.id_uni);
				_llenarCombo('col_capacidad',$('#id_cap'), data.id_cap);
			});
		}else{
			_llenarCombo('col_curso_unidad',$('#id_uni'));
			_llenarCombo('col_capacidad',$('#id_cap'));
			$('#col_unidad_capacidad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		unidad_capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La relacion de unidad capacidad nos da la programacion anual';
	else
		titulo = 'Nuevo  La relacion de unidad capacidad nos da la programacion anual';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function unidad_capacidad_listar_tabla(){
	_get('api/unidadCapacidad/listar/',
			function(data){
			console.log(data);
				$('#col_unidad_capacidad-tabla').dataTable({
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
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_capacidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_capacidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="unidad_capacidad_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

