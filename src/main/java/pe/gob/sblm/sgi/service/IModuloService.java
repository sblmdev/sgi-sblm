package pe.gob.sblm.sgi.service;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilmodulo;

public interface IModuloService {
	public void registrarModulo(Modulo modulo);

	public void actualizarModulo(Modulo modulo);

	public void eliminarModulo(Modulo modulo);

	public Modulo listarModuloPorId(int id);

	public List<Modulo> listarModulos();
	
	public int obtenerNumeroModulos();
	public String obtenerUltimoModulo();
	public Date obtenerFechaUltimoModulo();

	Modulo obtenerUltimoModulocreado();

	void registrarPaginamodulo(Paginamodulo paginamodulo);


	List<Pagina> listarPaginasDeModulos(int idmodulo);

	List<Pagina> listarPaginas();

	void eliminarPaginaModulo(int idmodulo);

	public List<Perfilmodulo> verficarModuloEnPerfil(int idmodulo);
	
	public Integer obtenerIdPaginaModulo(int idmodulo ,int idpagina );

	public Perfil  listarPerfilPorId(int idperfil);
	

}
