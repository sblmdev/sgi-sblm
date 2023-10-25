package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

import net.sf.jasperreports.engine.JRException;
import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetalleCuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Condicionincumplimientoclausula;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detalleclausula;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.InteresMora;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInteresMoraService;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.service.ITipoCambioService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.service.IUsuarioService;
import pe.gob.sblm.sgi.serviceImpl.TipoCambioService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;
import pe.gob.sblm.sgi.util.PropiedadesBaseDatos;

/**
* Resumen.
* Objeto : CobranzaConsultaManagedBean
* Descripcion : Clase controladora de consultas y reportes  de cobranza.
* Fecha de Creacion : 02/07/2014.
* Autor : Franco Mallqui
* ------------------------------------------------------------------------
* Modificaciones
* Fecha            Nombre                     Descripción
* ------------------------------------------------------------------------
* 03/11/2014	Franco Mallqui  	- Consultas diferentes patrones
* 										de búsqueda.
* 03/01/2015	Franco Mallqui  	- Reportes para cartera activa y pesada
* 
* 17/01/2020    Marco Alarcon 		- Reporte especial para nota de credito pago otros.
*/
@ManagedBean(name = "cobranzaConsultaMB")
@ViewScoped
public class CobranzaConsultaManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger Logger=LoggerFactory.getLogger(CobranzaConsultaManagedBean.class);
	
	private List<Cartera> listacartera= new ArrayList<Cartera>();
	private Date desdeCobrador,hastaCobrador,desdeCartera,hastaCartera,desdeUpa,hastaUpa,desdeInquilino,hastaInquilino,hastaDetraccion,desdeDetraccion,desdeAutoDetraccion,hastaAutoDetraccion,desdeDocumento,hastaDocumento,desdeConsulta_Compr,hastaConsulta_Compr;
	private String nrocartera="";
	private String claveupa="",claveUpaLiq="";
	private String claveupaDocAdm="";
	private String claveupaDeuda;

	private ItemBean selectedCobrador;
	private List<ItemBean> listaCobradores= new ArrayList<ItemBean>();
	private List<Usuario> listaUsuarios= new ArrayList<Usuario>();
	private int idSelectedCobrador;
	private List<String> selectedComprobanteTabCobrador= new ArrayList<String>();
	private Boolean selectedSiFacturacionElectronicaTabCobrador=Boolean.FALSE;
	private Boolean selectedSiDocumentosAnulados=Boolean.FALSE;
	private Boolean selectedSiUsuario=Boolean.FALSE;
	private Boolean selectedSiCobrador=Boolean.TRUE;
	
	private boolean selectedSiAnuladoTabCobrador=false, selectedSiRechazadoTabCobrador=false;
	private Boolean selectedSiIncluirAnulados=Boolean.FALSE,selectedSiIncluirRechazados=Boolean.FALSE ;
	

	private String nombreinquilino;
	private String nrodocumento;
	private String nombreusuario="",nombreusuariocompleto="";
	private List<ComprobanteBean> listacomprobantespago= new ArrayList<ComprobanteBean>();
	private List<ComprobanteBean> listacomprobantespagoespecial= new ArrayList<ComprobanteBean>();
	private List<ComprobanteBean> filtrolistacomprobantespago;
	private ComprobanteBean comprobantepago , comprobantepagoExt;
	private RentaBean rentaBean;
	private StreamedContent report;
	private Boolean sidetallecomprobantes=true;
	private ArrayList<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
	private ArrayList<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
	private String tipDocu;
	private String tipPago;
	private String tipoMovi;

	private int periodo;
	private String condicion;

	private Double total,totaligv,totaldetraccion;

	private String tipodeconsulta;

	private String carteraActivaReporteCartera,carteraPesadaReporteCartera;

	private Double tipcambio;
	private Tipocambio tipoCambio;

	//Reporte liquidacion Upa
	private List<CondicionBean> listaCondicionDeUpa = new ArrayList<CondicionBean>();
	private CondicionBean condicionSeleccionado,condicionSeleccionadoDeuda;
	private String claveupareporte;
	private String tipoReporteSeleccionadoTabUpa="3";
	private String tipoReporteSeleccionadoTabDeuda="1";
	private String tipoReporteSeleccionadoTabDeudaMoneda="1";
	private String tipoReporteSeleccionadoTabCarteraActiva="2",tipoReporteSeleccionadoTabCarteraPasiva="1";
	private String estadoupaseleccionado="0";

	private List<pe.gob.sblm.sgi.entity.Detallecuota> listaDetalleComprobante;
	private List<CondicionBean> listaCobroCartera= new ArrayList<CondicionBean>();
	private List<CondicionBean> listaCondicionContratoBean;

	private boolean renderedBotonDbf=false;
	private boolean renderedBotonDbfDoc=false;
	private boolean renderedReporteDetraccion=false;
	private boolean renderedReporteAutoDetraccion=false;
	
	private Date fechaReporteGerencialCA= new Date();
	private Date fechaReporteGerencial;
	private String idUsuarioSeleccionado;
	private Usuario usuarioSeleccionado=new Usuario();
	private Boolean renderedReporteDocumento= false;
	private String nombreusuariologueado;
	private String descripcionbaja;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private List<ArchivoAdjuntoBean> listaArchivosReferencia= new ArrayList<ArchivoAdjuntoBean>();
	private List<String> listaCondicionContratoxUpa= new ArrayList<String>();
	private List<Condicionincumplimientoclausula> listaCondicionClausula ,listaCondicionClausulaPenalidad;
	private Condicionincumplimientoclausula condicionClausula ,condicionClausulaSeleccionada;
	private InteresMora interesMora ;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
    
	@ManagedProperty(value = "#{carteraMB}")
	CobranzaManagedBean cobranzaMB;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;

	
	
	@ManagedProperty(value = "#{carteraService}")
	private transient IRecaudacionCarteraService carteraService;
	
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService reporteRecaudacionService;

	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;

	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private transient IUsuarioService usuarioService;
	
	
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;
	
	@ManagedProperty(value = "#{administrarArchivoService}")
	private transient AdministrarArchivoService administrarArchivoService;
	
	@ManagedProperty(value = "#{archivoMB}")
	ArchivoManagedBean archivoMB;
	
	@ManagedProperty(value="#{interesMoraService}")
	private transient IInteresMoraService interesMoraService;

	private Date fechaconsultacarteraactiva=new Date();
	public String observacionElemSelecionadoc="";
	public boolean reporte_especial=false;
	public String nro_reporte="";

	
	
	/**
	 * Inicializa los objetos y variables a utilizar en el controlador.
	 */
	@PostConstruct
	public void init(){
		inicializandoValoresSeleccionadosTabCobradorConsulta();
		listaCobradores=carteraService.listarCobradores();
		listaUsuarios=carteraService.listar_Usuarios();
		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(new Date()));
		nombreusuario=userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat();

		 if (userMB.nombreperfilSeleccionado.equals("Cobrador")) {
				if (carteraService.siAsignadoCobrador(userMB.getUsuariologueado().getIdusuario())) {
					selectedCobrador=carteraService.obtenerNombreCobrador(userMB.getUsuariologueado().getIdusuario());
				}
			}
	}
	
	/*Inicializar tipo de documentos seleccionados para el Tab Cobrador de Consulta de comprobante*/
	
	public void inicializandoValoresSeleccionadosTabCobradorConsulta(){
		selectedComprobanteTabCobrador.add("01");
		selectedComprobanteTabCobrador.add("03");
		selectedComprobanteTabCobrador.add("04");
		selectedComprobanteTabCobrador.add("08");
		selectedComprobanteTabCobrador.add("09");
		
	}

	/* *************************************************TAB CONSULTA*******************************************************************************/
	/* * Consulta,actualizacion y eliminacion* */
	public void obtenerListaContratosDeUpa() {
		System.out.println("clave ="+claveupa);
		if (carteraService.siExisteUpa(claveupa)) {
			listaCondicionDeUpa = carteraService.obtenerListaContratosDeUpa(claveupa);
			addInfoMessage("Upa Encontrada", "Se muestra información de contratos referentes a la UPA");
		} else {
			addWarnMessage("No existe upa", "La upa ingresada no esta registrada en el sistema");
		}
	}
	public void obtenerListaContratosxUpa() {
		System.out.println("clave ="+claveUpaLiq);
		if (carteraService.siExisteUpa(claveUpaLiq)) {
			listaCondicionDeUpa = carteraService.obtenerListaContratosDeUpa(claveUpaLiq);
			addInfoMessage("Upa Encontrada", "Se muestra información de contratos referentes a la UPA");
		} else {
			addWarnMessage("No existe upa", "La upa ingresada no esta registrada en el sistema");
		}
	}
	public void obtenerListaCondicionContratoxUpa(){
		if (carteraService.siExisteUpa(claveupaDeuda)) {
			listaCondicionContratoxUpa= carteraService.obtenerCondicionDeContratoxUpa(claveupaDeuda);
			addInfoMessage ("Upa Existente", "Se muestra la relacion de contrato para la upa ingresada");
		} else {
			addWarnMessage("No existe contratos", "La upa ingresada no contiene contratos");
		}
	}
	public void obtenerListaContratosDeuda() {
//		if (carteraService.siExisteUpa(claveupa)) {
			listaCondicionDeUpa = carteraService.obtenerListaContratosDeUpa(claveupa);
//		} else {
//			addWarnMessage("No existe upa", "La upa ingresada no esta registrada en el sistema");
//		}
	}
	public void elegirReporteTabUpaDeuda(String tipoArchivo,String opcion)throws JRException, IOException  {
		String tipomoneda="";
		RequestContext context = RequestContext.getCurrentInstance();
		try {
			if(tipoReporteSeleccionadoTabDeudaMoneda.equals("1")){
				tipomoneda=Constante.TIPO_MONEDA_SOLES;
			}else{
				tipomoneda=Constante.TIPO_MONEDA_DOLARES;
			}

			if (tipoReporteSeleccionadoTabDeuda.equals("1")) {
				generarReportePagosDeuda(Constante.CONDICION_CONTRATO_VIGENTE,tipoArchivo,opcion,tipomoneda);
			}else if (tipoReporteSeleccionadoTabDeuda.equals("2")) {
				generarReportePagosDeuda(Constante.CONDICION_CONTRATO_FINALIZADO,tipoArchivo,opcion,tipomoneda);
			}else if (tipoReporteSeleccionadoTabDeuda.equals("3")) {
				generarReportePagosDeuda(Constante.CONDICION_CONTRATO_VIGENTE_FINALIZADO,tipoArchivo,opcion,tipomoneda);
			}
		int dataSize = 1024*1024;

		Runtime runtime=Runtime.getRuntime();
        
		System.out.println("Memoria maxima:"+ runtime.maxMemory() /dataSize +"MB");
		System.out.println("Memoria total:"+ runtime.totalMemory() /dataSize +"MB");
		System.out.println("Memoria libre:"+ runtime.freeMemory() /dataSize +"MB");
		System.out.println("Memoria usada:"+ (runtime.totalMemory() - runtime.freeMemory())/dataSize +"MB");
		FacesMessage msg = new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Mensaje",
				"Ejecucion de Deuda SGI con exito");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		}catch(Exception e ){
			e.printStackTrace();
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error",
					"En exportar Archivo!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	public void generarReportePagosDeuda(String tipoReporte,String tipoArchivo,String opcion,String tipomoneda) throws JRException, IOException{
						
		   String  reporte= "";
		   int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
		   Double totaldeuda = 0.0,totalcancelado = 0.0,totalmora=0.0,totalmc=0.0,totaligv=0.0,totalnuevarenta=0.0;
		   List<CondicionBean> listaCondicionBean= new ArrayList<CondicionBean>();	   
		   List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		   //List<CondicionBean> listaContratoDetracion=new ArrayList<CondicionBean>();
		   listaContratoBean = contratoService.obtenerCondicionDeuda("Global",tipoReporte,"todos",tipomoneda);
//		   listaContratoDetracion = contratoService.obtenerCondicionContratoDetraccion("Global",tipoReporte,"todos");
//			for(int i=0;i<listaContratoBean.size();i++){
//				for(int j=0;j<listaContratoDetracion.size();j++){
//  					if(listaContratoBean.get(i).getIdcontrato() == listaContratoDetracion.get(j).getIdcontrato()){
// 						listaContratoBean.get(i).setSidetraccion(listaContratoDetracion.get(j).getSidetraccion());
//					}
//				}
//			}
			
		   List<CuotaBean> lista= new ArrayList<CuotaBean>();
		   //List<RentaBean> listaRenta=new ArrayList<RentaBean>();
		   Boolean estadolista=false;	   
		   Map<String, Object> parameters = new HashMap<String, Object>();
		     for (CondicionBean condicionSeleccionado:listaContratoBean){
			   if (condicionSeleccionado.getSinuevomantenimiento() ) {
				   //condicionSeleccionado.setRentas(null);//inicializa el valor 
					//if (tipoReporte.equals("pendientes")) {
//						   listaRenta=reporteRecaudacionService.obtenerRentasNuevoMantenimientoContrato(condicionSeleccionado.getIdcontrato());
						 /// listaRenta=reporteRecaudacionService.obtenerPeriodosPendientes(condicionSeleccionado.getIdcontrato());
						  lista=reporteRecaudacionService.obtenerPeriodosPendientesDeuda(condicionSeleccionado.getIdcontrato());
                          //System.out.println("lista ="+lista.size());
				//	}
				   
				   parameters.put("TITULO", tipoReporte.toUpperCase());
//					if(estadolista){
//						condicionSeleccionado.setRentas(listaRenta);
//					}
				  // condicionSeleccionado.setRentas(listaRenta);
				   
				   reporte="RECAUDACION_REP_LIQUIDACION_PENDIENTES_CANCELADAS_NUEVO.jrxml";
				   condicionSeleccionado.setCuotas(lista);
			   }else {
					lista=reporteRecaudacionService.obtenerInformacionCobroCondicionTipoxDeuda(condicionSeleccionado.getIdcontrato(), condicionSeleccionado.getCondicion(), "pendientes", tipcambio);	
				
					/*GENERA LA LISTA DE PENDIENTES, CANCELADAS, ratio(LIQUIDACIONES CON MORA)*/
			
	 				condicionSeleccionado.setCuotas(lista);/**Seteamos las cuotas a la condicion**/
	 				
//					reporte= tipoReporte.equals("ratio")?ConstantesReporteSGI.REP_LIQUIDACION_RATIO_MORA:(tipoReporte.equals("canceladas")?ConstantesReporteSGI.REP_LIQUIDACION_CANCELADAS:(tipoReporte.equals("pendientes")?ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES:ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES_CANCELADAS));
	 				 reporte="RECAUDACION_REP_LIQUIDACION_PENDIENTES_CANCELADAS_NUEVO.jrxml";	
				}
			   if(lista!=null){
              	  if(lista.size()>0){
              		  estadolista=true;
              	  }
                }
			   if(estadolista){
				   condicionSeleccionado.setCuotas(lista);
				   listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/

			   }
							 			  
			  //listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/
				}
			  /**Recorremos la lista de cuota para sumar determinar los totales**/
//			  if(listaRenta!=null){
//				  
//			  if(listaRenta.size()>0){
//				  			
//			  for (RentaBean rentabean : listaRenta) {
//					 totaldeuda=totaldeuda+	rentabean.getRenta();
//					 //System.out.println("totaldeuda: "+totaldeuda);
//					 totalcancelado=totalcancelado+rentabean.getMontopagar();
//					 //System.out.println("totalcancelado: "+totalcancelado);
//					//-- totalmora=totalmora+rentabean.getMora();
//					//--- totalmc=totalmc+rentabean.getNuevamc();
//					 //System.out.println("totalmc: "+totalmc);
//					//--- totaligv=totaligv+rentabean.getIgv();
//					 //System.out.println("totaligv: "+totaligv);
//					 totalnuevarenta=totalnuevarenta+rentabean.getRenta();
//					 //System.out.println("totalnuevarenta: "+totalnuevarenta);
//
////					 if (!cuotabean.getSipagado()) {
////						 nropendiente++;
////					 }
//		
////					 if (rentabean.getSipagado().equals("0")) {     //Cuota pendiente
////						 nropendiente++;
////					 }
//////					 if (cuotabean.getSipagado()) {
//////						 nrocancelado++;
//////					 }
////					 if (rentabean.getSipagado().equals("1")) {     //cuota cancelada
////						 nrocancelado++;
////					 }
////					 if (rentabean.getSipagado().equals("2")){     // cuota generada
////							nropendiente++;
////						 }
//					 if(rentabean.getSicancelado().equals("0")){
//						 nropendiente++;
//					 }else{
//						 if(rentabean.getSicancelado().equals("1")){
//							 nrocancelado++;
//						 }else{
//							 nropendiente++;
//						 }
//					 }
////					 if (rentabean.getSiacuenta()){
////						nroacuenta++;
////					 }					 
//				
//			  }
//			  }
//			  }
//			  for (CuotaBean cuotabean : lista) {
//					 totaldeuda=totaldeuda+	cuotabean.getMonto();
//					 //System.out.println("totaldeuda: "+totaldeuda);
//					 totalcancelado=totalcancelado+cuotabean.getRentapagada();
//					 //System.out.println("totalcancelado: "+totalcancelado);
//					 totalmora=totalmora+cuotabean.getMora();
//					 totalmc=totalmc+cuotabean.getNuevamc();
//					 //System.out.println("totalmc: "+totalmc);
//					 totaligv=totaligv+cuotabean.getIgv();
//					 //System.out.println("totaligv: "+totaligv);
//					 totalnuevarenta=totalnuevarenta+cuotabean.getNuevarenta();
//					 //System.out.println("totalnuevarenta: "+totalnuevarenta);
//
////					 if (!cuotabean.getSipagado()) {
////						 nropendiente++;
////					 }
//					 if (cuotabean.getSipagado().equals("0")) {     //Cuota pendiente
//						 nropendiente++;
//					 }
////					 if (cuotabean.getSipagado()) {
////						 nrocancelado++;
////					 }
//					 if (cuotabean.getSipagado().equals("1")) {     //cuota cancelada
//						 nrocancelado++;
//					 }
//					 if (cuotabean.getSipagado().equals("2")){     // cuota generada
//							nropendiente++;
//						 }
//					 if (cuotabean.getSiacuenta()){
//						nroacuenta++;
//					 }					 
//				
//			  }
			  String moneda="";
			  if(tipomoneda.equals(Constante.TIPO_MONEDA_SOLES)){
				  moneda=Constante.TIPO_MONEDA_S;
			  }else {
				  moneda=Constante.TIPO_MONEDA_D;
			  }
			  
			  if (tipoArchivo.equals("pdf")) {
				
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   parameters.put("TIPOCAMBIO",tipcambio); //PARA MODIFICACION TIPO CAMBIO
				   parameters.put("NROCANCELADO",nrocancelado );
				   parameters.put("NROPENDIENTE",nropendiente);
				   parameters.put("NROACUENTA",nroacuenta);
				   parameters.put("TOTALCANCELADO",totalcancelado);
				   parameters.put("TOTALDEUDA",totaldeuda);
				   parameters.put("TOTALMORA", totalmora); //franco
				   parameters.put("TOTALMC", totalmc);   
				   parameters.put("TOTALIGV", totaligv);
				   parameters.put("TOTALNUEVARENTA", totalnuevarenta);
				   parameters.put("SIREGISTRADEUDA", lista.size()==0?"FALSE":"TRUE");
				   
				   

				   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
					   
			  } else {
				  		System.out.println("excel");
				  		if(opcion!=null){
				  		if(opcion.equals("1")){
				  			generarReporteUpaDeudaTipoExcel(listaCondicionBean,tipoReporte,moneda);
				  		}
				  		if(opcion.equals("2")){
				  			generarReporteUpaDeudaTipoExcel2(listaCondicionBean,tipoReporte,moneda);
				  		}
				  		if(opcion.equals("3")){
				  			generarReporteUpaDeudaTipoExcel3(listaCondicionBean,tipoReporte,moneda);
				  		}
				  		}
				  		else{
				  			
				  		}
				  		
				  		
			  }
						
			  
		}
	public void generarPagosDeudaxUpa(String tipoReporte,String tipoArchivo,String opcion,String tipomoneda, String claveupa) throws JRException, IOException {
		
		   String  reporte= "";
		   int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
		   Double totaldeuda = 0.0,totalcancelado = 0.0,totalmora=0.0,totalmc=0.0,totaligv=0.0,totalnuevarenta=0.0;
		   List<CondicionBean> listaCondicionBean= new ArrayList<CondicionBean>();	   
		   List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		   listaContratoBean = contratoService.obtenerCondicionDeuda("Global",tipoReporte,"todos",tipomoneda);			
		   List<CuotaBean> lista= new ArrayList<CuotaBean>();
		   //List<RentaBean> listaRenta=new ArrayList<RentaBean>();
		   Boolean estadolista=false;	   
		   Map<String, Object> parameters = new HashMap<String, Object>();
		     for (CondicionBean condicionSeleccionado:listaContratoBean){
			   if (condicionSeleccionado.getSinuevomantenimiento() ) {
				   //condicionSeleccionado.setRentas(null);//inicializa el valor 
					//if (tipoReporte.equals("pendientes")) {
//						   listaRenta=reporteRecaudacionService.obtenerRentasNuevoMantenimientoContrato(condicionSeleccionado.getIdcontrato());
						 /// listaRenta=reporteRecaudacionService.obtenerPeriodosPendientes(condicionSeleccionado.getIdcontrato());
						  lista=reporteRecaudacionService.obtenerPeriodosPendientesDeuda(condicionSeleccionado.getIdcontrato());
                       //System.out.println("lista ="+lista.size());
				//	}
				   
				   parameters.put("TITULO", tipoReporte.toUpperCase());
//					if(estadolista){
//						condicionSeleccionado.setRentas(listaRenta);
//					}
				  // condicionSeleccionado.setRentas(listaRenta);
				   
				   reporte="RECAUDACION_REP_LIQUIDACION_PENDIENTES_CANCELADAS_NUEVO.jrxml";
				   condicionSeleccionado.setCuotas(lista);
			   }else {
					lista=reporteRecaudacionService.obtenerInformacionCobroCondicionTipoxDeuda(condicionSeleccionado.getIdcontrato(), condicionSeleccionado.getCondicion(), "pendientes", tipcambio);	
				
					/*GENERA LA LISTA DE PENDIENTES, CANCELADAS, ratio(LIQUIDACIONES CON MORA)*/
			
	 				condicionSeleccionado.setCuotas(lista);/**Seteamos las cuotas a la condicion**/
	 				
//					reporte= tipoReporte.equals("ratio")?ConstantesReporteSGI.REP_LIQUIDACION_RATIO_MORA:(tipoReporte.equals("canceladas")?ConstantesReporteSGI.REP_LIQUIDACION_CANCELADAS:(tipoReporte.equals("pendientes")?ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES:ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES_CANCELADAS));
	 				 reporte="RECAUDACION_REP_LIQUIDACION_PENDIENTES_CANCELADAS_NUEVO.jrxml";	
				}
			   if(lista!=null){
           	  if(lista.size()>0){
           		  estadolista=true;
           	  }
             }
			   if(estadolista){
				   condicionSeleccionado.setCuotas(lista);
				   listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/

			   }
							 			  
			  //listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/
				}
			  /**Recorremos la lista de cuota para sumar determinar los totales**/
//			  if(listaRenta!=null){
//				  
//			  if(listaRenta.size()>0){
//				  			
//			  for (RentaBean rentabean : listaRenta) {
//					 totaldeuda=totaldeuda+	rentabean.getRenta();
//					 //System.out.println("totaldeuda: "+totaldeuda);
//					 totalcancelado=totalcancelado+rentabean.getMontopagar();
//					 //System.out.println("totalcancelado: "+totalcancelado);
//					//-- totalmora=totalmora+rentabean.getMora();
//					//--- totalmc=totalmc+rentabean.getNuevamc();
//					 //System.out.println("totalmc: "+totalmc);
//					//--- totaligv=totaligv+rentabean.getIgv();
//					 //System.out.println("totaligv: "+totaligv);
//					 totalnuevarenta=totalnuevarenta+rentabean.getRenta();
//					 //System.out.println("totalnuevarenta: "+totalnuevarenta);
//
////					 if (!cuotabean.getSipagado()) {
////						 nropendiente++;
////					 }
//		
////					 if (rentabean.getSipagado().equals("0")) {     //Cuota pendiente
////						 nropendiente++;
////					 }
//////					 if (cuotabean.getSipagado()) {
//////						 nrocancelado++;
//////					 }
////					 if (rentabean.getSipagado().equals("1")) {     //cuota cancelada
////						 nrocancelado++;
////					 }
////					 if (rentabean.getSipagado().equals("2")){     // cuota generada
////							nropendiente++;
////						 }
//					 if(rentabean.getSicancelado().equals("0")){
//						 nropendiente++;
//					 }else{
//						 if(rentabean.getSicancelado().equals("1")){
//							 nrocancelado++;
//						 }else{
//							 nropendiente++;
//						 }
//					 }
////					 if (rentabean.getSiacuenta()){
////						nroacuenta++;
////					 }					 
//				
//			  }
//			  }
//			  }
//			  for (CuotaBean cuotabean : lista) {
//					 totaldeuda=totaldeuda+	cuotabean.getMonto();
//					 //System.out.println("totaldeuda: "+totaldeuda);
//					 totalcancelado=totalcancelado+cuotabean.getRentapagada();
//					 //System.out.println("totalcancelado: "+totalcancelado);
//					 totalmora=totalmora+cuotabean.getMora();
//					 totalmc=totalmc+cuotabean.getNuevamc();
//					 //System.out.println("totalmc: "+totalmc);
//					 totaligv=totaligv+cuotabean.getIgv();
//					 //System.out.println("totaligv: "+totaligv);
//					 totalnuevarenta=totalnuevarenta+cuotabean.getNuevarenta();
//					 //System.out.println("totalnuevarenta: "+totalnuevarenta);
//
////					 if (!cuotabean.getSipagado()) {
////						 nropendiente++;
////					 }
//					 if (cuotabean.getSipagado().equals("0")) {     //Cuota pendiente
//						 nropendiente++;
//					 }
////					 if (cuotabean.getSipagado()) {
////						 nrocancelado++;
////					 }
//					 if (cuotabean.getSipagado().equals("1")) {     //cuota cancelada
//						 nrocancelado++;
//					 }
//					 if (cuotabean.getSipagado().equals("2")){     // cuota generada
//							nropendiente++;
//						 }
//					 if (cuotabean.getSiacuenta()){
//						nroacuenta++;
//					 }					 
//				
//			  }
			  String moneda="";
			  if(tipomoneda.equals(Constante.TIPO_MONEDA_SOLES)){
				  moneda=Constante.TIPO_MONEDA_S;
			  }else {
				  moneda=Constante.TIPO_MONEDA_D;
			  }
			  
			  if (tipoArchivo.equals("pdf")) {
				
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   parameters.put("TIPOCAMBIO",tipcambio); //PARA MODIFICACION TIPO CAMBIO
				   parameters.put("NROCANCELADO",nrocancelado );
				   parameters.put("NROPENDIENTE",nropendiente);
				   parameters.put("NROACUENTA",nroacuenta);
				   parameters.put("TOTALCANCELADO",totalcancelado);
				   parameters.put("TOTALDEUDA",totaldeuda);
				   parameters.put("TOTALMORA", totalmora); //franco
				   parameters.put("TOTALMC", totalmc);   
				   parameters.put("TOTALIGV", totaligv);
				   parameters.put("TOTALNUEVARENTA", totalnuevarenta);
				   parameters.put("SIREGISTRADEUDA", lista.size()==0?"FALSE":"TRUE");
				   
				   

				   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
					   
			  } else {
				  		System.out.println("excel");
				  		if(opcion!=null){
				  		if(opcion.equals("1")){
				  			generarReporteUpaDeudaTipoExcel(listaCondicionBean,tipoReporte,moneda);
				  		}
				  		if(opcion.equals("2")){
				  			generarReporteUpaDeudaTipoExcel2(listaCondicionBean,tipoReporte,moneda);
				  		}}
				  		else{
				  			
				  		}
				  		
				  		
			  }
						
			  
		}
