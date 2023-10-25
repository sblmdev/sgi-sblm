package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Tipocambio;

public interface ITipoCambioDAO {


	public Tipocambio obtenerTipoCambio();
	public Tipocambio listarTipoCambioPorId(int id);

	public List<Tipocambio> listarTipoCambios();
	public void registrarTipoCambio(Tipocambio tipoCambio);
	Tipocambio obtenerUltimoTipocambio();
	public Double obtenerTipoCambio(String fechaToString);
	public Tipocambio getTipoCambio();
}
