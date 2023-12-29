//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
var _id_gpf=null;
var _ape_pat=null;
var _ape_mat=null;
var _id_alu_pri=null;
var _id_apod=null;
var _id_apod_pag=null;
var _id_apod_aca=null;
var _id_bco_pag=null;
var _nro_contrato=null;
var _nro_mat=null;
var _nro_cont_env=null;
var _nro_dec_env=null;
var _nro_pro_env=null;
function onloadPage(params){
	_onloadPage();
	//var tipo=params.tipo;
	var id_usr=_usuario.id;
	 $("#frm-matricula #id_anio").val($('#_id_anio').text());
	 $("#frm-familia_mant #ubigeo").attr("max_length", 6);
	//Lo primero q se debe hacer es validar las deudas
	//validarDeudas(campo1);
	//llenarTabla(id_usr);
	//Aqui viene la logica
	$("#div_actualizacion_padres").load("pages/alumno/alu_familia_actualizacion_matricula.html", {}, function(responseText, textStatus, req){
		llenarDatosFamilia();
		
	});
	
	if(_id_gpf!=null){
		$('#frm-alumno_mant #btn_nuevo').click();
	}	
	
	$("#btn-grabar_matricula").hide();
	
	//onchange
	
	function llenarDatosFamilia(){
					   _get('api/familiar/obtenerDatosFamiliaxUusario/'+id_usr,function(data){
						   //habilitar_campos_familia();
						   $("#frm-familia_mant #btn_grabar").removeAttr('disabled');
						   //$("#tabs_familia").tabs("enable",1);
						   //$("#tabs_familia").tabs("enable",2);
							console.log(data);
						   $("#familia").text(data.nom);
						   $("#frm-familiares_mant #familia").text(data.nom);
						   $("#frm-familia_mant #id_gpf").val(data.id);
						  // alert(data.id_gpf);
						   $("#frm-matricula #id_grupo").val(data.id_gpf);
						   $("#frm-familia_mant #id").val(data.id);
						   $("#frm-familia_mant #id_usr").val(data.id_usr);
						   $("#frm-familia_mant #cod").val(data.cod);
						   $("#frm-familia_mant #nom").val(data.nom);
						   $("#frm-familia_mant #cod_aseg").val(data.cod_aseg);
						   $("#frm-familia_mant #login").val(data.login);
						   $("#frm-familia_mant #password").val(data.pass);
						   if(data.id_gen!=null){
								   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
								   $("#frm-alumno_mant #id_gen").change();
						   }
							
							if(data.est=='A'){
								$("#est").val('A');
								$('#est').change();
							} else if(data.est=='I'){
								$("#est").val('I');
								$('#est').change();
							}	
								
							$("#frm-familia_mant #direccion").val(data.direccion);
							$("#frm-familia_mant #referencia").val(data.referencia);
								$('#frm-familia_mant #id_pro').on('change',function(event){

								if ($(this).val()!=null && $(this).val()!=''){ 
								/*	$('#frm-familia_mant #id_dist').find('option').not(':first').remove();
								}
								else{*/
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#frm-familia_mant #id_dist'),
										id:data.id_dist,
										funcion:function(){
											console.log('remove data');
											$('#frm-familia_mant #id_dep').removeData('id_pro');//eliminar campos auxiliares
											$('#frm-familia_mant #id_pro').removeData('id_dist');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#frm-familia_mant #id_dep').on('change',function(event){
								
								 if($('#frm-familia_mant #id_dep').val()!=null && $('#frm-familia_mant #id_dep').val()!=''){ //$(this).val()!=null && $(this).val()!=''
									 _llenar_combo({
											url:'api/comboCache/provincias/' + $(this).val(),
											combo:$('#frm-familia_mant #id_pro'),
											id:data.id_pro,
											funcion:function(){
												$('#frm-familia_mant #id_pro').change();
											}
										}); 
								 }
							});
							_llenarCombo('cat_departamento',$('#frm-familia_mant #id_dep'),data.id_dep,null, function(){
								$('#frm-familia_mant #id_dep').change();
							});	
							
							$('#frm-familia_mant #id_csal').on('change',function(event){
								$("#dir_csalud").val($('#id_csal option:selected').attr('data-aux1'));
							});	
							_llenarCombo('cat_tipo_seguro',$('#frm-familia_mant #id_seg'), data.id_seg);
							_llenarCombo('cat_centro_salud',$('#frm-familia_mant #id_csal'), data.id_csal,null,function(){
								$('#frm-familia_mant #id_csal').change();
							});	
							
							//Listar a los familiares
							var param={id_gpf:data.id};
							_llenarComboURL('api/alumno/listarTodosFamiliares', $('#id_fam_emer'),
								data.id_fam_emer, param, function() {
									 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
									 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
									$('#id_fam_emer').change();
							});
							
							listarPadres();
							listarHijos();
							listarHijosMatriculados();
		
		$('#frm-familiar_mant #id_gpf').val(data.id);
		llenarDatosMatricula();
		});
		
		
	}	
	
	/*$('#frm-familia_mant #btn_grabar').on('click',function(event){
		var validation = $('#frm-familia_mant').validate(); 
		if ($('#frm-familia_mant').valid()){
			_post($('#frm-familia_mant').attr('action') , $('#frm-familia_mant').serialize(),function(data) {
				$('#frm-familia_mant #id').val(data.result.id_gpf);
				//$('#frm-familia_mant #login').val(data.result.usuario);
				//$('#frm-familia_mant #password').val(data.result.pass);
				$("#tabs_familia").tabs("enable",1);
				$("#tabs_familia").tabs("enable",2);
				//$('#frm-familiar_mant #btn_nuevo').click();
				$('#frm-familiar_mant #id_gpf').val(data.result.id_gpf);
			}	
			);
		}	
		
	});*/
	
	function llenarDatosMatricula(){
		
		
		
		//Mostrar a los alumnos a matricular
		listar_hermanos();
		$('#id_per_res').on('change',function(event){
			var parentesco = $('#id_per_res option:selected').attr('data-aux1');
			var celular = $('#id_per_res option:selected').attr('data-aux2');
			var tipo_doc = $('#id_per_res option:selected').attr('data-aux4');
			var nro_doc= $('#id_per_res option:selected').attr('data-aux3');
			var correo = $('#id_per_res option:selected').attr('data-aux5');
			var id_pare=$('#id_per_res option:selected').attr('data-aux7');
			$('#id_fam_par').val(parentesco);
			$('#id_fam_id_par').val(id_pare);
			$('#cel').val(celular);
			$('#corr').val(correo);
			$('#id_fam_dni').val(nro_doc);
			// numero de contrato correlativo
			/*if ($(this).val() == "") {
				$('#num_cont').val("");
			} else {
				var param3 = {
					id_fam : $('#id_fam').val(),
					id_anio : $("#_id_anio").text(),
					id_suc : $("#_id_suc").text()
				};
				_get(
						"api/matricula/MatriculaContratoCorrelativo/",
						function(data) {
							var contrato = data.contrato;
							_num_contrato=contrato;
							$('#num_cont')
									.val(contrato);
							$('#llb_num_cont')
									.addClass('active');
						}, param3);*/
			//}
		});
		
		$('#id_fam_res_pag').on('change',function(event){
			var parentesco = $('#id_fam_res_pag option:selected').attr('data-aux1');
			var nro_doc= $('#id_fam_res_pag option:selected').attr('data-aux3');
			$('#id_fam_res_pag_par').val(parentesco);
			$('#id_fam_res_pag_dni').val(nro_doc);
		});
		
		$('#id_fam_res_aca').on('change',function(event){
			var parentesco = $('#id_fam_res_aca option:selected').attr('data-aux1');
			var nro_doc= $('#id_fam_res_aca option:selected').attr('data-aux3');
			$('#id_fam_res_aca_par').val(parentesco);
			$('#id_fam_res_aca_dni').val(nro_doc);
		});

	// RESPONSABLE DE MATRÍCULA
	var param = {id_gpf:$('#frm-familia_mant #id').val()};
		_llenarComboURL('api/alumno/listarFamiliaresPMxGruFam', $('#id_per_res'),_id_apod, param, function() {
				$('#id_per_res').change();
				var param_fam={id_anio:$("#_id_anio").text(), id_usr:_usuario.id };
	_get('api/matricula/obtenerFamiliarSugeridoxUsuario', function(data) {
		console.log(data);
		if(data.length>0){
		_id_apod=data[0].id_per;
		 _id_bco_pag=data[0].id_bco_pag;
		 if(_id_apod!=null){
						//deshabilito el combo familiar
						$('#id_per_res').val(_id_apod).change();
						$('#id_bco').val(_id_bco_pag).change();
						$('#id_per_res').attr('disabled','disabled');
						$('#id_bco').attr('disabled','disabled');
				} else{
						$('#id_bco').removeAttr('disbled');
						$('#id_bco').removeAttr('disbled');
				}
		}	
		
		
	}, param_fam);
				
		});
	// RESPONSABLE DE PAGOS
	var param = {id_gpf:$('#frm-familia_mant #id').val()};
		_llenarComboURL('api/alumno/listarTodosFamiliaresxGruFam', $('#id_fam_res_pag'),_id_apod_pag, param, function() {
				$('#id_fam_res_pag').change();
				var param_fam_pag={id_anio:$("#_id_anio").text(), id_usr:_usuario.id };
			_get('api/matricula/obtenerResponsablePagoSugeridoxUsuario', function(data) {
				console.log(data);
				if(data.length>0){
				_id_apod_pag=data[0].id_fam;
				 //_id_bco_pag=data[0].id_bco_pag;
				 if(_id_apod_pag!=null){
								//deshabilito el combo familiar
								$('#id_fam_res_pag').val(_id_apod_pag).change();
								$('#id_fam_res_pag').attr('disabled','disabled');
								//$('#id_bco').val(_id_bco_pag).change();
								//$('#id_fam_res_pag').attr('disabled','disabled');
								//$('#id_bco').attr('disabled','disabled');
						} /*else{
								$('#id_bco').removeAttr('disbled');
								$('#id_bco').removeAttr('disbled');
						}*/
				}	
				
				
			}, param_fam_pag);
				
		});
		
		//RESPONSABLE ACADÉMICO
		var param = {id_gpf:$('#frm-familia_mant #id').val()};
		_llenarComboURL('api/alumno/listarTodosFamiliaresxGruFam', $('#id_fam_res_aca'),_id_apod_aca, param, function() {
				$('#id_fam_res_aca').change();
				var param_fam_aca={id_anio:$("#_id_anio").text(), id_usr:_usuario.id };
			_get('api/matricula/obtenerResponsableAcademicoSugeridoxUsuario', function(data) {
				console.log(data);
				if(data.length>0){
				_id_apod_aca=data[0].id_fam;
				 //_id_bco_pag=data[0].id_bco_pag;
				 if(_id_apod_aca!=null){
								//deshabilito el combo familiar
								$('#id_fam_res_aca').val(_id_apod_aca).change();
								$('#id_fam_res_aca').attr('disabled','disabled');
								//$('#id_bco').val(_id_bco_pag).change();
								//$('#id_fam_res_aca').attr('disabled','disabled');
								//$('#id_bco').attr('disabled','disabled');
						} /*else{
								$('#id_bco').removeAttr('disbled');
								$('#id_bco').removeAttr('disbled');
						}*/
				}	
				
				
			}, param_fam_aca);
				
		});
		
		_llenarCombo('fac_banco',$('#id_bco'),_id_bco_pag,null,function(){});
				
		
	}
	
	
	
	function listar_hermanos(){
	var param={id_gpf:$('#frm-familia_mant #id').val(), id_suc:_usuario.id_suc, id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/alumno/listarHermanosxGruFam',
	context: _URL,
   params:param,
   success:
	function(data){
	 //  _cant_padres=data.length;
	console.log(data);	
	if(data!=null){	
		if(data.length>0){
			//alert(1);
			validarDeudasGen(data[0].id_alu);
	   _cant_mat=data.length;
		$('#hermanos-tabla').dataTable({
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
					//{"title":"Nro. Documento", "data" : "nro_doc"}, 
					//{"title":"Apellidos y Nombres", "data" : "alumno"}, 
					{"title":"Apellidos y Nombres", "render": function ( data, type, row ) {
						   return row.ape_pat+" "+row.ape_mat+" "+row.nom;
					}} ,
					{"title":"Local", "render": function ( data, type, row ) {
						  // return row.sucursal; 
						  if(row.sucursal!=null){
							   return row.sucursal;
						  } else {
							 // if(row.tipo=='A'){
								// var id_suc_sug = (row.id_suc_sug!=null) ? row.id_suc_sug: '';
								  var id_gra_sig=row.gra_sig;
								  var id_niv_sig="";
								  if(id_gra_sig==1 || id_gra_sig==2 || id_gra_sig==3){
									  id_niv_sig=1;
								  } else if(id_gra_sig==4 || id_gra_sig==5 || id_gra_sig==6 || id_gra_sig==7 || id_gra_sig==8 || id_gra_sig==9 ){ 
									  id_niv_sig=2;
								  } else if(id_gra_sig==10 || id_gra_sig==11 || id_gra_sig==12 || id_gra_sig==13 || id_gra_sig==14){ 
									  id_niv_sig=3;
								  }
								  //$("#id_suc"+row.id_alu).val( row.id_suc_sug).change();
								  return "<select name='id_suc' id='id_suc"+row.id_alu+"' data-id_alu='"+row.id_alu+"' data-id_grad='"+id_gra_sig+"' data-id_niv='"+id_niv_sig+"' data-placeholder='Seleccione' class='form-control' onchange='consultarVacantesxGradoGen(this)'><option value=''>Seleccione</option></select>"; // onchange='consultarVacante(this)'
							 //console.log(row.id_suc_sug);
							  //alert(row.id_suc_sug);
							 //if(row.id_suc_sug!=null){
								
								
							 //}	 
							 // }	 else if(row.tipo=='N'){
							//	  return row.sucursal;
							 // }	  
						  }	  
						  
						  
					}} ,
					{"title":"Modalidad", "render": function ( data, type, row ) {
						  // return row.sucursal; 
						  if(row.modalidad!=null){
							  return row.modalidad;
						  } else {
							//  if(row.tipo=='A'){
							   var id_gra_sig=row.gra_sig;
							  var id_niv_sig="";
							  if(id_gra_sig==1 || id_gra_sig==2 || id_gra_sig==3){
								  id_niv_sig=1;
							  } else if(id_gra_sig==4 || id_gra_sig==5 || id_gra_sig==6 || id_gra_sig==7 || id_gra_sig==8 || id_gra_sig==9 ){ 
								  id_niv_sig=2;
							  } else if(id_gra_sig==10 || id_gra_sig==11 || id_gra_sig==12 || id_gra_sig==13 || id_gra_sig==14){ 
								  id_niv_sig=3;
							  }
							  return "<input type='hidden' id='id_cct"+row.id_alu+"' name='id_cct' value='' disabled /><select name='id_cme' id='id_cme"+row.id_alu+"' data-id_alu='"+row.id_alu+"' data-id_grad='"+id_gra_sig+"' data-id_niv='"+id_niv_sig+"'  data-placeholder='Seleccione' class='form-control select-search'  onchange='consultarVacante(this)'><option value=''>Seleccione</option></select>";
							 /* }	 else if(row.tipo=='N'){
								  return row.sucursal;
							  }	*/  
						  }	  
						  
						  
					}} ,
					{"title":"Nivel", "render": function ( data, type, row ) {
							 var id_gra_sig=row.gra_sig;
							  var niv_sig="";
							  if(id_gra_sig==1 || id_gra_sig==2 || id_gra_sig==3){
								  niv_sig='INICIAL';
							  } else if(id_gra_sig==4 || id_gra_sig==5 || id_gra_sig==6 || id_gra_sig==7 || id_gra_sig==8 || id_gra_sig==9 ){ 
								  niv_sig='PRIMARIA';
							  } else if(id_gra_sig==10 || id_gra_sig==11 || id_gra_sig==12 || id_gra_sig==13 || id_gra_sig==14){ 
								  niv_sig='SECUNDARIA';
							  }
						   return niv_sig; 
					}} ,
					{"title":"Grado", "render": function ( data, type, row ) {
							var id_gra_sig=row.gra_sig;
							var grado_sig="";
							  if(id_gra_sig==1){
								  grado_sig='3 AÑOS';
							  } else if(id_gra_sig==2){ 
								  grado_sig='4 AÑOS';
							  } else if(id_gra_sig==3){ 
								   grado_sig='5 AÑOS';
							  } else if(id_gra_sig==4){ 
								   grado_sig='PRIMERO';
							  } else if(id_gra_sig==5){ 
								   grado_sig='SEGUNDO';
							  } else if(id_gra_sig==6){ 
								   grado_sig='TERCERO';
							  } else if(id_gra_sig==7){ 
								   grado_sig='CUARTO';
							  } else if(id_gra_sig==8){ 
								   grado_sig='QUINTO';
							  } else if(id_gra_sig==9){ 
								   grado_sig='SEXTO';
							  } else if(id_gra_sig==10){ 
								   grado_sig='PRIMERO';
							  } else if(id_gra_sig==11){ 
								   grado_sig='SEGUNDO';
							  } else if(id_gra_sig==12){ 
								   grado_sig='TERCERO';
							  } else if(id_gra_sig==13){ 
								   grado_sig='CUARTO';
							  } else if(id_gra_sig==14){ 
								   grado_sig='QUINTO';
							  } 
						  
						   return grado_sig;
					}} ,
					{"title":"Estado", "render": function ( data, type, row ) {
						   if(row.id_mat!=null){
							   if(row.id_fac!=null){
								  return 'Matriculado';
							   } else {   
								 return 'Pre-Matriculado';
							   }
						   } else {
							   return 'No matriculado';
						   }
					}} ,
					/*{"title":"Documento", "render": function ( data, type, row ) {
						  /* if(row.id_mat!=null){
							   if(row.id_fac!=null){
								  return 'Matriculado';
							   } else {   
								 return 'Pre-Matriculado';
							   }
						   } else {*/
					/*		   return "<fieldset class='content-group text-center'><div class='col-sm-3 form-group'><div class='col-lg-10'><input type='file' class='file-input' "+
							   "data-show-caption='false' data-show-upload='false'><span class='help-block'>Suba el documento respectivo.</span></div></div></fieldset>";
						   //}
					}} ,*/
					{"title":"Seleccionar", "render": function ( data, type, row ) {
						if(row.id_mat!=null){
							return null;
							//_cant_alu_mat = _cant_alu_mat+1;
							//return '<button onclick="mostrarDatosMatricula(' + row.id_alu +',' + row.id_mat+','+row.id_gpf+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i> Editar Matricula</button>';
						}
						else{
							//return '<button onclick="mostrarDatosMatricula(' + row.id_alu +',' + row.id_mat+','+row.id_gpf+')" type="button" class="btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Datos Matricula</button>';
							var id_gra_sig=row.gra_sig;
							  var id_niv_sig="";
							  if(id_gra_sig==1 || id_gra_sig==2 || id_gra_sig==3){
								  id_niv_sig=1;
							  } else if(id_gra_sig==4 || id_gra_sig==5 || id_gra_sig==6 || id_gra_sig==7 || id_gra_sig==8 || id_gra_sig==9 ){ 
								  id_niv_sig=2;
							  } else if(id_gra_sig==10 || id_gra_sig==11 || id_gra_sig==12 || id_gra_sig==13 || id_gra_sig==14){ 
								  id_niv_sig=3;
							  }
							return "<input type='checkbox' id='id_alu_sel" + row.id_alu+"' name='id_alu_sel' value="+row.id_alu+" data-id_grad='"+id_gra_sig+"' data-id_niv='"+id_niv_sig+"' data-tipo='"+row.tipo+"' onclick='validarDeudas(this)'/>"; 
						}
						
					}}
			],
			"initComplete": function( settings ) {
				   _initCompleteDTSB(settings);
				  //for (var j in data) {
					  var j=0;
				   $("#frm-matricula select[name='id_suc']").each(function() {
					  
							console.log($(this));
							var id_niv=$(this).attr('data-id_niv');
							var param ={id_anio: $('#_id_anio').text() , id_niv:id_niv};
							console.log(data);
							//var i=0;
							//for (var i in data) {
								// alert();
							//for (var i = 0; i < data.length; i++) {
							var id_alu=data[j].id_alu;
							_llenarComboURL('api/aula/listarLocalesxNivelyAnio', $(this),
								data[j].id_suc_sug, param, function() {
									//$(this).change();
									//console.log(data[j]);
									//console.log(data[j].id_alu);
								$("#id_suc"+id_alu).change();	
							});
							
							j++;
							//}
								
							//_llenarCombo('ges_sucursal',$(this),null,null,function(){
							//$(this).change();
							//});
							
					});
				   //}
					/*$("#frm-matricula select[name='id_suc']").each(function() {
						console.log(data);
								for (var i in data) {
							//alert(data[i].id_alu);
							//alert(data[i].id_suc_sug);
							console.log($('#id_suc' + data[i].id_alu));
							//if($(this).attr('data-id_alu')==data[i].id_alu){
								//alert();
								$('#id_suc' + data[i].id_alu).val(data[i].id_suc_sug);
							//$('#id_suc' + data[i].id_alu).trigger('change');
							$('#id_suc' + data[i].id_alu).change();
							/*	$(this).val(data[i].id_suc_sug);
								$(this).trigger('change');
							}*/	
							//$('#id_suc' + data[i].id_alu).val(data[i].id_suc_sug);
							//$('#id_suc' + data[i].id_alu).change();
					//	}
					//});
					
					/*$('#id_suc').on('change',function(event){
						consultarModalidad($(this));
					});	*/
					/*$("#frm-matricula select[name='id_cme']").each(function() {
							console.log($(this));
							
							_llenarCombo('cat_modalidad_estudio',$(this),null,null,function(){
							//$(this).change();
							});
							
					});*/
					  
			 }
		});
//$("#frm-matricula select[name='id_suc']").each(function() {
								/*for (var i in data) {
							//alert(data[i].id_alu);
							//alert(data[i].id_suc_sug);
							$('#id_suc' + data[i].id_alu).val(data[i].id_suc_sug);
							$('#id_suc' + data[i].id_alu).change();
						}*/	
					//});
		}	
	}
	}
});
	
}
		


$('#frm-matricula #btn-consultar_vac').on('click',function(event){
	 consultar_vacantes();
});

}

