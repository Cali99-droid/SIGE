//edicion completa de una tabla
function grad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_grad_full.html');
	grad_full_modal(link);

}


function grad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#eva_vacante-tabla').data('id_padre',link.data('id'));
		$('#eva_matr_vacante-tabla').data('id_padre',link.data('id'));
		$('#col_capacidad_setup-tabla').data('id_padre',link.data('id'));
		$('#mat_reserva-tabla').data('id_padre',link.data('id'));
		$('#mat_matricula-tabla').data('id_padre',link.data('id'));
		$('#col_aula-tabla').data('id_padre',link.data('id'));
		$('#col_curso_anio-tabla').data('id_padre',link.data('id'));
		$('#col_indicador-tabla').data('id_padre',link.data('id'));
		$('#col_curso_subtema-tabla').data('id_padre',link.data('id'));
		$('#col_curso_unidad-tabla').data('id_padre',link.data('id'));

		vacante_listar_tabla();
		matr_vacante_listar_tabla();
		capacidad_setup_listar_tabla();
		reserva_listar_tabla();
		matricula_listar_tabla();
		aula_listar_tabla();
		curso_anio_listar_tabla();
		indicador_listar_tabla();
		curso_subtema_listar_tabla();
		curso_unidad_listar_tabla();
		_inputs('cat_grad_full-frm');
		
		$('#cat_grad_full-frm #btn-agregar').on('click',function(event){
			$('#cat_grad_full-frm #id').val('');
			_post($('#cat_grad_full-frm').attr('action') , $('#cat_grad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/grad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'),data.id_niv);	
						}
			);
		}else{
					_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'));
				}
		$('#cat_grad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		grad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Grado Educativo';
	else
		titulo = 'Nuevo  Grado Educativo';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function vacante_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_vacante_modal.html');
	vacante_modal(link);
}

function vacante_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_vacante_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	vacante_modal(link);
}

