package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;

public interface IArbitrioDAO {
 public Arbitrio obteneArbitrioPorClaveUpa(String clave);
 public List<ArbitrioBean> obtenerListaArbitrioBeanxUpa(String clave);
 public List<ArbitrioBean> obtenerListaxCondicion(String estado, String sicancelado);
 public List<ArbitrioBean> obtenerListaxCondicionCuotasPagadas(String estado);
 public ArbitrioBean obtenerArbitrioPorCondicion(int idarbitrio);
 public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idarbitrio);
 public List<CuotaArbitrioBean> obtenerPeriodosPendientes(int idarbitrio);
 public List<CuotaArbitrioBean> obtenerListaPeriodosPendientes(int idarbitrio);
 public List<CuotaArbitrioBean> obtenerPeriodosCancelados(int idarbitrio);
 public List<PagoArbitrioBean> obtenerPeriodoxMes(int idarbitrio);
 public List<CuotaArbitrioBean> obtenerPagosArbitrios(int idarbitrio);
 public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota);
 
 public boolean verificarExistenciaClaveUpa(String clave);
 public boolean verificarExistenciaClaveUpaArbitrio(String clave);
 public boolean verificarExistenciaClaveUpaPeriodoArbitrio(String clave , String periodo);
 public void saveUpdateArbitrio(Arbitrio arb)throws Exception;
 public List<ArbitrioBean> buscarArbitrio(String valorbusqueda,String tipo);
 public List<CuotaArbitrioBean> obtenerCuotasArbitrio(Integer idarbitrio);
 public Arbitrio obteneArbitrioPorId(int idarbitrio);
 public Cuota_arbitrio obtenerCuotaArbitrio(Integer idarbitrio,String mes);
 public Cuota_arbitrio obtenerCuotaArbitrioxId(Integer idcuota_arbitrio);
 public boolean verificarExistenciaPagosCuotaArbitrio(Integer idarbitrio);
}
