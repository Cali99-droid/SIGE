//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_eva_padre_modal.html" id="not_eva_padre-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Evaluacion Padre</a></li>');

	$('#not_eva_padre-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		eva_padre_modal($(this));
	});

	//lista tabla
	eva_padre_listar_tabla();
});

function eva_padre_eliminar(link){
	_delete('api/evaPadre/' + $(link).data("id"),
			function(){
					eva_padre_listar_tabla();
				}
			);
}

function eva_padre_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_eva_padre_modal.html');
	eva_padre_modal(link);
	
}

function eva_padre_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_eva_padre-frm');
		
		$('#not_eva_padre-frm #btn-agregar').on('click',function(event){
			$('#not_eva_padre-frm #id').val('');
			_post($('#not_eva_padre-frm').attr('action') , $('#not_eva_padre-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/evaPadre/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('ges_trabajador',$('#id_tra'), data.id_tra);
			});
		}else{
			_llenarCombo('ges_trabajador',$('#id_tra'));
			$('#not_eva_padre-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		eva_padre_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Evaluacion Padre';
	else
		titulo = 'Nuevo  Evaluacion Padre';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function eva_padre_listar_tabla(){
	_get('api/evaPadre/listar/',
			function(data){
			console.log(data);
				$('#not_eva_padre-tabla').dataTable({
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
							{"title":"Docente", "data": "trabajador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="eva_padre_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="eva_padre_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="eva_padre_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

