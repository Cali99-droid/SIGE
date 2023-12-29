var _roles;
function onloadPage(params){
	_onloadPage();
	var id_suc=$("#_id_suc").text();
	_roles = _usuario.usuariorols;
	var rol = _usuario.roles[0];	
	/*$('#id_gra').on('change',function(event){
 		_llenarCombo('col_aula',$('#id_au'),null,$("#_id_anio").text() + ',' + $(this).val());
	});
	
	$('#id_niv').on('change',function(event){
		_llenarCombo('cat_grad',$('#id_gra'),null,$(this).val(),function(){$("#id_gra").change()});
	
	});
	
	_llenarCombo('cat_nivel',$('#id_niv'),null,null,function(){
		$('#id_niv').change();
	});	
	_llenarCombo('ges_sucursal',$('#id_suc'));	*/
	
	$('#id_gra').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	
	
	$('#id_cic').on('change',function(event){
		var param={id_cic:$('#id_cic').val() ,id_grad:$('#id_gra').val()};
		_llenarComboURL('api/aula/listarAulasxCicloTurnoGrado', $('#id_au'),null, param, function() {$('#id_au').change();});
		//listar_aulas($('#id_cic').val(), $('#id_grad').val());
	});	

	$('#id_tpe').on('change',function(event){
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val(), id_tpe:$('#id_tpe').val(), id_suc:$('#id_suc').val()};
		_llenarComboURL('api/periodo/listarCiclosxAnio',$('#id_cic'),null,param,function(){$('#id_cic').change();});
	});
	$('#id_niv').on('change',function(event){
		var id_niv=$('#id_niv').val();
		_llenarCombo('cat_grad_todos',$('#id_gra'),null,id_niv,function(){$('#id_gra').change();});
		var param={id_anio:$("#_id_anio").text(), id_gir:$('#id_gir').val(), id_niv: $('#id_niv').val()};
		_llenarComboURL('api/periodo/listarTiposPeriodo',$('#id_tpe'),null,param,function(){$('#id_tpe').change();});
	});	
	

	
	$('#id_gir').on('change',function(event){
		if (rol=='1' || rol=='18'){
			var param={id_gir:$(this).val()};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		} else{
			var param={id_gir:$(this).val(),id_suc:_usuario.id_suc};
			_llenarComboURL('api/periodo/listarNivelesxGiroNegocio', $('#id_niv'),null, param, function() {$('#id_niv').change();});
		}	
		
		
	}); 
	
	

	if (_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 ){
		
		_llenarComboURL('api/trabajador/listarGirosNegocioxTrabajador/'+_usuario.id_tra+'/'+$('#_id_anio').text(),$('#id_gir'),null,null,function(){$('#id_gir').change();}); //
	} else {
		_llenarCombo('ges_giro_negocio',$('#id_gir'),$('#id_gir').val(),null, function(){	
		$('#id_gir').change();	
		});
	}

	if (_usuario.roles.indexOf(_ROL_ADMINISTRADOR)>-1){
		_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
			$('#id_suc').change();
		});		
		
	} else if(_usuario.roles.indexOf(_ROL_COORDINADOR_NIVEL)>-1 || _usuario.roles.indexOf(_ROL_ADMINISTRADOR_SEDE)>-1){
		_llenarComboURL('api/usuario/listarLocales/'+_usuario.id,$('#id_suc'),null,null,function(){$('#id_suc').change();});
	} else{
		if (id_suc!='0' ){
			var param = {
					id_tra : _usuario.id_tra,
					id_anio : $('#_id_anio').text()
				};
			
			_llenarComboURL('api/comportamiento/listarSucursalTutor', $('#id_suc'), null, param,
					function() {
						$('#id_suc').change();
					});
			
		} else{
			_llenarCombo('ges_sucursal',$('#id_suc'),null,null,function(){
				$('#id_suc').change();
			});				
		}
		
	}	
			
}

function existeRol(id_rol, arreglo){
	for(var i in arreglo){
		if (id_rol == arreglo[i].id_rol)
			return true;
	}
	
	return false;
}

var fncExito = function(data){//tu html no veo reporte_asiustencia.html
	$('#panel-asistencia-ranqueo').css("display","block");
	console.log(data);//lina luego lo pruebas con GET, por que el post asume q 
//	var cant_dias= fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val()
	$('#tabla-asistencia-ranqueo').dataTable({
		 data : data ,
		 aaSorting: [],
		 destroy: true,
		 pageLength: 50,
		 select: true,
         columns : [ 
        	       {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
				   {"title": "Alumno", "data" : "alumno"}, 
     	           {"title": "Nivel", "data" : "nivel","className": "text-center"}, 
    	           {"title": "Grado", "data" : "grado","className": "text-center"},
    	           {"title": "Seccion", "data" : "secc","className": "text-center"},
    	           {"title": "Cantidad", "data" : "cantidad", "className": "text-center"}           
        ],
        fnDrawCallback: function (oSettings) {
        	 var api = this.api(), data;
		        // Remove the formatting to get integer data for summation
		        var intVal = function ( i ) {
		            return typeof i === 'string' ?
		                i.replace(/[\$,]/g, '')*1 :
		                typeof i === 'number' ?
		                    i : 0;
		        };
		        	// Total over all pages
		            total = api
		                .column( 5)
		                .data()
		                .reduce( function (a, b) {
		                    return intVal(a) + intVal(b);
		                }, 0 );
		            console.log(total);

		            // Total over this page
		            pageTotal = api
		                .column( 5, { page: 'current'} )
		                .data()
		                .reduce( function (a, b) {
		                    return intVal(a) + intVal(b);
		                }, 0 );
		            console.log(pageTotal);

		            // Update footer
		            $( api.column( 5 ).footer() ).html(pageTotal);
        }
    });
	
};

$('#form_asistencia_ranqueo').on('submit',function(event){
	event.preventDefault();
	//alert($("#_id_suc").html());
	var param ={id_suc:$("#id_suc").val(),id_niv:$("#id_niv").val(),id_au:$("#id_au").val(), fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val(),asistencia:$("#asis").val(), id_anio:$('#_id_anio').text()};
	
	return _get('api/reporteAsistencia/ranqueoAsistencia',
	function(data){
		fncExito(data);//se le tiene q pasar el resultado del controller
	},param); //aqui
});




