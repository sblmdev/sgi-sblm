package pe.gob.sblm.sgi.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.dao.NotificarServiceDAO;
import pe.gob.sblm.sgi.entity.MaeNotificacion;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.AccionService;
import pe.gob.sblm.sgi.service.NotificarService;

@Service(value="notificarService")
public class NotificarServiceImpl implements NotificarService{
	
	@Autowired
	private NotificarServiceDAO notificarServiceDAO;

	@Autowired
	AccionService accionService;
	
	@Override
	public void notificarNuevoRegistroCondicion(String clave, String condicion) {
		
		List<AccionUsuarioBean> lista = null;
		
		if (condicion.equals(Constantes.CONDICION_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_CREAR_CONTRATO);
			
		}else if (condicion.equals(Constantes.CONDICION_SIN_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_CREAR_SIN_CONTRATO);
			
		}else if (condicion.equals(Constantes.CONDICION_PRE_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_CREAR_PRE_CONTRATO);
		
		}else if (condicion.equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_CREAR_RECONOCIMIENTO_DEUDA);
		
		}
		
		for (AccionUsuarioBean accionUsuarioBean : lista) {
			
			MaeNotificacion maeNotificacion=new MaeNotificacion();
			maeNotificacion.setEstadoLeido(false);
			maeNotificacion.setUidAlfresco(null);
			maeNotificacion.setFechaCreacion(new Date());
			maeNotificacion.setUsuarioCreador("SGI");
			maeNotificacion.setMensaje(accionUsuarioBean.getDescripcionAccion()+clave);
			maeNotificacion.setUsuariodestino(new Usuario(accionUsuarioBean.getIdusuario()));
			maeNotificacion.setEstadoCorreoEnviado(false);
			maeNotificacion.setTitulo(accionUsuarioBean.getNombreAccion());
			maeNotificacion.setSiNotificacionTipoReporte(false);
			
			notificarServiceDAO.notificarNuevoRegistroContrato(maeNotificacion);
			
		}
	
	}

	@Override
	public void notificarActualizacionCondicion(String clave, String condicion) {
		List<AccionUsuarioBean> lista = null;
		
		if (condicion.equals(Constantes.CONDICION_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_ACTUALIZAR_CONTRATO);
			
		}else if (condicion.equals(Constantes.CONDICION_SIN_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_ACTUALIZAR_SIN_CONTRATO);
			
		}else if (condicion.equals(Constantes.CONDICION_PRE_CONTRATO)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_ACTUALIZAR_PRE_CONTRATO);
		
		}else if (condicion.equals(Constantes.CONDICION_RECONOCIMIENTO_DEUDA)) {
			
			lista=accionService.obtenerUsuarioAccion(Constantes.ACCION_ACTUALIZAR_RECONOCIMIENTO_DEUDA);
		}
		
		for (AccionUsuarioBean accionUsuarioBean : lista) {
			
			MaeNotificacion maeNotificacion=new MaeNotificacion();
			maeNotificacion.setEstadoLeido(false);
			maeNotificacion.setUidAlfresco(null);
			maeNotificacion.setFechaCreacion(new Date());
			maeNotificacion.setUsuarioCreador("SGI");
			maeNotificacion.setMensaje(accionUsuarioBean.getDescripcionAccion()+clave);
			maeNotificacion.setUsuariodestino(new Usuario(accionUsuarioBean.getIdusuario()));
			maeNotificacion.setEstadoCorreoEnviado(false);
			maeNotificacion.setTitulo(accionUsuarioBean.getNombreAccion());
			maeNotificacion.setSiNotificacionTipoReporte(false);
			notificarServiceDAO.notificarNuevoRegistroContrato(maeNotificacion);
			
		}
		
	}


}
