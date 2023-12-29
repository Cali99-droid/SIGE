//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
var _id_gpf=null;
var _ape_pat=null;
var _ape_mat=null;
function onloadPage(params){
	_onloadPage();
	var tipo=params.tipo;
	var id_usr=_usuario.id;
	llenarTabla(id_usr);
	if(_id_gpf!=null){
		$('#frm-alumno_mant #btn_nuevo').click();
	}	
	
	//onchange
	
	
	function llenarTabla(id_usr){
				var param={id_usr:id_usr};
				_get('api/familiar/listarHijosFamilia',function(data){
					$('#tabla-alumnos').dataTable({
					 data : data,
					 /*
					 destroy: true,
					 select: true,
					 bFilter: false,
					 */
					 searching: false, 
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
					   console.log('1: '+codigo);
					   _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   $("#frm-matricula #id_alu").val(data.id_alu);
						   $("#alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
							listarMatriculas(data.id_alu);
							$("input:checkbox[name='tc_acept']").on("click", function(e) {
							if($("#frm-matricula input[name='tc_acept']").is(':checked')){
								$("#frm-matricula #tc_acept").val(1);
								$("#btn_matricular").removeAttr('disabled');
							} else{
								$("#frm-matricula #tc_acept").val(0);
								$("#btn_matricular").attr('disabled','disabled');
							}
							});
							console.log('2: ');
							
							$('#id_per_res').off('change');
						   
						    //Cargamos los datos
						    $('#id_per_res').on('change',function(event){
								var parentesco = $('#id_per_res option:selected').attr('data-aux1');
								var celular = $('#id_per_res option:selected').attr('data-aux2');
								var tipo_doc = $('#id_per_res option:selected').attr('data-aux4');
								var nro_doc= $('#id_per_res option:selected').attr('data-aux3');
								var correo = $('#id_per_res option:selected').attr('data-aux5');
								$('#id_fam_par').val(parentesco);
								$('#cel').val(celular);
								$('#corr').val(correo);
								$('#id_fam_dni').val(nro_doc);
								_llenarCombo('cat_tipo_documento',$('#id_tdc'), tipo_doc,null,function(){
								$('#id_tdc').change();
							});
							});
							
						   var param={id_gpf:data.id_gpf, id_alu:data.id_alu};
							_llenarComboURL('api/alumno/listarTodosIntegrantesFamilia', $('#id_per_res'),
								data.id_per_res, param, function() {
									$('#id_per_res').change();
							});
								
							$('#id_gpf').attr("disabled", "disabled");
							_llenar_combo({
							url:'api/alumno/listarFamilias',
							combo:$('#id_gpf'),
							text:'nom',
							context: _URL,
							id:data.id_gpf,
							function(){
								$('#id_gpf').change();
							}
							});	
														
							/*$('#id_grad').on('change',function(event){
								var param={id_cic:$('#id_cic').val(),id_grad:$('#id_grad').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),
								data.id_au, param, function() {
									$('#id_au').change();
								});
							});*/
							
							$('#nro_cuo').on('change',function(event){
								$('#cuotas-tabla').html('');
								//$('#cuotas-tabla').find('tbody').html('');
								var nro_cuotas=$('#nro_cuo').val();
								if(nro_cuotas){
									var id_cct=$('#id_tur option:selected').attr("data-aux1");
									
									for (var i=1;i<= nro_cuotas; i++) {
										var monto=300;
										var fec_venc='19/01/2021';
										var estado='cancelado';
										//var div="<p><input type='checkbox' id='nro_cuota'"+i+" name='nro_cuota' value="+i+" />"+i+"</p>";
										var div="<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Cuota"+i+"</label><input type='hidden' name='nro_cuota' id='nro_cuota"+i+"' value="+i+"><input type='text' name='monto' id='monto"+i+"' value="+monto+" class='form-control' readonly='readonly'>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Fecha Límite de Pago</label><input type='text' name='fec_venc' id='fec_venc"+i+"' value="+fec_venc+" class='form-control ' readonly='readonly'>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Banco</label><select name='id_bco_pag'  id='id_bco_pag"+i+"' data-placeholder='Seleccione' class='form-control select-search' required><option value=''>Seleccione</option><option value='1'>Banco de Crédito del Perú</option><option value='2'>Banco Continental</option></select>"
													+"</div>"
												+"</div>"
												+"<div class='col-md-3'>"
													+"<div class='form-group'>"
													+"<label>Estado</label><input type='text' name='estado' id='estado"+i+"' value="+estado+" class='form-control' readonly='readonly'>"
													+"</div>"
												+"</div>"
										$("#cuotas-tabla").append(div);
										/*$('.daterange-single').daterangepicker({ 
											singleDatePicker: true,
											autoUpdateInput: true,
											locale: { format: 'DD/MM/YYYY'}
										});*/
												//alert(i);
												var param={id_cct:id_cct, nro_cuo:i,nro_cuo_total:nro_cuotas,id_alu:data.id_alu,id_per:$('#frm-matricula #id_per').val()};
												_get('api/confPagosCiclo/obtenerDatosCuotaxCiclo',function(data){
												console.log(data);
												
												//if(data){
													//alert('entroooo');
													monto=(data.monto_total!=null) ? data.monto_total: '';
													//alert(id_cuota);
													//costo=(data.costo!=null) ? data.costo: cuota_men;
													fec_venc=(data.fec_venc!=null) ? data.fec_venc: '19/01/2021'; 
													estado=(data.estado!=null) ? data.estado: '';
													var numero_cuota=data.nro_cuota;
												//}
												
												$("input[name=nro_cuota]").each(function (index) {  
													if($(this).val()==numero_cuota){
														//Si es igual le pongo check
														$("#monto"+numero_cuota).val(monto);
														$("#fec_venc"+numero_cuota).val(fec_venc);
														$("#estado"+numero_cuota).val(estado);
														//$("#fec_venc"+numero_cuota).val(fec_venc);
														/*$('.daterange-single').daterangepicker({ 
															singleDatePicker: true,
															autoUpdateInput: true,
															locale: { format: 'DD/MM/YYYY'}
														});*/
													}
												});
																
												//crearTabla(j,id_cuota,costo, fec_venc);
												},param);
									}
									listarResumenPagos(id_cct,data.id_alu,$('#frm-matricula #id_per').val());
								}
							});	
							$('#id_tur').off('change');
							$('#id_tur').on('change',function(event){
								var param={id_cic:$('#id_cic').val(),id_tur:$('#id_tur').val()};
								_llenarComboURL('api/aula/listarGradosxTurnoCiclo', $('#id_grad'),
								null, param, function() {
									$('#id_grad').change();
								});
								var nro_cuo=$('#id_tur option:selected').attr("data-aux2");
								var id_cct=$('#id_tur option:selected').attr("data-aux1");
								$('#id_cct').val(id_cct);
								if(nro_cuo== 'undefined'){
									swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "No existen configurado costos para este ciclo en este turno, por favor comunicarse con el Administrador.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;
								} else {
										var valor = $('#nro_cuo').find("option:first-child").val();
										var _id=null;
										if (valor==''){
											$('#nro_cuo').find('option').not(':first').remove();
										}else{
											$('#nro_cuo').empty();
										}
											for(var i=1;i<=nro_cuo;i++)
											{
												var id = i;
												var value = i;
												$('#nro_cuo').append('<option value="'+ id +'">' +value + '</option>');
											}

											  if (typeof _id != 'undefined'&& _id!=null ){
												  if($('#nro_cuo').hasClass("select-search"))
														$('#nro_cuo').val(_id).change();
												  else
														$('#nro_cuo').val(_id);
											 }
											  if (typeof funcion != 'undefined'&& funcion!=null ){
												  funcion();
											  }	
								}
								$('#nro_cuo').change();								
							});	
							
							$("#id_cic").off("change");
							$('#id_cic').on('change',function(event){
								var param1={id_cic: $('#id_cic').val()};
								_llenarComboURL('api/periodo/listarTurnosExistentesxCiclo',$('#id_tur'),null,param1,function(){
									$('#id_tur').change();
								});
								var id_niv=$('#id_cic option:selected').attr("data-aux2");
								//_llenarCombo('cat_grad_todos',$('#id_grad'),null,id_niv,function(){$('#id_grad').change();});
								$('#id_niv').val(id_niv);
							});	
							
							$("#id_per").off("change");
							$('#id_per').on('change',function(event){
								var param={id_per:$('#id_per').val()};
								_llenarComboURL('api/periodo/listarCiclosCombo', $('#id_cic'),
								data.id_cic, param, function() {
									$('#id_cic').change();
								});
							});	
							
							/*$('#id_suc').on('change',function(event){
								var param={id_anio:id_anio, id_gir:$('#id_gir').val(),id_suc:$('#id_suc').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									//id: data.id_per,
									funcion:function(){
										$('#id_per').change();
									}
								});
							});	*/
							
							$("#id_gir").off("change");

							$('#id_gir').on('change',function(event){
								console.log('3 :');
								var id_gir=$('#id_gir').val();
								if(id_gir==3){
									$('#tipo').val('V');
									$('#lbl_grado').text('Grado');
								} else if(id_gir==2){
									$('#tipo').val('A');
									$('#lbl_grado').text('Aula');
								}
								if($('#id_gir').val()==null){
									$('#id_gir').val(2);
								}
								
								var param={id_anio:id_anio, id_gir:$('#id_gir').val()};
								_llenar_combo({
									url:'api/periodo/listar',
									combo:$('#id_per'),
									params: param,
									context:_URL,
									text:'value',
									//id: data.id_per,
									funcion:function(){
										$('#id_per').change();
									}
								});
								/*_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
								$('#id_suc').change();*/
							//});
								
							});	
							
							$('#id_gir').change();
							/*_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
								$('#id_gir').change();
							});*/

							//Listar las matriculas del alumno
							var param={id_alu:data.id_alu};
							 _get('api/alumno/listarMatriculas',function(data){
								 $('#tab_matriculas').dataTable({
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
										{"title":"Año", "data" : "anio"}, 
										{"title":"N", "data" : "niv"}, 
										{"title":"G", "data" : "grado"}, 
										{"title":"S", "data" : "secc"}, 
										{"title":"Salón", "data" : "salon"}, 
										{"title":"Servicio", "data" : "servicio"}, 
										{"title":"Estado", "data" : "estado"}
								],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
							 },param);	 
							
					   });
					}
					);

					$('.ok').on('click', function(e){
						alert($("#table tr.selected td:first").html());
					});
	    			 }
			});
		},param);	
	}

