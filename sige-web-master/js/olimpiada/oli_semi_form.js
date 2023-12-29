//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	console.log(params);
	$("#oli_config-frm #id_anio").val($("#_id_anio").text());
	if(params!=null){
		var id=params.id;
		_GET({
			url:'api/seminario/' + id,
			success:function(data){
				$("#oli_config-frm #id").val(data.id);
				$("#oli_config-frm #nom").val(data.nom);
				$("#oli_config-frm #corr_envio").val(data.corr_envio);
				$("#oli_config-frm #fec").val(_parseDate(data.fec));
				$("#oli_config-frm #fec_ini_ins").val(_parseDate(data.fec_ini_ins));
				$("#oli_config-frm #fec_fin_ins").val(_parseDate(data.fec_fin_ins));
				$("#oli_config-frm #costo").val(data.costo);
				$("#oli_config-frm #costo").trigger('change.select2');
				$("#div-oli_config-grupo").css('display','block');
				$('#oli_config-grupo #id_sem').val(data.id);
				config_listar_grupos();
			}
		});
		

		
	} else{
		
		$("#div-oli_config-grupo").css('display','none');
		
	}
	
	 
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

});


$('#oli_config-frm #btn-grabar').on('click',function(event){
	//$('#oli_config-frm #id').val('');
	
	var validator = $("#oli_config-frm").validate();
	
	if (validator.valid()){

		_POST({form:$('#oli_config-frm'),
			  success:function(data){
				  console.log(data.result);
				  $('#oli_config-frm #id').val(data.result);
				  $("#div-oli_config-grupo").css('display','block');
				  //_llenarCombo('cat_modalidad_costo',$('#id_cmc'),null,null,null,_URL_OLI);
				config_listar_grupos();

			}
		});
		
	}
	

});


/**
 * AGREGAR GRUPO NUEVO
 */
$('#oli_config-grupo #btn-agregar-grupo').on('click',function(event){
	$('#oli_config-grupo #id').val('');
	$('#oli_config-grupo #id_sem').val($("#oli_config-frm #id").val());
	_POST({form:$('#oli_config-grupo'),
		  success:function(data){
			  console.log(data.result);
			  config_listar_grupos();
			  limpiar_grupo();

		}
	});
});

$('#oli_config-grupo #btn-editar-grupo').on('click',function(event){	
	$('#oli_config-grupo #id_sem').val($("#oli_config-frm #id").val());
	_POST({form:$('#oli_config-grupo'),
		  success:function(data){
			  console.log(data.result);
			  config_listar_grupos();
			 limpiar_grupo();
		}
	});
});

function config_eliminar(link){
	_DELETE({url:'api/config/' + $(link).data("id"),
			success:function(){
					config_listar_tabla();
				}
			});
}

function config_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/olimpiada/oli_config_modal.html');
	config_modal(link);
	
}

function limpiar_grupo(){
$('#oli_config-grupo #id_sem').val('');
			  $('#oli_config-grupo #id').val('');
			  $('#oli_config-grupo #nom').val('');
			  $('#oli_config-grupo #nro').val('');
			  $('#oli_config-grupo #cap').val('');
			   $('#oli_config-grupo #hor_ing').val('');
}	

function config_listar_grupos(){
	$('#oli_config-grupo #btn-editar-grupo').hide();
	$('#oli_config-grupo #btn-agregar-grupo').show();
	var param={id_oli:$('#oli_config-frm #id_oli').val()};
	
	_GET({ url: 'api/seminario/listarGrupos',
		   params: param,
		   success:
			function(data){
			console.log(data);
				$('#oli_config-grupos').dataTable({
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
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Nro Grupo", "data" : "nro"}, 
							{"title":"Capacidad", "data" : "cap"}, 
							{"title":"Hora Ingreso", "data" : "hor_ing"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
							  return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="grupo_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
									 '<a href="#"  data-id="' + row.id + '" onclick="grupo_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>';
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

function grupo_editar(row,e){
	e.preventDefault();
	 $('#oli_config-grupo #btn-editar-grupo').show();
	  $('#oli_config-grupo #btn-agregar-grupo').hide();
	var link = $(row);
	_GET({url:'api/seminario/editarGrupo/' + link.data('id'),
		  success:	function(data){
			  $("#div-oli_config-grupo #id").val(data.id);
			  $("#div-oli_config-grupo #id_sem").val(data.id_sem);
			  $("#div-oli_config-grupo #nom").val(data.nom);
			  $("#div-oli_config-grupo #nro").val(data.nro);
			  $("#div-oli_config-grupo #cap").val(data.cap);
			  $("#div-oli_config-grupo #hor_ing").val(data.hor_ing);
			   //$('#id_ti').val(data.id_cg).change();
			 // $('#id_cg').val(data.id_ti).change();  
		  }
		}
	);
}

function grupo_eliminar(row,e){
	var link = $(row);
	_DELETE({url:'api/seminario/eliminarGrupo/' + $(link).data("id"),
		success:function(){
			config_listar_grupos();
			}
		});
}


