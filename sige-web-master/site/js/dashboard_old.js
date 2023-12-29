var _usuario = null;
var _familiar;

$(function(){
	
/*
 * var swalInit = swal.mixin({ buttonsStyling: false, confirmButtonClass: 'btn
 * btn-primary', cancelButtonClass: 'btn btn-light' });
 */
	validarSessionToken(); //para las matriculas onlene comentado
	_llenarComboURL('/api/anio/listarAnios',$('#anio_com'), 2, null,null);
	$.ajaxSetup({
	    headers: { 'Authorization': getCookie("_token")}
	});

	
	$("#_alumno").autocomplete({
        minLength: 3,
        source: _URL + 'api/alumno/autocomplete?id_anio=' + $('#_id_anio').text(),
        search: function() {
            $(this).parent().addClass('ui-autocomplete-processing');
            $('#panel-alumno').hide();
        },
        open: function() {
            $(this).parent().removeClass('ui-autocomplete-processing');
        },
        select: function( event, ui ) {
        	$('#id_niv').val(ui.item.id_niv);
        	$('#id_mat').val(ui.item.id);
        	$('#nivel').val(ui.item.nivel);
        	$('#grado').val(ui.item.grado);
        	$('#seccion').val(ui.item.secc);
        	$('#id_mat').val(ui.item.id);
       	
        	__ver_alumno(ui.item.id);
       	 
        },
        change: function( event, ui ) {

        }

       
    });
	
	// INICIO ONLOAD
	var fncExitoSESSION = function(data){
		//alert('fncExitoSESSION');
		_response(data);

		if(data==null)
			document.location.href = "index.html";
// data.usuario.ini=2;
		_usuario = data.usuario;
		$('#_id_suc').text(_usuario.id_suc);
		console.log("_usuario",_usuario);
		
		var ini = _usuario.ini;// si ya registro sus datos completos?. por
								// defecto vacio o null

		if(_usuario.id_per==_PERFIL_FAMILIAR){
 			_familiar = data.familiar;
			$('#_foto').attr('src','/colegio/Foto/familiar/' + _familiar.id);
			
			// $('.language-switch').unbind();
 			$('#_anios').html('');
			
			
			$('#usuario\\.trabajador\\.ape_pat').text(_familiar.ape_pat);
			$("#usuario\\.trabajador\\.nom").text(_familiar.nom);
			
			$('#link_profile').on('click', function(){
				_send('pages/alumno/alu_familiar_form.html','Actualizaci&oacute;n de datos personales','Datos del familiar');
				//_send('pages/campus_virtual/cv_info_modal','Informaci&oacute;n del campus virtual','Campus Virtual');
			});
			$('#heading-familiar').css('display', 'inline-block');
			$('#heading-trabajador').css('display', 'none');

		}
		if (_usuario.id_per==_PERFIL_TRABAJADOR || _usuario.id_per==_PERFIL_EXTERNO ){
			$("#usuario\\.trabajador\\.nom").text(_usuario.nombres);

			$('#link_profile').on('click', function(){
				_send('pages/seguridad/ges_trabajador_form.html','Actualizaci&oacute;n','Datos del trabajador');
			});

			$('#_alumno').css('display','inline-block');

		}

		// if (ini<_ESTADO_CAMBIO_CLAVE &&
		// _usuario.id_per==_PERFIL_FAMILIAR){//todavia no ha completado la
		// validacion de usuario
		/*if (ini<_ESTADO_CELULAR && _usuario.id_per==_PERFIL_FAMILIAR){// todavia
																		// no ha
																		// completado
																		// la
																		// validacion
																		// de
																		// usuario
		
			// alert();
			//$(".sidebar-main").css('display','none');
			
			if (ini==null){
				alert();
				if (_usuario.id_per==_PERFIL_TRABAJADOR ){
					_send('pages/seguridad/ges_trabajador_form.html','Actualizaci&oacute;n de datos personales','Datos del trabajador');
				}else if(_usuario.id_per==_PERFIL_FAMILIAR){
					//_send('pages/alumno/alu_familiar_validacion.html','Verificaci&oacute;n de datos personales','Datos del familiar',{ini:ini});
					_send('pages/campus_virtual/cv_info_modal.html','Verificaci&oacute;n de datos personales','Datos del familiar');
					//$('.content-body').html('');
				}
			}
			/*else{
				if(_usuario.id_per==_PERFIL_FAMILIAR){

					_send('pages/alumno/alu_familiar_validacion.html','Verificaci&oacute;n de datos personales','Datos del familiar',{ini:ini});
					
				}
			}*/
			
	//	}else{
			$('body').removeClass('login-container');
			
			if (_usuario.id_per==_PERFIL_TRABAJADOR ||  _usuario.id_per==_PERFIL_EXTERNO){
				

				if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
					//MODULO DE MATRICULA
					$("#li_modulo_matricula").css('display','block');
						$("#li_admision").css('display','block');
							$("#li_registro_familia").css('display','block');
							$("#li_otrogamiento_vacante").css('display','block');
								$("#li_inscripcion_eva").css('display','block');
								$("#li_evaluacion_economica").css('display','block');
								$("#li_evaluacion_psicologica").css('display','block');
								$("#li_admi_resultados").css('display','block');
								$("#li_constancia_vacante").css('display','block');
							$("#li_admi_reportes").css('display','block');
							$("#li_admision_conf").css('display','block');
						$("#li_matricula").css('display','block');
							$("#li_aula_sugerida").css('display','block');
							$("#li_col_situacion_mat").css('display','block');
							$("#li_generacion_usr_alu").css('display','block');
							$("#li_cambio_local").css('display','block');
							$("#li_cambio_aula").css('display','block');
							$("#li_condicion_eco").css('display','block');
							$("#li_matricula_reportes").css('display','block');	
								$("#li_padres_codigo_barras").css('display','block');
								$("#li_usuarios_alumnos").css('display','block');
							$("#li_matricula_conf").css('display','block');
						$("#li_campus_virtual").css('display','block');
							$("#li_inscripciones").css('display','block');
							$("#li_asignar_grupos").css('display','block');
						$("#li_traslados").css('display','block');
							$("#li_registro_traslados").css('display','block');
							$("#li_reporte_traslados").css('display','block');
					
					//MODULO TESORERIA
					$("#li_tesoreria").css('display','block');
						$("#li_caja").css('display','block');
							$("#li_caja_reportes").css('display','block');
							$("#li_caja_conf").css('display','block');
						$("#li_tarifa_emergencia").css('display','block');
						$("#li_nota_credito").css('display','block');
						$("#li_tesoreria_banco").css('display','block');
							$("#li_banco_exp").css('display','block');
							$("#li_banco_carga").css('display','block');
							$("#li_banco_reporte").css('display','block');
						$("#li_teso_admin_electronica").css('display','block');
						$("#li_cartera_morosa").css('display','block');
						$("#li_tesoreria_conf").css('display','block');
						
					//MODUL ACADEMICO
					$("#li_academico").css('display','block');	
						$("#li_planificacion").css('display','block');
							$("#li_planificacion_reportes").css('display','block');
								$("#li_reporte_temario_general").css('display','block');
								$("#li_reporte_capacidad").css('display','block');
								$("#li_reporte_unidades").css('display','block');
								$("#li_reporte_unidades_docente").css('display','block');
								$("#li_reporte_programacion_anual").css('display','block');
							$("#li_planificacion_conf").css('display','block');
						$("#li_evaluacion").css('display','block');
							$("#li_nota_traslados").css('display','block');	
							$("#li_evaluacion_reportes").css('display','block');
								$("#li_reporte_nota_curso").css('display','block');
								$("#li_reporte_nota_aula").css('display','block');
								$("#li_reporte_nota_area").css('display','block');
								$("#li_reporte_consolidado_docente").css('display','block');
								$("#li_reporte_situacion_final").css('display','block');
								$("#li_reporte_entrega_lib").css('display','block');
								$("#li_registros_auxiliares").css('display','block');
							$("#li_evaluacion_configuracion").css('display','block');
						$("#li_asistencia").css('display','block');
							$("#li_asistencia_codigo_barras").css('display','block');
							$("#li_asistencia_codigo_barras_seccion").css('display','block');
							$("#li_asistencia_procesar").css('display','block');
							$("#li_asistencia_reportes").css('display','block');
								$("#li_asistencia_diaria").css('display','block');
								$("#li_asistencia_estadistica").css('display','block');
								$("#li_asistencia_ranqueo").css('display','block');
								$("#li_asistencia_educando").css('display','block');
								$("#li_registros_auxiliares").css('display','block');
								$("#li_registro_asistencia_mensual").css('display','block');
							$("#li_asistencia_conf").css('display','block');
						$("#li_conductual").css('display','block');
							$("#li_nota_comportamiento").css('display','block');
							$("#li_condicion_cond").css('display','block');
							$("#li_busqueda_incidencia").css('display','block');
							$("#li_conductual_reportes").css('display','block');
								$("#li_reporte_notas_com").css('display','block');
					$("#li_administracion_ie").css('display','block');
					
					
					
					/*$("#li_academico").css('display','block');
					$("#li_asistencia").css('display','block');
					$("#li_reportes").css('display','block');*/
					$("#li_seguridad").css('display','block');
					$("#li_configuracion_gen").css('display','block');
					$("#li_prueba").css('display','block');
					$("#li_padres").css('display','block');
					
					$("#li_admision_admin").css('display','block');
					/*$("#li_reporte_situacion_final").css('display','block');
					$("#li_nota_traslados").css('display','block');
					$("#li_col_situacion_mat").css('display','none');
					$("#li_reporte_unidades_docente").css('display','block');
					$("#li_reporte_temario_general").css('display','block');*/
					$("#li_olimpiadas").css('display','block');
					$("#li_olimpiadas").css('display','block');
					$("#li_oli_result").css('display','block');
					$("#li_oli_calificacion").css('display','block');
					//PROTOTIPO
					$("#li_rrhh").css('display','block');

				}
				if (_usuario.roles.indexOf(_ROL_SECRETARIA)>-1 ){
					//MODULO DE MATRICULA
					$("#li_modulo_matricula").css('display','block');
					$("#li_admision").css('display','block');
						$("#li_registro_familia").css('display','block');
						$("#li_otrogamiento_vacante").css('display','block');
							$("#li_inscripcion_eva").css('display','block');
							$("#li_admi_resultados").css('display','block');
						$("#li_admi_reportes").css('display','block');							
					$("#li_matricula").css('display','block');
						$("#li_mat_reserva").css('display','block');
						$("#li_registro_matricula").css('display','block');
						$("#li_cambio_local").css('display','block');
						$("#li_cambio_aula").css('display','block');
						$("#li_matricula_reportes").css('display','block');
							$("#li_usuarios_alumnos").css('display','block');
							$("#li_padres_codigo_barras").css('display','block');
					$("#li_campus_virtual").css('display','block');
						$("#li_inscripciones").css('display','block');
					$("#li_traslados").css('display','block');
						$("#li_registro_traslados").css('display','block');
						$("#li_reporte_traslados").css('display','block');						
					//MODULO TESORERIA
					$("#li_tesoreria").css('display','block');
							$("#li_caja").css('display','block');
								$("#li_ingresos").css('display','block');
								$("#li_salidas").css('display','block');
								$("#li_caja_reportes").css('display','block');
							$("#li_tesoreria_banco").css('display','block');
								$("#li_banco_reporte").css('display','block');
							$("#li_cartera_morosa").css('display','block');	
					//MODUL ACADEMICO
						$("#li_academico").css('display','block');	
							$("#li_evaluacion").css('display','block');
								$("#li_alumnos_libreta").css('display','block');
								$("#li_evaluacion_reportes").css('display','block');
									$("#li_reporte_entrega_lib").css('display','block');
									$("#li_registros_auxiliares").css('display','block');
							$("#li_asistencia").css('display','block');
							$("#li_asistencia_procesar").css('display','block');
								$("#li_asistencia_reportes").css('display','block');									
									$("#li_asistencia_estadistica").css('display','block');
									$("#li_asistencia_ranqueo").css('display','block');
									$("#li_asistencia_educando").css('display','block');								
					$("#li_padres").css('display','none');
					//$("#li_padres_codigo_barras").css('display','none');
					//$("#li_matricula").css('display','block');
					/*$("#li_matricula_admin").css('display','block');
						$("#li_matricula_conf").css('display','none');
						$("#li_matricula_rep").css('display','block');
							$("#li_num_vacantes").css('display','block');
							$("#li_num_matriculados").css('display','none');*/
					
				/*	$("#li_tesoreria").css('display','block');
						$("#li_fac_banco_exp").css('display','none');
						$("#li_tesoreria_conf").css('display','none');
						$("#li_fac_alumno_descuento").css('display','none');
						$("#li_fac_banco_pagos").css('display','none');
						$("#li_tesoreria_reportes_factura_electronica").css('display','none');
						$("#li_teso_admin_elec").css('display','none');
						$("#li_nota_credito").css('display','none');*/
						
					//$("#li_reportes").css('display','block');
					//$("#li_alumnos").css('display','block'); //entrega de libretas verificar
					/*$("#li_academico").css('display','block');
						$("#li_configuracion").css('display','none');
						$("#li_calendario").css('display','none');
						$("#li_curricula").css('display','none');
						$("#li_calificaciones").css('display','none');
						$("#li_sit_matricula").css('display','none');
						$("#li_condicion_matricula").css('display','none');
						$("#li_reporte").css('display','block');
							$("#li_reporte_tema").css('display','none');
							$("#li_reporte_capacidad").css('display','none');
							$("#li_reporte_notas").css('display','none');
							$("#li_reporte_notas_com").css('display','none');
							$("#li_reporte_entrega_lib").css('display','block');//verificar la la aentre
							$("#li_reporte_nota_aula").css('display','none');
							$("#li_reporte_nota_curso").css('display','none');
							$("#li_reporte_nota_area").css('display','none');
							$("#li_reporte_situacion_final").css('display','none');*/
						
					/*$("#li_asistencia").css('display','block');
						$("#li_asistencia_codigo_barras").css('display','none');
						$("#li_asistencia_codigo_barras_seccion").css('display','none');
						$("#li_asistencia_procesar").css('display','block');
						$("#li_asistencia_conf").css('display','none');
						$("#li_asistencia_reportes").css('display','block');
							$("#li_asistencia_diaria").css('display','none');
							$("#li_estadistica").css('display','block');
							$("#li_ranqueo").css('display','block');
							$("#li_asistencia_educando").css('display','block');
					$("#li_condicion_matricula").css('display','none');*/
					
					$("#li_olimpiadas").css('display','block');
					$("#li_oli_config").css('display','none');
					$("#li_oli_reportes").css('display','none');
					$("#li_oli_result_leer").css('display','none');
					$("#li_oli_result_generar").css('display','none');
					$("#li_oli_tesoreria").css('display','block');
					
					$("#li_oli_reportes").css('display','block');
					// $("#li_oli_reportes_delegaciones").css('display','none');
					// li_oli_reportes_delegaciones
				}
				if (_usuario.roles.indexOf(_ROL_CONTADOR)>-1 ){
					/*$("#li_tesoreria").css('display','block');
						// tesoreria
							$("#li_tesoreria_reportes_caja").css('display','none');
							$("#li_tesoreria_reportes_banco").css('display','none');
							$("#li_tesoreria_reportes_factura_electronica").css('display','block');
							$("#li_tesoreria_reportes_pensiones").css('display','none');
							$("#li_tesoreria_reportes_infocorp").css('display','none');
						// tesoreria
						$("#li_fac_pagos_buscar_alumno").css('display','none');
						$("#li_ingresos").css('display','none');
						$("#li_fac_banco_exp").css('display','none');
						$("#li_tesoreria_conf").css('display','none');
						$("#li_fac_alumno_descuento").css('display','none');
						$("#li_fac_banco_pagos").css('display','none');*/
						
						//MODULO TESORERIA
						$("#li_tesoreria").css('display','block');
							$("#li_caja").css('display','none');
								$("#li_caja_reportes").css('display','none');
							$("#li_tesoreria_banco").css('display','none');
								$("#li_banco_reporte").css('display','none');
							$("#li_teso_admin_electronica").css('display','block');
								$("#li_envio_sunat").css('display','none');
								$("#li_comunicacion_baja").css('display','none');
								$("#li_teso_elect_reportes").css('display','block');
									$("#li_resumen_diario").css('display','none');
									$("#li_tesoreria_reportes_factura_electronica").css('display','block');
							
					//$("#li_reportes").css('display','block');
					/*$("#li_alumnos").css('display','none');

					$("#li_asistencia").css('display','none');*/
					$("#li_padres").css('display','none');
					$("#li_reportes").css('display','none');
					$("#li_seguridad").css('display','none');
					$("#li_condicion_matricula").css('display','none');	
				}
				
				if (_usuario.roles.indexOf(_ROL_ASISTENTE)>-1 ){
					/*$("#li_matricula").css('display','none');
					$("#li_tesoreria").css('display','none');*/
					$("#li_modulo_matricula").css('display','block');
					$("#li_matricula").css('display','block');
						$("#li_admision").css('display','block');
							$("#li_registro_familia").css('display','block');
						$("#li_matricula_reportes").css('display','block');	
							$("#li_reporte_reservas").css('display','none');	
							$("#li_estudiantes_seccion").css('display','none');
							$("#li_no_matriculados").css('display','none');
							$("#li_resumen_genero").css('display','none');
							$("#li_contratos").css('display','none');
							$("#li_padres_codigo_barras").css('display','block');
					//$("#li_academico").css('display','block');
					$("#li_academico").css('display','none');
						$("#li_asistencia").css('display','block');
							$("#li_asistencia_codigo_barras").css('display','block');
							$("#li_asistencia_codigo_barras_seccion").css('display','block');
							$("#li_asistencia_procesar").css('display','block');
							$("#li_asistencia_reportes").css('display','block');
								$("#li_registros_auxiliares").css('display','block');
							$("#li_evaluacion").css('display','block');
								$("#li_evaluacion_reportes").css('display','block');
									$("#li_registros_auxiliares").css('display','block');
									$("#li_reporte_nota_aula").css('display','block');
					
					/*$("#li_reportes").css('display','block');
					$("#li_alumnos").css('display','block');
					$("#li_asistencia").css('display','block');
						$("#li_asistencia_codigo_barras").css('display','block');
						$("#li_asistencia_codigo_barras_seccion").css('display','block');
						$("#li_asistencia_procesar").css('display','none');
						$("#li_asistencia_conf").css('display','none');
						$("#li_asistencia_reportes").css('display','none');*/
					$("#li_padres").css('display','block');
					//$("#li_reportes").css('display','block');
					$("#li_seguridad").css('display','none');
					//$("#li_condicion_matricula").css('display','none');
					//$("#li_olimpiadas").css('display','block');
					$("#li_olimpiadas").css('display','none');
						$("#li_oli_reportes").css('display','block');
						$("#li_oli_tesoreria").css('display','none');
						$("#li_oli_calificacion").css('display','block');
						$("#li_oli_config").css('display','none');
						// $("#li_oli_reportes_consolidado").css('display','none');
				}
				
				if (_usuario.roles.indexOf(_ROL_VIGILANTE)>-1 ){
					$("#li_academico").css('display','block');
						$("#li_asistencia").css('display','block');
							$("#li_asistencia_procesar").css('display','block');	
							//$("#li_asistencia_reportes").css('display','none');
					/*$("#li_alumnos").css('display','none');
					$("#li_asistencia").css('display','block');
						$("#li_asistencia_codigo_barras").css('display','none');
						$("#li_asistencia_codigo_barras_seccion").css('display','none');
						$("#li_asistencia_procesar").css('display','block');
						$("#li_asistencia_conf").css('display','none');
						$("#li_asistencia_reportes").css('display','block');*/
					$("#li_padres").css('display','none');
					//$("#li_reportes").css('display','none');
					$("#li_seguridad").css('display','none');
					$("#li_condicion_matricula").css('display','none');
				}
				
				
				if (_usuario.roles.indexOf(_ROL_TUTOR)>-1 ){

					$("#li_academico").css('display','block');					
						$("#li_asistencia").css('display','block');
							$("#li_asistencia_reportes").css('display','block');
								$("#li_asistencia_diaria").css('display','block');
								$("#li_registro_asistencia_mensual").css('display','block');
							$("#li_conductual").css('display','block');
								$("#li_nota_comportamiento").css('display','block');
								$("#li_condicion_cond").css('display','none');
								$("#li_busqueda_incidencia").css('display','block');
								$("#li_conductual_reportes").css('display','block');
									$("#li_reporte_notas_com").css('display','block');
					$("#li_modulo_matricula").css('display','block');
					$("#li_matricula").css('display','block');
								$("#li_admision").css('display','block');
									$("#li_registro_familia").css('display','block');
								$("#li_matricula_reportes").css('display','block');	
										$("#li_reporte_reservas").css('display','none');	
										$("#li_estudiantes_seccion").css('display','none');
										$("#li_no_matriculados").css('display','none');
										$("#li_resumen_genero").css('display','none');
										$("#li_contratos").css('display','none');
										$("#li_padres_codigo_barras").css('display','block');
								$("#li_campus_virtual").css('display','block');
										$("#li_inscripciones").css('display','block');
				}
				
				if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 ){
					//$("#li_reportes").css('display','none');
					$("#li_academico").css('display','block');
						$("#li_asistencia").css('display','block');
							$("#li_asistencia_procesar").css('display','block');
							$("#li_asistencia_reportes").css('display','block');
								$("#li_asistencia_estadistica").css('display','block');
								$("#li_asistencia_ranqueo").css('display','block');
								$("#li_asistencia_educando").css('display','block');
							$("#li_conductual").css('display','block');
								$("#li_nota_comportamiento").css('display','none');
								$("#li_condicion_cond").css('display','block');
								$("#li_busqueda_incidencia").css('display','none');
								$("#li_conductual_reportes").css('display','none');
									$("#li_reporte_notas_com").css('display','none');
						
				/*			$("#li_asistencia_diaria").css('display','none');
							$("#li_estadistica").css('display','block');
							$("#li_ranqueo").css('display','block');
							$("#li_asistencia_educando").css('display','block');
				$("#li_academico").css('display','block');
						$("#li_configuracion").css('display','none');
						$("#li_calendario").css('display','none');
						$("#li_curricula").css('display','none');
						$("#li_calificaciones").css('display','none');
							$("#li_evaluaciones").css('display','none');
							$("#li_ingreso_notas").css('display','none');
							$("#li_promedio_notas").css('display','none');
							$("#li_nota_comportamiento").css('display','none');
							$("#li_exoneraciones").css('display','none');
							$("#li_semana_sesiones").css('display','none');
				$("#li_sit_matricula").css('display','none');
				$("#li_reporte").css('display','none');
				$("#li_matricula").css('display','none');
				// $("#li_condicion_matricula").css('display','none');
				$("#li_condicion_matricula").css('display','block');
					$("#li_condicion_eco").css('display','none');
					$("#li_condicion_cond").css('display','block');*/
				}
				
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
					
					// $("#li_nota").css('display','block');
					// $("#li_agenda").css('display','block');
					$("#li_academico").css('display','block');
						$("#li_planificacion").css('display','block');
							$("#li_planificacion_reportes").css('display','block');
								$("#li_reporte_programacion_anual").css('display','block');
								$("#li_reporte_unidades").css('display','block');
						$("#li_evaluacion").css('display','block');
							$("#li_semana_sesiones").css('display','block');
							$("#li_ingreso_notas").css('display','block');
							$("#li_evaluacion_reportes").css('display','block');
								$("#li_reporte_consolidado_docente").css('display','block');
								
						/*$("#li_curricula").css('display','none');
						$("#li_calificaciones").css('display','block');// aqui
							$("#li_evaluaciones").css('display','block');
							$("#li_semana_sesiones").css('display','block');
							$("#li_ingreso_notas").css('display','block');
							$("#li_nota_comportamiento").css('display','none');
							$("#li_exoneraciones").css('display','none');
							$("#li_promedio_notas").css('display','none');
						$("#li_sit_matricula").css('display','none');
						$("#li_reporte").css('display','block');
							$("#li_reporte_tema").css('display','none');
							$("#li_reporte_capacidad").css('display','none');
							$("#li_reporte_notas").css('display','block');
							$("#li_reporte_notas_com").css('display','none');
							$("#li_reporte_entrega_lib").css('display','none');
							$("#li_reporte_nota_aula").css('display','none');
							$("#li_reporte_nota_curso").css('display','none');
							$("#li_reporte_nota_area").css('display','none');
							$("#li_reporte_programacion_anual").css('display','block');
							$("#li_reporte_unidades").css('display','block');

							
						$("#li_matricula").css('display','none');
						$("#li_condicion_matricula").css('display','none');
							$("#li_condicion_eco").css('display','none');
							$("#li_condicion_cond").css('display','none');*/
				}

				if (_usuario.roles.indexOf(_ROL_COORDINADOR_AREA)>-1 ){
					$("#li_academico").css('display','block');
						$("#li_planificacion").css('display','block');
							$("#li_subtema_grado").css('display','block');
							$("#li_desempenios").css('display','block');
							$("#li_unidades").css('display','block');
							$("#li_sesiones").css('display','block');
							$("#li_planificacion_reportes").css('display','block');
								$("#li_reporte_unidades").css('display','block');
								$("#li_reporte_programacion_anual").css('display','block');								
						$("#li_evaluacion").css('display','block');
							$("#li_semana_sesiones").css('display','block');
							$("#li_ingreso_notas").css('display','block');
							$("#li_evaluacion_reportes").css('display','block');
								$("#li_reporte_consolidado_docente").css('display','block');
					/*$("#li_academico").css('display','block');
						$("#li_configuracion").css('display','none');
						$("#li_calendario").css('display','none');
						$("#li_curricula").css('display','block');
							$("#li_area_anio").css('display','none');
							$("#li_curso_anio").css('display','none');
							$("#li_area_coordinador").css('display','none');
							$("#li_curso_coordinador").css('display','none');
							$("#li_subtema_capacidad").css('display','block');
							$("#li_curso_unidad").css('display','block');
							$("#li_indicador").css('display','none');
							$("#li_curso_unidad").css('display','block');
							$("#li_unidad_tema").css('display','none');
							$("#li_unidad_capacidad").css('display','none');
							$("#li_unidad_sesion").css('display','block');
							$("#li_sesion_indicador").css('display','none');
							$("#li_sesion_tema").css('display','none');
							$("#li_sesion_actividad").css('display','none');
							$("#li_tutor_aula").css('display','none');
							$("#li_curso_aula").css('display','none');
							$("#li_curso_horario").css('display','none');
					  $("#li_calificaciones").css('display','block');// aqui
							$("#li_evaluaciones").css('display','block');
							$("#li_ingreso_notas").css('display','block');
							$("#li_nota_comportamiento").css('display','none');
							$("#li_exoneraciones").css('display','none');
							$("#li_promedio_notas").css('display','none');
					 $("#li_sit_matricula").css('display','none');

					 $("#li_reporte").css('display','block');
							$("#li_reporte_tema").css('display','none');
							$("#li_reporte_capacidad").css('display','none');
							$("#li_reporte_notas").css('display','block');
							$("#li_reporte_notas_com").css('display','none');
							$("#li_reporte_entrega_lib").css('display','none');
							$("#li_reporte_nota_aula").css('display','none');
							$("#li_reporte_nota_curso").css('display','none');
							$("#li_reporte_nota_area").css('display','none');
							$("#li_reporte_situacion_final").css('display','none');
							$("#li_reporte_programacion_anual").css('display','block');
							$("#li_reporte_unidades").css('display','block');

						$("#li_matricula").css('display','none');
						$("#li_condicion_matricula").css('display','none');
							$("#li_condicion_eco").css('display','none');
							$("#li_condicion_cond").css('display','none');*/
							
				}
				

				if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 || _usuario.roles.indexOf(_ROL_PSICOLOGO)>-1){
					$("#li_academico").css('display','block');						
					$("#li_evaluacion").css('display','block');
						$("#li_evaluacion_reportes").css('display','block');
							$("#li_reporte_nota_aula").css('display','block');
							
					/*$("#li_academico").css('display','block');
						$("#li_configuracion").css('display','none');
						$("#li_calendario").css('display','none');
						$("#li_curricula").css('display','none');
						$("#li_calificaciones").css('display','none');
						$("#li_sit_matricula").css('display','none');
						$("#li_condicion_matricula").css('display','none');
						$("#li_reporte").css('display','block');
							$("#li_reporte_tema").css('display','none');
							$("#li_reporte_capacidad").css('display','none');
							$("#li_reporte_notas").css('display','none');
							$("#li_reporte_notas_com").css('display','none');
							$("#li_reporte_entrega_lib").css('display','none');
							$("#li_reporte_tema").css('display','none');
							$("#li_reporte_capacidad").css('display','none');
							$("#li_reporte_notas").css('display','none');
							$("#li_reporte_notas_com").css('display','none');
							$("#li_reporte_nota_curso").css('display','none');
							$("#li_reporte_nota_aula").css('display','block');
							$("#li_reporte_nota_area").css('display','none');
							$("#li_reporte_situacion_final").css('display','none');
							$("#li_reporte_programacion_anual").css('display','none');
							$("#li_reporte_unidades").css('display','none');*/

							
							/*
							 * $("#li_anio").css('display','none');
							 * $("#li_periodo").css('display','none');
							 * $("#li_academico_grad").css('display','none');
							 * $("#li_academico_area").css('display','none');
							 * $("#li_academico_curso").css('display','block');
							 */
				}
				
				// ROLES PARA OLIMPIADA
				if (_usuario.roles.indexOf(_ROL_SECRETARIA_OLI)>-1 ){

					$("#li_admision_admin").css('display','none');
					$("#li_matricula").css('display','none');
					$("#li_padres").css('display','none');
					$("#li_padres_codigo_barras").css('display','none');
					$("#li_matricula").css('display','none');
					$("#li_matricula_admin").css('display','none');					
					$("#li_tesoreria").css('display','none');
					$("#li_reportes").css('display','none');
					$("#li_alumnos").css('display','none');
					$("#li_academico").css('display','none');
					$("#li_asistencia").css('display','none');
					$("#li_condicion_matricula").css('display','none');	
					$("#li_olimpiadas").css('display','block');
						$("#li_oli_config").css('display','none');
						$("#li_oli_reportes").css('display','none');
						$("#li_oli_result_leer").css('display','none');
						$("#li_oli_result_generar").css('display','none');
						$("#li_oli_tesoreria").css('display','block');
						$("#li_oli_reportes").css('display','block');
							$("#li_oli_reportes_inscritos").css('display','block');
							$("#li_oli_reportes_delegaciones").css('display','block');
							$("#li_oli_reportes_asistencia").css('display','block');
							$("#li_oli_reportes_consolidado").css('display','none');
					// $("#li_oli_reportes_delegaciones").css('display','none');
					// li_oli_reportes_delegaciones
				}
				
				if (_usuario.roles.indexOf(_ROL_ADMIN_OLI)>-1 ){
					
					$("#li_admision_admin").css('display','none');
					$("#li_matricula").css('display','none');
					$("#li_padres").css('display','none');
					$("#li_padres_codigo_barras").css('display','none');
					$("#li_matricula").css('display','none');
					$("#li_matricula_admin").css('display','none');					
					$("#li_tesoreria").css('display','none');
					$("#li_reportes").css('display','none');
					$("#li_alumnos").css('display','none');
					$("#li_academico").css('display','none');
					$("#li_asistencia").css('display','none');
					$("#li_condicion_matricula").css('display','none');						
					$("#li_olimpiadas").css('display','block');
						$("#li_oli_config").css('display','none');
						$("#li_oli_reportes").css('display','block');
						$("#li_oli_result_leer").css('display','block');
						$("#li_oli_result_generar").css('display','block');
						$("#li_oli_tesoreria").css('display','none');
						$("#li_oli_reportes").css('display','block');
							$("#li_oli_reportes_inscritos").css('display','block');
							$("#li_oli_reportes_delegaciones").css('display','block');
							$("#li_oli_reportes_asistencia").css('display','block');
							$("#li_oli_reportes_consolidado").css('display','none');
					// $("#li_oli_reportes_delegaciones").css('display','none');
					// li_oli_reportes_delegaciones
				}

				
			}else if(_usuario.id_per==_PERFIL_FAMILIAR){
				$('.content-body').html('');
//				$('.sidebar-main').hide();


				//_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
				
				_send('pages/apoderado/apo_modulos.html');
				
				if (ini==null){

				//funcion q se ejecuta al cargar el modal
					var onShowModal = function(){
						
						_inputs('frm-info_campus_virtual');
						
					}

					//funcion q se ejecuta despues de grabar
					var onSuccessSave = function(data){
						
					}
					
					//Abrir el modal
					var titulo='BIENVENIDOS AL PROCESO DE INSCRIPCIÓN AULA VIRTUAL';
					
					_modal_full(titulo,'pages/campus_virtual/cv_info_modal.html',onShowModal,onSuccessSave);
				}

			}
			
			
						
	//	}



		
	}
	_get('api/seguridad/usuario/session',fncExitoSESSION);
	// FIN ONLOAD
	
	$("#logout").on('click',function() {
		
		_get('api/seguridad/usuario/logout',
			function(data){
				document.location.href = "index.html";
			}
		);
		
	});
	
	function _attachSubmitFuntion(idForm, $btn, _fncPre,_fncPost){
		
		var	url = $('#' + idForm).attr('action');
		
		$('#' + idForm).on('submit',function(event){
			event.preventDefault();
			if($('#' + idForm).valid()){

				var valor = _fncPre();
				if (valor==true ){
					return _fncPost();
				}else{
					
					return false;
				}
			}
			
		});
		
	}
	
});

