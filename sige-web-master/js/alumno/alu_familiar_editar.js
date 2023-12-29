//edicion completa de una tabla
function familiar_editarFull(row,e){
	e.preventDefault();
	
	var link = $(row);
	link.attr('href','pages/alumno/alu_familiar_editar.html');
	familiar_full_modal(link);

}


function familiar_full_modal(link){

	//funcion q se ejecuta al cargar el modal
	var onShowModal = function(){
		_inputs('alu_familiar_modal-frm');
		
		$('#id_gpf').val(link.data('id_gpf'));
				
		if (link.data('id')){
			_get('api/familiar/' + link.data('id'),function(data){
					
					_fillForm(data.familiar,$('#modal_full').find('form') );
					_llenarCombo('cat_tipo_documento',$('#alu_familiar_modal-frm #id_tdc'),data.familiar.id_tdc );
					_llenarCombo('cat_parentesco',$('#alu_familiar_modal-frm #id_par'),link.data('id_par') );
					$('#btn-buscar-nro_doc').hide();
					$('#alu_familiar_modal-frm #nro_doc').attr('readonly',true);
					$('#alu_familiar_modal-frm #id_par').attr('disabled',true);
					$('#alu_familiar_modal-frm #id_gen').attr('disabled',true);
					//alert(link.data('id_fam'));
					if (link.data('id')==_usuario.id){
						//EL MISMO APODERADO NO PUEDE QUITARSE PRIVILEGIOS
						$('#btn-aceptar').show();
						$('#btn-grabar').hide();
					}
					
				});
			
		}else{
			var id_par_usuario = $('#alu_familiar-frm #id_par').val();
			$('.fam-det').hide();
			var id_par= "";
			if (id_par_usuario =="1")
				id_par = "2";
			if (id_par_usuario =="2")
				id_par = "1";
			
			_llenarCombo('cat_tipo_documento',$('#alu_familiar_modal-frm #id_tdc') );
			_llenarCombo('cat_parentesco',$('#alu_familiar_modal-frm #id_par'),id_par );
			_llenarCombo('cat_est_civil',$('#alu_familiar_modal-frm #id_eci') );
			_llenarCombo('cat_grado_instruccion',$('#alu_familiar_modal-frm #id_gin') );
			_llenarCombo('cat_religion',$('#alu_familiar_modal-frm #id_rel') );
			
		
			_disabled_combo($('#alu_familiar_modal-frm #id_tdc'), false);
			$('#alu_familiar_modal-frm #id_tdc').val('1'); //POR DEFECTO
		
		}
		
		var validator = $("#alu_familiar_modal-frm").validate();

		$('#alu_familiar_modal-frm #btn-grabar').on('click',function(event){
			
			$("#alu_familiar_modal-frm #_id_fam").val(link.data('id_fam'));
			$("#alu_familiar_modal-frm #_id_par").val($("#alu_familiar-frm #id_par").val());
			
			event.preventDefault();
			if ($("#alu_familiar_modal-frm").valid()){
				_post_silent( "api/familiar/grabar" , $('#alu_familiar_modal-frm').serialize(),function(data){
					console.log(data);
					if (data.code==null){
						_alert("Familiares","Se agregó familiar nuevo al grupo familiar");
						$("#modal_form_vertical2 .close").click();
						var i=0;
						_get('api/familiar/familias/' + $('#_id_anio').text() + '/' + _usuario.id, function(data){
							$('.familia-div').each(function () {
								var familia = data[i];
								var padre_madre = obtener_padre_madre(familia.gruFamFamiliars);
								
								$(this).find('#btn-agregar').data('id_gpf',familia.gruFamFamiliars[0].id_gpf); 
								$(this).find('#btn-agregar').data('id_fam',familia.gruFamFamiliars[0].id_fam); 
								$(this).find('#btn-agregar').on('click', function(e){
									familiar_editarFull($(this),e);	
								});
								
								$(this).find('#titulo').html('FAMILIA ' + padre_madre);
								$(this).find('#familiar-tabla').dataTable({
									 data : familia.gruFamFamiliars,
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
							        	 	{"title":"Familiar",  "render":function ( data, type, row,meta ) { return row.familiar.ape_pat + ' ' + row.familiar.ape_mat +', ' + row.familiar.nom}},
							        	 	{"title":"Nro Doc", "data": "familiar.nro_doc"},
							        	 	{"title":"Parentesco", "data": "familiar.parentesco.par"},
											{"title":"Acciones", "render": function ( data, type, row ) {
							                   return '<ul class="icons-list">'+
													'<li class="dropdown">'+
												'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'+
													'<i class="icon-menu9"></i>'+
													'</a>'+
												'<ul class="dropdown-menu dropdown-menu-right">'+
													'<li><a href="#" data-id="' + row.id + '" onclick="familiar_editarFull(this, event)"><i class="fa fa-pencil-square-o"></i> Editar</a></li>'+
													'<li><a href="#" data-id="' + row.id + '" onclick="familiar_eliminar(this)"><i class="fa fa-trash-o"></i> Eliminar</a></li>'+
												'</ul>'+
							                   '</li>'+
							                   '</ul>';}
											}
								    ]
							    });
								
								i++;
							});
						});
					}
				});
				
			}
		});
	}

	//funcion q se ejecuta despues de grabar
	var onSuccessSave = function(data){
		familiar_listar_tabla();
	}
	
	//Abrir el modal
	var titulo;
	if (link.data('id'))
		titulo = 'Editar Familiar del alumno';
	else
		titulo = 'Nuevo  Familiar del alumno';
	
	_modal_full(titulo, link.attr('href'),onShowModal,onSuccessSave);

}

