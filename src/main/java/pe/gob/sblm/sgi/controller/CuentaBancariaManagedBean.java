package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.service.ICuentaBancariaService;
	
@ManagedBean(name = "cuentabancariaMB")
@ViewScoped
public class CuentaBancariaManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{cuentabancariaService}")
	private transient ICuentaBancariaService cuentabancariaService;
	Cuentabancaria cuentabancaria;
	Banco banco;
	List<Cuentabancaria> listaCuentabancarias;
	private int nroCuentabancarias;
	private Cuentabancaria ultimoCuentabancaria;
	List<Banco> listaBancos;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	

	@PostConstruct
	public void initObjects() {
		cuentabancaria = new Cuentabancaria();
		banco = new Banco();
		cuentabancaria.setBanco(banco);
		listaBancos= new ArrayList<Banco>();
		obtenerCuentaBancaria();
		obtenerUltimaInstitucionCreada();
		nroCuentasBancarias();
	}
	
	public void obtenerUltimaInstitucionCreada() {
		if (listaCuentabancarias.size()!=0) {
			ultimoCuentabancaria=listaCuentabancarias.get(0);
		}
		
	}
	
	public void obtenerCuentaBancaria(){
		listaCuentabancarias=cuentabancariaService.obtenerTodasCuentasBancarias();
	}

	public void limpiarRegistro() {
		cuentabancaria=new Cuentabancaria();
		cuentabancaria.setBanco(banco);
		cuentabancaria.setIdcuentabancaria(0);
	}
	
	public void grabarRepresentante(){
		if (cuentabancaria.getIdcuentabancaria()==0) {
			cuentabancaria.setFechacreacion(new Date());
			cuentabancaria.setUsuariocreador(userMB.getUsuariologueado().getNombrescompletos());
			cuentabancaria.setBanco(banco);
			cuentabancariaService.grabarCuentaBancaria(cuentabancaria);
			limpiarRegistro();
			obtenerCuentaBancaria();
			nroCuentasBancarias();
			obtenerUltimaInstitucionCreada();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Registro correctamente La cuenta bancaria.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			cuentabancaria.setFechamodificacion(new Date());
			cuentabancaria.setUsuariomodificador(userMB.getUsuariologueado().getNombrescompletos());
			cuentabancaria.setBanco(banco);
			cuentabancariaService.grabarCuentaBancaria(cuentabancaria);
			limpiarRegistro();
			obtenerCuentaBancaria();
			nroCuentasBancarias();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Actualizó correctamente La cuenta bancaria.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
	
	public List<String> autoCompleteBanco(String query){
		List<String> result = new ArrayList<String>();
		listaBancos = cuentabancariaService.listBancos();
		 
		
		for(Banco usu : (List<Banco>)listaBancos){
			
			String nomComplusu=usu.getNombre();
			
			if(nomComplusu.toLowerCase().contains(query.toLowerCase())){
				result.add(nomComplusu);
			}
		}
		
		return result;
		
	}
	
	
	
	public void nroCuentasBancarias(){
		nroCuentabancarias=cuentabancariaService.nroCuentasBancarias();
		
	}
	
	public void eliminarCuentabancaria() {
		cuentabancariaService.eliminarCuentabancaria(cuentabancaria);
		obtenerCuentaBancaria();
		nroCuentasBancarias();
		obtenerUltimaInstitucionCreada();
		limpiarRegistro();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Exito", "Se Eliminó correctamente La cuenta bancaria.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void seleccionBanco(SelectEvent event) {
		
		for (Banco b: listaBancos) {
			if (b.getNombre().equals(banco.getNombre())) {
				banco.setIdbanco(b.getIdbanco());
			}
			
		}
		
	}
	
	public void setearBanco(SelectEvent event) {
		banco=cuentabancaria.getBanco();
	}


	public Cuentabancaria getCuentabancaria() {
		return cuentabancaria;
	}

	public void setCuentabancaria(Cuentabancaria cuentabancaria) {
		this.cuentabancaria = cuentabancaria;
	}

	public List<Banco> getListaBancos() {
		return listaBancos;
	}

	public void setListaBancos(List<Banco> listaBancos) {
		this.listaBancos = listaBancos;
	}

	public List<Cuentabancaria> getListaCuentabancarias() {
		return listaCuentabancarias;
	}

	public void setListaCuentabancarias(List<Cuentabancaria> listaCuentabancarias) {
		this.listaCuentabancarias = listaCuentabancarias;
	}

	public int getNroCuentabancarias() {
		return nroCuentabancarias;
	}

	public void setNroCuentabancarias(int nroCuentabancarias) {
		this.nroCuentabancarias = nroCuentabancarias;
	}

	public Cuentabancaria getUltimoCuentabancaria() {
		return ultimoCuentabancaria;
	}

	public void setUltimoCuentabancaria(Cuentabancaria ultimoCuentabancaria) {
		this.ultimoCuentabancaria = ultimoCuentabancaria;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public ICuentaBancariaService getCuentabancariaService() {
		return cuentabancariaService;
	}

	public void setCuentabancariaService(
			ICuentaBancariaService cuentabancariaService) {
		this.cuentabancariaService = cuentabancariaService;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	

	
}
