package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.dao.MaeDetallecondicionDao;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;

@Repository(value = "detallecondicionDAO")
public class MaeDetallecondicionDaoImpl implements MaeDetallecondicionDao,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void registrarDetalleCondicion(MaeDetalleCondicion maeDetalleCondicion) {
		
		sessionFactory.getCurrentSession().save(maeDetalleCondicion);
		
	}

	@SuppressWarnings("unchecked")
	public List<RentaBean> obtenerDetalleCondicion(int idcontrato) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
		hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append(" dc.sipagofraccionrenta as sipagofraccionrenta , dc.sirentagenerada as sirentagenerada ,");
		hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, dc.nrocuota as nrocuota , dc.fechavencimiento as fechavencimiento ,  ");
		hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta ");
		hql.append(" from MaeDetalleCondicion ");
		hql.append(" dc inner join dc.contrato c");
		hql.append(" where c.idcontrato= "+idcontrato+" and dc.estado='GENERADO' ");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return (List<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
		
	}
	@SuppressWarnings("unchecked")
	public List<RentaBean> obtenerDetalleMaeCondicion(int idcontrato) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
		hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, dc.nrocuota as nrocuota , dc.fechavencimiento as fechavencimiento ,  ");
		hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta ");
		hql.append(" from MaeDetalleCondicion ");
		hql.append(" dc inner join dc.contrato c");
		hql.append(" where c.idcontrato= "+idcontrato+" and dc.estado='GENERADO' ");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return (List<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
		
	}

}
