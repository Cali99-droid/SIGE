//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_ind_eva_modal.html" id="not_ind_eva-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Indicador Evaluacion</a></li>');

	$('#not_ind_eva-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		ind_eva_modal($(this));
	});

	//lista tabla
	ind_eva_listar_tabla();
});

function ind_eva_eliminar(link){
	_delete('api/indEva/' + $(link).data("id"),
			function(){
					ind_eva_listar_tabla();
				}
			);
}

function ind_eva_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_ind_eva_modal.html');
	ind_eva_modal(link);
	
}

function ind_eva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_ind_eva-frm');
		
		$('#not_ind_eva-frm #btn-agregar').on('click',function(event){
			$('#not_ind_eva-frm #id').val('');
			_post($('#not_ind_eva-frm').attr('action') , $('#not_ind_eva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/indEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('not_evaluacion',$('#id_ne'), data.id_ne);
				_llenarCombo('col_ind_sub',$('#id_cis'), data.id_cis);
			});
		}else{
			_llenarCombo('not_evaluacion',$('#id_ne'));
			_llenarCombo('col_ind_sub',$('#id_cis'));
			$('#not_ind_eva-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ind_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador Evalaucion';
	else
		titulo = 'Nuevo  Indicador Evalaucion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function ind_eva_listar_tabla(){
	_get('api/indEva/listar/',
			function(data){
			console.log(data);
				$('#not_ind_eva-tabla').dataTable({
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
							{"title":"Evaluacion", "data": "evaluacion.ins"}, 
							{"title":"Indicador Subtema", "data": "indSub.id_ind"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_eva_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_eva_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

