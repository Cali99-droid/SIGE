//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	$("#id_anio").val($('#_id_anio').text());

	//lista tabla
	grado_asignar_grupo();
	
	/*$('#btn-generar').on('click',function(event){
		var params = $('#frm_generacion_usu_alu').serialize();
		_post('api/alumno/alumnoGenerarUsuarios',params, function(data){
			console.log(data);
			periodo_listar_generacion_usu_alu();
			
			/*for ( var i=0; i<data.length;i++){
				alert();
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_per=" +data.id_per;
			}*/
			
		/*	$("input[type=checkbox]:checked").each(function(){
				//monto_doc= monto_doc + parseFloat($(this).attr('monto'));
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_gra=" +$(this).val()+"&id_anio="+$('#_id_anio').text();
		    });
			
			//document.location.href = _URL + "api/matricula/exportarDirectorioPPFF?id_au=" + id_au+"&id_niv="+$("#id_nvl").val() + "&id_grad="+id_grad+"&usuario=" + _usuario.nombres + "&id_anio=" + $("#_id_anio").html();
		});
	});*/
	

});

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	grado_asignar_grupo();	
}

 

function grado_asignar_grupo(){
	
	var params = {id_anio : $('#_id_anio').text()};

	
	_get('api/grad/listarGradosColegio',
			function(data){
			var param = {id_anio:$('#_id_anio').text()};
			console.log(data);
				$('#asignacion_grupo_tab').dataTable({
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
			        	 	{"title":"Nivel", "data": "nivel"}, 
			        	 	{"title":"Grado", "data": "grado"},
			        	 	{"title":"20", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},
							{"title":"25", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},
							{"title":"30", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},
							{"title":"Acciones", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<button type="button" id="btn-asignar" class="btn btn-primary pull-right btn-sm" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)">Asignar Grupos</button>';
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},params
	);

}

function generar_grupos(obj,event){
	event.preventDefault();
	var id_gra = $(obj).data('id_gra');	
	_POST({url:'/api/grupoAlumno/asignarGrupos/' + id_gra+'/'+$('#_id_anio').text(), context:_URL});
}

