package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.NumeracionComprobantePagoBean;
import pe.gob.sblm.sgi.dao.MaeNumeracionComprobantePagoDAO;
@Repository(value = "maeNumeracionComprobantePagoDAO")
public class MaeNumeracionComprobantePagoDAOImpl implements MaeNumeracionComprobantePagoDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public NumeracionComprobantePagoBean obtenerNumeracionBoletaFactura(String idtipodocuseleccionado, String nombrecarteraContrato) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select mncp.idNumeracionComprobantepago as idnumeracioncomprobantepago, mscp.serie as serie,mncp.numeracion as numero from MaeNumeracionComprobantepago mncp ");
		hql.append("inner join mncp.maeSerieComprobantepago mscp ");
		hql.append("inner join mscp.cartera car ");
		hql.append("inner join mscp.tipodocu td ");
		hql.append("WHERE mncp.LUsado='false' and td.idtipodocu='"+idtipodocuseleccionado+"' and car.numero='"+nombrecarteraContrato+"' ");
		hql.append("order by mncp.idNumeracionComprobantepago asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setMaxResults(1);
		return (NumeracionComprobantePagoBean) query.setResultTransformer(Transformers.aliasToBean(NumeracionComprobantePagoBean.class)).uniqueResult();
		
	}
	
	
	@Override
	public NumeracionComprobantePagoBean obtenerNumeracionNotaDebitoCredito(String idtipodocuseleccionado, String nombrecarteraContrato,String idtipodocpadrenotadebito) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select mncp.idNumeracionComprobantepago as idnumeracioncomprobantepago, mscp.serie as serie,mncp.numeracion as numero from MaeNumeracionComprobantepago mncp ");
		hql.append("inner join mncp.maeSerieComprobantepago mscp ");
		hql.append("inner join mscp.cartera car ");
		hql.append("inner join mscp.tipodocu td ");
		hql.append("WHERE mncp.LUsado='false' and td.idtipodocu='"+idtipodocuseleccionado+"' and car.numero='"+nombrecarteraContrato+"' ");

		
		if (idtipodocpadrenotadebito.equals("01")) {
			hql.append(" and mscp.serie like 'F%' ");
		} else {
			hql.append(" and mscp.serie like 'B%' ");
		}
		
		
		hql.append("order by mncp.idNumeracionComprobantepago asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		return (NumeracionComprobantePagoBean) query.setResultTransformer(Transformers.aliasToBean(NumeracionComprobantePagoBean.class)).uniqueResult();
		
	}

	@Override
	public void usarNumeracionComprobantepago(int idNumeracionComprobantepago) {
		StringBuilder hql = new StringBuilder();
		hql.append("update MaeNumeracionComprobantepago  set ");
		hql.append("LUsado = :LUsado ");
		hql.append("where idNumeracionComprobantepago = :idNumeracionComprobantepago");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("LUsado", true);
		query.setParameter("idNumeracionComprobantepago", idNumeracionComprobantepago);
		query.executeUpdate();
	}
	@Override
	public void usarNumeracionComprobantepagoRC(int idNumeracionComprobantepago) {
		StringBuilder hql = new StringBuilder();
		hql.append("update MaeNumeracionReciboCaja  set ");
		hql.append("l_usado = :l_usado ");
		hql.append("where id_numeracion_recibocaja = :id_numeracion_recibocaja");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("l_usado", true);
		query.setParameter("id_numeracion_recibocaja", idNumeracionComprobantepago);
		query.executeUpdate();
	}

	@Override
	public boolean siExisteNumeroOperacion(String numerooperacion) {
		if (sessionFactory.getCurrentSession().createQuery("select c.idcomprobante from Comprobantepago c where c.numerooperacion='"+numerooperacion+"'").list().size()==0) {
			return false;
		} else {
			return true;
		}
	}
     
	@Override
	public NumeracionComprobantePagoBean obtenerNumeracionComprobantePago(String idtipodocuseleccionado) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select mnrc.id_numeracion_recibocaja as idnumeracioncomprobantepago, msrc.serie as serie,mnrc.numeracion as numero from MaeNumeracionReciboCaja mnrc ");
		hql.append("inner join mnrc.maeserierecibocaja msrc ");
		hql.append("inner join msrc.tipodocu td ");
		hql.append("WHERE mnrc.l_usado='false' and td.idtipodocu='"+idtipodocuseleccionado+"' ");
		hql.append("order by mnrc.id_numeracion_recibocaja asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setMaxResults(1);
		return (NumeracionComprobantePagoBean) query.setResultTransformer(Transformers.aliasToBean(NumeracionComprobantePagoBean.class)).uniqueResult();
		
	}
}
