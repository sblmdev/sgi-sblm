package pe.gob.sblm.sgi.dao;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetalleCuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.PeriodoContratoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecartera;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Tipodocu;
import pe.gob.sblm.sgi.entity.Tipomodalidadpago;
import pe.gob.sblm.sgi.entity.Tipotransaccion;
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.bean.DetallecarteraBean;

public interface ICarteraDAO {

	public void registrarCartera(Cartera cartera);


	public void eliminarCartera(Cartera cartera);

	public Cartera obtenerUltimoCartera();

	public Cartera obtenerCarteraPorId(int id);

	List<Usuario> listarUsuarios();

	void registrarDetalleCartera(Detallecartera detallecartera);

	List<Detallecartera> listarDetalleCarteras();

	List<Uso> listarUsos();

	void eliminarCarteraDetalle(Detallecartera detallecartera);


	public List<DetallecarteraBean> listarDetalleCarterasPorIdCartera(int idcartera);


	List<Detallecartera> listarDetalleCarterasPorNroContrato(String nrocontrato);

	List<Detallecartera> listarContratosxcartera(String nrocartera);

	void registrarDetalleCuota(Detallecuota detallecuota);
	
	void registrarDetalleCuotaArbitrio(Detallecuota_arbitrio detallecuota);

	List<Cuota> listarcuotasxcontrato(int idcontrato);
	List<Cuota> listarcuotascontrato(int idcontrato);
	List<Detallecuota> listardetallecuotasxcontrato(int idcontrato);
	List<Detallecuota> listardetallecuotascontrato(int idcontrato);
	List<Cuota_arbitrio> listarcuotasxarbitrio(int idarbitrio);

	List<Tipopago> listartipopagos();

	List<Tipodocu> listartipodocus();
	
	List<Tipotransaccion> listartipotransaccion();

	List<Tipomovimiento> listartipomovimientos();
	
	List<Tipomodalidadpago> listartipomodalidadpagos();

	public Double obtenerUltimoIgv();

	Cartera obtenerCarteraPorIdContrato(int idcontrato);

	public Tipocambio buscarTipoCambio(String fecha);

	public String buscarobtenerNroContratoxIdTipoCambio(int idcontrato);

	public int obtenerNroPagosCuotaContrato(int idcontrato);

	public double obtenerSumaAcumuladaFraccionCuota(String nrocuota, int idcontrato);

	public Cuota obtenerIdCuota(String nrocuota, int idcontrato);

	public Contrato obtenerContratoSeleccionado(int idcontratoseleccionado);

	public int obtenerIddetalleCartera(int idcontratoseleccionado);
	
	public int obtenerIddetalleCarteraAutovaluo(int idseleccionado);

	public void grabarComprobantePago(Comprobantepago cp);

	public int obtenerNroCuotasCanceladas(int idcontratoseleccionado);

	public boolean siExisteCarteraConNombre(String numero);

	public Contrato obtenerContratoxId(Integer claveupaAgregarCartera);

	public boolean siExisteUpa(String claveupaAgregarCartera);

	public boolean siPerteneceCartera(int idcontrato);

	public List<Cartera> listaCarteras();

