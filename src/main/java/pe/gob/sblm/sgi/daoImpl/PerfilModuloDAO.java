package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IPerfilModuloDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "perfilmoduloDAO")
public class PerfilModuloDAO implements IPerfilModuloDAO,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	public void registrarPerfilModulo(Perfilmodulo perfilmodulo) {
		System.out.println(":::registro perfil modulo DAO:::");
		
		getSessionFactory().getCurrentSession().merge(perfilmodulo);
		
	}
	


	
	public void actualizarPerfilModulo(Perfilmodulo perfilmodulo) {
		// TODO Auto-generated method stub
		
	}

	
	public void eliminarPerfilModulo(Perfilmodulo perfilmodulo) {
		// TODO Auto-generated method stub
		
	}

	
	public Perfil listarPerfilModuloPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Perfilmodulo> listarPerfilmodulos() {
		
		Session session = getSessionFactory().openSession();
		
	    try{
	    	return session.createQuery("from Perfilmodulo").list();
	    }
	    catch(HibernateException e){
	    	System.out.println("error:::"+e);
	    	throw e;
	    }
	    finally{
	    	session.close();
	    }

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<Perfilmodulo> listarPerfilModuloPorIdPerfil(int idperfil) {
		
		return getSessionFactory().getCurrentSession().createQuery("select pm from Perfilmodulo  pm LEFT JOIN FETCH pm.perfil p  LEFT JOIN FETCH pm.modulo m LEFT JOIN FETCH pm.permiso pe where p.idperfil='"+idperfil+"' order by pm.feccre  asc ").list();
//		Session session = getSessionFactory().openSession();
//		return session.createQuery("select pm from Perfilmodulo  pm LEFT JOIN FETCH pm.perfil p  LEFT JOIN FETCH pm.modulo m  where p.idperfil="+idperfil+" ").list();
	}

	
	public void eliminarPerfilModuloId(int idperfil) {
		try {System.out.println("#############paso a dao eliminarPerfilModuloId");
		getSessionFactory().getCurrentSession().createSQLQuery("delete from PERFILMODULO WHERE idperfil='"+idperfil+"'").executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao:::"+e.getMessage());
		}
		
	}
	
}
