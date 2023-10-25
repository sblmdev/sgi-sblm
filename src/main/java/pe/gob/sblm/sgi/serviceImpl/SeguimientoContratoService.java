package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.ISeguimientoContratoDAO;
import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Detallesegcontrato;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Requisito;
import pe.gob.sblm.sgi.entity.Seguimientocontrato;
import pe.gob.sblm.sgi.entity.Tiporequisito;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.ISeguimientoContratoService;
@Transactional(readOnly = true)
@Service(value="seguimientocontratoService")
public class SeguimientoContratoService implements ISeguimientoContratoService {
	@Autowired
	private ISeguimientoContratoDAO seguimientocontratoDAO; 

	@Transactional(readOnly = false)
	
	public void registrarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato) {
		getSeguimientocontratoDAO().registrarSeguimientoContrato(seguimientoContrato);
		
	}

	@Transactional(readOnly = false)
	
	public void eliminarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato) {
		getSeguimientocontratoDAO().eliminarSeguimientoContrato(seguimientoContrato);
		
	}

	
	public List<Seguimientocontrato> listarSeguimientoContratos() {
		return getSeguimientocontratoDAO().listarSeguimientoContratos();
	}
	
	public List<Seguimientocontrato> listarSeguimientoContratosXContrato(int idcontrato) {
		return getSeguimientocontratoDAO().listarSeguimientoContratosXContrato(idcontrato);
	}
	
	
	public List<Integer> listarIDContratosDeSeguimientoContrato() {
		return getSeguimientocontratoDAO().listarIDContratosDeSeguimientoContrato();
	}
	
	
	public List<Seguimientocontrato> listarSeguimientoContratosXContrato(Contrato cont) {
		return getSeguimientocontratoDAO().listarSeguimientoContratosXContrato(cont);
	}
	
	
	public List<Contrato> listarContratos() {
		return getSeguimientocontratoDAO().listarContratos();
	}
	
	public List<Tiporequisito> listarTiporequisitos() {
		return getSeguimientocontratoDAO().listarTiporequisitos();
	}
	
	
	public Seguimientocontrato obtenerUltimoSeguimientoContrato() {
		return getSeguimientocontratoDAO().obtenerUltimoSeguimientoContrato();
	}
	
	public Seguimientocontrato obtenerSeguimientoContratoParametros(int idcontrato, int paso) {
		return getSeguimientocontratoDAO().obtenerSeguimientoContratoParametros(idcontrato,paso);
	}
	
	public List<Requisito> obteneRequisitos() {
		return getSeguimientocontratoDAO().obteneRequisitos();
	}
	
	public Seguimientocontrato obtenerSeguimientoContratoPorId(int id) {
		return getSeguimientocontratoDAO().obtenerSeguimientoContratoPorId(id);
	}
	
	public Tiporequisito obtenerTiporequisitoPorId(int id) {
		return getSeguimientocontratoDAO().obtenerTiporequisitoPorId(id);
	}
	
	
	public int obtenerNumeroRegistros() {
		return getSeguimientocontratoDAO().obtenerNumeroRegistros();
	}

	public ISeguimientoContratoDAO getSeguimientocontratoDAO() {
		return seguimientocontratoDAO;
	}

	public void setSeguimientocontratoDAO(
			ISeguimientoContratoDAO seguimientocontratoDAO) {
		this.seguimientocontratoDAO = seguimientocontratoDAO;
	}

//franco
	
	public void registrarDetalleSeguimientoContrato(Detallesegcontrato detallesegcontrato) {
		getSeguimientocontratoDAO().registrarDetalleSeguimientoContrato(detallesegcontrato);
		
	}

	
	public List<Detallesegcontrato> obtenerDetalleSeguimientoContrato(int idcontrato) {
		return getSeguimientocontratoDAO().obtenerDetalleSeguimientoContrato(idcontrato);
	}

	
	public List<Detallesegcontrato> obtenerTodo() {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().obtenerTodo();
	}

	
	public int pasoactualcontrato(int idcontrato) {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().pasoactualcontrato(idcontrato);
	}

	
	public void eliminardetalleSeguimientoContrato(int idsegcontrato) {
		getSeguimientocontratoDAO().eliminardetalleSeguimientoContrato(idsegcontrato);
		
	}

	
	public Boolean obtenerValorSiFinalizado(int idpaso,int idcontrato) {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().obtenerValorSiFinalizado(idpaso,idcontrato);
	}

	
	public void actualizarPaso(int pasoactualcontrato,String nombresusr, String rutaimagenusr) {
		getSeguimientocontratoDAO().actualizarPaso(pasoactualcontrato,nombresusr,rutaimagenusr);
		
	}

	
	public List<Usuario> obtenerUsuarios() {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().obtenerUsuarios();
	}

	
	public List<Perfilusuario> getPerfilesUsuario(int idUsuarioSeleccionado) {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().getPerfilesUsuario(idUsuarioSeleccionado);
	}

	
	public List<Perfil> obtenerPerfiles() {
		// TODO Auto-generated method stub
		return getSeguimientocontratoDAO().obtenerPerfiles();
	}

	
	public void actualizarPaso3YNotificar(int idcontrato, int nroPaso,
			String contenidoMensajePersonalizado,
			List<Usuario> listadousuariosSeleccionados, String destinatarios) {
		getSeguimientocontratoDAO().actualizarPaso3YNotificar(idcontrato,nroPaso,contenidoMensajePersonalizado,listadousuariosSeleccionados,destinatarios);
		
	}

	
	public void grabarArchivosAdjuntos(Archivo archivodocu) {
		getSeguimientocontratoDAO().grabarArchivosAdjuntos(archivodocu);
		
	}

	
	public void eliminarArchivoDocumentoNoEncontrado(int idarchivodocumento) {
		getSeguimientocontratoDAO().eliminarArchivoDocumentoNoEncontrado(idarchivodocumento);
		
	}

	
	public List<Seguimientocontrato> obtenerSeguimientoContrato(int idcontrato) {
		return getSeguimientocontratoDAO().obtenerSeguimientoContrato(idcontrato);
	}


}