function consultarVacantesxGradoGen(campo1){
	console.log(campo1);
	var campo =$(campo1);
	console.log(campo.val());
	var id_suc = campo.val();
	var id_alu = campo.attr('data-id_alu');
	var id_grad = campo.attr('data-id_grad');
	//var id_alu = campo.attr('data-id_alu');
	var id_niv = campo.attr('data-id_niv');
	
	var param = {
	id_grad : id_grad,
	id_niv : id_niv,
	id_anio : $('#_id_anio').text(),
	id_alu : id_alu,
	id_suc: id_suc
	};
	_GET({ url: 'api/matricula/capacidadxGrado',
		context: _URL,
		   params:param,
		   success:
			function(data){
				console.log(data);
				if(data!=null){
					if(data.capacidad==0){
						$("#id_cme"+id_alu).hide();
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existe el grado en este local, por favor seleccionar otro. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
					} else{
					var nro_vac = data.nro_vac;
					//alert('nro_vac'+nro_vac);
					if(nro_vac>0){
						$('#id_alu_sel'+id_alu).show();
						$("#id_cme"+id_alu).show();
						consultarModalidad(id_suc, id_grad, id_alu);
						/*swal(
						{
							title : "LOCAL Y MODALIDAD DISPONIBLE",
							text :'Existen <font id="_seconds">' + nro_vac+ '</font> vacantes en este local y modalidad, puede proceder a seleccionar al alumno.',
							type: "success",
							//showCancelButton : true,
							//confirmButtonColor : "rgb(33, 150, 243)",
							//cancelButtonColor : "#EF5350",
							//confirmButtonText : "Si, Continuar",
							//cancelButtonText : "No, Salir!",
							closeOnConfirm : true,
							//closeOnCancel : true,
							html:true
						},
						);*/
					} else {
						$("#id_cme"+id_alu).hide();
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existen vacantes disponibles en este local, por favor seleccionar otro. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
						//return false;
					}
					}										
				}
			}, error:function(data){
				_alert_error(data.msg);
				$('#id_alu_sel'+id_alu).hide();
			}
		});	
}	

function consultarModalidad(id_suc, id_grad, id_alu){
	//alert();
	//console.log(campo1);
	//var campo =$(campo1);
	//console.log(campo.val());
	//var id_suc = campo.val();
	//var id_alu = campo.attr('data-id_alu');
	//var id_grad = campo.attr('data-id_grad');
	//var id_alu = campo.attr('data-id_alu');
	//var id_niv = campo.attr('data-id_niv');
	var param = {
	id_grad : id_grad,
	id_suc : id_suc,
	//id_niv : id_niv,
	id_anio : $('#_id_anio').text(),
	//id_alu : id_alu
	};
	
	console.log(param);
//var param = {id_niv:id_niv, id_gir:id_gir};
	if(id_suc!=null && id_suc!=''){
			_get('api/aula/listarModalidadesxLocalyGrado',function(data){
				console.log(data.length);
				if(data.length>0){
					$("#id_cme"+id_alu).html('');
					$("#id_cme"+id_alu).show();
					$("#id_cme"+id_alu).append('<option value="">Seleccione</option>');
					for ( var i in data) {
						
						$("#id_cme"+id_alu).append('<option value="'+ data[i].id + '" data-id_cct="'+data[i].aux1+'">' + data[i].value + '</option>');
					}
				} else {
					$("#id_cme"+id_alu).html('');
					//alert('no hay');
					$("#id_cme"+id_alu).hide();
						swal({
									  title: "IMPORTANTE",
									  html:false,
									  type : "warning",
									  text: "No existe modalidades configuradas para dicho local, por favor seleccionar otro. Gracias!",
									  icon: "info",
									  button: "Cerrar",
									});
				}

			},param);
	}	
					//var param={id_anio: $('#_id_anio').text(),id_grad:$('#id_grad').val(),id_suc:$('#id_cic option:selected').attr("data-aux3")};
					//_llenarComboURL('api/aula/listarModalidadesxLocalyGrado',$('#id_cme')+id_alu,null,param);
			
	
}	

