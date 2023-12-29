//edicion completa de una tabla
function anio_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_anio_full.html');
	anio_full_modal(link);

}


function anio_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#mat_cronograma-tabla').data('id_padre',link.data('id'));
		$('#mat_conf_fechas-tabla').data('id_padre',link.data('id'));
		$('#per_periodo-tabla').data('id_padre',link.data('id'));
		$('#asi_grado_horario-tabla').data('id_padre',link.data('id'));
		$('#cat_grado_horario-tabla').data('id_padre',link.data('id'));
		$('#col_area_anio-tabla').data('id_padre',link.data('id'));
		$('#col_curso_horario-tabla').data('id_padre',link.data('id'));
		$('#col_indicador-tabla').data('id_padre',link.data('id'));
		$('#col_curso_subtema-tabla').data('id_padre',link.data('id'));
		$('#col_per_uni-tabla').data('id_padre',link.data('id'));

		cronograma_listar_tabla();
		conf_fechas_listar_tabla();
		periodo_listar_tabla();
		grado_horario_listar_tabla();
		grado_horario_listar_tabla();
		area_anio_listar_tabla();
		curso_horario_listar_tabla();
		indicador_listar_tabla();
		curso_subtema_listar_tabla();
		per_uni_listar_tabla();
		_inputs('col_anio_full-frm');
		
		$('#col_anio_full-frm #btn-agregar').on('click',function(event){
			$('#col_anio_full-frm #id').val('');
			_post($('#col_anio_full-frm').attr('action') , $('#col_anio_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/anio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#col_anio_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar A&ntilde;o Acad&eacute;mico';
	else
		titulo = 'Nuevo  A&ntilde;o Acad&eacute;mico';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cronograma_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_cronograma_modal.html');
	cronograma_modal(link);
}

function cronograma_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_cronograma_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	cronograma_modal(link);
}

function cronograma_eliminar(link){
	_delete('api/cronograma/' + $(link).data("id"),
			function(){
				cronograma_listar_tabla();
				}
			);
}
function cronograma_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_cronograma-frm');
		
		
		
		$('#mat_cronograma-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma-frm #id').val('');
			_post($('#mat_cronograma-frm').attr('action') , $('#mat_cronograma-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cronograma/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'),data.id_niv);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'));
			$('#mat_cronograma-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma de matr&iacute;cula';
	else
		titulo = 'Nuevo  Cronograma de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cronograma_listar_tabla(){
	_get('api/cronograma/listar/',
			function(data){
			console.log(data);
				$('#mat_cronograma-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha para matricula", "data" : "fec_mat"}, 
							{"title":"Del", "data" : "del"}, 
							{"title":"Al", "data" : "al"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Cronograma de matrícula', 'cronograma_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#mat_cronograma-tabla').data('id_padre')}
	);

}	

function conf_fechas_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_conf_fechas_modal.html');
	conf_fechas_modal(link);
}

function conf_fechas_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/mat_conf_fechas_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	conf_fechas_modal(link);
}

function conf_fechas_eliminar(link){
	_delete('api/confFechas/' + $(link).data("id"),
			function(){
				conf_fechas_listar_tabla();
				}
			);
}
function conf_fechas_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mat_conf_fechas-frm');
		
		
		
		$('#mat_conf_fechas-frm #btn-agregar').on('click',function(event){
			$('#mat_conf_fechas-frm #id').val('');
			_post($('#mat_conf_fechas-frm').attr('action') , $('#mat_conf_fechas-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/confFechas/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			$('#mat_conf_fechas-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_fechas_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma de matr&iacute;cula';
	else
		titulo = 'Nuevo  Cronograma de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function conf_fechas_listar_tabla(){
	_get('api/confFechas/listar/',
			function(data){
			console.log(data);
				$('#mat_conf_fechas-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tipo", "data" : "tipo"}, 
							{"title":"Del", "data" : "del"}, 
							{"title":"Al", "data" : "al"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_fechas_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_fechas_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Cronograma de matrícula', 'conf_fechas_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#mat_conf_fechas-tabla').data('id_padre')}
	);

}	

function periodo_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/$child.module.directory/per_periodo_modal.html');
	periodo_modal(link);
}

function periodo_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/$child.module.directory/per_periodo_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	periodo_modal(link);
}

function periodo_eliminar(link){
	_delete('api/periodo/' + $(link).data("id"),
			function(){
				periodo_listar_tabla();
				}
			);
}
function periodo_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('per_periodo-frm');
		
		
		
		$('#per_periodo-frm #btn-agregar').on('click',function(event){
			$('#per_periodo-frm #id').val('');
			_post($('#per_periodo-frm').attr('action') , $('#per_periodo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/periodo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('ges_servicio',$('#col_anio-frm #id_srv'),data.id_srv);	
						_llenarCombo('cat_tip_periodo',$('#col_anio-frm #id_tpe'),data.id_tpe);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('ges_servicio',$('#col_anio-frm #id_srv'));
			_llenarCombo('cat_tip_periodo',$('#col_anio-frm #id_tpe'));
			$('#per_periodo-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		periodo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo de estudio';
	else
		titulo = 'Nuevo  Periodo de estudio';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function periodo_listar_tabla(){
	_get('api/periodo/listar/',
			function(data){
			console.log(data);
				$('#per_periodo-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Sucursal", "data" : "id_suc"}, 
							{"title":"Nivel", "data" : "id_niv"}, 
							{"title":"Inicio", "data" : "fec_ini"}, 
							{"title":"Fin", "data" : "fec_fin"}, 
							{"title":"Fin Matr&iacute;cula", "data" : "fec_cie_mat"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Servicio", "data": "servicio.nom"}, 
							{"title":"Tipo de periodo", "data": "tipPeriodo.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="periodo_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="periodo_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Periodo', 'periodo_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#per_periodo-tabla').data('id_padre')}
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
	link.data('id_anio',$('#col_anio_full-frm #id').val());
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
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_anio-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('col_aula',$('#col_anio-frm #id_au'));
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
			},{"id_anio":$('#asi_grado_horario-tabla').data('id_padre')}
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
	link.data('id_anio',$('#col_anio_full-frm #id').val());
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
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_aula',$('#col_anio-frm #id_au'),data.id_au);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('col_aula',$('#col_anio-frm #id_au'));
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
			},{"id_anio":$('#cat_grado_horario-tabla').data('id_padre')}
	);

}	

function area_anio_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_modal.html');
	area_anio_modal(link);
}

function area_anio_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	area_anio_modal(link);
}

function area_anio_eliminar(link){
	_delete('api/areaAnio/' + $(link).data("id"),
			function(){
				area_anio_listar_tabla();
				}
			);
}
function area_anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_area_anio-frm');
		
		
		
		$('#col_area_anio-frm #btn-agregar').on('click',function(event){
			$('#col_area_anio-frm #id').val('');
			_post($('#col_area_anio-frm').attr('action') , $('#col_area_anio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/areaAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_area',$('#col_anio-frm #id_area'),data.id_area);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'));
			_llenarCombo('cat_area',$('#col_anio-frm #id_area'));
			$('#col_area_anio-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar &Aacute;reas Educativas';
	else
		titulo = 'Nuevo  &Aacute;reas Educativas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function area_anio_listar_tabla(){
	_get('api/areaAnio/listar/',
			function(data){
			console.log(data);
				$('#col_area_anio-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Orden", "data" : "ord"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Area", "data": "area.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="area_anio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="area_anio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Áreas', 'area_anio_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#col_area_anio-tabla').data('id_padre')}
	);

}	

function curso_horario_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_modal.html');
	curso_horario_modal(link);
}

function curso_horario_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	curso_horario_modal(link);
}

function curso_horario_eliminar(link){
	_delete('api/cursoHorario/' + $(link).data("id"),
			function(){
				curso_horario_listar_tabla();
				}
			);
}
function curso_horario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_curso_horario-frm');
		
		
		
		$('#col_curso_horario-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario-frm #id').val('');
			_post($('#col_curso_horario-frm').attr('action') , $('#col_curso_horario-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoHorario/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('col_curso_aula',$('#col_anio-frm #id_cca'),data.id_cca);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('col_curso_aula',$('#col_anio-frm #id_cca'));
			$('#col_curso_horario-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_horario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Horario';
	else
		titulo = 'Nuevo  Curso Horario';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function curso_horario_listar_tabla(){
	_get('api/cursoHorario/listar/',
			function(data){
			console.log(data);
				$('#col_curso_horario-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Dia", "data" : "dia"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Curso Aula", "data": "cursoAula.id_cua"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Curso Horario', 'curso_horario_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#col_curso_horario-tabla').data('id_padre')}
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
	link.data('id_anio',$('#col_anio_full-frm #id').val());
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
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_grad',$('#col_anio-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_capacidad',$('#col_anio-frm #id_cap'),data.id_cap);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('cat_grad',$('#col_anio-frm #id_gra'));
			_llenarCombo('col_capacidad',$('#col_anio-frm #id_cap'));
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
			},{"id_anio":$('#col_indicador-tabla').data('id_padre')}
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
	link.data('id_anio',$('#col_anio_full-frm #id').val());
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
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_grad',$('#col_anio-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_curso',$('#col_anio-frm #id_cur'),data.id_cur);	
						_llenarCombo('col_subtema',$('#col_anio-frm #id_sub'),data.id_sub);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			_llenarCombo('cat_nivel',$('#col_anio-frm #id_niv'));
			_llenarCombo('cat_grad',$('#col_anio-frm #id_gra'));
			_llenarCombo('cat_curso',$('#col_anio-frm #id_cur'));
			_llenarCombo('col_subtema',$('#col_anio-frm #id_sub'));
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
			},{"id_anio":$('#col_curso_subtema-tabla').data('id_padre')}
	);

}	

function per_uni_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_modal.html');
	per_uni_modal(link);
}

function per_uni_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_modal.html');
	link.data('id_anio',$('#col_anio_full-frm #id').val());
	per_uni_modal(link);
}

function per_uni_eliminar(link){
	_delete('api/perUni/' + $(link).data("id"),
			function(){
				per_uni_listar_tabla();
				}
			);
}
function per_uni_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_per_uni-frm');
		
		
		
		$('#col_per_uni-frm #btn-agregar').on('click',function(event){
			$('#col_per_uni-frm #id').val('');
			_post($('#col_per_uni-frm').attr('action') , $('#col_per_uni-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perUni/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_per_aca_nivel',$('#col_anio-frm #id_cpan'),data.id_cpan);	
						_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),data.id_anio);	
				}
			);
		}else{
			_llenarCombo('cat_per_aca_nivel',$('#col_anio-frm #id_cpan'));
			_llenarCombo('col_anio',$('#col_anio-frm #id_anio'),$('#col_anio_full-frm #id').val());
			$('#col_per_uni-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_uni_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Unidad';
	else
		titulo = 'Nuevo  Periodo Unidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function per_uni_listar_tabla(){
	_get('api/perUni/listar/',
			function(data){
			console.log(data);
				$('#col_per_uni-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Numero de Periodo", "data" : "nump"}, 
							{"title":"N&uacute;mero de Unidad Inicio", "data" : "numu_ini"}, 
							{"title":"N&uacute;mero de Unidad Fin", "data" : "numu_fin"}, 
							{"title":"Fecha Inicio", "data" : "fec_ini"}, 
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							{"title":"Periodo Acad&eacute;mico Nivel", "data": "perAcaNivel.id_niv"}, 
							{"title":"Anio", "data": "anio.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Periodo Unidad', 'per_uni_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_anio":$('#col_per_uni-tabla').data('id_padre')}
	);

}	
