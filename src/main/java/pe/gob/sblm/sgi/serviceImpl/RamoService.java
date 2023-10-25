package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IRamoDAO;
import pe.gob.sblm.sgi.entity.Ramo;
import pe.gob.sblm.sgi.service.IRamoService;

@Transactional(readOnly = true)
@Service(value="ramoService")
public class RamoService implements IRamoService {
	@Autowired
	private IRamoDAO ramoDAO;

	@Transactional(readOnly = false)
	
	public void registrarRamo(Ramo ramo) {
		getRamoDAO().registrarRamo(ramo);
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarRamo(Ramo ramo) {
		getRamoDAO().actualizarRamo(ramo);
	}
	@Transactional(readOnly = false)
	
	public void eliminarRamo(Ramo ramo) {
		getRamoDAO().eliminarRamo(ramo);
		
	}

	
	public List<Ramo> listarRamos() {
		
		return getRamoDAO().listarRamos();
	}

	
	public Ramo obtenerUltimoRamo() {
		// TODO Auto-generated method stub
		return getRamoDAO().obtenerUltimoRamo();
	}

	
	public Ramo obtenerRamoPorId(int id) {
		// TODO Auto-generated method stub
		return getRamoDAO().obtenerRamoPorId(id);
	}

	
	public int obtenerNumeroRegistros() {
		// TODO Auto-generated method stub
		return getRamoDAO().obtenerNumeroRegistros();
	}

	public IRamoDAO getRamoDAO() {
		return ramoDAO;
	}

	public void setRamoDAO(IRamoDAO ramoDAO) {
		this.ramoDAO = ramoDAO;
	}
	
}