$('#frm-matricula #btn-consultar_vac').on('click',function(event){
	 consultar_vacantes();
});

}

$('#btn_nuevo').on('click',function(event){
	form.steps("previous");
	form.steps("previous");
	form.steps("previous");
		//$("#frm-matricula [name=actyc]").val('');
		$("#frm-matricula [name=actyc]").attr("checked",false);
		//$("#frm-matricula [name=srvint]").val('');
		$("#frm-matricula [name=srvint]").attr("checked",false);
		//$("#frm-matricula [name=camweb]").val('');
		$("#frm-matricula [name=camweb]").attr("checked",false);
		$('#frm-matricula #id').val('');
		$('#id_familias').attr("disabled", "disabled");
		$("#id_per_res").removeAttr('disabled');
		$("#id_gir").removeAttr('disabled');
		$("#id_per").removeAttr('disabled');
		//$("#id_suc").removeAttr('disabled');
		$('#id_cic').removeAttr('disabled');
		$('#id_tur').removeAttr('disabled');
		$("#id_per_res").val('').trigger('change');
		//$("#id_suc").val('').trigger('change');
		$("#id_gir").val('').trigger('change');
		$('#id_cic').val('').trigger('change');
		$("#id_per").val('').trigger('change');
		$('#id_grad').val('').trigger('change');
		$('#id_au').val('').trigger('change');
		$('#id_tur').val('').trigger('change');
		
		$('#div_aula').hide();
		$('#id_au').attr("disabled", "disabled");
		$('#resumen-tabla').html('');
		$('#total_costo').val('');
		//llenarDatos($('#frm-matricula #id_gpf').val(), null);
		
		//form.steps("previous");
});

