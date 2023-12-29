//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_per_uni_det_modal.html" id="col_per_uni_det-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Periodo Unidad detalle</a></li>');

	$('#col_per_uni_det-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		per_uni_det_modal($(this));
	});

	//lista tabla
	per_uni_det_listar_tabla();
});

function per_uni_det_eliminar(link){
	_delete('api/perUniDet/' + $(link).data("id"),
			function(){
					per_uni_det_listar_tabla();
				}
			);
}

function per_uni_det_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_det_modal.html');
	per_uni_det_modal(link);
	
}

function per_uni_det_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_per_uni_det-frm');
		
		$('#col_per_uni_det-frm #btn-agregar').on('click',function(event){
			$('#col_per_uni_det-frm #id').val('');
			_post($('#col_per_uni_det-frm').attr('action') , $('#col_per_uni_det-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perUniDet/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_per_uni',$('#id_cpu'), data.id_cpu);
			});
		}else{
			_llenarCombo('col_per_uni',$('#id_cpu'));
			$('#col_per_uni_det-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_uni_det_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Unidad detalle';
	else
		titulo = 'Nuevo  Periodo Unidad detalle';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function per_uni_det_listar_tabla(){
	_get('api/perUniDet/listar/',
			function(data){
			console.log(data);
				$('#col_per_uni_det-tabla').dataTable({
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
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Periodo Unidad", "data": "perUni.nump"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_det_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_det_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

