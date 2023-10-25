package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.constants.sgi.ConstantesReporteSGI;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecartera;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Tipoconcepto;
import pe.gob.sblm.sgi.entity.Tipodocu;
import pe.gob.sblm.sgi.entity.Tipomodalidadpago;
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.IAutovaluoArbitrioService;
import pe.gob.sblm.sgi.service.IFHvariadoService;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.service.IReporteGeneradorService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;
import pe.gob.sblm.sgi.util.PropiedadesBaseDatos;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name ="autovaluoCobranzaMB")
@ViewScoped
public class AutovaluoCobranzaManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger Logger=LoggerFactory.getLogger(AutovaluoCobranzaManagedBean.class);
	
    private int nrocuotascanceladas;
    private int nrocuotaPendienteCancelacion;
	private double valorTipoCambio;
	private String condicion;
	private int iddetallecartera;
	private BigDecimal montoDetraccion;
	private BigDecimal totalSubTotalComprobante;
	private BigDecimal igvComprobante;
	private BigDecimal totalComprobante;
	private Double totalmora =0.0;
	private List<ArbitrioBean> listaAutovaluoArbitrio=new ArrayList<ArbitrioBean>();
	private String claveupa;
	private ItemBean selectedCobrador;
	private String tipodocumentoSeleccionado;
	private String idtipodocuseleccionado,idtipomovimientoseleccionado,idtipoconceptoseleccionado,idtipopagoseleccionado;
	private String selectedTipoDocumento;
	private String serieFacturacionFisica;
	private String correlativoFacturacionFisica;
	private boolean sigarantia ,sicobrador=false, siperfilcobrador=false;
	private boolean sifacturacionelectronica;
	private List<CuotaArbitrioBean> listaCuotasPendientesCondicion = new ArrayList<CuotaArbitrioBean>();
	private List<CuotaArbitrioBean> listaCuotasCondicion = new ArrayList<CuotaArbitrioBean>();
	private List<CuotaArbitrioBean> listaDetallecuotasCondicion = new ArrayList<CuotaArbitrioBean>();
	private List<CuotaArbitrioBean> listaDetallecuotaspagados = new ArrayList<CuotaArbitrioBean>();
	private List<CuotaArbitrioBean> listaCuotasPendientesCondicionCancelar = new ArrayList<CuotaArbitrioBean>();
	private List<CuotaArbitrioBean> cuotasPendientesSeleccion,listaCuotasPendientesCondicionCancelarFilter;
	private List<PeriodoPagadoBean> listacuotasPagadas;
	private List<PagoArbitrioBean> listacuotasxMes;
	private Date minFechaCancelacion,maxFechaCancelacion;
	// Comprobante
    private Comprobantepago comprobantepago = new Comprobantepago();
    private CobranzaManagedBean cobranzabean = new CobranzaManagedBean();
    private ArbitrioBean condicionSeleccionado;
    private CuotaArbitrioBean cuotaPendienteCondicion;
    private List<Tipopago> tipopagos;
    private List<Tipodocu> listaTipoDocumentos;
    private List<Tipomovimiento> tipomovimientos;
    private Date minfechacancelacion=new Date(); 
    private String idtipomodalidadpago;
    private Boolean habilitarDocDNI=Boolean.TRUE,habilitarDocRUC=Boolean.TRUE;
	private Integer longDocuPersona;
	
    @ManagedProperty(value = "#{FHvariadoService}")
	private transient  IFHvariadoService FHvariadoService;
	@ManagedProperty(value = "#{autovaluoarbitrioService}")
	private transient IAutovaluoArbitrioService arbitrioService;
	@ManagedProperty(value = "#{carteraService}")
	private transient IRecaudacionCarteraService carteraService;
	@ManagedProperty(value ="#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{reporteGeneradorService}")
	private transient IReporteGeneradorService reporteGeneradorService;
	@ManagedProperty(value = "#{administrarArchivoService}")
	private transient AdministrarArchivoService administrarArchivoService;
    @PostConstruct
    public void initObjects(){
    	tipopagos=getCarteraService().listartipopagos();
    	
    }
public void obtenerListaArbitrioxUpa(){
	try{
	listaAutovaluoArbitrio=arbitrioService.obtenerListaArbitrioBeanxUpa(claveupa);
	}catch(Exception e ){
		addErrorMessage("",Constantes.MSG_GENERO_ERROR_OPERACION);
		e.printStackTrace();
		Logger.error(e.getMessage(),e);
		addWarnMessage("No existe upa", "La upa ingresado no esta registrada en el sistema");
	}
	
	 
 }
