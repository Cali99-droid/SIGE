var _id_tem;
var _id_curso;
function onloadPage(params){
	var id = params.id;
	var curso = params.curso;
	_id_curso = id;
	$('#col_tema-tabla').data('id_padre',id);
	$('#col_curso_anio-tabla').data('id_padre',id);
	$('#cat_curso_full-frm #nom').val(curso);
	
	curso_anio_listar_tabla();
	tema_listar_tabla();

	_inputs('cat_curso_full-frm');
	
	
	$('#cat_curso_full-frm #btn-agregar').on('click',function(event){
		$('#cat_curso_full-frm #id').val('');
		_post($('#cat_curso_full-frm').attr('action') , $('#cat_curso_full-frm').serialize(),
		function(data){
				onSuccessSave(data) ;
			}
		);
	});

	
		_get('api/curso/' + id,
		function(data){
			_fillForm(data,$('#modal_full').find('form') );
			}
		);

}

$('#btn-clonar').on('click',function(event){
	event.preventDefault();	
	var id_cur=$('#col_tema-tabla').data('id_padre');
	_post('api/curso/clonar/'+id_cur+'/'+$('#_id_anio').text(),null,
			function(data){
			tema_listar_tabla();
	});
	
});

function _onchangeAnio(id_anio, anio){
	tema_listar_tabla();
}

function curso_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_curso_full.html');
	curso_full_modal(link);

}



function curso_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		
		//$('#col_tema-tabla').data('id_padre',link.data('id'));
		//$('#col_curso_anio-tabla').data('id_padre',link.data('id'));

		$('#col_tema-tabla').data('id_padre','1');
		$('#col_curso_anio-tabla').data('id_padre','1');

		
		curso_anio_listar_tabla();
		tema_listar_tabla();

		_inputs('cat_curso_full-frm');
		
		
		$('#cat_curso_full-frm #btn-agregar').on('click',function(event){
			$('#cat_curso_full-frm #id').val('');
			_post($('#cat_curso_full-frm').attr('action') , $('#cat_curso_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
			_get('api/curso/' + '1',
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_listar_tabla();
	}
	
	//Abrir el modal
	var titulo = 'Editar Curso';
	
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
	link.data('id_cur',$('#cat_curso_full-frm #id').val());
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
				_llenarCombo('per_periodo',$('#col_curso_anio-frm #id_per'));	
				_llenarCombo('cat_grad',$('#col_curso_anio-frm #id_gra'));	
				_llenarCombo('col_area_anio',$('#col_curso_anio-frm #id_caa'));	
				_llenarCombo('cat_curso',$('#col_curso_anio-frm #id_cur'),link.data('id_cur'));

				}
			);
		}
		//else
			//$('#col_curso_anio-frm #btn-grabar').hide();
		
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
					   _agregarIcono(settings, 'Agregar Curso por aï¿½o', 'curso_anio_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cur":$('#col_curso_anio-tabla').data('id_padre')}
	);

}	

