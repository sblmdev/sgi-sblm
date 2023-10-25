function validacionSeleccionContratoSeg() {
	$(".error").remove();
	if ($("#frmdlgselecContra\\:idContratoSegui option:selected").val() == 0) {
		$("#frmdlgselecContra\\:idContratoSegui").focus().after(
				"<span class='error'>Seleccione Contrato</span>");
		return false;
	} else {
		
		$(".error").remove();
		dlgAlertaSeguimiento.show();
	}

};
