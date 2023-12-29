//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a id="oli_config-btn-nuevo"  href="#" onclick="_send(\'pages/olimpiada/oli_semi_form.html\',\'Configuración Seminario\',\'Configuración Seminario\');" ><i class="icon-file-plus"></i> Nuevo Configuraci&oacute;n Seminario</a></li>');

	/*$('#oli_config-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		config_modal($(this));
	});*/

	//lista tabla
	config_listar_tabla();
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
	link.attr('href','pages/olimpiada/oli_semi_form.html');
	config_modal(link);
	
}

function config_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
				
		$('#oli_config-frm #btn-agregar').on('click',function(event){
			$('#oli_config-frm #id').val('');
			_POST({form:$('#oli_config-frm'),
				  success:function(data){
					onSuccessSave(data) ;
				}
			});
		});
		
		if (link.data('id')){
			_GET({url:'api/config/' + link.data('id'),
				  success:	function(data){
				_fillForm(data,$('#modal').find('form') );
				_inputs('oli_config-frm');
				_llenarCombo('col_colegio',$('#id_col'));
				_llenarCombo('cat_gestion',$('#id_cg'),null,null,null,_URL_OLI);
				_llenarCombo('cat_modalidad_costo',$('#id_cmc'),null,null,null,_URL_OLI);
				  }
				}
			);
		}else{
			_inputs('oli_config-frm');
			_llenarCombo('col_colegio',$('#id_col'));
			_llenarCombo('cat_gestion',$('#id_cg'),null,null,null,_URL_OLI);
			_llenarCombo('cat_modalidad_costo',$('#id_cmc'),null,null,null,_URL_OLI);
			$('#oli_config-frm #btn-grabar').hide();
			var param={id_oli:null};
			_GET({
				context:_URL,
				url:'api/grad/listarGradosConcurso/',
				params:param,
				success:function(data){
					console.log(data);
					$('#oli_config-grados').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
						 searching: false, 
						 paging: false, 
						 info: false,
				         columns : [
				                    
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				        	 	{"title":"Seleccionar", "render":function ( data, type, row,meta ) {
				        	 		//creamos un hidden x cada checkbox
				        	 		var checked = (row.oli_grado!=null) ? ' checked ': ' ';
				        	 		var disabled = (checked) ? 'disabled':'';
				        	 		//var ideva = (row.eva_id!=null) ? row.eva_id: '';
				        	 		return "<input type='checkbox' " + checked + "id='id_gra"+row.id+"'  name ='id_gra' value='" + row.id + "' class='id_gra' /><font align='center'>"+row.grado+"</font>";
				        	 	} 
				        	 	}
					    ]
				    });
				}
			});
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		config_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Configuraci&oacute;n Olimpiada';
	else
		titulo = 'Nuevo  Configuraci&oacute;n Olimpiada';
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave,800);

}

function config_listar_tabla(){
	var param={id_anio:$("#_id_anio").text(), id_usr:_usuario.id};
	_GET({ url: 'api/seminario/listar/',
		   params:param,
		   success:
			function(data){
			console.log(data);
				$('#oli_config-tabla').dataTable({
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
			        	 	{"title":"Fecha", "render": function ( data, type, row ) {
								return _parseDate(row.fec); }
							},
							//{"title":"Fecha", "data" : "fec"}, 
							{"title":"Nombre", "data" : "nom"}, 
							//{"title":"Nivel", "data" : "nivel.nom"}, 
							//{"title":"Colegio", "data" : "colegio.nom"}, 
							/*{"title":"Fecha Inicio Inscripci&oacute;n", "data" : "fec_ini_ins"}, 
							{"title":"Fecha Fin Inscripci&oacute;n", "data" : "fec_fin_ins"}, 
							{"title":"Nro. alumnos por delegaci&oacute;n", "data" : "nro_alu_del"}, 
							{"title":"Puntaje Correcto", "data" : "ptje_corr"}, 
							{"title":"Puntaje Incorrecto", "data" : "ptje_inc"}, 
							{"title":"Puntaje Blanco", "data" : "ptje_bla"}, 
							{"title":"Costo", "data" : "costo"}, 
							{"title":"Impresi&oacute;n Boletas", "data" : "imp_bo"}, */
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="_send(\'pages/olimpiada/oli_semi_form.html\',\'Configuración Seminario\',\'Configuración Seminario\',{id:'+row.id+'});" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#"  data-id="' + row.id + '" onclick="config_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>';
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});

}

	

		
