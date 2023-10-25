package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IAlertaDAO;
import pe.gob.sblm.sgi.entity.Alerta;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAlertaService;

@Transactional(readOnly = true)
@Service(value="alertaService")
public class AlertaService implements IAlertaService {
	@Autowired
	private IAlertaDAO alertaDAO;
	
	@Transactional(readOnly = false)
	
	public void registrarAlerta(Alerta alerta) {
		getAlertaDAO().registrarAlerta(alerta);
		 
	}

	
	public void actualizarAlerta(Alerta alerta) {
		
	}

	
	public void eliminarAlerta(Alerta alerta) {
		getAlertaDAO().eliminarAlerta(alerta);
		
	}
	
	public Usuario obtenerUsuarioxNombreUsuario(String nombre) {
		return getAlertaDAO().obtenerUsuarioxNombreUsuario(nombre); 
	}
	
	
	public List<Alerta> listarAlertas() {
		return getAlertaDAO().listarAlertas();
	}

	public IAlertaDAO getAlertaDAO() {
		return alertaDAO;
	}

	public void setAlertaDAO(IAlertaDAO alertaDAO) {
		this.alertaDAO = alertaDAO;
	}

	
	public List<Alerta> alertasNoenviadas() {
		// TODO Auto-generated method stub
		return alertaDAO.alertasNoenviadas();
	}

	
	public void enviarNotificacion(int idalerta, String mensaje, int idusuario,int idusuarioenvio) {
		alertaDAO.enviarNotificacion(idalerta,mensaje,idusuario,idusuarioenvio);
		
	}

	
	public void actualizarAenviado(int idalerta) {
		alertaDAO.actualizarAenviado(idalerta);
		
	}

}
