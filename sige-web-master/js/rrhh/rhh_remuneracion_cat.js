//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=null;
function onloadPage(params){
id_anio=$('#_id_anio').text();

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/rrhh/rhh_remuneracion_cat_modal.html" id="rhh_remuneracion_cat-btn-nuevo" ><i class="icon-file-plus"></i> Nueva Linea Carrera</a></li>');

	$('#rhh_remuneracion_cat-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		remuneracion_cat_modal($(this));
	});

	//lista tabla
	remuneracion_cat_listar_tabla($('#_id_anio').text());
});

function _onchangeAnio(id_anio, anio){
	remuneracion_cat_listar_tabla($('#_id_anio').text());
}

function remuneracion_cat_eliminar(link){
	_DELETE({url:'api/remuneracionCat/' + $(link).data("id"),
			success:function(){
					remuneracion_cat_listar_tabla($('#_id_anio').text());
				}
			});
}

function remuneracion_cat_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/rrhh/rhh_remuneracion_cat_modal.html');
	remuneracion_cat_modal(link);
	
}

function remuneracion_cat_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('rhh_remuneracion_cat-frm');
		//alert(id_anio);
		$('#id_anio_rem').val($('#_id_anio').text());
		$('#rhh_remuneracion_cat-frm #btn-agregar').on('click',function(event){
			$('#rhh_remuneracion_cat-frm #id').val('');
			_POST({form:$('#rhh_remuneracion_cat-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/remuneracionCat/' + link.data('id'),
				  success:	function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('cat_linea_carrera',$('#id_lcarr'), data.id_lcarr);
				_llenarCombo('cat_denominacion',$('#id_cden'), data.id_cden);
				_llenarCombo('cat_categoria_ocupacional',$('#id_cocu'), data.id_cocu);
				  }
				}
			);
		}else{
			_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('cat_linea_carrera',$('#id_lcarr'));
			_llenarCombo('cat_denominacion',$('#id_cden'));
			_llenarCombo('cat_categoria_ocupacional',$('#id_cocu'));
			$('#rhh_remuneracion_cat-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		remuneracion_cat_listar_tabla($('#_id_anio').text());
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Remuneracion Categoria Ocupacional';
	else
		titulo = 'Nuevo  Remuneracion Categoria Ocupacional';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function remuneracion_cat_listar_tabla(id_anio){
	_GET({ url: 'api/remuneracionCat/listar/'+id_anio,
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#rhh_remuneracion_cat-tabla').dataTable({
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
							{"title":"Remuneracion", "data" : "rem"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Linea Carrera", "data": "lineaCarrera.nom"}, 
							{"title":"Denominacion", "data": "denominacion.nom"}, 
							{"title":"Categoria Ocupacional", "data": "categoriaOcupacional.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="remuneracion_cat_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="remuneracion_cat_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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
		});

}

