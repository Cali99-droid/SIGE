function onloadPage(params){
	_onloadPage();	
	
	_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){
		$('#id_niv').change();
	});	

	
	$('#id_niv').on('change',function(){
		var id_niv = $(this).val();
		if(id_niv!=''){
			//llena periodos
			var param = {id_niv: id_niv, id_anio: $('#_id_anio').text()};
			_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null,param,function(){$('#id_cpu').change()});
			
			//llena grados
			var param ={id_niv:$(this).val()};
			_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
		}
	});
	

	 $('#id_gra').on('change',function(event) {
		 if($('#id_gra').val()!='' &&  $('#id_cpu').val()!=''){
			//llena unidades
				var param = {
						id_niv : $('#id_niv').val(),
						id_gra : $('#id_gra').val(),
						id_cpu : $('#id_cpu').val()
					};
					_llenarComboURL('api/cursoUnidad/listarUnidadesxGrado', $('#num'),null, param, function() {
							//unidad_sesion_listar_tabla($('#num').val());
						});
		 }
	});
	
	 $('#id_cpu').on('change',function(event) {
		 if($('#id_gra').val()!='' &&  $('#id_cpu').val()!=''){
			//llena unidades
				var param = {
						id_niv : $('#id_niv').val(),
						id_gra : $('#id_gra').val(),
						id_cpu : $('#id_cpu').val()
					};
					_llenarComboURL('api/cursoUnidad/listarUnidadesxGrado', $('#num'),null, param, function() {
							//unidad_sesion_listar_tabla($('#num').val());
						});
		 }
	});
	
	
	$('#frm-reporte-unidad').on('submit', function (event){
		event.preventDefault();
		
		return _get('api/cursoUnidad/unidadesDocente',
				function (data){
					console.log(data);
					var cursos = data.cursos;
					var docentes = data.docentes;
	
					if(cursos.length>0  && docentes.length>0 )
						$('#btn-exportar').css('display','inline-block');
					else
						$('#btn-exportar').css('display','none');
					
					$('#col_reporte_unidad').find('tbody').html("");
					
					//titulos
					var th = '<tr><th>Nro</th><th>Profesor</th>';
					for(var curso in cursos){
						var nom = cursos[curso].nom;
						th = th + '<th class="rotate"><p>' + nom + '</p></th>';
					}
					th = th + '</tr>'; 
					$('#col_reporte_unidad').find('thead').html(th);
					
					//cuerpo
					for(var i in docentes){
						
						var docente = docentes[i];
						var curso_unidades = docente.curso_unidades; 
						console.log(docente);
						var docente_id_cur = docente.id_cur;
						var tr = '<tr><td>' + (parseInt(i)+1) + '</td><td nowrap>' + docente.docente + '</td>';
						for(var j in cursos){
							tr += '<td class="text-center">';
							var curso =cursos[j];
							var id_cur = curso.id;
							
							//revisar si tiene curso
							var curso_docente = null;
							for(var k in curso_unidades){
								var id_curso_docente =curso_unidades[k].id_cur;
								if (id_curso_docente== id_cur)
									curso_docente = curso_unidades[k];
							}
							
							if(curso_docente!=null){
								console.log(curso_docente);
								if (curso_docente.id_ccuc!=null){
									tr += '<a target="_blank" href="' + _URL + 'api/cursoUnidad/pdf/' + curso_docente.id_ccuc + '"><i class="fa fa-file-pdf-o position-right"></i></a>';
								}else
									tr += '<font color="red">F</font>';	
							}else
								tr += '&nbsp;';
							
							tr += '</td>';
						}
						tr += '</tr>';
						$('#col_reporte_unidad').find('tbody').append(tr);
					}

				
				}
				, $(this).serialize()  + "&id_anio=" + $('#_id_anio').text());

		
	});
	

	$('#btn-exportar').on('click',function(event){
	
		var url = _URL + 'api/cursoUnidad/xls?id_niv=' + $('#id_niv').val()  + "&id_gra=" + $('#id_gra').val() +"&id_cpu=" +$('#id_cpu').val() +"&num=" + $('#num').val()+"&id_anio="+ $('#_id_anio').text();
	    window.open( url, '_blank');

	});
}
 
/* 
 * Genera pdf Unidad de aprendizaje
 * */
function generar_pdf(obj, e){
	e.preventDefault();
	var id = $(obj).data('id');
    
    var param = {id_uni:id};
	_GET({  url:'api/cursoUnidad/validarSesionesCompletasxUnidad',
		    params:param,
			success:function(data){
				var param1={id_uni:id, id_tra:_usuario.id_tra, id_anio: $('#_id_anio').text()};
				_GET({  url:'api/cursoUnidad/validarSesionesVinculadasxUnidad',
				    params:param1,
					success:function(data){
						console.log(data);
						var url = _URL + 'api/cursoUnidad/unidadAprendizajePDF/' + $('#_id_anio').text() + '/' + _usuario.id_tra + '/' + id;
					    window.open( url, '_blank');
						
					},
					error:function(data){
						_alert_error(data.msg);
					}
			     });
			},
			error:function(data){
				_alert_error(data.msg);
			}
	     });
    
   
}