//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
var aulas_existen;
var aulas_desaprobados;
function onloadPage(params) {
	
	_onloadPage();	

	$('#id_niv').on('change',function(event){
		var param ={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});
	
	$('#id_gra').on('change',function(event){
		var param={id_gra:$(this).val(),id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
			_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),null, param, function() {$('#id_au').change();});
		
		/*
		_get('api/combo/col_aula/' + (parseInt($('#_id_anio').text()) + 1) + ',' + $(this).val(), function(data){//MEJORAR ESTA FUNCION
			_secciones_anio_proximo = data;
			console.log(_secciones_anio_proximo);
		});
		*/
		
		
	});
	
	$('#id_au').on('change',function(event){
		var param1={id_gra:$('#id_gra').val(),id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val()};
		_get('api/aula/listarAulasxGradoLocal', function(data){
			aulas_existen = data;
			//aulas_desaprobados = data.aulas_desaprobados;

			if ($('#id_au').val()>-1){
				var param = {id_anio:$('#_id_anio').text(),id_gir:$('#id_gir').val(), id_gra:$('#id_gra').val(), id_au:$('#id_au').val(), id_niv:$('#id_niv').val()}
				return _get('api/matricula/reporteMatriculaListCambioSec',fncExito, param);
				
			}else{
				$('#mat_seccion_sugerida-tabla').hide();
			}
			
		},param1);
		
		
					
	});
	
	$('#id_gir').on('change',function(event){
		$('#id_gra').change();
	});	
	_llenarCombo('cat_nivel',$('#id_niv'), null,null,function(){ $('#id_niv').change()});
	_llenarCombo('ges_giro_negocio',$('#id_gir'), null,null,function(){ $('#id_gir').change()});
	
	$('#btn-grabar').hide();

	$('#btn-grabar').on('click', function(e){
		
		swal(
				{
					title : "Esta seguro?",
					text :'Se actualizará el aula sugerida en función de la situación final',
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "rgb(33, 150, 243)",
					cancelButtonColor : "#EF5350",
					confirmButtonText : "Si, actualizar",
					cancelButtonText : "No, cancela!!!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
						
						var param ={id_au:$('#id_au').val(), id_anio:$('#_id_anio').text()};
						 _post('api/seccionSugerida/generar',param, function(data){
							 console.log(data);
							 $('#id_au').change();
						 });
						 
							 
						
					} else {
						swal({
							title : "Cancelado",
							text : "No se ha actualizado ninguna aula sugerida",
							confirmButtonColor : "#2196F3",
							type : "error"
						});
					}
				});
		
	});
	
	var fncExito = function(data){
		
		console.log(data);
		//$('#btn-grabar').show();
		/*if (data.con_sugerencia==0)// SIGNIFICA CANTIDAD DE ALUMNOS Q TIENEN AULA SUGERIDA
			$('#btn-grabar').show();
		else
			$('#btn-grabar').hide();*/
			
		$('#mat_alumnos_seccion-tabla').show();
		$('#mat_alumnos_seccion-tabla').dataTable({
			 data : data,
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Alumno", "data" : "Alumno"},
	     	           {"title": "Genero", "data" : "Genero"},
	     	           {"title": "Nro Doc Papa", "data" : "DNIPadre"},
	     	           {"title": "Nro Doc mama", "data" : "DNIMadre"},
	     	           //{"title": "Nivel", "data" : "nivel"},
	     	          // {"title": "Situacion Final", "data" : "nom"},
					    {"title": "Matricula Anterior", "data" : "aula_anterior"},
	     	           {"title": "Grado matricula", "data" : "grado"},
					   {"title": "Seccion originaria", "data" : "secc_org"},
	     	           {"title": "Seccion Final", "render":function ( data, type, row,meta ) {
	     	        	  var combo; 
	     	           
	     	        	  // if (row.id_sit == 1 || row.id_sit == 2)
	     	        		   combo = _getComboJSON( aulas_existen,row.sug_id,row.id_au, row.id);
	     	        	  /* else
	     	        		  combo = _getComboJSON( aulas_desaprobados,row.sug_id,row.sug_au_id, row.id_mat);*/
	     	        	   
	     	        	   return combo;
	     	           } }
	        ],
	        "initComplete": function( settings ) {
	        	$("<span><a href='#' target='_blank' onclick=''> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			 }

	    });
		
	};
	
	$('#btn-generar').on('click', function(e){
		
		swal(
				{
					title : "Esta seguro?",
					text :'Se actualizará la seccion del alumno.',
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "rgb(33, 150, 243)",
					cancelButtonColor : "#EF5350",
					confirmButtonText : "Si, procesar",
					cancelButtonText : "No, cancela!!!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					if (isConfirm) {
						
						var param ={id_anio:$('#_id_anio').text()};
						 _post('api/seccionSugerida/generar',param, function(data){
							 console.log(data);
							 $('#id_au').change();
						 });
						 
							 
						
					} else {
						swal({
							title : "Cancelado",
							text : "No se ha actualizado ninguna aula sugerida",
							confirmButtonColor : "#2196F3",
							type : "error"
						});
					}
				});
		
	});
	

	
	
	


}

 

function _getComboJSON( data,sug_id, sug_au_id, id_mat ){

	
	var s = $('<select/>');
	if (sug_au_id==null)
		sug_au_id = '';
	
	$('<option />', {value: '-1', text: 'seleccione'}).appendTo(s);
	 
	 for ( var i in data) {
		 var id = data[i].id;
		 var value = data[i].value;
			
	    //$('<option data-id_secc_sug="' + _id  + '" />', {value: id, text: value}).appendTo(s);
	    //$('<option/>', {value: id, text: value}).appendTo(s);
		 if (sug_au_id!='' && id==sug_au_id )
			 s.append('<option selected value="'+ id + '">' + value + '</option>');
		 else
			 s.append('<option value="'+ id + '">' + value + '</option>');
 
	}
	 
	  //if (_id!='') s.val(_id);
	 
 

	 return '<select class="form-control select-search" data-sug_id="' + sug_id+ '"  data-sug_au_id="' + sug_au_id + '" data-id_mat="' + id_mat + '" onchange="onchangeCombo(this)">' +  s.html() + '</select>';
	
	  
}
function onchangeCombo( field){
	var combo = $(field);
	var id_au_sug = combo.val();
	var id_mat = combo.data('id_mat');
	var sug_id = combo.data('sug_id');
	var sug_au_id = combo.data('sug_au_id');//de la bd
	
	if (sug_au_id=='')
		sug_au_id = null;
	if (sug_id=='')
		sug_id = null;
	
	//var param ={id:sug_id, sug_au_id:sug_au_id,id_au:$('#id_au').val(), id_mat:id_mat, id_anio:$('#_id_anio').text(), id_au_sug : id_au_sug };
	var param ={id:id_mat,id_au : id_au_sug };
	
	swal(
			{
				title : "Esta seguro?",
				text :'Se actualizará la sección.',
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, actualizar",
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					
					_post('api/matricula/actualizarSeccion',param, function(data){
						 console.log(data.result);
						 combo.data('sug_id',data.result);
					});
						 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha actualizado la sección.",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
					
					combo.val(sug_au_id);//regresa a su estado anterior
				}
			});
	

}