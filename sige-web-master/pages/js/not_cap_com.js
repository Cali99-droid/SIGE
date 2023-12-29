//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_cap_com_modal.html" id="not_cap_com-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Nota Capacidad Comportamiento</a></li>');

	$('#not_cap_com-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cap_com_modal($(this));
	});

	//lista tabla
	cap_com_listar_tabla();
});

function cap_com_eliminar(link){
	_delete('api/capCom/' + $(link).data("id"),
			function(){
					cap_com_listar_tabla();
				}
			);
}

function cap_com_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_cap_com_modal.html');
	cap_com_modal(link);
	
}

function cap_com_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_cap_com-frm');
		
		$('#not_cap_com-frm #btn-agregar').on('click',function(event){
			$('#not_cap_com-frm #id').val('');
			_post($('#not_cap_com-frm').attr('action') , $('#not_cap_com-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/capCom/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('not_comportamiento',$('#id_nc'), data.id_nc);
				_llenarCombo('col_capacidad',$('#id_cap'), data.id_cap);
			});
		}else{
			_llenarCombo('not_comportamiento',$('#id_nc'));
			_llenarCombo('col_capacidad',$('#id_cap'));
			$('#not_cap_com-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cap_com_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Capacidad Comportamiento';
	else
		titulo = 'Nuevo  Nota Capacidad Comportamiento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cap_com_listar_tabla(){
	_get('api/capCom/listar/',
			function(data){
			console.log(data);
				$('#not_cap_com-tabla').dataTable({
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
							{"title":"Nota", "data" : "nota"}, 
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Nota Comportamiento", "data": "comportamiento.prom"}, 
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cap_com_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cap_com_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

