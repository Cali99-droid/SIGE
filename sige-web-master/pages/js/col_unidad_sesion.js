//Se ejecuta cuando la pagina q lo llama le envia parametros
var _data;
function onloadPage(params) {

}
// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();

	//$('#_botonera').html('<li><a href="pages/academico/col_unidad_sesion_modal.html" id="col_unidad_sesion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Unidad Sesion</a></li>');

	$('#col_unidad_sesion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		unidad_sesion_modal($(this));
	});

	// lista tabla
	//unidad_sesion_listar_tabla_json(null);

	// eventos
	
	$('#id_uni').on('change',function(event){ //alert(); Integer id_niv, Integer id_cur, Integer id_cpu, Integer id_gra
		var param = {id_uni: $('#id_uni').val(), id_niv:$('#id_niv_bus').val(), id_cur:$('#id_cur_bus').val(), id_cpu:$('#id_cpu').val(), id_gra:$('#id_gra_bus').val()};
		_GET({  url:'api/cursoUnidad/validarSesionesCompletas',
			    params:param,
				success:function(data){
					console.log(data);
					unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
					$("#col_unidad_sesion-tabla").show();
				},
				error:function(data){
					_alert_error(data.msg);
					$("#col_unidad_sesion-tabla").hide();
				}
		     });
	});
	

	 	
	 $('#id_cpu').on(
				'change',
				function(event) {
					var param = {
						id_niv : $('#id_niv_bus').val(),
						id_gra : $('#id_gra_bus').val(),
						id_cur : $('#id_cur_bus').val(),
						nump : $('#id_cpu').val()
					};
					_llenarComboURL('api/cursoUnidad/listarUnidades', $('#id_uni'),
							null, param, function() {
								unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
							});

	});
	 
	 $('#id_cur_bus').on(
				'change',
				function(event) {
					var param1 = {
							id_niv : $('#id_niv_bus').val(),
							id_anio : $('#_id_anio').text()
						};
						_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null, param1, function() {
							$('#id_cpu').change();
						});
	});
	 
	$('#id_gra_bus').on(
			'change',
			function(event) {
				var param = {
					id_anio : $('#_id_anio').text(),
					id_tra : _usuario.id_tra,
					id_niv : $('#id_niv_bus').val(),
					id_gra : $('#id_gra_bus').val()
				};
				_llenarComboURL('api/areaCoordinador/listarCursos',
						$('#id_cur_bus'), null, param, function() {
							$('#id_cur_bus').change();
						});
});
	
	$('#id_niv_bus').on(
			'change',
			function(event) {
				var param = {
					id_anio : $('#_id_anio').text(),
					id_tra : _usuario.id_tra,
					id_niv : $(this).val()
				};
				console.log(param);
				_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra_bus'), null, param, function() {
					$('#id_gra_bus').change();
				});
				

			});


	_llenarComboURL('api/areaCoordinador/listarNiveles/' + _usuario.id_tra,
			$('#id_niv_bus'), null, null, function() {
				$('#id_niv_bus').change();
			});	
});

function unidad_sesion_eliminar(link) {
	_delete('api/unidadSesion/' + $(link).data("id"), function() {
		unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
	});
}

/**
 * agregar tipo de sesion ( clase, examen,etc a la sesion)
 * @param id
 * @returns
 */
