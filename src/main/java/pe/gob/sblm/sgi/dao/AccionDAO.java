package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.entity.MaeAccion;

public interface AccionDAO {

	public List<AccionUsuarioBean> obtenerUsuarioAccion(String nombreAccion);
	public List<AccionUsuarioBean> obtenerUsuarioAccion(int idaccion);

	public List<MaeAccion> obtenerAcciones();
	public void grabarUsuarioAccion(int idusuario,int idAccion);
	public void eliminarUsuarioAccion(int idusuario, int idaccion);

	



}
