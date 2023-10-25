package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPerfilDAO;
import pe.gob.sblm.sgi.dao.ITipoCambioDAO;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.service.IPerfilService;
import pe.gob.sblm.sgi.util.PerfilModuloPermiso;

@Transactional(readOnly = true)
@Service(value="perfilService")
public class PerfilService implements IPerfilService{

	@Autowired
	private IPerfilDAO perfilDAO;
	
	
	@Transactional(readOnly = false)
	
	public void registrarPerfil(Perfil perfil) {
		getPerfilDAO().registrarPerfil(perfil);
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarPerfil(Perfil perfil) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarPerfil(Perfil perfil) {
		getPerfilDAO().eliminarPerfil(perfil);
		
	}

	
	public Perfil listarPerfilPorId(int id) {
		return getPerfilDAO().listarPerfilPorId(id);
	}

	
	public List<Perfil> listarPerfiles() {
		return getPerfilDAO().listarPerfiles();
	}
	
	public List<PerfilModuloPermiso> listarPerfilesModulosPermisos() {
		return getPerfilDAO().listarPerfilesModulosPermisos();
	}
	

	public IPerfilDAO getPerfilDAO() {
		return perfilDAO;
	}

	public void setPerfilDAO(IPerfilDAO perfilDAO) {
		this.perfilDAO = perfilDAO;
	}
	
	public int obtenerUltimoIdPerfil() {
		return getPerfilDAO().obtenerUltimoIdPerfil();
	}
	
	public int obtenerNumeroPerfiles() {
		return getPerfilDAO().obtenerNumeroPerfiles();
	}
	
	public String obtenerUltimoPerfil() {
		return getPerfilDAO().obtenerUltimoPerfil();
	}
	
	public Date obtenerFechaUltimoPerfil() {
		return getPerfilDAO().obtenerFechaUltimoPerfil();
	}

}
