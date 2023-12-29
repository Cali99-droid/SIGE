//Se ejecuta cuando la pagina q lo llama le envia parametros
var desglose= true;
var cant_inf=null;
var id_tip_inc=null;
var res_entre=false;
var acta_comp_alu=false;
function onloadPage(params){
	_onloadPage();
	
	
	var id_inc=params.id;
	//Cargar el detalle de la Incidencia
	var param = {id_inc:id_inc};
	$("#frm_entrevista #id_cfi").val(3);
	$("#div_entrevista_res #id_cfi").val(4);
	$("#div_compromiso_alu #id_cfi").val(5);
	$("#div_compromiso_fam #id_cfi").val(6);
	$("#div_derivacion #id_cfi").val(7);
	$("#div_plan_tutoria #id_cfi").val(8);
	$("#div_ficha_seguimiento #id_cfi").val(9);
	$("#div_compromiso_alu #id_ctc").val(1);
	$("#div_compromiso_fam #id_ctc").val(2);
	_GET({
		context:_URL_CON,
		url:'api/incidencia/verDetalle/',
		params:param,
		success:function(data){
			console.log(data);
			if(data.id_cti=="1"){
				id_tip_inc=1;
				$("#div_reporte_bulling").hide();
				$("#div_reporte_conductual #lbl_periodo").text(data.periodo);
				$("#div_reporte_conductual #lbl_fecha").text(data.fecha);
				$("#div_reporte_conductual #lbl_informante").text(data.informante);
				$("#div_reporte_conductual #lbl_cargo").text(data.cargo);
				$("#div_reporte_conductual #lbl_docente").text(data.profesor);
				$("#div_reporte_conductual #lbl_descripcion").text(data.des);
				$("#div_entrevistado_docente #lbl_doc_entrevistado").text(data.profesor);
				$("#div_entrevistado_docente #id_tra").val(data.id_tra);
				$("#div_derivacion_tipo_bulling").hide();
				$("#id_inc").val(data.id_inc);
				//Cargar los tipos de violencia
				var param1={id_cp:data.id};
				_GET({
					context:_URL_CON,
					url:'api/reporteConductual/listarInfractores/',
					params:param1,
					success:function(data){
						console.log(data);
						$('#infractores_tabla').dataTable({
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
					        	 	{"title":"Alumno", "data": "alumno"}
					        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
					        	 	} */
					        	 	//}
						    ]
					    });
						$('#involucrados_tabla').dataTable({
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
					        	 	{"title":"Alumno", "data": "alumno"}
					        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
					        	 	} */
					        	 	//}
						    ]
					    });
						cant_inf=data.length;
						//aca
						//clonar a la cantidad de infractores
						for(i=0; i<cant_inf-1; i++){
							var last=$(".div_entrevistado_alumno").last();
							console.log(last.html());
							divEntrevistadoAlu = last.clone(false);
							$("#entrevistado_alumnos").append(divEntrevistadoAlu);
							//Para el acta de compromiso tipo Alumno
							var last_est=$(".div_estudiantes").last();
							divEstuadiante= last_est.clone(false);
							$("#estudiantes").append(divEstuadiante);
							
							//Para el acta de compromiso tipo Familiar
							var last_fam=$(".div_familiares").last();
							divFamiliar= last_fam.clone(false);
							$("#familiares").append(divFamiliar);
							var last_res=$(".div_responsable").last();
							divResponsable= last_res.clone(false);
							$("#responsables").append(divResponsable);
							
							//Para la derivacion
							var last_inf=$(".div_estudiantes_inf").last();
							divEstuadianteInf= last_inf.clone(false);
							$("#estudiantes_inf").append(divEstuadianteInf);
						} 
						//asignar valores a la olimpoiada
						$(".div_entrevistado_alumno").each(function (index, value) {							
							//Para el formato de entrevistas
							$(this).find("#id_mat").val('');
							$(this).find("#lbl_alu_entrevistado").text(data[index].alumno);
							$(this).find("#des").val('');
							$(this).find("#id_mat").val(data[index].id_mat);
							//Para listar los involucrados
							$("#lista_involucrados").append("<li>" + data[index].alumno + "</li>");
						});
						
						//Para el acta de compromiso tipo Alumno
						$(".div_estudiantes").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].dni);
							$(this).find("#lbl_alumno").text(data[index].alumno);
						});
						
						//Para el acta de compromiso tipo Familiar
						//listar los familiares
						$(".div_familiares").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].nro_doc_fam);
							$(this).find("#lbl_familiar").text(data[index].familiar);
						});
						//listar los responsbles
						$(".div_responsable").each(function (index, value) {
							if(data[index]!=null){
								$(this).find("#lbl_dni").text(data[index].dni);
								$(this).find("#lbl_alumno").text(data[index].alumno);	
							}							
						});
						
						//para el plan de tutoria
						$(".div_responsable_tutoria").each(function (index, value) {
							if(data[index]!=null){
								$(this).find("#lbl_dni").text(data[index].dni);
								$(this).find("#lbl_alumno").text(data[index].alumno);	
							}							
						});
						
						//llenar los infractores para derivacion
						$(".div_estudiantes_inf").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].dni);
							$(this).find("#lbl_alumno").text(data[index].alumno);
						});					

					}
				});
				_GET({
					context:_URL_CON,
					url:'api/reporteConductual/listarFaltas/',
					params:param1,
					success:function(data){
						console.log(data);
						$('#faltas_tabla').dataTable({
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
					        	 	{"title":"Falta", "data": "falta"}
					        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
					        	 	} */
					        	 	//}
						    ], "initComplete": function( settings ) {
								   _initCompleteDT(settings);
							 }
					    });
					}
				});
						console.log(param);
				_GET({
					context:_URL_CON,
					url:'api/formEntrevista/listarEntrevistasDoc/',
					params:{id_inc:id_inc},
					success:function(data){
						console.log(data);
								$("#div_docente").css('display','block');
								$("#div_agredido").css('display','none');
								if(data.length>0){
								$("#div_entrevistado_docente").find("#id_ent_doc").val(data[0].id);
								$("#div_entrevistado_docente").find("#des_doc").val(data[0].des);
								$("#div_entrevistado_docente").find("#des_doc").attr('readonly', true);
								$("#div_entrevistado_docente").find("#btn_grabar").attr('disabled','disabled');
								}
								if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
									$("#div_entrevistado_docente").find("#des_doc").attr('readonly', true);
									$("#div_entrevistado_docente").find("#btn_grabar").attr('disabled','disabled');
								}
								
					}
					});
				/*_GET({
					context:_URL_CON,
					url:'api/formEntrevista/listarEntrevistasDoc/',
					params:param,
					success:function(data){
						alert();
						console.log(data);
								if(data!=null){
								$("#div_entrevistado_docente").find("#id_ent_doc").val(data[0].id);
								$("#div_entrevistado_docente").find("#des_doc").val(data[0].des);
								}
					}
					});*/
				
			} else if(data.id_cti=="2"){
				id_tip_inc=2;
				$("#div_reporte_conductual").hide();
				$("#div_reporte_bulling #lbl_periodo").text(data.periodo);
				$("#div_reporte_bulling #lbl_fecha").text(data.fecha);
				$("#div_reporte_bulling #lbl_informante").text(data.informante);
				$("#div_reporte_bulling #lbl_cargo").text(data.cargo);
				$("#div_reporte_bulling #lbl_agredido").text(data.victima);
				$("#div_reporte_bulling #lbl_grado").text(data.grado);
				$("#div_reporte_bulling #lbl_edad").text(data.edad);
				$("#div_reporte_bulling #lbl_sexo").text(data.sexo);
				$("#div_reporte_bulling #lbl_descripcion").text(data.des);
				$("#id_inc").val(data.id_inc);
				
				_GET({
					context:_URL_CON,
					url:'api/reporteBulling/listarTiposViolencia/',
					params:{id_inc:id_inc},
					success:function(data){
						console.log(data);
						$('#violencia_tabla').dataTable({
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
					        	 	{"title":"Tipo de Violencia", "data": "tipo_violencia"},
					        		{"title":"Tipo de Falta", "data": "tipo_falta"}
					        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
					        	 	} */
					        	 	//}
						    ]
					    });
					}
				});
				
				_GET({
					context:_URL_CON,
					url:'api/reporteBulling/listarAgresores/',
					params:{id_inc:id_inc},
					success:function(data){
						console.log(data);
						cant_agresores=data.length;
						for(i=0; i<cant_agresores-1; i++){
							var last=$(".div_entrevistado_alumno").last();
							console.log(last.html());
							divEntrevistadoAlu = last.clone(false);
							$("#entrevistado_alumnos").append(divEntrevistadoAlu);
							//Para el acta de compromiso tipo Alumno
							var last_est=$(".div_estudiantes").last();
							divEstuadiante= last_est.clone(false);
							$("#estudiantes").append(divEstuadiante);
							
							//Para el acta de compromiso tipo Familiar
							var last_fam=$(".div_familiares").last();
							divFamiliar= last_fam.clone(false);
							$("#familiares").append(divFamiliar);
							var last_res=$(".div_responsable").last();
							divResponsable= last_res.clone(false);
							$("#responsables").append(divResponsable);
							
							//Para la derivacion
							var last_inf=$(".div_estudiantes_inf").last();
							divEstuadianteInf= last_inf.clone(false);
							$("#estudiantes_inf").append(divEstuadianteInf);
						} 
						
						//asignar valores a la olimpoiada
						$(".div_entrevistado_alumno").each(function (index, value) {							
							//Para el formato de entrevistas
							$(this).find("#id_mat").val('');
							$(this).find("#lbl_alu_entrevistado").text(data[index].agresor);
							$(this).find("#des").val('');
							$(this).find("#id_mat").val(data[index].id_mat);
							//Para listar los involucrados
							$("#lista_involucrados").append("<li>" + data[index].agresor + "</li>");
						});
						
						//Para el acta de compromiso tipo Alumno
						$(".div_estudiantes").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].dni);
							$(this).find("#lbl_alumno").text(data[index].agresor);
						});
						
						//Para el acta de compromiso tipo Familiar
						//listar los familiares
						$(".div_familiares").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].nro_doc_fam);
							$(this).find("#lbl_familiar").text(data[index].familiar);
						});
						//listar los responsbles
						$(".div_responsable_tutoria").each(function (index, value) {
							if(data[index]!=null){
								console.log(data[index].agresor);
								$(this).find("#lbl_dni").text(data[index].dni);
								$(this).find("#lbl_alumno").text(data[index].agresor);	
							}							
						});
						
						//listar los responsbles
						$(".div_responsable").each(function (index, value) {
							if(data[index]!=null){
								console.log(data[index].agresor);
								$(this).find("#lbl_dni").text(data[index].dni);
								$(this).find("#lbl_alumno").text(data[index].agresor);	
							}							
						});
						
						//llenar los infractores para derivacion
						$("#div_derivacion_tipo_conductual").css('display','none');
						$("#div_derivacion_tipo_bulling").css('display','block');
						$(".div_agresor").each(function (index, value) {	
							$(this).find("#lbl_dni").text(data[index].dni);
							$(this).find("#lbl_alumno").text(data[index].agresor);
						});	
						
						//para el plan de tutoria
						$(".div_responsable_tutoria").each(function (index, value) {
							if(data[index]!=null){
								$(this).find("#lbl_dni").text(data[index].dni);
								$(this).find("#lbl_alumno").text(data[index].alumno);	
							}							
						});

						$('#agresores_tabla').dataTable({
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
					        	 	{"title":"Agresor", "data": "agresor"}
					        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
					        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
					        	 	} */
					        	 	//}
						    ]
					    });
					}
				});
				
				_GET({
					context:_URL_CON,
					url:'api/reporteBulling/datosVictima/',
					params:{id_inc:id_inc},
					success:function(data){
						$("#div_docente").css('display','none');
						$("#div_agredido").css('display','block');
						$("#div_entrevistado_agredido #lbl_agredido").text(data.victima);
						$("#div_entrevistado_agredido #id_vic").val(data.id_vic);
						$("#div_familiar_agredido").css('display','block');
						$("#div_familiar_agredido #lbl_familiar").text(data.familiar);
						$("#div_estudiante_agredido").css('display','block');
						$("#div_estudiante_agredido #lbl_alumno").text(data.victima);
						$("#div_victima #lbl_alumno").text(data.victima);
				}});
				
				_GET({
					context:_URL_CON,
					url:'api/reporteBulling/listarMotivoBulling/',
					params:{id_inc:id_inc},
					success:function(data){
						console.log(data);
						if(data.tipo="O"){
							$('#motivo_otro').css('display','block');
							$("#lbl_motivo_otro").text(data.motivo);
						} else if(data.tipo="L"){
							$('#agresores_tabla').dataTable({
								 data : data.motivos,
								 aaSorting: [],
								 destroy: true,
								 orderCellsTop:true,
								 select: true,
								 searching: false, 
								 paging: false, 
								 info: false,
						         columns : [
						                    
						        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						        	 	{"title":"Agresor", "data": "agresor"}
						        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
						        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
						        	 	} */
						        	 	//}
							    ]
						    });
						}					
					}
				});
			}
			
			//Llenar datos de la entrevista
			var param={id_inc:data.id_inc};
			_GET({
				context:_URL_CON,
				url:'api/formEntrevista/listarEntrevistasAlu/',
				params:param,
				success:function(data){
					console.log(data);
					//var i=0;	
					var cant_entrevistas_alu=data.length;
					if(data.length>0){
						$(".div_entrevistado_alumno").each(function (index, value){		
							if(data[index]!=null){
								$(this).find("#id_ent_alu").val(data[index].id);
								$(this).find("#des").val(data[index].des);
								//i=i+1;
								$(this).find("#des").attr('readonly', true);
								$(this).find("#btn_grabar").attr('disabled','disabled');
							}
							
							if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
								$(this).find("#des").attr('readonly', true);
								$(this).find("#btn_grabar").attr('disabled','disabled');
							}
							
						});
						//Si ya esta lleno ya las entrevistas le sugerimos que llene los resultados
						if(cant_inf==cant_entrevistas_alu){
							$("#coll-ficha_res_entrevista").show();
							/*
							$('#coll-ficha').on('click', function (e) {
							    e.stopPropagation();
							});
							*/
						} else{
							$("#coll-ficha_res_entrevista").hide();
							$("#coll-acta_comp_alu").hide();
							$("#coll-plan_tutoria").hide();
							$("#coll-acta_comp_pad").hide();
							$("#coll-ficha_derivacion").hide();
							$("#coll-ficha_seg").hide();
							//$("#accordion-styled-group2").sortable('disabled');
							//$("#accordion-styled-group2", context).sortable('disabled');
							//$(".div_result_entrevista").sortable("disabled");
							//$("#accordion-styled-group2").sortable("disable");
						}
						
						
					}	else{
						/*alert(3);
						$(".div_result_entrevista").sortable("disabled");*/
					}					
					
				}
			});
			
			
			
			_GET({
				context:_URL_CON,
				url:'api/formEntrevista/listarEntrevistaVictima/',
				params:param,
				success:function(data){
					console.log(data);
							if(data.length>0){
							$("#div_entrevistado_agredido").find("#id_ent_agr").val(data[0].id);
							$("#div_entrevistado_agredido").find("#des_agr").val(data[0].des);
							$("#div_entrevistado_agredido").find("#des_agr").attr('readonly', true);
							$("#div_entrevistado_agredido").find("#btn_grabar").attr('disabled','disabled');
							}
							
							if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
								$("#div_entrevistado_agredido").find("#des_agr").attr('readonly', true);
								$("#div_entrevistado_agredido").find("#btn_grabar").attr('disabled','disabled');
							}
				}
				});
			
			_GET({
				context:_URL_CON,
				url:'api/formResultEntrevista/listarDetalle/',
				params:param,
				success:function(data){
					console.log(data);
							if(data!=null){
								res_entre=true;
							$("#id_ent_res").val(data.id);
							$("#div_entrevista_res #des").val(data.des);
							$("#div_compromiso_alu #des").val(data.des);
							$("#div_compromiso_fam #des").val(data.des);
							$("#div_entrevista_res #des").attr('readonly', true);
							$("#div_entrevista_res #btn_grabar").attr('disabled','disabled');
							}
							
							if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
								$("#div_entrevista_res #des").attr('readonly', true);
								$("#div_entrevista_res #btn_grabar").attr('disabled','disabled');
							}
							
							if(res_entre){
								$("#coll-acta_comp_alu").show();
							} else{
								$("#coll-acta_comp_alu").hide();
								$("#coll-plan_tutoria").hide();
								$("#coll-acta_comp_pad").hide();
								$("#coll-ficha_derivacion").hide();
								$("#coll-ficha_seg").hide();
							}
				}
				
				});
			
			_GET({
				context:_URL_CON,
				url:'api/incidencia/datosTrabajador/'+_usuario.id,
				params:param,
				success:function(data){
					console.log(data);
							if(data!=null){
							$("#div_directivo_docente #id_dir").val(data.tra_id);
							$("#div_directivo_docente #lbl_dni").text(data.dni);
							$("#div_directivo_docente #lbl_cargo").text(data.rol);
							$("#div_directivo_docente #lbl_trabajador").text(data.trabajador);
							}
				}
				});
			
			_GET({
				context:_URL_CON,
				url:'api/compromiso/listarDetalle/',
				params:param,
				success:function(data){
					console.log(data);
					var cant_com=data.length;
					console.log(cant_com);
					if(cant_com=>1){
						for(i=0; i<cant_com; i++){
							if(data[i].id_ctc==1){
								$("#div_compromiso_alu #id_com_alu").val(data[i].id);
								$("#div_compromiso_alu #des").val(data[i].des);
								$("#div_compromiso_alu #comp").val(data[i].comp);
								$("#div_compromiso_alu #des").attr('readonly', true);
								$("#div_compromiso_alu #comp").attr('readonly', true);
								$("#div_compromiso_alu #btn_grabar").attr('disabled','disabled');
							} else{
								$("#div_compromiso_fam #id_com_fam").val(data[i].id);
								$("#div_compromiso_fam #des").val(data[i].des);
								$("#div_compromiso_fam #comp").val(data[i].comp);
								$("#div_compromiso_fam #des").attr('readonly', true);
								$("#div_compromiso_fam #comp").attr('readonly', true);
								$("#div_compromiso_fam #btn_grabar").attr('disabled','disabled');
							}
						}
							
					}
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
						$("#div_compromiso_fam #comp").attr('readonly', true);
						$("#div_compromiso_fam #btn_grabar").attr('disabled','disabled');
					}
					
					if(cant_inf==cant_com){
						$("#coll-plan_tutoria").show();
						$("#coll-acta_comp_pad").show();
					} else{
						$("#coll-plan_tutoria").hide();
						$("#coll-acta_comp_pad").hide();
						$("#coll-ficha_derivacion").hide();
						$("#coll-ficha_seg").hide();
					}
				}
				});
			
			
			_GET({
				context:_URL_CON,
				url:'api/fichaDerivacion/listarDetalle/',
				params:param,
				success:function(data){
					if(data!=null){
					$('#div_derivacion #id_der').val(data.id);
					$('#div_derivacion #des').val(data.des);
					$('#div_derivacion #id_cdd').val(data.id_cdd);
					$('#div_derivacion #id_cdd').trigger();
					$('#div_derivacion #des').attr('readonly', true);
					$("#div_derivacion #btn_grabar").attr('disabled','disabled');
					}
					
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
						$('#div_derivacion #des').attr('readonly', true);
						$("#div_derivacion #btn_grabar").attr('disabled','disabled');
					}
				}
				});
			
			/*_GET({
				context:_URL_CON,
				url:'api/reporteConductual/listarFaltas/',
				params:param,
				success:function(data){
					console.log(data);
					$('#faltas_tabla').dataTable({
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
				        	 	{"title":"Falta", "data": "falta"}
				        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
				        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
				        	 	} */
				        	 	//}
		/*			    ]
				    });
				}
			});*/
			
			_llenar_combo({
				tabla : 'con_departamento_derivacion',
				combo : $('#id_cdd'),
				text: 'nom',
				context : _URL
			});
			
			var param={id_anio:$('#_id_anio').text()}
			_llenar_combo({
				url : 'api/trabajador/listarProfesoresTutores',
				params: param,
				combo : $('#id_enc'),
				text: 'trabajador',
				context : _URL
			});
			/*_llenar_combo({
				tabla : 'con_departamento_derivacion',
				combo : $('#id_cdd'),
				text: 'nom',
				context : _URL
			});*/ //trabajador
			
			//Llenar datos de la entrevista
			
			var param={id_inc:data.id_inc};
			alert(id_inc);
			alert(data.id_inc);
			_GET({
				context:_URL_CON,
				url:'api/planTutoria/listarDetalle/',
				params:param,
				success:function(data){
					console.log(data);
					if(data!=null){
						$("#div_plan_tutoria #id_plan_tu").val(data.id);
						$("#div_plan_tutoria #fun").val(data.fun);
						$("#div_plan_tutoria #obj").val(data.obj);
						$("#div_plan_tutoria #eva").val(data.eva);
						$("#div_plan_tutoria #fun").attr('readonly', true);
						$("#div_plan_tutoria #obj").attr('readonly', true);
						$("#div_plan_tutoria #eva").attr('readonly', true);
						$("#div_plan_tutoria #btn_grabar").attr('disabled','disabled');
						
						_GET({
							context:_URL_CON,
							url:'api/planTutoria/listarActividades/',
							params:{id_plan:$("#div_plan_tutoria #id_plan_tu").val()},
							success:function(data){
								console.log(data);
										if(data.length>0){
											cant_act=data.length;
											console.log(cant_act);
											for(var i=0; i<cant_act-1;i++){
												var last=$(".div_actividad").last();
												divActividad = last.clone(false);
												$("#div_actividades").append(divActividad);
											}
											$('.div_actividad').each(function (index){
												$(this).find("#id_plan_act").val(data[index].id);
												$(this).find("#fec").val(data[index].fec);
												$(this).find("#des").val(data[index].des);
												$(this).find("#fec").attr('readonly', true);
												$(this).find("#des").attr('readonly', true);
												$(this).find("#id_enc").val(data[index].id_enc);
												$(this).find("#id_enc").trigger("change");
											});
											$("#coll-ficha_derivacion").show();
											$("#coll-ficha_seg").show();
										} else{
											$("#coll-ficha_derivacion").hide();
											$("#coll-ficha_seg").hide();
										}
							}
							});
					} else{
						$("#coll-ficha_derivacion").hide();
						$("#coll-ficha_seg").hide();
					}	
					
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
						$("#div_plan_tutoria #fun").attr('readonly', true);
						$("#div_plan_tutoria #obj").attr('readonly', true);
						$("#div_plan_tutoria #eva").attr('readonly', true);
						$("#div_plan_tutoria #btn_grabar").attr('disabled','disabled');
					}
				}
			});

			var param={id_inc:id_inc};
			_GET({
				context:_URL_CON,
				url:'api/fichaSeguimiento/listarSeguimiento/',
				params:param,
				success:function(data){
					console.log(data);
					if(data.length>0){
						$("#div_ficha_seguimiento #id_fic_seg").val(data[0].id_cfg);
						if(data[0].cierre==1)
							$("#div_ficha_seguimiento #cierre").attr("checked","checked");
						$("#div_ficha_seguimiento #cierre").val()
						$('#table_actividades').dataTable({
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
					        	 	{"title":"Fecha", "data" : "fec"},
					        	 	{"title":"Acción", "data" : "des"},
					        	 	{"title":"Encargado", "data" : "trabajador"},
					        	 	{"title":"Resultado", "render":function ( data, type, row,meta ) {
					        	 		var res = (row.res!=null) ? row.res: '';
					        	 		var id_cpa = (row.id_cpa!=null) ? row.id_cpa: '';
					        	 		var id_cfsd = (row.id_cfsd!=null) ? row.id_cfsd: '';
					        	 		return "<div class='seguimiento_detalle'><input type='hidden' id='id_seg_det' name='id_seg_det' class='id_seg_det' value='"+id_cfsd+"'/><input type='hidden' id='id_cpa' name='id_cpa' value='"+row.id_cpa+"'/><input type='text' id='res'  name ='res' value='" + res+ "' class='form-control' /></div>";
					        	 	}}
					        	 	/*{"title":"Nueva Acciòn", "render":function ( data, type, row,meta ) {
					        	 		var acc = (row.acc!=null) ? row.acc: '';
					        	 		return "<input type='text' id='acc'  name ='acc' value='" + acc+ "' class='form-control' />";
					        	 	} 
					        	 	}*/
						    ]
					    });
					}
				}
			});
			
			
			
		}
	
	});
	
	$('#div_plan_tutoria #btn_agregar_actividad').on('click',function(event){
		var last=$(".div_actividad").last();
		console.log(last.html());
		divActividad = last.clone(false);
		//divActividad.find("#fec").val('');
		divActividad.find("#des").val('');
		divActividad.find("#id_enc").val('');
		$("#div_actividades").append(divActividad);
		_inputs();
	});

}

