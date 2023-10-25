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

import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.entity.Ubigeo;
import pe.gob.sblm.sgi.service.IInmuebleOldService;

@ManagedBean(name = "inmuebleOldMB")
@ViewScoped
public class InmuebleOldManagedBean implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	@ManagedProperty(value = "#{inmuebleOldService}")
	private transient IInmuebleOldService inmuebleService;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;

	private List<Inmueble> inmuebles;
	private List<Inmueble> filtroinmuebles;

	private Inmueble inmueble;
	private Inmueble inmueblecapturado;
	boolean actualizado = false;
	private Inmueble resultadoinmueble;
	private int numinmuebles;
	private List<Tipovia> listaTipoVia;

	private List<Titularidad> listaTitularidad;
	private List<Tipotitularidad> listaTipoTitularidad;
	private List<Materialpredominante> listaMaterialPredominante;
	private List<Tipointerior> listaTipoInterior;

	private List<String> listaDptos;
	private List<String> listaProvincias;
	private List<String> listaDistritos;
	private List<String> listaUbigeos;

	private String valorDpto;
	private String valorProvincia;
	private String valorDistrito;

	private int tipourbanizacion;
	private boolean direccionnumero = false;
	private boolean repetido = false;
	private boolean repetidosbn = false;

	private boolean activoHU = false;
	
	private int idtipovia;
	private int idtitularidad;
	private int idtipotitularidad;
	private int idmaterialpredominante;
	private int idtipointerior;
	
	private String depa;
	private String prov;
	private String dist;
	
	private String idubigeo;

	/*******************************/
	@PostConstruct
	public void initObjects() {
		
		try {
			inicializarDatos();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mostrarNombreHabUrbana() {
		activoHU = inmueble.getHabilitacionurbana();
	}

	public void inicializarDatos() {
		inmuebles = getInmuebleService().listarInmuebles();

		listaTipoVia = getInmuebleService().listarTipoVia();
		listaTitularidad = getInmuebleService().listarTitularidad();
		listaTipoTitularidad = getInmuebleService().listarTipoTitularidad();
		listaMaterialPredominante = getInmuebleService().listarMaterialPredominante();
		listaTipoInterior = getInmuebleService().listarTipoInterior();
		listaUbigeos = getInmuebleService().listarUbigeos();

		listaDptos = getInmuebleService().listarDepartamentos();
	}

	public void activarNumero() {
		
		if (inmueble.getNumeroprincipal() == "") {
			System.out.println("vacio");
			direccionnumero = true;// x direccion
		} else {
			System.out.println("contenido");
			direccionnumero = false;// x manzana
		}

	}

	public void activarManzana() {
		if (inmueble.getManzana() == "") {
			direccionnumero = false;// x direccion
		} else {
			direccionnumero = true;// x manzana
		}
	}

	public void cargarUltimoregistro() {
		actualizado = true;
		inmueble = getInmuebleService().obtenerUltimoInmueble();
	}

	public void validarsbnRepetido(String numreg) {
		if (getInmuebleService().validarRepetido(numreg) != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Notificacion", "Ya existe El Numero de SBN!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			repetidosbn = true;
		}else{
			repetidosbn = false;
		}
	}

	public void validarclaveRepetido(String clave) {
		if (getInmuebleService().validarClaveRepetido(clave) != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Notificacion", "Ya existe la clave, ingrese otra clave!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			repetido = true;
		}else{
			repetido = false;
		}
	}




	/*******************************/

	public void registrarInmueble() {
			Date ahora = new Date();

		if (repetidosbn == false) {
			if (repetido == false) {
				if (actualizado == true) {

					String usermodificador = userMB.getUsuariologueado().getNombres()+ " "+ userMB.getUsuariologueado().getApellidopat();
					Ubigeo ubigeo= new Ubigeo();
					ubigeo.setUbigeo(idubigeo);
					inmueble.setUbigeo(ubigeo);
					inmueble.setFechmod(ahora);
					inmueble.setUsrmod(usermodificador);
					
					getInmuebleService().registrarInmueble(inmueble);
					// para mostrar la nueva lista en la tabla
					inmuebles = getInmuebleService().listarInmuebles();
					
					limpiarCampos();
					
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito","Se Actualizo correctamente el Inmueble.");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {

					Titularidad titu = new Titularidad();
					titu.setIdtitularidad(1);
					
					Tipotitularidad tiptitu = new Tipotitularidad();
					tiptitu.setIdtipotitularidad(8);
					
					Materialpredominante matpre = new Materialpredominante();
					matpre.setIdmaterialpredominante(6);
					
					
					Tipointerior tipointerior= new Tipointerior();
					tipointerior.setIdtipointerior(1);
					
					Tipovia tipovia= new Tipovia();
					tipovia.setIdtipovia(5);
					
					Ubigeo ubigeo= new Ubigeo();
					ubigeo.setUbigeo(idubigeo);
					inmueble.setUbigeo(ubigeo);
					
					inmueble.setTipovia(tipovia);		
					
					inmueble.setTitularidad(titu);
					inmueble.setTipotitularidad(tiptitu);
					inmueble.setMaterialpredominante(matpre);
					inmueble.setTipointerior(tipointerior);
					
					
					String usercreador = userMB.getUsuariologueado()
							.getNombres()
							+ " "
							+ userMB.getUsuariologueado().getApellidopat();
					inmueble.setUsrcre(usercreador);
					inmueble.setFechcre(ahora);

					getInmuebleService().registrarInmueble(inmueble);
					/****** cargado de archivos ****/
					// para mostrar la nueva lista en la tabla
					inmuebles = getInmuebleService().listarInmuebles();
					limpiarCampos();
					/****** ****/
					FacesMessage msg = new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Exito",
							"Se registro correctamente el Inmueble.");

					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} else {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error",
						"La clave ya se encuentra registrado en la Base de datos.");

				FacesContext.getCurrentInstance().addMessage(null, msg);
				repetido = true;
			}
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error",
					"El Numero SBN ya se encuentra registrado en la Base de datos.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
			repetidosbn = true;
		}
		

	}

	public void onRowSelect(SelectEvent event) {
		actualizado = true;

		depa=inmueble.getUbigeo().getDepa();
		prov=inmueble.getUbigeo().getProv();
		dist=inmueble.getUbigeo().getDist();
		
		listaProvincias = getInmuebleService().listarProvincias(depa);
		listaDistritos = getInmuebleService().listarDistritos(prov);
		
		idubigeo=inmueble.getUbigeo().getUbigeo();
		
	}

	public void eliminarInmueble() {
		System.out.println("controlador eliminar inmueble::");
		try {
			getInmuebleService().eliminarInmueble(inmueblecapturado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Se eliminó el Inmueble " + inmueblecapturado.getNumregistrosbn() + " correctamente.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
			limpiarCampos();
			inicializarDatos();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"Advertencia",
					"No se puede eliminar el inmueble porque tiene dependencias, Primero elimine las dependencias del inmueble.");

			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	

	public void cargarComboProvincias() {
		listaProvincias = getInmuebleService().listarProvincias(depa);
	}

	public void cargarComboDistritos() {
		listaDistritos = getInmuebleService().listarDistritos(prov);
	}

	public void cargarUbigeo() {
		listaUbigeos = getInmuebleService().listarUbigeoPorDistrito(depa, prov,dist);
		idubigeo=listaUbigeos.get(0);
	}

	public void cargarDdtoProvDist() {
		listaDptos = getInmuebleService().listarDptoPorUbigeo(
				inmueble.getUbigeo().getUbigeo());
		listaProvincias = getInmuebleService().listarProvinciaPorUbigeo(
				inmueble.getUbigeo().getUbigeo());
		listaDistritos = getInmuebleService().listarDistritoPorUbigeo(
				inmueble.getUbigeo().getUbigeo());
	}

	public void limpiarCampos() {
		listaDptos = getInmuebleService().listarDepartamentos();
		inmueble = new Inmueble();
		actualizado = false;
		repetido = false;
		repetidosbn = false;
	}



	public IInmuebleOldService getInmuebleService() {
		return inmuebleService;
	}

	public void setInmuebleService(IInmuebleOldService inmuebleService) {
		this.inmuebleService = inmuebleService;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public List<Inmueble> getInmuebles() {

		return inmuebles;
	}

	public void setInmuebles(List<Inmueble> inmuebles) {
		this.inmuebles = inmuebles;
	}

	public Inmueble getInmueble() {
		if (inmueble == null) {
			inmueble = new Inmueble();
		}
		return inmueble;
	}

	public void setInmueble(Inmueble inmueble) {
		this.inmueble = inmueble;
	}

	public Inmueble getInmueblecapturado() {
		return inmueblecapturado;
	}

	public void setInmueblecapturado(Inmueble inmueblecapturado) {
		this.inmueblecapturado = inmueblecapturado;
	}

	public Inmueble getResultadoinmueble() {
		return resultadoinmueble;
	}

	public void setResultadoinmueble(Inmueble resultadoinmueble) {
		this.resultadoinmueble = resultadoinmueble;
	}

	public int getNuminmuebles() {
		return numinmuebles;
	}

	public void setNuminmuebles(int numinmuebles) {
		this.numinmuebles = numinmuebles;
	}

	public List<String> getListaDptos() {

		return listaDptos;
	}

	public void setListaDptos(List<String> listaDptos) {
		this.listaDptos = listaDptos;
	}

	public List<String> getListaProvincias() {
		return listaProvincias;
	}

	public void setListaProvincias(List<String> listaProvincias) {
		this.listaProvincias = listaProvincias;
	}

	public List<String> getListaDistritos() {
		return listaDistritos;
	}

	public void setListaDistritos(List<String> listaDistritos) {
		this.listaDistritos = listaDistritos;
	}

	public String getValorDpto() {
		return valorDpto;
	}

	public void setValorDpto(String valorDpto) {
		this.valorDpto = valorDpto;
	}

	public String getValorProvincia() {
		return valorProvincia;
	}

	public void setValorProvincia(String valorProvincia) {
		this.valorProvincia = valorProvincia;
	}

	public String getValorDistrito() {
		return valorDistrito;
	}

	public void setValorDistrito(String valorDistrito) {
		this.valorDistrito = valorDistrito;
	}

	public List<Tipovia> getListaTipoVia() {

		return listaTipoVia;
	}

	public void setListaTipoVia(List<Tipovia> listaTipoVia) {
		this.listaTipoVia = listaTipoVia;
	}

	public List<String> getListaUbigeos() {

		return listaUbigeos;
	}

	public void setListaUbigeos(List<String> listaUbigeos) {
		this.listaUbigeos = listaUbigeos;
	}

	public List<Titularidad> getListaTitularidad() {

		return listaTitularidad;
	}

	public void setListaTitularidad(List<Titularidad> listaTitularidad) {
		this.listaTitularidad = listaTitularidad;
	}

	public List<Tipotitularidad> getListaTipoTitularidad() {

		return listaTipoTitularidad;
	}

	public void setListaTipoTitularidad(
			List<Tipotitularidad> listaTipoTitularidad) {
		this.listaTipoTitularidad = listaTipoTitularidad;
	}

	public List<Materialpredominante> getListaMaterialPredominante() {

		return listaMaterialPredominante;
	}

	public void setListaMaterialPredominante(
			List<Materialpredominante> listaMaterialPredominante) {
		this.listaMaterialPredominante = listaMaterialPredominante;
	}

	public List<Tipointerior> getListaTipoInterior() {

		return listaTipoInterior;
	}

	public void setListaTipoInterior(List<Tipointerior> listaTipoInterior) {
		this.listaTipoInterior = listaTipoInterior;
	}


	public boolean isDireccionnumero() {
		return direccionnumero;
	}

	public void setDireccionnumero(boolean direccionnumero) {
		this.direccionnumero = direccionnumero;
	}

	public int getTipourbanizacion() {
		return tipourbanizacion;
	}

	public void setTipourbanizacion(int tipourbanizacion) {
		this.tipourbanizacion = tipourbanizacion;
	}

	public boolean isActivoHU() {
		return activoHU;
	}

	public void setActivoHU(boolean activoHU) {
		this.activoHU = activoHU;
	}

	public boolean isRepetidosbn() {
		return repetidosbn;
	}

	public void setRepetidosbn(boolean repetidosbn) {
		this.repetidosbn = repetidosbn;
	}

	public int getIdtipovia() {
		return idtipovia;
	}

	public void setIdtipovia(int idtipovia) {
		this.idtipovia = idtipovia;
	}

	public int getIdtitularidad() {
		return idtitularidad;
	}

	public void setIdtitularidad(int idtitularidad) {
		this.idtitularidad = idtitularidad;
	}

	public int getIdtipotitularidad() {
		return idtipotitularidad;
	}

	public void setIdtipotitularidad(int idtipotitularidad) {
		this.idtipotitularidad = idtipotitularidad;
	}

	public int getIdmaterialpredominante() {
		return idmaterialpredominante;
	}

	public void setIdmaterialpredominante(int idmaterialpredominante) {
		this.idmaterialpredominante = idmaterialpredominante;
	}

	public int getIdtipointerior() {
		return idtipointerior;
	}

	public void setIdtipointerior(int idtipointerior) {
		this.idtipointerior = idtipointerior;
	}

	public String getDepa() {
		return depa;
	}

	public void setDepa(String depa) {
		this.depa = depa;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getIdubigeo() {
		return idubigeo;
	}

	public void setIdubigeo(String idubigeo) {
		this.idubigeo = idubigeo;
	}

	public List<Inmueble> getFiltroinmuebles() {
		return filtroinmuebles;
	}

	public void setFiltroinmuebles(List<Inmueble> filtroinmuebles) {
		this.filtroinmuebles = filtroinmuebles;
	}


}
