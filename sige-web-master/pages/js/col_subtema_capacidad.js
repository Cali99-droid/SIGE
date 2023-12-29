//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	// Basic initialization    
}

var __capacidades;
//se ejecuta siempre despues de cargar el html
$(function(){

	_onloadPage();
	
	$('#id_tem').on('change',function(event){
		var param={};
		param.id_tem = $('#id_tem').val();
		param.id_anio = $('#_id_anio').text();
		param.id_niv = $('#id_niv').val();
		param.id_gra = $('#id_gra').val();
		param.id_cur = $('#id_cur').val();
		_llenarComboURL('api/subtema/listarCursoSubtemas',$('#id_sub'),null,param,function(){$('.multiselect').multiselect();});
		
	    subtema_capacidad_listar_tabla();
	    $('.tema').html($("#id_tem option:selected").text());
	});
	
	
	$('#id_cur').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_niv:$('#id_niv').val(), id_cur: $('#id_cur').val(), id_gra:$('#id_gra').val()};
		_llenarComboURL('api/subtemaCapacidad/listarTema',$('#id_tem'), null,param,function(){
			$('#id_tem').change();
		});
		
		//llenar competenciasxxx
		//_llenarComboApi('api/competencia/listar?id_niv=' +  $('#_id_niv').text() + '&id_cur=' + $('#id_cur').val(),'nom',$('#id_com'), null,function(){
		_llenarComboApi('api/competencia/listaCompetenciasCursoAnioCatalogo?id_anio=' + $('#_id_anio').text() +  '&id_niv=' + $('#id_niv').val() + '&id_gra=' + $('#id_gra').val() + '&id_cur=' + $('#id_cur').val(),'competencia',$('#id_com'), null,function(){
		
			$('#id_com').change();
		});

	});
	
	$('#id_niv').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $(this).val()};
		_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra'), null,param, function(){$('#id_gra').change();});
	});

	$('#id_gra').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val()};
		param.id_gra = $('#id_gra').val();
		_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), null,param,function(){$('#id_cur').change();});
	});

	
	$('#id_com').on('change',function(event){
		_llenarComboApi('api/capacidad/listar/' +  $(this).val() ,'nom',$('#id_cap'), null);
	});
	
	//llenar niveles
	_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),null, null, function(){
		if ($('#id_niv').val()=='' ){
			$("#id_niv").prop("selectedIndex", 1);
		}
			
		$('#id_niv').change();
		

	});

	$('#frm-seccion-buscar').on('submit',function(event){
		event.preventDefault();

		subtema_capacidad_listar_tabla();
	});
	
	
	$('#frm-seccion-agregar').on('submit',function(event){
		event.preventDefault();

		$('#id_anio').val($('#_id_anio').text());
		
		_post('api/subtemaCapacidad/grabarSubtemasCapacidad',$(this).serialize(), function(data){
			subtema_capacidad_listar_tabla();
		});
		
	});
	


	$(document).click(function (event) {
        //hide all our dropdowns
        $('.dropdown-menu[data-parent]').hide();

    });
    $(document).on('click', '.table-responsive [data-toggle="dropdown"]', function () {
    	
    	$('.dropdown-menu').css('display','none');
    	
        // if the button is inside a modal
        if ($('body').hasClass('modal-open')) {
            throw new Error("This solution is not working inside a responsive table inside a modal, you need to find out a way to calculate the modal Z-index and add it to the element")
            return true;
        }

        $buttonGroup = $(this).parent();
        if (!$buttonGroup.attr('data-attachedUl')) {
            var ts = +new Date;
            $ul = $(this).siblings('ul');
            $ul.attr('data-parent', ts);
            $buttonGroup.attr('data-attachedUl', ts);
            $(window).resize(function () {
                $ul.css('display', 'none').data('top');
            });
        } else {
            $ul = $('[data-parent=' + $buttonGroup.attr('data-attachedUl') + ']');
        }
        if (!$buttonGroup.hasClass('open')) {
            $ul.css('display', 'none');
            return;
        }
        dropDownFixPosition($(this).parent(), $ul);
        function dropDownFixPosition(button, dropdown) {
            var dropDownTop = button.offset().top + button.outerHeight();
            dropdown.css('top', dropDownTop + "px");
            dropdown.css('left', button.offset().left + "px");
            dropdown.css('position', "absolute");
   
            dropdown.css('width', dropdown.width());
            dropdown.css('heigt', dropdown.height());
            dropdown.css('display', 'block');
            dropdown.appendTo('body');
        }
    });

	
    
});


 
/**
 * Lista la grilla por grupo de subtemas/competencia-capacidad/desemepeños 
 * 
 * @returns
 */
