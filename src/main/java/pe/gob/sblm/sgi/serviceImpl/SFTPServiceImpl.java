package pe.gob.sblm.sgi.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.gob.sblm.api.sftp.client.SftpClient;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.controller.ArchivoManagedBean;
import pe.gob.sblm.sgi.service.InicioService;
import pe.gob.sblm.sgi.service.SFTPService;

@Service(value="sftpService")
public class SFTPServiceImpl implements SFTPService {
	
	private static final Logger Logger = LoggerFactory.getLogger(ArchivoManagedBean.class.getName());
	
	@Autowired
	private InicioService inicioService;

	@Override
	public void upload(ArchivoAdjuntoBean archivoAdjuntoBean)
			throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		
		try{
			sftpClient.iniciarConexion();
			
			InputStream inputStream = new ByteArrayInputStream( archivoAdjuntoBean.getStream());
			sftpClient.upload(inputStream, archivoAdjuntoBean.getUuidSftp(),createDirectory(archivoAdjuntoBean),archivoAdjuntoBean.getTipo());
			inputStream.close();

		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}finally{
			
			sftpClient.cerrarConexion();
		}
	}
	@Override
	public byte[] downloadByteArray(String nombreArchivo, Date fcreacion)
			throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		
		try{
			sftpClient.iniciarConexion();
			
			return sftpClient.download(nombreArchivo, fcreacion);
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
			
		}finally{
			sftpClient.cerrarConexion();
		}
	}
	@Override
	public byte[] downloadByteArray(String nombreArchivo, Date fcreacion,String ruta)
			throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		
		try{
			sftpClient.iniciarConexion();
			
			return sftpClient.download(nombreArchivo, fcreacion,ruta);
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
			
		}finally{
			sftpClient.cerrarConexion();
		}
	}

 
	@Override
	public String createDirectory(ArchivoAdjuntoBean archivoAdjuntoBean) throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		try{
			sftpClient.iniciarConexion();
			
			return sftpClient.createDirectory(archivoAdjuntoBean.getFeccre(), archivoAdjuntoBean.getRuta());
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
			
		}finally{
			sftpClient.cerrarConexion();
		}
	}
	@Override
	public byte[] visualizarByteArray(String nombreArchivo, Date fcreacion)
			throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		
		try{
			sftpClient.iniciarConexion();
			
			return sftpClient.download(nombreArchivo, fcreacion);
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
			
		}finally{
			sftpClient.cerrarConexion();
		}
	}
	@Override
	public byte[] visualizarByteArray(String nombreArchivo, Date fcreacion,String ruta)
			throws Exception {
		
		SftpClient sftpClient = new SftpClient(inicioService.getParamConfigSftpBean().getHost(), inicioService.getParamConfigSftpBean().getUser(), inicioService.getParamConfigSftpBean().getPass(), inicioService.getParamConfigSftpBean().getPort());
		
		try{
			sftpClient.iniciarConexion();
			
			return sftpClient.download(nombreArchivo, fcreacion,ruta);
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
			
		}finally{
			sftpClient.cerrarConexion();
		}
	}
	
}
