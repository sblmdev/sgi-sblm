package pe.gob.sblm.sgi.controller;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.MaeConfiguracionParametro;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.IProcesoJudicialUpaService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.service.MaeConfiguracionParametroService;



@ManagedBean(name = "consultaogajMB")	
@ViewScoped
public class ConsultaOGAFManagedBean extends BaseController implements Serializable {
	private static final Logger Logger = LoggerFactory.getLogger(ConsultaOGAFManagedBean.class);
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	@ManagedProperty(value = "#{procesoJudicialUpaService}")
	private transient IProcesoJudicialUpaService procesoJudicialUpaService;
	@ManagedProperty(value = "#{MaeConfiguracionParametroService}")
	private transient MaeConfiguracionParametroService maeConfiguracionParametroService; 
	private Upa upa;
	private Boolean siprocesojudicial;
	private Procesojudicialupa segupa;
	private String clave;
	private String motivo;
	private String expediente;
	private String situacion;
	private String observacion;
	private String observacionmodificacion;
	private ProcesojudicialupaBean condicionSeleccionado;
	private List<ProcesojudicialupaBean> procesojudicialseleccionado;
	private List<UpaBean> listaUpaBean;	
	private String tipoBusquedaUpa=Constantes.BUSQUEDA_CLAVE;
	private String valorbusquedaUpa;
	private List<UpaBean> listaUpaBeanFilter;
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{archivoMB}")
	ArchivoManagedBean archivoMB;
	
//	@ManagedProperty(value = "#{upaMB}")
//	UpaManagedBean upaBean;
	private UpaBean upaBean;
	
	private List<Procesojudicialupa>  listsegupa=new ArrayList<Procesojudicialupa>();
	
