 
//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/per_periodo_modal.html" id="per_periodo-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Periodo</a></li>');

	$('#per_periodo-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		periodo_modal($(this));
	});

	//lista tabla
	periodo_listar_tabla();
});

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	periodo_listar_tabla();	
}


function periodo_eliminar(link){
	_delete('api/periodo/' + $(link).data("id"),
			function(){
					periodo_listar_tabla();
				}
			);
}

function periodo_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/per_periodo_modal.html');
	periodo_modal(link);
	
}

function periodo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('per_periodo-frm');
		$("#id_anio").val($('#_id_anio').text());
		$('#per_periodo-frm #btn-agregar').on('click',function(event){
			$('#per_periodo-frm #id').val('');
			_post($('#per_periodo-frm').attr('action') , $('#per_periodo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/periodo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//_llenarCombo2('col_anio',$('#id_anio'), data.id_anio);
				//_llenarCombo2('ges_servicio',$('#id_srv'), data.id_srv);
				_llenarComboURL('api/servicio/listarServicios',$('#id_srv'), data.id_srv);
				_llenarCombo('cat_tip_periodo',$('#id_tpe'), data.id_tpe);
				$("#fec_ini").val(_parseDate(data.fec_ini));
				$("#fec_fin").val(_parseDate(data.fec_fin));
				$("#fec_cie_mat").val(_parseDate(data.fec_cie_mat));
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			     //   autoUpdateInput: false,
			        locale: { format: 'DD/MM/YYYY'}
			    });
			});
		}else{
			//_llenarCombo2('col_anio',$('#id_anio'));
			//_llenarCombo2('ges_servicio',$('#id_srv'));
			_llenarComboURL('api/servicio/listarServicios',$('#id_srv'));
			_llenarCombo('cat_tip_periodo',$('#id_tpe'));
			$('#per_periodo-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		periodo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo de estudio';
	else
		titulo = 'Nuevo  Periodo de estudio';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function periodo_listar_tabla(){
	var params = {id_anio : $('#_id_anio').text()};
	_get('api/periodo/listar',
			function(data){
			console.log(data);
				$('#per_periodo-tabla').dataTable({
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
							{"title":"Local", "data" : "servicio.sucursal.nom"}, 
							{"title":"Nivel", "data" : "servicio.nom"}, 
							{"title":"Inicio", "data" : "fec_ini"}, 
							{"title":"Fin", "data" : "fec_fin"}, 
							{"title":"Fin Matr&iacute;cula", "data" : "fec_cie_mat"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Servicio", "data": "servicio.nom"}, 
							{"title":"Tipo de periodo", "data": "tipPeriodo.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="periodo_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="periodo_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},params
	);

}
