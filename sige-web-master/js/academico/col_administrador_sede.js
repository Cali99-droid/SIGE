//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_administrador_sede_modal.html" id="col_administrador_sede-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Administrador de Sede</a></li>');

	$('#col_administrador_sede-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		administrador_sede_modal($(this));
	});

	//lista tabla
	administrador_sede_listar_tabla();
});

function administrador_sede_eliminar(link){
	_DELETE({url:'api/administradorSede/' + $(link).data("id"),
			success:function(){
					administrador_sede_listar_tabla();
				}
			});
}

function administrador_sede_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_administrador_sede_modal.html');
	administrador_sede_modal(link);
	
}

function administrador_sede_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_administrador_sede-frm');
		
		$('#id_anio').val($('#_id_anio').text());
		
		$('#col_administrador_sede-frm #btn-agregar').on('click',function(event){
			$('#col_administrador_sede-frm #id').val('');
			_POST({form:$('#col_administrador_sede-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/administradorSede/' + link.data('id'),
				  success:	function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('ges_sucursal',$('#id_suc'), data.id_suc);
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				//_llenarCombo('ges_trabajador',$('#id_tra'), data.id_tra);
				_llenarComboURL('api/trabajador/listarAdministradorSedeUsuario/'+$('#_id_anio').text(), $('#id_tra'),
				data.id_tra, null, function() {
					$('#id_tra').change();
				});
				  }
				}
			);
		}else{
			_llenarCombo('ges_sucursal',$('#id_suc'));
			_llenarCombo('col_anio',$('#id_anio'));
			//_llenarCombo('ges_trabajador',$('#id_tra'));
			_llenarComboURL('api/trabajador/listarAdministradorSedeUsuario/'+$('#_id_anio').text(), $('#id_tra'),
				null, null, function() {
					$('#id_tra').change();
				});
			$('#col_administrador_sede-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		administrador_sede_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Administrador Sede';
	else
		titulo = 'Nuevo  Administrador Sede';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function administrador_sede_listar_tabla(){
	_GET({ url: 'api/administradorSede/listar/',
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#col_administrador_sede-tabla').dataTable({
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
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Trabajador", "render": function ( data, type, row ) {
			            	   return row.trabajador.nom+" "+row.trabajador.ape_pat+" "+row.trabajador.ape_mat;
			                }} , 
							{"title":"Sucursal", "data": "sucursal.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="administrador_sede_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="administrador_sede_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

