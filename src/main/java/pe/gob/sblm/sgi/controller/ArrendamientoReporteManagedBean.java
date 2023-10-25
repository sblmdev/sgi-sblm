package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.bean.InquilinoBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Tipopersona;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipozona;
import pe.gob.sblm.sgi.entity.Ubigeo;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInmuebleService;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

@ManagedBean(name = "arrendamientoReporteMB")
@ViewScoped
public class ArrendamientoReporteManagedBean extends BaseController implements Serializable {
	
	
	
	/**Grafica especifica de recaudacion de todos los meses por anio**/
	private String tipSelectedCondicionFinalizado="1";
	private Date desdeCartera,hastaCartera,desdeCarteraTodo,hastaCarteraTodo,desdeContratoFinalizado,hastaContratoFinalizado;
	/**variables para consulta de contrato */
	private Date desdeInicioContrato, hastaInicioContrato, desdeFinContrato,hastaFinContrato;
	
	
	private StreamedContent report;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	
	@ManagedProperty(value = "#{carteraService}")
	private transient IRecaudacionCarteraService carteraService;
	
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;

	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService reporteRecaudacionService;
	
	@ManagedProperty(value = "#{inmuebleService}")
	private transient IInmuebleService inmuebleService;
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;
	
		
	CondicionBean condicionBeanSelected= new CondicionBean();
	List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
	List<String> listacondicion, listaestado,selectedCondicionSeleccionado , selectedEstadoSeleccionado ,listaDepartamentos,listaProvincias,listaDistritos;
	private String depa, prov,dist;
	private List<Upa> listaUpas= new ArrayList<Upa>();
	private Upa upa;
	private Upa upaselected = new Upa();
	private UpaBean upaBean ;
	private InmuebleBean inmuebleBean;
	private List<Uso> listaTipoUso;
	private List<Tipotitularidad> listaTipoTitularidad;
	private List<Tipozona> listaTipoZona;
	private String ubigeo;
	private Inmueblemaestro inmuebleSeleccionado,inmuebleMaestro ;
	//private List<Inmueblemaestro> listaInmuebleMaestro;
	private List<InmuebleBean> listaInmuebleMaestro;
	private Tipozona tipozona;
	private Inquilino inquilino;
	private Integer idtipopersona;
	private List<Tipopersona> listaTipoPersona;
	private Boolean sipersonanatural;
	private InquilinoBean inquilinoBean;
	List<InquilinoBean> listaInquilino ;
	
	@PostConstruct
	public void init(){
		inicializarValoresCondicionConsultaContrato();
	}
	
	public void elegirReporteTabContratoVigente(String tipo) {
		if (tipSelectedCondicionFinalizado.equals("1")) {
			
		}else if (tipSelectedCondicionFinalizado.equals("2")) {
			
		}else if(tipSelectedCondicionFinalizado.equals("3")){
			
		}
	}
	