public void validar_acceso_opciones(){
	nombreusuariologueado="";
    nombreusuariologueado=userMB.getNombreusr();
    if(nombreusuariologueado!=null){
    	//opcion fechacancelacion
    	if(Constante.OPCION_RECAUDACION_CONSULTAS_01.equals(Constante.USUARIO_OPCION_RECAUDACION_CONSULTAS_SGI.get(nombreusuariologueado))){
    		//minfechacancelacion=ponerDiasFechaInicioMes(minfechacancelacion);
    		cobranzaMB.setAcceso_opcion(Constante.OPCION_RECAUDACION_CONSULTAS_01);
    		cobranzaMB.setUser_acceso_fechacanc(true);
     		cobranzaMB.setConsulta_minfechacancelacion(new Date());
    		cobranzaMB.setConsulta_minfechacancelacion(cobranzaMB.ponerDiasFechaInicioMesComprobantes(cobranzaMB.getConsulta_minfechacancelacion(),-5));
    	}else if(Constante.OPCION_RECAUDACION_CONSULTAS_02.equals(Constante.USUARIO_OPCION_RECAUDACION_CONSULTAS_SGI.get(nombreusuariologueado))){
    		cobranzaMB.setAcceso_opcion(Constante.OPCION_RECAUDACION_CONSULTAS_02);
    		cobranzaMB.setUser_acceso_fechacanc(false);
    		cobranzaMB.setConsulta_minfechacancelacion(new Date());
    		cobranzaMB.setConsulta_minfechacancelacion(cobranzaMB.ponerDiasFechaInicioMesComprobantes(cobranzaMB.getConsulta_minfechacancelacion(),-5));
    
    	} else if(Constante.OPCION_RECAUDACION_CONSULTAS_03.equals(Constante.USUARIO_OPCION_RECAUDACION_CONSULTAS_SGI.get(nombreusuariologueado))){
    		cobranzaMB.setUser_acceso_fechacanc(false);
    		cobranzaMB.setAcceso_opcion(Constante.OPCION_RECAUDACION_CONSULTAS_03);
    		cobranzaMB.setConsulta_minfechacancelacion(new Date());
    	}else{
    		cobranzaMB.setUser_acceso_fechacanc(false);
    		cobranzaMB.setAcceso_opcion(Constante.OPCION_RECAUDACION_CONSULTAS_04);
    		cobranzaMB.setConsulta_minfechacancelacion(new Date());
    		//cobranzaMB.setConsulta_minfechacancelacion(cobranzaMB.ponerDiasFechaInicioMesComprobantes(cobranzaMB.getConsulta_minfechacancelacion(),0));
    	}
    	
    }
    System.out.println("nombreusuariologueado = "+nombreusuariologueado);
}
public boolean siExistenciaComprobante(int idcomprobante){
	boolean resp=false;
	Comprobantepago cp = new Comprobantepago();
	cp=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);
	if(cp!=null){
		resp=true;
	}else{
		resp=false;
	}
	return resp;
}
public void validarActualizacionEstadoComprobante(){
	List<Integer> listaIdSunatComp= new ArrayList<Integer>();
	List<Integer> listaIdComp= new ArrayList<Integer>();
	listaIdSunatComp=cobranzaRecaudacionService.obtenerListaIdSunatComprobanteRechazadosxPeriodo();
	listaIdComp=cobranzaRecaudacionService.obtenerListaIdComprobanteRechazadosxPeriodo();
	
	listaIdSunatComp.removeAll(listaIdComp);
    System.out.println("N° de Elemento = "+listaIdSunatComp.size());
	if(listaIdSunatComp.size()>0){
		for(int idcomp:listaIdSunatComp ){
			if (siExistenciaComprobante(idcomp)) {			// buscar idcomprobante si existe
					Comprobantepago comp = new Comprobantepago();
					comp.setIdcomprobante(idcomp);
					cobranzaRecaudacionService
							.actualizarEstadoComprobante(comp);//
					actualizarCondicionComprobanteRechazado(idcomp);
				}
		}
		
	}
	System.out.println(" exito la actualizacion de estado de comprobante");
	Logger.info("se realizo con exito la actualizacion de estado de comprobante ");
	
}
public void buscarComprobantesCobrador() 
{       
	    listacomprobantespago.clear();	    
		tipodeconsulta="cobrador";
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (desdeCobrador==null || hastaCobrador==null) {
			addWarnMessage("", "Debe ingresar ambos campos de las fechas para realizar la búsqueda");
			
		} else if (selectedCobrador==null) {
			addWarnMessage("", "Debe ingresar de cobrador para realizar la búsqueda");
			
		}else {
			
			Calendar hasta = Calendar.getInstance();
			hasta.setTime(hastaCobrador);
	        hasta.add(Calendar.DAY_OF_YEAR, 1);

			Calendar desde = Calendar.getInstance();
			desde.setTime(desdeCobrador);
			if (selectedSiFacturacionElectronicaTabCobrador) {
				tipodeconsulta="cobradorFE";
				
			     listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
			     total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
				  //caso especial
			     listacomprobantespagoespecial=cobranzaRecaudacionService.buscarComprobanteEspecial(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
			} else {
				tipodeconsulta="cobrador";
				
		        listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
		        //total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
		        total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
			    //caso especial
		        listacomprobantespagoespecial=cobranzaRecaudacionService.buscarComprobanteEspecial(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
			}
			
						
		}
}
public void buscarComprobantepago() 
{
	   listacomprobantespago.clear();
	   if(selectedSiUsuario){
		   tipodeconsulta=Constante.TIPO_CONSULTA_USUARIO;
	   }else{ if(selectedSiCobrador){
		   tipodeconsulta=Constante.TIPO_CONSULTA_COBRADOR;
	      }
		   
	   }
		//tipodeconsulta="cobrador";

		if (desdeConsulta_Compr==null || hastaConsulta_Compr==null) {
			addWarnMessage("", "Debe ingresar ambos campos de las fechas para realizar la búsqueda");
			
		} 
//		else if (selectedCobrador==null) {
//			addWarnMessage("", "Debe ingresar de cobrador para realizar la búsqueda");
//			
//		}
		else {
			
			Calendar hasta = Calendar.getInstance();
			hasta.setTime(hastaConsulta_Compr);
	        hasta.add(Calendar.DAY_OF_YEAR, 1);

			Calendar desde = Calendar.getInstance();
			desde.setTime(desdeConsulta_Compr);
			/*if (selectedSiFacturacionElectronicaTabCobrador) {
				tipodeconsulta="cobradorFE";*/
				System.out.println("tipodeconsulta="+tipodeconsulta);
				System.out.println("idUsuarioSeleccionado="+idUsuarioSeleccionado);
				if(selectedSiUsuario){
				     listacomprobantespago=cobranzaRecaudacionService.buscarDocumento(null,claveupa,usuarioSeleccionado,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiDocumentosAnulados,selectedSiIncluirAnulados,selectedSiIncluirRechazados);

				}else{
				     listacomprobantespago=cobranzaRecaudacionService.buscarDocumento(selectedCobrador.getId(),claveupa,usuarioSeleccionado,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiDocumentosAnulados,selectedSiIncluirAnulados,selectedSiIncluirRechazados);

				}
			     //listacomprobantespago=cobranzaRecaudacionService.buscarDocumento(selectedCobrador.getId(),claveupa,idUsuarioSeleccionado,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiDocumentosAnulados,selectedSiIncluirAnulados);
			     //total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
				  //caso especial
			     //listacomprobantespagoespecial=cobranzaRecaudacionService.buscarComprobanteEspecial(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
		/*	} else {
				tipodeconsulta="cobrador";
				System.out.println("tipodeconsultaN="+tipodeconsulta);
		        listacomprobantespago=cobranzaRecaudacionService.buscarDocumento(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiDocumentosAnulados,selectedSiIncluirAnulados);
		        //total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()));
		       // total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
			    //caso especial
		        //listacomprobantespagoespecial=cobranzaRecaudacionService.buscarComprobanteEspecial(selectedCobrador.getId(),claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
			}*/
			
						
		}
}
    /*METODO DE BUSQUEDA DE LA LISTA DE DOCUMENTO ADMINISTRATIVO */// 
public void buscarDocumentoAdministrativo() 
{      //System.out.println("buscardocumento administrativo");
       observacionElemSelecionadoc="";
	   listacomprobantespago.clear();
	   tipodeconsulta="Doc.Administrativo";
	   validarActualizacionEstadoComprobante();//rechazados actualizacion
	   selectedComprobanteTabCobrador.clear();
	   selectedComprobanteTabCobrador.add("DA");
	   selectedSiAnuladoTabCobrador=true;
	  // selectedCobrador.setId(99999);
      if (claveupaDocAdm.equals(""))
		{  
		addWarnMessage("Ingresar clave de upa", "Debe ingresar al menos la clave de la upa para realizar la búsqueda");
		}

		else { if ((hastaDocumento!=null && desdeDocumento!=null && !claveupaDocAdm.equals(""))) //busqueda con fecha y upa
		{
			Calendar hasta = Calendar.getInstance();
			hasta.setTime(hastaDocumento);
			hasta.add(Calendar.DAY_OF_YEAR, 1);

			Calendar desde = Calendar.getInstance();
			desde.setTime(desdeDocumento);
			listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupaDocAdm,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
		}
		else if (hastaDocumento==null && desdeDocumento==null && !claveupaDocAdm.equals("")) //busqueda solo con upa sin fecha
			{
				
				listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupaDocAdm,"","","","","",tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
		        //total=cobranzaRecaudacionService.sumarComprobanteCobrador(selectedCobrador.getId(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);

			}
			
		
		}
}
	/*METODO QUE BUSCA LA LISTA DE COMPROBANTES - CONSULTAxUPA*/ //CINDY
	public void buscarComprobantesUpa()
	{
			listacomprobantespago.clear();
			tipodeconsulta="upa";
			validarActualizacionEstadoComprobante();//rechazados actualizacion
			validar_acceso_opciones();
	
				if (hastaUpa==null && desdeUpa==null && claveupa.equals("")) 
				{
					addWarnMessage("Ingresar clave de upa", "Debe ingresar al menos la clave de la upa para realizar la búsqueda");
	
				}else if ((hastaUpa==null && desdeUpa!=null && !claveupa.equals(""))|| (hastaUpa!=null && desdeUpa==null && !claveupa.equals("")))
				{
					addWarnMessage("Ingresar rango de fecha", "Debe completar en rango de fechas para realizar la búsqueda");
				}
				else if ((hastaUpa!=null && desdeUpa!=null && !claveupa.equals(""))) //busqueda con fecha y sin fecha
					{
						Calendar hasta = Calendar.getInstance();
						hasta.setTime(hastaUpa);
						hasta.add(Calendar.DAY_OF_YEAR, 1);
		
						Calendar desde = Calendar.getInstance();
						desde.setTime(desdeUpa);
								
						listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupa,nombreusuario,nombreinquilino,nrodocumento,FuncionesHelper.fechaHoraToString(desde.getTime()),FuncionesHelper.fechaHoraToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
		
					}
					else if (hastaUpa==null && desdeUpa==null && !claveupa.equals("")) //busqueda solo con upa
						{
							listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupa,"","","","","",tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
						}
				
		}

	public void buscarComprobantesInquilino(){
		listacomprobantespago.clear();
		tipodeconsulta="inquilino";
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (desdeInquilino==null || hastaInquilino==null) {
			addWarnMessage("Ingresar fecha 	rango de fechas", "Debe ingresar ambos campos de las fechas para realizar la busqueda");
		} else {
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(desdeInquilino);
	        calendar.add(Calendar.DAY_OF_YEAR, -1);
	        listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupa,"",nombreinquilino,"",FuncionesHelper.fechaHoraToString(calendar.getTime()),FuncionesHelper.fechaHoraToString(hastaInquilino),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
		}
	}

	public void buscarComprobantesNroDocumento(){
		listacomprobantespago.clear();
		tipodeconsulta="documento";
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (nrodocumento.equals("")) {
			addWarnMessage("Ingresar número de documento", "Debe ingresar el nï¿½mero de documento para poder realizar la bï¿½squeda");
		} else {
	        listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,claveupa,"","",nrodocumento,"","",tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
	        
	        total=FuncionesHelper.redondear(cobranzaRecaudacionService.sumarComprobanteNrodocumento(nrodocumento,"",""),2);
	        System.out.println("TOTAL="+total);
		}

	}

	public void buscarComprobantesDetraccion(){
		listacomprobantespago.clear();
		tipodeconsulta=Constante.TIPO_CONSULTA_DETRACCION;
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (desdeDetraccion==null || desdeDetraccion==null) {
			addWarnMessage("Ingresar fecha 	rango de fechas", "Debe ingresar ambos campos de las fechas para realizar la busqueda");
		} else {
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(desdeDetraccion);
	        calendar.add(Calendar.DAY_OF_YEAR, -1);
	        listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,"","","","",FuncionesHelper.fechaHoraToString(calendar.getTime()),FuncionesHelper.fechaHoraToString(hastaDetraccion),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
	        total=FuncionesHelper.redondear(cobranzaRecaudacionService.sumarComprobanteDetraccion(FuncionesHelper.fechaHoraToString(calendar.getTime()),FuncionesHelper.fechaHoraToString(hastaDetraccion)),2);
		}
	}
	public void buscarComprobantesAutoDetraccion(){
		listacomprobantespago.clear();
		tipodeconsulta=Constante.TIPO_CONSULTA_AUTODETRACCION;
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (desdeAutoDetraccion==null || desdeAutoDetraccion==null) {
			addWarnMessage("Ingresar fecha 	rango de fechas", "Debe ingresar ambos campos de las fechas para realizar la busqueda");
		} else {
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(desdeAutoDetraccion);
	        calendar.add(Calendar.DAY_OF_YEAR, -1);
	        listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,"","","","",FuncionesHelper.fechaHoraToString(calendar.getTime()),FuncionesHelper.fechaHoraToString(hastaAutoDetraccion),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
	        total=FuncionesHelper.redondear(cobranzaRecaudacionService.sumarComprobanteAutoDetraccion(FuncionesHelper.fechaHoraToString(calendar.getTime()),FuncionesHelper.fechaHoraToString(hastaAutoDetraccion)),2);
		}
	}

	public void buscarComprobantesUsuario(){
		tipodeconsulta="usuario";
		validarActualizacionEstadoComprobante();//rechazados actualizacion
		validar_acceso_opciones();
		if (desdeCartera==null || hastaCartera==null) {
				
			addWarnMessage("Ingresar fecha 	rango de fechas", "Debe ingresar ambos campos de las fechas para realizar la busqueda");
		} else {
			Calendar hasta = Calendar.getInstance();
			hasta.setTime(hastaCartera);
	        hasta.add(Calendar.DAY_OF_YEAR, 1);

			Calendar desde = Calendar.getInstance();
			desde.setTime(desdeCartera);

			listacomprobantespago=cobranzaRecaudacionService.buscarComprobante(99999,"",userMB.getNombrecompleto(),"","",FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime()),tipodeconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
			total=FuncionesHelper.redondear(cobranzaRecaudacionService.sumarComprobanteCartera(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat(),FuncionesHelper.fechaToString(desde.getTime()),FuncionesHelper.fechaToString(hasta.getTime())),2);
		}
	}

	public void obtenerDetalleComprobante(){
		System.out.println("m. obtenerdetallecomprobante()");
		if(comprobantepagoExt!=null){
		if (!comprobantepagoExt.getIdtipdocu().equals("04") || !comprobantepagoExt.getIdtipdocu().equals("08")  || !comprobantepagoExt.getIdtipdocu().equals("09") ) {
			listaDetalleComprobante=cobranzaRecaudacionService.obtenerDetalleComprobante(comprobantepagoExt.getIdcomprobante());
			Detallecuota detalle=new Detallecuota();
			if(listaDetalleComprobante.size()!=0){
				detalle=listaDetalleComprobante.get(0);
				detalle.setMontosoles(FuncionesHelper.redondearNum(detalle.getMontosoles(),2));
				listaDetalleComprobante.set(0,detalle);
			}else{
				System.out.println("listaDetalleComprobante is null");
			}
				
		}

		if (listaDetalleComprobante.size()!=0) {
			periodo=listaDetalleComprobante.get(0).getAnio();
			condicion=comprobantepagoExt.getCondicion();
		}
		comprobantepagoExt.setTotal(FuncionesHelper.redondearNum(comprobantepagoExt.getTotal(),2));
		tipDocu=cobranzaRecaudacionService.obtenerDescripcionComprobantePago(comprobantepagoExt.getIdtipdocu());
		tipoMovi=cobranzaRecaudacionService.obtenerDescripcionTipoMovimiento(comprobantepagoExt.getIdtipmovimiento());
		tipPago=cobranzaRecaudacionService.obtenerDescripcionTipoPago(comprobantepagoExt.getIdtippago());
		}else{
			System.out.println("comprobantepago is null");
		}
}

   public void obtenerDetalleComprobanteExtDeuda(){
	   obtenerDetalleComprobante();
	   observacionElemSelecionadoc=comprobantepagoExt.getObservacion();
   }
	public List<String> autoCompleteCartera(String query){

		List<String> result = new ArrayList<String>();
		listacartera=carteraService.listaCarteras();
		for(Cartera cartera : (List<Cartera>)listacartera){
			String numeroCartera=cartera.getNumero();
			if(numeroCartera.toLowerCase().contains(query.toLowerCase())){
				result.add(numeroCartera);
			}
		}

		return result;
	}

	public void limpiarBusqueda(TabChangeEvent event)
	{	
		
		TabView tabView = (TabView) event.getComponent();
		System.out.println("TABVIEW="+tabView.getActiveIndex());
		renderedBotonDbf=tabView.getActiveIndex()==3?true:false;
		renderedBotonDbfDoc=tabView.getActiveIndex()==6?true:false;
		renderedReporteDetraccion=tabView.getActiveIndex()==5?true:false;
		renderedReporteAutoDetraccion=tabView.getActiveIndex()==6?true:false;
		System.out.println("renderedBotonDbf="+renderedBotonDbf);
		System.out.println("renderedBotonDbfDOC="+renderedBotonDbfDoc);
		System.out.println("renderedReporteDetraccion="+renderedReporteDetraccion);
		System.out.println("renderedReporteAutoDetraccion="+renderedReporteAutoDetraccion);
		listacomprobantespago.clear();
		nombreinquilino="";
		claveupa="";
		desdeCartera=null;
		desdeDocumento=null;
		hastaDocumento=null;
		hastaCartera=null;
		desdeCobrador=null;
		hastaCobrador=null;
		desdeUpa=null;
		hastaUpa=null;
		desdeInquilino=null;
		hastaInquilino=null;
		desdeDetraccion=null;
		hastaDetraccion=null;
		nrocartera="";
	}

	public void vercomprobante(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();

		if (comprobantepago!=null) {
			if (comprobantepago.getIdtipdocu().equals("01") || comprobantepago.getIdtipdocu().equals("03") || comprobantepago.getIdtipdocu().equals("00")  ) {
				listaDetalleComprobante=cobranzaRecaudacionService.obtenerDetalleComprobante(comprobantepago.getIdcomprobante());
				contextRequest.execute("dlgVerComprobante.show();");
			} else if(comprobantepago.getIdtipdocu().equals("04")){
				contextRequest.execute("dlgVerReciboCaja.show();");
			}else{
				contextRequest.execute("dlgVerNotadebito.show();");
			}
		} else {
			addWarnMessage("Seleccione comprobante", "Usted no ha seleccionado ningï¿½n comprobante, seleccione para continuar");
		}

	}

	public void validareliminarcomprobante(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();

		if (comprobantepago==null) {
			addWarnMessage("No ha seleccionado ningun comprobante", "Para poder eliminar seleccione un item");
		} else {
			contextRequest.execute("dlgConfirmarEliminarComprobante.show();");
		}
	}

    public boolean validarEstadoSunatComprobante(Sunat_Comprobante sunat_comprobantemodel ){
    	boolean resp=Boolean.TRUE;//se encuentra validado
    	System.out.println("RESPUESTA SUNAT="+sunat_comprobantemodel.getRespuesta_sunat());
    	if(sunat_comprobantemodel.getRespuesta_sunat()==null){
    		resp=Boolean.FALSE;
    	}else{
    		 if(sunat_comprobantemodel.getRespuesta_sunat().equals(Constante.ESTADO_ACE)||sunat_comprobantemodel.getRespuesta_sunat().equals(Constante.ESTADO_CONDICION_ANU)){
    			 resp=Boolean.TRUE;
    		 }else{
    			 resp=Boolean.FALSE;
    		 }
    	}
    	return resp;
    }
    public boolean verificarEstado(Integer idcomp, String estado){
    	boolean resp= false;
    	Comprobantepago comprobantemodel= new Comprobantepago();
		comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomp);
		if(comprobantemodel!=null){
			if(comprobantemodel.getEstado().equalsIgnoreCase(estado)){
					resp=true;				
			}
		}
    	return resp;
    }
    public void validarAnularComprobante(){
    	RequestContext contextRequest = RequestContext.getCurrentInstance();
    	
    	try{
    	if (comprobantepago==null) {
			addWarnMessage("No ha seleccionado ningun comprobante", "Para poder anular seleccione un item");
		} else {	
			//verificar si esta anulado 			
			if(!verificarEstado(comprobantepago.getIdcomprobante(),Constante.ESTADO_ANU)){
			//if(!comprobantepago.getSianulado()){
				if(comprobantepago.getSifacturacionelectronica()){// Documentos electronicos
					Sunat_Comprobante sunat_comprobantemodel= new Sunat_Comprobante();
					sunat_comprobantemodel=cobranzaRecaudacionService.obtenerSunatComprobanteModel(comprobantepago.getIdcomprobante());
					if(validarEstadoSunatComprobante(sunat_comprobantemodel)){
						int idcomprobante=comprobantepago.getIdcomprobante();
						Comprobantepago comprobantemodel= new Comprobantepago();
						comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);
						if(validarFechaAnulacion(comprobantemodel.getFechaemision())){
							if(comprobantemodel.getNotadebito()!=null || comprobantemodel.getNotadebitopenalizacion()!= null){
								addInfoMessage("","El comprobante de pago se encuentra amarrada a una Nota de Debito ,Para realizar la operación , anule primero la Nota de Débito");
							}else if(comprobantemodel.getNotacredito()!=null||comprobantemodel.getNotacredito2()!=null){
								addInfoMessage("","El comprobante de pago se encuentra amarrada a una Nota de Crédito ,Para realizar la operación , anule primero la Nota de Crédito");
							}else{
								contextRequest.execute("dlgVerComprobantePagoAnular.show();");
							}
						}else {
							addWarnMessage("", " El comprobante seleccionado excede el limite de dias para su anulación ");
						}
					}else{
						addWarnMessage(" ", " El comprobante seleccionado no se encuentra validado "+" Para poder ANULARLO , realice primero la validacion del documento en el Sistema de Facturación Electrónica");
					 }
					}else{// Documentos fisicos 
						int idcomprobante=comprobantepago.getIdcomprobante();
						Comprobantepago comprobantemodel= new Comprobantepago();
						comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);
						if(validarFechaAnulacion(comprobantemodel.getFechaemision())){
							if(comprobantemodel.getNotadebito()!=null || comprobantemodel.getNotadebitopenalizacion()!= null){
								addInfoMessage("","El comprobante de pago se encuentra amarrada a una Nota de Debito ,Para realizar la operación , anule primero la Nota de Débito");
							}else if(comprobantemodel.getNotacredito()!=null||comprobantemodel.getNotacredito2()!=null){
								addInfoMessage("","El comprobante de pago se encuentra amarrada a una Nota de Crédito ,Para realizar la operación , anule primero la Nota de Crédito");
							}else{
								contextRequest.execute("dlgVerComprobantePagoAnular.show();");
							}
						}else {
							addWarnMessage("", " El comprobante seleccionado excede el limite de dias para su anulación ");
						}
						
						
						
				}
			}else{
				addWarnMessage("", " El comprobante seleccionado se encuentra en estado ANULADO ");
			}
		}
    	}catch(Exception e ){
    		e.printStackTrace();
    		
    	}
    }
    public void validarAnularDocumento(){
    	RequestContext contextRequest = RequestContext.getCurrentInstance();
    	String valor="";
    	try{
    		System.out.println("validar documento");
    		valor=descripcionbaja;
    		if(valor!=null){     			
    			valor=valor.replaceAll(" ", "");    			    			
    			if(valor.length()<=0 || valor.equals("")){
    	    		addInfoMessage("","Agrege el motivo de la anulación del documento");
    			}else{
    				//System.out.println("DESCRIPCION ="+descripcionbaja);
    				comprobantepago.setDescripcion_baja(descripcionbaja);
    				contextRequest.execute("dlgConfirmarAnularComprobante.show();");
    				
    			}
    		}
    	}catch(Exception e ){
    		e.printStackTrace();
    	}
    }
    public void anularComprobantePago(){
    	try{
    	Comprobantepago cp = new Comprobantepago();
    	Sunat_Comprobante scp= new Sunat_Comprobante();
    	if(comprobantepago.getSifacturacionelectronica()){// comprobantes electronicos
    		scp=cobranzaRecaudacionService.obtenerSunatComprobanteModel(comprobantepago.getIdcomprobante());   	
    		// Se actualizara la tabla de SUNATFE_COMPROBANTE
    		scp.setUsuario_Anula_Documento(userMB.getNombreusr().toUpperCase());
    		scp.setFecha_Anula_Documento(new Date());
    		scp.setObservaciones(comprobantepago.getDescripcion_baja());
    		scp.setHost_IP_Anula_Documento(CommonsUtilities.getRemoteIpAddress());
    		cobranzaRecaudacionService.anularComprobanteSFE(scp);
    		
    	}
    	// SGI COMPROBANTE
    	cp=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
    	cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
    	cp.setUsuario_anula_documento(userMB.getNombreusr().toUpperCase());
    	cp.setFechamodificacion(new Date());
    	cp.setFecha_anula_documento(cp.getFechamodificacion());
    	cp.setDescripcion_baja(comprobantepago.getDescripcion_baja());
    	cp.setHost_ip_anula_documento(CommonsUtilities.getRemoteIpAddress());
    	cp.setEstado(Constante.ESTADO_ANU);
    	cp.setCondicion(Constante.ESTADO_CONDICION_ANU);
    	cp.setSianulado(Boolean.TRUE);
    	//cobranzaRecaudacionService.anularComprobante(cp);
    	cobranzaRecaudacionService.actualizarComprobantePago(cp);
    	
    	}catch(Exception e){
    		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
    		e.printStackTrace();
    		Logger.error(e.getMessage(), e);
    		
    	}
    }
    public void anularComprobante(){
    	try{
    		int idcomprobante=comprobantepago.getIdcomprobante();
    		Comprobantepago comprobantemodel= new Comprobantepago();
    		int idcontrato = 0;
    		
    		List<Detallecuota> detcuotaComp= new ArrayList<Detallecuota>();
    		List<Detallecuota_arbitrio> detcuotaArbComp= new ArrayList<Detallecuota_arbitrio>();
    		detcuotaComp=cobranzaRecaudacionService.obtenerDetalleComprobante(idcomprobante);
    		comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);
    		detcuotaArbComp=cobranzaRecaudacionService.obtenerDetalleCuotaArbitrioComprobante(idcomprobante);
    		
    		
    		Integer nroSubpagosMes;
    		
    		for (int i = 0; i < detcuotaComp.size(); i++) {
    			Cuota cuotaSeleccionada= new Cuota();
    		    Cuota cuota= new Cuota();

    		    nroSubpagosMes=cobranzaRecaudacionService.obtenerNroSubpagosMesDetalleCuota(detcuotaComp.get(i).getCuota().getIdcuota());
    		    cuotaSeleccionada=cobranzaRecaudacionService.obtenerCuota(detcuotaComp.get(i).getCuota().getIdcuota());

//    		    Contrato con= new Contrato();
//    		    con.setIdcontrato(cuotaSeleccionada.getContrato().getIdcontrato());
//    		    idcontrato=con.getIdcontrato();
    		    
    		    cuotaSeleccionada.setFecmod(new Date());
				cuotaSeleccionada.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuotaSeleccionada.setCancelado("0");
				cuotaSeleccionada.setNropagos(cuotaSeleccionada.getNropagos()-1);
				System.out.println("cuotaSeleccionada.morasoles="+cuotaSeleccionada.getMorasoles());
				System.out.println("detcuotaComp.morasoles="+detcuotaComp.get(i).getMora());
				System.out.println("cuotaSeleccionada.Montopenalizacion="+cuotaSeleccionada.getMontopenalizacion());
				System.out.println("detcuotaComp.Montopenalizacion="+detcuotaComp.get(i).getMontopenalizacion());
				System.out.println("cuotaSeleccionada.Montosoles="+cuotaSeleccionada.getMontosoles());
				System.out.println("detcuotaComp.Montosoles="+detcuotaComp.get(i).getMontosoles());
				
				if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){//nota de debito
					if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_MORA)){
						cuotaSeleccionada.setMorasoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMorasoles(),detcuotaComp.get(i).getMora()));
					}else if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
						cuotaSeleccionada.setMontopenalizacion(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontopenalizacion(),detcuotaComp.get(i).getMontopenalizacion()));
					}					
				}else if(comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){//nota de credito
					cuotaSeleccionada.setMontosoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontosoles(),detcuotaComp.get(i).getMontosoles()));
				}else{//cuota de renta
					cuotaSeleccionada.setMontosoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontosoles(),detcuotaComp.get(i).getMontosoles()));
				}
			    detcuotaComp.get(i).setEstado(Constante.ESTADO_ANU);
			    detcuotaComp.get(i).setCondicion(Constante.ESTADO_CONDICION_ANU);
				detcuotaComp.get(i).setUsuario_anula(userMB.getNombreusr().toUpperCase());
				detcuotaComp.get(i).setFecha_anula(new Date());
				detcuotaComp.get(i).setDescripcion_anula(comprobantepago.getDescripcion_baja());
				detcuotaComp.get(i).setHost_ip_anula(CommonsUtilities.getRemoteIpAddress());
				System.out.println("cuotaSeleccionada.morasolesF="+cuotaSeleccionada.getMorasoles());
				System.out.println("cuotaSeleccionada.MontopenalizacionF="+cuotaSeleccionada.getMontopenalizacion());
				System.out.println("cuotaSeleccionada.MontosolesF="+cuotaSeleccionada.getMontosoles());
				cobranzaRecaudacionService.actualizarCuota(cuotaSeleccionada);
				cobranzaRecaudacionService.actualizarEstadoDetalleCuota(detcuotaComp.get(i));   		
    		}
    		for (int i = 0; i < detcuotaArbComp.size(); i++) {
    			Cuota_arbitrio cuotaSeleccionada= new Cuota_arbitrio();

    		    nroSubpagosMes=cobranzaRecaudacionService.obtenerNroSubpagosMesDetalleCuotaArb(detcuotaArbComp.get(i).getCuota_arbitrio().getIdcuota_arbitrio());
    		    cuotaSeleccionada=cobranzaRecaudacionService.obtenerCuotaArbitrio(detcuotaArbComp.get(i).getCuota_arbitrio().getIdcuota_arbitrio());
    		    Comprobantepago c= null;
    		    cuotaSeleccionada.setComprobantepago(c);
    		    cuotaSeleccionada.setEstado(Constante.ESTADO_PENDIENTE);
    		    cuotaSeleccionada.setFecmod(new Date());
				cuotaSeleccionada.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuotaSeleccionada.setCancelado("0");
				cuotaSeleccionada.setNropagos(cuotaSeleccionada.getNropagos()-1);
				
				 if(comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){//nota de credito
					cuotaSeleccionada.setMonto(FuncionesHelper.restarDouble(cuotaSeleccionada.getMonto(),detcuotaArbComp.get(i).getMontosoles()));
				}//else{//cuota de renta
					//cuotaSeleccionada.setMonto(cuotaSeleccionada.getMonto()-detcuotaArbComp.get(i).getMontosoles());
				//}
				
				detcuotaArbComp.get(i).setEstado(Constante.ESTADO_ANU);
				detcuotaArbComp.get(i).setCondicion(Constante.ESTADO_CONDICION_ANU);
				detcuotaArbComp.get(i).setUsuario_anula(userMB.getNombreusr().toUpperCase());
			    detcuotaArbComp.get(i).setFecha_anula(new Date());
				detcuotaArbComp.get(i).setDescripcion_anula(comprobantepago.getDescripcion_baja());
				detcuotaArbComp.get(i).setHost_ip_anula(CommonsUtilities.getRemoteIpAddress());
				cobranzaRecaudacionService.actualizarCuotaArbitrio(cuotaSeleccionada);
				cobranzaRecaudacionService.actualizarEstadoDetalleCuotaArbitrio(detcuotaArbComp.get(i));   		
    		}
    		//Verificar si es nota debito/nota credito o otro
    		List<Comprobantepago> lista=new ArrayList<Comprobantepago>();
    		if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)) {
    			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"debito");
    			for (Comprobantepago cp : lista) {
    				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
    				  cp.setSigeneronotadebito(false);
    				  Comprobantepago c= null;
    				  cp.setNotadebito(c);
    				  cp.setMoradetectada(0.0);
    				  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
    				  cp.setFechamodificacion(new Date());
    				  //cp.setSianulado(true);
    				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
    			}
    			/*anular Nota Debito*/
    			
				//cobranzaRecaudacionService.eliminarComprobante(idcomprobante);
				anularComprobantePago();
				//cobranzaRecaudacionService.anularComprobante(comprobantepago);
				
    			
    		}else if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)) {
    			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"credito");
    			for (Comprobantepago cp : lista) {
    				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
    				  cp.setSigeneronotacredito(false);
    				  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
    				  cp.setFechamodificacion(new Date());
    				  //cp.setSianulado(true);
    				  Comprobantepago c= null;
    				  cp.setNotacredito(c);
    				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
    			}
    			/*anular Nota Credito*/
				//cobranzaRecaudacionService.eliminarComprobante(idcomprobante);
				anularComprobantePago();
    		}else {			
    			anularComprobantePago();
    		}
    		if (tipodeconsulta.equals("cobrador")) {
    			buscarComprobantesCobrador();
    		}else if (tipodeconsulta.equals("usuario")) {
    			buscarComprobantesUsuario();
    		}else if (tipodeconsulta.equals("upa")) {
    			buscarComprobantesUpa();
    		}else if (tipodeconsulta.equals("contrato")) {
    			buscarComprobantesNroDocumento();
    		}else if (tipodeconsulta.equals("inquilino")) {
    			buscarComprobantesInquilino();
    		}else if (tipodeconsulta.equals("documento")){
    			buscarComprobantesNroDocumento();
    		}else{
    			buscarComprobantesDetraccion();
    		}
    		
    		addInfoMessage("","Se anulo con exito el comprobante de pago N°"+comprobantepago.getNroserie()+'-'+comprobantepago.getNrocomprobante());
    	}catch(Exception e){
    		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
    		e.printStackTrace();
    		Logger.error(e.getMessage(), e);
    	}
    }
    public void actualizarCondicionComprobanteRechazado(int idcomp){
    	try{
    		//int idcomprobante=comprobantepago.getIdcomprobante();
    		int idcomprobante=idcomp;
    		Comprobantepago comprobantemodel= new Comprobantepago();
    		int idcontrato = 0;
    		
    		List<Detallecuota> detcuotaComp= new ArrayList<Detallecuota>();
    		detcuotaComp=cobranzaRecaudacionService.obtenerDetalleComprobante(idcomprobante);
    		comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);
    		
    		Integer nroSubpagosMes;
    		
    		for (int i = 0; i < detcuotaComp.size(); i++) {
    			Cuota cuotaSeleccionada= new Cuota();
    		    Cuota cuota= new Cuota();

    		    nroSubpagosMes=cobranzaRecaudacionService.obtenerNroSubpagosMesDetalleCuota(detcuotaComp.get(i).getCuota().getIdcuota());
    		    cuotaSeleccionada=cobranzaRecaudacionService.obtenerCuota(detcuotaComp.get(i).getCuota().getIdcuota());
                
    		    Contrato con= new Contrato();
    		    con.setIdcontrato(cuotaSeleccionada.getContrato().getIdcontrato());
    		    idcontrato=con.getIdcontrato();
    		    
    		    //cuotaSeleccionada.setFecmod(new Date());
				//cuotaSeleccionada.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuotaSeleccionada.setCancelado("0");
				cuotaSeleccionada.setNropagos(cuotaSeleccionada.getNropagos()-1);
				System.out.println("cuotaSeleccionada.morasoles="+cuotaSeleccionada.getMorasoles());
				System.out.println("detcuotaComp.morasoles="+detcuotaComp.get(i).getMora());
				System.out.println("cuotaSeleccionada.Montopenalizacion="+cuotaSeleccionada.getMontopenalizacion());
				System.out.println("detcuotaComp.Montopenalizacion="+detcuotaComp.get(i).getMontopenalizacion());
				System.out.println("cuotaSeleccionada.Montosoles="+cuotaSeleccionada.getMontosoles());
				System.out.println("detcuotaComp.Montosoles="+detcuotaComp.get(i).getMontosoles());
				
				if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){//nota de debito
					if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_MORA)){
						cuotaSeleccionada.setMorasoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMorasoles(),detcuotaComp.get(i).getMora()));
					}else if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
						cuotaSeleccionada.setMontopenalizacion(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontopenalizacion(),detcuotaComp.get(i).getMontosoles()));
					}					
				}else if(comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){//nota de credito
					cuotaSeleccionada.setMontosoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontosoles(),detcuotaComp.get(i).getMontosoles()));
				}else{//cuota de renta
					cuotaSeleccionada.setMontosoles(FuncionesHelper.restarDouble(cuotaSeleccionada.getMontosoles(),detcuotaComp.get(i).getMontosoles()));
				}
			    detcuotaComp.get(i).setEstado(Constante.ESTADO_REC); //RECHZADO
			    detcuotaComp.get(i).setCondicion(Constante.ESTADO_CONDICION_REC); //RECHZADO
				//detcuotaComp.get(i).setUsuario_anula(userMB.getNombreusr());
				//detcuotaComp.get(i).setFecha_anula(new Date());
				//detcuotaComp.get(i).setDescripcion_anula(comprobantepago.getDescripcion_baja());
				//detcuotaComp.get(i).setHost_ip_anula(CommonsUtilities.getRemoteIpAddress());
			    System.out.println("cuotaSeleccionada.morasolesF="+cuotaSeleccionada.getMorasoles());
				System.out.println("cuotaSeleccionada.MontopenalizacionF="+cuotaSeleccionada.getMontopenalizacion());
				System.out.println("cuotaSeleccionada.MontosolesF="+cuotaSeleccionada.getMontosoles());
				cobranzaRecaudacionService.actualizarCuota(cuotaSeleccionada);
				cobranzaRecaudacionService.actualizarEstadoDetalleCuota(detcuotaComp.get(i));   		
    		}
    		//Verificar si es nota debito/nota credito o otro
    		List<Comprobantepago> lista=new ArrayList<Comprobantepago>();
    		if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)) {
    			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"debito");
    			for (Comprobantepago cp : lista) {
    				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
    				  cp.setSigeneronotadebito(false);
    				  Comprobantepago c= null;
    				  cp.setNotadebito(c);
    				  cp.setMoradetectada(0.0);
    				  //cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
    				  //cp.setFechamodificacion(new Date());
    				  //cp.setSianulado(true);
    				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
    			}				
    			
    		}else if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)) {
    			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"credito");
    			for (Comprobantepago cp : lista) {
    				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
    				  cp.setSigeneronotacredito(false);
    				 // cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
    				  //cp.setFechamodificacion(new Date());
    				  //cp.setSianulado(true);
    				  Comprobantepago c= null;
    				  cp.setNotacredito(c);
    				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
    			}
    			/*anular Nota Credito*/
				//cobranzaRecaudacionService.eliminarComprobante(idcomprobante);
				//anularComprobantePago();
    		}else {			
    			//anularComprobantePago();
    		}
