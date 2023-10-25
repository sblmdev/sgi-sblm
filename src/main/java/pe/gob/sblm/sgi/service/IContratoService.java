package pe.gob.sblm.sgi.service;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.Adenda;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.entity.Condicionincumplimientoclausula;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detalleclausula;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Fechapago;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.entity.Tipoclausula;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;

public interface IContratoService {

	public List<Uso> getListaUsos();
	public void registrarContrato(Contrato contrato) throws Exception;
	public List<Inquilino> getListaInquilino();
	public List<Upa> buscarUpasXInmueble(int idinmueble);
	public List<CondicionBean> getListaContrato(String condicion);
	public List<Institucion> getListaInstitucion();
	public List<Representante> getListaRepresentante();
	public List<Cliente> getListaCliente();
	public List<Usuario> obtenerUsuarios();
	public void enviarNotificacionPersonalizable(String contenidoMensajePersonalizado,int idusuariodestino);
	public List<Cuentabancaria> getCtasBancarias();
	public Contrato obtenerContratoPorID(int id);
	public List<Contrato> ListarContratosPorID(int idcontrato);
	public void registrarCoutasContrato(Cuota couta);
	public List<Contrato> obtenerContratosUpa(int idupa);
	public void actualizarEstadoUltimoContratoDeUpa(int idupa);
	public List<Cuota> obtenerCuotasDeContrato(int idcontratoGlobal);
	public List<Detallecuota> obtenerDetalleDeCuota(int idcuota);
	public boolean verificarExistenciaCodigoContrato(String nrocontrato);
	public CondicionBean obtenerContratoxNrocontrato(String nrocontrato);
	public List<Contrato> obtenerContratosInquilino(int idinquilinoseleccionado);
	public Double sumacobrado(int idcontrato);
	public void actualizarMonto(int idcontrato, Double sumacobrado);
	public void finalizarContrato(int idcontrato);
	public List<CondicionBean> buscarConSinContratoxClave(String valorbusqueda,String tipo);
	public List<CondicionBean> buscarContrato(Contrato contrato);
	public List<CondicionBean> buscarConSinContratoxDireccion(String valorbusqueda,String tipo);
	public List<CondicionBean> buscarConSinContratoxNombreInquilino(String valorbusqueda,String tipo);
	public List<CondicionBean> buscarAdenda(String valorbusqueda,String tipo,int tipobusqueda);
	public void eliminarxCondicionContrato(int idcontrato);
	public void cambiarCondicionContrato(String condicion, int idcontrato, String numerocontrato);
	public List<Contrato> contratosTotalmenteCancelados();
	public void cancelarSinContrato(int idcontratoGlobal, String observacion,Date fecUltimoPagoSinContrato ,String usuario);
	public List<Contrato> sincontratoFinalizados();
	public void actualizarContrato(Contrato contrato);
	public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idcontratoGlobal);
	public List<PeriodoPagadoBean> obtenerPeriodosPagadosObservacion(int idcuota);
	public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota);
	public List<CondicionBean> obtenerCarteraActiva(String nombrecartera);
	public void incluirAdendaContrato(Adenda adenda,int idcontratoGlobal,String observacionadenda, String fincobro,String fincontratoadenda);
	public void incluirAdendaContrato(Adenda adenda,CondicionBean contratobean);
	public void resolverContrato(int idcontratoGlobal, String observacion, String fincobro, String fincontratoresuelto);
	public void finalizarContrato(int idcontratoGlobal, String observacion, String fincobro, String fincontratoresuelto,String usuario);
	public void anularContrato(CondicionBean condicion);
	public CondicionBean obtenerInformacionCondicion(int idcontrato);
	public CondicionBean obtenerInformacionCondicionxArbitrio(int idupa);
	public List<CondicionBean> obtenerCondicion(String condicion, String estado,String sidocumento);
	public List<CondicionBean> obtenerCondicionDeuda(String condicion, String estado,String sicancelado,String tipomoneda);
	public List<CondicionBean> obtenerCondicionDeudaxUpa(String condicion, String estado,String sicancelado,String tipomoneda,String claveupa);
	public List<CondicionBean> obtenerCondicionContratoDetraccion(String condicion, String estado,String sicancelado);
	public List<CondicionBean> obtenerListaContratoxCuota(String condicion, String estado,String sicancelado);
	public List<CondicionBean> obtenerCondicionxFecha(String condicion, String estado,String sicancelado,String desde,String hasta);
	public Boolean verificarExisteCondicionVigente(int idupa);
	public Boolean verificarExisteCondicionVigente(int idupa, String condicion);
	public List<CondicionBean> obtenerListaContratoBeanDeUpa(String claveupaTabCondicion);
	public List<CondicionBean> obtenerListaContratoBean(String claveupaTabCondicion);
	public List<CondicionBean> obtenerListaContratoUpa();
	public CondicionBean obtenerCondicionBean(int idcondicion);
	public CondicionBean obtenerUltimaCondicion(Integer idupa); 
	public List<CondicionBean> obtenerCondicionesVigentes(String carteraActivaReporteCartera);
	public CondicionBean verificarDisponibilidadContrato(int idupa);
	public void actualizarDevolucionInmuebleContrato(int idcontrato,String fechaactadevolucion, String numeroactadevolucion);
	public void actualizarPenalidadContrato(int idcontrato,String observacionpenalidad);
    public List<MaeDetalleCondicion> obtenerListaMaeCondicion(int idcontrato);
    public List<MaeDetCondicionDetalle> obtenerListaMaeCondicionDetalle(int idcontrato);
    public void asignarCondicion(Contrato contrato , Integer idcontrato, String usuario);
	public void registrarMaeDetalleCondicion(MaeDetalleCondicion maedetalle) throws Exception;
	public MaeDetalleCondicion getMaeDetalleCondicion(int idDetalleCondicion);
	public int numSecuenciaMaeDetalleCondicion(int idcontrato);
	public List<Fechapago> listarFechaPago();
	public Fechapago getFechaPago(int id);
	public Detalleclausula getDetalleClausula(String id);
	public Tipoclausula getTipoClausula(String id);
	public List<Tipoclausula> listarTipoClausula();
	public List<Detalleclausula> listarDetalleTipoClausula(String id);
	public List<Condicionincumplimientoclausula> listarCondicionIncumplimiento(Integer idcontrato);
	public void registrarClausulaContrato(Condicionincumplimientoclausula clausula)throws Exception;
	public List<CondicionBean> obtenerListaContratos(List<String> condicion,List<String> estado,String desdeinicio,String hastainicio,String desdefin, String hastafin);
	
	
	
	
//	public boolean verificarRegistroContrato()
}
