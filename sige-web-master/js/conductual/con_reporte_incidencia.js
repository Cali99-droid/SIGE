//Se ejecuta cuando la pagina q lo llama le envia parametros

function onloadPage(params){
	
	//Cargar por defecto
	$("#btn_agregar_agresorTrabajador").hide();
	$("#btn_agregar_agresorAlumno").hide();
	$("#div-agresor_alumno").css('display','none');
	$("#div-agresor_trabajador").css('display','none');
	$("#col_reporte_conductual-frm #form_informante").css('display','block');
	$("#col_reporte_conductual-frm #form_otro_informante").css('display','none');
	$("#col_reporte_conductual-frm #form_otro_familiar").css('display','none'); 
	$("#col_reporte_conductual-frm #id_anio").val($('#_id_anio').text());
	$("#col_reporte_bulling-frm #id_anio").val($('#_id_anio').text());
	
	$('#id_cti').on('change',function(event){
		if($('#id_cti').val()==2){
			$('#col_reporte_bulling-frm').css('display','block');
			$("#col_reporte_conductual-frm").css('display','none');
			$("#col_reporte_bulling-frm #id_cti_inc").val($('#id_cti').val());
			//_llenarCombo('cat_tip_falta',$('#col_reporte_conductual-frm #id_ctf'),null,null,function(){$("#col_reporte_conductual-frm #id_ctf").change();});
			_llenar_combo({
				url : 'api/tipFalta/tipoReporte/' +$(this).val(),
				combo : $('#col_reporte_bulling-frm #id_ctf'),
				context : _URL_CON
			});
			
		}else{
			$("#col_reporte_conductual-frm").css('display','block');
			$('#col_reporte_bulling-frm').css('display','none');
			$("#col_reporte_conductual-frm #id_cti_inc").val($('#id_cti').val());
			_llenar_combo({
				tabla : 'cat_parentesco',
				combo : $('#id_par'),
				text: 'par',
				context : _URL
			});
			_llenar_combo({
				tabla : 'cat_tipo_documento',
				combo : $('#col_reporte_bulling-frm #tip_doc_otro'),
				context : _URL
			});
			//_llenarCombo('cat_tip_falta',$('#col_reporte_bulling-frm #id_ctf'),null,null,function(){$("#col_reporte_bulling-frm #id_ctf").change();});
			_llenar_combo({
				url : 'api/tipFalta/tipoReporte/' +$(this).val(),
				combo : $('#col_reporte_conductual-frm #id_ctf'),
				context : _URL_CON
			});
			
			//Cargar los tipos de violencia
			_GET({
				context:_URL_CON,
				url:'api/tipoViolencia/listar/',
				success:function(data){
					console.log(data);
					$('#list_tip_violencia_tabla').dataTable({
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
				        	 		return "<label class='checkbox-inline'><input type='checkbox' id='id_ctv" + row.id +"' name='id_ctv' value='"+row.id+"'/><font align='center'>"+row.nom+"</font></label>"; 
				        	 	} 
				        	 	}
					    ]
				    });
				}
			});
			
			_GET({
				context:_URL_CON,
				url:'api/motivoBulling/listar/',
				success:function(data){
					console.log(data);
					$('#list_motivo_violencia_tabla').dataTable({
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
				        	 		return "<input type='checkbox' id='id_cmb" + row.id +"' name='id_cmb' value='"+row.id+"'/><font align='center'>"+row.nom+"</font>";
				        	 	} 
				        	 	}
					    ]
				    });
				}
			});
		}
	});
	
	_llenarCombo('cat_tip_inc',$('#id_cti'),null,null,function(){$('#id_cti').change();});
	
	$('#col_reporte_bulling-frm #id_ctf').on('change',function(event){
		//alert($("#col_reporte_bulling-frm [name=agresor]").val());
		if($(this).val()==3){
			$("#div-agresor_alumno").css('display','block');
			$("#div-agresor_trabajador").css('display','none');
			$("#btn_agregar_agresorTrabajador").hide();
			$("#btn_agregar_agresorAlumno").show();
		} else if($(this).val()==4){
			$("#div-agresor_alumno").css('display','none');
			$("#div-agresor_trabajador").css('display','block');
			$("#btn_agregar_agresorTrabajador").show();
			$("#btn_agregar_agresorAlumno").hide();
		}
	});
	
		
	//CONDUCTUAL
	
	$('#col_reporte_conductual-frm #btn_agregar_alumno').on('click',function(event){
		
		reporte_buscar_modal($(this));
	
	});
	$("#col_reporte_conductual-frm #otro").click(function() {  
		if ($("#col_reporte_conductual-frm #otro").is(':checked')){
			$("#col_reporte_conductual-frm #form_informante").css('display','none');
			$("#col_reporte_conductual-frm #form_otro_informante").css('display','block');
		} else{            
			$("#col_reporte_conductual-frm #form_informante").css('display','block');
			$("#col_reporte_conductual-frm #form_otro_informante").css('display','none');
			$("#col_reporte_conductual-frm #form_informante").css('display','block');
		}
	 });  
	
	//BULLING
	$("#col_reporte_bulling-frm #form_informante").css('display','block');
	$("#col_reporte_bulling-frm #form_otro_informante").css('display','none');
	$("#col_reporte_bulling-frm #form_otro_familiar").css('display','none'); 
	$("#col_reporte_bulling-frm #id_anio").val($('#_id_anio').text());
	

	$("#col_reporte_bulling-frm #otro_check_mot").click(function() {  
		if ($("#col_reporte_bulling-frm #otro_check_mot").is(':checked')){
			$("#col_reporte_bulling-frm #otro_motivo").removeAttr("readonly");
			$('#list_motivo_violencia_tabla').hide();
		} else{            
			$("#col_reporte_bulling-frm #otro_motivo").attr("readonly", "readonly");
			$('#list_motivo_violencia_tabla').show();
		}
	 });  
	
	$('#col_reporte_bulling-frm #btn_agregar_alumno').on('click',function(event){
		
		reporte_buscar_modal($(this));
	
	});
	
	$('#col_reporte_bulling-frm #btn_agregar_agresor').on('click',function(event){
		
		reporte_buscar_agredido($(this));
	
	});
	
	$('#col_reporte_bulling-frm #btn_agregar_agresorAlumno').on('click',function(event){
		
		reporte_buscar_agresorAlumno($(this));
	
	});
	
	$('#col_reporte_bulling-frm #btn_agregar_agresorTrabajador').on('click',function(event){
		
		reporte_buscar_agresorTrabajador($(this));
	
	});
	$("#col_reporte_bulling-frm #otro").click(function() {  
	if ($("#col_reporte_bulling-frm #otro").is(':checked')){
		$("#col_reporte_bulling-frm #form_informante").css('display','none');
		$("#col_reporte_bulling-frm #form_otro_informante").css('display','block');
	} else{            
		$("#col_reporte_bulling-frm #form_informante").css('display','block');
		$("#col_reporte_bulling-frm #form_otro_informante").css('display','none');
		$("#col_reporte_bulling-frm #form_informante").css('display','block');
	}
	 });  
	

		$('#id_per').on('change',function(event){
			var id_nvl=$('#col_reporte_conductual-frm #id_per option:selected').attr("data-aux1");
			var param={id_anio:$('#_id_anio').text(), id_niv:id_nvl};
			_llenarComboURL('api/trabajador/profesoresxNivel',$('#col_reporte_conductual-frm #id_prof'),null, param);	
						
		});
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			id_rol=_ROL_ADMINISTRADOR;
		else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
			id_rol=_ROL_PROFESOR;
		else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
			id_rol=_ROL_TUTOR;
		else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
			id_rol=_ROL_COORDINADOR_TUTOR;
		console.log(_usuario.roles);
		var id_suc=params.id_suc;
		var id_niv=params.id_niv;
		var param={id_anio:$('#_id_anio').text(),id_suc:id_suc,id_niv:id_niv};
		_llenarComboURL('api/periodoAca/listarPeriodosxLocalNivel',$('#col_reporte_conductual-frm #id_per'),null, param, function(){$("#col_reporte_conductual-frm #id_per").change();});
		_llenarComboURL('api/periodoAca/listarPeriodosxLocalNivel',$('#col_reporte_bulling-frm #id_per'),null, param, function(){$("#col_reporte_bulling-frm #id_per").change();});
		//_llenarCombo('per_periodo',$('#id_per'),null, param, function(){$("id_per").change();});
		$("#col_reporte_conductual-frm #fecha").val(_fechaActual());
	
		_llenarCombo('cat_tipo_documento',$('#col_reporte_conductual-frm #tip_doc_otro'));
		_get('api/usuario/datosTrabajador/' + _usuario.id,
				function(data){
					console.log(data);
					//$('#id_inf').val(data.id);
					$('#col_reporte_conductual-frm #trabajador').val(data.trabajador);
					$('#col_reporte_conductual-frm #dni_inf').val(data.dni);
					$('#col_reporte_conductual-frm #rol').val(data.rol);
					$('#col_reporte_conductual-frm #form_informante #id_inf').val(data.tra_id);
					$('#col_reporte_conductual-frm #form_informante #tip_inf').val("T");
					$('#col_reporte_bulling-frm #trabajador').val(data.trabajador);
					$('#col_reporte_bulling-frm #dni_inf').val(data.dni);
					$('#col_reporte_bulling-frm #rol').val(data.rol);
					$('#col_reporte_bulling-frm #form_informante #id_inf').val(data.tra_id);
					$('#col_reporte_bulling-frm #form_informante #tip_inf').val("T");
		});
		

		$('#id_ctf').on('change',function(event){
			var param={id_ctf:$(this).val()};
			_GET({
				context:_URL_CON,
				url:'api/falta/listarFalta/',
				params:param,
				success:function(data){
					console.log(data);
					$('#list_faltas-tabla').dataTable({
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
				        	 		var checked = (row.eva_id!=null) ? ' checked ': ' ';
				        	 		var disabled = (checked) ? 'disabled':'';
				        	 		//var ideva = (row.eva_id!=null) ? row.eva_id: '';
				        	 		return "<input type='hidden' id='id_cf" + row.id +"' name='id_cf' " + disabled + "/><input type='checkbox' " + checked + "id='id_cf"+row.id+"'  name ='id_cf' value='" + row.id + "' class='id_cf' /><font align='center'>"+row.nom+"</font>";
				        	 	} 
				        	 	}
					    ]
				    });
				}
			});
		});	
				
		
		
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
});

