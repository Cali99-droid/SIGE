//edicion completa de una tabla
function evaluacion_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_evaluacion_full.html');
	evaluacion_full_modal(link);

}


function evaluacion_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#not_ind_eva-tabla').data('id_padre',link.data('id'));

		ind_eva_listar_tabla();
		_inputs('not_evaluacion_full-frm');
		
		$('#not_evaluacion_full-frm #btn-agregar').on('click',function(event){
			$('#not_evaluacion_full-frm #id').val('');
			_post($('#not_evaluacion_full-frm').attr('action') , $('#not_evaluacion_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/evaluacion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('col_curso_aula',$('#not_evaluacion-frm #id_cca'),data.id_cca);	
						_llenarCombo('not_tip_eva',$('#not_evaluacion-frm #id_nte'),data.id_nte);	
						}
			);
		}else{
					_llenarCombo('col_curso_aula',$('#not_evaluacion-frm #id_cca'));
					_llenarCombo('not_tip_eva',$('#not_evaluacion-frm #id_nte'));
				}
		$('#not_evaluacion_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		evaluacion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Evaluaciones Acad&eacute;micas';
	else
		titulo = 'Nuevo  Evaluaciones Acad&eacute;micas';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function ind_eva_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_ind_eva_modal.html');
	ind_eva_modal(link);
}

function ind_eva_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_ind_eva_modal.html');
	link.data('id_ne',$('#not_evaluacion_full-frm #id').val());
	ind_eva_modal(link);
}

function ind_eva_eliminar(link){
	_delete('api/indEva/' + $(link).data("id"),
			function(){
				ind_eva_listar_tabla();
				}
			);
}
function ind_eva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_ind_eva-frm');
		
		
		
		$('#not_ind_eva-frm #btn-agregar').on('click',function(event){
			$('#not_ind_eva-frm #id').val('');
			_post($('#not_ind_eva-frm').attr('action') , $('#not_ind_eva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/indEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('not_evaluacion',$('#not_evaluacion-frm #id_ne'),data.id_ne);	
						_llenarCombo('col_ind_sub',$('#not_evaluacion-frm #id_cis'),data.id_cis);	
				}
			);
		}else{
			_llenarCombo('not_evaluacion',$('#not_evaluacion-frm #id_ne'),$('#not_evaluacion_full-frm #id').val());
			_llenarCombo('col_ind_sub',$('#not_evaluacion-frm #id_cis'));
			$('#not_ind_eva-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		ind_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Indicador Evalaucion';
	else
		titulo = 'Nuevo  Indicador Evalaucion';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function ind_eva_listar_tabla(){
	_get('api/indEva/listar/',
			function(data){
			console.log(data);
				$('#not_ind_eva-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Evaluacion", "data": "evaluacion.ins"}, 
							{"title":"Indicador Subtema", "data": "indSub.id_ind"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_eva_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="ind_eva_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Indicador Evaluacion', 'ind_eva_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_ne":$('#not_ind_eva-tabla').data('id_padre')}
	);

}	
