package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.dao.AccionDAO;
import pe.gob.sblm.sgi.entity.MaeAccion;
import pe.gob.sblm.sgi.service.AccionService;

@Service(value="accionService")
public class AccionServiceImpl implements AccionService {
	@Autowired
	private AccionDAO accionDAO;

	@Transactional(readOnly=true)
	public List<AccionUsuarioBean> obtenerUsuarioAccion(String nombreAccion) {
		
		return accionDAO.obtenerUsuarioAccion(nombreAccion);
	}

	@Transactional(readOnly=true)
	public List<AccionUsuarioBean> obtenerUsuarioAccion(int idaccion) {
		return accionDAO.obtenerUsuarioAccion(idaccion);
	}
	
	@Transactional(readOnly=true)
	public List<MaeAccion> obtenerAcciones() {
		return accionDAO.obtenerAcciones();
	}


	@Transactional(readOnly=false)
	public void grabarUsuarioAccion(int idusuario,int idaccion) {
		accionDAO.grabarUsuarioAccion(idusuario,idaccion);
	}

	@Transactional(readOnly=false)
	public void eliminarUsuarioAccion(int idusuario,
			int idaccion) {
		accionDAO.eliminarUsuarioAccion(idusuario,idaccion);
		
	}
}
