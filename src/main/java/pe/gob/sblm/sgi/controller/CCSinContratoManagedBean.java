package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;

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
* 11/12/2019    Marco  Alarcón 				Adicion de flag de Reconocimiento de deuda. 
*/

@ManagedBean(name = "scMB")
@ViewScoped
public class CCSinContratoManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private RentaBean selectedRenta;
	
	private Usuario selectRegistroUsuario;
	private List<Integer> listaAnho;

	// Objetos
	private CondicionBean contratoBean= new CondicionBean();
	
	private Inquilino inquilino;
	private Upa upa;
	
	// Lista
	private List<Inquilino> listInquilino, listInquilinoFiltro;
	private List<Upa> listUpa, listUpaFiltro;
	private List<CondicionBean> listContrato, listContratoFiltro;

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
	private Boolean disabledInicioCobro=true;
	
	//Pestaña Cuotas
	private List<pe.gob.sblm.sgi.entity.Cuota> listacuotas;
	private List<Detallecuota> listadetallecuota=new ArrayList<Detallecuota>();
	private Cuota cuota= new Cuota();
	
	private String  mesfechaUltimoPago;
	private String aniofechaUltimoPago;
	private String  mesfechaUltimoPagoSinContrato;
	private String aniofechaUltimoPagoSinContrato;
	
	
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;

	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private UploadedFile file;
	private boolean fileCargado;
	private StreamedContent fileDescargar;
	
	
	public void metodo() {
		if (contratoBean.getNumerocuotas()!=0 && contratoBean.getIniciocontrato()!=null) {
			disabledInicioCobro=false;
		}else {
			disabledInicioCobro=true;
		}
	}
	

	
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
		contratoBean.setCuota1(0.0);
		contratoBean.setSireconocimientodeuda(false);
		contratoBean.setSimontoinicialdeuda(false);
		contratoBean.setSiescriturapublica(false);
		sidolar = false;
		sisoles = true;
		tipobusqueda=1;
		tipobusquedaInquilino=Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL;
		tipobusquedaUpa=1;
	}
	
	public void initNewMB() {
		listUpa = new ArrayList<Upa>();
		listInquilino = new ArrayList<Inquilino>();
		todosUsuarios = new ArrayList<Usuario>();
		listadousuariosSeleccionados = new ArrayList<Usuario>();
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

	public void buscarContrato(){
		if (tipobusqueda==1) {
			listContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,"Sin Contrato");
			
		} else if (tipobusqueda==2) {
			listContrato=contratoService.buscarConSinContratoxDireccion(valorbusqueda,"Sin Contrato");
		}else  {
			listContrato=contratoService.buscarConSinContratoxNombreInquilino(valorbusqueda,"Sin Contrato");
		}
	}
	
	/**
	 * Busqueda de upa para vincular con contrato.
	 */	
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
		listInquilino = getInquilinoService().listarInquilinos(valorbusquedaInquilino, tipobusquedaInquilino);
	}
	
	/**
	 * Carga la informacion de contrato al seleccionarlo.
	 */	
	public void setearContratoSeleccionado() {
		
		listaRentas.clear();
		listacuotas=contratoService.obtenerCuotasDeContrato(contratoBean.getIdcontrato());
		
		
		if (contratoBean.getIniciocobro()!=null) {
			Calendar inicio = Calendar.getInstance();
			inicio.setTime(contratoBean.getIniciocobro());
			aniofechaUltimoPago=String.valueOf(inicio.get(Calendar.YEAR));
			mesfechaUltimoPago=Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH));
		}
		
		if (contratoBean.getFincobro()!=null) {
			Calendar fin = Calendar.getInstance();
			fin.setTime(contratoBean.getFincobro());
			aniofechaUltimoPagoSinContrato=String.valueOf(fin.get(Calendar.YEAR));
			mesfechaUltimoPagoSinContrato=Almanaque.obtenerNombreMes(fin.get(Calendar.MONTH));
		}
		

		
		
		if (contratoBean.getMoneda() != null) {
			if (FuncionesHelper.eliminarEspacios(contratoBean.getMoneda()).equals("S")) {
				sisoles = true;
				sidolar = false;
			} else {
				
				sisoles = false;
				sidolar = true;
			}
		}
	
	}

	
	/**
	 * Validacion para  determinar si la upa esta disponible para vincular.
	 */	
	public void seleccionarUpa(SelectEvent event) {
		if(!upa.getSiprocesojudicial()){/*CONDICION DE CLAVE UPA EN PROCESO JUDICIAL*/
		if (contratoService.verificarExisteCondicionVigente(upa.getIdupa())) {
			upa = null;
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar la upa debido a que se encuentra ocupada en una condición vigente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else {
			contratoBean.setIdupa(upa.getIdupa());
			contratoBean.setClaveUpa(upa.getClave());
			contratoBean.setDireccion(upa.getDireccion()+ " "+ upa.getNumprincipal()+" "+upa.getNombrenuminterior());
//			contratoBean.setDireccion(upa.getInmueble().getDireccion()+ " "+ upa.getInmueble().getNumeroprincipal()+" "+upa.getNombrenuminterior());
//			contratoBean.setUso(upa.getUso());
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
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un contrato con el número: "+contratoBean.getNrocontrato()+" registrado en el sistema", "Reingrese el número de contrato, de lo contrario no podra registrar el contrato");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		}	
	}
	

	
	/**
	 * Valida los campos del formulario contrato ante de guardar.
	 */
	public void validarGrabarCabeceraContrato(){
		if(contratoBean.getIdupa()==0){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Seleccionar upa ","Para proceder a registrar el Sin Contrato es obligatorio seleccionar la upa "));
		}else if(contratoBean.getIdinquilino()==0){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Seleccionar Inquilino ","Para proceder a registrar Sin Contrato es obligatorio seleccionar el inquilino "));
		}else if (mesfechaUltimoPago.equals("")){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ingresar mes del ultimo mes cobro de contrato","Para proceder complete lo indicado"));
		}else if (aniofechaUltimoPago.equals("")){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ingresar año del ultimo año de cobro de contrato","Para proceder complete lo indicado"));
		}else if (contratoBean.getCuota1()==0.0) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ingresar renta","Ingrese la renta del sin contrato para continuar"));
		}else if (contratoBean.getUsoespecifico().equals("")) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Ingresar uso especifico","Ingrese uso especifico del sin contrato para continuar"));
		}else {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarContrato.show();");
		}
//		if (contratoBean.getIdupa()!=0 && contratoBean.getIdinquilino()!=0 && !mesfechaUltimoPago.equals("") && !aniofechaUltimoPago.equals("")) {
//			RequestContext context = RequestContext.getCurrentInstance();  
//			context.execute("dlgRegistrarContrato.show();");
//			
//		} else if (mesfechaUltimoPago.equals("")|| aniofechaUltimoPago.equals("")){
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage("Ingresar mes o año de ultimo mes cobro de contrato","Para proceder complete lo indicado"));
//		}else if (contratoBean.getCuota1()==0.0) {
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage("Ingresar renta","Ingrese la renta del sin contrato para continuar"));
//		}else if (contratoBean.getUsoespecifico().equals("")) {
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage("Ingresar uso especifico","Ingrese uso especifico del sin contrato para continuar"));
//		}else {
//			if (upa.getIdupa()==0 || inquilino.getIdinquilino()==0) {
//				FacesContext context = FacesContext.getCurrentInstance();
//				context.addMessage(null, new FacesMessage("Seleccionar upa o inquilino ","Para proceder a registrar contrato es obligatorio seleccionar los items indicados"));
//			}else {
//				FacesContext context = FacesContext.getCurrentInstance();
//				context.addMessage(null, new FacesMessage("Ingrese número de cuotas, fecha Inicio o Fin de Contrato ","Para proceder a registrar contrato es obligatorio ingresar los items indicados"));
//			}
//		}
		
	}
	
	/**
	 * Graba o actualiza un nuevo contrato.
	 */
	public void grabarCabeceraContrato() {
		
		
	try{
		Calendar inicioCobro = new GregorianCalendar();
		inicioCobro.set(Calendar.YEAR,Integer.parseInt(aniofechaUltimoPago));
		inicioCobro.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesfechaUltimoPago))-1);
		inicioCobro.set(Calendar.DATE, 1);
		inicioCobro.set(Calendar.HOUR,0);
		inicioCobro.set(Calendar.MINUTE,0);
		inicioCobro.set(Calendar.SECOND,0);
		inicioCobro.set(Calendar.MILLISECOND,0);
		
		contratoBean.setIniciocobro(inicioCobro.getTime());
		
		//Moneda
		if (sisoles) {
			contratoBean.setMoneda("S");
		} else {
			contratoBean.setMoneda("D");
		}
		
		Contrato contrato=new Contrato();
		contrato.setIniciocobro(contratoBean.getIniciocobro());
		
		contrato.setMontocuotasoles(contratoBean.getCuota1());
		contrato.setMontocuotasoles2(0.0);
		contrato.setMontocuotasoles3(0.0);
		contrato.setMontocuotasoles4(0.0);
		contrato.setMontocuotasoles5(0.0);
		contrato.setMontocuotasoles6(0.0);

        contrato.setSiescriturapublica(contratoBean.getSiescriturapublica()==null? false : contratoBean.getSiescriturapublica());
        contrato.setSidetraccion(contratoBean.getSidetraccion()==null? false : contratoBean.getSidetraccion());
        contrato.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
		
		if (contratoBean.getIdcontrato() == 0) {//NUEVO
			
			contrato.setCondicion("Sin Contrato");
			contrato.setSinuevomantenimiento(Boolean.FALSE);
			contrato.setEstado("VIGENTE");
			
			contrato.setUpa(upa);
			contrato.setInquilino(inquilino);
			
			contrato.setAniocontrato(contratoBean.getAniocontrato());
			
			contrato.setSiresuelto(false);
			contrato.setSiadenda(false);
			contrato.setFechacreacion(new Date());
			contrato.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			contrato.setSicuotascanceladas(false);
			contrato.setSicompromisopago(false);
			
			
			contrato.setObservacion(contratoBean.getObservacion());
			contrato.setUsoespecifico(contratoBean.getUsoespecifico());
			
			contrato.setSiocupante(contratoBean.getSiocupante());
			contrato.setSidniocupante(contratoBean.getDniocupante());
			contrato.setSinombreocupante(contratoBean.getNombreocupante());
			
			contrato.setSisuscrito(false);
			contrato.setSiactaentrega(false);
			contrato.setSiresolucion(false);
			contrato.setSifinalizado(false);
			
			contrato.setNumerocuotas(0);
			
			contrato.setTipomoneda(contratoBean.getMoneda());
			//ANULADO
			contrato.setSianulado(false);
			contrato.setSidocumento(Boolean.FALSE);
			contrato.setSireconocimientodeuda(contratoBean.getSireconocimientodeuda());
			contrato.setSimontoinicialdeuda(contratoBean.getSimontoinicialdeuda());
			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se registro nuevo sin contrato con éxito");
			limpirRegistroPropiedades();
			
			
		} else { //ACTUALIZAR
			
			
			System.out.println("actualizando contrato");
			

			
			//Valores internos que se conservan
			contrato.setIdcontrato(contratoBean.getIdcontrato());
			contrato.setSinuevomantenimiento(contratoBean.getSinuevomantenimiento());
			contrato.setCondicion(contratoBean.getCondicion());
			contrato.setEstado(contratoBean.getEstado());
			contrato.setSicuotascanceladas(contratoBean.getSicuotascanceladas());
			contrato.setFechacreacion(contratoBean.getFeccre());
			contrato.setUsuariocreador(contratoBean.getUsrcre());
			
			contrato.setFincobro(contratoBean.getFincobro());
			
			
			//Valores internos de auditoria
			contrato.setFechamodificacion(new Date());
			contrato.setSicompromisopago(false);
			contrato.setUsuariomodificador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			//ANULADO
			contrato.setSianulado(false);
			
			//Valores cambiantes mediante interfaz grafica
			Upa upa= new Upa();
			upa.setIdupa(contratoBean.getIdupa());
			upa.setClave(contratoBean.getClaveUpa());
			contrato.setUpa(upa);
			 
			Inquilino inquilino= new Inquilino();
			inquilino.setIdinquilino(contratoBean.getIdinquilino());
			contrato.setInquilino(inquilino);
			
			contrato.setAniocontrato(contratoBean.getAniocontrato());
			
			contrato.setObservacion(contratoBean.getObservacion());
			contrato.setUsoespecifico(contratoBean.getUsoespecifico());
			contrato.setSiocupante(contratoBean.getSiocupante());
			contrato.setSinombreocupante(contratoBean.getNombreocupante());
			contrato.setSidniocupante(contratoBean.getDniocupante());
			
			
			contrato.setNumerocuotas(0);
			contrato.setSiresuelto(false);
			contrato.setSiadenda(false);
			contrato.setSisuscrito(false);
			contrato.setSiactaentrega(false);
			contrato.setSiresolucion(false);
			contrato.setSifinalizado(contratoBean.getSifinalizado());
			contrato.setTipomoneda(contratoBean.getMoneda());
			contrato.setObservacionfinalizado(contratoBean.getObservacionfinalizado());
			contrato.setSidocumento(Boolean.FALSE);
			contrato.setSireconocimientodeuda(contratoBean.getSireconocimientodeuda());
			contrato.setSimontoinicialdeuda(contratoBean.getSimontoinicialdeuda());
		
			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se actualizó sin contrato con éxito");

			limpirRegistroPropiedades();
			contratoBean = new CondicionBean();
			listContrato.clear();
		}
		
		

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void detectarSiCompletoCuotasSinContrato(CondicionBean bean){
		List<CuotaBean> listaCuotas= new ArrayList<CuotaBean>();
		
		CuotaBean cuota;
		Calendar ultimo = Calendar.getInstance();
		ultimo.setTime(bean.getIniciocobro());
		ultimo.add(Calendar.MONTH, 1);
		List<Cuota> listaCuotasPagadas= new ArrayList<Cuota>();
		listaCuotasPagadas=cobranzaRecaudacionService.listarcuotasxcontrato(bean.getIdcontrato());
		
		Calendar fin = GregorianCalendar.getInstance();
		
		if (bean.getFincobro()!=null) {
			fin.setTime(bean.getFincobro());
				
				while (ultimo.compareTo(fin) < 0) {
					
					cuota= new CuotaBean();
					ultimo.add(Calendar.MONTH, 1);
				    
				    cuota.setAnio(ultimo.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(ultimo.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Cancelado");
				    cuota.setMonto(bean.getCuota1());
				    cuota.setNropagos(1);

				    listaCuotas.add(cuota);
				    
				}
				
				for (int j = 0;j < listaCuotas.size(); j++) {
					
					for (int k = 0; k <listaCuotasPagadas.size() ; k++) {
						if (listaCuotas.get(j).getMes().equals(listaCuotasPagadas.get(k).getMespagado())&& listaCuotas.get(j).getAnio()==listaCuotasPagadas.get(k).getAniocuotames()  ) {
							if (listaCuotasPagadas.get(k).getCancelado().equals("1")) {
								listaCuotas.get(j).setSipagado("1");
							} else {
								listaCuotas.get(j).setMonto(FuncionesHelper.redondear(bean.getCuota1()-listaCuotasPagadas.get(k).getMontosoles(), 2));
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
		}
		
		if (listaCuotas.size()==0) {
			cobranzaRecaudacionService.actualizarSiCompletoCuotasContrato_SinContrato(bean.getIdcontrato(),true);
		}else {
			cobranzaRecaudacionService.actualizarSiCompletoCuotasContrato_SinContrato(bean.getIdcontrato(),false);
		}
	}
	
	public void validarCamposFinalizarSinContrato() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if(mesfechaUltimoPagoSinContrato.equals("") || aniofechaUltimoPagoSinContrato.equals(""))  {
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia","Complete el último mes cancelado del sin contrato para finalizar"));
		}else {
			contextRequest.execute("dglConfirmarCancelarSinContrato.show();");
		}
	}

	public void validarSeleccionFinalizarSinContrato() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if(contratoBean.getIdcontrato()==0) {
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia","Seleccione un registro para finalizar"));	
		}else {
				contextRequest.execute("dlgCancelarSinContrato.show();");
		}
	}
	
	public void finalizarSinContrato() {

		if (contratoBean.getIdcontrato() != 0) {
			Calendar fecUltimoPagoSinContrato = new GregorianCalendar();
			fecUltimoPagoSinContrato.set(Calendar.YEAR,Integer.parseInt(aniofechaUltimoPagoSinContrato));
			fecUltimoPagoSinContrato.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesfechaUltimoPagoSinContrato))-1);
			fecUltimoPagoSinContrato.set(Calendar.DATE, fecUltimoPagoSinContrato.getActualMaximum(Calendar.DAY_OF_MONTH));
			fecUltimoPagoSinContrato.set(Calendar.HOUR,0);
			fecUltimoPagoSinContrato.set(Calendar.MINUTE,0);
			fecUltimoPagoSinContrato.set(Calendar.SECOND,0);
			fecUltimoPagoSinContrato.set(Calendar.MILLISECOND,0);
			
			contratoService.cancelarSinContrato(contratoBean.getIdcontrato(),contratoBean.getObservacionfinalizado(),fecUltimoPagoSinContrato.getTime(),userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			detectarSiCompletoCuotasSinContrato(contratoBean);
			
			listContrato.clear();
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Éxito", "Se finalizo sin contrato éxitosamente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			listContrato.clear();
			limpirRegistroPropiedades();
		}
	}


	public String obtenerCorreoXid(int id) {
		String correo = "";

		for (int i = 0; i < todosUsuarios.size(); i++) {
			if (todosUsuarios.get(i).getIdusuario() == id) {
				correo = todosUsuarios.get(i).getEmailusr();
			}
		}
		return correo;
	}

	public String obtenerNombresXid(int id) {
		String nomApe = "";

		for (int i = 0; i < todosUsuarios.size(); i++) {
			if (todosUsuarios.get(i).getIdusuario() == id) {
				nomApe = todosUsuarios.get(i).getNombrescompletos();
			}
		}
		return nomApe;
	}

	
	public void limpirRegistroPropiedades() {
		contratoBean = new CondicionBean();
		contratoBean.setSisuscrito(false);
		contratoBean.setSiactaentrega(false);
		contratoBean.setSiresuelto(false);
		contratoBean.setSiadenda(false);
		contratoBean.setSiocupante(true);
		contratoBean.setCuota1(0.0);
		contratoBean.setSireconocimientodeuda(false);
		aniofechaUltimoPago="";
		aniofechaUltimoPagoSinContrato="";
		mesfechaUltimoPago="";
		mesfechaUltimoPagoSinContrato="";
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
	
	
	
	public void detectarnumerodecutas() {	
		int n=0;
		
		if (contratoBean.getIniciocontrato()!=null && contratoBean.getFincontrato()!=null) {
			Calendar ini = Calendar.getInstance();
			ini.setTime(contratoBean.getIniciocontrato());
			
			Calendar fin = Calendar.getInstance();
			fin.setTime(contratoBean.getFincontrato());
//			if (ini.get(Calendar.DAY_OF_YEAR)>28) {
				fin.add(Calendar.DAY_OF_YEAR, -3);	
//			}
			
			
			while (ini.compareTo(fin) <= 0) {
				ini.add(Calendar.MONTH, 1);
				n++;					
			}
		}
		contratoBean.setNumerocuotas(n);
		
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

	public void eliminarUsuarioDeLista(ActionEvent event) {

		for (int i = 0; i < listadousuariosSeleccionados.size(); i++) {

			if (listadousuariosSeleccionados.get(i) == getSelectRegistroUsuario()) {
				listadousuariosSeleccionados.remove(i);
			}
		}

	}

	public List<String> autoCompleteUsuario(String query) {

		List<String> result = new ArrayList<String>();

		for (Usuario usu : (List<Usuario>) todosUsuarios) {

			String nomComplusu = usu.getNombrescompletos();

			if (nomComplusu.toLowerCase().contains(query.toLowerCase())) {
				result.add(nomComplusu);
			}
		}

		return result;
	}

	public void agregarUsuarioLista() {
		boolean usuarioVacio = false;

		if (!usuarioVacio) {
		}
		Usuario Usu = new Usuario();
		Usu.setEmailusr("Escriba Nombre Usuario");
		Usu.setRutaimgusr("default.jpg");
		Usu.setContrasenausr("deleteUsuario.png");
		listadousuariosSeleccionados.add(Usu);
		setIndicesalvador(listadousuariosSeleccionados.size() - 1);

	}

	public void onCellEdit(CellEditEvent event) {

		if (Integer.parseInt(event.getColumn().getWidth()) == 99) {
			Object newValue = event.getNewValue();

			FacesContext contextFaces = FacesContext.getCurrentInstance();

			// buscamos id usuario
			int id = 0;
			for (int k = 0; k < todosUsuarios.size(); k++) {
				if ((todosUsuarios.get(k).getNombrescompletos())
						.equals(newValue)) {
					id = k;
				}

			}

			boolean flag = true;

			for (int i = 0; i < listadousuariosSeleccionados.size() - 1; i++) {
				System.out.println("i=" + i);
				if ((listadousuariosSeleccionados.get(i).getEmailusr())
						.equals(newValue)) {

					contextFaces.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_WARN, "Advertencia",
							"usuario ya se encuentra en la lista"));
					listadousuariosSeleccionados.remove(getIndicesalvador());
					flag = false;

				}
			}

			if (flag) {
				System.out.println("nuevo");
				listadousuariosSeleccionados.get(getIndicesalvador())
						.setRutaimgusr(todosUsuarios.get(id).getRutaimgusr());
				listadousuariosSeleccionados.get(getIndicesalvador()).setCargo(
						todosUsuarios.get(id).getCargo());
				listadousuariosSeleccionados.get(getIndicesalvador())
						.setIdusuario(todosUsuarios.get(id).getIdusuario());
				listadousuariosSeleccionados.get(getIndicesalvador())
						.setUsrmod("Ingrese Perfil");

				Area A = new Area();
				A.setDesare(todosUsuarios.get(id).getArea().getDesare());
				listadousuariosSeleccionados.get(getIndicesalvador())
						.setArea(A);
				setIdUsuarioSeleccionado(todosUsuarios.get(id).getIdusuario());

			}

		} else {
			Object newValue = event.getNewValue();

			listadousuariosSeleccionados.get(getIndicesalvador()).setUsrmod(
					newValue.toString());

		}
	}

	public void validarCancelacionContrato() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			contextRequest.execute("dlgCancelarContrato.show();");
		} else {
			contextFaces.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Advertencia",
					"Seleccione el contrato a resolver"));
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
	public Boolean getDisabledFinCobro() {
		return disabledFinCobro;
	}
	public void setDisabledFinCobro(Boolean disabledFinCobro) {
		this.disabledFinCobro = disabledFinCobro;
	}
	public Boolean getDisabledInicioCobro() {
		return disabledInicioCobro;
	}
	public void setDisabledInicioCobro(Boolean disabledInicioCobro) {
		this.disabledInicioCobro = disabledInicioCobro;
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
	public String getMesfechaUltimoPago() {
		return mesfechaUltimoPago;
	}
	public void setMesfechaUltimoPago(String mesfechaUltimoPago) {
		this.mesfechaUltimoPago = mesfechaUltimoPago;
	}
	public String getAniofechaUltimoPago() {
		return aniofechaUltimoPago;
	}
	public void setAniofechaUltimoPago(String aniofechaUltimoPago) {
		this.aniofechaUltimoPago = aniofechaUltimoPago;
	}
	public String getMesfechaUltimoPagoSinContrato() {
		return mesfechaUltimoPagoSinContrato;
	}
	public void setMesfechaUltimoPagoSinContrato(String mesfechaUltimoPagoSinContrato) {
		this.mesfechaUltimoPagoSinContrato = mesfechaUltimoPagoSinContrato;
	}
	public String getAniofechaUltimoPagoSinContrato() {
		return aniofechaUltimoPagoSinContrato;
	}
	public void setAniofechaUltimoPagoSinContrato(String aniofechaUltimoPagoSinContrato) {
		this.aniofechaUltimoPagoSinContrato = aniofechaUltimoPagoSinContrato;
	}
	public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
		return cobranzaRecaudacionService;
	}
	public void setCobranzaRecaudacionService(IRecaudacionCobranzaService cobranzaRecaudacionService) {
		this.cobranzaRecaudacionService = cobranzaRecaudacionService;
	}

}
