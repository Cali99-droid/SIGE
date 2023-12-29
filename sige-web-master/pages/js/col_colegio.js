//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/configuracion/col_colegio_modal.html" id="col_colegio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Colegio</a></li>');

	$('#col_colegio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		colegio_modal($(this));
	});

	//lista tabla
	colegio_listar_tabla();
});

function colegio_eliminar(link){
	_delete('api/colegio/' + $(link).data("id"),
			function(){
					colegio_listar_tabla();
				}
			);
}

function colegio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/configuracion/col_colegio_modal.html');
	colegio_modal(link);
	
}

function colegio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_colegio-frm');
		
		$('#col_colegio-frm #btn-agregar').on('click',function(event){
			$('#col_colegio-frm #id').val('');
			_post($('#col_colegio-frm').attr('action') , $('#col_colegio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}                                   
			);
		});
		
		if (link.data('id')){
			_get('api/colegio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				
				$('#id_prov').on('change',function(event){
					_llenarCombo('cat_distrito',$('#id_dist'),data.id_dist,$(this).val());
				});
				$('#id_dep').on('change',function(event){
					_llenarCombo('cat_provincia',$('#id_prov'),data.provincia.id,$(this).val(),function(){$('#id_prov').change();});
				});
				_llenarCombo('cat_departamento',$('#id_dep'), data.departamento.id,null,function(){$('#id_dep').change();});
			});
		}else{
			$('#id_prov').on('change',function(event){
				_llenarCombo('cat_distrito',$('#id_dist'),null,$(this).val());
			});
			$('#id_dep').on('change',function(event){
				_llenarCombo('cat_provincia',$('#id_prov'),null,$(this).val(),function(){$('#id_prov').change();});
			});
			_llenarCombo('cat_departamento',$('#id_dep'), null,null,function(){$('#id_dep').change();});
			$('#col_colegio-frm #btn-grabar').hide();
		}
		
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
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function colegio_listar_tabla(){
	_get('api/colegio/listar/',
			function(data){
			console.log(data);
				$('#col_colegio-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"C&oacute;digo modular", "data" : "cod_mod"}, 
							{"title":"nivel", "data" : "nom_niv"}, 
							{"title":"Nombre del colegio", "data" : "nom"}, 
						//	{"title":"Estatal", "data" : "estatal"}, 
							{"title":"direccion", "data" : "dir"}, 
							{"title":"Tel&eacute;fono", "data" : "tel"}, 
							{"title":"Distrito", "data": "distrito.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="colegio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="colegio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="colegio_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

