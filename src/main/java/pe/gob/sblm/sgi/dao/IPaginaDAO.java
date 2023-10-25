package pe.gob.sblm.sgi.dao;

import java.util.Date;
import java.util.List;

import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Paginamodulo;

public interface IPaginaDAO {

	public void registrarPagina(Pagina pagina);

	public void actualizarPagina(Pagina pagina);

	public void eliminarPagina(Pagina pagina);


	public List<Pagina> listarPaginas();
	public List<Paginamodulo> listarPaginasModulos();
	public int obtenerNumeroPaginas();
	public String obtenerUltimaPagina();
	public Date obtenerFechaUltimaPagina();
	public void registrarPaginamodulo(Paginamodulo paginamodulo);
	public Pagina obtenerUltimaPaginaCreada();
	
	public List<Paginamodulo> listarPaginamodulos();

	void actualizarPaginamodulo(Paginamodulo paginamodulo);

	void eliminarPaginamodulo(Paginamodulo paginamodulo);

	Pagina verificarPaginaEnModulo(String descripcionpagina,String nombrepagina, int idmodulo, int idpagina);
}
