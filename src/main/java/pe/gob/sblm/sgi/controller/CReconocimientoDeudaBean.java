package pe.gob.sblm.sgi.controller;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.ConvertirUtil;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.MaeIpc;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.ITipoCambioService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.service.IpcService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

/** 
* Resumen.
* Objeto : CReconocimientoDeudaBean
* Descripción : Clase controladora de CReconocimientoDeudaBean.
* Fecha de Creación : 27/01/2020.
* Autor :Marco Alarcón. 
* ------------------------------------------------------------------------
* Modificaciones
* Fecha            Nombre                     Descripción
* ------------------------------------------------------------------------
* 27/01/2020	Marco Alarcón				Creación de clase controladora
*.
*/

@ManagedBean(name = "rdMB")
@ViewScoped
public class CReconocimientoDeudaBean extends BaseController implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger Logger=LoggerFactory.getLogger(AutovaluoCobranzaManagedBean.class);
	
	private CondicionBean contratoBean= new CondicionBean();
	private String estado;
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private String activeIndexTabView;
	private Boolean sicontratofuturo;
	private int tipobusqueda;
	private int tipobusquedaUpa;
	private String tipobusquedaInquilino;
	private String valorbusqueda;
	private String valorbusquedaUpa;
	private String valorbusquedaInquilino;
	private boolean sidolar;
	private boolean sisoles;
	private boolean sigenerarcuotas=false;
	private List<Upa> listUpa, listUpaFiltro;
	private List<Inquilino> listInquilino, listInquilinoFiltro;
	List<Usuario> todosUsuarios;
	private List<Usuario> listadousuariosSeleccionados;
	private List<Cuentabancaria> listCtasBancarias;
	private List<CondicionBean> listContrato, listContratoFiltro,listaContrato,listaAdenda;
	private List<String> listaOrigenSupensionTemporalRenta; 
	
	private Institucion institucion;
	private Inquilino inquilino;
	private Upa upa;
	private Cliente cliente;
	private Representante representante;
	private Representante representanteaval;
	private RentaBean selectedRenta;
	private Cuentabancaria ctabancaria;
	
	private Integer numAniosContrato, numMesesContrato;
	private String  mesInicioCobro="",mesFinCobro="",anioInicioCobro="",anioFinCobro="",mesFinCobroResuelto="",anioFinCobroResuelto="",mesFinCobroAdenda="",anioFinCobroAdenda="";
	/****/
	private String mesDesde_rec="",anioDesde_rec="",mesHasta_rec="",anioHasta_rec="";
	private Boolean disabledFinCobro=true;
	private Boolean sitipocontratoalquiler=Boolean.FALSE;
	private String mensaje="";
	private CuotaBean cuotaPendienteCondicion = new CuotaBean();
	private List<CuotaBean> lista= new ArrayList<CuotaBean>();
	private List<CuotaBean> listaCuotasPendientesCondicion= new ArrayList<CuotaBean>();
	private List<CuotaBean> listaCuotasPendienltesCondicionCancelarFilter;
	private List<CuotaBean> listaCuotasPendientesCondicionCancelar = new ArrayList<CuotaBean>();
	private String claveupatemporal;
	private Double montosolescuotas;
	private Double montosolesdolares;
	/**numero de adenda*/
	private int nroadenda_ant=0;
	private Double tipcambio;
	private Boolean obser=false;
	
	private BigDecimal totaldeudasoles;
	private BigDecimal totaldeudadolares;
	private List<MaeDetalleCondicion> listaMaeCondicion= new ArrayList<MaeDetalleCondicion>();
	private List<MaeDetCondicionDetalle> listaMaeCondicionDetalle= new ArrayList<MaeDetCondicionDetalle>();
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	//private String ipAddress="";
	private List<Integer> listaidcontrato= new ArrayList<Integer>();
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{ipcService}")
	private transient IpcService ipcService;
	
	@ManagedProperty(value = "#{archivoMB}")
	ArchivoManagedBean archivoMB;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService reporteRecaudacionService;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	
	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;
	
	@PostConstruct
	public void init() {
		
		initNewMB();
		initListDAO();
		contratoBean.setSisuscrito(false);
		contratoBean.setSiactaentrega(false);
		contratoBean.setSiocupante(true);
		contratoBean.setNumerocuotas(0);
		contratoBean.setSiactaentrega(false);
		contratoBean.setMontodeuda(0.0);
		//
		contratoBean.setContraprestacionadicional(0.0);
		contratoBean.setSicontraprestacionadicional(false);
		contratoBean.setAniocontraprestacionadicional(0);
		//
		sidolar = false;
		sisoles = true;
		tipobusqueda=1;
		tipobusquedaInquilino=Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL;
		tipobusquedaUpa=1;
		activeIndexTabView="0";
		sicontratofuturo=Boolean.FALSE;
		contratoBean.setEstado(Constantes.CONDICION_ESTADO_VIGENTE);
		estado=Constantes.CONDICION_RECONOCIMIENTO_DEUDA;
		/***/
		contratoBean.setSiclausulaperiodogracia(Boolean.FALSE);
		contratoBean.setSiclausulareconocimientoinversion(Boolean.FALSE);
		contratoBean.setSiclausulareconocimientorenta(Boolean.FALSE);
		contratoBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);
		contratoBean.setSipenalidad(Boolean.FALSE);
		contratoBean.setSicontraprestacionadicional(Boolean.FALSE);
		contratoBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);
		contratoBean.setSiclausulapagoposterior(Boolean.FALSE);
		contratoBean.setSicuotascanceladas(Boolean.FALSE);
		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(new Date()));
		System.out.println("host-ip="+CommonsUtilities.hostname()+"/"+request.getRemoteAddr());
		 InetAddress ip;
	        String hostname;
	        try {
//	            ip = InetAddress.getLocalHost();
//	            hostname = ip.getHostName();
//	            System.out.println("Your current IP address : " + ip);
//	            System.out.println("Your current Hostname : " + hostname);
//	            System.out.println("host name ="+request.getLocalName());
//	            System.out.println("host name ="+request.getRemoteHost());
	             ip = InetAddress.getByName(request.getRemoteAddr());
	            System.out.println("host-name="+ip.getHostName());
	 
	        } catch (UnknownHostException e) {
	 
	            e.printStackTrace();
	        }

	}
	public void initNewMB() {
		listUpa = new ArrayList<Upa>();
		listInquilino = new ArrayList<Inquilino>();
		cliente = new Cliente();
		representanteaval = new Representante();
		todosUsuarios = new ArrayList<Usuario>();
		listadousuariosSeleccionados = new ArrayList<Usuario>();
		listCtasBancarias = new ArrayList<Cuentabancaria>();
	}
	public void initListDAO() {
		todosUsuarios = contratoService.obtenerUsuarios();
	}
	public void validarGrabarCabeceraContrato(){
		
//		if (StringUtils.isEmpty(contratoBean.getTipocontrato())) {
//			
//			addWarnMessage("","Ingrese el tipo de contrato para continuar.");
//			
//		} else if (contratoBean.getTipocontrato().equals("Alquiler") && StringUtils.isEmpty(contratoBean.getTipo())) {
//			
//			addWarnMessage("","Seleccionar el tipo de alquiler para continuar.");
//		} else 
		if(contratoBean.getNroacta()==null){
			addWarnMessage("","Ingrese el n\u00famero de acta de renococimiento de deuda para continuar.");
		}else if (estado.equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA) && contratoBean.getNroacta().equals("")) {
			
			addWarnMessage("","Ingrese el n\u00famero de acta de renococimiento de deuda para continuar.");
		}else if (contratoBean.getUsoespecifico().equals("")){
			addWarnMessage("","Ingrese el uso especifico del reconocimiento de deuda para continuar.");
		
		}else if (contratoBean.getIdupa()==0) {
			
			addWarnMessage("","Seleccionar una upa para continuar.");
		} else if (contratoBean.getIdinquilino()==0) {
			
			addWarnMessage("","Seleccionar un inquilino para continuar.");
		} else if (listaCuotasPendientesCondicionCancelar.size()==0) {
			
			addWarnMessage("","Debe agregar las cuotas pendientes  para continuar.");
		}else if (listaRentas.size()==0) {
			
			addWarnMessage("","Debe generar periodos de renta para continuar.");
//		}  else if (contratoBean.getSicartafianza() && (contratoBean.getValorcartafianza()==0.0 || contratoBean.getValorcartafianza()==null)) {
//			activeIndexTabView="5";
//			addWarnMessage("","Ingresar valor de carta fianza.");
			
//		}else if (contratoBean.getSicartafianza() && contratoBean.getFechavencimientocartafianza()==null) {
//			activeIndexTabView="5";
//			addWarnMessage("","Ingresar fecha de vencimiento de carta fianza.");
			
		}else if(contratoBean.getObservacion().length()==0 && obser){
			addWarnMessage("","No se esta agregando la observacion registrada.");
		} else  {
//			if(archivoMB.getListaArchivoAdjunto().size()==0){
//				RequestContext context = RequestContext.getCurrentInstance();  
//				context.execute("dlgValidarArchivoAdjunto.show();");
//			}else{
			//validarCuotaRentas();
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarContrato.show();");
//			}
			
		}
	}
//	PUBLIC VOID VALIDARCUOTARENTAS(){
//		FOR(RENTABEAN RENTA:LISTARENTAS){
//			FOR(MAEDETCONDICIONDETALLEBEAN MAEDETCOND :RENTA.GETMAECONDICIONDETALLE()){
//				SYSTEM.OUT.PRINTLN("MAEDETCOND ID CONTRATO= "+MAEDETCOND.GETIDCONTRATO());
//				SYSTEM.OUT.PRINTLN("MAEDETCOND ID DETALLECONDICION= "+MAEDETCOND.GETIDDETALLECONDICION());
//			}
//		}
//	}
	/**
	 * Graba o actualiza un nuevo contrato.
	 */
	public void grabarCabeceraContrato() {
		
		
		try{
		
		
		//El inicio de cobro y el fin de cobro se parse desde inicio en un dia 1 hasta el fin el un dia 31
		Calendar inicontrato = Calendar.getInstance();
		Calendar fincontrato = Calendar.getInstance();
		Calendar iniCobro = new GregorianCalendar();
		Calendar finCobro = new GregorianCalendar();
		InetAddress addr = InetAddress.getLocalHost();
		inicontrato.setTime(contratoBean.getIniciocontrato());
		fincontrato.setTime(contratoBean.getFincontrato());
		
		iniCobro.set(Calendar.YEAR,Integer.parseInt(anioInicioCobro));
		iniCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesInicioCobro))-1);
		iniCobro.set(Calendar.DATE, 1);
		iniCobro.set(Calendar.HOUR,0);
		iniCobro.set(Calendar.MINUTE,0);
		iniCobro.set(Calendar.SECOND,0);
		iniCobro.set(Calendar.MILLISECOND,0);
		
		
		finCobro.set(Calendar.YEAR,Integer.parseInt(anioFinCobro));
		finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesFinCobro))-1);
		finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		finCobro.set(Calendar.HOUR,0);
		finCobro.set(Calendar.MINUTE,0);
		finCobro.set(Calendar.SECOND,0);
		finCobro.set(Calendar.MILLISECOND,0);
		contratoBean.setIniciocobro(iniCobro.getTime());
		contratoBean.setFincobro(finCobro.getTime());
		
		
		//Moneda
		if (sisoles) {
			contratoBean.setMoneda("S");
		} else {
			contratoBean.setMoneda("D");
		}
		
		Contrato contrato=new Contrato();
		contrato.setIniciocobro(contratoBean.getIniciocobro());
		contrato.setFincobro(contratoBean.getFincobro());
	
		contratoBean.setCuota1(contratoBean.getContraprestacion());	
		contratoBean.setCuota2(contratoBean.getContraprestacion());	
		contratoBean.setCuota3(contratoBean.getContraprestacion());
		contratoBean.setCuota4(contratoBean.getContraprestacion());
		contratoBean.setCuota5(contratoBean.getContraprestacion());
		contratoBean.setCuota6(contratoBean.getContraprestacion());

		
