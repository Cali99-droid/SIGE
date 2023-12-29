//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[]; //cambiooo
var _param = {};//parametros de busqueda;
var _permiso_doc=null;
var tip_cali='';
var tip_pro_anu='';
var tip_pro_per='';
var _letrasAutorizadas = [];
var _nota_ini='';
var _nota_fin='';
var __capacidades=[];
function onloadPage(params) {
	//alert(2);
	$('#btn-grabar_actual').hide();
	$('#btn-eliminar').hide();
	var id_tra=_usuario.id_tra;
	var id_anio=$("#_id_anio").text();
	var param = {
		id_tra : _usuario.id_tra,
		id_anio : $('#_id_anio').text()
	};
	
	$('#id_area').on('change',function(event) {
		var param ={};
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		
			param={id_caa:$('#id_area').val(), id_au:$('#id_au').val()};
		} else if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
			
			param={id_caa:$('#id_area').val(), id_au:$('#id_au').val()};
		} else {
			param={id_caa:$('#id_area').val(), id_au:$('#id_au').val(), id_tra:_usuario.id_tra};
		}	
		$('#id_cur').find('option[value=""]').remove();
		_llenarComboURL('api/areaAnio/listarCursosDCNXAreaxGradoCombo',
			$('#id_cur'), null, param, function() {
				console.log($('#id_cur').val());
				if($('#id_cur').val()==null){
					$('#curso').css('display','none');
					$('#btn-buscar').click();
				} else {
					$('#curso').css('display','block');
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
					$("#id_cur").prepend("<option value='T' selected='selected'>TODOS</option>");
					}
					$('#id_cur').change();
					$('#btn-buscar').click();
					//$('#btn-buscar').click();
				}	
				
			});
	});	
	
	$('#id_cpu').on('change',function(event) {
		$('#btn-buscar').click();
		//Obtener las letras autorizadas
		var param={id_gra:$('#id_au option:selected').attr("data-aux1"), id_anio:$("#_id_anio").text(), id_cpu:$('#id_cpu').val(), id_gir:$('#id_gir').val()};
		_get('api/perUni/detallePeriodoCalificacion',
			function(data){
			var id_tca=data.id_tca;
			
			//_letrasAutorizadas.length=0;
			_letrasAutorizadas.splice(0, _letrasAutorizadas.length);
			_nota_ini='';
			_nota_fin='';
			if(id_tca==1){
				var listaLetras=data.notas;
				var i=0;
				for (var key in data.notas) {
					i++;					
				 _letrasAutorizadas.push(data.notas[key].letras);					
				}	
			} else if(id_tca==2){
				_nota_ini=data.nota_ini;
				_nota_fin=data.nota_fin;
			}
			console.log(_letrasAutorizadas);
			console.log(_nota_ini);
			console.log(_nota_fin);
		}, param);
	});	
	
	$('#id_au').on('change',function(event) {
		var param={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"), id_tra:_usuario.id_tra, id_gir:$('#id_gir').val()};
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			_llenarComboURL('api/areaAnio/listarAreasxCoordinadorDCNGradoCombo',
			$('#id_area'), null, param, function() {
				$('#id_area').change();
				//$('#btn-buscar').click();
			});
		} else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
			var param={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/areaAnio/listarAreasDCNGradoCombo',
			$('#id_area'), null, param, function() {
				$('#id_area').change();
				//$('#btn-buscar').click();
			});
		} else {
			var param={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"),id_tra:_usuario.id_tra, id_gir:$('#id_gir').val()};
			_llenarComboURL('api/areaAnio/listarAreasDCNGradoCombo',
			$('#id_area'), null, param, function() {
				$('#id_area').change();
				//$('#btn-buscar').click();
			});
		}				
		/*var param={id_anio:$("#_id_anio").text(), id_niv:$('#id_au option:selected').attr("data-aux2")};
		_llenarComboURL('api/perAcaNivel/listarPeriodosxNivelyAnio',
			$('#id_cpan'), null, param, function() {
				$('#id_cpan').change();
			});	*/
		//listar Periodos
		var param1 = {id_niv : $('#id_au option:selected').attr("data-aux2") , id_anio: $('#_id_anio').text(), id_gir:$('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1, function() {
				
				$('#id_cpu').change();
		});
	});

	$('#id_gir').on('change',function(event) {	
	var param2={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"), id_tra:_usuario.id_tra, id_gir:$('#id_gir').val()};
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			_llenarComboURL('api/aula/listarAulasxCoordinadorNivel', $('#id_au'),null, param2, function() {
			$('#id_au').change();
			});
		} else if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
			var param ={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/aula/listarAulasxGiroNivelGrado', $('#id_au'),null, param, function() {
			$('#id_au').change();
			});
		} else {
			_llenarComboURL('api/aula/listarAulasxDocente', $('#id_au'),null, param2, function() {
			$('#id_au').change();
			});
		}	

	});
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();});
	} else if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1){
		_llenarComboURL('api/trabajador/listarGirosNegocioxDocente/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();});
	} else if (_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
		_llenarCombo('ges_giro_negocio',$('#id_gir'), null, null,function() {
			$('#id_gir').change();
		});	
	} else {	
		_llenarCombo('ges_giro_negocio',$('#id_gir'), null, null,function() {
			$('#id_gir').change();
		});	
	}
	/*$('#id_eva').on('change',function(e){
		//var id_eva = $(this).val();
		//var table = $('#not_nota-tabla').DataTable();
		//table.clear().draw();
		$('#not_nota-div').hide();
		$('#btn-grabar').hide();
		$('#btn-actualizar').hide();
	});

	var param = {
		id_tra : _usuario.id_tra,
		id_anio : $('#_id_anio').text()
	};*/
		

	$('#btn-buscar').on('click', function(e) {
	event.preventDefault();//no referesca
	listar_tabla();
	});
	
function listar_tabla(){
	var param = {id_anio: $('#_id_anio').text(),id_cur:$('#id_cur').val(),id_niv:$('#id_au option:selected').attr("data-aux2"),id_gra:$('#id_au option:selected').attr("data-aux1"),id_au:$('#id_au').val(), id_dcare:$('#id_area option:selected').attr("data-aux1"),id_cpu:$('#id_cpu').val()};
	
	//columna dinamica
	
	_get('api/nota/listarNotasAulaAreaoCurso/',
			function(data){
			if($('#id_cur').val()=="T"){
				$('#btn-agregar').hide();
			} else {
				$('#btn-agregar').show();
			}	
//este es, se qued
		__capacidades =data.capacidades;
		__indicadores =data.indicadores;
		var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					//	{"title": "DNI","data": "nro_doc"},
						{"title": "Alumno","data": "alumno"}];
						//{"title": "Sub-tema","data": "nom"}];
		var i=0;
		
		//limpiamos la tabla
		$('#not_reporte_aula-tabla').find('thead').html('');
		$('#not_reporte_aula-tabla').find('tbody').html('');

		for (var key in data.capacidades) {
			i++;					
			var titulo = data.capacidades[key];
			var id_cap = titulo.id;
			
			var element = {"title": titulo.nom,"data": "cap" + titulo.id,
					"render": function ( data, type, row, meta ) {
						
						}
				
					};
			
			columns.push(element);
			
		}
		var cont=0;
		var alumnos = data.alumnos;
		var competencias = data.competencias;
		var capacidades = data.capacidades;
		var desempenios = data.desempenios;
		var cursos = data.cursos;
		
		if(desempenios.length>0){
			if($('#id_cur').val()=="T"){
			$('#btn-grabar_actual').hide();
			} else {
				$('#btn-grabar_actual').show();
				//Verificar si esta dentro de las fechas
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1){
					var param_per = {id_niv: $('#id_au option:selected').attr("data-aux2"), id_anio: $('#_id_anio').text(), nump: $('#id_cpu').val()};
					_get('api/perUni/verificarPeriodoVig', function(data1) {
						//console.log(data1);
						if(data1=="1"){
							//alert();
							$('#btn-grabar_actual').show();
						} else {
							//Verificar si el docente tiene ampliación
							var param_vig={id_tra:_usuario.id_tra, id_au: $('#id_au').val(), id_cpu:$('#id_cpu').val()};
							_get('api/permisoDocente/obtenerVigencia', function(data2) {
								console.log(data2);
								if(data2!=null){ //si hay
									$('#btn-grabar_actual').show();
								} else{
									$('#btn-grabar_actual').hide();
								}
							}, param_vig);	
							//$('#btn-grabar_actual').hide();
						}	
						//flag_vig = data1.flag;
					},param_per);
				} 		
			}	
			//$('#btn-grabar_actual').show();
			if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			$('#btn-eliminar').show();
			}
		} else {
			$('#btn-grabar_actual').hide();
			$('#btn-eliminar').hide();
		}	

		//INICIO - header de la tabla
		//COMPETENCIAS
		var td = '';
		if($('#id_cur').val()=="T"){
			for (var key in competencias) {
			var id_com=competencias[key].com_id;
			var cont_cur=0;
			for (var y in cursos) {
				if(id_com==cursos[y].id_com){
					cont_cur++;
				}	
			}
			/*var cont_cap=0;
			for (var y in capacidades) {
				if(capacidades[y].com_nom==competencias[key].nom){
					cont_cap++;
				}	
			}	*/
			console.log(cont_cur);
			td = td + '<th colspan="' + (cont_cur + 1) + '" bgcolor="#AED6F1">' + competencias[key].nom  + '</th>';
			//td =  td + '<th >PROM.</th>';
			}
		} else {
			for (var key in competencias) {
			console.log(competencias[key].count);
			/*var cont_cap=0;
			for (var y in capacidades) {
				if(capacidades[y].com_nom==competencias[key].nom){
					cont_cap++;
				}	
			}	*/
			console.log(capacidades.length);
			td = td + '<th colspan="' + (competencias[key].count + 1) + '" bgcolor="#AED6F1">' + competencias[key].nom  + '</th>';
			//td =  td + '<th >PROM.</th>';
			}
		}	
		
		if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
			$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<tr>');
		} else {
			$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<th bgcolor="#aac3e4">Promedio</th><tr>');
		}	

		//INICIO - header de la tabla
		//CAPACIDADES
		td = '';
		var com = '';
		var i=1;
		if($('#id_cur').val()=="T"){
			var id_com = '';
			for (var y in competencias) {
			for (var key in cursos) {
				if(competencias[y].com_id==cursos[key].id_com){
				if(id_com!='' && cursos[key].id_com!=id_com)
					td =  td + '<th bgcolor="#AED6F1">PROM.</th>';
				//td = td + '<th data-popup="tooltip" title="' +  capacidades[key].nom + '" data-placement="top">C' + i + '</th>';
				td = td + '<th bgcolor="#D5F5E3" data-popup="tooltip" title="' +  cursos[key].curso + '" data-placement="top">' + cursos[key].curso + '</th>';
				id_com = cursos[key].id_com;
				i++;
				}
			}
			}
		} else {
			for (var key in capacidades) {
			if(com!='' && capacidades[key].com_nom!=com)
				td =  td + '<th bgcolor="#AED6F1">PROM.</th>';
			//td = td + '<th data-popup="tooltip" title="' +  capacidades[key].nom + '" data-placement="top">C' + i + '</th>';
			td = td + '<th colspan="' + (capacidades[key].count_cap) + '" bgcolor="#D5F5E3" data-popup="tooltip" title="' +  capacidades[key].capacidad + '" data-placement="top">' + capacidades[key].nom + '</th>';
			com = capacidades[key].com_nom;
			i++;
			}
		}	
		
		td =  td + '<th bgcolor="#AED6F1">PROM.</th>';
		if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
			$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<tr>');
		} else{
			$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<th bgcolor="#aac3e4">Promedio</th><tr>');
		}	
		
		
		//DESEMPENIOS
		td = '';
		var com = '';
		var i=1;
		if($('#id_cur').val()!="T"){
			//alert();
				for (var n in capacidades) {
			for (var key in desempenios) {
				
			
			//td = td + '<th data-popup="tooltip" title="' +  desempenios[key].des_nom + '" data-placement="top">D' + i + '</th>';
		
			if(desempenios[key].id_cap==capacidades[n].id){
				if(com!='' && desempenios[key].com_nom!=com)
				td =  td + '<th bgcolor="#AED6F1">PROM.</th>';
				td = td + '<th data-popup="tooltip"  bgcolor="#FCF3CF" title="' +  desempenios[key].des_nom + '" data-placement="top">D' + desempenios[key].orden + '</th>';
				com = desempenios[key].com_nom;
				i++;
			}	
			//}
			}
		}
			td =  td + '<th bgcolor="#AED6F1">PROM.</th>';
			if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
				$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<tr>');
			} else {
				$('#not_reporte_aula-tabla').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Alumno</th>' + td  + '<th bgcolor="#aac3e4">Promedio</th><tr>');
			}	
		}	
		
				
		var param={id_anio:$('#_id_anio').text() , id_gra:$('#id_au option:selected').attr("data-aux1"), id_adc:$('#id_area option:selected').attr("data-aux1")};

		_get('api/nota/obtenerConfiguracionArea',function(data){
			//console.log(data);
			 tip_cali=data.tip_cali;
			 tip_prom_per=data.tip_pro_per;
			 tip_prom_anu=data.tip_pro_anu;
			 		var first=true;
		for (var key in alumnos) {
			//alert();
			cont++;
			var id_alu = key;
			var notas = alumnos[id_alu].nota;
			var id_alumno=alumnos[id_alu].id_alu;
			var competenciaSuma =alumnos[id_alu].competenciaSuma;
			var notasCur = alumnos[id_alu].notasCursos;
			var tdsNotas= "";
			var tdsPromComp="";
			if($('#id_cur').val()=="T"){
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
						var value_nota_db=notasCur[keyNotas].nota;
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
			} else {
				for (var keyNotas in notas) {
					var id_com = notas[keyNotas].id_com;
					var id_cap = notas[keyNotas].id_cap;
					//Verifico si el ingreso de Notas para este aula, area y/o curso en cualitativo o cuantitativo
					/*var param={id_anio:$('#_id_anio').text() , id_gra:$('#_id_anio').text(), id_adc:$('#id_area option:selected').attr("data-aux1")};
					_get('api/nota/obtenerConfiguracionArea',function(data){*/
					//	console.log(data);
					//	var tip_cali=data.tip_pro_per;
					//	var tip_prom_per=data.tip_pro_per;
					//	var tip_prom_anu=data.tip_pro_anu;
						var value_nota_db=notas[keyNotas].nota;
						var nota_value="";
						//console.log(tip_cali);
						var color="";
						if(tip_cali=="CUALI"){
							//alert();
							if(value_nota_db!=''){
								if(value_nota_db=='4'){
									  nota_value='AD';
									  color="blue";
								}  else if(value_nota_db=='3'){
									  nota_value = 'A';
									   color="blue";
								}  else if(value_nota_db=='2'){
									  nota_value = 'B';
									   color="green";
								}  else if(value_nota_db=='1'){
									  nota_value = 'C';
									  color="red";
								}  else{
									  nota_value ='';
								}	  
							}
							tdsNotas = tdsNotas  + "<td><input id='n"  + notas[keyNotas].id_desau+"' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' class='form-control' onkeyup=onckeydownNota(this,event) data-id='" + notas[keyNotas].id_not + "' data-bd='" + notas[keyNotas].nota + "' data-id_com='" + id_com + "' data-id_cap='" + id_cap + "' data-cant_com='" + competencias.length + "' data-tipo='CAP' data-id_desau='" + notas[keyNotas].id_desau + "' data-id_alu='" + id_alumno + "' value='" + nota_value + "'/></td>";
						} else if(tip_cali=="CUANTI"){
							if(value_nota_db!='' && value_nota_db!=null){
								if(value_nota_db<10){
								nota_value="0"+value_nota_db;
								color="red";
								}else{
									nota_value=value_nota_db;
									 color="blue";
								}	
							}	
							tdsNotas = tdsNotas  + "<td><input id='n"  + notas[keyNotas].id_desau+"' style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' class='form-control' onkeyup=onckeydownNotaCuanti(this,event)  data-id='" + notas[keyNotas].id_not + "' data-bd='" + notas[keyNotas].nota + "' data-id_com='" + id_com + "' data-id_cap='" + id_cap + "' data-cant_com='" + competencias.length + "' data-tipo='CAP' data-id_desau='" + notas[keyNotas].id_desau + "' data-id_alu='" + id_alumno + "' value='" + nota_value + "'/></td>";
						}	
						 
				}
			}	
			
			if( Math.round(alumnos[id_alu].promedioGeneral)<10){
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '</tr>');
				} else {
					if(alumnos[id_alu].id_sit!=null){
						if(alumnos[id_alu].id_sit=='5'){
							$('#not_reporte_aula-tabla').find('tbody').append('<tr><td bgcolor="#FF0000">' + cont + '</td><td bgcolor="#FF0000">' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
						} else {
							$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
						}	
						
					} else {
						$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
					}	
					//$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
				}	
			}else{	
				if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1 ){
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '</tr>');
				} else {
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
				}	
			}
			if(true){
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
			}
			calcular_promedio(id_alumno);
		}
		},param);	
		//console.log(tip_cali);
		//body de la tabla
		
			$('[data-popup="tooltip"]').tooltip();
				
			}, param
	);

}

	$('#btn-grabar_actual').on('click',function(){
		$(this).attr('disabled','disabled');
		var notaAula ={};
		//var evaluacion ={};
		//evaluacion.id_eva = $('#id_eva').val();
		//evaluacion.id_tra = _usuario.id_tra;
		//evaluacion.id_ind = _indicadores;
		//console.log('iteracion de filas');
		//notas
		//var table= $('#not_reporte_aula-tabla').DataTable();
		var error= false;
		var _input_error =null;
		var notaDesempeniotot = {};
		var notaDesempenios = [];
		var notaCompetencias = [];
		var id_cpu=$('#id_cpu').val();
		var id_au=$('#id_au').val();
		var id_cua=$('#id_cur').val();
		//table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
		$("#not_reporte_aula-tabla tbody tr").each(function (index) {
		 //   $(this).children("td").each(function (index2) {
			
			$(this).find(':input').each(function(){
				var notaDesempenio = {};
				var notaCompetencia = {};
				var tipo = $(this).attr("data-tipo");
				var id_alumno = $(this).attr("data-id_alu");
				if(tipo=='CAP'){
					var id_not_des = $(this).attr("data-id");
					var id_desau = $(this).attr("data-id_desau");
					var nota = $(this).val();
					console.log(id_not_des);
					if(nota!=''){
						var nota_equi="";
						if(tip_cali=='CUALI'){
							if(nota=="AD"){
								nota_equi=4;
							} else if(nota=="A"){
								nota_equi=3;
							} else if(nota=="B"){
								nota_equi=2;
							} else if(nota=="C"){
								nota_equi=1;
							}		
						} else if(tip_cali=='CUANTI'){
							nota_equi=nota;
						}	
						notaDesempenio.id_alu=id_alumno;
						notaDesempenio.id_not_des=id_not_des
						notaDesempenio.id_tra=_usuario.id_tra;
						notaDesempenio.id_desau=id_desau;
						notaDesempenio.nota=nota_equi;
						notaDesempenios.push(notaDesempenio);
					} else if(nota=='' && id_not_des!='null' && id_not_des!=''){
						var nota_equi="";
						notaDesempenio.id_alu=id_alumno;
						notaDesempenio.id_not_des=id_not_des
						notaDesempenio.id_tra=_usuario.id_tra;
						notaDesempenio.id_desau=id_desau;
						notaDesempenio.nota=nota_equi;
						notaDesempenios.push(notaDesempenio);
					}	
				} else if(tipo=='COM'){
					var id_com = $(this).attr("data-id_com");
					var id = $(this).attr("data-id");
					var prom = $(this).val();
					if(prom!=''){
						var nota_equi="";
						if(tip_cali=='CUALI'){
							if(prom=="AD"){
								nota_equi=4;
							} else if(prom=="A"){
								nota_equi=3;
							} else if(prom=="B"){
								nota_equi=2;
							} else if(prom=="C"){
								nota_equi=1;
							}		
						} else if(tip_cali=='CUANTI'){
							nota_equi=prom;
						}	
						notaCompetencia.id=id;
						notaCompetencia.id_com=id_com;
						notaCompetencia.id_tra=_usuario.id_tra;
						notaCompetencia.id_alu=id_alumno;
						notaCompetencia.id_cpu=id_cpu;
						notaCompetencia.id_au=id_au;
						notaCompetencia.prom=nota_equi;
						notaCompetencia.id_cua=id_cua;
						notaCompetencias.push(notaCompetencia);
					} else if(prom=='' && id!='null' && id!='') {
						var nota_equi="";
						notaCompetencia.id=id;
						notaCompetencia.id_com=id_com;
						notaCompetencia.id_tra=_usuario.id_tra;
						notaCompetencia.id_alu=id_alumno;
						notaCompetencia.id_cpu=id_cpu;
						notaCompetencia.id_au=id_au;
						notaCompetencia.prom=nota_equi;
						notaCompetencia.id_cua=id_cua;
						notaCompetencias.push(notaCompetencia);
					}		
				}	
			});
		 });	
		
		notaDesempeniotot.notaDesempenioReq=notaDesempenios;
		notaDesempeniotot.notaPromCompetenciaReq=notaCompetencias;
		//objeto q se enviará al controller de lina
		console.log(notaDesempeniotot);
		
		
		if(error){
			var msg ='Completar todas las notas.';

			_alert_error(msg,
			function(){
				_input_error.focus();
			});
			//$('#btn-grabar_actual').removeAttr("disabled");
		}
		else
			_post_json('api/nota/grabarNotasDesCom', notaDesempeniotot, function(data) {
				$('#btn-grabar_actual').removeAttr("disabled");
				$('#btn-buscar').click();
				//$('#btn-actualizar').show();
				//$('#btn-grabar').hide();
				//$('#buscar-frm').submit();
			});
		
	});
	
	
	/*$('#btn-eliminar').on('click',function(){
		
		var id_eva=$('#id_eva').val();
		_delete('api/nota/' + id_eva,
				function(){
			$('#buscar-frm').submit();
		},
		'De elimar todas las notas de esta evaluación?'
				);
	});*/
	
	$('#btn-eliminar').on('click',function(){
		
		var id_au=$('#id_au').val();
		var id_cpu=$('#id_cpu').val();
		var id_cua=$('#id_cur').val();
		var id_cur=null;
		if(id_cua==null){
			id_cur=0;
		} else {
			id_cur=id_cua;
		}	
		var id_dcarea=$('#id_area option:selected').attr("data-aux1");
		var param={id_cua:id_cua};
		_delete('api/nota/elimarNotasDesempenioCompetencia/'+ id_au+'/'+id_cpu+'/'+id_dcarea+'/'+id_cur,
				function(){
			listar_tabla();
		},null,
		'Desea eliminar todas las notas del aula, para el presente periodo?');
				
		/*_DELETE({url:'api/nota/elimarNotasDesempenioCompetencia/'+ id_au+'/'+id_cpu+'/'+id_dcarea,
		context:_URL,
		param : param,
			success:function(){
				listar_tabla();
			}
		});*/
	});	
}
// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();

});

