//Se ejecuta cuando la pagina q lo llama le envia parametros
var i =0;
var _id_cca;
function onloadPage(params){
	var id_suc=$("#_id_suc").text();
	
	_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){$('#id_suc').change()});

/*	$('#id_gra').on('change',function(event){
		var id_suc=$("#id_suc").val();
		_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc);
	});*/
	
	$('#id_niv').on('change',function(event){
		//_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$('#id_gra').change()});
		
		var param={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null, param,function(){$('#id_gra').change()});
		var param1={id_anio:$('#_id_anio').text(), id_niv:id_nvl};
		_llenarComboURL('api/trabajador/profesoresxNivel',$('#id_prof'),null, param1);	

	});
	
	$('#id_suc').on('change',function(event){
		_llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val(),function(){$('#id_niv').change()});
	});
	
	_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){$('#id_suc').change()});

	$('#btn-grabar').on('click', function(event){
	
		var events =  $('.fullcalendar-external').fullCalendar('clientEvents');
		
		if (events.length == 0){
			_alert_error('Por favor ingrese al menos un horario');
		}
		
		//constuir el json para grabar la semana
		var horarios = [];
	 
        for(var i=0; i< events.length; i++) {
			 console.log(events[i]);
           var eventsid = events[i].id;
           var params = events[i].extendedProps.split("-");
           var horarioReq ={};
           var _start = events[i].start._d;
           var _end = events[i].end._d;
           
           
           var day = _start.getDay();
           var start = _start.getHours() + ':' + _start.getMinutes() ; //_pad( _start[3],'00') + ":" +_pad( _start[4],'00');
           var end  = _end.getHours() + ':' + _end.getMinutes() ;//_pad( _end[3],'00') + ":" + _pad( _end[4],'00');
           console.log(start);
           console.log(end);
           
           /*
           if (end == start){
        	   end = _end.substring(15,17) + ':' + _end.substring(18,20);
           }*/
           console.log(end);
           
           //horarioReq.id = (params[0]=='')?null:parseInt(params[0]);
           if(params[0]!='')
        	   horarioReq.id = params[0]; 
           horarioReq.id_cca = params[1];
           horarioReq.hora_dia = start;
           horarioReq.hora_fin = end;
           horarioReq.dia = day; 
           horarios.push(horarioReq);
       }
       
        //datos del calendario
        var semana = {};
        semana.id_au = $('#id_au').val();
        semana.id_anio = $('#_id_anio').text();
        semana.horarios = horarios;
        
        
        _post_json('api/cursoHorario/grabar',semana, function(data){
        		//actualizar el calendario
        		$('.fullcalendar-external').fullCalendar( 'removeEvents');
        		$(".fullcalendar-external").fullCalendar('addEventSource', data.result); 

        });
        
	});

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_horario_modal.html" id="col_curso_horario-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Curso Horario</a></li>');

	$('#col_curso_horario-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_horario_modal($(this));
	});
	

	
});

function curso_horario_eliminar(link){
	_delete('api/cursoHorario/' + $(link).data("id"),
			function(){
					curso_horario_listar_tabla();
				}
			);
}

function curso_horario_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_modal.html');
	curso_horario_modal(link);
	
}


var fncMostrarCalendario = function(data){
	
	
	$("#div-horario").css('display','block');
	$("#btn-grabar").css('display','inline-block');
	$(".fullcalendar-external").css('display','block');
	
	dibujarCalendario(data.defaultDate, data.horarios);
	
	console.log(data);
	$("#div-cursos").find("label").html(data.cursos[0].curso);
	$("#div-cursos").find(">:first-child").prop("params","-"+data.cursos[0].id + '-' + data.cursos[0].docente);//id-id_cca

	for(i=1;i<data.cursos.length;i++){
		//console.log(data.cursos[i].curso);
		var divCurso = $("#div-cursos").find(">:first-child").clone();
		divCurso.find("label").html(data.cursos[i].curso);
		divCurso.prop("params","-"+data.cursos[i].id + '-' + data.cursos[i].docente);
		$( "#div-cursos" ).append(divCurso);
	}
	
	  // Initialize the external events
    $('#external-events .fc-event').each(function() {

        // Different colors for events
        $(this).css({'backgroundColor': $(this).data('color'), 'borderColor': $(this).data('color')});

        // Store data so the calendar knows to render an event upon drop
        $(this).data('event', {
            //title: $.trim($(this).html()), // use the element's text as the event title
        	title: $.trim($(this).text()) + '\n (' + $(this).prop('params').split('-')[2]  + ')', // use the element's text as the event title
            color: $(this).data('color'),
            stick: true, // maintain when user navigates (see docs on the renderEvent method)
            extendedProps :  $(this).prop('params')
        });

        // Make the event draggable using jQuery UI
        $(this).draggable({
            zIndex: 999,
            revert: true, // will cause the event to go back to its
            revertDuration: 0 // original position after the drag
        });
    });
    
	
};

