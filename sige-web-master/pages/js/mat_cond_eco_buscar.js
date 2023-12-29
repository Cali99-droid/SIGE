function onloadPage(params){
	_onloadPage();
	$("#id_anio").val($('#_id_anio').text());
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
	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	           //{"data" : "apoderado"}, 
	           //{"data" : "dni"}, 
	           {"title":"Alumno", "data" : "alumno"},
	           {"title":"Nivel", "data" : "nivel"}, 
	           {"title":"Grado", "data" : "grado"}, 
	           {"title":"Sección", "data" : "seccion"},
	           {"title":"Condición Económica", "data": "condicion"},
	           {"title":"Situación Matrícula", "data": "situacion"},
	           {"title":"Historial Ecomnómico", "data" : "hist", 
	        	   "render":function ( data, type, row ) {
	        		   if (row.hist!=null)
	        			   return '<a href="#" title="HISTORIAL" onclick="historial_eco(' + row.id_mat + ',' + row.id_mat_cond +',\''+row.alumno+'\',\''+row.apoderado+'\',\''+row.nro_doc+'\',\''+row.cel+'\',\''+row.nivel+'\',\''+row.grado+'\',\''+row.seccion+'\')"><i class="fa fa-book" style="color:blue"></i></a>'; 
	        		   else
	        			   return '<a href="#" title="HISTORIAL" onclick="historial_eco(' + row.id_mat + ',' + row.id_mat_cond +',\''+row.alumno+'\',\''+row.apoderado+'\',\''+row.nro_doc+'\',\''+row.cel+'\',\''+row.nivel+'\',\''+row.grado+'\',\''+row.seccion+'\')"><i class="fa fa-book" style="color:red"></i></a>';
	        	   }
	           }
	        ]
	    });
		
	};
	
	$('#frm-condeco-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/condMatricula/listarAlumnos',fncExito, $(this).serialize());
	});
	

	
});

/****************************/
/** 	Mostramos las mensualides por pagar		***/
/****************************/
function historial_eco(id_mat, id_mat_cond, alumno,apoderado,nro_doc,cel,nivel, grado, seccion){
	
	//alert('mensualidadesxPagar');
	
	_send('pages/matricula/mat_condicion_modal.html','Condición Económica','Búsqueda de Alumnos', {id_mat:id_mat,id_mat_cond:id_mat_cond, alumno:alumno, apoderado:apoderado, nro_doc:nro_doc, cel:cel, nivel: nivel, grado:grado, secc:seccion});
	
	

	
}