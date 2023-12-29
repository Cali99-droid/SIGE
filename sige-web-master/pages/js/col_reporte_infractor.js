//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_reporte_infractor_modal.html" id="col_reporte_infractor-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Infractor del Reporte Conductual</a></li>');

	$('#col_reporte_infractor-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		reporte_infractor_modal($(this));
	});

	//lista tabla
	reporte_infractor_listar_tabla();
});

function reporte_infractor_eliminar(link){
	_delete('api/reporteInfractor/' + $(link).data("id"),
			function(){
					reporte_infractor_listar_tabla();
				}
			);
}

function reporte_infractor_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_infractor_modal.html');
	reporte_infractor_modal(link);
	
}

function reporte_infractor_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_reporte_infractor-frm');
		
		$('#col_reporte_infractor-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_infractor-frm #id').val('');
			_post($('#col_reporte_infractor-frm').attr('action') , $('#col_reporte_infractor-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reporteInfractor/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_reporte_conductual',$('#id_cp'), data.id_cp);
				_llenarCombo('mat_matricula',$('#id_mat'), data.id_mat);
			});
		}else{
			_llenarCombo('col_reporte_conductual',$('#id_cp'));
			_llenarCombo('mat_matricula',$('#id_mat'));
			$('#col_reporte_infractor-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_infractor_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Infractor del Reporte Conductual';
	else
		titulo = 'Nuevo  Infractor del Reporte Conductual';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function reporte_infractor_listar_tabla(){
	_get('api/reporteInfractor/listar/',
			function(data){
			console.log(data);
				$('#col_reporte_infractor-tabla').dataTable({
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
							{"title":"Reporte Conductual", "data": "reporteConductual.cod"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_infractor_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_infractor_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