//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
});

function grabarEntrevista(obj){
		console.log(obj);
		var frm_entrevista=$(obj).parent().parent().parent();
		//var dni=frm_entrevista.find("#nro_dni").val();
		console.log(frm_entrevista.html());
		var json_entrevista={};
		json_entrevista.id_inc=$("#id_inc").val();
		json_entrevista.id_cfi=$("#div_entrevistados #id_cfi").val();
		json_entrevista.id_ent_doc=frm_entrevista.find("#id_ent_doc").val();;
		json_entrevista.id_tra=frm_entrevista.find("#id_tra").val();
		json_entrevista.des_doc=frm_entrevista.find("#des_doc").val();
		json_entrevista.id_ent_alu=frm_entrevista.find("#id_ent_alu").val();
		json_entrevista.id_mat=frm_entrevista.find("#id_mat").val();
		json_entrevista.des_alu=frm_entrevista.find("#des").val();
		json_entrevista.id_ent_vic=frm_entrevista.find("#id_ent_agr").val();
		json_entrevista.id_vic=frm_entrevista.find("#id_vic").val();
		json_entrevista.des_vic=frm_entrevista.find("#des_agr").val();
		console.log(json_entrevista);
		//Usado cuando se graba con un solo boton todo
		/*json_entrevista.entrevistados_alumnos=[];
		
		var entrevistados_alu=[];
		var ent_alu={};
		$('.entrevistado_alumnos').each(function (index){
			ent_alu.id_ent_alu=$(this).find("#id_ent_alu").val();
			ent_alu.id_mat=$(this).find("#id_mat").val();
			ent_alu.des=$(this).find("#des").val();
			entrevistados_alu.push(ent_alu);
		});
		
		json_entrevista.entrevistados_alumnos=entrevistados_alu;*/
		
		_POST({
			context:_URL_CON,
			url:'api/formEntrevista',
			params:JSON.stringify(json_entrevista),
			contentType:"application/json",
			success: function(data){
				console.log(data);
				/*$('.entrevistado_alumnos').each(function (index){
					$(this).find("#id_ent_alu").val(data.id);
				});	*/
				//funcionalidad por probar
				console.log(frm_entrevista.html());
				if(data.result.tipo=='A'){
					frm_entrevista.find("#id_ent_alu").val(data.result.id_en);
					frm_entrevista.find("#des").attr('readonly', true);
					frm_entrevista.find("#btn_grabar").attr('disabled','disabled');
				} else if(data.result.tipo=='D'){
					frm_entrevista.find("#id_ent_doc").val(data.result.id_en);
					frm_entrevista.find("#des").attr('readonly', true);
					frm_entrevista.find("#btn_grabar").attr('disabled','disabled');
				} else if(data.result.tipo=='V'){
					frm_entrevista.find("#id_ent_vic").val(data.result.id_en);
					frm_entrevista.find("#des_agr").attr('readonly', true);
					frm_entrevista.find("#btn_grabar").attr('disabled','disabled');
				}
				//frm_entrevista.find("#id_ent_alu")
			}
		});	
};

