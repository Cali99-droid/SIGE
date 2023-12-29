//Se ejecuta cuando la pagina q lo llama le envia parametros
var _id_gpf=null;
var _ape_pat=null;
var _ape_mat=null;
var rol=null;
function onloadPage(params){
	_onloadPage();
	console.log(params);
	
	//$("#li-tabs-1").attr
	
	if($("[name='tip_busqueda']:checked").length>0){
		var valor=$('input[name="tip_busqueda"]:checked').val();
		//alert(valor);
		llenarTabla(valor);
	}	
	
	$('input[name="tip_busqueda"]').change(function() {	
		var valor=$('input[name="tip_busqueda"]:checked').val();
		//var param={tipBusqueda:valor};
		//alert(valor);
		llenarTabla(valor);
	});
	console.log(_usuario);	
	var id_usr=_usuario.id;
	rol = _usuario.roles[0];
		
}	
								
	//$("[name='tip_busqueda']").change(function () {
	
	function llenarTabla(valor){
		//if(valor=='A')
			//valor='A'
				var param={tipBusqueda:valor, id_anio:$('#_id_anio').text(), id_gir:null, id_suc: $("#_id_suc").text(), id_niv:null};
				_get('api/alumno/busquedaAlumnos',function(data){
					$('#tabla-alumnos').dataTable({
					 data : data,
					 /*
					 destroy: true,
					 select: true,
					 bFilter: false,
					 */
					 searching: true, 
					 paging: false, 
					 info: false,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [ 
			        	 	//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Codigo", "data": "cod"},
							{"title":"Alumno", "data": "alumno"},
	    	        ],
	    	        "initComplete": function( settings ) {
					_initCompleteDT(settings);
					$("#total").text(data.length);
					$("#tabla-alumnos tbody tr").click(function(){
					$("#id_anio").val($('#_id_anio').text());
					   $(this).addClass('selected').siblings().removeClass('selected');    
					   var codigo=$(this).find('td:nth-child(1)').html();
					   _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   $("#alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						    $("#frm-pagos-resumen #id_alu").val(data.id_alu);
							//$('#tabla-detalle_pagos').html('');
							deudas_pendientes(data.id_alu);
							listaPagos(data.id_alu);
							listaDevoluciones(data.id_alu);
							//Cargamos los datos
						   $('#id_per').on('change',function(event){
								var parentesco = $('#id_per option:selected').attr('data-aux1');
								var celular = $('#id_per option:selected').attr('data-aux2');
								var nro_doc= $('#id_per option:selected').attr('data-aux3');
								var correo = $('#id_per option:selected').attr('data-aux5');
								var direccion = $('#id_per option:selected').attr('data-aux6');
								//$('#id_fam_par').val(parentesco);
								$('#cel').val(celular);
								$('#nro_doc').val(nro_doc);
								$('#direccion').val(direccion);
																
							});
																					
							var param={id_gpf:data.id_gpf, id_alu:data.id_alu};
							_llenarComboURL('api/alumno/listarTodosIntegrantesFamilia', $('#id_per'),
								null, param, function() {
									$('#id_per').change();
							});
															
					   });
					}
					);

	    			 }
			});
		},param);	
	}



