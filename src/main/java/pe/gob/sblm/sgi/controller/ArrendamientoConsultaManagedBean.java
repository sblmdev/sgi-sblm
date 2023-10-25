package pe.gob.sblm.sgi.controller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.dao.IContratoDAO;
import pe.gob.sblm.sgi.entity.Adenda;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IInquilinoService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.ITipoCambioService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;



@ManagedBean(name = "arrendamientoconsultaMB")	
@ViewScoped
public class ArrendamientoConsultaManagedBean extends BaseController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	
	@ManagedProperty(value = "#{inquilinoService}")
	private transient IInquilinoService inquilinoService;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;
	
	private Double tipcambio;
		
	//upa
	private String claveupaTabCondicion;
	private String claveupaTabUpa;
	private Upa upa= new Upa();
	private Upa upaselected = new Upa();
	private List<Upa> listaUpas= new ArrayList<Upa>();
	
	//contrato
	private String nrocontrato;
	private List<CondicionBean> listaContrato= new ArrayList<CondicionBean>();;
	private boolean merced1=false,merced2=false,merced3=false,merced4=false,merced5=false,merced6=false,sipersonanatural=true;
	
	//Inquilino
	private int idinquilinoseleccionado;
	private int tipobusquedatabContratoSubInquilino=1;
	private String valorbusquedatabContratoSubInquilino;
	
	
	private Inquilino inquilino= new Inquilino();
	private List<Inquilino> listaInquilino= new ArrayList<Inquilino>();
	
	
	//Pestaña Cuotas
	private List<PeriodoPagadoBean> cuotasPagadas,cuotasPagadasSeleccion,cuotasPagadasFilter, cuotasPagadasObservacion;
	
	private List<CuotaBean> cuotasPendientes,cuotasPendientesSeleccion,cuotasPendientesFilter;
	private List<CuotaBean> cuotasPendientesC;
	private PeriodoPagadoBean cuota= new PeriodoPagadoBean();
	private List<ComprobanteBean> listaComprobantePeriodo= new ArrayList<ComprobanteBean>();
	
	private CondicionBean contratoBean;
	private List<CondicionBean> listaDetalleAdenda,listaDetalleAdendaObservacion;
	
	
	private double totalMontoCuotasPagadasSeleccion;
	private double totalMoraCuotasPagadasSeleccion;
	
	private double totalMontoCuotasPendientesSeleccion;
	private double totalMoraCuotasPendientesSeleccion;
	
	private String nombreInquilinoBusqueda;
	private StreamedContent report;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();
	private List<RentaBean> listaCuotasReconocimiento= new ArrayList<RentaBean>();
	private List<MaeDetalleCondicion> listaMaeCondicion= new ArrayList<MaeDetalleCondicion>();
	private List<MaeDetCondicionDetalle> listaMaeCondicionDetalle= new ArrayList<MaeDetCondicionDetalle>();
	
	
	@PostConstruct
	public void init(){
		
		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(new Date()));
	}
	
	
	public void buscarInquilino(){
		
		listaInquilino = inquilinoService.listarInquilinos(nombreInquilinoBusqueda, Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL);
	
		addInfoMessage("Inquilino encontrada", "Se le muestra la información referente al inquilino, para actualizar algún podrá hacerlo desde mantenimiento de Inquilino");
	}
	
	public void configurarTipoPersona(){
		if (inquilino.getTipopersona().getIdtipopersona()==1) {
			sipersonanatural=true;
		} else {
			sipersonanatural=false;
		}
		
	}
	
	public void buscarUpa(){
		
		upa=upaService.obtenerUpaPorClave(claveupaTabUpa);
		if(upa!=null){
			addInfoMessage("Upa encontrada", "Se le muestra la información referente a la upa, para actualizar algún podrá hacerlo desde mantenimiento de Upa");
		}else{
			addInfoMessage("Upa no encontrada", "la clave UPA no se encuentra registra en el sistema");

		}
	
	}
	public void buscarListaUpa(Upa upa){
		try{
			listaUpas.clear();
		    listaUpas=upaService.listarUpasxCondicion(upa.getClave(), upa.getDireccion(), upa.getNumprincipal(), upa.getNombrenuminterior());
		    if(listaUpas!=null && listaUpas.size()>0){
		    	addInfoMessage("Busqueda Exitosa", "Se le muestra la información buscada");
		    }else{
		    	addInfoMessage("No existe datos", "No se encontro ninguno registro de upa");
		    }
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	public void limpiarListaFormulario(){
		listaUpas.clear();
		upa= new Upa();
	}
	public void onRowSelect(SelectEvent event) {
		//System.out.println("upa ="+upaselected.getClave());
	}
	
	public void ocultando(){

		merced1=false;merced2=false;merced3=false;merced4=false;merced5=false;merced6=false;
	}
	
	public void buscarxcontrato(){
		ocultando();
		contratoBean=contratoService.obtenerContratoxNrocontrato(nrocontrato);
		if(contratoBean!=null){
		cuotasPagadas=contratoService.obtenerPeriodosPagados(contratoBean.getIdcontrato());
		obtenerInformacionCuotasPagadas();
		
			if (contratoBean.getNumerocuotas()<=12) {
				merced1=true;
			}else if (contratoBean.getNumerocuotas()<=24) {
				merced1=true;merced2=true;
			}else if (contratoBean.getNumerocuotas()<=36) {
				merced1=true;merced2=true;merced3=true;
			}else if (contratoBean.getNumerocuotas()<=48) {
				merced1=true;merced2=true;merced3=true;merced4=true;
			}else if (contratoBean.getNumerocuotas()<=60) {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;
			}else {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;merced6=true;
			}
			
			if (contratoBean.getTipopersona().equals("1")) {
				sipersonanatural=true;
			} else {
				sipersonanatural=false;
			}
			
			addInfoMessage("Contrato encontrado", "Se le muestra la información referente al contrato, para actualizar algún podrá hacerlo desde mantenimiento de contrato");
		}else{
			addInfoMessage("Contrato No encontrado", "No se encontrato información del numero de contrato buscado.");

		}
	}
	
	public void buscarxinquilino(){
		
		ocultando();
		obtenerInformacionCuotasPagadas();
		contratoBean=contratoService.obtenerInformacionCondicion(contratoBean.getIdcontrato());
		
		if (contratoBean.getCondicion().equals("Contrato")) {
			
			if (contratoBean.getNumerocuotas()<=12) {
				merced1=true;
			}else if (contratoBean.getNumerocuotas()<=24) {
				merced1=true;merced2=true;
			}else if (contratoBean.getNumerocuotas()<=36) {
				merced1=true;merced2=true;merced3=true;
			}else if (contratoBean.getNumerocuotas()<=48) {
				merced1=true;merced2=true;merced3=true;merced4=true;
			}else if (contratoBean.getNumerocuotas()<=60) {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;
			}else {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;merced6=true;
			}
			
			if (contratoBean.getTipopersona().equals("1")) {
				sipersonanatural=true;
			} else {
				sipersonanatural=false;
			}
			
		}else 
		
		addInfoMessage("Contrato encontrado", "Se le muestra la información referente al contrato, para actualizar algún podrá hacerlo desde mantenimiento de contrato");
	}
	
	public void clearing(){
		contratoBean=null;
		cuotasPagadas.clear();
		listaContrato.clear();
	}

	public void obtenerInformacionCuotasPagadas() {
		if (contratoBean != null) {
			inicializarSumaTotalCuotasSeleccion();
			cuotasPagadas = contratoService.obtenerPeriodosPagados(contratoBean.getIdcontrato());
				//agregado por el index incremental en cuotas pagadas
			int cont=1;
			for (int i = 0; i < cuotasPagadas.size(); i++) {
				if (cuotasPagadas.get(i).getEstado() != null) {
					if (cuotasPagadas.get(i).getEstado()
							.equals(Constante.ESTADO_ANULADO)) {
						cuotasPagadas.get(i).setIndex("0");// indice 0 por
															// estado anulado
					} else {
						cuotasPagadas.get(i).setIndex(Integer.toString(cont));
						cont++;
					}
				} else {
					cuotasPagadas.get(i).setIndex(Integer.toString(cont));
					cont++;
				}
			}
		}
	}
	public void obtenerInformacionCuotasPagadas(Integer idcontrato) {
		if (idcontrato != null) {
			inicializarSumaTotalCuotasSeleccion();
			cuotasPagadas = contratoService.obtenerPeriodosPagados(idcontrato);
				//agregado por el index incremental en cuotas pagadas
			int cont=1;
			for (int i = 0; i < cuotasPagadas.size(); i++) {
				if (cuotasPagadas.get(i).getEstado() != null) {
					if (cuotasPagadas.get(i).getEstado()
							.equals(Constante.ESTADO_ANULADO)) {
						cuotasPagadas.get(i).setIndex("0");// indice 0 por
															// estado anulado
					} else {
						cuotasPagadas.get(i).setIndex(Integer.toString(cont));
						cont++;
					}
				} else {
					cuotasPagadas.get(i).setIndex(Integer.toString(cont));
					cont++;
				}
			}
		}
	}
	
	
	public void obtenerInformacionCuotasPendientes(){
		if (contratoBean != null) {
			inicializarSumaTotalCuotasSeleccion();
			//listadependientes();
			if (contratoBean.getSinuevomantenimiento() != null && contratoBean.getSinuevomantenimiento()) {
				if(contratoBean.getSireconocimientodeuda()){ //reconocimiento de deuda
                	cuotasPendientes = recaudacionReporteService.generarRentasPendientesParaCobroReconocimientoDeuda(contratoBean.getIdcontrato(), tipcambio);
                }else{                                       // nuevo contrato y precontrato
                	cuotasPendientes = recaudacionReporteService.generarRentasPendientesParaCobroNuevoContrato(contratoBean.getIdcontrato(), tipcambio);
                }
			} else {
				   cuotasPendientes = recaudacionReporteService.obtenerInformacionCobroCondicionTipo(contratoBean.getIdcontrato(),contratoBean.getCondicion(), "pendientes",tipcambio);
			}
		}
	}
  public void listadependientes(){
	  List<CondicionBean> condicion=new ArrayList<CondicionBean>();
	  condicion=contratoService.obtenerListaContratoBean("");
	  for(int i =0;i<condicion.size();i++){
		  cuotasPendientesC = recaudacionReporteService
					.obtenerInformacionCobroCondicionTipo(
							condicion.get(i).getIdcontrato(),
							condicion.get(i).getCondicion(), "pendientes",
							tipcambio);
		 for(int j =0;j<cuotasPendientesC.size();j++){
			 if(cuotasPendientesC.get(j).getIdcuota()!=null){
			 System.out.println("idcuota="+cuotasPendientesC.get(j).getIdcuota()+";monto"+cuotasPendientesC.get(j).getMonto());
		 
			 }
			 }
	  
	  }
	
  }
	 public void buscarxupa(){
		 System.out.println("buscarxupa()=");
		ocultando();
		if (contratoBean!=null){
		contratoBean=contratoService.obtenerInformacionCondicion(contratoBean.getIdcontrato());
        System.out.println("contratoBean.getIdcontrato()=");
		}else{
			addWarnMessage("No se ha encontrado contrato", "Verifique que ha ingresado en número de contrato correcto y/o vuelva a buscar");
		}
		
		if (contratoBean!=null) {
			if (contratoBean.getCondicion().equals("Sin Contrato") ) {
				merced1=true;
				Calendar iniciocobro=Calendar.getInstance();
				iniciocobro.setTime(contratoBean.getIniciocobro());
				iniciocobro.add(Calendar.MONTH, 1);
				//addInfoMessage(Constantes.CONDICION_SIN_CONTRATO+"encontrado", "Se le muestra la información referente al "+Constantes.CONDICION_SIN_CONTRATO+ ", para actualizar algún dato podrá hacerlo desde mantenimiento de "+ Constantes.CONDICION_SIN_CONTRATO);
				addInfoMessage(contratoBean.getCondicion()+"encontrado", "Se le muestra la información referente al "+contratoBean.getCondicion()+". Para su actualización ingrese a mantenimiento de "+ contratoBean.getCondicion());

			}else {
				if(contratoBean.getCondicion().equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)){
					if (contratoBean.getSinuevomantenimiento() !=null && contratoBean.getSinuevomantenimiento()) {
						setearReconocimientoDeudaSeleccionado();
						addInfoMessage(contratoBean.getCondicion()+"encontrado", "Se le muestra la información referente al "+contratoBean.getCondicion()+". Para su actualización ingrese a mantenimiento de "+ contratoBean.getCondicion() );
					}
				}else{
				  setearContratoSeleccionado();
				
				  if (contratoBean.getCondicion().equals("Contrato")) {
						addInfoMessage(contratoBean.getCondicion()+"encontrado", "Se le muestra la información referente al "+contratoBean.getCondicion() +". Para su actualización ingrese a mantenimiento de "+ contratoBean.getCondicion() );
					
				  } else {
						addInfoMessage(contratoBean.getCondicion()+"encontrado", "Se le muestra la información referente al "+contratoBean.getCondicion() +". Para su actualización ingrese a mantenimiento de "+ contratoBean.getCondicion()  );
					
				    }
				}
			}
		}else {
			
			addWarnMessage("No se ha encontrado contrato", "Verifique que ha ingresado en número de contrato correcto y/o vuelva a buscar");
			
		}
		
		
	
	}
	
	public void buscarComprobantesPeriodo(int idcuota) {
		listaComprobantePeriodo=contratoService.buscarComprobantesPeriodo(idcuota);
	}
	public void buscarObservacionCuota(int idcuota) { 
		//listaComprobantePeriodo=contratoService.buscarComprobantesPeriodo(idcuota);
	if (contratoBean != null) {
		inicializarSumaTotalCuotasSeleccion();
		cuotasPagadasObservacion = contratoService.obtenerPeriodosPagadosObservacion(idcuota);
				
	}
	}
	public void buscarObservacionAdenda(int idadenda){
		listaDetalleAdendaObservacion=new ArrayList<CondicionBean>();
		//listaDetalleAdendaObservacion.clear();
		for(int i=0;i<listaDetalleAdenda.size();i++){
		   if(Integer.toString(listaDetalleAdenda.get(i).getIdadenda()).equals(Integer.toString(idadenda)))	{
			   CondicionBean cond=new CondicionBean();
			   cond.setIdadenda(listaDetalleAdenda.get(i).getIdadenda());
			   cond.setObservacionadenda(listaDetalleAdenda.get(i).getObservacionadenda());
			   listaDetalleAdendaObservacion.add(cond);
		   }
		}
	}
	public void buscarAdenda(String idcontrato) { 
		//listaComprobantePeriodo=contratoService.buscarComprobantesPeriodo(idcuota);
		int tipobusqueda=4;
		String estado="";
//	if (contratoBean != null) {
		inicializarSumaTotalCuotasSeleccion();
		
		listaDetalleAdenda = contratoService.buscarAdenda(idcontrato,estado,tipobusqueda);
				
//	}
	}
	public void buscarContratoxInquilino(){
		
		if (tipobusquedatabContratoSubInquilino==1) {
			listaContrato=contratoService.buscarConSinContratoxClave(valorbusquedatabContratoSubInquilino,"");
			
		} else if (tipobusquedatabContratoSubInquilino==2) {
			listaContrato=contratoService.buscarConSinContratoxDireccion(valorbusquedatabContratoSubInquilino,"");
		}else  {
			listaContrato=contratoService.buscarConSinContratoxNombreInquilino(valorbusquedatabContratoSubInquilino,"");
		}
	
	}
	
	public void buscarContratoxUpa(){
		System.out.println("claveupaTabCondicion = "+claveupaTabCondicion);
		listaContrato.clear();
		listaContrato=contratoService.obtenerListaContratoBeanDeUpa(claveupaTabCondicion);
	
	}
	
	public void determinarSumaTotalCuotasPagadasSeleccion(){
		inicializarSumaTotalCuotasSeleccion();
		
		for (PeriodoPagadoBean periodoPagadoBean : cuotasPagadasSeleccion) {
			totalMontoCuotasPagadasSeleccion=totalMontoCuotasPagadasSeleccion+periodoPagadoBean.getMonto();	
			totalMoraCuotasPagadasSeleccion=totalMoraCuotasPagadasSeleccion+periodoPagadoBean.getMora();
		}
		totalMontoCuotasPendientesSeleccion=FuncionesHelper.redondearNum(totalMontoCuotasPendientesSeleccion,2);
		totalMoraCuotasPendientesSeleccion=FuncionesHelper.redondearNum(totalMoraCuotasPendientesSeleccion,2);
	}
	
	public void determinarSumaTotalCuotasPendientesSeleccion(){
		inicializarSumaTotalCuotasSeleccion();
		for (CuotaBean cuotaBean : cuotasPendientesSeleccion) {
			totalMontoCuotasPendientesSeleccion=totalMontoCuotasPendientesSeleccion+cuotaBean.getMonto();	
			totalMoraCuotasPendientesSeleccion=totalMoraCuotasPendientesSeleccion+cuotaBean.getMora();
		}
		totalMontoCuotasPendientesSeleccion=FuncionesHelper.redondearNum(totalMontoCuotasPendientesSeleccion,2);
		totalMoraCuotasPendientesSeleccion=FuncionesHelper.redondearNum(totalMoraCuotasPendientesSeleccion,2);
	}
	
	public void inicializarSumaTotalCuotasSeleccion(){
		
		totalMoraCuotasPagadasSeleccion=0.0;
		totalMontoCuotasPagadasSeleccion=0.0;
		totalMoraCuotasPendientesSeleccion=0.0;
		totalMontoCuotasPendientesSeleccion=0.0;
	}
	
	/**
	 * Carga la informacion de contrato al seleccionarlo.
	 */	
	public void setearContratoSeleccionado() {
		listaRentas.clear();
		
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(contratoBean.getIniciocobro());
		
		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(contratoBean.getFincobro()!=null?contratoBean.getFincobro():new Date());
		
		
		int nrocoutas=contratoBean.getNumerocuotas();
		
		if (contratoBean.getSinuevomantenimiento() !=null && contratoBean.getSinuevomantenimiento()) {
			listaRentas=recaudacionReporteService.obtenerRentasNuevoMantenimientoContrato(contratoBean.getIdcontrato());
			
		} else {
			for (int i = 1; i <= nrocoutas; i++) {
				RentaBean rentaBean= new RentaBean();
				rentaBean.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
				rentaBean.setAnio(String.valueOf(iniciocobro.get(Calendar.YEAR)));
				rentaBean.setSecuencia(i);
				rentaBean.setContraprestacion(contratoBean.getCuota1());
				rentaBean.setId(listaRentas.size()+1);
				if (i<=12) {
					rentaBean.setRenta(contratoBean.getCuota1());
					rentaBean.setMontopagar(contratoBean.getCuota1());
					
				}else if (i<=24) {
					rentaBean.setRenta(contratoBean.getCuota2());
					rentaBean.setMontopagar(contratoBean.getCuota2());
				}else if (i<=36) {
					rentaBean.setRenta(contratoBean.getCuota3());
					rentaBean.setMontopagar(contratoBean.getCuota3());
				}else if (i<=48) {
					rentaBean.setRenta(contratoBean.getCuota4());
					rentaBean.setMontopagar(contratoBean.getCuota4());
				}else if (i<=60) {
					rentaBean.setRenta(contratoBean.getCuota5());
					rentaBean.setMontopagar(contratoBean.getCuota5());
				}else {
					rentaBean.setRenta(contratoBean.getCuota6());
					rentaBean.setMontopagar(contratoBean.getCuota6());
				}
				
				
				listaRentas.add(rentaBean);
				iniciocobro.add(Calendar.MONTH, 1);
			}
		}
		
		

	}
	public void setearReconocimientoDeudaSeleccionado(){
		try{
		listaRentas.clear();
		
		if (contratoBean.getSinuevomantenimiento() !=null && contratoBean.getSinuevomantenimiento()) {
			listaRentas=recaudacionReporteService.obtenerRentasNuevoMantenimientoContrato(contratoBean.getIdcontrato());
			listaMaeCondicion=contratoService.obtenerListaMaeCondicion(contratoBean.getIdcontrato());
			listaMaeCondicionDetalle=contratoService.obtenerListaMaeCondicionDetalle(contratoBean.getIdcontrato());
			 
				for (MaeDetalleCondicion mae : listaMaeCondicion) {
					Set<MaeDetCondicionDetalle> maecondiciondetalle = new HashSet<MaeDetCondicionDetalle>();
					for (MaeDetCondicionDetalle maed : listaMaeCondicionDetalle) {
						if (mae.getIdDetalleCondicion() == maed.getMaedetallecondicion().getIdDetalleCondicion()) {
							maecondiciondetalle.add(maed);
						}
					}
					mae.setMaedetcondiciondetalles(maecondiciondetalle);		
				}
				for(RentaBean renta:listaRentas){
					Set<MaeDetCondicionDetalleBean> maecondBean = new HashSet<MaeDetCondicionDetalleBean>();
					
					
					for(MaeDetalleCondicion mae :listaMaeCondicion){
						
						
					 if(renta.getNrocuota().equals(mae.getNrocuota())){
						 List<MaeDetCondicionDetalleBean> maeconddetalle = new ArrayList<MaeDetCondicionDetalleBean>();
						  for(MaeDetCondicionDetalle maecond_detalle:mae.getMaedetcondiciondetalles()){
							  MaeDetCondicionDetalleBean maecond_detalleBean= new MaeDetCondicionDetalleBean();
							  maecond_detalleBean.setAnio(maecond_detalle.getAnio());
							  maecond_detalleBean.setConcepto(maecond_detalle.getConcepto());
							  maecond_detalleBean.setEstado(maecond_detalle.getEstado());
							  maecond_detalleBean.setFec_anula(maecond_detalle.getFec_anula());
							  maecond_detalleBean.setFeccre(maecond_detalle.getFeccre());
							  maecond_detalleBean.setFecmod(maecond_detalle.getFecmod());
							  maecond_detalleBean.setHost_ip_anula(maecond_detalle.getHost_ip_anula());
							  maecond_detalleBean.setHost_ip_cre(maecond_detalle.getHost_ip_cre());
							  maecond_detalleBean.setHost_ip_mod(maecond_detalle.getHost_ip_mod());
							  maecond_detalleBean.setId_condicion(maecond_detalle.getMaedetallecondicion().getIdDetalleCondicion());
							  maecond_detalleBean.setId_condicion_detalle(maecond_detalle.getIdcondiciondetalle());
							  maecond_detalleBean.setIdcontrato(maecond_detalle.getIdcontrato());
							  maecond_detalleBean.setIddetallecondicion(maecond_detalle.getIddetallecondicion());
							  maecond_detalleBean.setIdconcepto(maecond_detalle.getIdconcepto());
							  maecond_detalleBean.setMes(maecond_detalle.getMes());
							  maecond_detalleBean.setMonto(maecond_detalle.getMonto());
							  maecond_detalleBean.setMotivo_anula(maecond_detalle.getMotivo_anula());
							  maecond_detalleBean.setNumeromes(maecond_detalle.getNumeromes());
							  maecond_detalleBean.setObs(maecond_detalle.getObs());
							  maecond_detalleBean.setObs_mod(maecond_detalle.getObs_mod());
							  maecond_detalleBean.setSecuencia(maecond_detalle.getSecuencia());
							  maecond_detalleBean.setSianulado(maecond_detalle.getSianulado());
							  maecond_detalleBean.setTipomoneda(maecond_detalle.getTipomoneda());
							  maecond_detalleBean.setUsr_anula(maecond_detalle.getUsr_anula());
							  maecond_detalleBean.setUsrcre(maecond_detalle.getUsrcre());
							  maecond_detalleBean.setUsrmod(maecond_detalle.getUsrmod());
							  maeconddetalle.add(maecond_detalleBean);					  
						  }
						  Collections.sort(maeconddetalle, new Comparator<MaeDetCondicionDetalleBean>(){

							    @Override
							    public int compare(MaeDetCondicionDetalleBean o1, MaeDetCondicionDetalleBean o2) {
							        return o1.getSecuencia().compareTo(o2.getSecuencia());
							    }
							});
						  
						  renta.setMaecondiciondetalle(maeconddetalle);
					 }
				}
			 }
 			
		}
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public void Excel(ActionEvent actionEvent) throws JRException, IOException {
		if(listaUpas!=null && listaUpas.size()>0){
			XLS_Documento(listaUpas);
			addInfoMessage("", "Se exporto con exito el archivo ");
		}else{
			addInfoMessage("", "La lista de UPAS a exportar se encuentra vacia");
		}
     }
	public void XLS_Documento(List<Upa> lista) throws FileNotFoundException 
	{
		
		
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("listaUpas");
	        
//	        SXSSFWorkbook workbook= new SXSSFWorkbook(); //para datos grandes consume la memoria..
//	        SXSSFSheet sheet= workbook.createSheet("Lista");

	        
	        sheet.setColumnWidth(0, 3000); 
	        sheet.setColumnWidth(1, 6500);
	        sheet.setColumnWidth(2, 2500);
	        sheet.setColumnWidth(3, 2500);
	        sheet.setColumnWidth(4, 3500);
	        sheet.setColumnWidth(5, 3500);
	        sheet.setColumnWidth(6, 3500);
	        sheet.setColumnWidth(8, 3500);
	        sheet.setColumnWidth(9, 3500);
	        sheet.setColumnWidth(10, 3500);
	        sheet.setColumnWidth(11, 3500);
	        sheet.setColumnWidth(12, 3500);
	        sheet.setColumnWidth(13, 3500);
	        sheet.setColumnWidth(14, 3500);
	        sheet.setColumnWidth(15, 3500);
	        sheet.setColumnWidth(18, 4000);
	        
	        /**Insercion de cabecera (rownum=0) **/
	        CellStyle csBold = null;
	        
	        Font bold = workbook.createFont();
	        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        bold.setFontHeightInPoints((short) 10);
	        
	        csBold = workbook.createCellStyle();
	        csBold.setBorderBottom(CellStyle.BORDER_THIN);
	        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	        csBold.setFont(bold);
	        
	        String[] headers = new String[] { "CLAVE", "DIRECCION", "N° PRINCIPAL","N° DE INTERIOR","DISTRITO","PROVINCIA","DEPARTAMENTO","USO","CONDICION","PROCESO JUDICIAL","COPROPIEDAD","ESTADO","ACTUALIZACION DE CLAVE","CLAVE ANTERIOR"};
	        Row r = sheet.createRow(0);
	        for (int rn=0; rn<headers.length; rn++) {
	           Cell cell = r.createCell(rn);
	           cell.setCellValue(headers[rn]);
	           cell.setCellStyle(csBold);
	        }
	        
	        
	        
	        /**Insercion de datos **/
	        
	        
	        int rownum = 1;
	        for (int i = 0; i < lista.size(); i++) {
	        	
	            Upa bean= new Upa();
	            bean= lista.get(i);

					Row row = sheet.createRow(rownum++);
	                for (int j = 0; j < 14; j++) {//NUMERO DE COLUMNAS
	                	Cell cell = row.createCell(j);
	                	if (j==0)
	                		cell.setCellValue((String)bean.getClave());
	                	 else if (j==1)
	                		 cell.setCellValue((String)bean.getDireccion());
	                			else if (j==2)
	                				  cell.setCellValue((String)bean.getNumprincipal());
	                			   else if (j==3)
	                				   	cell.setCellValue((String)bean.getNombrenuminterior());
	                			   		else if (j==4)
	                			   				cell.setCellValue((String)bean.getInmueblemaestro().getUbigeo().getDist());
	                			   				else if (j==5)
	                			   						cell.setCellValue((String)bean.getInmueblemaestro().getUbigeo().getProv());             	
	                			   						else if (j==6)
	                			   								cell.setCellValue((String)bean.getInmueblemaestro().getUbigeo().getDepa());
	                			   								else if (j==7)
	                			   										cell.setCellValue((String)bean.getUso());
	                			   										else if (j==8)
	                			   												cell.setCellValue((String)bean.getTugurizadoruinoso());
	                			   												else if (j==9)
	                			   														cell.setCellValue(bean.getSiprocesojudicial()!=null? (bean.getSiprocesojudicial()==true? "SI":"NO"):"NO");
				                			 											else if (j==10)
				                			 													cell.setCellValue(bean.getSicopropiedad()!=null? (bean.getSicopropiedad()==true? "SI":"NO"):"NO");
				                			 													else if (j==11)
				                			 															cell.setCellValue(bean.getEstado()!=null? (bean.getEstado().equals("0")? "Activo":"Inactivo"):"Activo");
				                			 															else if (j==12)
				                			 																	cell.setCellValue(bean.getSiactualizaclave()!=null? (bean.getSiactualizaclave()==true? "Si":"No"):"No");
				                			 																	else if (j==13)
				                			 																			cell.setCellValue(bean.getSiactualizaclave()==true? bean.getIdupa_anterior().getClave():"");
					                					 	 
	                							}

				
			}



	        try {
	        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        	try {
	        		workbook.write(bos);
	        	} finally {
	        	    bos.close();
	        	}
	        	
	            InputStream stream;
	            stream = new ByteArrayInputStream(bos.toByteArray());
	        	
				report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLS, "Reporte_Clave_UPA_"+FuncionesHelper.fechaToString(new Date())+".xls");
				//report= new DefaultStreamedContent(stream,Constantes.APPLICATION_XLSX, "ReporteComprobante"+FuncionesHelper.fechaToString(new Date())+".XLSX");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	}
	public IUpaService getUpaService() {
		return upaService;
	}
	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public String getNrocontrato() {
		return nrocontrato;
	}
	public void setNrocontrato(String nrocontrato) {
		this.nrocontrato = nrocontrato;
	}
	public IContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isMerced1() {
		return merced1;
	}
	public void setMerced1(boolean merced1) {
		this.merced1 = merced1;
	}
	public boolean isMerced2() {
		return merced2;
	}
	public void setMerced2(boolean merced2) {
		this.merced2 = merced2;
	}
	public boolean isMerced3() {
		return merced3;
	}
	public void setMerced3(boolean merced3) {
		this.merced3 = merced3;
	}
	public boolean isMerced4() {
		return merced4;
	}
	public void setMerced4(boolean merced4) {
		this.merced4 = merced4;
	}
	public boolean isMerced5() {
		return merced5;
	}
	public void setMerced5(boolean merced5) {
		this.merced5 = merced5;
	}
	public boolean isMerced6() {
		return merced6;
	}
	public void setMerced6(boolean merced6) {
		this.merced6 = merced6;
	}
	public boolean isSipersonanatural() {
		return sipersonanatural;
	}
	public void setSipersonanatural(boolean sipersonanatural) {
		this.sipersonanatural = sipersonanatural;
	}

	public List<Inquilino> getListaInquilino() {
		return listaInquilino;
	}
	public void setListaInquilino(List<Inquilino> listaInquilino) {
		this.listaInquilino = listaInquilino;
	}
	public IInquilinoService getInquilinoService() {
		return inquilinoService;
	}
	public void setInquilinoService(IInquilinoService inquilinoService) {
		this.inquilinoService = inquilinoService;
	}
	public int getIdinquilinoseleccionado() {
		return idinquilinoseleccionado;
	}
	public void setIdinquilinoseleccionado(int idinquilinoseleccionado) {
		this.idinquilinoseleccionado = idinquilinoseleccionado;
	}

	public List<CondicionBean> getListaContrato() {
		return listaContrato;
	}


	public void setListaContrato(List<CondicionBean> listaContrato) {
		this.listaContrato = listaContrato;
	}


	public String getClaveupaTabCondicion() {
		return claveupaTabCondicion;
	}


	public void setClaveupaTabCondicion(String claveupaTabCondicion) {
		this.claveupaTabCondicion = claveupaTabCondicion;
	}


	public String getClaveupaTabUpa() {
		return claveupaTabUpa;
	}


	public void setClaveupaTabUpa(String claveupaTabUpa) {
		this.claveupaTabUpa = claveupaTabUpa;
	}


	public Upa getUpa() {
		return upa;
	}
	public void setUpa(Upa upa) {
		this.upa = upa;
	}
	public Inquilino getInquilino() {
		return inquilino;
	}
	public void setInquilino(Inquilino inquilino) {
		this.inquilino = inquilino;
	}

	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}
	public void setRecaudacionReporteService(IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}
	
	public CondicionBean getContratoBean() {
		return contratoBean;
	}
	public void setContratoBean(CondicionBean contratoBean) {
		this.contratoBean = contratoBean;
	}

	public PeriodoPagadoBean getCuota() {
		return cuota;
	}
	
	public void setCuota(PeriodoPagadoBean cuota) {
		this.cuota = cuota;
	}
	public List<ComprobanteBean> getListaComprobantePeriodo() {
		return listaComprobantePeriodo;
	}
	public void setListaComprobantePeriodo(
			List<ComprobanteBean> listaComprobantePeriodo) {
		this.listaComprobantePeriodo = listaComprobantePeriodo;
	}


	public int getTipobusquedatabContratoSubInquilino() {
		return tipobusquedatabContratoSubInquilino;
	}


	public void setTipobusquedatabContratoSubInquilino(
			int tipobusquedatabContratoSubInquilino) {
		this.tipobusquedatabContratoSubInquilino = tipobusquedatabContratoSubInquilino;
	}


	public String getValorbusquedatabContratoSubInquilino() {
		return valorbusquedatabContratoSubInquilino;
	}


	public void setValorbusquedatabContratoSubInquilino(
			String valorbusquedatabContratoSubInquilino) {
		this.valorbusquedatabContratoSubInquilino = valorbusquedatabContratoSubInquilino;
	}


	/**
	 * @return the cuotasPagadasSeleccion
	 */
	public List<PeriodoPagadoBean> getCuotasPagadasSeleccion() {
		return cuotasPagadasSeleccion;
	}


	/**
	 * @param cuotasPagadasSeleccion the cuotasPagadasSeleccion to set
	 */
	public void setCuotasPagadasSeleccion(
			List<PeriodoPagadoBean> cuotasPagadasSeleccion) {
		this.cuotasPagadasSeleccion = cuotasPagadasSeleccion;
	}



	/**
	 * @return the totalMontoCuotasPagadasSeleccion
	 */
	public double getTotalMontoCuotasPagadasSeleccion() {
		return totalMontoCuotasPagadasSeleccion;
	}

	/**
	 * @param totalMontoCuotasPagadasSeleccion the totalMontoCuotasPagadasSeleccion to set
	 */
	public void setTotalMontoCuotasPagadasSeleccion(
			double totalMontoCuotasPagadasSeleccion) {
		this.totalMontoCuotasPagadasSeleccion = totalMontoCuotasPagadasSeleccion;
	}

	/**
	 * @return the totalMoraCuotasPagadasSeleccion
	 */
	public double getTotalMoraCuotasPagadasSeleccion() {
		return totalMoraCuotasPagadasSeleccion;
	}

	/**
	 * @param totalMoraCuotasPagadasSeleccion the totalMoraCuotasPagadasSeleccion to set
	 */
	public void setTotalMoraCuotasPagadasSeleccion(
			double totalMoraCuotasPagadasSeleccion) {
		this.totalMoraCuotasPagadasSeleccion = totalMoraCuotasPagadasSeleccion;
	}

	/**
	 * @return the totalMontoCuotasPendientesSeleccion
	 */
	public double getTotalMontoCuotasPendientesSeleccion() {
		return totalMontoCuotasPendientesSeleccion;
	}

	/**
	 * @param totalMontoCuotasPendientesSeleccion the totalMontoCuotasPendientesSeleccion to set
	 */
	public void setTotalMontoCuotasPendientesSeleccion(
			double totalMontoCuotasPendientesSeleccion) {
		this.totalMontoCuotasPendientesSeleccion = totalMontoCuotasPendientesSeleccion;
	}

	/**
	 * @return the totalMoraCuotasPendientesSeleccion
	 */
	public double getTotalMoraCuotasPendientesSeleccion() {
		return totalMoraCuotasPendientesSeleccion;
	}

	/**
	 * @param totalMoraCuotasPendientesSeleccion the totalMoraCuotasPendientesSeleccion to set
	 */
	public void setTotalMoraCuotasPendientesSeleccion(
			double totalMoraCuotasPendientesSeleccion) {
		this.totalMoraCuotasPendientesSeleccion = totalMoraCuotasPendientesSeleccion;
	}

	/**
	 * @return the cuotasPendientes
	 */
	public List<CuotaBean> getCuotasPendientes() {
		return cuotasPendientes;
	}


	/**
	 * @param cuotasPendientes the cuotasPendientes to set
	 */
	public void setCuotasPendientes(List<CuotaBean> cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	/**
	 * @return the tipocambioService
	 */
	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}

	/**
	 * @param tipocambioService the tipocambioService to set
	 */
	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}

	/**
	 * @return the tipcambio
	 */
	public Double getTipcambio() {
		return tipcambio;
	}

	/**
	 * @param tipcambio the tipcambio to set
	 */
	public void setTipcambio(Double tipcambio) {
		this.tipcambio = tipcambio;
	}

	/**
	 * @return the cuotasPendientesSeleccion
	 */
	public List<CuotaBean> getCuotasPendientesSeleccion() {
		return cuotasPendientesSeleccion;
	}

	/**
	 * @param cuotasPendientesSeleccion the cuotasPendientesSeleccion to set
	 */
	public void setCuotasPendientesSeleccion(
			List<CuotaBean> cuotasPendientesSeleccion) {
		this.cuotasPendientesSeleccion = cuotasPendientesSeleccion;
	}


	/**
	 * @return the cuotasPagadas
	 */
	public List<PeriodoPagadoBean> getCuotasPagadas() {
		return cuotasPagadas;
	}

	/**
	 * @param cuotasPagadas the cuotasPagadas to set
	 */
	public void setCuotasPagadas(List<PeriodoPagadoBean> cuotasPagadas) {
		this.cuotasPagadas = cuotasPagadas;
	}

	/**
	 * @return the cuotasPagadasFilter
	 */
	public List<PeriodoPagadoBean> getCuotasPagadasFilter() {
		return cuotasPagadasFilter;
	}

	/**
	 * @param cuotasPagadasFilter the cuotasPagadasFilter to set
	 */
	public void setCuotasPagadasFilter(List<PeriodoPagadoBean> cuotasPagadasFilter) {
		this.cuotasPagadasFilter = cuotasPagadasFilter;
	}

	/**
	 * @return the cuotasPendientesFilter
	 */
	public List<CuotaBean> getCuotasPendientesFilter() {
		return cuotasPendientesFilter;
	}

	/**
	 * @param cuotasPendientesFilter the cuotasPendientesFilter to set
	 */
	public void setCuotasPendientesFilter(List<CuotaBean> cuotasPendientesFilter) {
		this.cuotasPendientesFilter = cuotasPendientesFilter;
	}
	public String getNombreInquilinoBusqueda() {
		return nombreInquilinoBusqueda;
	}

	public void setNombreInquilinoBusqueda(String nombreInquilinoBusqueda) {
		this.nombreInquilinoBusqueda = nombreInquilinoBusqueda;
	}


	public List<RentaBean> getListaRentas() {
		return listaRentas;
	}


	public void setListaRentas(List<RentaBean> listaRentas) {
		this.listaRentas = listaRentas;
	}


	public List<PeriodoPagadoBean> getCuotasPagadasObservacion() {
		return cuotasPagadasObservacion;
	}


	public void setCuotasPagadasObservacion(
			List<PeriodoPagadoBean> cuotasPagadasObservacion) {
		this.cuotasPagadasObservacion = cuotasPagadasObservacion;
	}


	public List<CondicionBean> getListaDetalleAdenda() {
		return listaDetalleAdenda;
	}


	public void setListaDetalleAdenda(List<CondicionBean> listaDetalleAdenda) {
		this.listaDetalleAdenda = listaDetalleAdenda;
	}


	public List<CondicionBean> getListaDetalleAdendaObservacion() {
		return listaDetalleAdendaObservacion;
	}


	public void setListaDetalleAdendaObservacion(
			List<CondicionBean> listaDetalleAdendaObservacion) {
		this.listaDetalleAdendaObservacion = listaDetalleAdendaObservacion;
	}


	public List<RentaBean> getListaCuotasReconocimiento() {
		return listaCuotasReconocimiento;
	}


	public void setListaCuotasReconocimiento(
			List<RentaBean> listaCuotasReconocimiento) {
		this.listaCuotasReconocimiento = listaCuotasReconocimiento;
	}

	public List<MaeDetalleCondicion> getListaMaeCondicion() {
		return listaMaeCondicion;
	}


	public void setListaMaeCondicion(List<MaeDetalleCondicion> listaMaeCondicion) {
		this.listaMaeCondicion = listaMaeCondicion;
	}


	public List<MaeDetCondicionDetalle> getListaMaeCondicionDetalle() {
		return listaMaeCondicionDetalle;
	}


	public void setListaMaeCondicionDetalle(
			List<MaeDetCondicionDetalle> listaMaeCondicionDetalle) {
		this.listaMaeCondicionDetalle = listaMaeCondicionDetalle;
	}


	public List<Upa> getListaUpas() {
		return listaUpas;
	}


	public void setListaUpas(List<Upa> listaUpas) {
		this.listaUpas = listaUpas;
	}


	public StreamedContent getReport() {
		return report;
	}


	public void setReport(StreamedContent report) {
		this.report = report;
	}


	public Upa getUpaselected() {
		return upaselected;
	}


	public void setUpaselected(Upa upaselected) {
		this.upaselected = upaselected;
	}

    


     

	
	
}