function inicializar_wizard(){
	alert();
	// Initialize wizard
	$(".steps-validation").steps({
			headerTag : "h6",
			bodyTag : "fieldset",
			transitionEffect : "fade",
			titleTemplate : '<span class="number">#index#</span> #title#',
			autoFocus : true,
			onStepChanging : function(event, currentIndex, newIndex) {
				var form = $("#alu_familiar-frm").show();
			//alert(newIndex + "z" + _ini);
				
				//alert(newIndex);	
				
				if (currentIndex > newIndex) {
					return true;
				}
				
				//alert(newIndex +"," +_ini);

				if (currentIndex < newIndex) {

					if (currentIndex == 0) {
						// alert($("#alu_familiar-frm").data('changed'));
						/*if (_ini == _ESTADO_ACTUALIZADO ||  (_ini==_ESTADO_CELULAR ) || !$("#alu_familiar-frm").data('changed')) {
							//alert('cambio!!');
							// YA TIENE SUS DATOS VALIDADOS EN BD
							return true;
						}*/

						/*form = $("#alu_familiar-frm").show();
						form.validate().settings.ignore = ":disabled,:hidden";
						var isValid = form.valid();

						if (isValid) {
							form.find(".body:eq(" + newIndex+ ") label.error").remove();
							form.find(".body:eq(" + newIndex + ") .error").removeClass("error");

							// actualizar datos
							familiar_actualizar_datos();
						}*/
						
						//return false;
					}

					if (newIndex == 2){
					/*	if ( _ini== _ESTADO_CELULAR || _ini== _ESTADO_CAMBIO_CLAVE )  {
							return true;
						}else{
							_alert_error('Validar el celular es obligatorio.');
							return false;
						}
						*/
					}else{
						
					}

					
				}
 
				
				// Needed in some cases if the user went back (clean
				// up)
/*							
							if (currentIndex < newIndex) {
								alert('mnarcabndi no eror');
								// To remove error styles
								form.find(".body:eq(" + newIndex+ ") label.error").remove();
								form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
							}
*/
				//form.validate().settings.ignore = ":disabled,:hidden";
				//return form.valid();
			},

			onStepChanged : function(event, currentIndex, priorIndex) {

				$('#idPasos').html('Paso ' + (currentIndex + 1) + ' de 2');

				// Used to skip the "Warning" step if the user is
				// old enough.
				if (currentIndex === 2
						&& Number($("#age-2").val()) >= 18) {
					form.steps("next");
				}

				// Used to skip the "Warning" step if the user is
				// old enough and wants to the previous step.
				if (currentIndex === 2 && priorIndex === 3) {
					form.steps("previous");
				}
				

			},

			onFinishing : function(event, currentIndex) {
 /*
					$('#frm-change #id').val(_usuario.id);
					$('#frm-change #id_per').val(_usuario.id_per);
					
					var isValid = $('#frm-change').valid();
					if (isValid) {
						_post('api/seguridad/change',$('#frm-change').serialize(), function(data){
							
							document.location.href = 'dashboard.html';
						});
						
					}
	*/
				/*if( _ini== _ESTADO_CELULAR)
					document.location.href="dashboard.html=t=" + new Date();
				else
					_alert_error('El número celular es obligatorio.');
					return false;*/
				
			},

			onFinished : function(event, currentIndex) {
				//alert("Submitted!");
			}
		});

	
}