//		for (int i = 0; i < listaRentas.size(); i++) {
//			if (i==0) {
//				contratoBean.setCuota1(listaRentas.get(i).getRenta());	
//			}else if (i==1) {
//				contratoBean.setCuota2(listaRentas.get(i).getRenta());	
//			}else if (i==2) {
//				contratoBean.setCuota3(listaRentas.get(i).getRenta());
//			}else if (i==3) {
//				contratoBean.setCuota4(listaRentas.get(i).getRenta());
//			}else if (i==4) {
//				contratoBean.setCuota5(listaRentas.get(i).getRenta());
//			}else if (i==5) {
//				contratoBean.setCuota6(listaRentas.get(i).getRenta());
//			}
//		}
		
		contrato.setMontocuotasoles(contratoBean.getCuota1());
		contrato.setMontocuotasoles2(contratoBean.getCuota2());
		contrato.setMontocuotasoles3(contratoBean.getCuota3());
		contrato.setMontocuotasoles4(contratoBean.getCuota4());
		contrato.setMontocuotasoles5(contratoBean.getCuota5());
		contrato.setMontocuotasoles6(contratoBean.getCuota6());

        contrato.setSiescriturapublica(contratoBean.getSiescriturapublica()==null? false : contratoBean.getSiescriturapublica());

		
		if (contratoBean.getIdcontrato() == 0) {//NUEVO
			
			contrato.setCondicion(estado);
			contrato.setEstado(contratoBean.getEstado());
			
			
//			Upa u= new Upa(contratoBean.getIdupa());
			contrato.setUpa(upa);
			
			Inquilino i=new Inquilino(contratoBean.getIdinquilino());
			contrato.setInquilino(i);
			
			
			contrato.setSiresuelto(false);
			contrato.setSiadenda(false);
			contrato.setFechacreacion(new Date());
			contrato.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			contrato.setSicuotascanceladas(false);
			contrato.setSicompromisopago(false);
			contrato.setSifinalizado(false);
			
			contrato.setNrocontrato(contratoBean.getNrocontrato());
			contrato.setNroacta(contratoBean.getNroacta());
			contrato.setTipo(contratoBean.getTipo());
			contrato.setTipocontrato(contratoBean.getTipocontrato());
			contrato.setObservacion(contratoBean.getObservacion());
			System.out.println("observacion==)==="+contrato.getObservacion());
			contrato.setUsoespecifico(contratoBean.getUsoespecifico());
			
			contrato.setSiocupante(contratoBean.getSiocupante());
			contrato.setSidniocupante(contratoBean.getDniocupante());
			contrato.setSinombreocupante(contratoBean.getNombreocupante());
			
			contrato.setSisuscrito(contratoBean.getSisuscrito());
			contrato.setSifechasuscrito(contratoBean.getFechasuscrito());
			
			contrato.setSiactaentrega(contratoBean.getSiactaentrega());
			contrato.setSifechaactaentrega(contratoBean.getFechaactaentrega());
			contrato.setNumeroactaentrega(contratoBean.getNumeroactaentrega());
			
			contrato.setSiresolucion(contratoBean.getSiresolucion());
			contrato.setNumeroresolucion(contratoBean.getNumeroresolucion());
			contrato.setFecharesolucion(contratoBean.getFecharesolucion());
			contrato.setFecmodresolucion(new Date());
			
			contrato.setIniciocontrato(contratoBean.getIniciocontrato());
			contrato.setFincontrato(contratoBean.getFincontrato());
			contrato.setNumerocuotas(numAniosContrato*12+ numMesesContrato);
			
			contrato.setTipomoneda(contratoBean.getMoneda());
			contrato.setSiclausulapagoposterior(contratoBean.getSiclausulapagoposterior());
			contrato.setSiclausulaperiodogracia(contratoBean.getSiclausulaperiodogracia());
			contrato.setSiclausulareconocimientoinversion(contratoBean.getSiclausulareconocimientoinversion());
			contrato.setSiclausulareconocimientorenta(contratoBean.getSiclausulareconocimientorenta());
			contrato.setSiclausulasuspensiontemporalrenta(contratoBean.getSiclausulasuspensiontemporalrenta());
			
			contrato.setSipagofraccionrenta(contratoBean.getSipagofraccionrenta());
			contrato.setSicontraprestacionadicional(contratoBean.getSicontraprestacionadicional());
			//contrato.setContraprestacionadicional(contratoBean.getContraprestacionadicional());
			contrato.setTiporeajusteanual(contratoBean.getTiporeajusteanual());
			contrato.setReajusteanual(contratoBean.getReajusteanual());			
			//contrato.setSigeneracionrentacompletada(contratoBean.getTiporeajusteanual().equals("2")?Boolean.FALSE:Boolean.TRUE);
			//contrato.setSigeneracionrentacompletada(Boolean.FALSE);
			contrato.setSinuevomantenimiento(Boolean.TRUE);
			contrato.setPorcentajepenalidad(contratoBean.getPorcentajepenalidad());
			contrato.setSipenalidad(contratoBean.getSipenalidad());
			//sianulado
			contrato.setSianulado(false);
			contrato.setSidocumento(Boolean.FALSE);
			contrato.setSireconocimientodeuda(Boolean.TRUE);
			contrato.setSimontoinicialdeuda(contratoBean.getSimontoinicialdeuda());
			contrato.setMontoinicialdeuda(contratoBean.getMontoinicialdeuda()!= null?  (contratoBean.getMontoinicialdeuda()>0? contratoBean.getMontoinicialdeuda() :0.0 ):0.0);
			contrato.setMontodeuda(contratoBean.getMontodeuda());
			//
			contrato.setMonto_acta(contratoBean.getMontodeuda());
			contrato.setMonto_inicial_acta(contratoBean.getMontoinicialdeuda()!= null?  (contratoBean.getMontoinicialdeuda()>0? contratoBean.getMontoinicialdeuda() :0.0 ):0.0);
			contrato.setMonto_cuota_acta(contratoBean.getContraprestacion());
			contrato.setFec_inicio_acta(contratoBean.getIniciocontrato());
			contrato.setFec_fin_acta(contratoBean.getFincontrato());
			contrato.setNromeses_rec(contratoBean.getNromes_rec());
			contrato.setFec_inicio_rec(contratoBean.getFechadesde_rec());
			contrato.setFec_fin_rec(contratoBean.getFechahasta_rec());
			//contrato.setHost_ip_cre(request.getRemoteHost()+"/"+request.getRemoteAddr());
			contrato.setHost_ip_cre(""+"/"+CommonsUtilities.getRemoteIpAddress());
			contrato.setObservacion(contratoBean.getObservacion());
			
			
			
			
			Set<MaeDetalleCondicion> maeDetalleCondicions= new HashSet<MaeDetalleCondicion>();
			
			for (RentaBean rentaBean : listaRentas) {
				MaeDetalleCondicion maeDetalleCondicion = new MaeDetalleCondicion();
				
				maeDetalleCondicion.setAnio(rentaBean.getAnio());
				maeDetalleCondicion.setMes(rentaBean.getMes());
				maeDetalleCondicion.setContrato(contrato);
				
				maeDetalleCondicion.setSiclausulapagoposterior(rentaBean.getSiclausulapagoposterior());
				maeDetalleCondicion.setMontopagoposterior(rentaBean.getMontopagoposterior());
				maeDetalleCondicion.setObservacionpagoposterior(rentaBean.getObservacionpagoposterior());
				
				maeDetalleCondicion.setSiclausulaperiodogracia(rentaBean.getSiclausulaperiodogracia());
				maeDetalleCondicion.setSiclausulareconocimientoinversion(rentaBean.getSiclausulareconocimientoinversion());
				
				maeDetalleCondicion.setSipagofraccionrenta(rentaBean.getSipagofraccionrenta());
				
				maeDetalleCondicion.setSiclausulareconocimientorenta(rentaBean.getSiclausulareconocimientorenta());
				maeDetalleCondicion.setObservacionreconocimientorenta(rentaBean.getObservacionreconocimientorenta());
				maeDetalleCondicion.setDescuentoreconocimientorenta(rentaBean.getDescuentoreconocimientorenta());
				
				
				maeDetalleCondicion.setSiclausulasuspensiontemporalrenta(rentaBean.getSiclausulasuspensiontemporalrenta());
				maeDetalleCondicion.setObservacionsuspensiontemporalrenta(rentaBean.getObservacionsuspensiontemporalrenta());
				maeDetalleCondicion.setMontosuspensiontemporalrenta(rentaBean.getMontosuspensiontemporalrenta());
				
				
				maeDetalleCondicion.setSecuencia(rentaBean.getSecuencia());
				
				maeDetalleCondicion.setContraprestacion(rentaBean.getContraprestacion());
				
				maeDetalleCondicion.setMontopagar(rentaBean.getMontopagar());
				maeDetalleCondicion.setRenta(rentaBean.getRenta());
				maeDetalleCondicion.setNumeromes(Integer.parseInt(Almanaque.mesanumero(rentaBean.getMes())));
				
				maeDetalleCondicion.setSirentagenerada(rentaBean.getSirentagenerada());
				maeDetalleCondicion.setEstado(Constante.ESTADO_GENERADO);
				//
				maeDetalleCondicion.setNrocuota(rentaBean.getNrocuota());
				maeDetalleCondicion.setFechavencimiento(rentaBean.getFechavencimiento());
				maeDetalleCondicion.setUsr_cre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				maeDetalleCondicion.setFec_cre(new Date());
				//maeDetalleCondicion.setObs_cre(rentaBean.getObservacionpagofraccionrenta());
				maeDetalleCondicion.setSianulado(Boolean.FALSE);
                maeDetalleCondicion.setHost_ip_cre(""+"/"+CommonsUtilities.getRemoteIpAddress());
                
				//agregar cuotas de deuda por contrato 
				Set<MaeDetCondicionDetalle> maedetcondiciondetalles= new HashSet<MaeDetCondicionDetalle>();
				for(MaeDetCondicionDetalleBean maedetcond :rentaBean.getMaecondiciondetalle()){
					MaeDetCondicionDetalle maedetalle= new MaeDetCondicionDetalle();
					maedetalle.setMaedetallecondicion(maeDetalleCondicion);
					maedetalle.setIdconcepto(maedetcond.getIdconcepto());
					maedetalle.setConcepto(maedetcond.getConcepto());
					maedetalle.setMes(maedetcond.getMes());
					maedetalle.setAnio(maedetcond.getAnio());
					maedetalle.setMonto(maedetcond.getMonto());
					maedetalle.setTipomoneda(maedetcond.getTipomoneda());
					maedetalle.setNumeromes(maedetcond.getNumeromes());
					maedetalle.setSecuencia(maedetcond.getSecuencia());
					maedetalle.setIdcontrato(maedetcond.getIdcontrato());
					maedetalle.setIddetallecondicion(maedetcond.getIddetallecondicion());
					maedetalle.setSianulado(maedetcond.getSianulado());
					maedetalle.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
                    maedetalle.setFeccre(new Date());
                    maedetalle.setEstado(maedetcond.getEstado());
                    //maedetalle.setHost_ip_cre(CommonsUtilities.getHostNameIp()+"/"+CommonsUtilities.getRemoteIpAddress());
                    maeDetalleCondicion.setHost_ip_cre(""+"/"+CommonsUtilities.getRemoteIpAddress());
                    listaidcontrato.add(maedetalle.getIdcontrato());
                    maedetcondiciondetalles.add(maedetalle); 
				}
				maeDetalleCondicion.setMaedetcondiciondetalles(maedetcondiciondetalles);
			    //
				maeDetalleCondicions.add(maeDetalleCondicion);
			}
			
			
			contrato.setMaeDetalleCondicions(maeDetalleCondicions);
			if(archivoMB.getListaArchivoAdjunto().size()>0){
				contrato.setSidocumento(Boolean.TRUE);
			}
			contrato.setSiescriturapublica(contratoBean.getSiescriturapublica()==null? false : contratoBean.getSiescriturapublica());
		    contrato.setSidetraccion(contratoBean.getSidetraccion()==null? false : contratoBean.getSidetraccion());
		    contrato.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
			contratoService.registrarContrato(contrato);
			
			//insertar los documentos
			
			insertarDocumentoAdjunto(contrato);
			asignarCondicionContrato(contrato,userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			//
			addInfoMessage("", "Se registro nuevo reconocimiento de deuda con éxito");
			limpiarRegistroPropiedades();
			archivoMB.limpiarDocumentoADjuntos();
			
		} else { //ACTUALIZAR
			
			System.out.println("actualizando contrato");
			
			
			//Valores internos que se conservan
			contrato.setIdcontrato(contratoBean.getIdcontrato());
			contrato.setCondicion(contratoBean.getCondicion());
			contrato.setEstado(contratoBean.getEstado());
			contrato.setSicuotascanceladas(contratoBean.getSicuotascanceladas());
			contrato.setFechacreacion(contratoBean.getFeccre());
			contrato.setUsuariocreador(contratoBean.getUsrcre());
			
//			contrato.setSicompromisopago(contratoBean.getSi);
			
			//Valores internos de auditoria
			contrato.setFechamodificacion(new Date());
			contrato.setSicompromisopago(false);
			contrato.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			
			
			//Valores cambiantes mediante interfaz grafica
			Upa upa= new Upa();
			upa.setIdupa(contratoBean.getIdupa());
			upa.setClave(contratoBean.getClaveUpa());
			contrato.setUpa(upa);
			 
			Inquilino inquilino= new Inquilino();
			inquilino.setIdinquilino(contratoBean.getIdinquilino());
			contrato.setInquilino(inquilino);
			
			contrato.setNrocontrato(contratoBean.getNrocontrato());
			contrato.setTipo(contratoBean.getTipo());
			contrato.setTipocontrato(contratoBean.getTipocontrato());
			contrato.setObservacion(contratoBean.getObservacion());
			contrato.setUsoespecifico(contratoBean.getUsoespecifico());
			contrato.setSiocupante(contratoBean.getSiocupante());
			contrato.setSinombreocupante(contratoBean.getNombreocupante());
			contrato.setSidniocupante(contratoBean.getDniocupante());
			
			
			contrato.setIniciocontrato(contratoBean.getIniciocontrato());
			contrato.setFincontrato(contratoBean.getFincontrato());
			contrato.setNumerocuotas(contratoBean.getNumerocuotas());
			
			contrato.setSiresuelto(contratoBean.getSiresuelto());
			contrato.setFecmodresuelto(contratoBean.getFecmodresuelto());
			contrato.setObservacionresuelto(contratoBean.getObservacionresuelto());
			contrato.setFincontratoresuelto(contratoBean.getFincontratoresuelto());
			
			contrato.setSiadenda(contratoBean.getSiactaentrega());
			contrato.setObservacionadenda(contratoBean.getObservacionadenda());
			contrato.setFincontratoadenda(contratoBean.getFincontratoadenda());
			
			contrato.setSisuscrito(contratoBean.getSisuscrito());
			contrato.setSifechasuscrito(contratoBean.getFechasuscrito());
			
			contrato.setSiresolucion(contratoBean.getSiresolucion());
			contrato.setNumeroresolucion(contratoBean.getNumeroresolucion());
			contrato.setFecmodresolucion(contratoBean.getFecharesolucion());

			contrato.setSiactaentrega(contratoBean.getSiactaentrega());
			contrato.setNumeroactaentrega(contratoBean.getNumeroactaentrega());
			contrato.setSifechaactaentrega(contratoBean.getFechaactaentrega());
			
			contrato.setTipomoneda(contratoBean.getMoneda());
			contrato.setSifinalizado(contratoBean.getSifinalizado());
			
			
			contrato.setSiclausulapagoposterior(contratoBean.getSiclausulapagoposterior());
			contrato.setSiclausulaperiodogracia(contratoBean.getSiclausulaperiodogracia());
			contrato.setSiclausulareconocimientoinversion(contratoBean.getSiclausulareconocimientoinversion());
			contrato.setSiclausulareconocimientorenta(contratoBean.getSiclausulareconocimientorenta());
			contrato.setSipagofraccionrenta(contratoBean.getSipagofraccionrenta());
			
			contrato.setSicontraprestacionadicional(contratoBean.getSicontraprestacionadicional());
			contrato.setContraprestacionadicional(contratoBean.getContraprestacionadicional());
			contrato.setTiporeajusteanual(contratoBean.getTiporeajusteanual());
			contrato.setReajusteanual(contratoBean.getReajusteanual());			
			contrato.setSinuevomantenimiento(Boolean.TRUE);
			//SIANULADO
//			contrato.setSianulado(contratoBean.getSianulado());
//			contrato.setObservacionanulado(contratoBean.getObservacionanulado());
			contrato.setSianulado(contratoBean.getSiadenda());
			contrato.setSidocumento(contratoBean.getSidocumento());
			contrato.setSireconocimientodeuda(Boolean.FALSE);
			////
			contrato.setMonto_acta(contratoBean.getMontodeuda());
			contrato.setMonto_inicial_acta(contratoBean.getMontoinicialdeuda()!= null?  (contratoBean.getMontoinicialdeuda()>0? contratoBean.getMontoinicialdeuda() :0.0 ):0.0);
			contrato.setMonto_cuota_acta(contratoBean.getContraprestacion());
			contrato.setFec_inicio_acta(contratoBean.getIniciocontrato());
			contrato.setFec_fin_acta(contratoBean.getFincontrato());
			contrato.setNromeses_rec(contratoBean.getNromes_rec());
			contrato.setFec_inicio_rec(contratoBean.getFechadesde_rec());
			contrato.setFec_fin_rec(contratoBean.getFechahasta_rec());
			contrato.setHost_ip_cre(addr.getHostName()+"/"+addr.getHostAddress());
			contrato.setObservacion(contratoBean.getObservacion());
			
			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se actualizó nuevo contrato con éxito");

			limpiarRegistroPropiedades();
			contratoBean = new CondicionBean();
			listContrato.clear();
		}
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	public void insertarDocumentoAdjunto(Contrato contrato){
		   //buscar contrato insertado
	        System.out.println("IDCONTRATO"+contrato.getIdcontrato());
		  // listContrato=contratoService.buscarContrato(contrato);
	       System.out.println("tamaño archivo="+archivoMB.getListaArchivoAdjunto().size());
	       if(archivoMB.getListaArchivoAdjunto().size()>0){
	    	   archivoMB.grabarDocumentoAdjunto(contrato.getCondicion(),contrato.getIdcontrato());
	       }
		   
	}
	public void limpiarRegistroPropiedades() {
		
		contratoBean = new CondicionBean();
		contratoBean.setSisuscrito(false);
		contratoBean.setSiactaentrega(false);
		contratoBean.setSiresuelto(false);
		contratoBean.setSiadenda(false);
		contratoBean.setSiocupante(true);
		institucion = null;
		representante = null;
		cliente = null;
		representanteaval = null;
		ctabancaria = null;
		anioInicioCobro="";
		mesInicioCobro="";
		anioFinCobro="";
		mesFinCobro="";
		sicontratofuturo=Boolean.FALSE;
		contratoBean.setEstado(Constantes.CONDICION_ESTADO_VIGENTE);
		sitipocontratoalquiler=Boolean.FALSE;
		
		listaRentas.clear();
		/**/
		sicontratofuturo=Boolean.FALSE;
		contratoBean.setEstado(Constantes.CONDICION_ESTADO_VIGENTE);
		estado=Constantes.CONDICION_RECONOCIMIENTO_DEUDA;
		/***/
		contratoBean.setSiclausulaperiodogracia(Boolean.FALSE);
		contratoBean.setSiclausulareconocimientoinversion(Boolean.FALSE);
		contratoBean.setSiclausulareconocimientorenta(Boolean.FALSE);
		contratoBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);
		contratoBean.setSipenalidad(Boolean.FALSE);
		contratoBean.setSicontraprestacionadicional(Boolean.FALSE);
		contratoBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);
		contratoBean.setSiclausulapagoposterior(Boolean.FALSE);
		contratoBean.setSicuotascanceladas(Boolean.FALSE);
		//LISTA DE CUOTAS PENDIENTES
		listaCuotasPendientesCondicion.clear();
		listaCuotasPendientesCondicionCancelar.clear();
		listaidcontrato.clear();
	}
	
	/**
	 * 
	 * @param rentaBean
	 * Metodos para configurar los periodos referencia de clausula de suspension temporal de renta
	 */
	
	public  void configurarClausulaSuspensionTemporalRenta(RentaBean rentaBean){
		
		listaOrigenSupensionTemporalRenta.clear();
		
		if (rentaBean.getSiclausulasuspensiontemporalrenta()) {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dglConfigurarSuspensionTemporalRenta.show();");
			
		}
	}
	public void buscarUpa(){
		System.out.println("clave="+valorbusquedaUpa);
		if (tipobusquedaUpa==1) {
			listUpa=getUpaService().listarUpasxClave(valorbusquedaUpa);
		} else {
			listUpa=getUpaService().listarUpasxDireccion(valorbusquedaUpa);
		}
	}
	/**
	 * Busqueda de inquilino para vincular con contrato.
	 */
	public void buscarInquilino(){
		listInquilino = getInquilinoService().listarInquilinos(valorbusquedaInquilino, String.valueOf(tipobusquedaInquilino));
		
	}
	public void seleccionarInquilino(SelectEvent event) {
		contratoBean.setIdinquilino(inquilino.getIdinquilino());
		contratoBean.setNombresInquilino(inquilino.getNombrescompletos());
		contratoBean.setRuc(inquilino.getTipopersona().equals("1")?inquilino.getDni():inquilino.getRuc());
	}
	public void validarDevolucionInmueble(){
		
		if (StringUtils.isEmpty(contratoBean.getNumeroactadevolucion())) {
			addWarnMessage("", "Ingrese la número de acta de devolución para continuar");

			
		}else if (contratoBean.getFechaactadevolucion()==null) {
			addWarnMessage("", "Ingrese la fecha de devolución para continuar");

		}else {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgConfirmDevolucionInmueble.show();");
		}
	}
	/**
	 * Validacion para  determinar si la upa esta disponible para vincular.
	 */	
	public void seleccionarUpa(SelectEvent event) {
		if(!upa.getSiprocesojudicial()){/*CONDICION DE CLAVE UPA EN PROCESO JUDICIAL*/
			if (contratoService.verificarExisteCondicionVigente(upa.getIdupa(),estado)) {
				upa = null;
				
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar la upa para reconocimiento de deuda , debido a que se encuentra ocupada en una condición vigente");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}else {
				contratoBean.setIdupa(upa.getIdupa());
				contratoBean.setClaveUpa(upa.getClave());
				contratoBean.setDireccion(upa.getDireccion()+ " "+ upa.getNumprincipal()+" "+upa.getNombrenuminterior());
				//actualizar lista de cuotas deudas
				listaCuotasPendientesCondicion=generarPagosDeudaxUpa();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upa encontrada", "Se seleciono exitosamente la clave upa ");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			}else{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra bloqueada", "La upa seleccionada esta judicializada por lo tanto no se puede realizar ningun tipo de registro");
				FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void ActivarDesactivarSiOcupante() {
		
		if (!contratoBean.getSiocupante()) {
			contratoBean.setSiocupante(false);
		} else {
			contratoBean.setSiocupante(true);
		}
	
	}
	public void actualizacionCuotaPendiente(){
		try{
			System.out.println("ACTUALIZACONCUOTAPENDIENTE");
			System.out.println("cuotaPendienteCondicion.getMonto()="+cuotaPendienteCondicion.getMonto());
			if(cuotaPendienteCondicion.getMonto()<0){
				System.out.println("MONTO  NO VALIDO");
				addWarnMessage("", "monto erroneo");
			}else {
				System.out.println("MONTO VALIDO");
				System.out.println("cuotaPendienteCondicion.getMonto()="+cuotaPendienteCondicion.getMonto());
				calcularTotales();
			}
		}catch(Exception e){
			
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	public void validarPeriodo() {
			
			Integer contador=0;
			if (selectedRenta.getSiclausulaperiodogracia()) {
				contador++;
			}if (selectedRenta.getSiclausulareconocimientoinversion()) {
				contador++;
			}if (selectedRenta.getSiclausulapagoposterior()) {
				contador++;
			}if (selectedRenta.getSiclausulareconocimientorenta()) {
				contador++;
			}
			
			if (contador>1) {
			
				addWarnMessage("","Solo puede seleccionar una clausula por item.");
			
			}else {
				
				/* Clausula de Periodo de gracia */
				if (selectedRenta.getSiclausulaperiodogracia() && !selectedRenta.getSiclausulareconocimientoinversion()) {
					selectedRenta.setMontopagar(0.0);
					
				}else {
					
					selectedRenta.setMontopagar(selectedRenta.getRenta());
				
				}
				
				
				/* Clausula de Reconocimiento de Renta */
				
				if (selectedRenta.getSiclausulareconocimientorenta() && selectedRenta.getDescuentoreconocimientorenta()==0.0) {
					
					selectedRenta.setDescuentoreconocimientorenta(0.0);
					selectedRenta.setObservacionreconocimientorenta("");
					selectedRenta.setSiclausulareconocimientorenta(Boolean.FALSE);
					addWarnMessage("","Debe ingresar el valor");
					
				}else if (selectedRenta.getSiclausulareconocimientorenta() && selectedRenta.getObservacionreconocimientorenta().equals("")) {
					selectedRenta.setDescuentoreconocimientorenta(0.0);
					selectedRenta.setObservacionreconocimientorenta("");
					selectedRenta.setSiclausulareconocimientorenta(Boolean.FALSE);
					addWarnMessage("","Debe ingresar la observación");
				}else if (selectedRenta.getSiclausulareconocimientorenta() && selectedRenta.getObservacionreconocimientorenta().length()<10) {
					selectedRenta.setDescuentoreconocimientorenta(0.0);
					selectedRenta.setObservacionreconocimientorenta("");
					selectedRenta.setSiclausulareconocimientorenta(Boolean.FALSE);
					addWarnMessage("","Observación debe tener al menos 10 caracteres.");
				}else if (!selectedRenta.getSiclausulareconocimientorenta() && selectedRenta.getDescuentoreconocimientorenta()!=0.0) {
					
					selectedRenta.setDescuentoreconocimientorenta(0.0);
					selectedRenta.setObservacionreconocimientorenta("");
					selectedRenta.setSiclausulareconocimientorenta(Boolean.FALSE);
					addWarnMessage("","Usted debe indicar que tiene reconocimiento de renta para ingresar los valores");
					
				}else if (!selectedRenta.getSiclausulareconocimientorenta() && !selectedRenta.getObservacionreconocimientorenta().equals("")) {
					
					selectedRenta.setDescuentoreconocimientorenta(0.0);
					selectedRenta.setObservacionreconocimientorenta("");
					selectedRenta.setSiclausulareconocimientorenta(Boolean.FALSE);
					addWarnMessage("","Usted debe indicar que tiene reconocimiento de renta para ingresar los valores");
					
				}else if (selectedRenta.getSiclausulareconocimientorenta() && selectedRenta.getDescuentoreconocimientorenta()!=0.0 && !selectedRenta.getObservacionreconocimientorenta().equals("")) {
					
					selectedRenta.setDescuentoreconocimientorenta(selectedRenta.getDescuentoreconocimientorenta());
					selectedRenta.setMontopagar(selectedRenta.getRenta()+ConvertirUtil.parseNegativeNumber(selectedRenta.getDescuentoreconocimientorenta()));
				}
				
				
				/* Clausula de Reconocimiento de Inversion */
				
	
				
				
				
				/** Pago de Fraccion **/
				
	//			if (!selectedRenta.getSipagofraccionrenta()) {
	//				selectedRenta.setSipagofraccionrenta(Boolean.FALSE);
	//				selectedRenta.setMontopagofraccionrenta(0.0);
	//				selectedRenta.setObservacionpagofraccionrenta("");
		//
	//			
	//			} else if (selectedRenta.getSipagofraccionrenta() && selectedRenta.getId()!=0) {
	//				selectedRenta.setMontopagofraccionrenta(0.0);
	//				selectedRenta.setObservacionpagofraccionrenta("");
	//				selectedRenta.setSipagofraccionrenta(Boolean.FALSE);
	//				
	//				addWarnMessage("","Solo puede modificar la primera renta. ");
	//				
	//			} else if (selectedRenta.getSipagofraccionrenta() && selectedRenta.getMontopagofraccionrenta()>selectedRenta.getMontopagar()) {
	//				selectedRenta.setMontopagofraccionrenta(0.0);
	//				selectedRenta.setObservacionpagofraccionrenta("");
	//				selectedRenta.setSipagofraccionrenta(Boolean.FALSE);
	//				
	//				addWarnMessage("","Monto de fracción a pagar no puede superar a la contraprestación. ");
	//				
	//			} else 	if (selectedRenta.getSipagofraccionrenta() && selectedRenta.getObservacionpagofraccionrenta().equals("")) {
	//				
	//				selectedRenta.setMontopagofraccionrenta(0.0);
	//				selectedRenta.setObservacionpagofraccionrenta("");
	//				selectedRenta.setSipagofraccionrenta(Boolean.FALSE);
	//				addWarnMessage("","Debe ingresar observación");
	//				
	//			} else 	if (selectedRenta.getSipagofraccionrenta() && selectedRenta.getMontopagofraccionrenta()==0.0) {
	//				
	//				selectedRenta.setMontopagofraccionrenta(0.0);
	//				selectedRenta.setObservacionpagofraccionrenta("");
	//				selectedRenta.setSipagofraccionrenta(Boolean.FALSE);
	//				addWarnMessage("","Debe ingresar el valor de la fracción a pagar");
	//				
	//			}else 	if (selectedRenta.getSipagofraccionrenta() && selectedRenta.getMontopagofraccionrenta()!=0.0 && !selectedRenta.getObservacionpagofraccionrenta().equals("")) {
		//
	//				selectedRenta.setMontopagar(selectedRenta.getMontopagofraccionrenta());
	//			}
				
			}
			
			
	
	}
	public void asignarCuotas(){		
		Double monto=0.0;
		MaeDetCondicionDetalleBean maecondicion;
		List<MaeDetCondicionDetalleBean> listamaecondicion;
		List<CuotaBean> listaCuotasDeuda= new ArrayList<CuotaBean>();
		int i=0,ind=0;
		for(CuotaBean cuo :listaCuotasPendientesCondicionCancelar){
			 CuotaBean c = new CuotaBean();
			 c.setMonto(cuo.getMonto());
			 c.setMes(cuo.getMes());
			 c.setAnio(cuo.getAnio());
			 c.setIdcontrato(cuo.getIdcontrato());
			// c.setIddetallecondicion(cuo.getIddetallecondicion());
			 c.setId_maedetallecondicion(cuo.getId_maedetallecondicion());
			 listaCuotasDeuda.add(c);
		}
//		System.out.println("total");
//		System.out.println("size="+listaCuotasDeuda.size());
		for(RentaBean condicion :listaRentas){
			
			//for(int i=0;i<listaCuotasPendientesCondicionCancelar.size();i++){
				monto=condicion.getContraprestacion();
				maecondicion= new MaeDetCondicionDetalleBean();
				listamaecondicion=new ArrayList<MaeDetCondicionDetalleBean>();
				while(monto>0 && listaCuotasDeuda.size()>i){
				
					if(monto>listaCuotasDeuda.get(i).getMonto()){	
						maecondicion= new MaeDetCondicionDetalleBean();
						maecondicion.setMonto(FuncionesHelper.redondearNum(listaCuotasDeuda.get(i).getMonto(),2));
						maecondicion.setAnio(Integer.toString(listaCuotasDeuda.get(i).getAnio()));
						maecondicion.setMes(listaCuotasDeuda.get(i).getMes());
						maecondicion.setIdconcepto(Constante.CONCEPTO_RECONOCIMIENTO_CANCELA);
						maecondicion.setConcepto(Constante.MAP_CONCEPTO_RECONOCIMIENTO.get(maecondicion.getIdconcepto()));
						maecondicion.setTipomoneda(sisoles==true? Constante.TIPO_MONEDA_SOLES:Constante.TIPO_MONEDA_DOLARES);
						maecondicion.setNumeromes(Integer.parseInt(Almanaque.mesanumero(listaCuotasDeuda.get(i).getMes())));
						maecondicion.setSecuencia(ind+1);
						maecondicion.setEstado(Constante.ESTADO_REG);
						maecondicion.setSianulado(Boolean.FALSE);
						maecondicion.setIdcontrato(listaCuotasDeuda.get(i).getIdcontrato());
						maecondicion.setIddetallecondicion(listaCuotasDeuda.get(i).getId_maedetallecondicion());
						monto=monto-listaCuotasDeuda.get(i).getMonto();
//						System.out.println("listamonto="+maecondicion.getMonto());
//						System.out.println("nuevo monto="+monto);
						
						listamaecondicion.add(maecondicion);
						i++;ind++;
					}else if(monto<listaCuotasDeuda.get(i).getMonto()){
						maecondicion= new MaeDetCondicionDetalleBean();	
						maecondicion.setMonto(FuncionesHelper.redondearNum(monto,2));
					    //maecondicion.setMonto(FuncionesHelper.redondearNum(listaCuotasPendientesCondicionCancelar.get(i).getMonto(),2));
						maecondicion.setAnio(Integer.toString(listaCuotasDeuda.get(i).getAnio()));
						maecondicion.setMes(listaCuotasDeuda.get(i).getMes());
						maecondicion.setIdconcepto(Constante.CONCEPTO_RECONOCIMIENTO_A_CUENTA);
						maecondicion.setConcepto(Constante.MAP_CONCEPTO_RECONOCIMIENTO.get(maecondicion.getIdconcepto()));						
						maecondicion.setTipomoneda(sisoles==true? Constante.TIPO_MONEDA_SOLES:Constante.TIPO_MONEDA_DOLARES);
						maecondicion.setNumeromes(Integer.parseInt(Almanaque.mesanumero(listaCuotasDeuda.get(i).getMes())));
						maecondicion.setSecuencia(ind+1);
						maecondicion.setEstado(Constante.ESTADO_REG);
						maecondicion.setSianulado(Boolean.FALSE);
						maecondicion.setIdcontrato(listaCuotasDeuda.get(i).getIdcontrato());
						maecondicion.setIddetallecondicion(listaCuotasDeuda.get(i).getId_maedetallecondicion());
						
						listaCuotasDeuda.get(i).setMonto(FuncionesHelper.redondearNum((listaCuotasDeuda.get(i).getMonto()-monto),2));
						monto=0.0;
//						System.out.println("listamonto2="+maecondicion.getMonto());
//						System.out.println("nuevo monto2="+monto);
						listamaecondicion.add(maecondicion);
						
						ind++;
						//i++;						
					}else{
						maecondicion= new MaeDetCondicionDetalleBean();
						maecondicion.setMonto(FuncionesHelper.redondearNum((listaCuotasDeuda.get(i).getMonto()),2));
						maecondicion.setMonto(FuncionesHelper.redondearNum(listaCuotasDeuda.get(i).getMonto(),2));
						maecondicion.setAnio(Integer.toString(listaCuotasDeuda.get(i).getAnio()));
						maecondicion.setMes(listaCuotasDeuda.get(i).getMes());
						maecondicion.setIdconcepto(Constante.CONCEPTO_RECONOCIMIENTO_CANCELA);
						maecondicion.setConcepto(Constante.MAP_CONCEPTO_RECONOCIMIENTO.get(maecondicion.getIdconcepto()));
						maecondicion.setTipomoneda(sisoles==true? Constante.TIPO_MONEDA_SOLES:Constante.TIPO_MONEDA_DOLARES);
						maecondicion.setNumeromes(Integer.parseInt(Almanaque.mesanumero(listaCuotasDeuda.get(i).getMes())));
						maecondicion.setSecuencia(ind+1);
						maecondicion.setEstado(Constante.ESTADO_REG);
						maecondicion.setSianulado(Boolean.FALSE);
						maecondicion.setIdcontrato(listaCuotasDeuda.get(i).getIdcontrato());
						maecondicion.setIddetallecondicion(listaCuotasDeuda.get(i).getId_maedetallecondicion());
						monto=FuncionesHelper.redondearNum((monto-listaCuotasDeuda.get(i).getMonto()),2);
//						System.out.println("listamonto3="+maecondicion.getMonto());
//						System.out.println("nuevo monto3="+monto);
						listamaecondicion.add(maecondicion);
						i++;ind++;
					}
					
				}
				condicion.setMaecondiciondetalle(listamaecondicion);
			}
//		for(RentaBean condicion :listaRentas){
//			System.out.println("mes="+condicion.getMes()+", Anio="+condicion.getAnio()+", MOntocuota="+condicion.getMontopagar());
//			for(int j=0;j<condicion.getMaecondiciondetalle().size();j++){
//			System.out.println("cuota="+condicion.getMaecondiciondetalle().get(j).getMonto());
//			}
//		}
		
	}
	public void generarPeriodos() {
		
		
		List<MaeIpc> listaIpc= new ArrayList<MaeIpc>();
		listaIpc=ipcService.listarIpc();
		Calendar iniciocobro = FechaUtil.setTime(Integer.parseInt(anioInicioCobro), Integer.parseInt(Almanaque.mesanumero(mesInicioCobro)),0, 0, 0, 0, 0);
		Calendar fincobro= Calendar.getInstance();
		fincobro.set(Integer.parseInt(anioFinCobro), Integer.parseInt(Almanaque.mesanumero(mesFinCobro))-1, 1, 0, 0, 0);
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		
	
		Calendar fechafinal = GregorianCalendar.getInstance();
		fechafinal.set(fechafinal.get(Calendar.YEAR), 11, 31, 0, 0, 0);
		
		int numanio=1;
		Double indice=1.0;
		int i=0;
		
		
		Boolean flagReajusteAnual=Boolean.FALSE,flagReajusteIpc=Boolean.FALSE;
		Double valorUltimaRenta = 0.0,valorIpc=0.0;
		Boolean siIpcAnioRegistrado=Boolean.FALSE;
		Integer periodosTerminoPrimerAnio=12-iniciocobro.get(Calendar.MONTH);
		Boolean siPrimerAnio=Boolean.FALSE;
		Double contraprestacion;
	
		while (iniciocobro.compareTo(fincobro) < 0){
			
			RentaBean rentaBean= new RentaBean();
			
			rentaBean.setId(listaRentas.size());
			rentaBean.setSecuencia(rentaBean.getId()+1);
			//System.out.println("SECUENCIA="+rentaBean.getSecuencia());
			
			rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));		
			rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
		   // System.out.println("FECHA VENCIMIENTO="+FechaUtil.sumarRestarMeses(contratoBean.getIniciocontrato(),i));
		    rentaBean.setFechavencimiento(FechaUtil.sumarRestarMeses(contratoBean.getIniciocontrato(),i));	
		    //System.out.println("NRO CUOTA = "+String.format("%03d",(i+1)));
		    rentaBean.setNrocuota(String.format("%03d",(i+1)));
		    //rentaBean.setNrocuota(i+1);
			rentaBean.setSiclausulaperiodogracia(Boolean.FALSE);
			rentaBean.setSiclausulareconocimientoinversion(Boolean.FALSE);
			rentaBean.setSiclausulapagoposterior(Boolean.FALSE);
			rentaBean.setSiclausulareconocimientorenta(Boolean.FALSE);
			rentaBean.setSipagofraccionrenta(Boolean.FALSE);
			rentaBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);			
			rentaBean.setSirentagenerada(Boolean.TRUE);
			
			
			
			
			if (contratoBean.getSicontraprestacionadicional() && (contratoBean.getAniocontraprestacionadicional().intValue()-1)*12<i+1) {
				
				contraprestacion=contratoBean.getContraprestacionadicional();
				
				if ((contratoBean.getAniocontraprestacionadicional().intValue()-1)*12==i) {
					numanio=1;
					indice=1.0;
					flagReajusteAnual=Boolean.FALSE;flagReajusteIpc=Boolean.FALSE;
					valorUltimaRenta = 0.0;valorIpc=0.0;
					siIpcAnioRegistrado=Boolean.FALSE;
					periodosTerminoPrimerAnio=12-iniciocobro.get(Calendar.MONTH)+i;
					siPrimerAnio=Boolean.FALSE;
				}
				
			}else {
				contraprestacion=contratoBean.getContraprestacion();
			}
				
				
				rentaBean.setContraprestacion(contraprestacion);	
				/*SIN INDICE DE INCREMENTO*/
				rentaBean.setRenta(FuncionesHelper.redondear(contraprestacion, 2));
				rentaBean.setRentaTemp(rentaBean.getRenta());
				rentaBean.setMontopagar(rentaBean.getRenta());
				//rentaBean.setSaldoPendiente(FuncionesHelper.redondearNum((contratoBean.getMontodeuda()-sumaCuota),2));
				
				
				/*INICIO DE REAJUSTE ANUAL*/
//				if (contratoBean.getTiporeajusteanual().equals("0")) {
//					if (i%12==0) {
//						
//						indice= Math.pow((contratoBean.getReajusteanual()+100)/100, numanio-1);
//						numanio++;
//					
//					}
//	
//					rentaBean.setRenta(FuncionesHelper.redondear(contraprestacion*indice, 2));
//					rentaBean.setRentaTemp(rentaBean.getRenta());
//					rentaBean.setMontopagar(rentaBean.getRenta());
//				
//				}else if (contratoBean.getTiporeajusteanual().equals("1")) {
//					
//					//if (iniciocobro.compareTo(fechafinal)<0) {
//					if (iniciocobro.compareTo(fechafinal)<0) {
//						
//						
//						/** Detector primer año**/
//						if (i<periodosTerminoPrimerAnio) { 
//							rentaBean.setRenta(contraprestacion);
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(rentaBean.getRenta());
//							siPrimerAnio=Boolean.TRUE;
//							
//						}else if (iniciocobro.get(Calendar.MONTH)==0) {
//							
//							valorUltimaRenta=listaRentas.get(i-1).getRenta();
//							siPrimerAnio=Boolean.FALSE;
//							
//							for (MaeIpc maeIpc : listaIpc) {
//								if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
//									valorIpc=maeIpc.getValor();
//									break;
//								}
//							}
//						}
//	
//						if (!siPrimerAnio) {
//							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(rentaBean.getRenta());
//							
//						}
//						
//					}else {
//						rentaBean.setRenta(0.0);
//						rentaBean.setRentaTemp(rentaBean.getRenta());
//						rentaBean.setMontopagar(0.0);
//						rentaBean.setSirentagenerada(Boolean.FALSE);
//					}
//	
//				}else if (contratoBean.getTiporeajusteanual().equals("2")) {
//					
//					
//					if (iniciocobro.compareTo(fechafinal)<0) {
//						
//						
//						/** Detector primer año**/
				
//						if (i<periodosTerminoPrimerAnio) {
//							rentaBean.setRenta(contraprestacion);
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(rentaBean.getRenta());
//							siPrimerAnio=Boolean.TRUE;
//							
//						}else if (iniciocobro.get(Calendar.MONTH)==0) {
//							
//							siPrimerAnio=Boolean.FALSE;
//							
//								for (MaeIpc maeIpc : listaIpc) {
//									siIpcAnioRegistrado=Boolean.FALSE;
//	//								if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
//									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
//										siIpcAnioRegistrado=Boolean.TRUE;
//										break;
//									}
//								}
//							
//							valorUltimaRenta=listaRentas.get(i-1).getRenta();
//							
//							if (siIpcAnioRegistrado) {
//								
//								for (MaeIpc maeIpc : listaIpc) {
//									
//									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()>contratoBean.getReajusteanual()) {
//										flagReajusteIpc=Boolean.TRUE;
//										flagReajusteAnual=Boolean.FALSE;
//										valorIpc=maeIpc.getValor();
//										
//										break;
//							
//									} else if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()<=contratoBean.getReajusteanual()) {
//										flagReajusteAnual=Boolean.TRUE;
//										flagReajusteIpc=Boolean.FALSE;
//										
//										break;
//							
//									}
//								
//								}
//								
//							} 
//						}
//						
//						if (flagReajusteAnual) {
//							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+contratoBean.getReajusteanual())/100, 2));
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(rentaBean.getRenta());
//						
//						}
//						if (flagReajusteIpc) {
//							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(rentaBean.getRenta());
//						} 
//						if (!siIpcAnioRegistrado && !siPrimerAnio) {
//							rentaBean.setRenta(0.0);
//							rentaBean.setRentaTemp(rentaBean.getRenta());
//							rentaBean.setMontopagar(0.0);
//							rentaBean.setSirentagenerada(Boolean.FALSE);
//						}	
//						
//					}else {
//						rentaBean.setRenta(0.0);
//						rentaBean.setRentaTemp(rentaBean.getRenta());
//						rentaBean.setMontopagar(0.0);
//						rentaBean.setSirentagenerada(Boolean.FALSE);
//					}
//				}
			/* FIN DE REAJUSTE ANUAL*/
	//		}
			
			 if ( contratoBean.getSimontoinicialdeuda() && i==0){
				 
				 rentaBean.setSecuencia(rentaBean.getId()+1);
				 //rentaBean.setMes("Inicial");
				 rentaBean.setContraprestacion(FuncionesHelper.redondear(contratoBean.getMontoinicialdeuda(),2));
				 rentaBean.setRenta(FuncionesHelper.redondear(contratoBean.getMontoinicialdeuda(), 2));
				 rentaBean.setRentaTemp(rentaBean.getRenta());
				 rentaBean.setMontopagar(rentaBean.getRenta());
				 
				 iniciocobro.add(Calendar.MONTH, 1); 
			 }else{
				 iniciocobro.add(Calendar.MONTH, 1); 
			 }
			i++;			
			listaRentas.add(rentaBean);
		}
		Integer ind=numAniosContrato*12+ numMesesContrato;
		if(contratoBean.getSimontoinicialdeuda()){
			   if(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)<FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(ind-1)+contratoBean.getMontoinicialdeuda(),2)){
				   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getMontoinicialdeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(i-2),2));
				   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(listaRentas.get(ind-1).getContraprestacion(), 2));
			   }else if(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)>FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(ind-1)+contratoBean.getMontoinicialdeuda(),2)){
				   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getMontoinicialdeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(i-2),2));
				   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(listaRentas.get(ind-1).getContraprestacion(), 2));
			   }
			}else{
				if(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)<FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(ind-1),2)){
					 listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(i-1),2));
					 listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(listaRentas.get(ind-1).getContraprestacion(), 2));
				}else if(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)>FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(ind-1),2)){
					   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(contratoBean.getMontodeuda(),2)-FuncionesHelper.redondearNum(contratoBean.getContraprestacion()*(i-1),2));
					   listaRentas.get(ind-1).setContraprestacion(FuncionesHelper.redondearNum(listaRentas.get(ind-1).getContraprestacion(), 2));
				}
			}
		listaRentas.get(ind-1).setRenta(FuncionesHelper.redondear( listaRentas.get(ind-1).getContraprestacion(), 2));
		listaRentas.get(ind-1).setRentaTemp(listaRentas.get(ind-1).getRenta());
		listaRentas.get(ind-1).setMontopagar(listaRentas.get(ind-1).getRenta());