function listarResumenPagos(id_cct, id_alu, id_per){
	var param = {
		id_cct : $('#id_cct').val(),
		nro_cuotas : $('#nro_cuo').val(),
		id_alu: id_alu,
		id_per: id_per
		};
	_get("api/confPagosCiclo/ResumendePagos", function(data) {
		$('#resumen-tabla').dataTable(
								{	data : data.listPagos,
									aaSorting : [],
									destroy : true,
									orderCellsTop : true,
									select : true,
									bLengthChange : false,
									bPaginate : false,
									bFilter : false,
									bInfo : false,
									columns : [
											{"title" : "Nro","render" : function(data,type,row,meta) {return parseInt(meta.row) + 1;}},
											{"title" : "Descripción","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return '<B>'+row.descripcion+'</B>';
												else if (row.tip == 'DESC')
													return '<B>'+row.descripcion+'</B>';
												else 
													return null;}},
											{"title" : "Monto","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return 'S/.' + $.number(row.monto,2);
												else if (row.tip == 'DESC')
													return '<font color="red">- S/.' + $.number(row.monto,2)+'</font>';
												else
													return null; }}
									]
					});
					//alert(data.monto_total);
					$('#total_costo').val(data.monto_total);
		
	},param);	
}	

function consultar_vacantes(){
	if($('#id_grad').val()!=''){								
								var param = {
								id_cic : $('#id_cic').val(),
								id_grad : $('#id_grad').val()
								};
							_get("api/matricula/capacidadAcadVac", function(data) {

								console.log(data);
								if(data!=null){
									var nro_vac = data;
									//alert('nro_vac'+nro_vac);
									if(nro_vac>0){
										swal(
										{
											title : "Desea continuar con el proceso de PRE MATRÍCULA?",
											text :'Existen <font id="_seconds">' + nro_vac+ '</font> vacantes.',
											type: "success",
											showCancelButton : true,
											confirmButtonColor : "rgb(33, 150, 243)",
											cancelButtonColor : "#EF5350",
											confirmButtonText : "Si, Continuar",
											cancelButtonText : "No, Salir!",
											closeOnConfirm : true,
											closeOnCancel : true,
											html:true
										},
										function(isConfirm) {

											if (isConfirm) {
												form.steps("next");

											}else{
													$(".modal .close").click();
												}
											}
										);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:false,
											  type : "warning",
											  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;
									}									
								}
								

							}, param);
							} else {
								swal({
									  title: "IMPORTANTE",
									  html:true,
									  type : "warning",
									  text: "Por favor seleccionar el grado al que se desea matricular.",
									  icon: "info",
									  button: "Cerrar",
									});
								return false;								
							}
}	

