//Se ejecuta cuando la pagina q lo llama le envia parametros
var grad_seleccionado=null;
var nom_grad_seleccionado=null;
var area_seleccionado=null;
var nom_area_seleccionado=null;
function onloadPage(params){
	_onloadPage();
	$('#div_cualitativo').hide();
	$('#div_cuantitativo').hide();
	var id_tra=_usuario.id_tra;
	//Cargar los combos
	/*$('#id_gir').on('change',function(event){
		_llenarComboURL('api/periodo/listarNivelesxGiroNegocio',$('#id_niv'),null,null,function(){$('#id_niv').change();});	
	});	*/
	
	var id_anio=$("#_id_anio").text();
	
	$('#id_cpu').on('change',function(event){
		listar_grados($('#id_niv').val(),$('#id_gir').val());
	});	
	
	$('#id_tca').on('change',function(event){
		if($('#id_tca option:selected').attr("data-aux1")=="CUALI"){
			$('#div_cualitativo').show();
			$('#div_cuantitativo').hide();
		} else if($('#id_tca option:selected').attr("data-aux1")=="CUANTI"){
			$('#div_cualitativo').hide();
			$('#div_cuantitativo').show();
		}	
		
	});	
	
	_llenarComboURL('api/areaAnio/listarTiposCalificacion',$('#id_tca'),null);
	$('#id_niv').on('change',function(event){
		var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text(), id_gir: $('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1, function() {
				
				//$('#id_cpu').change();
		});
		//if(com_seleccionado!=null){
			listar_grados($('#id_niv').val(),$('#id_gir').val());
		//}			
	});	
	
	
	$('#id_gir').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){
				$('#id_niv').find('option[value=""]').remove();
				$('#id_niv').change();
				});
		} else {
			_llenarComboURL('api/disenioCurricular/listarNivelesComboxGiro/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){$('#id_niv').change();});
		}	
		
	});	
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}	
	
	
	
	
	/*_llenarCombo('cat_nivel',$('#id_niv'),$('#id_niv').val(),null, function(){	
		$('#id_niv').change();	
	});*/
	
}

function listar_grados(id_niv, id_gir){
	$('#tabla_grados').html('');
	var param = {id_niv:id_niv, id_gir:id_gir};
	_get('api/grad/listarTodosGradosxGiro',function(data){
	if(data.length>0){
	var id_gra_pri=data[0].id;
	grad_seleccionado=id_gra_pri;
	nom_grad_seleccionado=data[0].value;
	} else {
		grad_seleccionado=0;
	}	
	$('#tabla_grados').dataTable({
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
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nivel", "data": "nivel"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'" data-nom="'+row.value+'"/>'+row.value; 
			}},
			{"title":"Generar Libretas", "render": function ( data, type, row ) {
				   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
					return '<button type="button" id="btn-generar" class="btn btn-primary pull-center btn-sm" data-id_gra="' + row.id+ '" onclick="generar_libreta(this,event)">Generar</button>';
			}},
			{"title":"Descargar", "render": function ( data, type, row ) {
						 return '<a href="#" data-id_gra="' + row.id+ '" onclick="pdf(this,event)" title="Descargar Libretas"><i class="icon-file-pdf mr-3 icon-2x" style="color:red"></i></a>'; 
						//return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="alumno_editar('+row.cod+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'
			}},
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				//listar_capacidades(com_seleccionado);
				//listar_desempenios(com_seleccionado,$('#id_grad').val());
				
				//listar_giro_negocio(id_dc_pri);
				
				$('#nom_grado').text(nom_grad_seleccionado);
				
				 
				}
			});
	},param);
}

function generar_libreta(obj,event){
	var id_gra=$(obj).data('id_gra');
	//var param ={id_anio: $('#_id_anio').text(), id_cpu:$('#id_cpu').val(), id_gra:id_gra};
		/*_get('api/nota/generarLibretaNuevo/'+$('#_id_anio').text()+'/'+$('#id_cpu').val()+'/'+id_gra,
			function(data){
			
		});*/
		
	//	var id_fmo = $(o).data('id_fmo');
	document.location.href=_URL + 'api/archivo/generarLibretaNuevo/'+$('#_id_anio').text()+'/'+$('#id_cpu').val()+'/'+id_gra+'/'+$('#id_gir').val();
}	

function pdf(obj,event){
	var id_gra=$(obj).data('id_gra');
	//alert();
	//e.preventDefault();
	//var id_fmo = $(o).data('id_fmo');
	document.location.href=_URL + "api/archivo/downloadFiles2/"+$('#id_gir').val()+"/"+$('#_id_anio').text()+"/"+id_gra+"/"+$('#id_cpu').val();
	//document.location.href="http://login.ae.edu.pe:8080/documentos/ContratoColegioAE2021.pdf";
	//document.location.href="http://login.ae.edu.pe:8080/documentos/Contrato Colegio AE.pdf";
	//window.open(url,'Download');
	//document.execCommand('SaveAs',true,url);
}	

