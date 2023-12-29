//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}

//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio) {
	cronograma_listar_tabla('mat_cronograma_normal-tabla','N');
	cronograma_listar_tabla('matricula_extemporaneo-tabla','E');
}

//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_cronograma_modal.html" id="mat_cronograma-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Cronograma de matr&iacute;cula</a></li>');

	$('#mat_cronograma-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cronograma_modal($(this));
	});

	//lista tabla
	cronograma_listar_tabla('mat_cronograma_normal-tabla','N');
	cronograma_listar_tabla('matricula_extemporaneo-tabla','E');
});

function cronograma_eliminar(link){
	_delete('api/cronograma/' + $(link).data("id"),
			function(){
				cronograma_listar_tabla('mat_cronograma_normal-tabla','N');
				cronograma_listar_tabla('matricula_extemporaneo-tabla','E');
				}
			);
}

function cronograma_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_cronograma_modal.html');
	cronograma_modal(link);
	
}

function cronograma_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		
		
		
		
		$('#mat_cronograma-frm #btn-agregar').on('click',function(event){
			$('#mat_cronograma-frm #id').val('');
			_post($('#mat_cronograma-frm').attr('action') , $('#mat_cronograma-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/cronograma/' + link.data('id'),
			function(data){
				
				_fillForm(data,$('#modal').find('form') );
				$('#mat_cronograma-frm #fec_mat').val(_parseDate(data.fec_mat));
				//_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				
				_inputs('mat_cronograma-frm');
			});
		}else{
			_inputs('mat_cronograma-frm');
			var tipo = $('#normal-tab').hasClass('active')?'N':'E';

			$('#mat_cronograma-frm #tipo').val(tipo);
			$('#mat_cronograma-frm #id_anio').val($('#_id_anio').text());
			
			//_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('cat_nivel',$('#id_niv'));
			$('#mat_cronograma-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		var tipo = $('#normal-tab').hasClass('active')?'N':'E';
		if(tipo=='N')
			cronograma_listar_tabla('mat_cronograma_normal-tabla','N');
		else
			cronograma_listar_tabla('matricula_extemporaneo-tabla','E');
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma de matr&iacute;cula ' + $('#_ID_ANIO').text();
	else
		titulo = 'Nuevo  Cronograma de matr&iacute;cula ' + $('#_ID_ANIO').text();
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cronograma_listar_tabla(id_tabla, tipo){
	_get('api/cronograma/listar',
			function(data){
			console.log(data);
				$('#' + id_tabla).dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Fecha para matricula", "data" : "fec_mat"}, 
							{"title":"Del", "data" : "del"}, 
							{"title":"Al", "data" : "al"}, 
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Nivel", "render":function ( data, type, row,meta ) { 
								if (row.nivel!=null)
									return row.nivel.nom;
								else
									return 'TODOS';
							}},
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="cronograma_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},{id_anio:$('#_id_anio').text(), tipo:tipo}
	);

}