function grabarResultEntrevista(obj){

	var json_entrevista_result={};
	json_entrevista_result.id_res_ent=$("#id_ent_res").val();
	json_entrevista_result.id_inc=$("#id_inc").val();
	json_entrevista_result.id_cfi=$("#div_entrevista_res #id_cfi").val();
	json_entrevista_result.des=$("#div_entrevista_res #des").val();
		
	_POST({
		context:_URL_CON,
		url:'api/formResultEntrevista',
		params:JSON.stringify(json_entrevista_result),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_ent_res").val(data.result);	
			$("#div_compromiso_alu #des").val($("#div_entrevista_res #des").val());
			$("#div_compromiso_alu #fam").val($("#div_entrevista_res #des").val());
			$("#div_entrevista_res #des").attr('readonly', true);
			$("#div_entrevista_res #btn_grabar").attr('disabled','disabled');
			
		}
	});	
};

function grabarCompromisoAlu(obj){

	var json_compromiso_alu={};
	json_compromiso_alu.id_comp_alu=$("#id_comp_alu").val();
	json_compromiso_alu.id_inc=$("#id_inc").val();
	json_compromiso_alu.id_cfi=$("#div_compromiso_alu #id_cfi").val();
	json_compromiso_alu.id_dir=$("#div_compromiso_alu #id_dir").val();
	json_compromiso_alu.id_ctc=$("#div_compromiso_alu #id_ctc").val();
	json_compromiso_alu.des=$("#div_compromiso_alu #des").val();
	json_compromiso_alu.comp=$("#div_compromiso_alu #comp").val();	
	_POST({
		context:_URL_CON,
		url:'api/compromiso',
		params:JSON.stringify(json_compromiso_alu),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_com_alu").val(data.result);	
			$("#div_compromiso_alu #des").attr('readonly', true);
			$("#div_compromiso_alu #comp").attr('readonly', true);
			$("#div_compromiso_alu #btn_grabar").attr('disabled','disabled');
		}
	});	
};

