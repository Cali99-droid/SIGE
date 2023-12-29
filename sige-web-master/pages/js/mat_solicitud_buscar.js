

function onloadPage(params) {
	
	_onloadPage();	

	_llenarComboApi('api/solicitud/otrosLocales?id_suc=' + $('#_id_suc').text(), 'nom', $('#id_suc_ori'));
  
	$('#frm-alumno-buscar').on('submit',function(event){
		event.preventDefault();
		//PARANETRUZAR
		//return false;
 		$("#id_suc").val($("#_id_suc").html());
		var param= {alumno:$('#alumno').val(), id_anio: $('#_id_anio').text(), id_suc_des:$('#_id_suc').text(), id_suc_ori:$('#id_suc_ori').val()};
		console.log(param);
		return _get('api/solicitud/alumnoBuscar',function (data){
			
			$('#panel-matricula-alumnos').show();
			$('#tabla-matricula-alumnos').dataTable({
				 data : data,
				 destroy: true,
				 pageLength: 1000,
				 select: true,
		         columns : [ 
		     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
		     	           {"title": "Alumno", "render":function ( data, type, row,meta ) { 
		     	        	   //return row.ape_pat + " " + row.ape_mat + ", " + row.nom
		                	   return "<a href='#' data-id='" + row.id_alu + "' onclick='_ver_alumno(this)'>"  + row.ape_pat+" "+row.ape_mat+" "+row.nom + "</a>";
		     	        	   }},
		     	        	  {"title":"Nivel","data":"nivel"},
		     	        	  {"title":"Grado","data":"grado"},
			     	           {"title": "Estado", "render":function ( data, type, row,meta ) { 
		     	        	   if (row.tipo=='M')//matriculado
		     	        		   return "Matriculado";
		     	        	   else if (row.tipo=='R')//Reserva
		     	        		   return "Con Reserva";
		     	        	   else if (row.tipo=='A')//Reserva
		     	        		   return "Antiguo no matriculado";
		     	        	   else if (row.tipo=='S')//Con solicitud pero aun no efectiva
		     	        		   return "(Solicitado)Pendiente de cambio de local";
		     	        	   else 
		     	        		   return "";
		     	        	}},
		     	           {"title": "Solicitud", "render":function ( data, type, row,meta ) {
		     	        	   var button="<a href='#' onclick='solicitar(" + row.id_mat +"," + row.id_alu + "," + row.id_res + ",\"" + row.tipo + "\")'><button class='btn btn-warning'><span class='glyphicon glyphicon-edit'></span>Solicitar</button></a>"; 
		     	           
		     	        	   return button;
		     	           } }
		        ]


		    });
			
		}, param);
		
	});
	
	  
	

}

function solicitar(id_mat, id_alu, id_res, tipo){
	
	var paramSolicitar ={id_mat:id_mat, id_alu:id_alu, id_res:id_res, tipo:tipo, id_suc_ori:$('#id_suc_ori').val()};
	
	if (tipo=='S'){
		_send('pages/matricula/mat_solicitud_form.html','Matricula','Registro de solicitud de cambio de local',paramSolicitar);
	}else{

		//validar si el local tiene la seccion esperada
		var request ={id_mat:id_mat ,id_alu:id_alu ,id_anio: $('#_id_anio').text(),id_suc: $('#_id_suc').text(), tipo:tipo};
		_get('api/solicitud/validarLocal',function(data){
			console.log(data);
			
			_send('pages/matricula/mat_solicitud_form.html','Matricula','Registro de solicitud de cambio de local',paramSolicitar);
			
		}, request);

	}
		
}

 
