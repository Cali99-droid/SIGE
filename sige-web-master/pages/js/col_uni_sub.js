//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_uni_sub_modal.html" id="col_uni_sub-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Unidad Subtema</a></li>');

	$('#col_uni_sub-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		uni_sub_modal($(this));
	});

	//lista tabla
	uni_sub_listar_tabla();
});

function uni_sub_eliminar(link){
	_delete('api/uniSub/' + $(link).data("id"),
			function(){
					uni_sub_listar_tabla();
				}
			);
}

function uni_sub_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_uni_sub_modal.html');
	uni_sub_modal(link);
	
}

function uni_sub_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_uni_sub-frm');
		
		$('#col_uni_sub-frm #btn-agregar').on('click',function(event){
			$('#col_uni_sub-frm #id').val('');
			_post($('#col_uni_sub-frm').attr('action') , $('#col_uni_sub-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/uniSub/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_curso_unidad',$('#id_uni'), data.id_uni);
				_llenarCombo('col_curso_subtema',$('#id_ccs'), data.id_ccs);
			});
		}else{
			_llenarCombo('col_curso_unidad',$('#id_uni'));
			_llenarCombo('col_curso_subtema',$('#id_ccs'));
			$('#col_uni_sub-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		uni_sub_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Subtema';
	else
		titulo = 'Nuevo  Unidad Subtema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function uni_sub_listar_tabla(){
	_get('api/uniSub/listar/',
			function(data){
			console.log(data);
				$('#col_uni_sub-tabla').dataTable({
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
							{"title":"Unidad Acad&eacute;mica", "data": "cursoUnidad.nom"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="uni_sub_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="uni_sub_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

