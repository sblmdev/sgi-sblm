package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.service.IClienteService;

@ManagedBean(name = "clienteMB")
@ViewScoped
public class ClienteManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{clienteService}")
	private transient IClienteService clienteService;
	Cliente cliente;
	List<Cliente> listaClientees;
	private int nroClientees;
	private Cliente ultimaCliente;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	

	@PostConstruct
	public void initObjects() {
		cliente = new Cliente();
		obtenerClientees();
		obtenerUltimaClienteCreada();
		nroClientees();
	}
	
	public void obtenerUltimaClienteCreada() {
		if (listaClientees.size()!=0) {
			ultimaCliente=listaClientees.get(0);
		}
		
	
	}
	
	public void obtenerClientees(){
		listaClientees=clienteService.obtenerTodosClientes();
	}

	public void limpiarRegistro() {
		cliente=new Cliente();
		cliente.setIdcliente(0);
	}
	
	public void grabarRepresentante(){
		if (cliente.getIdcliente()==0) {
			cliente.setFechacreacion(new Date());
			cliente.setUsuariocreador(userMB.getUsuariologueado().getNombrescompletos());
			clienteService.grabarCliente(cliente);
			limpiarRegistro();
			obtenerClientees();
			nroClientees();
			obtenerUltimaClienteCreada();
		} else {
			cliente.setFechamodificacion(new Date());
			cliente.setUsuariomodificador(userMB.getUsuariologueado().getNombrescompletos());
			clienteService.grabarCliente(cliente);
			limpiarRegistro();
			obtenerClientees();
			nroClientees();
		}
		
	}
	
	public void nroClientees(){
		nroClientees=clienteService.nroClientes();
		
	}
	
	public void eliminarRepresentante() {
		clienteService.eliminarCliente(cliente);
		obtenerClientees();
		nroClientees();
		obtenerUltimaClienteCreada();
		limpiarRegistro();
	}

	public IClienteService getclienteService() {
		return clienteService;
	}

	public void setClienteService(IClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListaClientees() {
		return listaClientees;
	}

	public void setListaClientees(List<Cliente> listaClientees) {
		this.listaClientees = listaClientees;
	}
	public int getNroClientees() {
		return nroClientees;
	}
	public void setNroClientees(int nroClientees) {
		this.nroClientees = nroClientees;
	}
	public UsuarioManagedBean getUserMB() {
		return userMB;
	}
	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}
	public Cliente getUltimaCliente() {
		return ultimaCliente;
	}
	public void setUltimaCliente(Cliente ultimaCliente) {
		this.ultimaCliente = ultimaCliente;
	}
	
}
