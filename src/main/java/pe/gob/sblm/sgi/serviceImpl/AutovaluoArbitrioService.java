package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.dao.IArbitrioDAO;
import pe.gob.sblm.sgi.dao.IRecaudacionAutovaluoArbitrioDAO;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Detallearbitrio;
import pe.gob.sblm.sgi.service.IAutovaluoArbitrioService;

@Transactional(readOnly = true)
@Service(value="autovaluoarbitrioService")
public class AutovaluoArbitrioService implements IAutovaluoArbitrioService{

	@Autowired
	private IRecaudacionAutovaluoArbitrioDAO recaudacionautovaluoarbitrioDAO;
    
	@Autowired
	private IArbitrioDAO arbitrioDAO;
	
	public List<ArbitrioBean> listarArbitriosxAnio(String idupa) {
		return recaudacionautovaluoarbitrioDAO.listarArbitriosxAnio(idupa);
	}
	public List<ArbitrioBean> listarArbitriosxAnio(int idupa) {
		return recaudacionautovaluoarbitrioDAO.listarArbitriosxAnio(idupa);
	}

	
	public void grabarPeriodoArbitrio(Arbitrio arbitrio) {
		recaudacionautovaluoarbitrioDAO.grabarPeriodoArbitrio(arbitrio);
	}

	
	public Boolean siexistePeriodo(int idupa, int anioseleccionado) {
		
		return recaudacionautovaluoarbitrioDAO.siexistePeriodo(idupa, anioseleccionado);
	}

	
	public void grabarDetallePeriodoArbitrio(Detallearbitrio detalleArbitrioBean) {
		recaudacionautovaluoarbitrioDAO.grabarDetallePeriodoArbitrio(detalleArbitrioBean);
	}

	
	public List<DetalleArbitrioBean> listarDetalleArbitrio(int idarbitrio) {
		return recaudacionautovaluoarbitrioDAO.listarDetalleArbitrio(idarbitrio);
	}

	
	public void editarDetalleArbitrio(DetalleArbitrioBean detalleArbitrioBean) {
		recaudacionautovaluoarbitrioDAO.editarDetalleArbitrio(detalleArbitrioBean);
	}
    public Arbitrio obtenerArbitrioPorClaveUpa( String clave){
    	return getArbitrioDAO().obteneArbitrioPorClaveUpa(clave);
    }
    public IArbitrioDAO getArbitrioDAO(){
    	return arbitrioDAO;
    }

	public List<ArbitrioBean> obtenerListaArbitrioBeanxUpa(String clave) {
		
		return arbitrioDAO.obtenerListaArbitrioBeanxUpa(clave);
	}
	
	 public List<ArbitrioBean> obtenerListaxCondicion(String estado, String sicancelado){
		 return arbitrioDAO.obtenerListaxCondicion(estado,sicancelado);
	 }
	 public List<ArbitrioBean> obtenerListaxCondicionCuotasPagadas(String estado){
		 return arbitrioDAO.obtenerListaxCondicionCuotasPagadas(estado);
	 }
	@Override
	public ArbitrioBean obtenerArbitrioPorCondicion(int idarbitrio) {
	
		return arbitrioDAO.obtenerArbitrioPorCondicion(idarbitrio);
	}
	@Override
	public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idarbitrio) {
		return arbitrioDAO.obtenerPeriodosPagados(idarbitrio);
	}
	@Override
	public List<CuotaArbitrioBean> obtenerPeriodosPendientes(int idarbitrio) {
		return arbitrioDAO.obtenerPeriodosPendientes(idarbitrio);
	}
	public List<CuotaArbitrioBean> obtenerListaPeriodosPendientes(int idarbitrio) {
		return arbitrioDAO.obtenerListaPeriodosPendientes(idarbitrio);
	}
	@Override
	public List<PagoArbitrioBean> obtenerPeriodoxMes(int idarbitrio) {
		return arbitrioDAO.obtenerPeriodoxMes(idarbitrio);
	}
	@Override
	public List<CuotaArbitrioBean> obtenerPagosArbitrios(int idarbitrio) {
		// TODO Auto-generated method stub
		return arbitrioDAO.obtenerPagosArbitrios(idarbitrio);
	}
	public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota) {
		return arbitrioDAO.buscarComprobantesPeriodo(idcuota);		
	}
	@Override
	public List<CuotaArbitrioBean> obtenerPeriodosCancelados(int idarbitrio) {
		return arbitrioDAO.obtenerPeriodosCancelados(idarbitrio);
	}

    
}
