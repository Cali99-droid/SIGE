//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	console.log(params);
	$("#oli_config-frm #id_anio").val($("#_id_anio").text());
	if(params!=null){
		var id=params.id;
		
		_GET({
			context:_URL_OLI,
			url:'api/config/' + id,
			params:param,
			success:function(data){
				$("#oli_config-frm #id_oli").val(data.id);
				$("#oli_config-frm #nom").val(data.nom);
				$("#oli_config-frm #corr_envio").val(data.corr_envio);
				$("#oli_config-frm #fec").val(_parseDate(data.fec));
				$("#oli_config-frm #fec_ini_ins").val(_parseDate(data.fec_ini_ins));
				$("#oli_config-frm #fec_fin_ins").val(_parseDate(data.fec_fin_ins));
				$("#oli_config-frm #nro_alu_del").val(data.nro_alu_del);
				$("#oli_config-frm #nro_pre").val(data.nro_pre);
				$("#oli_config-frm #ptje_corr").val(data.ptje_corr);
				$("#oli_config-frm #ptje_inc").val(data.ptje_inc);
				$("#oli_config-frm #ptje_bla").val(data.ptje_bla);
				$("#oli_config-frm #costo").val(data.costo);
				$("#oli_config-frm #costo").trigger('change.select2');
				$("#imp_bo").val(data.imp_bo);
				$("#imp_bo").trigger('change.select2');
				$("#res_det").val(data.res_det);
				$("#res_det").trigger('change.select2');
				$("#div-oli_config-costo").css('display','block');
				$('#oli_config-costo #id_oli').val(data.id);
				config_listar_costos();
			}
		});
		
		var param={id_oli:id};//id_col_org
		//mostrar los colegios modulares (<div>)
		//FIELDSET COLEGIO ORGANIZARDOR 
		_GET({
			context:_URL_OLI,
			url:'api/config/listarColegioxOli',
			params:param,
			success:function(data){
				//if(data.id_col_org)
				var colegiosModularesList = data;
				var nroColegios  = colegiosModularesList.length; //EJEM: 2  O 3 COLEGIOS MODULARES
				//CLONO  - nroColegios -1
				for (var i=0;i<nroColegios-1;i++ ){
					//clonanmos los div
					divColegio = ($("#div_colegio").last()).clone();
					$( "#div_organizador" ).append(divColegio);
				}
				
				//llenas los datos
				for(var i in colegiosModularesList){
					console.log(i); //0, 1, 2
					var colModular = colegiosModularesList[i];
					var div = $('.divColegioModular:eq('+i+')');
					console.log(colModular.id);
					console.log(div.find('#dep').html());
					_llenar_combo({
						url: 'api/colegio/listarcolegios',
						combo:div.find('#id_col'),
						id: colModular.id,
						//params:param,
						text:'value',
						attr: true,
						funcion:function(){
							$("#id_ti").on('change',function(event){
								//alert(_MODALIDAD_COSTO_OLI);
								if($("#id_ti").val()==_MODALIDAD_COSTO_OLI){
									var arr_gestion =[];
									$('.colegio').each(function() {
									   //console.log(obj);
									  var option =  $(this).find('option:selected');
									   //alert(option.attr('estatal'));
									   arr_gestion.push(option.attr('estatal'));
									});

									console.log(arr_gestion);
									var arr = arr_gestion.join('&id_gestion=');
									
									_llenar_combo({
									   	url:'api/config/listarGestionxTipo?id_gestion=' + arr,
									   	params: arr,
										combo:$('#id_cg'),
										context:_URL_OLI
									});		
									
								} else{
									_llenar_combo({
										tabla:'cat_gestion',
										combo:$('#id_cg'),
										context:_URL_OLI
									});
								}
							});
							_llenar_combo({
								tabla:'cat_tipo_inscripcion',
								combo:$('#id_ti'),
								context:_URL_OLI,
								funcion:function(){
									$('#id_ti').change();
								}
							});
						}
					});
					div.find('#dep').val(colModular.aux1);
					div.find('#pro').val(colModular.aux2);
					div.find('#dist').val(colModular.aux3);
				}
			}
		});
		
	} else{
		_llenar_combo({
			url: 'api/colegio/listarcolegios',
			combo:$('#id_col'),
			text:'value',
			attr: true
		});
		$("#div-oli_config-costo").css('display','none');
		
		/* $("#id_ti").on('change',function(event){
			if($("#id_ti").val()==_MODALIDAD_COSTO_OLI){
				var arr_gestion =[];
				$('.colegio').each(function() {
				  var option =  $(this).find('option:selected');
				   arr_gestion.push(option.attr('estatal'));
				});

				console.log(arr_gestion);
				var arr = arr_gestion.join('&id_gestion=');
				
				_llenar_combo({
				   	url:'api/config/listarGestionxTipo?id_gestion=' + arr,
				   	//id:data.id_cg,
					combo:$('#id_cg'),
					context:_URL_OLI
				});		
				
			} else{
				_llenar_combo({
					tabla:'cat_gestion',
					combo:$('#id_cg'),
					context:_URL_OLI
				});
			}
		});*/
	}
	
	
	var param=null;
	if(params!=null)
		param={id_oli:params.id};

	_GET({
		context:_URL_OLI,
		url:'api/public/grado/listarGradosConcurso/',
		params:param,
		success:function(data){
			console.log(data);
			$('#oli_config-grados').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 orderCellsTop:true,
				 select: true,
				 searching: false, 
				 paging: false, 
				 info: false,
		         columns : [
		                    
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
		        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
		        	 		//creamos un hidden x cada checkbox
		        	 		var checked = (row.oli_grado!=null) ? ' checked ': ' ';
		        	 		var disabled = (checked) ? 'disabled':'';
		        	 		//var ideva = (row.eva_id!=null) ? row.eva_id: '';
		        	 		return "<input type='checkbox' " + checked + "id='id_gra"+row.id+"'  name ='id_gra' value='" + row.id + "' class='id_gra' /><font align='center'>"+row.grado+"</font>";
		        	 	} 
		        	 	}
			    ]
		    });
		}
	});
	$("#id_col").on('change',function(event){
		var padre = $(this).parent().parent().parent();
		//alert($("#id_col option:selected").attr("data-aux1")),
		padre.find("#dep").val($(this).find("option:selected").attr("aux1"));
		padre.find("#pro").val($(this).find("option:selected").attr("aux2"));
		padre.find("#dist").val($(this).find("option:selected").attr("aux3"));
	});
	
	$('#fec_fin_ins').on('apply.daterangepicker', function(ev, picker) { 
		var arr_fin=$('#fec_fin_ins').val().split('/');
		var fecha_fin=arr_fin[2]+arr_fin[1]+arr_fin[0];
		var arr_fec=$('#fec').val().split('/');
		var fec=arr_fec[2]+arr_fec[1]+arr_fec[0];
		if(fecha_fin>fec){
			alert('La fecha final de la inscripción no puede ser mayor que la fecha del concurso!!');
			$('#fec_fin_ins').val('');
		}
		var arr_ini=$('#fec_ini_ins').val().split('/');
		var fecha_ini=arr_ini[2]+arr_ini[1]+arr_ini[0];
		if(fecha_fin<fecha_ini){
			alert('La fecha final de la inscripción no puede ser menor que la fecha de inicio!!');
			$('#fec_fin_ins').val('');
		}
	});
	
	$('#fec_ini_ins').on('apply.daterangepicker', function(ev, picker) { 
		var arr_ini=$('#fec_ini_ins').val().split('/');
		var fecha_ini=arr_ini[2]+arr_ini[1]+arr_ini[0];
		var arr_fec=$('#fec').val().split('/');
		var fec=arr_fec[2]+arr_fec[1]+arr_fec[0];
		if(fecha_ini>fec){
			alert('La fecha inical de la inscripción no puede ser mayor que la fecha del concurso!!');
			$('#fec_ini_ins').val('');
		}
	});

	 
	  
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

});

