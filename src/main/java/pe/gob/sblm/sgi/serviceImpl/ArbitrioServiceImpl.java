package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.dao.IArbitrioDAO;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.service.ArbitrioService;

@Transactional(readOnly = true)
@Service(value="arbitrioService")
public class ArbitrioServiceImpl implements ArbitrioService{

	private static final Logger Logger = LoggerFactory.getLogger(ArbitrioServiceImpl.class);
	
	@Autowired
	private IArbitrioDAO arbitrioDAO;

	@Override
	public boolean verificarExistenciaClaveUpa(String clave) {		
		return arbitrioDAO.verificarExistenciaClaveUpa(clave);
	}

	@Override
	public boolean verificarExistenciaClaveUpaArbitrio(String clave) {		
		return arbitrioDAO.verificarExistenciaClaveUpaArbitrio(clave);
	}

	@Override
	public boolean verificarExistenciaClaveUpaPeriodoArbitrio(String clave,
			String periodo) {
		return arbitrioDAO.verificarExistenciaClaveUpaPeriodoArbitrio(clave, periodo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void saveUpdateArbitrio(Arbitrio arb)throws Exception {
		try{
			arbitrioDAO.saveUpdateArbitrio(arb);
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		
	}

	public IArbitrioDAO getArbitrioDAO() {
		return arbitrioDAO;
	}

	public void setArbitrioDAO(IArbitrioDAO arbitrioDAO) {
		this.arbitrioDAO = arbitrioDAO;
	}

	public static Logger getLogger() {
		return Logger;
	}

	public List<ArbitrioBean> buscarArbitrio(String valorbusqueda , String tipo) {
		// TODO Auto-generated method stub
		return arbitrioDAO.buscarArbitrio(valorbusqueda, tipo);
	}

	@Override
	public List<CuotaArbitrioBean> obtenerCuotasxArbitrio(Integer idarbitrio) {
		// TODO Auto-generated method stub
		return arbitrioDAO.obtenerCuotasArbitrio(idarbitrio);
	}

	@Override
	public Arbitrio obtenerArbitrio(int idarbitrio) {
		// TODO Auto-generated method stub
		return arbitrioDAO.obteneArbitrioPorId(idarbitrio);
	}

	@Override
	public Cuota_arbitrio obtenerCuotaArbitrio(Integer idarbitrio, String mes) {
		// TODO Auto-generated method stub
		return arbitrioDAO.obtenerCuotaArbitrio(idarbitrio, mes);
	}

	@Override
	public Cuota_arbitrio obtenerCuotaArbitrioxId(Integer idcuota_arbitrio) {
		// TODO Auto-generated method stub
		return arbitrioDAO.obtenerCuotaArbitrioxId(idcuota_arbitrio);
	}

	@Override
	public boolean verificarExistenciaPagosCuotaArbitrio(Integer idarbitrio) {
		// TODO Auto-generated method stub
		return arbitrioDAO.verificarExistenciaPagosCuotaArbitrio(idarbitrio);
	}

}
