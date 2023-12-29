//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	

	_llenarCombo('cat_nivel',$('#id_niv'));

	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(), function(){$('#id_gra').change()});
	});

	$('#id_gra').on('change',function(event){
		_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
	});
	
	$('#frm-seccion-buscar').on('submit',function(event){
		event.preventDefault();
		familiar_listar_tabla($('#id_suc').val());
	});

	
});


function familiar_listar_tabla(id_suc){
	$('#id_anio').val($('#_id_anio').text());
	_get('api/familiar/usuario',
			function(data){
			console.log(data);
				$('#alumno-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 pageLength: 50,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
				   	       {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				   	       {"title": "Nro doc", "data":"nro_doc"}, 
				   	       {"title": "Apoderado", "render": function ( data, type, row ){return row.ape_pat + ' ' + row.ape_mat + ', ' + row.nom;}},
				   	       {"title": "Password", "data":"pass"}, 
				    ],
			        fnDrawCallback: function (oSettings) {
			        	$("<span><a href='#' target='_blank' title='IMPRIMR PDF' onclick='printGeneracionSeccion(event)'> <i class='icon-file-pdf'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			        }
			    });
			}, $('#frm-seccion-buscar').serialize()
	);

}


function printGeneracionSeccion(event){
	event.preventDefault();
	document.location.href = _URL + "api/familiar/exportarClaves/" + $("#id_au").val();
}