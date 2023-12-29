//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	/*$('#id_tip').on('change',function(event){
		listarIncidencia();
	});*/
	//_llenarCombo('ges_sucursal',$('#id_suc'));
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
			$('#id_suc').change();
		});		
		
	}else{
		if (id_suc!='0' ){
			var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text()
				};
			
			_llenarComboURL('api/comportamiento/listarSucursalTutor', $('#id_suc'), null, param,
					function() {
						$('#id_suc').change();
					});
			
		} else{
			_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
				$('#id_suc').change();
			});				
		}
		
	}
		
	$('#id_suc').on('change',function(event){
		if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			_llenarCombo('cat_nivel',$('#id_niv'));	
		}else{
			var param = { id_tra : _usuario.id_tra, id_anio : $('#_id_anio').text() };
			_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_niv'),null, param, function() { $('#id_niv').change(); });
			
		}
			
	});
	
	
	_llenarCombo('cat_estado_conductual',$('#id_cec'));
	_llenarCombo('cat_tip_inc',$('#id_tip'),null,null,function(){$('#id_tip').change();
	//listarIncidencia();
	});
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="#"  onclick="nueva_incidencia()" id="con_incidencia-btn-nuevo" ><i class="icon-file-plus"></i> Nueva Incidencia</a></li>');
	
	$('#id_suc').on('change',function(event){
		$('#con_incidencia-btn-nuevo').data('id_suc',$(this).val());
	});
	
});

function nueva_incidencia() {
	var param = {};
	param.id_suc=$('#id_suc').val();
	param.id_niv=$('#id_niv').val();
	_send('pages/conductual/con_reporte_incidencia.html','Busqueda Incidencia','Busqueda Incidencia',param);	
}

$('#btn-buscar').on('click', function(e) {
	e.preventDefault();
	listarIncidencia();
});

function compromiso_eliminar(link){
	_delete('api/compromiso/' + $(link).data("id"),
			function(){
					compromiso_listar_tabla();
				}
			);
}

function compromiso_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/con_compromiso_modal.html');
	compromiso_modal(link);
	
}

function compromiso_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('con_compromiso-frm');
		
		$('#con_compromiso-frm #btn-agregar').on('click',function(event){
			$('#con_compromiso-frm #id').val('');
			_post($('#con_compromiso-frm').attr('action') , $('#con_compromiso-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/compromiso/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('con_incidencia',$('#id_inc'), data.id_inc);
				_llenarCombo('con_formato_incidencia',$('#id_cfi'), data.id_cfi);
				_llenarCombo('ges_trabajador',$('#id_tra'), data.id_tra);
				_llenarCombo('con_tipo_compromiso',$('#id_ctc'), data.id_ctc);
			});
		}else{
			_llenarCombo('con_incidencia',$('#id_inc'));
			_llenarCombo('con_formato_incidencia',$('#id_cfi'));
			_llenarCombo('ges_trabajador',$('#id_tra'));
			_llenarCombo('con_tipo_compromiso',$('#id_ctc'));
			$('#con_compromiso-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		compromiso_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Compromiso';
	else
		titulo = 'Nuevo  Compromiso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

/*function compromiso_listar_tabla(){
	_get('api/compromiso/listar/',
			function(data){
			console.log(data);
				$('#con_compromiso-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Descripci&oacute;n del Caso", "data" : "des"}, 
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Compromiso de los Alumnos", "data" : "comp"}, 
							{"title":"Documento Escaneado", "data" : "archivo"}, 
							{"title":"Incidencia", "data": "incidencia.id_cti"}, 
							{"title":"Formato Incidencia", "data": "formatoIncidencia.nom"}, 
							{"title":"Directivo", "data": "trabajador.nom"}, 
							{"title":"Tipo Compromiso", "data": "tipoCompromiso.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="compromiso_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="compromiso_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}*/

function listarIncidencia(){
	var param={id_cec:$('#id_cec').val(), id_anio:$('#_id_anio').text(), id_tip:$('#id_tip').val(), id_niv:$('#id_niv').val(),id_rol:_usuario.roles[0], id_tra:_usuario.id_tra};
	_GET({
		context:_URL_CON,
		url:'api/incidencia/listarIncidencias/',
		params:param,
		success:function(data){
			console.log(data);
			$('#con_busqueda_incidencia-tabla').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 orderCellsTop:true,
				 select: true,
		         columns : [
		                    
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
		        	 	{"title":"Tipo de Reporte", "data": "tipo"},
		        	 	{"title":"Código", "data": "cod"},
		        	 	{"title":"Fecha", "data": "fecha"},
		        	 	{"title":"Estado", "data": "estado"},
		        	 //	{"title":"Alumno", "data": "alumno"},	
		        	 	/*{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
		        	 		//creamos un hidden x cada checkbox
		        	 		var checked = (row.eva_id!=null) ? ' checked ': ' ';
		        	 		var disabled = (checked) ? 'disabled':'';
		        	 		//var ideva = (row.eva_id!=null) ? row.eva_id: '';
		        	 		return "<input type='hidden' id='id_cf" + row.id +"' name='id_cf' " + disabled + "/><input type='checkbox' " + checked + "id='id_cf"+row.id+"'  name ='id_cf' value='" + row.id + "' class='id_cf' /><font align='center'>"+row.nom+"</font>";
		        	 	} 
		        	 	}*/
		        	 	/*{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" data-id_nep="' + row.id_nep + '"onclick="evaluacion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Tareas</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
						}*/
		        	 	{"title": "Acciones", "data" : "id","render":function ( data, type, row,meta ) { 
		     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="ver_detalle(this, event)" class="list-icons-item" title="Ver Detalle"><i class="icon-file-text2 mr-3 icon-2x ui-blue ui-size" aria-hidden="true"></i></a> </div>';
		     	         }}
			    ]
		    });
		}
	});
}

function ver_detalle(obj, e){
	e.preventDefault();
	var id = $(obj).data('id');
    
	var param = {};
	param.id=id;

	_send('pages/conductual/con_reporte_incidencia_det.html','Detalle de la Incidencia','Detalle de la Incidencia',param);	

}

