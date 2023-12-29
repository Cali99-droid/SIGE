//edicion completa de una tabla
function tip_eva_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_tip_eva_full.html');
	tip_eva_full_modal(link);

}


function tip_eva_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#eva_evaluacion_vac_examen-tabla').data('id_padre',link.data('id'));

		evaluacion_vac_examen_listar_tabla();
		_inputs('eva_tip_eva_full-frm');
		
		$('#eva_tip_eva_full-frm #btn-agregar').on('click',function(event){
			$('#eva_tip_eva_full-frm #id').val('');
			_post($('#eva_tip_eva_full-frm').attr('action') , $('#eva_tip_eva_full-frm').serialize(),
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
		$('#eva_tip_eva_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tip_eva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tipo de evaluacion';
	else
		titulo = 'Nuevo  Tipo de evaluacion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function evaluacion_vac_examen_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_evaluacion_vac_examen_modal.html');
	evaluacion_vac_examen_modal(link);
}

function evaluacion_vac_examen_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_evaluacion_vac_examen_modal.html');
	link.data('id_tae',$('#eva_tip_eva_full-frm #id').val());
	evaluacion_vac_examen_modal(link);
}

function evaluacion_vac_examen_eliminar(link){
	_delete('api/evaluacionVacExamen/' + $(link).data("id"),
			function(){
				evaluacion_vac_examen_listar_tabla();
				}
			);
}
function evaluacion_vac_examen_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_evaluacion_vac_examen-frm');
		
		
		
		$('#eva_evaluacion_vac_examen-frm #btn-agregar').on('click',function(event){
			$('#eva_evaluacion_vac_examen-frm #id').val('');
			_post($('#eva_evaluacion_vac_examen-frm').attr('action') , $('#eva_evaluacion_vac_examen-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/evaluacionVacExamen/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('eva_evaluacion_vac',$('#eva_tip_eva-frm #id_eva_vac'),data.id_eva_vac);	
						_llenarCombo('eva_area_eva',$('#eva_tip_eva-frm #id_eae'),data.id_eae);	
						_llenarCombo('eva_tip_eva',$('#eva_tip_eva-frm #id_tae'),data.id_tae);	
				}
			);
		}else{
			_llenarCombo('eva_evaluacion_vac',$('#eva_tip_eva-frm #id_eva_vac'));
			_llenarCombo('eva_area_eva',$('#eva_tip_eva-frm #id_eae'));
			_llenarCombo('eva_tip_eva',$('#eva_tip_eva-frm #id_tae'),$('#eva_tip_eva_full-frm #id').val());
			$('#eva_evaluacion_vac_examen-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		evaluacion_vac_examen_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Examen por Marcaci&oacute;n';
	else
		titulo = 'Nuevo  Examen por Marcaci&oacute;n';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function evaluacion_vac_examen_listar_tabla(){
	_get('api/evaluacionVacExamen/listar/',
			function(data){
			console.log(data);
				$('#eva_evaluacion_vac_examen-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha Examen", "data" : "fec_exa"}, 
							{"title":"Fecha ingreso nota", "data" : "fec_not"}, 
							{"title":"Evaluaci&oacute;n Vacante", "data": "evaluacionVac.des"}, 
							{"title":"Area", "data": "areaEva.nom"}, 
							{"title":"Tipo de evaluaci&oacute;n", "data": "tipEva.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
						      return '<div class="list-icons"> <a href="#" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a> <a href="#" class="list-icons-item"><i class="fa fa-trash ui-blue ui-size" aria-hidden="true"></i></a> <a href="#" class="list-icons-item"><i class="fa fa-cog ui-blue ui-size" aria-hidden="true"></i></a></div>';

			     //               return '<ul class="icons-list">'+
								// 	'<li class="dropdown">'+
								// '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
								// 	'<i class="icon-menu9"></i>'+
								// 	'</a>'+
								// '<ul class="dropdown-menu dropdown-menu-right">'+
								// 	'<li><a href="#" data-id="' + row.id + '" onclick="evaluacion_vac_examen_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
								// 	'<li><a href="#" data-id="' + row.id + '" onclick="evaluacion_vac_examen_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								// '</ul>'+
			     //               '</li>'+
			     //               '</ul>';
			               }
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Examen por Marcación', 'evaluacion_vac_examen_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_tae":$('#eva_evaluacion_vac_examen-tabla').data('id_padre')}
	);

}	
