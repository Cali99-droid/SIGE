//edicion completa de una tabla
function central_riesgo_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_central_riesgo_full.html');
	central_riesgo_full_modal(link);

}


function central_riesgo_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_historial_eco-tabla').data('id_padre',link.data('id'));
		$('#col_historial_eco-tabla').data('id_padre',link.data('id'));

		historial_eco_listar_tabla();
		historial_eco_listar_tabla();
		_inputs('cat_central_riesgo_full-frm');
		
		$('#cat_central_riesgo_full-frm #btn-agregar').on('click',function(event){
			$('#cat_central_riesgo_full-frm #id').val('');
			_post($('#cat_central_riesgo_full-frm').attr('action') , $('#cat_central_riesgo_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/centralRiesgo/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#cat_central_riesgo_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		central_riesgo_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Central Riesgo';
	else
		titulo = 'Nuevo  Central Riesgo';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function historial_eco_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_modal.html');
	historial_eco_modal(link);
}

function historial_eco_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_modal.html');
	link.data('id_ccr',$('#cat_central_riesgo_full-frm #id').val());
	historial_eco_modal(link);
}

function historial_eco_eliminar(link){
	_delete('api/historialEco/' + $(link).data("id"),
			function(){
				historial_eco_listar_tabla();
				}
			);
}
function historial_eco_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_historial_eco-frm');
		
		
		
		$('#col_historial_eco-frm #btn-agregar').on('click',function(event){
			$('#col_historial_eco-frm #id').val('');
			_post($('#col_historial_eco-frm').attr('action') , $('#col_historial_eco-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/historialEco/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#cat_central_riesgo-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),data.id_ccr);	
						_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),data.id_ccr);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#cat_central_riesgo-frm #id_mat'));
			_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),$('#cat_central_riesgo_full-frm #id').val());
			_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),$('#cat_central_riesgo_full-frm #id').val());
			$('#col_historial_eco-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		historial_eco_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Historial Economico Alumno';
	else
		titulo = 'Nuevo  Historial Economico Alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function historial_eco_listar_tabla(){
	_get('api/historialEco/listar/',
			function(data){
			console.log(data);
				$('#col_historial_eco-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Utlima Mensualidad Cancelada", "data" : "ult_men"}, 
							{"title":"N&uacute;mero de Mensualidades que adeuda ", "data" : "nro_mens"}, 
							{"title":"Ingresos Familiares", "data" : "ing_fam"}, 
							{"title":"Puntaje", "data" : "puntaje"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Central Riesgo Padre", "data": "centralRiesgo.nom"}, 
							{"title":"Central Riesgo Madre", "data": "centralRiesgo.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Historial Economico Alumno', 'historial_eco_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ccr":$('#col_historial_eco-tabla').data('id_padre')}
	);

}	

function historial_eco_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_modal.html');
	historial_eco_modal(link);
}

function historial_eco_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_historial_eco_modal.html');
	link.data('id_ccr',$('#cat_central_riesgo_full-frm #id').val());
	historial_eco_modal(link);
}

function historial_eco_eliminar(link){
	_delete('api/historialEco/' + $(link).data("id"),
			function(){
				historial_eco_listar_tabla();
				}
			);
}
function historial_eco_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_historial_eco-frm');
		
		
		
		$('#col_historial_eco-frm #btn-agregar').on('click',function(event){
			$('#col_historial_eco-frm #id').val('');
			_post($('#col_historial_eco-frm').attr('action') , $('#col_historial_eco-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/historialEco/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_matricula',$('#cat_central_riesgo-frm #id_mat'),data.id_mat);	
						_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),data.id_ccr);	
						_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),data.id_ccr);	
				}
			);
		}else{
			_llenarCombo('mat_matricula',$('#cat_central_riesgo-frm #id_mat'));
			_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),$('#cat_central_riesgo_full-frm #id').val());
			_llenarCombo('cat_central_riesgo',$('#cat_central_riesgo-frm #id_ccr'),$('#cat_central_riesgo_full-frm #id').val());
			$('#col_historial_eco-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		historial_eco_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Historial Economico Alumno';
	else
		titulo = 'Nuevo  Historial Economico Alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function historial_eco_listar_tabla(){
	_get('api/historialEco/listar/',
			function(data){
			console.log(data);
				$('#col_historial_eco-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Utlima Mensualidad Cancelada", "data" : "ult_men"}, 
							{"title":"N&uacute;mero de Mensualidades que adeuda ", "data" : "nro_mens"}, 
							{"title":"Ingresos Familiares", "data" : "ing_fam"}, 
							{"title":"Puntaje", "data" : "puntaje"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Central Riesgo Padre", "data": "centralRiesgo.nom"}, 
							{"title":"Central Riesgo Madre", "data": "centralRiesgo.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="historial_eco_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Historial Economico Alumno', 'historial_eco_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ccr":$('#col_historial_eco-tabla').data('id_padre')}
	);

}	
