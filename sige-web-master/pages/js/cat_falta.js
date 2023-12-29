//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_falta_modal.html" id="cat_falta-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Faltas</a></li>');

	$('#cat_falta-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		falta_modal($(this));
	});

	//lista tabla
	falta_listar_tabla();
});

function falta_eliminar(link){
	_delete('api/falta/' + $(link).data("id"),
			function(){
					falta_listar_tabla();
				}
			);
}

function falta_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_falta_modal.html');
	falta_modal(link);
	
}

function falta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_falta-frm');
		
		$('#cat_falta-frm #btn-agregar').on('click',function(event){
			$('#cat_falta-frm #id').val('');
			_post($('#cat_falta-frm').attr('action') , $('#cat_falta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/falta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_tip_falta',$('#id_ctf'), data.id_ctf);
			});
		}else{
			_llenarCombo('cat_tip_falta',$('#id_ctf'));
			$('#cat_falta-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Faltas';
	else
		titulo = 'Nuevo  Faltas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function falta_listar_tabla(){
	_get('api/falta/listar/',
			function(data){
			console.log(data);
				$('#cat_falta-tabla').dataTable({
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
							{"title":"Nombre de la Falta", "data" : "nom"}, 
							{"title":"Falta", "data": "tipFalta.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="falta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="falta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="falta_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