//    		if (tipodeconsulta.equals("cobrador")) {
//    			buscarComprobantesCobrador();
//    		}else if (tipodeconsulta.equals("usuario")) {
//    			buscarComprobantesUsuario();
//    		}else if (tipodeconsulta.equals("upa")) {
//    			buscarComprobantesUpa();
//    		}else if (tipodeconsulta.equals("contrato")) {
//    			buscarComprobantesNroDocumento();
//    		}else if (tipodeconsulta.equals("inquilino")) {
//    			buscarComprobantesInquilino();
//    		}else if (tipodeconsulta.equals("documento")){
//    			buscarComprobantesNroDocumento();
//    		}else{
//    			buscarComprobantesDetraccion();
//    		}
    		
    		//addInfoMessage("","Se anulo con exito el comprobante de pago N°"+comprobantepago.getNroserie()+'-'+comprobantepago.getNrocomprobante());
    	}catch(Exception e){
    		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
    		e.printStackTrace();
    		Logger.error(e.getMessage(), e);
    	}
    }
    
    public void cancelarAnularDocumento(){
    	descripcionbaja=null;
    	comprobantepago.setDescripcion_baja(null);
    }
	public void eliminarcomprobante() 
	{   try{
		int idcomprobante=comprobantepago.getIdcomprobante();
		Comprobantepago comprobantemodel= new Comprobantepago();
		int idcontrato = 0;

		List<Detallecuota> detcuotaComp= new ArrayList<Detallecuota>();
		List<Detallecuota_arbitrio> detcuotaArbComp= new ArrayList<Detallecuota_arbitrio>();
		detcuotaComp=cobranzaRecaudacionService.obtenerDetalleComprobante(idcomprobante);
		detcuotaArbComp=cobranzaRecaudacionService.obtenerDetalleCuotaArbitrioComprobante(idcomprobante);
		comprobantemodel=cobranzaRecaudacionService.obtenerComprobanteModel(idcomprobante);

		Integer nroSubpagosMes;

		for (int i = 0; i < detcuotaComp.size(); i++) {
			Cuota cuotaSeleccionada= new Cuota();
		   // Cuota cuota= new Cuota();

		    nroSubpagosMes=cobranzaRecaudacionService.obtenerNroSubpagosMesDetalleCuota(detcuotaComp.get(i).getCuota().getIdcuota());
		    cuotaSeleccionada=cobranzaRecaudacionService.obtenerCuota(detcuotaComp.get(i).getCuota().getIdcuota());

		    Contrato con= new Contrato();
		    con.setIdcontrato(cuotaSeleccionada.getContrato().getIdcontrato());
		    idcontrato=con.getIdcontrato();
//		    cuota.setIdcuota(cuotaSeleccionada.getIdcuota());
//		    cuota.setCuotanumero(cuotaSeleccionada.getCuotanumero());
//		    cuota.setAniocuotames(cuotaSeleccionada.getAniocuotames());
//		    cuota.setMespagado(cuotaSeleccionada.getMespagado());
//		    cuota.setFeccre(cuotaSeleccionada.getFeccre());
//		    cuota.setUsrcre(cuotaSeleccionada.getUsrcre());
//		    cuota.setContrato(con);
            		    
		    if (nroSubpagosMes==1) {
				cobranzaRecaudacionService.eliminarCuota(detcuotaComp.get(i).getCuota().getIdcuota());
			}else {	
				cuotaSeleccionada.setFecmod(new Date());
				cuotaSeleccionada.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuotaSeleccionada.setCancelado("0");
				cuotaSeleccionada.setNropagos(cuotaSeleccionada.getNropagos()-1);
				if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){//nota de debito
					if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_MORA)){
						cuotaSeleccionada.setMorasoles(cuotaSeleccionada.getMorasoles()-detcuotaComp.get(i).getMora());
					}else if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
						cuotaSeleccionada.setMontopenalizacion(cuotaSeleccionada.getMontopenalizacion()-detcuotaComp.get(i).getMontopenalizacion());
					}					
				}else if(comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){//nota de credito
					cuotaSeleccionada.setMontosoles(cuotaSeleccionada.getMontosoles()-detcuotaComp.get(i).getMontosoles());
				}else{//cuota de renta
					cuotaSeleccionada.setMontosoles(cuotaSeleccionada.getMontosoles()-detcuotaComp.get(i).getMontosoles());
				}
				cobranzaRecaudacionService.actualizarCuota(cuotaSeleccionada);
			}
           
		    
		}
		for (int i = 0; i < detcuotaArbComp.size(); i++) {
			Cuota_arbitrio cuotaSeleccionada= new Cuota_arbitrio();
		    //Cuota cuota= new Cuota();

		    nroSubpagosMes=cobranzaRecaudacionService.obtenerNroSubpagosMesDetalleCuotaArb(detcuotaComp.get(i).getCuota().getIdcuota());
		    cuotaSeleccionada=cobranzaRecaudacionService.obtenerCuotaArbitrio(detcuotaComp.get(i).getCuota().getIdcuota());

		    //Contrato con= new Contrato();
		    Arbitrio arb = new Arbitrio();
		    arb.setIdarbitrio(cuotaSeleccionada.getArbitrio().getIdarbitrio());
		   // idcontrato=arb.getIdcontrato();
//		    cuota.setIdcuota(cuotaSeleccionada.getIdcuota());
//		    cuota.setCuotanumero(cuotaSeleccionada.getCuotanumero());
//		    cuota.setAniocuotames(cuotaSeleccionada.getAniocuotames());
//		    cuota.setMespagado(cuotaSeleccionada.getMespagado());
//		    cuota.setFeccre(cuotaSeleccionada.getFeccre());
//		    cuota.setUsrcre(cuotaSeleccionada.getUsrcre());
//		    cuota.setContrato(con);
            		    
//		    if (nroSubpagosMes==1) {
//				cobranzaRecaudacionService.eliminarCuota(detcuotaComp.get(i).getCuota().getIdcuota());
//			}else {	
				cuotaSeleccionada.setFecmod(new Date());
				cuotaSeleccionada.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuotaSeleccionada.setCancelado("0");
				cuotaSeleccionada.setNropagos(cuotaSeleccionada.getNropagos()-1);
//				if (comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){//nota de debito
//					if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_MORA)){
//						cuotaSeleccionada.setMorasoles(cuotaSeleccionada.getMorasoles()-detcuotaComp.get(i).getMora());
//					}else if(comprobantemodel.getTipoconcepto().getIdtipoconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
//						cuotaSeleccionada.setMontopenalizacion(cuotaSeleccionada.getMontopenalizacion()-detcuotaComp.get(i).getMontopenalizacion());
//					}					
//				}else if(comprobantemodel.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){//nota de credito
//					cuotaSeleccionada.setMontosoles(cuotaSeleccionada.getMontosoles()-detcuotaComp.get(i).getMontosoles());
//				}else{//cuota de renta
//					cuotaSeleccionada.setMontosoles(cuotaSeleccionada.getMontosoles()-detcuotaComp.get(i).getMontosoles());
//				}
				//cobranzaRecaudacionService.actualizarCuota(cuotaSeleccionada); 
			//}
           
		    
		}
		//Verificar si es nota debito/nota credito o otro
		List<Comprobantepago> lista=new ArrayList<Comprobantepago>();
		if (comprobantemodel.getTipodocu().getIdtipodocu().equals("08")) {/*Se selecciono una nota debito para eliminar*/
			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"debito");

			for (Comprobantepago cp : lista) {
				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
				  cp.setSigeneronotadebito(false);
				  Comprobantepago c= null;
				  cp.setNotadebito(c);
				  cp.setMoradetectada(0.0);
				  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				  cp.setFechamodificacion(new Date());
				  //cp.setSianulado(true);
				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
			}
				/*eliminar Nota Debito*/
				cobranzaRecaudacionService.eliminarComprobante(idcomprobante);

		} else if (comprobantemodel.getTipodocu().getIdtipodocu().equals("09")) {/*Se selecciono una nota credito para eliminar*/
			lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,"credito");

			for (Comprobantepago cp : lista) {
				/*actualizar dependencias - comprobante padre sigeneronotadebito*/
				  cp.setSigeneronotacredito(false);
				  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				  cp.setFechamodificacion(new Date());
				  //cp.setSianulado(true);
				  Comprobantepago c= null;
				  cp.setNotacredito(c);
				  cobranzaRecaudacionService.actualizarComprobantePago(cp);
			}
				/*eliminar Nota Debito*/
				cobranzaRecaudacionService.eliminarComprobante(idcomprobante);

		} else {/*Se selecciono un tipo de comprobante diferente para eliminar*/
			if (comprobantemodel.getNotadebito()!=null) {
				lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(comprobantemodel.getNotadebito().getIdcomprobante(),"debito");//buscaridnotadebito
				for (Comprobantepago cp : lista) {
					/*actualizar dependencias - comprobante padre sigeneronotadebito*/
					  cp.setSigeneronotadebito(false);
					  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					  cp.setFechamodificacion(new Date());
					  //cp.setSianulado(true);
					  Comprobantepago c= null;
					  cp.setNotadebito(c);
					  cobranzaRecaudacionService.actualizarComprobantePago(cp);
				}
				cobranzaRecaudacionService.eliminarComprobante(idcomprobante);//eliminar otro comprobante
				cobranzaRecaudacionService.eliminarComprobante(comprobantemodel.getNotadebito().getIdcomprobante());//eliminar Notadebito
			}if (comprobantemodel.getNotacredito()!=null){
				lista=cobranzaRecaudacionService.getIdComprobantesPadreDeNotaDebito_Credito(comprobantemodel.getNotacredito().getIdcomprobante(),"credito");//buscaridnotadebito

				for (Comprobantepago cp : lista) {
					/*actualizar dependencias - comprobante padre sigeneronotacredito*/
					  cp.setSigeneronotacredito(false);
					  cp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					  cp.setFechamodificacion(new Date());
					  Comprobantepago c= null;
					  cp.setNotacredito(c);
					  cobranzaRecaudacionService.actualizarComprobantePago(cp);
				}
				cobranzaRecaudacionService.eliminarComprobante(idcomprobante);//eliminar otro comprobante
				cobranzaRecaudacionService.eliminarComprobante(comprobantemodel.getNotacredito().getIdcomprobante());//eliminar Notadebito
			}else {
//				Comprobantepago cpp=new Comprobantepago();
//				 cpp.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				 cpp.setFechamodificacion(new Date());
//				 cpp.setIdcomprobante(idcomprobante);
//				 cobranzaRecaudacionService.actualizarComprobantePago(cpp);
				 cobranzaRecaudacionService.eliminarComprobante(idcomprobante);//eliminar otro comprobante
			}
		}

		if (tipodeconsulta.equals("cobrador")) {
			buscarComprobantesCobrador();
		}else if (tipodeconsulta.equals("usuario")) {
			buscarComprobantesUsuario();
		}else if (tipodeconsulta.equals("upa")) {
			buscarComprobantesUpa();
		}else if (tipodeconsulta.equals("contrato")) {
			buscarComprobantesNroDocumento();
		}else if (tipodeconsulta.equals("inquilino")) {
			buscarComprobantesInquilino();
		}else if (tipodeconsulta.equals("documento")){
			buscarComprobantesNroDocumento();
		}else{
			buscarComprobantesDetraccion();
		}

	    //actualizar
		    Contrato contrato= new Contrato();
		    contrato=contratoService.obtenerContratoPorID(idcontrato);

		    if (contrato.getCondicion().equals("Contrato") || contrato.getCondicion().equals("Precontrato")||contrato.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)) {
		    	cobranzaRecaudacionService.detectarSiCompletoCuotasContrato(contrato.getIdcontrato());
			} else {
				detectarSiCompletoCuotasSinContrato(contrato);
			}
		    addInfoMessage("","Se anulo con exito el comprobante de pago N°"+comprobantepago.getNroserie()+'-'+comprobantepago.getNrocomprobante());
	  }catch(Exception e){
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		e.printStackTrace();
		Logger.error(e.getMessage(), e);
     }
	}

    public void actualizarfecha(){
    	if(validarFechaBlanco()){
    	comprobantepago.setFecCancelacion(null);
    	}else{
    		addWarnMessage("No permitido", "NO SE PERMITE PONER LA FECHA DE CANCELACIÓN EN BLANCO (maximo 3 dias antes de la fecha actual)");
    	}
    }
    public Boolean  validarFechaAnulacion(Date fecha){
    	Boolean resp=false;
    	try{
    		if(fecha!=null){
    			if(FechaUtil.getDiasDiferencia( new Date(),fecha)<=Constante.ANULACION_MAX_DIAS){
    				resp=true;
    			}else{
    				resp=false;
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		
    	}
    	
    	return resp;
    }
    public Boolean  validarFechaCancelacion(){
    	Boolean resp= false;
    	Comprobantepago comprobante= new Comprobantepago();
    	if(!cobranzaMB.getUser_acceso_fechacanc()){
    		comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
    		if(comprobante.getFechacancelacion()!=null){
    			 if(comprobantepago.getFecCancelacion()!=null){
					if (FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(),comprobante.getFechacancelacion()) == 0) {
						resp = true;
					} else {
						if(FechaUtil.getDiasDiferencia(new Date(),comprobante.getFechacancelacion()) > 5){
							comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
							resp = false;
						}else{
						if (FechaUtil.getDiasDiferencia(new Date(),comprobantepago.getFecCancelacion()) > 5) {
							// comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
							comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
							resp = false;
						} else {
							resp = true;
						}
					}}
				} else {
					resp = true;
				}
    		}else{
    			//modifica
    			if(comprobantepago.getFecCancelacion()!=null){
    				 if(FechaUtil.getDiasDiferencia(new Date(), comprobantepago.getFecCancelacion())>5){   
             			   // comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
             			    comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
             				resp= false;
             			}else{
             			resp= true;
             		   }
            		
    			}else{
    				resp= true;
    			}
    			
    		}

    	}else{
    		resp= true;
    	}
    	return resp;
    }
    public Boolean validarFechaBlanco(){
    	Boolean resp= false;
    	Comprobantepago comprobante= new Comprobantepago();
    	comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
    	if(!cobranzaMB.getUser_acceso_fechacanc()){
    		if(comprobante.getFechacancelacion()!=null){
    			if(FechaUtil.getDiasDiferencia(new Date(),comprobante.getFechacancelacion()) > 3){
					comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
					resp = false;
				}else{
					resp=true;
				}
    		}
    	}else{
    		resp=true;
    	}
    	return resp;
		
    }
    public Boolean  validarNombreCobrador(){
    	Boolean resp= false;
//    	Comprobantepago comprobante= new Comprobantepago();
//    	if(!cobranzaMB.getUser_acceso_fechacanc()){
//    		comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
//    		if(comprobante.getFechacancelacion()!=null){
//    			 if(comprobantepago.getFecCancelacion()!=null){
//					if (FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(),comprobante.getFechacancelacion()) == 0) {
//						if(FechaUtil.getDiasDiferencia(new Date(),comprobantepago.getFecCancelacion()) > 5){
//							resp = false;
//						}else{
//						resp = true;}
//					} else {
//						if(FechaUtil.getDiasDiferencia(new Date(),comprobante.getFechacancelacion()) > 5){
//							comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
//							resp = false;
//						}else{
//						if (FechaUtil.getDiasDiferencia(new Date(),comprobantepago.getFecCancelacion()) > 5) {
//							// comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
//							comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
//							resp = false;
//						} else {
//							resp = true;
//						}
//					}}
//				} else {
//					resp = true;
//				}
//    		}else{
//    			//modifica
//    			if(comprobantepago.getFecCancelacion()!=null){
//    				 if(FechaUtil.getDiasDiferencia(new Date(), comprobantepago.getFecCancelacion())>3){   
//             			   // comprobante=cobranzaRecaudacionService.obtenerComprobanteModel(comprobantepago.getIdcomprobante());
//             			    comprobantepago.setFecCancelacion(comprobante.getFechacancelacion());
//             				resp= false;
//             			}else{
//             			resp= true;
//             		   }
//            		
//    			}else{
//    				resp= true;
//    			}
//    			
//    		}
//
//    	}else{
//    		resp= true;
//    	}
    	resp=true;
    	return resp;
    }
	public void actualizarComprobante(){
		try{
		if (comprobantepago.getSifacturacionelectronica()) {
			
//			if (comprobantepago.getIdtipdocu().equals("01") && FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(), comprobantepago.getFecVencimiento())>7){
//				comprobantepago.setFecCancelacion(null);
//				addWarnMessage("", "La fecha de cancelación puede superar como máximo en 7 días a la fecha de vencimiento para una Factura. ");
//				
//			}else 
			if(comprobantepago.getIdtipdocu().equals("01") && comprobantepago.getFecCancelacion()!=null && FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(), comprobantepago.getFecEmision())<0){
				comprobantepago.setFecCancelacion(null);
				addWarnMessage("", "La fecha de cancelación debe ser mayor o igual a la fecha de emisión para una Factura. ");
				
			}
//			else if(comprobantepago.getIdtipdocu().equals("03") && FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(), comprobantepago.getFecVencimiento())>0){
//				comprobantepago.setFecCancelacion(null);
//				addWarnMessage("", "La fecha de cancelación debe ser menor o igual a la fecha de vencimiento para una Boleta de Venta. ");
//				
//			}
			else if(comprobantepago.getIdtipdocu().equals("03") && comprobantepago.getFecCancelacion()!=null && FechaUtil.getDiasDiferencia(comprobantepago.getFecCancelacion(), comprobantepago.getFecEmision())<0)
			{
				comprobantepago.setFecCancelacion(null);
				addWarnMessage("", "La fecha de cancelación debe ser mayor o igual a la fecha de emisión para una Boleta de Venta. ");
				
			} else if (!validarFechaCancelacion()){
				addWarnMessage("No permitido", "NO SE PERMITE MODIFICAR LA FECHA DE CANCELACIÓN (maximo 3 dias antes de la fecha actual) ");
			}else if (!validarNombreCobrador()){
				addWarnMessage("No permitido", "NO SE PERMITE MODIFICAR EL NOMBRE DE COBRADOR (maximo 3 dias antes de la fecha actual) ");
			}else {
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+"-"+comprobantepago.getNrocomprobante());
					if (comprobantepago.getSiautodetraccion() != null) {
						if (comprobantepago.getSiautodetraccion()) {
							comprobantepago.setSiautodetraccion(true);
							comprobantepago
									.setMontoautodetraccion(FuncionesHelper.redondearNum(
											(comprobantepago.getTotal() * 0.1),
											2));
						} else {
							comprobantepago.setSiautodetraccion(false);
							comprobantepago.setMontoautodetraccion(0.0);
						}
					}else{
						comprobantepago.setSiautodetraccion(false);
						comprobantepago.setMontoautodetraccion(0.0);
					}
				for (ItemBean cobrador : listaCobradores) {
					if (cobrador.getDescripcion().equals(comprobantepago.getNombrecobrador())) {

						comprobantepago.setIdcobrador(cobrador.getId());
					}
				}

				comprobantepago.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				comprobantepago.setFecmod(new Date());
				
				cobranzaRecaudacionService.actualizarComprobante(comprobantepago);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Éxito", "Se Actualizo al Comprobante de pago N°: "+comprobantepago.getNroseriecomprobante() + " ");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

			
		} 
		else {
				
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+"-"+comprobantepago.getNrocomprobante());
				if (comprobantepago.getSiautodetraccion() != null) {
					if (comprobantepago.getSiautodetraccion()) {
						comprobantepago.setSiautodetraccion(true);
						comprobantepago
								.setMontoautodetraccion(FuncionesHelper.redondearNum(
										(comprobantepago.getTotal() * 0.1),
										2));
					} else {
						comprobantepago.setSiautodetraccion(false);
						comprobantepago.setMontoautodetraccion(0.0);
					}
				}else{
					comprobantepago.setSiautodetraccion(false);
					comprobantepago.setMontoautodetraccion(0.0);
				}
				for (ItemBean cobrador : listaCobradores) {
					if (cobrador.getDescripcion().equals(comprobantepago.getNombrecobrador())) {

						comprobantepago.setIdcobrador(cobrador.getId());
					}
				}

				comprobantepago.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				comprobantepago.setFecmod(new Date());
				//System.out.println("comprobantepago.getTotal()"+comprobantepago.getTotal());
				//comprobantepago.setTotal(comprobantepago.getTotal());
				//comprobantepago.setIgv(FuncionesHelper.redondear((FuncionesHelper.redondear(comprobantepago.getTotal(),2)-FuncionesHelper.redondear(comprobantepago.getSubtotal(),2)),2));
				cobranzaRecaudacionService.actualizarComprobante(comprobantepago);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Éxito", "Se Actualizo al Comprobante de pago N°: "+comprobantepago.getNroseriecomprobante() + " ");
				FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}catch(Exception e ){
		addErrorMessage("",Constantes.MSG_GENERO_ERROR_OPERACION);
		Logger.error(e.getMessage(),e);
		e.printStackTrace();
	}
}

public void detectarSiCompletoCuotasSinContrato(Contrato contrato)
{
		List<CuotaBean> listaCuotas= new ArrayList<CuotaBean>();
		CuotaBean cuota;
		Calendar ultimo = Calendar.getInstance();
		ultimo.setTime(contrato.getIniciocobro());
		List<Cuota> listaCuotasPagadas= new ArrayList<Cuota>();
		listaCuotasPagadas=cobranzaRecaudacionService.listarcuotasxcontrato(contrato.getIdcontrato());

		Calendar fin = GregorianCalendar.getInstance();

		if (contrato.getFincobro()!=null) {
			fin.setTime(contrato.getFincobro());
			fin.add(Calendar.MONTH, -1);

				while (ultimo.compareTo(fin) < 0) {

					cuota= new CuotaBean();
					ultimo.add(Calendar.MONTH, 1);

				    cuota.setAnio(ultimo.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(ultimo.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Cancelado");
				    cuota.setMonto(contrato.getMontocuotasoles());
				    cuota.setNropagos(1);

				    listaCuotas.add(cuota);
				}

				for (int j = 0;j < listaCuotas.size(); j++) {

					for (int k = 0; k <listaCuotasPagadas.size() ; k++) {
						if (listaCuotas.get(j).getMes().equals(listaCuotasPagadas.get(k).getMespagado())&& listaCuotas.get(j).getAnio()==listaCuotasPagadas.get(k).getAniocuotames()  ) {
							if (listaCuotasPagadas.get(k).getCancelado().equals("1")) {
								listaCuotas.get(j).setSipagado("1");
							} else {
								listaCuotas.get(j).setMonto(FuncionesHelper.redondear(contrato.getMontocuotasoles()-listaCuotasPagadas.get(k).getMontosoles(), 2));
								listaCuotas.get(j).setSiacuenta(true);
								listaCuotas.get(j).setDeudaacuenta(FuncionesHelper.redondear(listaCuotasPagadas.get(k).getMontosoles(),2));
								listaCuotas.get(j).setNropagos(listaCuotasPagadas.get(k).getNropagos());
							}
						}
					}
				}

			List<CuotaBean> lista= new ArrayList<CuotaBean>();

			for (int i = 0; i < listaCuotas.size(); i++) {
//				if (!listaCuotas.get(i).getSipagado()) {
				if (listaCuotas.get(i).getSipagado().equals("0")) {
					lista.add(listaCuotas.get(i));
				}
			}
			listaCuotas.clear();
			listaCuotas.addAll(lista);

			if (listaCuotas.size()==0) {
				cobranzaRecaudacionService.actualizarSiCompletoCuotasContrato_SinContrato(contrato.getIdcontrato(),true);
			}else {
				cobranzaRecaudacionService.actualizarSiCompletoCuotasContrato_SinContrato(contrato.getIdcontrato(),false);
			}

		}

	}
public void setearListaComprobantePago(List<ComprobanteBean>lista ){
	//Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
	   for (ComprobanteBean bean : lista) {
		   if(bean.getIdtipdocu().equals("09")) {
			    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
			if (bean.getTipodescuentonotacredito().equals("Renta")) {
				bean.setMora(0.0);
				bean.setOtro(0.0);
				bean.setRec_A(0.0);
				bean.setPenalizacion(0.0);
				bean.setGarantia(0.0);
				bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
				bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
				bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
				bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
			}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
				bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
				bean.setOtro(0.0);
				bean.setRec_A(0.0);
				bean.setPenalizacion(0.0);
				bean.setGarantia(0.0);
				bean.setSubtotal(0.0);					
				bean.setIgv(0.0); 
				bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
				bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
			}
		   }
	   }
}
public void agregarListaComprobantePago(List<ComprobanteBean> listaComprobante,List<ComprobanteBean> listaComprobanteEspecial){
	for (ComprobanteBean bean : listaComprobante) {
	       
		   ComprobanteBean [] array2 = new ComprobanteBean[listaComprobanteEspecial.size()];
		   array2=listaComprobanteEspecial.toArray(array2);
		   bean.setListacomprobantebeanespecial(Arrays.asList(array2));
	}
}
public void PDF_Validacion(ActionEvent actionEvent){
	try{
			if (listacomprobantespago != null) {
				if (listacomprobantespago.size() > 0) {
					/**
					 * Nota de credito con devolucion años anteriores en pagos
					 * otros
					 **/
					if (listacomprobantespagoespecial != null) {
						if (listacomprobantespagoespecial.size() > 0 && (listacomprobantespago.size() > listacomprobantespagoespecial.size())) {
							setearListaComprobantePago(listacomprobantespagoespecial);
							agregarListaComprobantePago(listacomprobantespago,listacomprobantespagoespecial);
							reporte_especial = true;
							PDFEspecial(actionEvent);
						} else {
							PDF(actionEvent);/** proceso normal de creacion de reporte PDF **/
						}
					} else {
						/** proceso normal de creacion de reporte PDF **/
						PDF(actionEvent);
					}
				} else {
					addWarnMessage("", "No existe elementos en la consulta. ");
				}
			} else {
				addWarnMessage("",
						"No existe elementos en la lista de comprobantes. ");
			}
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			e.printStackTrace();
		}
}
public void PDFEspecial(ActionEvent actionEvent){
	
	try {
			
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
			    if(cp.getSianulado()) {
			        iter.remove();
			    }
			    if(cp.getComp_opc()!=null){/**REMOVER LOS QUE CONTIENEN CONDICION COMP_OPC='D'*/
			    	if(cp.getComp_opc().equals("D")){/**D=Devolucion*/
			    		iter.remove();
			    	}
			    }
			    if(cp.getEstado()!=null){
			    	if(cp.getEstado().equals(Constante.ESTADO_REC)){// no considerar rechazados
			    		iter.remove();
			    	}
			    }
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   //jessi
			   if (!sidetallecomprobantes) { 

				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
				   nombrearchivo="Liquidación";
			   }else {
				   if(selectedComprobanteTabCobrador.size()==1 ){
					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
						   nombrearchivo="ExtincióndeDeuda";
					   }else{
						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
						   nombrearchivo="LiquidaciónDetallada";
					   }
				   }else{
					   if(reporte_especial){
					   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO_ESPECIAL;;
					   nombrearchivo="LiquidaciónDetallada";
					   } else{
						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
						   nombrearchivo="LiquidaciónDetallada";
					   }
				   }
				   
			   }
			  
			   if (desdeCobrador!=null) {
				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeCobrador));
			   }else {
				   
			   }
			  
			   parameters.put("FECHATIPOCAMBIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FECHALIQUIDACION",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("COBRADOR",selectedCobrador.getDescripcion());
			   parameters.put("INICIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FIN",hastaCobrador!=null? FuncionesHelper.fechaToString(hastaCobrador):"");
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   
			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
			   for (ComprobanteBean bean : listacomprobantespago) {
				   //System.out.println("listacomprobantespago="+listacomprobantespago);
				  
				   if(bean.getNumerooperacion()==null){
					   
					   bean.setNumerooperacion("");
					   
				   }else{
					   
					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				   }
				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				  
				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)) {
				    	
				    	sumatotal=sumatotal+bean.getTotal();
				    	suma_mc=suma_mc+bean.getSubtotal();
				    	suma_igv=suma_igv+bean.getIgv();
				    	suma_mora=suma_mora+bean.getMora();
				    	
				    }else {
				    	
				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
				 
					}
					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08"))) {
						bean.setMora(0.0);//mora para factura
						//bean.setMora(bean.getMora());
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || bean.getIdtipmovimiento().equals("06") || bean.getIdtipmovimiento().equals("07") )) {						
						bean.setMora(0.0);
						bean.setOtro(bean.getTotal());
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("08")) {
						if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
							bean.setMora(0.0);
							bean.setPenalizacion(bean.getTotal());
						}else{
							bean.setMora(bean.getTotal());
							bean.setPenalizacion(0.0);
						}
						//bean.setMora(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setGarantia(0.0);
						bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
						if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
							bean.setRec_A(bean.getTotal());
							bean.setOtro(0.0);	
							bean.setGarantia(0.0);
						}else{
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
									bean.setGarantia(bean.getTotal());
									bean.setOtro(0.0);
								}else{
								bean.setOtro(bean.getTotal());
								
								}
							}														
						}
						bean.setPenalizacion(0.0);
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setRec_A(bean.getTotal());
//						bean.setOtro(bean.getTotal());
						bean.setSubtotal(0.0);
						//bean.setGarantia(0.0);
						bean.setMora(0.0);
						bean.setIgv(0.0);
					}else if(bean.getIdtipdocu().equals("09")) {
						    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
						if (bean.getTipodescuentonotacredito().equals("Renta")) {
							bean.setMora(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);					
							bean.setIgv(0.0); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}
						
					} else {
						//System.out.println("else9");
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						//bean.setSubtotal(0.0);
//						bean.setGarantia(bean.getTotal());
						bean.setGarantia(0.0);
						//System.out.println("garantia="+bean.getGarantia());
						//bean.setMora(0.0);
						//bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						
					}
					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
					if (sidetallecomprobantes){
						  
						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
						   array=lista.toArray(array);
						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						 //calculo de igv y subtotal por detalle 
						   Double s_igv=0.0,s_subtotal=0.0;  
						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
							  
							   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
								   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
								   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
									   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
								   }
								  
							   } else{
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
								   }
								   
							   }

						   }
					if (bean.getListadetallecomprobanteBean().size() != 0) {
						if (bean.getSubtotal() != s_subtotal) {
							bean.setSubtotal(s_subtotal);
						}
						if (bean.getIgv() != s_igv) {
							bean.setIgv(s_igv);
						}
					}
					}
						R_IGV=R_IGV+bean.getIgv();
						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
				   }
			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
			   
				//validación de los totales 05-03-2019
//				Boolean validacion=false;
//			    while(!validacion){		    	
//			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//			    	if(!validacion){
//			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//			    	}
//					
//				}
			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
			   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
			   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
			   parameters.put("SUMA_TOTAL_GLOBAL",total);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+selectedCobrador.getDescripcion()+"_"+FuncionesHelper.fechaToString(new Date()));

		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
		}
	}

public void PDF(ActionEvent actionEvent){
	
	try {
			
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
				if(!selectedSiAnuladoTabCobrador){
			    if(cp.getSianulado()) { //no considerar anulados
			        iter.remove();
			        
			    }}
				if(!selectedSiRechazadoTabCobrador){
			    if(cp.getEstado()!=null){
			    	if(cp.getEstado().equals(Constante.ESTADO_REC)){// no considerar rechazados
			    		iter.remove();
			    	}
			    }
				}
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   //jessi
			   if (!sidetallecomprobantes) { 

				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
				   nombrearchivo="Liquidación";
			   }else {
				   if(selectedComprobanteTabCobrador.size()==1 ){
					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
						   nombrearchivo="ExtincióndeDeuda";
					   }else{
						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
						   nombrearchivo="LiquidaciónDetallada";
					   }
				   }else{
					   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
					   nombrearchivo="LiquidaciónDetallada";
				   }
				   
			   }
			  
			   if (desdeCobrador!=null) {
				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeCobrador));
			   }else {
				   
			   }
			  
			   parameters.put("FECHATIPOCAMBIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FECHALIQUIDACION",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("COBRADOR",selectedCobrador.getDescripcion());
			   parameters.put("INICIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FIN",hastaCobrador!=null? FuncionesHelper.fechaToString(hastaCobrador):"");
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   
			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
			   for (ComprobanteBean bean : listacomprobantespago) {
				   //System.out.println("listacomprobantespago="+listacomprobantespago);
				  //agrego a la lista
				   if(bean.getNumerooperacion()==null){
					   
					   bean.setNumerooperacion("");
					   
				   }else{
					   
					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				   }
				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				  
				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)&& !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USO_RESOLUCION_COACTIVA)) {
				    	
				    	sumatotal=sumatotal+bean.getTotal();				    	
				    	suma_mc=suma_mc+bean.getSubtotal();
				    	suma_igv=suma_igv+bean.getIgv();
				    	suma_mora=suma_mora+bean.getMora();
				    	
				    }else {
				    	
				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
				    					    	
					}
					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
						bean.setMora(0.0);//mora para factura
						//bean.setMora(bean.getMora());
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
						bean.setMora(0.0);
						bean.setOtro(bean.getTotal());
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("08")) {
						if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
							bean.setMora(0.0);
							bean.setPenalizacion(bean.getTotal());
						}else{
							bean.setMora(bean.getTotal());
							bean.setPenalizacion(0.0);
						}
						//bean.setMora(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setGarantia(0.0);
						bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
						if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
							bean.setRec_A(bean.getTotal());
							bean.setOtro(0.0);	
							bean.setGarantia(0.0);
						}else{
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
									bean.setGarantia(bean.getTotal());
									bean.setOtro(0.0);
								}else{
								bean.setOtro(bean.getTotal());
								
								}
							}														
						}
						bean.setPenalizacion(0.0);
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setRec_A(bean.getTotal());
//						bean.setOtro(bean.getTotal());
						bean.setSubtotal(0.0);
						//bean.setGarantia(0.0);
						bean.setMora(0.0);
						bean.setIgv(0.0);
					}else if(bean.getIdtipdocu().equals("09")) {
						    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
						if (bean.getTipodescuentonotacredito().equals("Renta")) {
							bean.setMora(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);					
							bean.setIgv(0.0); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}
						
					} else {
						//System.out.println("else9");
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						//bean.setSubtotal(0.0);
//						bean.setGarantia(bean.getTotal());
						bean.setGarantia(0.0);
						//System.out.println("garantia="+bean.getGarantia());
						//bean.setMora(0.0);
						//bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						
					}
					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
					if (sidetallecomprobantes){
						  
						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
						   array=lista.toArray(array);
						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						 //calculo de igv y subtotal por detalle 
						   Double s_igv=0.0,s_subtotal=0.0;  
						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
							  
							   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
								   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
								   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
									   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
								   }
								  
							   } else{
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
								   }
								   
							   }

						   }
					if (bean.getListadetallecomprobanteBean().size() != 0) {
						if (bean.getSubtotal() != s_subtotal) {
							bean.setSubtotal(s_subtotal);
						}
						if (bean.getIgv() != s_igv) {
							bean.setIgv(s_igv);
						}
					}
					}
						R_IGV=R_IGV+bean.getIgv();
						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
				   }
			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
			   
				//validación de los totales 05-03-2019
//				Boolean validacion=false;
//			    while(!validacion){		    	
//			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//			    	if(!validacion){
//			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//			    	}
//					
//				}
			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
			   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
			   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+selectedCobrador.getDescripcion()+"_"+FuncionesHelper.fechaToString(new Date()));

		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
		}
	}
public void PDF_Detraccion_Validacion(String tipoconsulta){
		try {
			if (listacomprobantespago != null) {
				if (listacomprobantespago.size() > 0) {
					if (tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)) {
						tipodeconsulta = Constante.TIPO_CONSULTA_DETRACCION;
					} else if (tipoconsulta
							.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {
						tipodeconsulta = Constante.TIPO_CONSULTA_AUTODETRACCION;
					}
					PDF_Detraccion(tipoconsulta);
				}else{
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Observación", "No hay elementos en la la consulta ");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Observación", "No hay elementos en la la consulta ");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
public void PDF_Detraccion(String tipoconsulta){
	
	try {
			
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
			    if(cp.getSianulado()) {   //no considera anulados
			        iter.remove();
			    }
			    if(cp.getEstado()!=null){
			    	if(cp.getEstado().equals(Constante.ESTADO_REC)){// no considera rechazados
			    		iter.remove();
			    	}
			    }
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   //jessi
//			   if (!sidetallecomprobantes) { 
//
//				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
//				   nombrearchivo="Liquidación";
//			   }else {
//				   if(selectedComprobanteTabCobrador.size()==1 ){
//					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
//						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
//						   nombrearchivo="ExtincióndeDeuda";
//					   }else{
//						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
//						   nombrearchivo="LiquidaciónDetallada";
//					   }
//				   }else{
//					   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
//					   nombrearchivo="LiquidaciónDetallada";
//				   }
//				   
//			   }
			   if(tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
				   reportPath=  ConstantesReporteSGI.REP_DETRACCION;;
				   nombrearchivo="Reporte_Detracciones_";
				   if (desdeDetraccion!=null) {
					   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeDetraccion));
				   }else {
					   
				   }
				  
				   parameters.put("FECHATIPOCAMBIO",desdeDetraccion!=null? FuncionesHelper.fechaToString(desdeDetraccion):"");
				   parameters.put("FECHALIQUIDACION",desdeDetraccion!=null? FuncionesHelper.fechaToString(desdeDetraccion):"");
				   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   //parameters.put("COBRADOR",selectedCobrador.getDescripcion());
				   parameters.put("COBRADOR","");
				   parameters.put("INICIO",desdeDetraccion!=null? FuncionesHelper.fechaToString(desdeDetraccion):"");
				   parameters.put("FIN",hastaDetraccion!=null? FuncionesHelper.fechaToString(hastaDetraccion):"");
				   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
				   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
				   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
				   parameters.put("REPORT_CONNECTION",conn);
				   
				   Double sumatotal=0.0,sumadetraccion=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
				   for (ComprobanteBean bean : listacomprobantespago) {
					   //System.out.println("listacomprobantespago="+listacomprobantespago);
					  //agrego a la lista
					   if(bean.getNumerooperacion()==null){
						   
						   bean.setNumerooperacion("");
						   
					   }else{
						   
						   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
					   }
					   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
					  
					    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)) {
					    	
					    	sumatotal=sumatotal+bean.getTotal();
					    	suma_mc=suma_mc+bean.getSubtotal();
					    	suma_igv=suma_igv+bean.getIgv();
					    	suma_mora=suma_mora+bean.getMora();
					    	sumadetraccion=sumadetraccion+bean.getMontodetraccion();
					    	
					    }else {
					    	
					    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
					 
						}
						if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
							bean.setMora(0.0);//mora para factura
							//bean.setMora(bean.getMora());
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
							bean.setMora(0.0);
							bean.setOtro(bean.getTotal());
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if (bean.getIdtipdocu().equals("08")) {
							if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
								bean.setMora(0.0);
								bean.setPenalizacion(bean.getTotal());
							}else{
								bean.setMora(bean.getTotal());
								bean.setPenalizacion(0.0);
							}
							//bean.setMora(bean.getTotal());
							bean.setSubtotal(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							bean.setIgv(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
							if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
								bean.setRec_A(bean.getTotal());
								bean.setOtro(0.0);	
								bean.setGarantia(0.0);
							}else{
								bean.setRec_A(0.0);
								bean.setGarantia(0.0);
								if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
									if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
										bean.setGarantia(bean.getTotal());
										bean.setOtro(0.0);
									}else{
									bean.setOtro(bean.getTotal());
									
									}
								}														
							}
							bean.setPenalizacion(0.0);
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//							bean.setRec_A(bean.getTotal());
//							bean.setOtro(bean.getTotal());
							bean.setSubtotal(0.0);
							//bean.setGarantia(0.0);
							bean.setMora(0.0);
							bean.setIgv(0.0);
						}else if(bean.getIdtipdocu().equals("09")) {
							    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
							if (bean.getTipodescuentonotacredito().equals("Renta")) {
								bean.setMora(0.0);
								bean.setOtro(0.0);
								bean.setRec_A(0.0);
								bean.setPenalizacion(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
								bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
								bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
								bean.setOtro(0.0);
								bean.setRec_A(0.0);
								bean.setPenalizacion(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(0.0);					
								bean.setIgv(0.0); 
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							}
							
						} else {
							//System.out.println("else9");
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							//bean.setSubtotal(0.0);
//							bean.setGarantia(bean.getTotal());
							bean.setGarantia(0.0);
							//System.out.println("garantia="+bean.getGarantia());
							//bean.setMora(0.0);
							//bean.setIgv(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							
						}
						System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
						if (sidetallecomprobantes){
							  
							   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
							   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
							   array=lista.toArray(array);
							   bean.setListadetallecomprobanteBean( Arrays.asList(array));
							 //calculo de igv y subtotal por detalle 
							   Double s_igv=0.0,s_subtotal=0.0;  
							   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
								  
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
									   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
									   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
										   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
										   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
									   }
									  
								   } else{
									   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//										   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
									   }
									   
								   }

							   }
						if (bean.getListadetallecomprobanteBean().size() != 0) {
							if (bean.getSubtotal() != s_subtotal) {
								bean.setSubtotal(s_subtotal);
							}
							if (bean.getIgv() != s_igv) {
								bean.setIgv(s_igv);
							}
						}
						}
							R_IGV=R_IGV+bean.getIgv();
							R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
					   }
				   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
				   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
				   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
				   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
				   sumadetraccion=FuncionesHelper.redondearNum(sumadetraccion,2);
				   
					//validación de los totales 05-03-2019
//					Boolean validacion=false;
//				    while(!validacion){		    	
//				    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//				    	if(!validacion){
//				    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//				    	}
//						
//					}
				   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
				   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
				   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
				   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
				   parameters.put("SUMA_DETRACCION",FuncionesHelper.redondearNum(sumadetraccion,2));
				   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
				   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+FuncionesHelper.fechaToString(new Date()));

			   }else if(tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)){
				   reportPath=  ConstantesReporteSGI.REP_AUTODETRACCION;;
				   nombrearchivo="Reporte_Pago_Autodetracciones_";
				   if (desdeDetraccion!=null) {
					   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeAutoDetraccion));
				   }else {
					   
				   }
				  
				   parameters.put("FECHATIPOCAMBIO",desdeAutoDetraccion!=null? FuncionesHelper.fechaToString(desdeAutoDetraccion):"");
				   parameters.put("FECHALIQUIDACION",desdeAutoDetraccion!=null? FuncionesHelper.fechaToString(desdeAutoDetraccion):"");
				   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   //parameters.put("COBRADOR",selectedCobrador.getDescripcion());
				   parameters.put("COBRADOR","");
				   parameters.put("INICIO",desdeAutoDetraccion!=null? FuncionesHelper.fechaToString(desdeAutoDetraccion):"");
				   parameters.put("FIN",hastaAutoDetraccion!=null? FuncionesHelper.fechaToString(hastaAutoDetraccion):"");
				   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
				   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
				   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
				   parameters.put("REPORT_CONNECTION",conn);
				   
				   Double sumatotal=0.0,sumaautodetraccion=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
				   for (ComprobanteBean bean : listacomprobantespago) {
					   //System.out.println("listacomprobantespago="+listacomprobantespago);
					  //agrego a la lista
					   if(bean.getNumerooperacion()==null){
						   
						   bean.setNumerooperacion("");
						   
					   }else{
						   
						   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
					   }
					   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
					  
					    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)) {
					    	
					    	sumatotal=sumatotal+bean.getTotal();
					    	suma_mc=suma_mc+bean.getSubtotal();
					    	suma_igv=suma_igv+bean.getIgv();
					    	suma_mora=suma_mora+bean.getMora();
					    	sumaautodetraccion=sumaautodetraccion+bean.getMontoautodetraccion();
					    	
					    }else {
					    	
					    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
					 
						}
						if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
							bean.setMora(0.0);//mora para factura
							//bean.setMora(bean.getMora());
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
							bean.setMora(0.0);
							bean.setOtro(bean.getTotal());
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if (bean.getIdtipdocu().equals("08")) {
							if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
								bean.setMora(0.0);
								bean.setPenalizacion(bean.getTotal());
							}else{
								bean.setMora(bean.getTotal());
								bean.setPenalizacion(0.0);
							}
							//bean.setMora(bean.getTotal());
							bean.setSubtotal(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							bean.setIgv(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
							if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
								bean.setRec_A(bean.getTotal());
								bean.setOtro(0.0);	
								bean.setGarantia(0.0);
							}else{
								bean.setRec_A(0.0);
								bean.setGarantia(0.0);
								if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
									if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
										bean.setGarantia(bean.getTotal());
										bean.setOtro(0.0);
									}else{
									bean.setOtro(bean.getTotal());
									
									}
								}														
							}
							bean.setPenalizacion(0.0);
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//							bean.setRec_A(bean.getTotal());
//							bean.setOtro(bean.getTotal());
							bean.setSubtotal(0.0);
							//bean.setGarantia(0.0);
							bean.setMora(0.0);
							bean.setIgv(0.0);
						}else if(bean.getIdtipdocu().equals("09")) {
							    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
							if (bean.getTipodescuentonotacredito().equals("Renta")) {
								bean.setMora(0.0);
								bean.setOtro(0.0);
								bean.setRec_A(0.0);
								bean.setPenalizacion(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
								bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
								bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
								bean.setOtro(0.0);
								bean.setRec_A(0.0);
								bean.setPenalizacion(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(0.0);					
								bean.setIgv(0.0); 
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							}
							
						} else {
							//System.out.println("else9");
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							//bean.setSubtotal(0.0);
//							bean.setGarantia(bean.getTotal());
							bean.setGarantia(0.0);
							//System.out.println("garantia="+bean.getGarantia());
							//bean.setMora(0.0);
							//bean.setIgv(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							
						}
						System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
						if (sidetallecomprobantes){
							  
							   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
							   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
							   array=lista.toArray(array);
							   bean.setListadetallecomprobanteBean( Arrays.asList(array));
							 //calculo de igv y subtotal por detalle 
							   Double s_igv=0.0,s_subtotal=0.0;  
							   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
								  
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
									   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
									   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
										   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
										   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
									   }
									  
								   } else{
									   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//										   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
									   }
									   
								   }

							   }
						if (bean.getListadetallecomprobanteBean().size() != 0) {
							if (bean.getSubtotal() != s_subtotal) {
								bean.setSubtotal(s_subtotal);
							}
							if (bean.getIgv() != s_igv) {
								bean.setIgv(s_igv);
							}
						}
						}
							R_IGV=R_IGV+bean.getIgv();
							R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
					   }
				   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
				   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
				   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
				   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
				   sumaautodetraccion=FuncionesHelper.redondearNum(sumaautodetraccion,2);
				   
					//validación de los totales 05-03-2019
//					Boolean validacion=false;
//				    while(!validacion){		    	
//				    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//				    	if(!validacion){
//				    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//				    	}
//						
//					}
				   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
				   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
				   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
				   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
				   parameters.put("SUMA_AUTODETRACCION",FuncionesHelper.redondearNum(sumaautodetraccion,2));
				   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
				   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+FuncionesHelper.fechaToString(new Date()));

			   }
			  
		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
		}
	}