//**  *************recibo de caja de arbitrio*********************   */
public void elegirTipoFacturacion(String tipodoc){
	tipodocumentoSeleccionado=tipodoc;
	selectedTipoDocumento=setNombreSelectedTipoDocumento(tipodoc);
	if(condicionSeleccionado==null){
		addWarnMessage("Seleccionar una upa","No se ha seleccionado ninguna upa , seleccione una upa para poder agregar el pago");

	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
		verificarPermisoCobro("");
	}
	
//	if(condicionSeleccionado==null && tipodoc.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
//		verificarPermisoCobro("");
//	}else if(condicionSeleccionado==null){
//		addWarnMessage("Seleccionar una upa","No se ha seleccionado ninguna upa , seleccione una upa para poder agregar el pago");
//	}else{
//		if(tipodoc.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
//		verificarPermisoCobro("");
//		}
//	}
}
public void verificarPermisoCobro(String tipofacturacion){
	RequestContext contextRequest = RequestContext.getCurrentInstance();
	valorTipoCambio=0.0;
	if (tipodocumentoSeleccionado.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)) {
		idtipodocuseleccionado=tipodocumentoSeleccionado;
		idtipomovimientoseleccionado="03";
//		idtipoconceptoseleccionado="03";		
		sigarantia=true;
		cargarInformacionArbitrioSeleccionado();
		comprobantepago = new Comprobantepago();
		inicializarTotales();
		contextRequest.execute("dlgRegistroReciboCajaRA.show();");
		
	}
}
//******************* CUOTAS PAGADAS ********************************
public void obtenerInformacionCuotasPagadasArbitrios(){
	//inicializarSumaTotalCuotasSeleccion();
	listacuotasPagadas=arbitrioService.obtenerPeriodosPagados(condicionSeleccionado.getIdarbitrio());
	
}
//******************* CUOTAS PENDIENTES *****************************
//public void obtenerInformacionCuotasPendientesArbitrios(){
//	if(arbitrioBean!=null){
//	inicializarSumaTotalCuotasSeleccion();
//	//cuotasPendientes=recaudacionReporteService.obtenerInformacionPagosPendientes(arbitrioBean.getIdarbitrio(),listaPagos,"","pendiente",0.0);
//	cuotasPendientes=arbitrioService.obtenerPeriodosPendientes(arbitrioBean.getIdarbitrio());
//	}
//	
//}
/** cargar Informacion de recibo de caja para arbitrio   */
public void cargarInformacionArbitrioSeleccionado(){
	try{
		listaDetallecuotasCondicion.clear();
		ArbitrioBean arbitrioBean=new ArbitrioBean();
		limpiarListaCuotasPendientesCondicionCancelar();
		nrocuotaPendienteCancelacion = 0;
		inicializandoValores();
		condicion=condicionSeleccionado.getEstado();
		arbitrioBean=arbitrioService.obtenerArbitrioPorCondicion(condicionSeleccionado.getIdarbitrio());
		//obtenerInformacionCuotasPagadasArbitrios();
		//listaCuotasPendientesCondicion=recaudacionReporteService.obtenerInformacionPagosPendientes(arbitrioBean,valorTipoCambio,listacuotasPagadas);
		listaCuotasPendientesCondicion=recaudacionReporteService.obtenerPeriodosPendientesxMes(arbitrioBean.getIdarbitrio());
		listaDetallecuotasCondicion=recaudacionReporteService.obtenerDetalleCuotaPeriodosPendientesxMes(arbitrioBean.getIdarbitrio());
		if(listaDetallecuotasCondicion.size()>0){
			for(int i=0;i<listaCuotasPendientesCondicion.size();i++){
				for(int j=0;j<listaDetallecuotasCondicion.size();j++){
					if(Integer.toString(listaCuotasPendientesCondicion.get(i).getIdcuota()).equals(Integer.toString(listaDetallecuotasCondicion.get(j).getCuota()))){
						if(listaCuotasPendientesCondicion.get(i).getMes().equals(listaDetallecuotasCondicion.get(j).getMes())){
							Double monto=listaCuotasPendientesCondicion.get(i).getMonto()-listaDetallecuotasCondicion.get(j).getMonto();
							if(monto>=0){
								listaCuotasPendientesCondicion.get(i).setMonto(FuncionesHelper.redondearNum(monto,2));
							}
							
						}
					}
				}
			}
		}
		//listaCuotasPendientesCondicion=arbitrioService.obtenerPeriodosPendientes(arbitrioBean.getIdarbitrio());
	    //System.out.println("listacuotas pendientes="+listaCuotasPendientesCondicion.toString());
	}catch(Exception e ){
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
}
public void inicializandoValores(){
	Calendar fechaInicio=Calendar.getInstance();
	Calendar fechaFin=Calendar.getInstance();
	
	fechaFin.set(Calendar.DATE, fechaInicio.getActualMaximum(Calendar.DAY_OF_MONTH));
	fechaFin.set(Calendar.HOUR_OF_DAY, 0);
	fechaFin.set(Calendar.MINUTE, 0);
	fechaFin.set(Calendar.SECOND, 0);
	fechaFin.set(Calendar.MILLISECOND, 0);

	fechaInicio.set(Calendar.DATE, 1);	
	
	minFechaCancelacion=fechaInicio.getTime();
	maxFechaCancelacion=fechaFin.getTime();
}

public String setNombreSelectedTipoDocumento(String tipodoc){
	 if(tipodoc.equals(Constante.TIPO_DOCUMENTO_SIN_VALOR_LEGAL)){
		 return Constante.TIPO_DOCUMENTO_00;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_FACTURA)){
		return Constante.TIPO_DOCUMENTO_01;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_BOLETA_VENTA)){
		return Constante.TIPO_DOCUMENTO_03;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
		return Constante.TIPO_DOCUMENTO_04;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_NOTA_DEBITO)){
		return Constante.TIPO_DOCUMENTO_08;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_NOTA_CREDITO)){
		return Constante.TIPO_DOCUMENTO_09;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_DOCUMENTO_ADMINISTRATIVO)){
		return Constante.TIPO_DOCUMENTO_DA;
	}else if(tipodoc.equals(Constante.TIPO_DOCUMENTO_PERIODO_GRACIA)){
		return Constante.TIPO_DOCUMENTO_PG;
	}
    return null;
}
public void  inicializarTotales(){
	
	totalSubTotalComprobante = new BigDecimal("0.0");
	totalComprobante 		 = new BigDecimal("0.0");
	igvComprobante 			 = new BigDecimal("0.0");
	montoDetraccion 		 = new BigDecimal("0.0");
//	totalSubTotalComprobanteN= new BigDecimal("0.0");
//	igvComprobanteN 	     = new BigDecimal("0.0");
//	totalComprobanteN 		 = new BigDecimal("0.0");
//	totalmora				 = 0.0;
//	subtotalnotacredito		 = 0.0;
//	totalnotacredito		 = 0.0;
//	igvnotacredito			 = 0.0;
//	sivisibleReciboCajaRef=Boolean.FALSE;
}
private static Date ponerDiasFechaFinMes(Date fecha){

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(fecha); // Configuramos la fecha que se recibe
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); 
    return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

}
public void buscarTipoCambio() {
	try{
	 if(tipodocumentoSeleccionado!=null){
		 if(tipodocumentoSeleccionado.equals("08")||tipodocumentoSeleccionado.equals("10")){
			 //comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
			 minFechaCancelacion=comprobantepago.getFechaemision();
		 }else{
			 comprobantepago.setFechacancelacion(comprobantepago.getFechaemision());
			// comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
		 }
	 }else{
		 //comprobantepago.setFechavencimiento(ponerDiasFechaFinMes(comprobantepago.getFechaemision()));
	 }

	 limpiarListaCuotasPendientesCondicionCancelar();
	 
	}catch(Exception e ){
		e.printStackTrace();
	}
}
public void compararfechaPagoArbitrio(){
	String resultado="";
	 if ( comprobantepago.getFechaemision().before(comprobantepago.getFechaemision()) ){
		    resultado= "La Fecha fecha emision es menor ";
		    }else{
		     if ( comprobantepago.getFechaemision().before(comprobantepago.getFechaemision()) ){
		      resultado= "La Fecha emsion es Mayor ";
		     }else{
		      resultado= "Las Fechas Son iguales ";
		     } 
		    };
  System.out.println("resultado="+resultado);
}
public void limpiarListaCuotasPendientesCondicionCancelar(){
	 listaCuotasPendientesCondicionCancelar.clear();
}
public List<ItemBean> completarUsuarioCobrador(String texto){
	List<ItemBean> listaUsuariosCobradores= new ArrayList<ItemBean>();
		
	listaUsuariosCobradores = FHvariadoService.obtenerCobradores(texto);
	return listaUsuariosCobradores;
}
public void calcularIgvSubtotal() {
	
	totalSubTotalComprobante.setScale(2,RoundingMode.HALF_UP);
	totalComprobante.setScale(2,RoundingMode.HALF_UP);
	igvComprobante.setScale(2,RoundingMode.HALF_UP);
	montoDetraccion.setScale(2,RoundingMode.HALF_UP);
	
	if (idtipodocuseleccionado.equals("04") || idtipodocuseleccionado.equals("08")) {
		totalSubTotalComprobante =  totalComprobante;
		igvComprobante =   new BigDecimal("0.0");
	} else {
		//totalSubTotalComprobante =  totalComprobante/1.18 , 2);
		totalSubTotalComprobante =  totalComprobante.divide(new BigDecimal("1.18"),2,RoundingMode.HALF_UP);
		//igvComprobante =  FuncionesHelper.redondear((totalComprobante/1.18)*0.18 , 2);
		igvComprobante =  totalComprobante.multiply(new BigDecimal("0.18")).divide(new BigDecimal("1.18"),4,RoundingMode.HALF_UP);
	}
}
public void validacionRegistrarReciboCaja(){
	
	if (idtipodocuseleccionado.equals("0")) {
		addWarnMessage("", "Seleccione Tipo de documento");
	}else if (idtipoconceptoseleccionado.equals("0")) {
		addWarnMessage("", "Seleccione Tipo de concepto");
	} else if (idtipopagoseleccionado.equals("0")) {
		addWarnMessage("", "Seleccione Tipo de pago ");
	} else if (comprobantepago.getFechaemision() == null) {
		addWarnMessage("", "Ingrese Fecha Emisión");
	} else if (comprobantepago.getFechacancelacion() == null) {
		addWarnMessage("", "Ingrese Fecha Cancelacion");
//	}else if (idtipodocuseleccionado.equals("04") && serieFacturacionFisica.equals("")) {
//		addWarnMessage("", "Ingrese Nro Serie");
//	} else if (idtipodocuseleccionado.equals("04") && correlativoFacturacionFisica.equals("")) {
//		addWarnMessage("", "Ingrese Nro Comprobante");
	} else if (idtipodocuseleccionado.equals("04") && idtipomodalidadpago.equals("0")) {
		addWarnMessage("", "Ingrese Forma de pago");
	}else if(comprobantepago.getNombrepagante().equals("")||comprobantepago.getNombrepagante()==null){
			addWarnMessage("", "Ingrese Nombre de Pagante");
	} else if(comprobantepago.getDnirucpagante().equals("")||comprobantepago.getDnirucpagante()==null){
		addWarnMessage("", "Ingrese Nro de documento de la persona");
	} else if (idtipodocuseleccionado.equals("04") && comprobantepago.getIdtipopersona().equals("0")) {
		addWarnMessage("", "Ingrese Tipo de Persona");
	} else if(!validarNumeroDocPersona()){
		addWarnMessage("", "Ingrese un numero de documento de persona valido.");
	}else if((idtipodocuseleccionado.equals("04") && comprobantepago.getObs_direccion().equals(""))||(idtipodocuseleccionado.equals("04") && comprobantepago.getObs_direccion()==null)){
			addWarnMessage("", "Ingrese Dirección");
	}else if (selectedCobrador==null) {
		addWarnMessage("", "Ingrese nombre cobrador");
	}
//	else if (cobranzaRecaudacionService.siregistradocomprobantepago(comprobantepago.getNroserie()+"-"+comprobantepago.getNrocomprobante())) {
//			
//		addErrorMessage("", "Ya existe un recibo de caja con el N° de comprobante registrado en el sistema");
//	}
	else if (totalComprobante.doubleValue()==0) {
		addErrorMessage("","Usted debe ingresar el monto para el comprobante de pago .");
	}else {
		
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		 
		if(idtipodocuseleccionado.equals("04")){
			 contextRequest.execute("dlgConfirmarRegistroReciboCajaArbitrio.show();");
			 
		}else if (sifacturacionelectronica) {
			contextRequest.execute("dlgConfirmarRegistroPagoOtro.show();");
		}else {
			 contextRequest.execute("dlgNumeroDocumentoFacturacionFisica.show();");
		}
	}
	
}
public void validarCabeceraPagoArbitrio(){
	
//	 if (cobranzaRecaudacionService.siExisteComprobanteRegistrado(comprobantepago.getNroserie(),comprobantepago.getNrocomprobante())) {
//		addErrorMessage("Error Registro", "Comprobante de pago no se puede registrar por que que ya se encuentra registrado en el sistema");
//	}
		
		RequestContext contextRequest = RequestContext.getCurrentInstance();
		
//		if (numerooperacion.equals("") && comprobantepago.getFechacancelacion()!=null ) {
//			addWarnMessage("", "Ingrese una número de operación para continuar. ");
//			
//		}else 
//		if (numerooperacion.length()<7 && comprobantepago.getFechacancelacion()!=null ) {
//			addWarnMessage("", "Longitud de número de operación no puede ser menor a 8.");
//			
//		}else if (numerooperacion.length()>11 && comprobantepago.getFechacancelacion()!=null ) {
//			addWarnMessage("", "Longitud de número de operación no puede ser mayor a 11.");
//			
//		}else if (fechaoperacion.equals("") && comprobantepago.getFechacancelacion()!=null ) {
//			addWarnMessage("", "Ingrese una fecha de operación para continuar.");
//		}
		
	/**VALIDACION NUMERO OPERACION**/	
//		else if (cobranzaRecaudacionService.siExisteNumeroOperacion(fechaoperacion+"-"+numerooperacion)) {
//			System.out.println("fechaoperacion+"-"+numerooperacion: "+ fechaoperacion+"-"+numerooperacion)
//			addErrorMessage("", "Número de operación ya se encuentra registrado en el sistema");
//		}
//		else 
		
		if (idtipodocuseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de documento");
			
		}else if (idtipopagoseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de pago");
			
		}else if (idtipomovimientoseleccionado.equals("0")) {
			addWarnMessage("", "Seleccione Tipo de movimiento");
			
		}else if (selectedCobrador==null) {
			addWarnMessage("", "Ingrese nombre cobrador");
			
		}else if (sifacturacionelectronica) {
			
			if (comprobantepago.getFechaemision() == null) {
				addWarnMessage("", "Ingrese Fecha de Emisión");
				
			}else if (comprobantepago.getFechacancelacion()==null && comprobantepago.getFechavencimiento()==null ) {
				addWarnMessage("", "Seleccione fecha de cancelación o fecha de vencimiento");
				
			}else if (comprobantepago.getFechacancelacion()!=null && comprobantepago.getFechavencimiento()!=null ) {
				addWarnMessage("", "Seleccione solo fecha de cancelación o solo fecha de vencimiento");
				
			}else if (comprobantepago.getFechavencimiento()!=null) {
				
//				if (idtipodocuseleccionado.equals("01")) {
//					if (comprobantepago.getFechavencimiento().compareTo(maxFechaVencimiento)!=0) {
//						addWarnMessage("", "Seleccione fin de mes de fecha de vencimiento para continuar.");
//						
//					}else {
						contextRequest.execute("dlgPendientePagoRenta.show();");
						
//					}
					
//				} 
//				else if(idtipodocuseleccionado.equals("03")) {
//					Calendar fecha =Calendar.getInstance();
//					
//					fecha.setTime(comprobantepago.getFechaemision());
//					fecha.add(Calendar.DATE, 6);
//					
//					if (comprobantepago.getFechavencimiento().compareTo(fecha.getTime())>0) {
//						addWarnMessage("", "Seleccione una fecha de cancelación no mayor a 7 días que la fecha de emisión");
//						
//					}else {
//						contextRequest.execute("dlgPendientePagoRenta.show();");
//						
//					}
//
//				}
				
			}else if (comprobantepago.getObservacion().equals("")) {
				addWarnMessage("", "Ingrese una observación para continuar");
				
			} 
//			else if (comprobantepago.getObservacion().length()<10) {
//				addWarnMessage("", "Observación debe tener al menos 10 caracteres");
//				
//			}
			else {
				contextRequest.execute("dlgPendientePagoRenta.show();");
				
			}
			
			
		} else {
			
			if (comprobantepago.getFechaemision() == null) {
				addWarnMessage("", "Ingrese Fecha de Emisión");
				
				
			}else if (comprobantepago.getFechacancelacion() == null) {
				addWarnMessage("", "Ingrese Fecha de Cancelación ");
				
			}else {
				contextRequest.execute("dlgPendientePagoRenta.show();");
			}
		}
		
	}
