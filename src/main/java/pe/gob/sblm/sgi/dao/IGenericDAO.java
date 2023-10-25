package pe.gob.sblm.sgi.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Resumen.
 * Objecto : IGenericDao
 * Descripcion : Interfaz de acceso a datos generica
 * Fecha de creacion : 03/05/2015 11:36:10
 * @author Cindy Vallejos
 * ----------------------------------------------------------------
 * Modificaciones.
 * Fecha		Nombre		Descripcion
 * ----------------------------------------------------------------
 *
 */
public interface IGenericDAO {

	/**
	 * Graba una entidad.
	 * @param object - Entidad a grabar, tipo Object.
	 * @return Entidad grabada, tipo Object.
	 */
	public abstract Object save(Object object);
	
	/**
	 * Graba o Actualiza(en caso exista) una entidad.
	 * @param object - Entidad a ser grabada o actualizada, tipo Object.
	 * @return Entidad grabada o actualizada, tipo Object.
	 */
	public abstract Object saveOrUpdate(Object object);
	
	/**
	 * Actualiza una entidad.
	 * @param object - Entidad a ser actualizada, tipo Object.
	 * @return Entidad actualizada, tipo Object.
	 */
	public abstract Object update(Object object);
	
	/**
	 * Obtiene una entidad por su identificador.
	 * @param clazz - Clase a recuperar, tipo Class.
	 * @param id - identificador de la clase a recuperar, tipo  Serializable.
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public abstract Object getById(Class clazz,Serializable id);
	
	/**
	 * Busca una o mas entidades por una propiedad.
	 * @param entityName - Nombre de la entidad, tipo String.
	 * @param propertyName - Nombre de la propiedad de la entidad, tipo String. 
	 * @param value - Valor de la propiedad propertyName, tipo Object. 
	 * @return Lista de registros encotrados, tipo List.
	 */
	@SuppressWarnings("rawtypes")
	public abstract List findByProperty(String entityName, String propertyName, Object value);
	
	/**
	 * Busca una entidad por el valor de una de sus propiedades.
	 * @param table - Nombre de la entidad, tipo String.
	 * @param propertyName - Nombre de la propiedad de la entidad, tipo String.
	 * @param value - Valor de la propiedad propertyName, tipo Object. 
	 * @return registro encontrador, tipo Object.
	 */
	public abstract Object getByProperty(String table, String propertyName, Object value);
}
