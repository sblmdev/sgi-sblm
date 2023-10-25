package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IIgvDAO;
import pe.gob.sblm.sgi.dao.IUsoDAO;
import pe.gob.sblm.sgi.entity.Igv;
import pe.gob.sblm.sgi.service.IIgvService;

@Transactional(readOnly = true)
@Service(value="igvService")
public class IgvService implements IIgvService {
	@Autowired
	private IIgvDAO igvDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarIgv(Igv igv) {
		getIgvDAO().registrarIgv(igv);
		
	}

	
	public void actualizarIgv(Igv igv) {
		// TODO Auto-generated method stub
		
	}

	
	public void eliminarIgv(Igv igv) {
		 getIgvDAO().eliminarIgv(igv);
		
	}

	
	public List<Igv> listarIgvs() {
		return getIgvDAO().listarIgvs();
	}

	
	public Igv obtenerUltimoIgv() {
		return getIgvDAO().obtenerUltimoIgv();
	}

	
	public Igv obtenerIgvPorId(int id) {
		return getIgvDAO().obtenerIgvPorId(id);
	}

	
	public int obtenerNumeroRegistros() {
		return getIgvDAO().obtenerNumeroRegistros();
	}

	public IIgvDAO getIgvDAO() {
		return igvDAO;
	}

	public void setIgvDAO(IIgvDAO igvDAO) {
		this.igvDAO = igvDAO;
	}

}
