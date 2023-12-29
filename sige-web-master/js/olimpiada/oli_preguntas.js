//Se ejecuta cuando la pagina q lo llama le envia parametros
var _indicadores =[];
var _param = {};//parametros de busqueda;
var _permiso_doc;
function onloadPage(params) {
	//alert(2);
	$('#btn-eliminar').hide();

	
	$('#id_niv').on('change',function(event){
		if($('#id_niv').val()!=''){
			_llenar_combo({
				url:'api/public/grado/listarGrados/'+$('#id_oli').val()+'/'+$('#id_niv').val(),
				combo:$('#id_og'),
				text:'grado',
				context: _URL_OLI,
				funcion:function(){
					$('#id_og').change();
				}
			});	
		}

	});
	
	$('#id_oli').on('change',function(event){
	_llenar_combo({
	   	url:'api/public/grado/listarNiveles/'+ $('#id_oli').val(),
		combo:$('#id_niv'),
		context:_URL_OLI,
		text:'nivel',
		funcion:function(){
			$('#id_niv').change();
		}
	});
	})
	
	_llenar_combo({
		tabla : 'oli_config',
		combo : $('#id_oli'),
		context : _URL_OLI,
		funcion : function() {
			$('#id_oli').change();
		}
		});;
	
	
	
	
}
// se ejecuta siempre despues de cargar el html
$(function() {
	_onloadPage();
	
});

$('#btn-buscar').on('click', function(event){

	var param={id_og:$('#id_og').val(), id_oli:$('#id_oli').val()}
		
	_GET({url:'api/config/listarPreguntas',
		  params: param,
		  context : _URL_OLI,
		  success:function(data){
			  console.log(data);
			  $('#btn-grabar').show();
			  $('#oli_preguntas-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 select: true,
					 scrollX: true,
					 pageLength: 50,
			         columns : [ 
						 {"title":"Pregunta", "data" : "nro_pre"},
						 {"title":"Respuesta", "render":function ( data, type, row,meta ) { 
								var res=(row.res != null) ? row.res :'';
								var id=(row.id != null) ? row.id :'';
								var nro_pre=(row.nro_pre != null) ? row.nro_pre :'';
								return "<input type='hidden' id='id' name='id' value='"+id+"' /><input type='hidden' id='nro_pre' name='nro_pre' value='"+nro_pre+"' /><input type='text' id='res' name='res' onkeyup=onckeyRespuesta(this,event) value='" +res + "' class='form-control'/>";
							} 
						 }
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			 
		  }
		});
});

$('#btn-grabar').on('click',function(event){
	 $('#btn-grabar').attr('disabled','disabled');
	_POST({form:$('#preguntas_frm'),
		  context:_URL_OLI,
		  success:function(data){
			   console.log(data);  
			   $('#btn-grabar').removeAttr('disabled');	 
		 }, 
		 error:function(data){
			 _alert_error(data.msg);
			 $('#btn-grabar').removeAttr('disabled');
		}
	}); 
});

$('#res').on('blur',function(e){
	  var valor = $(this).val();
	  //$(this).attr('type','text');
	  if(valor!='A' || valor!='B' ||  valor!='C' ||  valor!='D' ||  valor!='E')
		  $(this).val('');
  });

function onckeyRespuesta(field, e){
	if(field.value.length>=1  ){
		field.value=field.value.toUpperCase(); 
				if(field.value=='A' || field.value=='B' || field.value=='C' || field.value=='D' || field.value=='E')
					$.tabNext();					
				else
					field.value='';
	}
}

