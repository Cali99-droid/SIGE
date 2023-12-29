function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	
	/*if (rol=='1')
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	else
		_llenarCombo('ges_servicio',$('#id_nvl'),null,_usuario.id_suc,function(){$("#id_nvl").change()});*/

		//_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
 
	//_llenarCombo('cat_nivel',$('#id_niv'));
	
	/* comentado 2021 $('#id_gra').on('change',function(event){

		if (rol=='1'){
			var param={id_gra:$(this).val(),id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
			//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
		}else{
			var param={id_suc:_usuario.id_suc ,id_gra:$(this).val(),id_anio:$("#_id_anio").text(),id_gir:$('#id_gir').val()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
			//_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+_usuario.id_suc);
		}	
			

		
	}); */
	
	/*$('#id_grad').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	*/
	
	/*$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});*/
	/*$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});*/
	$('#id_nvl').on('change',function(event){
		var id_niv=$('#id_nvl').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		//var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val()};
		//_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	
	$('#id_gir').on('change',function(event){
		if (rol=='1'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
			_llenarComboURL('api/encuesta/listarEncuestaxGiro',$('#id_enc'),null,param,function(){$('#id_enc').change()});

	}); 
		
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});

	$('#resp_letras').on('click',function(){
	   if(this.checked){
		 $('#resp_letras').val(1);
	   } else {
		 $('#resp_letras').val(0);
	   }	   
	});
	
}



$(function(){
	_onloadPage();	

	
	
});

