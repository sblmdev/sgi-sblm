package pe.gob.sblm.sgi.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
	
@ManagedBean(name = "parametrocobranzaMB")
@ViewScoped
public class ParametrosCobranzaManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int nrodiastolerancia;
	private boolean accessgranted=false;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init() {
		nrodiastolerancia=FuncionesHelper.DIASTOLERANCIA;
		if (userMB.nombreperfilSeleccionado.equals("Supervisor S.G.I.")) {
			accessgranted=true;
		}else if (userMB.nombreperfilSeleccionado.equals("Supervisor Recaudación")) {
			accessgranted=true;
		}else if (userMB.nombreperfilSeleccionado.equals("Asistente S.G.I.")) {
			accessgranted=true;
		}
	}
	  
	public void cambiar() {
		FuncionesHelper.DIASTOLERANCIA=nrodiastolerancia;
	}

	public int getNrodiastolerancia() {
		return nrodiastolerancia;
	}

	public void setNrodiastolerancia(int nrodiastolerancia) {
		this.nrodiastolerancia = nrodiastolerancia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isAccessgranted() {
		return accessgranted;
	}

	public void setAccessgranted(boolean accessgranted) {
		this.accessgranted = accessgranted;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	
	
	
}
