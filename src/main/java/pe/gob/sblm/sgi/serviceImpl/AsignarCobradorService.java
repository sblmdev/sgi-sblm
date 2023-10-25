package pe.gob.sblm.sgi.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IAsignarCobradorServiceDAO;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAsignarCobradorService;

@Transactional(readOnly = true)
@Service(value="asignarcobradorService")
public class AsignarCobradorService implements IAsignarCobradorService{
	@Autowired
	private IAsignarCobradorServiceDAO asignarcobradorDAO;

	
	public void asignarCobrador(Usuario usuario) {
		asignarcobradorDAO.asignarCobrador(usuario);
		System.out.println("Service"+usuario.getIdusuario());
		
	}
	


	}