function deudas_pendientes(id_alu){
	$('#tabla-deudas_pendientes').html('');
	
	//Lista de dudas pendientes
	 _get('api/pagos/todospagosPendientesAlumno/'+id_alu+'/'+$("#_id_suc").text(),function(data){
		 console.log(data);
		 //$('#tabla-detalle_pagos').html('');
		 $('#tabla-deudas_pendientes').dataTable({
		 data : data,
		 aaSorting: [],
		 destroy: true,
		 orderCellsTop:true,
		 searching: true, 
		 paging: false, 
		 info: false,
		 select: true,
		 columns : [ 
				{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				//{"title":"Año", "data" : "anio"}, 
				/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					return "<label class='checkbox-inline'><input type='checkbox' id='id_fac" + row.id +"' name='id_fac' value='"+row.id+"'/></label>"; 
					} 
				},*/
				{"title":"Año", "render": function ( data, type, row ) {
					return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><label id="anio_nom">'+row.anio+'</label>'
				}},
				{"title":"Mes", "render": function ( data, type, row ) {
					if(row.matricula.tipo=='A' || row.matricula.tipo=='V')
						return '<input type="hidden" id="mens" name="mens" value="'+row.mens+'"/><label id="mes"></label>';
					else
						return '<input type="hidden" id="mens" name="mens" value="'+row.mens+'"/><label id="mes">'+row.mens+'</label>';
				}},
				{"title":"Concepto", "render": function ( data, type, row ) {
					if(row.matricula.tipo=='A')
						return '<input type="hidden" id="tipo" name="tipo" value="A"/><label id="concepto">'+row.concepto+'</label>';
					else if(row.matricula.tipo=='V')
						return '<input type="hidden" id="tipo" name="tipo" value="V"/><label id="concepto">'+row.concepto+'</label>';
					else
						return '<input type="hidden" id="tipo" name="tipo" value="C"/><label id="concepto">'+row.concepto+'</label>';
				}},
				{"title":"Monto", "render": function ( data, type, row ) {
					return '<label id="monto">'+row.monto+'</label>';
				}},
				{"title":"Dsto. Beca", "render": function ( data, type, row ) {
					return '<label id="desc_beca">'+row.desc_beca+'</label>';
				}},
				{"title":"Dsto. Her.", "render": function ( data, type, row ) {
					return '<label id="desc_hermano">'+row.desc_hermano+'</label>';
				}},
				{"title":"Dsto. Pago Ade.", "render": function ( data, type, row ) {
					return '<label id="desc_pago_adelantado">'+row.desc_pago_adelantado+'</label>';
				}},
				{"title":"Dsto. Per.", "render": function ( data, type, row ) {
					return '<label id="desc_personalizado">'+row.desc_personalizado+'</label>';
				}},
				{"title":"Monto a Pagar", "render": function ( data, type, row ) {
					//if(row.desc_beca!=null){
						var monto_total="";
						if(row.montoTotal!=null)
							monto_total=parseFloat(row.montoTotal)-parseFloat(row.desc_beca)-parseFloat(row.desc_hermano);
						else
							monto_total=parseFloat(row.monto)-parseFloat(row.desc_beca)-parseFloat(row.desc_hermano);
						return '<label id="montoTotal">'+monto_total+'</label>';
					//}	
					//return '<label id="montoTotal">'+row.montoTotal+'</label>';
				}},
				{"title":"Banco", "render":function ( data, type, row,meta ) {
						//var selected1 = (row.id_bco_pag=='N') ? 'selected': ' ';
					return "<select name='id_bco' id='id_bco"+row.id+"' data-placeholder='Seleccione' data-id_mat='"+row.id_mat+"' data-id_alu='"+id_alu+"' data-id_bco_or='"+row.id_bco_pag+"' class='form-control select-search' required onchange='seleccionarBanco(this)'><option value=''>Seleccione</option></select>"; 
				} 
				},
				{"title":"Exonerar", "render": function ( data, type, row ) {
					if (rol=='1'){
						return '<button type="button" id="btnExonerar" title="Exonerar Pago" data-id_fac="'+row.id+'" data-id_alu='+id_alu+' class="btn btn-orange btn-xs" onclick="exonerar(this,event)"><i class="icon-subtract"></i></button>';
					} else{
						return '';
					}	
					/*if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
						return '<button type="button" id="btnExonerar" title="Exonerar Pago" data-id_fac="'+row.id+'" data-id_alu='+id_alu+' class="btn btn-warning btn-xs" onclick="exonerar(this,event)"><i class="icon-subtract"></i></button>';
					} else{
						return '';
					}	*/
					
				}},
		],
		"initComplete": function( settings ) {
		   _initCompleteDTSB(settings);
		    $("#tabla-deudas_pendientes select[name='id_bco']").each(function() {
				/*_llenarCombo('fac_banco',$(this),null,null,function(){
					$(this).change();
						
				});*/
					_llenar_combo({
					url:'api/banco/listar',
					combo:$(this),
					text:'cod',
					funcion:function(){
						//$(this).change();
					}
				});
			});	
			
			
			
			
				_get('api/pagos/todospagosPendientesAlumno/'+id_alu+'/'+$("#_id_suc").text(),function(data){
					for ( var i in data) {
						$("#tabla-deudas_pendientes select[name='id_bco']").each(function() {
							$(this).val(data[i].id_bco_pag);
						});		
					}	
					//$("#tabla-deudas_pendientes select[name='id_bco']").each(function() {
					//$(this).html();
					console.log(data);
					//$(this).val(data.id_bco_pag).change();
				//});	
				});	
			


				
			$('#tabla-deudas_pendientes tr').on('dblclick', function() {
			   $(this).remove();
			   var anio=$(this).find('td:nth-child(2)').find("#anio_nom").html();
			   var lbl_anio=$(this).find('td:nth-child(2)').find("#anio_nom").text();
			   
			  // alert($(this).find('td:nth-child(2)').find("#anio_nom").text());
			   
			   var lbl_mes=$(this).find('td:nth-child(3)').find("#mes").text();
			   var concepto=$(this).find('td:nth-child(4)').html();
			   var lbl_tipo=$(this).find('td:nth-child(4)').find("#tipo").val();
			   var lbl_concepto=$(this).find('td:nth-child(4)').find("#concepto").text();
			   var monto_unit=$.number(parseFloat($(this).find('td:nth-child(5)').find("#monto").text()),2);
			   var lbl_monto_unit=$(this).find('td:nth-child(5)').find("#monto").text();
			   var cant=1;
			   var monto=$.number(parseFloat($(this).find('td:nth-child(5)').find("#monto").text()),2);
			   var lbl_monto=$(this).find('td:nth-child(5)').find("#monto").text();
			   console.log($(this).find('td:nth-child(8)').html());
			   //var desc=$.number((parseFloat($(this).find('td:nth-child(8)').find("#desc_personalizado").text())+parseFloat($(this).find('td:nth-child(6)').find("#desc_hermano").text())+parseFloat($(this).find('td:nth-child(7)').find("#desc_pago_adelantado").text())),2); //parseFloat($(this).find('td:nth-child(6)').find("#desc_hermano").text())+parseFloat($(this).find('td:nth-child(7)').find("#desc_pago_adelantado").text())
			  var desc=$.number((parseFloat($(this).find('td:nth-child(6)').find("#desc_beca").text())+parseFloat($(this).find('td:nth-child(9)').find("#desc_personalizado").text())+parseFloat($(this).find('td:nth-child(7)').find("#desc_hermano").text())+parseFloat($(this).find('td:nth-child(8)').find("#desc_pago_adelantado").text())),2); //parseFloat($(this).find('td:nth-child(6)').find("#desc_hermano").text())+parseFloat($(this).find('td:nth-child(7)').find("#desc_pago_adelantado").text())
			  var lbl_desc_beca=$(this).find('td:nth-child(6)').find("#desc_beca").text();
			   var lbl_desc_her=$(this).find('td:nth-child(7)').find("#desc_hermano").text();
			   var lbl_desc_pago_ade=$(this).find('td:nth-child(8)').find("#desc_pago_adelantado").text();
			   var lbl_desc_per=$(this).find('td:nth-child(9)').find("#desc_personalizado").text();
			   var monto_total=$.number(parseFloat($(this).find('td:nth-child(10)').find("#montoTotal").text()),2);
			   var lbl_monto_total=$(this).find('td:nth-child(10)').find("#montoTotal").text();
			   //Datos para el pago
			   var id_fac=$(this).find('td:nth-child(2)').find("#id").val();
			   var mens=$(this).find('td:nth-child(3)').find("#mens").text();
			   var lbl_desc_sec=0;
			   var cont=$('#tabla-detalle_pagos tr').length ;
			   //Buscar la regla de negoio , si pagamos pensiones por tesoreria o no
			  
				if(lbl_tipo=='C' && lbl_concepto=='Pensión'){
					 _GET({
					context:_URL,
					url:'api/pagos/pagoMensualidad',
					success:function(data){
						console.log(data);
							if(data.val!=0){
								$('#tabla-detalle_pagos').find('tbody').append(
								'<tr><td><div class="resumen" data-resumen-id="'+id_fac+'" data-mens="' + mens +'" data-monto="' + monto +'" data-descuento_beca="' + lbl_desc_beca +'" data-descuento_hermano="' + lbl_desc_her +'" data-descuento_12="' + lbl_desc_her +'" data-descuento_personalizado="' + lbl_desc_per +'" data-descuento_secretaria="' + lbl_desc_sec +'" data-descuento_pago_ade="' + lbl_desc_pago_ade +'" data-tipo_pago="' + lbl_tipo +'">' + cont + '</div></td><td>' + anio+ '</td><td>' + lbl_mes+ '</td><td>'+ concepto+ '</td><td class="monto_unit text-center">' + monto_unit+ '</td><td class="cant text-center">'+cant+'</td><td class="monto text-center">'+monto+'</td><td class="desc text-center">'+desc+'</td><td align="right" class="monto_total text-center">'+ monto_total + '</td>'+
								'<td><a href="#" data-anio_nom="' +lbl_anio + '"  data-mes="' +lbl_mes+ '" data-concepto="' +lbl_concepto+ '" data-monto_unit="' + lbl_monto_unit+ '" data-monto="' +lbl_monto+ '" data-desc_beca="'+lbl_desc_beca+'" data-desc_her="'+lbl_desc_her+'" data-desc_pago_ade="'+lbl_desc_pago_ade+'" data-desc_per="'+lbl_desc_per+'" data-monto_total="'+lbl_monto_total+'"'
								+' onclick="quitar_pago(this,event)"  class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></td></tr>');
								calcular();
							} 
						}
					});
				} else {
					 $('#tabla-detalle_pagos').find('tbody').append(
					'<tr><td><div class="resumen" data-resumen-id="'+id_fac+'" data-mens="' + mens +'" data-monto="' + monto +'" data-descuento_beca="' + lbl_desc_beca +'" data-descuento_hermano="' + lbl_desc_her +'" data-descuento_12="' + lbl_desc_her +'" data-descuento_personalizado="' + lbl_desc_per +'" data-descuento_secretaria="' + lbl_desc_sec +'" data-descuento_pago_ade="' + lbl_desc_pago_ade +'" data-tipo_pago="' + lbl_tipo +'">' + cont + '</div></td><td>' + anio+ '</td><td>' + lbl_mes+ '</td><td>'+ concepto+ '</td><td class="monto_unit text-center">' + monto_unit+ '</td><td class="cant text-center">'+cant+'</td><td class="monto text-center">'+monto+'</td><td class="desc text-center">'+desc+'</td><td align="right" class="monto_total text-center">'+ monto_total + '</td>'+
					'<td><a href="#" data-anio_nom="' +lbl_anio + '"  data-mes="' +lbl_mes+ '" data-concepto="' +lbl_concepto+ '" data-monto_unit="' + lbl_monto_unit+ '" data-monto="' +lbl_monto+ '" data-desc_beca="'+lbl_desc_beca+'" data-desc_her="'+lbl_desc_her+'" data-desc_pago_ade="'+lbl_desc_pago_ade+'" data-desc_per="'+lbl_desc_per+'" data-monto_total="'+lbl_monto_total+'"'
					+' onclick="quitar_pago(this,event)"  class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></td></tr>');
					calcular();
				}	
				
				_llenarCombo('ges_sucursal',$(this),null,null,function(){
					$(this).change();
				});
			}); 
			// Total over all pages
				//var total = api.column(5).data().sum();
				//alert(total);
		}
		});
	 });	
}


	

