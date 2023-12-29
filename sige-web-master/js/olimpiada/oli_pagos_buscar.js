function onloadPage(params){
	_onloadPage();
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		//tabla : 'oli_config',
		url : 'api/config/listarOlimpiadasxAnio',
		params: param,
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
		});
	} else{
		var param={id_anio:$("#_id_anio").text(),id_usr:_usuario.id};
		_llenar_combo({
			url : 'api/config/listarOliUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}
}

$(function(){
	$("#alumno").focus();
	_body ='<div class="panel panel-flat"><div class="panel-heading"><h5 class="panel-title">Buscar alumno</h5><div class="heading-elements"><ul class="icons-list"> <li><a data-action="collapse"></a></li> <li><a data-action="reload"></a></li> <li><a data-action="close"></a></li> </ul> </div></div><div class="panel-body"><fieldset class="content-group"><form class="form-horizontal" id="frm-pagos-buscar"><input type="hidden" name="id_anio" value="2" ><input type="hidden" name="id_suc" id="id_suc" ><div class="form-group"><div class="col-lg-12"><input type="text" class="form-control input-xs" id="alumno" name="alumno" placeholder="Ingrese apellidos y nombres del alumno" autocomplete="off"></div></div><div class="text-right"><button type="submit" class="btn btn-primary">Buscar <i class="icon-arrow-right14 position-right"></i></button></div></form></fieldset></div></div><!-- /form horizontal --><div class="panel panel-flat" style="display:none" id="panel-alumnos"><div class="panel-heading"><h5 class="panel-title">Resultados</h5><div class="heading-elements"><ul class="icons-list"> <li><a data-action="collapse"></a></li> <li><a data-action="reload"></a></li> <li><a data-action="close"></a></li> </ul> </div></div><table class="table" id="tabla-alumnos"></table></div>';
	_title = 'Pago de mensualidad';
	_sub_title = 'Buscar Alumno';
	
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();

		$('#panel-alumnos').css("display","block");
		
		$("#id_suc").val($("#_id_suc").html());
		$("#id_anio").val($("#_id_anio").text());
		
		_GET({  url:'api/pagos/buscarAlumnoxPagar',
				params: $(this).serialize(),
				context:_URL_OLI,
				success:function(data){
					console.log(data);
					$('#tabla-alumnos').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 pageLength: 50,
						 select: true,
				         columns : [ 
				           {"title": "Nro doc.", "data" : "nro_dni"},
				           {"title":"Alumno", "data" : "alumno",
				        	   "render": function ( data, type, row,meta ) {
				                   return '<a href="#" onclick="olimpiadaPagar(' + meta.row+ ')" >'  + row.alumno +' </a>';}
				        	   },
				           {"title":"Colegio", "data" : "colegio"}, 
					       {"title":"Nivel", "data" : "nivel"}, 
					       {"title":"Grado", "data" : "grado"}, 
					       {"title":"Modalidad", "data" : "modalidad"}, 
					       {"title":"Monto", "data" : "monto",
				        	   "render": function ( data) {
				                   return "S/"  + _formatMonto(data);}
				        	   }, 
				           {"title":"Pagado", "data" : "id_pag",
					        	   "render": function ( data) {
					        		   if(data==null)
					        			   return "NO";
					        		   else
					        			   return "SI";
					        	}
					        },
							{"title": "Seleccionar", "render":function ( data, type, row,meta ) {
								var tipo=null;
								return "<input type='checkbox' id='id_ins"+row.id+"' data-nro_dni='" + row.nro_dni + "' data-alumno='" + row.alumno + "' data-colegio='" + row.colegio + "' data-nivel='" + row.nivel + "' data-grado='" + row.grado + "' class='id_ins' name ='id_ins' value='" + row.id+ "'   />";							
							}},
				        ]
				    });
				
			}
		});

	});
	

	
});

function pagarseleccionados(e){
	var id_ins=[];
	$("#tabla-reporte input[name='id_ins']:checked").each(function() {
		id_ins.push($(this).val());
		tipo.push($(this).data('tipo'));						
	 });
	_download(_URL_OLI+'api/pagos/pagarVarios?id_ins='+id_ins+'&id_suc=0','carnets_olimpiada.pdf');	
}

/****************************/
/** 	Mostramos las mensualides por pagar		***/
/****************************/
/**
 * id id (independiente o delegacion)
 * id_ti tipo de inscripcion
 */
function olimpiadaPagar(index){
	
	var table = $('#tabla-alumnos').DataTable();
	var row = table.rows(index).data()[0];
	
	_send('pages/olimpiada/oli_pagos_form.html','Pago de inscripcion de olimpiada','Pagar', row);

}