package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IPerfilUsuarioDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "perfilusuarioDAO")
public class PerfilUsuarioDAO implements IPerfilUsuarioDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	
	public void registrarPerfilUsuario(Perfilusuario perfilusuario) {
		getSessionFactory().getCurrentSession().save(perfilusuario);
		
		

	}

	
	public void actualizarPerfilUsuario(Perfilusuario perfilusuario) {
		getSessionFactory().getCurrentSession().merge(perfilusuario);


	}

	
	public void eliminarPerfilUsuario(Perfilusuario perfilusuario) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Perfilusuario WHERE idperfilusuario='"
									+ perfilusuario.getIdperfilusuario() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar perfil usuario:::"
					+ e.getMessage());
		}
	}

	
	public Perfilusuario listarPerfilUsuarioPorId(int id) {
		Session session=getSessionFactory().openSession();
        return (Perfilusuario) session.load(Perfilusuario.class, id);
	}

	@SuppressWarnings("unchecked")
	
	public List<Perfilusuario> listarPerfilUsuarios() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Perfilusuario").list();
		} catch (HibernateException e) {
			System.out.println("error listarPerfilUsuarios:::" + e);
			throw e;
		} 
//		finally {
//			session.close();
//		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public  void settingLog(int idusuario, int idmodulo, int idestadoauditoria, int ideventoauditoria, Date fechaentrada, String nombrepantalla,
			String url,Boolean estado, int codauditoria){


	Auditoria Adt= new Auditoria();
	Usuario usr= new Usuario();
	usr.setIdusuario(idusuario);
	Modulo mod= new Modulo();
	mod.setIdmodulo(idmodulo);
	Estadoauditoria esa= new Estadoauditoria();
	esa.setIdestadoauditoria(idestadoauditoria);
	Eventoauditoria eva= new Eventoauditoria();
	eva.setIdeventoauditoria(ideventoauditoria);
	Adt.setUsuario(usr);
	Adt.setModulo(mod);
	Adt.setEstadoauditoria(esa);
	Adt.setEventoauditoria(eva);
	Adt.setFecentrada(fechaentrada);
	Adt.setNompantalla(nombrepantalla);
	Adt.setUrl(url);
	Adt.setEstado(estado);
	Adt.setCodauditoria(codauditoria);
	try {
	getSessionFactory().getCurrentSession().save(Adt);
	
	} catch (Exception e) {
	e.printStackTrace();		}
	
	
	
	}

	@SuppressWarnings("unchecked")
	
	public List<Perfilusuario> listarPerfilUsuariosPorId(int id) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("select pu from Perfilusuario pu left join fetch pu.perfil p  left join fetch pu.usuario u where u.idusuario="+id+" ").list();
		} catch (HibernateException e) {
			System.out.println("error listarPerfilUsuariosPorId:::" + e);
			throw e;
		} 
	}

}
