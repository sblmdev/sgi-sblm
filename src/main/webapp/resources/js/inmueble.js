$(function() {

	$("#idDivGeneral").show();
	$("#idDivRegistral").hide();
	$("#idDivTecnico").hide();

	$(".demo").hide();

	/** **oculta mensaje inmueble general * */

	$(
			"#frmGeneral\\:idContratoClave,#frmGeneral\\:txtRegSbn, #frmGeneral\\:txtDenominacion,#frmGeneral\\:txtDireccion")
			.keyup(function() {
				if ($(this).val() != "") {
					$(".error").fadeOut();
					return false;
				}
			});
});

function tabGeneral() {
	$("#idGeneral").attr('style', 'background-color: #ffc600');
	$("#idRegistral").attr('style', 'background-color: #fed02e');
	$("#idTecnico").attr('style', 'background-color: #fed02e');
	valor = $("#idGeneral").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneral").show();
	$("#idDivRegistral").hide();
	$("#idDivTecnico").hide();

	// alert($("#pnDerivacion").text());
	// alert($("#pnDerivacion").attr("visible"));
	$("#idDivGeneral").removeAttr("visible");

}
function tabRegistral() {

	$("#idGeneral").attr('style', 'background-color: #fed02e');
	$("#idRegistral").attr('style', 'background-color: #ffc600');
	$("#idTecnico").attr('style', 'background-color: #fed02e');
	valor = $("#idRegistral").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneral").hide();
	$("#idDivRegistral").show();
	$("#idDivTecnico").hide();

}
function tabTecnico() {
	$("#idGeneral").attr('style', 'background-color: #fed02e');
	$("#idRegistral").attr('style', 'background-color: #fed02e');
	$("#idTecnico").attr('style', 'background-color: #ffc600');
	valor = $("#idTecnico").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneral").hide();
	$("#idDivRegistral").hide();
	$("#idDivTecnico").show();
}

/** *********validacion inmueble general************* */
function posicionarEnElemento(identifier, topPadding) {
	if (topPadding == undefined) {
		topPadding = 0;
	}
	var moveTo = $(identifier).offset().top - topPadding;
	$('html, body').stop().animate({
		scrollTop : moveTo
	}, 1000);
}
function validacioInmuebleGeneral() {

	$(".error").remove();

	if ($("#frmGeneral\\:idContratoClave").val() == "") {
		posicionarEnElemento('#frmGeneral\\:idContratoClave', 200);
		$("#frmGeneral\\:idContratoClave").focus().after(
				"<span class='error'>Ingrese Clave</span>");
		return false;
	} else if ($("#frmGeneral\\:txtRegSbn").val() == "") {
		posicionarEnElemento('#frmGeneral\\:txtRegSbn', 200);
		$("#frmGeneral\\:txtRegSbn").focus().after(
				"<span class='error'>Ingrese Nro. Registro SBN</span>");
		return false;

	} else if ($("#frmGeneral\\:cbxLstDptos option:selected").val() == 0) {

		posicionarEnElemento('#frmGeneral\\:cbxLstDptos', 200);

		$("#frmGeneral\\:cbxLstDptos").focus().after(
				"<span class='error'>Seleccione Departamento</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstProvincias option:selected").val() == 0) {
		posicionarEnElemento('#frmGeneral\\:cbxLstProvincias', 200);
		$("#frmGeneral\\:cbxLstProvincias").focus().after(
				"<span class='error'>Seleccione Provincia</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstDistritos option:selected").val() == 0) {
		posicionarEnElemento('#frmGeneral\\:cbxLstDistritos', 200);
		$("#frmGeneral\\:cbxLstDistritos").focus().after(
				"<span class='error'>Seleccione Distrito</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstDistritos option:selected").val() == 0) {
		posicionarEnElemento('#frmGeneral\\:cbxLstDistritos', 200);
		$("#frmGeneral\\:cbxLstDistritos").focus().after(
				"<span class='error'>Seleccione Distrito</span>");
		return false;
	} else if ($("#frmGeneral\\:txtDireccion").val() == "") {
		posicionarEnElemento('#frmGeneral\\:cbxLstDistritos', 200);
		$("#frmGeneral\\:txtDireccion").focus().after(
				"<span class='error'>Ingrese Direccion</span>");
		return false;
	 }  else if ($("#frmGeneral\\:txtUbigeo option:selected").val() == 0) {
			 posicionarEnElemento('#frmGeneral\\:txtUbigeo', 200);
		 $("#frmGeneral\\:txtUbigeo").focus().after(
		 "<span class='error'>Seleccione  Ubigeo</span>");
		 return false;
	} else {
		dlgRegistrarInmuebleGeneral.show();
	}

};
function validacionInmuebleRegistral() {

	$(".error").remove();
	if ($("#frmRegistral\\:txtRegSbn02").val() == "") {

		msjRegistralx.renderMessage({
			summary : 'Notificacion',
			detail : 'Debe seleccionar un registro de la tabla',
			severity : 0
		});
		return false;

	} else if ($("#frmRegistral\\:txtDenominacion").val() == "") {
		$("#frmRegistral\\:txtRegSbn").focus().after(
				"<span class='error'>Ingrese Denominacion</span>");
		return false;
	} else if ($("#frmRegistral\\:txtTitularidad option:selected").val() == 0) {
		$("#frmRegistral\\:txtTitularidad").focus().after(
				"<span class='error'>Seleccione titularidad</span>");
		return false;
	} else if ($("#frmRegistral\\:txtTipoTitularidad option:selected").val() == 0) {
		$("#frmRegistral\\:txtTipoTitularidad").focus().after(
				"<span class='error'>Seleccione tipo titularidad</span>");
		return false;
	} else {
		dlgRegistrarInmuebleRegistral.show();
	}
};

function validacionInmuebleTecnico() {

	$(".error").remove();
	if ($("#frmTecnico\\:txtRegSbn03").val() == "") {
		msjTecnicox.renderMessage({
			summary : 'Notificacion',
			detail : 'Debe seleccionar un registro de la tabla',
			severity : 0
		});
		return false;
	} else if ($("#frmTecnico\\:txtDenominacion").val() == "") {
		$("#frmTecnico\\:txtRegSbn").focus().after(
				"<span class='error'>Ingrese Denominacion</span>");
		return false;
	} else if ($("#frmTecnico\\:txtTitularidad option:selected").val() == 0) {
		$("#frmTecnico\\:txtTitularidad").focus().after(
				"<span class='error'>Seleccione titularidad</span>");
		return false;
	} else if ($("#frmTecnico\\:txtTipoTitularidad option:selected").val() == 0) {
		$("#frmTecnico\\:txtTipoTitularidad").focus().after(
				"<span class='error'>Seleccione tipo titularidad</span>");
		return false;
	} else {

		dlgRegistrarInmuebleTecnico.show();
	}
};
function limpiarvalidacion() {
	if ($("#frmGeneral\\:txtTipoVia option:selected").val() != "0") {

		$(".error").fadeOut();
		return false;
	}
}