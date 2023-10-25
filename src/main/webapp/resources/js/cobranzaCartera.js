$(function() {
//	alert("cobranza cartera");
	$("#idDivCarteraRegistro").show();
	$("#idDivCarteraConsulta").hide();

	$(".demo").hide();
	$("#idTabCarteraRegistro").attr('style', 'background-color: #328CCB');
	$("#idTabCarteraConsulta").attr('style', 'background-color: #4ABBF4');

	/** **oculta mensaje inquilino general * */

	
	
	$("#frmCarteraCDet\\:idCarteraContrato").keyup(function() { 
		
		if ($(this).val() != "" || $(this).val() != 0 ) {
			$(".error").fadeOut();
			return false;
		}
	});
	

});

function funcionTabCarteraRegistro() {

	$("#idTabCarteraRegistro").attr('style', 'background-color: #328CCB');
	$("#idTabCarteraConsulta").attr('style', 'background-color: #4ABBF4');

	valor = $("#idTabCarteraRegistro").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivCarteraRegistro").show();
	$("#idDivCarteraConsulta").hide();

	$("#idDivCarteraRegistro").removeAttr("visible");

}
function funcionTabCarteraConsulta() {

	$("#idTabCarteraRegistro").attr('style', 'background-color: #4ABBF4');
	$("#idTabCarteraConsulta").attr('style', 'background-color: #328CCB');

	valor = $("#idTabCarteraConsulta").text();// capturamos el valor del div
	$("#lblSubtitulo").text('' + valor);

	$("#idDivCarteraRegistro").hide();
	$("#idDivCarteraConsulta").show();

}

/** *********validacion cartera cabecera************* */

function validacionCarteraCabecera() {
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove();
	
	if ($("#frmCarteraCabecera\\:idCarteraNumCartera").val() == "") {
		
		$("#frmCarteraCabecera\\:idCarteraNumCartera").focus().after(
				"<span class='error'>Ingrese numero de cartera</span>");
		return false;
	} else if ($("#frmCarteraCabecera\\:idCarteraCobrador option:selected").val() == 0) {
		
		$("#frmCarteraCabecera\\:idCarteraCobrador").focus().after(
				"<span class='error'>Seleccione cobrador</span>");
		return false;
} else if ($("#frmCarteraCabecera\\:idCarteraTipoLista option:selected").val() == 0) {
		
		$("#frmCarteraCabecera\\:idCarteraTipoLista").focus().after(
				"<span class='error'>Seleccione tipo lista</span>");
		return false;
	}
	
};

/** *********validacion cartera detalle************* */

function validacionCarteraDetalle() {
	
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove();
	if ($("#frmCarteraCD\\:idCarteraUpa").val() == "") {
		$("#frmCarteraCD\\:idCarteraUpa").focus().after(
				"<span class='error'>Ingrese UPA</span>");
		return false;
	} else if ($("#frmCarteraCD\\:idCarteraContrato").val() == "") {
		$("#frmCarteraCD\\:idCarteraContrato").focus().after(
				"<span class='error'>Ingrese Contrato</span>");
		return false;
	} else if ($("#frmCarteraCD\\:idCarteraUso option:selected").val() == 0) {
		$("#frmCarteraCD\\:idCarteraUso").focus().after(
				"<span class='error'>Seleccione USO</span>");
		return false;
	} else {
		
		dlgRegistrarCarteraDetalle.show();
	}

};
function validacionCartera() {
	
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove();
	if ($("#frmCarteraCD\\:idCarteraUpa").val() == "") {
		$("#frmCarteraCD\\:idCarteraUpa").focus().after(
				"<span class='error'>Ingrese UPA</span>");
		return false;
	} else if ($("#frmCarteraCD\\:idCarteraContrato").val() == "") {
		$("#frmCarteraCD\\:idCarteraContrato").focus().after(
				"<span class='error'>Ingrese Contrato</span>");
		return false;
	} else if ($("#frmCarteraCD\\:idCarteraUso option:selected").val() == 0) {
		$("#frmCarteraCD\\:idCarteraUso").focus().after(
				"<span class='error'>Seleccione USO</span>");
		return false;
	} else {
		
		dlgcartera.show();
	}

};
function validarConsultaCartera(){
	
	var letras = /^[a-zA-Z]+$/;
	$(".error").remove(); 
	
		if ($("#frmCarteraCabecera2\\:idCarteraNumeroCartera").val() == "") {
			$("#frmCarteraCabecera2\\:btnEditarCarteraDetalle").focus().after(
					"<span class='error'>Seleccione registro de la Tabla</span>");
			return false;
	} else {
		funcionTabCarteraRegistro();
	}

	
}

function validarCartera() {
	
	$(".error").remove();
	
		
		if ($("#frmCarteraCabecera2\\:idCarteraNumeroCartera").val() == "") {
			$("#frmCarteraCabecera2\\:btnEditarCarteraDetalle").focus().after(
					"<span class='error'>Seleccione registro de la Tabla</span>");
			return false;
	} else {
		
		dlgcartera.show();
	}
};
function validaciondetallecartera() {
	$(".error").remove();
	if ($("#frmCarteraCDet\\:idCarteraContrato").val() == "" || $("#frmCarteraCDet\\:idCarteraContrato").val() == 0) {
		$("#frmCarteraCDet\\:idCarteraContrato").focus().after(
				"<span class='error'>Ingrese valor de Contrato valido</span>");
		return false;
} else {
		dlgRegistrarCarteraDetalle.show();
	}
	
	
}

function limpiarvalidacion() {
	if ($("#frmGeneral\\:inquiTipoPersona option:selected").val() != "0") {

		$(".error").fadeOut();
		return false;
	}
}