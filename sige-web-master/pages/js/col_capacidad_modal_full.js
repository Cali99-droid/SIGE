//edicion completa de una tabla
function capacidad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_capacidad_full.html');
	capacidad_full_modal(link);

}


function capacidad_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_indicador-tabla').data('id_padre',link.data('id'));
		$('#col_subtema_capacidad-tabla').data('id_padre',link.data('id'));

		indicador_listar_tabla();
		subtema_capacidad_listar_tabla();
		_inputs('col_capacidad_full-frm');
		
		$('#col_capacidad_full-frm #btn-agregar').on('click',function(event){
			$('#col_capacidad_full-frm #id').val('');
			_post($('#col_capacidad_full-frm').attr('action') , $('#col_capacidad_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/capacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_competencia',$('#id_com-frm #id_com'),data.id_com);	
						}
			);
		}else{
					_llenarCombo('col_competencia',$('#id_com-frm #id_com'));
				}
		$('#col_capacidad_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Capacidad';
	else
		titulo = 'Nuevo  Capacidad';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

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
	link.data('id_cap',$('#col_capacidad_full-frm #id').val());
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
						_llenarCombo('col_anio',$('#id_anio-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_grad',$('#id_gra-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'),data.id_cap);	
				}
			);
		}else{
			_llenarCombo('col_anio',$('#id_anio-frm #id_anio'));
			_llenarCombo('cat_grad',$('#id_gra-frm #id_gra'));
			_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'));
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
			},{"id_cap":$('#col_indicador-tabla').data('id_padre')}
	);

}	

function subtema_capacidad_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_capacidad_modal.html');
	subtema_capacidad_modal(link);
}

function subtema_capacidad_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_capacidad_modal.html');
	link.data('id_cap',$('#col_capacidad_full-frm #id').val());
	subtema_capacidad_modal(link);
}

function subtema_capacidad_eliminar(link){
	_delete('api/subtemaCapacidad/' + $(link).data("id"),
			function(){
				subtema_capacidad_listar_tabla();
				}
			);
}
function subtema_capacidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_subtema_capacidad-frm');
		
		
		
		$('#col_subtema_capacidad-frm #btn-agregar').on('click',function(event){
			$('#col_subtema_capacidad-frm #id').val('');
			_post($('#col_subtema_capacidad-frm').attr('action') , $('#col_subtema_capacidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/subtemaCapacidad/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_curso_subtema',$('#id_ccs-frm #id_ccs'),data.id_ccs);	
						_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'),data.id_cap);	
				}
			);
		}else{
			_llenarCombo('col_curso_subtema',$('#id_ccs-frm #id_ccs'));
			_llenarCombo('col_capacidad',$('#id_cap-frm #id_cap'));
			$('#col_subtema_capacidad-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_capacidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar La relacion de tema capacidad nos da la programacion anual';
	else
		titulo = 'Nuevo  La relacion de tema capacidad nos da la programacion anual';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function subtema_capacidad_listar_tabla(){
	_get('api/subtemaCapacidad/listar/',
			function(data){
			console.log(data);
				$('#col_subtema_capacidad-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Curso Subtema", "data": "cursoSubtema.dur"}, 
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_capacidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="subtema_capacidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Subtema Capacidad', 'subtema_capacidad_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cap":$('#col_subtema_capacidad-tabla').data('id_padre')}
	);

}	
