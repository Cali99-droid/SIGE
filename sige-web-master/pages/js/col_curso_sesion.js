//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_sesion_modal.html" id="col_curso_sesion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Sesiones del Curso</a></li>');

	$('#col_curso_sesion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_sesion_modal($(this));
	});

	//lista tabla
	curso_sesion_listar_tabla();
});

function curso_sesion_eliminar(link){
	_delete('api/cursoSesion/' + $(link).data("id"),
			function(){
					curso_sesion_listar_tabla();
				}
			);
}

function curso_sesion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_sesion_modal.html');
	curso_sesion_modal(link);
	
}

function curso_sesion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_sesion-frm');
		
		$('#col_curso_sesion-frm #btn-agregar').on('click',function(event){
			$('#col_curso_sesion-frm #id').val('');
			_post($('#col_curso_sesion-frm').attr('action') , $('#col_curso_sesion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoSesion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('col_area_anio',$('#id_caa'), data.id_caa);
				_llenarCombo('cat_curso',$('#id_cur'), data.id_cur);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_grad',$('#id_gra'));
			_llenarCombo('col_area_anio',$('#id_caa'));
			_llenarCombo('cat_curso',$('#id_cur'));
			$('#col_curso_sesion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_sesion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Sesiones del curso';
	else
		titulo = 'Nuevo  Sesiones del curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_sesion_listar_tabla(){
	_get('api/cursoSesion/listar/',
			function(data){
			console.log(data);
				$('#col_curso_sesion-tabla').dataTable({
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
							{"title":"Numero de Sesiones", "data" : "nro_ses"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"&Aacute;rea", "data": "areaAnio.ord"}, 
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_sesion_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_sesion_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