function _getComboJSON( data,sug_id, sug_au_id, id_mat ){

	
	var s = $('<select/>');
	if (sug_au_id==null)
		sug_au_id = '';
	
	$('<option />', {value: '-1', text: 'seleccione'}).appendTo(s);
	 
	 for ( var i in data) {
		 var id = data[i].id;
		 var value = data[i].secc;
			
	    //$('<option data-id_secc_sug="' + _id  + '" />', {value: id, text: value}).appendTo(s);
	    //$('<option/>', {value: id, text: value}).appendTo(s);
		 if (sug_au_id!='' && id==sug_au_id )
			 s.append('<option selected value="'+ id + '">' + value + '</option>');
		 else
			 s.append('<option value="'+ id + '">' + value + '</option>');
 
	}
	 
	  //if (_id!='') s.val(_id);
	 
 

	 return '<select class="form-control select-search" data-sug_id="' + sug_id+ '"  data-sug_au_id="' + sug_au_id + '" data-id_mat="' + id_mat + '" onchange="onchangeCombo(this)">' +  s.html() + '</select>';
	
	  
}


function validarDeudas(campo1){
	console.log(campo1);
	var id_alu =$(campo1);
	var id = id_alu.val();
	var tipo = id_alu.attr('data-tipo');
	//Para prueba nada mas se ha comentado , importante para el 2022
	_GET({  url:'api/matricula/validarDeudas/'+id+'/' + $('#_id_anio').text(),
					success:function(data){
						
						
					},
		error:function(data){
		_alert_error(data.msg);
		$('#id_alu_sel' + id ).attr( "checked", false );
		return false;
		}
	});
	if(tipo=='A'){
		if($('#id_suc' + id ).val()=='' && tipo=='A'){
				swal({
				  title: "IMPORTANTE",
				  html:false,
				  type : "warning",
				  text: "Seleccione el local al que desea matricular",
				  icon: "info",
				  button: "Cerrar",
				});
			$('#id_alu_sel' + id ).attr( "checked", false );
		} else {
				//Validar vacantes
				var id_grad = id_alu.attr('data-id_grad');
				var id_niv = id_alu.attr('data-id_niv');
				var param = {
				id_grad : id_grad,
				id_suc : $('#id_suc' + id ).val(),
				//id_cme : $('#id_cme' + id ).val(),
				id_niv : id_niv,
				id_anio : $('#_id_anio').text(),
				id_alu : id
				};
				
		_GET({ url: 'api/matricula/capacidadxGrado',
		context: _URL,
		   params:param,
		   success:
			function(data){
				console.log(data);
				if(data!=null){
					if(data.capacidad==0){
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existe el grado en este local, por favor seleccionar otro. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
					} else{
						var nro_vac = data.nro_vac;
					//alert('nro_vac'+nro_vac);
					if(nro_vac>0){
						$('#id_alu_sel'+id_alu).show();
						$("#id_cme"+id_alu).show();
						consultarModalidad(id_suc, id_grad, id_alu);
					} else {
						$("#id_cme"+id_alu).hide();
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existen vacantes disponibles en este local, por favor seleccionar otro. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
						//return false;
					}
					}										
				}
			}, error:function(data){
				_alert_error(data.msg);
				$('#id_alu_sel'+id_alu).hide();
			}
		});	
		}
	}

		validarCondiciones(id);
}	

function validarDeudasGen(id_alu){
	//Comentado solo para pruebas de matricula
	_GET({  url:'api/matricula/validarDeudas/'+id_alu+'/' + $('#_id_anio').text(),
					success:function(data){
						
						
					},
		error:function(data){
		_alert_error(data.msg);
		$('#id_alu_sel' + id_alu).attr( "checked", false );
		$("#frm-familia_mant #btn_grabar").attr('disabled','disabled');
		$('#frm-familiar_mant #btn_grabar').attr("disabled", "disabled");
		//var _btnSiguienteHabil = false;
		$( 'a[href$="next"]').addClass('disabled');
		return false;
		}
	});
}	

function validarCondiciones(id){
	//alert(id_alu);
	//Comentado solo para pruebas de matricula
	//var param1={id_alu:id};
	_GET({  url:'api/matricula/validarObservacionesMatricula/'+id,
					success:function(data){
						
						
					},
		error:function(data){
			if(data.code=="418"){
				swal({
				  title: "IMPORTANTE",
				  html:false,
				  type : "warning",
				  text: data.msg,
				  icon: "info",
				  button: "Cerrar",
				});
			} else {
				_alert_error(data.msg);
				$('#id_alu_sel' + id).attr( "checked", false );
				//$("#frm-familia_mant #btn_grabar").attr('disabled','disabled');
				//$('#frm-familiar_mant #btn_grabar').attr("disabled", "disabled");
				//var _btnSiguienteHabil = false;
				$( 'a[href$="next"]').addClass('disabled');
				return false;
			}	
		
		}
	});
}	

function cargarResumenPagos(){
	 /*$("#frm-matricula input[name='id_suc']:unselected").each(function() {
					$(this).prop('disabled',true);
	});*/
	
	 $("#frm-matricula select[name='id_suc']").each(function() {
					if($(this).val()==''){
						//alert();
						$(this).prop('disabled',true);
					} else {
						$(this).prop('disabled',false);
					}	
					
	});
	
	 $("#frm-matricula select[name='id_cme']").each(function() {
					if($(this).val()==''){
						//alert();
						$(this).prop('disabled',true);
					} else {
						$(this).prop('disabled',false);
					}	
					
	});
	_post_silent('api/matricula/ResumenPagoMatriculaWebColegio' , $('#frm-matricula').serialize(),
						function(data){
				console.log(data);
				if(data!=null){
					$("#btn-grabar_matricula").show();
					//alert();
					//$("#btn-grabar_matricula").prop("disabled", false);
					/*if(data.result.length>0){
						$("#btn-grabar_matricula").show();
					} else {
						$("#btn-grabar_matricula").hide();
					}*/	
					
				} /*else {
					//$("#btn-grabar_matricula").prop("disabled", true);
					$("#btn-grabar_matricula").hide();
				}*/	
				
				$('#resumen-pagos_mat').dataTable(
								{	data : data.result.listPagos,
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
											{"title": "Alumno", "data" : "descripcion"},
											{"title" : "Concepto","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return '<B>MATRÍCULA</B>';
												else if (row.tip == 'ING')
													return '<B>CUOTA DE INGRESO</B>';
												else if (row.tip == 'RES')
													return '<B>RESERVA</B>';
											else 
													return null;}},
											{"title" : "Monto","render" : function(data,type,row) {
												if (row.tip == 'MAT')
													return 'S/.' + $.number(row.monto,2);
												else if (row.tip == 'DESC')
													return '<font color="red">- S/.' + $.number(row.monto,2)+'</font>';
											    else if (row.tip == 'ING')
													return 'S/.' + $.number(row.monto,2);
												else if (row.tip == 'RES')
													return '<font color="red">             S/.' + $.number(row.monto,2)+'</font>';
												else
													return null; }}
									]
					});
					//alert(data.monto_total);
					$('#total_costo').val(data.result.monto_total);
							}
	);

}

function listarPadres(){
	var param={id_gpf:$("#frm-familia_mant #id").val(), id_anio:$('#_id_anio').text()};
	_get('api/alumno/listarTodosFamiliares',function(data){
										$('#tabla_familiares').dataTable({
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
												{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
												{"title":"N° Doc", "data": "nro_doc"},
												{"title":"Apellidos y Nombres", "data": "aux1"},
												{"title":"Parentesco", "data": "value"},
												{"title":"Acciones", "render": function ( data, type, row ) {
													if(row.id_anio_act!=null){
														if(row.id_anio_act==$('#_id_anio').text()){
															return '<button data-id="' + row.id + '" onclick="familiar_editar('+row.id+')"  type="button" tipo="E" class="btnPadre btn btn-success btn-xs"><i class="icon-pencil3"></i> Datos Actualizados</button>'
														} else {
															return '<button data-id="' + row.id + '" onclick="familiar_editar('+row.id+')"  type="button" tipo="N" class="btnPadre btn btn-warning btn-xs"><i class="icon-pencil3"></i>Actualizar Datos</button>';
														}															
													} else{
														return '<button data-id="' + row.id + '" onclick="familiar_editar('+row.id+')"  type="button" tipo="N" class="btnPadre btn btn-warning btn-xs"><i class="icon-pencil3"></i>Actualizar Datos</button>';
													}
											//return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="familiar_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
											//'<a href="#" data-id="' + row.id + '" onclick="familiar_eliminar(' + row.id + ')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							
										}}
										],
										"initComplete": function( settings ) {
										_initCompleteDTSB(settings);
										//Pasar al tab alumnos
										var cantidadPadres = $('.btnPadre').length;	
											 var _cant_pad_act=0;
											//alert('cantidadPadres'+cantidadPadres);
											$( ".btnPadre" ).each(function( index ) {
												 // alert($( this ).attr('tipo') );
												  if($( this ).attr('tipo')=='E'){
													  _cant_pad_act=_cant_pad_act+1;
												  }
											});
											console.log('cantidadPadres:'+cantidadPadres);
											console.log('_cant_pad_act: '+_cant_pad_act);
											if(cantidadPadres==_cant_pad_act){
												$("#li-tabs-3").click();
											}	
										$("#total").text(data.length);
										$("#tabla_familiares tbody tr").click(function(){
										$("#id_anio").val($('#_id_anio').text());
										   $(this).addClass('selected').siblings().removeClass('selected');    
										   var nro=$(this).find('td:nth-child(1)').html();

										}
										);

								 }
							});
							//_padre_madre=obtener_padre_madre(data);
		},param);	
		$("#div_actualizacion_padre").hide();
}

	function validarDatosLlenos(){
		
		var cantidadPadres = $('.btnPadre').length;	
		var _cant_pad_act=0;
		//alert('cantidadPadres'+cantidadPadres);
		$( ".btnPadre" ).each(function( index ) {
			 // alert($( this ).attr('tipo') );
			  if($( this ).attr('tipo')=='E'){
				  _cant_pad_act=_cant_pad_act+1;
			  }
		});
		//alert(_cant_pad_act);
		
		var cantidadAlumnos=$('.btnAlumno').length;
		//alert('cantidadAlumnos'+cantidadAlumnos);
		var _cant_alu_act=0;
		$( ".btnAlumno" ).each(function( index ) {
			  //alert($( this ).attr('tipo') );
			  if($( this ).attr('tipo')=='E'){
				  _cant_alu_act=_cant_alu_act+1;
			  }
		});
		//alert(_cant_alu_act);
		//Regla de Negocio para verificar si la actualizacion de datos es obligatorio
		//_get('api/reglasNegocio/validarActualizacionDatos',
			//function(data){
			//if(data.val=="1")	
				console.log(cantidadPadres);
			console.log(_cant_pad_act);
					if(cantidadPadres==_cant_pad_act && cantidadAlumnos==_cant_alu_act){
						form.find('a[href$="next"]').show();
						//return true;
					} else{
						form.find('a[href$="next"]').hide();
						//return false;
					}	
	}	

