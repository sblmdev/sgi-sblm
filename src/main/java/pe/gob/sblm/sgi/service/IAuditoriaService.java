package pe.gob.sblm.sgi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

public interface IAuditoriaService {

	public List<Auditoria> listarAuditoria();

	public List<Usuario> listUsuariobyNom();

	public List<Perfilusuario> listPerfilbyNom();

	public List<Modulo> listModulobyNom();

	public List listAuditoriaFiltro(Date fechaInicio, Date fechaFin,
			String nombreUsuario, String nomPantalla, String nomPerfil,
			String nomModulo);

	public List<Pagina> listRecursobyNom();

	public String nroConectadosDelDia();

	public Usuario listUsuarioTop();

//	public List<Usuario> listUserTop(String string);

	public List listAuditoriaFiltroPerfil(Date fechaInicio,
			Date fechaFin, String nombrePerfil, String recursoBusqueda,
			String nomPerfil, String moduloBusqueda);

	public Object ultimoModuloVisitado();

	public Object ultimaPaginaVisitado();

	

}
