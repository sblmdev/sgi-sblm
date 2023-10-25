package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.InquilinoBean;
import pe.gob.sblm.sgi.entity.Califica;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Tipoentidad;

public interface IInquilinoDAO {
	public void registrarInquilino(Inquilino inquilino);
	public void actualizarInquilino(Inquilino inquilino);

	public void eliminarInquilino(Inquilino inquilino);
	
	public Inquilino obtenerUltimoInquilino();

	int obtenerNumeroRegistros();
	/**lista tipoentidad**/
	List<Tipoentidad> listarTipoEntidad();
	/**lista tipoentidad**/
	public Inquilino validarInquilinoRepetido(String dni,Integer idtipopersona);
	List<Califica> listarCalificacion();
	void registrarCalificacion(Califica calificacion);
	List<Califica> listarCalificacion(Inquilino inqui);
	void eliminarCalificacion(Inquilino inquilino);
	public List<Inquilino> listarInquilinos(String valorbusqueda,String tipoBusqueda);
	public List<InquilinoBean> listarInquilino(InquilinoBean inq);
}
