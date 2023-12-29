//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="#" onclick="nuevo_trabajador()" id="ges_trabajador-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Trabajador</a></li>');

	$('#seg_usuario-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		usuario_modal($(this));
	});

	//lista tabla
	trabajador_listar_tabla();
	
	
});

function nuevo_trabajador(){
	_send('pages/rrhh/ges_trabajador/nuevo.html','Trabajadores','Nuevo Trabajador');
}

function usuario_eliminar(link){
	_delete('api/usuario/' + $(link).data("id"),
			function(){
					usuario_listar_tabla();
				}
			);
}

function usuario_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_modal.html');
	usuario_modal(link);
	
}


function trabajador_listar_tabla(){

	$.getJSON( "pages/rrhh/ges_trabajador/lista_trabajadores.json", function( data ) {

		$('#ges_trabajador-tabla').dataTable({
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
	        	 	{"title":"Trabajador", "render": function ( data, type, row ) {
		            	   return row.nombre;
		            }} , 
		            {"title":"Tip.Doc", "data": "tip_doc"}, 
					{"title":"Nro.Doc", "data" : "nro_doc"},
					{"title":"Cumpleaños", "data" : "cumpleanios"}, 
					{"title":"Acciones", "render": function ( data, type, row ) {
	                   return '<div class="list-icons"> <a href="#" data-id="135" onclick="nuevo_trabajador(this)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a><a href="#" data-id="135" onclick="alumno_descuento_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></div>';
	                   }
					}
		    ],
		    "initComplete": function( settings ) {
				   _initCompleteDT(settings);
			 }
	    });
		
	});
	
/*
	_get('api/usuario/listar/',
			function(data){
			console.log(data);
				$('#seg_usuario-tabla').dataTable({
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
			        	 	{"title":"Trabajador", "render": function ( data, type, row ) {
				            	   return row.trabajador.ape_pat+" "+row.trabajador.ape_mat+" "+row.trabajador.nom;
				            }} , 
				            {"title":"Perfil", "data": "perfil.nom"}, 
							{"title":"Usuario", "data" : "login"},
							{"title":"Pasword", "data" : "password"}, 
							{"title":"Local", "data" : "sucursal.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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
*/
}

