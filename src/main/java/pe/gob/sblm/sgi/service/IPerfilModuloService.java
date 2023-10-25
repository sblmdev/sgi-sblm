package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;

public interface IPerfilModuloService {

	public void registrarPerfilModulo(Perfilmodulo perfilmodulo);

	public void actualizarPerfilModulo(Perfilmodulo perfilmodulo);

	public void eliminarPerfilModulo(Perfilmodulo perfilmodulo);

	public Perfil listarPerfilModuloPorId(int id);

	public List<Perfilmodulo> listarPerfilmodulos();
	
	//
	public void eliminarPerfilModuloId(int idperfil);
	
	public List<Perfilmodulo> listarPerfilModuloPorIdPerfil (int idperfil);
}
