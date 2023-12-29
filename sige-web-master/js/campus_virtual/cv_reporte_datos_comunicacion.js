function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	
	$('#id_gra').on('change',function(event){
 		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
 			_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
 		} else{
 			var param = {
 					id_tra : _usuario.id_tra,
 					id_grad : $(this).val(),
 					id_suc : $('#id_suc').val(),
 					id_anio : $('#_id_anio').text()
 				};
 			_llenarComboURL('api/comportamiento/listarAulaTutor', $('#id_au'),	null, param, function() {
 						$('#id_au').change();
 					});
 		}		
	});
	
	$('#id_nvl').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
			_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
		} else{
			var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text(),
					id_niv : $("#id_nvl").val()
				};
			_llenarComboURL('api/comportamiento/listarGrados', $('#id_gra'),
				null, param, function() {
					$('#id_gra').change();
				});
		}
		
	});
	
	if (rol=='1' || rol=='2')
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	else{
		var param = { id_tra : _usuario.id_tra, id_anio : $('#_id_anio').text() };
		_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_nvl'),null, param, function() { $('#id_nvl').change(); });
	}
}



$(function(){
	_onloadPage();	

	var fncExito = function(data){
		
		$('#mat_reporte_datos_comunicacion').dataTable({
			 data : data,
			 //aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	     	           {"title": "Alumno", "data" : "alumno"},
	     	           {"title": "Nivel", "data" : "nivel"},
	     	           {"title": "Grado", "data" : "grado"},
	     	           {"title": "Sección", "data" : "seccion"},
	     	           {"title": "Parentesco", "data" : "parentesco"},
	     	           {"title": "Familiar", "data" : "familiar"},
	     	           {"title": "Celular", "data" : "fam_cel"},
	     	           {"title": "Dirección", "data" : "fam_dir"},
	     	           {"title": "Correo", "data" : "fam_corr"},
	     	           {"title":"Vive con el alumno?", "render":function ( data, type, row,meta ) {
	     	        	   if(row.viv_alu==1)
	     	        		   return 'Si';
	     	        	   else if (row.viv_alu==0)
	     	        		   return 'No';
	     	        	   else
	     	        		   return '';
		        	 	} 
		        	 	}
	        ],
	        "initComplete": function( settings ) {
	        	$("<span><a href='#' target='_blank' onclick='printReporteDirectorio(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			 }

	    });
		
	};
	
	$('#frm-reporte-datos_comunicacion').on('submit',function(event){
		event.preventDefault();
		var param={id_au:$('#id_au').val(), id_niv: $('#id_nvl').val(), id_grad: $('#id_gra').val(),id_anio:$('#_id_anio').text()};
		return _get('api/matricula/listarDatosComunicacionXAula',fncExito, param);
	});
	
});

function printReporteDirectorio(event){
	event.preventDefault();
	var id_au="";
	var id_grad="";
	if($("#id_au").val()!=null && $("#id_au").val()!=""){
		id_au=$("#id_au").val();
	}

	if($("#id_gra").val()!=null && $("#id_gra").val()!=""){
		id_grad=$("#id_gra").val();
	}
		
	document.location.href = _URL + "api/matricula/exportarDirectorioPPFF?id_au=" + id_au+"&id_niv="+$("#id_nvl").val() + "&id_grad="+id_grad+"&usuario=" + _usuario.nombres + "&id_anio=" + $("#_id_anio").html();
}

