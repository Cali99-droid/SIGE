//edicion completa de una tabla
function reporte_conductual_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_conductual_full.html');
	reporte_conductual_full_modal(link);

}


function reporte_conductual_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_reporte_infractor-tabla').data('id_padre',link.data('id'));
		$('#col_reporte_falta-tabla').data('id_padre',link.data('id'));

		reporte_infractor_listar_tabla();
		reporte_falta_listar_tabla();
		_inputs('col_reporte_conductual_full-frm');
		
		$('#col_reporte_conductual_full-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_conductual_full-frm #id').val('');
			_post($('#col_reporte_conductual_full-frm').attr('action') , $('#col_reporte_conductual_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/reporteConductual/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('per_periodo',$('#col_reporte_conductual-frm #id_per'),data.id_per);	
						_llenarCombo('seg_usuario',$('#col_reporte_conductual-frm #id_usr'),data.id_usr);	
						_llenarCombo('col_curso_aula',$('#col_reporte_conductual-frm #id_cca'),data.id_cca);	
						}
			);
		}else{
					_llenarCombo('per_periodo',$('#col_reporte_conductual-frm #id_per'));
					_llenarCombo('seg_usuario',$('#col_reporte_conductual-frm #id_usr'));
					_llenarCombo('col_curso_aula',$('#col_reporte_conductual-frm #id_cca'));
				}
		$('#col_reporte_conductual_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_conductual_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reporte Conductual';
	else
		titulo = 'Nuevo  Reporte Conductual';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reporte_infractor_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_infractor_modal.html');
	reporte_infractor_modal(link);
}

function reporte_infractor_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_infractor_modal.html');
	link.data('id_cp',$('#col_reporte_conductual_full-frm #id').val());
	reporte_infractor_modal(link);
}

function reporte_infractor_eliminar(link){
	_delete('api/reporteInfractor/' + $(link).data("id"),
			function(){
				reporte_infractor_listar_tabla();
				}
			);
}
function reporte_infractor_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_reporte_infractor-frm');
		
		
		
		$('#col_reporte_infractor-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_infractor-frm #id').val('');
			_post($('#col_reporte_infractor-frm').attr('action') , $('#col_reporte_infractor-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reporteInfractor/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_reporte_conductual',$('#col_reporte_conductual-frm #id_cp'),data.id_cp);	
						_llenarCombo('mat_matricula',$('#col_reporte_conductual-frm #id_mat'),data.id_mat);	
				}
			);
		}else{
			_llenarCombo('col_reporte_conductual',$('#col_reporte_conductual-frm #id_cp'),$('#col_reporte_conductual_full-frm #id').val());
			_llenarCombo('mat_matricula',$('#col_reporte_conductual-frm #id_mat'));
			$('#col_reporte_infractor-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_infractor_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Infractor del Reporte Conductual';
	else
		titulo = 'Nuevo  Infractor del Reporte Conductual';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reporte_infractor_listar_tabla(){
	_get('api/reporteInfractor/listar/',
			function(data){
			console.log(data);
				$('#col_reporte_infractor-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Reporte Conductual", "data": "reporteConductual.cod"}, 
							{"title":"Matricula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_infractor_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_infractor_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Infractor del Reporte Conductual', 'reporte_infractor_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cp":$('#col_reporte_infractor-tabla').data('id_padre')}
	);

}	

function reporte_falta_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_falta_modal.html');
	reporte_falta_modal(link);
}

function reporte_falta_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_falta_modal.html');
	link.data('id_cp',$('#col_reporte_conductual_full-frm #id').val());
	reporte_falta_modal(link);
}

function reporte_falta_eliminar(link){
	_delete('api/reporteFalta/' + $(link).data("id"),
			function(){
				reporte_falta_listar_tabla();
				}
			);
}
function reporte_falta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_reporte_falta-frm');
		
		
		
		$('#col_reporte_falta-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_falta-frm #id').val('');
			_post($('#col_reporte_falta-frm').attr('action') , $('#col_reporte_falta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reporteFalta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_reporte_conductual',$('#col_reporte_conductual-frm #id_cp'),data.id_cp);	
						_llenarCombo('cat_falta',$('#col_reporte_conductual-frm #id_cf'),data.id_cf);	
				}
			);
		}else{
			_llenarCombo('col_reporte_conductual',$('#col_reporte_conductual-frm #id_cp'),$('#col_reporte_conductual_full-frm #id').val());
			_llenarCombo('cat_falta',$('#col_reporte_conductual-frm #id_cf'));
			$('#col_reporte_falta-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reporte Falta';
	else
		titulo = 'Nuevo  Reporte Falta';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reporte_falta_listar_tabla(){
	_get('api/reporteFalta/listar/',
			function(data){
			console.log(data);
				$('#col_reporte_falta-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Reporte Conductual", "data": "reporteConductual.cod"}, 
							{"title":"Falta", "data": "falta.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_falta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_falta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Reporte Falta', 'reporte_falta_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cp":$('#col_reporte_falta-tabla').data('id_padre')}
	);

}	
