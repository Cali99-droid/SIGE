//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	$('#id_gir').on('change',function(event){
		grado_asignar_grupo($('#id_gir').val());
	});	
	$("#id_anio").val($('#_id_anio').text());
	_llenarCombo('ges_giro_negocio',$('#id_gir'), null,null,function(){ $('#id_gir').change()});
	//lista tabla
	//grado_asignar_grupo();
	
	/*$('#btn-generar').on('click',function(event){
		var params = $('#frm_generacion_usu_alu').serialize();
		_post('api/alumno/alumnoGenerarUsuarios',params, function(data){
			console.log(data);
			periodo_listar_generacion_usu_alu();
			
			/*for ( var i=0; i<data.length;i++){
				alert();
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_per=" +data.id_per;
			}*/
			
		/*	$("input[type=checkbox]:checked").each(function(){
				//monto_doc= monto_doc + parseFloat($(this).attr('monto'));
				document.location.href = _URL + "api/alumno/exportarFormatoUsuariosAlu?id_gra=" +$(this).val()+"&id_anio="+$('#_id_anio').text();
		    });
			
			//document.location.href = _URL + "api/matricula/exportarDirectorioPPFF?id_au=" + id_au+"&id_niv="+$("#id_nvl").val() + "&id_grad="+id_grad+"&usuario=" + _usuario.nombres + "&id_anio=" + $("#_id_anio").html();
		});
	});*/
	

});

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	grado_asignar_grupo(null);	
}

 

function grado_asignar_grupo(id_gir){
	
	var params = {id_anio : $('#_id_anio').text(), id_gir:id_gir};

	
	_get('api/periodo/listarCiclosxAnio',
			function(data){
			var param = {id_anio:$('#_id_anio').text()};
			console.log(data);
				$('#ciclo_tab').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 select: true,
				     bInfo: false,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Giro de Negocio", "data": "giro"}, 
							{"title":"Tipo Periodo", "data": "tipo_periodo"}, 
			        	 	{"title":"Ciclo", "data": "value"}, 
							{"title":"Estado", "render": function ( data, type, row ) { 
								if(row.est=='A'){
									return 'Activo';
								} else if(row.est=='I'){
									return '<font color="red">Inactivo</font>';
								} else
									return '';
							}},
			        	 	//{"title":"Grado", "data": "grado"},
			        	 	/*{"title":"20", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},
							{"title":"25", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},
							{"title":"30", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<input type="checkbox" id="btn-asignar" data-id_gra="' + row.id_gra + '" onclick="generar_grupos(this,event)"></input>';
							}},*/
							{"title":"Desactivar Ciclo", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<button type="button" id="btn-desactivar" class="btn btn-primary pull-right btn-sm" data-id_cic="' + row.id+ '" onclick="desactivar_grupos(this,event)">Desactivar Grupos Classroom</button>';
							}},
							{"title":"Desactivar Cuentas", "render": function ( data, type, row ) {
				                   //return '<input type="checkbox" ' + ((row.flag_sit==null) ? '': 'disabled') + ' ' + ((row.flag_sit==null) ? '': 'checked') + ' name="id" value="' + row.id + '" >';}
									return '<button type="button" id="btn-desactivar_cuentas" class="btn btn-primary pull-right btn-sm" data-id_cic="' + row.id+ '" onclick="desactivar_cuentas(this,event)">Desactivar Cuentas Classroom</button>';
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
			},params
	);

}

function desactivar_grupos(obj,event){
	event.preventDefault();
	swal(
		{
			title : "Esta seguro",
			text :'Se desactivara todos los grupos de este ciclo en el Google Classroom',
			type: "success",
			showCancelButton : true,
			confirmButtonColor : "rgb(33, 150, 243)",
			cancelButtonColor : "#EF5350",
			confirmButtonText : "Si, Continuar",
			cancelButtonText : "No, Salir!",
			closeOnConfirm : true,
			closeOnCancel : true,
			html:true
		},
		function(isConfirm) {

			if (isConfirm) {
				var id_cic = $(obj).data('id_cic');	
				_POST({url:'/api/matricula/desactivarGruposxCiclo/' + id_cic, context:_URL});

			}else{
					$(".modal .close").click();
				}
			}
		);
	
}

function desactivar_cuentas(obj,event){
	event.preventDefault();
	swal(
		{
			title : "Esta seguro",
			text :'Se desactivara todos las cuentas de este ciclo en Google Classroom',
			type: "success",
			showCancelButton : true,
			confirmButtonColor : "rgb(33, 150, 243)",
			cancelButtonColor : "#EF5350",
			confirmButtonText : "Si, Continuar",
			cancelButtonText : "No, Salir!",
			closeOnConfirm : true,
			closeOnCancel : true,
			html:true
		},
		function(isConfirm) {

			if (isConfirm) {
				var id_cic = $(obj).data('id_cic');	
				_POST({url:'/api/matricula/desactivarCuentasxCiclo/' + id_cic, context:_URL});

			}else{
					$(".modal .close").click();
				}
			}
		);
	
}

