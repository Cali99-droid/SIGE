//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_curso_unidad_modal.html" id="col_curso_unidad-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Unidad</a></li>');

	$('#col_curso_unidad-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		curso_unidad_modal($(this));
	});

	//lista tabla
	//curso_unidad_listar_tabla();
	// eventos
	
	$('#id_cur_bus').on('change',function(event){
		curso_unidad_listar_tabla();
	});
	
	$('#id_gra_bus').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv_bus').val(), id_gra:$('#id_gra_bus').val()};
		_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur_bus'), null,param,function(){$('#id_cur_bus').change();});
		
	});
	
	$('#id_niv_bus').on('change',function(event){
		var param = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $(this).val()};
		console.log(param);
		_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra_bus'), null,param, function(){$('#id_gra_bus').change();});
	});
		
	//llenar niveles
	_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv_bus'),null, null, function(){
		$('#id_niv_bus').change();
	});
});

function _onchangeAnio(id_anio, anio){
	curso_unidad_listar_tabla();	
}


function curso_unidad_eliminar(link){
	_delete('api/cursoUnidad/' + $(link).data("id"),
			function(){
					curso_unidad_listar_tabla();
				}
			);
}

function curso_unidad_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_modal.html');
	curso_unidad_modal(link);
	
}

function curso_unidad_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_unidad-frm');
		$('#id_anio').val($('#_id_anio').text());
		
		$('#col_curso_unidad-frm #btn-agregar').on('click',function(event){
			$('#col_curso_unidad-frm #id').val('');
			$('#id_gra').removeAttr('disabled');
			$('#id_niv').removeAttr('disabled');
			$('#id_cur').removeAttr('disabled');
			_post($('#col_curso_unidad-frm').attr('action') , $('#col_curso_unidad-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					$('#id_gra').prop('disabled', 'disabled');
					$('#id_niv').prop('disabled', 'disabled');
					$('#id_cur').prop('disabled', 'disabled');
				}
			);
		});

		$('#id_gra').prop('disabled', 'disabled');
		$('#id_niv').prop('disabled', 'disabled');
		$('#id_cur').prop('disabled', 'disabled');
		if (link.data('id')){
			_get('api/cursoUnidad/' + link.data('id'),
			function(data){
				console.log(data);


				_fillForm(data,$('#modal').find('form') ); 

				$('#id_niv').on('change',function(event){
				//_llenarCombo('cat_grad_todos',$('#id_gra'), data.grad.id, $(this).val(), function(){$('#id_gra').change()});//no se ejecuta el change del grado
				var param1 = {
							id_anio : $('#_id_anio').text(),
							id_tra : _usuario.id_tra,
							id_niv : $(this).val()
				};
				
				_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra'), data.grad.id, param1, function() {$('#id_gra').change();});
				var param = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), data.id_cpu,param,function(){$('#id_cpu').change()});
				});

				$('#id_gra').on('change',function(event){
				var param2 = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: data.nivel.id, id_gra:$('#id_gra').val()};
				_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), data.curso.id,param2);
				});
			
				$('#id_cpu').on('change',function(event){
				var uni_ini=$('#id_cpu option:selected').data('aux2');
				var uni_fin=$('#id_cpu option:selected').data('aux3');
				var options = "";
				for (var i = uni_ini; i <= uni_fin; i++) {
					if(data.num==i)
				    options += '<option value="' +i+ '" selected>UNIDAD ' + i+ '</option>';
					else
					options += '<option value="' +i+ '" >UNIDAD ' + i+ '</option>';
				}
				$("#num").html(options);
				});	
			
				_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),data.nivel.id, null, function(){$('#id_niv').change()});
				$('#col_curso_unidad-frm #btn-agregar').hide();
			});
		}else{
			//ACA ES NUEVO
			$('#id_cpu').on('change',function(event){
				var uni_ini=$('#id_cpu option:selected').data('aux2');
				var uni_fin=$('#id_cpu option:selected').data('aux3');
				var options = "";
				for (var i = uni_ini; i <= uni_fin; i++) {
				    options += '<option value="' +i+ '">UNIDAD ' + i+ '</option>';
				}
				$("#num").html(options);
			});	
			
			$('#id_gra').on('change',function(event){
				var param2 = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: $('#id_niv').val(), id_gra: $('#id_gra').val()};
					_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), $('#id_cur_bus').val(),param2,function(){$('#id_cur').change();});	
			});
			
			$('#id_niv').on('change',function(event){
				//_llenarCombo('cat_grad_todos',$('#id_gra'), null, $(this).val(), function(){ $('#id_gra').change(); });//no se ejecuta el change del grado
				var param1 = {
						id_anio : $('#_id_anio').text(),
						id_tra : _usuario.id_tra,
						id_niv : $(this).val()
				};
				_llenarComboURL('api/areaCoordinador/listarGrados',$('#id_gra'), $('#id_gra_bus').val(), param1, function() {$('#id_gra').change();});
				var param = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null,param,function(){$('#id_cpu').change();});
			});

			
			
			
			_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),$('#id_niv_bus').val(), null, function(){
				$('#id_niv').change();
			});
			
			$('#col_curso_unidad-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_unidad_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Did&aacute;ctica';
	else
		titulo = 'Nuevo  Unidad Did&aacute;ctica';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_unidad_listar_tabla(){
	var param = {id_anio: $("#_id_anio").text(), id_tra:_usuario.id_tra, id_gra: $("#id_gra_bus").val(), id_cur:$("#id_cur_bus").val()};
	_get('api/cursoUnidad/listarUnidadesTrabajador/',
			function(data){
			console.log(data);
				$('#col_curso_unidad-tabla').dataTable({
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
			        	 	{"title":"Nivel", "data": "nivel"}, 
			        	 	{"title":"Grado", "data": "grado"}, 
			        	 	{"title":"Curso", "data": "curso"}, 
			        	 	{"title":"Nombre", "data" : "nom"},
							{"title":"N&uacute;mero de Unidad", "data" : "num"}, 
							{"title":"Producto", "data" : "producto"}, 			
							{"title":"Acciones", "render": function ( data, type, row ) {
			                  /* return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									//'<li><a href="#" data-id="' + row.id + '" onclick="curso_unidad_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="_send(\'pages/academico/col_curso_unidad_full.html\',\'Configuracion Subtemas\',\'Edici&oacute;n avanzada\',{id:' + row.id +'})"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
									
								'</ul>'+
			                   '</li>'+
			                   '</ul>';*/
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="curso_unidad_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
										' <a href="#" data-id="' + row.id + '" onclick="_send(\'pages/academico/col_curso_unidad_full.html\',\'Configuracion Subtemas\',\'Edici&oacute;n avanzada\',{id:' + row.id +'})" class="list-icons-item" title="Configuraci&oacute;n de Subtemas"><i class="fa fa-list-alt mr-2 ui-blue ui-size" aria-hidden="true"></i></a></div>';
			                   
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

