//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();

	//list de alumnos
	/*var param = {};
	param.id_fam = _usuario.id;
	param.id_anio= $('#_id_anio').text();*/
	var param={id_usr:_usuario.id};
	_get('api/familiar/listarHijosFamilia',
			function(data){
			
			//console.log(data);
			var lista = $('#alumnos-list');
			var linea = lista.html();
			console.log(linea);
			for ( var i=0; i<data.length-1;i++){
				console.log(data[i]);
				var alumno = data[i].alumno;
				//alert(alumno);
				lista.children().eq(i).find('nom').text(alumno);
				console.log(data[i].cod);
				lista.children().eq(i).find('id_alu').val(data[i].cod);
				
				lista.append(linea);
			}
			
			var i=0;
			lista.children().each(function () {
				var id_alu = data[i].id_alu;
				var alu =data[i];
				console.log($(this).html());
				$(this).find('#nom').html(alu.alumno);
				$(this).find('#id_alu').val(alu.id_alu);
				//_init();
				//$(this).find('#foto').attr('src',_URL + 'api/alumno/foto/' + alu.id_alu);
				$(this).find('#tabla').attr('id','tabla' + id_alu);
				var param={id_alu:id_alu, id_anio:$('#_id_anio').text()};
		_GET({ url: 'api/matricula/listarMatriculasCicloxAlumno/',
			context: _URL,
		   params:param,
		   success:
			function(data){
			//   _cant_padres=data.length;
			console.log(data);
				$('#tabla'+ id_alu).html('');
				$('#tabla' + id_alu).dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 orderCellsTop:true,
					 searching: false, 
					 paging: false, 
					 info: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							//{"title":"Alumno", "data" : "alumno"},
							{"title":"Giro de Negocio", "data" : "giro"},
							{"title":"Ciclo", "data" : "ciclo"}, 
							//{"title":"Turno", "data" : "turno"}, 
							{"title":"Nivel", "data" : "nivel"}, 
							{"title":"Aula", "data" : "grado"}, 
							//{"title":"Sección", "data" : "secc"}, 
							{"title":"Estado", "data" : "situacion"}, 
							{"title": "Fec. Vencimiento", "render":function ( data, type, row,meta ) {
								if(row.fec_venc!=null){
									return _parseDate(row.fec_venc);
								} else{
									return null;
								}	
							}},	
							{"title":"Acciones", "render": function ( data, type, row ) {
								//return '<button onclick="editarOtroFamiliar(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3 position-left"></i> Editar</button>&nbsp;'
								//return '<button onclick="eliminarMatricula(' + row.id_gpf +',' + row.id+')" type="button" class="btn btn-danger btn-xs"><i class="icon-trash-alt position-left"></i> Eliminar</button>';
							//if(row.id_au_asi!=null)
							if(row.canc==1)
								return null;
							else
								return '<a href="#" data-id="' + row.id + '" onclick="matricula_eliminar(this,event)" class="list-icons-item"><i class="fa fa-trash-o ui-blue ui-size"></i></a>'
							}}
				    ],
				    "initComplete": function( settings ) {
						   _initCompleteDTSB(settings);
						  
					 }
			    });
			}
		});				
				i++;
			});
			
			
		},param);

	//lista tabla
	//alumno_listar_tabla();
});

 function printGeneracionSeccion(obj,event){
								//alert();
								event.preventDefault();
								var div_alu=$(obj).parent();
								console.log(div_alu.html());
								var id_alu= div_alu.find("#id_alu").val();
								//alert(id_alu);
								//var id_alu = $(obj).data('id_mat');	
								document.location.href = _URL + "api/archivo/generarCarnet/" + id_alu;
								// window.location.href = _URL + "api/archivo/seccion/" + $("#id_au").val();
								 //var url= _URL + "api/archivo/seccion/" + $("#id_au").val();
								// console.log(url);
							  // location.href= url;

							// window.open(url, 'Download');
							}

function matricula_eliminar(row,e){
	e.preventDefault();
	var link = $(row);
	var id=link.data('id');
	_delete('api/matricula/eliminarMatriculaAcadVac/' + $(link).data("id"),
			function(){
				_send('pages/matricula/mat_servicios_familia.html','Lista de Matrículas','Lista');
				}
			);
	
}

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



