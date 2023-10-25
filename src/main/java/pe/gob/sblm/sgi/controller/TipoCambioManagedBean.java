package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.ITipoCambioService;

@ManagedBean(name = "tipocambioMB")
@ViewScoped
public class TipoCambioManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;

	private List<Tipocambio> tipocambios;
	private Tipocambio tipoCam;
	private Double resultadoTipoCambio;
	private Usuario usuario;
	private Date fechaTipoCambio;

	boolean actualizado = false;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@PostConstruct
	public void initObjects(){

		try {
			Tipocambio tipocambio = getTipocambioService().obtenerTipoCambio();
			
			if(tipocambio==null){
				resultadoTipoCambio =0.0;
			}else{
				resultadoTipoCambio = tipocambio.getTipocambio();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  irtipocambio(){
		
		return "pgTipoCambio?faces-redirect=true";
	}
	

	public void onRowSelect(SelectEvent event) {
		actualizado = true;
		fechaTipoCambio=tipoCam.getFecha();
		FacesMessage msg = new FacesMessage("tipo cambio :" + tipoCam.getTipocambio());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void limpiarCampos() {
		tipoCam = null;
		fechaTipoCambio=null;
	}

	public void registrarTipoCambio() throws ParseException {
		
		Date ahora = new Date();
		
		
		if (actualizado == true) {
			String usermodificador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			tipoCam.setFecha(ahora);
			tipoCam.setUsrmod(usermodificador);
			
			
			tipoCam.setFecha(fechaTipoCambio);
			getTipocambioService().registrarTipoCambio(tipoCam);
			FacesMessage msg = new FacesMessage(
					"Se Actualizo correctamente el tipo de cambio.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarCampos();
		} else {
			String usercreador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			tipoCam.setUsrcre(usercreador);
			tipoCam.setFeccre(ahora);
			tipoCam.setFecha(fechaTipoCambio);
			getTipocambioService().registrarTipoCambio(tipoCam);
			FacesMessage msg = new FacesMessage("Se Registro correctamente el tipo de cambio.");
			limpiarCampos();
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		// para refrescar el valor en el panel
		Tipocambio tipocamb = getTipocambioService().obtenerTipoCambio();
		resultadoTipoCambio = tipocamb.getTipocambio();

	}

	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}

	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}

	public Tipocambio getTipoCam() {
		if (tipoCam == null) {
			tipoCam = new Tipocambio();
		}
		return tipoCam;
	}

	public void setTipoCam(Tipocambio tipoCam) {
		this.tipoCam = tipoCam;
	}

	public Double getResultadoTipoCambio() {
		return resultadoTipoCambio;
	}

	public void setResultadoTipoCambio(Double resultadoTipoCambio) {
		this.resultadoTipoCambio = resultadoTipoCambio;
	}

	public List<Tipocambio> getTipocambios() {
		tipocambios = getTipocambioService().listarTipoCambios();
		return tipocambios;
	}

	public void setTipocambios(List<Tipocambio> tipocambios) {
		this.tipocambios = tipocambios;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public Date getFechaTipoCambio() {
		return fechaTipoCambio;
	}

	public void setFechaTipoCambio(Date fechaTipoCambio) {
		this.fechaTipoCambio = fechaTipoCambio;
	}

}
