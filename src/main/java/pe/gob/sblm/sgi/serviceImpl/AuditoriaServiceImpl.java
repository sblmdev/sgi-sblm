package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IAuditoriaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAuditoriaService;

@Transactional(readOnly = true)
@Service(value="panelAuditoriaServiceImpl")
public class AuditoriaServiceImpl implements IAuditoriaService{

	@Autowired
	private IAuditoriaDAO auditoriaDAO;

	
	
	
	public List<Auditoria> listarAuditoria() {
		return auditoriaDAO.listarAuditoriaSingle();
	}



	
	public List<Usuario> listUsuariobyNom() {
		return auditoriaDAO.listUsuariobyNom();
	}



	
	public List<Perfilusuario> listPerfilbyNom() {
		// TODO Auto-generated method stub
		 return auditoriaDAO.listPerfilbyNom();
	}



	
	public List<Modulo> listModulobyNom() {
		// TODO Auto-generated method stub
		return auditoriaDAO.listmodulobyNom();
	}

	
	public List<Pagina> listRecursobyNom() {
		// TODO Auto-generated method stub
		return auditoriaDAO.listRecursobyNom();
	}
	
	


	
	public String nroConectadosDelDia() {
		// TODO Auto-generated method stub
		return auditoriaDAO.nroConectadosDelDia();
	}

	

	
	public List listAuditoriaFiltro(Date fechaInicio, Date fechaFin,
			String nombreUsuario, String nomPantalla, String nomPerfil,
			String nomModulo) {

		return auditoriaDAO.listAuditoriaFiltro(fechaInicio, fechaFin,
				nombreUsuario,nomPantalla,nomPerfil,
				nomModulo);
	}
	
	
	public List listAuditoriaFiltroPerfil(Date fechaInicio, Date fechaFin,
			String nombrePerfil, String recursoBusqueda, String nomPerfil,
			String moduloBusqueda) {
		// TODO Auto-generated method stub
		return auditoriaDAO.listAuditoriaFiltroPerfil(fechaInicio, fechaFin,
				nombrePerfil,recursoBusqueda,nomPerfil,
				moduloBusqueda);
	}
	


	
	public Usuario listUsuarioTop() { 
		// TODO Auto-generated method stub
		return auditoriaDAO.listUsuarioTop();
	}



	
	public Object ultimoModuloVisitado() {
		// TODO Auto-generated method stub
		return auditoriaDAO.ultimoModuloVisitado();	}



	
	public Object ultimaPaginaVisitado() {
		// TODO Auto-generated method stub
		return  auditoriaDAO.ultimaPaginaVisitado();
	}




//
//	
//	public List<Usuario> listUserTop(String string) {
//		// TODO Auto-generated method stub
//		return auditoriaDAO.listUserTop(string);
//	}









	



	

}
