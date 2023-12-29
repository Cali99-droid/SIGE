/** Se ejeuta al ser llamado desde otra pagina * */
function onloadPage(params) {

	_onloadPage();
	console.log(params);
	var _id_alu=params.id;
	var _id_gpf= params.id_gpf;
	
	//listar_inscripciones(_id_alu);
	
	$("#inscripcion_vacante-frm #id_alu").val(_id_alu);
	listar_inscripciones(_id_alu);
	_GET({  url:'api/matricula/validarDeudasPendientes/'+_id_alu+'/' + $('#_id_anio').text(),
					success:function(data){
					console.log(data);
			$("#btn-grabar").removeAttr("disabled");
			$("#btn-grabar").show();
			$("#id_eva").removeAttr("disabled");	
			$("#id_gra").removeAttr("disabled");
			$("#id_niv").removeAttr("disabled");			
					},
		error:function(data){
		
		_alert_error(data.msg);
		/*swal({
		  title: "IMPORTANTE",
		  html:false,
		  type : "warning",
		  text: data.msg,
		  icon: "info",
		  button: "Cerrar",
		});*/
		 $("#id_niv").attr("disabled","disabled");
		 $("#id_gra").attr("disabled","disabled");
		 $("#id_eva").attr("disabled","disabled");
		 $("#btn-grabar").hide();
		 $("#btn-grabar").attr("disabled","disabled");
		
		// _alert_error(data.msg);
		}
	});
	_GET({url:'api/matrVacante/validarCondicion/'+_id_alu, 
		context:_URL,
		//params: param,
		success:function(data){
			console.log(data);
			$("#btn-grabar").removeAttr("disabled");
			$("#id_eva").removeAttr("disabled");
			$("#id_gra").removeAttr("disabled");
			$("#id_niv").removeAttr("disabled");			
		}, error:function(data){
			 console.log(data);
			 _alert_error(data.msg);
			 $("#btn-grabar").attr("disabled","disabled");
			 $("#id_eva").attr("disabled","disabled");
			 $("#id_gra").attr("disabled","disabled");
			 $("#id_niv").attr("disabled","disabled");
		}
	});
	
	_GET({url:'api/matrVacante/validarCondicionDesaprobado/'+_id_alu+'/'+$('#_id_anio').text(), 
		context:_URL,
		//params: param,
		success:function(data){
			console.log(data);
			 $("#btn-grabar").removeAttr("disabled");
			 $("#id_eva").removeAttr("disabled");
			 $("#id_gra").removeAttr("disabled");
			 $("#id_niv").removeAttr("disabled");
		}, error:function(data){
			 console.log(data);
			 $("#btn-grabar").attr("disabled","disabled");
			 $("#id_eva").attr("disabled","disabled");
			  $("#id_gra").attr("disabled","disabled");
			  $("#id_niv").attr("disabled","disabled");
			 _alert_error(data.msg);
		}
	});
	//Validar si el alumno puede postular

/*	$.ajax( "validarCondicion/"+$(this).val()).done(function(data) {
		//Funcionalidad nueva
		//aca desactiva el oton
		if(data.error!=null){
			//alert(1);
			var theme = 'air';
			Messenger.options = {theme : theme};
			Messenger().post({
				message : data.error,
				type : "error",
				showCloseButton : true
			});
			$("#btnGrabar").attr("disabled","disabled");
			$("#vacante-form #id_eva").attr("disabled","disabled");
		}else{
			$("#vacante-form #id_eva").removeAttr("disabled");
			 $("#btnGrabar").removeAttr("disabled");
		}
			
	});*/
	
	var param={id_gpf:_id_gpf};
	_llenar_combo({
	   	url:'api/familiar/listarPadres',
	   	params: param,
		combo:$('#id_cli'),
		context:_URL,
		text:'familiar'
	});
	
	$('#id_eva').on('change',function(event){
		var costo = $("#id_eva option:selected").data("precio");
		var id_per = $("#id_eva option:selected").data("id_per");
		$("#costo").val(costo);
		var vacante	= $("#vacante");
		var vac=0;
		var param ={id_per:id_per, id_grad:$('#id_gra').val() ,id_eva:$('#id_eva').val()};
		if($('#id_eva').val()!=null){
			_GET({url:'api/matrVacante/listarNumeroVacantes', 
				context:_URL,
				params: param,
				success:function(data){
					console.log(data);
					var anio_setup=data.anio_setup
					if(anio_setup=='Y'){
						var postulantes= data.postulantes;
						var vac=data.vacantes;
						alert($("#id").val());
						//Tener datos de inscripcion_vacante
						if($("#id").val()!=null && $("#id").val()!=''){
								var param={id_ins: $("#id").val()};
								_GET({url:'api/matrVacante/editarInscripcion',
									  params: param,
									  success:	function(data){
										  var id_eva=data.id_eva;
										  var id_gra=data.id_gra;
										  if($("#id_eva").val()==id_eva && $("#id_gra").val()==id_gra){
											  vac=vac+1;
										  }  
								}});	  
						}	
						if(postulantes==null){
							  alert('NO TIENE REGISTRO DE POSTULANTES');
							  $("#btn-grabar").attr("disabled","disabled");
						  } else if(vac!=null && vac>0 ){
							  vacante.val(vac);
							  $("#btn-grabar").removeAttr("disabled");
						  }
						  else{
							  vacante.val('No hay vacantes');
							  $("#btn-grabar").attr("disabled","disabled");
							  console.log(data);
						  }
					} else{
						//if(data!=null){
						var vac=data.vacantes;
						if($("#id").val()!=null && $("#id").val()!=''){
								var param={id_ins: $("#id").val()};
								_GET({url:'api/matrVacante/editarInscripcion',
									  params: param,
									  success:	function(data){
										  var id_eva=data.id_eva;
										  var id_gra=data.id_gra;
				
										  if($("#id_eva").val()==id_eva && $("#id_gra").val()==id_gra){

											  console.log(vac);
											  vac=vac+1;
											  console.log(vac);
											  if(vac!=null && vac>0 ){
												  vacante.val(vac);
												  $("#btn-grabar").removeAttr("disabled");
											  }
											  else{
												  vacante.val('No hay vacantes');
												  $("#btn-grabar").attr("disabled","disabled");
												  console.log(data);
											  }	
										  } else {
											   if(vac!=null && vac>0 ){
												  vacante.val(vac);
												  $("#btn-grabar").removeAttr("disabled");
											  }
											  else{
												  vacante.val('No hay vacantes');
												  $("#btn-grabar").attr("disabled","disabled");
												  console.log(data);
											  }	
										  }
								}});
								
															
						} else {
							if(vac!=null && vac>0 ){
							  vacante.val(vac);
							  $("#btn-grabar").removeAttr("disabled");
							  }
							  else{
								  vacante.val('No hay vacantes');
								  $("#btn-grabar").attr("disabled","disabled");
								  console.log(data);
							  }
						}	

					//  }
					}
					
				}
			});
		}
		
	});
	
	$('#id_gra').on('change',function(event){
		var param1={id_niv:$('#id_niv').val(), id_suc:0, id_anio:$('#_id_anio').text()};
		_llenar_combo({
		   	url:'api/evaluacionVac/listarEvaluacionesVigentes',
		   	params: param1,
			combo:$('#id_eva'),
			context:_URL,
			text:'evaluacion',
			funcion:function(){
				$('#id_eva').change();
			}
		});
		var niv=null;
		var id_gra =  $(this).val();
		if(id_gra==4 || id_gra==1 || id_gra==2 || id_gra==3){
			niv='INICIAL';
		} else if (id_gra==10 || id_gra==5 || id_gra==6 || id_gra==7 || id_gra==8 || id_gra==9){
			niv='PRIMARIA';
		} else if (id_gra==11 || id_gra==12 || id_gra==13 || id_gra==14){
			niv='SECUNDARIA';
		}
		
		var param2={nivel:niv};
		_llenar_combo({
		   	url:'api/colegio/listarcolegiosConcurso',
		   	params: param2,
			combo:$('#id_col'),
			context:_URL,
			text:'value',
			/*funcion:function(){
				$('#id_eva').change();
			}*/
		});
	});
	
	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){
			$('#id_gra').change();
		});
		if($('#id_niv').val()==1){
			$("#id_col").removeAttr('required');
		} else{
			$("#id_col").attr('required','required');
		}
			
	});
	
	/*_llenar_combo({
		tabla:'cat_nivel',
		combo:$('#id_niv'),
		context:_URL,
		funcion:function(){
			//$('#id_niv').change();
		}
	});	*/
		_llenarCombo('cat_nivel',$('#id_niv'),null,null,function(){
			//$('#id_gra').change();
		});
}

