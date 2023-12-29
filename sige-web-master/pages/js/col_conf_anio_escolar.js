//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/configuracion/col_conf_anio_escolar_modal.html" id="col_conf_anio_escolar-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Anio</a></li>');

	$('#col_conf_anio_escolar-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		conf_anio_escolar_modal($(this));
	});

	//lista tabla
	conf_anio_escolar_listar_tabla();
});

function conf_anio_escolar_eliminar(link){
	_delete('api/confAnioEscolar/' + $(link).data("id"),
			function(){
					conf_anio_escolar_listar_tabla();
				}
			);
}

function conf_anio_escolar_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/configuracion/col_conf_anio_escolar_modal.html');
	conf_anio_escolar_modal(link);
	
}

function conf_anio_escolar_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_conf_anio_escolar-frm');
		$("#id_anio").attr('disabled', true);
		$('#col_conf_anio_escolar-frm #btn-agregar').on('click',function(event){
			$('#col_conf_anio_escolar-frm #id').val('');
			$("#id_anio").removeAttr("disabled")
			_post($('#col_conf_anio_escolar-frm').attr('action') , $('#col_conf_anio_escolar-frm').serialize(),
			function(data){
					$("#id_anio").attr('disabled', true);
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/confAnioEscolar/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				$('#col_conf_anio_escolar-frm #btn-agregar').hide();
			});
		}else{
			_llenarCombo('col_anio',$('#id_anio'),$('#_id_anio').text());
			$('#col_conf_anio_escolar-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_anio_escolar_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar A&ntilde;o Acad&eacute;mico';
	else
		titulo = 'Nuevo  A&ntilde;o Acad&eacute;mico';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function conf_anio_escolar_listar_tabla(){
	_get('api/confAnioEscolar/listar/',
			function(data){
			console.log(data);
				$('#col_conf_anio_escolar-tabla').dataTable({
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
							{"title":"N&uacute;mero de Semanas", "data" : "nro_sem"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_anio_escolar_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_anio_escolar_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

