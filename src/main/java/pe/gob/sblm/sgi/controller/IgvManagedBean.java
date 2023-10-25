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
import pe.gob.sblm.sgi.service.IIgvService;

@ManagedBean(name = "igvMB")
@ViewScoped
public class IgvManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{igvService}")
	private transient IIgvService igvService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private List<Igv> igvs;
	
	private Igv igv;
	private Igv igvcapturado;
	boolean actualizado = false;
	private Igv resultadoigv;
	private int numigvs;
	
	@PostConstruct
	public void initObjects() {
		listarIgv();
		try {

			ActualizarMensajes();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void listarIgv(){
		
		igvs=getIgvService().listarIgvs();
	}
	
	
	public void ActualizarMensajes() {
		resultadoigv=getIgvService().obtenerUltimoIgv();
		numigvs=getIgvService().obtenerNumeroRegistros();
	}
	
	
	public void registrarIgv() {
			
			if (actualizado == true) {
				String usermodificador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
				igv.setFecmod(new Date());
				igv.setUsrmod(usermodificador);
				getIgvService().registrarIgv(igv);
				FacesMessage msg = new FacesMessage(
						"Se Actualizo correctamente el Igv.");
	
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				String usercreador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
				
				
				igv.setUsrcre(usercreador);
				igv.setFeccre(new Date());
				getIgvService().registrarIgv(igv);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Exito", "Se Registro correctamente el IGV.");
				
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
			}
			// para refrescar el valor en el panel
			ActualizarMensajes();
			listarIgv();
		}


public void limpiarCampos() {
	igv = null;
}

public void onRowSelect(SelectEvent event) {
	actualizado = true;
	
}
public IIgvService getIgvService() {
	return igvService;
}
public void setIgvService(IIgvService igvService) {
	this.igvService = igvService;
}
public UsuarioManagedBean getUserMB() {
	return userMB;
}
public void setUserMB(UsuarioManagedBean userMB) {
	this.userMB = userMB;
}

public Igv getIgv() {
	if (igv == null) {
		igv = new Igv();
	}
	return igv;
}
public void setIgv(Igv igv) {
	this.igv = igv;
}
public Igv getIgvcapturado() {
	return igvcapturado;
}
public void setIgvcapturado(Igv igvcapturado) {
	this.igvcapturado = igvcapturado;
}
public Igv getResultadoigv() {
	return resultadoigv;
}
public void setResultadoigv(Igv resultadoigv) {
	this.resultadoigv = resultadoigv;
}
public int getNumigvs() {
	return numigvs;
}
public void setNumigvs(int numigvs) {
	this.numigvs = numigvs;
}
public List<Igv> getIgvs() {
	return igvs;
}
public void setIgvs(List<Igv> igvs) {
	this.igvs = igvs;
}

}
