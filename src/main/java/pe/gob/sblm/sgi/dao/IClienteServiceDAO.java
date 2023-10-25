package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Cliente;

public interface IClienteServiceDAO {

	void grabarCliente(Cliente cliente);

	List<Cliente> obtenerTodosClientes();

	void eliminarCliente(Cliente cliente);

	int nroClientes();

}
