function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	$('#fec_ini').val('');
	$('#fec_fin').val('');
	
	
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
	
	if (_usuario.roles.indexOf(_ROL_SECRETARIA)>-1 ){
		$("#rat").val("NR").change();
		$("#rat").attr("disabled", "disabled");
	}	
	
	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	
	
	/*$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});*/
	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_nvl').on('change',function(event){
		var id_niv=$('#id_nvl').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); 
		
	/*_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});*/
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$("#_id_anio").text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}

	$('#rep_com').on('click',function(){
	   if(this.checked){
		 $('#rep_com').val(1);
	   } else {
		 $('#rep_com').val(0);
	   }	   
	});
	
}



$(function(){
	_onloadPage();	

	
	
});
var fncExito = function(data){
		
		$('#mat_reporte_matriculados').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 scrollY:        true,
			 scrollX:        true,
			 scrollCollapse: true,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	     	           //{"title": "Nro Doc", "data" : "nro_doc"},
	     	           {"title": "Alumno", "data" : "alumno"},
					   {"title": "Nivel", "data" : "nivel"},
					   {"title": "Grado", "data" : "grado"},
	     	           {"title": "Sección", "data" : "secc"},
					   {"title": "Fam. Res.", "data" : "familiar"},
					   {"title": "Celular", "data" : "cel"},
					   {"title": "Correo", "data" : "corr"},
					   {"title": "Continuará en el Colegio?","render":function ( data, type, row ) {
	    	        	   if (row.res==null){
							return ' ';
	    	        	   } else{
								if(row.res=="1"){
									return "<a href='#' onclick='actualizarRatificacion(\"" + row.alumno + "\"," + row.id_alu + "," +  row.id_grad  + "," +  row.id_mat  + "," +  row.id_rat  + "," +  row.id_suc  + "," + row.res +  ")'>SÍ</a>";
								} else if(row.res=="0"){
									return "<a href='#' onclick='actualizarRatificacion(\"" + row.alumno + "\"," + row.id_alu + "," +  row.id_grad  + "," +  row.id_mat  + "," +  row.id_rat  + "," +  row.id_suc  + "," + row.res +  ")'>NO</a>";
								}
	    	        	   }
					   }}     	           
	        ],
	        "initComplete": function( settings ) {

	        	//$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
function actualizarRatificacion(alumno,id_alu,id_grad,id_mat,id_rat,id_suc,res){
	
	var onShowModal = function(){
		$('#mat_ratificacion-frm #alumno').val(alumno);
		$('#mat_ratificacion-frm #id_alu').val(id_alu);
		$('#mat_ratificacion-frm #id_grad').val(id_grad);
		$('#mat_ratificacion-frm #id_mat').val(id_mat);
		$('#mat_ratificacion-frm #id_rat').val(id_rat);
		$('#mat_ratificacion-frm #id_suc').val(id_suc);
		$('#mat_ratificacion-frm #res').val(res).change();

	};
	var onSuccessSave = function(){
		//alert('onSuccessSave');
		$('#btn-cancelar').click();
		$('#btn-buscar').click();
		
	};
	
	_modal("Actualizar Ratificación", 'pages/matricula/mat_ratificacion.html',onShowModal,onSuccessSave);
	
}
	
	
$('#frm-reporte-matriculados').on('submit',function(event){
		event.preventDefault();
		
	
		var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val(), rat:$('#rat').val(), id_cic:$('#id_cic').val(), fec_ini:$('#fec_ini').val(), fec_fin:$('#fec_fin').val()};
		return _get('api/matricula/reporteRatificacion',fncExito, param);
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

