package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.InquilinoBean;
import pe.gob.sblm.sgi.dao.IInquilinoDAO;
import pe.gob.sblm.sgi.dao.TipoPersonaDAO;
import pe.gob.sblm.sgi.entity.Califica;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Tipoentidad;
import pe.gob.sblm.sgi.entity.Tipopersona;
import pe.gob.sblm.sgi.service.IInquilinoService;
@Transactional(readOnly = true)
@Service(value="inquilinoService")
public class InquilinoService implements IInquilinoService{
	
	@Autowired
	private IInquilinoDAO inquilinoDAO;
	
	@Autowired
	private TipoPersonaDAO tipopersonaDAO;
	

	@Transactional(readOnly = false)
	
	public void registrarInquilino(Inquilino inquilino) {
		getInquilinoDAO().registrarInquilino(inquilino);
		
	}
	@Transactional(readOnly = false)
	
	public void actualizarInquilino(Inquilino inquilino) {
		getInquilinoDAO().actualizarInquilino(inquilino);
		
	}
	@Transactional(readOnly = false)
	
	public void eliminarInquilino(Inquilino inquilino) {
		getInquilinoDAO().eliminarInquilino(inquilino);
		
	}
	
	public List<Califica> listarCalificacion(Inquilino inqui) {
		return getInquilinoDAO().listarCalificacion(inqui);
	}
	@Transactional(readOnly = false)
	
	public void eliminarCalificacion(Inquilino inquilino) {
		getInquilinoDAO().eliminarCalificacion(inquilino);
		
	}
	
	public Inquilino obtenerUltimoInquilino() {
		return getInquilinoDAO().obtenerUltimoInquilino();
	}
	
	public Inquilino validarInquilinoRepetido(String dni,Integer idtipopersona) {
		return getInquilinoDAO().validarInquilinoRepetido(dni,idtipopersona);
	}
	public int obtenerNumeroRegistros() {
		// TODO Auto-generated method stub
		return getInquilinoDAO().obtenerNumeroRegistros();
	}

	
	public List<Tipoentidad> listarTipoEntidad() {
		// TODO Auto-generated method stub
		return getInquilinoDAO().listarTipoEntidad();
	}
	
	public List<Califica> listarCalificacion() {
		// TODO Auto-generated method stub
		return getInquilinoDAO().listarCalificacion();
	}
	
	
	public List<Tipopersona> listarTipoPersona() throws Exception {
		// TODO Auto-generated method stub
		return tipopersonaDAO.listarTipoPersona();
	}
	
	
	public void registrarCalificacion(Califica calificacion) {
		getInquilinoDAO().registrarCalificacion(calificacion);
		
	}
	public IInquilinoDAO getInquilinoDAO() {
		return inquilinoDAO;
	}

	public void setInquilinoDAO(IInquilinoDAO inquilinoDAO) {
		this.inquilinoDAO = inquilinoDAO;
	}
	@Override
	public List<Inquilino> listarInquilinos(String valorbusqueda,String tipoBusqueda) {
		return getInquilinoDAO().listarInquilinos(valorbusqueda,tipoBusqueda);
	}
	@Override
	public List<InquilinoBean> listaInquilinos(InquilinoBean inq) {
		// TODO Auto-generated method stub
		return getInquilinoDAO().listarInquilino(inq);
	}
	

}
