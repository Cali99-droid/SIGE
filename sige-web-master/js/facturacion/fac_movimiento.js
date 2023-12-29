var _id_mat;
var _alumno;
var _tipo;
var _arreglo_json;
//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();	
	
	//botones
	$('#_botonera').html('<li><a href="pages/tesoreria/fac_movimiento_ingreso_modal.html" id="fac_movimiento-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo ingreso de caja</a></li>');

	$('#fac_movimiento-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		movimiento_modal($(this));
	});
	
	_get('api/matricula/apoderado/' + params.id_mat,function(data){
		console.log(data);
		//RESPONSE - MOSTRAMOS EN PANTALLA LOS DATOS DEL APODERADO
		$("#apoderado\\.nom").val(data.apoderado.nom);
		$("#apoderado\\.nro_doc").val(data.apoderado.nro_doc);
		$("#apoderado\\.cel").val(data.apoderado.cel);
		$("#apoderado\\.direccion").val(data.apoderado.direccion);
		$("#apoderado\\.num_cont").html(data.apoderado.num_cont);
		var url_img = _URL + 'Foto/familiar/' + data.apoderado.id;

		$("#apoderado\\.foto").attr("src", url_img );
		
	});
	_id_mat = params.id_mat;
	_id_alu = params.id_alu;
	_alumno = params.alumno;
	_tipo = params.tipo;
	
	console.log(params);
	//lista tabla
	movimiento_listar_tabla();	
}
//se ejecuta siempre despues de cargar el html
$(function(){


});

function movimiento_eliminar(link){
	_delete('api/movimiento/' + $(link).data("id"),
			function(){
					movimiento_listar_tabla();
				}
			);
}


function movimiento_detalle_eliminar(id_fco){
	
	var nuevo_arreglo_detalle = [];
	
	for (index in _arreglo_json) {
		
		var element = _arreglo_json[index];
		
		if (element.id_fco != id_fco)
			nuevo_arreglo_detalle.push(element);
		
	}
	_arreglo_json = nuevo_arreglo_detalle;
	movimiento_listar_detalle(_arreglo_json);

}

function movimiento_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_movimiento_ingreso_modal.html');
	movimiento_modal(link);
	
}

function movimiento_imprimir(row,e){
	e.preventDefault();
	var link= $(row);
	var nro_rec= link.data('numrec');
	
		 _post_json('api/movimiento/imprimir/caja/'+nro_rec,null, function(data){
			 $.ajax({
					type: "POST",
					url: "http://localhost:8081/api/print",
					data: JSON.stringify(data.result),
					contentType : 'application/json',
					dataType : 'json',
					success: function(data){
					}});
			});		
	
	
}

function movimiento_eliminar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_movimiento_ingreso_modal.html');
	movimiento_modal(link);
	
}