/*$(function(){
	_onloadPage();
});*/

$('#inscripcion_vacante-frm #btn-grabar').on('click', function(event){	
	if ($("#inscripcion_vacante-frm").valid()) {
	//	$("#vacante-form #id_gpf").val($('#_id_gpf').val());
		 $('#inscripcion_vacante-frm #btn-grabar').attr('disabled','disabled');
	
		_POST({form:$('#inscripcion_vacante-frm'),
			  context: _URL,
			  msg_type:'notification',
			  msg_exito : 'SE INSCRIBIO CORRECTAMENTE AL ALUMNO.',
			  success:function(data){
				    //DESCARGAR 
				  //id_gpf=2490, id_per=null, id=5010, error=null
				  $("#inscripcion_vacante-frm #id").val(data.id);
				  listar_inscripciones(data.id_alu);
			 }, 
			 error:function(data){
				 console.log(data);
				 _alert_error(data.msg);
				 $('#inscripcion_vacante-frm #btn-grabar').removeAttr('disabled');
			}
		}); 
	}
	return false;

});

function listar_inscripciones(id_alu){
	var param={id_alu:id_alu};
	_GET({ url: 'api/matrVacante/listarMatriculaVacante/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			console.log(data);
				$('#admi_inscripcion_alumno-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 searching: false, 
					 paging: false, 
					 info: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Evaluaci&oacute;n", "data" : "des"}, 
							{"title":"Anio", "data" : "anio"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.vigencia==1)
									return '<button onclick="editarEvaluacionVacante(' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
								else
									return '<button onclick="editarEvaluacionVacante(' + row.id+')" type="button" disabled class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});
}
function editarEvaluacionVacante(id_ins){
	var param={id_ins: id_ins};
	_GET({url:'api/matrVacante/editarInscripcion',
		  params: param,
		  success:	function(data){
	//$('#id_gra').val(data.id_gra);
	//$('#id_gra').selectmenu('refresh', true);
	$("#id_gra").off("change");
	$('#id_gra').on('change',function(event){
		var param1={id_niv:$('#id_niv').val(), id_suc:0, id_anio:$('#_id_anio').text()};
		_llenar_combo({
		   	url:'api/evaluacionVac/listarEvaluacionesVigentes',
		   	params: param1,
			combo:$('#id_eva'),
			id: data.id_eva,
			context:_URL,
			text:'evaluacion',
			funcion:function(){
				$('#id_eva').change();
			}
		});
		var niv=null;
		var id_gra =  $(this).val();
		if(id_gra==4 || id_gra==1 || id_gra==2 || id_gra==3){
			niv='INICIAL';
		} else if (id_gra==10 || id_gra==5 || id_gra==6 || id_gra==7 || id_gra==8 || id_gra==9){
			niv='PRIMARIA';
		} else if (id_gra==11 || id_gra==12 || id_gra==13 || id_gra==14){
			niv='SECUNDARIA';
		}
		
		var param2={nivel:niv};
		_llenar_combo({
		   	url:'api/colegio/listarcolegiosConcurso',
		   	params: param2,
			combo:$('#id_col'),
			id: data.id_col,
			context:_URL,
			text:'value',
			/*funcion:function(){
				$('#id_eva').change();
			}*/
		});
	});
	
	$("#id_niv").off("change");
	$('#id_niv').on('change',function(event){
			
		_llenarCombo('cat_grad',$('#id_gra'),data.id_gra,$(this).val(),function(){
			$('#id_gra').change();
		});
		
	});
	//$('#id_gra').change();
	//$("#id_gra option[value=3]").attr("selected",true);
	$('#id_niv').val(data.id_niv).change();
	
	_fillForm(data,$('#inscripcion_vacante-frm'));
	//alert();
		
	
		//$('#id_eva').val(data.id_eva).change();
		//$('#id_col').val(data.id_col).change();
		//alert(data.id_gra);
	//	$('#id_gra').val(data.id_gra).change();
	//	$('#id_niv').val(data.id_niv).change();
			
				//_fillForm(data,$('#inscripcion_vacante-frm'));
				/*$('#id_gra').on('change',function(event){
					var param1={id_niv:$('#id_niv').val(), id_suc:$("#_id_suc").text()};
					_llenar_combo({
					   	url:'api/evaluacionVac/listarEvaluacionesVigentes',
					   	params: param1,
						combo:$('#id_eva'),
						context:_URL,
						id: data.id_eva,
						text:'evaluacion',
						funcion:function(){
							$('#id_eva').change();
						}
					});
					var niv=null;
					var id_gra =  $(this).val();
					if(id_gra==4 || id_gra==1 || id_gra==2 || id_gra==3){
						niv='INICIAL';
					} else if (id_gra==10 || id_gra==5 || id_gra==6 || id_gra==7 || id_gra==8 || id_gra==9){
						niv='PRIMARIA';
					} else if (id_gra==11 || id_gra==12 || id_gra==13 || id_gra==14){
						niv='SECUNDARIA';
					}
					
					var param2={nivel:niv};
					_llenar_combo({
					   	url:'api/colegio/listarcolegiosConcurso',
					   	params: param2,
						combo:$('#id_col'),
						context:_URL,
						id: data.id_col,
						text:'value',
						/*funcion:function(){
							$('#id_eva').change();
						}*/
					/*});
				});*/
				
				/*$('#id_niv').on('change',function(event){
			alert(1);
					/*_llenarCombo('cat_grad',$('#id_gra'),data.id_gra,$(this).val(),function(){
						$('#id_gra').change();
					});*/
					
				//});
				/*$('#id_gra').change();
				
				_llenar_combo({
					tabla:'cat_nivel',
					combo:$('#id_niv'),
					context:_URL,
					id: data.id_niv,
					function(){
						$('#id_niv').change();
					}
				});	*/
		}
		}
	);
}

