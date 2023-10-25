package pe.gob.sblm.sgi.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import pe.gob.sblm.sgi.service.IAuditoriaService;
import pe.gob.sblm.sgi.service.BandejaService;
import pe.gob.sblm.sgi.service.ITipoCambioService;


@ManagedBean
public class GadgetManagedBean {
	
	@ManagedProperty(value = "#{panelNotificacionesServiceImpl}")
	private transient BandejaService panelNotificacionesServiceImpl;
	
	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;
	
	@ManagedProperty(value = "#{panelAuditoriaServiceImpl}")
	private transient IAuditoriaService panelAuditoriaServiceImpl;
	
	private int nroPendienteTotal=0;
	private int nroNotificacionesMes=0;
	private int nroRevisado=0;
	private int nroCancelado=0;
	private int nroPendiente=0;
	private Double resultadoTipoCambio=0.0;
	private String ultimoModulo="";
	private String ultimaPagina="";
	
	
	public void counterNumeroNoficaciones(){
//		nroPendiente=Integer.parseInt(panelNotificacionesServiceImpl.nroNotificacionesPendiente().toString());
//		nroCancelado=Integer.parseInt(panelNotificacionesServiceImpl.nroNotificacionesCancelado().toString());
//		nroRevisado= Integer.parseInt(panelNotificacionesServiceImpl.nroNotificacionesRevisado().toString());
//		nroNotificacionesMes=Integer.parseInt(panelNotificacionesServiceImpl.nroNotificacionesDelMes().toString());
//		nroPendienteTotal=Integer.parseInt(panelNotificacionesServiceImpl.nroNotificacionesTotal().toString());
//		resultadoTipoCambio=tipocambioService.obtenerTipoCambio().getTipocambio();
//		ultimoModulo=panelAuditoriaServiceImpl.ultimoModuloVisitado().toString();
////		ultimaPagina=panelAuditoriaServiceImpl.ultimaPaginaVisitado().toString();
	}

	public int getNroPendienteTotal() {
		return nroPendienteTotal;
	}
	public void setNroPendienteTotal(int nroPendienteTotal) {
		this.nroPendienteTotal = nroPendienteTotal;
	}
	public int getNroNotificacionesMes() {
		return nroNotificacionesMes;
	}
	public void setNroNotificacionesMes(int nroNotificacionesMes) {
		this.nroNotificacionesMes = nroNotificacionesMes;
	}
	public int getNroRevisado() {
		return nroRevisado;
	}
	public void setNroRevisado(int nroRevisado) {
		this.nroRevisado = nroRevisado;
	}
	public int getNroCancelado() {
		return nroCancelado;
	}
	public void setNroCancelado(int nroCancelado) {
		this.nroCancelado = nroCancelado;
	}
	public int getNroPendiente() {
		return nroPendiente;
	}
	public void setNroPendiente(int nroPendiente) {
		this.nroPendiente = nroPendiente;
	}

	public Double getResultadoTipoCambio() {
		return resultadoTipoCambio;
	}

	public void setResultadoTipoCambio(Double resultadoTipoCambio) {
		this.resultadoTipoCambio = resultadoTipoCambio;
	}
	
	public String getUltimoModulo() {
		return ultimoModulo;
	}

	public void setUltimoModulo(String ultimoModulo) {
		this.ultimoModulo = ultimoModulo;
	}

	public String getUltimaPagina() {
		return ultimaPagina;
	}

	public void setUltimaPagina(String ultimaPagina) {
		this.ultimaPagina = ultimaPagina;
	}

	public BandejaService getPanelNotificacionesServiceImpl() {
		return panelNotificacionesServiceImpl;
	}

	public void setPanelNotificacionesServiceImpl(
			BandejaService panelNotificacionesServiceImpl) {
		this.panelNotificacionesServiceImpl = panelNotificacionesServiceImpl;
	}

	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}

	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}

	public IAuditoriaService getPanelAuditoriaServiceImpl() {
		return panelAuditoriaServiceImpl;
	}

	public void setPanelAuditoriaServiceImpl(
			IAuditoriaService panelAuditoriaServiceImpl) {
		this.panelAuditoriaServiceImpl = panelAuditoriaServiceImpl;
	}
	
	
}
