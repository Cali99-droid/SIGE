//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params) {
	_onloadPage();
	// lista tabla

	$('#_a_titulo').on( "click", function(){
		_send('pages/apoderado/apo_hijos.html','Mis hijos','Datos principales');
	} );
	
	
	_get('api/trabajador/profesoresxMat/' + params.id_mat, function(data) {


		var lista = $('#profesores-list');
		var linea = lista.html();
		for ( var i=0; i<data.length-1;i++){
			lista.append(linea);
		}
		
		var i=0;
		lista.children().each(function () {
			var trabajador = data[i];
			console.log(trabajador);
			$(this).find('#trabajador').html(trabajador.trabajador);
			$(this).find('#curso').html(trabajador.curso);
			$(this).find('#foto').attr('src',_URL + 'api/trabajador/foto/' + trabajador.tra_id);
				
			i++;
		});
		
		
	});

}
