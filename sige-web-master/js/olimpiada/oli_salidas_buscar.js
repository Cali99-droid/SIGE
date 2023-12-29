function onloadPage(params){
	_onloadPage();
	
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		var param={id_anio:$("#_id_anio").text()};
		_llenar_combo({
		//tabla : 'oli_config',
		url : 'api/config/listarOlimpiadasxAnio',
		params: param,
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
		});
	} else{
		var param={id_anio:$("#_id_anio").text(),id_usr:_usuario.id};

		_llenar_combo({
			url : 'api/config/listarOliVigxUsuario',
			combo : $('#id_oli'),
			params: param,
			context : _URL_OLI,
			funcion : function() {
				$('#id_oli').change();
			}
		});
	}
}

$(function(){
	$("#alumno").focus();
	
	$('#frm-salidas-buscar').on('submit',function(event){
		event.preventDefault();

		$('#panel-alumnos').css("display","block");
		
		
		_GET({  url:'api/reporte/buscarInscritos',
				params: $(this).serialize(),
				context:_URL_OLI,
				success:function(data){
					console.log(data);
					$('#tabla-alumnos').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 pageLength: 50,
						 select: true,
				         columns : [ 
				           {"title": "Nro doc.", "data" : "nro_dni"},
				           {"title":"Alumno", "data" : "alumno"},
				           {"title":"Colegio", "data" : "colegio"}, 
					       {"title":"Nivel", "data" : "nivel"}, 
					       {"title":"Grado", "data" : "grado"}, 
					       {"title":"Hora de ingreso", "data" : "hor_ing"}, 
					       {"title":"Hora de salida", "data" : "hor_sal"}, 
					       {"title": "Editar", "render":function ( data, type, row,meta ) {
								return "<a href=\"javascript:editar(" + meta.row +")\"><i class='fa fa-pencil  ui-blue'></i></a>"; 
					        }}
				        ]
				    });
				
			}
		});

	});
	

	
});


function editar(index){
	
	var table = $('#tabla-alumnos').DataTable();
	var row = table.rows(index).data()[0];
	
	console.log(row);

	//funcion q se ejecuta al cargar el modal
		var onShowModal = function(){
			$('#nom').val(row.alumno);
			$('#config-modal-frm #id_oli').val($('#frm-salidas-buscar #id_oli').val());
			$('#id').val(row.id);
			$('#hor_ing').val(row.hor_ing);
			$('#hor_sal').val(row.hor_sal);
			
			$('.daterange-time').daterangepicker({
	            timePicker : true,
	            singleDatePicker:true,
	            timePicker24Hour : true,
	            timePickerIncrement : 1,
	            timePickerSeconds : true,
	            locale : {
	                format : 'hh:mm:ss'
	            }
	        }).on('show.daterangepicker', function(ev, picker) {
	            picker.container.find(".calendar-table").hide();
	   });
			
			$('#config-modal-frm').off();
			$('#config-modal-frm').on('submit', function(event){
				event.preventDefault();
				_POST({
					form:$('#config-modal-frm'), 
					context:_URL_OLI,
					success:function(data){
						$('#frm-salidas-buscar').submit();
						$(".modal .close").click();
					}});
			});
			
			
		}
		
		_modal('Actualizar hora de salida','pages/olimpiada/oli_salidas_modal.html',onShowModal);

	
}