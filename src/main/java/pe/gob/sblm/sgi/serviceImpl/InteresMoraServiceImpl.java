package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IInteresMoraDAO;
import pe.gob.sblm.sgi.entity.InteresMora;
import pe.gob.sblm.sgi.entity.Tipointeresmora;
import pe.gob.sblm.sgi.service.IInteresMoraService;

@Transactional(readOnly = true)
@Service(value="interesMoraService")
public class InteresMoraServiceImpl implements IInteresMoraService{

	@Autowired
	private IInteresMoraDAO interesMoraDAO;
	
	@Transactional(readOnly = false)
	public void grabarInteresMora(InteresMora interes) {
		     interesMoraDAO.grabarInteresMora(interes);		
	}
	@Override
	public List<InteresMora> listarInteresMora(String tipointeres) {		
		return interesMoraDAO.listarInteresMora(tipointeres);
	}

	@Override
	public InteresMora obtenerInteresMora(String tipointeres, Integer idinteres, String fecha) {		
		return interesMoraDAO.obtenerInteresMora(tipointeres, idinteres, fecha);
	}

	@Override
	public InteresMora obtenerUltimoInteresMora(String tipointeres) {
		return interesMoraDAO.obtenerUltimoInteresMora(tipointeres);
	}
	
	@Override
	public Tipointeresmora obtenerTipoInteresMora(String tipointeres) {		
		return interesMoraDAO.obtenerTipoInteresMora(tipointeres);
	}
	@Override
	public List<Tipointeresmora> listarTipoInteresMora() {		
		return interesMoraDAO.listarTipoInteresMora();
	}



}
