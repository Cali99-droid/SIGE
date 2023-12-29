function onloadPage(params){
	var id_usr=_usuario.id;
	var rol = _usuario.roles[0];
	if (rol!='1'){
		$('#frm-pagos-buscar #id_usr').val(id_usr);
	}	
	
	$('#id_per').on('change', function(event) {
		//limpiarTabla();
		
		var param1 = {id_per : $('#id_per').val() , id_anio: $('#_id_anio').text(), id_gir : $('#id_gir').val()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Alumno',
			$('#id_cpu'), null, param1, function() {
				
				$('#id_cpu').change();
				listar_tabla();
		});

	});
	
	$('#id_gir').on('change', function(event) {
		var param = {id_usr:id_usr};
		_llenarComboURL('api/familiar/listarHijosFamilia', $('#id_per'),null, param, function() {
			$('#id_per').change();
		});	
	});	
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
	$('#id_gir').change();	
	});
}

function listar_tabla(){
	var param = {id_anio: $('#_id_anio').text(),id_cur:$('#id_cur').val(),id_niv:$('#id_au option:selected').attr("data-aux2"),id_gra:$('#id_au option:selected').attr("data-aux1"),id_au:$('#id_au').val(), id_dcare:$('#id_area option:selected').attr("data-aux1"),id_cpu:$('#id_cpu').val()};
	
	//columna dinamica
	
	_get('api/nota/listarNotasAlumnoPeriodo/',
			function(data){
			if($('#id_cur').val()=="T"){
				$('#btn-agregar').hide();
			} else {
				$('#btn-agregar').show();
			}	
//este es, se qued
		__periodos =data.periodos;
		__areas =data.areas;
		var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					//	{"title": "DNI","data": "nro_doc"},
						{"title": "Áreas","data": "areas"}];
						//{"title": "Sub-tema","data": "nom"}];
		var i=0;
		
		//limpiamos la tabla
		$('#not_reporte_notas_alumno').find('thead').html('');
		$('#not_reporte_notas_alumno').find('tbody').html('');

		for (var key in data.periodos) {
			i++;					
			var titulo = data.periodos[key];
			var id_per = titulo.id;
			
			var element = {"title": titulo.nom,"data": "per" + titulo.id,
					"render": function ( data, type, row, meta ) {
						
						}
				
					};
			
			columns.push(element);
			
		}
		var cont=0;
		var alumnos = data.alumnos;
		var periodos = data.periodos;
		var capacidades = data.capacidades;
		var desempenios = data.desempenios;
		var cursos = data.cursos;
		
		

		//INICIO - header de la tabla
		//COMPETENCIAS
		var td = '';
	/*	var id_com = '';
			for (var y in periodos) {
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
			}*/
		$('#not_reporte_notas_alumno').find('thead').append('<tr><th bgcolor="#AED6F1"> Nro</th><th bgcolor="#AED6F1">Área</th>' + td  + '<tr>');
		

	
		
				
		var param={id_anio:$('#_id_anio').text() , id_gra:$('#id_au option:selected').attr("data-aux1"), id_adc:$('#id_area option:selected').attr("data-aux1")};

		for (var key in areas_competencia) {
			//alert();
			cont++;
			var id_area = key;
			var competencias = areas_competencia[id_area].competencias;
			var area_nom = areas_competencia[id_area].nom_area;
			var id_alumno=alumnos[id_alu].id_alu;
			var competenciaSuma =alumnos[id_alu].competenciaSuma;
			var notasCur = alumnos[id_alu].notasCursos;
			var tdsNotas= "";
			var tdsPromComp="";
			tdsNotas = tdsNotas  + "<td><label style='font-size: 9pt; font-weight: bold; color: "+color+"; text-align: center;' class='form-control'>"+area_nom+"</label></td>";
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
					$('#not_reporte_aula-tabla').find('tbody').append('<tr><td >' + cont + '</td><td>' + alumnos[id_alu].alumno  + '</td>'  + tdsNotas + '<td bgcolor="#aac3e4" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><label for="'+id_alumno+'" id="'+id_alumno+'" style="font-size: 9pt; font-weight: bold; color: black; text-align: center;"><font style="text-align: center;">'+ Math.round(alumnos[id_alu].promedioGeneral) +'</font></label></td></tr>');
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
		
		
			$('[data-popup="tooltip"]').tooltip();
				
			}, param
	);

}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$("#div_pago").css('display','block');
		var pagos=data;
		$('#panel-alumnos').css("display","block");
		for (var i=0;i<data.length-1;i++){
					//agregar el mes a la sección alumno
			var last_div_pago= ($("#div_pago").last()).clone();
			
			$( "#pagos_resumen" ).append(last_div_pago);
		}
		$(".div_pago").each(function (index, value) {
			console.log($(this).html());		
			$(this).find("#concepto").text(data[index].concepto);
			if(data[index].canc==1){
				$(this).removeClass("bg-danger-300");
				$(this).addClass("bg-green-200");
				$(this).find("#fecha").text('Fecha de Pago: ');
				$(this).find("#lbl_fecha_est").text(_parseDate(data[index].fec_pago));
				$(this).find("#estado").text('CANCELADO: ');
				$(this).find("#lbl_monto").text(data[index].monto_total);
				$(this).find("#div_impresion").css('display','block');
				$(this).find("#btnImprimir").attr("data-id_fmo", data[index].id_fmo);
				$(this).find("#btnImprimir").attr("data-id_alu", data[index].id);
			} else if(data[index].canc==0){
				$(this).removeClass("bg-green-200");
				$(this).addClass("bg-danger-300");
				$(this).find("#fecha").text('Fecha de Vencimiento: ');
				$(this).find("#lbl_fecha_est").text(_parseDate(data[index].fec_venc));
				$(this).find("#estado").text('PENDIENTE: ');
				var monto_final=0;
				var monto=data[index].monto;
				var beca=data[index].desc_beca;
				if(beca!=null)
					monto_final=monto-beca;
				else
					monto_final=monto;
				$(this).find("#lbl_monto").text(monto_final);
				$(this).find("#div_impresion").css('display','none');
			}	
		});	
		/*for(var k in pagos) {
			var detallePago = '<div class="form-group div-detalle-pago"> '
				+ '<label class="control-label col-lg-8">Mensualidad de mensualidad_desc, alumno</label>'
				+ '<div class="col-lg-2">'
				+ '<span class="label label-success position-right">S/.<detalle_mes>mensualidad</detalle_mes></span>'
				+ '</div>'
				+ '<div class="col-lg-2">&nbsp;</div>' + '</div>';
		$("#frm-pagos-resumen-mes").append(detallePago);		
		$('#panel-alumnos').css("display","block");
		}*/	
		
		
		/*$('#tabla-alumno_pagos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           //{"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
					   {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
	    	        	   if (row.tipo=="I")
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   else 
	    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   }
	     	           {"title": "Usuario", "data" : "login"}, 
					   {"title": "Recibo", "data" : "nro_rec"}, 
	    	           {"title": "Concepto", "data" : "concepto"},
	    	           {"title": "Fecha", "data" : "fecha"},
	     	           {"title": "Alumno/Observacion", "data" : "obs"}, 
	     	           {"title": "Nivel", "data" : "nivel"}, 
	     	           {"title": "Grado", "data" : "grado"}, 
	     	           {"title": "Seccion", "data" : "secc"}, 
	    	           /*{"title": "Tipo", "data" : "tipo","render":function ( data, type, row,meta ) {
	    	        	   if (data =="I")
	    	        		   return "Ingreso"
	    	        	   else
	    	        	       return "Salida"
	    	           		}
					   },*/
	    	/*           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
	    	        	   if (row.tipo=="I")
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   else 
	    	        		   return '<font color="red">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   }
	        ],
	        "initComplete": function( settings ) {
	        	_dataTable_total(settings,'monto_total');
				//_initCompleteDT(settings);
				/*var table = $('#tabla-alumnos').DataTable();
				table.on( 'search.dt', function () {
					_dataTable_total(settings,'monto_total');
					//alert();
				//$('#filterInfo').html( 'Currently applied global search: '+table.search() );
				} );*/
	        	
	       /* 	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });*/
		
	};
	
	/*$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		//$("#div_pago").last().remove();
		$("#div_pago").css('display','none');
		$("#id_suc").val($("#_id_suc").html());
		var param = {id_anio_des: $("#id_anio_des").val(), id_per: $("#id_per").val()}
		//return _get('api/alumno/listarPagosxAlumno',fncExito, $(this).serialize());
		return _get('api/alumno/listarPagosxAlumno',fncExito, param);
	});*/
	

	
});

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	var id_alu = $(o).data('id_alu');
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo+"/"+id_alu;

}