function agregarDatos(id) {
	var onShowModal = function() {
		
		$("#not_evaluacion").css('display','none');
		
		$('#btn-evaluacion').on(
				'click',
				function(event) {
				$(".modal .close").click();
		});
		
		$('#col_unidad_sesion-frm #btn-agregar').on('click',
				function(event) {
					$('#id_cts').removeAttr('disabled');
					
					_post($('#col_unidad_sesion-frm').attr('action'), $('#col_unidad_sesion-frm').serialize(), function(data) {
						onSuccessSave(data);
						if($('#id_cts').val()=='2'){
							$("#not_evaluacion").css('display','block');
						}
						$('#id_cts').attr('disabled', 'disabled');
					});
		});
		
		
		$("#col_unidad_sesion-frm #id_uns").val(id);
		// lista de desempeños
		$('#id_cap').on('change', function(event) {
			listar_desempenio();
		});
		$('#id_sub').on(
				'change',
				function(event) {
					var param = {
						id_ccs : $('#id_sub').val()
					};
					_llenarComboURL('api/capacidad/listarCapacidadxSubtema',
							$('#id_cap'), null, param, function() {
								$('#id_cap').change();
							});
				});

		var param = {
			id_anio : $('#_id_anio').text(),
			id_niv : $('#id_niv_bus').val(),
			id_gra : $('#id_gra_bus').val(),
			id_cur : $('#id_cur_bus').val(),
			id_uni : $('#id_uni').val()
		};
		_llenarComboURL('api/uniSub/listarSubtemasxUnidad', $('#id_sub'), null,
				param, function() {
					$('#id_sub').change();
				});
		$('#id_cts').on(
				'change',
				function(event) {
					if($('#id_cts').val()=='2'){//EXAMEN
						$("#div_desempenio").css('display','none');
					}
		});
		
/*		_get('api/unidadSesion/buscarSesionesClase/' + id,
				function(data){
					console.log(data);
				});

		_llenarCombo('cat_tipo_sesion', $('#id_cts'));
*/
		_llenarComboURL('api/unidadSesion/tiposSesionDisponible/' + id, $('#id_cts'),null, null, function(){
			$('#id_cts').change();
		});
	};

	var onSuccessSave = function(data) {
		$(".modal .close").click();
		unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
		
	};
	
	//VALIDAR QUE TENGA INDICADOR EN LA SESION ANTERIOR
	// obtener indice de la seson seleccionada
	var index = 0;
	var cont = 0;
	for(var i in _data){
		var session = _data[i];
		var id_ses = session.id;
		if (id == id_ses)
			index = cont;
		cont++;
	}
	
	if(index==0 || indicadoresLleno(index-1))
		_modal('Agregar Desempe&ntilde;os', 'pages/academico/col_unidad_sesion_modal.html', onShowModal, onSuccessSave);
	else
		swal({
			html: true,
			title : "Error",
			text :'Debe ingresar indicadores a la sesion anterior',
			confirmButtonColor : "#2196F3",
			type : "error"
		});
}


function unidad_sesion_editar(row, e) {
	e.preventDefault();

	var link = $(row);
	link.attr('href', 'pages/academico/col_unidad_sesion_modal.html');
	unidad_sesion_modal(link);

}

function unidad_sesion_listar_tabla(id,id_gra) {

	_get('api/unidadSesion/listarSesionesxUnidad/' + id+'/'+id_gra, function(data) {
		console.log(data);
		unidad_sesion_listar_tabla_json(data.sesiones);
		//habilitar solo sesiones vinculadas
		deshabilitar_sesiones_vinculadas(data.sesionesVinculadas);
	});
}

function deshabilitar_sesiones_vinculadas(sesionesVinculadas){
	
	console.log("----------------");
	$("[data-clase]").each( function( index, element ){
		//console.log( $( this ).data('sesion') );
		//console.log( $( this ).html() );
		var id_clase= $(this).data('clase');
		
		var tr =  $( this );
		
		//revisar si esta en sesion vinculada
		var sesionVinculada = false;
		for(var i in sesionesVinculadas){
			if (id_clase == sesionesVinculadas[i].id)
				sesionVinculada = true;
		}
		if (sesionVinculada){
			tr.find('a').each(function(){
				
				if(!$(this).hasClass('notatool'))
					$(this).html("");
			});
			
		}
	});

}

function listar_desempenio() {
	var param = {
		id_cap : $('#id_cap').val(), id_cgsp:$('#id_cap option:selected').attr('data-aux1'), id_ses: $('#id_ses').val()
	};
	_get('api/desempenio/listarDesempenios/', function(data) {
		//console.log(data);
		$('#col_desempenio-tabla').dataTable({
			data : data,
			aaSorting : [],
			destroy : true,
			 orderCellsTop:false,
			 bLengthChange: false,
			 bPaginate: false,
			 bFilter: false,
		     bInfo: false,
			select : true,
			columns : [ {
				"title" : "Nro",
				"render" : function(data, type, row, meta) {
					return parseInt(meta.row) + 1;
				}
			}, {"title":"Desempe&ntilde;o", "render":function ( data, type, row,meta ) {
       	 		
				return "<input type='hidden' id='id_cde" + row.id +"' name='id_cde' value="+row.id+">"+row.desempenio+"</input>";
    	 	}}],
			"initComplete" : function(settings) {
			//	$(".datatable-header").ccs("padding","0");
				
			}
		});
	}, param);

}