function reporte_buscar_modal(link){
//alert(1);
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_reporte_buscar-frm');
		
		$.ajaxSetup({
		    headers: { 'Authorization': getCookie("_token")}
		});
		
		//alert(2);
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			id_rol=_ROL_ADMINISTRADOR;
		else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
			id_rol=_ROL_PROFESOR;
		else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
			id_rol=_ROL_TUTOR;
		else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
			id_rol=_ROL_COORDINADOR_TUTOR;
		$("#alumno").autocomplete({
	        minLength: 3,
	        source: _URL + 'api/alumno/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id+'&id_per='+$('#id_per').val() ,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	var alumno=ui.item.label;
	        	var nivel=ui.item.nivel;
	        	var grado=ui.item.grad;
	        	var seccion=ui.item.secc;
	        	var id_mat=ui.item.id;
	        	if($("#div-infractor_alumno #id_mat").val()==''){
		        	var id_mat=ui.item.id;
	        		$("#div-infractor_alumno #id_mat").val(id_mat);
	        		$("#div-infractor_alumno #alumno_dni").val('11');
	        		$("#div-infractor_alumno #alumno_nom").val(alumno);
	        		$("#div-infractor_alumno #aula").val(seccion);
	        		_get('api/alumno/obtenerDatosAlumno/' +id_mat,
		    				function(data){
		    					console.log(data);
		    					$("#div-infractor_alumno #id_mat").val(data.id);
		    					$("#div-infractor_alumno #alumno_dni").val(data.nro_doc);
		    					$("#div-infractor_alumno #alumno_nom").val(data.alumno);
		    					$("#div-infractor_alumno #aula").val(data.aula);
		    					$("#div-infractor_alumno #edad").val(data.edad);
		    					$("#div-infractor_alumno #sexo").val(data.sexo);
		    					$("#div-infractor_alumno #celular").val(data.celular);
		    					$(".modal .close").click();	
		    		});
	        	} else{
	        		divAlumno = ($("#div-infractor_alumno").last()).clone();
		        	divAlumno.find("#alumno_dni").val('');
		        	divAlumno.find("#alumno_nom").val('');
		        	divAlumno.find("#aula").val('');
		        	divAlumno.find("#id_mat").val(''); 
		        	divAlumno.find("#edad").val('');
		        	divAlumno.find("#sexo").val('');
		        	divAlumno.find("#celular").val('');

		        	_get('api/alumno/obtenerDatosAlumno/' +id_mat,
		    				function(data){
		    					console.log(data);
		    					divAlumno.find('#id_mat').val(data.id);
		    					divAlumno.find('#alumno_dni').val(data.nro_doc);
		    					divAlumno.find('#alumno_nom').val(data.alumno);
		    					divAlumno.find('#aula').val(data.aula);
		    					divAlumno.find('#edad').val(data.edad);
		    					divAlumno.find('#sexo').val(data.sexo);
		    					divAlumno.find('#celular').val(data.celular);
		    		});
					$( "#div-infractor" ).append(divAlumno);
					$(".modal .close").click();	
	        	}
	        },
	    });	
	}

	
	var titulo = 'Nuevo  Reporte Conductual';
	
	_modal(titulo, link.attr('href'),onShowModal,null);

}