public void calcularTotales() {
	try{
	BigDecimal suma =new BigDecimal("0.0");
	totalSubTotalComprobante=new BigDecimal("0.0");
	igvComprobante=new BigDecimal("0.0");
	totalComprobante=new BigDecimal("0.0");
	for (CuotaArbitrioBean c : listaCuotasPendientesCondicionCancelar){
		if(idtipodocuseleccionado.equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
			suma=new BigDecimal(c.getMonto());
			Double numsuma=FuncionesHelper.redondearNum(Double.parseDouble(suma.toString()), 2);
		    suma=new BigDecimal(numsuma.toString());
		    totalSubTotalComprobante=totalSubTotalComprobante.add(suma);
		    totalComprobante=totalComprobante.add(suma);
		}
	}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void grabarReciboCaja(){
	try {
		listaCuotasCondicion=recaudacionReporteService.obtenerPeriodosPendientesxMes(condicionSeleccionado.getIdarbitrio());
		totalSubTotalComprobante.setScale(2, RoundingMode.HALF_UP);
		igvComprobante.setScale(2, RoundingMode.HALF_UP);
		totalComprobante.setScale(2, RoundingMode.HALF_UP);
		montoDetraccion.setScale(2, RoundingMode.HALF_UP);
		Tipodocu tipodocu = new Tipodocu(idtipodocuseleccionado);
		Tipomovimiento tipomovimiento = new Tipomovimiento("04");
		Tipoconcepto tipoconcepto= new Tipoconcepto(idtipoconceptoseleccionado);
		Tipopago tipopago = new Tipopago(idtipopagoseleccionado);
		Tipomodalidadpago tipomodalidadpago= new Tipomodalidadpago(idtipomodalidadpago);
		
		Usuario usuario= new Usuario();
		
		usuario.setIdusuario(selectedCobrador.getId());
        
		comprobantepago.setUsuario(usuario);
//		if (sigarantia && condicionSeleccionado!=null) {
//			iddetallecartera = getCarteraService().obtenerIddetalleCarteraAutovaluo(condicionSeleccionado.getIdarbitrio());
//			System.out.println("iddetallecartera="+iddetallecartera);
//			Detallecartera dc = new Detallecartera();
//			dc.setIddetallecartera(iddetallecartera);
//			comprobantepago.setDetallecartera(dc);
//			
//		}else {
			comprobantepago.setDetallecartera(null);
//		}
		comprobantepago.setUsuariocreador(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		comprobantepago.setFechacreacion(new Date());
		//comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
		comprobantepago.setNombreusr_cre(userMB.getNombreusr().toUpperCase());
		comprobantepago.setTipopago(tipopago);
		comprobantepago.setTipomovimiento(tipomovimiento);
		comprobantepago.setTipoconcepto(tipoconcepto);
		comprobantepago.setTipodocu(tipodocu);
		comprobantepago.setTipomodalidadpago(tipomodalidadpago);
		comprobantepago.setMoradetectada(0.0);
		comprobantepago.setNombrecobrador(selectedCobrador.getDescripcion());
		comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
		comprobantepago.setTotalsoles(totalComprobante.doubleValue());
		comprobantepago.setNrocomprobantepadre("");
		comprobantepago.setSianulado(Boolean.FALSE);
		comprobantepago.setTipcambio(0.0);
		comprobantepago.setNumerooperacion("-");
		comprobantepago.setSiautodetraccion(false);
		comprobantepago.setMontoautodetraccion(FuncionesHelper.redondear(0.0, 2));
		comprobantepago.setSirecibocajausado(Boolean.FALSE);
		comprobantepago.setSigeneronotadebito(Boolean.FALSE);
		comprobantepago.setSigeneropenalizacion(Boolean.FALSE);
		comprobantepago.setSigeneronotacredito2(Boolean.FALSE);
		comprobantepago.setMontopenalizacion(0.0);
		comprobantepago.setEstado(Constante.ESTADO_ACT);
		comprobantepago.setCondicion(Constante.ESTADO_CONDICION_ACT);
		if (idtipodocuseleccionado.equals("01") || idtipodocuseleccionado.equals("03")) {
			if (totalComprobante.doubleValue() >= 700 && idtipodocuseleccionado.equals("01")) {
				comprobantepago.setSidetraccion(true);
				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(totalComprobante.doubleValue() * 0.12, 2));
				
			} else {
				comprobantepago.setSidetraccion(false);
				comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			}
			comprobantepago.setSigeneronotacredito(false);
			comprobantepago.setSubtotalsoles(totalSubTotalComprobante.doubleValue());
			comprobantepago.setIgvsoles(igvComprobante.doubleValue());
		} else {
			comprobantepago.setSidetraccion(false);
			comprobantepago.setMontodetraccion(FuncionesHelper.redondear(0.0, 2));
			comprobantepago.setIgvsoles(0.0);
			comprobantepago.setSubtotalsoles(totalComprobante.doubleValue());
			comprobantepago.setSigeneronotacredito(true);
		}
		comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
		/**
		 *  1.- GUARDAR COMPROBANTE PAGO SEGUN EL TIPO DE FACTURACION
		 *  **/
		if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
//			comprobantepago.setNroserie(serieFacturacionFisica);
//			comprobantepago.setNrocomprobante(correlativoFacturacionFisica);
//			comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
			comprobantepago.setSifacturacionelectronica(Boolean.FALSE);
			comprobantepago.setComp_descripcionitem(comprobantepago.getObservacion());
			comprobantepago.setTextoimportetotal(FuncionesHelper.numeroaLetra(comprobantepago.getTotalsoles()));
			System.out.println(idtipoconceptoseleccionado);
			cobranzaRecaudacionService.grabarComprobantePago(comprobantepago);
			//grabarDocumentoPDF(comprobantepago);

		}else {
			comprobantepago.setSifacturacionelectronica(Boolean.TRUE);
			cobranzaRecaudacionService.grabarBoletaFacturaFE(comprobantepago,idtipodocuseleccionado,"CARTERA102");	
		}
		for(int i =0;i<listaCuotasPendientesCondicionCancelar.size();i++){
			listaCuotasPendientesCondicionCancelar.get(i).setSiacuenta(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			if(listaCuotasPendientesCondicionCancelar.get(i).getSiacuenta()){
				Cuota_arbitrio  cuo=new Cuota_arbitrio();
				cuo.setUsrcre(listaCuotasPendientesCondicionCancelar.get(i).getUsrcre());
				cuo.setFeccre(listaCuotasPendientesCondicionCancelar.get(i).getFeccre());
				cuo.setIdcuota_arbitrio(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres()+" "+ userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
//				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setPeriodo(listaCuotasPendientesCondicionCancelar.get(i).getPeriodo());
//				cuo.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto()+listaCuotasPendientesCondicionCancelar.get(i).getDeudaacuenta());
//				cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMoraacumulada()+listaCuotasPendientesCondicionCancelar.get(i).getMora());
				cuo.setTrimestre(listaCuotasPendientesCondicionCancelar.get(i).getTrimestre());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMes())));
				cuo.setNropagos(listaCuotasPendientesCondicionCancelar.get(i).getNropagos()+1);
//						// REGISTRA ID DE COMPROBANTEPAGO EN CUOTAS 
				Comprobantepago cmp=new Comprobantepago();
				cmp.setIdcomprobante(comprobantepago.getIdcomprobante());
				cuo.setComprobantepago(cmp);
				Arbitrio a=new Arbitrio();
				a.setIdarbitrio(condicionSeleccionado.getIdarbitrio());
				cuo.setArbitrio(a);
				cuo.setEstado(Constante.ESTADO_PENDIENTE);
				cuo.setCancelado("0");
//				cuo.setTipocambio(valorTipoCambio);
//				if (listaCuotasPendientesCondicionCancelar.get(i).getEstado().equals("Cancelado")) {
//					cuo.setCancelado("1");
//				} else if (listaCuotasPendientesCondicionCancelar.get(i).getEstado().equals("Pendiente"))  {
//					cuo.setCancelado("1");
//				}else {
//					cuo.setCancelado("2");
//				}
				for(int k=0;k<listaCuotasCondicion.size();k++){					
					if(listaCuotasPendientesCondicionCancelar.get(i).getMes().equals(listaCuotasCondicion.get(k).getMes())){
						cuo.setMonto(listaCuotasCondicion.get(k).getMonto());
					}
				}
				cobranzaRecaudacionService.actualizarCuotaArbitrio(cuo);
				Detallecuota_arbitrio detallecuota = new Detallecuota_arbitrio();
				detallecuota.setCuota_arbitrio(cuo);
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setPeriodo(listaCuotasPendientesCondicionCancelar.get(i).getPeriodo());
				detallecuota.setTrimestre(listaCuotasPendientesCondicionCancelar.get(i).getTrimestre());
				detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMes())));
//				detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				detallecuota.setFeccre(new Date());		
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
				getCarteraService().registrarDetalleCuotaArbitrio(detallecuota);
				//buscar la suma de detalle cuota 			
				listaDetallecuotaspagados=recaudacionReporteService.obtenerDetalleCuotaPeriodosPagadosxMes(condicionSeleccionado.getIdarbitrio());
				if(listaDetallecuotaspagados.size()>0){
					for(int l=0;l<listaDetallecuotaspagados.size();l++){					
						if(cuo.getMes().equals(listaDetallecuotaspagados.get(l).getMes())){
							if(Double.toString(cuo.getMonto()).equals(Double.toString(listaDetallecuotaspagados.get(l).getMonto()))){
								cuo.setEstado(Constante.ESTADO_CANCELADO);
								cuo.setCancelado("1");
								cobranzaRecaudacionService.actualizarCuotaArbitrio(cuo);
							}
						}
					}
				}
			}else{
				Cuota_arbitrio cuo = new Cuota_arbitrio();
				cuo.setUsrcre(listaCuotasPendientesCondicionCancelar.get(i).getUsrcre());
				cuo.setFeccre(listaCuotasPendientesCondicionCancelar.get(i).getFeccre());
				cuo.setIdcuota_arbitrio(listaCuotasPendientesCondicionCancelar.get(i).getIdcuota());
				cuo.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				cuo.setFecmod(new Date());
//				cuo.setCuotanumero(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				cuo.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));

				cuo.setPeriodo(listaCuotasPendientesCondicionCancelar.get(i).getPeriodo());
				cuo.setMonto(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
//				cuo.setMorasoles(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				cuo.setTrimestre(listaCuotasPendientesCondicionCancelar.get(i).getTrimestre());
				cuo.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMes())));
				//cuo.setEstado(Constante.ESTADO_CANCELADO);				
				cuo.setObservacion(comprobantepago.getObservacion());
				cuo.setNropagos(1);
				
				
				Arbitrio a= new Arbitrio();
				a.setIdarbitrio(condicionSeleccionado.getIdarbitrio());
				cuo.setArbitrio(a);
				
				// REGISTRA ID DE COMPROBANTEPAGO EN CUOTAS 
				Comprobantepago cmp=new Comprobantepago();
				cmp.setIdcomprobante(comprobantepago.getIdcomprobante());
				cuo.setComprobantepago(cmp);
				for(int k=0;k<listaCuotasCondicion.size();k++){					
					if(listaCuotasPendientesCondicionCancelar.get(i).getMes().equals(listaCuotasCondicion.get(k).getMes())){
						if(Double.toString(listaCuotasPendientesCondicionCancelar.get(i).getMonto()).equals(Double.toString(listaCuotasCondicion.get(k).getMonto()))){
							cuo.setEstado(Constante.ESTADO_CANCELADO);
							cuo.setCancelado("1");
						}else{
							cuo.setEstado(Constante.ESTADO_PENDIENTE);
							cuo.setCancelado("0");
						}
						cuo.setMonto(listaCuotasCondicion.get(k).getMonto());
					}
				}
				
				cobranzaRecaudacionService.actualizarCuotaArbitrio(cuo);
				
				// Detalle
				Detallecuota_arbitrio detallecuota = new Detallecuota_arbitrio();
				detallecuota.setCuota_arbitrio(cuo);
				
				detallecuota.setComprobantepago(comprobantepago);
				detallecuota.setMes(String.valueOf(listaCuotasPendientesCondicionCancelar.get(i).getMes()));
				detallecuota.setPeriodo(listaCuotasPendientesCondicionCancelar.get(i).getPeriodo());
				detallecuota.setTrimestre(listaCuotasPendientesCondicionCancelar.get(i).getTrimestre());
				detallecuota.setMora(listaCuotasPendientesCondicionCancelar.get(i).getMora());
				detallecuota.setNromes(Integer.parseInt(Almanaque.mesanumero(cuo.getMes())));