function editarIndicador(id_cde,id,nom){
	var onShowModal = function() {
		$("#not_evaluacion").css('display','none');
		$("#col_sesion_indicador-frm #id").val(id);
		$("#id_csd").val(id_cde);
		if (id!='')
			$("#col_sesion_indicador-frm #nom").val(nom);
		
		$('#col_sesion_indicador-frm #btn-agregar').on(
				'click',
				function(event) {
 
					_post($('#col_sesion_indicador-frm').attr('action'), $('#col_sesion_indicador-frm').serialize(), function(data) {
						onSuccessSave(data);
					});
		});
		
	};

	var onSuccessSave = function(data) {
		unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
	};
	_modal('Agregar Indicadores',
		'pages/academico/col_sesion_indicador_modal.html', onShowModal,
		onSuccessSave);
		
}



function agregarDesempenio(id_ses){

		var onShowModal = function() {
			$('#col_unidad_sesion-frm #btn-agregar').on(
					'click',
					function(event) {
						$('#id_cts').removeAttr('disabled');
						_post('api/unidadSesion/agregarDesempenio/'+id_ses, $('#col_unidad_sesion-frm').serialize(), function(data) {
							onSuccessSave(data);
							if($('#id_cts').val()=='2'){
								$("#not_evaluacion").css('display','block');
							}
							$('#id_cts').attr('disabled', 'disabled');
						});
			});
			
			if(id_ses!=null){
				$('#id_cts').attr('disabled', 'disabled');
			}
			
			if($('#id_cts').val()=='2'){
				$("#not_evaluacion").css('display','block');
			}
			
			$("#col_unidad_sesion-frm #id_ses").val(id_ses);
			// lista de desempeños
			$('#id_cap').on('change', function(event) {
				listar_desempenio();
			});
			$('#id_sub').on(
					'change',
					function(event) {
						var param = {
							id_ccs : $('#id_sub').val()
						};
						_llenarComboURL('api/capacidad/listarCapacidadxSubtema',
								$('#id_cap'), null, param, function() {
									$('#id_cap').change();
								});
					});
		 
			var param = {
				id_anio : $('#_id_anio').text(),
				id_niv : $('#id_niv_bus').val(),
				id_gra : $('#id_gra_bus').val(),
				id_cur : $('#id_cur_bus').val(),
				id_uni : $('#id_uni').val()
			};
			_llenarComboURL('api/uniSub/listarSubtemasxUnidad', $('#id_sub'), null,
					param, function() {
						$('#id_sub').change();
					});
			$('#id_cts').on(
					'change',
					function(event) {
						if($('#id_cts').val()=='2'){
							$("#div_desempenio").css('display','none');
						}
			});
			_llenarCombo('cat_tipo_sesion', $('#id_cts'));

		};

		var onSuccessSave = function(data) {
			unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
			$(".modal .close").click();
		};
	_modal('Agregar Desempe&ntilde;os',
			'pages/academico/col_unidad_sesion_modal.html', onShowModal,
			onSuccessSave);
		
}

function agregarEvaluacion(id_ses){
 
	var onShowModal = function() {
 
		$('#not_evaluacion-frm #btn-grabar').on(
				'click',
				function(event) {
					if($("#not_evaluacion-frm").valid()) 
					_post('api/evaluacion/grabarEvaluaciones', $('#not_evaluacion-frm').serialize(), function(data) {
						onSuccessSave(data);
						
					});
		});
		$('#id_tra').val(_usuario.id_tra);
		$('#id_anio').val($('#_id_anio').text());
		$('#not_evaluacion-frm #id_cur').val($('#id_cur_bus').val());
		$('#not_evaluacion-frm #id_gra').val($('#id_gra_bus').val());
		$('#not_evaluacion-frm #id_niv').val($('#id_niv_bus').val());
		$('#not_evaluacion-frm #_id_cpu').val($('#id_cpu').val());
		$('#not_evaluacion-frm #id_ses').val(id_ses);		
		_llenarCombo('not_tip_eva',$('#id_nte'),null);
		var nump=$('#id_cpu option:selected').attr("data-aux1");
		var param={id_cur:$("#id_cur_bus").val(),id_anio:$('#_id_anio').text(),num:nump,id_gra:$("#id_gra_bus").val(), id_nep:$("#id_nep").val(), id_cpu:$('#id_cpu').val(), id_uni:$("#id_uni").val()};
		_get('api/cursoUnidad/listarDesem/',
				function(data){
				//console.log(data);
					$('#list_indicadores').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
				         columns : [
				                    
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
				        	 		var checked = (row.nie_id!=0 && row.est=="A" && row.cpu_id==$('#id_cpu').val()) ? ' checked ': ' ';
				        	 		var disabled = (checked) ? 'disabled':'';
				        	 		var id_ind = (row.nie_id!=null) ? row.nie_id: '';
									return "<input type='checkbox'"+checked+" id='id_cis"+row.id+"' name ='id_ind' value='" + row.id + "' data-ideva=''  />";
				        	 	} 
				        	 	},
				        	 	{"title":"Indicador", "data" : "indicador"}
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
								//$('.daterange-single').val('');
								//$('.daterange-single').prop('disabled',true);
						 }
				    });
				}, param
		);	

	};

	var onSuccessSave = function(data) {
		unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
		$(".modal .close").click();
	};