function seleccionarBanco(campo1){
	//Esto es por modalidad
	console.log(campo1);
	var campo =$(campo1);
	var id_bco_des = campo.val();
	var id_mat = campo.attr('data-id_mat');
	var id_alu = campo.attr('data-id_alu');
	var id_bco_or= campo.attr('data-id_bco_or');
	swal(
			{
				title : "Esta seguro?",
				text :'Se actualizará el banco para el pago de pensiones.',
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, actualizar",
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					var param={id_mat:id_mat, id_bco:id_bco_des};
					_post('api/banco/actualizarBancoAlu',param, function(data){
						 console.log(data.result);
						deudas_pendientes(id_alu);
					});
						 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha actualizado la sección.",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
					
					campo.val(id_bco_or);//regresa a su estado anterior
				}
			});
	//var div_local=$(campo1).parent().parent();
	//console.log(div_local.html());
	//var id_sucursal= div_local.find("#id_suc"+id_alu).val();
	//console.log(id_sucursal);
	
	
//Consultar el banco de pago
}

function listaPagos(id_alu){ 
	_get('api/pagos/pagadosxAlumno/' +  id_alu,
				function(data){
					
					for (var i in data) {
						console.log(data);
						//$("#tab_pagados").append("<h5>" + data[i].alumno + "</h5><table class='table' id='tabla-pagados" + data[i].id_mat  + "'></table>");
					
						$('#tabla-pagos_alumno').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 bLengthChange: false,
							 bPaginate: false,
							 bFilter: false,
							 select: true,
					         columns : [ 
								   {"title": "Anio", "data" : "anio"}, 
								   {"title":"Recibo", "data" : "nro_rec"},
								   {"title": "Concepto", "data" : "concepto"}, 
								   {"title":"Mes", "render": function ( data, type, row ) {
									   if(row.mens==1){
										   return 'Enero';
									   } else if(row.mens==2){
										   return 'Febrero';
									   } else if(row.mens==3){
										   return 'Marzo';
									   } else if(row.mens==4){
										   return 'Abril';
									   } else if(row.mens==5){
										   return 'Mayo';
									   } else if(row.mens==6){
										   return 'Junio';
									   } else if(row.mens==7){
										   return 'Julio';
									   } else if(row.mens==8){
										   return 'Agosto';
									   } else if(row.mens==9){
										   return 'Setiembre';
									   } else if(row.mens==10){
										   return 'Octubre';
									   } else if(row.mens==11){
										   return 'Noviembre';
									   } else if(row.mens==12){
										   return 'Diciembre';
									   } else {
										   return null;
									   }
										//return row.mens;
									}},
								   {"title": "Fec. pago", "data" : "fec_pago"}, 
						           {"title":"Monto", "data" : "monto","render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}},
								   {"title":"Desc.", "data" : "descuento","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. x her.", "data" : "desc_hermano","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. pronto pago", "data" : "desc_pronto_pago","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. admi.", "data" : "desc_personalizado","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
								   //{"title":"Desc. pago ade.", "data" : "desc_pago_adelantado","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           {"title":"Monto total", "data" : "monto_total", 
						        	   "render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}
						           },
						           {"title":"Boleta", "data" : "nro_rec", 
						        	   "render":function ( data, type, row ) {
						        		   if (row.nro_rec.indexOf("B")=='0' || row.nro_rec.indexOf("F")=='0')
						        			   return '<a href="#" title="RE-IMPRESION" onclick="reimprimir(\'' +data +'\',\'' +  row.banco +'\',\''+id_alu+'\')"><i class="fa fa-print" style="color:blue"></i></a>&nbsp;&nbsp;&nbsp;<a href="#" data-id_fmo="' +  row.id_fmo + '"  data-id_alu="' + id_alu + '" onclick="pdf(event,this)" title="DESCARGA PDF"><i class="fa fa-file-pdf-o" style="color:red"></i></a>'; 
						        		   else
						        			   return '';
						        	   }
						           }
					        ]
					    });
					}
			
					
				}
		);
}	

