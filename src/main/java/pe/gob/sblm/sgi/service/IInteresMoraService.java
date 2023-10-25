package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.InteresMora;
import pe.gob.sblm.sgi.entity.Tipointeresmora;

public interface IInteresMoraService {

	public void grabarInteresMora(InteresMora interesmora);
	public List<InteresMora> listarInteresMora(String tipointeres);
	public InteresMora obtenerInteresMora(String tipointeres,Integer idinteres,String fecha);
    public InteresMora obtenerUltimoInteresMora(String tipointeres);
    public Tipointeresmora obtenerTipoInteresMora(String tipointeres);
    public List<Tipointeresmora> listarTipoInteresMora();
	
	
	
}
