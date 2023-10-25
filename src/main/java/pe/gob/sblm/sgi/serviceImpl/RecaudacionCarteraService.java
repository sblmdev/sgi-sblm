package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.dao.CuotaDao;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.dao.IUpaDAO;
import pe.gob.sblm.sgi.dao.TipoDocuPersonaDAO;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecartera;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Tipodocu;
import pe.gob.sblm.sgi.entity.Tipodocupersona;
import pe.gob.sblm.sgi.entity.Tipomodalidadpago;
import pe.gob.sblm.sgi.entity.Tipotransaccion;
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.bean.DetallecarteraBean;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;

@Transactional(readOnly = true)
@Service(value="carteraService")
public class RecaudacionCarteraService implements IRecaudacionCarteraService{
	@Autowired
	private ICarteraDAO carteraDAO;
	
	@Autowired
	private CuotaDao cuotaDAO;
	
	@Autowired
	private IUpaDAO upaDAO;
	
	@Autowired
	private TipoDocuPersonaDAO tipoDocuPersonaDAO;
	
	@Autowired
	private IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	
	@Transactional(readOnly = false)
	public void registrarCartera(Cartera cartera) {
		getCarteraDAO().registrarCartera(cartera);
		
	}
	@Transactional(readOnly = false)
	public void registrarCuota(Cuota cuota) {
		cuotaDAO.registrarCuota(cuota);
		
	}
	@Transactional(readOnly = false)
	public void registrarCuotaArbitrio(Cuota_arbitrio cuota) {
		cuotaDAO.registrarCuotaArbitrio(cuota);
		
	}
		
	@Transactional(readOnly = false)
	
	public void registrarDetalleCuota(Detallecuota detallecuota) {
		getCarteraDAO().registrarDetalleCuota(detallecuota);
		
	}
	@Transactional(readOnly = false)
	
	public void registrarDetalleCuotaArbitrio(Detallecuota_arbitrio detallecuota) {
		getCarteraDAO().registrarDetalleCuotaArbitrio(detallecuota);
		
	}
	
	@Transactional(readOnly = false)
	
	public void registrarDetalleCartera(Detallecartera detallecartera) {
		getCarteraDAO().registrarDetalleCartera(detallecartera);
		
	}

	@Transactional(readOnly = false)
	
