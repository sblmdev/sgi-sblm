package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IInstitucionServiceDAO;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.service.IInstitucionService;

@Transactional(readOnly = true)
@Service(value="institucionService")
public class InstitucionService implements IInstitucionService{
	@Autowired
	private IInstitucionServiceDAO institucionDAO;
	


	@Transactional(readOnly = false)
	
	public void grabarInstitucion(Institucion institucion) {
		institucionDAO.grabarInstitucion(institucion);
		
	}

	
	public List<Institucion> obtenerTodosInstituciones() {
		
		return institucionDAO.obtenerTodosInstituciones();
	}

	@Transactional(readOnly = false)
	
	public void eliminarInstitucion(Institucion institucion) {
		institucionDAO.eliminarInstitucion(institucion);
		
	}

	
	public int nroInstituciones() {
		
		return institucionDAO.nroInstituciones();
	}



}
