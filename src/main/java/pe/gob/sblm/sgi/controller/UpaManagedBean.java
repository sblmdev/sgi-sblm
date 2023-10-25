package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Origendominio;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Tipozona;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.entity.Ubigeo;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.service.IInmuebleService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Constante;

@ManagedBean(name = "upaMB")
@ViewScoped
public class UpaManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	
	private static final Logger Logger = LoggerFactory.getLogger(UpaManagedBean.class);

	
	@ManagedProperty(value = "#{inmuebleService}")
	private transient IInmuebleService inmuebleService;
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;

	private String depa;
	private String prov;
	private String dist;
	private String codigotitularidad;
	private String codigoinmueble;
	private String codigoupa;
    
	
	private InmuebleBean inmuebleBean;
	private UpaBean upaBean;
	private Upa upa;
	private List<InmuebleBean> listaInmuebleBean;
	private List<Inmueble> listaInmueble;
	private List<UpaBean> listaUpaBean;	
	private List<UpaBean> listaCondicionUpa=  new ArrayList<UpaBean>();
	private List<InmuebleBean> listaInmuebleBeanFilter;
	private List<UpaBean> listaUpaBeanFilter;
	private List<Tipointerior> listaTipoInterior;
//	private List<Tipotitularidad>listaTipoTitularidad;
//	private List<Materialpredominante> listaMaterialPredominante;
	private List<Uso> listaTipoUso;
	private List<Tipovia> listaTipoVia= new ArrayList<Tipovia>();
	private List<Tipozona> listaTipoZona= new ArrayList<Tipozona>();	

	private List<Upa> listaUpa= new ArrayList<Upa>();
	private List<Upa> listaUpaFilter;

	private String tipoBusquedaInmueble=Constantes.BUSQUEDA_DIRECCION;
	private String tipoBusquedaUpa=Constantes.BUSQUEDA_CLAVE;
	private Boolean siprocesojudicial=true;
	/**Mapa**/
	private MapModel simpleModel;
	

	/**Busqueda**/
	private String valorbusquedaInmueble;
	private String valorbusquedaUpa;
    //
	private CondicionBean condicionSeleccionado;
	private List<String> listaIdAgregarUpa;
	private List<Upa> listaUpasActivas = new ArrayList<Upa>();
	private Upa upaSeleccionada ;
	private CondicionBean claveSeleccionada = new CondicionBean();
	private List<Upa> filtrolistaupa;
	private UpaBean upabean;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init(){
		try {
			listaTipoInterior=upaService.listarTipoInterior();
			listaTipoUso=upaService.listarTipoUso();
			listaTipoVia=inmuebleService.listarTipoVia();	
			listaTipoZona=inmuebleService.listarTipoZona();
			
			inmuebleBean= new InmuebleBean();
			upaBean= new UpaBean();
			upa= new Upa();
			listaInmuebleBean= new ArrayList<InmuebleBean>();
			listaUpaBean= new ArrayList<UpaBean>();
			listaInmueble= new ArrayList<Inmueble>();
			obtenerUbicacionGmap();
			inicializarUpaActivos();
		}catch(Exception e){
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	
	
	public void buscarInmueble(){
		
		listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusquedaInmueble,tipoBusquedaInmueble);
		
	}
	public void validarInmueble(String clave){
		if(listaInmueble.size()==0){
			System.out.println();
			grabarInmueble();
			buscarInmueble(clave);
		}
	}
	public  void buscarInmueble(String clave){
		listaInmueble= inmuebleService.buscarInmueble(clave);

	}
	public void buscarUpa(){

		listaUpaBean=upaService.listarUpasBean(valorbusquedaUpa,tipoBusquedaUpa);
		listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusquedaUpa.substring(0,7),tipoBusquedaUpa);
	
	}
	public void buscarListaUpa(){
        Upa upa = new Upa();
        System.out.println(siprocesojudicial);
        	upa.setSiprocesojudicial(siprocesojudicial);
        
		listaUpaBean=upaService.listarUpasBean(valorbusquedaUpa,tipoBusquedaUpa,upa);
		//listaInmuebleBean=inmuebleService.buscarInmuebles(valorbusquedaUpa.substring(0,7),tipoBusquedaUpa);
	
	}

	public void seleccionarUpa(){
	try{
		upa=upaService.obtenerUpaPorClave(upaBean.getClave());
		inmuebleBean=listaInmuebleBean.get(0);
		setearInmuebleSeleccionado();
		//inmuebleBean.setNombretipovia(upa.getDireccion());
		//inmuebleBean.setDireccionprincipal(upa.getNumprincipal());
		codigoupa=upa.getClave();
		listaTipoInterior=upaService.listarTipoInterior();
		listaTipoUso=upaService.listarTipoUso();
		listaTipoVia=inmuebleService.listarTipoVia();	
		listaTipoZona=inmuebleService.listarTipoZona();
		addWarnMessage("","Selección exitosa .");
	}catch(Exception e){
		System.out.println("error");
		e.printStackTrace();
		addWarnMessage("","Error.");
		
	}
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	public void validarUpa()throws Exception{
		try{
			if(Integer.parseInt(codigotitularidad)>0 && Integer.parseInt(codigotitularidad)<=99)
			{
					if(Integer.parseInt(codigoinmueble)>0 && Integer.parseInt(codigoinmueble)<=9999){
						if(Integer.parseInt(codigoupa)>0 && Integer.parseInt(codigoupa)<=999){
							codigoupa=codigotitularidad+'-'+codigoinmueble+'-'+codigoupa;
							System.out.println("CODIGOUPA="+codigoupa);
							addWarnMessage("","Exitoso.");
						}
					}
					
					
			}
			}catch(Exception e){
					System.out.println("error");
					addWarnMessage("","Exitoso.");
				}		
	}
	public void verificarExistenciaInmueble(){
		 List<InmuebleBean> listaInmuebleBean=inmuebleService.buscarInmuebles(upaBean.getCodigoinmueble(),Constantes.BUSQUEDA_CLAVE);
		 System.out.println("paso ");
	}
	public void validarUpdateUpa()throws Exception{
		if (upa.getTipointerior().equals("")) {
			addWarnMessage("",
					"Seleccione el tipo de interior para continuar.");

		} else if (upa.getNombrenuminterior().equals("")) {
			addWarnMessage("",
					"Ingrese número de interior para continuar.");

		} else if (upa.getTugurizadoruinoso().equals("")) {
			addWarnMessage("", "Seleccione el estado para continuar.");

		} else if (upa.getUso().equals("")) {
			addWarnMessage("",
					"Seleccione el tipo de uso para continuar.");

		} else {
			RequestContext requestContext = RequestContext
					.getCurrentInstance();
			requestContext.execute("dlgRegistrarUpa.show();");
		}
	}
	public void validarGrabarUpa()throws Exception{
		 String codupa=codigoupa;
		 if(codupa.equals("")||isNumeric(codupa.substring(0,2))==false || isNumeric(codupa.substring(3,7))==false || isNumeric(codupa.substring(8,11))==false){
			 addWarnMessage("","Ingrese codigo upa.");
		 }else{
			 codigotitularidad=codupa.substring(0,2);
			 codigoinmueble=codupa.substring(3,7);
			 codigoupa=codupa.substring(8,11);
			 if(inmuebleBean.getCodigoinmueble()!=null){
				 if(!inmuebleBean.getCodigoinmueble().equals(codigoinmueble)){
					 inmuebleBean= new InmuebleBean();
				 }
			 }
		 if (codigotitularidad.equals("")||codigoinmueble.equals("")||codigoupa.equals("")) {
			addWarnMessage("","Ingrese la clave de upa para continuar.");
		
		}else 
			if (upaService.validarClaveRepetido(codigotitularidad+"-"+codigoinmueble+"-"+codigoupa)!=null) {
				limpiarformulario();
			addWarnMessage("","Clave ingresada ya se encuentra registrada. Ingrese una clave distinta para continuar.");
			
		}else 
			if (inmuebleBean.getIdinmueble()==0) {
				//busca el inmueble
				valorbusquedaInmueble=codupa.substring(0,7);
				System.out.println("valor de busqueda="+valorbusquedaInmueble);
				tipoBusquedaInmueble="clave";
				buscarInmueble();
				if(listaInmuebleBean.size()==0){
					//limpiarformulario();
					addWarnMessage("","El inmueble no se encuentra registrado");
					
				}else{
					inmuebleBean=listaInmuebleBean.get(0);
					upa.setDireccion(inmuebleBean.getNombretipovia());
					upa.setNumprincipal(inmuebleBean.getDireccionprincipal());
					setearInmuebleSeleccionado();
					addWarnMessage("","Ingrese informacion de UPA para continuar.");}
		
		}else 
			if(upa!=null){
			if (upa.getTipointerior().equals("")) {
			addWarnMessage("","Seleccione el tipo de interior para continuar.");
		
		}else if (upa.getNombrenuminterior().equals("")) {
			addWarnMessage("","Ingrese número de interior para continuar.");
		
		}
		else if (upa.getTugurizadoruinoso().equals("")){
			addWarnMessage("","Seleccione el estado para continuar.");

		}
		else if (  upa.getUso().equals("")){
			addWarnMessage("","Seleccione el tipo de uso para continuar.");

		}
		else{
			RequestContext requestContext= RequestContext.getCurrentInstance();  
			requestContext.execute("dlgRegistrarUpa.show();");
		}
			}else{
				addWarnMessage("","Ingrese informacion de UPA para continuar.");
			}
		 }
		

	}
	
	
	
	public void grabarUpa() {
		
		Upa upan= new Upa();

		if (upaBean.getIdupa()!=0) {/**Actualizar*/
			upan = upaService.obtenerUpaxClave(codigoupa);
			//upan.setIdupa(upa.getIdupa());
			upan.setFecmod(new Date());
			upan.setUsrmod(userMB.getNombreusr().toUpperCase());
			
		}else {/**Nuevo*/
			
			upan.setFeccre(new Date());
			upan.setUsrcre(userMB.getNombreusr().toUpperCase());
			//upan.setIdinmueblemaestro(inmuebleBean.getIdinmueble());
			Inmueblemaestro inm= new Inmueblemaestro();
			inm.setIdinmueble(inmuebleBean.getIdinmueble());
			upan.setInmueblemaestro(inm);
			
		}
	   	    
		upan.setTugurizadoruinoso(upa.getTugurizadoruinoso());
		upan.setNombrenuminterior(upa.getNombrenuminterior());
		upan.setTipointerior(upa.getTipointerior());
		upan.setUso(upa.getUso());
		upan.setNumprincipal(upa.getNumprincipal());
		upan.setClave(codigoupa);
		upan.setDireccion(upa.getDireccion());
		upan.setAntiguedad(Constantes.NO_SELECCIONADO);
		upan.setConservacion(Constantes.NO_SELECCIONADO);
		upan.setMep(Constantes.NO_SELECCIONADO);
		upan.setValuacion(Constantes.NO_SELECCIONADO);
		upan.setSicopropiedad(upa.getSicopropiedad());
		upan.setObservacioncopropiedad(upa.getSicopropiedad()==true? upa.getObservacioncopropiedad():null);
		upan.setSiprocesojudicial(false);
		upan.setEstado(Constante.ESTADO_A);
		upan.setSiactualizaclave(upa.getSiactualizaclave());
		System.out.println("usuario="+userMB.getNombrecompleto());
		if(upa.getSiactualizaclave()){
			Upa upaAnterior= new Upa();
			if(listaCondicionUpa!=null){
				upaAnterior.setIdupa(listaCondicionUpa.get(0).getIdupa());
				upan.setIdupa_anterior(upaAnterior);
				upan.setObs_mod(upa.getObs_mod());
				upan.setSiactualizaclave(upa.getSiactualizaclave());
			}
			
			
		}


			//BUSCAR EN TABLA INMUEBLE
			buscarInmueble(upan.getClave().substring(0, 7));
			validarInmueble(upan.getClave().substring(0, 7));
			for (int i=0;i<listaInmueble.size();i++) {
				    if(listaInmueble.get(i).getClave().substring(8, 11).equalsIgnoreCase("000"))
				    {
				    Inmueble in=new Inmueble();
				    in.setIdinmueble(listaInmueble.get(i).getIdinmueble());
				    upan.setInmueble(in);
				    }
				 }

			
	   //MODIFICACION ID INMUEBLE CON ID INMUEBLE MATRIZ
//		Inmueble in=new Inmueble();
//		in.setIdinmueble(inmuebleBean.getIdinmueble());
//		upan.setInmueble(in);
		
		upaService.registrarUpa(upan);/**Graba o actualiza*/
		if(upan.getSiactualizaclave()){
			upaService.actualizarUpa(upan);
		}
		
		addInfoMessage("", "Se grabo la información de clave UPA correctamente. ");
	
		limpiarformulario();	
	}

	public void grabarInmueble(){
		Inmueble inmueble= new Inmueble();
		Ubigeo ubigeo= new Ubigeo();
		ubigeo.setUbigeo(inmuebleBean.getUbigeo());
		
		Tipovia tipovia= new Tipovia();
		tipovia.setIdtipovia(inmuebleBean.getIdtipovia());
		Tipozona tipozona=new Tipozona();
		tipozona.setIdtipozona(inmuebleBean.getIdtipozona());	
		Titularidad titularidad= new Titularidad();
		titularidad.setIdtitularidad(1);	
		inmueble.setUbigeo(ubigeo);
		inmueble.setTipovia(tipovia);	
		inmueble.setTitularidad(titularidad);
		inmueble.setClave(inmuebleBean.getClave().substring(0, 11));
		//inmueble.setCodigoinmueble(inmuebleBean.getCodigoinmueble());
		System.out.println("Tipo titularidad: "+titularidad);
		System.out.println("clave: "+inmueble.getClave());
		System.out.println("codigo de inmueble: "+inmuebleBean.getCodigoinmueble());
		inmueble.setNumregistrosbn(inmuebleBean.getNumregistrosbn());
		inmueble.setDireccion(inmuebleBean.getNombretipovia().trim());
		inmueble.setNumeroprincipal(inmuebleBean.getDireccionprincipal().trim());

		/**Localizacion**/
		inmueble.setLongitudlocalizacion(inmuebleBean.getLongitudlocalizacion());
		inmueble.setLatitudlocalizacion(inmuebleBean.getLatitudlocalizacion());
		inmueble.setDescripcionlocalizacion(inmuebleBean.getDescripcionlocalizacion());
		
		
//		if (inmuebleBean.getIdinmueble()!=0) {/**Actualizar*/
//			inmueble.setIdinmueble(inmuebleBean.getIdinmueble());
//			
//			inmueble.setFechcre(inmuebleBean.getFeccre());
//			inmueble.setUsrcre(inmuebleBean.getUsrcre());
//			inmueble.setFechmod(new Date());
//			inmueble.setUsrmod(userMB.getNombrecompleto());
//			inmueble.setInmueblesaneamientos(obtenerEstadoCondicionesSaneamientoInmueble(inmueble,"update"));
//			
//		}else {/**Nuevo*/
			
//			inmueble.setFechcre(new Date());
//			inmueble.setUsrcre(userMB.getNombrecompleto());
			inmueble.setFechcre(inmuebleBean.getFeccre());
			inmueble.setUsrcre(inmuebleBean.getUsrcre());
			//inmueble.setInmueblesaneamientos(obtenerEstadoCondicionesSaneamientoInmueble(inmueble,"insert"));
//		}
//		
		
		//getInmuebleService().grabarInmueble(inmueble);/**Graba o actualiza*/
		upaService.grabarInmueble(inmueble);/**Graba o actualiza*/
		//addInfoMessage("Se grabo con exito", "Se grabo la información del inmueble correctamente. ");
		//limpiarformulario();	
	}

	
	public void validarEliminarInmueble(){
		
		if (inmuebleBean.getIdinmueble()!=0) {
			addErrorMessage("", "Contactase con el administrador para poder eliminar.");
			
		} else {
			
			addWarnMessage("", "Para proceder a eliminar inmueble es obligatorio seleccionar un inmueble");
			limpiarformulario();
		}
	}
	
	
	public void validarEliminarUPA(){
		
		if (inmuebleBean.getIdinmueble()!=0) {
			addErrorMessage("", "Contactase con el administrador para poder eliminar.");
			
		} else {
			
			addWarnMessage("", "Para proceder a eliminar inmueble es obligatorio seleccionar un inmueble");
			limpiarformulario();
		}
	}
	public void limpiarformulario(){
		codigoupa="";
		depa="";
		prov="";
		dist="";
		
		inmuebleBean= new InmuebleBean();
		upaBean=new UpaBean();
		upa= new Upa();
		listaUpaBean.clear();
		listaCondicionUpa.clear();
		valorbusquedaUpa="";
		init();
	}
	
	

	public void setearInmuebleSeleccionado() throws Exception{
			depa=inmuebleBean.getDepartamento();
			prov=inmuebleBean.getProvincia();
			dist=inmuebleBean.getDistrito();
			
			cargarUbigeo();
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
	public void inicializarUpaActivos() {
		RequestContext contextRequest = RequestContext.getCurrentInstance();
			listaUpasActivas = upaService.listarUpasActivas();


	}
	public void cargarUpa(){
		System.out.println("entro");
		valorbusquedaUpa="";
		if(upaSeleccionada!=null){
		listaCondicionUpa= upaService.listarUpasBean(upaSeleccionada.getClave(),tipoBusquedaUpa);
		System.out.println("clave="+listaCondicionUpa.get(0).getClave());
		}else{
			listaCondicionUpa.clear();
		}
	}
	public void limpiarTablaUpa(){
		listaCondicionUpa.clear();
	}
	
	public void cargarUbigeo() {
		inmuebleBean.setUbigeo(getInmuebleService().obtenerIdUbigeo(depa, prov,dist));
	}
	public IInmuebleService getInmuebleService() {
		return inmuebleService;
	}
	public void setInmuebleService(IInmuebleService inmuebleService) {
		this.inmuebleService = inmuebleService;
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
	public String getTipoBusquedaInmueble() {
		return tipoBusquedaInmueble;
	}
	public void setTipoBusquedaInmueble(String tipoBusquedaInmueble) {
		this.tipoBusquedaInmueble = tipoBusquedaInmueble;
	}	public IUpaService getUpaService() {
		return upaService;
	}
	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public List<Tipointerior> getListaTipoInterior() {
		return listaTipoInterior;
	}
	public void setListaTipoInterior(List<Tipointerior> listaTipoInterior) {
		this.listaTipoInterior = listaTipoInterior;
	}
	

	public List<Uso> getListaTipoUso() {
		return listaTipoUso;
	}
	public void setListaTipoUso(List<Uso> listaTipoUso) {
		this.listaTipoUso = listaTipoUso;

	}
	public String getTipoBusquedaUpa() {
		return tipoBusquedaUpa;
	}
	public void setTipoBusquedaUpa(String tipoBusquedaUpa) {
		this.tipoBusquedaUpa = tipoBusquedaUpa;
	}
	public List<Tipovia> getListaTipoVia() {
		return listaTipoVia;
	}
	public void setListaTipoVia(List<Tipovia> listaTipoVia) {
		this.listaTipoVia = listaTipoVia;
	}
	public List<Tipozona> getListaTipoZona() {
		return listaTipoZona;
	}
	public void setListaTipoZona(List<Tipozona> listaTipoZona) {
		this.listaTipoZona = listaTipoZona;
	}
	public String getValorbusquedaInmueble() {
		return valorbusquedaInmueble;
	}
	public void setValorbusquedaInmueble(String valorbusquedaInmueble) {
		this.valorbusquedaInmueble = valorbusquedaInmueble;
	}
	public String getValorbusquedaUpa() {
		return valorbusquedaUpa;
	}
	public void setValorbusquedaUpa(String valorbusquedaUpa) {
		this.valorbusquedaUpa = valorbusquedaUpa;
	}
	public Upa getUpa() {
		return upa;
	}
	public void setUpa(Upa upa) {
		this.upa = upa;
	}
	public List<Upa> getListaUpa() {
		return listaUpa;
	}
	public void setListaUpa(List<Upa> listaUpa) {
		this.listaUpa = listaUpa;
	}
	public List<Upa> getListaUpaFilter() {
		return listaUpaFilter;
	}
	public void setListaUpaFilter(List<Upa> listaUpaFilter) {
		this.listaUpaFilter = listaUpaFilter;
	}
	public String getCodigotitularidad() {
		return codigotitularidad;
	}
	public void setCodigotitularidad(String codigotitularidad) {
		this.codigotitularidad = codigotitularidad;
	}
	public String getCodigoinmueble() {
		return codigoinmueble;
	}
	public void setCodigoinmueble(String codigoinmueble) {
		this.codigoinmueble = codigoinmueble;
	}
	public String getCodigoupa() {
		return codigoupa;
	}
	public void setCodigoupa(String codigoupa) {
		this.codigoupa = codigoupa;
	}
	public UpaBean getUpaBean() {
		return upaBean;
	}
	public void setUpaBean(UpaBean upaBean) {
		this.upaBean = upaBean;
	}
	public List<UpaBean> getListaUpaBean() {
		return listaUpaBean;
	}
	public void setListaUpaBean(List<UpaBean> listaUpaBean) {
		this.listaUpaBean = listaUpaBean;
	}
	public List<UpaBean> getListaUpaBeanFilter() {
		return listaUpaBeanFilter;
	}
	public void setListaUpaBeanFilter(List<UpaBean> listaUpaBeanFilter) {
		this.listaUpaBeanFilter = listaUpaBeanFilter;
	}

	public List<Inmueble> getListaInmueble() {
		return listaInmueble;
	}

	public void setListaInmueble(List<Inmueble> listaInmueble) {
		this.listaInmueble = listaInmueble;
	}
	public MapModel getSimpleModel() {
		return simpleModel;
	}
	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public List<String> getListaIdAgregarUpa() {
		return listaIdAgregarUpa;
	}

	public void setListaIdAgregarUpa(List<String> listaIdAgregarUpa) {
		this.listaIdAgregarUpa = listaIdAgregarUpa;
	}

	public List<Upa> getListaUpasActivas() {
		return listaUpasActivas;
	}

	public void setListaUpasActivas(List<Upa> listaUpasActivas) {
		this.listaUpasActivas = listaUpasActivas;
	}

	public Upa getUpaSeleccionada() {
		return upaSeleccionada;
	}

	public void setUpaSeleccionada(Upa upaSeleccionada) {
		this.upaSeleccionada = upaSeleccionada;
	}

	public CondicionBean getClaveSeleccionada() {
		return claveSeleccionada;
	}

	public void setClaveSeleccionada(CondicionBean claveSeleccionada) {
		this.claveSeleccionada = claveSeleccionada;
	}

	public List<UpaBean> getListaCondicionUpa() {
		return listaCondicionUpa;
	}

	public void setListaCondicionUpa(List<UpaBean> listaCondicionUpa) {
		this.listaCondicionUpa = listaCondicionUpa;
	}

	public CondicionBean getCondicionSeleccionado() {
		return condicionSeleccionado;
	}

	public void setCondicionSeleccionado(CondicionBean condicionSeleccionado) {
		this.condicionSeleccionado = condicionSeleccionado;
	}


	public List<Upa> getFiltrolistaupa() {
		return filtrolistaupa;
	}


	public void setFiltrolistaupa(List<Upa> filtrolistaupa) {
		this.filtrolistaupa = filtrolistaupa;
	}


	public UpaBean getUpabean() {
		return upabean;
	}


	public void setUpabean(UpaBean upabean) {
		this.upabean = upabean;
	}


	public boolean getSiprocesojudicial() {
		return siprocesojudicial;
	}


	public void setSiprocesojudicial(boolean siprocesojudicial) {
		this.siprocesojudicial = siprocesojudicial;
	}


	
   
	
	
}
