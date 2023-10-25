package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.service.IPerfilService;

@ManagedBean(name = "perfilMB")
@ViewScoped
public class PerfilManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{perfilService}")
	private transient IPerfilService perfilService;

	private Perfil perfil;
	private List<Perfil> perfiles;
	
	private String ultimoPerfil;
	private Date fechaultimoPerfil;
	private int numPerfiles;
	@PostConstruct
	public void initObjects() {

		try {
			numPerfiles = getPerfilService().obtenerNumeroPerfiles();
			ultimoPerfil = getPerfilService().obtenerUltimoPerfil();
			fechaultimoPerfil = getPerfilService().obtenerFechaUltimoPerfil();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void onRowSelect(SelectEvent event) {
		
		
	}

	public void registrarPerfil() {
		System.out.println(":::::registrar  Perfil  MB:::::");
		
		getPerfilService().registrarPerfil(perfil);
		
	}

	public IPerfilService getPerfilService() {
		return perfilService;
	}

	public void setPerfilService(IPerfilService perfilService) {
		this.perfilService = perfilService;
	}

	public Perfil getPerfil() {
		if (perfil == null) {
			perfil = new Perfil();
		}
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getPerfiles() {
		perfiles = getPerfilService().listarPerfiles();
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}


	public String getUltimoPerfil() {
		return ultimoPerfil;
	}


	public void setUltimoPerfil(String ultimoPerfil) {
		this.ultimoPerfil = ultimoPerfil;
	}


	public Date getFechaultimoPerfil() {
		return fechaultimoPerfil;
	}


	public void setFechaultimoPerfil(Date fechaultimoPerfil) {
		this.fechaultimoPerfil = fechaultimoPerfil;
	}


	public int getNumPerfiles() {
		return numPerfiles;
	}


	public void setNumPerfiles(int numPerfiles) {
		this.numPerfiles = numPerfiles;
	}


	

}