$('#btn-agregar').on('click',function(event){
	event.preventDefault();//no referesca
	not_conf_desempenios();
});

function not_conf_desempenios() {

	// funcion q se ejecuta al cargar el modal
	var onShowModal = function() {

		_inputs('not_nota-frm');
		tree();

$('#btn-grabar_des').on('click', function () {
//alert();
	//var checkedIds = $('#tree').treeview('getSelected', nodeId);
	//console.log(checkedIds);
/*var checked_ids = []; 
$("#tree").treeview('get_selected').each(function(){  
     checked_ids.push($(this).data('id'));       
}); 
console.log(checked_ids);*/
var checkedNodes = $('#tree').treeview('getChecked');

        console.log(checkedNodes);

        var nodeIds = [];
		var nodeCapacidad=[];

        $.each(checkedNodes, function (i, n) {
			
            nodeIds.push(n.id);
			
			//console.log($('#tree').treeview('getParent',  n.id ).id) ; //var ParentID=$('#tree_0').treeview('getParent', NodeID).nodeId;
			//nodeCapacidad.push($('#tree').treeview('getParent',  n.id ).id);
			//  $('#tree').treeview('revealNode', [ n.id, { silent: true } ]);
			//  console.log($('#tree').treeview('revealNode', [ n.id, { silent: true } ]));

        })

        console.log(nodeIds);
		var json_configuracion={};
		json_configuracion.id_des=nodeIds;
		json_configuracion.id_aula=$('#id_aula').val();
		json_configuracion.id_areadc=$('#id_areadc option:selected').attr("data-aux1");
		json_configuracion.id_curdc=$('#id_curdc').val();
		json_configuracion.id_periodo=$('#id_periodo').val();
		console.log(json_configuracion);
		/*json_derivacion.id_inc=$("#id_inc").val();
		json_derivacion.id_cfi=$("#div_derivacion #id_cfi").val();
		json_derivacion.id_dir=$("#div_derivacion #id_dir").val();
		json_derivacion.id_cdd=$("#div_derivacion #id_cdd").val();
		json_derivacion.des=$("#div_derivacion #des").val();*/
			
		_POST({
			context:_URL,
			url:'api/nota/grabarConfiguracion',
			params:JSON.stringify(json_configuracion),
			contentType:"application/json",
			success: function(data){
				console.log(data);
				$('#btn-buscar').click();
				/*$("#id_der").val(data.result);	
				$("#div_derivacion #des").attr('readonly', true);
				$("#div_derivacion #btn_grabar").attr('disabled','disabled');*/
			}
		});	
		// console.log(nodeCapacidad);

        //[1, 6, 7, 2, 8, 4]
});
		//tree_2();
			var param = {
		id_tra : _usuario.id_tra,
		id_anio : $('#_id_anio').text()
	};
	
	$('#id_areadc').on('change',function(event) {
		var param={id_caa:$('#id_areadc').val()};
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			param={id_caa:$('#id_areadc').val(), id_au:$('#id_aula').val()};
		} else {
			param={id_caa:$('#id_areadc').val(), id_au:$('#id_aula').val(), id_tra:_usuario.id_tra};
		}	
		_llenarComboURL('api/areaAnio/listarCursosDCNXAreaxGradoCombo',
			$('#id_curdc'), $('#id_cur').val(), param, function() {
				$('#id_curdc').change();
			});
	});	
	
	$('#id_aula').on('change',function(event) {
		var param={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"), id_tra:_usuario.id_tra, id_gir:$('#id_gir').val()};
		/*_llenarComboURL('api/areaAnio/listarAreasDCNGradoCombo',
			$('#id_areadc'), $('#id_area').val(), param, function() {
				$('#id_areadc').change();
			});*/
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			_llenarComboURL('api/areaAnio/listarAreasxCoordinadorDCNGradoCombo',
			$('#id_areadc'), $('#id_area').val(), param, function() {
				$('#id_areadc').change();
				//$('#btn-buscar').click();
			});
		} else {
			_llenarComboURL('api/areaAnio/listarAreasDCNGradoCombo',
			$('#id_areadc'), $('#id_area').val(), param, function() {
				$('#id_areadc').change();
				//$('#btn-buscar').click();
			});
		}	
			
		//listar Periodos
		var param1 = {id_niv : $('#id_aula option:selected').attr("data-aux2") , id_anio: $('#_id_anio').text(), id_gir:$('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_periodo'), $('#id_cpu').val(), param1, function() {
				$('#id_periodo').change();
		});

	});	
	
	/*_llenarComboURL('api/aula/listarAulasxDocente', $('#id_aula'),$('#id_au').val(), param, function() {
		$('#id_aula').change();
	});*/
	var param={id_anio:$("#_id_anio").text(), id_gra:$('#id_au option:selected').attr("data-aux1"), id_tra:_usuario.id_tra, id_gir:$('#id_gir').val()};
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		_llenarComboURL('api/aula/listarAulasxCoordinadorNivel', $('#id_aula'),$('#id_au').val(), param, function() {
		$('#id_aula').change();
		});
	} else {
		_llenarComboURL('api/aula/listarAulasxDocente', $('#id_aula'),$('#id_au').val(), param, function() {
		$('#id_aula').change();
		});
	}	
		
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_giro'),null,null,function(){$('#id_giro').change();}); //
	} else if (_usuario.roles.indexOf(_ROL_PROFESOR)>-1){
		_llenarComboURL('api/trabajador/listarGirosNegocioxDocente/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_giro'),null,null,function(){$('#id_giro').change();}); //
	} else {
			_llenarCombo('ges_giro_negocio',$('#id_giro'), null, null,function() {
		$('#id_giro').change();
		});	
	}	

	
	$('#id_aula').prop('disabled',true);
	$('#id_giro').prop('disabled',true);
	$('#id_periodo').prop('disabled',true);
	$('#id_areadc').prop('disabled',true);
	$('#id_curdc').prop('disabled',true);
	/*$('#not_nota-frm #btn-agregar').on(
			'click',
			function(event) {
				$('#not_nota-frm #id').val('');
				_post($('#not_nota-frm').attr('action'), $('#not_nota-frm')
						.serialize(), function(data) {
					onSuccessSave(data);
				});
	});*/

		

	}

	// funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data) {
		nota_listar_tabla();
	}

	// Abrir el modal
	var titulo;

		titulo = 'Nuevo  Nota Evaluacion';

	_modal_full(titulo, 'pages/notas/not_conf_desempenios.html', onShowModal, onSuccessSave);

}

