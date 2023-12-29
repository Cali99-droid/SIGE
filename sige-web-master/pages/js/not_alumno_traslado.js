 
function onloadPage(params){
 	_onloadPage();	
 
	$('#btn-grabar').on('click', function(e) {
		e.preventDefault();
		swal(
				{
					title : "Esta seguro?",
					text :"Todas las notas se grabaran en todas las evaluaciones del periodo del curso",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "rgb(33, 150, 243)",
					cancelButtonColor : "#EF5350",
					confirmButtonText : "Si, Grabar",
					cancelButtonText : "No, cancela!!!",
					closeOnConfirm : false,
					closeOnCancel : false
				},
				function(isConfirm) {
					_post('api/notaTraslado/grabar',$('#frm-notas').serialize(), function(data){
						console.log(data);
						$(this).attr('disabled','disabled');
					});
				}
			);
		
		
		
	});
	
	$("#alumno").autocomplete({
        minLength: 3,
        source: _URL + 'api/alumno/autocomplete?id_anio=' + $('#_id_anio').text(),
        search: function() {
            $(this).parent().addClass('ui-autocomplete-processing');
            $('#panel-alumno').hide();
        },
        open: function() {
            $(this).parent().removeClass('ui-autocomplete-processing');
        },
        select: function( event, ui ) {
        	$('#panel-alumno').show();
        	$('#id_niv').val(ui.item.id_niv);
        	$('#id_mat').val(ui.item.id);
        	$('#nivel').val(ui.item.nivel);
        	$('#grado').val(ui.item.grado);
        	$('#seccion').val(ui.item.secc);
        	$('#id_mat').val(ui.item.id);
        	$('.detalle').show();
			 
			var param = {
					id_niv : $('#id_niv').val(),
					id_anio : $('#_id_anio').text()
				};
			_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',
					$('#id_cpu'), null, param, function() {
						$('#id_cpu').change();
					});
        },
        change: function( event, ui ) {
        	//alert('change');
        	//$('#panel-alumno').hide();
        }

       
    });
	
	
	$('#id_cpu').on('change', function(){
		
		var param ={id_mat:$('#id_mat').val(), nump:$(this).val()};
		_get('api/notaTraslado/cursosPeriodo',function(data){

			$('#table-alumno').dataTable({
				 data : data,
				 aaSorting: [],
				 destroy: true,
				 pageLength: 50,
				 select: true,
		         columns : [ 
		             {"title": "Nro", "data" : "nro_rec","render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
		             {"title": "Curso", "data" : "curso"}, 
		             {"title": "Nota",  "render": function ( data, type, row ) {
		            	 var nota = "";
		            	 if (row.promedio!=null)
		            		 nota = row.promedio;
		                   return '<input id="nota" name="nota" size="4" value="' + nota + '" class="text-center" maxlength="2">';}
		        	   }
		        ],
		    "initComplete": function( settings ) {
		    	var notaPendiente = false;
		    	for (var i in data){
		    		var promedio = data[i].promedio;
		    		if (promedio==null){
		    			notaPendiente = true;
		    			break;
		    		}
		    	}
		    	
		    	if (notaPendiente)
		    		$('#btn-grabar').removeAttr('disabled');
		    	else
		    		$('#btn-grabar').attr('disabled','disabled');
		    }
		 });
		
		},param);
		

		
	});
	
	
}
 