$(function() {

	$("#idDivGeneralUpaUpa").show();
	$("#idDivTecnicosUpa").hide();
	$("#idDivOtrosUpa").hide();

	/** **oculta mensaje UPA general * */
 
	$(" #frmGeneral\\:idUpaClave, #frmGeneral\\:txtNombreHabUrbana,#frmGeneral\\:txtUpaInmueble, #frmGeneral\\:txtLote, #frmGeneral\\:txtSubLote, #frmGeneral\\:txtDireccion, #frmGeneral\\:txtNombreHabUrbana, #frmGeneral\\:txtNroPrincipal")
			.keyup(function() {
				
				if ($(this).val() != "") {
					$(".error").fadeOut();
					return false;
				}
			});
	
	$("#frmTecnicos\\:txtAreaTerreno, #frmTecnicos\\:txtAreaConstruida, #frmTecnicos\\:txtAreaComun, #frmTecnicos\\:txtAreaLibre , #frmTecnicos\\:txtAreaDeclarada ")
	.keyup(function() {
		
		if ($(this).val() != "") {
			$(".error").fadeOut(); 
			return false;
		}
	});

	$("#frmValidacionUpa\\:txtMercedConduc")
	.keyup(function() {
		
		if ($(this).val() != "") {
			$(".error").fadeOut();
			return false;
		}
	});


	
});

function tabGeneralUpa() {
	$("#idGeneralUpa").attr('style', 'background-color: #ffc600');
	$("#idTecnicosUpa").attr('style', 'background-color: #fed02e');
	$("#idOtrosUpa").attr('style', 'background-color: #fed02e');
	valor = $("#idGeneralUpa").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneralUpa").show();
	$("#idDivTecnicosUpa").hide();
	$("#idDivOtrosUpa").hide();

	// alert($("#pnDerivacion").text());
	// alert($("#pnDerivacion").attr("visible"));
	$("#idDivGeneralUpa").removeAttr("visible");

}
function tabTecnicosUpa() {

	$("#idGeneralUpa").attr('style', 'background-color: #fed02e');
	$("#idTecnicosUpa").attr('style', 'background-color: #ffc600');
	$("#idOtrosUpa").attr('style', 'background-color: #fed02e');
	valor = $("#idTecnicosUpa").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneralUpa").hide();
	$("#idDivTecnicosUpa").show();
	$("#idDivOtrosUpa").hide();

}
function tabOtrosUpa() {
	$("#idGeneralUpa").attr('style', 'background-color: #fed02e');
	$("#idTecnicosUpa").attr('style', 'background-color: #fed02e');
	$("#idOtrosUpa").attr('style', 'background-color: #ffc600');
	valor = $("#idOtrosUpa").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Datos' + valor);

	$("#idDivGeneralUpa").hide();
	$("#idDivTecnicosUpa").hide();
	$("#idDivOtrosUpa").show();
}

/** *********validacion upa general************* */

