package pe.gob.sblm.sgi.daoImpl;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.dao.IProcesoJudicialUpaDAO;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;

@Repository(value= "procesoJudicialUpaDAO")
public class ProcesoJudicialUpaDAO implements IProcesoJudicialUpaDAO, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<ProcesojudicialupaBean> buscarExpediente(int idprocesojudicialupa) {
		List<ProcesojudicialupaBean> lista = new ArrayList<ProcesojudicialupaBean>();
		StringBuilder hql=new StringBuilder();
		hql.append("select p.idprocesojudicialupa as idprocesojudicialupa ,u.idupa as idupa , p.expediente as expediente , p.estado as estado , p.motivo as motivo , p.situacion as situacion ,p.sidocumento as sidocumento, p.sifinalizado as sifinalizado, p.observacion as observacion, ");
    	hql.append("p.usrcre as usrcre ,p.feccre as feccre ,p.usrmod as usrmod ,p.fecmod as fecmod ");
    	hql.append(" from Procesojudicialupa p ");
    	hql.append("inner join p.upa u ");
    	hql.append("where p.idprocesojudicialupa="+idprocesojudicialupa);
		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (ArrayList<ProcesojudicialupaBean>)query.setResultTransformer(Transformers.aliasToBean(ProcesojudicialupaBean.class)).list();
		//return lista;
	}
	
	@Transactional(readOnly=true)
	public Procesojudicialupa buscarDatosExpediente(int idprocesojudicial){
		return (Procesojudicialupa) getSessionFactory().getCurrentSession().createQuery(
				"select pj from Procesojudicialupa pj where pj.idprocesojudicialupa="+idprocesojudicial)
				.uniqueResult();
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

}