public void PDF_DocumentoPago(ActionEvent actionEvent){
	try {
           if(listacomprobantespago.size()>0){
			PDF_Documento(actionEvent);
           }else{
        	   addWarnMessage("Lista Vacia ", "No tiene elementos para exportar en PDF");
           }

	}catch(Exception e){
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
	}
}
public void PDF_Documento(ActionEvent actionEvent){
	     String nombre_ref="";
	try {
			
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
			    if(cp.getSianulado()) {
			        iter.remove();
			    }
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   //jessi
			   if (!sidetallecomprobantes) { 

				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
				   nombrearchivo="Liquidación";
			   }else {
				   if(selectedComprobanteTabCobrador.size()==1 ){
					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
						   nombrearchivo="ExtincióndeDeuda";
					   }else{
						   reportPath=  ConstantesReporteSGI.REP_CONSULTA_COMPROBANTE_PAGO;
						   nombrearchivo="Reporte_Documentos_De_Pago";
					   }
				   }else{
					   reportPath=  ConstantesReporteSGI.REP_CONSULTA_COMPROBANTE_PAGO;
					   nombrearchivo="Reporte_Documentos_De_Pago";
				   }
				   
			   }
			  
			   if (desdeConsulta_Compr!=null) {
				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeConsulta_Compr));
			   }else {
				   
			   }
			  
			   parameters.put("FECHATIPOCAMBIO",desdeConsulta_Compr!=null? FuncionesHelper.fechaToString(desdeConsulta_Compr):"");
			   parameters.put("FECHALIQUIDACION",desdeConsulta_Compr!=null? FuncionesHelper.fechaToString(desdeConsulta_Compr):"");
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   if(selectedSiUsuario){
				   parameters.put("USUARIOCONSULTA",usuarioSeleccionado.getNombrescompletos());
				   parameters.put("OPCIONCONSULTA", "Usuario SGI:");
				   nombre_ref=usuarioSeleccionado.getNombres()+" "+usuarioSeleccionado.getApellidopat();
			   }
			   if(selectedSiCobrador){
				   parameters.put("USUARIOCONSULTA",selectedCobrador.getDescripcion());
				   parameters.put("OPCIONCONSULTA", "Cobrador:");
				   nombre_ref=selectedCobrador.getDescripcion();
				   
			   }
			   parameters.put("INICIO",desdeConsulta_Compr!=null? FuncionesHelper.fechaToString(desdeConsulta_Compr):"");
			   parameters.put("FIN",hastaConsulta_Compr!=null? FuncionesHelper.fechaToString(hastaConsulta_Compr):"");
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   Calendar hasta = Calendar.getInstance();
				hasta.setTime(hastaConsulta_Compr);
				//hasta.add(Calendar.DAY_OF_YEAR, 1);
				
				Calendar desde = Calendar.getInstance();
				desde.setTime(desdeConsulta_Compr);
			   if(FechaUtil.getDiasDiferencia(hastaConsulta_Compr, desdeConsulta_Compr)>0){
				   if(selectedSiDocumentosAnulados){
					   parameters.put("CONDICION","DOCUMENTOS DE PAGO ANULADOS DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime())); 
				   }else{
					   parameters.put("CONDICION","DOCUMENTOS DE PAGO DEL "+FuncionesHelper.fechaToString(desde.getTime())+" AL "+FuncionesHelper.fechaToString(hasta.getTime()));
				   }				      
			   }else{
				   if(selectedSiDocumentosAnulados){
					   parameters.put("CONDICION","DOCUMENTOS DE PAGO ANULADOS DEL "+FuncionesHelper.fechaToString(desde.getTime()));
				   }else{
					   parameters.put("CONDICION","DOCUMENTOS DE PAGO DEL "+FuncionesHelper.fechaToString(desde.getTime()));
				   }
			   
			   }
			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
			   for (ComprobanteBean bean : listacomprobantespago) {
				   //System.out.println("listacomprobantespago="+listacomprobantespago);
				  //agrego a la lista
				   if(bean.getNumerooperacion()==null){
					   
					   bean.setNumerooperacion("");
					   
				   }else{
					   
					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				   }
				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				  
				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)&& !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USO_RESOLUCION_COACTIVA)) {
				    	
				    	sumatotal=sumatotal+bean.getTotal();				    	
				    	suma_mc=suma_mc+bean.getSubtotal();
				    	suma_igv=suma_igv+bean.getIgv();
				    	suma_mora=suma_mora+bean.getMora();
				    	
				    }else {
				    	
				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
				    					    	
					}
					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
						bean.setMora(0.0);//mora para factura
						//bean.setMora(bean.getMora());
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
						bean.setMora(0.0);
						bean.setOtro(bean.getTotal());
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("08")) {
						if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
							bean.setMora(0.0);
							bean.setPenalizacion(bean.getTotal());
						}else{
							bean.setMora(bean.getTotal());
							bean.setPenalizacion(0.0);
						}
						//bean.setMora(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setGarantia(0.0);
						bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
						if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
							bean.setRec_A(bean.getTotal());
							bean.setOtro(0.0);	
							bean.setGarantia(0.0);
						}else{
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
									bean.setGarantia(bean.getTotal());
									bean.setOtro(0.0);
								}else{
								bean.setOtro(bean.getTotal());
								
								}
							}														
						}
						bean.setPenalizacion(0.0);
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setRec_A(bean.getTotal());
//						bean.setOtro(bean.getTotal());
						bean.setSubtotal(0.0);
						//bean.setGarantia(0.0);
						bean.setMora(0.0);
						bean.setIgv(0.0);
					}else if(bean.getIdtipdocu().equals("09")) {
						    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito()==null? "Renta":(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito()));
						if (bean.getTipodescuentonotacredito().equals("Renta")) {
							bean.setMora(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);					
							bean.setIgv(0.0); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}
						
					} else {
						//System.out.println("else9");
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						//bean.setSubtotal(0.0);
//						bean.setGarantia(bean.getTotal());
						bean.setGarantia(0.0);
						//System.out.println("garantia="+bean.getGarantia());
						//bean.setMora(0.0);
						//bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						
					}
					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
					if (sidetallecomprobantes){
						  
						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
						   array=lista.toArray(array);
						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						 //calculo de igv y subtotal por detalle 
						   Double s_igv=0.0,s_subtotal=0.0;  
						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
							  
							   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
								   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
								   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
									   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
								   }
								  
							   } else{
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
								   }
								   
							   }

						   }
					if (bean.getListadetallecomprobanteBean().size() != 0) {
						if (bean.getSubtotal() != s_subtotal) {
							bean.setSubtotal(s_subtotal);
						}
						if (bean.getIgv() != s_igv) {
							bean.setIgv(s_igv);
						}
					}
					}
						R_IGV=R_IGV+bean.getIgv();
						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
				   }
			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
			   
				//validación de los totales 05-03-2019
//				Boolean validacion=false;
//			    while(!validacion){		    	
//			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//			    	if(!validacion){
//			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//			    	}
//					
//				}
			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
			   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
			   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+nombre_ref+"_"+FuncionesHelper.fechaToString(new Date()));

		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
		}
	}
public void PDF_Usuario(ActionEvent actionEvent){
	
	try {
			
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
			    if(cp.getSianulado()) {
			        iter.remove();
			    }
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   //jessi
			   if (!sidetallecomprobantes) { 

				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
				   nombrearchivo="Liquidación";
			   }else {
				   if(selectedComprobanteTabCobrador.size()==1 ){
					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
						   nombrearchivo="ExtincióndeDeuda";
					   }else{
						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
						   nombrearchivo="LiquidaciónDetallada";
					   }
				   }else{
					   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
					   nombrearchivo="LiquidaciónDetallada";
				   }
				   
			   }
			  
			   if (desdeCobrador!=null) {
				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeCobrador));
			   }else {
				   
			   }
			  
			   parameters.put("FECHATIPOCAMBIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FECHALIQUIDACION",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("COBRADOR",selectedCobrador.getDescripcion());
			   parameters.put("INICIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
			   parameters.put("FIN",hastaCobrador!=null? FuncionesHelper.fechaToString(hastaCobrador):"");
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   
			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
			   for (ComprobanteBean bean : listacomprobantespago) {
				   //System.out.println("listacomprobantespago="+listacomprobantespago);
				  //agrego a la lista
				   if(bean.getNumerooperacion()==null){
					   
					   bean.setNumerooperacion("");
					   
				   }else{
					   
					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				   }
				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				  
				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)&& !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USO_RESOLUCION_COACTIVA)) {
				    	
				    	sumatotal=sumatotal+bean.getTotal();				    	
				    	suma_mc=suma_mc+bean.getSubtotal();
				    	suma_igv=suma_igv+bean.getIgv();
				    	suma_mora=suma_mora+bean.getMora();
				    	
				    }else {
				    	
				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
				    					    	
					}
					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
						bean.setMora(0.0);//mora para factura
						//bean.setMora(bean.getMora());
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
						bean.setMora(0.0);
						bean.setOtro(bean.getTotal());
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("08")) {
						if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
							bean.setMora(0.0);
							bean.setPenalizacion(bean.getTotal());
						}else{
							bean.setMora(bean.getTotal());
							bean.setPenalizacion(0.0);
						}
						//bean.setMora(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setGarantia(0.0);
						bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
						if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
							bean.setRec_A(bean.getTotal());
							bean.setOtro(0.0);	
							bean.setGarantia(0.0);
						}else{
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
									bean.setGarantia(bean.getTotal());
									bean.setOtro(0.0);
								}else{
								bean.setOtro(bean.getTotal());
								
								}
							}														
						}
						bean.setPenalizacion(0.0);
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setRec_A(bean.getTotal());
//						bean.setOtro(bean.getTotal());
						bean.setSubtotal(0.0);
						//bean.setGarantia(0.0);
						bean.setMora(0.0);
						bean.setIgv(0.0);
					}else if(bean.getIdtipdocu().equals("09")) {
						    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
						if (bean.getTipodescuentonotacredito().equals("Renta")) {
							bean.setMora(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setPenalizacion(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);					
							bean.setIgv(0.0); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						}
						
					} else {
						//System.out.println("else9");
						bean.setOtro(0.0);
						bean.setRec_A(0.0);
						bean.setPenalizacion(0.0);
						//bean.setSubtotal(0.0);
//						bean.setGarantia(bean.getTotal());
						bean.setGarantia(0.0);
						//System.out.println("garantia="+bean.getGarantia());
						//bean.setMora(0.0);
						//bean.setIgv(0.0);
						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
						
					}
					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
					if (sidetallecomprobantes){
						  
						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
						   array=lista.toArray(array);
						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						 //calculo de igv y subtotal por detalle 
						   Double s_igv=0.0,s_subtotal=0.0;  
						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
							  
							   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
								   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
								   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
									   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
								   }
								  
							   } else{
								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
//									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
								   }
								   
							   }

						   }
					if (bean.getListadetallecomprobanteBean().size() != 0) {
						if (bean.getSubtotal() != s_subtotal) {
							bean.setSubtotal(s_subtotal);
						}
						if (bean.getIgv() != s_igv) {
							bean.setIgv(s_igv);
						}
					}
					}
						R_IGV=R_IGV+bean.getIgv();
						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
				   }
			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
			   
				//validación de los totales 05-03-2019
//				Boolean validacion=false;
//			    while(!validacion){		    	
//			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//			    	if(!validacion){
//			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//			    	}
//					
//				}
			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
			   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
			   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+selectedCobrador.getDescripcion()+"_"+FuncionesHelper.fechaToString(new Date()));

		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
		}
	}
public void PDFValidar(ActionEvent actionEvent){
  System.out.println("Metodo PDFVALIDAR");
	try{
		if(comprobantepagoExt!=null){
			System.out.println(" COMPROBANTE ES NULL");
			PDFDoc(actionEvent);
		}else{
			addWarnMessage("Seleccionar item", "Debe seleccionar  un elemento de la lista  para crear el reporte");
		}
	} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
//		Logger.error(e.getMessage(), e);
	}
	
}

public void PDFDoc(ActionEvent actionEvent){
	
	try {
		    System.out.println("Metodo PDFDoc");
			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
			while(iter.hasNext()) {
				ComprobanteBean cp = iter.next();
			    if(cp.getSianulado()) {
			        iter.remove();
			    }
			}
				
			   Double tipcambio=0.0;
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   if (sidetallecomprobantes) { 

				   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADM_INDIVIDUAL;
				   nombrearchivo="ExtincióndeDeudaIndividual";
			   }else{
				   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADM_INDIVIDUAL;
				   nombrearchivo="ExtincióndeDeudaIndividual";
			   }

			  
			   if (desdeDocumento!=null) {
				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeDocumento));
			   }else {
				   
			   }
			  
			   parameters.put("FECHATIPOCAMBIO",desdeDocumento!=null? FuncionesHelper.fechaToString(desdeDocumento):"");
			   parameters.put("FECHALIQUIDACION",desdeDocumento!=null? FuncionesHelper.fechaToString(desdeDocumento):"");
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   parameters.put("UPA",comprobantepagoExt.getClaveUpa());//PARA MODIFICACION TIPO CAMBIO
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("COBRADOR","");
			   parameters.put("INICIO",desdeDocumento!=null? FuncionesHelper.fechaToString(desdeDocumento):"");
			   parameters.put("FIN",hastaDocumento!=null? FuncionesHelper.fechaToString(hastaDocumento):"");
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   
			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
			   List<ComprobanteBean> listacomprobantespagoDoc= new ArrayList<ComprobanteBean>();
			   for (int i=0;i< listacomprobantespago.size();i++) {
				      if(listacomprobantespago.get(i).getIdcomprobante()==comprobantepagoExt.getIdcomprobante()){
				    	  listacomprobantespagoDoc.add(listacomprobantespago.get(i));
				      }
			   }
			   for (ComprobanteBean bean : listacomprobantespagoDoc) {
				  
				   //System.out.println("listacomprobantespago="+listacomprobantespago);
				  
				   if(bean.getNumerooperacion()==null){
					   
					   bean.setNumerooperacion("");
					   
				   }else{
					   
					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				   }
				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
				  
				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)) {
				    	
				    	sumatotal=sumatotal+bean.getTotal();
				    	suma_mc=suma_mc+bean.getSubtotal();
				    	suma_igv=suma_igv+bean.getIgv();
				    	suma_mora=suma_mora+bean.getMora();
				    	
				    }else {
				    	
				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
				 
					}
					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08"))) {
						bean.setMora(0.0);
						bean.setOtro(0.0);
						bean.setGarantia(0.0);
						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || bean.getIdtipmovimiento().equals("06") || bean.getIdtipmovimiento().equals("07") )) {						
						bean.setMora(0.0);
						bean.setOtro(bean.getTotal());
						bean.setGarantia(0.0);
						bean.setSubtotal(0.0);
					}else if (bean.getIdtipdocu().equals("08")) {
						bean.setMora(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setOtro(0.0);
						bean.setGarantia(0.0);
						bean.setIgv(0.0);
					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
						bean.setOtro(bean.getTotal());
						bean.setSubtotal(0.0);
						bean.setGarantia(0.0);
						bean.setMora(0.0);
						bean.setIgv(0.0);
					}else if(bean.getIdtipdocu().equals("09")) {
						if (bean.getTipodescuentonotacredito().equals("Renta")) {
							bean.setMora(0.0);
							bean.setOtro(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
							bean.setOtro(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);					
							bean.setIgv(0.0); 
						}
						
					} else {
						//System.out.println("else9");
						bean.setOtro(0.0);
						//bean.setSubtotal(0.0);
//						bean.setGarantia(bean.getTotal());
						bean.setGarantia(0.0);
						//System.out.println("garantia="+bean.getGarantia());
						//bean.setMora(0.0);
						//bean.setIgv(0.0);
						
					}
					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
					if (sidetallecomprobantes){
						  //System.out.println("if10");
						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
						   array=lista.toArray(array);
						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						 //calculo de igv y subtotal por detalle 
						   Double s_igv=0.0,s_subtotal=0.0 ;
						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
							  
							   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
							   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
							   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
							   							   
						   }
						   parameters.put("OBSERVACION_DOC",bean.getObservacion());
						   if(bean.getSubtotal()!=s_subtotal){
							   bean.setSubtotal(s_subtotal);
						   }
						   if(bean.getIgv()!=s_igv){
							   bean.setIgv(s_igv);
						   }
					}
						R_IGV=R_IGV+bean.getIgv();
						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
				   }
			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
			   
				//validación de los totales 05-03-2019
//				Boolean validacion=false;
//			    while(!validacion){		    	
//			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
//			    	if(!validacion){
//			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
//			    	}
//					
//				}
			   
			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
			   parameters.put("SUMA_TOTAL",sumatotal);
			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespagoDoc,nombrearchivo+"_"+""+"_"+FuncionesHelper.fechaToString(new Date()));

	} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		e.printStackTrace();
//		Logger.error(e.getMessage(), e);
	}
	}
public StreamedContent PDFRecibo()throws Exception{
	StreamedContent stream=null;
	try {
		
		listaArchivosReferencia=administrarArchivoService.obtenerArchivosReferencia(comprobantepago.getIdcomprobante(),Constantes.COMPROBANTEPAGO);
		
		for(ArchivoAdjuntoBean archivo:listaArchivosReferencia ){
			stream=archivoMB.descargarAdjunto(archivo,Constantes.DIR_SGI_RECIBO_CAJA);
			break;		
		}
		
	} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		Logger.error(e.getMessage(), e);
	}
	return stream;
}
public void PDFRecibos(ActionEvent actionEvent){
	
	try {
			
		    Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   reportPath=  ConstantesReporteSGI.REP_RECIBO_CAJA_SGI;
			   nombrearchivo="Recibo_de_caja_";
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);
			   List<ComprobanteBean> comp= new ArrayList<>();
			   comp.add(comprobantepago);
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, comp,nombrearchivo+FuncionesHelper.fechaToString(new Date()));
//			Iterator<ComprobanteBean> iter = listacomprobantespago.iterator();
//			while(iter.hasNext()) {
//				ComprobanteBean cp = iter.next();
//				if(!selectedSiAnuladoTabCobrador){
//			    if(cp.getSianulado()) { //no considerar anulados
//			        iter.remove();
//			        
//			    }}
//				if(!selectedSiRechazadoTabCobrador){
//			    if(cp.getEstado()!=null){
//			    	if(cp.getEstado().equals(Constante.ESTADO_REC)){// no considerar rechazados
//			    		iter.remove();
//			    	}
//			    }
//				}
//			}
//				
//			   Double tipcambio=0.0;
//			   //jessi
//			   if (!sidetallecomprobantes) { 
//
//				   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR;
//				   nombrearchivo="Liquidación";
//			   }else {
//				   if(selectedComprobanteTabCobrador.size()==1 ){
//					   if(selectedComprobanteTabCobrador.get(0).equals("DA")){
//						   reportPath=  ConstantesReporteSGI.REP_EXTINCION_DE_DEUDA_DOC_ADMINISTRATIVO;;
//						   nombrearchivo="ExtincióndeDeuda";
//					   }else{
//						   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
//						   nombrearchivo="LiquidaciónDetallada";
//					   }
//				   }else{
//					   reportPath=  ConstantesReporteSGI.REP_LIQUIDACION_COBRO_COBRADOR_DETALLADO;;
//					   nombrearchivo="LiquidaciónDetallada";
//				   }
//				   
//			   }
//			  
//			   if (desdeCobrador!=null) {
//				   tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(desdeCobrador));
//			   }else {
//				   
//			   }
//			  
//			   parameters.put("FECHATIPOCAMBIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
//			   parameters.put("FECHALIQUIDACION",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
//			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
//			   
//			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//			   parameters.put("COBRADOR",selectedCobrador.getDescripcion());
//			   parameters.put("INICIO",desdeCobrador!=null? FuncionesHelper.fechaToString(desdeCobrador):"");
//			   parameters.put("FIN",hastaCobrador!=null? FuncionesHelper.fechaToString(hastaCobrador):"");
//			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
//			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
//			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
//			   parameters.put("REPORT_CONNECTION",conn);			   
//			   Double sumatotal=0.0,sumaPagousoFianzaGarantia=0.0,suma_mc=0.0,suma_igv=0.0,suma_mora=0.0,R_IGV=0.0,R_SUBTOTAL=0.0;
//			   for (ComprobanteBean bean : listacomprobantespago) {
//				   //System.out.println("listacomprobantespago="+listacomprobantespago);
//				  //agrego a la lista
//				   if(bean.getNumerooperacion()==null){
//					   
//					   bean.setNumerooperacion("");
//					   
//				   }else{
//					   
//					   bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
//				   }
//				   //bean.setNumerooperacion(bean.getNumerooperacion().equals("null-null")? "":bean.getNumerooperacion());
//				  
//				    if (!bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) && !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USOGARANTIA)&& !bean.getIdtippago().equals(Constantes.TIPOPAGO_ID_USO_RESOLUCION_COACTIVA)) {
//				    	
//				    	sumatotal=sumatotal+bean.getTotal();				    	
//				    	suma_mc=suma_mc+bean.getSubtotal();
//				    	suma_igv=suma_igv+bean.getIgv();
//				    	suma_mora=suma_mora+bean.getMora();
//				    	
//				    }else {
//				    	
//				    	sumaPagousoFianzaGarantia=sumaPagousoFianzaGarantia+bean.getTotal();
//				    					    	
//					}
//					if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
//						bean.setMora(0.0);//mora para factura
//						//bean.setMora(bean.getMora());
//						bean.setOtro(0.0);
//						bean.setRec_A(0.0);
//						bean.setPenalizacion(0.0);
//						bean.setGarantia(0.0);
//						bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
//						bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
//						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));	
//						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//					}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {						
//						bean.setMora(0.0);
//						bean.setOtro(bean.getTotal());
//						bean.setRec_A(0.0);
//						bean.setPenalizacion(0.0);
//						bean.setGarantia(0.0);
//						bean.setSubtotal(0.0);
//						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//					}else if (bean.getIdtipdocu().equals("08")) {
//						if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
//							bean.setMora(0.0);
//							bean.setPenalizacion(bean.getTotal());
//						}else{
//							bean.setMora(bean.getTotal());
//							bean.setPenalizacion(0.0);
//						}
//						//bean.setMora(bean.getTotal());
//						bean.setSubtotal(0.0);
//						bean.setOtro(0.0);
//						bean.setRec_A(0.0);
//						bean.setGarantia(0.0);
//						bean.setIgv(0.0);
//						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//					}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
//						if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
//							bean.setRec_A(bean.getTotal());
//							bean.setOtro(0.0);	
//							bean.setGarantia(0.0);
//						}else{
//							bean.setRec_A(0.0);
//							bean.setGarantia(0.0);
//							if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
//								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_P_GARANTIA)){
//									bean.setGarantia(bean.getTotal());
//									bean.setOtro(0.0);
//								}else{
//								bean.setOtro(bean.getTotal());
//								
//								}
//							}														
//						}
//						bean.setPenalizacion(0.0);
//						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
////						bean.setRec_A(bean.getTotal());
////						bean.setOtro(bean.getTotal());
//						bean.setSubtotal(0.0);
//						//bean.setGarantia(0.0);
//						bean.setMora(0.0);
//						bean.setIgv(0.0);
//					}else if(bean.getIdtipdocu().equals("09")) {
//						    bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito());
//						if (bean.getTipodescuentonotacredito().equals("Renta")) {
//							bean.setMora(0.0);
//							bean.setOtro(0.0);
//							bean.setRec_A(0.0);
//							bean.setPenalizacion(0.0);
//							bean.setGarantia(0.0);
//							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
//							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
//							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//						}else if(bean.getTipodescuentonotacredito().equals("Mora")) {
//							bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
//							bean.setOtro(0.0);
//							bean.setRec_A(0.0);
//							bean.setPenalizacion(0.0);
//							bean.setGarantia(0.0);
//							bean.setSubtotal(0.0);					
//							bean.setIgv(0.0); 
//							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//						}
//						
//					} else {
//						//System.out.println("else9");
//						bean.setOtro(0.0);
//						bean.setRec_A(0.0);
//						bean.setPenalizacion(0.0);
//						//bean.setSubtotal(0.0);
////						bean.setGarantia(bean.getTotal());
//						bean.setGarantia(0.0);
//						//System.out.println("garantia="+bean.getGarantia());
//						//bean.setMora(0.0);
//						//bean.setIgv(0.0);
//						bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
//						bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
//						
//					}
//					System.out.println("sidetallecomprobantes="+sidetallecomprobantes);
//					if (sidetallecomprobantes){
//						  
//						   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
//						   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
//						   array=lista.toArray(array);
//						   bean.setListadetallecomprobanteBean( Arrays.asList(array));
//						 //calculo de igv y subtotal por detalle 
//						   Double s_igv=0.0,s_subtotal=0.0;  
//						   for(int i=0;i<bean.getListadetallecomprobanteBean().size();i++){
//							  
//							   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==0 ){//renta 
//								   //System.out.println("sumatotal="+bean.getListadetallecomprobanteBean().get(i).getTotal());
//								   if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
//									   s_igv=s_igv+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18)*0.18,2);
//									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()/1.18),2);
//								   }
//								  
//							   } else{
//								   if(bean.getListadetallecomprobanteBean().get(i).getIndicador()==1 ){//arbitrio
////									   s_subtotal=s_subtotal+FuncionesHelper.redondearNum((bean.getListadetallecomprobanteBean().get(i).getTotal()),2);
//								   }
//								   
//							   }
//
//						   }
//					if (bean.getListadetallecomprobanteBean().size() != 0) {
//						if (bean.getSubtotal() != s_subtotal) {
//							bean.setSubtotal(s_subtotal);
//						}
//						if (bean.getIgv() != s_igv) {
//							bean.setIgv(s_igv);
//						}
//					}
//					}
//						R_IGV=R_IGV+bean.getIgv();
//						R_SUBTOTAL=R_SUBTOTAL+bean.getSubtotal();
//				   }
//			   sumatotal=FuncionesHelper.redondearNum(sumatotal,2);
//			   suma_mc=FuncionesHelper.redondearNum(suma_mc,2);
//			   suma_igv=FuncionesHelper.redondearNum(suma_igv,2);
//			   suma_mora=FuncionesHelper.redondearNum(suma_mora,2);
//			   
//				//validación de los totales 05-03-2019
////				Boolean validacion=false;
////			    while(!validacion){		    	
////			    	validacion=validacionTotales(R_SUBTOTAL,R_IGV,sumatotal);
////			    	if(!validacion){
////			    		R_IGV=FuncionesHelper.redondearNum((sumatotal-R_SUBTOTAL),2); 
////			    	}
////					
////				}
//			   parameters.put("SUMA_IGV",FuncionesHelper.redondearNum(R_IGV,2));
//			   parameters.put("SUMA_SUBTOTAL",FuncionesHelper.redondearNum(R_SUBTOTAL,2));
//			   //parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal+suma_mora,2));
//			   parameters.put("SUMA_TOTAL",FuncionesHelper.redondearNum(sumatotal,2));
//			   parameters.put("SUMA_PAGOUSO_FIANZA_GARANTIA",sumaPagousoFianzaGarantia);
//			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listacomprobantespago,nombrearchivo+"_"+selectedCobrador.getDescripcion()+"_"+FuncionesHelper.fechaToString(new Date()));

		} catch (Exception e) {
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		Logger.error(e.getMessage(), e);
		}
	}
public static  Boolean validacionTotales(Double SubTotal,Double igv,Double total){

	if(FuncionesHelper.redondearNum((total-SubTotal),2)!=igv){
		return false;
	}else{
		return true;
	}
}
public void Excel_Validacion(ActionEvent actionEvent) throws JRException, IOException
{
	try{
		if(listacomprobantespago.size()>0){
		renderedReporteDocumento=true;
		Excel(actionEvent);
		}else {
			addWarnMessage("Lista Vacia ", "No tiene elementos para exportar en EXCEL");
			
		}
	}catch(Exception e){
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		e.printStackTrace();
	}
}
public void Excel(ActionEvent actionEvent) throws JRException, IOException 
{

				   for (ComprobanteBean bean : listacomprobantespago) 
				   {
					   if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08")|| bean.getIdtipmovimiento().equals("06"))) {
							if(bean.getMora()!=null){
								bean.setMora(FuncionesHelper.redondear(bean.getMora(),2));	
							}else{
						    bean.setMora(0.0);}
							bean.setPenalizacion(0.0);
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setGarantia(0.0);
							bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
							bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));
						}else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("05") || /**bean.getIdtipmovimiento().equals("06") || */bean.getIdtipmovimiento().equals("07") )) {
							bean.setMora(0.0);
							bean.setPenalizacion(0.0);
							bean.setRec_A(0.0);
							bean.setOtro(bean.getTotal());
							bean.setGarantia(0.0);
							bean.setSubtotal(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));
						}else if (bean.getIdtipdocu().equals("08")) {
							if(bean.getIdtipconcepto().equals("17")){
								bean.setMora(0.0);
								bean.setPenalizacion(bean.getTotal());
							}else{
							bean.setMora(bean.getTotal());
							bean.setPenalizacion(0.0);
							}
							bean.setSubtotal(0.0);
							bean.setRec_A(0.0);
							bean.setOtro(0.0);
							bean.setGarantia(0.0);
							bean.setIgv(0.0);
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));
						}else if (bean.getIdtipdocu().equals("04") && !bean.getIdtipmovimiento().equals("03")) {
							if(bean.getIdtipconcepto().equalsIgnoreCase((Constante.TIPO_CONCEPTO_REC_A))){
								bean.setRec_A(bean.getTotal());
								bean.setOtro(0.0);
							}else{
								bean.setRec_A(0.0);
								if(!bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_CARTA_FIANZA)){
									bean.setOtro(bean.getTotal());
								}
							}
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));
//							bean.setOtro(bean.getTotal());
							bean.setSubtotal(0.0);
							bean.setGarantia(0.0);
							bean.setMora(0.0);
							bean.setPenalizacion(0.0);
							bean.setIgv(0.0);
						}else if(bean.getIdtipdocu().equals("09")) {
							 System.out.println("nroserie"+bean.getNrocomprobante());
							 
							 bean.setTipodescuentonotacredito(bean.getTipodescuentonotacredito()==null? "Renta":(bean.getTipodescuentonotacredito().length()>4?  "Renta":bean.getTipodescuentonotacredito()));
							if (bean.getTipodescuentonotacredito().equals("Renta")) {
								bean.setMora(0.0);
								bean.setPenalizacion(0.0);
								bean.setOtro(0.0);
								bean.setGarantia(0.0);
								bean.setRec_A(0.0);
								bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));					
								bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));

							}else if(bean.getTipodescuentonotacredito().substring(0,4).equals("Mora")) {
								bean.setRec_A(0.0);
								bean.setMora(FuncionesHelper.redondear(bean.getTotal(), 2));
								bean.setPenalizacion(0.0);
								bean.setOtro(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(0.0);					
								bean.setIgv(0.0); 
								bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
								bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
								bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));

							}
							
						} else {
							if(bean.getIdtipdocu().equals("DA")){
								if(bean.getMora()!= null){
								bean.setMora(FuncionesHelper.redondear(bean.getMora(),2));}
								else{
									bean.setMora(0.0);	
								}
								bean.setPenalizacion(0.0);
								bean.setOtro(0.0);
								bean.setRec_A(0.0);
								bean.setGarantia(0.0);
								bean.setSubtotal(FuncionesHelper.redondear(bean.getTotal()/1.18, 2));
								bean.setIgv(FuncionesHelper.redondear((bean.getTotal()/1.18)*0.18, 2)); 								
							}
							else{
							bean.setOtro(0.0);
							bean.setRec_A(0.0);
							bean.setSubtotal(0.0);
							bean.setGarantia(bean.getTotal());
							bean.setMora(0.0);
							bean.setPenalizacion(0.0);
							bean.setIgv(0.0);}
							
							bean.setConcepto(Constante.MAP_TIPO_CONCEPTO_SGI.get(bean.getIdtipconcepto()));
							bean.setDocumento(Constante.MAP_TIPO_DOCUMENTO_SGI.get(bean.getIdtipdocu()));
							bean.setTip_pago(Constante.MAP_TIPO_PAGO_SGI.get(bean.getIdtippago()));
						}

						
						if (sidetallecomprobantes) 
						{
							   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));
							   DetallecomprobanteBean [] array = new DetallecomprobanteBean[lista.size()];
							   array=lista.toArray(array);
							   bean.setListadetallecomprobanteBean( Arrays.asList(array));
						}

						
					}
               if(!renderedReporteDocumento){
			   XLS_Comprobantepago(listacomprobantespago);
               }else{
            	   XLS_Documento(listacomprobantespago);
            	   renderedReporteDocumento=false;
               }

}

