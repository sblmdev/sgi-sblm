package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Perfilusuario;

public interface IPerfilUsuarioDAO {

	public void registrarPerfilUsuario(Perfilusuario perfilusuario);

	public void actualizarPerfilUsuario(Perfilusuario perfilusuario);

	public void eliminarPerfilUsuario(Perfilusuario perfilusuario);

	public Perfilusuario listarPerfilUsuarioPorId(int id);

	public List<Perfilusuario> listarPerfilUsuarios();
	
	public List<Perfilusuario> listarPerfilUsuariosPorId(int id);
}
