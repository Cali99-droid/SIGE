//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/evaluacion/eva_evaluacion_vac_modal.html" id="eva_evaluacion_vac-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Evaluacion</a></li>');

	$('#eva_evaluacion_vac-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		evaluacion_modal($(this));
	});

	//lista tabla
	evaluacion_listar_tabla();
	
	 
});

function _onchangeAnio(id_anio, anio){
	evaluacion_listar_tabla();
}

function evaluacion_eliminar(link){
	_delete('api/evaluacionVac/' + $(link).data("id"),
			function(){
					evaluacion_listar_tabla();
				}
			);
}

function evaluacion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_evaluacion_vac_modal.html');
	evaluacion_modal(link);
	
}

function examen_editar(row){
	//e.preventDefault();
	//if(link.data('id_tip')==1){
		var link = $(row);
		var id_tae=link.data('id_tip');
		 
	//}
	//var link = $(row);
	$("#fieldsetTM" + id_tae).css("display", "block");
	_get('api/evaluacionVacExamen/obtenerDatos/' + link.data('id_exa')+'/'+link.data('id_tip'),
			function(data){
				console.log(data);
				console.log('aquiii'+link.data('id_tip')),
				_llenarCombo('eva_tip_eva',$('#id_tae'),data.id_tae,null);
				_llenarCombo('eva_area_eva',$('#id_eae'),data.id_eae,null);
				$('#id_examen').val(link.data('id_exa'));
				
			     $("#fieldsetTM" + id_tae + " input").removeAttr("disabled");
			     $("#fieldsetTM" + id_tae + " input").attr("required","required");
				if(link.data('id_tip')==1){
					$("#eva_esc").css("display", "block");
					var fec_exa= _parseDate(data.fec_exa);
					var fec_not= _parseDate(data.fec_not);
					$('#fec_exa').val(fec_exa);
					$('#fec_not').val(fec_not);
					$('.daterange-single').daterangepicker({ 
				        singleDatePicker: true,
				        locale: { format: 'DD/MM/YYYY'}
				    });
					$('#fieldsetTM1 #num_pre').val(data.num_pre);
					$('#fieldsetTM1 #pje_pre_cor').val(data.pje_pre_cor);
					$('#fieldsetTM1 #pje_pre_inc').val(data.pje_pre_inc);
					$('#fieldsetTM1 #id_marcacion').val(data.id_marcacion);
					$("#fieldsetTM" + 2).css("display", "none");
					$("#fieldsetTM" + 3).css("display", "none");
				} else if(link.data('id_tip')==2){
					$('#dur').val(data.dur);
					var fec_ini_psi=_parseDate(data.fec_ini_psi);
					var fec_fin_psi=_parseDate(data.fec_fin_psi);
					$('#fec_ini_psi').val(fec_ini_psi);
					$('#fec_fin_psi').val(fec_fin_psi);
					$('.daterange-single').daterangepicker({ 
				        singleDatePicker: true,
				        locale: { format: 'DD/MM/YYYY'}
				    });
					$('#id_criterio').val(data.id_criterio);
					$("#fieldsetTM" + 1).css("display", "none");
					$("#fieldsetTM" + 3).css("display", "none");
					var param={id_exa:$('#id_examen').val()};
					listar_instrumentos(param);
				}
	});
	
	
}

