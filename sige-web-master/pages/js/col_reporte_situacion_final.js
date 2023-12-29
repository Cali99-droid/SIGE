//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
var list;
function onloadPage(params) {
	
	$('#id_gra').on('change',function(event){
		//_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $('#id_gra').val(),function() {$('#id_au').change();});
		//var param = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text()};
		var param={id_gra:$(this).val(),id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
	});

	$('#id_niv').on('change',function(event){
		var param ={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});
	
	_llenarCombo('cat_nivel',$('#id_niv'), null,null,function(){ $('#id_niv').change()});
	$('#id_gir').on('change',function(event){
		$('#id_gra').change();
	});	
	
	_llenarCombo('ges_giro_negocio',$('#id_gir'), null,null,function(){ $('#id_gir').change()});

}

/*function _onchangeAnio(id_anio, anio){
	//_onloadPage();
	alert();
	//$('#id_gra').val('');
	$('#id_gra').change();
	//$('#id_au').val('');
	$('#not_reporte_situacion_final').css('display','none');
}*/

$(function(){

	//alert(1);
	_onloadPage();	
	//$('#not_reporte_situacion_final').css('display','block');

	var fncExito = function(data){
		console.log(data);
		$('#not_reporte_situacion_final-tabla').dataTable({
			 data : data,
			 //aaSorting: [],
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Alumno", "data" : "alumno"},
	     	           {"title": "Nivel", "data" : "nivel"},
	     	           {"title": "Grado", "data" : "grado"},
	     	           {"title": "Seccion", "data" : "seccion"},
	     	           {"title": "Situacion Final", "data" : "nom","render":function ( data, type, row,meta ) { 
	     	        	   return "<a href='#' onclick='actualizarSituacion(\"" + row.alumno + "\"," + row.id_alu + "," +  row.id_mat  + "," + row.id_sit +  ")'>" + data  + "</a>";
	     	           } }
	        ],
	        "initComplete": function( settings ) {
	        	$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			 }

	    });
		
	};
	
	$('#frm-not_reporte_situacion_final').on('submit',function(event){
		event.preventDefault(); 
		var id_au=$('#id_au').val();
		return _get('api/matricula/listarSituacionFinal?id_au='+id_au,fncExito, $(this).serialize());
	});
});

function actualizarSituacion(alumno,id_alu,id_mat,id_sit){
	
	var onShowModal = function(){
		$('#mat_matricula_sit-frm #alumno').val(alumno);
		$('#mat_matricula_sit-frm #id_alu').val(id_alu);
		$('#mat_matricula_sit-frm #id_mat').val(id_mat);
		
		_llenarCombo('cat_col_situacion',$('#id_sit'), id_sit);
		console.log('onShowModal');

		/*
		$('#mat_matricula_sit-frm').on('submit', function(event){
			event.preventDefault();
			console.log('enviarrrrr');
			_post ('api/matricula/actualizarSituacion', $('#mat_matricula_sit-frm').serialize(), function (data){
				console.log(data);
			});
		});
		*/

	};
	var onSuccessSave = function(){
		//alert('onSuccessSave');
		$('#btn-cancelar').click();
		$('#btn-buscar').click();
		
	};
	
	_modal("Actualizar Situacion", 'pages/matricula/mat_matricula_situacion.html',onShowModal,onSuccessSave);
	
}