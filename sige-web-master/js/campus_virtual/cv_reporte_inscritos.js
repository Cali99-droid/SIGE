function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	
	$('#id_gra').on('change',function(event){
 		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
 			//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
 			var param = {
 					id_gra : $('#id_gra').val(),
 					id_anio : $('#_id_anio').text()
 				};
 			console.log(param);
 			_llenarComboURL('api/grupoAulaVirtual/listarGrupos', $('#id_cga'),	null, param, function() {
					$('#id_cga').change();
				});
 		} else{
 			/*var param = {
 					id_tra : _usuario.id_tra,
 					id_grad : $(this).val(),
 					id_suc : $('#id_suc').val(),
 					id_anio : $('#_id_anio').text()
 				};
 			_llenarComboURL('api/comportamiento/listarAulaTutor', $('#id_au'),	null, param, function() {
 						$('#id_au').change();
 					});*/
 			var param = {
 					id_gra : $(this).val(),
 					id_anio : $('#_id_anio').text()
 				};
 			_llenarComboURL('api/grupoAulaVirtual/listarGrupos', $('#id_cga'),	null, param, function() {
					$('#id_cga').change();
				});
 		}		
	});
	
	$('#id_nvl').on('change',function(event){
		//if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
			_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
		/*} /*else{
			var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text(),
					id_niv : $("#id_nvl").val()
				};
			_llenarComboURL('api/comportamiento/listarGrados', $('#id_gra'),
				null, param, function() {
					$('#id_gra').change();
				});
		}*/
		
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
		
		$('#cv_reporte_inscritos_tab').dataTable({
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
	     	           {"title": "Sección (Aula Presencial)", "data" : "seccion"},
	     	           {"title": "Grupo Asignado", "data" : "grupo"},
	     	         //  {"title": "Parentesco", "data" : "parentesco"},
	     	           {"title": "Apoderado", "data" : "apoderado"},
	     	           {"title": "Correo", "data" : "correo"},
	     	           {"title": "Celular", "data" : "celular"},
	     	           {"title": "Usuario Google", "data" : "usr"},
	     	           {"title": "Contraseña", "data" : "psw"},
	     	           {"title": "Generar Usuario Google", "data" : "id","render":function ( data, type, row,meta ) { 
							if(row.usr==null)
		     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id_alu + '" onclick="generar_usuario(this, event)" class="list-icons-item" title="Generar usuario"><i class="icon-google-plus ui-blue ui-size" aria-hidden="true"></i></a> </div>';
							else
								return '';
		     	         }}
	     	           //{"title": "Correo", "data" : "fam_corr"},
	     	          /* {"title":"Vive con el alumno?", "render":function ( data, type, row,meta ) {
	     	        	   if(row.viv_alu==1)
	     	        		   return 'Si';
	     	        	   else if (row.viv_alu==0)
	     	        		   return 'No';
	     	        	   else
	     	        		   return '';
		        	 	} */
		        	 	//}
	        ],
	        "initComplete": function( settings ) {
	        	//$("<span><a href='#' target='_blank' onclick='printReporteDirectorio(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			 }

	    });
		
	};
	
	$('#frm-reporte-inscritos').on('submit',function(event){
		event.preventDefault();
		var param={id_cga:$('#id_cga').val(), id_niv: $('#id_nvl').val(), id_grad: $('#id_gra').val(),id_anio:$('#_id_anio').text(), inscripcion:$('#inscripcion').val()}; //id_au:$('#id_au').val()
		return _get('api/inscripcionCampus/listarInscritos',fncExito, param);
	});
	
	
	
});

function generar_usuario(obj,event){
	event.preventDefault();
	var id_alu = $(obj).data('id');	
	_POST({url:'/api/inscripcionCampus/generarUsuarioGoogle/' + id_alu+'/'+$('#_id_anio').text(), context:_URL,function(data){
		$('#frm-reporte-inscritos #btn-buscar').click();
	}
	});
	
}


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

