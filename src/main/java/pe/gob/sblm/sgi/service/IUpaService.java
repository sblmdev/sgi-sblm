package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Mep;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Valuacion;

public interface IUpaService {
	public void registrarUpa(Upa upa);
	public void actualizarUpa(Upa upa);

	public void eliminarUpa(Upa upa);
	
	public void grabarInmueble(Inmueble inmueble);
	public Upa obtenerUpaPorClave(String clave);
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
	public List<Tipointerior> listarTipoInterior() throws Exception;
	/**listado departamento**/
	List<String> listarDepartamentos();
	/**listado provincias**/
	List<String> listarProvincias(String dpto);
	/**listado distritos**/
	List<String> listarDistritos(String prov);
	/**listado ubigeo por distrito**/
	List<String> listarUbigeoPorDistrito(String prov,String dis);
	/**listado ubigeos**/
	List<String> listarUbigeos();
	/**listado Valores Por Ubigeo**/
	List<String> listarDptoPorUbigeo(String Ubigeo);
	List<String> listarProvinciaPorUbigeo(String Ubigeo);
	List<String> listarDistritoPorUbigeo(String Ubigeo);
	
	public List<Uso> listarTipoUso() throws Exception;
	List<Valuacion> listarValuacion();
	
	List<Mep> listarMep();
	List<String> listaInmuebles();
	Inmueble obtenerInmueblePorParametro(String valor);
	Upa validarRepetido(String numreg);
	Upa validarClaveRepetido(String clave);
	public List<Upa> listarUpas(int idinmueble);
	public Upa obtenerUpaxClave(String clave);
	public void actualizarProcesoJudicial(int idupa, Boolean siprocesojudicial);
	public void actualizarProcesoJudicial(int idupa, Boolean siprocesojudicial,Procesojudicialupa procesojudicial);
	public void actualizarProcesoJudicialUpa(Procesojudicialupa procesojudicialupa);
	public List<Upa> listarUpasxClave(String valorbusqueda);
	public List<Upa> listarUpasxDireccion(String valorbusqueda);
	public List<Upa> listarUpasxCondicion(String valor1, String valor2, String valor3, String valor4);
	public List<Upa> listarUpasxConsulta(Upa upa , UpaBean upaBean ,String ubigeo);
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa);
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa,Upa upa);
	public List<Upa> listarUpasActivas();
	public void grabarEventoSeguimiento(Procesojudicialupa segupa);
	public List<Procesojudicialupa> listarSegUpa(int idupa);
	public Upa obtenerUpaPorId(int idupa);
	public ProcesojudicialupaBean obtenerProcesoJudicialUpaxCondicion(int idprocesojudicialupa);
}
