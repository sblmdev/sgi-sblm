package pe.gob.sblm.sgi.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.apache.log4j.Logger;

public class ExportarArchivo implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Logger depurador = Logger.getLogger(ExportarArchivo.class.getName());

    public static byte[] exportPdf(String jasperFile, Map<String, Object> parameters, List<?> dataList) throws Exception {
        JasperReport report = JasperCompileManager.compileReport(jasperFile);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(dataList));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRPdfExporter jRPdfExporter = new JRPdfExporter();
        
        jRPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        
        jRPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        
        jRPdfExporter.exportReport();
        byte[] bytes = byteArrayOutputStream.toByteArray(); 
        jRPdfExporter = null;
        return bytes;
    }




}
