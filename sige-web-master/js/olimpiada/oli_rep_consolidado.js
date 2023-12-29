//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		//tabla : 'oli_config',
		url : 'api/config/listarOlimpiadasxAnio',
		params: param,
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
		});
	} else{
		var param={id_anio:$("#_id_anio").text(),id_usr:_usuario.id};

		_llenar_combo({
			url : 'api/config/listarOliUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}
	var validator = $('#frm-reporte').validate();
	

	$('#frm-reporte').on('submit', function(event){
		event.preventDefault();
		$('#id_anio').val($('#_id_anio').text());
		if ($(this).valid())
		_GET({url:'api/reporte/consolidado',
			  params: $(this).serialize(),
			  context : _URL_OLI,
			  success:function(data){
				  if ( $.fn.DataTable.isDataTable('#tabla-reporte') ) {
					  $('#tabla-reporte').DataTable().destroy();
					}
				  
				  var columns = [];
				  if ($('#modalidad').val()==1)
					  columns = [ 
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title":"Nivel", "data" : "nivel"}, 
						{"title":"Grado", "data": "grado"}, 
						{"title":"Sección", "data": "secc"},
						{"title":"inscritos", "className" : "sum", "data": "inscritos"},
						{"title":"Participantes","className" : "sum text-center", "data": "pagados"}
						];
				  else
					  columns = [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nivel", "data" : "nivel"}, 
							{"title":"Grado", "data": "grado"}, 
							{"title":"inscritos", "className" : "sum","data": "inscritos"},
							{"title":"Participantes", "className" : "sum","data": "pagados"}
							];
				  
				  $('#panel-inscritos').show();
				  
				  $('#tabla-reporte').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 scrollX: true,
						 bPaginate: false,
						 bFilter: false,
					     bInfo : false,
				         columns :columns ,
				         "footerCallback": function(row, data, start, end, display) {
			                  var api = this.api();
			                 var rcnt=0;
			                  api.columns('.sum', {
			                    page: 'current'
			                  }).every(function() {
			                    var sum = this
			                      .data()
			                      .reduce(function(a, b) {
			                        var x = parseFloat(a) || 0;
			                        var y = parseFloat(b) || 0;
			                        return x + y;
			                      }, 0);
			                    console.log(sum); //alert(sum);
/*
			                    if(rcnt==0){
			                        $("#foot").append('<td style="background:#a1eaed;color:black; text-align: center;">Total</td>');
			                    }else{
			                        $("#foot").append('<td style="background:#a1eaed;color:black; text-align: center;">'+sum+'</td>');
			                    }
			                    */
			                    $("#total_parti").html(sum);
			                    rcnt++;
			                    //$(this.footer()).html(sum);
			                  });
			                }
				  
			});
		}
	});
		
		
  });
	
	
	
}