package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

//import com.sun.el.parser.ParseException;


import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInmuebleService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;

@ManagedBean(name= "inmuebleReporteMB")
@ViewScoped
public class InmuebleReporteManagedBean extends BaseController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StreamedContent report;
	
	@ManagedProperty(value = "#{inmuebleService}")
	private transient IInmuebleService inmuebleService;
	
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{inmuebleMB}")
	InmuebleManagedBean inmuebleMB;
	
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;
	
	private List<InmuebleBean> listainmueblematriz = new ArrayList<InmuebleBean>();
	

	@PostConstruct
	public void init(){
		
		
	}
	
	public void generarReporte(String tipoarchivo) {
		
	try{
	   
		List<InmuebleBean> listaInmuebleBean=new ArrayList<InmuebleBean>();
		String  reportPath=  ConstantesReporteSGI.REP_INMUEBLE;
		Map<String, Object> parameters = new HashMap<String, Object>();
		listaInmuebleBean = inmuebleService.obtenerCondicion();		   
		parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		parameters.put("CONDICION","INMUEBLE MATRIZ AL "+FuncionesHelper.fechaToString(new Date()));
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaInmuebleBean,"Reporte_Inmueble_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Inmueble(listaInmuebleBean);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
	public void generarReporteInmueble(String tipoarchivo) {
		
	try{
	   
		List<InmuebleBean> listaInmuebleBean=new ArrayList<InmuebleBean>();
		String  reportPath=  ConstantesReporteSGI.REP_INMUEBLE;
		Map<String, Object> parameters = new HashMap<String, Object>();
		listaInmuebleBean = listainmueblematriz;		   
		parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		parameters.put("CONDICION","INMUEBLE MATRIZ AL "+FuncionesHelper.fechaToString(new Date()));
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaInmuebleBean,"Reporte_Inmueble_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Inmueble(listaInmuebleBean);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
	
	public void buscarInmuebleMatriz(){
		listainmueblematriz.clear();
		String claveinmueble=null;
		if(inmuebleMB.getInmuebleSeleccionado()!=null){
			claveinmueble=inmuebleMB.getInmuebleSeleccionado().getClave();
		}
		listainmueblematriz=inmuebleService.listarInmueblematriz(claveinmueble, inmuebleMB.getDepa(), inmuebleMB.getProv(), inmuebleMB.getDist(), inmuebleMB.getTtitularidad());
		System.out.println("oka");
	}
public void XLS_Inmueble(List<InmuebleBean> lista){

		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Inmueble Matriz");
        sheet.setColumnWidth(0, 2500); 
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 2500);
        sheet.setColumnWidth(5, 2500);
        sheet.setColumnWidth(6, 2500);
//        sheet.setColumnWidth(8, 3000);
//        sheet.setColumnWidth(9, 3000);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        String[] headers = new String[] { "CLAVE", "DIRECCION","DISTRITO","PROVINCIA","DEPARTAMENTO","NOMBRE TITULARIDAD","TIPO DE TITULARIDAD","MONUMENTO HISTORICO","TIPO DE MONUMENTO HISTORICO","CODIGO DE PREDIO","FECHA INSCRIPCION","ASIENTO","FOJAS","TOMO","FICHA DE PARTIDA","PARTIDA ELECTRONICA","ORIGEN DE DOMINIO","DECLARATORIA DE FABRICA","INDEPENDIZACION","GRAVAMEN","CARGAS","SANEAMIENTO","MATERIAL PREDOMINANTE","AÑO DE CONSTRUCCION","NUM DE PISOS","AREA DE TERRENO (M2)","AREA CONSTRUIDA(M2)","PLANO UBICACION","MEMORIA DESCRIPTIVA","FOTOGRAFIAS","TASACION","LOCALIZACION LATITUD","LOCALIZACION LONGITUD","CONDICION"};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
            Row row = sheet.createRow(rownum++);
           InmuebleBean bean= new InmuebleBean();
            bean= lista.get(i);
                
                for (int j = 0; j < 34; j++) {
                	Cell cell = row.createCell(j);
                	
                	if (j==0)
                		cell.setCellValue((String)bean.getClave());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
//                	 	else if (j==2)
//                		 cell.setCellValue((String)bean.getCodigopredio());
                		 	else if (j==2)
                		 		cell.setCellValue((String)bean.getDistrito());
                			 	else if (j==3) 
                			 		cell.setCellValue((String)bean.getProvincia());
                			     	else if (j==4)
                			     		cell.setCellValue((String)bean.getDepartamento());
                			     		else if (j==5)
                			     		cell.setCellValue((String)bean.getNombretitularidad());
                			     	else if (j==6)
                			     		cell.setCellValue((String)bean.getTipotitularidad());
                			     	else if (j==7){              			     	
    				 					if(bean.getSimonumentohistorico()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==8){
                			     		if(bean.getTipomonumentohistorico()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getTipomonumentohistorico());
                			     		}
                			     	}
                			     		
                			     	else if (j==9){
                			     		if(bean.getCodigopredio()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getCodigopredio());
                			     		}
                			     	}
                			     		
                			     	else if (j==10) {               			     		
                						if(bean.getFecinscripcionregistral()==null){
                							cell.setCellValue("");
                							}else{
                									cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFecinscripcionregistral()));	
                					     }}	
                			     	else if (j==11){
                			     		if(bean.getAsientoregistral()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getAsientoregistral());
                			     		}
                			     	}
                			     		
                			     	else if (j==12){
                			     		if(bean.getFojasregistral()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getFojasregistral());
                			     		}
                			     	}
                			     		
                			     	else if (j==13){
                			     		if(bean.getTomoregistral()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getTomoregistral());
                			     		}
                			     	}
                			     		
                			     	else if (j==14){
                			     		if(bean.getFicharegistral()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getFicharegistral());
                			     		}
                			     	}
                			     		
                			     	else if (j==15){
                			     		if(bean.getPartidaelectronicaregistral()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getPartidaelectronicaregistral());
                			     		}
                			     	}
                			     		
                			     	else if (j==16){
                			     		if(bean.getDescripcionorigendominio()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getDescripcionorigendominio());
                			     		}
                			     	}
                			     		
                			     	else if (j==17){              			     	
    				 					if(bean.getSideclaratoriafabrica()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==18){              			     	
    				 					if(bean.getSiindependizacion()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==19){              			     	
    				 					if(bean.getSigravamen()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==20){              			     	
    				 					if(bean.getSimandascargas()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==21){              			     	
    				 					if(bean.getSisaneado()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==22)
                			     	{
                			     		if(bean.getMaterialpredominante()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue((String)bean.getMaterialpredominante());
                			     		}
                			     	}
                			     		
                			     	else if (j==23){
                			     		if(bean.getAnioconstruccion()==null){
                			     			cell.setCellValue("");
                			     		}else{
                			     			cell.setCellValue(bean.getAnioconstruccion());
                			     		}
                			     	}
                			     		
                			     	else if (j==24)
                			     		cell.setCellValue(bean.getNropisos());
                			     	else if (j==25){
                			     	   //cell.setCellValue((Double)Double.parseDouble(bean.getAreaterreno()));
                			     		cell.setCellValue((String)(bean.getAreaterreno()));

                			     	}
                			     	else if (j==26)
                			     		//cell.setCellValue((Double)Double.parseDouble(bean.getAreaconstruida()));
                						cell.setCellValue((String)(bean.getAreaconstruida()));
//                			     	else if (j==27){
//                			     		if(bean.getPartidaelectronicaregistral()==null){
//                			     			cell.setCellValue("");
//                			     		}else{
//                			     		cell.setCellValue((String)bean.getPartidaelectronicaregistral());
//                			     		}
//                			     	}
                			     	else if (j==27){              			     	
    				 					if(bean.getSiplanoubicacion()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==28){              			     	
    				 					if(bean.getSimemoriadescriptiva()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==29){              			     	
    				 					if(bean.getSifotografias()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==30){              			     	
    				 					if(bean.getSitasacion()){
    				 						cell.setCellValue("Si");
    				 					}else{
    				 						cell.setCellValue("No");
    				 					}
    				 				}
                			     	else if (j==31)
                			     		cell.setCellValue((String)bean.getLatitudlocalizacion());
                			     	else if (j==32)
                			     		cell.setCellValue((String)bean.getLongitudlocalizacion());
                			     	else if (j==33) {
                			     		if(bean.getCondicion()==null){
                			     	
                 			 		   cell.setCellValue("");
                			     		}else {
                			     			cell.setCellValue((String)bean.getCondicion());
                			     		}
                			     	}
                
                }
		                				 
	                				 				
                }
        try {
            //FileOutputStream out =  new FileOutputStream(new File("C:\\Contratos.xls"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Excel written successfully..");
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	try {
        		workbook.write(bos);
        	} finally {
        	    bos.close();
        	}
            InputStream stream; 
//          stream = new FileInputStream("c:\\Contratos.xls");
            stream = new ByteArrayInputStream(bos.toByteArray());
            report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Inmueble_Matriz"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 
	public StreamedContent getReport() {
		return report;
	}

	public void setReport(StreamedContent report) {
		this.report = report;
	}

	public IInmuebleService getInmuebleService() {
		return inmuebleService;
	}

	public void setInmuebleService(IInmuebleService inmuebleService) {
		this.inmuebleService = inmuebleService;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public IReporteGeneradorService getReporteGeneradorService() {
		return reporteGeneradorService;
	}

	public void setReporteGeneradorService(
			IReporteGeneradorService reporteGeneradorService) {
		this.reporteGeneradorService = reporteGeneradorService;
	}

	public InmuebleManagedBean getInmuebleMB() {
		return inmuebleMB;
	}

	public void setInmuebleMB(InmuebleManagedBean inmuebleMB) {
		this.inmuebleMB = inmuebleMB;
	}

	public List<InmuebleBean> getListainmueblematriz() {
		return listainmueblematriz;
	}

	public void setListainmueblematriz(List<InmuebleBean> listainmueblematriz) {
		this.listainmueblematriz = listainmueblematriz;
	}
	
}