function evaluacion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('eva_evaluacion_vac-frm');
		
		$('#eva_evaluacion_vac-frm #btn-grabar').on('click',function(event){
			//$('#eva_evaluacion_vac-frm #id').val('');
			_post($('#eva_evaluacion_vac-frm').attr('action') , $('#eva_evaluacion_vac-frm').serialize(),
			function(data){
				console.log(data);
				$("#id_eva_vacante").val(data.result.id_eva);
				//$("#id_examen").val(data.result.id_examen);
				 listarExamenes();
				 onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/evaluacionVac/' + link.data('id'),
			function(data){
				console.log(data);
				//_fillForm(data,$('#modal').find('form') );
				var param={id_anio:$('#_id_anio').text()};
				_llenarComboURL('api/periodoAca/listarPeriodosVacante',$('#id_per'),data.id_per,param);
				$("#des").val(data.des);
				$("#precio").val(data.precio);
				$("#ptje_apro").val(data.ptje_apro);
				var fec_ini=_parseDate(data.fec_ini);
				var fec_fin=_parseDate(data.fec_fin);
				var fec_vig_vac=_parseDate(data.fec_vig_vac);
				$("#fec_ini").val(fec_ini);
				$("#fec_fin").val(fec_fin);
				$("#fec_vig_vac").val(fec_vig_vac);
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        locale: { format: 'DD/MM/YYYY'}
			    });
				$("#id_eva_vacante").val(data.id);
				//$("#des")
				if(link.data('id_exa')!=null){
					_get('api/evaluacionVacExamen/' + link.data('id_exa'),
							function(data){
							if(data!=null){
								$("#fieldsetTM" + data.id_tae).css("display", "block");
								_llenarCombo('eva_tip_eva',$('#id_tae'),data.id_tae,null, function(){$('#id_tae').change()});
								_llenarCombo('eva_area_eva',$('#id_eae'),data.id_eae,null);
								$("#id_examen").val(link.data('id_exa'));
								if(data.id_tae==1){
									$("#eva_esc").css("display", "block");
									var fec_exa= _parseDate(data.fec_exa);
									var fec_not= _parseDate(data.fec_not);
									$('#fec_exa').val(fec_exa);
									$('#fec_not').val(fec_not);
									$('.daterange-single').daterangepicker({ 
								        singleDatePicker: true,
								        locale: { format: 'DD/MM/YYYY'}
								    });
								    _get('api/evaluacionVacExamen/obtenerDatos/' + link.data('id_exa')+'/'+data.id_tae,
										function(data){
								    	console.log(data.num_pre);
								    	$('#fieldsetTM1 #num_pre').val(data.num_pre);
										$('#fieldsetTM1 #pje_pre_cor').val(data.pje_pre_cor);
										$('#fieldsetTM1 #pje_pre_inc').val(data.pje_pre_inc);
										$('#fieldsetTM1 #id_marcacion').val(data.id_marcacion);
								    });   
								    $("#fieldsetTM" + 2).css("display", "none");
									$("#fieldsetTM" + 3).css("display", "none");
									$("#fieldsetTM" + 2 + " input").attr("disabled","disabled");
									$("#fieldsetTM" + 3 + " input").attr("disabled","disabled");
								} else if(data.id_tae==2){
									 _get('api/evaluacionVacExamen/obtenerDatos/' + link.data('id_exa')+'/'+data.id_tae,
											function(data){
										 	$('#dur').val(data.dur);
										 	var fec_ini_psi=_parseDate(data.fec_ini_psi);
											var fec_fin_psi=_parseDate(data.fec_fin_psi);
											$('#fec_ini_psi').val(fec_ini_psi);
											$('#fec_fin_psi').val(fec_fin_psi);
											$('.daterange-single').daterangepicker({ 
										        singleDatePicker: true,
										        locale: { format: 'DD/MM/YYYY'}
										    });
											$('#id_criterio').val(data.id_criterio);
											var param={id_exa: data.id};
											listar_instrumentos(param);
									 });  
									 $("#fieldsetTM" + 1).css("display", "none");
									 $("#fieldsetTM" + 3).css("display", "none");
									 $("#fieldsetTM" + 1 + " input").attr("disabled","disabled");
									 $("#fieldsetTM" + 3 + " input").attr("disabled","disabled");
								}
							} else {
								//$("#fieldsetTM" + data.id_tae).css("display", "block");
								_llenarCombo('eva_tip_eva',$('#id_tae'),null,null, function(){$('#id_tae').change()});
								_llenarCombo('eva_area_eva',$('#id_eae'),null,null);
								$("#id_tae").on("change",function(){
									// alert();
							    	var id_tae = $(this).val();//tipo de evaluacion
							    	if(id_tae=='1'){
							    		$("#eva_esc").css("display", "block");
							    	} else{
							    		$("#eva_esc").css("display", "none");
							    		var param={id_exa:link.data('id_exa')};
							    		 listar_instrumentos(param);
							    	}
							        //hago deshabiltado, y no requerido  y no visibles a todo
							        $(".tipo_evaluacion input").attr("disabled","disabled");
							        $(".tipo_evaluacion input").removeAttr("required");
							        $(".tipo_evaluacion").css("display", "none");
							        $("#fec_exa").removeAttr("required");
							        $("#fec_not").removeAttr("required");
							        
							        //hago habiltado, y requerido  y visibles al tipo de evaluacion requerida
							        $("#fieldsetTM" + id_tae).css("display", "block");
							        $("#fieldsetTM" + id_tae + " input").removeAttr("disabled");
							        $("#fieldsetTM" + id_tae + " input").attr("required","required");
							        
							       // if("#fieldsetTM" +  == "#fieldsetT2"){
							        //	("#evaluacion_vac_form #fec_exa").attr("disabled","disabled");
							       // }
							        //id no tiene validacion de requerido
							        $(".tipo_evaluacion input[type='hidden']").removeAttr("required");

							    });
								
							}
							
						});
				} else{
					_llenarCombo('eva_tip_eva',$('#id_tae'),null,null, function(){$('#id_tae').change()});
					_llenarCombo('eva_area_eva',$('#id_eae'),null,null);
					$("#id_tae").on("change",function(){
						// alert();
				    	var id_tae = $(this).val();//tipo de evaluacion
				    	if(id_tae=='1'){
				    		$("#eva_esc").css("display", "block");
				    	} else{
				    		$("#eva_esc").css("display", "none");
				    		var param={id_exa:link.data('id_exa')};
				    		 listar_instrumentos(param);
				    	}
				        //hago deshabiltado, y no requerido  y no visibles a todo
				        $(".tipo_evaluacion input").attr("disabled","disabled");
				        $(".tipo_evaluacion input").removeAttr("required");
				        $(".tipo_evaluacion").css("display", "none");
				        $("#fec_exa").removeAttr("required");
				        $("#fec_not").removeAttr("required");
				        
				        //hago habiltado, y requerido  y visibles al tipo de evaluacion requerida
				        $("#fieldsetTM" + id_tae).css("display", "block");
				        $("#fieldsetTM" + id_tae + " input").removeAttr("disabled");
				        $("#fieldsetTM" + id_tae + " input").attr("required","required");
				        
				       // if("#fieldsetTM" +  == "#fieldsetT2"){
				        //	("#evaluacion_vac_form #fec_exa").attr("disabled","disabled");
				       // }
				        //id no tiene validacion de requerido
				        $(".tipo_evaluacion input[type='hidden']").removeAttr("required");
					});
				}

				listarExamenes();
			});
		}else{
			//alert();
			//$('#eva_evaluacion_vac-frm #btn-grabar').hide();
			var param={id_anio:$('#_id_anio').text()};
			_llenarComboURL('api/periodoAca/listarPeriodosVacante',$('#id_per'),null,param);
			_llenarCombo('eva_tip_eva',$('#id_tae'),null,null, function(){$('#id_tae').change()});
			_llenarCombo('eva_area_eva',$('#id_eae'),null,null);
			$("#id_tae").on("change",function(){
				// alert();
		    	var id_tae = $(this).val();//tipo de evaluacion
		    	if(id_tae=='1'){
		    		$("#eva_esc").css("display", "block");
		    	} else{
		    		$("#eva_esc").css("display", "none");
		    		var param={id_exa:link.data('id_exa')};
		    		 listar_instrumentos(param);
		    	}
		        //hago deshabiltado, y no requerido  y no visibles a todo
		        $(".tipo_evaluacion input").attr("disabled","disabled");
		        $(".tipo_evaluacion input").removeAttr("required");
		        $(".tipo_evaluacion").css("display", "none");
		        $("#fec_exa").removeAttr("required");
		        $("#fec_not").removeAttr("required");
		        
		        //hago habiltado, y requerido  y visibles al tipo de evaluacion requerida
		        $("#fieldsetTM" + id_tae).css("display", "block");
		        $("#fieldsetTM" + id_tae + " input").removeAttr("disabled");
		        $("#fieldsetTM" + id_tae + " input").attr("required","required");
		        
		       // if("#fieldsetTM" +  == "#fieldsetT2"){
		        //	("#evaluacion_vac_form #fec_exa").attr("disabled","disabled");
		       // }
		        //id no tiene validacion de requerido
		        $(".tipo_evaluacion input[type='hidden']").removeAttr("required");

		    });
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		evaluacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Evaluacion';
	else
		titulo = 'Nuevo  Evaluacion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function evaluacion_listar_tabla(){
	var param={id_anio:$('#_id_anio').text()};
	_get('api/evaluacionVac/listarEvaluaciones/',
			function(data){
			console.log(data);
				$('#eva_evaluacion_vac-tabla').dataTable({
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
							{"title":"Local", "data" : "sucursal"}, 
							{"title":"Nivel", "data" : "servicio"}, 
							{"title":"Descripci&oacute;n", "data" : "des"}, 
							{"title":"Inicio Evaluación", "data" : "fec_ini"},
							{"title":"Fin Evaluación", "data" : "fec_fin"},
							{"title":"Fecha Vigencia", "data" : "fec_vig_vac"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '"  data-id_exa="' + row.id_exa+ '" onclick="evaluacion_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="evaluacion_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

function listar_instrumentos(param){
	_get('api/instrumento/listarInstrumentos/',
			function(data){
			console.log(data);
				$('#instrumentos-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
			        	 		//creamos un hidden x cada checkbox
			        	 		var checked = (row.id_ins!=null) ? ' checked ': ' ';
			        	 		var disabled = (checked) ? 'disabled':'';
			        	 		//var ideva = (row.eva_id!=null) ? row.eva_id: '';
			        	 		return "<input type='checkbox' " + checked + "id='id_ins"+row.id+"'  name ='id_ins' value='" + row.id + "' class='id_ins' /><font align='center'>"+row.nom+"</font>";
			        	 	} 
			        	 	}
				    ],
				   
			    });
			}, param
	);
}

function ExamenNuevo(){

	$("#evaluacion_vac_form #id_eae").val(''); // para q el combo tenga valor vacio.
	$("#evaluacion_vac_form #id_eae").change(); // para que refresque el DIV
	$("#evaluacion_vac_form #id_tae").val('');
	$("#evaluacion_vac_form #fec_exa").val('');
	$("#evaluacion_vac_form #fec_not").val('');
	$("#evaluacion_vac_form #id_examen").val('');
	$("#fieldsetTM3 #id_escrito").val('');	
	$("#fieldsetTM3 #num_pre").val('');	
	$("#fieldsetTM3 #ptje_apro").val('');	
	$("#fieldsetTM3 #pje_pre_cor").val('');	
	$("#fieldsetTM3 #pje_pre_inc").val('');	
	
	
	$("#fieldsetTM1 #id_marcacion").val('');	
	$("#fieldsetTM1 #num_pre").val('');	
	$("#fieldsetTM1 #ptje_apro").val('');	
	$("#fieldsetTM1 #pje_pre_cor").val('');	
	$("#fieldsetTM1 #pje_pre_inc").val('');	
	
	$(".tipo_evaluacion input").attr("disabled","disabled");
    $(".tipo_evaluacion input").removeAttr("required");
    $(".tipo_evaluacion").css("display", "none");
    $(".tipo_evaluacion input[type='text']").val("");
    _llenarCombo('eva_tip_eva',$('#id_tae'),null,null, function(){$('#id_tae').change()});
	_llenarCombo('eva_area_eva',$('#id_eae'),null,null);
    
}

function listarExamenes(){
	var param={id_eva:$("#id_eva_vacante").val()};
	_get('api/evaluacionVacExamen/listar/',
			function(data){
			console.log(data);
				$('#evaluacion_table').dataTable({
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
							{"title":"Area", "data" : "areaEva.nom"}, 
							{"title":"Tipo de Evaluación", "data" : "tipEva.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id_eva="' + row.evaluacionVac.id + '" data-id_exa="'+row.id+'" data-id_tip="'+row.tipEva.id+'" onclick="examen_editar(this)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id_eva="' + row.evaluacionVac.id + '" data-id_exa="'+row.id+'" data-id_tip="'+row.tipEva.id+'" onclick="examen_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}
//aqui
function examen_eliminar(row) {
	var link = $(row);
	var id_eva=link.data('id_eva');
	var id_exa=link.data('id_exa');
	
	var param={id_eva:id_eva , id_exa:id_exa};
	_GET({ url: 'api/evaluacionVac/eliminarExamen/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			   listarExamenes();
			   $("#eva_evaluacion_vac-frm #btnExamenNuevo").click();
			   
			}
		});
	
}

