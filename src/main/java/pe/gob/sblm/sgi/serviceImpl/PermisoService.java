package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPermisoDAO;
import pe.gob.sblm.sgi.entity.Permiso;
import pe.gob.sblm.sgi.service.IPermisoService;

@Transactional(readOnly = true)
@Service(value="permisoService")
public class PermisoService implements IPermisoService{

	@Autowired
	private IPermisoDAO permisoDAO;

	
	@Transactional(readOnly = false)
	
	public void registrarPermiso(Permiso permiso) {
		getPermisoDAO().registrarPermiso(permiso);
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarPermiso(Permiso permiso) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarPermiso(Permiso permiso) {
		// TODO Auto-generated method stub
		
	}

	
	public Permiso listarPermisoPorId(int id) {
		return getPermisoDAO().listarPermisoPorId(id);
	}

	
	public List<Permiso> listarPermisos() {
		return getPermisoDAO().listarPermisos();
	}
	public IPermisoDAO getPermisoDAO() {
		return permisoDAO;
	}
	public void setPermisoDAO(IPermisoDAO permisoDAO) {
		this.permisoDAO = permisoDAO;
	}
}
