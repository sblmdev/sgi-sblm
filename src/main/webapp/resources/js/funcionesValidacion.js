$(document)
		.ready(
				function() {
					var emailreg = /^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$/;

					$("#frmFormuPaginas\\:btnregistroPagina").click(function() {
					});

					/** **oculta mensaje pagina* */
					$("#frmFormuPaginas\\:cbxEstadoPagina").change(function() {
						if ($(this).val() != '0') {
							$(".error").fadeOut();
							return false;
						}
					});

					$(
							"#frmFormuPaginas\\:txtnombrePagina , #frmFormuPaginas\\:txtDescripcionPagina  ")
							.keyup(function() {
								if ($(this).val() != "") {
									$(".error").fadeOut();
									return false;
								}
							});

					/** **oculta mensaje super usuarios* */
					$(
							"#formSuperUsuarios\\:idUsuario, #formSuperUsuarios\\:idApellidos , #formSuperUsuarios\\:idNombre  , #formSuperUsuarios\\:idCargo , #formSuperUsuarios\\:idEmail , #formSuperUsuarios\\:idNacimiento ")
							.keyup(function() {
								if ($(this).val() != "") {
									$(".error").fadeOut();
									return false;
								}
							});

					$(".email").keyup(
							function() {
								if ($(this).val() != ""
										&& emailreg.test($(this).val())) {
									$(".error").fadeOut();
									return false;
								}
							});

					/** **oculta mensaje modulo* */
					$("#frmModulo\\:txtModulo").keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});
					$("#frmModulo\\:cbxEstadoModulo").change(function() {

						if ($(this).val() != '0') {
							$(".error").fadeOut();
							return false;
						}
					});

					/** **oculta mensaje tipocambio* */
					$("#frmTipoCambio\\:idTipoCambio").keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});
					/** **oculta mensaje Perfil* */
					$(
							"#frmRegPerfil\\:txtPerfil, #frmRegPerfil\\:txtDescripcion")
							.keyup(function() {
								if ($(this).val() != "") {
									$(".error").fadeOut();
									return false;
								}
							});
					/** **oculta mensaje Perfil agregar modulos* */

					$("#frmRegPerfil\\:cbxModPerfilmodulo").change(function() {
						if ($(this).val() != '0') {
							$(".error").fadeOut();
							return false;
						}
					});
					/** **oculta mensaje Mantenimientos* */
					$("#frmFormuPaginas\\:iddescripcion").keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});

				});

/** *********validacion modulo************* */

function validacioModulo() {

	$(".error").remove();
	if ($("#frmModulo\\:txtModulo").val() == "") {
		$("#frmModulo\\:txtModulo").focus().after(
				"<span class='error'>Ingrese el modulo</span>");
		return false;
	} else if ($("#frmModulo\\:cbxEstadoModulo option:selected").val() == 0) {
		$("#frmModulo\\:cbxEstadoModulo").focus().after(
				"<span class='error'>Seleccione Estado</span>");
		return false;
	} else {
		dlgRegistrarModulo.show();
	}

};
/** *********validacion pagina************* */

function validacioPagina() {

	$(".error").remove();

	if ($("#frmFormuPaginas\\:txtnombrePagina").val() == "") {

		$("#frmFormuPaginas\\:txtnombrePagina").focus().after(
				"<span class='error'>Ingrese el  nombre pagina</span>");
		return false;
	} else if ($("#frmFormuPaginas\\:txtDescripcionPagina").val() == "") {
		$("#frmFormuPaginas\\:txtDescripcionPagina").focus().after(
				"<span class='error'>Seleccione Descripcion Pagina</span>");
		return false;
	} else if ($("#frmFormuPaginas\\:cbxEstadoPagina option:selected").val() == "sa") {
		$("#frmFormuPaginas\\:cbxEstadoPagina").focus().after(
				"<span class='error'>Seleccione Estado Pagina</span>");
		return false;
	} else if ($("#frmFormuPaginas\\:cbxModuPagina option:selected").val() == 0) {
		$("#frmFormuPaginas\\:cbxModuPagina").focus().after(
				"<span class='error'>Seleccione Modulo</span>");
		return false;
	} else {
		dlgRegistrarPagina.show();
	}

};
/** *********validacion tipo cambio************* */

