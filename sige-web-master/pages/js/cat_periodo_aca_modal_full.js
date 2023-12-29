//edicion completa de una tabla
function periodo_aca_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_periodo_aca_full.html');
	periodo_aca_full_modal(link);

}


function periodo_aca_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		$('#cat_per_aca_nivel-tabla').data('id_padre',link.data('id'));

		per_aca_nivel_listar_tabla();
		_inputs('cat_periodo_aca_full-frm');
		
		$('#cat_periodo_aca_full-frm #btn-agregar').on('click',function(event){
			$('#cat_periodo_aca_full-frm #id').val('');
			_post($('#cat_periodo_aca_full-frm').attr('action') , $('#cat_periodo_aca_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
		if (link.data('id')){
			_get('api/periodoAca/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
						}
			);
		}else
			$('#cat_periodo_aca_full-frm #btn-grabar').hide();
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		periodo_aca_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Acad&eacute;mico Ejm(Bimestre - Trimestres)';
	else
		titulo = 'Nuevo  Periodo Acad&eacute;mico Ejm(Bimestre - Trimestres)';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function per_aca_nivel_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/catalogos/cat_per_aca_nivel_modal.html');
	per_aca_nivel_modal(link);
}

function per_aca_nivel_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_per_aca_nivel_modal.html');
	link.data('id_cpa',$('#cat_periodo_aca_full-frm #id').val());
	per_aca_nivel_modal(link);
}

function per_aca_nivel_eliminar(link){
	_delete('api/perAcaNivel/' + $(link).data("id"),
			function(){
				per_aca_nivel_listar_tabla();
				}
			);
}
function per_aca_nivel_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('cat_per_aca_nivel-frm');
		
		
		
		$('#cat_per_aca_nivel-frm #btn-agregar').on('click',function(event){
			$('#cat_per_aca_nivel-frm #id').val('');
			_post($('#cat_per_aca_nivel-frm').attr('action') , $('#cat_per_aca_nivel-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perAcaNivel/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
						_llenarCombo('cat_nivel',$('#cat_per_aca_nivel-frm #id_niv'),data.id_niv);	
						_llenarCombo('cat_periodo_aca',$('#cat_per_aca_nivel-frm #id_cpa'),data.id_cpa);	
				}
			);
		}else
			$('#cat_per_aca_nivel-frm #btn-grabar').hide();
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function per_aca_nivel_listar_tabla(){
	_get('api/perAcaNivel/listar/',
			function(data){
			console.log(data);
				$('#cat_per_aca_nivel-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"N&uacute;mero de Unidades", "data" : "nro_uni"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Periodo Academico", "data": "periodoAca.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_aca_nivel_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="per_aca_nivel_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar Periodo Academico-Nivel', 'per_aca_nivel_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},{"id_cpa":$('#cat_per_aca_nivel-tabla').data('id_padre')}
	);

}	
