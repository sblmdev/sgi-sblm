
function validacionRecaudacionArbitrio() {

	$(".error").remove();

	var hasError = false;
	
	
	if ($("#formPanelRegistroArbitrio\\:idmonto").val() == "") {
		$("#formPanelRegistroArbitrio\\:idmonto").focus().after(
				"<span class='error'>Ingrese monto</span>");
		return false;
	} else 
	{
		widgetDlgConfirmGuardarArbitrio.show();

	}

}

function validacionRecaudacionCarta() {

	$(".error").remove();

	var hasError = false;
	
	widgetDlgConfirmGuardarCarta.show();

}




