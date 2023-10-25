package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.service.IModuloService;

@ManagedBean(name = "modulopermisoMB")
@ViewScoped
public class ModuloPermisoManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
//	@ManagedProperty(value = "#{modulopermisoService}")
//	private transient IModuloPermisoService modulopermisoService;
	
	@ManagedProperty(value = "#{moduloService}")
	private transient IModuloService moduloService;
	
//	private Modulopermiso modulopermiso;
//	private List<Modulopermiso> modulopermisos;
//	
	//para capturar el valor de lectura/escritura de pagina perfil
	private boolean valorescritura;
	//para capturar el valor de estado de pagina perfil
	private boolean estado;
	private List<Boolean> estados;
	ArrayList<Boolean> estadosCapturados = new ArrayList<Boolean>();
	
	//para capturar el valor de estado de pagina perfil
	private String nombremodulocapturado;
	//lsita para capturar valores de la tabla
	private List<String> listaModuloSeleccionado;
	private List<Modulo> modulos;
	private Modulo modulo;
	
	public Modulo getModulo() {
		if (modulo == null) {
			modulo = new Modulo	();
		}
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public ModuloPermisoManagedBean(){
		
		listaModuloSeleccionado = new ArrayList<String>();
		
	}

	public void registrarModuloPermiso(){
		FacesContext facesContext = FacesContext.getCurrentInstance(); 
//		Map params = facesContext.getExternalContext().getRequestParameterMap(); 
//		String parametroObtenido= (String) params.get("nombreParametro"); 
//		System.out.println("parametroObtenido:::"+parametroObtenido);
//		System.out.println("valorescritura:::"+valorescritura);
//		System.out.println("estado:::"+estado);
//		System.out.println("nombremodulo:::"+nombremodulocapturado);
//		
		System.out.println("xxxxxxxx");
		
		

		for (int i=0;i<modulos.size();i++) {

			System.out.println("####################");
						System.out.println("getNombremodulo:::"+modulos.get(i).getNombremodulo());
						System.out.println("getIdmodulo:::"+modulos.get(i).getIdmodulo());
						System.out.println("getEstado:::"+modulos.get(i).getEstado());
						System.out.println("getTipopermiso:::"+modulos.get(i).getTipopermiso());
						
						Map params = facesContext.getExternalContext().getRequestParameterMap(); 
						String parametroObtenido= (String) params.get("nombreParametro"); 
					//	System.out.println("parametroObtenido:::"+parametroObtenido);
						
//			Modulopermiso modulopermiso= new Modulopermiso();
						
						
//			modulopermiso.setModulo(modulo);
//			modulopermiso.setPermiso(permiso);
//			
//			Perfilusuario perfilusuario=new Perfilusuario();
//
//			perfilusuario.setPerfil(perfil);
//			perfilusuario.setUsuario(user);
//			perfilusuario.setFeccre(new Date());
//			perfilusuario.setFecmod(new Date());
//			perfilusuario.setUsrcre("super");
//			
//			panelSuperUsuarioServiceImpl.save(perfilusuario);
		}
		
//		FacesMessage msg = new FacesMessage("Id :"+modulopermiso.getIdmodulopermiso()); 
//		getModulopermisoService().registrarModuloPermiso(modulopermiso);
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
//	public Modulopermiso getModulopermiso() {
//		return modulopermiso;
//	}
//	public void setModulopermiso(Modulopermiso modulopermiso) {
//		this.modulopermiso = modulopermiso;
//	}
//	public List<Modulopermiso> getModulopermisos() {
//		modulopermisos=getModulopermisoService().listarModuloPermisos();
//		return modulopermisos;
//	}
//	public void setModulopermisos(List<Modulopermiso> modulopermisos) {
//		this.modulopermisos = modulopermisos;
//	}
//
//	public IModuloPermisoService getModulopermisoService() {
//		return modulopermisoService;
//	}
//
//	public void setModulopermisoService(IModuloPermisoService modulopermisoService) {
//		this.modulopermisoService = modulopermisoService;
//	}

	public boolean isValorescritura() {
		return valorescritura;
	}

	public void setValorescritura(boolean valorescritura) {
		this.valorescritura = valorescritura;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	
	public String getNombremodulocapturado() {
		return nombremodulocapturado;
	}

	public void setNombremodulocapturado(String nombremodulocapturado) {
		this.nombremodulocapturado = nombremodulocapturado;
	}

	public List<String> getListaModuloSeleccionado() {
		return listaModuloSeleccionado;
	}
	public void setListaModuloSeleccionado(List<String> listaModuloSeleccionado) {
		this.listaModuloSeleccionado = listaModuloSeleccionado;
	}

	public IModuloService getModuloService() {
		return moduloService;
	}

	public void setModuloService(IModuloService moduloService) {
		this.moduloService = moduloService;
	}int i=0;
	public List<Modulo> getModulos() {
		modulos=getModuloService().listarModulos();
		System.out.println("########"+i);
		i++;
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public List<Boolean> getEstados() {
		return estados;
	}

	public void setEstados(List<Boolean> estados) {
		this.estados = estados;
	}

	
}
