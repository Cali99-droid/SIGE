//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_tema_modal.html" id="col_tema-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tema</a></li>');

	$('#col_tema-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tema_modal($(this));
	});

	//lista tabla
	tema_listar_tabla();
})

function _onchangeAnio(id_anio, anio){
	tema_listar_tabla();
}

function tema_eliminar(link){
	_delete('api/tema/' + $(link).data("id"),
			function(){
					tema_listar_tabla();
				}
			);
}

function tema_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_tema_modal.html');
	tema_modal(link);
	
}

function tema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_tema-frm');
		
		$('#col_tema-frm #btn-agregar').on('click',function(event){
			$('#col_tema-frm #id').val('');
			_post($('#col_tema-frm').attr('action') , $('#col_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_curso',$('#id_cur'), data.id_cur);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_curso',$('#id_cur'));
			$('#col_tema-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tema a tratarse por curso';
	else
		titulo = 'Nuevo  Tema a tratarse por curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tema_listar_tabla(){
	var param = {id_anio:$('#_id_anio').text()};
	_get('api/tema/listar/',
			function(data){
			console.log(data);
				$('#col_tema-tabla').dataTable({
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
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Curso", "data": "curso.nom"},
							{"title":"Tema", "data" : "nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tema_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