$("#btn_agregar_colegio").on('click',function(event){
	divColegio = ($("#div_colegio").last()).clone();
	divColegio.find("#id_col").val('');
	//divColegio.find("#id_col").find('option').remove();
	
	//divColegio.find("#id_col").change();
	divColegio.find("#dep").val('');
	divColegio.find("#pro").val('');
	divColegio.find("#dist").val('');
	$( "#div_organizador" ).append(divColegio);
	
	divColegio.find(".select2").remove();
	
	divColegio.find("#id_col").select2();

	divColegio.find("#id_col").on('change',function(event){
		var padre = $(this).parent().parent().parent();
		
		//alert($("#id_col option:selected").attr("data-aux1")),
		padre.find("#dep").val($("#id_col option:selected").attr("aux1"));
		padre.find("#pro").val($("#id_col option:selected").attr("aux2"));
		padre.find("#dist").val($("#id_col option:selected").attr("aux3"));
	});
	
});

$('#oli_config-frm #btn-grabar').on('click',function(event){
	$('#oli_config-frm #id').val('');
	
	var validator = $("#oli_config-frm").validate();
	
	if (validator.valid()){

		_POST({form:$('#oli_config-frm'),
			  context:_URL_OLI,
			  success:function(data){
				  console.log(data.result);
				  $('#oli_config-frm #id_oli').val(data.result);
				  $("#div-oli_config-costo").css('display','block');
				  //_llenarCombo('cat_modalidad_costo',$('#id_cmc'),null,null,null,_URL_OLI);
				_llenar_combo({
					tabla:'cat_tipo_inscripcion',
					combo:$('#id_ti'),
					context:_URL_OLI,
					funcion:function(){
						$('#id_ti').change();
					}
				});
				config_listar_costos();

			}
		});
		
	}
	

});


