package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.entity.Permiso;
import pe.gob.sblm.sgi.service.IPermisoService;

@ManagedBean(name = "permisoMB")
@ViewScoped
public class PermisoManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{permisoService}")
	private transient IPermisoService permisoService;
	private Permiso permiso;
	private List<Permiso> permisos;
	public IPermisoService getPermisoService() {
		return permisoService;
	}
	public void setPermisoService(IPermisoService permisoService) {
		this.permisoService = permisoService;
	}
	public Permiso getPermiso() {
		if (permiso == null) {
			permiso = new Permiso	();
		}
		
		return permiso;
	}
	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}
	public List<Permiso> getPermisos() {
		permisos=getPermisoService().listarPermisos();
		return permisos;
	}
	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}
	
	
	
	
}
