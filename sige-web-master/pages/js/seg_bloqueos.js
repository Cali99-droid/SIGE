//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {

}
// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();

	_llenarCombo('seg_perfil', $('#id_per'), null, 'A');

	$('#nom').focus();

	$('#buscar-frm').on('submit', function(e) {
		e.preventDefault();
		usuario_listar_tabla();
	});

});


function usuario_listar_tabla() {

	var id_per = $('#id_per').val();

	if (id_per == _PERFIL_FAMILIAR) {
		var param = {
				'nom' :  $('#nom').val(),
				'id_anio' :  $('#_id_anio').text(),
				'est' :  $('#est').val()
			};
		
		_get(
				'api/familiar/buscar/',
				function(data) {
					console.log(data);
					$('#seg_usuario-tabla')
							.dataTable(
									{
										data : data,
										aaSorting : [],
										destroy : true,
										orderCellsTop : true,
										select : true,
										columns : [
												{
													"title" : "Nro",
													"render" : function(data,
															type, row, meta) {
														return parseInt(meta.row) + 1;
													}
												},
												{
													"title" : "Familiar",
													"render" : function(data,
															type, row) {
														return row.ape_pat
																+ " "
																+ row.ape_mat
																+ ", "
																+ row.nom;
													}
												},
												{
													"title" : "Nro.doc",
													"data" : "nro_doc"
												},
												{
													"title" : "Fec. Nac.",
													"render" : function(data,
															type, row) {
														return _parseDate(row.fec_nac);
													}
												},
												{
													"title" : "Correo",
													"render" : function(data,
															type, row) {
														return row.corr;
													}
												},
												{
													"title" : "Celular",
													"render" : function(data,
															type, row) {
														return row.cel;
													}
												},
												{
													"title" : "Estado",
													"render" : function(data,
															type, row) {
														if(row.est=='A')
															return 'HABILITADO';
														else
															return 'BLOQUEADO';
													}
												},
												{
													"title" : "Acciones",
													"className" : "text-center",
													"render" : function(data,
															type, row) {
														if (row.est == 'A')
															return '<a href="#" title="BLOQUEAR" data-id="' + row.id + '"' +
															' data-nom="' + row.ape_pat + ' ' + row.ape_mat + ', ' + row.nom + '" onclick="familiar_bloquear(this, event,0)"><i class="icon-user-block text-danger"></i></a>';
														else
															return '<a href="#" title="HABILITAR" data-id="' + row.id  + '"' +
															' data-nom="' + row.ape_pat + ' ' + row.ape_mat + ', ' + row.nom + '" onclick="familiar_bloquear(this, event,1)"><i class="icon-user-check text-success"></i></a>';
													}
												} ],
										"initComplete" : function(settings) {
											_initCompleteDT(settings);
										}
									});
				}, param);

	} else if (id_per == _PERFIL_TRABAJADOR) {

		var param = {
				'nom' :  $('#nom').val(),
				'est' :  $('#est').val()
			};
		
		_get(
				'api/usuario/buscar/',
				function(data) {
					console.log(data);
					$('#seg_usuario-tabla')
							.dataTable(
									{
										data : data,
										aaSorting : [],
										destroy : true,
										orderCellsTop : true,
										// bLengthChange: false,
										// bPaginate: false,
										// bFilter: false,
										select : true,
										columns : [
												{
													"title" : "Nro",
													"render" : function(data,
															type, row, meta) {
														return parseInt(meta.row) + 1;
													}
												},
												{
													"title" : "Trabajador",
													"render" : function(data,
															type, row) {
														return row.trabajador.ape_pat
																+ " "
																+ row.trabajador.ape_mat
																+ ", "
																+ row.trabajador.nom;
													}
												},
												{
													"title" : "Usuario",
													"data" : "login"
												},
												{
													"title" : "Fec. Nac.",
													"render" : function(data,
															type, row) {
														return _parseDate(row.trabajador.fec_nac);
													}
												},
												{
													"title" : "Correo",
													"render" : function(data,
															type, row) {
														return row.trabajador.corr;
													}
												},
												{
													"title" : "Celular",
													"render" : function(data,
															type, row) {
														return row.trabajador.cel;
													}
												},
												{
													"title" : "Estado",
													"render" : function(data,
															type, row) {
														if(row.est=='A')
															return 'HABILITADO';
														else
															return 'BLOQUEADO';
													}
												},
												{
													"title" : "Acciones",
													"className" : "text-center",
													"render" : function(data,
															type, row) {
														if (row.est == 'A')
															return '<a href="#" title="BLOQUEAR" data-id="'+ row.id + '"' +
																' data-nom="' + row.trabajador.ape_pat + ' ' + row.trabajador.ape_mat + ', ' + row.trabajador.nom + '" onclick="usuario_bloquear(this, event,0)"><i class="icon-user-block text-danger"></i></a>';
														else
															return '<a href="#" title="HABILITAR" data-id="'+ row.id + '"' +
																' data-nom="' + row.trabajador.ape_pat + ' ' + row.trabajador.ape_mat + ', ' + row.trabajador.nom + '"  onclick="usuario_bloquear(this, event,1)"><i class="icon-user-check text-success"></i></a>';
													}
												} ],
										"initComplete" : function(settings) {
											_initCompleteDT(settings);
										}
									});
				}, param);

	}

}

function familiar_bloquear(obj, e, est){
	console.log(obj);
	var text = '';
	var boton= '';
	var nuevo_est = '';
	if(est=='0'){//bloquear
		text = 'De bloquear al familiar ' + $(obj).data('nom');
		boton = 'Si, bloquear';
		nuevo_est = 'I';
	}else{//desbloquear
		text = 'De habilitar al familiar ' + $(obj).data('nom');
		boton = 'Si, habilitar';
		nuevo_est = 'A';
	}
	
	swal(
			{
				title : "Esta seguro?",
				text :text,
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText :boton,
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					
					_post('api/familiar/actualizarEstado',{'est':nuevo_est,'id': $(obj).data('id')}, function success(){
						usuario_listar_tabla();

					});

					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha actualizado ningun familiar",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
				}
			});
	
}


function usuario_bloquear(obj, e, est){
	console.log(obj);
	var text = '';
	var boton= '';
	var nuevo_est = '';
	if(est=='0'){//bloquear
		text = 'De bloquear al usuario ' + $(obj).data('nom');
		boton = 'Si, bloquear';
		nuevo_est = 'I';
	}else{//desbloquear
		text = 'De habilitar al usuario ' + $(obj).data('nom');
		boton = 'Si, habilitar';
		nuevo_est = 'A';
	}
	
	swal(
			{
				title : "Esta seguro?",
				text :text,
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "rgb(33, 150, 243)",
				cancelButtonColor : "#EF5350",
				confirmButtonText :boton,
				cancelButtonText : "No, cancela!!!",
				closeOnConfirm : false,
				closeOnCancel : false
			},
			function(isConfirm) {
				if (isConfirm) {
					
					_post('api/usuario/actualizarEstado',{'est':nuevo_est,'id': $(obj).data('id')}, function success(){
						usuario_listar_tabla();

					});

					
				} else {
					swal({
						title : "Cancelado",
						text : "No se ha actualizado ningun usuario",
						confirmButtonColor : "#2196F3",
						type : "error"
					});
				}
			});
	
}