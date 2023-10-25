$(function() {
			var argumentos;

			$("#idDivSeguimientoSeg").show();
			$("#idDivConsultaSeg").hide();
			$("#idDivAlertaSeg").hide();
			$("#idDivReporteSeg").hide();

			$(".demo").hide();
			

			// CIERRA BLOQUE QUE OCULTA
		});
		///cmbiado de tabs

		function tabSeguimientoSeg() {
			$("#idSeguimientoSeg").attr('style', 'background-color: #ffc600');
			$("#idConsultaSeg").attr('style', 'background-color: #fed02e');
			$("#idAlertaSeg").attr('style', 'background-color: #fed02e');
			$("#idReporteSeg").attr('style', 'background-color: #fed02e');
			valor = $("#idSeguimientoSeg").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivSeguimientoSeg").show();
			$("#idDivConsultaSeg").hide();
			$("#idDivAlertaSeg").hide();
			$("#idDivReporteSeg").hide();

			
			//$("#idDivSeguimientoSeg").removeAttr("visible");

		}
function tabConsultaSeg() {
			
			$("#idSeguimientoSeg").attr('style', 'background-color: #fed02e');
			$("#idConsultaSeg").attr('style', 'background-color: #ffc600');
			$("#idAlertaSeg").attr('style', 'background-color: #fed02e');
			$("#idReporteSeg").attr('style', 'background-color: #fed02e');
			valor = $("#idConsultaSeg").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivSeguimientoSeg").hide();
			$("#idDivConsultaSeg").show();
			$("#idDivAlertaSeg").hide();
			$("#idDivReporteSeg").hide();

		}
		function tabAlertaSeg() {
			
			$("#idSeguimientoSeg").attr('style', 'background-color: #fed02e');
			$("#idConsultaSeg").attr('style', 'background-color: #fed02e');
			$("#idAlertaSeg").attr('style', 'background-color: #ffc600');
			$("#idReporteSeg").attr('style', 'background-color: #fed02e');
			valor = $("#idAlertaSeg").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivSeguimientoSeg").hide();
			$("#idDivConsultaSeg").hide();
			$("#idDivAlertaSeg").show();
			$("#idDivReporteSeg").hide();

		}
		function tabReporteSeg() {
			$("#idSeguimientoSeg").attr('style', 'background-color: #fed02e');
			$("#idConsultaSeg").attr('style', 'background-color: #fed02e');
			$("#idAlertaSeg").attr('style', 'background-color: #fed02e');
			$("#idReporteSeg").attr('style', 'background-color: #ffc600');
			valor = $("#idReporteSeg").text();//capturamos el valor del div
			$("#lblSubtitulo").text(valor);

			$("#idDivSeguimientoSeg").hide();
			$("#idDivConsultaSeg").hide();
			$("#idDivAlertaSeg").hide();
			$("#idDivReporteSeg").show();
		}
		//franco
		
		function bloquearxPermiso(){
			
			var idtab = $("input[name*='idinputConsultado']").val();
			
			switch (idtab)
			{
			case 'tab1':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab1").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
	
			  break;
			case 'tab2':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab2").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
			  break;
			case 'tab3':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab3").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
			  break;
			case 'tab4':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab4").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
			  break;
			case 'tab5':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab5").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
			  break;
			case 'tab6':
				
			var valorInput=$("input[name*='idinputTab1']").val();
			
			if (valorInput!='true') {
				$("#frmSegContrato\\:idmayor\\:tab6").block({ 
	                message: null, 
	                css: { opacity: 0 } 
	            });
			}
			  break;
			case 'tab7':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab7").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
			  break;
			case 'tab8':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab8").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab9':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab9").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab10':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab10").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab11':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab11").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab12':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab12").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab13':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab13").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab14':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab14").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab15':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab15").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab16':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab16").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
			case 'tab17':
				var valorInput=$("input[name*='idinputTab1']").val();
				
				if (valorInput!='true') {
					$("#frmSegContrato\\:idmayor\\:tab17").block({ 
		                message: null, 
		                css: { opacity: 0 } 
		            });
				}
				  break;
	  
			}
		}
		
		function bloquearElement2(){
			 $("#frmSegContrato\\:idmayor\\:tab3").block({ message: null }); 
		}