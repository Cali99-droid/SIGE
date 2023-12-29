function onloadPage(params){
	var id_suc=$("#_id_suc").text();
	var id_usr=_usuario.id;
	var rol = _usuario.roles[0];
	if (rol!='1'){
		_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc);
	} else {
		_llenarCombo('ges_sucursal',$('#id_suc'));
	}	
	/*if (id_suc!='0' ){
	_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc);
	} else{
				
	}*/
	$('#id_suc').on('change',function(event){
		
		//_llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val());
		_llenarCombo('cat_nivel',$('#id_niv'),null,function(){$("#id_niv").change();});
	});
	$('#id_niv').on('change',function(event){
		//_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val());
		 var param ={id_niv:$(this).val()};
		 _llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});


	$('#id_gra').on('change',function(event){
		//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
		var id_suc=$("#id_suc").val();
		if($("#_id_anio").text()>=5)
			_llenarCombo('col_aula_local_nuevo',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
		else
			_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
	});	
}


	//$("#fecha").focus();

	_onloadPage();	

	var fncExito = function(data){
		$('#panel-pagos').css("display","block");
		
		$('#tabla-pagos').dataTable({
			 data : data,
			 //aaSorting: [],
			 order: [[ 1, "asc" ]],
			 destroy: true,
			 pageLength: 50,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Apellidos y Nombres", "data" : "alumno"}, 
	     	           {"title": "Matricula Siguiente", "render":function ( data, type, row,meta ){
	     	        	   if( row.id_mat_sig!=null){
	     	        		  return '<div class="token bg-warning text-white text-center"><span class="token-label" style="width: 70%;">SI</span></div>';
	    	        	   }  else{
	    	        		   return null;
	    	        	   }
	     	           }},
	    	           {"title": "Matrícula", "render":function ( data, type, row,meta ) { return row.mat_nro_rec.substring(5, 20) + "<br>" +"S/."+ $.number(row.mat_monto,2); }},
	     	           {"title": "Cuota Ingreso", "render":function ( data, type, row,meta ) { return row.ing_nro_rec.substring(3, 20) + "<br>" +"S/."+ $.number(row.ing_monto,2); }},
	     	           {"title": "Mar", "render":function ( data, type, row,meta ) { 
	     	        	   	if( $.number(row.marzo_monto,2)== '0.00' || row.marzo_monto==""){
		     	        		 return row.marzo_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.marzo_monto,2)!= '0.00' && row.marzo_banco!=''){
		    	        		 return "<font color='blue' align='center'>"+row.marzo_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.marzo_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.marzo_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.marzo_monto,2);
		    	        	   } 
	     	           }},
	     	           
	     	           {"title": "Abr", "render":function ( data, type, row,meta ){
	     	        	   if( $.number(row.abril_monto,2)== '0.00' || row.abril_monto==""){
	     	        		 return row.abril_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
	    	        	   } else if($.number(row.abril_monto,2)!= '0.00' && row.abril_banco!=null){
	    	        		 return "<font color='blue' align='center'>"+row.abril_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.abril_monto,2)+"</font>";  
	    	        	   } else { 
	    	        		 return row.abril_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.abril_monto,2);
	    	        	   } 
	     	           }},
	     	           
	     	           {"title": "May", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.mayo_monto,2)== '0.00' || row.mayo_monto==""){
		     	        		 return row.mayo_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.mayo_monto,2)!= '0.00' && row.mayo_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.mayo_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.mayo_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.mayo_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.mayo_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Jun", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.junio_monto,2)== '0.00' || row.junio_monto==""){
		     	        		 return row.junio_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.junio_monto,2)!= '0.00' && row.junio_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.junio_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.junio_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.junio_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.junio_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Jul", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.julio_monto,2)== '0.00' || row.julio_monto==""){
		     	        		 return row.julio_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.julio_monto,2)!= '0.00' && row.julio_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.julio_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.julio_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.julio_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.julio_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Ago", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.agosto_monto,2)== '0.00' || row.agosto_monto==""){
		     	        		 return row.agosto_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.agosto_monto,2)!= '0.00' && row.agosto_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.agosto_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.agosto_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.agosto_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.agosto_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Set", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.setiembre_monto,2)== '0.00' || row.setiembre_monto==""){
		     	        		 return row.setiembre_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.setiembre_monto,2)!= '0.00' && row.setiembre_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.setiembre_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.setiembre_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.setiembre_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.setiembre_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Oct", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.octubre_monto,2)== '0.00' || row.octubre_monto==""){
		     	        		 return row.octubre_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.octubre_monto,2)!= '0.00' && row.octubre_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.octubre_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.octubre_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.octubre_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.octubre_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Nov", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.noviembre_monto,2)== '0.00' || row.noviembre_monto==""){
		     	        		 return row.noviembre_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.noviembre_monto,2)!= '0.00' && row.noviembre_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.noviembre_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue'align='center'>S/."+ $.number(row.noviembre_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.noviembre_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.noviembre_monto,2);
		    	        	   }  
	     	           }},
	     	           {"title": "Dic", "render":function ( data, type, row,meta ) { 
	     	        	  if( $.number(row.diciembre_monto,2)== '0.00' || row.diciembre_monto==""){
		     	        		 return row.diciembre_nro_rec.substring(5, 20) + "<br>"+"<font color='red'>S./0.00</font>"; 
		    	        	   } else if($.number(row.diciembre_monto,2)!= '0.00' && row.diciembre_banco!=null){
		    	        		 return "<font color='blue' align='center'>"+row.diciembre_nro_rec.substring(5, 20)+"</font>" + "<br>"+"<font color='blue' align='center'>S/."+ $.number(row.diciembre_monto,2)+"</font>";  
		    	        	   } else { 
		    	        		 return row.diciembre_nro_rec.substring(5, 20) + "<br>"+"S/."+$.number(row.diciembre_monto,2);
		    	        	   }  
	     	           }}
	        ],
	        fnDrawCallback: function (oSettings) {
	        	$("<span><a href='#' target='_blank' onclick='printReportePagos(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
	        }
	    });
		
	};
	
	$('#reporte_pagos-frm').on('submit',function(event){
		event.preventDefault();
		var id_au=$("#id_au").val();
		return _get('api/reportePagos/reporte/'+id_au+'/'+$('#_id_anio').text(),
			function(data){
			fncExito(data);
		});
	});
	
function printReportePagos(event){
	event.preventDefault();
	var niv= $('#id_niv option:selected').text();
	var grado= $('#id_gra option:selected').text();
	var aula= $('#id_au option:selected').text();
	var id_au= $('#id_au').val();

	
	document.location.href = _URL + "api/reportePagos/excel?nivel=" + niv +"&grado="+grado+"&aula="+aula+"&sucursal=" + $("#_sucursal").text()+"&id_au="+id_au+"&id_anio="+$('#_id_anio').text();
}
