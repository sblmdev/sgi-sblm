package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.NotificacionBean;
import pe.gob.sblm.sgi.entity.Usuario;

public interface BandejaDAO {


	void actualizarPendienteToRevisado(int idauditoria);

	Object nroNotificacionesDelMes();

	List listarNotificaciones(int i, String mesSeleccionado, String anioSeleccionado);


	Object nroNotificacionesTotal();


	void actualizarPendienteToCancelado(int idauditoria);


	void enviarNotificacion(String contenidoMensajePersonalizado,
			List<Usuario> listadousuariosSeleccionados);


	
	public List<NotificacionBean> obtenerNotificacionesxEstado(boolean estadoLeido, int idusuario);
	public int nroNotificacionesPendiente(int idusuario);
	public int nroNotificacionesRevisado(int idusuario);

	public void cambiarEstadoNotificacion(int idNotificacion);

}
