function onloadPage(){
	
}
$(function(){
	

	$('#frm-change').on('submit',function(event){
		event.preventDefault();
		$('#frm-change #id').val(_usuario.id);
		$('#frm-change #id_per').val(_usuario.id_per);
		_post('api/seguridad/change',$(this).serialize(), function(data){
			
			document.location.href = 'dashboard.html';
		});
	});
	
	
});