function grabarCompromisoFam(obj){

	var json_compromiso_fam={};
	json_compromiso_fam.id_comp_alu=$("#id_comp_fam").val();
	json_compromiso_fam.id_inc=$("#id_inc").val();
	json_compromiso_fam.id_cfi=$("#div_compromiso_fam #id_cfi").val();
	json_compromiso_fam.id_dir=$("#div_compromiso_fam #id_dir").val();
	json_compromiso_fam.id_ctc=$("#div_compromiso_fam #id_ctc").val();
	json_compromiso_fam.des=$("#div_compromiso_fam #des").val();
	json_compromiso_fam.comp=$("#div_compromiso_fam #comp").val();	
	_POST({
		context:_URL_CON,
		url:'api/compromiso',
		params:JSON.stringify(json_compromiso_fam),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_comp_fam").val(data.result);	
			$("#div_compromiso_fam #des").attr('readonly', true);
			$("#div_compromiso_fam #comp").attr('readonly', true);
			$("#div_compromiso_fam #btn_grabar").attr('disabled','disabled');
		}
	});	
};

function grabarDerivacion(obj){

	var json_derivacion={};
	json_derivacion.id_der=$("#id_der").val();
	json_derivacion.id_inc=$("#id_inc").val();
	json_derivacion.id_cfi=$("#div_derivacion #id_cfi").val();
	json_derivacion.id_dir=$("#div_derivacion #id_dir").val();
	json_derivacion.id_cdd=$("#div_derivacion #id_cdd").val();
	json_derivacion.des=$("#div_derivacion #des").val();
		
	_POST({
		context:_URL_CON,
		url:'api/fichaDerivacion',
		params:JSON.stringify(json_derivacion),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_der").val(data.result);	
			$("#div_derivacion #des").attr('readonly', true);
			$("#div_derivacion #btn_grabar").attr('disabled','disabled');
		}
	});	
};

