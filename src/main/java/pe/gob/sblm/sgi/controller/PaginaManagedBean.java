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

import org.primefaces.event.SelectEvent;
////import org.primefaces.model.DefaultMenuModel;
//import org.primefaces.model.MenuModel;

import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.service.IModuloService;
import pe.gob.sblm.sgi.service.IPaginaService;

@ManagedBean(name = "paginaMB")
@ViewScoped
public class PaginaManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{paginaService}")
	private transient IPaginaService paginaService;

	@ManagedProperty(value = "#{moduloService}")
	private transient IModuloService moduloService;
//	private MenuModel model;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;

	@ManagedProperty(value = "#{moduloMB}")
	ModuloManagedBean moduloMB;

	private Pagina pagina;
	private List<Pagina> paginas;
	private String ultimoPagina;
	private int numPaginas;
	private Date fechaultimaPagina;

	private Paginamodulo paginamodulo;
	private List<Paginamodulo> paginamodulos;
	private boolean actualizado;

	public PaginaManagedBean() {

	}

	@PostConstruct
	public void initObjects() {
		inicializar();
		try {

			ultimoPagina = getPaginaService().obtenerUltimaPagina();
			fechaultimaPagina = getPaginaService().obtenerFechaUltimaPagina();
			numPaginas = getPaginaService().obtenerNumeroPaginas();

//			model = new DefaultMenuModel();
		
			// List <Modulo> lstmodulo= getModuloService().listarModulos();
			//
			// for (Modulo modu : lstmodulo) {
			// Submenu submenup = new Submenu();
			// submenup.setLabel(modu.getNombremodulo()); //menus
			// submenup.setRendered(new Boolean(modu.getEstado() ) );
			// Column colum =new Column();
			// Submenu submenu = new Submenu();
			// System.out.println("modulo:::"+modu.getNombremodulo());
			// List <Pagina> lstpagina=
			// getPaginaService().listarPaginasModulos(modu.getIdmodulo());
			// for (Pagina pag : lstpagina) {
			// MenuItem item = new MenuItem(); //items
			// item.setValue(pag.getNombrepagina());
			// item.setUrl(pag.getNombrepagina()+".jsf");
			// submenu.getChildren().add(item);
			// System.out.println("pagina:::"+pag.getNombrepagina());
			// }
			// colum.getChildren().add(submenu);
			// submenup.getChildren().add(colum);
			// model.addSubmenu(submenup);
			// }

			// First submenu
			// Submenu submenup = new Submenu();
			// submenup.setLabel("Administracion");
			//
			// Column colum =new Column();
			// Submenu submenu = new Submenu();
			//
			// MenuItem item = new MenuItem(); //items
			// item.setValue("auditoria");
			// item.setUrl("#");
			// submenu.getChildren().add(item);
			//
			//
			// colum.getChildren().add(submenu);
			// submenup.getChildren().add(colum);
			//
			// model.addSubmenu(submenup);
			//
			// //Second submenu
			// submenu = new Submenu();
			// submenu.setLabel("Mantenimiento");
			//
			// item = new MenuItem();
			// item.setValue("TipoCmabio");
			// item.setUrl("#");
			// submenu.getChildren().add(item);
			//
			// item = new MenuItem();
			// item.setValue("TipoCmabio 2");
			// item.setUrl("#");
			// submenu.getChildren().add(item);
			//
			// model.addSubmenu(submenu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public MenuModel getModel() {
//		return model;
//	}

	public void inicializar(){
		paginas = getPaginaService().listarPaginas();
		paginamodulos = getPaginaService().listarPaginamodulos();
	}
	public void onRowSelect(SelectEvent event) {
		actualizado = true;
		// FacesMessage msg = new FacesMessage("Id :" + pagina.getIdpagina());

		// FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void registrarPagina() {

		if (!actualizado) {
			// REGISTRO DE PAGINA
			Pagina pag = new Pagina();
			pag = getPaginaService().verificarPaginaEnModulo(
					paginamodulo.getPagina().getDescripcionpagina(),paginamodulo.getPagina().getNombrepagina(),
					paginamodulo.getModulo().getIdmodulo(),paginamodulo.getPagina().getIdpagina());
			if (pag==null) {
				
				String usercreador = userMB.getUsuariologueado().getNombres()
						+ " " + userMB.getUsuariologueado().getApellidopat();
				Pagina pagi = new Pagina();
				pagi.setDescripcionpagina(paginamodulo.getPagina()
						.getDescripcionpagina());
				pagi.setEstado(paginamodulo.getPagina().getEstado());
				pagi.setNombrepagina(paginamodulo.getPagina().getNombrepagina());
				pagi.setUsrcre(usercreador);
				Date fechacre = new Date();
				pagi.setFeccre(fechacre);
				getPaginaService().registrarPagina(pagi);

				// REGISTRO DE PAGINAMODULO
				pagi.setIdpagina(getPaginaService().obtenerUltimaPaginaCreada()
						.getIdpagina());
				paginamodulo.setValortabla("pagina");
				paginamodulo.setPagina(pagi);
				getPaginaService().registrarPaginamodulo(paginamodulo);
				limpiarCampos();
				inicializar();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Exito", "Se creó la  página "
								+ pagi.getDescripcionpagina()
								+ " Correctamente.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error",
						"No se puede actualizar, ya existe una pagina con el mismo nombre");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} else {// ACTUALIZADO
			
			Pagina pag = new Pagina();
			pag = getPaginaService().verificarPaginaEnModulo(paginamodulo.getPagina().getDescripcionpagina(),paginamodulo.getPagina().getNombrepagina(),paginamodulo.getModulo().getIdmodulo(),paginamodulo.getPagina().getIdpagina());
			if (pag==null) {
				
				Modulo mod = moduloMB.getModuloService().listarModuloPorId(
						paginamodulo.getModulo().getIdmodulo());
				mod.setIdmodulo(paginamodulo.getModulo().getIdmodulo());
				mod.setNombremodulo(mod.getNombremodulo());

				paginamodulo.setModulo(mod);

				String usermodificador = userMB.getUsuariologueado().getNombres()
						+ " " + userMB.getUsuariologueado().getApellidopat();
				Date fechamod = new Date();
				Pagina pagi = new Pagina();
				pagi = paginamodulo.getPagina();
				System.out.println("====::paginaid modif:::" + pagi.getIdpagina());
				System.out.println("====::pagina nombre modif:::"
						+ pagi.getNombrepagina());
				pagi.setUsrmod(usermodificador);
				pagi.setFecmod(fechamod);

				getPaginaService().registrarPagina(pagi);
				getPaginaService().actualizarPaginamodulo(paginamodulo);
				limpiarCampos();
				inicializar();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Exito", "Se Actualizó la  pagina "
								+ paginamodulo.getPagina().getDescripcionpagina()
								+ " Correctamente.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error",
						"No se puede actualizar, ya existe una pagina con el mismo nombre ");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}

		getUserMB().obtenerMenu();

		ultimoPagina = getPaginaService().obtenerUltimaPagina();
		fechaultimaPagina = getPaginaService().obtenerFechaUltimaPagina();
		numPaginas = getPaginaService().obtenerNumeroPaginas();

		// limpiarCampos();
	}

	public void eliminarPagina() {
		
		getPaginaService().eliminarPaginamodulo(paginamodulo);
		
		getPaginaService().eliminarPagina(paginamodulo.getPagina());
		
		getUserMB().obtenerMenu();
		
		ultimoPagina = getPaginaService().obtenerUltimaPagina();
		fechaultimaPagina = getPaginaService().obtenerFechaUltimaPagina();
		numPaginas = getPaginaService().obtenerNumeroPaginas();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Exito", "Se eliminó la pagina "
						+ paginamodulo.getPagina().getNombrepagina()
						+ " correctamente.");

		FacesContext.getCurrentInstance().addMessage(null, msg);
		limpiarCampos();
		inicializar();
	}

	public void limpiarCampos() {

		pagina = null;
		paginamodulo = null;
		actualizado = false;
	}

	public IPaginaService getPaginaService() {
		return paginaService;
	}

	public void setPaginaService(IPaginaService paginaService) {
		this.paginaService = paginaService;
	}

	public Pagina getPagina() {
		if (pagina == null) {
			pagina = new Pagina();
		}
		return pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}

	public List<Pagina> getPaginas() {
//		paginas = getPaginaService().listarPaginas();
		
		return paginas;
	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginas = paginas;
	}

	public String getUltimoPagina() {
		return ultimoPagina;
	}

	public void setUltimoPagina(String ultimoPagina) {
		this.ultimoPagina = ultimoPagina;
	}

	public IModuloService getModuloService() {
		return moduloService;
	}

	public void setModuloService(IModuloService moduloService) {
		this.moduloService = moduloService;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public int getNumPaginas() {
		return numPaginas;
	}

	public void setNumPaginas(int numPaginas) {
		this.numPaginas = numPaginas;
	}

	public Date getFechaultimaPagina() {
		return fechaultimaPagina;
	}

	public void setFechaultimaPagina(Date fechaultimaPagina) {
		this.fechaultimaPagina = fechaultimaPagina;
	}

	public Paginamodulo getPaginamodulo() {
		if (paginamodulo == null) {
			paginamodulo = new Paginamodulo();
		}
		return paginamodulo;
	}

	public void setPaginamodulo(Paginamodulo paginamodulo) {
		this.paginamodulo = paginamodulo;
	}

	public List<Paginamodulo> getPaginamodulos() {
//		paginamodulos = getPaginaService().listarPaginamodulos();
		return paginamodulos;
	}

	public void setPaginamodulos(List<Paginamodulo> paginamodulos) {
		this.paginamodulos = paginamodulos;
	}

	public boolean isActualizado() {
		return actualizado;
	}

	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}

	public ModuloManagedBean getModuloMB() {
		return moduloMB;
	}

	public void setModuloMB(ModuloManagedBean moduloMB) {
		this.moduloMB = moduloMB;
	}

}
