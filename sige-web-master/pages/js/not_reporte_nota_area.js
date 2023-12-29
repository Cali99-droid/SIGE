//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores = [];
var _param = {};// parametros de busqueda;
var list = [];
function onloadPage(params) {

	$('#id_gra').on(
			'change',
			function(event) {
				_llenarCombo('col_aula', $('#id_au'), null, $("#_id_anio")
						.text()
						+ ',' + $('#id_gra').val(), function() {
					$('#id_au').change();
				});
				var param = {
					id_niv : $('#id_niv').val(),
					id_anio : $('#_id_anio').text()
				};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
						$('#id_cpu'), null, param, function() {
							$('#id_cpu').append(
									new Option('Promedio Final', '0', true,
											true));

						});
				limpiarTabla();
			});

	$('#id_niv').on('change', function(event) {
		var param = {
			id_niv : $(this).val()
		};
		// _llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
		_llenarCombo('cat_grad', $('#id_gra'), null, $(this).val(), function() {
			$('#id_gra').change()
		});

		limpiarTabla();
	});

	$('#id_au').on('change', function(event) {
		limpiarTabla();
		
		/*4E secundaria o 5E secundarioa*/
		if ($('#id_cpu').val()=='0' && ($(this).val()=='132' || $(this).val()=='136') ){
			$('#btn-grabar-siage').show();
		}else
			$('#btn-grabar-siage').hide();

	});
	
	
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

	_llenarCombo('cat_nivel', $('#id_niv'), null, null, function() {
		$('#id_niv').change()
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
		nump : $('#id_cpu').val()
	};

	// columna dinamica

	_get('api/nota/listarNotasArea', function(data) {

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
	var areas = list.areas;

	console.log(areas);

	// INICIO - header de la tabla
	// CURSOS
	var td = '';
	
	if ($('#id_cpu').val()=='0')
		td = '<th class="rotate"><div><span>COMPORTAMIENTO</span></div></th>';
	
	for ( var key in areas) {
		td = td + '<th class="rotate"><div><span>' + areas[key].area.nom
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
		var area_alu = alumnos[id_alu].areas;
		var tdsNotas = '';
		if ($('#id_cpu').val()=='0')
			tdsNotas = '<td>' +  alumnos[id_alu].comportamiento + '</td>';
		
		var suma = 0;
		for ( var id_area in areas) {
			/*console.log("id_alu:" +id_alu + "***********");
			console.log("id_area:"); 
			console.log(areas[id_area]);
			console.log("area_alu:");
			console.log(area_alu);*/
			var promedio = area_alu[areas[id_area].id_area];
			
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
				

			

		}
		var promFinal = suma / Object.keys(areas).length;

		$('#not_reporte_aula-tabla').find('tbody').append(
				'<tr><td>' + cont + '</td><td>' + alumnos[id_alu].nro_doc
						+ '</td><td>' + alumnos[id_alu].alumno + '</td><td>'
						+ alumnos[id_alu].grado + alumnos[id_alu].secc
						+ '</td>' + tdsNotas
						+ '<td align="right" bgcolor="#aac3e4">'
						+ $.number(promFinal, 2) + '</td></tr>');

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
