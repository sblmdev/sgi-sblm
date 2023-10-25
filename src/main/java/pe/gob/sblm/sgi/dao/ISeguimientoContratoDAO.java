package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Detallesegcontrato;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Requisito;
import pe.gob.sblm.sgi.entity.Seguimientocontrato;
import pe.gob.sblm.sgi.entity.Tiporequisito;
import pe.gob.sblm.sgi.entity.Usuario;

public interface ISeguimientoContratoDAO {
	public void registrarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato);

	public void eliminarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato);

	public List<Seguimientocontrato> listarSeguimientoContratos();

	public Seguimientocontrato obtenerUltimoSeguimientoContrato();

	public Seguimientocontrato obtenerSeguimientoContratoPorId(int id);

	int obtenerNumeroRegistros();

	List<Contrato> listarContratos();

	List<Tiporequisito> listarTiporequisitos();

	Tiporequisito obtenerTiporequisitoPorId(int id);

	public List<Requisito> obteneRequisitos();

	public void registrarDetalleSeguimientoContrato(
			Detallesegcontrato detallesegcontrato);
	List<Seguimientocontrato> listarSeguimientoContratosXContrato(Contrato con);

	Seguimientocontrato obtenerSeguimientoContratoParametros(int idcontrato,
			int paso);

	List<Integer> listarIDContratosDeSeguimientoContrato();

	List<Seguimientocontrato> listarSeguimientoContratosXContrato(int idcontrato);
	
	//franco
		public List<Detallesegcontrato> obtenerDetalleSeguimientoContrato(int idcontrato);

	public List<Detallesegcontrato> obtenerTodo();

	public int pasoactualcontrato(int idcontrato);

	public void eliminardetalleSeguimientoContrato(int idsegcontrato);

	public Boolean obtenerValorSiFinalizado(int idpaso, int idcontrato);

	public void actualizarPaso(int pasoactualcontrato, String nombresusr, String rutaimagenusr);

	public List<Usuario> obtenerUsuarios();

	public List<Perfilusuario> getPerfilesUsuario(int idUsuarioSeleccionado);

	public List<Perfil> obtenerPerfiles();

	public void actualizarPaso3YNotificar(int idcontrato, int nroPaso,
			String contenidoMensajePersonalizado,
			List<Usuario> listadousuariosSeleccionados, String destinatarios);

	public void grabarArchivosAdjuntos(Archivo archivodocu);

	public void eliminarArchivoDocumentoNoEncontrado(int idarchivodocumento);

	public List<Seguimientocontrato> obtenerSeguimientoContrato(int idcontrato);
}
