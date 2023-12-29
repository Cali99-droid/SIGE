//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_traslado_buscar.html" id="mat_traslado-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Traslado</a></li>');

	$('#mat_traslado-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		//situacion_mat_modal($(this));
		traslado_detalle($(this));
	});

	//lista tabla
	situacion_mat_listar_tabla();
});

function situacion_mat_eliminar(link){
	_delete('api/situacionMat/' + $(link).data("id"),
			function(){
					situacion_mat_listar_tabla();
				}
			);
}

function situacion_mat_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_situacion_mat_modal.html');
	situacion_mat_modal(link);
	
}


function traslado_detalle(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		var id_rol;
		_inputs('col_reporte_buscar-frm');
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			id_rol=_ROL_ADMINISTRADOR;
		else if(_usuario.roles.indexOf(_ROL_SECRETARIA)>-1)
			id_rol=_ROL_SECRETARIA;
		else if(_usuario.roles.indexOf(_ROL_ASIST_ADMINISTRATIVO)>-1)
			id_rol=_ROL_ASIST_ADMINISTRATIVO;

		
		$("#alumno").autocomplete({ //id_anio, Integer id_rol, Integer id_usr
	        minLength: 3,
	        source: _URL + 'api/archivo/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id ,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	var alumno=ui.item.label;
	        	var nivel=ui.item.nivel;
	        	var grado=ui.item.grad;
	        	var seccion=ui.item.secc;
	        	var id_mat=ui.item.id;
	        	var id_alu=ui.item.id_alu;
	        	_get('api/trasladoDetalle/validarAnioEstudios/'+id_mat+'/'+id_alu,function(data){
	        		console.log(data);
	        		if(data==0){
	        			alert('El alumno no puede hacer su traslado en este año no correspondel al último año de estudios');
	        		} else{
	        			$(".modal .close").click();
	    	        	_send('pages/matricula/mat_traslado_detalle.html','Registro del traslado','Datos del Traslado',{alumno:alumno, nivel:nivel, grado:grado, seccion:seccion, id_mat:id_mat, id_alu:id_alu});
	        		}
	        	});	
	        	
	        },
	       
	    });
		
		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_conductual_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Incidente';
	else
		titulo = 'Nuevo  Incidente';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function onkeyAlumno(){

 	$('.detalle').hide();
	$('#mat_situacion_mat-frm #btn-grabar').hide();

}

function situacion_mat_listar_tabla(){
	var param ={id_anio:$('#_id_anio').text()};
	_get('api/trasladoDetalle/listar/',
			function(data){
			console.log(data);
				$('#mat_traslado-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Alumno", "render":function ( data, type, row,meta ) { return row.matricula.alumno.ape_pat + ' '+row.matricula.alumno.ape_mat +' '+ row.matricula.alumno.nom}},
			        	 	{"title":"Nivel", "data": "matricula.nivel.nom"}, 
			        	 	{"title":"Grado", "data": "matricula.grad.nom"}, 
			        	 	{"title":"Sección", "data": "matricula.aula.secc"}, 
							{"title":"Mes Traslado", "render":function ( data, type, row,meta ) {    
							var fecha=_parseDate(row.fec);
						
							var arrFecha = fecha.split('/');
							var mes = 	arrFecha[1];
							var anio = arrFecha[2];
							//return mes;
								if(mes=='01'){
									return 'Enero - '+anio;
								} else if(mes=='02'){
									return 'Febrero - '+anio;
								} else if(mes=='03'){
									return 'Marzo - '+anio;
								} else if(mes=='04'){
									return 'Abril - '+anio;
								} else if(mes=='05'){
									return 'Mayo - '+anio;
								}else if(mes=='06'){
									return 'Junio - '+anio;
								}else if(mes=='07'){
									return 'Julio - '+anio;
								}else if(mes=='08'){
									return 'Agosto - '+anio;
								}else if(mes=='09'){
									return 'Setiembre - '+anio;
								}else if(mes=='10'){
									return 'Octubre - '+anio;
								} else if(mes=='11'){
									return 'Noviembre - '+anio;
								}else if(mes=='12'){
									return 'Diciembre - '+anio;
								}else {
									return '';
								}				
							// 
							 return date.getMonth();
							
							}},
			        		{"title":"Fecha Traslado", "render":function ( data, type, row,meta ) { return _parseDate(row.fec)}},
			        		{"title":"Colegio Destino", "data": "colegio.nom"}, 
			        		{"title":"Distrito", "data": "colegio.distrito.nom"}, 
			        		{"title":"Provincia", "data": "colegio.provincia.nom"}, 
			        		{"title":"Departamento", "data": "colegio.departamento.nom"}, 
			        		{"title":"Observaciones Conductuales/Economicas", "data": "condicion.des"}, 
			        	 	{"title":"Situaci&oacute;n", "data": "colSituacion.nom"}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

