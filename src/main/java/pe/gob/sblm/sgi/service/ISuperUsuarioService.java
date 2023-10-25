package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

public interface ISuperUsuarioService {

	public List<Usuario> listarUsuarios();


	public void save(Perfilusuario perfilusuario);

	public void nuevoUsuario(Usuario usr);

	public  List<Perfil> listPerfilbyNom();

	public  Object getLastIdUsuario();

	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario);

	public List getUsuarioEditConPerfil(String selectIdRegistroUsuario);

	public void eliminarUsuario(String selectIdRegistroUsuario);

	public String existenciaUsuario();

	public void actualizarUsuario(Usuario u, String fechaUpdate);

	void eliminarAntiguoPerfilUsuario(int idusuario);

	public List<Area> getAllArea();

	public Usuario getUsuarioaEditar(String selectIdRegistroUsuario);

	public List<Perfilusuario> getPerfilesUsuario(String selectIdRegistroUsuario);

	



}
