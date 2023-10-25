package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.exolab.castor.xml.validators.IntegerValidator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.NumeracionComprobantePagoBean;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detallecartera;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Tipoconcepto;
import pe.gob.sblm.sgi.entity.Tipodocu;
import pe.gob.sblm.sgi.entity.Tipodocupersona;
import pe.gob.sblm.sgi.entity.Tipomodalidadpago;
import pe.gob.sblm.sgi.entity.Tipotransaccion;
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.bean.DetallecarteraBean;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IFHvariadoService;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;
import pe.gob.sblm.sgi.util.PropiedadesBaseDatos;

@ManagedBean(name = "carteraMB")
@ViewScoped
public class CobranzaManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger Logger = LoggerFactory.getLogger(CobranzaManagedBean.class);
	
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	@ManagedProperty(value = "#{carteraService}")
	private transient IRecaudacionCarteraService carteraService;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;
	
	@ManagedProperty(value = "#{administrarArchivoService}")
	private transient AdministrarArchivoService administrarArchivoService;
	private Date hoy,ayer;
	private ItemBean selectedCobrador;
	
	private String selectedTipoDocumento;
	// Cartera
	private Cartera cartera;
	private int idcobradorseleccionado;
	private List<Contrato> listaContratosVigentes = new ArrayList<Contrato>();
	private List<Contrato> listaContratos = new ArrayList<Contrato>();
	private List<String> listaIdContratosAgregarCartera;
	private List<Contrato> listaContratosAgregarCartera = new ArrayList<Contrato>();
	private Contrato contratoCartera;
	private List<Cartera> listaCarteras = new ArrayList<Cartera>();
	private List<Cartera> filtrolistaCarteras;
	
	private int idcarteraEditar;
	private boolean sivisibleigv;

	private String claveupa;
	private List<CondicionBean> listaContratosDeUpa = new ArrayList<CondicionBean>();

	/** Detalle cartera **/
	private List<DetallecarteraBean> listadetalleCarteraBean,filtrolistadetalleCarteraBean;
	private DetallecarteraBean detallecarteraBean;


	private Detallecuota detallecuota;
	private List<Cuota> cuotasxcontrato = new ArrayList<Cuota>();

	private List<Tipopago> tipopagos;
	private List<Tipodocu> listaTipoDocumentos;
	private List<Tipotransaccion> tipotransaccions;
	
	private List<Tipomovimiento> tipomovimientos;
	private List<Tipomodalidadpago> listatipomodalidadpagos;

	private Comprobantepago comprobantepagoPadreNotaTipo;
	private Comprobantepago comprobantepagoPadreNotaTipoND;
	private int idComprobantePagoPadrenotatipo;
	
	// Nota de debito
	private String nrocomprobantepadrenotadebito, nombrecomprobanteNotadebito,observacionnotadebito,observacionnotadebitopenalizacion;
	
	// Nota de credito
	private String observacionnotacredito;

	private Date fechaemisionNotaDebito,fechaemisionNotaCredito;
	private List<Comprobantepago> listacomprobantes = new ArrayList<Comprobantepago>();
	private List<Comprobantepago> listaReciboCajaReferencia;

	private String condicion;
	private int iddetallecartera;
	private int idcomprobantepadrenotadebito;
	private String idtipodocpadrenotadebito;
	
	// Pago con contrato
	private Double valorTipoCambio=0.0;
	private Date fechaValorTipoCambio, fechaValorIgv;
	private String valorIgv;
	private Boolean usotipocambiosistema = true;
	private int nrocuotasDetectadasContrato;
	private Double montoTotalContrato;
	private Double montoCuotaContrato;
	private Double montoSolesIngresado;
	private String idtipopagoseleccionado, idtipodocuseleccionado, idtipomovimientoseleccionado,idtipoconceptoseleccionado ,idtipotransaccionseleccionado,idtipomodalidadpago;
	private int idcontratoseleccionado;

	private int nromesespagados, nromesespendientes, periodoSinContrato;
	private Double montomensualrenta;

	private CondicionBean condicionSeleccionado;
	
	
	private CuotaBean cuotaPendienteCondicion = new CuotaBean();
	
	
	// Pago  contrato
	private List<CuotaBean> listaCuotasPendientesCondicion = new ArrayList<CuotaBean>();
	private List<CuotaBean> listaCuotasPendientesCondicionInicial = new ArrayList<CuotaBean>();
	//Pago sin contrato 
	
	private List<CuotaBean> listaCuotasPendientesCondicionCancelar = new ArrayList<CuotaBean>();
	private List<CuotaBean> listaCuotasPendientesCondicionCancelarFilter;
	private List<CuotaBean> listaCuotasPendientesCondicionCancelarxNotaDebito = new ArrayList<CuotaBean>();
	
	//Pagados 
	private List<CuotaBean> listaCuotasPagadas = new ArrayList<CuotaBean>();
	private List<CuotaBean> listaCuotasPagadasFilter;
	
	// Cuota
	private int nrocuotaFila;
	private String mescuotaFila;
	private String mesFila;
	private Double moraFila, subtotalFila, montoFila;
	//private Double igvComprobante, totalSubTotalComprobante, montoDetraccion;
	
	private BigDecimal montoDetraccion;
	private BigDecimal totalSubTotalComprobante;
	private BigDecimal totalSubTotalComprobanteN;
	private BigDecimal igvComprobante;
	private BigDecimal igvComprobanteN;
	private BigDecimal totalComprobante;
	private BigDecimal totalComprobanteN;
	private Double totalnotacredito,subtotalnotacredito,igvnotacredito;

	private Double totalmora = 0.0;
	private Double totalpenalizacion = 0.0;
	
	private String tipoDescuentoNotaCreditoSeleccionado;
	

	/* Artificio caso excepcional */
	private int nrocuotascanceladas;
	private int nrocuotaPendienteCancelacion;

	private List<Usuario> listaCobradores = new ArrayList<Usuario>();

	// Comprobante
	private Comprobantepago comprobantepago = new Comprobantepago();
	private ComprobanteBean comprobantepagoNC = new ComprobanteBean();
	private HashMap<Integer,CuotaBean> mapacuotames= new HashMap<Integer, CuotaBean>();
	
	private String nombrecarteraContrato;
	private Boolean sigarantia=false;
	private Boolean user_acceso_fechacanc=Boolean.FALSE;
	private String acceso_opcion;
	private Boolean habilitarDocDNI=Boolean.TRUE,habilitarDocRUC=Boolean.TRUE;
	private Integer longDocuPersona;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{autovaluoCobranzaMB}")
	AutovaluoCobranzaManagedBean autovaluoCobranzaMB;
	
	
	//private CobranzaConsultaManagedBean cobranzaConsultaMB = new CobranzaConsultaManagedBean();;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{FHvariadoService}")
	private transient  IFHvariadoService FHvariadoService;
	
	private Boolean visibleReporte=true,visibleCartera=true,visibleCobranza=true,visibleCobranzaRegistro=true;
	private Boolean siperfilcobrador=false,siperfilpermitidocobranza=false,sicobrador=false;
	private String carteradetectadaCobrador="",nombredetectadoCobrador="";
	private Boolean sivisibleReciboCajaRef=Boolean.FALSE;
	
	private Boolean siActivoFechaVencimiento;
	private Date minFechaVencimiento,maxFechaVencimiento;
	private Comprobantepago reciboCajaReferencia;
	private String tipodocumentoSeleccionado;
	private Boolean sifacturacionelectronica;
	private Boolean sigeneracionautomaticanotadebito=Boolean.FALSE ,sigeneracionautomaticanotadebitoPenalizacion=Boolean.FALSE,sivalidacioncomprobantefisico=Boolean.FALSE ;
	private String serieFacturacionFisica;
	private String correlativoFacturacionFisica;
	private Date minfechacancelacion=new Date();
	private Date consulta_minfechacancelacion =new Date();
	private String fechaoperacion,numerooperacion;
	//tipo de descuento Total o Parcial 
	private String tipoNotaDebitoyCredito;
	private String tipoConceptoNotaDebito;
	private String opciontipodoc;
	private String nombreusuariologueado;
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	HttpSession varSesionVTC = request.getSession();


	@PostConstruct
	public void initObjects() {
		opciontipodoc=Constante.OPCION_TIPO_DOC_DNI;
		tipoConceptoNotaDebito=Constante.TIPO_CONCEPTO_MORA;//por defecto mora 
		if (userMB.nombreperfilSeleccionado.equals("Asistente Recaudación")) {
			visibleReporte=true;visibleCartera=true;visibleCobranza=true;
		}else if (userMB.nombreperfilSeleccionado.equals("Cobrador")) {
			siperfilcobrador=true;siperfilpermitidocobranza=true;
//			visibleReporte=true;visibleCartera=false;visibleCobranza=true;
			visibleReporte=true;visibleCartera=false;visibleCobranza=true;visibleCobranzaRegistro=false;
			
			
			if (carteraService.siAsignadoCobrador(userMB.getUsuariologueado().getIdusuario())) {
				carteradetectadaCobrador=carteraService.obtenerCarteraCobrador(userMB.getUsuariologueado().getIdusuario());
				selectedCobrador=carteraService.obtenerNombreCobrador(userMB.getUsuariologueado().getIdusuario());
				comprobantepago.setNombrecobrador(nombredetectadoCobrador);
				sicobrador=true;
			}
		}else if (userMB.nombreperfilSeleccionado.equals("Supervisor S.G.I.")) {
			siperfilpermitidocobranza=true;
		}else if (userMB.nombreperfilSeleccionado.equals("Supervisor Recaudación")) {
			siperfilpermitidocobranza=true;
		}else if (userMB.nombreperfilSeleccionado.equals("Asistente Tesorería")) {
			siperfilpermitidocobranza=true;visibleReporte=false;visibleCartera=false;visibleCobranza=false;
		}else if (userMB.nombreperfilSeleccionado.equals("Asistente S.G.I.")) {
			siperfilpermitidocobranza=true;
		}
		tipopagos = getCarteraService().listartipopagos();
		tipotransaccions= getCarteraService().listartipotransaccion();
		listaTipoDocumentos = getCarteraService().listartipodocus();
		listatipomodalidadpagos=getCarteraService().listartipomodalidadpagos(); 
		tipomovimientos = getCarteraService().listartipomovimientos();
		listaCobradores = getCarteraService().listarUsuarios();
		inicializarTotales();
		inicializarValores();
	}
	
	public void inicializarValores(){
		Calendar fechaInicio=Calendar.getInstance();
		Calendar fechaFin=Calendar.getInstance();
		
		fechaFin.set(Calendar.DATE, fechaInicio.getActualMaximum(Calendar.DAY_OF_MONTH));
		fechaFin.set(Calendar.HOUR_OF_DAY,0);
		fechaFin.set(Calendar.MINUTE,0);
		fechaFin.set(Calendar.SECOND,0);
		fechaFin.set(Calendar.MILLISECOND,0);
		
		fechaInicio.set(Calendar.DATE,1);
		
		
		minFechaVencimiento=fechaInicio.getTime();
		maxFechaVencimiento=fechaFin.getTime();
		
	}
	
	public void convertirTotalTipoCambio(){
		
	}
	
	public void cargarCarteras(){
		if (listaCarteras.size()==0) {
			listaCarteras = getCarteraService().listaCarteras();
		}
	}

	
	public void obtenerListaContratosDeUpa() {
		if (carteraService.siExisteUpa(claveupa)) {
			listaContratosDeUpa = carteraService.obtenerListaContratosDeUpa(claveupa);
			if (listaContratosDeUpa.size()!=0) {
				for (CondicionBean bean: listaContratosDeUpa) {
					System.out.println("SIREFERENCIADEUDA="+bean.getSireferenciareconocimientodeuda());
					if (bean.getCondicion().equals("Sin Contrato")) {
						Calendar iniciocobro=Calendar.getInstance();
						iniciocobro.setTime(bean.getIniciocobro());
						iniciocobro.add(Calendar.MONTH, 1);
						
						bean.setIniciocobro(iniciocobro.getTime());
					}
				}
			}
			
		} else {
			addWarnMessage("No existe upa", "La upa ingresado no esta registrada en el sistema");
		}
		
	}
	
	
	public void settingNombreCartera(){
		iddetallecartera=carteraService.obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato()); 
		nombrecarteraContrato=carteraService.obtenerNombreCartera(iddetallecartera);
		List<Cuota> lista= new ArrayList<Cuota>(cobranzaRecaudacionService.obtenerPagosContrato(condicionSeleccionado.getIdcontrato()));		
		listaCuotasPagadas.clear();
		for (int i = 0; i < lista.size(); i++) {
			CuotaBean c= new CuotaBean();
			c.setIdcuota(lista.get(i).getIdcuota());
			c.setAnio(lista.get(i).getAniocuotames());
			c.setMes(lista.get(i).getMespagado());
			c.setMonto(lista.get(i).getMontosoles());
			if (lista.get(i).getCancelado().equals("1")) {
				c.setCondicion("Cancelado");
			} else if (lista.get(i).getCancelado().equals("0")){
				c.setCondicion("Pendiente");
			}else {
				c.setCondicion("Generado");
			}
			
			listaCuotasPagadas.add(c);
		}
	}
	

	/************************************* PAGOS ****************************************/
	
	public String setNombreSelectedTipoDocumento(String tipdoc){
		
		if (tipdoc.equals("00")) {
			selectedTipoDocumento="Sin valor legal";
		} else if (tipdoc.equals("01")){
			selectedTipoDocumento="Factura";
		} else if (tipdoc.equals("03")){
			selectedTipoDocumento="Boleta de Venta";
		} else if (tipdoc.equals("04")||tipdoc.equals("49")){
			selectedTipoDocumento="Recibo de Caja";
		} else if (tipdoc.equals("08")){
			selectedTipoDocumento="Nota de Débito";
		} else if (tipdoc.equals("09") || tipdoc.equals("10")){
			selectedTipoDocumento="Nota de Crédito";
		}else if (tipdoc.equals("DA")){
			selectedTipoDocumento="EXTINCIÓN DE DEUDA CON DOCUMENTO ADMINISTRATIVO";
		}
		return "ok";
}

	public void elegirTipoFacturacionNotaDebitoyCredito(String tipdoc,String tipoDescRenta){
		tipoNotaDebitoyCredito=tipoDescRenta;	
		totalSubTotalComprobante=new BigDecimal("0.0");
		igvComprobante=new BigDecimal("0.0");
		totalComprobante=new BigDecimal("0.0");
		limpiarListaCuotasPendientesCondicionCancelar();
		elegirTipoFacturacion(tipdoc);
			}
	public void elegirTipoFacturacion(String tipdoc){
		
		tipodocumentoSeleccionado=tipdoc;
		
		setNombreSelectedTipoDocumento(tipdoc);
		//if(!condicionSeleccionado.getSiprocesojudicial()||(tipdoc.equals("99")||tipdoc.equals("RA")||tipdoc.equals("49"))){
		//cobranzaConsultaMB.validarActualizacionEstadoComprobante();
		if (tipdoc.equals("99")) {
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgElegirTipoFacturacion.show();");
		}else if(tipdoc.equals("NC")||tipdoc.equals("ND")){	
			verificarPermisoCobro("");
		} else {
			if (tipdoc.substring(0, 2).equals(
					Constante.OPCION_RECUPERACION_ARBITRIOS)) {// Recuperación  de Arbitrios																
				if (tipdoc.length() > 2 && tipdoc.substring(2, 4).equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)) {// recibo de caja recuperación de arbitrio																		 
					RequestContext contextRequest = RequestContext.getCurrentInstance();
					contextRequest.execute("dlgReciboCajaRecuperacionArbitrio.show();");
				} else {// registro de pago , busqueda recuperacion de arbitrios
					RequestContext contextRequest = RequestContext.getCurrentInstance();
					contextRequest.execute("dlgRecuperacionArbitrios.show();");
				}
			}
		
		else {
//			if (condicionSeleccionado==null && tipdoc.equals("04")) {
//				verificarPermisoCobro("");
//			} 
			if (condicionSeleccionado==null && tipdoc.equals("49")) {
				verificarPermisoCobro("");
			}
			else if (condicionSeleccionado == null) {
				addWarnMessage("Seleccionar una upa","No ha seleccionado ninguna upa, seleccione una upa para poder agregar el pago");
			} 
			else if  (siperfilpermitidocobranza && siperfilcobrador && carteradetectadaCobrador.equals("")){
				addWarnMessage("Usted no es cobrador", "Usted tiene perfil cobrador, pero no ha sido asignado  como un usuario cobrador, contáctese con el Área de Recaudación.");
			} 
			else if (getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato()) == 0) {
				addWarnMessage("Upa no esta agregada a cartera","la upa no ha sido agregada a una cartera, contactese con el Área de Recaudación.");
			}else if(siperfilpermitidocobranza && siperfilcobrador && !carteradetectadaCobrador.equals("")&& !carteradetectadaCobrador.equals(nombrecarteraContrato)){
					addWarnMessage("Upa no pertenece a su cartera", "Usted no puede cobrar upa, contactese con el Área de Recaudación. ");
			}else if(!siperfilpermitidocobranza){
					addWarnMessage("Permisos insuficientes", "Usted no puede cobrar upa, contactese con el Área de Informática.");
//			}else if(tipodocumentoSeleccionado.equals("01") && condicionSeleccionado.getTipopersona().equalsIgnoreCase(Constantes.TIPO_PERSONA_1)){
//				addFatalMessage("No se puede ingresar pago","Persona natural no puede registrar el tipo de documento factura, usted no puede ingresar pago, contactese con el Área de Arrendamiento.");
			}else if(tipodocumentoSeleccionado.equals("01") && condicionSeleccionado.getTipopersona().equalsIgnoreCase(Constantes.TIPO_PERSONA_1) && condicionSeleccionado.getRuc().length()==0){
				addFatalMessage("No se puede ingresar pago","Persona natural no tiene registrado el RUC , contactese con el Área de Arrendamiento.");
			}else if(tipodocumentoSeleccionado.equals("01") && condicionSeleccionado.getIdtipopersona()=='1'&& condicionSeleccionado.getRuc().length()>0){
				addFatalMessage("No se puede ingresar pago","Persona natural no puede registrar el tipo de documento factura, usted no puede ingresar pago, contactese con el Área de Arrendamiento.");
			}else if(!condicionSeleccionado.getCondicion().equals("Sin Contrato") && condicionSeleccionado.getNumerocuotas()==0 ){
					addFatalMessage("Nº Cuota no registradas","No se ha registrado el número de cuotas del contrato, Usted no puede cobrar upa, contactese con el Área de Arrendamiento.");
			}
			else {
				
				System.out.println("Tipodocumento D: "+tipdoc); 
				if(!condicionSeleccionado.getSiprocesojudicial()){//condicion de judicializado
//				if(condicionSeleccionado.getEstado().equals(Constante.CONDICION_CONTRATO_VIGENTE)){//CONDICION SOLO SI ESTA VIGENTE
				//System.out.println("estado="+condicionSeleccionado.getEstadoupa());
			    if(condicionSeleccionado.getEstadoupa().equals(Constante.ESTADO_A)){// condicion Clave UPA  solo si esta activa
			    if(!condicionSeleccionado.getEstado().equalsIgnoreCase(Constante.CONDICION_CONTRATO_ANULADO)){
			    if(!condicionSeleccionado.getSireferenciareconocimientodeuda() ){ // si esta amarrada a un acta de reconocimiendo deuda
			     if (tipdoc.equals("08")) { // Nota de débito
					verificarPermisoCobro("");
				} else if (tipdoc.equals("10") || tipdoc.equals("11") ) { //Notas de crédito
					verificarPermisoCobro("");
				} else if (tipdoc.equals("04") ) { // Recibo de caja
					verificarPermisoCobro("");
				} else if (tipdoc.equals("49") ) { 
					verificarPermisoCobro("");
				} else if (tipdoc.equals("DA") ) {  //Extincion de deuda con Documento Administrativo
					verificarPermisoCobro("");
				} else{
					RequestContext contextRequest = RequestContext.getCurrentInstance();
					contextRequest.execute("dlgElegirTipoFacturacion.show();");
				}
//				}else{
//					addWarnMessage("Seleccionar una upa","El contrato seleccionado se encuentra en estado FINALIZADO por lo tanto no se puede realizar ningun tipo de pago");
//				}
			    }else{
			    	addWarnMessage("Seleccionar una upa","El contrato seleccionado se encuentra amarrada a un ACTA de RECONOCIMIENTO DEUDA");

			    }
			    }else{
			    	addWarnMessage("Seleccionar un Precontrato","El Precontrato seleccionado se encuentra en estado ANULADO no se puede realizar ningun tipo de pago.");
			    }
				}else{
				addWarnMessage("Seleccionar una upa","El contrato seleccionado se encuentra con CLAVE UPA  bloqueada por lo tanto no se puede realizar ningun tipo de pago");
			      }
				}else{
					addWarnMessage("Seleccionar una upa","La upa seleccionada esta judicializada por lo tanto no se puede realizar ningun tipo de pago");
					
							}
				
				
			}
		}	
		}
//		}else{
//			addWarnMessage("Seleccionar una upa","La upa seleccionada esta judicializada por lo tanto no se puede realizar ningun tipo de pago");
//
//		}
	}
	public Comprobantepago setearValoresComprobante(Comprobantepago comprobantepago){
		comprobantepago.setIdtipopersona(condicionSeleccionado.getIdtipopersona().toString());
		if(condicionSeleccionado.getIdtipopersona()==Constantes.TIPO_PERSONA_JURIDICA){
			comprobantepago.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_RUC);
			comprobantepago.setDnirucpagante(condicionSeleccionado.getRuc().trim());
		}else if(condicionSeleccionado.getIdtipopersona()==Constantes.TIPO_PERSONA_NATURAL){
			
			if (condicionSeleccionado.getRuc() != null) {
				if (condicionSeleccionado.getRuc().trim().length() > 0) {
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getRuc().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_RUC);
				} else if(condicionSeleccionado.getDni().trim().length()>0){
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getDni().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_DNI);
				}else if(condicionSeleccionado.getCarnetextranjeria().trim().length()>0){
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getCarnetextranjeria().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_CARNET_EXTRANJERIA);
				}
			}else if(condicionSeleccionado.getDni() != null){
				if(condicionSeleccionado.getDni().trim().length()>0){
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getDni().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_DNI);
				}else if(condicionSeleccionado.getCarnetextranjeria().trim().length()>0){
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getCarnetextranjeria().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_CARNET_EXTRANJERIA);
				}
			}else{
				if(condicionSeleccionado.getCarnetextranjeria().trim().length()>0){
					comprobantepago.setDnirucpagante(condicionSeleccionado
							.getCarnetextranjeria().trim());
					comprobantepago
							.setTipodoc_receptor(Constante.TIPO_DOC_RECEPTOR_CARNET_EXTRANJERIA);
				}
			}
		}
		
		return comprobantepago;
	}
	public void verificarPermisoCobro(String tipofacturacion) {
		    nombreusuariologueado="";
		    nombreusuariologueado=userMB.getNombreusr();
		    if(nombreusuariologueado!=null){
		    	if(nombreusuariologueado.equals(Constante.USUARIO_ESPECIAL_SGI.get("01"))||nombreusuariologueado.equals(Constante.USUARIO_ESPECIAL_SGI.get("02"))){
		    		//minfechacancelacion=ponerDiasFechaInicioMes(minfechacancelacion);
		    		//user_acceso_fechacanc=true;
		    		minfechacancelacion=new Date();
		    		minfechacancelacion=ponerDiasFechaInicioMesComprobantes(minfechacancelacion,-3);
		    	}
		    }
		    System.out.println("nombreusuariologueado = "+nombreusuariologueado);
		    
			if (tipofacturacion.equals(Constantes.TIPO_FACTURACION_FISICA)) {
				sifacturacionelectronica=Boolean.FALSE;
			} else if(tipofacturacion.equals(Constantes.TIPO_FACTURACION_ELECTRONICA)) {
				sifacturacionelectronica=Boolean.TRUE;	
			}else {
				sifacturacionelectronica=null;
			}
			RequestContext contextRequest = RequestContext.getCurrentInstance();
		    //condicionSeleccionado
			//System.out.println("condicionSeleccionado = "+condicionSeleccionado.toString());
			valorTipoCambio=0.0;
			if (tipodocumentoSeleccionado.equals("01") || tipodocumentoSeleccionado.equals("03") || tipodocumentoSeleccionado.equals("00") ) {
				idtipodocuseleccionado=tipodocumentoSeleccionado;
				sivisibleigv=tipodocumentoSeleccionado.equals("01")?true:false;
				
				
				if (condicionSeleccionado.getMoneda().equals("S")) {
					 varSesionVTC.setAttribute("tipocambio", FuncionesHelper.redondearNum(valorTipoCambio,3));
					 Object sesionUserName = (Object) varSesionVTC.getAttribute("tipocambio");
					 System.out.println(condicionSeleccionado.getCondicion().equals("Sin Contrato")?cargarInformacionSinContratoSeleccionado():(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)? cargarInformacionReconocimientoDeudaSeleccionado():cargarInformacionContratoSeleccionado()));//No borrar pinta ok!!
					 contextRequest.execute("dlgRegistroCuotaSinContrato.show()");
				} else {
					
					
					contextRequest.execute("dlgIngresarTipoCambio.show();");
				}
				inicializarTotales();
				comprobantepago = new Comprobantepago();
                comprobantepago = setearValoresComprobante(comprobantepago);
				
				
			}
			
			// EXTINCIÓN DE DEUDA CON DOCUMENTO ADMINISTRATIVO
			else if (tipodocumentoSeleccionado.equals("DA") ) {
//				idtipodocuseleccionado=tipodocumentoSeleccionado;
				idtipodocuseleccionado="DA";
				idtipopagoseleccionado="04";
				idtipomovimientoseleccionado="05";
				sivisibleigv=tipodocumentoSeleccionado.equals("DA")?false:true;
				sifacturacionelectronica=Boolean.FALSE;
				
				System.out.println("condicionSeleccionado.getMoneda()="+condicionSeleccionado.getMoneda());
				if (condicionSeleccionado.getMoneda().equals("S")) {
					System.out.println("Condición: ");
					System.out.println(condicionSeleccionado.getCondicion().equals("Sin Contrato")?cargarInformacionSinContratoSeleccionado():cargarInformacionContratoSeleccionado());//No borrar pinta ok!!
					contextRequest.execute("dlgRegistroDocumentoAdministrativo.show()");
				} else {
					contextRequest.execute("dlgIngresarTipoCambioDA.show();");
				}
				inicializarTotales();
				comprobantepago = new Comprobantepago();
				comprobantepago = setearValoresComprobante(comprobantepago);
				System.out.println("*******************************");
				System.out.println("tipodocumentoSeleccionado="+tipodocumentoSeleccionado);

			}
			
			// FIN DE EXTINCIÓN DE DEUDA CON DOCUMENTO ADMINISTRATIVO
		
			
			else if (tipodocumentoSeleccionado.equals("04")) {
				idtipodocuseleccionado=tipodocumentoSeleccionado;
				idtipomovimientoseleccionado="03";
//				idtipoconceptoseleccionado="03";
				
				
				sigarantia=true;
				
				comprobantepago = new Comprobantepago();
				//comprobantepago = setearValoresComprobante(comprobantepago);
				inicializarTotales();
				contextRequest.execute("dlgRegistroReciboCaja.show();");
				
			}
			
			
			else if (tipodocumentoSeleccionado.equals("49")) {
				idtipodocuseleccionado="04";
				idtipomovimientoseleccionado="03";
//				idtipoconceptoseleccionado="03";
				
				sigarantia=false;//modificado para habilitar sin upa
				//sigarantia=true;
				
				comprobantepago = new Comprobantepago();
				inicializarTotales();
				contextRequest.execute("dlgRegistroReciboCaja.show();");
				
			}else if (tipodocumentoSeleccionado.equals("08")) {//NOTA DE DEBITO NUEVO Y PENDIENTE
				
				
				System.out.println("tipoDescuentoNotaCreditoSeleccionadoA=="+tipoDescuentoNotaCreditoSeleccionado);
				idtipodocuseleccionado=tipodocumentoSeleccionado;
				tipoDescuentoNotaCreditoSeleccionado="";
				if(tipoNotaDebitoyCredito.equals("Pendiente")){
					    listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"08"+tipoNotaDebitoyCredito);
						if (listacomprobantes.size() == 0) {
							addInfoMessage("No existe nota de débito pendientes","Los comprobantes pertenecientes a las upa ya han generado su respectiva nota de débito");
						} else {
						contextRequest.execute("dlgRegistroNotaDebitoManual.show();dlgRegistroCuotaSinContrato.hide()");
						}
				}else{
					if(tipoNotaDebitoyCredito.equals("Nuevo")){
						tipoDescuentoNotaCreditoSeleccionado="Renta";
						listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"08"+tipoNotaDebitoyCredito);
					if (listacomprobantes.size() == 0) {
						addInfoMessage("No existe nota de débito pendientes","Los comprobantes pertenecientes a las upa ya han generado su respectiva nota de débito");
					} else {
						 comprobantepago = new Comprobantepago();
						 comprobantepago = setearValoresComprobante(comprobantepago);
						 contextRequest.execute("dlgRegistroDebitoTipoDetalle.show();");
					}
					}else{
						 if(tipoNotaDebitoyCredito.equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
							tipoDescuentoNotaCreditoSeleccionado="Renta";
							listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"08"+tipoNotaDebitoyCredito);
							if (listacomprobantes.size() == 0) {
								addInfoMessage("No existe nota de débito pendientes","Los comprobantes pertenecientes a las upa ya han generado su respectiva nota de débito");
							} else {
								 comprobantepago = new Comprobantepago();
								 comprobantepago = setearValoresComprobante(comprobantepago);
								 contextRequest.execute("dlgRegistroNotaDebitoPenalizacionTipoDetalle.show();");
							}
						 }
					}
					  
					
				}
				//listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"08");
				 
