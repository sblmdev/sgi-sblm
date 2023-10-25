$(function() {

	$("#idDivTab01").show();
	$("#idDivTab02").hide();
	$("#idDivTab03").hide();
	$("#idDivTab04").hide();
	$("#idDivTab05").hide();
	$("#idDivTab06").hide();

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

function funcionTab01() {
	$("#idTab01").attr('style', 'background-color: #ffc600');
	$("#idTab02").attr('style', 'background-color: #fed02e');
	$("#idTab03").attr('style', 'background-color: #fed02e');
	$("#idTab04").attr('style', 'background-color: #fed02e');
	$("#idTab05").attr('style', 'background-color: #fed02e');
	$("#idTab06").attr('style', 'background-color: #fed02e');
	valor = $("#idTab01").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").show();
	$("#idDivTab02").hide();
	$("#idDivTab03").hide();
	$("#idDivTab04").hide();
	$("#idDivTab05").hide();
	$("#idDivTab06").hide();
	
	$("#idDivTab01").removeAttr("visible");

}
function funcionTab02() {

	$("#idTab01").attr('style', 'background-color: #fed02e');
	$("#idTab02").attr('style', 'background-color: #ffc600'); 
	$("#idTab03").attr('style', 'background-color: #fed02e');
	$("#idTab04").attr('style', 'background-color: #fed02e');
	$("#idTab05").attr('style', 'background-color: #fed02e');
	$("#idTab06").attr('style', 'background-color: #fed02e');
	valor = $("#idTab02").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").hide();
	$("#idDivTab02").show();
	$("#idDivTab03").hide();
	$("#idDivTab04").hide();
	$("#idDivTab05").hide();
	$("#idDivTab06").hide();

}
function funcionTab03() {
	$("#idTab01").attr('style', 'background-color: #fed02e');
	$("#idTab02").attr('style', 'background-color: #fed02e');
	$("#idTab03").attr('style', 'background-color: #ffc600');
	$("#idTab04").attr('style', 'background-color: #fed02e');
	$("#idTab05").attr('style', 'background-color: #fed02e');
	$("#idTab06").attr('style', 'background-color: #fed02e');
	valor = $("#idTab03").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").hide();
	$("#idDivTab02").hide();
	$("#idDivTab03").show();
	$("#idDivTab04").hide();
	$("#idDivTab05").hide();
	$("#idDivTab06").hide();
}
function funcionTab04() {
	$("#idTab01").attr('style', 'background-color: #fed02e');
	$("#idTab02").attr('style', 'background-color: #fed02e');
	$("#idTab03").attr('style', 'background-color: #fed02e');
	$("#idTab04").attr('style', 'background-color: #ffc600');
	$("#idTab05").attr('style', 'background-color: #fed02e');
	$("#idTab06").attr('style', 'background-color: #fed02e');
	valor = $("#idTab04").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").hide();
	$("#idDivTab02").hide();
	$("#idDivTab03").hide();
	$("#idDivTab04").show();
	$("#idDivTab05").hide();
	$("#idDivTab06").hide();
}
function funcionTab05() {
	$("#idTab01").attr('style', 'background-color: #fed02e');
	$("#idTab02").attr('style', 'background-color: #fed02e');
	$("#idTab03").attr('style', 'background-color: #fed02e');
	$("#idTab04").attr('style', 'background-color: #fed02e');
	$("#idTab05").attr('style', 'background-color: #ffc600');
	$("#idTab06").attr('style', 'background-color: #fed02e');
	valor = $("#idTab05").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").hide();
	$("#idDivTab02").hide();
	$("#idDivTab03").hide();
	$("#idDivTab04").hide();
	$("#idDivTab05").show();
	$("#idDivTab06").hide();
}
function funcionTab06() {
	$("#idTab01").attr('style', 'background-color: #fed02e');
	$("#idTab02").attr('style', 'background-color: #fed02e');
	$("#idTab03").attr('style', 'background-color: #fed02e');
	$("#idTab04").attr('style', 'background-color: #fed02e');
	$("#idTab05").attr('style', 'background-color: #fed02e');
	$("#idTab06").attr('style', 'background-color: #ffc600');
	valor = $("#idTab06").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01").hide();
	$("#idDivTab02").hide();
	$("#idDivTab03").hide();
	$("#idDivTab04").hide();
	$("#idDivTab05").hide();
	$("#idDivTab06").show();
}
/** *********validacion inquilino general************* */

function limpiarvalidacion() {
	if ($("#frmGeneral\\:inquiTipoPersona option:selected").val() != "0") {

		$(".error").fadeOut();
		return false;
	}
}