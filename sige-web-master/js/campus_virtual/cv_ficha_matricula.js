var alu_ant=null;
var alu_nue=null;

//var checks_con=null;

function matricular(){
	 console.log('view tcy' + $("[name='tc_view_tyc']:checked").length);
	 console.log('view con' + $("[name='tc_view_con']:checked").length);
	 var cant_hijos=$("[name='id_alu']:checked").length;
	// console.log(cant_hijos);
	// console.log( $("[name='tc_view_con']:checked").length + $("[name='tc_view_tyc']:checked").length +$("[name='tc_view_cos']:checked").length);
	 //var cant_cont=$("[name='tc_view_tyc']:checked").length;
	if($("[name='id_alu']:checked").length==0){
		swal({
			  title: "IMPORTANTE",
			  text: "Debe seleccionar por lo menos un alumno.",
			  icon: "info",
			  button: "Cerrar",
			});
		//alert('Seleccione al menos un alumno!!');
	} else if ( $("[name='tc_view_con']:checked").length + $("[name='tc_view_tyc']:checked").length +$("[name='tc_view_cos']:checked").length != cant_hijos+2 ){
		
		
		swal({
			  title: "IMPORTANTE",
			  html:true,
			  text: "Para continuar es necesario abrir y leer los archivos: <li>Términos y condiciones</li> <li>Contrato de Servicios</li><li>Costos Fijos y Variables minimos del Servicio Educativo (DL NRO. 1476)</li>",
			  icon: "info",
			  button: "Cerrar",
			});
		//alert('Para continuar es necesario abrir y leer los archivos: Tacute;rminos y condiciones</li> <li>Contrato de Servicios</li>');
		
	}  else if($("[name='tc_acept']:checked").length==0){
		swal({
			  title: "IMPORTANTE",
			  html:true,
			  text: "Para continuar es necesario aceptar los términos y condiciones.",
			  icon: "info",
			  button: "Cerrar",
			});
		//alert('Seleccione su respuesta de los términos y condiciones');
	} else {
		//_post($('#frm-ficha_matricula').attr('action') , $('#frm-ficha_matricula').serialize());
		console.log('matriculado');
		_POST({
			context:_URL,
			url:'api/inscripcionCampus',
			params:$('#frm-ficha_matricula').serialize(),
			msg_type:'notification',
			msg_exito : 'SE REALIZÓ CORRECTAMENTE LA MATRÍCULA.',
			//contentType:"application/json",
			success: function(data){
				console.log(data);
				var id_fam = $("#frm-ficha_matricula #id_fam").val();	
				_POST({url:'/api/familiar/enviarAceptacinMatricula/' + id_fam,context:_URL});
				$(".modal .close").click();
				_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
			}
		});	
	}
 }
 

 /*generar_costo(){
		document.location.href=_URL + "api/reserva/generarCosto/" + $( '#frm-reserva #id' ).val();		 
});*/
var onShowModal = function(){
	//alert('3-onShowModal');
	
	console.log('onShowModal!!!');
	$("#div_no_acepta_tc").css('display','none');
	$("#frm-ficha_matricula #id_fam").val(_usuario.id);
	$("#frm-ficha_matricula #id_anio").val($('#_id_anio').text());
	$("#frm-ficha_matricula #id_gpf").val($('#_id_anio').text());
	
	$('#frm-ficha_matricula #btn_matricular').on('click',function(event){
		//alert('matricular!!');
		event.preventDefault();
	});
	
	
	/*if($("#frm-ficha_matricula input[name='id_alu']").is(':checked')){
		$("input:radio[name='tc_acept']").attr("disabled",false);
		$('#btn-matricular').removeAttr('disabled');
	} 
	*/
	$("#btn_matricular").attr('disabled','disabled');
	$("input:checkbox[name='tc_acept']").on("click", function(e) {
		if($("#frm-ficha_matricula input[name='tc_acept']").is(':checked')){
			$("#frm-ficha_matricula #tc_acept").val(1);
			$("#btn_matricular").removeAttr('disabled');
		} else{
			$("#frm-ficha_matricula #tc_acept").val(0);
			$("#btn_matricular").attr('disabled','disabled');
		}
	});
	
	
}
//funcion q se ejecuta despues de grabar
var onSuccessSave = function(data){

}
function ficha_modal(link){
	//alert('1-modal');
	//funcion q se ejecuta al cargar el modal
	
	
	//Abrir el modal
	var titulo= 'FICHA DE INSCRIPCI&Oacute;N';
	//alert('2-_modal_full');
	_modal_full(titulo,'pages/campus_virtual/cv_ficha_matricula.html',onShowModal,onSuccessSave);
	
	var param = {};
	param.id_fam = _usuario.id;
	param.id_anio= $('#_id_anio').text();
	
	_get('api/familiar/hijos/',
			function(data){
			console.log(data.length);
			for (var i=0;i< data.length; i++) {
				var arrFec_matr = data[i].fecha.split('-');
				var arrFec_matr_nuevas = data[i].fec_conf_nue_matr.split('-');
				var fecha_matricula=arrFec_matr[2] +'/' + arrFec_matr[1] + '/' + arrFec_matr[0];
				var fecha_conf_matr=arrFec_matr_nuevas[2] +'/' + arrFec_matr_nuevas[1] + '/' + arrFec_matr_nuevas[0];
				console.log('fecha_mat'+fecha_matricula);
				console.log('fecha_mat_nue'+fecha_conf_matr);
				var links=null;
				if(Date.parse(fecha_matricula)>=Date.parse(fecha_conf_matr)){
					console.log('nuevo');
					links='<a href="http://ae.edu.pe:8080/documentos/AN_Contrato_Servicios_Virtuales.pdf" onclick="document.getElementById(\'tc_view_con'+data[i].id+'\').checked=true;" target="_blank">Contrato servicios virtuales</a><input id="tc_view_con'+data[i].id+'" style="display:none" name="tc_view_con" id="tc_view_con" type="checkbox"></a>';
				} else{
					console.log('antiguo');
					links='<a href="http://ae.edu.pe:8080/documentos/AA_Contrato_Servicios_Virtuales.pdf" onclick="document.getElementById(\'tc_view_con'+data[i].id+'\').checked=true;" target="_blank">Contrato servicios virtuales</a><input id="tc_view_con'+data[i].id+'" style="display:none" name="tc_view_con" id="tc_view_con" type="checkbox"></a>';
				}
				//$("#hijos").append(links);
				var alumno=data[i].nom;
				if(data[i].tc_acept==null || data[i].tc_acept=='0'){
					var div="<p><input type='checkbox' id='id_alu'"+data[i].id+" name='id_alu' value="+data[i].id+" />"+alumno+" /  "+data[i].nivel+" - "+data[i].grado+" / "+links+"</p>";
					$("#hijos").append(div);
				}
				
			}
			$('#frm-ficha_matricula #id_gpf').val(data[0].id_gpf);
			}, param
	);

}
