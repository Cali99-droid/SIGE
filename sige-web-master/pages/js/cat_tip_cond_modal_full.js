//edicion completa de una tabla
function tip_cond_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_tip_cond_full.html');
	tip_cond_full_modal(link);

}


function tip_cond_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#cat_cond_alumno-tabla').data('id_padre',link.data('id'));

		cond_alumno_listar_tabla();
		_inputs('cat_tip_cond_full-frm');
		
		$('#cat_tip_cond_full-frm #btn-agregar').on('click',function(event){
			$('#cat_tip_cond_full-frm #id').val('');
			_post($('#cat_tip_cond_full-frm').attr('action') , $('#cat_tip_cond_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tipCond/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#cat_tip_cond_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_cond_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de Condicion';
	else
		titulo = 'Nuevo  Tipo de Condicion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cond_alumno_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/catalogos/cat_cond_alumno_modal.html');
	cond_alumno_modal(link);
}

function cond_alumno_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_cond_alumno_modal.html');
	link.data('id_ctc',$('#cat_tip_cond_full-frm #id').val());
	cond_alumno_modal(link);
}

function cond_alumno_eliminar(link){
	_delete('api/condAlumno/' + $(link).data("id"),
			function(){
				cond_alumno_listar_tabla();
				}
			);
}
function cond_alumno_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('cat_cond_alumno-frm');
		
		
		
		$('#cat_cond_alumno-frm #btn-agregar').on('click',function(event){
			$('#cat_cond_alumno-frm #id').val('');
			_post($('#cat_cond_alumno-frm').attr('action') , $('#cat_cond_alumno-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/condAlumno/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_tip_cond',$('#cat_tip_cond-frm #id_ctc'),data.id_ctc);	
				}
			);
		}else{
			_llenarCombo('cat_tip_cond',$('#cat_tip_cond-frm #id_ctc'),$('#cat_tip_cond_full-frm #id').val());
			$('#cat_cond_alumno-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cond_alumno_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Condici&oacute;n del alumno';
	else
		titulo = 'Nuevo  Condici&oacute;n del alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cond_alumno_listar_tabla(){
	_get('api/condAlumno/listar/',
			function(data){
			console.log(data);
				$('#cat_cond_alumno-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Condicion", "data" : "nom"}, 
							{"title":"Descripci&oacute;n", "data" : "des"}, 
							{"title":"Tipo de Condicion", "data": "tipCond.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cond_alumno_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cond_alumno_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Condición del alumno', 'cond_alumno_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ctc":$('#cat_cond_alumno-tabla').data('id_padre')}
	);

}	
