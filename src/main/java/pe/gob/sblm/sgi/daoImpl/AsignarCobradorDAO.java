package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IAsignarCobradorServiceDAO;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "asignarcobradorDAO")
public class AsignarCobradorDAO implements IAsignarCobradorServiceDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;




	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	public void asignarCobrador(Usuario usuario) {
		
		String query="UPDATE USUARIO SET ESCOBRADOR='"+usuario.getEscobrador()+"',COD_COB='"+usuario.getCodCob()+"' WHERE idusuario='"
				+ usuario.getIdusuario() + "'";
		
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(query)
					.executeUpdate();
	}
	
	}