//				detallecuota.setTipocambio(cuo.getTipocambio());
				detallecuota.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
				detallecuota.setMontosoles(listaCuotasPendientesCondicionCancelar.get(i).getMonto());
				detallecuota.setFeccre(new Date());
				detallecuota.setObservacion(comprobantepago.getObservacion());
				detallecuota.setEstado(Constante.ESTADO_ACT);
				detallecuota.setCondicion(Constante.ESTADO_CONDICION_ACT);
				getCarteraService().registrarDetalleCuotaArbitrio(detallecuota);
			
			}
		}
		if(idtipodocuseleccionado.equals("04") || !sifacturacionelectronica){
			grabarDocumentoPDF(comprobantepago);
		}
		addInfoMessage("","Se registro con exito el comprobante de pago");
		//comprobacion todas las cuotas canceladas cambiar estado de arbitrio a cancelado
		
		listacuotasxMes=arbitrioService.obtenerPeriodoxMes(condicionSeleccionado.getIdarbitrio());
		if(listacuotasxMes!=null && listacuotasxMes.size()>0){
		comprobarFinalizacionArbitrio(listacuotasxMes,condicionSeleccionado.getIdarbitrio() );
		}else{
			
		}
		iddetallecartera=0;
		serieFacturacionFisica="";
		correlativoFacturacionFisica="";

		valorTipoCambio=0.0;

		
		limpiarCampos();
		
		
	}catch(Exception e ){
		
		addErrorMessage("",Constantes.MSG_GENERO_ERROR_OPERACION);
		Logger.error(e.getMessage(),e);
		e.printStackTrace();
	}
	
	
}
public void grabarDocumentoPDF(Comprobantepago cp){
	StreamedContent report;
	List<ComprobanteBean> comprobantebean= new ArrayList<ComprobanteBean>();
	comprobantebean=cobranzaRecaudacionService.buscarComprobante(cp);
	try {
		   Map<String, Object> parameters = new HashMap<String, Object>();
		   String  reportPath;
		   String nombrearchivo;
		   reportPath=  ConstantesReporteSGI.REP_RECIBO_CAJA_SGI;
		   nombrearchivo="RC_"+comprobantebean.get(0).getNroseriecomprobante()+"_"+FechaUtil.fechaToString(new Date())+Constantes.EXTENSION_FORMATO_PDF;
		   Class.forName(PropiedadesBaseDatos.getString("driverClassName"));
		   Connection conn = DriverManager.getConnection(PropiedadesBaseDatos.getString("url"), PropiedadesBaseDatos.getString("usuario"),PropiedadesBaseDatos.getString("contrasenia"));
		   parameters.put("REPORT_LOCALE", new Locale("es", "ES"));
		   parameters.put("REPORT_CONNECTION",conn);

		   //report = reporteGeneradorService.generarPDF(reportPath, parameters, comprobantebean,nombrearchivo+FuncionesHelper.fechaToString(new Date()));
		   ArchivoAdjuntoBean archivoAdjuntoBean= new ArchivoAdjuntoBean();
    		archivoAdjuntoBean.setTitulo("Recibo de caja "+comprobantebean.get(0).getNroseriecomprobante()+" "+FechaUtil.fechaToString(new Date()));
    		archivoAdjuntoBean.setNombre(nombrearchivo);
    		archivoAdjuntoBean.setUsrcre(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());    		
    		archivoAdjuntoBean.setTipo(Constantes.PDF);
    		archivoAdjuntoBean.setStream(reporteGeneradorService.generarPDF(reportPath, parameters, comprobantebean));
    		archivoAdjuntoBean.setRuta(Constantes.DIR_SGI_RECIBO_CAJA);
    		archivoAdjuntoBean.setFeccre(new Date());
    		administrarArchivoService.grabarArchivo(archivoAdjuntoBean, Constantes.COMPROBANTEPAGO, cp.getIdcomprobante());
	}catch(Exception e){
		addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
		Logger.error(e.getMessage(), e);
	}

}
public void comprobarFinalizacionArbitrio(List<PagoArbitrioBean> list,Integer idarbitrio ){
	Double totalpagado=0.0;
	Double total=0.0;
	ArbitrioBean arbitrioBean=new ArbitrioBean();
	arbitrioBean=arbitrioService.obtenerArbitrioPorCondicion(idarbitrio);
	total=FuncionesHelper.redondear(arbitrioBean.getTotal(),2);
	int numMeses=0;
	for(int i=0;i<list.size();i++){
		if(list.get(i).getEstado().equals("CANCELADO")||(list.get(i).getEstado().equals("PENDIENTE")&& FuncionesHelper.redondear(list.get(i).getMonto(),2)==0)){
			totalpagado=totalpagado+FuncionesHelper.redondear(list.get(i).getMonto(),2);
			numMeses++;
		}		
	}
	System.out.println("totalpagado="+FuncionesHelper.redondearNum(totalpagado,2));
	System.out.println("total="+total);
	if(Double.toString(FuncionesHelper.redondearNum(totalpagado,2)).equals(Double.toString(FuncionesHelper.redondear(total,2)))&& numMeses==12){
		//Actualizar estado de arbitrio
		Arbitrio a =new Arbitrio();
		a.setIdarbitrio(idarbitrio);
		a.setUsrmod(userMB.getUsuariologueado().getNombres() + " " + userMB.getUsuariologueado().getApellidopat());
		a.setFecmod(new Date());
		cobranzaRecaudacionService.actualizarArbitrio(a);
	}
	
}
public void limpiarCampos()  {
//	cartera = null;
//	idcontratoseleccionado = 0;
//	
//	moraFila = 0.0;
//
//	observacionnotadebito = "";
//	nrocomprobantepadrenotadebito="";
//	observacionnotacredito="";
	listaAutovaluoArbitrio.clear();

	inicializarTotales();
	
	idtipomovimientoseleccionado = "0";
	idtipopagoseleccionado = "0";
	idtipoconceptoseleccionado="0";
	idtipodocuseleccionado="0";
	
//	idComprobantePagoPadrenotatipo=0;
	limpiarListaCuotasPendientesCondicionCancelar();
//	sigeneracionautomaticanotadebito=Boolean.FALSE;
	
	serieFacturacionFisica="";
	correlativoFacturacionFisica="";
	
//	fechaemisionNotaDebito=null;
	selectedCobrador=null;
	habilitarDocDNI=true;habilitarDocRUC=true;
//	observacionnotadebito="";
//	fechaoperacion="";
//	numerooperacion="";
}
public void cargarTipoDocumentoValido(){
	if(!comprobantepago.getIdtipopersona().equalsIgnoreCase("0")){
	if(comprobantepago.getIdtipopersona().equalsIgnoreCase(Constantes.TIPO_PERSONA_NATURAL.toString())){
		habilitarDocDNI=false;habilitarDocRUC=false;
	}else{
		habilitarDocRUC=false;habilitarDocDNI=true;
	}
	}else{			
		comprobantepago.setTipodoc_receptor("0");
		comprobantepago.setDnirucpagante("");
		habilitarDocDNI=true;habilitarDocRUC=true;
		addWarnMessage("","Debe seleccionar un tipo de persona valido	");
	}
}
public void cargarLongitudTipoDocumento(){
	if(!comprobantepago.getTipodoc_receptor().equalsIgnoreCase("0")){
		longDocuPersona= carteraService.longitudTipoDocumentoPersona (comprobantepago.getTipodoc_receptor());
	}
}
public Boolean validarNumeroDocPersona(){
	Boolean resp=false;
	if(comprobantepago.getDnirucpagante()!=null){
		      
		if(comprobantepago.getTipodoc_receptor().equalsIgnoreCase("1")){
			if(comprobantepago.getDnirucpagante().length()==8){
				resp=true;
			}
		}else if (comprobantepago.getTipodoc_receptor().equalsIgnoreCase("6")){
			if(comprobantepago.getDnirucpagante().length()==11){
				resp=true;
			}
		}else if (comprobantepago.getTipodoc_receptor().equalsIgnoreCase("4")){
			if(comprobantepago.getDnirucpagante().length()<=12){
				resp=true;
			}
		}
	}
	return resp;
}
public String getClaveupa() {
	return claveupa;
}

