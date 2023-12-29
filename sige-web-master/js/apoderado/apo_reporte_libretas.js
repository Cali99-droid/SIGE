//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	//list de alumnos
	var param = {};
	param.id_fam = _usuario.id;
	param.id_anio= $('#_id_anio').text();
	_get('api/familiar/hijos/',
			function(data){
			//console.log(data);
			var lista = $('#alumnos-list');
			var linea = lista.html();
			for ( var i=0; i<data.length-1;i++){
				var alumno = data[i];
				
				lista.children().eq(i).find('nom').html(alumno.nom);
				
				lista.append(linea);
			}
			
			var i=0;
			lista.children().each(function () {
				var id_mat = data[i].id_mat;
				var alumno =data[i];
				$(this).find('#nom').html(alumno.nom);
				$(this).find('#sucursal').html(alumno.sucursal);
				$(this).find('#nivel').html(alumno.nivel);
				$(this).find('#grado').html(alumno.grado);
				$(this).find('#seccion').html(alumno.seccion);
				$(this).find('#foto').attr('src',_URL + 'api/alumno/foto/' + alumno.id);
				$(this).find('#tabla').attr('id','tabla' + id_mat);
				
				_get('api/seguimientoDoc/entregaLibretas?id_mat=' + id_mat,
						function(data1){
								$('#tabla' + id_mat).dataTable({
									 data : data1,
									 aaSorting: [],
									 destroy: true,
									 orderCellsTop:true,
									 select: true,
									 bLengthChange: false,
									 bPaginate: false,
									 bFilter: false,
								     bInfo : false,
							         columns : [ 
						        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
						        	 	{"title": "Periodo", "render":function ( data, type, row,meta ) {
						        	 		var nro = row.nro;
						        	 		var des = nro + ' ' + row.periodo;
						        	 		if (nro=='1') 
						        	 			des = '1er ' + row.periodo; 
						        	 		if (nro=='2') 
						        	 			des = '2do ' + row.periodo; 
						        	 		if (nro=='3') 
						        	 			des = '3ro ' + row.periodo; 
						        	 		if (nro=='4') 
						        	 			des = '4to' + row.periodo; 
						        	 			return des;} 
						        	 	},
						        	 	{"title":"Familiar", "data": "familiar"},
						        	 	{"title": "Libreta", "render":function ( data, type, row,meta ) {
						        	 		return "<a href='#' onclick='descargarLibreta(" + row.id + ")'><i class='icon-file-pdf text-success' ></i></a>"; 
						        	 	}},
						        	 	{"title":"Dia", "data": "dia"},
						        	 	{"title":"Hora", "data": "hora"}
								    ]
							    });
					});
				
				i++;
			});
			
			
		},param);

	
	//lista tabla
	//alumno_listar_tabla();
});

function alumno_eliminar(link){
	_delete('api/alumno/' + $(link).data("id"),
			function(){
					alumno_listar_tabla();
				}
			);
}

function descargarLibreta(id){
	window.open(_URL + 'api/seguimientoDoc/reimprimir/' + id);
}
function alumno_editar(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/$table.module.directory/alu_alumno_modal.html');
	alumno_modal(link);
	
}

function alumno_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		
		_inputs('alu_alumno-frm');
		
		$('#alu_alumno-frm #btn-agregar').on('click',function(event){
			$('#alu_alumno-frm #id').val('');
			_post($('#alu_alumno-frm').attr('action') , $('#alu_alumno-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});
		
		if (link.data('id')){
			_get('api/alumno/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal').find('form') );
				_llenarCombo2('cat_tipo_documento',$('#id_tdc'), data.id_tdc);
				_llenarCombo2('cat_idioma',$('#id_idio'), data.id_idio);
				_llenarCombo2('cat_idioma',$('#id_idio'), data.id_idio);
				_llenarCombo2('cat_est_civil',$('#id_eci'), data.id_eci);
			});
		}else{
			_llenarCombo2('cat_tipo_documento',$('#id_tdc'));
			_llenarCombo2('cat_idioma',$('#id_idio'));
			_llenarCombo2('cat_idioma',$('#id_idio'));
			_llenarCombo2('cat_est_civil',$('#id_eci'));
			$('#alu_alumno-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Alumno';
	else
		titulo = 'Nuevo  Alumno';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function alumno_listar_tabla(){
	
	var param = {};
	param.id_fam = _usuario.id;
	param.id_anio= $('#_id_anio').text();
	_get('api/familiar/hijos/',
			function(data){
			console.log(data);
				$('#alu_alumno-tabla').DataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 bInfo: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Numero de documento", "data" : "nro_doc"}, 
							{"title":"Nombres", "data" : "nom", "render":function ( data, type, row,meta ) { 
							
			                   return '<ul class="icons-list">'+
											'<li class="dropdown">'+
										'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
											data+
											'</a>'+
										'<ul class="dropdown-menu dropdown-menu-right">'+
										'<li><a href="#" data-id="' + row.id_mat + '" onclick="seleccionarAlumno(this, event)"><i class="fa fa-pencil-square-o"></i> Profesores</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Notas</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Asistencia</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Agenda</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Justificaciones</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Reclamos</a></li>'+
										'<li><a href="#" data-id="' + row.id_mat + '"><i class="fa fa-pencil-square-o"></i> Datos del alumno</a></li>'+
										'</ul>'+
					                   '</li>'+
					                   '</ul>';
								
								//return "<a href='#' onclick='seleccionarAlumno(" + row.id_mat + ")'>" + data + "</a>"; }
								}
							}, 
							{"title":"Local", "data" : "sucursal"}, 
							{"title":"Nivel", "data" : "nivel"}, 
							{"title":"Grado", "data" : "grado"}, 
							{"title":"Sección", "data": "seccion"}, 


				    ],
				    select: {
				    	 select: true
			        },
				    "initComplete": function( settings ) {
						   _initCompleteDT(settings);
						   
					 }
			    });
			},param
	);

}

function profesores(field){
	var param={id_mat:$(field).data('id_mat')};
	_send('pages/apoderado/apo_profesores.html','Mis Hijos','Lista de Profesores',param);
}

function notasPromedio(field){

	_send('pages/apoderado/apo_notas_promedio.html','Mis Hijos','Lista de notas',$(field).data('alumno'));
}

