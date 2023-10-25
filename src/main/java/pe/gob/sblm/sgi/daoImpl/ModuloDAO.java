package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IModuloDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "moduloDAO")
public class ModuloDAO implements IModuloDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	
	public void registrarModulo(Modulo modulo) {

		getSessionFactory().getCurrentSession().merge(modulo);

		 try {
		 //idestadoauditoria 0 auditoria / 1 notificacion
		 settingLog(5);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }

	}

	
	public void actualizarModulo(Modulo modulo) {
		// TODO Auto-generated method stub

	}

	
	public void eliminarModulo(Modulo modulo) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from MODULO WHERE idmodulo='"
									+ modulo.getIdmodulo() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar modulo:::"
					+ e.getMessage());
		}
	}

	
	public void eliminarPaginaModulo(int idmodulo) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Paginamodulo WHERE idmodulo='"
									+ idmodulo + "' and  valortabla='modulo' ")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar eliminarPaginaModulo:::"
					+ e.getMessage());
		}
	}

	
	public Perfil listarPerfilPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Perfil) session.load(Perfil.class, id);
	}

	
	public List<Perfilmodulo> verficarModuloEnPerfil(int idmodulo) {

		Session session = getSessionFactory().openSession();

		try {
			System.out.println("aaaaaaaaaaa");
			return session.createQuery(
					"from Perfilmodulo where idmodulo= " + idmodulo + " ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	
	public Modulo listarModuloPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Modulo) session.load(Modulo.class, id);
	}

	@SuppressWarnings("unchecked")
	
	public List<Modulo> listarModulos() {

		Session session = getSessionFactory().openSession();

		try {
			return session
					.createQuery(
							"from Modulo m where m.nombremodulo!='sistema' order by feccre desc")
					.list();
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}

	}
	@SuppressWarnings("unchecked")

	public Integer obtenerIdPaginaModulo(int idmodulo,int idpagina) {

		Session session = getSessionFactory().openSession();

		try {
			return (Integer)(session.createQuery("select pm.idpaginamodulo from Paginamodulo pm  inner join pm.pagina p where pm.modulo.idmodulo="+ idmodulo + "and pm.pagina.idpagina="+idpagina)).uniqueResult();
			//return (ArrayList<Pagina>)(session.createQuery("select pm.pagina  from Paginamodulo pm  inner join pm.pagina p where pm.modulo.idmodulo="+ idmodulo + "")).list();
		} catch (HibernateException e) {
			System.out.println("error DAO idPaginasDeModulos:::" + e);
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

	public int obtenerNumeroModulos() {

		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Modulo").uniqueResult();

		return count.intValue();

	}

	
	public String obtenerUltimoModulo() {
		// TODO Auto-generated method stub
		return (String) getSessionFactory()
				.openSession()
				.createQuery(
						"select t.nombremodulo from Modulo t where idmodulo=( select max(idmodulo) from Modulo)")
				.uniqueResult();
	}

	
	public Modulo obtenerUltimoModulocreado() {
		// TODO Auto-generated method stub
		return (Modulo) getSessionFactory()
				.openSession()
				.createQuery(
						"select t from Modulo t where idmodulo=( select max(idmodulo) from Modulo)")
				.uniqueResult();
	}

	
	public Date obtenerFechaUltimoModulo() {
		// TODO Auto-generated method stub
		return (Date) getSessionFactory()
				.openSession()
				.createQuery(
						"select t.feccre from Modulo t where idmodulo=( select max(idmodulo) from Modulo)")
				.uniqueResult();
	}

	public void settingLog( int ideventoauditoria) {
		String url = FuncionesHelper.getURL().toString();

		try {
			int index = url.indexOf("pages/");
			url = url.substring(index + 6, url.length());
			//url = url.substring(0, url.length() - 4);
			System.out.println("url ="+ url);
		} catch (Exception e) {
		}
		Auditoria Adt = new Auditoria();
		Usuario usr = new Usuario();
		Perfil per= new Perfil();
		usr.setIdusuario((Integer) (FuncionesHelper.getUsuario()));
		//perfil
		per.setIdperfil((Integer) (FuncionesHelper.getPerfil()));
		Modulo mod = new Modulo();
		Session session = getSessionFactory().openSession();

		Query numero = session
				.createSQLQuery("select m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA inner join PERFILMODULO pem on m.IDMODULO=pem.IDMODULO where P.NOMBREPAGINA='"
						+ url + "' and pem.IDPERFIL='"+per.getIdperfil() +"' order by m.idmodulo");

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

	
	public void registrarPaginamodulo(Paginamodulo paginamodulo) {
		getSessionFactory().getCurrentSession().saveOrUpdate(paginamodulo);
		getSessionFactory().getCurrentSession().flush();
		
	}

	
	@SuppressWarnings("unchecked")
	public List<Pagina> listarPaginasDeModulos(int idmodulo) {

		Session session = getSessionFactory().openSession();

		try {
			return (ArrayList<Pagina>)(session.createQuery("select pm.pagina  from Paginamodulo pm  inner join pm.pagina p where pm.modulo.idmodulo="+ idmodulo + " and pm.valortabla='modulo'")).list();
			//return (ArrayList<Pagina>)(session.createQuery("select pm.pagina  from Paginamodulo pm  inner join pm.pagina p where pm.modulo.idmodulo="+ idmodulo + "")).list();
		} catch (HibernateException e) {
			System.out.println("error DAO listarPaginasDeModulos:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	
	public List<Pagina> listarPaginas() {

		Session session = getSessionFactory().openSession();

		try {

			List<Pagina> lstpagina = new ArrayList<Pagina>();

			lstpagina = session.createQuery("select p from Pagina p").list();

			for (int i = 0; i < lstpagina.size(); i++) {
				Pagina usrA = (Pagina) lstpagina.get(i);
				for (int j = i + 1; j < lstpagina.size(); j++) {
					Pagina usrB = (Pagina) lstpagina.get(j);
					if (usrA.getNombrepagina().equals(usrB.getNombrepagina())) {
						lstpagina.remove(j);
						j = j - 1;
					}
				}
			}

			// List<Pagina> lstpagina2= new ArrayList <Pagina>();
			// session.createQuery("select p from Pagina p where").list();
			// for (Pagina pagina : lstpagina) {
			//
			// }
			//
			//
			//
			// for (Iterator<Pagina> iter = lstpagina.iterator();
			// iter.hasNext();) {
			// Pagina algo = iter.next();
			// //hacer algo con algo
			// if (algo.equals(pagina)) {
			// iter.remove(); //Esto quita el elemento actual de la lista, SIN
			// causar problemas
			// }
			// }
			//

			return lstpagina;

		} catch (HibernateException e) {
			System.out.println("error DAO listarPaginas s:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

}
