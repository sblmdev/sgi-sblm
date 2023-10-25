package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Institucion;

public interface IInstitucionServiceDAO {

	void grabarInstitucion(Institucion institucion);

	List<Institucion> obtenerTodosInstituciones();

	void eliminarInstitucion(Institucion institucion);

	int nroInstituciones();

}
