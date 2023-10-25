package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IRepresentanteServiceDAO;
import pe.gob.sblm.sgi.entity.Representante;
@Repository(value = "representateDAO")
public class RepresentanteDAO implements IRepresentanteServiceDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;


	
	public void grabarRepresentante(Representante representante) {
		sessionFactory.getCurrentSession().merge(representante);
	}

	
	public List<Representante> obtenerTodosRepresentantes() {
		Session session = sessionFactory.openSession();

		return session.createQuery("from Representante order by fechacreacion desc").list();
	}

	
	public void eliminarRepresentante(Representante representante) {
		sessionFactory.getCurrentSession().createSQLQuery("delete Representante where idrepresentante='"+representante.getIdrepresentante()+"'").executeUpdate();
	}


	
	public int nrorepresentantes() {
		Long count = (Long) sessionFactory.openSession()
				.createQuery("select COUNT(IDREPRESENTANTE) from Representante").uniqueResult();
				return count.intValue();
	}
	}
