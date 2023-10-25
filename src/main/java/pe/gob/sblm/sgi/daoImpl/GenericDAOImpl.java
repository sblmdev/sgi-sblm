/**
 * 
 */
package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IGenericDAO;

/**
 * Resumen.
 * Objecto : GenericDaoImpl.java
 * Descripcion : Clase generica de acceso a datos
 * Fecha de creacion : 03/12/2015
 * @author Cindy Vallejos
 * ----------------------------------------------------------------
 * Modificaciones.
 * Fecha		Nombre		Descripcion
 * ----------------------------------------------------------------
 *
 */

@Repository("iGenericDao")
public class GenericDAOImpl implements IGenericDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Graba una entidad.
	 * @param object - Entidad a grabar, tipo Object.
	 * @return Entidad grabada, tipo Object.
	 */
	@Transactional
	public Object save(Object object){
		getSessionFactory().save(object);
		return object;
	}
	
	/**
	 * Graba o Actualiza(en caso exista) una entidad.
	 * @param object - Entidad a ser grabada o actualizada, tipo Object.
	 * @return Entidad grabada o actualizada, tipo Object.
	 */
	@Transactional
	public Object saveOrUpdate(Object object){
		getSessionFactory().saveOrUpdate(object);
		return object;
	}

	/**
	 * Actualiza una entidad.
	 * @param object - Entidad a ser actualizada, tipo Object.
	 * @return Entidad actualizada, tipo Object.
	 */
	@Transactional
	public Object update(Object object){
		getSessionFactory().update(object);
		return object;
	}
	
	/**
	 * Obtiene una entidad por su identificador.
	 * @param clazz - Clase a recuperar, tipo Class.
	 * @param id - identificador de la clase a recuperar, tipo  Serializable.
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	
	@Transactional(readOnly=true)
	public Object getById(Class clazz,Serializable id){
		return getSessionFactory().get(clazz, id);
	}
	
	/**
	 * Busca una o mas entidades por una propiedad.
	 * @param entityName - Nombre de la entidad, tipo String.
	 * @param propertyName - Nombre de la propiedad de la entidad, tipo String. 
	 * @param value - Valor de la propiedad propertyName, tipo Object. 
	 * @return Lista de registros encotrados, tipo List.
	 */
	@SuppressWarnings("rawtypes")
	
	@Transactional
	public List findByProperty(String entityName, String propertyName, Object value){
		StringBuilder hql = new StringBuilder();
		hql.append("from ");
		hql.append(entityName);
		hql.append(" as model ");
		hql.append("where model.");
		hql.append(propertyName);
		hql.append(" = ");
		hql.append(value);
		Query query = getSessionFactory().createQuery(hql.toString());
		return query.list();
	}
	
	/**
	 * Busca una entidad por el valor de una de sus propiedades.
	 * @param table - Nombre de la entidad, tipo String.
	 * @param propertyName - Nombre de la propiedad de la entidad, tipo String.
	 * @param value - Valor de la propiedad propertyName, tipo Object. 
	 * @return registro encontrador, tipo Object.
	 */
	
	@Transactional(readOnly = true)
	public Object getByProperty(String table, String propertyName, Object value){
		StringBuilder hql = new StringBuilder(); 
		hql.append("from ");
		hql.append(table);
		hql.append(" as model where model.");
		hql.append(propertyName);
		hql.append(" = ");
		if(value instanceof String){
			 hql.append("'");
			 hql.append(value); 
			 hql.append("'");
		}else{
			hql.append(value);
		}
		Query query = getSessionFactory().createQuery(hql.toString());
		query.setMaxResults(1);
		return query.uniqueResult();
	}
	
	/**
	 * Retorna una sesión del SessionFactory
	 * @return Session - Sesión activa, tipo Session.
	 */
	public Session getSessionFactory() {
		return sessionFactory.getCurrentSession();
	}


}
