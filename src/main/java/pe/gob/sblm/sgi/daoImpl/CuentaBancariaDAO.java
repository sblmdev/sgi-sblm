package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.ICuentaBancariaDAO;
import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.entity.Cuentabancaria;

@Repository(value = "cuentabancariaDAO")
public class CuentaBancariaDAO implements ICuentaBancariaDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	public void grabarCuentaBancaria(Cuentabancaria cuentabancaria) {
		getSessionFactory().getCurrentSession().merge(cuentabancaria);
		
	}


	
	public List<Cuentabancaria> obtenerTodasCuentasBancarias() {
		Session session = sessionFactory.openSession();
		return session.createQuery("from Cuentabancaria order by fechacreacion desc").list();
	}


	
	public void eliminarCuentabancaria(Cuentabancaria cuentabancaria) {
		sessionFactory.getCurrentSession().createSQLQuery("delete Cuentabancaria where idcuentabancaria='"+cuentabancaria.getIdcuentabancaria()+"'").executeUpdate();
	}


	
	public int nroCuentasBancarias() {
		Long count = (Long) sessionFactory.openSession()
				.createQuery("select COUNT(IDCUENTABANCARIA) from Cuentabancaria").uniqueResult();
				return count.intValue();
	}


	
	public List<Banco> listBancos() {
		Session session = sessionFactory.openSession();
		return session.createQuery("from Banco").list();
	}
	
	}