public void XLS_Comprobantepago(List<ComprobanteBean> lista) throws FileNotFoundException 
{
	
	
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Comprobantes");
        
//        SXSSFWorkbook workbook= new SXSSFWorkbook(); //para datos grandes consume la memoria..
//        SXSSFSheet sheet= workbook.createSheet("Comprobantes");

        
        sheet.setColumnWidth(0, 2500); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(18, 4000);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        
//        String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.D","Nº SERIE","Nº DOC","Nº OPE","SUBTOTAL","I.G.V","MORA","GARANTIA","OTRO","TOTAL COMPROBANTE","F. CANC","CARTERA","COMP. REF","MES","ANIO","TOTAL"};
//        String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.D","T.CONCEPTO","Nº SERIE","Nº DOC","SUBTOTAL","I.G.V","MORA","GARANTIA","OTRO","TOTAL COMPROBANTE","F. EM","F. CANC","CARTERA","COMP. REF","MES","ANIO","TOTAL"};
//       String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.CONCEPTO","T.D","Nº SERIE","Nº DOC","SUBTOTAL","I.G.V","MORA","PENALIDAD","TOTAL","F. CANC","NOMBRE COBRADOR","MES","ANIO","TOTAL COMPROBANTE","F. EM","COMP. REF","F. EM REF","T.PAGO","ESTADO"};
        String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.D","T.CONCEPTO","Nº SERIE","Nº DOC","Nº OPERACIÓN","SUBTOTAL","I.G.V","MORA","PENALIDAD","TOTAL","F. CANC","Nº CARTERA","NOMBRE COBRADOR","MES","ANIO","TOTAL COMPROBANTE","F. EM","COMP. REF","F. EM REF","T.PAGO","ESTADO"};
        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        
        
        
        /**Insercion de datos **/
        
        
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
        	
            ComprobanteBean bean= new ComprobanteBean();
            bean= lista.get(i);
           
            
            if (lista.get(i).getListadetallecomprobanteBean().size()>0) {
            	for (DetallecomprobanteBean detalleBean : lista.get(i).getListadetallecomprobanteBean()) {
            		Row row = sheet.createRow(rownum++);
                    for (int j = 0; j < 24; j++) {
                    	Cell cell = row.createCell(j);
                    	if (j==0)
                    		cell.setCellValue((String)bean.getClaveUpa());
                    	 else if (j==1)
                    		 cell.setCellValue((String)bean.getDireccion());
                    		 else if (j==2)
                    			 cell.setCellValue((String)bean.getNombreInquilino());
                    			 else if (j==3)
//                    				 cell.setCellValue((String)bean.getIdtipdocu());
                    				 cell.setCellValue((String)bean.getDocumento());//Documento                  				 
                    			 else if (j==4)                				 
                    				 cell.setCellValue((String)bean.getConcepto());//Concepto
    	                			 else if (j==5)   	                				 
    	                				 cell.setCellValue((String)bean.getNroserie());//SERIE
    		                			 else if (j==6)
    		                				 cell.setCellValue((String)bean.getNrocomprobante());// NUMERO DE DOCUMENTO
//    		                			 else if (j==6)
//        		                				 cell.setCellValue(bean.getNumerooperacion()!=null? (String)bean.getNumerooperacion():"");
    		                			 	else if (j==7)
    		                				 cell.setCellValue((String)bean.getNumerooperacion()!=null? bean.getNumerooperacion() :"-");// NUMERO DE DOCUMENTO
    			                			 else if (j==8)
    			                				     if(detalleBean.getId_maedcondiciondetalle()!=null){
    			                				    	 cell.setCellValue("");//SUBTOTAL
    			                				     }else{
    			                				    	 cell.setCellValue((Double)bean.getSubtotal());//SUBTOTAL
    			                				     }
    			                					 
    			                					 else if (j==9)
    			                						 if(detalleBean.getId_maedcondiciondetalle()!=null){
    			                							 cell.setCellValue("");//IGV
    			                						 }else{
    			                							 cell.setCellValue((Double)bean.getIgv());//IGV
    			                						 }
    				                					 else if (j==10)
    				                						 if(detalleBean.getId_maedcondiciondetalle()!=null){
    				                							 cell.setCellValue("");
    				                						 }else{
    				                							 cell.setCellValue((Double)bean.getMora()); //MORA
    				                						 }
    				                					     else if(j==11)
    				                							 cell.setCellValue((Double)bean.getPenalizacion()); //PENALIDAD
    				                						 
//    				                						 else if (j==10) //GARANTIA
//    				                							 {
//    				                							   if(bean.getGarantia()==null){
//    				                								   bean.setGarantia(0.0);
//    				                							   }													
//    				                							 cell.setCellValue((Double)bean.getGarantia());
//    				                						    }
//    					                						 else if (j==11){//OTRO
//    					                							 if(bean.getOtro()==null){
//      				                								   bean.setOtro(0.0);
//      				                							   }
//    					                							 cell.setCellValue((Double)bean.getOtro());}
    						                						 else if (j==12)
    						                							 //cell.setCellValue((Double)bean.getTotal());
    						                							 if(detalleBean.getId_maedcondiciondetalle()!=null){
    						                								 cell.setCellValue("");
    						                							 }else{
    					           											 cell.setCellValue((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& !bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))  ?(Double)detalleBean.getMora():((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))?(Double)detalleBean.getPenalidad():(Double)detalleBean.getTotal()));
		
    						                							 }

    						                							// cell.setCellValue((Double)detalleBean.getTotal());    						               							 
    						                						     else if(j==13)
							                							      cell.setCellValue(bean.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):"");
    						                						     	else if (j==14)
							                						    	 cell.setCellValue((String)bean.getNrocartera()!=null ? bean.getNrocartera():"");	
    							                						     else if (j==15)
    							                						    	 cell.setCellValue((String)bean.getNombrecobrador());
        						                						         
    								                						      else if (j==16)
    								                							       //cell.setCellValue((String)bean.getNrocartera());
    								                						    	  //cell.setCellValue(bean.getFecEmision()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmision()):"");
    								                						    	  if(detalleBean.getId_maedcondiciondetalle()!=null){
    								                						    		  cell.setCellValue("Ref. "+(String)detalleBean.getMes());
    								                						    	  }else{
//    								                						    		  if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_RECONO_DEUDA)){
//    								                						    			  
//    								                						    		  }else{
//    								                						    			  
//    								                						    		  }
    								                						    		  cell.setCellValue((String)detalleBean.getMes());
    								                						    	  }  
    									                						       else if (j==17)
    									                							        //cell.setCellValue((String)bean.getNrocomprobanteref());
    									                						    	   if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_RECONO_DEUDA) && detalleBean.getId_maedcondiciondetalle()==null){
    									                						    		   cell.setCellValue("");
    									                						    	   }else{
    									                						    		   cell.setCellValue((Integer)detalleBean.getAnio());
    									                						    	   }
                    																		
   	 									                						            else if (j==18)
   	 									                							             //cell.setCellValue((String)detalleBean.getMes());
                    																			// cell.setCellValue((Integer)detalleBean.getAnio());
   	 									                						                 if(detalleBean.getId_maedcondiciondetalle()!=null){
   	 									                						                	cell.setCellValue("");
   	 									                						                 }else{
   	 									                						                	 cell.setCellValue((Double)bean.getTotal());
   	 									                						                 }
   		 									                						             else if (j==19)
   		 									                							              //cell.setCellValue((Integer)detalleBean.getAnio());
				    	  																			  cell.setCellValue(bean.getFecEmision()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmision()):"");
   			 									                						              else if (j==20){
   			 									                							           //cell.setCellValue((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& !bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))  ?(Double)detalleBean.getMora():(Double)detalleBean.getTotal());
   			 									                						            	cell.setCellValue((String)bean.getNrocomprobanteref());}
   			 									                						              	else if (j==21)
   			 									                						              			cell.setCellValue(bean.getFecEmisionref()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmisionref()):"");
   			 									                						              			else if (j==22){
   			 									                						              					//cell.setCellValue((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& !bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))  ?(Double)detalleBean.getMora():(Double)detalleBean.getTotal());
   			 									                						              					cell.setCellValue((String)bean.getTip_pago());
   			 									                							           
   			 									                						              				}else if(j==23){
   			 									                						              						cell.setCellValue(bean.getEstado()==null? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_ACT)? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_CONDICION_ANU)? Constante.ESTADO_CONDICION_ANU:Constante.ESTADO_CONDICION_REC)));
   			 									                						              					}
                    							}
            	}
			} else {
				Row row = sheet.createRow(rownum++);
                for (int j = 0; j < 24; j++) {//NUMERO DE COLUMNAS
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClaveUpa());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
                		 else if (j==2)
                			 cell.setCellValue((String)bean.getNombreInquilino());
                			 else if (j==3)
                				 //cell.setCellValue((String)bean.getIdtipdocu());
                				  cell.setCellValue((String)bean.getDocumento());
                			   else if (j==4)
                				 //cell.setCellValue((String)bean.getConcepto());
                				cell.setCellValue((String)bean.getConcepto());
	                			 else if (j==5)
	                				 cell.setCellValue((String)bean.getNroserie());
		                			 else if (j==6)
		                				 cell.setCellValue((String)bean.getNrocomprobante());
		                			 else if (j==7)
		                				 cell.setCellValue((String)bean.getNumerooperacion()!=null? bean.getNumerooperacion() :"-");// NUMERO DE OPERACION
//		                			 else if (j==6)
//		                				 cell.setCellValue(bean.getNumerooperacion()!=null? (String)bean.getNumerooperacion():"");
                	
			                			 else if (j==8)
			                					 cell.setCellValue((Double)bean.getSubtotal());
			                					 else if (j==9)
			                						 cell.setCellValue((Double)bean.getIgv());
				                					 else if (j==10)
				                						 cell.setCellValue((Double)bean.getMora());
				                					 	 else if(j==11)
			                							   cell.setCellValue((Double)bean.getPenalizacion()); //PENALIDAD
//				                						 else if (j==10)//GARANTIA
//				                							 cell.setCellValue((Double)bean.getGarantia());
//					                						 else if (j==11)
//					                						 { if(bean.getOtro()==null){//OTRO
//					                								 bean.setOtro(0.0);
//					                							 }
//					                							 cell.setCellValue((Double)bean.getOtro());}
						                						 else if (j==12)
						                							 cell.setCellValue((Double)bean.getTotal());
						                						 else if(j==13)
					 												 cell.setCellValue(bean.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):"");
						                						 	else if (j==14)
					                						    	 cell.setCellValue((String)bean.getNrocartera()!=null ? bean.getNrocartera():"");	
						                						 	 else if (j==15)
							                							 //cell.setCellValue(bean.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):"");
                														 cell.setCellValue((String)bean.getNombrecobrador());
						                						 	   else if (j==16)
								                							 //cell.setCellValue((String)bean.getNrocartera());
                															 //cell.setCellValue((String)bean.getNombrecobrador());
                															 cell.setCellValue("");
									                						 else if (j==17)
									                							// cell.setCellValue((String)bean.getNrocomprobanteref());
                																cell.setCellValue("");
	 									                						 else if (j==18)
	 									                							// cell.setCellValue("");
                																	 cell.setCellValue((Double)bean.getTotal());
		 									                						 else if (j==19)
		 									                							 //cell.setCellValue("");
			         																	 cell.setCellValue(bean.getFecEmision()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmision()):"");
			 									                						else if (j==20){
			 									                							//cell.setCellValue((Double)bean.getTotal());
			 									                							cell.setCellValue((String)bean.getNrocomprobanteref());}
			 									                							else if (j==21)
				 									                							 //cell.setCellValue("");
					         																	 cell.setCellValue(bean.getFecEmisionref()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmisionref()):"");
			 									                									else if (j==22){
			 									                											//cell.setCellValue((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& !bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))  ?(Double)detalleBean.getMora():(Double)detalleBean.getTotal());
			 									                											cell.setCellValue((String)bean.getTip_pago());
			 									                						}
			 									                						else if(j==23){
									                						            		cell.setCellValue(bean.getEstado()==null? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_ACT)? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_CONDICION_ANU)? Constante.ESTADO_CONDICION_ANU:Constante.ESTADO_CONDICION_REC)));
									                						            	}
                							}

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
        	
			report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLS, "ReporteComprobante"+FuncionesHelper.fechaToString(new Date())+".xls");
			//report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLSX, "ReporteComprobante"+FuncionesHelper.fechaToString(new Date())+".XLSX");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

}
public void XLS_Documento(List<ComprobanteBean> lista) throws FileNotFoundException 
{
	
	    String nombre_ref="";
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
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        
//        String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.D","Nº SERIE","Nº DOC","Nº OPE","SUBTOTAL","I.G.V","MORA","GARANTIA","OTRO","TOTAL COMPROBANTE","F. CANC","CARTERA","COMP. REF","MES","ANIO","TOTAL"};
//        String[] headers = new String[] { "CLAVE", "DIRECCION", "INQUILINO","T.D","T.CONCEPTO","Nº SERIE","Nº DOC","SUBTOTAL","I.G.V","MORA","GARANTIA","OTRO","TOTAL COMPROBANTE","F. EM","F. CANC","CARTERA","COMP. REF","MES","ANIO","TOTAL"};
        String[] headers = new String[] {"CONDICION", "CLAVE", "DIRECCION", "INQUILINO","T.D","T.CONCEPTO","Nº SERIE","Nº DOC","SUBTOTAL","I.G.V","MORA","TOTAL COMPROBANTE","F. EM","F. CANC","NOMBRE COBRADOR","COMP. REF","MES","ANIO","TOTAL","ESTADO"};
        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        
        
        
        /**Insercion de datos **/
        
        
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
        	
            ComprobanteBean bean= new ComprobanteBean();
            bean= lista.get(i);
           
            
            if (lista.get(i).getListadetallecomprobanteBean().size()>0) {
            	for (DetallecomprobanteBean detalleBean : lista.get(i).getListadetallecomprobanteBean()) {
            		Row row = sheet.createRow(rownum++);
                    for (int j = 0; j < 20; j++) {
                    	Cell cell = row.createCell(j);
                    	if (j==0)
                    		cell.setCellValue((String)bean.getAccion_aud()==null ? Constante.ESTADO_REG:Constante.ESTADO_CONDICION_ANU);
                    	else if (j==1)
                    		cell.setCellValue((String)bean.getClaveUpa());
                    	 else if (j==2)
                    		 cell.setCellValue((String)bean.getDireccion());
                    		 else if (j==3)
                    			 cell.setCellValue((String)bean.getNombreInquilino());
                    			 else if (j==4)
//                    				 cell.setCellValue((String)bean.getIdtipdocu());
                    				 cell.setCellValue((String)bean.getDocumento());//DOCUMENTO
                    			 else if (j==5)
	                				 
	                				 cell.setCellValue((String)bean.getConcepto());//CONCEPTO
    	                			 else if (j==6)   	                				 
    	                				 cell.setCellValue((String)bean.getNroserie());
    		                			 else if (j==7)
    		                				 cell.setCellValue((String)bean.getNrocomprobante());
//    		                			 else if (j==6)
//        		                				 cell.setCellValue(bean.getNumerooperacion()!=null? (String)bean.getNumerooperacion():"");
    			                			 else if (j==8)
    			                					 cell.setCellValue((Double)bean.getSubtotal());
    			                					 else if (j==9)
    			                						 cell.setCellValue((Double)bean.getIgv());
    				                					 else if (j==10)
    				                						 cell.setCellValue((Double)bean.getMora());
//    				                						 else if (j==10) //GARANTIA
//    				                							 {
//    				                							   if(bean.getGarantia()==null){
//    				                								   bean.setGarantia(0.0);
//    				                							   }													
//    				                							 cell.setCellValue((Double)bean.getGarantia());
//    				                						    }
//    					                						 else if (j==11){//OTRO
//    					                							 if(bean.getOtro()==null){
//      				                								   bean.setOtro(0.0);
//      				                							   }
//    					                							 cell.setCellValue((Double)bean.getOtro());}
    						                						 else if (j==11)
    						                							 cell.setCellValue((Double)bean.getTotal());
    						                							// cell.setCellValue((Double)detalleBean.getTotal());    						               							 
    						                						     else if(j==12)
    						                						         cell.setCellValue(bean.getFecEmision()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmision()):"");
    							                						     else if (j==13)
    							                							      cell.setCellValue(bean.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):"");
    								                						      else if (j==14)
    								                							       //cell.setCellValue((String)bean.getNrocartera());
                    																	cell.setCellValue((String)bean.getNombrecobrador());
    									                						       else if (j==15)
    									                							        cell.setCellValue((String)bean.getNrocomprobanteref());
   	 									                						            else if (j==16)
   	 									                							             cell.setCellValue((String)detalleBean.getMes());
   		 									                						             else if (j==17)
   		 									                							              cell.setCellValue((Integer)detalleBean.getAnio());
   			 									                						              else if (j==18){
   			 									                							           cell.setCellValue((bean.getTipodescuentonotacredito()!=null && bean.getTipodescuentonotacredito().equals("Mora")) || (bean.getIdtipdocu().equals("08")&& !bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION))  ?(Double)detalleBean.getMora():(Double)detalleBean.getTotal());
   			 									                						              	}else if(j==19){
   			 									                						            		cell.setCellValue(bean.getEstado()==null? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_ACT)? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_CONDICION_ANU)? Constante.ESTADO_CONDICION_ANU:Constante.ESTADO_CONDICION_REC)));
   			 									                						            	}
                    							}
            	}
			} else {
				Row row = sheet.createRow(rownum++);
                for (int j = 0; j < 20; j++) {//NUMERO DE COLUMNAS
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getAccion_aud()==null ? Constante.ESTADO_REG:Constante.ESTADO_CONDICION_ANU);
                	else if (j==1)
                		cell.setCellValue((String)bean.getClaveUpa());
                	 else if (j==2)
                		 cell.setCellValue((String)bean.getDireccion());
                		 else if (j==3)
                			 cell.setCellValue((String)bean.getNombreInquilino());
                			 else if (j==4)
                				 //cell.setCellValue((String)bean.getIdtipdocu());
                					cell.setCellValue((String)bean.getDocumento());
                			   else if (j==5)
                				 cell.setCellValue((String)bean.getConcepto());
	                			 else if (j==6)
	                				 cell.setCellValue((String)bean.getNroserie());
		                			 else if (j==7)
		                				 cell.setCellValue((String)bean.getNrocomprobante());
//		                			 else if (j==6)
//		                				 cell.setCellValue(bean.getNumerooperacion()!=null? (String)bean.getNumerooperacion():"");
                	
			                			 else if (j==8)
			                					 cell.setCellValue((Double)bean.getSubtotal());
			                					 else if (j==9)
			                						 cell.setCellValue((Double)bean.getIgv());
				                					 else if (j==10)
				                						 cell.setCellValue((Double)bean.getMora());
//				                						 else if (j==10)//GARANTIA
//				                							 cell.setCellValue((Double)bean.getGarantia());
//					                						 else if (j==11)
//					                						 { if(bean.getOtro()==null){//OTRO
//					                								 bean.setOtro(0.0);
//					                							 }
//					                							 cell.setCellValue((Double)bean.getOtro());}
						                						 else if (j==11)
						                							 cell.setCellValue((Double)bean.getTotal());
						                						 else if(j==12)
					                						         cell.setCellValue(bean.getFecEmision()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecEmision()):"");
							                						 else if (j==13)
							                							 cell.setCellValue(bean.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):"");
								                						 else if (j==14)
								                							 //cell.setCellValue((String)bean.getNrocartera());
                															 cell.setCellValue((String)bean.getNombrecobrador());
									                						 else if (j==15)
									                							 cell.setCellValue((String)bean.getNrocomprobanteref());
	 									                						 else if (j==16)
	 									                							 cell.setCellValue("");
		 									                						 else if (j==17)
		 									                							 cell.setCellValue("");
			 									                						else if (j==18){
			 									                							cell.setCellValue((Double)bean.getTotal());
			 									                						}else if(j==19){
									                						            		cell.setCellValue(bean.getEstado()==null? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_ACT)? Constante.ESTADO_CONDICION_ACT:(bean.getEstado().equals(Constante.ESTADO_CONDICION_ANU)? Constante.ESTADO_CONDICION_ANU:Constante.ESTADO_CONDICION_REC)));
									                						            	}
                							}

			}
		}



        try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	try {
        		workbook.write(bos);
        	} finally {
        	    bos.close();
        	}
        	if(selectedSiUsuario){
				   nombre_ref=usuarioSeleccionado.getNombres()+" "+usuarioSeleccionado.getApellidopat();
			   }
			if(selectedSiCobrador){

				   nombre_ref=selectedCobrador.getDescripcion();
				   
			}
            InputStream stream;
            stream = new ByteArrayInputStream(bos.toByteArray());
       	
			report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLS, "Reporte_Documentos_De_Pago_"+nombre_ref+"_"+FuncionesHelper.fechaToString(new Date())+".XLS");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

}
/*GENERA REPORTE EN FORMATO DBF*/
public void DBF(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException
{

		Locale.setDefault (Locale.US);

		DBFField fields[] = new DBFField[16];
		fields[0] = new DBFField();
		fields[0].setName("COD_INMB");
		fields[0].setDataType( DBFField.FIELD_TYPE_C);
		fields[0].setFieldLength(11);

		fields[1] = new DBFField();
		fields[1].setName("NUM_CONTR");
		fields[1].setDataType( DBFField.FIELD_TYPE_C);
		fields[1].setFieldLength(8);

		fields[2] = new DBFField();
		fields[2].setName("INQUILINO");
		fields[2].setDataType( DBFField.FIELD_TYPE_C);
		fields[2].setFieldLength(45);

		fields[3] = new DBFField();
		fields[3].setName("NUM_RUC");
		fields[3].setDataType( DBFField.FIELD_TYPE_C);
		fields[3].setFieldLength(11);

		fields[4] = new DBFField();
		fields[4].setName("AA");
		fields[4].setDataType( DBFField.FIELD_TYPE_C);
		fields[4].setFieldLength(4);

		fields[5] = new DBFField();
		fields[5].setName("MM");
		fields[5].setDataType( DBFField.FIELD_TYPE_C);
		fields[5].setFieldLength(2);

		fields[6] = new DBFField();
		fields[6].setName("TIP_DOCU");
		fields[6].setDataType( DBFField.FIELD_TYPE_C);
		fields[6].setFieldLength(2);

		fields[7] = new DBFField();
		fields[7].setName("NRO_DOCU");
		fields[7].setDataType( DBFField.FIELD_TYPE_C);
		fields[7].setFieldLength(13);

		fields[8] = new DBFField();
		fields[8].setName("FCH_EM");
		fields[8].setDataType( DBFField.FIELD_TYPE_D);
		
		fields[9] = new DBFField();
		fields[9].setName("FCH_PAGO");
		fields[9].setDataType( DBFField.FIELD_TYPE_D);

		fields[10] = new DBFField();
		fields[10].setName("TIP_MOVI");
		fields[10].setDataType( DBFField.FIELD_TYPE_C);
		fields[10].setFieldLength(2);

		fields[11] = new DBFField();
		fields[11].setName("TIP_PAGO");
		fields[11].setDataType( DBFField.FIELD_TYPE_C);
		fields[11].setFieldLength(2);

		fields[12] = new DBFField();
		fields[12].setName("RENTA");
		fields[12].setDataType( DBFField.FIELD_TYPE_N);
		fields[12].setFieldLength(14);
		fields[12].setDecimalCount(2);

		fields[13] = new DBFField();
		fields[13].setName("IGV");
		fields[13].setDataType(DBFField.FIELD_TYPE_N);
		fields[13].setFieldLength(10);
		fields[13].setDecimalCount(2);

		fields[14] = new DBFField();
		fields[14].setName("MORA");
		fields[14].setDataType( DBFField.FIELD_TYPE_N);
		fields[14].setFieldLength(10);
		fields[14].setDecimalCount(2);

		fields[15] = new DBFField();
		fields[15].setName("COD_COB");
		fields[15].setDataType( DBFField.FIELD_TYPE_N);
		fields[15].setFieldLength(3);
		fields[15].setDecimalCount(0);

		DBFWriter dbfWriter= new DBFWriter();

		dbfWriter.setFields(fields);
		Object rowData[];

		for (ComprobanteBean bean : listacomprobantespago) {
				   List<DetallecomprobanteBean> lista= new ArrayList<DetallecomprobanteBean>(cobranzaRecaudacionService.obtenerDetalleComprobanteBean(bean.getIdcomprobante()));

				   if (lista.size()==0) {
					   rowData = new Object[16];

						rowData[0] = bean.getClaveUpa();
						rowData[1] = (bean.getNrocontrato()!=null)? bean.getNrocontrato().toUpperCase():"";

						if (bean.getIddetallecartera()!=0) {
							if (bean.getCondicion().equals("Sin Contrato")) {
								if (!bean.getSiocupante()) {
									rowData[2] = bean.getNombreocupante().toUpperCase();
									rowData[3] = bean.getDniocupante();
								 }else {

									 rowData[2] = (bean.getNombreInquilino()!=null)? bean.getNombreInquilino().toUpperCase():"";
									 rowData[3] = bean.getRuc();
									   if (bean.getIdtipopersona()==1) {
											rowData[3] = bean.getDni();
									   	}
								 }
							}else {

								 rowData[2] = (bean.getNombreInquilino()!=null)? bean.getNombreInquilino().toUpperCase():"";
								 rowData[3] = bean.getRuc();
								   if (bean.getIdtipopersona()==1) {
										rowData[3] = bean.getDni();
								   	}
							}
						} else {
							 rowData[2] = (bean.getNombreInquilino()!=null)? bean.getNombreInquilino().toUpperCase():"";
							 rowData[3] = bean.getRuc();
						}

						rowData[6] = bean.getIdtipdocu();
						rowData[7] = bean.getNroseriecomprobante();
						rowData[8] = bean.getFecEmision();
						
						rowData[9] = bean.getFecCancelacion();
						rowData[10] = bean.getIdtipmovimiento();
						rowData[11] = bean.getIdtippago();

						if (bean.getNrocartera()!=null) {
							rowData[15] = FuncionesHelper.redondear(Double.parseDouble(bean.getNrocartera()), 0);
						}else {
							rowData[15] = FuncionesHelper.redondear(Double.parseDouble("102"),0);
						}

						if (bean.getIdtipdocu().equals("08")) {
							if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){//Penalización
							     rowData[12]=FuncionesHelper.redondear(bean.getTotal(),2);
							     rowData[14] = 0.0;
							     }else{
							rowData[12] =0.0;
							rowData[14] = bean.getTotal();	
							     }
												
							rowData[13] =0.0;

						}else if (bean.getIdtipdocu().equals("04") ) {//&& !bean.getIdtipmovimiento().equals("03")
							rowData[12]=bean.getTotal();
							rowData[14]=0.0;
							rowData[13] =0.0;
						}

					   if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03") || bean.getIdtipdocu().equals("09")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08") || bean.getIdtipmovimiento().equals("09"))) {
							rowData[14]=0.0;
							rowData[12] =bean.getTotal()/1.18;
							rowData[13] =bean.getTotal()/1.18*0.18;
						}

					   rowData[4] = String.valueOf(Almanaque.getNumeroAnio(bean.getFecCancelacion()));
					   rowData[5] = String.valueOf(Almanaque.getNumeroMes(bean.getFecCancelacion())+1);

					   dbfWriter.addRecord(rowData);
				   }
				   else {

					   for (DetallecomprobanteBean detalleBean : lista) {
						   rowData = new Object[16];
							rowData[0] = bean.getClaveUpa();
							rowData[1] = bean.getNrocontrato();

							if (bean.getIddetallecartera()!=0) {
								if (bean.getCondicion().equals("Sin Contrato")) {
									if (!bean.getSiocupante()) {
										if(bean.getNombreocupante().equals("")){
											rowData[2] = bean.getNombreInquilino();
											rowData[3] = bean.getDni();
										}else{
										rowData[2] = bean.getNombreocupante();
										rowData[3] = bean.getDniocupante();
										}
									 }else {

										 rowData[2] = bean.getNombreInquilino();
										 rowData[3] = bean.getRuc();
										   if (bean.getIdtipopersona()==1) {
												rowData[3] = bean.getDni();
										   	}
									 }
								}else {

									 rowData[2] = bean.getNombreInquilino();
									 rowData[3] = bean.getRuc();
									   if (bean.getIdtipopersona()==1) {
											rowData[3] = bean.getDni();
									   	}
								}
							} else {
								 rowData[2] = bean.getNombreInquilino();
								 rowData[3] = bean.getRuc();
							}

							rowData[6] = bean.getIdtipdocu();
							rowData[7] = bean.getNroseriecomprobante(); 
							rowData[8] = bean.getFecEmision();
							
							rowData[9] = bean.getFecCancelacion();
							rowData[10] = bean.getIdtipmovimiento();
							rowData[11] = bean.getIdtippago();

							if (bean.getNrocartera()!=null && !(bean.getNrocartera().trim().length()==0)) {
								rowData[15] = FuncionesHelper.redondear(Double.parseDouble(bean.getNrocartera()), 0);
							}
							
							if (bean.getIdtipdocu().equals("08")) {
								if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_PENALIZACION)){//Penalización
								     rowData[12]=FuncionesHelper.redondear(detalleBean.getTotal(), 2);
							   		 rowData[14]=0.0;

								     }else{
								     rowData[12] =0.0;
							   		 rowData[14]=FuncionesHelper.redondear(detalleBean.getMora(),2);

								     }
							   		 //rowData[14]=FuncionesHelper.redondear(detalleBean.getMora(),2);
								     rowData[13] =0.0;
								     
						   }else  if (bean.getIdtipdocu().equals("04") ) {//&& !bean.getIdtipmovimiento().equals("03") //recibo de caja 
							     if(bean.getIdtipconcepto().equals(Constante.TIPO_CONCEPTO_REC_A)){//recuperacion de arbitrio
							     rowData[12]=FuncionesHelper.redondear(detalleBean.getTotal(), 2);
							     }else{
								rowData[12]=bean.getTotal();}
								rowData[14]=0.0;
								rowData[13] =0.0;
							}
							else if (bean.getIdtipdocu().equals("09")) {
							   
							   if(bean.getTipodescuentonotacredito()==null){
								   	
								    rowData[14]=0.0;
									rowData[12] =FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2);
									rowData[13] =FuncionesHelper.redondear(detalleBean.getTotal()-FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2),2);   
							   
							   }else if (bean.getTipodescuentonotacredito().equals("Renta")) {
								    
								    rowData[14]=0.0;
									rowData[12] =FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2);
									rowData[13] =FuncionesHelper.redondear(detalleBean.getTotal()-FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2),2);
							   } else {
								   
									rowData[14]=FuncionesHelper.redondear(detalleBean.getMora(),2);
									rowData[12] =0.0;
									rowData[13] =0.0;
							  }
						   
						   
						   }else if ((bean.getIdtipdocu().equals("01") || bean.getIdtipdocu().equals("03")) && (bean.getIdtipmovimiento().equals("01") || bean.getIdtipmovimiento().equals("02") || bean.getIdtipmovimiento().equals("03") || bean.getIdtipmovimiento().equals("04") || bean.getIdtipmovimiento().equals("08"))) {
							  //rowData[14]=0.0;//MORA
							   	rowData[14]=FuncionesHelper.redondear(detalleBean.getMora(), 2);
								rowData[12] =FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2);
								rowData[13] =FuncionesHelper.redondear(detalleBean.getTotal()-FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2),2);
							}else if(bean.getIdtipdocu().equals("DA")){
								rowData[14]=FuncionesHelper.redondear(detalleBean.getMora(), 2);
								rowData[12] =FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2);
								rowData[13] =FuncionesHelper.redondear(detalleBean.getTotal()-FuncionesHelper.redondear(detalleBean.getTotal()/1.18, 2),2);
							}

						   rowData[4] = String.valueOf(detalleBean.getAnio());
						   rowData[5] = Almanaque.mesanumero(detalleBean.getMes());

						   dbfWriter.addRecord(rowData);
					   }
				   	}
				}

	 	try {
	        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        	try {
	        		dbfWriter.write(bos);
	        	} finally {
	        	    bos.close();
	        	}
	        	
	            InputStream stream;
	            stream = new ByteArrayInputStream(bos.toByteArray());
	        	
				report= new DefaultStreamedContent(stream, Constantes.APPLICATION_DBF, "API_"+FuncionesHelper.fechaToString(new Date())+".DBF");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
}

	/********************************************************************** PESTAÑA REPORTE *******************************************************************************/

		//TAB UPA
		public void elegirReporteTabUpa(String tipoArchivo){

			if (condicionSeleccionado!=null) {
				if (tipoReporteSeleccionadoTabUpa.equals("1")) {
					generarReportePagos("canceladas y pendientes",tipoArchivo);
				}else if (tipoReporteSeleccionadoTabUpa.equals("2")) {
					generarReportePagos("canceladas",tipoArchivo);
				}else if (tipoReporteSeleccionadoTabUpa.equals("3")) {
					generarReportePagos("pendientes",tipoArchivo);
				}else if (tipoReporteSeleccionadoTabUpa.equals("4")){
					generarReportePagos("ratio",tipoArchivo);
				}else {
					generarReportePagos("ratio detalle",tipoArchivo);
				}
				
			} else {
				addWarnMessage("Seleccionar item", "Debe seleccionar al menos una condición para crear el reporte");
			}
			
				
		}
		//TAB UPA
		public void elegirReporteTabUpaNuevo(String tipoArchivo) throws JRException, IOException{

			if (condicionSeleccionado!=null) {
				if (tipoReporteSeleccionadoTabUpa.equals("1")) {
					generarReportePagosNuevo("canceladas y pendientes",tipoArchivo);
				}else if (tipoReporteSeleccionadoTabUpa.equals("2")) {
					generarReportePagosNuevo("canceladas",tipoArchivo);
				}else if (tipoReporteSeleccionadoTabUpa.equals("3")) {
					generarReportePagosNuevo("pendientes",tipoArchivo);
				}else{
					addWarnMessage("Seleccionar item", "Debe seleccionar un item de pendiente y/o cancelado ");
				}
				
			} else {
				addWarnMessage("Seleccionar item", "Debe seleccionar al menos una condición para crear el reporte");
			}
			
				
		}
		public void verificarTipoOpcionElegidaReporteUpa(){
			
		System.out.println("verificarTipoOpcionElegidaReporteUpa()");
		}
		


		/*GENERA REPORTE DE PAGOS */ //CINDY
		public void generarReportePagos(String tipoReporte,String tipoArchivo) {
			System.out.println("Obtener reporte ");
		   String  reporte= "";
		   int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
		   Double totaldeuda = 0.0,totalcancelado = 0.0,totalmora=0.0,totalmc=0.0,totaligv=0.0,totalnuevarenta=0.0;
		   List<CondicionBean> listaCondicionBean= new ArrayList<CondicionBean>();
		   List<CuotaBean> lista= new ArrayList<CuotaBean>();
		   List<RentaBean> listaRenta=new ArrayList<RentaBean>();
		   Boolean estadolista=false;	   
		   Map<String, Object> parameters = new HashMap<String, Object>();
		     
			   if (condicionSeleccionado.getSinuevomantenimiento() &&( tipoReporte.equals("ratio detalle")||tipoReporte.equals("pendientes")||tipoReporte.equals("canceladas"))||!condicionSeleccionado.getSinuevomantenimiento() && tipoReporte.equals("ratio detalle")) {
				   condicionSeleccionado.setRentas(null);//inicializa el valor 
					if (tipoReporte.equals("pendientes")) {
//						   listaRenta=reporteRecaudacionService.obtenerRentasNuevoMantenimientoContrato(condicionSeleccionado.getIdcontrato());
						  listaRenta=reporteRecaudacionService.obtenerPeriodosPendientes(condicionSeleccionado.getIdcontrato());
                          if(listaRenta!=null){
                        	  if(listaRenta.size()>0){
                        		  estadolista=true;
                        	  }
                          }
					}else if (tipoReporte.equals("canceladas")) {
	 					   listaRenta=reporteRecaudacionService.obtenerPeriodosPagados(condicionSeleccionado.getIdcontrato());
	 					  if(listaRenta!=null){
                        	  if(listaRenta.size()>0){
                        		  estadolista=true;
                        	  }
                          }
	 				}else if (tipoReporte.equals("ratio")) {
	 				 System.out.println("tipo de reporte ratio ");
	 				}else if(tipoReporte.equals("ratio detalle")){
	 					 System.out.println("tipo de reporte ratio con detalle ");
	 				}
				   
				   parameters.put("TITULO", tipoReporte.toUpperCase());
					if(estadolista){
						condicionSeleccionado.setRentas(listaRenta);
					}
				  // condicionSeleccionado.setRentas(listaRenta);
				   
				   reporte="RECAUDACION_REP_LIQUIDACION_PENDIENTES_CANCELADAS_NUEVO.jrxml";

			   }else {
					//lista=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicionSeleccionado.getIdcontrato(), condicionSeleccionado.getCondicion(), tipoReporte, tipcambio);	
					
					lista=reporteRecaudacionService.obtenerInformacionCobroCondicionTipoSinMantenimiento(condicionSeleccionado.getIdcontrato(), condicionSeleccionado.getCondicion(), tipoReporte, tipcambio);	

					/*GENERA LA LISTA DE PENDIENTES, CANCELADAS, ratio(LIQUIDACIONES CON MORA)*/
	 				if (tipoReporte.equals("pendientes")) {
	 						lista=seleccionarRentaDeuda(lista);
	 				
	 				}else if (tipoReporte.equals("canceladas")) {
	 						lista=seleccionarRentaCanceladas(lista);														
	 						
	 				}else if (tipoReporte.equals("ratio")) {	
	 					 
 							lista=test(lista, condicionSeleccionado.getFincobro());
 							lista=incluirMoraCuotasNoCanceladas(condicionSeleccionado.getMoneda(),lista);
 							lista=seleccionarRentaDeuda(lista);   //Selecciona la lista de pendientes
	 				}
				
	 				condicionSeleccionado.setCuotas(lista);/**Seteamos las cuotas a la condicion**/
	 				
					reporte= tipoReporte.equals("ratio")?ConstantesReporteSGI.REP_LIQUIDACION_RATIO_MORA:(tipoReporte.equals("canceladas")?ConstantesReporteSGI.REP_LIQUIDACION_CANCELADAS:(tipoReporte.equals("pendientes")?ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES:ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES_CANCELADAS));

				}
							 			  
			  listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/
			 
			  /**Recorremos la lista de cuota para sumar determinar los totales**/
			  if(listaRenta!=null){
				  
			  if(listaRenta.size()>0){
				  			
			  for (RentaBean rentabean : listaRenta) {
					 totaldeuda=totaldeuda+	rentabean.getRenta();
					 //System.out.println("totaldeuda: "+totaldeuda);
					 totalcancelado=totalcancelado+rentabean.getMontopagar();
					 //System.out.println("totalcancelado: "+totalcancelado);
					//-- totalmora=totalmora+rentabean.getMora();
					//--- totalmc=totalmc+rentabean.getNuevamc();
					 //System.out.println("totalmc: "+totalmc);
					//--- totaligv=totaligv+rentabean.getIgv();
					 //System.out.println("totaligv: "+totaligv);
					 totalnuevarenta=totalnuevarenta+rentabean.getRenta();
					 //System.out.println("totalnuevarenta: "+totalnuevarenta);

//					 if (!cuotabean.getSipagado()) {
//						 nropendiente++;
//					 }
		
//					 if (rentabean.getSipagado().equals("0")) {     //Cuota pendiente
//						 nropendiente++;
//					 }
////					 if (cuotabean.getSipagado()) {
////						 nrocancelado++;
////					 }
//					 if (rentabean.getSipagado().equals("1")) {     //cuota cancelada
//						 nrocancelado++;
//					 }
//					 if (rentabean.getSipagado().equals("2")){     // cuota generada
//							nropendiente++;
//						 }
					 if(rentabean.getSicancelado().equals("0")){
						 nropendiente++;
					 }else{
						 if(rentabean.getSicancelado().equals("1")){
							 nrocancelado++;
						 }else{
							 nropendiente++;
						 }
					 }
//					 if (rentabean.getSiacuenta()){
//						nroacuenta++;
//					 }					 
				
			  }
			  }
			  }
			  for (CuotaBean cuotabean : lista) {
					 totaldeuda=totaldeuda+	cuotabean.getMonto();
					 //System.out.println("totaldeuda: "+totaldeuda);
					 totalcancelado=totalcancelado+cuotabean.getRentapagada();
					 //System.out.println("totalcancelado: "+totalcancelado);
					 totalmora=totalmora+cuotabean.getMora();
					 totalmc=totalmc+cuotabean.getNuevamc();
					 //System.out.println("totalmc: "+totalmc);
					 totaligv=totaligv+cuotabean.getIgv();
					 //System.out.println("totaligv: "+totaligv);
					 totalnuevarenta=totalnuevarenta+cuotabean.getNuevarenta();
					 //System.out.println("totalnuevarenta: "+totalnuevarenta);

//					 if (!cuotabean.getSipagado()) {
//						 nropendiente++;
//					 }
					 if (cuotabean.getSipagado().equals("0")) {     //Cuota pendiente
						 nropendiente++;
					 }
//					 if (cuotabean.getSipagado()) {
//						 nrocancelado++;
//					 }
					 if (cuotabean.getSipagado().equals("1")) {     //cuota cancelada
						 nrocancelado++;
					 }
					 if (cuotabean.getSipagado().equals("2")){     // cuota generada
							nropendiente++;
						 }
					 if (cuotabean.getSiacuenta()){
						nroacuenta++;
					 }					 
				
			  }
			  
			  
			  if (tipoArchivo.equals("pdf")) {
				
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   parameters.put("TIPOCAMBIO",tipcambio); //PARA MODIFICACION TIPO CAMBIO
				   parameters.put("NROCANCELADO",nrocancelado );
				   parameters.put("NROPENDIENTE",nropendiente);
				   parameters.put("NROACUENTA",nroacuenta);
				   parameters.put("TOTALCANCELADO",totalcancelado);
				   parameters.put("TOTALDEUDA",totaldeuda);
				   parameters.put("TOTALMORA", totalmora); //franco
				   parameters.put("TOTALMC", totalmc);   
				   parameters.put("TOTALIGV", totaligv);
				   parameters.put("TOTALNUEVARENTA", totalnuevarenta);
				   parameters.put("SIREGISTRADEUDA", lista.size()==0?"FALSE":"TRUE");
				   
				   

				   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
					   
			  } else {//excel

			  }
						
			  
		}
		/** REPORTE DE PAGOS RENTAS Y NUMERO DE COMPROBANTE DE PAGO 
		 * @throws IOException 
		 * @throws JRException */
		public void generarReportePagosNuevo(String tipoReporte,String tipoArchivo) throws JRException, IOException {
			
			   String  reporte= "";
			   int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
			   Double totaldeuda = 0.0,totalpagado = 0.0,totalcancelado = 0.0,totalmora=0.0,totalmc=0.0,totaligv=0.0,totalnuevarenta=0.0;
			   List<CondicionBean> listaCondicionBean= new ArrayList<CondicionBean>();
			   List<CuotaBean> lista= new ArrayList<CuotaBean>();
			   List<RentaBean> listaRenta=new ArrayList<RentaBean>();
			   Boolean estadolista=false;	   
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   		condicionSeleccionado.setRentas(null);//inicializa el valor 
						if (tipoReporte.equals("pendientes")) {
							  listaRenta= reporteRecaudacionService.obtenerRentasPendientesxContrato(condicionSeleccionado.getIdcontrato(),condicionSeleccionado.getCondicion(),condicionSeleccionado.getSinuevomantenimiento());
	                          if(listaRenta!=null){
	                        	  if(listaRenta.size()>0){
	                        		  estadolista=true;
	                        	  }
	                          }
	                          reporte="RECAUDACION_REP_LIQUIDACION_RENTAS_PENDIENTES.jrxml";
						}else if (tipoReporte.equals("canceladas")) {
		 					   listaRenta= reporteRecaudacionService.obtenerRentasPagadosxContrato(condicionSeleccionado.getIdcontrato(),condicionSeleccionado.getCondicion(),condicionSeleccionado.getSinuevomantenimiento());
		 					  if(listaRenta!=null){
	                        	  if(listaRenta.size()>0){
	                        		  estadolista=true;
	                        	  }
	                        	  reporte="RECAUDACION_REP_LIQUIDACION_RENTAS_PAGADAS.jrxml";
	                          }
		 				}else if (tipoReporte.equals("canceladas y pendientes")) {
		 					listaRenta= reporteRecaudacionService.obtenerRentasPagadosPendientesxContrato(condicionSeleccionado.getIdcontrato(),condicionSeleccionado.getCondicion(),condicionSeleccionado.getSinuevomantenimiento());
	                          if(listaRenta!=null){
	                        	  if(listaRenta.size()>0){
	                        		  estadolista=true;
	                        	  }
	                          }
	                          reporte="RECAUDACION_REP_LIQUIDACION_RENTAS_PAGADOS_Y_PENDIENTES.jrxml";
		 				}else if(tipoReporte.equals("ratio detalle")){
		 					 System.out.println("tipo de reporte ratio con detalle ");
		 				}
					   
					   parameters.put("TITULO", tipoReporte.toUpperCase());
						if(estadolista){
							condicionSeleccionado.setRentas(listaRenta);
						}
							 			  
				  listaCondicionBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/
				 
				  /**Recorremos la lista de cuota para sumar determinar los totales**/
				  if(listaRenta!=null){
					  
				  if(listaRenta.size()>0){
					  			
				  for (RentaBean rentabean : listaRenta) {
						 totaldeuda=totaldeuda+	rentabean.getRenta();
						 totalpagado = totalpagado + rentabean.getMontopagado();	
						 totalcancelado=totalcancelado+rentabean.getMontopagar();

						 totalnuevarenta=totalnuevarenta+rentabean.getRenta();
					if (rentabean.getTipomoneda().equalsIgnoreCase(
							Constante.TIPO_MONEDA_SOLES)) {
						if (FuncionesHelper.redondearNum(rentabean.getRenta()
								- rentabean.getMontopagado(), 2) > 0) {
							rentabean.setSicancelado("0");
							nropendiente++;
						} else {
							rentabean.setSicancelado("1");
							nrocancelado++;
						}
					} else {
						if (FuncionesHelper.redondearNum(rentabean.getRenta()
								- rentabean.getMontopagado(), 2) > 0) {
							rentabean.setSicancelado("0");
							nropendiente++;
						} else {
							rentabean.setSicancelado("1");
							nrocancelado++;
						}
					}
			 
					
				  }
				  }
				  }
				  for (CuotaBean cuotabean : lista) {
						 totaldeuda=totaldeuda+	cuotabean.getMonto();
						 //System.out.println("totaldeuda: "+totaldeuda);
						 totalcancelado=totalcancelado+cuotabean.getRentapagada();
						 //System.out.println("totalcancelado: "+totalcancelado);
						 totalmora=totalmora+cuotabean.getMora();
						 totalmc=totalmc+cuotabean.getNuevamc();
						 //System.out.println("totalmc: "+totalmc);
						 totaligv=totaligv+cuotabean.getIgv();
						 //System.out.println("totaligv: "+totaligv);
						 totalnuevarenta=totalnuevarenta+cuotabean.getNuevarenta();
						 //System.out.println("totalnuevarenta: "+totalnuevarenta);

//						 if (!cuotabean.getSipagado()) {
//							 nropendiente++;
//						 }
						 if (cuotabean.getSipagado().equals("0")) {     //Cuota pendiente
							 nropendiente++;
						 }
//						 if (cuotabean.getSipagado()) {
//							 nrocancelado++;
//						 }
						 if (cuotabean.getSipagado().equals("1")) {     //cuota cancelada
							 nrocancelado++;
						 }
						 if (cuotabean.getSipagado().equals("2")){     // cuota generada
								nropendiente++;
							 }
						 if (cuotabean.getSiacuenta()){
							nroacuenta++;
						 }					 
					
				  }
				  for (CondicionBean condicion : listaCondicionBean) {
					 condicion.setNro_meses_pendientes(nropendiente);
					 condicion.setNro_meses_pagados(nrocancelado);
					 condicion.setTotal_cancelado(totalpagado);
					 condicion.setTotal_adeudado(totalcancelado);
				}
				  
				  if (tipoArchivo.equals("pdf")) {
					
					   
					   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					   parameters.put("TIPOCAMBIO",tipcambio); //PARA MODIFICACION TIPO CAMBIO
					   parameters.put("NROCANCELADO",nrocancelado );
					   parameters.put("NROPENDIENTE",nropendiente);
					   parameters.put("NROACUENTA",nroacuenta);
					   parameters.put("TOTALCANCELADO",totalcancelado);
					   parameters.put("TOTALPAGADO",totalpagado);
					   parameters.put("TOTALDEUDA",totaldeuda);
					   parameters.put("TOTALMORA", totalmora); //franco
					   parameters.put("TOTALMC", totalmc);   
					   parameters.put("TOTALIGV", totaligv);
					   parameters.put("TOTALNUEVARENTA", totalnuevarenta);
					   parameters.put("SIREGISTRADEUDA", listaRenta.size()==0?"FALSE":"TRUE");
					   parameters.put("TITULO", tipoReporte.toUpperCase());
					   
					   

					   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
						   
				  } else {
					  		
					  		reporteExcelNuevo(listaCondicionBean,tipoReporte.toUpperCase());
					  		
				  }
							
				  
			}
	
		public void reporteExcelNuevo(List<CondicionBean> lista ,String nombre)throws JRException, IOException {
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("Página 1");

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
	        sheet.setColumnWidth(2, 3500);
	        sheet.setColumnWidth(3, 3500);
	        sheet.setColumnWidth(4, 3500);
	        sheet.setColumnWidth(5, 3500);
	        sheet.setColumnWidth(6, 3500);
	        sheet.setColumnWidth(7, 3500);
	        sheet.setColumnWidth(8, 3500);
	        sheet.setColumnWidth(18, 4000);
	        
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
	        cc.setCellValue("REPORTE DE RENTA "+nombre);
	        cc.setCellStyle(csBold);
	        
	        ind=8;
	        r = sheet.createRow(ind);
	        for (CondicionBean condicionBean : lista) {	        	
		        Cell c = r.createCell(0);
		        c.setCellValue("TITULAR:");
		        
		        c = r.createCell(1);
		        c.setCellValue(condicionBean.getNombresInquilino());
		        
		        c = r.createCell(4);
		        c.setCellValue("CONDICIÓN:");		        
		        c = r.createCell(5);
		        c.setCellValue(condicionBean.getCondicion().toUpperCase());
		        
		        c = r.createCell(7);
		        c.setCellValue("MONEDA:");		        
		        c = r.createCell(8);
		        c.setCellValue(condicionBean.getMoneda().toUpperCase());
		        
		        ind++;
		        r = sheet.createRow(ind);
		        c = r.createCell(0);		        
		        c.setCellValue("OCUPANTE:");
		        
		        c = r.createCell(1);
		        c.setCellValue((String)condicionBean.getNombreocupante()!=null? condicionBean.getNombreocupante().toUpperCase():"NO REGISTRA");
		        
		        c = r.createCell(4);
		        c.setCellValue("ESTADO:");
		        c = r.createCell(5);
		        c.setCellValue((String)condicionBean.getEstado()!=null? condicionBean.getEstado().toUpperCase():"NO REGISTRA");
		        
		        c = r.createCell(7);
		        c.setCellValue("INICIO DE COBRO:");
		        c = r.createCell(8);
		        c.setCellValue(condicionBean.getIniciocobro()!=null? (String)FuncionesHelper.fechaToString(condicionBean.getIniciocobro()):"NO REGISTRA");
		        
	        
		        ind++;
		        r = sheet.createRow(ind);
		        c = r.createCell(0);
		        c.setCellValue("UPA:");
		        c = r.createCell(1);
		        c.setCellValue((String)condicionBean.getClaveUpa()!=null? condicionBean.getClaveUpa().toUpperCase():"NO REGISTRA");
		        
		        c = r.createCell(4);
		        c.setCellValue("NRO:");
		        c = r.createCell(5);
		        c.setCellValue((String)condicionBean.getNrocontrato()!=null? condicionBean.getNrocontrato().toUpperCase():"NO REGISTRA");
		        
		        c = r.createCell(7);
		        c.setCellValue("FIN DE COBRO:");
		        c = r.createCell(8);
		        c.setCellValue(condicionBean.getFincobro()!=null? (String)FuncionesHelper.fechaToString(condicionBean.getFincobro()):"NO REGISTRA");
		        
		        ind++;
		        r = sheet.createRow(ind);
		        c = r.createCell(0);
		        c.setCellValue("DIRECCIÓN:");
		        c = r.createCell(1);
		        c.setCellValue((String)condicionBean.getDireccion()!=null? condicionBean.getDireccion().toUpperCase():"No registra");
		       
		        c = r.createCell(4);
		        c.setCellValue("INICIO DE CONTRATO:");
		        c = r.createCell(5);
		        c.setCellValue(condicionBean.getIniciocontrato()!=null? (String)FuncionesHelper.fechaToString(condicionBean.getIniciocontrato()):"NO REGISTRA");
		        
		        
		        ind++;
		        r = sheet.createRow(ind);
		        c = r.createCell(0);
		        c.setCellValue("DISTRITO:");
		        c = r.createCell(1);
		        c.setCellValue((String)condicionBean.getDistrito()!=null? condicionBean.getDistrito():"No registra");
		        c = r.createCell(4);
		        c.setCellValue("FIN DE CONTRATO:");
		        c = r.createCell(5);
		        c.setCellValue(condicionBean.getFincontrato()!=null? (String)FuncionesHelper.fechaToString(condicionBean.getFincontrato()):"NO REGISTRA");
		        
		        
		        
		        ind++;
		        r = sheet.createRow(ind);
		        c = r.createCell(0);
		        c.setCellValue("USO:");
		        c = r.createCell(1);
		        c.setCellValue((String)condicionBean.getUso().toUpperCase()!=null? condicionBean.getUso().toUpperCase():"NO REGISTRA");
		        c = r.createCell(4);
		        c.setCellValue("NRO MESES PENDIENTES:");
		        c = r.createCell(5);
		        c.setCellValue(condicionBean.getNro_meses_pendientes()!=null? condicionBean.getNro_meses_pendientes():0);
		        
		        
	        }
	        String[] headers ;
	        if(nombre.equalsIgnoreCase("CANCELADAS")){
		        headers = new String[] { "MES", "AÑO", "CONTRAPRESTACION","RENTA","CANCELADO","TOTAL CANCELADO","TIPO DOC.","Nº COMPROBANTE","TOTAL ","FECHA CANCELACION","Nº COMPROBANTE REFERENCIA"};

	        }else if (nombre.equalsIgnoreCase("PENDIENTES")){
		         headers = new String[] { "MES", "AÑO", "CONTRAPRESTACION","RENTA","CANCELADO","TOTAL ADEUDADO"};

	        }else{
		         headers = new String[] { "MES", "AÑO", "CONTRAPRESTACION","RENTA","CANCELADO","PAGADO","TOTAL ADEUDA"};

	        }
	        r = sheet.createRow(15);
	        for (int rn=0; rn<headers.length; rn++) {
	           Cell cell = r.createCell(rn);
	           cell.setCellValue(headers[rn]);
	           cell.setCellStyle(csBold);
	        }
	        
	        /**Insercion de datos **/
	        
	        
	        int rownum = 16;
	        if(nombre.equalsIgnoreCase("CANCELADAS")|| nombre.equalsIgnoreCase("PENDIENTES")){
	        for (int i = 0; i < lista.size(); i++) {
	        	CondicionBean bean = new CondicionBean();
	        	bean= lista.get(i);
	        	
	        	for(RentaBean renta : bean.getRentas()){
	        		if(renta.getDetalleCuotaBean()!=null && renta.getDetalleCuotaBean().size()>0){
	        			
	        			
	        		for (DetalleCuotaBean detalle : renta.getDetalleCuotaBean()) {
	        		Row row = sheet.createRow(rownum++);
	        		for (int j = 0; j < 11; j++) {
	        			Cell cell = row.createCell(j);
	        			if (j==0){
	        				cell.setCellValue((String)renta.getMes());
	        			}else if(j==1){
	        				cell.setCellValue((String)renta.getAnio());
	        			}else if(j==2){
	        				cell.setCellValue((Double)renta.getContraprestacion() );//renta
	        			}else if(j==3){
	        				cell.setCellValue((Double)renta.getRenta());//contraprestacon
	        			}else if(j==4){
	        				cell.setCellValue(renta.getSicancelado()!=null? (renta.getSicancelado().equalsIgnoreCase("1")? "SI":"NO"):"NO");//montopagado
	        			}else if(j==5){
	        				cell.setCellValue((Double)renta.getMontopagar());//montopagado
	        			}else if(j==6){
		        				cell.setCellValue((String)detalle.getNombretipodocu());//montopagado		        		
	        			}else if(j==7){
	        				cell.setCellValue((String)detalle.getNroseriecomprobante());
	        			}else if(j==8){
	        				cell.setCellValue((Double)detalle.getMontosoles());//montopagado
	        			}else if(j==9){
	        				cell.setCellValue(detalle.getFecCancelacion()!=null ?(String)FuncionesHelper.fechaToString(detalle.getFecCancelacion()):"");
	        		    }else if(j==10){
		        				cell.setCellValue((String)detalle.getNroseriecomprobanteref());
		        			}
	        			}
	        		}
	        		}else{
	        	 		Row row = sheet.createRow(rownum++);
		        		for (int j = 0; j < 6; j++) {
		        			Cell cell = row.createCell(j);
		        			if (j==0){
		        				cell.setCellValue((String)renta.getMes());
		        			}else if(j==1){
		        				cell.setCellValue((String)renta.getAnio());
		        			}else if(j==2){
		        				cell.setCellValue((Double)renta.getContraprestacion() );//renta
		        			}else if(j==3){
		        				cell.setCellValue((Double)renta.getRenta());//contraprestacon
		        			}else if(j==4){
		        				cell.setCellValue(renta.getSicancelado()!=null? (renta.getSicancelado().equalsIgnoreCase("1")? "SI":"NO"):"NO");//montopagado		        			
		        			}else if(j==5){
		        				cell.setCellValue((Double)renta.getMontopagar());//montopagado
		        			
		        			}
	        		}
	        	 }
	        		
	        		ind=rownum;
	        	}
 
	   
	        }
	        }else{
	        	for (int i = 0; i < lista.size(); i++) {
		        	CondicionBean bean = new CondicionBean();
		        	bean= lista.get(i);
		        	
		        	for(RentaBean renta : bean.getRentas()){
		        		Row row = sheet.createRow(rownum++);
		        		for (int j = 0; j < 7; j++) {
		        			Cell cell = row.createCell(j);
		        			if (j==0){
		        				cell.setCellValue((String)renta.getMes());
		        			}else if(j==1){
		        				cell.setCellValue((String)renta.getAnio());
		        			}else if(j==2){
		        				cell.setCellValue((Double)renta.getContraprestacion() );//renta
		        			}else if(j==3){
		        				cell.setCellValue((Double)renta.getRenta());//contraprestacon
		        			}else if(j==4){
		        				cell.setCellValue(renta.getSicancelado()!=null? (renta.getSicancelado().equalsIgnoreCase("1")? "SI":"NO"):"NO");//montopagado		        			
		        			}else if(j==5){
		        				cell.setCellValue((Double)renta.getMontopagado());//montopagado		        			
		        			}else if(j==6){
		        				cell.setCellValue((Double)renta.getMontopagar());//ADEUDA		        			
		        			}
		        	}
		        		ind=rownum;
	        	}
	        }
	        }
        	for (CondicionBean condicionBean : lista) {	
        	r = sheet.createRow(ind+1);
        	Cell ce= r.createCell(7);
        	System.out.println("Nombre = "+nombre);
        	if(nombre.equalsIgnoreCase("CANCELADAS")){
        		ce.setCellValue("TOTAL CANCELADO :");
        		ce = r.createCell(8);
	        	ce.setCellValue(condicionBean.getTotal_cancelado()!=null? condicionBean.getTotal_cancelado():0.0);
        	}else if(nombre.equalsIgnoreCase("PENDIENTES")){
        		ce= r.createCell(4);
        		ce.setCellValue("TOTAL PENDIENTE :");
        		ce = r.createCell(5);
	        	ce.setCellValue(condicionBean.getTotal_adeudado()!=null? condicionBean.getTotal_adeudado():0.0);
        	}else if(nombre.equalsIgnoreCase("CANCELADAS Y PENDIENTES")){
        		ce = r.createCell(5);
        		ce.setCellValue("TOTAL PENDIENTE :");
        		ce = r.createCell(6);
	        	ce.setCellValue(condicionBean.getTotal_adeudado()!=null? condicionBean.getTotal_adeudado():0.0);
	        	ind++;
	        	r = sheet.createRow(ind+1);
	        	ce = r.createCell(5);
	        	ce.setCellValue("TOTAL PAGADO :");
        		ce = r.createCell(6);
	        	ce.setCellValue(condicionBean.getTotal_cancelado()!=null? condicionBean.getTotal_cancelado():0.0);
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
	        	
				report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLS, "ReporteUpa"+FuncionesHelper.fechaToString(new Date())+".xls");
				//report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLSX, "ReporteComprobante"+FuncionesHelper.fechaToString(new Date())+".XLSX");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
		}
/*INFORMACION PAGOS DE CONTRATO, SACA LAS PENDIENTES*/ /*CINDY*/
	
	public List<CuotaBean> test(List<CuotaBean> lista,Date fincobro){
		CuotaBean cuota;
		
		Calendar inicio=Calendar.getInstance();
		inicio.setTime(fincobro!=null?fincobro:inicio.getTime());
		inicio.set(Calendar.DATE, inicio.getActualMaximum(Calendar.DAY_OF_MONTH));
		//System.out.println("FECHA INICIO="+inicio.getTime());
		
		Calendar fin=Calendar.getInstance();
		fin.set(Calendar.DATE, fin.getActualMinimum(Calendar.DAY_OF_MONTH));
		System.out.println("FINICIO="+inicio.getTime());
		System.out.println("FFINAL="+fin.getTime());
		
		while (inicio.getTime().compareTo(fin.getTime())< 0) 
		{
			inicio.add(Calendar.MONTH, 1);
			
			cuota= new CuotaBean();
			cuota.setAnio(inicio.get(Calendar.YEAR));
	    	cuota.setMes(Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH)));
	    	cuota.setSipagado("1");
	    	cuota.setCondicion("Cancelado");
	    	cuota.setNropagos(1);
	    	cuota.setMonto(FuncionesHelper.redondear(0.0, 2)); 
	    	cuota.setRenta(cuota.getMonto());
	    	cuota.setIgv(FuncionesHelper.redondear((cuota.getRenta()/1.18)*0.18, 2)); 
	    	cuota.setRentapagada(0.0);

	    	cuota.setMora(0.0);
	    	cuota.setNuevamc(0.0);
	    	cuota.setNuevarenta(0.0);
	    	
	    	/* CONDICIONAL PARA REPORTE DE LIQUIDACIÓN PENDIENTES CANCELADOS Y GENERADOS */
		    /**********************************************/
//		    if (cuota.getSipagado().equals("2")) {
//				cuota.setSiacuenta(false);
//		        cuota.setMonto(0.0);
//			}
		    /**********************************************/
	    	
	    	
	    	
	    	
	    	lista.add(cuota);
	    	
		}

		return lista;
	}	
		
	public List<CuotaBean> seleccionarRentaDeuda(List<CuotaBean> cuotas){
		List<CuotaBean> resultado= new ArrayList<CuotaBean>();

//		for (int i = 0; i < cuotas.size(); i++) {
//			//if (!cuotas.get(i).getSipagado()) {   //if(false)
//
//				resultado.add(cuotas.get(i));
//			}
//		}
		
		for (int i = 0; i < cuotas.size(); i++) {
			//if (!cuotas.get(i).getSipagado()) {   //if(false)
			//if (cuotas.get(i).getSipagado().equals("0")) {
			if (cuotas.get(i).getSipagado().equals("0") || cuotas.get(i).getSipagado().equals("2")) 
			{
				System.out.println("upa: "+cuotas.get(i).getClaveupa());
				System.out.println("DeudaAcuenta: "+cuotas.get(i).getDeudaacuenta());
				System.out.println("MC: "+cuotas.get(i).getMc());
				System.out.println("Monto: "+cuotas.get(i).getMonto());
				System.out.println("Monto: "+cuotas.get(i).getMonto());
				/********************/
//				if (cuotas.get(i).getSipagado().equals("2")) {
//					cuotas.set(i).setMonto(0.0); 
//					cuotas.set(i).setSiacuenta(false);
//					resultado.add(cuotas.get(i));
//				}
				/********************/
				resultado.add(cuotas.get(i));
			}
		}

		cuotas.clear();
		cuotas.addAll(resultado);
		
		return cuotas;
	}