/**
 * Evento del año de la barra superiro del dashboard
 * 
 * @param objetoList
 * @returns
 */
function _onclickAnio(obj){
	var id_anio = $(obj).data('id');
	var anio = $(obj).text();
	$("#_ID_ANIO").text(anio.trim());
	$("#_id_anio").text(id_anio);
	
	_onchangeAnio(id_anio, anio);
}

function _onchangeAnio(id_anio, anio){
	console.log(id_anio);
	console.log(anio);
}

function _registrosAuxiliares(){
	var url = _URL + 'api/reporte/registro_auxiliares?id_anio=' + $('#_id_anio').text()+'&id_suc='+ $("#_id_suc").text();
 	document.location.href = url;	
} 

function _descarga_oli(){
	_download(_URL_OLI  + 'api/resultados/formato','FORMATO_CARGA_LECTORA.xls');
}

function _crearGruposClassroom(){
	var url = _URL + 'api/crearGruposClassroom/' + $('#_id_anio').text();
 	document.location.href = url;	
} 

function _actualizarIdGoogle(){
	var url = _URL + 'api/actualizarIdGoogle';
 	document.location.href = url;	
} 

function _enrolFull(){
	var url = _URL + 'api/enrol/'+ $('#_id_anio').text();
 	document.location.href = url;	
} 
/*function validarSessionToken(){
	setInterval(myMethod, 60000);

	function myMethod( )
	{
	  _GET({url:'api/seguridad/validarSessionToken',
		  silent :true,
		  success: function(data){
			  
			  console.log(data);
			  
			  if (data.estado=='P' && $('#_seconds').length==0){
				  
				  var plazoCierreSession = setInterval(actualizarSegundos, 1000);
			        var s = 60;
			        function actualizarSegundos( ){
			        	s = s-1;
			        	console.log(s);
			        	$('#_seconds').html(s);
			        	if(s==0){
		        		    document.cookie = '_token=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			        		document.location.href ='index.html';
			        	}
			        }
			        
				  	console.log("MENSAJE SWAL!!!");
				  
				        swal({
				        	 title: '<b>Desea continuar?</b>',
				        	 text: "El sistema está a punto de cerrar sesión en <font id='_seconds' color='red'>60</font> segundos",
				            type: "warning",
				            html: true,
				            showCancelButton: true,
				            confirmButtonColor: "#4caf4f",
				            cancelButtonColor: "red",
				            confirmButtonText: "Si, Continuar trabajando.",
				            cancelButtonText: "No, Salir del sistema.",
				            closeOnConfirm: false,
				            closeOnCancel: false
				        },
				        function(isConfirm){
				            if (isConfirm) {
				                
				            	_POST({url:'api/seguridad/reconectar', silent:true,success(data){
				            		var token = data.result;
				            		console.log(token);
				            		document.cookie = '_token=' + token;
				            		$.ajaxSetup({
				            		    headers: { 'Authorization': token}
				            		});
				            		clearInterval(plazoCierreSession);
				            		swal.close();
				            	}});
				            }
				            else {
				            	
				            	// alert('salir');
				            	/*
								 * swal({ title: "Adios!", text: "Saliendo del
								 * sistema...", confirmButtonColor: "#66BB6A",
								 * type: "success" });
								 */
