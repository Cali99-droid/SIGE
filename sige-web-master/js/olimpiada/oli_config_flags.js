//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

//	$('#_botonera').html('<li><a id="oli_config-btn-nuevo"  href="#" onclick="_send(\'pages/olimpiada/oli_config_form.html\',\'Configuración Concurso\',\'Configuración Concurso\');" ><i class="icon-file-plus"></i> Nuevo Configuraci&oacute;n Olimpiada</a></li>');
 

	//lista tabla
	config_listar_tabla();
});

 
function _onchangeAnio(id_anio, anio){
	config_listar_tabla();
}

function config_modal(id){
	//console.log(index);
	//var table = $('#oli_config-tabla').DataTable();
	//var row = table.rows(index).data()[0];
	///console.log(row);
	
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		//datos
		_GET({
			context:_URL_OLI,
			url:'api/config/' + id,
			success:function(data){
				$('#result_flag').val(data.result_flag);
				$("#result_flag").trigger('change.select2');
				$('#nom').val(data.nom);
				$('#id_oli').val(data.id);
				$('#fec_ini_ins').val(_parseDate(data.fec_ini_ins));
				$('#fec_fin_ins').val(_parseDate(data.fec_fin_ins));
				$('#nom').focus();
			}
		});
				
		$('.daterange-time').daterangepicker({
			timePicker : true,
			singleDatePicker : true,
			applyClass : 'bg-slate-600',
			cancelClass : 'btn-light',
			locale : {
				format : 'DD/MM/YYYY h:mm a'
			}
		});
		
		$('#config-modal-frm').off();
		$('#config-modal-frm').on('submit', function(event){
			event.preventDefault();
			_POST({
				form:$('#config-modal-frm'), 
				context:_URL_OLI,
				function(data){
					config_listar_tabla();
				}});
		});
		
		
	}
	
	_modal('Permisos','pages/olimpiada/oli_config_flags_modal.html',onShowModal);

}

function config_listar_tabla(){
	_GET({ url: 'api/config/listar/' + $('#_id_anio').text(),
			context: _URL_OLI,
		   //params:,
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
							{"title":"Fecha", "data" : "fec"}, 
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"Organizador", "data" : "colegio"}, 
							{"title":"Acciones", "className": "text-center","render": function ( data, type, row ) {
								return '<div class="list-icons"> <a href="#" onclick="config_modal(' + row.id + ')" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'; 
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
		});

}

	

		
