//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_tutor_aula_modal.html" id="col_tutor_aula-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Aula del Tutor</a></li>');

	$('#col_tutor_aula-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tutor_aula_modal($(this));
	});

	//lista tabla
	tutor_aula_listar_tabla();
});

function tutor_aula_eliminar(link){
	_delete('api/tutorAula/' + $(link).data("id"),
			function(){
					tutor_aula_listar_tabla();
				}
			);
}

function _onchangeAnio(id_anio, anio){
	tutor_aula_listar_tabla();
}

function tutor_aula_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_tutor_aula_modal.html');
	tutor_aula_modal(link);
	
}

function tutor_aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_tutor_aula-frm');
		
		$('#col_tutor_aula-frm #btn-agregar').on('click',function(event){
			$('#col_tutor_aula-frm #id').val('');
			_post($('#col_tutor_aula-frm').attr('action') , $('#col_tutor_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/tutorAula/' + link.data('id'),
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form') );
				//_llenarCombo('col_aula',$('#id_aula'), data.id_aula);
				//_llenarCombo('ges_trabajador',$('#id_tra'), data.id_tra);
				
				$('#id_gra').on('change',function(event){
					_llenarCombo('col_aula',$('#id_au'),data.aula.id,$("#_id_anio").text() + ',' + $(this).val(), function(){$('#id_au').change();});
					var id_niv=$('#id_niv').val();
				});
				$('#id_niv').on('change',function(event){
					_llenarCombo('cat_grad_todos',$('#id_gra'),data.aula.id_grad,$(this).val(), function(){ $('#id_gra').change(); });
					
				});
				_llenarCombo('cat_nivel',$('#id_niv'),data.nivel.id, null, function(){$('#id_niv').change();});
				_llenarComboURL('api/tutorAula/listarProfesor',$('#id_tra'), data.trabajador.id);
				//_llenarCombo('col_aula',$('#id_aula'));
				//_llenarCombo('ges_trabajador',$('#id_tra'));
				
			});
		}else{

			$('#id_gra').on('change',function(event){
				_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val(), function(){$('#id_au').change();});
				var id_niv=$('#id_niv').val();
			});
			$('#id_niv').on('change',function(event){
				_llenarCombo('cat_grad_todos',$('#id_gra'),null,$(this).val(), function(){ $('#id_gra').change(); });
				
			});
			_llenarCombo('cat_nivel',$('#id_niv'),null, null, function(){$('#id_niv').change();});
			
			_llenarComboURL('api/tutorAula/listarProfesor',$('#id_tra'));
			$('#col_tutor_aula-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tutor_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Aula del Tutor';
	else
		titulo = 'Nuevo  Aula del Tutor';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tutor_aula_listar_tabla(){
	var param ={id_anio: $('#_id_anio').text()};
	_get('api/tutorAula/listar/',
			function(data){
			console.log(data);
				$('#col_tutor_aula-tabla').dataTable({
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
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Aula", "data": "aula.secc"}, 
							{"title":"Docente", "render": function ( data, type, row ) {
				            	   return row.trabajador.ape_pat+" "+row.trabajador.ape_mat+" "+row.trabajador.nom;
				            }} ,
				            {"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="tutor_aula_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.id + '" onclick="tutor_aula_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'	
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

