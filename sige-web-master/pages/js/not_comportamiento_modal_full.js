//edicion completa de una tabla
function comportamiento_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_comportamiento_full.html');
	comportamiento_full_modal(link);

}


function comportamiento_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#not_cap_com-tabla').data('id_padre',link.data('id'));

		cap_com_listar_tabla();
		_inputs('not_comportamiento_full-frm');
		
		$('#not_comportamiento_full-frm #btn-agregar').on('click',function(event){
			$('#not_comportamiento_full-frm #id').val('');
			_post($('#not_comportamiento_full-frm').attr('action') , $('#not_comportamiento_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/comportamiento/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('ges_trabajador',$('#not_comportamiento-frm #id_tra'),data.id_tra);	
						_llenarCombo('alu_alumno',$('#not_comportamiento-frm #id_alu'),data.id_alu);	
						_llenarCombo('col_aula',$('#not_comportamiento-frm #id_au'),data.id_au);	
						_llenarCombo('col_per_uni',$('#not_comportamiento-frm #id_cpu'),data.id_cpu);	
						}
			);
		}else{
					_llenarCombo('ges_trabajador',$('#not_comportamiento-frm #id_tra'));
					_llenarCombo('alu_alumno',$('#not_comportamiento-frm #id_alu'));
					_llenarCombo('col_aula',$('#not_comportamiento-frm #id_au'));
					_llenarCombo('col_per_uni',$('#not_comportamiento-frm #id_cpu'));
				}
		$('#not_comportamiento_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		comportamiento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Comportamiento';
	else
		titulo = 'Nuevo  Nota Comportamiento';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cap_com_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_cap_com_modal.html');
	cap_com_modal(link);
}

function cap_com_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_cap_com_modal.html');
	link.data('id_nc',$('#not_comportamiento_full-frm #id').val());
	cap_com_modal(link);
}

function cap_com_eliminar(link){
	_delete('api/capCom/' + $(link).data("id"),
			function(){
				cap_com_listar_tabla();
				}
			);
}
function cap_com_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_cap_com-frm');
		
		
		
		$('#not_cap_com-frm #btn-agregar').on('click',function(event){
			$('#not_cap_com-frm #id').val('');
			_post($('#not_cap_com-frm').attr('action') , $('#not_cap_com-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/capCom/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('not_comportamiento',$('#not_comportamiento-frm #id_nc'),data.id_nc);	
						_llenarCombo('col_capacidad',$('#not_comportamiento-frm #id_cap'),data.id_cap);	
				}
			);
		}else{
			_llenarCombo('not_comportamiento',$('#not_comportamiento-frm #id_nc'),$('#not_comportamiento_full-frm #id').val());
			_llenarCombo('col_capacidad',$('#not_comportamiento-frm #id_cap'));
			$('#not_cap_com-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cap_com_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Capacidad Comportamiento';
	else
		titulo = 'Nuevo  Nota Capacidad Comportamiento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function cap_com_listar_tabla(){
	_get('api/capCom/listar/',
			function(data){
			console.log(data);
				$('#not_cap_com-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nota", "data" : "nota"}, 
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Nota Comportamiento", "data": "comportamiento.prom"}, 
							{"title":"Capacidad", "data": "capacidad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cap_com_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cap_com_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Nota Capacidad Comportamiento', 'cap_com_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_nc":$('#not_cap_com-tabla').data('id_padre')}
	);

}	
