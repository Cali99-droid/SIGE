function onloadPage(params){
	_onloadPage();
}

$(function(){
	$("#alumno").focus();
	_body ='<div class="panel panel-flat"><div class="panel-heading"><h5 class="panel-title">Buscar alumno</h5><div class="heading-elements"><ul class="icons-list"> <li><a data-action="collapse"></a></li> <li><a data-action="reload"></a></li> <li><a data-action="close"></a></li> </ul> </div></div><div class="panel-body"><fieldset class="content-group"><form class="form-horizontal" id="frm-pagos-buscar"><input type="hidden" name="id_anio" value="2" ><input type="hidden" name="id_suc" id="id_suc" ><div class="form-group"><div class="col-lg-12"><input type="text" class="form-control input-xs" id="alumno" name="alumno" placeholder="Ingrese apellidos y nombres del alumno" autocomplete="off"></div></div><div class="text-right"><button type="submit" class="btn btn-primary">Buscar <i class="icon-arrow-right14 position-right"></i></button></div></form></fieldset></div></div><!-- /form horizontal --><div class="panel panel-flat" style="display:none" id="panel-alumnos"><div class="panel-heading"><h5 class="panel-title">Resultados</h5><div class="heading-elements"><ul class="icons-list"> <li><a data-action="collapse"></a></li> <li><a data-action="reload"></a></li> <li><a data-action="close"></a></li> </ul> </div></div><table class="table" id="tabla-alumnos"></table></div>';
	_title = 'Pago de mensualidad';
	_sub_title = 'Buscar Alumno';
	
	var fncExito = function(data){
		$('#panel-alumnos').css("display","block");
		$('#tabla-alumnos').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 50,
			 select: true,
	         columns : [ 
	           {"title": "Contrato", "data" : "num_cont"}, 
	           //{"data" : "apoderado"}, 
	           //{"data" : "dni"}, 
	           {"title":"Alumno", "data" : "alumno",
	        	   "render": function ( data, type, row ) {
	                   return '<a href="#" onclick="mensualidadesxPagar(' + row.id + ',\'' + row.alumno +'\')" >'  + row.alumno +' </a>';}
	        	   },
	           {"title":"Nivel", "data" : "nivel"}, 
	           {"title":"Grado", "data" : "grado"}, 
	           {"title":"Sección", "data" : "seccion"}
	        ]
	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		
		$("#id_suc").val($("#_id_suc").html());
		$("#id_anio").val($("#_id_anio").text());
		
		return _get('api/alumno/consultar',fncExito, $(this).serialize());
	});
	

	
});

/****************************/
/** 	Mostramos las mensualides por pagar		***/
/****************************/
function mensualidadesxPagar(id_mat, alumno){
	
	//alert('mensualidadesxPagar');
	
	_send('pages/tesoreria/fac_pagos_mensualidad.html','Pago de mensualidad','Pagar', {id_mat:id_mat, alumno:alumno});
	
	

	
}