//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_aula_especial_modal.html" id="col_aula_especial-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Alumno por sal&oacute;n especial</a></li>');

	$('#col_aula_especial-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		aula_especial_modal($(this));
	});

	//lista tabla
	aula_especial_listar_tabla();
});

function aula_especial_eliminar(link){
	_delete('api/aulaEspecial/' + $(link).data("id"),
			function(){
					aula_especial_listar_tabla();
				}
			);
}

function _onchangeAnio(id_anio, anio){

	aula_especial_listar_tabla();

}


function aula_especial_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_aula_especial_modal.html');
	aula_especial_modal(link);
	
}

function aula_especial_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_aula_especial-frm');

		$('#col_aula_especial-frm #btn-agregar').on('click',function(event){
			$('#col_aula_especial-frm #id').val('');
			_post($('#col_aula_especial-frm').attr('action') , $('#col_aula_especial-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/aulaEspecial/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			
			var id_suc=$("#_id_suc").text();
			if (id_suc!='0' ){
				_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc);
			} else{
					_llenarCombo('ges_sucursal',$('#id_suc'));		
			}
				
			$('#id_suc').on('change',function(event){
				_llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val());
			});

			$('#id_niv').on('change',function(event){
				_llenarCombo('cat_grad',$('#id_gra_ori'),null,$(this).val());
			});

			$('#id_gra_ori').on('change',function(event){
				var id_suc=$("#id_suc").val();
				_llenarCombo('col_aula_local',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val()+','+id_suc, function(){
					$('#id_au').change();
				});
			});
			
			
			$('#id_au').on('change',function(event){
				_llenarComboURL('api/alumno/matriculadosCombo?id_au=' + $('#id_au').val(),$('#id_mat'));
			});

			_llenarCombo('col_aula_especial',$('#id_gra'));

			$('#col_aula_especial-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		aula_especial_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno por sal&oacute;n especial';
	else
		titulo = 'Nuevo  Alumno por sal&oacute;n especial';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function aula_especial_listar_tabla(){
	_get('api/aulaEspecial/listar/',
			function(data){
			console.log(data);
				$('#col_aula_especial-tabla').dataTable({
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
			        	 	{"title":"Aula Especial", "data": "grad.nom"}, 
			        	 	{"title":"Alumno", "render": function ( data, type, row,meta ) { return row.matricula.alumno.ape_pat + " " + row.matricula.alumno.ape_mat + ", " + row.matricula.alumno.nom;}}, 
							{"title":"Grado origen", "data": "matricula.grad.nom"}, 
							{"title":"Sección origen", "data": "matricula.aula.secc"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="aula_especial_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.id + '" onclick="aula_especial_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

