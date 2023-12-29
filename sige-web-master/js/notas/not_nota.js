//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
var _permiso_doc=null;
function onloadPage(params) {
	//alert(2);
	$('#btn-eliminar').hide();

	$('#id_cur').on('change',function(event) {

		var param1 = {id_niv : $('#id_niv').val() , id_anio: $('#_id_anio').text()};
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1, function() {
				$('#id_cpu').change();
			});

		});

	
	$('#id_cpu').on('change',function(event) {
				var param = {
					id_au : $('#id_au').val(),
					nump : $('#id_cpu').val(),
					id_anio : $('#_id_anio').text(),
					id_cur: $('#id_cur').val()
				};
								
				_llenarComboURL('api/nota/listarEvaluaciones', $('#id_eva'),null, param,function(){$('#id_eva').change()});
				
			});

	$('#id_au').on(
			'change',
			function(event) {
				var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text(),
					id_au : $(this).val()
				}
				_llenarComboURL('api/nota/listarCursosProfesor', $('#id_cur'),null, param, function(){$('#id_cur').change()});
			});

	$('#id_gra').on(
			'change',
			function(event) {
				var param = {
					id_tra : _usuario.id_tra,
					id_grad : $(this).val(),
					//id_suc : $('#id_suc').val(),
					id_anio : $('#_id_anio').text()
				}
				_llenarComboURL('api/nota/listarAulaProfesor', $('#id_au'),	null, param, function() {
							$('#id_au').change();
						});
			});

	$('#id_niv').on(
			'change',
			function(event) {
				// _llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$('#id_gra').change();
				var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text(),
					id_niv : $("#id_niv").val()
				}
				_llenarComboURL('api/evaluacion/listarGrados', $('#id_gra'),
						null, param, function() {
							$('#id_gra').change();
							/*var param1 = {
								id_niv : $('#id_niv').val()
							};
							_llenarComboURL('api/cursoUnidad/listarPeriodos',
									$('#id_cpu'), null, param1, function() {
										$('#id_cpu').change();
									});
							*/
						});

			});


	
	$('#id_eva').on('change',function(e){
		//var id_eva = $(this).val();
		//var table = $('#not_nota-tabla').DataTable();
		//table.clear().draw();
		$('#not_nota-div').hide();
		$('#btn-grabar').hide();
		$('#btn-actualizar').hide();
	});

	var param = {
		id_tra : _usuario.id_tra,
		id_anio : $('#_id_anio').text()
	};
		
	_llenarComboURL('api/evaluacion/listarNiveles', $('#id_niv'),
			null, param, function() {
				$('#id_niv').change();
			});
	
	/*_llenarComboURL('api/nota/listarSucursal', $('#id_suc'), null, param,
			function() {
				$('#id_suc').change();
			});*/

	$('#buscar-frm').on('submit', function(e) {
		e.preventDefault();
		_param = {};
		_param.id_au = $('#id_au').val();
		_param.nump = $('#id_cpu').val();
		_param.id_eva = $('#id_eva').val();
		_param.id_anio = $('#_id_anio').text();
		
		var fec= $('#id_eva option:selected').attr('data-aux1');
		if(fec=='' || fec==null){
			_alert_error('Debe configurar la fecha de evaluación de acuerdo al horario, para poder ingresar notas!!',
					function() {
						return false;
					});
			return false;
		}
		console.log(param);
		$('#not_nota-div').show();
		var param_per = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text(), nump: $('#id_cpu').val()};
		var flag_vig=null;
		var param_vig={id_tra:_usuario.id_tra, id_au: $('#id_au').val(), id_cpu:$('#id_cpu').val()};
		_get('api/perUni/verificarPeriodoVig', function(data1) {
			console.log(data1);
			flag_vig = data1.flag;
			
			_get('api/permisoDocente/obtenerVigencia', function(data2) {
				console.log(data2);
				if(data2!=null){
					flag_vig = data2.vig;
					_permiso_doc=1;
				} else{
					_permiso_doc=0;
				}
				


			_get('api/nota/listarAlumnoIndicadores', function(data) {
				console.log(data);
				var exonerado = data.exonerado;
				
				 _indicadores =[];
				var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
								{"title": "Alumno","render": function  ( data, type, row,meta ) { return row.ape_pat + " " +  row.ape_mat + ", " + row.nom;  }}
					];
				
				var ind_count=0;
				
				for (var key in data.indicadores) {
					var indicador = data.indicadores[key];
					//console.log(indicador);
					
					var titulo = ++ind_count;
					titulo = "I" + titulo;
					_indicadores.push( indicador.id);
					var id_ind = indicador.id;
					var nivel = $('#id_niv').val();
					
					var element = {"title": titulo,"data": "nota_" + indicador.id, 
							"render":function( data, type, row,meta ){
								var value = row['nota_' +  _indicadores[meta.col-2] ];
								var id = row['nni' +  _indicadores[meta.col-2] + '_id'] ;//nni19.id
							
								if(row.cod=='T'){
									return '<div class="token bg-primary text-white text-center"><span class="token-label" style="width: 70%;">Trasladado</span></div>';
								} else if(row.cod=='F'){
									return '<div class="token bg-warning text-white text-center"><span class="token-label" style="width: 70%;">Fallecido</span></div>';
								} else if(row.cod=='R'){
									return '<div class="token bg-warning text-white text-center"><span class="token-label" style="width: 70%;">Retirado</span></div>';
								} else if(row.exonerado=='1'){
									return '<div class="token bg-success text-white text-center"><span class="token-label" style="width: 70%;">Exonerado</span></div>';
								} else if(flag_vig=='1'){
									var value_db=value;
									if(nivel=='1'){
										if(value!=''){
											if(value=='4')
							    				  value='AD';
							    			  else if(value=='3')
							    				  value = 'A';
							    			  else if(value=='2')
							    				  value = 'B';
							    			  else if(value=='1')
							    				  value = 'C';
							    			  else
							    				  value ='';
										}
										console.log(value);
										return "<input id='n"  + row.id + "_" + _indicadores[meta.col-2] +"' min='1' max='4' class='form-control' onkeyup=onckeydownNota(this,event) data-id='" + id + "' data-bd='" + value_db + "' data-id_ne='" + indicador.id_ne + "' value='" + value + "'/>";
									}
									else
										return "<input type='number' id='n"  + row.id + "_" + _indicadores[meta.col-2] +"' min='0' max='20' class='form-control' onkeyup=onckeydownNota(this,event) data-id='" + id + "' data-bd='" + value + "' data-id_ne='" + indicador.id_ne + "' value='" + value + "'/>";
								} else {
									
									var value_db=value;
									if(nivel=='1'){
										if(value!=''){
											if(value=='4')
							    				  value='AD';
							    			  else if(value=='3')
							    				  value = 'A';
							    			  else if(value=='2')
							    				  value = 'B';
							    			  else if(value=='1')
							    				  value = 'C';
							    			  else
							    				  value ='';
										}
										return "<input readonly id='n"  + row.id + "_" + _indicadores[meta.col-2] +"' min='1' max='4' class='form-control' data-id='" + id + "' data-bd='" + value_db + "' data-id_ne='" + indicador.id_ne + "' value='" + value + "'/>";
									}else
										return "<input type='number' readonly id='n"  + row.id + "_" + _indicadores[meta.col-2] +"' min='0' max='20' class='form-control' data-id='" + id + "' data-bd='" + value_db + "'value='" + value + "'/>";
								}
									
							} };
					columns.push(element);
				}
				
				console.log(columns);
				
				if ( $.fn.DataTable.isDataTable('#not_nota-tabla') ) {
					  $('#not_nota-tabla').DataTable().destroy();
					  console.log('tabla destruida');
					  $('#not_nota-tabla').html('');
					}

				
				console.log( data.alumnosIndicadores);
				$('#not_nota-tabla').dataTable({
					    data : data.alumnosIndicadores,
					    columns:columns,
						pageLength: 50,
						aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 //bLengthChange: false,
						 //bPaginate: false,
						 //bFilter: false,
						 select: true,		 
				    "initComplete": function( settings ) {
				    	
				    	  //formatear la nota
				    	  var nota_llena = false;
				    	  $('#not_nota-tabla input').each(function(){
				    		  var val = $(this).val();
				    		  if($('#id_niv').val()!='1' && val.length==1)
				    			  $(this).val('0' + val);
				    		  if(($('#id_niv').val()!='1' && val.length>1) || ($('#id_niv').val()=='1' && val.length>0))
				    			  nota_llena = true;
				    		  
				    		  if( ($('#id_niv').val()=='1' &&  parseFloat(val)<2 ) || ($('#id_niv').val()!='1' && parseFloat(val)<11 ))
				  				$(this).css('color','red');
				  			  else
				  				$(this).css('color','black');//
				    	  });
				    	  

				    	  if(flag_vig=='1' && $('#id_niv').val()=='1'){
				    		  
				    		  $('#not_nota-tabla input').on('blur',function(e){
				    			  var valor = $(this).val();
				    			  //$(this).attr('type','text');
				    			  if(valor=='4')
				    				  $(this).val('AD');
				    			  else if(valor=='3')
				    				  $(this).val('A');
				    			  else if(valor=='2')
				    				  $(this).val('B');
				    			  else if(valor=='1')
				    				  $(this).val('C');
				    			  else
				    				  $(this).val('');
				    		  });

				    		  $('#not_nota-tabla input').on('focus',function(e){
				    			  var valor = $(this).val();
				    			  console.log('focus:' + valor);
				    			  //$(this).attr('type','number');
				    			  if(valor=='AD')
				    				  $(this).val('4');
				    			  else if(valor=='A')
				    				  $(this).val('3');
				    			  else if(valor=='B')
				    				  $(this).val('2');
				    			  else if(valor=='C')
				    				  $(this).val('1');
				    			  else
				    				  $(this).val('');
				    		  });
				    	  }
				    	  
				    	  for (var j=0; j<data.indicadores.length; j++){
				    		  var $th = $("#not_nota-tabla thead tr th").eq(j+2);
					    	  $th.attr('title',data.indicadores[j].indicador  );  
				    	  }
				    	  
				    	  //boton grabar o actualizar
				    	  //alert(nota_llena);
				    	  if(data.alumnosIndicadores.length>0){
				    		  if (nota_llena){
				    			  $('#btn-actualizar').show();
				    			  $('#btn-grabar').hide();
				    			  // ($('#id_niv').val()==3 && $('#id_cpu').val()==7) || ($('#id_niv').val()==2 && $('#id_cpu').val()==4) || ($('#id_niv').val()==3 && $('#id_cpu').val()==8) || ($('#id_niv').val()==1 && $('#id_cpu').val()==1) || ($('#id_niv').val()==2 && $('#id_cpu').val()==5) 
				    			 if(flag_vig=='1'){
				    				 $('#btn-eliminar').show(); 
				    				 $('#btn-actualizar').show(); 
				    			 }
				    			 else{
				    				 $('#btn-eliminar').hide(); 
				    				 $('#btn-actualizar').hide();
				    			 }
				    		  }else{
				    			  $('#btn-grabar').show();
				    			  $('#btn-actualizar').hide();
				    		  }
				    	  }else{
								$('#btn-grabar').hide();
								$('#btn-actualizar').hide();
				    	  }
					 }
			    });
				
				
				
				
			}, _param);
			
			}, param_vig);	
		}, param_per);
		

	});
	
	$('#btn-grabar').on('click',function(){
		var evaluacion ={};
		evaluacion.id_eva = $('#id_eva').val();
		evaluacion.id_tra = _usuario.id_tra;
		evaluacion.id_ind = _indicadores;
		console.log('iteracion de filas');
		//notas
		var table= $('#not_nota-tabla').DataTable();
		var notaAlumno_arreglo = [];
		var error= false;
		var _input_error =null;
		table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
			var data = this.data();
		    console.log(data);

		    var notaAlumno_item = {};
			
			var notaIndicadores = [];
			notaAlumno_item.id_alu = data.id;
			//console.log(id_ind):

			for (var i=0;i< _indicadores.length; i++) {
				var id_ind = _indicadores[i];
				//console.log('#n' + data.id + '_' + id_ind + ':::', '>' + $('#n' + data.id + '_' + id_ind).val() + '<')
				
				if($('#n' + data.id + '_' + id_ind).val()=='' ){
					if(($('#id_niv').val()!='1'&&  $('#n' + data.id + '_' + id_ind).val().length<2) || ($('#id_niv').val()=='1'&&  $('#n' + data.id + '_' + id_ind).val().length<1)){
						console.log('error');
						error= true;
						_input_error = $('#n' + data.id + '_' + id_ind);
					}
				}
				var nota_input = $('#n' + data.id + '_' + id_ind).val();
				if ($('#id_niv').val()=='1'){
					  if(nota_input=='AD')
						  nota_input = 4;
	    			  else if(nota_input=='A')
	    				  nota_input = 3;
	    			  else if(nota_input=='B')
	    				  nota_input = 2;
	    			  else if(nota_input=='C')
	    				  nota_input = 1;
	    			  else
	    				  nota_input = 1;	
				}
					
				notaIndicadores.push(nota_input);
			}
			
			notaAlumno_item.notas = notaIndicadores;
		    
			notaAlumno_arreglo.push(notaAlumno_item);

		} );
		
		evaluacion.notaAlumno = notaAlumno_arreglo;
		
		//objeto q se enviará al controller de lina
		console.log(evaluacion);
		if(error){
			var msg ='Completar todas las notas.';

			_alert_error(msg,
			function(){
				_input_error.focus();
			});
		}
		else
			_post_json('api/nota/grabar', evaluacion, function(data) {
				$('#btn-actualizar').show();
				$('#btn-grabar').hide();
				$('#buscar-frm').submit();
			});
		
	});
	
	$('#btn-eliminar').on('click',function(){
		
		var id_eva=$('#id_eva').val();
		_delete('api/nota/' + id_eva,
				function(){
			$('#buscar-frm').submit();
		},
		'De elimar todas las notas de esta evaluación?'
				);
	});
	
	$('#btn-actualizar').on('click',function(){
		var evaluacion ={};
		evaluacion.id_eva = $('#id_eva').val();
		evaluacion.id_tra = _usuario.id_tra;
		evaluacion.id_ind = _indicadores;
		console.log('iteracion de filas');
		//notas
		var table= $('#not_nota-tabla').DataTable();
		var notaUpdate  = {};
		var notaAlumno_arreglo = [];
		var error= false;
		table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
			var data = this.data();
			
			console.log(data);
		    
			for (var i=0;i< _indicadores.length; i++) {
				var id_ind = _indicadores[i];
				var input = $('#n' + data.id + '_' + id_ind);
				var id = input.data('id');
				var bd = input.data('bd');
				var value = input.val();
				if (value==''){
					//alert();
					error = true;// _alert_error('Es obligatorio ingresar la nota');
				}
				
				if ($('#id_niv').val()=='1'){
					  if(value=='AD')
						  value =4;
	    			  else if(value=='A')
	    				  value = 3;
	    			  else if(value=='B')
	    				  value = 2;
	    			  else if(value=='C')
	    				  value = 1;
	    			  else
	    				  value = 1;	
				}
			
			
				//if(id!=null ){
						
					if (parseInt(value)!= parseInt(bd)){
						
						var notaAlumno_item = {};
							
						notaAlumno_item.id = id;
						notaAlumno_item.nota = value;
						notaAlumno_item.id_usr = _usuario.id_tra;
						notaAlumno_item.id_ind=id_ind;
						notaAlumno_item.id_alu = data.id;
						notaAlumno_item.id_ne = input.data('id_ne');
						console.log(notaAlumno_item)
							
						notaAlumno_arreglo.push(notaAlumno_item);
					}
				//}
			}
			
			
			//notaAlumno_arreglo.push(notaAlumno_item);

		} );
		
		//evaluacion.notaAlumno = notaAlumno_arreglo;
		
		//objeto q se enviará al controller de lina
		//console.log(evaluacion);
		if(error)
			_alert_error('Completar todas las notas.');
		else
			{
			//notaUpdate.notas = notaAlumno_arreglo;
			//notaUpdate.id_tra = _usuario.id_tra;
				console.log(notaAlumno_arreglo);
				/* var param ={notaAlumno_arreglo:notaAlumno_arreglo,permiso:_permiso_doc};		
				 console.log(param);*/
				_post_json('api/nota/actualizar/'+_permiso_doc, notaAlumno_arreglo, function(data) {
					
					for (var i=0;i< notaAlumno_arreglo.length; i++) {
						var notaAlumno = notaAlumno_arreglo[i];
						var id = notaAlumno.id;
						var value =  notaAlumno.nota;
						var element = $('[data-id="' + id + '"]');
						console.log(id, element);
						element.data('bd', value);
					}
					
					$('#btn-actualizar').show();
					$('#btn-grabar').hide();
				});
				 
			}
		
		
	});
	
}
// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();

	$('#_botonera')
			.html(
					'<li><a href="pages/notas/not_nota_modal.html" id="not_nota-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Nota Evaluacion</a></li>');

	$('#not_nota-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		nota_modal($(this));
	});

	// lista tabla
	//nota_listar_tabla();
});

