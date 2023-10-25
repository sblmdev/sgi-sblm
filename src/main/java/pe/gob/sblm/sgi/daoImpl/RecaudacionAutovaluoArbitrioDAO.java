package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.dao.IRecaudacionAutovaluoArbitrioDAO;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Detallearbitrio;

@Repository(value = "recaudacionautovaluoarbitrioDAO")
public class RecaudacionAutovaluoArbitrioDAO implements IRecaudacionAutovaluoArbitrioDAO,Serializable{

	private static final long serialVersionUID = -8268300686535569846L;
	
	@Autowired
	private SessionFactory sessionFactory;
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<ArbitrioBean> listarArbitriosxAnio(String idupa) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select arb.idarbitrio as idarbitrio,arb.anio as anio,arb.frecuencia as frecuencia");
		hql.append(" from Arbitrio arb ");
		hql.append(" inner join arb.upa u ");
		hql.append("where u.idupa='"+idupa+"'");

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
	}
	@SuppressWarnings("unchecked")
	
	public List<ArbitrioBean> listarArbitriosxAnio(int idupa) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select arb.idarbitrio as idarbitrio,arb.anio as anio,arb.frecuencia as frecuencia");
		hql.append(" from Arbitrio arb ");
		hql.append(" inner join arb.upa u ");
		hql.append("where u.idupa='"+idupa+"'");

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
	}
	
	public void grabarPeriodoArbitrio(Arbitrio arbitrio) {
		getSessionFactory().getCurrentSession().save(arbitrio);
		
	}

	
	public boolean siexistePeriodo(int idupa, int anioseleccionado) {
		Long  val= (Long) getSessionFactory().getCurrentSession().createQuery("select count(*) from Arbitrio arb inner join arb.upa u where u.idupa='"+idupa+"' and arb.anio='"+anioseleccionado+"'").uniqueResult();
		
		if (val.intValue()==0) {
			return false;
		} else {
			return true;
		}
	}

	
	public void grabarDetallePeriodoArbitrio(Detallearbitrio detalleArbitrioBean) {
		 getSessionFactory().getCurrentSession().save(detalleArbitrioBean);
		}

	
	@SuppressWarnings("unchecked")
	
	public List<DetalleArbitrioBean> listarDetalleArbitrio(int idarbitrio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select da.iddetallearbitrio as iddetallearbitrio,da.mes as mes,da.insoluto as insoluto, da.observacion as situacion, da.fechavencimiento as fecVencimiento,");
		hql.append(" da.nrorecibo as nrorecibo,da.secuencia as secuencia,da.mora as mora, da.usrcre as usrcre, da.usrmod as usrmod,da.fecmod as fecmod ,da.feccre as feccre ");
		hql.append(" from Detallearbitrio da ");
		hql.append(" inner join da.arbitrio a ");
		hql.append("where a.idarbitrio='"+idarbitrio+"'");

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return query.setResultTransformer(Transformers.aliasToBean(DetalleArbitrioBean.class)).list();
	}

	
	public void editarDetalleArbitrio(DetalleArbitrioBean detalleArbitrioBean) {
		StringBuilder hql = new StringBuilder();
		hql.append("update Detallearbitrio set ");
		hql.append("nrorecibo = :nrorecibo, ");
		hql.append("insoluto = :insoluto, ");
		hql.append("mora = :mora, ");
		hql.append("fechavencimiento = :fechavencimiento, ");
		hql.append("observacion = :observacion ");
		hql.append("where iddetallearbitrio = :iddetallearbitrio");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("nrorecibo", detalleArbitrioBean.getNrorecibo());
		query.setParameter("insoluto", detalleArbitrioBean.getInsoluto());
		query.setParameter("mora", detalleArbitrioBean.getMora());
		query.setParameter("fechavencimiento", detalleArbitrioBean.getFecVencimiento());
		query.setParameter("observacion", detalleArbitrioBean.getSituacion());
		query.setParameter("iddetallearbitrio", detalleArbitrioBean.getIddetallearbitrio());
		query.executeUpdate();
	}
}
