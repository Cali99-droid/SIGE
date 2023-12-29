//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
function onloadPage(params) {
	
	$('#id_cur').on('change',function(event) {
		var param = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null, param);

	});
	
	$('#id_au').on('change',function(event){
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			var param = {id_anio:$('#_id_anio').text(), id_au:$('#id_au').val()};
			_llenarComboURL('api/cursoAnio/listarCursosAula',$('#id_cur'),null,param, function(){$('#id_cur').change()});
		} else{
			var param1={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val(), id_gra:$("#id_gra").val()}
			_llenarComboURL('api/evaluacion/listarCursos',$('#id_cur'), null, param1,function(){$('#id_cur').change();});
		}
	});
	
	$('#id_gra').on('change',function(event){
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $('#id_gra').val(),function() {$('#id_au').change();});	
		} else{
			var param={id_tra:_usuario.id_tra, id_gra: $('#id_gra').val(),id_anio: $('#_id_anio').text(), id_niv: $('#id_niv').val()};
			_llenarComboURL('api/evaluacion/listarAulasProfesor',$('#id_au'),null,param,function(){$('#id_au').change()});	
		}	
	});
	

	$('#id_niv').on('change',function(event){
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		  var param ={id_niv:$(this).val()};
		 _llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
		}else {
		  var param2={id_tra:_usuario.id_tra,id_anio: $('#_id_anio').text(),id_niv: $("#id_niv").val()};
		 _llenarComboURL('api/evaluacion/listarGrados',$('#id_gra'),null, param2,function(){$('#id_gra').change()});	
		}
	});
	
	if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		_llenarCombo('cat_nivel',$('#id_niv'), null,null,function(){ $('#id_niv').change()});
	} else{
		var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};
		_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),null,param,function(){$('#id_niv').change();});	
	}
	
}

$('#buscar-frm').on('submit',function(event){
	event.preventDefault();//no referesca
	listar_tabla();
});

function listar_tabla(){
	
	var param = {id_anio: $('#_id_anio').text(),id_cur:$('#id_cur').val(),id_niv:$('#id_niv').val(),id_au:$('#id_au').val(), nump:$('#id_cpu').val()};
	
	//columna dinamica
	
	
	_get('api/nota/listarNotasAula/',
			function(data){
//este es, se qued
		__capacidades =data.capacidades;
		__indicadores =data.indicadores;
		var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title": "DNI","data": "nro_doc"},
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

		//INICIO - header de la tabla
		//COMPETENCIAS
		var td = '';
		for (var key in competencias) {
			td = td + '<th colspan="' + (competencias[key].count + 1) + '">' + competencias[key].nom  + '</th>';
		}
		$('#not_reporte_aula-tabla').find('thead').append('<tr><th> Nro</th><th>DNI</th><th>Alumno</th>' + td  + '<th>Promedio</th><tr>');

		//INICIO - header de la tabla
		//CAPACIDADES
		td = '';
		var com = '';
		var i=1;
		for (var key in capacidades) {
			if(com!='' && capacidades[key].com_nom!=com)
				td =  td + '<th >PROM.</th>';
			td = td + '<th data-popup="tooltip" title="' +  capacidades[key].nom + '" data-placement="top">C' + i + '</th>';
			com = capacidades[key].com_nom;
			i++;
		}
		td =  td + '<th >PROM.</th>';
		$('#not_reporte_aula-tabla').find('thead').append('<tr><th> Nro</th><th>DNI</th><th>Alumno</th>' + td  + '<th>Promedio</th><tr>');

		
		//body de la tabla
		var first=true;
		for (var key in alumnos) {
			cont++;
			var id_alu = key;
			var notas = alumnos[id_alu].nota;
			var competenciaSuma =alumnos[id_alu].competenciaSuma;
			var tdsNotas= "";
			var tdsPromComp="";
			for (var keyNotas in notas) {
				var id_com = notas[keyNotas].id_com;
				
				if(notas[keyNotas].nota<10)
						tdsNotas = tdsNotas  + '<td>0' + notas[keyNotas].nota  + '</td>';	
				else
						tdsNotas = tdsNotas  + '<td>' + notas[keyNotas].nota  + '</td>';
			}
			if( Math.round(alumnos[id_alu].promedioGeneral)<10)
				$('#not_reporte_aula-tabla').find('tbody').append('<tr><td>' + cont + '</td><td>'+alumnos[id_alu].nro_doc+'</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4">0'+ Math.round(alumnos[id_alu].promedioGeneral) +'</td></tr>');
			else	
				$('#not_reporte_aula-tabla').find('tbody').append('<tr><td>' + cont + '</td><td>'+alumnos[id_alu].nro_doc+'</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</td></tr>');
			
			if(true){
				//first=false;
				//console.log(competenciaSuma);
				
				var columnas=1;
				for (var key in competenciaSuma) {//aca las 3 competencias
					//obtener el nro de notas q tiene la competencia
					var num_notas = 0;
					var suma_notas = Math.round(competenciaSuma[key]);
					
					for (var key1 in competencias) {
						if (competencias[key1].com_id==key)
							num_notas = competencias[key1].count;
						
					}
					columnas = columnas + num_notas+1;

					if(suma_notas<10)
					$('#not_reporte_aula-tabla > tbody:last tr:last td:eq(' + columnas + ')').after('<td bgcolor="#ffca65">0' +suma_notas + '</td>');
					else
					$('#not_reporte_aula-tabla > tbody:last tr:last td:eq(' + columnas + ')').after('<td bgcolor="#ffca65">' +suma_notas + '</td>');	
					
				}
			}
		}
			$('[data-popup="tooltip"]').tooltip();
				
			}, param
	);

}