function listaDevoluciones(id_alu){ 
	_get('api/pagos/ncxAlumno/' +  id_alu,
				function(data){
					
					for (var i in data) {
						console.log(data);
						//$("#tab_pagados").append("<h5>" + data[i].alumno + "</h5><table class='table' id='tabla-pagados" + data[i].id_mat  + "'></table>");
					
						$('#tabla-devoluciones_alumno').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 bLengthChange: false,
							 bPaginate: false,
							 bFilter: false,
							 select: true,
					         columns : [ 
								   {"title": "Anio", "data" : "anio"}, 
								   {"title":"Nota de Crédito", "data" : "nro_rec"},
								   {"title": "Nro. Recibo Afectado", "data" : "nro_rec_afec"},
								   {"title": "Concepto", "data" : "obs"}, 
								  /* {"title":"Mes", "render": function ( data, type, row ) {
									   if(row.mens==1){
										   return 'Enero';
									   } else if(row.mens==2){
										   return 'Febrero';
									   } else if(row.mens==3){
										   return 'Marzo';
									   } else if(row.mens==4){
										   return 'Abril';
									   } else if(row.mens==5){
										   return 'Mayo';
									   } else if(row.mens==6){
										   return 'Junio';
									   } else if(row.mens==7){
										   return 'Julio';
									   } else if(row.mens==8){
										   return 'Agosto';
									   } else if(row.mens==9){
										   return 'Setiembre';
									   } else if(row.mens==10){
										   return 'Octubre';
									   } else if(row.mens==11){
										   return 'Noviembre';
									   } else if(row.mens==12){
										   return 'Diciembre';
									   } else {
										   return null;
									   }
										//return row.mens;
									}},*/
								    
								   {"title": "Fecha Emitida", "data" : "fec_pago"}, 
						           {"title":"Monto Devuelto", "data" : "monto","render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}},
								  // {"title":"Desc.", "data" : "descuento","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. x her.", "data" : "desc_hermano","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. pronto pago", "data" : "desc_pronto_pago","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						           //{"title":"Desc. admi.", "data" : "desc_personalizado","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
								   //{"title":"Desc. pago ade.", "data" : "desc_pago_adelantado","render":function ( data, type, row ) {return 'S./' +$.number( data, 2);}},
						          /* {"title":"Monto total", "data" : "monto_total", 
						        	   "render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}
						           },*/
						           {"title":"Nota de Crédito", "data" : "nro_rec", 
						        	   "render":function ( data, type, row ) {
						        		   if (row.nro_rec.indexOf("B")=='0' || row.nro_rec.indexOf("F")=='0')
						        			   return '<a href="#" title="RE-IMPRESION" onclick="reimprimir(\'' +data +'\',\'' +  row.banco +'\',\''+id_alu+'\')"><i class="fa fa-print" style="color:blue"></i></a>&nbsp;&nbsp;&nbsp;<a href="#" data-id_fmo="' +  row.id_fmo + '"  data-id_alu="' + id_alu + '" onclick="pdf(event,this)" title="DESCARGA PDF"><i class="fa fa-file-pdf-o" style="color:red"></i></a>'; 
						        		   else
						        			   return '';
						        	   }
						           }
					        ]
					    });
					}
			
					
				}
		);
}	


