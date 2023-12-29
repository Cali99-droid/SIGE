//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	$('#id_mat').val(params.id_mat);
	$("#alumno").text(params.alumno);
	$("#nivel").text(params.nivel);
	$("#grado").text(params.grado);
	$("#seccion").text(params.secc);
	historial_conductual(params.id_mat);
	var param ={id_mat:params.id_mat, tip:'C'}
	
	_GET({
		context:_URL,
		params: param,
		url:'api/condicion/obtenerBloqueo',
		success:function(data){
			console.log(data);
			$('#mat_blo').val(data.mat_blo);
			$('#obs_blo').val(data.obs_blo);
			if(data.mat_blo=="1"){
				$('#mat_blo').prop("checked", true);
			}
			$('#mat_condicion-frm #id').val(data.id);
		}
	});
	/*_get('api/condicion/obtenerBloqueo', function(data) {
				console.log(data);
				$('#mat_blo').val(data.mat_blo);
				$('#obs_blo').val(data.obs_blo);
				if(data.mat_blo=="1"){
					$('#mat_blo').prop("checked", true);
				}
				$('#mat_condicion-frm #id').val(data.id);
	},param);*/
	if(params.id_cond){
		_get('api/condicion/' + params.id_mat_cond+'/2', function(data) {
			console.log(data);
			$('#des').val(data.des);
		});
		_llenarComboURL('api/condAlumno/listarCondicionTipoCond',$('#id_cond'), params.id_cond);
		
		$('#id_cond').prop('disabled', 'disabled');
		$("#mat_condicion-frm #id").val(params.id_mat_cond);
		$('#btn-agregar_cond').hide();
		$('#btn-grabar_cond').hide();
		$('#bloqueo').css('display','none');
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			$('#btn-grabar_cond').show();
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
			$('#bloqueo').css('display','block');
		else
			$('#bloqueo').css('display','none');
		
		
	} else{
		_llenarComboURL('api/condAlumno/listarCondicionTipoCond',$('#id_cond'), null);
		if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
		$('#bloqueo').css('display','block');
		else
			$('#bloqueo').css('display','none');
		$('#btn-agregar_cond').hide();
	}
	
	if(_usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1 || _usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1)
		$("#mat_condicion-frm").css('display','block');
	
	$('#mat_condicion-frm #btn-grabar_cond').on('click',function(event){
		$('#id_cond').removeAttr('disabled');
		if($('#id_cond').val()!=''){
			$('#mat_blo').val('');
			$('#obs_blo').val('');
			$('#tip_blo').val('');
		} else{
			if($('#mat_blo').is(':checked'))
				$('#mat_blo').val('1');
			else
				$('#mat_blo').val('');
		}
		_post($('#mat_condicion-frm').attr('action') , $('#mat_condicion-frm').serialize(),
		function(data){
			$('#id_cond').prop('disabled', 'disabled');
			$('#mat_condicion-frm #btn-grabar_cond').prop('disabled', 'disabled');
			}
		);
	});
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

});

$('#btn_nuevo-reporte').on('click',function(event){
	//alert();
	_send('pages/academico/col_reporte_conductual_modal.html','Registro de Incidencias','Reporte',{id_mat:$('#id_mat').val()});
});


function historial_conductual(id_mat) {
	var param=	{id_mat:id_mat};
	_GET({
		context:_URL_CON,
		url:'api/reporteConductual/listarHistorial/',
		params:param,
		success:function(data){
			console.log(data);
			$('#col_historial_conductual-tabla').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 orderCellsTop:true,
				 searching: false, 
				 select: true,
		         columns : [ 
		        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						{"title":"C&oacute;digo del reporte", "data" : "cod"}, 
						{"title":"Tipo de Incidencia", "data" : "tipo_incidencia"},
						{"title":"Celular del Apoderado", "data" : "cel"}, 
						{"title":"Alumno", "data" : "alumno"}, 
						//{"title":"Reporta Incidencia", "data": "tutor"}, 
						//{"title":"Falta Cometida", "data" : "nom"},
						{"title":"Descripci&oacute;n del Informante", "data" : "descripcion"}, 
						//{"title":"Periodo", "data": ""}, 
						//{"title":"Informante del Caso", "data": "informante"}, 
						{"title": "Acciones", "data" : "id","render":function ( data, type, row,meta ) { 
		     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id_inc + '" onclick="ver_detalle(this, event)" class="list-icons-item" title="Ver Detalle"><i class="icon-file-text2 mr-3 icon-2x ui-blue ui-size" aria-hidden="true"></i></a> </div>';
		     	         }}
			    ],
			    "initComplete": function( settings ) {
					   _initCompleteDT(settings);
				 }
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