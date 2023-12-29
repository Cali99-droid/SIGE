//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	//list de alumnos
	var param = {};
	param.id_fam = _usuario.id;
	param.id_anio= $('#_id_anio').text();
	
	_get('api/familiar/hijos/',
			function(data){
			console.log(data);
				$('#av_reporte_acceso').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 searching: false,
					 paging: false,
					 info:    false,
					 select: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title": "Alumno", "data":"nom"}, 
			        	 	{"title": "Usuario", "data":"usuario"},
			        	 	{"title": "Contraseña", "data":"pass_educando"}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
							//$('.daterange-single').val('');
							//$('.daterange-single').prop('disabled',true);
					 }
			    });
			}, param
	);

}
//se ejecuta siempre despues de cargar el html
$(function(){
	//_onloadPage();


	
	//lista tabla
	//alumno_listar_tabla();
});
