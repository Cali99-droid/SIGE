//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/notas/not_cronograma_libreta_modal.html" id="not_cronograma_libreta-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Cronograma para recojer libretas</a></li>');

	$('#not_cronograma_libreta-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		cronograma_libreta_modal($(this));
	});

	//lista tabla
	cronograma_libreta_listar_tabla();
});

function cronograma_libreta_eliminar(link){
	_delete('api/cronogramaLibreta/' + $(link).data("id"),
			function(){
					cronograma_libreta_listar_tabla();
				}
			);
}

function cronograma_libreta_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/notas/not_cronograma_libreta_modal.html');
	cronograma_libreta_modal(link);
	
}

function cronograma_libreta_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('not_cronograma_libreta-frm');
		
		
		$('#not_cronograma_libreta-frm #id_anio').val($('#_id_anio').text());

		$('#not_cronograma_libreta-frm #btn-agregar').on('click',function(event){
			$('#not_cronograma_libreta-frm #id').val('');
			_post($('#not_cronograma_libreta-frm').attr('action') , $('#not_cronograma_libreta-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		
		
		
		if (link.data('id')){
			_get('api/cronogramaLibreta/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				var fec_ini= _parseDate(data.fec_ini);
				var fec_fin= _parseDate(data.fec_fin);
				$('#fec_ini').val(fec_ini);
				$('#fec_fin').val(fec_fin);	
				$('.daterange-single').daterangepicker({ 
			        singleDatePicker: true,
			        locale: { format: 'DD/MM/YYYY'}
			    });
				$('#id_niv').on('change',function(event){
					_llenarCombo('cat_grad',$('#id_gra'),data.id_gra,$('#id_niv').val());
					var param1 = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
					_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), data.id_cpu,param1,null);
				});
				_llenarCombo('cat_nivel',$('#id_niv'),data.id_niv,null,function(){
					$('#id_niv').change();				
				});
			});
		}else{
			$('#id_niv').on('change',function(event){
				_llenarCombo('cat_grad',$('#id_gra'),null,$('#id_niv').val());
				var param1 = {id_niv: $('#id_niv').val(), id_anio: $('#_id_anio').text()};
				_llenarComboURL('api/cronogramaLibreta/listarPeriodosX_Nivel',$('#id_cpu'), null,param1,null);
				});
			_llenarCombo('cat_nivel',$('#id_niv'),null,null,function(){
				$('#id_niv').change();				
			});

			$('#not_cronograma_libreta-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		cronograma_libreta_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Cronograma para recojer libretas';
	else
		titulo = 'Nuevo  Cronograma para recojer libretas';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function cronograma_libreta_listar_tabla(){
	id_anio = $('#_id_anio').text();
	_get('api/cronogramaLibreta/listar/'+id_anio,
			function(data){
			console.log(data);
				$('#not_cronograma_libreta-tabla').dataTable({
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
							{"title":"A&ntilde;o", "data": "anio.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Periodo","render":function ( data, type, row,meta ) { return row.perUni.periodoAca.nom  + " " + row.perUni.nump} }, 
							{"title":"Desde", "render":function ( data, type, row,meta ) { return _parseDate(row.fec_ini)} }, 
							{"title":"Hasta", "render":function ( data, type, row,meta ) { return _parseDate(row.fec_fin)} },
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<div class="list-icons"> <a href="#" data-id="' + row.id + '" onclick="cronograma_libreta_editar(this, event)" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a>'+ 
								' <a href="#" data-id="' + row.id + '" onclick="cronograma_libreta_eliminar(this)"><i class="fa fa-trash-o ui-blue ui-size"></i></a></div>'; 
							}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

