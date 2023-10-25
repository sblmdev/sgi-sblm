package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.InquilinoBean;
import pe.gob.sblm.sgi.dao.IInquilinoDAO;
import pe.gob.sblm.sgi.entity.Califica;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Tipoentidad;

@Repository(value = "inquilinoDAO")
public class InquilinoDAO implements IInquilinoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	public void registrarInquilino(Inquilino inquilino) {
		try{
		getSessionFactory().getCurrentSession().merge(inquilino);
		}
		catch (HibernateException e) {
			System.out.println("error :::" + e);
			throw e;
		}
	}

	
	public void actualizarInquilino(Inquilino inquilino) {
		// TODO Auto-generated method stub
		try{
			getSessionFactory().getCurrentSession().update(inquilino);
			}
			catch (HibernateException e) {
				System.out.println("error :::" + e);
				throw e;
			}
	}

	
	public void eliminarInquilino(Inquilino inquilino) {
//		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Inquilino WHERE idinquilino='"
									+ inquilino.getIdinquilino() + "'")
					.executeUpdate();
			
			
//		} catch (Exception e) {
//			System.out.println("error en dao eliminar inquilino:::"
//					+ e.getMessage());
//		}
		
	}

	public List<Califica> listarCalificacion(Inquilino inqui) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Califica where  idinquilino='"+inqui.getIdinquilino()+"' ").list();
		} catch (HibernateException e) {
			System.out.println("error listado listarCalificacion dao:::" + e);
			throw e;
		}
	}
	
	
	public void eliminarCalificacion(Inquilino inquilino) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Califica WHERE idinquilino='"
									+ inquilino.getIdinquilino() + "'")
					.executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar inquilino:::"
					+ e.getMessage());
		}
		
	}
	
	public Inquilino obtenerUltimoInquilino() {
		return (Inquilino) getSessionFactory()
				.openSession()
				.createQuery(
						"select t from Inquilino t where idinquilino=( select max(idinquilino) from Inquilino)")
				.uniqueResult();
	}
	
	
	
	public Inquilino validarInquilinoRepetido(String valor,Integer idtipopersona) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select t from Inquilino t");
		
		if (idtipopersona==Constantes.TIPO_PERSONA_NATURAL) {
			hql.append(" where dni='"+valor+"'  ");
		} else {
			hql.append(" where ruc='"+valor+"'  ");
		}
		
		return (Inquilino) getSessionFactory().getCurrentSession().createQuery(hql.toString()).uniqueResult();
	}
	

	
	public int obtenerNumeroRegistros() {
		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Inquilino").uniqueResult();
		return count.intValue();
	}

	
	public List<Tipoentidad> listarTipoEntidad() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Tipoentidad order by feccre desc").list();
		} catch (HibernateException e) {
			System.out.println("error listado Tipoentidad dao:::" + e);
			throw e;
		}
	}
	
	public List<Califica> listarCalificacion() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Califica order by concepto").list();
		} catch (HibernateException e) {
			System.out.println("error listado Califica dao:::" + e);
			throw e;
		}
	}
	
	
	public void registrarCalificacion(Califica calificacion) {
		
		
		try {
			getSessionFactory().getCurrentSession().save(calificacion);
		} catch (Exception e) {
			System.out.println("####errorx::"+e.getMessage());
		}
	}
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Inquilino> listarInquilinos(String valorbusqueda,String tipoBusqueda) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select i from Inquilino i ");
		
		if (tipoBusqueda.equals(Constantes.BUSQUEDA_NOMBRES_RAZONSOCIAL)) {
			hql.append("where i.nombrescompletos like '%"+valorbusqueda+"%' ");
			
			
		} else if(tipoBusqueda.equals(Constantes.BUSQUEDA_RUC)){
			hql.append("where i.ruc like '"+valorbusqueda+"%'");
			
		} else if(tipoBusqueda.equals(Constantes.BUSQUEDA_DNI)){
			hql.append("where i.dni like '"+valorbusqueda+"%'");
		}
		
		return sessionFactory.getCurrentSession().createQuery(hql.toString()).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InquilinoBean> listarInquilino(InquilinoBean inq) {
		List<InquilinoBean> lista = new ArrayList<InquilinoBean>();
		String query="" ,query1="" ,query2="",query3="",query4="",query5="",query6="",query7="",query8="";
		if(inq.getIdtipopersona()>0){
			query1="and tp.idtipopersona='"+inq.getIdtipopersona()+"' ";
			query=query+query1;
		}
		if(inq.getApellidopat()!=null){
			if(inq.getApellidopat().length()>0){
				query2="and i.apellidopat like '%"+inq.getApellidopat()+"%' ";
				query=query+query2;
			}
		}
		if(inq.getApellidomat()!=null){
			if(inq.getApellidomat().length()>0){
				query3="and i.apellidomat like '%"+inq.getApellidomat()+"%' ";
				query=query+query3;
			}
		}
		if(inq.getNombre()!=null){
			if(inq.getNombre().length()>0){
				query4="and i.nombre like '%"+inq.getNombre()+"%' ";
				query=query+query4;
			}
		}
		if(inq.getDni()!=null){
			if(inq.getDni().length()>0){
				query5="and i.dni like '%"+inq.getDni()+"%' ";
				query=query+query;
			}
		}
		if(inq.getRuc()!=null){
			if(inq.getRuc().length()>0){
				query6="and i.ruc like '%"+inq.getRuc()+"%' ";
				query=query+query6;
			}
		}
		if(inq.getCarnetextranjeria()!=null){
			if(inq.getCarnetextranjeria().length()>0){
				query7="and i.carnetextranjeria like '%"+inq.getCarnetextranjeria()+"%' ";
				query=query+query7;
			}
		}
		if(inq.getRazonsocial()!=null){
			if(inq.getRazonsocial().length()>0){
				query8="and i.razonsocial like '%"+inq.getRazonsocial()+"%' ";
				query=query+query8;
			}
		}

		StringBuilder hql= new StringBuilder();
		hql.append("select tp.idtipopersona as idtipopersona,i.apellidopat as apellidopat, i.apellidomat as apellidomat , i.nombre as nombre,i.razonsocial as razonsocial  ");
		hql.append(",i.dni as dni , i.ruc as ruc ,i.carnetextranjeria as carnetextranjeria ,i.sifallecido as sifallecido ,i.nombrescompletos as nombrescompletos, i.sianulado as sianulado ,tp.descripcion as descripciontipopersona  ");
		hql.append(",i.usrcre as usrcre,i.usrmod as usrmod, i.feccre as feccre, i.fecmod as fecmod  ");
		hql.append("from Inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("where 1=1 "+query);
		hql.append("order by i.apellidopat asc ");
		
		Query querysql= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = querysql.setResultTransformer(Transformers.aliasToBean(InquilinoBean.class)).list();
		return lista;
	}
    
	


}