$('#col_reporte_conductual-frm #btn-agregar').on('click',function(event){
	$('#col_reporte_conductual-frm #id').val('');
	console.log($(".id_cf").val());
	if($("#col_reporte_conductual-frm #id_per").val()==""){
		_alert_error("Seleccione el periodo porfavor!!");
	} else if($("[name='id_cf']:checked").length==0){
		_alert_error("Seleccione las faltas cometidas!!");
	}
	else{
		
		_POST({
			context:_URL_CON,
			url:$('#col_reporte_conductual-frm').attr('action'),
			form:$('#col_reporte_conductual-frm'),
			success: function(data){
				console.log(data);
				_send('pages/academico/col_historial_conductual_alumno.html','Historial Conducutal del Alumno','Lista de historiales',{alumno:data.result.alumno, nivel:data.result.nivel, grado:data.result.grado, seccion:data.result.seccion, id_mat:data.result.id_mat});				
			}
		});

	}
	
});

$('#col_reporte_conductual-frm #btn-grabar').on('click',function(event){
	//$('#col_reporte_conductual-frm #id').val('');
	console.log($(".id_cf").val());
	if($("#col_reporte_conductual-frm #id_per").val()==""){
		_alert_error("Seleccione el periodo porfavor!!");
	} else if($("[name='id_cf']:checked").length==0){
		_alert_error("Seleccione las faltas cometidas!!");
	}
	else{
		
		_POST({
			context:_URL_CON,
			url:$('#col_reporte_conductual-frm').attr('action'),
			form:$('#col_reporte_conductual-frm'),
			success: function(data){
				console.log(data);
				_send('pages/conductual/con_busqueda_incidencia.html','Lista de Incidencia','Lista de Incidencia');				
			}
		});

	}
	
});

