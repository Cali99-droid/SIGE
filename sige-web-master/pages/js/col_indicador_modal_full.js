//edicion completa de una tabla
function indicador_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_indicador_full.html');
	indicador_full_modal(link);

}


function indicador_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_sesion_indicador-tabla').data('id_padre',link.data('id'));

		sesion_indicador_listar_tabla();
		_inputs('col_indicador_full-frm');
		
		$('#col_indicador_full-frm #btn-agregar').on('click',function(event){
			$('#col_indicador_full-frm #id').val('');
			_post($('#col_indicador_full-frm').attr('action') , $('#col_indicador_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/indicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_anio',$('#$child.dataName-frm #id_anio'),data.id_anio);	
						_llenarCombo('cat_grad',$('#$child.dataName-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_capacidad',$('#$child.dataName-frm #id_cap'),data.id_cap);	
						}
			);
		}else
			$('#col_indicador_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_indicador_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_indicador_modal.html');
	sesion_indicador_modal(link);
}

function sesion_indicador_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_sesion_indicador_modal.html');
	link.data('id_ind',$('#col_indicador_full-frm #id').val());
	sesion_indicador_modal(link);
}

function sesion_indicador_eliminar(link){
	_delete('api/sesionIndicador/' + $(link).data("id"),
			function(){
				sesion_indicador_listar_tabla();
				}
			);
}
function sesion_indicador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_sesion_indicador-frm');
		
		
		
		$('#col_sesion_indicador-frm #btn-agregar').on('click',function(event){
			$('#col_sesion_indicador-frm #id').val('');
			_post($('#col_sesion_indicador-frm').attr('action') , $('#col_sesion_indicador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/sesionIndicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_unidad_sesion',$('#col_sesion_indicador-frm #id_ses'),data.id_ses);	
						_llenarCombo('col_indicador',$('#col_sesion_indicador-frm #id_ind'),data.id_ind);	
				}
			);
		}else
			$('#col_sesion_indicador-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		sesion_indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicadores que se trabajara en la sesion';
	else
		titulo = 'Nuevo  Indicadores que se trabajara en la sesion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function sesion_indicador_listar_tabla(){
	_get('api/sesionIndicador/listar/',
			function(data){
			console.log(data);
				$('#col_sesion_indicador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Sesion", "data": "unidadSesion.tit"}, 
							{"title":"Indicador", "data": "indicador.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_indicador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="sesion_indicador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Sesion Indicador', 'sesion_indicador_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ind":$('#col_sesion_indicador-tabla').data('id_padre')}
	);

}	