function listarHijos(){
	//Listar las matriculas del alumno
	var id_anio_act=$('#_id_anio').text();
	var id_anio_ant = parseInt(id_anio_act) - 1;
		var param={id_gpf:$("#frm-familia_mant #id").val(), id_anio_ant:id_anio_ant , id_anio_act:id_anio_act};
		 _get('api/alumno/listarAlumnosAntNuevosRecparaMatriculaAct',function(data){
			 $('#tabla_hijos').dataTable({
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
					{"title":"Código", "data" : "cod"}, 
					{"title":"Apellidos y Nombres", "data" : "alumno"}, 
					//{"title":"Nivel", "data" : "nivel"}, 
					//{"title":"Grado", "data" : "grado"}, 
					//{"title":"Sección", "data" : "aula"}, 
					//{"title":"Matricula", "data" : "matricula"}, 
					//{"title":"Situacion", "data" : "estado"},
					{"title":"Acciones", "render": function ( data, type, row ) {
						if(row.id_anio_act!=null){
							if(row.id_anio_act==$('#_id_anio').text()){
								//return '<button data-id="' + row.id + '" onclick="familiar_editar('+row.id+')"  type="button" tipo="E" class="btnPadre btn btn-success btn-xs"><i class="icon-pencil3"></i> Datos Actualizados</button>'
								return '<button data-id="' + row.id + '" onclick="alumno_editar(\''+row.cod+'\')" type="button" tipo="E" class="btnAlumno btn btn-success btn-xs"><i class="icon-pencil3"></i>Datos Actualizados</button>';
							} else {
								return '<button data-id="' + row.id + '" onclick="alumno_editar(\''+row.cod+'\')"  type="button" tipo="N" class="btnAlumno btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Datos</button>';
							}															
						} else{
							return '<button data-id="' + row.id + '" onclick="alumno_editar(\''+row.cod+'\')" type="button" tipo="N" class="btnAlumno btn btn-warning btn-xs"><i class="icon-pencil3"></i>Cargar Datos</button>';
						}
						//return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="alumno_editar('+row.cod+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'
					}}
			],
			"initComplete": function( settings ) {
				   _initCompleteDTSB(settings);
				   var cantidadAlumnos=$('.btnAlumno').length;
							//alert('cantidadAlumnos'+cantidadAlumnos);
					var _cant_alu_act=0;
					$( ".btnAlumno" ).each(function( index ) {
						  //alert($( this ).attr('tipo') );
						  if($( this ).attr('tipo')=='E'){
							  _cant_alu_act=_cant_alu_act+1;
						  }
					});
					if(cantidadAlumnos==_cant_alu_act){
						form.steps("next");
					}	
			 }
			});
			validarDatosLlenos();
		 },param);	
		 $("#div_actualizacion_alumno").hide();
}

function listarHijosMatriculados(){
	//Listar las matriculas del alumno
		var param={id_gpf:$("#frm-familia_mant #id").val(), id_anio:$('#_id_anio').text()};
		 _get('api/alumno/listarTodosHijosMatriculadosxFamilia',function(data){
			  if(data){
				  if(data.length>0){
					  _nro_mat=data.length;
					  
					  $("#div_documentos").css('display','block'); 
				  } else {
					  $("#div_documentos").css('display','none'); 
				  }	  
			  }		
			 $('#hijos_mat-tabla').dataTable({
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
					{"title":"Apellidos y Nombres", "data" : "alumno"}, 
					{"title":"Nivel", "data" : "nivel"}, 
					{"title":"Grado", "data" : "grado"}, 
					{"title":"Modalidad", "data" : "modalidad"}
					//{"title":"Sección", "data" : "aula"}, 
					//{"title":"Matricula", "data" : "matricula"}, 
					//{"title":"Situacion", "data" : "estado"},
					/*{"title":"Contrato", "render": function ( data, type, row ) {
						 return '<a href="#" data-id_fmo="' +  row.id_fmo + '" onclick="pdf(event,this)" title="Descargar Contrato"><i class="icon-file-pdf mr-3 icon-2x" style="color:red"></i></a>'; 
						//return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="alumno_editar('+row.cod+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'
					}},
					{"title":"Archivos", "render": function ( data, type, row ) {
					 return "<fieldset class='content-group text-center'><div class='col-lg-4'><input type='file' class='file-input' "+
							   "data-show-caption='false' data-show-upload='false'></div><div class='col-lg-4'>Suba el contrato respectivamente llenado.</div><div class='col-lg-4'><button type='button' id='btnExonerar' title='Subir Contrato' data-id_mat='"+row.id_mat+"' class='btn btn-primary btn-xs' onclick='subir_contrato(this,event)'><i class='fa fa-upload fa-2x'></i></button></div></fieldset>";
					}}*/
			],
			"initComplete": function( settings ) {
				   _initCompleteDTSB(settings);
				   var cantidadAlumnos=$('.btnAlumno').length;
							//alert('cantidadAlumnos'+cantidadAlumnos);
					var _cant_alu_act=0;
					$( ".btnAlumno" ).each(function( index ) {
						  //alert($( this ).attr('tipo') );
						  if($( this ).attr('tipo')=='E'){
							  _cant_alu_act=_cant_alu_act+1;
						  }
					});
					if(cantidadAlumnos==_cant_alu_act){
						form.steps("next");
					} else{
						//alert();
						swal({
						  title: "IMPORTANTE",
						  html:true,
						  text: "Es obligatorio la actualización de datos todos los hijos pertenecientes a la familia.",
						  icon: "info",
						  button: "Cerrar",
						});
						return false;
					}					
			 }
			});
			validarDatosLlenos();
		 },param);	
		 
		 //Si hay alumno matriculados, activo la parte de documentos
		  
		 
		
		 
		 //Verificar si hay contrato enviados
		  _get('api/matricula/matriculasContratoEnviado/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text(),function(data){
			  if(data){
				  if(data.length>0){
					  _nro_cont_env=data.length;
					  if(_nro_mat==_nro_cont_env){
						  $("#div_completado_F_1").css('display','block');
					  }	  
					  
				  }	  
			  }  
		  });  
		  
		   _get('api/matricula/matriculasDeclaracionEnviado/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text(),function(data){
			  if(data){
				  if(data.length>0){
					  _nro_dec_env=data.length;
					  if(_nro_mat==_nro_dec_env){
						 $("#div_completado_F_3").css('display','block'); 
					  }	  				  
				  }	  
			  }  
		  });  
		  
		   _get('api/matricula/matriculasProtocoloEnviado/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text(),function(data){
			  if(data){
				  if(data.length>0){
					  _nro_pro_env=data.length;
					  if(_nro_mat==_nro_pro_env){
						  $("#div_completado_F_2").css('display','block');
					  }	  
				  }	  
			  }  
		  });  
		 $("#div_actualizacion_alumno").hide();
		 
}
function subir_contrato(nombreform) {

	//e.preventDefault();
	//var id_fam = $(obj).data('id');
	//document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_anio="+$('#_id_anio').text();
//alert('#' + nombreform);
    var form = $('#' + nombreform)[0];
	 var file1 =  $('#file1')[0].files[0];
   
	
    var data = new FormData(form);
	 data.append('file', file1); 
	 
	 if(file1==null){
		 swal({
		  title: "ATENCIÓN",
		  html:false,
		  type : "warning",
		  text: "No ha seleccionado ningun documento. Por favor, seleccione el contrato correctamente llenado y firmado a enviar.",
		  icon: "info",
		  button: "Cerrar",
		});
		
	 }	 else {
		    // var url = _URL_OLI +  'api/resultados/xls/upload';1
			var url = _URL+ 'api/archivo/subirContrato/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text();

			$('#btn-procesar').prop("disabled", true);
			
			 $.blockUI({ 
					message: '<i class="icon-spinner4 spinner"></i>',
					//timeout: 2000, //unblock after 2 seconds
					overlayCSS: {
						backgroundColor: '#1b2024',
						opacity: 0.8,
						zIndex: 1200,
						cursor: 'wait'
					},
					css: {
						border: 0,
						color: '#fff',
						padding: 0,
						zIndex: 1201,
						backgroundColor: 'transparent'
					}
				});    

			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: url,
				data: data,
				processData: false, //prevent jQuery from automatically transforming the data into a query string
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {
					 $.unblockUI();
					 
					console.log(data);
					var error = data.code;
					
					if (error==null){
						
						$("#div_completado_"+nombreform).css('display','block');

						//lectora_listar_tabla(data.result);
							
					}else{
						console.log("Error al subir archivo");
						 $("#div_completado_"+nombreform).css('display','none');
						 swal({
								title : "Error al procesar el archivo",
								text : data.msg,
								confirmButtonColor : "#2196F3",
								type : "error"
							});
					}
						
					
					$('#btn-procesar').prop("disabled", false);

				},
			   
				error: function (e) {
					 $.unblockUI();
					$("#result").text(e.responseText);
					console.log("ERROR : ", e);
					$("#btnSubmit").prop("disabled", false);
					 $("#div_completado").css('display','none');
				}
			});
	 }	 

}

function subir_declaracion(nombreform) {

	//e.preventDefault();
	//var id_fam = $(obj).data('id');
	//document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_anio="+$('#_id_anio').text();
//alert('#' + nombreform);
    var form = $('#' + nombreform)[0];
	 var file1 =  $('#file3')[0].files[0];
   
	
    var data = new FormData(form);
	 data.append('file', file1); 
	 
	 if(file1==null){
		 swal({
		  title: "ATENCIÓN",
		  html:false,
		  type : "warning",
		  text: "No ha seleccionado ningun documento. Por favor, seleccione el contrato correctamente llenado y firmado a enviar.",
		  icon: "info",
		  button: "Cerrar",
		});
		
	 }	 else {
		    // var url = _URL_OLI +  'api/resultados/xls/upload';1
			var url = _URL+ 'api/archivo/subirDeclaracion/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text();

			$('#btn-procesar').prop("disabled", true);
			
			 $.blockUI({ 
					message: '<i class="icon-spinner4 spinner"></i>',
					//timeout: 2000, //unblock after 2 seconds
					overlayCSS: {
						backgroundColor: '#1b2024',
						opacity: 0.8,
						zIndex: 1200,
						cursor: 'wait'
					},
					css: {
						border: 0,
						color: '#fff',
						padding: 0,
						zIndex: 1201,
						backgroundColor: 'transparent'
					}
				});    

			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: url,
				data: data,
				processData: false, //prevent jQuery from automatically transforming the data into a query string
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {
					 $.unblockUI();
					 
					console.log(data);
					var error = data.code;
					
					if (error==null){
						
						$("#div_completado_"+nombreform).css('display','block');

						//lectora_listar_tabla(data.result);
							
					}else{
						console.log("Error al subir archivo");
						 $("#div_completado_"+nombreform).css('display','none');
						 swal({
								title : "Error al procesar el archivo",
								text : data.msg,
								confirmButtonColor : "#2196F3",
								type : "error"
							});
					}
						
					
					$('#btn-procesar').prop("disabled", false);

				},
			   
				error: function (e) {
					 $.unblockUI();
					$("#result").text(e.responseText);
					console.log("ERROR : ", e);
					$("#btnSubmit").prop("disabled", false);
					 $("#div_completado").css('display','none');
				}
			});
	 }	 

}


function subir_protocolo(nombreform) {

	//e.preventDefault();
	//var id_fam = $(obj).data('id');
	//document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_anio="+$('#_id_anio').text();
//alert('#' + nombreform);
    var form = $('#' + nombreform)[0];
	 var file1 =  $('#file2')[0].files[0];
   
	
    var data = new FormData(form);
	 data.append('file', file1); 
	 
	 if(file1==null){
		 swal({
		  title: "ATENCIÓN",
		  html:false,
		  type : "warning",
		  text: "No ha seleccionado ningun documento. Por favor, seleccione el contrato correctamente llenado y firmado a enviar.",
		  icon: "info",
		  button: "Cerrar",
		});
		
	 }	 else {
		    // var url = _URL_OLI +  'api/resultados/xls/upload';1
			var url = _URL+ 'api/archivo/subirProtocolo/'+$("#frm-familia_mant #id").val()+'/'+$('#_id_anio').text();

			$('#btn-procesar').prop("disabled", true);
			
			 $.blockUI({ 
					message: '<i class="icon-spinner4 spinner"></i>',
					//timeout: 2000, //unblock after 2 seconds
					overlayCSS: {
						backgroundColor: '#1b2024',
						opacity: 0.8,
						zIndex: 1200,
						cursor: 'wait'
					},
					css: {
						border: 0,
						color: '#fff',
						padding: 0,
						zIndex: 1201,
						backgroundColor: 'transparent'
					}
				});    

			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: url,
				data: data,
				processData: false, //prevent jQuery from automatically transforming the data into a query string
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {
					 $.unblockUI();
					 
					console.log(data);
					var error = data.code;
					
					if (error==null){
						
						$("#div_completado_"+nombreform).css('display','block');

						//lectora_listar_tabla(data.result);
							
					}else{
						console.log("Error al subir archivo");
						 $("#div_completado_"+nombreform).css('display','none');
						 swal({
								title : "Error al procesar el archivo",
								text : data.msg,
								confirmButtonColor : "#2196F3",
								type : "error"
							});
					}
						
					
					$('#btn-procesar').prop("disabled", false);

				},
			   
				error: function (e) {
					 $.unblockUI();
					$("#result").text(e.responseText);
					console.log("ERROR : ", e);
					$("#btnSubmit").prop("disabled", false);
					 $("#div_completado").css('display','none');
				}
			});
	 }	 

}

