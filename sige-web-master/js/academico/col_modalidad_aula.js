function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	if (rol!='1'){
		$('#id_au').empty();
	}
	
	$('#id_suc').on('change',function(event){
		//_llenarCombo('ges_servicio',$('#id_nvl'),null,$(this).val(),function(){$("#id_nvl").change()});
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	});
	//_llenarCombo('cat_nivel',$('#id_niv'));
	$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});


	$('#id_gra').on('change',function(event){
		var id_suc=$("#id_suc").val();
		_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
	});
	_llenarCombo('ges_sucursal',$('#id_suc'),null,null, function(){$("#id_suc").change()});
	
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
	
	/*$('#id_grad').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	*/
	
	/*$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});*/
	/*$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_nvl').on('change',function(event){
		var id_niv=$('#id_nvl').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	
	$('#id_gir').on('change',function(event){
		if (rol=='1'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); 
		
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});

	_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
		$('#id_suc').change();
	});*/
	
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
			 scrollY:        false,
			         scrollX:        true,
			         scrollCollapse: true,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	     	           {"title": "Nro Doc", "data" : "nro_doc"},
	     	           {"title": "Alumno", "data" : "Alumno"},
					   {"title": "Procedencia", "data" : "cod_mod"},
					   {"title": "Usuario Google", "data" : "usuario"},
	     	           {"title": "Contraseña", "data" : "pass_google"},
					   {"title": "Generar", "data" : "id","render":function ( data, type, row,meta ) { 
					   if(row.tipo!='C'){
						   if(row.pass_google!=null && row.id_classRoom!=null)
		     	        	  	return ''; 
							else
								return '<div class="list-icons"> <a href="#" data-id_mat="' + row.id + '" data-id_alu="' + row.id_alu + '" onclick="generar_usuario(this, event)" class="list-icons-item" title="Generar usuario"><i class="icon-google-plus ui-blue ui-size" aria-hidden="true"></i></a> </div>';
					   } else if(row.tipo=='C'){
						  if(row.pass_google==null && row.estado=="Cancelado")
								return '<div class="list-icons"> <a href="#" data-id_mat="' + row.id + '" data-id_alu="' + row.id_alu + '" onclick="generar_usuario_colegio(this, event)" class="list-icons-item" title="Generar usuario"><i class="icon-google-plus ui-blue ui-size" aria-hidden="true"></i></a> </div>';  	
							else
								return ''; 
					   }	
							
		     	         }},
						 {"title": "Grado", "data" : "grado"},
	     	           {"title": "Sección", "data" : "secc"},
					   {"title": "Familiar Responsable", "data" : "Persona_Responsable"},
					   //{"title": "Parentesco", "data" : "parentesco"},
					   {"title": "Parentesco","render":function ( data, type, row ) {
	    	        	   if (row.parentesco==null)
	    	        		   return 'Mismo Alumno';
	    	        	   else 
	    	        		   return row.parentesco;
	    	        	   }
					   },
					   {"title": "Celular Responsable", "data" : "Celular_Responsable"},
					   {"title": "Correo Responsable", "data" : "Correo_Responsable"},
					   {"title": "Provincia", "data" : "provincia"},
					   {"title": "Distrito", "data" : "distrito"},
	     	           //{"title": "Nro Doc. Papa", "data" : "DNIPadre"},
	     	           //{"title": "Nro Doc. mama", "data" : "DNIMadre"},
	     	           
					   {"title": "Fecha","render":function ( data, type, row ) {
	    	        		   return _parseDate(row.fecha);
					   }},
	     	           {"title": "Nro. Recibo", "data" : "nro_rec"},
					   {"title": "Estado","render":function ( data, type, row ) {
	    	        	   if (row.estado=="Cancelado")
	    	        		   return '<font color="blue">'+row.estado+'</font>';
	    	        	   else 
	    	        		   return '<font color="red">'+row.estado+'</font>';
	    	        	   }
					   }
					  /* {"title": "Fec. Venc","render":function ( data, type, row ) {
	    	        	   if (row.estado=="Pendiente")
	    	        		   return row.fec_venc;
	    	        	   else 
	    	        		   return null;
	    	        	   }
					   },*/
					  // {"title": "Estado", "data" : "estado"},
					  // {"title": "Fec. Venc", "data" : "fec_venc"},
	     	         //  {"title": "Procedencia", "data" : "cod_mod"},
	     	           
	        ],
	        "initComplete": function( settings ) {

	        	//$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	
$('#frm-reporte-matriculados').on('submit',function(event){
		event.preventDefault();
		
	
		var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val(), rep_com:$('#rep_com').val(), id_cic:$('#id_cic').val()};
		return _get('api/matricula/reporteMatriculaList',fncExito, param);
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

