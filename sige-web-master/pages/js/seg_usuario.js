//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		$('#_botonera').html('<li><a href="pages/seguridad/seg_usuario_modal.html" id="seg_usuario-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Usuario</a></li>');
	} 
	$('#seg_usuario-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		usuario_modal($(this));
	});

	//lista tabla
	usuario_listar_tabla();
	
	
});

function usuario_eliminar(link){
	_delete('api/usuario/' + $(link).data("id"),
			function(){
					usuario_listar_tabla();
				}
			);
}

function usuario_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_usuario_modal.html');
	usuario_modal(link);
	
}

function usuario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('seg_usuario-frm');
		//usuario por defaut a PERSONALIZADO
		$('#id_tra').on('change',function(){
			var nro_doc = $( "#id_tra option:selected" ).data('aux2');
			//alert('nro_doc:' + nro_doc);
			if(nro_doc!='' && typeof nro_doc!='undefined'){
				$('#login').val(nro_doc);
				//$('#password').val(nro_doc);
			}
		});
		
		
		$('#seg_usuario-frm #btn-agregar').on('click',function(event){
			$('#seg_usuario-frm #id').val('');
			_post($('#seg_usuario-frm').attr('action') , $('#seg_usuario-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
			$('#seg_usuario-frm #btn-agregar').hide();
		} else {
			$('#seg_usuario-frm #btn-agregar').show();
		}	
		
		if (link.data('id')){
			_get('api/usuario/' + link.data('id'),
			function(data){
				//alert(data.login);
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('seg_perfil',$('#id_per'), data.usuario.id_per,null, function(){
 					var name = data.usuario.trabajador.ape_pat + ' ' + data.usuario.trabajador.ape_mat + ', ' + data.usuario.trabajador.nom;
					$('#id_tra').append('<option value="'+data.usuario.id_tra+'">'+ name +'</option>' );
					$('#id_tra').val(data.usuario.id_tra);
					$('#login').val(data.usuario.login);
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
						$('#login').attr('readonly','readonly');
					} else {
						$('#login').removeAttr('readonly');
					}	
					$('#password').val(data.usuario.password);
					$('#seg_usuario-frm #id').val(data.usuario.id);
					console.log(data);
					$('#usuario-nivel-tabla').dataTable({
						 data : data.niveles,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 bLengthChange: false,
						 bPaginate: false,
						 bFilter: false,
						 bInfo : false,
						 bSort :false,
						 select: true,
				         columns : [ 
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				        	 	{"title":"Nivel", "data": "niv_nom"},
				        	 	{"title":"Seleccionar","className": 'text-center',"render": function ( data, type, row ) {
				        	 		var checked = (row.sun_id_niv!=null && row.sun_id_niv==row.niv_id) ? ' checked ': ' ';
				        	 		var sun_id = (row.sun_id!=null) ? row.sun_id: '';
				        	 		var id_niv = (row.niv_id!=null) ? row.niv_id: '';
					            	return '<input type="hidden" name="id_usr_niv" value="' + sun_id + '"></input><input type="checkbox" '+checked+' name="id_niveles" value="' + id_niv + '"></input>';
					            }}  
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
						 }
				    });
					
					
					
				});
				
				_llenarCombo('ges_sucursal',$('#id_suc'), data.id_suc);
				}
			);
		}else{
			_llenarCombo('seg_perfil',$('#id_per'));
			_llenarComboURL('api/usuario/listarTrabajador',$('#id_tra'));
			_llenarCombo('ges_sucursal',$('#id_suc'));
			$('#seg_usuario-frm #btn-grabar').hide();
			_get('api/grad/listarNiveles',
			function(data){
				//alert(data.login);
				
					$('#usuario-nivel-tabla').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 bLengthChange: false,
						 bPaginate: false,
						 bFilter: false,
						 bInfo : false,
						 bSort :false,
						 select: true,
				         columns : [ 
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				        	 	{"title":"Nivel", "data": "nom"},
				        	 	{"title":"Seleccionar","className": 'text-center',"render": function ( data, type, row ) {
				        	 		//var checked = (row.sun_id_niv!=null && row.sun_id_niv==row.id) ? ' checked ': ' ';
				        	 		//var sun_id = (row.sun_id!=null) ? row.sun_id: '';
				        	 		var id_niv = (row.id!=null) ? row.id: '';
					            	return '<input type="hidden" name="id_usr_niv" value=""></input><input type="checkbox"  name="id_niveles" value="' + id_niv + '"></input>';
					            }}  
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
						 }
				    });
					
					
					

				
				}
			);
		}
			
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		usuario_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Usuario del sistema';
	else
		titulo = 'Nuevo  Usuario del sistema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function usuario_listar_tabla(){
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 || _usuario.roles.indexOf(_ROL_COORDINADOR_TUTOR)>-1){
		_get('api/usuario/listarUsuariosDocentes',
			function(data){
			console.log(data);
				$('#seg_usuario-tabla').dataTable({
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
			        	 	 {"title":"Trabajador", "data": "trabajador"},
				            {"title":"Perfil", "data": "perfil"}, 
							{"title":"Usuario", "data" : "login"},
							{"title":"Pasword", "data" : "password"}, 
							{"title":"Local", "data" : "sucursal"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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
	} else{
		_get('api/usuario/listar/',
			function(data){
			console.log(data);
				$('#seg_usuario-tabla').dataTable({
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
			        	 	{"title":"Trabajador", "render": function ( data, type, row ) {
				            	   return row.persona.ape_pat+" "+row.persona.ape_mat+" "+row.persona.nom;
				            }} , 
				            {"title":"Perfil", "data": "perfil.nom"}, 
							{"title":"Usuario", "data" : "login"},
							{"title":"Pasword", "data" : "password"}, 
							{"title":"Local", "data" : "sucursal.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="usuario_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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
	

}

