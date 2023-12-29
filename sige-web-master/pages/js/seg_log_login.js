//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/seguridad/seg_log_login_modal.html" id="seg_log_login-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Log de login</a></li>');

	$('#seg_log_login-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		log_login_modal($(this));
	});

	//lista tabla
	log_login_listar_tabla();
});

function log_login_eliminar(link){
	_delete('api/logLogin/' + $(link).data("id"),
			function(){
					log_login_listar_tabla();
				}
			);
}

function log_login_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/seguridad/seg_log_login_modal.html');
	log_login_modal(link);
	
}

function log_login_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('seg_log_login-frm');
		
		$('#seg_log_login-frm #btn-agregar').on('click',function(event){
			$('#seg_log_login-frm #id').val('');
			_post($('#seg_log_login-frm').attr('action') , $('#seg_log_login-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/logLogin/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo2('seg_perfil',$('#id_per'), data.id_per);
			});
		}else{
			_llenarCombo2('seg_perfil',$('#id_per'));
			$('#seg_log_login-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		log_login_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Log de login al sistema';
	else
		titulo = 'Nuevo  Log de login al sistema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function log_login_listar_tabla(){
	_get('api/logLogin/listar/',
			function(data){
			console.log(data);
				$('#seg_log_login-tabla').dataTable({
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
							{"title":"usuario", "data" : "id_usr"}, 
							{"title":"Ip", "data" : "ip"}, 
							{"title":"Exito", "data" : "exito"}, 
							{"title":"Perfil", "data": "perfil.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="log_login_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="log_login_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

