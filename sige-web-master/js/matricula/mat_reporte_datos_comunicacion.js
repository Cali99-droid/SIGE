function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	
	/*$('#id_gra').on('change',function(event){
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
	});*/
	/*$('#id_gra').on(
	'change',
	function(event) {
		/*_llenarCombo('col_aula', $('#id_au'), null, $("#_id_anio")
				.text()
				+ ',' + $(this).val());*/
	/*	var param ={id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_gir:$('#id_gir').val()}
		_llenarComboURL('api/aula/listarAulasxGiroNivelGrado',$('#id_au'), null, param, function(){
			
		});
	});*/
	
	/*$('#id_nvl').on('change',function(event){
		
		
	});*/
	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val(), id_suc:$('#id_suc').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});
	
	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_nvl').on('change',function(event){
		var id_niv=$('#id_nvl').val();
		//_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
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
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});
	
	/*if (rol=='1' || rol=='2')
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	else{
		var param = { id_tra : _usuario.id_tra, id_anio : $('#_id_anio').text() };
		_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_nvl'),null, param, function() { $('#id_nvl').change(); });
	}*/
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
		
		/*var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});*/
	}); 
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null);
	} else {
		_llenarComboURL('api/usuario/listarLocales/'+_usuario.id,$('#id_suc'),null,null,function(){$('#id_suc').change();}); //
	}	
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
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
					   {"title": "DNI", "data" : "dni_fam"},
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
		var param={id_au:$('#id_au').val(), id_niv: $('#id_nvl').val(), id_grad: $('#id_gra').val(),id_anio:$('#_id_anio').text(),id_gir:$('#id_gir').val(),id_suc:$('#id_suc').val(),id_cic:$('#id_cic').val()};
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
		
	document.location.href = _URL + "api/archivo/exportarDirectorioPPFF?id_au=" + id_au+"&id_niv="+$("#id_nvl").val() + "&id_grad="+id_grad+"&usuario=" + _usuario.nombres + "&id_anio=" + $("#_id_anio").html()+"&id_gir="+$('#id_gir').val()+"&id_suc="+$('#id_suc').val()+"&id_cic="+$('#id_cic').val();
}

