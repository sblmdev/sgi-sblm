$(function() {
	$("#idDivRegistroRegistro").show();
	$("#idDivRegistroConsulta").hide();
	
	

	$(".demo").hide();
	$("#idTabRegistroRegistro").attr('style', 'background-color: #328CCB');
	$("#idTabRegistroConsulta").attr('style', 'background-color: #4ABBF4');
	
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

function funcionTabRegistroRegistro() {
	$("#idTabRegistroRegistro").attr('style', 'background-color: #328CCB');
	$("#idTabRegistroConsulta").attr('style', 'background-color: #4ABBF4');
	
	valor = $("#idTabRegistroRegistro").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivRegistroRegistro").show();
	$("#idDivRegistroConsulta").hide();
	
	
	
	$("#idDivRegistroRegistro").removeAttr("visible");

}
function funcionTabRegistroConsulta() {

	$("#idTabRegistroRegistro").attr('style', 'background-color: #4ABBF4');
	$("#idTabRegistroConsulta").attr('style', 'background-color: #328CCB'); 
	
	valor = $("#idTabRegistroConsulta").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivRegistroRegistro").hide();
	$("#idDivRegistroConsulta").show();
	
}


/** *********validacion detalle cuota ************* */



function validacionRegistroDetalleCuota() {
	
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove(); 
	
	if ($("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_ContratoCuota option:selected").val() == 0) {
		$("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_ContratoCuota").focus().after(
				"<span class='error'>Seleccione Numero contrato</span>");
		return false;
	} else	if ($("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_ContratoNumCuota option:selected").val() == 0) {
			$("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_ContratoNumCuota").focus().after(
					"<span class='error'>Seleccione Numero Cuotas</span>");
			return false;
	} else if ($("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_montosoles").val() == "") {
		$("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_montosoles").focus().after(
				"<span class='error'>Ingrese monto en soles</span>");
		return false;
	} else if ($("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_montodolar").val() == "") {
		$("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_montodolar").focus().after(
				"<span class='error'>Ingrese monto en dolares</span>");
		return false;
	
//	} else if ($("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_fecharegistro").val() == "") {
//		$("#frmReg_RegistroDetalleCuota\\:idDetalleCuota_fecharegistro").focus().after(
//				"<span class='error'>Ingrese Fecha registro</span>");
//		return false;
	
	} else {
		
		dlgConfirmarRegistroDetalleCuota.show();
	}
 
};
function validacionLlenadoCartera() {
	$(".error").remove();
	if ($("#frmRegistroCabecera\\:idregcartera").val() == "") {
		$("#frmRegistroCabecera\\:idregcartera").focus().after(
				"<span class='error'>Ingrese Cartera</span>");
		return false;
	} else {
		
		$(".error").remove();
		dlgRegistroCuota.show();
	}

};

/**limpiado de combo**/
function limpiarvalidacion() {
	if ($("#frmReg_RegistroCuota\\:idContratoCuota option:selected").val() != "0") {

		$(".error").fadeOut();
		return false;
	}
}

function desplegarRegistroPago() {
	var condicion=$("input[name*='condicion']");
	
	if (condicion.val()=="Contrato") {
		dlgRegistroCuotaContrato.show();
	} else {
		dlgRegistroCuotaSinContrato.show();
	}
}




function validacionRegistroCuota() {
	
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove(); 
	
	if ($("#frmReg_RegistroCuota\\:idContratoCuota option:selected").val() == 0) {
		$("#frmReg_RegistroCuota\\:idContratoCuota").focus().after(
				"<span class='error'>Seleccione Numero contrato</span>");
		return false;
	} else if ($("#frmReg_RegistroCuota\\:idcuota_montosoles").val() == "") {
		$("#frmReg_RegistroCuota\\:idcuota_montosoles").focus().after(
				"<span class='error'>Ingrese monto en soles</span>");
		return false;
	}  else if ($("#frmReg_RegistroCuota\\:idcuota_mora").val() == "") {
		$("#frmReg_RegistroCuota\\:idcuota_mora").focus().after(
				"<span class='error'>Ingrese mora</span>");
		return false;
	} else if ($("#frmReg_RegistroCuota\\:idcuota_garantia").val() == "") {
		$("#frmReg_RegistroCuota\\:idcuota_garantia").focus().after(
				"<span class='error'>Ingrese garantia</span>");
		return false;
	
	} else {
		
		dlgConfirmarRegistroCuota.show();
	}
 
};

