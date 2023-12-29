//Se ejecuta cuando la pagina q lo llama le envia parametros
var grad_seleccionado=null;
var nom_grad_seleccionado=null;
var area_seleccionado=null;
var nom_area_seleccionado=null;
function onloadPage(params){
	_onloadPage();
	
	var id_tra=_usuario.id_tra;
	//Cargar los combos
	/*$('#id_gir').on('change',function(event){
		_llenarComboURL('api/periodo/listarNivelesxGiroNegocio',$('#id_niv'),null,null,function(){$('#id_niv').change();});	
	});	*/
	var id_anio=$("#_id_anio").text();
	$('#id_niv').on('change',function(event){
		//if(com_seleccionado!=null){
			listar_grados($('#id_niv').val(),$('#id_gir').val());
		//}			
	});	
	
	
	$('#id_gir').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
			_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){
				$('#id_niv').find('option[value=""]').remove();
				$('#id_niv').change();
				});
		} else {
			_llenarComboURL('api/disenioCurricular/listarNivelesComboxGiro/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){$('#id_niv').change();});
		}	
		
	});	
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
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
			}}
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
				listar_areas_grado(grad_seleccionado, $("#_id_anio").text(),$("#id_gir").val());
				$('#nom_grado').text(nom_grad_seleccionado);
				$("#tabla_grados tbody tr").dblclick(function(){  
					grad_seleccionado=($(this).find('td:nth-child(2)').find('#id').val());
					areas_modal($("#_id_anio").text(),grad_seleccionado, $("#id_gir").val());
				});	 
				$("#tabla_grados tbody tr").click(function(){ 
				console.log($(this).find('td:nth-child(2)').find('#id').data("nom"));
					nom_grad_seleccionado=($(this).find('td:nth-child(2)').find('#id').data("nom"));
					$('#nom_grado').text(nom_grad_seleccionado);
					grad_seleccionado=($(this).find('td:nth-child(2)').find('#id').val());
					listar_areas_grado(grad_seleccionado, $("#_id_anio").text(),$("#id_gir").val());
				});	 
				}
			});
	},param);
}

$('#btn_agregar_areas').on('click',function(event){
	areas_modal($("#_id_anio").text(), grad_seleccionado,$("#id_gir").val());
});

$('#btn_agregar_cursos').on('click',function(event){
	cursos_modal(area_seleccionado, nom_area_seleccionado, nom_grad_seleccionado)
});

function areas_modal(id_anio, id_gra, id_gir){

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
		
		
		var param ={id_anio: id_anio, id_gra:id_gra, id_gir: id_gir};
		_get('api/areaAnio/listarAreasDCN',
			function(data){
			var id_tca="";
			var id_pro_per="";
			var id_pro_anu="";
			if(data.length>0){
				 for (var i = 0; i < data.length; i++) {
                  if(data[i].id_tca!=null){
					  id_tca=data[i].id_tca;
					  id_pro_per=data[i].id_pro_per;
					  id_pro_anu=data[i].id_pro_anu;
					  break;
				  }	  
                }
				$('#col_area_anio_dc-frm #id_tca').val(id_tca).change();
				$('#col_area_anio_dc-frm #id_pro_per').val(id_pro_per).change();
				$('#col_area_anio_dc-frm #id_pro_anu').val(id_pro_anu).change();
			}	
			
			_llenarComboURL('api/areaAnio/listarTiposCalificacion',$('#col_area_anio_dc-frm #id_tca'),id_tca);
			_llenarComboURL('api/areaAnio/listarTiposPromedio',$('#col_area_anio_dc-frm #id_pro_per'),id_pro_per);
			_llenarComboURL('api/areaAnio/listarTiposPromedio',$('#col_area_anio_dc-frm #id_pro_anu'),id_pro_anu);
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
		listar_areas_grado(id_gra, id_anio, id_gir);
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

function listar_areas_grado(id_gra, id_anio, id_gir){
	$('#tabla_areas').html('');
	var param={id_gra:id_gra, id_anio:id_anio, id_gir: id_gir};
	_get('api/areaAnio/listarAreasDCNGrado',function(data){
		if(data.length>0){
		var id_area_pri=data[0].id_area_anio;
		area_seleccionado=id_area_pri;
		nom_area_seleccionado=data[0].value;
		} else {
			area_seleccionado=0;
		}	
	$('#tabla_areas').dataTable({
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
			{"title":"Area", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id_area_anio+'" data-nom="'+row.area+'" data-grado="'+row.grado+'"/>'+row.area 
			}},
			{"title":"Calificación", "data": "cal"},
			{"title":"P. Periodo", "data": "pro_per"},
			{"title":"P. Anual", "data": "pro_anu"},
			{"title":"Orden", "data": "ord"},
			/*{"title":"Acciones", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/><div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="local_editar('+row.id+')" class="list-icons-item"><i class="icon-pencil7 ui-blue ui-size" aria-hidden="true"></i></a>'+ 
				'<a href="#" data-id="' + row.id + '" onclick="local_eliminar('+row.id+')" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			}}*/
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_cursos_anio(area_seleccionado);
				$("#tabla_areas tbody tr").dblclick(function(){  
					area_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					nom_area_seleccionado=($(this).find('td:nth-child(1)')).data("nom");
					//nom_grad_seleccionado=($(this).find('td:nth-child(1)').find('#grado').val());
					//alert(nom_area_seleccionado);
					//alert(nom_grad_seleccionado);
					cursos_modal(area_seleccionado,nom_area_seleccionado,nom_grad_seleccionado);
				});	 
				$("#tabla_areas tbody tr").click(function(){ 
					area_seleccionado=($(this).find('td:nth-child(1)').find('#id').val());
					listar_cursos_anio(area_seleccionado);
				});
				}
			});
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


