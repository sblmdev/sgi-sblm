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

import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.service.IRepresentanteService;

@ManagedBean(name = "representanteMB")
@ViewScoped
public class RepresentanteManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{representanteService}")
	private transient IRepresentanteService representateService;
	Representante representante;
	List<Representante> listaRepresentantes;
	private int nrorepresentantes;
	private Representante ultimoRepresentante;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;

	

	@PostConstruct
	public void initObjects() {
		representante = new Representante();
		obtenerRepresentantes();
		obtenerUltimaRepresentanteCreada();
		nrorepresentantes();
	}
	
	public void obtenerRepresentantes(){
		listaRepresentantes=representateService.obtenerTodosRepresentantes();
	}
	
	public void obtenerUltimaRepresentanteCreada() {
		ultimoRepresentante=listaRepresentantes.get(0);
	}
	
	public void limpiarRegistro() {
		representante= new Representante();
		representante.setIdrepresentante(0);
	}
	
	public void grabarRepresentante(){
		
		if(representante.getIdrepresentante()==0){
			representante.setFechacreacion(new Date());
			representante.setUsuariocreador(userMB.getUsuariologueado().getNombrescompletos());
			representante.setNombrescompletos(representante.getNombres()+" "+representante.getApellidos());
			representateService.grabarRepresentante(representante);
			
			obtenerRepresentantes();
			obtenerUltimaRepresentanteCreada();
			limpiarRegistro();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Registro correctamente el Representante.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			representante.setFechamodificacion(new Date());
			representante.setUsuariomodificador(userMB.getUsuariologueado().getNombrescompletos());
			representante.setNombrescompletos(representante.getNombres()+" "+representante.getApellidos());
			representateService.grabarRepresentante(representante);
			
			obtenerRepresentantes();
			limpiarRegistro();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Actualizo correctamente el Representante.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	public void eliminarRepresentante() {
		representateService.eliminarRepresentante(representante);
		obtenerRepresentantes();
		nrorepresentantes();
		obtenerUltimaRepresentanteCreada();
		limpiarRegistro();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Exito", "Se Eliminó correctamente el Representante.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	
	public void nrorepresentantes(){
		nrorepresentantes=representateService.nrorepresentantes();
	}
	public Representante getRepresentante() {
		return representante;
	}
	public void setRepresentante(Representante representante) {
		this.representante = representante;
	}
	public List<Representante> getListaRepresentantes() {
		return listaRepresentantes;
	}
	public void setListaRepresentantes(List<Representante> listaRepresentantes) {
		this.listaRepresentantes = listaRepresentantes;
	}
	public IRepresentanteService getRepresentateService() {
		return representateService;
	}
	public void setRepresentateService(IRepresentanteService representateService) {
		this.representateService = representateService;
	}
	public int getNrorepresentantes() {
		return nrorepresentantes;
	}
	public void setNrorepresentantes(int nrorepresentantes) {
		this.nrorepresentantes = nrorepresentantes;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public Representante getUltimoRepresentante() {
		return ultimoRepresentante;
	}
	public void setUltimoRepresentante(Representante ultimoRepresentante) {
		this.ultimoRepresentante = ultimoRepresentante;
	}
	
}
