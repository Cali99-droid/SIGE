//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_subtema_modal.html" id="col_curso_subtema-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tema</a></li>');
	$("#btn-clonar").css('display','block');
	$('#col_curso_subtema-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_subtema_modal($(this));
	});
	
	$("#id_anio").val($("#_id_anio").text());
	//lista tabla
	//
	
	// eventos
	$('#id_niv_bus').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $(this).val()};
		console.log(param);
		_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra_bus'), null,param, function(){$('#id_gra_bus').change();});
	});

	$('#id_gra_bus').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv_bus').val(), id_gra:$('#id_gra_bus').val()};
		_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur_bus'), null,param,function(){$('#id_cur_bus').change();});
		
	});
	
	$('#id_cur_bus').on('change',function(event){
		curso_subtema_listar_tabla();
	});
		
	//llenar niveles
	_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv_bus'),null, null, function(){
		$('#id_niv_bus').change();
	});
	
	
});

function _onchangeAnio(id_anio, anio){
	$('#id_niv_bus').change();
	$('#btn-buscar').click();
}

function curso_subtema_eliminar(link){
	_delete('api/cursoSubtema/' + $(link).data("id"),
			function(){
					curso_subtema_listar_tabla();
				}
			);
}

function curso_subtema_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_subtema_modal.html');
	curso_subtema_modal(link);
	
}

$('#btn-buscar').on('click',function(event){
	curso_subtema_listar_tabla();	
});

$('#btn-clonar').on('click',function(event){
	event.preventDefault();	
	_post('api/cursoSubtema/clonar/'+$('#_id_anio').text(),
			function(data){
			console.log(data);
			//per_uni_listar_tabla();	
	});
	curso_subtema_listar_tabla();	
});

function curso_subtema_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_subtema-frm');
		
		$('#col_curso_subtema-frm #btn-agregar').on('click',function(event){
			$('#col_curso_subtema-frm #id').val('');
			$('#id_gra').removeAttr('disabled');
			$('#id_niv').removeAttr('disabled');
			_post($('#col_curso_subtema-frm').attr('action') , $('#col_curso_subtema-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					$('#id_gra').prop('disabled', 'disabled');
					$('#id_niv').prop('disabled', 'disabled');
				}
			);
		});
		
		$('#id_gra').prop('disabled', 'disabled');
		$('#id_niv').prop('disabled', 'disabled');
		
		if (link.data('id')){
			_get('api/cursoSubtema/' + link.data('id'),
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form') );
				/*_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				//_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				
				_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'));

				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('cat_curso',$('#id_cur'), data.id_cur);
				_llenarCombo('col_subtema',$('#id_sub'), data.id_sub);*/
				
				// eventos

				/*$('#id_cur').on('change',function(event){
					//_llenarCombo('col_subtema',$('#id_sub'),data.id_sub,$('#id_cur').val()+ ',' + $('#id_niv').val(),function(){$('#id_sub').change()});
 					_llenarComboApi('api/subtema/listarSubtemasxAnioCurso?id_cur=' + $('#id_cur').val() + '&id_anio=' + $('#_id_anio').text(), 'value', $('#id_sub'), data.id_sub, function(){$('#id_sub').change()});

				});*/
				

				$('#id_cur').on('change',function(event){
					//_llenarCombo('col_subtema',$('#id_sub'),null,$('#id_cur').val()+ ',' + $('#id_niv').val());
					var param={id_cur:$('#id_cur').val(),id_anio: $('#_id_anio').text(),id_niv:$('#id_niv').val()};
					_llenarComboURL('api/subtema/listarSubtemasxAnioCurso',$('#id_sub'),data.id_sub,param);
					//_llenarComboApi('api/subtema/listarSubtemasxAnioCurso?id_cur=' + $('#id_cur').val() + '&id_anio=' + $('#_id_anio').text() + '&id_niv=' + $('#id_niv').val(), 'value', $('#id_sub'), null, function(){$('#id_sub').change()});

				});	
				
				$('#id_gra').on('change',function(event){
					var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val()};
					param.id_gra = $('#id_gra').val();
					//alert( data.id_cur);
					_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), data.id_cur,param, function(){$('#id_cur').change()});
				});
					
				$('#id_niv').on('change',function(event){
					var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $(this).val()};
					console.log(param);
					_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra'), data.id_gra,param, function(){$('#id_gra').change()});
				});
				
				//llenar niveles
				_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),data.id_niv,null, function(){$('#id_niv').change()});
				
				_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				
			});
		}else{
			_llenarCombo('col_anio',$('#id_anio'),$('#_id_anio').text());
			
			// eventos

			$('#id_cur').on('change',function(event){
				//_llenarCombo('col_subtema',$('#id_sub'),null,$('#id_cur').val()+ ',' + $('#id_niv').val());
				var param={id_cur:$('#id_cur').val(),id_anio: $('#_id_anio').text(),id_niv:$('#id_niv').val()};
				_llenarComboURL('api/subtema/listarSubtemasxAnioCurso',$('#id_sub'),null,param);
				//_llenarComboApi('api/subtema/listarSubtemasxAnioCurso?id_cur=' + $('#id_cur').val() + '&id_anio=' + $('#_id_anio').text() + '&id_niv=' + $('#id_niv').val(), 'value', $('#id_sub'), null, function(){$('#id_sub').change()});

			});	
			
			$('#id_gra').on('change',function(event){
				var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val()};
				param.id_gra = $('#id_gra').val();
				_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), $('#id_cur_bus').val(),param,function(){$('#id_cur').change();});
			});
			
			$('#id_niv').on('change',function(event){
				var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $(this).val()};
				console.log(param);
				_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra'), $('#id_gra_bus').val(),param, function(){$('#id_gra').change();});
				
			});
			
			
			//llenar niveles
			_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),$('#id_niv_bus').val(), null, function(){
				$('#id_niv').change();
			});
			
			
			$('#col_curso_subtema-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_subtema_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Curso Subtema';
	else
		titulo = 'Nuevo  Curso Subtema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_subtema_listar_tabla(){
	var param = {id_anio: $("#_id_anio").text(), id_tra:_usuario.id_tra, id_gra: $("#id_gra_bus").val(), id_cur:$("#id_cur_bus").val()};

	_get('api/cursoSubtema/listar/',
			function(data){
			console.log(data);
			if(_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
			if(data.length==0){
				$("#btn-clonar").css('display','block');
				new PNotify({
			         title: 'Ayuda',
			         text: 'Se puede clonar la configuración del año anterior, en el botón "Clonar Año Anterior"',
			         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
			         delay:10000
			     });
			} else{
				$("#btn-clonar").css('display','none');
			}} else{
				$("#btn-clonar").css('display','none');
			}
				$('#col_curso_subtema-tabla').dataTable({
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
							{"title":"Curso", "data": "curso.nom"}, 
							{"title":"Tema", "data": "tema.nom"}, 
							{"title":"Subtema", "data": "subtema.nom"}, 
							{"title":"No de Semanas", "data" : "dur"},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="curso_subtema_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="curso_subtema_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