/*			        		    document.cookie = '_token=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';

				            	document.location.href ='index.html';
				            	
				            }
				        });
				        // btn bg-teal btn-sm
				        // $('.sa-icon sa-warning pulseWarning
						// .cancel').addClass('btn btn-danger btn-sm');
				        /*
						 * $('.sa-icon sa-warning pulseWarning
						 * .cancel').removeClass('cancel'); $('.sa-icon
						 * sa-warning pulseWarning .cancel').addClass('btn
						 * btn-danger');
						 * 
						 * $('.sa-confirm-button-container
						 * .confirm').removeClass('confirm');
						 * $('.sa-confirm-button-container
						 * .confirm').addClass('btn btn-warning');
						 */
				        // alert();
			        
				       
/*			  }
		  }
	  	});
	}

}*/

function validarSessionToken(){
	setInterval(myMethod, 60000);

	function myMethod( )
	{
	  _GET({url:'api/seguridad/validarSessionToken',
		  silent :true,
		  success: function(data){
			  
			  console.log(data);
			  
			  if (data.estado=='P' && $('#_seconds').length==0){
				  
				  var plazoCierreSession = setInterval(actualizarSegundos, 1000);
			        var s = 60;
			        function actualizarSegundos( ){
			        	s = s-1;
			        	console.log(s);
			        	$('#_seconds').html(s);
			        	if(s==0){
		        		    document.cookie = '_token=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			        		document.location.href ='index.html';
			        	}
			        }
			        
				  	/*console.log("MENSAJE SWAL!!!");
				  
				        swal({
				        	 title: '<b>Desea continuar?</b>',
				        	 text: "El sistema está a punto de cerrar sesión en <font id='_seconds' color='red'>60</font> segundos",
				            type: "warning",
				            html: true,
				            showCancelButton: true,
				            confirmButtonColor: "#4caf4f",
				            cancelButtonColor: "red",
				            confirmButtonText: "Si, Continuar trabajando.",
				            cancelButtonText: "No, Salir del sistema.",
				            closeOnConfirm: false,
				            closeOnCancel: false
				        },
				        function(isConfirm){
				            if (isConfirm) {
				                
				            	_POST({url:'api/seguridad/reconectar', silent:true,success(data){
				            		var token = data.result;
				            		console.log(token);
				            		document.cookie = '_token=' + token;
				            		$.ajaxSetup({
				            		    headers: { 'Authorization': token}
				            		});
				            		clearInterval(plazoCierreSession);
				            		swal.close();
				            	}});
				            }
				            else {
				            	
				            	// alert('salir');
				            	/*
								 * swal({ title: "Adios!", text: "Saliendo del
								 * sistema...", confirmButtonColor: "#66BB6A",
								 * type: "success" });
								 */
			        	/*	    document.cookie = '_token=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';

				            	document.location.href ='index.html';
				            	
				            }
				        });*/
				        // btn bg-teal btn-sm
				        // $('.sa-icon sa-warning pulseWarning
						// .cancel').addClass('btn btn-danger btn-sm');
				        /*
						 * $('.sa-icon sa-warning pulseWarning
						 * .cancel').removeClass('cancel'); $('.sa-icon
						 * sa-warning pulseWarning .cancel').addClass('btn
						 * btn-danger');
						 * 
						 * $('.sa-confirm-button-container
						 * .confirm').removeClass('confirm');
						 * $('.sa-confirm-button-container
						 * .confirm').addClass('btn btn-warning');
						 */
				        // alert();
			        
				       
			  }
		  }
	  	});
	}

}