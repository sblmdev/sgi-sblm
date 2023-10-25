$(function() {

	/** **oculta mensaje * */

	$("#frmFormuPaginas\\:iddescripcionIgv").keyup(function() {

		if ($(this).val() != "") {
			$(".error").fadeOut();
			return false;
		}
	});

});

function validacionIgv() {
 
	$(".error").remove();
	if (isNaN($("#frmFormuPaginas\\:iddescripcionIgv").val())) {
		$("#frmFormuPaginas\\:iddescripcionIgv").focus().after(
				"<span class='error'>El IGV debe ser numerico</span>");
		return false;
	} else if ($("#frmFormuPaginas\\:iddescripcionIgv").val()< 1) {
		$("#frmFormuPaginas\\:iddescripcionIgv").focus().after(
				"<span class='error'>El IGV no debe ser negativo.</span>");
		return false;
	} else if ($("#frmFormuPaginas\\:iddescripcionIgv").val() == "") {
		$("#frmFormuPaginas\\:iddescripcionIgv").focus().after(
				"<span class='error'>Ingrese IGV</span>");
		return false;
	} else {

		dlgRegistrarPagina.show();
	}
};