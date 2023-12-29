function onloadPage(params){
	//alert('onload pagos buscar');
	_onloadPage();	
	/*var fncLlenarCombo = function(data){
	     console.log(data);
	    	 var options = "";
	    	 options += '<option value="">TODOS</option>';
	         for (var i = 0; i < data.length; i++) {
	             options += '<option value="' + data[i].id + '">' + data[i].nom + '</option>';
	         }
	         $("#local").html(options);
	};
	//PONES LA URL DE TU REST Y LA FUNCION Q SE EJECUTARA AL RECIBIR LA DATA JSON
	_get('api/reporte/listaCombo', fncLlenarCombo, null);
	_llena*/
	$("#id_anio").val($('#_id_anio').text());
	
	var id_suc=$("#_id_suc").text();
	if (id_suc!='0' ){
		_llenarCombo('ges_sucursal_sec',$('#local'),null,id_suc);
	} else{
			_llenarCombo('ges_sucursal',$('#local'));		
	}
	
	
	$('#frm-reporte').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		return _get('api/reporte/reporte',fncExito, $(this).serialize());
	});
}

var fncExito = function(data){
	$('#panel-alumnos').css("display","block");

	
	$('#tabla-reporte').dataTable({
		 data : data,
		 aaSorting: [],
		 destroy: true,
		 pageLength: 1000,
		 select: true,
         columns : [ 
           {"title": "FAMILIA", "data" : "familia"}, 
           {"title":"APODERADO", "data" : "apoderado" ,
        	   "render": function ( data, type, row ) {
                   return '<a href="#" onclick="datosApoderado(' + row.id + ',\'' + row.apoderado +'\')" >'  + row.apoderado +' </a>';}   
           },
           {"title":"NRO. DOC", "data" : "nro_doc" },
           {"title":"NRO. CONTRATO", "data" : "num_cont" },
           {"title":"NRO. ADENDA", "data" : "num_adenda" },
           {"title":"LOCAL", "data" : "local" },
           {"title":"CEL", "data" : "cel" },
           {"title":"NRO. HIJOS", "data" : "nro_hijos" },
        ]
    });
	
};

/****************************/
/** 	Mostramos datos del apoderado	***/
/****************************/
function datosApoderado(id, apoderado){
	//e.preventDefault();
	movimiento_modal(id, apoderado);
}


function movimiento_modal(id, apoderado){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
	$('#id').val(id);
	$('#fam').val(apoderado); 
	//$("#tlf").mask("(999)99-9999");
	//$("#cel").mask("999-999-999");
	_get('api/familiar/' + id,
	function(data){
	_fillForm(data,$('.modal').find('form') );
	}
	);
	}

	var onSuccessSave = function(data){
		listar_tabla();
	}
	var titulo;
	titulo = 'Datos Personales del Apoderado';
	
	_modal(titulo, 'pages/reportes/fam_datos_personales.html',onShowModal,onSuccessSave);

}

function listar_tabla(){
	$('#frm-reporte').submit();	
	//$("#id_suc").val($("#_id_suc").html());
	//return _get('api/reporte/reporte',fncExito, $(this).serialize());
	//Lina este $(this) es nulo, por eso falla..
	//$(this) se usa para el objeto que usa el jquery... aca no existe un objeto jquery
	//en la linea 17 si lo usas.. por q tu objeto es el formulario.. y $(this) se refiere al objeto formulario
}