$('#col_reporte_bulling-frm #btn-grabar').on('click',function(event){
	$('#col_reporte_bulling-frm #id').val('');
	console.log($(".id_cf").val());
	if($("#col_reporte_bulling-frm #id_per").val()==""){ //id_ctv id_cmb
		_alert_error("Seleccione el periodo porfavor!!");
	} else if($("[name='id_ctv']:checked").length==0){
		_alert_error("Seleccione los tipos de violencia!");
	} else if($("[name='id_cmb']:checked").length==0){
		_alert_error("Seleccione el motivo de violencia!");
	}
	else{
		
		_POST({
			context:_URL_CON,
			url:$('#col_reporte_bulling-frm').attr('action'),
			form:$('#col_reporte_bulling-frm'),
			success: function(data){
				console.log(data);
				_send('pages/conductual/con_busqueda_incidencia.html','Lista de Incidencia','Lista de Incidencia');				
			}
		});

	}
	
});


//MÓDULO BULLING
function reporte_buscar_agredido(link){
	//alert(1);
		//funcion q se ejecuta al cargar el modal
		var onShowModal = function(){
			
			_inputs('col_reporte_buscar-frm');
			
			$.ajaxSetup({
			    headers: { 'Authorization': getCookie("_token")}
			});
			
			//alert(2);
			if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
				id_rol=_ROL_ADMINISTRADOR;
			else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
				id_rol=_ROL_PROFESOR;
			else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
				id_rol=_ROL_TUTOR;
			else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
				id_rol=_ROL_COORDINADOR_TUTOR;
			$("#alumno").autocomplete({
		        minLength: 3,
		        source: _URL + 'api/alumno/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id+'&id_per='+$('#id_per').val() ,
		        search: function() {
		            $(this).parent().addClass('ui-autocomplete-processing');
		        },
		        open: function() {
		            $(this).parent().removeClass('ui-autocomplete-processing');
		        },
		        select: function( event, ui ) {
		        	var alumno=ui.item.label;
		        	var nivel=ui.item.nivel;
		        	var grado=ui.item.grad;
		        	var seccion=ui.item.secc;
		        	var id_mat=ui.item.id;
		        	//if($("#div_form_agredido #id_vic").val()==''){
			        	var id_mat=ui.item.id;
		        		$("#div_form_agredido #id_vic").val(id_mat);
		        		$("#div_form_agredido #alumno_dni").val('11');
		        		$("#div_form_agredido #agredido").val(alumno);
		        		$("#div_form_agredido #aula").val(seccion);
		        		$(".modal .close").click();	
		        		$("#btn_agregar_agresor").hide();
		        },
		    });	
		}

		
		var titulo = 'Nuevo  Reporte Conductual';
		
		_modal(titulo, link.attr('href'),onShowModal,null);

	}


