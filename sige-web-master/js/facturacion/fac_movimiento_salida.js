

//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){


	//lista tabla
	movimiento_listar_tabla();	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();	

	//botones
	$('#_botonera').html('<li><a href="pages/tesoreria/fac_movimiento_salida_modal.html" id="fac_movimiento-btn-nuevo" ><i class="icon-file-plus"></i> Nueva salida</a></li>');

	$('#fac_movimiento-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		movimiento_modal($(this));
	});


});

function movimiento_eliminar(link){
	_delete('api/movimiento/' + $(link).data("id"),
			function(){
					movimiento_listar_tabla();
				}
			);
}

function movimiento_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_movimiento_salida_modal.html');
	movimiento_modal(link);
	
}

function movimiento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_llenarCombo('fac_concepto',$('#id_fco'),null,'S');
		//_llenarCombo('mat_matricula',$('#id_mat'));
		$('#id_suc').val($('#_id_suc').text()); 

		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true
	    });
		
		_get('api/common/fecha',function(data){$('#fec').val(data); });

		_llenarCombo('fac_banco',$('#id_fba'),null);

		if (link.data('id')){
			_get('api/movimiento/' + link.data('id'),
			function(data){
				_fillForm(data,$('.modal').find('form') );
				}
			);
		}


	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		movimiento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Entradas y salidas';
	else
		titulo = 'Ingrese los datos del pago a realizar';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function movimiento_listar_tabla(){
	
		_get('api/movimiento/listar?id_suc=' + $("#_id_suc").text() + '&tipo=S',
				function(data){
				console.log(data);
					$('#fac_movimiento-tabla').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 select: true,
				         columns : [ 
						       {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						       {"title":"N&uacute;mero Operaci&oacute;n", "data" : "nro_rec"}, 
				               {"title":"Fecha", "data" : "fec"}, 
				               {"title":"Concepto", "data" : "concepto.nom"}, 
				               {"title":"Obs.", "data" : "obs"}, 
				               {"title":"Monto total", "data" : "monto_total","render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}}, 
					           {"title":"Acciones", "render": function ( data, type, row ) {
				                   return '<ul class="icons-list">'+
										'<li class="dropdown">'+
									'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
										'<i class="icon-menu9"></i>'+
										'</a>'+
									'<ul class="dropdown-menu dropdown-menu-right">'+
										'<li><a href="#" data-id="' + row.id + '" onclick="movimiento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
										'<li><a href="#" data-id="' + row.id + '" onclick="movimiento_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'</ul>'+
				                   '</li>'+
				                   '</ul>';}
				        	   }
					    ]
				    });
				}
			);
		
	
}

function onchangeConcepto(field){
    var element = $("option:selected", $(field));

	$("#monto").val(element.data("aux1"));
}	
