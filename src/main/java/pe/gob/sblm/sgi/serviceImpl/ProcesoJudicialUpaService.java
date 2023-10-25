package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.dao.IProcesoJudicialUpaDAO;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
import pe.gob.sblm.sgi.service.IProcesoJudicialUpaService;

@Transactional(readOnly = true)
@Service(value="procesoJudicialUpaService")
public class ProcesoJudicialUpaService implements IProcesoJudicialUpaService{
    @Autowired
	private IProcesoJudicialUpaDAO procesoJudicialUpaDAO;
    
	@Override
	public List<ProcesojudicialupaBean> buscarExpediente(int idprocesojudicialupa) {
		// TODO Auto-generated method stub
		return procesoJudicialUpaDAO.buscarExpediente(idprocesojudicialupa);
	}
	@Override
	public  Procesojudicialupa buscarDatosExpediente(int idprocesojudicialupa){
		return procesoJudicialUpaDAO.buscarDatosExpediente(idprocesojudicialupa);
	}

}
