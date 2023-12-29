//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_tra=_usuario.id_tra;
var id_anio=$('#_id_anio').text();
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/col_per_uni_modal.html" id="col_per_uni-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Periodo Unidad</a></li>');
	$("#btn-clonar").css('display','block');

	$('#col_per_uni-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		per_uni_modal($(this));
	});

	//lista tabla
	per_uni_listar_tabla();
});

function _onchangeAnio(id_anio, anio){
	per_uni_listar_tabla();	
}

function per_uni_eliminar(link){
	_delete('api/perUni/' + $(link).data("id"),
			function(){
					per_uni_listar_tabla();
				}
			);
}

function per_uni_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/col_per_uni_modal.html');
	per_uni_modal(link);
	
}

$('#btn-clonar').on('click',function(event){
	event.preventDefault();	
	_post('api/perUni/clonar/'+$('#_id_anio').text(),
			function(data){
			console.log(data);
			//per_uni_listar_tabla();	
	});
	per_uni_listar_tabla();	
});

function per_uni_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true,
	        locale: { format: 'DD/MM/YYYY'},
	    });
		
		_inputs('col_per_uni-frm');
		
		
		$('#col_per_uni-frm #btn-grabar').on('click',function(event){
			
			var id_anio=$('#_id_anio').text();
			$('#id_anio').val(id_anio);
			_post($('#col_per_uni-frm').attr('action') , $('#col_per_uni-frm').serialize(),
			function(data){
					console.log(data.result);
					$('#col_per_uni-frm #id').val(data.result);
					$('#col_per_uni-frm #numu_ini').attr("readonly","readonly");
					$('#col_per_uni-frm #numu_fin').attr("readonly","readonly");
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/perUni/' + link.data('id'),
			function(data){
				$('#col_per_uni-frm #numu_ini').attr("readonly","readonly");
				$('#col_per_uni-frm #numu_fin').attr("readonly","readonly");
				_fillForm(data,$('#modal').find('form') );
				console.log(data);
				var fec_ini= _parseDate(data.fec_ini);
				var fec_fin= _parseDate(data.fec_fin);
				var fec_ini_ing= _parseDate(data.fec_ini_ing);
				var fec_fin_ing= _parseDate(data.fec_fin_ing);
				$('#col_per_uni-frm #id').val(data.id);
				$('#fec_ini').val(fec_ini);
				$('#fec_fin').val(fec_fin);
				$('#fec_ini_ing').val(fec_ini_ing);
				$('#fec_fin_ing').val(fec_fin_ing);
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        locale: { format: 'DD/MM/YYYY'}
			    });
				/*_llenarCombo('cat_nivel',$('#id_niv'), data.nivel.id, null,function(){$('#id_niv').change();} );
				_llenarCombo('ges_giro_negocio',$('#id_gir'), data.id_gir, null,function(){$('#id_gir').change();} );
				
				$('#id_gir').on('change',function(event){
					$('#id_niv').change();
				});	*/
				
			
				$('#id_niv').on('change',function(event){
					var param= {id_niv:$('#id_niv').val(),id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val()};
					_llenarComboURL('api/perAcaNivel/listarPeriodosxNivelyAnio',$('#id_cpa'), data.perAcaNivel.id, param,function(){$('#id_cpa').change();});
					_llenarComboArreglo(_ARRAY_BIMESTRE,$('#nump'),data.nump);
				});	
				
				$('#id_gir').on('change',function(event){
					if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
						_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),data.nivel.id,null,function(){
							$('#id_niv').find('option[value=""]').remove();
							$('#id_niv').change();
							});
					} else {
						_llenarCombo('cat_nivel',$('#id_niv'), data.nivel.id, null,function(){$('#id_niv').change();} );
					}	
				});	
				
			
				if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
					
					_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),data.giroNegocio.id,null,function(){$('#id_gir').change();}); //
				} else {
					_llenarCombo('ges_giro_negocio',$('#id_gir'), data.giroNegocio.id, null,function(){$('#id_gir').change();} );
				}	
				
				/*2021 _llenarCombo('cat_per_nivel',$('#id_cpa'),data.perAcaNivel.id,data.nivel.id, function(){
					var periodo=$('#id_cpa option:selected').data('aux2');
					// comentado 2021 if(periodo==1){
						_llenarComboArreglo(_ARRAY_BIMESTRE,$('#nump'),data.nump);
					/*}
					else{
						_llenarComboArreglo(_ARRAY_TRIMESTRE,$('#nump'),data.nump);
					}*/
				/* 2021 });*/
				per_uni_det_listar_tabla();
			});
			
		}else{
			/*$('#id_gir').on('change',function(event){
				$('#id_niv').change();
			});	*/
			
			$('#id_niv').on('change',function(event){
				//alert();
				var param= {id_niv:$('#id_niv').val(),id_anio:$('#_id_anio').text(), id_gir:$('#id_gir').val()};
					_llenarComboURL('api/perAcaNivel/listarPeriodosxNivelyAnio',$('#id_cpa'), null, param,function(){$('#id_cpa').change();});
					_llenarComboArreglo(_ARRAY_BIMESTRE,$('#nump'),null);
				/*2021 _llenarCombo('cat_per_nivel',$('#id_cpa'),null,$(this).val(), function(){
					var periodo=$('#id_cpa option:selected').data('aux2');
					//comentado 2021 if(periodo==1){
						_llenarComboArreglo(_ARRAY_BIMESTRE,$('#nump'));
					/*}
					else{
						_llenarComboArreglo(_ARRAY_TRIMESTRE,$('#nump'));
					}*/
				//2021 });
				
			});
			
			$('#id_gir').on('change',function(event){
				if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
					_llenarComboURL('api/trabajador/listarGirosNivelesxCoordinador/'+id_tra+'/'+id_anio+'/'+$('#id_gir').val(),$('#id_niv'),null,null,function(){
						$('#id_niv').find('option[value=""]').remove();
						$('#id_niv').change();
						});
				} else {
					_llenarCombo('cat_nivel',$('#id_niv'), null, null,function(){$('#id_niv').change();} );
				}	
			});	
			
			
			if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
				_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+id_tra+'/'+id_anio,$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
			} else {
				_llenarCombo('ges_giro_negocio',$('#id_gir'), null, null,function(){$('#id_gir').change();} );
			}	
			
			//$('#col_per_uni-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		per_uni_listar_tabla();
		per_uni_det_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Periodo Unidad';
	else
		titulo = 'Nuevo  Periodo Unidad';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function per_uni_listar_tabla(){
	var param = null;
	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1){
		var param ={id_anio: $('#_id_anio').text(), id_tra:id_tra };
	} else {
		var param ={id_anio: $('#_id_anio').text()};
	}	
	
	_get('api/perUni/listarPeriodosxAnio',
			function(data){
			console.log(data.length);
			if(data.length==0){
				$("#btn-clonar").css('display','block');
				new PNotify({
			         title: 'Ayuda',
			         text: 'Se puede clonar la configuración del año anterior, en el botón "Clonar Año Anterior"',
			         addclass: 'alert alert-styled-left alert-styled-custom alert-arrow-left alpha-teal text-teal-800',
			         delay:10000
			     });
			} else{
				$("#btn-clonar").css('display','none');
			}
			console.log(data);
				$('#col_per_uni-tabla').dataTable({
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
							{"title":"Giro Negocio", "data" : "gir_nom"},
			        	 	{"title":"Nivel", "data" : "niv_nom"}, 
			        	 	{"title":"Periodo Académico", "render": function ( data, type, row ) {
				            	   return row.cpa_nom+" "+row.cpu_nump;
				            }} ,  
							{"title":"Unidad Inicio", "data" : "cpu_numu_ini"}, 
							{"title":"Unidad Fin", "data" : "cpu_numu_fin"}, 
							{"title":"Fecha Inicio", "data" : "cpu_fec_ini", "render":function(data, type, row){return _parseDate(data)}},
							{"title":"Fecha Fin", "data" : "cpu_fec_fin", "render":function(data, type, row){return _parseDate(data)}},
							{"title":"Acciones", "render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#"  data-id="' + row.id + '" onclick="per_uni_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								'<a href="#" data-id="' + row.id + '" onclick="per_uni_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
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

function per_uni_det_listar_tabla(){
	var param ={id_cpu: $('#col_per_uni-frm #id').val()};
	_get('api/perUniDet/listar/',
			function(data){
			console.log(data);
			
				$('#col_per_uni_det-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	//{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			        	 	{"title":"Unidad", "data" : "ord"},  
							//{"title":"Nro de Semana", "data" : "nro_sem"}, 
							{"title":"Nro de Semana", "render":function ( data, type, row,meta ) {  
									return "<input type='hidden' id='id_uni_per_det" + row.id +"' name='id_uni_per_det' value='" + row.id + "'/><input type='text' id='nro_sem" + row.id + "' name='nro_sem' value='" + row.nro_sem + "'  data-id='" + row.id + "' class='form-control input-sm daterange-single' maxlength='10' />";
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

