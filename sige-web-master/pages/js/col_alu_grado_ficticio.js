//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_alu_grado_ficticio_modal.html" id="col_alu_grado_ficticio-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Alumno por sal&oacute;n especial</a></li>');

	$('#col_alu_grado_ficticio-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		alu_grado_ficticio_modal($(this));
	});

	//lista tabla
	alu_grado_ficticio_listar_tabla();
});

function alu_grado_ficticio_eliminar(link){
	_delete('api/aluGradoFicticio/' + $(link).data("id"),
			function(){
					alu_grado_ficticio_listar_tabla();
				}
			);
}

function alu_grado_ficticio_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_alu_grado_ficticio_modal.html');
	alu_grado_ficticio_modal(link);
	
}

function alu_grado_ficticio_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_alu_grado_ficticio-frm');

		var id_suc=$("#_id_suc").text();
		if (id_suc!='0' ){
			_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc);
		} else{
				_llenarCombo('ges_sucursal',$('#id_suc'));		
		}
			
		$('#id_suc').on('change',function(event){
			_llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val());
		});
		//_llenarCombo('cat_nivel',$('#id_niv'));
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

		
		_llenarCombo('cat_grad',$('#id_gra_ficticio'));

		
		/*
	    $("#alumno").autocomplete({
	        minLength: 2,
	        source: _URL + 'api/alumno/autocomplete?id_anio=' + $('#_id_anio').text() + '&id_au=' + $('#id_au').val(),
	        search: function() {
	            $(this).parent().addClass('ui-autocomplete-processing');
	        },
	        open: function() {
	            $(this).parent().removeClass('ui-autocomplete-processing');
	        },
	        select: function( event, ui ) {
	        	$('.detalle_alumno').show();
	        	$('#id_mat').val(ui.item.id);
	        },
	       
	    });
	    */
		
		$('#col_alu_grado_ficticio-frm #btn-agregar').on('click',function(event){
			$('#col_alu_grado_ficticio-frm #id').val('');
			_post($('#col_alu_grado_ficticio-frm').attr('action') , $('#col_alu_grado_ficticio-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/aluGradoFicticio/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				//_llenarCombo('mat_matricula',$('#id_mat'), data.id_mat);
			});
		}else{
			//_llenarCombo('cat_grad',$('#id_gra'));
			//_llenarCombo('mat_matricula',$('#id_mat'));
			$('#col_alu_grado_ficticio-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alu_grado_ficticio_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno por sal&oacute;n especial';
	else
		titulo = 'Nuevo  Alumno por sal&oacute;n especial';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function alu_grado_ficticio_listar_tabla(){
	_get('api/aluGradoFicticio/listar/',
			function(data){
			console.log(data);
				$('#col_alu_grado_ficticio-tabla').dataTable({
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
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Matr&iacute;cula", "data": "matricula.fecha"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alu_grado_ficticio_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="alu_grado_ficticio_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

