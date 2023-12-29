//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

/*	$('#_botonera').html('<li><a href="pages/evaluacion/eva_vacante_modal.html" id="eva_vacante-btn-nuevo" ><i class="icon-file-plus"></i> Nuevo Vacante</a></li>');

	$('#eva_vacante-btn-nuevo').on('click', function(e) {
		e.preventDefault();
		vacante_modal($(this));
	});

	//lista tabla
	vacante_listar_tabla();*/

	$('#id_eva').on('change',function(event){
		var id_niv=$('#id_per option:selected').attr("data-aux1");
		var param={id_eva:$("#id_eva").val(),id_niv:id_niv};
		_get('api/vacante/listarGrados/',
				function(data){
				console.log(data);
					$('#vacante_table').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 orderCellsTop:true,
						 select: true,
				         columns : [
				                    
				        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				        	 	{"title":"Grado", "render":function ( data, type, row,meta ) {
									return "<input type='text' name ='id_grad' value='" + row.nom + "'  />";
				        	 	}},
				        	 	{"title":"Capacidad", "render":function ( data, type, row,meta ) {
									return "<input type='text' name ='capacidad' value=''  />";
				        	 	}},
				        	 	{"title":"Vacantes Ofertadas", "render":function ( data, type, row,meta ) {
									return "<input type='text' name ='vac_ofe' value='" + row.vac_ofe + "'  />";
				        	 	}},
				        	 	{"title":"Postulantes", "render":function ( data, type, row,meta ) {
									return "<input type='text' name ='post' value='" + row.post + "'  />";
				        	 	}}
					    ],
					    "initComplete": function( settings ) {
							   _initCompleteDT(settings);
								//$('.daterange-single').val('');
								//$('.daterange-single').prop('disabled',true);
						 }
				    });
				}, param
		);
	});
	
	$('#id_per').on('change',function(event){
		var param={id_per:$('#id_per').val()};
		_llenarComboURL('api/vacante/listarEvaluaciones',$('#id_eva'), null,param);
	});
	
	var param={id_anio:$('#_id_anio').text()};
	_llenarComboURL('api/periodoAca/listarPeriodosVacante',$('#id_per'),null,param);
	
	_llenarCombo('cat_grad',$('#id_gra'));
	$('#eva_vacante-frm #btn-grabar').hide();
	
});

function vacante_eliminar(link){
	_delete('api/vacante/' + $(link).data("id"),
			function(){
					vacante_listar_tabla();
				}
			);
}

function vacante_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/evaluacion/eva_vacante_modal.html');
	vacante_modal(link);
	
}

function vacante_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('eva_vacante-frm');
		
		$('#eva_vacante-frm #btn-agregar').on('click',function(event){
			$('#eva_vacante-frm #id').val('');
			_post($('#eva_vacante-frm').attr('action') , $('#eva_vacante-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/vacante/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				var param={id_anio:$('#_id_anio').text()};
				_llenarComboURL('api/periodoAca/listarPeriodosVacante',$('#id_per'),data.id_per,param);
				$('#id_per').on('change',function(event){
					var param={id_per:$('#id_per').val()};
					_llenarComboURL('api/vacante/listarEvaluaciones',$('#id_eva'), data.id_eva_vac,param);	
				});
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
			});
		}else{
			var param={id_anio:$('#_id_anio').text()};
			_llenarComboURL('api/periodoAca/listarPeriodosVacante',$('#id_per'),null,param);
			$('#id_per').on('change',function(event){
				alert();
				var param={id_per:$('#id_per').val()};
				_llenarComboURL('api/vacante/listarEvaluaciones',$('#id_eva'), null,param);
			});
			_llenarCombo('cat_grad',$('#id_gra'));
			$('#eva_vacante-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		vacante_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Vacante';
	else
		titulo = 'Nuevo  Vacante';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function vacante_listar_tabla(){
	_get('api/vacante/listar/',
			function(data){
			console.log(data);
				$('#eva_vacante-tabla').dataTable({
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
							{"title":"Capacidad ", "data" : "nro_vac"}, 
							{"title":"Vacantes ofertadas ", "data" : "vac_ofe"}, 
							{"title":"Postulantes", "data" : "post"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Evaluaci&oacute;n Vacante", "data": "evaluacionVac.des"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								 return '<div class="list-icons"> <a href="#" class="list-icons-item"><i class="fa fa-pencil ui-blue ui-size" aria-hidden="true"></i></a> <a href="#" class="list-icons-item"><i class="fa fa-trash ui-blue ui-size" aria-hidden="true"></i></a> <a href="#" class="list-icons-item"><i class="fa fa-cog ui-blue ui-size" aria-hidden="true"></i></a></div>';

			     //               return '<ul class="icons-list">'+
								// 	'<li class="dropdown">'+
								// '<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
								// 	'<i class="icon-menu9"></i>'+
								// 	'</a>'+
								// '<ul class="dropdown-menu dropdown-menu-right">'+
								// 	'<li><a href="#" data-id="' + row.id + '" onclick="vacante_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
								// 	'<li><a href="#" data-id="' + row.id + '" onclick="vacante_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
								// '</ul>'+
			     //               '</li>'+
			     //               '</ul>';
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

