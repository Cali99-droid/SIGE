//Se ejecuta cuando la pagina q lo llama le envia parametros
var _arreglo_json;
function onloadPage(params){
	_inputs('frm-traslado');
	console.log(params);
	$('#lbl_alumno').text(params.alumno);
	$("#frm-traslado #id_mat").val(params.id_mat);
	var id_sit=5;
	//_llenarCombo('cat_col_situacion', $('#id_sit'),id_sit,null, null);
	_llenarCombo('cat_col_situacion',$('#id_sit'),5);
	$("#id_sit").prop('disabled', 'disabled');
	_llenarComboURL('api/colegio/listarcolegios',$('#id_col'));
	
	listarMesesDeuda(params.id_mat);
	$("#inicial").css('display','none');
	$("#primaria").css('display','none');
	$("#secundaria").css('display','none');
	//var total=$("#_th_total").val();
	//$("#total").val(total);
	console.log(params.nivel);
	if(params.nivel=='INICIAL')
		$("#inicial").css('display','block');
	else if(params.nivel=='PRIMARIA')
		$("#primaria").css('display','block');
	else if(params.nivel=='SECUNDARIA')
		$("#secundaria").css('display','block');
	
	_get('api/matricula/datosApoderado/'+params.id_mat,function(data){
		$("#apoderado\\.nom").val(data.nombres);
		$("#apoderado\\.nro_doc").val(data.nro_doc);
		$("#apoderado\\.cel").val(data.celular);
		$("#apoderado\\.direccion").val(data.direccion);
	});	
	
	$('#abono').on('keyup', function(e) {
		var total = parseFloat($('#total').val());
		var abono = parseFloat($(this).val());
		var vuelto = abono - total;
		$('#vuelto').val(vuelto);
	});
	
	var _tipo="I";
	_llenarCombo('fac_concepto',$('#id_fco'), null, _tipo, function(){
		$('#id_fco').change();
	});	
	
	_get('api/common/fecha',function(data){
		console.log(data);		
		$('#fec').val(data);
		$('#fecha').val(data);
		});
	
	$('input[type=checkbox]').on("click", function(){ 
		var monto_doc=0;
		$("input[type=checkbox]:checked").each(function(){
			monto_doc= monto_doc + parseFloat($(this).attr('monto'));
	    });
		$("#total_doc").val(_formatMonto(monto_doc));
		var monto_mens=parseFloat($("#total_mens").val());
		console.log(monto_doc);
		console.log(monto_mens);
		var monto_total=monto_mens+monto_doc;
		$("#total").val(_formatMonto(monto_total));
	}); 
	
	
}



function listarMesesDeuda(id_mat){
	//var monto_total=0;
	_get('api/situacionMat/obtenerMontoPago/'+id_mat,
			function(data){
			console.log(data);
				$('#table-mensualidades_adedua').dataTable({
					 data : data.meses_deuda,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 LengthChange: false,
					 bPaginate: false, 
					 bFilter: false,
					 bInfo : false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Mes", "data" : "mes"}, 
							{"title":"Monto", "data" : "monto"}
				    ],
				    "initComplete": function( settings ) {
				    	   _dataTable_total_sin_punto(settings,'monto');
						   _initCompleteDTSB(settings);
						   console.log($("#_th_total > b").html());
						   $("#total_mens").val(($("#_th_total > b").html()).substr(3));
					 }
			    });
			}
	);
		
	/*_get('api/situacionMat/obtenerMontoPago/'+id_mat,
			function(data){
			console.log(data);
			//$("#total").val(data);
			}
	);*/
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


