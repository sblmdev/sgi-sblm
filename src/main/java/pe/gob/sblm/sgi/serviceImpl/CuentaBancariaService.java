package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.ICuentaBancariaDAO;
import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.service.ICuentaBancariaService;

@Transactional(readOnly = true)
@Service(value="cuentabancariaService")
public class CuentaBancariaService implements ICuentaBancariaService{
	@Autowired
	private ICuentaBancariaDAO cuentabancariaDAO;
	


	@Transactional(readOnly = false)
	
	public void grabarCuentaBancaria(Cuentabancaria cuentabancaria) {
		cuentabancariaDAO.grabarCuentaBancaria(cuentabancaria);
		
	}

	
	public List<Cuentabancaria> obtenerTodasCuentasBancarias() {
		
		return cuentabancariaDAO.obtenerTodasCuentasBancarias();
	}

	@Transactional(readOnly = false)
	
	public void eliminarCuentabancaria(Cuentabancaria cuentabancaria) {
		cuentabancariaDAO.eliminarCuentabancaria(cuentabancaria);
		
	}

	
	public int nroCuentasBancarias() {
		
		return cuentabancariaDAO.nroCuentasBancarias();
	}

	
	public List<Banco> listBancos() {
		// TODO Auto-generated method stub
		return cuentabancariaDAO.listBancos();
	}



}
