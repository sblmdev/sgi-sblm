package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Uso;

public interface IUsoDAO {
	public void registrarUso(Uso uso);
	public void actualizarUso(Uso uso);

	public void eliminarUso(Uso uso);
	public List<Uso> listarUsos();
	
	public Uso obtenerUltimoUso();

	public Uso obtenerUsoPorId(int id);
	int obtenerNumeroRegistros();
}