function tree(){
	 var data2 = [
  {
    text: "Equipos españoles",
state: 
		{
			checked: true,
			expanded: true,
			
		},
		color: "red",
		backColor: "#AED6F1",
    nodes: [
      {
        text: "Real Madrid",
		id: "1",
        nodes: [
          {
            text: "Gareth Bale",
			id: "2",
			state: 
		{
			checked: true,
			expanded: true,
			disabled:true
		}
          },
          {
            text: "Marcelo",
			id: "3"
          }
        ]
      },
      {
        text: "Barcelona",
        nodes: [
          {
            text: "Lio Messi"
          },
          {
            text: "Arturo Vidal"
          }
        ]
      }
    ]
  },
  {
    text: "Equipos ingleses",
	color: "red",
	backColor: "#AED6F1",
    nodes: [
      {
        text: "Chelsea",
		backColor: "#D5F5E3",
        nodes: [
          {
            text: "Pedro",
			backColor: "#FCF3CF"
          },
          {
            text: "Eden Hazard"
          }
        ]
      },
      {
        text: "Manchester City",
        nodes: [
          {
            text: "Gabriel Jesús"
          },
          {
            text: "Ederson"
          }
        ]
      }
    ]
  },
];
	var param={id_au:$('#id_au').val(), id_dcare:$('#id_area option:selected').attr("data-aux1"),id_gra:$('#id_au option:selected').attr("data-aux1"),id_cpu:$('#id_cpu').val(), id_cua:$('#id_cur').val()};
	_get('api/nota/listarConfCompCapDesem',function(data){
		 $('#tree').treeview({
     //checkbox: true,
showCheckbox: true,
                showBorder: false,
                selectedBackColor: 'skyblue',
                selectedColor: 'white',
  selectMode: 3,
  data: data,
/* expandIcon: "glyphicon glyphicon-stop",
    collapseIcon: "glyphicon glyphicon-unchecked",
  activate: function(event, data) {
    $("#statusLine").text(event.type + ": " + data.node);
  },*/
  select: function(event, data) {
    $("#statusLine").text(
      event.type + ": " + data.node.isSelected() + " " + data.node
    );
  },
onNodeChecked: function(event, node) {
	var thisDiv = $('#tree');
                                var parentNode = thisDiv.treeview('getParent', node);
								if(parentNode!='undefined'){
									if(parentNode && 0 <= parentNode.nodeId) {
                                    console.log(parentNode);
                                                                         // selected
                                    thisDiv.treeview('checkNode', [ parentNode, { silent: true } ]);
									var parentNode2 = thisDiv.treeview('getParent', parentNode.nodeId);
								 if(parentNode2 && 0 <= parentNode2.nodeId) {
                                    console.log(parentNode2);
                                                                         // selected
                                    thisDiv.treeview('checkNode', [ parentNode2, { silent: true } ]);
                                                                         // recursive
                                }
                                                                         // recursive
                                }
								}	
                                
								
								
    //var children = node['nodes'];
   //displays In general, scorpions are not aggressive.
    //alert( children[0].text )       
	//alert(node.text)    ;
	var childrenNodes = _getChildren(node);
	//var parents = $('#tree').treeview('getParent', node);
	//var parents = _getParent(node);
		$(childrenNodes).each(function(){
			$('#tree').treeview('checkNode', [ this.nodeId, { silent: true } ]);;
		});

},onNodeUnchecked: function(event, node) {
    var children = node['nodes'];
	var id=node.id;
	//verificar si tiene notas
 var cadena = id.split("-");
 var cadena1 = cadena[0].substring(0,3);
 var cadena2 = cadena[0].substring(3,7);
// console.log(cap.substring(0,3));
if(cadena1=="COM"){
	var param = {id_com:cadena2, id_cua: $('#id_cur').val(), id_au:$('#id_au').val(), id_cpu:$('#id_cpu').val()};
	_get('api/nota/listarNotasxCompetencia',function(data){
		console.log(data);
		if(data.length>0){
			alert('No se puede desactivar esta competencia porque tiene notas relacionadas');
			$('#tree').treeview('checkNode', [ node.nodeId, { silent: true } ]);;
		} else {
			var childrenNodes = _getChildren(node);
		$(childrenNodes).each(function(){
			$('#tree').treeview('uncheckNode', [ this.nodeId, { silent: true } ]);;
		});
		}	
	}, param);	
} else if(cadena1=="CAP"){
	var param = {id_cap:cadena2, id_cua: $('#id_cur').val(), id_au:$('#id_au').val(), id_cpu:$('#id_cpu').val()};
	_get('api/nota/listarNotasxCapacidad',function(data){
		console.log(data);
		if(data.length>0){
			alert('No se puede desactivar esta capacidad porque tiene notas relacionadas');
			$('#tree').treeview('checkNode', [ node.nodeId, { silent: true } ]);;
		} else {
			var childrenNodes = _getChildren(node);
		$(childrenNodes).each(function(){
			$('#tree').treeview('uncheckNode', [ this.nodeId, { silent: true } ]);;
		});
		}	
	}, param);
} else{
	var childrenNodes = _getChildren(node);
		$(childrenNodes).each(function(){
			$('#tree').treeview('uncheckNode', [ this.nodeId, { silent: true } ]);;
		});
}	
	
	var param = {};
	/*_get('api/nota/listarNotasxCompetencia',function(data){
		
	});	*/
   //displays In general, scorpions are not aggressive.
    //alert( children[0].text )       
	//alert(node.text)    ;
	
}
  });
	},param);	 
  
}	



