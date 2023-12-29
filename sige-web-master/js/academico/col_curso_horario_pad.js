//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_horario_pad_modal.html" id="col_curso_horario_pad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Curso Horario Padre</a></li>');

	$('#col_curso_horario_pad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_horario_pad_modal($(this));
	});

	//lista tabla
	curso_horario_pad_listar_tabla();
});

function curso_horario_pad_eliminar(link){
	_DELETE({url:'api/cursoHorarioPad/' + $(link).data("id"),
			success:function(){
					curso_horario_pad_listar_tabla();
				}
			});
}

function curso_horario_pad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_horario_pad_modal.html');
	curso_horario_pad_modal(link);
	
}

function curso_horario_pad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_horario_pad-frm');
		
		$('#col_curso_horario_pad-frm #btn-agregar').on('click',function(event){
			$('#col_curso_horario_pad-frm #id').val('');
			_POST({form:$('#col_curso_horario_pad-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/cursoHorarioPad/' + link.data('id'),
				  success:	function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('col_aula',$('#id_au'), data.id_au);
				  }
				}
			);
		}else{
			_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('col_aula',$('#id_au'));
			$('#col_curso_horario_pad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_horario_pad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Horario Padre';
	else
		titulo = 'Nuevo  Curso Horario Padre';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_horario_pad_listar_tabla(){
	_GET({ url: 'api/cursoHorarioPad/listar/',
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#col_curso_horario_pad-tabla').dataTable({
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
							{"title":"Fecha Inicio Vigencia", "data" : "fec_ini_vig"}, 
							{"title":"Fecha Fin Vigencia", "data" : "fec_fin_vig"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_pad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_pad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_horario_pad_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

