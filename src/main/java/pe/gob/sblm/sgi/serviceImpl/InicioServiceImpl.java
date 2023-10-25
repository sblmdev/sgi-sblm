package pe.gob.sblm.sgi.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.sgi.bean.ParamConfigAlfrescoBean;
import pe.gob.sblm.sgi.bean.ParamConfigEmailBean;
import pe.gob.sblm.sgi.bean.ParamConfigSftpBean;
import pe.gob.sblm.sgi.entity.MaeConfiguracionServicio;
import pe.gob.sblm.sgi.service.ConfiguracionServicioService;
import pe.gob.sblm.sgi.service.InicioService;
import pe.gob.sblm.sgi.util.Almanaque;

@Transactional(readOnly = true)
@Service(value="inicioService")
public class InicioServiceImpl implements InicioService {
	
	private static final Logger Logger = LoggerFactory.getLogger(InicioServiceImpl.class);
	
	private ParamConfigAlfrescoBean paramConfigAlfrescoBean;
	private ParamConfigEmailBean paramConfigEmailBean;
	private ParamConfigSftpBean paramConfigSftpBean;
		
	@Autowired
	ConfiguracionServicioService configuracionServicioService;
	
	private List<String> listaMeses;
	private List<Integer> listaAnio;
	private List<Integer> listaAnioDesc;
	
	
	@PostConstruct
	public void init() throws Exception{
		
		cargarDatosAyuda();
		cargarParametrosConfigServicios();
	}
	
	
	
	public void cargarDatosAyuda(){
		
		listaMeses= new ArrayList<String>();
		listaAnio =new ArrayList<Integer>();
		listaAnioDesc =new ArrayList<Integer>();
		
		 for(int i=0;i<12;i++){
			 listaMeses.add(Almanaque.obtenerNombreMes(i));
		 }
		
		 Integer anioActual=Integer.parseInt(FechaUtil.getYear(new Date()));
		
		 
		 
		 for (int i = anioActual+60; i > 1970; i--) {
			 listaAnioDesc.add(i);
		 }
		 
		 for (int i = 1970; i < anioActual+60; i++) {
			listaAnio.add(i);
		 }						
	}

	private void cargarParametrosConfigServicios()throws Exception{
		try {
			
			MaeConfiguracionServicio maeConfiguracionServicio = new MaeConfiguracionServicio();
			maeConfiguracionServicio.setLActivo(Constantes.ACTIVO);
			/**
	    	 * Obtenemos los datos de la configuracion de alfresco
	    	 * */
			
			maeConfiguracionServicio.setCTabla(Constantes.CODIGO_TABLA_ALFRESCO);
	    	
	    	List<MaeConfiguracionServicio> listConfigAlfresco = configuracionServicioService.listaConfiguracionesPorTabla(maeConfiguracionServicio);

	    	if(CollectionUtils.isNotEmpty(listConfigAlfresco)){
	    		paramConfigAlfrescoBean = cargarConfigAlfresco(listConfigAlfresco);
	    		System.out.println(paramConfigAlfrescoBean.toString());
	    	}
	    	
			/**
	    	 * Obtenemos los datos de la configuracion de SFTP
	    	 * */
			
			maeConfiguracionServicio.setCTabla(Constantes.CODIGO_TABLA_SFTP);
	    	
	    	List<MaeConfiguracionServicio> listConfigSFTP = configuracionServicioService.listaConfiguracionesPorTabla(maeConfiguracionServicio);
	    	

	    	if(CollectionUtils.isNotEmpty(listConfigSFTP)){
	    		
	    		paramConfigSftpBean = cargarConfigSftp(listConfigSFTP);
	    	}
	    	
	    	
	    	/**
	    	 * Obtenemos los datos de configuracion para el envio de correos
	    	 * */
//	    	maeConfiguracionServicio.setCTabla( Constantes.CODIGO_TABLA_EMAIL);
//	    	
//	    	List<MaeConfiguracionServicio> listConfigEmail = configuracionServicioService.listaConfiguracionesPorTabla(MaeConfiguracionServicio);
//
//	    	if(CollectionUtils.isNotEmpty(listConfigPdf)){
//	    		paramConfigEmailBean = cargarConfigEmail(listConfigEmail);
//	    	}
	    	
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
	
	}

	
	private ParamConfigAlfrescoBean cargarConfigAlfresco(List<MaeConfiguracionServicio> listaConfiguracionServicioBean) throws Exception{
		
		ParamConfigAlfrescoBean bean = new ParamConfigAlfrescoBean();
		
		try{
			for (MaeConfiguracionServicio maeConfiguracionServicio : listaConfiguracionServicioBean) {
				
				if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_USER_ALFRESCO)) {
					bean.setUser(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_PASS_ALFRESCO)) {
					bean.setPass(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_HOST_ALFRESCO)) {
					bean.setHost(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_PORT_ALFRESCO)) {
					bean.setPort(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_ATOMPUB_ALFRESCO)) {
					bean.setAtompub(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_ROOT_ALFRESCO)) {
					bean.setRoot(maeConfiguracionServicio.getXValor());
				}
					
			}
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		
		return bean;
	}
	