//				if (listacomprobantes.size() == 0) {
//					addInfoMessage("No existe nota de débito pendientes","Los comprobantes pertenecientes a las upa ya han generado su respectiva nota de débito");
//				} else {
//				contextRequest.execute("dlgRegistroNotaDebitoManual.show();dlgRegistroCuotaSinContrato.hide()");
//				}
				
			}else if(tipodocumentoSeleccionado.equals("10")) //NOTA DE CRÉDITO - REBAJA DE RENTA
			{   inicializarTotales();
				comprobantepago = new Comprobantepago();
				comprobantepago = setearValoresComprobante(comprobantepago);
				tipoDescuentoNotaCreditoSeleccionado="Renta";
				idtipodocuseleccionado="09";
				idtipopagoseleccionado="04";
				idtipomovimientoseleccionado="04";
				if(tipoNotaDebitoyCredito!=null){
					if(tipoNotaDebitoyCredito.equals(Constante.TIPO_NOTA_CREDITO_3)){
						listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"10",tipoNotaDebitoyCredito);
					}else{
						listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"10");
					}
				}else{
				listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"10");}
				contextRequest.execute("dlgRegistroNotaCreditoTipoDetalle.show();");
			} else if(tipodocumentoSeleccionado.equals("11")) // NOTA DE CRÉDITO - REBAJA DE MORA 
			{   listaCuotasPendientesCondicionCancelar.clear();
				totalmora=0.0;
				comprobantepago = new Comprobantepago();
				comprobantepago = setearValoresComprobante(comprobantepago);
				tipoDescuentoNotaCreditoSeleccionado="Mora";
				idtipodocuseleccionado="09";
				idtipopagoseleccionado="04";
				idtipomovimientoseleccionado="04";
				listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(condicionSeleccionado.getIdcontrato(),"11");
				
				contextRequest.execute("dlgRegistroNotaCreditoTipoDetalle.show();");
				
			}else if(tipodocumentoSeleccionado.equals("99")) {    //OTROS PAGOS
			
				inicializarPagoOtroSinCondicion();
				
				contextRequest.execute("dlgRegistroPagoOtro.show();");
				
			}else if(tipodocumentoSeleccionado.equals("NC")) {    //OTROS PAGOS- NOTA DE CREDITO
				sifacturacionelectronica=true;//condicion
				inicializarPagoOtroSinCondicion();
				listacomprobantes.clear();
				//listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(3891,"10");
				listacomprobantes = cobranzaRecaudacionService.listaComprobantes(tipodocumentoSeleccionado);
				contextRequest.execute("dlgRegistroPagoOtroNotaCredito.show();");

				
			}else if(tipodocumentoSeleccionado.equals("ND")) {    //OTROS PAGOS- NOTA DE DEBITO
				sifacturacionelectronica=true;//condicion
				inicializarPagoOtroSinCondicion();
				listacomprobantes.clear();
				//listacomprobantes = cobranzaRecaudacionService.listaComprobantesContrato(3891,"10");
				listacomprobantes = cobranzaRecaudacionService.listaComprobantes(tipodocumentoSeleccionado);
				contextRequest.execute("dlgRegistroPagoOtroNotaDebito.show();");

				
			}else {
				sivisibleigv = false;
			}
	
	}

	
	public void limpiarCampos()  {
			cartera = null;
			idcontratoseleccionado = 0;
			
			moraFila = 0.0;
	
			observacionnotadebito = "";
			nrocomprobantepadrenotadebito="";
			observacionnotacredito="";
			
	
			inicializarTotales();
			
			idtipomovimientoseleccionado = "0";
			idtipopagoseleccionado = "0";
			idtipoconceptoseleccionado="0";
			idtipodocuseleccionado="0";
			
			idComprobantePagoPadrenotatipo=0;
			limpiarListaCuotasPendientesCondicionCancelar();
			sigeneracionautomaticanotadebito=Boolean.FALSE;
			sigeneracionautomaticanotadebitoPenalizacion=Boolean.FALSE;
			serieFacturacionFisica="";
			correlativoFacturacionFisica="";
			
			fechaemisionNotaDebito=null;
			selectedCobrador=null;
			observacionnotadebito="";
			fechaoperacion="";
			numerooperacion="";
			habilitarDocDNI=true;habilitarDocRUC=true;
			longDocuPersona=0;
			
	}

	public void  inicializarTotales(){
		
		totalSubTotalComprobante = new BigDecimal("0.0");
		totalComprobante 		 = new BigDecimal("0.0");
		igvComprobante 			 = new BigDecimal("0.0");
		montoDetraccion 		 = new BigDecimal("0.0");
		totalSubTotalComprobanteN= new BigDecimal("0.0");
		igvComprobanteN 	     = new BigDecimal("0.0");
		totalComprobanteN 		 = new BigDecimal("0.0");
		totalmora				 = 0.0;
		subtotalnotacredito		 = 0.0;
		totalnotacredito		 = 0.0;
		igvnotacredito			 = 0.0;
		sivisibleReciboCajaRef=Boolean.FALSE;
		selectedCobrador=null;
		idtipopagoseleccionado=null;
		idtipomovimientoseleccionado=null;
		sivalidacioncomprobantefisico=Boolean.FALSE;
	}

	
	
	public void inicializandoValores() {
		
		if ((valorIgv =String.valueOf(carteraService.obtenerUltimoIgv())) != null) {
			fechaValorIgv = carteraService.obtenerFechaIgv();
		} else {
			valorIgv = String.valueOf(0.18);
		}

		inicializarTotales();
		
		listaCuotasPendientesCondicion.clear();
		limpiarListaCuotasPendientesCondicionCancelar();
	}

	private static Date ponerDiasFechaFinMes(Date fecha){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); 
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

 }
	private static Date ponerDiasFechaInicioMes(Date fecha){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

 }
	public Date ponerDiasFechaInicioMesComprobantes(Date fecha,int dias){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
       // calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); 
        calendar.add(Calendar.DAY_OF_YEAR,dias);
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

 }
