//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	_llenar_combo({
		tabla : 'cat_tipo_documento',
		combo : $('#id_tdc'),
		text: 'nom'
	});
	
	
	$.getJSON( "pages/rrhh/ges_trabajador/lista_contrato.json", function( data ) {

		$('#ges_contrato-tabla').dataTable({
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
	        	 	{"title":"REgimen", "render": function ( data, type, row ) {
		            	   return row.regimen;
		            }} , 
		            {"title":"Inicio", "data": "fec_ini"}, 
					{"title":"Fin", "data" : "fec_fin"},
					{"title":"Acciones", "render": function ( data, type, row ) {
	                   return '<div class="list-icons"> <a href="#" data-id="135" onclick="alumno_descuento_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a><a href="#" data-id="135" onclick="alumno_descuento_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></div>';
	                   }
					}
		    ],
		    "initComplete": function( settings ) {
				   _initCompleteDT(settings);
			 }
	    });
		
	});
	
	$.getJSON( "pages/rrhh/ges_trabajador/lista_regimenes.json", function( data ) {

		$('#ges_regimen-tabla').dataTable({
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
	        	 	{"title":"Regimen", "render": function ( data, type, row ) {
		            	   return row.regimen;
		            }} , 
		            {"title":"Inicio", "data": "fec_ini"}, 
		            {"title":"Fin", "data" : "fec_fin"},
		            {"title":"Entidad", "data" : "entidad"},
					{"title":"Acciones", "render": function ( data, type, row ) {
	                   return '<div class="list-icons"> <a href="#" data-id="135" onclick="alumno_descuento_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a><a href="#" data-id="135" onclick="alumno_descuento_eliminar(this)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a></div>';
	                   }
					}
		    ],
		    "initComplete": function( settings ) {
				   _initCompleteDT(settings);
			 }
	    });
		
	});
	
	
	$('#btn-nuevo-contrato').on('click',function(){
		
		contrato_modal();
	});
	
	


	function contrato_modal(){

		//funcion q se ejecuta al cargar el modal
		var onShowModal = function(){
			 
					
			}
				
				
		

		//funcion q se ejecuta despues de grabar
		var onSuccessSave = function(data){

		}
		
		//Abrir el modal
		var titulo;

			titulo = 'Nuevo  Contrato';
		
		_modal_full(titulo, "pages/rrhh/ges_trabajador/contrato.html",onShowModal,onSuccessSave);

	}

	
});