public void setClaveupa(String claveupa) {
	this.claveupa = claveupa;
}


public List<ArbitrioBean> getListaAutovaluoArbitrio() {
	return listaAutovaluoArbitrio;
}

public void setListaAutovaluoArbitrio(List<ArbitrioBean> listaAutovaluoArbitrio) {
	this.listaAutovaluoArbitrio = listaAutovaluoArbitrio;
}

public String getSelectedTipoDocumento() {
	return selectedTipoDocumento;
}

public void setSelectedTipoDocumento(String selectedTipoDocumento) {
	this.selectedTipoDocumento = selectedTipoDocumento;
}

public String getIdtipodocuseleccionado() {
	return idtipodocuseleccionado;
}

public void setIdtipodocuseleccionado(String idtipodocuseleccionado) {
	this.idtipodocuseleccionado = idtipodocuseleccionado;
}

public List<Tipodocu> getListaTipoDocumentos() {
	return listaTipoDocumentos;
}

public void setListaTipoDocumentos(List<Tipodocu> listaTipoDocumentos) {
	this.listaTipoDocumentos = listaTipoDocumentos;
}

public String getIdtipomovimientoseleccionado() {
	return idtipomovimientoseleccionado;
}

public void setIdtipomovimientoseleccionado(String idtipomovimientoseleccionado) {
	this.idtipomovimientoseleccionado = idtipomovimientoseleccionado;
}

