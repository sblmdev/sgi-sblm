package pe.gob.sblm.sgi.serviceImpl;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.alfresco.manager.CMISManager;
import pe.gob.sblm.api.alfresco.manager.CMISManagerBuilder;
import pe.gob.sblm.api.alfresco.manager.FileProperties;
import pe.gob.sblm.api.alfresco.repository.Repository;
import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.DataFileUtil;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.service.AlfrescoService;
import pe.gob.sblm.sgi.service.InicioService;

@Transactional(readOnly = true)
@Service(value="alfrescoService")
public class AlfrescoServiceImpl implements AlfrescoService {
	
	private static final Logger Logger = LoggerFactory.getLogger(AlfrescoServiceImpl.class);
	
    CMISManager cmisManager;
    
	@Autowired
	private InicioService inicioService;
	
	public void abriConexion() throws Exception {
		Repository repository = new Repository();
		
        repository.setUser(inicioService.getParamConfigAlfrescoBean().getUser());
        repository.setPassword(inicioService.getParamConfigAlfrescoBean().getPass());
        repository.setHost(inicioService.getParamConfigAlfrescoBean().getHost());
        repository.setPort(inicioService.getParamConfigAlfrescoBean().getPort());
        repository.setAtompub_url("default");

        try {
        	CMISManagerBuilder builderManager = new CMISManagerBuilder();
            cmisManager = builderManager.build(repository);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}

    }

    public String storeFile(ArchivoAdjuntoBean archivoAdjuntoBean,String vinculoDucumento,String path) throws Exception {
    	
    	try {
			abriConexion();	
	        FileProperties fp = new FileProperties();
	        fp.addProp(PropertyIds.OBJECT_TYPE_ID, "D:sgicm:sgiDocumento");
	        fp.addProp(PropertyIds.NAME, archivoAdjuntoBean.getNombre());
	        fp.addProp(PropertyIds.CREATED_BY, "admin");
	        
	         return cmisManager.cmisUploadFile(fp,  archivoAdjuntoBean.getStream(),"", createDirectory(archivoAdjuntoBean),DataFileUtil.obtenerMimeTypeArchivo(archivoAdjuntoBean.getTipo()));
	    } catch (Exception e) {
	 		Logger.error(e.getMessage(), e);
	 		e.printStackTrace();
			throw new Exception(e.getMessage(), e);
	    }
    }

	public String createDirectory(ArchivoAdjuntoBean archivoAdjuntoBean) throws Exception {
		
		try {
			abriConexion();
		      String newFolder = "";
		      
		      String path="";
		      if(archivoAdjuntoBean.getRuta()!=null){
		    	  if(archivoAdjuntoBean.getRuta().equals(Constantes.DIR_SGI_RECIBO_CAJA)){
		    		  path=archivoAdjuntoBean.getRuta();
		    	  }else{
		    		  path=Constantes.DIR_PRINCIPAL_SGI;
		    	  }
		      }else{
		    	  path=Constantes.DIR_PRINCIPAL_SGI;
		      }
	
		      newFolder = FechaUtil.getYear(archivoAdjuntoBean.getFeccre());
		      cmisManager.createFolferIfNotExist(path,newFolder);
	
		      path = path + "/" + newFolder;
		      newFolder = FechaUtil.getMonth(archivoAdjuntoBean.getFeccre());
		      cmisManager.createFolferIfNotExist(path, newFolder);
	
		      path = path + "/" + newFolder;
		      newFolder = FechaUtil.getDay(archivoAdjuntoBean.getFeccre());
		      cmisManager.createFolferIfNotExist(path, newFolder);
	
		      path = path + "/" + newFolder;
	
		      return path.toString();
		} catch (Exception e) {
     		Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	}



}