//	public void buscarTipoCambio() {
//		 comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
//		 minFechaVencimiento=comprobantepago.getFechaemision();
//		 limpiarListaCuotasPendientesCondicionCancelar();
//	}
	public void ObtenerFechaCancelacion() {
		 if(tipodocumentoSeleccionado!=null){
			 if(tipodocumentoSeleccionado.equals("08")||tipodocumentoSeleccionado.equals("10")||tipodocumentoSeleccionado.equals("11")){				 
				 //comprobantepago.setFechacancelacion(comprobantepago.getFechaemision()); setear fecha de cancelacion con femision
				 minFechaVencimiento=comprobantepago.getFechaemision();
			 }else{
				 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
			 }
		 }else{
			 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
		 }

		 
	}
	public void setearFecha() {
		 if(tipodocumentoSeleccionado!=null){
			 if(tipodocumentoSeleccionado.equals("08")||tipodocumentoSeleccionado.equals("10")){
				 comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
				 minFechaVencimiento=comprobantepago.getFechaemision();
			 }else{
				 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
				 comprobantepago.setFechacancelacion(null);
			 }
		 }else{
			 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
		 }

       	 limpiarListaCuotasPendientesCondicionCancelar();
        
		 
	}
	public void buscarTipoCambio() {
		 if(tipodocumentoSeleccionado!=null){
			 if(tipodocumentoSeleccionado.equals("08")||tipodocumentoSeleccionado.equals("10")){
				 comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
				 minFechaVencimiento=comprobantepago.getFechaemision();
			 }else{
				 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
				 //comprobantepago.setFechavencimiento(ponerDiasFechaInicioMes(comprobantepago.getFechaemision()));
			 }
		 }else{
			 comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
		 }

        	 limpiarListaCuotasPendientesCondicionCancelar();
         
		 
	}
	public void limpiarListaCuotasPendientesCondicionCancelar(){
		 listaCuotasPendientesCondicionCancelar.clear();
		 
	}
	public void limpiarListaCuotasPendientesCondicionCancelarboletafactura(){
		System.out.println("fechade cancelacion="+comprobantepago.getFechacancelacion());
		if(comprobantepago.getFechavencimiento()!=null){
			comprobantepago.setFechavencimiento(null);
		}
		 listaCuotasPendientesCondicionCancelar.clear();
		 
	}

	
	public Boolean validacionTotales(BigDecimal SubTotal,BigDecimal igv,BigDecimal total){
		double totalComp=0.0,subtotalComp=0.0,igvComp=0.0;
	    totalComp=FuncionesHelper.convertirDouble(total);
	    subtotalComp=FuncionesHelper.convertirDouble(SubTotal);
	    igvComp=FuncionesHelper.convertirDouble(igv);
		if(FuncionesHelper.redondearNum((totalComp-subtotalComp),2)!=igvComp){
			return false;
		}else{
			return true;
		}
	}

	public void calcularTotales() {
			
			//DecimalFormat formatter = new DecimalFormat("#.##");
			BigDecimal suma = new BigDecimal("0.0");
			BigDecimal sumaN = new BigDecimal("0.0");
			totalSubTotalComprobante=new BigDecimal("0.0");
			igvComprobante=new BigDecimal("0.0");
			totalComprobante=new BigDecimal("0.0");
			suma.setScale (2,RoundingMode.HALF_UP); // Lanza ArithmeticException
			sumaN.setScale (2,RoundingMode.HALF_UP);
			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalSubTotalComprobanteN.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobanteN.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobanteN.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
			
			Double sumamora=0.0,sumapenalizacion=0.0;
			
			for (CuotaBean c : listaCuotasPendientesCondicionCancelar) {
				   c.setMontopenalizacion(c.getMontopenalizacion()!=null? c.getMontopenalizacion():0.0);
				if (idtipodocuseleccionado.equals("09")) {
					
					
					if (tipoDescuentoNotaCreditoSeleccionado.equals("Renta")) {
						c.setMonto(c.getMonto()<=0?c.getMonto():c.getMonto()*-1);	
//						suma=suma.add(BigDecimal.valueOf(c.getMonto())); //convierte un Double "c.getMonto()" a BigDecimal: BigDecimal.valueOf(c.getMonto())
						suma=new BigDecimal(c.getMonto());				//monto
						sumaN=sumaN.add(BigDecimal.valueOf(c.getMonto()));//suma total  //convierte un Double "c.getMonto()" a BigDecimal: BigDecimal.valueOf(c.getMonto())
						sumamora=sumamora+c.getMora();
						sumapenalizacion=sumapenalizacion+c.getMontopenalizacion();
					} else {
						c.setMora(c.getMora()<=0?c.getMora():c.getMora()*-1);
						sumamora=sumamora+c.getMora();//suma mora 
						sumapenalizacion=sumapenalizacion+c.getMontopenalizacion();//suma penalizacion
					}

				}else {
//					suma=suma.add(BigDecimal.valueOf(c.getMonto())); //convierte un Double "c.getMonto()" a BigDecimal: BigDecimal.valueOf(c.getMonto())
					suma=new BigDecimal(c.getMonto());
					sumamora=sumamora+c.getMora();
					sumapenalizacion=sumapenalizacion+c.getMontopenalizacion();//suma penalizacion
				}
				
				//agregado
				if (idtipodocuseleccionado.equals("01")) 
				{	
//					totalComprobante = suma;
					totalComprobanteN = suma;
					double totalN=FuncionesHelper.redondearNum(Double.parseDouble(totalComprobanteN.toString()),2);
					totalComprobanteN=new BigDecimal(Double.toString(totalN));
					totalComprobanteN.setScale(2,RoundingMode.HALF_UP);
					totalComprobante = totalComprobante.add(totalComprobanteN);
					
//					totalSubTotalComprobante = suma.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					totalSubTotalComprobanteN =  suma.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					double numN=FuncionesHelper.redondearNum(Double.parseDouble(totalSubTotalComprobanteN.toString()),2);
					totalSubTotalComprobanteN = new BigDecimal(Double.toString(numN))  ;
					totalSubTotalComprobanteN.setScale(2,RoundingMode.HALF_UP);
					totalSubTotalComprobante=totalSubTotalComprobante.add(totalSubTotalComprobanteN);
					
					
//					igvComprobante = suma.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					igvComprobanteN = suma.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					double igv=FuncionesHelper.redondearNum(Double.parseDouble(igvComprobanteN.toString()),2);
					igvComprobanteN = new BigDecimal(Double.toString(igv)); 
					igvComprobanteN.setScale(2,RoundingMode.HALF_UP);
					igvComprobante=igvComprobante.add(igvComprobanteN);
					
				} else {
					Double numsuma=FuncionesHelper.redondearNum(Double.parseDouble(suma.toString()), 2);
					suma=new BigDecimal(numsuma.toString());
					//totalSubTotalComprobante =  suma.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					totalSubTotalComprobanteN =  suma.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					double num=FuncionesHelper.redondearNum(Double.parseDouble(totalSubTotalComprobanteN.toString()),2);
					totalSubTotalComprobanteN = new BigDecimal(Double.toString(num))  ;
					totalSubTotalComprobanteN.setScale(2,RoundingMode.HALF_UP);
					totalSubTotalComprobante=totalSubTotalComprobante.add(totalSubTotalComprobanteN);

//					igvComprobante = suma.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					igvComprobanteN = suma.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
					double igv=FuncionesHelper.redondearNum(Double.parseDouble(igvComprobanteN.toString()),2);
					igvComprobanteN = new BigDecimal(Double.toString(igv)); 
					igvComprobanteN.setScale(2,RoundingMode.HALF_UP);
					igvComprobante=igvComprobante.add(igvComprobanteN);
					
					
					totalComprobanteN = suma;
					double total=FuncionesHelper.redondearNum(Double.parseDouble(totalComprobanteN.toString()),2);
					totalComprobanteN=new BigDecimal(Double.toString(total));
					totalComprobanteN.setScale(2,RoundingMode.HALF_UP);
					totalComprobante = totalComprobante.add(totalComprobanteN);
					
					montoDetraccion  = new BigDecimal("0.0");
				}
				
				
				
	
			}
			/*DETRACCCION */
			if (idtipodocuseleccionado.equals("01")) 
				{	
					BigDecimal min = new BigDecimal("700");
					
					if (totalComprobante.doubleValue() >= min.doubleValue()) {
						montoDetraccion = totalComprobante.multiply(new BigDecimal("0.10")).divide(new BigDecimal("1"),2,RoundingMode.HALF_UP);; 
					} else {
						//montoDetraccion=0.0;
						montoDetraccion = new BigDecimal("0.0");
					}
	
				} else {
					montoDetraccion  = new BigDecimal("0.0");
				}
			
			totalmora = sumamora;
			totalpenalizacion=sumapenalizacion;
			System.out.println("totalmora="+totalmora);
			System.out.println("totalpenalizacion="+totalpenalizacion);
			BigDecimal totalcalculado=new BigDecimal("0.0");
			totalmora=FuncionesHelper.redondearNum(Double.parseDouble(totalmora.toString()),2);
			totalpenalizacion=FuncionesHelper.redondearNum(Double.parseDouble(totalpenalizacion.toString()),2);
			totalcalculado = new BigDecimal(Double.toString(totalmora)); 
			totalcalculado.setScale(2,RoundingMode.HALF_UP);
			//validación de los totales 05-03-2019
			Boolean validacion=false;
		    while(!validacion){		    	
		    	validacion=validacionTotales(totalSubTotalComprobante,igvComprobante,totalComprobante);
		    	if(!validacion){
		    		BigDecimal igvCompB=new BigDecimal(Double.toString(FuncionesHelper.redondearNum((FuncionesHelper.convertirDouble(totalComprobante)-FuncionesHelper.convertirDouble(totalSubTotalComprobante)),2))); 
					igvComprobante=igvCompB;
					igvComprobante.setScale(2,RoundingMode.HALF_UP);
		    	}
				
			}
			
			
//			if (sifacturacionelectronica) {
//				if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03") ) {
//					if (comprobantepago.getFechavencimiento()!=null) {
//						for (CuotaBean cuotaBean : listaCuotasPendientesCondicionCancelar) {
//							 cuotaBean.setSiActivo(Boolean.FALSE);
//							 cuotaBean.setCondicion("Generado");
//						}
//						
//					}else {
//						for (CuotaBean cuotaBean : listaCuotasPendientesCondicionCancelar) {
//							 cuotaBean.setSiActivo(Boolean.TRUE);
//						}
//						
//					}
//				}
//			}
			if (sifacturacionelectronica) {
				if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03") ) {
					if (comprobantepago.getFechavencimiento()!=null) {
						for (CuotaBean cuotaBean : listaCuotasPendientesCondicionCancelar) {
							 cuotaBean.setSiActivo(Boolean.FALSE);
							 cuotaBean.setCondicion("Pendiente");
						}
						
					}else {
						
						if(comprobantepago.getFechacancelacion()!=null && (idtipomovimientoseleccionado.equals("01")||idtipomovimientoseleccionado.equals("06"))){
							for (CuotaBean cuotaBean : listaCuotasPendientesCondicionCancelar) {
								 cuotaBean.setSiActivo(Boolean.TRUE);
								 cuotaBean.setCondicion("Cancelado");
							}
							
						}
						else{
							for (CuotaBean cuotaBean : listaCuotasPendientesCondicionCancelar) {
								 cuotaBean.setSiActivo(Boolean.TRUE);
								 cuotaBean.setCondicion("Pendiente");
							}
							
						}
						
					}
				}
			}
			//System.out.println("descripcion="+generarDescripcionReconocimiento(listaCuotasPendientesCondicionCancelar));
	}

	

	/*************************************************************************************************************************************************************************************************/
	/*********************************************************************************************************** Nota de Debito *************************************************************************/

	
	
	public void validarRegistroNotaDebito(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		
		if (!sigeneracionautomaticanotadebito && comprobantepagoPadreNotaTipo==null) {
			
			addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
		} else if (selectedCobrador==null) {
			addWarnMessage("", "Ingrese nombre cobrador");
		}else if (fechaemisionNotaDebito == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		}else if(comprobantepago.getIdtipopersona()==null ||comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
			addWarnMessage("","Usted no ha seleccionado el tipo de persona ");	
		}else if(comprobantepago.getDnirucpagante().trim().length()==0){
				addWarnMessage("","El campo de DNI/RUC se encuentra vacio");			
		}else if(sifacturacionelectronica){
			 System.out.println("sifacturacionelectronica="+sifacturacionelectronica);
			if (!sigeneracionautomaticanotadebito) {
				System.out.println("nota de debito manual ");
				contextRequest.execute("dlgConfirmarRegistroNotadebitoManual.show();");
			} else {
				System.out.println("notadebitoautomatica");
				contextRequest.execute("dlgConfirmarRegistroNotadebitoAutomatica.show();");
			} 
		}else {
			System.out.println("numerodocumentofacturacionfisica");
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
		
		
	}
	public void validarRegistroNotaDebitoPenalizacion(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		
		if (!sigeneracionautomaticanotadebitoPenalizacion && comprobantepagoPadreNotaTipo==null) {
			
			addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
		} else if (selectedCobrador==null) {
			addWarnMessage("", "Ingrese nombre cobrador");
		}else if (fechaemisionNotaDebito == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		}else if(comprobantepago.getIdtipopersona()==null ||comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
			addWarnMessage("","Usted no ha seleccionado el tipo de persona ");	
		}else if(comprobantepago.getDnirucpagante().trim().length()==0){
				addWarnMessage("","El campo de DNI/RUC se encuentra vacio");
		}else if(sifacturacionelectronica){
			 System.out.println("sifacturacionelectronica="+sifacturacionelectronica);
			if (!sigeneracionautomaticanotadebitoPenalizacion) {
				System.out.println("nota de debito manual ");
				contextRequest.execute("dlgConfirmarRegistroNotadebitoManual.show();");
				

			} else {
				System.out.println("notadebitoautomatica");
				contextRequest.execute("dlgConfirmarRegistroNotadebitoPenalizacionAutomatica.show();");
			} 
		
		}else {
			System.out.println("numerodocumentofacturacionfisica");
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
		
		
	}
	public void generacionNotaDebitoAutomatica(){
		sigeneracionautomaticanotadebito=Boolean.TRUE;
	}
	public void generacionNotaDebitoPenalizacionAutomatica(){
		sigeneracionautomaticanotadebitoPenalizacion=Boolean.TRUE;
	}
	public void validacionRegistrarComprobantefisico(){
		sivalidacioncomprobantefisico=Boolean.TRUE;
		validacionRegistrarComprobante();
	}
	public void registrarNuevoNotaDebito() {
		
		try{
			
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("08");
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto("07");
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechacancelacion(comprobantepago.getFechacancelacion());
			comprobantepago.setObservacion(comprobantepago.getObservacion());
			comprobantepago.setFechaemision(comprobantepago.getFechaemision());
			comprobantepago.setFechavencimiento(null);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSubtotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setTotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setSigeneropenalizacion(false);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			//Agregar Comprobante ref
			Comprobantepago cp = new Comprobantepago(comprobantepagoPadreNotaTipo.getIdcomprobante());
			comprobantepago.setComprobanteref(cp);
						
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			System.out.println("sifacturacionelectronica="+sifacturacionelectronica);
			if(sifacturacionelectronica){
				
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, nombrecarteraContrato, comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			//guardar lista de cuotas 
			//guardarlistaCuotasPendientesCondicionCancelar();
			//cargarDetalleComprobantePadreParaComprobanteNotaTipo();
			
		
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
				//CuotaBean cuotaBean= new CuotaBean();
				Cuota cuo= new Cuota();	
				cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				//cuo.setMorasoles(FuncionesHelper.redondear(totalmora, 2));
				//cuo.setMontosoles(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getMora(),2));
				/**/
//				cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				cuo.setFeccre(new Date());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				cuo.setMorasoles(cuo.getMorasoles()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMora(), 2));
				//cuo.setMontopenalizacion(cuo.getMontopenalizacion()+FuncionesHelper.redondear(totalpenalizacion, 2));
				cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
				//cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
				cuo.setNropagos(cuo.getNropagos()+1);
				Contrato c= new Contrato();
				c.setIdcontrato(condicionSeleccionado.getIdcontrato());
				cuo.setContrato(c);
				System.out.println("TIPO DE CAMBIO C ="+cuo.getTipocambio());
				//cuo.setCancelado("0");
				//cuo.setTipocambio(valorTipoCambio);
				
				System.out.println("IDCUOTA: "+ cuo.getIdcuota());
				System.out.println("IDCONTRATO: "+ c.getIdcontrato());
				System.out.println("CUOTA: "+ cuo.getContrato());
				
				cobranzaRecaudacionService.actualizarCuota(cuo);
				/**/
				Detallecuota detallecuota = new Detallecuota();
				
				detallecuota.setCuota(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				//detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				//detallecuota.setMora(FuncionesHelper.redondear(totalmora, 2));
				detallecuota.setMora(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMora(),2));
				detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(detallecuota.getMes())));
				detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				detallecuota.setMontosoles(0.0);
				detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
				//detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				detallecuota.setFeccre(new Date());
				detallecuota.setCancelado(listaCuotasPendientesCondicionCancelar.get(i).getCancelado());
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
				//detallecuota.setCancelado("1");
				getCarteraService().registrarDetalleCuota(detallecuota);
			}
			
			cobranzaRecaudacionService.apuntarComprobanteAnotadebito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante(),FuncionesHelper.redondearNum(totalmora,2));
			
			totalmora = FuncionesHelper.redondear(0.0, 2);
			addInfoMessage("Nuevo Registro","Se registro con exito el comprobante de nota de débito");
	
			
	
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}
public void registrarNuevoNotaDebitoPenalizacion() {
		
		try{
			
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("08");
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(Constante.TIPO_CONCEPTO_PENALIZACION);
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());		
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechacancelacion(comprobantepago.getFechacancelacion());
			comprobantepago.setObservacion(comprobantepago.getObservacion());
			comprobantepago.setFechaemision(comprobantepago.getFechaemision());
			comprobantepago.setFechavencimiento(null);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSubtotalsoles(FuncionesHelper.redondear(totalpenalizacion, 2));
			comprobantepago.setTotalsoles(FuncionesHelper.redondear(totalpenalizacion, 2));
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setMontopenalizacion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setSigeneropenalizacion(true);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			//Agregar Comprobante ref
			Comprobantepago cp = new Comprobantepago(comprobantepagoPadreNotaTipo.getIdcomprobante());
			comprobantepago.setComprobanteref(cp);
			
			
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){
				
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, nombrecarteraContrato, comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			//guardar lista de cuotas 
			guardarlistaCuotasPendientesCondicionCancelar();
			cargarDetalleComprobantePadreParaComprobanteNotaTipo();
			
		
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
				
				Cuota cuo= new Cuota();
				cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				//cuo.setMorasoles(FuncionesHelper.redondear(totalmora, 2));
				/**/
//				cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				cuo.setFeccre(new Date());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
				//cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				//cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				//cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				//cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+FuncionesHelper.redondear(totalmora, 2));
			    
				cuo.setMontopenalizacion(cuo.getMontopenalizacion()+listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
				
				//cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				//cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
				cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
				Contrato c= new Contrato();
				c.setIdcontrato(condicionSeleccionado.getIdcontrato());
				cuo.setContrato(c);
				
				//cuo.setCancelado("0");
				//cuo.setTipocambio(valorTipoCambio);
				
				System.out.println("IDCUOTA: "+ cuo.getIdcuota());
				System.out.println("IDCONTRATO: "+ c.getIdcontrato());
				System.out.println("CUOTA: "+ cuo.getContrato());
				
				cobranzaRecaudacionService.actualizarCuota(cuo);
				/**/
				Detallecuota detallecuota = new Detallecuota();
				
				detallecuota.setCuota(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				//detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setMora(FuncionesHelper.redondear(0.0, 2));
				detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(detallecuota.getMes())));
				detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				//detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
				detallecuota.setMontosoles(0.0);
				detallecuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
				detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
				//detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				detallecuota.setCancelado("1");
				detallecuota.setFeccre(new Date());
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
	
				getCarteraService().registrarDetalleCuota(detallecuota);
			}
			
			cobranzaRecaudacionService.apuntarComprobanteAnotadebitoPenalizacion(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante(),FuncionesHelper.redondearNum(totalpenalizacion,2));
			                           
			totalmora = FuncionesHelper.redondear(0.0, 2);
			totalpenalizacion = FuncionesHelper.redondear(0.0, 2);
			addInfoMessage("Nuevo Registro","Se registro con exito el comprobante de nota de débito");
	
			
	
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}
	public void registrarNotaDebitoManual() {
		System.out.println("notadebitomanual");
		System.out.println("condicionSeleccionado="+condicionSeleccionado.toString());
		
		try{
			
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			System.out.println("iddetallecartera ="+iddetallecartera);
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("08");
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto("07");
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
	
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechacancelacion(fechaemisionNotaDebito);
			comprobantepago.setObservacion(observacionnotadebito);
			comprobantepago.setFechaemision(fechaemisionNotaDebito);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSubtotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setTotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			
			
			
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){
				
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, nombrecarteraContrato, comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			
			cargarDetalleComprobantePadreParaComprobanteNotaTipo();
			
		
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
				
				Cuota cuo= new Cuota();
				cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				//cuo.setMorasoles(FuncionesHelper.redondear(totalmora, 2));
				//cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				/**/
//				cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				cuo.setFeccre(new Date());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto()+listaCuotasPendientesCondicionCancelar.get(i).getDeudaacuenta());
				cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMora(), 2));
				cuo.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion(), 2));
				cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
				cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
				Contrato c= new Contrato();
				c.setIdcontrato(condicionSeleccionado.getIdcontrato());
				cuo.setContrato(c);
				
				//cuo.setCancelado("0");
				//cuo.setTipocambio(valorTipoCambio);
				
				System.out.println("IDCUOTA: "+ cuo.getIdcuota());
				System.out.println("IDCONTRATO: "+ c.getIdcontrato());
				System.out.println("CUOTA: "+ cuo.getContrato());
				
				cobranzaRecaudacionService.actualizarCuota(cuo);
				
				Detallecuota detallecuota = new Detallecuota();
				
				detallecuota.setCuota(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				//detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setMora(FuncionesHelper.redondear(totalmora, 2));
				detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(detallecuota.getMes())));
				detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				detallecuota.setMontosoles(0.0);
				//detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				detallecuota.setFeccre(new Date());
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
	
				getCarteraService().registrarDetalleCuota(detallecuota);
			}
			

			cobranzaRecaudacionService.apuntarComprobanteAnotadebito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());
			
			
			addInfoMessage("Nuevo Registro","Se registro con exito el comprobante de pago");
	
			totalmora = FuncionesHelper.redondear(0.0, 2);

			limpiarCampos();
	
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}


	public void registrarNotaDebitoAutomatica() {


		try{
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("08");
			Tipomovimiento 	tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto("07");
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechaemision(fechaemisionNotaDebito);
			comprobantepago.setObservacion(observacionnotadebito);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setNrocomprobantepadre(nrocomprobantepadrenotadebito);
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSubtotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setTotalsoles(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setMontopenalizacion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			//Agregar comprobante ref
			Comprobantepago cp= new Comprobantepago();
			cp.setIdcomprobante(idcomprobantepadrenotadebito);
			comprobantepago.setComprobanteref(cp);
			
	
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){

				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, "08", nombrecarteraContrato,idtipodocpadrenotadebito);
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			//guardar lista de cuotas 
			guardarlistaCuotasPendientesCondicionCancelar();
			
			cargarDetalleComprobantePadreParaComprobanteNotaTipo();
		
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
				
				Cuota cuo= new Cuota();
				cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				/* ***************************agregar cuota*********************** */
				cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				//cuo.setMorasoles(FuncionesHelper.redondear(totalmora, 2));
				System.out.println("mora="+listaCuotasPendientesCondicionCancelar.get(i).getMora());
				cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				System.out.println("mora cuota ="+cuo.getMorasoles());
				
				/**/
//				cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				cuo.setFeccre(new Date());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				//cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto()); monto soles
				listaCuotasPendientesCondicionCancelar.get(i).setMoraacumulada(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()!=null? listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada():0.0);
				cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMora(), 2));
				listaCuotasPendientesCondicionCancelar.get(i).setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()!=null? listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion():0.0);
				//cuo.setMontopenalizacion(cuo.getMontopenalizacion()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion(), 2));
				cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
				cuo.setNropagos(cuo.getNropagos()+1);
				Contrato c= new Contrato();
				c.setIdcontrato(condicionSeleccionado.getIdcontrato());
				cuo.setContrato(c);
				
				//cuo.setCancelado("0");
				//cuo.setTipocambio(valorTipoCambio);
				
				System.out.println("IDCUOTA: "+ cuo.getIdcuota());
				System.out.println("IDCONTRATO: "+ c.getIdcontrato());
				System.out.println("CUOTA: "+ cuo.getContrato());
				
				cobranzaRecaudacionService.actualizarCuota(cuo);
				/* ***************************fin de agregar cuota*********************** */
				Detallecuota detallecuota = new Detallecuota();
				
				detallecuota.setCuota(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(detallecuota.getMes())));
				//detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setTipocambio(valorTipoCambio);
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				detallecuota.setMontosoles(0.0);
				detallecuota.setFeccre(new Date());
				detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
				detallecuota.setCancelado(listaCuotasPendientesCondicionCancelar.get(i).getCancelado());
			    detallecuota.setEstado(Constante.ESTADO_ACT);
			    detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
			   // detallecuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
			    detallecuota.setMontopenalizacion(0.0);
				getCarteraService().registrarDetalleCuota(detallecuota);
			}
			
			
			cobranzaRecaudacionService.apuntarComprobanteAnotadebito(idcomprobantepadrenotadebito,comprobantepago.getIdcomprobante());
			addInfoMessage("","Se registro con exito el comprobante de pago");
	
			totalmora = FuncionesHelper.redondear(0.0, 2);
	
			//penalizacion
			if (totalpenalizacion != 0.0) {
				
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				contextRequest.execute("dlgConfirmarRegistroNotaDebitoPenalizacion.show();");
				//nrocomprobantepadrenotadebito = comprobantepago.getNroserie() + "-"+ comprobantepago.getNrocomprobante();
				//fechaemisionNotaDebito = comprobantepago.getFechaemision();
				nombrecomprobanteNotadebito = "Nota de Débito";
				
				}else{
			limpiarCampos();
			}
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}
	//guardar cuotas pendientes para nota de debito 
	public void guardarlistaCuotasPendientesCondicionCancelar(){
		listaCuotasPendientesCondicionCancelarxNotaDebito.clear();
		for(int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++){
			CuotaBean cuota =new CuotaBean();
			cuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
			cuota.setMes(listaCuotasPendientesCondicionCancelar.get(i).getMes());
			cuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
			cuota.setMonto(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
			cuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
			cuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
			cuota.setObservacionpenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacionpenalizacion());
			listaCuotasPendientesCondicionCancelarxNotaDebito.add(cuota);
		}
	}
	public void registrarNotaDebitoPenalizacionAutomatica() {


		try{
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("08");
			Tipomovimiento 	tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(Constante.TIPO_CONCEPTO_PENALIZACION);
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechaemision(fechaemisionNotaDebito);
			comprobantepago.setObservacion(observacionnotadebitopenalizacion);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setNrocomprobantepadre(nrocomprobantepadrenotadebito);
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSubtotalsoles(FuncionesHelper.redondear(totalpenalizacion, 2));
			comprobantepago.setTotalsoles(FuncionesHelper.redondear(totalpenalizacion, 2));
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setSigeneropenalizacion(true);
			comprobantepago.setMontopenalizacion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			Comprobantepago cp = new Comprobantepago();
			cp.setIdcomprobante(idcomprobantepadrenotadebito);
			comprobantepago.setComprobanteref(cp);
			
			
			
	
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){

				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, "08", nombrecarteraContrato,idtipodocpadrenotadebito);
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			
			//guardar lista de cuotas 
			guardarlistaCuotasPendientesCondicionCancelar();
			cargarDetalleComprobantePadreParaComprobanteNotaTipo();
		
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
				
				Cuota cuo= new Cuota();
				cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				 //********* agregar a cuota*************************
				cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				/**/
//				cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//				cuo.setFeccre(new Date());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				//cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				listaCuotasPendientesCondicionCancelar.get(i).setMoraacumulada(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()!=null? listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada():0.0);
				//cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+FuncionesHelper.redondear(totalmora, 2));---mora soles
				listaCuotasPendientesCondicionCancelar.get(i).setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()!=null? listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion():0.0);
				cuo.setMontopenalizacion(cuo.getMontopenalizacion()+FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion(), 2));
				cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
				cuo.setNropagos(cuo.getNropagos()+1);
				Contrato c= new Contrato();
				c.setIdcontrato(condicionSeleccionado.getIdcontrato());
				cuo.setContrato(c);
				
				//cuo.setCancelado("0");
				//cuo.setTipocambio(valorTipoCambio);
				
				System.out.println("IDCUOTA: "+ cuo.getIdcuota());
				System.out.println("IDCONTRATO: "+ c.getIdcontrato());
				System.out.println("CUOTA: "+ cuo.getContrato());
				
				cobranzaRecaudacionService.actualizarCuota(cuo);
				 // ***************fin de agregar cuota ******************
				Detallecuota detallecuota = new Detallecuota();
				
				detallecuota.setCuota(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
				//detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setMora(0.0);
				detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(detallecuota.getMes())));
				//detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setTipocambio(valorTipoCambio);
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				//detallecuota.setMontosoles(0.0);
				//detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
				detallecuota.setMontosoles(0.0);
				detallecuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
				detallecuota.setFeccre(new Date());
				detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacionpenalizacion());
				detallecuota.setCancelado(listaCuotasPendientesCondicionCancelar.get(i).getCancelado());
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
				getCarteraService().registrarDetalleCuota(detallecuota);
			}
			
			
			//cobranzaRecaudacionService.apuntarComprobanteAnotadebito(idcomprobantepadrenotadebito,comprobantepago.getIdcomprobante());
			cobranzaRecaudacionService.apuntarComprobanteAnotaDebitoPenalizacion(idcomprobantepadrenotadebito,comprobantepago.getIdcomprobante());
			addInfoMessage("","Se registro con exito el comprobante de pago");
	
			totalmora = FuncionesHelper.redondear(0.0, 2);
			totalpenalizacion = FuncionesHelper.redondear(0.0, 2);
	
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}
	//Nota de Credito Comprobante 
	public void cargarDetalleComprobantePadreParaComprobanteNotaCredito(){
		try{
	    comprobantepago=cobranzaRecaudacionService.obtenerDetalleComprobantePago(comprobantepagoPadreNotaTipo.getIdcomprobante());
		System.out.println("tipodocu="+comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
		//comprobantepagoPadreNotaTipo.getTipodocu().setIdtipodocu(comprobantepagoNC.getIdtipdocu());
		System.out.println("tipodocu2="+comprobantepago.getTipodocu().getIdtipodocu());

		}catch(Exception e ){
			e.printStackTrace();
		}
		}
	public void cargarDetalleComprobantePadreParaComprobanteNotaTipo(){

		listaCuotasPendientesCondicionCancelar.clear();
		if (sigeneracionautomaticanotadebito||sigeneracionautomaticanotadebitoPenalizacion) {//Nota debito automatica
			if(condicionSeleccionado.getSireconocimientodeuda()){//caso reconocimiento  
				listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(idComprobantePagoPadrenotatipo,tipoDescuentoNotaCreditoSeleccionado,condicionSeleccionado.getIdcontrato());
			}else{
				listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(idComprobantePagoPadrenotatipo,tipoDescuentoNotaCreditoSeleccionado);
			}
			
		} else {//resto
			sifacturacionelectronica=comprobantepagoPadreNotaTipo.getSifacturacionelectronica();	
			if(condicionSeleccionado.getSireconocimientodeuda()){//caso reconocimiento 
				listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(comprobantepagoPadreNotaTipo.getIdcomprobante(),tipoDescuentoNotaCreditoSeleccionado,condicionSeleccionado.getIdcontrato());

			}else{
				listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(comprobantepagoPadreNotaTipo.getIdcomprobante(),tipoDescuentoNotaCreditoSeleccionado);
			}
			}
        
		if (tipoDescuentoNotaCreditoSeleccionado.equals("")) {
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				listaCuotasPendientesCondicionCancelar.get(i).setMonto(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				CuotaBean cuotaBean= new CuotaBean();
				cuotaBean=cobranzaRecaudacionService.obtenerValorCuotaPagada(condicionSeleccionado.getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getAnio(),listaCuotasPendientesCondicionCancelar.get(i).getMes());
				listaCuotasPendientesCondicionCancelar.get(i).setIdcuota(cuotaBean.getIdcuota());
				if(listaCuotasPendientesCondicionCancelarxNotaDebito!=null && listaCuotasPendientesCondicionCancelarxNotaDebito.size()>0){
				listaCuotasPendientesCondicionCancelar.get(i).setObservacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacion());
				listaCuotasPendientesCondicionCancelar.get(i).setObservacionpenalizacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacionpenalizacion());
				listaCuotasPendientesCondicionCancelar.get(i).setMontopenalizacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getMontopenalizacion());
				}
				}
			
		} else if (tipoDescuentoNotaCreditoSeleccionado.equals("Mora")) {
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				listaCuotasPendientesCondicionCancelar.get(i).setNrocuota(i+1);
				listaCuotasPendientesCondicionCancelar.get(i).setMonto(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setCondicion("Pendiente");
				listaCuotasPendientesCondicionCancelar.get(i).setSiacuenta(true);
				listaCuotasPendientesCondicionCancelar.get(i).setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				CuotaBean cuotaBean= new CuotaBean();
				cuotaBean=cobranzaRecaudacionService.obtenerValorCuotaPagada(condicionSeleccionado.getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getAnio(),listaCuotasPendientesCondicionCancelar.get(i).getMes());
				listaCuotasPendientesCondicionCancelar.get(i).setIdcuota(cuotaBean.getIdcuota());
				listaCuotasPendientesCondicionCancelar.get(i).setNropagos(cuotaBean.getNropagos());
				listaCuotasPendientesCondicionCancelar.get(i).setMoraacumulada(cuotaBean.getMora());
				if(listaCuotasPendientesCondicionCancelarxNotaDebito!=null && listaCuotasPendientesCondicionCancelarxNotaDebito.size()>0){
				listaCuotasPendientesCondicionCancelar.get(i).setObservacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacion());
				listaCuotasPendientesCondicionCancelar.get(i).setObservacionpenalizacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacionpenalizacion());
				listaCuotasPendientesCondicionCancelar.get(i).setMontopenalizacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getMontopenalizacion());
				}
				if(tipodocumentoSeleccionado.equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO_MORA)){
					cuotaBean.setObservacion(Constante.MAP_COM_DESCRIPCION_SGI.get("4")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					listaCuotasPendientesCondicionCancelar.get(i).setObservacion(cuotaBean.getObservacion());
				}
				}
			
			} else {
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				listaCuotasPendientesCondicionCancelar.get(i).setNrocuota(i+1);
				//listaCuotasPendientesCondicionCancelar.get(i).setMonto(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setMora(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setCondicion("Pendiente");
				listaCuotasPendientesCondicionCancelar.get(i).setSiacuenta(true);
				CuotaBean cuotaBean= new CuotaBean();
				cuotaBean=cobranzaRecaudacionService.obtenerValorCuotaPagada(condicionSeleccionado.getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getAnio(),listaCuotasPendientesCondicionCancelar.get(i).getMes());
				
				//listaCuotasPendientesCondicionCancelar.get(i).setDeudaacuenta(cuotaBean.getDeudaacuenta());
				listaCuotasPendientesCondicionCancelar.get(i).setDeudaacuenta(cuotaBean.getMonto());
				//listaCuotasPendientesCondicionCancelar.get(i).setMonto(cuotaBean.getMonto());
				listaCuotasPendientesCondicionCancelar.get(i).setMoraacumulada(cuotaBean.getMora());
				listaCuotasPendientesCondicionCancelar.get(i).setNropagos(cuotaBean.getNropagos());
				listaCuotasPendientesCondicionCancelar.get(i).setIdcuota(cuotaBean.getIdcuota());
				//observacion
				if(listaCuotasPendientesCondicionCancelarxNotaDebito!=null && listaCuotasPendientesCondicionCancelarxNotaDebito.size()>0){
					System.out.println("OBSERVACION="+listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacion());
					listaCuotasPendientesCondicionCancelar.get(i).setObservacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getObservacion());
					listaCuotasPendientesCondicionCancelar.get(i).setMontopenalizacion(listaCuotasPendientesCondicionCancelarxNotaDebito.get(i).getMontopenalizacion());
				}
				if(tipodocumentoSeleccionado.equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO_)){
					cuotaBean.setObservacion(Constante.MAP_COM_DESCRIPCION_SGI.get("3")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					listaCuotasPendientesCondicionCancelar.get(i).setObservacion(cuotaBean.getObservacion());
				}
				
				System.out.println("IDCUOTAs "+ cuotaBean.getIdcuota());
				
			}
		}
		if (tipoNotaDebitoyCredito != null) {
			if (tipoNotaDebitoyCredito.equals("Total")) {
				if (listaCuotasPendientesCondicionCancelar != null) {
					for (int i = 0; i < listaCuotasPendientesCondicionCancelar
							.size(); i++) {
						listaCuotasPendientesCondicionCancelar.get(i).setMonto(
								listaCuotasPendientesCondicionCancelar.get(i)
										.getMonto() * (-1));

					}
					calcularTotales();
				}
			}else{
				if (tipoNotaDebitoyCredito.equals("Parcial")){
				totalSubTotalComprobante=new BigDecimal("0.0");
				igvComprobante=new BigDecimal("0.0");
				totalComprobante=new BigDecimal("0.0");
				}
			}

		} else {
			if (tipoDescuentoNotaCreditoSeleccionado.equals("Mora")) {
				if (listaCuotasPendientesCondicionCancelar != null) {
					for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
						listaCuotasPendientesCondicionCancelar.get(i).setMora(
								listaCuotasPendientesCondicionCancelar.get(i).getMora()==0?
										listaCuotasPendientesCondicionCancelar.get(i).getMora():		
											listaCuotasPendientesCondicionCancelar.get(i).getMora() * (-1));

					}
					calcularTotales();
				}
			}
			
		}
		
	}

