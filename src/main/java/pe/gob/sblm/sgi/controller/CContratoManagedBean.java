package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
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
import pe.gob.sblm.sgi.bean.PeriodoContratoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Adenda;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.entity.Condicionincumplimientoclausula;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detalleadenda;
import pe.gob.sblm.sgi.entity.Detalleclausula;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Fechapago;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.MaeIpc;
import pe.gob.sblm.sgi.entity.PeriodoContrato;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.service.IpcService;
import pe.gob.sblm.sgi.serviceImpl.ContratoService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

/** 
* Resumen.
* Objeto : CContratoManagedBean
* Descripción : Clase controladora de CContratoManagedBean.
* Fecha de Creación : 27/03/2014.
* Autor : Franco Mallqui. 
* ------------------------------------------------------------------------
* Modificaciones
* Fecha            Nombre                     Descripción
* ------------------------------------------------------------------------
* 05/04/2014	Franco Mallqui				Adición de variables.
* 14/04/2014	Franco Mallqui				Adición de Validaciones al guardar un contrato.
*/

@ManagedBean(name = "ccMB")
@ViewScoped
public class CContratoManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger Logger=LoggerFactory.getLogger(AutovaluoCobranzaManagedBean.class);
	
	private String estado;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private List<RentaBean> listaRentasAdenda= new ArrayList<RentaBean>();
	private List<RentaBean> listaRentaPagoPosterior= new ArrayList<RentaBean>();
	private Integer secuenciaRentaPagoPosterior;
	private Double rentaPagoPosterior,rentaPagoPosteriorSelected;
	private String observacionPagoPosterior="";
	private RentaBean selectedRenta,selectedRentaAdenda;
	
	
	private Usuario selectRegistroUsuario;
	private List<Integer> listaAnho;

	// Objetos
	private CondicionBean contratoBean= new CondicionBean();
	private Boolean sitipocontratoalquiler=Boolean.FALSE;
	
	
	private Inquilino inquilino;
	private Upa upa;
	
	private Institucion institucion;
	private Representante representante;
	private Representante representanteaval;
	private Cliente cliente;
	private Cuentabancaria ctabancaria;

	// Lista
	private List<Inquilino> listInquilino, listInquilinoFiltro;
	private List<Upa> listUpa, listUpaFiltro;
	private List<CondicionBean> listContrato, listContratoFiltro,listaContrato,listaAdenda;
	private List<Cuentabancaria> listCtasBancarias;

	private List<Institucion> listInstitucion;
	private List<Representante> listRepresentante;
	private List<Cliente> listCliente;

	private boolean sidolar;
	private boolean sisoles;

	private List<Usuario> listadousuariosSeleccionados;
	private int indicesalvador;
	List<Usuario> todosUsuarios;

	private int idUsuarioSeleccionado;
	
	private int tipobusqueda;
	private int tipobusquedaUpa;
	private String tipobusquedaInquilino;
	private String valorbusqueda;
	private String valorbusquedaUpa;
	private String valorbusquedaInquilino;

	private String nombreOcupante;
	
	/****/
	private Boolean disabledFinCobro=true;
	private Boolean disabledButtonAgregarPeriodo=false , disabledValorIncremento=false;

	
	//Pestaña Cuotas
	private List<Cuota> listacuotas;
	private List<Detallecuota> listadetallecuota=new ArrayList<Detallecuota>();
	private Cuota cuota= new Cuota();
	private String  mesInicioCobro="",mesFinCobro="",anioInicioCobro="",anioFinCobro="",mesFinCobroResuelto="",anioFinCobroResuelto="",mesFinCobroFinalizado="",anioFinCobroFinalizado="",mesFinCobroAdenda="",anioFinCobroAdenda="",mesInicioCobroAdenda="",anioInicioCobroAdenda="";

	private List<PeriodoContratoBean> listaperiodos = new  ArrayList<PeriodoContratoBean>(); 
	private PeriodoContratoBean periodoContratoBean = new PeriodoContratoBean(); 
	private Date minFechaInicio ;
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;

	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{ipcService}")
	private transient IpcService ipcService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{archivoMB}")
	ArchivoManagedBean archivoMB;
	
	@ManagedProperty(value= "#{arrendamientoconsultaMB}")
	ArrendamientoConsultaManagedBean arrendamientoConsultaMB;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService reporteRecaudacionService;
	
	
	private String activeIndexTabView;
	private Boolean sicontratofuturo;
	
	
	private Integer numAniosContrato, numMesesContrato;
	private Integer numAniosPeriodo,numMesesPeriodo;
	private Integer numAniosAdendaContrato,numMesesAdendaContrato;
	
	private String numContratoCambiarEstadoCondicion;
	
	private List<String> listaOrigenSupensionTemporalRenta; 
	/**numero de adenda*/
	private int nroadenda_ant=0;
	private Integer nroPeriodosxInteres=0;
	private int indicadorPeriodo;
	//
	private boolean siexistenumMesAnio=true,siexistenumMesAnioAdenda=true;
	private boolean isCompletoCondicionAdenda=false;
	
	private List<CuotaBean> cuotasPendientes,cuotasPendientesSeleccion,cuotasPendientesFilter;
	private CuotaBean cuotaPendienteCondicion;
	private List<MaeDetalleCondicion> listaMaeDetalleCondicion = new ArrayList<MaeDetalleCondicion>();
	private List<Fechapago> listaDiaPago = new ArrayList<Fechapago>();
	private List<Detalleclausula> listaPenalidades = new ArrayList<Detalleclausula>();
	private List<Condicionincumplimientoclausula> listaCondicionIncumplimiento = new ArrayList<Condicionincumplimientoclausula>();
	private Condicionincumplimientoclausula condicionCumplimiento = new Condicionincumplimientoclausula();
	private String tipoInteresMoratorio;
	private Detalleclausula detalleclausula ;
	private Date fechActual= new Date();
	
	/**
	 * Inicializa los objetos del controlador.
	 */
	@PostConstruct
	public void init() {
		
		initNewMB();
		initListDAO();
		contratoBean.setSisuscrito(false);
		contratoBean.setSiactaentrega(false);
		contratoBean.setSiocupante(true);
		contratoBean.setNumerocuotas(0);
		contratoBean.setSiactaentrega(false);
		contratoBean.setRentaMensualAdenda(0.0);
		sidolar = false;
		sisoles = true;
		tipobusqueda=1;
		tipobusquedaInquilino=Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL;
		tipobusquedaUpa=1;
		activeIndexTabView="0";
		sicontratofuturo=Boolean.FALSE;
		contratoBean.setEstado(Constantes.CONDICION_ESTADO_VIGENTE);
		contratoBean.setSiescriturapublica(false);
		contratoBean.setSiincrementomontovariable(false);
		contratoBean.setSiincrementoporcentajevariable(false);
		minFechaInicio = FuncionesHelper.ponerDiasFechaInicioAnio(FuncionesHelper.sumarRestarAnios(new Date(),-1));
		
		
	}
	
public void validarAgregarNumeroPeriodos(){
	listaperiodos.clear();
	cancelarDatosIncrementoVariable();
	if(contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)){
		if(!contratoBean.getSiincrementoporcentajevariable() && !contratoBean.getSiincrementomontovariable()){
			addWarnMessage("", "Seleccione tipo de incremento variable de las rentas.");
		}else{
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgIngresarNumeroPeriodos.show();");
		}
	}
}
	
public void validarGenerarPeriodos(){
		
		listaRentas.clear();
		//listaperiodos.clear();
		selectedRenta=null;
		
		if (numAniosContrato==0 && numMesesContrato==0) {
			
			addWarnMessage("", "Ingrese n\u00famero de años y/o meses de contrato.");
			
		}else if (contratoBean.getIniciocontrato()==null) {
			
			addWarnMessage("","Seleccionar inicio de contrato para continuar.");
		} else if (contratoBean.getFincontrato()==null) {
			
			addWarnMessage("","Seleccionar fin de contrato para continuar.");
		} 
//		else if (validarIntervaloFechaContrato()) {
//			
//			addWarnMessage("","Seleccionar intervalo de fecha de contrato válido para continuar.");
//		} 
		else if (anioInicioCobro.equals("") || mesInicioCobro.equals("") ) {
			
			addWarnMessage("","Ingrese inicio de cobro para continuar.");
		} else if (validarIntervaloFechaCobro()) {
			
			addWarnMessage("","Seleccionar fecha de inicio de cobro válido para continuar.");
		} else if (contratoBean.getContraprestacion()==0.0) {
		
			addWarnMessage("","Ingresar valor de contraprestación correspondiente.");
		
		} else if (contratoBean.getSicontraprestacionadicional() && contratoBean.getContraprestacionadicional()==0.0) {
		
			addWarnMessage("","Ingresar valor de contraprestación  adicional correspondiente.");
		
		} else if (contratoBean.getSicontraprestacionadicional() && contratoBean.getAniocontraprestacionadicional()==0) {
		
			addWarnMessage("","Ingresar n\u00famero de año desde que aplica contraprestación adicional.");
		
		} else if (contratoBean.getTiporeajusteanual().equals("")) {
		
			addWarnMessage("","Seleccionar tipo de reajuste anual.");
		
		}else if ((contratoBean.getTiporeajusteanual().equals("0") || contratoBean.getTiporeajusteanual().equals("2")) && contratoBean.getReajusteanual()==0.0) {
		
			addWarnMessage("","Ingrese valor  reajuste anual.");
		
		}else if ((contratoBean.getTiporeajusteanual().equals("0") || contratoBean.getTiporeajusteanual().equals("2")) && contratoBean.getReajusteanual()==0.0) {
		
			addWarnMessage("","Ingrese valor  reajuste anual.");
		
		}else {
			if(contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)){
				generarPeriodosIncrementosxContrato();
			}else{
				generarPeriodos();
			}
			
		}
		
		
	}
public void validarGenerarPeriodosAdenda(){
	
	listaRentasAdenda.clear();
	selectedRentaAdenda=null;
	
	if (numAniosAdendaContrato==0 && numMesesAdendaContrato==0) {
		
		addWarnMessage("", "Ingrese n\u00famero de años y/o meses para el aumento del plazo del Contrato.");
		
	}else if (contratoBean.getInicioAdendaContrato()==null) {
		
		addWarnMessage("","Seleccionar inicio de Adenda para continuar.");
	} else if (contratoBean.getFinAdendaContrato()==null) {
		
		addWarnMessage("","Seleccionar fin de Adenda para continuar.");
	} 
//	else if (validarIntervaloFechaContrato()) {
//		
//		addWarnMessage("","Seleccionar intervalo de fecha de contrato válido para continuar.");
//	} 
	else if (anioInicioCobroAdenda.equals("") || mesInicioCobroAdenda.equals("") ) {
		
		addWarnMessage("","Ingrese inicio de cobro para continuar.");
	} else if (validarIntervaloFechaCobroAdenda()) {
		
		addWarnMessage("","Seleccionar fecha de inicio de cobro válido para continuar.");
	} else if (contratoBean.getRentaMensualAdenda()==0.0) {
	
		addWarnMessage("","Ingresar valor de renta mensual correspondiente.");
	
	}else{
		generarPeriodosxAdendaContrato();
	}
	
	
}
	private Calendar dateToCalendar(Date date) {

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar;

	}
    public void generarPeriodosIncrementosxContrato(){
    	for(PeriodoContratoBean periodoconBean : listaperiodos ){
    	List<MaeIpc> listaIpc= new ArrayList<MaeIpc>();
		listaIpc=ipcService.listarIpc();
		
		
		//Calendar iniciocobro = FechaUtil.setTime(Integer.parseInt(anioInicioCobro), Integer.parseInt(Almanaque.mesanumero(mesInicioCobro)),0, 0, 0, 0, 0);
		Calendar iniciocobro= dateToCalendar(periodoconBean.getFechainicioperiodo());
		Calendar fincobro= Calendar.getInstance();
		//fincobro.set(Integer.parseInt(anioFinCobro), Integer.parseInt(Almanaque.mesanumero(mesFinCobro))-1, 1, 0, 0, 0);
		fincobro= dateToCalendar(periodoconBean.getFechafinperiodo());
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
		
		while (iniciocobro.compareTo(fincobro) < 0 && i< periodoconBean.getNumerocuotas()){
			
			RentaBean rentaBean= new RentaBean();
			
			rentaBean.setId(listaRentas.size());
			rentaBean.setSecuencia(rentaBean.getId()+1);
			rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
		
			
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
//				rentaBean.setContraprestacion(periodoconBean.getMonto());
				
				if (contratoBean.getTiporeajusteanual().equals("0")) {
					if (i%12==0) {
						
						indice= Math.pow((contratoBean.getReajusteanual()+100)/100, numanio-1);
						numanio++;
					
					}

					rentaBean.setRenta(FuncionesHelper.redondear(contraprestacion*indice, 2));
					rentaBean.setRentaTemp(rentaBean.getRenta());
					rentaBean.setMontopagar(rentaBean.getRenta());
				
				}else if (contratoBean.getTiporeajusteanual().equals("1")) {
					
					//if (iniciocobro.compareTo(fechafinal)<0) {
					if (iniciocobro.compareTo(fechafinal)<0) {
						
						
						/** Detector primer año**/
						if (i<periodosTerminoPrimerAnio) { 
							rentaBean.setRenta(contraprestacion);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							siPrimerAnio=Boolean.TRUE;
							
						}else if (iniciocobro.get(Calendar.MONTH)==0) {
							
							valorUltimaRenta=listaRentas.get(i-1).getRenta();
							siPrimerAnio=Boolean.FALSE;
							
							for (MaeIpc maeIpc : listaIpc) {
								if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
									valorIpc=maeIpc.getValor();
									break;
								}
							}
						}

						if (!siPrimerAnio) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							
						}
						
					}else {
						rentaBean.setRenta(0.0);
						rentaBean.setRentaTemp(rentaBean.getRenta());
						rentaBean.setMontopagar(0.0);
						rentaBean.setSirentagenerada(Boolean.FALSE);
					}

				}else if (contratoBean.getTiporeajusteanual().equals("2")) {
					
					
					if (iniciocobro.compareTo(fechafinal)<0) {
						
						
						/** Detector primer año**/
						if (i<periodosTerminoPrimerAnio) {
							rentaBean.setRenta(contraprestacion);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							siPrimerAnio=Boolean.TRUE;
							
						}else if (iniciocobro.get(Calendar.MONTH)==0) {
							
							siPrimerAnio=Boolean.FALSE;
							
								for (MaeIpc maeIpc : listaIpc) {
									siIpcAnioRegistrado=Boolean.FALSE;
//									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
										siIpcAnioRegistrado=Boolean.TRUE;
										break;
									}
								}
							
							valorUltimaRenta=listaRentas.get(i-1).getRenta();
							
							if (siIpcAnioRegistrado) {
								
								for (MaeIpc maeIpc : listaIpc) {
									
									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()>contratoBean.getReajusteanual()) {
										flagReajusteIpc=Boolean.TRUE;
										flagReajusteAnual=Boolean.FALSE;
										valorIpc=maeIpc.getValor();
										
										break;
							
									} else if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()<=contratoBean.getReajusteanual()) {
										flagReajusteAnual=Boolean.TRUE;
										flagReajusteIpc=Boolean.FALSE;
										
										break;
							
									}
								
								}
								
							} 
						}
						
						if (flagReajusteAnual) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+contratoBean.getReajusteanual())/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
						
						}
						if (flagReajusteIpc) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
						} 
						if (!siIpcAnioRegistrado && !siPrimerAnio) {
							rentaBean.setRenta(0.0);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(0.0);
							rentaBean.setSirentagenerada(Boolean.FALSE);
						}	
						
					}else {
						rentaBean.setRenta(0.0);
						rentaBean.setRentaTemp(rentaBean.getRenta());
						rentaBean.setMontopagar(0.0);
						rentaBean.setSirentagenerada(Boolean.FALSE);
					}
				}else if (contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)) {
//					if (i%periodoconBean.getNumerocuotas()==0) {
//						
//						indice= Math.pow((periodoconBean.getMonto() +100)/100, numanio-1);
//						numanio++;
//					
//					}
					

					rentaBean.setRenta(FuncionesHelper.redondear(periodoconBean.getMonto(), 2));
					rentaBean.setRentaTemp(rentaBean.getRenta());
					rentaBean.setMontopagar(rentaBean.getRenta());
				
				}
			
