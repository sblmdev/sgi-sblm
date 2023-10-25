package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.dao.AdministrarArchivoDAO;
import pe.gob.sblm.sgi.dao.IGenericDAO;
import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.AdministrarArchivoService;
import pe.gob.sblm.sgi.service.AlfrescoService;
import pe.gob.sblm.sgi.service.SFTPService;
import pe.gob.sblm.sgi.util.GeneradorUID;

@Transactional(readOnly = true)
@Service(value="administrarArchivoService")
public class AdministrarArchivoServiceImpl implements AdministrarArchivoService {
	
	private static final Logger Logger = LoggerFactory.getLogger(AdministrarArchivoServiceImpl.class);
	
	@Autowired
	private AlfrescoService alfrescoService;
	
	@Autowired
	private SFTPService sftpService;

	@Autowired
	private IGenericDAO genericDAO;
	
	@Autowired
	private AdministrarArchivoDAO administrarArchivoDAO;

	
	
	@Override
	public void grabarArchivo(ArchivoAdjuntoBean archivoAdjuntoBean,String entidad, int identificador) throws Exception {
		
		try {
			//System.out.println("OBSERVACION="+archivoAdjuntoBean.getObservacion());
			String uuidAlfresco,uuidSftp;
			
			/**1. Grabar Sftp**/
			uuidSftp=GeneradorUID.generateUniqueID();
			archivoAdjuntoBean.setUuidSftp(uuidSftp);
			sftpService.upload(archivoAdjuntoBean);
			
			
			/**2. Grabar Alfresco**/
			uuidAlfresco=alfrescoService.storeFile(archivoAdjuntoBean,entidad,archivoAdjuntoBean.getRuta());
			
			
			/**3. Insertar Base de datos**/
			Archivo archivo= new Archivo();
			archivo.setUid(uuidSftp);//dando una clave unica como nombre de archivo	
			archivo.setUidAlfresco(uuidAlfresco.replace(";1.0", ""));
			archivo.setTipo(archivoAdjuntoBean.getTipo());
			archivo.setTitulo(archivoAdjuntoBean.getTitulo());
			archivo.setDescripcion(archivoAdjuntoBean.getDescripcion());
			archivo.setObservacion(archivoAdjuntoBean.getObservacion());
			archivo.setUsuariocreador(archivoAdjuntoBean.getUsrcre());
			archivo.setFechacreacion(archivoAdjuntoBean.getFeccre());
			archivo.setPeso(archivoAdjuntoBean.getStream().length);
			archivo.setNombre(archivoAdjuntoBean.getNombre());
			
			if (entidad.equals(Constantes.INMUEBLE_SGI)) {
				
				Inmueblemaestro inmueble= new Inmueblemaestro();
				inmueble.setIdinmueble(identificador);
				archivo.setInmueble(inmueble);
			
			}else if(entidad.equals(Constantes.UPA_SGI)){
				Upa upa = new Upa(identificador);
				archivo.setUpa(upa);
				
			}else if(entidad.equals(Constantes.CONTRATO_SGI)||entidad.equals(Constantes.PRECONTRATO_SGI)||entidad.equals(Constantes.CONDICION_SGI)||
					entidad.equals(Constantes.RECONOCIMIENTO_DEUDA_SGI)||entidad.equals(Constantes.SIN_CONTRATO_SGI)){
				Contrato contrato=new Contrato(identificador);
				archivo.setContrato(contrato);
				
			}else if(entidad.equals(Constantes.INQUILINO_GI)){
				
			}else if(entidad.equals(Constantes.PROCESOJUDICIALUPA_SGI)){
				Procesojudicialupa procesojudicialupa=new Procesojudicialupa(identificador);
				archivo.setProcesojudicialupa(procesojudicialupa);
			}else if(entidad.equals(Constantes.COMPROBANTEPAGO)){
				Comprobantepago  comprobantepago= new Comprobantepago(identificador);
				archivo.setComprobantepago(comprobantepago);
			}
				
			genericDAO.save(archivo);
			
		} catch (Exception e) {
			
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
			
		}
		
	}

		@Override
		public List<ArchivoAdjuntoBean> obtenerArchivosReferencia(int idinmueble,String nombreEntidad) {
			return administrarArchivoDAO.obtenerArchivosReferencia(idinmueble,nombreEntidad);
		}
		@Override
        public void actualizarestadodocumentocontrato(int idcontrato){
			administrarArchivoDAO.actualizarestadodocumentocontrato(idcontrato);
        }
		@Override
        public void actualizarEstadoxDocumentoReferencia(String entidad ,Integer id){
			administrarArchivoDAO.actualizarEstadoxDocumentoReferencia(entidad, id);
        }
		@Override
		public byte[] descargaArchivoSftp(String nombreArchivo,Date fcreacion)throws Exception {
			
			return sftpService.downloadByteArray(nombreArchivo, fcreacion);
		}
		@Override
		public byte[] descargaArchivoSftp(String nombreArchivo,Date fcreacion,String ruta)throws Exception {
			
			return sftpService.downloadByteArray(nombreArchivo, fcreacion,ruta);
		}
		@Override
		public byte[] visualizarArchivoSftp(String nombreArchivo,Date fcreacion)throws Exception {
			
			return sftpService.visualizarByteArray(nombreArchivo, fcreacion);
		}
		@Override
		public byte[] descargaArchivoAlfresco(String uuid) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
}
