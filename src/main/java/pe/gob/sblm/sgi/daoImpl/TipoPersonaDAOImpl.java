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
import pe.gob.sblm.sgi.dao.TipoPersonaDAO;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipopersona;

@Repository(value = "tipopersonaDAO")
public class TipoPersonaDAOImpl implements TipoPersonaDAO,Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final Logger Logger = LoggerFactory.getLogger(TipoPersonaDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Tipopersona> listarTipoPersona() throws Exception {
		
		try{
			List<Tipopersona> lista;
			
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT tp  ");
			hql.append(" FROM Tipopersona tp where tp.estado='1' ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			
			
			lista=(ArrayList<Tipopersona>)query.list();
			
			return lista;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	}
 


}
