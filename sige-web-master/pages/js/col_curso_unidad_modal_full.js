//Se ejecuta cuando la pagina q lo llama le envia parametros
var _id_uni;
function onloadPage(params){
	//asignamos el id del padre
	//$('#col_uni_sub-tabla').data('id_padre',params.id);

	//unidad_sesion_listar_tabla();
	_inputs('col_curso_unidad_full-frm');
	
	_id_uni = params.id;
	
	if (params.id!=null){
		_get('api/cursoUnidad/' + params.id,
		function(data){
			//_fillForm(data,$('#modal_full').find('form') );
			$('#id').val(data.id);
			$('#nom').val(data.nom);
			$('#des').val(data.des);
			$('#nro_sem').val(data.nro_sem);
			$('#producto').val(data.producto);
			$('#id_niv').on('change',function(event){
				_llenarCombo('cat_grad_todos',$('#id_gra'), data.grad.id, $(this).val(),function(){$('#id_gra').change()});
				var param = {id_niv: $('#id_niv').val() , id_anio: $('#_id_anio').text()};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), data.id_cpu,param,function(){$('#id_cpu').change()});
				});

				$('#id_gra').on('change',function(event){
				var param2 = {id_anio: $('#_id_anio').text(), id_tra:_usuario.id_tra, id_niv: data.nivel.id, id_gra:$('#id_gra').val()};
				_llenarComboURL('api/areaCoordinador/listarCursos',$('#id_cur'), data.curso.id,param2,function(){
					$('#id_cur').change();
					uni_sub_listar_tabla();

				});
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
			
				_llenarComboURL('api/areaCoordinador/listarNiveles/'+ _usuario.id_tra,$('#id_niv'),data.nivel.id, null,function(){$('#id_niv').change()});
				$('#col_curso_unidad_full-frm #id_niv').attr('disabled',true);
				$('#col_curso_unidad_full-frm #id_gra').attr('disabled',true);
				$('#col_curso_unidad_full-frm #id_cur').attr('disabled',true);
				$('#col_curso_unidad_full-frm #id_cpu').attr('disabled',true);
				$('#col_curso_unidad_full-frm #num').attr('disabled',true);
					}
		);
	}else{
				_llenarCombo('cat_nivel',$('#col_curso_unidad-frm #id_niv'));
				_llenarCombo('cat_grad_todos',$('#col_curso_unidad-frm #id_gra'));
				_llenarCombo('cat_curso',$('#col_curso_unidad-frm #id_cur'));
				_llenarCombo('col_per_uni',$('#col_curso_unidad-frm #id_cpu'));
			}
	$('#col_curso_unidad_full-frm #btn-grabar').hide();
	
	var onSuccessSave = function(data){
		curso_unidad_listar_tabla();
	}

}

//edicion completa de una tabla
function curso_unidad_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_unidad_full.html');
	curso_unidad_full_modal(link);

}



function uni_sub_nuevo(row,e){
	 
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/col_uni_sub_modal.html');
	uni_sub_modal(link);
}

function uni_sub_eliminar(link){
	_delete('api/uniSub/' + $(link).data("id"),
			function(){
					uni_sub_listar_tabla();
				}
			);
}

function uni_sub_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_uni_sub_modal.html');
	uni_sub_modal(link);
	
}

