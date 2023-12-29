//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_capacidad_modal.html" id="col_capacidad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Capacidad</a></li>');

	$('#col_capacidad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		capacidad_modal($(this));
	});

	//lista tabla
	capacidad_listar_tabla();
});

function capacidad_eliminar(link){
	_delete('api/capacidad/' + $(link).data("id"),
			function(){
					capacidad_listar_tabla();
				}
			);
}

function capacidad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_modal.html');
	capacidad_modal(link);
	
}

function capacidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_capacidad-frm');
		
		$('#col_capacidad-frm #btn-agregar').on('click',function(event){
			$('#col_capacidad-frm #id').val('');
			_post($('#col_capacidad-frm').attr('action') , $('#col_capacidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/capacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_competencia',$('#id_com'), data.id_com);
			});
		}else{
			_llenarCombo('col_competencia',$('#id_com'));
			$('#col_capacidad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Capacidad';
	else
		titulo = 'Nuevo  Capacidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function capacidad_listar_tabla(){
	_get('api/capacidad/listar/',
			function(data){
			console.log(data);
				$('#col_capacidad-tabla').dataTable({
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
							{"title":"Capacidad", "data" : "nom"}, 
							{"title":"Competencia", "data": "competencia.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="capacidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="capacidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="capacidad_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

