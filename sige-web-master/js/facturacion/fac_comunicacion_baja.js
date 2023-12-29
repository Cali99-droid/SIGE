function onloadPage(params){
	
}

$(function(){
	$("#fecha").focus();
	//alert(1);
	_onloadPage();	

	var fncExito = function(data){
		$('#panel-alumnos').css("display","block");
		
		$('#tabla-recibos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 1000,
			 scrollX:true,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Tipo", "data" : "doc"}, 
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
	    	           {"title": "Monto", "data" : "monto_total","render":function ( data, type, row ) {
	    	        	   //if (row.tipo=="I")
	    	        		   return '<font color="blue">S./' + $.number( data, 2) + '</font>';
	    	        	   //else 
	    	        		//   return '<font color="red">S./' + $.number( data, 2) + '</font>';
	    	        	   }
					   },
					   {"title": "Seleccionar", "render":function ( data, type, row,meta ) { return '<button class="btn btn-danger" onclick="seleccionarBaja(' + meta.row + ')">Seleccionar</button>';} } 
	        ],
	        "initComplete": function( settings ) {
	        	 _initCompleteDT(settings);
	        	//_dataTable_total(settings,'monto_total');
	        	//$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			 }

	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/comunicacionBaja/consulta',fncExito, $(this).serialize());
	});
	

	
});

function printReporteCaja(event){
	event.preventDefault();
	document.location.href = _URL + "api/reporteCaja/excel?fec_ini=" + $("#fec_ini").val()+"&fec_fin="+$("#fec_fin").val() + "&usuario=" + $("#usuario\\.login").text() + "&sucursal=" + $("#_sucursal").text() + "&id_suc=" + $("#_id_suc").html();
}



function seleccionarBaja(index){
	
	var table = $('#tabla-recibos').DataTable();
	var recibo = table.rows(index).data()[0];
	
	console.log(recibo);
//	_send('pages/olimpiada/fac_comunicacion_baja_form.html','Facturaci&oacute;n electr&oacute;nica','Recibo a Dar de Baja', recibo);
	
	var onShowModal = function(recibo){
		var baja_est = recibo.baja_est;//ESTADO DEL DOCUMENTO DE BAJA
		if(baja_est==null ){
			$('#btnGrabar').show();
			$('#btnEnviarSunat').hide();
		}else if(baja_est=='A' || baja_est=='E'){//grabado o con error
			$('#btnGrabar').hide();
			$('#btnEnviarSunat').show();
			$('#motivo').attr('readonly','readonly');
		}else if(baja_est=='S'){//ENVIADO A SUNAT
			$('#btnGrabar').hide();
			$('#btnEnviarSunat').hide();
			$('#motivo').attr('readonly','readonly');
		}		
		
		$('#id_fmo').val(recibo.id);
		$('#id').val(recibo.baja_id);
		$('#concepto').val(recibo.concepto);
		$('#doc').val(recibo.doc);
		$('#fecha').val(_parseDate(recibo.fecha));
		$('#grado').val(recibo.grado);
		_llenarCombo('ges_sucursal',$('#id_suc'),recibo.id_suc); 
		$('#nivel').val(recibo.nivel);
		$('#secc').val(recibo.secc);
		$('#monto_total').val(_formatMonto(recibo.monto_total));
		$('#nro_rec').val(recibo.nro_rec);
		$('#obs').val(recibo.obs);
		$('#motivo').val(recibo.motivo);//MOTIVO DE BAJA
		
		
		_inputs('comunicacion_baja-form');
		
		$("#comunicacion_baja-form").unbind();//remover el evento dado por el _modal
		$("#comunicacion_baja-form").on('submit',function (event){
			event.preventDefault();
			
			_POST({
				form:$("#comunicacion_baja-form"),
				success: function(data){
					$('#btnGrabar').hide();
					$('#btnEnviarSunat').show();
					$('#motivo').attr('readonly','readonly');
					$('#id').val(data);
					$('#frm-pagos-buscar').submit();
					
					
				}
			});
		});
	
		
		var validation = $('#comunicacion_baja-form').validate(); 
		
		
		$('#btnEnviarSunat').on('click',function(e){
			console.log('1');
			if ($('#comunicacion_baja-form').valid()){
				_POST({
					url: 'api/comunicacionBaja/enviarSunat',
					confirm: 'El documento se dará de baja en la SUNAT y se reflejará en el portal de la clave SOL.',
					params:$('#comunicacion_baja-form').serialize(),
					msg_exito : 'El documento está dado de baja en la SUNAT.',
					button: $(this),
					success:function(data){
						console.log('2');
						$('#frm-pagos-buscar').submit();
					}
				});
			}
		});
		
	}
		 
	
	/*
	$('#btnEnviarSunat').on('click',function(e){
		if ($('#comunicacion_baja-form').valid()){
			_POST({
				url: 'api/comunicacionBaja/enviarSunat',
				confirm: 'El documento se dará de baja en la SUNAT',
				params:$('#comunicacion_baja-form').serialize(),
				button: $(this),
				sucess:function(data){
					$('#frm-pagos-buscar').click();
				}
			});
		}
	});*/
	 
	_modal("Grabar comunicación de baja", 'pages/tesoreria/fac_comunicacion_baja_modal.html',onShowModal,null,recibo);


}