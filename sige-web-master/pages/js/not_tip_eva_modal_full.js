//edicion completa de una tabla
function tip_eva_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_tip_eva_full.html');
	tip_eva_full_modal(link);

}


function tip_eva_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#not_evaluacion-tabla').data('id_padre',link.data('id'));

		evaluacion_listar_tabla();
		_inputs('not_tip_eva_full-frm');
		
		$('#not_tip_eva_full-frm #btn-agregar').on('click',function(event){
			$('#not_tip_eva_full-frm #id').val('');
			_post($('#not_tip_eva_full-frm').attr('action') , $('#not_tip_eva_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/tipEva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else{
				}
		$('#not_tip_eva_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de Evaluacion';
	else
		titulo = 'Nuevo  Tipo de Evaluacion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function evaluacion_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_evaluacion_modal.html');
	evaluacion_modal(link);
}

function evaluacion_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_evaluacion_modal.html');
	link.data('id_nte',$('#not_tip_eva_full-frm #id').val());
	evaluacion_modal(link);
}

function evaluacion_eliminar(link){
	_delete('api/evaluacion/' + $(link).data("id"),
			function(){
				evaluacion_listar_tabla();
				}
			);
}
function evaluacion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_evaluacion-frm');
		
		
		
		$('#not_evaluacion-frm #btn-agregar').on('click',function(event){
			$('#not_evaluacion-frm #id').val('');
			_post($('#not_evaluacion-frm').attr('action') , $('#not_evaluacion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/evaluacion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_curso_aula',$('#not_tip_eva-frm #id_cca'),data.id_cca);	
						_llenarCombo('not_tip_eva',$('#not_tip_eva-frm #id_nte'),data.id_nte);	
				}
			);
		}else{
			_llenarCombo('col_curso_aula',$('#not_tip_eva-frm #id_cca'));
			_llenarCombo('not_tip_eva',$('#not_tip_eva-frm #id_nte'),$('#not_tip_eva_full-frm #id').val());
			$('#not_evaluacion-frm #btn-grabar').hide();//si se
		}
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function evaluacion_listar_tabla(){
	_get('api/evaluacion/listar/',
			function(data){
			console.log(data);
				$('#not_evaluacion-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Instrumento", "data" : "ins"}, 
							{"title":"Evidencia", "data" : "evi"}, 
							{"title":"Fecha Inicio", "data" : "fec_ini"}, 
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							{"title":"Curso Aula", "data": "cursoAula.id_cua"}, 
							{"title":"Tipo Evaluacion", "data": "tipEva.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="evaluacion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="evaluacion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Evaluaciones Académicas', 'evaluacion_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_nte":$('#not_evaluacion-tabla').data('id_padre')}
	);

}	