	public void generarReporteContratoVigente(String tipoarchivo) {
		
//			List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
//			listaContratoBean = contratoService.obtenerCondicion("Contrato","VIGENTE","todos");
//			
//		   String  reportPath=  "Arrendamiento//Condicion";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("CONDICION","CONTRATOS VIGENTES AL "+FuncionesHelper.fechaToString(new Date()));
//		   
//		   if (tipoarchivo.equals("pdf")) {
//			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Vigentes_"+FuncionesHelper.fechaToString(new Date()));  
//			} else {
//				XLS_Contrato(listaContratoBean);
//			}
			
		   
			List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
			 String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_VIGENTES;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			if (desdeCartera==null || hastaCartera==null) {
				listaContratoBean = contratoService.obtenerCondicion("Contrato","VIGENTE","todos");
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   parameters.put("CONDICION","CONTRATOS VIGENTES AL "+FuncionesHelper.fechaToString(new Date()));
			}else{
				Calendar hasta = Calendar.getInstance();
				hasta.setTime(hastaCartera);
				//hasta.add(Calendar.DAY_OF_YEAR, 1);
				
				Calendar desde = Calendar.getInstance();
				desde.setTime(desdeCartera);
			listaContratoBean = contratoService.obtenerCondicionxFecha("Contrato","VIGENTE","todos",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
			 
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("CONDICION","CONTRATOS VIGENTES DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
			}
//		   String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_VIGENTES;
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("CONDICION","CONTRATOS VIGENTES AL "+FuncionesHelper.fechaToString(new Date()));
		   
		   if (tipoarchivo.equals("pdf")) {
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Vigentes_"+FuncionesHelper.fechaToString(new Date()));  
			} else {
				XLS_Contrato(listaContratoBean);
			}
		   
	}
	public void generarReporteContratoVigenteFinalizado(String tipoarchivo) {
			   
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_VIGENTES;
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (desdeCarteraTodo==null || hastaCarteraTodo==null) {
			listaContratoBean = contratoService.obtenerCondicion("Contrato","todos","todos");
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("CONDICION","CONTRATOS AL "+FuncionesHelper.fechaToString(new Date()));
		}else{
			Calendar hasta = Calendar.getInstance();
			hasta.setTime(hastaCarteraTodo);
			//hasta.add(Calendar.DAY_OF_YEAR, 1);
			
			Calendar desde = Calendar.getInstance();
			desde.setTime(desdeCarteraTodo);
		listaContratoBean = contratoService.obtenerCondicionxFecha("Contrato","todos","todos",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
		 
		   
		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   parameters.put("CONDICION","CONTRATOS DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
		}
//	   String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_VIGENTES;
//	   Map<String, Object> parameters = new HashMap<String, Object>();
//	   
//	   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//	   parameters.put("CONDICION","CONTRATOS VIGENTES AL "+FuncionesHelper.fechaToString(new Date()));
	   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Vigentes_Finalizados"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Contrato(listaContratoBean);
		}
	   
}
	
	public void generarReporteContratoGlobal_Vigentes(String tipoarchivo) {
		   
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		List<CondicionBean> listaContratoxCuotaBean=new ArrayList<CondicionBean>();
		String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_GLOBAL;
		Map<String, Object> parameters = new HashMap<String, Object>();
			listaContratoBean = contratoService.obtenerCondicion("Global","VIGENTE","todos");
			listaContratoxCuotaBean = contratoService.obtenerListaContratoxCuota("Contrato","VIGENTE","todos");
			if(listaContratoBean!=null && listaContratoBean.size()>0 && listaContratoxCuotaBean!=null && listaContratoxCuotaBean.size()>0){
				for(int i=0;i<listaContratoxCuotaBean.size();i++){
					for(int j=0;j<listaContratoBean.size();j++){
						if(listaContratoBean.get(j).getIdcontrato()==listaContratoxCuotaBean.get(i).getIdcontrato()){
							listaContratoBean.get(j).setFeccre(listaContratoxCuotaBean.get(i).getFeccre());
							listaContratoBean.get(j).setMontosoles(listaContratoxCuotaBean.get(i).getMontosoles());
							
						}
					}
				}
			}
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("CONDICION","CONTRATOS AL "+FuncionesHelper.fechaToString(new Date()));
		

	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"Reporte_Contrato_GLOBAL_SGI_VIGENTES_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_ContratoGlobal(listaContratoBean);
		}
	   
}  
	public void generarReporteContratoGlobal(String tipoarchivo) {
		   
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		List<CondicionBean> listaContratoxCuotaBean=new ArrayList<CondicionBean>();
		String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_GLOBAL;
		Map<String, Object> parameters = new HashMap<String, Object>();
			listaContratoBean = contratoService.obtenerCondicion("Global","todos","todos");
			listaContratoxCuotaBean = contratoService.obtenerListaContratoxCuota("Global","todos","todos");
			if(listaContratoBean!=null && listaContratoBean.size()>0 && listaContratoxCuotaBean!=null && listaContratoxCuotaBean.size()>0){
				for(int i=0;i<listaContratoxCuotaBean.size();i++){
					for(int j=0;j<listaContratoBean.size();j++){
						if(listaContratoBean.get(j).getIdcontrato()==listaContratoxCuotaBean.get(i).getIdcontrato()){
							listaContratoBean.get(j).setFeccre(listaContratoxCuotaBean.get(i).getFeccre());
							listaContratoBean.get(j).setMontosoles(listaContratoxCuotaBean.get(i).getMontosoles());
							
						}
					}
				}
			}
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("CONDICION","CONTRATOS AL "+FuncionesHelper.fechaToString(new Date()));
		

	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"Reporte_Contrato_GLOBAL_SGI_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_ContratoGlobal(listaContratoBean);
		}
	   
} 
	public void generarReportePreContratoVigente(String tipoarchivo) {
		   
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_GLOBAL;
		Map<String, Object> parameters = new HashMap<String, Object>();
			listaContratoBean = contratoService.obtenerCondicion("Precontrato","VIGENTE","todos");

			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("CONDICION","PRE CONTRATOS AL "+FuncionesHelper.fechaToString(new Date()));
		

	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"Reporte_PreContrato_SGI_VIGENTES_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_PreContrato(listaContratoBean);
		}
	   
}  
	public void generarReporteContratoFinalizado(String tipoarchivo) {
		
//		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
//	   String  reportPath=  "Arrendamiento//Condicion";
//	   Map<String, Object> parameters = new HashMap<String, Object>();
//		   
//		if (tipSelectedCondicionFinalizado.equals("1")) {
//			listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","todos");
//			 parameters.put("CONDICION","CONTRATOS FINALIZADOS "+FuncionesHelper.fechaToString(new Date()));
//		}else if (tipSelectedCondicionFinalizado.equals("2")) {
//			listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","si");
//			 parameters.put("CONDICION","CONTRATOS FINALIZADOS SIN DEUDA AL "+FuncionesHelper.fechaToString(new Date()));
//		}else if(tipSelectedCondicionFinalizado.equals("3")){
//			listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","no");
//			 parameters.put("CONDICION","CONTRATOS FINALIZADOS CON DEUDA AL "+FuncionesHelper.fechaToString(new Date()));
//		}
//		
//	   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//	  
//	   
//	   if (tipoarchivo.equals("pdf")) {
//		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Finalizados_"+FuncionesHelper.fechaToString(new Date())); 
//		} else {
//			XLS_Contrato(listaContratoBean);
//		}
	   
	   
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		   String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_FINALIZADOS;
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   if (desdeContratoFinalizado==null || hastaContratoFinalizado==null) {
			if (tipSelectedCondicionFinalizado.equals("1")) {
				listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","todos");
				 parameters.put("CONDICION","CONTRATOS FINALIZADOS AL "+FuncionesHelper.fechaToString(new Date()));
			}else if (tipSelectedCondicionFinalizado.equals("2")) {
				listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","si");
				 parameters.put("CONDICION","CONTRATOS FINALIZADOS SIN DEUDA AL "+FuncionesHelper.fechaToString(new Date()));
			}else if(tipSelectedCondicionFinalizado.equals("3")){
				listaContratoBean = contratoService.obtenerCondicion("Contrato","FINALIZADO","no");
				 parameters.put("CONDICION","CONTRATOS FINALIZADOS CON DEUDA AL "+FuncionesHelper.fechaToString(new Date()));
			}
			
		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   }else{
			   Calendar hasta = Calendar.getInstance();
				hasta.setTime(hastaContratoFinalizado);
				//hasta.add(Calendar.DAY_OF_YEAR, 1);
				
				Calendar desde = Calendar.getInstance();
				desde.setTime(desdeContratoFinalizado);
				if (tipSelectedCondicionFinalizado.equals("1")) {
					listaContratoBean = contratoService.obtenerCondicionxFecha("Contrato","FINALIZADO","todos",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
					 parameters.put("CONDICION","CONTRATOS FINALIZADOS DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
				}else if (tipSelectedCondicionFinalizado.equals("2")) {
					listaContratoBean = contratoService.obtenerCondicionxFecha("Contrato","FINALIZADO","si",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
					 parameters.put("CONDICION","CONTRATOS FINALIZADOS SIN DEUDA DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
				}else if(tipSelectedCondicionFinalizado.equals("3")){
					listaContratoBean = contratoService.obtenerCondicionxFecha("Contrato","FINALIZADO","no",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
					 parameters.put("CONDICION","CONTRATOS FINALIZADOS CON DEUDA DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
				}
				parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   }
		   
		   if (tipoarchivo.equals("pdf")) {
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Finalizados_"+FuncionesHelper.fechaToString(new Date())); 
			} else {
				XLS_Contrato(listaContratoBean);
			}
	   
	   
	   
		   
	   

	}
	

	
	public void XLS_Contrato(List<CondicionBean> lista){

		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Página 1");
        sheet.setColumnWidth(0, 2500); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 10000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        String[] headers = new String[] { "CLAVE", "DIRECCION","DISTRITO","NOMBRES/RAZÓN SOCIAL","NRO CONTRATO","INICIO CONTRATO","FIN CONTRATO","MONEDA","RENTA"};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
            Row row = sheet.createRow(rownum++);
            CondicionBean bean= new CondicionBean();
            bean= lista.get(i);
                
                for (int j = 0; j < 9; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClaveUpa());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
                		 else if (j==2)
                			 cell.setCellValue((String)bean.getDistrito());
                			 else if (j==3) 
                	            cell.setCellValue((String)bean.getNombresInquilino());
                			     else if (j==4)
                			    	 cell.setCellValue((String)bean.getNrocontrato());
	                				 else if (j==5){
	                					 	if(bean.getIniciocontrato()==null){
	                					 		cell.setCellValue("");
	                					 	}else{
	                					 		cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getIniciocontrato()));
	                					 	}}
	                				 
	                				 		else if (j==6){
	                				 				if(bean.getFincontrato()==null){
	                				 					cell.setCellValue("");
	                				 					}else{
	                				 				cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFincontrato()));	
	                				 					}}
		                				 
	                				 				else if (j==7)
	                				 						cell.setCellValue((String)bean.getMoneda());
	                				 						else if (j==8)
	                				 							cell.setCellValue((Double)bean.getCuota1());
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
            report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Contratos_"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 
	
/**Reporte Excel de Condición de Arrendamiento */
public void XLS_Condición_Arrendamiento(List<CondicionBean> lista){

		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Pagina 1");
        final CreationHelper helper = workbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();

		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType( ClientAnchor.MOVE_AND_RESIZE );
        
		final int pictureIndex =workbook.addPicture( CommonsUtilities.getByteArray(CommonsUtilities.getRealPath()+"reportes"+File.separator+"logo.png"), Workbook.PICTURE_TYPE_PNG );
		anchor.setCol1( 0 );
		anchor.setRow1( 1 ); // same row is okay
		anchor.setRow2( 2 );
		anchor.setCol2( 1 );
		final Picture pict = drawing.createPicture( anchor, pictureIndex );
		pict.resize();

        sheet.setColumnWidth(0, 2500); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 12000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        int ind=6;
        Row r = sheet.createRow(ind);
        Cell cc = r.createCell(3);
        cc.setCellValue("REPORTE CONDICIÓN DE ARRENDAMIENTO");
        cc.setCellStyle(csBold);
        
        
        String[] headers = new String[] { "CLAVE", "CONDICION","ESTADO","NOMBRES/RAZÓN SOCIAL","DIRECCION","DISTRITO","NRO CONTRATO","NRO ACTA","INICIO CONTRATO","FIN CONTRATO","MONEDA","ADENDA","ESCRITURA PUBLICA","TIPO ALQUILER","USO ESPECIFICO","COPROPIEDAD","INICIO COBRO","FIN COBRO","PROCESO JUDICIAL","DOCUMENTO","RENTA ACTUAL","ULTIMO RENTA PAGADA","FECHA PAGO"};

        r = sheet.createRow(8);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        int rownum = 9;
        for (int i = 0; i < lista.size(); i++) {
            Row row = sheet.createRow(rownum++);
            CondicionBean bean= new CondicionBean();
            bean= lista.get(i);
                
                for (int j = 0; j < 23; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClaveUpa());
                	else if (j==1)
                		 cell.setCellValue((String)bean.getCondicion());
                	else if (j==2)
                	 	cell.setCellValue((String)bean.getEstado());
                	else if (j==3)
                	 	cell.setCellValue((String)bean.getNombresInquilino());
                	else if (j==4)
                	 	cell.setCellValue((String)bean.getDireccion());
                	else if (j==5)
                	 	cell.setCellValue((String)bean.getDistrito());
                	else if (j==6) 
                	 	cell.setCellValue((String)bean.getNrocontrato());
                	else if (j==7)
                	 	cell.setCellValue((String)bean.getNroacta());
                	else if (j==8){
                	 	if(bean.getIniciocontrato()==null){
                	 		cell.setCellValue("");
                	 	}else{
                	 		 cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getIniciocontrato()));
                	 	}}	                				 
                	else if (j==9){
                	 	if(bean.getFincontrato()==null){
                	 		cell.setCellValue("");
                	 	}else{
                	 		cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFincontrato()));	
                	 	}}		                				 
                	else if (j==10)
                	 		cell.setCellValue((String)bean.getMoneda());
                	else if (j==11)
            	 			cell.setCellValue(bean.getSiadenda()? "SI":"NO");
                	else if (j==12)
            	 			cell.setCellValue(bean.getSiescriturapublica()!=null? (bean.getSiescriturapublica()?"SI":"NO"):"NO");
                	else if (j==13)
            	 			cell.setCellValue((String)bean.getTipo());
                	else if (j==14)
            	 			cell.setCellValue(bean.getUsoespecifico());
                	else if (j==15)
            	 			cell.setCellValue(bean.getSicopropiedad()!=null? (bean.getSicopropiedad()?"SI":"NO"):"NO");
                	else if (j==16){
                	 		if(bean.getIniciocobro()==null){
                          	 		cell.setCellValue("");
                          	}else{
                          	 	    cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getIniciocobro()));
                          	}}	
                	else if (j==17){
                	 		if(bean.getFincobro()==null){
                             	 cell.setCellValue("");
                            }else{
                             	 cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFincobro()));
                            }}	
                	else if (j==18)
            	 			cell.setCellValue(bean.getSiprocesojudicial()!=null? (bean.getSiprocesojudicial()?"SI":"NO"):"NO");
                	else if (j==19)
            	 			cell.setCellValue(bean.getSidocumento()? "SI":"NO");
                	else if (j==20)
            	 			cell.setCellValue(bean.getRentaActual());
                	else if (j==21)
            	 			cell.setCellValue(bean.getRentaPagada());
                	else if (j==22){
                	 		if(bean.getFechaPagada()==null){
                            	 	cell.setCellValue("");
                            }else{
                            	 	cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFechaPagada()));
                            }}	
                	 													
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
            report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Contratos_"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 

