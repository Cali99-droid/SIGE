//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	var rol = _usuario.roles[0];
	/*_llenarCombo('cat_nivel',$('#id_niv'));
	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	*/
	
	/*$('#id_nvl').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	});*/

	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	

	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_niv').on('change',function(event){
		var id_niv=$('#id_niv').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	
	/*$('#id_gir').on('change',function(event){
		//if (rol=='1' || rol=='18'){

			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		/*} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
		}	*/
		
	/*	var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); */
	
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		}	
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); 
		
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}
	

	/*$('#id_gra').on('change',function(event){
		_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
	});*/
	/*$('#id_gra').on(
	'change',
	function(event) {
		/*_llenarCombo('col_aula', $('#id_au'), null, $("#_id_anio")
				.text()
				+ ',' + $(this).val());*/
	/*	var param ={id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_gir:$('#id_gir').val()}
		_llenarComboURL('api/aula/listarAulasxGiroNivelGrado',$('#id_au'), null, param, function(){
			
		});
	});
	
	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(), function (){
			$('#id_gra').change();
		});
	});
	
	_llenarCombo('ges_giro_negocio', $('#id_gir'), null, null, function() {
	});*/

	$('#frm-seccion-buscar').on('submit',function(event){
		event.preventDefault();

		return seccion_listar_tabla($("#id_au").val());
	});	

});


function seccion_listar_tabla(id_au){
	_get('api/matricula/seccion/' + id_au,
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
				   	       {"title": "C&oacute;digo", "data":"cod"}, 
				   	       {"title": "Alumno", "data":"Alumno"} 
				    ],
			        fnDrawCallback: function (oSettings) {
			        	$("<span><a href='#' target='_blank' title='IMPRIMR CARNET COLEGIO' onclick='printGeneracionSeccion(event)'> <i class='icon-newspaper'></i></a>&nbsp;</span><span><a href='#' target='_blank' title='IMPRIMR PDF CODIGO DE BARRAS' onclick='printGeneracionCodigoSeccion(event)'> <i class='icon-file-pdf'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			        }
			    });
			}
	);

}


function printGeneracionSeccion(event){
	event.preventDefault();
	document.location.href = _URL + "api/archivo/seccion/" + $("#id_au").val()+"/"+$("#id_gir").val()+"/"+$("#id_cic").val();
	// window.location.href = _URL + "api/archivo/seccion/" + $("#id_au").val();
	 //var url= _URL + "api/archivo/seccion/" + $("#id_au").val();
	// console.log(url);
  // location.href= url;

// window.open(url, 'Download');
}

function printGeneracionCodigoSeccion(event){
	event.preventDefault();
	document.location.href = _URL + "api/archivo/seccion2/" + $("#id_au").val()+"/"+$("#id_gir").val()+"/"+$("#id_cic").val();
	// window.location.href = _URL + "api/archivo/seccion/" + $("#id_au").val();
	 //var url= _URL + "api/archivo/seccion/" + $("#id_au").val();
	// console.log(url);
  // location.href= url;

// window.open(url, 'Download');
}