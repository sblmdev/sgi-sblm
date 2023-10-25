package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.NotificacionBean;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.BandejaService;
import pe.gob.sblm.sgi.util.CompDataModel;
import pe.gob.sblm.sgi.util.PropiedadesAlfresco;


@ManagedBean(name = "bandejaMB")
@ViewScoped
public class BandejaManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;

	@ManagedProperty(value = "#{bandejaService}")
	private transient BandejaService bandejaService;

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
 
	
	private int contadorFecha;
	private String selectIdRegistroAuditoria="0";
	private Boolean estadoNotificacion=Boolean.FALSE;
	private List<Auditoria> listNotificacionesPendientesInit;
	private Auditoria selectRegistroAuditoria;
	private CompDataModel compDataModel;

	private int nroNotificacionesMes;
	private ArrayList<String> fechaSinFormato;
	private String mesActual;

	private String mesSeleccionado="";
	private String anioSeleccionado="";
	private Date instanteFecha;
	private String mensajedeNotificaciones;
	private Usuario selectRegistroUsuario;
	private List<Usuario> listadousuariosSeleccionados;
	private String contenidoMensajePersonalizado;
	private int indicesalvador;
	private int idUsuarioSeleccionado;
	
	
	
	private List<NotificacionBean> notificaciones;
	private NotificacionBean notificacion;
	private int nroLeido;
	private int nroPendiente;
	
	

	public void openAlfrescoShare(String uuidAlfresco){
		String urlAlfrescoShare="http://"+PropiedadesAlfresco.getString("ip")+":"+PropiedadesAlfresco.getString("puerto")+"/share/page/document-details?nodeRef=workspace://SpacesStore/"+uuidAlfresco;
		RequestContext.getCurrentInstance().execute("window.open('"+urlAlfrescoShare+"')");
		
	}

	public BandejaManagedBean(){

	}
	
	
	@PostConstruct
	public void initObjects(){
		
		obtenerInformacionNotificaciones();
	}
	
	public Boolean sipgNotificaciones () {
		String urlpagNotificaciones=FuncionesHelper.getURL();
		urlpagNotificaciones=urlpagNotificaciones.substring(urlpagNotificaciones.lastIndexOf("/pg")+1);
		
		if (urlpagNotificaciones.equals("pgNotificaciones.jsf")) {
			return true;
		}else {
			return false;
		}
			
	}

	
	public void actualizarPendiente(){
		
		bandejaService.actualizarPendienteToRevisado(selectRegistroAuditoria.getIdauditoria());
		obtenerInformacionNotificaciones();
	
	}

	
	public void cambiarEstadoNotificacion(){
		
		bandejaService.cambiarEstadoNotificacion(notificacion.getIdNotificacion());
		obtenerInformacionNotificaciones();
	}

	
	
	public void seleccionIdAuditoria() {

		try {
			setSelectIdRegistroAuditoria(((Integer.toString(selectRegistroAuditoria.getIdauditoria()))));
		} catch (Exception e) {
		}
				
	} 
	
	
	public void obtenerInformacionNotificaciones(){
		nroPendiente=bandejaService.nroNotificacionesPendiente(userMB.getUsuariologueado().getIdusuario());
		nroLeido= bandejaService.nroNotificacionesRevisado(userMB.getUsuariologueado().getIdusuario());
		
		notificaciones=bandejaService.obtenerNotificacionesxEstado(estadoNotificacion, userMB.getUsuariologueado().getIdusuario());
		
	}
	
	public void inicializarNotificacionCompuesto(){
		listadousuariosSeleccionados.clear();
		setMensajedeNotificaciones("");
	}
	

	
	public void eliminarUsuarioDeLista(ActionEvent event) {
		
		for(int i=0;i<listadousuariosSeleccionados.size();i++){
			
			if(listadousuariosSeleccionados.get(i)==getSelectRegistroUsuario()){
				listadousuariosSeleccionados.remove(i);
			}
		}

	}
	
	public void agregarUsuarioLista(){
		boolean usuarioVacio = false;

		if (!usuarioVacio) {
		}
			Usuario Usu = new Usuario();
			Usu.setEmailusr("Escriba Nombre Usuario");
			Usu.setRutaimgusr("default.jpg");
			Usu.setContrasenausr("deleteUsuario.png");
			listadousuariosSeleccionados.add(Usu);
			setIndicesalvador(listadousuariosSeleccionados.size()-1);
		}
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Auditoria> getListNotificacionesPendientesInit() {
		return listNotificacionesPendientesInit;
	}
	public void setListNotificacionesPendientesInit(List<Auditoria> listNotificacionesPendientesInit) {
		this.listNotificacionesPendientesInit = listNotificacionesPendientesInit;
	}
	public String getSelectIdRegistroAuditoria() {
		return selectIdRegistroAuditoria;
	}
	public void setSelectIdRegistroAuditoria(String selectIdRegistroAuditoria) {
		this.selectIdRegistroAuditoria = selectIdRegistroAuditoria;
	}
	public Auditoria getSelectRegistroAuditoria() {
		return selectRegistroAuditoria;
	}
	public void setSelectRegistroAuditoria(Auditoria selectRegistroAuditoria) {
		this.selectRegistroAuditoria = selectRegistroAuditoria;
	}
	public CompDataModel getCompDataModel() {
		return compDataModel;
	}
	public void setCompDataModel(CompDataModel compDataModel) {
		this.compDataModel = compDataModel;
	}

	/**
	 * @return the estadoNotificacion
	 */
	public Boolean getEstadoNotificacion() {
		return estadoNotificacion;
	}
	/**
	 * @param estadoNotificacion the estadoNotificacion to set
	 */
	public void setEstadoNotificacion(Boolean estadoNotificacion) {
		this.estadoNotificacion = estadoNotificacion;
	}
	public int getContadorFecha() {
		return contadorFecha;
	}
	public void setContadorFecha(int contadorFecha) {
		this.contadorFecha = contadorFecha;
	}
	public ArrayList<String> getFechaSinFormato() {
		return fechaSinFormato;
	}
	public void setFechaSinFormato(ArrayList<String> fechaSinFormato) {
		this.fechaSinFormato = fechaSinFormato;
	}
	
	/**
	 * @return the nroLeido
	 */
	public int getNroLeido() {
		return nroLeido;
	}

	/**
	 * @param nroLeido the nroLeido to set
	 */
	public void setNroLeido(int nroLeido) {
		this.nroLeido = nroLeido;
	}

	public int getNroPendiente() {
		return nroPendiente;
	}
	public void setNroPendiente(int nroPendiente) {
		this.nroPendiente = nroPendiente;
	}
	public String getMesActual() {
		return mesActual;
	}
	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public String getMesSeleccionado() {
		return mesSeleccionado;
	}
	public void setMesSeleccionado(String mesSeleccionado) {
		this.mesSeleccionado = mesSeleccionado;
	}

	public String getAnioSeleccionado() {
		return anioSeleccionado;
	}
	public void setAnioSeleccionado(String anioSeleccionado) {
		this.anioSeleccionado = anioSeleccionado;
	}
	public Date getInstanteFecha() {
		return instanteFecha;
	}
	public void setInstanteFecha(Date instanteFecha) {
		this.instanteFecha = instanteFecha;
	}
	public String getMensajedeNotificaciones() {
		return mensajedeNotificaciones;
	}
	public void setMensajedeNotificaciones(String mensajedeNotificaciones) {
		this.mensajedeNotificaciones = mensajedeNotificaciones;
	}
	public int getNroNotificacionesMes() {
		return nroNotificacionesMes;
	}
	public void setNroNotificacionesMes(int nroNotificacionesMes) {
		this.nroNotificacionesMes = nroNotificacionesMes;
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
	public String getContenidoMensajePersonalizado() {
		return contenidoMensajePersonalizado;
	}
	public void setContenidoMensajePersonalizado(
			String contenidoMensajePersonalizado) {
		this.contenidoMensajePersonalizado = contenidoMensajePersonalizado;
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



	/**
	 * @return the bandejaService
	 */
	public BandejaService getBandejaService() {
		return bandejaService;
	}
	/**
	 * @param bandejaService the bandejaService to set
	 */
	public void setBandejaService(BandejaService bandejaService) {
		this.bandejaService = bandejaService;
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
	 * @return the notificaciones
	 */
	public List<NotificacionBean> getNotificaciones() {
		return notificaciones;
	}

	/**
	 * @param notificaciones the notificaciones to set
	 */
	public void setNotificaciones(List<NotificacionBean> notificaciones) {
		this.notificaciones = notificaciones;
	}

	/**
	 * @return the notificacion
	 */
	public NotificacionBean getNotificacion() {
		return notificacion;
	}

	/**
	 * @param notificacion the notificacion to set
	 */
	public void setNotificacion(NotificacionBean notificacion) {
		this.notificacion = notificacion;
	}

}
