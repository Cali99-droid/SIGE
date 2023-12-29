var _roles;
function onloadPage(params){
	_onloadPage();
	
}

$(function(){
	$("#alumno").focus();
	var fncExito = function(data){
		$('#panel-alumnos').css("display","block");
		$('#panel-asistencia').css("display","none");
		$('#tabla-alumnos').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	           {"title": "Contrato", "data" : "num_cont"}, 
	           //{"data" : "apoderado"}, 
	           //{"data" : "dni"}, 
	           {"title":"Alumno", "data" : "alumno",
	        	   "render": function ( data, type, row ) {
	                   return '<a href="#" onclick="asistencia(' + row.id+ ','+ row.id_alu +',\'' +row.alumno +'\',\''+row.nivel+'\',\''+row.grado+'\',\''+row.seccion+'\')" >'  + row.alumno +' </a>';}
	        //   return '<a href="#" title="HISTORIAL" onclick="historial_eco(' + row.id_mat + ',' + row.id_mat_cond +',\''+row.alumno+'\',\''+row.apoderado+'\',\''+row.nro_doc+'\',\''+row.cel+'\',\''+row.nivel+'\',\''+row.grado+'\',\''+row.seccion+'\')"><i class="fa fa-book" style="color:blue"></i></a>'; 
	           },
	           {"title":"Nivel", "data" : "nivel"}, 
	           {"title":"Grado", "data" : "grado"}, 
	           {"title":"Sección", "data" : "seccion"}
	        ]
	    });
		
	};
	
	$('#form_educando_asistencia').on('submit',function(event){
		event.preventDefault();
		//$("#id_suc").val($("#_id_suc").html());
		$("#id_anio").val($("#_id_anio").text());
		
		return _get('api/alumno/consultar',fncExito, $(this).serialize());
	});
	
});

function asistencia(id_mat, id_alu,alumno, nivel, grado, seccion){
	
	//alert('mensualidadesxPagar');
	$('#panel-alumnos').css("display","none");
	$('#panel-asistencia').css("display","block");
	var datos = alumno+" - ("+nivel+"/"+grado+"/"+seccion+")";
	$('#alumno_aula').text(datos);
	var param={id_alu:id_alu, fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val(), asistencia:$("#asis").val()}
	_get('api/reporteAsistencia/asistenciaAlumno/',
			function(data){
			console.log(data);
				$('#tabla-educando-asistencia').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Giro", "data" : "giro"}, 
							{"title":"Fecha", "data" : "fecha"}, 
							{"title":"Hora Ingreso", "data": "fecha_ori"}, 
							{"title":"Estado", "data": "asistencia_nom"}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);
}



