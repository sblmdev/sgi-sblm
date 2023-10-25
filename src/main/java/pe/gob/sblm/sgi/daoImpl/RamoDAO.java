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
import pe.gob.sblm.sgi.dao.IRamoDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Ramo;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "ramoDAO")
public class RamoDAO implements IRamoDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	public void registrarRamo(Ramo ramo) {
		getSessionFactory().getCurrentSession().merge(ramo);
		 try {
			 settingLog(17);
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
	}

	
	public void actualizarRamo(Ramo ramo) {
		// TODO Auto-generated method stub
		
	}

	
	public void eliminarRamo(Ramo ramo) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Ramo WHERE idramo='"
									+ ramo.getIdramo() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar ramo:::"
					+ e.getMessage());
		}
	}

	
	public List<Ramo> listarRamos() {

Session session = getSessionFactory().openSession();
		
	    try{
	    	return session.createQuery("from Ramo order by feccre desc").list();
	    }
	    catch(HibernateException e){
	    	System.out.println("error listar ramos:::"+e.getMessage());
	    	throw e;
	    }
	    finally{
	    	session.close();
	    }
	}

	
	public Ramo obtenerUltimoRamo() {
		 return (Ramo) getSessionFactory().openSession().createQuery(
					"select t from Ramo t where idramo=( select max(idramo) from Ramo)").uniqueResult();
	
	}

	
	public Ramo obtenerRamoPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Ramo) session.load(Ramo.class, id);
	}

	
	public int obtenerNumeroRegistros() {
		 Long count = (Long) getSessionFactory().openSession()
					.createQuery("select count(*) from Ramo").uniqueResult();
    return count.intValue();
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
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
