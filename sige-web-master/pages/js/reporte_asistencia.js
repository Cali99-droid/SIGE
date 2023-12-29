var _roles;
function onloadPage(params){
	_onloadPage();
	var id_suc=$("#_id_suc").text();
	_roles = _usuario.usuariorols;
	var rol = _usuario.roles[0];	
	//_llenarCombo('cat_nivel',$('#id_niv'));
	

/*comentado desde aqui
	$('#id_gra').on('change',function(event){
 		var id_suc=$("#id_suc").val();
		//var param ={id_suc:$("#id_suc").val(), id_tra: _usuario.id_tra, id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val()}
		//_llenarComboURL('api/aula/listarAulas',$('#id_au'),null,param);
	if($('#id_gra').val()!=''){
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			var param ={id_suc:$("#id_suc").val(), id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_niv:$('#id_niv').val()}
			console.log(param);
			_llenarComboURL('api/aula/listarAulasxNivelGradoSucursal',$('#id_au'),null,param);
		} else if (_usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
			var param ={id_suc:$("#id_suc").val(), id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_niv:$('#id_niv').val()}
			_llenarComboURL('api/aula/listarAulasxNivelGradoSucursal',$('#id_au'),null,param);
		} else {
			if ($(this).val()!=''){

 	 		var param = {
 					id_tra : _usuario.id_tra,
 					id_grad : $(this).val(),
 					id_suc : $('#id_suc').val(),
 					id_anio : $('#_id_anio').text()
 				};
 			_llenarComboURL('api/comportamiento/listarAulaTutor', $('#id_au'),	null, param, function() {
 						$('#id_au').change();
 					});
			} else {
				$('#id_au').empty();
			}	
		}	
	}	
	
	});
	
	$('#id_niv').on('change',function(event){
		//_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val());
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			var id_niv=$('#id_niv').val();
			_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		} else if (_usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
			var id_niv=$('#id_niv').val();
			_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		} else {
			var param = {
				id_tra : _usuario.id_tra,
				id_anio : $('#_id_anio').text(),
				id_niv : $("#id_niv").val()
			};
			_llenarComboURL('api/comportamiento/listarGrados', $('#id_gra'),
				null, param, function() {
					$('#id_gra').change();
				});
		}		
	});*/
	
	//$('#id_suc').on('change',function(event){
		/*if (_roles!=null && existeRol(_ROL_SECRETARIA,_roles) ){
			_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val());
		}else{*/
	/*		var param = { id_tra : _usuario.id_tra, id_anio : $('#_id_anio').text() };
			_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_niv'),null, param, function() { $('#id_niv').change(); });
			
		//}

		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			_llenarCombo('cat_nivel',$('#id_niv'),null,$(this).val());
		} else if (_usuario.roles.indexOf(_ROL_SECRETARIA)>-1){
			_llenarCombo('cat_nivel',$('#id_niv'),null,$(this).val());
		}else{
			var param = { id_tra : _usuario.id_tra, id_anio : $('#_id_anio').text() };
			_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_niv'),null, param, function() { $('#id_niv').change(); });
			
		}
			
	});*/
	
	$('#id_gra').on('change',function(event){
		$('#id_cic').change();
	});	
	
	$('#id_cic').on('change',function(event){
		if(rol=='5'){
			var param={id_tra: _usuario.id_tra,id_grad:$('#id_gra').val(), id_cic:$('#id_cic').val()};
			_llenarComboURL('api/comportamiento/listarAulaAuxiliar', $('#id_au'),null, param, function() {$('#id_au').change();});
		} else {
			var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
			_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		}	
		
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	

	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_niv').on('change',function(event){
		if (rol=='5'){
			var param={id_tra: _usuario.id_tra, id_anio:$("#_id_anio").text(), id_niv: $('#id_niv').val()};
		_llenarComboURL('api/comportamiento/listarGradosAuxiliar',$('#id_gra'),null,param,function(){$('#id_gra').change();});
		} else {
			var id_niv=$('#id_niv').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		}	
		
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
		} if(rol=='5'){
			var param={id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text(), id_gir: $(this).val()};
			_llenarComboURL('api/comportamiento/listarNivelesAuxiliar', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		}	
		
		var param={id_gir:$('#id_gir').val(), id_anio:$("#_id_anio").text()};
		_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cic'),null,param,function(){$('#id_cic').change()});
	}); 
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
			$('#id_suc').change();
		});		
		
	} else if(_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		_llenarComboURL('api/usuario/listarLocales/'+_usuario.id,$('#id_suc'),null,null,function(){$('#id_suc').change();});
	} else{
		if (id_suc!='0' ){
			if(_usuario.roles.indexOf(_ROL_AUXILIAR)>-1){
				var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text()
				};
			
			_llenarComboURL('api/comportamiento/listarSucursalAuxiliar', $('#id_suc'), null, param,
					function() {
						$('#id_suc').change();
					});
			} else {
				var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text()
				};
			
			_llenarComboURL('api/comportamiento/listarSucursalTutor', $('#id_suc'), null, param,
					function() {
						$('#id_suc').change();
					});
			}	
			
			
		} else{
			_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
				$('#id_suc').change();
			});				
		}
		
	}
	
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else if ( _usuario.roles.indexOf(_ROL_AUXILIAR)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxAuxiliar/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	}
	else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}

}

