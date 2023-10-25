package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.ITipoCambioDAO;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.service.ITipoCambioService;

@Transactional(readOnly = true)
@Service(value="tipocambioService")
public class TipoCambioService implements ITipoCambioService{

	@Autowired
	private ITipoCambioDAO tipocambioDAO;
	
	public ITipoCambioDAO getTipocambioDAO() {
		return tipocambioDAO;
	}

	public void setTipocambioDAO(ITipoCambioDAO tipocambioDAO) {
		this.tipocambioDAO = tipocambioDAO;
	}

	@Transactional(readOnly = false)
	
	public void registrarTipoCambio(Tipocambio tipoCambio) {
		getTipocambioDAO().registrarTipoCambio(tipoCambio);
		
	}

	public Tipocambio obtenerTipoCambio() {
		return getTipocambioDAO().obtenerTipoCambio();
	}


	
	public Tipocambio listarTipoCambioPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Tipocambio> listarTipoCambios() {
		return getTipocambioDAO().listarTipoCambios();
	}
	
	
	public Tipocambio obtenerUltimoTipocambio() {
		return getTipocambioDAO().obtenerUltimoTipocambio();
	}

	
	public Double obtenerTipoCambio(String fecha) {
		return getTipocambioDAO().obtenerTipoCambio(fecha);
	}
	
	public Tipocambio getTipoCambio() {
		return getTipocambioDAO().getTipoCambio();
	}
}
