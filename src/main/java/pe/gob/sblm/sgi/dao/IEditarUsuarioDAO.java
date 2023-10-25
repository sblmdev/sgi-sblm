package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Usuario;

public interface IEditarUsuarioDAO {

	List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario);

	void actualizarUsuario(String nombreUsuario, String nombreRegistro,
			String pat, String mat, String cargoRegistro, String emailRegistro,
			String passRegistro, String formatearFechaString, String ruta);

}
