package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.service.InicioService;

@ManagedBean(name = "inicioMB")
@ViewScoped
public class InicioManagedBean implements Serializable {
	
	private List<String> listaMes;
	private List<Integer> listaAnio;
	private String nombreMesActual;
	private List<Integer> listaAnioDesc;
	
	@ManagedProperty(value = "#{inicioService}")
	private transient InicioService inicioService;
	
	@PostConstruct
	public void init(){
		listaMes= new ArrayList<String>();
		listaAnio=new ArrayList<Integer>();
		
		listaAnio=inicioService.getListaAnio();
		listaAnioDesc=inicioService.getListaAnio();
		listaMes=inicioService.getListaMeses();
		
		
		
	}
	

	/**
	 * @return the inicioService
	 */
	public InicioService getInicioService() {
		return inicioService;
	}

	/**
	 * @param inicioService the inicioService to set
	 */
	public void setInicioService(InicioService inicioService) {
		this.inicioService = inicioService;
	}


	/**
	 * @return the listaMes
	 */
	public List<String> getListaMes() {
		return listaMes;
	}


	/**
	 * @param listaMes the listaMes to set
	 */
	public void setListaMes(List<String> listaMes) {
		this.listaMes = listaMes;
	}


	/**
	 * @return the listaAnio
	 */
	public List<Integer> getListaAnio() {
		return listaAnio;
	}


	/**
	 * @param listaAnio the listaAnio to set
	 */
	public void setListaAnio(List<Integer> listaAnio) {
		this.listaAnio = listaAnio;
	}


	/**
	 * @return the nombreMesActual
	 */
	public String getNombreMesActual() {
		return nombreMesActual;
	}


	/**
	 * @param nombreMesActual the nombreMesActual to set
	 */
	public void setNombreMesActual(String nombreMesActual) {
		this.nombreMesActual = nombreMesActual;
	}


	public List<Integer> getListaAnioDesc() {
		return listaAnioDesc;
	}


	public void setListaAnioDesc(List<Integer> listaAnioDesc) {
		this.listaAnioDesc = listaAnioDesc;
	}
	
	
	
	
}