/**
 * AGREGAR COSTO NUEVO
 */
$('#oli_config-costo #btn-agregar-costo').on('click',function(event){
	$('#oli_config-costo #id').val('');
	$('#oli_config-costo #id_oli').val($("#oli_config-frm #id_oli").val());
	_POST({form:$('#oli_config-costo'),
		  context:_URL_OLI,
		  success:function(data){
			  console.log(data.result);
			  config_listar_costos();
			  $('#oli_config-costo #id_oli').val('');

		}
	});
});

$('#oli_config-costo #btn-editar-costo').on('click',function(event){	
	$('#oli_config-costo #id_oli').val($("#oli_config-frm #id_oli").val());
	_POST({form:$('#oli_config-costo'),
		  context:_URL_OLI,
		  success:function(data){
			  console.log(data.result);
			  config_listar_costos();
			  $('#oli_config-costo #id_oli').val('');
			  $('#oli_config-costo #id').val('');
			  $('#oli_config-costo #btn-editar-costo').hide();
			  $('#oli_config-costo #btn-agregar-costo').show();
		}
	});
});

function config_eliminar(link){
	_DELETE({url:'api/config/' + $(link).data("id"),
			success:function(){
					config_listar_tabla();
				}
			});
}

function config_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/olimpiada/oli_config_modal.html');
	config_modal(link);
	
}

function config_listar_costos(){
	$('#oli_config-costo #btn-editar-costo').hide();
	$('#oli_config-costo #btn-agregar-costo').show();
	var param={id_oli:$('#oli_config-frm #id_oli').val()};
	
	_GET({ url: 'api/config/listarCostos',
		   context: _URL_OLI,
		   params: param,
		   success:
			function(data){
			console.log(data);
				$('#oli_config-precios').dataTable({
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
							{"title":"Fec. Inicio", "render":function ( data, type, row,meta ) { 
								return _parseDate(row.fec_ini) ;
								} 
							},
							{"title":"Fec. Final", "render":function ( data, type, row,meta ) { 
								return _parseDate(row.fec_fin) ;
								} 
							},
							{"title":"Modalidad", "data" : "modalidad"}, 
							{"title":"Gestion", "data" : "gestion"}, 
							{"title":"Costo", "data" : "costo"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
							  return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="costo_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
									 '<a href="#"  data-id="' + row.id + '" onclick="costo_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>';
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});

}

function costo_editar(row,e){
	e.preventDefault();
	 $('#oli_config-costo #btn-editar-costo').show();
	  $('#oli_config-costo #btn-agregar-costo').hide();
	var link = $(row);
	_GET({url:'api/public/costo/' + link.data('id'),
		  context: _URL_OLI,
		  success:	function(data){
			  $("#div-oli_config-costo #id").val(data.id);
			  $("#div-oli_config-costo #id_oli").val(data.id_oli);
			  $("#div-oli_config-costo #fec_ini").val(_parseDate(data.fec_ini));
			  $("#div-oli_config-costo #fec_fin").val(_parseDate(data.fec_fin));
			  $("#div-oli_config-costo #costo").val(data.costo);
			   //$('#id_ti').val(data.id_cg).change();
			 // $('#id_cg').val(data.id_ti).change();
			  _llenar_combo({
					tabla:'cat_tipo_inscripcion',
					combo:$('#id_ti'),
					id:data.id_ti,
					context:_URL_OLI,
					funcion:function(){
						$('#id_ti').change();
					}
				});
				
			/*$("#id_ti").on('change',function(event){
				alert(data.id_cg);
				$('#id_cg').val(data.id_cg).change();
			});	*/
				//$('#id_cg').val(data.id_cg).change();
			  
			  $("#id_ti").on('change',function(event){
					if($("#id_ti").val()==_MODALIDAD_COSTO_OLI){
						var arr_gestion =[];
						$('.colegio').each(function() {
						  var option =  $(this).find('option:selected');
						   arr_gestion.push(option.attr('estatal'));
						});

						console.log(arr_gestion);
						var arr = arr_gestion.join('&id_gestion=');
						
						_llenar_combo({
						   	url:'api/config/listarGestionxTipo?id_gestion=' + arr,
						   	id:data.id_cg,
							combo:$('#id_cg'),
							context:_URL_OLI
						});		
						
					} else{
						_llenar_combo({
							tabla:'cat_gestion',
							id:data.id_cg,
							combo:$('#id_cg'),
							context:_URL_OLI
						});
					}
				});
			  
			  
		  }
		}
	);
}

function costo_eliminar(row,e){
	var link = $(row);
	_DELETE({url:'api/public/costo/' + $(link).data("id"),
		context: _URL_OLI,
		success:function(){
			config_listar_costos();
			}
		});
}


