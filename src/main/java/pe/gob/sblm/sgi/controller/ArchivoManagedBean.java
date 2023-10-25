package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.DataFileUtil;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.InicioService;
import pe.gob.sblm.sgi.util.Constante;


@ManagedBean(name = "archivoMB")
@ViewScoped
public class ArchivoManagedBean extends BaseController implements Serializable {
	
	private static final Logger Logger = LoggerFactory.getLogger(ArchivoManagedBean.class.getName());
	
	private static final long serialVersionUID = 5524190003746598593L;

	private StreamedContent vistaPrevia;
	
	@ManagedProperty(value = "#{administrarArchivoService}")
	private transient AdministrarArchivoService administrarArchivoService;
	
	@ManagedProperty(value = "#{inicioService}")
	private transient InicioService inicioService;

	/**upload**/
	private List<ArchivoAdjuntoBean> listaArchivoAdjunto= new ArrayList<ArchivoAdjuntoBean>();
	private List<ArchivoAdjuntoBean> listaArchivoAdjuntoContrato= new ArrayList<ArchivoAdjuntoBean>();
	/**download**/
	private List<ArchivoAdjuntoBean> listaArchivosReferencia= new ArrayList<ArchivoAdjuntoBean>();
	private List<ArchivoAdjuntoBean> listaArchivosReferenciaFilter;
	private ArchivoAdjuntoBean  archivoAdjuntoBean=new ArchivoAdjuntoBean();
	
	private String entidad;
	private int identificador;
	private int idcontrato=0, idupa=0;
	private String observacion;

	private Boolean disabledUpload=Boolean.TRUE;
	
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	

