package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Inmueblesaneamiento;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Origendominio;
import pe.gob.sblm.sgi.entity.Saneamiento;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Tipozona;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.entity.Ubigeo;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.IInmuebleService;

@ManagedBean(name = "inmuebleMB")
@ViewScoped
public class InmuebleManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	
	
	@ManagedProperty(value = "#{inmuebleService}")
	private transient IInmuebleService inmuebleService;
	
	
	private List<String> listaDepartamentos;
	private List<String> listaProvincias;
	private List<String> listaDistritos;
	private List<String> listaTipoTitularidad;
	
	private String depa;
	private String prov;
	private String dist;
	private String ttitularidad;
	private String claveInmueble;
	


	private InmuebleBean inmuebleBean= new InmuebleBean();
	private List<InmuebleBean> listaInmuebleBean= new ArrayList<InmuebleBean>();
	private List<InmuebleBean> listaInmuebleBeanFilter;
	
	
	private List<Tipovia> listaTipoVia;
	private List<Origendominio> listaOrigenDominio;
	private List<Tipozona> listaTipoZona;
	private List<Materialpredominante> listaMaterialPredominante;
	
	
	private List<Saneamiento> listaCondicionSaneamiento;
	private List<Saneamiento> listaCondicionSaneamientoSeleccionado;/**Vista**/
	private List<String> listaCondSaneamientoSeleccionado;
	private List<Inmueblesaneamiento> listaCondicionInmuebleSaneamientoSeleccionado;/**Vista**/

	private List<InmuebleBean> listainmueblematriz = new ArrayList<InmuebleBean>();
	private String tipoBusqueda=Constantes.BUSQUEDA_DIRECCION;
	private List<String> selectedTabTipoTitularidad= new ArrayList<String>();
	private List<Inmueblemaestro> listaInmuebleMaestro = new ArrayList<Inmueblemaestro>();
	private Inmueblemaestro inmuebleSeleccionado ;
	
	/**Mapa**/
	private MapModel simpleModel;
	
	/**Busqueda**/
	private String valorbusqueda;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init(){
		valoresInicialesInmueble();
		obtenerUbicacionGmap();
		listaCondicionSaneamientoSeleccionado = new ArrayList<Saneamiento>();
		listaCondSaneamientoSeleccionado = new ArrayList<String>();
		listaDepartamentos = inmuebleService.listarDepartamentos();
		listaTipoTitularidad = inmuebleService.listarTipoTitularidad();
		listaInmuebleMaestro = inmuebleService.listarInmuebleMaestro();
		inicializandoValoresSeleccionadosTabTipoTitularidadConsulta();
	}
	
	
	public void valoresInicialesInmueble(){
		inmuebleBean.setCodigotitularidad("01");
		inmuebleBean.setLatitudlocalizacion("-12.050197");
		inmuebleBean.setLongitudlocalizacion("-77.032738");
		generarClaveInmuebleSgt();
		listaTipoVia=inmuebleService.listarTipoVia();
		listaOrigenDominio=inmuebleService.listarOrigenDominio();
		listaTipoZona=inmuebleService.listarTipoZona();
		listaMaterialPredominante=inmuebleService.listarMaterialPredominante();
		listaCondicionSaneamiento=inmuebleService.listarCondicionesSaneamiento();
		
	
	}
	
	public void generarClaveInmuebleSgt(){
		inmuebleBean.setCodigoinmueble(inmuebleService.obtenerSgtCodigoInmueble(inmuebleBean.getCodigotitularidad()));
	}
	
	public void buscarInmueble(){
		try{
			listaInmuebleBean.clear();
			listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusqueda,tipoBusqueda);
		if(listaInmuebleBean.size()==0){
			addWarnMessage(""," No se encuentra el inmueble buscado .");
		}else{
			addInfoMessage("", " Busqueda exitosa !!! .");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	 
	 public void verificarExistenciaTitularidad(){
		 Titularidad titularidad=inmuebleService.obtenerTitularidad(inmuebleBean.getCodigotitularidad());
		 
		 if (inmuebleBean.getCodigotitularidad().length()<2) {
			addWarnMessage("Codigó de titularidad no existe", "Ingresar un codigo de titularidad valido, número de digitos debe ser igual a 2 .");
			 inmuebleBean.setNombretitularidad("");
			 inmuebleBean.setTipotitularidad("");		
		 } 
		 else if(titularidad!=null){

			 inmuebleBean.setNombretitularidad(titularidad.getNombre());
			 inmuebleBean.setTipotitularidad(titularidad.getTipotitularidad().getDescripcion());
			 inmuebleBean.setCodigoinmueble(inmuebleService.obtenerSgtCodigoInmueble(inmuebleBean.getCodigotitularidad()));
			 inmuebleBean.setIdtitularidad(titularidad.getIdtitularidad());
		 }
		 else {
			addWarnMessage("Codigó de titularidad no existe", "Ingresar un codigo de titularidad valido, en caso de no estar registrado contactarse con el area de informática.");
			 inmuebleBean.setNombretitularidad("");
			 inmuebleBean.setTipotitularidad("");
			 inmuebleBean.setIdtitularidad(0);
		 }
	 }
	 
	 
	public void validarGrabarInmueble(){
		
		if (inmuebleBean.getIdtitularidad()==0) {
			addWarnMessage("","Ingrese un codigo de titularidad válido y/o existente.");
		
		}else if (inmuebleBean.getIdinmueble()==0 && inmuebleService.siExisteClaveInmueble(inmuebleBean.getClave())) {
			addWarnMessage("","Clave de inmueble ya existe, ingrese una clave de inmueble distinta para continuar o dar clic en el boton debajo de la clave del inmueble para que el sistema asigne una clave automaticamente");
		
		}else if (depa.equals("0") || prov.equals("0") || dist.equals("0")) {
			addWarnMessage("","Seleccionar la procedencia del inmueble (departamento,provincia distrito)  ");
		
		}
		else if (inmuebleBean.getIdtipovia()==0 ||  inmuebleBean.getIdtipozona()==0){
			addWarnMessage("","Ingrese tipo de zona y tipo de vía. ");

		}else if (  inmuebleBean.getDireccionprincipal().equals("")){
			addWarnMessage("","Ingrese número principal o identificador de la dirección principal.");

		}else if (inmuebleBean.getLatitudlocalizacion().equals("")|| inmuebleBean.getLongitudlocalizacion().equals("")){
			addWarnMessage("Usted no ha ingresado las coordenadas geolocalización","Para proceder a registrar inmueble es obligatorio especificar las coordenadas de geolocalización.");

		}
		else if (inmuebleBean.getIdorigendominio()==0){
			addWarnMessage("","Usted no ha ingresado el origen de dominio.");
		}
		else{
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dlgRegistrarInmueble.show();");
		}
	}
	
	
	
	public void grabarInmueble(){
		Inmueblemaestro inmueble= new Inmueblemaestro();
		Ubigeo ubigeo= new Ubigeo();
		ubigeo.setUbigeo(inmuebleBean.getUbigeo());
		
		Tipovia tipovia= new Tipovia();
		tipovia.setIdtipovia(inmuebleBean.getIdtipovia());
		
		
		if (inmuebleBean.getIdorigendominio()!=0) {
			Origendominio origendominio= new Origendominio();
			origendominio.setIdorigendominio(inmuebleBean.getIdorigendominio());
			inmueble.setOrigendominio(origendominio);
		}
		
		
		Tipozona tipozona=new Tipozona();
		tipozona.setIdtipozona(inmuebleBean.getIdtipozona());
		
		if (inmuebleBean.getIdmaterialpredominante()!=0) {
			Materialpredominante materialpredominante= new Materialpredominante();
			materialpredominante.setIdmaterialpredominante(inmuebleBean.getIdmaterialpredominante());
			inmueble.setMaterialpredominante(materialpredominante);
		}

		
		Titularidad titularidad= new Titularidad();
		titularidad.setIdtitularidad(inmuebleBean.getIdtitularidad());
		inmuebleBean.setClave(inmuebleBean.getCodigotitularidad()+"-"+inmuebleBean.getCodigoinmueble()+"-000");
		inmueble.setUbigeo(ubigeo);
		inmueble.setTipovia(tipovia);
		
		inmueble.setTipozona(tipozona);
		
		inmueble.setTitularidad(titularidad);
		inmueble.setClave(inmuebleBean.getClave());
		
		inmueble.setCodigoinmueble(inmuebleBean.getCodigoinmueble());
//		System.out.println("Tipo titularidad: "+titularidad);
//		System.out.println("clave: "+inmuebleBean.getClave());
//		System.out.println("codigo de inmueble: "+inmuebleBean.getCodigoinmueble());
		inmueble.setNumregistrosbn(inmuebleBean.getNumregistrosbn());

		inmueble.setNombretipovia(inmuebleBean.getNombretipovia().trim());
		inmueble.setNombretipozona(inmuebleBean.getNombretipozona().trim());
		inmueble.setDireccionprincipal(inmuebleBean.getDireccionprincipal().trim());
		inmueble.setDireccionreferencia(inmuebleBean.getReferenciadireccion().trim());
		
	
		
		inmueble.setDireccion(generarDireccionInmueble().trim());
		
		
		inmueble.setSimonumentohistorico(inmuebleBean.getSimonumentohistorico());;
		inmueble.setTipomonumentohistorico(inmuebleBean.getTipomonumentohistorico());;
		inmueble.setResolucionmonumentohistorico(inmuebleBean.getResolucionmonumentohistorico());;
		inmueble.setFecharesolucionmonumentohistorico(inmuebleBean.getFecresolucionmonumentohistorico());;
		
		inmueble.setCodigopredio(inmuebleBean.getCodigopredio());;
		inmueble.setFechainscripcionregistral(inmuebleBean.getFecinscripcionregistral());
		inmueble.setAsientoregistral(inmuebleBean.getAsientofabrica());;
		inmueble.setFojasregistral(inmuebleBean.getFojasregistral());;
		inmueble.setTomoregistral(inmuebleBean.getTomoregistral());;
		inmueble.setFicharegistral(inmuebleBean.getFicharegistral());;
		inmueble.setPartidaelectronicaregistral(inmuebleBean.getPartidaelectronicaregistral());;
		inmueble.setObservacionregistral(inmuebleBean.getObservacionregistral());
		
		
		inmueble.setSideclaratoriafabrica(inmuebleBean.getSideclaratoriafabrica());;
		inmueble.setAsientofabrica(inmuebleBean.getAsientofabrica());;
		inmueble.setFojasfabrica(inmuebleBean.getFojasfabrica());;
		inmueble.setTomofabrica(inmuebleBean.getTomofabrica());;
		inmueble.setFichafabrica(inmuebleBean.getFichafabrica());;
		inmueble.setPartidaelectronicafabrica(inmuebleBean.getPartidaelectronicafabrica());
		inmueble.setObservacionfabrica(inmuebleBean.getObservacionfabrica());;				
		
		
		inmueble.setSiindependizacion(inmuebleBean.getSiindependizacion());;
		inmueble.setUnidadesinmobiliariasindependizacion(inmuebleBean.getUnidadesinmobiliariasindependizacion());;
		inmueble.setAsientoindependizacion(inmuebleBean.getAsientoindependizacion());;
		inmueble.setFojasindependizacion(inmuebleBean.getFojasindependizacion());;
		inmueble.setTomoindependizacion(inmuebleBean.getTomoindependizacion());;
		inmueble.setFichaindependizacion(inmuebleBean.getFichaindependizacion());;
		inmueble.setPartidaelectronicaindependizacion(inmuebleBean.getPartidaelectronicaindependizacion());;
		inmueble.setFechainscripcionindependizacion(inmuebleBean.getFechainscripcionindependizacion());;
		inmueble.setObservacionindependizacion(inmuebleBean.getObservacionindependizacion());	
		
		inmueble.setSigravamen(inmuebleBean.getSigravamen());;
		inmueble.setAfavordegravamen(inmuebleBean.getAfavordegravamen());;
		inmueble.setAsientogravamen(inmuebleBean.getAsientogravamen());;
		inmueble.setFojasgravamen(inmuebleBean.getFojasgravamen());;
		inmueble.setTomogravamen(inmuebleBean.getTomogravamen());;
		inmueble.setFichagravamen(inmuebleBean.getFichagravamen());;
		inmueble.setPartidaelectronicagravamen(inmuebleBean.getPartidaelectronicagravamen());;
		inmueble.setFechainscripciongravamen(inmuebleBean.getFechainscripciongravamen());;
		inmueble.setObservaciongravamen(inmuebleBean.getObservaciongravamen());;
		
		
		inmueble.setSimandascargas(inmuebleBean.getSimandascargas());;
		inmueble.setAfavordemandascargas(inmuebleBean.getAfavordemandascargas());;
		inmueble.setAsientomandascargas(inmuebleBean.getAsientomandascargas());;
		inmueble.setFojasmandascargas(inmuebleBean.getFojasmandascargas());;
		inmueble.setTomomandascargas(inmuebleBean.getTomomandascargas());;
		inmueble.setFichamandascargas(inmuebleBean.getFichamandascargas());;
		inmueble.setPartidaelectronicamandascargas(inmuebleBean.getPartidaelectronicamandascargas());;
		inmueble.setFechainscripcionmandascargas(inmuebleBean.getFechainscripcionmandascargas());;
		inmueble.setObservacionmandascargas(inmuebleBean.getObservacionmandascargas());;
		
		inmueble.setSisaneado(inmuebleBean.getSisaneado());;
		
		
		//Tecnicos

		inmueble.setAnioconstruccion(inmuebleBean.getAnioconstruccion());;
		inmueble.setNumpisos(inmuebleBean.getNropisos());;
		inmueble.setAreaterreno(inmuebleBean.getAreaterreno());;
		inmueble.setAreaconstruida(inmuebleBean.getAreaconstruida());;
		
		inmueble.setSipartidaregistral(inmuebleBean.getSipartidaregistral());;
		inmueble.setSiplanoubicacion(inmuebleBean.getSiplanoubicacion());;
		inmueble.setSimemoriadescrip(inmuebleBean.getSimemoriadescriptiva());;
		inmueble.setSifotos(inmuebleBean.getSifotografias());
		inmueble.setSitasacion(inmuebleBean.getSitasacion());;
		inmueble.setFechatasacion(inmuebleBean.getFectasacion());
		
		
		/**Localizacion**/
		inmueble.setLongitudlocalizacion(inmuebleBean.getLongitudlocalizacion());
		inmueble.setLatitudlocalizacion(inmuebleBean.getLatitudlocalizacion());
		inmueble.setDescripcionlocalizacion(inmuebleBean.getDescripcionlocalizacion());
		inmueble.setSidocumento(inmuebleBean.getSidocumento()==null? Boolean.FALSE:inmuebleBean.getSidocumento());
		
		if (inmuebleBean.getIdinmueble()!=0) {/**Actualizar*/
			inmueble.setIdinmueble(inmuebleBean.getIdinmueble());			
			inmueble.setFechcre(inmuebleBean.getFeccre());
			inmueble.setUsrcre(inmuebleBean.getUsrcre());
			inmueble.setFechmod(new Date());
			inmueble.setUsrmod(userMB.getNombrecompleto());
			inmueble.setEstado(true);
			inmueble.setInmueblesaneamientos(obtenerEstadoCondicionesSaneamientoInmueble(inmueble,"update"));
			
		}else {/**Nuevo*/
			
			inmueble.setFechcre(new Date());
			inmueble.setUsrcre(userMB.getNombrecompleto());
			inmueble.setInmueblesaneamientos(obtenerEstadoCondicionesSaneamientoInmueble(inmueble,"insert"));
			inmueble.setSidocumento(false);
			inmueble.setEstado(true);
		}
		
		
		getInmuebleService().grabarInmueble(inmueble);/**Graba o actualiza*/
		addInfoMessage("Se grabo con exito", "Se grabo la información del inmueble correctamente. ");
	
		limpiarformulario();	
	}
	
	public Set<Inmueblesaneamiento> obtenerEstadoCondicionesSaneamientoInmueble(Inmueblemaestro inmueble,String tipo){
		

		Set<Inmueblesaneamiento> inmueblesaneamientos= new HashSet<Inmueblesaneamiento>();
		if (tipo.equals("insert")) {
			
			for (Saneamiento saneamiento : listaCondicionSaneamiento) {
				
				Inmueblesaneamiento inmueblesaneamiento=new Inmueblesaneamiento();
				inmueblesaneamiento.setActivo(false);
				
				for (Saneamiento selected: listaCondicionSaneamientoSeleccionado) {
					if (saneamiento.getIdsaneamiento()==selected.getIdsaneamiento()){
						inmueblesaneamiento.setActivo(true);		
					}
				}
				inmueblesaneamiento.setSaneamiento(saneamiento);
				inmueblesaneamiento.setInmueble(inmueble);
				inmueblesaneamientos.add(inmueblesaneamiento);
			}
		} else {
			
			/**Actualizando para en caso que haya aumentado el numero de registros de saneamiento al actualizar**/
			if (listaCondicionSaneamiento.size()!=listaCondicionInmuebleSaneamientoSeleccionado.size()) {
				
				for (Saneamiento saneamiento : listaCondicionSaneamiento) {
					boolean encontrado=false;
					

					
					for (Inmueblesaneamiento selected: listaCondicionInmuebleSaneamientoSeleccionado) {
						if (saneamiento.getIdsaneamiento()==selected.getSaneamiento().getIdsaneamiento()){
							encontrado=true;
						}
					}
					
					if (!encontrado) {
						Inmueblesaneamiento inmueblesaneamiento=new Inmueblesaneamiento();
						inmueblesaneamiento.setActivo(false);
						inmueblesaneamiento.setIdinmueblesaneamiento(0);
						inmueblesaneamiento.setSaneamiento(saneamiento);
						inmueblesaneamiento.setInmueble(inmueble);
						listaCondicionInmuebleSaneamientoSeleccionado.add(inmueblesaneamiento);
					}
					
				}
			}

			
			/**Actualizando la lista obtenida de la base de datos a como esta  en la vista para actualizarlo**/
			for (Inmueblesaneamiento selected : listaCondicionInmuebleSaneamientoSeleccionado) {
				selected.setActivo(false);
//				for (Saneamiento selectedVista : (List<Saneamiento>)listaCondicionSaneamientoSeleccionado) {
//						if (selected.getSaneamiento().getIdsaneamiento()==selectedVista.getIdsaneamiento()) {
//							selected.setActivo(true);			
//						}
//				}
				for (String idcond : listaCondSaneamientoSeleccionado){
					if (selected.getSaneamiento().getIdsaneamiento()== Integer.valueOf(idcond)) {
						selected.setActivo(true);			
						}
				}
			}
			
			inmueblesaneamientos.addAll(listaCondicionInmuebleSaneamientoSeleccionado);
		}
		
		
		return inmueblesaneamientos;
	}
	
	
	public void obtenerOpcionesSaneamiento(){
		//listaCondicionSaneamientoSeleccionado.clear();
		listaCondSaneamientoSeleccionado.clear();
		listaCondicionInmuebleSaneamientoSeleccionado=inmuebleService.obtenerCondicionesActivasInmueble(inmuebleBean.getIdinmueble());
		
		for (Inmueblesaneamiento selected: listaCondicionInmuebleSaneamientoSeleccionado) {
			for (Saneamiento saneamiento : listaCondicionSaneamiento) {
				if (selected.getSaneamiento().getIdsaneamiento()==saneamiento.getIdsaneamiento() && selected.getActivo()) {
					//listaCondicionSaneamientoSeleccionado.add(saneamiento);
					listaCondSaneamientoSeleccionado.add(String.valueOf(saneamiento.getIdsaneamiento()));
				}
			}
		}	
	}

	
	public void validarEliminarInmueble(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inmuebleBean.getIdinmueble()!=0) {
			Inmueble inmueble= new Inmueble();
			inmueble.setIdinmueble(inmuebleBean.getIdinmueble());
			getInmuebleService().eliminarInmueble(inmueble);
			context.addMessage(null, new FacesMessage("Baja de Inmueble exitosa","Se ha dado de baja el inmueble exitosamente "));
			
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para eliminar ","Para proceder a dar de baja el inmueble es obligatorio seleccionar un inmueble"));
			limpiarformulario();
		}
	}
	
	public void validarAdjuntarArchivo(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (inmuebleBean.getIdinmueble()!=0) {
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dglAdjuntarArchivo.show();");
		} else {
			context.addMessage(null, new FacesMessage("Seleccionar un inmueble para adjuntar ","Para proceder a adjuntar archivo es obligatorio seleccionar un inmueble"));
		}
	}
	
	
	public void verificarSaneamientoInmueble(String event){
		
		if (event.equals("change")) {
			if (listaCondicionSaneamiento.size()==listaCondicionSaneamientoSeleccionado.size()) {
				inmuebleBean.setSisaneado(true);
				
			}else {
				inmuebleBean.setSisaneado(false);
			}
		}else {
			if (listaCondicionSaneamiento.size()!=listaCondicionSaneamientoSeleccionado.size()) {
				inmuebleBean.setSisaneado(true);
				
			}else {
				inmuebleBean.setSisaneado(false);
			}
		}
		
	}
	
	
	
	public void limpiarformulario(){
		depa="0";
		prov="0";
		dist="0";
		
		inmuebleBean= new InmuebleBean();
		
		listaCondicionSaneamientoSeleccionado.clear();
		listaCondSaneamientoSeleccionado.clear();
		init();
	}
	
	

	public void obtenerUbicacionGmap(){
		if (inmuebleBean.getLongitudlocalizacion()!=null  && inmuebleBean.getLatitudlocalizacion()!=null) {
			 simpleModel = new DefaultMapModel();
			  
			 LatLng coord1 = new LatLng(Double.parseDouble(inmuebleBean.getLatitudlocalizacion()),Double.parseDouble(inmuebleBean.getLongitudlocalizacion()));
			  
			  
		      simpleModel.addOverlay(new Marker(coord1, inmuebleBean.getDescripcionlocalizacion()));
		} else {
			 
			 simpleModel = new DefaultMapModel();
			  
			 LatLng coord1 = new LatLng(Double.parseDouble("-12.194171"),Double.parseDouble("-76.9904426"));
		     
			  
		     simpleModel.addOverlay(new Marker(coord1, "Sociedad de Beneficencia de Lima Metropolitana"));
		     inmuebleBean.setLatitudlocalizacion("-12.194171");
		     inmuebleBean.setLongitudlocalizacion("-76.9904426");
		}
		
		
		 
	}
	

	
	public void validarGrabarLocalizacion(){
		if (inmuebleBean.getLatitudlocalizacion().equals("") || inmuebleBean.getLongitudlocalizacion().equals("")) {
			addWarnMessage("","");
		} else {
			RequestContext requestContext= RequestContext.getCurrentInstance();
			requestContext.execute("");
		}
	}
	
	public void grabarLocalizacion(){
		inmuebleService.grabarLocalizacion(inmuebleBean.getIdinmueble(),inmuebleBean.getLatitudlocalizacion(),inmuebleBean.getLongitudlocalizacion(),inmuebleBean.getDescripcionlocalizacion());
	}
	

	public void setearInmuebleSeleccionado() throws Exception{
			depa=inmuebleBean.getDepartamento();
			prov=inmuebleBean.getProvincia();
			dist=inmuebleBean.getDistrito();
			
			cargarComboProvincias();
			cargarComboDistritos();
			cargarUbigeo();
			obtenerUbicacionGmap();
			obtenerOpcionesSaneamiento();
		}
	


	public String generarDireccionInmueble(){
		String direccion="";
		String nombretipovia=inmuebleBean.getNombretipovia()==null? " ":( inmuebleBean.getNombretipovia().equals("")? " ":inmuebleBean.getNombretipovia());
		String direccionprincipal=inmuebleBean.getDireccionprincipal()==null? " ":(inmuebleBean.getDireccionprincipal().equals("")? " ":inmuebleBean.getDireccionprincipal());
		String nombretipozona=inmuebleBean.getNombretipozona()==null?" ":(inmuebleBean.getNombretipozona().equals("")? " ":inmuebleBean.getNombretipozona());
		direccion=obtenerAbreviaturaTipoVia(inmuebleBean.getIdtipovia())+" "+nombretipovia+ " "
				 +" "+direccionprincipal+" "
				 +obtenerAbreviaturaTipoZona(inmuebleBean.getIdtipozona())+" "+nombretipozona;
		
		return direccion;
	}
	
	public String obtenerAbreviaturaTipoVia(int idtipovia){
		for (Tipovia tipovia : listaTipoVia) {
			if (tipovia.getIdtipovia()==idtipovia) {
				return tipovia.getAbreviatura();
			}
		}
		return "";
	}
	
	public String obtenerAbreviaturaTipoZona(int idtipozona){
		for (Tipozona tipozona : listaTipoZona) {
			if (tipozona.getIdtipozona()==idtipozona) {
				return tipozona.getAbreviatura();
			}
		}
		return "";
	}
	
	public void cargarUbigeo() {
		try{
		inmuebleBean.setUbigeo(getInmuebleService().obtenerIdUbigeo(depa, prov,dist));
		}catch(Exception e ){
			addWarnMessage("Error","Ubigeo no encontrado");
			e.printStackTrace();
		}
		
	}
	
	
	public void cargarComboProvincias() {
		listaProvincias = getInmuebleService().listarProvincias(depa);
		System.out.println("InmuebleMB depa: "+depa);
		System.out.println("Tamaño lista Provincias "+ listaProvincias.size());
		
		Iterator iter = listaProvincias.iterator();
		while (iter.hasNext())
		  System.out.println(iter.next());
		
		
//		for(int i=0;i<listaProvincias.size();i++){
//			System.out.println("Provincia "+i+" : "+listaProvincias[i]);
//		}
	}
	public void buscarInmuebleMatriz(){
		listainmueblematriz.clear();
		String claveinmueble=null;
		if(inmuebleSeleccionado!=null){
			claveinmueble=inmuebleSeleccionado.getClave();
		}
		listainmueblematriz=inmuebleService.listarInmueblematriz(claveinmueble, depa, prov, dist, ttitularidad);
		System.out.println("oka");
	}
	public void cargaInmueble(){
		if(inmuebleSeleccionado!=null){
			System.out.println("clave="+inmuebleSeleccionado.getClave());
			}else{
				System.out.println("es null");
			}
		if(ttitularidad!=null){
			System.out.println("tipotitularidad="+ttitularidad);
		}
	}
	/*Inicializar tipo de documentos seleccionados para el Tab Cobrador de Consulta de comprobante*/
	
	public void inicializandoValoresSeleccionadosTabTipoTitularidadConsulta(){
		selectedTabTipoTitularidad.add("5");
		selectedTabTipoTitularidad.add("6");
		selectedTabTipoTitularidad.add("7");
		selectedTabTipoTitularidad.add("8");
		selectedTabTipoTitularidad.add("9");
		
	}
	public void cargarComboDistritos() {
		listaDistritos = getInmuebleService().listarDistritos(prov);
		System.out.println("provincia: " + prov);
	}
	public IInmuebleService getInmuebleService() {
		return inmuebleService;
	}
	public void setInmuebleService(IInmuebleService inmuebleService) {
		this.inmuebleService = inmuebleService;
	}
	public List<String> getListaDepartamentos() {
		return listaDepartamentos;
	}
	public void setListaDepartamentos(List<String> listaDepartamentos) {
		this.listaDepartamentos = listaDepartamentos;
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
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public InmuebleBean getInmuebleBean() {
		return inmuebleBean;
	}
	public void setInmuebleBean(InmuebleBean inmuebleBean) {
		this.inmuebleBean = inmuebleBean;
	}
	public List<InmuebleBean> getListaInmuebleBean() {
		return listaInmuebleBean;
	}
	public void setListaInmuebleBean(List<InmuebleBean> listaInmuebleBean) {
		this.listaInmuebleBean = listaInmuebleBean;
	}
	public List<InmuebleBean> getListaInmuebleBeanFilter() {
		return listaInmuebleBeanFilter;
	}
	public void setListaInmuebleBeanFilter(List<InmuebleBean> listaInmuebleBeanFilter) {
		this.listaInmuebleBeanFilter = listaInmuebleBeanFilter;
	}
	public String getValorbusqueda() {
		return valorbusqueda;
	}
	public void setValorbusqueda(String valorbusqueda) {
		this.valorbusqueda = valorbusqueda;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}
	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}
	public List<Tipovia> getListaTipoVia() {
		return listaTipoVia;
	}
	public void setListaTipoVia(List<Tipovia> listaTipoVia) {
		this.listaTipoVia = listaTipoVia;
	}
	public List<Saneamiento> getListaCondicionSaneamiento() {
		return listaCondicionSaneamiento;
	}
	public void setListaCondicionSaneamiento(List<Saneamiento> listaCondicionSaneamiento) {
		this.listaCondicionSaneamiento = listaCondicionSaneamiento;
	}
	public List<Saneamiento> getListaCondicionSaneamientoSeleccionado() {
		return listaCondicionSaneamientoSeleccionado;
	}
	public void setListaCondicionSaneamientoSeleccionado(List<Saneamiento> listaCondicionSaneamientoSeleccionado) {
		this.listaCondicionSaneamientoSeleccionado = listaCondicionSaneamientoSeleccionado;
	}
	public List<Tipozona> getListaTipoZona() {
		return listaTipoZona;
	}
	public void setListaTipoZona(List<Tipozona> listaTipoZona) {
		this.listaTipoZona = listaTipoZona;
	}
	public List<Materialpredominante> getListaMaterialPredominante() {
		return listaMaterialPredominante;
	}
	public void setListaMaterialPredominante(List<Materialpredominante> listaMaterialPredominante) {
		this.listaMaterialPredominante = listaMaterialPredominante;
	}

	public List<Inmueblesaneamiento> getListaCondicionInmuebleSaneamientoSeleccionado() {
		return listaCondicionInmuebleSaneamientoSeleccionado;
	}

	public void setListaCondicionInmuebleSaneamientoSeleccionado(
			List<Inmueblesaneamiento> listaCondicionInmuebleSaneamientoSeleccionado) {
		this.listaCondicionInmuebleSaneamientoSeleccionado = listaCondicionInmuebleSaneamientoSeleccionado;
	}

	public List<Origendominio> getListaOrigenDominio() {
		return listaOrigenDominio;
	}

	public void setListaOrigenDominio(List<Origendominio> listaOrigenDominio) {
		this.listaOrigenDominio = listaOrigenDominio;
	}


	public String getTipoBusqueda() {
		return tipoBusqueda;
	}


	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}


	public List<String> getListaTipoTitularidad() {
		return listaTipoTitularidad;
	}


	public void setListaTipoTitularidad(List<String> listaTipoTitularidad) {
		this.listaTipoTitularidad = listaTipoTitularidad;
	}


	public String getTtitularidad() {
		return ttitularidad;
	}


	public void setTtitularidad(String ttitularidad) {
		this.ttitularidad = ttitularidad;
	}
   
	public String getClaveInmueble() {
		return claveInmueble;
	}


	public void setClaveInmueble(String claveInmueble) {
		this.claveInmueble = claveInmueble;
	}


	public List<InmuebleBean> getListainmueblematriz() {
		return listainmueblematriz;
	}


	public void setListainmueblematriz(List<InmuebleBean> listainmueblematriz) {
		this.listainmueblematriz = listainmueblematriz;
	}


	public List<String> getSelectedTabTipoTitularidad() {
		return selectedTabTipoTitularidad;
	}


	public void setSelectedTabTipoTitularidad(
			List<String> selectedTabTipoTitularidad) {
		this.selectedTabTipoTitularidad = selectedTabTipoTitularidad;
	}


	public List<Inmueblemaestro> getListaInmuebleMaestro() {
		return listaInmuebleMaestro;
	}


	public void setListaInmuebleMaestro(List<Inmueblemaestro> listaInmuebleMaestro) {
		this.listaInmuebleMaestro = listaInmuebleMaestro;
	}


	public Inmueblemaestro getInmuebleSeleccionado() {
		return inmuebleSeleccionado;
	}


	public void setInmuebleSeleccionado(Inmueblemaestro inmuebleSeleccionado) {
		this.inmuebleSeleccionado = inmuebleSeleccionado;
	}


	public List<String> getListaCondSaneamientoSeleccionado() {
		return listaCondSaneamientoSeleccionado;
	}


	public void setListaCondSaneamientoSeleccionado(
			List<String> listaCondSaneamientoSeleccionado) {
		this.listaCondSaneamientoSeleccionado = listaCondSaneamientoSeleccionado;
	}
    
	
}
