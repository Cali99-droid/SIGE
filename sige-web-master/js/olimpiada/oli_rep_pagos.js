//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

	_onloadPage();	

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

	_llenar_combo({
		tabla : 'cat_tipo_inscripcion',
		combo : $('#id_ti'),
		context : _URL_OLI
	});

	_llenar_combo({
		tabla : 'cat_gestion',
		combo : $('#id_cg'),
		context : _URL_OLI
	});

	$('#id_oli').on('change', function() {
		if($(this).val()==''){
			$(this).find('#id_niv').not(':first').remove();
			$(this).find('#id_gra').not(':first').remove();
		}else
			_llenar_combo({
				url : 'api/config/listarNiveles/' + $(this).val(),
				combo : $('#id_niv'),
				context : _URL_OLI,
				funcion : function() {
					$('#id_niv').change();
				}
			});
	});

	$('#id_niv').on('change',function() {
			if($(this).val()=='' ||  $('#id_oli').val()=='')
				$(this).find('#id_gra').not(':first').remove();
			else
				_llenar_combo({
					url : 'api/config/listarGrados/' + $('#id_oli').val()+ '/' + $(this).val(),
					combo : $('#id_gra'),
					context : _URL_OLI
				});
			});

	
	$('#frm-reporte').on('submit', function(event){
		event.preventDefault();
		//alert();
		$("#id_suc").val($("#_id_suc").html());

		_GET({url:'api/reporte/pagos',
			  params: $(this).serialize(),
			  context : _URL_OLI,
			  success:function(data){
				  $('#panel-inscritos').show();
				  $('#tabla-reporte').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
//						 scrollX: true,
						 //i.id,i.nro_dni, CONCAT(i.ape_pat, ' ' , i.ape_mat, ', ', i.nom ) alumno, "
							//+ "\n dep.nom departamento, pro.nom provincia, dist.nom distrito,"
							//+ "\n concat(ape_pat_dir, ' ' ,ape_mat_dir, ', ',nom_dir) director,"
							//+ "\n concat(ape_pat_del, ' ', ape_mat_del, ', ',nom_del) delegado,"
							//+ "\n p.monto, date(p.fec_ins) fec
				         columns : [ 
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
								//{"title":"DNI", "data" : "nro_dni"}, 
				        	 	{"title":"Alumno", "data": "alumno"}, 
				        	 	{"title":"Modalidad", "data": "modalidad"},
								{"title":"Colegio", "data": "colegio"},
				        	 	{"title":"Nivel", "data": "nivel"},
								{"title":"Grado", "data": "grado"},
								{"title":"Monto", "data": "monto","render":function ( data, type, row,meta ) { return "S/"  + _formatMonto(data);} },
								
								{"title":"Fecha", "data": "fec","render":function ( data, type, row,meta ) { return    _parseDate(data);} },
					    ],
					    "initComplete": function( settings ) {
							  // _initCompleteDT(settings);
							   _dataTable_total(settings,'monto');
						 }
				    });
			  }
			});
	});
}
//REENVIAR CORREO
function reenviar(id){
	_POST({url:'/api/public/inscripcionInd/reenviar/' + id, context:_URL_OLI});
}