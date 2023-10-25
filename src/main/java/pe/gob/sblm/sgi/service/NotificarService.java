package pe.gob.sblm.sgi.service;



public interface NotificarService {

	public void notificarNuevoRegistroCondicion(String clave, String condicion);

	public void notificarActualizacionCondicion(String clave, String condicion);


}
