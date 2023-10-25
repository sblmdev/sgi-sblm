package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Tipoentidad;
import pe.gob.sblm.sgi.entity.Tipopersona;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.util.Constante;

@ManagedBean(name = "inquilinoMB")
@ViewScoped
public class InquilinoManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	
	private static final Logger Logger = LoggerFactory.getLogger(InquilinoManagedBean.class);
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;
	
	
	private Inquilino inquilino;
	private List<Inquilino> listaInquilino= new ArrayList<Inquilino>();
	private List<Inquilino> listaInquilinoFilter;
	
	
	private List<Tipopersona> listaTipoPersona;
	
	private String tipoBusqueda=Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL;
	

	private Integer idtipopersona;
	private Boolean sipersonanatural=Boolean.TRUE;
	
	/**Busqueda**/
	private String valorbusqueda;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	@PostConstruct
	public void init(){
		
		try {
			inquilino= new Inquilino();
			listaTipoPersona=inquilinoService.listarTipoPersona();
			
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
		
		
	}
	
	
	public void buscarInquilinoxNombres(){
		 try{
			listaInquilino.clear();
			listaInquilino=inquilinoService.listarInquilinos(valorbusqueda,tipoBusqueda);
			if(listaInquilino.size()==0){
				addWarnMessage("","No se encuentra el inquilino buscado ");
			}else{
				addInfoMessage("", "Busqueda exitosa");
			}
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	}
	
	public void cambiarTipoPersona(){
		//inquilino = new Inquilino();
		if (idtipopersona==1 ) {
			sipersonanatural=Boolean.TRUE;           
		} else if(idtipopersona==2 ) {
			sipersonanatural=Boolean.FALSE;
		}else{
			
		}
		
	}
	public void validarInquilino(){
		if (idtipopersona > 0) {
           if(inquilino.getSianulado()==null|| !inquilino.getSianulado()){
			if (sipersonanatural) {
			 if (inquilino.getApellidopat().equals("")){
				addWarnMessage("","Ingrese Apellido Paterno para continuar. ");

			}else if ( inquilino.getApellidomat().equals("")){
				addWarnMessage("","Ingrese Apellido Materno para continuar. ");

			}else if ( inquilino.getNombre().equals("")) {
				addWarnMessage("","Ingrese nombre para continuar.");
			
			}else if ( inquilino.getDni().equals("")&& inquilino.getCarnetextranjeria().equalsIgnoreCase("")) {
				addWarnMessage("","Ingrese DNI para continuar");
			
			}else if ( inquilino.getDni().length()!=8 && inquilino.getCarnetextranjeria().equalsIgnoreCase("")) {
				addWarnMessage("","DNI ingresado tiene que tener 8 dígitos.");
			}else if(inquilino.getRuc().length()>=1 && inquilino.getRuc().length()<11){
				addWarnMessage("","RUC ingresado tiene que tener 11 dígitos.");
			}else if(inquilino.getCarnetextranjeria().length()<=1 && inquilino.getCarnetextranjeria().length()>12){
					addWarnMessage("","Carnet Extranjeria ingresado debe tener como maximo 12 digitos.");		
			}else if ( inquilino.getSifallecido() == null) {
					addWarnMessage("",
						"Seleccione si la persona se encuentra fallecida.");

			}else {
				RequestContext requestContext= RequestContext.getCurrentInstance();  
				requestContext.execute("dlgRegistrarInquilino.show();");
			 }
			} else 
				{
					if(inquilino.getRazonsocial().equals("")){
						addWarnMessage("","Ingrese Razon Social para continuar. ");
					}else if (inquilino.getRuc().equals("")) {
					
						addWarnMessage("", "Ingrese RUC para continuar");

					} else if ( inquilino.getRuc().length() != 11) {
						addWarnMessage("","RUC ingresado tiene que tener 11 dígitos.");

					} else{
						RequestContext requestContext= RequestContext.getCurrentInstance();  
						requestContext.execute("dlgRegistrarInquilino.show();");
					}
				}
	    	} else {
	    		addWarnMessage("", "El inquilino se encuentra en estado ANULADO.");			
	    	}
		}else{
			addWarnMessage("", "Seleccione tipo de persona para continuar.");
		}
	}
	public void validarGrabarInquilino(){
		
		if (idtipopersona==0) {
			addWarnMessage("","Seleccione tipo de persona para continuar.");
		
		}else if (idtipopersona==1 && inquilino.getNombre().equals("")) {
			addWarnMessage("","Ingrese nombre para continuar.");
		
		}else if (idtipopersona==1 && inquilino.getApellidopat().equals("")){
			addWarnMessage("","Ingrese tipo de zona y tipo de vía. ");

		}else if (idtipopersona==1 && inquilino.getApellidomat().equals("")){
			addWarnMessage("","Ingrese tipo de zona y tipo de vía. ");

		}else if (idtipopersona==1 && inquilino.getDni().equals("")) {
			addWarnMessage("","Ingrese DNI para continuar");
		
		}else if (idtipopersona==1 && inquilino.getDni().length()!=8) {
			addWarnMessage("","DNI ingresado tiene que tener 8 dígitos.");
		
		}
//		else if (getInquilinoService().validarInquilinoRepetido(idtipopersona==Constantes.TIPO_PERSONA_JURIDICA? inquilino.getRuc():inquilino.getDni(),idtipopersona)!=null) {
//			addWarnMessage("","DNI ingresado ya se encuentra registrado.");
//		
//		}
		else if (idtipopersona==2 && inquilino.getRazonsocial().equals("")) {
			addWarnMessage("","Ingrese Razón Social para continuar");
		
		}else if (idtipopersona==2 && inquilino.getRuc().equals("")) {
			addWarnMessage("","Ingrese RUC para continuar");
		
		}else if (idtipopersona==2 && inquilino.getRuc().length()!=11) {
			addWarnMessage("","RUC ingresado tiene que tener 11 dígitos.");
		
		}else if (idtipopersona==1 && inquilino.getSifallecido()==null){
			addWarnMessage("","Seleccione si la persona se encuentra fallecida.");

		}else{
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dlgRegistrarInquilino.show();");
		}
		
		
	}
	
	
	
	public void grabarInquilino(){
		System.out.println("***grabar inquilino***");
		Tipopersona tipopersona= new Tipopersona();
		tipopersona.setIdtipopersona(idtipopersona);
		System.out.println("idtipopersona="+idtipopersona);
		Tipoentidad tipoentidad= new Tipoentidad();
		tipoentidad.setIdtipoentidad(1);
		
		inquilino.setTipoentidad(tipoentidad);
		inquilino.setTipopersona(tipopersona);
		inquilino.setSianulado(Boolean.FALSE);
		
		if (idtipopersona==Constantes.TIPO_PERSONA_NATURAL) {
			inquilino.setNombrescompletos(inquilino.getApellidopat()+" "+inquilino.getApellidomat()+ " "+inquilino.getNombre());
		} else {
			inquilino.setNombrescompletos(inquilino.getRazonsocial());
		}
		if (inquilino.getIdinquilino()!=0) {/**Actualizar*/
			
			inquilino.setFecmod(new Date());
			inquilino.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			inquilinoService.actualizarInquilino(inquilino);/** actualiza*/
			actualizarInformacionComprobante(inquilino);
			
		}else {/**Nuevo*/
			inquilino.setEstado(Constantes.ESTADO_INQUILINO_1);
			inquilino.setFeccre(new Date());
			inquilino.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			inquilinoService.registrarInquilino(inquilino);/**Graba */
		}
		
		//inquilinoService.registrarInquilino(inquilino);/**Graba o actualiza*/
		addInfoMessage("", "Se grabo la información del inmueble correctamente. ");
	
		limpiarformulario();	
		

	}
	public void actualizarInformacionComprobante(Inquilino inquilino){
		try{
		Comprobantepago cp = new Comprobantepago(); 
		List<Sunat_Comprobante> listasunatcomprobante = recaudacionReporteService.obtenerListaSunatComprobantePendientes();
		boolean estado;
		if(listasunatcomprobante!=null &&listasunatcomprobante.size()>0){
			for (Sunat_Comprobante sunat_Comprobante : listasunatcomprobante) {
				cp = cobranzaRecaudacionService.obtenerComprobantexInquilino(sunat_Comprobante.getId_referencia(), inquilino.getIdinquilino());
				estado=false;
				if(cp!=null){
					switch (cp.getTipodoc_receptor()) {
					case Constante.TIPO_DOC_RECEPTOR_DNI:
						if(!cp.getDnirucpagante().equalsIgnoreCase(inquilino.getDni())){
                    		cp.setDnirucpagante(inquilino.getDni());
                    		estado=true;
                    	}
						
						break;
					case Constante.TIPO_DOC_RECEPTOR_RUC:
						if(!cp.getDnirucpagante().equalsIgnoreCase(inquilino.getRuc())){
                    		cp.setDnirucpagante(inquilino.getRuc());
                    		estado=true;
                    	}
						
						break;
					case Constante.TIPO_DOC_RECEPTOR_CARNET_EXTRANJERIA:
						if(!cp.getDnirucpagante().equalsIgnoreCase(inquilino.getCarnetextranjeria())){
                    		cp.setDnirucpagante(inquilino.getCarnetextranjeria());
                    		estado=true;
                    	}
						
						break;
					default:estado= false;
						break;
					}
					if(estado){
						// actualizar comprobante sgi y sunatfe
						cobranzaRecaudacionService.updateComprobanteSgiSunatFe(cp);
						
					}

				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void seleccionarInquilino(){
		
		idtipopersona=inquilino.getTipopersona().getIdtipopersona();
		
		if (idtipopersona.equals(Constantes.TIPO_PERSONA_JURIDICA)) {
			sipersonanatural=Boolean.FALSE;
		} else {
			sipersonanatural=Boolean.TRUE;
		}
		
	}

	
	public void validarEliminarInmueble(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inquilino.getIdinquilino()!=0) {
			context.addMessage(null, new FacesMessage("Eliminación exitosa","Se ha eliminado exitosamente el registro de inmueble "));
			
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para eliminar ","Para proceder a eliminar inmueble es obligatorio seleccionar un inmueble"));
			limpiarformulario();
		}
	}
	public void validarAnularInquilino(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		if(inquilino.getIdinquilino()!=0){
			if(inquilino.getSianulado()){
				contextFaces.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Advertencia",
						"Este inquilino ya se encuentra en condicion de anulado"));
			}else{
				contextRequest.execute("dlgAnularInquilino.show();");
			}
		}else{
			contextFaces.addMessage(null, new FacesMessage("Seleccionar un inquilino para anular ","Para proceder con el proceso de anulacion del inquilino es obligatorio seleccionar un inquilino"));
			limpiarformulario();
		}
	}
	public void validarDatosAnularInquilino(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		if(inquilino.getMotivo_anulacion()!=null && !inquilino.getMotivo_anulacion().trim().equalsIgnoreCase("")){
			
			contextRequest.execute("dlgConfirmarAnularInquilino.show();");
		}else {
			contextFaces.addMessage(null, new FacesMessage(" ","Debe agregar el motivo de anulación "));

		}
		
		
	}
	public void anularInquilino(){
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		if (inquilino.getIdinquilino()!=0) {/**Actualizar*/
			
			inquilino.setFec_anula(new Date());
			inquilino.setUsr_anula(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			inquilino.setEstado(Constantes.ESTADO_INQUILINO_2);
			inquilino.setSianulado(Boolean.TRUE);
			inquilino.setHost_ip_anula(CommonsUtilities.getRemoteIpAddress());
			inquilinoService.actualizarInquilino(inquilino);/** actualiza*/
			contextFaces.addMessage(null, new FacesMessage("Anulación exitosa","Se ha anulado exitosamente el inquilino "));
		}else{
			contextFaces.addMessage(null, new FacesMessage(" ","Debe seleccionar un inquilino "));
		}
		
		
		

	}
	
	
	
	public void validarAdjuntarArchivo(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inquilino.getIdinquilino()!=0) {
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dglAdjuntarArchivo.show();");
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para adjuntar ","Para proceder a adjuntar archivo es obligatorio seleccionar un inmueble"));
		}
	}
	
	
	
	
	public void limpiarformulario(){
		
		inquilino =new Inquilino();
		idtipopersona=0;
	}
	
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getValorbusqueda() {
		return valorbusqueda;
	}
	public void setValorbusqueda(String valorbusqueda) {
		this.valorbusqueda = valorbusqueda;
	}
	public Integer getIdtipopersona() {
		return idtipopersona;
	}
	public void setIdtipopersona(Integer idtipopersona) {
		this.idtipopersona = idtipopersona;
	}
	public String getTipoBusqueda() {
		return tipoBusqueda;
	}
	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	public IInquilinoService getInquilinoService() {
		return inquilinoService;
	}
	public void setInquilinoService(IInquilinoService inquilinoService) {
		this.inquilinoService = inquilinoService;
	}
	public List<Tipopersona> getListaTipoPersona() {
		return listaTipoPersona;
	}
	public void setListaTipoPersona(List<Tipopersona> listaTipoPersona) {
		this.listaTipoPersona = listaTipoPersona;
	}
	public Inquilino getInquilino() {
		return inquilino;
	}
	public void setInquilino(Inquilino inquilino) {
		this.inquilino = inquilino;
	}
	public List<Inquilino> getListaInquilino() {
		return listaInquilino;
	}
	public void setListaInquilino(List<Inquilino> listaInquilino) {
		this.listaInquilino = listaInquilino;
	}
	public List<Inquilino> getListaInquilinoFilter() {
		return listaInquilinoFilter;
	}
	public void setListaInquilinoFilter(List<Inquilino> listaInquilinoFilter) {
		this.listaInquilinoFilter = listaInquilinoFilter;
	}
	public Boolean getSipersonanatural() {
		return sipersonanatural;
	}
	public void setSipersonanatural(Boolean sipersonanatural) {
		this.sipersonanatural = sipersonanatural;
	}


	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}


	public void setRecaudacionReporteService(
			IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}


	public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
		return cobranzaRecaudacionService;
	}


	public void setCobranzaRecaudacionService(
			IRecaudacionCobranzaService cobranzaRecaudacionService) {
		this.cobranzaRecaudacionService = cobranzaRecaudacionService;
	}
	
	
	
}
