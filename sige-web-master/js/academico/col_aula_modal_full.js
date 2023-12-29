//edicion completa de una tabla
function aula_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_full.html');
	aula_full_modal(link);

}


function aula_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#mat_reserva-tabla').data('id_padre',link.data('id'));
		$('#mat_matricula-tabla').data('id_padre',link.data('id'));
		$('#asi_grado_horario-tabla').data('id_padre',link.data('id'));
		$('#cat_grado_horario-tabla').data('id_padre',link.data('id'));
		$('#col_tutor_aula-tabla').data('id_padre',link.data('id'));
		$('#col_curso_aula-tabla').data('id_padre',link.data('id'));
		$('#col_curso_horario_pad-tabla').data('id_padre',link.data('id'));
		$('#col_permiso_docente-tabla').data('id_padre',link.data('id'));
		$('#not_comportamiento-tabla').data('id_padre',link.data('id'));

		reserva_listar_tabla();
		matricula_listar_tabla();
		grado_horario_listar_tabla();
		grado_horario_listar_tabla();
		tutor_aula_listar_tabla();
		curso_aula_listar_tabla();
		curso_horario_pad_listar_tabla();
		permiso_docente_listar_tabla();
		comportamiento_listar_tabla();
		_inputs('col_aula_full-frm');
		
		$('#col_aula_full-frm #btn-agregar').on('click',function(event){
			$('#col_aula_full-frm #id').val('');
			_post($('#col_aula_full-frm').attr('action') , $('#col_aula_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/aula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('per_periodo',$('#col_aula-frm #id_per'),data.id_per);	
						_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_turno',$('#col_aula-frm #id_turno'),data.id_turno);	
						}
			);
		}else{
					_llenarCombo('per_periodo',$('#col_aula-frm #id_per'));
					_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'));
					_llenarCombo('col_turno',$('#col_aula-frm #id_turno'));
				}
		$('#col_aula_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Aula del colegio';
	else
		titulo = 'Nuevo  Aula del colegio';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reserva_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_reserva_modal.html');
	reserva_modal(link);
}

function reserva_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_reserva_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	reserva_modal(link);
}

