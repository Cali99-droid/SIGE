//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_oli=null;
function onloadPage(params) {
	
	console.log(params);
	id_oli=params.id;
	_GET({
		context:_URL_OLI,
		url:'api/public/config/' + id_oli,
		success:function(data){
			$("#nombre").text(data.nom);
			$("#fecha").text(_parseDate(data.fec));
		}
	});
	

}

$('#oli_resultados-frm').on('submit',function(e){
	e.preventDefault();
	$('#id_oli').val(id_oli);
	_GET({
		url:'api/public/resultados/delegacion',
		params:$('#oli_resultados-frm').serialize(),
		context:_URL_OLI,
		success:function(data){
			if (data==null ){
				$('#divVacio').show();
				$('#divResultados').hide();
			}else{
				$('#divResultados').show();
				$('#divVacio').hide();
				
				$('#colegio').val(data.delegacion.nom);
				$('#nivel').val('INICIAL');
				$('#delegado').val(data.delegacion.ape_mat_del + ' ' + data.delegacion.ape_pat_del + ', ' + data.delegacion.nom_del );
				$('#gestion').val(data.delegacion.gestion);
				
				$('#resultados-table').dataTable({
					 data : data.list,
					 aaSorting: [],
					 destroy: true,
					 pageLength: 50,
					 select: true,
			         columns : [ 
			           {"title": "Nro", "render": function ( data, type, row,meta ) {return parseInt(meta.row)+1;}},
			           {"title":"Alumno", "render": function ( data, type, row,meta ) {return row.ape_pat + ' ' + row.ape_mat + ', ' + row.nom}},
				       {"title":"Nivel", "data" : "nivel"}, 
				       {"title":"Grado", "data" : "grado"}, 
				       {"title":"Puntaje", "className":"text-center", "data" : "puntaje","render": function ( data, type, row,meta ) {
				    	   if(data!=null)
				    		   return data;
				    	   else
				    		   return '-';
				       }},
				       {"title":"Puesto", "className":"text-center", "data" : "puesto","render": function ( data, type, row,meta ) {
				    	   if(row.puntaje!=null)
				    		   return data;
				    	   else
				    		   return '-';
				       }},
				       {"title":"Total Alumnos del grado", "className":"text-center", "data" : "cant"},
				       {"title":"Hora Ingreso", "className":"text-center", "data" : "hor_ing"},
				       {"title":"Hora Salida", "className":"text-center", "data" : "hor_sal"}
			        ],
			        initComplete: function( settings ) {
			        	$("<span><a href='#' onclick='generarPDF(event)'> <i class='icon-file-pdf ui-blue'></i>Descargar</a>&nbsp;</span>").insertBefore($("#_paginator"));
					 }
			    });
			}
			
		},
		error:function(data){
			$('#divVacio').show();
			$('#divResultados').hide();
		}
	
	});
});

$('#btn-cancelar').on('click',function(event){
	var id_oli= $("#id_oli").val();
	var param={};
	param.id=id_oli;
	//param.tip='D';
	_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
});

function generarPDF(e){
	e.preventDefault();
		_download(_URL_OLI+'api/public/resultados/pdfResultados?id_oli='+$("#id_oli").val()+'&cod_mod=' + $('#cod_mod').val(),'resultados.pdf');

}