function movimiento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_arreglo_json = [];
		_llenarCombo('fac_concepto',$('#id_fco'), null, _tipo, function(){
			$('#id_fco').change();
		});	
		
		$('#id_suc').val($('#_id_suc').text()); 
		$('#id_mat').val(_id_mat); 
		$('#alumno').val(_alumno); 
		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true,
	        locale: { format: 'DD/MM/YYYY'},
	    });
		_inputs();

		_get('api/common/fecha',function(data){$('#fec').val(data); });

		if(_tipo=='S'){
			_llenarCombo('fac_banco',$('#id_fba'),null);
			_inputs();
		}
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
						$('#nro_rec').val(data);
						}
					);
		}

	}

	//BOTON "<AGREGAR>"
	//Esta funcion crea un regisro TR en la tabla DETALLLE.. TODAVIA NO GRABA.. SOLO LO AGREGA COMO FILA...
	var fnc_agregar_detalle = function(data){//este es el callback
		console.log(data);

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

function abrir_modal(titulo,url_html,callBackOnShowModal,callBackSave){
	
	  try{
		  $modal = $('#modal').modal('show');
		  
		  $modal.find('.modal-body').load(url_html, function(){
			  $modal.find('.modal-title').html(titulo);
			  $modal.find('form').on('submit',function(event){
		    		event.preventDefault();

		    		var id_fco = $('#id_fco').val();
		    		var obs = $('#obs').val();
		    		var monto = $('#monto').val();
		    		var concepto = $('#id_fco option:selected').text();

		    		
		    		var row_json = {"concepto": concepto, "obs": obs, "monto":monto, "id_fco": id_fco  };
		    		callBackSave(row_json);

				});

			  callBackOnShowModal();

		  });
		  
		  
	  }catch(e){
		  console.error(e);
	  }
	
}

function movimiento_listar_tabla(){
		_get('api/movimiento/listar?id_suc=' + _usuario.id_suc + '&tipo=' + _tipo + "&id_mat=" + _id_mat + "&id_alu=" + _id_alu + "&id_anio=" + $("#_id_anio").text(),
			function(data){
			console.log(data);
				$('#fac_movimiento-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 select: true,
			         columns : [ 
					       {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					       {"title":"N&uacute;mero Recibo", "data" : "nro_rec"}, 
			               {"title":"Fecha", "data" : "fecha"}, 
			               {"title":"Concepto", "data" : "concepto"}, 
			               {"title":"Monto total", "data" : "monto_total","render":function ( data, type, row ) {return 'S./' + $.number( data, 2);}}, 
			               {"title":"Detalle",  "render": function ( data, type, row ) {
			            	   //if (row.tabla=='M' || row.tabla=='E')
			            		   return ' <a href="#" data-id="' + row.id + '" onclick="movimiento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Detalle</a>';
			            	   //else
			            		//   return "";
			                }}, 
			                {"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="movimiento_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-numrec="' + row.nro_rec + '" onclick="movimiento_imprimir(this, event)" class="list-icons-item" title="Imprimir"><i class="fa fa-print ui-blue ui-size"></i></a>'+
								' <a href="#" data-id_fmo="' + row.id_fmo + '" onclick="pdf(event,this)" class="list-icons-item" title="Generar Recibo"><i class="icon-file-pdf mr-3 ui-blue ui-size" aria-hidden="true"></i></a></div>';
								}
			        	   }
				    ]
			    });
			}
		);

	
}
/*
function movimiento_imprimir(data){
	var nro_rec = $(data).data('nro_rec');
	_post_silent('api/movimiento/imprimir/caja/'+ nro_rec,null,
			function(data){
				_post_json( "http://localhost:8081/api/print", data.result, 
					function(data){
						console.log(data);
						//$("#_btnRefresh").click();
					}
				);
		}
	);
}
*/