	public boolean siExisteRegistradoPago(int idcontratoseleccionado);
	public Date obtenerFechaUltimoPago(int idcontratoseleccionado);
	public List<Cuota> listarcuotasxSincontrato(int idcontratoseleccionado, int periodoSinContrato);
	public List<ComprobanteBean> buscarComprobante(Integer selectedCobrador,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador);
	public List<ComprobanteBean> buscarComprobante(Integer selectedCobrador,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public List<ComprobanteBean> buscarComprobanteEspecial(Integer selectedCobrador,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoCobrador);
	public List<ComprobanteBean> buscarDocumento(Integer selectedCobrador,String claveupa,Usuario usuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,Boolean selectedSiDocumentosAnulados,Boolean selectedSiIncluirAnulados,Boolean selectedSiIncluirRechazados);
	public List<ComprobanteBean> buscarComprobante(Comprobantepago cp);
	public List<Contrato> listarContratosVigentes();
	public List<CondicionBean> obtenerListaContratosDeUpa(String clave);
	public List<Detallecuota> obtenerDetalleComprobante(int idcomprobante);
	public List<Detallecuota_arbitrio> obtenerDetalleCuotaArbitrioComprobante(int idcomprobante);
	public String obtenerDescripcionComprobantePago(String idtipDocu);
	public String obtenerDescripcionTipoMovimiento(String idtipomovimiento);
	public String obtenerDescripcionTipoPago(String idtipopago);
	public Double obtenerMoraComprobante(Integer idcomprobantepadre);
	public List<Comprobantepago> listaComprobantes(int idcontrato);
	public Double sumarComprobanteCartera(String nrocartera,String fechaHoraToString, String fechaHoraToString2);
	public double sumarComprobanteNrodocumento(String nrocontrato,String fechaHoraToString, String fechaHoraToString2);
	public double sumarComprobanteCobrador(Integer idusuario,String fechaHoraToString, String fechaHoraToString2,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public double sumarComprobanteCobrador(Integer idusuario,String fechaHoraToString, String fechaHoraToString2,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public double sumarComprobanteDetraccion(String fechaHoraToString,String fechaHoraToString2);
	public double sumarComprobanteAutoDetraccion(String fechaHoraToString,String fechaHoraToString2);
	public String obtenerNombreCartera(int idcontrato);
	public void eliminarComprobante(int idcomprobante);
	public void anularComprobante(Comprobantepago cp);
	public void anularComprobanteSFE(Sunat_Comprobante scp);
	public boolean siregistradocomprobantepago(String nrocomprobante);
	public Date obtenerFechaIgv();
	public void actualizarCuota(Cuota cuo);
	public void actualizarCuotaNC(Cuota cuo);
	public void actualizarCuotaArbitrio(Cuota_arbitrio cuo);
	public void actualizarArbitrio(Arbitrio a);
	public Double obtenerTotalValorPagadoDolares(int idcuota);
	public List<Cuota> obtenerPagosContrato(int idcontrato);
	public void editarEstadoPago(Integer idcuota, String estado);
	public void eliminarCuota(int idcuota);
	public Integer obtenerNroSubpagosMesDetalleCuota(int idcuota);
	public Integer obtenerNroSubpagosMesDetalleCuotaArb(int idcuota);
	public Cuota obtenerCuota(int idcuota);
	public Cuota_arbitrio obtenerCuotaArbitrio(int idcuota);
	public Cuota obtenerCuota(int idcontrato, String mes, Integer anio);
	public void actualizarDetalleCuota(Detallecuota dcuo);
	public void actualizarEstadoDetalleCuota(Detallecuota dcuo);
	public void actualizarEstadoDetalleCuotaArbitrio(Detallecuota_arbitrio dcuo);
	public void actualizarEstadoComprobante(Comprobantepago cp);
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idnotadebito);
	public void apuntarComprobanteAnotaDebitoPenalizacion(int idcomprobantetemporal,int idnotadebito);
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idnotadebito,double mora);
	public void apuntarComprobanteAnotadebitoPenalizacion(int idcomprobantetemporal,int idnotadebito,double montopenalizacion);
	public void actualizarComprobante(ComprobanteBean comprobantepago);
	public List<Comprobantepago> getIdComprobantesPadreDeNotaDebito_Credito(Integer idcomprobante,String tipo);
	public boolean siExisteComprobanteRegistrado(String nroserie,String nrocomprobante);
	public Comprobantepago obtenerComprobanteModel(Integer idcomprobante);
	public Comprobantepago obtenerComprobantexInquilino(Integer idcomprobante,Integer idinquilino);
	public Sunat_Comprobante obtenerSunatComprobanteModel(Integer idcomprobante);
	public List<Integer> obtenerListaIdSunatComprobanteRechazadosxPeriodo();
	public List<Integer> obtenerListaIdComprobanteRechazadosxPeriodo();
	public List<DetallecomprobanteBean> obtenerDetalleComprobanteBean(int idcomprobante);
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxUpa(int idupa);
	public void detectarSiCompletoCuotasContrato(int idcontrato);
	public void actualizarSiCompletoCuotasContrato_SinContrato(int idcontrato, boolean value);
	public String obtenerCarteraCobrador(int idusuario);
	public boolean siAsignadoCobrador(int idusuario);
	public List<Comprobantepago> listaComprobantes(String tipodocumento);
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu);
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu , String tipo);
	public Double obtenerSumaComprobanteEspecializado(String desde,String hasta, String tipo, String uso);
	public void apuntarComprobanteAnotacredito(int idcomprobanteRef,int idnotacredito);
	public void apuntarComprobanteAnotacredito2(int idcomprobanteRef,int idnotacredito);
	public ItemBean obtenerNombreCobrador(int idusuario);
	public List<ItemBean> listarCobradores();
	public List<Usuario> listar_Usuarios();
	public int NroUpasVisitadasCobrador(int idusuario, String desde,String hasta);
	public Double sumarMoraComprobanteCobrador(int idusuario, String desde,String hasta);
	public int NroUpasMoraCobradaCobrador(int idusuario, String desde,String hasta);
	public List<CuotaBean> obtenerDetalleComprobantePadre(int nrocomprobantepadrenotacredito, String tipoDescuentoNotaCreditoSeleccionado);
	public List<CuotaBean> obtenerDetalleComprobantePadre(int nrocomprobantepadrenotacredito, String tipoDescuentoNotaCreditoSeleccionado,int idcontrato);
	public Comprobantepago obtenerDetalleComprobantePago(int idComprobantePago);
	public CuotaBean obtenerValorCuotaPagada(int idcontrato, int anio, String mes);
	public List<UpaPagadorDeudorBean> obtenerDeterminadosComprobantes(String desde, String hasta,String nombrecartera);

	public List<Integer> listarUpasConCondicion();
	public List<CondicionBean> obtenerCondicionesDeUpaConDeuda(int idupa);
	

	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxCondicion(int idcondicion, String Desde,String Hasta);



	public List<CuotaBean> listarRentaMensualPagadoCondicionxIntervalo(
			int idcondicion, Date desde, Date hasta, String moneda);


	public List<Comprobantepago> listarReciboCajaReferencia(int idcondicion, String idtipoconcepto);


	public void actualizarComprobantePago(Comprobantepago cp);


	public List<CuotaBean> generarPendientesParaCobroNuevoContrato(int idcontrato, Double valorTipoCambio);
	public List<CuotaBean> generarPendientesParaCobroReconocimientoDeuda(int idcontrato, Double valorTipoCambio);
	public List<CuotaBean> generarPendientesCuotasDetalleReconocimientoDeuda(Integer iddetallecondicion,Double valorTipoCmabio);
	public List<MaeDetCondicionDetalleBean> generarPendientesDetalleCuotaReconocimientoDeuda(Integer iddetallecondicion);
	public List<RentaBean> obtenerPeriodosPagados(int idcontrato);
	public List<RentaBean> obtenerPeriodosPendientes(int idcontrato);
	public List<RentaBean> obtenerPeriodosPendientesContrato(int idcontrato);
	public List<CuotaBean> obtenerPeriodosPendientesDeuda(int idcontrato);
	public List<CuotaBean> obtenerPeriodosPendientesContratoDeuda(int idcontrato);
	public List<CuotaArbitrioBean> obtenerPeriodosPendientesxMes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPendientesxMes(int idarbitrio);
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPagadosxMes(int arbitrio);
	public Sunat_Comprobante obtenerComprobante(Comprobantepago comp);
	public void grabarComprobanteDetalle(Sunat_Comprobante_Detalle compdetalle);
	public Cuota obtenerCuota(int idcontrato , String mes , String anio);
	public List<PeriodoContratoBean> obtenerPeriodoxContrato(int idcontrato);
	public List<String> obtenerCondicionDeContratoxUpa(String clave);
	public List<CondicionBean> obtenerListaContratosDeUpaxCondicion(String clave ,String condicion) ;
	public List<Sunat_Comprobante> obtenerListaSunatComprobantePendientes();
	public void updateComprobanteSgiSunatFe(Comprobantepago comprobante);
	
	public List<RentaBean> listarRentaPendientesxContrato(int idcontrato,String condicion, Boolean condMaeDetalle );
	public List<RentaBean> listarRentasPagadasxContrato(int idcontrato , String condicion,Boolean condMaeDetalle);
	public List<RentaBean> listarRentasPendientesxContrato(int idcontrato , String condicion, Boolean condMaeDetalle);
	public List<DetalleCuotaBean> listarDetalleCuotasxRenta(int idrenta);
	public boolean siRentasPagadasxContrato(int idcontrato);
	public RentaBean ultimaRentaPagada(int idcontrato );
	


}
