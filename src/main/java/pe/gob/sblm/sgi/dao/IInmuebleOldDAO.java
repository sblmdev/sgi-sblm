package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;

public interface IInmuebleOldDAO {
	public void registrarInmueble(Inmueble inmueble);
	public void actualizarInmueble(Inmueble inmueble);

	public void eliminarInmueble(Inmueble inmueble);
	public List<Inmueble> listarInmuebles();
	
	public Inmueble obtenerUltimoInmueble();

	public Inmueble obtenerInmueblePorId(int id);
	int obtenerNumeroRegistros();
	/**listar tipo via**/
	List<Tipovia> listarTipoVia();
	
	/**listar Titularidad**/
	List<Titularidad> listarTitularidad();
	/**listar TipoTitularidad**/
	List<Tipotitularidad> listarTipoTitularidad();
	/**listar Materialpredominante**/
	List<Materialpredominante> listarMaterialPredominante();
	/**listar Tipointerior**/
	List<Tipointerior> listarTipoInterior();
	/**listado departamentos**/
	List<String> listarDepartamentos();
	/**listado provincias**/
	List<String> listarProvincias(String dpto);
	/**listado Distritos**/
	List<String> listarDistritos(String prov);
	/**listado ubigeo por distrito
	 * @param dis **/
	List<String> listarUbigeoPorDistrito(String prov,String dist, String dis);
	/**listado ubigeos**/
	List<String> listarUbigeos();
	/**listado datos por ubigeos**/
	List<String> listarDistritoPorUbigeo(String ubigeo);
	List<String> listarProvinciaPorUbigeo(String ubigeo);
	List<String> listarDptoPorUbigeo(String ubigeo);
	Inmueble validarRepetido(String numreg);
	Inmueble validarClaveRepetido(String clave);
	
	
	
	
}