function subtema_capacidad_listar_tabla(){

	var tabla = $('#col_subtema_capacidad-tabla');
	//limpiamos la tabla
	tabla.find('thead').html('');
	tabla.find('tbody').html('');
	
	//listamos la lista de subtema-capacidades-desemepeños
	var param ={};
	param.id_anio = $('#_id_anio').text();
	param.id_tem = $('#id_tem').val();
	param.id_niv= $('#id_niv').val();
	param.id_gra=$('#id_gra').val()	;
	param.id_cur=$('#id_cur').val()	;
	_get('api/subtemaCapacidad/listSubtemaCapacidades/',
		function(data){
		
		//obtener maximo nro de columnas
		var columnas = 0;
		var filas = 0;
		var arrSubtemas =[];
		
		for (var gr in data){
			var capacidades = data[gr].capacidades.length;
			
			if (capacidades>columnas)
				columnas = capacidades;
			var cant_subtemas =  data[gr].subtemas.length;
			var subtemas = data[gr].subtemas;
			filas = filas + cant_subtemas;
			
			for (var subtema in subtemas){
				console.log(subtemas[subtema]);
				arrSubtemas.push(subtemas[subtema]);
				//var id_css = subtemas[subtemas].id_css;	

			}
		}	
		 
		//agregar filas a la tabla
		 
	 
		var primerSubtema = true;
		var pintoCapacidadesTH = true;
		for (var gr in data){

			var grupotema = data[gr];
			var subtemas = grupotema.subtemas;
			var nro_subtemas =subtemas.length;
			var primera_fila  = true;
			
			
			if (primerSubtema){
				primerSubtema = false;
				var htmlTH = "<tr class='bg-grey-300' ><th rowspan='2'>Sub-tema</th>";
				for(var c=0; c<columnas; c++){
					if (grupotema.capacidades.length>c)
						htmlTH = htmlTH + "<th class='bg-primary-600' title='Competencia' >" + grupotema.capacidades[c].competencia  + "</th>";
					else
						htmlTH = htmlTH + "<th></th>";
				}
				htmlTH = htmlTH + "</tr>";
				
				htmlTH = htmlTH + "<tr class='bg-primary-300'>";
				for(var c=0; c<columnas; c++){
					if (grupotema.capacidades.length>c)
						htmlTH = htmlTH + "<th title='Capacidad'>" + grupotema.capacidades[c].capacidad  + "</th>";
					else
						htmlTH = htmlTH + "<th></th>";
				}
				htmlTH = htmlTH + "</tr>";
				
				tabla.find('thead').append(htmlTH);
			}
			
			 
			if(!pintoCapacidadesTH){
				var tdCompetencia ='';
				var tdCapacidad ='';
				for(var c=0; c<columnas; c++){
					if (grupotema.capacidades.length>c){
						tdCompetencia = tdCompetencia + "<td title='Competencia'>" + grupotema.capacidades[c].competencia  + "</td>";
						tdCapacidad = tdCapacidad + "<td title='Capacidad'>" + grupotema.capacidades[c].capacidad  + "</td>";
					}else
						htmlTH = htmlTH + "<td></td>";
				}
				tabla.find('tbody').append('<tr class="bg-primary-600"><td rowspan="2" class="bg-grey-300">Sub-tema</td>' + tdCompetencia + '</tr>');
				tabla.find('tbody').append('<tr class="bg-primary-300">' + tdCapacidad + '</tr>');
				
			}else
				pintoCapacidadesTH = false;
		 
			for (var st in subtemas){
				
				if (primera_fila){
				
					primera_fila  = false;
					
					var celdaDesempenios = '';
					for(var c=1; c<columnas; c++){
						var htmlDesempenios = '';
						//lista de desempeños
						if (grupotema.capacidades.length>c){
							var desempenios = grupotema.capacidades[c].desempenios;
							htmlDesempenios = '<ul>';
							for (var d in desempenios){
								htmlDesempenios = htmlDesempenios + '<li>' + desempenios[d].desempenio  + '</li>';
							}
							htmlDesempenios = htmlDesempenios + '</ul>';
							
							if (desempenios.length == 0)
								htmlDesempenios = '<a href="#" onclick="agregarDesempenio(' + grupotema.capacidades[c].id  + ')">Agregar desempeños</a>';
							
						}
						if (grupotema.capacidades.length>c)
							celdaDesempenios = celdaDesempenios + '<td rowspan="' + nro_subtemas +'"><a onclick="agregarDesempenio(' + grupotema.capacidades[c].id  + ')">' + htmlDesempenios + '</a></td>';
						else
							celdaDesempenios = celdaDesempenios + '<td rowspan="' + nro_subtemas +'">&nbsp;</td>';
					}
					
					var desempenios1 = grupotema.capacidades[0].desempenios;
					var htmlDesempenios1 = '<ul>';
					for (var d in desempenios1){
						htmlDesempenios1 = htmlDesempenios1 + '<li>' + desempenios1[d].desempenio  + '</li>';
					}
					htmlDesempenios1 = htmlDesempenios1 + '</ul><br><a href="#" onclick="eliminarAgrupacion(' + grupotema.capacidades[0].id  + ')">Eliminar Agrupaci&oacute;n</a>';
					
					if (desempenios1.length == 0)
						htmlDesempenios1 = '<a href="#" onclick="agregarDesempenio(' + grupotema.capacidades[0].id + ')">Agregar desempeños</a><br><a href="#" onclick="eliminarAgrupacion(' + grupotema.capacidades[0].id + ')">Eliminar Agrupaci&oacute;n</a>';
					
					tabla.find('tbody').append('<tr><td>' + subtemas[st].subtema  + '</td><td rowspan="' + nro_subtemas + '"><a onclick="agregarDesempenio(' + grupotema.capacidades[0].id  + ')">' + htmlDesempenios1 + '</a></td>' +celdaDesempenios + '</tr>');

				}else{
	 
					tabla.find('tbody').append('<tr><td>' + subtemas[st].subtema  + '</td></tr>');
				}
					
			}
		    
		}
		
	},param);

}	 

