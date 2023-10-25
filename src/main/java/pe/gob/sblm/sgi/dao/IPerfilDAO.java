package pe.gob.sblm.sgi.dao;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.util.PerfilModuloPermiso;

public interface IPerfilDAO {

	public void registrarPerfil(Perfil perfil);

	public void actualizarPerfil(Perfil perfil);

	public void eliminarPerfil(Perfil perfil);

	public Perfil listarPerfilPorId(int id);

	public List<Perfil> listarPerfiles();
	public List<PerfilModuloPermiso> listarPerfilesModulosPermisos();
	
	public int obtenerUltimoIdPerfil();
	public String obtenerUltimoPerfil();
	public int obtenerNumeroPerfiles();
	
	public Date obtenerFechaUltimoPerfil();
}