function reimprimir(nro_rec, banco,id_alu1){
	var url;
	console.log(">" + banco  + "<-");
	/*if(banco==null ||banco=="null" || banco=="")
		url = 'api/movimiento/imprimir/mensualidad/'+ nro_rec+'/'+id_alu1;
	else*/
	//alert();
		url = 'api/banco/imprimir/mensualidad/'+ nro_rec+'/'+id_alu1;
	
	_post_silent(url ,null,
			function(data){
				_post_json( "http://localhost:8082/api/print", data.result, 
					function(data){
						console.log(data);
						//$("#_btnRefresh").click();
					}
				);
		}
	);
	
}

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	var id_alu1 = $(o).data('id_alu');
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo+"/"+id_alu1;

}


function calcular() {
    var sum_monto_unit=0;
				$('.monto_unit').each(function() {  
				 sum_monto_unit += parseFloat($(this).text());  
				}); 
				var total_monto_unit=sum_monto_unit.toFixed(2);
				$('#total_monto_unit').text(total_monto_unit);
				var sum_cant=0;
				$('.cant').each(function() {  
				 sum_cant += parseFloat($(this).text());  
				}); 
				var total_cant=sum_cant;
				$('#total_cant').text(total_cant);
				var sum_monto=0;
				$('.monto').each(function() {  
				 sum_monto += parseFloat($(this).text());  
				}); 
				var total_monto=sum_monto.toFixed(2);
				$('#total_monto').text(total_monto);
				var sum_desc=0;
				$('.desc').each(function() {  
				 sum_desc += parseFloat($(this).text());  
				}); 
				var total_desc=sum_desc.toFixed(2);
				$('#total_desc').text(total_desc);
				var sum_monto_total=0;
				$('.monto_total').each(function() {  
				 sum_monto_total += parseFloat($(this).text());  
				}); 
				var total_monto_total=sum_monto_total.toFixed(2);
				$('#total_monto_total').text(total_monto_total);
}

		   
function quitar_pago(row,e){
	e.preventDefault();
	var link = $(row);
	var id_fac=link.data('resumen-id');
	var anio_nom=link.data('anio_nom');
	var mes =link.data('mes');
	var mens = link.data('mens');
	var concepto=link.data('concepto');
	var monto_uni=link.data('monto_unit');
	var monto=link.data('monto');
	var desc_beca=link.data('desc_beca');
	var desc_her=link.data('desc_her');
	var desc_pago_ade=link.data('desc_pago_ade');
	var desc_per=link.data('desc_per');
	var desc_sec=link.data('descuento_secretaria');
	var tipo_pago=link.data('tipo_pago');
	var monto_total=link.data('monto_total');
	//var desc=$.number((parseFloat(desc_her)+parseFloat(desc_pago_ade)+parseFloat(desc_per)),2); //parseFloat($(this).find('td:nth-child(6)').find("#desc_hermano").text())+parseFloat($(this).find('td:nth-child(7)').find("#desc_pago_adelantado").text())
	console.log(row.parentNode);
	row.parentNode.parentNode.remove();
	 var cont=$('#tabla-deudas_pendientes tr').length -1;
	 var cant=1;
	 calcular();
	 $('#tabla-deudas_pendientes').find('tbody').append(
	'<tr><td>' + cont + '</td><td>' + anio_nom+ '</td><td>' + mes+ '</td><td>'+ concepto+ '</td><td>'+monto+'</td><td>'+desc_beca+'</td><td>'+desc_her+'</td><td>'+desc_pago_ade+'</td><td>'+desc_per+'</td><td>'+ monto_total + '</td></tr>');
	/*$('tr').on('dblclick', function() {
			   $(this).remove();
			    var desc=$.number((parseFloat(desc_beca)+parseFloat(desc_her)+parseFloat(desc_pago_ade)+parseFloat(desc_per)),2); 
			   var cont=$('#tabla-detalle_pagos tr').length ;
			$('#tabla-detalle_pagos').find('tbody').append(
			'<tr><td><div class="resumen" data-resumen-id="'+id_fac+'" data-mens="' + mens +'" data-monto="' + monto +'" data-descuento_beca="' + desc_beca +'" data-descuento_hermano="' + desc_her +'" data-descuento_12="' + desc_her +'" data-descuento_personalizado="' + desc_per +'" data-descuento_secretaria="' + desc_sec +'" data-descuento_pago_ade="' + desc_pago_ade +'" data-tipo_pago="' + tipo_pago +'">' + cont + '</div></td><td>' + anio_nom+ '</td><td>' + mes+ '</td><td>'+ concepto+ '</td><td class="monto_unit text-center">' + monto_uni+ '</td><td class="cant text-center">'+cant+'</td><td class="monto text-center">'+monto+'</td><td class="desc text-center">'+desc+'</td><td align="right" class="monto_total text-center">'+ monto_total + '</td>'+
			'<td><a href="#" data-anio_nom="' +anio_nom + '"  data-mes="' +mes+ '" data-concepto="' +concepto+ '" data-monto_unit="' + monto_uni+ '" data-monto="' +monto+ '" data-desc_her="'+desc_her+'" data-desc_pago_ade="'+desc_pago_ade+'" data-desc_per="'+desc_per+'" data-monto_total="'+monto_total+'"'
			+' onclick="quitar_pago(this,event)"  class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></td></tr>');
	});*/		
}

