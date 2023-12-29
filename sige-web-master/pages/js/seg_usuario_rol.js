//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	//_onloadPage();
	usuario_rol_listar_tabla();
	$("#tip_con").on("change", function() {
			usuario_rol_listar_tabla();
	});	
}
//se ejecuta siempre despues de cargar el html
/*$(function(){
	$('#seg_usuario_rol-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		usuario_rol_modal($(this));
	});

	//lista tabla
	usuario_rol_listar_tabla();
});*/

$(function(){
	_onloadPage();
 
	$('#_botonera').html('<li><a href="pages/seguridad/seg_usuario_rol_modal.html" id="seg_usuario_rol-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Rol</a></li>');

	$('#seg_usuario_rol-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		usuario_rol_modal($(this));
	});
	
	//$('#tip_con').change();

	//lista tabla
	

	
	
});

function usuario_rol_eliminar(link){
	_delete('api/usuarioRol/' + $(link).data("id"),
			function(){
					usuario_rol_listar_tabla();
				}
			);
}

function usuario_rol_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_rol_modal.html');
	usuario_rol_modal(link);
	
}

function usuario_rol_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_inputs('seg_usuario_rol-frm');	
		
		if (link.data('id')){
			//$('#id_tra').attr('disabled','disabled');
			_get('api/usuarioRol/' + link.data('id'),
			function(data){
				_llenarComboURL('api/usuario/datos',$('#id_tra'),data.id_usr, null, function(){$('#id_tra').change();});
				$('#login').val(data.usuario.login);
				//_llenarCombo('seg_rol',$('#id_rol'),data.id_rol);
				_llenarComboURL('api/usuario/listarRol',$('#id_rol'),data.id_rol, null, function(){$('#id_rol').change();});
				_fillForm(data,$('.modal').find('form') );
				}
			);
		} else{
			//$('#id_tra').removAttr('disabled');
			_llenarComboURL('api/usuario/datos',$('#id_tra'),null, null, function(){$('#id_tra').change();});
			$('#id_tra').on('change',function(event){
				var login=$('#id_tra option:selected').data('aux1');
				var id_usr=$('#id_tra option:selected').val();
				//alert(id_usr);
				$('#login').val(login);
				$('#id_usr').val(id_usr);
			});
			//_llenarCombo('seg_rol',$('#id_rol'));
			_llenarComboURL('api/usuario/listarRol',$('#id_rol'),null, null, function(){$('#id_rol').change();});
		}
		

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		usuario_rol_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Rol Usuario ';
	else
		titulo = 'Nuevo  Rol Usuario ';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}
function usuario_rol_listar_tabla(){
	var param={tip_con:$("#tip_con").val()};
	_get('api/usuarioRol/listarUsuariosxContrato/',
			function(data){
			console.log(data);
				$('#seg_usuario_rol-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			               {"title":"Trabajador", "render": function ( data, type, row ) {
			            	   return row.per_ape_pat+" "+row.per_ape_mat+" "+row.per_nom;
			               }} ,     
			               {"title":"Usuario", "data" : "usr_login"}, 
			               {"title":"Rol", "data" : "rol_nom"}, 
				           {"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.uro_id + '" onclick="usuario_rol_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.uro_id + '" onclick="usuario_rol_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
			        	   }
				    ]
			    });
			}, param
	);

}