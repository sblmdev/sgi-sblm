package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.NotificacionBean;
import pe.gob.sblm.sgi.dao.BandejaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "bandejaDAO")
public class BandejaDAOImpl implements BandejaDAO, Serializable {
	private static final long serialVersionUID = -7132329520845816103L;
	Calendar fecha = Calendar.getInstance();
	private int nroMes=fecha.getTime().getMonth()+1;

	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	public List listarNotificaciones(int estado, String mesSeleccionado,
			String anioSeleccionado) {
		
		Session session = getSessionFactory().openSession();
		
		String query="";
		String queryMes = "";
		String queryAnio ="";
		
		

		if(!mesSeleccionado.equals("")){
			queryMes = "MONTH(A.fecentrada) = '"+(Integer.parseInt(mesSeleccionado)+1)+"' and "; 
		}
		if(!anioSeleccionado.equals("")){
			queryAnio = "YEAR(A.fecentrada)='"+anioSeleccionado+"' and ";
		}

		
		List<List> list = null;
		
		String queryPendientes="select new List(EA.tipoevento,U.rutaimgusr,A.idauditoria,A.fecentrada,U.nombres,U.apellidopat,U.apellidomat,A.mensajepersonalizado,A.inboxdestino,U.idusuario) from Auditoria A inner join A.usuario U inner join A.usuariodestino UD inner join A.eventoauditoria EA" +
				" WHERE A.codauditoria=1  AND UD.idusuario='"+FuncionesHelper.getUsuario().toString()+"' AND A.estadoauditoria.idestadoauditoria="+estado+" and "+queryAnio+queryMes+"  1=1 order by A.fecentrada  desc  ";
		
		
		
		list = session.createQuery(queryPendientes).list();
		List<Auditoria> lista = new ArrayList<Auditoria>();
		
		for(int i=0;i<list.size();i++){
			Usuario usu= new Usuario();
			usu.setRutaimgusr((String) list.get(i).get(1));
			usu.setNombres((String) list.get(i).get(4));
			usu.setApellidopat((String) list.get(i).get(5));
			usu.setApellidomat((String) list.get(i).get(6));
			usu.setIdusuario((Integer)list.get(i).get(9));
			
			Eventoauditoria eve= new Eventoauditoria();
			eve.setTipoevento((String) list.get(i).get(0));
			
			Auditoria auditori = new Auditoria();
			auditori.setIdauditoria((Integer)list.get(i).get(2));
			auditori.setFecentrada((Date)list.get(i).get(3));
			auditori.setMensajepersonalizado((String)list.get(i).get(7));
			auditori.setInboxdestino((String) list.get(i).get(8));
			auditori.setUsuario(usu);
			auditori.setEventoauditoria(eve);
			
			lista.add(auditori);
		}
		
		return lista;
		

	}


	
	public void actualizarPendienteToRevisado(int idauditoria) {
String updateQuery="UPDATE AUDITORIA SET IDESTADOAUDITORIA=2 WHERE IDAUDITORIA='"+idauditoria+"'";
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();			
	}

	
	public void actualizarPendienteToCancelado(int idauditoria) {
String updateQuery="UPDATE AUDITORIA SET IDESTADOAUDITORIA=3 WHERE IDAUDITORIA='"+idauditoria+"'";
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();		
	}
	
	



	
	public Object nroNotificacionesDelMes() {
		Session session = getSessionFactory().openSession();
		
		Query numero=session.createQuery("select count(*) from Auditoria A inner join A.usuario U inner join A.usuariodestino UD where A.codauditoria='1' and MONTH(A.fecentrada)="+nroMes+" and UD.idusuario='"+FuncionesHelper.getUsuario().toString()+"' and A.codauditoria='1'");
		
		
	      try {
	    	  return numero.list().get(0);
	      } catch (HibernateException e) { 
	    	  throw e;
	      }  finally { 
	           session.close();
	      }
	}
	



	
	public Object nroNotificacionesTotal() {
		Session session = getSessionFactory().openSession();

		
		Query numero=session.createQuery("select count(*) from Auditoria A inner join A.usuario U inner join A.usuariodestino UD where A.codauditoria='1'  and UD.idusuario='"+FuncionesHelper.getUsuario().toString()+"' and A.estadoauditoria.idestadoauditoria='1' and A.codauditoria='1'");
		
	      try {
	    	  return numero.list().get(0);
	      } catch (HibernateException e) { 
	    	  throw e;
	      }  finally { 
	           session.close();
	      }

	}
	
	/*Finaliza uso gadget*/


	
	public void enviarNotificacion(String contenidoMensajePersonalizado,List<Usuario> listadousuariosSeleccionados) {

		try {
			for (Usuario usuario : listadousuariosSeleccionados) {
				
//				settingLog(1, 12, usuario.getIdusuario(),contenidoMensajePersonalizado);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


	@SuppressWarnings("unchecked")
	@Override
	public List<NotificacionBean> obtenerNotificacionesxEstado(
			boolean estadoLeido, int idusuario) {
		
		StringBuilder hql= new StringBuilder();
		
		hql.append("select n.idNotificacion as idNotificacion, u.idusuario as idusuariodestino,n.mensaje as mensaje,n.uidAlfresco as uidAlfresco, n.estadoLeido as estadoLeido, n.usuarioCreador as usuarioCreador, n.fechaCreacion as fechaCreacion,n.siNotificacionTipoReporte as siNotificacionTipoReporte");
		hql.append(" from MaeNotificacion n inner join n.usuariodestino u where n.estadoLeido='"+estadoLeido+"' and u.idusuario='"+idusuario+"' order by n.fechaCreacion desc");
		
		Query query=getSessionFactory().getCurrentSession().createQuery(hql.toString());
		
		
		return	query.setResultTransformer(Transformers.aliasToBean(NotificacionBean.class)).list();
		
	}


	@Override
	public int nroNotificacionesPendiente(int idusuario) {
		StringBuilder hql= new StringBuilder();
		hql.append("select count(*)");
		hql.append(" from MaeNotificacion n inner join n.usuariodestino u where  u.idusuario='"+idusuario+"' and n.estadoLeido=0");
		
		return ((Long) getSessionFactory().getCurrentSession().createQuery(hql.toString()).uniqueResult()).intValue();
	}


	@Override
	public int nroNotificacionesRevisado(int idusuario) {
		StringBuilder hql= new StringBuilder();
		hql.append("select count(*)");
		hql.append(" from MaeNotificacion n inner join n.usuariodestino u where  u.idusuario='"+idusuario+"' and n.estadoLeido=1");
		
		return ((Long) getSessionFactory().getCurrentSession().createQuery(hql.toString()).uniqueResult()).intValue();
	}


	@Override
	public void cambiarEstadoNotificacion(int idNotificacion) {
		String updateQuery="update Mae_Notificacion set estado_leido=1 where id_notificacion='"+idNotificacion+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();	
		
	}


}
