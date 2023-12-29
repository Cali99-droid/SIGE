//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
function onloadPage(params) {
			
}

$('#reporte_infocorp-frm').on('submit', function(e) {
	e.preventDefault();
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
		
	var param= {id_anio: $('#_id_anio').text(), id_mes: $('#id_mes').val(), id_suc: id_suc};
	
	reporte(param);
});


function reporte(param){
	var codigo="094490";
	_get('api/pagos/reportePagosInfocorp/',
			function(data){
			console.log(data);
				$('#tabla-pagos_info').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 pageLength: 100,
					 select: true,
					 scrollY:        "400px",
			         scrollX:        true,
			         scrollCollapse: true,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha Reporte", "data" : "fec_con"},
							{"title":"Código de la Entidad", "data": "codigo"},
							{"title":"Nº Doc. Moroso", "data" : "id"},
							{"title":"Tip. Doc. Entidad", "data" : "id_tdc"},
							{"title":"Nº Documento Identidad", "data" : "numdoc"},
							{"title":"Tip. Persona", "data" : "tip_per"},
							{"title":"Tip de Deudor", "data": "tip_deudor"},
							{"title":"Apellidos y Nombres o Razon Social", "data" : "familiar"},
							{"title":"Dirección", "data" : "direccion"},
							{"title":"Distrito", "data" : "dis_nom"},
							{"title":"Departamento", "data" : "dep_nom"},
							{"title":"Fecha de Vencimiento", "data" : "fecha_ven"},
							{"title":"Documento de Credito", "data": "doc_cre"},
							{"title":"Tipo de Moneda", "data": "tip_mo"},
							{"title":"Monto Impago", "data" : "monto","className": "text-center"},
				    ], "initComplete": function( settings ) {
			        	
			        	$("<span><a href='#' target='_blank' onclick='printReporteInfocorp(event)'> <i class='fa fa-file-excel-o'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			        	//_dataTable_total(settings,'monto');
					 },
						"footerCallback": function ( row, data, start, end, display ) {
					        var api = this.api(), data;
					        // Remove the formatting to get integer data for summation
					        var intVal = function ( i ) {
					            return typeof i === 'string' ?
					                i.replace(/[\$,]/g, '')*1 :
					                typeof i === 'number' ?
					                    i : 0;
					        };
					        	// Total over all pages
					            total = api
					                .column( 15)
					                .data()
					                .reduce( function (a, b) {
					                    return intVal(a) + intVal(b);
					                }, 0 );
					            console.log(total);

					            // Total over this page
					            pageTotal = api
					                .column( 15, { page: 'current'} )
					                .data()
					                .reduce( function (a, b) {
					                    return intVal(a) + intVal(b);
					                }, 0 );
					            console.log(pageTotal);

					            // Update footer
					            $( api.column( 15 ).footer() ).html('S/.'+       pageTotal       );
					    }
			    });
			}, param
	);
}

function printReporteInfocorp(event){
	event.preventDefault(); 
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
	document.location.href = _URL + "api/pagos/excel?id_anio=" + $('#_id_anio').text()+"&id_mes="+$('#id_mes').val()+"&id_suc="+id_suc;
}
	
function generarCartas(){
	var id_suc;
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 )
		id_suc='';
	else
		id_suc=_usuario.id_suc;
	document.location.href = _URL + "api/pagos/carta_cobranza1?id_anio=" + $('#_id_anio').text()+"&id_mes="+$('#id_mes').val()+"&id_suc="+id_suc;
}