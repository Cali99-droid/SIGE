function onloadPage(params){
	//_llenarCombo('cat_nivel',$('#id_niv'));

	var param = {id_anio:$('#_id_anio').text()};
	//alert();
	console.log(param);
	_get('api/matricula/reporteNoMatriculados/',
			function(data){
			console.log(data);
				$('#mat_reporte_no_matriculados').dataTable({
					data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 pageLength: 100,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Alumno", "data": "alumno"},
							{"title":"Ratifico", "data": "ratifico"},
			        	 	//{"title":"DNI", "data": "nro_doc"},
			        	 	{"title":"Nivel", "data": "nivel"},
			        	 	{"title":"Grado", "data": "grado"},	
							{"title":"Sección", "data": "secc"},
							{"title":"Local", "data": "sucursal_ant"},
							{"title":"Apoderado", "data": "familiar"},
							{"title":"Celular", "data": "cel"},
							{"title":"Correo", "data": "corr"}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);		
	
}

