package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPaginaDAO;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.service.IPaginaService;
@Transactional(readOnly = true)
@Service(value="paginaService")

public class PaginaService implements IPaginaService{

	@Autowired
	private IPaginaDAO paginaDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarPagina(Pagina pagina) {
		getPaginaDAO().registrarPagina(pagina);
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarPagina(Pagina pagina) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarPagina(Pagina pagina) {
		getPaginaDAO().eliminarPagina(pagina);
		
	}



	
	public List<Pagina> listarPaginas() {
		return getPaginaDAO().listarPaginas();
	}

	
	public int obtenerNumeroPaginas() {
		return getPaginaDAO().obtenerNumeroPaginas();
	}

	
	public String obtenerUltimaPagina() {
		return getPaginaDAO().obtenerUltimaPagina();
	}
	
	public Pagina verificarPaginaEnModulo(String descripcionpagina,String nombrepagina, int idmodulo,int idpagina) {
		return getPaginaDAO().verificarPaginaEnModulo(descripcionpagina,nombrepagina,idmodulo,idpagina);
	}
	public IPaginaDAO getPaginaDAO() {
		return paginaDAO;
	} 

	public void setPaginaDAO(IPaginaDAO paginaDAO) {
		this.paginaDAO = paginaDAO;
	}
	
	public List<Paginamodulo> listarPaginasModulos() {
		return getPaginaDAO().listarPaginasModulos();
	}
	
	public Date obtenerFechaUltimaPagina() {
		return getPaginaDAO().obtenerFechaUltimaPagina();
	}
	
	public void registrarPaginamodulo(Paginamodulo paginamodulo) {
		 getPaginaDAO().registrarPaginamodulo(paginamodulo);
		
	}
	
	public void actualizarPaginamodulo(Paginamodulo paginamodulo) {
		 getPaginaDAO().actualizarPaginamodulo(paginamodulo);
		
	}
	
	public void eliminarPaginamodulo(Paginamodulo paginamodulo) {
		getPaginaDAO().eliminarPaginamodulo(paginamodulo);
		
	}
	
	public Pagina obtenerUltimaPaginaCreada() {
		return getPaginaDAO().obtenerUltimaPaginaCreada();
	}
	
	public List<Paginamodulo> listarPaginamodulos() {
		return getPaginaDAO().listarPaginamodulos();
	}

}
