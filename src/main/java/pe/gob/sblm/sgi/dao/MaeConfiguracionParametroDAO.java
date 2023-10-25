package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.MaeConfiguracionParametro;

public interface MaeConfiguracionParametroDAO {

	
	public List<MaeConfiguracionParametro> listaConfiguracionParametro(MaeConfiguracionParametro maeConfiguracionParametro)  throws Exception;
;
	public MaeConfiguracionParametro buscarConfiguracionParametroxCondicion(MaeConfiguracionParametro maeConfiguracionParametro,String condicion)throws Exception;
}