function grabarMatricula(){
							if($("[name='tc_acept']:checked").val()==1){
							//var validation = $('#frm-matricula').validate(); 
							//if ($('#frm-matricula').valid()){
							//if($("[name='id_bco_pag']:selected").length>0){
							/*$('.banco').each(function() {
								alert($(this).val());
								if ($(this).val() == '') {
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Seleccione el banco de mayor comodidad.",
								  icon: "info",
								  button: "Cerrar",
								});
								//return false;
								}
							 });*/

							if($('#frm-matricula #nro_cuo').val()!=''){
								swal({
								title : "Esta seguro?",
								text : "Esta Ud. de acuerdo con realizar la pre matrícula del alumno:" + $("#alumno").text(),
								type : "warning",
								showCancelButton : true,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "Sí, Pre Matricular",
								cancelButtonText : "No, cancela!!!",
								closeOnConfirm : true,
								closeOnCancel : true
							},
							function(isConfirm) {
								if (isConfirm) {
									//Validar si hay vacantes removeAttr('disabled');
									$('#id_gpf').removeAttr('disabled');
									if($('#id_grad').val()!=''){								
										var param = {
										id_cic : $('#id_cic').val(),
										id_grad : $('#id_grad').val()
										};
									_get("api/matricula/capacidadAcadVac", function(data) {

										console.log(data);
										if(data!=null){
											var nro_vac = data;
											//alert('nro_vac'+nro_vac);
											if(nro_vac>0){
												var validation = $('#frm-matricula').validate(); 
												if ($('#frm-matricula').valid()){
														/*_post($('#frm-matricula').attr('action') , $('#frm-matricula').serialize(),
														function(data){
																$('#frm-matricula #id').val(data.result);
																listarMatriculas($('#frm-matricula #id_alu').val());
															}
														);*/
														_POST({form:$('#frm-matricula'),
														  context:_URL,
														  msg_type:'notification',
														  msg_exito : 'Pre Matrícula exitosa.',
														  success:function(data){
																//$('#frm-matricula #id').val(data.result.id_mat);
																form.steps("next");
																swal({
																  title: "IMPORTANTE",
																  html:true,
																  type : "success",
																  text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
																  icon: "info",
																  button: "Cerrar",
																});
																listarMatriculas($('#frm-matricula #id_alu').val());
																$('#id_gpf').attr("disabled", "disabled");
																  
														 }, 
														 error:function(data){
															 _alert_error(data.msg);
															 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
															 $('#nro_dni').focus();*/
														}
													}); 
												}
											} else {
												swal({
													  title: "IMPORTANTE",
													  html:false,
													  type : "warning",
													  text: "No existen vacantes disponibles en este grado, por favor seleccionar otro. Gracias!",
													  icon: "info",
													  button: "Cerrar",
													});
												return false;
											}									
										}
										

									}, param);
									} else {
										swal({
											  title: "IMPORTANTE",
											  html:true,
											  type : "warning",
											  text: "Por favor seleccionar el grado al que se desea matricular.",
											  icon: "info",
											  button: "Cerrar",
											});
										return false;								
									}
								
								} else {
									swal({
										title : "Cancelado",
										text : "No se ha realizado la matricula",
										confirmButtonColor : "#2196F3",
										type : "error"
									});

									return false;
								}
								});
							}else {
									 swal({
									  title: "IMPORTANTE",
									  html:true,
									  text: "Porfavor seleccione el número de cuotas en el que desea pagar.",
									  icon: "info",
									  button: "Cerrar",
									});
									return false;
								}	
							//} 
							/*else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Seleccione el banco de mayor comodidad.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;	
							}*/
							//}
							} else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}	

}

