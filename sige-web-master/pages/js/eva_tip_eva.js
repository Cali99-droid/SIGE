//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/evaluacion/eva_tip_eva_modal.html" id="eva_tip_eva-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tipo de area de evaluacion</a></li>');

	$('#eva_tip_eva-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tip_eva_modal($(this));
	});

	//lista tabla
	tip_eva_listar_tabla();
});

function tip_eva_eliminar(link){
	_delete('api/tipEva/' + $(link).data("id"),
			function(){
					tip_eva_listar_tabla();
				}
			);
}

function tip_eva_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_tip_eva_modal.html');
	tip_eva_modal(link);
	
}

function tip_eva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('eva_tip_eva-frm');
		
		$('#eva_tip_eva-frm #btn-agregar').on('click',function(event){
			$('#eva_tip_eva-frm #id').val('');
			_post($('#eva_tip_eva-frm').attr('action') , $('#eva_tip_eva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tipEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#eva_tip_eva-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de evaluacion';
	else
		titulo = 'Nuevo  Tipo de evaluacion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tip_eva_listar_tabla(){
	_get('api/tipEva/listar/',
			function(data){
			console.log(data);
				$('#eva_tip_eva-tabla').dataTable({
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
							{"title":"Tipo de evaluaci&oacute;n", "data" : "nom"}, 
							{"title":"Entidad", "data" : "tabla"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_eva_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_eva_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tip_eva_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

