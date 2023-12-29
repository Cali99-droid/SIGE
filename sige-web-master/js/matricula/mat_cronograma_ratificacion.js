//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_cronograma_ratificacion_modal.html" id="mat_cronograma_ratificacion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Cronograma Ratificacion</a></li>');

	$('#mat_cronograma_ratificacion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cronograma_ratificacion_modal($(this));
	});

	//lista tabla
	cronograma_ratificacion_listar_tabla();
});

function cronograma_ratificacion_eliminar(link){
	_DELETE({url:'api/cronogramaRatificacion/' + $(link).data("id"),
			success:function(){
					cronograma_ratificacion_listar_tabla();
				}
			});
}

function cronograma_ratificacion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_cronograma_ratificacion_modal.html');
	cronograma_ratificacion_modal(link);
	
}

function cronograma_ratificacion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_cronograma_ratificacion-frm');
		
		$('#mat_cronograma_ratificacion-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma_ratificacion-frm #id').val('');
			_POST({form:$('#mat_cronograma_ratificacion-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/cronogramaRatificacion/' + link.data('id'),
				  success:	function(data){
					_fillForm(data,$('#modal').find('form') );
					_llenarCombo('col_anio',$('#id_anio_rat'), data.id_anio_rat);
					$('#fec_ini').val(_parseDate(data.fec_ini));
					$('#fec_fin').val(_parseDate(data.fec_fin));
					$('.daterange-single').daterangepicker({ 
						singleDatePicker: true,
						locale: { format: 'DD/MM/YYYY'},
					});
				 }
				}
			);
		}else{
			_llenarCombo('col_anio',$('#id_anio_rat'));
			$('#mat_cronograma_ratificacion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_ratificacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma Ratificacion';
	else
		titulo = 'Nuevo  Cronograma Ratificacion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cronograma_ratificacion_listar_tabla(){
	_GET({ url: 'api/cronogramaRatificacion/listar/',
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#mat_cronograma_ratificacion-tabla').dataTable({
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
							{"title":"Fecha Inicio", "data" : "fec_ini"}, 
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_ratificacion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_ratificacion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