function listarMatriculas(id_alu){
var param={id_alu:id_alu};
	_GET({ url: 'api/matricula/listarMatriculasCicloxAlumno/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			//   _cant_padres=data.length;
			console.log(data);
				$('#matriculas_alumno-tabla').dataTable({
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
							//{"title":"Alumno", "data" : "alumno"},
							{"title":"Giro de Negocio", "data" : "giro"},
							{"title":"Ciclo", "data" : "ciclo"}, 
							{"title":"Nivel", "data" : "nivel"}, 
							{"title":"Aula", "data" : "grado"}, 
							//{"title":"Sección", "data" : "secc"}, 
							{"title":"Sección", "render": function ( data, type, row ) {
								if(row.secc==null)
									return '<font color="red">Pre-Matrícula</font>';
								else
									return row.secc;
							}},	
							{"title":"Turno", "data" : "turno"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								//return '<button onclick="editarOtroFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
								//return '<button onclick="eliminarMatricula(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-danger btn-xs"><i class="icon-trash-alt position-left"></i> Eliminar</button>';
							if(row.id_au_asi!=null)
								return null;
							else
								return '<a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this,event)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
			}
		});
}

function matricula_eliminar(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	_delete('api/matricula/eliminarMatriculaAcadVac/' + $(link).data("id"),
			function(){
					listarMatriculas($('#frm-matricula #id_alu').val());
				}
			);
	
}	
	
	var form = $(".steps-validation").show();
	
	$(".steps-validation").steps(
				{	headerTag : "h6",
					bodyTag : "fieldset",
					transitionEffect : "fade",
					titleTemplate : '<span class="number">#index#</span> #title#',
					autoFocus : true,
					onStepChanging : function(event, currentIndex, newIndex) {

						if (currentIndex == 0) {
							//alert();
							//if($("[name='tc_acept1']:checked").length==0){
							if($('#id_alu').val()==""){
								swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Falta seleccionar al alumno(a).",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}	
							if($("[name='tc_acept']:checked").val()==0){
							swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							} 
						
											
						}

						if (currentIndex == 1 && newIndex == 2) {
							
						}
						if (currentIndex === 2 && newIndex === 3) {
							//console.log('DDD');
							//form.steps("previous");
								//alert ("ventana para matricular");	
								
						}

						// Forbid next action on "Warning" step if the user is
						// to young
						/*if (newIndex === 3 && Number($("#age-2").val()) < 18) {
							console.log('AAA');
							return false;
							
						}*/

						// Needed in some cases if the user went back (clean up)
						if (currentIndex < newIndex) {
							console.log('BBB');
							// To remove error styles
							form.find(".body:eq(" + newIndex + ") label.error")
									.remove();
							form.find(".body:eq(" + newIndex + ") .error")
									.removeClass("error");
						}

						form.validate().settings.ignore = ":disabled,:hidden";
						return form.valid();
					},

					onStepChanged : function(event, currentIndex, priorIndex) {

						/*//TAB DE PAGOS
						if (currentIndex === 1) {
						}

						/*if (currentIndex === 2 && priorIndex === 3) {
							console.log('DDD');
							form.steps("previous");
						}*/
					},

					onFinishing : function(event, currentIndex) {
						console.log('EEE');
						form.validate().settings.ignore = ":disabled";
						return form.valid();
					},

					onFinished : function(event, currentIndex) {
						alert("Submitted!");
					}
				});

$('a[href$="next"]').text('Siguiente >>');
$('a[href$="previous"]').text('<< Anterior ');