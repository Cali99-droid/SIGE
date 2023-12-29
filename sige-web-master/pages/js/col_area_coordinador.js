//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
/*
	$('#_botonera').html('<li><a href="pages/academico/col_area_coordinador_modal.html" id="col_area_coordinador-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Coordinador por &Aacute;reas</a></li>');

	$('#col_area_coordinador-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		area_coordinador_modal($(this));
	});
*/
	//lista tabla
	$('#id_nivel').on('change',function(event){
		area_coordinador_listar_tabla();
	});
	
	$('#id_giro').on('change',function(event){
			_llenarComboURL('api/disenioCurricular/listarNivelesComboxGiro/'+$("#_id_anio").text()+'/'+$('#id_giro').val(),$('#id_nivel'),null,null,function(){$('#id_nivel').change();});	
		
	});	
				
	_llenarCombo('ges_giro_negocio',$('#id_giro'), null, null,function() {
			$('#id_giro').change();
	});	
	//area_coordinador_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	area_coordinador_listar_tabla();
}


function area_coordinador_eliminar(link){
	_delete('api/areaCoordinador/' + $(link).data("id"),
			function(){
					area_coordinador_listar_tabla();
				}
			);
}

function area_coordinador_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_area_coordinador_modal.html');
	area_coordinador_modal(link);
	
}

function area_coordinador_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_area_coordinador-frm');
		$('#col_area_coordinador-frm #id_anio').val($('#_id_anio').text());
		$('#col_area_coordinador-frm #id_gir').val($('#id_giro').val());
		$('#col_area_coordinador-frm #btn-agregar').on('click',function(event){
			//$('#col_area_coordinador-frm #id').val('');
			_post($('#col_area_coordinador-frm').attr('action') , $('#col_area_coordinador-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		

		if (link.data('id')){

			_get('api/areaCoordinador/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_area',$('#id_area'), data.id_area);
				_llenarComboURL('api/areaCoordinador/listarCoordinadores',$('#id_tra'), data.id_tra);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'),link.data('id_nivel'));
			_llenarCombo('cat_area',$('#id_area'),link.data('id_area'));
			_llenarComboURL('api/areaCoordinador/listarCoordinadores',$('#id_tra'));
			$('#col_area_coordinador-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_coordinador_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Coordinador por &Aacute;reas';
	else
		titulo = 'Nuevo  Coordinador por &Aacute;reas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function area_coordinador_listar_tabla(){
	_get('api/areaCoordinador/listarAreaxNivel/' + $('#_id_anio').text()+'/'+$('#id_giro').val()+'/'+$('#id_nivel').val(),
			function(data){
			console.log(data);
				$('#col_area_coordinador-tabla').dataTable({
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
							{"title":"Area", "data": "nom"}, 
							{"title":"Coordinador", "render": function ( data, type, row ) {
								var textSeleccionar = "";
								//if(row.area_nivel1){
									if (row.coor_area)
										textSeleccionar = row.nom1;
									else 
										textSeleccionar = "Seleccionar";
								//}
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									textSeleccionar +
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
								'<li><a href="#" data-id="' + row.coor_area + '"  data-id_nivel="'+$('#id_nivel').val()+'" data-id_area="' + row.id_area + '" onclick="area_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
								'<li><a href="#" data-id="' + row.coor_area + '" onclick="area_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}/*,
							{"title":"Primaria", "render": function ( data, type, row ) {
								var textSeleccionar = "";
								if(row.area_nivel2){
									if (row.primaria)
										textSeleccionar = row.nom2;
									else 
										textSeleccionar = "Seleccionar";
								}
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									textSeleccionar +
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.primaria + '"  data-id_niv=2 data-id_area="' + row.id_area + '" onclick="area_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.primaria + '" onclick="area_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							},
							{"title":"Secundaria", "render": function ( data, type, row ) {
								var textSeleccionar = "";
								if(row.area_nivel3){
									if (row.secundaria)
										textSeleccionar = row.nom3;
									else 
										textSeleccionar = "Seleccionar";
								}
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									textSeleccionar +
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.secundaria + '"  data-id_niv=3 data-id_area="' + row.id_area + '" onclick="area_coordinador_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.secundaria + '" onclick="area_coordinador_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}*/
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