public List<CuotaBean> incluirMoraCuotasNoCanceladas(String moneda, List<CuotaBean> lista) 
{
		int nromes=0 ;
		int anioActual;
		int index = 0;
		String mesActual;
		Calendar now = Calendar.getInstance();
		anioActual=now.get(Calendar.YEAR);
		mesActual=Almanaque.obtenerNombreMes(now.get(Calendar.MONTH));

		for (int i = 0; i < lista.size(); i++) 
		{
			if (lista.get(i).getMes().equals(mesActual) && lista.get(i).getAnio()==anioActual) 
			{
				index=i;
			}
		}

		for (int i = lista.size()-1; i >=0 ; i--) 
		{
			if (index<=i) {
				lista.get(i).setRatiomora(0.0);
				lista.get(i).setMora(0.0);
				
				lista.get(i).setMc(FuncionesHelper.redondear(lista.get(i).getMonto()/1.18, 2));
				lista.get(i).setIgv(FuncionesHelper.redondear((lista.get(i).getMonto()/1.18)*0.18,2));
				lista.get(i).setNuevamc(lista.get(i).getMc());
				lista.get(i).setNuevarenta(lista.get(i).getNuevamc()+lista.get(i).getIgv());
			} 
			else 
			{
				int value=0;
				value=moneda.equals("S")? 12:36;   //es lo mismo a decir:           if moneda='S' {value=12} else {value=36}
				
				if (nromes>=value) {
					lista.get(i).setRatiomora(moneda.equals("S")? Constantes.ratiosMoraSoles.get(Constantes.ratiosMoraSoles.size()-1):Constantes.ratiosMoraDolares.get(Constantes.ratiosMoraDolares.size()-1));
				} 
				else 
				{
					lista.get(i).setRatiomora(moneda.equals("S")? Constantes.ratiosMoraSoles.get(nromes):Constantes.ratiosMoraDolares.get(nromes));
				}
				
				lista.get(i).setMc(FuncionesHelper.redondear(lista.get(i).getMonto()/1.18, 2));   // MC: merced conductiva tanto para dolares y soles. //Cindy
				lista.get(i).setNuevamc(FuncionesHelper.redondear(lista.get(i).getMc()*lista.get(i).getRatiomora(), 2));
				lista.get(i).setIgv(FuncionesHelper.redondear((lista.get(i).getMonto()/1.18)*0.18,2));
				lista.get(i).setMora(FuncionesHelper.redondear(lista.get(i).getNuevamc()-lista.get(i).getMc(), 2));
				lista.get(i).setNuevarenta(lista.get(i).getNuevamc()+lista.get(i).getIgv());
				nromes++;
			}
		}
		
		return lista;
	}

	public List<CuotaBean> seleccionarRentaCanceladas(List<CuotaBean> lista){
		List<CuotaBean> listaCuotaBean= new ArrayList<CuotaBean>();
	
		for (int i = 0; i < lista.size(); i++) {
//			if (lista.get(i).getSipagado()) {
			if (lista.get(i).getSipagado().equals("1")) {
				listaCuotaBean.add(lista.get(i));
			}
		}
	
		lista.clear();
		lista.addAll(listaCuotaBean);
	
		return lista;
	}




	/**
	 * Seleccionador de tipo de reporte de Cartera Activa.
	 * @param tipo - Tipo de opcion seleccionada para el reporte.
	 */
	public void elegirReporteTabCartera(String tipo) {
		if (tipoReporteSeleccionadoTabCarteraActiva.equals("1")) {
			generarReporteCobranzaxCartera(tipo);
		}else if (tipoReporteSeleccionadoTabCarteraActiva.equals("2")) {
			generarReporteDeudorPagadorCA("por cobrar",tipo);
		}else if(tipoReporteSeleccionadoTabCarteraActiva.equals("3")){
			generarReporteDeudorPagadorCA("cobradas",tipo);
		}else if (tipoReporteSeleccionadoTabCarteraActiva.equals("4")) {
			generarReporteDeudorPagadorCA("de Cartera",tipo);
		} else if (tipoReporteSeleccionadoTabCarteraActiva.equals("5")){
			generarReporteDeudorPagadorCA("Al día",tipo);
		}else {
			generarReporteDeudorPagadorRentaPendientesCA(tipo);
		}
	}

	/**
	 * Seleccionador de tipo de reporte de Cartera Pesada.
	 * @param tipo - Tipo de opcion seleccionada para el reporte.
	 */
	public void elegirReporteTabCarteraPasiva(String tipo) {

		if (tipoReporteSeleccionadoTabCarteraPasiva.equals("1")) {
			generarReporteDeudorPagadorCP("",carteraPesadaReporteCartera,tipo);
		}
	}

	/**
	 * Reporte con listado de todos los pendientes de pagos por numero de cartera.
	 * @param tipo 
	 */
	public void generarReporteCobranzaxCartera(String tipo) {

		if (obtenerTipoCambioDia()) {
			
			List<CondicionBean> listaCondicionesVigentes=new ArrayList<CondicionBean>();
			listaCondicionesVigentes=contratoService.obtenerCondicionesVigentes(carteraActivaReporteCartera);

			for (CondicionBean bean : listaCondicionesVigentes) {
				
				if (bean.getCondicion().equals("Contrato") || bean.getCondicion().equals("Precontrato")) {
					if (bean.getIniciocobro()!=null && bean.getFincobro()!=null) {
						bean.setCuotas(reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(bean.getIdcontrato(),"Contrato || Pre Contrato","pendientes",tipcambio));
						if (bean.getCuotas().size()>0) {
							listaCobroCartera.add(bean);
						}
					}
				}
				if (bean.getCondicion().equals("Sin Contrato")) {
					if (bean.getIniciocobro()!=null) {
						bean.setCuotas(reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(bean.getIdcontrato(),"Sin Contrato","pendientes",tipcambio));
						if (bean.getCuotas().size()>0) {
							listaCobroCartera.add(bean);
						}

					}
				}
			}
			
			
			if (tipo.equals(Constantes.PDF)) {
				
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
			   parameters.put("CARTERA",carteraActivaReporteCartera );//PARA MODIFICACION TIPO CAMBIO
			   report = reporteGeneradorService.generarPDF(ConstantesReporteSGI.REP_COBRO_DETALLADO_CONDICION_CARTERA_ACTIVA, parameters, listaCobroCartera,"ReporteCobranza_upasXcartera_"+FuncionesHelper.fechaToString(new Date()));
			   	
			   
			   
			} else if (tipo.equals(Constantes.XLS)) {
				
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("Página 1");
				
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
		        sheet.setColumnWidth(2, 6000);
		        sheet.setColumnWidth(3, 10000);
		        sheet.setColumnWidth(4, 3000);
		        sheet.setColumnWidth(5, 3000);
		        sheet.setColumnWidth(6, 3000);
		        sheet.setColumnWidth(8, 3000);
		        
		        /**Insercion de cabecera (rownum=0) **/
		        CellStyle csBold = null;
		        
		        Font bold = workbook.createFont();
		        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		        bold.setFontHeightInPoints((short) 10);
		        
		        csBold = workbook.createCellStyle();
		        csBold.setBorderBottom(CellStyle.BORDER_THIN);
		        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		        csBold.setFont(bold);
		        
		        String[] headers = new String[] { "CLAVE", "DIRECCION", "DISTRITO","INQUILINO","CONDICION","CARTERA","MONEDA","P.J","CUOTA1","CUOTA2","CUOTA3","CUOTA4" ,"CUOTA5","CUOTA6","MES","AÑO","RENTA","A CUENTA","A PAGAR"};
		
		        Row r = sheet.createRow(5);
		        for (int rn=0; rn<headers.length; rn++) {
		           Cell cell = r.createCell(rn);
		           cell.setCellValue(headers[rn]);
		           cell.setCellStyle(csBold);
		        }
		        
		        
		        /**Insercion de datos **/
		        int rownum = 6;
		        for (int i = 0; i < listaCondicionesVigentes.size(); i++) {
		        	
		           
		           CondicionBean bean= new CondicionBean();
		           bean= listaCondicionesVigentes.get(i);
		           
		           	try{
		           		
		               	for (CuotaBean cuotaBean : bean.getCuotas()) {
		               		Row row = sheet.createRow(rownum++);
	
		           		
		                for (int j = 0; j < 19; j++) {
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
			                					 cell.setCellValue((String)bean.getCondicion());
			                			 	else if (j==5)
			                					 cell.setCellValue((String)bean.getNumerocartera());
				                					 else if (j==6)
				                						 cell.setCellValue((String)bean.getMoneda()); 
						                					 else if (j==7)
						                						 cell.setCellValue((String)(bean.getSiprocesojudicial()==true?"Sí":"No"));
							                						 else if (j==8)
							                							 cell.setCellValue(bean.getCuota1()!=null?bean.getCuota1():0.0);
									                						 else if (j==9)
									                							 cell.setCellValue(bean.getCuota2()!=null?bean.getCuota2():0.0);
											                						 else if (j==10)
											                							 cell.setCellValue(bean.getCuota3()!=null?bean.getCuota3():0.0);
												                						 else if (j==11)
												                							 cell.setCellValue(bean.getCuota4()!=null?bean.getCuota4():0.0);
													                						 else if (j==12)
													                							 cell.setCellValue(bean.getCuota5()!=null?bean.getCuota5():0.0);
														                						 else if (j==13)
														                							 cell.setCellValue(bean.getCuota6()!=null?bean.getCuota6():0.0);
															                						 else if (j==14)
															                							 cell.setCellValue((String)cuotaBean.getMes());
																                						 else if (j==15)
																                							 cell.setCellValue((Integer)cuotaBean.getAnio());
																	                						 else if (j==16)
																	                							 cell.setCellValue((Double)cuotaBean.getRenta());
																		                						 else if (j==17)
																		                							 cell.setCellValue((Double)(cuotaBean.getRenta()-cuotaBean.getMonto()));
																			                						 else if (j==18)
																			                							 cell.setCellValue((Double)cuotaBean.getMonto());

		                } }
		           	} catch (Exception e) {
			        	   System.out.println("Controlando con excepcion para continuar y no caer Cartera Activa, error en : "+bean.getClaveUpa());
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
		        	
					report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Cobro_Detallado"+Constantes.EXTENSION_FORMATO_XLS);
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			} 
			
		}else{
			addWarnMessage("", "El tipo de cambio del dia no se ha registrado en el sistema SGI. ");
		}
	}



	/**
	 * Reporte de upas cobradas,por cobrar,al día, de cartera por número de cartera.
	 **/
	public void generarReporteDeudorPagadorCA(String tipo,String tipoarchivo) {
		Double sumasoles=0.0,sumadolares=0.0;
		List<UpaPagadorDeudorBean> listaTipoCA= new ArrayList<UpaPagadorDeudorBean>();
		
		Calendar Hasta = Calendar.getInstance();
		Calendar Desde = Calendar.getInstance();

		Hasta.setTime(fechaconsultacarteraactiva);
		Desde.setTime(fechaconsultacarteraactiva);

		Desde.add(Calendar.DAY_OF_YEAR, -365);
		
		
			
			List<CondicionBean> listaCondicionesVigentes=new ArrayList<CondicionBean>();
			listaCondicionesVigentes=contratoService.obtenerCondicionesVigentes(carteraActivaReporteCartera);
			
			
			for (CondicionBean condicion : listaCondicionesVigentes) {
				
				UpaPagadorDeudorBean upaPagadorDeudorBean=reporteRecaudacionService.obtenerInformacionUltimoPagoRentaxCondicion(condicion.getIdcontrato(),FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));
				//boolean siexistePagoIntervalo = false,siexistePago = false,siiniciocobrodentroCA = false,sicanceloRentaVigente = false,sicanceloRentaAdelantada = false,sicuotascanceladas = false;
				boolean siexistePagoIntervalo = false,siexistePago = false,siiniciocobrodentroCA = false,sicanceloRentaVigente = false,sicanceloRentaAdelantada = false;
				String sicuotascanceladas = "0";
				
				
				if (upaPagadorDeudorBean!=null) {/**No se encontro resultado dentro de intervalo**/
					siexistePagoIntervalo=true;
					
					sicanceloRentaVigente=FechaUtil.getYear(Hasta.getTime()).equals(FechaUtil.getYear(upaPagadorDeudorBean.getFecCancelacion()))&& 
										   FechaUtil.getMonth(Hasta.getTime()).equals(FechaUtil.getMonth(upaPagadorDeudorBean.getFecCancelacion()));
					
					sicanceloRentaAdelantada=(FechaUtil.getYear(Hasta.getTime()).equals(upaPagadorDeudorBean.getAnio().toString()) && 
					Integer.parseInt(FechaUtil.getMonth(Hasta.getTime()))<=Integer.parseInt(Almanaque.mesanumero(upaPagadorDeudorBean.getMes()))) ||
					Integer.parseInt(FechaUtil.getYear(Hasta.getTime()))<upaPagadorDeudorBean.getAnio();
					
				}else{
					upaPagadorDeudorBean=reporteRecaudacionService.obtenerInformacionUltimoPagoRentaxCondicion(condicion.getIdcontrato(),"01/01/1900",FuncionesHelper.fechaToString(Hasta.getTime()));
					Calendar calendar=Calendar.getInstance();
					calendar.setTime(Hasta.getTime());
					calendar.add(Calendar.MONTH, -12);
					
					if(upaPagadorDeudorBean!=null){
						siexistePago=true;
					
					}else if (condicion.getIniciocobro().compareTo(calendar.getTime())>0)  {
						siiniciocobrodentroCA=true;/**Inicio de cobro debe estar dentro del intervalo de cartera activo, sino no deberia aparecer en el reporte de CA, mas si en el CP**/
						
					}
					/**Instanceamos ya que esta al no obtener resultado dentro del intervalo de cartera activa en la consulta al service no se instanceo y quedo nulo**/
					upaPagadorDeudorBean= new UpaPagadorDeudorBean();
				}	
					
				
				if (tipo.equals("por cobrar")) {
					
					upaPagadorDeudorBean.setObservacion("ok");
					List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
					List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
					
					
					if (!siexistePagoIntervalo && !siexistePago && siiniciocobrodentroCA) {
						/**Para incluir aquellas condiciones que no tiene un pago realizado pero deberia aparecer en el reporte para que se cobre**/
						CondicionBean condicionBean= new CondicionBean();
						condicionBean=contratoService.obtenerCondicionBean(condicion.getIdcontrato());
						upaPagadorDeudorBean.setCondicion(condicionBean.getCondicion());
						upaPagadorDeudorBean.setNroseriecomprobante(null);
						upaPagadorDeudorBean.setMoneda(condicionBean.getMoneda());
						upaPagadorDeudorBean.setFecCancelacion(null);
						upaPagadorDeudorBean.setMes(null);
						upaPagadorDeudorBean.setAnio(null);
						upaPagadorDeudorBean.setFincobro(null);
						upaPagadorDeudorBean.setClaveUpa(condicionBean.getClaveUpa());
						upaPagadorDeudorBean.setDireccion(condicionBean.getDireccion());
						upaPagadorDeudorBean.setIniciocobro(condicionBean.getIniciocobro());
						upaPagadorDeudorBean.setNombreInquilino(condicionBean.getNombresInquilino());
						upaPagadorDeudorBean.setDistrito(condicionBean.getDistrito());
						
						upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(condicion.getIdcontrato()));
						upaPagadorDeudorBean.setPrimeraRentaPendiente(reporteRecaudacionService.obtenerPrimeraRentaPendienteCondicion(condicion.getIdcontrato()));
						
						listaTipoCA.add(upaPagadorDeudorBean);
					
					
					}else if(siexistePagoIntervalo){
						
							if (!sicanceloRentaVigente && !sicanceloRentaAdelantada) {
									try {
										if (condicion.getCondicion().equals("Sin Contrato")) {
											listaPendientesSinContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Sin Contrato" , "pendientes",tipcambio);
										} else {
											listaPendientesContratoPreContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Contrato || Pre Contrato","pendientes",tipcambio);
										}
									
									} catch (NullPointerException e) {
										System.out.println(e.getMessage());
										upaPagadorDeudorBean.setObservacion("fail");
									}

									if (listaPendientesSinContratoCuotaBean.size()!=0 || listaPendientesContratoPreContratoCuotaBean.size()!=0) {
										
										upaPagadorDeudorBean.setPrimeraRentaPendiente(upaPagadorDeudorBean.getCondicion().equals("Sin Contrato")?listaPendientesSinContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesSinContratoCuotaBean.get(0).getAnio():listaPendientesContratoPreContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesContratoPreContratoCuotaBean.get(0).getAnio());
										upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(upaPagadorDeudorBean.getIdcontrato()));
										listaTipoCA.add(upaPagadorDeudorBean);
									}
						}
						
					}
			
			
			}else if (tipo.equals("cobradas")){
				if (siexistePagoIntervalo && sicanceloRentaVigente) {
						listaTipoCA.add(upaPagadorDeudorBean);
				}
			}else if (tipo.equals("de Cartera")) {

				upaPagadorDeudorBean.setObservacion("ok");
				List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
				List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
				
				
				if (!siexistePagoIntervalo && !siexistePago && siiniciocobrodentroCA) {
					/**Para incluir aquellas condiciones que no tiene un pago realizado pero deberia aparecer en el reporte para que se cobre**/
					CondicionBean condicionBean= new CondicionBean();
					condicionBean=contratoService.obtenerCondicionBean(condicion.getIdcontrato());
					upaPagadorDeudorBean.setCondicion(condicionBean.getCondicion());
					upaPagadorDeudorBean.setNroseriecomprobante(null);
					upaPagadorDeudorBean.setMoneda(condicionBean.getMoneda());
					upaPagadorDeudorBean.setFecCancelacion(null);
					upaPagadorDeudorBean.setMes(null);
					upaPagadorDeudorBean.setAnio(null);
					upaPagadorDeudorBean.setFincobro(null);
					upaPagadorDeudorBean.setClaveUpa(condicionBean.getClaveUpa());
					upaPagadorDeudorBean.setDireccion(condicionBean.getDireccion());
					upaPagadorDeudorBean.setIniciocobro(condicionBean.getIniciocobro());
					upaPagadorDeudorBean.setNombreInquilino(condicionBean.getNombresInquilino());
					upaPagadorDeudorBean.setDistrito(condicionBean.getDistrito());
					
					upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(condicion.getIdcontrato()));
					upaPagadorDeudorBean.setPrimeraRentaPendiente(reporteRecaudacionService.obtenerPrimeraRentaPendienteCondicion(condicion.getIdcontrato()));
					
					listaTipoCA.add(upaPagadorDeudorBean);
				
				
				}else if(siexistePagoIntervalo){
					
							try {
								if (condicion.getCondicion().equals("Sin Contrato")) {
									listaPendientesSinContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Sin Contrato", "pendientes",tipcambio);
								} else {
									listaPendientesContratoPreContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Contrato || Pre Contrato","pendientes",tipcambio);
								}
							
							} catch (NullPointerException e) {
								System.out.println(e.getMessage());
								upaPagadorDeudorBean.setObservacion("fail");
							}

							if (listaPendientesSinContratoCuotaBean.size()!=0 || listaPendientesContratoPreContratoCuotaBean.size()!=0) {
								
								upaPagadorDeudorBean.setPrimeraRentaPendiente(upaPagadorDeudorBean.getCondicion().equals("Sin Contrato")?listaPendientesSinContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesSinContratoCuotaBean.get(0).getAnio():listaPendientesContratoPreContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesContratoPreContratoCuotaBean.get(0).getAnio());
								upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(upaPagadorDeudorBean.getIdcontrato()));
								
							}else {
								upaPagadorDeudorBean.setPrimeraRentaPendiente("S/D");
								upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(upaPagadorDeudorBean.getIdcontrato()));
							}
							
							listaTipoCA.add(upaPagadorDeudorBean);
				
			}
						
			}else if (tipo.equals("Al día")) {
			
				
				if (siexistePagoIntervalo) {
					if ( sicanceloRentaAdelantada) {
						listaTipoCA.add(upaPagadorDeudorBean);
					}
						
				}
			} 
		}
			
			
			/**Sumando los totales en dolares y soles de las lista resultado*/
			for (UpaPagadorDeudorBean upaPagadorDeudorBean : listaTipoCA) {
				if (upaPagadorDeudorBean.getMoneda().equals("S")) {
					sumasoles=sumasoles+upaPagadorDeudorBean.getRenta();
				} else {
					sumadolares=sumadolares+upaPagadorDeudorBean.getRenta();
				}
			}
		
		   
		   
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
		   parameters.put("CARTERA",carteraActivaReporteCartera.equals("")?"TODOS":carteraActivaReporteCartera.toString());
		   parameters.put("TIPOCONSULTA",tipo);
		   parameters.put("TOTALSOLES",sumasoles);
		   parameters.put("TOTALDOLARES",sumadolares);

		   parameters.put("FECHACANCELACION",FuncionesHelper.fechaToString(Hasta.getTime()));


		   if (tipoarchivo.equals("pdf")) {
				   report = reporteGeneradorService.generarPDF(ConstantesReporteSGI.REP_COBRO_CARTERA_ACTIVA, parameters, listaTipoCA,"ReporteUpas_"+tipo+"_"+FuncionesHelper.fechaToString(new Date()));
			} else {
				generarReporteCarteraActivaTipoExcel(listaTipoCA,tipo);
			}
	}
	
	/**
	 * Reporte total de deuda pendiente por upa de cartera por número de cartera.
	 **/
	public void generarReporteDeudorPagadorRentaPendientesCA(String tipoarchivo) {
		Double sumasoles=0.0,sumadolares=0.0;
		List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
		
		
		Calendar Hasta = Calendar.getInstance();
		Calendar Desde = Calendar.getInstance();

		Hasta.setTime(fechaconsultacarteraactiva);
		Desde.setTime(fechaconsultacarteraactiva);

		Desde.add(Calendar.DAY_OF_YEAR, -365);
		
		
		listaCA=reporteRecaudacionService.determinarCarteraActiva(carteraActivaReporteCartera,FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));

		if (obtenerTipoCambioDia()) {

		List<UpaPagadorDeudorBean> listaTipoCA= new ArrayList<UpaPagadorDeudorBean>();
		
		for (UpaPagadorDeudorBean condicionCobro: listaCA) {

			condicionCobro.setObservacion("ok");
			
			try {
			
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en idcondicion="+condicionCobro.getCondicion()+"-"+e.getMessage());
				condicionCobro.setObservacion("fail");
			}
			
			/** por cobrar que no hayan realizado un pago el mes vigente*/
				List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
				List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
						
						/**Exceptuando aquellos que no pagaron el mes vigente pero que ya han pagado rentas adelantadas.*/
						
					if (listaPendientesSinContratoCuotaBean.size()!=0 || listaPendientesContratoPreContratoCuotaBean.size()!=0 || !condicionCobro.getSicuotascanceladas() ) {
						condicionCobro.setResumendeuda(reporteRecaudacionService.resumenPeriodosPendientesCondicion(condicionCobro.getIdcontrato(),condicionCobro.getCondicion()));
						if (FechaUtil.getYear(Hasta.getTime()).equals(condicionCobro.getAnio().toString()) && Integer.parseInt(FechaUtil.getMonth(Hasta.getTime())) >Integer.parseInt(Almanaque.mesanumero(condicionCobro.getMes()))) {
							if (condicionCobro.getMoneda().equals("S")) {
								sumasoles=sumasoles+condicionCobro.getRenta();
							} else {
								sumadolares=sumadolares+condicionCobro.getRenta();
							}
							
							listaTipoCA.add(condicionCobro);
						}
						
						if(Integer.parseInt(FechaUtil.getYear(Hasta.getTime()))>condicionCobro.getAnio()) {
							if (condicionCobro.getMoneda().equals("S")) {
								sumasoles=sumasoles+condicionCobro.getRenta();
							} else {
								sumadolares=sumadolares+condicionCobro.getRenta();
							}
							listaTipoCA.add(condicionCobro);
						}
					
					}else {/**Todas las condiciones cuyo ultimo pago es considerada dentro de la cartera activa  y completaron sus cuotas, ya tienen una nueva condicion y debe salir la informacion en el reporte asi no tenga ultimo pago 
						para poder cobrar**/
					CondicionBean condicionBean= new CondicionBean();
					condicionBean=contratoService.obtenerUltimaCondicion(condicionCobro.getIdupa());
					
					if (condicionBean!=null && condicionBean.getIdcontrato()!=condicionCobro.getIdcontrato()) {
						condicionCobro.setResumendeuda(reporteRecaudacionService.resumenPeriodosPendientesCondicion(condicionBean.getIdcontrato(),condicionBean.getCondicion()));
						listaTipoCA.add(condicionCobro);
					}
		   }
		}
 
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
		   parameters.put("NROCARTERA",carteraActivaReporteCartera.equals("")?"TODOS":carteraActivaReporteCartera.toString());
		   parameters.put("TIPOCARTERA","ACTIVA");

		   parameters.put("FECHACANCELACION",FuncionesHelper.fechaToString(Hasta.getTime()));


		   if (tipoarchivo.equals("pdf")) {
				   report = reporteGeneradorService.generarPDF(ConstantesReporteSGI.REP_COBRO_INTERV_PENDIENTES_CARTERA_ACTIVA, parameters, listaTipoCA,"ReporteUpasCA_RentaPendientes_"+FuncionesHelper.fechaToString(new Date()));
			} else {
				generarReporteDeudorPagadorExcel(listaTipoCA);
			}
	}
	}

	/**
	 * Reporte de upas cobradas,por cobrar,al día, de cartera por número de cartera.
	 */
