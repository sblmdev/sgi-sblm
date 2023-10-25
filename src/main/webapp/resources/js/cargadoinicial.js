
$.blockUI({
					message : '<img  width="25" height="25" src="../resources/icons/load.gif" /><h1 style="color:#B2B2B2;font-size:13px;"> Procesando...</h1>',
					css : {
						border : 'none',
						padding : '15px',
						backgroundColor : '#000',
						'-webkit-border-radius' : '10px',
						'-moz-border-radius' : '10px',
						opacity : .9,
						color : '#fff'
					}

				});  
 
$(document).ready(function() {
	
window.onload =cargaterminado();

});