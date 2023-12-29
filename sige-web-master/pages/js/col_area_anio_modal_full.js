//edicion completa de una tabla
function area_anio_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_full.html');
	area_anio_full_modal(link);

}


function area_anio_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_curso_anio-tabla').data('id_padre',link.data('id'));

		curso_anio_listar_tabla();
		_inputs('col_area_anio_full-frm');
		
		_llenarCombo('col_anio',$('#col_area_anio_full-frm #id_anio'));
		_llenarCombo('cat_nivel',$('#col_area_anio_full-frm #id_niv'));
		_llenarCombo('cat_area',$('#col_area_anio_full-frm #id_area'));
		
		$('#col_area_anio_full-frm #btn-agregar').on('click',function(event){
			$('#col_area_anio_full-frm #id').val('');
			_post($('#col_area_anio_full-frm').attr('action') , $('#col_area_anio_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/areaAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else
			$('#col_area_anio_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

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
	link.data('id_caa',$('#col_area_anio_full-frm #id').val());
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
		
		_llenarCombo('per_periodo',$('#col_curso_anio-frm #id_per'));	
		_llenarCombo('cat_grad',$('#col_curso_anio-frm #id_gra'));	
		_llenarCombo('col_area_anio',$('#col_curso_anio-frm #id_caa'),link.data('id_caa'));
		_llenarCombo('cat_curso',$('#col_curso_anio-frm #id_cur'));	
		
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
				}
			);
		}else
			$('#col_curso_anio-frm #btn-grabar').hide();
		
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
			},{"id_caa":$('#col_curso_anio-tabla').data('id_padre')}
	);

}	
