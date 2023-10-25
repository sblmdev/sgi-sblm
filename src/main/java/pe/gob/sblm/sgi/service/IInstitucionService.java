package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Institucion;

public interface IInstitucionService {

	public void grabarInstitucion(Institucion institucion);

	public List<Institucion> obtenerTodosInstituciones();

	public void eliminarInstitucion(Institucion institucion);

	public int nroInstituciones();

}