//		for(RentaBean renta: listaRentas){
//			System.out.println("LISTA RENTABEAN");
//			System.out.println("rentagetid="+renta.getId());
//			System.out.println("renta secuencia ="+renta.getSecuencia());
//		}
		asignarCuotas();
	}
	public void validarGenerarPeriodos(){
		
		listaRentas.clear();
		selectedRenta=null;
		
//		if (numAniosContrato==0 && numMesesContrato==0) {
//			
//			addWarnMessage("", "Ingrese n\u00famero de años y/o meses del reconocimiento de deuda.");
//			
//		}else if (contratoBean.getIniciocontrato()==null) {
//			
//			addWarnMessage("","Seleccionar inicio de reconocimiento de deuda para continuar.");
//		} else if (contratoBean.getFincontrato()==null) {
//			
//			addWarnMessage("","Seleccionar fin de reconocimiento de deuda para continuar.");
//		} 
	//	else if (validarIntervaloFechaContrato()) {
	//		
	//		addWarnMessage("","Seleccionar intervalo de fecha de contrato válido para continuar.");
	//	} 
		//else 
			if (anioInicioCobro.equals("") || mesInicioCobro.equals("") ) {
			
			addWarnMessage("","Ingrese inicio de cobro para continuar.");
		} else if (validarIntervaloFechaCobro()) {
			
			addWarnMessage("","Seleccionar fecha de inicio de cobro válido para continuar.");
		} else if (contratoBean.getContraprestacion()==0.0) {
		
			addWarnMessage("","Ingresar valor de la cuota mensual correspondiente.");
		
		} else if (contratoBean.getSicontraprestacionadicional() && contratoBean.getContraprestacionadicional()==0.0) {
		
			addWarnMessage("","Ingresar valor de contraprestación  adicional correspondiente.");
		
		} else if (contratoBean.getSicontraprestacionadicional() && contratoBean.getAniocontraprestacionadicional()==0) {
		
			addWarnMessage("","Ingresar n\u00famero de año desde que aplica contraprestación adicional.");
		
//		} else if (contratoBean.getTiporeajusteanual().equals("")) {
//		
//			addWarnMessage("","Seleccionar tipo de reajuste anual.");
//		
//		}else if ((contratoBean.getTiporeajusteanual().equals("0") || contratoBean.getTiporeajusteanual().equals("2")) && contratoBean.getReajusteanual()==0.0) {
//		
//			addWarnMessage("","Ingrese valor  reajuste anual.");
//		
//		}else if ((contratoBean.getTiporeajusteanual().equals("0") || contratoBean.getTiporeajusteanual().equals("2")) && contratoBean.getReajusteanual()==0.0) {
//		
//			addWarnMessage("","Ingrese valor  reajuste anual.");
		}else if (validarMontoDeuda()) {
			
			addWarnMessage("",mensaje);
		}else {
			/* VERIFICAR  QUE LOS TOTALES CUMPLEN**/
			//validarMontoDeuda();
			generarPeriodos();
		}
		
		
	}
	public Boolean validarMontoDeuda(){
		//monto cuota es menor a monto deuda
		Boolean respuesta=false;
		if(contratoBean.getMontodeuda()<=0){
			mensaje="Ingrese un monto valido para la deuda ";
			respuesta=true;
		}else if(contratoBean.getContraprestacion()<=0){
			mensaje="Ingrese un monto de la cuota valido para la cuota mensual ";
			respuesta=true;
		}else if(contratoBean.getMontodeuda()<contratoBean.getContraprestacion()){
			mensaje="El monto de la cuota es mayor a la deuda ";
			respuesta=true;
		}else if(contratoBean.getSimontoinicialdeuda()){//monto inicial es menor a monto deuda
			if(contratoBean.getMontoinicialdeuda()>contratoBean.getMontodeuda()){
				mensaje="El monto de la cuota inicial es mayor a la deuda ";
				respuesta=true;
			}else if(contratoBean.getMontoinicialdeuda()<=0){
				mensaje="Ingrese un monto valido para el monto inicial ";
				respuesta=true;
			}else if(contratoBean.getMontodeuda()<(contratoBean.getContraprestacion()*(numAniosContrato*12+ numMesesContrato -2)+contratoBean.getMontoinicialdeuda())){
				mensaje="Ingrese un monto valido para el monto inicial ";
				respuesta=true;
			}else{
				respuesta=false;
			}
		}else if(contratoBean.getMontodeuda()<(contratoBean.getContraprestacion()*(numAniosContrato*12+ numMesesContrato))){
			mensaje="Ingrese un monto de cuota mensual valido ";
			respuesta=true;
		}
		
		
		return respuesta;
	}
	public void buscarContrato(){
		if (tipobusqueda==1) {
			listContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,estado);
			listaContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,"");
			//System.out.println("Idcontrato ="+listContrato.get(0).getIdcontrato());
			if(listaContrato!=null){
				for(int i=0;i<listaContrato.size();i++){
					if(listaContrato.get(i).getEstado().equals("VIGENTE"))
					{System.out.println("tipo de cONDICION ="+listaContrato.get(i).getCondicion());

					//FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Usuario o Contraseña incorrecto!!!"); 
					FacesMessage msg =new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						       "Title", 
						       "<html>List:<ul><li> List item 1;</li><li> List item 2;</li><li> List item 3.</li></ul></html>") ;
					FacesContext.getCurrentInstance().addMessage(null, msg);
					
					}
				}
						
			}
			
		} else if (tipobusqueda==2) {
			listContrato=contratoService.buscarConSinContratoxDireccion(valorbusqueda,estado);
		}else  {
			listContrato=contratoService.buscarConSinContratoxNombreInquilino(valorbusqueda,estado);
		}
		listaAdenda=contratoService.buscarAdenda(valorbusqueda,estado,tipobusqueda);
		if(listaAdenda.size()>0){			
			setearlistacontratoconadenda();
		}
	}
	/**
	 * Busqueda de upa para vincular con contrato.
	 */	
	public void setearlistacontratoconadenda(){
		for(int i=0;i<listContrato.size();i++){
			for(int j=0;j<listaAdenda.size();j++){
				if(Integer.toString(listContrato.get(i).getIdcontrato()).equals(Integer.toString(listaAdenda.get(j).getIdcontrato()))&& listContrato.get(i).getSiadenda() && !listaAdenda.get(j).getSifinalizado()){					
					listContrato.get(i).setNroadenda(listaAdenda.get(j).getNroadenda());
					nroadenda_ant=listaAdenda.get(j).getNroadenda();
				}
			}
		}
	}
	/**
	 * Carga la informacion de contrato al seleccionarlo.
	 */	
	public void setearContratoSeleccionado() {
		try{
		listaRentas.clear();
//		listacuotas=contratoService.obtenerCuotasDeContrato(contratoBean.getIdcontrato());
		
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(contratoBean.getIniciocobro());
		mesInicioCobro=Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH));
		anioInicioCobro=String.valueOf(iniciocobro.get(Calendar.YEAR));
		
		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(contratoBean.getFincobro()!=null?contratoBean.getFincobro():new Date());
		
		mesFinCobro=Almanaque.obtenerNombreMes(fincobro.get(Calendar.MONTH));
		anioFinCobro=String.valueOf(fincobro.get(Calendar.YEAR));
		
		
		if (contratoBean.getMoneda() != null) {
			if (FuncionesHelper.eliminarEspacios(contratoBean.getMoneda()).equals("S")) {
				sisoles = true;
				sidolar = false;
			} else {
				
				sisoles = false;
				sidolar = true;
			}
		}
		int nrocoutas=contratoBean.getNumerocuotas();
		numAniosContrato=nrocoutas/12;
		numMesesContrato=nrocoutas%12;
		activarTipoAlquiler();
		
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
		
		
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	public void activarTipoAlquiler(){
		
		if (contratoBean.getTipocontrato()!=null && contratoBean.getTipocontrato().equals(Constantes.CONTRATO_TIPO_ALQUILER)) {
			sitipocontratoalquiler=Boolean.TRUE;
		} else {
			sitipocontratoalquiler=Boolean.FALSE;
		}
		
	}
	public Boolean validarIntervaloFechaCobro(){
	
		Calendar iniciocontrato=Calendar.getInstance();
		iniciocontrato.setTime(contratoBean.getIniciocontrato());
		
		Calendar iniciocobro=Calendar.getInstance();
	
		iniciocobro.set(Calendar.YEAR,Integer.parseInt(anioInicioCobro));
		iniciocobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesInicioCobro))-1);
		iniciocobro.set(Calendar.DATE, 1);
	
		
		if (iniciocontrato.get(Calendar.MONTH)==iniciocobro.get(Calendar.MONTH) && iniciocontrato.get(Calendar.YEAR)==iniciocobro.get(Calendar.YEAR)) {
			return false;
		}else if (iniciocontrato.get(Calendar.MONTH)==(iniciocobro.get(Calendar.MONTH)+1) && iniciocontrato.get(Calendar.YEAR)==iniciocobro.get(Calendar.YEAR)) {
			return false;
		}else if (iniciocontrato.get(Calendar.MONTH)==(iniciocobro.get(Calendar.MONTH)-1) && iniciocontrato.get(Calendar.YEAR)==iniciocobro.get(Calendar.YEAR)) {
			return false;
		}else if (iniciocontrato.get(Calendar.MONTH)==0 && iniciocobro.get(Calendar.MONTH)==11 && iniciocontrato.get(Calendar.YEAR)==(iniciocobro.get(Calendar.YEAR)+1)) {
			return false;
		}else if (iniciocontrato.get(Calendar.MONTH)==11 && iniciocobro.get(Calendar.MONTH)==0 && (iniciocontrato.get(Calendar.YEAR)+1)==iniciocobro.get(Calendar.YEAR)) {
			return false;
		}else{
			return true;
		}
	}
	public void calcularFinActa(){
		
	}
	public void calcularFinContrato() {
		
		/** Calculo fin contrato**/
			Calendar iniciocontrato=Calendar.getInstance();
			iniciocontrato.setTime(contratoBean.getIniciocontrato());
			
			iniciocontrato.add(Calendar.MONTH, numAniosContrato*12+ numMesesContrato-1);
			iniciocontrato.add(Calendar.DAY_OF_YEAR, 0);
			contratoBean.setFincontrato(iniciocontrato.getTime());
			
			iniciocontrato.setTime(contratoBean.getIniciocontrato());
			
		/** Calculo inicio cobro**/	
			Calendar iniciocobro=Calendar.getInstance();
			iniciocobro.set(iniciocontrato.get(Calendar.YEAR), iniciocontrato.get(Calendar.MONTH), 1, 0, 0, 0);
			iniciocobro.set(Calendar.MILLISECOND,0);
			
			mesInicioCobro=Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH));
			anioInicioCobro=String.valueOf(iniciocobro.get(Calendar.YEAR));
			
			calcularFinCobro();
	}
	public void calcularFinCobro() {
		if (!mesInicioCobro.equals("") && !anioInicioCobro.equals("")) {
			
			Calendar inicio=Calendar.getInstance();
			inicio.set(Integer.parseInt(anioInicioCobro), Integer.parseInt(Almanaque.mesanumero(mesInicioCobro))-1, 1, 0, 0, 0);
			inicio.set(Calendar.MILLISECOND,0);
			
			inicio.add(Calendar.MONTH, numAniosContrato*12+ numMesesContrato);
			inicio.add(Calendar.DAY_OF_YEAR, -1);
			disabledFinCobro=false;
			
			mesFinCobro=Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH));
			anioFinCobro=String.valueOf(inicio.get(Calendar.YEAR));
			
		}else {
			disabledFinCobro=true;
		}
	}
	public void calcularFinCobroxReconocimiento() {
		if (!mesInicioCobro.equals("") && !anioInicioCobro.equals("")) {
			
			Calendar inicio=Calendar.getInstance();
			inicio.set(Integer.parseInt(anioInicioCobro), Integer.parseInt(Almanaque.mesanumero(mesInicioCobro))-1, 1, 0, 0, 0);
			inicio.set(Calendar.MILLISECOND,0);
			
			inicio.add(Calendar.MONTH,contratoBean.getNumerocuotas());
			inicio.add(Calendar.DAY_OF_YEAR, -1);
			disabledFinCobro=false;
			
			mesFinCobro=Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH));
			anioFinCobro=String.valueOf(inicio.get(Calendar.YEAR));
			
		}else {
			disabledFinCobro=true;
		}
	}
	public void limpiarFinContrato(){
		contratoBean.setIniciocontrato(null);
		contratoBean.setFincontrato(null);
	}
	public void asignarMesAnio(){
		numAniosContrato=contratoBean.getNumerocuotas()/12;
		numMesesContrato=contratoBean.getNumerocuotas()%12;
		limpiarFinContrato();
		//calcularFinContrato();
	}
	public void cambiarDolarSoles() {
		if (sidolar) {
			sisoles = true;
			sidolar = false;
		} else {
			sidolar = false;
		}
	}
	public void cambiarSolesDolar() {
		if (sisoles) {
			sidolar = true;
			sisoles = false;
		} else {
			sisoles = true;
			sidolar = false;
		}
	}
	public void validarCabeceraCuotasPendientes(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();

		try{
			
			if(contratoBean.getFechadesde_rec()==null){
				addWarnMessage("", "Seleccione Fecha inicio que reconoce para continuar..");
			}else if(contratoBean.getFechahasta_rec()==null){
				addWarnMessage("", "Seleccione Fecha fin que reconoce para continuar..");
			}else if (contratoBean.getNromes_rec()<=0){
				addWarnMessage("", "Ingrese Cantidad de meses que reconoce para continuar..");
			}else {
				contextRequest.execute("dlgPendientePagoRenta.show();");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public List<CuotaBean> generarPagosDeudaxUpa(){
		List<CondicionBean> listaContratoBean=new ArrayList<CondicionBean>();
		List<CuotaBean> lista= new ArrayList<CuotaBean>();
		List<CuotaBean> listaPendientes= new ArrayList<CuotaBean>();
		listaContratoBean = contratoService.obtenerCondicionDeudaxUpa("Global","GLOBAL","todos","",contratoBean.getClaveUpa());	
		if(listaContratoBean.size()>0){
			
		for (CondicionBean condicionSeleccionado:listaContratoBean){
			   lista=null;
			   if (condicionSeleccionado.getSinuevomantenimiento() ) {
					  //lista=reporteRecaudacionService.obtenerPeriodosPendientesDeuda(condicionSeleccionado.getIdcontrato());
					  //lista=recaudacionReporteService.generarRentasPendientesParaCobroNuevoContrato(condicionSeleccionado.getIdcontrato(), tipcambio);
					  lista=recaudacionReporteService.generarRentasPendientesNuevoContrato(condicionSeleccionado.getIdcontrato(), tipcambio);
			   }else{
					  lista=reporteRecaudacionService.obtenerInformacionCobroCondicionTipoxDeuda(condicionSeleccionado.getIdcontrato(), condicionSeleccionado.getCondicion(), "pendientes", tipcambio);	
			   }			   			   
			   listaPendientes.addAll(lista);
		}
		}else{
			
			lista=null;
			listaPendientes.clear();
		}
		if(listaPendientes!=null){
		if(listaPendientes.size()>0){
			   int i=0;
			   for(CuotaBean cuo:listaPendientes){
//				   System.out.println("cuo.getNrocuota()="+cuo.getNrocuota());
				   cuo.setNrocuota(i);
				   System.out.println("cuo idcontrato = "+cuo.getIdcontrato());
				   if(cuo.getId_maedetallecondicion() !=null){
					   System.out.println("cuo iddetallecondicion = "+cuo.getId_maedetallecondicion());  
				   }
				   
				   i++;
			   }
		   }}
	   return listaPendientes;
	}
	public void validarListaCuotas(){
		Collections.sort(listaCuotasPendientesCondicionCancelar, new Comparator<CuotaBean>(){
			
			@Override
			public  int  compare(CuotaBean cuo1,CuotaBean cuo2){
				return cuo1.getNrocuota().compareTo(cuo2.getNrocuota());
			}
		});

	}
	public void generarcuotasxupa(){
		lista=generarPagosDeudaxUpa();
		listaCuotasPendientesCondicion=generarPagosDeudaxUpa();
	}
	public void calcularTotales() {
		//DecimalFormat formatter = new DecimalFormat("#.##");
		BigDecimal totalsoles=new BigDecimal("0.0");
		BigDecimal suma = new BigDecimal("0.0");
		BigDecimal sumaN = new BigDecimal("0.0");
//		totalSubTotalComprobante=new BigDecimal("0.0");
//		igvComprobante=new BigDecimal("0.0");
		totaldeudasoles=new BigDecimal("0.0");
		suma.setScale (2,RoundingMode.HALF_UP); // Lanza ArithmeticException
		sumaN.setScale (2,RoundingMode.HALF_UP);
		//totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
		//totalSubTotalComprobanteN.setScale(2,RoundingMode.HALF_UP);
		totaldeudasoles.setScale(2,RoundingMode.HALF_UP);
		totalsoles.setScale(2,RoundingMode.HALF_UP);
//		igvComprobante.setScale(2,RoundingMode.HALF_UP);
//		igvComprobanteN.setScale(2,RoundingMode.HALF_UP);
		//montoDetraccion.setScale(2,RoundingMode.HALF_UP);
		
		for (CuotaBean c : listaCuotasPendientesCondicionCancelar) {
//			System.out.println("Cuota = "+ c.getMonto());
			suma=new BigDecimal(c.getMonto());			
			totalsoles = suma;
			totalsoles.setScale(2,RoundingMode.HALF_UP);
			totaldeudasoles = totaldeudasoles.add(totalsoles);
           			
		}
		contratoBean.setTotalsoles_rec(FuncionesHelper.redondearNum(Double.parseDouble(totaldeudasoles.toString()),2));
		contratoBean.setMontodeuda(FuncionesHelper.redondearNum(Double.parseDouble(totaldeudasoles.toString()),2));
		
		
	}
	public void validarNroActa(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		try{	
			if (contratoBean.getNroacta().length() >0) {
				     contextRequest.execute("iddlgListaUpaRD.show();");
					
			} else {
				addWarnMessage("","Ingrese el n\u00famero de acta de reconocimiento de deuda para continuar.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void validarRegistroUpa(){
		RequestContext contextRequest= RequestContext.getCurrentInstance();
		try{
			
			if (contratoBean.getNroacta().length() == 0) {
				addWarnMessage("",
						"Ingrese el n\u00famero de acta de reconocimiento de deuda para continuar.");
			} else {
				if (contratoBean.getClaveUpa() != null) {
					if(contratoBean.getUsoespecifico().length()==0){
						addWarnMessage("",
								"Ingrese la Uso Especifico de la clave UPA para continuar.");
					}else {
					contextRequest.execute("iddlgListaInquilinoRD.show();");}
				} else  {
					addWarnMessage("",
							"Ingrese la clave UPA del acta de reconocimiento de deuda para continuar.");
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void validarRegistroInmueble(){
		try{
			if(contratoBean.getIdinquilino()!=0){
				if(contratoBean.getFechadesde_rec()==null){
					contratoBean.setFechahasta_rec(null);
					addWarnMessage("",
							"Ingrese la Fecha Inicio del acta de reconocimiento de deuda para continuar.");
					
				}else{
					
				}
			}else{
				
				   
				  if(contratoBean.getFechadesde_rec()==null || contratoBean.getFechahasta_rec()==null){
					  if (contratoBean.getNroacta().length() ==0){
						  contratoBean.setFechadesde_rec(null);
							contratoBean.setFechahasta_rec(null);
						  addWarnMessage("","Ingrese el n\u00famero de acta de reconocimiento de deuda para continuar.");
					  }else if(contratoBean.getClaveUpa() == null){
						  contratoBean.setFechadesde_rec(null);
							contratoBean.setFechahasta_rec(null);
						  addWarnMessage("",
									"Ingrese la clave UPA del acta de reconocimiento de deuda para continuar.");
					  }else if(contratoBean.getIdinquilino()==0){
						  contratoBean.setFechadesde_rec(null);
							contratoBean.setFechahasta_rec(null);
						  addWarnMessage("",
									"Ingrese la Nombre de Inquilino del acta de reconocimiento de deuda para continuar.");
				     }
					  
					
				  }
				
				
			}
			
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	public void asignarObservacion(){
		if(contratoBean.getObservacion().length()>0){
			System.out.println("Observacion="+contratoBean.getObservacion());
			obser=true;
		}
		
	}
	public void asignarCondicionContrato(Contrato contrato,String nombreusuario){
		  List<Integer> list = new ArrayList<Integer>();

		    Map<Integer ,Integer> map = new HashMap<>();

		      for(  Integer r  : listaidcontrato) {
		          if(  map.containsKey(r)   ) {
		                 map.put(r, map.get(r) + 1);
		          }//if
		          else {
		              map.put(r, 1);
		          }
		      }//for

		      //iterate

		      Set< Map.Entry<Integer ,Integer> > entrySet = map.entrySet();
		      for(    Map.Entry<Integer ,Integer>  entry : entrySet     ) {
		          System.out.printf(   "%s : %d %n "    , entry.getKey(),entry.getValue()  );
		          contratoService.asignarCondicion(contrato,entry.getKey(),nombreusuario);
		      }//for

		}

	public CondicionBean getContratoBean() {
		return contratoBean;
	}
	public void setContratoBean(CondicionBean contratoBean) {
		this.contratoBean = contratoBean;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<RentaBean> getListaRentas() {
		return listaRentas;
	}
	public void setListaRentas(List<RentaBean> listaRentas) {
		this.listaRentas = listaRentas;
	}
	public Boolean getSicontratofuturo() {
		return sicontratofuturo;
	}
	public void setSicontratofuturo(Boolean sicontratofuturo) {
		this.sicontratofuturo = sicontratofuturo;
	}
	public IUpaService getUpaService() {
		return upaService;
	}
	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public int getTipobusqueda() {
		return tipobusqueda;
	}
	public void setTipobusqueda(int tipobusqueda) {
		this.tipobusqueda = tipobusqueda;
	}
	public int getTipobusquedaUpa() {
		return tipobusquedaUpa;
	}
	public void setTipobusquedaUpa(int tipobusquedaUpa) {
		this.tipobusquedaUpa = tipobusquedaUpa;
	}
	public String getTipobusquedaInquilino() {
		return tipobusquedaInquilino;
	}
	public void setTipobusquedaInquilino(String tipobusquedaInquilino) {
		this.tipobusquedaInquilino = tipobusquedaInquilino;
	}
	public String getValorbusqueda() {
		return valorbusqueda;
	}
	public void setValorbusqueda(String valorbusqueda) {
		this.valorbusqueda = valorbusqueda;
	}
	public String getValorbusquedaUpa() {
		return valorbusquedaUpa;
	}
	public void setValorbusquedaUpa(String valorbusquedaUpa) {
		this.valorbusquedaUpa = valorbusquedaUpa;
	}
	public String getValorbusquedaInquilino() {
		return valorbusquedaInquilino;
	}
	public void setValorbusquedaInquilino(String valorbusquedaInquilino) {
		this.valorbusquedaInquilino = valorbusquedaInquilino;
	}
	public String getActiveIndexTabView() {
		return activeIndexTabView;
	}
	public void setActiveIndexTabView(String activeIndexTabView) {
		this.activeIndexTabView = activeIndexTabView;
	}
	public List<Upa> getListUpa() {
		return listUpa;
	}
	public void setListUpa(List<Upa> listUpa) {
		this.listUpa = listUpa;
	}
	public List<Upa> getListUpaFiltro() {
		return listUpaFiltro;
	}
	public void setListUpaFiltro(List<Upa> listUpaFiltro) {
		this.listUpaFiltro = listUpaFiltro;
	}
	public List<Inquilino> getListInquilino() {
		return listInquilino;
	}
	public void setListInquilino(List<Inquilino> listInquilino) {
		this.listInquilino = listInquilino;
	}
	public List<Inquilino> getListInquilinoFiltro() {
		return listInquilinoFiltro;
	}
	public void setListInquilinoFiltro(List<Inquilino> listInquilinoFiltro) {
		this.listInquilinoFiltro = listInquilinoFiltro;
	}
	public IInquilinoService getInquilinoService() {
		return inquilinoService;
	}
	public void setInquilinoService(IInquilinoService inquilinoService) {
		this.inquilinoService = inquilinoService;
	}
	public Inquilino getInquilino() {
		return inquilino;
	}
	public void setInquilino(Inquilino inquilino) {
		this.inquilino = inquilino;
	}
	public Upa getUpa() {
		return upa;
	}
	public void setUpa(Upa upa) {
		this.upa = upa;
	}
	public boolean isSidolar() {
		return sidolar;
	}
	public void setSidolar(boolean sidolar) {
		this.sidolar = sidolar;
	}
	public boolean isSisoles() {
		return sisoles;
	}
	public void setSisoles(boolean sisoles) {
		this.sisoles = sisoles;
	}
	public List<Usuario> getTodosUsuarios() {
		return todosUsuarios;
	}
	public void setTodosUsuarios(List<Usuario> todosUsuarios) {
		this.todosUsuarios = todosUsuarios;
	}
	public List<Usuario> getListadousuariosSeleccionados() {
		return listadousuariosSeleccionados;
	}
	public void setListadousuariosSeleccionados(
			List<Usuario> listadousuariosSeleccionados) {
		this.listadousuariosSeleccionados = listadousuariosSeleccionados;
	}
	public List<Cuentabancaria> getListCtasBancarias() {
		return listCtasBancarias;
	}
	public void setListCtasBancarias(List<Cuentabancaria> listCtasBancarias) {
		this.listCtasBancarias = listCtasBancarias;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Representante getRepresentanteaval() {
		return representanteaval;
	}
	public void setRepresentanteaval(Representante representanteaval) {
		this.representanteaval = representanteaval;
	}
	public IContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public Integer getNumAniosContrato() {
		return numAniosContrato;
	}
	public void setNumAniosContrato(Integer numAniosContrato) {
		this.numAniosContrato = numAniosContrato;
	}
	public Integer getNumMesesContrato() {
		return numMesesContrato;
	}
	public void setNumMesesContrato(Integer numMesesContrato) {
		this.numMesesContrato = numMesesContrato;
	}
	public String getMesInicioCobro() {
		return mesInicioCobro;
	}
	public void setMesInicioCobro(String mesInicioCobro) {
		this.mesInicioCobro = mesInicioCobro;
	}
	public String getMesFinCobro() {
		return mesFinCobro;
	}
	public void setMesFinCobro(String mesFinCobro) {
		this.mesFinCobro = mesFinCobro;
	}
	public String getAnioInicioCobro() {
		return anioInicioCobro;
	}
	public void setAnioInicioCobro(String anioInicioCobro) {
		this.anioInicioCobro = anioInicioCobro;
	}
	public String getAnioFinCobro() {
		return anioFinCobro;
	}
	public void setAnioFinCobro(String anioFinCobro) {
		this.anioFinCobro = anioFinCobro;
	}
	public String getMesFinCobroResuelto() {
		return mesFinCobroResuelto;
	}
	public void setMesFinCobroResuelto(String mesFinCobroResuelto) {
		this.mesFinCobroResuelto = mesFinCobroResuelto;
	}
	public String getAnioFinCobroResuelto() {
		return anioFinCobroResuelto;
	}
	public void setAnioFinCobroResuelto(String anioFinCobroResuelto) {
		this.anioFinCobroResuelto = anioFinCobroResuelto;
	}
	public String getMesFinCobroAdenda() {
		return mesFinCobroAdenda;
	}
	public void setMesFinCobroAdenda(String mesFinCobroAdenda) {
		this.mesFinCobroAdenda = mesFinCobroAdenda;
	}
	public String getAnioFinCobroAdenda() {
		return anioFinCobroAdenda;
	}
	public void setAnioFinCobroAdenda(String anioFinCobroAdenda) {
		this.anioFinCobroAdenda = anioFinCobroAdenda;
	}
	public Boolean getDisabledFinCobro() {
		return disabledFinCobro;
	}
	public void setDisabledFinCobro(Boolean disabledFinCobro) {
		this.disabledFinCobro = disabledFinCobro;
	}
	public List<CondicionBean> getListContrato() {
		return listContrato;
	}
	public void setListContrato(List<CondicionBean> listContrato) {
		this.listContrato = listContrato;
	}
	public List<CondicionBean> getListContratoFiltro() {
		return listContratoFiltro;
	}
	public void setListContratoFiltro(List<CondicionBean> listContratoFiltro) {
		this.listContratoFiltro = listContratoFiltro;
	}
	public List<CondicionBean> getListaContrato() {
		return listaContrato;
	}
	public void setListaContrato(List<CondicionBean> listaContrato) {
		this.listaContrato = listaContrato;
	}
	public List<CondicionBean> getListaAdenda() {
		return listaAdenda;
	}
	public void setListaAdenda(List<CondicionBean> listaAdenda) {
		this.listaAdenda = listaAdenda;
	}
	public RentaBean getSelectedRenta() {
		return selectedRenta;
	}
	public void setSelectedRenta(RentaBean selectedRenta) {
		this.selectedRenta = selectedRenta;
	}
	public List<String> getListaOrigenSupensionTemporalRenta() {
		return listaOrigenSupensionTemporalRenta;
	}
	public void setListaOrigenSupensionTemporalRenta(
			List<String> listaOrigenSupensionTemporalRenta) {
		this.listaOrigenSupensionTemporalRenta = listaOrigenSupensionTemporalRenta;
	}
	public IpcService getIpcService() {
		return ipcService;
	}
	public void setIpcService(IpcService ipcService) {
		this.ipcService = ipcService;
	}
	public ArchivoManagedBean getArchivoMB() {
		return archivoMB;
	}
	public void setArchivoMB(ArchivoManagedBean archivoMB) {
		this.archivoMB = archivoMB;
	}
	public Institucion getInstitucion() {
		return institucion;
	}
	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}
	public Representante getRepresentante() {
		return representante;
	}
	public void setRepresentante(Representante representante) {
		this.representante = representante;
	}
	public Cuentabancaria getCtabancaria() {
		return ctabancaria;
	}
	public void setCtabancaria(Cuentabancaria ctabancaria) {
		this.ctabancaria = ctabancaria;
	}
	public Boolean getSitipocontratoalquiler() {
		return sitipocontratoalquiler;
	}
	public void setSitipocontratoalquiler(Boolean sitipocontratoalquiler) {
		this.sitipocontratoalquiler = sitipocontratoalquiler;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public int getNroadenda_ant() {
		return nroadenda_ant;
	}
	public void setNroadenda_ant(int nroadenda_ant) {
		this.nroadenda_ant = nroadenda_ant;
	}
	public IRecaudacionReporteService getReporteRecaudacionService() {
		return reporteRecaudacionService;
	}
	public void setReporteRecaudacionService(
			IRecaudacionReporteService reporteRecaudacionService) {
		this.reporteRecaudacionService = reporteRecaudacionService;
	}
	public CuotaBean getCuotaPendienteCondicion() {
		return cuotaPendienteCondicion;
	}
	public void setCuotaPendienteCondicion(CuotaBean cuotapendientecondicion) {
		this.cuotaPendienteCondicion = cuotapendientecondicion;
	}
	public List<CuotaBean> getListaCuotasPendientesCondicion() {
		return listaCuotasPendientesCondicion;
	}
	public void setListaCuotasPendientesCondicion(
			List<CuotaBean> listaCuotasPendientesCondicion) {
		this.listaCuotasPendientesCondicion = listaCuotasPendientesCondicion;
	}
	public List<CuotaBean> getListaCuotasPendienltesCondicionCancelarFilter() {
		return listaCuotasPendienltesCondicionCancelarFilter;
	}
	public void setListaCuotasPendienltesCondicionCancelarFilter(
			List<CuotaBean> listaCuotasPendienltesCondicionCancelarFilter) {
		this.listaCuotasPendienltesCondicionCancelarFilter = listaCuotasPendienltesCondicionCancelarFilter;
	}
	public List<CuotaBean> getListaCuotasPendientesCondicionCancelar() {
		return listaCuotasPendientesCondicionCancelar;
	}
	public void setListaCuotasPendientesCondicionCancelar(
			List<CuotaBean> listaCuotasPendientesCondicionCancelar) {
		this.listaCuotasPendientesCondicionCancelar = listaCuotasPendientesCondicionCancelar;
	}
	public boolean getSigenerarcuotas() {
		return sigenerarcuotas;
	}
	public void setSigenerarcuotas(boolean sigenerarcuotas) {
		this.sigenerarcuotas = sigenerarcuotas;
	}
	public String getMesDesde_rec() {
		return mesDesde_rec;
	}
	public void setMesDesde_rec(String mesDesde_rec) {
		this.mesDesde_rec = mesDesde_rec;
	}
	public String getAnioDesde_rec() {
		return anioDesde_rec;
	}
	public void setAnioDesde_rec(String anioDesde_rec) {
		this.anioDesde_rec = anioDesde_rec;
	}
	public String getMesHasta_rec() {
		return mesHasta_rec;
	}
	public void setMesHasta_rec(String mesHasta_rec) {
		this.mesHasta_rec = mesHasta_rec;
	}
	public String getAnioHasta_rec() {
		return anioHasta_rec;
	}
	public void setAnioHasta_rec(String anioHasta_rec) {
		this.anioHasta_rec = anioHasta_rec;
	}
	public List<CuotaBean> getLista() {
		return lista;
	}
	public void setLista(List<CuotaBean> lista) {
		this.lista = lista;
	}
	public Double getTipcambio() {
		return tipcambio;
	}
	public void setTipcambio(Double tipcambio) {
		this.tipcambio = tipcambio;
	}
	public String getClaveupatemporal() {
		return claveupatemporal;
	}
	public void setClaveupatemporal(String claveupatemporal) {
		this.claveupatemporal = claveupatemporal;
	}
	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}
	public void setRecaudacionReporteService(
			IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}
	public Double getMontosolescuotas() {
		return montosolescuotas;
	}
	public void setMontosolescuotas(Double montosolescuotas) {
		this.montosolescuotas = montosolescuotas;
	}
	public Double getMontosolesdolares() {
		return montosolesdolares;
	}
	public void setMontosolesdolares(Double montosolesdolares) {
		this.montosolesdolares = montosolesdolares;
	}
	public BigDecimal getTotaldeudasoles() {
		return totaldeudasoles;
	}
	public void setTotaldeudasoles(BigDecimal totaldeudasoles) {
		this.totaldeudasoles = totaldeudasoles;
	}
	public BigDecimal getTotaldeudadolares() {
		return totaldeudadolares;
	}
	public void setTotaldeudadolares(BigDecimal totaldeudadolares) {
		this.totaldeudadolares = totaldeudadolares;
	}


	/**
	 * @return the tipocambioService
	 */
	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}
	/**
	 * @param tipocambioService the tipocambioService to set
	 */
	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}
	public Boolean getObser() {
		return obser;
	}
	public void setObser(Boolean obser) {
		this.obser = obser;
	}
	public List<Integer> getListaidcontrato() {
		return listaidcontrato;
	}
	public void setListaidcontrato(List<Integer> listaidcontrato) {
		this.listaidcontrato = listaidcontrato;
	}
	public List<MaeDetalleCondicion> getListaMaeCondicion() {
		return listaMaeCondicion;
	}
	public void setListaMaeCondicion(List<MaeDetalleCondicion> listaMaeCondicion) {
		this.listaMaeCondicion = listaMaeCondicion;
	}
	public List<MaeDetCondicionDetalle> getListaMaeCondicionDetalle() {
		return listaMaeCondicionDetalle;
	}
	public void setListaMaeCondicionDetalle(
			List<MaeDetCondicionDetalle> listaMaeCondicionDetalle) {
		this.listaMaeCondicionDetalle = listaMaeCondicionDetalle;
	}
    
    
	
}
