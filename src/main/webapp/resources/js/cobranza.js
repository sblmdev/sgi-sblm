$(function() {
//alert("cobranza");
	$("#idDivTab01Cobranza").hide();
	$("#idDivTab02Cobranza").hide();
	$("#idDivTab03Cobranza").show();
	

	$(".demo").hide();
	$("#idTab01Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab02Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab03Cobranza").attr('style', 'background-color: #ffc600');
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

function funcionTab01Cobranza() {
	
	$("#idTab01Cobranza").attr('style', 'background-color: #ffc600');
	$("#idTab02Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab03Cobranza").attr('style', 'background-color: #fed02e');

	valor = $("#idTab01Cobranza").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01Cobranza").show();
	$("#idDivTab02Cobranza").hide();
	$("#idDivTab03Cobranza").hide();
	
	
	$("#idDivTab01Cobranza").removeAttr("visible");

}
function funcionTab02Cobranza() {
	
	$("#idTab01Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab02Cobranza").attr('style', 'background-color: #ffc600'); 
	$("#idTab03Cobranza").attr('style', 'background-color: #fed02e');
	
	valor = $("#idTab02Cobranza").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01Cobranza").hide();
	$("#idDivTab02Cobranza").show();
	$("#idDivTab03Cobranza").hide();
	

}
function funcionTab03Cobranza() {
	
	$("#idTab01Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab02Cobranza").attr('style', 'background-color: #fed02e');
	$("#idTab03Cobranza").attr('style', 'background-color: #ffc600');
	
	valor = $("#idTab03Cobranza").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivTab01Cobranza").hide();
	$("#idDivTab02Cobranza").hide();
	$("#idDivTab03Cobranza").show();
	
}

/** *********validacion inquilino general************* */
