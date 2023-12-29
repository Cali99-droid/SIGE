//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	
	$('#col_reporte_conductual-frm #btn-agregar').on('click',function(event){
		$('#col_reporte_conductual-frm #id').val('');
		_post($('#col_reporte_conductual-frm').attr('action') , $('#col_reporte_conductual-frm').serialize(),
		function(data){
				onSuccessSave(data) ;
			}
		);
	});
	
/*	if (link.data('id')){
		_get('api/reporteConductual/' + link.data('id'),
		function(data){
			_fillForm(data,$('#modal').find('form') );
			_llenarCombo('per_periodo',$('#id_per'), data.id_per);
			_llenarCombo('seg_usuario',$('#id_usr'), data.id_usr);
			_llenarCombo('col_curso_aula',$('#id_cca'), data.id_cca);
		});
	}else{
		_llenarCombo('per_periodo',$('#id_per'));
		_llenarCombo('seg_usuario',$('#id_usr'));
		_llenarCombo('col_curso_aula',$('#id_cca'));
		$('#col_reporte_conductual-frm #btn-grabar').hide();
	}*/

}

function _onchangeAnio(id_anio, anio){
	reporte_conductual_listar_tabla();	
}

//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	//$('#_botonera').html('<li><a href="pages/academico/col_reporte_conductual_buscar.html" id="col_reporte_conductual-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Incidente</a></li>');

	$('#col_reporte_conductual-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		reporte_buscar_modal($(this));
	});

	//lista tabla
	reporte_conductual_listar_tabla();
});

function reporte_conductual_eliminar(link){
	_delete('api/reporteConductual/' + $(link).data("id"),
			function(){
					reporte_conductual_listar_tabla();
				}
			);
}

function reporte_conductual_editar(row,e){
	e.preventDefault();
	var link = $(row);
	_send('pages/academico/col_historial_conductual_alumno.html','Historial Conducutal del Alumno','Lista de historiales',{alumno:link.data('alumno'), nivel:link.data('nivel'), grado:link.data('grado'), seccion:link.data('seccion'), id_mat:link.data('id_mat'), id_mat_cond:link.data('id_mat_cond'), id_cond:link.data('id_cond')});
	
}

function reporte_buscar_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_reporte_buscar-frm');
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			id_rol=_ROL_ADMINISTRADOR;
		else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
			id_rol=_ROL_PROFESOR;
		else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
			id_rol=_ROL_TUTOR;
		
		$("#alumno").autocomplete({ //id_anio, Integer id_rol, Integer id_usr
	        minLength: 3,
	        source: _URL + 'api/alumno/autocompleteCondicionConductual?id_anio=' + $('#_id_anio').text() + '&id_rol=' +id_rol+'&id_usr='+_usuario.id ,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	var alumno=ui.item.label;
	        	var nivel=ui.item.nivel;
	        	var grado=ui.item.grad;
	        	var seccion=ui.item.secc;
	        	var id_mat=ui.item.id;
	        	$(".modal .close").click();//aca pones el class de tu modal, debe llamarse asi.. prueba
	        	//_send('pages/academico/col_reporte_conductual_modal.html','Reporte Conductual','Reporte Conductual');
	        	_send('pages/academico/col_historial_conductual_alumno.html','Historial Conducutal del Alumno','Lista de historiales',{alumno:alumno, nivel:nivel, grado:grado, seccion:seccion, id_mat:id_mat});
	        },
	       
	    });
		
		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_conductual_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Incidente';
	else
		titulo = 'Nuevo  Incidente';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}



function reporte_conductual_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_reporte_conductual-frm');
		
		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_conductual_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Incidente';
	else
		titulo = 'Nuevo Incidente';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function reporte_conductual_listar_tabla(){
	if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
		id_rol=_ROL_ADMINISTRADOR;
	else if(_usuario.roles.indexOf(_ROL_PROFESOR)>-1)
		id_rol=_ROL_PROFESOR;
	else if(_usuario.roles.indexOf(_ROL_TUTOR)>-1)
		id_rol=_ROL_TUTOR;
	else if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1)
		id_rol=_ROL_COORDINADOR_TUTOR;
	
	var param={id_anio:$('#_id_anio').text(), id_rol:id_rol, id_usr:_usuario.id};
	_GET({
		context:_URL_CON,
		url:'api/reporteConductual/listarAlumnos/',
		params:param,
		success:function(data){
			console.log(data);
			$('#col_reporte_conductual-tabla').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 orderCellsTop:true,
				 select: true,
		         columns : [
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						//{"title":"C&oacute;digo del reporte", "data" : "cod"}, 
						//{"title":"N&uacute;mero de Registro Siseve", "data" : "nro_reg"}, 
						{"title":"Alumno", "data" : "alumno"}, 
						{"title":"Nivel", "data" : "nivel"}, 
						{"title":"Grado", "data" : "grado"}, 
						{"title":"Sección", "data": "seccion"}, 
						{"title":"Situación Matrícula", "data": "situacion"}, 
						{"title":"Condición Conductual", "data": "condicion"}, 
						{"title": "Cantidad de Hojas Conductuales",  "render": function ( data, type, row ) {
							var cant_inc=row.cant_rep_con + row.cant_rep_bu;
		    	        		   return cant_inc;
						}},
						//{"title":"Cantidad de Hojas Conductuales", "data": "cant"}, 
						{"title": "Bloqueo Conductual",  "render": function ( data, type, row ) {
							//console.log(row.tip_blo.length);
		    	        	   if (row.tip_blo!='' ){
		    	        		  // if((row.tip_blo.length)>0)
		    	        		   if (row.tip_blo!=null)
		    	        		  return '<font color="red">SI</font>';
		    	        		   else
		    	        			   return '<font color="blue">NO</font>';
		    	        		  
		    	        	   } else {
		    	        		   return '<font color="blue">NO</font>';
		    	        	   }
						}
						},
						{"title":"Acciones", "render": function ( data, type, row ) {
		                   return '<ul class="icons-list">'+
								'<li class="dropdown">'+
							'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
								'<i class="icon-menu9"></i>'+
								'</a>'+
							'<ul class="dropdown-menu dropdown-menu-right">'+
								'<li><a href="#" data-id_mat="' + row.id_mat + '" data-alumno="' + row.alumno + '"  data-nivel="' + row.nivel + '" data-grado="' + row.grado + '" data-seccion="' + row.seccion + '" data-rep_cond="' + row.rep_cond + '" data-id_mat_cond="'+row.id_mat_cond+'" data-id_cond="'+row.id_cond+'" onclick="reporte_conductual_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
								/*'<li><a href="#" data-id="' + row.id + '" onclick="reporte_conductual_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+*/
								/*'<li><a href="#" data-id="' + row.id + '" onclick="reporte_conductual_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+*/
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
	});
}	

/*function historial_conductual () {
	_get('api/reporteConductual/listar/',
			function(data){
			console.log(data);
				$('#col_reporte_conductual-tabla').dataTable({
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
							{"title":"C&oacute;digo del reporte", "data" : "cod"}, 
							{"title":"N&uacute;mero de Registro Siseve", "data" : "nro_reg"}, 
							{"title":"Observaci&oacute;n", "data" : "obs"}, 
							{"title":"Descripci&oacute;n del Docente", "data" : "des_doc"}, 
							{"title":"Descripci&oacute;n del Informante", "data" : "des_inf"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Informante del Caso", "data": "usuario.login"}, 
							{"title":"Curso", "data": "cursoAula.id_cua"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_conductual_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_conductual_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_conductual_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

