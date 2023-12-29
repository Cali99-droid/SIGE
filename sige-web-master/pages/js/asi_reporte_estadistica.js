var _roles;
function onloadPage(params){
	_onloadPage();
	var id_suc=$("#_id_suc").text();
	_roles = _usuario.usuariorols;
	var rol = _usuario.roles[0];	
	//_llenarCombo('ges_sucursal',$('#id_suc'));	
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
	//_llenarCombo('cat_nivel',$('#id_niv'));
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
}

function existeRol(id_rol, arreglo){
	for(var i in arreglo){
		if (id_rol == arreglo[i].id_rol)
			return true;
	}
	
	return false;
}

var fncExito = function(data){//tu html no veo reporte_asiustencia.html
	$('#panel-asistencia').css("display","block");
	console.log(data);//lina luego lo pruebas con GET, por que el post asume q 
//	var cant_dias= fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val()
	$('#tabla-estadistica-asistencia').dataTable({
		 data : data ,
		 aaSorting: [],
		 destroy: true,
		 pageLength: 50,
		 select: true,
         columns : [ //punt.sucursal, punt.nivel, punt.nom grado, punt.secc,punt.id_gra, cap.total_capacidad, IFNULL(punt.puntuales,0) puntuales, IFNULL(tard.tardanza,0) tardanza, IFNULL(falt.faltas,0) faltas, IFNULL(sint.sin_tarjeta,0) sin_tarjeta
        	       {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
     	           {"title": "Local", "data" : "sucursal"}, 
     	           {"title": "Nivel", "data" : "nivel"}, 
    	           {"title": "Grado", "data" : "grado"},
    	           {"title": "Seccion", "data" : "secc"},
    	          //title": "Fecha", "data" : "fecha"},
    	           {"title": "Total Alumnos", "data" : "total_capacidad"},
    	           {"title": "Puntuales (%)", "render":function ( data, type, row,meta ) { 
    	        	   var punt=parseInt(row.puntuales);
    	        	   var cant=parseInt(row.total_capacidad);
    	        	   var punt_est=(punt/cant)*100;
    	        	   return _formatMonto(punt_est);
    	        	   	
    	           }},
    	           {"title": "Tardanzas (%)", "render":function ( data, type, row,meta ) { 
    	        	   var tard=parseInt(row.tardanza);
    	        	   var cant=parseInt(row.total_capacidad);
    	        	   var tard_est=(tard/cant)*100;
    	        	   return _formatMonto(tard_est);
    	        	   	
    	           }},
    	           {"title": "Faltas (%)", "render":function ( data, type, row,meta ) { 
    	        	   var falt=parseInt(row.faltas);
    	        	   var cant=parseInt(row.total_capacidad);
    	        	   var falt_est=(falt/cant)*100;
    	        	   return _formatMonto(falt_est);
    	        	   	
    	           }},
    	           {"title": "Sin Tarjeta (%)", "render":function ( data, type, row,meta ) { 
    	        	   var sin_tar=parseInt(row.sin_tarjeta);
    	        	   var cant=parseInt(row.total_capacidad);
    	        	   var sint_est=(sin_tar/cant)*100;
    	        	   return _formatMonto(sint_est);
    	        	   	
    	           }}	           
        ],
       /* "footerCallback": function ( row, data, start, end, display ) {
            var api = this.api(), data;
            // Remove the formatting to get integer data for summation
            var intVal = function ( i ) {
                return typeof i === 'string' ?
                    i.replace(/[\$,]/g, '')*1 :
                    typeof i === 'number' ?
                        i : 0;
            };
            
            var i;
            for(i=5; i<=10; i++){
            	   // Total over all pages
                total = api
                    .column( i)
                    .data()
                    .reduce( function (a, b) {
                    	//alert(intVal(a));
                        return Math.round(a) + Math.round(b);
                    }, 0 );
                console.log(total);

                // Total over this page
                pageTotal = api
                    .column( i, { page: 'current'} )
                    .data()
                    .reduce( function (a, b) {
                    	//alert(intVal(a));
                        return Math.round(a) + Math.round(b);
                    }, 0 );
                console.log(pageTotal);

                // Update footer
                $( api.column( i ).footer() ).html(       pageTotal+"%"       );
            }

         
        },*/
    });
	
};

$('#form_asistencia_estadistica').on('submit',function(event){
	event.preventDefault();
	//alert($("#_id_suc").html());
	var param ={id_niv:$("#id_niv").val(), fec_ini:$("#fec_ini").val(), fec_fin:$("#fec_fin").val(), id_suc:$("#id_suc").val(), id_anio:$('#_id_anio').text(), id_gir:$("#id_gir").val()};
	
	return _get('api/reporteAsistencia/reporteEstadisticaAsistencia',
	function(data){
		fncExito(data);//se le tiene q pasar el resultado del controller
	},param); //aqui
});




