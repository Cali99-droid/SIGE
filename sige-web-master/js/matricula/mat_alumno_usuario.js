//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	

	_llenarCombo('cat_nivel',$('#id_niv'));

	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(), function(){$('#id_gra').change()});
	});

	$('#id_gra').on('change',function(event){
		_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
	});
	
	$('#frm-seccion-buscar').on('submit',function(event){
		event.preventDefault();
		alumno_listar_tabla($('#id_suc').val());
	});

	
});


function alumno_listar_tabla(id_suc){
	$('#id_anio').val($('#_id_anio').text());
	_get('api/matricula/reporteMatriculados',
			function(data){
			console.log(data);
				$('#alumno-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 pageLength: 50,
					 //bLengthChange: false,
					 //bPaginate: false,
					 //bFilter: false,
					 select: true,
			         columns : [ 
				   	       {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				   	       {"title": "Alumno", "data":"alumno"},
				   	       {"title": "Usuario", "data":"usuario"},
				   	       //{"title": "Apoderado", "render": function ( data, type, row ){return row.ape_pat + ' ' + row.ape_mat + ', ' + row.nom;}},
				   	       {"title": "Password", "data":"psw"}, 
					   	   {"title": "Acciones", "data" : "id","render":function ( data, type, row,meta ) { 
			     	        	   return '<div class="list-icons"> <a href="#" data-id="' + row.id_alu + '" title="Editar Contraseña" onclick="editar_pasword(this, event)" class="list-icons-item"><i class="fa fa-edit ui-blue ui-size" aria-hidden="true"></i></a> </div>';
			     	         }},
				    ],
			        fnDrawCallback: function (oSettings) {
			        	$("<span><a href='#' target='_blank' title='IMPRIMR PDF' onclick='printGeneracionSeccion(event)'> <i class='icon-file-pdf'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			        }
			    });
			}, $('#frm-seccion-buscar').serialize()
	);

}

function editar_pasword(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/matricula/mat_alumno_usr_modal.html');
	alumno_usuario_modal(link);
	
}

function alumno_usuario_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		$('.daterange-single').daterangepicker({ 
	        singleDatePicker: true,
	        locale: { format: 'DD/MM/YYYY'},
	    });
		
		_inputs('mat_alumno_usr_modal-frm');
		
		$('#mat_alumno_usr_modal-frm #btn-grabar').on('click',function(event){
			
			var id_anio=$('#_id_anio').text();
			$('#id_anio').val(id_anio);
			_POST({url:'api/alumno/actualizarContrasenia',
				   params:{id_alu:$("#id_alu").val(),pass_educando:$("#pass_educando").val()},
				   silent:false,
				   success:function(data){
					   onSuccessSave(data) ;
				   }
			});
		});
		
		if (link.data('id')){
			_get('api/alumno/' + link.data('id'),
			function(data){
				console.log(data);
				$('#lbl_alumno').text(data.ape_pat+" "+data.ape_mat+" "+data.nom);
				$('#id_alu').val(data.id);
				$('#lbl_usuario').text(data.usuario);
				$('#pass_educando').val(data.pass_educando);
				per_uni_det_listar_tabla();
			});
			
		}		
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		alumno_listar_tabla($('#id_suc').val());
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Contraseña';
	
	_modal(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function printGeneracionSeccion(event){
	event.preventDefault();
	document.location.href = _URL + "api/familiar/exportarClaves/" + $("#id_au").val();
}