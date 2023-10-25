package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Seguimientoflujo;

public interface IDioDAO {

	List<Seguimientoflujo> obtenerSeguimientoFlujodeDocumento(int mes);
	void regresarSupervisor(int idsegflujodocumento);
	List<Seguimientoflujo> obtenerSeguimientoFlujodeDocumentoAtendido(
			int mesSeleccionado);
	void actualizarComentarioRespuestaSeguimientoFlujoDocumento(
			int idsegflujodocumento, String comentarioRechazo,
			int idusuarioremitente, String titulo);
	List<Seguimientoflujo> listaFiltroDocumentosMes(String respuesta);
	int obtenerNumeroDerivadosMes(int mesActualcapturado);
	int obtenerNumeroDerivados();
	int obtenerNumeroPendientesMes(int mesActualcapturado);
	int obtenerNumeroPendientes();
	int obtenerNumeroDocumentosMes(String respuesta, int mesActualcapturado);
	int obtenerNumeroDocumentosMes(String respuesta);
	Boolean estaDocumentoFinalizado(int idsegflujodocumento);
	String getUltimoExpedienteDespacho();



}
