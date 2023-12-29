//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/tesoreria/fac_res_diario_boleta_modal.html" id="fac_res_diario_boleta-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Resumen diario Boleta</a></li>');

	$('#fac_res_diario_boleta-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		res_diario_boleta_modal($(this));
	});

	//lista tabla
	res_diario_boleta_listar_tabla();
});

function res_diario_boleta_eliminar(link){
	_delete('api/resDiarioBoleta/' + $(link).data("id"),
			function(){
					res_diario_boleta_listar_tabla();
				}
			);
}

function res_diario_boleta_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_res_diario_boleta_modal.html');
	res_diario_boleta_modal(link);
	
}

function res_diario_boleta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('fac_res_diario_boleta-frm');
		
		$('#fac_res_diario_boleta-frm #btn-agregar').on('click',function(event){
			$('#fac_res_diario_boleta-frm #id').val('');
			_post($('#fac_res_diario_boleta-frm').attr('action') , $('#fac_res_diario_boleta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/resDiarioBoleta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#fac_res_diario_boleta-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		res_diario_boleta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Resumen Diario de Boletas';
	else
		titulo = 'Nuevo  Resumen Diario de Boletas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function res_diario_boleta_listar_tabla(){
	_get('api/resDiarioBoleta/listar/',
			function(data){
			console.log(data);
				$('#fac_res_diario_boleta-tabla').dataTable({
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
							{"title":"Numero", "data" : "numero"}, 
							{"title":"Fecha Emision", "data" : "fec_emi"}, 
							{"title":"Cantidad de Registros Procesados", "data" : "cant_reg"}, 
							{"title":"Estado", "data" : "estado"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="res_diario_boleta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="res_diario_boleta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

