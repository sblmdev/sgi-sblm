package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IIgvDAO;
import pe.gob.sblm.sgi.dao.IRepresentanteServiceDAO;
import pe.gob.sblm.sgi.dao.IUsoDAO;
import pe.gob.sblm.sgi.entity.Igv;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.service.IIgvService;
import pe.gob.sblm.sgi.service.IRepresentanteService;

@Transactional(readOnly = true)
@Service(value="representanteService")
public class RepresentanteService implements IRepresentanteService{
	@Autowired
	private IRepresentanteServiceDAO representanteDAO;
	


	@Transactional(readOnly = false)
	
	public void grabarRepresentante(Representante representante) {
		representanteDAO.grabarRepresentante(representante);
		
	}



	
	public List<Representante> obtenerTodosRepresentantes() {
		
		return representanteDAO.obtenerTodosRepresentantes();
	}



	
	public void eliminarRepresentante(Representante representante) {
		representanteDAO.eliminarRepresentante(representante);
		
	}



	
	public int nrorepresentantes() {
		// TODO Auto-generated method stub
		return representanteDAO.nrorepresentantes();
	}



}
