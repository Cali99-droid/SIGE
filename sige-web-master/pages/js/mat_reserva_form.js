//Se ejecuta cuando la pagina q lo llama le envia parametros
var _alumno;
var _reserva;

function onloadPage(params){
	console.log(params);
	if(("condicional" in params) && params.condicional!='')
		 new PNotify({
	         title: 'Ayuda',
	         text: params.condicional,
	         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
	         delay:10000
	     });
	_alumno = params.alumno;
	_reserva = params.reserva;
	_cronograma= params.cronograma;
	console.log(_cronograma);
	$( '#frm-reserva #fec_lim' ).val(_parseDate(_reserva.fec_lim));
	
	//$( '#frm-reserva #fec_lim' ).val('28/02/2022'); //verificar si se usa
	_inputs();
	$( '#nom_alu').text(_alumno.ape_pat+' '+_alumno.ape_mat+' '+_alumno.nom);
	
	//$('#id_au').on('change',function(event){
	$('#id_gra').on('change',function(event){
				var params = {"id_au": $('#id_au').val(), id_anio: $('#_id_anio').text(), id_alu:$('#id_alu').val(), id_grad:$('#id_gra').val(), id_suc:$('#_id_suc').text() };
		
		//var id_au = $('#id_au').val();
		var id_gra = $('#id_gra').val();
		
		if(id_gra!=''){
			_get('api/reserva/obtenerNroVacxGrado',
				function(data){
				console.log(data);
	    			var capacidad=data.capacidad;
	    			var nro_vac=data.nro_vac;
	    			$('#spanCap').html("de " + capacidad);
	    			$('#nro_vacantes').val(nro_vac);
	    			if(parseInt($('#nro_vacantes').val())<=0)
	    				$('#btn-grabar').attr("disabled", "disabled");
	    			else
	    				$('#btn-grabar').removeAttr("disabled");
			},params);
		}
	});	
	_llenarCombo('cat_nivel',$('#id_niv'), _reserva.id_niv);
	_llenarCombo('cat_grad',$('#id_gra'), _reserva.id_gra, null,function(event){
		$('#id_gra').change();
	});
	var param={id_gra:_reserva.id_gra,id_anio:$("#_id_anio").text(), id_gir:1, id_suc:$('#_id_suc').text()};
	// A partir del año 2022 ya no se utilizará las reservas por Aula, solo será por grado
	
	/*_llenarComboURL('api/aula/listarAulasxGradoLocal', $('#id_au'),_reserva.id_au, param, function() {
		$('#id_au').change();
	//_llenarCombo('col_aula_local_nuevo',$('#id_au'), _reserva.id_au,$("#_id_anio").text() + ',' + _reserva.id_gra + ',' + $('#_id_suc').text(), function(){
		$('#id_au').attr("disabled", true);
		if((_reserva.id_au!='' && _reserva.id_au!=null)  && _cronograma=='AC'){
			$('#id_au').attr("disabled", true);
		}else if( _cronograma=='AS'){
//			if (params.solicitud_cambio_local=='1'){
				$('#id_au').removeAttr("disabled");
//			}else{
				//alert('disabled');
				//$('#id_au').attr("disabled", 'disabled');
//			}
		}else{
			//$('#id_au').attr("disabled", false);
		}
		
		//onchangeCap(document.forms[0].id_au);
	});*/
	_llenarComboURL('api/alumno/listarFamiliares',$('#id_fam'),_reserva.id_fam, {id_alu: _alumno.id} );
	_llenarCombo('cat_cond_matricula',$('#id_con'), _reserva.id_con);

	$( '#frm-reserva #id_per' ).val(_reserva.id_per);
	$( '#frm-reserva #id_alu' ).val(_reserva.id_alu);
	$( '#frm-reserva #id_cli' ).val(_reserva.id_cli);
	
	$( '#frm-reserva #id ' ).val(_reserva.id);
	
	$('#monto').val(_reserva.reservaCuota.monto);
	
	
	
if (_reserva.id == null){
	$('#btn-imprimir').attr("disabled", "disabled");
	$('#btn-imprimir-bol').attr("disabled", "disabled");
	$('#btn-pdf-bol').attr("disabled", "disabled");
	$('#btn-grabar').removeAttr("disabled");
}else{
	$('#btn-imprimir').removeAttr("disabled");
	$('#btn-imprimir-bol').removeAttr("disabled");
	$('#btn-pdf-bol').removeAttr("disabled");
	$('#btn-grabar').attr("disabled", "disabled");
}


	
	//botones
	//btn grabar
	$('#frm-reserva').on('submit', function(event){
		event.preventDefault();
		
		/*if (_reserva.id!=null &&  _reserva.id_au==$('#id_au').val()){
			
			 new PNotify({
		         title: 'AVISO',
		         text: 'Los datos de la reserva no ha sido modificada,\n si desea grabar debe cambiar de sección.',
		         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
		         delay:1500
		     });

			return;
		}*/
		
		$( '#frm-reserva #id_gra' ).removeAttr("disabled");
		$( '#frm-reserva #id_niv' ).removeAttr("disabled");
		$( '#frm-reserva #id_con' ).removeAttr("disabled");
		$( '#frm-reserva #fec' ).removeAttr("disabled");
		$('#id_au').attr("disabled", false);
		if($('#nro_vacantes').val()>0){
		_post('api/reserva/grabar' , $(this).serialize(), function (data){
			console.log(data.result);
			$( '#frm-reserva #id' ).val(data.result.id);
			
			//alert(_reserva.id);
			if (_reserva.id ==null){
				_reserva.reservaCuota.id_fmo = data.result.id_fmo;
				_reserva.reservaCuota.nro_recibo=data.result.nro_rec;
			}
			$('#btn-imprimir').removeAttr("disabled");
			$('#btn-pdf-bol').removeAttr("disabled");
			
			if (_reserva.id ==null)
				$('#btn-imprimir-bol').click();
			$('#id_au').attr("disabled", true);
		});
		$( '#frm-reserva #id_gra' ).attr("disabled","disabled");
		$( '#frm-reserva #id_niv' ).attr("disabled","disabled");
		$( '#frm-reserva #id_con' ).attr("disabled","disabled");
		$( '#frm-reserva #id_cli' ).attr("disabled","disabled");
		$( '#frm-reserva #fec' ).attr("disabled","disabled");
		} else {
			alert('No hay vacantes disponibles en este grado');
		}	
	});
	
	//btn imprimir
	$('#btn-imprimir').on('click', function(event){
		document.location.href=_URL + "api/archivo/imprimir/" + $( '#frm-reserva #id' ).val();
		/* 
		
				*/
			 
	});
	
	//btn pdf boleta
	$('#btn-pdf-bol').on('click', function(event){
		document.location.href=_URL + "api/archivo/pdf/boleta/" + _reserva.reservaCuota.id_fmo+"/"+_reserva.id_alu;
	});

	//btn imprimir impresora
	$('#btn-imprimir-bol').on('click', function(event){
		
		var nro_rec= _reserva.reservaCuota.nro_recibo;
		
		 _post_json('api/movimiento/imprimir/caja/'+nro_rec+'/'+$('#_id_anio').text()+'/'+_reserva.id_alu,null, function(data){
			 $.ajax({
					type: "POST",
					url: "http://localhost:8082/api/print",
					data: JSON.stringify(data.result),
					contentType : 'application/json',
					dataType : 'json',
					success: function(data){
					}});
			});		
	});

}

 //no se usa , pero guardado x si desean modificar
