//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){

	 $('#text-field-help').editable();
	    $('#text-field-help').on('shown', function(e, editable) {
	        $('<span class="help-block">Sirve para la direccion de referencia</span>').insertAfter(editable.input.$input);
	    });
	    
	    $('#editable-json-response').editable({
	        url: '/api/parametro/editable-json-response/',    
	        ajaxOptions: {
	            dataType: 'json'
	        },
	        success: function(response, newValue) {
	            if(!response) {
	                return "Unknown error!";
	            }          

	            if(response.success === false) {
	                return response.msg;
	            }
	        }
	    });
	    

});

