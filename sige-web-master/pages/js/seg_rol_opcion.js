//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){

	$('#rol_nombre').html(params.rol);
	$('.tree-checkbox').fancytree({
        checkbox: true,
        selectMode: 3,
        source: {
            url: _URL + 'api/rolOpcion/menuTree/' + params.id
        },
      /*  init: function(event, data){
            $(".tree-checkbox").fancytree("getTree").expandAll();

        }*/
    });
	
	$('#btn-grabar').on('click', function(event){
		var nodes = $('.tree-checkbox').fancytree('getTree').getSelectedNodes();
		console.log(nodes);
		var param ={id:params.id};
		var arr = [];
		for(var i in nodes){
			if(nodes[i].children==null)//ES ULTIMO NIVEL
				arr.push(nodes[i].key);
		}
		param.opciones = arr;
		  _post_json('api/rolOpcion',param, function(data){
			  console.log(data);
		  });

	});
	/*
	_post($('#seg_rol_opcion-frm').attr('action') , $('#seg_rol_opcion-frm').serialize(),
			function(data){
					onSuccessSave(data) ;
				}
			);*/
	
}

