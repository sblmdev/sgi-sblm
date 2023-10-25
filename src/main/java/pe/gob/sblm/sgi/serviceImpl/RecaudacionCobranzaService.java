package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.NumeracionComprobantePagoBean;
import pe.gob.sblm.sgi.dao.CuotaDao;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.dao.MaeNumeracionComprobantePagoDAO;
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
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.util.Constante;

@Transactional(readOnly = true)
@Service(value="cobranzaRecaudacionService")
public class RecaudacionCobranzaService implements IRecaudacionCobranzaService{
	
	private static final Logger Logger = LoggerFactory.getLogger(RecaudacionCobranzaService.class);
	
	@Autowired
	private ICarteraDAO carteraDAO;
	
	@Autowired
	private CuotaDao cuotaDAO;
	
	@Autowired
	private MaeNumeracionComprobantePagoDAO maeNumeracionComprobantePagoDAO;
	
	public void detectarSiCompletoCuotasContrato(int idcontrato) {
		carteraDAO.detectarSiCompletoCuotasContrato(idcontrato);
	}
	
	public void actualizarSiCompletoCuotasContrato_SinContrato(int idcontrato,boolean value) {
		carteraDAO.actualizarSiCompletoCuotasContrato_SinContrato(idcontrato,value);
	}
	
