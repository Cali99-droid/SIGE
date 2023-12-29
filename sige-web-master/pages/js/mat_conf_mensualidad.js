//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	conf_mensualidad_listar_tabla();	
}


//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_conf_mensualidad_modal.html" id="mat_conf_mensualidad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Configuraci&oacute;n de mensualidad</a></li>');

	$('#mat_conf_mensualidad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		var anio = $("#_ID_ANIO").text();
        $(this).data('anio' ,anio);
		conf_mensualidad_modal($(this));
	});

	//lista tabla
	conf_mensualidad_listar_tabla();
});

function conf_mensualidad_eliminar(link){
	_delete('api/confMensualidad/' + $(link).data("id"),
			function(){
					conf_mensualidad_listar_tabla();
				}
			);
}

function conf_mensualidad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_conf_mensualidad_modal.html');
	conf_mensualidad_modal(link);
	
}

function conf_mensualidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_conf_mensualidad-frm');
		
		$('#mat_conf_mensualidad-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_mensualidad-frm #id').val('');
			_post($('#mat_conf_mensualidad-frm').attr('action') , $('#mat_conf_mensualidad-frm').serialize(),
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
			_get('api/confMensualidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				$('#id_tpe').on('change',function(event){
					var param={id_gir:1, id_anio:$('#_id_anio').text(), id_tpe:$('#id_tpe').val(),  id_niv: $('#id_nvl').val()};
					_llenarComboURL('api/periodo/listarCiclosxGiroNegocioxTipPeriodo',$('#id_cct'), data.id_cct ,param,function(){$('#id_cct').change()});
				});	
				$('#id_nvl').on('change',function(event){
				var param={id_anio:$("#_id_anio").text(), id_gir:1, id_niv: $('#id_nvl').val()};
					_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),data.periodo.id_tpe,param,function(){$('#id_tpe').change();});
				});
				_llenarCombo('cat_modalidad_estudio',$('#id_cme'), data.id_cme);
				var param={id_gir:1};
				_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_nvl'),data.periodo.id_niv, param, function() {$('#id_nvl').change();});
			});
		}else{
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
			$('#mat_conf_mensualidad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_mensualidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de mensualidad (A&ntilde;o ' +  link.data('anio')+ ')';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de mensualidad (A&ntilde;o ' +  link.data('anio')+ ')';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function conf_mensualidad_listar_tabla(){
	_get('api/confMensualidad/listar/' + $('#_id_anio').text(),
			function(data){
			console.log(data);
				$('#mat_conf_mensualidad-tabla').dataTable({
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
			        	 	{"title":"Servicio", "data": "periodo.servicio.nom"},
			        	 	{"title":"Local", "data": "periodo.servicio.sucursal.nom"},
							{"title":"Turno", "data": "turno.nom"}, 
							{"title":"Modalidad", "data": "modalidadEstudio.nom"}, 
							{"title":"Monto de Mensualidad", "data" : "monto","render":function( data, type, row,meta){
			        	 		return 'S./' + _formatMonto(data);
			        	 	}}, 
			        	 	{"title":"Dcto. Secretaria", "data" : "descuento","render":function( data, type, row,meta){
			        	 		return 'S./' +  _formatMonto(data);
			        	 	}},  
			        	 	{"title":"Dcto. Banco", "render":function( data, type, row,meta){
			        	 		return 'S./' + _formatMonto(row.desc_banco);
			        	 	}},  
							{"title":"&Uacute;ltimo D&iacute;a de Pago", "data" : "dia_mora"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" + data-anio ="' + row.periodo.anio.nom + '" onclick="conf_mensualidad_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="conf_mensualidad_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

