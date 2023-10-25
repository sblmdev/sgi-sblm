window.onload=EstadoNotificacion;

function EstadoNotificacion(){
	
		var idTip = $("select[name*='idTipo']").val();
		

	    if(idTip==1){ 
	    	
	    	$(".clsRevisado").attr('style','display:visible');
	    	$(".clsCancelado").attr('style','display:visible');
	    	$(".nroCabeceraPendiente").attr('style','display:visible'); 
	    	$(".nroCabeceraRevisado").attr('style','display:none'); 
	    	$(".nroCabeceraCancelado").attr('style','display:none'); 

	    }else{
	        if(idTip==2){
	        	$(".clsRevisado").attr('style','display:none');
	        	$(".clsCancelado").attr('style','display:none');
		    	$(".nroCabeceraPendiente").attr('style','display:none'); 
		    	$(".nroCabeceraRevisado").attr('style','display:visible'); 
		    	$(".nroCabeceraCancelado").attr('style','display:none'); 


	        }else{
	        	if(idTip==3){
		        	$(".clsRevisado").attr('style','display:visible');
		        	$(".clsCancelado").attr('style','display:none');
			    	$(".nroCabeceraPendiente").attr('style','display:none'); 
			    	$(".nroCabeceraRevisado").attr('style','display:none'); 
			    	$(".nroCabeceraCancelado").attr('style','display:visible'); 
	
		        }else{
	        	
	        	$(".clsRevisado").attr('style','display:visible');
	        	$(".clsCancelado").attr('style','display:visible');
		    	$(".nroCabeceraPendiente").attr('style','display:visible'); 
		    	$(".nroCabeceraRevisado").attr('style','display:none'); 
		    	$(".nroCabeceraCancelado").attr('style','display:none'); 
		        }
	        }
	    	
	    }
	}

function verHU(){
	var valor = $("input[name*='xxx']").val();
	
	if(valor==='true'){ 
		 $("#idHU").attr('style','display:visible');
	}else{
		 $("#idHU").attr('style','display:none');
	}
		 
}
//mostrar-ocultar panel hab urbana UPA.
function verupaHU(){
	var valor = $("input[name*='yyyy']").val();
	
	if(valor==='true'){ 
		
		 $("#idupaHU").attr('style','display:visible');
	}else{
	
		 $("#idupaHU").attr('style','display:none');
	}
		 
}

function seleccionarTipo()
    {        
	    var idTipo = $("select[name*='idTipo']").val();
		    if(idTipo==1){ 
		        $(".clsBotonUsuario").attr('style','display:visible');
		        $(".clsBotonPerfil").attr('style','display:none');
		        $(".clsRestoUsuario").attr('style','display:visible');
		        $(".mensajeDefectoAuditoria").attr('style','display:none');
		    	
		        }
	        if(idTipo==2){ 
	            $(".clsBotonUsuario").attr('style','display:none');
	            $(".clsBotonPerfil").attr('style','display:visible');
	            $(".clsRestoUsuario").attr('style','display:visible');
	            $(".mensajeDefectoAuditoria").attr('style','display:none');
	        }
    }
	
	function Tipo2(){
    	$(".botonAtendido").attr('style','background-color: #ffc600');
    	$(".botonPendiente").attr('style','background-color: #fed02e');
    	$("#idTipo").attr('value','xxxx');
	}
	
	function Tipo1(){
		$(".botonAtendido").attr('style','background-color: #fed02e');
		$(".botonPendiente").attr('style','background-color: #ffc600');
	}
	
	function ocultar(){
		$(".clsUno").attr('style','display:none');
		$(".clsDos").attr('style','display:visible');
	}
	
	function ocultar2(){
		$(".clsUno").attr('style','display:visible');
		$(".clsDos").attr('style','display:none');
	}
		
	
	
	function EstadoNotificacionMaestras(){
		
		var idTip = $("select[name*='idTipo']").val();
    	$(".nroCabeceraRevisado").attr('style','display:none'); 
    	$(".nroCabeceraCancelado").attr('style','display:none'); 
    	$(".nroCabeceraPendiente").attr('style','display:none'); 
		

	    if(idTip==1){ 
//	    	
    	$(".nroCabeceraPendiente").attr('style','display:visible'); 
//	    	$(".nroCabeceraRevisado").attr('style','display:none'); 
//	    	$(".nroCabeceraCancelado").attr('style','display:none'); 
	    }else{
	        if(idTip==2){
//		    	$(".nroCabeceraPendiente").attr('style','display:none'); 
		    	$(".nroCabeceraRevisado").attr('style','display:visible'); 
//		    	$(".nroCabeceraCancelado").attr('style','display:none'); 

	        }else{
	        	if(idTip==3){
//			    	$(".nroCabeceraPendiente").attr('style','display:none'); 
//			    	$(".nroCabeceraRevisado").attr('style','display:none'); 
			    	$(".nroCabeceraCancelado").attr('style','display:visible'); 
		        }
	        }
	    	
	    }
	}
	

	
	function openUploadFileDialogue(){
		$('input[type=file]').click();
	}
	

	
	function blockpage(){
		$.blockUI({ css: { 
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        } }); 
	}
	
	function unblockpage(){
		$.unblockUI(); 
	}