	@PostConstruct
	public void init(){
		try {
			siprocesojudicial=true;
			upaBean= new UpaBean();
			segupa= new Procesojudicialupa();
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void buscarUpaSeleccionada(){
		clave=upaBean.getClave();
		buscarUpa();
	}
	public void buscarUpa(){
		listsegupa.clear();
		upa=upaService.obtenerUpaxClave(clave);
		if (upa==null) {
			addWarnMessage("Upa no existe", "La clave de upa ingresada no esta registrada en el sistema");
		} else {
			siprocesojudicial=upa.getSiprocesojudicial();
			listsegupa=upaService.listarSegUpa(upa.getIdupa());
		}

	}
	public void validarRegistroExpediente(){		
		if(expediente.equals("")||expediente.replaceAll("[__]", "").length()==6||expediente.length()==0){
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente1.show();");
		}else if(motivo.equals("")){
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente2.show();");
		}else if(motivo.trim().length()<8){
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente2_1.show();");
		}else if(situacion.equals("")){
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente5.show();");
		}
		else{
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarRegistroExpedienteProcesoJudicial.show();");
		}
	}
	public void validarActualizarExpediente()throws Exception{
//		if(expediente.equals("")||expediente.length()==0){
//			RequestContext contextRequest = RequestContext.getCurrentInstance();
//			contextRequest.execute("dlgmensaje_expediente1.show();");
//		}else if(motivo.equals("")){
//			RequestContext contextRequest = RequestContext.getCurrentInstance();
//			contextRequest.execute("dlgmensaje_expediente2.show();");
//		}
//		else 
		if(segupa.getSifinalizado()&& segupa.getEstado().equalsIgnoreCase(Constantes.CONDICION_ESTADO_FINALIZADO)){
			if(validarEstadoProceso(segupa)){
				if(segupa.getObservacionmodificacion().equals("")|| segupa.getObservacionmodificacion().trim().length()<50){
					addWarnMessage("Motivo de Modificacion", "El motivo de modificación debe ser mayor a 50 caracteres");
				}else{
					RequestContext contextRequest = RequestContext.getCurrentInstance();
					contextRequest.execute("dlgConfirmarActualizarExpedienteProcesoJudicial.show();");
				}
			}else{
				addWarnMessage("Observación", "El tiempo limite de modificacion es de 15 dias . ");
			}

			
		}else{
			if(segupa.getObservacion().equals("")|| segupa.getObservacion().length()==0){
//				RequestContext contextRequest = RequestContext.getCurrentInstance();
//				contextRequest.execute("dlgmensaje_expediente2.show();");
				addWarnMessage("Observación", "Debe Ingresar la observación del expediente");
			}
			else{
				RequestContext contextRequest = RequestContext.getCurrentInstance();
				contextRequest.execute("dlgConfirmarActualizarExpedienteProcesoJudicial.show();");
			}
		}
		
		
		
	}
	public Boolean  validarEstadoProceso(Procesojudicialupa segupa) throws Exception{
    	Boolean resp= false;
    	MaeConfiguracionParametro maeConfigParametro= new MaeConfiguracionParametro();
    	maeConfigParametro.setN_tabla(Constantes.TABLA_PJU);
    	maeConfigParametro = maeConfiguracionParametroService.buscarConfiguracionParametroxCondicion(maeConfigParametro,Constantes.ACTIVO);
    	if(FechaUtil.getDiasDiferencia(new Date(),segupa.getFecmod()) > maeConfigParametro.getN_dias()){
    		resp=false;
    	}else{
    		resp=true;
    	}
    	return resp;
    }
	
	
	/**GRABAR PROCESO JUDICIAL EXPEDIENTE */
	public void grabarEventoSeguimiento(){
		segupa= new Procesojudicialupa();
		segupa.setExpediente(expediente);
		segupa.setMotivo(motivo);
		segupa.setObservacion(observacion);	
		segupa.setSituacion(situacion);
		segupa.setSifinalizado(false);
		segupa.setUpa(upa);
		segupa.setFeccre(new Date());
		segupa.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		segupa.setEstado("VIGENTE");
		segupa.setHost_ip_usrcre(gethost_ip());
//		if(archivoMB.getListaArchivoAdjunto().size()>0){
			segupa.setSidocumento(archivoMB.getListaArchivoAdjunto().size()>0?  Boolean.TRUE: Boolean.FALSE);
//		}	
		upaService.grabarEventoSeguimiento(segupa);
		//insertar los documentos
		if(archivoMB.getListaArchivoAdjunto().size()>0){
		insertarDocumentoAdjunto(segupa);
		}
		listsegupa=upaService.listarSegUpa(upa.getIdupa());
		siprocesojudicial=true;
		upaService.actualizarProcesoJudicial(upa.getIdupa(),siprocesojudicial);	
		addInfoMessage("Registro exitoso", "Se registro el expediente del proceso judicial de la upa exitosamente");
	}
	/**ACTUALIZAR PROCESO JUDICIAL EXPEDIENTE */
	public void actualizarSeguimiento(){
		try {
		ProcesojudicialupaBean procesojudicialupa= new ProcesojudicialupaBean();
		procesojudicialupa=upaService.obtenerProcesoJudicialUpaxCondicion(condicionSeleccionado.getIdprocesojudicialupa());
		//actualizar
		segupa= new Procesojudicialupa();		
		segupa.setExpediente(procesojudicialupa.getExpediente());
		segupa.setObservacion(procesojudicialupa.getObservacion());		
		segupa.setUpa(upa);
		segupa.setFeccre(procesojudicialupa.getFeccre());
		segupa.setUsrcre(procesojudicialupa.getUsrcre());
		segupa.setFecmod(new Date());
		segupa.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		segupa.setSifinalizado(siprocesojudicial);
		segupa.setEstado("FINALIZADO");
		segupa.setHost_ip_usrmod(gethost_ip());
		//upaService.grabarEventoSeguimiento(segupa);
		upaService.actualizarProcesoJudicialUpa(segupa);
		listsegupa=upaService.listarSegUpa(upa.getIdupa());
		siprocesojudicial=true;
//		actualizarProcesoJudicial();
		addInfoMessage("Actualización de registro exitoso", "Se actualizo el expediente del proceso judicial upa exitosamente");
		limpiarDatosExpedienteRegistro();
		}catch(Exception e){
			e.printStackTrace();
			
		}
    }

	public void actualizarExpedienteJudicial(){
		try{
			
	        segupa.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
	        segupa.setFecmod(new Date());
	        segupa.setHost_ip_usrmod(gethost_ip());
			if(archivoMB.getListaArchivoAdjunto().size()>0){
				segupa.setSidocumento(true);	
				insertarDocumentoAdjunto(segupa);
			}	
			upaService.grabarEventoSeguimiento(segupa);
			addInfoMessage("Actualización de registro exitoso", "Se actualizo el expediente del proceso judicial  exitosamente");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void actualizarProcesoJudicial(){
		//actualizarSeguimiento();
		//
		Procesojudicialupa procesojudicial= new Procesojudicialupa();
		procesojudicial.setObservacionmodificacion(observacionmodificacion);
		procesojudicial.setEstado("FINALIZADO");
		procesojudicial.setUsrmod(userMB.getNombrecompleto());
		procesojudicial.setFecmod(new Date());
		procesojudicial.setSifinalizado(Boolean.TRUE);
		procesojudicial.setHost_ip_usrmod(gethost_ip());
		//upaService.actualizarProcesoJudicial(upa.getIdupa(),siprocesojudicial);
		upaService.actualizarProcesoJudicial(upa.getIdupa(),siprocesojudicial,procesojudicial);
		listsegupa.clear();
		listsegupa=upaService.listarSegUpa(upa.getIdupa());
		addInfoMessage("Se cambio estado exitosamente", "Se cambio estado judicial de upa exitosamente");
	}
	public void validarProcesoJudicialUpa(){
		if(siprocesojudicial.equals(true)){
			addWarnMessage("No se cambio el estado de judicializado!!!", "Se debe agregar expediente para judicializar la UPA");
		}else if(listsegupa.size()==0){
			addWarnMessage("No se cambio el estado de judicializado!!!", "Se debe agregar expediente para judicializar la UPA");
		}else{
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			//contextRequest.execute("dlgConfirmarActualizarProcesoJudicial.show();");
			contextRequest.execute("dlgCambioProcesoJudicial.show();");
		}
	}
	public void validarModificacionCondicionProcesoJudicialUpa(){
		if(listsegupa.size()==0){
			addWarnMessage("No se cambio el estado de judicializado!!!", "Se debe agregar expediente para judicializar la UPA");
		}else if(observacionmodificacion.length()==0||observacionmodificacion.equals("")){
			//addWarnMessage("No se cambio el estado de judicializado!!!", "Se debe agregar el motivo del cambio en el proceso judicializado");
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente3.show();");
		}else {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarActualizarProcesoJudicial.show();");
			
		}
	}
	public void insertarDocumentoAdjunto(Procesojudicialupa procesojudicial){
		   //buscar contrato insertado
	        System.out.println("IDPROCESOJUDICIALUPA="+procesojudicial.getIdprocesojudicialupa());
		  // listContrato=contratoService.buscarContrato(contrato);
	       System.out.println("tamaño archivo="+archivoMB.getListaArchivoAdjunto().size());
	       if(archivoMB.getListaArchivoAdjunto().size()>0){
	    	   archivoMB.grabarDocumentoAdjunto(Constantes.PROCESOJUDICIALUPA_SGI,procesojudicial.getIdprocesojudicialupa());
	       }
		   
	   }
    public void agregarExpedienteProcesoJudicialUpa(){
    	if(upa==null){
    		RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgmensaje_expediente4.show();");
    	}else{
    	limpiarDatosExpedienteRegistro();
    	RequestContext contextRequest = RequestContext.getCurrentInstance();
    	contextRequest.execute("dlgAgregarExpediente.show();");
    	}
    }
	public void buscarExpediente(int idprocesojudicial){
		if(listsegupa!=null){
		procesojudicialseleccionado=procesoJudicialUpaService.buscarExpediente(idprocesojudicial);
		}
	}
	public void datosExpediente(int idprocesojudicial){
		segupa= null;
		segupa= procesoJudicialUpaService.buscarDatosExpediente(idprocesojudicial);
		archivoMB.limpiarDocumentoADjuntos();
		
	}
	public void buscarListaUpa(){
        Upa upa = new Upa();
        System.out.println(siprocesojudicial);
        	upa.setSiprocesojudicial(siprocesojudicial);
        
		listaUpaBean=upaService.listarUpasBean(valorbusquedaUpa,tipoBusquedaUpa,upa);
		//listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusquedaUpa.substring(0,7),tipoBusquedaUpa);
	
	}
	public void limpiarDatosExpediente(){
		archivoMB.limpiarDocumentoADjuntos();
		System.out.println("limpiar tabla datos adjuntos");
	}
	public void limpiarDatosExpedienteRegistro(){
		expediente="";
		motivo="";
		situacion="";
		observacion="";
		archivoMB.limpiarDocumentoADjuntos();
		
	}
	
	public String gethost_ip(){
		String host_ip=null;
		InetAddress ip;
		try {
            ip = InetAddress.getByName(request.getRemoteAddr());
           System.out.println("host-ip="+ip.getHostName()+"/"+request.getRemoteAddr());
           host_ip=ip.getHostName()+"/"+request.getRemoteAddr();

       } catch (UnknownHostException e) {

           e.printStackTrace();
       }
		return host_ip;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Boolean getSiprocesojudicial() {
		return siprocesojudicial;
	}

	public void setSiprocesojudicial(Boolean siprocesojudicial) {
		this.siprocesojudicial = siprocesojudicial;
	}

	public List<Procesojudicialupa> getListsegupa() {
		return listsegupa;
	}

	public void setListsegupa(List<Procesojudicialupa> listsegupa) {
		this.listsegupa = listsegupa;
	}

	public Procesojudicialupa getSegupa() {
		return segupa;
	}

	public void setSegupa(Procesojudicialupa segupa) {
		this.segupa = segupa;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public String getSituacion() {
		return situacion;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public ProcesojudicialupaBean getCondicionSeleccionado() {
		return condicionSeleccionado;
	}

	public void setCondicionSeleccionado(
			ProcesojudicialupaBean condicionSeleccionado) {
		this.condicionSeleccionado = condicionSeleccionado;
	}

	public ArchivoManagedBean getArchivoMB() {
		return archivoMB;
	}

	public void setArchivoMB(ArchivoManagedBean archivoMB) {
		this.archivoMB = archivoMB;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public IProcesoJudicialUpaService getProcesoJudicialUpaService() {
		return procesoJudicialUpaService;
	}

	public void setProcesoJudicialUpaService(
			IProcesoJudicialUpaService procesoJudicialUpaService) {
		this.procesoJudicialUpaService = procesoJudicialUpaService;
	}

	public List<ProcesojudicialupaBean> getProcesojudicialseleccionado() {
		return procesojudicialseleccionado;
	}

	public void setProcesojudicialseleccionado(
			List<ProcesojudicialupaBean> procesojudicialseleccionado) {
		this.procesojudicialseleccionado = procesojudicialseleccionado;
	}

	public String getObservacionmodificacion() {
		return observacionmodificacion;
	}

	public void setObservacionmodificacion(String observacionmodificacion) {
		this.observacionmodificacion = observacionmodificacion;
	}
	
	public List<UpaBean> getListaUpaBean() {
		return listaUpaBean;
	}
	public void setListaUpaBean(List<UpaBean> listaUpaBean) {
		this.listaUpaBean = listaUpaBean;
	}
	public String getTipoBusquedaUpa() {
		return tipoBusquedaUpa;
	}
	public void setTipoBusquedaUpa(String tipoBusquedaUpa) {
		this.tipoBusquedaUpa = tipoBusquedaUpa;
	}
	public String getValorbusquedaUpa() {
		return valorbusquedaUpa;
	}
	public void setValorbusquedaUpa(String valorbusquedaUpa) {
		this.valorbusquedaUpa = valorbusquedaUpa;
	}
	public UpaBean getUpaBean() {
		return upaBean;
	}
	public void setUpaBean(UpaBean upaBean) {
		this.upaBean = upaBean;
	}
	public List<UpaBean> getListaUpaBeanFilter() {
		return listaUpaBeanFilter;
	}
	public void setListaUpaBeanFilter(List<UpaBean> listaUpaBeanFilter) {
		this.listaUpaBeanFilter = listaUpaBeanFilter;
	}
	public static Logger getLogger() {
		return Logger;
	}
	public MaeConfiguracionParametroService getMaeConfiguracionParametroService() {
		return maeConfiguracionParametroService;
	}
	public void setMaeConfiguracionParametroService(
			MaeConfiguracionParametroService maeConfiguracionParametroService) {
		this.maeConfiguracionParametroService = maeConfiguracionParametroService;
	}
    
	
}