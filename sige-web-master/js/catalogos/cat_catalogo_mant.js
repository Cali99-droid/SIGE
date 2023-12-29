//Se ejecuta cuando la pagina q lo llama le envia parametros
var id_anio=$('#_id_anio').text();
function onloadPage(params){
	_onloadPage();
	var id_anio=$('#_id_anio').text();
	/*listar_periodos(id_anio);
	$("#div_periodo").show();
	$("#div_ciclo").hide();
	$("#frm-mant_periodo #id_anio").val(id_anio);*/
	var pri_cat=($('#tabla_catalogos').find('tr:nth-child(1)').find('#cat').val());
	var pri_nombre_cat=($('#tabla_catalogos').find('tr:nth-child(1)').find('#nombre').text());
	//alert(nombre_cat,);
	listar_datos(pri_cat,pri_nombre_cat);
	$("#tabla_catalogos tbody tr").click(function(){ 
	var cat=($(this).find('td:nth-child(2)').find('#cat').val());
	var nombre_cat=($(this).find('td:nth-child(2)').find('#nombre').text());
	listar_datos(cat,nombre_cat);
	});
}

function listar_datos(cat,nombre_cat){
	//alert(cat);
	var param={cat:cat};
	$('#catalogo #nom_catalogo').text(nombre_cat);
	$('#catalogo #cat').val(cat);
	//$('#_botonera').html('<li><a href="pages/catalogos/cat_area_modal.html" id="cat_area-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Area</a></li>');
	_get('api/catalogo/listar',function(data){
	$('#tabla_listado_catalogo').dataTable({
	 data : data,
	 /*
	 destroy: true,
	 select: true,
	 bFilter: false,
	 */
	 searching: false, 
	 paging: false, 
	 info: false,
	 aaSorting: [],
	 destroy: true,
	 orderCellsTop:true,
	 select: true,
	 columns : [ 
			{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} }, 
			{"title":"Nombre", "render": function ( data, type, row ) {
				return '<input type="hidden" id="id" name="id" value="'+row.id+'"/>'+row.nom
			}}
			],
				"initComplete": function( settings ) {
				//_initCompleteDT(settings);
				_initCompleteDTSB(settings);
				//listar_ciclos(id_per_pri);
				$("#tabla_listado_catalogo tbody tr").click(function(){ 
					//e.preventDefault();
					//var link = null;
					//link.attr('href','pages/catalogos/cat_catalogo_modal.html');
					//var cat=$(this).find('td:nth-child(2)').find('#nombre').text();
					var id=$(this).find('td:nth-child(2)').find('#id').val();
					//alert(cat);
					catalogo_modal(nombre_cat,id,cat);
				});	   
				}
			});
	},param);
}

function catalogo_modal(nom_cat,id,cat){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('cat_catalogo-frm');
		$('#cat_catalogo-frm #cat').val(cat);
		$('#cat_catalogo-frm #btn-grabar').on('click',function(event){
			event.preventDefault();
			var validation = $('#frm-mant_ciclo').validate(); 
		if ($('#cat_catalogo-frm').valid()){
			_post($('#cat_catalogo-frm').attr('action') , $('#cat_catalogo-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		}		
		});
		
		if (id){
			_get('api/catalogo/' +id+'/'+cat,
			function(data){
				_fillForm(data,$('#modal').find('form') );
			});
		}else{
			$('#cat_area-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		$('#cat_catalogo-frm #id').val(data.result);
		listar_datos(cat,nom_cat);

	}
	
	//Abrir el modal
	var titulo;
	//if (link.data('id'))
		titulo = 'Mantenimiento de '+nom_cat;
	/*else
		titulo = 'Nuevo  Area';*/
	
	_modal(titulo, 'pages/catalogos/cat_catalogo_modal.html',onShowModal,onSuccessSave);

}
$('#catalogo #btn_nuevo').on('click',function(event){
	catalogo_modal($('#catalogo #nom_catalogo').text(),null,$('#catalogo #cat').val());			
});
