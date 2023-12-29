//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	//_onloadPage();

	$("#apellidosNombres").focus();
		
	var fncExito = function(data){
		console.log(data);
		$('#panel-matricula-alumnos').css("display","block");
		$('#tabla-matricula-alumnos').dataTable({
			 data : data.alumnoList,
			 aaSorting: [],
			 destroy: true,
			 pageLength: 50,
			 select: true,
	         columns : [ 
	           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
	           //{"title":"Alumno", "data" : "alumno"}, 
	           {"title":"Alumno", "render": function ( data, type, row ) {
            	   return "<a href='#' data-id='" + row.id_alu + "' onclick='_ver_alumno(this)'>"  + row.ape_pat+" "+row.ape_mat+" "+row.nom + "</a>";
	           }} , 
	           {"title":"Acciones", "render": function ( data, type, row ) {
            	   if (row.id_mat!=null){
                   return '<a href="#" data-id="' + row.id_mat + '" onclick="matricula_editar(' + row.id_mat + ',\'' + row.ape_pat +'\',\''+row.ape_mat +'\',\''+row.nom+'\',\''+row.id_alu+'\',\''+row.id_gpf+'\')"></i> Editar Matricula</a>';
            	   } else
            	   return '<a href="#" data-id="" onclick="matricula_editar(' + row.id_mat + ',\'' + row.ape_pat +'\',\''+row.ape_mat +'\',\''+row.nom+'\',\''+row.id_alu+'\',\''+row.id_gpf+'\')"><i class="fa fa-pencil-square-o"></i> Nueva Matricula</a>';
            	   
	           }
	           }
	        ]
	    });
		
	};
	
	$('#frm-matricula-buscar').on('submit',function(event){
		event.preventDefault();
		//alert($("#_id_suc").html());
		$("#id_suc").val($("#_id_suc").html());
		var param= {apellidosNombres:$('#apellidosNombres').val(), id_anio: $('#_id_anio').text(), id_suc:$('#id_suc').val()};
		return _get('api/matricula/MatriculaBuscar2',fncExito, param);
	});
});

function matricula_editar(id_mat, ape_pat, ape_mat, nom, id_alu, id_gpf){
	var alumno=ape_pat+" "+ape_mat+" "+nom;
	_send('pages/matricula/mat_matricula_form.html','Registro de Matrícula','Matrícula', {id_mat:id_mat, alumno:alumno, id_alu:id_alu, id_gpf:id_gpf});
	
}

/*function matricula_eliminar(link){
	_delete('api/matricula/' + $(link).data("id"),
			function(){
					matricula_listar_tabla();
				}
			);
}

function matricula_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_matricula_modal.html');
	//matricula_modal(link);
	
}

function matricula_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('mat_matricula-frm');
		
		$('#mat_matricula-frm #btn-agregar').on('click',function(event){
			$('#mat_matricula-frm #id').val('');
			_post($('#mat_matricula-frm').attr('action') , $('#mat_matricula-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/matricula/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo('alu_alumno',$('#id_alu'), data.id_alu);
				_llenarCombo('alu_familiar',$('#id_fam'), data.id_fam);
				_llenarCombo('alu_familiar',$('#id_fam'), data.id_fam);
				_llenarCombo('cat_cond_matricula',$('#id_cma'), data.id_cma);
				_llenarCombo('cat_cliente',$('#id_cli'), data.id_cli);
				_llenarCombo('per_periodo',$('#id_per'), data.id_per);
				_llenarCombo('col_aula',$('#id_au'), data.id_au);
				_llenarCombo('col_aula',$('#id_au'), data.id_au);
				_llenarCombo('cat_grad',$('#id_gra'), data.id_gra);
				_llenarCombo('cat_nivel',$('#id_niv'), data.id_niv);
			});
		}else{
			_llenarCombo('alu_alumno',$('#id_alu'));
			_llenarCombo('alu_familiar',$('#id_fam'));
			_llenarCombo('alu_familiar',$('#id_fam'));
			_llenarCombo('cat_cond_matricula',$('#id_cma'));
			_llenarCombo('cat_cliente',$('#id_cli'));
			_llenarCombo('per_periodo',$('#id_per'));
			_llenarCombo('col_aula',$('#id_au'));
			_llenarCombo('col_aula',$('#id_au'));
			_llenarCombo('cat_grad',$('#id_gra'));
			_llenarCombo('cat_nivel',$('#id_niv'));
			$('#mat_matricula-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		matricula_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Matricula del alumno';
	else
		titulo = 'Nuevo  Matricula del alumno';
	
	//_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function matricula_listar_tabla(){
	_get('api/matricula/listar/',
			function(data){
			console.log(data);
				$('#mat_matricula-tabla').dataTable({
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
							{"title":"Fecha matricula", "data" : "fecha"}, 
							{"title":"Presenta carta poder", "data" : "car_pod"}, 
							{"title":"N&uacute;mero de Contrato", "data" : "num_cont"}, 
							{"title":"Observaci&oacute;n", "data" : "obs"}, 
							{"title":"Alumno", "data": "alumno.nom"}, 
							{"title":"Apoderado", "data": "familiar.nom"}, 
							{"title":"Encargado", "data": "familiar.nom"}, 
							{"title":"Condicion Matricula", "data": "condMatricula.nom"}, 
							{"title":"Cliente", "data": "cliente.nom"}, 
							{"title":"Periodo", "data": "periodo.id_suc"}, 
							{"title":"Aula", "data": "aula.id_secc_ant"}, 
							{"title":"Aula Asistencia", "data": "aula.id_secc_ant"}, 
							{"title":"Grado", "data": "grad.nom"}, 
							{"title":"Nivel", "data": "nivel.nom"}, 
							{"title":"Acciones", "render": function ( data, type, row ) {
			                   return '<ul class="icons-list">'+
									'<li class="dropdown">'+
								'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
									'<i class="icon-menu9"></i>'+
									'</a>'+
								'<ul class="dropdown-menu dropdown-menu-right">'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matricula_editar(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
									'<li><a href="#" data-id="' + row.id + '" onclick="matricula_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Edici&oacute;n avanzada</a></li>'+
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

}*/

