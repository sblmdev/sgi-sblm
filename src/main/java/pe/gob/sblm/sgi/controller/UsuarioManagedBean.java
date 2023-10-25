package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Alerta;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.BandejaService;
import pe.gob.sblm.sgi.service.IAlertaService;
import pe.gob.sblm.sgi.service.IModuloService;
import pe.gob.sblm.sgi.service.IPaginaService;
import pe.gob.sblm.sgi.service.IPerfilModuloService;
import pe.gob.sblm.sgi.service.IPerfilService;
import pe.gob.sblm.sgi.service.IUsuarioService;

@ManagedBean(name = "usuarioMB")
@SessionScoped
public class UsuarioManagedBean implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;

	@ManagedProperty(value = "#{usuarioService}")
	private transient IUsuarioService usuarioService;

	private Usuario usuario;
	private List<Usuario> usuarios;
	List<Paginamodulo> page_module_ALL; 
	private String nombrecompleto;

	private String nombreusr="admin";
	private String contrasenausr="4dm1n1str4t0r";
	private boolean loggedIn;


	private boolean modadministracion;
	private boolean modmantenimiento;
	private boolean modmodulos;
	private String horaconexion;
	public  String nombreperfilSeleccionado;

	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	HttpSession varSesion = request.getSession();
	HttpSession varSesionperfil = request.getSession();

	private int idperfil;
	// para obtener los atributos del user en las paginas
	private Usuario usuariologueado;
	// para menu dinamicos
	@ManagedProperty(value = "#{moduloService}")
	private transient IModuloService moduloService;
	private MenuModel model;

	@ManagedProperty(value = "#{paginaService}")
	private transient IPaginaService paginaService;

	@ManagedProperty(value = "#{perfilService}")
	private transient IPerfilService perfilService;
	private Perfil perfil;

	@ManagedProperty(value = "#{perfilmoduloService}")
	private transient IPerfilModuloService perfilmoduloService;
	
	@ManagedProperty(value = "#{alertaService}")
	private transient IAlertaService alertaService;
	

	@ManagedProperty(value = "#{panelNotificacionesServiceImpl}")
	private transient BandejaService notificacionesService;
	
	private List<Usuario> usuariosdgi;
	
	private Boolean flag=false;
	List<Alerta> listaalerta= new ArrayList<Alerta>();
	
	@PostConstruct
	public void initObjects() {
		inicializarDatos();
	}
	
	public void inicializarDatos(){
		//listaalerta=alertaService.alertasNoenviadas();
	}
	//prueba 
	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;
	Transaction txt = null;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
  //fin prueba
	public void actualizarDatosUsuario(){
		
		usuariologueado = getUsuarioService().buscarUsuarioxId(Integer.parseInt(FuncionesHelper.getUsuario().toString()));
		nombrecompleto =usuariologueado.getNombres()+" "+usuariologueado.getApellidopat();
		
	}

	public void cerrarSesion() {
		FacesContext context = FacesContext.getCurrentInstance();

		ExternalContext externalContext = context.getExternalContext();

		Object session = externalContext.getSession(false);

		HttpSession httpSession = (HttpSession) session;

		httpSession.invalidate();
	}


	public void loguear() {
		RequestContext context = RequestContext.getCurrentInstance();
		
		loggedIn = false;
        //Sunat_Comprobante_Detalle scompd= new Sunat_Comprobante_Detalle();
		Usuario usuario = new Usuario();
		usuario.setNombreusr(nombreusr);
		usuario.setContrasenausr(contrasenausr);
		usuario = getUsuarioService().buscarUsuario(usuario);
		//scompd.setId_compr_det(68602);
		//System.out.println("SUnaT_DATOSm: iDcOmp_DET="+scompd.getId_compr_det());
		//scompd= getUsuarioService().buscarComprobante(scompd);
		//System.out.println("SUnaT_DATOS: iDcOmp_DET="+scompd.getId_compr_det()+" NROITEM= "+scompd.getNroitem());
		usuariologueado = usuario;
		
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		formateador.format(ahora);
		horaconexion = formateador.format(ahora);
		if (usuario==null) {
			System.out.println("paso metodo login INCORRECTO");
			loggedIn = false;

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error", "Usuario o Contraseña incorrecto!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			if (usuario.getContrasenausr().equals("INACTIVO**")
					&& usuario.getEstado().equals(false)
					|| usuario.getEstado().equals(false)) {
				System.out
						.println("usuario.getEstado()=" + usuario.getEstado());
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error",
						"Usuario se encuentra de baja comunicarse con el administrador del sistema!!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				if (usuario.getContrasenausr().equals(contrasenausr)) {
					nombrecompleto = usuariologueado.getNombres() + " "
							+ usuariologueado.getApellidopat();
					FuncionesHelper.nombrecompletoUsuario = nombrecompleto;

					loggedIn = true;
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Mensaje",
							"Login correcto");
					FacesContext.getCurrentInstance().addMessage(null, msg);

					varSesion.setAttribute("usuario", usuario.getIdusuario());
					Object sesionUserName = (Object) varSesion
							.getAttribute("usuario");
					

					try {
						idperfil = getUsuarioService().obtenerIdPerfil(usuario);
						varSesionperfil.setAttribute("perfil", idperfil);
						Object sesionPerfilName = (Object) varSesionperfil
								.getAttribute("perfil");	
						perfil = getPerfilService().listarPerfilPorId(idperfil);
						nombreperfilSeleccionado = perfil.getNombreperfil();
						obtenerMenu();

					} catch (Exception e) {
						System.out.println("mi  error controller:"
								+ e.getMessage());
					}

					// ejecutarTareaProgramada();
				} else {
					if(usuario.getContrasenausr().equalsIgnoreCase(contrasenausr)){
						loggedIn = false;

						FacesMessage msg = new FacesMessage(
								FacesMessage.SEVERITY_ERROR, "Error",
								"Contraseña es en minuscula!!!");
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					else
					{loggedIn = false;

					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Error",
							"Contraseña incorrecta!!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);}
				}

			}
		}

		
		context.addCallbackParam("loggedIn", loggedIn);

		// ***********para manejar los estado del modulo*******//
		// ///////visualizacion de modulos dinamicos////////
		


	}
	
	
	public void ejecutarTareaProgramada(){
		
		if (listaalerta.size()!=0) {
			for (Alerta alerta : listaalerta) {
				if (alerta.getEnvionotificacion().equals(true)) {
					alertaService.enviarNotificacion(alerta.getIdalerta(),alerta.getMensaje(),alerta.getUsuario().getIdusuario(),Integer.parseInt(alerta.getUsrmod()));
				}
				alertaService.actualizarAenviado(alerta.getIdalerta());};
		}	

		}
	
	public void obtenerPerfil(){
		
		Usuario U= new Usuario();
		U.setIdusuario((Integer)FuncionesHelper.getUsuario());
		
		idperfil = getUsuarioService().obtenerIdPerfil(U);
		perfil = getPerfilService().listarPerfilPorId(idperfil);
		
//		idperfil = getUsuarioService().obtenerIdPerfil(usuario);
		varSesionperfil.setAttribute("perfil", idperfil);
		Object sesionPerfilName = (Object) varSesionperfil
				.getAttribute("perfil");
	}
	
	public List<Pagina> paginasModulos(int idmodulo){
		List<Pagina> tmp= new ArrayList<Pagina>();
		for (Paginamodulo pm : page_module_ALL) {
			if (pm.getModulo().getIdmodulo()==idmodulo) {
				tmp.add(pm.getPagina());
			}
		}
		
		return tmp;
	}

	public void obtenerMenu() {
//		page_module_ALL= new ArrayList<Paginamodulo>(getPaginaService().listarPaginasModulos());//verificar
//		List<Pagina> lstpagina= new ArrayList<Pagina>();
//		try {
//			List<Perfilmodulo> lstperfilmodulo = new ArrayList<Perfilmodulo>();
//			lstperfilmodulo=getPerfilmoduloService().listarPerfilModuloPorIdPerfil(getUsuarioService().obtenerIdPerfil(usuariologueado));
//			model = new DefaultMenuModel();
//			DefaultSubMenu subMenu = new DefaultSubMenu("some submenu");
//			DefaultMenuItem menuItem = new DefaultMenuItem("some item");
//			subMenu.addElement(menuItem);
//
//			model.addElement(subMenu);
//
//
//		} catch (NullPointerException e) {
//			System.out.println("error obtener menu: " + e.getMessage());
//		}
		
		page_module_ALL= new ArrayList<Paginamodulo>(getPaginaService().listarPaginasModulos());//verificar
		List<Pagina> lstpagina= new ArrayList<Pagina>();
		try {
			List<Perfilmodulo> lstperfilmodulo = new ArrayList<Perfilmodulo>();
			lstperfilmodulo=getPerfilmoduloService().listarPerfilModuloPorIdPerfil(getUsuarioService().obtenerIdPerfil(usuariologueado));
			model = new DefaultMenuModel();
			for (Perfilmodulo modu : lstperfilmodulo) {
				DefaultSubMenu submenup = new DefaultSubMenu();
				submenup.setLabel(modu.getModulo().getNombremodulo()); // menus
				submenup.setRendered(modu.getEstado());
				lstpagina = paginasModulos(modu.getModulo().getIdmodulo());
				for (Pagina pag : lstpagina) {
					if (usuarioService.siAutorizado(idperfil, pag.getIdpagina())) {
						DefaultMenuItem item = new DefaultMenuItem(); // items
						item.setValue(pag.getDescripcionpagina());
						item.setUrl(pag.getNombrepagina());
						submenup.addElement(item);
					}
				} 
				model.addElement(submenup);
			}

		} catch (NullPointerException e) {
			System.out.println("error obtener menu: " + e.getMessage());
		}
	}

	public void recuperarContrasenha() {
		
		FacesContext contextFaces = FacesContext.getCurrentInstance(); 

		Usuario usr= getUsuarioService().obtenerUsuario(
				usuario.getNombreusr());
		
		if (usr == null) {
			
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Mensaje","Nombre de usuario incorrecto"));
			
		} else {
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Mensaje","Se envio su contraseña a su correo electronico"));
			
		}

	}
	
	public void  limpiarNombreUsuario(){
		FacesContext contextFaces = FacesContext.getCurrentInstance(); 
		if(usuario!=null){
			usuario.setNombreusr("");
		}else{
			contextFaces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Mensaje","Nombre de usuario no encontrado"));
		}
		
	}

	public String navega() {
		System.out.println("error sa");
		return "/pages/login.jsf";

	}

	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		usuarios = getUsuarioService().listarUsuarios();
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getNombreusr() {
		return nombreusr;
	}

	public void setNombreusr(String nombreusr) {
		this.nombreusr = nombreusr;
	}

	public String getContrasenausr() {
		return contrasenausr;
	}

	public void setContrasenausr(String contrasenausr) {
		this.contrasenausr = contrasenausr;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	// public Perfilmodulo getPerfilmod() {
	// return perfilmod;
	// }
	//
	// public void setPerfilmod(Perfilmodulo perfilmod) {
	// this.perfilmod = perfilmod;
	// }

	public boolean isModadministracion() {
		return modadministracion;
	}

	public void setModadministracion(boolean modadministracion) {
		this.modadministracion = modadministracion;
	}

	public boolean isModmantenimiento() {
		return modmantenimiento;
	}

	public void setModmantenimiento(boolean modmantenimiento) {
		this.modmantenimiento = modmantenimiento;
	}

	public boolean isModmodulos() {
		return modmodulos;
	}

	public void setModmodulos(boolean modmodulos) {
		this.modmodulos = modmodulos;
	}

	public Usuario getUsuariologueado() {
		return usuariologueado;
	}

	public void setUsuariologueado(Usuario usuariologueado) {
		this.usuariologueado = usuariologueado;
	}

	public IModuloService getModuloService() {
		return moduloService;
	}

	public void setModuloService(IModuloService moduloService) {
		this.moduloService = moduloService;
	}

	public IPaginaService getPaginaService() {
		return paginaService;
	}

	public void setPaginaService(IPaginaService paginaService) {
		this.paginaService = paginaService;
	}



	public int getIdperfil() {
		return idperfil;
	}

	public void setIdperfil(int idperfil) {
		this.idperfil = idperfil;
	}

	public IPerfilService getPerfilService() {
		return perfilService;
	}

	public void setPerfilService(IPerfilService perfilService) {
		this.perfilService = perfilService;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getHoraconexion() {
		return horaconexion;
	}

	public void setHoraconexion(String horaconexion) {
		this.horaconexion = horaconexion;
	}

	public IPerfilModuloService getPerfilmoduloService() {
		return perfilmoduloService;
	}

	public void setPerfilmoduloService(IPerfilModuloService perfilmoduloService) {
		this.perfilmoduloService = perfilmoduloService;
	}

	public List<Usuario> getUsuariosdgi() {
		System.out.println("###############llamdo #################");
		usuariosdgi = getUsuarioService().listarUsuarios(usuariologueado.getIdusuario());
		return usuariosdgi;
	}

	public void setUsuariosdgi(List<Usuario> usuariosdgi) {
		this.usuariosdgi = usuariosdgi;
	}

	public String getNombrecompleto() {
		return nombrecompleto;
	}

	public void setNombrecompleto(String nombrecompleto) {
		this.nombrecompleto = nombrecompleto;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public IAlertaService getAlertaService() {
		return alertaService;
	}

	public void setAlertaService(IAlertaService alertaService) {
		this.alertaService = alertaService;
	}

	public BandejaService getNotificacionesService() {
		return notificacionesService;
	}

	public void setNotificacionesService(
			BandejaService notificacionesService) {
		this.notificacionesService = notificacionesService;
	}

	/**
	 * @return the model
	 */
	public MenuModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(MenuModel model) {
		this.model = model;
	}
	
	
}