function validacioTipoCambio() {

	$(".error").remove();
	if ($("#frmTipoCambio\\:idTipoCambio").val() == "") {
		$("#frmTipoCambio\\:idTipoCambio").focus().after(
				"<span class='error'>Ingrese el tipo de cambio</span>");
		return false;
	} else if ($("#frmTipoCambio\\:idTipoCambio").val() < 1) {
		$("#frmTipoCambio\\:idTipoCambio").focus().after(
				"<span class='error'>No se aceptan valores negativos</span>");
		return false;
	} else if (isNaN($("#frmTipoCambio\\:idTipoCambio").val())) {
		$("#frmTipoCambio\\:idTipoCambio").focus().after(
				"<span class='error'>Solo se aceptan numeros</span>");
		return false;
	} else {
		dlgRegistrarTipoCambio.show();
	}

};

/** *************FIN************* */

/** *********validacion perfil************* */

function validacionPerfil() {

	$(".error").remove();
	if ($("#frmRegPerfil\\:txtPerfil").val() == "") {
		$("#frmRegPerfil\\:txtPerfil").focus().after(
				"<span class='error'>Ingrese el nombre del perfil</span>");
		return false;

	} else if ($("#frmRegPerfil\\:txtDescripcion").val() == "") {
		$("#frmRegPerfil\\:txtDescripcion").focus().after(
				"<span class='error'>Ingrese descripcion del perfil</span>");
		return false;
	} else if ($("#frmRegPerfil\\:cbxestadoPerfil option:selected").val() == 0) {
		$("#frmRegPerfil\\:cbxestadoPerfil").focus().after(
				"<span class='error'>Seleccione Estado</span>");
		return false;
	} else {
		dlgRegistrarPerfil.show();
	}

};
/** **validacion perfil agregar modulos* */
function validacionPerfilAgregarModulo() {
	$(".error").remove();
	if ($("#frmRegPerfil\\:cbxModPerfilmodulo option:selected").val() == 0) {
		$("#frmRegPerfil\\:cbxModPerfilmodulo").focus().after(
				"<span class='error'>Seleccione modulo </span>");

		return false;
	} else {

		return true;
	}

};
/** *************FIN************* */

/** *********validacion Mantenimientos************* */

function validacionMantenimientos() {

	$(".error").remove();
	if ($("#frmFormuPaginas\\:iddescripcion").val() == "") {
		$("#frmFormuPaginas\\:iddescripcion").focus().after(
				"<span class='error'>Ingrese la descripcion</span>");
		return false;
	} else {
		dlgRegistrarPagina.show();
	}

};

