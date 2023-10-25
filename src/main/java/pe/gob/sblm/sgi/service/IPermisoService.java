package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Permiso;

public interface IPermisoService {

	public void registrarPermiso(Permiso permiso);

	public void actualizarPermiso(Permiso permiso);

	public void eliminarPermiso(Permiso permiso);

	public Permiso listarPermisoPorId(int id);

	public List<Permiso> listarPermisos();
}
