function onloadPage(){
	document.cookie = '_token=;_timesession=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}
$(function(){
	
	var fncExito = function(data){
		console.log(data);
		/*
		if(data.result.ini==null){
			$('#frm-change #id').val(data.result.id);
			$('#frm-change #id_per').val(data.result.id_per);
			$modal = $('#modal-change').modal('show');		
		}else{
			document.location.href = 'dashboard.html';
		}*/
		
		//guardar coockie
		var token = data.result.token;
		document.cookie = '_token=' + token;
		document.cookie ='_options=' + JSON.stringify(data.result.opciones);

		
//		document.cookie = '_fechaExpiracion=' + token.fechaExpiracion;
//		document.cookie = '_minutosExpiracion=' + token.minutosExpiracion;
//		document.cookie = '_minutosAviso=' + token.minutosAviso;
		
		console.log(getCookie('_token'));
		console.log(getCookie('_options'));
		document.location.href = 'dashboard.html';
		
	};
	
	$('#frm-login').on('submit',function(event){
		event.preventDefault();
		return _POST({  url:'api/seguridad/login',
						form: $(this),
						success:fncExito,
						cookie:false,
						silent:true
						});
	});

	$('#frm-change').on('submit',function(event){
		event.preventDefault();
		_post('api/seguridad/change',$(this).serialize(), function(data){
			document.location.href = 'index.html';
		});
	});
	
	$('#frm-recover').on('submit',function(event){
		event.preventDefault();
		_post('api/seguridad/recover',$(this).serialize(), function(data){
			
			$(".modal-recover .close").click();
			
			swal({
				html:true,
				title: "<font color='black'>Transacción existosa!</font>",
	            //text: "<font color='black'>La operación se efectuó exitosamente, por favor revise su correo</font>",
				text: "<font color='black'>"+data.msg+"</font>",
	            confirmButtonColor: "#66BB6A",
	            type: "success"
	        });
		},true);
	});
	
});

function abrirRecover(){
	 swal.close();
	$('#modal-recover').modal('show');	
}

