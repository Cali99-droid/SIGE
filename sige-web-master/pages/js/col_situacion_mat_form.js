//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
 

	//lista tabla
	periodo_listar_tabla();
	
	$('#btn-grabar').on('click',function(event){
		var params = $('#col_situacion_mat-frm').serialize();
		_post('api/situacionFinal',params, function(data){
			console.log(data.result);
			periodo_listar_tabla();	
		});
	});
});

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	periodo_listar_tabla();	
}

 

function periodo_listar_tabla(){
	
	var params = {id_anio : $('#_id_anio').text()};

	
	_get('api/situacionFinal/listar',
			function(data){
			var param = {id_anio:$('#_id_anio').text(), id_tpe:2};
			console.log(data);
				$('#col_situacion_mat-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 select: true,
				     bInfo: false,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	 {"title":"Local", "data": "servicio.sucursal.nom"}, 
			        	 	 {"title":"Nivel", "data": "servicio.nom"}, 
							 {"title":"Inicio", "data" : "fec_ini"}, 
							 {"title":"Fin", "data" : "fec_fin"}, 
							 {"title":"Situacion final", "render":function ( data, type, row,meta ) { 
								 if(row.flag_sit == null)
									 return "PENDIENTE";
								 else
									 return "PROCESADO";
							 } },
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
								return '<input type="checkbox" ' + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},params
	);

}

