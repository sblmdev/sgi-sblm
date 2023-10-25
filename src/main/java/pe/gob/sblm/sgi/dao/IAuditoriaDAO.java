package pe.gob.sblm.sgi.dao;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

public interface IAuditoriaDAO {

	public List<Auditoria> listarAuditoriaSingle();

	public List<Usuario> listUsuariobyNom();

	public List<Perfilusuario> listPerfilbyNom();

	public List<Modulo> listmodulobyNom();

	public List<Auditoria> listAuditoriaFiltro(Date fechaInicio, Date fechaFin,
			String nombreUsuario, String nomPantalla, String nomPerfil,
			String nomModulo);

	public List<Pagina> listRecursobyNom();

	public String nroConectadosDelDia();

	public Usuario listUsuarioTop();

//	public List<Usuario> listUserTop(String string);

	public List<Auditoria> listAuditoriaFiltroPerfil(Date fechaInicio, Date fechaFin,
			String nombrePerfil, String recursoBusqueda, String nomPerfil,
			String moduloBusqueda);

	public Object ultimoModuloVisitado();

	public Object ultimaPaginaVisitado();

	
}
