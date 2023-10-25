
$(function() {
			var argumentos;

			$("#idDivDerivacion").show();
			$("#idDivDespacho").hide();
			$("#idDivConsultas").hide();

			$(".demo").hide();
			

			// CIERRA BLOQUE QUE OCULTA
		});
		///cmbiado de tabs

		function tabDerivacion() {
			$("#idDerivacion").attr('style', 'background-color: #ffc600');
			$("#idDespacho").attr('style', 'background-color: #fed02e');
			$("#idConsultas").attr('style', 'background-color: #fed02e');
			valor = $("#idDerivacion").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivDerivacion").show();
			$("#idDivDespacho").hide();
			$("#idDivConsultas").hide();

			//  			alert($("#pnDerivacion").text());
			// 			alert($("#pnDerivacion").attr("visible"));
			$("#idDivDerivacion").removeAttr("visible");

		}
		function tabDespacho() {
			
			$("#idDerivacion").attr('style', 'background-color: #fed02e');
			$("#idDespacho").attr('style', 'background-color: #ffc600');
			$("#idConsultas").attr('style', 'background-color: #fed02e');
			valor = $("#idDespacho").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivDerivacion").hide();
			$("#idDivDespacho").show();
			$("#idDivConsultas").hide();

		}
		function tabConsultas() {
			$("#idDespacho").attr('style', 'background-color: #fed02e');
			$("#idDerivacion").attr('style', 'background-color: #fed02e');
			$("#idConsultas").attr('style', 'background-color: #ffc600');
			valor = $("#idConsultas").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivDerivacion").hide();
			$("#idDivDespacho").hide();
			$("#idDivConsultas").show();
		}
		
		//abre el BLOQUE
		function mostrarGadget() {
			$(".demo").show("slide", {
				direction : "left"
			}, 1000);
			return false;
		};

		//oculta BLOQUE
		function ocultarGadget() {
			$(".demo").hide("slide", {
				direction : "left"
			}, 1000);
			return false;
		};
		