function pdf(){
	//alert();
	//e.preventDefault();
	//var id_fmo = $(o).data('id_fmo');
	//document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo;
	//document.location.href="http://login.ae.edu.pe:8080/documentos/ContratoColegioAE2021.pdf";
	document.location.href="http://login.ae.edu.pe:8080/documentos/Contrato Colegio AE.pdf";
	//window.open(url,'Download');
	//document.execCommand('SaveAs',true,url);
}	

function protocolo(){
	//alert();
	//e.preventDefault();
	//var id_fmo = $(o).data('id_fmo');
	//document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo;
	//document.location.href="http://login.ae.edu.pe:8080/documentos/ContratoColegioAE2021.pdf";
	document.location.href="http://login.ae.edu.pe:8080/documentos/Protocolo de Seguridad.pdf";
	//window.open(url,'Download');
	//document.execCommand('SaveAs',true,url);
}	

function declaracion(){
	//alert();
	//e.preventDefault();
	//var id_fmo = $(o).data('id_fmo');
	//document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo;
	//document.location.href="http://login.ae.edu.pe:8080/documentos/ContratoColegioAE2021.pdf";
	document.location.href="http://login.ae.edu.pe:8080/documentos/Declaración jurada de riesgo.pdf";
	//window.open(url,'Download');
	//document.execCommand('SaveAs',true,url);
}	

function grabarFamilia(obj){
	if($("#frm-familia_mant #id_dist").val()!=""){
		if($("#frm-familia_mant #direccion").val()!=""){
			if($("#frm-familia_mant #referencia").val()!=""){
				$('#frm-familia_mant #btn_grabar').attr("disabled", "disabled");
					var frm_familia=$(obj).parent().parent().parent();
					//var dni=frm_entrevista.find("#nro_dni").val();
					var json_familia={};
					  json_familia.id=frm_familia.find("#id").val();
					  json_familia.id_dep=frm_familia.find("#id_dep").val();
					  json_familia.id_pro=frm_familia.find("#id_pro").val();
					  json_familia.id_dist=frm_familia.find("#id_dist").val();
					  json_familia.direccion=frm_familia.find("#direccion").val();
					  json_familia.referencia=frm_familia.find("#referencia").val();
					  json_familia.id_seg=frm_familia.find("#id_seg").val();
					  json_familia.cod_aseg=frm_familia.find("#cod_aseg").val();
					  json_familia.id_csal=frm_familia.find("#id_csal").val();
					  json_familia.dir_csalud=frm_familia.find("#dir_csalud").val();
					   _POST({
							context:_URL,
							url:'api/gruFam/FamiliaGrabarReq',
							params:JSON.stringify(json_familia),
							contentType:"application/json",
							msg_type:'notification',
							msg_exito : 'se actualizó correctamente datos de la familia.',
							success: function(data){
								console.log(data);
								//$("#frm_datos_familiar").css('display','none');
								  $("#frm-familia_mant #id").val(data.id_gpf);
								//  $("#frm_datos_familiar #id_gpf").val(data.id_gpf);
								  $('#frm-familia_mant #btn_grabar').removeAttr('disabled');
								  $('#li-tabs-2').click();
								 // form.next();
								 // listarPadres();
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 $('#frm-familia_mant #btn_grabar').removeAttr('disabled');
								 //$('#alu_familiar_alumno-frm #btn-grabar').removeAttr('disabled');
								// $('#nro_dni').focus();
								//frm_entrevista.find("#id_ent_alu")
							}
						});	
			} else {
				alert('Escriba la referencia donde vive la familia');
				$('#frm-familia_mant #btn_grabar').removeAttr('disabled');
			}		
		} else{
			alert('Escriba la dirección donde vive la familia');
			$('#frm-familia_mant #btn_grabar').removeAttr('disabled');
		}	
		
	}	else{
		alert('Seleccione el distrito donde vive la familia');
		$('#frm-familia_mant #btn_grabar').removeAttr('disabled');
	}	
		
	}	

