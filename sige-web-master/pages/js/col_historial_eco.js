//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_historial_eco_modal.html" id="col_historial_eco-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Historial Economico Alumno</a></li>');

	$('#col_historial_eco-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		historial_eco_modal($(this));
	});

	//lista tabla
	historial_eco_listar_tabla();
});

function historial_eco_eliminar(link){
	_delete('api/historialEco/' + $(link).data("id"),
			function(){
					historial_eco_listar_tabla();
				}
			);
}

function historial_eco_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_modal.html');
	historial_eco_modal(link);
	
}

function historial_eco_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_historial_eco-frm');
		
		$('#col_historial_eco-frm #btn-agregar').on('click',function(event){
			$('#col_historial_eco-frm #id').val('');
			_post($('#col_historial_eco-frm').attr('action') , $('#col_historial_eco-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/historialEco/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('mat_matricula',$('#id_mat'), data.id_mat);
				_llenarCombo('cat_central_riesgo',$('#id_ccr'), data.id_ccr);
				_llenarCombo('cat_central_riesgo',$('#id_ccr'), data.id_ccr);
			});
		}else{
			_llenarCombo('mat_matricula',$('#id_mat'));
			_llenarCombo('cat_central_riesgo',$('#id_ccr'));
			_llenarCombo('cat_central_riesgo',$('#id_ccr'));
			$('#col_historial_eco-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		historial_eco_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Historial Economico Alumno';
	else
		titulo = 'Nuevo  Historial Economico Alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function historial_eco_listar_tabla(){
	_get('api/historialEco/listar/',
			function(data){
			console.log(data);
				$('#col_historial_eco-tabla').dataTable({
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
							{"title":"Utlima Mensualidad Cancelada", "data" : "ult_men"}, 
							{"title":"N&uacute;mero de Mensualidades que adeuda ", "data" : "nro_mens"}, 
							{"title":"Ingresos Familiares", "data" : "ing_fam"}, 
							{"title":"Puntaje", "data" : "puntaje"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Central Riesgo Padre", "data": "centralRiesgo.nom"}, 
							{"title":"Central Riesgo Madre", "data": "centralRiesgo.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

