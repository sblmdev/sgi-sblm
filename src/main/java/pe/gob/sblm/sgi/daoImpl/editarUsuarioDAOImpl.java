package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IEditarUsuarioDAO;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "editarusuarioDAO")
public class editarUsuarioDAOImpl implements IEditarUsuarioDAO, Serializable {
	private static final long serialVersionUID = -7132329520845816103L;
	AuditoriaDAOImpl audilog= new AuditoriaDAOImpl();
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	
	@SuppressWarnings("unchecked")
	
	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario) {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Usuario where Idusuario='"+selectIdRegistroUsuario+"'").list();
	}
	
	

	
	public void actualizarUsuario(String nombreUsuario, String nombreRegistro, String pat,
			String mat, String cargoRegistro, String emailRegistro,
			String passRegistro, String formatearFechaString,String ruta) {
		
		String updateQuery;
		if(passRegistro.equals("")){

			 updateQuery=	"UPDATE USUARIO  SET [NOMBRES] ='"+nombreRegistro+"'"+
					",[APELLIDOMAT] ='" +mat+"'"+
					",[APELLIDOPAT] ='" +pat+"'"+
					",[FECHANACIMIENTO] ='"+formatearFechaString+"'"+
					",[NOMBREUSR] ='" +nombreUsuario+"'"+
//					",[CONTRASENAUSR] ='" +passRegistro+"'"+
			//		",[FECCRE] = <FECCRE, date,>" +
					",[RUTAIMGUSR] ='" +ruta+"'"+
					",[USRCRE] ='super'"+
					",[EMAILUSR] ='" +emailRegistro+"'"+
					",[CARGO] ='" +cargoRegistro+"'"+
					" WHERE IDUSUARIO='"+FuncionesHelper.getUsuario().toString()+"'";
			
		}else{

			 updateQuery=	"UPDATE USUARIO  SET [NOMBRES] ='"+nombreRegistro+"'"+
					",[APELLIDOMAT] ='" +mat+"'"+
					",[APELLIDOPAT] ='" +pat+"'"+
					",[FECHANACIMIENTO] ='"+formatearFechaString+"'"+
					",[NOMBREUSR] ='" +nombreUsuario+"'"+
					",[CONTRASENAUSR] ='" +passRegistro+"'"+
			//		",[FECCRE] = <FECCRE, date,>" +
					",[RUTAIMGUSR] ='" +ruta+"'"+
					",[USRCRE] ='super'"+
					",[EMAILUSR] ='" +emailRegistro+"'"+
					",[CARGO] ='" +cargoRegistro+"'"+
					" WHERE IDUSUARIO='"+FuncionesHelper.getUsuario().toString()+"'";
		}
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
		
	}

	

	
	
}
