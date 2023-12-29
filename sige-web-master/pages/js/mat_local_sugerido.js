//Se ejecuta cuando la pagina q lo llama le envia parametros
var _param = {};//parametros de busqueda;
var aulas_aprobados;
var aulas_desaprobados;
var locales_disponibles;
function onloadPage(params) {
	
	_onloadPage();	

	_llenarCombo('ges_giro_negocio',$('#id_gir'), null,null,function(){ $('#id_gir').change()});
	
	$('#id_niv').on('change',function(event){
		var param ={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null,param,function(){$('#id_gra').change()});
	});

	
	$('#id_gra').on('change',function(event){
		_get('api/combo/col_aula_ant/' + $('#_id_anio').text() + ',' + $(this).val(), function(data){
			_secciones_anio_actual = data;
			_llenarComboJSON($('#id_au'), data,null ,function(){
				//lista de aulas del proximo año

				
				$('#id_au').change();
				});

		});
		
		/*
		_get('api/combo/col_aula/' + (parseInt($('#_id_anio').text()) + 1) + ',' + $(this).val(), function(data){//MEJORAR ESTA FUNCION
			_secciones_anio_proximo = data;
			console.log(_secciones_anio_proximo);
		});
		*/
		
		
	});
	
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
		
		if (data.con_sugerencia==0)// SIGNIFICA CANTIDAD DE ALUMNOS Q TIENEN AULA SUGERIDA
			$('#btn-grabar').show();
		else
			$('#btn-grabar').hide();
			
		$('#mat_seccion_sugerida-tabla').show();
		$('#mat_seccion_sugerida-tabla').dataTable({
			 data : data.alumnos,
			 destroy: true,
			 pageLength: 1000,
			 select: true,
	         columns : [ 
	     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
	     	           {"title": "Alumno", "data" : "alumno"},
	     	           {"title": "Genero", "data" : "genero"},
	     	           {"title": "Nro Doc Papa", "data" : "papa_dni"},
	     	           {"title": "Nro Doc mama", "data" : "mama_dni"},
	     	           //{"title": "Nivel", "data" : "nivel"},
	     	           {"title": "Situacion Final", "data" : "nom"},
	     	           {"title": "Grado matricula", "data" : "sug_grado"},
	     	           {"title": "Local Sugerido", "render":function ( data, type, row,meta ) {
	     	        	  var combo; 
	     	           
	     	        	  /* if (row.id_sit == 1 || row.id_sit == 2)
	     	        		   combo = _getComboJSON( aulas_aprobados,row.sug_id,row.sug_au_id, row.id_mat);
	     	        	   else
	     	        		  combo = _getComboJSON( aulas_desaprobados,row.sug_id,row.sug_au_id, row.id_mat);*/
						   combo = _getComboJSON( locales_disponibles,row.sug_id,row.sug_id_suc, row.id_mat);
	     	        	   
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
					text :'Se actualizará el aula sugerida para todo el colegio del año actual',
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
	

	
	$('#id_au').on('change',function(event){

		//_get('api/aula/siguientePeriodo?id_au=' + $(this).val() +'&id_gra=' +  $('#id_gra').val() + "&id_anio=" + $('#_id_anio').text(), function(data){
			var id_anio=$('#_id_anio').text();
			var id_anio_sig=parseInt(id_anio)+1;
			//var id_niv=$('#id_niv').val();
			//var id_gra=$('#id_gra').val();
			
			var param = {id_anio: id_anio_sig }
		_get('api/aula/listarLocalesxAnio', function(data){
			locales_disponibles=data;
			//aulas_aprobados = data.aulas_aprobados;
			//aulas_desaprobados = data.aulas_desaprobados;

			if ($('#id_au').val()>-1){
				var param = {id_au:$('#id_au').val(), id_anio:$('#_id_anio').text()}
				return _get('api/seccionSugerida/listar',fncExito, param);
				
			}else{
				$('#mat_seccion_sugerida-tabla').hide();
			}
			
		}, param);
		
		
					
	});
	
	_llenarCombo('cat_nivel',$('#id_niv'), null,null,function(){ $('#id_niv').change()});
	


}

 

function _getComboJSON( data,sug_id, sug_id_suc, id_mat ){

	
	var s = $('<select/>');
	if (sug_id_suc==null)
		sug_id_suc = '';
	
	$('<option />', {value: '-1', text: 'seleccione'}).appendTo(s);
	 
	 for ( var i in data) {
		 var id = data[i].id;
		 var value = data[i].value;
			
	    //$('<option data-id_secc_sug="' + _id  + '" />', {value: id, text: value}).appendTo(s);
	    //$('<option/>', {value: id, text: value}).appendTo(s);
		 if (sug_id_suc!='' && id==sug_id_suc )
			 s.append('<option selected value="'+ id + '">' + value + '</option>');
		 else
			 s.append('<option value="'+ id + '">' + value + '</option>');
 
	}
	 
	  //if (_id!='') s.val(_id);
	 
 

	 return '<select class="form-control select-search" data-sug_id="' + sug_id+ '"  data-sug_id_suc="' + sug_id_suc + '" data-id_mat="' + id_mat + '" onchange="onchangeCombo(this)">' +  s.html() + '</select>';
	
	  
}
function onchangeCombo( field){
	var combo = $(field);
	var id_suc_sug = combo.val();
	var id_mat = combo.data('id_mat');
	var sug_id = combo.data('sug_id');
	var sug_id_suc = combo.data('sug_id_suc');//de la bd
	
	if (sug_id_suc=='')
		sug_id_suc = null;
	if (sug_id=='')
		sug_id = null;
	
	var param ={id:sug_id, sug_id_suc:sug_id_suc, id_mat:id_mat, id_anio:$('#_id_anio').text(), id_suc_sug : id_suc_sug };
	
	swal(
			{
				title : "Esta seguro?",
				text :'Se actualizará el local sugerido',
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
					
					_post('api/seccionSugerida/grabarLocal',param, function(data){
						 console.log(data.result);
						 combo.data('sug_id',data.result);
					});
						 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha actualizado el local sugerido",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
					
					combo.val(sug_id_suc);//regresa a su estado anterior
				}
			});
	

}