$('#frm-calificacion_periodo #btn_grabar').on('click',function(event){
	$('#frm-calificacion_periodo #id_tca').prop('disabled',false);
		var validation = $('#frm-calificacion_periodo').validate(); 
		if ($('#frm-calificacion_periodo').valid()){
			_post($('#frm-calificacion_periodo').attr('action') , $('#frm-calificacion_periodo').serialize(),function(data) {
				$('#frm-calificacion_periodo #id_tca').prop('disabled',true);
				//$('#frm-mant_periodo #id').val(data.result);
				//listar_periodos(id_anio);
			}	
			);
		}	
		
});

$('#btn_agregar_areas').on('click',function(event){
	areas_modal($("#_id_anio").text(), grad_seleccionado);
});

$('#btn_agregar_cursos').on('click',function(event){
	cursos_modal(area_seleccionado, nom_area_seleccionado, nom_grad_seleccionado)
});

function areas_modal(id_anio, id_gra){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_area_anio_dc-frm');
		$('#col_area_anio_dc-frm #id_gra').val(id_gra);
		$('#col_area_anio_dc-frm #id_anio').val($('#_id_anio').text());
		$('#col_area_anio_dc-frm #id_gir').val($('#id_gir').val());
		$('#col_area_anio_dc-frm #btn-grabar').on('click',function(event){
			$('#col_area_anio_dc-frm #id').val('');
			_post($('#col_area_anio_dc-frm').attr('action') , $('#col_area_anio_dc-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		_llenarComboURL('api/areaAnio/listarTiposCalificacion',$('#col_area_anio_dc-frm #id_tca'),null);
		_llenarComboURL('api/areaAnio/listarTiposPromedio',$('#col_area_anio_dc-frm #id_pro_per'),null);
		_llenarComboURL('api/areaAnio/listarTiposPromedio',$('#col_area_anio_dc-frm #id_pro_anu'),null);
		var param ={id_anio: id_anio, id_gra:id_gra};
		_get('api/areaAnio/listarAreasDCN',
			function(data){
			$('#tabla_areas_dc').dataTable({
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
						//{"title":"Nombre", "data": "nom"},
						{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
							var checked = (row.id_area_anio!=null && row.est=="A") ? ' checked ': ' ';
							var disabled = (checked) ? '':'disabled';
							//var id_ind = (row.nie_id!=null) ? row.nie_id: '';
							return "<input type='hidden' id='id_caa" + row.id_adc +"' name='id_caa' " + disabled + " value='"+row.id_area_anio+"'/><input type='checkbox'"+checked+" id='id_adc"+row.id+"' name ='id_adc' value='" + row.id_adc + "' data-id_area='"+row.id_area+"'  onclick='seleccionarArea(this)'/>";
						}}, 
						{"title":"Area", "data": "area"} //,
						/*{"title":"Nombre", "render": function ( data, type, row ) {
							return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value 
						}}*/
						/*{"title":"Acciones", "render": function ( data, type, row ) {
							return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
							'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
						}}*/
						],
							"initComplete": function( settings ) {
							//_initCompleteDT(settings);
							_initCompleteDTSB(settings);
							
							}
						});
		}, param);
	}	
	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		//festivo_listar_tabla();
		listar_areas_grado(id_gra, id_anio);
	}
	
	//Abrir el modal
	var titulo;
		titulo = 'Configuración de Áreas';
	
	_modal(titulo, 'pages/academico/col_area_anio_dc_modal.html',onShowModal,onSuccessSave);

}

function seleccionarArea(campo1){
	var campo =$(campo1);
	var id = campo.val();
	if(campo.is(':checked')){
		$('#id_caa' + id).prop('disabled',false);
	}else{
		//$('#id_caa' + id).val('null');
		$('#id_caa' + id).prop('disabled',true);
	}
}	

