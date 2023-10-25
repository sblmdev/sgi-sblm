package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IAlertaDAO;
import pe.gob.sblm.sgi.entity.Alerta;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "alertaDAO")
public class AlertaDAO implements IAlertaDAO,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
 
	Session sesion = null; 
	
	
	public void registrarAlerta(Alerta alerta) {
		getSessionFactory().getCurrentSession().merge(alerta);
	}

	
	public void actualizarAlerta(Alerta alerta) {
		// TODO Auto-generated method stub
		
	}

	
	public void eliminarAlerta(Alerta alerta) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Alerta WHERE idialerta='"
									+ alerta.getIdalerta() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar alerta:::"
					+ e.getMessage());
		}
		
	}
	
	
	public Usuario obtenerUsuarioxNombreUsuario(String nombre) {
		System.out.println("zzzzzzzz");
		return (Usuario) getSessionFactory().getCurrentSession().createQuery("from Usuario u where u.nombrescompletos="+nombre+"").uniqueResult();
	}
	
	public List<Alerta> listarAlertas() {
		Session session = getSessionFactory().openSession();

		try {//left join fetch inm.upas :para carga perezosa
			return session.createQuery("from Alerta ").list();
		} catch (HibernateException e) {
			System.out.println("error listado Alerta dao:::" + e);
			throw e;
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public List<Alerta> alertasNoenviadas() {
		
		Date now= new Date();
		
		
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Alerta A where  A.estado='false' and A.fechaejecucion<='"+FuncionesHelper.fechaHORAtoString(now)+"'").list();
	}

	
	public void enviarNotificacion(int idalerta, String mensaje, int idusuario,int idusuarioenvio) {
		
		try {
			settingLog(1, 12, idusuario,mensaje,idusuarioenvio);
		} catch (Exception e) {
			e.getMessage();
		}
		
	}
	

	
	public void actualizarAenviado(int idalerta) {
		getSessionFactory().getCurrentSession().createSQLQuery("UPDATE ALERTA SET ESTADO='TRUE' WHERE IDALERTA='"+idalerta+"'").executeUpdate();
	}
	
	
	public  void settingLog(int idestadoauditoria, int ideventoauditoria, int idusuariodestino, String mensajePersonalizado, int idusuarioenvio){
		
		String url=FuncionesHelper.getURL().toString();
	
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {e.getMessage();	}

				Session session = sessionFactory.openSession();
				Auditoria Adt= new Auditoria();
				Usuario usr= new Usuario();
				usr.setIdusuario(idusuarioenvio);
				
				Usuario usrdes= new Usuario();
				usrdes.setIdusuario(idusuariodestino);
				
				Modulo mod= new Modulo();
				
				SQLQuery modulo=session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"+url+"'");
				
				if (modulo.list().size()==1) {
					int var=(Integer) modulo.list().get(0);
					mod.setIdmodulo(var);
				} else {
					//mod.setIdmodulo(idmodulo)
				}
				
				
				Estadoauditoria esa= new Estadoauditoria();
				esa.setIdestadoauditoria(idestadoauditoria);
				Eventoauditoria eva= new Eventoauditoria();
				eva.setIdeventoauditoria(ideventoauditoria);
				Adt.setUsuario(usr);
				Adt.setUsuariodestino(usrdes);
				Adt.setModulo(mod);
				Adt.setEstadoauditoria(esa);
				Adt.setEventoauditoria(eva);
				Adt.setFecentrada( new Date());
				Adt.setNompantalla(url);
				Adt.setUrl(FuncionesHelper.getURL().toString());
				Adt.setIp(FuncionesHelper.getTerminal().toString());
				Adt.setEstado(true);
				Adt.setCodauditoria(1);
				Adt.setMensajepersonalizado(mensajePersonalizado);
				
		try {
			sessionFactory.getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	}



}
