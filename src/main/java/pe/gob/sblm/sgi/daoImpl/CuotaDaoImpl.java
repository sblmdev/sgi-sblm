package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.CuotaDao;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecuota;

@Repository(value = "cuotaDAO")
public class CuotaDaoImpl implements CuotaDao,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	
	public void registrarCuota(Cuota cuota) {
		getSessionFactory().getCurrentSession().save(cuota);

	}
	public void registrarCuotaArbitrio(Cuota_arbitrio cuota) {
		getSessionFactory().getCurrentSession().save(cuota);

	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	@Override
	public void actualizarEstadoCuota(int idcomprobante) {
		
		StringBuilder hqlA = new StringBuilder();
		hqlA.append("update Cuota set ");
		hqlA.append("cancelado = :cancelado ");
		hqlA.append("where idcuota IN (select cuo.idcuota from Detallecuota dc inner join dc.comprobantepago cp inner join dc.cuota cuo where cp.idcomprobante='"+idcomprobante+"')");
		
		Query queryA = sessionFactory.getCurrentSession().createQuery(hqlA.toString());
		queryA.setParameter("cancelado", "1");
		queryA.executeUpdate();
		
		StringBuilder hqlB = new StringBuilder();
		hqlB.append("update Detallecuota set ");
		hqlB.append("cancelado = :cancelado ");
		hqlB.append("where iddetallecuota IN (select dc.iddetallecuota from Detallecuota dc inner join dc.comprobantepago cp  where cp.idcomprobante='"+idcomprobante+"')");
		
		Query queryB = sessionFactory.getCurrentSession().createQuery(hqlB.toString());
		queryB.setParameter("cancelado", "1");
		queryB.executeUpdate();		
		
	}

	
	
}
