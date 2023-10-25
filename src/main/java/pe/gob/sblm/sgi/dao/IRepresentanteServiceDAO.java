package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Representante;

public interface IRepresentanteServiceDAO {

	void grabarRepresentante(Representante representante);

	List<Representante> obtenerTodosRepresentantes();

	void eliminarRepresentante(Representante representante);

	int nrorepresentantes();

}
