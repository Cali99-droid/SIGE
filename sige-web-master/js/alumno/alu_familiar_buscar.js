//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
	$("#alu_familiar-frm #nro_doc").focus();
	_llenarCombo('cat_tipo_documento',$('#id_tdc'), '1',null, function(){
		$('#id_tdc').change();
	});//POR DEFECTO DNI
	
	$('#type').val('1');// POR DEFECTO ES BUSQUEDA POR NRO DE DNI
	
	$("#alu_familiar-frm #id_tdc").on('change', function(){
		if($(this).val()==1){
			 $("#alu_familiar-frm #nro_doc").attr('maxlength','8');
		}else if($(this).val()==2){
			 $("#alu_familiar-frm #nro_doc").attr('maxlength','11');
		}
	});
	
	
	$('#li-tabs-1').on('click',function(){
		$('#type').val('1');
		$('#id_tpd').prop('required',true);
		$('#nro_doc').prop('required',true);
		$('#apellidosNombres').prop('required',false);
	});
	$('#li-tabs-2').on('click',function(){
		$('#type').val('2');
		$('#id_tpd').prop('required',false);
		$('#nro_doc').prop('required',false);
		$('#apellidosNombres').prop('required',true);		
	});
	
	$('#alu_familiar-frm').on('submit',function(event){
		event.preventDefault();
		
		_get('api/familiar/FamiliarBuscarResultados',function(data){
				console.log(data);
				$('#alu_familiar-tabla').dataTable({
					 data : data.gruFamList,
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
			        	 	{"title":"Codigo Familiar", "data": "cod"},
			        	 	{"title":"Alumno", "data": "gruFamAlumnos","render": function ( data, type, row ) {
			        	 		console.log(data);
			        	 		var str = "";
			        	 		if (data!=null){
				        	 		
				        	 		for(var i in data)
				        	 		{
				        	 		     var alumno = data[i].alumno;
				        	 		    str =  str + "<li>" + alumno.ape_pat +  " "  + alumno.ape_mat +  ", "  + alumno.nom +  "</li>";
				        	 		}
				        	 		return str;
			        	 		}else
			        	 			return "";
			        	 	}},
							{"title":"Editar", "render": function ( data, type, row ) {
								return '<button onclick="editarFamiliar(' + row.id +',' + row.id_fam +')" type="button" class="btn bg-teal btn-ladda  ladda-button"><i class="glyphicon glyphicon-edit"></i></button>';}
							}
	    	        ],
	    	        "initComplete": function( settings ) {

	    	        	if (data.gruFamList.length==0){
	    	        		
	    	        		$('#alu_familiar-panel').css('display','none');
	    	        		$('#alu_no_existe').css('display','block');
	    	        		$('#alu_evaluacion-panel').css('display','block');
	    	        		
	    	        		//eva_vac.id eva_vac_id, eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , 
	    	        		//eva_vac.precio eva_vac_precio,eva_vac.ptje_apro eva_vac_ptje_apro  ,
	    	        		//eva_vac.est eva_vac_est, eva_vac.fec_ini eva_vac_fec_ini, eva_vac.fec_fin eva_vac_fec_fin , 
	    	        		//pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  , ani.nom ani_nom, srv.nom srv_nom, suc.nom suc_nom from eva_evaluacion_vac eva_vac left join per_periodo pee on pee.id = eva_vac.id_per  left join col_anio ani on pee.id_anio = ani.id  left join ges_servicio srv on srv.id = pee.id_srv  left join ges_sucursal suc on suc.id = srv.id_suc   where  eva_vac.fec_ini<='" + fecha + "' and  eva_vac.fec_fin>='" + fecha + "' order by suc.nom desc, srv.nom asc";


	    					$('#alu_evaluacion-tabla').dataTable({
	    						 data : data.gruFamList,
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
	    				        	 	{"title":"Evaluaci&oacute;n", "data": "eva_vac_des"},
	    				        	 	{"title":"Nivel", "data": "srv_nom"},
	    				          	 	{"title":"Local", "data": "suc_nom"},
	    				          	 	{"title":"Desde", "data": "eva_vac_fec_ini"},
	    				          	 	{"title":"Hasta", "data": "eva_vac_fec_fin"}
	    		    				        	 	 
	    		    	        ] 
	    					});	    	        		
	    	        		
						}else{
	    	        		$('#alu_familiar-panel').css('display','block');
	    	        		$('#alu_no_existe').css('display','none');
	    	        		$('#alu_evaluacion-panel').css('display','block');
						}
	    			 }
			});
		}, $(this).serialize() );
	});
}


function editarFamiliar(id_gpf, id_fam){
	var params ={id_gpf:id_gpf, id_fam, id_fam:id_fam};
	
	_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Edic&iacute;on de familias", params);
}

function crearFamilia(){
	//_send("FamiliarNuevo?dni=" + $("#dni").val());
	var params ={dni:$("#nro_doc").val()};
	_send("pages/alumno/alu_familiar_tabs.html", "Familias", "Creaci;on de la familia", params);
}

