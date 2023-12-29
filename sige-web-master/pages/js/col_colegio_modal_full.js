//edicion completa de una tabla
function colegio_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/configuracion/col_colegio_full.html');
	colegio_full_modal(link);

}


function colegio_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#eva_matr_vacante-tabla').data('id_padre',link.data('id'));

		matr_vacante_listar_tabla();
		_inputs('col_colegio_full-frm');
		
		$('#col_colegio_full-frm #btn-agregar').on('click',function(event){
			$('#col_colegio_full-frm #id').val('');
			_post($('#col_colegio_full-frm').attr('action') , $('#col_colegio_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/colegio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_distrito',$('#col_colegio-frm #id_dist'),data.id_dist);	
						}
			);
		}else{
					_llenarCombo('cat_distrito',$('#col_colegio-frm #id_dist'));
				}
		$('#col_colegio_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		colegio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Colegio';
	else
		titulo = 'Nuevo  Colegio';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function matr_vacante_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_matr_vacante_modal.html');
	matr_vacante_modal(link);
}

function matr_vacante_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_matr_vacante_modal.html');
	link.data('id_col',$('#col_colegio_full-frm #id').val());
	matr_vacante_modal(link);
}

function matr_vacante_eliminar(link){
	_delete('api/matrVacante/' + $(link).data("id"),
			function(){
				matr_vacante_listar_tabla();
				}
			);
}
function matr_vacante_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('eva_matr_vacante-frm');
		
		
		
		$('#eva_matr_vacante-frm #btn-agregar').on('click',function(event){
			$('#eva_matr_vacante-frm #id').val('');
			_post($('#eva_matr_vacante-frm').attr('action') , $('#eva_matr_vacante-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/matrVacante/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('alu_alumno',$('#col_colegio-frm #id_alu'),data.id_alu);	
						_llenarCombo('eva_evaluacion_vac',$('#col_colegio-frm #id_eva_vac'),data.id_eva_vac);	
						_llenarCombo('cat_grad',$('#col_colegio-frm #id_gra'),data.id_gra);	
						_llenarCombo('col_colegio',$('#col_colegio-frm #id_col'),data.id_col);	
						_llenarCombo('alu_familiar',$('#col_colegio-frm #id_fam'),data.id_fam);	
				}
			);
		}else{
			_llenarCombo('alu_alumno',$('#col_colegio-frm #id_alu'));
			_llenarCombo('eva_evaluacion_vac',$('#col_colegio-frm #id_eva_vac'));
			_llenarCombo('cat_grad',$('#col_colegio-frm #id_gra'));
			_llenarCombo('col_colegio',$('#col_colegio-frm #id_col'),$('#col_colegio_full-frm #id').val());
			_llenarCombo('alu_familiar',$('#col_colegio-frm #id_fam'));
			$('#eva_matr_vacante-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		matr_vacante_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Examen Vacante';
	else
		titulo = 'Nuevo  Examen Vacante';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function matr_vacante_listar_tabla(){
	_get('api/matrVacante/listar/',
			function(data){
			console.log(data);
				$('#eva_matr_vacante-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Recibo", "data" : "num_rec"}, 
							{"title":"Contrato", "data" : "num_cont"}, 
							{"title":"Resultado", "data" : "res"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Evaluaci&oacute;n Vacante", "data": "evaluacionVac.des"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Colegio", "data": "colegio.nom"}, 
							{"title":"Familiar", "data": "familiar.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matr_vacante_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matr_vacante_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Matricula Vacante', 'matr_vacante_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_col":$('#eva_matr_vacante-tabla').data('id_padre')}
	);

}	