function familiar_editar(id_fam){
	$("#div_actualizacion_padre").show();
	$("#frm-familiar_mant #id_gen").attr('disabled','disabled');
	_es_padre_madre=false;
	var dni=null;
	limpiar_campos_familiar(); 
	_get('api/familiar/obtenerDatosFamiliarPersona/'+id_fam+'/'+dni,function(data){
	   $("#frm-familiar_mant #id").val(data.id);
	   $("#frm-familiar_mant #id_fam").val(id_fam);
	   $("#frm-familiar_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
	   $("#frm-familiar_mant #ape_pat").val(data.ape_pat);
	   $("#frm-familiar_mant #ape_mat").val(data.ape_mat);
	   $("#frm-familiar_mant #nom").val(data.nom);
	   $("#frm-familiar_mant #ubigeo").val(data.ubigeo);
	   $("#frm-familiar_mant #prof").val(data.prof);
	   $("#frm-familiar_mant #ocu").val(data.ocu);
		$("#frm-familiar_mant #corr").val(data.corr);
		$("#frm-familiar_mant #email_inst").val(data.email_inst);
		$("#frm-familiar_mant #cel").val(data.cel);
		$("#frm-familiar_mant #tlf").val(data.tlf);
		$("#frm-familiar_mant #face").val(data.face);
		$("#frm-familiar_mant #istrg").val(data.istrg);
		$("#frm-familiar_mant #twitter").val(data.twitter);
		$("#frm-familiar_mant #usuario").val(data.usuario);
		$("#frm-familiar_mant #pass_educando").val(data.pass_educando); 
	   //$('#frm-familiar_mant #nro_doc').attr("readonly", "readonly");
	   $('#frm-familiar_mant #id_par').attr("disabled", "disabled");
	    $('#frm-familiar_mant #id_tdc').attr("disabled", "disabled");
	   if(data.id_anio_act==$('#_id_anio').text()){
		   $("#frm-familiar_mant #cel").attr("readonly", "readonly");
		   $("#frm-familiar_mant #corr").attr("readonly", "readonly");
	   } else {
		   $("#frm-familiar_mant #cel").val('');
		   $("#frm-familiar_mant #corr").val('');
	   }   
	   if(data.id_gen!=null){
			   $("#frm-familiar_mant #id_gen").val(data.id_gen); 
			   $("#frm-familiar_mant #id_gen").change();
	   }
	   _llenarCombo('cat_parentesco',$('#frm-familiar_mant #id_par'), data.id_par,null,function(){
			$('#frm-familiar_mant #id_par').change();
		});
		_llenarCombo('cat_tipo_documento',$('#frm-familiar_mant #id_tdc'), data.id_tdc,null,function(){
			$('#frm-familiar_mant id_tdc').change();
		});	
		$("#frm-familiar_mant #nro_doc").val(data.nro_doc);
		_llenarCombo('cat_est_civil',$('#frm-familiar_mant #id_eci'), data.id_tdc,null,function(){
			$('#frm-familiar_mant #id_eci').change();
		});
		var fec_nac = data.fec_nac;
		if(fec_nac!=null){
			
			var arrFec_nac = fec_nac.split('-');
			$('#frm-familiar_mant #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
			//var dateControl = document.querySelector('input[type="date"]');
			//dateControl.value = arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0];
			var fecha_actual = new Date();
			var anio=fecha_actual.getFullYear();
			var edad=anio-arrFec_nac[0];
			$('#frm-familiar_mant #edad').text('Edad:'+edad);
		}
		
		$("#frm-familiar_mant #corr").on('blur',function(){
			validarEmail($("#frm-familiar_mant #corr").val());
		});	
		

		if(data.viv==null){
			
			$("#frm-familiar_mant #id_vivf1").click();
			$("#frm-familiar_mant #id_vivf1").val('1');
			$("#frm-familiar_mant #div_defuncion_padre").hide();
			$("#frm-familiar_mant #fec_def").attr("disabled", "disabled");
		} else{
			if(data.viv=='0'){
				//alert('2');
			$("#frm-familiar_mant #id_vivf2").click();
			$('#frm-familiar_mant #div_defuncion_padre').show();
			$('#frm-familiar_mant #fec_def').removeAttr("disabled");
			}
			if(data.viv=='1'){
				//
				//alert('3');
				//alert('3');
			$("#frm-familiar_mant #id_vivf1").click();
			$('#frm-familiar_mant #div_defuncion_padre').hide();
			$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		} 
		
		if(data.ocu==null){
				$("#id_tra_rem2").click();
		} else{
				$("#id_tra_rem1").click();
		} 
		$("input:radio[name='id_tra_rem']").on("click", function(e) {
			var valor = $(this).val();
			if(valor==0){
				$("#div_trabajo").css('display','none');
			} else 	if(valor==1){
				$("#div_trabajo").css('display','block');
			}
		});
		
		$("input:radio[name='viv']").on("click", function(e) {
			if($(this).val()==0){
				$('#frm-familiar_mant #div_defuncion_padre').show();
				$('#frm-familiar_mant #fec_def').removeAttr("disabled");
			}else {
				$('#frm-familiar_mant #div_defuncion_padre').hide();
				$('#frm-familiar_mant #fec_def').attr("disabled", "disabled");
			}
		});
		if(data.trab==null){
			$("#trab2").click();
			$("#trab2").attr("checked",true);
			$('#id_cond').val(2);
			$('#id_cond').change();
		} else{
			if(data.trab=='1'){
			$('#trab1').click();
			}
			if(data.viv=='0'){
			$('#div_trabaja').hide();
			$('#trab2').click();
			}
		} 
		$("input:radio[name='trab']").on("click", function(e) {
			if($(this).val()==1){
				$("#id_cond").val('1');
				$('#id_cond').change();
			}else {
				$("#id_cond").val('2');
				$('#id_cond').change();
			}
		});
		
		if(data.est=='A'){
			$("#est").val('A');
			$('#est').change();
		} else if(data.est=='I'){
			$("#est").val('I');
			$('#est').change();
		}	
		

});
}

function grabarFamiliar(obj){
	  $('#frm-familiar_mant #id_tdc').removeAttr('disabled');
	  $('#frm-familiar_mant #id_par').removeAttr('disabled');
	//  $('#frm_datos_familiar input[name=id_tap]').attr("disabled",false);
	  
	event.preventDefault();
	if($('#frm-familiar_mant #nro_doc').val().length<8 && $('#frm-familiar_mant #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#frm-familiar_mant #nro_doc").focus();
	}  else if($('#frm-familiar_mant #nro_doc').val().length<11 && $('#frm-familiar_mant #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#frm-familiar_mant #nro_doc").focus();
	} else if($('#frm-familiar_mant #ubigeo').val()==""){
		alert('El ubigeo es obligatorio');
		$("#frm-familiar_mant #ubigeo").focus();
	} else if($('#frm-familiar_mant #nro_doc').val().length<11 && $('#frm-familiar_mant #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#frm-familiar_mant #nro_doc").focus();
	} else if($('#frm-familiar_mant #ubigeo').val().length>6){
		alert('El número de ubigeo no puede tener más de 6 dígitos');
		$("#frm-familiar_mant #ubigeo").focus();
	} else if($('#frm-familiar_mant #corr').val()==""){
		alert('El correo es obligatorio');
		//$("#frm-familiar_mant #corr").focus();
	} else if($('#frm-familiar_mant #cel').val()==""){
		alert('El celular es obligatorio');
		//$("#frm-familiar_mant #corr").focus();
	}/*else if($("#alu_familiar-frm #id_pais").selectedIndex==-1){
		alert("Seleccione el país!!");			
	}*/ else{
		
		//if ($("#alu_familiar_alumno-frm").valid()){
		/* $("#frm_datos_familiar").validate({

			    onfocusout:true,
			    rules:{
			    	id_eci:"required",
			    	id_pais:"required"
			        
			    }
		});
		 if( $("#frm_datos_familiar").validate()){
			 alert(333333333);
		 }*/
			/*if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
				_post($('#alu_familiar_alumno-frm').attr('action') , $('#alu_familiar_alumno-frm').serialize());
			}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
				/*if(!$("#alu_familiar-frm #id_pais").selectedIndex!=-1){
					alert("Seleccione el país!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dep']").is(':checked')){
					alert("Seleccione el departamento!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_pro']").is(':checked')){
					alert("Seleccione la provincia!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dist']").is(':checked')){
					alert("Seleccione el distrito!!");			
				} else{*/
				/*if(!$("#frm_datos_familiar input[name='id_gen']").is(':checked')){
					alert("Seleccione el género, porfavor!!");			
				} else{*/
					$('#frm-familiar_mant #btn_grabar').attr("disabled", "disabled");
					$("#frm-familiar_mant #id_gen").removeAttr('disabled');
					var frm_familiar=$(obj).parent().parent().parent();
					//var dni=frm_entrevista.find("#nro_dni").val();
					var json_familiar={};
					  json_familiar.id_per=frm_familiar.find("#id").val();
					  json_familiar.id_fam=frm_familiar.find("#id_fam").val();
					  json_familiar.id_gpf=frm_familiar.find("#id_gpf").val();
					  json_familiar.est=frm_familiar.find("#est").val();
					  json_familiar.id_par=frm_familiar.find("#id_par").val();
					  json_familiar.id_tdc=frm_familiar.find("#id_tdc").val();
					  json_familiar.nro_doc=frm_familiar.find("#nro_doc").val();
					  json_familiar.ubigeo=frm_familiar.find("#ubigeo").val();
					  //json_familiar.id_tap=$("input:radio[name=id_tap]:checked").val();
					  json_familiar.ape_pat=frm_familiar.find("#ape_pat").val();
					  json_familiar.ape_mat=frm_familiar.find("#ape_mat").val();
					  json_familiar.nom=frm_familiar.find("#nom").val();
					  json_familiar.id_gen=$("#id_gen").val();
					  json_familiar.id_eci=frm_familiar.find("#id_eci").val();
					  json_familiar.id_anio=$('#_id_anio').text();
					  json_familiar.fec_nac=frm_familiar.find("#fec_nac").val();
					  json_familiar.viv=frm_familiar.find("#vivf").val();
					  if(frm_familiar.find("#vivf").val()!=1)
					  json_familiar.fec_def=frm_familiar.find("#fec_def").val();
					  //json_familiar.viv_alu=frm_familiar.find("#viv_alu").val();
					  //json_familiar.id_pais=frm_familiar.find("#id_pais").val();
					  json_familiar.id_tra_rem=frm_familiar.find("#id_tra_rem").val();
					  json_familiar.prof=frm_familiar.find("#prof").val();
					  json_familiar.ocu=frm_familiar.find("#ocu").val();
					  
					  /*json_familiar.id_dist=frm_familiar.find("#id_dist").val();
					  json_familiar.dir=frm_familiar.find("#dir").val();
					  json_familiar.ref=frm_familiar.find("#ref").val();*/
					  json_familiar.corr=frm_familiar.find("#corr").val();
					  json_familiar.tlf=frm_familiar.find("#tlf").val();
					  json_familiar.cel=frm_familiar.find("#cel").val();
					  json_familiar.face=frm_familiar.find("#face").val();
					  json_familiar.istrg=frm_familiar.find("#istrg").val();
					  json_familiar.twitter=frm_familiar.find("#twitter").val();
					 // json_familiar.id_gin=frm_familiar.find("#id_gin").val();
					  //json_familiar.flag_alu_ex=frm_familiar.find("#flag_alu_ex").val();
					  
					 // json_familiar.dir_tra=frm_familiar.find("#dir_tra").val();
					 // json_familiar.tlf_tra=frm_familiar.find("#tlf_tra").val();
					  //json_familiar.cel_tra=frm_familiar.find("#cel_tra").val();
					  //json_familiar.email_tra=frm_familiar.find("#email_tra").val();
					  console.log(json_familiar);
					  
					  _POST({
							context:_URL,
							url:'api/familiar/FamiliarGrabarReq',
							params:JSON.stringify(json_familiar),
							contentType:"application/json",
							msg_type:'notification',
							msg_exito : 'Se actualizó correctamente datos del familiar.',
							success: function(data){
								console.log(data);
								//$("#frm-familiar_mant").css('display','none');
								//  $("#frm_datos_familiar #id").val(data.id);
								//  $("#frm_datos_familiar #id_gpf").val(data.id_gpf);
								  $('#frm-familiar_mant #btn_grabar').removeAttr('disabled');
								  limpiar_campos_familiar();
								  $("#div_actualizacion_padre").hide();
								  listarPadres();
								  $('#frm_datos_familiar #id_tdc').attr("disabled", "disabled");
								  $('#frm_datos_familiar #id_par').attr("disabled", "disabled");
								  //$('#frm_datos_familiar input[name=id_tap]').attr("disabled",true);
								  //_id_par=data.id_par;
								  /*_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + data.id_gpf + "&id_alu=",
											function(data){
											$("#fam_alumnos-tab").load("pages/matricula/mat_actualizacion_dato_alu.html", {}, function(responseText, textStatus, req){
												mostrarDatosTabAlumno(data);
												$("#div_alumno").css('display','block');
												$('#alu_alumno-frm #id_gpf').val(data.id_gpf);
											});
											
									});*/
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 $('#frm-familiar_mant #btn_grabar').removeAttr('disabled');
								 $('#nro_dni').focus();
								//frm_entrevista.find("#id_ent_alu")
							}
						});	
						
				//}	
				//}
				
				
			//}
		//}
	}
} //);

function limpiar_campos_familiar(){
	$("#frm-familiar_mant #id").val('');
	$("#frm-familiar_mant #nro_doc").val('');
	$("#frm-familiar_mant #ubigeo").val('');
	$("#frm-familiar_mant #id_fam").val('');
	$("#frm-familiar_mant #alumno").text('');
	$("#frm-familiar_mant #ape_pat").val('');
	$("#frm-familiar_mant #ape_mat").val('');
	$("#frm-familiar_mant #nom").val('');
	$("#frm-familiar_mant #fec_nac").val('');
	$("#frm-familiar_mant #prof").val('');
	$("#frm-familiar_mant #ocu").val('');
	$('#frm-familiar_mant #id_par').removeAttr('disabled');
	$('#frm-familiar_mant #id_tdc').removeAttr('disabled');
	$('#frm-familiar_mant #nro_doc').removeAttr('readonly');
	$('#frm-familiar_mant #ape_pat').removeAttr('readonly');
	$('#frm-familiar_mant #ape_mat').removeAttr('readonly');
	$("#frm-familiar_mant #corr").removeAttr('readonly');
	$("#frm-familiar_mant #cel").removeAttr('readonly');
	$('#frm-familiar_mant #nom').removeAttr('readonly');
	$("#frm-familiar_mant #id_par").val('').trigger('change');
	$("#frm-familiar_mant #id_eci").val('').trigger('change');
	$("#frm-familiar_mant #id_tdc").val('').trigger('change');
	$("#frm-familiar_mant #id_gen").val('').trigger('change');
	//$("#frm-familiar_mant #fec_nac").val('02/02/1990');
	$("#frm-familiar_mant #corr").val('');
	$("#frm-familiar_mant #email_inst").val('');
	$("#frm-familiar_mant #cel").val('');
	$("#frm-familiar_mant #tlf").val('');
	$("#frm-familiar_mant #face").val('');
	$("#frm-familiar_mant #istrg").val('');
	$("#frm-familiar_mant #twitter").val('');
	$("#frm-familiar_mant #usuario").val('');
	$("#frm-familiar_mant #pass_educando").val(''); 
}	

function limpiar_campos_alumno(){
	$("#frm-alumno_mant #id_per").val('');
	$("#frm-alumno_mant #cod").val('');
	$("#frm-alumno_mant #id_anio").val('');
	$("#frm-alumno_mant #alumno").text('');
	$("#frm-alumno_mant #ape_pat").val('');
	$("#frm-alumno_mant #ape_mat").val('');
	$("#frm-alumno_mant #nom").val('');
	$("#frm-alumno_mant #fec_nac").val('');
	$("#frm-alumno_mant #id_eci").val('').trigger('change');
	$("#frm-alumno_mant #id_tdc").val('').trigger('change');
	$("#frm-alumno_mant #id_gen").val('').trigger('change');
	//$("#frm-familiar_mant #fec_nac").val('02/02/1990');
	$("#frm-alumno_mant #corr").val('');
	$("#frm-alumno_mant #ubigeo").val('');
	$("#frm-alumno_mant #email_inst").val('');
	$("#frm-alumno_mant #cel").val('');
	$("#frm-alumno_mant #tlf").val('');
	$("#frm-alumno_mant #id_pais_nac").val('').trigger('change');
	$("#frm-alumno_mant #id_dep_nac").val('').trigger('change');
	$("#frm-alumno_mant #id_pro_nac").val('').trigger('change');
	$("#frm-alumno_mant #id_dist_nac").val('').trigger('change');
	$("#frm-alumno_mant #id_rel").val('').trigger('change');
	$("#frm-alumno_mant #id_idio1").val('').trigger('change');
	$("#frm-alumno_mant #id_idio2").val('').trigger('change');
	$("#frm-alumno_mant #id_cond").val('').trigger('change');
}	

function alumno_editar(codigo){
					//var codigo=$(this).find('td:nth-child(1)').html();
					$("#div_actualizacion_alumno").show();
					$("#frm-alumno_mant #id_tdc").attr('disabled','disabled'); 
					   _get('api/alumno/obtenerDatosAlumnoxCod/'+codigo+'/'+$('#_id_anio').text(),function(data){
						   $("#frm-alumno_mant #alumno").text(data.ape_pat+' '+data.ape_mat+' '+data.nom);
						   $("#frm-alumno_mant #id_per").val(data.id);
						   $("#frm-alumno_mant #id_alu").val(data.id_alu);
						   $("#frm-alumno_mant #cod").val(data.cod);
						   $("#frm-alumno_mant #ape_pat").val(data.ape_pat);
						   $("#frm-alumno_mant #ape_mat").val(data.ape_mat);
						   $("#frm-alumno_mant #nom").val(data.nom);
						   if(data.id_gen!=null){
								   $("#frm-alumno_mant #id_gen").val(data.id_gen); 
								   $("#frm-alumno_mant #id_gen").change();
						   }
							_llenarCombo('cat_tipo_documento',$('#frm-alumno_mant #id_tdc'), data.id_tdc,null,function(){
								$('#id_tdc').change();
							});	
							$("#frm-alumno_mant #nro_doc").val(data.nro_doc);
							$("#frm-alumno_mant #ubigeo").val(data.ubigeo);
							_llenarCombo('cat_est_civil',$('#frm-alumno_mant #id_eci'), data.id_tdc,null,function(){
								$('#id_eci').change();
							});
							_llenarCombo('cat_condicion_per',$('#frm-alumno_mant #id_cond'), data.id_cond,null,function(){
								$('#id_cond').change();
							});
							var fec_nac = data.fec_nac;
							if(fec_nac){
								
								var arrFec_nac = fec_nac.split('-');
								$('#frm-alumno_mant #fec_nac').val(arrFec_nac[2] +'/' + arrFec_nac[1] + '/' + arrFec_nac[0]);	
								var fecha_actual = new Date();
								var anio=fecha_actual.getFullYear();
								var edad=anio-arrFec_nac[0];
								$('#edad').text('Edad:'+edad);
							}
		
							
							
							if(data.viv==null){
								$("#id_viv1").click();
								$("#id_viv1").val('1');
								$('#div_defuncion').hide();
								$('#fec_def').attr("disabled", "disabled");
							} else{
								if(data.viv=='0')
								$("#id_viv2").click();
								if(data.viv=='1')
								$("#id_viv1").click();
							} 
							
							$("input:radio[name='viv']").on("click", function(e) {
								if($(this).val()==0){
									$('#div_defuncion').show();
									$('#fec_def').removeAttr("disabled");
								}else {
									$('#div_defuncion').hide();
									$('#fec_def').attr("disabled", "disabled");
								}
							});
							if(data.trab==null){
								$("#trab2").click();
								$("#trab2").attr("checked",true);
								$('#id_cond').val(2);
								$('#id_cond').change();
							} else{
								if(data.trab=='1'){
								$('#trab1').click();
								}
								if(data.viv=='0'){
								$('#div_trabaja').hide();
								$('#trab2').click();
								}
							} 
							
							$("input:radio[name='trab']").on("click", function(e) {
								if($(this).val()==1){
									$("#id_cond").val('1');
									$('#id_cond').change();
								}else {
									$("#id_cond").val('2');
									$('#id_cond').change();
								}
							});
							
							if(data.est=='A'){
								$("#est").val('A');
								$('#est').change();
							} else if(data.est=='I'){
								$("#est").val('I');
								$('#est').change();
							}	
							
							if(data.id_fam_emer!=null){
								$('#div_familiar_emergencia').show();
							} else {
								$('#div_familiar_emergencia').hide();	
							}
							$('#id_fam_emer').on('change',function(event){
								$('#familiar_emer').val()
							});	
							
							$('#id_fam_emer').on('change',function(event){
								 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
								 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
							});
							
							var param={id_gpf:data.id_gpf};
							_llenarComboURL('api/alumno/listarTodosFamiliares', $('#id_fam_emer'),
								data.id_fam_emer, param, function() {
									 var familiar = $('#id_fam_emer option:selected').attr('data-aux1');
									 var celular = $('#id_fam_emer option:selected').attr('data-aux2');
									 $('#familiar_emer').val(familiar);
									 $('#celular_emer').val(celular);
									$('#id_fam_emer').change();
							});
								
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
							$("#frm-alumno_mant #corr").val(data.corr);
							$("#frm-alumno_mant #email_inst").val(data.email_inst);
							$("#frm-alumno_mant #cel").val(data.cel);
							$("#frm-alumno_mant #tlf").val(data.tlf);
							$("#frm-alumno_mant #usuario").val(data.usuario);
							$("#frm-alumno_mant #pass_educando").val(data.pass_educando);
								$('#id_pro_nac').on('change',function(event){

								if ($(this).val()==null || $(this).val()==''){ 
									$('#id_dist_nac').find('option').not(':first').remove();
								}
								else{
									_llenar_combo({
										url:'api/comboCache/distritos/' + $(this).val(),
										combo:$('#id_dist_nac'),
										id:data.id_dist_nac,
										funcion:function(){
											console.log('remove data');
											$('#id_dep_nac').removeData('id_pro_nac');//eliminar campos auxiliares
											$('#id_pro_nac').removeData('id_dist_nac');//eliminar campos auxiliares
										}
									});		 		
								}
							 
							});
							
							$('#id_dep_nac').on('change',function(event){
								
								 if($('#id_dep_nac').val()!=null){
									 _llenar_combo({
											url:'api/comboCache/provincias/' + $(this).val(),
											combo:$('#id_pro_nac'),
											id:data.id_pro_nac,
											funcion:function(){
												$('#id_pro_nac').change();
											}
										}); 
								 }
							});
							
							$('#id_pais_nac').on('change',function(event){
								 if($('#id_pais_nac').val()=="193"){
									 $('#id_dep_nac').removeAttr("disabled");
									 $('#id_pro_nac').removeAttr("disabled");
									 $('#id_dist_nac').removeAttr("disabled");
									// $('#dir').removeAttr("readonly");
									// $('#referencia').removeAttr("readonly");
									 _llenar_combo({
											url:'api/comboCache/departamentos/' + $(this).val(),
											combo:$('#id_dep_nac'),
											id: data.id_dep_nac,
											funcion:function(){
												$('#id_dep_nac').change();
											}
										});

									_llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'), data.id_nac);
								 } else {
									 $('#id_dep_nac').val('');
									 $('#id_pro_nac').val('');
									 $('#id_dist_nac').val('');
									// $('#dir').val('');
									// $('#referencia').val('');
									 $('#id_dep_nac').attr("disabled", "disabled");
									 $('#id_pro_nac').attr("disabled", "disabled");
									 $('#id_dist_nac').attr("disabled", "disabled");
									// $('#dir').attr("readonly", "readonly");
									// $('#referencia').attr("readonly", "readonly");
									 $('#id_dep_nac').change();
									 _llenarCombo('cat_nacionalidad',$('#alu_alumno-frm #id_nac'));
								 }
							});
								console.log(data.id_pais_nac);
							_llenarCombo('cat_pais',$('#id_pais_nac'),data.id_pais_nac,null, function(){
								$('#id_pais_nac').change();
							});	
							_llenarCombo('cat_religion',$('#id_rel'), data.id_reli);
							_llenarCombo('cat_idioma',$('#id_idio1'), data.id_idio1);
							_llenarCombo('cat_idioma',$('#id_idio2'), data.id_idio2);
							
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

function grabarAlumno(obj){
	  $('#frm-alumno_mant #id_tdc').removeAttr('disabled');	  
	event.preventDefault();
	/*if($('#frm-familiar_mant #nro_doc').val().length<8 && $('#frm-familiar_mant #id_tdc').val()==1 ){
		alert('El DNI debe de tener 8 dígitos!!');
		$("#frm-familiar_mant #nro_doc").focus();
	}  else if($('#frm-familiar_mant #nro_doc').val().length<11 && $('#frm-familiar_mant #id_tdc').val()==3){
		alert('El carnet de extranjeria debe de tener 11 dígitos!!');
		$("#frm-familiar_mant #nro_doc").focus();
	} /*else if($("#alu_familiar-frm #id_pais").selectedIndex==-1){
		alert("Seleccione el país!!");			
	}*/ //else{
		
		//if ($("#alu_familiar_alumno-frm").valid()){
		/* $("#frm_datos_familiar").validate({

			    onfocusout:true,
			    rules:{
			    	id_eci:"required",
			    	id_pais:"required"
			        
			    }
		});
		 if( $("#frm_datos_familiar").validate()){
			 alert(333333333);
		 }*/
			/*if($("#ini").val()=="3"){//SI YA TIENE TODO VALIDADO
				_post($('#alu_familiar_alumno-frm').attr('action') , $('#alu_familiar_alumno-frm').serialize());
			}else{ //SE ENVIARA CORREO POR Q TIENE VALIDACION EN INI=1
				/*if(!$("#alu_familiar-frm #id_pais").selectedIndex!=-1){
					alert("Seleccione el país!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dep']").is(':checked')){
					alert("Seleccione el departamento!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_pro']").is(':checked')){
					alert("Seleccione la provincia!!");			
				} else if(!$("#alu_familiar-frm  input[id='id_dist']").is(':checked')){
					alert("Seleccione el distrito!!");			
				} else{*/
				/*if(!$("#frm_datos_familiar input[name='id_gen']").is(':checked')){
					alert("Seleccione el género, porfavor!!");			
				} else{*/
					$('#frm-alumno_mant #btn_grabar').attr("disabled", "disabled");
					$("#frm-alumno_mant #id_tdc").removeAttr('disabled');
					var frm_alumno=$(obj).parent().parent().parent();
					if($('#frm-alumno_mant #ubigeo').val()==""){
						alert('El ubigeo es obligatorio');
						$("#frm-alumno_mant #ubigeo").focus();
						$('#frm-alumno_mant #btn_grabar').removeAttr('disabled');
					} else if($('#frm-alumno_mant #ubigeo').val().lengt>6 && $('#frm-alumno_mant #ubigeo').val().lengt<6){
						alert('El ubigeo es obligatorio');
						$("#frm-alumno_mant #ubigeo").focus();
						$('#frm-alumno_mant #btn_grabar').removeAttr('disabled');
					} else {
						var json_alumno={};
					  json_alumno.id_per=frm_alumno.find("#id_per").val();
					  json_alumno.id_alu=frm_alumno.find("#id_alu").val();
					  json_alumno.cod=frm_alumno.find("#cod").val();
					  json_alumno.id_gen=frm_alumno.find("#id_gen").val();
					  json_alumno.id_tdc=frm_alumno.find("#id_tdc").val();
					  json_alumno.nro_doc=frm_alumno.find("#nro_doc").val();
					  json_alumno.ubigeo=frm_alumno.find("#ubigeo").val();
					  json_alumno.viv=$("input:radio[name=viv]:checked").val();
					  json_alumno.trab=$("input:radio[name=trab]:checked").val();
					  if(frm_alumno.find($("input:radio[name=viv]:checked").val())!=1)
					  json_alumno.fec_def=frm_alumno.find("#fec_def").val();
				      json_alumno.fec_nac=frm_alumno.find("#fec_nac").val();
					  json_alumno.corr=frm_alumno.find("#corr").val();
					  json_alumno.tlf=frm_alumno.find("#tlf").val();
					  json_alumno.cel=frm_alumno.find("#cel").val();
					  json_alumno.id_eci=frm_alumno.find("#id_eci").val();
					  json_alumno.id_anio=$('#_id_anio').text();
					  json_alumno.id_pais_nac=frm_alumno.find("#id_pais_nac").val();
					  json_alumno.id_dep_nac=frm_alumno.find("#id_dep_nac").val();
					  json_alumno.id_pro_nac=frm_alumno.find("#id_pro_nac").val();
					  json_alumno.id_dist_nac=frm_alumno.find("#id_dist_nac").val();
					  json_alumno.id_rel=frm_alumno.find("#id_rel").val();
					  json_alumno.id_idio1=frm_alumno.find("#id_idio1").val();
					  json_alumno.id_idio2=frm_alumno.find("#id_idio2").val();
					  json_alumno.id_cond=frm_alumno.find("#id_cond").val();
					  console.log(json_alumno);
					  
					  _POST({
							context:_URL,
							url:'api/alumno/alumnoActualiarDatosV2',
							params:JSON.stringify(json_alumno),
							contentType:"application/json",
							msg_type:'notification',
							msg_exito : 'Se actualizó correctamente datos del alumno.',
							success: function(data){
								console.log(data);
								//$("#frm-familiar_mant").css('display','none');
								//  $("#frm_datos_familiar #id").val(data.id);
								//  $("#frm_datos_familiar #id_gpf").val(data.id_gpf);
								  $('#frm-alumno_mant #btn_grabar').removeAttr('disabled');
								  limpiar_campos_alumno();
								  $("#div_actualizacion_alumno").hide();
								  listarHijos();
								 validarDatosLlenos();
								  $('#frm-alumno_mant #id_tdc').attr("disabled", "disabled");
								  //$('#frm_datos_familiar input[name=id_tap]').attr("disabled",true);
								  //_id_par=data.id_par;
								  /*_get('api/alumno/grupoFamiliar/alumnos?id_gpf=' + data.id_gpf + "&id_alu=",
											function(data){
											$("#fam_alumnos-tab").load("pages/matricula/mat_actualizacion_dato_alu.html", {}, function(responseText, textStatus, req){
												mostrarDatosTabAlumno(data);
												$("#div_alumno").css('display','block');
												$('#alu_alumno-frm #id_gpf').val(data.id_gpf);
											});
											
									});*/
								  
							 }, 
							 error:function(data){
								 console.log(data);
								 _alert_error(data.msg);
								 $('#frm-alumno_mant #btn_grabar').removeAttr('disabled');
								 $('#nro_dni').focus();
								//frm_entrevista.find("#id_ent_alu")
							}
						});	
					}	
					//var dni=frm_entrevista.find("#nro_dni").val();
					
						
				//}	
				//}
				
				
			//}
		//}
	//}
} //);

$('#frm-alumno_mant #btn_grabar').on('click',function(event){
	alert();
		/*var validation = $('#frm-alumno_mant').validate(); 
		if ($('#frm-alumno_mant').valid()){
			_post($('#frm-alumno_mant').attr('action') , $('#frm-alumno_mant').serialize(),function(data) {
			console.log(data);
			$('#frm-alumno_mant #cod').val(data.result.cod);
			$("#frm-alumno_mant #alumno").text(data.result.ape_pat+' '+data.result.ape_mat+' '+data.result.nom);
			if($("[name='tip_busqueda']:checked").length>0){
			valor=$('input[name="tip_busqueda"]:checked').val();
			llenarTabla(valor);
			}
			});
		}	*/
});	

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

function consultarVacante(campo1){
	//Esto es por modalidad
	console.log(campo1);
	var campo =$(campo1);
	var id_alu = campo.attr('data-id_alu');
	var div_local=$(campo1).parent().parent();
	console.log(div_local.html());
	var id_sucursal= div_local.find("#id_suc"+id_alu).val();
	console.log(id_sucursal);
	var id = campo.val();
	
	var id_grad = campo.attr('data-id_grad');
	//var id_alu = campo.attr('data-id_alu');
	var id_niv = campo.attr('data-id_niv');
	var id_cct=$('#id_cme'+id_alu+' option:selected').attr('data-id_cct');
	//var id_cct = campo.attr('data-id_cct');
	console.log(id_cct);
	if(id_cct!=''){
		$('#id_cct'+id_alu).val(id_cct);
		$('#id_cct'+id_alu).removeAttr('disabled');
	}	
	
	var param2 = {
	id_grad : id_grad,
	id_cme : id,
	id_niv : id_niv,
	id_anio : $('#_id_anio').text(),
	id_alu : id_alu,
	id_suc: id_sucursal
	};
	console.log(param2);
	_GET({ url: 'api/matricula/capacidadxGradoModalidad',
		context: _URL,
		   params:param2,
		   success:
			function(data){
				console.log(data);
				if(data!=null){
					if(data.capacidad==0){
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existe el grado en este local, por favor seleccionar otro. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
					} else{
											var nro_vac = data.nro_vac;
					//alert('nro_vac'+nro_vac);
					if(nro_vac>0){
						$('#id_alu_sel'+id_alu).show();
						$('#id_alu_sel'+id_alu).prop('checked', true);
						validarCondiciones(id_alu);
						/*swal(
						{
							title : "LOCAL Y MODALIDAD DISPONIBLE",
							//text :'Importante, proceda a seleccionar al alumno.',
							type: "success",
							//showCancelButton : true,
							//confirmButtonColor : "rgb(33, 150, 243)",
							//cancelButtonColor : "#EF5350",
							//confirmButtonText : "Si, Continuar",
							//cancelButtonText : "No, Salir!",
							closeOnConfirm : true,
							//closeOnCancel : true,
							html:true
						},
						/*function(isConfirm) {

							if (isConfirm) {
								form.steps("next");

							}else{
									$(".modal .close").click();
								}
							}*/
						//);
					} else {
						swal({
							  title: "IMPORTANTE",
							  html:false,
							  type : "warning",
							  text: "No existen vacantes en este local o modalidad, por favor seleccione otra. Gracias!",
							  icon: "info",
							  button: "Cerrar",
							});
							$('#id_alu_sel'+id_alu).hide();
							$('#id_alu_sel'+id_alu).prop('checked', false);
						//return false;
					}
					}										
				}
			}, error:function(data){
				_alert_error(data.msg);
				$('#id_alu_sel'+id_alu).hide();
			}
		});	
}

function grabarMatricula(){
							//if($("[name='tc_acept']:checked").val()==1){
							if($("#id_per_res").val()==""){
								alert('Es necesario seleccionar al responsable de matrícula.');
							} else if($("#id_fam_res_pag").val()==""){
								alert('Es necesario seleccionar al responsable económico.');
							} else if($("#id_fam_res_aca").val()==""){
								alert('Es necesario seleccionar responsable académico.');
							} else if($("#id_bco").val()==""){
								alert('Es necesario seleccionar el banco para pago de pensiones');
							} else{
								
								if ( $("[name='id_alu_sel']:checked").length >0){
																//if($('#frm-matricula #nro_cuo').val()!=''){
								swal({
								title : "Esta seguro?",
								text : "Esta Ud. de acuerdo con realizar la pre matrícula del alumno(s)" + $("#alumno").text(),
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
									$('#btn-grabar_matricula').attr('disabled','disabled');
									$('#id_gpf').removeAttr('disabled');
									$('#id_per_res').removeAttr('disabled');
									$('#id_fam_res_pag').removeAttr('disabled');
									$('#id_fam_res_aca').removeAttr('disabled');
									$('#id_bco').removeAttr('disabled');
														_POST({form:$('#frm-matricula'),
														  context:_URL,
														  msg_type:'notification',
														  msg_exito : 'Usted ha realizado satisfactoriamente la PREMATRÍCULA',
														  success:function(data){
																//$('#frm-matricula #id').val(data.result.id_mat);
																//$('#btn-grabar_matricula').removeAttr('disabled');
																$('#id_per_res').attr('disabled','disabled');
																$('#id_bco').attr('disabled','disabled');
																$('#id_fam_res_pag').attr('disabled','disabled');
																$('#id_fam_res_aca').attr('disabled','disabled');
																form.steps("next");
																_nro_contrato=data.contrato;
																listarHijosMatriculados();
																
																/*swal({
																  title: "IMPORTANTE",
																  html:true,
																  type : "success",
																  //text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
																   text: "<p>A la brevedad verificaremos los documentos enviados y mediante email le comunicaremos la conformidad de ellos, para que pueda realizar los pagos y finalizar con el proceso de matrícula </p>",
																  icon: "info",
																 // confirmButtonColor : "rgb(33, 150, 243)",
																  //cancelButtonColor : "#EF5350",
																  //confirmButtonText : "OK",
																 button: "Cerrar",
																});*/
																//Enviar Correo
																//_post_silent({url:'/api/familiar/enviarMensajexMatriculaColegio/' + data.result.contrato,context:_URL});
																//if (isConfirm) {
																	//auritacomentado_send('pages/apoderado/apo_menu_matricula_web.html','Matrícula Web','Lista',{tipo:'C'});
																//}
																//Por ahora comentado2022
																/*_post_silent('api/familiar/enviarMensajexMatriculaColegio/'+ data.result.contrato ,null,
																function(data){
																	
																});	*/
																	
																//_send('pages/apoderado/apo_menu_matricula_web.html','Matrícula Web','Matrícula Web');
																//listarMatriculas($('#frm-matricula #id_alu').val());
																//generar contrato
																/*$('#lblObservaciones').html('LISTA DE ALUMNOS MATRICULADOS (CONTRATO: '+ data.result.contrato+ ' )');
																_get("api/matricula/matriculadosxContrato?num_cont="+ $('#num_cont').val(),function(data1) {
																	$('#matriculados-tabla').dataTable(
																									{	data : data1,
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
																									{"title" : "alumno","data" : "alumno"},
																									{"title" : "nivel","data" : "nivel"},
																									{"title" : "grado","data" : "grado"},
																									{"title" : "seccion","data" : "seccion"}
																							]
																});*/
																//console.log('pinta matriculadls');
																//});		
																//form.steps("next");
																//document.location.href = _URL + "api/archivo/generarContratoMatriculaWeb?id_per="+$("#id_per_res").val()+"&id_anio="+$('#_id_anio').text();	  
														 }, 
														 error:function(data){
															 _alert_error(data.msg);
															 $('#btn-grabar_matricula').attr('disabled','disabled');
															 $('#id_fam_res_pag').attr('disabled','disabled');
															$('#id_fam_res_aca').attr('disabled','disabled');
															$('#id_per_res').attr('disabled','disabled');
																$('#id_bco').attr('disabled','disabled');
															 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
															 $('#nro_dni').focus();*/
														}
													}); 
								
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
								}	else {
									//Mensaje
										swal({
										  title: "IMPORTANTE",
										  html:true,
										  text: "Por favor seleccione al alumno(s) que desea matricular.",
										  icon: "info",
										  button: "Cerrar",
										});
											return false;
								}
							}
							/*} else{
								  swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario aceptar los términos y condiciones.",
								  icon: "info",
								  button: "Cerrar",
								});
							return false;
							}	*/

}

function generarContrato(obj, e){
	e.preventDefault();
	var id_fam = $(obj).data('id');
	document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_anio="+$('#_id_anio').text();
}	

function reimprimir(){
	document.location.href = _URL + "api/archivo/generarContratoMatriculaWeb?id_per="+$("#id_per_res").val()+"&id_anio="+$('#_id_anio').text();	  
}
	
	var form = $(".steps-validation").show();
			
	$(".steps-validation").steps(
				{	headerTag : "h6",
					bodyTag : "fieldset",
					transitionEffect : "fade",
					titleTemplate : '<span class="number">#index#</span> #title#',
					autoFocus : true,
					onStepChanging : function(event, currentIndex, newIndex) {
						
						
					/*	if (currentIndex == -1 ) {
							alert('1');
							form.find('a[href$="next"]').hide();			
						}
						
						if (newIndex == 1 ) {
							alert('2');
							form.find('a[href$="next"]').hide();			
						}*/

						if (currentIndex == 0) {
							var cantidadPadres = $('.btnPadre').length;	
							var _cant_pad_act=0;
							//alert('cantidadPadres'+cantidadPadres);
							$( ".btnPadre" ).each(function( index ) {
								 // alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_pad_act=_cant_pad_act+1;
								  }
							});
							//alert(_cant_pad_act);
							
							var cantidadAlumnos=$('.btnAlumno').length;
							//alert('cantidadAlumnos'+cantidadAlumnos);
							var _cant_alu_act=0;
							$( ".btnAlumno" ).each(function( index ) {
								  //alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_alu_act=_cant_alu_act+1;
								  }
							});
							//alert(_cant_alu_act);
							//Regla de Negocio para verificar si la actualizacion de datos es obligatorio
							//_get('api/reglasNegocio/validarActualizacionDatos',
								//function(data){
								//if(data.val=="1")	
										if(cantidadPadres==_cant_pad_act && cantidadAlumnos==_cant_alu_act){
											return true;
										} else {
											return false;
										}	/*else{
											swal({
											  title: "IMPORTANTE",
											  html:true,
											  text: "Es obligatorio la actualización de datos de la papá, mamá y alumnos, para proceder con la matricula!!",
											  icon: "info",
											  button: "Cerrar",
											});
											//alert('Debe de actualizar todos los datos de los padres y alumnos, para proceder con la matricula!!');
											return false;
										}*/	
											
								//});		
						}

						if (currentIndex == 1 && newIndex == 2) {
							var cantidadPadres = $('.btnPadre').length;	
							var _cant_pad_act=0;
							//alert('cantidadPadres'+cantidadPadres);
							$( ".btnPadre" ).each(function( index ) {
								 // alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_pad_act=_cant_pad_act+1;
								  }
							});
							//alert(_cant_pad_act);
							
							var cantidadAlumnos=$('.btnAlumno').length;
							//alert('cantidadAlumnos'+cantidadAlumnos);
							var _cant_alu_act=0;
							$( ".btnAlumno" ).each(function( index ) {
								  //alert($( this ).attr('tipo') );
								  if($( this ).attr('tipo')=='E'){
									  _cant_alu_act=_cant_alu_act+1;
								  }
							});
							
							if(cantidadPadres==_cant_pad_act && cantidadAlumnos==_cant_alu_act){
								if ( $("[name='id_alu_sel']:checked").length >0){
									cargarResumenPagos();
									}
								return true;
							} else {
								swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Es obligatorio la actualización de datos del papá, mamá y alumnos, para proceder con la matricula!!",
								  icon: "info",
								  button: "Cerrar",
								});
								return false ;
							}
							
							// console.log('view tcy' + $("[name='tc_view_con']:checked").length);
							 // console.log('tc_acept' + $("[name='tc_acept']:checked").val());
							//comentado2022 if ( $("[name='reg_int']:checked").length >0 && $("[name='con']:checked").length >0 && $("[name='plan']:checked").length >0 && $("[name='costos']:checked").length >0){
								/*if($("[name='rg_acept']:checked").length>0 && $("[name='con_acept']:checked").length>0 && $("[name='pe_acept']:checked").length>0 && $("[name='co_acept']:checked").length>0){
									if($("[name='rg_acept']:checked").val()==0 && $("[name='con_acept']:checked").val()==0 && $("[name='pe_acept']:checked").val()==0 && $("[name='co_acept']:checked").val()==0 ){
									swal({
										  title: "IMPORTANTE",
										  html:true,
										  text: "Para continuar es necesario aceptar y leer todos los documentos.",
										  icon: "info",
										  button: "Cerrar",
										});
									return false;
									} 
								} else {
									swal({
										  title: "IMPORTANTE",
										  html:true,
										  text: "Para continuar es necesario aceptar y leer todos los documentos.",
										  icon: "info",
										  button: "Cerrar",
										});
									return false;
								}	*/
								
							/* comentado 2022 } else{
								swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Para continuar es necesario abrir y leer los documentos normativos para el año 2021: <li>Reglamento Interno</li> <li>Contrato de Servicios</li><li>Plan de Estudios</li><li>Costos Fijos y Variables minimos del Servicio Educativo (DL NRO. 1476)</li>",
								  icon: "info",
								  button: "Cerrar",
								});
								return false;
							}	*/	
						}
						if (currentIndex === 2 && newIndex === 3) {	
						
						/*if($("#id_per_res").val()==""){
							swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Por favor seleccionar al apoderado.",
								  icon: "info",
								  button: "Cerrar",
								});
									return false;
						} else {
							if ( $("[name='id_alu_sel']:checked").length >0){
								cargarResumenPagos();
							} else {
								swal({
								  title: "IMPORTANTE",
								  html:true,
								  text: "Por favor seleccionar a los alumnos a los que desea matricular.",
								  icon: "info",
								  button: "Cerrar",
								});
									return false;
							}	
						}*/
								
								
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
						//form.validate().settings.ignore = ":disabled";
						//return form.valid();
						/*swal({
						  title: "IMPORTANTE",
						  html:true,
						  type : "warning",
						  //text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
						   text: "<p>A la brevedad verificaremos los documentos enviados y mediante email le comunicaremos la conformidad de ellos, para que pueda realizar los pagos y finalizar con el proceso de matrícula </p>",
						  icon: "info",
						  confirmButtonColor : "rgb(33, 150, 243)",
						  cancelButtonColor : "#EF5350",
						  //confirmButtonText : "OK",
						 button: "Cerrar",
						});*/
						
						swal({
								title: "IMPORTANTE",
								 text: "A la brevedad verificaremos los documentos enviados y mediante email le comunicaremos la conformidad de ellos, para que pueda realizar los pagos y finalizar con el proceso de matrícula",
								type : "warning",
								showCancelButton : true,
								confirmButtonColor : "rgb(33, 150, 243)",
								cancelButtonColor : "#EF5350",
								confirmButtonText : "De acuerdo",
								cancelButtonText : "No, cancelar!!!",
								closeOnConfirm : true,
								closeOnCancel : true
							},
							function(isConfirm) {
								if (isConfirm) {									
								_send('pages/apoderado/apo_menu_matricula_web.html','Matrícula Web','Matrícula Web');
								} else {
									swal({
										title : "Cancelado",
										text : "Subir Docuemntos",
										confirmButtonColor : "#2196F3",
										type : "error"
									});

									return false;
								}
								});
					},

					onFinished : function(event, currentIndex) {
						
					}
				});

$('a[href$="next"]').text('Siguiente >>');
$('a[href$="previous"]').text('<< Anterior ');

function validarEmail(valor) {
  /*if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3,4})+$/.test(valor)){
   alert("La dirección de email " + valor + " es correcta.");
  } else {
   alert("La dirección de email es incorrecta.");
  }*/
  expr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if ( !expr.test(valor) ){
        alert("El email es inválido.");
		$("#frm-familiar_mant #corr").val('');
	}	
}