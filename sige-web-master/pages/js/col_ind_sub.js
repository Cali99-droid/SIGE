//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_ind_sub_modal.html" id="col_ind_sub-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Indicador Subtema</a></li>');

	$('#col_ind_sub-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		ind_sub_modal($(this));
	});

	//lista tabla
	ind_sub_listar_tabla();
});

function ind_sub_eliminar(link){
	_delete('api/indSub/' + $(link).data("id"),
			function(){
					ind_sub_listar_tabla();
				}
			);
}

function ind_sub_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_ind_sub_modal.html');
	ind_sub_modal(link);
	
}

function ind_sub_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_ind_sub-frm');
		
		$('#col_ind_sub-frm #btn-agregar').on('click',function(event){
			$('#col_ind_sub-frm #id').val('');
			_post($('#col_ind_sub-frm').attr('action') , $('#col_ind_sub-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/indSub/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_indicador',$('#id_ind'), data.id_ind);
				_llenarCombo('col_curso_subtema',$('#id_ccs'), data.id_ccs);
			});
		}else{
			_llenarCombo('col_indicador',$('#id_ind'));
			_llenarCombo('col_curso_subtema',$('#id_ccs'));
			$('#col_ind_sub-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ind_sub_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador Subtema';
	else
		titulo = 'Nuevo  Indicador Subtema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function ind_sub_listar_tabla(){
	_get('api/indSub/listar/',
			function(data){
			console.log(data);
				$('#col_ind_sub-tabla').dataTable({
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
							{"title":"Indicador", "data": "indicador.nom"}, 
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_sub_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_sub_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

