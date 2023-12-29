//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores = [];
var _param = {};// parametros de busqueda;
var list = [];
function onloadPage(params) {

var rol = _usuario.roles[0];
	/*$('#id_gra').on(
			'change',
			function(event) {
				_llenarCombo('col_aula', $('#id_au'), null, $("#_id_anio")
						.text()
						+ ',' + $('#id_gra').val(), function() {
					$('#id_au').change();
				});
				var param = {
					id_niv : $('#id_niv').val(),
					id_anio : $('#_id_anio').text(),
					id_gir : $('#id_gir').val()
				};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
						$('#id_cpu'), null, param, function() {
							$('#id_cpu').append(
									new Option('Promedio Final', '0', true,
											true));

						});
				limpiarTabla();
			});*/

	/*$('#id_niv').on('change', function(event) {
		var param = {
			id_niv : $(this).val()
		};
		// _llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
		_llenarCombo('cat_grad', $('#id_gra'), null, $(this).val(), function() {
			$('#id_gra').change()
		});

		limpiarTabla();
	});*/

	/*$('#id_au').on('change', function(event) {
		limpiarTabla();
		
		/*4E secundaria o 5E secundarioa*/
		/*if ($('#id_cpu').val()=='0' && ($(this).val()=='132' || $(this).val()=='136') ){
			$('#btn-grabar-siage').show();
		}else
			$('#btn-grabar-siage').hide();*/
		/*var param1 = {id_niv : $('#id_au option:selected').attr("data-aux2") , id_anio: $('#_id_anio').text(), id_gir : $('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1, function() {
				
				$('#id_cpu').change();
		});

	});*/
	
	$('#id_au').on('change', function(event) {
		limpiarTabla();
	});	
	
	
	$('#id_gra').on('change',function(event){
		
		var id_tra=null;
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
			id_tra=0
		} else {
			id_tra=_usuario.id_tra;
		}	
		var param = {
			id_anio: $("#_id_anio").text(),
			id_tra: id_tra,
			id_gir : $('#id_gir').val(),
			id_gra : $(this).val()
		};
		
		if(_usuario.roles.indexOf(_ROL_TUTOR)>-1){
				_llenarComboURL('api/aula/listAulasxTutorxAnio', $('#id_au'),null, param, function() {
			$('#id_au').change();
			});
		} else {
					_llenarComboURL('api/aula/listarAulasxCoordinadorGiro', $('#id_au'),null, param, function() {
			$('#id_au').change();
			});
		}

		var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text(), id_gir : $('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1, function() {
				
				$('#id_cpu').change();
		});		

		limpiarTabla();
	});	
		
	$('#id_niv').on('change', function(event) {
	  var id_niv=$('#id_niv').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
	});
	
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		}	
		
	
	}); 
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		var id_tra=_usuario.id_tra;
		var id_anio= $("#_id_anio").text();
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else if (_usuario.roles.indexOf(_ROL_TUTOR)>-1){
		var id_tra=_usuario.id_tra;
		var id_anio= $("#_id_anio").text();
		_llenarComboURL('api/trabajador/listarGirosNegocioxTutor/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}	
	
	$('#btn-grabar-siage').on('click', function(event) {
		var param = {
			id_anio : $('#_id_anio').text(),
			id_gra : $('#id_gra').val(),
			id_niv : $('#id_niv').val(),
			id_au : $('#id_au').val(),
			nump : $('#id_cpu').val()
		};
		_post('api/nota/grabarNotaAreaSiage',param,function (data){
			console.log(data);
		});

	});
	

	$('#id_cpu').on('change', function(event) {
		limpiarTabla();
	});

	/*_llenarCombo('cat_nivel', $('#id_niv'), null, null, function() {
		$('#id_niv').change()
	});*/
	
	$('#rep_com').on('click',function(){
	   if(this.checked){
		 $('#rep_com').val(1);
	   } else {
		 $('#rep_com').val(0);
	   }	   
	});

	$('#btn-excel').attr('disabled', 'disabled');

	$('#btn-excel').on(
			'click',
			function() {

				var params = "?id_anio=" + $('#_id_anio').text() + "&id_gra="
						+ $('#id_gra').val() + "&id_niv=" + $('#id_niv').val()
						+ "&id_au=" + $('#id_au').val() + "&nump="
						+ $('#id_cpu').val();

				document.location.href = _URL + 'api/nota/notas_areas_finales'
						+ params;
			});
}

function limpiarTabla() {
	$('#not_reporte_aula-tabla').find('thead').html('');
	$('#not_reporte_aula-tabla').find('tbody').html('');
	$('#btn-excel').attr('disabled', 'disabled');
}

$('#buscar-frm').on('submit', function(event) {
	event.preventDefault();// no referesca
	listar_tabla();
});

function listar_tabla() {

	var param = {
		id_anio : $('#_id_anio').text(),
		id_gra : $('#id_gra').val(),
		id_niv : $('#id_niv').val(),
		id_au : $('#id_au').val(),
		nump : $('#id_cpu').val(),
		id_gir : $('#id_gir').val(), 
		rep_com : $('#rep_com').val()
	};

	// columna dinamica

	_get('api/nota/listarNotasAreaCompetencias', function(data) {

		///var data = result.result;

		pirtarTabla(data, "sorting", "sorting");

		$('#btn-excel').removeAttr('disabled');

	}, param);

}

function pirtarTabla(data, clazzAlu, clazzProm) {

	list = data;

	$('#not_reporte_aula-tabla').find('thead').html('');
	$('#not_reporte_aula-tabla').find('tbody').html('');

	var cont = 0;
	var alumnos = list.alumnos;
	var areas = list.areaCompetencias;

	console.log(areas);

	// INICIO - header de la tabla
	// CURSOS
	var td = '';
	
	if ($('#id_cpu').val()=='0')
		td = '<th class="rotate"><div><span>COMPORTAMIENTO</span></div></th>';
	
	for ( var key in areas) {
		var competencias_area=areas[key].competencias;
		for ( var y in competencias_area) {
		td = td + '<th class="rotate"  data-popup="tooltip" style="font-size: 7pt;" title="' +  competencias_area[y].nom + '"><div><span>' + competencias_area[y].nom
				+ '</span></div></th>';
		}	
		td = td + '<th class="rotate" bgcolor="#D5F5E3" style="font-size: 9pt; font-weight: bold; color: black; " ><div><span>' + areas[key].nom_area
				+ '</span></div></th>';
	}
	$('#not_reporte_aula-tabla')
			.find('thead')
			.append(
					'<tr role="row"><th> Nro</th><th>DNI</th><th onclick="javascript:order(\'Alu\',this)" class="'
							+ clazzAlu
							+ '" data-sort="0" tabindex="0"  >Alumno</th><th class="rotate"><div><span>GRADO</span></div></th>'
							+ td
							+ '<th onclick="javascript:order(\'Prom\',this)" class="'
							+ clazzProm
							+ '" data-sort="0" tabindex="0" >Promedio</th><tr>');

	// body de la tabla
	for ( var key in alumnos) {

		cont++;
		var id_alu = key;
		//var area_alu = alumnos[id_alu].areas;
		var notas = alumnos[id_alu].notas;
		var tdsNotas = '';
		if ($('#id_cpu').val()=='0')
			tdsNotas = '<td>' +  alumnos[id_alu].comportamiento + '</td>';
		
		var suma = 0;
		for ( var y in areas) {
			var id_area_cab=areas[y].id;
			for ( var keyNotas in notas) {
				var id_area=notas[keyNotas].id;
				if(id_area_cab==id_area){
					var competencias_area=areas[y].competencias;
					var competencias_notas=notas[keyNotas].competencia_notas;
					for ( var x in competencias_area) {
						var id_com=competencias_area[x].id;
						for ( var l in competencias_notas) {
							var id_com_not=competencias_notas[l].id;
							if(id_com==id_com_not){
								var promedio =competencias_notas[l].nota;
								if (promedio == null){
								tdsNotas = tdsNotas + '<td style="color:red" title="NO PRESENTA NOTAS">S.N</td>';
								} else {
									tdsNotas = tdsNotas + '<td>' + promedio + '</td>';
								}	
							}	
						}	
						
						
					}
					tdsNotas = tdsNotas + '<td></td>';					
				}	
			
			}
			
		}	
			
		/*for ( var id_area in areas) {
			/*console.log("id_alu:" +id_alu + "***********");
			console.log("id_area:"); 
			console.log(areas[id_area]);
			console.log("area_alu:");
			console.log(area_alu);*/
		/*	var promedio = area_alu[areas[id_area].id_area];
			
			if (promedio == null){
					tdsNotas = tdsNotas + '<td style="color:red" title="NO PRESENTA NOTAS">S.N</td>';
			}else{
				suma = suma + promedio;
				if (promedio < 10) {
					tdsNotas = tdsNotas + '<td style="color:red">0' + promedio
							+ '</td>';
				} else {

					if (promedio < 11) {
						tdsNotas = tdsNotas + '<td style="color:red">' + promedio
								+ '</td>';
					} else
						tdsNotas = tdsNotas + '<td>' + promedio + '</td>';

				}
			}
				

			

		}*/
		var promFinal = suma / Object.keys(areas).length;

		
				if(alumnos[id_alu].id_sit!=null){
						if(alumnos[id_alu].id_sit=='5'){
							$('#not_reporte_aula-tabla').find('tbody').append('<tr><td style="color:red">' + cont + '</td><td style="color:red">' + alumnos[id_alu].nro_doc
							+ '</td><td style="color:red" title="TRASLADADO">' + alumnos[id_alu].alumno + '</td><td style="color:red">'
							+ alumnos[id_alu].grado + alumnos[id_alu].secc
							+ '</td>' + tdsNotas
							+ '<td align="right" bgcolor="#aac3e4">'
							+ $.number(promFinal, 2) + '</td></tr>');
						} else {
							$('#not_reporte_aula-tabla').find('tbody').append('<tr><td>' + cont + '</td><td>' + alumnos[id_alu].nro_doc
							+ '</td><td>' + alumnos[id_alu].alumno + '</td><td>'
							+ alumnos[id_alu].grado + alumnos[id_alu].secc
							+ '</td>' + tdsNotas
							+ '<td align="right" bgcolor="#aac3e4">'
							+ $.number(promFinal, 2) + '</td></tr>');
						}	
				} else {
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td>' + cont + '</td><td>' + alumnos[id_alu].nro_doc
							+ '</td><td>' + alumnos[id_alu].alumno + '</td><td>'
							+ alumnos[id_alu].grado + alumnos[id_alu].secc
							+ '</td>' + tdsNotas
							+ '<td align="right" bgcolor="#aac3e4">'
							+ $.number(promFinal, 2) + '</td></tr>');
				}
				
	}
}

