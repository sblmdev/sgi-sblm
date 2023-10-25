package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IClienteServiceDAO;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.service.IClienteService;

@Transactional(readOnly = true)
@Service(value="clienteService")
public class ClienteService implements IClienteService{
	@Autowired
	private IClienteServiceDAO clienteDAO;
	


	@Transactional(readOnly = false)
	
	public void grabarCliente(Cliente cliente) {
		clienteDAO.grabarCliente(cliente);
		
	}

	
	public List<Cliente> obtenerTodosClientes() {
		
		return clienteDAO.obtenerTodosClientes();
	}

	@Transactional(readOnly = false)
	
	public void eliminarCliente(Cliente cliente) {
		clienteDAO.eliminarCliente(cliente);
		
	}

	
	public int nroClientes() {
		
		return clienteDAO.nroClientes();
	}



}
