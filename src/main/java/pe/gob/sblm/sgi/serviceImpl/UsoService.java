package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IUsoDAO;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.service.IUsoService;
@Transactional(readOnly = true)
@Service(value="usoService")
public class UsoService implements IUsoService{
	@Autowired
	private IUsoDAO usoDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarUso(Uso uso) {
		getUsoDAO().registrarUso(uso);
			
	}
	@Transactional(readOnly = false)
	
	public void actualizarUso(Uso uso) {
		// TODO Auto-generated method stub
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarUso(Uso uso) {
		 getUsoDAO().eliminarUso(uso);
		
	}

	
	public List<Uso> listarUsos() {
		return getUsoDAO().listarUsos();
	}

	
	public Uso obtenerUltimoUso() {
		return getUsoDAO().obtenerUltimoUso();
	}
	
	public int obtenerNumeroRegistros() {
		return getUsoDAO().obtenerNumeroRegistros();
	}
	
	
	public Uso obtenerUsoPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	public IUsoDAO getUsoDAO() {
		return usoDAO;
	}
	public void setUsoDAO(IUsoDAO usoDAO) {
		this.usoDAO = usoDAO;
	}

}