public void XLS_Upa(List<Upa> lista) throws FileNotFoundException 
{
	
	HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Pagina 1");
    final CreationHelper helper = workbook.getCreationHelper();
	final Drawing drawing = sheet.createDrawingPatriarch();

	final ClientAnchor anchor = helper.createClientAnchor();
	anchor.setAnchorType( ClientAnchor.MOVE_AND_RESIZE );
    
	final int pictureIndex =workbook.addPicture( CommonsUtilities.getByteArray(CommonsUtilities.getRealPath()+"reportes"+File.separator+"logo.png"), Workbook.PICTURE_TYPE_PNG );
	anchor.setCol1( 0 );
	anchor.setRow1( 1 ); // same row is okay
	anchor.setRow2( 2 );
	anchor.setCol2( 1 );
	final Picture pict = drawing.createPicture( anchor, pictureIndex );
	pict.resize();

    sheet.setColumnWidth(0, 3000); 
    sheet.setColumnWidth(1, 3000);
    sheet.setColumnWidth(2, 3000);
    sheet.setColumnWidth(3, 4000);
    sheet.setColumnWidth(4, 8000);
    sheet.setColumnWidth(5, 3000);
    sheet.setColumnWidth(6, 3000);
    sheet.setColumnWidth(7, 3000);
    sheet.setColumnWidth(8, 3000);
    sheet.setColumnWidth(9, 3000);
    sheet.setColumnWidth(10, 3000);
    
    /**Insercion de cabecera (rownum=0) **/
    CellStyle csBold = null;
    
    Font bold = workbook.createFont();
    bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
    bold.setFontHeightInPoints((short) 10);
    
    csBold = workbook.createCellStyle();
    csBold.setBorderBottom(CellStyle.BORDER_THIN);
    csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    csBold.setFont(bold);
    int ind=6;
    Row r = sheet.createRow(ind);
//    Cell cc = r.createCell(3);
//    cc.setCellValue("");
//    cc.setCellStyle(csBold);
        
        String[] headers = new String[] { "CLAVE", "DIRECCION", "N° PRINCIPAL","N° DE INTERIOR","DISTRITO","PROVINCIA","DEPARTAMENTO","USO","CONDICION","TIPO TITULARIDAD","PROCESO JUDICIAL","COPROPIEDAD","ESTADO","ACTUALIZACION DE CLAVE","CLAVE ANTERIOR"};
         r = sheet.createRow(7);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        
        
        
        /**Insercion de datos **/
        
        
        int rownum = 8;
        for (int i = 0; i < lista.size(); i++) {
        	
            Upa bean= new Upa();
            bean= lista.get(i);

				Row row = sheet.createRow(rownum++);
                for (int j = 0; j < 15; j++) {//NUMERO DE COLUMNAS
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClave());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
                			else if (j==2)
                				  cell.setCellValue((String)bean.getNumprincipal());
                			   else if (j==3)
                				   	cell.setCellValue((String)bean.getNombrenuminterior());
                			   		else if (j==4)
                			   				cell.setCellValue(bean.getInmueblemaestro()!=null? (String)bean.getInmueblemaestro().getUbigeo().getDist():(String)bean.getInmueble().getUbigeo().getDist());
                			   				else if (j==5)
                			   						cell.setCellValue(bean.getInmueblemaestro()!=null? (String)bean.getInmueblemaestro().getUbigeo().getProv():(String)bean.getInmueble().getUbigeo().getProv());             	
                			   						else if (j==6)
                			   								cell.setCellValue(bean.getInmueblemaestro()!=null? (String)bean.getInmueblemaestro().getUbigeo().getDepa():(String)bean.getInmueble().getUbigeo().getDepa());
                			   								else if (j==7)
                			   										cell.setCellValue((String)bean.getUso());
                			   										else if (j==8)
                			   												cell.setCellValue((String)bean.getTugurizadoruinoso());
                			   										else if (j==9){               			   											
            			   												cell.setCellValue(bean.getInmueblemaestro()!=null? (String)bean.getInmueblemaestro().getTitularidad().getTipotitularidad().getDescripcion():"");
                			   									        	}else if (j==10)
                			   														cell.setCellValue(bean.getSiprocesojudicial()!=null? (bean.getSiprocesojudicial()==true? "SI":"NO"):"NO");
			                			 											else if (j==11)
			                			 													cell.setCellValue(bean.getSicopropiedad()!=null? (bean.getSicopropiedad()==true? "SI":"NO"):"NO");
			                			 													else if (j==12)
			                			 															cell.setCellValue(bean.getEstado()!=null? (bean.getEstado().equals("0")? "Activo":"Inactivo"):"Activo");
			                			 															else if (j==13)
			                			 																	cell.setCellValue(bean.getSiactualizaclave()!=null? (bean.getSiactualizaclave()==true? "Si":"No"):"No");
			                			 																	else if (j==14)
			                			 																			cell.setCellValue(bean.getSiactualizaclave()==true? bean.getIdupa_anterior().getClave():"");
				                					 	 
                							}

			
		}



        try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	try {
        		workbook.write(bos);
        	} finally {
        	    bos.close();
        	}
        	
            InputStream stream;
            stream = new ByteArrayInputStream(bos.toByteArray());
        	
			report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLS, "Reporte_Clave_UPA_"+FuncionesHelper.fechaToString(new Date())+".xls");
			//report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLSX, "ReporteComprobante"+FuncionesHelper.fechaToString(new Date())+".XLSX");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

}
public void XLS_ContratoGlobal(List<CondicionBean> lista){

		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Contratos SGI");
        sheet.setColumnWidth(0, 2500); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 10000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(7, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 3000);
        sheet.setColumnWidth(11, 3000);
        sheet.setColumnWidth(12, 3000);
       // sheet.setColumnWidth(13, 3000);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        String[] headers = new String[] { "CLAVE", "DIRECCION","DISTRITO","NOMBRES/RAZÓN SOCIAL","CONDICIÓN","NRO CONTRATO","TIPO DE DOCUMENTO","USO ESPECIFICO","COPROPIEDAD","INICIO CONTRATO","FIN CONTRATO","MONEDA","RENTA","ULTIMO PAGO"};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
            Row row = sheet.createRow(rownum++);
            CondicionBean bean= new CondicionBean();
            bean= lista.get(i);
                
                for (int j = 0; j < 14; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClaveUpa());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
                		 else if (j==2)
                			 cell.setCellValue((String)bean.getDistrito());
                			 else if (j==3) 
                	            cell.setCellValue((String)bean.getNombresInquilino());
                			 	else 	if (j==4) { if(bean.getCondicion()==null){
                			 		   cell.setCellValue("");
                			 			}else{
                			 		cell.setCellValue((String)bean.getCondicion());
                			 			}
                			 		}                			 		 
                			     else if (j==5)
                			    	 cell.setCellValue((String)bean.getNrocontrato());
	                				 else if (j==6){
	                					 	if(bean.getSiescriturapublica()==true){
	                					 		cell.setCellValue((String)Constante.TIPO_DOC_ESCRITURA_PUBLICA);
	                					 	}else{
	                					 		cell.setCellValue("");
	                					 	}}
			                				 else if (j==7){
			                					 	if(bean.getUsoespecifico()==null){
			                					 		cell.setCellValue("");
			                					 	}else{
			                					 		cell.setCellValue((String)bean.getUsoespecifico());
			                					 	}}
			                				 		else if (j==8){
			                				 				if(bean.getSicopropiedad()==null){
			                				 						cell.setCellValue("No");
			                				 				}else{
			                				 					if(bean.getSicopropiedad()){
			                				 						cell.setCellValue("Si");
			                				 					}else{
			                				 						cell.setCellValue("No");
			                				 					}
			                				 				}}
			                				 				else if (j==9){
			                				 						if(bean.getIniciocontrato()==null){
			                				 							cell.setCellValue("");
			                				 						}else{
			                				 							cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getIniciocontrato()));
			                				 						}}				
					                				 				else if (j==10){
					                				 					if(bean.getFincontrato()==null){
					                				 						cell.setCellValue("");
					                				 					}else{
					                				 						cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFincontrato()));	
					                				 					}}						                				 
					                				 					else if (j==11)
					                				 							cell.setCellValue((String)bean.getMoneda());
					                				 						else if (j==12)
					                				 							cell.setCellValue((Double)bean.getCuota1());
					                				 							else if (j==13){
					            	                				 				if(bean.getFeccre()==null){
					            	                				 					cell.setCellValue("");
					            	                				 					}else{
					            	                				 				cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFeccre()));	
					            	                				 					}}
