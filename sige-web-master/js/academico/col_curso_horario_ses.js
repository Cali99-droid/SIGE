//Se ejecuta cuando la pagina q lo llama le envia parametros
var i =0;
var _id_cca;

function onloadPage(params){
	var id_suc=$("#_id_suc").text();
	var param = {id_tra: _usuario.id_tra, id_anio: $('#_id_anio').text()};

	_llenarComboURL('api/evaluacion/listarNiveles',$('#id_niv'),null,param,function(){
		$('#id_niv').change();
	});

	$('#id_niv').on('change',function(){
		var param = {id_anio:$('#_id_anio').text(),id_niv:$('#id_niv').val()}
		
		_llenarComboURL('api/cursoUnidad/listaxNivel',$('#unidad'),null,param,function(){
			$('#unidad').change();
		});
	});
	
	$('#unidad').on('change',function(){
			
			_llenarComboApi('api/confSemanas/listarxUnidad?id_anio=' + $('#_id_anio').text() + '&id_niv=' + $('#id_niv').val() + '&num=' + $('#unidad').val(), 'nro_sem', $('#id_sem'), null, function(){
				$('#id_sem').change();
			});
		
	});
	

	$('#id_sem').on('change',function(){
		consultarCalendario();
	});
	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
});

function curso_horario_eliminar(link){
	_delete('api/cursoHorario/' + $(link).data("id"),
			function(){
					curso_horario_listar_tabla();
				}
			);
}

 

/*BUSQUEDA POR SEMANA */
function consultarCalendario(){
	
	var param	 = {};
	param.id_tra = _usuario.id_tra;
	param.id_sem = $('#id_sem').val();
	param.id_niv = $('#id_niv').val();
	param.id_suc = 0;
	_block_UI();
	return _get('api/cursoHorario/listarPorSemana',
		function(data){
			dibujarCalendario(data.primer_dia,data.fecha_inicio,data.horarios);
			$.unblockUI();
	}, param);
	
}

function dibujarCalendario(primer_dia,fecha_inicio, horarios){
	/*
	if( $('.fullcalendar-external').children().length>0 ){
		alert(primer_dia);
		$('.fullcalendar-external').fullCalendar( 'removeEvents');
		$('.fullcalendar-external').fullCalendar('firstDay', primer_dia);
		$('.fullcalendar-external').fullCalendar('gotoDate', fecha_inicio);

		$(".fullcalendar-external").fullCalendar('addEventSource', horarios); 
		
	    $('.fullcalendar-external').fullCalendar('refetchEvents');

		var events =  $('.fullcalendar-external').fullCalendar('clientEvents');

		//return;
	}
	*/
	
	if( $('.fullcalendar-external').children().length>0 ){
		$('.fullcalendar-external').fullCalendar('destroy');
	}
	
	console.log(primer_dia);
	console.log(horarios);
	
     // Initialize the calendar
    $('.fullcalendar-external').fullCalendar({
        header: {
            left: '',
            center: '',
            right: ''
        },
        editable: true,
        firstDay: primer_dia,//DIA DE SEMANA Q MUESTRA
        defaultDate: fecha_inicio,
        allDaySlot : false,
        slotDuration: '00:05:00', 
        events: horarios,
        locale: 'es',
        defaultView: 'agendaWeek',
        minTime : '07:00:00',
        maxTime : '19:00:00',
        views: {
            agenda: {
            	columnFormat: 'ddd DD/MM',
            }
        },
        hiddenDays: [ 0,6 ], 
        droppable: false, // this allows things to be dropped onto the calendar
        drop: function(info) {
            console.log(info);
            $(this).remove(); // if so, remove the element from the "Draggable Events" list
        },
        timeZone: 'America/Lima',
        defaultTimedEventDuration: '00:60:00',
		forceEventDuration: true,
		eventOverlap:false,
		editable:false,
		eventClick: function(calEvent, jsEvent, view) {
				console.log(calEvent);
				abrirModalSesiones(calEvent.extendedProps, calEvent.start._d);
		}
    });


  

}

/**
 * 
 * @param params (cch.id  + '-' + cch.id_cca + '-' + ccu.id)
 * @param fecha
 * @returns
 */