/*	function onchangeCap(campo){
	var url = _URL + "api/reserva/obtenerNroVac";
			$.ajax({
	    		url : url,
	    		type : 'get',
	    		data : params,
	    		success : function(data) {
	    			console.log(data);
	    			var capacidad=data.result.capacidad;
	    			var nro_vac=data.result.nro_vac;
	    			$('#spanCap').html("de " + capacidad);
	    			$('#nro_vacantes').val(nro_vac);
	    			if(parseInt($('#nro_vacantes').val())<=0)
	    				$('#btn-grabar').attr("disabled", "disabled");
	    			else
	    				$('#btn-grabar').removeAttr("disabled");
	    		/*	var alumnos_de_otra_seccion_sugeridos 	= parseInt(data.result.alumnos_de_otra_seccion_sugeridos);
	    			var pasan_anio							= parseInt(data.result.pasan_anio);
	    			var total_inscritos						= parseInt(data.result.total_inscritos);
	    			var vacantes_disponibles				= parseInt(data.result.vacantes_disponibles); 
	    			var inscritos							= parseInt(data.result.inscritos);
	    			var reserva								= parseInt(data.result.reserva);
	    			var alumnos_se_van_otra_seccion_sugerido= parseInt(data.result.alumnos_se_van_otra_seccion_sugeridos); 
	    			var capacidad							= parseInt(data.result.capacidad);
	    			
	    			var disponibles							= capacidad - pasan_anio - reserva;
	    			
	    			if ($("#aula_sugerida").val()=="1")
	    				$('#nro_vacantes').val(vacantes_disponibles +1 + alumnos_de_otra_seccion_sugeridos);
					else
						$('#nro_vacantes').val(vacantes_disponibles + alumnos_de_otra_seccion_sugeridos);
	    			$('#spanCap').html("de " + capacidad);
	    			
	    			if(parseInt($('#nro_vacantes').val())<=0)
	    				$('#btn-grabar').attr("disabled", "disabled");
	    			else
	    				$('#btn-grabar').removeAttr("disabled");
	    		*/	
	    			
	/*    			}
	    		});
	}*/

