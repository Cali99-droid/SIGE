//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){


}


//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	conf_cuota_listar_tabla();	
}


//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_conf_cuota_modal.html" id="mat_conf_cuota-btn-nuevo" ><i class="fa fa-plus-square-o ui-blue icono-more" aria-hidden="true"></i> Nuevo Configuraci&oacute;n de pagos matricula</a></li>');

	$('#mat_conf_cuota-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		var anio = $("#_ID_ANIO").text();
        $(this).data('anio' ,anio);
		conf_cuota_modal($(this));
	});

	//lista tabla
	conf_cuota_listar_tabla();
});

function conf_cuota_eliminar(link){
	_delete('api/confCuota/' + $(link).data("id"),
			function(){
					conf_cuota_listar_tabla();
				}
			);
}

function conf_cuota_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_conf_cuota_modal.html');
	conf_cuota_modal(link);
	
}

function conf_cuota_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_conf_cuota-frm');
		
		$('#mat_conf_cuota-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_cuota-frm #id').val('');
			_post($('#mat_conf_cuota-frm').attr('action') , $('#mat_conf_cuota-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		$('#id_cct').on('change',function(event){
				var id_per=$('#id_cct option:selected').attr("data-aux1")
				$('#id_per').val(id_per);
		});
		
		if (link.data('id')){
			_get('api/confCuota/datosCuota/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//_llenarCombo('per_periodo',$('#id_per'), data.id_per);
				$('#id_tpe').on('change',function(event){
					var param={id_gir:1, id_anio:$('#_id_anio').text(), id_tpe:$('#id_tpe').val(),  id_niv: $('#id_nvl').val()};
					_llenarComboURL('api/periodo/listarCiclosxGiroNegocioxTipPeriodo',$('#id_cct'), data.id_cct ,param,function(){$('#id_cct').change()});
				});	
				//var param={id_gir:1, id_anio:$('#_id_anio').text()};
				//_llenarComboURL('api/periodo/listarCiclosxGiroNegocio',$('#id_cct'), data.id_cct,param,function(){$('#id_cct').change()});
				$('#id_nvl').on('change',function(event){
				var param={id_anio:$("#_id_anio").text(), id_gir:1, id_niv: $('#id_nvl').val()};
					_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),data.id_tpe,param,function(){$('#id_tpe').change();});
				});
				_llenarCombo('cat_modalidad_estudio',$('#id_cme'), data.id_cme);
				if(data.tip_cuota_ing=='A'){
					$("#tip_cuota_ing").val('A').change();
				} else if(data.tip_cuota_ing=='U') {
					$("#tip_cuota_ing").val('U').change();
				}
				
				var param={id_gir:1};
				_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),data.id_niv, param, function() {$('#id_nvl').change();});
				$('#mat_conf_cuota-frm #btn-grabar').hide();
			});
		}else{
			//_llenarCombo('per_periodo',$('#id_per'));
			
			
			$('#id_tpe').on('change',function(event){
				var param={id_gir:1, id_anio:$('#_id_anio').text(), id_tpe:$('#id_tpe').val(), id_niv: $('#id_nvl').val()};
				_llenarComboURL('api/periodo/listarCiclosxGiroNegocioxTipPeriodo',$('#id_cct'), null,param,function(){$('#id_cct').change()});
			});	
			$('#id_nvl').on('change',function(event){
				var param={id_anio:$("#_id_anio").text(), id_gir:1, id_niv: $('#id_nvl').val()};
				_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
			});	
			_llenarCombo('cat_modalidad_estudio',$('#id_cme'));
			var param={id_gir:1};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),null, param, function() {$('#id_nvl').change();});
			$('#mat_conf_cuota-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_cuota_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de ingreso (A&ntilde;o ' +  link.data('anio')+ ')';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de ingreso (A&ntilde;o ' + link.data('anio') + ')';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function conf_cuota_listar_tabla(){
	_get('api/confCuota/listar/' + $('#_id_anio').text(),
			function(data){
			console.log(data);
				$('#mat_conf_cuota-tabla').dataTable({
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
			        	 	{"title":"A&ntilde;o", "data": "periodo.anio.nom"}, 
			        	 	{"title":"Local", "data": "periodo.sucursal.nom"}, 
							{"title":"Turno", "data": "turno.nom"}, 
			        	 	{"title":"Servicio", "data": "periodo.servicio.nom"}, 
							{"title":"Modalidad", "data": "modalidadEstudio.nom"}, 
			        	 	{"title":"Costo Matricula", "data" : "matricula","render":function( data, type, row,meta){
			        	 		return 'S./' +$.number( data, 2);
			        	 	}}, 
							{"title":"Cuota Ingreso", "data" : "cuota","render":function( data, type, row,meta){
			        	 		return 'S./' +$.number( data, 2);
			        	 	}}, 
							{"title":"Costo Reserva Vacante", "data" : "reserva","render":function( data, type, row,meta){
			        	 		return 'S./' +$.number( data, 2);
			        	 	}},
			        	 	{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" + data-anio ="' + row.periodo.anio.nom + '" onclick="conf_cuota_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="conf_cuota_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
			        	 	}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