function nota_eliminar(link) {
	_delete('api/nota/' + $(link).data("id"), function() {
		nota_listar_tabla();
	});
}

function nota_editar(row, e) {
	e.preventDefault();

	var link = $(row);
	link.attr('href', 'pages/notas/not_nota_modal.html');
	nota_modal(link);

}

function nota_modal(link) {

	// funcion q se ejecuta al cargar el modal
	var onShowModal = function() {

		_inputs('not_nota-frm');

		$('#not_nota-frm #btn-agregar').on(
				'click',
				function(event) {
					$('#not_nota-frm #id').val('');
					_post($('#not_nota-frm').attr('action'), $('#not_nota-frm')
							.serialize(), function(data) {
						onSuccessSave(data);
					});
				});

		if (link.data('id')) {
			_get('api/nota/' + link.data('id'), function(data) {
				_fillForm(data, $('#modal').find('form'));
				_llenarCombo('not_evaluacion', $('#id_ne'), data.id_ne);
				_llenarCombo('ges_trabajador', $('#id_tra'), data.id_tra);
				_llenarCombo('alu_alumno', $('#id_alu'), data.id_alu);
			});
		} else {
			_llenarCombo('not_evaluacion', $('#id_ne'));
			_llenarCombo('ges_trabajador', $('#id_tra'));
			_llenarCombo('alu_alumno', $('#id_alu'));
			$('#not_nota-frm #btn-grabar').hide();
		}

	}

	// funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data) {
		nota_listar_tabla();
	}

	// Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Evaluacion';
	else
		titulo = 'Nuevo  Nota Evaluacion';

	_modal(titulo, link.attr('href'), onShowModal, onSuccessSave);

}


function onckeydownNota(field, e){
	var id_niv = $('#id_niv').val();
	console.log(e.keyCode);
	var key = e.keyCode; 
	if(e.keyCode==37 || e.keyCode==39)
		return;
	//if(e.keyCode==109 || e.keyCode==189)
	//	field.value='';
	//else{
		
		e.preventDefault();
		
		if(id_niv==1){//INICIAL SOLO DEBE PERMITIR DE 1 A 4
			if((key>=96 && key<=100) || (key>=48 && key<=52) ){// SOLO PERMITE DESDE EL 0 AL 4
				if(field.value.length>=1  ){
					if( parseFloat(field.value)>4 || parseFloat(field.value)<1 )
						field.value='';
					else{
						if(parseFloat(field.value)<2)
							$(field).css('color','red');
						else
							$(field).css('color','black');
						$.tabNext();
						
					}
				}
			}else
				field.value='';
		}else{

			if(field.value.length>=2  ){
				
				if(parseFloat(field.value)>20 || parseFloat(field.value)<5)
					field.value='';
				else{
					if(parseFloat(field.value)<11)
						$(field).css('color','red');
					else
						$(field).css('color','black');
					$.tabNext();
				}
			}
		}
	//}
}

