package pe.gob.sblm.sgi.service;

import java.util.List;
import java.util.Map;

import org.primefaces.model.StreamedContent;


public interface IReporteGeneradorService {

	public StreamedContent generarPDF(String nombreReporte, Map<String, Object> parameters,List<?> lista,String nombrearchivo);
	public byte[] generarPDF(String nombreReporte, Map<String, Object> parameters,List<?> lista);
	
}
