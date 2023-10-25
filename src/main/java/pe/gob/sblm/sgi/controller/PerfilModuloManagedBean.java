package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.entity.Permiso;
import pe.gob.sblm.sgi.service.IModuloService;
import pe.gob.sblm.sgi.service.IPerfilModuloService;
import pe.gob.sblm.sgi.service.IPerfilService;
import pe.gob.sblm.sgi.service.IPermisoService;
import pe.gob.sblm.sgi.util.PerfilModuloPermiso;

@ManagedBean(name = "perfilmoduloMB")
@ViewScoped
public class PerfilModuloManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{perfilmoduloService}")
	private transient IPerfilModuloService perfilmoduloService;

	@ManagedProperty(value = "#{moduloService}")
	private transient IModuloService moduloService;

	@ManagedProperty(value = "#{perfilService}")
	private transient IPerfilService perfilService;

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	private Perfil perfil;
	private List<Perfil> perfiles;

	private Perfilmodulo perfilmodulo;
	private List<Perfilmodulo> perfilmodulos;
	// lsita para capturar valores de la tabla
	private Modulo modulo;
	private List<Modulo> modulos;

	private String valor;

	private boolean activoperfil = false;// para mostrar modulos a actualizar

	private String ultimoPerfil;
	private Date fechaultimoPerfil;
	private int numPerfiles;
	// //////***//////////
	private int idenmodulo;
	private boolean idenestado = true;
	private int idenpermiso;
	private boolean estadoagregar = false;
	private List<PerfilModuloPermiso> listaperfmodperm;
	@ManagedProperty(value = "#{permisoService}")
	private transient IPermisoService permisoService;
	private boolean actualizado = false;
	private boolean mostrartabla = false;
	private boolean mostrarboton = false;

	private PerfilModuloPermiso perfilmoduPermiso;
	private Perfil perfilmoduloeliminar;
	private PerfilModuloPermiso moduloquitado;

	public void actualizarRegistroSeleccionado() {// actualiza el registro
													// existente
		mostrarboton = false;

		List<PerfilModuloPermiso> nuevalistaperfmodperm = new ArrayList<PerfilModuloPermiso>();
		for (PerfilModuloPermiso pmp : listaperfmodperm) {
			if (pmp.getIdmodulo() == perfilmoduPermiso.getIdmodulo()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Advertencia", "registro actualizado...");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				PerfilModuloPermiso perfmodperm2 = new PerfilModuloPermiso();

				perfmodperm2.setEstado(idenestado);
				perfmodperm2.setIdmodulo(idenmodulo);
				perfmodperm2.setIdpermiso(idenpermiso);
				Modulo mod = getModuloService().listarModuloPorId(idenmodulo);
				perfmodperm2.setNombremodulo(mod.getNombremodulo());
				Permiso permi = getPermisoService().listarPermisoPorId(
						idenpermiso);
				perfmodperm2.setNombrepermiso(permi.getValcrud());
				nuevalistaperfmodperm.add(perfmodperm2);

			} else {

				nuevalistaperfmodperm.add(pmp);
			}
		}

		listaperfmodperm = nuevalistaperfmodperm;
		// reseteado de valores del combo
		idenmodulo = 0;
		idenestado = false;
		idenpermiso = 0;
	}

	public void agregarRegistro() {

		boolean esrepetido = false;
		PerfilModuloPermiso perfmodperm = new PerfilModuloPermiso();
		if (estadoagregar == false) {// si es el primer registro
			listaperfmodperm = new ArrayList<PerfilModuloPermiso>();

			perfmodperm.setIdmodulo(idenmodulo);
			perfmodperm.setEstado(idenestado);
			perfmodperm.setIdpermiso(idenpermiso);
			Modulo mod = getModuloService().listarModuloPorId(idenmodulo);
			perfmodperm.setNombremodulo(mod.getNombremodulo());
			Permiso permi = getPermisoService().listarPermisoPorId(idenpermiso);
			perfmodperm.setNombrepermiso(permi.getValcrud());

			listaperfmodperm.add(perfmodperm);

		} else {// si hay mas de 1 registro
			for (PerfilModuloPermiso pfmpr : listaperfmodperm) {
				if (pfmpr.getIdmodulo() == idenmodulo) {// verif. si exis.
														// regis.
					esrepetido = true;
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Advertencia",
							"Ya existe modulo seleccionado...");

					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}

			if (!esrepetido) {

				// perfmodperm = new PerfilModuloPermiso();
				perfmodperm.setIdmodulo(idenmodulo);
				perfmodperm.setEstado(idenestado);
				perfmodperm.setIdpermiso(idenpermiso);
				Modulo mod = getModuloService().listarModuloPorId(idenmodulo);
				perfmodperm.setNombremodulo(mod.getNombremodulo());
				Permiso permi = getPermisoService().listarPermisoPorId(
						idenpermiso);
				perfmodperm.setNombrepermiso(permi.getValcrud());

				listaperfmodperm.add(perfmodperm);

			}
		}

		estadoagregar = true;
		// reseteado de valores del combo
		idenmodulo = 0;
		idenestado = false;
		idenpermiso = 0;
	}

	@PostConstruct
	public void initObjects() {

		try {
			// para mostrar en el cuadro informacion
			numPerfiles = getPerfilService().obtenerNumeroPerfiles();
			ultimoPerfil = getPerfilService().obtenerUltimoPerfil();
			fechaultimoPerfil = getPerfilService().obtenerFechaUltimoPerfil();

			inicializar() ;
			// isMostrartabla();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void inicializar() {
		perfilmodulos = getPerfilmoduloService().listarPerfilmodulos();
		modulos = getModuloService().listarModulos();
		perfiles = getPerfilService().listarPerfiles();
	}

	public void registrarPerfilModulo2() {

		if (!actualizado) {// REGISTRAR
			registrarPerfil();
			registrarPerfiModulos();
			// para mostrar en el cuadro informacion
			numPerfiles = getPerfilService().obtenerNumeroPerfiles();
			ultimoPerfil = getPerfilService().obtenerUltimoPerfil();
			fechaultimoPerfil = getPerfilService().obtenerFechaUltimoPerfil();
			inicializar();
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Registro el perfil  "
							+ perfil.getNombreperfil() + " correctamente.");

			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {// ACTUALIZAR
			perfilmoduloService.eliminarPerfilModuloId(perfil.getIdperfil());
			registrarPerfil();
			actualizarPerfiModulos(perfil.getIdperfil());
			// para mostrar en el cuadro informacion
			numPerfiles = getPerfilService().obtenerNumeroPerfiles();
			ultimoPerfil = getPerfilService().obtenerUltimoPerfil();
			fechaultimoPerfil = getPerfilService().obtenerFechaUltimoPerfil();
			inicializar();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exito", "Se Actualizo el perfil "
							+ perfil.getNombreperfil() + "  correctamente.");

			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

	}

	public void actualizarPerfiModulos(int idperfil) {
		Perfil perf = new Perfil();
		perf.setIdperfil(idperfil);
		for (PerfilModuloPermiso pmodp : listaperfmodperm) {

			Modulo mod = new Modulo();
			mod.setIdmodulo(pmodp.getIdmodulo());

			Permiso perm = new Permiso();
			perm.setIdpermiso(pmodp.getIdpermiso());
			Perfilmodulo pf = new Perfilmodulo();
			pf.setModulo(mod);
			pf.setPerfil(perf);
			pf.setPermiso(perm);
			pf.setEstado(pmodp.isEstado());
			String userCreador = userMB.getUsuariologueado().getNombres() + " "
					+ userMB.getUsuariologueado().getApellidopat();
			pf.setUsrcre(userCreador);
			Date fechacre = new Date();
			pf.setFeccre(fechacre);
			getPerfilmoduloService().registrarPerfilModulo(pf);

		}
	}

	public void eliminarPerfiModulo() {

		getPerfilService().eliminarPerfil(perfilmoduloeliminar);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Exito", "Se Eliminó el perfil "
						+ perfilmoduloeliminar.getNombreperfil()
						+ " correctamente.");

		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void quitarModulo() {
		actualizado = true;
		System.out.println("aaaa:::" + moduloquitado.getNombremodulo());

	//List<PerfilModuloPermiso> lista;
		for (Iterator<PerfilModuloPermiso> iter = listaperfmodperm.iterator(); iter
				.hasNext();) {
			PerfilModuloPermiso algo = iter.next();
			// hacer algo con algo
			if (algo.equals(moduloquitado)) {
				iter.remove(); // Esto quita el elemento actual de la lista, SIN
								// causar problemas
			}
		}
		
		for (PerfilModuloPermiso pm : listaperfmodperm) {
			System.out.println(":moduloxxx::"+pm.getNombremodulo());
		}

	}

	public void registrarPerfiModulos() {

		Perfil perf = new Perfil();
		perf.setIdperfil(getPerfilService().obtenerUltimoIdPerfil());
		if (listaperfmodperm == null) {
			System.out.println("se registro perfil sin modulos......");
		} else {
			for (PerfilModuloPermiso pmodp : listaperfmodperm) {

				Modulo mod = new Modulo();
				mod.setIdmodulo(pmodp.getIdmodulo());

				Permiso perm = new Permiso();
				perm.setIdpermiso(pmodp.getIdpermiso());
				Perfilmodulo pf = new Perfilmodulo();
				pf.setModulo(mod);
				pf.setPerfil(perf);
				pf.setPermiso(perm);
				pf.setEstado(pmodp.isEstado());
				String userCreador = userMB.getUsuariologueado().getNombres()
						+ " " + userMB.getUsuariologueado().getApellidopat();
				pf.setUsrcre(userCreador);
				Date fechacre = new Date();
				pf.setFeccre(fechacre);
				getPerfilmoduloService().registrarPerfilModulo(pf);

			}
		}
	}

	public void registrarPerfil() {
		if (!actualizado) {// registro
			String usuarioCreador;

			Date fechacre = new Date();
			usuarioCreador = userMB.getUsuariologueado().getNombres()
					+ userMB.getUsuariologueado().getApellidopat();
			perfil.setUsrcre(usuarioCreador);
			perfil.setFeccre(fechacre);
			getPerfilService().registrarPerfil(perfil);

		} else {// modificado
			String modificador;

			Date fechamod = new Date();
			modificador = userMB.getUsuariologueado().getNombres() + " "
					+ userMB.getUsuariologueado().getApellidopat();
			perfil.setUsrmod(modificador);
			perfil.setFecmod(fechamod);
			getPerfilService().registrarPerfil(perfil);
		}

	}

	public void activarActualizado() {
		
		mostrarboton = true;
		idenmodulo = perfilmoduPermiso.getIdmodulo();
		idenestado = perfilmoduPermiso.isEstado();
		idenpermiso = perfilmoduPermiso.getIdpermiso();

	}

	public void onRowSelect(SelectEvent event) {

		mostrartabla = true;
		estadoagregar = true;
		actualizado = true;

		List<Perfilmodulo> listapermod = new ArrayList<Perfilmodulo>();
		listapermod = getPerfilmoduloService().listarPerfilModuloPorIdPerfil(
				perfil.getIdperfil());

		List<PerfilModuloPermiso> milista = new ArrayList<PerfilModuloPermiso>();

		for (Perfilmodulo perfilmodulo : listapermod) {

			int idpermiso = perfilmodulo.getPermiso().getIdpermiso();
			int idmodulo = perfilmodulo.getModulo().getIdmodulo();
			boolean estadocap = perfilmodulo.getEstado();

			PerfilModuloPermiso pmp = new PerfilModuloPermiso();

			pmp.setIdpermiso(idpermiso);
			pmp.setIdmodulo(idmodulo);
			pmp.setEstado(estadocap);
			pmp.setNombremodulo(perfilmodulo.getModulo().getNombremodulo());
			pmp.setNombrepermiso(perfilmodulo.getPermiso().getValcrud());

			milista.add(pmp);

		}
		// listapmp = milista;
		listaperfmodperm = milista;

	}

	// //////////////////

	public void limpiarCampos() {
		activoperfil = false;
		perfilmodulo = null;
		modulo = null;
		perfil = null;

	}

	public IPerfilModuloService getPerfilmoduloService() {
		return perfilmoduloService;
	}

	public void setPerfilmoduloService(IPerfilModuloService perfilmoduloService) {
		this.perfilmoduloService = perfilmoduloService;
	}

	public Perfilmodulo getPerfilmodulo() {
		if (perfilmodulo == null) {
			perfilmodulo = new Perfilmodulo();
		}
		return perfilmodulo;
	}

	public void setPerfilmodulo(Perfilmodulo perfilmodulo) {
		this.perfilmodulo = perfilmodulo;
	}

	public List<Perfilmodulo> getPerfilmodulos() {

//		perfilmodulos = getPerfilmoduloService().listarPerfilmodulos();
		
		return perfilmodulos;
	}

	public void setPerfilmodulos(List<Perfilmodulo> perfilmodulos) {
		this.perfilmodulos = perfilmodulos;
	}

	public Modulo getModulo() {
		if (modulo == null) {
			modulo = new Modulo();
		}
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public List<Modulo> getModulos() {
//		modulos = getModuloService().listarModulos();
		
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public IModuloService getModuloService() {
		return moduloService;
	}

	public void setModuloService(IModuloService moduloService) {
		this.moduloService = moduloService;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public IPerfilService getPerfilService() {
		return perfilService;
	}

	public void setPerfilService(IPerfilService perfilService) {
		this.perfilService = perfilService;
	}

	public Perfil getPerfil() {
		if (perfil == null) {
			perfil = new Perfil();
			perfil.setEstado(true);
		}
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getPerfiles() {
//		perfiles = getPerfilService().listarPerfiles();
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {

		this.perfiles = perfiles;
	}

	public boolean isActualizado() {
		return actualizado;
	}

	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}

	public boolean isActivoperfil() {
		return activoperfil;
	}

	public void setActivoperfil(boolean activoperfil) {
		this.activoperfil = activoperfil;
	}

	public String getUltimoPerfil() {
		return ultimoPerfil;
	}

	public void setUltimoPerfil(String ultimoPerfil) {
		this.ultimoPerfil = ultimoPerfil;
	}

	public Date getFechaultimoPerfil() {
		return fechaultimoPerfil;
	}

	public void setFechaultimoPerfil(Date fechaultimoPerfil) {
		this.fechaultimoPerfil = fechaultimoPerfil;
	}

	public int getNumPerfiles() {
		return numPerfiles;
	}

	public void setNumPerfiles(int numPerfiles) {
		this.numPerfiles = numPerfiles;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public int getIdenmodulo() {
		return idenmodulo;
	}

	public void setIdenmodulo(int idenmodulo) {
		this.idenmodulo = idenmodulo;
	}

	public int getIdenpermiso() {
		return idenpermiso;
	}

	public void setIdenpermiso(int idenpermiso) {
		this.idenpermiso = idenpermiso;
	}

	public boolean isEstadoagregar() {
		return estadoagregar;
	}

	public void setEstadoagregar(boolean estadoagregar) {
		this.estadoagregar = estadoagregar;
	}

	public List<PerfilModuloPermiso> getListaperfmodperm() {
		return listaperfmodperm;
	}

	public void setListaperfmodperm(List<PerfilModuloPermiso> listaperfmodperm) {
		this.listaperfmodperm = listaperfmodperm;
	}

	public boolean isIdenestado() {
		return idenestado;
	}

	public void setIdenestado(boolean idenestado) {
		this.idenestado = idenestado;
	}

	public IPermisoService getPermisoService() {
		return permisoService;
	}

	public void setPermisoService(IPermisoService permisoService) {
		this.permisoService = permisoService;
	}

	public boolean isMostrartabla() {
		return mostrartabla;
	}

	public void setMostrartabla(boolean mostrartabla) {
		this.mostrartabla = mostrartabla;
	}

	public PerfilModuloPermiso getPerfilmoduPermiso() {
		return perfilmoduPermiso;
	}

	public void setPerfilmoduPermiso(PerfilModuloPermiso perfilmoduPermiso) {
		this.perfilmoduPermiso = perfilmoduPermiso;
	}

	public boolean isMostrarboton() {
		return mostrarboton;
	}

	public void setMostrarboton(boolean mostrarboton) {
		this.mostrarboton = mostrarboton;
	}

	public Perfil getPerfilmoduloeliminar() {
		return perfilmoduloeliminar;
	}

	public void setPerfilmoduloeliminar(Perfil perfilmoduloeliminar) {
		this.perfilmoduloeliminar = perfilmoduloeliminar;
	}

	public PerfilModuloPermiso getModuloquitado() {
		return moduloquitado;
	}

	public void setModuloquitado(PerfilModuloPermiso moduloquitado) {
		this.moduloquitado = moduloquitado;
	}

}
