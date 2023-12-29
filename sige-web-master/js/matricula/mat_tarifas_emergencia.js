//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/matricula/mat_tarifas_emergencia_modal.html" id="mat_tarifas_emergencia-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Tarifas de Emergencia</a></li>');

	$('#mat_tarifas_emergencia-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		tarifas_emergencia_modal($(this));
	});

	//lista tabla
	tarifas_emergencia_listar_tabla();
});

function tarifas_emergencia_eliminar(link){
	_DELETE({url:'api/tarifasEmergencia/' + $(link).data("id"),
			success:function(){
					tarifas_emergencia_listar_tabla();
				}
			});
}

function tarifas_emergencia_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_tarifas_emergencia_modal.html');
	tarifas_emergencia_modal(link);
	
}

function tarifas_emergencia_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_tarifas_emergencia-frm');
		$("#btn-procesar").css('display', 'none');
		$('#mat_tarifas_emergencia-frm #btn-grabar').on('click',function(event){
			//$('#mat_tarifas_emergencia-frm #id').val('');
			_POST({form:$('#mat_tarifas_emergencia-frm'),
				  success:function(data){
					console.log(data);
					onSuccessSave(data) ;
					$('#id').val(data.result);
					$("#btn-procesar").css('display', 'block');
				}
			});
		});
		
		$('#mat_tarifas_emergencia-frm #btn-agregar').on('click',function(event){
			$('#mat_tarifas_emergencia-frm #id').val('');
			_POST({form:$('#mat_tarifas_emergencia-frm'),
				  success:function(data){
					console.log(data);
					onSuccessSave(data) ;
					$('#id').val(data.result);
					$("#btn-procesar").css('display', 'block');
				}
			});
		});
		
		$('#mat_tarifas_emergencia-frm #btn-procesar').on('click',function(event){
			var param_emer={id_per:$("#id_per").val(), mes:$("#mes").val(),id_tar:$("#id").val()};
			/*_get('api/tarifasEmergencia/procesarTarifa', function(data) {
				console.log(data);

			}, param_emer);*/
			
			_POST({
				context:_URL,
				url:'api/tarifasEmergencia/procesarTarifa',
				params:param_emer,
				//contentType:"application/json",
				success: function(data){
					console.log(data);
					
				}
			});

		});
		
		if (link.data('id')){
			_GET({url:'api/tarifasEmergencia/' + link.data('id'),
				  success:	function(data){
				_fillForm(data,$('#modal').find('form') );
				$("#fec_ven").val(_parseDate(data.fec_ven));
				_llenarCombo('per_periodo',$('#id_per'), data.id_per);
				if(data.procesado!=null)
					$("#btn-procesar").css('display', 'none');
				else
					$("#btn-procesar").css('display', 'block');
				
				$('#exonerado').on('change',function(event){
					if($('#exonerado').val()==1)
						$("#div_conf_montos").css('display','none');
					else if($('#exonerado').val()==0)
						$("#div_conf_montos").css('display','block');
				});
				
				$('#exonerado').change();
				  }
				}
			);
		}else{
			_llenarCombo('per_periodo',$('#id_per'));
			
			$('#exonerado').on('change',function(event){
				if($('#exonerado').val()==1)
					$("#div_conf_montos").css('display','none');
				else if($('#exonerado').val()==0)
					$("#div_conf_montos").css('display','block');
			});
			
			$('#exonerado').change();
			//$('#mat_tarifas_emergencia-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		tarifas_emergencia_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Tarifas de emergencia';
	else
		titulo = 'Nuevo  Tarifas de emergencia';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function tarifas_emergencia_listar_tabla(){
	_GET({ url: 'api/tarifasEmergencia/listar/'+$('#_id_anio').text(),
		   //params:,
		   success:
			function(data){
			console.log(data);
				$('#mat_tarifas_emergencia-tabla').dataTable({
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
							/*{"title":"Monto", "data" : "monto"}, 
							{"title":"% Descuento", "data" : "descuento"}, 
							{"title":"% Descuento Hermano", "data" : "des_hermano"}, 
							{"title":"% Descuento Banco", "data" : "des_banco"}, */
			        	 	{"title":"Servicio", "data": "periodo.servicio.nom"},
			        	 	{"title":"Local", "data": "periodo.servicio.sucursal.nom"},
			        	 	{"title":"Mes", "data" : "monto","render":function( data, type, row,meta){
			        	 		if(row.mes==3)
			        	 			return 'Marzo';
			        	 		else if(row.mes==4)
			        	 			return 'Abril';
			        	 		else if(row.mes==5)
			        	 			return 'Mayo';
			        	 		else if(row.mes==6)
			        	 			return 'Junio';
			        	 		else if(row.mes==7)
			        	 			return 'Julio';
			        	 		else if(row.mes==8)
			        	 			return 'Agosto';
			        	 		else if(row.mes==9)
			        	 			return 'Setiembre';
			        	 		else if(row.mes==10)
			        	 			return 'Octubre';
			        	 		else if(row.mes==11)
			        	 			return 'Noviembre';
			        	 		else if(row.mes==12)
			        	 			return 'Diciembre';
			        	 		else
			        	 			return '';
			        	 	}},  
			        	 	{"title":"Estado", "data" : "monto","render":function( data, type, row,meta){
			        	 		if(row.procesado==1)
			        	 			return 'PROCESADO';
			        	 		else 
			        	 			return 'NO PROCESADO';
			        	 	}}, 
							{"title":"Monto de Mensualidad", "data" : "monto","render":function( data, type, row,meta){
			        	 		return 'S./' + _formatMonto(data);
			        	 	}}, 
			        	 	{"title":"Dcto. Secretaria", "data" : "descuento","render":function( data, type, row,meta){
			        	 		return 'S./' +  _formatMonto(data);
			        	 	}},  
			        	 	{"title":"Dcto. Banco", "render":function( data, type, row,meta){
			        	 		return 'S./' + _formatMonto(row.desc_banco);
			        	 	}},  
							{"title":"Acciones", "render": function ( data, type, row ) {
							   return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="tarifas_emergencia_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="tarifas_emergencia_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
			                }
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});

}

