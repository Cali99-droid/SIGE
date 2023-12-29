//Se ejecuta cuando la pagina q lo llama le envia parametros

function onloadPage(params){
	_onloadPage();
	listar_niveles();
	$("#div_nivel").show();
	$("#div_grado").hide();
}

function listar_niveles(){
	_get('api/grad/listarNiveles',function(data){
	var id_niv_pri=data[0].id;
	$('#tabla_nivel').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			{"title":"Nivel", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				listar_grados(id_niv_pri);
				$("#tabla_nivel tbody tr").click(function(){  
					$("#div_nivel").show();
					$("#div_grado").hide();
					var id_niv=($(this).find('td:nth-child(1)').find('#id').val());
					listar_grados(id_niv);
					_get('api/grad/obtenerDatosNivel/'+id_niv,
					function(data){
						$("#div_nivel #nom_nivel").text("NIVEL "+data.nom);
						console.log(data);
						_fillForm(data,$('#frm-mant_nivel'));	
					});
				});	   
				}
			});
	});
}

function listar_grados(id_niv){
	var param={id_niv:id_niv};
	_get('api/grad/listar',function(data){
	$('#tabla_grado').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom 
			}}
			],
				"initComplete": function( settings ) {
				_initCompleteDTSB(settings);
				//_initCompleteDT(settings);
				$('#frm-mant_grado #id_nvl').val(id_niv);
				$("#tabla_grado tbody tr").click(function(){  
					var id_gra=($(this).find('td:nth-child(1)').find('#id').val());
					$("#div_grado").show();
					$("#div_nivel").hide();
					if(id_gra){
						_get('api/grad/' + id_gra,
						function(data){
						console.log(data);
						_fillForm(data,$('#div_grado'));
						$("#div_grado #nom_grado").text("GRADO "+data.nom);
						_llenarCombo('cat_nivel',$('#frm-mant_grado #id_nvl'),data.id_nvl,null, function(){
							$('#frm-mant_grado #id_nvl').change();
						});
						_llenarCombo('cat_grad',$('#frm-mant_grado #id_gra_ant'),data.id_gra_ant,null, function(){
							$('#frm-mant_grado #id_gra_ant').change();
						});
						});	
					}	
				});	   
				}
			});
	},param);
}	


$('#frm-mant_nivel #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_nivel').validate(); 
		if ($('#frm-mant_nivel').valid()){
			_post($('#frm-mant_nivel').attr('action') , $('#frm-mant_nivel').serialize(),function(data) {
				$('#frm-mant_nivel #id').val(data.result);
				listar_niveles();
			}	
			);
		}	
		
});

$('#frm-mant_nivel #btn_nuevo').on('click',function(event){
	$('#frm-mant_nivel #id').val('');
	$('#frm-mant_nivel #nom').val('');
	$('#frm-mant_nivel #cod_mod').val('');
});	

$('#frm-mant_nivel #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/grad/eliminarNivel' + $('#frm-mant_nivel #id').val(),
		context:_URL,
		success:function(){
			listar_niveles();
			$('#frm-mant_nivel #id').val('');
			$('#frm-mant_nivel #nom').val('');
			$('#frm-mant_nivel #cod_mod').val('');
		}
		});
});

$('#frm-mant_grado #btn_grabar').on('click',function(event){
		var validation = $('#frm-mant_grado').validate(); 
		if ($('#frm-mant_grado').valid()){
			_post($('#frm-mant_grado').attr('action') , $('#frm-mant_grado').serialize(),function(data) {
				$('#frm-mant_grado #id').val(data.result);
				listar_grados($('#frm-mant_grado #id_nvl').val());
			}	
			);
		}		
});

$('#frm-mant_grado #btn_eliminar').on('click',function(event){
		_DELETE({url:'api/grad/' + $('#frm-mant_grado #id').val(),
		context:_URL,
		success:function(){
			listar_grados($('#frm-mant_grado #id_nvl').val());
			$('#frm-mant_grado #id').val('');
			$('#frm-mant_grado #nom').val('');
			$('#frm-mant_grado #des').val('');
			//$("#frm-mant_giro_negocio #id_emp").val('').trigger('change');
		}
		});
});

$('#frm-mant_grado #btn_nuevo').on('click',function(event){
	$('#frm-mant_grado #id').val('');
	$('#frm-mant_grado #nom').val('');
	$('#frm-mant_grado #abrv_classroom').val('');
	$('#frm-mant_grado #abrv').val('');
	$("#frm-mant_grado #tipo").val('').trigger('change');
	$("#frm-mant_grado #id_gra_ant").val('').trigger('change');
	_llenarCombo('cat_nivel',$('#frm-mant_grado #id_nvl'),null,null, function(){
		$('#frm-mant_grado #id_nvl').change();
	});
	_llenarCombo('cat_grad',$('#frm-mant_grado #id_gra_ant'),null,null, function(){
		$('#frm-mant_grado #id_gra_ant').change();
	});
});

