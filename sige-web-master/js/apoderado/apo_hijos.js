//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	//list de alumnos
	var param = {};
	//param.id_fam = _usuario.id;
	param.id_usr = _usuario.id;
	param.id_anio= $('#_id_anio').text();
	_get('api/familiar/hijos/',
			function(data){
			console.log(data);
			var lista = $('#alumnos-list');
			var linea = lista.html();
			for ( var i=0; i<data.length-1;i++){
				var alumno = data[i];
				
				lista.children().eq(i).find('nom').html(alumno.nom);
				
				lista.append(linea);
			}
			
			var i=0;
			lista.children().each(function () {
				var alumno = data[i];
				$(this).find('#nom').html(alumno.nom);
				$(this).find('#sucursal').html(alumno.sucursal);
				$(this).find('#nivel').html(alumno.nivel);
				$(this).find('#grado').html(alumno.grado);
				$(this).find('#seccion').html(alumno.seccion);
				$(this).find('button').data('id_mat',alumno.id_mat);
				$(this).find('button').data('alumno',alumno);
				$(this).find('button').data('tc_acept',alumno.tc_acept);
				//console.log(_URL + 'api/alumno/foto/');
				$(this).find('#foto').attr('src',_URL + 'api/alumno/foto/' + alumno.id);
				
				
	
				var btnNotas =$(this).find('#btnNotas');
				var btnAgenda =$(this).find('#btnAgenda');
				var btnAsistencia =$(this).find('#btnAsistencia');
				var btnCampusVirtual=$(this).find('#btnCampusVirtual');
				if(alumno.tc_acept!=null && alumno.tc_acept=='1'){
					btnCampusVirtual.prop('disabled', false); //por ahora comentado
					$(this).find("#div_accesos").css('display','block'); //por ahora no mostrar sus accesos
					$(this).find("#usuario").text(alumno.usuario_campus);
					$(this).find("#psw").text(alumno.psw_campus);
					$(this).find('#link_restablecer').attr('usuario',alumno.usuario_campus);
				} else{
					btnCampusVirtual.removeAttr("disabled");
					btnCampusVirtual.css({'color':'black','font-size':'1.3em','background':'yellow'});
					//btnCampusVirtual.class('button2');
					btnCampusVirtual.html('Matriculate <br>al Campus'),
					$(this).find("#div_accesos").css('display','none');
				}
				
				btnNotas.prop('disabled', true);
				//Por ahora congelado los pagos
				/*_get('api/pagos/pagosAtrasados/' + alumno.id_mat, function (data){
					if(data.length==0){//SIGNIFICA QUE NO TIENE PAGOS ATRASADOS
						
						btnNotas.prop('disabled', false);
						btnNotas.on('click',function() {notasPromedio($(this))});
					} else{
						alert('Tiene deudas pendientes, por tanto no podrá ver las Notas, porfavor cancelarlas...Gracias!!');
					}
					//btnNotas.on('click',function() {notasPromedio($(this))});
				});*/
				btnAsistencia.on('click',function() {asistencia($(this))});
				btnAgenda.on('click',function() {agenda($(this))});
				btnCampusVirtual.on('click',function() {campus_virtual($(this))});
					
				i++;
			});
			
			
		},param);

	
	//lista tabla
	//alumno_listar_tabla();
});


function alumno_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/alu_alumno_modal.html');
	alumno_modal(link);
	
}
 


function profesores(field){
	var param={id_mat:$(field).data('id_mat')};
	_send('pages/apoderado/apo_profesores.html','Mis Hijos','Lista de Profesores',param);
}

function notasPromedio(field){
	
	_send('pages/apoderado/apo_notas_promedio.html','Mis Hijos','Lista de notas',field.data('alumno'));
}
 function asistencia(field) {
	 console.log(field.data('alumno'));
	 _send('pages/asistencia/asi_asistencia_hijo.html','Mis Hijos','Lista de notas',field.data('alumno'));
}
 
 function campus_virtual(field) {
	 var tc_acept=field.data('tc_acept');
	 if(tc_acept!=null && tc_acept=='1'){
		 //_send('pages/asistencia/asi_asistencia_hijo.html','Mis Hijos','Lista de notas',field.data('alumno'));
		// location.href="https://aulavirtual.ae.edu.pe/";
		 window.open('https://aulavirtual.ae.edu.pe/', '_blank');
		// window.open('pages/campus_virtual/cv_info_mantenimiento.html', '_blank');
	 } else{
		 var onShowModal = function(){	
				_inputs('frm-info_campus_virtual');
				
			}

			//funcion q se ejecuta despues de grabar
			var onSuccessSave = function(data){
				
			}
			
			//Abrir el modal
			var titulo='BIENVENIDOS AL CAMPUS VIRTUAL';
			
			_modal_full(titulo,'pages/campus_virtual/cv_info_modal.html',onShowModal,onSuccessSave);
	 }
	 
}

 
 function agenda (field){
	_send('pages/alumno/alu_agenda.html','Mis Hijos','Agenda',field.data('alumno'));
 }
 
 function restablecer(obj){
		//e.preventDefault();
	 //console.log(obj);
	 	var usuario = $(obj).attr('usuario');
		
		alert(usuario);
		
	 	
		//var usuario=obj.usuario;
		var onShowModal = function(){
				alert(usuario);
			$("#frm-restablecer_contrasenia #usuario").text(usuario);
			$("#frm-restablecer_contrasenia #usr").val(usuario);
			//$("#usuario").text('1');
			//alert();
		}

		//funcion q se ejecuta despues de grabar
		var onSuccessSave = function(data){
			
		}
		
		//Abrir el modal
		var titulo='RESTABLECER CONTRASEÑA';
		//alert(usuario);
		var params={usuario:usuario};
		_modal(titulo,'pages/campus_virtual/cv_restablecer_contrasenia_modal.html',onShowModal,onSuccessSave,params);
}
 
 