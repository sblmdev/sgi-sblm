package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;

public interface IInmuebleOldService {

	public void registrarInmueble(Inmueble inmueble);
	public void actualizarInmueble(Inmueble inmueble);

	public void eliminarInmueble(Inmueble inmueble);
	public List<Inmueble> listarInmuebles();
	
	public Inmueble obtenerUltimoInmueble();

	public Inmueble obtenerInmueblePorId(int id);
	int obtenerNumeroRegistros();
	/**lista tipo via**/
	List<Tipovia> listarTipoVia();
	
	/**lista Titularidad**/
	List<Titularidad> listarTitularidad();
	/**lista tipo Titularidad**/
	List<Tipotitularidad> listarTipoTitularidad();
	/**lista tipo Materialpredominante**/
	List<Materialpredominante> listarMaterialPredominante();
	/**lista Tipointerior**/
	List<Tipointerior> listarTipoInterior();
	/**listado departamento**/
	List<String> listarDepartamentos();
	/**listado provincias**/
	List<String> listarProvincias(String dpto);
	/**listado distritos**/
	List<String> listarDistritos(String prov);
	/**listado ubigeo por distrito
	 * @param dist **/
	List<String> listarUbigeoPorDistrito(String depa,String prov, String dist);
	/**listado ubigeos**/
	List<String> listarUbigeos();
	/**listado Valores Por Ubigeo**/
	List<String> listarDptoPorUbigeo(String Ubigeo);
	List<String> listarProvinciaPorUbigeo(String Ubigeo);
	List<String> listarDistritoPorUbigeo(String Ubigeo);
	Inmueble validarRepetido(String numreg);
	Inmueble validarClaveRepetido(String clave);
	
	
	
	
	
	
}
