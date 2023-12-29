//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('');

	col_programacion_anual_tabla();
});

function _onchangeAnio(id_anio, anio){
	col_programacion_anual_tabla();	
}

   

function col_programacion_anual_tabla(){
	var param ={id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra};
	_get('api/cursoAula/listarCursosProfesor/',
			function(data){
			 
			console.log(data);
				$('#col_programacion_anual-tabla').dataTable({
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
			        	 	{"title":"Nivel", "data" : "nivel"}, 
			        	 	{"title":"Grado", "data" : "grado"}, 
			        	 	{"title":"Curso", "data" : "curso"}, 
			        	 	{"title":"Descargar programaci&oacute;n anual", "render": function ( data, type, row ) { //href=\"' + _URL + 'api/cursoAula/programacionAnual/' + $('#_id_anio').text() + '/' + row.id_niv + '/' + row.id_gra +'/' + row.id + '\"
			                  // return '<a href="#" target=\"_blank\"  data-id="' + row.id + '"  data-id_anio="' +  $('#_id_anio').text() + '" data-id_niv="'+row.id_niv+'" data-id_gra="'+row.id_gra+'"	onclick="validarUnidades(this, event)">' + 'Descargar' + '</a>';
			                   //return '<a target=\"_blank\" href=\"' + _URL + 'api/cursoAula/programacionAnual/' + _usuario.id_tra + '/' + $('#_id_anio').text() + '/' + row.id_niv + '/' + row.id_gra +'/' + row.id + '\" >' + 'Descargar' + '</a>';}   
			        	 	   return '<a href=\"#\" onclick=\"validarUnidadesCompletas(' + _usuario.id_tra + ',' + $('#_id_anio').text() + ',' + row.id_niv + ',' + row.id_gra +',' + row.id + ')" >Descargar</a>';}
			        	 	
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param			
	);

} 

function validarUnidadesCompletas(id_tra,id_anio,id_niv,id_gra,id) {

_get('api/cursoAula/validarUnidadesCompletas/' +id_tra + '/'+ id_anio+'/'+id_niv+'/'+id_gra+'/'+id, function(data) {
		
		    window.open(_URL + "api/cursoAula/programacionAnual/" +id_tra + '/'+ id_anio+'/'+id_niv+'/'+id_gra+'/'+id, '_blank');
		
	
	});
}