function _getChildren(node) {
    if (node.nodes === undefined) return [];
    var childrenNodes = node.nodes;
    node.nodes.forEach(function(n) {
        childrenNodes = childrenNodes.concat(_getChildren(n));
    });
    return childrenNodes;
}

/*function nota_eliminar(link) {
	_delete('api/nota/' + $(link).data("id"), function() {
		nota_listar_tabla();
	});
}*/



function calcular_promedio(id_alu){
	//alert(cant_com);
	var promedio="";
	  $("#not_reporte_aula-tabla tbody tr").each(function (index) {
		  console.log('entrooo');
		 //   $(this).children("td").each(function (index2) {
			 var promedio_alu=0;
			 var promedio=0;
			 var cant_not=0;
				 $(this).find(':input').each(function(){
					 var id_alumno = $(this).attr("data-id_alu");
					 var tipo= $(this).attr("data-tipo");
					 if(id_alu==id_alumno && tipo=='COM'){
						
					var nota= $(this).val();
					var nota_value="";
					
					
				//	console.log(tipo);
					console.log(nota);
					if(nota!=''){
						if(tip_cali=='CUALI'){
							if(nota=='AD'){
								nota_value=4;
							} else if(nota=='A'){
								nota_value=3;
							}  else if(nota=='B'){
								nota_value=2;
							} else if(nota=='C'){
								nota_value=1;
							}	
						} else if(tip_cali=='CUANTI'){
							nota_value=parseFloat(nota);
						}	
						
					 if(id_alu==id_alumno && tipo=='COM'){
						 promedio = promedio + nota_value;
					 }
					 cant_not = cant_not + 1;
					// console.log(cant_not);
					} else{
						nota_value=0;
						if(id_alu==id_alumno && tipo=='COM'){
						 promedio = promedio + nota_value;
						} 
					}	
					//console.log(promedio);
					//alert(promedio);
				
					 }	 
					
						 	 
				});	
				//if(promedio!=''){
					console.log(promedio);
					console.log(cant_not);
					promedio_alu= promedio/cant_not;
				//} 
				
				var nota_equi="";
				if(tip_cali=='CUALI'){
					if(promedio_alu>3.4 ){ //&& promedio_alu<=4
						nota_equi="AD";
					} else if(promedio_alu>2.4 && promedio_alu<=3.4){
						nota_equi="A";
					} else if(promedio_alu>1.4 && promedio_alu<=2.4){
						nota_equi="B";
					} else if(promedio_alu<=1.4){
						nota_equi="C";
					}		
				} else if(tip_cali=='CUANTI'){
					//console.log(promedio_alu);
					if(promedio_alu>0){
						nota_equi=Math.round(promedio_alu);				
					} else {
						nota_equi='';
					}	
				}	
				
				//$(this).find('label[for=prom]').each(function(){
				$(this).children("td").each(function (index2) {

					//$(this).find("#lbl_dni").text(data[index].dni);
					$(this).find('label[for="'+id_alu+'"]').text(nota_equi);
							 
				});		  
	  });  
	 
}	

