package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.ISuperUsuarioDAO;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.ISuperUsuarioService;

@Transactional(readOnly = true)
@Service(value="panelSuperUsuarioServiceImpl")
public class SuperUsuarioServiceImpl implements ISuperUsuarioService{

	@Autowired
	private ISuperUsuarioDAO superusuarioDAO;

	@Autowired
	private ISuperUsuarioDAO usuarioDAO;

	
	
	public List<Usuario> listarUsuarios() {
		return superusuarioDAO.listarUsuarios();
	}


	
	public List<Perfilusuario> getPerfilesUsuario(String selectIdRegistroUsuario) {
		return superusuarioDAO.getPerfilesUsuario(selectIdRegistroUsuario);	
		}


	
	public void save(Perfilusuario perfilusuario) {
		superusuarioDAO.save(perfilusuario);			
	}


	
	public void nuevoUsuario(Usuario usr) {
		superusuarioDAO.nuevoUsuario(usr);			
		
	}


	
	public List<Perfil> listPerfilbyNom() {
		return superusuarioDAO.listPerfilbyNom();	
	}


	
	public Object getLastIdUsuario() {
		
		return superusuarioDAO.getLastIdUsuario();	
	}


	
	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario) {

		return superusuarioDAO.getUsuarioEditSinPerfil(selectIdRegistroUsuario);
	}


	
	public List getUsuarioEditConPerfil(String selectIdRegistroUsuario) {
		// TODO Auto-generated method stub
		return superusuarioDAO.getUsuarioEditConPerfil(selectIdRegistroUsuario);
	}


	
	public void eliminarUsuario(String selectIdRegistroUsuario) {
		superusuarioDAO.eliminarUsuario(selectIdRegistroUsuario);
		
	}


	
	public String existenciaUsuario() {
		return null;
	}


	
	public void actualizarUsuario(Usuario u, String fechaUpdate) {
		usuarioDAO.actualizarUsuario(u,fechaUpdate);
		
	}
	
	
	public void eliminarAntiguoPerfilUsuario(int idusuario) {
		usuarioDAO.eliminarAntiguoPerfilUsuarios(idusuario);		
	}


	public ISuperUsuarioDAO getSuperusuarioDAO() {
		return superusuarioDAO;
	}


	public void setSuperusuarioDAO(ISuperUsuarioDAO superusuarioDAO) {
		this.superusuarioDAO = superusuarioDAO;
	}


	public ISuperUsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}


	public void setUsuarioDAO(ISuperUsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}


	
	public List<Area> getAllArea() {
		return superusuarioDAO.getAllArea();	
	}


	
	public Usuario getUsuarioaEditar(String selectIdRegistroUsuario) {
		return superusuarioDAO.getUsuarioaEditar(selectIdRegistroUsuario);	
	}


	
}
