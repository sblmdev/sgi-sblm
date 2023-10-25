package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Tipocambio;

public interface ITipoCambioService {

	public void registrarTipoCambio(Tipocambio tipoCambio);

	public Tipocambio obtenerTipoCambio();

	
	public Tipocambio listarTipoCambioPorId(int id);

	public List<Tipocambio> listarTipoCambios();

	Tipocambio obtenerUltimoTipocambio();

	public Double obtenerTipoCambio(String fechaToString);
	
	public Tipocambio  getTipoCambio();
}
