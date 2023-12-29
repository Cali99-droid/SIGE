//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/configuracion/col_conf_semanas_modal.html" id="col_conf_semanas-btn-nuevo" ><i class="icon-file-plus"></i> Nueva Configuraci&oacute;n</a></li>');

	$('#col_conf_semanas-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		conf_semanas_modal($(this));
	});

	//lista tabla
	conf_semanas_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	conf_semanas_listar_tabla();	
}

function conf_semanas_eliminar(link){
	_delete('api/confSemanas/' + $(link).data("id"),
			function(){
					conf_semanas_listar_tabla();
				}
			);
}

function conf_semanas_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/configuracion/col_conf_semanas_modal.html');
	conf_semanas_modal(link);
	
}

function conf_semanas_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('col_conf_semanas-frm');	
		
		
		$('#col_conf_semanas-frm #btn-agregar').on('click',function(event){
			$('#col_conf_semanas-frm #id').val('');
			var fec_ini=$('#col_conf_semanas-frm #fec_ini').val();
			var fec_fin=$('#col_conf_semanas-frm #fec_fin').val();
			var fec1 = fec_ini.split('/');
			var fec2 = fec_fin.split('/');
			var fecha_inicio=fec1[2]+fec1[1]+fec1[0];
			var fecha_fin=fec2[2]+fec2[1]+fec2[0];
			if(fecha_inicio>fecha_fin){
				_alert_error('La fecha de inicio no puede ser mayor que la fecha fin!!');
			} else{
			_post($('#col_conf_semanas-frm').attr('action') , $('#col_conf_semanas-frm').serialize(),
					function(data){
							onSuccessSave(data) ;
					}
			);	
			}
		});
		
		if (link.data('id')){
			_get('api/confSemanas/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				var fec_ini=_parseDate(data.fec_ini);
				var fec_fin=_parseDate(data.fec_fin);
				$("#fec_ini").val(fec_ini);
				$("#fec_fin").val(fec_fin);
				_get('api/confAnioEscolar/obtenerConfSemanas/' +  $('#_id_anio').text(),
						function(data1){
							$('#id_cnf_anio').val(data1.id); 
							var nro_sem=data1.nro_sem;
							var options = "";
							for (var i = 1; i <=nro_sem;i++) {
								if(data.nro_sem==i)
							    options += '<option value="' +i+ '" selected>SEMANA ' + i+ '</option>';
								else
								options += '<option value="' +i+ '" >SEMANA ' + i+ '</option>';
							}
							$("#nro_sem").html(options);
						});
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        autoUpdateInput: false,
			        locale: { format: 'DD/MM/YYYY'}
			    });
			});
			$('#col_conf_semanas-frm #btn-agregar').hide();
		}else{
			//_llenarCombo('col_conf_anio_escolar',$('#id_cnf_anio'));
			$('#col_conf_semanas-frm #btn-grabar').hide();
			
			_get('api/confAnioEscolar/obtenerConfSemanas/' +  $('#_id_anio').text(),
					function(data){
				console.log(data);
						$('#id_cnf_anio').val(data.id); 
						var nro_sem=data.nro_sem;
						var options = "";
						for (var i = 1; i <=nro_sem;i++) {
							options += '<option value="' +i+ '" >SEMANA ' + i+ '</option>';
						}
						$("#nro_sem").html(options);
					});
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		conf_semanas_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n de Semanas';
	else
		titulo = 'Nuevo  Configuraci&oacute;n de Semanas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function conf_semanas_listar_tabla(){
	var param={id_anio:$('#_id_anio').text()}
	_get('api/confSemanas/listar/',
			function(data){
			console.log(data);
				$('#col_conf_semanas-tabla').dataTable({
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
							{"title":"N&uacute;mero de Semana", "data" : "nro_sem"}, 
							{"title":"Fecha Inicio", "data" : "fec_ini"}, 
							{"title":"Fecha Fin", "data" : "fec_fin"}, 
							//{"title":"Configuraci&oacute;n a&ntilde;o escolar", "data": "confAnioEscolar.nro_sem"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_semanas_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="conf_semanas_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}, param
	);

}

