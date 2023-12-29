//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	$('#_botonera').html('<li><a href="pages/academico/aca_feriado_modal.html" id="aca_feriado-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Feriados</a></li>');

	$('#aca_feriado-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		feriado_modal($(this));
	});

	//lista tabla
	feriado_listar_tabla();
});

function feriado_eliminar(link){
	_delete('api/feriado/' + $(link).data("id"),
			function(){
					feriado_listar_tabla();
				}
			);
}

function feriado_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/academico/aca_feriado_modal.html');
	feriado_modal(link);
	
}

function feriado_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('aca_feriado-frm');
		
		$('#aca_feriado-frm #btn-agregar').on('click',function(event){
			$('#aca_feriado-frm #id').val('');
			_post($('#aca_feriado-frm').attr('action') , $('#aca_feriado-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/feriado/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				//aca habria q transformar por MIETNRAS.. LUEGO VEO COMO SE HACE DE OTRA FORMA
				//ESTE METODO HAZLO EN EL SIGE. CREATE UN METODO _fechaHtml(data.dia); //q retonrne el nuevo formato
				var dia=data.dia;
				_fechaHtml(dia,'dia');
			});
		}else{
			$('#aca_feriado-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		feriado_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Feriados';
	else
		titulo = 'Nuevo  Feriados';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function feriado_listar_tabla(){
	_get('api/feriado/listar/',
			function(data){
			console.log(data);
				$('#aca_feriado-tabla').dataTable({
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
							{"title":"Nombre", "data" : "nom"}, 
							{"title":"D&iacute;a", "data" : "dia"}, 
							{"title":"Motivo", "data" : "motivo"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="feriado_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="feriado_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="feriado_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
								'</ul>'+
			                   '</li>'+
			                   '</ul>';}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			}
	);

}