function exonerar(row,e){
	e.preventDefault();
	var link = $(row);
					swal(
						{
							title : "Esta seguro?",
							text : "Se exonerará esta mensualidad, Esta seguro?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "Si, Exonerar",
							cancelButtonText : "No, cancela!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
								var id_fac=link.data('id_fac');
								var id_alu=link.data('id_alu');
								$("#btnPagar").attr("disabled", "disabled");
								_post( "api/pagos/exonerarMensualidad/"+id_fac, null, 
										function(data){
											console.log(data.result);
											deudas_pendientes(id_alu); 
										}
								);
								
								
							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado ninguna transaccion",
									confirmButtonColor : "#2196F3",
									type : "error"
								});
							}
						});
}	

$("#btnPagar").on('click',function() {
				swal(
						{
							title : "Esta seguro?",
							text : "El pago afectara a caja, Esta seguro que el cliente va a pagar?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "Si, Pagar",
							cancelButtonText : "No, cancela!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
								
								var pago_id = "";
								var pago_monto = "";
								var pago_descuento = "";
								var pago_descuento_secretaria = "";
								var pago_descuento_personalizado = "";
								var pago_descuento_pago_ade= "";
								var pago_descuento_beca= "";
								var tipo_pago = "";
								$(".resumen").each(function(index){
									pago_id = pago_id + "|" + $(this).data("resumen-id");
									pago_monto = pago_monto +"|" + $(this).data("monto");
									pago_descuento = pago_descuento + "|" + $(this).data("descuento_hermano");
									pago_descuento_secretaria = pago_descuento_secretaria + "|" + $(this).data("descuento_secretaria");
									pago_descuento_personalizado = pago_descuento_personalizado + "|" + $(this).data("descuento_personalizado");
									pago_descuento_pago_ade = pago_descuento_pago_ade + "|" + $(this).data("descuento_pago_ade");
									pago_descuento_beca = pago_descuento_beca + "|" + $(this).data("descuento_beca");
									tipo_pago = tipo_pago + "|" + $(this).data("tipo_pago");
								});
								
								$("#frm-pagos-resumen #id").val(pago_id);
								$('#frm-pagos-resumen #id_per_pag').val($('#id_per').val());
								$("#frm-pagos-resumen #id_suc").val($('#_id_suc').text());
								$("#frm-pagos-resumen #monto").val(pago_monto);
								$("#frm-pagos-resumen #descuento").val(pago_descuento);
								$("#frm-pagos-resumen #descuento_secretaria").val(pago_descuento_secretaria);
								$("#frm-pagos-resumen #descuento_personalizado").val(pago_descuento_personalizado);
								$("#frm-pagos-resumen #descuento_pago_ade").val(pago_descuento_pago_ade);
								$("#frm-pagos-resumen #descuento_beca").val(pago_descuento_beca);
								$("#frm-pagos-resumen #tipo_pago").val(tipo_pago);
								$("#btnPagar").attr("disabled", "disabled");
								_post( "api/pagos/pagarDeudasAlumno", $("#frm-pagos-resumen").serialize(), 
										function(data){
											console.log(data.result);
											 for (var i=0;i<data.result.length;i++) {
												 
												//  setTimeout(function(){
													data.result[i].cabecera.codigoBarras="PENDIENTE";
													_post_json( "http://localhost:8082/api/print", data.result[i], 
														function(data){
															console.log(data);
															//$("#_btnRefresh").click();
															
														}
													);
												//  }, 1000);
												
											 } 
											$("#btnPagar").removeAttr("disabled"); 
											deudas_pendientes($("#frm-pagos-resumen #id_alu").val());
											listaPagos($("#frm-pagos-resumen #id_alu").val());
											$("#_btnRefresh").click();
											$("#tabla-deudas_pendientes").html('');
											$("#tabla-pagos_alumno").html('');
										}
								);
								
								
							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado ninguna transaccion",
									confirmButtonColor : "#2196F3",
									type : "error"
								});
							}
						});

});


