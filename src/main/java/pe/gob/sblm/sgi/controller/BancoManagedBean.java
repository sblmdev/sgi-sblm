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

import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.service.IBancoService;
	
@ManagedBean(name = "bancoMB")
@ViewScoped
public class BancoManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{bancoService}")
	private transient IBancoService bancoService;
	Banco banco;
	List<Banco> listaBancos;
	private int nroBancos;
	private Banco ultimoBanco;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	

	@PostConstruct
	public void initObjects() {
		banco = new Banco();
		obtenerInstituciones();
		obtenerUltimaInstitucionCreada();
		nroInstituciones();
	}
	
	public void obtenerUltimaInstitucionCreada() {
		if (listaBancos.size()!=0) {
			ultimoBanco=listaBancos.get(0);
		}
		
	}
	
	public void obtenerInstituciones(){
		listaBancos=bancoService.obtenerTodosBancos();
	}

	public void limpiarRegistro() {
		banco=new Banco();
		banco.setIdbanco(0);
	}
	
	public void grabarRepresentante(){
		if (banco.getIdbanco()==0) {
			banco.setFechacreacion(new Date());
			banco.setUsuariocreador(userMB.getUsuariologueado().getNombrescompletos());
			bancoService.grabarBanco(banco);
			limpiarRegistro();
			obtenerInstituciones();
			nroInstituciones();
			obtenerUltimaInstitucionCreada();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Registro correctamente el Banco.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			banco.setFechamodificacion(new Date());
			banco.setUsuariomodificador(userMB.getUsuariologueado().getNombrescompletos());
			bancoService.grabarBanco(banco);
			limpiarRegistro();
			obtenerInstituciones();
			nroInstituciones();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Actualizo correctamente el Banco.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	public void nroInstituciones(){
		nroBancos=bancoService.nroBancos();
		
	}
	
	public void eliminarBanco() {
		bancoService.eliminarBanco(banco);
		obtenerInstituciones();
		nroInstituciones();
		obtenerUltimaInstitucionCreada();
		limpiarRegistro();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Exito", "Se Eliminó correctamente el Banco.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public IBancoService getInstitucionService() {
		return bancoService;
	}

	public void setInstitucionService(IBancoService bancoService) {
		this.bancoService = bancoService;
	}

	public Banco getInstitucion() {
		return banco;
	}

	public void setInstitucion(Banco banco) {
		this.banco = banco;
	}

	public IBancoService getBancoService() {
		return bancoService;
	}

	public void setBancoService(IBancoService bancoService) {
		this.bancoService = bancoService;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public List<Banco> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}

	public int getNroBancos() {
		return nroBancos;
	}

	public void setNroBancos(int nroBancos) {
		this.nroBancos = nroBancos;
	}

	public Banco getUltimoBanco() {
		return ultimoBanco;
	}

	public void setUltimoBanco(Banco ultimoBanco) {
		this.ultimoBanco = ultimoBanco;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	

	
}