public void generarReporteDeudorPagadorCP(String tipo, String nombrecartera,String tipoarchivo) {
	

		Double sumasoles=0.0,sumadolares=0.0;
		List<UpaPagadorDeudorBean> listaTipoCP= new ArrayList<UpaPagadorDeudorBean>();
		
		Calendar Hasta = Calendar.getInstance();
		Calendar Desde = Calendar.getInstance();

//		Hasta.setTime(fechaconsultacarteraactiva);
//		Desde.setTime(fechaconsultacarteraactiva);

		Desde.add(Calendar.DAY_OF_YEAR, -365);
		
		
			
			List<CondicionBean> listaCondicionesVigentes=new ArrayList<CondicionBean>();
			listaCondicionesVigentes=contratoService.obtenerCondicionesVigentes(carteraPesadaReporteCartera);
			
			
			for (CondicionBean condicion : listaCondicionesVigentes) {
				
				UpaPagadorDeudorBean upaPagadorDeudorBean=reporteRecaudacionService.obtenerInformacionUltimoPagoRentaxCondicion(condicion.getIdcontrato(),FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));
				if (upaPagadorDeudorBean==null) {/**No se encontro resultado dentro de intervalo**/
					
					upaPagadorDeudorBean=reporteRecaudacionService.obtenerInformacionUltimoPagoRentaxCondicion(condicion.getIdcontrato(),"01/01/1900",FuncionesHelper.fechaToString(Hasta.getTime()));
					if(upaPagadorDeudorBean!=null){
						
						upaPagadorDeudorBean.setObservacion("ok");
						List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
						List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
						
						try {
							if (condicion.getCondicion().equals("Sin Contrato")) {
								listaPendientesSinContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Sin Contrato", "pendientes",tipcambio);
							} else {
								listaPendientesContratoPreContratoCuotaBean=reporteRecaudacionService.obtenerInformacionCobroCondicionTipo(condicion.getIdcontrato(),"Contrato || Pre Contrato","pendientes",tipcambio);
							}
						
						} catch (NullPointerException e) {
							System.out.println(e.getMessage());
							upaPagadorDeudorBean.setObservacion("fail");
						}
							
							upaPagadorDeudorBean.setPrimeraRentaPendiente(upaPagadorDeudorBean.getCondicion().equals("Sin Contrato")?listaPendientesSinContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesSinContratoCuotaBean.get(0).getAnio():listaPendientesContratoPreContratoCuotaBean.get(0).getMes().substring(0,3)+"-"+listaPendientesContratoPreContratoCuotaBean.get(0).getAnio());
							upaPagadorDeudorBean.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(upaPagadorDeudorBean.getIdcontrato()));
							listaTipoCP.add(upaPagadorDeudorBean);
						
					}
				}
						
		}
			
			
			/**Sumando los totales en dolares y soles de las lista resultado*/
			for (UpaPagadorDeudorBean upaPagadorDeudorBean : listaTipoCP) {
				if (upaPagadorDeudorBean.getMoneda().equals("S")) {
					sumasoles=sumasoles+upaPagadorDeudorBean.getRenta();
				} else {
					sumadolares=sumadolares+upaPagadorDeudorBean.getRenta();
				}
			}
		
		   String  reportPath=  "reporteUpaCarteraPaganteDeudor";
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO
		   parameters.put("CARTERA",carteraPesadaReporteCartera.equals("")?"TODOS":carteraPesadaReporteCartera.toString());
		   parameters.put("TIPOCONSULTA",tipo);
		   parameters.put("TOTALSOLES",sumasoles);
		   parameters.put("TOTALDOLARES",sumadolares);

		   parameters.put("FECHACANCELACION",FuncionesHelper.fechaToString(Hasta.getTime()));


		   if (tipoarchivo.equals("pdf")) {
				   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaTipoCP,"ReporteUpas_"+tipo+"_"+FuncionesHelper.fechaToString(new Date()));
		   } else {
				generarReporteDeudorPagadorExcel(listaTipoCP);
			}	
}

/**
 * Reporte de cartera activa especificando monto total de deuda
 **/
public void generarReporteDeudaCA(String tipoarchivo) {
	
	List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
	
	Calendar Hasta = Calendar.getInstance();
	Calendar Desde = Calendar.getInstance();
	
	Hasta.setTime(fechaReporteGerencialCA);
	Desde.setTime(fechaReporteGerencialCA);

	Desde.add(Calendar.DAY_OF_YEAR, -365);
	
	
	listaCA=reporteRecaudacionService.determinarCarteraActiva("",FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));

	if (obtenerTipoCambioDia()) {

	
	for (UpaPagadorDeudorBean condicionCobro: listaCA) {

		condicionCobro.setObservacion("ok");
		condicionCobro.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(condicionCobro.getIdcontrato()));
			
			try {
				List<CuotaBean> listaCuotaaPagar = new ArrayList<CuotaBean>();
				List<CuotaBean> listaCuotaPagado= new ArrayList<CuotaBean>();
				
				listaCuotaaPagar=reporteRecaudacionService.listarRentaMensualAPagarCondicionxIntervalo(condicionCobro.getIdcontrato(),Desde.getTime(),Hasta.getTime());
				listaCuotaPagado=reporteRecaudacionService.listarRentaMensualPagadoCondicionxIntervalo(condicionCobro.getIdcontrato(),Desde.getTime(),Hasta.getTime(),condicionCobro.getMoneda());
				
				
				
				Double totalapagar=reporteRecaudacionService.obtenerSumaTotalCuotas(listaCuotaaPagar);
				Double totalpagado=reporteRecaudacionService.obtenerSumaTotalCuotas(listaCuotaPagado);
				
				
				
				Integer mesesPagado = 0;
				Integer mesesApagar=listaCuotaaPagar.size();
				
				for (CuotaBean apagar : listaCuotaaPagar) {
					for (CuotaBean pagado: listaCuotaPagado) {
						if (apagar.getAnio()==pagado.getAnio() && apagar.getMes().equals(pagado.getMes())) {

							if (pagado.getSipagado().equals("1")) {
								mesesPagado=mesesPagado+1;
							}
						}
					}
				}
				
				Double deudatotal=totalapagar-totalpagado;
				condicionCobro.setMesesdeuda(mesesApagar-mesesPagado);
				condicionCobro.setDeudatotal(deudatotal<0.0?0.0:deudatotal);
				
				
				if (condicionCobro.getMesesdeuda()>1) {
					condicionCobro.setSituacion("MOROSO");
				
				}else {
					condicionCobro.setSituacion("AL DÍA");
				}

				
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				condicionCobro.setObservacion("fail");
			}
	}


	   String  reportPath=  ConstantesReporteSGI.REP_DEUDA_TOTAL_CARTERA_ACTIVA;
	   Map<String, Object> parameters = new HashMap<String, Object>();
	   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
	   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO


	   if (tipoarchivo.equals("pdf")) {
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaCA,"ReporteUpas_"+""+"_"+FuncionesHelper.fechaToString(new Date()));
			   
	   } else {
		   generarReporteDeudorPagadorExcel(listaCA);
		}
	}else{
		addWarnMessage("", "El tipo de cambio del dia no se ha registrado en el sistema SGI. ");
	}
}

/**
 * Reporte de Upas a una fecha especifica, pendientes de cobro para el ejercicio 2015
 **/
public void generarReporteUpasPendienteAnio(String tipoarchivo) {
	
	List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
	
	Calendar Hasta = Calendar.getInstance();
	Calendar Desde = Calendar.getInstance();
	
	Desde.add(Calendar.DAY_OF_YEAR, -365);
	Hasta.set(Calendar.MONTH, 06);
	Hasta.set(Calendar.YEAR, 2016);
	Hasta.set(Calendar.DATE, 13);
	
	
	
	listaCA=reporteRecaudacionService.determinarCarteraActiva("",FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));

	if (obtenerTipoCambioDia()) {

	ArrayList<CuotaBean> resultado= new ArrayList<CuotaBean>();
	
	
	for (UpaPagadorDeudorBean condicionCobro: listaCA) {

		condicionCobro.setObservacion("ok");
		
		List<CuotaBean> listaAPagar= new ArrayList<CuotaBean>();
		List<CuotaBean> listaPagado= new ArrayList<CuotaBean>();
		
		
		listaPagado=reporteRecaudacionService.listarRentaMensualPagadoCondicionxIntervalo(condicionCobro.getIdcontrato(),Desde.getTime(),Hasta.getTime(),condicionCobro.getMoneda());
		listaAPagar=reporteRecaudacionService.listarRentaMensualAPagarCondicionxIntervalo(condicionCobro.getIdcontrato(), Desde.getTime(), Hasta.getTime());
		
		
		double resto=0.0;
		for (CuotaBean apagar : listaAPagar) {
			boolean encontrado=false;
			for (CuotaBean pagado: listaPagado) {
				if (apagar.getMes().equals(pagado.getMes()) && apagar.getAnio()==pagado.getAnio()) {
					
						resto=FuncionesHelper.redondear(apagar.getMonto()-pagado.getMonto(), 2);	
					if (pagado.getSipagado().equals("1")) {
						encontrado=true;	
					}else {
						apagar.setMonto(resto);
						encontrado=false;
					}
					
				}
			}
			
			if (!encontrado) {
				apagar.setMoneda(condicionCobro.getMoneda());
				apagar.setInquilino(condicionCobro.getNombreInquilino());
				resultado.add(apagar);
			}
		}
	}
	generarReporteUpasPendienteAnio(resultado);

	}
}


public void generarReporteUpasPendienteAnio(List<CuotaBean> lista)
{
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("Página 1");

	        int rownum = 0;
	        for (int i = 0; i < lista.size(); i++) {
	            Row row = sheet.createRow(rownum++);
	            CuotaBean bean= new CuotaBean();
	           bean= lista.get(i);

	                for (int j = 0; j < 7; j++) {
	                	Cell cell = row.createCell(j);
	                	if (j==0)
	                		cell.setCellValue((String)bean.getCondicion());
	                	 else if (j==1)
	                		 cell.setCellValue((String)bean.getClaveupa());
	                		 else if (j==2)
	                			 cell.setCellValue((String)bean.getMes());
	                			 else if (j==3)
	                				 cell.setCellValue((Integer)bean.getAnio());
	                			 else if (j==4)
	                				 cell.setCellValue((Double)bean.getMonto());
	                			 	else if (j==5)
	                			 		cell.setCellValue((String)bean.getMoneda());
	                			 	else if(j==6)
	                			 		cell.setCellValue((String)bean.getInquilino());
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
	            
				report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "ReporteDetalladoMensualPendientePago"+FuncionesHelper.fechaToString(new Date())+".xls");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
}


/**
 * Reporte de cartera activa especificando monto total de deuda
 **/
public void generarReporteDeuda(String tipoarchivo) {
	
	List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
	
	
	Calendar Hasta = Calendar.getInstance();
	Calendar Desde = Calendar.getInstance();

	Hasta.setTime(fechaconsultacarteraactiva);
	Desde.setTime(fechaconsultacarteraactiva);

	Desde.add(Calendar.DAY_OF_YEAR, -365);
	
	
	listaCA=reporteRecaudacionService.determinarCarteraActiva("",FuncionesHelper.fechaToString(Desde.getTime()),FuncionesHelper.fechaToString(Hasta.getTime()));

	if (obtenerTipoCambioDia()) {

	
	for (UpaPagadorDeudorBean condicionCobro: listaCA) {


		condicionCobro.setObservacion("ok");
		condicionCobro.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(condicionCobro.getIdcontrato()));

			
			if (condicionCobro.getCondicion().equals("Contrato")) {//Para incluir la renta real contrato
				condicionCobro.setRenta(reporteRecaudacionService.obtenerRentaActualCondicion(condicionCobro.getIdcontrato()));
			}
			
			try {
				Map<String,String> datos=reporteRecaudacionService.obtenerDatosDeudaCobroUPA(condicionCobro.getIdupa(), tipcambio);
				
				condicionCobro.setMesesdeuda(Integer.parseInt(datos.get("MESESDEUDA")));
				condicionCobro.setDeudatotal(Double.valueOf(datos.get("VALORDEUDA")));
				
				if (condicionCobro.getMesesdeuda()>1) {
					condicionCobro.setSituacion("MOROSO");
				
				}else {
					condicionCobro.setSituacion("AL DÍA");
				}
			} catch (Exception e) {
				condicionCobro.setObservacion("fail");
			}


	
	}


	   String  reportPath=  "Gerencial/9report";
	   Map<String, Object> parameters = new HashMap<String, Object>();
	   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
	   parameters.put("TIPOCAMBIO",tipcambio);//PARA MODIFICACION TIPO CAMBIO

	   parameters.put("FECHACANCELACION",FuncionesHelper.fechaToString(Hasta.getTime()));


	   if (tipoarchivo.equals("pdf")) {
			   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaCA,"ReporteUpas_"+""+"_"+FuncionesHelper.fechaToString(new Date()));
	   } else {
		   generarReporteDeudorPagadorExcel(listaCA);
		}
	}
}

	public void generarReporteCarteraActivaTipoExcel(List<UpaPagadorDeudorBean> lista,String nombretiporeporte){
	
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
	        
	        /**Insercion de cabecera (rownum=0) **/
	        CellStyle csBold = null;
	        
	        Font bold = workbook.createFont();
	        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        bold.setFontHeightInPoints((short) 10);
	        
	        csBold = workbook.createCellStyle();
	        csBold.setBorderBottom(CellStyle.BORDER_THIN);
	        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	        csBold.setFont(bold);
	        
	        String[] headers = new String[] { "CLAVE", "DIRECCION", "DISTRITO","INQUILINO","CONDICION","RENTA","MONEDA","ÚLT. FECHA PAGO","COMPROBANTE","MES","AÑO" };
	
	        Row r = sheet.createRow(0);
	        for (int rn=0; rn<headers.length; rn++) {
	           Cell cell = r.createCell(rn);
	           cell.setCellValue(headers[rn]);
	           cell.setCellStyle(csBold);
	        }
	        
	        
	        
	        /**Insercion de datos **/
	        int rownum = 1;
	        for (int i = 0; i < lista.size(); i++) {
	        	
	           Row row = sheet.createRow(rownum++);
	           UpaPagadorDeudorBean bean= new UpaPagadorDeudorBean();
	           bean= lista.get(i);
	           
	           	try{
	                for (int j = 0; j < 11; j++) {
	                	Cell cell = row.createCell(j);
	                	if (j==0)
	                		cell.setCellValue((String)bean.getClaveUpa());
	                	 else if (j==1)
	                		 cell.setCellValue((String)bean.getDireccion());
	                		 else if (j==2)
	                			 cell.setCellValue((String)bean.getDistrito());
	                			 else if (j==3)
	                				 cell.setCellValue((String)bean.getNombreInquilino());
		                			 	else if (j==4)
		                					 cell.setCellValue((String)bean.getCondicion());
			                					 else if (j==5)
			                						 cell.setCellValue((Double)bean.getRenta()); 
					                					 else if (j==6)
					                						 cell.setCellValue((String)bean.getMoneda());
						                						 else if (j==7)
						                							 cell.setCellValue(bean.getFecCancelacion()!=null?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):null);
								                						 else if (j==8)
								                							 cell.setCellValue((String)bean.getNroseriecomprobante());
										                						 else if (j==9)
										                							 cell.setCellValue((String)bean.getMes());
											                						 else if (j==10)
											                							 cell.setCellValue(bean.getAnio());
					}
	           	} catch (Exception e) {
		        	   System.out.println("Controlando con excepcion para continuar y no caer Cartera Activa, error en : "+bean.getClaveUpa());
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
	        	
				report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "ReporteCarteraActivaTipo_"+nombretiporeporte+Constantes.EXTENSION_FORMATO_XLS);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		        
	}
	
	public void generarReporteDeudorPagadorExcel(List<UpaPagadorDeudorBean> lista)
	{
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
		        /**Insercion de cabecera (rownum=0) **/
		        CellStyle csBold = null;
		        
			      //Bold Fond
			        Font bold = workbook.createFont();
			        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			        bold.setFontHeightInPoints((short) 11);
			        
			        csBold = workbook.createCellStyle();
			        csBold.setBorderBottom(CellStyle.BORDER_THIN);
			        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			        csBold.setFont(bold);
			        
			        String[] headers = new String[] { "CLAVE", "DIRECCIÓN", "DISTRITO","INQUILINO","USO","CONDICIÓN","MESES DEUDA","MONEDA","RENTA","DEUDA TOTAL","FECHA ÚLT. PAGO","NRO. COMPROBANTE","MES","AÑO" };

			        Row r = sheet.createRow(0);
			        for (int rn=0; rn<headers.length; rn++) {
			           Cell cell = r.createCell(rn);
			           cell.setCellValue(headers[rn]);
			           cell.setCellStyle(csBold);
			        }
			        /**Insercion de datos (rownum=1) **/
		        int rownum = 1;
		        for (int i = 0; i < lista.size(); i++) {
		            Row row = sheet.createRow(rownum++);
		           UpaPagadorDeudorBean bean= new UpaPagadorDeudorBean();
		           bean= lista.get(i);
		           
		          
		           try {
					
		                for (int j = 0; j < 15; j++) {
		                	Cell cell = row.createCell(j);
		                	if (j==0)
		                		cell.setCellValue((String)bean.getClaveUpa());
		                	 else if (j==1)
		                		 cell.setCellValue((String)bean.getDireccion());
		                		 else if (j==2)
		                			 cell.setCellValue((String)bean.getDistrito());
		                			 else if (j==3)
		                				 cell.setCellValue((String)bean.getNombreInquilino());
			                			 else if (j==4)
			                				 cell.setCellValue((String)bean.getUso());
			                			 	else if (j==5)
			                					 cell.setCellValue((String)bean.getCondicion());
					                			 	else if (j==6)
					                					 cell.setCellValue((Integer)bean.getMesesdeuda());
						                			 	 else if (j==7)
					                						 cell.setCellValue((String)bean.getMoneda());
						                					 else if (j==8)
						                						 cell.setCellValue((Double)bean.getRenta());
										        					 else if (j==9)
										        						 cell.setCellValue((Double)bean.getDeudatotal());
										                						 else if (j==10)
										                							 cell.setCellValue(bean.getFecCancelacion()!=null?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):null);
												                						 else if (j==11)
												                							 cell.setCellValue((String)bean.getNroseriecomprobante());
													                						 else if (j==12)
													                							 cell.setCellValue((String)bean.getMes());
															                					 else if (j==13)
														                							 cell.setCellValue(bean.getAnio());
						}
		           } catch (Exception e) {
		        	   System.out.println("Controlando con excepcion para continuar y no caer, error en : "+bean.getClaveUpa());
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
		            
		            
		        	
					report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "ReporteListaUpasDeudaActiva_"+FuncionesHelper.fechaToString(new Date())+".xls");
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		        
	}
	
	public void generarReporteUpaDeudaTipoExcel(List<CondicionBean> lista,String nombretiporeporte,String tipomoneda)throws FileNotFoundException {
		
//		HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Página 1");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet=workbook.createSheet("Página 1");
        sheet.setColumnWidth(0, 6000); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);

        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        
        String[] headers = new String[] {"NOMBRE INQUILINO" ,"UPA","PERIODO UBICACION", "VENCE","FIN","EMISION","EMISION2",};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
             
        /**Insercion de datos **/
        int rownum = 1;
        System.out.println("lista.size()="+lista.size());
        CuotaBean cuota=new CuotaBean();
        CondicionBean bean= new CondicionBean();
       
        for (int i = 0; i < lista.size(); i++) {
        	
           bean=null;           
           bean= lista.get(i);
           for (int K = 0; K < lista.get(i).getCuotas().size(); K++) {
        	   //System.out.println("rownum"+rownum);
//        	   if(rownum+1>=65537){
//        		   break;
//        	   }
        	   
        	   Row row = sheet.createRow(rownum++);
        	   
        	   cuota=null;
        	   cuota= lista.get(i).getCuotas().get(K);
           
           	try{
                for (int j = 0; j < 7; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getNombresInquilino());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getClaveUpa());
                		 else if (j==2)
                			 cell.setCellValue((String)("MES: "+Almanaque.mesanumero(cuota.getMes())+"-"+cuota.getAnio()+"-"+bean.getDireccion()));
		                					 else if (j==3)
		                						 {
		                						 Calendar finCobro = new GregorianCalendar();
		                						finCobro.set(Calendar.YEAR,cuota.getAnio());
		                						finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(cuota.getMes()))-1);
		                						finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		                						
		                						cell.setCellValue(finCobro.getTime()!=null?(String)FuncionesHelper.fechaToString(finCobro.getTime()):"");
		                					 		}
				                					 else if (j==4)
				                						 cell.setCellValue(bean.getFincobro()!=null?(String)FuncionesHelper.fechaToString(bean.getFincobro()):"");
					                						 else if (j==5){
					                							 bean.setSidetraccion(bean.getSidetraccion()==null? false : bean.getSidetraccion());
					                							 if(bean.getSidetraccion()){
					                								 //cell.setCellValue((Double)FuncionesHelper.redondearNum(cuota.getNuevarenta()*0.9,2));
					                								 cell.setCellValue((Double)FuncionesHelper.redondearNum((cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0)*0.9,2));
					                							 }else{
					                								// cell.setCellValue((Double)cuota.getNuevarenta());
					                								 cell.setCellValue((Double)(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0));
					                							 }
					                						 		}
//					                							 cell.setCellValue(bean.getFecCancelacion()!=null?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):null);
							                						 else if (j==6)
							                							 // cell.setCellValue((Double)cuota.getNuevarenta());
                														  cell.setCellValue((Double)(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0));
//									                						 else if (j==9)
//									                							 cell.setCellValue((Double)cuota.getMonto());
//										                						 else if (j==10)
//										                							 cell.setCellValue((String)bean.getCondicion());
//										                						 else if(j==11)
//										                							 cell.setCellValue((String)bean.getEstado());
//										                						 		else if (j==12)
//										                						 			cell.setCellValue((String)bean.getMoneda());
				}
           	} catch (Exception e) {
	        	   System.out.println("Controlando con excepcion para continuar y no caer en Reporte de Deuda, error en : "+bean.getClaveUpa());
				} 
		}}

       try {
        		
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		
        	try {
        		
        	    workbook.write(bos);
        	
        	} finally {
        	    bos.close();
        	}
        	
            InputStream stream;
            stream = new ByteArrayInputStream(bos.toByteArray());
        	
			report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Deuda_Contrato_SGI_"+nombretiporeporte+"_"+tipomoneda+"_"+FuncionesHelper.fechaToString(new Date())+Constantes.EXTENSION_FORMATO_XLS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	        
}
public void generarReporteUpaDeudaTipoExcel2(List<CondicionBean> lista,String nombretiporeporte,String tipomoneda)throws FileNotFoundException {
		
//		HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Página 1");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet=workbook.createSheet("Página 1");
        sheet.setColumnWidth(0, 6000); 
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(7, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 3000);
        sheet.setColumnWidth(11, 2500);
        sheet.setColumnWidth(12, 1000);
        
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        
        String[] headers = new String[] {"NOMBRE INQUILINO" ,"UPA","MES","AÑO", "DIRECCION", "VENCE","FIN","RENTA MENSUAL","EMISION","EMISION2","CONDICION","ESTADO","MONEDA(S/D)"};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
             
        /**Insercion de datos **/
        int rownum = 1;
        System.out.println("lista.size()="+lista.size());
        CuotaBean cuota=new CuotaBean();
        CondicionBean bean= new CondicionBean();
       
        for (int i = 0; i < lista.size(); i++) {
        	
           bean=null;           
           bean= lista.get(i);
           for (int K = 0; K < lista.get(i).getCuotas().size(); K++) {
        	  // System.out.println("rownum"+rownum);
//        	   if(rownum+1>=65537){
//        		   break;
//        	   }
        	   
        	   Row row = sheet.createRow(rownum++);
        	   
        	   cuota=null;
        	   cuota= lista.get(i).getCuotas().get(K);
           
           	try{
                for (int j = 0; j < 13; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getNombresInquilino());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getClaveUpa());
                		 else if (j==2)
                			 cell.setCellValue((String)cuota.getMes());
                			 else if (j==3)
                				 cell.setCellValue(cuota.getAnio());
	                			 	else if (j==4)
	                					 cell.setCellValue((String)bean.getDireccion());
		                					 else if (j==5)
		                						 {
		                						 Calendar finCobro = new GregorianCalendar();
		                						finCobro.set(Calendar.YEAR,cuota.getAnio());
		                						finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(cuota.getMes()))-1);
		                						finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
//		                						finCobro.set(Calendar.HOUR,0);
//		                						finCobro.set(Calendar.MINUTE,0);
//		                						finCobro.set(Calendar.SECOND,0);
//		                						finCobro.set(Calendar.MILLISECOND,0);
		                						
		                						cell.setCellValue(finCobro.getTime()!=null?(String)FuncionesHelper.fechaToString(finCobro.getTime()):"");
		                					 		}
				                					 else if (j==6)
				                						 cell.setCellValue(bean.getFincobro()!=null?(String)FuncionesHelper.fechaToString(bean.getFincobro()):"");
				                					     else if (j==7)
			                							      cell.setCellValue((Double)cuota.getNuevarenta());
					                						    else if (j==8){
					                						    	bean.setSidetraccion(bean.getSidetraccion()==null? false : bean.getSidetraccion());
					                							    if(bean.getSidetraccion()){
					                								 cell.setCellValue((Double)FuncionesHelper.redondearNum((cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0)*0.9,2));
					                							    }else{
					                								 cell.setCellValue((Double)FuncionesHelper.redondearNum(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0,2));
					                							    }
					                						    }
//					                							 cell.setCellValue(bean.getFecCancelacion()!=null?(String)FuncionesHelper.fechaToString(bean.getFecCancelacion()):null);
							                						 else if (j==9)
							                							  cell.setCellValue((Double)(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0));
//									                						 else if (j==9)
//									                							 cell.setCellValue(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0);
										                						 else if (j==10)
										                							 cell.setCellValue((String)bean.getCondicion());
										                						 else if(j==11)
										                							 cell.setCellValue((String)bean.getEstado());
										                						 		else if (j==12)
										                						 			cell.setCellValue((String)bean.getMoneda());
				}
           	} catch (Exception e) {
	        	   System.out.println("Controlando con excepcion para continuar y no caer Reporte de deuda, error en : "+bean.getClaveUpa());
				} 
		}}

       try {
        		
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		
        	try {
        		
        	    workbook.write(bos);
        	
        	} finally {
        	    bos.close();
        	}
        	
            InputStream stream;
            stream = new ByteArrayInputStream(bos.toByteArray());
        	
			report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Deuda_Contrato_SGI_"+nombretiporeporte+"_"+tipomoneda+"_"+FuncionesHelper.fechaToString(new Date())+Constantes.EXTENSION_FORMATO_XLS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	        
}
public void generarReporteUpaDeudaTipoExcel3(List<CondicionBean> lista,String nombretiporeporte,String tipomoneda)throws FileNotFoundException {
	
//	HSSFWorkbook workbook = new HSSFWorkbook();
//    HSSFSheet sheet = workbook.createSheet("Página 1");
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet=workbook.createSheet("Página 1");
    sheet.setColumnWidth(0, 3000); 
    sheet.setColumnWidth(1, 6000);
    sheet.setColumnWidth(2, 6000);
    sheet.setColumnWidth(3, 2500);
    sheet.setColumnWidth(4, 3000);
    sheet.setColumnWidth(5, 3000);
    sheet.setColumnWidth(6, 3000);

    
    /**Insercion de cabecera (rownum=0) **/
    CellStyle csBold = null;
    
    Font bold = workbook.createFont();
    bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
    bold.setFontHeightInPoints((short) 10);
    
    csBold = workbook.createCellStyle();
    csBold.setBorderBottom(CellStyle.BORDER_THIN);
    csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    csBold.setFont(bold);
    
    String[] headers = new String[] {"CLAVE" ,"DIRECCIÓN","ARRENDATARIO", "MES","AÑO DE PAGO","MONTO",};

    Row r = sheet.createRow(0);
    for (int rn=0; rn<headers.length; rn++) {
       Cell cell = r.createCell(rn);
       cell.setCellValue(headers[rn]);
       cell.setCellStyle(csBold);
    }
         
    /**Insercion de datos **/
    int rownum = 1;
    System.out.println("lista.size()="+lista.size());
    CuotaBean cuota=new CuotaBean();
    CondicionBean bean= new CondicionBean();
   
    for (int i = 0; i < lista.size(); i++) {
    	
       bean=null;           
       bean= lista.get(i);
       for (int K = 0; K < lista.get(i).getCuotas().size(); K++) {
    	   //System.out.println("rownum"+rownum);
//    	   if(rownum+1>=65537){
//    		   break;
//    	   }
    	   
    	   Row row = sheet.createRow(rownum++);
    	   
    	   cuota=null;
    	   cuota= lista.get(i).getCuotas().get(K);
       
       	try{
            for (int j = 0; j < 6; j++) {
            	Cell cell = row.createCell(j);
            	if (j==0)
            		cell.setCellValue((String)bean.getClaveUpa());
            	else if (j==1)
           		 cell.setCellValue((String)bean.getDireccion());
            	else if (j==2)
            		 cell.setCellValue((String)bean.getNombresInquilino());
            	else if (j==3)
            		cell.setCellValue(Integer.parseInt(Almanaque.mesanumero(cuota.getMes())));
	            else if (j==4)
	                	{	                						
	                cell.setCellValue(cuota.getAnio());
	                	}
			    else if (j==5)
			        cell.setCellValue((Double)(cuota.getMonto()!=null?((Double)cuota.getMonto()):0.0));
				                					


			}
       	} catch (Exception e) {
        	   System.out.println("Controlando con excepcion para continuar y no caer en Reporte de Deuda, error en : "+bean.getClaveUpa());
			} 
	}}

   try {
    		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
    	try {
    		
    	    workbook.write(bos);
    	
    	} finally {
    	    bos.close();
    	}
    	
        InputStream stream;
        stream = new ByteArrayInputStream(bos.toByteArray());
    	
		report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Deuda_Contrato_SGI_"+nombretiporeporte+"_"+tipomoneda+"_"+FuncionesHelper.fechaToString(new Date())+Constantes.EXTENSION_FORMATO_XLS);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
        
}
	/*
	 * Reutilizables
	 */
    public void generarReporte(CondicionBean condicion){
    	try{
    	RequestContext contextRequest = RequestContext.getCurrentInstance();
    	tipoCambio=tipocambioService.getTipoCambio();
    	listaCondicionClausula = contratoService.listarCondicionIncumplimiento(condicion.getIdcontrato());
    	condicionClausula = new Condicionincumplimientoclausula();
    	
    	listaCondicionClausulaPenalidad = new ArrayList<Condicionincumplimientoclausula>();
    	
 	    Iterator<Condicionincumplimientoclausula> iter = listaCondicionClausula.iterator();
 			while (iter.hasNext()) {
 				Condicionincumplimientoclausula cond = iter.next();
 				if(cond.getDetalleclausula().getTipoclausula().getId_tc().equals(Constante.TIPO_CLAUSULA_INTERES_MORA)){
 					condicionClausula.setDetalleclausula(contratoService.getDetalleClausula(cond.getDetalleclausula().getId_detalle()));
 					interesMora = interesMoraService.obtenerUltimoInteresMora(String.valueOf(Integer.parseInt(cond.getDetalleclausula().getId_detalle())));
 					
 				}else{
 					listaCondicionClausulaPenalidad.add(cond);
 				}
 				
 			}
 	    generarReportePagos(condicion);
 		contextRequest.execute("dlgReporteDeudaInteresPenalidad.show();");
     	}catch(Exception e){
 			e.printStackTrace();
     	}
    	
    	
    }
    /**GENERAR LA VISTA  DE LIQUIDACION DE DEUDA RENTAS */
    public void generarReportePagos(CondicionBean condicionSeleccionado){
    	   listaCondicionContratoBean= new ArrayList<CondicionBean>();
		   List<RentaBean> listaRenta=new ArrayList<RentaBean>();
		   Boolean estadolista=false;
		   condicionSeleccionado.setTotal_renta(0.0);
		   condicionSeleccionado.setTotal_adeudado(0.0);
		   condicionSeleccionado.setTotal_penalidad_1(0.0);
		   condicionSeleccionado.setTotal_penalidad_2(0.0);
		   condicionSeleccionado.setTipomoneda(condicionSeleccionado.getMoneda().equalsIgnoreCase(Constante.TIPO_MONEDA_SOLES)? Constante.TIPO_MONEDA_SOLES_FORMATO:Constante.TIPO_MONEDA_DOLARES_FORMATO);
		   //if(condicionSeleccionado.getSinuevomantenimiento()!=null && condicionSeleccionado.getSinuevomantenimiento()){
			   listaRenta=reporteRecaudacionService.listarRentaPendientesxContrato(condicionSeleccionado.getIdcontrato(),condicionSeleccionado.getCondicion(),condicionSeleccionado.getSinuevomantenimiento());
			   //lista=reporteRecaudacionService.obtenerPeriodosPendientesDeuda(condicionSeleccionado.getIdcontrato());
			   Iterator<RentaBean> iter= listaRenta.iterator();
			while (iter.hasNext()) {
				RentaBean renta = iter.next();
				renta.setMesAnio(renta.getMes().substring(0, 3).toUpperCase()+"-"+renta.getAnio().substring(2,4));
				renta.setTipomoneda(condicionSeleccionado.getMoneda().equalsIgnoreCase(Constante.TIPO_MONEDA_SOLES)? condicionSeleccionado.getMoneda().toUpperCase()+"/." : "$");
				if(renta.getMontopagar()>0){
					renta.setSicancelado("0");
					
				}
				if(renta.getMontopagar()== 0.0){
					 iter.remove();
					
				}else{
			    
				renta.setInteremoratorio(FuncionesHelper.redondearNum(
						calculoInteresMoratorio(
								renta.getMontopagar(),
								condicionSeleccionado.getMoneda()
										.equalsIgnoreCase("S") ? interesMora
										.getTamn() : interesMora.getTamex(),
								calcularDiasEntreFechas(condicionSeleccionado,
										renta,
										Constante.TIPO_CLAUSULA_INTERES_MORA)),
						2));
				if (FuncionesHelper.diasEntreDosFechas(
						condicionSeleccionado.getFincontrato(), new Date()) > 0) {
					for (Condicionincumplimientoclausula penalidad : listaCondicionClausulaPenalidad) {
						if (penalidad
								.getDetalleclausula()
								.getId_detalle()
								.equalsIgnoreCase(
										Constante.PENALIDAD_DEMORA_PAGO_RENTA)) {
							if(renta.getNrodias_adeudo_penalidad()==0){
								renta.setPenalDemoraPago(0.0);
							}else{
							renta.setPenalDemoraPago(calculoPenalidadMoratoria(
									renta.getRenta(),
									penalidad.getValor_porcentaje(),
									renta.getNrodias_adeudo_penalidad(),
									penalidad.getEjecucion_periodica()));
							}
						}
						if (penalidad
								.getDetalleclausula()
								.getId_detalle()
								.equalsIgnoreCase(
										Constante.PENALIDAD_DEMORA_ENTREGA_INMUEBLE)) {
							if(renta.getNrodias_adeudo_penalidad()==0){
								renta.setPenalDemoraEntregaInmueble(0.0);
							}else{
								
							
							renta.setPenalDemoraEntregaInmueble(calculoPenalidadMoratoria(
									renta.getRenta(),
									penalidad.getValor_porcentaje(),
									renta.getNrodias_adeudo_penalidad(),
									penalidad.getEjecucion_periodica()));
							}
						}
					}
				}
				renta.setPenalDemoraEntregaInmueble(renta.getPenalDemoraEntregaInmueble()==null? 0.0 :renta.getPenalDemoraEntregaInmueble());
				renta.setPenalDemoraPago(renta.getPenalDemoraPago()==null? 0.0 :renta.getPenalDemoraPago());
				renta.setTotal_adeudado(FuncionesHelper.redondearNum(renta.getMontopagar()+renta.getInteremoratorio()+renta.getPenalDemoraPago()+ renta.getPenalDemoraEntregaInmueble(),2));
				condicionSeleccionado.setTotal_renta(FuncionesHelper.redondearNum( condicionSeleccionado.getTotal_renta()+  renta.getMontopagar(),2)); 
				condicionSeleccionado.setTotal_adeudado(FuncionesHelper.redondearNum( condicionSeleccionado.getTotal_adeudado()+renta.getTotal_adeudado(),2));
				condicionSeleccionado.setTotal_penalidad_1(FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(condicionSeleccionado.getTotal_penalidad_1(),renta.getPenalDemoraPago()),2));
				condicionSeleccionado.setTotal_penalidad_2(FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(condicionSeleccionado.getTotal_penalidad_2(),renta.getPenalDemoraEntregaInmueble()),2));
				
				}
				}
			   if(listaRenta!=null){
             	  if(listaRenta.size()>0){
             		  estadolista=true;         		  
             		  condicionSeleccionado.setRentas(listaRenta);
//             		  for (RentaBean renta : listaRenta) {
//						System.out.println("Mes : "+renta.getMes()+ " Anio : "+renta.getAnio()+" Renta Asignada :"+renta.getRenta()+" renta pendiente:"+ renta.getMontopagar()+" fecha Vencimiento :"+renta.getFechavencimiento()+" Dias Atraso :"+renta.getNrodias_adeudo()+" interes moratorio :"+ renta.getInteremoratorio()+" penalidad Moratoria :"+renta.getPenalDemoraPago()+" Penalidad Demora Inmueble :"+renta.getPenalDemoraEntregaInmueble()+" ");
//					}
             	  }
               }

