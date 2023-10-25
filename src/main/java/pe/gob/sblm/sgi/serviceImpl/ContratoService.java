package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.dao.IContratoDAO;
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
import pe.gob.sblm.sgi.service.IContratoService;

@Transactional(readOnly = true)
@Service(value="contratoService")
public class ContratoService implements IContratoService{
	
	private static final Logger Logger = LoggerFactory.getLogger(ContratoService.class);
	
	@Autowired
	private IContratoDAO contratoDAO;
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void registrarContrato(Contrato contrato) throws Exception {
		
		
		try{
		getContratoDAO().registrarContrato(contrato);
		
		}catch(Exception e){
			
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void registrarMaeDetalleCondicion(MaeDetalleCondicion maedetalle) throws Exception {
		
		
		try{
		getContratoDAO().registrarMaeDetalleCondicion(maedetalle);
		
		}catch(Exception e){
			
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
	}
	
	
	
	public IContratoDAO getContratoDAO() {
		return contratoDAO;
	}

	public void setContratoDAO(IContratoDAO contratoDAO) {
		this.contratoDAO = contratoDAO;
	}

	
	
	public Contrato obtenerContratoPorID(int id) {
		return getContratoDAO().obtenerContratoPorID(id);
	}
	
	public List<Uso> getListaUsos() {
		return contratoDAO.getListaUsos();
	}
	
	public List<Contrato> ListarContratosPorID(int idcontrato) {
		return contratoDAO.ListarContratosPorID(idcontrato);
	}

	
	public List<Inquilino> getListaInquilino() {
		return contratoDAO.getListaInquilino();
	}

	
	public List<Upa> buscarUpasXInmueble(int idinmueble) {
		return contratoDAO.buscarUpasXInmueble(idinmueble);
	}

	
	public List<CondicionBean> getListaContrato(String condicion) {
		return contratoDAO.getListaContrato(condicion);
	}

	
	public List<Institucion> getListaInstitucion() {
		return contratoDAO.getListaInstitucion();
	}


	
	public List<Representante> getListaRepresentante() {
		// TODO Auto-generated method stub
		return contratoDAO.getListaRepresentante();
	}


	
	public List<Cliente> getListaCliente() {
		// TODO Auto-generated method stub
		return contratoDAO.getListaCliente();
	}

	
	public List<Usuario> obtenerUsuarios() {
		// TODO Auto-generated method stub
		return contratoDAO.obtenerUsuarios();
	}

	
	public void enviarNotificacionPersonalizable(
			String contenidoMensajePersonalizado, int idusuariodestino) {
		contratoDAO.enviarNotificacionPersonalizable(contenidoMensajePersonalizado,idusuariodestino);
		
	}
	
	public List<Cuentabancaria> getCtasBancarias() {
		// TODO Auto-generated method stub
		return contratoDAO.getCtasBancarias();
	}


	
	public void registrarCoutasContrato(Cuota couta) {
		contratoDAO.registrarCoutasContrato(couta);
		
	}

	
	public List<Contrato> obtenerContratosUpa(int idupa) {
		return  contratoDAO.obtenerContratosUpa(idupa);
	}

	
	public void actualizarEstadoUltimoContratoDeUpa(int idupa) {
		contratoDAO.actualizarEstadoUltimoContratoDeUpa(idupa);
	}

	
	public List<Cuota> obtenerCuotasDeContrato(int idcontratoGlobal) {
		return contratoDAO.obtenerCuotasDeContrato(idcontratoGlobal);
	}

	
	public List<Detallecuota> obtenerDetalleDeCuota(int idcuota) {
		return contratoDAO.obtenerDetalleDeCuota(idcuota);
	}

	
	public boolean verificarExistenciaCodigoContrato(String nrocontrato) {
		return contratoDAO.verificarExistenciaCodigoContrato(nrocontrato);
	}

	
	public CondicionBean obtenerContratoxNrocontrato(String nrocontrato) {
		return contratoDAO.obtenerContratoxNrocontrato(nrocontrato);
	}

	
	public List<Contrato> obtenerContratosInquilino(int idinquilinoseleccionado) {
		return contratoDAO.obtenerContratosInquilino(idinquilinoseleccionado);
	}

	
	public Double sumacobrado(int idcontrato) {
		return contratoDAO.sumacobrado(idcontrato);
	}
    

	
	public void actualizarMonto(int idcontrato, Double sumacobrado) {
		contratoDAO.actualizarMonto(idcontrato,sumacobrado);
	}

	
	public void finalizarContrato(int idcontrato) {
		contratoDAO.actualizarMonto(idcontrato);
	}

	
	public List<CondicionBean> buscarConSinContratoxClave(String valorbusqueda,String tipo) {
		return contratoDAO.buscarConSinContratoxClave(valorbusqueda,tipo);
	}
	public List<CondicionBean> buscarContrato(Contrato contrato) {
		return contratoDAO.buscarContrato(contrato);
	}
	
	public List<CondicionBean> buscarConSinContratoxDireccion(String valorbusqueda,String tipo) {
		return contratoDAO.buscarConSinContratoxDireccion(valorbusqueda,tipo);
	}

	
	public List<CondicionBean> buscarConSinContratoxNombreInquilino(String valorbusqueda,String tipo) {
		return contratoDAO.buscarConSinContratoxNombreInquilino(valorbusqueda,tipo);
	}
	public List<CondicionBean> buscarAdenda(String valorbusqueda,String tipo,int tipobusqueda) {
		return contratoDAO.buscarAdenda(valorbusqueda,tipo,tipobusqueda);
	}
	
	public void eliminarxCondicionContrato(int idcontrato) {
		contratoDAO.eliminarxCondicionContrato(idcontrato);
	}


	
	public void cambiarCondicionContrato(String condicion, int idcontrato, String numerocontrato) {
		contratoDAO.cambiarCondicionContrato(condicion,idcontrato, numerocontrato);
	}

	
	public List<Contrato> contratosTotalmenteCancelados() {
		return contratoDAO.contratosTotalmenteCancelados();
	}

	
	public void cancelarSinContrato(int idcontratoGlobal, String observacion,Date fecUltimoPagoSinContrato ,String usuario) {
		contratoDAO.cancelarSinContrato(idcontratoGlobal,observacion,fecUltimoPagoSinContrato,usuario);		
	}

	
	public List<Contrato> sincontratoFinalizados() {
		return contratoDAO.sincontratoFinalizados();
	}

	
	public void actualizarContrato(Contrato contrato) {
		contratoDAO.actualizarContrato(contrato);		
	}

	
	public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idcontratoGlobal) {
		return contratoDAO.obtenerPeriodosPagados(idcontratoGlobal);		
	}
	public List<PeriodoPagadoBean> obtenerPeriodosPagadosObservacion(int idcuota) {
		return contratoDAO.obtenerPeriodosPagadosObservacion(idcuota);		
	}
	public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota) {
		return contratoDAO.buscarComprobantesPeriodo(idcuota);		
	}

	
	public List<CondicionBean> obtenerCarteraActiva(String nombrecartera) {
		return contratoDAO.obtenerCarteraActiva(nombrecartera);		
	}

	
	public void incluirAdendaContrato(Adenda adenda,int idcontratoGlobal,String observacionadenda, String fincobro, String fincontratoadenda) {
		contratoDAO.incluirAdendaContrato(adenda,idcontratoGlobal,observacionadenda,fincobro,fincontratoadenda);
	}
	public void incluirAdendaContrato(Adenda adenda,CondicionBean condicion) {
		contratoDAO.incluirAdendaContrato(adenda,condicion);
	}
	
	public void resolverContrato(int idcontratoGlobal, String observacion,String fincobro, String fincontratoresuelto) {
		contratoDAO.resolverContrato(idcontratoGlobal,observacion,fincobro,fincontratoresuelto);
	}
	public void finalizarContrato(int idcontratoGlobal, String observacion,String fincobro, String fincontratoresuelto,String usuario) {
		contratoDAO.finalizarContrato(idcontratoGlobal,observacion,fincobro,fincontratoresuelto,usuario);
	}
	
	public CondicionBean obtenerInformacionCondicion(int idcontrato) {
		return contratoDAO.obtenerInformacionCondicion(idcontrato);
	}

	public CondicionBean obtenerInformacionCondicionxArbitrio(int idupa){
		return contratoDAO.obtenerInformacionCondicionxArbitrio(idupa);
	}
	public List<CondicionBean> obtenerCondicion(String condicion, String estado,String sicancelado) {
		return contratoDAO.obtenerCondicion(condicion,estado,sicancelado);
	}
	public List<CondicionBean> obtenerCondicionDeuda(String condicion, String estado,String sicancelado,String tipomoneda) {
		return contratoDAO.obtenerCondicionDeuda(condicion,estado,sicancelado,tipomoneda);
	}
	public List<CondicionBean> obtenerCondicionDeudaxUpa(String condicion, String estado,String sicancelado,String tipomoneda,String claveupa) {
		return contratoDAO.obtenerCondicionDeudaxUpa(condicion,estado,sicancelado,tipomoneda,claveupa);
	}

	public List<CondicionBean> obtenerCondicionContratoDetraccion(String condicion, String estado,String sicancelado) {
		return contratoDAO.obtenerCondicionContratoDetraccion(condicion,estado,sicancelado);
	}
	public List<CondicionBean> obtenerListaContratoxCuota(String condicion, String estado,String sicancelado) {
		return contratoDAO.obtenerListaContratoxCuota(condicion,estado,sicancelado);
	}

	public List<CondicionBean> obtenerCondicionxFecha(String condicion, String estado,String sicancelado,String desde,String hasta) {
		return contratoDAO.obtenerCondicionxFecha(condicion,estado,sicancelado,desde,hasta);
	}
	
	public Boolean verificarExisteCondicionVigente(int idupa) {
		return contratoDAO.verificarExisteCondicionVigente(idupa);
	}
	public Boolean verificarExisteCondicionVigente(int idupa,String condicion) {
		return contratoDAO.verificarExisteCondicionVigente(idupa,condicion);
	}
	
	public List<CondicionBean> obtenerListaContratoBeanDeUpa(String claveupaTabCondicion) {
		return contratoDAO.obtenerListaContratoBeanDeUpa(claveupaTabCondicion);
	}
	public List<CondicionBean> obtenerListaContratoBean(String claveupaTabCondicion) {
		return contratoDAO.obtenerListaContratoBean(claveupaTabCondicion);
	}
	public List<CondicionBean> obtenerListaContratoUpa() {
		return contratoDAO.obtenerListaContratoUpa();
	}
	public CondicionBean obtenerCondicionBean(int idcondicion) {
		return contratoDAO.obtenerCondicionBean(idcondicion);
	}
	@Override
	public CondicionBean obtenerUltimaCondicion(Integer idupa) {
		return contratoDAO.obtenerUltimaCondicion(idupa);
	}
	@Override
	public List<CondicionBean> obtenerCondicionesVigentes(String carteraActivaReporteCartera) {
		return contratoDAO.obtenerCondicionesVigentes(carteraActivaReporteCartera);
	}

	@Override
	public CondicionBean verificarDisponibilidadContrato(int idupa) {
		return contratoDAO.verificarDisponibilidadContrato(idupa);
	}

	@Override
	public void actualizarDevolucionInmuebleContrato(int idcontrato, String fechaactadevolucion, String numeroactadevolucion) {
		contratoDAO.actualizarDevolucionInmuebleContrato(idcontrato,fechaactadevolucion,numeroactadevolucion);		
	}

	@Override
	public void actualizarPenalidadContrato(int idcontrato,String observacionpenalidad) {
		contratoDAO.actualizarPenalidadContrato(idcontrato,observacionpenalidad);
	}

	@Override
	public List<MaeDetalleCondicion> obtenerListaMaeCondicion(int idcontrato){
		return contratoDAO.obtenerListaMaeCondicion(idcontrato);
	}
	
	@Override
	public List<MaeDetCondicionDetalle> obtenerListaMaeCondicionDetalle(int idcontrato){
		return contratoDAO.obtenerListaMaeCondicionDetalle(idcontrato);
	}
     
	@Override
	public void asignarCondicion(Contrato contrato , Integer idcondicion, String nombreusuario){
		  contratoDAO.asignarCondicion(contrato,idcondicion,nombreusuario);
	}
	
	public int numSecuenciaMaeDetalleCondicion(int idcontrato){
		return contratoDAO.numSecuenciaMaeDetalleCondicion(idcontrato);
	}
	
	@Override
	public MaeDetalleCondicion getMaeDetalleCondicion(int idDetalleCondicion){
		return contratoDAO.getMaeDetalleCondicion(idDetalleCondicion);
	}
	
	@Override
	public List<Fechapago> listarFechaPago(){
		return contratoDAO.listarFechaPago();
	}
	@Override
	public Fechapago  getFechaPago(int id){		
		return contratoDAO.getFechaPago(id);
	}

	@Override
	public Detalleclausula getDetalleClausula(String id) {
		// TODO Auto-generated method stub
		return contratoDAO.getDetalleClausula(id);
	}

	@Override
	public Tipoclausula getTipoClausula(String id) {
		// TODO Auto-generated method stub
		return contratoDAO.getTipoClausula(id);
	}

	@Override
	public List<Tipoclausula> listarTipoClausula() {
		// TODO Auto-generated method stub
		return contratoDAO.listarTipoClausula();
	}

	@Override
	public List<Detalleclausula> listarDetalleTipoClausula(
			String id) {
		// TODO Auto-generated method stub
		return contratoDAO.listarDetalleTipoClausula(id);
	}

	@Override
	public List<Condicionincumplimientoclausula> listarCondicionIncumplimiento(
			Integer idcontrato) {
		// TODO Auto-generated method stub
		return contratoDAO.listarCondicionIncumplimiento(idcontrato);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor= Exception.class)
	public void registrarClausulaContrato(Condicionincumplimientoclausula clausula)throws Exception{
		try{
		getContratoDAO().registrarClausulaContrato(clausula);
		
		}catch(Exception e){
			
			Logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
	}
	public List<CondicionBean> obtenerListaContratos(List<String> condicion,List<String> estado,String desdeinicio,String hastainicio,String desdefin, String hastafin){
		return contratoDAO.obtenerListaContratos(condicion,estado,desdeinicio,hastainicio,desdefin,hastafin);

	}

	@Override
	public void anularContrato(CondicionBean condicion) {
		contratoDAO.anularContrato(condicion);
		
	}

}
