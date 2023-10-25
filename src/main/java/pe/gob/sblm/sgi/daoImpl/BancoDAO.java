package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IBancoServiceDAO;
import pe.gob.sblm.sgi.entity.Banco;

@Repository(value = "bancoDAO")
public class BancoDAO implements IBancoServiceDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	public void grabarbanco(Banco banco) {
		getSessionFactory().getCurrentSession().merge(banco);
	}


	public List<Banco> obtenerTodosBancos() {
		Session session = sessionFactory.openSession();

		return session.createQuery("from Banco order by fechacreacion desc").list();
	}

	public void eliminarBanco(Banco banco) {
		sessionFactory.getCurrentSession().createSQLQuery("delete Banco where idbanco='"+banco.getIdbanco()+"'").executeUpdate();
	}


	public int nroBancos() {
		Long count = (Long) sessionFactory.openSession()
				.createQuery("select COUNT(IDBANCO) from Banco").uniqueResult();
				return count.intValue();
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	}
