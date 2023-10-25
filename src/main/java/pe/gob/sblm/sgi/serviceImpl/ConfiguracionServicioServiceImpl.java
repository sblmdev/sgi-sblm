package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.MaeConfiguracionServicioDao;
import pe.gob.sblm.sgi.entity.MaeConfiguracionServicio;
import pe.gob.sblm.sgi.service.ConfiguracionServicioService;

@Service("configuracionServicioService")
public class ConfiguracionServicioServiceImpl implements ConfiguracionServicioService{
	
	private static final Logger Logger = LoggerFactory.getLogger(ConfiguracionServicioServiceImpl.class);
	
	@Autowired
	MaeConfiguracionServicioDao maeConfiguracionServicioDao;


	@Transactional(readOnly = true)
	@Override
	public List<MaeConfiguracionServicio> listaConfiguracionesPorTabla(MaeConfiguracionServicio configuracionServicio) throws Exception {
		
		try{
			
			List<MaeConfiguracionServicio> resultado = maeConfiguracionServicioDao.listaConfiguracionesPorTabla(configuracionServicio);
			Logger.info(resultado.toString());
			
			return resultado;
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		
	}

}