function calcular_promedio_competencia(id_alu, id_com, capacidades){
	//alert();
	//console.log(id_cap);
	//alert(cant_com);
	console.log(capacidades);
	//console.log(id_cap);
	var promedio="";
	  $("#not_reporte_aula-tabla tbody tr").each(function (index) {
		 //   $(this).children("td").each(function (index2) {
			 var promedio_alu=0;
			 var promedio=0;
			 var cant_not=0;
			 var cap_existe="";
			 var promedio_capacidades=0;
			 
			
			var prom_final=0;
			for (var i=0; i<capacidades.length; i++) {
				
				var prom_capacidad=0;
				var cant_not_cap=0;
				 var id_cap_conf=capacidades[i].id;
				 $(this).find(':input').each(function(){
					 var id_alumno = $(this).attr("data-id_alu");
					 var tipo= $(this).attr("data-tipo");
					 var id_com_ex= $(this).attr("data-id_com");
					 var id_cap_ex= $(this).attr("data-id_cap");
					 
					// console.log(id_cap);
					// if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex && id_cap==id_cap_ex){
					if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex  && id_cap_conf==id_cap_ex){
						
					var nota= $(this).val();
					var nota_value="";
					
					
				//	console.log(tipo);
					//console.log(nota);
					if(nota!=''){
						if(tip_cali=='CUALI'){
							if(nota=='AD'){
								nota_value=4;
							} else if(nota=='A'){
								nota_value=3;
							}  else if(nota=='B'){
								nota_value=2;
							} else if(nota=='C'){
								nota_value=1;
							}	
						} else if(tip_cali=='CUANTI'){
							nota_value=parseFloat(nota);
						}	
					//console.log('nota1'+nota_value);
					//for (var i=0; i<capacidades.length; i++) {
						
						// if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex && id_cap==id_cap_ex){
						if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex && id_cap_conf==id_cap_ex){
							//console.log('entrocapacidad'+id_cap_conf);
							//console.log('nota2'+nota_value);
							//promedio = promedio + nota_value;
							prom_capacidad = prom_capacidad + nota_value;
							cant_not_cap = cant_not_cap+1;
							//console.log('entroooo1');
						}
						//cant_not = cant_not + 1;
					// }	 
					 
					// console.log(cant_not);
					} else{
						nota_value=0;
						//var id_cap_conf=capacidades[i].id;
						//if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex && id_cap==id_cap_ex){
							if(id_alu==id_alumno && tipo=='CAP' && id_com==id_com_ex && id_cap_conf==id_cap_ex){
								prom_capacidad = prom_capacidad + nota_value;
						 //promedio = promedio + nota_value;
						} 
					}
					//console.log(promedio);
					//alert(promedio);
				
					 }	 
					
						 	 
				});	
				if(prom_capacidad>0){
					promedio_capacidades=prom_capacidad/cant_not_cap;
				}	
				
				console.log(promedio_capacidades);
				//console.log(cant_not_cap);
				if(promedio_capacidades!=''){
					if(promedio_capacidades>0){
					cant_not = cant_not + 1;
					}	
					promedio_alu = promedio_alu + promedio_capacidades;
				}	
				
				//console.log(promedio_alu);
				
				
				}
				console.log('nota_final'+promedio_alu);
				console.log('cant_notas'+cant_not);
				prom_final=promedio_alu/cant_not;
				//if(promedio!=''){
				//	promedio_alu= promedio/cant_not;
				//} 
				/*$(this).children("td").each(function (index2) {
					$(this).find('data_prom_cap'+id_cap+""+id_alu).attr(promedio_alu);
				}*/
				
			
				var nota_equi="";
				if(tip_cali=='CUALI'){
					if(prom_final>3.4){ //&& promedio_alu<=4
						nota_equi="AD";
					} else if(prom_final>2.4 && prom_final<=3.4){
						nota_equi="A";
					} else if(prom_final>1.4 && prom_final<=2.4){
						nota_equi="B";
					} else if(prom_final<=1.4){
						nota_equi="C";
					}		
				} else if(tip_cali=='CUANTI'){
					//console.log(promedio_alu);
					if(prom_final>0){
						nota_equi=Math.round(prom_final);				
					} else {
						nota_equi='';
					}	
				}	
				
				//$(this).find('label[for=prom]').each(function(){
				$(this).children("td").each(function (index2) {

					//$(this).find("#lbl_dni").text(data[index].dni);
					$(this).find('label[for="'+id_com+"_"+id_alu+'"]').text(nota_equi);
					$(this).find('input[id="'+id_com+"_"+id_alu+'"]').val(nota_equi);		 
				});		  
	  });  
	   calcular_promedio(id_alu);
}	

