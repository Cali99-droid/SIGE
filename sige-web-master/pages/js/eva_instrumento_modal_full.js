//edicion completa de una tabla
function instrumento_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_instrumento_full.html');
	instrumento_full_modal(link);

}


function instrumento_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#eva_ins_exa_cri-tabla').data('id_padre',link.data('id'));
		$('#eva_ins_exa_cri-tabla').data('id_padre',link.data('id'));

		ins_exa_cri_listar_tabla();
		ins_exa_cri_listar_tabla();
		_inputs('eva_instrumento_full-frm');
		
		$('#eva_instrumento_full-frm #btn-agregar').on('click',function(event){
			$('#eva_instrumento_full-frm #id').val('');
			_post($('#eva_instrumento_full-frm').attr('action') , $('#eva_instrumento_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/instrumento/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#eva_instrumento_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		instrumento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Instrumentos';
	else
		titulo = 'Nuevo  Instrumentos';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function ins_exa_cri_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_ins_exa_cri_modal.html');
	ins_exa_cri_modal(link);
}

function ins_exa_cri_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_ins_exa_cri_modal.html');
	link.data('id_ins',$('#eva_instrumento_full-frm #id').val());
	ins_exa_cri_modal(link);
}

function ins_exa_cri_eliminar(link){
	_delete('api/insExaCri/' + $(link).data("id"),
			function(){
				ins_exa_cri_listar_tabla();
				}
			);
}
function ins_exa_cri_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_ins_exa_cri-frm');
		
		
		
		$('#eva_ins_exa_cri-frm #btn-agregar').on('click',function(event){
			$('#eva_ins_exa_cri-frm #id').val('');
			_post($('#eva_ins_exa_cri-frm').attr('action') , $('#eva_ins_exa_cri-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/insExaCri/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('eva_exa_conf_criterio',$('#eva_instrumento-frm #id_ex_cri'),data.id_ex_cri);	
						_llenarCombo('eva_instrumento',$('#eva_instrumento-frm #id_ins'),data.id_ins);	
				}
			);
		}else{
			_llenarCombo('eva_exa_conf_criterio',$('#eva_instrumento-frm #id_ex_cri'));
			_llenarCombo('eva_instrumento',$('#eva_instrumento-frm #id_ins'),$('#eva_instrumento_full-frm #id').val());
			$('#eva_ins_exa_cri-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ins_exa_cri_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Examen Criterio Instrumentos';
	else
		titulo = 'Nuevo  Examen Criterio Instrumentos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function ins_exa_cri_listar_tabla(){
	_get('api/insExaCri/listar/',
			function(data){
			console.log(data);
				$('#eva_ins_exa_cri-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Examen Criterio", "data": "exaConfCriterio.dur"}, 
							{"title":"Instrumento", "data": "instrumento.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ins_exa_cri_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ins_exa_cri_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Examen Criterio Instrumentos', 'ins_exa_cri_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ins":$('#eva_ins_exa_cri-tabla').data('id_padre')}
	);

}	

function ins_exa_cri_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_ins_exa_cri_modal.html');
	ins_exa_cri_modal(link);
}

function ins_exa_cri_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_ins_exa_cri_modal.html');
	link.data('id_ins',$('#eva_instrumento_full-frm #id').val());
	ins_exa_cri_modal(link);
}

function ins_exa_cri_eliminar(link){
	_delete('api/insExaCri/' + $(link).data("id"),
			function(){
				ins_exa_cri_listar_tabla();
				}
			);
}
function ins_exa_cri_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_ins_exa_cri-frm');
		
		
		
		$('#eva_ins_exa_cri-frm #btn-agregar').on('click',function(event){
			$('#eva_ins_exa_cri-frm #id').val('');
			_post($('#eva_ins_exa_cri-frm').attr('action') , $('#eva_ins_exa_cri-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/insExaCri/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('eva_exa_conf_criterio',$('#eva_instrumento-frm #id_ex_cri'),data.id_ex_cri);	
						_llenarCombo('eva_instrumento',$('#eva_instrumento-frm #id_ins'),data.id_ins);	
				}
			);
		}else{
			_llenarCombo('eva_exa_conf_criterio',$('#eva_instrumento-frm #id_ex_cri'));
			_llenarCombo('eva_instrumento',$('#eva_instrumento-frm #id_ins'),$('#eva_instrumento_full-frm #id').val());
			$('#eva_ins_exa_cri-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ins_exa_cri_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Examen Criterio Instrumentos';
	else
		titulo = 'Nuevo  Examen Criterio Instrumentos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function ins_exa_cri_listar_tabla(){
	_get('api/insExaCri/listar/',
			function(data){
			console.log(data);
				$('#eva_ins_exa_cri-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Examen Criterio", "data": "exaConfCriterio.dur"}, 
							{"title":"Instrumento", "data": "instrumento.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ins_exa_cri_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ins_exa_cri_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Examen Criterio Instrumentos', 'ins_exa_cri_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ins":$('#eva_ins_exa_cri-tabla').data('id_padre')}
	);

}	
