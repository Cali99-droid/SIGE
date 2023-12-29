//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

	_llenarCombo('cat_nivel',$('#cat_curso_full-frm #id_niv'),null,null, function(){
		//$('#id_niv').change();	
	});
	$('#id_niv').on('change',function(event){
		if ($(this).val()==''){
			$('#id_grad').val('');
			curso_aula_listar_tabla();
		}else{
			_llenarCombo('cat_grad',$('#cat_curso_full-frm #id_grad'),null,$(this).val(), function (){
				curso_aula_listar_tabla();
			});
		}
	});
 
	$("#btn-clonar").on('click',function(){
		_post('api/cursoAula/clonarAnio' , {id_anio:$('#_id_anio').text()},
			function(data){
			curso_aula_listar_tabla();
			}
		);
	});

	
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_aula_modal.html" id="col_curso_aula-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Curso profesor</a></li>');

	$('#col_curso_aula-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_aula_modal($(this));
	});

	//lista tabla
	curso_aula_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	curso_aula_listar_tabla();
}

function curso_aula_eliminar(link){
	_delete('api/cursoAula/' + $(link).data("id"),
			function(){
					curso_aula_listar_tabla();
				}
			);
}

function curso_aula_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_aula_modal.html');
	curso_aula_modal(link);
	
}

function curso_aula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_aula-frm');
		
		$('#col_curso_aula-frm #btn-agregar').on('click',function(event){
			$('#col_curso_aula-frm #id').val('');
			_post($('#col_curso_aula-frm').attr('action') , $('#col_curso_aula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cursoAula/' + link.data('id'),
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form') );

				//PRIMERO DE DECLARAN LOS EVENTOS
				$('#col_curso_aula-frm #id_au').on('change',function(event){
					var id_suc= $('#col_curso_aula-frm #id_au option:selected').attr('data-aux3');
					var param={id_niv:$('#col_curso_aula-frm #id_niv').val(),id_gra:$('#col_curso_aula-frm #id_gra').val(),id_anio:$("#_id_anio").text(),id_suc:id_suc};
					_llenarComboURL('/api/cursoAula/listarCursos', $('#id_cua'), data.id_cua,param);
				});
				
				$('#col_curso_aula-frm #id_gra').on('change',function(event){
					_llenarCombo('col_aula',$('#col_curso_aula-frm #id_au'),data.id_au,$("#_id_anio").text() + ',' + $(this).val(), function(){$('#col_curso_aula-frm #id_au').change();});
					var id_niv=$('#col_curso_aula-frm #id_niv').val();
				});
				
				$('#col_curso_aula-frm #id_niv').on('change',function(event){
					_llenarCombo('cat_grad_todos',$('#col_curso_aula-frm #id_gra'),data.grad.id,$(this).val(), function(){ $('#col_curso_aula-frm #id_gra').change(); });	
				});
				
				//LUEGO SE LLENAN LOS BOMOS Y/O LANZAR LOS EVENTOS CHANGE
				_llenarCombo('cat_nivel',$('#col_curso_aula-frm #id_niv'),data.niv.id, null, function(){$('#col_curso_aula-frm #id_niv').change();});
				_llenarComboURL('/api/cursoAula/listarTrabajador',$('#id_tra'),data.id_tra);
				
				$('#col_curso_aula-frm #btn-grabar').show();
			});
		}else{
			//PRIMERO DE DECLARAN LOS EVENTOS
			$('#col_curso_aula-frm #id_au').on('change',function(event){
				var id_suc= $('#id_au option:selected').attr('data-aux3');
				var param={id_niv:$('#col_curso_aula-frm #id_niv').val(),id_gra:$('#col_curso_aula-frm #id_gra').val(),id_anio:$("#_id_anio").text(),id_suc:id_suc};
				_llenarComboURL('/api/cursoAula/listarCursos', $('#col_curso_aula-frm #id_cua'), null,param);
			});
			
			$('#col_curso_aula-frm #id_gra').on('change',function(event){
				_llenarCombo('col_aula',$('#col_curso_aula-frm #id_au'),null,$("#_id_anio").text() + ',' + $(this).val(), function(){$('#col_curso_aula-frm #id_au').change();});
				var id_niv=$('#col_curso_aula-frm #id_niv').val();
				
				
			});
			
			$('#col_curso_aula-frm #id_niv').on('change',function(event){
				_llenarCombo('cat_grad_todos',$('#col_curso_aula-frm #id_gra'),null,$(this).val(), function(){ $('#col_curso_aula-frm #id_gra').change(); });
				
			});
			
			/*$('#id_niv').on('change',function(event){
				var param ={id_niv:$('#id_niv').val()};
				//_llenarComboURL('/api/grad/listarTodosGrados',$('#id_gra'),null,param, function(){ $('#id_gra').change(); });
				
			});*/
			
			//LUEGO SE LLENAN LOS BOMOS Y/O LANZAR LOS EVENTOS CHANGE
			_llenarCombo('cat_nivel',$('#col_curso_aula-frm #id_niv'),null, null, function(){$('#col_curso_aula-frm #id_niv').change();});
			_llenarComboURL('/api/cursoAula/listarTrabajador',$('#id_tra'));
			
			$('#col_curso_aula-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_aula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Aula';
	else
		titulo = 'Nuevo  Curso Aula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_aula_listar_tabla(){
	_get('api/cursoAula/listar/',
			function(data){
			console.log(data);
			
			if(data.cantidad==0){
				$("#btn-clonar").css('display','inline-block');
				new PNotify({
				        title: 'Ayuda',
				        text: 'Se puede clonar la configuración del año anterior, en el botón "Clonar desde Año Anterior"',
				        addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
				        delay:10000
				    });
			} else{
					$("#btn-clonar").css('display','none');
			}

			
				$('#col_curso_aula-tabla').dataTable({
					 data : data.cursos,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Nivel", "data": "niv_nom"},
			        	 	{"title":"Grado", "data": "gra_nom"}, 
							{"title":"Aula", "data": "au_secc"}, 
							{"title":"Curso", "data": "cur_nom"}, 
							{"title":"Docente", "render": function ( data, type, row ) {
				            	   return row.tra_ape_pat+" "+row.tra_ape_mat+" "+row.tra_nom;
				            }}, 
				            {"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.cca_id + '" onclick="curso_aula_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.cca_id + '" onclick="curso_aula_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
								}
				            }
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},{id_niv:$('#id_niv').val(), id_gra:$('#id_grad').val(), id_anio:$('#_id_anio').text()}
	);

}

