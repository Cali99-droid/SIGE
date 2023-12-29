//edicion completa de una tabla
function cond_matricula_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_cond_matricula_full.html');
	cond_matricula_full_modal(link);

}


function cond_matricula_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#mat_reserva-tabla').data('id_padre',link.data('id'));
		$('#mat_matricula-tabla').data('id_padre',link.data('id'));

		reserva_listar_tabla();
		matricula_listar_tabla();
		_inputs('cat_cond_matricula_full-frm');
		
		$('#cat_cond_matricula_full-frm #btn-agregar').on('click',function(event){
			$('#cat_cond_matricula_full-frm #id').val('');
			_post($('#cat_cond_matricula_full-frm').attr('action') , $('#cat_cond_matricula_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/condMatricula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#cat_cond_matricula_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cond_matricula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Condicion de matricula';
	else
		titulo = 'Nuevo  Condicion de matricula';
	
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
	link.data('id_cma',$('#cat_cond_matricula_full-frm #id').val());
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
						_llenarCombo('alu_alumno',$('#cat_cond_matricula-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#cat_cond_matricula-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#cat_cond_matricula-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#cat_cond_matricula-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_cond_matricula',$('#cat_cond_matricula-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#cat_cond_matricula-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#cat_cond_matricula-frm #id_per'),data.id_per);	
						_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'),data.id_fam);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#cat_cond_matricula-frm #id_alu'));
			_llenarCombo('col_aula',$('#cat_cond_matricula-frm #id_au'));
			_llenarCombo('cat_grad',$('#cat_cond_matricula-frm #id_gra'));
			_llenarCombo('cat_nivel',$('#cat_cond_matricula-frm #id_niv'));
			_llenarCombo('cat_cond_matricula',$('#cat_cond_matricula-frm #id_cma'),$('#cat_cond_matricula_full-frm #id').val());
			_llenarCombo('cat_cliente',$('#cat_cond_matricula-frm #id_cli'));
			_llenarCombo('per_periodo',$('#cat_cond_matricula-frm #id_per'));
			_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'));
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
			},{"id_cma":$('#mat_reserva-tabla').data('id_padre')}
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
	link.data('id_cma',$('#cat_cond_matricula_full-frm #id').val());
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
						_llenarCombo('alu_alumno',$('#cat_cond_matricula-frm #id_alu'),data.id_alu);	
						_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'),data.id_fam);	
						_llenarCombo('cat_cond_matricula',$('#cat_cond_matricula-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#cat_cond_matricula-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#cat_cond_matricula-frm #id_per'),data.id_per);	
						_llenarCombo('col_aula',$('#cat_cond_matricula-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#cat_cond_matricula-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#cat_cond_matricula-frm #id_niv'),data.id_niv);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#cat_cond_matricula-frm #id_alu'));
			_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'));
			_llenarCombo('alu_familiar',$('#cat_cond_matricula-frm #id_fam'));
			_llenarCombo('cat_cond_matricula',$('#cat_cond_matricula-frm #id_cma'),$('#cat_cond_matricula_full-frm #id').val());
			_llenarCombo('cat_cliente',$('#cat_cond_matricula-frm #id_cli'));
			_llenarCombo('per_periodo',$('#cat_cond_matricula-frm #id_per'));
			_llenarCombo('col_aula',$('#cat_cond_matricula-frm #id_au'));
			_llenarCombo('cat_grad',$('#cat_cond_matricula-frm #id_gra'));
			_llenarCombo('cat_nivel',$('#cat_cond_matricula-frm #id_niv'));
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
			},{"id_cma":$('#mat_matricula-tabla').data('id_padre')}
	);

}	
