/** Se ejeuta al ser llamado desde otra pagina * */
function onloadPage(_params) {
	//id_alu, id_matr_vac:id_matr_vac
	var id_alu= _params.id_alu;
	var id_matr_vac = _params.id_matr_vac;
	var id_ecn = _params.id_ecn;
	$("#admi_evaluacion_psicologica-frm #id_alu").val(id_alu);
	$("#admi_evaluacion_psicologica-frm #id_mat_vac").val(id_matr_vac);

	var param ={id_alu: id_alu};
	_GET({ url: 'api/alumno/datosAlumnoEvaPsico/',
	   context: _URL,
	   params:param,
	   success:
		function(data){
		  // alert('Se grabo correctamente');
		   $("#apeNom").text(data.alumno);
		   $("#fecNac").text(data.fec_nac);
		   $("#edad").text(data.edad);
		   $("#grado").text(data.grado);
		   $("#fecha").text(data.fecha);
		}
	});
	
	//$('#frm_registro_participante #id_niv').on('change',function(event){
	
	
	var param1 ={id_mat:id_matr_vac};
	_GET({url:'api/criterioNota/listarPreguntasRespuestas', 
		context:_URL,
		params: param1,
		success:function(data){
			console.log(data);
		$('#preguntas_table').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 50,
			 select: true,
			 bLengthChange: false,
			 bPaginate: false,
			 bFilter: false,
			 searching: false, 
			 info: false, 
	         columns : [ 
	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	           {"title":"Pregunta", "data" : "pre"},
	           {"title":"Alternativa", "render":function ( data, type, row,meta ) {	        		   
	        	   var combo="<input  type='hidden' id='preg' name='preg' value='"+row.id+"' /><select id='id_alt' name='id_alt' required placeholder='Seleccione' class='form-control combos'  onChange='javascript:sumar(this)'>" ;
	        	   var options='<option value="-1">Seleccione</option>';
	        	   var cant_alt=row.criterioAlternativas;
	        	   for (var i=0;i<cant_alt.length;i++) {
	        		   console.log(cant_alt.length);
	        		   console.log('aqui');
						var id = cant_alt[i].id;
						var text = cant_alt[i].alt;
						var punt = cant_alt[i].punt;
						//alert(row.id_alt);
						//alert(cant_alt[i].id);
						var selected = (row.id_alt==cant_alt[i].id) ? ' selected ': ' ';
							options=options+'<option value="'+ id +'"'+selected+' punt='+punt+' class="punt">' +text + '</option>';
					}
	        	   combo=combo+options;
	        	   combo=combo+"</select>";
	        	   console.log(combo);
					return combo;
       	 		} }
	         ]
	    });
	}
	});
	
	_GET({url:'api/criterioNota/listarInstrumentosEvaluacion', 
		context:_URL,
		params: param1,
		success:function(data){
			console.log(data);		
		$('#instrumentos_table').dataTable({
			 data : data,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 50,
			 select: true,
			 bLengthChange: false,
			 bPaginate: false,
			 bFilter: false,
			 searching: false, 
			 info: false, 
	         columns : [ 
	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	           {"title":"Instrumento", "data" : "nom"}
	         ]
	    });
	}
	});
	
	if(id_ecn!=null){
		_get('api/criterioNota/' + id_ecn,
				function(data){
					console.log(data);
					_fillForm(data,$('#admi_evaluacion_psicologica-frm'));
					$("#num").val(data.num);
					$("#num").css({'color':'black','font-size':'1.5em'});
					$("#informe").css({'color':'black','font-size':'1.5em'});
					if(data.apto=='Y')
						$('#apto1').attr("checked", "checked");
					else if(data.apto=='N')
						$('#apto2').attr("checked", "checked");
		});
	} else{
		_get('api/criterioNota/obtenerNumero',
				function(data){
					console.log(data);
					$("#num").val(data.numero);
					$("#informe").css({'color':'black','font-size':'1.5em'});
					$("#num").css({'color':'black','font-size':'1.5em'});
		});
	}
	
}
	
function sumar(obj){
	var puntajeTotal = 0;
//alert();
	$( ".punt" ).each(function(  ) {
		var obj = $( this );
		//console.log(obj);
		if(obj.is(':selected')){
			//alert(obj.attr("monto"));
			var puntaje = parseFloat(obj.attr("punt"));
			puntajeTotal = puntajeTotal + puntaje;
			//aca teienes q asignar valor a la caja de texto
			$("#puntaje").val(puntajeTotal); //algo asi
			//alert(puntajeTotal);
		}

});
}

$('#admi_evaluacion_psicologica-frm #btn-grabar').on('click', function(event){	
	//var validator = $("#alu_familiar-frm").validate();
	var incompletos = false;
	 $('.combos').each(function() {
		    if ($(this).val()== '-1') {
		      incompletos = true; // AQUI modificamos la variable
		    }
	});
	if($("[name='apto']:checked").length==0){
		alert("Seleccione la condición Final!");
	} else if(incompletos==true){
		alert('Seleccione todas las repuestas de todas las preguntas, porfavor!!')
	} else if ($("#admi_evaluacion_psicologica-frm").valid()) {
	//	$("#vacante-form #id_gpf").val($('#_id_gpf').val());
		 $('#admi_evaluacion_psicologica-frm #btn-grabar').attr('disabled','disabled');
		_POST({form:$('#admi_evaluacion_psicologica-frm'),
			  context: _URL,
			  msg_type:'notification',
			  msg_exito : 'SE INSCRIBIO CORRECTAMENTE AL ALUMNO.',
			  success:function(data){
				    //DESCARGAR 
				  //id_gpf=2490, id_per=null, id=5010, error=null
				  $("#admi_evaluacion_psicologica-frm #id").val(data.result);
				  _send('pages/admision/admi_busqueda_eva_psicologica.html','Búsqueda de Evaluaciones','Listado de Alumnos');

				 // listar_inscripciones(data.id_alu);
			 }, 
			 error:function(data){
				 console.log(data);
				 _alert_error(data.msg);
				 $('#admi_evaluacion_psicologica-frm #btn-grabar').removeAttr('disabled');
			}
		}); 
	}
	return false;

});

