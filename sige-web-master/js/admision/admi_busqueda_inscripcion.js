/** Se ejeuta al ser llamado desde otra pagina * */
function onloadPage() {

	//_onloadPage();
	$("#alumno").focus();
//	$('#frm-admi_busqueda_inscripcion').parsley().on('form:submit', function(formInstance) {
	
		
}

$(function(){
	_onloadPage();
	$('#frm-admi_busqueda_inscripcion').on('submit',function(event){
		event.preventDefault();
		var alumno=$("#alumno").val();
		if(alumno==null){
			alert('Escriba los apellidos del alumno que desea buscar');
		} else{
			var param ={apellidosNombres:alumno};			
			_GET({url:'api/alumno/listarTodosAlumnos', 
					context:_URL,
					params: param,
					success:function(data){
						console.log(data);
					$('#admi_busqueda_inscripcion-tabla').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 pageLength: 50,
						 select: true,
				         columns : [ 
				           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				           {"title":"Alumno", "data" : "alumno"},
				           {"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="inscripcionAlumno(' + row.id +','+row.id_gpf+',\''+row.alumno+'\')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i>Inscripci&oacute;n</button>';
						   }} 
				        ]
				    });
					}
				});
		}
		
	});

});

function inscripcionAlumno(id_alu, id_gpf, alumno){
	_send('pages/admision/admi_inscripcion_alumno.html','Inscripci&oacuten del Alumno '+alumno+'','Registro',{id:id_alu, id_gpf:id_gpf});
}