var fnc_agregar_detalle = function(data){
	console.log(data);
	alert('antes de grabar');
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

$("#frm-traslado").submit(function () {
	var titulo;

		titulo = 'Ingrese los datos del pago a realizar';

	abrir_modal(titulo, null,null,fnc_agregar_detalle);
});






function abrir_modal(titulo,url_html,callBackOnShowModal,callBackSave){
	  try{
		 // $modal = $('#modal').modal('show');
		  
		  //$modal.find('.modal-body').load(url_html, function(){
			 // $modal.find('.modal-title').html(titulo);
			//  $modal.find('form').on('submit',function(event){
		    		event.preventDefault();

		    		var id_fco = $('#id_fco').val();
		    		var obs = $('#obs').val();
		    		var monto = $('#monto').val();
		    		var concepto = $('#id_fco option:selected').text();

		    		
		    		var row_json = {"concepto": concepto, "obs": obs, "monto":monto, "id_fco": id_fco  };
		    		callBackSave(row_json);

			//	});

			//  callBackOnShowModal();

		 // });
		  
		  
	  }catch(e){
		  console.error(e);
	  }
	
}



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

var form = $(".steps-validation").show();
//$('.select').select2();
//Initialize wizard
$(".steps-validation").steps({
  headerTag: "h6",
  bodyTag: "fieldset",
  transitionEffect: "fade",
  titleTemplate: '<span class="number">#index#</span> #title#',
  autoFocus: true,
  onStepChanging: function (event, currentIndex, newIndex) {

      // Allways allow previous action even if the current form is not valid!
      if (currentIndex > newIndex) {
          return true;
      }

      // Forbid next action on "Warning" step if the user is to young
      if (newIndex === 3 && currentIndex==2) {
    	  var vuelto = $('#vuelto').val();

			if ($('#abono').val() == "") {
				_alert_error('Debe ingresar el monto a pagar',
						function() {
							return false;
						});
				return false;
			}

			if (parseFloat(vuelto) < 0) {
				_alert_error(
						'Monto insuficiente para cubirar el traslado',
						function() {
							return false;
						});
				return false;
			}

			swal(
					{
						title : "Esta seguro?",
						text : "Se procedera a trasladar al alumno:"
								+ $("#label_alumno").text(),
						type : "warning",
						showCancelButton : true,
						confirmButtonColor : "rgb(33, 150, 243)",
						cancelButtonColor : "#EF5350",
						confirmButtonText : "Si, Trasladar",
						cancelButtonText : "No, cancela!!!",
						closeOnConfirm : false,
						closeOnCancel : false
					},
					function(isConfirm) {
						if (isConfirm) {

							$('#frm-traslado #id_suc').val(_usuario.id_suc);
							$('#id_sit').removeAttr('disabled');
							_post(
									"api/trasladoDetalle",
									$('#frm-traslado')
											.serialize(),
									function(data) {
										console.log(data);
										$("#id_sit").prop('disabled', 'disabled');
										_post_json(
												"http://localhost:8081/api/print",
												data.result,
												function(data) {

												});


										form.steps("next");
									});

						} else {
							swal({
								title : "Cancelado",
								text : "No se ha realizado el traslado",
								confirmButtonColor : "#2196F3",
								type : "error"
							});

							return false;
						}
					});

			return false;
      }

      // Needed in some cases if the user went back (clean up)
      if (currentIndex < newIndex) {

          // To remove error styles
          form.find(".body:eq(" + newIndex + ") label.error").remove();
          form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
      }

      form.validate().settings.ignore = ":disabled,:hidden";
      return form.valid();
  },

  onStepChanged: function (event, currentIndex, priorIndex) {
  	
      // Used to skip the "Warning" step if the user is old enough.
  	if (currentIndex === 1){
  		//onloadMatricula();
  	}
      if (currentIndex === 2 && Number($("#age-2").val()) >= 18) {
          form.steps("next");
      }

      // Used to skip the "Warning" step if the user is old enough and wants to the previous step.
      if (currentIndex === 2 && priorIndex === 3) {
          form.steps("previous");
      }
  },

  onFinishing: function (event, currentIndex) {
      form.validate().settings.ignore = ":disabled";
      return form.valid();
  },

  onFinished: function (event, currentIndex) {
      alert("Submitted!");
  }
});

//Initialize validation
$(".steps-validation").validate({
  ignore: 'input[type=hidden], .select2-search__field', // ignore hidden fields
  errorClass: 'validation-error-label',
  successClass: 'validation-valid-label',
  highlight: function(element, errorClass) {
      $(element).removeClass(errorClass);
  },
  unhighlight: function(element, errorClass) {
      $(element).removeClass(errorClass);
  },

  // Different components require proper error label placement
  errorPlacement: function(error, element) {

      // Styled checkboxes, radios, bootstrap switch
      if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container') ) {
          if(element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
              error.appendTo( element.parent().parent().parent().parent() );
          }
           else {
              error.appendTo( element.parent().parent().parent().parent().parent() );
          }
      }

      // Unstyled checkboxes, radios
      else if (element.parents('div').hasClass('checkbox') || element.parents('div').hasClass('radio')) {
          error.appendTo( element.parent().parent().parent() );
      }

      // Input with icons and Select2
      else if (element.parents('div').hasClass('has-feedback') || element.hasClass('select2-hidden-accessible')) {
          error.appendTo( element.parent() );
      }

      // Inline checkboxes, radios
      else if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
          error.appendTo( element.parent().parent() );
      }

      // Input group, styled file input
      else if (element.parent().hasClass('uploader') || element.parents().hasClass('input-group')) {
          error.appendTo( element.parent().parent() );
      }

      else {
          error.insertAfter(element);
      }
  },
  rules: {
      email: {
          email: true
      }
  }
});
