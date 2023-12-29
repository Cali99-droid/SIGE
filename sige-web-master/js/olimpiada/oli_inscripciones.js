//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_GET({
		context:_URL_OLI,
		url:'api/public/config/listarConcursos',
		success:function(data){
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
					$(this).find("#fecha").text('Fecha del concurso: '+_parseDate(data[index].fec));
					$(this).find("#link_mod_libre").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_libre").attr("data-id_anio",data[index].id_anio);
					$(this).find("#link_mod_delegacion").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_delegacion").attr("data-id_anio",data[index].id_anio);
					$(this).find("#link_mod_interno").attr("data-id_oli",data[index].id);
					$(this).find("#link_mod_interno").attr("data-nom_oli",data[index].nom);
					$(this).find("#link_mod_interno").attr("data-id_anio",data[index].id_anio);
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
					var id_anio= $(this).attr("data-id_anio");
					var param={};
					param.id=id_oli;
					param.id_anio=id_anio;
					param.tip='L';
					_send('pages/olimpiada/oli_inscripcion_ind.html','Inscripcion Libre','Registro',param);
				});
						
				$('.link_mod_delegacion').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var id_anio= $(this).attr("data-id_anio");
					var param={};
					param.id=id_oli;
					param.id_anio=id_anio;
					//param.tip='D';
					_send('pages/olimpiada/oli_inscripcion_del.html','Inscripcion Delegaci&oacute;n','Registro',param);
				});
				
				$('.link_mod_interno').on('click',function(event){
					var id_oli= $(this).attr("data-id_oli");
					var nom = $(this).attr("data-nom_oli");
					var id_anio= $(this).attr("data-id_anio");
					var param={};
					param.id=id_oli;
					param.nom=nom;
					param.id_anio=id_anio;
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
	});
}
//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	
	
	

});

