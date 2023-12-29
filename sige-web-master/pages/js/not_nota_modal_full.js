//edicion completa de una tabla
function nota_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_nota_full.html');
	nota_full_modal(link);

}


function nota_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#not_nota_indicador-tabla').data('id_padre',link.data('id'));

		nota_indicador_listar_tabla();
		_inputs('not_nota_full-frm');
		
		$('#not_nota_full-frm #btn-agregar').on('click',function(event){
			$('#not_nota_full-frm #id').val('');
			_post($('#not_nota_full-frm').attr('action') , $('#not_nota_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/nota/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('not_evaluacion',$('#not_nota-frm #id_ne'),data.id_ne);	
						_llenarCombo('ges_trabajador',$('#not_nota-frm #id_tra'),data.id_tra);	
						_llenarCombo('alu_alumno',$('#not_nota-frm #id_alu'),data.id_alu);	
						}
			);
		}else{
					_llenarCombo('not_evaluacion',$('#not_nota-frm #id_ne'));
					_llenarCombo('ges_trabajador',$('#not_nota-frm #id_tra'));
					_llenarCombo('alu_alumno',$('#not_nota-frm #id_alu'));
				}
		$('#not_nota_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		nota_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Evaluacion';
	else
		titulo = 'Nuevo  Nota Evaluacion';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function nota_indicador_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/notas/not_nota_indicador_modal.html');
	nota_indicador_modal(link);
}

function nota_indicador_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/notas/not_nota_indicador_modal.html');
	link.data('id_not',$('#not_nota_full-frm #id').val());
	nota_indicador_modal(link);
}

function nota_indicador_eliminar(link){
	_delete('api/notaIndicador/' + $(link).data("id"),
			function(){
				nota_indicador_listar_tabla();
				}
			);
}
function nota_indicador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('not_nota_indicador-frm');
		
		
		
		$('#not_nota_indicador-frm #btn-agregar').on('click',function(event){
			$('#not_nota_indicador-frm #id').val('');
			_post($('#not_nota_indicador-frm').attr('action') , $('#not_nota_indicador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/notaIndicador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('not_nota',$('#not_nota-frm #id_not'),data.id_not);	
						_llenarCombo('not_ind_eva',$('#not_nota-frm #id_nie'),data.id_nie);	
				}
			);
		}else{
			_llenarCombo('not_nota',$('#not_nota-frm #id_not'),$('#not_nota_full-frm #id').val());
			_llenarCombo('not_ind_eva',$('#not_nota-frm #id_nie'));
			$('#not_nota_indicador-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		nota_indicador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Indicador';
	else
		titulo = 'Nuevo  Nota Indicador';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function nota_indicador_listar_tabla(){
	_get('api/notaIndicador/listar/',
			function(data){
			console.log(data);
				$('#not_nota_indicador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Nota Evaluacion", "data": "nota.fec"}, 
							{"title":"Indicador Evaluacion", "data": "indEva.id_ne"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="nota_indicador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="nota_indicador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Nota Indicador', 'nota_indicador_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_not":$('#not_nota_indicador-tabla').data('id_padre')}
	);

}	