function tema_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_subtema-tabla').data('id_padre',link.data('id'));
		
		subtema_listar_tabla();
		_inputs('col_tema_full-frm');
		
		
		$('#col_tema_full-frm #btn-agregar').on('click',function(event){
			$('#col_tema_full-frm #id').val('');
			_post($('#col_tema_full-frm').attr('action') , $('#col_tema_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					_llenarCombo('cat_nivel',$('#col_tema_full-frm #id_niv'), data.id_niv);//esto deberia ser auto-gen
					_llenarCombo('cat_curso',$('#col_tema_full-frm #id_cur'), data.id_cur);//ya grabe, ya debe estar en el

				}
			);
		});

		
		if (link.data('id')){
			_get('api/tema/' + link.data('id'),
			function(data){
				_llenarCombo('cat_nivel',$('#col_tema_full-frm #id_niv'), data.id_niv);//esto deberia ser auto-gen
				_llenarCombo('cat_curso',$('#col_tema_full-frm #id_cur'), data.id_cur);//ya grabe, ya debe estar en el
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else{
			$('#col_tema_full-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tema a tratarse por curso';
	else
		titulo = 'Nuevo  Tema a tratarse por curso';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_tema_modal.html');
	tema_modal(link);
}

function tema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_tema_modal.html');
	link.data('id_cur',_id_curso);
	tema_modal(link);
}

function tema_eliminar(link){
	_delete('api/tema/' + $(link).data("id"),
			function(){
				tema_listar_tabla();
				}
			);
}
function tema_modal(link){
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_tema-frm');
		$('#col_tema-frm #id_anio').val($('#_id_anio').text());
		$('#col_tema-frm #btn-agregar').on('click',function(event){
			$('#col_tema-frm #id').val('');
			$('#col_tema-frm #id_cur').attr('disabled',false);
			_post($('#col_tema-frm').attr('action') , $('#col_tema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
			$('#col_tema-frm #id_cur').attr('disabled',true);
		});
		
		if (link.data('id')){
			
			_get('api/tema/' + link.data('id'),
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form') );
					_llenarCombo('cat_nivel',$('#col_tema-frm #id_niv'),data.id_niv);	//esto fue la correccion para editar
					_llenarCombo('cat_curso',$('#col_tema-frm #id_cur'),data.id_cur); //pero hay dos combos
					$('#col_tema-frm #id_cur').attr('disabled',true); 
					//_llenarCombo('cat_nivel',$('#col_tema_full-frm #id_niv'), data.id_niv);//esto deberia ser auto-gen
					//_llenarCombo('cat_curso',$('#col_tema_full-frm #id_cur'), data.id_cur);//ya grabe, ya debe estar en el

				}
			);
		}else{
			_llenarCombo('cat_nivel',$('#col_tema-frm #id_niv'));	
			_llenarCombo('cat_curso',$('#col_tema-frm #id_cur'),_id_curso);//pruebalo
			$('#col_tema-frm #id_cur').attr('disabled',true); 
			$('#col_tema-frm #btn-grabar').hide();}
		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tema a tratarse por curso';
	else
		titulo = 'Nuevo  Tema a tratarse por curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function tema_listar_tabla(){

	var param = {id_anio:$('#_id_anio').text(), id_cur:$('#col_tema-tabla').data('id_padre')};
	console.log(param);
	_get('api/tema/listar/',
			function(data){
			console.log(data);
			if(data.length==0){
				$("#btn-clonar").css('display','inline-block');
				new PNotify({
			         title: 'Ayuda',
			         text: 'Se puede clonar la configuración del año anterior, en el botón "Clonar desde Año Anterior"',
			         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
			         delay:10000
			     });
			} else{
				$("#btn-clonar").css('display','none');
			}
				$('#col_tema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Nivel", "data": "nivel.nom"}, 
						//	{"title":"Curso", "data": "curso.nom"},
							{"title":"Tema", "data" : "nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="tema_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="tema_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'+
								' <a href="#" data-id="' + row.id + '" onclick="tema_editarFull(this, event)" class="list-icons-item" title="Configuraci&oacute;n de Subtemas"><i class="fa fa-list-alt mr-2 ui-blue ui-size" aria-hidden="true"></i></a></div>';
							}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Tema', 'tema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},param
	);

}

function tema_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_tema_full.html');
	tema_full_modal(link);

}

function subtema_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_modal.html');
	subtema_modal(link);
}

function subtema_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_subtema_modal.html');
	link.data('id_tem',$('#col_tema_full-frm #id').val());
	subtema_modal(link);
}

function subtema_eliminar(link){
	_delete('api/subtema/' + $(link).data("id"),
			function(){
				subtema_listar_tabla();
				}
			);
}
function subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_subtema-frm');
		

		$('#col_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_subtema-frm #id').val('');
			//$('#col_subtema-frm #id_tem').attr('disabled',false);//COMBO HABILITADO
			_post($('#col_subtema-frm').attr('action') , $('#col_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
			//$('#col_subtema-frm #id_tem').attr('disabled',true);
		});
		
		if (link.data('id')){
			_get('api/subtema/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				$('#col_subtema-frm #id_tem').val(link.data('id_tem'));
				$('#col_subtema-frm #nom_tem').val(link.data('tema'));
			//	_llenarCombo('col_tema',$('#col_subtema-frm #id_tem'),data.id_tem);
				//$('#col_subtema-frm #id_tem').attr('disabled',true);//COMBO DESHABILITADO
				}
			);
		}else{
			_id_tem=$('#col_tema_full-frm #id').val();
			
			$('#col_subtema-frm #id_tem').val(_id_tem);
			$('#col_subtema-frm #nom_tem').val($('#col_tema_full-frm #nom').val());
			//_llenarCombo('col_tema',$('#col_subtema-frm #id_tem'),_id_tem);
			//$('#col_subtema-frm #id_tem').attr('disabled',true);
			$('#col_subtema-frm #btn-grabar').hide();
		}
			
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Subtema por Tema';
	else
		titulo = 'Nuevo  Subtema por Tema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function subtema_listar_tabla(){
	_get('api/subtema/listar/',
			function(data){
			console.log(data);
				$('#col_subtema-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Tema", "data": "tema.nom"}, 
							{"title":"Subtema", "data" : "nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Observacion", "data" : "obs"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" data-tema="'+row.tema.nom+'" data-id_tem="' + row.tema.id + '" onclick="subtema_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="subtema_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Subtema', 'subtema_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_tem":$('#col_subtema-tabla').data('id_padre')}
	);

}	

