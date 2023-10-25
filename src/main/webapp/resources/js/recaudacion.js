$(function() {
			$("#idpanel_recauAutovaluoAtendidos").hide();
			$("#idpanel_recauAutovaluoPendientes").show();
			
			$("#idpanel_recauAutovaluoRegistro").show();
			$("#idpanel_recauAutovaluoCartas").hide();
			
			$("#idsubpanel_recauAutovaluoRegistro").show();
			$("#idsubpanel_recauAutovaluoConsultaArbitrio").hide();
			
//			$("#idDivCartas").attr('style', 'background-color: #ffc600');
//			$("#idDivRegistro").attr('style', 'background-color: #fed02e');
//			$("#idDivReporte").attr('style', 'background-color: #fed02e');
			
			
		});

function tabAtendidos_recauAutovaluo() {
	$("#idDivAtendidos").attr('style', 'background-color: #ffc600');
	$("#idDivPendientes").attr('style', 'background-color: #fed02e');
	
	valor = $("#idDivAtendidos").text();// capturamos el valor del div
	$("#arren_lblSubtituloDesc").text(valor);

	$("#idpanel_recauAutovaluoAtendidos").show();
	$("#idpanel_recauAutovaluoPendientes").hide();
	

//	$("#idDivAtendidos").removeAttr("visible");

}

function tabPendientes_recauAutovaluo() {

	$("#idDivAtendidos").attr('style', 'background-color: #fed02e');
	$("#idDivPendientes").attr('style', 'background-color: #ffc600');
	
	valor = $("#idDivPendientes").text();// capturamos el valor del div
	$("#arren_lblSubtituloDesc").text(valor);

	$("#idpanel_recauAutovaluoAtendidos").hide();
	$("#idpanel_recauAutovaluoPendientes").show();

}



function tabCartas_recauAutovaluoArbitrio() {
	$("#idDivCartas").attr('style', 'background-color: #fed02e');
	$("#idDivRegistro").attr('style', 'background-color: #ffc600');
	
	valor = $("#idDivCartas").text();// capturamos el valor del div
	$("#arren_lblSubtituloDesc").text(valor);

	$("#idpanel_recauAutovaluoRegistro").show();
	$("#idpanel_recauAutovaluoCartas").hide();
	

//	$("#idDivAtendidos").removeAttr("visible");

}

function tabRegistro_recauAutovaluoArbitrio() {

	$("#idDivCartas").attr('style', 'background-color: #fed02e');
	$("#idDivRegistro").attr('style', 'background-color: #ffc600');
	
	valor = $("#idDivRegistro").text();// capturamos el valor del div
	$("#arren_lblSubtituloDesc").text(valor);

	$("#idpanel_recauAutovaluoRegistro").hide();
	$("#idpanel_recauAutovaluoCartas").show();

}

function subTabRegistro_recauAutovaluo_Arbitrio() {

	$("#idDivR").attr('style', 'background-color: #4ABBF4');
	$("#idDiv").attr('style', 'background-color: #328CCB');
	

	$("#idsubpanel_recauAutovaluoConsultaArbitrio").hide();
	$("#idsubpanel_recauAutovaluoRegistro").show();

}

function subTabConsulta_recauAutovaluo_Arbitrio() {
	$("#idDivR").attr('style', 'background-color: #328CCB');
	$("#idDiv").attr('style', 'background-color: #4ABBF4');
	

	$("#idsubpanel_recauAutovaluoConsultaArbitrio").show();
	$("#idsubpanel_recauAutovaluoRegistro").hide();
	

}

function subTabRegistro_recauAutovaluo_Carta() {

	$("#idDivR").attr('style', 'background-color: #4ABBF4');
	$("#idDiv").attr('style', 'background-color: #328CCB');
	

	$("#idsubpanel_recauAutovaluoConsultaArbitrio").hide();
	$("#idsubpanel_recauAutovaluoRegistro").show();

}

function subTabConsulta_recauAutovaluo_Carta() {
	$("#idDivR").attr('style', 'background-color: #328CCB');
	$("#idDiv").attr('style', 'background-color: #4ABBF4');
	

	$("#idsubpanel_recauAutovaluoConsultaArbitrio").show();
	$("#idsubpanel_recauAutovaluoRegistro").hide();
	

}