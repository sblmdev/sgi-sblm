package pe.gob.sblm.sgi.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.MaeConfiguracionParametroDAO;
import pe.gob.sblm.sgi.entity.MaeConfiguracionParametro;
@Repository(value = "maeConfiguracionParametroDAO")
public class MaeConfiguracionParametroDAOImpl implements MaeConfiguracionParametroDAO{

	private static final Logger Logger = LoggerFactory.getLogger(MaeConfiguracionParametroDAOImpl.class);
	
	@Autowired	
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<MaeConfiguracionParametro> listaConfiguracionParametro(MaeConfiguracionParametro maeConfiguracionParametro)  throws Exception
{
		try{
			List<MaeConfiguracionParametro> lista;
			
			Logger.info(maeConfiguracionParametro.getN_tabla());
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT mcp  ");
			hql.append(" FROM MaeConfiguracionParametro mcp ");
			hql.append(" WHERE mcp.n_Tabla = :tabla ");
			hql.append(" AND mcp.Activo = :activo ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			query.setParameter( "tabla", maeConfiguracionParametro.getN_tabla() );
			query.setParameter( "activo", maeConfiguracionParametro.getActivo() );
			
			
			lista=(ArrayList<MaeConfiguracionParametro>)query.list();
			
			return lista;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		
	}

	@Override
	public MaeConfiguracionParametro buscarConfiguracionParametroxCondicion( MaeConfiguracionParametro maeConfiguracionParametro ,
			String condicion) throws Exception {
		try{
			MaeConfiguracionParametro maeconfiguracion;
			
			Logger.info(maeConfiguracionParametro.getN_tabla());
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT mcp  ");
			hql.append(" FROM MaeConfiguracionParametro mcp ");
			hql.append(" WHERE mcp.n_tabla = :tabla ");
			hql.append(" AND mcp.activo = :activo ");
			
			Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );
			query.setParameter( "tabla", maeConfiguracionParametro.getN_tabla() );
			query.setParameter( "activo", condicion );
			
			
			maeconfiguracion=(MaeConfiguracionParametro)query.uniqueResult();
			
			return maeconfiguracion;
			
		
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	

}
