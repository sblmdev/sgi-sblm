package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPerfilUsuarioDAO;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.service.IPerfilUsuarioService;
@Transactional(readOnly = true)
@Service(value="perfilusuarioService")
public class PerfilUsuarioServices  implements IPerfilUsuarioService{

	@Autowired
	private IPerfilUsuarioDAO perfilusuarioDAO;
	
	@Transactional(readOnly = false)
	
	
	public void registrarPerfilUsuario(Perfilusuario perfilusuario) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarPerfilUsuario(Perfilusuario perfilusuario) {
		getPerfilusuarioDAO().actualizarPerfilUsuario(perfilusuario);
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarPerfilUsuario(Perfilusuario perfilusuario) {
		getPerfilusuarioDAO().eliminarPerfilUsuario(perfilusuario);
		
	}

	
	public Perfilusuario listarPerfilUsuarioPorId(int id) {
		return getPerfilusuarioDAO().listarPerfilUsuarioPorId(id);
	}

	
	public List<Perfilusuario> listarPerfilUsuarios() {
		return getPerfilusuarioDAO().listarPerfilUsuarios();
	}
	public IPerfilUsuarioDAO getPerfilusuarioDAO() {
		return perfilusuarioDAO;
	}
	public void setPerfilusuarioDAO(IPerfilUsuarioDAO perfilusuarioDAO) {
		this.perfilusuarioDAO = perfilusuarioDAO;
	}
	
	public List<Perfilusuario> listarPerfilUsuariosPorId(int id) {
		return getPerfilusuarioDAO().listarPerfilUsuariosPorId(id);
	}

}