public void cargarDetalleComprobantePadreParaComprobanteNotaDebito(){
		
		if (sigeneracionautomaticanotadebito) {//Nota debito automatica
			
			listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(idComprobantePagoPadrenotatipo,tipoDescuentoNotaCreditoSeleccionado);
		} else {//resto
			System.out.println("tipoDescuentoNotaCreditoSeleccionado=="+tipoDescuentoNotaCreditoSeleccionado);
			sifacturacionelectronica=comprobantepagoPadreNotaTipo.getSifacturacionelectronica();	
			listaCuotasPendientesCondicionCancelar=cobranzaRecaudacionService.obtenerDetalleComprobantePadre(comprobantepagoPadreNotaTipo.getIdcomprobante(),tipoDescuentoNotaCreditoSeleccionado);
		}

		if (tipoDescuentoNotaCreditoSeleccionado.equals("")) {
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				listaCuotasPendientesCondicionCancelar.get(i).setMonto(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				CuotaBean cuotaBean= new CuotaBean();
				cuotaBean=cobranzaRecaudacionService.obtenerValorCuotaPagada(condicionSeleccionado.getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getAnio(),listaCuotasPendientesCondicionCancelar.get(i).getMes());
				listaCuotasPendientesCondicionCancelar.get(i).setIdcuota(cuotaBean.getIdcuota());
				
			}
			
		} else {
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				listaCuotasPendientesCondicionCancelar.get(i).setNrocuota(i+1);
				//listaCuotasPendientesCondicionCancelar.get(i).setMonto(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setMora(0.0);
				listaCuotasPendientesCondicionCancelar.get(i).setCondicion("Pendiente");
				listaCuotasPendientesCondicionCancelar.get(i).setSiacuenta(true);
				CuotaBean cuotaBean= new CuotaBean();
				cuotaBean=cobranzaRecaudacionService.obtenerValorCuotaPagada(condicionSeleccionado.getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getAnio(),listaCuotasPendientesCondicionCancelar.get(i).getMes());
				if(tipodocumentoSeleccionado.equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){
					if(tipoNotaDebitoyCredito.equals("Nuevo"))/**Mora*/ {
						cuotaBean.setObservacion(Constante.MAP_COM_DESCRIPCION_SGI.get("1")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					}else if (tipoNotaDebitoyCredito.equals(Constante.TIPO_CONCEPTO_PENALIZACION)){/**Penalización*/
						cuotaBean.setObservacion(Constante.MAP_COM_DESCRIPCION_SGI.get("2")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					}
						
				}
				//listaCuotasPendientesCondicionCancelar.get(i).setDeudaacuenta(cuotaBean.getDeudaacuenta());
				listaCuotasPendientesCondicionCancelar.get(i).setDeudaacuenta(cuotaBean.getMonto());
				listaCuotasPendientesCondicionCancelar.get(i).setMonto(FuncionesHelper.redondearNum(cuotaBean.getMonto(), 2));
				listaCuotasPendientesCondicionCancelar.get(i).setMoraacumulada(cuotaBean.getMora());
				listaCuotasPendientesCondicionCancelar.get(i).setNropagos(cuotaBean.getNropagos());
				listaCuotasPendientesCondicionCancelar.get(i).setIdcuota(cuotaBean.getIdcuota());
				listaCuotasPendientesCondicionCancelar.get(i).setObservacion(cuotaBean.getObservacion());
				//System.out.println("IDCUOTA "+ cuotaBean.getIdcuota());
				
			}
		}
	
	}

public void validacionRegistrarComprobanteNotaDebito() {
	RequestContext contextRequest = RequestContext.getCurrentInstance();
	if(tipodocumentoSeleccionado.equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO) && tipoNotaDebitoyCredito.equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
		if (!sigeneracionautomaticanotadebitoPenalizacion && comprobantepagoPadreNotaTipo==null) {			
			addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
		}else if (comprobantepago.getFechaemision() == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		}else if(comprobantepago.getTipodoc_receptor()==null||comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
			addWarnMessage("", "Seleccione el tipo de documento de la persona ");
		}else if(comprobantepago.getDnirucpagante().trim().length()==0){
			addWarnMessage("","El campo de N° Doc. Persona se encuentra vacio");
		}else if(selectedCobrador==null){
			addWarnMessage("", "Ingrese nombre cobrador");
		}else if(comprobantepago.getObservacion().equals("")){
			addWarnMessage("", "Ingrese una observación para continuar");
		}else if(totalpenalizacion.doubleValue()==0){
			addErrorMessage("","Usted debe ingresar el monto de la penalización para generar la nota de debito.");
		}else if(sifacturacionelectronica){
			 
			if (!sigeneracionautomaticanotadebitoPenalizacion) {
				contextRequest.execute("dlgConfirmarRegistroNuevoNotadebitoPenalizacion.show();");

			} else {
				contextRequest.execute("dlgConfirmarRegistroNotadebitoPenalizacionAutomatica.show();");
			} 
		
		}else {
			
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisicaNotaDebitoPenalizacion.show();");
		}
	}else{
		if (!sigeneracionautomaticanotadebito && comprobantepagoPadreNotaTipo==null) {
			
			addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
		}else if (comprobantepago.getFechaemision() == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		}else if(comprobantepago.getTipodoc_receptor()==null||comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
			addWarnMessage("", "Seleccione el tipo de documento de la persona ");
		}else if(comprobantepago.getDnirucpagante().trim().length()==0){
			addWarnMessage("","El campo de N° Doc. Persona se encuentra vacio");
		}else if(selectedCobrador==null){
			addWarnMessage("", "Ingrese nombre cobrador");
		}else if(comprobantepago.getObservacion().equals("")){
			addWarnMessage("", "Ingrese una observación para continuar");
		}else if(totalmora.doubleValue()==0){
			addErrorMessage("","Usted debe ingresar el monto de la mora para generar la nota de debito.");
		}else if(sifacturacionelectronica){
			 
			if (!sigeneracionautomaticanotadebito) {
				contextRequest.execute("dlgConfirmarRegistroNuevoNotadebito.show();");

			} else {
				contextRequest.execute("dlgConfirmarRegistroNotadebitoAutomatica.show();");
			} 
		
		}else {
			
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisicaNotaDebito.show();");
		}
	}
	
	
	
}
public void validacionRegistrarComprobanteNotaDebitoAux() {

	System.out.println("validar nota de debito ");
	System.out.println("respueste validarPOrUsoRecibo de caja ="+validarPorUsoReciboCaja());
	if (validarPorUsoReciboCaja()) {
		
	}else if(idtipodocuseleccionado.equals("09"))  {//
		
		if (comprobantepagoPadreNotaTipo==null) {
			
			addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
		}
		else if( ( tipodocumentoSeleccionado.equals("10")  && totalComprobante.doubleValue()==0) || ( tipodocumentoSeleccionado.equals("11") && totalmora.doubleValue()==0) ){
			addErrorMessage("","Usted debe ingresar el monto a rebajar de la renta pagada.");
		}
		else {
			RequestContext contextRequest = RequestContext
					.getCurrentInstance();
			if (comprobantepago.getFechaemision() == null) {
				addWarnMessage("", "Ingrese Fecha de Emisión");
			} 
//			else if (comprobantepago.getFechacancelacion() == null) {
//				addWarnMessage("", "Seleccione fecha de cancelación");
//			} 
			else if (selectedCobrador == null) {
				addWarnMessage("", "Ingrese nombre cobrador");
			} else if (comprobantepago.getObservacion().equals("")) {
				addWarnMessage("", "Ingrese una observación para continuar");
			} else {
				if (sifacturacionelectronica) {
					contextRequest
							.execute("dlgConfirmarRegistroNotaCreditoTipoDetalle.show();");
				} else {
					contextRequest
							.execute("dlgNumeroDocumentoFacturacionFisica.show();");
				}
			}
		}
	}else if (listaCuotasPendientesCondicionCancelar.size() == 0  ) {
		addWarnMessage("","Usted no ha agregado ninguna cuota, agrege cuota para registrar");
			
	}
//	else if (sifacturacionelectronica && listaCuotasPendientesCondicionCancelar.size() >1) {
//		addWarnMessage("","Solo puede pagar un mes de renta a la vez cuando paga por facturación electronica");
//	}
	
	else{
		
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		
		 if(sifacturacionelectronica || tipodocumentoSeleccionado.equals("DA") ){
			 
			 contextRequest.execute("dlgConfirmarRegistroCuota.show();");
		 }else {
			
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
			
	}
}
	public void registrarNotaCredito() {
		
		try{

			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
			
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu("09");
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")?"08":"13");
			Tipopago 		tipopago 		= new Tipopago("01");
			
			Usuario usuario= new Usuario();
			
			usuario.setIdusuario(selectedCobrador.getId());
	
			comprobantepago.setUsuario(usuario);
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			
			comprobantepago.setFechacreacion(new Date());
			
			
			if (tipoDescuentoNotaCreditoSeleccionado.equals("Renta")) {
				comprobantepago.setIgvsoles(igvComprobante.doubleValue());
				comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
				comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			} else {
				comprobantepago.setIgvsoles(0.0);
				comprobantepago.setSubtotalsoles(totalmora);
				comprobantepago.setTotalsoles(totalmora);
			}
			
			
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setSigeneronotadebito(false);//
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			System.out.println("tipoNotaDebitoyCredito="+tipoNotaDebitoyCredito);
			if(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")){
				switch(tipoNotaDebitoyCredito){
					case "Total":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"01");break;
					case "Parcial":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"05");break;
					case Constante.TIPO_NOTA_CREDITO_3 :comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"05");break;
					default:comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);
				}
			}else{
				comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);//tipo de descuento notadecredito  : Renta01 , Renta05 , Mora
			}
			//comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);//tipo de descuento notadecredito  : Renta01 , Renta05 , Mora
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			Comprobantepago cp = new Comprobantepago(comprobantepagoPadreNotaTipo.getIdcomprobante());
			comprobantepago.setComprobanteref(cp);
			
			

			
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, nombrecarteraContrato, comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			
			}else {
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			
				
				if(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")){
					// NOTA DE CRÉDITO - REBAJA DE RENTA
					
					for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {	
						
						// ACTUALIZA LA CUOTA A LA QUE ESTÁ REFERENCIADA LA NOTA DE CRÉDITO
						Cuota cuo = new Cuota();
						cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
						cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
						
						cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						cuo.setFecmod(new Date());
//						cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
//						cuo.setFeccre(new Date());
						cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						cuo.setMontosoles(FuncionesHelper.sumarDouble( listaCuotasPendientesCondicionCancelar.get(i).getMonto(),listaCuotasPendientesCondicionCancelar.get(i).getDeudaacuenta()));
						cuo.setMorasoles(FuncionesHelper.sumarDouble(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada(),listaCuotasPendientesCondicionCancelar.get(i).getMora()));
						cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
						Contrato c= new Contrato();
						c.setIdcontrato(condicionSeleccionado.getIdcontrato());
						cuo.setContrato(c);
						
						//cuo.setCancelado("0");
						//cuo.setTipocambio(valorTipoCambio);
						
						System.out.println("IDCUOTA: "+ cuo.getIdcuota());
						System.out.println("IDCONTRATO: "+ c.getIdcontrato());
						System.out.println("CUOTA: "+ cuo.getContrato());
						
						//cobranzaRecaudacionService.actualizarCuota(cuo);
						cobranzaRecaudacionService.actualizarCuotaNC(cuo);
						
		
						// REGISTRA EN EL DETALLE DE CUOTA LA NOTA DE CRÉDITO
						Detallecuota detallecuota = new Detallecuota();
						detallecuota.setCuota(cuo);
						
						detallecuota.setComprobantepago(comprobantepago);
						detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
						detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						//detallecuota.setTipocambio(cuo.getTipocambio());
						detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
		//				detallecuota.setMontodolar(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMonto()/valorTipoCambio, 2));
						detallecuota.setFeccre(new Date());
						detallecuota.setCancelado(cuo.getCancelado());
						detallecuota.setEstado(Constante.ESTADO_ACT);
						detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
						System.out.println("Tipo de Cambio ="+ comprobantepagoPadreNotaTipo.getTipcambio());
						detallecuota.setTipocambio(comprobantepagoPadreNotaTipo.getTipcambio());						
//						if(detalle_cuota.getObservacion().length()>0){
//							System.out.println("Observacion_des="+detalle_cuota.getObservacion());
//							detallecuota.setObservacion(detalle_cuota.getObservacion());
//						}
						detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
						System.out.println("comprobantepago"+ detallecuota.getComprobantepago());
						System.out.println(""+ detallecuota.getMes());

						
						
						getCarteraService().registrarDetalleCuota(detallecuota);
					
					}
				}else {
					// NOTA DE CRÉDITO - REBAJA DE MORA - NO SE USA
					for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {			
					Cuota cuo = new Cuota();
					cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
					
					//********* agregar a cuota*************************
					cuo=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
					
					cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					cuo.setFecmod(new Date());
					
					//cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
					//cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
					//cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto()+listaCuotasPendientesCondicionCancelar.get(i).getDeudaacuenta());
					cuo.setMorasoles(cuo.getMorasoles()+listaCuotasPendientesCondicionCancelar.get(i).getMora());
					//cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					//cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
					cuo.setNropagos(cuo.getNropagos()+1);
					
//					Contrato c= new Contrato();
//					c.setIdcontrato(condicionSeleccionado.getIdcontrato());
					//cuo.setContrato(c);
					
					//cuo.setCancelado("0");
					//cuo.setTipocambio(valorTipoCambio);
					
					cobranzaRecaudacionService.actualizarCuota(cuo);
					
	
					// Detalle
					Detallecuota detallecuota = new Detallecuota();
					detallecuota.setCuota(cuo);
					
					detallecuota.setComprobantepago(comprobantepago);
					detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
					detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
					detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
					detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
					//detallecuota.setTipocambio(cuo.getTipocambio());
					detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					detallecuota.setMontosoles(0.0);
	//				detallecuota.setMontodolar(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMonto()/valorTipoCambio, 2));
					detallecuota.setFeccre(new Date());
					detallecuota.setCancelado(cuo.getCancelado());
					detallecuota.setEstado(Constante.ESTADO_ACT);
					detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
					detallecuota.setObservacion(listaCuotasPendientesCondicionCancelar.get(i).getObservacion());
	
					getCarteraService().registrarDetalleCuota(detallecuota);
					
					}
				}
			    if(tipoNotaDebitoyCredito.equals(Constante.TIPO_NOTA_CREDITO_3)){
			    	cobranzaRecaudacionService.apuntarComprobanteAnotacredito2(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());
			    }else{
			    	cobranzaRecaudacionService.apuntarComprobanteAnotacredito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());
			    }
				
			addInfoMessage("","Se registro con exito la nota de Crédito");
			limpiarCampos();
	
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	
	public void calcularIgvSubtotalNotaCredito() {
			subtotalnotacredito =  FuncionesHelper.redondear(totalnotacredito/1.18, 2);
			igvnotacredito=  FuncionesHelper.redondear(totalnotacredito-subtotalnotacredito , 2);
	}

	
	/*************************************************************************************************************************************************************************************************/
	/*********************************************************************************************************** PAGO RENTA *************************************************************************/

	
	public void validacionRegistrarComprobanteDescontarRenta() {
	
		
		if (validarPorUsoReciboCaja()) {
			
		}else if(idtipodocuseleccionado.equals("09"))  {//
			
			if (comprobantepagoPadreNotaTipo==null) {
				
				addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
			}
			else if( ( tipodocumentoSeleccionado.equals("10")  && totalComprobante.doubleValue()==0) || ( tipodocumentoSeleccionado.equals("11") && totalmora.doubleValue()==0) ){
				addErrorMessage("","Usted debe ingresar el monto a rebajar de la renta pagada.");
			}
			else {
				RequestContext contextRequest = RequestContext
						.getCurrentInstance();
				if (comprobantepago.getFechaemision() == null) {
					addWarnMessage("", "Ingrese Fecha de Emisión");
				} 
//				else if (comprobantepago.getFechacancelacion() == null) {
//					addWarnMessage("", "Seleccione fecha de cancelación");
//				} 
//				else if(comprobantepago.getIdtipopersona()==null ||comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
//				addWarnMessage("","Usted no ha seleccionado el tipo de persona ");
//				}
				else if(comprobantepago.getTipodoc_receptor()==null || comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
				addWarnMessage("","Debe seleccionar un tipo de documento de persona para continuar. ");
				}else if(comprobantepago.getDnirucpagante().trim().length()==0){
				addWarnMessage("","El campo de N° Doc. se encuentra vacio");
				}
				else if (selectedCobrador == null) {
					addWarnMessage("", "Ingrese nombre cobrador");
				} else if (comprobantepago.getObservacion().equals("")) {
					addWarnMessage("", "Ingrese una observación para continuar");
				} else {
					if (sifacturacionelectronica) {
						contextRequest.execute("dlgConfirmarRegistroNotaCreditoNuevo.show();");
						//contextRequest.execute("dlgConfirmarRegistroNotaCredito.show();");
					} else {
//						 if(idtipodocuseleccionado.equals("09")){
//							 contextRequest
//								.execute("dlgNumeroDocumentoFacturacionFisicaNotaCredito.show();");
//						 }else{
						contextRequest
								.execute("dlgNumeroDocumentoFacturacionFisicaNotaCredito.show();");
						//}
					}
				}
			}
		}else if (listaCuotasPendientesCondicionCancelar.size() == 0  ) {
			addWarnMessage("","Usted no ha agregado ninguna cuota, agrege cuota para registrar");
				
		}
//		else if (sifacturacionelectronica && listaCuotasPendientesCondicionCancelar.size() >1) {
//			addWarnMessage("","Solo puede pagar un mes de renta a la vez cuando paga por facturación electronica");
//		}
		
		else{
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			
			 if(sifacturacionelectronica || tipodocumentoSeleccionado.equals("DA") ){
				 
				 contextRequest.execute("dlgConfirmarRegistroCuota.show();");
			 }else {
				
				 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
			}
				
		}
	}
	public void validacionRegistrarComprobante() {
		
		if (validarPorUsoReciboCaja()) {
			
		}else if(idtipodocuseleccionado.equals("09"))  {//
			
			if (comprobantepagoPadreNotaTipo==null) {
				
				addWarnMessage("", "Seleccione un comprobante referencia para continuar. ");
			}
			else if( ( tipodocumentoSeleccionado.equals("10")  && totalComprobante.doubleValue()==0) || ( tipodocumentoSeleccionado.equals("11") && totalmora.doubleValue()==0) ){
				addErrorMessage("","Usted debe ingresar el monto a rebajar de la renta pagada.");
			}
			else {
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				
				if(sifacturacionelectronica){

					contextRequest.execute("dlgConfirmarRegistroNotaCreditoTipoDetalle.show();");
				}else {
					 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
				}
				
			}
		}else if(comprobantepago.getFechaemision()==null){
			addWarnMessage("","Usted no ha seleccionado la fecha de emision ");
		}else if(comprobantepago.getTipodoc_receptor() ==null ||comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
			addWarnMessage("","Usted no ha seleccionado el tipo de documento de la persona ");
		}else if(comprobantepago.getDnirucpagante().trim().length()==0){
			addWarnMessage("","El campo de N° de documento se encuentra vacio");
		}else if(!validarNumeroDocPersona()|| comprobantepago.getDnirucpagante().equalsIgnoreCase(Constante.NRO_DOC_RECEPTOR_INVALIDO)){
			addWarnMessage("", "El numero de documento de la persona no es valido");
		}else if (listaCuotasPendientesCondicionCancelar.size() == 0  ) {
			addWarnMessage("","Usted no ha agregado ninguna cuota, agrege cuota para registrar");
		}else if (condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)&& listaCuotasPendientesCondicionCancelar.size() > 1){		
			addWarnMessage("","Solo considerar UNA CUOTA por pago de reconocimiento de deuda");
		}else if (listaCuotasPendientesCondicionCancelar.size() > 7  && sifacturacionelectronica) {
			addWarnMessage("","Usted supero el limite maximo de 7 cuotas para registrar");
//		else if (sifacturacionelectronica && listaCuotasPendientesCondicionCancelar.size() >1) {
//			addWarnMessage("","Solo puede pagar un mes de renta a la vez cuando paga por facturación electronica");
//		}
		
		}else{
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			
			 if(sifacturacionelectronica || tipodocumentoSeleccionado.equals("DA") ){
				 
				 contextRequest.execute("dlgConfirmarRegistroCuota.show();");
			 }else  {if((tipodocumentoSeleccionado.equals("01")||tipodocumentoSeleccionado.equals("03"))&& !sivalidacioncomprobantefisico){
				 				contextRequest.execute("dlgMensajeComprobanteFIsicoCuota.show();");
			 			}else{
			 					contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
			 			}
			 }
				
		}
	}
	public void validarNotaCreditoRenta(){
		if(comprobantepago.getFechaemision() == null){
			addWarnMessage("", "Ingrese Fecha de Emisión");
		}else 
			if (comprobantepago.getFechacancelacion()==null ){
				addWarnMessage("", "Seleccione fecha de cancelación");
			}
			else
				if (selectedCobrador==null) {
					addWarnMessage("", "Ingrese nombre cobrador");
				}
				else
					if (comprobantepago.getObservacion().equals("")) {
						addWarnMessage("", "Ingrese una observación para continuar");}

	}
	
	public void validarCabeceraPagoRenta(){
		
//		 if (cobranzaRecaudacionService.siExisteComprobanteRegistrado(comprobantepago.getNroserie(),comprobantepago.getNrocomprobante())) {
//			addErrorMessage("Error Registro", "Comprobante de pago no se puede registrar por que que ya se encuentra registrado en el sistema");
//		}
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			
//			if (numerooperacion.equals("") && comprobantepago.getFechacancelacion()!=null ) {
//				addWarnMessage("", "Ingrese una número de operación para continuar. ");
//				
//			}else 
//			if (numerooperacion.length()<7 && comprobantepago.getFechacancelacion()!=null ) {
//				addWarnMessage("", "Longitud de número de operación no puede ser menor a 8.");
//				
//			}else if (numerooperacion.length()>11 && comprobantepago.getFechacancelacion()!=null ) {
//				addWarnMessage("", "Longitud de número de operación no puede ser mayor a 11.");
//				
//			}else if (fechaoperacion.equals("") && comprobantepago.getFechacancelacion()!=null ) {
//				addWarnMessage("", "Ingrese una fecha de operación para continuar.");
//			}
			
		/**VALIDACION NUMERO OPERACION**/	
//			else if (cobranzaRecaudacionService.siExisteNumeroOperacion(fechaoperacion+"-"+numerooperacion)) {
//				System.out.println("fechaoperacion+"-"+numerooperacion: "+ fechaoperacion+"-"+numerooperacion)
//				addErrorMessage("", "Número de operación ya se encuentra registrado en el sistema");
//			}
//			else 
			
			if (idtipodocuseleccionado.equals("0")) {
				addWarnMessage("", "Seleccione Tipo de documento");
				
			}else if (idtipopagoseleccionado.equals("0")) {
				addWarnMessage("", "Seleccione Tipo de pago");
				
			}else if (idtipomovimientoseleccionado.equals("0")) {
				addWarnMessage("", "Seleccione Tipo de movimiento");
				
			}else if (selectedCobrador==null) {
				addWarnMessage("", "Ingrese nombre cobrador");
				
			}else if (sifacturacionelectronica) {
				
				if (comprobantepago.getFechaemision() == null) {
					addWarnMessage("", "Ingrese Fecha de Emisión");
					
				}else if (comprobantepago.getFechacancelacion()==null && comprobantepago.getFechavencimiento()==null ) {
					addWarnMessage("", "Seleccione fecha de cancelación o fecha de vencimiento");
					
				}else if (comprobantepago.getFechacancelacion()!=null && comprobantepago.getFechavencimiento()!=null ) {
					addWarnMessage("", "Seleccione solo fecha de cancelación o solo fecha de vencimiento");
					
				}else if (comprobantepago.getFechavencimiento()!=null) {
					
//					if (idtipodocuseleccionado.equals("01")) {
//						if (comprobantepago.getFechavencimiento().compareTo(maxFechaVencimiento)!=0) {
//							addWarnMessage("", "Seleccione fin de mes de fecha de vencimiento para continuar.");
//							
//						}else {
							contextRequest.execute("dlgPendientePagoRenta.show();");
							
//						}
						
//					} 
//					else if(idtipodocuseleccionado.equals("03")) {
//						Calendar fecha =Calendar.getInstance();
//						
//						fecha.setTime(comprobantepago.getFechaemision());
//						fecha.add(Calendar.DATE, 6);
//						
//						if (comprobantepago.getFechavencimiento().compareTo(fecha.getTime())>0) {
//							addWarnMessage("", "Seleccione una fecha de cancelación no mayor a 7 días que la fecha de emisión");
//							
//						}else {
//							contextRequest.execute("dlgPendientePagoRenta.show();");
//							
//						}
	//
//					}
					
				}else if (comprobantepago.getObservacion().equals("")) {
					addWarnMessage("", "Ingrese una observación para continuar");
					
				} 
//				else if (comprobantepago.getObservacion().length()<10) {
//					addWarnMessage("", "Observación debe tener al menos 10 caracteres");
//					
//				}
				else {
					contextRequest.execute("dlgPendientePagoRenta.show();");
					
				}
				
				
			} else {
				
				if (comprobantepago.getFechaemision() == null) {
					addWarnMessage("", "Ingrese Fecha de Emisión");
					
					
				}else if (comprobantepago.getFechacancelacion() == null) {
					addWarnMessage("", "Ingrese Fecha de Cancelación ");
					
				}else {
					contextRequest.execute("dlgPendientePagoRenta.show();");
				}
			}
			
		}
	

	public void validarNumeroDocumentoFacturacionFisica(){
		
		if (serieFacturacionFisica.equals("")) {
			addWarnMessage("", "Ingrese numero de serie para continuar.");
		} else if (serieFacturacionFisica.length()!=4) {
			addWarnMessage("", "El número de serie debe tener 4 dígitos.");
		} else if (correlativoFacturacionFisica.equals("")) {
			addWarnMessage("", "Ingrese correlativo para continuar.");
		} else if (correlativoFacturacionFisica.length()!=6) {
			addWarnMessage("", "El número correlativo debe tenet 6 dígitos.");
		} else {
			
		 RequestContext contextRequest = RequestContext.getCurrentInstance();
			 
		 
			if(idtipodocuseleccionado.equals("08")){
				if(tipoNotaDebitoyCredito.equals("Nuevo")){ //nota de debito mora nuevo 
					contextRequest.execute("dlgConfirmarRegistroNuevoNotadebito.show();");
				}else if(tipoNotaDebitoyCredito.equals("Pendiente")){
					contextRequest.execute("dlgConfirmarRegistroNotadebitoManual.show();");
				}else{ if(tipoNotaDebitoyCredito.equals(Constante.TIPO_CONCEPTO_PENALIZACION)){//nota de debito penalizacion 
								contextRequest.execute("dlgConfirmarRegistroNuevoNotadebitoPenalizacion.show();");
						}else{
								contextRequest.execute("dlgConfirmarRegistroNotadebitoManual.show();");}
					}
				 
			 }else if(sigeneracionautomaticanotadebito) {
				 contextRequest.execute("dlgConfirmarRegistroNotadebitoAutomatica.show();");
			 }else if(sigeneracionautomaticanotadebitoPenalizacion) {
					 contextRequest.execute("dlgConfirmarRegistroNotadebitoPenalizacionAutomatica.show();");	
			 }else if(idtipodocuseleccionado.equals("09")){
				 contextRequest.execute("dlgConfirmarRegistroNotaCreditoNuevo.show();");
				 //contextRequest.execute("dlgConfirmarRegistroNotaCredito.show();");
				 
			 }else if(idtipodocuseleccionado.equals("04")){
				 contextRequest.execute("dlgConfirmarRegistroReciboCaja.show();");
				 
			 }else if(tipodocumentoSeleccionado.equals("99")){
				 contextRequest.execute("dlgConfirmarRegistroPagoOtro.show();");
				 
			 }else if(tipodocumentoSeleccionado.equals("NC")){
				 contextRequest.execute("dlgConfirmarRegistroPagoOtroNotaCredito.show();");
				 
			 }else if(tipodocumentoSeleccionado.equals("ND")){
				 contextRequest.execute("dlgConfirmarRegistroPagoOtroNotaDebito.show();");
				 
			 } else {
				 contextRequest.execute("dlgConfirmarRegistroCuota.show();");
			 }
		}
		
	}
	

	public String cargarInformacionSinContratoSeleccionado() {
		
		limpiarListaCuotasPendientesCondicionCancelar();
		nrocuotaPendienteCancelacion = 0;
		varSesionVTC.setAttribute("tipocambio", FuncionesHelper.redondear(valorTipoCambio,3));
		Object sesionUserName = (Object) varSesionVTC.getAttribute("tipocambio");
		
		inicializandoValores();
		condicion = condicionSeleccionado.getCondicion();
		
		if (condicionSeleccionado.getMoneda().equals("S")) {
			montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1(), 2);
		} else {
			montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1()*valorTipoCambio, 2);
		}
		
		listaCuotasPendientesCondicion=recaudacionReporteService.generarPendientesParaCobroSinContrato(condicionSeleccionado.getIdcontrato(), valorTipoCambio);
		//listaCuotasPendientesCondicionInicial=listaCuotasPendientesCondicion;
		return "ok";
		
	}
	
	public void cargarInformacionSinContratoCompromisoSeleccionadoDolares(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		System.out.println("cargarInformacionSinContratoCompromisoSeleccionadoDolares");
		System.out.println("Tipo documento seleccionado: "+tipodocumentoSeleccionado);
		
//		if (!valorTipoCambio.equals(0.0)) {
//			if(tipodocumentoSeleccionado.equals("DA")){
//				cargarInformacionSinContratoSeleccionado();
//				contextRequest.execute("dlgRegistroDocumentoAdministrativo.show();");
//			}
//			else{
//				cargarInformacionSinContratoSeleccionado();
//				contextRequest.execute("dlgRegistroCuotaSinContrato.show();");
//			}
//		} 
//		else {
//			addWarnMessage("Ingrese el tipo de cambio","Para poder agregar cuota debe agregar el tipo de cambio");
//		}
		if (!valorTipoCambio.equals(0.0)) {
			cargarInformacionSinContratoSeleccionado();
			if(tipodocumentoSeleccionado.equals("DA")){
				//cargarInformacionContratoSeleccionado();
				contextRequest.execute("dlgRegistroDocumentoAdministrativo.show();");
			}else{
			contextRequest.execute("dlgRegistroCuotaSinContrato.show();");}
		} else {
			addWarnMessage("Ingrese el tipo de cambio","Para poder agregar cuota debe agregar el tipo de cambio");
		}
	}
	
	public void cargarInformacionContratoSeleccionadoDolares(){
		
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		System.out.println("cargarInformacionContratoCompromisoSeleccionadoDolares");
		System.out.println("valorTipoCambio="+valorTipoCambio);
		if (!valorTipoCambio.equals(0.0) ) {
			if(tipodocumentoSeleccionado.equals("DA")){
				cargarInformacionContratoSeleccionado();
				contextRequest.execute("dlgRegistroDocumentoAdministrativo.show();");
			}
			else{
			if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
				cargarInformacionReconocimientoDeudaSeleccionado();
			}else{
				cargarInformacionContratoSeleccionado();
			}
			contextRequest.execute("dlgRegistroCuotaSinContrato.show();");
			}
		} 
		else {
			addWarnMessage("Ingrese el tipo de cambio","Para poder agregar cuota debe agregar el tipo de cambio");
		}
		
