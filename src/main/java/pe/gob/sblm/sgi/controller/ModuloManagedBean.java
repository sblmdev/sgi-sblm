package pe.gob.sblm.sgi.controller;

import java.awt.Color;
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
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.service.IModuloService;

@ManagedBean(name = "moduloMB")
@ViewScoped
public class ModuloManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{moduloService}")
	private transient IModuloService moduloService;
	private Modulo modulo;
	private List<Modulo> modulos;
	private Modulo objetomodulo;

	private int numModulos;
	private String ultimoModulo;
	private Date fechaultimoModulo;
	private Modulo modu;
	private List<String> listapaginas;
	
	
	
	
	public ModuloManagedBean() {
		super();
		listapaginas = new ArrayList<String>();
	}

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	private List<Pagina>paginas;
	
	private boolean  actualizado;
	
	@PostConstruct
	public void initObjects() {

		try {
			numModulos = getModuloService().obtenerNumeroModulos();
			ultimoModulo = getModuloService().obtenerUltimoModulo();
			fechaultimoModulo= getModuloService().obtenerFechaUltimoModulo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onRowSelect(SelectEvent event) {
		listapaginas.clear();
		actualizado=true;
		
		List <Pagina> lstpagina= getModuloService().listarPaginasDeModulos(modulo.getIdmodulo());
		for (Pagina pag : lstpagina) {
			
			try {
				listapaginas.add(String.valueOf(pag.getIdpagina()));
			} catch (Exception e) {
			System.out.println(e.getMessage());	
			}
			
			
			
		}
		
		////paginas=lstpagina;
		
	}

	public void limpiarCampos() {
		modulo = null;
		actualizado=false;
		listapaginas.clear();
		
	}

	public void eliminarModulo() {
		//getModuloService().eliminarPerfilModulo(modulo.getIdmodulo());
		List <Perfilmodulo>  listmdl=new ArrayList <Perfilmodulo> ();
		listmdl=getModuloService().verficarModuloEnPerfil(modulo.getIdmodulo());
	
		
		
		
		if(listmdl.isEmpty()){
			getModuloService().eliminarPaginaModulo(modulo.getIdmodulo());
			getModuloService().eliminarModulo(modulo);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito","Se elimino el modulo "+modulo.getNombremodulo()+ " Correctamente."); 
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			numModulos = getModuloService().obtenerNumeroModulos();
			ultimoModulo = getModuloService().obtenerUltimoModulo();
			fechaultimoModulo= getModuloService().obtenerFechaUltimoModulo();
			getUserMB().obtenerMenu();
			limpiarCampos();
			
			
		}else{
			
			
			String listadependecias="";
			
			for (Perfilmodulo perfilmodulo : listmdl) {
				System.out.println("yyyyyyyyyyy:::::::."+perfilmodulo.getPerfil().getIdperfil());
				
				listadependecias=listadependecias+getModuloService().listarPerfilPorId(perfilmodulo.getPerfil().getIdperfil() ).getNombreperfil()+", ";
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error","El modulo tiene dependencias de los perfiles : "+listadependecias+ " Modulo no eliminado!! ."); 
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			
		}
		
	}

	public void registrarModulo() {
		
		if(!actualizado){
			System.out.println("Nuevo registro de modulo ");
			//REGISTRO DE MODULO
			String usercreador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			modulo.setUsrcre(usercreador);
			Date fechacre = new Date();
			modulo.setFeccre(fechacre);
			getModuloService().registrarModulo(modulo);
			
			//REGISTRO PAGINAMODULO
			Modulo modu =getModuloService().obtenerUltimoModulocreado();
			for(String idpagina: listapaginas){
				Pagina pagina=new Pagina();
				pagina.setIdpagina(Integer.parseInt(idpagina));
				Paginamodulo paginamod=new Paginamodulo();
				paginamod.setModulo(modu);
				paginamod.setPagina(pagina); 
				paginamod.setValortabla("modulo");
				getModuloService().registrarPaginamodulo(paginamod);
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito","Se creó el modulo "+modulo.getNombremodulo()+ " Correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg); 
			ultimoModulo = getModuloService().obtenerUltimoModulo();//lanzamos llamdo de nuemro modulos
			fechaultimoModulo= getModuloService().obtenerFechaUltimoModulo();
			numModulos = getModuloService().obtenerNumeroModulos();
			
		}else{
			System.out.println("actualizando modulo ");
			//ACTUALIZADO MODULO
			String usermodificador =userMB.getUsuariologueado().getNombres()+" "+userMB.getUsuariologueado().getApellidopat();
			modulo.setUsrmod(usermodificador);
			Date fechamod = new Date();
			modulo.setFecmod(fechamod);
			getModuloService().registrarModulo(modulo);
			
			getModuloService().eliminarPaginaModulo(modulo.getIdmodulo());
			Integer idpagmodulo;
			
			for(String idpagina: listapaginas){
				Pagina pagina=new Pagina();
				pagina.setIdpagina(Integer.parseInt(idpagina));
				Paginamodulo paginamod=new Paginamodulo();
				idpagmodulo=getModuloService().obtenerIdPaginaModulo(modulo.getIdmodulo(),pagina.getIdpagina());
				System.out.println("idpaginamodulo="+idpagmodulo);
				if(idpagmodulo!=null){
					System.out.println("idpaginamodulo2="+idpagmodulo);
					paginamod.setIdpaginamodulo(idpagmodulo);
				}
				paginamod.setModulo(modulo);
				paginamod.setPagina(pagina);
				paginamod.setValortabla("modulo");
				getModuloService().registrarPaginamodulo(paginamod);
			}
			
			
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito","Se Actualizó el modulo "+modulo.getNombremodulo()+ " Correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg); 
			System.out.println("####getNombremodulo::"+modulo.getNombremodulo());
			System.out.println("####getEstado::"+modulo.getEstado());
			
		}
		
		
		
		getUserMB().obtenerMenu();
		
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

	public IModuloService getModuloService() {
		return moduloService;
	}

	public void setModuloService(IModuloService moduloService) {
		this.moduloService = moduloService;
	}

	public List<Modulo> getModulos() {
		modulos = getModuloService().listarModulos();
		return modulos;
	}

	public Modulo getObjetomodulo() {
		if (objetomodulo == null) {
			objetomodulo = new Modulo();
		}

		return objetomodulo;
	}

	public void setObjetomodulo(Modulo objetomodulo) {
		this.objetomodulo = objetomodulo;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public int getNumModulos() {
		return numModulos;
	}

	public void setNumModulos(int numModulos) {
		this.numModulos = numModulos;
	}

	public String getUltimoModulo() {
		return ultimoModulo;
	}

	public void setUltimoModulo(String ultimoModulo) {
		this.ultimoModulo = ultimoModulo;
	}


	public Modulo getModu() {
		return modu;
	}


	public void setModu(Modulo modu) {
		this.modu = modu;
	}


	public UsuarioManagedBean getUserMB() {
		return userMB;
	}


	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}


	public Date getFechaultimoModulo() {
		return fechaultimoModulo;
	}


	public void setFechaultimoModulo(Date fechaultimoModulo) {
		this.fechaultimoModulo = fechaultimoModulo;
	}


	public List<String> getListapaginas() {
		return listapaginas;
	}


	public void setListapaginas(List<String> listapaginas) {
		this.listapaginas = listapaginas;
	}

	public boolean isActualizado() {
		return actualizado;
	}

	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}

	public List<Pagina> getPaginas() {
		paginas=getModuloService().listarPaginas();
		return paginas;
	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginas = paginas;
	}


	

}
