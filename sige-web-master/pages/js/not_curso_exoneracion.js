//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}

function _onchangeAnio(id_anio, anio){
	curso_exoneracion_listar_tabla();
}


//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_curso_exoneracion_modal.html" id="not_curso_exoneracion-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Exoneraci&oacute;n de cursos</a></li>');

	$('#not_curso_exoneracion-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_exoneracion_modal($(this));
	});

	//lista tabla
	curso_exoneracion_listar_tabla();
});

function curso_exoneracion_eliminar(link){
	_delete('api/cursoExoneracion/' + $(link).data("id"),
			function(){
					curso_exoneracion_listar_tabla();
				}
			);
}

function curso_exoneracion_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_curso_exoneracion_modal.html');
	curso_exoneracion_modal(link);
	
}

function curso_exoneracion_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_curso_exoneracion-frm');
		
		$('.detalle').hide();
		
		$("#alumno").autocomplete({
	        minLength: 3,
	        source: _URL + 'api/alumno/autocomplete?id_anio=' + $('#_id_anio').text() + '&id_suc=' + _usuario.id_suc,
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	$('.detalle_alumno').show();
	        	$('#nivel').val(ui.item.nivel);
	        	$('#grado').val(ui.item.grado);
	        	$('#seccion').val(ui.item.secc);
	        	$('#id_mat').val(ui.item.id_mat);
	        	$('.detalle').show();
				$('#not_curso_exoneracion-frm #btn-grabar').show();
				console.log(ui.item.id);
	        	_llenarComboURL('api/curso/porMatricula/' + ui.item.id_mat, $('#id_cur'),null,null, null);
	        	
	        },
	       
	    });
		
		$('#not_curso_exoneracion-frm #btn-agregar').on('click',function(event){
			$('#not_curso_exoneracion-frm #id').val('');
			_post($('#not_curso_exoneracion-frm').attr('action') , $('#not_curso_exoneracion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		
		
		if (link.data('id')){

			$('.detalle').show();
			_get('api/cursoExoneracion/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				var fecha= _parseDate(data.fecha);
				$('#fecha').val(fecha);
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        locale: { format: 'DD/MM/YYYY'}
			    });
				
				$('#alumno').val(data.matricula.alumno.ape_pat + ' ' + data.matricula.alumno.ape_mat + ' ' + data.matricula.alumno.nom );
				_llenarComboURL('api/curso/porMatricula/' + data.id_mat, $('#id_cur'),data.id_cur,null, null);	        	

			});
		}else{
			_llenarCombo('cat_curso',$('#id_cur'));
			//_llenarCombo('mat_matricula',$('#id_mat'));
			$('#not_curso_exoneracion-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_exoneracion_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Exoneraci&oacute;n de cursos';
	else
		titulo = 'Nuevo  Exoneraci&oacute;n de cursos';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function onkeyAlumno(){

 	$('.detalle').hide();
	$('#not_curso_exoneracion-frm #btn-grabar').hide();


}


function curso_exoneracion_listar_tabla(){
	_get('api/cursoExoneracion/listarAnio/' + $('#_id_anio').text(),
			function(data){
			console.log(data);
				$('#not_curso_exoneracion-tabla').dataTable({
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
			        	 	{"title":"Fecha", "data" : "fecha"}, 
			        	 	{"title":"DNI", "data" : "nro_doc"}, 
			        	 	{"title":"Alumno","data" : "alumno"}, 
			        	 	{"title":"Curso", "data": "curso"},
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Resolucion", "data" : "resol"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="curso_exoneracion_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="curso_exoneracion_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
								}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

