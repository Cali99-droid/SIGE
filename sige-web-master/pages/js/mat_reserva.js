//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//chancar el evento del combo año principal
function _onchangeAnio(id_anio, anio){
	reserva_listar_tabla();	
}


//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	//lista tabla
	reserva_listar_tabla();
});

function reserva_eliminar(link){
	_delete('api/reserva/' + $(link).data("id"),
			function(){
					reserva_listar_tabla();
				}
			);
}

function reserva_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_reserva_modal.html');
	reserva_modal(link);
	
}

function reserva_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_reserva-frm');
		
		$('#mat_reserva-frm #btn-agregar').on('click',function(event){
			$('#mat_reserva-frm #id').val('');
			$('#id_au').attr("disabled", false);
			_post($('#mat_reserva-frm').attr('action') , $('#mat_reserva-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
					$('#id_au').attr("disabled", true);
				}
			);
		});
		
		if (link.data('id')){
			_get('api/reserva/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('alu_alumno',$('#id_alu'), data.id_alu);
				_llenarCombo('col_aula',$('#id_au'), data.id_au);
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
				_llenarCombo('cat_cond_matricula',$('#id_cma'), data.id_cma);
				_llenarCombo('cat_cliente',$('#id_cli'), data.id_cli);
				_llenarCombo('per_periodo',$('#id_per'), data.id_per);
				_llenarCombo('alu_familiar',$('#id_fam'), data.id_fam);
			});
		}else{
			_llenarCombo('alu_alumno',$('#id_alu'));
			if($('#id_au').val()!=null){
				alert()
				$('#id_au').attr("disabled", true);
			}
				
			_llenarCombo('col_aula',$('#id_au'));
			_llenarCombo('cat_grad',$('#id_gra'));
			_llenarCombo('cat_nivel',$('#id_niv'));
			_llenarCombo('cat_cond_matricula',$('#id_cma'));
			_llenarCombo('cat_cliente',$('#id_cli'));
			_llenarCombo('per_periodo',$('#id_per'));
			_llenarCombo('alu_familiar',$('#id_fam'));
			$('#mat_reserva-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		reserva_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Reserva de matr&iacute;cula';
	else
		titulo = 'Nuevo  Reserva de matr&iacute;cula';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function reserva_listar_tabla(){
	var params ={id_suc : $('#_id_suc').text(), id_anio :$('#_id_anio').text()};
	_get('api/reserva/listar/',
			function(data){
			console.log(data);
				$('#mat_reserva-tabla').dataTable({
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
			        	 	{"title":"Alumno", "render":function ( data, type, row,meta ) { return row.ape_pat  + " "+ row.ape_mat + ", " + row.nom;} }, 
							{"title":"Nivel", "data" : "servicio"}, 
							{"title":"Grado", "data" : "grado"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
								if (row.id_res == null )
									//return '<button type="button" class="btn btn-default btn-xs">XSmall</button>';
									return '<button onclick="nuevaReserva(' + row.id_vac +',' + row.id_alu +')" type="button" class="btn btn-success btn-sm "><i class="glyphicon glyphicon-edit"></i> Reservar</button>';
								else
									return '<button onclick="editarReserva(' + row.id_res +',' + row.id_vac + ',' + row.id_alu + ')" type="button" class="btn btn-warning btn-sm "><i class="glyphicon glyphicon-edit"></i> Editar Reserva</button>';
								}
							}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
					 }
			    });
			},params
	);

}

function nuevaReserva(id_vac,id_alu){
	var params ={id_vac:id_vac, id_alu:id_alu, id_suc:$('#_id_suc').text(), id_anio:$('#_id_anio').text()};
	
	_get('api/reserva/validarCondicion?id_alu='+id_alu, function(data){
		
		params.condiconal=data;
		_get('api/reserva/editar', function(data){
			console.log(data);

			_send('pages/matricula/mat_reserva_form.html','Reserva de matricula','Creaci&oacute;n de nueva reserva',data);
		}, params);
	});
	
	
	
}

function editarReserva(id_res,id_vac,id_alu){
	var params ={id_res:id_res, id_vac:id_vac, id_alu:id_alu, id_suc:$('#_id_suc').text(), id_anio:$('#_id_anio').text()};
	
	_get('api/reserva/editar', function(data){
		console.log(data);
		_send('pages/matricula/mat_reserva_form.html','Reserva de matricula','Actualizac&iacute;on de reserva',data);
		
	}
	, params);
	
}