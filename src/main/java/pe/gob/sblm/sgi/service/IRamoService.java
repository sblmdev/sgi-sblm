package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Ramo;

public interface IRamoService {
	public void registrarRamo(Ramo ramo);
	public void actualizarRamo(Ramo ramo);

	public void eliminarRamo(Ramo ramo);
	public List<Ramo> listarRamos();
	
	public Ramo obtenerUltimoRamo();

	public Ramo obtenerRamoPorId(int id);
	int obtenerNumeroRegistros();

}
