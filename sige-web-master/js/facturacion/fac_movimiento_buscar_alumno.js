var _tipo_movimiento;
function onloadPage(params){
	if (typeof(params)=='undefined')
		_tipo_movimiento = "I";
	else
		_tipo_movimiento = params.tipo;
	_onloadPage();	
	$('#_botonera').html('<li><a href="pages/tesoreria/fac_movimiento_ingreso_modal.html" id="fac_movimiento-btn-otros-nuevo" ><i class="icon-file-plus"></i> Otros pagos</a></li>');

	_tipo = "I";
	$('#fac_movimiento-btn-otros-nuevo').on('click', function(e) {
		e.preventDefault();
		movimiento_otro_modal($(this));
	});
}


function movimiento_otro_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_arreglo_json = [];
		_inputs();
		_llenarCombo('fac_concepto',$('#id_fco'),null,_tipo);
		//_llenarCombo('mat_matricula',$('#id_mat'));
		$('#id_suc').val($('#_id_suc').text()); 
		$('#id_mat').val(_id_mat); 
		$('#alumno').val(_alumno); 
		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true,
	        locale: { format: 'DD/MM/YYYY'},
	    });
		
		_get('api/common/fecha',function(data){$('#fec').val(data); });

		if(_tipo=='S')
			_llenarCombo('fac_banco',$('#id_fba'),null);

		if (link.data('id')){
			_get('api/movimiento/' + link.data('id'),
			function(data){
				console.log(_arreglo_json);
				_fillForm(data,$('.modal').find('form') );
				$('.movimiento_ver').hide();
				_arreglo_json = data.movimientoDetalleReqs;
				 movimiento_listar_detalle(_arreglo_json);
				}
			);
		}else{
			if(_tipo=='I')
			_get('api/movimiento/nro_rec/' + $('#_id_suc').text(),
					function(data){
					//alert(data);
						$('#nro_rec').val(data);
						}
					);
		}

	}

	//funcion q se ejecuta despues de grabar
	var fnc_agregar_detalle = function(data){

		var existeElemento = false;
		for (index in _arreglo_json) {
			var element = _arreglo_json[index];
			if (element.id_fco == data.id_fco)
				existeElemento = true;
		}
		
		if(existeElemento){
			swal({
				title : "Error",
				text : "Concepto ya existe",
				confirmButtonColor : "#2196F3",
				type : "error"
			});
		}else{
			_arreglo_json.push(data);
			movimiento_listar_detalle(_arreglo_json);
			$('#id_fco').val('');
			$('#obs').val('');
			$('#monto').val('');			
		}
		
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Entradas y salidas';
	else
		titulo = 'Ingrese los datos del pago a realizar';
	
	abrir_modal(titulo, link.attr('href'),onShowModal,fnc_agregar_detalle);

}

$(function(){
	$("#alumno").focus();

	var fncExito = function(data){
		$('#panel-alumnos').css("display","block");

		
		$('#tabla-alumnos').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 50,
			 select: true,
	         columns : [ 
	             {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	             {"title": "Contrato", "data" : "num_cont"}, 
	             {"title":"Alumno", "data" : "alumno",
	        	   "render": function ( data, type, row ) {
	                   return '<a href="#" onclick="conceptosxPagar(' + row.id + ',' + row.id_alu + ',\'' + row.alumno +'\')" >'  + row.alumno +' </a>';}
	        	   }
	        ]
	    });
		
	};
	
	$('#frm-pagos-buscar').on('submit',function(event){
		event.preventDefault();
		
		$("#id_suc").val($("#_id_suc").html());
		$("#id_anio").val($("#_id_anio").text());
		//alert($("#id_anio").val());
		return _get('api/alumno/consultar',fncExito, $(this).serialize());
	});
	

	
});


/******************************************/
/** 	Mostramos la pantalla para que pague el padre del alumno		***/
/******************************************/
function conceptosxPagar(id_mat, id_alu,alumno){
	
		_send('pages/tesoreria/fac_movimiento_ingreso.html','Ingresos Caja','Pago de concepto', {id_mat:id_mat, id_alu:id_alu, alumno:alumno,tipo:"I"});
	

	
}