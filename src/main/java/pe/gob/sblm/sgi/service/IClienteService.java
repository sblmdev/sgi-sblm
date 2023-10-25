package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Cliente;

public interface IClienteService {

	public void grabarCliente(Cliente cliente);

	public List<Cliente> obtenerTodosClientes();

	public void eliminarCliente(Cliente cliente);

	public int nroClientes();

}