//			}
			
			i++;
			iniciocobro.add(Calendar.MONTH, 1);
			listaRentas.add(rentaBean);
		}
		
    	}
    	
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
			rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
		
			
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
				
				if (contratoBean.getTiporeajusteanual().equals("0")) {
					if (i%12==0) {
						
						indice= Math.pow((contratoBean.getReajusteanual()+100)/100, numanio-1);
						numanio++;
					
					}

					rentaBean.setRenta(FuncionesHelper.redondear(contraprestacion*indice, 2));
					rentaBean.setRentaTemp(rentaBean.getRenta());
					rentaBean.setMontopagar(rentaBean.getRenta());
				
				}else if (contratoBean.getTiporeajusteanual().equals("1")) {
					
					//if (iniciocobro.compareTo(fechafinal)<0) {
					if (iniciocobro.compareTo(fechafinal)<0) {
						
						
						/** Detector primer año**/
						if (i<periodosTerminoPrimerAnio) { 
							rentaBean.setRenta(contraprestacion);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							siPrimerAnio=Boolean.TRUE;
							
						}else if (iniciocobro.get(Calendar.MONTH)==0) {
							
							valorUltimaRenta=listaRentas.get(i-1).getRenta();
							siPrimerAnio=Boolean.FALSE;
							
							for (MaeIpc maeIpc : listaIpc) {
								if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
									valorIpc=maeIpc.getValor();
									break;
								}
							}
						}

						if (!siPrimerAnio) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							
						}
						
					}else {
						rentaBean.setRenta(0.0);
						rentaBean.setRentaTemp(rentaBean.getRenta());
						rentaBean.setMontopagar(0.0);
						rentaBean.setSirentagenerada(Boolean.FALSE);
					}

				}else if (contratoBean.getTiporeajusteanual().equals("2")) {
					
					
					if (iniciocobro.compareTo(fechafinal)<0) {
						
						
						/** Detector primer año**/
						if (i<periodosTerminoPrimerAnio) {
							rentaBean.setRenta(contraprestacion);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
							siPrimerAnio=Boolean.TRUE;
							
						}else if (iniciocobro.get(Calendar.MONTH)==0) {
							
							siPrimerAnio=Boolean.FALSE;
							
								for (MaeIpc maeIpc : listaIpc) {
									siIpcAnioRegistrado=Boolean.FALSE;
//									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
										siIpcAnioRegistrado=Boolean.TRUE;
										break;
									}
								}
							
							valorUltimaRenta=listaRentas.get(i-1).getRenta();
							
							if (siIpcAnioRegistrado) {
								
								for (MaeIpc maeIpc : listaIpc) {
									
									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()>contratoBean.getReajusteanual()) {
										flagReajusteIpc=Boolean.TRUE;
										flagReajusteAnual=Boolean.FALSE;
										valorIpc=maeIpc.getValor();
										
										break;
							
									} else if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR) && maeIpc.getValor()<=contratoBean.getReajusteanual()) {
										flagReajusteAnual=Boolean.TRUE;
										flagReajusteIpc=Boolean.FALSE;
										
										break;
							
									}
								
								}
								
							} 
						}
						
						if (flagReajusteAnual) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+contratoBean.getReajusteanual())/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
						
						}
						if (flagReajusteIpc) {
							rentaBean.setRenta(FuncionesHelper.redondear(valorUltimaRenta*(100+valorIpc)/100, 2));
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(rentaBean.getRenta());
						} 
						if (!siIpcAnioRegistrado && !siPrimerAnio) {
							rentaBean.setRenta(0.0);
							rentaBean.setRentaTemp(rentaBean.getRenta());
							rentaBean.setMontopagar(0.0);
							rentaBean.setSirentagenerada(Boolean.FALSE);
						}	
						
					}else {
						rentaBean.setRenta(0.0);
						rentaBean.setRentaTemp(rentaBean.getRenta());
						rentaBean.setMontopagar(0.0);
						rentaBean.setSirentagenerada(Boolean.FALSE);
					}
				}
			
//			}
			
			i++;
			iniciocobro.add(Calendar.MONTH, 1);
		
			listaRentas.add(rentaBean);
		}
		
	}
