//edicion completa de una tabla
function per_aca_nivel_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_per_aca_nivel_full.html');
	per_aca_nivel_full_modal(link);

}


function per_aca_nivel_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#col_per_uni-tabla').data('id_padre',link.data('id'));

		per_uni_listar_tabla();
		_inputs('cat_per_aca_nivel_full-frm');
		
		$('#cat_per_aca_nivel_full-frm #btn-agregar').on('click',function(event){
			$('#cat_per_aca_nivel_full-frm #id').val('');
			_post($('#cat_per_aca_nivel_full-frm').attr('action') , $('#cat_per_aca_nivel_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/perAcaNivel/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						_llenarCombo('cat_nivel',$('#cat_per_aca_nivel-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_periodo_aca',$('#cat_per_aca_nivel-frm #id_cpa'),data.id_cpa);	
						}
			);
		}else{
					_llenarCombo('cat_nivel',$('#cat_per_aca_nivel-frm #id_niv'));
					_llenarCombo('cat_periodo_aca',$('#cat_per_aca_nivel-frm #id_cpa'));
				}
		$('#cat_per_aca_nivel_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_aca_nivel_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo  Acad&eacute;mico por Nivel';
	else
		titulo = 'Nuevo  Periodo  Acad&eacute;mico por Nivel';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function per_uni_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_modal.html');
	per_uni_modal(link);
}

function per_uni_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_modal.html');
	link.data('id_cpan',$('#cat_per_aca_nivel_full-frm #id').val());
	per_uni_modal(link);
}

function per_uni_eliminar(link){
	_delete('api/perUni/' + $(link).data("id"),
			function(){
				per_uni_listar_tabla();
				}
			);
}
function per_uni_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('col_per_uni-frm');
		
		
		
		$('#col_per_uni-frm #btn-agregar').on('click',function(event){
			$('#col_per_uni-frm #id').val('');
			_post($('#col_per_uni-frm').attr('action') , $('#col_per_uni-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perUni/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_per_aca_nivel',$('#cat_per_aca_nivel-frm #id_cpan'),data.id_cpan);	
				}
			);
		}else{
			_llenarCombo('cat_per_aca_nivel',$('#cat_per_aca_nivel-frm #id_cpan'),$('#cat_per_aca_nivel_full-frm #id').val());
			$('#col_per_uni-frm #btn-grabar').hide();//si se
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_uni_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Unidad';
	else
		titulo = 'Nuevo  Periodo Unidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function per_uni_listar_tabla(){
	_get('api/perUni/listar/',
			function(data){
			console.log(data);
				$('#col_per_uni-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Numero de Periodo", "data" : "nump"}, 
							{"title":"N&uacute;mero de Unidad", "data" : "numu"}, 
							{"title":"Periodo Acad&eacute;mico Nivel", "data": "perAcaNivel.id_niv"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_uni_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Periodo Unidad', 'per_uni_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cpan":$('#col_per_uni-tabla').data('id_padre')}
	);

}	
