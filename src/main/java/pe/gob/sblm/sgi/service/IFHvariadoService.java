package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Usuario;

public interface IFHvariadoService {

	public List<Perfil> obtenerPerfiles();

	public List<Usuario> obtenerUsuarios();

	public List<Usuario> obtenerCobradores();

	public List<ItemBean> obtenerCobradores(String texto);

	public List<ItemBean> obtenerCobradoresPorNombreExacto(String nombre);

}
