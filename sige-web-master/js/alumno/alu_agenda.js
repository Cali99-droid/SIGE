//Se ejecuta cuando la pagina q lo llama le envia parametros
var i =0;
var _id_mat;
function onloadPage(params){
	_id_mat= params.id_mat;
	consultarCalendario(null);//marzo
	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	
});

 

/*BUSQUEDA POR SEMANA */
function consultarCalendario(mes){
	
	var param	 = {};
	param.id_mat = _id_mat;
	param.mes = mes;
	param.id_anio = $('#_id_anio').text();

	_block_UI();
	return _get('api/cursoHorario/listaAgenda',
		function(data){
			dibujarCalendario( data);
			$.unblockUI();
	}, param);
	
}

function dibujarCalendario( data){
 	var horarios = data.horarios;
 	var _mes = parseInt(data.fecha.split("-")[1]);
 	//alert(_mes);
	if( $('.fullcalendar-agenda').children().length>0 ){
		$('.fullcalendar-agenda').fullCalendar( 'removeEvents');
		$('.fullcalendar-agenda').fullCalendar('gotoDate', data.fecha);
		$(".fullcalendar-agenda").fullCalendar('addEventSource', horarios); 
		return;
	}
	
	console.log(horarios);
	
     // Initialize the calendar
    $('.fullcalendar-agenda').fullCalendar({
        header: {
        	  left: 'prev,next today',
              center: 'title',
              right: 'month,agendaWeek,agendaDay'
        },
        editable: false,
        defaultDate: data.fecha,
        allDaySlot : false,
        slotDuration: '00:60:00', 
        events: horarios,
        locale: 'es',
       // defaultView: 'dayGridMonth',
        minTime : '07:00:00',
        maxTime : '19:00:00',
        views: {
            agenda: {
            	columnFormat: 'ddd DD/MM',
            }
        },
       // hiddenDays: [ 0,6 ], 
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
				abrirModalSesionesDia(calEvent);
		},
		viewRender: function(view, element) {
			  var b = $('.fullcalendar-agenda').fullCalendar('getDate');

			  var mes = b.format('M');
			  if(_mes != mes)
				  consultarCalendario(mes);
			},
    });


  

}
 
 
/**
 * 
 * @param params (cch.id  + '-' + cch.id_cca + '-' + ccu.id)
 * @param fecha
 * @returns
 */
function abrirModalSesionesDia(calEvent){

	var extendedProps =calEvent.extendedProps;
	var id_cchs = extendedProps; 
	var fecha = calEvent.start._d;
	
	_GET({url:'api/cursoHorarioSes/listarDetalle',
			params:{id_cchs:id_cchs},
			success: function(data){
				var tituloModal = calEvent.title + ' - ' + fecha.getDate() + '/' + _MES[fecha.getMonth()] ;
				console.log(data);
				//alert(2);
				_modal(tituloModal,'pages/academico/col_curso_horario_temas_modal.html',function (){
					//dibujar los temas y subtemas
					var $tabla = $('#col_temas-tabla');
					for(var i in data){
						
						var tema = data[i];
						var nro_subtemas = tema.subtemas.length;
						var tr = '<tr><td rowspan="' + nro_subtemas + '">' + tema.tema + '</td>';
						
						//1er subtema subtema
						if (nro_subtemas>0){
							tr = tr + '<td>' + tema.subtemas[0].subtema  + '</td>';
							console.log(tema.subtemas[0].subtema);
							var nro_indicadores = tema.subtemas[0].indicadores.length;
							if(nro_indicadores>0)
								tr = tr + '<td>' + tema.subtemas[0].indicadores[0].indicador + '</td>';//INDICADORES
							else
								tr = tr + '<td>&nbsp;</td>';//NO TIENE  INDICADORES

						}else{
							tr = tr + '<td>&nbsp;</td>';//NO TIENE TEMA
							tr = tr + '<td>&nbsp;</td>';//NO TIENE  INDICADORES
						}
						
						tr = tr + '</tr>';
						
						//2do subtema para adelante
						if (nro_subtemas>1){
							
							for(var j in tema.subtemas){
								
								if(j>0){
									var subtema = tema.subtemas[j];
									var nro_indicadores = subtema.indicadores.length;
									
									var trS = '<tr rowspan="' + nro_indicadores + '" >';
									
									trS = trS + '<td>' + subtema.subtema + '</td>';
									
									if(nro_indicadores>0)
										trS = trS + '<td>' +  subtema.indicadores[0].indicador + '</td>';//INDICADORES
									else
										trS = trS + '<td>&nbsp;</td>';//NO TIENE  INDICADORES
									
									trS = trS + '</tr>';
									tr = tr + trS;
									
									//indicadores a partir de uno
									if(nro_indicadores>1){
										var trI = '<tr>';
										for(var x in subtema.indicadores){
											var indicador = subtema.indicadores[x];
											if(x>0){
												trI = trI + '<td>' + indicador.indicador + '</td>';	
											}
										}
										trI = trI + '</tr>';
										tr = tr + trI;

									}
									
									
								}
								
							}
							
							
							/*
							if (subtema.indicadores.length>1){
								var trI = '<tr>';
								for(var x in subtema.indicadores){
									var indicador = subtema.indicadores[x];
									if(x>1){
										trI = trI + '<td>' + indicador.indicador + '</td>';	
									}
								}
								trI = trI + '</tr>';
							}
							
							tr = tr + trI;
							*/
						}
							
						
						$tabla.append(tr);
						
					}
					
				});
			}
		}
	);
	
}
