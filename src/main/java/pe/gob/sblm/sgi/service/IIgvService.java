package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Igv;

public interface IIgvService {
	public void registrarIgv(Igv igv);
	public void actualizarIgv(Igv igv);

	public void eliminarIgv(Igv igv);
	public List<Igv> listarIgvs();
	
	public Igv obtenerUltimoIgv();

	public Igv obtenerIgvPorId(int id);
	int obtenerNumeroRegistros();

}