public void generarPeriodosxAdendaContrato() {
		
		
//		List<MaeIpc> listaIpc= new ArrayList<MaeIpc>();
//		listaIpc=ipcService.listarIpc();
		
		Calendar iniciocobro = FechaUtil.setTime(Integer.parseInt(anioInicioCobroAdenda), Integer.parseInt(Almanaque.mesanumero(mesInicioCobroAdenda)),0, 0, 0, 0, 0);
		Calendar fincobro= Calendar.getInstance();
		fincobro.set(Integer.parseInt(anioFinCobroAdenda), Integer.parseInt(Almanaque.mesanumero(mesFinCobroAdenda))-1, 1, 0, 0, 0);
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		

		Calendar fechafinal = GregorianCalendar.getInstance();
		fechafinal.set(fechafinal.get(Calendar.YEAR), 11, 31, 0, 0, 0);
		
		int numanio=1;
		Double indice=1.0;
		int i=0;
		
		System.out.println("fincontrato adenda"+contratoBean.getFinAdendaContrato());
		Boolean flagReajusteAnual=Boolean.FALSE,flagReajusteIpc=Boolean.FALSE;
		Double valorUltimaRenta = 0.0,valorIpc=0.0;
		Boolean siIpcAnioRegistrado=Boolean.FALSE;
		Integer periodosTerminoPrimerAnio=12-iniciocobro.get(Calendar.MONTH);
		Boolean siPrimerAnio=Boolean.FALSE;
		Double contraprestacion;
		
		while (iniciocobro.compareTo(fincobro) < 0){
			
			RentaBean rentaBean= new RentaBean();
			
			rentaBean.setId(listaRentasAdenda.size());
			rentaBean.setSecuencia(rentaBean.getId()+1);
			rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
		
			
			rentaBean.setSiclausulaperiodogracia(Boolean.FALSE);
			rentaBean.setSiclausulareconocimientoinversion(Boolean.FALSE);
			rentaBean.setSiclausulapagoposterior(Boolean.FALSE);
			rentaBean.setSiclausulareconocimientorenta(Boolean.FALSE);
			rentaBean.setSipagofraccionrenta(Boolean.FALSE);
			rentaBean.setSiclausulasuspensiontemporalrenta(Boolean.FALSE);
			
			rentaBean.setSirentagenerada(Boolean.TRUE);
			
			
			
			
//			if (contratoBean.getSicontraprestacionadicional() && (contratoBean.getAniocontraprestacionadicional().intValue()-1)*12<i+1) {
//				
//				contraprestacion=contratoBean.getContraprestacionadicional();
//				
//				if ((contratoBean.getAniocontraprestacionadicional().intValue()-1)*12==i) {
//					numanio=1;
//					indice=1.0;
//					flagReajusteAnual=Boolean.FALSE;flagReajusteIpc=Boolean.FALSE;
//					valorUltimaRenta = 0.0;valorIpc=0.0;
//					siIpcAnioRegistrado=Boolean.FALSE;
//					periodosTerminoPrimerAnio=12-iniciocobro.get(Calendar.MONTH)+i;
//					siPrimerAnio=Boolean.FALSE;
//				}
//				
//			}else {
				//contraprestacion=contratoBean.getRentaMensualAdenda();
				contraprestacion=contratoBean.getContraprestacion();
//			}
				
				
				rentaBean.setContraprestacion(contraprestacion);	
				
//				if (contratoBean.getTiporeajusteanual().equals("0")) {
//					if (i%12==0) {
//						
//						indice= Math.pow((contratoBean.getReajusteanual()+100)/100, numanio-1);
//						numanio++;
//					
//					}

					//rentaBean.setRenta(FuncionesHelper.redondear(contraprestacion*indice, 2));
					rentaBean.setRenta(FuncionesHelper.redondear(contratoBean.getRentaMensualAdenda(), 2));
					rentaBean.setRentaTemp(rentaBean.getRenta());
					rentaBean.setMontopagar(rentaBean.getRenta());
//				}
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
////									if (Integer.parseInt(maeIpc.getAnio())==iniciocobro.get(Calendar.YEAR)) {
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
			
//			}
			
			i++;
			iniciocobro.add(Calendar.MONTH, 1);
		
			listaRentasAdenda.add(rentaBean);
			isCompletoCondicionAdenda=Boolean.TRUE;
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
	
	
	/**
	 * 
	 * @param rentaBean
	 * Metodos para configurar los periodos referencia de clausula de pago posterior
	 */
	
	public  void configurarClausulaPagoPosterior(RentaBean rentaBean){
		
		listaRentaPagoPosterior.clear();
		
		if (rentaBean.getSiclausulapagoposterior()) {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("configurarPagoRentaPosterior.show();");
			
		}
	}

	public void validarAgregarConfiguracionPagoRentaPosterior(){
		
		if (rentaPagoPosteriorSelected==0.0) {
			
			addWarnMessage("","Ingrese el valor del monto de período seleccionado que se pagara posteriormente. ");
			
		}else if (secuenciaRentaPagoPosterior==0) {
			
			addWarnMessage("","Seleccione un período posterior. ");
		}else if (secuenciaRentaPagoPosterior==selectedRenta.getSecuencia()) {
			
			addWarnMessage("","Usted no puede seleccionar un período de pago posterior igual al periodo a configurar. ");
		}else if (rentaPagoPosterior==0.0 || rentaPagoPosterior<0.0) {
			
			addWarnMessage("","Ingrese un valor del período posterior. ");
		}else {
			
			agregarConfiguracionPagoRentaPosterior();
		}
		
		
	}
	
	public void agregarConfiguracionPagoRentaPosterior(){
		
		
		for (RentaBean rentaBean : listaRentas) {
			if (rentaBean.getSecuencia()==secuenciaRentaPagoPosterior) {
				observacionPagoPosterior=observacionPagoPosterior+rentaBean.getMes()+" "+rentaBean.getAnio()+",";
				rentaBean.setMontopagoposterior(rentaPagoPosterior);
				rentaBean.setSiclausulapagoposterior(Boolean.FALSE);
				listaRentaPagoPosterior.add(rentaBean);
			}
		
		}
		
		rentaPagoPosterior=0.0;
		secuenciaRentaPagoPosterior=0;
	}
	
	
	
	
	public void grabarConfiguracionPagoRentaPosterior(){
		
		selectedRenta.setMontopagoposterior(ConvertirUtil.parseNegativeNumber(rentaPagoPosteriorSelected));
		selectedRenta.setObservacionpagoposterior(observacionPagoPosterior);
		selectedRenta.setMontopagar(selectedRenta.getRenta()+selectedRenta.getMontopagoposterior());
		
		for (RentaBean rentaBeanPagoPosterior : listaRentaPagoPosterior) {
			for (RentaBean rentaBean : listaRentas) {
				
				if (rentaBeanPagoPosterior.getSecuencia()==rentaBean.getSecuencia()) {
					rentaBean.setMontopagoposterior(rentaBeanPagoPosterior.getMontopagoposterior());
					rentaBean.setMontopagar(rentaBean.getSirentagenerada()? rentaBeanPagoPosterior.getRenta()+rentaBeanPagoPosterior.getMontopagoposterior():0.0);
					rentaBean.setObservacionpagoposterior(selectedRenta.getMes()+" "+selectedRenta.getAnio());
				}
			}
		}
		
		observacionPagoPosterior="";
	}
	
	public void validarConfiguracionPagoRentaPosterior(){
		
		/* Clausula de Reconocimiento de Posterior */
		
		Double suma=0.0;
		
		for (RentaBean rentaBeanPagoPosterior : listaRentaPagoPosterior) {
			suma=suma+rentaBeanPagoPosterior.getMontopagoposterior();
			
		}
		
		if (!suma.equals(rentaPagoPosteriorSelected)) {
			
			addWarnMessage("","La suma de montos de períodos asociados no corresponden al monto "+rentaPagoPosteriorSelected+" posterior a pagar. ");
		} else {
			
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgConfigurarPeriodosFuturos.show();");
			
		}
		
		
	}
	
	
	public void limpiarConfiguracionPagoRentaPosterior(){
		
		listaRentaPagoPosterior.clear();
		
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
	
	
	public  void validarConfigurarSuspensionTemporalRenta(){
		
		if (listaOrigenSupensionTemporalRenta.size()==0) {
			
			addWarnMessage("","Usted no ha seleccionado ningún periodo con suspensión de temporal de renta. ");
		}else {
			
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgConfirmConfigurarSuspensionTemporalRenta.show();");
			
		}
	}
	
	public  void grabarConfigurarSuspensionTemporalRenta(){
		
		
		for ( String secuencia: listaOrigenSupensionTemporalRenta) {
			for (RentaBean rentaBean : listaRentas) {
				if (Integer.parseInt(secuencia)==rentaBean.getSecuencia()) {

					rentaBean.setSiclausulasuspensiontemporalrenta(Boolean.TRUE);
//					rentaBean.setMontosuspensiontemporalrenta(rentaBean.getRenta());
//					rentaBean.setObservacionsuspensiontemporalrenta("");
					rentaBean.setMontopagar(rentaBean.getRenta());
				}
			}
		}
		
	}
	
	
	
	public  void limpiarConfigurarSuspensionTemporalRenta(){
		listaOrigenSupensionTemporalRenta.clear();
	}
	

	
	public void calcularFinContrato() {
			
		/** Calculo fin contrato**/
			Calendar iniciocontrato=Calendar.getInstance();
			iniciocontrato.setTime(contratoBean.getIniciocontrato());
			
			iniciocontrato.add(Calendar.MONTH, numAniosContrato*12+ numMesesContrato);
			iniciocontrato.add(Calendar.DAY_OF_YEAR, -1);
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
	public void calcularFinPeriodo() {
		
		/** Calculo fin periodo contrato**/
			Calendar inicioperiodocontrato=Calendar.getInstance();
			inicioperiodocontrato.setTime(periodoContratoBean.getFechainicioperiodo());
			
			inicioperiodocontrato.add(Calendar.MONTH, numAniosPeriodo*12+ numMesesPeriodo);
			inicioperiodocontrato.add(Calendar.DAY_OF_YEAR, -1);
			periodoContratoBean.setFechafinperiodo(inicioperiodocontrato.getTime());
			
			inicioperiodocontrato.setTime(periodoContratoBean.getFechainicioperiodo());
			
//		/** Calculo inicio cobro**/	
//			Calendar iniciocobro=Calendar.getInstance();
//			iniciocobro.set(inicioperiodocontrato.get(Calendar.YEAR), inicioperiodocontrato.get(Calendar.MONTH), 1, 0, 0, 0);
//			iniciocobro.set(Calendar.MILLISECOND,0);
//			
//			mesInicioCobro=Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH));
//			anioInicioCobro=String.valueOf(iniciocobro.get(Calendar.YEAR));
//			
//			calcularFinCobro();
	}
	public void calcularFinAdendaContrato() {
		
		/** Calculo fin contrato**/
			Calendar iniciocontrato=Calendar.getInstance();
			iniciocontrato.setTime(contratoBean.getInicioAdendaContrato());
			
			iniciocontrato.add(Calendar.MONTH, numAniosAdendaContrato*12+ numMesesAdendaContrato);
			iniciocontrato.add(Calendar.DAY_OF_YEAR, -1);
			contratoBean.setFinAdendaContrato(iniciocontrato.getTime());
			
			iniciocontrato.setTime(contratoBean.getInicioAdendaContrato());
			
		/** Calculo inicio cobro**/	
			Calendar iniciocobro=Calendar.getInstance();
			iniciocobro.set(iniciocontrato.get(Calendar.YEAR), iniciocontrato.get(Calendar.MONTH), 1, 0, 0, 0);
			iniciocobro.set(Calendar.MILLISECOND,0);
			
			mesInicioCobroAdenda=Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH));
			anioInicioCobroAdenda=String.valueOf(iniciocobro.get(Calendar.YEAR));
			
			calcularFinCobroAdenda();
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
	public void calcularFinCobroAdenda() {
		if (!mesInicioCobroAdenda.equals("") && !anioInicioCobroAdenda.equals("")) {
			
			Calendar inicio=Calendar.getInstance();
			inicio.set(Integer.parseInt(anioInicioCobroAdenda), Integer.parseInt(Almanaque.mesanumero(mesInicioCobroAdenda))-1, 1, 0, 0, 0);
			inicio.set(Calendar.MILLISECOND,0);
			
			inicio.add(Calendar.MONTH, numAniosAdendaContrato*12+ numMesesAdendaContrato);
			inicio.add(Calendar.DAY_OF_YEAR, -1);
			//disabledFinCobro=false;
			
			mesFinCobroAdenda=Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH));
			anioFinCobroAdenda=String.valueOf(inicio.get(Calendar.YEAR));
			
		}else {
			//disabledFinCobro=true;
		}
	}
	
	public void limpiarFinContrato(){
		contratoBean.setIniciocontrato(null);
		contratoBean.setFincontrato(null);
	}
	public void limpiarFechaInicioFinPeriodo(){
		periodoContratoBean.setFechainicioperiodo(null);
		periodoContratoBean.setFechafinperiodo(null);
		siexistenumMesAnio=false;
	}
    public void limpiarFechaInicioFinAdendaContrato(){
    	contratoBean.setInicioAdendaContrato(null);
		contratoBean.setFinAdendaContrato(null);
		siexistenumMesAnioAdenda=false;
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

	 public void onRowToggle(ToggleEvent event) {
		 
		 if (cuota!=null) {
			 listadetallecuota=contratoService.obtenerDetalleDeCuota(cuota.getIdcuota());
		}else {
			listadetallecuota.clear();
		}
		 cuota=null;
	 }

	 public void cargarInstitucion(){
		listInstitucion = contratoService.getListaInstitucion();
	}
	
	public void cargarClienteRepresentante(){
		listRepresentante = contratoService.getListaRepresentante();
		listCliente = contratoService.getListaCliente();
	}

	public void cargarCuentasBancarias(){
		listCtasBancarias = contratoService.getCtasBancarias();
	}
	
	public void buscarContrato(){

		if (tipobusqueda==1) {
			listContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,estado);
			listaContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,"");
			if(listaContrato!=null){
				for(int i=0;i<listaContrato.size();i++){
					if(listaContrato.get(i).getEstado().equals("VIGENTE"))
					{//System.out.println("tipo de cONDICION ="+listaContrato.get(i).getCondicion());
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
	public void buscarUpa(){
		
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
	
	/**
	 * Carga la informacion de contrato al seleccionarlo.
	 */	
	public void setearContratoSeleccionado() {
		try{
		listaRentas.clear();
		listaperiodos.clear();
		listaCondicionIncumplimiento.clear();
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
			if(contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)){
				listaperiodos=reporteRecaudacionService.obtenerPeriodoxContrato(contratoBean.getIdcontrato());
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
		if(contratoBean.getSicondicionclausula()!=null){
			if(contratoBean.getSicondicionclausula()){
					listaCondicionIncumplimiento= contratoService.listarCondicionIncumplimiento(contratoBean.getIdcontrato());
			}
		}
		}catch(Exception e ){
			e.printStackTrace();
		}
	}

	
	/**
	 * Validacion para  determinar si la upa esta disponible para vincular.
	 */	
	public void seleccionarUpa(SelectEvent event) {
		
	
//		if (contratoService.verificarExisteCondicionVigente(upa.getIdupa())) {
//			
//			CondicionBean condicionBean=new CondicionBean();
			
//			condicionBean=contratoService.verificarDisponibilidadContrato(upa.getIdupa());
//			
//			if (condicionBean==null) {
//				upa = null;
//				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar upa debido a que se encuentra ocupada en una condición vigente");
//				FacesContext.getCurrentInstance().addMessage(null, msg);
//			} else {
				
//				sicontratofuturo=Boolean.TRUE;
//				contratoBean.setNrocontrato(Constantes.CONTRATO_NUMERO_PENDIENTE);
//				contratoBean.setTipo(Constantes.CONTRATO_TIPO_RENOVACION);
//				contratoBean.setNombresInquilino(condicionBean.getNombresInquilino());
//				contratoBean.setIdinquilino(condicionBean.getIdinquilino());
//				contratoBean.setRuc(condicionBean.getTipopersona().equals("1")?condicionBean.getDni():condicionBean.getRuc());
//				
//				
//				Calendar fincontrato=Calendar.getInstance();
//				fincontrato.setTime(condicionBean.getFincontrato());
//				fincontrato.add(Calendar.DATE, 1);
//				
//				contratoBean.setIniciocontrato(fincontrato.getTime());
//				contratoBean.setDireccion(condicionBean.getDireccion());
//				contratoBean.setNumeroprincipal(condicionBean.getNumeroprincipal());
//				contratoBean.setNumerointerior(condicionBean.getNumerointerior());
//				contratoBean.setUso(condicionBean.getUso());
//				contratoBean.setIdupa(condicionBean.getIdupa());
//				contratoBean.setClaveUpa(condicionBean.getClaveUpa());
//				contratoBean.setEstado(Constantes.CONDICION_ESTADO_PENDIENTE);
//				
				
//			}
		
//		}else {
		if(!upa.getSiprocesojudicial()){/*CONDICION DE CLAVE UPA EN PROCESO JUDICIAL*/
		if (contratoService.verificarExisteCondicionVigente(upa.getIdupa())) {
			upa = null;
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar la upa debido a que se encuentra ocupada en una condición vigente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else {
			contratoBean.setIdupa(upa.getIdupa());
			contratoBean.setClaveUpa(upa.getClave());
//			contratoBean.setUso(upa.getUso());
			//contratoBean.setDireccion(upa.getInmueble().getDireccion()+ " "+ upa.getInmueble().getNumeroprincipal()+" "+upa.getNombrenuminterior());
			contratoBean.setDireccion(upa.getDireccion()+ " "+ upa.getNumprincipal()+" "+upa.getNombrenuminterior());
		}
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra bloqueada", "La upa seleccionada esta judicializada por lo tanto no se puede realizar ningun tipo de registro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void seleccionarInquilino(SelectEvent event) {
		contratoBean.setIdinquilino(inquilino.getIdinquilino());
		contratoBean.setNombresInquilino(inquilino.getNombrescompletos());
		contratoBean.setRuc(inquilino.getTipopersona().equals("1")?inquilino.getDni():inquilino.getRuc());
	}



	public void verificarExistenciaCodigoContrato() {
		
	if (contratoBean.getNrocontrato().length()>7) {
		if (contratoService.verificarExistenciaCodigoContrato(contratoBean.getNrocontrato())) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un contrato con el n\u00famero: "+contratoBean.getNrocontrato()+" registrado en el sistema", "Reingrese el n\u00famero de contrato, de lo contrario no podra registrar el contrato");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		}	
	}
	

	
	/**
	 * Valida los campos del formulario contrato ante de guardar.
	 */
	public void validarGrabarCabeceraContrato(){
		
		if (StringUtils.isEmpty(contratoBean.getTipocontrato())) {
			
			addWarnMessage("","Ingrese el tipo de contrato para continuar.");
			
		} else if (contratoBean.getTipocontrato().equals("Alquiler") && StringUtils.isEmpty(contratoBean.getTipo())) {
			
			addWarnMessage("","Seleccionar el tipo de alquiler para continuar.");
		} else if (estado.equals(Constantes.CONDICION_CONTRATO) && contratoBean.getNrocontrato().equals("")) {
			
			addWarnMessage("","Ingrese el n\u00famero de contrato para continuar.");
		}else if (contratoBean.getIdupa()==0) {
			
			addWarnMessage("","Seleccionar una upa para continuar.");
		} else if (contratoBean.getIdinquilino()==0) {
			
			addWarnMessage("","Seleccionar un inquilino para continuar.");
		} else if (listaRentas.size()==0) {
			
			addWarnMessage("","Debe generar periodos de renta para continuar.");
		}  else if (contratoBean.getSicartafianza() && (contratoBean.getValorcartafianza()==0.0 || contratoBean.getValorcartafianza()==null)) {
			activeIndexTabView="5";
			addWarnMessage("","Ingresar valor de carta fianza.");
			
		}else if (contratoBean.getSicartafianza() && contratoBean.getFechavencimientocartafianza()==null) {
			activeIndexTabView="5";
			addWarnMessage("","Ingresar fecha de vencimiento de carta fianza.");
			
		}else  {
//			if(archivoMB.getListaArchivoAdjunto().size()==0){
//				RequestContext context = RequestContext.getCurrentInstance();  
//				context.execute("dlgValidarArchivoAdjunto.show();");
//			}else{
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarContrato.show();");
//			}
			
		}
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
	
	public void grabarDevolucionInmueble(){
		contratoService.actualizarDevolucionInmuebleContrato(contratoBean.getIdcontrato(),FuncionesHelper.fechaToString(contratoBean.getFechaactadevolucion()),contratoBean.getNumeroactadevolucion());
		addInfoMessage("", "Se actualizo estado de inmueble a devuelto.");
	
		limpiarRegistroPropiedades();
		listContrato.clear();
		
	}
	
	public void validarQuitarPenalidad(){
		
		if (StringUtils.isEmpty(contratoBean.getObservacionpenalidad())) {
			addWarnMessage("", "Ingrese la observación para continuar");

			
		}else if (contratoBean.getObservacionpenalidad().length()<10) {
			addWarnMessage("", "Debe ingresar mínimo 10 caracteres para continuar. ");

		}else {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgConfirmQuitarPenalidad.show();");
		}
		
	}
	
	public void grabarQuitarPenalidad(){
		
		contratoService.actualizarPenalidadContrato(contratoBean.getIdcontrato(),contratoBean.getObservacionpenalidad());
		addInfoMessage("", "Se quito la penalidad a contrato.");

		limpiarRegistroPropiedades();
		listContrato.clear();
	}
	
	
	public Boolean validarIntervaloFechaContrato(){
		
		int resultado=(FechaUtil.obtenerDiasEntreFecha(contratoBean.getFincontrato(), contratoBean.getIniciocontrato()))/30;
		
		if (resultado==numAniosContrato*12+numMesesContrato) {
			
			return false;
		}else{
			return true;
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
	public Boolean validarIntervaloFechaCobroAdenda(){
		
		Calendar iniciocontrato=Calendar.getInstance();
		iniciocontrato.setTime(contratoBean.getInicioAdendaContrato());
		
		Calendar iniciocobro=Calendar.getInstance();
		
		iniciocobro.set(Calendar.YEAR,Integer.parseInt(anioInicioCobroAdenda));
		iniciocobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesInicioCobroAdenda))-1);
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
		
		for (int i = 0; i < listaRentas.size(); i++) {
			if (i==0) {
				contratoBean.setCuota1(listaRentas.get(i).getRenta());	
			}else if (i==1) {
				contratoBean.setCuota2(listaRentas.get(i).getRenta());	
			}else if (i==2) {
				contratoBean.setCuota3(listaRentas.get(i).getRenta());
			}else if (i==3) {
				contratoBean.setCuota4(listaRentas.get(i).getRenta());
			}else if (i==4) {
				contratoBean.setCuota5(listaRentas.get(i).getRenta());
			}else if (i==5) {
				contratoBean.setCuota6(listaRentas.get(i).getRenta());
			}
		}
		
		contrato.setMontocuotasoles(contratoBean.getCuota1());
		contrato.setMontocuotasoles2(contratoBean.getCuota2());
		contrato.setMontocuotasoles3(contratoBean.getCuota3());
		contrato.setMontocuotasoles4(contratoBean.getCuota4());
		contrato.setMontocuotasoles5(contratoBean.getCuota5());
		contrato.setMontocuotasoles6(contratoBean.getCuota6());
        contrato.setSiescriturapublica(contratoBean.getSiescriturapublica()==null? false : contratoBean.getSiescriturapublica());
        contrato.setSidetraccion(contratoBean.getSidetraccion()==null? false : contratoBean.getSidetraccion());
        contrato.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
		
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
			contrato.setTipo(contratoBean.getTipo());
			contrato.setTipocontrato(contratoBean.getTipocontrato());
			contrato.setObservacion(contratoBean.getObservacion());
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
			contrato.setContraprestacionadicional(contratoBean.getContraprestacionadicional());
			contrato.setTiporeajusteanual(contratoBean.getTiporeajusteanual());
			contrato.setReajusteanual(contratoBean.getReajusteanual());			
			contrato.setSigeneracionrentacompletada(contratoBean.getTiporeajusteanual().equals("2")?Boolean.FALSE:Boolean.TRUE);
			contrato.setSinuevomantenimiento(Boolean.TRUE);
			contrato.setPorcentajepenalidad(contratoBean.getPorcentajepenalidad());
			contrato.setSipenalidad(contratoBean.getSipenalidad());
			// sianulado
			contrato.setSianulado(false);
			contrato.setSidocumento(Boolean.FALSE);
			contrato.setSireconocimientodeuda(Boolean.FALSE);
			contrato.setSimontoinicialdeuda(Boolean.FALSE);
			// Incremento variable
			contrato.setSiincrementovariable(contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)? Boolean.TRUE:Boolean.FALSE);
		    if(contrato.getSiincrementovariable()){
				contrato.setSiincrementoporcentajevariable(contratoBean.getSiincrementoporcentajevariable());
				contrato.setSiincrementomontovariable(contratoBean.getSiincrementomontovariable());
			}else{
				contrato.setSiincrementoporcentajevariable(Boolean.FALSE);
				contrato.setSiincrementomontovariable(Boolean.FALSE);
			}
			
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
				maeDetalleCondicions.add(maeDetalleCondicion);
			}
              //Mae_Detalle Condicion                   
			 contrato.setMaeDetalleCondicions(maeDetalleCondicions);
				if (contratoBean.getTiporeajusteanual().equals(Constante.TIPO_REAJUSTE_RENTA_D)) {
					
					Set<PeriodoContrato> listaperiodoContrato = new HashSet<PeriodoContrato>();
					for (PeriodoContratoBean periodo : listaperiodos) {
						PeriodoContrato periocon = new PeriodoContrato();
						periocon.setContrato(contrato);
						periocon.setPeriodo(periodo.getPeriodo());
						periocon.setMonto(periodo.getMonto());
						periocon.setFechainicioperiodo(periodo
								.getFechainicioperiodo());
						periocon.setFechafinperiodo(periodo
								.getFechafinperiodo());
						periocon.setValorincremento(periodo
								.getValorincremento());
						periocon.setTipoincremento(periodo.getTipoincremento());
						periocon.setNombretipoincremento(periodo
								.getNombretipoincremento());
						periocon.setEstado(periodo.getEstado());
						periocon.setCondicion(periodo.getCondicion());
						periocon.setNumerocuotas(periodo.getNumerocuotas());
						periocon.setUsuariocreador(userMB.getUsuariologueado()
								.getNombres()
								+ " "
								+ userMB.getUsuariologueado().getApellidopat());
						periocon.setFechacreacion(new Date());
						listaperiodoContrato.add(periocon);

					}
					//Periodo_Contrato
					contrato.setPeriodoContrato(listaperiodoContrato);
				}
			if(archivoMB.getListaArchivoAdjunto().size()>0){
				contrato.setSidocumento(Boolean.TRUE);
			}
			
			contratoService.registrarContrato(contrato);
			
			//insertar los documentos
			
			insertarDocumentoAdjunto(contrato);
			//
			addInfoMessage("", "Se registro nuevo contrato con éxito");
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
			contrato.setSimontoinicialdeuda(Boolean.FALSE);
			
			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se actualizó nuevo contrato con éxito");

			limpiarRegistroPropiedades();
			contratoBean = new CondicionBean();
			listContrato.clear();
		}
		
		} catch (Exception e) {
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
  public void validarDatosResolverContrato(){
	  RequestContext contextRequest = RequestContext.getCurrentInstance();
	 // System.out.println("CONTRATOBEAN="+contratoBean.toString());
	  if(contratoBean.getFincontratoresuelto() == null){
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Resolver Contrato", " Debe ingresar la fecha de fin de contrato  ");
		 FacesContext.getCurrentInstance().addMessage(null, msg);
	  }else if(contratoBean.getObservacionresuelto().trim().length()==0){
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Resolver Contrato", " Debe ingresar el motivo para resolver el contrato");
		  FacesContext.getCurrentInstance().addMessage(null, msg);
	  }else if(anioFinCobroResuelto.trim().length()==0){
	  		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Resolver Contrato", "Debe ingresar el mes de fin de cobro contrato  ");
			FacesContext.getCurrentInstance().addMessage(null, msg);
	  }else  if(mesFinCobroResuelto.trim().length()==0){
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Resolver Contrato", " Debe ingresar el año de fin de cobro contrato  ");
		  FacesContext.getCurrentInstance().addMessage(null, msg);
	  }
	  else{
		  contextRequest.execute("dlgConfirmarResolverContrato.show();");
	  }
  	}
  public void validarDatosAnularContrato(){
	  RequestContext contextRequest = RequestContext.getCurrentInstance();
	 // System.out.println("CONTRATOBEAN="+contratoBean.toString());
	  if(contratoBean.getMotivo_anula() == null){
//		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Anular Contrato", " Debe ingresar el motivo de anulacion del "+ contratoBean.getCondicion());
//		 FacesContext.getCurrentInstance().addMessage(null, msg);
		  addWarnMessage("Anular Contrato","Debe ingresar el motivo de anulacion del "+ contratoBean.getCondicion());
	  }else if(contratoBean.getMotivo_anula().length()<10){
//		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Anular Contrato", " Motivo debe ser como minimo de 10 caracteres");
//			 FacesContext.getCurrentInstance().addMessage(null, msg);
		  addWarnMessage("Anular Contrato"," Motivo debe ser como minimo de 10 caracteres");
	  }else{
		  contextRequest.execute("dlgConfirmarAnularCondicion.show();");
	  }
	  System.out.println("fin de metodo validar ");
  	}
  public void validarDatosFinalizarContrato(){
	  RequestContext contextRequest = RequestContext.getCurrentInstance();
	  //System.out.println("CONTRATOBEAN="+contratoBean.toString());
//	  if(contratoBean.getFincontratoresuelto() == null){
//		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Resolver Contrato", " Debe ingresar la fecha de fin de contrato  ");
//		 FacesContext.getCurrentInstance().addMessage(null, msg);
//	  }else 
	  if(contratoBean.getObservacionfinalizado().trim().length()==0){
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Finalizar "+contratoBean.getCondicion(), " Debe ingresar el motivo para finalizar el "+contratoBean.getCondicion());
		  FacesContext.getCurrentInstance().addMessage(null, msg);
	  }else if(anioFinCobroFinalizado.trim().length()==0){
	  		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Finalizar "+contratoBean.getCondicion(), "Debe ingresar el año de fin de cobro del "+contratoBean.getCondicion());
			FacesContext.getCurrentInstance().addMessage(null, msg);
	  }else  if(mesFinCobroFinalizado.trim().length()==0){
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Finalizar "+contratoBean.getCondicion(), " Debe ingresar el mes de fin de cobro del "+contratoBean.getCondicion());
		  FacesContext.getCurrentInstance().addMessage(null, msg);
	  }
	  else{
		  //listarMaeDetalleCondicionRentaAnular();
		  contextRequest.execute("dlgConfirmarFinalizarContrato.show();");
	  }
  	}
   public void listarMaeDetalleCondicionRentaAnular(){
	   listaMaeDetalleCondicion.clear();
		for (CuotaBean cuo : cuotasPendientes) {
			if (cuo.getEstado_maed().equals(Constante.ESTADO_ANULADO)) {
				MaeDetalleCondicion mae = new MaeDetalleCondicion();
				mae = contratoService.getMaeDetalleCondicion(cuo.getIddetallecondicion());
				mae.setFec_anula(new Date());
				mae.setUsr_anula(userMB.getNombreusr());
				//mae.setMotivo_anula(contratoBean.getObservacionfinalizado().toUpperCase());
				mae.setHost_ip_anula(CommonsUtilities.getRemoteIpAddress());
				mae.setEstado(cuo.getEstado_maed());
				listaMaeDetalleCondicion.add(mae);
			}
		}
	   
	   
	   
   }
	public void resolverContrato() {
		
		Calendar finCobro=Calendar.getInstance();
		System.out.println("anioFinCobroResuelto="+anioFinCobroResuelto);
		
		finCobro.set(Calendar.YEAR,Integer.parseInt(anioFinCobroResuelto));
		finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesFinCobroResuelto))-1);
		finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		finCobro.set(Calendar.HOUR,0);
		finCobro.set(Calendar.MINUTE,0);
		finCobro.set(Calendar.SECOND,0);
		finCobro.set(Calendar.MILLISECOND,0);
		
		contratoBean.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		if (contratoBean.getIdcontrato() != 0) {
			contratoService.resolverContrato(contratoBean.getIdcontrato(),contratoBean.getObservacionresuelto(),FuncionesHelper.fechaToString(finCobro.getTime()),FuncionesHelper.fechaToString(contratoBean.getFincontratoresuelto()));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Contrato Resuelto", "Se resolvió contrato: "+ contratoBean.getNrocontrato() + " éxitosamente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarRegistroPropiedades();
			listContrato.clear();
		}
	}
	public void anularContrato() {
		try{
		Boolean sidocumento=false;
        System.out.println("registro de anulacion del precontrato ");
		contratoBean.setUsrmod(userMB.getNombreusr());
		contratoBean.setIp_host_anula(CommonsUtilities.getRemoteIpAddress());
		contratoBean.setEstado(Constante.CONDICION_CONTRATO_ANULADO);
		if(archivoMB.getListaArchivoAdjunto().size()>0){
			contratoBean.setSidocumento(Boolean.TRUE);
			sidocumento=true;
		}
		if (contratoBean.getIdcontrato() != 0) {
			//contratoService.resolverContrato(contratoBean.getIdcontrato(),contratoBean.getObservacionresuelto(),FuncionesHelper.fechaToString(finCobro.getTime()),FuncionesHelper.fechaToString(contratoBean.getFincontratoresuelto()));
			contratoService.anularContrato(contratoBean);
			if(sidocumento){
				Contrato c = new Contrato();
				c.setIdcontrato(contratoBean.getIdcontrato());
				c.setCondicion(contratoBean.getCondicion());
				insertarDocumentoAdjunto(c);
			}
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,contratoBean.getCondicion()+" Anulado", "Se anulo "+contratoBean.getCondicion()+" éxitosamente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarRegistroPropiedades();
			listContrato.clear();
		}

		}catch(Exception e){
			addErrorMessage("",Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		
		
		
	}
	public void finalizarContrato() throws Exception {
		
		Calendar finCobro=Calendar.getInstance();
		
		finCobro.set(Calendar.YEAR,Integer.parseInt(anioFinCobroFinalizado));
		finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesFinCobroFinalizado))-1);
		finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		finCobro.set(Calendar.HOUR,0);
		finCobro.set(Calendar.MINUTE,0);
		finCobro.set(Calendar.SECOND,0);
		finCobro.set(Calendar.MILLISECOND,0);
		contratoBean.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());

		if (contratoBean.getIdcontrato() != 0) {
			if(listaMaeDetalleCondicion.size()>0){
				registrarMaeDetalleCondicion(listaMaeDetalleCondicion);
			}
			if (contratoBean.getIdcontrato() != 0) {
				Contrato c = new Contrato();
				c= contratoService.obtenerContratoPorID(contratoBean.getIdcontrato());
				c.setEstado(Constante.CONDICION_CONTRATO_FINALIZADO);
				c.setSifinalizado(Boolean.TRUE);
				c.setObservacionfinalizado(contratoBean.getObservacionfinalizado().toUpperCase());
				c.setFincobro(finCobro.getTime());
//				if(contratoBean.getCondicion().equals(Constante.CONDICION_PRECONTRATO)){
//					c.setFincontrato(contratoBean.getFincontrato());
//				}				
				c.setFechamodificacion(new Date());
				//c.setFincontrato(c.getFincontrato().equals(contratoBean.getFincontrato())? c.getFincontrato() :contratoBean.getFincontrato());
				c.setFecmodfinalizado(c.getFechamodificacion());
				c.setUsuariomodificador(contratoBean.getUsrmod());
				c.setHost_ip_mod(CommonsUtilities.getRemoteIpAddress());
				
				contratoService.registrarContrato(c);
			}
			
			
			//contratoService.finalizarContrato(contratoBean.getIdcontrato(),contratoBean.getObservacionfinalizado(),FuncionesHelper.fechaToString(finCobro.getTime()),null,contratoBean.getUsrmod());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,contratoBean.getCondicion()+" Finalizado", "Se finalizó el "+contratoBean.getCondicion()+" : "+ contratoBean.getNrocontrato() + " éxitosamente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarRegistroPropiedades();
			listContrato.clear();
		}
		
	}
	public void validacionRegistrarAdenda(){
		if (contratoBean.getNroadenda()==0 ) {
			addWarnMessage("", "El numero de Adenda no puede ser cero");
		} else if(contratoBean.getNroadenda()>0 && contratoBean.getNroadenda()==nroadenda_ant){
			addWarnMessage("", "Ya existe el Nro de Adenda");
		} else if (contratoBean.getObservacionadenda().equals("") ) {
			addWarnMessage("", "Agregar motivo de la adenda");
		} else if(archivoMB.getListaArchivoAdjunto().size()<=0){
			addWarnMessage("","Agregar Documento Adenda .");
		} else if (contratoBean.getTipocondicionadenda()=="" ) {
			addWarnMessage("", "Agregar Tipo de Modificacion Adenda");
		} else if(contratoBean.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_A) && !isCompletoCondicionAdenda ){
			addWarnMessage("", "Debe completar los campos de "+Constante.NOMBRE_TIPO_CONDICION_ADENDA_CONTRATO_SGI.get(Constante.TIPO_CONDICION_ADENDA_A));
		} else if(contratoBean.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_B) && !isCompletoCondicionAdenda){
			addWarnMessage("", "Debe completar los campos de "+Constante.NOMBRE_TIPO_CONDICION_ADENDA_CONTRATO_SGI.get(Constante.TIPO_CONDICION_ADENDA_B));
		}else{
		
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarRegistroAdendaContrato.show();");		
		}
		
	}
	public void incluirAdendaContrato() {
		try{
			
			InetAddress ip;
			String host_ip=null;
			Integer numsecuencia;
			List<MaeDetalleCondicion> listaMaeDetalleCondicion = new ArrayList<MaeDetalleCondicion>();
	        try {
	             ip = InetAddress.getByName(request.getRemoteAddr());
	            System.out.println("host-ip="+ip.getHostName()+"/"+request.getRemoteAddr());
	            host_ip=ip.getHostName()+"/"+request.getRemoteAddr();
	 
	        } catch (UnknownHostException e) {
	 
	            e.printStackTrace();
	        }
			Adenda a= new Adenda();
			Contrato c=new Contrato();
			c.setIdcontrato(contratoBean.getIdcontrato());//idcontrato
			c.setCondicion(contratoBean.getCondicion());
			
			a.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			a.setFeccre(new Date());
			a.setTipocondicionadenda(Integer.parseInt(contratoBean.getTipocondicionadenda()));
			a.setNombretipocondicion(Constante.NOMBRE_TIPO_CONDICION_ADENDA_CONTRATO_SGI.get(contratoBean.getTipocondicionadenda()));
			a.setNroadenda(contratoBean.getNroadenda());
			a.setContrato(c);
			a.setSifinalizado(false);
			a.setEstado(Constante.ESTADO_VIGENTE);
			a.setObservacionadenda(contratoBean.getObservacionadenda());
			a.setEstado_doc(Constante.ESTADO_GENERADO);
			a.setHost_ip_usrcre(host_ip);
			Set<Detalleadenda> detalleadendas= new HashSet<Detalleadenda>();
			if(contratoBean.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_A)){
				//El inicio de cobro y el fin de cobro se parse desde inicio en un dia 1 hasta el fin el un dia 31
//				Calendar inicontrato = Calendar.getInstance();
//				Calendar fincontrato = Calendar.getInstance();
				Calendar iniCobro = new GregorianCalendar();
				Calendar finCobro = new GregorianCalendar();
//				inicontrato.setTime(contratoBean.getInicioAdendaContrato());
//				fincontrato.setTime(contratoBean.getFinAdendaContrato());
				
				iniCobro.set(Calendar.YEAR,Integer.parseInt(anioInicioCobroAdenda));
				iniCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesInicioCobroAdenda))-1);
				iniCobro.set(Calendar.DATE, 1);
				iniCobro.set(Calendar.HOUR,0);
				iniCobro.set(Calendar.MINUTE,0);
				iniCobro.set(Calendar.SECOND,0);
				iniCobro.set(Calendar.MILLISECOND,0);
				
				finCobro.set(Calendar.YEAR,Integer.parseInt(anioFinCobroAdenda));
				finCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesFinCobroAdenda))-1);
				finCobro.set(Calendar.DATE, finCobro.getActualMaximum(Calendar.DAY_OF_MONTH));
				finCobro.set(Calendar.HOUR,0);
				finCobro.set(Calendar.MINUTE,0);
				finCobro.set(Calendar.SECOND,0);
				finCobro.set(Calendar.MILLISECOND,0);
				contratoBean.setEstado(Constantes.CONDICION_ESTADO_VIGENTE);
				contratoBean.setFincobro(finCobro.getTime());
				contratoBean.setFincontrato(contratoBean.getFinAdendaContrato());
				contratoBean.setFincontratoadenda(contratoBean.getFinAdendaContrato());
				a.setIniciocontratoadenda(contratoBean.getInicioAdendaContrato());
				a.setFincontratoadenda(contratoBean.getFinAdendaContrato());				
				a.setFincobrocontrato(finCobro.getTime());
				a.setIniciocobroadenda(iniCobro.getTime());
				a.setFincobroadenda(finCobro.getTime());
				a.setFincobrocontrato(finCobro.getTime());
				a.setNuevofincontrato(contratoBean.getFinAdendaContrato());
				numsecuencia = contratoService.numSecuenciaMaeDetalleCondicion(c.getIdcontrato());
				int i=0;
				
				for (RentaBean rentaBean : listaRentasAdenda) {
					Detalleadenda detalleadenda = new Detalleadenda();
					
					detalleadenda.setAnio(rentaBean.getAnio());
					detalleadenda.setMes(rentaBean.getMes());
					detalleadenda.setAdenda(a);					
					
					detalleadenda.setSecuencia(rentaBean.getSecuencia());
					
					detalleadenda.setContraprestacion(rentaBean.getContraprestacion());
					
					detalleadenda.setMontopagar(rentaBean.getMontopagar());
					detalleadenda.setRenta(rentaBean.getRenta());
					detalleadenda.setNumeromes(Integer.parseInt(Almanaque.mesanumero(rentaBean.getMes())));
					detalleadenda.setEstado(Constante.ESTADO_GENERADO);
					detalleadenda.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					detalleadenda.setFeccre(new Date());
					detalleadenda.setHost_ip_usrcre(host_ip);
					
					detalleadendas.add(detalleadenda);
					
					MaeDetalleCondicion maeDetalleCondicion = new MaeDetalleCondicion();
					
					maeDetalleCondicion.setAnio(rentaBean.getAnio());
					maeDetalleCondicion.setMes(rentaBean.getMes());
					maeDetalleCondicion.setContrato(c);
					
					maeDetalleCondicion.setSiclausulapagoposterior(listaRentas.get(0).getSiclausulapagoposterior());
					maeDetalleCondicion.setMontopagoposterior(listaRentas.get(0).getMontopagoposterior());
					maeDetalleCondicion.setObservacionpagoposterior(listaRentas.get(0).getObservacionpagoposterior());
					
					maeDetalleCondicion.setSiclausulaperiodogracia(listaRentas.get(0).getSiclausulaperiodogracia());
					maeDetalleCondicion.setSiclausulareconocimientoinversion(listaRentas.get(0).getSiclausulareconocimientoinversion());
					
					maeDetalleCondicion.setSipagofraccionrenta(listaRentas.get(0).getSipagofraccionrenta());
					
					maeDetalleCondicion.setSiclausulareconocimientorenta(listaRentas.get(0).getSiclausulareconocimientorenta());
					maeDetalleCondicion.setObservacionreconocimientorenta(listaRentas.get(0).getObservacionreconocimientorenta());
					maeDetalleCondicion.setDescuentoreconocimientorenta(listaRentas.get(0).getDescuentoreconocimientorenta());
					
					
					maeDetalleCondicion.setSiclausulasuspensiontemporalrenta(listaRentas.get(0).getSiclausulasuspensiontemporalrenta());
					maeDetalleCondicion.setObservacionsuspensiontemporalrenta(listaRentas.get(0).getObservacionsuspensiontemporalrenta());
					maeDetalleCondicion.setMontosuspensiontemporalrenta(listaRentas.get(0).getMontosuspensiontemporalrenta());
					
					
					maeDetalleCondicion.setSecuencia(numsecuencia +i);
					
					maeDetalleCondicion.setContraprestacion(rentaBean.getContraprestacion());
					
					maeDetalleCondicion.setMontopagar(rentaBean.getMontopagar());
					maeDetalleCondicion.setRenta(rentaBean.getRenta());
					maeDetalleCondicion.setNumeromes(Integer.parseInt(Almanaque.mesanumero(rentaBean.getMes())));
					
					maeDetalleCondicion.setSirentagenerada(listaRentas.get(0).getSirentagenerada());
					maeDetalleCondicion.setEstado(Constante.ESTADO_GENERADO);
					listaMaeDetalleCondicion.add(maeDetalleCondicion);
					i++;
				}
	             
				
			}else if(contratoBean.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_B)){
				
					Detalleadenda detalleadenda = new Detalleadenda();
					detalleadenda.setAdenda(a);		
					detalleadenda.setEstado(Constante.ESTADO_GENERADO);
					detalleadenda.setMotivodetalle(contratoBean.getMotivodetalleadenda());
					detalleadenda.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
					detalleadenda.setFeccre(new Date());
					detalleadenda.setHost_ip_usrcre(host_ip);					
					detalleadendas.add(detalleadenda);
	               
			}
			 //detalle adenda                
			 a.setDetalleadendas(detalleadendas);
			
			//a.setObservacionmod("");
			if(archivoMB.getListaArchivoAdjunto().size()>0){
				a.setSidocumento(Boolean.TRUE);
				contratoBean.setSidocumento(Boolean.TRUE);
			}
			
			
		
			if (contratoBean.getIdcontrato() != 0) {
				//contratoService.incluirAdendaContrato(a,contratoBean.getIdcontrato(),contratoBean.getObservacionadenda(),FuncionesHelper.fechaToString(finCobro.getTime()),FuncionesHelper.fechaToString(contratoBean.getFincontratoadenda()));
				contratoService.incluirAdendaContrato(a, contratoBean);
				if(contratoBean.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_A)){
					registrarMaeDetalleCondicion(listaMaeDetalleCondicion);
				}				
				insertarDocumentoAdjunto(c);				
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Éxito", "Se Agrego al contrato: "+ contratoBean.getNrocontrato() + " la adenda éxitosamente");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				limpiarRegistroAdenda();
//				limpiarRegistroPropiedades();
//				listContrato.clear();

			}
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				contextRequest.execute("dlgCancelarContrato.hide();");
				//		addInfoMessage("","Se registro con exito la Adenda");
			}catch(Exception e ){
				addErrorMessage("",Constantes.MSG_GENERO_ERROR_OPERACION);
				Logger.error(e.getMessage(),e);
				e.printStackTrace();
		}
	}
	
	public void registrarMaeDetalleCondicion(List<MaeDetalleCondicion> lista) throws Exception{
		try{
		for(MaeDetalleCondicion maedetalle : lista){
			contratoService.registrarMaeDetalleCondicion(maedetalle);
		}
		}catch(Exception e){
			e.printStackTrace();
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
		//por finalizar Precontrato y Contrato
		//listaMaeDetalleCondicion.clear();
		//cuotasPendientes.clear();

	}
	public void limpiarRegistroAdenda(){
		
		contratoBean.setObservacionadenda("");
		archivoMB.limpiarDocumentoADjuntos();
		contratoBean.setInicioAdendaContrato(null);
		contratoBean.setFinAdendaContrato(null);
		anioInicioCobroAdenda="";
		mesInicioCobroAdenda="";
		anioFinCobroAdenda="";
		mesFinCobroAdenda="";
		contratoBean.setRentaMensualAdenda(null);
		listaRentasAdenda.clear();
		isCompletoCondicionAdenda=false;
		contratoBean.setMotivodetalleadenda("");
		contratoBean.setTipocondicionadenda(null);
		
	}
	public void activarTipoAlquiler(){
		
		if (contratoBean.getTipocontrato()!=null && contratoBean.getTipocontrato().equals(Constantes.CONTRATO_TIPO_ALQUILER)) {
			sitipocontratoalquiler=Boolean.TRUE;
		} else {
			sitipocontratoalquiler=Boolean.FALSE;
		}
		
	}

	public void ActivarDesactivarSiOcupante() {
		
		if (!contratoBean.getSiocupante()) {
			contratoBean.setSiocupante(false);
		} else {
			contratoBean.setSiocupante(true);
		}
	
	}

	public void ActivarDesactivarSifechaSuscrito() {
		if (contratoBean.getSisuscrito()) {
			contratoBean.setSisuscrito(true);
		} else {
			contratoBean.setSisuscrito(false);
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

	public void cambiarDolarSoles() {
		if (sidolar) {
			sisoles = true;
			sidolar = false;
		} else {
			sidolar = false;
		}
	}

    public void validarCondicionContrato(){
    	RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			if(contratoBean.getSifinalizado()){
				contextFaces.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Este contrato ya se encuentra en condicion de Finalizado"));
			}else{
				//TRAER RENTAS PENDIENTES
				//cuotasPendientes= new ArrayList<CuotaBean>();
				//obtenerInformacionCuotasPendientes();
				contextRequest.execute("dlgFinalizarContrato.show();");
			}
			
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione el contrato a resolver"));
		}
    }
	public void validarCancelacionContrato() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			if(contratoBean.getSifinalizado()){
				contextFaces.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Este contrato ya se encuentra en condicion de Finalizado"));
			}else{
				contextRequest.execute("dlgResolverContrato.show();");
			}
			
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione el contrato a resolver"));
		}

	}
	public void validarAnularContrato() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			//METODO VERIFICADO SI TIENE PAGOS 
			boolean res= recaudacionReporteService.siRentasPagadasxContrato(contratoBean.getIdcontrato());
			if(!res){
				if(contratoBean.getEstado().equalsIgnoreCase(Constante.CONDICION_CONTRATO_ANULADO)){
					contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Este Precontrato ya se encuentra en condicion de Anulado"));
				}else{
					contextRequest.execute("dlgAnularContrato.show();");
				}

			}else{
				contextFaces.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Este Precontrato contiene aun pagos realizados , se pide que se anule los pagos para continuar con el proceso"));
			}
			
