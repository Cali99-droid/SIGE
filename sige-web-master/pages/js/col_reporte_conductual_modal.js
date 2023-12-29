//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	$("#form_informante").css('display','block');
	$("#form_otro_informante").css('display','none');
	$("#form_otro_familiar").css('display','none'); 
	$("#col_reporte_conductual-frm #id_anio").val($('#_id_anio').text());
	$('#col_reporte_conductual-frm #btn_agregar_alumno').on('click',function(event){
		
		//e.preventDefault();
		reporte_buscar_modal($(this));
	
	});
	$("#otro").click(function() {  
	if ($("#otro").is(':checked')){
		$("#form_informante").css('display','none');
		$("#form_otro_informante").css('display','block');
		//$("#login :checkbox").attr('checked', false);
		//$("#form_informante #login").removeAttr("checked"); 
	} else{            
		$("#form_informante").css('display','block');
		$("#form_otro_informante").css('display','none');
		//$("#login :checkbox").attr('checked', true);
		//$("#form_informante #login").attr("checked");
		$("#form_informante").css('display','block');
	}
	 });  
	
	/*if (link.data('id')){
		_get('api/reporteConductual/' + link.data('id'),
		function(data){
			_fillForm(data,$('#modal').find('form') );
			_llenarCombo('per_periodo',$('#id_per'), data.id_per);
			_llenarCombo('seg_usuario',$('#id_usr'), data.id_usr);
			_llenarCombo('col_curso_aula',$('#id_cca'), data.id_cca);
		});
	}else{*/
		$('#id_per').on('change',function(event){
			var id_nvl=$('#id_per option:selected').attr("data-aux1");
			var param={id_anio:$('#_id_anio').text(), id_niv:id_nvl};
			_llenarComboURL('api/trabajador/profesoresxNivel',$('#id_prof'),null, param);	
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
		var param={id_anio:$('#_id_anio').text(), id_usr: _usuario.id, id_rol:id_rol };
		_llenarComboURL('api/periodoAca/listarPeriodoxTrabajador',$('#id_per'),null, param, function(){$("id_per").change();});
		$("#fecha").val(_fechaActual());
		_llenarCombo('cat_tip_falta',$('#id_ctf'),null,null,function(){$("#id_ctf").change();});
		_llenarCombo('cat_tipo_documento',$('#tip_doc_otro'));
		_get('api/usuario/datosTrabajador/' + _usuario.id,
				function(data){
					console.log(data);
					//$('#id_inf').val(data.id);
					$('#trabajador').val(data.trabajador);
					$('#dni_inf').val(data.dni);
					$('#rol').val(data.rol);
					$('#form_informante #id_inf').val(data.tra_id);
					$('#form_informante #tip_inf').val("T");
		});
		_get('api/alumno/obtenerDatosAlumno/' + params.id_mat,
				function(data){
					console.log(data);
					$('#id_mat').val(data.id);
					$('#alumno_dni').val(data.nro_doc);
					$('#alumno_nom').val(data.alumno);
					$('#aula').val(data.aula);
					$('#edad').val(data.edad);
					$('#sexo').val(data.sexo);
					$('#celular').val(data.celular);
		});
		//_llenarCombo('seg_usuario',$('#id_usr'));
		//_llenarCombo('col_curso_aula',$('#id_cca'));
		$('#col_reporte_conductual-frm #btn-grabar').hide();
		
	//}
		
		$('#id_ctf').on('change',function(event){
			var param={id_ctf:$(this).val()};
			_get('api/falta/listarFalta/',
					function(data){
					console.log(data);
						$('#list_faltas-tabla').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 orderCellsTop:true,
							 select: true,
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
					}, param
			);
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
	        source: _URL + 'api/alumno/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id ,
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
	        	divAlumno = ($("#div-infractor_alumno").last()).clone();
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
				$( "#div-infractor" ).append(divAlumno);
				$(".modal .close").click();
	        	//_send('pages/academico/col_reporte_conductual_modal.html','Reporte Conductual','Reporte Conductual');
	        	//_send('pages/academico/col_historial_conductual_alumno.html','Historial Conducutal del Alumno','Lista de historiales',{alumno:alumno, nivel:nivel, grado:grado, seccion:seccion, id_mat:id_mat});
	        },
	       
	    });
		
		
		
	}

	
	var titulo = 'Nuevo  Reporte Conductual';
	
	_modal(titulo, link.attr('href'),onShowModal,null);

}

$('#col_reporte_conductual-frm #btn-agregar').on('click',function(event){
	$('#col_reporte_conductual-frm #id').val('');
	console.log($(".id_cf").val());
	if($("#id_per").val()==""){
		_alert_error("Seleccione el periodo porfavor!!");
	} else if($("[name='id_cf']:checked").length==0){
		_alert_error("Seleccione las faltas cometidas!!");
	}
	else{
		_post($('#col_reporte_conductual-frm').attr('action') , $('#col_reporte_conductual-frm').serialize(),
				function(data){
					console.log(data);
					_send('pages/academico/col_historial_conductual_alumno.html','Historial Conducutal del Alumno','Lista de historiales',{alumno:data.result.alumno, nivel:data.result.nivel, grado:data.result.grado, seccion:data.result.seccion, id_mat:data.result.id_mat});
					}
				);
	}
	
});

/*$('#btn-grabar').on('click',function(event){
	event.preventDefault();
		_post('api/historialEco/grabarHistorial/'+id_mat , $('#col_reporte_conductual-frm').serialize(),
				function(data){ 
						//onSuccessSave(data) ;
						historial_eco_listar_tabla(id_mat);
					});
	
});*/



