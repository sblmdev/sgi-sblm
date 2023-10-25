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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.DataFileUtil;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Ubigeo;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.IInmuebleService;

@ManagedBean(name = "subinmuebleMB")
@ViewScoped
public class SubInmuebleManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	
	@ManagedProperty(value = "#{inmuebleService}")
	private transient IInmuebleService inmuebleService;
	
	@ManagedProperty(value = "#{administrarArchivoService}")
	private transient AdministrarArchivoService administrarArchivoService;
	
	private List<String> listaDepartamentos;
	private List<String> listaProvincias;
	private List<String> listaDistritos;
	private String depa;
	private String prov;
	private String dist;
	
	private InmuebleBean inmuebleBean= new InmuebleBean();
	private List<InmuebleBean> listaInmuebleBean= new ArrayList<InmuebleBean>();
	private List<InmuebleBean> listaInmuebleBeanFilter;
	

	
	/**Adjuntar**/
	private UploadedFile file;
	private List<ArchivoAdjuntoBean> listaArchivoAdjunto= new ArrayList<ArchivoAdjuntoBean>();
	private List<ArchivoAdjuntoBean> listaArchivosReferencia= new ArrayList<ArchivoAdjuntoBean>();
	private ArchivoAdjuntoBean  archivoAdjuntoBean=new ArchivoAdjuntoBean();
	
	/**Mapa**/
	private MapModel simpleModel;
	private String descripcionLocalizacion;
	
	/**Busqueda**/
	private String valorbusqueda;
	private String tipoBusqueda=Constantes.BUSQUEDA_DIRECCION;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init(){
		
		incluirLocalizacionPorDefecto();
		obtenerUbicacionGmap();
	listaDepartamentos = inmuebleService.listarDepartamentos();
	}
	
	
	public void buscarInmueble(){
		listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusqueda,tipoBusqueda);
	}
	
	
	 public void handleFileUpload(FileUploadEvent event) {
		 	archivoAdjuntoBean.setStream(event.getFile().getContents());
			archivoAdjuntoBean.setTipo(DataFileUtil.obtenerExtensionArchivo(event.getFile().getFileName()));
		 	
	    }
	 
	 public void limpiarAgregarArchivoAdjunto(){
			archivoAdjuntoBean=new ArchivoAdjuntoBean();
	 }
	
	public void validarGrabarInmueble(){
		FacesContext context = FacesContext.getCurrentInstance();
		if ( inmuebleBean.getDireccion()==null || inmuebleBean.getDireccion().equals("")) {
			
			context.addMessage(null, new FacesMessage("Ingrese dirección del inmueble ","Para proceder a registrar inmueble es obligatorio ingresar el item indicado"));
		
		}
//		else if (inmuebleBean.getNumeroprincipal()==null ||  inmuebleBean.getNumeroprincipal().equals("")){
//			context.addMessage(null, new FacesMessage("Ingrese número principal/ del inmueble ","Para proceder a registrar inmueble es obligatorio ingresar el item indicado"));
//		
//		}
		else if (depa.equals("0") || prov.equals("0") || dist.equals("0")) {
			context.addMessage(null, new FacesMessage("Seleccionar la procedencia del inmueble (departamento,provincia distrito)  ","Para proceder a registrar inmueble es obligatorio seleccionar los items indicados"));
		
		}else if (inmuebleBean.getLatitudlocalizacion().equals("")|| inmuebleBean.getLongitudlocalizacion().equals("")){
			context.addMessage(null, new FacesMessage("Usted no ha ingresado las coordenadas geolocalización","Para proceder a registrar inmueble es obligatorio especificar las coordenadas de geolocalización"));
			
		}else{
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dlgRegistrarInmueble.show();");
		}
	}
	
	
	public void grabarInmueble(){
		Inmueble inmueble= new Inmueble();
		Ubigeo ubigeo= new Ubigeo();
		ubigeo.setUbigeo(getInmuebleService().obtenerIdUbigeo(depa, prov,dist));
		
		inmueble.setDireccion(inmuebleBean.getDireccion());
//		inmueble.setNumeroprincipal(inmuebleBean.getNumeroprincipal());
		inmueble.setUbigeo(ubigeo);
		inmueble.setLongitudlocalizacion(inmuebleBean.getLongitudlocalizacion());
		inmueble.setLatitudlocalizacion(inmuebleBean.getLatitudlocalizacion());
		inmueble.setDescripcionlocalizacion(inmuebleBean.getDescripcionlocalizacion());
		
		
		if (inmuebleBean.getIdinmueble()!=0) {/**Actualizar*/
			inmueble.setIdinmueble(inmuebleBean.getIdinmueble());
			inmueble.setFechcre(inmuebleBean.getFeccre());
			inmueble.setUsrcre(inmuebleBean.getUsrcre());
			inmueble.setFechmod(new Date());
			inmueble.setUsrmod(userMB.getNombrecompleto());
			
			
		}else {/**Nuevo*/
			inmueble.setFechcre(new Date());
			inmueble.setUsrcre(userMB.getNombrecompleto());
			
		}
		
		
//		getInmuebleService().registrarInmueble(inmueble);/**Graba o actualiza*/
		limpiarformulario();
	}
	
	public void validarEliminarInmueble(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inmuebleBean.getIdinmueble()!=0) {
			Inmueble inmueble= new Inmueble();
			inmueble.setIdinmueble(inmuebleBean.getIdinmueble());
			getInmuebleService().eliminarInmueble(inmueble);
			context.addMessage(null, new FacesMessage("Eliminación exitosa","Se ha eliminado exitosamente el registro de inmueble "));
			
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para eliminar ","Para proceder a eliminar inmueble es obligatorio seleccionar un inmueble"));
			limpiarformulario();
		}
	}
	
	public void validarAdjuntarArchivo(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inmuebleBean.getIdinmueble()!=0) {
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dglAdjuntarArchivo.show();");
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para adjuntar ","Para proceder a adjuntar archivo es obligatorio seleccionar un inmueble"));
		}
	}
	
	public void limpiarformulario(){
		depa="0";
		prov="0";
		dist="0";
		
		inmuebleBean= new InmuebleBean();
	}
	
	
	public void agregarAdjunto(){
		listaArchivoAdjunto.add(archivoAdjuntoBean);
	}
	
	public void validarGrabarAdjuntos(){
		if (listaArchivoAdjunto.size()==0) {
			addErrorMessage("Error ", "Usted no hay agregado ningún archivo a la vista previa");
		} else {
			RequestContext requestContext=RequestContext.getCurrentInstance();
			requestContext.execute("dlgRegistroAdjuntos.show();");
		}
	}

	public void validarAgregarAdjunto(){
		if (archivoAdjuntoBean.getTitulo().equals("")) {
			addInfoMessage("Error de validación", "Ingrese título");
		}else if (archivoAdjuntoBean.getDescripcion().equals("")){
			addInfoMessage("Error de validación", "Ingrese descripción");
		}else if (archivoAdjuntoBean.getStream()==null){
			addInfoMessage("Error de validación", "Adjunte archivo");
		}else {
			agregarAdjunto();
			RequestContext requestContext=RequestContext.getCurrentInstance();
			requestContext.execute("dglAgregarArchivoAdjunto.hide();");
		}
		
	}
	
	public  void grabarArchivoAdjunto() throws Exception{
		for (ArchivoAdjuntoBean archivoAdjuntoBean : listaArchivoAdjunto) {
			archivoAdjuntoBean.setFeccre(new Date());
			archivoAdjuntoBean.setUsrcre(userMB.getNombrecompleto());

//			administrarArchivoService.grabarArchivo(archivoAdjuntoBean,"Inmueble",inmuebleBean.getIdinmueble(),inmuebleBean.getDireccion());
		}
		
		listaArchivoAdjunto.clear();
	}
	
	public void obtenerArchivosReferencia() throws Exception{
		listaArchivosReferencia=administrarArchivoService.obtenerArchivosReferencia(inmuebleBean.getIdinmueble(),"Inmueble");
	}
	

	public void obtenerUbicacionGmap(){
		  simpleModel = new DefaultMapModel();
        
		  
		  
		  LatLng coord1 = new LatLng(Double.parseDouble("-12.194171"),Double.parseDouble("-76.9904426"));
		  	      
		  
	      //Basic marker
	      simpleModel.addOverlay(new Marker(coord1, descripcionLocalizacion));
	}

	
	public void validarGrabarLocalizacion(){
		if (inmuebleBean.getLatitudlocalizacion().equals("") || inmuebleBean.getLongitudlocalizacion().equals("")) {
			addWarnMessage("","");
		} else {
			RequestContext requestContext= RequestContext.getCurrentInstance();
			requestContext.execute("");
		}
	}
	
	public void grabarLocalizacion(){
		inmuebleService.grabarLocalizacion(inmuebleBean.getIdinmueble(),inmuebleBean.getLatitudlocalizacion(),inmuebleBean.getLongitudlocalizacion(),inmuebleBean.getDescripcionlocalizacion());
	}
	
	public void incluirLocalizacionPorDefecto(){
		inmuebleBean.setLatitudlocalizacion("-12.049975");
		inmuebleBean.setLongitudlocalizacion("-77.034156");
	}

	public void setearInmuebleSeleccionado() throws Exception{
			depa=inmuebleBean.getDepartamento();
			prov=inmuebleBean.getProvincia();
			dist=inmuebleBean.getDistrito();
			obtenerArchivosReferencia();
			obtenerUbicacionGmap();
			
		}
	

	
	public void cargarComboProvincias() {
		listaProvincias = getInmuebleService().listarProvincias(depa);
	}
	public void cargarComboDistritos() {
		listaDistritos = getInmuebleService().listarDistritos(prov);
	}
	public IInmuebleService getInmuebleService() {
		return inmuebleService;
	}
	public void setInmuebleService(IInmuebleService inmuebleService) {
		this.inmuebleService = inmuebleService;
	}
	public List<String> getListaDepartamentos() {
		return listaDepartamentos;
	}
	public void setListaDepartamentos(List<String> listaDepartamentos) {
		this.listaDepartamentos = listaDepartamentos;
	}
	public List<String> getListaProvincias() {
		return listaProvincias;
	}
	public void setListaProvincias(List<String> listaProvincias) {
		this.listaProvincias = listaProvincias;
	}
	public List<String> getListaDistritos() {
		return listaDistritos;
	}
	public void setListaDistritos(List<String> listaDistritos) {
		this.listaDistritos = listaDistritos;
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
	public String getDepa() {
		return depa;
	}
	public void setDepa(String depa) {
		this.depa = depa;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getDist() {
		return dist;
	}
	public void setDist(String dist) {
		this.dist = dist;
	}
	public InmuebleBean getInmuebleBean() {
		return inmuebleBean;
	}
	public void setInmuebleBean(InmuebleBean inmuebleBean) {
		this.inmuebleBean = inmuebleBean;
	}
	public List<InmuebleBean> getListaInmuebleBean() {
		return listaInmuebleBean;
	}
	public void setListaInmuebleBean(List<InmuebleBean> listaInmuebleBean) {
		this.listaInmuebleBean = listaInmuebleBean;
	}
	public List<InmuebleBean> getListaInmuebleBeanFilter() {
		return listaInmuebleBeanFilter;
	}
	public void setListaInmuebleBeanFilter(List<InmuebleBean> listaInmuebleBeanFilter) {
		this.listaInmuebleBeanFilter = listaInmuebleBeanFilter;
	}
	public String getValorbusqueda() {
		return valorbusqueda;
	}
	public void setValorbusqueda(String valorbusqueda) {
		this.valorbusqueda = valorbusqueda;
	}
	public List<ArchivoAdjuntoBean> getListaArchivoAdjunto() {
		return listaArchivoAdjunto;
	}
	public void setListaArchivoAdjunto(List<ArchivoAdjuntoBean> listaArchivoAdjunto) {
		this.listaArchivoAdjunto = listaArchivoAdjunto;
	}
	public ArchivoAdjuntoBean getArchivoAdjuntoBean() {
		return archivoAdjuntoBean;
	}
	public void setArchivoAdjuntoBean(ArchivoAdjuntoBean archivoAdjuntoBean) {
		this.archivoAdjuntoBean = archivoAdjuntoBean;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public AdministrarArchivoService getAdministrarArchivoService() {
		return administrarArchivoService;
	}
	public void setAdministrarArchivoService(
			AdministrarArchivoService administrarArchivoService) {
		this.administrarArchivoService = administrarArchivoService;
	}


	public List<ArchivoAdjuntoBean> getListaArchivosReferencia() {
		return listaArchivosReferencia;
	}
	public void setListaArchivosReferencia(List<ArchivoAdjuntoBean> listaArchivosReferencia) {
		this.listaArchivosReferencia = listaArchivosReferencia;
	}
	public MapModel getSimpleModel() {
		return simpleModel;
	}
	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}


	public String getTipoBusqueda() {
		return tipoBusqueda;
	}


	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
	
}
