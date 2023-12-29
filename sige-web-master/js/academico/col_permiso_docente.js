//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_permiso_docente_modal.html" id="col_permiso_docente-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Permiso Docente</a></li>');

	$('#col_permiso_docente-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		permiso_docente_modal($(this));
	});

	//lista tabla
	permiso_docente_listar_tabla();
});

function permiso_docente_eliminar(link){
	_DELETE({url:'api/permisoDocente/' + $(link).data("id"),
			success:function(){
					permiso_docente_listar_tabla();
				}
			});
}

function permiso_docente_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_permiso_docente_modal.html');
	permiso_docente_modal(link);
	
}

function permiso_docente_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_permiso_docente-frm');
		
		$('#col_permiso_docente-frm #btn-agregar').on('click',function(event){
			$('#col_permiso_docente-frm #id').val('');
			_POST({form:$('#col_permiso_docente-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/permisoDocente/' + link.data('id'),
				  success:	function(data){
					  console.log(data);
				_fillForm(data,$('#modal').find('form') );
				$('#id_gra').on(
						'change',
						function(event) {
							var param = {
								id_tra : $('#id_prof').val(),
								id_grad : $(this).val(),
								id_anio : $('#_id_anio').text(),
								id_gir:$('#id_gir').val()
							}
							_llenarComboURL('api/nota/listarAulaProfesor', $('#id_au'),	data.id_au, param, function() {
										$('#id_au').change();
									});
						});

				$('#id_niv').on(
						'change',
						function(event) {
							var param = {
								id_tra : $('#id_prof').val(),
								id_anio : $('#_id_anio').text(),
								id_niv : $("#id_niv").val()
							}
							_llenarComboURL('api/evaluacion/listarGrados', $('#id_gra'),
									data.grad.id,param, function() {
										$('#id_gra').change();
									});
							
							var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text(), id_gir:$('#id_gir').val()};
							_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
								$('#id_cpu'), data.id_cpu, param1, function() {
									$('#id_cpu').change();
								});

				});
				
				$('#id_gir').on(
						'change',
						function(event) {
						var param = {
								id_tra : $('#id_prof').val(),
								id_anio : $('#_id_anio').text(),
								id_gir : $('#id_gir').val()
							};
						_llenarComboURL('api/evaluacion/listarNiveles', $('#id_niv'),
								data.nivel.id, param, function() {
									$('#id_niv').change();
								});
							
				});
				
				$('#id_prof').on(
						'change',
						function(event) {

							_llenarComboURL('/api/trabajador/listarGirosNegocioxDocente/'+$('#id_prof').val()+'/'+$('#_id_anio').text(),$('#id_gir'),data.giro.id,null,function(){$('#id_gir').change()});

				});
				
				_llenarComboURL('/api/cursoAula/listarTrabajador',$('#id_prof'),data.id_prof,null,function(){$('#id_prof').change()});
				  }
				}
			);
		}else{
			$('#id_gra').on(
					'change',
					function(event) {
						var param = {
							id_tra : $('#id_prof').val(),
							id_grad : $(this).val(),
							id_anio : $('#_id_anio').text(),
							id_gir:$('#id_gir').val()
						}
						_llenarComboURL('api/nota/listarAulaProfesor', $('#id_au'),	null, param, function() {
									$('#id_au').change();
								});
					});

			$('#id_niv').on(
					'change',
					function(event) {
						var param = {
							id_tra : $('#id_prof').val(),
							id_anio : $('#_id_anio').text(),
							id_niv : $("#id_niv").val()
						}
						_llenarComboURL('api/evaluacion/listarGrados', $('#id_gra'),
								null, param, function() {
									$('#id_gra').change();
								});
						
						var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text(), id_gir:$('#id_gir').val()};
						_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
							$('#id_cpu'), null, param1, function() {
								$('#id_cpu').change();
							});

			});
			
			$('#id_gir').on(
						'change',
						function(event) {
						var param = {
								id_tra : $('#id_prof').val(),
								id_anio : $('#_id_anio').text(),
								id_gir : $('#id_gir').val()
							};
						_llenarComboURL('api/evaluacion/listarNiveles', $('#id_niv'),
								null, param, function() {
									$('#id_niv').change();
								});
							
			});
			
			$('#id_prof').on(
					'change',
					function(event) {
						_llenarComboURL('/api/trabajador/listarGirosNegocioxDocente/'+$('#id_prof').val()+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change()});

			});
			_llenarComboURL('/api/cursoAula/listarTrabajador',$('#id_prof'),null,null,function(){$('#id_prof').change()});

			$('#col_permiso_docente-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		permiso_docente_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Permiso Docente';
	else
		titulo = 'Nuevo  Permiso Docente';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function permiso_docente_listar_tabla(){
	//var param={id_anio:$('#_id_anio').text()};
	_GET({ url: 'api/permisoDocente/listarxAnio/'+$('#_id_anio').text(),
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#col_permiso_docente-tabla').dataTable({
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
							{"title":"Profesor", "render": function ( data, type, row ) {
				            	   return row.persona;

				            }} ,
							{"title":"Periodo Académico", "render": function ( data, type, row ) {
				            	   return row.nom+" "+row.nump;
				            }} ,  
							//{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Aula", "render": function ( data, type, row ) {
				            	   return row.grado+" "+row.secc;
				            }} , 
				            {"title":"Fecha", "render": function ( data, type, row ) {
				            	   return _parseDate(row.fec_ins);
				            }} , 
							{"title":"Dias", "data" : "dias"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="permiso_docente_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="permiso_docente_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
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

