//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	/* se utulizara este metodo
	$('#anio_com').on('change',function(event){
		habilitacion_listar_tabla();
	});
	
	*/
	
	//chancar el evento del combo año principal
	function _onchangeAnio(id_anio, anio){
		habilitacion_listar_tabla();	
	}

	
	$('#_botonera').html('<li><a href="pages/matricula/mat_habilitacion_modal.html" id="mat_habilitacion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Habilitaci&oacute;n de matricula</a></li>');

	$('#mat_habilitacion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		habilitacion_modal($(this));
	});

	//lista tabla
	habilitacion_listar_tabla();
});

function habilitacion_eliminar(link){
	_delete('api/habilitacion/' + $(link).data("id"),
			function(){
					habilitacion_listar_tabla();
				}
			);
}

function habilitacion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_habilitacion_modal.html');
	habilitacion_modal(link);
	
}

function habilitacion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_habilitacion-frm');
		
		$('.detalle').hide();
		
		$("#alumno").autocomplete({
	        minLength: 3,
	        source: _URL + 'api/habilitacion/buscarAlumnos?id_anio=' + $('#_id_anio').text() + '&id_suc=' + _usuario.id_suc,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	$('.detalle_alumno').show();
	        	$('#id_alu').val(ui.item.id_alu);
	        	console.log("_usuario",_usuario);
	        	$('#usuario').val(_usuario.trabajador.ape_pat+" "+_usuario.trabajador.ape_mat+" "+_usuario.trabajador.nom);
	        	$('#usr_ins').val(_usuario.id);
	        	$('#id_anio').val( $('#_id_anio').text());	        	
	        	
	        
	        	$('.detalle').show();
				$('#mat_habilitacion-frm #btn-grabar').show();	
	        },
	       
	    });
		
		$('#mat_habilitacion-frm #btn-agregar').on('click',function(event){
			$('#mat_habilitacion-frm #id').val('');
			_post($('#mat_habilitacion-frm').attr('action') , $('#mat_habilitacion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			$('.detalle').show();
			_get('api/habilitacion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//var fec= _parseDate(data.fec_fin);
				//$('#fec_fin').val(fec);
				$('#fec_fin').val(data.fec_fin);
				console.log(data.alumno.ape_pat);
				$('#alumno').val(data.alumno.ape_pat + ' ' + data.alumno.ape_mat + ' ' + data.alumno.nom );
				$('#id_alu').val(data.id_alu);
				var trab=data.trabajador.ape_pat+' '+data.trabajador.ape_mat+' '+data.trabajador.nom;
				$('#usuario').val(trab);
	        	$('#usr_ins').val(data.usr_ins);
	        	$('#id_anio').val(data.id_anio);
	        	$('#hab').on('change',function(event){
					if($('#hab').val()=='1'){
						$('#fec_fin').val(data.fec_fin);	
			    	} else{
			    	 $('#fec_fin').val('');
			    	}
				});
			});
		}else{
			_llenarCombo('alu_alumno',$('#id_alu'));
			_llenarCombo('col_anio',$('#id_anio'));
			$('#hab').on('change',function(event){
				if($('#hab').val()=='1'){
		    		var fecha = incrementarFecha(60); 
		        	$('#fec_fin').val(fecha);	
		    	} else{
		    		$('#fec_fin').val('');
		    	}
			});
			$('#mat_habilitacion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		habilitacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Habilitaci&oacute;n de matr&iacute;cula';
	else
		titulo = 'Nuevo  Habilitaci&oacute;n de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function onkeyAlumno(){

 	$('.detalle').hide();
	$('#mat_habilitacion-frm #btn-grabar').hide();


}

function habilitacion_listar_tabla(){
	var param={id_anio:$('#_id_anio').text()};
	_get('api/habilitacion/listar/',
			function(data){
			console.log(data);
				$('#mat_habilitacion-tabla').dataTable({
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
			        	 	{"title":"Alumno", "render": function ( data, type, row ) {
				            	   return row.alumno.ape_pat+" "+row.alumno.ape_mat+" "+row.alumno.nom;
				            }} , 
			        	 	//{"title":"Habilitado?", "data" : "hab"}, 
			        	 	{"title": "Habilitado?", "render":function ( data, type, row,meta ) { 
			     	        	   	if( row.hab== '1'){
				     	        		 return "<font color='blue'>SI</font>"; 
				     	        	}else { 
				    	        		 return "<font color='red'>NO</font>"; 
				    	        	} 
			     	        }},
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							{"title":"Año", "data": "anio.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="habilitacion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="habilitacion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}