function listar_calificacion_periodo(id_gra, id_anio, id_cpu,nom_grad_seleccionado){
	$("#nota_cuali_AD").prop("checked",false);
	$("#nota_cuali_A").prop("checked",false);
	$("#nota_cuali_B").prop("checked",false);
	$("#nota_cuali_C").prop("checked",false);
	$('#frm-calificacion_periodo #nota_ini').val('');
	$('#frm-calificacion_periodo #nota_fin').val('');
	$('#frm-calificacion_periodo #nom_grado').text(nom_grad_seleccionado);
	$('#frm-calificacion_periodo #id_gra').val(id_gra);
	$('#frm-calificacion_periodo #id_anio').val(id_anio);
	$('#frm-calificacion_periodo #id_cpu').val(id_cpu);
	
	var param={id_gra:id_gra, id_anio:id_anio, id_cpu:id_cpu};
	_get('api/perUni/detallePeriodoCalificacion',
			function(data){
			var id_tca=data.id_tca;
			$('#frm-calificacion_periodo #id_tca').val(id_tca).change();
			$('#frm-calificacion_periodo #nota_ini').val(data.nota_ini);
			$('#frm-calificacion_periodo #nota_fin').val(data.nota_fin);
			$('#frm-calificacion_periodo #id_tca').prop('disabled',true);
			var listaLetras=data.notas;
			var i=0;
			for (var key in data.notas) {
				i++;					
				var letra = data.notas[key].letras;
				console.log(letra);
				//$(".div_cualitativo").each(function (index, value) {							
					//Para el formato de entrevistas
					//console.log($(this).find("#nota_cuali_"+letra+'').html());
					$("#nota_cuali_"+letra+'').prop("checked",true);
				//});
				
			}	
		
	}, param);
}

function listar_cursos_anio(id_caa){
	$('#tabla_cursos').html('');
	var param = {id_caa:id_caa};
	_get('api/areaAnio/listarCursosDCNXArea',function(data){
	$('#tabla_cursos').dataTable({
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
			//{"title":"Nombre", "data": "nom"},
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'" data-nom="'+row.curso+'"/>'+row.curso; 
			}}
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings); 
				}
			});
	},param);
}

function cursos_modal(id_caa, nom_area, nom_grado){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_anio_dc-frm');
		$('#col_curso_anio_dc-frm #id_caa').val(id_caa);
		$('#nom_area_dc').val(nom_area);
		$('#nom_grado_dc').val(nom_grado);
		//$('#col_area_anio_dc-frm #id_anio').val($('#_id_anio').text());
		//$('#col_area_anio_dc-frm #id_gir').val($('#id_gir').val());
		$('#col_curso_anio_dc-frm #btn-grabar').on('click',function(event){
			$('#col_curso_anio_dc-frm #id').val('');
			_post($('#col_curso_anio_dc-frm').attr('action') , $('#col_curso_anio_dc-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		//_llenarComboURL('api/areaAnio/listarTiposCalificacion',$('#col_area_anio_dc-frm #id_tca'),null);
		var param ={id_caa: id_caa};
		_get('api/areaAnio/listarCursosDCNGrado',
			function(data){
			$('#tabla_cursos_dc').dataTable({
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
						//{"title":"Nombre", "data": "nom"},
						{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
							var checked = (row.id_cua!=null && row.est=="A") ? ' checked ': ' ';
							//alert(checked);
							//var disabled = (checked=="checked") ? '':'disabled';
							if(row.id_cua!=null && row.est=="A"){
								return "<input type='hidden' id='id_cua" + row.id +"' name='id_cua' value='"+row.id_cua+"'/><input type='checkbox'"+checked+" id='id_cur"+row.id+"' name ='id_cur' value='" + row.id+ "'  onclick='seleccionarCurso(this)'/>";
							} else{
								return "<input type='hidden' id='id_cua" + row.id +"' name='id_cua' disabled value='"+row.id_cua+"'/><input type='checkbox'"+checked+" id='id_cur"+row.id+"' name ='id_cur' value='" + row.id+ "'  onclick='seleccionarCurso(this)'/>";
							}
							
						}}, 
						{"title":"Curso", "data": "curso"} //,
						/*{"title":"Nombre", "render": function ( data, type, row ) {
							return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.value 
						}}*/
						/*{"title":"Acciones", "render": function ( data, type, row ) {
							return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="empresa_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
							'<a href="#" data-id="' + row.id + '" onclick="empresa_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
						}}*/
						],
							"initComplete": function( settings ) {
							//_initCompleteDT(settings);
							_initCompleteDTSB(settings);
							
							}
						});
		}, param);
	}	
	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		//festivo_listar_tabla();
		listar_cursos_anio(id_caa);
	}
	
	//Abrir el modal
	var titulo;
		titulo = 'Configuración de Cursos';
	
	_modal(titulo, 'pages/academico/col_curso_anio_dc_modal.html',onShowModal,onSuccessSave);

}

function seleccionarCurso(campo1){
	var campo =$(campo1);
	var id = campo.val();
	if(campo.is(':checked')){
		$('#id_cua' + id).prop('disabled',false);
	}else{
		//$('#id_caa' + id).val('null');
		$('#id_cua' + id).prop('disabled',true);
	}
}	


