package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.IAutovaluoArbitrioService;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;
/**
 * creado el 04-04-2019
 * autor: Marco Alarcón 
 * 
 */
@ManagedBean(name = "autovaluoconsultaMB")
@ViewScoped
public class AutovaluoConsultaManagedBean extends BaseController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//ARBITRIO
	private Arbitrio arbitrio=new Arbitrio();
	//UPA
	private String claveUpaArbitrio;
	//TIPO DE ELECCION EN REPORTE X UPA
	private String tipoReporteSeleccionadoTabUpa="4";
	@ManagedProperty(value = "#{autovaluoarbitrioService}")
	private transient IAutovaluoArbitrioService arbitrioService;
 
	private List<UpaBean> listaUPA=new ArrayList<UpaBean>();
	private List<UpaBean> listaUPAFiltered;
	private List<ComprobanteBean> listaComprobantePeriodo= new ArrayList<ComprobanteBean>();
	private List<CondicionBean> listaContrato= new ArrayList<CondicionBean>();;
	
	private UpaBean upaBean=new UpaBean();
	private String direccionUPA;
	private boolean sipersonanatural=true;
	private Date desdeCobrador,hastaCobrador,desdeCartera,hastaCartera,desdeUpa,hastaUpa,desdeInquilino,hastaInquilino;
	
	private List<ArbitrioBean> listaARBITRIO;
	private List<ArbitrioBean> listaAutovaluoArbitrio;
	private List<ArbitrioBean> listaARBITRIOFiltered;
	private List<PagoArbitrioBean> listaPagos=new ArrayList<PagoArbitrioBean>();
	private List<CuotaArbitrioBean> listaDetallecuotasCondicion = new ArrayList<CuotaArbitrioBean>();
	private List<DetalleArbitrioBean> listaDETALLEARBITRIO;
	private DetalleArbitrioBean detalleArbitrioBean;
	
	private ArbitrioBean arbitrioBean,condicionSeleccionado;
	private int anioseleccionado=2014;
	private String  frecuenciaseleccionado;
	private int mesInicial,mesFinal;
	private StreamedContent report;
	private Double tipcambio;
	//cuotas
	private List<PeriodoPagadoBean> cuotasPagadas,cuotasPagadasSeleccion,cuotasPagadasFilter,listaPagosxMes;
	private List<CuotaArbitrioBean> cuotasPendientes,cuotasPendientesSeleccion,cuotasPendientesFilter;
	//total y mora
	private double totalMontoCuotasPagadasSeleccion,totalMontoCuotasPendientesSeleccion,totalMoraCuotasPagadasSeleccion,totalMoraCuotasPendientesSeleccion;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean usuarioMB;
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;

	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value ="#{contratoService}")
	private transient IContratoService contratoService;
	
	@ManagedProperty(value ="#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;


	private CondicionBean contratoBean;
	private Date desdeRepPendiente,hastaRepPendiente,desdeRepCancelado, hastaRepCancelado,desdeRepPendienteCancelado,hastaRepPendienteCancelado;
	
	@PostConstruct
	public void init(){
		
	}
	
	public void buscarxUpaArb(){

		arbitrio=arbitrioService.obtenerArbitrioPorClaveUpa(claveUpaArbitrio);
		addInfoMessage("Upa encontrada","Se le muestra la informacion referente a la upa ");
	}
	public String nombreInquilinoxCondicion(String Condicion,List<CondicionBean> lista,ArbitrioBean arbitriobean){
		
		return null;
	}
	public int fechaAnioDate(Date datetime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String fechaComoCadena = sdf.format(datetime);
//		System.out.println("año="+Integer.parseInt(fechaComoCadena));
	     return Integer.parseInt(fechaComoCadena);
	}
	public int fechaMesDate(Date datetime){
		int anio=fechaAnioDate(datetime);
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String fechaComoCadena = sdf.format(datetime);
//		System.out.println("mes="+Integer.parseInt(fechaComoCadena));
		if (arbitrioBean.getPeriodo() == anio) {
			mesInicial = Integer.parseInt(fechaComoCadena);
		} else {
			if (arbitrioBean.getPeriodo() > anio) {
				anio = arbitrioBean.getPeriodo();
				mesInicial = 0;
			}
		}
//		System.out.println("año="+anio+" mes="+mesInicial);
       return mesInicial;
	}
	public List<Date> getListaEntreFechas(Date fechaInicio, Date fechaFin) {
	    Calendar c1 = Calendar.getInstance();
	    c1.setTime(fechaInicio);
	    Calendar c2 = Calendar.getInstance();
	    c2.setTime(fechaFin);
	    List<Date> listaFechas = new ArrayList<Date>();
	    while (!c1.after(c2)) {
	        listaFechas.add(c1.getTime());
	        c1.add(Calendar.MONTH, 1);
	    }
	    return listaFechas;
	}
	public String buscarArbitrioxcondicion(ArbitrioBean arbitrioBeans){

		CondicionBean contratoVigente=new CondicionBean();
		boolean condicionAutovaluo=false;
        mesInicial=0; mesFinal=0;
		arbitrioBean=arbitrioService.obtenerArbitrioPorCondicion(arbitrioBeans.getIdarbitrio());
		 
		if(arbitrioBean!=null){
				
			listaContrato=contratoService.obtenerListaContratoBeanDeUpa(arbitrioBean.getClave());
			if(listaContrato!=null){
			for(int i =0;i<listaContrato.size();i++){
				if(listaContrato.get(i).getEstado().equals(Constante.CONDICION_CONTRATO_VIGENTE)){
					contratoVigente.setCondicion(listaContrato.get(i).getCondicion());
					contratoVigente.setNombresInquilino(listaContrato.get(i).getNombresInquilino());
					contratoVigente.setDni(listaContrato.get(i).getDni());
					contratoVigente.setRuc(listaContrato.get(i).getRuc());
					contratoVigente.setTipopersona(listaContrato.get(i).getTipopersona());
					contratoVigente.setSiocupante(listaContrato.get(i).getSiocupante());
					contratoVigente.setNombreocupante(listaContrato.get(i).getNombreocupante());
				}
			}
			
			for(int i =0;i<listaContrato.size();i++){
		     
			switch(listaContrato.get(i).getCondicion()){
			case Constante.CONDICION_CONTRATO: if(fechaAnioDate(listaContrato.get(i).getIniciocontrato())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincontrato())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocontrato(),listaContrato.get(i).getFincontrato());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocontrato());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}} ;break;
			case Constante.CONDICION_SINCONTRATO: 
				if(listaContrato.get(i).getEstado().equals(Constante.CONDICION_CONTRATO_VIGENTE)){ listaContrato.get(i).setFincobro(new Date());}
				if(fechaAnioDate(listaContrato.get(i).getIniciocobro())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincobro())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocobro(),listaContrato.get(i).getFincobro());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocobro());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}} ;break;
			case Constante.CONDICION_PRECONTRATO: if(fechaAnioDate(listaContrato.get(i).getIniciocobro())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincobro())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocobro(),listaContrato.get(i).getFincobro());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocobro());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}};break;
			default :arbitrioBean.setTipopersona(contratoVigente.getTipopersona());
			arbitrioBean.setNombreinquilino(contratoVigente.getNombresInquilino());
			arbitrioBean.setDni(contratoVigente.getDni());
			arbitrioBean.setRuc(contratoVigente.getRuc());
			arbitrioBean.setSiocupante(contratoVigente.getSiocupante());
			arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
			arbitrioBean.setNombreocupante(contratoVigente.getNombreocupante());condicionAutovaluo=true;break;
			}