function grabarPlantutoria(obj){
	alert($("#id_inc").val());
	var json_plan_tutoria={};
	json_plan_tutoria.id_plan_tu=$("#id_plan_tu").val();
	json_plan_tutoria.id_inc=$("#id_inc").val();
	json_plan_tutoria.id_cfi=$("#div_plan_tutoria #id_cfi").val();
	json_plan_tutoria.fun=$("#div_plan_tutoria #fun").val();
	json_plan_tutoria.obj=$("#div_plan_tutoria #obj").val();	
	json_plan_tutoria.eva=$("#div_plan_tutoria #eva").val();
	var plan_actividades = [];
	
	$(".div_actividad").each(function (index, value) {
		var actividad={};
		actividad.id_plan_act=$(this).find("#id_plan_act").val()
		actividad.fec=$(this).find("#fec").val();
		actividad.des=$(this).find("#des").val();
		actividad.id_enc=$(this).find("#id_enc").val();
		plan_actividades.push(actividad);
	});
	json_plan_tutoria.planActividadReq=plan_actividades;	

	_POST({
		context:_URL_CON,
		url:'api/planTutoria',
		params:JSON.stringify(json_plan_tutoria),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_plan_tu").val(data.result.id_plan);
			$("#div_plan_tutoria #fun").attr('disabled','disabled');
			$("#div_plan_tutoria #fun").attr('readonly', true);
			$("#div_plan_tutoria #obj").attr('readonly', true);
			$("#div_plan_tutoria #eva").attr('readonly', true);
			$("#div_plan_tutoria #btn_grabar").attr('disabled','disabled');
			$('.div_actividad').each(function (index){
				console.log(index);
				$(this).find("#id_plan_act").val(data.result.id_plan_act[index].id);
			});			
		}
	});
};