function reporte_buscar_agresorAlumno(link){
	//alert(1);
		//funcion q se ejecuta al cargar el modal
		var onShowModal = function(){
			
			_inputs('col_reporte_buscar-frm');
			
			$.ajaxSetup({
			    headers: { 'Authorization': getCookie("_token")}
			});
			
			//alert(2);
			if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
				id_rol=_ROL_ADMINISTRADOR;
			else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
				id_rol=_ROL_PROFESOR;
			else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
				id_rol=_ROL_TUTOR;
			else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
				id_rol=_ROL_COORDINADOR_TUTOR;
			$("#alumno").autocomplete({
		        minLength: 3,
		        source: _URL + 'api/alumno/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id+'&id_per='+$('#id_per').val() ,
		        search: function() {
		            $(this).parent().addClass('ui-autocomplete-processing');
		        },
		        open: function() {
		            $(this).parent().removeClass('ui-autocomplete-processing');
		        },
		        select: function( event, ui ) {
		        	var alumno=ui.item.label;
		        	var nivel=ui.item.nivel;
		        	var grado=ui.item.grad;
		        	var seccion=ui.item.secc;
		        	var id_mat=ui.item.id;
		        	if($("#div-agresor_alumno #id_mat").val()==''){
			        	var id_mat=ui.item.id;
		        		$("#div-agresor_alumno #id_mat").val(id_mat);
		        		$("#div-agresor_alumno #alumno_dni").val('11');
		        		$("#div-agresor_alumno #alumno_nom").val(alumno);
		        		$("#div-agresor_alumno #aula").val(seccion);
		        		_get('api/alumno/obtenerDatosAlumno/' +id_mat,
			    				function(data){
			    					console.log(data);
			    					$("#div-agresor_alumno #id_mat").val(data.id);
			    					$("#div-agresor_alumno #alumno_dni").val(data.nro_doc);
			    					$("#div-agresor_alumno #alumno_nom").val(data.alumno);
			    					$("#div-agresor_alumno #aula").val(data.aula);
			    					$("#div-agresor_alumno #edad").val(data.edad);
			    					$("#div-agresor_alumno #sexo").val(data.sexo);
			    					$("#div-agresor_alumno #celular").val(data.celular);
			    					$(".modal .close").click();	
			    		});
		        	} else{
		        		divAlumno = ($("#div-agresor_alumno").last()).clone();
			        	divAlumno.find("#alumno_dni").val('');
			        	divAlumno.find("#alumno_nom").val('');
			        	divAlumno.find("#aula").val('');
			        	divAlumno.find("#id_mat").val(''); 
			        	divAlumno.find("#edad").val('');
			        	divAlumno.find("#sexo").val('');
			        	divAlumno.find("#celular").val('');
			        	//divAlumno.find("#edad").val('');
			        	//divAlumno.find("#sexo").val('');
			        	//divAlumno.find("#alumno_dni").val(alumno);
			        	//divAlumno.find("#alumno_nom").val(alumno);
			        	//divAlumno.find("#aula").val('');
			        	//divAlumno.find("#id_mat").val('');
			        	_get('api/alumno/obtenerDatosAlumno/' +id_mat,
			    				function(data){
			    					console.log(data);
			    					divAlumno.find('#id_mat').val(data.id);
			    					divAlumno.find('#alumno_dni').val(data.nro_doc);
			    					divAlumno.find('#alumno_nom').val(data.alumno);
			    					divAlumno.find('#aula').val(data.aula);
			    					divAlumno.find('#edad').val(data.edad);
			    					divAlumno.find('#sexo').val(data.sexo);
			    					divAlumno.find('#celular').val(data.celular);
			    		});
						$( "#div-agresor" ).append(divAlumno);
						$(".modal .close").click();	
		        	}
		        },
		    });	
		}

		
		var titulo = 'Nuevo  Reporte Conductual';
		
		_modal(titulo, link.attr('href'),onShowModal,null);

	}