//		   }else{
//			   System.out.println("Contrato anteriores");
//		   }
			   System.out.println("penalidad 1 =" +condicionSeleccionado.getTotal_penalidad_1());
		   condicionSeleccionadoDeuda=condicionSeleccionado;
		   listaCondicionContratoBean.add(condicionSeleccionado);/**Agregamos el objecto condicion a la lista que sera enviada al reporte**/
		   
		   /**Recorremos la lista de cuota para sumar determinar los totales**/
			  if(listaRenta!=null){
				  
				  if(listaRenta.size()>0){
				  
				  }			  
			  }	   
    }
    
    public Double calculoInteresMoratorio(Double renta,Double tasa,Integer dias){

    	return FuncionesHelper.redondearNum((Double)(((renta/1.18)*(tasa/100))/365)*dias ,2);
    }
    public Double calculoPenalidadMoratoria(Double renta , Double tasa ,Integer dias, String condicion){
    	if(condicion.equalsIgnoreCase("Diaria")){
    		return FuncionesHelper.redondearNum ( renta*(tasa/100)*dias,2);
    	}else{
    		return FuncionesHelper.redondearNum ( renta*(tasa/100) ,2);
    	}    	
    }
    public Integer calcularDiasEntreFechas(CondicionBean condicionBean , RentaBean renta ,String condicion){
    	 Long dias=null;
    	 String fecha;
    	 if(condicion.equalsIgnoreCase(Constante.PENALIDAD_DEMORA_ENTREGA_INMUEBLE)){
    		 if(condicionBean.getCondicion().equalsIgnoreCase(Constante.CONDICION_CONTRATO)){
    			 dias = FuncionesHelper.diasEntreDosFechas(condicionBean.getFincontrato(), new Date());
    		 }
    		 
    	 }
    	 if(condicion.equalsIgnoreCase(Constante.PENALIDAD_DEMORA_PAGO_RENTA)|| condicion.equalsIgnoreCase(Constante.TIPO_CLAUSULA_INTERES_MORA) ){
    		 fecha = condicionBean.getIdfechapago()+"/"+Almanaque.mesanumero(renta.getMes())+"/"+renta.getAnio();
             Date date=FuncionesHelper.convertirCadenaFecha(fecha);
        	 dias = FuncionesHelper.diasEntreDosFechas(date, new Date());
    	 }
    	 
    	return dias.intValue() ;
    }
	public void seleccionarTipoCambioxFecha()  {
		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(fechaconsultacarteraactiva));

	}
	public Boolean obtenerTipoCambioDia() {
			if (tipcambio==0.0) {
				
				return false;
			} else {
				return true;
			}
		}
    public void cambiarTipoConsultaUsuario(){
    	if(selectedSiUsuario){
    		selectedSiCobrador=false;
    		selectedSiUsuario=true;  		
    	}else{
    		selectedSiCobrador=true;
    		selectedSiUsuario=false;
    	}
    } 
    public void cambiarTipoConsultaCobrador(){
    	if(selectedSiCobrador){
    		selectedSiCobrador=true;
    		selectedSiUsuario=false;
    	}else{
    		selectedSiCobrador=false;
    		selectedSiUsuario=true;
    	}
    		
    }
    public void cambiarIncluirAnulados(){
    	if(selectedSiDocumentosAnulados){
    		selectedSiIncluirAnulados=true;
    	}else{
    		selectedSiIncluirAnulados=false;
    	}
    }
    public void setearUsuarioSeleccionado(){
    	System.out.println("seteando---");
    	System.out.println("usuarioselec==="+idUsuarioSeleccionado);
    	usuarioSeleccionado  =usuarioService.buscarUsuarioxId(Integer.parseInt(idUsuarioSeleccionado));
    	nombreusuario        =usuarioSeleccionado.getNombres()+" "+usuarioSeleccionado.getApellidopat();
    	nombreusuariocompleto=usuarioSeleccionado.getNombrescompletos();
    }
    /** GENERA EL REPORTE DE LIQUIDACION DE DEUDA DE RENTA */
    public void generarReporteLiquidacionDeuda(String tipoArchivo){    	
    	String  reporte= "" ;
    	String tipoReporte="Liquidacion_de_Deuda_Renta";
    	int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
    	Integer sirentas=0;
    	Boolean sipenalidadcontrato=false;
    	Tipocambio tipoCambio = tipocambioService.obtenerTipoCambio();
    	Double total_renta=0.0 , total_renta_pendiente=0.0,total_interes_moratorio=0.0,total_penalidad_1=0.0 , total_penalidad_2=0.0 ,total_deuda=0.0;
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	
    	 if (tipoArchivo.equals("pdf")) {
    		  for (CondicionBean cond : listaCondicionContratoBean) {
    			  sirentas= cond.getRentas().size();
				  for(RentaBean rentabean:cond.getRentas()){

					 total_renta =FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_renta ,rentabean.getRenta()),2);
					 total_renta_pendiente=FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_renta_pendiente,rentabean.getMontopagar()),2);
					 total_interes_moratorio=FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_interes_moratorio,rentabean.getInteremoratorio()),2);
					 total_penalidad_1=FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_penalidad_1,rentabean.getPenalDemoraPago()),2);
					 total_penalidad_2=FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_penalidad_2,rentabean.getPenalDemoraEntregaInmueble()),2);
					 total_deuda=FuncionesHelper.redondearNum(FuncionesHelper.sumarDouble(total_deuda, rentabean.getTotal_adeudado()),2);

					 if(rentabean.getSicancelado().equals("0")){
						 nropendiente++;
					 }else{
						 if(rentabean.getSicancelado().equals("1")){
							 nrocancelado++;
						 }else{
							 nropendiente++;
						 }
					 }
					
				}
				  System.out.println("dias ="+FuncionesHelper.diasEntreDosFechas(cond.getFincontrato(), new Date()));
				  sipenalidadcontrato=cond.getFincontrato()!=null? (FuncionesHelper.diasEntreDosFechas(cond.getFincontrato(), new Date())<=0? false:true ) : false;
			 }
    		   reporte="RECAUDACION_REP_LIQUIDACION_DEUDA_POR_RENTA.jrxml";
			   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			   parameters.put("TIPOCAMBIO",tipoCambio.getTipocambio()); 
			   parameters.put("NROCANCELADO",nrocancelado );
			   parameters.put("NROPENDIENTE",nropendiente);
			   parameters.put("TOTALRENTA",total_renta);
			   parameters.put("TOTALRENTAPENDIENTE",total_renta_pendiente);
			   parameters.put("TOTALINTERESMORATORIO",total_interes_moratorio);
			   parameters.put("TOTALPENALIDAD_1",total_penalidad_1);
			   parameters.put("TOTALPENALIDAD_2",total_penalidad_2);
			   parameters.put("TOTALDEUDA",total_deuda);
			   parameters.put("NRO_REPORTE", nro_reporte);
			   parameters.put("SIREGISTRADEUDA", sirentas==0?"FALSE":"TRUE");
			   parameters.put("NOMBRETASAINTERES", condicionClausula!=null ? condicionClausula.getDetalleclausula().getNombre() :"(NO INDICA)" );
			   parameters.put("VALORTASAINTERESMN", condicionClausula!=null ? interesMora.getTamn()+"%":"(NO INDICA)");
			   parameters.put("VALORTASAINTERESMEX", condicionClausula!=null ? interesMora.getTamex()+"%":"(NO INDICA)");
			   String valorpenalidad1="",valorpenalidad2="",tipoejecucion_1="",tipoejecucion_2="";
			   for( Condicionincumplimientoclausula cond  :listaCondicionClausulaPenalidad){
				   if(cond.getDetalleclausula().getId_detalle().equalsIgnoreCase("03")){
					   valorpenalidad1= cond.getValor_porcentaje()+"%"  ;
					   tipoejecucion_1= cond.getEjecucion_periodica();
				   }
				   	if(cond.getDetalleclausula().getId_detalle().equalsIgnoreCase("04")){
				   		valorpenalidad2=cond.getValor_porcentaje()+"%"  ;
				   	    tipoejecucion_2= cond.getEjecucion_periodica();
				   }
			   }
			   parameters.put("PENALIDAD_1",sipenalidadcontrato?( valorpenalidad1.length()>0? valorpenalidad1:"(NO INDICA)" ):"(NO APLICA)" );
			   parameters.put("PENALIDAD_MODO_1",sipenalidadcontrato?( valorpenalidad1.length()>0? "("+tipoejecucion_1+")":"" ):"" );
			   parameters.put("PENALIDAD_1",sipenalidadcontrato?( valorpenalidad1.length()>0? valorpenalidad1:"(NO INDICA)" ):"(NO APLICA)" );
			   parameters.put("PENALIDAD_2",sipenalidadcontrato? (valorpenalidad2.length()>0? valorpenalidad2:"(NO INDICA)" ):"(NO APLICA)" );
			   parameters.put("PENALIDAD_MODO_2",sipenalidadcontrato?( valorpenalidad1.length()>0? "("+tipoejecucion_2+")":"" ):"" );
			   

			   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionContratoBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
				   
		  } else {
			  		System.out.println("excel");
		  }
    	 System.out.println("report="+ report);
    	 System.out.println("fin proceeso");
    }
	public void setdesdecartera() {
		hastaCartera=desdeCartera;
	}
	public void setdesdeinquilino() {
		hastaInquilino=desdeInquilino;
	}
	public void setdesdecobrador() {
		hastaCobrador=desdeCobrador;
	}
	public void setdesdecontrato() {
		hastaInquilino=desdeInquilino;
	}
	public void setdesdedetraccion() {
		hastaDetraccion=desdeDetraccion;
	}
	public void setdesdeautodetraccion() {
		hastaAutoDetraccion=desdeAutoDetraccion;
	}
	public void setdesdeupa() {
		hastaUpa=desdeUpa;
	}
	public String getNrocartera() {
		return nrocartera;
	}
	public void setNrocartera(String nrocartera) {
		this.nrocartera = nrocartera;
	}

	public String getClaveupa() {
		return claveupa;
	}
	public void setClaveupa(String claveupa) {
		this.claveupa = claveupa;
	}
	public List<Cartera> getListacartera() {
		return listacartera;
	}
	public void setListacartera(List<Cartera> listacartera) {
		this.listacartera = listacartera;
	}
	public List<ComprobanteBean> getListacomprobantespago() {
		return listacomprobantespago;
	}
	public void setListacomprobantespago(List<ComprobanteBean> listacomprobantespago) {
		this.listacomprobantespago = listacomprobantespago;
	}
	public String getNombreinquilino() {
		return nombreinquilino;
	}
	public void setNombreinquilino(String nombreinquilino) {
		this.nombreinquilino = nombreinquilino;
	}
	public List<ComprobanteBean> getFiltrolistacomprobantespago() {
		return filtrolistacomprobantespago;
	}
	public void setFiltrolistacomprobantespago(List<ComprobanteBean> filtrolistacomprobantespago) {
		this.filtrolistacomprobantespago = filtrolistacomprobantespago;
	}
	public ComprobanteBean getComprobantepago() {
		return comprobantepago;
	}
	public void setComprobantepago(ComprobanteBean comprobantepago) {
		this.comprobantepago = comprobantepago;
	}
	public List<pe.gob.sblm.sgi.entity.Detallecuota> getListaDetalleComprobante() {
		return listaDetalleComprobante;
	}
	public void setListaDetalleComprobante(List<pe.gob.sblm.sgi.entity.Detallecuota> listaDetalleComprobante) {
		this.listaDetalleComprobante = listaDetalleComprobante;
	}
	public String getTipDocu() {
		return tipDocu;
	}
	public void setTipDocu(String tipDocu) {
		this.tipDocu = tipDocu;
	}
	public String getTipPago() {
		return tipPago;
	}
	public void setTipPago(String tipPago) {
		this.tipPago = tipPago;
	}
	public String getTipoMovi() {
		return tipoMovi;
	}
	public void setTipoMovi(String tipoMovi) {
		this.tipoMovi = tipoMovi;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public Date getDesdeCobrador() {
		return desdeCobrador;
	}
	public void setDesdeCobrador(Date desdeCobrador) {
		this.desdeCobrador = desdeCobrador;
	}
	public Date getHastaCobrador() {
		return hastaCobrador;
	}
	public void setHastaCobrador(Date hastaCobrador) {
		this.hastaCobrador = hastaCobrador;
	}
	public Date getDesdeCartera() {
		return desdeCartera;
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
	public Date getDesdeUpa() {
		return desdeUpa;
	}
	public void setDesdeUpa(Date desdeUpa) {
		this.desdeUpa = desdeUpa;
	}
	public Date getHastaUpa() {
		return hastaUpa;
	}
	public void setHastaUpa(Date hastaUpa) {
		this.hastaUpa = hastaUpa;
	}
	public Date getDesdeInquilino() {
		return desdeInquilino;
	}
	public void setDesdeInquilino(Date desdeInquilino) {
		this.desdeInquilino = desdeInquilino;
	}
	public Date getHastaInquilino() {
		return hastaInquilino;
	}
	public void setHastaInquilino(Date hastaInquilino) {
		this.hastaInquilino = hastaInquilino;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getTotaligv() {
		return totaligv;
	}
	public void setTotaligv(Double totaligv) {
		this.totaligv = totaligv;
	}
	public Double getTotaldetraccion() {
		return totaldetraccion;
	}
	public void setTotaldetraccion(Double totaldetraccion) {
		this.totaldetraccion = totaldetraccion;
	}
	public Date getHastaDetraccion() {
		return hastaDetraccion;
	}
	public void setHastaDetraccion(Date hastaDetraccion) {
		this.hastaDetraccion = hastaDetraccion;
	}
	public Date getDesdeDetraccion() {
		return desdeDetraccion;
	}
	public void setDesdeDetraccion(Date desdeDetraccion) {
		this.desdeDetraccion = desdeDetraccion;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public StreamedContent getReport() {
		return report;
	}
	public void setReport(StreamedContent report) {
		this.report = report;
	}
	public Boolean getSidetallecomprobantes() {
		return sidetallecomprobantes;
	}
	public void setSidetallecomprobantes(Boolean sidetallecomprobantes) {
		this.sidetallecomprobantes = sidetallecomprobantes;
	}
	public List<CondicionBean> getListaCobroCartera() {
		return listaCobroCartera;
	}
	public void setListaCobroCartera(List<CondicionBean> listaCobroCartera) {
		this.listaCobroCartera = listaCobroCartera;
	}
	public IContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public ArrayList<CuotaBean> getListaPendientesContratoPreContratoCuotaBean() {
		return listaPendientesContratoPreContratoCuotaBean;
	}
	public void setListaPendientesContratoPreContratoCuotaBean(ArrayList<CuotaBean> listaPendientesContratoPreContratoCuotaBean) {
		this.listaPendientesContratoPreContratoCuotaBean = listaPendientesContratoPreContratoCuotaBean;
	}
	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}
	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}
	public ArrayList<CuotaBean> getListaPendientesSinContratoCuotaBean() {
		return listaPendientesSinContratoCuotaBean;
	}
	public void setListaPendientesSinContratoCuotaBean(ArrayList<CuotaBean> listaPendientesSinContratoCuotaBean) {
		this.listaPendientesSinContratoCuotaBean = listaPendientesSinContratoCuotaBean;
	}
	public String getcarteraActivaReporteCartera() {
		return carteraActivaReporteCartera;
	}
	public void setcarteraActivaReporteCartera(String carteraActivaReporteCartera) {
		this.carteraActivaReporteCartera = carteraActivaReporteCartera;
	}

	public List<CondicionBean> getListaCondicionDeUpa() {
		return listaCondicionDeUpa;
	}

	public void setListaCondicionDeUpa(List<CondicionBean> listaCondicionDeUpa) {
		this.listaCondicionDeUpa = listaCondicionDeUpa;
	}


	public CondicionBean getCondicionSeleccionado() {
		return condicionSeleccionado;
	}

	public void setCondicionSeleccionado(CondicionBean condicionSeleccionado) {
		this.condicionSeleccionado = condicionSeleccionado;
	}

	public String getClaveupareporte() {
		return claveupareporte;
	}
	public void setClaveupareporte(String claveupareporte) {
		this.claveupareporte = claveupareporte;
	}

	public String getTipoReporteSeleccionadoTabUpa() {
		return tipoReporteSeleccionadoTabUpa;
	}
	public void setTipoReporteSeleccionadoTabUpa(String tipoReporteSeleccionadoTabUpa) {
		this.tipoReporteSeleccionadoTabUpa = tipoReporteSeleccionadoTabUpa;
	}
	public String getTipoReporteSeleccionadoTabCarteraActiva() {
		return tipoReporteSeleccionadoTabCarteraActiva;
	}
	public void setTipoReporteSeleccionadoTabCarteraActiva(String tipoReporteSeleccionadoTabCarteraActiva) {
		this.tipoReporteSeleccionadoTabCarteraActiva = tipoReporteSeleccionadoTabCarteraActiva;
	}

	public String getTipoReporteSeleccionadoTabCarteraPasiva() {
		return tipoReporteSeleccionadoTabCarteraPasiva;
	}
	public void setTipoReporteSeleccionadoTabCarteraPasiva(String tipoReporteSeleccionadoTabCarteraPasiva) {
		this.tipoReporteSeleccionadoTabCarteraPasiva = tipoReporteSeleccionadoTabCarteraPasiva;
	}
	public String getCarteraActivaReporteCartera() {
		return carteraActivaReporteCartera;
	}
	public void setCarteraActivaReporteCartera(String carteraActivaReporteCartera) {
		this.carteraActivaReporteCartera = carteraActivaReporteCartera;
	}
	public String getCarteraPesadaReporteCartera() {
		return carteraPesadaReporteCartera;
	}
	public void setCarteraPesadaReporteCartera(String carteraPesadaReporteCartera) {
		this.carteraPesadaReporteCartera = carteraPesadaReporteCartera;
	}
	public Date getFechaconsultacarteraactiva() {
		return fechaconsultacarteraactiva;
	}
	public void setFechaconsultacarteraactiva(Date fechaconsultacarteraactiva) {
		this.fechaconsultacarteraactiva = fechaconsultacarteraactiva;
	}
	public String getNrodocumento() {
		return nrodocumento;
	}
	public void setNrodocumento(String nrodocumento) {
		this.nrodocumento = nrodocumento;
	}
	public ItemBean getSelectedCobrador() {
		return selectedCobrador;
	}
	public void setSelectedCobrador(ItemBean selectedCobrador) {
		this.selectedCobrador = selectedCobrador;
	}
	public List<ItemBean> getListaCobradores() {
		return listaCobradores;
	}
	public void setListaCobradores(List<ItemBean> listaCobradores) {
		this.listaCobradores = listaCobradores;
	}
	public int getIdSelectedCobrador() {
		return idSelectedCobrador;
	}
	public void setIdSelectedCobrador(int idSelectedCobrador) {
		this.idSelectedCobrador = idSelectedCobrador;
	}
	public IUpaService getUpaService() {
		return upaService;
	}
	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public boolean isRenderedBotonDbf() {
		return renderedBotonDbf;
	}
	public void setRenderedBotonDbf(boolean renderedBotonDbf) {
		this.renderedBotonDbf = renderedBotonDbf;
	}
	public List<String> getSelectedComprobanteTabCobrador() {
		return selectedComprobanteTabCobrador;
	}
	public void setSelectedComprobanteTabCobrador(List<String> selectedComprobanteTabCobrador) {
		this.selectedComprobanteTabCobrador = selectedComprobanteTabCobrador;
	}
	public boolean isSelectedSiAnuladoTabCobrador() {
		return selectedSiAnuladoTabCobrador;
	}
	public void setSelectedSiAnuladoTabCobrador(boolean selectedSiAnuladoTabCobrador) {
		this.selectedSiAnuladoTabCobrador = selectedSiAnuladoTabCobrador;
	}
	public Date getFechaReporteGerencialCA() {
		return fechaReporteGerencialCA;
	}
	public void setFechaReporteGerencialCA(Date fechaReporteGerencialCA) {
		this.fechaReporteGerencialCA = fechaReporteGerencialCA;
	}
	public IReporteGeneradorService getReporteGeneradorService() {
		return reporteGeneradorService;
	}
	public void setReporteGeneradorService(IReporteGeneradorService reporteGeneradorService) {
		this.reporteGeneradorService = reporteGeneradorService;
	}
	public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
		return cobranzaRecaudacionService;
	}
	public void setCobranzaRecaudacionService(IRecaudacionCobranzaService cobranzaRecaudacionService) {
		this.cobranzaRecaudacionService = cobranzaRecaudacionService;
	}
	public IRecaudacionCarteraService getCarteraService() {
		return carteraService;
	}
	public void setCarteraService(IRecaudacionCarteraService carteraService) {
		this.carteraService = carteraService;
	}
	public IRecaudacionReporteService getReporteRecaudacionService() {
		return reporteRecaudacionService;
	}
	public void setReporteRecaudacionService(IRecaudacionReporteService reporteRecaudacionService) {
		this.reporteRecaudacionService = reporteRecaudacionService;
	}
	public Date getFechaReporteGerencial() {
		return fechaReporteGerencial;
	}
	public void setFechaReporteGerencial(Date fechaReporteGerencial) {
		this.fechaReporteGerencial = fechaReporteGerencial;
	}
	public Boolean getSelectedSiFacturacionElectronicaTabCobrador() {
		return selectedSiFacturacionElectronicaTabCobrador;
	}
	public void setSelectedSiFacturacionElectronicaTabCobrador(Boolean selectedSiFacturacionElectronicaTabCobrador) {
		this.selectedSiFacturacionElectronicaTabCobrador = selectedSiFacturacionElectronicaTabCobrador;
	}

	public Date getDesdeDocumento() {
		return desdeDocumento;
	}

	public void setDesdeDocumento(Date desdeDocumento) {
		this.desdeDocumento = desdeDocumento;
	}
	public void setDesdeDocumento() {
		hastaDocumento = desdeDocumento;
	}
	public Date getHastaDocumento() {
		return hastaDocumento;
	}

	public void setHastaDocumento(Date hastaDocumento) {
		this.hastaDocumento = hastaDocumento;
	}

	public String getClaveupaDocAdm() {
		return claveupaDocAdm;
	}

	public void setClaveupaDocAdm(String claveupaDocAdm) {
		this.claveupaDocAdm = claveupaDocAdm;
	}

	public boolean isRenderedBotonDbfDoc() {
		return renderedBotonDbfDoc;
	}

	public void setRenderedBotonDbfDoc(boolean renderedBotonDbfDoc) {
		this.renderedBotonDbfDoc = renderedBotonDbfDoc;
	}

	public String getObservacionElemSelecionadoc() {
		return observacionElemSelecionadoc;
	}

	public void setObservacionElemSelecionadoc(String observacionElemSelecionadoc) {
		this.observacionElemSelecionadoc = observacionElemSelecionadoc;
	}

	public List<ComprobanteBean> getListacomprobantespagoespecial() {
		return listacomprobantespagoespecial;
	}

	public void setListacomprobantespagoespecial(
			List<ComprobanteBean> listacomprobantespagoespecial) {
		this.listacomprobantespagoespecial = listacomprobantespagoespecial;
	}

	public String getTipoReporteSeleccionadoTabDeuda() {
		return tipoReporteSeleccionadoTabDeuda;
	}

	public void setTipoReporteSeleccionadoTabDeuda(
			String tipoReporteSeleccionadoTabDeuda) {
		this.tipoReporteSeleccionadoTabDeuda = tipoReporteSeleccionadoTabDeuda;
	}

	public String getTipoReporteSeleccionadoTabDeudaMoneda() {
		return tipoReporteSeleccionadoTabDeudaMoneda;
	}

	public void setTipoReporteSeleccionadoTabDeudaMoneda(
			String tipoReporteSeleccionadoTabDeudaMoneda) {
		this.tipoReporteSeleccionadoTabDeudaMoneda = tipoReporteSeleccionadoTabDeudaMoneda;
	}

	public boolean getRenderedReporteDetraccion() {
		return renderedReporteDetraccion;
	}

	public void setRenderedReporteDetraccion(boolean renderedReporteDetraccion) {
		this.renderedReporteDetraccion = renderedReporteDetraccion;
	}

	public Date getDesdeAutoDetraccion() {
		return desdeAutoDetraccion;
	}

	public void setDesdeAutoDetraccion(Date desdeAutoDetraccion) {
		this.desdeAutoDetraccion = desdeAutoDetraccion;
	}

	public Date getHastaAutoDetraccion() {
		return hastaAutoDetraccion;
	}

	public void setHastaAutoDetraccion(Date hastaAutoDetraccion) {
		this.hastaAutoDetraccion = hastaAutoDetraccion;
	}

	public boolean isRenderedReporteAutoDetraccion() {
		return renderedReporteAutoDetraccion;
	}

	public void setRenderedReporteAutoDetraccion(
			boolean renderedReporteAutoDetraccion) {
		this.renderedReporteAutoDetraccion = renderedReporteAutoDetraccion;
	}

	public String getEstadoupaseleccionado() {
		return estadoupaseleccionado;
	}

	public void setEstadoupaseleccionado(String estadoupaseleccionado) {
		this.estadoupaseleccionado = estadoupaseleccionado;
	}

	public Boolean getSelectedSiDocumentosAnulados() {
		return selectedSiDocumentosAnulados;
	}

	public void setSelectedSiDocumentosAnulados(Boolean selectedSiDocumentosAnulados) {
		this.selectedSiDocumentosAnulados = selectedSiDocumentosAnulados;
	}

	

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Boolean getSelectedSiUsuario() {
		return selectedSiUsuario;
	}

	public void setSelectedSiUsuario(Boolean selectedSiUsuario) {
		this.selectedSiUsuario = selectedSiUsuario;
	}

	public Boolean getSelectedSiCobrador() {
		return selectedSiCobrador;
	}

	public void setSelectedSiCobrador(Boolean selectedSiCobrador) {
		this.selectedSiCobrador = selectedSiCobrador;
	}

	public Boolean getSelectedSiIncluirAnulados() {
		return selectedSiIncluirAnulados;
	}

	public void setSelectedSiIncluirAnulados(Boolean selectedSiIncluirAnulados) {
		this.selectedSiIncluirAnulados = selectedSiIncluirAnulados;
	}
	public Boolean getSelectedSiIncluirRechazados() {
		return selectedSiIncluirRechazados;
	}

	public void setSelectedSiIncluirRechazados(Boolean selectedSiIncluirRechazados) {
		this.selectedSiIncluirRechazados = selectedSiIncluirRechazados;
	}
	public String getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(String idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Date getDesdeConsulta_Compr() {
		return desdeConsulta_Compr;
	}

	public void setDesdeConsulta_Compr(Date desdeConsulta_Compr) {
		this.desdeConsulta_Compr = desdeConsulta_Compr;
	}

	public Date getHastaConsulta_Compr() {
		return hastaConsulta_Compr;
	}

	public void setHastaConsulta_Compr(Date hastaConsulta_Compr) {
		this.hastaConsulta_Compr = hastaConsulta_Compr;
	}
    
	public void setConsulta_Compr() {
		hastaConsulta_Compr=desdeConsulta_Compr;
	}

	public Boolean getRenderedReporteDocumento() {
		return renderedReporteDocumento;
	}

	public void setRenderedReporteDocumento(Boolean renderedReporteDocumento) {
		this.renderedReporteDocumento = renderedReporteDocumento;
	}

	public String getNombreusuariologueado() {
		return nombreusuariologueado;
	}

	public void setNombreusuariologueado(String nombreusuariologueado) {
		this.nombreusuariologueado = nombreusuariologueado;
	}

	public CobranzaManagedBean getCobranzaMB() {
		return cobranzaMB;
	}

	public void setCobranzaMB(CobranzaManagedBean cobranzaMB) {
		this.cobranzaMB = cobranzaMB;
	}

	public String getDescripcionbaja() {
		return descripcionbaja;
	}

	public void setDescripcionbaja(String descripcionbaja) {
		this.descripcionbaja = descripcionbaja;
	}

	public boolean isSelectedSiRechazadoTabCobrador() {
		return selectedSiRechazadoTabCobrador;
	}

	public void setSelectedSiRechazadoTabCobrador(
			boolean selectedSiRechazadoTabCobrador) {
		this.selectedSiRechazadoTabCobrador = selectedSiRechazadoTabCobrador;
	}

	public List<ArchivoAdjuntoBean> getListaArchivosReferencia() {
		return listaArchivosReferencia;
	}

	public void setListaArchivosReferencia(
			List<ArchivoAdjuntoBean> listaArchivosReferencia) {
		this.listaArchivosReferencia = listaArchivosReferencia;
	}

	public AdministrarArchivoService getAdministrarArchivoService() {
		return administrarArchivoService;
	}

	public void setAdministrarArchivoService(
			AdministrarArchivoService administrarArchivoService) {
		this.administrarArchivoService = administrarArchivoService;
	}

	public ArchivoManagedBean getArchivoMB() {
		return archivoMB;
	}

	public void setArchivoMB(ArchivoManagedBean archivoMB) {
		this.archivoMB = archivoMB;
	}

	public String getClaveupaDeuda() {
		return claveupaDeuda;
	}

	public void setClaveupaDeuda(String claveupaDeuda) {
		this.claveupaDeuda = claveupaDeuda;
	}

	public List<String> getListaCondicionContratoxUpa() {
		return listaCondicionContratoxUpa;
	}

	public void setListaCondicionContratoxUpa(
			List<String> listaCondicionContratoxUpa) {
		this.listaCondicionContratoxUpa = listaCondicionContratoxUpa;
	}

	public List<Condicionincumplimientoclausula> getListaCondicionClausula() {
		return listaCondicionClausula;
	}

	public void setListaCondicionClausula(
			List<Condicionincumplimientoclausula> listaCondicionClausula) {
		this.listaCondicionClausula = listaCondicionClausula;
	}

	public Condicionincumplimientoclausula getCondicionClausula() {
		return condicionClausula;
	}

	public void setCondicionClausula(
			Condicionincumplimientoclausula condicionClausula) {
		this.condicionClausula = condicionClausula;
	}

	public List<Condicionincumplimientoclausula> getListaCondicionClausulaPenalidad() {
		return listaCondicionClausulaPenalidad;
	}

	public void setListaCondicionClausulaPenalidad(
			List<Condicionincumplimientoclausula> listaCondicionClausulaPenalidad) {
		this.listaCondicionClausulaPenalidad = listaCondicionClausulaPenalidad;
	}

	public Condicionincumplimientoclausula getCondicionClausulaSeleccionada() {
		return condicionClausulaSeleccionada;
	}

	public void setCondicionClausulaSeleccionada(
			Condicionincumplimientoclausula condicionClausulaSeleccionada) {
		this.condicionClausulaSeleccionada = condicionClausulaSeleccionada;
	}

	public IInteresMoraService getInteresMoraService() {
		return interesMoraService;
	}

	public void setInteresMoraService(IInteresMoraService interesMoraService) {
		this.interesMoraService = interesMoraService;
	}

	public InteresMora getInteresMora() {
		return interesMora;
	}

	public void setInteresMora(InteresMora interesMora) {
		this.interesMora = interesMora;
	}

	public ComprobanteBean getComprobantepagoExt() {
		return comprobantepagoExt;
	}

	public void setComprobantepagoExt(ComprobanteBean comprobantepagoExt) {
		this.comprobantepagoExt = comprobantepagoExt;
	}

	public CondicionBean getCondicionSeleccionadoDeuda() {
		return condicionSeleccionadoDeuda;
	}

	public void setCondicionSeleccionadoDeuda(
			CondicionBean condicionSeleccionadoDeuda) {
		this.condicionSeleccionadoDeuda = condicionSeleccionadoDeuda;
	}

	public RentaBean getRentaBean() {
		return rentaBean;
	}

	public void setRentaBean(RentaBean rentaBean) {
		this.rentaBean = rentaBean;
	}

	public Tipocambio getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(Tipocambio tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getClaveUpaLiq() {
		return claveUpaLiq;
	}

	public void setClaveUpaLiq(String claveUpaLiq) {
		this.claveUpaLiq = claveUpaLiq;
	}

	public String getNro_reporte() {
		return nro_reporte;
	}

	public void setNro_reporte(String nro_reporte) {
		this.nro_reporte = nro_reporte;
	}
    
  
}