$("#btnBecar").on('click',function() {
	pagoBecaModal( $("#frm-pagos-resumen #id_alu").val(),$("#alumno").text());
});	

$("#btnBanco").on('click',function() {
	bancoAlumnoModal( $("#frm_pago_beca #id_alu").val(),$("#alumno").text());
});	

function pagoBecaModal(id_alu,alumno){
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		$('#frm_pago_beca #alumno').val(alumno);
		$('#frm_pago_beca #btn-grabar').on('click',function(event){
			event.preventDefault();
			//Desactivamos todos los combos beca
			$("#frm_pago_beca input[name='id_bec_sel']").each(function() {
				if($(this).prop('checked')==false){
					$('#id_bec' + $(this).val()).prop('disabled',true);
				}
			});
			var validation = $('#frm_pago_beca').validate(); 
			if ($('#frm_pago_beca').valid()){
				swal(
						{
							title : "Esta seguro?",
							text : "Se procederá a efectuar el descuento, Esta seguro?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "Si, Becar",
							cancelButtonText : "No, cancela!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
								_post($('#frm_pago_beca').attr('action') , $('#frm_pago_beca').serialize(),
								function(data){
								onSuccessSave(data) ;
								});
							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado ninguna transaccion",
									confirmButtonColor : "#2196F3",
									type : "error"
								});
							}
						});
				//}	
			}		
		});
		
		listarDeudasPendientesaBecar(id_alu);
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		listarDeudasPendientesaBecar(id_alu);
		//listaPagos(id_alu);
		deudas_pendientes(id_alu);
	}
	//Abrir el modal
	var titulo;
	//if (link.data('id'))
		titulo = 'Registro de Becas';
	/*else
		titulo = 'Nuevo  Area';*/
	
	_modal_full(titulo, 'pages/tesoreria/fac_pago_beca_modal.html',onShowModal,onSuccessSave);

	
}

function bancoAlumnoModal(id_alu,alumno){
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		$('#frm_pago_beca #alumno').val(alumno);
		_llenarCombo('fac_banco',$('#id_bco'),_id_bco_pag,null,function(){});
		
		$('#frm_pago_beca #btn-grabar').on('click',function(event){
			event.preventDefault();
			//Desactivamos todos los combos beca
			$("#frm_pago_beca input[name='id_bec_sel']").each(function() {
				if($(this).prop('checked')==false){
					$('#id_bec' + $(this).val()).prop('disabled',true);
				}
			});
			var validation = $('#frm_pago_beca').validate(); 
			if ($('#frm_pago_beca').valid()){
				swal(
						{
							title : "Esta seguro?",
							text : "Se procederá a efectuar el descuento, Esta seguro?",
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : "rgb(33, 150, 243)",
							cancelButtonColor : "#EF5350",
							confirmButtonText : "Si, Becar",
							cancelButtonText : "No, cancela!",
							closeOnConfirm : false,
							closeOnCancel : false
						},
						function(isConfirm) {
							if (isConfirm) {
								_post($('#frm_pago_beca').attr('action') , $('#frm_pago_beca').serialize(),
								function(data){
								onSuccessSave(data) ;
								});
							} else {
								swal({
									title : "Cancelado",
									text : "No se ha realizado ninguna transaccion",
									confirmButtonColor : "#2196F3",
									type : "error"
								});
							}
						});
				//}	
			}		
		});
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		listarDeudasPendientesaBecar(id_alu);
		//listaPagos(id_alu);
		deudas_pendientes(id_alu);
	}
	//Abrir el modal
	var titulo;
	//if (link.data('id'))
		titulo = 'Registro de Becas';
	/*else
		titulo = 'Nuevo  Area';*/
	
	_modal_full(titulo, 'pages/tesoreria/fac_pago_beca_modal.html',onShowModal,onSuccessSave);

	
}

