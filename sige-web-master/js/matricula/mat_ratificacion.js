//Se ejecuta cuando la pagina q lo llama le envia parametros

var _btnSiguienteHabil = false;
var _data;
var _cant_pre;
var _alu_sele;
var _msj_fin;
var _params;
//false: cuando el usuario ya jalo los datos y puede cambiar los datos del combo
function onloadPage(params) {
	console.log(params);
	_params=params;
	$("#id_anio_rat").val(params.id_anio_rat);
	//Obtener el nombre del año
	_get('api/anio/'+params.id_anio_rat,
				function(data){
					console.log(data);
					$("#nom_rat").text('RATIFICACIÓN DE MATRÍCULA PARA EL AÑO '+data.nom);
				//$('#msj_ini').text(data.msj_ini);
				//var div = document.getElementById("div_inicio");
				//div.innerHTML = data.msj_ini;
				//_msj_fin = data.msj_fin;
				//$("#titulo_encuesta").text(data.nom);
				//$("#div_inicio").write(data.msj_ini);
				
	});
	console.log(_usuario);
	listar_hermanos();
	
}
	


function grabarRatificacion(){
	var validation = $('#frm-ratificacion').validate();
	if ($('#frm-ratificacion').valid()){
		var cant_respuestas=0;
		var cant_hijos=0;
		//var cant_preguntas=0;
		/*$(".alternativas").each(function (index, value) {
			console.log($(this).html());
				console.log($(this).val());
				
				if ($(this).val()!=''){
					cant_respuestas = cant_respuestas + 1;
				}	
				/*} else{
					
				}	*/
	//	});
	
		$(".id_matricula").each(function (index, value) {
			
				console.log($(this).val());
				
				if ($(this).val()!=''){
					cant_hijos = cant_hijos + 1;
				}	
				
		});
		
		$('input[class=res]:radio').each(function(){
			 if( $(this).is(':checked') ){
			//if ($(this).val()!=''){
				cant_respuestas = cant_respuestas + 1;	
			}			
		});
		
		console.log(cant_respuestas);
		console.log(cant_hijos);
		if(cant_respuestas==cant_hijos){
			var json_ratificacion={};
			var id_mats = [];
			$(".id_matricula").each(function (index, value) {
				//var id_mat_alu={};
				//id_mat_alu.id_mat=$(this).val();
				//id_mats.push(id_mat_alu);
				id_mats.push($(this).val());
			});
			var respuestas =[];
			$('input[class=res]:radio').each(function(){
				 if( $(this).is(':checked') ){
					var respuesta={};
					//respuesta.res=$(this).val();
					//respuestas.push(respuesta);
					respuestas.push($(this).val());
				 }
			});	
			json_ratificacion.id_mats_alu=id_mats;
			json_ratificacion.resp_alu=respuestas;
			json_ratificacion.id_anio=$("#id_anio_rat").val();
		//	if($("[name='id_alt']").selectedIndex!=-''){
			_POST({
			//form:$('#frm-ratificacion'),
			  url:'api/matricula/grabarRatificacion',
			  context:_URL,
			  params:JSON.stringify(json_ratificacion),
			  contentType:"application/json",
			  msg_type:'notification',
			  msg_exito : 'Se grabo la encuesta correctamente',
			  success:function(data){
				  swal({
					  title: "COMPLETADO",
					  html:true,
					  type : "success",
					  //text: "<p>¡Felicidades!, se ha realizado una <b>PRE MATRÍCULA</b>, recuerde el plazo para completar el proceso es hasta el <font color='green'>"+data.result.dia+" de "+data.result.mes+" del "+data.result.anio+"</font>, el pago lo puede realizar en el "+data.result.banco+" o en Secretaría Jr Huaylas Nro 245 en el horario de <b>8:00am a 3:00pm.</b></p>",
					   text: _msj_fin,
					  icon: "info",
					 // confirmButtonColor : "rgb(33, 150, 243)",
					  //cancelButtonColor : "#EF5350",
					  //confirmButtonText : "OK",
					 button: "Cerrar",
					});
				//Enviar Correo
				//_post_silent('api/familiar/enviarCorreoRatificacion/'+ id_mats ,null,
				/*function(data){
					
				});*/
					listar_hermanos();
					_send('pages/apoderado/apo_menu_matricula_web.html','','Lista');
					//$('#frm-matricula #id').val(data.result.id_mat);
					//$('#btn-grabar_matricula').removeAttr('disabled');
					//$('#id_per_res').attr('disabled','disabled');
					//$('#id_bco').attr('disabled','disabled');
					//form.steps("next");
					//
						
					//_send('pages/apoderado/apo_menu_matricula_web.html','Matrícula Web','Matrícula Web');
					//
			 }, 
			 error:function(data){
				 _alert_error(data.msg);
				 $('#btn-grabar_ratificacion').attr('disabled','disabled');
				 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
				 $('#nro_dni').focus();*/
			}
			}); 
		} else {
			swal({
					  title: "IMPORTANTE",
					  html:false,
					  type : "warning",
					  text: "Por favor seleccionar la respuesta para todos sus hijos.",
					  icon: "info",
					  button: "Cerrar",
					});
		}	
		
	}	
	
							

}



