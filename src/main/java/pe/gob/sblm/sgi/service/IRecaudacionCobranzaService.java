package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.NumeracionComprobantePagoBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Usuario;


public interface IRecaudacionCobranzaService {
	
	
	public List<ComprobanteBean> buscarComprobante(Integer integer,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador);
	public List<ComprobanteBean> buscarComprobante(Integer integer,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public List<ComprobanteBean> buscarComprobanteEspecial(Integer integer,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public List<ComprobanteBean> buscarDocumento(Integer integer,String claveupa,Usuario usuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,Boolean selectedSiDocumentosAnulados,Boolean selectedSiIncluirAnulados,Boolean selectedSiIncluirRechazados);
    public List<ComprobanteBean> buscarComprobante(Comprobantepago cp);
	public void actualizarSiCompletoCuotasContrato_SinContrato(int idcontrato, boolean value);
	public void detectarSiCompletoCuotasContrato(int idcontrato);
	
	public Double sumarComprobanteCartera(String nrocartera,String fechaHoraToString, String fechaHoraToString2);
	public double sumarComprobanteNrodocumento(String nrocontrato,String fechaHoraToString, String fechaHoraToString2);
	public double sumarComprobanteCobrador(Integer integer,String fechaHoraToString, String fechaHoraToString2 ,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public double sumarComprobanteCobrador(Integer integer,String fechaHoraToString, String fechaHoraToString2,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador);
	public double sumarComprobanteDetraccion(String fechaHoraToString, String fechaHoraToString2);
	public double sumarComprobanteAutoDetraccion(String fechaHoraToString, String fechaHoraToString2);
	
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idcomprobante);
	public void apuntarComprobanteAnotaDebitoPenalizacion(int idcomprobantetemporal,int idcomprobante);
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idcomprobante,double mora);
	public void apuntarComprobanteAnotadebitoPenalizacion(int idcomprobantetemporal,int idcomprobante,double montopenalizacion);
	public void actualizarComprobante(ComprobanteBean comprobantepago);
	public List<Comprobantepago> getIdComprobantesPadreDeNotaDebito_Credito(Integer idcomprobante,String tipo);
	public boolean siExisteComprobanteRegistrado(String nroserie,String nrocomprobante);
	public Comprobantepago obtenerComprobanteModel(Integer idcomprobante);
	public Comprobantepago obtenerComprobantexInquilino(Integer idcomprobante ,Integer inquilino);
	public Sunat_Comprobante obtenerSunatComprobanteModel(Integer idcomprobante);
	public List<Integer> obtenerListaIdSunatComprobanteRechazadosxPeriodo();
	public List<Integer> obtenerListaIdComprobanteRechazadosxPeriodo();
	public List<DetallecomprobanteBean> obtenerDetalleComprobanteBean(int idcomprobante);
	public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado);
	public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado ,int idcontrato);
	public Comprobantepago obtenerDetalleComprobantePago(int idcomprobantepago);
	public List<Cuota> obtenerPagosContrato(int idcontrato);
	public boolean siregistradocomprobantepago(String string);
	//public void editarEstadoPago(Integer idcuota, boolean estado);
	public void editarEstadoPago(Integer idcuota, String estado);
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu);
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu, String tipo);
	public List<Comprobantepago> listaComprobantes(String tipodocumento);
	public void grabarComprobantePago(Comprobantepago cp);
	public void actualizarComprobantePago(Comprobantepago cp);
	public List<Detallecuota> obtenerDetalleComprobante(int idcomprobante);
	public List<Detallecuota_arbitrio> obtenerDetalleCuotaArbitrioComprobante(int idcomprobante);
	public String obtenerDescripcionComprobantePago(String idtipDocu);
	public String obtenerDescripcionTipoMovimiento(String idtipomovimiento);
	public String obtenerDescripcionTipoPago(String idtipopago);
	public Double obtenerMoraComprobante(Integer idcomprobantepadre);
	
	/*****************obtener valor cuota pagada nota de crédito*****************/
	public CuotaBean obtenerValorCuotaPagada(int idcontrato, int anio, String mes);
	public void eliminarComprobante(int idcomprobante);
	public void anularComprobante(Comprobantepago cp);
	public void anularComprobanteSFE(Sunat_Comprobante scp);
	/***********************************************************************/
	public List<Comprobantepago> listaComprobantes(int idcontrato); 
	public Contrato obtenerContratoxId(Integer id);
	public List<Contrato> listarContratosVigentes();
	
	public Double obtenerSumaComprobanteEspecializado(String desde,String hasta, String tipo, String uso);
	public void apuntarComprobanteAnotacredito(int idcomprobanteRef,int idnotacredito);
	public void apuntarComprobanteAnotacredito2(int idcomprobanteRef,int idnotacredito);
	
	public Integer obtenerNroSubpagosMesDetalleCuota(int idcuota);
	public Integer obtenerNroSubpagosMesDetalleCuotaArb(int idcuota);
	public Cuota obtenerCuota(int idcuota);
	public Cuota_arbitrio obtenerCuotaArbitrio(int idcuota);
	public Cuota obtenerCuota(int idcontrato ,String mes, Integer anio);
	public void eliminarCuota(int idcuota);
	public void actualizarCuota(Cuota cuo); // ACTUALIZA CUOTA
	public void actualizarCuotaNC(Cuota cuo); // ACTUALIZA CUOTA NC
	public void actualizarCuotaArbitrio(Cuota_arbitrio cuo); // ACTUALIZA CUOTA ARBITRIO
	public void actualizarArbitrio(Arbitrio a); // ACTUALIZA CUOTA ARBITRIO
	public void actualizarDetalleCuota(Detallecuota dcuo);
	public void actualizarEstadoDetalleCuota(Detallecuota dcuo);
	public void actualizarEstadoDetalleCuotaArbitrio(Detallecuota_arbitrio dcuo);
	public void actualizarEstadoComprobante(Comprobantepago cp);
	public int NroUpasVisitadasCobrador(int idusuario, String desde,String hasta);
	public Double sumarMoraComprobanteCobrador(int idusuario,String fechaToString, String fechaToString2);
	public int NroUpasMoraCobradaCobrador(int idusuario,String fechaToString, String fechaToString2);
	
	public List<Cuota> listarcuotasxcontrato(int idcontrato);
	public Double obtenerTotalValorPagadoDolares(int idcuota);

	public List<Comprobantepago> listarReciboCajaReferencia(int idcondicion, String idtipoconcepto);

	public void grabarBoletaFacturaFE(Comprobantepago comprobantepago,String idtipodocuseleccionado, String nombrecarteraContrato) throws Exception;

	public void grabarNotaDebitoCreditoFE(Comprobantepago comprobantepago,String idtipodocuseleccionado, String nombrecarteraContrato, String idtipodocpadrenotadebito) throws Exception;

	public boolean siExisteNumeroOperacion(String numerooperacion);
	
	public Sunat_Comprobante obtenerComprobante(Comprobantepago comp);
	public void grabarSunatComprobanteDetalle(Sunat_Comprobante_Detalle cpdetalle) throws Exception;
	public void updateComprobanteSgiSunatFe(Comprobantepago cp);

	
	
	

}
