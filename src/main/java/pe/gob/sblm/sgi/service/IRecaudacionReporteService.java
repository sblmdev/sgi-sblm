package pe.gob.sblm.sgi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetalleCuotaBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoContratoBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;


public interface IRecaudacionReporteService {
	
	
	
	
	public double obtenerSumaTotalCuotas(List<CuotaBean> lista);
	public double obtenerDeudaCobroSinContratoCA(int idcondicion);
	public double obtenerDeudaCobroContratoCA(int idcondicion);
	public Map<String,String> obtenerDatosDeudaCobroUPA(int idupa,double tipocambio);
	public double obtenerRentaActualCondicion(Integer idcondicion);
	
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxUpa(int idupa);
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxCondicion(int idcondicion, String Desde,String Hasta);
	
	public String resumenPeriodosPendientesCondicion(int idcondicion,String condicion);
	
	
	public String obtenerPrimeraRentaPendienteCondicion(Integer idcondicion);
	public Integer obtenerNumeroRentasCondicion(int idcondicion,String condicion,Date desde,Date hasta);
	
	
	
	public List<UpaPagadorDeudorBean> determinarCarteraActiva(String nombrecartera, String Desde, String Hasta);
	public List<UpaPagadorDeudorBean> determinarCarteraPesada(String nombrecartera, String Desde, String Hasta);
	
	
	public List<CuotaBean> generarPendientesParaCobroSinContrato(int idcondicion,Double tipocambio);
	public List<CuotaBean> generarPendientesParaCobroContrato(int idcondicion,Double tipocambio);
	
	
	public List<CuotaBean> obtenerInformacionCobroCondicionTipo(int idcondicion,String condicion,String tipoReporte,Double tipocambio) ;
	public List<CuotaBean> obtenerInformacionCobroCondicionTipoSinMantenimiento(int idcondicion,String condicion,String tipoReporte,Double tipocambio) ;
	public List<CuotaBean> obtenerInformacionCobroCondicionTipoxDeuda(int idcondicion,String condicion,String tipoReporte,Double tipocambio) ;


//	public List<CuotaBean> obtenerInformacionCobroContratoTipo(int idcondicion, String tipo,Double tipocambio);
//	public List<CuotaBean> obtenerInformacionCobroSinContratoTipo(int idcondicion, String tipo,Double tipocambio);

	public List<CuotaBean> listarRentaMensualAPagarCondicionxIntervalo(int idcontrato, Date desde, Date hasta);

	public List<CuotaBean> listarRentaMensualPagadoCondicionxIntervalo(int idcondicion,Date desde, Date hasta, String moneda);



	public List<CuotaBean> generarRentasPendientesParaCobroNuevoContrato(int idcontrato, Double valorTipoCambio);
	public List<CuotaBean> generarRentasPendientesParaCobroReconocimientoDeuda(int idcontrato, Double valorTipoCambio);
	public List<RentaBean> obtenerRentasNuevoMantenimientoContrato(int idcontrato);
	public List<CuotaBean> generarRentasPendientesNuevoContrato(int idcontrato, Double valorTipoCambio);
	public List<RentaBean> obtenerCuotasMantenimientoReconocimietoDeuda(int idcontrato);

	public List<RentaBean> obtenerPeriodosPagados(int idcontrato);
	public List<RentaBean> obtenerRentasPagadosxContrato(int idcontrato , String condicion ,Boolean condMaeDetalle);
	public List<RentaBean> obtenerRentasPendientesxContrato (int idcontrato , String condicion , Boolean  condMaeDetalle);
	public List<RentaBean> obtenerRentasPagadosPendientesxContrato (int idcontrato , String condicion , Boolean  condMaeDetalle);

	public List<DetalleCuotaBean> obtenerDetalleRentasPagadas(int id);
	public List<RentaBean> obtenerPeriodosPendientes(int idcontrato);
	public List<RentaBean> obtenerPeriodosPendientesContrato(int idcontrato);
	public List<CuotaBean> obtenerPeriodosPendientesDeuda(int idcontrato);
	public List<CuotaBean> obtenerPeriodosPendientesContratoDeuda(int idcontrato);
	//cuotas arbitrios
	public List<CuotaArbitrioBean> obtenerInformacionPagosPendientes(int idcondicion, List<PagoArbitrioBean> listaPagos,String condicion,String tipoReporte,Double tipocambio);
	public List<CuotaArbitrioBean> obtenerInformacionPagosPendientes(ArbitrioBean arbitrioBean,Double tipocambio,List<PeriodoPagadoBean> listacuotaspagadas);
	public List<CuotaArbitrioBean> obtenerPeriodosPendientesxMes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPendientesxMes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPagadosxMes(int idarbitrio);
	//periodos contrato
	public List<PeriodoContratoBean> obtenerPeriodoxContrato(int idcontrato);
	public List<Sunat_Comprobante> obtenerListaSunatComprobantePendientes();
    ///obtencion de renta de deuda hasta la fecha 
	public List<RentaBean> listarRentaPendientesxContrato(int idcontrato ,String condicion, Boolean condMaeDetalle);
	public boolean siRentasPagadasxContrato(int idcontrato);
	public RentaBean ultimaRentaPagada(int idcontrato );
	
	
	
}
