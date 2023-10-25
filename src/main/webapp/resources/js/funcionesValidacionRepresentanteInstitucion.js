$(document)
		.ready(
				function() {

					/** **oculta mensaje super usuarios* */
					$(
							"#frmInstitucion\\:idnombre, #frmInstitucion\\:idtipo, #frmInstitucion\\:idruc ")
							.keyup(function() {
								if ($(this).val() != "") {
									$(".error").fadeOut();
									return false;
								}
							});
					
					$(
					"#frmRepresentante\\:idnombres, #frmRepresentante\\:idapellidos, #frmRepresentante\\:idruc , #frmRepresentante\\:iddni ")
					.keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});

					$(
					"#frmCliente\\:idnombres, #frmCliente\\:idapellidos, #frmCliente\\:idruc , #frmCliente\\:iddni ")
					.keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});


					$(
					"#frmbanco\\:idcodigo, #frmbanco\\:idnombre")
					.keyup(function() {
						if ($(this).val() != "") {
							$(".error").fadeOut();
							return false;
						}
					});
				});




function validacionRepresentante() {

	$(".error").remove();

	var hasError = false;
	
	if ($("#frmRepresentante\\:idnombres").val() == "") {
		$("#frmRepresentante\\:idnombres").focus().after(
				"<span class='error'>Ingrese la nombres </span>");
		return false;
	} else if ($("#frmRepresentante\\:idapellidos").val() == "") {
		$("#frmRepresentante\\:idapellidos").focus().after(
				"<span class='error'>Ingrese apellidos</span>");
		return false;
	} else if ($("#frmRepresentante\\:idruc").val() == "") {
		$("#frmRepresentante\\:idruc").focus().after(
				"<span class='error'>Ingrese ruc </span>");
		return false;
	} else  if ($("#frmRepresentante\\:iddni").val() == "") {
		$("#frmRepresentante\\:iddni").focus().after(
		"<span class='error'>Ingrese dni </span>");
		return false;
	}
	{

		dlgRegistrarRepresentante.show();

	}

}

function validacionInstitucion() {
	$(".error").remove();

	var hasError = false;
	
	if ($("#frmInstitucion\\:idnombre").val() == "") {
		$("#frmInstitucion\\:idnombre").focus().after(
				"<span class='error'>Ingrese la nombre de representante</span>");
		return false;
	} else if ($("#frmInstitucion\\:idapellidos").val() == "") {
		$("#frmInstitucion\\:idapellidos").focus().after(
				"<span class='error'>Ingrese apellido de representante</span>");
		return false;
	} else if ($("#frmInstitucion\\:idruc").val() == "") {
		$("#frmInstitucion\\:idruc").focus().after(
				"<span class='error'>Ingrese direcci&oacuten </span>");
		return false;
	} else 
	{
		dlgRegistrarInstitucion.show();
	}

}


function validacionCliente() {
	$(".error").remove();

	var hasError = false;
	
	if ($("#frmCliente\\:idnombres").val() == "") {
		$("#frmCliente\\:idnombres").focus().after(
				"<span class='error'>Ingrese la nombre de cliente</span>");
		return false;
	} else if ($("#frmCliente\\:idapellidos").val() == "") {
		$("#frmCliente\\:idapellidos").focus().after(
				"<span class='error'>Ingrese apellido de cliente</span>");
		return false;
	} else if ($("#frmCliente\\:idruc").val() == "") {
		$("#frmCliente\\:idruc").focus().after(
				"<span class='error'>Ingrese direcci&oacuten </span>");
		return false;
	} else 
	{
		dlgRegistrarCliente.show();
	}

}


function validacionbanco() {
	$(".error").remove();

	var hasError = false;
	
	if ($("#frmbanco\\:idcodigo").val() == "") {
		$("#frmbanco\\:idcodigo").focus().after(
				"<span class='error'>Ingrese codigo de banco </span>");
		return false;
	} else if ($("#frmbanco\\:idnombre").val() == "") {
		$("#frmbanco\\:idnombre").focus().after(
				"<span class='error'>Ingrese el nombre de banco</span>");
		return false;
	}  else 
	{
		dlgRegistrarbanco.show();
	}

}