function listar_tabla(){
	var param = {id_enc:$('#id_enc').val(),id_niv:$('#id_nvl').val(),id_gra:$('#id_gra').val(), id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), res:$('#res').val(), resp_letras: $('#resp_letras').val()};
	
	//columna dinamica
	
	_get('api/encuesta/listarRespuestasxEncuesta/',
			function(data){
			/*if($('#id_cur').val()=="T"){
				$('#btn-agregar').hide();
			} else {
				$('#btn-agregar').show();
			}	*/
//este es, se qued
		//__preguntas =data.preguntas;
		var preguntas =data.preguntas;
		//__indicadores =data.indicadores;
		var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					//	{"title": "DNI","data": "nro_doc"},
						{"title": "Alumno","data": "alumno"},
						{"title": "Local Actual","data": "local"},
						{"title": "Nivel Siguiente","data": "nivel_sig"},	
						{"title": "Grado Siguiente","data": "grado_sig"},
						{"title": "Cel. Apoderado ","data": "cel"}];
						//{"title": "Sub-tema","data": "nom"}];
		var i=0;
		
		//limpiamos la tabla
		$('#not_reporte_encuesta-tabla').find('thead').html('');
		$('#not_reporte_encuesta-tabla').find('tbody').html('');

		for (var key in data.preguntas) {
			i++;					
			var titulo = data.preguntas[key];
			var id_pre = titulo.id;
			
			var element = {"title": titulo.pre,"data": "pre" + titulo.id,
					"render": function ( data, type, row, meta ) {
						
						}
				
					};
			
			columns.push(element);
			
		}
		var cont=0;
		var alumnos = data.alumnos;
		//var competencias = data.competencias;
		//var capacidades = data.capacidades;
		//var desempenios = data.desempenios;
		//var cursos = data.cursos;
		
		

		//INICIO - header de la tabla
		//COMPETENCIAS
		var td = '';
		
		for (var key in preguntas) {
		
		td = td + '<th  bgcolor="#AED6F1" title="'+preguntas[key].pre_com+'">' + preguntas[key].pre  + '</th>';
		//td =  td + '<th >PROM.</th>';
		}
		
		
		//if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
			$('#not_reporte_encuesta-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th><th bgcolor="#AED6F1">Local Actual</th><th bgcolor="#AED6F1">Nivel Siguiente</th><th bgcolor="#AED6F1">Grado Siguiente</th><th bgcolor="#AED6F1">Cel. Apoderado</th>' + td  + '<tr>');
		//} else {
		//	$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<th bgcolor="#aac3e4">Promedio</th><tr>');
		//}	

		//INICIO - header de la tabla
		//CAPACIDADES
		

		//_get('api/nota/obtenerConfiguracionArea',function(data){
			//console.log(data);
			 
			 		var first=true;
		for (var key in alumnos) {
			//alert();
			cont++;
			var id_alu = key;
			var respuestas = alumnos[id_alu].respuesta;
			//var notas = alumnos[id_alu].nota;
			//var id_alumno=alumnos[id_alu].id_alu;
			//var competenciaSuma =alumnos[id_alu].competenciaSuma;
			//var notasCur = alumnos[id_alu].notasCursos;
			var tdsNotas= "";
			var tdsPromComp="";
			/*if($('#id_cur').val()=="T"){
			//	for (var y in cursos) {
				//if(id_com==cursos[y].id_com){
				//	cont_cur++;
				//}	
			//}
				for (var keyNotas in notasCur) {
				//	var id_com = notas[keyNotas].id_com;
				//	var id_cap = notas[keyNotas].id_cap;
					//Verifico si el ingreso de Notas para este aula, area y/o curso en cualitativo o cuantitativo
					/*var param={id_anio:$('#_id_anio').text() , id_gra:$('#_id_anio').text(), id_adc:$('#id_area option:selected').attr("data-aux1")};
					_get('api/nota/obtenerConfiguracionArea',function(data){*/
					//	console.log(data);
					//	var tip_cali=data.tip_pro_per;
					//	var tip_prom_per=data.tip_pro_per;
					//	var tip_prom_anu=data.tip_pro_anu;
					//if(cursos[y].id_cua==notasCur[keyNotas].id_cua && cursos[y].id_com==notasCur[keyNotas].id_com){
				/*		var value_nota_db=notasCur[keyNotas].nota;
						var nota_value="";
						console.log(value_nota_db);
						var color="";
						if(tip_cali=="CUALI"){
							//alert();
							if(value_nota_db!=''){
								if(value_nota_db>3.4){
									  nota_value='AD';
									  color="blue";
								}  else if(value_nota_db>2.4 && value_nota_db<=3.4){
									  nota_value = 'A';
									   color="blue";
								}  else if(value_nota_db>1.4 && value_nota_db<=2.4){
									  nota_value = 'B';
									   color="green";
								}  else if(value_nota_db<=1.4){
									  nota_value = 'C';
									  color="red";
								}  else{
									  nota_value ='';
								}	  
							}
							tdsNotas = tdsNotas  + "<td><label style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' class='form-control'>"+nota_value+"</label></td>";
						} else if(tip_cali=="CUANTI"){
							if(value_nota_db!='' && value_nota_db!=null){
								if(value_nota_db<10){
								nota_value="0"+Math.round(value_nota_db);
								color="red";
								}else{
									nota_value=Math.round(value_nota_db);
									 color="blue";
								}	
							}	
							tdsNotas = tdsNotas  + "<td><label style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' class='form-control'>"+nota_value+"</label></td>";
						}	
					//}		 
				}
				//}	
			} else {*/
			for (var y in preguntas) {
				for (var keyRespuestas in respuestas) {
					
					var id_pre = respuestas[keyRespuestas].id_enc_pre;
					var res_alu ="";
					if(respuestas[keyRespuestas].alt!=null){
						res_alu=respuestas[keyRespuestas].alt;
					}	
					//var id_cap = notas[keyNotas].id_cap;
					//Verifico si el ingreso de Notas para este aula, area y/o curso en cualitativo o cuantitativo
					/*var param={id_anio:$('#_id_anio').text() , id_gra:$('#_id_anio').text(), id_adc:$('#id_area option:selected').attr("data-aux1")};
					_get('api/nota/obtenerConfiguracionArea',function(data){*/
					//	console.log(data);
					//	var tip_cali=data.tip_pro_per;
					//	var tip_prom_per=data.tip_pro_per;
					//	var tip_prom_anu=data.tip_pro_anu;
					if(preguntas[y].id==id_pre){
						tdsNotas = tdsNotas  + "<td style='font-size: 9pt; font-weight: bold; color: black; text-align: center;'>"+res_alu+"</td>";
					}	
						
						 
				}
				}	
			/*}	*/
			$('#not_reporte_encuesta-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td><td>'+alumnos[id_alu].local+'</td><td>'+alumnos[id_alu].nivel_sig+'</td><td>'+alumnos[id_alu].grado_sig+'</td><td>'+alumnos[id_alu].cel+'</td>'  + tdsNotas + '</tr>');
			/*if( Math.round(alumnos[id_alu].promedioGeneral)<10){
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '</tr>');
				} else {
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
				}	
			}else{	
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '</tr>');
				} else {
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
				}	
			}*/
			/*if(true){
				//first=false;
				//console.log(competenciaSuma);
				
				var columnas=0;
				for (var key in competenciaSuma) {
					//aca las 3 competencias
					//obtener el nro de notas q tiene la competencia
					var num_notas = 0;
					var suma_notas = Math.round(competenciaSuma[key]);
					if($('#id_cur').val()=="T"){
						for (var key1 in cursos) {
						if (cursos[key1].id_com==key)
							num_notas = num_notas+1;
						
						}
					} else {
						for (var key1 in competencias) {
						if (competencias[key1].com_id==key)
							num_notas = competencias[key1].count;
						
						}
					}	
					
					columnas = columnas + num_notas+1;
					var nota_com="";
					var color="";
					console.log(tip_cali);
					console.log(columnas);
					console.log(columnas);
					if(tip_cali=="CUALI"){
						if(suma_notas!=''){
							if(suma_notas=='4'){
								  nota_com='AD';
								  color="blue";
							}  else if(suma_notas=='3'){
								  nota_com = 'A';
								  color="blue";
							}  else if(suma_notas=='2'){
								  nota_com = 'B';
								  color="green";
							}  else if(suma_notas=='1'){
								  nota_com = 'C';
								  color="red";
							}  else{
								  nota_com ='';
							}	  
						}
						if($('#id_cur').val()=="T"){
							console.log('entro1');
							console.log(nota_com);
							//$('#not_reporte_aula-tabla > tbody:last tr:last td:eq(' + columnas + ')').after('<td bgcolor="#D6EAF8" style="width: 1px;"><label style="font-size: 9pt; font-weight: bold; color: '+color+'; text-align: center;" for="'+key+'_'+id_alumno+'">' +nota_com + '</label></td>');
							$("#not_reporte_aula-tabla > tbody:last tr:last td:eq(" + columnas + ")").after("<td bgcolor='#D6EAF8' style='width: 1px; '><input type='hidden' id='"+key+"_"+id_alumno+"' min='1' max='4' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' data-id_com='" + key + "' data-id='" + competenciaSuma[key] + "' data-bd='" + suma_notas + "' data-cant_com='" + competencias.length + "' data-tipo='COM' data-id_alu='" + id_alumno + "'  value='" + nota_com +'\'><label style="font-size: 9pt; font-weight: bold; color: '+color+'; text-align: center;" for="'+key+'_'+id_alumno+'">'+nota_com+'</label></td>');
						} else {
							if(tip_prom_per=="MANU"){
								$("#not_reporte_aula-tabla > tbody:last tr:last td:eq(" + columnas + ")").after("<td bgcolor='#D6EAF8' style='width: 1px;' ><input id='"+key+"' min='1' max='4' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;'   onkeyup=onckeydownNota(this,event) onblur=onckeydownNota(this,event)  data-id_com='" + key + "' data-id='" + competenciaSuma[key] + "' data-bd='" + suma_notas + "' data-cant_com='" + competencias.length + "' data-tipo='COM' data-id_alu='" + id_alumno + "' value='" + nota_com +'\'></td>');
							} else {
								$('#not_reporte_aula-tabla > tbody:last tr:last td:eq(' + columnas + ')').after('<td bgcolor="#D6EAF8" style="width: 1px;">' +nota_com + '</td>');
							}	
						}	
						
					}  else if(tip_cali=="CUANTI"){
						if(suma_notas!='' && suma_notas!=null){
							if(suma_notas<10){
							nota_com="0"+Math.round(suma_notas);
							color="red";
							}else{
								nota_com=Math.round(suma_notas);
								color="blue";
							}	
						}	
						if($('#id_cur').val()=="T"){
							$("#not_reporte_aula-tabla > tbody:last tr:last td:eq(" + columnas + ")").after("<td bgcolor='#D6EAF8' style='width: 1px; '><input type='hidden' id='"+key+"_"+id_alumno+"' min='1' max='4' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' data-id_com='" + key + "' data-id='" + competenciaSuma[key] + "' data-bd='" + suma_notas + "' data-cant_com='" + competencias.length + "' data-tipo='COM' data-id_alu='" + id_alumno + "'  value='" + nota_com +'\'><label style="font-size: 9pt; font-weight: bold; color: '+color+'; text-align: center;" for="'+key+'_'+id_alumno+'">'+nota_com+'</label></td>');
						} else {
							if(tip_prom_per=="MANU"){
							$("#not_reporte_aula-tabla > tbody:last tr:last td:eq(" + columnas + ")").after("<td bgcolor='#D6EAF8' style='width: 1px; '><input id='"+key+"' min='1' max='4' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' onkeyup=onckeydownNotaCuanti(this,event) onblur=onckeydownNota(this,event)  data-id_com='" + key + "' data-id='" + competenciaSuma[key] + "' data-bd='" + suma_notas + "' data-cant_com='" + competencias.length + "' data-tipo='COM' data-id_alu='" + id_alumno + "'  value='" + nota_com +'\'></td>');
							} else {
								$("#not_reporte_aula-tabla > tbody:last tr:last td:eq(" + columnas + ")").after("<td bgcolor='#D6EAF8' style='width: 1px; '><input type='hidden' id='"+key+"_"+id_alumno+"' min='1' max='4' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' data-id_com='" + key + "' data-id='" + competenciaSuma[key] + "' data-bd='" + suma_notas + "' data-cant_com='" + competencias.length + "' data-tipo='COM' data-id_alu='" + id_alumno + "'  value='" + nota_com +'\'><label style="font-size: 9pt; font-weight: bold; color: '+color+'; text-align: center;" for="'+key+'_'+id_alumno+'">'+nota_com+'</label></td>');
							}	
						}			
					}						
				}
			}*/
			//calcular_promedio(id_alumno);
		}
		//},param);	
		//console.log(tip_cali);
		//body de la tabla
		
			$('[data-popup="tooltip"]').tooltip();
				
			}, param
	);

}
	
	
$('#frm-reporte-encuesta').on('submit',function(event){
		event.preventDefault();
		
	listar_tabla();
		//var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val(), rep_com:$('#rep_com').val(), id_cic:$('#id_cic').val()};
		//return _get('api/matricula/reporteMatriculaList',fncExito, param);
	});
	
function generar_usuario(obj,event){
	event.preventDefault();
	var id_mat = $(obj).data('id_mat');	
	var id_alu = $(obj).data('id_alu');	
	_POST({url:'/api/matricula/generarUsuarioGooglexMatricula/' + id_alu+'/'+id_mat+'/'+$('#_id_anio').text(), context:_URL,success:function(data){
			$('#frm-reporte-matriculados').submit();
			
			/*var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val()};
			return _get('api/matricula/reporteMatriculaList',fncExito, param);*/
		}
	});
	//$('#frm-reporte-matriculados').submit();
}

function generar_usuario_colegio(obj,event){
	event.preventDefault();
	var id_mat = $(obj).data('id_mat');	
	var id_alu = $(obj).data('id_alu');	
	_POST({url:'/api/matricula/generarUsuarioGooglexMatriculaColegio/' + id_alu+'/'+id_mat+'/'+$('#_id_anio').text(), context:_URL,success:function(data){
			$('#frm-reporte-matriculados').submit();
			
			/*var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val()};
			return _get('api/matricula/reporteMatriculaList',fncExito, param);*/
		}
	});
	//$('#frm-reporte-matriculados').submit();
}

