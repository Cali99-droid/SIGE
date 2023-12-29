function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var rol = _usuario.roles[0];
	 
	
	/*if (rol!='1'){
		$('#id_au').empty();
	}*/
	
	/*if (rol=='1')
		_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
	else
		_llenarCombo('ges_servicio',$('#id_nvl'),null,_usuario.id_suc,function(){$("#id_nvl").change()});*/

		//_llenarCombo('cat_nivel',$('#id_nvl'),null,null,function(){$("#id_nvl").change()});
 
	//_llenarCombo('cat_nivel',$('#id_niv'));
	
	/*$('#id_gra').on('change',function(event){

		if (rol=='1'){
			var param={id_gra:$(this).val(),id_anio:$("#_id_anio").text()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
			//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
		}else{
			var param={id_suc:_usuario.id_suc ,id_gra:$(this).val(),id_anio:$("#_id_anio").text()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
			//_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+_usuario.id_suc);
		}	
			

		
	}); 
	
	$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});
	
	$('#id_gir').on('change',function(event){
		if (rol=='1'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	
	}); */
		
	_llenarCombo('ges_giro_negocio',$('#id_gir'),null,null,function(){
		$('#id_gir').change();
	});
	
}



$(function(){
	_onloadPage();	

	
	
});
var fncExito = function(data){
		
		$('#mat_reporte_matriculasvalnoval').dataTable({
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
	     	           {"title": "Nro Contrato", "data" : "num_cont"},
					   {"title": "Familia", "data" : "familia"},
	     	           {"title": "Responsable de Matrícula", "data" : "familiar"},
					   {"title": "DNI", "data" : "nro_doc"},
	     	           {"title": "Dirección", "data" : "dir"},
					   {"title": "Banco", "data" : "banco"},
					   {"title": "Email", "data" : "corr"},
					   {"title": "Celular", "data" : "cel"},
					   {"title":"Contrato", "render": function ( data, type, row ) {
						   if(row.env_doc!=null){
							 return '<a href="#" data-num_cont="' +  row.num_cont + '"  data-fam="' +  row.familia + '" onclick="descargar_contrato(event,this)" title="Descargar Contrato"><i class="icon-file-pdf mr-3 icon-2x" style="color:red"></i></a>'; 
						   } else {
							 return '';
						   }   
					   }},
					   {"title":"Declaracion", "render": function ( data, type, row ) {
						   if(row.env_decla!=null){
							 return '<a href="#" data-num_cont="' +  row.num_cont + '"  data-fam="' +  row.familia + '" onclick="descargar_declaracion(event,this)" title="Descargar Declaracion"><i class="icon-file-pdf mr-3 icon-2x" style="color:red"></i></a>'; 
						   } else {
							 return '';
						   }   
					   }},
					   {"title":"Protocolo", "render": function ( data, type, row ) {
						   if(row.env_pro!=null){
							 return '<a href="#" data-num_cont="' +  row.num_cont + '"  data-fam="' +  row.familia + '" onclick="descargar_protocolo(event,this)" title="Descargar Protocolo"><i class="icon-file-pdf mr-3 icon-2x" style="color:red"></i></a>'; 
						   } else {
							 return '';
						   }   
					   }},
					   {"title": "Validar", "data" : "id","render":function ( data, type, row,meta ) { 
							if(row.con_val!=null)
		     	        	  	return ''; 
							else
								return '<div class="list-icons"> <a href="#" data-id_fam="' + row.id_fam+ '" onclick="validar_contrato(this, event)" class="list-icons-item" title="Validar Contrato"><i class="icon-file-check ui-blue ui-size" aria-hidden="true"></i></a> </div>';
		     	         }},
						 {"title": "Usuario Valida", "data" : "trabajador"}	     	         
	     	           
	        ],
	        "initComplete": function( settings ) {

	        	//$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

			 }

	    });
		
	};
	
	
