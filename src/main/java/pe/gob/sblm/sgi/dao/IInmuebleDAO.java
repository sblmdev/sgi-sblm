package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Inmueblesaneamiento;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Origendominio;
import pe.gob.sblm.sgi.entity.Saneamiento;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Tipozona;
import pe.gob.sblm.sgi.entity.Titularidad;

public interface IInmuebleDAO {
	public void registrarInmueble(Inmueble inmueble);
	public void actualizarInmueble(Inmueble inmueble);

	public void eliminarInmueble(Inmueble inmueble);
	
	public List<Tipovia> listarTipoVia();
	public List<Origendominio> listarOrigenDominio();
	public List<Tipozona> listarTipoZona();
	
	public List<Titularidad> listarTitularidad();
	public List<Materialpredominante> listarMaterialPredominante();
	public List<String> listarDepartamentos();
	public List<String> listarProvincias(String dpto);
	public List<String> listarDistritos(String prov);
	
	public List<InmuebleBean> buscarInmuebles(String valorbusqueda,String tipobusqueda);
	public List<InmuebleBean> obtenerCondicion();
	public List<Inmueble> buscarInmueble(String clave);
	public String obtenerIdUbigeo(String depa, String prov, String dist);
	public void grabarLocalizacion(int idinmueble,String latitud, String longitud, String descripcion);
	public Titularidad obtenerTitularidad(String titularidad);
	public List<Saneamiento> listarCondicionesSaneamiento();
	public String obtenerSgtCodigoInmueble(String codigotitularidad);
	public List<Inmueblesaneamiento> obtenerCondicionesActivasInmueble(int idinmueble);
	public boolean siExisteClaveInmueble(String clave);
	public List<String> listarTipoTitularidad();
	public List<Tipotitularidad> listarTipotitularidad();
	public List<InmuebleBean> listarInmueblematriz(String clave,String depa,String prov,String dist,String tipotitularidad);
	public List<Inmueblemaestro> listarInmuebleMaestro();
	public List<InmuebleBean> listarInmuebleMaestro(InmuebleBean inm);

	
	
}
