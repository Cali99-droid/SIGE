//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	//botones
	$('#_botonera').html('<li><a href="#" id="nuevo" onclick="alumno_descuento_modal()"><i class="icon-file-plus"></i> Nuevo descuento</a></li>');
	
	
	$('#fac_alumno_descuento-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		alumno_descuento_modal($(this));
	});

	//lista tabla
	alumno_descuento_listar_tabla();
});

function alumno_descuento_eliminar(link){
	_delete('api/alumnoDescuento/' + $(link).data("id"),
			function(){
					alumno_descuento_listar_tabla();
				}
			);
}

function alumno_descuento_editar(row,e){
	e.preventDefault();
	
	var link = $(row);

	alumno_descuento_modal(link.data('id'));
	
}

function alumno_descuento_modal(id, lectura){
	
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		$('#alumno').focus();
			var origin = window.location.origin;

	_URL = window.location.origin.replace(':8080','')+ ":8081/sige-mat/";
		//alert('api/alumno/consultar?alumno=&id_anio=' + $('#_id_anio').text() + '&id_suc=' + _usuario.id_suc);
	    $("#alumno").autocomplete({
	        minLength: 2,
	        //source: _URL + 'api/alumno/autocompleteParaDescuento?id_anio=' + $('#_id_anio').text() + '&id_suc=' + _usuario.id_suc,
			source: _URL + 'api/archivo/autocompleteParaDescuento?id_anio=' + $('#_id_anio').text() + '&id_suc=' + _usuario.id_suc,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	$('.detalle_alumno').show();
	        	$('#id_mat').val(ui.item.id);
	        	$('#mensualidad').focus();
	        	$('#mensualidad').val('0');
	        	_get('api/alumnoDescuento/descuentos/' + ui.item.id,
	        			function(data){
	        				
	        				$('.detalle_alumno').show();
	        				$('#nivel').val(data.nivel);
	        				$('#grado').val(data.grado);
	        				$('#seccion').val(data.seccion);
	        				$('#monto').val(data.monto);
	        				$('#span_desc_secre').html('Descuento: S/.' + $.number(0,2));
	        				$('#ldbMonto').html('Monto a pagar secretaria (Original S/.' + $.number(data.monto,2) + ')');
	        				
	        				
	        				}
	        			);
	        },
	       
	    });

		if (lectura){
			 _disabled('#alumno_descuento-form');
		}
		
		if(id){
			
			$('.detalle_alumno').show();
			_get('api/alumnoDescuento/' + id,
			function(data){
				_fillForm(data,$('.modal').find('form') );
				_inputs();
				
				$('#span_desc_secre').html('Descuento: S/.' + $.number(data.descuento,2));
				$('#ldbMonto').html('Monto a pagar secretaria (Original S/.' + $.number(data.monto,2) + ')');
				$('#span_desc_banco').html('Descuento: S/.' + $.number(parseFloat(data.monto) - parseFloat(data.mensualidad_bco),2));

				}
			);
		}else{
			$('.detalle_alumno').hide();
		}
		


	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_descuento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if(lectura){
		titulo = 'Detalle del Descuento del alumno';
		$('#btnModalAlumnoDesc').hide();
	}else{
		if (id)
			titulo = 'Editar Descuento del alumno';
		else
			titulo = 'Nuevo  Descuento del alumno';		
	}

	
	_modal(titulo, 'pages/tesoreria/fac_alumno_descuento_modal.html',onShowModal,onSuccessSave);

}

function onkeyAlumno(){

     	$('.detalle_alumno').hide();

}
function onkeypressMensualidad(field){
	var mensualidad = field.value;
	var descuento = parseFloat($('#monto').val()) - parseFloat(mensualidad);
	$('#descuento').val(descuento);
	$('#span_desc_secre').html('Descuento: S/.' + $.number(descuento,2));
}

function onkeypressMensualidadBco(field){
	var mensualidad_bco = field.value;
	var descuento = parseFloat($('#monto').val()) - parseFloat(mensualidad_bco);
	$('#span_desc_banco').html('Descuento: S/.' + $.number(descuento,2));
}


function alumno_descuento_listar_tabla(){
	_get('api/alumnoDescuento/listar/' + $('#_id_anio').text(),
			function(data){
			console.log(data);
				$('#fac_alumno_descuento-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,

					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
				     	   {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
			               {"title":"Alumno", "data" : "alumno"}, 
			               {"title":"Nivel", "data" : "nivel"}, 
			               {"title":"Grado", "data" : "grado"}, 
			               {"title":"P. Secretaria", "render": function ( data, type, row ) { return 'S/.' +  $.number(row.mensualidad,2) }},
			               {"title":"P. Banco", "render": function ( data, type, row ) { return 'S/.' +  $.number(row.mensualidad_bco,2) }},
			               {"title":"Motivo",  "data" : "motivo"},
			               {"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="alumno_descuento_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="alumno_descuento_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
						   }
			               }
				    ]
			    });
			}
	);

}