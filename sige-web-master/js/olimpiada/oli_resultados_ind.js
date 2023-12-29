//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {
	
	console.log(params);
	var param={id_oli:params.id_oli};
	_GET({url:'api/public/config/ontenerDatosConcurso',
		params: param,
		context:_URL_OLI,
		success:function(data){
			console.log(data);
			if(data!=null){
				$('#nombre').html(data.nom);
				$('#fecha').html('Fecha del concurso: '+_parseDate(data.fec));
			} 
		}

	});	
	
	
	$('#oli_resultados-frm').on('submit',function(e){
		e.preventDefault();
		$('#id_oli').val(params.id);
		_GET({
			url:'api/public/resultados/dni',
			params:$('#oli_resultados-frm').serialize(),
			context:_URL_OLI,
			success:function(data){
				if (data==null){
					$('#divVacio').show();
					$('#divResultados').hide();
					$('#divPago').hide();
				}else{
					_GET({
						url:'api/public/resultados/obtenerPagoParticipante',
						params:$('#oli_resultados-frm').serialize(),
						context:_URL_OLI,
						success:function(data1){
							//if(data1==null){
							/*	$('#divPago').show();
								$('#divResultados').hide();
								$('#divVacio').hide();*/
							//}else{
								if(data.puntaje!=null){
									$('#puntaje').val(data.puntaje);
									$('#puesto').val(data.puesto);
									$('#hor_ing').val(data.hor_ing);
									$('#hor_sal').val(data.hor_sal);
									$('#total_buenas').val(data.total_buenas);
									$('#total_malas').val(data.total_malas);
									$('#total_blanco').val(data.total_blanco);
								}else{
									$('#puntaje').val('-');
									$('#puesto').val('-');
									$('#hor_ing').val('-');
									$('#hor_sal').val('-');
									$('#total_buenas').val('-');
									$('#total_malas').val('-');
									$('#total_blanco').val('-');
								}
								$('#divResultados').show();
								$('#divVacio').hide();
								$('#divPago').hide();
								$('#nom').val(data.nom);	
								$('#ape_pat').val(data.ape_pat);
								$('#ape_mat').val(data.ape_mat);
								$('#colegio').val(data.colegio);
								$('#nivel').val(data.nivel);
								$('#grado').val(data.grado);
								if(data.id_ti==1)
									$('#modalidad').html('MODALIDAD INTERNO');
								else
									$('#modalidad').html('MODALIDAD ' + data.gestion);
								var param ={id_gra:data.id_gra, id_ti: data.id_ti, id_cg:data.id_cg, id_oli:data.id_oli};
								_GET({
									url:'api/public/resultados/cantParticipantes',
									params:param,
									context:_URL_OLI,
									success:function(data){
										
										$('#total').val(data.cant_participantes);	
									}
								});
								
								var param={id_ins:data.id};
								_GET({
									url:'api/public/resultados/listarExamenDetalle',
									params: param ,
									context:_URL_OLI,
									success:function(data){
										console.log(data);
										  $('#tb_detalle').dataTable({
												 data : data,
												 aaSorting: [],
												 destroy: true,
												 orderCellsTop:true,
												 select: true,
												 scrollX: true,
												 pageLength: 50,
												 searching: false,
												 paging: false, 
												 info: false,
										         columns : [ 
										        	 	
														{"title":"Pregunta", "data" : "nro_pre","className": "text-center"}, 
														{"title":"Clave", "data" : "res","className": "text-center"}, 
														{"title":"Rpta. del Alumno", "data" : "mar","className": "text-center"},
														{"title":"Estado", "data" : "puntaje","render": function ( data, type, row,meta ) {
															console.log(row.flag);
													    	   if(row.flag==1)
													    		   return '<font color="blue">CORRECTA</font>';
													    	   else if(row.flag==0)
													    		   return '<font color="red">INCORRECTA</font>';
													    	   else if(row.flag==3)
													    		   return 'EN BLANCO';
													    	   else
													    		   return null;
													       }},
											    ],
											    "initComplete": function( settings ) {
													   _initCompleteDT(settings);
												 }
										    });	
									}
								});
							//}
						}
					});
					
				}
				
			}
		});
	});
	
	$('#btn-cancelar').on('click',function(event){
		var id_oli= $("#id_oli").val();
		var param={};
		param.id=id_oli;
		//param.tip='D';
		_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
	});
}