function grabarSeguimiento(obj){

	var json_ficha_seguimiento={};
	json_ficha_seguimiento.id_seg=$("#id_fic_seg").val();
	json_ficha_seguimiento.id_inc=$("#id_inc").val();
	json_ficha_seguimiento.id_cfi=$("#div_ficha_seguimiento #id_cfi").val();
	if ($("#div_ficha_seguimiento #cierre").is(':checked')){
		json_ficha_seguimiento.cierre=1;
	} else{
		json_ficha_seguimiento.cierre=0;
	}
	//json_ficha_seguimiento.cierre=$("#div_ficha_seguimiento #cierre").val();
	var resultados = [];
	
	$(".seguimiento_detalle").each(function (index, value) {
		var resultado={};
		resultado.id_seg_det=$(this).find("#id_seg_det").val()
		resultado.id_cpa=$(this).find("#id_cpa").val();
		resultado.res=$(this).find("#res").val();
		resultados.push(resultado);
	});
	json_ficha_seguimiento.fichaSeguimientoDetReq=resultados;	

	_POST({
		context:_URL_CON,
		url:'api/fichaSeguimiento',
		params:JSON.stringify(json_ficha_seguimiento),
		contentType:"application/json",
		success: function(data){
			console.log(data);
			$("#id_fic_seg").val(data.result.id_seg);
			$("#div_ficha_seguimiento #res").attr('readonly', true);
			$("#div_ficha_seguimiento #btn_grabar").attr('disabled','disabled');
			$('.seguimiento_detalle').each(function (index){
				console.log(index);
				$(this).find("#id_seg_det").val(data.result.id_seg_det[index].id);
			});			
		}
	});
};