/** *************FIN************* */
// FRANCO
function validacioneEditarUsuario() {

	$(".error").remove();

	var hasError = false;
	var passwordVal = $("#formEditarUsuario\\:pwd1").val();
	var checkVal = $("#formEditarUsuario\\:pwd2").val();

	if ($("#formEditarUsuario\\:idUsuario").val() == "") {
		$("#formEditarUsuario\\:idUsuario").focus().after(
				"<span class='error'>Ingrese el  nombre de usuario</span>");
		return false;
	} else if ($("#formEditarUsuario\\:idApellidos").val() == "") {
		$("#formEditarUsuario\\:idApellidos").focus().after(
				"<span class='error'>Ingrese apellidos</span>");
		return false;
	} else if ($("#formEditarUsuario\\:idNombre").val() == "") {
		$("#formEditarUsuario\\:idNombre").focus().after(
				"<span class='error'>Ingrese Nombres</span>");
		return false;
	} else if ($("#formEditarUsuario\\:idEmail").val() == "") {
		$("#formEditarUsuario\\:idEmail").focus().after(
				"<span class='error'>Ingrese E-mail</span>");
		return false;
	} else

	// if( $("#formEditarUsuario\\:idNacimiento").val() == ""){
	// $("#formEditarUsuario\\:idNacimiento").focus().after("<span
	// class='error'>Ingrese Fecha Nacimiento</span>");
	// return false;
	// }else
	if ($("#formEditarUsuario\\:idCargo").val() == "") {
		$("#formEditarUsuario\\:idCargo").focus().after(
				"<span class='error'>Ingrese nombre de Cargo</span>");
		return false;
	} else if ($("#formEditarUsuario\\:pwd1").val() == ""
			&& $("#formEditarUsuario\\:pwd2").val() == "") {

		widgetDlgConfirmTypePass.show();

	}

	else

	{
		if ($("#formEditarUsuario\\:pwd2").val() == ""
				|| $("#formEditarUsuario\\:pwd1").val() == "") {
			$("#formEditarUsuario\\:pwd2").focus().after(
					"<span class='error'>Repita Contrase&ntilde;a</span>");
			return false;
		} else if (passwordVal != checkVal) {
			$("#formEditarUsuario\\:pwd2")
					.after(
							'<span class="error">Contrase&ntilde;as no coinciden</span>');
			hasError = true;
		} else if (checkVal.length < 5) {
			$("#formEditarUsuario\\:pwd2")
					.focus()
					.after(
							"<span class='error'> Contrase&ntilde;a minima de 5 caracteres </span>");
			return false;
		} else {
			widgetDlgConfirmTypePass.show();
		}

	}

}
function validarPass() {
	if ($("#formEditarUsuario\\:idContrasenia").val() != $(
			"#formEditarUsuario\\:idContraseniaIngresada").val()) {
		widgetDlgContraseniaIncorrecta.show();
		widgetDlgConfirmGuardar.hide();
	} else {
		widgetDlgConfirmGuardar.show();
	}

}

function validacionFranco() {
	
	$(".error").remove();

	var hasError = false;
	var passwordVal = $("#formSuperUsuarios\\:pwd1").val();
	var checkVal = $("#formSuperUsuarios\\:pwd2").val();

	if ($("#formSuperUsuarios\\:idNombre").val() == "") {
		$("#formSuperUsuarios\\:idNombre").focus().after(
				"<span class='error'>Ingrese Nombres</span>");
		return false;
	} else if ($("#formSuperUsuarios\\:idApellidos").val() == "") {
		$("#formSuperUsuarios\\:idApellidos").focus().after(
				"<span class='error'>Ingrese apellidos</span>");
		return false;
	} else if ($("#formSuperUsuarios\\:idUsuario").val() == "") {
		$("#formSuperUsuarios\\:idUsuario").focus().after(
				"<span class='error'>Ingrese el  nombre de usuario</span>");
		return false;
	
	
	} else if ($("#formSuperUsuarios\\:idEmail").val() == "") {
		$("#formSuperUsuarios\\:idEmail").focus().after(
				"<span class='error'>Ingrese E-mail</span>");
		return false;
	} else

	 if( $("#formSuperUsuarios\\:idNacimiento_input").val() == ""){
	 $("#formSuperUsuarios\\:idNacimiento_input").focus().after(
			 "<span class='error'>Ingrese Fecha Nacimiento</span>");
	 return false;
	 }else
	if ($("#formSuperUsuarios\\:idCargo").val() == "") {
		$("#formSuperUsuarios\\:idCargo").focus().after(
				"<span class='error'>Ingrese nombre de Cargo</span>");
		return false;
	} else if ($("#formSuperUsuarios\\:idareauser option:selected").val() == 0) {
		$("#formSuperUsuarios\\:idareauser").focus().after(
					"<span class='error'>Seleccione Area</span>");
			return false;
	}else {

		widgetDlgConfirmGuardar.show();

	}

}
