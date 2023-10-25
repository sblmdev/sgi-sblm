package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.IInteresMoraDAO;
import pe.gob.sblm.sgi.entity.InteresMora;
import pe.gob.sblm.sgi.entity.Tipointeresmora;

@Repository(value="interesMoraDAOImpl")
public class InteresMoraDAOImpl implements IInteresMoraDAO,Serializable{

	private static final long serialVersionUID = -37160255276803954L;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public InteresMoraDAOImpl() {
		
	}

	@Override
	public void grabarInteresMora(InteresMora interes) {
		
		getSessionFactory().getCurrentSession().saveOrUpdate(interes);
	}

	@SuppressWarnings("unchecked")
	public List<InteresMora> listarInteresMora(String tipointeres) {
		
		return getSessionFactory().getCurrentSession().createQuery("select i from InteresMora i inner join i.tipointeresmora ti where ti.id_tipointeres='"+tipointeres+"' order by i.fec_reg desc").list();
	}
	@Override
	public InteresMora obtenerInteresMora(String tipointeres, Integer idinteres , String fecha) {
		String query1="", query2="";
		if(idinteres!=null){
			if(idinteres>0){
				query1=" and i.id_interes="+idinteres ;	
			}else{
				
			}
		}
		if(fecha!=null){
			query2=" and i.fec_reg='"+fecha+"'";
		}
        //query1=idinteres!=null? (idinteres>0?(query1=" and t.id_interes="+idinteres):query1):query1;		
		//query2=fecha!=null? (" and t.fec_reg='"+fecha+"'"):query2 ;
		return (InteresMora) getSessionFactory().getCurrentSession().createQuery("Select i from InteresMora i inner join i.tipointeresmora ti where ti.id_tipointeres='"+tipointeres+"'"+query1+query2 ).uniqueResult();
	}
	
	@Override
	public InteresMora obtenerUltimoInteresMora(String tipointeres) {
		
		return (InteresMora) getSessionFactory().getCurrentSession().createQuery("Select t from InteresMora t where t.fec_reg=(select max(i.fec_reg) from InteresMora i inner join i.tipointeresmora ti where ti.id_tipointeres='"+tipointeres+"') and t.tipointeresmora.id_tipointeres='"+tipointeres+"'").uniqueResult();
	}
	@Override
	public Tipointeresmora obtenerTipoInteresMora(String tipointeres) {
		
		return (Tipointeresmora) getSessionFactory().getCurrentSession().createQuery("Select t from Tipointeresmora t where  t.id_tipointeres='"+tipointeres+"'").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Tipointeresmora> listarTipoInteresMora() {
		
		return getSessionFactory().getCurrentSession().createQuery("Select t from Tipointeresmora t order by t.id_tipointeres asc").list();
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	

}
