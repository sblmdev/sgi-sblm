package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IPaginaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "paginaDAO")
public class PaginaDAO implements IPaginaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	
	public void registrarPagina(Pagina pagina) {
		getSessionFactory().getCurrentSession().merge(pagina);
		 try {
		 settingLog(4);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
	}

	
	public void actualizarPagina(Pagina pagina) {

	}

	
	public void eliminarPagina(Pagina pagina) {


		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from PAGINA WHERE idpagina='"
									+ pagina.getIdpagina() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar pagina:::"
					+ e.getMessage());
		}

	}



	
	public List<Pagina> listarPaginas() {
		return null;


	}

	
	public int obtenerNumeroPaginas() {
		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Pagina").uniqueResult();

		return count.intValue();
	}

	
	public String obtenerUltimaPagina() {
		return (String) getSessionFactory()
				.openSession()
				.createQuery(
						"select p.descripcionpagina from Pagina p where idpagina=( select max(idpagina) from Pagina)")
				.uniqueResult();

	}

	
	public Pagina verificarPaginaEnModulo(String descripcionpagina,String nombrepagina, int idmodulo,int idpagina) {
		return (Pagina) getSessionFactory()
				.openSession()
				.createQuery(
						"select  p  from  Pagina p  where p.idpagina!='"+idpagina+"' and p.nombrepagina= LTRIM( RTRIM('"+nombrepagina+"'))  ) ")
				.uniqueResult();

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<Paginamodulo> listarPaginasModulos() {
		return 	getSessionFactory()
				.openSession()
				.createQuery(
						"select pm from  Paginamodulo pm LEFT JOIN FETCH pm.modulo m LEFT JOIN FETCH pm.pagina p").list();
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

	
	public Date obtenerFechaUltimaPagina() {
		return (Date) getSessionFactory()
				.openSession()
				.createQuery(
						"select t.feccre from Pagina t where idpagina=( select max(idpagina) from Pagina)")
				.uniqueResult();

	}

	
	public void registrarPaginamodulo(Paginamodulo paginamodulo) {
		getSessionFactory().getCurrentSession().save(paginamodulo);

	}

	
	public void actualizarPaginamodulo(Paginamodulo paginamodulo) {

		String updateQuery = "UPDATE Paginamodulo  SET [idmodulo] ='"
				+ paginamodulo.getModulo().getIdmodulo()
				+ "' WHERE idpaginamodulo=" + paginamodulo.getIdpaginamodulo()
				+ " ";

		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery)
				.executeUpdate();

	}

	
	public void eliminarPaginamodulo(Paginamodulo paginamodulo) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Paginamodulo WHERE idpaginamodulo='"
									+ paginamodulo.getIdpaginamodulo() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar paginamodulo:::"
					+ e.getMessage());
		}

	}

	
	public Pagina obtenerUltimaPaginaCreada() {
		return (Pagina) getSessionFactory()
				.openSession()
				.createQuery(
						" from Pagina t where idpagina=( select max(idpagina) from Pagina)")
				.uniqueResult();

	}

	
	public List<Paginamodulo> listarPaginamodulos() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Paginamodulo pm order by pm.pagina.feccre desc").list();
		} catch (HibernateException e) {
			System.out.println("error listado Paginamodulo dao:::" + e);
			throw e;
		}
	}
}