function validacioUpaGeneral() {

	$(".error").remove();
	
	if ($("#frmGeneral\\:idUpaClave").val() == "") {
		posicionarEnElemento('#frmGeneral\\:idUpaClave', 200);
		$("#frmGeneral\\:idUpaClave").focus().after(
				"<span class='error'>Ingrese Clave</span>");
		return false;
	} else if ($("#frmGeneral\\:txtUpaInmueble").val() == "") {
		posicionarEnElemento('#frmGeneral\\:txtUpaInmueble', 200);
		$("#frmGeneral\\:txtUpaInmueble").focus().after(
				"<span class='error'>Ingrese inmueble</span>");
		return false;
	} else if ($("#frmGeneral\\:txtTipoVia  option:selected").val() == 0) {
		$("#frmGeneral\\:txtTipoVia").focus().after(
				"<span class='error'>Ingrese Tipo de Via</span>");
		return false;
	} else if ($("#frmGeneral\\:txtLote").val() == "") {

		$("#frmGeneral\\:txtLote").focus().after(
				"<span class='error'>Ingrese Lote</span>");
		return false;
	} else if ($("#frmGeneral\\:txtSubLote").val() == "") {
		$("#frmGeneral\\:txtSubLote").focus().after(
				"<span  class='error'>Ingrese SubLote</span>");
		return false;

	} else if ($("#frmGeneral\\:txtTipoVia option:selected").val() == 0) {
		$("#frmGeneral\\:txtTipoVia").focus().after(
				"<span class='error'>Seleccione tipo via</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstDptos option:selected").val() == 0) {
		$("#frmGeneral\\:cbxLstDptos")
				.focus()
				.after(
						"<span style='float:left' class='error'>Seleccione Departamento</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstProvincias option:selected").val() == 0) {
		$("#frmGeneral\\:cbxLstProvincias").focus().after(
				"<span class='error'>Seleccione Provincia</span>");
		return false;
	} else if ($("#frmGeneral\\:cbxLstDistritos option:selected").val() == 0) {
		$("#frmGeneral\\:cbxLstDistritos").focus().after(
				"<span class='error'>Seleccione Distrito</span>");
		return false;
	} else if ($("#frmGeneral\\:txtDireccion").val() == "") {
		$("#frmGeneral\\:txtDireccion").focus().after(
				"<span  class='error'>Ingrese Direccion</span>");
		return false;
	} 
//	else if ($("#frmGeneral\\:txtTipoInterior option:selected").val() == 0) {
//		$("#frmGeneral\\:txtTipoInterior").focus().after(
//				"<span class='error'>Seleccione Tipo Interior</span>");
//		return false;
//	} 
	else if ($("#frmGeneral\\:txtNroPrincipal").val() == "") {
		$("#frmGeneral\\:txtNroPrincipal").focus().after(
				"<span  class='error'>Ingrese Nro. Principal</span>");
		return false;
//	} else if ($("#frmGeneral\\:txtHabUrbana option:selected").val() == 0) {
//		$("#frmGeneral\\:txtHabUrbana").focus().after(
//				"<span class='error'>Seleccione Habilitacion Urbana</span>");
//		return false;
//	} else if ($("#frmGeneral\\:txtNombreHabUrbana").val() == "") {
//		$("#frmGeneral\\:txtNombreHabUrbana").focus().after(
//				"<span  class='error'>Ingrese Nombre Hab. Urbana</span>");
//		return false;

	} else if ($("#frmGeneral\\:txtUbigeo option:selected").val() == 0) {
		$("#frmGeneral\\:txtUbigeo").focus().after(
				"<span class='error'>Seleccione  Ubigeo</span>");
		return false;
	} else {
		dlgRegistrarUpaGeneral.show();
	}

};
function validacionUpaTecnico() {

	$(".error").remove();
	if ($("#frmTecnicos\\:txtUpaInmueble02").val() == "") {

		msjTecnicosx.renderMessage({
			summary : 'Notificacion',
			detail : 'Debe seleccionar un registro de la tabla',
			severity : 0
		});
		return false;
//	} else if ($("#frmTecnicos\\:txtAntiguedad option:selected").val() == 0) {
//		$("#frmTecnicos\\:txtAntiguedad").focus().after(
//				"<span class='error'>Seleccione Antiguedad</span>");
//		return false;
//	} else if ($("#frmTecnicos\\:txtMep option:selected").val() == 0) {
//		$("#frmTecnicos\\:txtMep").focus().after(
//				"<span class='error'>Seleccione Material estructural</span>");
//		return false;

//	} else if ($("#frmTecnicos\\:txtConservacion option:selected").val() == 0) {
//		$("#frmTecnicos\\:txtConservacion").focus().after(
//				"<span class='error'>Seleccione Conservacion</span>");
//		return false;
//	} else if ($("#frmTecnicos\\:txtUso option:selected").val() == 0) {
//		$("#frmTecnicos\\:txtUso").focus().after(
//				"<span class='error'>Seleccione Uso</span>");
//		return false;
	} else if (isNaN($("#frmTecnicos\\:txtAreaTerreno").val())) {
		$("#frmTecnicos\\:txtAreaTerreno").focus().after(
				"<span class='error'>Area Terreno ser numerico</span>");
		return false;
	} else if (isNaN($("#frmTecnicos\\:txtAreaConstruida").val())) {
		$("#frmTecnicos\\:txtAreaConstruida").focus().after(
				"<span class='error'>Area Construida debe ser numerico</span>");
		return false;
	} else if (isNaN($("#frmTecnicos\\:txtAreaComun").val())) {
		$("#frmTecnicos\\:txtAreaComun").focus().after(
				"<span class='error'>Area Comun debe ser numerico</span>");
		return false;
	} else if (isNaN($("#frmTecnicos\\:txtAreaLibre").val())) {
		$("#frmTecnicos\\:txtAreaLibre").focus().after(
				"<span class='error'>Area Libre debe ser numerico</span>");
		return false;
	} else if (isNaN($("#frmTecnicos\\:txtAreaDeclarada").val())) {
		$("#frmTecnicos\\:txtAreaDeclarada").focus().after(
				"<span class='error'>Area Declarada debe ser numerico</span>");
		return false;

	} else if ($("#frmRegistral\\:txtTipoTitularidad option:selected").val() == 0) {
		$("#frmRegistral\\:txtTipoTitularidad").focus().after(
				"<span class='error'>Seleccione tipo titularidad</span>");
		return false;
	} else {

		dlgRegistrarUpaTecnico.show();
	}
};

function validacionUpaOtros() {

	$(".error").remove();
	if ($("#frmValidacionUpa\\:txtUpaInmueble03").val() == "") {

		msjTecnicox.renderMessage({
			summary : 'Notificacion',
			detail : 'Debe seleccionar un registro de la tabla',
			severity : 0
		});

		return false;

	
	} else if (isNaN($("#frmValidacionUpa\\:txtMercedConduc").val())) {
		$("#frmValidacionUpa\\:txtMercedConduc").focus().after(
				"<span class='error'>Merced conductiva debe ser numerico</span>");
		return false;
	} else {

		dlgRegistrarUpaOtros.show();
	}
};
function limpiarvalidacion() {
	if ($("#frmGeneral\\:txtTipoVia option:selected").val() != "0") {

		$(".error").fadeOut();
		return false;
	}
}