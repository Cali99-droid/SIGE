/** Se ejeuta al ser llamado desde otra pagina * */
function onloadPage() {

	//_onloadPage();
	$("#alumno").focus();
//	$('#frm-admi_busqueda_inscripcion').parsley().on('form:submit', function(formInstance) {
		
	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val());
		//id_anio, 	Integer id_niv, Integer id_suc
		var param={id_anio:$("#_id_anio").text(), id_niv:$('#id_niv').val(), id_suc:$('#id_suc').val()};
		_llenar_combo({
		   	url:'api/evaluacionVac/listarEvaluacionesLocalNivel',
		   	params: param,
			combo:$('#id_eva'),
			context:_URL,
			text:'evaluacion'
		});
	});
	
	$('#id_suc').on('change',function(event){
		_llenarCombo('ges_servicio',$('#id_niv'),null,$(this).val(),function(){
			$('#id_niv').change();
		});
	});
	
	var id_suc=$("#_id_suc").text();
	//if (id_suc!='0' ){
	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
			$('#id_suc').change();
		});	
	} else{
		_llenarCombo('ges_sucursal_sec',$('#id_suc'),null,id_suc,function(){
			$('#id_suc').change();
		});		
	}
	
	
	
}

function _onchangeAnio(id_anio, anio){
	var param={id_anio:$("#_id_anio").text(), id_niv:$('#id_niv').val(), id_suc:$('#id_suc').val()};
	_llenar_combo({
	   	url:'api/evaluacionVac/listarEvaluacionesLocalNivel',
	   	params: param,
		combo:$('#id_eva'),
		context:_URL,
		text:'evaluacion'
	});
}

$(function(){
	//_onloadPage();
	$('#frm-admi_busqueda_eva_psicologica').on('submit',function(event){
		event.preventDefault();
		var alumno=$("#alumno").val();
		// Integer id_eva,Integer id_grad, String ex_esc, String ex_psi, String vac
			var param ={id_eva:$('#id_eva').val(), id_grad:$('#id_gra').val()};			
			_GET({url:'api/matrVacante/listarAlumnosEvaPsi', 
					context:_URL,
					params: param,
					success:function(data){
						console.log(data);
					$('#admi_lista_alumnos_eva_psi-tabla').dataTable({
						 data : data,
						 aaSorting: [],
						 destroy: true,
						 pageLength: 50,
						 select: true,
				         columns : [ 
				           {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				           {"title":"ALUMNO", "data" : "alumno"},
				           {"title":"NIVEL", "data" : "nivel"},
				           {"title":"GRADO", "data" : "grado"},
				          /* {"title":"LOCAL", "data" : "local"},
				           {"title":"NIV-GRAD", "data" : "nivel_grado"},*/
				           {"title": "EVALUACIÓN", "render":function ( data, type, row,meta ) { 
			     	        	  /*if(row.ex_esc== 'NR'){
				     	        		 return "NO RINDIO"; 
				    	          } else {
				    	        		 return row.ex_esc ;
				    	          } */
				        	   if(row.id_ecn!=null)
				        		   return '<button onclick="ingresarEvaluacion(' + row.id_alu +','+row.id_matri+',\''+row.alumno+'\','+row.id_ecn+'\)" type="button" class="btn btn-success btn-xs"><i class="fa fa-cubes position-left"></i>Editar Evaluacion</button>&nbsp;'
				        	   else 
				        		   return '<button onclick="ingresarEvaluacion(' + row.id_alu +','+row.id_matri+',\''+row.alumno+'\','+row.id_ecn+'\)" type="button" class="btn btn-warning btn-xs"><i class="fa fa-cubes position-left"></i>Ingresar Evaluacion</button>&nbsp;'
			     	       }} //,
			     	    /*  {"title": "E. PSICOLOGICO", "render":function ( data, type, row,meta ) { 
		     	        	  if(row.examen_psico_res== 'Y'){
			     	        		 return "APTO"; 
			    	          } else if(row.examen_psico_res== 'N'){
			    	        		 return "NO APTO";
			    	          } else if(row.examen_psico_res== 'NR'){
			    	        		 return "NO RINDIO";
			    	          } 
			     	      }},
			     	     {"title": "CONDICION FINAL", "render":function ( data, type, row,meta ) { 
			     	    	var selected1 = (row.res_final=='N') ? 'selected': ' ';
			     	    	var selected2 = (row.res_final=='A') ? 'selected': ' ';
			     	    	var disabled_select = null;
			     	    	var disabled_btn=null;
			     	    	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1 ){
			     	    		/*$("#res").removeAttr("disabled");
			     	    		$("#btn_grabar_vac").removeAttr("disabled");*/
			     	    /*		disabled_select= 'enabled';
			     	    		disabled_btn = 'enabled';
			     	   	//	$("nota").style.display = "block";
			     	    	} else {

			     	    		disabled_select= 'disabled';
			     	    		disabled_btn = 'disabled';
			     	   	//	$("nota").style.display = "none";
			     	    	}
							return "<div class='row'><div class='col-sm-6'><input type='hidden' value="+row.id_mat+" id='id_mat' name='id_mat'><select id='res' name='res' class='form-control' "+disabled_select+"><option value='N'>Seleccione</option><option value='' "+selected1+">Sin Vacante</option><option value='A'"+selected2+">Obtiene Vacante</option></select></div><button class='btn btn-warning' id='btn_grabar_vac'  type='button' onclick='grabarPre(this)' "+disabled_btn+"><span class='fa fa-save' aria-hidden='true'></span></button><div>";            
			     	      }} //,
				         
				          /* {"title":"Acciones", "render": function ( data, type, row ) {
								return '<button onclick="inscripcionAlumno(' + row.id +','+row.id_gpf+',\''+row.alumno+'\')" type="button" class="btn btn-success btn-xs"><i class="icon-pencil3"></i>Inscripci&oacute;n</button>';
						   }} */
				        ]
				    });
					}
				});
	});

	
});

function ingresarEvaluacion(id_alu,id_matr_vac,alumno, id_ecn){
	_send('pages/admision/admi_evaluacion_psicologica.html','Evaluaci&oacute;n Psicol&oacute;gica del Alumno '+alumno+'','Registro',{id_alu:id_alu, id_matr_vac:id_matr_vac, id_ecn:id_ecn});
}


/*function grabarPre(btn){
	var divSeleccionado = btn.parentNode.parentNode.parentNode;
	//$(divSeleccionado).find("#id_pre").val(id_pre); 
	//alert($(divSeleccionado).html());
	//alert($(divPregunta).html());
	//alert($(divPregunta).attr("valor"));
	var res=$(divSeleccionado).find("#res").val();
	var id_mat=$(divSeleccionado).find("#id_mat").val();
	var param ={res:res, id_mat:id_mat};
	_GET({ url: 'api/matrVacante/GrabarCondFinal/',
	   context: _URL,
	   params:param,
	   success:
		function(data){
		   alert('Se grabo correctamente');
		}
	});
}*/

