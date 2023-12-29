//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_URL = window.location.origin.replace(':8080','')+ ":8081/sige-mat/";
	
		$.ajax({
				type: "GET",
				enctype: 'multipart/form-data',
				url: _URL+'api/archivo/listarSeminarios/',
				//data: data.result,
				processData: false, //prevent jQuery from automatically transforming the data into a query string
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {
					data=data.result;
					console.log(data);
			cant_conc=data.length;
		
				//clonar a la cantidad de olimpiadas
				for(i=0; i<cant_conc-1; i++){
					var last=$(".div_concurso").last();
					console.log(last.html());
					divConcurso = last.clone(false);
					
					$("#div_lista_olimpiadas").append(divConcurso);
				} 
				//asignar valores a la olimpoiada
				$(".div_concurso").each(function (index, value) {
					$(this).find("#id_oli").text(data[index].nom);
					$(this).find("#nombre").text(data[index].nom);
					$(this).find("#fecha").text('Fecha del seminario: '+_parseDate(data[index].fec));
					$(this).find("#link_mod_libre").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_delegacion").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_interno").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_interno").attr("data-nom_oli",data[index].nom);
					$(this).find("#link_bases").attr("data-id_oli",data[index].id);
					$(this).find("#col_org").text(data[index].col_org);
					//alert(data[index].result_flag);
					if(data[index].result_flag==1){
						//aqui deberia funcionar
						$(this).find('#li_result_flag').show();
						$(this).find('#li_result_flag').attr("data-id_oli",data[index].id);
					} else{
						$(this).find('#li_result_flag').hide();
					}
					
					if(data[index].vigente!='1'){
						$(this).find('.oli-ins').hide();
					}
					
				});
				
				//aqui
				$('.link_mod_libre').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					param.tip='L';
					_send('pages/olimpiada/oli_inscripcion_seminario.html','Inscripcion a Seminarios','Registro',param);
				});
						
				$('.link_mod_delegacion').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					//param.tip='D';
					_send('pages/olimpiada/oli_inscripcion_del.html','Inscripcion Delegaci&oacute;n','Registro',param);
				});
				
				$('.link_mod_interno').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var nom = $(this).attr("data-nom_oli");
					var param={};
					param.id=id_oli;
					param.nom=nom;
					param.tip='I';
					//_send('pages/olimpiada/oli_inscripcion_ind.html','Inscripcion Interno','Registro',param);
					_send('pages/olimpiada/oli_inscripcion_interno.html','Inscripcion Interno','Inscripción',param);
				});
				
				$('.li_result_flag').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
				});
				
				$('.link_bases').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					//_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
					var url="http://ae.edu.pe/concursos/bases_"+id_oli+".pdf";    
	                window.open(url, 'Bases');
					// _download(url,'bases.pdf');
				});

				},
			   
				error: function (e) {
					 $.unblockUI();
					alert(2);
				}
			});
	
	/*_GET({
		url:'api/archivo/listarSeminarios/',
		block:true,
	//	url:'api/seminario/listar',
		//context:_URL_SIGE,
		//param:{id_anio:$("#_id_anio").text()},
		success:function(data){
	//_get('api/archivo/listarSeminarios/',
	//		function(data){
			console.log(data);
			cant_conc=data.length;
		
				//clonar a la cantidad de olimpiadas
				for(i=0; i<cant_conc-1; i++){
					var last=$(".div_concurso").last();
					console.log(last.html());
					divConcurso = last.clone(false);
					
					$("#div_lista_olimpiadas").append(divConcurso);
				} 
				//asignar valores a la olimpoiada
				$(".div_concurso").each(function (index, value) {
					$(this).find("#id_oli").text(data[index].nom);
					$(this).find("#nombre").text(data[index].nom);
					$(this).find("#fecha").text('Fecha del seminario: '+_parseDate(data[index].fec));
					$(this).find("#link_mod_libre").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_delegacion").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_interno").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_interno").attr("data-nom_oli",data[index].nom);
					$(this).find("#link_bases").attr("data-id_oli",data[index].id);
					$(this).find("#col_org").text(data[index].col_org);
					//alert(data[index].result_flag);
					if(data[index].result_flag==1){
						//aqui deberia funcionar
						$(this).find('#li_result_flag').show();
						$(this).find('#li_result_flag').attr("data-id_oli",data[index].id);
					} else{
						$(this).find('#li_result_flag').hide();
					}
					
					if(data[index].vigente!='1'){
						$(this).find('.oli-ins').hide();
					}
					
				});
				
				//aqui
				$('.link_mod_libre').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					param.tip='L';
					_send('pages/olimpiada/oli_inscripcion_seminario.html','Inscripcion a Seminarios','Registro',param);
				});
						
				$('.link_mod_delegacion').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					//param.tip='D';
					_send('pages/olimpiada/oli_inscripcion_del.html','Inscripcion Delegaci&oacute;n','Registro',param);
				});
				
				$('.link_mod_interno').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var nom = $(this).attr("data-nom_oli");
					var param={};
					param.id=id_oli;
					param.nom=nom;
					param.tip='I';
					//_send('pages/olimpiada/oli_inscripcion_ind.html','Inscripcion Interno','Registro',param);
					_send('pages/olimpiada/oli_inscripcion_interno.html','Inscripcion Interno','Inscripción',param);
				});
				
				$('.li_result_flag').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var param={};
					param.id=id_oli;
					_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
				});
				
				$('.link_bases').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					//_send('pages/olimpiada/oli_resultados_tipo.html','Resultados','Elegir',param);
					var url="http://ae.edu.pe/concursos/bases_"+id_oli+".pdf";    
	                window.open(url, 'Bases');
					// _download(url,'bases.pdf');
				});
				//http://ae.edu.pe/concursos/bases inicial.pdf
		}
		});*/
	
		/*_get('api/matricula/seccion/' + id_au,
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
				   	       {"title": "C&oacute;digo", "data":"cod"}, 
				   	       {"title": "Alumno", "data":"Alumno"} 
				    ],
			        fnDrawCallback: function (oSettings) {
			        	$("<span><a href='#' target='_blank' title='IMPRIMR PDF CODIGO DE BARRAS' onclick='printGeneracionSeccion(event)'> <i class='icon-file-pdf'></i></a>&nbsp;</span>").insertBefore($("#_paginator"));
			        }
			    });
			}
	);*/

}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	
	

});