function listar_hermanos(){
	var param={id_usr:_usuario.id , id_anio:_params.id_anio_mat};
	_GET({ url: 'api/alumno/listarHijosxUsuarioRatificacion/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			 //  _cant_padres=data.length;
			console.log(data);	
			   _cant_mat=data.length;
				$('#hermanos-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 searching: false, 
					 paging: false, 
					 info: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							//{"title":"Nro. Documento", "data" : "nro_doc"}, 
							//{"title":"Apellidos y Nombres", "data" : "alumno"}, 
							{"title":"Apellidos y Nombres", "render": function ( data, type, row ) {
				            	   return '<input type="hidden" class="id_matricula" name="id_mat" id="id_mat" value=' + row.id_mat + '>'+row.alumno;
				            }} ,
							{"title":"Nivel - Grado Sgte.", "render": function ( data, type, row ) {
				            	   return row.niv_sig+" - "+row.gra_sig;
				            }} ,
							{"title":"Seleccione", "render": function ( data, type, row ) {
								if(row.id_rat!=null){
									$('#btn-grabar_ratificacion').attr('disabled','disabled');
									if(row.res==1)
									  return 'SI';
									else if(row.res==0)
									  return 'NO';	
								} else {
									$('#btn-grabar_ratificacion').removeAttr('disabled');
									return '<div class=" col-sm-3"><label>Continuará en el colegio?</label></div><div class=" col-sm-3"><label class="radio-inline"><input type="radio" id="res'+row.id_mat+'" class="res" name="res'+row.id_mat+'" data-id_mat=' + row.id_mat + ' value="1" ><label class="form-check-label text-bold text-primary-800" for="exampleCheck1">SI</label></label></div><div class=" col-sm-3"><label class="radio-inline"><input type="radio" id="res'+row.id_mat+'"  class="res" name="res'+row.id_mat+'" data-id_mat=' + row.id_mat + ' value="0"><label class="form-check-label text-bold text-primary-800" for="exampleCheck1">NO</label></label></div>';
								}
				            	  
				            }} 
							/*{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.id_enc_alu!=null){
									//_cant_alu_mat = _cant_alu_mat+1;
									return '<button type="button" class="btn btn-success btn-xs" title="Encuesta Completada"><i class="icon-clipboard5"></i></button>';
								}
								else{
									return '<button onclick="mostrarEncuesta(' + row.id_alu +',' + row.id_mat+',1,\''+row.alumno+'\',\''+row.niv_sig+'\',\''+row.sucursal+'\',\''+row.gra_sig+'\')" type="button" title="Llenar Encuesta" class="btn btn-warning btn-xs"><i class="icon-pencil3"></i> Seleccionar</button>';
								}
								
							}}*/
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
					 }
			    });
			}
		});
	//console.log(_cant_alu_mat);
}

var form = $(".steps-validation").show();


$(".steps-validation").steps(
				{	headerTag : "h6",
					bodyTag : "fieldset",
					transitionEffect : "fade",
					titleTemplate : '<span class="number">#index#</span> #title#',
					autoFocus : true,
					onStepChanging : function(event, currentIndex, newIndex) {

						if (currentIndex == 0 ) {
							//alert(1);
							form.find('a[href$="next"]').hide();				
						}

						if (currentIndex == 1 && newIndex == 2) {
							//alert(2);
							//form.find('a[href$="next"]').hide();
							//$("#id_mat").val('');
							//console.log(_alu_sele);
							//form.find('a[href$="next"]').hide();
							//$('a[href$="next"]').hasClass('disabled');
							/*if(_alu_sele!=''){
								return true;
							} else {
								alert('Seleccione un alumno por favor.')
								return false;
							}*/	
						}

						// Forbid next action on "Warning" step if the user is
						// to young
						if (newIndex === 3 && Number($("#age-2").val()) < 18) {
							console.log('AAA');
							return false;
							
						}
						//Regla de Negocio para verificar si la actualizacion de datos es obligatorio
							//_get('api/reglasNegocio/pagoObligatorio',
							//	function(data){
							//	if(data.val=="1"){
						if (currentIndex == 2 && newIndex == 3) {
									
						}											
								//}
						
						//});

						// Needed in some cases if the user went back (clean up)
						if (currentIndex < newIndex) {
							console.log('BBB');
							// To remove error styles
							form.find(".body:eq(" + newIndex + ") label.error")
									.remove();
							form.find(".body:eq(" + newIndex + ") .error")
									.removeClass("error");
						}

						form.validate().settings.ignore = ":disabled,:hidden";
						return form.valid();
					},

					onStepChanged : function(event, currentIndex, priorIndex) {

						//TAB DE PAGOS
						/*if (currentIndex === 1) {
							alert(3);
							//form.find('a[href$="next"]').show()
						}*/
						if (currentIndex === 0 && priorIndex === 1) {
							form.find('a[href$="next"]').show();
							
						}
						if (currentIndex === 1 && priorIndex === 2) {
							//alert(4);
							form.find('a[href$="next"]').hide();
						}
					},

					onFinishing : function(event, currentIndex) {
						console.log('EEE');
						form.validate().settings.ignore = ":disabled";
						return form.valid();
					},

					onFinished : function(event, currentIndex) {
						//alert("Submitted!");
						grabarEncuesta();
					}
				});

$('a[href$="next"]').text('Siguiente >>');
$('a[href$="previous"]').text('<< Anterior ');
$('a[href$="finish"]').text('<< Finalizar ');