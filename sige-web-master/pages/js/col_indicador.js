//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_indicador_modal.html" id="col_indicador-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Indicador</a></li>');

	$('#col_indicador-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		indicador_modal($(this));
	});

	//lista tabla
	indicador_listar_tabla();
});

function indicador_eliminar(link){
	_delete('api/indicador/' + $(link).data("id"),
			function(){
					indicador_listar_tabla();
				}
			);
}

function indicador_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_indicador_modal.html');
	indicador_modal(link);
	
}

function indicador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_indicador-frm');
		
		$('#col_indicador-frm #btn-agregar').on('click',function(event){
			$('#col_indicador-frm #id').val('');
			_post($('#col_indicador-frm').attr('action') , $('#col_indicador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/indicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('col_capacidad',$('#id_cap'), data.id_cap);
			});
		}else{
			_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('cat_grad',$('#id_gra'));
			_llenarCombo('col_capacidad',$('#id_cap'));
			$('#col_indicador-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador';
	else
		titulo = 'Nuevo  Indicador';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function indicador_listar_tabla(){
	_get('api/indicador/listar/',
			function(data){
			console.log(data);
				$('#col_indicador-tabla').dataTable({
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
							{"title":"Indicador", "data" : "nom"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="indicador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="indicador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="indicador_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

