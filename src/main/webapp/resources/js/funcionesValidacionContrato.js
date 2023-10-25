$(document)
		.ready(
				function() {

					/** **oculta mensaje super usuarios* */
					$(
							"#frmPropiedades\\:idContratoNumero, #frmPropiedades\\:idContratoAnho")
							.keyup(function() {
								if ($(this).val() != "") {
									$(".error").fadeOut();
									return false;
								}
							});

				});



function validacionContratoPropiedades() {
	$(".error").remove();
	var hasError = false;
	
	if ($("#frmPropiedades\\:idContratoTipo option:selected").val() == "") {
		posicionarEnElemento('#frmPropiedades\\:idContratoTipo', 200);
		$("#frmPropiedades\\:idContratoTipo").focus().after(
				"<span class='error'>Seleccione tipo de contrato</span>");
		return false;
	} else if ($("#frmPropiedades\\:idContratoNumero").val() == "") {
		$("#frmPropiedades\\:idContratoNumero").focus().after(
				"<span class='error'>Ingrese n&uacutemero de contrato</span>");
		return false;
	} else if ($("#frmPropiedades\\:idContratoAnho option:selected").val() == "0") {
		posicionarEnElemento('#frmPropiedades\\:idContratoTipo', 200);
		$("#frmPropiedades\\:idContratoAnho").focus().after(
				"<span class='error'>Ingrese a&ntilde;o de contrato </span>");
		return false;
	}
	posicionarEnElemento('#frmPropiedades\\:idContratoTipo', 0);
}



function validacionSinContratoPropiedades() {
	
	$(".error").remove();
	var hasError = false;
	

	
	posicionarEnElemento('#frmPropiedadesSC\\:idContratoAnho', 20);
}


function limpiarvalidacion() {
	if ($("#frmPropiedades\\:idContratoTipo option:selected").val() != "") {

		$(".error").fadeOut();
		return false;
	}
}


function limpiarvalidacionAnioContrato() {
	if ($("#frmPropiedades\\:idContratoAnho option:selected").val() != "") {

		$(".error").fadeOut();
		return false;
	}
}

function posicionarEnElemento(identifier, topPadding) {
	if (topPadding == undefined) {
		topPadding = 0;
	}
	var moveTo = $(identifier).offset().top - topPadding;
	$('html, body').stop().animate({
		scrollTop : moveTo
	}, 1000);
}



