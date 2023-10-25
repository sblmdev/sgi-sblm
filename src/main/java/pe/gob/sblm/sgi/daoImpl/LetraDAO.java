package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.ILetraDAO;
import pe.gob.sblm.sgi.entity.Letra;

@Repository(value = "letraDAO")
public class LetraDAO implements ILetraDAO,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
 
	Session sesion = null;

	
	public void registrarLetra(Letra letra) {
		sessionFactory.getCurrentSession().merge(letra);
		
		
	} 
	

}