	public void abrirDialogoAsociarAdjunto(String entidad,int identificador){
		System.out.println("entidad="+entidad);
		System.out.println("identificador="+identificador);
		if(entidad.equals(Constantes.CONTRATO_SGI)){
			if(idcontrato==0){
				idcontrato=identificador;
			}else{ if(idcontrato==identificador){
				 
			}else{
				listaArchivoAdjunto.clear();
				idcontrato=identificador;
			}
				
			}
		}else if(entidad.equals(Constantes.UPA_SGI)){
			if(idupa==0){
				idupa=identificador;
			}else{ if(idupa==identificador){
				 
			}else{
				listaArchivoAdjunto.clear();
				idupa=identificador;
			}
				
			}
		}
		if (identificador!=0) {

			RequestContext requestContext=RequestContext.getCurrentInstance();
			requestContext.execute("dglAdjuntarArchivo.show();");
			
			this.identificador=identificador;
			this.entidad=entidad;
			
		} else {
			
			addWarnMessage("","Usted debe seleccionar un/una  "+entidad+". Si aún no ha creado el/la "+entidad +" debera primero crearlo para adjuntar documento adjunto.");
		}
		
	}
	public void abrirDialogoAsociarAdjuntoContrato(){
		
//		if (identificador!=0) {

			RequestContext requestContext=RequestContext.getCurrentInstance();
			requestContext.execute("dglAdjuntarArchivoContrato.show();");
			
//			this.identificador=identificador;
//			this.entidad=entidad;
//			
//		} else {
//			
//			addWarnMessage("","Usted debe seleccionar un/una  "+entidad+". Si aún no ha creado el/la "+entidad +" debera primero crearlo para adjuntar documento adjunto.");
//		}
		
	}
	public void abrirDialogoVerAdjunto(String entidad,int identificador){
		
		
		
		if (identificador!=0) {
			try {
				
				listaArchivosReferencia=administrarArchivoService.obtenerArchivosReferencia(identificador,entidad);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			RequestContext requestContext=RequestContext.getCurrentInstance();
			
			requestContext.update(":frmVerDocumento:listaArchivosReferencia");
			requestContext.execute("dglVerArchivo.show();");
			
			this.identificador=identificador;
			this.entidad=entidad;
			
		} else {
			
			addWarnMessage("","Usted debe seleccionar un inmueble");
		}
		
	} 
	public void abrirDialogoVerAdjuntoContrato(){
		
		
		
//		if (identificador!=0) {
//			try {
//				
//				listaArchivosReferencia=administrarArchivoService.obtenerArchivosReferencia(identificador,entidad);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			listaArchivosReferencia=listaArchivoAdjuntoContrato;
			RequestContext requestContext=RequestContext.getCurrentInstance();
			
			requestContext.update(":frmVerDocumento:listaArchivosReferencia");
			requestContext.execute("dglVerArchivo.show();");
			
//			this.identificador=identificador;
//			this.entidad=entidad;
			
//		} else {
//			
//			addWarnMessage("","Usted debe seleccionar un inmueble");
//		}
		
	}
	
	public void activarUpload(){
		
		if (archivoAdjuntoBean.getTitulo()!=null && archivoAdjuntoBean.getDescripcion()!=null && !archivoAdjuntoBean.getTitulo().equals("") && !archivoAdjuntoBean.getDescripcion().equals("")) {
			disabledUpload=Boolean.FALSE;
		}else {
			disabledUpload=Boolean.TRUE;
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.update("frmAdjuntarDocumento:uploadFile");
	}
	public void activarUploadContrato(){
		
		if (archivoAdjuntoBean.getTitulo()!=null && archivoAdjuntoBean.getDescripcion()!=null && !archivoAdjuntoBean.getTitulo().equals("") && !archivoAdjuntoBean.getDescripcion().equals("")) {
			disabledUpload=Boolean.FALSE;
		}else {
			disabledUpload=Boolean.TRUE;
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.update("frmAdjuntarArchivo:uploadFileContrato");
	}


	public void agregarAdjunto(FileUploadEvent event) {
	 	archivoAdjuntoBean.setStream(event.getFile().getContents());
	 	archivoAdjuntoBean.setNombre(event.getFile().getFileName());
		archivoAdjuntoBean.setTipo(DataFileUtil.obtenerExtensionArchivo(event.getFile().getFileName()));
		archivoAdjuntoBean.setRuta(Constantes.DIR_PRINCIPAL_SGI);
		//agregar usuario y fecha
		archivoAdjuntoBean.setFeccre(new Date());
		archivoAdjuntoBean.setUsrcre(userMB.getNombrecompleto());
		listaArchivoAdjunto.add(archivoAdjuntoBean);
		
		archivoAdjuntoBean=new ArchivoAdjuntoBean();
    }
	
	public void validarGrabarAdjunto(){
		if (listaArchivoAdjunto.size()==0) {
			addErrorMessage("", "Usted no hay agregado ningún archivo a la vista previa");
		} else {
			RequestContext requestContext=RequestContext.getCurrentInstance();
			requestContext.execute("dlgConfirmacionGrabarAdjunto.show();");
		}
	}
	public void adjuntarArchivo(){
//		if (listaArchivoAdjunto.size()==0) {
//			addErrorMessage("", "Usted no hay agregado ningún archivo a la vista previa");
//		} else {
//			RequestContext requestContext=RequestContext.getCurrentInstance();
//			requestContext.execute("dlgConfirmacionGrabarAdjunto.show();");
//		}
		listaArchivoAdjuntoContrato=listaArchivoAdjunto;
	}
	public  void grabarAdjunto() {
		
		try{
			
			for (ArchivoAdjuntoBean archivoAdjuntoBean : listaArchivoAdjunto) {
				archivoAdjuntoBean.setFeccre(new Date());
				archivoAdjuntoBean.setUsrcre(userMB.getNombrecompleto());
				archivoAdjuntoBean.setRuta(Constantes.DIR_PRINCIPAL_SGI);
	
				administrarArchivoService.grabarArchivo(archivoAdjuntoBean,entidad,identificador);
				
			}
			if(entidad.equals(Constantes.CONTRATO_SGI)||entidad.equals(Constantes.CONDICION_SGI)){
				//actualizar contrato
				administrarArchivoService.actualizarestadodocumentocontrato(identificador);
				
			}else {
				administrarArchivoService.actualizarEstadoxDocumentoReferencia(entidad, identificador);
			}
			addInfoMessage("", "Se subieron archivos exitosamente.");
			
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
		
//		Integer subidosExitosos=0;
//		
//
//		
//		if (listaArchivoAdjunto.size()==subidosExitosos) {
//			addInfoMessage("Éxito", "Se subieron archivos exitosamente, se agregaron al repositorio "+subidosExitosos+" exitosamente");
//		
//		}else if(subidosExitosos==0) {
//			addFatalMessage("Error al subir archivo ", "");
//		
//		}else if(listaArchivoAdjunto.size()>subidosExitosos){
//			addWarnMessage("Parcialmente", "");
//		
//		}
		
		
		listaArchivoAdjunto.clear();
	}
	public void limpiarDocumentoADjuntos(){
		listaArchivoAdjunto.clear();
	}
	public  void grabarDocumentoAdjunto(String entidad, int identificador) {
		
		try{
			
			for (ArchivoAdjuntoBean archivoAdjuntoBean : listaArchivoAdjunto) {
				archivoAdjuntoBean.setFeccre(new Date());
				archivoAdjuntoBean.setUsrcre(userMB.getNombrecompleto());
				archivoAdjuntoBean.setRuta(Constantes.DIR_PRINCIPAL_SGI);
	
				administrarArchivoService.grabarArchivo(archivoAdjuntoBean,entidad,identificador);
				
			}
			
			addInfoMessage("", "Se subieron archivos exitosamente.");
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
		
		
		listaArchivoAdjunto.clear();
	}
	public StreamedContent descargarAdjunto(ArchivoAdjuntoBean adjuntoBean) throws Exception{
		
		StreamedContent streamedFile=null;
		
		try{
			
			byte[] archivoAdjunto = administrarArchivoService.descargaArchivoSftp(adjuntoBean.getUuidSftp().concat(".").concat(adjuntoBean.getTipo()), adjuntoBean.getFeccre());
			
			InputStream inputStream= new ByteArrayInputStream(archivoAdjunto);
				
				if(archivoAdjunto != null){
					String mimetype = FacesContext.getCurrentInstance().getExternalContext().getMimeType(adjuntoBean.getUuidSftp().concat(adjuntoBean.getTipo()));
					
					streamedFile=new DefaultStreamedContent(inputStream, mimetype,adjuntoBean.getNombre());
				}else{
					addWarnMessage(null, "El documento no se encuentra generado.");
				}
		
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

		return streamedFile;
		
	}
	public StreamedContent descargarAdjunto(ArchivoAdjuntoBean adjuntoBean,String ruta) throws Exception{
		
		StreamedContent streamedFile=null;
		
		try{
			
			byte[] archivoAdjunto = administrarArchivoService.descargaArchivoSftp(adjuntoBean.getUuidSftp().concat(".").concat(adjuntoBean.getTipo()), adjuntoBean.getFeccre(),ruta);
			
			InputStream inputStream= new ByteArrayInputStream(archivoAdjunto);
				
				if(archivoAdjunto != null){
					String mimetype = FacesContext.getCurrentInstance().getExternalContext().getMimeType(adjuntoBean.getUuidSftp().concat(adjuntoBean.getTipo()));
					
					streamedFile=new DefaultStreamedContent(inputStream, mimetype,adjuntoBean.getNombre());
				}else{
					addWarnMessage(null, "El documento no se encuentra generado.");
				}
		
		} catch (Exception e) {
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}

		return streamedFile;
		
	}
	
	public void openAlfrescoShare(String uuidAlfresco){
		String urlAlfrescoShare="http://"+inicioService.getParamConfigAlfrescoBean().getHost()+":"+inicioService.getParamConfigAlfrescoBean().getPort()+"/share/page/document-details?nodeRef=workspace://SpacesStore/"+uuidAlfresco;
		RequestContext.getCurrentInstance().execute("window.open('"+urlAlfrescoShare+"')");
		
	}
	

	/**
	 * @return the listaArchivoAdjunto
	 */
	public List<ArchivoAdjuntoBean> getListaArchivoAdjunto() {
		return listaArchivoAdjunto;
	}

	/**
	 * @param listaArchivoAdjunto the listaArchivoAdjunto to set
	 */
	public void setListaArchivoAdjunto(List<ArchivoAdjuntoBean> listaArchivoAdjunto) {
		this.listaArchivoAdjunto = listaArchivoAdjunto;
	}

	/**
	 * @return the listaArchivosReferencia
	 */
	public List<ArchivoAdjuntoBean> getListaArchivosReferencia() {
		return listaArchivosReferencia;
	}

	/**
	 * @param listaArchivosReferencia the listaArchivosReferencia to set
	 */
	public void setListaArchivosReferencia(
			List<ArchivoAdjuntoBean> listaArchivosReferencia) {
		this.listaArchivosReferencia = listaArchivosReferencia;
	}

	/**
	 * @return the archivoAdjuntoBean
	 */
	public ArchivoAdjuntoBean getArchivoAdjuntoBean() {
		return archivoAdjuntoBean;
	}

	/**
	 * @param archivoAdjuntoBean the archivoAdjuntoBean to set
	 */
	public void setArchivoAdjuntoBean(ArchivoAdjuntoBean archivoAdjuntoBean) {
		this.archivoAdjuntoBean = archivoAdjuntoBean;
	}


	/**
	 * @return the administrarArchivoService
	 */
	public AdministrarArchivoService getAdministrarArchivoService() {
		return administrarArchivoService;
	}


	/**
	 * @param administrarArchivoService the administrarArchivoService to set
	 */
	public void setAdministrarArchivoService(
			AdministrarArchivoService administrarArchivoService) {
		this.administrarArchivoService = administrarArchivoService;
	}


	/**
	 * @return the userMB
	 */
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}


	/**
	 * @param userMB the userMB to set
	 */
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	/**
	 * @return the disabledUpload
	 */
	public Boolean getDisabledUpload() {
		return disabledUpload;
	}

	/**
	 * @param disabledUpload the disabledUpload to set
	 */
	public void setDisabledUpload(Boolean disabledUpload) {
		this.disabledUpload = disabledUpload;
	}

	/**
	 * @return the entidad
	 */
	public String getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the identificador
	 */
	public int getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the vistaPrevia
	 */
	public StreamedContent getVistaPrevia() {
		return vistaPrevia;
	}

	/**
	 * @param vistaPrevia the vistaPrevia to set
	 */
	public void setVistaPrevia(StreamedContent vistaPrevia) {
		this.vistaPrevia = vistaPrevia;
	}

	public InicioService getInicioService() {
		return inicioService;
	}

	public void setInicioService(InicioService inicioService) {
		this.inicioService = inicioService;
	}

	public List<ArchivoAdjuntoBean> getListaArchivosReferenciaFilter() {
		return listaArchivosReferenciaFilter;
	}

	public void setListaArchivosReferenciaFilter(
			List<ArchivoAdjuntoBean> listaArchivosReferenciaFilter) {
		this.listaArchivosReferenciaFilter = listaArchivosReferenciaFilter;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


}
