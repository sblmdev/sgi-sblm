package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.entity.MaeAccion;


public interface AccionService {

	public List<AccionUsuarioBean> obtenerUsuarioAccion(String nombreAccion);
	public List<AccionUsuarioBean> obtenerUsuarioAccion(int idaccionSeleccionado);
	public List<MaeAccion> obtenerAcciones();
	public void grabarUsuarioAccion(int idusuarioSeleccionado,int idAccionSeleccionado);
	public void eliminarUsuarioAccion(int idusuarioSeleccionado,int idaccionSeleccionado);
	

}
