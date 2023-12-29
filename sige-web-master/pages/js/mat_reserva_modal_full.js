//edicion completa de una tabla
function reserva_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/mat_reserva_full.html');
	reserva_full_modal(link);

}


function reserva_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#fac_reserva_cuota-tabla').data('id_padre',link.data('id'));

		reserva_cuota_listar_tabla();
		_inputs('mat_reserva_full-frm');
		
		$('#mat_reserva_full-frm #btn-agregar').on('click',function(event){
			$('#mat_reserva_full-frm #id').val('');
			_post($('#mat_reserva_full-frm').attr('action') , $('#mat_reserva_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/reserva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('alu_alumno',$('#mat_reserva-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#mat_reserva-frm #id_au'),data.id_au);	
						_llenarCombo('cat_grad',$('#mat_reserva-frm #id_gra'),data.id_gra);	
						_llenarCombo('cat_nivel',$('#mat_reserva-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_cond_matricula',$('#mat_reserva-frm #id_cma'),data.id_cma);	
						_llenarCombo('cat_cliente',$('#mat_reserva-frm #id_cli'),data.id_cli);	
						_llenarCombo('per_periodo',$('#mat_reserva-frm #id_per'),data.id_per);	
						_llenarCombo('alu_familiar',$('#mat_reserva-frm #id_fam'),data.id_fam);	
						}
			);
		}else{
					_llenarCombo('alu_alumno',$('#mat_reserva-frm #id_alu'));
					_llenarCombo('col_aula',$('#mat_reserva-frm #id_au'));
					_llenarCombo('cat_grad',$('#mat_reserva-frm #id_gra'));
					_llenarCombo('cat_nivel',$('#mat_reserva-frm #id_niv'));
					_llenarCombo('cat_cond_matricula',$('#mat_reserva-frm #id_cma'));
					_llenarCombo('cat_cliente',$('#mat_reserva-frm #id_cli'));
					_llenarCombo('per_periodo',$('#mat_reserva-frm #id_per'));
					_llenarCombo('alu_familiar',$('#mat_reserva-frm #id_fam'));
				}
		$('#mat_reserva_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reserva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reserva de matr&iacute;cula';
	else
		titulo = 'Nuevo  Reserva de matr&iacute;cula';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reserva_cuota_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_reserva_cuota_modal.html');
	reserva_cuota_modal(link);
}

function reserva_cuota_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/tesoreria/fac_reserva_cuota_modal.html');
	link.data('id_mat_res',$('#mat_reserva_full-frm #id').val());
	reserva_cuota_modal(link);
}

function reserva_cuota_eliminar(link){
	_delete('api/reservaCuota/' + $(link).data("id"),
			function(){
				reserva_cuota_listar_tabla();
				}
			);
}
function reserva_cuota_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('fac_reserva_cuota-frm');
		
		
		
		$('#fac_reserva_cuota-frm #btn-agregar').on('click',function(event){
			$('#fac_reserva_cuota-frm #id').val('');
			_post($('#fac_reserva_cuota-frm').attr('action') , $('#fac_reserva_cuota-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reservaCuota/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('mat_reserva',$('#mat_reserva-frm #id_mat_res'),data.id_mat_res);	
				}
			);
		}else{
			_llenarCombo('mat_reserva',$('#mat_reserva-frm #id_mat_res'),$('#mat_reserva_full-frm #id').val());
			$('#fac_reserva_cuota-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reserva_cuota_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Pago de cuota de Reserva de matr&iacute;cula';
	else
		titulo = 'Nuevo  Pago de cuota de Reserva de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reserva_cuota_listar_tabla(){
	_get('api/reservaCuota/listar/',
			function(data){
			console.log(data);
				$('#fac_reserva_cuota-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Monto", "data" : "monto"}, 
							{"title":"Nro de recibo", "data" : "nro_recibo"}, 
							{"title":"Reserva", "data": "reserva.fec"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reserva_cuota_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reserva_cuota_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Reserva Cuota', 'reserva_cuota_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_mat_res":$('#fac_reserva_cuota-tabla').data('id_padre')}
	);

}	