function order(columna, th) {
	var $th = $(th);
	var clazz = $th.attr('class');
	var arreglo = list.alumnos;

	if (columna == "Prom") {
		if (clazz == "sorting_asc") {
			arreglo.sort(order_desc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting", "sorting_desc");
		} else {
			console.log(arreglo);
			arreglo.sort(order_asc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting", "sorting_asc");
		}
	} else {
		if (clazz == "sorting_asc") {
			arreglo.sort(order_alu_desc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting_desc", "sorting");
		} else {
			console.log(arreglo);
			arreglo.sort(order_alu_asc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting_asc", "sorting");
		}
	}

}

function order_asc(a, b) {
	if (a.promedio < b.promedio)
		return -1;
	if (a.promedio > b.promedio)
		return 1;
	return 0;
}

function order_desc(a, b) {
	if (a.promedio > b.promedio)
		return -1;
	if (a.promedio < b.promedio)
		return 1;
	return 0;
}

function order_alu_asc(a, b) {
	if (a.alumno < b.alumno)
		return -1;
	if (a.alumno > b.alumno)
		return 1;
	return 0;
}

function order_alu_desc(a, b) {
	if (a.alumno > b.alumno)
		return -1;
	if (a.alumno < b.alumno)
		return 1;
	return 0;
}
