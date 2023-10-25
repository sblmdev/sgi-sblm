package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAsignarCobradorService;
import pe.gob.sblm.sgi.service.IFHvariadoService;
	
@ManagedBean(name = "asignarcobradorMB")
@ViewScoped
public class AsignarCobradorManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{asignarcobradorService}")
	private transient IAsignarCobradorService asignarcobradorService;
	
	@ManagedProperty(value = "#{FHvariadoService}")
	private transient  IFHvariadoService FHvariadoService;
	
	private Usuario usuario;
	private Usuario ultimousuario;
	private List<Usuario> listausuarios;
	private List<Usuario> listacobradores;
	private List<Usuario> filtrolistausuario;
	private int nroCobradores;
	private Boolean sicobrador=false;
	private int idusarioselecionado;
	private int codigoCobrador;
	
	@PostConstruct
	public void initObjects() {
		usuario = new Usuario();
		listausuarios= new ArrayList<Usuario>();
		listacobradores= new ArrayList<Usuario>();
		 actualizarValores();
		limpiarFormulario();
	}
	
	public void actualizarValores(){
		listausuarios=FHvariadoService.obtenerUsuarios();
		nroCobradores=numerocobradoresActuales();
		listarCobradores();
	}
	
	public  void listarCobradores(){
		listacobradores.clear();
		for (Usuario usu : listausuarios) {
			if (usu.getEscobrador()!=null) {
				if (usu.getEscobrador()==true) {
					listacobradores.add(usu);
				}
			}
			
			
		}
	}
	
	
	public void limpiarFormulario(){
		Calendar cal = GregorianCalendar.getInstance();
		
	}
	
	public void setear(){
		for (Usuario usu: listausuarios) {
			if (usu.getIdusuario()==idusarioselecionado) {
				usuario=usu;
				
			}
			
		}
		System.out.println(usuario.getIdusuario());
	}
	
	public int numerocobradoresActuales(){
		int count=0;
		
		for (Usuario usuario: listausuarios) {
		  if (usuario.getEscobrador()!=null && usuario.getEstado()!=null) {
			  if (usuario.getEstado()==true && usuario.getEscobrador()==true ) {
					count++;
				}
		  }	
			
		}
		
		return count;
	}
	
	public void asignarCobrador(){
		usuario.setCodCob(codigoCobrador);
		
		asignarcobradorService.asignarCobrador(usuario);
		actualizarValores();
		usuario=null;
		idusarioselecionado=0;
	}
	
	public void ActivarDesactivarSiCobrador(){
		
		
		if (usuario.getEscobrador()==true) {
			sicobrador=true;
			System.out.println(usuario.getEscobrador());
		} else {
			sicobrador=false;
		}
		
	}
	
	public void verificarsicobrador(){
		
		idusarioselecionado=usuario.getIdusuario();
		
		for (Usuario usr: listausuarios) {
			if (usr.getIdusuario()==usuario.getIdusuario()) {
				if (usr.getEscobrador()==null) {
					sicobrador=false;
				}else {
					sicobrador=true;
				}
			}
		}
	}
	
	public IAsignarCobradorService getAsignarcobradorService() {
		return asignarcobradorService;
	}
	public void setAsignarcobradorService(
			IAsignarCobradorService asignarcobradorService) {
		this.asignarcobradorService = asignarcobradorService;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public IFHvariadoService getFHvariadoService() {
		return FHvariadoService;
	}
	public void setFHvariadoService(IFHvariadoService fHvariadoService) {
		FHvariadoService = fHvariadoService;
	}
	public List<Usuario> getListausuarios() {
		return listausuarios;
	}
	public void setListausuarios(List<Usuario> listausuarios) {
		this.listausuarios = listausuarios;
	}
	public Usuario getUltimousuario() {
		return ultimousuario;
	}
	public void setUltimousuario(Usuario ultimousuario) {
		this.ultimousuario = ultimousuario;
	}
	public int getNroCobradores() {
		return nroCobradores;
	}
	public void setNroCobradores(int nroCobradores) {
		this.nroCobradores = nroCobradores;
	}
	public Boolean getSicobrador() {
		return sicobrador;
	}
	public void setSicobrador(Boolean sicobrador) {
		this.sicobrador = sicobrador;
	}
	public List<Usuario> getFiltrolistausuario() {
		return filtrolistausuario;
	}
	public void setFiltrolistausuario(List<Usuario> filtrolistausuario) {
		this.filtrolistausuario = filtrolistausuario;
	}

	public List<Usuario> getListacobradores() {
		return listacobradores;
	}

	public void setListacobradores(List<Usuario> listacobradores) {
		this.listacobradores = listacobradores;
	}

	public int getIdusarioselecionado() {
		return idusarioselecionado;
	}

	public void setIdusarioselecionado(int idusarioselecionado) {
		this.idusarioselecionado = idusarioselecionado;
	}

	public int getCodigoCobrador() {
		return codigoCobrador;
	}

	public void setCodigoCobrador(int codigoCobrador) {
		this.codigoCobrador = codigoCobrador;
	}
	
	
}
