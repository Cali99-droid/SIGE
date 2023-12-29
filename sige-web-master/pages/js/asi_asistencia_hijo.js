var _roles;
function onloadPage(params){
	_onloadPage();
	console.log(params.id);

	$(function(){
		//$("#alumno").focus();
		var fncExito = function(data){
			//$('#panel-alumnos').css("display","block");
			$('#panel-asistencia').css("display","block");
			var datos = params.nom+" - ("+params.nivel+"/"+params.grado+"/"+params.seccion+")";
			$('#alumno_aula').text(datos);
			$('#tabla-educando-asistencia').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 orderCellsTop:true,
				 lengthMenu: [50],
				 //bLengthChange: false,
				 //bPaginate: false,
				 //bFilter: false,
				 select: true,
		         columns : [ 
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title":"Fecha", "data" : "fecha"}, 
						{"title":"Hora Ingreso", "data": "fecha_ori"}, 
						{"title":"Estado", "data": "asistencia_nom"}
			    ],
			    "initComplete": function( settings ) {
					   _initCompleteDT(settings);
				 }
		    });		
		};
		
		$('#form_hijo_asistencia').on('submit',function(event){
			event.preventDefault();
			//$("#id_suc").val($("#_id_suc").html());
			//$("#id_anio").val($("#_id_anio").text());
			var param={id_alu:params.id, fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val(), asistencia:$("#asis").val()}
			return _get('api/reporteAsistencia/asistenciaAlumno/',fncExito, param);
		});
		
	});

}


/*function asistencia(id_mat, id_alu,alumno, nivel, grado, seccion){
	
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
}*/



