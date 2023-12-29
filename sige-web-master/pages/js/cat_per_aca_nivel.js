//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/catalogos/cat_per_aca_nivel_modal.html" id="cat_per_aca_nivel-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Periodo Academico-Nivel</a></li>');

	$('#cat_per_aca_nivel-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		per_aca_nivel_modal($(this));
	});
	
	var id_anio=$('#_id_anio').text();

	//lista tabla
	per_aca_nivel_listar_tabla(id_anio);
});

function _onchangeAnio(id_anio, anio){
	per_aca_nivel_listar_tabla(id_anio);
}

function per_aca_nivel_eliminar(link){
	_delete('api/perAcaNivel/' + $(link).data("id"),
			function(){
					per_aca_nivel_listar_tabla(id_anio);
				},'No se puede eliminar este periodo por nivel, porque esta siendo usado en las configuraciones académicas!!'
			);
}

function per_aca_nivel_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/cat_per_aca_nivel_modal.html');
	per_aca_nivel_modal(link);
	
}

function per_aca_nivel_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_per_aca_nivel-frm');
		$('#cat_per_aca_nivel-frm #id_anio').val($('#_id_anio').text());
		
		$('#cat_per_aca_nivel-frm #btn-agregar').on('click',function(event){
			$('#cat_per_aca_nivel-frm #id').val('');
			_post($('#cat_per_aca_nivel-frm').attr('action') , $('#cat_per_aca_nivel-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perAcaNivel/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_periodo_aca',$('#id_cpa'), data.id_cpa);
				_llenarCombo('ges_giro_negocio',$('#id_gir'), data.id_gir);
			});
		}else{
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_periodo_aca',$('#id_cpa'));
			_llenarCombo('ges_giro_negocio',$('#id_gir'));
			$('#cat_per_aca_nivel-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_aca_nivel_listar_tabla($('#_id_anio').text());
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo  Acad&eacute;mico por Nivel';
	else
		titulo = 'Nuevo  Periodo  Acad&eacute;mico por Nivel';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function per_aca_nivel_listar_tabla(id_anio){
	var param={id_anio:id_anio};
	_get('api/perAcaNivel/listar/',
			function(data){
			console.log(data);
				$('#cat_per_aca_nivel-tabla').dataTable({
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
							{"title":"Giro Negocio", "data": "giroNegocio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Tipo de Periodo Academico", "data": "periodoAca.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="per_aca_nivel_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="per_aca_nivel_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

