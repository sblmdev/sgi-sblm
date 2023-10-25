package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IClienteServiceDAO;
import pe.gob.sblm.sgi.entity.Cliente;

@Repository(value = "clienteDAO")
public class ClienteDAO implements IClienteServiceDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;


	
	public void grabarCliente(Cliente cliente) {
		getSessionFactory().getCurrentSession().merge(cliente);
	}


	
	public List<Cliente> obtenerTodosClientes() {
		Session session = sessionFactory.openSession();

		return session.createQuery("from Cliente order by fechacreacion desc").list();
	}

	
	public void eliminarCliente(Cliente cliente) {
		sessionFactory.getCurrentSession().createSQLQuery("delete Cliente where idCliente='"+cliente.getIdcliente()+"'").executeUpdate();
	}


	
	public int nroClientes() {
		Long count = (Long) sessionFactory.openSession()
				.createQuery("select COUNT(IDCliente) from Cliente").uniqueResult();
				return count.intValue();
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	}