//					                				 								else if (j==14)
//					                				 									cell.setCellValue((Integer)bean.getIdupa());
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
            report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Contratos_SGI_"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 
public void XLS_PreContrato(List<CondicionBean> lista){

	
	HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Precontratos SGI");
    sheet.setColumnWidth(0, 2500); 
    sheet.setColumnWidth(1, 2500);
    sheet.setColumnWidth(2, 6000);
    sheet.setColumnWidth(3, 10000);
    sheet.setColumnWidth(4, 3000);
    sheet.setColumnWidth(5, 3000);
    sheet.setColumnWidth(6, 3000);
    sheet.setColumnWidth(7, 3000);
    sheet.setColumnWidth(8, 3000);
    sheet.setColumnWidth(9, 3000);
    sheet.setColumnWidth(10, 3000);
    sheet.setColumnWidth(11, 3000);
    sheet.setColumnWidth(12, 3000);
   // sheet.setColumnWidth(13, 3000);
    
    /**Insercion de cabecera (rownum=0) **/
    CellStyle csBold = null;
    
    Font bold = workbook.createFont();
    bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
    bold.setFontHeightInPoints((short) 10);
    
    csBold = workbook.createCellStyle();
    csBold.setBorderBottom(CellStyle.BORDER_THIN);
    csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    csBold.setFont(bold);
    String[] headers = new String[] { "CLAVE", "DIRECCION","DISTRITO","NOMBRES/RAZÓN SOCIAL","CONDICIÓN","NRO CONTRATO","TIPO DE DOCUMENTO","USO ESPECIFICO","COPROPIEDAD","INICIO CONTRATO","FIN CONTRATO","MONEDA","RENTA"};

    Row r = sheet.createRow(0);
    for (int rn=0; rn<headers.length; rn++) {
       Cell cell = r.createCell(rn);
       cell.setCellValue(headers[rn]);
       cell.setCellStyle(csBold);
    }
    int rownum = 1;
    for (int i = 0; i < lista.size(); i++) {
        Row row = sheet.createRow(rownum++);
        CondicionBean bean= new CondicionBean();
        bean= lista.get(i);
            
            for (int j = 0; j < 13; j++) {
            	Cell cell = row.createCell(j);
            	if (j==0)
            		cell.setCellValue((String)bean.getClaveUpa());
            	 else if (j==1)
            		 cell.setCellValue((String)bean.getDireccion());
            		 else if (j==2)
            			 cell.setCellValue((String)bean.getDistrito());
            			 else if (j==3) 
            	            cell.setCellValue((String)bean.getNombresInquilino());
            			 	else 	if (j==4) { if(bean.getCondicion()==null){
            			 		   cell.setCellValue("");
            			 			}else{
            			 		cell.setCellValue((String)bean.getCondicion());
            			 			}
            			 		}                			 		 
            			     else if (j==5)
            			    	 cell.setCellValue((String)bean.getNrocontrato());
                				 else if (j==6){
                					 	if(bean.getSiescriturapublica()==true){
                					 		cell.setCellValue((String)Constante.TIPO_DOC_ESCRITURA_PUBLICA);
                					 	}else{
                					 		cell.setCellValue("");
                					 	}}
		                				 else if (j==7){
		                					 	if(bean.getUsoespecifico()==null){
		                					 		cell.setCellValue("");
		                					 	}else{
		                					 		cell.setCellValue((String)bean.getUsoespecifico());
		                					 	}}
		                				 		else if (j==8){
		                				 				if(bean.getSicopropiedad()==null){
		                				 						cell.setCellValue("No");
		                				 				}else{
		                				 					if(bean.getSicopropiedad()){
		                				 						cell.setCellValue("Si");
		                				 					}else{
		                				 						cell.setCellValue("No");
		                				 					}
		                				 				}}
		                				 				else if (j==9){
		                				 						if(bean.getIniciocontrato()==null){
		                				 							cell.setCellValue("");
		                				 						}else{
		                				 							cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getIniciocontrato()));
		                				 						}}				
				                				 				else if (j==10){
				                				 					if(bean.getFincontrato()==null){
				                				 						cell.setCellValue("");
				                				 					}else{
				                				 						cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFincontrato()));	
				                				 					}}						                				 
				                				 					else if (j==11)
				                				 							cell.setCellValue((String)bean.getMoneda());
				                				 						else if (j==12)
				                				 							cell.setCellValue((Double)bean.getCuota1());
