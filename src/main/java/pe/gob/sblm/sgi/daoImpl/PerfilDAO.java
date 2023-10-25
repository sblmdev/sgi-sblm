package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IPerfilDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.util.PerfilModuloPermiso;

@Repository(value = "perfilDAO")
public class PerfilDAO implements IPerfilDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;
	Transaction txt = null;

	
	public void registrarPerfil(Perfil perfil) {
		System.out.println(":::xxx registro perfil DAO xxx:::");
		getSessionFactory().getCurrentSession().merge(perfil);
		 try {
		 //idestadoauditoria 0 auditoria / 1 notificacion
		 settingLog(6);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
	}

	
	public void actualizarPerfil(Perfil perfil) {
		// TODO Auto-generated method stub

	}

	
	public void eliminarPerfil(Perfil perfil) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Perfil WHERE idperfil='"
									+ perfil.getIdperfil() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar eliminarPerfil:::"
					+ e.getMessage());
		}
	}

	
	public Perfil listarPerfilPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Perfil) session.load(Perfil.class, id);
	}

	
	public List<Perfil> listarPerfiles() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Perfil order by feccre desc")
					.list();
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public int obtenerUltimoIdPerfil() {

		Integer val = (Integer) getSessionFactory().openSession()
				.createQuery("  select max(p.idperfil) from Perfil p")
				.uniqueResult();
		// if(val == null){
		// System.out.println("ultimo perfil#####");
		// return 0;}
		// else
		return val;

	}

	
	public List<PerfilModuloPermiso> listarPerfilesModulosPermisos() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from PerfilModuloPermiso").list();
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	public void settingLog( int ideventoauditoria) {
		String url = FuncionesHelper.getURL().toString();

		try {
			int index = url.indexOf("pages/");
			url = url.substring(index + 6, url.length());
			url = url.substring(0, url.length() - 4);
		} catch (Exception e) {
		}
		Auditoria Adt = new Auditoria();
		Usuario usr = new Usuario();
		usr.setIdusuario((Integer) (FuncionesHelper.getUsuario()));

		Modulo mod = new Modulo();
		Session session = getSessionFactory().openSession();

		Query numero = session
				.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"
						+ url + "'");

		int var = (Integer) numero.uniqueResult();
		System.out.println("###########::::" + var);
		mod.setIdmodulo(var);
		Estadoauditoria esa = new Estadoauditoria();
		esa.setIdestadoauditoria(4);
		Eventoauditoria eva = new Eventoauditoria();
		eva.setIdeventoauditoria(ideventoauditoria);
		Adt.setUsuario(usr);
		Adt.setModulo(mod);
		Adt.setEstadoauditoria(esa);
		Adt.setEventoauditoria(eva);
		Adt.setFecentrada(new Date());
		Adt.setNompantalla(url);
		Adt.setUrl(FuncionesHelper.getURL().toString());
		if (FuncionesHelper.getTerminal().toString().equals("0:0:0:0:0:0:0:1")) {
			Adt.setIp("192.");
		} else {
			Adt.setIp(FuncionesHelper.getTerminal().toString());
		}
		Adt.setEstado(true);
		Adt.setCodauditoria(0);
		try {
			getSessionFactory().getCurrentSession().save(Adt);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	public String obtenerUltimoPerfil() {
		return (String) getSessionFactory()
				.openSession()
				.createQuery(
						"select p.nombreperfil from Perfil p where p.idperfil=( select max(idperfil) from Perfil)")
				.uniqueResult();
	}

	
	public Date obtenerFechaUltimoPerfil() {
		Date fecha = (Date) getSessionFactory()
				.openSession()
				.createQuery(
						"select p.feccre from Perfil p where p.idperfil=( select max(idperfil) from Perfil)")
				.uniqueResult();
		return fecha;
	}

	
	public int obtenerNumeroPerfiles() {

		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Perfil").uniqueResult();

		return count.intValue();

	}
}