_modal('Agregar Evaluaciones',
		'pages/notas/not_evaluacion_sesion_modal.html', onShowModal,
		onSuccessSave);
	
}

//eliminarEvaluacion
function eliminarEvaluacion(id_ses){
	_delete('api/unidadSesion/' +id_ses,
			function(){
			unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
				}
			);
}

function eliminarDesempenio(id_ses) {
	_delete('api/unidadSesion/eliminarClaseoRepaso/' +id_ses,
			function(){
			unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
				}
			);
}

function eliminarIndicador(id){
	_delete('api/indicador/' +id,
			function(){
			unidad_sesion_listar_tabla($('#id_uni').val(),$('#id_gra_bus').val());
				}
			);
}

function unidad_sesion_listar_tabla_json(data) {

	_data = data;
	var nro_sesiones = data.length;
	var nro_filas_total = 0;
	for ( var i in data) {
		var sesion = data[i];
		var clases = sesion.clases;
		var session_filas = 0;
		if (clases == null || clases.length == 0) {
			session_filas += 1;
			sesion.clases = [];
		} else {
			for ( var c in clases) {
				var clase = clases[c];
				var grupos = clase.grupos;
				var clase_filas = 0;
				if (grupos != null && grupos.length > 0) {
					for ( var s in grupos) {
						var grupo = grupos[s];
						if (grupo.capacidades != null){
							var nrodesempenios = 0;
							for( var c in grupo.capacidades)
								nrodesempenios += grupo.capacidades[c].desempenios.length;
							grupo.filas = nrodesempenios;
						}
						else
							grupo.filas = 1;

						clase_filas += grupo.filas;
					}
				} else
					clase_filas = 1;

				clase.filas = clase_filas;
				session_filas += clase.filas;
			}
		}
		sesion.filas = session_filas;

		nro_filas_total += sesion.filas;
	}

	//console.log(data);

	var tabla = $('#col_unidad_sesion-tabla');
	tabla.find('tbody').html('');

	var primera_session = true;

	var nro_filas = 0;
	var nro_sessiones = 0;
	for (s in data) {

		nro_sessiones++;
		
		//console.log("nro_sessiones:" + nro_sessiones);
		var primera_clase = true;

		var session = data[s];
		var clases = session.clases;

		console.log("NRO:" + session.nro);

		if (clases.length == 0) {

			tabla.find('tbody').append('<tr data-sesion="' + session.nro + '"><td class="bg-primary-300" >' + session.nro + '<div class="list-icons-item"><a href="#" onclick="agregarDatos('+session.id+')"><i class="icon-add">Agregar Tipo</i></a></div></td><td></td><td></td><td></td><td></td></tr>');

		} else {
			for (cl in clases) {
				var clase = clases[cl];
				var grupos = clase.grupos;

				//console.log("***** GRUPOS->********");
				//console.log(grupos);
				//console.log("***** <-GRUPOS********");
				
				var tdClase = clase.tipo;
				var primer_grupo = true;

				if (primera_clase) {

					primera_clase = false;

					var rowspan_clase = clase.filas;
					var rowspan_grupos = 1;
					var rowspan_capacidades = 1;

					var tdGrupo = '';
					var tdDesempenio = '';
					var tdCapacidad = '';
					var tdEliminarEva = '<a>Eliminar Evaluación</<a>';
					var tdIndicador = '';

					if (clase.grupos.length > 0) {
						tdGrupo = "<ul>";
						for (g in clase.grupos[0].subtemas){
							tdGrupo = tdGrupo + "<li>" + clase.grupos[0].subtemas[g].subtema + "</li>"; 
						}

						rowspan_grupos = clase.grupos[0].filas;
					}
					
					if (clase.grupos.length > 0 && clase.grupos[0].capacidades.length>0 && clase.grupos[0].capacidades[0].desempenios.length > 0) {
						tdDesempenio = clase.grupos[0].capacidades[0].desempenios[0].desempenio;
						rowspan_capacidades = clase.grupos[0].capacidades[0].desempenios.length;
						/*
						tdIndicador = '<ul>';
						for (i in clase.grupos[0].capacidades[0].desempenios[0].indicadores){
							tdIndicador += '<li>' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].nom + '</li>';
						}
						tdIndicador += '</li>';
						*/
						
						var tdIndicador = '';
						for (i in clase.grupos[0].capacidades[0].desempenios[0].indicadores){
							tdIndicador += '<div class="list-icons">';
							if (clase.id_cts!='2')
								tdIndicador += '<a onclick="eliminarIndicador(' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].id + ')" class="list-icons-item"><i class="icon-trash"> </i></a>';
							if (clase.id_cts!='2')
								tdIndicador += '<a onclick="editarIndicador(' + clase.grupos[0].capacidades[0].desempenios[0].id + ',' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].id + ',\'' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].nom  +'\')" class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].nom ;
							else
								tdIndicador +=  '&nbsp;' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].nom ;
							tdIndicador += '</div>';
						}
						if (clase.id_cts!='2')//						
							tdIndicador = tdIndicador + '<div class="list-icons"><a href="#" onclick="editarIndicador(' + clase.grupos[0].capacidades[0].desempenios[0].id + ',\'\')" class="list-icons-item"><i class="icon-add"> Indicador</i></a></div>';

					}

					if (clase.grupos!=null && clase.grupos.length>0 && clase.grupos[0].capacidades.length>0 ){
						tdCapacidad =  '<div class="list-icons">';
					/*	if (clase.id_cts!='2')//
							tdCapacidad += '<a onclick="eliminarDesempenio(' + clase.grupos[0].capacidades[0].id_cap + ')" class="list-icons-item"><i class="icon-trash"> </i></a>'; */
						tdCapacidad += clase.grupos[0].capacidades[0].capacidad; 
						tdCapacidad += '</div>';
					}
						

					//tdCapacidad = tdCapacidad + '<div class="list-icons"><a href="#" onclick="agregarDesempenio(' + clase.id + ')" class="list-icons-item"><i class="icon-add"> Agregar Desempe&ntilde;o</i></a></div>';

					
					// primera fila de la tabla
					var htmltr = '<tr data-clase="' + clase.id + '"><td class="bg-primary-300" rowspan="' + session.filas + '"><div class="list-icons-item">' + session.nro + '</div><br/><div class="list-icons-item"><a href="#" onclick="agregarDatos('+session.id+')"><i class="icon-add">Agregar Tipo</i></a></div></td>';
					
					if (clase.id_cts=='2'){
						htmltr = htmltr +'<td data-clase="' + clase.id + '"rowspan="' + rowspan_clase + '"><a href="#" class="notatool"  data-popup="popover-custom" data-placement="top"  title="Instrumento" data-content="' + clase.ins+ '">' +  tdClase +'</a><div class="list-icons"><a href="#" onclick="agregarEvaluacion('+clase.id +')" class="list-icons-item"><i class="icon-add">Indicadores</i></a><br><a href="#" onclick="eliminarEvaluacion('+clase.id +')" class="list-icons-item text-danger-600"><i class="icon-subtract">Eliminar</i></a></div></td>';
				    }										
					else{
						htmltr = htmltr  +'<td data-clase="' + clase.id + '" rowspan="' + rowspan_clase + '">' + tdClase + '<div class="list-icons"><a href="#" onclick="agregarDesempenio(' + clase.id + ')" class="list-icons-item"><i class="icon-add">Tema</i></a><br><a href="#" onclick="eliminarDesempenio(' + clase.id + ')" class="list-icons-item text-danger-600"><i class="icon-subtract">Eliminar</i></a></div></td>';
					}					
					
					htmltr = htmltr + '<td rowspan="' + rowspan_grupos + '">' + tdGrupo + '</td>'
					+'<td  rowspan="' + rowspan_capacidades + '">' + tdCapacidad + '</td>'
					+'<td > ' + tdDesempenio + '</td>'
					+'<td>' + tdIndicador + '</td>'
					+'</tr>';
					
					tabla.find('tbody').append(htmltr);

					
					
					for (var st = 0; st < grupos.length; st++) {
						//debugger;
						var grupo = grupos[st];
						var tdGrupo = "<ul>";
						for (g in clase.grupos[st].subtemas){
							tdGrupo = tdGrupo + "<li>" + clase.grupos[st].subtemas[g].subtema + "</li>"; 
						}
						var capacidades = grupo.capacidades;

						
						for (var c = 0; c < capacidades.length; c++) {
							var capacidad = capacidades[c];
							
							//console.log("CAPACIDAD:" + capacidad.capacidad);
							var desempenios = capacidad.desempenios;
							var tdDesempenio = 'agregar desempenio';
							var rowspan_desempenio = 1;
							if (desempenios.length>0)
								tdDesempenio = desempenios[0].desempenio;

							var rowspan_capacidades = capacidad.desempenios.length;
							if (capacidad.desempenios.length==0)
								rowspan_capacidades = 1;

							var tdIndicador = '';
							for (i in capacidad.desempenios[0].indicadores){
								//tdIndicador += '<li>' + capacidad.desempenios[0].indicadores[i].nom + '</li>';
								//tdIndicador += '<div class="list-icons"><a class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;' + capacidad.desempenios[0].indicadores[i].nom + '</div>';
								if (clase.id_cts=='2')
									tdIndicador += '<div class="list-icons"> &nbsp;' + capacidad.desempenios[0].indicadores[i].nom + '</div>';
								else
									tdIndicador += '<div class="list-icons"><a onclick="eliminarIndicador(' + capacidad.desempenios[0].indicadores[i] + ')" class="list-icons-item"><i class="icon-trash"> </i></a><a onclick="editarIndicador(' + desempenios[0].id + ',' + capacidad.desempenios[0].indicadores[i].id + ',\'' + capacidad.desempenios[0].indicadores[i].nom + '\')" class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;' + capacidad.desempenios[0].indicadores[i].nom + '</div>';
							}
							//tdIndicador += '</ul>';
							if (clase.id_cts!='2')
								tdIndicador = tdIndicador + '<div class="list-icons"><a href="#" onclick="editarIndicador(' + desempenios[0].id + ',\'\')" class="list-icons-item"><i class="icon-add"> Indicador</i></a></div>';

							
							if(capacidad.desempenios[0].indicadores.length>0)
								rowspan_desempenio = capacidad.desempenios[0].indicadores.length;

							//console.log("st:" + st  + "c:" + c);							

							/**
							 * Primera capacidad del grupo
							 */
							if(st>0 && c==0){
								tabla.find('tbody').append(
										'<tr data-clase="' + clase.id + '" >'
										+'<td rowspan="' + grupo.filas + '">' + tdGrupo + '</td>'
										+'<td rowspan="' + rowspan_capacidades + '">' + capacidad.capacidad + '</td>'
										+'<td> ' + tdDesempenio + '</td>'
										+'<td>' + tdIndicador + '</td>'
										+'</tr>');
							}
							if(st==0 && c>0){
								tabla.find('tbody').append(
										'<tr data-clase="' + clase.id + '" >'
										+'<td rowspan="' + rowspan_capacidades + '">-' + capacidad.capacidad + '</td>'
										+'<td> ' + tdDesempenio + '</td>'
										+'<td>' + tdIndicador + '</td>'
										+'</tr>');
							}
							
							if(cl>0 && st==0 && c>0){
								
								
								
								tabla.find('tbody').append(
										'<tr  data-clase="' + clase.id + '">'
										+'<td rowspan="' + rowspan_capacidades + '">' + capacidad.capacidad + '</td>'
										+'<td> ' + tdDesempenio + '</td>'
										+'<td>' + tdIndicador + '</td>'
										+'</tr>');
							}
							
							//if(c>0){

								for (var d = 1; d < desempenios.length; d++) {
									
									var tdIndicador = '';
									for (i in capacidad.desempenios[d].indicadores){
										//tdIndicador += '<i class="icon-add">' + capacidad.desempenios[d].indicadores[i].nom + '</i>';
										//tdIndicador += '<div class="list-icons"><a class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;' + capacidad.desempenios[d].indicadores[i].nom + '</div>';
										if (clase.id_cts=='2')
											tdIndicador += '<div class="list-icons">&nbsp;' + capacidad.desempenios[d].indicadores[i].nom + '</div>';
										else
											tdIndicador += '<div class="list-icons"><a onclick="eliminarIndicador(' + capacidad.desempenios[d].indicadores[i].id + ')" class="list-icons-item"><i class="icon-trash"> </i></a><a onclick="editarIndicador(' + desempenios[d].id + ',' +capacidad.desempenios[d].indicadores[i].id + ',\''+  capacidad.desempenios[d].indicadores[i].nom +'\')" class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;' + capacidad.desempenios[d].indicadores[i].nom + '</div>';
									}
									//tdIndicador += '<div class="list-icons"><a class="list-icons-item"><i class="icon-pencil7"> </i></a> &nbsp;indicaro 3</div>';
									
									if (clase.id_cts!='2')
										tdIndicador = tdIndicador + '<div class="list-icons"><a href="#" onclick="editarIndicador(' + desempenios[d].id + ',\'\')" class="list-icons-item"><i class="icon-add"> Indicador</i></a></div>';
										
									
									//desempeños a partir de la segunda fila
									tabla.find('tbody').append('<tr data-clase="' + clase.id + '">'
										    +    '<td>' + desempenios[d].desempenio + '</td>'
										    +    '<td>' + tdIndicador + '</td></tr>');
								}

								
							//}
							
						 
						}
					}

				}else{
				
					//console.log("SEGUNDA CLASE");
					var tdClase = clase.tipo;
					var primer_grupo = true;
					
					var rowspan_clase = clase.filas;
					var rowspan_grupos = 1;
					var rowspan_capacidades = 1;

					var tdGrupo = '';
					var tdDesempenio = '';
					var tdCapacidad = '';
					var tdIndicador = '';

					if (clase.grupos.length > 0) {
						tdGrupo = "<ul>";
						for (g in clase.grupos[0].subtemas){
							tdGrupo = tdGrupo + "<li>" + clase.grupos[0].subtemas[g].subtema + "</li>"; 
						}

						rowspan_grupos = clase.grupos[0].filas;
					}
					
					if (clase.grupos.length>0 && clase.grupos[0].capacidades.length>0)
						tdCapacidad =  clase.grupos[0].capacidades[0].capacidad;
					
					if (clase.grupos.length > 0 && clase.grupos[0].capacidades.length>0 && clase.grupos[0].capacidades[0].desempenios.length > 0) {
						tdDesempenio = clase.grupos[0].capacidades[0].desempenios[0].desempenio;
						rowspan_capacidades = clase.grupos[0].capacidades[0].desempenios.length;
						tdIndicador = '<ul>';
						for (i in clase.grupos[0].capacidades[0].desempenios[0].indicadores){
							tdIndicador += '<li>' + clase.grupos[0].capacidades[0].desempenios[0].indicadores[i].nom + '</li>';
						}
						tdIndicador += '</li>';
					}
					
					
					// primera fila de la clase 2
					var htmlTr = '<tr  data-clase="' + clase.id + '">';
					if (clase.id_cts=='2'){//
						htmlTr += '<td data-sesion="' + session.nro + '" rowspan="' + rowspan_clase + '" ><a href="#" class="notatool"  data-popup="popover-custom" data-placement="top"  title="Instrumento" data-content="' + clase.ins+ '">' +  tdClase +'</a><div class="list-icons"><a href="#" onclick="agregarEvaluacion('+clase.id +')" class="list-icons-item"><i class="icon-add">Indicadores</i></a><br><a href="#" onclick="eliminarEvaluacion('+clase.id +')" class="list-icons-item"><i class="icon-add">Eliminar</i></a></div></td>';
						//htmlTr += '<td data-sesion="' + session.nro + '" rowspan="' + rowspan_clase + '">' + tdClase + '</td>';
					}else	
						htmlTr += '<td data-sesion="' + session.nro + '" rowspan="' + rowspan_clase + '">' + tdClase + '</td>';
					
					htmlTr += '<td rowspan="' + rowspan_grupos + '">' + tdGrupo + '</td>';
					htmlTr += '<td rowspan="' + rowspan_capacidades + '">' + tdCapacidad + '</td>';
					htmlTr += '<td > ' + tdDesempenio + '</td>';
					htmlTr += '<td>' + tdIndicador + '</td>';
					htmlTr += '</tr>';
					
					tabla.find('tbody').append(htmlTr);
								
					for (var st = 0; st < grupos.length; st++) {
						//debugger;
						var grupo = grupos[st];
						var tdGrupo = "<ul>";
						for (g in clase.grupos[st].subtemas){
							tdGrupo = tdGrupo + "<li>" + clase.grupos[st].subtemas[g].subtema + "</li>"; 
						}
						var capacidades = grupo.capacidades;

						//console.log('GRUPO:' + grupo.id_cgsp);
						
						for (var c = 0; c < capacidades.length; c++) {
							var capacidad = capacidades[c];
							
							//console.log('cap:' + capacidad.capacidad);
							
							var desempenios = capacidad.desempenios;
							var tdDesempenio = 'agregar desempenio';
							var rowspan_desempenio = 1;
							if (desempenios.length>0)
								tdDesempenio = desempenios[0].desempenio;

							var rowspan_capacidades = capacidad.desempenios.length;
							if (capacidad.desempenios.length==0)
								rowspan_capacidades = 1;

							var tdIndicador = '<ul>';
							for (i in capacidad.desempenios[0].indicadores){
								tdIndicador += '<li>' + capacidad.desempenios[0].indicadores[i].nom + '</li>';
							}
							tdIndicador += '</ul>';
							
							if(capacidad.desempenios[0].indicadores.length>0)
								rowspan_desempenio = capacidad.desempenios[0].indicadores.length;
							

							/**
							 * Primera capacidad del grupo
							 */
							 
							 //console.log("st:" + st + "c:" + c);
							 
							
							if(st>0 && c==0){
								//console.log('TRcap:' + capacidad.capacidad);
								tabla.find('tbody').append(
										'<tr data-clase="' + clase.id + '" >'
										+'<td rowspan="' + grupo.filas + '">' + tdGrupo + '</td>'
										+'<td rowspan="' + rowspan_capacidades + '">cccc' + capacidad.capacidad + '</td>'
										+'<td> ' + tdDesempenio + '</td>'
										+'<td>' + tdIndicador + '</td>'
										+'</tr>');
							}
							if(st==0 && c>0){
								//console.log('TRcap2:' + capacidad.capacidad);
								tabla.find('tbody').append(
										'<tr data-clase="' + clase.id + '" >'
										+'<td rowspan="' + rowspan_capacidades + '">xxx' + capacidad.capacidad + '</td>'
										+'<td> ' + tdDesempenio + '</td>'
										+'<td>' + tdIndicador + '</td>'
										+'</tr>');
							}
							//if(c>0){

								for (var d = 1; d < desempenios.length; d++) {
									
									var tdIndicador = '<ul>';
									for (i in capacidad.desempenios[d].indicadores){
										tdIndicador += '<li>' + capacidad.desempenios[d].indicadores[i].nom + '</li>';
									}
									tdIndicador += '</li>';
									
									//desempeños a partir de la segunda fila
									tabla.find('tbody').append('<tr data-clase="' + clase.id + '" >'
										    +    '<td>' + desempenios[d].desempenio + '</td>'
										    +    '<td>' + tdIndicador + '</td></tr>');
								}

								
							//}
							
						 
						}
					
					}
					
				}

			}
		}

	}

	$('.notatool').popover({
		template: '<div class="popover border-teal-400"><div class="arrow"></div><h3 class="popover-title bg-teal-400"></h3><div class="popover-content"></div></div>'
	});

}

function indicadoresLleno(index){

	var ii= 0;
	for(var i in _data){
		var session = _data[i];
		var id_ses = session.id;
		if (ii == index){
			//VALIDAR QUE EXISTA AL MENOS UN INDICADOR
			var clases = session.clases;
			if (clases.length==0)
				return false;
			
			for(c in clases){
				var clase = clases[c];
				var grupos = clase.grupos;
				
				if (grupos== null || grupos.length==0)
					return false;
				
				for( var g in grupos){
					var grupo = grupos[g];
					var capacidades = grupo.capacidades;
					if (capacidades== null || capacidades.length==0)
						return false;
				
					for( var ca in capacidades){
						var capacidad = capacidades[ca];
						var desempenios = capacidad.desempenios;
						
						if (desempenios== null || desempenios.length==0)
							return false;
						
						for (var d in desempenios){
							var desempenio = desempenios[d];
							if (desempenio.indicadores==null ||desempenio.indicadores.length==0)
								return false;
						}
					}
				}
			}
		}
			
		ii++;
	}
	
	return true;
}