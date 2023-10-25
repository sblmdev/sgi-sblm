package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.ParamConfigAlfrescoBean;
import pe.gob.sblm.sgi.bean.ParamConfigSftpBean;


public interface InicioService {

	public List<String> getListaMeses();
	public List<Integer> getListaAnio();
	public List<Integer> getListaAnioDesc();
	public ParamConfigSftpBean getParamConfigSftpBean();
	public ParamConfigAlfrescoBean getParamConfigAlfrescoBean();
}
