package pe.gob.sblm.sgi.service;

import java.util.Date;

import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;



public interface SFTPService {

	public void upload(ArchivoAdjuntoBean archivoAdjuntoBean) throws Exception;
	public byte[] downloadByteArray(String nombre, Date fcreacion)throws Exception ;
	public byte[] downloadByteArray(String nombre, Date fcreacion,String ruta)throws Exception ;
	public String createDirectory(ArchivoAdjuntoBean archivoAdjuntoBean) throws Exception;
	public byte[] visualizarByteArray(String nombre, Date fcreacion)throws Exception ;
	public byte[] visualizarByteArray(String nombre, Date fcreacion,String ruta)throws Exception ;
	

}
