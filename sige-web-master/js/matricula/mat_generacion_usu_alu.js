//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	$("#id_anio").val($('#_id_anio').text());

	//lista tabla
	periodo_listar_generacion_usu_alu();
	
	$('#btn-generar').on('click',function(event){
		var params = $('#frm_generacion_usu_alu').serialize();
		_post('api/alumno/alumnoGenerarUsuarios',params, function(data){
			console.log(data);
			periodo_listar_generacion_usu_alu();
			
			/*for ( var i=0; i<data.length;i++){
				alert();
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_per=" +data.id_per;
			}*/
			
			$("input[type=checkbox]:checked").each(function(){
				//monto_doc= monto_doc + parseFloat($(this).attr('monto'));
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_gra=" +$(this).val()+"&id_anio="+$('#_id_anio').text();
		    });
			
			//document.location.href = _URL + "api/matricula/exportarDirectorioPPFF?id_au=" + id_au+"&id_niv="+$("#id_nvl").val() + "&id_grad="+id_grad+"&usuario=" + _usuario.nombres + "&id_anio=" + $("#_id_anio").html();
		});
	});
	
	$("#lectora-frm").on('submit',function (event) {

	    event.preventDefault();
	    subirArchivoExcel();

	});
});

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	periodo_listar_generacion_usu_alu();	
}

 

function periodo_listar_generacion_usu_alu(){
	
	var params = {id_anio : $('#_id_anio').text()};

	
	_get('api/grad/listarGradosColegio',
			function(data){
			var param = {id_anio:$('#_id_anio').text()};
			console.log(data);
				$('#mat_generacion_usu_alu').dataTable({
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
							/* {"title":"Inicio", "data" : "fec_ini"}, 
							 {"title":"Fin", "data" : "fec_fin"}, */
							 {"title":"Usuarios Generados", "render":function ( data, type, row,meta ) {
								 console.log(row.id_anio_usu_alu);
								 console.log($('#_id_anio').text());
								 if(row.id_anio_usu_alu == $('#_id_anio').text()){
									 return "PROCESADO";
								 }
								 else
									 return "PENDIENTE";
							 } },
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
								return '<input type="checkbox" ' + ' ' + ((row.id_anio_usu_alu==$('#_id_anio').text()) ? 'checked': '') + ' name="id" value="' + row.id_gra + '" >';}
							},
							{"title": "Descargar Formato", "data" : "id","render":function ( data, type, row,meta ) { 
								if(row.id_anio_usu_alu==$('#_id_anio').text())
			     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id_gra + '" onclick="generar_formato(this, event)" class="list-icons-item"><i class="icon-file-excel ui-blue ui-size" aria-hidden="true"></i></a> </div>';
								else
									return '';
			     	         }} ,
			     	        {"title": "Enviar Correo", "data" : "id","render":function ( data, type, row,meta ) { 
								//if(row.id_anio_usu_alu==$('#_id_anio').text())
			     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id_gra + '" onclick="enviar_correo(this, event)" class="list-icons-item"><i class="fa fa-envelope-o ui-blue ui-size" aria-hidden="true"></i></a> </div>';
								//else
								//	return '';
			     	         }}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},params
	);

}

function generar_formato(obj,event){
	event.preventDefault();
	var id_gra = $(obj).data('id');	
	document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_gra=" +id_gra+"&id_anio="+$('#_id_anio').text();
}

function enviar_correo(obj,event){
	event.preventDefault();
	var id_gra = $(obj).data('id');	
	_POST({url:'/api/familiar/pruebasCorreo/' + id_gra+'/'+$('#_id_anio').text(), context:_URL});
}

function subirArchivoExcel(seGraba) {

    var form = $('#lectora-frm')[0];
    var data = new FormData(form);
    var url = _URL +  'api/alumno/xls/uploadPsw';

    $('#btn-procesar').prop("disabled", true);
    
	 $.blockUI({ 
	        message: '<i class="icon-spinner4 spinner"></i>',
	        //timeout: 2000, //unblock after 2 seconds
	        overlayCSS: {
	            backgroundColor: '#1b2024',
	            opacity: 0.8,
	            zIndex: 1200,
	            cursor: 'wait'
	        },
	        css: {
	            border: 0,
	            color: '#fff',
	            padding: 0,
	            zIndex: 1201,
	            backgroundColor: 'transparent'
	        }
	    });    

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: url,
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
        	 $.unblockUI();
        	 
        	console.log(data);
        	var error = data.code;
        	
        	if (error==null){

        		//lectora_listar_tabla(data.result);
        		 	
        	}else{
        		console.log("Error al subir archivo");
        		 
				 swal({
						title : "Error al procesar el archivo",
						text : data.msg,
						confirmButtonColor : "#2196F3",
						type : "error"
					});
        	}
        		
            
        	$('#btn-procesar').prop("disabled", false);

        },
       
        error: function (e) {
        	 $.unblockUI();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