//			if(contratoBean.getSifinalizado()){
//				contextFaces.addMessage(null, new FacesMessage(
//						FacesMessage.SEVERITY_WARN, "Advertencia",
//						"Este contrato ya se encuentra en condicion de Finalizado"));
//			}else{
//				contextRequest.execute("dlgResolverContrato.show();");
//			}
			
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione el Precontrato para su Anulación"));
		}

	}
	public void validarInclusionAdenda() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			contextRequest.execute("dlgIncluirAdenda.show();");
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione el contrato a incluir adenda"));
		}

	}
	


	
	public List<Condicionincumplimientoclausula> getListaCondicionIncumplimiento() {
		return listaCondicionIncumplimiento;
	}

	public void setListaCondicionIncumplimiento(
			List<Condicionincumplimientoclausula> listaCondicionIncumplimiento) {
		this.listaCondicionIncumplimiento = listaCondicionIncumplimiento;
	}

	/**Cambiar estado precontrato a contrato**/
	
	
	public void validarCambiarEstadoCondicion(){
		
		if (StringUtils.isEmpty(numContratoCambiarEstadoCondicion)) {
			
			addWarnMessage("", "Ingrese número de contrato para continuar.");
		} else {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmCambiarEstadoCondicion.show();");
		}
		
	} 
	
	public void limpiarCambiarEstadoCondicion(){
		
		this.numContratoCambiarEstadoCondicion="";
		
	}
	
	public void grabarCambiarEstadoCondicion(){
		
		contratoService.cambiarCondicionContrato(Constantes.CONDICION_CONTRATO, contratoBean.getIdcontrato(),numContratoCambiarEstadoCondicion);
		addInfoMessage("", "Se actualizó condición de precontrato exitosamente.");
		limpiarRegistroPropiedades();
	}
	public void cargarInformacionPeriodosxCondicion(){
		Integer tipoincremento=0;

		tipoincremento= contratoBean.getSiincrementoporcentajevariable()==true? Constante.TIPO_INCREMENTO_VARIABLE_PORCENTAJE :Constante.TIPO_INCREMENTO_VARIABLE_MONTO;
        contratoBean.setNombretipoincremento(contratoBean.getSiincrementoporcentajevariable()==true? Constante.TIPO_INCREMENTO_VARIABLE_1 :Constante.TIPO_INCREMENTO_VARIABLE_2);
		contratoBean.setTipoincremento(tipoincremento);
               
	}
	public void cancelarDatosIncrementoVariable(){
		nroPeriodosxInteres=0;
		listaperiodos.clear();
		periodoContratoBean = new PeriodoContratoBean();
		periodoContratoBean.setMonto(0.0);
		disabledButtonAgregarPeriodo=false;
		disabledValorIncremento=false;
		//minFechaInicio=FuncionesHelper.ponerDiasFechaInicioAnio(new Date());
		minFechaInicio = FuncionesHelper.ponerDiasFechaInicioAnio(FuncionesHelper.sumarRestarAnios(new Date(),-1));

	}
	public void validarNumeroPeriodos(){
		listaperiodos.clear();
		periodoContratoBean = new PeriodoContratoBean();
		periodoContratoBean.setMonto(0.0);
		disabledButtonAgregarPeriodo=false;
		disabledValorIncremento=false;
		minFechaInicio = FuncionesHelper.ponerDiasFechaInicioAnio(FuncionesHelper.sumarRestarAnios(new Date(),-1));
		if(nroPeriodosxInteres!=null){
			if(nroPeriodosxInteres>0){
				// abrir ventana
				indicadorPeriodo=0;
				cargarPeriodosIncremento();
			}else{
				System.out.println("valor ingresado menor o igual  a cero ");
			}
		}else{
			System.out.println("valor null!!");
		}
	}
	public void cargarPeriodosIncremento(){
		
		indicadorPeriodo++;
	//	System.out.println("indicadorPeriodo="+indicadorPeriodo);
		if((nroPeriodosxInteres-indicadorPeriodo)>=0){
			if((nroPeriodosxInteres-indicadorPeriodo)==0 && contratoBean.getSiincrementoporcentajevariable()){
				disabledValorIncremento=true;
				System.out.println("disabledValorIncremento="+disabledValorIncremento);
			}
			
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("iddlgGenerarPeriodo.show();");
			
		}else{
			disabledButtonAgregarPeriodo=true;
			nroPeriodosxInteres=0;
		}
		
	}
	public boolean validarValorIncremento(){
		boolean resp=false;
		if(contratoBean.getSiincrementoporcentajevariable()){
			if((nroPeriodosxInteres-indicadorPeriodo)>0){
				if(periodoContratoBean.getValorincremento()<=0){
					resp=true;
				}
			}else{
				resp=false;
			}
      	}else{
			resp=false;
		}
    	return resp;
	}
	public void validarPeriodoIncremento(){
//		if(contratoBean.getSiincrementoporcentajevariable()){
		   System.out.println("periodoContratoBean.getFechainicioperiodo()= "+periodoContratoBean.getFechainicioperiodo());
			if(periodoContratoBean.getMonto()<=0){
				addWarnMessage("", "Ingrese un monto mayor a cero para el periodo");
			}else if (numAniosPeriodo==0 && numMesesPeriodo==0) {		
				addWarnMessage("", "Ingrese n\u00famero de años y/o meses del periodo.");		
			}else if (periodoContratoBean.getFechainicioperiodo()==null){
				addWarnMessage("", "Ingrese la fecha de inicio del periodo");
			}else if (periodoContratoBean.getFechafinperiodo()==null){
				addWarnMessage("", "Ingrese la fecha de fin del periodo");
			}else if(validarValorIncremento()){
				addWarnMessage("", "Ingrese el valor de incremento para el periodo");
			}else{
			
				generarPeriodosIncremento();			
				cargarPeriodosIncremento();
			}
//		}
//		if(contratoBean.getSiincrementomontovariable()){
//			generarPeriodosIncremento();
//			cargarPeriodosIncremento();
//		}
	}
	public void generarPeriodosIncremento(){
     PeriodoContratoBean periodocontratoBean= new PeriodoContratoBean();
     periodocontratoBean.setIdcontrato(contratoBean.getIdcontrato());
     periodocontratoBean.setMonto(periodoContratoBean.getMonto());
     periodocontratoBean.setFechainicioperiodo(periodoContratoBean.getFechainicioperiodo());
     periodocontratoBean.setFechafinperiodo(periodoContratoBean.getFechafinperiodo());
     periodocontratoBean.setNombretipoincremento(contratoBean.getNombretipoincremento());
     periodocontratoBean.setTipoincremento(contratoBean.getTipoincremento());
     periodocontratoBean.setValorincremento(periodoContratoBean.getValorincremento());
     periodocontratoBean.setNumerocuotas(numAniosPeriodo*12+numMesesPeriodo);
     periodocontratoBean.setPeriodo(indicadorPeriodo);
     periodocontratoBean.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
     periodocontratoBean.setFechacreacion(new Date());
     periodocontratoBean.setEstado(true);
     periodocontratoBean.setCondicion(Constante.ESTADO_CONDICION_ACT);
     
     listaperiodos.add(periodocontratoBean);
//      for(PeriodoContratoBean p :listaperiodos) {
//    	  System.out.println(p.toString());
//      }
      limpiarVariablesPeriodos();
      minFechaInicio =new Date();
      minFechaInicio=FuncionesHelper.ponerDiasFechaInicio(minFechaInicio,(int)FuncionesHelper.diasEntreDosFechas(new Date(),periodocontratoBean.getFechafinperiodo())+1);
      if(contratoBean.getSiincrementoporcentajevariable()){
    	  if(disabledValorIncremento){
    		  periodoContratoBean.setMonto(0.0);
    	  }else{
        	  periodoContratoBean.setMonto(FuncionesHelper.redondearNum(periodocontratoBean.getMonto()*(1+ periodocontratoBean.getValorincremento()/100),2));
  
    	  }
      }
		
	}
	public void validarDetalleCondicionAdenda(){
		if(contratoBean.getMotivodetalleadenda()!=null){
			if(contratoBean.getMotivodetalleadenda().trim().isEmpty()){
//				addWarnMessage("", "Ingrese detalle de la adenda");
				isCompletoCondicionAdenda=Boolean.FALSE;
			}else{
				isCompletoCondicionAdenda=Boolean.TRUE;
			}
		}else{
			isCompletoCondicionAdenda=Boolean.FALSE;
		}
	}
    public void limpiarVariablesPeriodos(){
    	periodoContratoBean = new PeriodoContratoBean();
    	periodoContratoBean.setMonto(0.0);
    	numAniosPeriodo=0;
    	numMesesPeriodo=0;
    	siexistenumMesAnio=true;
    }
	
	public void cambiarIncrementoxMonto(){
		if(contratoBean.getSiincrementomontovariable()){
			contratoBean.setSiincrementomontovariable(true);
    		contratoBean.setSiincrementoporcentajevariable(false);
    		
    	}else{
    		contratoBean.setSiincrementomontovariable(false);
    		contratoBean.setSiincrementoporcentajevariable(true);
    	}
		cargarInformacionPeriodosxCondicion();
	}
	
	public void cambiarIncrementoxPorcentaje(){
		if(contratoBean.getSiincrementoporcentajevariable()){
    		contratoBean.setSiincrementoporcentajevariable(true);
    		contratoBean.setSiincrementomontovariable(false);
    	}else{    		
    		contratoBean.setSiincrementoporcentajevariable(false);
    		contratoBean.setSiincrementomontovariable(true);
    	}
		cargarInformacionPeriodosxCondicion();
	}
	public void evaluarCondicionContrato(){
		if(contratoBean!=null){
		 if(!contratoBean.getSinuevomantenimiento()){
			addWarnMessage("", "El contrato no tiene habilitado la opcion de Ampliar plazo");
		 }
		}
	}
	
	public void obtenerInformacionCuotasPendientes(){
		Double tipcambio=0.0;
		if (contratoBean != null) {
			//inicializarSumaTotalCuotasSeleccion();
			//listadependientes();
			if (contratoBean.getSinuevomantenimiento() != null && contratoBean.getSinuevomantenimiento()) {
				                                   // nuevo contrato y precontrato
                	cuotasPendientes = recaudacionReporteService.generarRentasPendientesParaCobroNuevoContrato(contratoBean.getIdcontrato(), tipcambio);
                
			} 
		}
	}
	public void validarRegistroClausulas(){
		try{
			if(contratoBean.getIdfechapago()==null || contratoBean.getIdfechapago()==0 ){
				
				addWarnMessage("","Seleccione el dia de cobro del contrato.");

			}else if(listaCondicionIncumplimiento.size()==0){
				addWarnMessage("","Debe añadir una clausula a la tabla de condiciones .");

			}else{

			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarClausulaContrato.show();");
			
			}	
			
		}catch(Exception e){
			e.printStackTrace();		}
	}
	public void validarEstadoClausulasContrato(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();
//		Contrato c = contratoService.obtenerContratoPorID(id);
//		if(c!=null){
//			if(contratoBean.getSicondicionclausula()!=null && )
//			if(contratoBean.getSicondicionclausula()!=null){
//				if(contratoBean.getSicondicionclausula()){
//						listaCondicionIncumplimiento= contratoService.listarCondicionIncumplimiento(contratoBean.getIdcontrato());
//				}
//			}
//		}
		
		listaDiaPago = contratoService.listarFechaPago();
		listaPenalidades = contratoService.listarDetalleTipoClausula("02");
		if(contratoBean.getSicondicionclausula()==null || !contratoBean.getSicondicionclausula()){
			listaCondicionIncumplimiento = new ArrayList<Condicionincumplimientoclausula>();
		}
		
		if (contratoBean.getIdcontrato() != 0) {			
			contextRequest.execute("dlgEstadoClausulasContrato.show();");
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione un contrato "));
		}
	}
	
	public Condicionincumplimientoclausula AddCondicion(String condicion, String opc){
		Condicionincumplimientoclausula obj = new Condicionincumplimientoclausula();
		if(condicion.equalsIgnoreCase("01")){
			condicionCumplimiento.setValor_porcentaje(null);
			condicionCumplimiento.setEjecucion_periodica(Constante.FORMA_PERIODICA_01);
			Detalleclausula det= new Detalleclausula();
			det=contratoService.getDetalleClausula(opc);
			obj.setDetalleclausula(det) ;
			
		}else {
			Detalleclausula det= new Detalleclausula();
			det=contratoService.getDetalleClausula(opc);
			obj.setDetalleclausula(det);
		}
		obj.setValor_porcentaje(condicionCumplimiento.getValor_porcentaje());
		obj.setEjecucion_periodica(condicionCumplimiento.getEjecucion_periodica());
		obj.setUsrcre(userMB.getUsuariologueado().getNombreusr());
		obj.setFeccre(new Date());
		obj.setContrato(new Contrato(contratoBean.getIdcontrato()));
		obj.setEstado(true);
		return obj;
	}
	public void limpiarListaCondicion(){
		try{
		if(contratoBean.getSiinteresmoratorio()!=null){
			if(!contratoBean.getSiinteresmoratorio()){
				listaCondicionIncumplimiento=eliminarElementosListaCondicion(listaCondicionIncumplimiento , 0 , "01");
				tipoInteresMoratorio=null;
			}
		}
		if(contratoBean.getSipenalidad()!=null){
				if(!contratoBean.getSipenalidad()){
					listaCondicionIncumplimiento=eliminarElementosListaCondicion(listaCondicionIncumplimiento , 0 , "02");
				}
		}}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public List<Condicionincumplimientoclausula> eliminarElementosListaCondicion(List<Condicionincumplimientoclausula> lista , Integer idcond , String tipoclausula){
		try{
			Iterator<Condicionincumplimientoclausula> iter = lista.iterator();
			while (iter.hasNext()) {
				Condicionincumplimientoclausula cond = iter.next();
				if (idcond != null && idcond > 0) {
					if (cond.getId_condicion_incumplimiento() == idcond) {
						iter.remove();
					}
				}else {
					if(idcond==0 && tipoclausula!=null){
							if(cond.getDetalleclausula().getTipoclausula().getId_tc().equalsIgnoreCase(tipoclausula)){
								iter.remove();
								}
						
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();		}
		return lista;
	}
	public void agregarClausulaxCondicion(String condicion, String opc){
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		if(condicion.equalsIgnoreCase("01")){//interes moratorio
			//agregar a tabla 		
				listaCondicionIncumplimiento=eliminarElementosListaCondicion(listaCondicionIncumplimiento , 0 , condicion);
				listaCondicionIncumplimiento.add(AddCondicion(condicion,opc));
			
		}else{
			if(condicionCumplimiento.getValor_porcentaje()>0){
				listaCondicionIncumplimiento.add(AddCondicion(condicion,opc));	
			}else{
				contextFaces.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Ingrese el valor de porcentaje "));
			}
			
			     		
		}
	}
	public Boolean verificarItemCondicion(String id){
		Boolean resp=false;
		if(listaCondicionIncumplimiento!=null && listaCondicionIncumplimiento.size()>0){
		 for(Condicionincumplimientoclausula cond :listaCondicionIncumplimiento){
			 if(cond.getDetalleclausula().getId_detalle().equalsIgnoreCase(id)){
				 resp=true;
				  break;
			 }
		 }
		}
		return resp;
	}
	public void agregarInformacionClausulaPenalidad(String id){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();
        if(!verificarItemCondicion(id)){
        	condicionCumplimiento= new Condicionincumplimientoclausula();
    		condicionCumplimiento.setDetalleclausula(contratoService.getDetalleClausula(id));
    		detalleclausula= contratoService.getDetalleClausula(id);
    		contextRequest.execute("dlgAgregarDatosPenalidad.show();");
        }else{
        	contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Ya se tiene esa condicion en la tabla "));
        }
		
		
	}
	public void eliminarItemClausulaxCondicion(Detalleclausula de){    
        try{
			Iterator<Condicionincumplimientoclausula> iter = listaCondicionIncumplimiento.iterator();
			while (iter.hasNext()) {
				Condicionincumplimientoclausula cond = iter.next();
				if (cond.getDetalleclausula().getId_detalle().equalsIgnoreCase(de.getId_detalle())){
					iter.remove();
					if(cond.getDetalleclausula().getTipoclausula().getId_tc().equalsIgnoreCase("01")){
						tipoInteresMoratorio=null;
					}
				}
			}
    	}catch(Exception e){
			e.printStackTrace();
    	}
	}
	 public void grabarClausulaContrato(){
        try{
        	//grabar en tabla condicion clausulas
        	for (Condicionincumplimientoclausula clausula  : listaCondicionIncumplimiento) {
        		contratoService.registrarClausulaContrato(clausula);
			}
        	//actualizar contrato
        	  Contrato c = new Contrato();
        	  c= contratoService.obtenerContratoPorID(contratoBean.getIdcontrato());
        	  c.setSicondicionclausula(Boolean.TRUE);
        	  c.setSipenalidad(contratoBean.getSipenalidad());
        	  c.setSiinteresmoratorio(contratoBean.getSiinteresmoratorio());
        	  c.setFechapago(new Fechapago(contratoBean.getIdfechapago()));
        	  contratoService.registrarContrato(c);
        	  //actualizar
        	  contratoBean.setSicondicionclausula(c.getSicondicionclausula());
        }catch(Exception e){
        	e.printStackTrace();
        }
		 addInfoMessage("", "Se registro con éxito las clauslas del contrato ");
		 
	 }
	
	public IContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
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
	public List<Inquilino> getListInquilinoFiltro() {
		return listInquilinoFiltro;
	}
	public void setListInquilinoFiltro(List<Inquilino> listInquilinoFiltro) {
		this.listInquilinoFiltro = listInquilinoFiltro;
	}

	public String getNombreOcupante() {
		return nombreOcupante;
	}
	public void setNombreOcupante(String nombreOcupante) {
		this.nombreOcupante = nombreOcupante;
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
	public List<Institucion> getListInstitucion() {
		return listInstitucion;
	}
	public void setListInstitucion(List<Institucion> listInstitucion) {
		this.listInstitucion = listInstitucion;
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
	public List<Representante> getListRepresentante() {
		return listRepresentante;
	}
	public void setListRepresentante(List<Representante> listRepresentante) {
		this.listRepresentante = listRepresentante;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<Cliente> getListCliente() {
		return listCliente;
	}
	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}
	public Representante getRepresentanteaval() {
		return representanteaval;
	}
	public void setRepresentanteaval(Representante representanteaval) {
		this.representanteaval = representanteaval;
	}
	public Usuario getSelectRegistroUsuario() {
		return selectRegistroUsuario;
	}
	public void setSelectRegistroUsuario(Usuario selectRegistroUsuario) {
		this.selectRegistroUsuario = selectRegistroUsuario;
	}
	public List<Usuario> getListadousuariosSeleccionados() {
		return listadousuariosSeleccionados;
	}
	public void setListadousuariosSeleccionados(
			List<Usuario> listadousuariosSeleccionados) {
		this.listadousuariosSeleccionados = listadousuariosSeleccionados;
	}
	public int getIndicesalvador() {
		return indicesalvador;
	}
	public void setIndicesalvador(int indicesalvador) {
		this.indicesalvador = indicesalvador;
	}
	public int getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}
	public void setIdUsuarioSeleccionado(int idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
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
	public List<Cuentabancaria> getListCtasBancarias() {
		return listCtasBancarias;
	}
	public void setListCtasBancarias(List<Cuentabancaria> listCtasBancarias) {
		this.listCtasBancarias = listCtasBancarias;
	}
	public Cuentabancaria getCtabancaria() {
		return ctabancaria;
	}
	public void setCtabancaria(Cuentabancaria ctabancaria) {
		this.ctabancaria = ctabancaria;
	}
	public List<Integer> getListaAnho() {
		listaAnho = new ArrayList<Integer>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		int anhoActual = Integer.parseInt(dateFormat.format(new Date()));

		for (int i = anhoActual; i >= 1950; i--) {
			// for(int i=1950;i<=anhoActual;i++){
			listaAnho.add(i);
		}

		return listaAnho;
	}

	public void setListaAnho(List<Integer> listaAnho) {
		this.listaAnho = listaAnho;
	}
	public void messageFacesPrederminado(String titulo, String mensaje,
			Severity severityFatal) {
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		contextFaces.addMessage(null, new FacesMessage(severityFatal, titulo,
				mensaje));
	}

	public String obtenerExtension(String nombreArchivo) {
		int tamanioCadena, posicionCaracter;
		String extension;

		tamanioCadena = nombreArchivo.length();
		posicionCaracter = nombreArchivo.lastIndexOf(".");
		extension = nombreArchivo.substring(posicionCaracter, tamanioCadena);

		return extension.toLowerCase();
	}
	public List<pe.gob.sblm.sgi.entity.Cuota> getListacuotas() {
		return listacuotas;
	}
	public void setListacuotas(List<pe.gob.sblm.sgi.entity.Cuota> listacuotas) {
		this.listacuotas = listacuotas;
	}
	public List<Detallecuota> getListadetallecuota() {
		return listadetallecuota;
	}
	public void setListadetallecuota(List<Detallecuota> listadetallecuota) {
		this.listadetallecuota = listadetallecuota;
	}
	public Cuota getCuota() {
		return cuota;
	}
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	public IUpaService getUpaService() {
		return upaService;
	}
	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public IInquilinoService getInquilinoService() {
		return inquilinoService;
	}
	public void setInquilinoService(IInquilinoService inquilinoService) {
		this.inquilinoService = inquilinoService;
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
	public Boolean getDisabledFinCobro() {
		return disabledFinCobro;
	}
	public void setDisabledFinCobro(Boolean disabledFinCobro) {
		this.disabledFinCobro = disabledFinCobro;
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
	public List<RentaBean> getListaRentas() {
		return listaRentas;
	}
	public void setListaRentas(List<RentaBean> listaRentas) {
		this.listaRentas = listaRentas;
	}
	public RentaBean getSelectedRenta() {
		return selectedRenta;
	}
	public void setSelectedRenta(RentaBean selectedRenta) {
		this.selectedRenta = selectedRenta;
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
	public CondicionBean getContratoBean() {
		return contratoBean;
	}
	public void setContratoBean(CondicionBean contratoBean) {
		this.contratoBean = contratoBean;
	}
	public String getActiveIndexTabView() {
		return activeIndexTabView;
	}
	public void setActiveIndexTabView(String activeIndexTabView) {
		this.activeIndexTabView = activeIndexTabView;
	}
	public Boolean getSicontratofuturo() {
		return sicontratofuturo;
	}
	public void setSicontratofuturo(Boolean sicontratofuturo) {
		this.sicontratofuturo = sicontratofuturo;
	}
	public Boolean getSitipocontratoalquiler() {
		return sitipocontratoalquiler;
	}
	public void setSitipocontratoalquiler(Boolean sitipocontratoalquiler) {
		this.sitipocontratoalquiler = sitipocontratoalquiler;
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
	public IpcService getIpcService() {
		return ipcService;
	}
	public void setIpcService(IpcService ipcService) {
		this.ipcService = ipcService;
	}
	public List<RentaBean> getListaRentaPagoPosterior() {
		return listaRentaPagoPosterior;
	}
	public void setListaRentaPagoPosterior(List<RentaBean> listaRentaPagoPosterior) {
		this.listaRentaPagoPosterior = listaRentaPagoPosterior;
	}
	public Integer getSecuenciaRentaPagoPosterior() {
		return secuenciaRentaPagoPosterior;
	}
	public void setSecuenciaRentaPagoPosterior(Integer secuenciaRentaPagoPosterior) {
		this.secuenciaRentaPagoPosterior = secuenciaRentaPagoPosterior;
	}
	public Double getRentaPagoPosterior() {
		return rentaPagoPosterior;
	}
	public void setRentaPagoPosterior(Double rentaPagoPosterior) {
		this.rentaPagoPosterior = rentaPagoPosterior;
	}
	public Double getRentaPagoPosteriorSelected() {
		return rentaPagoPosteriorSelected;
	}
	public void setRentaPagoPosteriorSelected(Double rentaPagoPosteriorSelected) {
		this.rentaPagoPosteriorSelected = rentaPagoPosteriorSelected;
	}
	public String getObservacionPagoPosterior() {
		return observacionPagoPosterior;
	}
	public void setObservacionPagoPosterior(String observacionPagoPosterior) {
		this.observacionPagoPosterior = observacionPagoPosterior;
	}
	public String getEstado() {
		return estado;
	}	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNumContratoCambiarEstadoCondicion() {
		return numContratoCambiarEstadoCondicion;
	}
	public void setNumContratoCambiarEstadoCondicion(String numContratoCambiarEstadoCondicion) {
		this.numContratoCambiarEstadoCondicion = numContratoCambiarEstadoCondicion;
	}

	public List<String> getListaOrigenSupensionTemporalRenta() {
		return listaOrigenSupensionTemporalRenta;
	}


	public void setListaOrigenSupensionTemporalRenta(List<String> listaOrigenSupensionTemporalRenta) {
		this.listaOrigenSupensionTemporalRenta = listaOrigenSupensionTemporalRenta;
	}


	public IRecaudacionReporteService getReporteRecaudacionService() {
		return reporteRecaudacionService;
	}


	public void setReporteRecaudacionService(
			IRecaudacionReporteService reporteRecaudacionService) {
		this.reporteRecaudacionService = reporteRecaudacionService;
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


	public int getNroadenda_ant() {
		return nroadenda_ant;
	}


	public void setNroadenda_ant(int nroadenda_ant) {
		this.nroadenda_ant = nroadenda_ant;
	}


	public ArchivoManagedBean getArchivoMB() {
		return archivoMB;
	}


	public void setArchivoMB(ArchivoManagedBean archivoMB) {
		this.archivoMB = archivoMB;
	}

	public Integer getNroPeriodosxInteres() {
		return nroPeriodosxInteres;
	}


	public void setNroPeriodosxInteres(Integer nroPeriodosxInteres) {
		this.nroPeriodosxInteres = nroPeriodosxInteres;
	}


	public int getIndicadorPeriodo() {
		return indicadorPeriodo;
	}


	public void setIndicadorPeriodo(int indicadorPeriodo) {
		this.indicadorPeriodo = indicadorPeriodo;
	}


	public List<PeriodoContratoBean> getListaperiodos() {
		return listaperiodos;
	}


	public void setListaperiodos(List<PeriodoContratoBean> listaperiodos) {
		this.listaperiodos = listaperiodos;
	}


	public Integer getNumAniosPeriodo() {
		return numAniosPeriodo;
	}


	public void setNumAniosPeriodo(Integer numAniosPeriodo) {
		this.numAniosPeriodo = numAniosPeriodo;
	}


	public Integer getNumMesesPeriodo() {
		return numMesesPeriodo;
	}


	public void setNumMesesPeriodo(Integer numMesesPeriodo) {
		this.numMesesPeriodo = numMesesPeriodo;
	}


	public PeriodoContratoBean getPeriodoContratoBean() {
		return periodoContratoBean;
	}


	public void setPeriodoContratoBean(PeriodoContratoBean periodoContratoBean) {
		this.periodoContratoBean = periodoContratoBean;
	}

	public Boolean getDisabledButtonAgregarPeriodo() {
		return disabledButtonAgregarPeriodo;
	}

	public void setDisabledButtonAgregarPeriodo(Boolean disabledButtonAgregarPeriodo) {
		this.disabledButtonAgregarPeriodo = disabledButtonAgregarPeriodo;
	}

	public Boolean getDisabledValorIncremento() {
		return disabledValorIncremento;
	}

	public void setDisabledValorIncremento(Boolean disabledValorIncremento) {
		this.disabledValorIncremento = disabledValorIncremento;
	}


	public Date getMinFechaInicio() {
		return minFechaInicio;
	}

	public void setMinFechaInicio(Date minFechaInicio) {
		this.minFechaInicio = minFechaInicio;
	}

	public boolean isSiexistenumMesAnio() {
		return siexistenumMesAnio;
	}

	public void setSiexistenumMesAnio(boolean siexistenumMesAnio) {
		this.siexistenumMesAnio = siexistenumMesAnio;
	}

	public String getMesInicioCobroAdenda() {
		return mesInicioCobroAdenda;
	}

	public void setMesInicioCobroAdenda(String mesInicioCobroAdenda) {
		this.mesInicioCobroAdenda = mesInicioCobroAdenda;
	}

	public String getAnioInicioCobroAdenda() {
		return anioInicioCobroAdenda;
	}

	public void setAnioInicioCobroAdenda(String anioInicioCobroAdenda) {
		this.anioInicioCobroAdenda = anioInicioCobroAdenda;
	}

	public Integer getNumAniosAdendaContrato() {
		return numAniosAdendaContrato;
	}

	public void setNumAniosAdendaContrato(Integer numAniosAdendaContrato) {
		this.numAniosAdendaContrato = numAniosAdendaContrato;
	}

	public Integer getNumMesesAdendaContrato() {
		return numMesesAdendaContrato;
	}

	public void setNumMesesAdendaContrato(Integer numMesesAdendaContrato) {
		this.numMesesAdendaContrato = numMesesAdendaContrato;
	}

	public boolean isSiexistenumMesAnioAdenda() {
		return siexistenumMesAnioAdenda;
	}

	public void setSiexistenumMesAnioAdenda(boolean siexistenumMesAnioAdenda) {
		this.siexistenumMesAnioAdenda = siexistenumMesAnioAdenda;
	}

	public List<RentaBean> getListaRentasAdenda() {
		return listaRentasAdenda;
	}

	public void setListaRentasAdenda(List<RentaBean> listaRentasAdenda) {
		this.listaRentasAdenda = listaRentasAdenda;
	}

	public RentaBean getSelectedRentaAdenda() {
		return selectedRentaAdenda;
	}

	public void setSelectedRentaAdenda(RentaBean selectedRentaAdenda) {
		this.selectedRentaAdenda = selectedRentaAdenda;
	}

	public String getAnioFinCobroFinalizado() {
		return anioFinCobroFinalizado;
	}

	public void setAnioFinCobroFinalizado(String anioFinCobroFinalizado) {
		this.anioFinCobroFinalizado = anioFinCobroFinalizado;
	}

	public String getMesFinCobroFinalizado() {
		return mesFinCobroFinalizado;
	}

	public void setMesFinCobroFinalizado(String mesFinCobroFinalizado) {
		this.mesFinCobroFinalizado = mesFinCobroFinalizado;
	}

	public ArrendamientoConsultaManagedBean getArrendamientoConsultaMB() {
		return arrendamientoConsultaMB;
	}

	public void setArrendamientoConsultaMB(
			ArrendamientoConsultaManagedBean arrendamientoConsultaMB) {
		this.arrendamientoConsultaMB = arrendamientoConsultaMB;
	}

	public List<CuotaBean> getCuotasPendientesSeleccion() {
		return cuotasPendientesSeleccion;
	}

	public void setCuotasPendientesSeleccion(
			List<CuotaBean> cuotasPendientesSeleccion) {
		this.cuotasPendientesSeleccion = cuotasPendientesSeleccion;
	}

	public List<CuotaBean> getCuotasPendientesFilter() {
		return cuotasPendientesFilter;
	}

	public void setCuotasPendientesFilter(List<CuotaBean> cuotasPendientesFilter) {
		this.cuotasPendientesFilter = cuotasPendientesFilter;
	}

	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}

	public void setRecaudacionReporteService(
			IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}

	public List<CuotaBean> getCuotasPendientes() {
		return cuotasPendientes;
	}

	public void setCuotasPendientes(List<CuotaBean> cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	public CuotaBean getCuotaPendienteCondicion() {
		return cuotaPendienteCondicion;
	}

	public void setCuotaPendienteCondicion(CuotaBean cuotaPendienteCondicion) {
		this.cuotaPendienteCondicion = cuotaPendienteCondicion;
	}

	public List<MaeDetalleCondicion> getListaMaeDetalleCondicion() {
		return listaMaeDetalleCondicion;
	}

	public void setListaMaeDetalleCondicion(
			List<MaeDetalleCondicion> listaMaeDetalleCondicion) {
		this.listaMaeDetalleCondicion = listaMaeDetalleCondicion;
	}

	public List<Fechapago> getListaDiaPago() {
		return listaDiaPago;
	}

	public void setListaDiaPago(List<Fechapago> listaDiaPago) {
		this.listaDiaPago = listaDiaPago;
	}

	public String getTipoInteresMoratorio() {
		return tipoInteresMoratorio;
	}

	public void setTipoInteresMoratorio(String tipoInteresMoratorio) {
		this.tipoInteresMoratorio = tipoInteresMoratorio;
	}

	public List<Detalleclausula> getListaPenalidades() {
		return listaPenalidades;
	}

	public void setListaPenalidades(List<Detalleclausula> listaPenalidades) {
		this.listaPenalidades = listaPenalidades;
	}

	public Condicionincumplimientoclausula getCondicionCumplimiento() {
		return condicionCumplimiento;
	}

	public void setCondicionCumplimiento(
			Condicionincumplimientoclausula condicionCumplimiento) {
		this.condicionCumplimiento = condicionCumplimiento;
	}

	public Detalleclausula getDetalleclausula() {
		return detalleclausula;
	}

	public void setDetalleclausula(Detalleclausula detalleclausula) {
		this.detalleclausula = detalleclausula;
	}

	public Date getFechActual() {
		return fechActual;
	}

	public void setFechActual(Date fechActual) {
		this.fechActual = fechActual;
	}
    
	
	
	
}