//			System.out.println("listacontrato["+i+"]="+listaContrato.get(i).getNombresInquilino()+"fechainicio="+listaContrato.get(i).getIniciocobro());
		}
			}
			else{
				arbitrioBean.setNombreinquilino(Constante.NO_NOMBRE_INQUILINO);
			}
		      
		}
		return arbitrioBean.getNombreinquilino();
	}

    public void buscarArbitrioxCondicionSeleccionado(){
    	listaPagos.clear();
    	listaContrato.clear();
    	CondicionBean contratoVigente=new CondicionBean();
		boolean condicionAutovaluo=false;
        mesInicial=0; mesFinal=0;
		arbitrioBean=arbitrioService.obtenerArbitrioPorCondicion(arbitrioBean.getIdarbitrio());
		listaContrato=contratoService.obtenerListaContratoUpa();
		
			 for(int i=0;i<listaContrato.size();i++){
				 if(arbitrioBean.getClave().equals(listaContrato.get(i).getClaveUpa())){
					arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino()); 
					arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
					arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
					arbitrioBean.setDni(listaContrato.get(i).getDni());
					arbitrioBean.setRuc(listaContrato.get(i).getRuc());
					arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
					arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
					arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;
				 }
			 }
			 if(arbitrioBean.getNombreinquilino()==null){
				 arbitrioBean.setNombreinquilino(Constante.NO_NOMBRE_INQUILINO);
				 condicionAutovaluo=true;
			 }
				if(condicionAutovaluo){
					setearArbitrioSeleccionado();
						}
					else{
						
					}
		
		
    }
	public void buscarArbitrioxcondicion(){

		CondicionBean contratoVigente=new CondicionBean();
		boolean condicionAutovaluo=false;
        mesInicial=0; mesFinal=0;
		arbitrioBean=arbitrioService.obtenerArbitrioPorCondicion(arbitrioBean.getIdarbitrio());
		 
		if(arbitrioBean!=null){
				
			listaContrato=contratoService.obtenerListaContratoBeanDeUpa(arbitrioBean.getClave());
			for(int i =0;i<listaContrato.size();i++){
				if(listaContrato.get(i).getEstado().equals(Constante.CONDICION_CONTRATO_VIGENTE)){
					contratoVigente.setCondicion(listaContrato.get(i).getCondicion());
					contratoVigente.setNombresInquilino(listaContrato.get(i).getNombresInquilino());
				}
			}
			for(int i =0;i<listaContrato.size();i++){
		     
			switch(listaContrato.get(i).getCondicion()){
			case Constante.CONDICION_CONTRATO: if(fechaAnioDate(listaContrato.get(i).getIniciocontrato())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincontrato())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocontrato(),listaContrato.get(i).getFincontrato());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocontrato());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}} ;break;
			case Constante.CONDICION_SINCONTRATO: 
				if(listaContrato.get(i).getEstado().equals(Constante.CONDICION_CONTRATO_VIGENTE)){ listaContrato.get(i).setFincobro(new Date());}
				if(fechaAnioDate(listaContrato.get(i).getIniciocobro())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincobro())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocobro(),listaContrato.get(i).getFincobro());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocobro());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}} ;break;
			case Constante.CONDICION_PRECONTRATO: if(fechaAnioDate(listaContrato.get(i).getIniciocobro())<=arbitrioBean.getPeriodo()&&fechaAnioDate(listaContrato.get(i).getFincobro())>=arbitrioBean.getPeriodo()){
				if(listaContrato.get(i).getNombresInquilino().equals(contratoVigente.getNombresInquilino())){
				getListaEntreFechas(listaContrato.get(i).getIniciocobro(),listaContrato.get(i).getFincobro());
				mesInicial=fechaMesDate(listaContrato.get(i).getIniciocobro());
				arbitrioBean.setTipopersona(listaContrato.get(i).getTipopersona());
				arbitrioBean.setNombreinquilino(listaContrato.get(i).getNombresInquilino());
				arbitrioBean.setDni(listaContrato.get(i).getDni());
				arbitrioBean.setRuc(listaContrato.get(i).getRuc());
				arbitrioBean.setSiocupante(listaContrato.get(i).getSiocupante());
				arbitrioBean.setFrecuencia(Constante.FRECUENCIA_ARBITRIO);
				arbitrioBean.setNombreocupante(listaContrato.get(i).getNombreocupante());condicionAutovaluo=true;}};break;
			}
