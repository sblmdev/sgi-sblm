package pe.gob.sblm.sgi.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.MaeConfiguracionServicioDao;
import pe.gob.sblm.sgi.entity.MaeConfiguracionServicio;

@Repository(value = "maeConfiguracionServicioDao")
public class MaeConfiguracionServicioDaoImpl implements MaeConfiguracionServicioDao {
	
	private static final Logger Logger = LoggerFactory.getLogger(MaeConfiguracionServicioDaoImpl.class);
	
	@Autowired	
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<MaeConfiguracionServicio> listaConfiguracionesPorTabla(MaeConfiguracionServicio configuracionServicio) throws Exception {
		
		try{
			List<MaeConfiguracionServicio> lista;
			
			Logger.info(configuracionServicio.getCTabla());
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT m  ");
			hql.append(" FROM MaeConfiguracionServicio m ");
			hql.append(" WHERE m.cTabla = :tabla ");
			hql.append(" AND m.lActivo = :activo ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			query.setParameter( "tabla", configuracionServicio.getCTabla() );
			query.setParameter( "activo", configuracionServicio.getLActivo() );
			
			
			lista=(ArrayList<MaeConfiguracionServicio>)query.list();
			
			return lista;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		
		
	}

}
