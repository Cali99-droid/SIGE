//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_llenarCombo('cat_nivel',$('#id_niv'),null,null, function(){
		$('#id_niv').change();	
	});
	$('#id_niv').on('change',function(event){
		if ($(this).val()==''){
			$('#id_grad').val('');
			curso_anio_listar_tabla();
		}else{
			_llenarCombo('cat_grad',$('#id_grad'),null,$(this).val(), function (){
				curso_anio_listar_tabla();
			});
		}
	});
 
	$("#btn-clonar").on('click',function(){
		_post('api/cursoAnio/clonarAnio' , {id_anio:$('#_id_anio').text()},
			function(data){
				curso_anio_listar_tabla();
			}
		);
	});

	
}

function _onchangeAnio(id_anio, anio){
	curso_anio_listar_tabla();
}

//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_anio_modal.html" id="col_curso_anio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Curso por a&ntilde;o</a></li>');

	$('#col_curso_anio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_anio_modal($(this));
	});

	//lista tabla
	//curso_anio_listar_tabla();
});

function curso_anio_eliminar(link){
	_delete('api/cursoAnio/' + $(link).data("id"),
			function(){
					curso_anio_listar_tabla();
				}
			);
}

function curso_anio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_anio_modal.html');
	curso_anio_modal(link);
	
}

function curso_anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_anio-frm');
		
		$('#col_curso_anio-frm #btn-agregar').on('click',function(event){
			$('#col_curso_anio-frm #id').val('');
		//	$('#id_cur_ses').val('');
		//	$('#nro_ses').val('');
			var frm = $('#col_curso_anio-frm');
			//var validator = frm.validate();
			
			if ($("#col_curso_anio-frm").valid()){
				_post($('#col_curso_anio-frm').attr('action') , $('#col_curso_anio-frm').serialize(),
						function(data){
								onSuccessSave(data) ;
								$('#col_curso_anio-frm #id').val('');
							}
						);				
			}

		});
		
		if (link.data('id')){
			
			_get('api/cursoAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				
				$('#id_caa').on('change',function(event){
					var param ={id_niv:$('#id_per option:selected').data('aux2'), id_gra:$('#id_gra').val(), id_caa:$('#id_caa').val(), id_cur:$('#id_cur').val()}
					_get('api/cursoAnio/obtenerNroSes', function(data) {
						console.log(data);
						if(data!=null){
							$('#id_cur_ses').val(data.id);
							$('#nro_ses').val(data.nro_ses);
						} else{
							$('#id_cur_ses').val('');
							$('#nro_ses').val('');
						}						
					},param);
				});
				
				//aqui
				$('#id_gra').on('change',function(event){
					param1={id_niv:$('#id_cic option:selected').attr("data-aux2"),id_anio: $("#_id_anio").text()};
					_llenarComboURL('api/cursoAnio/listarAreas',$('#id_caa'),data.id_caa, param1,function(){$('#id_caa').change()});
					
					
					
				});
				$('#id_cur').on('change',function(event){
					$('#id_gra').change();
				});
				
				$('#id_cic').on('change',function(event){
					var param={id_niv:$('#id_cic option:selected').attr("data-aux2")};
					_llenarComboURL('api/grado/listarTodosGrados',$('#col_curso_anio-frm #id_gra'),data.id_gra, param,function(){$('#id_gra').change()});
				
				});					
				
				$('#id_per').on('change',function(event){
										
					var param={id_per:$('#id_per').val()};
					_llenarComboURL('api/periodo/listarCiclosCombo', $('#id_cic'),
					data.id_cic, param, function() {
						$('#id_cic').change();
					});
					
				});
				//_llenarCombo('per_periodo',$('#id_per'), data.id_per,$("#_id_anio").text(),function(){$('#id_per').change()});
				var param={id_anio:$('#_id_anio').text()};
				_llenar_combo({
					url:'api/periodo/listar',
					combo:$('#id_per'),
					params: param,
					context:_URL,
					text:'value',
					id: data.id_per,
					funcion:function(){
						$('#id_per').change();
					}
				});
				
				_llenarCombo('cat_curso',$('#id_cur'), data.id_cur);
			});
			
		}else{
			$('#id_gra').on('change',function(event){
				var param ={id_niv:$('#id_per option:selected').data('aux2'), id_gra:$('#id_gra').val(), id_caa:$('#id_caa').val(), id_cur:$('#id_cur').val()}
				_get('api/cursoAnio/obtenerNroSes', function(data) {
					console.log(data);
					if(data!=null){
						$('#id_cur_ses').val(data.id);
						$('#nro_ses').val(data.nro_ses);
					} else{
						$('#id_cur_ses').val('');
						$('#nro_ses').val('');
					}						
				},param);
			});
			
			$('#id_cur').on('change',function(event){
				$('#id_gra').change();
			});
			
			_llenarCombo('cat_curso',$('#id_cur'));
			
			$('#id_cic').on('change',function(event){
				var param={id_niv:$('#id_cic option:selected').attr("data-aux2")};
				_llenarComboURL('api/grado/listarTodosGrados',$('#col_curso_anio-frm #id_gra'),null, param,function(){$('#id_gra').change()});
				param1={id_niv:$('#id_cic option:selected').attr("data-aux2"),id_anio: $("#_id_anio").text()};
				_llenarComboURL('api/cursoAnio/listarAreas',$('#id_caa'),null, param1);
			});	
			
			$('#id_per').on('change',function(event){				
				var param={id_per:$('#id_per').val()};
				_llenarComboURL('api/periodo/listarCiclosCombo', $('#id_cic'),
				null, param, function() {
					$('#id_cic').change();
				});
				
			});
			//_llenarCombo('per_periodo',$('#id_per'),null,$("#_id_anio").text());
			var param={id_anio:$('#_id_anio').text()};
			_llenar_combo({
				url:'api/periodo/listar',
				combo:$('#id_per'),
				params: param,
				context:_URL,
				text:'value',
				//id: data.id_per,
				funcion:function(){
					$('#id_per').change();
				}
			});

			//_attachEvent('id_per','id_gra_todos');
			
			$('#col_curso_anio-frm #btn-grabar').hide();
		}

	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso';
	else
		titulo = 'Nuevo  Curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_anio_listar_tabla(){
	_get('api/cursoAnio/listar/',
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
			
				$('#col_curso_anio-tabla').dataTable({
					 data : data.cursos,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 pageLength: 100,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,66

					 
					 select: true,
			         columns : [
			        	 //		String sql = "select cua.id cua_id, cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ,cua.est cua_est ";

			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Local", "data": "suc_nom"},
			        	 	{"title":"Nivel", "data": "niv_nom"},
			        	 	{"title":"Grado", "data": "gra_nom"}, 
			        	 	{"title":"&Aacute;rea", "data": "a_nom"}, 
			        	 	{"title":"Nro. Sesiones", "data": "ccs_nro_ses"}, 
			        	 	{"title":"Curso", "data": "cur_nom"}, 
							{"title":"Peso de evaluaci&oacute;n", "data" : "cua_peso"}, 
							{"title":"Orden", "data" : "cua_orden"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.cua_id + '" onclick="curso_anio_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.cua_id + '" onclick="curso_anio_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