	private ParamConfigSftpBean cargarConfigSftp(List<MaeConfiguracionServicio> listaConfiguracionServicioBean) throws Exception{
		
		ParamConfigSftpBean bean = new ParamConfigSftpBean();
		
		try{
			
			for (MaeConfiguracionServicio maeConfiguracionServicio : listaConfiguracionServicioBean) {
				
				if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_USER_SFTP)) {
					bean.setUser(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_PASS_SFTP)) {
					bean.setPass(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_HOST_SFTP)) {
					bean.setHost(maeConfiguracionServicio.getXValor());
				}else if (maeConfiguracionServicio.getCParametro().equals(Constantes.PARAM_CONFIG_PORT_SFTP)) {
					bean.setPort(maeConfiguracionServicio.getXValor());
				}
			}
			
		}catch(Exception e){
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		
		return bean;
	}


	/**
	 * @return the listaMeses
	 */
	public List<String> getListaMeses() {
		return listaMeses;
	}




	/**
	 * @param listaMeses the listaMeses to set
	 */
	public void setListaMeses(List<String> listaMeses) {
		this.listaMeses = listaMeses;
	}




	/**
	 * @return the listaAnio
	 */
	public List<Integer> getListaAnio() {
		return listaAnio;
	}




	/**
	 * @param listaAnio the listaAnio to set
	 */
	public void setListaAnio(List<Integer> listaAnio) {
		this.listaAnio = listaAnio;
	}




	/**
	 * @return the paramConfigAlfrescoBean
	 */
	public ParamConfigAlfrescoBean getParamConfigAlfrescoBean() {
		return paramConfigAlfrescoBean;
	}




	/**
	 * @param paramConfigAlfrescoBean the paramConfigAlfrescoBean to set
	 */
	public void setParamConfigAlfrescoBean(
			ParamConfigAlfrescoBean paramConfigAlfrescoBean) {
		this.paramConfigAlfrescoBean = paramConfigAlfrescoBean;
	}




	/**
	 * @return the paramConfigEmailBean
	 */
	public ParamConfigEmailBean getParamConfigEmailBean() {
		return paramConfigEmailBean;
	}




	/**
	 * @param paramConfigEmailBean the paramConfigEmailBean to set
	 */
	public void setParamConfigEmailBean(ParamConfigEmailBean paramConfigEmailBean) {
		this.paramConfigEmailBean = paramConfigEmailBean;
	}




	/**
	 * @return the paramConfigSftpBean
	 */
	public ParamConfigSftpBean getParamConfigSftpBean() {
		return paramConfigSftpBean;
	}




	/**
	 * @param paramConfigSftpBean the paramConfigSftpBean to set
	 */
	public void setParamConfigSftpBean(ParamConfigSftpBean paramConfigSftpBean) {
		this.paramConfigSftpBean = paramConfigSftpBean;
	}



	public List<Integer> getListaAnioDesc() {
		return listaAnioDesc;
	}



	public void setListaAnioDesc(List<Integer> listaAnioDesc) {
		this.listaAnioDesc = listaAnioDesc;
	}
	

	
}
