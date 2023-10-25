package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPerfilModuloDAO;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.service.IPerfilModuloService;

@Transactional(readOnly = true)
@Service(value="perfilmoduloService")
public class PerfilModuloService  implements IPerfilModuloService{

	@Autowired
	private IPerfilModuloDAO perfilmoduloDAO;
	
	
	@Transactional(readOnly = false)
	
	public void registrarPerfilModulo(Perfilmodulo perfilmodulo) {
		getPerfilmoduloDAO().registrarPerfilModulo(perfilmodulo);
		 
	}
	@Transactional(readOnly = false)
	
	public void actualizarPerfilModulo(Perfilmodulo perfilmodulo) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarPerfilModulo(Perfilmodulo perfilmodulo) {
		// TODO Auto-generated method stub
		
	}

	
	public Perfil listarPerfilModuloPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Perfilmodulo> listarPerfilmodulos() {
		return getPerfilmoduloDAO().listarPerfilmodulos();
	}

	public IPerfilModuloDAO getPerfilmoduloDAO() {
		return perfilmoduloDAO;
	}

	public void setPerfilmoduloDAO(IPerfilModuloDAO perfilmoduloDAO) {
		this.perfilmoduloDAO = perfilmoduloDAO;
	}
	
	public List<Perfilmodulo> listarPerfilModuloPorIdPerfil(int idperfil) {
		return getPerfilmoduloDAO().listarPerfilModuloPorIdPerfil(idperfil);
	}
	
	public void eliminarPerfilModuloId(int idperfil) {
		getPerfilmoduloDAO().eliminarPerfilModuloId(idperfil);
	}

}