//		if (!valorTipoCambio.equals(0.0)) {
//			cargarInformacionContratoSeleccionado();
//			contextRequest.execute("dlgRegistroCuotaSinContrato.show();");
//		} else {
//			addWarnMessage("Ingrese el tipo de cambio","Para poder agregar cuota debe agregar el tipo de cambio");
//
//		}
		
	}
	
	public String cargarInformacionContratoSeleccionado() {
		
		try {
		
		limpiarListaCuotasPendientesCondicionCancelar();
		nrocuotaPendienteCancelacion = 0;
		
		inicializandoValores();
		condicion = condicionSeleccionado.getCondicion();
		
		if (condicionSeleccionado.getMoneda().equals("S")) {
			montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1(), 2);
		} else {
			montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1()*valorTipoCambio, 2);
		}
		System.out.println("condicionSeleccionado.getSinuevomantenimiento()=="+condicionSeleccionado.getSinuevomantenimiento());
		if (condicionSeleccionado.getSinuevomantenimiento()!=null && condicionSeleccionado.getSinuevomantenimiento()) {
			
			listaCuotasPendientesCondicion=recaudacionReporteService.generarRentasPendientesParaCobroNuevoContrato(condicionSeleccionado.getIdcontrato(), valorTipoCambio);
			
		} else {
			listaCuotasPendientesCondicion=recaudacionReporteService.generarPendientesParaCobroContrato(condicionSeleccionado.getIdcontrato(), valorTipoCambio);
		}
		//BUSCAR LISTA DE CONTRATOS POR CLAVE UPA
		//System.out.println("clave UPA="+listaCuotasPendientesCondicion.toString());
		//listaContratos=recaudacionReporteService.buscarcontratos(listaCuotasPendientesCondicion.get(1).getClave())
		
		
		
		Integer nrocuotas=condicionSeleccionado.getNumerocuotas();
		
			if (nrocuotas<=12) {
				
				if (condicionSeleccionado.getCuota1()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota1(),2);
					
				}else {
					addErrorMessage("Falta actualizar en monto de renta", "No se puede agregar pago, por favor actualizar Contrato");
				} 
				
			}else if (nrocuotas<=24) {
				if (condicionSeleccionado.getCuota2()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota2(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del segundo añó", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=36) {
				if (condicionSeleccionado.getCuota3()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota3(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del tercer año", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=48) {
				if (condicionSeleccionado.getCuota4()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota4(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del cuarto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=60) {
				if (condicionSeleccionado.getCuota5()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota5(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del quinto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
			}else {
				if (condicionSeleccionado.getCuota6()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota6(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del sexto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
			
			return "ok";
	}
	
	public String  cargarInformacionReconocimientoDeudaSeleccionado(){
		try{
			limpiarListaCuotasPendientesCondicionCancelar();
			nrocuotaPendienteCancelacion = 0;
			
			inicializandoValores();
			condicion = condicionSeleccionado.getCondicion();
			
			if (condicionSeleccionado.getMoneda().equals("S")) {
				montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1(), 2);
			} else {
				montomensualrenta = FuncionesHelper.redondear(condicionSeleccionado.getCuota1()*valorTipoCambio, 2);
			}
			if (condicionSeleccionado.getSinuevomantenimiento()!=null && condicionSeleccionado.getSinuevomantenimiento()) {
				
				listaCuotasPendientesCondicion=recaudacionReporteService.generarRentasPendientesParaCobroReconocimientoDeuda(condicionSeleccionado.getIdcontrato(), valorTipoCambio);
				
			}
			Integer nrocuotas=condicionSeleccionado.getNumerocuotas();
			
			if (nrocuotas<=12) {
				
				if (condicionSeleccionado.getCuota1()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota1(),2);
					
				}else {
					addErrorMessage("Falta actualizar en monto de renta", "No se puede agregar pago, por favor actualizar Contrato");
				} 
				
			}else if (nrocuotas<=24) {
				if (condicionSeleccionado.getCuota2()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota2(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del segundo añó", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=36) {
				if (condicionSeleccionado.getCuota3()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota3(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del tercer año", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=48) {
				if (condicionSeleccionado.getCuota4()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota4(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del cuarto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
				
			}else if (nrocuotas<=60) {
				if (condicionSeleccionado.getCuota5()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota5(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del quinto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
			}else {
				if (condicionSeleccionado.getCuota6()!=null) {
					montoCuotaContrato = FuncionesHelper.redondear(condicionSeleccionado.getCuota6(),2);
				}else {
					addErrorMessage("Falta actualizar en monto de renta del sexto año", "No se puede agregar pago, por favor actualizar Contrato");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "ok";
	}
	public void cargarInformacionDolares(){
		System.out.println("------------------------------------------");
		System.out.println("condicionSeleccionado.getCondicion()="+condicionSeleccionado.getCondicion());
		System.out.println("TIPO DE CAMBIO $ ="+FuncionesHelper.redondearNum(valorTipoCambio,3));
		varSesionVTC.setAttribute("tipocambio", FuncionesHelper.redondearNum(valorTipoCambio,3));
		Object sesionUserName = (Object) varSesionVTC.getAttribute("tipocambio");
		//if (condicionSeleccionado.getCondicion().equals("Contrato") || condicionSeleccionado.getCondicion().equals("Precontrato")||(condicionSeleccionado.getCondicion().equals("Sin contrato") && tipodocumentoSeleccionado.equals("DA"))) {
		if (condicionSeleccionado.getCondicion().equals("Contrato") || condicionSeleccionado.getCondicion().equals("Precontrato")|| condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)) {
			cargarInformacionContratoSeleccionadoDolares();
		}else {
			cargarInformacionSinContratoCompromisoSeleccionadoDolares();
		}
		
	}
	
	public void registrarcomprobantedetalle(Comprobantepago comp){
		//
		Sunat_Comprobante scomp= new Sunat_Comprobante();
		Sunat_Comprobante_Detalle scompd= new Sunat_Comprobante_Detalle();
		scomp=cobranzaRecaudacionService.obtenerComprobante(comp);
		try{
		for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
			String conceptoref="",ref="Renta";
			scompd.setSunat_comprobante(scomp);
			scompd.setNroitem(i+1);
			//scompd.setCodigo_articulo(codigo_articulo);
			scompd.setCodigo_unidad("NIU");
			scompd.setDescripcion_articulo(Constante.MAP_TIPO_MOVIMIENTO_SGI.get(Constante.TIPO_MOVIMIENTO_PAGO_RECONOCIMIENTO_DEUDA)+" Cuota "+listaCuotasPendientesCondicionCancelar.get(i).getNumerocuota());
			
			for(MaeDetCondicionDetalleBean  maed :listaCuotasPendientesCondicionCancelar.get(i).getMaecondiciondetalle()){
				conceptoref=conceptoref+" "+maed.getMes().substring(0, 3)+maed.getAnio()+",";
			}
			scompd.setDescripcion_articulo(scompd.getDescripcion_articulo()+" - "+ref+conceptoref.replaceAll(",$", ""));
			scompd.setDescripcion_adicional("");
			scompd.setCantidad(new BigDecimal("1.0"));
			scompd.setPrecio_unitario_sinigv(new BigDecimal(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMonto()/1.18,2)));
			scompd.setImporte_subtotal(new BigDecimal(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMonto()/1.18,2)));
			scompd.setImporte_descuento(new BigDecimal("0.0"));
			scompd.setImporte_valorventa(new BigDecimal(FuncionesHelper.redondear(listaCuotasPendientesCondicionCancelar.get(i).getMonto()/1.18,2)));
			scompd.setImporte_igv(new BigDecimal(FuncionesHelper.redondear((listaCuotasPendientesCondicionCancelar.get(i).getMonto()/1.18)*0.18,2)));
			scompd.setImporte_isc(new BigDecimal("0.0"));
			scompd.setPrecio_unitario_conigv(new BigDecimal(listaCuotasPendientesCondicionCancelar.get(i).getMonto()));
			scompd.setImporte_total(new BigDecimal(listaCuotasPendientesCondicionCancelar.get(i).getMonto()));
			scompd.setTipo_afeccionigv("10");
			scompd.setEsgravado(true);
			scompd.setEsexonerado(false);
			scompd.setEsinafecto(false);
			scompd.setEsgratuito(false);
					
			
		}
		cobranzaRecaudacionService.grabarSunatComprobanteDetalle(scompd);
		}catch(Exception e){
			e.printStackTrace();
			addErrorMessage("", e.getMessage());
			Logger.error(e.getMessage(), e);
		}
		
	}
	public void registrarDetalleCuotaReconocimientoDeuda(){
      
		try{
			for (CuotaBean cuobean: listaCuotasPendientesCondicionCancelar) {//CUOTA DE RECONOCIMIENTO
				
				for(MaeDetCondicionDetalleBean maedcondbean:cuobean.getMaecondiciondetalle() ){// DETALLE DE DEUDA 
						if(maedcondbean.getSiacuenta()){
				
						}else{
							
						}
				}
			}
		}catch(Exception e ){
			e.printStackTrace();
			addErrorMessage("",e.getMessage());
			Logger.error(e.getMessage(), e);
		}
		
		
	}
	public String generarDescripcionReconocimiento(List<CuotaBean> listaCuota){
		String descripcion="";
		try{ 
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
				String conceptoref="",ref="Renta";
				descripcion=Constante.MAP_TIPO_MOVIMIENTO_SGI.get(Constante.TIPO_MOVIMIENTO_PAGO_RECONOCIMIENTO_DEUDA)+" Cuota "+listaCuotasPendientesCondicionCancelar.get(i).getNumerocuota().replaceFirst("^0+","")+"/"+condicionSeleccionado.getNumerocuotas();
				for(CuotaBean  cuo :listaCuotasPendientesCondicionCancelar.get(i).getListacuota()){
					conceptoref=conceptoref+" "+cuo.getMes().substring(0, 3)+cuo.getAnio()+",";
				}
				descripcion= descripcion +" - "+ref+conceptoref.replaceAll(",$", "");	
			}
			}catch(Exception e){
				e.printStackTrace();
				addErrorMessage("", e.getMessage());
				Logger.error(e.getMessage(), e);
			}
		descripcion=descripcion.length()>=200? descripcion.substring(0, 200) :descripcion;
		return descripcion;
	}
	
	public void registrarPagoRenta() { 
		
		try{
			/**Actualizo el tipo de cambio */
			valorTipoCambio=(Double)FuncionesHelper.getTipoCambio();
			
			
			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
					
			iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
			System.out.println("ID contrato seleccionado "+condicionSeleccionado.getIdcontrato());
			System.out.println("Clave upa seleccionado: "+condicionSeleccionado.getClaveUpa());
			System.out.println("");
			System.out.println("");
			System.out.println("");
			Detallecartera dc = new Detallecartera();
			dc.setIddetallecartera(iddetallecartera);
	
			Tipodocu 		tipodocu 		= new Tipodocu(idtipodocuseleccionado);
			Tipomovimiento  tipomovimiento  = new Tipomovimiento(idtipomovimientoseleccionado);
			//Tipoconcepto 	tipoconcepto	= new Tipoconcepto("06");
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(condicion.equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)? Constante.TIPO_CONCEPTO_RECONO_DEUDA:Constante.TIPO_CONCEPTO_RENTA);
			Tipopago 		tipopago 		= new Tipopago(idtipopagoseleccionado);
			
			Usuario usuario= new Usuario();
			
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
			comprobantepago.setDetallecartera(dc);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipopago(tipopago);
			
			comprobantepago.setIgvsoles(igvComprobante.doubleValue());
			comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
			comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setNrocomprobantepadre("");
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotacredito(comprobantepago.getTipodocu().getIdtipodocu().equals("09")? true:false);
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(totalmora, 2));
			comprobantepago.setSigeneronotadebito(totalmora>0?true:false);
			comprobantepago.setTipcambio(valorTipoCambio);
			comprobantepago.setNumerooperacion(fechaoperacion+"-"+numerooperacion);
			//penalizacion
			comprobantepago.setSigeneropenalizacion(totalpenalizacion>0?true:false);
			comprobantepago.setMontopenalizacion(FuncionesHelper.redondear(totalpenalizacion, 2));
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			
			
			if (idtipodocuseleccionado.equals("01")) {
				if(sifacturacionelectronica){
					Tipotransaccion tipotransaccion = new Tipotransaccion(idtipotransaccionseleccionado);
					comprobantepago.setTipotransaccion(tipotransaccion);
				}
				
				if (totalComprobante.doubleValue() >= 700) {
					comprobantepago.setSidetraccion(true);
					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(montoDetraccion.doubleValue(), 2));
				} else {
					comprobantepago.setSidetraccion(false);
					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
				}
			} else {
				comprobantepago.setSidetraccion(false);
				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			}
			
			
			/** Registro de referencia a recibo de caja para los comprobante por uso de carta fianza o por uso de garantia **/
			if (idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) || idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOGARANTIA) ) {
				
				
				reciboCajaReferencia.setSirecibocajausado(Boolean.TRUE);
				reciboCajaReferencia.setMontorecibocajausado(totalComprobante.doubleValue());
				cobranzaRecaudacionService.actualizarComprobantePago(reciboCajaReferencia);
				
				Comprobantepago reciboCaja= new Comprobantepago();
				reciboCaja.setIdcomprobante(reciboCajaReferencia.getIdcomprobante());
				comprobantepago.setReciboCaja(reciboCaja);
			}
	
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(sifacturacionelectronica){
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				
				cobranzaRecaudacionService.grabarBoletaFacturaFE(comprobantepago,idtipodocuseleccionado,nombrecarteraContrato);	
				//comprobantedetalle
//				if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
//					registrarcomprobantedetalle(comprobantepago);
//				}
				
				
			}else {
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			}
			
			
			
			idcomprobantepadrenotadebito=comprobantepago.getIdcomprobante();
			idtipodocpadrenotadebito=idtipodocuseleccionado;
			//obtener las cuotas iniciales.
			listaCuotasPendientesCondicionInicial=recaudacionReporteService.generarPendientesParaCobroSinContrato(condicionSeleccionado.getIdcontrato(), valorTipoCambio);
			for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
	
					if (listaCuotasPendientesCondicionCancelar.get(i).getSiacuenta()) {// pendiente
						
						Cuota cuo = new Cuota();
						cuo.setIdcuota(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
						cuo= cobranzaRecaudacionService.obtenerCuota(cuo.getIdcuota());
						cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						cuo.setFecmod(new Date());
						cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));						
						cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto()+listaCuotasPendientesCondicionCancelar.get(i).getDeudaacuenta());
						//cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+listaCuotasPendientesCondicionCancelar.get(i).getMora());
						//cuo.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()+FuncionesHelper.redondear(totalpenalizacion, 2));
						//cuo.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
						cuo.setMontopenalizacion(0.0);
						cuo.setMorasoles(0.0);
						
						cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						
						if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
							cuo.setNromes(Integer.parseInt( cuo.getMespagado().replaceFirst("^0+","")));
						}else {
							cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						}
						cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
						
						Contrato c= new Contrato();
						c.setIdcontrato(condicionSeleccionado.getIdcontrato());
						cuo.setContrato(c);
						cuo.setTipocambio(valorTipoCambio);
						
						if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Cancelado")) {
							cuo.setCancelado("1");
						} else if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente"))  {
							cuo.setCancelado("0");
						}else {
							cuo.setCancelado("2");
						}
						
						
						if (condicionSeleccionado.getSinuevomantenimiento()) {
							MaeDetalleCondicion maeDetalleCondicion= new MaeDetalleCondicion();
							maeDetalleCondicion.setIdDetalleCondicion(listaCuotasPendientesCondicionCancelar.get(i).getIddetallecondicion());
							cuo.setMaeDetalleCondicion(maeDetalleCondicion);
						}
						
						cobranzaRecaudacionService.actualizarCuota(cuo);
						if(listaCuotasPendientesCondicionCancelar.get(i).getMora()>=0){
							listaCuotasPendientesCondicionCancelar.get(i).setObservacion("Cancelación Mora "+cuo.getMespagado().substring(0, 3)+" - "+cuo.getAniocuotames()+" ");
						}
						if(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()>=0){
							listaCuotasPendientesCondicionCancelar.get(i).setObservacionpenalizacion("Cancelación Penalización "+cuo.getMespagado().substring(0, 3)+" - "+cuo.getAniocuotames()+" ");
						}
						Detallecuota detallecuota = new Detallecuota();
						detallecuota.setCuota(cuo);
						detallecuota.setComprobantepago(comprobantepago);
						detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
						detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						detallecuota.setTipocambio(cuo.getTipocambio());
						detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
						detallecuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
						if(condicionSeleccionado.getMoneda().equals("D")){
							detallecuota.setMontodolar(FuncionesHelper.redondearNum(detallecuota.getMontosoles()/detallecuota.getTipocambio(), 2));
						}
						detallecuota.setFeccre(new Date());
						//estado de detallecuota
						detallecuota.setCancelado(cuo.getCancelado());
						detallecuota.setObservacion(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)? generarDescripcionReconocimiento(listaCuotasPendientesCondicionCancelar) :detallecuota.getObservacion());
						detallecuota.setEstado(Constante.ESTADO_ACT);
						detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
						
						getCarteraService().registrarDetalleCuota(detallecuota);
						
	
					} else {
						
						Cuota cuo = new Cuota();
						cuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						cuo.setFeccre(new Date());
						cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						cuo.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
//						cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMora());
//						cuo.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
						cuo.setMorasoles(0.0);
						cuo.setMontopenalizacion(0.0);
						cuo.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
							cuo.setNromes( Integer.parseInt( cuo.getMespagado().replaceFirst("^0+","")));
						}else{
							cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						}
						cuo.setNropagos(1);
						
						
						Contrato c= new Contrato();
						c.setIdcontrato(condicionSeleccionado.getIdcontrato());
						cuo.setContrato(c);
						cuo.setTipocambio(valorTipoCambio);
						if(sifacturacionelectronica){
							if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Cancelado")) {
								cuo.setCancelado("1");
							} else if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente"))  {
								cuo.setCancelado("0");
							}else {
								cuo.setCancelado("2");
							}
						}
						else{
						if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Cancelado")||(listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente")&& comprobantepago.getFechacancelacion()!=null)) {
							boolean cancelado= false;
							if(listaCuotasPendientesCondicionInicial.size()>0){
							for(int k =0;k<listaCuotasPendientesCondicionInicial.size();k++){
								if(Double.toString(listaCuotasPendientesCondicionCancelar.get(i).getMonto()).equals(Double.toString(listaCuotasPendientesCondicionInicial.get(k).getMonto()))&&listaCuotasPendientesCondicionCancelar.get(i).getAnio()==listaCuotasPendientesCondicionInicial.get(k).getAnio()
									&&listaCuotasPendientesCondicionCancelar.get(i).getMes().equals(listaCuotasPendientesCondicionInicial.get(k).getMes())){				
									cancelado=true;
								}
							}
							}
							if(cancelado){
								cuo.setCancelado("1");//  mismo monto
							}else{
								cuo.setCancelado("0");//  diferente monto 
							}
							
						}else if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente")&& comprobantepago.getFechacancelacion()==null)  {
							cuo.setCancelado("0");
						}else {
							cuo.setCancelado("2");
						}
						}
						if (condicionSeleccionado.getSinuevomantenimiento()) {
							MaeDetalleCondicion maeDetalleCondicion= new MaeDetalleCondicion();
							maeDetalleCondicion.setIdDetalleCondicion(listaCuotasPendientesCondicionCancelar.get(i).getIddetallecondicion());
							cuo.setMaeDetalleCondicion(maeDetalleCondicion);
						}
						// REGISTRA ID DE COMPROBANTEPAGO EN CUOTAS 
						Comprobantepago cmp=new Comprobantepago();
						cmp.setIdcomprobante(comprobantepago.getIdcomprobante());
						cuo.setComprobantepago(cmp);
						
						
						getCarteraService().registrarCuota(cuo);
						//para descripcion de mora y penalizacion
						// Detalle
						Detallecuota detallecuota = new Detallecuota();
						detallecuota.setCuota(cuo);
						
						detallecuota.setComprobantepago(comprobantepago);
						detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
						detallecuota.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getAnio());
						detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
						detallecuota.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion());
						detallecuota.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMespagado())));
						if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
							detallecuota.setNumeromes( Integer.parseInt( cuo.getMespagado().replaceFirst("^0+","")));
						}
						detallecuota.setTipocambio(cuo.getTipocambio());
						detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
						detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
						if(condicionSeleccionado.getMoneda().equals("D")){
						//detallecuota.setMontodolar(listaCuotasPendientesCondicionCancelar.get(i).getMontodolar());
							detallecuota.setMontodolar(FuncionesHelper.redondearNum(detallecuota.getMontosoles()/detallecuota.getTipocambio(), 2));
						}
						detallecuota.setFeccre(new Date());
						//estado detallecuota
						detallecuota.setCancelado(cuo.getCancelado());
						detallecuota.setObservacion(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)? generarDescripcionReconocimiento(listaCuotasPendientesCondicionCancelar) :detallecuota.getObservacion());
						detallecuota.setEstado(Constante.ESTADO_ACT);
						detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
						getCarteraService().registrarDetalleCuota(detallecuota);
						if (totalmora != 0.0) {
							listaCuotasPendientesCondicionCancelar.get(i).setObservacion(Constante.MAP_COM_DESCRIPCION_SGI.get("1")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());

						}
						if (totalpenalizacion != 0.0) {
								listaCuotasPendientesCondicionCancelar.get(i).setObservacionpenalizacion(Constante.MAP_COM_DESCRIPCION_SGI.get("2")+listaCuotasPendientesCondicionCancelar.get(i).getMes().substring(0,3)+"-"+listaCuotasPendientesCondicionCancelar.get(i).getAnio());
							}
						
							
					}
			}
			// RECONOCIMIENTO DE DEUDA DETALLECUOTAS
			if(condicionSeleccionado.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){//
				for (int i = 0; i < listaCuotasPendientesCondicionCancelar.size(); i++) {
					for(int m=0 ;m<listaCuotasPendientesCondicionCancelar.get(i).getListacuota().size();m++){
						Cuota cuota= new Cuota(); 
						cuota=cobranzaRecaudacionService.obtenerCuota(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getIdcontrato(),listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes(),listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getAnio());
						
						listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).setNropagos(cuota!=null? (cuota.getNropagos()!=null? cuota.getNropagos():0 ):0);
						listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).setSiacuenta(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getNropagos()>0? Boolean.TRUE:Boolean.FALSE);
						if (listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getSiacuenta()) {// pendiente
							Cuota cu = new Cuota();
							cu= cobranzaRecaudacionService.obtenerCuota(cuota.getIdcuota());
							//cu.setIdcuota(cuota.getIdcuota());
							//cu.setUsrcre(cuota.getUsrcre());
							//cu.setFeccre(cuota.getFeccre());
							cu.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
							cu.setFecmod(new Date());
							//cu.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));
							//cu.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));						
							cu.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMonto()+cuota.getMontosoles());
							//cu.setMorasoles(cuota.getMorasoles()+listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMora());
							//cu.setMontopenalizacion(cuota.getMontopenalizacion()+FuncionesHelper.redondear(totalpenalizacion, 2));
							//cu.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getAnio());
							//cu.setNromes(Integer.parseInt(Almanaque.mesanumero(cu.getMespagado())));
							cu.setNropagos(cu.getNropagos()+1);
							
							Contrato con= new Contrato();
							con.setIdcontrato(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getIdcontrato());
							cu.setContrato(con);
							cu.setTipocambio(valorTipoCambio);
							
							if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Cancelado")) {
								cu.setCancelado("1");
							} else if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente"))  {
								cu.setCancelado("0");
							}else {
								cu.setCancelado("2");
							}
							
							
							if (listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedetallecondicion()!=null) {
								MaeDetalleCondicion maeDetalleCondicion= new MaeDetalleCondicion();
								maeDetalleCondicion.setIdDetalleCondicion(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedetallecondicion());
								cu.setMaeDetalleCondicion(maeDetalleCondicion);
							}
							
							cobranzaRecaudacionService.actualizarCuota(cu);