/*buscar*/
$('#col_curso_horario').on('submit',function(event){
	event.preventDefault();
	
	//limpiar divs
	$('#div-cursos').html('<div class="fc-event" data-color="#546E7A"><label>Curso</label></div>');
	
	var id_au=$("#id_au").val();
	return _get('api/cursoHorario/listarCursos?id_au='+id_au+'&id_anio='+$('#_id_anio').text(),fncMostrarCalendario, $(this).serialize());
});

/* ------------------------------------------------------------------------------
*
*  # Fullcalendar advanced options
*
*  Demo JS code for extra_fullcalendar_advanced.html page
*
* ---------------------------------------------------------------------------- */


function dibujarCalendario(_defaultDate, horarios){
	
	// Event colors
	//var eventColors = horarios;
	console.log(horarios);
 	
	if( $('.fullcalendar-external').children().length>0 ){
		$('.fullcalendar-external').fullCalendar( 'removeEvents');
		$(".fullcalendar-external").fullCalendar('addEventSource', horarios); 

		return;
	}

     // Initialize the calendar
    $('.fullcalendar-external').fullCalendar({
        header: {
            left: '',
            center: '',
            right: ''
        },
        editable: true,
        defaultDate: _defaultDate,
        allDaySlot : false,
        slotDuration: '00:05:00', 
        events: horarios,
        locale: 'es',
        defaultView: 'agendaWeek',
        minTime : '07:00:00',
        maxTime : '19:00:00',
        //weekNumbers: false,
        //weekNumberTitle: 'W',
        views: {
            agenda: {
            	columnFormat: 'dddd',
            }
        },
        //displayEventTime : false,
        hiddenDays: [ 0,6 ], 
        //plugins: [ 'interaction', 'dayGrid', 'timeGrid' ],
        droppable: true, // this allows things to be dropped onto the calendar
        drop: function(info) {
            console.log(info);
            $(this).remove(); // if so, remove the element from the "Draggable Events" list
            
        },
        timeZone: 'America/Lima',
        defaultTimedEventDuration: '00:60:00',
		forceEventDuration: true,
		eventOverlap:false,
		/*
		eventRender: function( event, element, view ) {
	        if(event.changing){
	            console.log("Start: "+event.start.format("YYYY-MM-DD hh:mma")+"- End: "+event.end.format("YYYY-MM-DD hh:mma"));
	        }
		},
		eventResizeStart: function(event, jsEvent, ui, view ){
			event.changing = true;
		},
		eventResizeEnd: function(event, jsEvent, ui, view ){
			event.changing = false;
		},
		eventDragStart: function(event, jsEvent, ui, view ){
			event.changing = true;
		},
		eventDragEnd: function(event, jsEvent, ui, view ){
			event.changing = false;
		},*/
		
		eventClick: function(calEvent, jsEvent, view) {
			
						// change the border color just for fun
						$(this).css('border-color', 'red');
						
						var events =  $('.fullcalendar-external').fullCalendar('clientEvents');
			                 for(var i=0; i< events.length; i++) {
								 console.log(events[i]);
			                    var eventsid = events[i].id;
							//	alert(eventsid);
			                }

					 }
			//evendDropStop: function()
    });


  

}
