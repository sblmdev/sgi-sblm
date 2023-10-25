package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.service.IPerfilService;
import pe.gob.sblm.sgi.service.IPerfilUsuarioService;

@ManagedBean(name = "perfilusuarioMB")
@SessionScoped
public class PerfilUsuarioManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{perfilusuarioService}")
	private transient IPerfilUsuarioService perfilusuarioService;
	private Perfilusuario perfilusuario;
	private List<Perfilusuario> perfilusuarios;
	
	private List<Perfilusuario> perfilusuariosporid;
	@ManagedProperty(value = "#{perfilService}")
	private transient IPerfilService perfilService;
	private Perfil perfil;
	private Perfilusuario [] listadoperfilusuarios;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	 private int idperfilseleccionado;
	
	
	public void cambiarPerfil() {
		System.out.println("ultimo idperfilseleccionado:"+idperfilseleccionado);
		int idusu=userMB.getUsuariologueado().getIdusuario();
		List <Perfilusuario> listaperusu= new ArrayList <Perfilusuario>();
		listaperusu=getPerfilusuarioService().listarPerfilUsuariosPorId(idusu);
		for (Perfilusuario perfilus : listaperusu) {
			
			if(idperfilseleccionado==perfilus.getPerfil().getIdperfil()){
				perfilus.setActivo(true);
				getPerfilusuarioService().actualizarPerfilUsuario(perfilus);
			}else{
				perfilus.setActivo(false);
				getPerfilusuarioService().actualizarPerfilUsuario(perfilus);
			}
		}
		userMB.getUsuariologueado().setIdusuario(idusu);
		userMB.obtenerPerfil();
		userMB.obtenerMenu();
		
		
	}
	public void registrarPerfilUsuario() {
		registrarPerfil();
		getPerfilService().obtenerUltimoIdPerfil();
	

		FacesMessage msg = new FacesMessage("Id :" + perfilusuario.getIdperfilusuario());
		getPerfilusuarioService().registrarPerfilUsuario(perfilusuario);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	public void registrarPerfil( ) {
		
		FacesMessage msg = new FacesMessage("Id :" + perfil.getIdperfil());
		getPerfilService().registrarPerfil(perfil);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	public IPerfilUsuarioService getPerfilusuarioService() {
		return perfilusuarioService;
	}
	public void setPerfilusuarioService(IPerfilUsuarioService perfilusuarioService) {
		this.perfilusuarioService = perfilusuarioService;
	}
	public Perfilusuario getPerfilusuario() {
		if (perfilusuario == null) {
			perfilusuario = new Perfilusuario	();
		}
		return perfilusuario;
	}
	public void setPerfilusuario(Perfilusuario perfilusuario) {
		this.perfilusuario = perfilusuario;
	}
	public List<Perfilusuario> getPerfilusuarios() {
		perfilusuarios = getPerfilusuarioService().listarPerfilUsuarios();
		return perfilusuarios;
	}
	public void setPerfilusuarios(List<Perfilusuario> perfilusuarios) {
		this.perfilusuarios = perfilusuarios;
	}
	public IPerfilService getPerfilService() {
		return perfilService;
	}
	public void setPerfilService(IPerfilService perfilService) {
		this.perfilService = perfilService;
	}
	public Perfil getPerfil() {
		if (perfil == null) {
			perfil = new Perfil	();
		}
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Perfilusuario[] getListadoperfilusuarios() {
		return listadoperfilusuarios;
	}
	public void setListadoperfilusuarios(Perfilusuario[] listadoperfilusuarios) {
		this.listadoperfilusuarios = listadoperfilusuarios;
	}
	public List<Perfilusuario> getPerfilusuariosporid() {
		int idusuariolog=userMB.getUsuariologueado().getIdusuario();
		perfilusuariosporid = getPerfilusuarioService().listarPerfilUsuariosPorId(idusuariolog);
		return perfilusuariosporid;
	}
	public void setPerfilusuariosporid(List<Perfilusuario> perfilusuariosporid) {
		this.perfilusuariosporid = perfilusuariosporid;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public int getIdperfilseleccionado() {
		return idperfilseleccionado;
	}
	public void setIdperfilseleccionado(int idperfilseleccionado) {
		this.idperfilseleccionado = idperfilseleccionado;
	}


//	public int getIdusuario() {
//		return idusuario;
//	}
//
//	public void setIdusuario(int idusuario) {
//		this.idusuario = idusuario;
//	}
	
	
}
