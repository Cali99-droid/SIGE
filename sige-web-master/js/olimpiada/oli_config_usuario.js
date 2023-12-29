//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/olimpiada/oli_config_usuario_modal.html" id="oli_config_usuario-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Configuracion Usuario Olimpiada</a></li>');

	$('#oli_config_usuario-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		config_usuario_modal($(this));
	});

	//lista tabla
	config_usuario_listar_tabla();
});

function config_usuario_eliminar(link){
	_DELETE({url:'api/configUsuario/' + $(link).data("id"),
		    context: _URL_OLI,
			success:function(){
					config_usuario_listar_tabla();
				}
			});
}

function config_usuario_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/olimpiada/oli_config_usuario_modal.html');
	config_usuario_modal(link);
	
}

function config_usuario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('oli_config_usuario-frm');
		
		$('#oli_config_usuario-frm #btn-agregar').on('click',function(event){
			$('#oli_config_usuario-frm #id').val('');
			_POST({form:$('#oli_config_usuario-frm'),
				  context: _URL_OLI,
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
				
		if (link.data('id')){
			_GET({url:'api/configUsuario/' + link.data('id'),
				  context: _URL_OLI,
				  success:	function(data){
					  console.log(data);
				_fillForm(data,$('#modal').find('form') );
				/*_llenar_combo({
					tabla : 'oli_config',
					combo : $('#id_oli'),
					context : _URL_OLI,
					id: data.id_oli,
					funcion : function() {
						//$('#id_oli').change();
					}
				});	*/
				var param={id_anio:$("#_id_anio").text()};
				_llenar_combo({
					//tabla : 'oli_config',
					url : 'api/config/listarOlimpiadasxAnio',
					params: param,
					combo : $('#id_oli'),
					context : _URL_OLI,
					id: data.id_oli,
					funcion : function() {
						$('#id_oli').change();
					}
				});
				_llenar_combo({
					//tabla : 'seg_usuario',
					url:'api/comboCache/usuarios',
					combo : $('#id_usr'),
					id: data.id_usr,
					text: "login",
					context : _URL_OLI,
					id: data.id_usr,
					funcion : function() {
						//$('#id_oli').change();
					}
				});
				$('#oli_config_usuario-frm #btn-agregar').hide();
				  }
				}
			);
		}else{
			/*_llenar_combo({
				tabla : 'oli_config',
				combo : $('#id_oli'),
				context : _URL_OLI,
				funcion : function() {
					//$('#id_oli').change();
				}
			});	*/
			var param={id_anio:$("#_id_anio").text()};
				_llenar_combo({
					//tabla : 'oli_config',
					url : 'api/config/listarOlimpiadasxAnio',
					params: param,
					combo : $('#id_oli'),
					context : _URL_OLI,
					funcion : function() {
						$('#id_oli').change();
					}
				});
			_llenar_combo({
				//tabla : 'api/combo/usuarios',
				url:'api/comboCache/usuarios',
				combo : $('#id_usr'),
				text: "login",
				context : _URL_OLI,
				funcion : function() {
					//$('#id_oli').change();
				}
			});
			$('#oli_config_usuario-frm #btn-grabar').hide();
		}
		$('#oli_config_usuario-frm #btn-grabar').on('click',function(event){
			alert();
			_POST({form:$('#oli_config_usuario-frm'),
				  context: _URL_OLI,
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		config_usuario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuracion Usuario Olimpiada';
	else
		titulo = 'Nuevo  Configuracion Usuario Olimpiada';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function config_usuario_listar_tabla(){
	var param={id_anio:$("#_id_anio").text()};
	_GET({ url: 'api/configUsuario/listarUsuarioxOli/',
			context: _URL_OLI,
		   params: param,
		   success:
			function(data){
			console.log(data);
				$('#oli_config_usuario-tabla').dataTable({
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
							{"title":"Olimpiada", "data": "olimpiada"}, 
							{"title":"Trabajador", "data": "trabajador"}, 
							{"title":"Usuario", "data": "login"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="config_usuario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="config_usuario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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
		});

}

