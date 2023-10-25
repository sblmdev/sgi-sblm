package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Detallearbitrio;


public interface IRecaudacionAutovaluoArbitrioDAO {

	
	public List<ArbitrioBean> listarArbitriosxAnio(String idupa);
	public List<ArbitrioBean> listarArbitriosxAnio(int idupa);
	public void grabarPeriodoArbitrio(Arbitrio arbitrio);
	public boolean siexistePeriodo(int idupa, int anioseleccionado);
	public void grabarDetallePeriodoArbitrio(Detallearbitrio detalleArbitrioBean);
	public List<DetalleArbitrioBean> listarDetalleArbitrio(int idarbitrio);
	public void editarDetalleArbitrio(DetalleArbitrioBean detalleArbitrioBean);

}