public String getIdtipoconceptoseleccionado() {
	return idtipoconceptoseleccionado;
}

public void setIdtipoconceptoseleccionado(String idtipoconceptoseleccionado) {
	this.idtipoconceptoseleccionado = idtipoconceptoseleccionado;
}

public String getIdtipopagoseleccionado() {
	return idtipopagoseleccionado;
}

public void setIdtipopagoseleccionado(String idtipopagoseleccionado) {
	this.idtipopagoseleccionado = idtipopagoseleccionado;
}

public String getSerieFacturacionFisica() {
	return serieFacturacionFisica;
}

public void setSerieFacturacionFisica(String serieFacturacionFisica) {
	this.serieFacturacionFisica = serieFacturacionFisica;
}

public String getCorrelativoFacturacionFisica() {
	return correlativoFacturacionFisica;
}

public void setCorrelativoFacturacionFisica(String correlativoFacturacionFisica) {
	this.correlativoFacturacionFisica = correlativoFacturacionFisica;
}

public Comprobantepago getComprobantepago() {
	return comprobantepago;
}

public void setComprobantepago(Comprobantepago comprobantepago) {
	this.comprobantepago = comprobantepago;
}

public String getTipodocumentoSeleccionado() {
	return tipodocumentoSeleccionado;
}

