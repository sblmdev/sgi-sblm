package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Usuario;


public interface IUsuarioService {


	public List<Usuario> listarUsuarios();
	
	public  Usuario buscarUsuario(Usuario usuario);
	
	public int obtenerIdPerfil(Usuario usuario);
	
	public List<String> loguear(Usuario usuario);
	
	
	public List<Usuario> listarUsuarios(int iduser);

	public Usuario buscarUsuarioxId(int parseInt);

	public String obtenerNombrePerfilSeleccionado(Usuario u);

	public Usuario obtenerUsuario(String nombreusr);

	public boolean siAutorizado(int idperfil, int idpagina);
	public  Sunat_Comprobante_Detalle buscarComprobante(Sunat_Comprobante_Detalle cmp);

	
}