//							if(listaCuotasPendientesCondicionCancelar.get(i).getMora()>=0){
//								listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).setObservacion("Cancelación Mora "+cu.getMespagado().substring(0, 3)+" - "+cu.getAniocuotames()+" ");
//							}
//							if(listaCuotasPendientesCondicionCancelar.get(i).getMontopenalizacion()>=0){
//								listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).setObservacionpenalizacion("Cancelación Penalización "+cu.getMespagado().substring(0, 3)+" - "+cu.getAniocuotames()+" ");
//							}
							Detallecuota detallecuo = new Detallecuota();
							detallecuo.setCuota(cu);
							detallecuo.setComprobantepago(comprobantepago);
							detallecuo.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));
							detallecuo.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getAnio());
							detallecuo.setMora(0.0);
							detallecuo.setMontopenalizacion(0.0);
							detallecuo.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cu.getMespagado())));
							detallecuo.setTipocambio(cu.getTipocambio());
							detallecuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
							detallecuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMonto());
							if(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMoneda().equals("D")){
								//detallecuo.setMontodolar(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMontodolar());
								detallecuo.setMontodolar(FuncionesHelper.redondearNum(detallecuo.getMontosoles()/detallecuo.getTipocambio(), 2));

							}
							detallecuo.setFeccre(new Date());
							//estado de detallecuota
							detallecuo.setCancelado(cu.getCancelado());
							if(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedcondiciondetalle()!=null){
								MaeDetCondicionDetalle maed = new MaeDetCondicionDetalle();
								maed.setIdcondiciondetalle(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedcondiciondetalle());
								detallecuo.setMaedcondiciondetalle(maed);	
							}
							detallecuo.setEstado(Constante.ESTADO_ACT);
							detallecuo.setCondicion(Constante.ESTADO_CONDICION_ACT);
							getCarteraService().registrarDetalleCuota(detallecuo);
						}else{
							Cuota cu = new Cuota();
							cu.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
							cu.setFeccre(new Date());
							cu.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));
							cu.setMespagado(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));
							cu.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMonto());
							cu.setMorasoles(0.0);
							cu.setMontopenalizacion(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMontopenalizacion()!=null? listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMontopenalizacion():0);

							cu.setAniocuotames(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getAnio());
							cu.setNromes(Integer.parseInt(Almanaque.mesanumero(cu.getMespagado())));
							cu.setNropagos(1);
							
							
							Contrato con= new Contrato();
							con.setIdcontrato(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getIdcontrato());
							cu.setContrato(con);
							cu.setTipocambio(valorTipoCambio);
							if(sifacturacionelectronica){
								if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Cancelado")) {
									cu.setCancelado("1");
								} else if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente"))  {
									cu.setCancelado("0");
								}else {
									cu.setCancelado("2");
								}
							}
							else{
							 if (listaCuotasPendientesCondicionCancelar.get(i).getCondicion().equals("Pendiente")&& comprobantepago.getFechacancelacion()==null)  {
								cu.setCancelado("0");
							}else {
								cu.setCancelado("1");
							}
							}
							if (listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedetallecondicion()!=null) {
								MaeDetalleCondicion maeDetalleCondicion= new MaeDetalleCondicion();
								maeDetalleCondicion.setIdDetalleCondicion(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedetallecondicion());
								cu.setMaeDetalleCondicion(maeDetalleCondicion);
							}
							// REGISTRA ID DE COMPROBANTEPAGO EN CUOTAS 
							Comprobantepago compr=new Comprobantepago();
							compr.setIdcomprobante(comprobantepago.getIdcomprobante());
							cu.setComprobantepago(compr);
							
							
							getCarteraService().registrarCuota(cu);
							//para descripcion de mora y penalizacion
							// Detalle
							Detallecuota detallecuo = new Detallecuota();
							detallecuo.setCuota(cu);
							
							detallecuo.setComprobantepago(comprobantepago);
							detallecuo.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMes()));
							detallecuo.setAnio(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getAnio());
							detallecuo.setMora(0.0);
							detallecuo.setMontopenalizacion(0.0);
							detallecuo.setNumeromes(Integer.parseInt(Almanaque.mesanumero(cu.getMespagado())));
							detallecuo.setTipocambio(cu.getTipocambio());
							detallecuo.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
							detallecuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMonto());
							if(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getMoneda().equals("D")){
							//detallecuo.setMontodolar(FuncionesHelper.redondear(detallecuo.getMontosoles()/cu.getTipocambio(),2));
								detallecuo.setMontodolar(FuncionesHelper.redondearNum(detallecuo.getMontosoles()/detallecuo.getTipocambio(), 2));
							}
							detallecuo.setFeccre(new Date());
							//estado detallecuota
							detallecuo.setCancelado(cu.getCancelado());
							if(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedcondiciondetalle()!=null){
								MaeDetCondicionDetalle maed = new MaeDetCondicionDetalle();
								maed.setIdcondiciondetalle(listaCuotasPendientesCondicionCancelar.get(i).getListacuota().get(m).getId_maedcondiciondetalle());
								detallecuo.setMaedcondiciondetalle(maed);	
							}
							detallecuo.setEstado(Constante.ESTADO_ACT);
							detallecuo.setCondicion(Constante.ESTADO_CONDICION_ACT);
							getCarteraService().registrarDetalleCuota(detallecuo);
						}
					}
				}
			}//FIN RECONOCIMIENTO DE DEUDA DETALLECUOTAS
			
			addInfoMessage("","Se registro con exito el comprobante de pago");
	        //guardar lista de cuotas 
			guardarlistaCuotasPendientesCondicionCancelar();
			
			if (totalmora != 0.0) {
				
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				contextRequest.execute("dlgConfirmarRegistroNotaDebito.show();");
				nrocomprobantepadrenotadebito = comprobantepago.getNroserie() + "-"+ comprobantepago.getNrocomprobante();
				fechaemisionNotaDebito = comprobantepago.getFechaemision();
				nombrecomprobanteNotadebito = "Nota de Débito";
			
			}else{
			//penalizacion
				if (totalpenalizacion != 0.0) {
				
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				contextRequest.execute("dlgConfirmarRegistroNotaDebitoPenalizacion.show();");
				nrocomprobantepadrenotadebito = comprobantepago.getNroserie() + "-"+ comprobantepago.getNrocomprobante();
				fechaemisionNotaDebito = comprobantepago.getFechaemision();
				nombrecomprobanteNotadebito = "Nota de Débito";
				}
			}
			iddetallecartera=0;
			tipoDescuentoNotaCreditoSeleccionado="";
			serieFacturacionFisica="";
			correlativoFacturacionFisica="";
			idComprobantePagoPadrenotatipo=comprobantepago.getIdcomprobante();
			valorTipoCambio=0.0;
			numerooperacion="";
			fechaoperacion="";
			
			if (condicionSeleccionado.getCondicion().equals("Sin Contrato")) {
				detectarSiCompletoCuotasSinContrato(condicionSeleccionado);
			} else {
				cobranzaRecaudacionService.detectarSiCompletoCuotasContrato(condicionSeleccionado.getIdcontrato());
			}
		
		}catch(Exception e){
				addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
				Logger.error(e.getMessage(), e);
		}
		
	}

	public void detectarSiCompletoCuotasSinContrato(CondicionBean contrato){
		
		if (contrato.getFincobro()!=null) {
			List<CuotaBean> listaCuotas= new ArrayList<CuotaBean>();
			
			CuotaBean cuota;
			Calendar ultimo = Calendar.getInstance();
			ultimo.setTime(contrato.getIniciocobro());
			List<Cuota> listaCuotasPagadas= new ArrayList<Cuota>();
			listaCuotasPagadas=cobranzaRecaudacionService.listarcuotasxcontrato(contrato.getIdcontrato());
			
			Calendar fin = GregorianCalendar.getInstance();
			fin.setTime(contrato.getFincobro());
				
				while (ultimo.compareTo(fin) < 0) {
					
					cuota= new CuotaBean();
					ultimo.add(Calendar.MONTH, 1);
				    
				    cuota.setAnio(ultimo.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(ultimo.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Cancelado");
				    cuota.setMonto(contrato.getCuota1());
				    cuota.setNropagos(1);

				    listaCuotas.add(cuota);
				    
				}
				
				for (int j = 0;j < listaCuotas.size(); j++) {
					
					for (int k = 0; k <listaCuotasPagadas.size() ; k++) {
						if (listaCuotas.get(j).getMes().equals(listaCuotasPagadas.get(k).getMespagado())&& listaCuotas.get(j).getAnio()==listaCuotasPagadas.get(k).getAniocuotames()  ) {
							if (listaCuotasPagadas.get(k).getCancelado().equals("1")) {
								listaCuotas.get(j).setSipagado("1");
							} else {
								listaCuotas.get(j).setMonto(FuncionesHelper.redondear(contrato.getCuota1()-listaCuotasPagadas.get(k).getMontosoles(), 2));
								listaCuotas.get(j).setSiacuenta(true);
								listaCuotas.get(j).setDeudaacuenta(FuncionesHelper.redondear(listaCuotasPagadas.get(k).getMontosoles(),2));
								listaCuotas.get(j).setNropagos(listaCuotasPagadas.get(k).getNropagos());
							}
						}
					}
				}
				
				//quitar
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

	

	
	/**
	 * Recibo de caja
	 * 
	 */
	
	public void inicializarPagoOtroSinCondicion(){
		
		comprobantepago = new Comprobantepago();
		sigarantia=false;
		selectedTipoDocumento="";
		limpiarCampos() ;
	}
	
	public void validacionRegistrarReciboNotaCredito(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		 
		if(idtipodocuseleccionado.equals("04")){
			 contextRequest.execute("dlgConfirmarRegistroReciboCaja.show();");
			 
		}else if(comprobantepagoPadreNotaTipo == null){
			addWarnMessage("", "Seleccione el comprobante de referencia");
		}else if(tipoNotaDebitoyCredito.equals("0")){
			addWarnMessage("", "Seleccione Tipo de Rebaja de la Nota de Crédito");
		}else if(comprobantepago.getFechaemision() == null){
			addWarnMessage("", "Ingrese la fecha de emisión de la nota de debito");
		}else if(comprobantepago.getNombrepagante().equals("")){
			addWarnMessage("", "Ingrese nombre del pagante de la nota de debito");
//		}else if(!validarTipoDocDni_Ruc()){
//			addWarnMessage("", "Ingresar un numero de documento valido");
		}else if(comprobantepago.getIdtipopersona().equals("0")){
			addWarnMessage("", "Seleccione tipo de persona para continuar");
		}else if(comprobantepago.getTipodoc_receptor().equals("0")){
			addWarnMessage("", "Seleccione Tipo de Doc. persona para continuar.");
		}else if(comprobantepago.getDnirucpagante().equals("")|| comprobantepago.getDnirucpagante()==null){
			addWarnMessage("", "Ingrese Numero de Doc de la persona.");
		} else if(!validarNumeroDocPersona()){
			addWarnMessage("", "Ingrese un numero de documento de persona valido.");
		}else if(comprobantepago.getObs_direccion().equals("")){
			addWarnMessage("", "Ingresar la dirección para el documento de nota de debito");
		}else if(selectedCobrador==null){
			addWarnMessage("", "Seleccionar nombre de cobrador de la nota de debito");
		}else if(comprobantepago.getObservacion().equals("")){
			addWarnMessage("", "Agregar una descripción para la nota de debito");
		}else if(totalComprobante.compareTo(new BigDecimal(0))>=0){
			addWarnMessage("", "Debe ingresar un monto valido");
		}else if (sifacturacionelectronica) {
			contextRequest.execute("dlgConfirmarRegistroPagoOtroNotaCredito.show();");
		}else {
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
	}
	public void validacionRegistrarReciboNotaDebito(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		 
		if(idtipodocuseleccionado.equals("04")){
			 contextRequest.execute("dlgConfirmarRegistroReciboCaja.show();");
			 
		}else if(comprobantepagoPadreNotaTipo == null){
			addWarnMessage("", "Seleccione el comprobante de referencia");
//		}else if(tipoConceptoNotaDebito.equals("")){
//			addWarnMessage("", "Seleccione Tipo de Rebaja de la Nota de Crédito");
		}else if(comprobantepago.getFechaemision() == null){
			addWarnMessage("", "Ingrese la fecha de emisión de la nota de debito");
		}else if(comprobantepago.getNombrepagante().equals("")){
			addWarnMessage("", "Ingrese nombre del pagante de la nota de debito");
		}else if(!validarNumeroDocPersona()){
			addWarnMessage("", "Ingresar un numero de documento valido");
		}else if(comprobantepago.getIdtipopersona().equals("0")){
			addWarnMessage("", "Seleccione tipo de persona para continuar");
		}else if(comprobantepago.getObs_direccion().equals("")){
			addWarnMessage("", "Ingresar la dirección para el documento de nota de debito");
		}else if(selectedCobrador==null){
			addWarnMessage("", "Seleccionar nombre de cobrador de la nota de debito");
		}else if(comprobantepago.getObservacion().equals("")){
			addWarnMessage("", "Agregar una descripción para la nota de debito");
		}else if(totalComprobante.compareTo(new BigDecimal(0))<=0){
			addWarnMessage("", "Debe ingresar un monto valido");
		}else if (sifacturacionelectronica) {
			contextRequest.execute("dlgConfirmarRegistroPagoOtroNotaDebito.show();");
		}else {
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
	}
	public Boolean validarTipoDocDni_Ruc(){
		Boolean resp=false;
		if(comprobantepago.getDnirucpagante()!=null){
				if(opciontipodoc.equals(Constante.OPCION_TIPO_DOC_DNI)){
					if(comprobantepago.getDnirucpagante().length()==8){
						resp=true;
					}else{
						resp=false;
					}
				}else if(opciontipodoc.equals(Constante.OPCION_TIPO_DOC_RUC)){
					if(comprobantepago.getDnirucpagante().length()==11){
						resp=true;
					}else{
						resp=false;
					} 
				}
		}
		return resp;
	}
	public Boolean validarNumeroDocPersona(){
		Boolean resp=false;
		if(comprobantepago.getDnirucpagante()!=null){
			      
			if(comprobantepago.getTipodoc_receptor().equalsIgnoreCase("1")){
				if(comprobantepago.getDnirucpagante().length()==8){
					resp=true;
				}
			}else if (comprobantepago.getTipodoc_receptor().equalsIgnoreCase("6")){
				if(comprobantepago.getDnirucpagante().length()==11){
					resp=true;
				}
			}else if (comprobantepago.getTipodoc_receptor().equalsIgnoreCase("4")){
				if(comprobantepago.getDnirucpagante().length()<=12){
					resp=true;
				}
			}
		}
		return resp;
	}
	public void validacionRegistrarReciboCaja(){
		
		if (idtipodocuseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de documento");
		}else if (idtipodocuseleccionado.equals("01")&& idtipotransaccionseleccionado.equals("0")) {
			//if (idtipotransaccionseleccionado.equals("0")) {
				addWarnMessage("", "Seleccione Tipo de forma de pago");
			//}
		}else if (idtipoconceptoseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de concepto");
		} else if (idtipopagoseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de pago ");
//		}else if (idtipodocuseleccionado.equals("04") && serieFacturacionFisica.equals("")) {
//			addWarnMessage("", "Ingrese Nro Serie");
//		} else if (idtipodocuseleccionado.equals("04") && correlativoFacturacionFisica.equals("")) {
//			addWarnMessage("", "Ingrese Nro Comprobante");
		} else if (idtipodocuseleccionado.equals("04") && idtipomodalidadpago.equals("0")) {
			addWarnMessage("", "Ingrese Forma de pago");
//		} else if(idtipodocuseleccionado.equals("99") && comprobantepago.getComp_concepto().equals("0")){
//			addWarnMessage("", "Ingrese el concepto de comprobante de pago ");
		} else if (comprobantepago.getFechaemision() == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		} else if(comprobantepago.getNombrepagante().equals("")||comprobantepago.getNombrepagante()==null){
			addWarnMessage("", "Ingrese Nombre de Pagante");
		} else if(comprobantepago.getDnirucpagante().equals("")||comprobantepago.getDnirucpagante()==null){
			addWarnMessage("", "Ingrese Nro de documento de la persona");
		} else if(idtipodocuseleccionado.equals("04") && comprobantepago.getIdtipopersona().equals("0")) {
			addWarnMessage("", "Ingrese Tipo de Persona");
		} else if(!validarNumeroDocPersona()){
			addWarnMessage("", "Ingrese un numero de documento de persona valido.");
		} else if((idtipodocuseleccionado.equals("04") && comprobantepago.getObs_direccion().equals(""))||(idtipodocuseleccionado.equals("04") && comprobantepago.getObs_direccion()==null)){
			addWarnMessage("", "Ingrese Dirección");
		} else if (selectedCobrador==null) {
			addWarnMessage("", "Ingrese nombre cobrador");
		} 
		
			
//		else if (cobranzaRecaudacionService.siregistradocomprobantepago(comprobantepago.getNroserie()+"-"+comprobantepago.getNrocomprobante())) {
//				
//			addErrorMessage("", "Ya existe un recibo de caja con el N° de comprobante registrado en el sistema");
//		}
		else if (totalComprobante.doubleValue()==0) {
			addErrorMessage("","Usted debe ingresar el monto para el comprobante de pago .");
		}else {
			System.out.println("idtipodocuseleccionado2="+idtipodocuseleccionado);
			
			if(!idtipodocuseleccionado.equals("04") ){
				System.out.println("comprobantepago.getComp_conceptos()="+comprobantepago.getComp_concepto());
				if ( Integer.valueOf( comprobantepago.getComp_concepto())==0) {
					addWarnMessage("", "Ingrese el Concepto del Comprobante de pago");
				}else if (comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
					addWarnMessage("", "Ingrese el tipo de persona");
				}else if (comprobantepago.getObs_direccion().equals("")||comprobantepago.getObs_direccion()==null){
					addWarnMessage("", "Ingrese la dirección");
				}else if (comprobantepago.getObservacion().equals("")||comprobantepago.getObservacion()==null){
					addWarnMessage("", "Ingrese la descripción");
				}else if (comprobantepago.getObservacion().equals("")||comprobantepago.getObservacion()==null){
						addWarnMessage("", "Ingrese la observacion");
				}
			}
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			 
			if(idtipodocuseleccionado.equals("04")){
				 contextRequest.execute("dlgConfirmarRegistroReciboCaja.show();");
				 
			}else if (sifacturacionelectronica) {
				contextRequest.execute("dlgConfirmarRegistroPagoOtro.show();");
			}else {
				 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
			}
		}
		
	}
	public void calcularIgvSubtotalPagoOtroNotaCredito() {
		Double total =FuncionesHelper.redondearNum(Double.parseDouble(totalComprobante.toString()),2);
		if(total>0){
			totalComprobante=totalComprobante.multiply(new BigDecimal("-1"));
			calcularIgvSubtotal();
		}else{
			calcularIgvSubtotal();
		}
	}
	public void calcularIgvSubtotalPagoOtroNotaDebito() {
		Double total =FuncionesHelper.redondearNum(Double.parseDouble(totalComprobante.toString()),2);
		if(total<0){
			totalComprobante=totalComprobante.multiply(new BigDecimal("-1"));
			totalSubTotalComprobante =  totalComprobante;
			igvComprobante=new BigDecimal("0.0");
		}else{
			totalSubTotalComprobante =  totalComprobante;
			igvComprobante=new BigDecimal("0.0");
		}
	}
	public void calcularIgvSubtotal() {
		
		totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
		totalComprobante.setScale(2,RoundingMode.HALF_UP);
		igvComprobante.setScale(2,RoundingMode.HALF_UP);
		montoDetraccion.setScale(2,RoundingMode.HALF_UP);
		
		if (idtipodocuseleccionado.equals("04") || idtipodocuseleccionado.equals("08")) {
			totalSubTotalComprobante =  totalComprobante;
			igvComprobante =   new BigDecimal("0.0");
		} else {
			//totalSubTotalComprobante =  totalComprobante/1.18 , 2);
			//---totalSubTotalComprobante =  totalComprobante.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
			//igvComprobante =  FuncionesHelper.redondear((totalComprobante/1.18)*0.18 , 2);
			//--igvComprobante =  totalComprobante.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),4,RoundingMode.HALF_UP);
			//
			totalSubTotalComprobante =  totalComprobante.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
			double numN=FuncionesHelper.redondearNum(Double.parseDouble(totalSubTotalComprobanteN.toString()),2);
			totalSubTotalComprobanteN = new BigDecimal(Double.toString(numN))  ;			
			igvComprobante = totalComprobante.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
			double igv=FuncionesHelper.redondearNum(Double.parseDouble(igvComprobante.toString()),2);
			igvComprobante = new BigDecimal(Double.toString(igv));
			
		}
	}
	
	

	public Boolean validarPorUsoReciboCaja(){
		
		/** Registro de referencia a recibo de caja para los comprobante por uso de carta fianza o por uso de garantia **/
		if (idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA) || idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOGARANTIA) ) {	
		
			if (reciboCajaReferencia==null) {
				
				addWarnMessage("", "Seleccione recibo de caja referencia");
			
				return Boolean.TRUE;
			}else if (totalComprobante.doubleValue()>reciboCajaReferencia.getTotalsoles()){
	
				addWarnMessage("", "El total de comprobante de pago no puedo ser mayor al total de recibo de caja.");
				
				return Boolean.TRUE;
			}
		
		}
		
		return Boolean.FALSE;
	}
	
	public void grabarReciboNotaCredito(){
		try{
			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
			tipoDescuentoNotaCreditoSeleccionado="Renta";
			idtipodocuseleccionado="09";
			//idtipodocu=comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu();//tipo de documento padre 
			Tipodocu 		tipodocu 		= new Tipodocu("09");					//tipo de documento 
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");					//tipo de movimiento
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")?"08":"13");
			Tipopago        tipopago        = new Tipopago("01");					//tipo de pago 
			System.out.println("tipodocu="+comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
//			if (sigarantia && condicionSeleccionado!=null) {
//				iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
//				
//				Detallecartera dc = new Detallecartera();
//				dc.setIddetallecartera(iddetallecartera);
//				comprobantepago.setDetallecartera(dc);
//				
//			}else {
//				comprobantepago.setDetallecartera(null);
//			}
			comprobantepago.setDetallecartera(null);
			
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());

			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setMoradetectada(0.0);
			
//			comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
//			comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			
			comprobantepago.setIgvsoles(igvComprobante.doubleValue());
			comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
			comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			comprobantepago.setSianulado(Boolean.FALSE);
			comprobantepago.setSirecibocajausado(Boolean.FALSE);
			comprobantepago.setSigeneronotadebito(Boolean.FALSE);
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setComp_opc("D");//Devolución
			comprobantepago.setUsuariomodificador(null);
			comprobantepago.setFechamodificacion(null);
			comprobantepago.setNotacredito(null);
			comprobantepago.setNotadebito(null);
			comprobantepago.setMontopenalizacion(0.0);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			//Referencia Comprobante padre
			Comprobantepago cp = new Comprobantepago(comprobantepagoPadreNotaTipo.getIdcomprobante());
			comprobantepago.setComprobanteref(cp);
			comprobantepago.setNombreusr_cre(userMB.getNombreusr().toUpperCase());
			
			//comprobantepago.setTipcambio(0.0);
			if(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")){
				switch(tipoNotaDebitoyCredito){
					case "Total":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"01");break;
					case "Parcial":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"05");break;
					default:comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);
				}
			}else{
				comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);//tipo de descuento notadecredito  : Renta01 , Renta05 , Mora
			}
//			if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03")) {
//				if (totalComprobante.doubleValue() >= 700 && idtipodocuseleccionado.equals("01")) {
//					comprobantepago.setSidetraccion(true);
//					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(totalComprobante.doubleValue() * 0.12, 2));
//					
//				} else {
//					comprobantepago.setSidetraccion(false);
//					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
//				}
//				comprobantepago.setSigeneronotacredito(false);
//				comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
//				comprobantepago.setIgvsoles(igvComprobante.doubleValue());
//			} else {
//				comprobantepago.setSidetraccion(false);
//				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
//				comprobantepago.setIgvsoles(0.0);
//				comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
//				comprobantepago.setSigeneronotacredito(true);
//			}
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			//comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
		
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
				if(comprobantepago.getComp_concepto()!=null){
					comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get(comprobantepago.getComp_concepto()));
				}
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				
				System.out.println(idtipoconceptoseleccionado);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);

			}else {
				//comprobante concepto
				comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get("1"));
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				//cobranzaRecaudacionService.grabarBoletaFacturaFE(comprobantepago,idtipodocuseleccionado,"CARTERA102");
				System.out.println("Tipodescuentonotacredito"+comprobantepago.getTipodescuentonotacredito());
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, "CARTERA102", comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
				System.out.println("idcomprobantepagopadre"+comprobantepagoPadreNotaTipo.getIdcomprobante());
				cobranzaRecaudacionService.apuntarComprobanteAnotacredito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());

			}
			
			addInfoMessage("","Se registro con exito la nota de Crédito");
			
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void grabarReciboNotaDebito(){
		try{
			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
			tipoDescuentoNotaCreditoSeleccionado="Renta";
			idtipodocuseleccionado="08";
			//idtipodocu=comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu();//tipo de documento padre 
			Tipodocu 		tipodocu 		= new Tipodocu("08");					//tipo de documento 
			Tipomovimiento  tipomovimiento  = new Tipomovimiento("01");					//tipo de movimiento
			Tipoconcepto 	tipoconcepto	= new Tipoconcepto(tipoConceptoNotaDebito);
			Tipopago        tipopago        = new Tipopago("01");					//tipo de pago 

			
			System.out.println("tipodocu="+comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
//			if (sigarantia && condicionSeleccionado!=null) {
//				iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
//				
//				Detallecartera dc = new Detallecartera();
//				dc.setIddetallecartera(iddetallecartera);
//				comprobantepago.setDetallecartera(dc);
//				
//			}else {
//				comprobantepago.setDetallecartera(null);
//			}
			comprobantepago.setDetallecartera(null);
		
			comprobantepago.setTipodocu(tipodocu);
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setFechacancelacion(comprobantepago.getFechacancelacion());
			comprobantepago.setFechaemision(comprobantepago.getFechaemision());
			comprobantepago.setFechavencimiento(null);
			comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
			comprobantepago.setIgvsoles(FuncionesHelper.redondear(0.0, 2));	
			comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
			comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			comprobantepago.setMoradetectada(0.0);
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSigeneronotacredito2(false);
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(0.0);
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(0.0);
			comprobantepago.setSianulado(false);
			comprobantepago.setSigeneronotadebito(true);
			comprobantepago.setSigeneropenalizacion(false);
			comprobantepago.setUsuariomodificador(null);
			comprobantepago.setFechamodificacion(null);
			comprobantepago.setNotadebito(null);
			comprobantepago.setNotacredito(null);
			comprobantepago.setMontopenalizacion(0.0);
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			//comprobantepago.setTipcambio(0.0);
						
			comprobantepago.setNrocomprobantepadre(comprobantepagoPadreNotaTipo.getNroseriecomprobante());
			comprobantepago.setSirecibocajausado(Boolean.FALSE);
			// Referencia  a Comprobantepadre
			Comprobantepago cp = new Comprobantepago(comprobantepagoPadreNotaTipo.getIdcomprobante());
			comprobantepago.setComprobanteref(cp);
			comprobantepago.setNombreusr_cre(userMB.getNombreusr().toUpperCase());
			
			//comprobantepago.setComp_opc("D");//Devolución
//			if(tipoDescuentoNotaCreditoSeleccionado.equals("Renta")){
//				switch(tipoNotaDebitoyCredito){
//					case "Total":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"01");break;
//					case "Parcial":comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado+"05");break;
//					default:comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);
//				}
//			}else{  
//				comprobantepago.setTipodescuentonotacredito(tipoDescuentoNotaCreditoSeleccionado);//tipo de descuento notadecredito  : Renta01 , Renta05 , Mora
//			}
//			if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03")) {
//				if (totalComprobante.doubleValue() >= 700 && idtipodocuseleccionado.equals("01")) {
//					comprobantepago.setSidetraccion(true);
//					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(totalComprobante.doubleValue() * 0.12, 2));
//					
//				} else {
//					comprobantepago.setSidetraccion(false);
//					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
//				}
//				comprobantepago.setSigeneronotacredito(false);
//				comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
//				comprobantepago.setIgvsoles(igvComprobante.doubleValue());
//			} else {
//				comprobantepago.setSidetraccion(false);
//				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
//				comprobantepago.setIgvsoles(0.0);
//				comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
//				comprobantepago.setSigeneronotacredito(true);
//			}
			comprobantepago.setMoradetectada(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			//comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
		
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
				if(comprobantepago.getComp_concepto()!=null){
					//comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get(comprobantepago.getComp_concepto()));
					comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get("1"));
				}
				comprobantepago.setNroserie(serieFacturacionFisica);
				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				
				System.out.println(idtipoconceptoseleccionado);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
				cobranzaRecaudacionService.apuntarComprobanteAnotadebito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());


			}else {
				//comprobante concepto
				comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get("1"));
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				//cobranzaRecaudacionService.grabarBoletaFacturaFE(comprobantepago,idtipodocuseleccionado,"CARTERA102");
				System.out.println("Tipodescuentonotacredito"+comprobantepago.getTipodescuentonotacredito());
				cobranzaRecaudacionService.grabarNotaDebitoCreditoFE(comprobantepago, idtipodocuseleccionado, "CARTERA102", comprobantepagoPadreNotaTipo.getTipodocu().getIdtipodocu());
				System.out.println("idcomprobantepagopadre"+comprobantepagoPadreNotaTipo.getIdcomprobante());
				cobranzaRecaudacionService.apuntarComprobanteAnotadebito(comprobantepagoPadreNotaTipo.getIdcomprobante(),comprobantepago.getIdcomprobante());

			}
			
			addInfoMessage("","Se registro con exito la nota de Débito");
			
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void grabarReciboCaja(){
		
		try{
			totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
			totalComprobante.setScale(2,RoundingMode.HALF_UP);
			igvComprobante.setScale(2,RoundingMode.HALF_UP);
			montoDetraccion.setScale(2,RoundingMode.HALF_UP);
			
			Tipodocu tipodocu = new Tipodocu(idtipodocuseleccionado);
			Tipomovimiento tipomovimiento = new Tipomovimiento("04");
			Tipoconcepto tipoconcepto= new Tipoconcepto(idtipoconceptoseleccionado);
			Tipopago tipopago = new Tipopago(idtipopagoseleccionado);
			
			
			
			Usuario usuario= new Usuario();
			usuario.setIdusuario(selectedCobrador.getId());
			comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
			comprobantepago.setUsuario(usuario);
			if (sigarantia && condicionSeleccionado!=null) {
				iddetallecartera = getCarteraService().obtenerIddetalleCartera(condicionSeleccionado.getIdcontrato());
				
				Detallecartera dc = new Detallecartera();
				dc.setIddetallecartera(iddetallecartera);
				comprobantepago.setDetallecartera(dc);
				
			}else {
				comprobantepago.setDetallecartera(null);
			}
			
			comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			comprobantepago.setFechacreacion(new Date());
			comprobantepago.setNombreusr_cre(userMB.getNombreusr().toUpperCase());
			//comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
			
			comprobantepago.setTipopago(tipopago);
			comprobantepago.setTipomovimiento(tipomovimiento);
			comprobantepago.setTipoconcepto(tipoconcepto);
			comprobantepago.setTipodocu(tipodocu);
           if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
        	   Tipomodalidadpago tipomodalidadpago= new Tipomodalidadpago(idtipomodalidadpago);
        	   comprobantepago.setTipomodalidadpago(tipomodalidadpago);
			}
			
			comprobantepago.setMoradetectada(0.0);
			
			comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
			comprobantepago.setTotalsoles(totalComprobante.doubleValue());
			comprobantepago.setNrocomprobantepadre("");
			comprobantepago.setSianulado(Boolean.FALSE);
			comprobantepago.setSirecibocajausado(Boolean.FALSE);
			comprobantepago.setSigeneronotadebito(Boolean.FALSE);//NO GENERO NOTA DE DEBITO
			comprobantepago.setSiautodetraccion(false);
			comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setEstado(Constante.ESTADO_ACT);
			comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
			
			if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03")) {
				if(idtipodocuseleccionado.equals("01")&& sifacturacionelectronica){
					Tipotransaccion tipotransaccion= new Tipotransaccion(idtipotransaccionseleccionado);
					comprobantepago.setTipotransaccion(tipotransaccion);
				}
				if (totalComprobante.doubleValue() >= 700 && idtipodocuseleccionado.equals("01")) {
					comprobantepago.setSidetraccion(true);
					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(totalComprobante.doubleValue() * 0.12, 2));
					
				} else {
					comprobantepago.setSidetraccion(false);
					comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
				}
				comprobantepago.setSigeneronotacredito(false);
				comprobantepago.setSigeneronotacredito2(false);
				comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
				comprobantepago.setIgvsoles(igvComprobante.doubleValue());
			} else {
				comprobantepago.setSidetraccion(false);
				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
				comprobantepago.setIgvsoles(0.0);
				comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
				comprobantepago.setSigeneronotacredito(true);
			}
			
			comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
		
			/**
			 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
			 *  **/
			if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
				if(comprobantepago.getComp_concepto()!=null){
					comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get(comprobantepago.getComp_concepto()));
				}

