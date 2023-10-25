package pe.gob.sblm.sgi.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Service;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.util.ExportarArchivo;

@Service(value="reporteGeneradorService")
public class ReporteGeneradorService implements IReporteGeneradorService {

	@SuppressWarnings("static-access")
	public StreamedContent generarPDF(String nombreReporte,Map<String, Object> parameters, List<?> lista, String nombrearchivo) {
		
		
		ExportarArchivo printer = new ExportarArchivo();
		StringBuilder jasperFile = new StringBuilder();
		StringBuilder reporte = new StringBuilder();
		
		
		jasperFile.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		jasperFile.append(ConstantesReporteSGI.REPORT_PATH);
		jasperFile.append(nombreReporte);
		
		
		StringBuilder temp = new StringBuilder();
		temp.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		temp.append(ConstantesReporteSGI.REPORT_PATH);
		String imagenRuta = temp.toString();		
		parameters.put("LOGO_DIR", imagenRuta);
		
		
		StringBuilder temp2 = new StringBuilder();
		temp2.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		temp2.append(ConstantesReporteSGI.REPORT_PATH);
		String subReportRuta = temp2.toString();		
		parameters.put("SUBREPORT_DIR", subReportRuta);
		
		try{
			byte[] array = printer.exportPdf(jasperFile.toString(), parameters, lista);
			InputStream stream = new ByteArrayInputStream(array);
			reporte.append(nombrearchivo);
			reporte.append(".");
			reporte.append(Constantes.EXTENSION_FORMATO_PDF);
			
			return new DefaultStreamedContent(stream,  Constantes.APPLICATION_PDF, reporte.toString());
		}catch(Exception e){
			System.out.println("ExceptioN:"+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("static-access")
	public byte[] generarPDF(String nombreReporte,Map<String, Object> parameters, List<?> lista) {
		
		
		ExportarArchivo printer = new ExportarArchivo();
		StringBuilder jasperFile = new StringBuilder();
		StringBuilder reporte = new StringBuilder();
		
		
		jasperFile.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		jasperFile.append(ConstantesReporteSGI.REPORT_PATH);
		jasperFile.append(nombreReporte);
		
		
		StringBuilder temp = new StringBuilder();
		temp.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		temp.append(ConstantesReporteSGI.REPORT_PATH);
		String imagenRuta = temp.toString();		
		parameters.put("LOGO_DIR", imagenRuta);
		
		
		StringBuilder temp2 = new StringBuilder();
		temp2.append(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"));
		temp2.append(ConstantesReporteSGI.REPORT_PATH);
		String subReportRuta = temp2.toString();		
		parameters.put("SUBREPORT_DIR", subReportRuta);
		
		try{
			byte[] array = printer.exportPdf(jasperFile.toString(), parameters, lista);
			return array;
		}catch(Exception e){
			System.out.println("ExceptioN:"+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
}