function imprimirFichasEntrevistas(obj){
	//console.log(obj);
	var frm_entrevista=$(obj).parent().parent().parent();
	var tipo_entrevista=null;
	var id_ent=null;
	var id_ent_alu=frm_entrevista.find("#id_ent_alu").val();
	var id_ent_doc=frm_entrevista.find("#id_ent_doc").val();
	var id_ent_agr=frm_entrevista.find("#id_ent_agr").val();
	if(id_ent_alu!=null){
		id_ent=id_ent_alu;
		tipo_entrevista='A';
	} else if(id_ent_doc!=null){
		id_ent=id_ent_doc;
		tipo_entrevista='D';
	} else if(id_ent_agr!=null){
		id_ent=id_ent_agr;
		tipo_entrevista='V';
	}		
	console.log($(obj).parent().parent().parent().html());
	console.log(id_ent);
	_download(_URL_CON+'api/incidencia/imprimirFichaEntrevista?id_ent='+id_ent+'&tipo='+tipo_entrevista,'Ficha_Entrevista.pdf');
}

function imprimirResultadoEntrevista(obj){
	_download(_URL_CON+'api/incidencia/imprimirResultadoEntrevista?id_inc='+$("#id_inc").val(),'Ficha_Resultado.pdf');
}

function imprimirActaCompromisoAlu(obj){
	
	_download(_URL_CON+'api/incidencia/imprimirCompromisoAlumno?id_com='+$("#id_com_alu").val()+'&id_inc='+$("#id_inc").val(),'Acta_Compromiso_Alumnos.pdf');
}

function imprimirPlanTutoria(obj){
	_download(_URL_CON+'api/incidencia/imprimirPlanTutoria?id_cpa='+$("#id_plan_tu").val()+'&id_inc='+$("#id_inc").val(),'Plan_Tutoria.pdf');
}

function imprimirFichaDerivacion(obj){
	//console.log(obj);

	_download(_URL_CON+'api/incidencia/imprimirFichaDerivacion?id_fic_der='+$("#id_der").val()+'&id_inc='+$("#id_inc").val()+'&id_cti='+id_tip_inc ,'Ficha_Derivacion.pdf');
}

function imprimirFichaSeguimiento(obj){
	//console.log(obj);

	_download(_URL_CON+'api/incidencia/imprimirFichaSeguimiento?id_fic_seg='+$("#id_fic_seg").val()+'&id_inc='+$("#id_inc").val() ,'Ficha_Seguimiento.pdf');
}