function reserva_eliminar(link){
	_delete('api/reserva/' + $(link).data("id"),
			function(){
				reserva_listar_tabla();
				}
			);
}
function reserva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_reserva-frm');
		
		
		
		$('#mat_reserva-frm #btn-agregar').on('click',function(event){
			$('#mat_reserva-frm #id').val('');
			_post($('#mat_reserva-frm').attr('action') , $('#mat_reserva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reserva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#col_aula-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_cond_matricula',$('#col_aula-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#col_aula-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#col_aula-frm #id_per'),data.id_per);	
						_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'),data.id_fam);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'));
			_llenarCombo('cat_nivel',$('#col_aula-frm #id_niv'));
			_llenarCombo('cat_cond_matricula',$('#col_aula-frm #id_cma'));
			_llenarCombo('cat_cliente',$('#col_aula-frm #id_cli'));
			_llenarCombo('per_periodo',$('#col_aula-frm #id_per'));
			_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'));
			$('#mat_reserva-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reserva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reserva de matr&iacute;cula';
	else
		titulo = 'Nuevo  Reserva de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reserva_listar_tabla(){
	_get('api/reserva/listar/',
			function(data){
			console.log(data);
				$('#mat_reserva-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha Reserva", "data" : "fec"}, 
							{"title":"Fecha L&iacute;mite", "data" : "fec_lim"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Condicion Matricula", "data": "condMatricula.nom"}, 
							{"title":"Cliente", "data": "cliente.nom"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Apoderado", "data": "familiar.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reserva_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reserva_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Reserva matrícula', 'reserva_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#mat_reserva-tabla').data('id_padre')}
	);

}	

function matricula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_matricula_modal.html');
	matricula_modal(link);
}

function matricula_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_matricula_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	matricula_modal(link);
}

function matricula_eliminar(link){
	_delete('api/matricula/' + $(link).data("id"),
			function(){
				matricula_listar_tabla();
				}
			);
}
function matricula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_matricula-frm');
		
		
		
		$('#mat_matricula-frm #btn-agregar').on('click',function(event){
			$('#mat_matricula-frm #id').val('');
			_post($('#mat_matricula-frm').attr('action') , $('#mat_matricula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/matricula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'),data.id_alu);	
						_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'),data.id_fam);	
						_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'),data.id_fam);	
						_llenarCombo('cat_cond_matricula',$('#col_aula-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#col_aula-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#col_aula-frm #id_per'),data.id_per);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#col_aula-frm #id_niv'),data.id_niv);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'));
			_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'));
			_llenarCombo('alu_familiar',$('#col_aula-frm #id_fam'));
			_llenarCombo('cat_cond_matricula',$('#col_aula-frm #id_cma'));
			_llenarCombo('cat_cliente',$('#col_aula-frm #id_cli'));
			_llenarCombo('per_periodo',$('#col_aula-frm #id_per'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			_llenarCombo('cat_grad',$('#col_aula-frm #id_gra'));
			_llenarCombo('cat_nivel',$('#col_aula-frm #id_niv'));
			$('#mat_matricula-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		matricula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Matricula del alumno';
	else
		titulo = 'Nuevo  Matricula del alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function matricula_listar_tabla(){
	_get('api/matricula/listar/',
			function(data){
			console.log(data);
				$('#mat_matricula-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha matricula", "data" : "fecha"}, 
							{"title":"Presenta carta poder", "data" : "car_pod"}, 
							{"title":"N&uacute;mero de Contrato", "data" : "num_cont"}, 
							{"title":"Observaci&oacute;n", "data" : "obs"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Apoderado", "data": "familiar.nom"}, 
							{"title":"Encargado", "data": "familiar.nom"}, 
							{"title":"Condicion Matricula", "data": "condMatricula.nom"}, 
							{"title":"Cliente", "data": "cliente.nom"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matricula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Matricula', 'matricula_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#mat_matricula-tabla').data('id_padre')}
	);

}	

function grado_horario_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/asistencia/asi_grado_horario_modal.html');
	grado_horario_modal(link);
}

function grado_horario_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/asistencia/asi_grado_horario_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	grado_horario_modal(link);
}

function grado_horario_eliminar(link){
	_delete('api/gradoHorario/' + $(link).data("id"),
			function(){
				grado_horario_listar_tabla();
				}
			);
}
function grado_horario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('asi_grado_horario-frm');
		
		
		
		$('#asi_grado_horario-frm #btn-agregar').on('click',function(event){
			$('#asi_grado_horario-frm #id').val('');
			_post($('#asi_grado_horario-frm').attr('action') , $('#asi_grado_horario-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/gradoHorario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_aula-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_aula-frm #id_anio'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			$('#asi_grado_horario-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		grado_horario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Horario por grado';
	else
		titulo = 'Nuevo  Horario por grado';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function grado_horario_listar_tabla(){
	_get('api/gradoHorario/listar/',
			function(data){
			console.log(data);
				$('#asi_grado_horario-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Hora de inicio", "data" : "hora_ini"}, 
							{"title":"Hora fin", "data" : "hora_fin"}, 
							{"title":"(Turno 2)Hora de inicio", "data" : "hora_ini_aux"}, 
							{"title":"(Turno 2)Hora fin", "data" : "hora_fin_aux"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="grado_horario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="grado_horario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Horario por grado', 'grado_horario_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#asi_grado_horario-tabla').data('id_padre')}
	);

}	

function grado_horario_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/catalogos/cat_grado_horario_modal.html');
	grado_horario_modal(link);
}

function grado_horario_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_grado_horario_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	grado_horario_modal(link);
}

function grado_horario_eliminar(link){
	_delete('api/gradoHorario/' + $(link).data("id"),
			function(){
				grado_horario_listar_tabla();
				}
			);
}
function grado_horario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('cat_grado_horario-frm');
		
		
		
		$('#cat_grado_horario-frm #btn-agregar').on('click',function(event){
			$('#cat_grado_horario-frm #id').val('');
			_post($('#cat_grado_horario-frm').attr('action') , $('#cat_grado_horario-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/gradoHorario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_aula-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_aula-frm #id_anio'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			$('#cat_grado_horario-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		grado_horario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Horario por grado';
	else
		titulo = 'Nuevo  Horario por grado';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function grado_horario_listar_tabla(){
	_get('api/gradoHorario/listar/',
			function(data){
			console.log(data);
				$('#cat_grado_horario-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Hora de inicio", "data" : "hora_ini"}, 
							{"title":"Hora fin", "data" : "hora_fin"}, 
							{"title":"(Turno 2)Hora de inicio", "data" : "hora_ini_aux"}, 
							{"title":"(Turno 2)Hora fin", "data" : "hora_fin_aux"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="grado_horario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="grado_horario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Horario por grado', 'grado_horario_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#cat_grado_horario-tabla').data('id_padre')}
	);

}	

function tutor_aula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_tutor_aula_modal.html');
	tutor_aula_modal(link);
}

function tutor_aula_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_tutor_aula_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	tutor_aula_modal(link);
}

function tutor_aula_eliminar(link){
	_delete('api/tutorAula/' + $(link).data("id"),
			function(){
				tutor_aula_listar_tabla();
				}
			);
}
function tutor_aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_tutor_aula-frm');
		
		
		
		$('#col_tutor_aula-frm #btn-agregar').on('click',function(event){
			$('#col_tutor_aula-frm #id').val('');
			_post($('#col_tutor_aula-frm').attr('action') , $('#col_tutor_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tutorAula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
						_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'),data.id_tra);	
				}
			);
		}else{
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'));
			$('#col_tutor_aula-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tutor_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Aula del Tutor';
	else
		titulo = 'Nuevo  Aula del Tutor';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function tutor_aula_listar_tabla(){
	_get('api/tutorAula/listar/',
			function(data){
			console.log(data);
				$('#col_tutor_aula-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Trabajador", "data": "trabajador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tutor_aula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="tutor_aula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Aula del Tutor', 'tutor_aula_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#col_tutor_aula-tabla').data('id_padre')}
	);

}	

function curso_aula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_modal.html');
	curso_aula_modal(link);
}

function curso_aula_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	curso_aula_modal(link);
}

function curso_aula_eliminar(link){
	_delete('api/cursoAula/' + $(link).data("id"),
			function(){
				curso_aula_listar_tabla();
				}
			);
}
function curso_aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_aula-frm');
		
		
		
		$('#col_curso_aula-frm #btn-agregar').on('click',function(event){
			$('#col_curso_aula-frm #id').val('');
			_post($('#col_curso_aula-frm').attr('action') , $('#col_curso_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoAula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_curso_anio',$('#col_aula-frm #id_cua'),data.id_cua);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
						_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'),data.id_tra);	
				}
			);
		}else{
			_llenarCombo('col_curso_anio',$('#col_aula-frm #id_cua'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'));
			$('#col_curso_aula-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Aula';
	else
		titulo = 'Nuevo  Curso Aula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_aula_listar_tabla(){
	_get('api/cursoAula/listar/',
			function(data){
			console.log(data);
				$('#col_curso_aula-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nro de sesiones por semana", "data" : "nro_ses"}, 
							{"title":"Curso Anio", "data": "cursoAnio.peso"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Docente", "data": "trabajador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_aula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_aula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Aula', 'curso_aula_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#col_curso_aula-tabla').data('id_padre')}
	);

}	

function curso_horario_pad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_pad_modal.html');
	curso_horario_pad_modal(link);
}

function curso_horario_pad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_pad_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	curso_horario_pad_modal(link);
}

function curso_horario_pad_eliminar(link){
	_delete('api/cursoHorarioPad/' + $(link).data("id"),
			function(){
				curso_horario_pad_listar_tabla();
				}
			);
}
function curso_horario_pad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_horario_pad-frm');
		
		
		
		$('#col_curso_horario_pad-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario_pad-frm #id').val('');
			_post($('#col_curso_horario_pad-frm').attr('action') , $('#col_curso_horario_pad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoHorarioPad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_aula-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_aula-frm #id_anio'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			$('#col_curso_horario_pad-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_horario_pad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Horario Padre';
	else
		titulo = 'Nuevo  Curso Horario Padre';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_horario_pad_listar_tabla(){
	_get('api/cursoHorarioPad/listar/',
			function(data){
			console.log(data);
				$('#col_curso_horario_pad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha Inicio Vigencia", "data" : "fec_ini_vig"}, 
							{"title":"Fecha Fin Vigencia", "data" : "fec_fin_vig"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_pad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_pad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Horario Padre', 'curso_horario_pad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#col_curso_horario_pad-tabla').data('id_padre')}
	);

}	

function permiso_docente_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_permiso_docente_modal.html');
	permiso_docente_modal(link);
}

function permiso_docente_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_permiso_docente_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	permiso_docente_modal(link);
}

function permiso_docente_eliminar(link){
	_delete('api/permisoDocente/' + $(link).data("id"),
			function(){
				permiso_docente_listar_tabla();
				}
			);
}
function permiso_docente_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_permiso_docente-frm');
		
		
		
		$('#col_permiso_docente-frm #btn-agregar').on('click',function(event){
			$('#col_permiso_docente-frm #id').val('');
			_post($('#col_permiso_docente-frm').attr('action') , $('#col_permiso_docente-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/permisoDocente/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'),data.id_tra);	
						_llenarCombo('col_per_uni',$('#col_aula-frm #id_cpu'),data.id_cpu);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'));
			_llenarCombo('col_per_uni',$('#col_aula-frm #id_cpu'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			$('#col_permiso_docente-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		permiso_docente_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Permiso Docente';
	else
		titulo = 'Nuevo  Permiso Docente';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function permiso_docente_listar_tabla(){
	_get('api/permisoDocente/listar/',
			function(data){
			console.log(data);
				$('#col_permiso_docente-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Dias", "data" : "dias"}, 
							{"title":"Profesor", "data": "trabajador.nom"}, 
							{"title":"Periodo Unidad", "data": "perUni.nump"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="permiso_docente_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="permiso_docente_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Permiso Docente', 'permiso_docente_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#col_permiso_docente-tabla').data('id_padre')}
	);

}	

function comportamiento_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_comportamiento_modal.html');
	comportamiento_modal(link);
}

function comportamiento_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_comportamiento_modal.html');
	link.data('id_au',$('#col_aula_full-frm #id').val());
	comportamiento_modal(link);
}

function comportamiento_eliminar(link){
	_delete('api/comportamiento/' + $(link).data("id"),
			function(){
				comportamiento_listar_tabla();
				}
			);
}
function comportamiento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_comportamiento-frm');
		
		
		
		$('#not_comportamiento-frm #btn-agregar').on('click',function(event){
			$('#not_comportamiento-frm #id').val('');
			_post($('#not_comportamiento-frm').attr('action') , $('#not_comportamiento-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/comportamiento/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'),data.id_tra);	
						_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#col_aula-frm #id_au'),data.id_au);	
						_llenarCombo('col_per_uni',$('#col_aula-frm #id_cpu'),data.id_cpu);	
				}
			);
		}else{
			_llenarCombo('ges_trabajador',$('#col_aula-frm #id_tra'));
			_llenarCombo('alu_alumno',$('#col_aula-frm #id_alu'));
			_llenarCombo('col_aula',$('#col_aula-frm #id_au'),$('#col_aula_full-frm #id').val());
			_llenarCombo('col_per_uni',$('#col_aula-frm #id_cpu'));
			$('#not_comportamiento-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		comportamiento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Comportamiento';
	else
		titulo = 'Nuevo  Nota Comportamiento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function comportamiento_listar_tabla(){
	_get('api/comportamiento/listar/',
			function(data){
			console.log(data);
				$('#not_comportamiento-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Promedio", "data" : "prom"}, 
							{"title":"Trabajador", "data": "trabajador.nom"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Periodo Academico", "data": "perUni.nump"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="comportamiento_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="comportamiento_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Nota Comportamiento', 'comportamiento_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_au":$('#not_comportamiento-tabla').data('id_padre')}
	);

}	
