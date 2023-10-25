package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.service.IUsoService;

@ManagedBean(name = "usoMB")
@ViewScoped
public class UsoManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{usoService}")
	private transient IUsoService usoService;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private List<Uso> usos;
	
	private Uso uso;
	private Uso usocapturado;
	boolean actualizado = false;
	private Uso resultadouso;
	private int numUsos;
	@PostConstruct
	public void initObjects() {

		try {

			ActualizarMensajes();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void ActualizarMensajes() {
		resultadouso=getUsoService().obtenerUltimoUso();
		numUsos=getUsoService().obtenerNumeroRegistros();
	}
public void registrarUso() {
		
		Date ahora = new Date();
		
		
		if (actualizado == true) {
			System.out.println("::::::entro actualizado");
			String usermodificador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			uso.setFecmod(ahora);
			uso.setUsrmod(usermodificador);
			getUsoService().registrarUso(uso);
			FacesMessage msg = new FacesMessage(
					"Se Actualizo correctamente el Uso.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			System.out.println("::::::entro registro");
			String usercreador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			uso.setUsrcre(usercreador);
			uso.setFeccre(ahora);
			getUsoService().registrarUso(uso);
			FacesMessage msg = new FacesMessage(
					"Se Registro correctamente el Uso.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		// para refrescar el valor en el panel
		ActualizarMensajes();

	}


public void eliminarUso() {
	getUsoService().eliminarUso(usocapturado);
	
	
	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
			"Exito", "Se eliminó el Uso "
					+ usocapturado.getDescripcion()
					+ " correctamente.");

	FacesContext.getCurrentInstance().addMessage(null, msg);
	limpiarCampos();
	ActualizarMensajes();
}
public void limpiarCampos() {

	uso = null;
}

public void onRowSelect(SelectEvent event) {
	actualizado = true;
	
}
	public List<Uso> getUsos() {
		return getUsoService().listarUsos();
	}
	public void setUsos(List<Uso> usos) {
		this.usos = usos;
	}
	
	
	
	public Uso getUso() {
		if (uso == null) {
			uso = new Uso();
		}
		return uso;
	}
	public void setUso(Uso uso) {
		this.uso = uso;
	}
	public IUsoService getUsoService() {
		return usoService;
	}
	public void setUsoService(IUsoService usoService) {
		this.usoService = usoService;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	

	public Uso getUsocapturado() {
		return usocapturado;
	}

	public void setUsocapturado(Uso usocapturado) {
		this.usocapturado = usocapturado;
	}

	

	public Uso getResultadouso() {
		return resultadouso;
	}

	public void setResultadouso(Uso resultadouso) {
		this.resultadouso = resultadouso;
	}

	public int getNumUsos() {
		return numUsos;
	}

	public void setNumUsos(int numUsos) {
		this.numUsos = numUsos;
	}
	
	
	
}