function abrirModalSesiones(params,fecha){
	
	
	var arr = params.split('-');
	var id = arr[0];
	var id_cca = arr[1];
	var id_ccu = arr[2];
	
	//VALIDAR QUE HAYA GRABADO LAS SESIONES DE LAS SEMANAS ANTERIORES
	var valido = false;
	_GET({url:'api/cursoHorarioSes/validarHorarioSesionxSemana',
		  async:false,
		  params:{id_ccu:id_ccu, id_cca:id_cca, nro_sem: $('#id_sem').val()},
		  success: function(data){
			  
			  valido = data;
		  }
		});

	if(!valido)
		return;
	
	var mes = _pad(fecha.getMonth() + 1,'00');
	var anio = fecha.getFullYear();
	var day = _pad(fecha.getDate(),'00');
	var hours = _pad(fecha.getHours() + 5,'00');
	var minutes = _pad(fecha.getMinutes(),'00');
	
	var fec = day + "/" + mes + "/" + anio  + " " + hours + ":" + minutes;

	console.log(params);
	_modal("Lista de sesiones", 'pages/academico/col_curso_horario_ses_modal.html',function(){
		
		//onload del modal
		var params ={id_cch: id, id_sem: $('#id_sem').val() };
		_get('api/cursoHorario/listarSessiones',
				function(data){
				var notas  = data.notas;
				//alert(notas);
				if(notas>0){
					//el usuario no podra eliminar la vinculacion con la sesion
					$('#btn-eliminar-sesion').attr('disabled','disabled');
					_alert_error('el usuario no podra eliminar la vinculacion con la sesion, porque tiene notas registradas para ese aula!!');
				}
			
				console.log(data);
					$('#col_semana_sesiones-tabla').dataTable({
						 data : data.sesiones,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 bLengthChange: false,
						 bPaginate: false,
						 bFilter: false,
						 bInfo: false,
						 bSort: false,
						 select: true,
				         columns : [ 
				        	    {"title":"Sesion", "data" : "nro"}, 
								//{"title":"Unidad", "data" : "unidad"}, 
				        	    {"title":"Tipo", "render": function ( data, type, row ) {
				        	    	var html = "<table width=100%>";
				        	    	var tipoSesionList = row.tipoSesionList;
				        	    	
				        	    	console.log(tipoSesionList);
				        	    	
				        	    	for(var i in tipoSesionList){
				        	    		var sutemas = tipoSesionList[i].subtemas;
				        	    		var htmlSubTemas = '';
				        	    		for(var j in sutemas){
				        	    			
				        	    			var st = sutemas[j].subtema;
				        	    			if(j>0)
				        	    				htmlSubTemas += '<br>';
				        	    			htmlSubTemas += st;
				        	    		}
				        	    		
				        	    		html += '<tr><td>' + tipoSesionList[i].nom  + '</td><td>' + htmlSubTemas + '</td></tr>';
				        	    	}
				        	    	html += '</table>';
				        	    	return html;
				        	    }},
				        	 	{"title":"Acciones", "render": function ( data, type, row ) {
									var checked = (row.id_cchs==null)?' ':' checked';
									return '<input type="radio" name="sesion" data-id=' + row.id_cchs + ' value="' + row.id +'"' + checked + ' />';
				                   }
								}
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
							   if($('input[name="sesion"]:checked').length==0){
								   $('#btn-grabar-sesion').show();
								   $('#btn-eliminar-sesion').hide();
							   }else{
								   $('#btn-eliminar-sesion').show();
								   $('#btn-grabar-sesion').hide();
							   }
								   
						 }
				    });
				},params
		);
		
		//grabar
		$('#btn-grabar-sesion').on('click',function(){
			var sesionesSeleccionadas =$('input[name="sesion"]:checked').length; 
			if (sesionesSeleccionadas>1){
				_alert_error('Debe seleccionar solamente una sesión.');
				return;
			}
			

			var frm={};
			frm.id_uns = $('input[name="sesion"]:checked').val();
			frm.id_cch = id;
			frm.id_ccs = $('#id_sem').val();
			frm.fec	   = fec;
			frm.est 	= 'A';
			if($('input[name="sesion"]:checked').data('id')!='null')
				frm.id 		= $('input[name="sesion"]:checked').data('id');
			
			_POST({ url:'api/cursoHorarioSes',
					params:frm,
					success:function(data){
						console.log(data);
						consultarCalendario();
			        	$(".modal .close").click();
					}
				   }
				);
		});

		//eliminar
		$('#btn-eliminar-sesion').on('click',function(){
			
			_DELETE({ url:'api/cursoHorarioSes/'+ $('input[name="sesion"]:checked').data('id') + "/" + id_cca ,

				success:function(data){
						console.log(data);
						consultarCalendario();
			        	$(".modal .close").click();
					}
				   }
				);
		});
		
	},function(){
		//onshowSuccess
	});	
}


 