public void setTipodocumentoSeleccionado(String tipodocumentoSeleccionado) {
	this.tipodocumentoSeleccionado = tipodocumentoSeleccionado;
}

public boolean isSigarantia() {
	return sigarantia;
}

public void setSigarantia(boolean sigarantia) {
	this.sigarantia = sigarantia;
}

public List<Tipopago> getTipopagos() {
	return tipopagos;
}

public void setTipopagos(List<Tipopago> tipopagos) {
	this.tipopagos = tipopagos;
}

public List<Tipomovimiento> getTipomovimientos() {
	return tipomovimientos;
}

public void setTipomovimientos(List<Tipomovimiento> tipomovimientos) {
	this.tipomovimientos = tipomovimientos;
}

public boolean isSicobrador() {
	return sicobrador;
}

public void setSicobrador(boolean sicobrador) {
	this.sicobrador = sicobrador;
}

public boolean isSiperfilcobrador() {
	return siperfilcobrador;
}

public void setSiperfilcobrador(boolean siperfilcobrador) {
	this.siperfilcobrador = siperfilcobrador;
}

public Date getMinFechaCancelacion() {
	return minFechaCancelacion;
}
public void setMinFechaCancelacion(Date minFechaCancelacion) {
	this.minFechaCancelacion = minFechaCancelacion;
}
public Date getMaxFechaCancelacion() {
	return maxFechaCancelacion;
}
public void setMaxFechaCancelacion(Date maxFechaCancelacion) {
	this.maxFechaCancelacion = maxFechaCancelacion;
}
public ItemBean getSelectedCobrador() {
	return selectedCobrador;
}

