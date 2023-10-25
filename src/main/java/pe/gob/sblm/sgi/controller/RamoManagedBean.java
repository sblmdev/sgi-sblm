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

import pe.gob.sblm.sgi.entity.Igv;
import pe.gob.sblm.sgi.entity.Ramo;
import pe.gob.sblm.sgi.service.IRamoService;

@ManagedBean(name = "ramoMB")
@ViewScoped
public class RamoManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{ramoService}")
	private transient IRamoService ramoService;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private List<Ramo> ramos;
	
	private Ramo ramo;
	private Ramo ramocapturado;
	boolean actualizado = false;
	private Ramo resultadoramo;
	private int numramos;
	@PostConstruct
	public void initObjects() {

		try {

			ActualizarMensajes();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void ActualizarMensajes() {
		resultadoramo=getRamoService().obtenerUltimoRamo();
		numramos=getRamoService().obtenerNumeroRegistros();
	}
public void registrarRamo() {
		
		Date ahora = new Date();
		
		
		if (actualizado == true) {
			System.out.println("::::::entro actualizado");
			String usermodificador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			ramo.setFecmod(ahora);
			ramo.setUsrmod(usermodificador);
			getRamoService().registrarRamo(ramo);
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Actualizo correctamente el Ramo.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			System.out.println("::::::entro registro");
			String usercreador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			ramo.setUsrcre(usercreador);
			ramo.setFeccre(ahora);
			getRamoService().registrarRamo(ramo);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se registro correctamente el Ramo.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		// para refrescar el valor en el panel
		ActualizarMensajes();

	}


public void eliminarRamo() {
	getRamoService().eliminarRamo(ramocapturado);
	
	
	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
			"Exito", "Se eliminó el Ramo "
					+ ramocapturado.getDescripcion()
					+ " correctamente.");

	FacesContext.getCurrentInstance().addMessage(null, msg);
	limpiarCampos();
	ActualizarMensajes();
}
public void limpiarCampos() {

	ramo = null;
}

public void onRowSelect(SelectEvent event) {
	actualizado = true;
	
}
public IRamoService getRamoService() {
	return ramoService;
}
public void setRamoService(IRamoService ramoService) {
	this.ramoService = ramoService;
}
public UsuarioManagedBean getUserMB() {
	return userMB;
}
public void setUserMB(UsuarioManagedBean userMB) {
	this.userMB = userMB;
}
public List<Ramo> getRamos() {
	return getRamoService().listarRamos();
}
public void setRamos(List<Ramo> ramos) {
	this.ramos = ramos;
}
public Ramo getRamo() {
	if (ramo == null) {
		ramo = new Ramo();
	}
	return ramo;
}
public void setRamo(Ramo ramo) {
	this.ramo = ramo;
}
public Ramo getRamocapturado() {
	return ramocapturado;
}
public void setRamocapturado(Ramo ramocapturado) {
	this.ramocapturado = ramocapturado;
}
public Ramo getResultadoramo() {
	return resultadoramo;
}
public void setResultadoramo(Ramo resultadoramo) {
	this.resultadoramo = resultadoramo;
}
public int getNumramos() {
	return numramos;
}
public void setNumramos(int numramos) {
	this.numramos = numramos;
}
}
