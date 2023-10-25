package pe.gob.sblm.sgi.service;

import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;


public interface AlfrescoService {

	public String storeFile(ArchivoAdjuntoBean archivoAdjuntoBean,String vinculoDucumento,String path)  throws Exception ;
	public String createDirectory(ArchivoAdjuntoBean archivoAdjuntoBean) throws Exception;
	
}
