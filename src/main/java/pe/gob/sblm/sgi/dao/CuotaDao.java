package pe.gob.sblm.sgi.dao;

import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;


public interface CuotaDao {

	public void registrarCuota(Cuota cuota);
	
	public void registrarCuotaArbitrio(Cuota_arbitrio cuota);

	public void actualizarEstadoCuota(int idcomprobante);
}
