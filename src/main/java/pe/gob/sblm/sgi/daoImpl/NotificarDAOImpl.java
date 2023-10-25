package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.NotificarServiceDAO;
import pe.gob.sblm.sgi.entity.MaeNotificacion;

@Repository(value = "notificarDAO")
public class NotificarDAOImpl implements NotificarServiceDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void notificarNuevoRegistroContrato(MaeNotificacion maeNotificacion) {
		sessionFactory.getCurrentSession().save(maeNotificacion);
	}

	
}
