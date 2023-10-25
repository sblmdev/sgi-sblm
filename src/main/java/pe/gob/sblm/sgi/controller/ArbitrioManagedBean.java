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
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.ConvertirUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.ArbitrioService;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

@ManagedBean(name = "arbMB")
@ViewScoped
public class ArbitrioManagedBean extends BaseController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger Logger = LoggerFactory.getLogger(ArbitrioManagedBean.class);
	
	public ArbitrioBean arbitrioBean;
	
	public List<CuotaArbitrioBean> listaCuotaArbitrioBean = new ArrayList<CuotaArbitrioBean>();
	public CuotaArbitrioBean selectedCuota = new CuotaArbitrioBean();
	
	private int tipobusqueda,tipobusquedaUpa;
	private Upa upa;
	private List<Upa> listUpa, listUpaFiltro;
	private List<ArbitrioBean> listaArbitrio,listArbitrioFiltro;
	private String valorbusqueda,valorbusquedaUpa;
	private Double montocuota=0.0;
	private boolean siperiodos=false, sicuotas=false,siexisteregistro=false;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{archivoMB}")
	ArchivoManagedBean archivoMB;
	
	@ManagedProperty(value = "#{arbitrioService}")
	private transient ArbitrioService arbitrioService;
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	
	public ArbitrioManagedBean() {
		// TODO Auto-generated constructor stub
	}
	@PostConstruct
	public void init(){
		arbitrioBean= new ArbitrioBean();
		tipobusqueda=1;
		tipobusquedaUpa=1;
		listaCuotaArbitrioBean=new ArrayList<CuotaArbitrioBean>();
	}
	
	public void verificarExistenciaClaveUpa(){
		
	}
	public void verificarExistenciaClaveUpaArbitrio(){
		
	}
	public boolean verificarExistenciaClaveUpaPeriodoArbitrio(ArbitrioBean arbitrio){
		return arbitrioService.verificarExistenciaClaveUpaPeriodoArbitrio(arbitrio.getClave(), arbitrio.getPeriodo().toString());
	}
	public void validarDatosPeriodo(ArbitrioBean arbitrio){
		if(arbitrio.getPeriodo()<1990 || arbitrio.getPeriodo()>2050){
			addInfoMessage("", "Ingrese un periodo valido ");
			siexisteregistro=false;
			arbitrioBean.setPeriodo(null);
		}
		else if(verificarExistenciaClaveUpaPeriodoArbitrio(arbitrio)){
			siexisteregistro=true;
			arbitrioBean.setPeriodo(null);
			addInfoMessage("", "El periodo "+arbitrio.getPeriodo()+" se encuentra registrado !!");
		}else{
			addInfoMessage("", "Ingrese los trimestres del periodo");
			siexisteregistro=false;
		}
	}
	
	public void validarPeriodosArbitrios(){
		if(arbitrioBean.getIdarbitrio()==0){
			listaCuotaArbitrioBean=calcularCuotasArbitrios(arbitrioBean);
		}else if(arbitrioBean.getIdarbitrio()!=0){
			List<CuotaArbitrioBean> lista = new ArrayList<CuotaArbitrioBean>();
			lista=calcularCuotasArbitrios(arbitrioBean);
			
		    Iterator<CuotaArbitrioBean> iter = listaCuotaArbitrioBean.iterator();
		    while(iter.hasNext()){
		    	CuotaArbitrioBean cuotaBean= iter.next();
				for(CuotaArbitrioBean cuota :lista){
					
					if(cuotaBean.getMes().equalsIgnoreCase(cuota.getMes())){
						cuotaBean.setMonto(cuota.getMonto());
						break;
					}
					
				}	
		    }

		}
		
		addInfoMessage("", "Generación de Cuotas de Arbitrio exitoso");
	}
	public List<CuotaArbitrioBean> calcularCuotasArbitrios(ArbitrioBean a){
		List<CuotaArbitrioBean> listaCuota = new ArrayList<CuotaArbitrioBean>();
		//trim 1
		listaCuota=asignarCuota(listaCuota,a.getTrim1(),a.getPeriodo(),1);
		//trim 2
		listaCuota=asignarCuota(listaCuota,a.getTrim2(),a.getPeriodo(),2);
		//trim 3
		listaCuota=asignarCuota(listaCuota,a.getTrim3(),a.getPeriodo(),3);
		//trim 4
		listaCuota=asignarCuota(listaCuota,a.getTrim4(),a.getPeriodo(),4);
		montocuota=0.0;
		for(CuotaArbitrioBean cuota: listaCuota){
			montocuota=FuncionesHelper.sumarDouble(montocuota,cuota.getMonto());
		}
		if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), montocuota)==0){
			sicuotas=true;
		}else{
			sicuotas=false;
		}
		
		return listaCuota;
	}
	public List<CuotaArbitrioBean> asignarCuota(List<CuotaArbitrioBean> lista,Double monto,Integer periodo,Integer trimestre){
		Double monto1,monto2,monto3;
		Integer n;
		if(lista.size()==0){
			n=0;
		}else{
			n=lista.size();
		}

		CuotaArbitrioBean cuota;
		List<Double> listamonto= new ArrayList<Double>();
		monto1=FuncionesHelper.redondearNum(monto/3, 2) ;listamonto.add(monto1);
		monto2=FuncionesHelper.redondearNum(monto/3, 2) ; listamonto.add(monto2);
		monto3=FuncionesHelper.redondearNum(monto-(monto1+monto2),2);listamonto.add(monto3);
        int i=0;
		while(i<3){	
			cuota= new CuotaArbitrioBean();
			cuota.setMes(Almanaque.obtenerNombreMes(n));
			cuota.setPeriodo(periodo);
			cuota.setMonto(listamonto.get(i));
			cuota.setTrimestre(trimestre);
			cuota.setNromes(n+1);
			cuota.setSecuencia(n+1);
			cuota.setNropagos(0);
			cuota.setEstado(Constante.ESTADO_PENDIENTE);
			cuota.setCancelado("0");
			//cuota.setUsrcre(userMB.getNombreusr());
			cuota.setFeccre(new Date());
			
			lista.add(cuota);
			n++;
			i++;
		}
		return lista;
		
		
	}
	public void validarGrabarArbitrio(){
		 
		if(arbitrioBean.getClave()==null){
			addInfoMessage("", "Debe ingresar la clave upa ");
		}else if(arbitrioBean.getDireccion().trim().length()<5){
			addInfoMessage("", "Debe ingresar la dirección !!");
		}else if(verificarExistenciaClaveUpaPeriodoArbitrio(arbitrioBean)){
			addInfoMessage("", "El periodo "+arbitrioBean.getPeriodo()+" se encuentra registrado !!");
		}else if((!siexisteregistro && arbitrioBean.getPeriodo()==0 )|| (!siexisteregistro && (arbitrioBean.getPeriodo()<1990 || arbitrioBean.getPeriodo()>2050))){
			addInfoMessage("", "Ingrese y valide el periodo !!");
		}else if(!siperiodos){
			addInfoMessage("", "Debe validar los trimestres !! ");
		}else if(!sicuotas){
			addInfoMessage("", "Debe generar las cuotas ");
		}else if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), getTotalArbitrio())!=0){
				addInfoMessage("", "Valide los trimestres para continuar ");
		}else if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), sumarCuotasArbitrios())!=0){
			addInfoMessage("", "El total de arbitrios es diferente a total del monto de cuotas , seleccione el botón generar cuotas para continuar");
		}else if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), montocuota)!=0){
			addInfoMessage("", "error en el total");
		}else if(arbitrioBean.getTotal()<=0.0){
			addInfoMessage("", "El monto total de arbitrio no puede ser cero ");
		}else if(verificarExistenciaClaveUpaPeriodoArbitrio(arbitrioBean)){
			addInfoMessage("", "El periodo "+arbitrioBean.getPeriodo()+" se encuentra registrado !!");
		}else{
		
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarArbitrio.show();");
		}
		
		
	}
	public Double sumarCuotasArbitrios(){
		Double monto=0.0;
		for(CuotaArbitrioBean  cuota : listaCuotaArbitrioBean){
			monto=FuncionesHelper.sumarDouble(monto, cuota.getMonto()) ;
		}
		return monto;
	}
	public Double getTotalArbitrio(){
		Double total=0.0;
		total=FuncionesHelper.sumarDouble(total,arbitrioBean.getTrim1());
		total=FuncionesHelper.sumarDouble(total,arbitrioBean.getTrim2());
		total=FuncionesHelper.sumarDouble(total,arbitrioBean.getTrim3());
		total=FuncionesHelper.sumarDouble(total,arbitrioBean.getTrim4());
		return total;
	}
	public void validarActualizarArbitrio(){

		if(arbitrioBean.getClave()==null){
			addInfoMessage("", "Debe ingresar la clave upa ");
		}else if(arbitrioService.verificarExistenciaPagosCuotaArbitrio(arbitrioBean.getIdarbitrio())){
			addInfoMessage("", "No se puede actualizar el arbitrio debido a que existe pagos correspondiente a este periodo. ");
		}else if(arbitrioBean.getPeriodo()==0){
			addInfoMessage("", "Debe ingresar el periodo ");
		}else if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), getTotalArbitrio())!=0){
			addInfoMessage("", "Valide los trimestres para continuar ");
		}else if(!sicuotas){
			addInfoMessage("", "Debe generar las cuotas ");
		}else if(FuncionesHelper.restarDouble(arbitrioBean.getTotal(), sumarCuotasArbitrios())!=0){
			addInfoMessage("", "El total de arbitrios es diferente a total del monto de cuotas , seleccione el botón generar cuotas para continuar");
		}else if(arbitrioBean.getTotal()<=0.0){
			addInfoMessage("", "El monto total de arbitrio no puede ser cero ");
		}else if(arbitrioBean.getMotivomod().trim().length()<10){
			addInfoMessage("", "Ingrese el motivo de modificación (más de 10 caracteres)");
		}else{
		
			RequestContext context = RequestContext.getCurrentInstance();  
			context.execute("dlgRegistrarArbitrio.show();");
		}
		
		
	}
	public void grabarArbitrio(){
		try{
			
			Arbitrio arbitrio = new Arbitrio();
			if(arbitrioBean.getIdarbitrio()!=0){
				arbitrio=arbitrioService.obtenerArbitrio(arbitrioBean.getIdarbitrio());
				arbitrio.setUsrmod(userMB.getNombreusr().toUpperCase());
				arbitrio.setFecmod(new Date());
				arbitrio.setMotivomod(arbitrioBean.getMotivomod().toUpperCase());
				arbitrio.setTrim1(arbitrioBean.getTrim1());
				arbitrio.setTrim2(arbitrioBean.getTrim2());
				arbitrio.setTrim3(arbitrioBean.getTrim3());
				arbitrio.setTrim4(arbitrioBean.getTrim4());
				arbitrio.setTotal(arbitrioBean.getTotal());
				arbitrio.setHost_ip_usrmod(CommonsUtilities.getRemoteIpAddress());
				Set<Cuota_arbitrio> cuotasArbitrio= new HashSet<Cuota_arbitrio>();
				for(CuotaArbitrioBean  cuota : listaCuotaArbitrioBean){
					Cuota_arbitrio cuo= new Cuota_arbitrio();
					cuo= arbitrioService.obtenerCuotaArbitrioxId(cuota.getIdcuota());
					cuo.setArbitrio(arbitrio);
					cuo.setMonto(cuota.getMonto());
					cuo.setCancelado("0");
					cuo.setEstado(cuo.getMonto()==0? Constante.ESTADO_CANCELADO: Constante.ESTADO_PENDIENTE);
					cuo.setUsrmod(userMB.getNombreusr().toUpperCase());
					cuo.setFecmod(new Date());
					cuotasArbitrio.add(cuo);
					
				}
				arbitrio.setCuotasArbitrio(cuotasArbitrio);
			}else{
				arbitrio.setUpa(upa);
				arbitrio.setSifinalizado(false);
				arbitrio.setEstado(Constante.ESTADO_PENDIENTE);
				arbitrio.setPeriodo(arbitrioBean.getPeriodo());
				arbitrio.setTrim1(arbitrioBean.getTrim1());
				arbitrio.setTrim2(arbitrioBean.getTrim2());
				arbitrio.setTrim3(arbitrioBean.getTrim3());
				arbitrio.setTrim4(arbitrioBean.getTrim4());
				arbitrio.setTotal(arbitrioBean.getTotal());
				arbitrio.setDireccion(arbitrioBean.getDireccion());
				arbitrio.setUsrcre(userMB.getNombreusr().toUpperCase());
				arbitrio.setFeccre(new Date());
				arbitrio.setObservacion(arbitrioBean.getObservacion());
				arbitrio.setHost_ip_usrcre(CommonsUtilities.getRemoteIpAddress());
				Set<Cuota_arbitrio> cuotasArbitrio= new HashSet<Cuota_arbitrio>();
				for(CuotaArbitrioBean  cuota : listaCuotaArbitrioBean){
					Cuota_arbitrio cuo= new Cuota_arbitrio();
					cuo.setArbitrio(arbitrio);
					cuo.setPeriodo(cuota.getPeriodo());
					cuo.setTrimestre(cuota.getTrimestre());
					cuo.setMes(cuota.getMes());
					cuo.setNromes(cuota.getNromes());
					cuo.setMonto(cuota.getMonto());
					cuo.setNropagos(0);
					cuo.setCancelado("0");
					cuo.setEstado(cuo.getMonto()==0? Constante.ESTADO_CANCELADO: Constante.ESTADO_PENDIENTE);
					cuo.setUsrcre(userMB.getNombreusr().toUpperCase());
					cuo.setFeccre(new Date());
					cuotasArbitrio.add(cuo);
					
				}
				arbitrio.setCuotasArbitrio(cuotasArbitrio);
			}
			
			arbitrioService.saveUpdateArbitrio(arbitrio);
			
			addInfoMessage("",arbitrioBean.getIdarbitrio()!=0?"Se actualizo la deuda de arbitrio con éxito" :"Se registro la deuda de arbitrio con éxito");
			limpiarDatosArbitrios();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void limpiarDatosArbitrios(){
		arbitrioBean= new ArbitrioBean();
		listaCuotaArbitrioBean.clear();
		listaArbitrio=new ArrayList<ArbitrioBean>();
		upa= new Upa();
		valorbusquedaUpa="";
		valorbusqueda="";
		siperiodos=false;
		sicuotas=false;
		siexisteregistro=false;
		limpiarTrimestres();
	}

	public void verificarCalculoTotal(ArbitrioBean arbitrio){
		if(arbitrio.getTrim1()==null || arbitrio.getTrim1()<0){
			addInfoMessage("", "Debe ingresar un monto valido en el trimestre 1");
		}else if(arbitrio.getTrim2()==null || arbitrio.getTrim2()<0){
			addInfoMessage("", "Debe ingresar un monto valido en el trimestre 2");
		}else if(arbitrio.getTrim3()==null || arbitrio.getTrim3()<0){
			addInfoMessage("", "Debe ingresar un monto valido en el trimestre 3");
		}else if(arbitrio.getTrim4()==null || arbitrio.getTrim4()<0){
			addInfoMessage("", "Debe ingresar un monto valido en el trimestre 4");
		}else{
			calcularTotal(arbitrio);
		}
	}
	public void calcularTotal(ArbitrioBean arbitrio){
		arbitrioBean.setTotal(totalArbitrio(arbitrio));
		siperiodos=true;
	}
	public Double totalArbitrio(ArbitrioBean arbitrioBean){
		Double total=FuncionesHelper.redondearNum(arbitrioBean.getTrim1()+arbitrioBean.getTrim2()+arbitrioBean.getTrim3()+arbitrioBean.getTrim4(),2);
	    return total;
	}

	public void limpiarTrimestres(){
		arbitrioBean.setTrim1(null);
		arbitrioBean.setTrim2(null);
		arbitrioBean.setTrim3(null);
		arbitrioBean.setTrim4(null);
		siperiodos=false;
		sicuotas=false;
	}

	public void buscarUpa(){
		
		if (tipobusquedaUpa==1) {
			listUpa=getUpaService().listarUpasxClave(valorbusquedaUpa);
			if(listUpa.size()==0){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa no encontrada", "La upa no se encuentra registrado en el sistema");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}else{
				addInfoMessage("", "Upa encontrada ");
			}
		} else {
			listUpa=getUpaService().listarUpasxDireccion(valorbusquedaUpa);
		}
	}
	public void seleccionarUpa(SelectEvent event) {
		

		if(!upa.getSiprocesojudicial()){/*CONDICION DE CLAVE UPA EN PROCESO JUDICIAL*/
//		if (contratoService.verificarExisteCondicionVigente(upa.getIdupa())) {
//			upa = null;
//			
//			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra ocupada", "No puede seleccionar la upa debido a que se encuentra ocupada en una condición vigente");
//			FacesContext.getCurrentInstance().addMessage(null, msg);
//		}else {
//			contratoBean.setIdupa(upa.getIdupa());
//			contratoBean.setClaveUpa(upa.getClave());
////			contratoBean.setUso(upa.getUso());
//			//contratoBean.setDireccion(upa.getInmueble().getDireccion()+ " "+ upa.getInmueble().getNumeroprincipal()+" "+upa.getNombrenuminterior());
//			contratoBean.setDireccion(upa.getDireccion()+ " "+ upa.getNumprincipal()+" "+upa.getNombrenuminterior());
//		}
			arbitrioBean.setClave(upa.getClave());
			arbitrioBean.setDireccion(upa.getDireccion()+" "+upa.getNumprincipal());
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upa se encuentra bloqueada", "La upa seleccionada esta judicializada por lo tanto no se puede realizar ningun tipo de registro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	public void validarPeriodo() {
		
		Integer contador=0;

	}
	public void limpiarFormulario(){
		limpiarDatosArbitrios();
	}
	public void buscarArbitrio(){
		try{
		
			if(tipobusqueda==1){
				listaArbitrio=arbitrioService.buscarArbitrio(valorbusqueda, Integer.toString(tipobusqueda));
			}else if(tipobusqueda==2) {
				listaArbitrio=arbitrioService.buscarArbitrio(valorbusqueda, Integer.toString(tipobusqueda));
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void setearArbitrioSeleccionado(){
		try{
			listaCuotaArbitrioBean.clear();
			listaCuotaArbitrioBean=arbitrioService.obtenerCuotasxArbitrio(arbitrioBean.getIdarbitrio());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public ArbitrioBean getArbitrioBean() {
		return arbitrioBean;
	}

	public void setArbitrioBean(ArbitrioBean arbitrioBean) {
		this.arbitrioBean = arbitrioBean;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public ArchivoManagedBean getArchivoMB() {
		return archivoMB;
	}

	public void setArchivoMB(ArchivoManagedBean archivoMB) {
		this.archivoMB = archivoMB;
	}
    
	public List<CuotaArbitrioBean> getListaCuotaArbitrioBean() {
		return listaCuotaArbitrioBean;
	}

	public void setListaCuotaArbitrioBean(
			List<CuotaArbitrioBean> listaCuotaArbitrioBean) {
		this.listaCuotaArbitrioBean = listaCuotaArbitrioBean;
	}

	public ArbitrioService getArbitrioService() {
		return arbitrioService;
	}

	public void setArbitrioService(ArbitrioService arbitrioService) {
		this.arbitrioService = arbitrioService;
	}
    
	public int getTipobusquedaUpa() {
		return tipobusquedaUpa;
	}

	public void setTipobusquedaUpa(int tipobusquedaUpa) {
		this.tipobusquedaUpa = tipobusquedaUpa;
	}

	public List<Upa> getListUpa() {
		return listUpa;
	}

	public void setListUpa(List<Upa> listUpa) {
		this.listUpa = listUpa;
	}

	public List<Upa> getListUpaFiltro() {
		return listUpaFiltro;
	}

	public void setListUpaFiltro(List<Upa> listUpaFiltro) {
		this.listUpaFiltro = listUpaFiltro;
	}
    
	public IUpaService getUpaService() {
		return upaService;
	}

	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
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
	public CuotaArbitrioBean getSelectedCuota() {
		return selectedCuota;
	}
	public void setSelectedCuota(CuotaArbitrioBean selectedCuota) {
		this.selectedCuota = selectedCuota;
	}
	public Double getMontocuota() {
		return montocuota;
	}
	public void setMontocuota(Double montocuota) {
		this.montocuota = montocuota;
	}
	public boolean isSiperiodos() {
		return siperiodos;
	}
	public void setSiperiodos(boolean siperiodos) {
		this.siperiodos = siperiodos;
	}
	public boolean isSicuotas() {
		return sicuotas;
	}
	public void setSicuotas(boolean sicuotas) {
		this.sicuotas = sicuotas;
	}
	public boolean isSiexisteregistro() {
		return siexisteregistro;
	}
	public void setSiexisteregistro(boolean siexisteregistro) {
		this.siexisteregistro = siexisteregistro;
	}
	public int getTipobusqueda() {
		return tipobusqueda;
	}
	public void setTipobusqueda(int tipobusqueda) {
		this.tipobusqueda = tipobusqueda;
	}
	public List<ArbitrioBean> getListaArbitrio() {
		return listaArbitrio;
	}
	public void setListaArbitrio(List<ArbitrioBean> listaArbitrio) {
		this.listaArbitrio = listaArbitrio;
	}
	public List<ArbitrioBean> getListArbitrioFiltro() {
		return listArbitrioFiltro;
	}
	public void setListArbitrioFiltro(List<ArbitrioBean> listArbitrioFiltro) {
		this.listArbitrioFiltro = listArbitrioFiltro;
	}
	public String getValorbusqueda() {
		return valorbusqueda;
	}
	public void setValorbusqueda(String valorbusqueda) {
		this.valorbusqueda = valorbusqueda;
	}


    
	
}