public void setSelectedCobrador(ItemBean selectedCobrador) {
	this.selectedCobrador = selectedCobrador;
}

public BigDecimal getTotalSubTotalComprobante() {
	return totalSubTotalComprobante;
}

public void setTotalSubTotalComprobante(BigDecimal totalSubTotalComprobante) {
	this.totalSubTotalComprobante = totalSubTotalComprobante;
}

public BigDecimal getIgvComprobante() {
	return igvComprobante;
}

public void setIgvComprobante(BigDecimal igvComprobante) {
	this.igvComprobante = igvComprobante;
}

public BigDecimal getTotalComprobante() {
	return totalComprobante;
}

public void setTotalComprobante(BigDecimal totalComprobante) {
	this.totalComprobante = totalComprobante;
}

public IFHvariadoService getFHvariadoService() {
	return FHvariadoService;
}

public void setFHvariadoService(IFHvariadoService fHvariadoService) {
	FHvariadoService = fHvariadoService;
}

public IAutovaluoArbitrioService getArbitrioService() {
	return arbitrioService;
}

public void setArbitrioService(IAutovaluoArbitrioService arbitrioService) {
	this.arbitrioService = arbitrioService;
}

public ArbitrioBean getCondicionSeleccionado() {
	return condicionSeleccionado;
}

public void setCondicionSeleccionado(ArbitrioBean condicionSeleccionado) {
	this.condicionSeleccionado = condicionSeleccionado;
}

public List<CuotaArbitrioBean> getListaCuotasPendientesCondicionCancelar() {
	return listaCuotasPendientesCondicionCancelar;
}

public void setListaCuotasPendientesCondicionCancelar(
		List<CuotaArbitrioBean> listaCuotasPendientesCondicionCancelar) {
	this.listaCuotasPendientesCondicionCancelar = listaCuotasPendientesCondicionCancelar;
}

public List<CuotaArbitrioBean> getCuotasPendientesSeleccion() {
	return cuotasPendientesSeleccion;
}

public void setCuotasPendientesSeleccion(
		List<CuotaArbitrioBean> cuotasPendientesSeleccion) {
	this.cuotasPendientesSeleccion = cuotasPendientesSeleccion;
}
public IRecaudacionCarteraService getCarteraService() {
	return carteraService;
}
public void setCarteraService(IRecaudacionCarteraService carteraService) {
	this.carteraService = carteraService;
}
public List<CuotaArbitrioBean> getListaCuotasPendientesCondicionCancelarFilter() {
	return listaCuotasPendientesCondicionCancelarFilter;
}
public void setListaCuotasPendientesCondicionCancelarFilter(
		List<CuotaArbitrioBean> listaCuotasPendientesCondicionCancelarFilter) {
	this.listaCuotasPendientesCondicionCancelarFilter = listaCuotasPendientesCondicionCancelarFilter;
}
public List<CuotaArbitrioBean> getListaCuotasPendientesCondicion() {
	return listaCuotasPendientesCondicion;
}
public void setListaCuotasPendientesCondicion(
		List<CuotaArbitrioBean> listaCuotasPendientesCondicion) {
	this.listaCuotasPendientesCondicion = listaCuotasPendientesCondicion;
}
public double getValorTipoCambio() {
	return valorTipoCambio;
}
public void setValorTipoCambio(double valorTipoCambio) {
	this.valorTipoCambio = valorTipoCambio;
}
public IRecaudacionReporteService getRecaudacionReporteService() {
	return recaudacionReporteService;
}
public void setRecaudacionReporteService(
		IRecaudacionReporteService recaudacionReporteService) {
	this.recaudacionReporteService = recaudacionReporteService;
}
public CuotaArbitrioBean getCuotaPendienteCondicion() {
	return cuotaPendienteCondicion;
}
public void setCuotaPendienteCondicion(CuotaArbitrioBean cuotaPendienteCondicion) {
	this.cuotaPendienteCondicion = cuotaPendienteCondicion;
}
public int getIddetallecartera() {
	return iddetallecartera;
}
public void setIddetallecartera(int iddetallecartera) {
	this.iddetallecartera = iddetallecartera;
}
public UsuarioManagedBean getUserMB() {
	return userMB;
}
public void setUserMB(UsuarioManagedBean userMB) {
	this.userMB = userMB;
}
public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
	return cobranzaRecaudacionService;
}
public void setCobranzaRecaudacionService(
		IRecaudacionCobranzaService cobranzaRecaudacionService) {
	this.cobranzaRecaudacionService = cobranzaRecaudacionService;
}
public Date getMinfechacancelacion() {
	return minfechacancelacion;
}
public void setMinfechacancelacion(Date minfechacancelacion) {
	this.minfechacancelacion = minfechacancelacion;
}
public String getIdtipomodalidadpago() {
	return idtipomodalidadpago;
}
public void setIdtipomodalidadpago(String idtipomodalidadpago) {
	this.idtipomodalidadpago = idtipomodalidadpago;
}
public CobranzaManagedBean getCobranzabean() {
	return cobranzabean;
}
public void setCobranzabean(CobranzaManagedBean cobranzabean) {
	this.cobranzabean = cobranzabean;
}
public IReporteGeneradorService getReporteGeneradorService() {
	return reporteGeneradorService;
}
public void setReporteGeneradorService(
		IReporteGeneradorService reporteGeneradorService) {
	this.reporteGeneradorService = reporteGeneradorService;
}
public AdministrarArchivoService getAdministrarArchivoService() {
	return administrarArchivoService;
}
public void setAdministrarArchivoService(
		AdministrarArchivoService administrarArchivoService) {
	this.administrarArchivoService = administrarArchivoService;
}
public Boolean getHabilitarDocDNI() {
	return habilitarDocDNI;
}
public void setHabilitarDocDNI(Boolean habilitarDocDNI) {
	this.habilitarDocDNI = habilitarDocDNI;
}
public Boolean getHabilitarDocRUC() {
	return habilitarDocRUC;
}
public void setHabilitarDocRUC(Boolean habilitarDocRUC) {
	this.habilitarDocRUC = habilitarDocRUC;
}
public Integer getLongDocuPersona() {
	return longDocuPersona;
}
public void setLongDocuPersona(Integer longDocuPersona) {
	this.longDocuPersona = longDocuPersona;
}


 
 
}