function vacante_eliminar(link){
	_delete('api/vacante/' + $(link).data("id"),
			function(){
				vacante_listar_tabla();
				}
			);
}
function vacante_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_vacante-frm');
		
		
		
		$('#eva_vacante-frm #btn-agregar').on('click',function(event){
			$('#eva_vacante-frm #id').val('');
			_post($('#eva_vacante-frm').attr('action') , $('#eva_vacante-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/vacante/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('eva_evaluacion_vac',$('#cat_grad-frm #id_eva_vac'),data.id_eva_vac);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
				}
			);
		}else{
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('eva_evaluacion_vac',$('#cat_grad-frm #id_eva_vac'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			$('#eva_vacante-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		vacante_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Vacante';
	else
		titulo = 'Nuevo  Vacante';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function vacante_listar_tabla(){
	_get('api/vacante/listar/',
			function(data){
			console.log(data);
				$('#eva_vacante-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Capacidad ", "data" : "nro_vac"}, 
							{"title":"Vacantes ofertadas ", "data" : "vac_ofe"}, 
							{"title":"Postulantes", "data" : "post"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Evaluaci&oacute;n Vacante", "data": "evaluacionVac.des"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="vacante_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="vacante_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Vacante', 'vacante_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#eva_vacante-tabla').data('id_padre')}
	);

}	

function matr_vacante_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_matr_vacante_modal.html');
	matr_vacante_modal(link);
}

function matr_vacante_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_matr_vacante_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	matr_vacante_modal(link);
}

function matr_vacante_eliminar(link){
	_delete('api/matrVacante/' + $(link).data("id"),
			function(){
				matr_vacante_listar_tabla();
				}
			);
}
function matr_vacante_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_matr_vacante-frm');
		
		
		
		$('#eva_matr_vacante-frm #btn-agregar').on('click',function(event){
			$('#eva_matr_vacante-frm #id').val('');
			_post($('#eva_matr_vacante-frm').attr('action') , $('#eva_matr_vacante-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/matrVacante/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'),data.id_alu);	
						_llenarCombo('eva_evaluacion_vac',$('#cat_grad-frm #id_eva_vac'),data.id_eva_vac);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_colegio',$('#cat_grad-frm #id_col'),data.id_col);	
						_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'),data.id_fam);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'));
			_llenarCombo('eva_evaluacion_vac',$('#cat_grad-frm #id_eva_vac'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('col_colegio',$('#cat_grad-frm #id_col'));
			_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'));
			$('#eva_matr_vacante-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		matr_vacante_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Examen Vacante';
	else
		titulo = 'Nuevo  Examen Vacante';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function matr_vacante_listar_tabla(){
	_get('api/matrVacante/listar/',
			function(data){
			console.log(data);
				$('#eva_matr_vacante-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Recibo", "data" : "num_rec"}, 
							{"title":"Contrato", "data" : "num_cont"}, 
							{"title":"Resultado", "data" : "res"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Evaluaci&oacute;n Vacante", "data": "evaluacionVac.des"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Colegio", "data": "colegio.nom"}, 
							{"title":"Familiar", "data": "familiar.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matr_vacante_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matr_vacante_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Matricula Vacante', 'matr_vacante_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#eva_matr_vacante-tabla').data('id_padre')}
	);

}	

function capacidad_setup_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_setup_modal.html');
	capacidad_setup_modal(link);
}

function capacidad_setup_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_setup_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	capacidad_setup_modal(link);
}

function capacidad_setup_eliminar(link){
	_delete('api/capacidadSetup/' + $(link).data("id"),
			function(){
				capacidad_setup_listar_tabla();
				}
			);
}
function capacidad_setup_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_capacidad_setup-frm');
		
		
		
		$('#col_capacidad_setup-frm #btn-agregar').on('click',function(event){
			$('#col_capacidad_setup-frm #id').val('');
			_post($('#col_capacidad_setup-frm').attr('action') , $('#col_capacidad_setup-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/capacidadSetup/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
				}
			);
		}else{
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			$('#col_capacidad_setup-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		capacidad_setup_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuracion Capacidad';
	else
		titulo = 'Nuevo  Configuracion Capacidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function capacidad_setup_listar_tabla(){
	_get('api/capacidadSetup/listar/',
			function(data){
			console.log(data);
				$('#col_capacidad_setup-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Cantidad", "data" : "cant"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="capacidad_setup_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="capacidad_setup_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Configuracion Capacidad', 'capacidad_setup_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_capacidad_setup-tabla').data('id_padre')}
	);

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
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
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
						_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#cat_grad-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_cond_matricula',$('#cat_grad-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#cat_grad-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'),data.id_fam);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'));
			_llenarCombo('col_aula',$('#cat_grad-frm #id_au'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'));
			_llenarCombo('cat_cond_matricula',$('#cat_grad-frm #id_cma'));
			_llenarCombo('cat_cliente',$('#cat_grad-frm #id_cli'));
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'));
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
			},{"id_gra":$('#mat_reserva-tabla').data('id_padre')}
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
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
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
						_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'),data.id_alu);	
						_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'),data.id_fam);	
						_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'),data.id_fam);	
						_llenarCombo('cat_cond_matricula',$('#cat_grad-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#cat_grad-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('col_aula',$('#cat_grad-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'),data.id_niv);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#cat_grad-frm #id_alu'));
			_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'));
			_llenarCombo('alu_familiar',$('#cat_grad-frm #id_fam'));
			_llenarCombo('cat_cond_matricula',$('#cat_grad-frm #id_cma'));
			_llenarCombo('cat_cliente',$('#cat_grad-frm #id_cli'));
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('col_aula',$('#cat_grad-frm #id_au'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'));
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
			},{"id_gra":$('#mat_matricula-tabla').data('id_padre')}
	);

}	

function aula_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_aula_modal.html');
	aula_modal(link);
}

function aula_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	aula_modal(link);
}

function aula_eliminar(link){
	_delete('api/aula/' + $(link).data("id"),
			function(){
				aula_listar_tabla();
				}
			);
}
function aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_aula-frm');
		
		
		
		$('#col_aula-frm #btn-agregar').on('click',function(event){
			$('#col_aula-frm #id').val('');
			_post($('#col_aula-frm').attr('action') , $('#col_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/aula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_turno',$('#cat_grad-frm #id_turno'),data.id_turno);	
				}
			);
		}else{
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('col_turno',$('#cat_grad-frm #id_turno'));
			$('#col_aula-frm #btn-grabar').hide();//si se
		}
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function aula_listar_tabla(){
	_get('api/aula/listar/',
			function(data){
			console.log(data);
				$('#col_aula-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Secci&oacute;n anterior", "data" : "id_secc_ant"}, 
							{"title":"Secci&oacute;n", "data" : "secc"}, 
							{"title":"Capacidad M&aacute;xima", "data" : "cap"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Turno", "data": "turno.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="aula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="aula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Aula', 'aula_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_aula-tabla').data('id_padre')}
	);

}	

function curso_anio_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_anio_modal.html');
	curso_anio_modal(link);
}

function curso_anio_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_anio_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	curso_anio_modal(link);
}

function curso_anio_eliminar(link){
	_delete('api/cursoAnio/' + $(link).data("id"),
			function(){
				curso_anio_listar_tabla();
				}
			);
}
function curso_anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_anio-frm');
		
		
		
		$('#col_curso_anio-frm #btn-agregar').on('click',function(event){
			$('#col_curso_anio-frm #id').val('');
			_post($('#col_curso_anio-frm').attr('action') , $('#col_curso_anio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'),data.id_per);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_area_anio',$('#cat_grad-frm #id_caa'),data.id_caa);	
						_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'),data.id_cur);	
				}
			);
		}else{
			_llenarCombo('per_periodo',$('#cat_grad-frm #id_per'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('col_area_anio',$('#cat_grad-frm #id_caa'));
			_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'));
			$('#col_curso_anio-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso';
	else
		titulo = 'Nuevo  Curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_anio_listar_tabla(){
	_get('api/cursoAnio/listar/',
			function(data){
			console.log(data);
				$('#col_curso_anio-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Peso de evaluaci&oacute;n", "data" : "peso"}, 
							{"title":"Orden", "data" : "orden"}, 
							{"title":"&iquest;Se promediara?", "data" : "flg_prom"}, 
							{"title":"Nivel", "data": "periodo.id_suc"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"&Aacute;rea", "data": "areaAnio.ord"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_anio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_anio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso por año', 'curso_anio_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_curso_anio-tabla').data('id_padre')}
	);

}	

function indicador_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_indicador_modal.html');
	indicador_modal(link);
}

function indicador_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_indicador_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	indicador_modal(link);
}

function indicador_eliminar(link){
	_delete('api/indicador/' + $(link).data("id"),
			function(){
				indicador_listar_tabla();
				}
			);
}
function indicador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_indicador-frm');
		
		
		
		$('#col_indicador-frm #btn-agregar').on('click',function(event){
			$('#col_indicador-frm #id').val('');
			_post($('#col_indicador-frm').attr('action') , $('#col_indicador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/indicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#cat_grad-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_capacidad',$('#cat_grad-frm #id_cap'),data.id_cap);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#cat_grad-frm #id_anio'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('col_capacidad',$('#cat_grad-frm #id_cap'));
			$('#col_indicador-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador';
	else
		titulo = 'Nuevo  Indicador';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function indicador_listar_tabla(){
	_get('api/indicador/listar/',
			function(data){
			console.log(data);
				$('#col_indicador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Indicador", "data" : "nom"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="indicador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="indicador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Indicador', 'indicador_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_indicador-tabla').data('id_padre')}
	);

}	

function curso_subtema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_modal.html');
	curso_subtema_modal(link);
}

function curso_subtema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	curso_subtema_modal(link);
}

function curso_subtema_eliminar(link){
	_delete('api/cursoSubtema/' + $(link).data("id"),
			function(){
				curso_subtema_listar_tabla();
				}
			);
}
function curso_subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_subtema-frm');
		
		
		
		$('#col_curso_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_curso_subtema-frm #id').val('');
			_post($('#col_curso_subtema-frm').attr('action') , $('#col_curso_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoSubtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#cat_grad-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'),data.id_cur);	
						_llenarCombo('col_subtema',$('#cat_grad-frm #id_sub'),data.id_sub);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#cat_grad-frm #id_anio'));
			_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'));
			_llenarCombo('col_subtema',$('#cat_grad-frm #id_sub'));
			$('#col_curso_subtema-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Subtema';
	else
		titulo = 'Nuevo  Curso Subtema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_subtema_listar_tabla(){
	_get('api/cursoSubtema/listar/',
			function(data){
			console.log(data);
				$('#col_curso_subtema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nº Semanas", "data" : "dur"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Subtema", "data": "subtema.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_subtema_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_subtema_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Subtema', 'curso_subtema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_curso_subtema-tabla').data('id_padre')}
	);

}	

function curso_unidad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_modal.html');
	curso_unidad_modal(link);
}

function curso_unidad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_modal.html');
	link.data('id_gra',$('#cat_grad_full-frm #id').val());
	curso_unidad_modal(link);
}

function curso_unidad_eliminar(link){
	_delete('api/cursoUnidad/' + $(link).data("id"),
			function(){
				curso_unidad_listar_tabla();
				}
			);
}
function curso_unidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_unidad-frm');
		
		
		
		$('#col_curso_unidad-frm #btn-agregar').on('click',function(event){
			$('#col_curso_unidad-frm #id').val('');
			_post($('#col_curso_unidad-frm').attr('action') , $('#col_curso_unidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoUnidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'),data.id_cur);	
						_llenarCombo('col_per_uni',$('#cat_grad-frm #id_cpu'),data.id_cpu);	
				}
			);
		}else{
			_llenarCombo('cat_nivel',$('#cat_grad-frm #id_niv'));
			_llenarCombo('cat_grad',$('#cat_grad-frm #id_gra'),$('#cat_grad_full-frm #id').val());
			_llenarCombo('cat_curso',$('#cat_grad-frm #id_cur'));
			_llenarCombo('col_per_uni',$('#cat_grad-frm #id_cpu'));
			$('#col_curso_unidad-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_unidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Did&aacute;ctica';
	else
		titulo = 'Nuevo  Unidad Did&aacute;ctica';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_unidad_listar_tabla(){
	_get('api/cursoUnidad/listar/',
			function(data){
			console.log(data);
				$('#col_curso_unidad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"N&uacute;mero de Unidad", "data" : "num"}, 
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Descripcion", "data" : "des"}, 
							{"title":"Semanas", "data" : "nro_sem"}, 
							{"title":"Producto", "data" : "producto"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Periodo Unidad", "data": "perUni.nump"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Unidad', 'curso_unidad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_gra":$('#col_curso_unidad-tabla').data('id_padre')}
	);

}	