function uni_sub_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_uni_sub-frm');
		
		$('#col_uni_sub-frm #btn-agregar').on('click',function(event){
			$('#col_uni_sub-frm #id').val('');
			$('#col_uni_sub-frm  #id_uni').attr('disabled',false); 
			_post($('#col_uni_sub-frm').attr('action') , $('#col_uni_sub-frm').serialize(),
			function(data){
					$(".modal .close").click();
					onSuccessSave(data) ;
					_llenarCombo('cat_curso',$('#col_tema_full-frm #id_cur'), data.id_cur);
				}
			);
			$('#col_uni_sub-frm  #id_uni').attr('disabled',true); 
		});
		
		if (link.data('id')){
			_get('api/uniSub/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('col_curso_unidad',$('#id_uni'), data.id_uni);
				var id_cur=$('#col_curso_unidad_full-frm #id_cur').val();
				var id_niv=$('#col_curso_unidad_full-frm #id_niv').val();
				var id_gra=$('#col_curso_unidad_full-frm #id_gra').val();
				var param = {id_cur: id_cur, id_niv: id_niv, id_gra: id_gra};
				//EN EL 2018 LLENABA UN COMBO
				//A PARTIR DEL 2019 DEBE MOSTRAR UNA GRILLA PARA SELECCIONAR LOS SUBTEMAS
				//_llenarComboURL('api/uniSub/listarSubtemas',$('#id_ccs'), data.id_ccs,param);
				param.id_anio = $('#_id_anio').text();
				param.tip ="E";
				param.id_uni=$('#id_uni').val();
				console.log(param);
				_get('api/uniSub/listarSubtemas',function(data){
					console.log(data);
						$('#subtema-unidad-tabla').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 orderCellsTop:true,
							 bLengthChange: false,
							 bPaginate: false,
							 bFilter: false,
							 bInfo : false,
							 bSort :false,
							 select: true,
					         columns : [ 
					        	 {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					        	 	{"title":"Subtemas", "render":function ( data, type, row,meta ) {
					        	 		var htmlSubtemas = '';
					        	 		for(var i in row.subtemas){
					        	 			var subtema = row.subtemas[i];
					        	 			var subtema_nom = subtema.subtema;
					        	 			htmlSubtemas = htmlSubtemas + '-' + subtema_nom  + '<br>';
					        	 		}
					        	 		return htmlSubtemas;
					        	 	}},
					        	 	{"title":"Seleccionar","className": 'text-center',"render": function ( data, type, row ) {
					        	 		   var checked = row.id_cus!=null ? ' checked ': ' ';
						            	   return '<input type="checkbox"'+checked+' name="id_subtemas" value="' + row.id_cgsp + '"></input><input type="hidden" name="id_cus" value="' + row.id_cus + '"></input>';
						            }}  
						    ],
						    "initComplete": function( settings ) {
								   _initCompleteDT(settings);
							 }
				 }) },param);
				
				
				$('#col_uni_sub-frm  #id_uni').attr('disabled',true); 
			});
		}else{
			//var id=$("#col_curso_unidad_full-frm #id").val();
			//alert(id);
			
			_llenarCombo('col_curso_unidad',$('#id_uni'),$("#col_curso_unidad_full-frm #id").val(),null,function(){
				var id_cur=$('#col_curso_unidad_full-frm #id_cur').val();
				var id_niv=$('#col_curso_unidad_full-frm #id_niv').val();
				var id_gra=$('#col_curso_unidad_full-frm #id_gra').val();
				//alert(id_niv);
				var param = {id_cur: id_cur, id_niv: id_niv, id_gra: id_gra};
				//EN EL 2018 LLENABA UN COMBO
				//A PARTIR DEL 2019 DEBE MOSTRAR UNA GRILLA PARA SELECCIONAR LOS SUBTEMAS
				//_llenarComboURL('api/uniSub/listarSubtemas',$('#id_ccs'), null,param);
				param.id_anio = $('#_id_anio').text();
				param.tip ="N";
				param.id_uni=$('#id_uni').val();
				_get('api/uniSub/listarSubtemas',function(data){
					console.log(data);
						$('#subtema-unidad-tabla').dataTable({
							 data : data,
							 aaSorting: [],
							 destroy: true,
							 orderCellsTop:true,
							 bLengthChange: false,
							 bPaginate: false,
							 bFilter: false,
							 bInfo : false,
							 bSort :false,
							 select: true,
					         columns : [ 
					        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
					        	 	{"title":"Subtemas", "render":function ( data, type, row,meta ) {
					        	 		var htmlSubtemas = '';
					        	 		for(var i in row.subtemas){
					        	 			var subtema = row.subtemas[i];
					        	 			var subtema_nom = subtema.subtema;
					        	 			htmlSubtemas = htmlSubtemas + '-' + subtema_nom  + '<br>';
					        	 		}
					        	 		return htmlSubtemas;
					        	 	}},
					        	 	{"title":"Seleccionar","className": 'text-center',"render": function ( data, type, row ) {
						            	   return '<input type="checkbox" name="id_subtemas" value="' + row.id_cgsp + '"></input>';
						            }}  
						    ],
						    "initComplete": function( settings ) {
								   _initCompleteDT(settings);
							 }
				 }) },param);
			});
			$('#col_uni_sub-frm  #id_uni').attr('disabled',true); 
			$('#col_uni_sub-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		uni_sub_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Unidad Subtema';
	else
		titulo = 'Nuevo  Unidad Subtema';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function uni_sub_listar_tabla(){
	var id_cur=$('#col_curso_unidad_full-frm #id_cur').val();
	var id_niv=$('#col_curso_unidad_full-frm #id_niv').val();
	var id_gra=$('#col_curso_unidad_full-frm #id_gra').val();
	var param = {id_cur: id_cur, id_niv: id_niv, id_gra: id_gra};
	param.id_anio = $('#_id_anio').text();
	param.tip ="L";
	//param.id_uni=$('#id_uni').val();
	param.id_uni=_id_uni;
	_get('api/uniSub/listarSubtemas',
			function(data){
			console.log(data);
				$('#col_uni_sub-tabla').dataTable({
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
			        	 	//{"title":"Tema", "data": "id_cus"}, 
							{"title":"Tema - Subtemas", "render": function ( data, type, row ) {
								var htmlSubtemas = '';
			        	 		for(var i in row.subtemas){
			        	 			var subtema = row.subtemas[i];
			        	 			var subtema_nom = subtema.subtema;
			        	 			htmlSubtemas = htmlSubtemas + '-' + subtema_nom  + '<br>';
			        	 		}
			        	 		return htmlSubtemas;
			                   }
							},
							{"title":"Eliminar", "render": function ( data, type, row ) {
				                   return '<div class="list-icons">' + 
			                		'<a href="#" data-id="' + row.id_cgsp + '" onclick="eliminar_curso_unidad(this)" class="list-icons-item text-danger-600"><i class="icon-trash"></i></a>'
			                	'</div>';}
								}
				    ],
				    "initComplete": function( settings ) {
						   _agregarIcono(settings, 'Agregar Subtema', 'uni_sub_nuevo(this,event)','icon-file-plus','SUBTEMAS');
						   _initCompleteDT(settings);
					    }
			    });
			}, param
	);

}


function eliminar_curso_unidad(row){
	var obj = $(row);
	var id_cgsp = obj.data('id');
	console.log('eliminando:' + id_cgsp);
	_delete('api/uniSub/eliminarxGrupo/' + id_cgsp,
			function(){
					uni_sub_listar_tabla();
				}
			);
	}