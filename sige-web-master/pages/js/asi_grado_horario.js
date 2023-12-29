//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {
	_onloadPage();
}
// se ejecuta siempre despues de cargar el html
$(function() {
	$('#asi_grado_horario-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		grado_horario_modal($(this));
	});

	// lista tabla
	grado_horario_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	grado_horario_listar_tabla();
}

function grado_horario_eliminar(link) {
	_delete('api/gradoHorario/' + $(link).data("id"), function() {
		grado_horario_listar_tabla();
	});
}

function grado_horario_editar(row, e) {
	e.preventDefault();

	var link = $(row);
	link.attr('href', 'pages/asistencia/asi_grado_horario_modal.html');
	grado_horario_modal(link);

}

function grado_horario_modal(link) {

	// funcion q se ejecuta al cargar el modal
	var onShowModal = function() {

		$('#id_anio').val($('#_id_anio').text());
		$('#btn_Agregar').on('click', function(event) {
			var form = $('.modal').find('form');
			form.validate();
			if (form.valid()) {

				_post(form.attr('action'), form.serialize(), function(data) {
					$('#id').val('');
					grado_horario_listar_tabla();
				});
			}

		});

		// cargamos los eventos
		

		/*$('#id_gra').on(
				'change',
				function(event) {
					/*_llenarCombo('col_aula', $('#id_au'), null, $("#_id_anio")
							.text()
							+ ',' + $(this).val());*/
				/*	var param ={id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_gir:$('#id_gir').val()}
					_llenarComboURL('api/aula/listarAulasxGiroNivelGrado',$('#id_au'), null, param, function(){
						
					});
				});*/
		$('#id_gra').on('change',function(event){
				$('#id_cic').change();
		});	
	
		$('#id_cic').on('change',function(event){
			var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
			_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
			//listar_aulas($('#id_cic').val(), $('#id_grad').val());
		});	
				
		$('#id_tpe').on('change',function(event){
			var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
			_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
		});
				
		$('#id_niv').on(
				'change',
				function(event) {
					_llenarCombo('cat_grad', $('#id_gra'), null, $(this).val(),
							function() {
								$('#id_gra').change();
							});
					var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
					_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
				});

		if (link.data('id')) {
			$('#btn-Agregar').hide();
			_get('api/gradoHorario/' + link.data('id'),
					function(data) {
						// inicialziamos la data
						_fillForm(data, $('.modal').find('form'));

						/*_llenarCombo('col_aula', $('#id_au'), data.id_au, $(
								"#_id_anio").text()
								+ ',' + data.id_gra);*/
						/*$('#id_gra').on(
							'change',
							function(event) {
								alert();
								var param ={id_anio:$("#_id_anio").text(), id_gra:$('#id_gra').val(), id_gir:$('#id_gir').val()}
								_llenarComboURL('api/aula/listarAulasxGiroNivelGrado',$('#id_au'), data.id_au, param, function(){						
								});
						});*/
						
		
	
		$('#id_cic').on('change',function(event){
			var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
			_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),data.id_au, param, function() {$('#id_au').change();});
			//listar_aulas($('#id_cic').val(), $('#id_grad').val());
		});	
				
		$('#id_tpe').on('change',function(event){
			var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val()};
			_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),data.id_cic,param,function(){$('#id_cic').change();});
		});
				
		$('#id_niv').on(
				'change',
				function(event) {
					_llenarCombo('cat_grad', $('#id_gra'), null, $(this).val(),
							function() {
								$('#id_gra').change();
							});
					var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
					_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),data.id_tpe,param,function(){$('#id_tpe').change();});
				});

								
						
						
						/*_llenarCombo('cat_grad', $('#id_gra'), data.id_gra,
								data.id_niv, function() {
								$('#id_gra').change();	
						});*/
				$('#id_gir').on('change',function(event){
					_llenarCombo('cat_nivel', $('#id_niv'), data.id_niv,null,function() {
								$('#id_niv').change();	
						});
				});	
						
						
						_llenarCombo('ges_giro_negocio', $('#id_gir'), data.id_gir, null, function() {
							$('#id_gir').change();
						});

					});
		} else {
			_llenarCombo('cat_nivel', $('#id_niv'), null, null, function() {
				$('#btn-Grabar').hide();
			});
			
			_llenarCombo('ges_giro_negocio', $('#id_gir'), null, null, function() {
			});
		}
		

	}

	// funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data) {
		grado_horario_listar_tabla();
	}

	// Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Horario por grado';
	else
		titulo = 'Nuevo  Horario por grado';

	_modal(titulo, link.attr('href'), onShowModal, onSuccessSave);

}
function grado_horario_listar_tabla() {
	var id_anio = $('#_id_anio').text();
	_get(
			'api/gradoHorario/listar/' + id_anio,
			function(data) {
				console.log(data);
				$('#asi_grado_horario-tabla')
						.dataTable(
								{
									data : data,
									aaSorting : [],
									destroy : true,
									// bLengthChange: false,
									// bPaginate: false,
									// bFilter: false,
									select : true,
									columns : [
											{
												"title" : "A&ntilde;o",
												"data" : "anio"
											},
											{
												"title" : "Local",
												"data" : "local"
											},
											{
												"title" : "Nivel",
												"data" : "nivel"
											},
											{
												"title" : "Grado - Sección",
												"data" : "aula"
											},
											{
												"title" : "Hora de inicio",
												"data" : "hora_ini"
											},
											{
												"title" : "Hora fin",
												"data" : "hora_fin"
											},
											{
												"title" : "(Turno 2)Hora de inicio",
												"data" : "hora_ini_aux"
											},
											{
												"title" : "(Turno 2)Hora fin",
												"data" : "hora_fin_aux"
											},
											{
												"title" : "Acciones",
												"render" : function(data, type,
														row) {
													return '<ul class="icons-list">'
															+ '<li class="dropdown">'
															+ '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'
															+ '<i class="icon-menu9"></i>'
															+ '</a>'
															+ '<ul class="dropdown-menu dropdown-menu-right">'
															+ '<li><a href="#" data-id="'
															+ row.id
															+ '" onclick="grado_horario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'
															+ '<li><a href="#" data-id="'
															+ row.id
															+ '" onclick="grado_horario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'
															+ '</ul>'
															+ '</li>'
															+ '</ul>';
												}
											} ]
								});
			});

}
