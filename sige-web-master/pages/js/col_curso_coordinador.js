//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	//lista tabla
	curso_coordinador_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	curso_coordinador_listar_tabla();
}


function curso_coordinador_eliminar(link){
	_delete('api/cursoCoordinador/' + $(link).data("id"),
			function(){
					curso_coordinador_listar_tabla();
				}
			);
}

function curso_coordinador_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_curso_coordinador_modal.html');
	curso_coordinador_modal(link);
	
}

function curso_coordinador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_curso_coordinador-frm');
		
		$('#id_anio').val($('#_id_anio').text());
		
		$('#col_curso_coordinador-frm #btn-agregar').on('click',function(event){
			$('#col_curso_coordinador-frm #id').val('');
			_post($('#col_curso_coordinador-frm').attr('action') , $('#col_curso_coordinador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		//alert(link.data('id'));

		if (link.data('id')){
			
			_get('api/cursoCoordinador/' + link.data('id'),
			function(data){
				console.log(data);
				if(data==null){
					_llenarCombo('cat_nivel',$('#id_niv'),link.data('id_niv'));
					_llenarCombo('cat_area',$('#id_area'),link.data('id_area'));
					$('#id_cur').val(link.data('id_cur'));
					$('#curso').val(link.data('curso').toUpperCase());
					_llenarComboURL('api/cursoCoordinador/coordinadoresDisponibles?id_anio=' + $('#_id_anio').text() + "&id_niv=" + link.data('id_niv') + "&id_cur=" + link.data('id_cur'),$('#id_tra'));
					$('#col_curso_coordinador-frm #btn-grabar').hide();
				}else{
					_fillForm(data,$('#modal').find('form') );
					_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
					_llenarCombo('cat_area',$('#id_area'), data.id_area);
					$('#curso').val(link.data('curso').toUpperCase());
					$('#id_cur').val(link.data('id_cur'));
					//_llenarComboURL('api/cursoCoordinador/coordinadoresDisponibles/' + link.data('id_cur'),$('#id_tra'), data.id_tra);
					_llenarComboURL('api/cursoCoordinador/coordinadoresDisponibles?id_anio=' + $('#_id_anio').text() + "&id_niv=" + link.data('id_niv') + "&id_cur=" + link.data('id_cur'),$('#id_tra'), data.id_tra);
				}
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'),link.data('id_niv'));
			_llenarCombo('cat_area',$('#id_area'),link.data('id_area'));
			$('#id_cur').val(link.data('id_cur'));
			$('#curso').val(link.data('curso').toUpperCase());
			_llenarComboURL('api/cursoCoordinador/coordinadoresDisponibles?id_anio=' + $('#_id_anio').text() + "&id_niv=" + link.data('id_niv') + "&id_cur=" + link.data('id_cur'),$('#id_tra'));
			$('#col_curso_coordinador-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		curso_coordinador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Coordinador por Curso';
	else
		titulo = 'Nuevo Coordinador por Curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function curso_coordinador_listar_tabla(){
	_get('api/cursoCoordinador/listaCursoxNivel/' + $('#_id_anio').text(),
			function(data){
			console.log(data);
				$('#col_curso_coordinador-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Area", "data": "nom_area"}, 
			        	 	{"title":"Curso", "data": "curso"}, 
							{"title":"Inicial", "render": function ( data, type, row ) {
								var sel = parseInt(row.inicial);
								if (sel>0){
									//verificar si tiene coordinadores
									var coordinadores = row.coordinadores;
									if(coordinadores ==null){
										//return '<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>';
										return '<ul class="icons-list">'+
										'<li class="dropdown">'+
										'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
										'<ul class="dropdown-menu dropdown-menu-right">'+
										'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=1 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
										'</ul>'+
					                   '</li>'+
					                   '</ul>';
									}else{
										 
										var html = '';
										var totCordinadores = 0;
										for(var coordinador in coordinadores ){
											if (coordinadores[coordinador].id_niv==1){
												html+='<ul class="icons-list">'+
												'<li class="dropdown">'+
													'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
														coordinadores[coordinador].trabajador.ape_pat  + ' ' + coordinadores[coordinador].trabajador.ape_mat + ', ' + coordinadores[coordinador].trabajador.nom +
														'</a>'+
													'<ul class="dropdown-menu dropdown-menu-right">'+
													'<li><a href="#" data-id="' + coordinadores[coordinador].id + '"  data-id_niv=1 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
													'<li><a href="#" data-id="' + coordinadores[coordinador].id + '" onclick="curso_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
													'</ul>'+
								                   '</li>'+
								                   '</ul>';
												totCordinadores ++;
											}
										}
										
										if(totCordinadores==0)
											return '<ul class="icons-list">'+
												'<li class="dropdown">'+
												'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
												'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=1 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
												'</ul>'+
							                   '</li>'+
							                   '</ul>';											
										else
											return html;										
									}
								}else{
									return "";
								}
							}},
							{"title":"Primaria", "render": function ( data, type, row ) {
								var sel = parseInt(row.primaria);
								if (sel>0){
									//verificar si tiene coordinadores
									var coordinadores = row.coordinadores;
									if(coordinadores ==null){
										return '<ul class="icons-list">'+
										'<li class="dropdown">'+
										'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
										'<ul class="dropdown-menu dropdown-menu-right">'+
										'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=2 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
										'</ul>'+
					                   '</li>'+
					                   '</ul>';
									}else{

										var html='';
										var totCordinadores = 0;
										for(var coordinador in coordinadores ){
											if (coordinadores[coordinador].id_niv==2){
												html+='<ul class="icons-list">'+
												'<li class="dropdown">'+
												'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
													coordinadores[coordinador].trabajador.ape_pat  + ' ' + coordinadores[coordinador].trabajador.ape_mat + ', ' + coordinadores[coordinador].trabajador.nom +
													'</a>'+
												'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + coordinadores[coordinador].id + '"  data-id_niv=2 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
												'<li><a href="#" data-id="' + coordinadores[coordinador].id + '" onclick="curso_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
												'</ul>'+
							                   '</li>'+
							                   '</ul>';
											  totCordinadores ++;
											}
										}
										
										if(totCordinadores ==0){
											return '<ul class="icons-list">'+
											'<li class="dropdown">'+
											'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
											'<ul class="dropdown-menu dropdown-menu-right">'+
											'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=2 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
											'</ul>'+
						                   '</li>'+
						                   '</ul>';
										}else
											return html;
									}
								}else{
									return "";
								}
							}},
							{"title":"Secundaria", "render": function ( data, type, row ) {
								var sel = parseInt(row.secundaria);
								if (sel>0){
									//verificar si tiene coordinadores
									var coordinadores = row.coordinadores;
									if(coordinadores ==null){
										return '<ul class="icons-list">'+
										'<li class="dropdown">'+
										'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
										'<ul class="dropdown-menu dropdown-menu-right">'+
										'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=3 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
										'</ul>'+
					                   '</li>'+
					                   '</ul>';
									}else{
										
										var html = '';
										var totCordinadores = 0;
										for(var coordinador in coordinadores ){
											if (coordinadores[coordinador].id_niv==3){
											html+='<ul class="icons-list">'+
											'<li class="dropdown">'+
												'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
													coordinadores[coordinador].trabajador.ape_pat  + ' ' + coordinadores[coordinador].trabajador.ape_mat + ', ' + coordinadores[coordinador].trabajador.nom +
													'</a>'+
												'<ul class="dropdown-menu dropdown-menu-right">'+
												'<li><a href="#" data-id="' + coordinadores[coordinador].id + '"  data-id_niv=3 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
												'<li><a href="#" data-id="' + coordinadores[coordinador].id + '" onclick="curso_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
												'</ul>'+
							                   '</li>'+
							                   '</ul>';
												totCordinadores++;
											}
										}
										
										if(totCordinadores==0)
											return '<ul class="icons-list">'+
											'<li class="dropdown">'+
											'<a href="#" class="dropdown-toggle" data-toggle="dropdown">Seleccionar</a>'+
											'<ul class="dropdown-menu dropdown-menu-right">'+
											'<li><a href="#" data-id="' + row.inicial + '"  data-id_niv=3 data-id_area="' + row.id_area + '" data-id_cur="' + row.id_cur + '" data-curso="' + row.curso + '" onclick="curso_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Agregar</a></li>'+
											'</ul>'+
						                   '</li>'+
						                   '</ul>';
										else
											return html;										

									}
								}else{
									return "";
								}
							}}							
							
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

