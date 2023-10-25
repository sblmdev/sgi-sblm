package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.MaeConfiguracionServicio;

public interface ConfiguracionServicioService {
	
	public List<MaeConfiguracionServicio> listaConfiguracionesPorTabla (MaeConfiguracionServicio configuracionServicio) throws Exception;

}
