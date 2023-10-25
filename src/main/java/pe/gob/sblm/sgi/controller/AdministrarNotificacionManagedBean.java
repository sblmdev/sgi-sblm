package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.entity.MaeAccion;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.AccionService;
import pe.gob.sblm.sgi.service.IUsuarioService;


@ManagedBean(name = "administrarNotificacionMB")
@ViewScoped
public class AdministrarNotificacionManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;

	@ManagedProperty(value = "#{accionService}")
	private transient AccionService accionService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private transient IUsuarioService usuarioService;

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	
	private int idaccionSeleccionado;
	private List<MaeAccion> listaAccion;
	private List<AccionUsuarioBean> listaUsuariosAccion;
	private AccionUsuarioBean accionUsuarioBean;
	
	private int idusuarioSeleccionado;
	private List<Usuario> listaUsuarios;
	
	
	
	
	@PostConstruct
	public void init(){
		
		listaAccion=accionService.obtenerAcciones();
		listaUsuarios=usuarioService.listarUsuarios();
		
	}
	
	public void obtenerUsuarioAccion(){
		
		listaUsuariosAccion=accionService.obtenerUsuarioAccion(idaccionSeleccionado);
		
	}
	
	public void grabarAgregarUsuarioNotificacion(){
		
		if (idusuarioSeleccionado==0) {
			addWarnMessage("Error Validación", "Debe seleccionar un usuario para continuar");
		} else {
			Boolean siUsuarioAgregado=Boolean.FALSE;
			for (AccionUsuarioBean accionUsuarioBean : listaUsuariosAccion) {
				if (accionUsuarioBean.getIdusuario()==idusuarioSeleccionado) {
					addWarnMessage("Error","Usuario ya se encuentra asociado a acción");
					siUsuarioAgregado=Boolean.TRUE;
				}
			}
			if (!siUsuarioAgregado) {
				
				accionService.grabarUsuarioAccion(idusuarioSeleccionado,idaccionSeleccionado);
				obtenerUsuarioAccion();
				RequestContext requestContext= RequestContext.getCurrentInstance();
				requestContext.execute("dlgAgregarUsuario.hide();");
				addInfoMessage("Éxito", "Usted a agregado usuario a acción satisfactoriamente. ");
			}
		}
		
	}
	
	public void grabarQuitarUsuarioNotificacion(){
		
		accionService.eliminarUsuarioAccion(accionUsuarioBean.getIdusuario(),idaccionSeleccionado);
		obtenerUsuarioAccion();
		RequestContext requestContext= RequestContext.getCurrentInstance();
		requestContext.execute("dlgQuitarUsuario.hide();");
		addInfoMessage("Éxito", "Usted a quitado usuario a acción satisfactoriamente. ");

	}
	
	public void agregarUsuarioNotificacion(){
		if (idaccionSeleccionado==0) {
			addWarnMessage("Error","Usted debe seleccionar una acción para continuar");
		} else {
		
				RequestContext requestContext= RequestContext.getCurrentInstance();
				requestContext.execute("dlgAgregarUsuario.show();");

		}
		
	}
	
	public void quitarUsuarioNotificacion(){
		if (accionUsuarioBean==null) {
			addWarnMessage("Error","Usted debe seleccionar un usuario para continuar");
		} else {
			RequestContext requestContext= RequestContext.getCurrentInstance();
			requestContext.execute("dlgQuitarUsuario.show();");

		}
		
	}
	
	
	
	

	/**
	 * @return the accionService
	 */
	public AccionService getAccionService() {
		return accionService;
	}

	/**
	 * @param accionService the accionService to set
	 */
	public void setAccionService(AccionService accionService) {
		this.accionService = accionService;
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
	 * @return the listaAccion
	 */
	public List<MaeAccion> getListaAccion() {
		return listaAccion;
	}

	/**
	 * @param listaAccion the listaAccion to set
	 */
	public void setListaAccion(List<MaeAccion> listaAccion) {
		this.listaAccion = listaAccion;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the listaUsuariosAccion
	 */
	public List<AccionUsuarioBean> getListaUsuariosAccion() {
		return listaUsuariosAccion;
	}

	/**
	 * @param listaUsuariosAccion the listaUsuariosAccion to set
	 */
	public void setListaUsuariosAccion(List<AccionUsuarioBean> listaUsuariosAccion) {
		this.listaUsuariosAccion = listaUsuariosAccion;
	}

 

	/**
	 * @return the usuarioService
	 */
	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	/**
	 * @param usuarioService the usuarioService to set
	 */
	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * @return the listaUsuarios
	 */
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * @param listaUsuarios the listaUsuarios to set
	 */
	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	/**
	 * @return the idusuarioSeleccionado
	 */
	public int getIdusuarioSeleccionado() {
		return idusuarioSeleccionado;
	}

	/**
	 * @param idusuarioSeleccionado the idusuarioSeleccionado to set
	 */
	public void setIdusuarioSeleccionado(int idusuarioSeleccionado) {
		this.idusuarioSeleccionado = idusuarioSeleccionado;
	}

	/**
	 * @return the idaccionSeleccionado
	 */
	public int getIdaccionSeleccionado() {
		return idaccionSeleccionado;
	}

	/**
	 * @param idaccionSeleccionado the idaccionSeleccionado to set
	 */
	public void setIdaccionSeleccionado(int idaccionSeleccionado) {
		this.idaccionSeleccionado = idaccionSeleccionado;
	}

	/**
	 * @return the accionUsuarioBean
	 */
	public AccionUsuarioBean getAccionUsuarioBean() {
		return accionUsuarioBean;
	}

	/**
	 * @param accionUsuarioBean the accionUsuarioBean to set
	 */
	public void setAccionUsuarioBean(AccionUsuarioBean accionUsuarioBean) {
		this.accionUsuarioBean = accionUsuarioBean;
	}
 
	
}
