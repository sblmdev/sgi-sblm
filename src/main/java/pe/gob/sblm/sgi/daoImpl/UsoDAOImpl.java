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
import pe.gob.sblm.sgi.dao.IUsoDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
@Repository(value = "usoDAO")
public class UsoDAOImpl implements IUsoDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	
	public void registrarUso(Uso uso) {
		
		getSessionFactory().getCurrentSession().merge(uso);
		 try {
			 //idestadoauditoria 0 auditoria / 1 notificacion
			 settingLog(14);
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
		
	}

	
	public void actualizarUso(Uso uso) {
		// TODO Auto-generated method stub
		
	}

	
	public void eliminarUso(Uso uso) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Uso WHERE iduso='"
									+ uso.getIduso() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar Uso:::"
					+ e.getMessage());
		}
	}

	
	
	public List<Uso> listarUsos() {
Session session = getSessionFactory().openSession();
		
	    try{
	    	return session.createQuery("from Uso order by feccre desc").list();
	    }
	    catch(HibernateException e){
	    	System.out.println("error listarUsos:::"+e.getMessage());
	    	throw e;
	    }
	    finally{
	    	session.close();
	    }
	}

	
	public Uso obtenerUltimoUso() {
		
		 return (Uso) getSessionFactory().openSession().createQuery(
					"select t from Uso t where iduso=( select max(iduso) from Uso)").uniqueResult();
		 
	}
	
	public int obtenerNumeroRegistros() {
		

		 Long count = (Long) getSessionFactory().openSession()
					.createQuery("select count(*) from Uso").uniqueResult();
          return count.intValue();
		 
	}
	
	
	public Uso obtenerUsoPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Uso) session.load(Uso.class, id);
	
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
}
