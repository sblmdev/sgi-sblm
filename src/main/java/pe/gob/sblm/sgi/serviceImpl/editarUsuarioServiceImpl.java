package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IEditarUsuarioDAO;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IEditarUsuarioService;

@Transactional(readOnly = true)
@Service(value="panelEditarUsuarioServiceImpl")
public class editarUsuarioServiceImpl implements IEditarUsuarioService{

	@Autowired
	private IEditarUsuarioDAO editarusuarioDAO;



	
	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario) {

		return editarusuarioDAO.getUsuarioEditSinPerfil(selectIdRegistroUsuario);
	}



	
	public void actualizarUsuarioMaestro(String nombreUsuario,String nombreRegistro, String pat,
			String mat, String cargoRegistro, String emailRegistro,
			String passRegistro, String formatearFechaString,String ruta) {

		editarusuarioDAO.actualizarUsuario(nombreUsuario,nombreRegistro,pat,mat,cargoRegistro,emailRegistro,passRegistro,formatearFechaString,ruta);
		
	}


	
	
	
}
