package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;

public interface IPerfilModuloDAO {

	public void registrarPerfilModulo(Perfilmodulo perfilmodulo);

	public void actualizarPerfilModulo(Perfilmodulo perfilmodulo);

	public void eliminarPerfilModulo(Perfilmodulo perfilmodulo);

	public Perfil listarPerfilModuloPorId(int id);

	public List<Perfilmodulo> listarPerfilmodulos();
	
	public List<Perfilmodulo> listarPerfilModuloPorIdPerfil (int idperfil);
	
	public void eliminarPerfilModuloId(int idperfil);
}