function subir_contrato(e,o) {
	e.preventDefault();
	//var form = $(o).data('nom');
	var id_fam = $(o).data('id_fam');

	//e.preventDefault();
	//var id_fam = $(obj).data('id');
	//document.location.href = _URL + "api/matricula/generarContrato?id_fam="+id_fam+"&id_anio="+$('#_id_anio').text();
//alert('#' + nombreform);
   // var form = $('#' + nombreform)[0];
	 var form = $('#F_1')[0]
	 var file1 =  $('#file1')[0].files[0];
   
	
    var data = new FormData(form);
	 data.append('file', file1); 
	 
	 if(file1==null){
		 swal({
		  title: "ATENCIÓN",
		  html:false,
		  type : "warning",
		  text: "No ha seleccionado ningun documento. Por favor, seleccione el contrato correctamente llenado y firmado a enviar.",
		  icon: "info",
		  button: "Cerrar",
		});
		
	 }	 else {
		    // var url = _URL_OLI +  'api/resultados/xls/upload';1
			var url = _URL+ 'api/archivo/subirContrato/'+id_fam+'/'+$('#_id_anio').text();

			$('#btn-procesar').prop("disabled", true);
			
			 $.blockUI({ 
					message: '<i class="icon-spinner4 spinner"></i>',
					//timeout: 2000, //unblock after 2 seconds
					overlayCSS: {
						backgroundColor: '#1b2024',
						opacity: 0.8,
						zIndex: 1200,
						cursor: 'wait'
					},
					css: {
						border: 0,
						color: '#fff',
						padding: 0,
						zIndex: 1201,
						backgroundColor: 'transparent'
					}
				});    

			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: url,
				data: data,
				processData: false, //prevent jQuery from automatically transforming the data into a query string
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {
					 $.unblockUI();
					 
					console.log(data);
					var error = data.code;
					
					if (error==null){
						
						$("#div_completado_F1").css('display','block');

						//lectora_listar_tabla(data.result);
							
					}else{
						console.log("Error al subir archivo");
						 $("#div_completado_F1").css('display','none');
						 swal({
								title : "Error al procesar el archivo",
								text : data.msg,
								confirmButtonColor : "#2196F3",
								type : "error"
							});
					}
						
					
					$('#btn-procesar').prop("disabled", false);

				},
			   
				error: function (e) {
					 $.unblockUI();
					$("#result").text(e.responseText);
					console.log("ERROR : ", e);
					$("#btnSubmit").prop("disabled", false);
					 $("#div_completado").css('display','none');
				}
			});
	 }	 

}

function descargar_contrato(e,o){
	//alert();
	e.preventDefault();
	var num_cont = $(o).data('num_cont');
	var familia = $(o).data('fam');
	document.location.href=_URL + "api/archivo/pdf/contrato/" + num_cont+"/"+familia;

}

function descargar_declaracion(e,o){
	//alert();
	e.preventDefault();
	var num_cont = $(o).data('num_cont');
	var familia = $(o).data('fam');
	document.location.href=_URL + "api/archivo/pdf/declaracion/" + num_cont+"/"+familia;

}

function descargar_protocolo(e,o){
	//alert();
	e.preventDefault();
	var num_cont = $(o).data('num_cont');
	var familia = $(o).data('fam');
	document.location.href=_URL + "api/archivo/pdf/protocolo/" + num_cont+"/"+familia;

}	
	
$('#frm-reporte-matriculasvalnoval').on('submit',function(event){
		event.preventDefault();
	// id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), , id_niv:$('#id_nvl').val()
		var param={id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_tip: $('#id_tip').val()};
		//envio el email
		return _get('api/matricula/listaMatriculasValidasNoValidadas',fncExito, param);
	});
	
function validar_contrato(obj,event){
	event.preventDefault();
	var id_fam = $(obj).data('id_fam');	
	//var id_alu = $(obj).data('id_alu');	
	_POST({url:'/api/matricula/validarContratoxFamiliar/' + id_fam+'/'+$('#_id_anio').text(), context:_URL,success:function(data){
		
//Comentado para pruebas 2022			
_post_silent('api/familiar/enviarMensajeMatriculaValidada/'+id_fam+'/'+$('#_id_anio').text() ,null,
			function(data){
				
			});	
			$('#frm-reporte-matriculasvalnoval').submit();
			
			/*var param={id_au:$('#id_au').val(), id_gra:$('#id_gra').val(), id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val(), id_niv:$('#id_nvl').val()};
			return _get('api/matricula/reporteMatriculaList',fncExito, param);*/
		}
	});
	//$('#frm-reporte-matriculados').submit();
}

