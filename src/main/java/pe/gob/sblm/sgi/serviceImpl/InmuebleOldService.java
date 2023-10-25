package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IInmuebleOldDAO;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Tipointerior;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.service.IInmuebleOldService;
@Transactional(readOnly = true)
@Service(value="inmuebleOldService")
public class InmuebleOldService implements IInmuebleOldService{
	@Autowired
	private IInmuebleOldDAO inmuebleDAO;

	@Transactional(readOnly = false)
	@Override
	public void registrarInmueble(Inmueble inmueble) {
		inmuebleDAO.registrarInmueble(inmueble);
		
	}
	@Transactional(readOnly = false)
	@Override
	public void actualizarInmueble(Inmueble inmueble) {
		inmuebleDAO.actualizarInmueble(inmueble);
		
	}
	@Transactional(readOnly = false)
	@Override
	public void eliminarInmueble(Inmueble inmueble) {
		inmuebleDAO.eliminarInmueble(inmueble);
		
	}

	@Override
	public List<Inmueble> listarInmuebles() {
		return inmuebleDAO.listarInmuebles();
	}

	@Override
	public Inmueble obtenerUltimoInmueble() {
		return inmuebleDAO.obtenerUltimoInmueble();
	}

	@Override
	public Inmueble obtenerInmueblePorId(int id) {
		return inmuebleDAO.obtenerInmueblePorId(id);
	}

	@Override
	public int obtenerNumeroRegistros() {
		return inmuebleDAO.obtenerNumeroRegistros();
	}
	@Override
	public Inmueble validarRepetido(String numreg) {
		return inmuebleDAO.validarRepetido(numreg);
	}
	@Override
	public Inmueble validarClaveRepetido(String clave) {
		return inmuebleDAO.validarClaveRepetido(clave);
	}
	

	
	/**tipo via**/
	@Override
	public List<Tipovia> listarTipoVia() {
		return inmuebleDAO.listarTipoVia();
	}
	
	/** listarTitularidad**/
	@Override
	public List<Titularidad> listarTitularidad() {
		return inmuebleDAO.listarTitularidad();
	}
	/** listarTipo Titularidad**/
	@Override
	public List<Tipotitularidad> listarTipoTitularidad() {
		return inmuebleDAO.listarTipoTitularidad();
	}
	/** listar Materialpredominante**/
	@Override
	public List<Materialpredominante> listarMaterialPredominante() {
		return inmuebleDAO.listarMaterialPredominante();
	}
	/** listar TipoInterior**/
	@Override
	public List<Tipointerior> listarTipoInterior() {
		return inmuebleDAO.listarTipoInterior();
	}
	
	/**listado departamentos*/
	@Override
	public List<String> listarDepartamentos() {
		return inmuebleDAO.listarDepartamentos();
	}
	/**listado provincias*/
	@Override
	public List<String> listarProvincias(String dpto) {
		return inmuebleDAO.listarProvincias(dpto);
	}
	/**listado distritos*/
	@Override
	public List<String> listarDistritos(String prov) {
		return inmuebleDAO.listarDistritos(prov);
	}
	/**listado ubigeo por distrito**/
	@Override
	public List<String> listarUbigeoPorDistrito(String depa,String prov, String dis) {
		return inmuebleDAO.listarUbigeoPorDistrito(depa, prov, dis);
	}
	/**listado ubigeos*/
	@Override
	public List<String> listarUbigeos() {
		return inmuebleDAO.listarUbigeos();
	}
	/**listado DptoPorUbigeo*/
	@Override
	public List<String> listarDptoPorUbigeo(String Ubigeo) {
		return inmuebleDAO.listarDptoPorUbigeo(Ubigeo);
	}
	/**listado ProvinciaPorUbigeo*/
	@Override
	public List<String> listarProvinciaPorUbigeo(String Ubigeo) {
		return inmuebleDAO.listarProvinciaPorUbigeo(Ubigeo);
	}
	/**listado DistritoPorUbigeo*/
	@Override
	public List<String> listarDistritoPorUbigeo(String Ubigeo) {
		return inmuebleDAO.listarDistritoPorUbigeo(Ubigeo);
	}
}
