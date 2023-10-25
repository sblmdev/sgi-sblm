$(function() {

	$("#idDivGerContrato").show(); 
	$("#idDivGerIngreso").hide();
	$("#idDivGerInmueble").hide();
	$("#idDivGerInquilino").hide();

	$(".demo").hide();

	/** **oculta mensaje inquilino general * */

	$( 
			"#frmGeneral\\:inquiApPaterno, #frmGeneral\\:inquiApMaterno, #frmGeneral\\:inquiNombres, #frmGeneral\\:inquiDni, #frmGeneral\\:inquiDireccion")
			.keyup(function() {
				if ($(this).val() != "") {
					$(".error").fadeOut();
					return false;
				}
			});

});

function tabRepGerContrato() {
	$("#idRepGerContrato").attr('style', 'background-color: #ffc600');
	$("#idRepGerIngreso").attr('style', 'background-color: #fed02e');
	$("#idRepGerInmueble").attr('style', 'background-color: #fed02e');
	$("#idRepGerInquilino").attr('style', 'background-color: #fed02e');
	valor = $("#idRepGerContrato").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Reporte' + valor);

	$("#idDivGerContrato").show();
	$("#idDivGerIngreso").hide();
	$("#idDivGerInmueble").hide();
	$("#idDivGerInquilino").hide();

	// alert($("#pnDerivacion").text());
	// alert($("#pnDerivacion").attr("visible"));
	$("#idDivGerContrato").removeAttr("visible");

}
function tabRepGerIngreso() {

	$("#idRepGerContrato").attr('style', 'background-color: #fed02e');
	$("#idRepGerIngreso").attr('style', 'background-color: #ffc600');
	$("#idRepGerInmueble").attr('style', 'background-color: #fed02e');
	$("#idRepGerInquilino").attr('style', 'background-color: #fed02e');
	valor = $("#idRepGerIngreso").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Reporte' + valor);

	$("#idDivGerContrato").hide();
	$("#idDivGerIngreso").show();
	$("#idDivGerInmueble").hide();
	$("#idDivGerInquilino").hide();

}
function tabRepGerInmueble() {
	$("#idRepGerContrato").attr('style', 'background-color: #fed02e');
	$("#idRepGerIngreso").attr('style', 'background-color: #fed02e');
	$("#idRepGerInmueble").attr('style', 'background-color: #ffc600');
	valor = $("#idRepGerInmueble").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Reporte' + valor);

	$("#idDivGerContrato").hide();
	$("#idDivGerIngreso").hide();
	$("#idDivGerInmueble").show();
}
function tabRepGerInquilino() {
	$("#idRepGerContrato").attr('style', 'background-color: #fed02e');
	$("#idRepGerIngreso").attr('style', 'background-color: #fed02e');
	$("#idRepGerInmueble").attr('style', 'background-color: #fed02e');
	$("#idRepGerInquilino").attr('style', 'background-color: #ffc600');
	valor = $("#idRepGerInquilino").text();// capturamos el valor del div
	$("#lblSubtitulo").text('Reporte' + valor);

	$("#idDivGerContrato").hide();
	$("#idDivGerIngreso").hide();
	$("#idDivGerInmueble").hide();
	$("#idDivGerInquilino").show();
}
