package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Alerta;
import pe.gob.sblm.sgi.entity.Usuario;

public interface IAlertaService {
	public void registrarAlerta(Alerta alerta);
	public void actualizarAlerta(Alerta alerta);

	public void eliminarAlerta(Alerta alerta);
	public List<Alerta> listarAlertas();
	public Usuario obtenerUsuarioxNombreUsuario(String usu);
	public List<Alerta> alertasNoenviadas();
	public void enviarNotificacion(int idalerta, String mensaje, int idusuario, int idusuarioenvio);
	public void actualizarAenviado(int idalerta);
	
}
