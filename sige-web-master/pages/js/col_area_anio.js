//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_area_anio_modal.html" id="col_area_anio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo &Aacute;reas</a></li>');

	$('#col_area_anio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		area_anio_modal($(this));
	});

	$("#btn-clonar").on('click',function(){
		_post('api/areaAnio/clonarAnio' , {id_anio:$('#_id_anio').text()},
			function(data){
				curso_anio_listar_tabla();
			}
		);
	});

	
	//lista tabla
	area_anio_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	area_anio_listar_tabla();

}


function area_anio_eliminar(link){
	_delete('api/areaAnio/' + $(link).data("id"),
			function(){
					area_anio_listar_tabla();
				}
			);
}

function area_anio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_area_anio_modal.html');
	area_anio_modal(link);
	
}

function area_anio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_area_anio-frm');
		$('#id_anio').val($("#_id_anio").text());
		$('#col_area_anio-frm #btn-agregar').on('click',function(event){
			$('#col_area_anio-frm #id').val('');
			_post($('#col_area_anio-frm').attr('action') , $('#col_area_anio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/areaAnio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//_llenarCombo('col_anio',$('#id_anio'), data.id_anio);
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_area',$('#id_area'), data.id_area);
			});
		}else{
			//_llenarCombo('col_anio',$('#id_anio'));
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_area',$('#id_area'));
			$('#col_area_anio-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		area_anio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar &Aacute;reas Educativas';
	else
		titulo = 'Nuevo  &Aacute;reas Educativas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function area_anio_listar_tabla(){
	_get('api/areaAnio/listar/',
			function(data){
			
			if(data.length==0){
				$("#cat_area_btn_clonar-panel").css('display','block');
				new PNotify({
				        title: 'Ayuda',
				        text: 'Se puede clonar la configuración del año anterior, en el botón "Clonar desde Año Anterior"',
				        addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
				        delay:10000
				    });
			} else{
					$("#cat_area_btn_clonar-panel").css('display','none');
			}
	
			
			console.log(data);
				$('#col_area_anio-tabla').dataTable({
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
							{"title":"Area", "data": "area.nom"}, 
							{"title":"Orden", "data" : "ord"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="area_anio_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.id + '" onclick="area_anio_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},{id_anio:$('#_id_anio').text()}
	);

}