	public List<ComprobanteBean> buscarComprobante(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador) {
		return carteraDAO.buscarComprobante(idusuario,claveupa,nombreusuario,nombreinquilino,nrocontrato,desde,hasta,tipoconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador);
	}
	public List<ComprobanteBean> buscarComprobante(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
		return carteraDAO.buscarComprobante(idusuario,claveupa,nombreusuario,nombreinquilino,nrocontrato,desde,hasta,tipoconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
	}
	public List<ComprobanteBean> buscarComprobanteEspecial(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador ,boolean selectedSiRechazadoTabCobrador) {
		return carteraDAO.buscarComprobanteEspecial(idusuario,claveupa,nombreusuario,nombreinquilino,nrocontrato,desde,hasta,tipoconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);
	}
	public List<ComprobanteBean> buscarDocumento(Integer idusuario,String claveupa,Usuario usuario,String nombreinquilino,String nrocontrato, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,Boolean selectedSiDocumentosAnulados,Boolean selectedSiIncluirAnulados,Boolean selectedSiIncluirRechazados) {
		return carteraDAO.buscarDocumento(idusuario,claveupa,usuario,nombreinquilino,nrocontrato,desde,hasta,tipoconsulta,selectedComprobanteTabCobrador,selectedSiDocumentosAnulados,selectedSiIncluirAnulados,selectedSiIncluirRechazados);
	}
	public List<ComprobanteBean> buscarComprobante(Comprobantepago cp) {
		return carteraDAO.buscarComprobante(cp);
	}
	public Double sumarComprobanteCartera(String nrocartera,
			String fechaHoraToString, String fechaHoraToString2) {
		return carteraDAO.sumarComprobanteCartera(nrocartera,fechaHoraToString,fechaHoraToString2);	}
	
	
	public double sumarComprobanteNrodocumento(String nrocontrato,String fechaHoraToString, String fechaHoraToString2) {
		return carteraDAO.sumarComprobanteNrodocumento(nrocontrato,fechaHoraToString,fechaHoraToString2);	
	}
	
	public double sumarComprobanteCobrador(Integer idusuario,String fechaHoraToString, String fechaHoraToString2,boolean selectedSiAnuladoTabCobrador, boolean selectedSiRechazadoTabCobrador) {
		return carteraDAO.sumarComprobanteCobrador(idusuario,fechaHoraToString,fechaHoraToString2,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);	
	}
	public double sumarComprobanteCobrador(Integer idusuario,String fechaHoraToString, String fechaHoraToString2,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
		return carteraDAO.sumarComprobanteCobrador(idusuario,fechaHoraToString,fechaHoraToString2,tipoconsulta,selectedComprobanteTabCobrador,selectedSiAnuladoTabCobrador,selectedSiRechazadoTabCobrador);	
	}
	public double sumarComprobanteDetraccion(String fechaHoraToString,
			String fechaHoraToString2) {
		return carteraDAO.sumarComprobanteDetraccion(fechaHoraToString,fechaHoraToString2);	
	}
	public double sumarComprobanteAutoDetraccion(String fechaHoraToString,
			String fechaHoraToString2) {
		return carteraDAO.sumarComprobanteAutoDetraccion(fechaHoraToString,fechaHoraToString2);	
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idnotadebito) {
		carteraDAO.apuntarComprobanteAnotadebito(idcomprobantetemporal,idnotadebito);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void apuntarComprobanteAnotaDebitoPenalizacion(int idcomprobantetemporal,int idnotadebito) {
		carteraDAO.apuntarComprobanteAnotaDebitoPenalizacion(idcomprobantetemporal,idnotadebito);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void apuntarComprobanteAnotadebito(int idcomprobantetemporal,int idnotadebito,double mora) {
		carteraDAO.apuntarComprobanteAnotadebito(idcomprobantetemporal,idnotadebito,mora);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void apuntarComprobanteAnotadebitoPenalizacion(int idcomprobantetemporal,int idnotadebito,double montopenalizacion) {
		carteraDAO.apuntarComprobanteAnotadebitoPenalizacion(idcomprobantetemporal,idnotadebito,montopenalizacion);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void actualizarComprobante(ComprobanteBean comprobantepago) {
		carteraDAO.actualizarComprobante(comprobantepago);
		
		if (comprobantepago.getSifacturacionelectronica()  && comprobantepago.getFecCancelacion()!=null) {
			
			cuotaDAO.actualizarEstadoCuota(comprobantepago.getIdcomprobante());
		}
		
	}
	
	public List<Comprobantepago> getIdComprobantesPadreDeNotaDebito_Credito(Integer idcomprobante,String tipo) {
		return carteraDAO.getIdComprobantesPadreDeNotaDebito_Credito(idcomprobante,tipo);
	}
	
	public boolean siExisteComprobanteRegistrado(String nroserie,String nrocomprobante) {
		return carteraDAO.siExisteComprobanteRegistrado(nroserie,nrocomprobante);
	}
	
	public Comprobantepago obtenerComprobanteModel(Integer idcomprobante) {
		return carteraDAO.obtenerComprobanteModel(idcomprobante);
	}
	@Transactional(readOnly = false)
	public Comprobantepago obtenerComprobantexInquilino(Integer idcomprobante,Integer idinquilino) {
		return carteraDAO.obtenerComprobantexInquilino(idcomprobante,idinquilino);
	}
	public Sunat_Comprobante obtenerSunatComprobanteModel(Integer idcomprobante) {
		return carteraDAO.obtenerSunatComprobanteModel(idcomprobante);
	}
	
	public List<Integer> obtenerListaIdSunatComprobanteRechazadosxPeriodo(){
		return carteraDAO.obtenerListaIdSunatComprobanteRechazadosxPeriodo();
	}
	public List<Integer> obtenerListaIdComprobanteRechazadosxPeriodo(){
		return carteraDAO.obtenerListaIdComprobanteRechazadosxPeriodo();
	}
	public List<Detallecuota> obtenerDetalleComprobante(int idcomprobante) {
		return carteraDAO.obtenerDetalleComprobante(idcomprobante);
	}
	public List<Detallecuota_arbitrio> obtenerDetalleCuotaArbitrioComprobante(int idcomprobante) {
		return carteraDAO.obtenerDetalleCuotaArbitrioComprobante(idcomprobante);
	}
	public String obtenerDescripcionComprobantePago(String idtipDocu) {
		return carteraDAO.obtenerDescripcionComprobantePago(idtipDocu);
	}
	
	public String obtenerDescripcionTipoMovimiento(String idtipomovimiento) {
		return carteraDAO.obtenerDescripcionTipoMovimiento(idtipomovimiento);
	}
	
	public String obtenerDescripcionTipoPago(String idtipopago) {
		return carteraDAO.obtenerDescripcionTipoPago(idtipopago);
	}
	
	public Double obtenerMoraComprobante(Integer idcomprobantepadre) {
		return carteraDAO.obtenerMoraComprobante(idcomprobantepadre);
	}
	
	public List<Comprobantepago> listaComprobantes(int idcontrato) {
		return carteraDAO.listaComprobantes(idcontrato);
	}
	public List<DetallecomprobanteBean> obtenerDetalleComprobanteBean(
			int idcomprobante) {
		return carteraDAO.obtenerDetalleComprobanteBean(idcomprobante);
	}
	
	public void eliminarComprobante(int idcomprobante) {
		carteraDAO.eliminarComprobante(idcomprobante);
	}
	public void anularComprobante(Comprobantepago cp) {
		carteraDAO.anularComprobante(cp);
	}
	public void anularComprobanteSFE(Sunat_Comprobante scp) {
		carteraDAO.anularComprobanteSFE(scp);
	}
	public Integer obtenerNroSubpagosMesDetalleCuota(int idcuota) {
		return carteraDAO.obtenerNroSubpagosMesDetalleCuota(idcuota);
	}
	public Integer obtenerNroSubpagosMesDetalleCuotaArb(int idcuota) {
		return carteraDAO.obtenerNroSubpagosMesDetalleCuotaArb(idcuota);
	}
	public Cuota obtenerCuota(int idcuota) {
		return carteraDAO.obtenerCuota(idcuota);
	}
	public Cuota_arbitrio obtenerCuotaArbitrio(int idcuota) {
		return carteraDAO.obtenerCuotaArbitrio(idcuota);
	}
	public Cuota obtenerCuota(int idcontrato, String mes , Integer anio) {
		return carteraDAO.obtenerCuota(idcontrato,mes,anio);
	}
	public void actualizarCuota(Cuota cuo) {
		carteraDAO.actualizarCuota(cuo);
	}
	public void actualizarCuotaNC(Cuota cuo) {
		carteraDAO.actualizarCuotaNC(cuo);
	}
    public void actualizarDetalleCuota(Detallecuota dcuo){
    	carteraDAO.actualizarDetalleCuota(dcuo);
    }
	public void actualizarCuotaArbitrio(Cuota_arbitrio cuo) {
		carteraDAO.actualizarCuotaArbitrio(cuo);
	}
	public void actualizarArbitrio(Arbitrio a) {
		carteraDAO.actualizarArbitrio(a);
	}
	public void actualizarEstadoDetalleCuota(Detallecuota dcuo){
		carteraDAO.actualizarEstadoDetalleCuota(dcuo);
	}
	public void actualizarEstadoDetalleCuotaArbitrio(Detallecuota_arbitrio dcuo){
		carteraDAO.actualizarEstadoDetalleCuotaArbitrio(dcuo);
	}
	public void actualizarEstadoComprobante(Comprobantepago cp){
		carteraDAO.actualizarEstadoComprobante(cp);
	}
	public void eliminarCuota(int idcuota) {
		carteraDAO.eliminarCuota(idcuota);
	}
	
	public Double sumarMoraComprobanteCobrador(int idusuario,
			String desde, String hasta) {
		return carteraDAO.sumarMoraComprobanteCobrador(idusuario,desde,hasta);
	}
	
	public int NroUpasMoraCobradaCobrador(int idusuario, String desde,
			String hasta) {
		return carteraDAO.NroUpasMoraCobradaCobrador(idusuario,desde,hasta);
	}
	
	public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado) {
		return carteraDAO.obtenerDetalleComprobantePadre(idComprobantePagoPadrenotatipo,tipoDescuentoNotaCreditoSeleccionado);
	}
	public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado,int idcontrato) {
		return carteraDAO.obtenerDetalleComprobantePadre(idComprobantePagoPadrenotatipo,tipoDescuentoNotaCreditoSeleccionado, idcontrato);
	}
	public Comprobantepago obtenerDetalleComprobantePago(int idComprobantePago) {
		return carteraDAO.obtenerDetalleComprobantePago(idComprobantePago);
	}
	public int NroUpasVisitadasCobrador(int idusuario, String desde,
			String hasta) {
		return carteraDAO.NroUpasVisitadasCobrador(idusuario,desde,hasta);
	}
	
	public boolean siregistradocomprobantepago(String nrocomprobante) {
		return carteraDAO.siregistradocomprobantepago(nrocomprobante);
	}
	
	public void editarEstadoPago(Integer idcuota, String estado) {
		carteraDAO.editarEstadoPago(idcuota,estado);
	}
	public List<Cuota> obtenerPagosContrato(int idcontrato) {
		return carteraDAO.obtenerPagosContrato(idcontrato);
	}
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu) {
		return carteraDAO.listaComprobantesContrato(idcontrato,idtipDocu);
	}
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu , String tipo) {
		return carteraDAO.listaComprobantesContrato(idcontrato,idtipDocu,tipo);
	}
	public List<Comprobantepago> listaComprobantes(String tipodocumento) {
		return carteraDAO.listaComprobantes(tipodocumento);
	}
	
	public Contrato obtenerContratoxId(Integer id) {
		return carteraDAO.obtenerContratoxId(id);
	}
	
	public List<Contrato> listarContratosVigentes() {
		return carteraDAO.listarContratosVigentes();
	}
	public Double obtenerSumaComprobanteEspecializado(String desde,String hasta, String tipo, String uso) {
		return carteraDAO.obtenerSumaComprobanteEspecializado(desde,hasta,tipo,uso);
	}
	
	public void apuntarComprobanteAnotacredito(int idcomprobanteRef,int idnotacredito) {
		carteraDAO.apuntarComprobanteAnotacredito(idcomprobanteRef,idnotacredito);
	}
	public void apuntarComprobanteAnotacredito2(int idcomprobanteRef,int idnotacredito) {
		carteraDAO.apuntarComprobanteAnotacredito2(idcomprobanteRef,idnotacredito);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)	
	public void grabarComprobantePago(Comprobantepago cp) {
		try{			
			NumeracionComprobantePagoBean numeracionComprobantePagoBean=maeNumeracionComprobantePagoDAO.obtenerNumeracionComprobantePago(Constante.TIPO_DOCUMENTO_RECIBO_CAJA);
			if(cp.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
			//	NumeracionComprobantePagoBean numeracionComprobantePagoBean=maeNumeracionComprobantePagoDAO.obtenerNumeracionComprobantePago(Constante.TIPO_DOCUMENTO_RECIBO_CAJA);
				cp.setNroserie(numeracionComprobantePagoBean.getSerie());
				cp.setNrocomprobante(numeracionComprobantePagoBean.getNumero());
				cp.setNroseriecomprobante(cp.getNroserie()+ "-" + cp.getNrocomprobante());
				cp.setSidocumento(Boolean.TRUE);
			}
			cp.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
			carteraDAO.grabarComprobantePago(cp);
			if(cp.getTipodocu().getIdtipodocu().equals(Constante.TIPO_DOCUMENTO_RECIBO_CAJA)){
				maeNumeracionComprobantePagoDAO.usarNumeracionComprobantepagoRC(numeracionComprobantePagoBean.getIdnumeracioncomprobantepago());
			}
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
			//throw new Exception(e.getMessage(), e);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)	
	public void grabarComprobanteDetalle(Sunat_Comprobante_Detalle compdetalle) {
		carteraDAO.grabarComprobanteDetalle(compdetalle);
		
	}
	public CuotaBean obtenerValorCuotaPagada(int idcontrato, int anio, String mes) {
		return carteraDAO.obtenerValorCuotaPagada(idcontrato,anio,mes);
	}
		
	public List<Cuota> listarcuotasxcontrato(int idcontrato) {
		return carteraDAO.listarcuotasxcontrato(idcontrato);
	}
	public List<Cuota> listarcuotascontrato(int idcontrato) {
		return carteraDAO.listarcuotascontrato(idcontrato);
	}
	public List<Detallecuota> listardetallecuotasxcontrato(int idcontrato) {
		return carteraDAO.listardetallecuotasxcontrato(idcontrato);
	}
	public List<Detallecuota> listardetallecuotascontrato(int idcontrato) {
		return carteraDAO.listardetallecuotascontrato(idcontrato);
	}
	public Double obtenerTotalValorPagadoDolares(int idcuota) {
		return carteraDAO.obtenerTotalValorPagadoDolares(idcuota);
	}

	@Override
	public List<Comprobantepago> listarReciboCajaReferencia(int idcondicion, String idtipoconcepto) {
		return carteraDAO.listarReciboCajaReferencia(idcondicion,idtipoconcepto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void actualizarComprobantePago(Comprobantepago cp) {
		cp.setIpoperacion(CommonsUtilities.getRemoteIpAddress());
		carteraDAO.actualizarComprobantePago(cp);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void grabarBoletaFacturaFE(Comprobantepago comprobantepago,String idtipodocuseleccionado, String nombrecarteraContrato) throws Exception {
		
		try{
			NumeracionComprobantePagoBean numeracionComprobantePagoBean=maeNumeracionComprobantePagoDAO.obtenerNumeracionBoletaFactura(idtipodocuseleccionado,nombrecarteraContrato);
			
			comprobantepago.setNroserie(numeracionComprobantePagoBean.getSerie());
			comprobantepago.setNrocomprobante(numeracionComprobantePagoBean.getNumero());
			comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
			
			grabarComprobantePago(comprobantepago);
			maeNumeracionComprobantePagoDAO.usarNumeracionComprobantepago(numeracionComprobantePagoBean.getIdnumeracioncomprobantepago());
			
		} catch(Exception e){
			
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void grabarSunatComprobanteDetalle(Sunat_Comprobante_Detalle compdetalle) throws Exception{
		try{
			grabarComprobanteDetalle(compdetalle);
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}
	}

	@Override
	public void grabarNotaDebitoCreditoFE(Comprobantepago comprobantepago,String idtipodocuseleccionado, String nombrecarteraContrato,String idtipodocpadrenotadebito) throws Exception {
		try{
			NumeracionComprobantePagoBean numeracionComprobantePagoBean=maeNumeracionComprobantePagoDAO.obtenerNumeracionNotaDebitoCredito(idtipodocuseleccionado,nombrecarteraContrato,idtipodocpadrenotadebito);
			
			comprobantepago.setNroserie(numeracionComprobantePagoBean.getSerie());
			comprobantepago.setNrocomprobante(numeracionComprobantePagoBean.getNumero());
			comprobantepago.setNroseriecomprobante(comprobantepago.getNroserie()+ "-" + comprobantepago.getNrocomprobante());
			
			grabarComprobantePago(comprobantepago);
			maeNumeracionComprobantePagoDAO.usarNumeracionComprobantepago(numeracionComprobantePagoBean.getIdnumeracioncomprobantepago());
			
		}catch(Exception e){
			
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}
	}

	@Override
	public boolean siExisteNumeroOperacion(String numerooperacion) {
		return maeNumeracionComprobantePagoDAO.siExisteNumeroOperacion(numerooperacion);
	}
    public List<Cuota_arbitrio> listarcuotasxarbitrio(int idarbitrio){
    	return carteraDAO.listarcuotasxarbitrio(idarbitrio);
    }
    
    public Sunat_Comprobante obtenerComprobante(Comprobantepago comp){
    	return carteraDAO.obtenerComprobante(comp);
    }
    @Transactional(readOnly = false ,rollbackFor= Exception.class)
    public void updateComprobanteSgiSunatFe(Comprobantepago comprobante){
    	   carteraDAO.updateComprobanteSgiSunatFe(comprobante);
    }
}
