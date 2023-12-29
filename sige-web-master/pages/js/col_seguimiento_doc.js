//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_seguimiento_doc_modal.html" id="col_seguimiento_doc-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Seguimiento Documento</a></li>');

	$('#col_seguimiento_doc-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		seguimiento_doc_modal($(this));
	});

	//lista tabla
	seguimiento_doc_listar_tabla();
});

function seguimiento_doc_eliminar(link){
	_delete('api/seguimientoDoc/' + $(link).data("id"),
			function(){
					seguimiento_doc_listar_tabla();
				}
			);
}

function seguimiento_doc_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_seguimiento_doc_modal.html');
	seguimiento_doc_modal(link);
	
}

function seguimiento_doc_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_seguimiento_doc-frm');
		
		$('#col_seguimiento_doc-frm #btn-agregar').on('click',function(event){
			$('#col_seguimiento_doc-frm #id').val('');
			_post($('#col_seguimiento_doc-frm').attr('action') , $('#col_seguimiento_doc-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/seguimientoDoc/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('alu_familiar',$('#id_fam'), data.id_fam);
				_llenarCombo('col_per_uni',$('#id_cpu'), data.id_cpu);
			});
		}else{
			_llenarCombo('alu_familiar',$('#id_fam'));
			_llenarCombo('col_per_uni',$('#id_cpu'));
			$('#col_seguimiento_doc-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		seguimiento_doc_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Seguimiento Documento';
	else
		titulo = 'Nuevo  Seguimiento Documento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function seguimiento_doc_listar_tabla(){
	_get('api/seguimientoDoc/listar/',
			function(data){
			console.log(data);
				$('#col_seguimiento_doc-tabla').dataTable({
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
							{"title":"Tipo", "data" : "tip"}, 
							{"title":"Familiar", "data": "familiar.nom"}, 
							{"title":"Periodo Academico", "data": "perUni.nump"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="seguimiento_doc_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="seguimiento_doc_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

