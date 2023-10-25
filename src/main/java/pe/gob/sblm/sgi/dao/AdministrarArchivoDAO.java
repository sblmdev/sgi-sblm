package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;

public interface AdministrarArchivoDAO {

	public List<ArchivoAdjuntoBean> obtenerArchivosReferencia(int idinmueble,String nombreEntidad);
	public void actualizarestadodocumentocontrato(int idcontrato);
	public void actualizarEstadoxDocumentoReferencia(String entidad ,Integer id);

}
