package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.dao.IGenericDAO;
import pe.gob.sblm.sgi.dao.IInmuebleDAO;
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
import pe.gob.sblm.sgi.service.IInmuebleService;
@Transactional(readOnly = true)
@Service(value="inmuebleService")
public class InmuebleService implements IInmuebleService{
	
	@Autowired
	private IInmuebleDAO inmuebleDAO;
	
	@Autowired
	private IGenericDAO genericDAO;
	
	public IInmuebleDAO getInmuebleDAO() {
		return inmuebleDAO;
	}
	public void setInmuebleDAO(IInmuebleDAO inmuebleDAO) {
		this.inmuebleDAO = inmuebleDAO;
	}


	@Transactional(readOnly = false)
	public void grabarInmueble(Inmueblemaestro inmueble) {
		
		getGenericDAO().saveOrUpdate(inmueble);
	}

	@Transactional(readOnly = false)
	public void actualizarInmueble(Inmueble inmueble) {
		getInmuebleDAO().actualizarInmueble(inmueble);
		
	}
	@Transactional(readOnly = false)
	public void eliminarInmueble(Inmueble inmueble) {
		getInmuebleDAO().eliminarInmueble(inmueble);
		
	}
	
	public List<Tipovia> listarTipoVia() {
		return getInmuebleDAO().listarTipoVia();
	}
	
	public List<Origendominio> listarOrigenDominio() {
		return getInmuebleDAO().listarOrigenDominio();
	}
	
	@Override
	public List<Tipozona> listarTipoZona() {
		return getInmuebleDAO().listarTipoZona();
	}
	
	public List<Titularidad> listarTitularidad() {
		return getInmuebleDAO().listarTitularidad();
	}
	
	
	public List<Materialpredominante> listarMaterialPredominante() {
		return getInmuebleDAO().listarMaterialPredominante();
	}
	
	
	
	public List<String> listarDepartamentos() {
		return getInmuebleDAO().listarDepartamentos();
	}
	
	public List<String> listarProvincias(String dpto) {
		return getInmuebleDAO().listarProvincias(dpto);
	}
	
	public List<String> listarDistritos(String prov) {
		return getInmuebleDAO().listarDistritos(prov);
	}
	
	public List<InmuebleBean> buscarInmuebles(String valorbusqueda,String tipoBusqueda) {
		return getInmuebleDAO().buscarInmuebles(valorbusqueda,tipoBusqueda);
	}
	public List<InmuebleBean> obtenerCondicion() {
		return getInmuebleDAO().obtenerCondicion();
	}
	public List<Inmueble> buscarInmueble(String clave) {
		return getInmuebleDAO().buscarInmueble(clave);
	}
	public String obtenerIdUbigeo(String depa, String prov, String dist) {
		return getInmuebleDAO().obtenerIdUbigeo(depa,prov,dist);
	}
	@Override
	public void grabarLocalizacion(int idinmueble,String latitud, String longitud, String descripcion) {
		 getInmuebleDAO().grabarLocalizacion(idinmueble,latitud,longitud,descripcion);
	}

	@Override
	public Titularidad obtenerTitularidad(String titularidad) {
		return getInmuebleDAO().obtenerTitularidad(titularidad);
		
	}
	@Override
	public String obtenerSgtCodigoInmueble(String codigotitularidad) {
		return getInmuebleDAO().obtenerSgtCodigoInmueble(codigotitularidad);
	}
	@Override
	public List<Saneamiento> listarCondicionesSaneamiento() {
		return getInmuebleDAO().listarCondicionesSaneamiento();
	}

	@Override
	public List<Inmueblesaneamiento> obtenerCondicionesActivasInmueble(int idinmueble) {
		return getInmuebleDAO().obtenerCondicionesActivasInmueble(idinmueble);
	}
	public IGenericDAO getGenericDAO() {
		return genericDAO;
	}
	public void setGenericDAO(IGenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	@Override
	public boolean siExisteClaveInmueble(String clave) {
		return getInmuebleDAO().siExisteClaveInmueble(clave);
	}
	public List<String> listarTipoTitularidad() {
		return getInmuebleDAO().listarTipoTitularidad();
	}
	public List<Tipotitularidad> listarTipotitularidad() {
		return getInmuebleDAO().listarTipotitularidad();
	}
	public List<InmuebleBean> listarInmueblematriz(String clave,String depa,String prov,String dist,String tipotitularidad) {
		return getInmuebleDAO().listarInmueblematriz(clave, depa,prov, dist,tipotitularidad);
	}
	public List<Inmueblemaestro> listarInmuebleMaestro(){
		return getInmuebleDAO().listarInmuebleMaestro();
	}
	public List<InmuebleBean> listarInmuebleMaestro(InmuebleBean inm){
		return getInmuebleDAO().listarInmuebleMaestro(inm);
	}

}
