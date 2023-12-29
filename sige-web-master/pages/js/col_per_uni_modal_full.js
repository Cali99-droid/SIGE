//edicion completa de una tabla
function per_uni_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_full.html');
	per_uni_full_modal(link);

}


function per_uni_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_curso_unidad-tabla').data('id_padre',link.data('id'));

		curso_unidad_listar_tabla();
		_inputs('col_per_uni_full-frm');
		
		$('#col_per_uni_full-frm #btn-agregar').on('click',function(event){
			$('#col_per_uni_full-frm #id').val('');
			_post($('#col_per_uni_full-frm').attr('action') , $('#col_per_uni_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/perUni/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_per_aca_nivel',$('#col_per_uni-frm #id_cpan'),data.id_cpan);	
						}
			);
		}else{
					_llenarCombo('cat_per_aca_nivel',$('#col_per_uni-frm #id_cpan'));
				}
		$('#col_per_uni_full-frm #btn-grabar').hide();
		
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
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

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
	link.data('id_cpu',$('#col_per_uni_full-frm #id').val());
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
						_llenarCombo('cat_nivel',$('#col_per_uni-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_grad',$('#col_per_uni-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_curso',$('#col_per_uni-frm #id_cur'),data.id_cur);	
						_llenarCombo('col_per_uni',$('#col_per_uni-frm #id_cpu'),data.id_cpu);	
				}
			);
		}else{
			_llenarCombo('cat_nivel',$('#col_per_uni-frm #id_niv'));
			_llenarCombo('cat_grad',$('#col_per_uni-frm #id_gra'));
			_llenarCombo('cat_curso',$('#col_per_uni-frm #id_cur'));
			_llenarCombo('col_per_uni',$('#col_per_uni-frm #id_cpu'),$('#col_per_uni_full-frm #id').val());
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
			},{"id_cpu":$('#col_curso_unidad-tabla').data('id_padre')}
	);

}	
