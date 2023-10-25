package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Representante;

public interface IRepresentanteService {

	public void grabarRepresentante(Representante representante);

	public List<Representante> obtenerTodosRepresentantes();

	public void eliminarRepresentante(Representante representante);

	public int nrorepresentantes();

}
