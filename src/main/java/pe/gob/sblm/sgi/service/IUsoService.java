package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Uso;

public interface IUsoService {
	public void registrarUso(Uso uso);
	public void actualizarUso(Uso uso);

	public void eliminarUso(Uso uso);
	public List<Uso> listarUsos();
	
	public Uso obtenerUltimoUso();

	public Uso obtenerUsoPorId(int id);
	int obtenerNumeroRegistros();

	
}
