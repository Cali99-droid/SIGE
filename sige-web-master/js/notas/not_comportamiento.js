//Se ejecuta cuando la pagina q lo llama le envia parametros
var _cualitativo;
function onloadPage(params){
	
	$('#id_cpu').on('change',function(e){
		$('#not_comportamiento-div').hide();
		$('#btn-grabar').hide();
		$('#btn-actualizar').hide();
	});
	
	$('#id_cur').on('change',function(event) {

		var param1 = {id_niv : $('#id_niv').val(), id_anio: $('#_id_anio').text() };
		_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
			$('#id_cpu'), null, param1);

		});


	$('#id_au').on(
			'change',
			function(event) {
				_llenarComboURL('api/comportamiento/listarCursosTutor', $('#id_cur'),null, param, function(){$('#id_cur').change()});
			});

	$('#id_gra').on(
			'change',
			function(event) {
				var param = {
					id_tra : _usuario.id_tra,
					id_grad : $(this).val(),
					id_suc : $('#id_suc').val(),
					id_anio : $('#_id_anio').text()
				};
				_llenarComboURL('api/comportamiento/listarAulaTutor', $('#id_au'),	null, param, function() {
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
				};
				_llenarComboURL('api/comportamiento/listarGrados', $('#id_gra'),
						null, param, function() {
							$('#id_gra').change();
						});

			});

	$('#id_suc').on(
			'change',
			function(event) {
				// _llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val(),function(){$('#id_niv').change();});
				var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text()
				};
				_llenarComboURL('api/comportamiento/listarNivelesTutor', $('#id_niv'),
						null, param, function() {
							$('#id_niv').change();
						});
			});
	
	

	var param = {
		id_tra : _usuario.id_tra,
		id_anio : $('#_id_anio').text()
	};
	
	_llenarComboURL('api/comportamiento/listarSucursalTutor', $('#id_suc'), null, param,
			function() {
				$('#id_suc').change();
	});
	
	
	$('#comportamiento-frm').on('submit', function(e) {
		e.preventDefault();
		_param = {};
		_param.id_cur = $('#id_cur').val();
		_param.id_niv = $('#id_niv').val();
		_param.id_cpu = $('#id_cpu').val();
		_param.id_au = $('#id_au').val();
		_param.id_anio = $('#_id_anio').text();

		$('#not_comportamiento-div').show();
		_get('api/comportamiento/listarAlumnosNotasCom', function(data) {
			_cualitativo =  true; // ESTO DEBE LLAMARSE DESDE EL REST ( PARAMETRO POR COLEGIO)
			 _capacidades =[];
			var columns = [{"title": "Nro","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title": "Alumno","render": function  ( data, type, row,meta ) { return row.ape_pat + " " +  row.ape_mat + ", " + row.nom;  }}
						  ];

			var cap_count=0;
			
			for (var key in data.capacidades) {
				var capacidad = data.capacidades[key];
				var titulo = ++cap_count;
				titulo = "C" + titulo;
				_capacidades.push( capacidad.id);//esto esta bien... 330 hasta 334 no entiendo bien q hace ese mta.col-2
				var id_cap = capacidad.id;//es otra historia del meta.col-- no se deberia usar... sino q es una ayuda para indicar la columna
				
				var element = {"title": titulo,"data": "ncc_" + capacidad.id, 
						"render":function( data, type, row,meta ){
							var value = row['ncc' +  _capacidades[meta.col-2]+'_nota' ];//esto es el value
							var id = row['ncc' +  _capacidades[meta.col-2] + '_id'] ;//nni19.id
							var value_db=value;
							
							if(_cualitativo  ){
								//console.log(value);
								if(value!=null && value!=''){
									if(value=='4')
					    				  value='AD';
					    			  else if(value=='3')
					    				  value = 'A';
					    			  else if(value=='2')
					    				  value = 'B';
					    			  else if(value=='1')
					    				  value = 'C';
					    			  else { 
					    				  value ='';
										  //value_db='';
									  }
								}else{
									value='';
									value_db='';
								}
								return "<input id='n"  + row.id + "_" + _capacidades[meta.col-2] +"' min='1' max='4' class='form-control' onkeyup=onckeydownNota(this,event) data-id='" + id + "' data-bd='" + value_db + "'value='" + value + "'/>";
							}
							else
								return "<input type='number' id='n"  + row.id + "_" + _capacidades[meta.col-2] +"' min='0' max='20' class='form-control' onkeyup=onckeydownNota(this,event) data-id='" + id + "' data-bd='" + value + "'value='" + value + "'/>";
						} };
				
				
				columns.push(element);
			}
			
			//console.log(columns);
			
			
			
			
			if ( $.fn.DataTable.isDataTable('#not_comportamiento-tabla') ) {
				  $('#not_comportamiento-tabla').DataTable().destroy();
				}

			
			//console.log( data.alumnosCapacidades);
			$('#not_comportamiento-tabla').dataTable({
				    data : data.alumnosCapacidades,
				    columns:columns,
					pageLength: 50,
					aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 select: true,		 
			    "initComplete": function( settings ) {
			    	
			    	  //formatear la nota
			    	  var nota_llena = false;
			    	  //console.log("_cualitativo:" + _cualitativo);
		    		  
			    	  $('#not_comportamiento-tabla input').each(function(){
			    		  var val = $(this).data('bd');
			    		  if (val !=null && typeof val !='undefined')
			    			  val = val.toString();
			    		 // console.log(val.length);
			    		  if(!_cualitativo && val.length==1)
			    			  $(this).val('0' + val);
			    		  
			    		  if((!_cualitativo && val.length>1) || (_cualitativo && val.length>0)){
			    			  nota_llena = true;
			    			  //console.log("xxxxxxxx");
			    		  }
			    		  
			    		  if( (_cualitativo &&  parseFloat(val)<2 ) || (!_cualitativo && parseFloat(val)<11 ))
			    			  $(this).css('color','red');
			  			  else
			  				  $(this).css('color','black');
			    		  
			    	  });
			    	  
			    	  //alert(nota_llena);
			    	  if(_cualitativo){
			    		  
			    		  $('#not_comportamiento-tabla input').on('blur',function(e){
			    			  var valor = $(this).val();
			    			  if(valor=='4'){
			    				  $(this).val('AD');
			    			  }else if(valor=='3')
			    				  $(this).val('A');
			    			  else if(valor=='2')
			    				  $(this).val('B');
			    			  else if(valor=='1')
			    				  $(this).val('C');
			    			  else{
			    				  $(this).val('');
			    			  }
			    		  });

			    		  $('#not_comportamiento-tabla input').on('focus',function(e){
			    			  var valor = $(this).val();
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

			    	  for (var j=0; j<data.capacidades.length; j++){
			    		  var $th = $("#not_comportamiento-tabla thead tr th").eq(j+2);
				    	  $th.attr('title',data.capacidades[j].capacidad  );  
			    	  }
			    	  
			    	  //boton grabar o actualizar
			    	  if(data.alumnosCapacidades.length>0){
			    		  if (nota_llena){
			    			  $('#btn-actualizar').show(); 
			    			  $('#btn-grabar').hide();
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

	});
}

function comportamiento_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_comportamiento-frm');
		
		$('#not_comportamiento-frm #btn-agregar').on('click',function(event){
			$('#not_comportamiento-frm #id').val('');
			_post($('#not_comportamiento-frm').attr('action') , $('#not_comportamiento-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/comportamiento/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('ges_trabajador',$('#id_tra'), data.id_tra);
				_llenarCombo('alu_alumno',$('#id_alu'), data.id_alu);
				_llenarCombo('col_aula',$('#id_au'), data.id_au);
				_llenarCombo('col_per_uni',$('#id_cpu'), data.id_cpu);
			});
		}else{
			_llenarCombo('ges_trabajador',$('#id_tra'));
			_llenarCombo('alu_alumno',$('#id_alu'));
			_llenarCombo('col_aula',$('#id_au'));
			_llenarCombo('col_per_uni',$('#id_cpu'));
			$('#not_comportamiento-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		comportamiento_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Nota Comportamiento';
	else
		titulo = 'Nuevo  Nota Comportamiento';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


$('#btn-grabar').on('click',function(){
	var comportamiento ={};
	comportamiento.id_au = $('#id_au').val();
	comportamiento.id_cpu =$('#id_cpu').val();
	comportamiento.id_tra= _usuario.id_tra;
	comportamiento.id_cap = _capacidades;
	//notas
	var table= $('#not_comportamiento-tabla').DataTable();
	var notaAlumno_arreglo = [];
	var error= false;
	var _input_error =null;
	table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
		var data = this.data();
	    console.log(data);

	    var notaAlumno_item = {};
		
		var notaCapacidades = [];
		notaAlumno_item.id_alu = data.id;
		//ah... es para enviar la data al servidor....
		for (var i=0;i< _capacidades.length; i++) {
			var id_cap = _capacidades[i];
			if($('#n' + data.id + '_' + id_cap).val()==''){
				console.log('error');
				error= true;
				_input_error = $('#n' + data.id + '_' + id_cap);
			}
			
			var nota_input = $('#n' + data.id + '_' + id_cap).val();

			//if ($('#id_niv').val()=='1'){
			if (_cualitativo){
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
			notaCapacidades.push(nota_input);
		}
		
		notaAlumno_item.notas = notaCapacidades;
	    
		notaAlumno_arreglo.push(notaAlumno_item);

	} );
	
	comportamiento.notaComAlumno = notaAlumno_arreglo;
	if(error)
		_alert_error('Todas las notas deben estar completadas.',
		function(){
			_input_error.focus();
		});
	else{
		_post_json('api/comportamiento/grabar', comportamiento, function(data) {
			$('#btn-actualizar').show();
			$('#btn-grabar').hide();
			$('#comportamiento-frm').submit();
		});
	}
});

$('#btn-actualizar').on('click',function(){
	var comportamiento ={};
	comportamiento.id_au = $('#id_au').val();
	comportamiento.id_cpu =$('#id_cpu').val();
	comportamiento.id_tra= _usuario.id_tra;
	comportamiento.id_cap = _capacidades;
	console.log('iteracion de filas');
	//notas
	var table= $('#not_comportamiento-tabla').DataTable();
	var notaAlumno_arreglo = [];
	var error= false;
	table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
		var data = this.data();
	    
		for (var i=0;i< _capacidades.length; i++) {
			var id_cap = _capacidades[i];
			var input = $('#n' + data.id + '_' + id_cap);
			var id = input.data('id');
			var bd = input.data('bd');
			var value = input.val();
			if (value==''){
				error = true;// _alert_error('Es obligatorio ingresar la nota');
			}
			
			//en caso de inicial
			//if ($('#id_niv').val()=='1'){
			if (_cualitativo){
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
		
			if(id!=''){

					if (parseInt(value)!= parseInt(bd)){
					
					var notaAlumno_item = {};
						
					notaAlumno_item.id = id;
					notaAlumno_item.nota = value;
					notaAlumno_item.id_usr = _usuario.id_tra;
					console.log(notaAlumno_item)
						
					notaAlumno_arreglo.push(notaAlumno_item);
				}
			}
		}
		
		
		//notaAlumno_arreglo.push(notaAlumno_item);

	} );
	
 
	if(error)
		_alert_error('Es obligatorio ingresar la nota');
	else
	_post_json('api/comportamiento/actualizar', notaAlumno_arreglo, function(data) {
		
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
	
});

$('#btn-eliminar').on('click',function(){
	
	var id_au=$('#id_au').val();
	var id_cpu=$('#id_cpu').val();
	_delete('api/comportamiento/' + id_au+'/'+id_cpu,
			function(){
		$('#comportamiento-frm').submit();
	},
	'De elimar todas las notas para este periodo?'
			);
});
 

function onckeydownNota(field, e){
	var id_niv = $('#id_niv').val();
	var key = e.keyCode;
	
	if(e.keyCode==37 || e.keyCode==39)
		return;
	
	e.preventDefault();
		
		if(_cualitativo){//INICIAL SOLO DEBE PERMITIR DE 1 A 4
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