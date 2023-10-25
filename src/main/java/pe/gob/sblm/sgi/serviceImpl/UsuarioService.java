package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IUsuarioDAO;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IUsuarioService;

@Transactional(readOnly = true)
@Service(value="usuarioService")
public class UsuarioService implements IUsuarioService {

	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	public IUsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}


	@Transactional(readOnly=true)	
	public List<Usuario> listarUsuarios() {
		return getUsuarioDAO().listarUsuarios();
	}
	
	public List<Usuario> listarUsuarios(int iduser) {
		return getUsuarioDAO().listarUsuarios(iduser);
	}
	
	public Usuario buscarUsuario(Usuario usuario) {
		return getUsuarioDAO().buscarUsuario(usuario);
	}
	public Sunat_Comprobante_Detalle buscarComprobante(Sunat_Comprobante_Detalle cmp) {
		return getUsuarioDAO().buscarComprobante(cmp);
	}

	
	public int obtenerIdPerfil(Usuario usuario) {
		return getUsuarioDAO(). obtenerIdPerfil( usuario);
		
	}

	
	public List<String> loguear(Usuario usuario) {
		return getUsuarioDAO().loguear(usuario);
		
	}
	

	
	public Usuario buscarUsuarioxId(int parseInt) {
		// TODO Auto-generated method stub
		return getUsuarioDAO().buscarUsuarioxId(parseInt);	}

	
	public String obtenerNombrePerfilSeleccionado(Usuario u) {
		// TODO Auto-generated method stub
		return getUsuarioDAO().obtenerNombrePerfilSeleccionado(u);
	}

	
	public Usuario obtenerUsuario(String nombreusr) {
		// TODO Auto-generated method stub
		return getUsuarioDAO().obtenerUsuario(nombreusr);
	}


	
	public boolean siAutorizado(int idperfil, int idpagina) {
		return getUsuarioDAO().siAutorizado(idperfil,idpagina);
	}


	


}
