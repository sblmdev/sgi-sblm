package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Detallearbitrio;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;

public interface IAutovaluoArbitrioService {

	
	public List<ArbitrioBean> listarArbitriosxAnio(String idupa);
	public List<ArbitrioBean> listarArbitriosxAnio(int idupa);
	public void grabarPeriodoArbitrio(Arbitrio arbitrio);
	public Boolean siexistePeriodo(int idupa, int anioseleccionado);
	public void grabarDetallePeriodoArbitrio(Detallearbitrio detalleArbitrio);
	public List<DetalleArbitrioBean> listarDetalleArbitrio(int idarbitrio);
	public void editarDetalleArbitrio(DetalleArbitrioBean detalleArbitrioBean);
	public Arbitrio obtenerArbitrioPorClaveUpa(String clave);
	public ArbitrioBean obtenerArbitrioPorCondicion(int idarbitrio);
	public List<ArbitrioBean> obtenerListaArbitrioBeanxUpa(String clave);
	public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerPeriodosPendientes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerListaPeriodosPendientes(int idarbitrio);
	public List<ArbitrioBean> obtenerListaxCondicion(String estado, String sicancelado);
	public List<ArbitrioBean> obtenerListaxCondicionCuotasPagadas(String estado);
	public List<CuotaArbitrioBean> obtenerPeriodosCancelados(int idarbitrio);
	public List<PagoArbitrioBean> obtenerPeriodoxMes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerPagosArbitrios(int idarbitrio);
	public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota);

}