function agregarDesempenio(id_cgc){

	var onShowModal = function (){
		//lista de desempeños
		$('#id_cgc').val(id_cgc);
	
		lista_desempenios(id_cgc);

		$('#btn-agregar-desempenio').show();
		$('#btn-grabar-desempenio').hide();
		$('#col_subtema_desempenio-frm #id').val('');

		$('#btn-agregar-desempenio').on('click',function(){
			var validator = $("#col_subtema_desempenio-frm").validate();

			if ($("#col_subtema_desempenio-frm").valid()){ 
				_post('api/subtemaCapacidad/agregarDesempenio',$("#col_subtema_desempenio-frm").serialize(), function(data){
					console.log(data);
					lista_desempenios(id_cgc);
					//ACTUALIZAR GRILLA PRINCIPAL
					subtema_capacidad_listar_tabla();
				});
			}
		});
		
		$('#btn-grabar-desempenio').on('click',function(){
			var validator = $("#col_subtema_desempenio-frm").validate();

			if ($("#col_subtema_desempenio-frm").valid()){ 
				_post('api/subtemaCapacidad/agregarDesempenio',$("#col_subtema_desempenio-frm").serialize(), function(data){
					console.log(data);
					lista_desempenios(id_cgc);
					//ACTUALIZAR GRILLA PRINCIPAL
					subtema_capacidad_listar_tabla();
				});
			}
		});
		
	};
	var onSuccessSave = function(data){
	};
	
	_modal('Agregar desempeños', 'pages/academico/col_subtema_capacidad_modal.html',onShowModal,onSuccessSave);

	
}    

function lista_desempenios(id_cgc){
	_get('api/subtemaCapacidad/listDesempenios',
			function(data){
				$('#col_subtema_desempenio-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Indicador", "data" : "nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<div class="list-icons">' + 
		                		'<a href="#" data-id="' + row.id + '" data-nom="' + row.nom + '" onclick="editar_desempenio(this)" class="list-icons-item text-primary-600"><i class="icon-pencil7"></i></a> &nbsp;' +
		                		'<a href="#" data-id="' + row.id + '" onclick="eliminar_desempenio(this)" class="list-icons-item text-danger-600"><i class="icon-trash"></i></a>'
		                	'</div>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		,{id_cgc:id_cgc}
	);
	
}

function editar_desempenio(row){
	var obj = $(row);
	$('#nom').val(obj.data('nom'));
	$('#col_subtema_desempenio-frm #id').val(obj.data('id'));

	$('#btn-agregar-desempenio').hide();
	$('#btn-grabar-desempenio').show();
	
}

function eliminar_desempenio(row){
	var obj = $(row);
	var id = obj.data('id');
	console.log('eliminando:' + id);
	_delete('api/subtemaCapacidad/eliminarDesempenio/' + id,
			function(){
					console.log('listnado');
					lista_desempenios($('#id_cgc').val());
					//ACTUALIZAR GRILLA PRINCIPAL
					subtema_capacidad_listar_tabla();
				}
			);
	
}

function eliminarAgrupacion(id_cgc){
	_delete('api/subtemaCapacidad/eliminarAgrupacion/' +id_cgc,
			function(){
			subtema_capacidad_listar_tabla();
				}
			);	
}