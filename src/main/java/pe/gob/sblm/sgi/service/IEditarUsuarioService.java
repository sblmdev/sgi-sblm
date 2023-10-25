package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Usuario;

public interface IEditarUsuarioService {
	
	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario);
	public void actualizarUsuarioMaestro(String nombreUsuario,String nombreRegistro, String pat,
			String mat, String cargoRegistro, String emailRegistro,
			String passRegistro, String formatearFechaString, String ruta);
	
}