function listarDeudasPendientesaBecar(id_alu){
				//Lista de dudas pendientes
	 _get('api/pagos/todospagosPendientesAluNoAfectadoBeca/'+id_alu+'/'+$("#_id_suc").text(),function(data){
		 console.log(data);
		 //$('#tabla-detalle_pagos').html('');
		 $('#tabla_deudas').dataTable({
		 data : data,
		 aaSorting: [],
		 destroy: true,
		 orderCellsTop:true,
		 searching: false, 
		 paging: false, 
		 info: false,
		 select: true,
		 columns : [ 
				//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				{"title":"Año", "render": function ( data, type, row ) {
					return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><label id="anio_nom">'+row.anio+'</label>'
				}},
				{"title":"Mes", "render": function ( data, type, row ) {
					if(row.matricula.tipo=='A' || row.matricula.tipo=='V')
						return '<input type="hidden" id="mens" name="mens" value="'+row.mens+'"/><label id="mes"></label>';
					else
						return '<input type="hidden" id="mens" name="mens" value="'+row.mens+'"/><label id="mes">'+row.mens+'</label>';
				}},
				{"title":"Concepto", "render": function ( data, type, row ) {
					if(row.matricula.tipo=='A')
						return '<input type="hidden" id="tipo" name="tipo" value="A"/><label id="concepto">'+row.concepto+'</label>';
					else if(row.matricula.tipo=='V')
						return '<input type="hidden" id="tipo" name="tipo" value="V"/><label id="concepto">'+row.concepto+'</label>';
					else
						return '<input type="hidden" id="tipo" name="tipo" value="C"/><label id="concepto">'+row.concepto+'</label>';
				}},
				{"title":"Monto", "render": function ( data, type, row ) {
					if(row.montoTotal!=null)
						return "<label id='montoTotal'>"+row.montoTotal+"</label><input type='hidden' disabled id='monto"+ row.id +"' name='monto' value='"+row.montoTotal+"' disabled />";
					else
						return "<label id='montoTotal'>"+row.monto+"</label><input type='hidden' disabled id='monto"+ row.id +"' name='monto' value='"+row.monto+"' disabled />";
				}},
				{"title":"Tipo Beca", "render":function ( data, type, row,meta ) {
					return "<select name='id_bec' id='id_bec"+row.id+"' data-placeholder='Seleccione' class='form-control select-search' required onchange='seleccionarMensualidad(this)'><option value=''>Seleccione</option></select>"; 
				} 
				},
				{"title":"Beca", "render":function ( data, type, row,meta ) {
					return "<label class='checkbox-inline'><input type='hidden' disabled id='id_fac" + row.id +"' name='id_fac' value='"+row.id+"' disabled /><input type='checkbox' id='id_bec_sel" + row.id +"' name='id_bec_sel' value="+row.id+" onclick='seleccionarMensualidad(this)'/></label>"; 
				} 
				},
				{"title":"Desc.", "render": function ( data, type, row ) {
					return '<div class="col-sm-1"><input type="text"  style="width:50px" disabled id="monto_afec'+row.id+'" name="monto_afec" readonly value=""\></div>';
				}},
				{"title":"Motivo Beca", "render":function ( data, type, row,meta ) {
					return "<select name='id_mbec' disabled id='id_mbec"+row.id+"' data-placeholder='Seleccione' class='form-control select-search' required><option value=''>Seleccione</option></select>"; 
				} 
				},
		],
		"initComplete": function( settings ) {
		   _initCompleteDTSB(settings);

			$("#frm_pago_beca select[name='id_bec']").each(function() {
				console.log($(this));
				
					/*	_llenarCombo('col_beca',$(this),null,null,function(){
					});*/
					_llenar_combo({
						url:'api/pagos/listaBecas',
						combo:$(this),
						context:_URL,
						text:'nom'
					});
			});
			
			$("#frm_pago_beca select[name='id_mbec']").each(function() {
				console.log($(this));
					_llenarCombo('col_motivo_beca',$(this),null,null,function(){
				//$('#id_bec').change();
					});
			});

		}
		});
	 });
}	

 function seleccionarMensualidad(campo1){
	console.log(campo1);
	var campo =$(campo1);
	var id = campo.val();
	 	// else{
		 if(campo.is(':checked')){
			 if($('#id_bec' + id).val()==""){
				 alert('Seleccione el tipo de Beca');
				 $('#id_bec_sel' + id).prop('checked',false);
			 } else{
				 $('#id_bec_sel' + id).prop('checked',true);
				 $('#id_fac' + id).prop('disabled',false);
			$('#id_bec_sel' + id).prop('disabled',false);
			$('#id_bec' + id).prop('disabled',false);
			$('#id_mbec' + id).prop('disabled',false);
			$('#id_mbec' + id).attr('required','required');
			$('#monto' + id).prop('disabled',false);
			$('#monto_afec' + id).prop('disabled',false);
			_get('api/pagos/obtenemosMontoBeca/'+$('#id_bec' + id).val(),function(data){
			console.log(data);
			var monto_total=$('#monto'+ id).val();
			if(data.tip_des=="POR"){
				var valor=parseFloat((data.valor)/100);
				console.log(valor);
				console.log(monto_total);
				var monto_afectado=parseFloat(monto_total)*parseFloat(valor);
				console.log(monto_afectado);
				$('#monto_afec'+ id).val(monto_afectado);
			} else if(data.tip_des=="MON"){
				var valor=data.valor;
				var monto_afectado=parseFloat(monto_total)-parseFloat(valor);
				$('#monto_afec'+ id).val(monto_afectado);
			}
			 
			
		});
			 }
		}else{
			//var id_eva = $('#id_eva' + id).val();
			$('#id_fac' + id).prop('disabled',true);
			//$('#id_bec_sel' + id).prop('disabled',true);
			$('#id_bec' + id).prop('disabled',true);
			$('#id_mbec' + id).prop('disabled',true);
			$('#monto' + id).prop('disabled',true);
			$('#monto_afec' + id).prop('disabled',true);
			$('#id_bec_sel' + id).prop('checked',false);
		}
	 //}
}

/*$('#id_bec' + $(this).val()).on('change',function(event){
		alert();
		//Obtenemos el valor de la beca
		_get('api/pagos/obtenemosMontoBeca/'+$('#id_bec' + $(this).val()).val(),function(data){
			console.log(data);
		});
	});	*/
	
	