function onckeydownNota(field, e){
	var id_niv = $('#id_au option:selected').attr("data-aux2");
	var id_alu= $(field).attr("data-id_alu");
	var id_com= $(field).attr("data-id_com");
	var id_cap= $(field).attr("data-id_cap");
	var cant_com=$(field).attr("data-cant_com");
	console.log(e.keyCode);
	console.log(tip_cali);
	console.log(tip_prom_per);
	var key = e.keyCode; 
	if(e.keyCode==38)//38 para arriba
      mover(e,-1);
    if(e.keyCode==40)//40 para abajo
      mover(e,1);
	 var items =  $("#not_reporte_aula-tabla").find(':input'),
  activo = document.activeElement;
  
  if (e.which == 39 || e.which == 13) {
    //ciclo for que valida si NO es el último 
    for (var i = 0; i < items.length - 1; i++) {
      if(activo === items[i]){
        items[i + 1].focus();
      }
    }
  }
  if (e.which == 37) {
    //ciclo for que valida si NO es el primero
    for (var i = 1; i < items.length; i++) {
      if (activo === items[i]) {
        items[i - 1].focus();
      }
    }
  }
  
	//if(e.keyCode==37 || e.keyCode==39)
	//	return;
	//if(e.keyCode==109 || e.keyCode==189)
	//	field.value='';
	//else{
		
		e.preventDefault();
		if(tip_cali=='CUALI'){
			if(_letrasAutorizadas.length>0){
				field.value=field.value.toUpperCase(); 
			//if((key>=96 && key<=100) || (key>=48 && key<=52) ){
			//	if(field.value=='AD' ||   field.value=='A' || field.value=='B' || field.value=='C' ){
				if(_letrasAutorizadas.includes(field.value)){
					if(field.value=='C'){
						/*if(field.value=='B')
							$(field).val(2);
						else (field.value=='C')
							$(field).val(1);*/
						$(field).css('color','red');
					} else if(field.value=='B'){
						$(field).css('color','green');
					} else{
						//$(field).toUpperCase();
						/*if(field.value=='AD')
							$(field).val(4);
						else (field.value=='A')
							$(field).val(3);*/
						$(field).css('color','blue');
						
					}
					if(tip_prom_per=='ARIT'){
						calcular_promedio_competencia(id_alu,id_com, __capacidades);
					}						
				} else{
					field.value='';
					if(tip_prom_per=='ARIT'){
						calcular_promedio_competencia(id_alu,id_com,__capacidades);
					}	
				}	
				
				calcular_promedio(id_alu);
			} else {
				alert('No hay una configuración de las calificaciones autorizadas, comuníquese con el coordinador');
				field.value='';
			}	
			
				//$.tabNext();
			//}	
		} else if(tip_cali=='CUANTI'){
			//if((key>=48 && key<=52) ){
			//if(field.value.length>=2  ){
			if(_nota_ini!='' && _nota_fin!=''){
				//alert(1);
				console.log(field.value);
				//var nota=parseFloat(field.value);
				//console.log(nota);
				//console.log(_nota_ini);
				//console.log(_nota_fin);
			
				if(parseFloat(field.value)>=parseFloat(_nota_ini) && parseFloat(field.value)<=parseFloat(_nota_fin)){
					//if(field.value.length>=2  ){
					
					if(parseFloat(field.value)>20 || parseFloat(field.value)<5)
						field.value='';
					else{
						if(parseFloat(field.value)<11)
							$(field).css('color','red');
						else
							$(field).css('color','blue');
						//$.tabNext();
					}
					if(tip_prom_per=='ARIT'){
						calcular_promedio_competencia(id_alu,id_com,__capacidades);
					}	
					//}
				} else {
					field.value='';
					if(tip_prom_per=='ARIT'){
						calcular_promedio_competencia(id_alu,id_com,__capacidades);
					}	
				}
				calcular_promedio(id_alu);	
			} else {
				alert('No hay una configuracion de las calificaciones autorizadas, comuníquese con el coordinador');
				field.value='';
			}	
			//} /*else{
				//field.value='';
			//}	
			/*} else
				field.value='';*/
			
		}	
}

