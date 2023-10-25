package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.TipoDocuPersonaDAO;
import pe.gob.sblm.sgi.entity.Tipodocupersona;

@Repository(value="TipoDocuPersonaDAOImpl")
public class TipoDocuPersonaDAOImpl implements TipoDocuPersonaDAO,Serializable{


	private static final long serialVersionUID = 1L;
	private static final Logger Logger= LoggerFactory.getLogger(TipoDocuPersonaDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Tipodocupersona> listarTipoDocumentoPersona() {
		
		return getSessionFactory().getCurrentSession().createQuery("from Tipodocupersona ").list();

	}

	public Tipodocupersona obtenerTipoDocPersona(String idtipo) {
	
		return (Tipodocupersona) getSessionFactory().getCurrentSession().createQuery("from Tipodocupersona where idtipodocupersona ='"+idtipo+"'").uniqueResult();
	}
	
	@Override
	public Integer longitudTipoDocumentoPersona(String idtipo) {
		// TODO Auto-generated method stub
		return (Integer)getSessionFactory().getCurrentSession().createQuery("select longitud from Tipodocupersona where idtipodocupersona ='"+idtipo+"'").uniqueResult();
	}
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	
	

}