//			System.out.println("listacontrato["+i+"]="+listaContrato.get(i).getNombresInquilino()+"fechainicio="+listaContrato.get(i).getIniciocobro());
		}
		
		}
		if(condicionAutovaluo){
		setearArbitrioSeleccionado();
			}
		else{
			
		}
	}
	 public void setearArbitrioSeleccionado(){
			if(arbitrioBean!=null){
				//inicializarSumaTotalCuotasSeleccion();
				//cuotasPagadas=arbitrioService.obtenerPeriodosPagados(arbitrioBean.getIdarbitrio());
				listaPagos=arbitrioService.obtenerPeriodoxMes(arbitrioBean.getIdarbitrio());
			}
	 }
	//******************* CUOTAS PAGADAS ********************************
	public void obtenerInformacionCuotasPagadasArbitrios(){
		try{
		if(arbitrioBean!=null){
		inicializarSumaTotalCuotasSeleccion();
		cuotasPagadas=arbitrioService.obtenerPeriodosPagados(arbitrioBean.getIdarbitrio());
		listaDetallecuotasCondicion=recaudacionReporteService.obtenerDetalleCuotaPeriodosPendientesxMes(arbitrioBean.getIdarbitrio());
		if(listaDetallecuotasCondicion.size()>0){
			for(int i=0;i<cuotasPagadas.size();i++){
				for(int j=0;j<listaDetallecuotasCondicion.size();j++){
					if(Integer.toString(cuotasPagadas.get(i).getIdcuota()).equals(Integer.toString(listaDetallecuotasCondicion.get(j).getCuota()))){
						if(cuotasPagadas.get(i).getMes().equals(listaDetallecuotasCondicion.get(j).getMes())){
							Double monto=listaDetallecuotasCondicion.get(j).getMonto();
							if(monto>=0){
								cuotasPagadas.get(i).setMonto(FuncionesHelper.redondearNum(monto,2));
							}
							
						}
					}
				}
			}
		}
		}
		}catch(Exception e ){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	//******************* CUOTAS PENDIENTES *****************************
//	public void obtenerInformacionCuotasPendientesArbitrios(){
//		if(arbitrioBean!=null){
//		inicializarSumaTotalCuotasSeleccion();
//		//cuotasPendientes=recaudacionReporteService.obtenerInformacionPagosPendientes(arbitrioBean.getIdarbitrio(),listaPagos,"","pendiente",0.0);
//		cuotasPendientes=arbitrioService.obtenerPeriodosPendientes(arbitrioBean.getIdarbitrio());
//		}
//		
//	}
	public void obtenerInformacionCuotasPendientesArbitrios(){
		try{
		if(arbitrioBean!=null){
			inicializarSumaTotalCuotasSeleccion();
			//ArbitrioBean arbitrioBean=new ArbitrioBean();
			//cuotasPendientes=recaudacionReporteService.obtenerInformacionPagosPendientes(arbitrioBean.getIdarbitrio(),listaPagos,"","pendiente",0.0);
			cuotasPendientes=recaudacionReporteService.obtenerPeriodosPendientesxMes(arbitrioBean.getIdarbitrio());
			listaDetallecuotasCondicion=recaudacionReporteService.obtenerDetalleCuotaPeriodosPendientesxMes(arbitrioBean.getIdarbitrio());
			if(listaDetallecuotasCondicion.size()>0){
				for(int i=0;i<cuotasPendientes.size();i++){
					for(int j=0;j<listaDetallecuotasCondicion.size();j++){
						if(Integer.toString(cuotasPendientes.get(i).getIdcuota()).equals(Integer.toString(listaDetallecuotasCondicion.get(j).getCuota()))){
							if(cuotasPendientes.get(i).getMes().equals(listaDetallecuotasCondicion.get(j).getMes())){
								Double monto=cuotasPendientes.get(i).getMonto()-listaDetallecuotasCondicion.get(j).getMonto();
								if(monto>=0){
									cuotasPendientes.get(i).setMonto(FuncionesHelper.redondearNum(monto,2));
								}
								
							}
						}
					}
				}
			}
		}
		}catch(Exception e ){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void determinarSumaTotalCuotasPagadasSeleccion(){
		inicializarSumaTotalCuotasSeleccion();
		
		for (PeriodoPagadoBean periodoPagadoBean : cuotasPagadasSeleccion) {
			
			periodoPagadoBean.setMonto((periodoPagadoBean.getMonto()!=null)? periodoPagadoBean.getMonto():0.0);
			periodoPagadoBean.setMora((periodoPagadoBean.getMora()!=null)? periodoPagadoBean.getMora():0.0);
			totalMontoCuotasPagadasSeleccion=totalMontoCuotasPagadasSeleccion+periodoPagadoBean.getMonto();	
			totalMoraCuotasPagadasSeleccion=totalMoraCuotasPagadasSeleccion+periodoPagadoBean.getMora();
		}
		totalMontoCuotasPagadasSeleccion=FuncionesHelper.redondearNum(totalMontoCuotasPagadasSeleccion,2);
		totalMoraCuotasPagadasSeleccion=FuncionesHelper.redondearNum(totalMoraCuotasPagadasSeleccion,2);
	}
	public void determinarSumaTotalCuotasPendientesSeleccion(){
		inicializarSumaTotalCuotasSeleccion();
		for (CuotaArbitrioBean cuotaArbitrioBean: cuotasPendientesSeleccion) {
			cuotaArbitrioBean.setMora((cuotaArbitrioBean.getMora()!=null)? cuotaArbitrioBean.getMora():0.0);
			totalMontoCuotasPendientesSeleccion=totalMontoCuotasPendientesSeleccion+cuotaArbitrioBean.getMonto();	
			totalMoraCuotasPendientesSeleccion=totalMoraCuotasPendientesSeleccion+cuotaArbitrioBean.getMora();
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
	public void buscarUpaArbitrio(){
		listaAutovaluoArbitrio=arbitrioService.obtenerListaArbitrioBeanxUpa(claveUpaArbitrio);
		for(int i=0;i<listaAutovaluoArbitrio.size();i++){
			System.out.println("Condicion"+i+"="+listaAutovaluoArbitrio.get(i).getCondicion());
			System.out.println("Nombre de Inquilino "+i+"="+listaAutovaluoArbitrio.get(i).getCondicion());
		}
	}
	public void obtenerListaArbitrioxUpa(){
		//listaAutovaluoArbitrio.clear();
		listaContrato.clear();
		listaContrato=contratoService.obtenerListaContratoUpa();		
		listaAutovaluoArbitrio=arbitrioService.obtenerListaArbitrioBeanxUpa(claveUpaArbitrio);
//		for(int i=0;i<listaAutovaluoArbitrio.size();i++){			
//			String nombreinquilino=buscarArbitrioxcondicion(listaAutovaluoArbitrio.get(i));
//			if(nombreinquilino!=null){
//				listaAutovaluoArbitrio.get(i).setNombreinquilino(nombreinquilino);
//			}
//			listaAutovaluoArbitrio.get(i).setFrecuencia(Constante.FRECUENCIA_ARBITRIO);		
//			}
		
		for(int i =0;i<listaAutovaluoArbitrio.size();i++){
			 for(int k=0;k<listaContrato.size();k++){
				 if(listaAutovaluoArbitrio.get(i).getClave().equals(listaContrato.get(k).getClaveUpa())){
					 listaAutovaluoArbitrio.get(i).setNombreinquilino(listaContrato.get(k).getNombresInquilino()); 
				 }
			 }
			 if(listaAutovaluoArbitrio.get(i).getNombreinquilino()==null){
				 listaAutovaluoArbitrio.get(i).setNombreinquilino(Constante.NO_NOMBRE_INQUILINO);
			 }
			 listaAutovaluoArbitrio.get(i).setFrecuencia(Constante.FRECUENCIA_ARBITRIO);	
		}
		
	}
	//REPORTE X UPA 
	public void elegirReporteTabUpa(String tipoArchivo){

		if (condicionSeleccionado!=null) {
			if (tipoReporteSeleccionadoTabUpa.equals("1")) {
				generarReportePagos("canceladas y pendientes",tipoArchivo);
			}else if (tipoReporteSeleccionadoTabUpa.equals("2")) {
				generarReportePagos("canceladas",tipoArchivo);
			}else if (tipoReporteSeleccionadoTabUpa.equals("4")) {
				generarReportePagos("pendientes",tipoArchivo);
//			}else if (tipoReporteSeleccionadoTabUpa.equals("4")){
//				generarReportePagos("ratio",tipoArchivo);
			}else {
				generarReportePagos("ratio",tipoArchivo);
			}
			
		} else {
			addWarnMessage("Seleccionar item", "Debe seleccionar al menos una condición para crear el reporte");
		}
		
			
	}
	public void verificarTipoOpcionElegidaReporteUpa(){
		
	System.out.println("verificarTipoOpcionElegidaReporteUpa()");
	}
	public void generarReportePagos(String tipoReporte,String tipoArchivo) {
		   condicionSeleccionado.setClaveUpa(condicionSeleccionado.getClave());
		   condicionSeleccionado.setMoneda(Constante.MONEDA_SOLES);
		   String  reporte= "";
		   int nroacuenta = 0,nrocancelado = 0,nropendiente = 0;
		   Double totaldeuda = 0.0,totalcancelado = 0.0,totalmora=0.0,totalmc=0.0,totaligv=0.0,totalnuevarenta=0.0;
		   List<ArbitrioBean> listaCondicionBean= new ArrayList<ArbitrioBean>();
		   List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
		   List<RentaBean> listaRenta=new ArrayList<RentaBean>();			   
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   /*GENERA LA LISTA DE PENDIENTES, CANCELADAS, ratio(LIQUIDACIONES CON MORA)*/
		           if (tipoReporte.equals("pendientes")) {
					lista=arbitrioService.obtenerPeriodosPendientes(condicionSeleccionado.getIdarbitrio());
					}else if(tipoReporte.equals("canceladas")){
						lista=arbitrioService.obtenerPeriodosCancelados(condicionSeleccionado.getIdarbitrio());
					}else if ((tipoReporte.equals("ratio"))){
//						lista=test(lista, condicionSeleccionado.getFincobro());
//						lista=incluirMoraCuotasNoCanceladas(condicionSeleccionado.getMoneda(),lista);
//						lista=seleccionarRentaDeuda(lista);   //Selecciona la lista de pendientes
						lista=arbitrioService.obtenerListaPeriodosPendientes(condicionSeleccionado.getIdarbitrio());
//						lista=arbitrioService.obtenerPeriodosCancelados(arbitrioBean.getIdarbitrio());
//						lista.addAll(arbitrioService.obtenerPeriodosPendientes(condicionSeleccionado.getIdarbitrio()));
					}
		           condicionSeleccionado.setCuotas(lista);
		           listaCondicionBean.add(condicionSeleccionado);
		           reporte= tipoReporte.equals("ratio")?ConstantesReporteSGI.REP_LIQUIDACION_RATIO_ARBITRIO:(tipoReporte.equals("canceladas")?ConstantesReporteSGI.REP_ARBITRIO_CANCELADOS:(tipoReporte.equals("pendientes")?ConstantesReporteSGI.REP_LIQUIDACION_RATIO_ARBITRIO:ConstantesReporteSGI.REP_LIQUIDACION_PENDIENTES_CANCELADAS));
		           for(CuotaArbitrioBean cuotaArbitriobean:lista){
		      		 totaldeuda=totaldeuda+	cuotaArbitriobean.getMonto();
					// System.out.println("totaldeuda: "+totaldeuda);
					 cuotaArbitriobean.setRentapagada(cuotaArbitriobean.getRentapagada()!=null? cuotaArbitriobean.getRentapagada():0.0);
					 cuotaArbitriobean.setMora(cuotaArbitriobean.getMora()!=null? cuotaArbitriobean.getMora():0.0);
					 cuotaArbitriobean.setNuevamc(cuotaArbitriobean.getNuevamc()!=null? cuotaArbitriobean.getNuevamc():0.0);
					 cuotaArbitriobean.setIgv(cuotaArbitriobean.getIgv()!=null? cuotaArbitriobean.getIgv(): 0.0);
					 cuotaArbitriobean.setNuevarenta(cuotaArbitriobean.getMonto()-cuotaArbitriobean.getRentapagada());
					 cuotaArbitriobean.setNuevarenta(cuotaArbitriobean.getNuevarenta()!=null? cuotaArbitriobean.getNuevarenta():0.0);
					 totalcancelado=totalcancelado+cuotaArbitriobean.getRentapagada();
					 //System.out.println("totalcancelado: "+totalcancelado);
					 totalmora=totalmora+cuotaArbitriobean.getMora();
					 totalmc=totalmc+cuotaArbitriobean.getSubtotal();
					 ///System.out.println("totalmc: "+totalmc);
					 totaligv=totaligv+cuotaArbitriobean.getIgv();
					 //System.out.println("totaligv: "+totaligv);
					 totalnuevarenta=totalnuevarenta+cuotaArbitriobean.getNuevarenta();
					 //System.out.println("totalnuevarenta: "+totalnuevarenta);

//					 if (!cuotabean.getSipagado()) {
//						 nropendiente++;
//					 }
					 if (cuotaArbitriobean.getSipagado().equals("0")) {     //Cuota pendiente
						 nropendiente++;
					 }
//					 if (cuotabean.getSipagado()) {
//						 nrocancelado++;
//					 }
					 if (cuotaArbitriobean.getSipagado().equals("1")) {     //cuota cancelada
						 nrocancelado++;
					 }
					 if (cuotaArbitriobean.getSipagado().equals("2")){     // cuota generada
							nropendiente++;
						 }
					 if (cuotaArbitriobean.getSiacuenta()){
						nroacuenta++;
					 }	
		        	   
		           }
		           
//	 				
			  if (tipoArchivo.equals("pdf")) {
				
				   
				   parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				   parameters.put("TIPOCAMBIO",tipcambio); //PARA MODIFICACION TIPO CAMBIO
				   parameters.put("NROCANCELADO",nrocancelado );
				   parameters.put("NROPENDIENTE",nropendiente);
				   parameters.put("NROACUENTA",nroacuenta);
				   parameters.put("TOTALCANCELADO",totalcancelado);
				   parameters.put("TOTALDEUDA",totaldeuda);
				   parameters.put("TOTALMORA", totalmora); //franco
				   parameters.put("TOTALMC", totalmc);   
				   parameters.put("TOTALIGV", totaligv);
				   parameters.put("TOTALNUEVARENTA", totalnuevarenta);
				   parameters.put("SIREGISTRADEUDA", lista.size()==0?"FALSE":"TRUE");
				   
				   

				   report = reporteGeneradorService.generarPDF(reporte, parameters, listaCondicionBean,"Reporte_"+tipoReporte+"_"+FuncionesHelper.fechaToString(new Date()));
					   
			  } else {
				  		System.out.println("excel");
			  }
						
			  
		}

	public void generarReporteArbitrioPendiente(String tipoarchivo) {
		listaContrato.clear();
	    String estado="Pendiente";
		List<ArbitrioBean> listaArbitrioBean=new ArrayList<ArbitrioBean>();
		List<ArbitrioBean> listaCuotasPagadas=new ArrayList<ArbitrioBean>();
		 String  reportPath=  ConstantesReporteSGI.REP_ARBITRIOS_PENDIENTES;// reporte jrxml
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   
		   listaContrato=contratoService.obtenerListaContratoUpa();
		   listaArbitrioBean = arbitrioService.obtenerListaxCondicion("PENDIENTE","todos");
		   listaCuotasPagadas= arbitrioService.obtenerListaxCondicionCuotasPagadas("CANCELADO");
			
			for(int i =0;i<listaArbitrioBean.size();i++){
				 for(int k=0;k<listaContrato.size();k++){
					 if(listaArbitrioBean.get(i).getClave().equals(listaContrato.get(k).getClaveUpa())){
						 listaArbitrioBean.get(i).setNombreinquilino(listaContrato.get(k).getNombresInquilino()); 
					 }
				 }
				 if(listaArbitrioBean.get(i).getNombreinquilino()==null){
					 listaArbitrioBean.get(i).setNombreinquilino(Constante.NO_NOMBRE_INQUILINO);
				 }
				if(listaCuotasPagadas!=null){
					for(int j=0;j<listaCuotasPagadas.size();j++){
						if(listaArbitrioBean.get(i).getClave().equals(listaCuotasPagadas.get(j).getClave())&&(listaArbitrioBean.get(i).getIdarbitrio()==listaCuotasPagadas.get(j).getIdarbitrio())&&(Integer.toString(listaArbitrioBean.get(i).getPeriodo()).equals(Integer.toString(listaCuotasPagadas.get(j).getPeriodo())))){
							listaArbitrioBean.get(i).setTotalpagado(listaCuotasPagadas.get(j).getTotalpagado());
							listaArbitrioBean.get(i).setTotalpendiente(listaArbitrioBean.get(i).getTotal()-(listaCuotasPagadas.get(j).getTotalpagado()));
						}
					}
				}
			}
			
			parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			parameters.put("CONDICION","ARBITRIOS PENDIENTES AL "+FuncionesHelper.fechaToString(new Date()));
		
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaArbitrioBean,"ReporteArbitrio_Pendientes_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_ReportePendientesCancelados(listaArbitrioBean,estado);
		}
	   
	}
	public void generarReporteArbitrioCancelado(String tipoarchivo) {
	    String estado="Cancelado";
	    listaContrato.clear();
		List<ArbitrioBean> listaArbitrioBean=new ArrayList<ArbitrioBean>();
		List<ArbitrioBean> listaCuotasPagadas=new ArrayList<ArbitrioBean>();
		 String  reportPath=  ConstantesReporteSGI.REP_ARBITRIOS_CANCELADOS;// reporte jrxml
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   
		    listaContrato=contratoService.obtenerListaContratoUpa();		
			listaArbitrioBean = arbitrioService.obtenerListaxCondicion("CANCELADO","todos");
			//listaCuotasPagadas= arbitrioService.obtenerListaxCondicionCuotasPagadas("CANCELADO");
			
			for(int i =0;i<listaArbitrioBean.size();i++){
				 for(int k=0;k<listaContrato.size();k++){
					 if(listaArbitrioBean.get(i).getClave().equals(listaContrato.get(k).getClaveUpa())){
						 listaArbitrioBean.get(i).setNombreinquilino(listaContrato.get(k).getNombresInquilino()); 
					 }
				 }
				 if(listaArbitrioBean.get(i).getNombreinquilino()==null){
					 listaArbitrioBean.get(i).setNombreinquilino(Constante.NO_NOMBRE_INQUILINO);
				 }
			}
			
			parameters.put("USUARIOCREADOR", userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
			parameters.put("CONDICION","ARBITRIOS CANCELADOS AL "+FuncionesHelper.fechaToString(new Date()));
		
   
	   if (tipoarchivo.equals("pdf")) {
		   report = reporteGeneradorService.generarPDF(reportPath, parameters, listaArbitrioBean,"ReporteArbitrio_Cancelados_"+FuncionesHelper.fechaToString(new Date()));  
		} else {
			XLS_ReportePendientesCancelados(listaArbitrioBean,estado);
		}
	   
	}
	public void XLS_ReportePendientesCancelados(List<ArbitrioBean> lista,String estado){

		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet;
		if(estado.equals("Pendiente")){
			 sheet = workbook.createSheet("Reporte Pendientes");
		}else{
			 sheet = workbook.createSheet("Reporte Cancelados");
		}
        sheet.setColumnWidth(0, 3000); 
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 4500);
        sheet.setColumnWidth(6, 4500);
        sheet.setColumnWidth(8, 4500);
        sheet.setColumnWidth(9, 4500);
        
        /**Insercion de cabecera (rownum=0) **/
        CellStyle csBold = null;
        
        Font bold = workbook.createFont();
        bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        bold.setFontHeightInPoints((short) 10);
        
        csBold = workbook.createCellStyle();
        csBold.setBorderBottom(CellStyle.BORDER_THIN);
        csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        csBold.setFont(bold);
        if(estado.equals("Pendiente")){
        String[] headers = new String[] { "CLAVE", "DIRECCION","NOMBRES/RAZÓN SOCIAL","PERIODO","TOTAL","TOTAL PAGADO","TOTAL PENDIENTE"};

        Row r = sheet.createRow(0);
        for (int rn=0; rn<headers.length; rn++) {
           Cell cell = r.createCell(rn);
           cell.setCellValue(headers[rn]);
           cell.setCellStyle(csBold);
        }
        int rownum = 1;
        for (int i = 0; i < lista.size(); i++) {
            Row row = sheet.createRow(rownum++);
            ArbitrioBean bean= new ArbitrioBean();
            bean= lista.get(i);
                
                for (int j = 0; j < 7; j++) {
                	Cell cell = row.createCell(j);
                	if (j==0)
                		cell.setCellValue((String)bean.getClave());
                	 else if (j==1)
                		 cell.setCellValue((String)bean.getDireccion());
                		 else if (j==2)
                			 cell.setCellValue((String)bean.getNombreinquilino());
                			 else if (j==3) 
                	            cell.setCellValue((Integer)bean.getPeriodo());
                			     else if (j==4)
                			    	 cell.setCellValue((Double)bean.getTotal());
	                				 else if (j==5)	                					 	
	                					 cell.setCellValue((Double)bean.getTotalpagado());               					 	
	                		 				else if (j==6)
	                		 					cell.setCellValue((Double)bean.getTotalpendiente()); 	                				 						
                }
		}
        }else{
        	String[] headers = new String[] { "CLAVE", "DIRECCION","NOMBRES/RAZÓN SOCIAL","PERIODO","TOTAL","OBSERVACION"};

            Row r = sheet.createRow(0);
            for (int rn=0; rn<headers.length; rn++) {
               Cell cell = r.createCell(rn);
               cell.setCellValue(headers[rn]);
               cell.setCellStyle(csBold);
            }
            int rownum = 1;
            for (int i = 0; i < lista.size(); i++) {
                Row row = sheet.createRow(rownum++);
                ArbitrioBean bean= new ArbitrioBean();
                bean= lista.get(i);
                    
                    for (int j = 0; j < 6; j++) {
                    	Cell cell = row.createCell(j);
                    	if (j==0)
                    		cell.setCellValue((String)bean.getClave());
                    	 else if (j==1)
                    		 cell.setCellValue((String)bean.getDireccion());
                    		 else if (j==2)
                    			 cell.setCellValue((String)bean.getNombreinquilino());
                    			 else if (j==3) 
                    	            cell.setCellValue((Integer)bean.getPeriodo());
                    			     else if (j==4)
                    			    	 cell.setCellValue((Double)bean.getTotal());
                    			     		else if (j==5)
                    			     			cell.setCellValue((String)bean.getObservacion());         				 						
                    }
    		}
        }
         
        try {
            //FileOutputStream out =  new FileOutputStream(new File("C:\\Contratos.xls"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Excel written successfully..");
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	try {
        		workbook.write(bos);
        	} finally {
        	    bos.close();
        	}
            InputStream stream; 
//          stream = new FileInputStream("c:\\Contratos.xls");
            stream = new ByteArrayInputStream(bos.toByteArray());
            if(estado.equals("Pendiente")){
                report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Arbitrios_Pendientes"+FuncionesHelper.fechaToString(new Date())+".xls");

            }else{
                report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Arbitrios_Cancelados"+FuncionesHelper.fechaToString(new Date())+".xls");

            }
            //report= new DefaultStreamedContent(stream,  Constantes.APPLICATION_XLS, "Reporte_Arbitrios_Pendientes"+FuncionesHelper.fechaToString(new Date())+".xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	} 
	public void loadInformationUPA() {
		listaARBITRIO=arbitrioService.listarArbitriosxAnio(upaBean.getIdupa());
	}
	
	public void buscarComprobantesPeriodo(int idcuota) {
		listaComprobantePeriodo=arbitrioService.buscarComprobantesPeriodo(idcuota);
	}
	
	public String getClaveUpaArbitrio() {
		return claveUpaArbitrio;
	}
	public void setClaveUpaArbitrio(String claveUpaArbitrio) {
		this.claveUpaArbitrio = claveUpaArbitrio;
	}

	public List<UpaBean> getListaUPA() {
		return listaUPA;
	}

	public void setListaUPA(List<UpaBean> listaUPA) {
		this.listaUPA = listaUPA;
	}

	public List<UpaBean> getListaUPAFiltered() {
		return listaUPAFiltered;
	}

	public void setListaUPAFiltered(List<UpaBean> listaUPAFiltered) {
		this.listaUPAFiltered = listaUPAFiltered;
	}

	public List<ArbitrioBean> getListaARBITRIO() {
		return listaARBITRIO;
	}

	public void setListaARBITRIO(List<ArbitrioBean> listaARBITRIO) {
		this.listaARBITRIO = listaARBITRIO;
	}

	public List<ArbitrioBean> getListaARBITRIOFiltered() {
		return listaARBITRIOFiltered;
	}

	public void setListaARBITRIOFiltered(List<ArbitrioBean> listaARBITRIOFiltered) {
		this.listaARBITRIOFiltered = listaARBITRIOFiltered;
	}

	public List<DetalleArbitrioBean> getListaDETALLEARBITRIO() {
		return listaDETALLEARBITRIO;
	}

	public void setListaDETALLEARBITRIO(
			List<DetalleArbitrioBean> listaDETALLEARBITRIO) {
		this.listaDETALLEARBITRIO = listaDETALLEARBITRIO;
	}

	public int getAnioseleccionado() {
		return anioseleccionado;
	}

	public void setAnioseleccionado(int anioseleccionado) {
		this.anioseleccionado = anioseleccionado;
	}

	public UpaBean getUpaBean() {
		return upaBean;
	}

	public void setUpaBean(UpaBean upaBean) {
		this.upaBean = upaBean;
	}
	public IAutovaluoArbitrioService getArbitrioService() {
		return arbitrioService;
	}

	public void setArbitrioService(
			IAutovaluoArbitrioService arbitrioService) {
		this.arbitrioService = arbitrioService;
	}
	public UsuarioManagedBean getUsuarioMB() {
		return usuarioMB;
	}

	public void setUsuarioMB(UsuarioManagedBean usuarioMB) {
		this.usuarioMB = usuarioMB;
	}
	public IUpaService getUpaService() {
		return upaService;
	}

	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public List<ArbitrioBean> getListaAutovaluoArbitrio() {
		return listaAutovaluoArbitrio;
	}

	public void setListaAutovaluoArbitrio(List<ArbitrioBean> listaAutovaluoArbitrio) {
		this.listaAutovaluoArbitrio = listaAutovaluoArbitrio;
	}

	public ArbitrioBean getArbitrioBean() {
		return arbitrioBean;
	}

	public void setArbitrioBean(ArbitrioBean arbitrioBean) {
		this.arbitrioBean = arbitrioBean;
	}

	public IContratoService getContratoService() {
		return contratoService;
	}

	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}

	public List<PagoArbitrioBean> getListaPagos() {
		return listaPagos;
	}

	public void setListaPagos(List<PagoArbitrioBean> listaPagos) {
		this.listaPagos = listaPagos;
	}

	public List<PeriodoPagadoBean> getCuotasPagadas() {
		return cuotasPagadas;
	}

	public void setCuotasPagadas(List<PeriodoPagadoBean> cuotasPagadas) {
		this.cuotasPagadas = cuotasPagadas;
	}

	public List<PeriodoPagadoBean> getCuotasPagadasSeleccion() {
		return cuotasPagadasSeleccion;
	}

	public void setCuotasPagadasSeleccion(
			List<PeriodoPagadoBean> cuotasPagadasSeleccion) {
		this.cuotasPagadasSeleccion = cuotasPagadasSeleccion;
	}

	public List<PeriodoPagadoBean> getCuotasPagadasFilter() {
		return cuotasPagadasFilter;
	}

	public void setCuotasPagadasFilter(List<PeriodoPagadoBean> cuotasPagadasFilter) {
		this.cuotasPagadasFilter = cuotasPagadasFilter;
	}

	public double getTotalMontoCuotasPagadasSeleccion() {
		return totalMontoCuotasPagadasSeleccion;
	}

	public void setTotalMontoCuotasPagadasSeleccion(
			double totalMontoCuotasPagadasSeleccion) {
		this.totalMontoCuotasPagadasSeleccion = totalMontoCuotasPagadasSeleccion;
	}

	public double getTotalMontoCuotasPendientesSeleccion() {
		return totalMontoCuotasPendientesSeleccion;
	}

	public void setTotalMontoCuotasPendientesSeleccion(
			double totalMontoCuotasPendientesSeleccion) {
		this.totalMontoCuotasPendientesSeleccion = totalMontoCuotasPendientesSeleccion;
	}

	public double getTotalMoraCuotasPagadasSeleccion() {
		return totalMoraCuotasPagadasSeleccion;
	}

	public void setTotalMoraCuotasPagadasSeleccion(
			double totalMoraCuotasPagadasSeleccion) {
		this.totalMoraCuotasPagadasSeleccion = totalMoraCuotasPagadasSeleccion;
	}

	public double getTotalMoraCuotasPendientesSeleccion() {
		return totalMoraCuotasPendientesSeleccion;
	}

	public void setTotalMoraCuotasPendientesSeleccion(
			double totalMoraCuotasPendientesSeleccion) {
		this.totalMoraCuotasPendientesSeleccion = totalMoraCuotasPendientesSeleccion;
	}

	public boolean isSipersonanatural() {
		return sipersonanatural;
	}

	public void setSipersonanatural(boolean sipersonanatural) {
		this.sipersonanatural = sipersonanatural;
	}

	public List<CuotaArbitrioBean> getCuotasPendientes() {
		return cuotasPendientes;
	}

	public void setCuotasPendientes(List<CuotaArbitrioBean> cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	public List<CuotaArbitrioBean> getCuotasPendientesSeleccion() {
		return cuotasPendientesSeleccion;
	}

	public void setCuotasPendientesSeleccion(
			List<CuotaArbitrioBean> cuotasPendientesSeleccion) {
		this.cuotasPendientesSeleccion = cuotasPendientesSeleccion;
	}

	public List<CuotaArbitrioBean> getCuotasPendientesFilter() {
		return cuotasPendientesFilter;
	}

	public void setCuotasPendientesFilter(
			List<CuotaArbitrioBean> cuotasPendientesFilter) {
		this.cuotasPendientesFilter = cuotasPendientesFilter;
	}

	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}

	public void setRecaudacionReporteService(
			IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}

	public List<ComprobanteBean> getListaComprobantePeriodo() {
		return listaComprobantePeriodo;
	}

	public void setListaComprobantePeriodo(
			List<ComprobanteBean> listaComprobantePeriodo) {
		this.listaComprobantePeriodo = listaComprobantePeriodo;
	}

	public Date getDesdeUpa() {
		return desdeUpa;
	}

	public void setDesdeUpa(Date desdeUpa) {
		this.desdeUpa = desdeUpa;
	}

	public Date getHastaUpa() {
		return hastaUpa;
	}

	public void setHastaUpa(Date hastaUpa) {
		this.hastaUpa = hastaUpa;
	}

	public void setdesdeupa() {
		hastaUpa=desdeUpa;
	}

	public ArbitrioBean getCondicionSeleccionado() {
		return condicionSeleccionado;
	}

	public void setCondicionSeleccionado(ArbitrioBean condicionSeleccionado) {
		this.condicionSeleccionado = condicionSeleccionado;
	}

	public List<CondicionBean> getListaContrato() {
		return listaContrato;
	}

	public void setListaContrato(List<CondicionBean> listaContrato) {
		this.listaContrato = listaContrato;
	}

	public String getTipoReporteSeleccionadoTabUpa() {
		return tipoReporteSeleccionadoTabUpa;
	}

	public void setTipoReporteSeleccionadoTabUpa(
			String tipoReporteSeleccionadoTabUpa) {
		this.tipoReporteSeleccionadoTabUpa = tipoReporteSeleccionadoTabUpa;
	}

	public StreamedContent getReport() {
		return report;
	}

	public void setReport(StreamedContent report) {
		this.report = report;
	}

	public IReporteGeneradorService getReporteGeneradorService() {
		return reporteGeneradorService;
	}

	public void setReporteGeneradorService(
			IReporteGeneradorService reporteGeneradorService) {
		this.reporteGeneradorService = reporteGeneradorService;
	}

	public Date getDesdeRepPendiente() {
		return desdeRepPendiente;
	}

	public void setDesdeRepPendiente(Date desdeRepPendiente) {
		this.desdeRepPendiente = desdeRepPendiente;
	}

	public Date getHastaRepPendiente() {
		return hastaRepPendiente;
	}

	public void setHastaRepPendiente(Date hastaRepPendiente) {
		this.hastaRepPendiente = hastaRepPendiente;
	}

	public Date getDesdeRepCancelado() {
		return desdeRepCancelado;
	}

	public void setDesdeRepCancelado(Date desdeRepCancelado) {
		this.desdeRepCancelado = desdeRepCancelado;
	}

	public Date getHastaRepCancelado() {
		return hastaRepCancelado;
	}

	public void setHastaRepCancelado(Date hastaRepCancelado) {
		this.hastaRepCancelado = hastaRepCancelado;
	}
	
	public Date getDesdeRepPendienteCancelado() {
		return desdeRepPendienteCancelado;
	}

	public void setDesdeRepPendienteCancelado(Date desdeRepPendienteCancelado) {
		this.desdeRepPendienteCancelado = desdeRepPendienteCancelado;
	}

	public Date getHastaRepPendienteCancelado() {
		return hastaRepPendienteCancelado;
	}

	public void setHastaRepPendienteCancelado(Date hastaRepPendienteCancelado) {
		this.hastaRepPendienteCancelado = hastaRepPendienteCancelado;
	}

	public void setdesdeRepPendiente() {
		hastaRepPendiente=desdeRepPendiente;
	}
	public void setdesdeRepCancelado() {
		hastaRepCancelado=desdeRepCancelado;
	}
	public void setdesdeRepPendienteCancelado() {
		hastaRepPendienteCancelado=desdeRepPendienteCancelado;
	}
	
}
