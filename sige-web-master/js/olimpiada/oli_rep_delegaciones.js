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
		
		_GET({url:'api/reporte/inscritosDelegacion',
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
						 scrollX: true,
				         columns : [ 
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
								{"title":"Código", "data" : "codigo"}, 
								{"title":"Colegio", "data" : "colegio"}, 
								{"title":"Gestión", "data" : "gestion"}, 
								{"title":"Departamento", "data" : "departamento"}, 
								{"title":"Provincia", "data" : "provincia"}, 
								{"title":"Distrito", "data" : "distrito"}, 
								{"title":"Director", "data" : "director"}, 
								{"title":"Delegado", "data" : "delegado"}, 
								{"title":"Correo", "data" : "corr"}, 
								{"title":"Password", "data" : "psw"}, 
								{"title":"Celular", "data" : "celular"}, 
								{"title":"Participantes", "data" : "tota_inscritos"},
								{"title": "Re-enviar ", "render":function ( data, type, row,meta ) {
									if(row.corr==null || row.corr=='')
										return "";
									else
										return "<a title=\"REENVIAR LOS PDF AL CORREO\" href=\"javascript:reenviar('" + row.id + "')\"><i class='fa fa-envelope-o'></i></a>" 
								}},
								{"title":"Ficha", "render":function ( data, type, row,meta ) {
									return "<a href=\"javascript:descargarFicha(" + row.id + ",'" + row.colegio + "')\"><i class='icon-file-pdf ui-blue'></i></a>" 
								}}, 
								{"title":"Carnet", "render":function ( data, type, row,meta ) {
									return "<a href=\"javascript:descargarCarnet(" + row.id + ",'" + row.colegio + "')\"><i class='icon-file-pdf ui-blue'></i></a>" 
								}}, 
								{"title":"Resultados", "render":function ( data, type, row,meta ) {
									return "<a href=\"javascript:descargarResultados(" + row.id + ",'" + row.colegio + "','"+row.codigo+"')\"><i class='icon-file-pdf ui-blue'></i></a>" 
								}}, 
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
						 }
				    });
			  }
			});
	});
}
//REENVIAR CORREO
function reenviar(id){
	_POST({url:'/api/public/inscripcionDel/reenviar/' + id, context:_URL_OLI});
}


function descargarCarnet(id,colegio){
	
	_download(_URL_OLI+'api/public/inscripcionInd/imprimirCarnet?tipo=D&id_ins='+id,'CARNET_' + colegio + '.pdf');
	
}

function descargarFicha(id,colegio){
	
		_download(_URL_OLI+'api/public/inscripcionDel/imprimirFichaIns?id_od='+id,'FICHA_' + colegio + '.pdf');
	
}

function descargarResultados(id,colegio, cod){
	
		_download(_URL_OLI+'api/public/resultados/pdfResultados?id_oli='+id+'&cod_mod=' +cod,'resultados.pdf');
	
}