//				comprobantepago.setNroserie(serieFacturacionFisica);
//				comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
//				comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
				comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
				comprobantepago.setComp_descripcionitem(comprobantepago.getObservacion());
				comprobantepago.setTextoimportetotal(FuncionesHelper.numeroaLetra(comprobantepago.getTotalsoles()));
				
				System.out.println(idtipoconceptoseleccionado);
				cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
				//grabarDocumentoPDF(comprobantepago);

			}else {
				//comprobante concepto
				comprobantepago.setComp_concepto(Constante.MAP_COMP_CONCEPTO_SGI.get(comprobantepago.getComp_concepto()));
				comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
				cobranzaRecaudacionService.grabarBoletaFacturaFE(comprobantepago,idtipodocuseleccionado,"CARTERA102");	
			}
			if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
				grabarDocumentoPDF(comprobantepago);
			}
			addInfoMessage("","Se registro con exito el comprobante de pago");
			
			limpiarCampos();
		
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void grabarDocumentoPDF(Comprobantepago cp){
		StreamedContent report;
		List<ComprobanteBean> comprobantebean= new ArrayList<ComprobanteBean>();
		comprobantebean=cobranzaRecaudacionService.buscarComprobante(cp);
		try {
			   Map<String, Object> parameters = new HashMap<String, Object>();
			   String  reportPath;
			   String nombrearchivo;
			   reportPath=  ConstantesReporteSGI.REP_RECIBO_CAJA_SGI;
			   nombrearchivo="RC_"+comprobantebean.get(0).getNroseriecomprobante()+"_"+FechaUtil.fechaToString(new Date())+Constantes.EXTENSION_FORMATO_PDF;
			   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
			   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
			   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
			   parameters.put("REPORT_CONNECTION",conn);

			   //report = reporteGeneradorService.generarPDF(reportPath, parameters, comprobantebean,nombrearchivo+FuncionesHelper.fechaToString(new Date()));
			   ArchivoAdjuntoBean archivoAdjuntoBean= new ArchivoAdjuntoBean();
	    		archivoAdjuntoBean.setTitulo("Recibo de caja "+comprobantebean.get(0).getNroseriecomprobante()+" "+FechaUtil.fechaToString(new Date()));
	    		archivoAdjuntoBean.setNombre(nombrearchivo);
	    		archivoAdjuntoBean.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());    		
	    		archivoAdjuntoBean.setTipo(Constantes.PDF);
	    		archivoAdjuntoBean.setStream(reporteGeneradorService.generarPDF(reportPath, parameters, comprobantebean));
	    		archivoAdjuntoBean.setRuta(Constantes.DIR_SGI_RECIBO_CAJA);
	    		archivoAdjuntoBean.setFeccre(new Date());
	    		administrarArchivoService.grabarArchivo(archivoAdjuntoBean, Constantes.COMPROBANTEPAGO, cp.getIdcomprobante());
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

	}
	public void cargarReciboCajaReferencia(){
		if (idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOCARTAFIANZA)) {
			listaReciboCajaReferencia=cobranzaRecaudacionService.listarReciboCajaReferencia(condicionSeleccionado.getIdcontrato(),"12");
			sivisibleReciboCajaRef=Boolean.TRUE;
		} else  if(idtipopagoseleccionado.equals(Constantes.TIPOPAGO_ID_USOGARANTIA) ){
			listaReciboCajaReferencia=cobranzaRecaudacionService.listarReciboCajaReferencia(condicionSeleccionado.getIdcontrato(),"03");
			sivisibleReciboCajaRef=Boolean.TRUE;
		}
		  else{
			sivisibleReciboCajaRef=Boolean.FALSE;
		}
	}

	/********************************* CARTERA ******************************************/

	public void inicializarContratosCartera() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();

		if (cartera.getIdcartera() != 0) {
			listaContratosAgregarCartera.clear();
			contratoCartera = new Contrato();
			listaIdContratosAgregarCartera.clear();
			listaContratosVigentes = cobranzaRecaudacionService.listarContratosVigentes();

			contextRequest.execute("dlgAgregarContratos.show();");
		} else {
			addErrorMessage("No ha seleccionado cartera",
					"Seleccione una upa en la pestaña Consulta de Cartera");
		}

	}

	public void agregarContratoBuscadoCartera() {
		listaContratosAgregarCartera.clear();

		if (listaIdContratosAgregarCartera.size() != 0) {

			for (String id: listaIdContratosAgregarCartera) {

				listaContratosAgregarCartera.add(cobranzaRecaudacionService.obtenerContratoxId(Integer.parseInt(id)));

			}
		}

	}

	// Detalle cartera
	public void validarRegistrarDetalleCartera() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		if (listaContratosAgregarCartera.size() == 0) {
			addWarnMessage("Lista Vacia","No ha agregado ninguna upa, agrege al menos 1 upa para continuar");

		} else {
			contextRequest.execute("dlgRegistrarCarteraDetalle.show();");
		}

	}

	public void validarCambiarCartera() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		
		if (detallecarteraBean == null) {
			addWarnMessage("","Ingrese una upa y seleccione una condición para continuar.");

		} else {
			idcarteraEditar=0;
			contextRequest.execute("dlgCambiardeCartera.show();");
		}

	}

	public void validarGuardarCambiarCartera() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();

		if (idcarteraEditar != 0) {
			contextRequest.execute("dlgActualizarNumeroCarteraDetalle.show();");

		} else {
			addWarnMessage("No ha seleccionado cartera","Seleccionar cartera para continuar");
		}

	}

	public void cambiarCartera() {

		Cartera car = new Cartera();
		car.setIdcartera(idcarteraEditar);
		Contrato contrato = new Contrato();
		contrato.setIdcontrato(detallecarteraBean.getIdcontrato());

		
		Detallecartera detallecartera= new Detallecartera();
		detallecartera.setIddetallecartera(detallecarteraBean.getIddetallecartera());
		detallecartera.setCartera(car);
		detallecartera.setContrato(contrato);
		detallecartera.setUsrcre(detallecarteraBean.getUsrcre());
		detallecartera.setFeccre(detallecarteraBean.getFeccre());
		detallecartera.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		detallecartera.setFecmod(new Date());
		carteraService.registrarDetalleCartera(detallecartera);

		listadetalleCarteraBean = getCarteraService().listarDetalleCarterasPorIdCartera(cartera.getIdcartera());
		detallecartera = null;
		idcarteraEditar = 0;
	}

	public void registrarDetalleCartera() {

		for (Contrato c : listaContratosAgregarCartera) {
			Detallecartera dc = new Detallecartera();
			dc.setContrato(c);
			dc.setCartera(cartera);
			dc.setFeccre(new Date());
			carteraService.registrarDetalleCartera(dc);
		}

		listadetalleCarteraBean = getCarteraService().listarDetalleCarterasPorIdCartera(cartera.getIdcartera());

	}

	private String tmpnrocartera;

	public void validarCreaciondeCartera() {
        boolean permiso=false;
		RequestContext contextRequest = RequestContext.getCurrentInstance();
        if(permiso){
		if (cartera.getIdcartera() != 0) {// actualizar
			if (tmpnrocartera.equals(cartera.getNumero())) {
				contextRequest.execute("dlgRegistrarCarteraCabecera.show();");
			} else {
				if (!carteraService.siExisteCarteraConNombre(cartera.getNumero())) {
					if (idcobradorseleccionado != 0 && !cartera.getTipolista().equals("0")) {
						contextRequest.execute("dlgRegistrarCarteraCabecera.show();");
					}
				} else {
					addErrorMessage("Número de cartera ya existe","El número de cartera ya se encuentra registrado, ingresar un número diferente al ingresado");
				}
			}
		} else {
			if (!carteraService.siExisteCarteraConNombre(cartera.getNumero())) {
				if (idcobradorseleccionado != 0 && !cartera.getTipolista().equals("0")) {
					contextRequest.execute("dlgRegistrarCarteraCabecera.show();");
				}

			} else {
				addErrorMessage("Número de cartera ya existe", "El número de cartera ya se encuentra registrado, ingresar un número diferente al ingresado");
			}

		}
		}else{
			addInfoMessage("El registro y modificacion se encuentran restrigidos","No tiene permido realizar la operación de registro");
		}
        
	}

	public void registrarCartera() {

		Date ahora = new Date();
		if (cartera.getIdcartera() != 0) {
			cartera.setFecmod(ahora);
			cartera.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			getCarteraService().registrarCartera(cartera);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito", "Se Actualizo correctamente el cartera.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			cartera.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			cartera.setFeccre(ahora);
			Usuario usuario = new Usuario();
			usuario.setIdusuario(idcobradorseleccionado);
			cartera.setUsuario(usuario);

			getCarteraService().registrarCartera(cartera);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito", "Se Registro correctamente el cartera.");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
		cartera = getCarteraService().obtenerUltimoCartera();

	}

	public void eliminarCartera() {
		getCarteraService().eliminarCartera(cartera);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito", "Se eliminó la cartera correctamente.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}



	public void editarCartera() {

		
		try {
			idcobradorseleccionado = cartera.getUsuario().getIdusuario();
			tmpnrocartera = cartera.getNumero();
			listadetalleCarteraBean = getCarteraService().listarDetalleCarterasPorIdCartera(cartera.getIdcartera());
			
			addInfoMessage("Se ha seleccionado Cartera","Se ha seleccionado  exitosamente cartera, ahora puede Ud. agregar upas correspondientes");
			
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);;
		}
		
	}


	public void setValoresNotaDebito() {
			totalmora = comprobantepagoPadreNotaTipo.getMoradetectada();
			sifacturacionelectronica=comprobantepagoPadreNotaTipo.getSifacturacionelectronica();
	}
	
	public void validarEditarPago(){
		if (condicionSeleccionado!=null) {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("widgetDlgListaPagosEdicion.show();");

		} else {
			addInfoMessage("No ha seleccionado upa", "Seleccione o busque la upa para poder continuar");
			
		}

	}
	
	public void editarEstadoPago(RowEditEvent event){
		try {
			if (((CuotaBean) event.getObject()).getCondicion().equals("Pendiente")) {
				cobranzaRecaudacionService.editarEstadoPago(((CuotaBean) event.getObject()).getIdcuota(),"0");
			}
			else if (((CuotaBean) event.getObject()).getCondicion().equals("Cancelado")){
				cobranzaRecaudacionService.editarEstadoPago(((CuotaBean) event.getObject()).getIdcuota(),"1");
			}	
			else {
				cobranzaRecaudacionService.editarEstadoPago(((CuotaBean) event.getObject()).getIdcuota(),"2");   //Documentos Generados
			}	
		
		addInfoMessage("Actualización completada", "Se realizó los cambios exitosamente");
		
	}catch(Exception e){
		e.printStackTrace();
		}	
	}

	public void validarRegistroAnularComprobante(){
		
		if (idtipodocuseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de documento");
		} else if (comprobantepago.getNroserie().equals("")) {
			addWarnMessage("", "Ingrese Nro Serie");
		} else if (comprobantepago.getNrocomprobante().equals("")) {
			addWarnMessage("", "Ingrese Nro Comprobante");
		} else if (comprobantepago.getFechaemision() == null) {
			addWarnMessage("", "Ingrese Fecha Emisión");
		} else if (selectedCobrador==null) {
			addWarnMessage("", "Ingrese nombre cobrador");
		}else if (cobranzaRecaudacionService.siregistradocomprobantepago(comprobantepago.getNroserie()+"-"+comprobantepago.getNrocomprobante())) {
				
		addErrorMessage("", "Ya existe un recibo de caja con el N° de comprobante registrado en el sistema");
		}else {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarRegistroComprobanteAnulado.show();");
		}
		
	}
	
	public void anularComprobante(){
		

		Tipodocu tipodocu = new Tipodocu(idtipodocuseleccionado);
		Tipomovimiento tipomovimiento = new Tipomovimiento("04");
		Tipoconcepto tipoconcepto= new Tipoconcepto("14");
		Tipopago tipopago = new Tipopago("04");
		
		Usuario usuario= new Usuario();
		
		usuario.setIdusuario(selectedCobrador.getId());

		comprobantepago.setUsuario(usuario);

		comprobantepago.setDetallecartera(null);
		comprobantepago.setTipodocu(tipodocu);
		comprobantepago.setTipomovimiento(tipomovimiento);
		comprobantepago.setTipoconcepto(tipoconcepto);
		comprobantepago.setTipopago(tipopago);
		comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
		comprobantepago.setIgvsoles(0.0);
		comprobantepago.setSubtotalsoles(0.0);
		comprobantepago.setTotalsoles(0.0);
		comprobantepago.setFechacreacion(new Date());
		comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		comprobantepago.setSigeneronotacredito(true);
		comprobantepago.setSigeneronotadebito(true);
		comprobantepago.setNrocomprobantepadre("");
		comprobantepago.setSidetraccion(false);
		comprobantepago.setMoradetectada(0.0);
		comprobantepago.setMontodetraccion(0.0);
		comprobantepago.setSiautodetraccion(false);
		comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
		comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
		comprobantepago.setSianulado(true);
		
		comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
		cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
		
		addInfoMessage("Nuevo Registro","Se registro con éxito el comprobante de pago anulado");
	}
	
	public void validardocumentoseleccionado(){
		 try{
			   if(idtipodocuseleccionado!=null){
				   System.out.println("idtipodcuseleeccionado="+idtipodocuseleccionado);
			   }else{
				   addInfoMessage("Seleccione","Seleccione documento valido para continuar");
			   }
				   
		 
		 }catch(Exception e ){
			 System.out.println(e.getMessage());
			 e.printStackTrace();
		 }
	}
	public void cargarTipoDocumentoValido(){
		if(!comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
			comprobantepago.setDnirucpagante("");
			comprobantepago.setTipodoc_receptor("0");
		if(comprobantepago.getIdtipopersona().equalsIgnoreCase(Constantes.TIPO_PERSONA_NATURAL.toString())){
			habilitarDocDNI=false;habilitarDocRUC=false;
		}else{
			habilitarDocRUC=false;habilitarDocDNI=true;
		}
		}else{			
			comprobantepago.setTipodoc_receptor("0");
			comprobantepago.setDnirucpagante("");
			habilitarDocDNI=true;habilitarDocRUC=true;
			addWarnMessage("","Debe seleccionar un tipo de persona valido	");
		}
	}
	public void cargarNumeroDeDocumento(){
		//traer datos del inquilino.
		
		if(comprobantepago.getTipodoc_receptor().equalsIgnoreCase("6")){
			if(condicionSeleccionado.getRuc()==null || condicionSeleccionado.getRuc().trim().equalsIgnoreCase("")){
				comprobantepago.setDnirucpagante("");
				comprobantepago.setIdtipopersona("");
				addWarnMessage("","No existe valor para esta opcion	");
			}else{
				comprobantepago.setDnirucpagante(condicionSeleccionado.getRuc().trim());
				comprobantepago.setIdtipopersona(condicionSeleccionado.getIdtipopersona().toString());
                
			}
			
		}else if(comprobantepago.getTipodoc_receptor().equalsIgnoreCase("1")) {
			if(condicionSeleccionado.getDni()==null || condicionSeleccionado.getDni().trim().equalsIgnoreCase("")){
				comprobantepago.setDnirucpagante("");
				comprobantepago.setIdtipopersona("");
				addWarnMessage("","No existe valor para esta opcion	");
			}else{
				comprobantepago.setDnirucpagante(condicionSeleccionado.getDni().trim());
				comprobantepago.setIdtipopersona(condicionSeleccionado.getIdtipopersona().toString());
			}

		}else{
			comprobantepago.setDnirucpagante("");
			addInfoMessage("Seleccione","Seleccione un tipo de persona valido	");
		}
		
	}
	public void cargarLongitudTipoDocumento(){
		if(!comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
			comprobantepago.setDnirucpagante("");
			longDocuPersona= carteraService.longitudTipoDocumentoPersona (comprobantepago.getTipodoc_receptor());
		}

	}
	
	/**
	 * Completa el usuario  cobrador.
	 * @param texto - Texto a completar, tipo String.
	 * @return Lista de usuarios cobradores cuya descripción contiene el texto ingresado, tipo List.
	 */
	public List<ItemBean> completarUsuarioCobrador(String texto){
		List<ItemBean> listaUsuariosCobradores= new ArrayList<ItemBean>();
			
		listaUsuariosCobradores = FHvariadoService.obtenerCobradores(texto);
		return listaUsuariosCobradores;
	}
	
	
	/**
	 * Busca un cobrador por nombre .
	 * @param nombre - Nombre del cobrador a buscar, tipo String.
	 * @return Datos del cobrador, tipo ItemBean.
	 */
	public ItemBean buscarUsuarioCobrador(String nombre){
		List<ItemBean> lista = FHvariadoService.
				obtenerCobradoresPorNombreExacto(nombre);
		if(lista != null && lista.size() > 0)
			return lista.get(0);
		else
			return null;
	}

	public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
		return cobranzaRecaudacionService;
	}


	public void setCobranzaRecaudacionService(
			IRecaudacionCobranzaService cobranzaRecaudacionService) {
		this.cobranzaRecaudacionService = cobranzaRecaudacionService;
	}


	public Cartera getCartera() {
		if (cartera == null) {
			cartera = new Cartera();
		}
		return cartera;
	}
	public void setCartera(Cartera cartera) {
		this.cartera = cartera;
	}
	public Detallecuota getDetallecuota() {
		if (detallecuota == null) {
			detallecuota = new Detallecuota();
		}
		return detallecuota;
	}
	public void setDetallecuota(Detallecuota detallecuota) {
		this.detallecuota = detallecuota;
	}
	public List<Cuota> getCuotasxcontrato() {
		return cuotasxcontrato;
	}
	public void setCuotasxcontrato(List<Cuota> cuotasxcontrato) {
		this.cuotasxcontrato = cuotasxcontrato;
	}
	public Double getValorTipoCambio() {
		return valorTipoCambio;
	}
	public void setValorTipoCambio(Double valorTipoCambio) {
		this.valorTipoCambio = valorTipoCambio;
	}
	public Date getFechaValorTipoCambio() {
		return fechaValorTipoCambio;
	}
	public void setFechaValorTipoCambio(Date fechaValorTipoCambio) {
		this.fechaValorTipoCambio = fechaValorTipoCambio;
	}
	public String getValorIgv() {
		return valorIgv;
	}
	public void setValorIgv(String valorIgv) {
		this.valorIgv = valorIgv;
	}
	public List<Tipopago> getTipopagos() {
		return tipopagos;
	}
	public void setTipopagos(List<Tipopago> tipopagos) {
		this.tipopagos = tipopagos;
	}
	public List<Tipodocu> getListaTipoDocumentos() {
		return listaTipoDocumentos;
	}
	public void setListaTipoDocumentos(List<Tipodocu> listaTipoDocumentos) {
		this.listaTipoDocumentos = listaTipoDocumentos;
	}
	public List<Tipomovimiento> getTipomovimientos() {
		return tipomovimientos;
	}
	public void setTipomovimientos(List<Tipomovimiento> tipomovimientos) {
		this.tipomovimientos = tipomovimientos;
	}
	public Boolean getUsotipocambiosistema() {
		return usotipocambiosistema;
	}
	public void setUsotipocambiosistema(Boolean usotipocambiosistema) {
		this.usotipocambiosistema = usotipocambiosistema;
	}
	public int getNrocuotasDetectadasContrato() {
		return nrocuotasDetectadasContrato;
	}
	public void setNrocuotasDetectadasContrato(int nrocuotasDetectadasContrato) {
		this.nrocuotasDetectadasContrato = nrocuotasDetectadasContrato;
	}
	public Double getMontoTotalContrato() {
		return montoTotalContrato;
	}
	public void setMontoTotalContrato(Double montoTotalContrato) {
		this.montoTotalContrato = montoTotalContrato;
	}
	public Double getMontoCuotaContrato() {
		return montoCuotaContrato;
	}
	public void setMontoCuotaContrato(Double montoCuotaContrato) {
		this.montoCuotaContrato = montoCuotaContrato;
	}
	public Double getMontoSolesIngresado() {
		return montoSolesIngresado;
	}
	public void setMontoSolesIngresado(Double montoSolesIngresado) {
		this.montoSolesIngresado = montoSolesIngresado;
	}
	public String getIdtipodocuseleccionado() {
		return idtipodocuseleccionado;
	}
	public void setIdtipodocuseleccionado(String idtipodocuseleccionado) {
		this.idtipodocuseleccionado = idtipodocuseleccionado;
	}
	public String getIdtipomovimientoseleccionado() {
		return idtipomovimientoseleccionado;
	}
	public void setIdtipomovimientoseleccionado(String idtipomovimientoseleccionado) {
		this.idtipomovimientoseleccionado = idtipomovimientoseleccionado;
	}
	public int getIdcontratoseleccionado() {
		return idcontratoseleccionado;
	}
	public void setIdcontratoseleccionado(int idcontratoseleccionado) {
		this.idcontratoseleccionado = idcontratoseleccionado;
	}
	public String getIdtipopagoseleccionado() {
		return idtipopagoseleccionado;
	}
	public void setIdtipopagoseleccionado(String idtipopagoseleccionado) {
		this.idtipopagoseleccionado = idtipopagoseleccionado;
	}
	public List<CuotaBean> getlistaCuotasPendientesCondicion() {
		return listaCuotasPendientesCondicion;
	}
	public void setlistaCuotasPendientesCondicion(List<CuotaBean> listaCuotasPendientesCondicion) {
		this.listaCuotasPendientesCondicion = listaCuotasPendientesCondicion;
	}
	public List<CuotaBean> getlistaCuotasPendientesCondicionCancelar() {
		return listaCuotasPendientesCondicionCancelar;
	}
	public void setlistaCuotasPendientesCondicionCancelar(List<CuotaBean> listaCuotasPendientesCondicionCancelar) {
		this.listaCuotasPendientesCondicionCancelar = listaCuotasPendientesCondicionCancelar;
	}
	public int getNrocuotaFila() {
		return nrocuotaFila;
	}
	public void setNrocuotaFila(int nrocuotaFila) {
		this.nrocuotaFila = nrocuotaFila;
	}
	public Double getMoraFila() {
		return moraFila;
	}
	public void setMoraFila(Double moraFila) {
		this.moraFila = moraFila;
	}
	public Double getSubtotalFila() {
		return subtotalFila;
	}
	public void setSubtotalFila(Double subtotalFila) {
		this.subtotalFila = subtotalFila;
	}
	public Double getMontoFila() {
		return montoFila;
	}
	public void setMontoFila(Double montoFila) {
		this.montoFila = montoFila;
	}
	public CuotaBean getcuotaPendienteCondicion() {
		return cuotaPendienteCondicion;
	}
	public void setcuotaPendienteCondicion(CuotaBean cuotaPendienteCondicion) {
		this.cuotaPendienteCondicion = cuotaPendienteCondicion;
	}
	public Date getFechaValorIgv() {
		return fechaValorIgv;
	}
	public void setFechaValorIgv(Date fechaValorIgv) {
		this.fechaValorIgv = fechaValorIgv;
	}
	public int getNrocuotascanceladas() {
		return nrocuotascanceladas;
	}
	public void setNrocuotascanceladas(int nrocuotascanceladas) {
		this.nrocuotascanceladas = nrocuotascanceladas;
	}
	public int getNrocuotaPendienteCancelacion() {
		return nrocuotaPendienteCancelacion;
	}
	public void setNrocuotaPendienteCancelacion(int nrocuotaPendienteCancelacion) {
		this.nrocuotaPendienteCancelacion = nrocuotaPendienteCancelacion;
	}
	public List<Usuario> getListaCobradores() {
		return listaCobradores;
	}
	public void setListaCobradores(List<Usuario> listaCobradores) {
		this.listaCobradores = listaCobradores;
	}
	public int getIdcobradorseleccionado() {
		return idcobradorseleccionado;
	}
	public void setIdcobradorseleccionado(int idcobradorseleccionado) {
		this.idcobradorseleccionado = idcobradorseleccionado;
	}
	public List<Contrato> getListaContratosAgregarCartera() {
		return listaContratosAgregarCartera;
	}
	public void setListaContratosAgregarCartera(List<Contrato> listaContratosAgregarCartera) {
		this.listaContratosAgregarCartera = listaContratosAgregarCartera;
	}
	public Contrato getContratoCartera() {
		return contratoCartera;
	}
	public void setContratoCartera(Contrato contratoCartera) {
		this.contratoCartera = contratoCartera;
	}
	public List<Cartera> getListaCarteras() {
		return listaCarteras;
	}
	public void setListaCarteras(List<Cartera> listaCarteras) {
		this.listaCarteras = listaCarteras;
	}
	public List<Cartera> getFiltrolistaCarteras() {
		return filtrolistaCarteras;
	}
	public void setFiltrolistaCarteras(List<Cartera> filtrolistaCarteras) {
		this.filtrolistaCarteras = filtrolistaCarteras;
	}
	public Comprobantepago getComprobantepago() {
		return comprobantepago;
	}
	public void setComprobantepago(Comprobantepago comprobantepago) {
		this.comprobantepago = comprobantepago;
	}
	public int getNromesespagados() {
		return nromesespagados;
	}
	public void setNromesespagados(int nromesespagados) {
		this.nromesespagados = nromesespagados;
	}
	public int getNromesespendientes() {
		return nromesespendientes;
	}
	public void setNromesespendientes(int nromesespendientes) {
		this.nromesespendientes = nromesespendientes;
	}
	public int getPeriodoSinContrato() {
		return periodoSinContrato;
	}
	public void setPeriodoSinContrato(int periodoSinContrato) {
		this.periodoSinContrato = periodoSinContrato;
	}
	public Double getMontomensualrenta() {
		return montomensualrenta;
	}
	public void setMontomensualrenta(Double montomensualrenta) {
		this.montomensualrenta = montomensualrenta;
	}
	public String getMesFila() {
		return mesFila;
	}
	public void setMesFila(String mesFila) {
		this.mesFila = mesFila;
	}
	public List<Contrato> getListaContratosVigentes() {
		return listaContratosVigentes;
	}
	public void setListaContratosVigentes(List<Contrato> listaContratosVigentes) {
		this.listaContratosVigentes = listaContratosVigentes;
	}
	public List<String> getListaIdContratosAgregarCartera() {
		return listaIdContratosAgregarCartera;
	}
	public void setListaIdContratosAgregarCartera(List<String> listaIdContratosAgregarCartera) {
		this.listaIdContratosAgregarCartera = listaIdContratosAgregarCartera;
	}
	public int getIdcarteraEditar() {
		return idcarteraEditar;
	}
	public void setIdcarteraEditar(int idcarteraEditar) {
		this.idcarteraEditar = idcarteraEditar;
	}
	public String getNrocomprobantepadrenotadebito() {
		return nrocomprobantepadrenotadebito;
	}
	public void setNrocomprobantepadrenotadebito(String nrocomprobantepadrenotadebito) {
		this.nrocomprobantepadrenotadebito = nrocomprobantepadrenotadebito;
	}

	public String getClaveupa() {
		return claveupa;
	}
	public void setClaveupa(String claveupa) {
		this.claveupa = claveupa;
	}
	public List<CondicionBean> getListaContratosDeUpa() {
		return listaContratosDeUpa;
	}
	public void setListaContratosDeUpa(List<CondicionBean> listaContratosDeUpa) {
		this.listaContratosDeUpa = listaContratosDeUpa;
	}
	public boolean getSivisibleigv() {
		return sivisibleigv;
	}
	public void setSivisibleigv(boolean sivisibleigv) {
		this.sivisibleigv = sivisibleigv;
	}
	public Double getTotalmora() {
		return totalmora;
	}
	public void setTotalmora(Double totalmora) {
		this.totalmora = totalmora;
	}
	public Date getFechaemisionNotaDebito() {
		return fechaemisionNotaDebito;
	}
	public void setFechaemisionNotaDebito(Date fechaemisionNotaDebito) {
		this.fechaemisionNotaDebito = fechaemisionNotaDebito;
	}
	public String getNombrecomprobanteNotadebito() {
		return nombrecomprobanteNotadebito;
	}
	public void setNombrecomprobanteNotadebito(
			String nombrecomprobanteNotadebito) {
		this.nombrecomprobanteNotadebito = nombrecomprobanteNotadebito;
	}
	public String getObservacionnotadebito() {
		return observacionnotadebito;
	}
	public void setObservacionnotadebito(String observacionnotadebito) {
		this.observacionnotadebito = observacionnotadebito;
	}
	public List<Comprobantepago> getListacomprobantes() {
		return listacomprobantes;
	}
	public void setListacomprobantes(List<Comprobantepago> listacomprobantes) {
		this.listacomprobantes = listacomprobantes;
	}
	public HashMap<Integer, CuotaBean> getMapacuotames() {
		return mapacuotames;
	}
	public void setMapacuotames(HashMap<Integer, CuotaBean> mapacuotames) {
		this.mapacuotames = mapacuotames;
	}
	public String getMescuotaFila() {
		return mescuotaFila;
	}
	public void setMescuotaFila(String mescuotaFila) {
		this.mescuotaFila = mescuotaFila;
	}
	public String getNombrecarteraContrato() {
		return nombrecarteraContrato;
	}
	public void setNombrecarteraContrato(String nombrecarteraContrato) {
		this.nombrecarteraContrato = nombrecarteraContrato;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public List<CuotaBean> getListaCuotasPendientesSinContratoCompromisoCancelar() {
		return listaCuotasPendientesCondicionCancelar;
	}
	public void setListaCuotasPendientesSinContratoCompromisoCancelar(List<CuotaBean> listaCuotasPendientesCondicionCancelar) {
		this.listaCuotasPendientesCondicionCancelar = listaCuotasPendientesCondicionCancelar;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public int getIddetallecartera() {
		return iddetallecartera;
	}
	public void setIddetallecartera(int iddetallecartera) {
		this.iddetallecartera = iddetallecartera;
	}
	public Boolean getSigarantia() {
		return sigarantia;
	}
	public void setSigarantia(Boolean sigarantia) {
		this.sigarantia = sigarantia;
	}
    
	public AutovaluoCobranzaManagedBean getAutovaluoCobranzaMB() {
		return autovaluoCobranzaMB;
	}

	public void setAutovaluoCobranzaMB(
			AutovaluoCobranzaManagedBean autovaluoCobranzaMB) {
		this.autovaluoCobranzaMB = autovaluoCobranzaMB;
	}

//	public CobranzaConsultaManagedBean getCobranzaConsultaMB() {
//		return cobranzaConsultaMB;
//	}
//
//	public void setCobranzaConsultaMB(CobranzaConsultaManagedBean cobranzaConsultaMB) {
//		this.cobranzaConsultaMB = cobranzaConsultaMB;
//	}

	public List<CuotaBean> getListaCuotasPagadas() {
		return listaCuotasPagadas;
	}
	public void setListaCuotasPagadas(List<CuotaBean> listaCuotasPagadas) {
		this.listaCuotasPagadas = listaCuotasPagadas;
	}
	public List<CuotaBean> getListaCuotasPagadasFilter() {
		return listaCuotasPagadasFilter;
	}
	public void setListaCuotasPagadasFilter(
			List<CuotaBean> listaCuotasPagadasFilter) {
		this.listaCuotasPagadasFilter = listaCuotasPagadasFilter;
	}
	public Boolean getVisibleReporte() {
		return visibleReporte;
	}
	public void setVisibleReporte(Boolean visibleReporte) {
		this.visibleReporte = visibleReporte;
	}
	public Boolean getVisibleCartera() {
		return visibleCartera;
	}
	public void setVisibleCartera(Boolean visibleCartera) {
		this.visibleCartera = visibleCartera;
	}
	public Boolean getVisibleCobranza() {
		return visibleCobranza;
	}
	public void setVisibleCobranza(Boolean visibleCobranza) {
		this.visibleCobranza = visibleCobranza;
	}
	public IContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public Boolean getSiperfilcobrador() {
		return siperfilcobrador;
	}
	public void setSiperfilcobrador(Boolean siperfilcobrador) {
		this.siperfilcobrador = siperfilcobrador;
	}
	public String getCarteradetectadaCobrador() {
		return carteradetectadaCobrador;
	}
	public void setCarteradetectadaCobrador(String carteradetectadaCobrador) {
		this.carteradetectadaCobrador = carteradetectadaCobrador;
	}
	public String getObservacionnotacredito() {
		return observacionnotacredito;
	}
	public void setObservacionnotacredito(String observacionnotacredito) {
		this.observacionnotacredito = observacionnotacredito;
	}
	public Date getFechaemisionNotaCredito() {
		return fechaemisionNotaCredito;
	}
	public void setFechaemisionNotaCredito(Date fechaemisionNotaCredito) {
		this.fechaemisionNotaCredito = fechaemisionNotaCredito;
	}
	public int getIdcomprobantepadrenotadebito() {
		return idcomprobantepadrenotadebito;
	}
	public void setIdcomprobantepadrenotadebito(int idcomprobantepadrenotadebito) {
		this.idcomprobantepadrenotadebito = idcomprobantepadrenotadebito;
	}
	public String getIdtipoconceptoseleccionado() {
		return idtipoconceptoseleccionado;
	}
	public void setIdtipoconceptoseleccionado(String idtipoconceptoseleccionado) {
		this.idtipoconceptoseleccionado = idtipoconceptoseleccionado;
	}
	public Double getTotalnotacredito() {
		return totalnotacredito;
	}
	public void setTotalnotacredito(Double totalnotacredito) {
		this.totalnotacredito = totalnotacredito;
	}
	public Double getSubtotalnotacredito() {
		return subtotalnotacredito;
	}
	public void setSubtotalnotacredito(Double subtotalnotacredito) {
		this.subtotalnotacredito = subtotalnotacredito;
	}
	public Double getIgvnotacredito() {
		return igvnotacredito;
	}
	public void setIgvnotacredito(Double igvnotacredito) {
		this.igvnotacredito = igvnotacredito;
	}
	public String getNombredetectadoCobrador() {
		return nombredetectadoCobrador;
	}
	public void setNombredetectadoCobrador(String nombredetectadoCobrador) {
		this.nombredetectadoCobrador = nombredetectadoCobrador;
	}
	public Boolean getSicobrador() {
		return sicobrador;
	}
	public void setSicobrador(Boolean sicobrador) {
		this.sicobrador = sicobrador;
	}
	public Date getAyer() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1*(FuncionesHelper.DIASTOLERANCIA-1));
		return calendar.getTime();
	}
	public Date getHoy() {
		return new Date();
	}
	public ItemBean getSelectedCobrador() {
		return selectedCobrador;
	}
	public void setSelectedCobrador(ItemBean selectedCobrador) {
		this.selectedCobrador = selectedCobrador;
	}
	public IFHvariadoService getFHvariadoService() {
		return FHvariadoService;
	}
	public void setFHvariadoService(IFHvariadoService fHvariadoService) {
		FHvariadoService = fHvariadoService;
	}
	public String getSelectedTipoDocumento() {
		return selectedTipoDocumento;
	}
	public void setSelectedTipoDocumento(String selectedTipoDocumento) {
		this.selectedTipoDocumento = selectedTipoDocumento;
	}
	public List<CuotaBean> getListaCuotasPendientesCondicionCancelar() {
		return listaCuotasPendientesCondicionCancelar;
	}
	public void setListaCuotasPendientesCondicionCancelar(List<CuotaBean> listaCuotasPendientesCondicionCancelar) {
		this.listaCuotasPendientesCondicionCancelar = listaCuotasPendientesCondicionCancelar;
	}
	public List<CuotaBean> getListaCuotasPendientesCondicionCancelarFilter() {
		return listaCuotasPendientesCondicionCancelarFilter;
	}
	public void setListaCuotasPendientesCondicionCancelarFilter(List<CuotaBean> listaCuotasPendientesCondicionCancelarFilter) {
		this.listaCuotasPendientesCondicionCancelarFilter = listaCuotasPendientesCondicionCancelarFilter;
	}
	public CondicionBean getCondicionSeleccionado() {
		return condicionSeleccionado;
	}
	public void setCondicionSeleccionado(CondicionBean condicionSeleccionado) {
		this.condicionSeleccionado = condicionSeleccionado;
	}
	public IRecaudacionCarteraService getCarteraService() {
		return carteraService;
	}
	public void setCarteraService(IRecaudacionCarteraService carteraService) {
		this.carteraService = carteraService;
	}

	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}

	public void setRecaudacionReporteService(IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}

	public BigDecimal getMontoDetraccion() {
		return montoDetraccion;
	}
	public void setMontoDetraccion(BigDecimal montoDetraccion) {
		this.montoDetraccion = montoDetraccion;
	}
	public BigDecimal getTotalSubTotalComprobante() {
		return totalSubTotalComprobante;
	}
	public void setTotalSubTotalComprobante(BigDecimal totalSubTotalComprobante) {
		this.totalSubTotalComprobante = totalSubTotalComprobante;
	}
	public BigDecimal getIgvComprobante() {
		return igvComprobante;
	}
	public void setIgvComprobante(BigDecimal igvComprobante) {
		this.igvComprobante = igvComprobante;
	}
	public BigDecimal getTotalComprobante() {
		return totalComprobante;
	}
	public void setTotalComprobante(BigDecimal totalComprobante) {
		this.totalComprobante = totalComprobante;
	}
	public String getTipoDescuentoNotaCreditoSeleccionado() {
		return tipoDescuentoNotaCreditoSeleccionado;
	}
	public void setTipoDescuentoNotaCreditoSeleccionado(String tipoDescuentoNotaCreditoSeleccionado) {
		this.tipoDescuentoNotaCreditoSeleccionado = tipoDescuentoNotaCreditoSeleccionado;
	}
	public List<DetallecarteraBean> getListadetalleCarteraBean() {
		return listadetalleCarteraBean;
	}
	public void setListadetalleCarteraBean(List<DetallecarteraBean> listadetalleCarteraBean) {
		this.listadetalleCarteraBean = listadetalleCarteraBean;
	}
	public List<DetallecarteraBean> getFiltrolistadetalleCarteraBean() {
		return filtrolistadetalleCarteraBean;
	}
	public void setFiltrolistadetalleCarteraBean(
			List<DetallecarteraBean> filtrolistadetalleCarteraBean) {
		this.filtrolistadetalleCarteraBean = filtrolistadetalleCarteraBean;
	}
	public DetallecarteraBean getDetallecarteraBean() {
		return detallecarteraBean;
	}
	public void setDetallecarteraBean(DetallecarteraBean detallecarteraBean) {
		this.detallecarteraBean = detallecarteraBean;
	}
	public List<Comprobantepago> getListaReciboCajaReferencia() {
		return listaReciboCajaReferencia;
	}
	public void setListaReciboCajaReferencia(List<Comprobantepago> listaReciboCajaReferencia) {
		this.listaReciboCajaReferencia = listaReciboCajaReferencia;
	}
	public Boolean getSivisibleReciboCajaRef() {
		return sivisibleReciboCajaRef;
	}
	public void setSivisibleReciboCajaRef(Boolean sivisibleReciboCajaRef) {
		this.sivisibleReciboCajaRef = sivisibleReciboCajaRef;
	}
	public Comprobantepago getReciboCajaReferencia() {
		return reciboCajaReferencia;
	}
	public void setReciboCajaReferencia(Comprobantepago reciboCajaReferencia) {
		this.reciboCajaReferencia = reciboCajaReferencia;
	}
	public String getIdtipodocpadrenotadebito() {
		return idtipodocpadrenotadebito;
	}
	public void setIdtipodocpadrenotadebito(String idtipodocpadrenotadebito) {
		this.idtipodocpadrenotadebito = idtipodocpadrenotadebito;
	}
	public Comprobantepago getComprobantepagoPadreNotaTipo() {
		return comprobantepagoPadreNotaTipo;
	}
	public void setComprobantepagoPadreNotaTipo(Comprobantepago comprobantepagoPadreNotaTipo) {
		this.comprobantepagoPadreNotaTipo = comprobantepagoPadreNotaTipo;
	}
	public int getIdComprobantePagoPadrenotatipo() {
		return idComprobantePagoPadrenotatipo;
	}
	public void setIdComprobantePagoPadrenotatipo(int idComprobantePagoPadrenotatipo) {
		this.idComprobantePagoPadrenotatipo = idComprobantePagoPadrenotatipo;
	}
	public String getTipodocumentoSeleccionado() {
		return tipodocumentoSeleccionado;
	}
	public void setTipodocumentoSeleccionado(String tipodocumentoSeleccionado) {
		this.tipodocumentoSeleccionado = tipodocumentoSeleccionado;
	}
	public Boolean getSifacturacionelectronica() {
		return sifacturacionelectronica;
	}
	public void setSifacturacionelectronica(Boolean sifacturacionelectronica) {
		this.sifacturacionelectronica = sifacturacionelectronica;
	}

	public String getSerieFacturacionFisica() {
		return serieFacturacionFisica;
	}

	public void setSerieFacturacionFisica(String serieFacturacionFisica) {
		this.serieFacturacionFisica = serieFacturacionFisica;
	}
	public String getCorrelativoFacturacionFisica() {
		return correlativoFacturacionFisica;
	}
	public void setCorrelativoFacturacionFisica(String correlativoFacturacionFisica) {
		this.correlativoFacturacionFisica = correlativoFacturacionFisica;
	}
	public Boolean getSigeneracionautomaticanotadebito() {
		return sigeneracionautomaticanotadebito;
	}
	public void setSigeneracionautomaticanotadebito(Boolean sigeneracionautomaticanotadebito) {
		this.sigeneracionautomaticanotadebito = sigeneracionautomaticanotadebito;
	}
	
	public Boolean getSigeneracionautomaticanotadebitoPenalizacion() {
		return sigeneracionautomaticanotadebitoPenalizacion;
	}

	public void setSigeneracionautomaticanotadebitoPenalizacion(
			Boolean sigeneracionautomaticanotadebitoPenalizacion) {
		this.sigeneracionautomaticanotadebitoPenalizacion = sigeneracionautomaticanotadebitoPenalizacion;
	}

	public Boolean getSiActivoFechaVencimiento() {
		return siActivoFechaVencimiento;
	}
	public void setSiActivoFechaVencimiento(Boolean siActivoFechaVencimiento) {
		this.siActivoFechaVencimiento = siActivoFechaVencimiento;
	}
	public Date getMinFechaVencimiento() {
		return minFechaVencimiento;
	}
	public void setMinFechaVencimiento(Date minFechaVencimiento) {
		this.minFechaVencimiento = minFechaVencimiento;
	}
	public Date getMaxFechaVencimiento() {
		return maxFechaVencimiento;
	}
	public void setMaxFechaVencimiento(Date maxFechaVencimiento) {
		this.maxFechaVencimiento = maxFechaVencimiento;
	}

	public Date getMinfechacancelacion() {
		return minfechacancelacion;
	}

	public void setMinfechacancelacion(Date minfechacancelacion) {
		this.minfechacancelacion = minfechacancelacion;
	}

	public String getFechaoperacion() {
		return fechaoperacion;
	}

	public void setFechaoperacion(String fechaoperacion) {
		this.fechaoperacion = fechaoperacion;
	}

	public String getNumerooperacion() {
		return numerooperacion;
	}

	public void setNumerooperacion(String numerooperacion) {
		this.numerooperacion = numerooperacion;
	}

	public CuotaBean getCuotaPendienteCondicion() {
		return cuotaPendienteCondicion;
	}

	public void setCuotaPendienteCondicion(CuotaBean cuotaPendienteCondicion) {
		this.cuotaPendienteCondicion = cuotaPendienteCondicion;
	}

	public List<CuotaBean> getListaCuotasPendientesCondicion() {
		return listaCuotasPendientesCondicion;
	}

	public void setListaCuotasPendientesCondicion(
			List<CuotaBean> listaCuotasPendientesCondicion) {
		this.listaCuotasPendientesCondicion = listaCuotasPendientesCondicion;
	}

	public Boolean getSiperfilpermitidocobranza() {
		return siperfilpermitidocobranza;
	}

	public void setSiperfilpermitidocobranza(Boolean siperfilpermitidocobranza) {
		this.siperfilpermitidocobranza = siperfilpermitidocobranza;
	}

	public String getTmpnrocartera() {
		return tmpnrocartera;
	}

	public void setTmpnrocartera(String tmpnrocartera) {
		this.tmpnrocartera = tmpnrocartera;
	}

	public void setHoy(Date hoy) {
		this.hoy = hoy;
	}

	public void setAyer(Date ayer) {
		this.ayer = ayer;
	}

	public String getTipoNotaDebitoyCredito() {
		return tipoNotaDebitoyCredito;
	}

	public void setTipoNotaDebitoyCredito(String tipoNotaDebitoyCredito) {
		this.tipoNotaDebitoyCredito = tipoNotaDebitoyCredito;
	}

	public Double getTotalpenalizacion() {
		return totalpenalizacion;
	}

	public void setTotalpenalizacion(Double totalpenalizacion) {
		this.totalpenalizacion = totalpenalizacion;
	}

	public String getObservacionnotadebitopenalizacion() {
		return observacionnotadebitopenalizacion;
	}

	public void setObservacionnotadebitopenalizacion(
			String observacionnotadebitopenalizacion) {
		this.observacionnotadebitopenalizacion = observacionnotadebitopenalizacion;
	}


	public Comprobantepago getComprobantepagoPadreNotaTipoND() {
		return comprobantepagoPadreNotaTipoND;
	}

	public void setComprobantepagoPadreNotaTipoND(
			Comprobantepago comprobantepagoPadreNotaTipoND) {
		this.comprobantepagoPadreNotaTipoND = comprobantepagoPadreNotaTipoND;
	}

	public String getTipoConceptoNotaDebito() {
		return tipoConceptoNotaDebito;
	}

	public void setTipoConceptoNotaDebito(String tipoConceptoNotaDebito) {
		this.tipoConceptoNotaDebito = tipoConceptoNotaDebito;
	}

	public String getOpciontipodoc() {
		return opciontipodoc;
	}

	public void setOpciontipodoc(String opciontipodoc) {
		this.opciontipodoc = opciontipodoc;
	}

	public Boolean getSivalidacioncomprobantefisico() {
		return sivalidacioncomprobantefisico;
	}

	public void setSivalidacioncomprobantefisico(
			Boolean sivalidacioncomprobantefisico) {
		this.sivalidacioncomprobantefisico = sivalidacioncomprobantefisico;
	}

	public String getNombreusuariologueado() {
		return nombreusuariologueado;
	}

	public void setNombreusuariologueado(String nombreusuariologueado) {
		this.nombreusuariologueado = nombreusuariologueado;
	}

	public Boolean getUser_acceso_fechacanc() {
		return user_acceso_fechacanc;
	}

	public void setUser_acceso_fechacanc(Boolean user_acceso_fechacanc) {
		this.user_acceso_fechacanc = user_acceso_fechacanc;
	}

	public Date getConsulta_minfechacancelacion() {
		return consulta_minfechacancelacion;
	}

	public void setConsulta_minfechacancelacion(Date consulta_minfechacancelacion) {
		this.consulta_minfechacancelacion = consulta_minfechacancelacion;
	}

	public Boolean getVisibleCobranzaRegistro() {
		return visibleCobranzaRegistro;
	}

	public void setVisibleCobranzaRegistro(Boolean visibleCobranzaRegistro) {
		this.visibleCobranzaRegistro = visibleCobranzaRegistro;
	}

	public String getAcceso_opcion() {
		return acceso_opcion;
	}

	public void setAcceso_opcion(String acceso_opcion) {
		this.acceso_opcion = acceso_opcion;
	}

	public List<Tipotransaccion> getTipotransaccions() {
		return tipotransaccions;
	}

	public void setTipotransaccions(List<Tipotransaccion> tipotransaccions) {
		this.tipotransaccions = tipotransaccions;
	}

	public String getIdtipotransaccionseleccionado() {
		return idtipotransaccionseleccionado;
	}

	public void setIdtipotransaccionseleccionado(String idtipotransaccionseleccionado) {
		this.idtipotransaccionseleccionado = idtipotransaccionseleccionado;
	}

	public String getIdtipomodalidadpago() {
		return idtipomodalidadpago;
	}

	public void setIdtipomodalidadpago(String idtipomodalidadpago) {
		this.idtipomodalidadpago = idtipomodalidadpago;
	}

	public List<Tipomodalidadpago> getListatipomodalidadpagos() {
		return listatipomodalidadpagos;
	}

	public void setListatipomodalidadpagos(
			List<Tipomodalidadpago> listatipomodalidadpagos) {
		this.listatipomodalidadpagos = listatipomodalidadpagos;
	}

	public IReporteGeneradorService getReporteGeneradorService() {
		return reporteGeneradorService;
	}

	public void setReporteGeneradorService(
			IReporteGeneradorService reporteGeneradorService) {
		this.reporteGeneradorService = reporteGeneradorService;
	}

	public AdministrarArchivoService getAdministrarArchivoService() {
		return administrarArchivoService;
	}

	public void setAdministrarArchivoService(
			AdministrarArchivoService administrarArchivoService) {
		this.administrarArchivoService = administrarArchivoService;
	}

	public Boolean getHabilitarDocDNI() {
		return habilitarDocDNI;
	}

	public void setHabilitarDocDNI(Boolean habilitarDocDNI) {
		this.habilitarDocDNI = habilitarDocDNI;
	}

	public Boolean getHabilitarDocRUC() {
		return habilitarDocRUC;
	}

	public void setHabilitarDocRUC(Boolean habilitarDocRUC) {
		this.habilitarDocRUC = habilitarDocRUC;
	}

	public Integer getLongDocuPersona() {
		return longDocuPersona;
	}

	public void setLongDocuPersona(Integer longDocuPersona) {
		this.longDocuPersona = longDocuPersona;
	}


		
}
/**2670**/