function reporte_buscar_agresorTrabajador(link){
	alert(1);
		//funcion q se ejecuta al cargar el modal
		var onShowModal = function(){
			
			_inputs('con_trabajador_buscar-frm');
			
			$.ajaxSetup({
			    headers: { 'Authorization': getCookie("_token")}
			});
			
			//alert(2);
			if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
				id_rol=_ROL_ADMINISTRADOR;
			else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
				id_rol=_ROL_PROFESOR;
			else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
				id_rol=_ROL_TUTOR;
			else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
				id_rol=_ROL_COORDINADOR_TUTOR;
			$("#trab_agresor").autocomplete({
		        minLength: 3,
		        source: _URL + 'api/trabajador/listarTrabajadores' ,
		        search: function() {
		            $(this).parent().addClass('ui-autocomplete-processing');
		        },
		        open: function() {
		            $(this).parent().removeClass('ui-autocomplete-processing');
		        },
		        select: function( event, ui ) {
		        	var trabajador=ui.item.label;
		        	var nro_doc=ui.item.nro_doc;
		        	var cargo=ui.item.cargo;
			        	var id_tra=ui.item.id;
		        		$("#div-agresor_trabajador #id_tra").val(id_tra);
		        		$("#div-agresor_trabajador #trabajador_dni").val('11');
		        		$("#div-agresor_trabajador #trabajador_nom").val(trabajador);
		        		$("#div-agresor_trabajador #cargo").val(cargo);
						$(".modal .close").click();	
		        },
		    });	
		}

		
		var titulo = 'Nuevo Agresor';
		
		_modal(titulo, link.attr('href'),onShowModal,null);

	}


