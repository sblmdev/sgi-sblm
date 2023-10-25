package pe.gob.sblm.sgi.service;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.ItemBean;
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
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Tipotransaccion;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.bean.DetallecarteraBean;

public interface IRecaudacionCarteraService {
	
	public void registrarCartera(Cartera cartera);
	public void eliminarCartera(Cartera cartera);
	
	public List<Tipopago> listartipopagos();
	public List<Tipotransaccion> listartipotransaccion();
	public List<Tipodocu> listartipodocus();
	public List<Tipomovimiento> listartipomovimientos();
	public List<Tipomodalidadpago> listartipomodalidadpagos();
	public Cartera obtenerUltimoCartera();
	public Cartera obtenerCarteraPorId(int id);
	public List<Usuario> listarUsuarios();
	public void registrarDetalleCartera(Detallecartera detallecartera);
	public List<Detallecartera> listarDetalleCarteras();
	public List<Uso> listarUsos();
	public void eliminarCarteraDetalle(Detallecartera detallecartera);
	public void registrarCuota(Cuota cuota);
	public void registrarCuotaArbitrio(Cuota_arbitrio cuota);
	public List<DetallecarteraBean> listarDetalleCarterasPorIdCartera(int idcartera);
	public List<Detallecartera> listarDetalleCarterasPorNroContrato(String nrocontrato);
	public List<Detallecartera> listarContratosxcartera(String nroCartera);
	public void registrarDetalleCuota(Detallecuota detallecuota);
	public void registrarDetalleCuotaArbitrio(Detallecuota_arbitrio detallecuota);	
	public Double obtenerUltimoIgv();
	public Cartera obtenerCarteraPorIdContrato(int idcontrato);
	public Tipocambio buscarTipoCambio(String fecha);
	public int obtenerNroPagosCuotaContrato(int idcontrato);
	public double obtenerSumaAcumuladaFraccionCuota(String nrocuota, int idcontrato);
	public Cuota obtenerIdCuota(String string, int idcontrato);
	public Contrato obtenerContratoSeleccionado(int idcontratoseleccionado);
	public int obtenerIddetalleCartera(int idcontratoseleccionado);
	public int obtenerIddetalleCarteraAutovaluo(int idcontratoseleccionado);
	public int obtenerNroCuotasCanceladas(int idcontratoseleccionado); 
	public boolean siExisteCarteraConNombre(String numero);
	public List<Contrato> obtenerContratosUpa(String claveupaAgregarCartera);
	public boolean siExisteUpa(String claveupaAgregarCartera);
	public boolean siPerteneceCartera(int idcontrato);
	public boolean siExisteRegistradoPago(int idcontratoseleccionado);
	public Date obtenerFechaUltimoPago(int idcontratoseleccionado);
	public List<Cuota> listarcuotasxSincontrato(int idcontratoseleccionado, int periodoSinContrato);
	public List<Cartera> listaCarteras();
	public List<CondicionBean> obtenerListaContratosDeUpa(String claveupa);
	public String obtenerNombreCartera(int idcontrato);
	public Date obtenerFechaIgv();
	public String obtenerCarteraCobrador(int idusuario);
	public boolean siAsignadoCobrador(int idusuario);
	public ItemBean obtenerNombreCobrador(int idusuario);
	public List<ItemBean> listarCobradores();
	public List<Usuario> listar_Usuarios();
	public List<String> obtenerCondicionDeContratoxUpa(String clave);
	public List<CondicionBean> obtenerListaContratosDeUpaxCondicion(String clave ,String condicion) ;
	public List<Tipodocupersona> listarTipoDocumentoPersona();
	public Tipodocupersona obtenerTipoDocPersona(String idtipo);
	public Integer longitudTipoDocumentoPersona(String idtipo);
	
	
	
}
