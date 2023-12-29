var _id_parametro;
var _id_modulo;
function onloadPage(params){
	/*var id = params.id;
	var modulo = params.modulo;
	_id_modulo = id;
	$('#mod_parametro-tabla').data('id_padre',id);
	$('#mod_modulo_full-frm #nom').val(modulo);
	
	parametro_listar_tabla();

	_inputs('mod_modulo_full-frm');
	
	
	/*$('#mod_modulo_full-frm #btn-agregar').on('click',function(event){
		$('#mod_modulo_full-frm #id').val('');
		_post($('#mod_modulo_full-frm').attr('action') , $('#mod_modulo_full-frm').serialize(),
		function(data){
				onSuccessSave(data) ;
			}
		);
	});

	
		_get('api/modulo/' + id,
		function(data){
			_fillForm(data,$('#modal_full').find('form') );
			}
		);*/

}



/*function parametro_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/catalogos/mod_modulo_full.html');
	modulo_full_modal(link);

}



function modulo_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
//asignamos el id del padre
		
		//$('#mod_parametro-tabla').data('id_padre',link.data('id'));
		//$('#col_curso_anio-tabla').data('id_padre',link.data('id'));

		$('#mod_parametro-tabla').data('id_padre','1');
		

		parametro_listar_tabla();

		_inputs('mod_modulo_full-frm');
		
		
		$('#mod_modulo_full-frm #btn-agregar').on('click',function(event){
			$('#mod_modulo_full-frm #id').val('');
			_post($('#mod_modulo_full-frm').attr('action') , $('#mod_modulo_full-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
		});

		
			_get('api/curso/' + '1',
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		parametro_listar_tabla();
	}
	
	//Abrir el modal
	var titulo = 'Editar Parametro';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}




function parametro_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mod_parametro_full-frm');
		
		
		$('#mod_parametro_full-frm #btn-agregar').on('click',function(event){
			$('#mod_parametro_full-frm #id').val('');
			_post($('#mod_parametro_full-frm').attr('action') , $('#mod_parametro_full-frm').serialize(),
			function(data){
				
				}
			);
		});

		
		if (link.data('id')){
			_get('api/parametro/' + link.data('id'),
			function(data){
				_fillForm(data,$('#modal_full').find('form') );
				}
			);
		}else{
			$('#mod_parametro_full-frm #btn-grabar').hide();
		}
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		parametro_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar parametro a tratarse por curso';
	else
		titulo = 'Nuevo  parametro a tratarse por curso';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}*/

function parametro_editar(row,e){
	e.preventDefault();
	var link = $(row);
	link.attr('href','pages/academico/mod_parametro_modal.html');
	parametro_modal(link);
}

function parametro_nuevo(row,e){
	e.preventDefault();	
	var link = $(row);
	link.attr('href','pages/academico/mod_parametro_modal.html');
	link.data('id_cur',_id_curso);
	parametro_modal(link);
}

function parametro_eliminar(link){
	_delete('api/parametro/' + $(link).data("id"),
			function(){
				parametro_listar_tabla();
				}
			);
}


function parametro_modal(link){
	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){

		_inputs('mod_parametro-frm');
		$('#mod_parametro-frm #btn-agregar').on('click',function(event){
			$('#mod_parametro-frm #id').val('');
			$('#mod_parametro-frm #id_mod').attr('disabled',false);
			_post($('#mod_parametro-frm').attr('action') , $('#mod_parametro-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);
			$('#mod_parametro-frm #id_mod').attr('disabled',true);
		});
		
		if (link.data('id')){
			
			_get('api/parametro/' + link.data('id'),
			function(data){
				console.log(data);
				_fillForm(data,$('#modal').find('form') );
					
				}
			);
		}else{
			$('#mod_parametro-frm #id_mod').attr('disabled',true); 
			$('#mod_parametro-frm #btn-grabar').hide();}
		
		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		parametro_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar parametro a tratarse por curso';
	else
		titulo = 'Nuevo  parametro a tratarse por curso';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}


function parametro_listar_tabla(){

	var param = {id_mod:$('#mod_parametro-tabla').data('id_padre')};
	console.log(param);
	_get('api/parametro/listarParametros/',
			function(data){
			console.log(data);
				$('#mod_parametro-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
			        	 	{"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
							{"title":"Parametro", "data" : "des"}, 
							{"title":"Valores", "render": function ( data, type, row ) {
								if(row.tipo_html=='valor'){
									return "<div class='parametro'><input type='hidden' id='id_par' name='id_par' value='"+row.id+"' /></input><input type='text' id='val' name='val' value='"+row.val+"' class='form-control input-sm'/></div>";
								}else if(row.tipo_html=='flag'){
									var checked = (row.val!=0) ? ' checked ': ' ';									
										return "<div class='parametro'><input type='hidden' id='id_par' name='id_par' value='"+row.id+"' /><input type='checkbox'"+checked+" id='val' name='val' value='"+row.val+"' class='form-control-styled input-sm' onclick='seleccionarCheck(this)'/></div>";
									
								}else if(row.tipo_html=='fecha'){
									return "<div class='parametro'><input type='hidden' id='id_par' name='id_par' value='"+row.id+"' /><input type='text' id='val' name='val' value='"+row.val+"' class='form-control input-sm daterange-single' /></div>";
								}
							}}
				    ],
					"initComplete": function( settings ) {
					   _agregarIcono(settings, 'Agregar parametro', 'parametro_nuevo(this,event)','icon-file-plus');
					   _initCompleteDT(settings);
				    }
			    });
			},param
	);

}

function seleccionarCheck(campo1){
	var campo =$(campo1);
	var id = campo.val();
	console.log(id);
	if(campo.is(':checked')){
		campo.val('1');
	}else{
		campo.val('0');
	}
}

$('#btn-grabar').on('click',function(){
	console.log($('#mod_modulo_full-frm'));
	var json_modulo={};
	var parametros=[];
	
	$(".parametro").each(function (index, value) {
		var parametro={};
		parametro.id_par=$(this).find("#id_par").val()
		parametro.val=$(this).find("#val").val();
		parametros.push(parametro);
	});
	json_modulo.parametroReq=parametros;	
	
/*	_POST({form:$('#mod_modulo_full-frm'),
		  context:_URL,
		  msg_type:'notification',
		  success:function(data){
			  parametro_listar_tabla();
		  }
	});
	*/
	_POST({
		context:_URL,
		url:'api/parametro',
		params:JSON.stringify(json_modulo),
		contentType:"application/json",
		success: function(data){
			parametro_listar_tabla();
		}
	});
});
