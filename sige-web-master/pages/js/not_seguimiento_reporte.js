//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
function onloadPage(params) {
		
	
	$('#id_cpu').on('change',function(event) {
				var param = {
					id_au : $('#id_au').val(),
					nump : $('#id_cpu').val(),
					id_anio : $('#_id_anio').text(),
					id_cur: $('#id_cur').val()
				};
				_llenarComboURL('api/nota/listarEvaluaciones', $('#id_eva'),null, param);
			});


	$('#id_niv').on('change',
			function(event) {
			var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text()};

				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
						$('#id_cpu'), null, param1, function() {
							$('#id_cpu').change();
						});
			});

	var param = {
			id_tra : 0,
			id_anio : $('#_id_anio').text()
		};
	_llenarComboURL('api/evaluacion/listarNiveles', $('#id_niv'),
			null, param, function() {
				$('#id_niv').change();
			});

	$('#buscar-frm').on('submit', function(e) {
		e.preventDefault();
		_param = {};
		_param.id_cpu = $('#id_cpu').val();
		_param.id_niv = $('#id_niv').val();
		_param.id_anio = $('#_id_anio').text();
		
		console.log(param);

		$('#not_nota-div').show();
		_get('api/seguimientoDoc/reporteEntregaLibretas', function(data) {
			console.log(data);
		
			var columns = [ {"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title": "Alumno","data": "alumno"},
							{"title": "Grado","data": "grado"},
							{"title": "Secc.","data": "secc"},
							{"title": "Familiar","data": "familiar"},
							{"title": "Par.","data": "parentesco"},
							{"title": "Fecha","data": "fecha"}
				];

			
			if ( $.fn.DataTable.isDataTable('#reporte-tabla') ) {
				  $('#reporte-tabla').DataTable().destroy();
				  console.log('tabla destruida');
				  $('#reporte-tabla').html('');
				}

			
			console.log( data);
			$('#reporte-tabla').dataTable({
				    data : data,
				    columns:columns,
					pageLength: 50,
					aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,		 
			    "initComplete": function( settings ) {
			    	
			    	  
				 }
		    });
			
			
			
			
		}, _param);

	});

}


// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();



	$('#not_nota-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		nota_modal($(this));
	});

	// lista tabla
	//nota_listar_tabla();
});

