package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IModuloDAO;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.service.IModuloService;

@Transactional(readOnly = true)
@Service(value="moduloService")
public class ModuloService implements IModuloService {

	@Autowired
	private IModuloDAO moduloDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarModulo(Modulo modulo) {
		getModuloDAO().registrarModulo(modulo);
		
	}

	@Transactional(readOnly = false)
	
	public void actualizarModulo(Modulo modulo) {
		// TODO Auto-generated method stub
		
	}

	@Transactional(readOnly = false)
	
	public void eliminarModulo(Modulo modulo) {
		getModuloDAO().eliminarModulo(modulo);
		
	}
	
	@Transactional(readOnly = false)
	
	public void eliminarPaginaModulo(int idmodulo) {
		getModuloDAO().eliminarPaginaModulo(idmodulo);
		
	}
	
	public Perfil  listarPerfilPorId(int idperfil) {
		return getModuloDAO().listarPerfilPorId(idperfil);
		
	}
	
	
	
	public List<Perfilmodulo> verficarModuloEnPerfil(int idmodulo) {
		return	getModuloDAO().verficarModuloEnPerfil(idmodulo);
		
	}
	public Integer obtenerIdPaginaModulo(int idmodulo,int idpagina) {
		return	getModuloDAO().obtenerIdPaginaModulo(idmodulo,idpagina);
		
	}
	
	public Modulo listarModuloPorId(int id) {
		return getModuloDAO().listarModuloPorId(id);
	}

	
	public List<Modulo> listarModulos() {
		return getModuloDAO().listarModulos();
	}

	public IModuloDAO getModuloDAO() {
		return moduloDAO;
	}

	public void setModuloDAO(IModuloDAO moduloDAO) {
		this.moduloDAO = moduloDAO;
	}

	
	public int obtenerNumeroModulos() {
		return getModuloDAO().obtenerNumeroModulos();
		
	}

	
	public String obtenerUltimoModulo() {
		return getModuloDAO().obtenerUltimoModulo();
	}
	
	public Date obtenerFechaUltimoModulo() {
		return getModuloDAO().obtenerFechaUltimoModulo();
	}
	
	
	public Modulo obtenerUltimoModulocreado() {
		return getModuloDAO().obtenerUltimoModulocreado();
	}
	
	public void registrarPaginamodulo(Paginamodulo paginamodulo) {
		getModuloDAO().registrarPaginamodulo(paginamodulo);
		
	}
	
	public List<Pagina> listarPaginasDeModulos(int idmodulo) {
		return getModuloDAO().listarPaginasDeModulos(idmodulo);
	}
	
	public List<Pagina> listarPaginas() {
		return getModuloDAO().listarPaginas();
	}
	
}
