//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

}

//se ejecuta siempre despues de cargar el html
$(function(){
	_onloadPage();
	/*$('#frm-info_campus_virtual #btn_siguiente').on('click',function(event){
		//event.preventDefault();
		
		$(".modal-body").find('form').remove();
		ficha_modal();
		
		 
	});*/
	
	$('#frm-info_campus_virtual #btn_siguiente').on('click',function(event){
		//event.preventDefault();
		$(".modal-body").find('form').remove();
		ficha_validacion_datos();
		
		 
	});
});