function existeRol(id_rol, arreglo){
	for(var i in arreglo){
		if (id_rol == arreglo[i].id_rol)
			return true;
	}
	
	return false;
}

var fncExito = function(data){//tu html no veo reporte_asiustencia.html
	$('#panel-asistencia').css("display","block");
	console.log(data);//lina luego lo pruebas con GET, por que el post asume q 

	$('#tabla-asistencia').dataTable({
		 data : data ,
		 aaSorting: [],
		 destroy: true,
		 pageLength: 50,
		 select: true,
         columns : [ 
     	           {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
     	           {"title": "Alumno", "data" : "alumno"}, 
     	           {"title": "Cel. Apoderado", "data" : "celular"}, 
    	           {"title": "Grado", "data" : "grado"},
    	           {"title": "Seccion", "data" : "secc"},
    	          //title": "Fecha", "data" : "fecha"},
    	           {"title": "Hora Ingreso", "data" : "fecha_ori"},
    	           {"title": "Asistencia", "data" : "asistencia_nom"},
    	           {"title": "Observación", "data" : "observacion"},
		           {"title":"Acciones", "render": function ( data, type, row ) {
	                   return '<ul class="icons-list">'+
							'<li class="dropdown">'+
						'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
							'<i class="icon-menu9"></i>'+
							'</a>'+
						'<ul class="dropdown-menu dropdown-menu-right">'+
							'<li><a href="#" data-id="' + row.id_asi + '" onclick="asistencia_editar('+ row.id_asi + ',\'' + row.alumno +'\',\''+ row.asistencia +'\',\''+ row.fecha +'\',\''+row.cod+'\',\''+row.id_per+'\' )"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
						'</ul>'+
	                   '</li>'+
	                   '</ul>'; }
		           }
    	           
        ],
        fnDrawCallback: function (oSettings) {
        	$("<span><a href='#' target='_blank' onclick='printReporteCaja(event)'> <i class='fa fa-print'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
        }
    });
	
};

$('#form_asistencia').on('submit',function(event){
	event.preventDefault();
	//alert($("#_id_suc").html());
	var param = "";
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_SECRETARIA)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		 param ={id_niv:$("#id_niv").val(), id_gra:$("#id_gra").val(), id_au:$("#id_au").val(), fec:$("#fec").val(),  asistencia:$("#asis").val(), id_suc:$("#id_suc").val(), id_anio:$('#_id_anio').text(), id_tra : null, id_cic:$("#id_cic").val()};
	
	} else {
		 param ={id_niv:$("#id_niv").val(), id_gra:$("#id_gra").val(), id_au:$("#id_au").val(), fec:$("#fec").val(),  asistencia:$("#asis").val(), id_suc:$("#id_suc").val(), id_anio:$('#_id_anio').text(), id_tra : _usuario.id_tra, id_cic:$("#id_cic").val()};
	}	

	return _get('api/reporteAsistencia/reporte',
	function(data){
		fncExito(data);//se le tiene q pasar el resultado del controller
	},param); //aqui
});

function asistencia_editar(id_asi, alumno, asistencia, fecha, cod, id_per){
	//e.preventDefault();
	//alert(asistencia);
	console.log( _usuario.roles);
	if(asistencia=='F' && ( _usuario.roles.indexOf(_ROL_TUTOR)>-1 || _usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1))
	asistencia_modal(id_asi, alumno, asistencia, fecha, cod, id_per);
}


function asistencia_modal(id_asi, alumno, asistencia, fecha, cod, id_per){
	//funcion q se ejecuta al cargar el modal
	
	var onShowModal = function(){
		//	alert();
		$('#id').val(id_asi);
		$('#alumno').val(alumno); 
		$('#codigo').val(cod); 
		$('#id_per').val(id_per); 
		//$('#fecha_cor').val(fecha); 
		$('#fecha_cor').val($("#fec").val()); 
		$('#fecha_ori').val($("#fec").val()); 
		
		_get('api/reporteAsistencia/detalle/' + id_asi,
				function(data){
			
			//OJO QUE ESTO SE EJECUTA DESPUES DE OBTENER EL RESULTADO DEL DETALLE DE TU REGISTRO..
			_fillForm(data,$('.modal').find('form') );
			
			if(id_asi!=null)
			$('#asistencia').val(asistencia);
			});
			$("#fecha_ori").AnyTime_picker({
	        format: "%H:%i %m/%d/%y",//h:m:s a M/dd/yy
			});
		
	}

	//esto se EJECUTA al FINALIZAR EL BOTON GRABAR
	var onSuccessSave = function(data){
		$('#form_asistencia').submit();	
	}
	var titulo;
	titulo = 'Asistencia del Dia';
	
	_modal(titulo, 

'pages/asistencia/asi_asistencia_diaria.html',onShowModal,onSuccessSave);
	
}


