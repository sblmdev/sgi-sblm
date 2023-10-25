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
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInquilinoService;
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
*/

@ManagedBean(name = "pcMB")
@ViewScoped
public class CPrecontratoManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private RentaBean selectedRenta;
	
	private Usuario selectRegistroUsuario;
	private List<Integer> listaAnho;

	// Objetos
	private CondicionBean contratoBean= new CondicionBean();
	
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
	private List<CondicionBean> listContrato, listContratoFiltro;
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
	private Boolean disabledInicioCobro=true;
	
	
	//Pestaña Cuotas
	private List<pe.gob.sblm.sgi.entity.Cuota> listacuotas;
	private List<Detallecuota> listadetallecuota=new ArrayList<Detallecuota>();
	private Cuota cuota= new Cuota();
	private String  mesInicioCobro="",mesFinCobro="",anioInicioCobro="",anioFinCobro="",mesFinCobroResuelto="",anioFinCobroResuelto="",mesFinCobroAdenda="",anioFinCobroAdenda="";
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;

	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private UploadedFile file;
	private boolean fileCargado;
	private StreamedContent fileDescargar;
	
	
	
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
		sidolar = false;
		sisoles = true;
		tipobusqueda=1;
		tipobusquedaInquilino=Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL;
		tipobusquedaUpa=1;
		
	}
	
	
	public void activarFechaInicioCobro() {
		if (contratoBean.getNumerocuotas()!=0 && contratoBean.getIniciocontrato()!=null) {
			disabledInicioCobro=true;
		}else {
			disabledInicioCobro=false;
		}
	}
	
	
	public void metodo2() {
		if (!mesInicioCobro.equals("") && !anioInicioCobro.equals("")) {
			Calendar inicio=Calendar.getInstance();
			inicio.set(Calendar.YEAR,Integer.parseInt(anioInicioCobro));
			inicio.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesInicioCobro))-1);
			inicio.set(Calendar.DATE, 1);
			inicio.set(Calendar.HOUR,0);
			inicio.set(Calendar.MINUTE,0);
			inicio.set(Calendar.SECOND,0);
			inicio.set(Calendar.MILLISECOND,0);
			
			inicio.add(Calendar.MONTH, contratoBean.getNumerocuotas());
			inicio.add(Calendar.DAY_OF_YEAR, -1);
			disabledFinCobro=false;
			
			mesFinCobro=Almanaque.obtenerNombreMes(inicio.get(Calendar.MONTH));
			anioFinCobro=String.valueOf(inicio.get(Calendar.YEAR));
			
		}else {
			disabledFinCobro=true;
		}
	}
	
	/**
	 * Genera una tabla para ingreso de las rentas correspondientes.
	 */
	public void agregarRentas(){
		
		listaRentas.clear();
		if (contratoBean.getNumerocuotas()==0) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("No se ha ingresado el numero de cuotas", "Ingresar un número antes de agregar los montos de cuota"));
		
		} else if (contratoBean.getNumerocuotas()>72) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("El número de cuotas supera el máximo permitido", "Ingresar el número menor para agregar los montos de cuota"));
		} else {
			
			
			int nrocoutas=contratoBean.getNumerocuotas();
			for (int i = 1; i <= nrocoutas; i=i+12) {
				System.out.println("for");
				RentaBean rentaBean= new RentaBean();
				rentaBean.setIgv(0.0);
				rentaBean.setMc(0.0);
				rentaBean.setRenta(0.0);
				rentaBean.setId(listaRentas.size()+1);
				listaRentas.add(rentaBean);
			}
		}
		
	}

	/**
	 * Calcula la mc e igv de las rentas ingresadas.
	 */
	public void actualizarValoresListaRenta() {
			selectedRenta.setIgv(FuncionesHelper.redondear(selectedRenta.getRenta()/1.18*0.18, 2));
			selectedRenta.setMc(FuncionesHelper.redondear(selectedRenta.getRenta()/1.18, 2));
		
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
			listContrato=contratoService.buscarConSinContratoxClave(valorbusqueda,"Precontrato");
			
		} else if (tipobusqueda==2) {
			listContrato=contratoService.buscarConSinContratoxDireccion(valorbusqueda,"Precontrato");
		}else  {
			listContrato=contratoService.buscarConSinContratoxNombreInquilino(valorbusqueda,"Precontrato");
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
		for (int i = 1; i <= nrocoutas; i=i+12) {
			RentaBean rentaBean= new RentaBean();
			rentaBean.setIgv(0.0);
			rentaBean.setMc(0.0);
			rentaBean.setId(listaRentas.size()+1);
			if (i<=12) {
				rentaBean.setRenta(contratoBean.getCuota1());
				
			}else if (i<=24) {
				rentaBean.setRenta(contratoBean.getCuota2());
			}else if (i<=36) {
				rentaBean.setRenta(contratoBean.getCuota3());			
			}else if (i<=48) {
				rentaBean.setRenta(contratoBean.getCuota4());
			}else if (i<=60) {
				rentaBean.setRenta(contratoBean.getCuota5());
			}else {
				rentaBean.setRenta(contratoBean.getCuota6());
			}
			listaRentas.add(rentaBean);
			
		}
	}

	
	/**
	 * Validacion para  determinar si la upa esta disponible para vincular.
	 */	
	public void seleccionarUpa(SelectEvent event) {
		if (contratoService.verificarExisteCondicionVigente(upa.getIdupa())) {
			upa = null;
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar upa debido a que se encuentra ocupada en una condición vigente");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else {
			contratoBean.setIdupa(upa.getIdupa());
			contratoBean.setClaveUpa(upa.getClave());
			//contratoBean.setDireccion(upa.getInmueble().getDireccion()+ " "+ upa.getInmueble().getNumeroprincipal()+" "+upa.getNombrenuminterior());
			contratoBean.setDireccion(upa.getDireccion()+ " "+ upa.getNumprincipal()+" "+upa.getNombrenuminterior());
//			contratoBean.setUso(upa.getUso());
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
		
		if (contratoBean.getIdupa()!=0 && contratoBean.getIdinquilino()!=0  && contratoBean.getNumerocuotas()!=0) {
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarContrato.show();");
			
		} else {
			if (upa.getIdupa()==0 || inquilino.getIdinquilino()==0) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Seleccionar upa o inquilino ","Para proceder a registrar contrato es obligatorio seleccionar los items indicados"));
			}else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Ingrese número de cuotas","Para proceder a registrar contrato es obligatorio ingresar los items indicados"));
			}
		}
		
	}
	
	/**
	 * Graba o actualiza un nuevo contrato.
	 */
	public void grabarCabeceraContrato() {
		
		
		//El inicio de cobro y el fin de cobro se parse desde inicio en un dia 1 hasta el fin el un dia 31
		Calendar iniCobro = new GregorianCalendar();
		Calendar finCobro = new GregorianCalendar();
		
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
		contrato.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
		
		
		if (contratoBean.getIdcontrato() == 0) {//NUEVO
			
			contrato.setCondicion("Precontrato");
			contrato.setEstado("VIGENTE");
			
			contrato.setUpa(upa);
			contrato.setInquilino(inquilino);
			
			
			contrato.setSiresuelto(false);
			contrato.setSiadenda(false);
			contrato.setFechacreacion(new Date());
			contrato.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			contrato.setSicuotascanceladas(false);
			contrato.setSicompromisopago(false);
			contrato.setSifinalizado(false);
			
			contrato.setNrocontrato("");
			contrato.setTipo("");
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
			
			contrato.setIniciocontrato(null);
			contrato.setFincontrato(null);
			contrato.setNumerocuotas(contratoBean.getNumerocuotas());
			
			contrato.setTipomoneda(contratoBean.getMoneda());
			
			
//			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se registro nuevo Precontrato con éxito");
			limpirRegistroPropiedades();
			
			
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
//			contrato.setFEcmo(contratoBean.getFe);
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
		
//			contratoService.registrarContrato(contrato);
			
			addInfoMessage("", "Se actualizó Precontrato con éxito");

			limpirRegistroPropiedades();
			contratoBean = new CondicionBean();
			listContrato.clear();
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
		institucion = null;
		representante = null;
		cliente = null;
		representanteaval = null;
		ctabancaria = null;
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
	
	public void validarRevertirSinContrato(){
		
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();

		if (contratoBean.getIdcontrato() != 0) {
			contextRequest.execute("dlgRevertirPreContrato.show();");
		} else {
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia","Seleccione un registro para finalizar"));
		}
	
	}
	
	public void revertirSinContrato(){
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		//contratoService.cambiarCondicionContrato("Contrato",contratoBean.getIdcontrato());
		
		contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Se realizo cambio exitosamente","Ir a pagina de contratos para actualizar la información correspondiente"));
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
	public Boolean getDisabledInicioCobro() {
		return disabledInicioCobro;
	}
	public void setDisabledInicioCobro(Boolean disabledInicioCobro) {
		this.disabledInicioCobro = disabledInicioCobro;
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
}
