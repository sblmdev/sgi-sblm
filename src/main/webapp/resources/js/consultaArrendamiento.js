	
$(function() {
	$("#contentTabConsultaArrendamientoUpa").hide();
	$("#contentTabConsultaArrendamientoInquilino").hide();
	$("#contentTabConsultaArrendamientoContrato").show();
	
	$("#contentContratoSubTabUpa").show();
	$("#contentContratoSubTabInquilino").hide();
	$("#contentContratoSubTabContrato").hide();
	
	
	$("#botonContratoSubTabUpa").attr('style', 'background-color: #328CCB');
	$("#botonContratoSubTabInquilino").attr('style', 'background-color: #4ABBF4');
	$("#botonContratoSubTabContrato").attr('style', 'background-color: #4ABBF4');

});

	function funcionTabConsultaArrendamientoUpa() {
		
		$("#botonTabConsultaArrendamientoUpa").attr('style', 'background-color: #ffc600');
		$("#botonInquilinoTabConsultaArrendamiento").attr('style', 'background-color: #fed02e');
		$("#botonTabConsultaArrendamientoContrato").attr('style', 'background-color: #fed02e');
	
		valor = $("#idTab01Cobranza").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
	
		$("#contentTabConsultaArrendamientoUpa").show();
		$("#contentTabConsultaArrendamientoInquilino").hide();
		$("#contentTabConsultaArrendamientoContrato").hide();
		
		
		$("#idDivTab01Cobranza").removeAttr("visible");
	
	}

	function funcionTabConsultaArrendamientoInquilino() {
		
		$("#botonTabConsultaArrendamientoUpa").attr('style', 'background-color: #fed02e');
		$("#botonInquilinoTabConsultaArrendamiento").attr('style', 'background-color: #ffc600'); 
		$("#botonTabConsultaArrendamientoContrato").attr('style', 'background-color: #fed02e');
		
		valor = $("#idTab02Cobranza").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
	
		$("#contentTabConsultaArrendamientoUpa").hide();
		$("#contentTabConsultaArrendamientoInquilino").show();
		$("#contentTabConsultaArrendamientoContrato").hide();
		
	
	}
	function funcionTabConsultaArrendamientoContrato() {
		
		$("#botonTabConsultaArrendamientoUpa").attr('style', 'background-color: #fed02e');
		$("#botonInquilinoTabConsultaArrendamiento").attr('style', 'background-color: #fed02e');
		$("#botonTabConsultaArrendamientoContrato").attr('style', 'background-color: #ffc600');
		
		valor = $("#idTab03Cobranza").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
	
		$("#contentTabConsultaArrendamientoUpa").hide();
		$("#contentTabConsultaArrendamientoInquilino").hide();
		$("#contentTabConsultaArrendamientoContrato").show();
		
	}

	function funcionContratoSubTabUpa() {
		$("#botonContratoSubTabUpa").attr('style', 'background-color: #328CCB');
		$("#botonContratoSubTabInquilino").attr('style', 'background-color: #4ABBF4');
		$("#botonContratoSubTabContrato").attr('style', 'background-color: #4ABBF4');
		
		valor = $("#botonContratoSubTabUpa").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
	
		$("#contentContratoSubTabUpa").show();
		$("#contentContratoSubTabInquilino").hide();
		$("#contentContratoSubTabContrato").hide();
		
		$("#botonContratoSubTabUpa").removeAttr("visible");
	}
	
	
	function funcionContratoSubTabInquilino() {
	
		$("#botonContratoSubTabUpa").attr('style', 'background-color: #4ABBF4');
		$("#botonContratoSubTabInquilino").attr('style', 'background-color: #328CCB');
		$("#botonContratoSubTabContrato").attr('style', 'background-color: #4ABBF4');
		
		valor = $("#idTabRegistroConsulta").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
		$("#contentContratoSubTabUpa").hide();
		$("#contentContratoSubTabInquilino").show();
		$("#contentContratoSubTabContrato").hide();
		
	}
	
	function funcionContratoSubTabContrato() {
	
		$("#botonContratoSubTabUpa").attr('style', 'background-color: #4ABBF4');
		$("#botonContratoSubTabInquilino").attr('style', 'background-color: #4ABBF4');
		$("#botonContratoSubTabContrato").attr('style', 'background-color: #328CCB');
		
		valor = $("#idTabRegistroConsulta").text();// capturamos el valor del div
		$("#lblSubtitulo").text('' + valor);
		$("#contentContratoSubTabUpa").hide();
		$("#contentContratoSubTabInquilino").hide();
		$("#contentContratoSubTabContrato").show();
		
	}