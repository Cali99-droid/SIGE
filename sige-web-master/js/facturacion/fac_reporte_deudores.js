function onloadPage(params){
	var id_suc=$("#_id_suc").text();
	var id_usr=_usuario.id;	

	
	/*$('#id_gra').on('change',function(event){
		//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
		 var param ={id_anio:$("#_id_anio").text(), id_niv:$('#id_niv').val(), id_gra:$('#id_gra').val()};
		 _llenarComboURL('api/aula/listarAulasxNivelGrado',$('#id_au'),null,param,function(){$('#id_au').change()});
	});	
	
	$('#id_niv').on('change',function(event){
		//_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val());
		 var param ={id_niv:$(this).val()};
		 _llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});
	
	_llenarCombo('cat_nivel',$('#id_niv'),null,function(){$("#id_niv").change();});*/
	
	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	
	
	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_nvl').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	
	$('#id_niv').on('change',function(event){
		var id_niv=$('#id_niv').val();
		if(id_niv!=null && id_niv!=''){
			_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
			
		}	
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
			_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});		
	});	
	
	$('#id_gir').on('change',function(event){	
		var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); 
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
	});
}

$(function(){
	_onloadPage();	

	$("#fecha").focus();
	
	
	var fncExito = function(data){
		$('#panel-pagos').css("display","block");
		
		if (_usuario.id_per == 1 ){
			
			$('#tabla-pagos').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
		     	           {"title": "Local", "data" : "local"}, 
		     	           {"title": "Recibo", "data" : "nro_rec"}, 
		     	           {"title": "Mes", "data" : "concepto"}, 
		     	           {"title": "Contrato", "data" : "num_cont"}, 
		     	           {"title": "Alumno", "data" : "alumno"}, 
		     	           {"title": "Nivel", "data" : "nivel"}, 
		     	           {"title": "Grado", "data" : "grado"}, 
		     	           {"title": "Secci&oacute;n", "data" : "secc"}, 
		     	           {"title": "Fec. pago", "data" : "fec_pago"}, 
		    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
		    	        	   if (row.tipo=="I")
		    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
		    	        	   else 
		    	        		   return '<font color="red">S./-' + $.number( data, 2) + '</font>';
		    	        	   }
						   }
		        ],
		        
		    });
			
		}else{
			
			$('#tabla-pagos').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
		     	           {"title": "Recibo", "data" : "nro_rec"}, 
		     	           //{"title": "Contrato", "data" : "num_cont"}, 
		     	           {"title": "Concepto", "data" : "concepto"}, 
		     	           {"title": "Alumno", "data" : "alumno"}, 
		     	           {"title": "Nivel", "data" : "nivel"}, 
		     	           {"title": "Grado", "data" : "grado"}, 
		     	           {"title": "Secci&oacute;n", "data" : "secc"}, 
		     	           {"title": "Fec. pago", "data" : "fec_pago"}, 
		    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
		    	        	   if (row.tipo=="I")
		    	        		   return '<font color="blue">' + $.number( data, 2) + '</font>'; // return '<font color="blue">S./' + $.number( data, 2) + '</font>';
		    	        	   else 
		    	        		   return '<font color="red">-' + $.number( data, 2) + '</font>'; // return '<font color="red">S./-' + $.number( data, 2) + '</font>';
		    	        	   }
						   },
				           {"title":"Boleta", "data" : "nro_rec", 
				        	   "render":function ( data, type, row ) {
				        		   if (row.nro_rec.indexOf("B")=='0' || row.nro_rec.indexOf("F")=='0')
				        			   return '<a href="#" title="RE-IMPRESION" onclick="reimprimir(\'' +data +'\')"><i class="fa fa-print" style="color:blue"></i></a>&nbsp;&nbsp;&nbsp;<a href="#" target="_blank" data-id_fmo="' + row.id_fmo + '" onclick="pdf(event,this)" title="DESCARGA PDF"><i class="fa fa-file-pdf-o" style="color:red"></i></a>'; 
				        		   else
				        			   return '';
				        	   }
				           }
		        ],
		        "initComplete": function( settings ) {
		        	_dataTable_total(settings,'monto_total');
		        	
		        	$("<span><a href='#' target='_blank' onclick='printReporteBanco(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));

				 }
		    });
			
		}

		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());

		//return _get('api/pagos/banco/' + $("#_id_suc").html(),fncExito, $(this).serialize());
		document.location.href = _URL + "api/archivo/excelDeudores?id_gir=" + $('#id_gir').val() +'&id_cic=' + $('#id_cic').val() +'&id_niv=' + $('#id_niv').val() + '&id_gra=' + $('#id_gra').val() + '&id_au=' + $('#id_au').val()+'&mes='+$('#mes').val()+'&mat='+$('#mat').val()+'&ord='+$('#ord').val()+'&id_anio='+$("#_id_anio").text() ;
	});
	

	
});


function reimprimir(nro_rec){
	
	_post_silent('api/banco/imprimir/mensualidad/'+ nro_rec,null,
			function(data){
				_post_json( "http://localhost:8082/api/print", data.result, 
					function(data){
						console.log(data);
						//$("#_btnRefresh").click();
					}
				);
		}
	);
	
}

function printReporteBanco(event){
	event.preventDefault();
	document.location.href = _URL + "api/banco/excel/" + $("#_id_suc").html() + "?fec_ini=" + $('#fec_ini').val() + '&usuario=' + $("#usuario\\.login").text() + '&sucursal=' + $("#_sucursal").text() ;
}

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	//btn pdf boleta
	//document.location.href=_URL + "api/movimiento/pdf/boleta/" + id_fmo;
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo;
}