package pe.gob.sblm.sgi.service;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;



public interface AdministrarArchivoService {

	public void grabarArchivo(ArchivoAdjuntoBean archivoAdjuntoBean, String vinculoDocumento, int idEntidad) throws Exception;
	public List<ArchivoAdjuntoBean> obtenerArchivosReferencia(int idinmueble,String nombreEntidad)  throws Exception;
	public void actualizarestadodocumentocontrato(int idcontrato);
	public void actualizarEstadoxDocumentoReferencia(String entidad,Integer id);
	public byte[] descargaArchivoSftp(String nombreArchivo,Date fcreacion) throws Exception;
	public byte[] descargaArchivoSftp(String nombreArchivo,Date fcreacion ,String ruta) throws Exception;
	public byte[] descargaArchivoAlfresco(String uuid) throws Exception;
	public byte[] visualizarArchivoSftp(String nombreArchivo,Date fcreacion) throws Exception;
}