//				                				 							else if (j==13){
//				            	                				 				if(bean.getFeccre()==null){
//				            	                				 					cell.setCellValue("");
//				            	                				 					}else{
//				            	                				 				cell.setCellValue((String)FuncionesHelper.fechaToString(bean.getFeccre()));	
//				            	                				 					}}
//				                				 								else if (j==14)
//				                				 									cell.setCellValue((Integer)bean.getIdupa());
            }
	}

     
    try {
        //FileOutputStream out =  new FileOutputStream(new File("C:\\Contratos.xls"));
//        workbook.write(out);
//        out.close();
//        System.out.println("Excel written successfully..");
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {
    		workbook.write(bos);
    	} finally {
    	    bos.close();
    	}
        InputStream stream; 
//      stream = new FileInputStream("c:\\Contratos.xls");
        stream = new ByteArrayInputStream(bos.toByteArray());
        report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Contratos_SGI_"+FuncionesHelper.fechaToString(new Date())+".xls");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
} 
public void generarReporteInquilino(String tipoarchivo) {
	
	try{
	   
		List<InquilinoBean> lista=new ArrayList<InquilinoBean>();
		String  reportPath=  ConstantesReporteSGI.REP_INMUEBLE;
		Map<String, Object> parameters = new HashMap<String, Object>();
		lista = listaInquilino;		   
		parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		parameters.put("CONDICION","INQUILINOS AL "+FuncionesHelper.fechaToString(new Date()));
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, lista,"Reporte_Inquilino_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Inquilino(lista);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void XLS_Inquilino(List<InquilinoBean> lista){
	HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Inquilino SGI");
    sheet.setColumnWidth(0, 3000); 
    sheet.setColumnWidth(1, 12000);
    sheet.setColumnWidth(2, 3000);
    sheet.setColumnWidth(3, 3000);
    sheet.setColumnWidth(4, 3000);
    sheet.setColumnWidth(5, 3000);
    sheet.setColumnWidth(6, 3000);
    sheet.setColumnWidth(7, 3000);
    sheet.setColumnWidth(8, 3000);
    sheet.setColumnWidth(9, 3000);
    sheet.setColumnWidth(10, 3000);
    sheet.setColumnWidth(11, 3000);
    sheet.setColumnWidth(12, 3000);
//    sheet.setColumnWidth(8, 3000);
//    sheet.setColumnWidth(9, 3000);
    
    /**Insercion de cabecera (rownum=0) **/
    CellStyle csBold = null;
    
    Font bold = workbook.createFont();
    bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
    bold.setFontHeightInPoints((short) 10);
    
    csBold = workbook.createCellStyle();
    csBold.setBorderBottom(CellStyle.BORDER_THIN);
    csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    csBold.setFont(bold);
    String[] headers = new String[] { "TIPO PERSONA", "RAZÓN SOCIAL ","A. PATERNO","A. MATERNO","NOMBRE","DNI","RUC","CARNET EXTRANJERIA","FALLECIO","ANULADO","USR. CREADOR","F. CREACIÓN","USR MODIFICADOR","F. MODIFICADOR"};

    Row r = sheet.createRow(0);
    for (int rn=0; rn<headers.length; rn++) {
       Cell cell = r.createCell(rn);
       cell.setCellValue(headers[rn]);
       cell.setCellStyle(csBold);
    }
    int rownum = 1;
    for (int i = 0; i < lista.size(); i++) {
        Row row = sheet.createRow(rownum++);
       InquilinoBean bean= new InquilinoBean();
        bean= lista.get(i);
            
            for (int j = 0; j < 14; j++) {
            	Cell cell = row.createCell(j);
            	
            	if (j==0)
            		cell.setCellValue((String)bean.getDescripciontipopersona());
            	else if (j==1)
           		 cell.setCellValue((String)bean.getRazonsocial());
            	 else if (j==2)
            		 cell.setCellValue((String)bean.getApellidopat());
            		 	else if (j==3)
            		 		cell.setCellValue((String)bean.getApellidomat());
            			 	else if (j==4) 
            			 		cell.setCellValue((String)bean.getNombre());
            			     	else if (j==5)
            			     		cell.setCellValue((String)bean.getDni());
            			     	else if (j==6)
            			     		cell.setCellValue((String)bean.getRuc());
            			     	else if (j==7)
            			     		cell.setCellValue((String)bean.getCarnetextranjeria());
            			     	else if (j==8)
            			     		cell.setCellValue(bean.getSifallecido()!=null? (bean.getSifallecido()? "SI":"NO" ): "NO");				 					
            			     	else if (j==9)              			     	
            			     		cell.setCellValue(bean.getSianulado()!=null? (bean.getSianulado()? "SI":"NO" ): "NO");	
            			     	else if (j==10)
            			     		cell.setCellValue((String)bean.getUsrcre());           			     		
            			     	else if (j==11)
            			     		cell.setCellValue(bean.getFeccre()!=null ?(String)FuncionesHelper.fechaToString(bean.getFeccre()):"");           			     	
            			     	else if (j==12)
            			     		cell.setCellValue((String)bean.getUsrmod());            			     		
            			     	else if (j==13){
            			     		cell.setCellValue(bean.getFecmod()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecmod()):"");
            			     	}

            
            }
	                				 
                				 				
            }
    try {
        //FileOutputStream out =  new FileOutputStream(new File("C:\\Contratos.xls"));
//        workbook.write(out);
//        out.close();
//        System.out.println("Excel written successfully..");
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try {
    		workbook.write(bos);
    	} finally {
    	    bos.close();
    	}
        InputStream stream; 
//      stream = new FileInputStream("c:\\Contratos.xls");
        stream = new ByteArrayInputStream(bos.toByteArray());
        report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Inquilinos_SGI_"+FuncionesHelper.fechaToString(new Date())+".xls");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
public void generarReporteInmueble(String tipoarchivo) {
	
	try{
	   
		List<InmuebleBean> listaInmuebleBean=new ArrayList<InmuebleBean>();
		String  reportPath=  ConstantesReporteSGI.REP_INMUEBLE;
		Map<String, Object> parameters = new HashMap<String, Object>();
		listaInmuebleBean = listaInmuebleMaestro;		   
		parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		parameters.put("CONDICION","INMUEBLE MATRIZ AL "+FuncionesHelper.fechaToString(new Date()));
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaInmuebleBean,"Reporte_Inmueble_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_InmuebleMaestro(listaInmuebleBean);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
    public void XLS_InmuebleMaestro(List<InmuebleBean> lista){
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
            report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Inmueble_Matriz_"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public void openDlgConsultaContrato(){
		limpiarValores();
		limpiarValoresConsultaUpa();
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		contextRequest.execute("dlgConsultaContrato.show();");
	}

	public void inicializarValoresCondicionConsultaContrato() {
		try {
			listacondicion = new ArrayList<String>();
			listaestado = new ArrayList<String>();
			selectedCondicionSeleccionado = new ArrayList<String>();
			selectedEstadoSeleccionado = new ArrayList<String>();
			listacondicion.add(Constante.CONDICION_CONTRATO);
			listacondicion.add(Constante.CONDICION_PRECONTRATO);
			listacondicion.add(Constante.CONDICION_SINCONTRATO);
			listacondicion.add(Constante.CONDICION_RECONOCIMIENTO_DEUDA);
			listaestado.add(Constante.CONDICION_CONTRATO_VIGENTE);
			listaestado.add(Constante.CONDICION_CONTRATO_FINALIZADO);
			selectedCondicionSeleccionado.add(Constante.CONDICION_CONTRATO);
			selectedEstadoSeleccionado.add(Constante.CONDICION_CONTRATO_VIGENTE);
			listaDepartamentos = inmuebleService.listarDepartamentos();
			listaTipoUso = upaService.listarTipoUso();
			listaTipoTitularidad=inmuebleService.listarTipotitularidad();
			//listaInmuebleMaestro = inmuebleService.listarInmuebleMaestro();
			listaInmuebleMaestro= new ArrayList<InmuebleBean>();
			listaTipoZona=inmuebleService.listarTipoZona();
			upa=new Upa();
			upaBean= new UpaBean();
			tipozona= new Tipozona();
			inmuebleMaestro= new Inmueblemaestro();
			inmuebleBean= new InmuebleBean();
			listaTipoPersona=inquilinoService.listarTipoPersona();
			sipersonanatural=Boolean.TRUE;
			inquilinoBean= new InquilinoBean();
			listaInquilino=new ArrayList<InquilinoBean>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   public void consultaContrato(){
	  
	   listaContratoBean.clear();
	    if(desdeInicioContrato==null && hastaInicioContrato==null && desdeFinContrato==null && hastaFinContrato==null){
			listaContratoBean = contratoService.obtenerListaContratos(selectedCondicionSeleccionado,selectedEstadoSeleccionado,null,null,null,null);
            
	    }
	    else if ((desdeInicioContrato!=null && hastaInicioContrato!=null) && (desdeFinContrato==null || hastaFinContrato==null)) {
			Calendar desdeInicio = Calendar.getInstance();
			desdeInicio.setTime(desdeInicioContrato);
			Calendar hastaInicio = Calendar.getInstance();
			hastaInicio.setTime(hastaInicioContrato);	
			//listaContratoBean = contratoService.obtenerCondicion("Contrato","VIGENTE","todos");
			listaContratoBean = contratoService.obtenerListaContratos(selectedCondicionSeleccionado,selectedEstadoSeleccionado,FuncionesHelper.fechaToString(desdeInicio.getTime()),FuncionesHelper.fechaToString(hastaInicio.getTime()),null,null);
		}else if((desdeFinContrato!=null && hastaFinContrato!=null) && (desdeInicioContrato==null || hastaInicioContrato==null)){
			Calendar desdeFin = Calendar.getInstance();
			desdeFin.setTime(desdeFinContrato);
			Calendar hastaFin = Calendar.getInstance();
			hastaFin.setTime(hastaFinContrato);	
			listaContratoBean = contratoService.obtenerListaContratos(selectedCondicionSeleccionado,selectedEstadoSeleccionado,null,null,FuncionesHelper.fechaToString(desdeFin.getTime()),FuncionesHelper.fechaToString(hastaFin.getTime()));

		}else{
			Calendar desdeInicio = Calendar.getInstance();
			desdeInicio.setTime(desdeInicioContrato);
			Calendar hastaInicio = Calendar.getInstance();
			hastaInicio.setTime(hastaInicioContrato);	
			
			Calendar desdeFin = Calendar.getInstance();
			desdeFin.setTime(desdeFinContrato);
			Calendar hastaFin = Calendar.getInstance();
			hastaFin.setTime(hastaFinContrato);	

		listaContratoBean = contratoService.obtenerListaContratos(selectedCondicionSeleccionado,selectedEstadoSeleccionado,FuncionesHelper.fechaToString(desdeInicio.getTime()),FuncionesHelper.fechaToString(hastaInicio.getTime()),FuncionesHelper.fechaToString(desdeFin.getTime()),FuncionesHelper.fechaToString(hastaFin.getTime()));
		}
	    List<RentaBean> listaRenta = new ArrayList<RentaBean>();
	    // obtener fecha ultimo
	    Iterator<CondicionBean> iter = listaContratoBean.iterator();
	    while(iter.hasNext()){
	    	CondicionBean condBean= iter.next();
	    	RentaBean rentaBean = new RentaBean();
	    	RentaBean renta = new RentaBean();
	    	if(reporteRecaudacionService.siRentasPagadasxContrato(condBean.getIdcontrato())){
	    		rentaBean= reporteRecaudacionService.ultimaRentaPagada(condBean.getIdcontrato());
	    		
	    		condBean.setRentaActual(getRentaActual(condBean));
		        condBean.setFechaPagada(rentaBean.getFechacancelacion());
		        condBean.setRentaPagada(rentaBean.getMontopagado());
	    	}	    	
	        condBean.setNrocontrato(condBean.getNrocontrato()!=null ? condBean.getNrocontrato() : "" );
	        
	    }

   }

   public Double getRentaActual(CondicionBean condicion){
	   Double renta =0.0;
	   List<RentaBean> listaRenta= obtenerRentasxCondicion(condicion);
	   Calendar  fechaActual = Calendar.getInstance();
	   if(listaRenta!=null){
		   Iterator<RentaBean> iter= listaRenta.iterator();
		   while(iter.hasNext()){
			   RentaBean re = iter.next();
			   if(Integer.parseInt( Almanaque.mesanumero(re.getMes()))==(fechaActual.get(Calendar.MONTH)+1) && Integer.parseInt(re.getAnio()) == fechaActual.get(Calendar.YEAR)){
				   renta= re.getRenta();
			   }
		   }
	   }
	   return  renta;
   }
   public List<RentaBean> obtenerRentasxCondicion(CondicionBean condicion){
	   List<RentaBean> rentaBean= new ArrayList<RentaBean>();
	   try{
		   if(condicion.getCondicion().equalsIgnoreCase(Constante.CONDICION_CONTRATO )|| condicion.getCondicion().equalsIgnoreCase(Constante.CONDICION_PRECONTRATO )||condicion.getCondicion().equalsIgnoreCase(Constante.CONDICION_SINCONTRATO )){
			   rentaBean=setearContratoSeleccionado(condicion);
		   }else{
			   rentaBean=setearReconocimientoDeudaSeleccionado(condicion);
		   }
	   		
	   
	 }catch(Exception ex){
		ex.printStackTrace();
	}
	   return rentaBean;
   }
   //CONTRATO PRECONTRATO Y SIN CONTRATO
   public List<RentaBean> setearContratoSeleccionado(CondicionBean contratoBean) {
	   List<RentaBean> listaRentas= new ArrayList<RentaBean>();
		
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(contratoBean.getIniciocobro());
		
		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(contratoBean.getFincobro()!=null?contratoBean.getFincobro():new Date());
		
		
		int nrocoutas=contratoBean.getNumerocuotas();
		
		if (contratoBean.getSinuevomantenimiento() !=null && contratoBean.getSinuevomantenimiento()) {
			listaRentas=reporteRecaudacionService.obtenerRentasNuevoMantenimientoContrato(contratoBean.getIdcontrato());
			
		} else {
			for (int i = 1; i <= nrocoutas; i++) {
				RentaBean rentaBean= new RentaBean();
				rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
				rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
				rentaBean.setSecuencia(i);
				rentaBean.setContraprestacion(contratoBean.getCuota1());
				rentaBean.setId(listaRentas.size()+1);
				if (i<=12) {
					rentaBean.setRenta(contratoBean.getCuota1());
					rentaBean.setMontopagar(contratoBean.getCuota1());
					
				}else if (i<=24) {
					rentaBean.setRenta(contratoBean.getCuota2());
					rentaBean.setMontopagar(contratoBean.getCuota2());
				}else if (i<=36) {
					rentaBean.setRenta(contratoBean.getCuota3());
					rentaBean.setMontopagar(contratoBean.getCuota3());
				}else if (i<=48) {
					rentaBean.setRenta(contratoBean.getCuota4());
					rentaBean.setMontopagar(contratoBean.getCuota4());
				}else if (i<=60) {
					rentaBean.setRenta(contratoBean.getCuota5());
					rentaBean.setMontopagar(contratoBean.getCuota5());
				}else {
					rentaBean.setRenta(contratoBean.getCuota6());
					rentaBean.setMontopagar(contratoBean.getCuota6());
				}
				
				
				listaRentas.add(rentaBean);
				iniciocobro.add(Calendar.MONTH, 1);
			}
		}
		
		return listaRentas;

	}
   public List<RentaBean> setearReconocimientoDeudaSeleccionado(CondicionBean contratoBean){
	   	List<RentaBean> listaRentas= new ArrayList<RentaBean>();
		List<MaeDetalleCondicion> listaMaeCondicion= new ArrayList<MaeDetalleCondicion>();
		List<MaeDetCondicionDetalle> listaMaeCondicionDetalle= new ArrayList<MaeDetCondicionDetalle>();
	try{		
		if (contratoBean.getSinuevomantenimiento() !=null && contratoBean.getSinuevomantenimiento()) {
			listaRentas=reporteRecaudacionService.obtenerRentasNuevoMantenimientoContrato(contratoBean.getIdcontrato());
			listaMaeCondicion=contratoService.obtenerListaMaeCondicion(contratoBean.getIdcontrato());
			listaMaeCondicionDetalle=contratoService.obtenerListaMaeCondicionDetalle(contratoBean.getIdcontrato());
			 
				for (MaeDetalleCondicion mae : listaMaeCondicion) {
					Set<MaeDetCondicionDetalle> maecondiciondetalle = new HashSet<MaeDetCondicionDetalle>();
					for (MaeDetCondicionDetalle maed : listaMaeCondicionDetalle) {
						if (mae.getIdDetalleCondicion() == maed.getMaedetallecondicion().getIdDetalleCondicion()) {
							maecondiciondetalle.add(maed);
						}
					}
					mae.setMaedetcondiciondetalles(maecondiciondetalle);		
				}
				for(RentaBean renta:listaRentas){
					Set<MaeDetCondicionDetalleBean> maecondBean = new HashSet<MaeDetCondicionDetalleBean>();
					
					
					for(MaeDetalleCondicion mae :listaMaeCondicion){
						
						
					 if(renta.getNrocuota().equals(mae.getNrocuota())){
						 List<MaeDetCondicionDetalleBean> maeconddetalle = new ArrayList<MaeDetCondicionDetalleBean>();
						  for(MaeDetCondicionDetalle maecond_detalle:mae.getMaedetcondiciondetalles()){
							  MaeDetCondicionDetalleBean maecond_detalleBean= new MaeDetCondicionDetalleBean();
							  maecond_detalleBean.setAnio(maecond_detalle.getAnio());
							  maecond_detalleBean.setConcepto(maecond_detalle.getConcepto());
							  maecond_detalleBean.setEstado(maecond_detalle.getEstado());
							  maecond_detalleBean.setFec_anula(maecond_detalle.getFec_anula());
							  maecond_detalleBean.setFeccre(maecond_detalle.getFeccre());
							  maecond_detalleBean.setFecmod(maecond_detalle.getFecmod());
							  maecond_detalleBean.setHost_ip_anula(maecond_detalle.getHost_ip_anula());
							  maecond_detalleBean.setHost_ip_cre(maecond_detalle.getHost_ip_cre());
							  maecond_detalleBean.setHost_ip_mod(maecond_detalle.getHost_ip_mod());
							  maecond_detalleBean.setId_condicion(maecond_detalle.getMaedetallecondicion().getIdDetalleCondicion());
							  maecond_detalleBean.setId_condicion_detalle(maecond_detalle.getIdcondiciondetalle());
							  maecond_detalleBean.setIdcontrato(maecond_detalle.getIdcontrato());
							  maecond_detalleBean.setIddetallecondicion(maecond_detalle.getIddetallecondicion());
							  maecond_detalleBean.setIdconcepto(maecond_detalle.getIdconcepto());
							  maecond_detalleBean.setMes(maecond_detalle.getMes());
							  maecond_detalleBean.setMonto(maecond_detalle.getMonto());
							  maecond_detalleBean.setMotivo_anula(maecond_detalle.getMotivo_anula());
							  maecond_detalleBean.setNumeromes(maecond_detalle.getNumeromes());
							  maecond_detalleBean.setObs(maecond_detalle.getObs());
							  maecond_detalleBean.setObs_mod(maecond_detalle.getObs_mod());
							  maecond_detalleBean.setSecuencia(maecond_detalle.getSecuencia());
							  maecond_detalleBean.setSianulado(maecond_detalle.getSianulado());
							  maecond_detalleBean.setTipomoneda(maecond_detalle.getTipomoneda());
							  maecond_detalleBean.setUsr_anula(maecond_detalle.getUsr_anula());
							  maecond_detalleBean.setUsrcre(maecond_detalle.getUsrcre());
							  maecond_detalleBean.setUsrmod(maecond_detalle.getUsrmod());
							  maeconddetalle.add(maecond_detalleBean);					  
						  }
						  Collections.sort(maeconddetalle, new Comparator<MaeDetCondicionDetalleBean>(){

							    @Override
							    public int compare(MaeDetCondicionDetalleBean o1, MaeDetCondicionDetalleBean o2) {
							        return o1.getSecuencia().compareTo(o2.getSecuencia());
							    }
							});
						  
						  renta.setMaecondiciondetalle(maeconddetalle);
					 }
				}
			 }
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return listaRentas;
	}
   public void generarReporteContratoNuevo(String tipoarchivo) {
		
   
		String  reportPath=  ConstantesReporteSGI.REP_CONTRATOS_VIGENTES;
		Map<String, Object> parameters = new HashMap<String, Object>();
	   
	    parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		parameters.put("CONDICION","CONTRATOS VIGENTES AL "+FuncionesHelper.fechaToString(new Date()));
	   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Vigentes_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Condición_Arrendamiento(listaContratoBean);
		}
	   
}
   public void generarReporteUpa(String tipoarchivo) throws FileNotFoundException{
	   if (tipoarchivo.equals("pdf")) {
		  // report = reporteGeneradorService.generarPDF(reportPath, parameters, listaContratoBean,"ReporteContrato_Vigentes_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_Upa(listaUpas);
		}
   }
   public void limpiarValores(){
		  desdeInicioContrato=null;
		  hastaInicioContrato=null;
		  desdeFinContrato=null;
		  hastaFinContrato=null;
		  inicializarValoresCondicionConsultaContrato();
		  listaContratoBean.clear();
	  
   }
   public void limpiarValoresConsultaUpa(){
	   listaUpas.clear();
	   upa= new Upa();
	   upaBean= new UpaBean();
	   dist="";
	   prov="";
	   depa="";
	   ubigeo="";
   }
   public void limpiarValoresConsultaInmueble(){
	   listaInmuebleMaestro.clear();
	   upa= new Upa();
	   upaBean= new UpaBean();
	   inmuebleBean= new InmuebleBean();
	   dist="";
	   prov="";
	   depa="";
	   ubigeo="";
   }
   public void limpiarValoresConsultaInquilino(){
	   listaInquilino.clear();
       inquilinoBean= new InquilinoBean();
   }
	public void cargarComboProvincias() {
		listaProvincias = getInmuebleService().listarProvincias(depa);
	}
	public void cargarComboDistritos() {
		listaDistritos = getInmuebleService().listarDistritos(prov);
	}
	public void cargarUbigeo() {
		try{
		ubigeo=getInmuebleService().obtenerIdUbigeo(depa, prov,dist);
		}catch(Exception e ){
			addWarnMessage("Error","Ubigeo no encontrado");
			e.printStackTrace();
		}
		
	}
	public void validarDatosConsultaUpa(Upa upa, UpaBean upaBean){
		boolean resp=true;
		if(!depa.equalsIgnoreCase("0")){
			if(prov.equalsIgnoreCase("0")){
				resp=false;
				addWarnMessage("", "Seleccione una provincia para continuar. ");
			}else{
				if(dist.equalsIgnoreCase("0")){
					resp=false;
					addWarnMessage("", "Seleccione un distrito para continuar. ");
				}
			}
		}
		if(resp){
			consultaUpa(upa,upaBean);
		}
	}
	
	public void validarDatosConsultaInmuebleMaestro(InmuebleBean inm){
		boolean resp=true;
		if(!depa.equalsIgnoreCase("0")){
			if(prov.equalsIgnoreCase("0")){
				resp=false;
				addWarnMessage("", "Seleccione una provincia para continuar. ");
			}else{
				if(dist.equalsIgnoreCase("0")){
					resp=false;
					addWarnMessage("", "Seleccione un distrito para continuar. ");
				}
			}
		}
		if(resp){
			if(ubigeo!=null&&ubigeo.length()>0)
				inm.setUbigeo(ubigeo);
			consultaInmuebleMaestro(inm);
		}
	}
	public void validarDatosConsultaInquilino(InquilinoBean inq){
		boolean resp=true;
//		if(inq.getIdtipopersona()==0){
//			resp=false;
//			addWarnMessage("", "Seleccione un tipo de persona para continuar. ");
//		}
//		if(resp){
			consultaInquilino(inq);
			addWarnMessage("", "Se muestra la consulta . ");
//		}
	}
	public void consultaInquilino(InquilinoBean inq){
		try{
			listaInquilino.clear();
			listaInquilino= inquilinoService.listaInquilinos(inq);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void consultaInmuebleMaestro(InmuebleBean inm){
		try{
			listaInmuebleMaestro.clear();	
			listaInmuebleMaestro=inmuebleService.listarInmuebleMaestro(inm);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void consultaUpa(Upa upa,UpaBean upaBean){
		try{
			listaUpas.clear();
		    listaUpas=upaService.listarUpasxConsulta(upa,upaBean,ubigeo);
		    if(listaUpas!=null && listaUpas.size()>0){
		    	addInfoMessage("Busqueda Exitosa", "Se le muestra la información buscada");
		    }else{
		    	addInfoMessage("No existe datos", "No se encontro ninguno registro de upa");
		    }
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	public void cambiarTipoPersona(){
		//inquilino = new Inquilino();
		if (inquilinoBean.getIdtipopersona()==1 ) {
			sipersonanatural=Boolean.TRUE;           
		} else if(inquilinoBean.getIdtipopersona()==2 ) {
			sipersonanatural=Boolean.FALSE;
		}else{
			
		}
		
		
	}
	public StreamedContent getReport() {
		return report;
	}

	public void setReport(StreamedContent report) {
		this.report = report;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public IContratoService getContratoService() {
		return contratoService;
	}

	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}

	public IRecaudacionCarteraService getCarteraService() {
		return carteraService;
	}

	public void setCarteraService(IRecaudacionCarteraService carteraService) {
		this.carteraService = carteraService;
	}

	public String getTipSelectedCondicionFinalizado() {
		return tipSelectedCondicionFinalizado;
	}

	public void setTipSelectedCondicionFinalizado(
			String tipSelectedCondicionFinalizado) {
		this.tipSelectedCondicionFinalizado = tipSelectedCondicionFinalizado;
	}

	public IReporteGeneradorService getReporteGeneradorService() {
		return reporteGeneradorService;
	}

	public void setReporteGeneradorService(
			IReporteGeneradorService reporteGeneradorService) {
		this.reporteGeneradorService = reporteGeneradorService;
	}

	public Date getDesdeCartera() {
		return desdeCartera;
	}
	public void setdesdecartera() {
		hastaCartera=desdeCartera;
	}
	public void setdesdecarteraTodo() {
		hastaCarteraTodo=desdeCarteraTodo;
	}
	public void setDesdeInicioContrato(){
		hastaInicioContrato=desdeInicioContrato;
	}
	public void setDesdeFinContrato(){
		hastaFinContrato=desdeFinContrato;
	}
	public void setdesdeContratoFinalizado() {
		hastaContratoFinalizado=desdeContratoFinalizado;
	}
	public void setDesdeCartera(Date desdeCartera) {
		this.desdeCartera = desdeCartera;
	}

	public Date getHastaCartera() {
		return hastaCartera;
	}

	public void setHastaCartera(Date hastaCartera) {
		this.hastaCartera = hastaCartera;
	}

	public Date getDesdeCarteraTodo() {
		return desdeCarteraTodo;
	}

	public void setDesdeCarteraTodo(Date desdeCarteraTodo) {
		this.desdeCarteraTodo = desdeCarteraTodo;
	}

	public Date getHastaCarteraTodo() {
		return hastaCarteraTodo;
	}

	public void setHastaCarteraTodo(Date hastaCarteraTodo) {
		this.hastaCarteraTodo = hastaCarteraTodo;
	}

	public Date getDesdeContratoFinalizado() {
		return desdeContratoFinalizado;
	}

	public void setDesdeContratoFinalizado(Date desdeContratoFinalizado) {
		this.desdeContratoFinalizado = desdeContratoFinalizado;
	}

	public Date getHastaContratoFinalizado() {
		return hastaContratoFinalizado;
	}

	public void setHastaContratoFinalizado(Date hastaContratoFinalizado) {
		this.hastaContratoFinalizado = hastaContratoFinalizado;
	}

	public List<CondicionBean> getListaContratoBean() {
		return listaContratoBean;
	}

	public void setListaContratoBean(List<CondicionBean> listaContratoBean) {
		this.listaContratoBean = listaContratoBean;
	}

	public List<String> getListacondicion() {
		return listacondicion;
	}

	public void setListacondicion(List<String> listacondicion) {
		this.listacondicion = listacondicion;
	}

	public List<String> getSelectedCondicionSeleccionado() {
		return selectedCondicionSeleccionado;
	}

	public void setSelectedCondicionSeleccionado(
			List<String> selectedCondicionSeleccionado) {
		this.selectedCondicionSeleccionado = selectedCondicionSeleccionado;
	}

	public List<String> getListaestado() {
		return listaestado;
	}

	public void setListaestado(List<String> listaestado) {
		this.listaestado = listaestado;
	}

	public List<String> getSelectedEstadoSeleccionado() {
		return selectedEstadoSeleccionado;
	}

	public void setSelectedEstadoSeleccionado(
			List<String> selectedEstadoSeleccionado) {
		this.selectedEstadoSeleccionado = selectedEstadoSeleccionado;
	}

	public CondicionBean getCondicionBeanSelected() {
		return condicionBeanSelected;
	}

	public void setCondicionBeanSelected(CondicionBean condicionBeanSelected) {
		this.condicionBeanSelected = condicionBeanSelected;
	}

	public Date getDesdeInicioContrato() {
		return desdeInicioContrato;
	}

	public void setDesdeInicioContrato(Date desdeInicioContrato) {
		this.desdeInicioContrato = desdeInicioContrato;
	}

	public Date getHastaInicioContrato() {
		return hastaInicioContrato;
	}

	public void setHastaInicioContrato(Date hastaInicioContrato) {
		this.hastaInicioContrato = hastaInicioContrato;
	}

	public Date getDesdeFinContrato() {
		return desdeFinContrato;
	}

	public void setDesdeFinContrato(Date desdeFinContrato) {
		this.desdeFinContrato = desdeFinContrato;
	}

	public Date getHastaFinContrato() {
		return hastaFinContrato;
	}

	public void setHastaFinContrato(Date hastaFinContrato) {
		this.hastaFinContrato = hastaFinContrato;
	}

	public IRecaudacionReporteService getReporteRecaudacionService() {
		return reporteRecaudacionService;
	}

	public void setReporteRecaudacionService(
			IRecaudacionReporteService reporteRecaudacionService) {
		this.reporteRecaudacionService = reporteRecaudacionService;
	}

	public IInmuebleService getInmuebleService() {
		return inmuebleService;
	}

	public void setInmuebleService(IInmuebleService inmuebleService) {
		this.inmuebleService = inmuebleService;
	}

	public List<String> getListaDepartamentos() {
		return listaDepartamentos;
	}

	public void setListaDepartamentos(List<String> listaDepartamentos) {
		this.listaDepartamentos = listaDepartamentos;
	}

	public String getDepa() {
		return depa;
	}

	public void setDepa(String depa) {
		this.depa = depa;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public List<String> getListaProvincias() {
		return listaProvincias;
	}

	public void setListaProvincias(List<String> listaProvincias) {
		this.listaProvincias = listaProvincias;
	}

	public List<String> getListaDistritos() {
		return listaDistritos;
	}

	public void setListaDistritos(List<String> listaDistritos) {
		this.listaDistritos = listaDistritos;
	}

	public List<Upa> getListaUpas() {
		return listaUpas;
	}

	public void setListaUpas(List<Upa> listaUpas) {
		this.listaUpas = listaUpas;
	}

	public IUpaService getUpaService() {
		return upaService;
	}

	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}

	public Upa getUpa() {
		return upa;
	}

	public void setUpa(Upa upa) {
		this.upa = upa;
	}

	public List<Uso> getListaTipoUso() {
		return listaTipoUso;
	}

	public void setListaTipoUso(List<Uso> listaTipoUso) {
		this.listaTipoUso = listaTipoUso;
	}

	public Upa getUpaselected() {
		return upaselected;
	}

	public void setUpaselected(Upa upaselected) {
		this.upaselected = upaselected;
	}

	public String getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	public List<Tipotitularidad> getListaTipoTitularidad() {
		return listaTipoTitularidad;
	}

	public void setListaTipoTitularidad(List<Tipotitularidad> listaTipoTitularidad) {
		this.listaTipoTitularidad = listaTipoTitularidad;
	}

	public UpaBean getUpaBean() {
		return upaBean;
	}

	public void setUpaBean(UpaBean upaBean) {
		this.upaBean = upaBean;
	}

	public Inmueblemaestro getInmuebleSeleccionado() {
		return inmuebleSeleccionado;
	}

	public void setInmuebleSeleccionado(Inmueblemaestro inmuebleSeleccionado) {
		this.inmuebleSeleccionado = inmuebleSeleccionado;
	}

	public List<InmuebleBean> getListaInmuebleMaestro() {
		return listaInmuebleMaestro;
	}

	public void setListaInmuebleMaestro(List<InmuebleBean> listaInmuebleMaestro) {
		this.listaInmuebleMaestro = listaInmuebleMaestro;
	}

	public Inmueblemaestro getInmuebleMaestro() {
		return inmuebleMaestro;
	}

	public void setInmuebleMaestro(Inmueblemaestro inmuebleMaestro) {
		this.inmuebleMaestro = inmuebleMaestro;
	}

	public List<Tipozona> getListaTipoZona() {
		return listaTipoZona;
	}

	public void setListaTipoZona(List<Tipozona> listaTipoZona) {
		this.listaTipoZona = listaTipoZona;
	}

	public Tipozona getTipozona() {
		return tipozona;
	}

	public void setTipozona(Tipozona tipozona) {
		this.tipozona = tipozona;
	}

	public InmuebleBean getInmuebleBean() {
		return inmuebleBean;
	}

	public void setInmuebleBean(InmuebleBean inmuebleBean) {
		this.inmuebleBean = inmuebleBean;
	}

	public Inquilino getInquilino() {
		return inquilino;
	}

	public void setInquilino(Inquilino inquilino) {
		this.inquilino = inquilino;
	}

	public Integer getIdtipopersona() {
		return idtipopersona;
	}

	public void setIdtipopersona(Integer idtipopersona) {
		this.idtipopersona = idtipopersona;
	}

	public List<Tipopersona> getListaTipoPersona() {
		return listaTipoPersona;
	}

	public void setListaTipoPersona(List<Tipopersona> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}

	public IInquilinoService getInquilinoService() {
		return inquilinoService;
	}

	public void setInquilinoService(IInquilinoService inquilinoService) {
		this.inquilinoService = inquilinoService;
	}

	public Boolean getSipersonanatural() {
		return sipersonanatural;
	}

	public void setSipersonanatural(Boolean sipersonanatural) {
		this.sipersonanatural = sipersonanatural;
	}

	public InquilinoBean getInquilinoBean() {
		return inquilinoBean;
	}

	public void setInquilinoBean(InquilinoBean inquilinoBean) {
		this.inquilinoBean = inquilinoBean;
	}

	public List<InquilinoBean> getListaInquilino() {
		return listaInquilino;
	}

	public void setListaInquilino(List<InquilinoBean> listaInquilino) {
		this.listaInquilino = listaInquilino;
	}
	
	
	
}

