//package pe.gob.sblm.sgi.controller;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.PostConstruct;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.ViewScoped;
//import net.sf.jasperreports.engine.JasperPrint;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.primefaces.model.DefaultStreamedContent;
//import org.primefaces.model.StreamedContent;
//import pe.gob.sblm.sgi.bean.ComprobanteBean;
//import pe.gob.sblm.sgi.bean.CuotaBean;
//import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
//import pe.gob.sblm.sgi.service.ICarteraService;
//import pe.gob.sblm.sgi.service.IContratoService;
//import pe.gob.sblm.sgi.service.IReporteGerencialService;
//import pe.gob.sblm.sgi.service.ITipoCambioService;
//import pe.gob.sblm.sgi.service.IUpaService;
//import pe.gob.sblm.sgi.util.Almanaque;
//import pe.gob.sblm.sgi.util.Constantes;
//import pe.gob.sblm.sgi.util.ExportarArchivo;
//import pe.gob.sblm.sgi.util.FuncionesHelper;
//
//@ViewScoped
//@ManagedBean(name = "reportegerencialMB")
//public class ReporteGerencialManagedBean extends BaseController implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//	private String value;
//	private Date fechaconsultacarteraactiva=new Date();
//	
//	
//	@ManagedProperty(value = "#{reportegerencialService}")
//	private IReporteGerencialService reportegerencialService;
//	private JasperPrint jasperPrint;
//	
//	@ManagedProperty(value = "#{tipocambioService}")
//	private ITipoCambioService tipocambiService;
//	
//	@ManagedProperty(value = "#{usuarioMB}")
//	public UsuarioManagedBean userMB;
//	
//	@ManagedProperty(value = "#{carteraService}")
//	private transient ICarteraService carteraService;
//	
//	@ManagedProperty(value = "#{contratoService}")
//	private transient IContratoService contratoService;
//
//	@ManagedProperty(value = "#{upaService}")
//	private transient IUpaService upaService;
//	
//	@ManagedProperty(value = "#{tipocambioService}")
//	private transient ITipoCambioService tipocambioService;
//	
//	private StreamedContent report;
//	private Double tipcambio;
//	List<ComprobanteBean> lista= new ArrayList<ComprobanteBean>();
//	private ArrayList<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
//	private ArrayList<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
//	
//	@PostConstruct
//	public void init(){
//		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(new Date()));
//	}
//	
//
//	
//	public void elegirReporte(String tipo){
//		
//	}
//	
//	public void reporte1() {
//		  int cantLocales;
//		  int cantOficinas;
//		  int cantViviendas;
//		  int cantTotal;
//		
//		  cantLocales=reportegerencialService.obtenerCantidadUpaUso("LOCAL","1");
//		  cantOficinas=reportegerencialService.obtenerCantidadUpaUso("OFICINA","1");
//		  cantViviendas=reportegerencialService.obtenerCantidadUpaUso("VIVIENDA","1");
//		  cantTotal=reportegerencialService.obtenerCantidadUpaUso("VIVIENDA","*");
//			
//		   String  reportPath=  "1reporteClasificacionSegunUso";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("cantLocales",cantLocales);
//		   parameters.put("cantOficinas",cantOficinas );
//		   parameters.put("cantViviendas",cantViviendas );
//		   parameters.put("cantTotal",cantTotal );
//		   report = generarPDF(reportPath, parameters, null);  
//		
//	}
//	
//	public void reporte2(){
//		
//		Double potencialRentaLocalSoles= 0.0,potencialRentaLocalDolares= 0.0,potencialRentaOficinaSoles= 0.0,potencialRentaOficinaDolares= 0.0,potencialRentaViviendaSoles= 0.0,potencialRentaViviendaDolares= 0.0;
//		
//		Calendar intervalo = Calendar.getInstance();
//		intervalo.setTime(new Date());
//		
//		String Hasta =FuncionesHelper.fechaToString(intervalo.getTime());
//		intervalo.add(Calendar.DAY_OF_YEAR, -364);
//		String Desde=FuncionesHelper.fechaToString(intervalo.getTime());
//		
//		if (obtenerTipoCambioDia()) {
//			
//		List<Integer> listaIdUpaBean= new ArrayList<Integer>();
//		listaIdUpaBean=contratoService.listarUpasCarteraActiva();
//		
//		Calendar fechacarteraActiva = Calendar.getInstance();
//		fechacarteraActiva.add(Calendar.MONTH, -12);
//		Calendar fecCancelacion = Calendar.getInstance();
//		
//		for (Integer idUpaBean : listaIdUpaBean) {
//			
//			UpaPagadorDeudorBean pagadorDeudorBean= new UpaPagadorDeudorBean();
//			pagadorDeudorBean=carteraService.obtenerTransaccionUltimaFechaCancelacion(idUpaBean,Desde,Hasta,"");
//		
//				if (pagadorDeudorBean!=null) {
//					fecCancelacion.setTime(pagadorDeudorBean.getFecCancelacion());
//					
//					if (pagadorDeudorBean.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
//						int i=0;
//						Calendar ini=Calendar.getInstance();
//						ini.setTime(pagadorDeudorBean.getIniciocobro());
//						
//						Calendar fin=Calendar.getInstance();
//						fin.setTime(pagadorDeudorBean.getFincobro());
//						
//						Calendar ahora=Calendar.getInstance();
//						
//						if (fin.compareTo(ahora)>0) {
//							fin.setTime(ahora.getTime());
//						}
//						
//						while (ini.compareTo(fin) <= 0) {
//							ini.add(Calendar.MONTH, 1);
//						    i++;
//						}
//						
//						if (i<=12) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota1());
//						}else if (i<=24) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota2());
//						}else if (i<=36) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota3());
//						}else if (i<=48) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota4());
//						}else if (i<=60) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota5());
//						}else {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota6());
//						}
//					}
//					
//					if (pagadorDeudorBean.getMoneda().equals("S")) {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							potencialRentaLocalSoles=potencialRentaLocalSoles+pagadorDeudorBean.getRenta();
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							potencialRentaViviendaSoles= potencialRentaViviendaSoles+pagadorDeudorBean.getRenta();
//						}else {
//							potencialRentaOficinaSoles= potencialRentaOficinaSoles+pagadorDeudorBean.getRenta();
//						}
//					} else {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							potencialRentaLocalDolares= potencialRentaLocalDolares+pagadorDeudorBean.getRenta();
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							potencialRentaViviendaDolares= potencialRentaViviendaDolares+pagadorDeudorBean.getRenta();
//						}else {
//							potencialRentaOficinaDolares =potencialRentaOficinaDolares+pagadorDeudorBean.getRenta();
//						}
//					}
//		}
//		
//		   String  reportPath=  "2reportePotencialRentaUpasSegunUso";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
//		   parameters.put("potencialRentaLocalSoles",potencialRentaLocalSoles);
//		   parameters.put("potencialRentaViviendaSoles",potencialRentaViviendaSoles);
//		   parameters.put("potencialRentaOficinaSoles",potencialRentaOficinaSoles);
//		   parameters.put("potencialRentaLocalDolares",potencialRentaLocalDolares);
//		   parameters.put("potencialRentaViviendaDolares",potencialRentaViviendaDolares);
//		   parameters.put("potencialRentaOficinaDolares",potencialRentaOficinaDolares);
//		   
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	}
//	
//	public void reporte3(){	
//		Double potencialRentaLocalSoles= 0.0,potencialRentaLocalDolares= 0.0,potencialRentaOficinaSoles= 0.0,potencialRentaOficinaDolares= 0.0,potencialRentaViviendaSoles= 0.0,potencialRentaViviendaDolares= 0.0;
//		int nroviviendas = 0,nrooficinas = 0,nrolocales = 0;
//		
//		Calendar intervalo = Calendar.getInstance();
//		intervalo.setTime(new Date());
//		
//		String Hasta =FuncionesHelper.fechaToString(intervalo.getTime());
//		intervalo.add(Calendar.DAY_OF_YEAR, -364);
//		String Desde=FuncionesHelper.fechaToString(intervalo.getTime());
//		
//		if (obtenerTipoCambioDia()) {
//			
//			
//		List<Integer> listaIdUpaBean= new ArrayList<Integer>();
//		listaIdUpaBean=contratoService.listarUpasCarteraActiva();
//		
//		Calendar fechacarteraActiva = Calendar.getInstance();
//		fechacarteraActiva.add(Calendar.MONTH, -12);
//		Calendar fecCancelacion = Calendar.getInstance();
//		
//		for (Integer idUpaBean : listaIdUpaBean) {
//			
//			UpaPagadorDeudorBean pagadorDeudorBean= new UpaPagadorDeudorBean();
//			pagadorDeudorBean=carteraService.obtenerTransaccionUltimaFechaCancelacion(idUpaBean,Desde,Hasta,"");
//		
//				if (pagadorDeudorBean!=null) {
//					fecCancelacion.setTime(pagadorDeudorBean.getFecCancelacion());
//					
//					if (pagadorDeudorBean.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
//						int i=0;
//						Calendar ini=Calendar.getInstance();
//						ini.setTime(pagadorDeudorBean.getIniciocobro());
//						
//						Calendar fin=Calendar.getInstance();
//						fin.setTime(pagadorDeudorBean.getFincobro());
//						
//						Calendar ahora=Calendar.getInstance();
//						
//						if (fin.compareTo(ahora)>0) {
//							fin.setTime(ahora.getTime());
//						}
//						
//						while (ini.compareTo(fin) <= 0) {
//							ini.add(Calendar.MONTH, 1);
//						    i++;
//						}
//						
//						if (i<=12) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota1());
//						}else if (i<=24) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota2());
//						}else if (i<=36) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota3());
//						}else if (i<=48) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota4());
//						}else if (i<=60) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota5());
//						}else {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota6());
//						}
//					}
//					
//					if (pagadorDeudorBean.getMoneda().equals("S")) {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							potencialRentaLocalSoles=potencialRentaLocalSoles+pagadorDeudorBean.getRenta();
//							nrolocales++;
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							potencialRentaViviendaSoles= potencialRentaViviendaSoles+pagadorDeudorBean.getRenta();
//							nroviviendas++;
//						}else {
//							potencialRentaOficinaSoles= potencialRentaOficinaSoles+pagadorDeudorBean.getRenta();
//							nrooficinas++;
//						}
//					} else {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							potencialRentaLocalDolares= potencialRentaLocalDolares+pagadorDeudorBean.getRenta();
//							nrolocales++;
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							potencialRentaViviendaDolares= potencialRentaViviendaDolares+pagadorDeudorBean.getRenta();
//							nroviviendas++;
//						}else {
//							potencialRentaOficinaDolares =potencialRentaOficinaDolares+pagadorDeudorBean.getRenta();
//							nrooficinas++;
//						}
//					}
//		}
//		
//		   String  reportPath=  "3report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
//		   parameters.put("potencialRentaLocalSoles",potencialRentaLocalSoles+potencialRentaLocalDolares*tipcambio);
//		   parameters.put("potencialRentaViviendaSoles",potencialRentaViviendaSoles+potencialRentaViviendaDolares*tipcambio);
//		   parameters.put("potencialRentaOficinaSoles",potencialRentaOficinaSoles*tipcambio*potencialRentaOficinaDolares);
//		   parameters.put("nrolocales",nrolocales);
//		   parameters.put("nrooficinas",nrooficinas);
//		   parameters.put("nroviviendas",nroviviendas);
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	}
//	
//	public void reporte4(){	
//		int nroviviendasActiva = 0,nrooficinasActiva  = 0,nrolocalesActiva  = 0,nroviviendasPesada = 0,nrooficinasPesada  = 0,nrolocalesPesada  = 0;
//		
//		Calendar intervalo = Calendar.getInstance();
//		intervalo.setTime(new Date());
//		
//		String Hasta =FuncionesHelper.fechaToString(intervalo.getTime());
//		intervalo.add(Calendar.DAY_OF_YEAR, -364);
//		String Desde=FuncionesHelper.fechaToString(intervalo.getTime());
//		
//		
//		if (obtenerTipoCambioDia()) {
//			
//		List<Integer> listaIdUpaBean= new ArrayList<Integer>();
//		listaIdUpaBean=contratoService.listarUpasCarteraActiva();
//		
//		
//		Calendar fechacarteraActiva = Calendar.getInstance();
//		Calendar actual = Calendar.getInstance();
//		fechacarteraActiva.add(Calendar.MONTH, -12);
//		Calendar fecCancelacion = Calendar.getInstance();
//		
//		for (Integer idUpaBean : listaIdUpaBean) {
//			
//			UpaPagadorDeudorBean pagadorDeudorBean= new UpaPagadorDeudorBean();
//			pagadorDeudorBean=carteraService.obtenerTransaccionUltimaFechaCancelacion(idUpaBean,Desde,Hasta,"");
//		
//				if (pagadorDeudorBean!=null) {
//					fecCancelacion.setTime(pagadorDeudorBean.getFecCancelacion());
//					if (pagadorDeudorBean.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
//						int i=0;
//						Calendar ini=Calendar.getInstance();
//						ini.setTime(pagadorDeudorBean.getIniciocobro());
//						
//						Calendar fin=Calendar.getInstance();
//						fin.setTime(pagadorDeudorBean.getFincobro());
//						
//						Calendar ahora=Calendar.getInstance();
//						
//						if (fin.compareTo(ahora)>0) {
//							fin.setTime(ahora.getTime());
//						}
//						
//						while (ini.compareTo(fin) <= 0) {
//							ini.add(Calendar.MONTH, 1);
//						    i++;
//						}
//						
//						if (i<=12) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota1());
//						}else if (i<=24) {	
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota2());
//						}else if (i<=36) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota3());
//						}else if (i<=48) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota4());
//						}else if (i<=60) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota5());
//						}else {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota6());
//						}
//					}
//					
//					if (fechacarteraActiva.compareTo(fecCancelacion)<0 ) {
//					if (pagadorDeudorBean.getMoneda().equals("S")) {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							nrolocalesActiva++;
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							nroviviendasActiva++;
//						}else {
//							nrooficinasActiva++;
//						}
//					} else {
//						if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//							nrolocalesActiva++;
//						}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//							nroviviendasActiva++;
//						}else {
//							nrooficinasActiva++;
//						}
//					}}else {
//						if (pagadorDeudorBean.getMoneda().equals("S")) {
//							if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//								nrolocalesPesada++;
//							}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//								nroviviendasPesada++;
//							}else {
//								nrooficinasPesada++;
//							}
//						} else {
//							if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//								nrolocalesPesada++;
//							}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//								nroviviendasPesada++;
//							}else {
//								nrooficinasPesada++;
//							}
//						}
//						
//					}
//		}
//		
//		   String  reportPath=  "4report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("nrolocalesActiva",nrolocalesActiva);
//		   parameters.put("nrooficinasActiva",nrooficinasActiva);
//		   parameters.put("nroviviendasActiva",nroviviendasActiva);
//		   parameters.put("nrolocalesPesada",nrolocalesPesada);
//		   parameters.put("nrooficinasPesada",nrooficinasPesada);
//		   parameters.put("nroviviendasPesada",nroviviendasPesada);
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	}
//	
//	public void reporte5(){	
//		int nroviviendasActivaSinDeuda = 0,nroviviendasActivaConDeuda = 0,nrooficinasActivaConDeuda  = 0,nrolocalesActivaSinDeuda  = 0,nrooficinasActivaSinDeuda  = 0,nrolocalesActivaConDeuda  = 0,nroviviendasPesada = 0,nrooficinasPesada  = 0,nrolocalesPesada  = 0;
//		
//		Calendar intervalo = Calendar.getInstance();
//		intervalo.setTime(new Date());
//		
//		String Hasta =FuncionesHelper.fechaToString(intervalo.getTime());
//		intervalo.add(Calendar.DAY_OF_YEAR, -364);
//		String Desde=FuncionesHelper.fechaToString(intervalo.getTime());
//		
//		if (obtenerTipoCambioDia()) {
//			
//			List<Integer> listaIdUpaBean= new ArrayList<Integer>();
//			listaIdUpaBean=contratoService.listarUpasCarteraActiva();
//		
//		Calendar fechacarteraActiva = Calendar.getInstance();
//		Calendar actual = Calendar.getInstance();
//		fechacarteraActiva.add(Calendar.MONTH, -12);
//		Calendar fecCancelacion = Calendar.getInstance();
//		
//		for (Integer idUpaBean : listaIdUpaBean) {
//			
//			UpaPagadorDeudorBean pagadorDeudorBean= new UpaPagadorDeudorBean();
//			pagadorDeudorBean=carteraService.obtenerTransaccionUltimaFechaCancelacion(idUpaBean,Desde,Hasta,"");
//		
//				if (pagadorDeudorBean!=null) {
//					boolean value = actual.get(Calendar.YEAR) == pagadorDeudorBean.getAnio() && actual.get(Calendar.MONTH)+1 == Integer.parseInt(Almanaque.mesanumero(pagadorDeudorBean.getMes()));
//					fecCancelacion.setTime(pagadorDeudorBean.getFecCancelacion());
//					if (pagadorDeudorBean.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
//						int i=0;
//						Calendar ini=Calendar.getInstance();
//						ini.setTime(pagadorDeudorBean.getIniciocobro());
//						
//						Calendar fin=Calendar.getInstance();
//						fin.setTime(pagadorDeudorBean.getFincobro());
//						
//						Calendar ahora=Calendar.getInstance();
//						
//						if (fin.compareTo(ahora)>0) {
//							fin.setTime(ahora.getTime());
//						}
//						
//						while (ini.compareTo(fin) <= 0) {
//							ini.add(Calendar.MONTH, 1);
//						    i++;
//						}
//						
//						if (i<=12) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota1());
//						}else if (i<=24) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota2());
//						}else if (i<=36) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota3());
//						}else if (i<=48) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota4());
//						}else if (i<=60) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota5());
//						}else {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota6());
//						}
//					}
//					
//					if (fechacarteraActiva.compareTo(fecCancelacion)<0 ) {
//						if (value) {
//							if (pagadorDeudorBean.getMoneda().equals("S")) {
//								if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//									nrolocalesActivaSinDeuda++;
//								}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//									nroviviendasActivaSinDeuda++;
//								}else {
//									nrooficinasActivaSinDeuda++;
//								}
//							} else {
//								if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//									nrolocalesActivaSinDeuda++;
//								}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//									nroviviendasActivaSinDeuda++;
//								}else {
//									nrooficinasActivaSinDeuda++;
//								}}
//							}else {
//								if (pagadorDeudorBean.getMoneda().equals("S")) {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										nrolocalesActivaConDeuda++;
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										nroviviendasActivaConDeuda++;
//									}else {
//										nrooficinasActivaConDeuda++;
//									}
//								} else {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										nrolocalesActivaConDeuda++;
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										nroviviendasActivaConDeuda++;
//									}else {
//										nrooficinasActivaConDeuda++;
//									}
//									}
//							}
//
//							}else {
//								if (pagadorDeudorBean.getMoneda().equals("S")) {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										nrolocalesPesada++;
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										nroviviendasPesada++;
//									}else {
//										nrooficinasPesada++;
//									}
//								} else {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										nrolocalesPesada++;
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										nroviviendasPesada++;
//									}else {
//										nrooficinasPesada++;
//									}
//								}
//							}
//					}
//								
//								
//							
//		}
//		
//		   String  reportPath=  "5report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("nrolocalesActivaConDeuda",nrolocalesActivaConDeuda);
//		   parameters.put("nrooficinasActivaConDeuda",nrooficinasActivaConDeuda);
//		   parameters.put("nroviviendasActivaConDeuda",nroviviendasActivaConDeuda);
//		   parameters.put("nrolocalesActivaSinDeuda",nrolocalesActivaSinDeuda);
//		   parameters.put("nrooficinasActivaSinDeuda",nrooficinasActivaSinDeuda);
//		   parameters.put("nroviviendasActivaSinDeuda",nroviviendasActivaSinDeuda);
//		   parameters.put("nrolocalesPesada",nrolocalesPesada);
//		   parameters.put("nrooficinasPesada",nrooficinasPesada);
//		   parameters.put("nroviviendasPesada",nroviviendasPesada);
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	
//	
//	public void reporte6(){
//		Double localConDeudaSolesA= 0.0,localConDeudaDolaresA= 0.0,oficinaConDeudaSolesA= 0.0,oficinaConDeudaDolaresA= 0.0,viviendaConDeudaSolesA= 0.0,viviendaConDeudaDolaresA= 0.0;
//		Double localConDeudaSolesP= 0.0,localConDeudaDolaresP= 0.0,oficinaConDeudaSolesP= 0.0,oficinaConDeudaDolaresP= 0.0,viviendaConDeudaSolesP= 0.0,viviendaConDeudaDolaresP= 0.0;
//		
//		Calendar intervalo = Calendar.getInstance();
//		intervalo.setTime(new Date());
//		
//		String Hasta =FuncionesHelper.fechaToString(intervalo.getTime());
//		intervalo.add(Calendar.DAY_OF_YEAR, -364);
//		String Desde=FuncionesHelper.fechaToString(intervalo.getTime());
//		
//		if (obtenerTipoCambioDia()) {
//			
//		List<Integer> listaIdUpaBean= new ArrayList<Integer>();
//		listaIdUpaBean=contratoService.listarUpasCarteraActiva();
//		
//		
//		Calendar fechacarteraActiva = Calendar.getInstance();
//		Calendar actual = Calendar.getInstance();
//		fechacarteraActiva.add(Calendar.MONTH, -12);
//		Calendar fecCancelacion = Calendar.getInstance();
//		
//		for (Integer idUpaBean : listaIdUpaBean) {
//			
//			UpaPagadorDeudorBean pagadorDeudorBean= new UpaPagadorDeudorBean();
//			pagadorDeudorBean=carteraService.obtenerTransaccionUltimaFechaCancelacion(idUpaBean,Desde,Hasta,"");
//		
//				if (pagadorDeudorBean!=null) {
//					boolean value = actual.get(Calendar.YEAR) == pagadorDeudorBean.getAnio() && actual.get(Calendar.MONTH)+1 == Integer.parseInt(Almanaque.mesanumero(pagadorDeudorBean.getMes()));
//					fecCancelacion.setTime(pagadorDeudorBean.getFecCancelacion());
//					if (pagadorDeudorBean.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
//						int i=0;
//						Calendar ini=Calendar.getInstance();
//						ini.setTime(pagadorDeudorBean.getIniciocobro());
//						
//						Calendar fin=Calendar.getInstance();
//						fin.setTime(pagadorDeudorBean.getFincobro());
//						
//						Calendar ahora=Calendar.getInstance();
//						
//						if (fin.compareTo(ahora)>0) {
//							fin.setTime(ahora.getTime());
//						}
//						
//						while (ini.compareTo(fin) <= 0) {
//							ini.add(Calendar.MONTH, 1);
//						    i++;
//						}
//						
//						if (i<=12) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota1());
//						}else if (i<=24) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota2());
//						}else if (i<=36) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota3());
//						}else if (i<=48) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota4());
//						}else if (i<=60) {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota5());
//						}else {
//							pagadorDeudorBean.setRenta(pagadorDeudorBean.getCuota6());
//						}
//					}
//					
//					if (fechacarteraActiva.compareTo(fecCancelacion)<0 ) {
//						if (!value) {
//							if (pagadorDeudorBean.getMoneda().equals("S")) {
//								if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//									localConDeudaSolesA=localConDeudaSolesA+pagadorDeudorBean.getRenta();
//								}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//									viviendaConDeudaSolesA=viviendaConDeudaSolesA+pagadorDeudorBean.getRenta();
//								}else {
//									oficinaConDeudaSolesA=oficinaConDeudaSolesA+pagadorDeudorBean.getRenta();
//								}
//							} else {
//								if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//									localConDeudaDolaresA=localConDeudaDolaresA+pagadorDeudorBean.getRenta();
//								}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//									viviendaConDeudaDolaresA=viviendaConDeudaDolaresA+pagadorDeudorBean.getRenta();
//								}else {
//									oficinaConDeudaDolaresA=oficinaConDeudaDolaresA+pagadorDeudorBean.getRenta();
//								}
//								}
//							}
//
//							}else {
//								if (pagadorDeudorBean.getMoneda().equals("S")) {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										localConDeudaSolesP=localConDeudaSolesP+pagadorDeudorBean.getRenta();
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										viviendaConDeudaSolesP=viviendaConDeudaSolesP+pagadorDeudorBean.getRenta();
//									}else {
//										oficinaConDeudaSolesP=oficinaConDeudaSolesP+pagadorDeudorBean.getRenta();
//									}
//								} else {
//									if (pagadorDeudorBean.getUso().equals("LOCAL")) {
//										localConDeudaDolaresP=localConDeudaDolaresP+pagadorDeudorBean.getRenta();
//									}else if (pagadorDeudorBean.getUso().equals("VIVIENDA")){
//										viviendaConDeudaDolaresP=viviendaConDeudaDolaresP+pagadorDeudorBean.getRenta();
//									}else {
//										oficinaConDeudaDolaresP=oficinaConDeudaDolaresP+pagadorDeudorBean.getRenta();
//									}
//								}
//							}
//					}
//		}
//		
//		   String  reportPath=  "6report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   System.out.println(localConDeudaSolesA+localConDeudaDolaresA*tipcambio);
//		   parameters.put("localConDeudaSolesA",localConDeudaSolesA+localConDeudaDolaresA*tipcambio);
//		   System.out.println(viviendaConDeudaSolesA+viviendaConDeudaDolaresA*tipcambio);
//		   parameters.put("viviendaConDeudaSolesA",viviendaConDeudaSolesA+viviendaConDeudaDolaresA*tipcambio);
//		   System.out.println(oficinaConDeudaSolesA+oficinaConDeudaSolesP*tipcambio);
//		   parameters.put("oficinaConDeudaSolesA",oficinaConDeudaSolesA+oficinaConDeudaSolesP*tipcambio);
//		   System.out.println(localConDeudaSolesP+localConDeudaDolaresP*tipcambio);
//		   parameters.put("localConDeudaSolesP",localConDeudaSolesP+localConDeudaDolaresP*tipcambio);
//		   System.out.println(viviendaConDeudaSolesP+viviendaConDeudaDolaresP*tipcambio);
//		   parameters.put("viviendaConDeudaSolesP",viviendaConDeudaSolesP+viviendaConDeudaDolaresP*tipcambio);
//		   System.out.println(oficinaConDeudaSolesP+oficinaConDeudaDolaresP*tipcambio);
//		   parameters.put("oficinaConDeudaSolesP",oficinaConDeudaSolesP+oficinaConDeudaDolaresP*tipcambio);
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	
//	public void reporte7(){
//		int anio;
//		Double eneLocal= 0.0,febLocal= 0.0,marLocal= 0.0,abrLocal= 0.0,mayLocal= 0.0,junLocal= 0.0,julLocal= 0.0,agoLocal= 0.0,sepLocal= 0.0,octLocal= 0.0,novLocal= 0.0,dicLocal= 0.0;
//		Double eneVivienda= 0.0,febVivienda= 0.0,marVivienda= 0.0,abrVivienda= 0.0,mayVivienda= 0.0,junVivienda= 0.0,julVivienda= 0.0,agoVivienda= 0.0,sepVivienda= 0.0,octVivienda= 0.0,novVivienda= 0.0,dicVivienda= 0.0;
//		Double eneOficina= 0.0,febOficina= 0.0,marOficina= 0.0,abrOficina= 0.0,mayOficina= 0.0,junOficina= 0.0,julOficina= 0.0,agoOficina= 0.0,sepOficina= 0.0,octOficina= 0.0,novOficina= 0.0,dicOficina= 0.0;
//		Double eneGarantia= 0.0,febGarantia= 0.0,marGarantia= 0.0,abrGarantia= 0.0,mayGarantia= 0.0,junGarantia= 0.0,julGarantia= 0.0,agoGarantia= 0.0,sepGarantia= 0.0,octGarantia= 0.0,novGarantia= 0.0,dicGarantia= 0.0;
//		Double eneEspecial= 0.0,febEspecial= 0.0,marEspecial= 0.0,abrEspecial= 0.0,mayEspecial= 0.0,junEspecial= 0.0,julEspecial= 0.0,agoEspecial= 0.0,sepEspecial= 0.0,octEspecial= 0.0,novEspecial= 0.0,dicEspecial= 0.0;
//
//		if (obtenerTipoCambioDia()) {
//			
//			Calendar hasta = Calendar.getInstance();
//			
//			hasta.set(Calendar.YEAR,2014);
//			hasta.set(Calendar.MONTH, 12);
//			hasta.set(Calendar.DATE, 31);
//			hasta.set(Calendar.HOUR,0);
//			hasta.set(Calendar.MINUTE,0);
//			hasta.set(Calendar.SECOND,0);
//			hasta.set(Calendar.MILLISECOND,0);
//			hasta.add(Calendar.DAY_OF_YEAR, 1);
//			
//			Calendar desde = Calendar.getInstance();
//			desde.set(Calendar.YEAR,2014);
//			desde.set(Calendar.MONTH, 0);
//			desde.set(Calendar.DATE, 1);
//			desde.set(Calendar.HOUR,0);
//			desde.set(Calendar.MINUTE,0);
//			desde.set(Calendar.SECOND,0);
//			desde.set(Calendar.MILLISECOND,0);
//	        
//			 eneLocal=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","uso","LOCAL");
//			 eneVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","uso","VIVIENDA");
//			 eneOficina=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","uso","OFICINA");
//			 eneGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","garantia","");
//			 eneEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","especial","");
//			 
//			 febLocal=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","uso","LOCAL");
//			 febVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","uso","VIVIENDA");
//			 febOficina=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","uso","OFICINA");
//			 febGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","garantia","");
//			 febEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","especial","");
//			 
//			 marLocal=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","uso","LOCAL");
//			 marVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","uso","VIVIENDA");
//			 marOficina=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","uso","OFICINA");
//			 marGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","garantia","");
//			 marEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","especial","");
//			 
//			 abrLocal=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","uso","LOCAL");
//			 abrVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","uso","VIVIENDA");
//			 abrOficina=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","uso","OFICINA");
//			 abrGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","garantia","");
//			 abrEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","especial","");
//			 
//			 mayLocal=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","uso","LOCAL");
//			 mayVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","uso","VIVIENDA");
//			 mayOficina=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","uso","OFICINA");
//			 mayGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","garantia","");
//			 mayEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","especial","");
//			 
//			 junLocal=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","uso","LOCAL");
//			 junVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","uso","VIVIENDA");
//			 junOficina=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","uso","OFICINA");
//			 junGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","garantia","");
//			 junEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","especial","");
//			 
//			 julLocal=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","uso","LOCAL");
//			 julVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","uso","VIVIENDA");
//			 julOficina=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","uso","OFICINA");
//			 julGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","garantia","");
//			 julEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","especial","");
//			 
//			 agoLocal=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","uso","LOCAL");
//			 agoVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","uso","VIVIENDA");
//			 agoOficina=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","uso","OFICINA");
//			 agoGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","garantia","");
//			 agoEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","especial","");
//			 
//			 sepLocal=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","uso","LOCAL");
//			 sepVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","uso","VIVIENDA");
//			 sepOficina=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","uso","OFICINA");
//			 sepGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","garantia","");
//			 sepEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","especial","");
//			 
//			 octLocal=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","uso","LOCAL");
//			 octVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","uso","VIVIENDA");
//			 octOficina=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","uso","OFICINA");
//			 octGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","garantia","");
//			 octEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","especial","");
//			 
//			 novLocal=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","uso","LOCAL");
//			 novVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","uso","VIVIENDA");
//			 novOficina=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","uso","OFICINA");
//			 novGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","garantia","");
//			 novEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","especial","");
//			 
//			 dicLocal=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","uso","LOCAL");
//			 dicVivienda=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","uso","VIVIENDA");
//			 dicOficina=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","uso","OFICINA");
//			 dicGarantia=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","garantia","");
//			 dicEspecial=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","especial","");
// 
//	        
//
//		   String  reportPath=  "7report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("eneLocal",eneLocal);
//		   parameters.put("eneVivienda",eneVivienda);
//		   parameters.put("eneOficina",eneOficina);
//		   parameters.put("eneGarantia",eneGarantia);
//		   parameters.put("eneEspecial",eneEspecial);
//		   
//		   
//		   parameters.put("febLocal",febLocal);
//		   parameters.put("febVivienda",febVivienda);
//		   parameters.put("febOficina",febOficina);
//		   parameters.put("febGarantia",febGarantia);
//		   parameters.put("febEspecial",febEspecial);
//		   
//		   parameters.put("marLocal",marLocal);
//		   parameters.put("marVivienda",marVivienda);
//		   parameters.put("marOficina",marOficina);
//		   parameters.put("marGarantia",marGarantia);
//		   parameters.put("marEspecial",marEspecial);
//		   
//		   parameters.put("abrLocal",abrLocal);
//		   parameters.put("abrVivienda",abrVivienda);
//		   parameters.put("abrOficina",abrOficina);
//		   parameters.put("abrGarantia",abrGarantia);
//		   parameters.put("abrEspecial",abrEspecial);
//		   
//		   parameters.put("mayLocal",mayLocal);
//		   parameters.put("mayVivienda",mayVivienda);
//		   parameters.put("mayOficina",mayOficina);
//		   parameters.put("mayGarantia",mayGarantia);
//		   parameters.put("mayEspecial",mayEspecial);
//		   
//		   parameters.put("junLocal",junLocal);
//		   parameters.put("junVivienda",junVivienda);
//		   parameters.put("junOficina",junOficina);
//		   parameters.put("junGarantia",junGarantia);
//		   parameters.put("junEspecial",junEspecial);
//		   
//		   parameters.put("julLocal",julLocal);
//		   parameters.put("julVivienda",julVivienda);
//		   parameters.put("julOficina",julOficina);
//		   parameters.put("julGarantia",julGarantia);
//		   parameters.put("julEspecial",julEspecial);
//		   
//		   parameters.put("agoLocal",agoLocal);
//		   parameters.put("agoVivienda",agoVivienda);
//		   parameters.put("agoOficina",agoOficina);
//		   parameters.put("agoGarantia",agoGarantia);
//		   parameters.put("agoEspecial",agoEspecial);
//		   
//		   parameters.put("sepLocal",sepLocal);
//		   parameters.put("sepVivienda",sepVivienda);
//		   parameters.put("sepOficina",sepOficina);
//		   parameters.put("sepGarantia",sepGarantia);
//		   parameters.put("sepEspecial",sepEspecial);
//		   
//		   parameters.put("octLocal",octLocal);
//		   parameters.put("octVivienda",octVivienda);
//		   parameters.put("octOficina",octOficina);
//		   parameters.put("octGarantia",octGarantia);
//		   parameters.put("octEspecial",octEspecial);
//		   
//		   parameters.put("novLocal",novLocal);
//		   parameters.put("novVivienda",novVivienda);
//		   parameters.put("novOficina",novOficina);
//		   parameters.put("novGarantia",novGarantia);
//		   parameters.put("novEspecial",novEspecial);
//		   
//		   parameters.put("dicLocal",dicLocal);
//		   parameters.put("dicVivienda",dicVivienda);
//		   parameters.put("dicOficina",dicOficina);
//		   parameters.put("dicGarantia",dicGarantia);
//		   parameters.put("dicEspecial",dicEspecial);
//
//		   
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	
//public void reporte8()
//{
//		int anio;
//		Double ene= 0.0,feb= 0.0,mar= 0.0,abr= 0.0,may= 0.0,jun= 0.0,jul= 0.0,ago= 0.0,sep= 0.0,oct= 0.0,nov= 0.0,dic= 0.0;
//
//		if (obtenerTipoCambioDia()) {
//			
//			Calendar hasta = Calendar.getInstance();
//			
//			hasta.set(Calendar.YEAR,2014);
//			hasta.set(Calendar.MONTH, 12);
//			hasta.set(Calendar.DATE, 31);
//			hasta.set(Calendar.HOUR,0);
//			hasta.set(Calendar.MINUTE,0);
//			hasta.set(Calendar.SECOND,0);
//			hasta.set(Calendar.MILLISECOND,0);
//			hasta.add(Calendar.DAY_OF_YEAR, 1);
//			
//			Calendar desde = Calendar.getInstance();
//			desde.set(Calendar.YEAR,2014);
//			desde.set(Calendar.MONTH, 0);
//			desde.set(Calendar.DATE, 1);
//			desde.set(Calendar.HOUR,0);
//			desde.set(Calendar.MINUTE,0);
//			desde.set(Calendar.SECOND,0);
//			desde.set(Calendar.MILLISECOND,0);
//	        
//			 ene=carteraService.obtenerSumaComprobanteEspecializado("01-01-14","01-02-14","todo","");
//			 feb=carteraService.obtenerSumaComprobanteEspecializado("01-02-14","01-03-14","todo","");
//			 mar=carteraService.obtenerSumaComprobanteEspecializado("01-03-14","01-04-14","todo","");
//			 abr=carteraService.obtenerSumaComprobanteEspecializado("01-04-14","01-05-14","todo","");
//			 may=carteraService.obtenerSumaComprobanteEspecializado("01-05-14","01-06-14","todo","");
//			 jun=carteraService.obtenerSumaComprobanteEspecializado("01-06-14","01-07-14","todo","");
//			 jul=carteraService.obtenerSumaComprobanteEspecializado("01-07-14","01-08-14","todo","");
//			 ago=carteraService.obtenerSumaComprobanteEspecializado("01-08-14","01-09-14","todo","");
//			 sep=carteraService.obtenerSumaComprobanteEspecializado("01-09-14","01-10-14","todo","");
//			 oct=carteraService.obtenerSumaComprobanteEspecializado("01-10-14","01-11-14","todo","");
//			 nov=carteraService.obtenerSumaComprobanteEspecializado("01-11-14","01-12-14","todo","");
//			 dic=carteraService.obtenerSumaComprobanteEspecializado("01-12-14","01-01-15","todo","");
// 
//	        
//
//		   String  reportPath=  "8report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   parameters.put("ene",ene);
//		   parameters.put("feb",feb);
//		   parameters.put("mar",mar);
//		   parameters.put("abr",abr);
//		   parameters.put("may",may);
//		   parameters.put("jun",jun);
//		   parameters.put("jul",jul);
//		   parameters.put("ago",ago);
//		   parameters.put("sep",sep);
//		   parameters.put("oct",oct);
//		   parameters.put("nov",nov);
//		   parameters.put("dic",dic);
//
//		   
//		   report = generarPDF(reportPath, parameters, null); 
//		}
//		
//	}
//	
//	public void reporte9(String tipo){
//		
//		if (obtenerTipoCambioDia()) {
//			
//			List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
//			
//			
//			Calendar Hasta = Calendar.getInstance();
//			Calendar Desde = Calendar.getInstance();
//
//			Hasta.setTime(fechaconsultacarteraactiva);
//			Desde.setTime(fechaconsultacarteraactiva);
//
//			Desde.add(Calendar.DAY_OF_YEAR, -365);
//			
//			
//			listaCA=carteraService.obtenerInformacionCarteraActiva("",FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));
//		
//		
//		   String  reportPath=  "9report";
//		   Map<String, Object> parameters = new HashMap<String, Object>();
//		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//		   if (tipo.equals("pdf")) {
//			   report = generarPDF(reportPath, parameters, lista); 
//			} else {
//		       try {
//				XLS(listaCA);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} 
//			}
//		}
//		
//	}
//
//
//	public Double calcularMontoDeudaContratoPrecontrato() {
//		Double suma=0.0;
//		for (int i = 0; i < listaPendientesContratoPreContratoCuotaBean.size(); i++) {
//			suma=suma+listaPendientesContratoPreContratoCuotaBean.get(i).getMonto();
//			}
//		return suma;
//	}
//	
//	public Double calcularMontoDeudaSinContrato() {
//		Double suma=0.0;
//		for (int i = 0; i < listaPendientesSinContratoCuotaBean.size(); i++) {
//			suma=suma+listaPendientesSinContratoCuotaBean.get(i).getMonto();
//			}
//		return suma;
//	}
//	
//	public void reporte10(){
//
//
//}
//	
//public void reporte11(){	
//			   String  reportPath=  "Letra";
//			   Map<String, Object> parameters = new HashMap<String, Object>();
//			   parameters.put("parameter1","xxx");
//			   report = generarPDF(reportPath, parameters, null); 
//}
//	
//	public void XLS(List<UpaPagadorDeudorBean> lista) throws FileNotFoundException {
//
//		
//		HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Página 1");
//        
//        int rownum = 0;
//        for (int i = 0; i < lista.size(); i++) {
//            Row row = sheet.createRow(rownum++);
//           UpaPagadorDeudorBean bean= new UpaPagadorDeudorBean();
//           bean= lista.get(i);
//                
//                for (int j = 0; j < 13; j++) {
//                	Cell cell = row.createCell(j);
//                	if (j==0)
//                		cell.setCellValue((String)bean.getClaveUpa());
//                	 else if (j==1)
//                		 cell.setCellValue((String)bean.getDireccion());
//                		 else if (j==2)
//                			 cell.setCellValue((String)bean.getDistrito());
//                			 else if (j==3)
//                				 cell.setCellValue((String)bean.getNombreInquilino());
//	                			 else if (j==4)
//	                    			 cell.setCellValue((String)bean.getNombreocupante());
//	                    			 else if (j==5)
//	                    				 cell.setCellValue((String)bean.getCondicion());	
//		                    			 else if (j==6)
//			                				 cell.setCellValue((String)bean.getSituacion());
//				                			 else if (j==7)
//				                				 cell.setCellValue((String)bean.getUso());
//				                			 	else if (j==8)
//				                					 cell.setCellValue((Double)bean.getRenta());
//				                					 else if (j==9)
//				                						 cell.setCellValue((Integer)bean.getMesesdeuda());
//				                					 else if (j==10)
//				                						 cell.setCellValue((String)bean.getMoneda());
//				                						 else if (j==11)
//				                							 cell.setCellValue((Double)bean.getDeudatotal());
//					                						 else if (j==12)
//					                							 cell.setCellValue((String)bean.getNrocartera());
//				}
//		}
//
//         
//        try {
//            FileOutputStream out =  new FileOutputStream(new File("C:\\ReporteListaUpasDeudaActiva.xls"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Excel written successfully..");
//             
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        InputStream stream = new FileInputStream("c:\\ReporteListaUpasDeudaActiva.xls");
//		 
//        report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "ReporteListaUpasDeudaActiva_"+FuncionesHelper.fechaToString(new Date())+".xls");
//	}
//	
//		
//	
//	
//	@SuppressWarnings("static-access")
//	private StreamedContent generarPDF(String nombreReporte, Map<String, Object> parameters, 
//			List<?> lista){
//		ExportarArchivo printer = new ExportarArchivo();
//		StringBuilder jasperFile = new StringBuilder();
//		StringBuilder reporte = new StringBuilder();
//		jasperFile.append("C://SGI//reportes//Gerencial//"+nombreReporte+".jrxml");
//		try{
//			byte[] array = printer.exportPdf(jasperFile.toString(), parameters, lista);
//			InputStream stream = new ByteArrayInputStream(array);
//			reporte.append(nombreReporte);
//			reporte.append(".");
//			reporte.append(Constantes.EXTENSION_FORMATO_PDF);
//			return new DefaultStreamedContent(stream,  Constantes.APPLICATION_PDF, reporte.toString());
//		}catch(Exception e){
//			return null;
//		}
//	}
//	@SuppressWarnings("static-access")
//	private StreamedContent generarExcel(String nombreReporte, Map<String, Object> parameters, 
//			List<?> lista){
//		ExportarArchivo printer = new ExportarArchivo();
//		StringBuilder jasperFile = new StringBuilder();
//		StringBuilder reporte = new StringBuilder();
//		jasperFile.append("C://SGI//reportes//Gerencial//"+nombreReporte+".jrxml");
//		try{
//			byte[] array = printer.exportXls(jasperFile.toString(), parameters, lista, false);
//			InputStream stream = new ByteArrayInputStream(array);
//			reporte.append(nombreReporte);
//			reporte.append(".");
//			reporte.append(Constantes.EXTENSION_FORMATO_XLS);
//			return new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, reporte.toString());
//		}catch(Exception e){
//			return null;
//		}
//	}
//	
//	public Boolean obtenerTipoCambioDia() {
//		if (tipcambio==null) {
//			addWarnMessage("Tipo de cambio del día no ingresado", "No se ha ingresado el tipo de cambio del día, por favor ingresar para continuar ");
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
//	
//public void seleccionarTipoCambioxFecha()  {
//	tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(fechaconsultacarteraactiva));
//	
//}
//	
//	
//	public IReporteGerencialService getReportegerencialService() {
//		return reportegerencialService;
//	}
//	public void setReportegerencialService(
//			IReporteGerencialService reportegerencialService) {
//		this.reportegerencialService = reportegerencialService;
//	}
//	public JasperPrint getJasperPrint() {
//		return jasperPrint;
//	}
//	public void setJasperPrint(JasperPrint jasperPrint) {
//		this.jasperPrint = jasperPrint;
//	}
//	public ITipoCambioService getTipocambiService() {
//		return tipocambiService;
//	}
//	public void setTipocambiService(ITipoCambioService tipocambiService) {
//		this.tipocambiService = tipocambiService;
//	}
//	public UsuarioManagedBean getUserMB() {
//		return userMB;
//	}
//	public void setUserMB(UsuarioManagedBean userMB) {
//		this.userMB = userMB;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//	public StreamedContent getReport() {
//		
//		return report;
//	}
//	public void setReport(StreamedContent report) {
//		this.report = report;
//	}
//
//
//	public ICarteraService getCarteraService() {
//		return carteraService;
//	}
//
//
//	public void setCarteraService(ICarteraService carteraService) {
//		this.carteraService = carteraService;
//	}
//
//
//	public IContratoService getContratoService() {
//		return contratoService;
//	}
//
//
//	public void setContratoService(IContratoService contratoService) {
//		this.contratoService = contratoService;
//	}
//
//	public ITipoCambioService getTipocambioService() {
//		return tipocambioService;
//	}
//
//	public void setTipocambioService(ITipoCambioService tipocambioService) {
//		this.tipocambioService = tipocambioService;
//	}
//
//	public Double getTipcambio() {
//		return tipcambio;
//	}
//
//	public void setTipcambio(Double tipcambio) {
//		this.tipcambio = tipcambio;
//	}
//
//	public List<ComprobanteBean> getLista() {
//		return lista;
//	}
//
//	public void setLista(List<ComprobanteBean> lista) {
//		this.lista = lista;
//	}
//
//	public ArrayList<CuotaBean> getListaPendientesContratoPreContratoCuotaBean() {
//		return listaPendientesContratoPreContratoCuotaBean;
//	}
//
//	public void setListaPendientesContratoPreContratoCuotaBean(
//			ArrayList<CuotaBean> listaPendientesContratoPreContratoCuotaBean) {
//		this.listaPendientesContratoPreContratoCuotaBean = listaPendientesContratoPreContratoCuotaBean;
//	}
//
//	public ArrayList<CuotaBean> getListaPendientesSinContratoCuotaBean() {
//		return listaPendientesSinContratoCuotaBean;
//	}
//
//	public void setListaPendientesSinContratoCuotaBean(
//			ArrayList<CuotaBean> listaPendientesSinContratoCuotaBean) {
//		this.listaPendientesSinContratoCuotaBean = listaPendientesSinContratoCuotaBean;
//	}
//
//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//
//	public Date getFechaconsultacarteraactiva() {
//		return fechaconsultacarteraactiva;
//	}
//
//	public void setFechaconsultacarteraactiva(Date fechaconsultacarteraactiva) {
//		this.fechaconsultacarteraactiva = fechaconsultacarteraactiva;
//	}
//
//
//
//	public IUpaService getUpaService() {
//		return upaService;
//	}
//
//
//
//	public void setUpaService(IUpaService upaService) {
//		this.upaService = upaService;
//	}
//
//}
