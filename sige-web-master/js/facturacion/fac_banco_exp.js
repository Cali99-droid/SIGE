//Se ejecuta cuando la pagina q lo llama le envia parametros
function onloadPage(params){
	_onloadPage();
}
//se ejecuta siempre despues de cargar el html
$(function(){
	$('#btn-continuar').on('click', function(e) {
		e.preventDefault();
		if ($this.val() ==1){//banbif
			_send('pages/tesoreria/fac_banco_banbif.html','Genaraci&oacute;n de archivos','Banco Financiero');
		}else
			_send('pages/tesoreria/fac_banco_exp.html','Genaraci&oacute;n de archivos','Banco Financiero');
	});
	
	//lista bancos
	
	_get('api/banco/listar/',
			function(data){
			console.log(data);
				$('#fac_banco-tabla').dataTable({
					 data : data,
					 aaSorting: [],
					 destroy: true,
					 bLengthChange: false,
					 bPaginate: false,
					 bFilter: false,
					 bInfo : false,
					 select: true,
			         columns : [ 
			               {"title": "Nro", "render":function ( data, type, row,meta ) { return parseInt(meta.row)+1;} },
			               {"title":"Nombre", "data" : "nom"}, 
			               {"title":"C&oacute;digo", "data" : "cod"}, 
			               {"title":"Moneda", "data" : "moneda"},
						   {"title":"Años Anteriores", "render": function ( data, type, row ) {
				        		   return "<label class='checkbox-inline'><input type='checkbox' id='marzo' name='marzo' value='3'/></label>";
						   }	   
			        	   },
				           {"title":"Meses", "render": function ( data, type, row ) {
				        		   return "<label class='checkbox-inline'><input type='checkbox' id='marzo' name='mes' value='3'>Marzo</input></label><label class='checkbox-inline'><input type='checkbox' id='abril' name='mes' value='4'>Abril</input></label><label class='checkbox-inline'><input type='checkbox' id='mayo' name='mes' value='5'>Mayo</input></label><label class='checkbox-inline'><input type='checkbox' id='junio' name='mes' value='6'>Junio</input></label><label class='checkbox-inline'><input type='checkbox' id='julio' name='mes' value='7'>Julio</input></label><label class='checkbox-inline'><input type='checkbox' id='agosto' name='mes' value='8'>Agosto</input></label><label class='checkbox-inline'><input type='checkbox' id='setiembre' name='mes' value='9'>Septiembre</input></label><label class='checkbox-inline'><input type='checkbox' id='octubre' name='mes' value='10'>Octubre</input></label><label class='checkbox-inline'><input type='checkbox' id='noviembre' name='mes' value='11'>Noviembre</input></label><label class='checkbox-inline'><input type='checkbox' id='diciembre' name='mes' value='12'>Diciembre</input></label>";
			                   }
			        	   },
						   {"title":"Generar", "render": function ( data, type, row ) {
				        		   //return '<button type="button" onclick="generarArchivo(event,' + row.id + ',\'' + row.nom + '\')" class="btn btn-success">Generar archivo <i class="icon-calculator3"></i></button>';
								   return '<button type="button" onclick="generarArchivo(this, ' + row.id + ',\'' + row.nom + '\')" class="btn btn-success">Generar archivo <i class="icon-calculator3"></i></button>';
			                   }
			        	   }
						   //<input type='checkbox' id='id_bec_sel" + row.id +"' name='id_bec_sel' value="+row.id+" onclick='seleccionarMensualidad(this)'/>
				    ]
			    });
			}
	);

 

});


function generarArchivo(obj,id_banco, nom){
//function generarArchivo(e,id_banco, nom){
	//e.preventDefault();
 var codigo=obj.parentNode.parentNode;
// var codigo=$(this).find('td:nth-child(6)').html();
 var meses=[]; 
console.log(codigo);
    $(codigo).find('input[type="checkbox"]:checked').each(function (i,k) {

         var item =$(this).val();       

         //Read the Id of the selected checkbox item.
		 console.log(item);
         meses.push(item);

     }); 
     console.log(meses);
	 console.log(id_banco);
	 console.log(nom);
	if (id_banco==1){
		//_send('pages/tesoreria/fac_banco_banbif.html',nom,'Generaci&oacute;n de archivos');
		document.location.href= _URL + 'api/archivo/genArchivoAnual/' + id_banco + '/' + $('#_id_anio').text()+ '/'+meses ;
	}else{

		//document.location.href= _URL + 'api/banco/genArchivoAnual/' + id_banco + '/' + $('#_id_anio').text() ;
		document.location.href= _URL + 'api/archivo/genArchivoAnual/' + id_banco + '/' + $('#_id_anio').text()+ '/'+meses ;
	}
}