function movimiento_listar_detalle(_arreglo_json){

	$('#fac_movimiento_detalle-tabla').dataTable({
			 data : _arreglo_json,
			 aaSorting: [],
			 destroy: true,
			 select: true,
	         columns : [ 
			       {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			       {"title":"Concepto", "data" : "concepto"}, 
			       {"title":"Monto", "data" : "monto"}, 
			       {"title":"Obs", "data" : "obs"}, 
		           {"title":"Acciones", "render": function ( data, type, row ) {
		        	   if (row.id)
		        		   return "No editable";
 		        	   else
	                      return '<a href="#" data-id="' + row.id + '" onclick="movimiento_detalle_eliminar(' + row.id_fco + ')"><i class="fa fa-trash-o"></i> Eliminar</a>';}
	        	   }
		    ]
	    });
	
	
	refreshBtnPagar();				
}


function onchangeConcepto(field){
    var element = $("option:selected", $(field));

	$("#monto").val(element.data("aux1"));
	
	var editable = element.data("aux3");
	if (editable=="1")
		$('#monto').prop('readOnly', false);
	else
		$('#monto').prop('readOnly', true);
}	

function refreshBtnPagar(){
	if( _arreglo_json.length == 0 )
		$('#btnPagar').hide();
	else
		$('#btnPagar').show();
}

function pagar(){
	
	var datos = {"obs" : $('#obs').val(),"fec" : $('#fec').val(),"id_mat" : $('#id_mat').val(), "id_suc": $('#id_suc').val(), "tipo":"I", detalle:_arreglo_json  };
	var pago_total = 0;

	for (index in _arreglo_json) {
		var monto = _arreglo_json[index].monto;
		pago_total = pago_total + parseFloat(monto);
	}
	
	swal(
			{
				title : "Esta seguro?",
				text : "De realizar el pago total del recibo:" + $('#nro_rec').val() + ", monto total de S/." + $.number( pago_total, 2) ,
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText : "Si, Pagar",
				cancelButtonText : "No, cancelar!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					
					 $.blockUI({ 
							message: '<i class="icon-spinner4 spinner"></i>',
							//timeout: 2000, //unblock after 2 seconds
							overlayCSS: {
								backgroundColor: '#1b2024',
								opacity: 0.8,
								zIndex: 1200,
								cursor: 'wait'
							},
							css: {
								border: 0,
								color: '#fff',
								padding: 0,
								zIndex: 1201,
								backgroundColor: 'transparent'
							}
						});
					 
						_post_json('api/movimiento/ingreso',datos, function(data){
							
							if(pagoCodigoBarras(_arreglo_json)){
								imprimirCodigoBarras();
							}
							
							if(pagoAccesoInranet(_arreglo_json)){
								imprimirClaveIntranets();
							}
							/*
								swal({
						            html:true,
						            title: "<font color='black'>Transaccion existosa!</font>",
						            text: "Por favor <a href='#' onclick='imprimirCodigoBarras(event)'>imprima el codigo de barras</a>",
						            confirmButtonColor: "#66BB6A",
						            type: "success"
						        });

							*/
							 
							
							_post_json( "http://localhost:8081/api/print", data.result, 
									function(data){
										console.log(data);
										$(".modal .close").click();
										movimiento_listar_tabla();
									}
							); 
							
							
						});				 
					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha realizado ningun pago",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
				}
			});
	
	

}

function pdf(e,o){
	e.preventDefault();
	var id_fmo = $(o).data('id_fmo');
	//btn pdf boleta
	//document.location.href=_URL + "api/movimiento/pdf/boleta/" + id_fmo;
	document.location.href=_URL + "api/archivo/pdf/boleta/" + id_fmo+"/"+_id_alu;

}

function pagoCodigoBarras(_arreglo_json){
	for(var i in _arreglo_json){
		if ( _arreglo_json[i].id_fco == _CONCEPTO_CODIGO_BARRAS)
			return true;
	}
	
	return false;
}

function pagoAccesoInranet(_arreglo_json){
	for(var i in _arreglo_json){
		if ( _arreglo_json[i].id_fco == _ACCESO_INTRANET)
			return true;
	}
	
	return false;
}

function imprimirCodigoBarras(){
	//e.preventDefault();
	
	//document.location.href=_URL + "api/codigoBarras/alumno/" + $('#id_mat').val();
/*
	$.ajax({
		  url: _URL + "api/codigoBarras/alumno/" + $('#id_mat').val(),
		  success: function(data) {
		    var blob=new Blob([data]);
		    var link=document.createElement('a');
		    link.href=window.URL.createObjectURL(blob);
		    link.download="codigo_barras.pdf";
		    link.click();
		  }
		});
	
	*/
	/*
	$.ajax({
		  url: _URL + "api/codigoBarras/alumno/" + $('#id_mat').val(),
		  success: download.bind(true, "codigo_barras2.pdf", "<FILE_MIME_TYPE>")
		});
	*/
    window.open(_URL + "api/archivo/alumno/" + $('#id_mat').val(), '_blank');

}

function imprimirClaveIntranets(){

    window.open(_URL + "api/codigoBarras/accesoIntranet/" + $('#id_mat').val(), '_blank');

}