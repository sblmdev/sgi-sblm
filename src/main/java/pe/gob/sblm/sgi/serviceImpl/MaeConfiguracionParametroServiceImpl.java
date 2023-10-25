package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.MaeConfiguracionParametroDAO;
import pe.gob.sblm.sgi.entity.MaeConfiguracionParametro;
import pe.gob.sblm.sgi.service.MaeConfiguracionParametroService;

@Transactional(readOnly = true)
@Service(value="MaeConfiguracionParametroService")
public class MaeConfiguracionParametroServiceImpl implements MaeConfiguracionParametroService{
	
	@Autowired
	MaeConfiguracionParametroDAO maeConfiguracionParametroDAO;

	public MaeConfiguracionParametroServiceImpl() {
		// TODO Auto-generated constructor stub
		
	}
	@Transactional(readOnly = true)
	@Override
	public List<MaeConfiguracionParametro> listaConfiguracionParametro (
			MaeConfiguracionParametro maeConfiguracionParametro) throws Exception  {
		return maeConfiguracionParametroDAO.listaConfiguracionParametro(maeConfiguracionParametro);
	}
	@Transactional(readOnly = true)
	@Override
	public MaeConfiguracionParametro buscarConfiguracionParametroxCondicion(
			MaeConfiguracionParametro maeConfiguracionParametro,
			String condicion) throws Exception {
		return maeConfiguracionParametroDAO.buscarConfiguracionParametroxCondicion(maeConfiguracionParametro, condicion);
	}

}
