//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_alumno_grado_ficticio_modal.html" id="cat_alumno_grado_ficticio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Alumno por grado ficticio</a></li>');

	$('#cat_alumno_grado_ficticio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		alumno_grado_ficticio_modal($(this));
	});

	//lista tabla
	alumno_grado_ficticio_listar_tabla();
});

function alumno_grado_ficticio_eliminar(link){
	_delete('api/alumnoGradoFicticio/' + $(link).data("id"),
			function(){
					alumno_grado_ficticio_listar_tabla();
				}
			);
}

function alumno_grado_ficticio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_alumno_grado_ficticio_modal.html');
	alumno_grado_ficticio_modal(link);
	
}

function alumno_grado_ficticio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_alumno_grado_ficticio-frm');
		
		$('#cat_alumno_grado_ficticio-frm #btn-agregar').on('click',function(event){
			$('#cat_alumno_grado_ficticio-frm #id').val('');
			_post($('#cat_alumno_grado_ficticio-frm').attr('action') , $('#cat_alumno_grado_ficticio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/alumnoGradoFicticio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('mat_matricula',$('#id_mat'), data.id_mat);
			});
		}else{
			_llenarCombo('cat_grad',$('#id_gra'));
			_llenarCombo('mat_matricula',$('#id_mat'));
			$('#cat_alumno_grado_ficticio-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_grado_ficticio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno por grado ficticio';
	else
		titulo = 'Nuevo  Alumno por grado ficticio';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function alumno_grado_ficticio_listar_tabla(){
	_get('api/alumnoGradoFicticio/listar/',
			function(data){
			console.log(data);
				$('#cat_alumno_grado_ficticio-tabla').dataTable({
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
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alumno_grado_ficticio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alumno_grado_ficticio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

