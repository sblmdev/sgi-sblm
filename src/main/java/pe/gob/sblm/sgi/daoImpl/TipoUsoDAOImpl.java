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

import pe.gob.sblm.sgi.dao.TipoUsoDAO;
import pe.gob.sblm.sgi.entity.Uso;

@Repository(value = "tipousoDAO")
public class TipoUsoDAOImpl implements TipoUsoDAO,Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final Logger Logger = LoggerFactory.getLogger(TipoUsoDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Uso> listarTipoUso() throws Exception {
		
		try{
			List<Uso> lista;
			
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT u  ");
			hql.append(" FROM Uso u ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			
			
			lista=(ArrayList<Uso>)query.list();
			
			return lista;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	}
 


}
