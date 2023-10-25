package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.bean.NotificacionBean;
import pe.gob.sblm.sgi.dao.BandejaDAO;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.BandejaService;

@Transactional(readOnly = true)
@Service(value="bandejaService")
public class BandejaServiceImpl implements BandejaService{

	@Autowired
	private BandejaDAO bandejaDAO;

	
	public List listarNotificaciones(int i, String mesSeleccionado,
			String anioSeleccionado) {
		// TODO Auto-generated method stub
		return bandejaDAO.listarNotificaciones(i,mesSeleccionado,anioSeleccionado);
	}

	
	public void actualizarPendienteToRevisado(int idauditoria) {
		bandejaDAO.actualizarPendienteToRevisado(idauditoria);
	}
	
	
	public void actualizarPendienteToCancelado(int idauditoria) {
		bandejaDAO.actualizarPendienteToCancelado(idauditoria);
		
	}


	
	public Object nroNotificacionesDelMes() {
		// TODO Auto-generated method stub
		return bandejaDAO.nroNotificacionesDelMes();
	}

	
	public Object nroNotificacionesTotal() {
		// TODO Auto-generated method stub
		return bandejaDAO.nroNotificacionesTotal();
	}

	
	public void enviarNotificacion(String contenidoMensajePersonalizado,
			List<Usuario> listadousuariosSeleccionados) {
		bandejaDAO.enviarNotificacion(contenidoMensajePersonalizado,listadousuariosSeleccionados);
	}


	@Override
	public List<NotificacionBean> obtenerNotificacionesxEstado(
			boolean estadoLeido, int idusuario) {
		return bandejaDAO.obtenerNotificacionesxEstado(estadoLeido,idusuario);
	}


	@Override
	public int nroNotificacionesPendiente(int idusuario) {
//		List<String> lista = null;
//		
//		lista.add("");
 		
		return bandejaDAO.nroNotificacionesPendiente(idusuario);
	}


	@Override
	public int nroNotificacionesRevisado(int idusuario) {
		return bandejaDAO.nroNotificacionesRevisado(idusuario);
	}


	@Override
	public void cambiarEstadoNotificacion(int idNotificacion) {
		bandejaDAO.cambiarEstadoNotificacion(idNotificacion);
		
	}
	
	

}
