package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.MaeConfiguracionServicio;

public interface MaeConfiguracionServicioDao {
	
	public List<MaeConfiguracionServicio> listaConfiguracionesPorTabla (MaeConfiguracionServicio configuracionServicio) throws Exception;

}
