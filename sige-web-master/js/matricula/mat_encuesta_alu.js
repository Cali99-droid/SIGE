//Se ejecuta cuando la pagina q lo llama le envia parametros

var _btnSiguienteHabil = false;
var _data;
var _cant_pre;
var _alu_sele;
var _msj_fin;
//false: cuando el usuario ya jalo los datos y puede cambiar los datos del combo
function onloadPage(params) {
	console.log(params);
	_get('api/encuesta/'+params.id_enc,
				function(data){
					console.log(data);
				//$('#msj_ini').text(data.msj_ini);
				var div = document.getElementById("div_inicio");
				div.innerHTML = data.msj_ini;
				_msj_fin = data.msj_fin;
				$("#titulo_encuesta").text(data.nom);
				//$("#div_inicio").write(data.msj_ini);
				
	});
	console.log(_usuario);
	listar_hermanos();
	
}
	

function mostrarEncuesta(id_alu,id_mat, id_enc,alumno, nivel, sucursal, grado){
	_alu_sele=id_alu;
	$("#lblNivel").text(nivel);
	$("#lblGrado").text(grado);
	$("#lblLocal").text(sucursal);
	$("#lblAlumno").text(alumno);
	$("#id_enc").val(id_enc);
	$("#id_mat").val(id_mat);
	form.steps("next");
	//Cargas las preguntas con sus alternativas
	//$("#tabla #tbody").html('');
	$("#tabla > tbody").html("");
	var param = {id_enc:id_enc};
	_get('api/encuesta/listarPreguntasAlt', function(data) {
		
		if(data.length>0){
							var cant_pre=data.length;
							_cant_pre= cant_pre;
								console.log(cant_pre);
				for(i=0; i<cant_pre; i++){
					//var ts = '<ul>';
					var ts = '<ul><li><label class="control-label" style="font-size: 10pt; font-weight: bold;">'+data[i].pre+'</label><span class="text-danger">(*)</span><input type="hidden" id="id_pre'+data[i].id+'" name="id_pre" value="'+data[i].id+'"></input></li></ul>'; 
					var s = $('<select/>');
					//if (sug_au_id==null)
					///	sug_au_id = '';
					
					$('<option />', {value: '', text: 'Seleccione'}).appendTo(s);
					var alternativas=data[i].encuestaAlts;
						if(alternativas){
							var cant_alt=alternativas.length;
							for(j=0; j<cant_alt; j++){
								if(id_enc==1){ // Regla solo para la encuesta 1
								if(data[i].id==19){
									if(alternativas[j].alt!=sucursal){
										s.append('<option value="'+ alternativas[j].id + '" style="background-color: #BDDFF7">' + alternativas[j].alt + '</option>');
									}	
									
								} else {
									s.append('<option value="'+ alternativas[j].id + '" style="background-color: #BDDFF7">' + alternativas[j].alt + '</option>');
								}	
								} else {
									s.append('<option value="'+ alternativas[j].id + '" style="background-color: #BDDFF7">' + alternativas[j].alt + '</option>');
								}	
								
							}
						}					 
				 

					// var td= '<select class="form-control select-search" data-sug_id="' + sug_id+ '"  data-sug_au_id="' + sug_au_id + '" data-id_mat="' + id_mat + '" onchange="onchangeCombo(this)">' +  s.html() + '</select>';
					  var td= '<tr><td id="td_'+data[i].id+'" >'+ts+'<select class="form-control select-search alternativas"  data-id_pre="'+data[i].id+'" id="id_alt_'+data[i].id+'" name="id_alt" onchange="onchangeCombo(this)" required style="display: block;">' +  s.html() + '</select></td></tr>';
					// document.getElementById('tabla').getElementsByTagName('tbody').html('');
					 var theBody = document.getElementById('tabla').getElementsByTagName('tbody')[0];
					  //theBody.html('');
					  theBody.insertAdjacentHTML('beforeend', td);
				}	
		}
$('#td_19').css('display','none');
	$('#id_alt_19').css('display','none');		
	}, param);	
	
	
}

function grabarEncuesta(){
	var validation = $('#frm-encuesta').validate();
	if ($('#frm-encuesta').valid()){
		var cant_respuestas=0;
		var cant_preguntas=0;
		$(".alternativas").each(function (index, value) {
			console.log($(this).html());
				console.log($(this).val());
				
				if ($(this).val()!=''){
					cant_respuestas = cant_respuestas + 1;
				}	
				/*} else{
					
				}	*/
		});
		
		
		$(".alternativas").each(function (index, value) {
			if($(this).css('display') == 'block'){
			cant_preguntas = cant_preguntas + 1;
			}
		});	
		console.log(cant_preguntas);
		console.log(cant_respuestas);
		if(cant_respuestas==cant_preguntas){
		//	if($("[name='id_alt']").selectedIndex!=-''){
			_POST({form:$('#frm-encuesta'),
			  context:_URL,
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
					form.steps("previous");
					listar_hermanos();
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
				 $('#btn-grabar_matricula').attr('disabled','disabled');
				 /*$('#alu_alumno-frm #btn-grabar').removeAttr('disabled');
				 $('#nro_dni').focus();*/
			}
			}); 
		} else {
			swal({
					  title: "IMPORTANTE",
					  html:false,
					  type : "warning",
					  text: "Por favor responder todas las preguntas.",
					  icon: "info",
					  button: "Cerrar",
					});
		}	
		
	}	
	
							

}

function onchangeCombo( field){
	var combo = $(field);
	var id_alt = combo.val();
	var id_pre = combo.data('id_pre');
	console.log(id_alt);
	console.log(id_pre);
	//var sug_id = combo.data('sug_id');
	//var sug_au_id = combo.data('sug_au_id');//de la bd
	//Busco la lista de preguntas que dependen de la respuesta
	var param = {id_pre: id_pre};
	_get('api/encuesta/listaPreguntasDependencia', function(data) {
		var cant_pre_dep=data.length;
		if(cant_pre_dep>0){
			for(j=0; j<cant_pre_dep; j++){
			var id_pre_depende = data[j].id_enc_pre;
			var id_alt_depende = data[j].id_alt;
			console.log(id_alt);
			console.log(id_alt_depende);
			if(id_alt_depende==id_alt){
				//$('#id_pre_'+id_pre_depende).css('display','block');
				$('#td_'+id_pre_depende).css('display','block');
				$('#id_alt_'+id_pre_depende).css('display','block');
			} else {	
				//$('#id_pre_'+id_pre_depende).css('display','none');
				$('#td_'+id_pre_depende).css('display','none');
				$('#id_alt_'+id_pre_depende).css('display','none');
			}
				
			}	
		}	
		
	}, param);	

}	

function listar_hermanos(){
	var param={id_usr:_usuario.id , id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/alumno/listarHijosxUsuario/',
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
				            	   return row.alumno;
				            }} ,
							{"title":"Acciones", "render": function ( data, type, row ) {
								if(row.id_enc_alu!=null){
									//_cant_alu_mat = _cant_alu_mat+1;
									return '<button type="button" class="btn btn-success btn-xs" title="Encuesta Completada"><i class="icon-clipboard5"></i></button>';
								}
								else{
									return '<button onclick="mostrarEncuesta(' + row.id_alu +',' + row.id_mat+',1,\''+row.alumno+'\',\''+row.niv_sig+'\',\''+row.sucursal+'\',\''+row.gra_sig+'\')" type="button" title="Llenar Encuesta" class="btn btn-warning btn-xs"><i class="icon-pencil3"></i> Seleccionar</button>';
								}
								
							}}
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