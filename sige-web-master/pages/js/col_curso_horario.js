//Se ejecuta cuando la pagina q lo llama le envia parametros
var i =0;
var _id_cca;
function onloadPage(params){
	var id_suc=$("#_id_suc").text();

	$('#id_au').on('change',function(event){
		var param={id_au:$("#id_au").val()};
		_GET({
			context:_URL,
			url:'api/cursoHorario/listarHorariosPadre/',
			params: param,
			success:function(data){
				var list = data;
				console.log(data);
				$('#tb_horario_pad').dataTable({
					 data : list,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
					 searching: false, 
					 paging: false, 
					 info: false,
			         columns : [
			                    
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Horario", "render":function ( data, type, row,meta ) {
			        	 		//creamos un hidden x cada checkbox
								return "<button type='button' onclick='seleccionarHorario(" + row.id + ")' class='btn btn-primary btn-xs' id='btn-seleccionar'>Seleccionar</button>";
			        	 	} 
			        	 	},
							{"title":"Fec. Inicio", "render":function ( data, type, row,meta ) {
								return _parseDate(row.fec_ini_vig);
								}
							},
							{"title":"Fec. Fin", "render":function ( data, type, row,meta ) {
								if (row.fec_fin_vig == null)
									return 'Fin de año';
								else
									return _parseDate(row.fec_fin_vig);
								} 
							},
							{"title":"Acción", "render":function ( data, type, row,meta ) {
								if (meta.row==list.length-1)
									return "<button type='button' onclick='nuevoHorario()' class='btn btn-primary btn-xs' id='btn-nuevo'>Nuevo horario <i class='icon-file-empty'></i></button>";
								else
									return "";
								}
							}
				    ]
			    });
			}
		});
	});
	
	$('#id_gra').on('change',function(event){
		var id_suc=$("#id_suc").val();
		_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc, function(){$('#id_au').change()});
	});
	
	$('#id_niv').on('change',function(event){
		//_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$('#id_gra').change()});
		
		var param={id_niv:$(this).val()};
		_llenarComboURL('api/grado/listarTodosGrados',$('#id_gra'),null, param,function(){$('#id_gra').change()});

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
//           console.log(start);
//           console.log(end);
           
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
        semana.id_cchp = $('#id_cchp').val();
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
	if(data.cursos.length>0){//PINTAMOS EL PRIMER CURSO
		
		$('#column-cursos').show();
		$('#column-calendar').removeClass('col-md-12');
		$('#column-calendar').addClass('col-md-9');

		
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
	    
	}else{
		$('#column-cursos').hide();
		$('#column-calendar').removeClass('col-md-9');
		$('#column-calendar').addClass('col-md-12');
	}
	
};

/*buscar*/
/* ESTO FUNCIONABA CUANDO SOLO HABIA UN HORARIO POR AÑO
$('#col_curso_horario').on('submit',function(event){
	event.preventDefault();
	
	//limpiar divs
	$('#div-cursos').html('<div class="fc-event" data-color="#546E7A"><label>Curso</label></div>');
	
	var id_au=$("#id_au").val();
	return _get('api/cursoHorario/listarCursos?id_au='+id_au,fncMostrarCalendario, $(this).serialize());
});
*/

/**
 * Se activa al seleccionar el horario padre
 * @returns
 */
function seleccionarHorario(id_cchp){
	$('#div-cursos').html('<div class="fc-event" data-color="#546E7A"><label>Curso</label></div>');
	var id_au=$("#id_au").val();
	$("#id_cchp").val(id_cchp);
	return _GET({
		url: 'api/cursoHorario/horariolistarCursos?id_au='+id_au  + '&id_cchp=' + id_cchp+'&id_anio='+$("#_id_anio").text(),
		block: true,
		success: fncMostrarCalendario
	});
	
}

function nuevoHorario(){
	
	var onShowModal = function(){
		_inputs('modal-body');
		$('#col_curso_horario_pad-frm #id_au').val($('#col_curso_horario #id_au').val());
		$('#col_curso_horario_pad-frm #id_anio').val($('#_id_anio').text());
	}
	
	var onSuccessSave = function(){
		$('#id_au').change();//listar los padres
	}
	
	_modal('Nuevo Horario','pages/academico/col_curso_horario_modal.html',onShowModal,onSuccessSave);

}

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


/*
 * _post('api/cursoHorario/grabar' , param,
                   			function(data){
                   					//onSuccessSave(data) ;
                   					
                   				}
                   			);
 * 
 */