function onckeydownNotaCuanti(field, e){
	//alert();
	var id_niv = $('#id_au option:selected').attr("data-aux2");
	var id_alu= $(field).attr("data-id_alu");
	var id_com= $(field).attr("data-id_com");
	var id_cap= $(field).attr("data-id_cap");
	var cant_com=$(field).attr("data-cant_com");
	console.log(e.keyCode);
	console.log(tip_cali);
	console.log(tip_prom_per);
	var key = e.keyCode; 
	if(e.keyCode==38)//38 para arriba
      mover(e,-1);
    if(e.keyCode==40)//40 para abajo
      mover(e,1);
	 var items =  $("#not_reporte_aula-tabla").find(':input:not([type=hidden])'),
  activo = document.activeElement;
  
  if (e.which == 39 || e.which == 13) {
    //ciclo for que valida si NO es el último 
    for (var i = 0; i < items.length - 1; i++) {
      if(activo === items[i]){
        items[i + 1].focus();
      }
    }
  }
  if (e.which == 37) {
    //ciclo for que valida si NO es el primero
    for (var i = 1; i < items.length; i++) {
      if (activo === items[i]) {
        items[i - 1].focus();
      }
    }
  }

  if(tip_prom_per=='ARIT'){
	//calcular_promedio_competencia(id_alu,id_com,id_cap);
		calcular_promedio_competencia(id_alu,id_com,__capacidades);
  }	
    calcular_promedio(id_alu);	
}


function mover(event, to) {
	//$.tabNext();
   let list = $('input:not([type=hidden])');
   let index = list.index($(event.target));
   index = Math.max(0,index + to);
   list.eq(index).focus();
}




