//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_subtema_modal.html" id="col_subtema-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Subtema</a></li>');

	$('#col_subtema-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		subtema_modal($(this));
	});

	//lista tabla
	subtema_listar_tabla();
});

function subtema_eliminar(link){
	_delete('api/subtema/' + $(link).data("id"),
			function(){
					subtema_listar_tabla();
				}
			);
}

function subtema_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_modal.html');
	subtema_modal(link);
	
}

function subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_subtema-frm');
		
		$('#col_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_subtema-frm #id').val('');
			_post($('#col_subtema-frm').attr('action') , $('#col_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/subtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_tema',$('#id_tem'), data.id_tem);
				$('#col_subtema-frm #btn-agregar').hide();
			});
		}else{
			
			_llenarCombo('cat_nivel',$('#id_niv'));
			$('#id_niv').on('change',function(event){
				_llenarCombo('cat_curso',$('#id_cur'),null,$(this).val());
			});
			//alert();
			//_llenarCombo('col_tema',$('#id_tem'));
			$('#col_subtema-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Subtema por Tema';
	else
		titulo = 'Nuevo  Subtema por Tema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function subtema_listar_tabla(){
	_get('api/subtema/listar/',
			function(data){
			console.log(data);
				$('#col_subtema-tabla').dataTable({
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
							{"title":"Subtema", "data" : "nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Observacion", "data" : "obs"}, 
							{"title":"Tema", "data": "tema.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

