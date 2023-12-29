//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_cronograma_reserva_modal.html" id="mat_cronograma_reserva-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Cronograma de Reserva</a></li>');

	$('#mat_cronograma_reserva-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cronograma_reserva_modal($(this));
	});

	//lista tabla
	cronograma_reserva_listar_tabla();
	
});

function _onchangeAnio(id_anio, anio){
	cronograma_reserva_listar_tabla();
}

function cronograma_reserva_eliminar(link){
	_delete('api/cronogramaReserva/' + $(link).data("id"),
			function(){
					cronograma_reserva_listar_tabla();
				}
			);
}

function cronograma_reserva_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_cronograma_reserva_modal.html');
	cronograma_reserva_modal(link);
	
}

function cronograma_reserva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_cronograma_reserva-frm');
		
		$('#mat_cronograma_reserva-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma_reserva-frm #id').val('');
			_post($('#mat_cronograma_reserva-frm').attr('action') , $('#mat_cronograma_reserva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cronogramaReserva/' + link.data('id'),
			function(data){
				var fec_ini= _parseDate(data.fec_ini);
				var fec_fin= _parseDate(data.fec_fin);
				$("#fec_ini").val(fec_ini);
				$("#fec_fin").val(fec_fin);
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        locale: { format: 'DD/MM/YYYY'}
			    });
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
			});
		}else{
			_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('cat_nivel',$('#id_niv'));
			$('#mat_cronograma_reserva-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_reserva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma de Reserva';
	else
		titulo = 'Nuevo  Cronograma de Reserva';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cronograma_reserva_listar_tabla(){
	var param={id_anio: $('#_id_anio').text()};
	_get('api/cronogramaReserva/listar/',
			function(data){
			console.log(data);
				$('#mat_cronograma_reserva-tabla').dataTable({
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
			        	 	{"title":"A&ntilde;o", "data": "anio.nom"}, 
			        	 	{"title":"Nivel", "data": "nivel.nom"}, 
			        	 	{"title":"Fecha Inicio", "data" : "fec_ini"}, 
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="cronograma_reserva_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="cronograma_reserva_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

