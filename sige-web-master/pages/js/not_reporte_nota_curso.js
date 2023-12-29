//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
var list;
function onloadPage(params) {
	
	$('#id_gra').on('change',function(event){
		var param ={id_suc:_usuario.id_suc,id_anio:$("#_id_anio").text(),id_grad:$('#id_gra').val()};
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_ASISTENTE)>-1){
			_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $('#id_gra').val(),function() {$('#id_au').change();});
		} else{
			_llenar_combo({
			   	url:'api/aula/listarAulasxLocal',
				combo:$('#id_au'),
				params: param,
				context:_URL,
				text:'secc',
				funcion:function(){
				
				}
			});
		}
		
		
		var param = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null, param,function(){
		 	$('#id_cpu').append(new Option('Promedio Final', '0', true, true));

		});
	});
	
	
	$('#id_niv').on('change',function(event){
		var param ={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_ASISTENTE)>-1){
		_llenarCombo('cat_nivel',$('#id_niv'), null,null,function(){ $('#id_niv').change()});
	}else{
		var param ={id_usr:_usuario.id}
		_llenar_combo({
		   	url:'api/usuario/listarNiveles',
			combo:$('#id_niv'),
			params: param,
			context:_URL,
			text:'nivel',
			funcion:function(){
				$('#id_niv').change();
			}
		});
	}
	
	
}

$('#buscar-frm').on('submit',function(event){
	event.preventDefault();//no referesca
	listar_tabla();
});

function listar_tabla(){
	$('#id_niv').removeAttr('disabled');
	var param = {id_anio: $('#_id_anio').text(),id_gra:$('#id_gra').val(),id_niv:$('#id_niv').val(),id_au:$('#id_au').val(), nump:$('#id_cpu').val()};
	
	//columna dinamica
	
	
	_get('api/nota/listarNotasCurso/', function(data){

		pirtarTabla(data, "sorting", "sorting", "sorting");
		$('#id_niv').prop('disabled', 'disabled');		
	}, param);

}



function pirtarTabla(data,clazzAlu, clazzProm, clazzPunt){
	list = data;

 
		$('#not_reporte_aula-tabla').find('thead').html('');
		$('#not_reporte_aula-tabla').find('tbody').html('');
 
		var cont=0;
		var alumnos = data.alumnos;
		var cursos = data.cursos;
		
		console.log(cursos);

		//INICIO - header de la tabla
		//CURSOS
		var td = '';
		for (var key in cursos) {
			td = td + '<th class="rotate"><div><span>' + cursos[key].value + '</span></div></th>';
		}
		td = td + '<th class="rotate"><div><span>Cursos desaprobados</span></div></th>';
		
		var fila = '<tr><th> Nro</th><th>DNI</th><th onclick="javascript:order(\'Alu\',this)" class="'
			+ clazzAlu
			+ '" data-sort="0" tabindex="0"  >Alumno</th><th class="rotate"><div><span>GRADO</span></div></th>' + td ;
		
		if($('#id_niv').val()=='2' || $('#id_niv').val()=='3')
			fila = fila + '<th onclick="javascript:order(\'Punt\',this)" class="'
				+ clazzPunt
				+ '" data-sort="0" tabindex="0" >Puntaje</th>';
		
		fila +='<th onclick="javascript:order(\'Prom\',this)" class="'
			+ clazzProm
			+ '" data-sort="0" tabindex="0" >Promedio</th>';
	
		
		fila = fila + '</tr>';
		
		$('#not_reporte_aula-tabla').find('thead').append(fila);

		
		//body de la tabla
		for (var key in alumnos) {
			var cursosDesaprobados=0;
			
			cont++;
			var id_alu = key;
			var cursos_alu = alumnos[id_alu].cursos;
			var tdsNotas ='';
			var suma = 0;
			for (var id_cur in cursos) {
				
				//var promedio = cursos[id_cur];
				var promedio = cursos_alu[cursos[id_cur].id];
				suma = suma + promedio;
				if($('#id_niv').val()=='1'){// PENDIENTES LINA II
					if(promedio!=''){
						if(promedio=='4')
							tdsNotas = tdsNotas  + '<td>AD</td>';
		    			  else if(promedio=='3')
		    				  tdsNotas = tdsNotas  + '<td>A</td>';
		    			  else if(promedio=='2')
		    				  tdsNotas = tdsNotas  + '<td>B</td>';
		    			  else if(promedio=='1')
		    				  tdsNotas = tdsNotas  + '<td>C</td>';
		    			  else
		    				  tdsNotas ='';
					}
				} else{
					if(promedio<10){
						tdsNotas = tdsNotas  + '<td>0' + promedio  + '</td>';
					}else{
					
						if(promedio<11){
							tdsNotas = tdsNotas  + '<td style="color:red">' + promedio  + '</td>';
							cursosDesaprobados++;
						}else
							tdsNotas = tdsNotas  + '<td>' + promedio  + '</td>';
						
					}
				}		
			}
			var promFinal =$.number(suma/Object.keys(cursos).length,2);
			console.log($.number(promFinal,0));
			if($('#id_niv').val()=='1'){
				if(promFinal!=''){
					if($.number(promFinal)<='1')
						 promFinal ='C';
					else if($.number(promFinal)>'1' && $.number(promFinal)<='2')
						 promFinal ='B';
					else if($.number(promFinal)>'2' && $.number(promFinal)<='3')
						promFinal ='A';
					else if($.number(promFinal)>'3' && $.number(promFinal)<='4')
						promFinal ='AD';
					else
	    				promFinal ='';
				}
			}
			alumnos[id_alu].promedio = promFinal;
			tdsNotas = tdsNotas  + '<td>' + cursosDesaprobados  + '</td>';

			
			if($('#id_niv').val()=='2' || $('#id_niv').val()=='3'){
				
				var puntaje = alumnos[key].puntaje;

				tdsNotas = tdsNotas + '<td>' + puntaje + '</td>';

			}
			
			
			$('#not_reporte_aula-tabla').find('tbody').append('<tr><td>' + cont + '</td><td>'+alumnos[id_alu].nro_doc+'</td><td>' + alumnos[id_alu].alumno  + '</td><td>' + alumnos[id_alu].grado +alumnos[id_alu].secc + '</td>'  + tdsNotas + '<td align="right" bgcolor="#aac3e4">' +promFinal +'</td></tr>');
						
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
	}else if(columna == "Alu") {
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
	}else {
		if (clazz == "sorting_asc") {
			arreglo.sort(order_punt_desc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting", "sorting","sorting_desc");
		} else {
			console.log(arreglo);
			arreglo.sort(order_punt_asc);
			list.alumnos = arreglo;
			pirtarTabla(list, "sorting", "sorting","sorting_asc");
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
	
	function order_punt_asc(a, b) {
		if (a.puntaje < b.puntaje)
			return -1;
		if (a.puntaje > b.puntaje)
			return 1;
		return 0;
	}

	function order_punt_desc(a, b) {
		if (a.puntaje > b.puntaje)
			return -1;
		if (a.puntaje < b.puntaje)
			return 1;
		return 0;
	}

