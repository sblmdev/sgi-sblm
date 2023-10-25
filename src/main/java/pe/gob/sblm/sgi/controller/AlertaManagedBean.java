package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Alerta;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAlertaService;
import pe.gob.sblm.sgi.service.IUsuarioService;

@ManagedBean(name = "alertaMB")
@ViewScoped
public class AlertaManagedBean implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	@ManagedProperty(value = "#{alertaService}")
	private transient IAlertaService alertaService;

	@ManagedProperty(value = "#{usuarioService}")
	private transient IUsuarioService usuarioService;
	

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;


	private Alerta alerta;
	private Contrato contrato;
	private DualListModel<Usuario> users;

	private List<Alerta> alertas,filtroAlertas;
	@PostConstruct
	public void mipostconstructor() {
		List<Usuario> usuarioOrigen = new ArrayList<Usuario>();
		List<Usuario> usuarioDestino = new ArrayList<Usuario>();
		alertas= getAlertaService().listarAlertas();
		List<Usuario> lista = getUsuarioService().listarUsuarios();
		for (Usuario usu : lista) {
			usuarioOrigen.add(usu);
		}
		users = new DualListModel<Usuario>(usuarioOrigen, usuarioDestino);
	}

	public void registrarAlerta() {

		List<Usuario> usuarioscapturados = users.getTarget();

		for (Usuario usu : usuarioscapturados) {
			alerta.setUsuario(usu);
			alerta.setEstado(false);
			alerta.setUsrcre(userMB.getUsuariologueado().getNombrescompletos());
			alerta.setUsrmod(FuncionesHelper.getUsuario().toString());
			getAlertaService().registrarAlerta(alerta);

		}
		
		//actualizado de tabla
		alertas= getAlertaService().listarAlertas();

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Notificacion", "Se Registró correctamente la alerta!!!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/*********** tarea Programada **********/
	private static final String JOB_GROUP = "jobGroup";
	private static final String CRON_GROUP = "cronGroup";



	public static void init() {

	}



	public void capturarUsuarios(TransferEvent event) {

		for (Usuario p : users.getTarget()) {
			System.out.println("usu añadido::" + p.getNombrescompletos());
		}

	}

	public DualListModel<Usuario> getUsers() {
		return users;
	}

	public void setUsers(DualListModel<Usuario> users) {
		this.users = users;
	}

	public IAlertaService getAlertaService() {
		return alertaService;
	}

	public void setAlertaService(IAlertaService alertaService) {
		this.alertaService = alertaService;
	}

	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Alerta getAlerta() {
		if (alerta == null) {
			alerta = new Alerta();
		}
		return alerta;
	}

	public void setAlerta(Alerta alerta) {
		this.alerta = alerta;
	}

	public Contrato getContrato() {
		if (contrato == null) {
			contrato = new Contrato();
		}
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<Alerta> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<Alerta> alertas) {
		this.alertas = alertas;
	}

	public List<Alerta> getFiltroAlertas() {
		return filtroAlertas;
	}

	public void setFiltroAlertas(List<Alerta> filtroAlertas) {
		this.filtroAlertas = filtroAlertas;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	
	
}