function buscarFamiliar(){
	
	var nro_doc = $('#alu_familiar_modal-frm #nro_doc').val();
	
	if (nro_doc.trim()==''){
		_alert_error('Nro de documento es obligatorio.');
		return;
	}
	if (nro_doc.length!=8){
		_alert_error('Nro de documento debe tener 8 digitos.');
		return;
	}	
	if (nro_doc.trim()==$('#alu_familiar-frm #nro_doc').val()){
		_alert_error('Nro de documento debe ser diferente al documento suyo.');
		return;
	}
	
	if ($('#alu_familiar_modal-frm #ape_mat')!='')
		$('#alu_familiar_modal-frm #id_tap').val('A');
	else
		$('#alu_familiar_modal-frm #id_tap').val('P');
	
	$('#alu_familiar_modal-frm').trigger('reset');
	$('#alu_familiar_modal-frm #nro_doc').val(nro_doc);
	
	_get('api/familiar/nro_doc/' + nro_doc, function(data){
		console.log(data);
		if (data==null){
			$('.fam-det').show();
			$('#alu_familiar_modal-frm #ape_pat').prop('readonly', false);
			$('#alu_familiar_modal-frm #ape_mat').prop('readonly', false);
			$('#alu_familiar_modal-frm #nom').prop('readonly', false);

			$('#alu_familiar_modal-frm #ape_pat').focus();

		}else{
			_fillForm(data,$('#modal_full').find('form') );
			$('.fam-det').show();
			
			$('#alu_familiar_modal-frm #ape_pat').prop('readonly', true);
			$('#alu_familiar_modal-frm #ape_mat').prop('readonly', true);
			$('#alu_familiar_modal-frm #nom').prop('readonly', true);


		}
	});
	
}

function limpiar(){
	$('.fam-det').hide();
	$('#alu_familiar_modal-frm #ape_pat').val('');
	$('#alu_familiar_modal-frm #ape_mat').val('');
	$('#alu_familiar_modal-frm #ape_nom').val('');
	$('#alu_familiar_modal-frm #fec_nac').val('');
	$('#alu_familiar_modal-frm #corr').val('');
	$('#alu_familiar_modal-frm #tlf').val('');
	$('#alu_familiar_modal-frm #cel').val('');
	$('#alu_familiar_modal-frm #ocu').val('');
	$('#alu_familiar_modal-frm #cto_tra').val('');
	
	$('#alu_familiar_modal-frm #id_gin').val('').change();
}

function cerrarModal(){
	//$("#modal_form_vertical2 .close").click();
	$(".modal-dialog .close").click();
}

function onchangeParentesco(field){
	var id_par = field.value;
	if (id_par=='1'){
		$('#id_gen').val('0').change();
		$('#id_gen').attr('disabled',true);
	}else if (id_par=='2'){
		$('#id_gen').val('1').change();
		$('#id_gen').attr('disabled',true);
	}else{
		$('#id_gen').val('1').change();
		$('#id_gen').attr('disabled',false);
	}
}