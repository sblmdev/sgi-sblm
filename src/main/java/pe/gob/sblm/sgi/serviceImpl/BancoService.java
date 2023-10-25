package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IBancoServiceDAO;
import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.service.IBancoService;

@Transactional(readOnly = true)
@Service(value="bancoService")
public class BancoService implements IBancoService{
	@Autowired
	private IBancoServiceDAO bancoDAO;
	


	@Transactional(readOnly = false)
	
	public void grabarBanco(Banco banco) {
		bancoDAO.grabarbanco(banco);
		
	}

	
	public List<Banco> obtenerTodosBancos() {
		
		return bancoDAO.obtenerTodosBancos();
	}

	@Transactional(readOnly = false)
	
	public void eliminarBanco(Banco banco) {
		bancoDAO.eliminarBanco(banco);
		
	}

	
	public int nroBancos() {
		
		return bancoDAO.nroBancos();
	}



}
