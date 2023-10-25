package pe.gob.sblm.sgi.service;

import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;

import java.util.List;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;

public interface ArbitrioService {

	public boolean verificarExistenciaClaveUpa(String clave);
	public boolean verificarExistenciaClaveUpaArbitrio(String clave);
	public boolean verificarExistenciaClaveUpaPeriodoArbitrio(String clave , String periodo);
	public void saveUpdateArbitrio(Arbitrio arb)throws Exception;
	public List<ArbitrioBean> buscarArbitrio(String valorbusqueda, String tipo);
	public List<CuotaArbitrioBean>obtenerCuotasxArbitrio(Integer idarbitrio);
	public Arbitrio obtenerArbitrio(int idarbitrio);
	public Cuota_arbitrio obtenerCuotaArbitrio(Integer idarbitrio,String mes);
	public Cuota_arbitrio obtenerCuotaArbitrioxId(Integer idcuota_arbitrio);
	public boolean verificarExistenciaPagosCuotaArbitrio(Integer idarbitrio);
}