var form = $(".steps-validation").show();
//$('.select').select2();
//Initialize wizard
$(".steps-validation").steps({
headerTag: "h6",
bodyTag: "fieldset",
transitionEffect: "fade",
titleTemplate: '<span class="number">#index#</span> #title#',
autoFocus: true,
onStepChanging: function (event, currentIndex, newIndex) {

    // Allways allow previous action even if the current form is not valid!
    if (currentIndex > newIndex) {
        return true;
    }

    // Forbid next action on "Warning" step if the user is to young
    if (newIndex === 3 && currentIndex==2) {
  	  var vuelto = $('#vuelto').val();

			if ($('#abono').val() == "") {
				_alert_error('Debe ingresar el monto a pagar',
						function() {
							return false;
						});
				return false;
			}

			if (parseFloat(vuelto) < 0) {
				_alert_error(
						'Monto insuficiente para cubirar el traslado',
						function() {
							return false;
						});
				return false;
			}

			swal(
					{
						title : "Esta seguro?",
						text : "Se procedera a trasladar al alumno:"
								+ $("#label_alumno").text(),
						type : "warning",
						showCancelButton : true,
						confirmButtonColor : "rgb(33, 150, 243)",
						cancelButtonColor : "#EF5350",
						confirmButtonText : "Si, Trasladar",
						cancelButtonText : "No, cancela!!!",
						closeOnConfirm : false,
						closeOnCancel : false
					},
					function(isConfirm) {
						if (isConfirm) {

							$('#frm-traslado #id_suc').val(_usuario.id_suc);
							$('#id_sit').removeAttr('disabled');
							_post(
									"api/trasladoDetalle",
									$('#frm-traslado')
											.serialize(),
									function(data) {
										console.log(data);
										$("#id_sit").prop('disabled', 'disabled');
										_post_json(
												"http://localhost:8081/api/print",
												data.result,
												function(data) {

												});


										form.steps("next");
									});

						} else {
							swal({
								title : "Cancelado",
								text : "No se ha realizado el traslado",
								confirmButtonColor : "#2196F3",
								type : "error"
							});

							return false;
						}
					});

			return false;
    }

    // Needed in some cases if the user went back (clean up)
    if (currentIndex < newIndex) {

        // To remove error styles
        form.find(".body:eq(" + newIndex + ") label.error").remove();
        form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
    }

    form.validate().settings.ignore = ":disabled,:hidden";
    return form.valid();
},

onStepChanged: function (event, currentIndex, priorIndex) {
	
    // Used to skip the "Warning" step if the user is old enough.
	if (currentIndex === 1){
		//onloadMatricula();
	}
    if (currentIndex === 2 && Number($("#age-2").val()) >= 18) {
        form.steps("next");
    }

    // Used to skip the "Warning" step if the user is old enough and wants to the previous step.
    if (currentIndex === 2 && priorIndex === 3) {
        form.steps("previous");
    }
},

onFinishing: function (event, currentIndex) {
    form.validate().settings.ignore = ":disabled";
    return form.valid();
},

onFinished: function (event, currentIndex) {
    alert("Submitted!");
}
});

//Initialize validation
$(".steps-validation").validate({
ignore: 'input[type=hidden], .select2-search__field', // ignore hidden fields
errorClass: 'validation-error-label',
successClass: 'validation-valid-label',
highlight: function(element, errorClass) {
    $(element).removeClass(errorClass);
},
unhighlight: function(element, errorClass) {
    $(element).removeClass(errorClass);
},

// Different components require proper error label placement
errorPlacement: function(error, element) {

    // Styled checkboxes, radios, bootstrap switch
    if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container') ) {
        if(element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
            error.appendTo( element.parent().parent().parent().parent() );
        }
         else {
            error.appendTo( element.parent().parent().parent().parent().parent() );
        }
    }

    // Unstyled checkboxes, radios
    else if (element.parents('div').hasClass('checkbox') || element.parents('div').hasClass('radio')) {
        error.appendTo( element.parent().parent().parent() );
    }

    // Input with icons and Select2
    else if (element.parents('div').hasClass('has-feedback') || element.hasClass('select2-hidden-accessible')) {
        error.appendTo( element.parent() );
    }

    // Inline checkboxes, radios
    else if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
        error.appendTo( element.parent().parent() );
    }

    // Input group, styled file input
    else if (element.parent().hasClass('uploader') || element.parents().hasClass('input-group')) {
        error.appendTo( element.parent().parent() );
    }

    else {
        error.insertAfter(element);
    }
},
rules: {
    email: {
        email: true
    }
}
});

// Accordion component sorting
/*$(".accordion-sortable").sortable({
    connectWith: '.accordion-sortable',
    items: '.panel',
    helper: 'original',
    cursor: 'move',
    handle: '[data-action=move]',
    revert: 100,
    containment: '.content-wrapper',
   // disabled: true ,
    forceHelperSize: true,
    placeholder: 'sortable-placeholder',
    forcePlaceholderSize: true,
    tolerance: 'pointer',
    start: function(e, ui){
        ui.placeholder.height(ui.item.outerHeight());
    }
});


// Collapsible component sorting
$(".collapsible-sortable").sortable({
    connectWith: '.collapsible-sortable',
    items: '.panel',
    helper: 'original',
    cursor: 'move',
    handle: '[data-action=move]',
    revert: 100,
    containment: '.content-wrapper',
    //disabled: true ,
    forceHelperSize: true,
    placeholder: 'sortable-placeholder',
    forcePlaceholderSize: true,
    tolerance: 'pointer',
    start: function(e, ui){
        ui.placeholder.height(ui.item.outerHeight());
    }
});*/
