package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.dao.IUpaDAO;
import pe.gob.sblm.sgi.dao.TipoInteriorDAO;
import pe.gob.sblm.sgi.dao.TipoUsoDAO;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
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
import pe.gob.sblm.sgi.service.IUpaService;
@Transactional(readOnly = true)
@Service(value="upaService")
public class UpaService  implements IUpaService{
	
	
	@Autowired
	private IUpaDAO upaDAO;

	@Autowired
	private TipoInteriorDAO tipoInteriorDAO;
	
	@Autowired
	private TipoUsoDAO tipoUsoDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarUpa(Upa upa) {
		getUpaDAO().registrarUpa(upa); 
	}
	
	@Transactional(readOnly = false)
	public void grabarInmueble(Inmueble inmueble) {
		
		getUpaDAO().grabarInmueble(inmueble);
	}
	
	@Transactional(readOnly = false)
	
	public void actualizarUpa(Upa upa) {
		getUpaDAO().actualizarUpa(upa); 
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarUpa(Upa upa) {
		getUpaDAO().eliminarUpa(upa); 
		
	}
	
	public Upa validarRepetido(String numreg) {
		return getUpaDAO().validarRepetido(numreg);
	}
	
	public Upa validarClaveRepetido(String clave) {
		return getUpaDAO().validarClaveRepetido(clave);
	}
	
	
	public Upa obtenerUpaPorClave(String clave) {
		return getUpaDAO().obtenerUpaPorClave(clave);
	}
	
	public Inmueble obtenerInmueblePorParametro(String valor) {
		return getUpaDAO().obtenerInmueblePorParametro(valor);
	}
	
	
	public int obtenerNumeroRegistros() {
		return getUpaDAO().obtenerNumeroRegistros();
	}

	
	public List<Tipovia> listarTipoVia() {
		return getUpaDAO().listarTipoVia();
	}

	
	
	public List<Valuacion> listarValuacion() {
		return getUpaDAO().listarValuacion();
	}

	
	public List<Uso> listarTipoUso() throws Exception {
		return tipoUsoDAO.listarTipoUso();
	}
	
	public List<Mep> listarMep() {
		return getUpaDAO().listarMep();
	}
	
	
	public List<Titularidad> listarTitularidad() {
		return getUpaDAO().listarTitularidad();
	}

	
	public List<Tipotitularidad> listarTipoTitularidad() {
		return getUpaDAO().listarTipoTitularidad();
	}

	
	public List<Materialpredominante> listarMaterialPredominante() {
		return getUpaDAO().listarMaterialPredominante();
	}

	
	public List<Tipointerior> listarTipoInterior() throws Exception {
		return tipoInteriorDAO.listarTipoInterior();
	}

	
	public List<String> listarDepartamentos() {
		// TODO Auto-generated method stub
		return getUpaDAO().listarDepartamentos();
	}
	
	public List<String> listaInmuebles() {
		// TODO Auto-generated method stub
		return getUpaDAO().listaInmuebles();
	}
	
	
	public List<String> listarProvincias(String dpto) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarProvincias(dpto);
	}

	
	public List<String> listarDistritos(String prov) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarDistritos(prov);
	}

	
	public List<String> listarUbigeoPorDistrito(String prov, String dis) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarUbigeoPorDistrito(prov, dis);
	}

	
	public List<String> listarUbigeos() {
		// TODO Auto-generated method stub
		return getUpaDAO().listarUbigeos();
	}

	
	public List<String> listarDptoPorUbigeo(String Ubigeo) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarDptoPorUbigeo(Ubigeo);
	}

	
	public List<String> listarProvinciaPorUbigeo(String Ubigeo) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarProvinciaPorUbigeo(Ubigeo);
	}

	
	public List<String> listarDistritoPorUbigeo(String Ubigeo) {
		// TODO Auto-generated method stub
		return getUpaDAO().listarDistritoPorUbigeo(Ubigeo);
	}
	public IUpaDAO getUpaDAO() {
		return upaDAO;
	}
	public void setUpaDAO(IUpaDAO upaDAO) {
		this.upaDAO = upaDAO;
	}
	
	public List<Upa> listarUpas(int idinmueble) {
		return getUpaDAO().listarUpas(idinmueble);
	}
	
	public Upa obtenerUpaxClave(String clave) {
		return getUpaDAO().obtenerUpaxClave(clave);
	}
	
	public void actualizarProcesoJudicial(int idupa,Boolean siprocesojudicial) {
		getUpaDAO().actualizarProcesoJudicial(idupa,siprocesojudicial);
	}
	public void actualizarProcesoJudicial(int idupa,Boolean siprocesojudicial,Procesojudicialupa procesojudicial) {
		getUpaDAO().actualizarProcesoJudicial(idupa,siprocesojudicial,procesojudicial);
	}
	public void actualizarProcesoJudicialUpa(Procesojudicialupa procesojudicialupa) {
		getUpaDAO().actualizarProcesoJudicialUpa(procesojudicialupa);
	}
	public List<Upa> listarUpasxClave(String valorbusqueda) {
		return getUpaDAO().listarUpasxClave(valorbusqueda);
	}
	
	public List<Upa> listarUpasxDireccion(String valorbusqueda) {
		return getUpaDAO().listarUpasxDireccion(valorbusqueda);
	}
	public List<Upa> listarUpasxCondicion(String valor1,String valor2,String valor3,String valor4) {
		return getUpaDAO().listarUpasxCondicion(valor1,valor2,valor3,valor4);
	}
	public List<Upa> listarUpasxConsulta(Upa upa ,UpaBean upaBean,String ubigeo) {
		return getUpaDAO().listarUpasxConsulta(upa,upaBean, ubigeo);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void grabarEventoSeguimiento(Procesojudicialupa segupa) {
		getUpaDAO().grabarEventoSeguimiento(segupa);
	}
	
	public List<Procesojudicialupa> listarSegUpa(int idupa) {
		return getUpaDAO().listarSegUpa(idupa);
	}
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa) {
		return getUpaDAO().listarUpasBean(valorbusquedaUpa,tipoBusquedaUpa);
	}
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa,Upa upa) {
		return getUpaDAO().listarUpasBean(valorbusquedaUpa,tipoBusquedaUpa,upa);
	}
	public List<Upa> listarUpasActivas(){
		return getUpaDAO().listarUpasActivas();
	}
	public Upa obtenerUpaPorId(int idupa) {
		return getUpaDAO().obtenerUpaPorId(idupa);
	}
	public ProcesojudicialupaBean obtenerProcesoJudicialUpaxCondicion(int idprocesojudicialupa){
		return getUpaDAO().obtenerProcesoJudicialUpaxCondicion(idprocesojudicialupa);
	}

}
