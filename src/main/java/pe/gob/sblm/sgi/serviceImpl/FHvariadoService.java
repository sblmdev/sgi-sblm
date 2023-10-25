package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.dao.IContratoDAO;
import pe.gob.sblm.sgi.dao.IFHvariadoDAO;
import pe.gob.sblm.sgi.dao.IInquilinoDAO;
import pe.gob.sblm.sgi.dao.IUpaDAO;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IFHvariadoService;

@Transactional(readOnly = true)
@Service(value="FHvariadoService")
public class FHvariadoService implements IFHvariadoService{
	@Autowired
	private IUpaDAO upaDAO;
	
	@Autowired
	private IInquilinoDAO inquilinoDAO;
	
	@Autowired
	private IContratoDAO contratoDAO;
	
	@Autowired
	private IFHvariadoDAO FHvariadoDAO;
	
	@Autowired
	private ICarteraDAO carteraDAO;
	
	
	public List<Perfil> obtenerPerfiles() {
		
		return FHvariadoDAO.obtenerPerfiles();
	}

	
	public List<Usuario> obtenerUsuarios() {
		// TODO Auto-generated method stub
		return FHvariadoDAO.obtenerUsuarios();
	}

	
	public List<Usuario> obtenerCobradores() {
		return FHvariadoDAO.obtenerCobradores();
	}

	
	public List<ItemBean> obtenerCobradores(String texto) {
		return FHvariadoDAO.obtenerCobradores(texto);
	}

	
	public List<ItemBean> obtenerCobradoresPorNombreExacto(String nombre) {
		return FHvariadoDAO.obtenerCobradoresPorNombreExacto(nombre);
	}



}
