package pe.gob.sblm.sgi.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.ILetraDAO;
import pe.gob.sblm.sgi.entity.Letra;
import pe.gob.sblm.sgi.service.ILetraService;


@Transactional(readOnly = true)
@Service(value="letraService")
public class LetraService implements ILetraService{
	@Autowired
	private ILetraDAO letraDAO;

	
	public void registrarLetra(Letra letra) {
		
		letraDAO.registrarLetra(letra);
		
	}
	

}
