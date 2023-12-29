//edicion completa de una tabla
function falta_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_falta_full.html');
	falta_full_modal(link);

}


function falta_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_reporte_falta-tabla').data('id_padre',link.data('id'));

		reporte_falta_listar_tabla();
		_inputs('cat_falta_full-frm');
		
		$('#cat_falta_full-frm #btn-agregar').on('click',function(event){
			$('#cat_falta_full-frm #id').val('');
			_post($('#cat_falta_full-frm').attr('action') , $('#cat_falta_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/falta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_tip_falta',$('#cat_falta-frm #id_ctf'),data.id_ctf);	
						}
			);
		}else{
					_llenarCombo('cat_tip_falta',$('#cat_falta-frm #id_ctf'));
				}
		$('#cat_falta_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Faltas';
	else
		titulo = 'Nuevo  Faltas';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reporte_falta_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_falta_modal.html');
	reporte_falta_modal(link);
}

function reporte_falta_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_reporte_falta_modal.html');
	link.data('id_cf',$('#cat_falta_full-frm #id').val());
	reporte_falta_modal(link);
}

function reporte_falta_eliminar(link){
	_delete('api/reporteFalta/' + $(link).data("id"),
			function(){
				reporte_falta_listar_tabla();
				}
			);
}
function reporte_falta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_reporte_falta-frm');
		
		
		
		$('#col_reporte_falta-frm #btn-agregar').on('click',function(event){
			$('#col_reporte_falta-frm #id').val('');
			_post($('#col_reporte_falta-frm').attr('action') , $('#col_reporte_falta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reporteFalta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('col_reporte_conductual',$('#cat_falta-frm #id_cp'),data.id_cp);	
						_llenarCombo('cat_falta',$('#cat_falta-frm #id_cf'),data.id_cf);	
				}
			);
		}else{
			_llenarCombo('col_reporte_conductual',$('#cat_falta-frm #id_cp'));
			_llenarCombo('cat_falta',$('#cat_falta-frm #id_cf'),$('#cat_falta_full-frm #id').val());
			$('#col_reporte_falta-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reporte_falta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reporte Falta';
	else
		titulo = 'Nuevo  Reporte Falta';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function reporte_falta_listar_tabla(){
	_get('api/reporteFalta/listar/',
			function(data){
			console.log(data);
				$('#col_reporte_falta-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Reporte Conductual", "data": "reporteConductual.cod"}, 
							{"title":"Falta", "data": "falta.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_falta_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="reporte_falta_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Reporte Falta', 'reporte_falta_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cf":$('#col_reporte_falta-tabla').data('id_padre')}
	);

}	