	public void eliminarCartera(Cartera cartera) {
		getCarteraDAO().eliminarCartera(cartera);
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarCarteraDetalle(Detallecartera detallecartera) {
		getCarteraDAO().eliminarCarteraDetalle(detallecartera);
		
	}
	
	public List<Tipopago> listartipopagos() {
		return getCarteraDAO().listartipopagos();
	}
	public List<Tipotransaccion> listartipotransaccion() {
		return getCarteraDAO().listartipotransaccion();
	}
	public List<Tipodocu> listartipodocus() {
		return getCarteraDAO().listartipodocus();
	}
	
	public List<Tipomovimiento> listartipomovimientos() {
		return getCarteraDAO().listartipomovimientos();
	}
	
	public List<Tipomodalidadpago> listartipomodalidadpagos() {
		return getCarteraDAO().listartipomodalidadpagos();
	}
	
	public List<Usuario> listarUsuarios() {
		return getCarteraDAO().listarUsuarios();
	}
	
	
	public List<Detallecartera> listarDetalleCarteras() {
		return getCarteraDAO().listarDetalleCarteras();
	}
	
	public List<DetallecarteraBean> listarDetalleCarterasPorIdCartera(int idcartera) {
		return getCarteraDAO().listarDetalleCarterasPorIdCartera(idcartera);
	}
	
	
	public List<Detallecartera> listarContratosxcartera(String nrocartera) {
		return getCarteraDAO().listarContratosxcartera(nrocartera); 
	}
	
	public List<Uso> listarUsos() {
		return getCarteraDAO().listarUsos();
	}
	
	public List<Detallecartera> listarDetalleCarterasPorNroContrato(String nrocontrato) {
		return getCarteraDAO().listarDetalleCarterasPorNroContrato(nrocontrato);
	}
	
	public Cartera obtenerUltimoCartera() {
		return getCarteraDAO().obtenerUltimoCartera();
	}
	
	public Double obtenerUltimoIgv() {
		return getCarteraDAO().obtenerUltimoIgv();
	}

	public List<Cartera> listaCarteras() {
		return carteraDAO.listaCarteras();
	}
	
	public Cartera obtenerCarteraPorId(int id) {
		return getCarteraDAO().obtenerCarteraPorId(id);
	}
	
	public Cartera obtenerCarteraPorIdContrato(int idcontrato) {
		return getCarteraDAO().obtenerCarteraPorIdContrato(idcontrato);
	}
	public ICarteraDAO getCarteraDAO() {
		return carteraDAO;
	}

	public void setCarteraDAO(ICarteraDAO carteraDAO) {
		this.carteraDAO = carteraDAO;
	}
	
	public Tipocambio buscarTipoCambio(String fecha) {
		return carteraDAO.buscarTipoCambio(fecha);
	}
	
	public int obtenerNroPagosCuotaContrato(int idcontrato) {
		return carteraDAO.obtenerNroPagosCuotaContrato(idcontrato);
	}
	
	public double obtenerSumaAcumuladaFraccionCuota(String nrocuota, int idcontrato) {
		return carteraDAO.obtenerSumaAcumuladaFraccionCuota(nrocuota, idcontrato);
	}
	
	public Cuota obtenerIdCuota(String nrocuota, int idcontrato) {
		return carteraDAO.obtenerIdCuota(nrocuota, idcontrato);
	}

	public Contrato obtenerContratoSeleccionado(int idcontratoseleccionado) {
		return carteraDAO.obtenerContratoSeleccionado(idcontratoseleccionado);
	}
	
	public int obtenerIddetalleCartera(int idcontratoseleccionado) {
		return carteraDAO.obtenerIddetalleCartera(idcontratoseleccionado);
	}
	
	public int obtenerIddetalleCarteraAutovaluo(int idseleccionado) {
		return carteraDAO.obtenerIddetalleCarteraAutovaluo(idseleccionado);
	}
	
	public int obtenerNroCuotasCanceladas(int idcontratoseleccionado) {
		return carteraDAO.obtenerNroCuotasCanceladas(idcontratoseleccionado);
	}
	
	public boolean siExisteCarteraConNombre(String numero) {
		return carteraDAO.siExisteCarteraConNombre(numero);
	}
	
	public List<Contrato> obtenerContratosUpa(String claveupaAgregarCartera) {
		return null;
	}
	
	public boolean siExisteUpa(String claveupaAgregarCartera) {
		return carteraDAO.siExisteUpa(claveupaAgregarCartera);
	}
	
	public boolean siPerteneceCartera(int idcontrato) {
		return carteraDAO.siPerteneceCartera(idcontrato);
	}

	public boolean siExisteRegistradoPago(int idcontratoseleccionado) {
		return carteraDAO.siExisteRegistradoPago(idcontratoseleccionado);
	}
	
	public Date obtenerFechaUltimoPago(int idcontratoseleccionado) {
		return carteraDAO.obtenerFechaUltimoPago(idcontratoseleccionado);
	}
	
	public List<Cuota> listarcuotasxSincontrato(int idcontratoseleccionado, int periodoSinContrato) {
		return carteraDAO.listarcuotasxSincontrato(idcontratoseleccionado,periodoSinContrato);
	}
	
	public List<CondicionBean> obtenerListaContratosDeUpa(String clave) {
		return carteraDAO.obtenerListaContratosDeUpa(clave);
	}

	public String obtenerNombreCartera(int idcontrato) {
		
		return carteraDAO.obtenerNombreCartera(idcontrato);
	}
	
	public Date obtenerFechaIgv() {
		return carteraDAO.obtenerFechaIgv();
	}

	public String obtenerCarteraCobrador(int idusuario) {
		return carteraDAO.obtenerCarteraCobrador(idusuario);
	}
	
	public boolean siAsignadoCobrador(int idusuario) {
		return carteraDAO.siAsignadoCobrador(idusuario);
	}
	
	public ItemBean obtenerNombreCobrador(int idusuario) {
		return carteraDAO.obtenerNombreCobrador(idusuario);
	}
	
	public List<ItemBean> listarCobradores() {
		return carteraDAO.listarCobradores();
	}
	public List<Usuario> listar_Usuarios() {
		return carteraDAO.listar_Usuarios();
	}
	public List<String> obtenerCondicionDeContratoxUpa(String clave){
		return carteraDAO.obtenerCondicionDeContratoxUpa(clave);
	}
	public List<CondicionBean> obtenerListaContratosDeUpaxCondicion(String clave, String condicion){
		return carteraDAO.obtenerListaContratosDeUpaxCondicion(clave, condicion);
	}
	@Override
	public List<Tipodocupersona> listarTipoDocumentoPersona() {
		return tipoDocuPersonaDAO.listarTipoDocumentoPersona();
	}
	@Override
	public Tipodocupersona obtenerTipoDocPersona(String idtipo) {
		return tipoDocuPersonaDAO.obtenerTipoDocPersona(idtipo);
	}
	@Override
	public Integer longitudTipoDocumentoPersona(String idtipo) {
		return  tipoDocuPersonaDAO.longitudTipoDocumentoPersona(idtipo);
	}
	
	
}
