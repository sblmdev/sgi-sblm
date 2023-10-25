package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.TipoInteriorDAO;
import pe.gob.sblm.sgi.entity.Tipointerior;

@Repository(value = "tipointeriorDAO")
public class TipoInteriorDAOImpl implements TipoInteriorDAO,Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final Logger Logger = LoggerFactory.getLogger(TipoInteriorDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Tipointerior> listarTipoInterior() throws Exception {
		
		try{
			List<Tipointerior> lista;
			
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT ti  ");
			hql.append(" FROM Tipointerior ti ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			
			
			lista=(ArrayList<Tipointerior>)query.list();
			
			return lista;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	}
 


}
